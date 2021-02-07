package molecule.tests.core.generic

import molecule.core.exceptions.MoleculeException
import molecule.core.util.SystemDevLocal
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out5._
import molecule.TestSpec
import molecule.datomic.base.util.{SystemDevLocal, SystemPeer, SystemPeerServer}


class Index extends TestSpec {

  class Setup extends CoreSetup {
    // Generally use `t` or `tx` to identify transaction and `txInstant` only to get
    // the wall clock time since Date's are a bit unreliable for precision.

    // First entity

    val txR1 = Ns.str("a").int(1).save
    val tx1  = txR1.tx
    val e1   = txR1.eid
    val t1   = txR1.t
    val d1   = txR1.inst

    val txR2 = Ns(e1).str("b").update
    val tx2  = txR2.tx
    val t2   = txR2.t
    val d2   = txR2.inst

    val txR3 = Ns(e1).int(2).update
    val tx3  = txR3.tx
    val t3   = txR3.t
    val d3   = txR3.inst


    // Second entity

    val txR4 = Ns.str("x").int(4).save
    val tx4  = txR4.tx
    val e2   = txR4.eid
    val t4   = txR4.t
    val d4   = txR4.inst

    val txR5 = Ns(e2).int(5).update
    val tx5  = txR5.tx
    val t5   = txR5.t
    val d5   = txR5.inst


    // Relationship

    val txR6 = Ref1.str1("hello").save
    val tx6  = txR6.tx
    val t6   = txR6.t
    val d6   = txR6.inst
    val e3   = txR6.eid

    // e2 points to e3
    val txR7 = Ns(e2).ref1(e3).update
    val tx7  = txR7.tx
    val t7   = txR7.t
    val d7   = txR7.inst


    // Cardinality-many attributes

    // 6, 7, 8
    val txR8 = Ns.ints(6, 7, 8).save
    val t8   = txR8.t
    val e4   = txR8.eid

    // 6, 70, 80
    val t9 = Ns(e4).ints.replace(7 -> 70, 8 -> 80).update.t

    // 70, 80
    val t10 = Ns(e4).ints.retract(6).update.t

    // 70, 80, 90
    val t11 = Ns(e4).ints.assert(60).update.t


    // e2 now points to e4
    val txR12 = Ns(e2).ref1(e4).update
    val t12   = txR12.t

    // e1 also points to e4
    val txR13 = Ns(e2).refs1(e4).update
    val t13   = txR13.t

    // Inline descriptions respectfully borrowed from the manual:
    // https://docs.datomic.com/on-prem/indexes.html
  }


  "All Datoms of Index" in new Setup {

    system match {
      case SystemPeer =>
        EAVT.a.get.size === 709
        AEVT.a.get.size === 709
        VAET.a.get.size === 349
        AVET.a.get.size === 220

      case SystemDevLocal =>
        EAVT.a.get.size === 569
        AEVT.a.get.size === 569
        VAET.a.get.size === 317
        AVET.a.get.size === 569

      case _ => // Peer Server (growing across tests, so we can't test deterministically here)
    }
  }

  "EAVT" >> {

    // The EAVT index provides efficient access to everything about a given entity.
    // Conceptually this is very similar to row access style in a SQL database, except
    // that entities can possess arbitrary attributes rather then being limited to
    // a predefined set of columns.

    // Note that Datomic sorts the attribute id, not the name/ident,
    // so we don't get alphabetic order by attribute name. Attributes are though
    // still grouped together.


    "Current values" in new Setup {

      // EAVT datom values of entity e1
      EAVT(e1).e.a.v.t.get.sortBy(_._4) === List(
        (e1, ":Ns/str", "b", t2),
        (e1, ":Ns/int", 2, t3),
      )

      // Freely order generic attributes as you like
      EAVT(e1).t.v.e.a.get.sortBy(_._1) === List(
        (t2, "b", e1, ":Ns/str"),
        (t3, 2, e1, ":Ns/int")
      )

      // No need to return the entity id that we filter by
      EAVT(e1).a.v.t.get.sortBy(_._3) === List(
        (":Ns/str", "b", t2),
        (":Ns/int", 2, t3)
      )

      // Attributes of entity e1
      EAVT(e1).a.get.sorted === List(":Ns/int", ":Ns/str")

      // Values of e1
      EAVT(e1).v.get === List("b", 2)

      // Transaction Ts of e1
      EAVT(e1).t.get === List(t2, t3)

      // Transaction entities of e1
      EAVT(e1).tx.get === List(tx2, tx3)

      // Transaction times of e1
      EAVT(e1).txInstant.get === List(d2, d3)

      // Operations of e1
      // Since the current database as of now will only have asserted values
      // this questions will always yield true and therefore not be interesting to query
      EAVT(e1).op.get === List(true, true)
    }


    "History values" in new Setup {

      if (system != SystemPeerServer) {
        // History of attribute values of entity e1
        // Generic attribute `op` is interesting when looking at the history database since
        // it tells wether datoms were asserted (true) or retracted (false).
        // NOTE that retracted datoms take precedence for the same EAV values, meaning that
        // the transaction value is sorted after the operation (so he index seems actually sorted by
        // all 5 datom elements EAVOpT and not only EAVT)
        EAVT(e1).e.a.v.t.op.getHistory.sortBy(p => (p._2, p._4, p._5)) === List(
          (e1, ":Ns/int", 1, t1, true),
          (e1, ":Ns/int", 1, t3, false),
          (e1, ":Ns/int", 2, t3, true),
          (e1, ":Ns/str", "a", t1, true),
          (e1, ":Ns/str", "a", t2, false),
          (e1, ":Ns/str", "b", t2, true),
        )

        // History of attribute :Ns/int values of entity e1
        // or
        // "What values has attribute :Ns/int of entity e1 had over time?"
        // - 1 was asserted in transaction t1, retracted in t3, and new value 2 asserted in t3
        EAVT(e1, ":Ns/int").e.a.v.t.op.getHistory.sortBy(p => (p._4, p._5)) === List(
          (e1, ":Ns/int", 1, t1, true),
          (e1, ":Ns/int", 1, t3, false),
          (e1, ":Ns/int", 2, t3, true),
        )

        // History of attribute :Ns/int value being 1 of entity e1
        // or
        // "What happened to entity e1's attribute :Ns/int value 1?"
        // - 1 was asserted in transaction t1 and then retracted in t3
        EAVT(e1, ":Ns/int", 1).e.a.v.t.op.getHistory.sortBy(p => (p._4, p._5)) === List(
          (e1, ":Ns/int", 1, t1, true),
          (e1, ":Ns/int", 1, t3, false),
        )

        // History of attribute :Ns/int value being 1 of entity e1 in transaction t3
        // or
        // "Was entity e1's attribute :Ns/int value 1 in transaction t1 asserted or retracted?"
        // - 1 was asserted in transaction t1
        EAVT(e1, ":Ns/int", 1, t1).e.a.v.t.op.getHistory === List(
          (e1, ":Ns/int", 1, t1, true)
        )
      }
    }


    "Card many" in new Setup {

      // Each value is asserted/retracted on its own
      EAVT(e4).a.v.t.get === List(
        (":Ns/ints", 60, t11),
        (":Ns/ints", 70, t9),
        (":Ns/ints", 80, t9)
      )

      EAVT(e4).a.v.t.op.getHistory === List(
        (":Ns/ints", 6, t10, false),
        (":Ns/ints", 6, t8, true),

        (":Ns/ints", 7, t9, false),
        (":Ns/ints", 7, t8, true),

        (":Ns/ints", 8, t9, false),
        (":Ns/ints", 8, t8, true),

        (":Ns/ints", 60, t11, true),
        (":Ns/ints", 70, t9, true),
        (":Ns/ints", 80, t9, true),
      )
    }


    "Datom args" in new Setup {

      // Applying values to Index attributes not allowed
      expectCompileError(
        "m(EAVT(42L).e.a.v(500).t)",
        "molecule.core.transform.exception.Dsl2ModelException: " +
          "EAVT index attributes not allowed to have values applied.\n" +
          "EAVT index only accepts datom arguments: `EAVT(<e/a/v/t>)`.")
    }
  }


  "AEVT" >> {

    "Args" in new Setup {

      // The AEVT index provides efficient access to all values for a given attribute,
      // comparable to traditional column access style.

      // Attribute :Ns/int's entities, values and transactions
      AEVT(":Ns/int").e.v.t.get.sortBy(_._3) === List(
        (e1, 2, t3),
        (e2, 5, t5)
      )

      // Entities having :Ns/int asserted
      AEVT(":Ns/int").e.get.sorted === List(e1, e2).sorted

      // All values of attribute :Ns/int
      AEVT(":Ns/int").v.get.map(_.asInstanceOf[Long]).sorted === List(2, 5)

      // All transactions where attribute :Ns/int is asserted
      AEVT(":Ns/int").t.get.sorted === List(t3, t5)


      // Attribute :Ns/int of entity e1's value and transaction
      AEVT(":Ns/int", e1).e.v.t.get === List(
        (e1, 2, t3)
      )

      // Attribute :Ns/int of entity e1 with value 2's transaction
      AEVT(":Ns/int", e1, 2).e.v.t.get === List(
        (e1, 2, t3)
      )

      // Attribute :Ns/int of entity e1 with value 2 in transaction t3
      AEVT(":Ns/int", e1, 2, t3).e.v.t.get === List(
        (e1, 2, t3)
      )

      // Attribute :Ns/int's historic entities, values and transactions
      if (system != SystemPeerServer)
        AEVT(":Ns/int").e.v.t.op.getHistory.sortBy(p => (p._3, p._4)) === List(
          (e1, 1, t1, true),
          (e1, 1, t3, false),
          (e1, 2, t3, true),
          (e2, 4, t4, true),
          (e2, 4, t5, false),
          (e2, 5, t5, true),
        )
    }


    "Only mandatory datom args" in new Setup {

      // Applying values to Index attributes not allowed
      expectCompileError(
        """m(AEVT(":Ns/int").a.e.v(42).t)""",
        "molecule.core.transform.exception.Dsl2ModelException: " +
          "AEVT index attributes not allowed to have values applied.\n" +
          "AEVT index only accepts datom arguments: `AEVT(<a/e/v/t>)`.")
    }
  }


  "AVET" >> {

    "Basics" in new Setup {

      // The AVET index provides efficient access to particular combinations of attribute and value.

      // Which entities in what transactions have attribute :Ns/int asserted with value 2?
      AVET(":Ns/int", 2).e.t.get === List(
        (e1, t3)
      )

      AVET(":Ns/int", 2, e1).t.get === List(t3)

      AVET(":Ns/int", 2, e1, t3).op.get === List(true)

      // History of entities with attribute :Ns/int having value 4
      if (system != SystemPeerServer) {
        AVET(":Ns/int", 4).e.t.op.getHistory === List(
          (e2, t5, false),
          (e2, t4, true)
        )

        AEVT(":Ns/int").v.e.t.op.getHistory.sortBy(p => (p._3, p._4)) === List(
          (1, e1, t1, true),
          (1, e1, t3, false),
          (2, e1, t3, true),
          (4, e2, t4, true),
          (4, e2, t5, false),
          (5, e2, t5, true),
        )
      }
    }


    "Only mandatory datom args" in new Setup {

      // Applying values to Index attributes not allowed
      expectCompileError(
        """m(AVET(":Ns/int").a.v.e(77L).t)""",
        "molecule.core.transform.exception.Dsl2ModelException: " +
          "AVET index attributes not allowed to have values applied.\n" +
          "AVET index only accepts datom arguments: `AVET(<a/v/e/t>)` or range arguments: `AVET.range(a, from, until)`.")
    }
  }


  "AVET Index range" >> {

    "Basics" in new Setup {

      // Apply attribute name and `from` + `until` value range arguments

      // Datoms with attribute :Ns/int having a value between 2 until 6 (not included)
      AVET.range(":Ns/int", Some(2), Some(6)).v.e.t.get === List(
        (2, e1, t3),
        (5, e2, t5)
      )
    }


    "Arg combinations" in new Setup {

      // `until` arg 5 is not included
      AVET.range(":Ns/int", Some(2), Some(5)).e.get === List(e1)

      // Both 2 and 5 matched
      AVET.range(":Ns/int", Some(2), Some(6)).e.get === List(e1, e2)

      // Only 5 matched
      AVET.range(":Ns/int", Some(3), Some(6)).e.get === List(e2)


      // 2 to end (2 included)
      AVET.range(":Ns/int", Some(2), None).e.get === List(e1, e2)

      // 3 to end (2 not included)
      AVET.range(":Ns/int", Some(3), None).e.get === List(e2)

      // 6 to end (2 and 5 not included)
      AVET.range(":Ns/int", Some(6), None).e.get === Nil


      // Start until 5 (5 not included)
      AVET.range(":Ns/int", None, Some(5)).e.get === List(e1)

      // Start until 6 (5 included)
      AVET.range(":Ns/int", None, Some(6)).e.get === List(e1, e2)

      // Start until 2 (2 and 5 not included)
      AVET.range(":Ns/int", None, Some(2)).e.get === Nil


      // Start - end
      // Molecule disallow returning from beginning to end (the whole database!)
      AVET.range(":Ns/int", None, None).e.get === List(e1, e2)
    }


    "Arg types" in new Setup {

      // Different range types throw an exception
      (AVET.range(":Ns/int", Some(1), Some("y")).e.get must throwA[MoleculeException])
        .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
        "Please supply range arguments of same type as attribute."

      // Two wrong types simply returns no result
      AVET.range(":Ns/int", Some("x"), Some("y")).e.get === Nil
    }


    "Arg variables" in new Setup {

      // Args can be supplied as variables

      val attr = ":Ns/int"
      val one  = 1
      val six  = 6

      // All variables
      AVET.range(attr, Some(one), Some(six)).e.get === Seq(e1, e2)

      // Mixing static values and variables ok
      AVET.range(":Ns/int", Some(one), Some(6)).e.get === Seq(e1, e2)

      // Optionals can be supplied as variables too
      val from1  = Some(1)
      val until6 = Some(6)
      val end    = None
      AVET.range(":Ns/int", from1, end).e.get === List(e1, e2)
      AVET.range(":Ns/int", from1, until6).e.get === List(e1, e2)
    }


    "History" in new Setup {

      if (system != SystemPeerServer) {
        // Attribute :Ns/int values from 1 to end
        AVET.range(":Ns/int", Some(1), None).v.e.t.op.getHistory === List(
          (1, e1, t3, false),
          (1, e1, t1, true),
          (2, e1, t3, true),
          (4, e2, t5, false),
          (4, e2, t4, true),
          (5, e2, t5, true)
        )

        // Attribute :Ns/int values from 1 until 6
        AVET.range(":Ns/int", Some(1), Some(6)).v.e.t.op.getHistory === List(
          (1, e1, t3, false),
          (1, e1, t1, true),
          (2, e1, t3, true),
          (4, e2, t5, false),
          (4, e2, t4, true),
          (5, e2, t5, true)
        )

        // Attribute :Ns/int values from 1 until 5 (5 not included)
        AVET.range(":Ns/int", Some(1), Some(5)).v.e.t.op.getHistory === List(
          (1, e1, t3, false),
          (1, e1, t1, true),
          (2, e1, t3, true),
          (4, e2, t5, false),
          (4, e2, t4, true)
        )

        // Attribute :Ns/int values from 2 until 5 (1 and 5 not included)
        AVET.range(":Ns/int", Some(2), Some(5)).v.e.t.op.getHistory === List(
          (2, e1, t3, true),
          (4, e2, t5, false),
          (4, e2, t4, true)
        )

        // Attribute :Ns/int values from 3 until 5 (1, 2 and 5 not included)
        AVET.range(":Ns/int", Some(3), Some(5)).v.e.t.op.getHistory === List(
          (4, e2, t5, false),
          (4, e2, t4, true)
        )
      }
    }
  }


  "VAET" >> {

    "Args" in new Setup {

      // e2 no longer points to e3
      VAET(e3).a.e.t.get === Nil

      // e1 and e2 points to e4
      VAET(e4).a.e.t.get === List(
        (":Ns/ref1", e2, t12),
        (":Ns/refs1", e2, t13)
      )

      // e2 pointed to e3
      VAET(e3).a.e.t.op.getHistory === List(
        (":Ns/ref1", e2, t12, false),
        (":Ns/ref1", e2, t7, true)
      )

      // e1 and e2 now points to e4
      VAET(e4).a.e.t.op.getHistory === List(
        (":Ns/ref1", e2, t12, true),
        (":Ns/refs1", e2, t13, true)
      )
    }


    "Only mandatory datom args" in new Setup {

      // Applying values to Index attributes not allowed
      expectCompileError(
        "m(VAET(42L).v.a.e(77L).t)",
        "molecule.core.transform.exception.Dsl2ModelException: " +
          "VAET index attributes not allowed to have values applied.\n" +
          "VAET index only accepts datom arguments: `VAET(<v/a/e/t>)`.")
    }
  }
}