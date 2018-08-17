package molecule.coretests.transaction

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.ops.exception.VerifyModelException

class TransactionMetaData extends CoreSpec {

  // See molecule.examples.dayOfDatomic.Provenance for more examples

  "Save" >> {

    "Basic" in new CoreSetup {

      // `tx` being tacit or mandatory has same effect
      val tx1 = Ns.int(1).Tx(Ns.str("tx tacit")).save.tx
      val tx2 = Ns.int(2).Tx(Ns.str("tx mandatory")).save.tx

      // tx meta attributes can be in any mode
      val tx3 = Ns.int(3).Tx(Ns.str("attr mandatory")).save.tx
      val tx4 = Ns.int(4).Tx(Ns.str_("attr tacit")).save.tx
      val tx5 = Ns.int(5).Tx(Ns.str$(Some("attr optional with value"))).save.tx
      val tx6 = Ns.int(6).Tx(Ns.str$(None)).save.tx // attr optional without value

      // Optional tx meta data
      Ns.int.Tx(Ns.str$).get.sortBy(_._1) === List(
        (1, Some("tx tacit")),
        (2, Some("tx mandatory")),
        (3, Some("attr mandatory")),
        (4, Some("attr tacit")),
        (5, Some("attr optional with value")),
        (6, None) // attr optional without value
      )

      // Mandatory tx meta data
      Ns.int.Tx(Ns.str).get.sortBy(_._1) === List(
        (1, "tx tacit"),
        (2, "tx mandatory"),
        (3, "attr mandatory"),
        (4, "attr tacit"),
        (5, "attr optional with value")
      )

      // Transactions without tx meta data
      Ns.int.Tx(Ns.str_(Nil)).get === List(6)

      // Transaction meta data expressions
      Ns.int.Tx(Ns.str.contains("mandatory")).get.sortBy(_._1) === List(
        (2, "tx mandatory"),
        (3, "attr mandatory")
      )
      Ns.int.<(3).Tx(Ns.str.contains("mandatory")).get === List(
        (2, "tx mandatory")
      )

      // tx entity id can be returned too
      Ns.int.tx.Tx(Ns.str$).get.sortBy(_._1) === List(
        (1, tx1, Some("tx tacit")),
        (2, tx2, Some("tx mandatory")),
        (3, tx3, Some("attr mandatory")),
        (4, tx4, Some("attr tacit")),
        (5, tx5, Some("attr optional with value")),
        (6, tx6, None) // attr optional without value
      )
    }


    "Multiple tx attributes with mixed modes" in new CoreSetup {

      // Modes mixed in different transactions
      Ns.int(1).Tx(Ns.str("a").long_(10L).bool$(Some(false))).save
      Ns.int(2).Tx(Ns.str_("b").long$(None).bool_(true)).save

      // Expected tx meta data
      Ns.int.Tx(Ns.str.long$.bool$).get.sortBy(_._1) === List(
        (1, "a", Some(10L), Some(false)),
        (2, "b", None, Some(true))
      )
    }


    "Tx refs" in new CoreSetup {

      // Saving tx meta data (Ns.str) that references another namespace attribute (Ref1.int1)
      Ns.int(1).Tx(Ns.str("a").Ref1.int1(10)).save

      // Tx meta data with ref attr
      Ns.int.Tx(Ns.str.Ref1.int1).get === List(
        (1, "a", 10)
      )

      // Tx meta data
      Ns.int.Tx(Ns.str).get === List(
        (1, "a")
      )

      // OBS: Ref1.int1 is not asserted with tx entity, but with ref from tx entity!
      Ns.int.Tx(Ref1.int1).get === Nil

      // Saving multiple tx refs
      Ns.int(2).Tx(Ns.str("b").Ref1.int1(20).Ref2.int2(200)).save

      // Getting multiple tx refs
      Ns.int.Tx(Ns.str.Ref1.int1.Ref2.int2).get === List(
        (2, "b", 20, 200)
      )
      Ns.int.Tx(Ns.str.Ref1.int1).get.sorted === List(
        (1, "a", 10), // First insert matches too
        (2, "b", 20)
      )
      Ns.int.Tx(Ns.str).get.sorted === List(
        (1, "a"),
        (2, "b")
      )
    }


    "Multiple tx groups" in new CoreSetup {

      // Tx meta data that is not connected by references can be saved by applying
      // individual tx molecules to separate tx's
      Ns.int(1).Tx(Ns.str("a")).Tx(Ref1.int1(10)).save

      // This is not the same as a composite since the composite would be its own entity
      // and not the tx entity. (won't compile either)
      // Ns.int.Tx(Ns.str ~ Ref1.int1).get

      // Ns.str has no own reference to Ref1.int1
      Ns.int.Tx(Ns.str.Ref1.int1).get === Nil

      // Tx entity has Ns.str asserted
      Ns.int.Tx(Ns.str).get === List(
        (1, "a")
      )

      // Tx entity also has Ref1.int1 asserted
      Ns.int.Tx(Ref1.int1).get === List(
        (1, 10)
      )

      // Tx entity has both Ns.str and Ref1.int1 asserted
      Ns.int.Tx(Ns.str).Tx(Ref1.int1).get === List(
        (1, "a", 10)
      )
    }


    "Multiple tx groups" in new CoreSetup {

      // Tx meta data that is not connected by references can be saved by applying
      // individual tx molecules to separate tx's
      Ns.int(1).Tx(Ns.str("a")).Tx(Ref1.int1(10)).save

      // This is not the same as a composite since the composite would be its own entity
      // and not the tx entity. (won't compile either)
      // Ns.int.Tx(Ns.str ~ Ref1.int1).get

      // Ns.str has no own reference to Ref1.int1
      Ns.int.Tx(Ns.str.Ref1.int1).get === Nil

      // Tx entity has Ns.str asserted
      Ns.int.Tx(Ns.str).get === List(
        (1, "a")
      )

      // Tx entity also has Ref1.int1 asserted
      Ns.int.Tx(Ref1.int1).get === List(
        (1, 10)
      )

      // Tx entity has both Ns.str and Ref1.int1 asserted
      Ns.int.Tx(Ns.str).Tx(Ref1.int1).get === List(
        (1, "a", 10)
      )
    }
  }


  "Insert" >> {

    "Insert 1 or more" in new CoreSetup {

      // Can't add transaction meta data along other data of molecule
      (Ns.int.Tx(Ns.str).insert(0, "a") must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Can't both apply meta data to tx attribute and insert meta data
      (Ns.int.Tx(Ns.str("a")).insert(List((0, "b"))) must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Can't both apply meta data to optional tx attribute and insert meta data
      (Ns.int.Tx(Ns.str$(Some("a"))).insert(List((0, Some("b")))) must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[onlyTacitTxAttrs]  For inserts, tx meta data can only be applied to tacit attributes, like: `Ns.str_(<metadata>)`"

      // Apply tx meta data to tacit tx attributes:
      Ns.int.Tx(Ns.str_("a")).insert(0)

      // Data without tx meta data
      Ns.int.insert(1)

      // Base data having tx meta data
      Ns.int.Tx(Ns.str_).get === List(0)

      // All base data
      Ns.int.get === List(1, 0)

      // Transaction molecules don't have to be related to the base molecule
      Ns.int.Tx(Ref1.str1_("b")).insert(2)
      Ns.int.Tx(Ref2.str2_("c")).insert(3)

      Ns.int.Tx(Ref1.str1_).get === List(2)
      Ns.int.Tx(Ref2.str2_).get === List(3)

      // Base data with tx meta data
      Ns.int.Tx(Ns.str).get === List((0, "a"))
      Ns.int.Tx(Ref1.str1).get === List((2, "b"))
      Ns.int.Tx(Ref2.str2).get === List((3, "c"))
    }


    "Large tx meta data" in new CoreSetup {

      Ns.str.Tx(Ns
        .int_(int1)
        .long_(long1)
        .float_(float1)
        .double_(double1)
        .bool_(bool1)
        .date_(date1)
        .uuid_(uuid1)
      ).insert("With tx meta data")

      // Add data without tx meta data
      Ns.str.insert("Without tx meta data")

      // Data with and without tx meta data created
      Ns.str.get === List(
        "With tx meta data",
        "Without tx meta data"
      )

      // Use transaction meta data to filter
      Ns.str.Tx(Ns.int_(int1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.long_(long1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.float_(float1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.double_(double1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.bool_(bool1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.date_(date1)).get === List("With tx meta data")
      Ns.str.Tx(Ns.uuid_(uuid1)).get === List("With tx meta data")

      // All tx meta data present
      Ns.str.Tx(Ns
        .int_(int1)
        .long_(long1)
        .float_(float1)
        .double_(double1)
        .bool_(bool1)
        .date_(date1)
        .uuid_(uuid1)
      ).get === List(
        "With tx meta data"
      )
    }
  }

  "Composite with multiple tx meta data molecules" in new CoreSetup {

    insert(
      Ns.str, Ns.int
    )(Seq(
      ("with tx meta data", 1)
    ))(
      // 2 tx meta data molecules - no limit on the number!
      Ns
        .bool(true)
        .bools(Set(false))
        .date(date7)
        .dates(Set(date8, date9))
        .double(7.0)
        .doubles(Set(8.0, 9.0))
        .enum(enum7)
        .enums(Set(enum8, enum9))
        .float(7f)
        .floats(Set(8f, 9f)),
      Ns
        .long(7L)
        .longs(Set(8L, 9L))
        .ref1(701L)
        .refSub1(702L)
        .uuid(uuid7)
        .uuids(Set(uuid8))
    )

    Ns.str.int.insert("without tx meta data", 2)

    // Since both attributes are from the same namespace
    // the two following queries will return both entities
    m(Ns.str.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )
    m(Ns.str ~ Ns.int).get.sorted === List(
      ("with tx meta data", 1),
      ("without tx meta data", 2)
    )

    // Find by some meta data
    m(Ns.str.int.Tx(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.Tx(Ns.float_(7f))).get.sorted === List(("with tx meta data", 1))

    // Find by other meta data
    m(Ns.str.int.Tx(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.Tx(Ns.long_(7L))).get.sorted === List(("with tx meta data", 1))

    // Find by two meta values
    m(Ns.str.int.Tx(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))
    m(Ns.str ~ Ns.int.Tx(Ns.float_(7f).long_(7L))).get === List(("with tx meta data", 1))

    // Entities _without_ meta data
    m(Ns.str.int.Tx(Ns.long_(Nil))).get.sorted === List(("without tx meta data", 2))
    m(Ns.str ~ Ns.int.Tx(Ns.long_(Nil))).get.sorted === List(("without tx meta data", 2))
  }


  "Update" in new CoreSetup {

    // tx 1: save
    val e = Ns.int(1).Tx(Ns.str("a")).save.eid

    // tx2: Update without tx meta data
    Ns(e).int(2).update

    // tx3: Update with tx meta data
    Ns(e).int(3).Tx(Ns.str("b")).update


    // History without tx meta data
    Ns(e).int.t.op.getHistory.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, 1028, true), // 1 asserted (save)

      // tx 2
      (1, 1030, false), // 1 retracted
      (2, 1030, true), // 2 asserted (update)

      // tx 3
      (2, 1031, false), // 2 retracted
      (3, 1031, true) // 3 asserted (update)
    )

    // History with tx meta data
    Ns(e).int.t.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._3)) === List(
      // tx 1
      (1, 1028, true, "a"), // 1 asserted (save)

      // (tx2 has no tx meta data)

      // tx 3
      (2, 1031, false, "b"), // 2 retracted
      (3, 1031, true, "b") // 3 asserted (update)
    )
  }


  "1 entity" in new CoreSetup {

    val e = Ns.int(1).Tx(Ns.str("a")).save.eid

    // Retract entity with tx meta data
    e.Tx(Ns.str("b")).retract

    Ns(e).int.t.op.getHistory.sortBy(r => (r._2, r._3)) === List(
      (1, 1028, true), // 1 asserted (save)
      (1, 1030, false) // 1 retracted (delete)
    )

    Ns(e).int.t.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._3)) === List(
      (1, 1028, true, "a"),

      // 1 was retracted with tx meta data "b"
      (1, 1030, false, "b")
    )
  }


  "Multiple entities without tx meta data" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

    Ns.int.get === List(1, 2, 3)

    // Retract multiple entities (without tx meta data)
    retract(Seq(e1, e2))

    Ns.int.get === List(3)
  }

  "Multiple entities" in new CoreSetup {

    // Insert multiple entities with tx meta data
    val List(e1, e2, e3, tx) = Ns.int.Tx(Ns.str_("a")) insert List(1, 2, 3) eids

    // Retract multiple entities with tx meta data
    retract(Seq(e1, e2), Ns.str("b"))

    // History with transaction data
    Ns.int.t.op.Tx(Ns.str).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
      (1, 1028, true, "a"),
      (2, 1028, true, "a"),
      (3, 1028, true, "a"),

      // 1 and 2 were retracted with tx meta data "b"
      (1, 1032, false, "b"),
      (2, 1032, false, "b")
    )

    // Entities and int values that were retracted with tx meta data "b"
    Ns.e.int.op(false).Tx(Ns.str("b")).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
      (e1, 1, false, "b"),
      (e2, 2, false, "b")
    )

    // Or: What int values were retracted with tx meta data "b"?
    Ns.int.op_(false).Tx(Ns.str_("b")).getHistory === List(1, 2)
  }


  "Multiple entities with tx meta data including ref" in new CoreSetup {

    // Insert multiple entities with tx meta data including ref
    val List(e1, e2, e3, tx, r1) = Ns.int.Tx(Ns.str_("a").Ref1.int1_(7)) insert List(1, 2, 3) eids

    // Add tx meta data to retracting multiple entities
    retract(Seq(e1, e2), Ns.str("b").Ref1.int1(8))

    // History with transaction data
    Ns.int.t.op.Tx(Ns.str.Ref1.int1).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
      (1, 1028, true, "a", 7),
      (2, 1028, true, "a", 7),
      (3, 1028, true, "a", 7),

      // 1 and 2 were retracted with tx meta data "b"
      (1, 1033, false, "b", 8),
      (2, 1033, false, "b", 8)
    )

    // Entities and int values that was retracted in tx "b"
    Ns.e.int.op(false).Tx(Ns.str("b").Ref1.int1(8)).getHistory.sortBy(r => (r._2, r._1, r._3)) === List(
      (e1, 1, false, "b", 8),
      (e2, 2, false, "b", 8)
    )

    // Or: What int values where retracted in tx "b"?
    Ns.int.op_(false).Tx(Ns.str_("b").Ref1.int1_(8)).getHistory === List(1, 2)

    // OBS: Note how referenced tx meta data is not asserted directly with the tx entity:
    Ns.e.int.op(false).Tx(Ref1.int1(8)).getHistory === Nil
    // While Ns.str is:
    Ns.e.int.op(false).Tx(Ns.str("b")).getHistory.sortBy(_._2) === List(
      (e1, 1, false, "b"),
      (e2, 2, false, "b")
    )
  }
}