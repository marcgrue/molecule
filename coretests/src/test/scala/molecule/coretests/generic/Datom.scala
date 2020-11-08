package molecule.coretests.generic

import molecule.datomic.peer.api._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.util.expectCompileError


/** Generic Datom attribute interface
  *
  * - `e` Entity id (Long)
  * - `a` Full attribute name like ":Person/name" (String)
  * - `v` Value of Datoms (Any)
  * - `t` Transaction pointer (Long)
  * - `tx` Transaction entity id
  * - `txInstant` Transaction wall clock time
  * - `op` Assertion (true) / retraction (false) status
  */
class Datom extends CoreSpec {
  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

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

  val txR3 = Ns(e1).int(3).update
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


  // Third entity, a ref

  val txR6 = Ref1.str1("hello").save
  val r1   = txR6.eid
  val tx6  = txR6.tx
  val t6   = txR6.t
  val d6   = txR6.inst

  val txR7 = Ns(e2).ref1(r1).update
  val tx7  = txR7.tx
  val t7   = txR7.t
  val d7   = txR7.inst


  "Entities" >> {

    "1 entity" >> {

      // Several generic attributes can be asserted with a common entity id
      // Molecules with an applied entity id and only generic attributes
      // matches the Datom values of the entity

      // Entity `e1` has 2 asserted datoms (and the entity id)
      e1.touch === Map(
        ":db/id" -> e1,
        ":Ns/int" -> 3,
        ":Ns/str" -> "b")

      // Entity id returned
      Ns.e(e1).get === List(e1)
      // Same as
      Ns(e1).e.get === List(e1)

      // Attribute names
      Ns(e1).a.get === List(":Ns/int", ":Ns/str")

      // Attribute values (of `Any` type)
      Ns(e1).v.get === List("b", 3)

      // Transaction entity id
      Ns(e1).tx.get === List(tx2, tx3)

      // Transaction time t
      Ns(e1).t.get === List(t2, t3)

      // Transaction wall clock time as Date
      Ns(e1).txInstant.get.sorted === List(d2, d3)

      // Transaction operation: true: assert, false: retract
      Ns(e1).op.get === List(true)

      // Core 5 Datom values (quintuplets)
      Ns(e1).e.a.v.tx.op.get === List(
        (e1, ":Ns/int", 3, tx3, true),
        (e1, ":Ns/str", "b", tx2, true),
      )

      // Generic attributes can be added in any order
      Ns(e1).v.t.e.op.tx.a.get === List(
        ("b", t2, e1, true, tx2, ":Ns/str"),
        (3, t3, e1, true, tx3, ":Ns/int")
      )
    }


    "n entities" >> {
      Ns(e1, e2).e.t.a.v.get.sortBy(t => (t._1, t._2)) === List(
        (e1, t2, ":Ns/str", "b"),
        (e1, t3, ":Ns/int", 3),
        (e2, t4, ":Ns/str", "x"),
        (e2, t5, ":Ns/int", 5),
        (e2, t7, ":Ns/ref1", r1)
      )
    }


    "History" >> {
      Ns(e1, e2).e.t.a.v.op.getHistory.sortBy(t => (t._1, t._2)) === List(
        (e1, t1, ":Ns/int", 1, true),
        (e1, t1, ":Ns/str", "a", true),
        (e1, t2, ":Ns/str", "a", false),
        (e1, t2, ":Ns/str", "b", true),
        (e1, t3, ":Ns/int", 1, false),
        (e1, t3, ":Ns/int", 3, true),

        (e2, t4, ":Ns/int", 4, true),
        (e2, t4, ":Ns/str", "x", true),
        (e2, t5, ":Ns/int", 4, false),
        (e2, t5, ":Ns/int", 5, true),
        (e2, t7, ":Ns/ref1", r1, true),
      )
    }
  }


  "Attributes" >> {

    "1 Attribute" >> {

      Ns.int.e.get === List((3, e1), (5, e2))
      Ns.int.a.get === List((3, ":Ns/int"), (5, ":Ns/int"))
      Ns.int.v.get === List((3, 3), (5, 5))
      Ns.int.tx.get === List((3, tx3), (5, tx5))
      Ns.int.t.get === List((3, t3), (5, t5))
      Ns.int.txInstant.get.sortBy(_._1).toString === List((3, d3), (5, d5)).toString
      Ns.int.op.get === List((5, true), (3, true))

      // Generic attributes after attribute with applied value
      Ns.int(5).e.get === List((5, e2))
      Ns.int(5).a.get === List((5, ":Ns/int"))
      Ns.int(5).v.get === List((5, 5))
      Ns.int(5).tx.get === List((5, tx5))
      Ns.int(5).t.get === List((5, t5))
      Ns.int(5).txInstant.get.toString === List((5, d5)).toString
      Ns.int(5).op.get === List((5, true))

      // Generic attributes after attribute with applied operation
      Ns.int.<(4).e.get === List((3, e1))
      Ns.int.<(4).a.get === List((3, ":Ns/int"))
      Ns.int.<(4).v.get === List((3, 3))
      Ns.int.<(4).tx.get === List((3, tx3))
      Ns.int.<(4).t.get === List((3, t3))
      Ns.int.<(4).txInstant.get.toString === List((3, d3)).toString
      Ns.int.<(4).op.get === List((3, true))

      // Generic attributes after attribute with applied aggregate keyword
      Ns.int(max).e.get === List((3, e1), (5, e2))
      Ns.int(max).a.get === List((5, ":Ns/int"))
      Ns.int(max).v.get === List((3, 3), (5, 5))
      Ns.int(max).tx.get === List((3, tx3), (5, tx5))
      Ns.int(max).t.get === List((3, t3), (5, t5))
      Ns.int(max).txInstant.get.sortBy(_._1).toString === List((3, d3), (5, d5)).toString
      Ns.int(max).op.get === List((5, true))
    }


    "order of attribute types" >> {

      // Generic entity id first is ok
      Ns.e.int.t.get.sorted === List(
        (e1, 3, t3),
        (e2, 5, t5),
      )

      // Other generic attributes not allowed before first attribute
      expectCompileError(
        "Ns.t.op.int.get",
        "molecule.ops.exception.VerifyRawModelException: " +
          "Can't add first attribute `int` after generic attributes (except `e` which is ok to have first). " +
          "Please add generic attributes `t`, `op` after `int`.")

      // Generic attributes after custom attribute ok
      Ns.int.t.op.get === List(
        (5, t5, true),
        (3, t3, true),
      )

      // Custom attributes after generic attributes ok as long
      // as at least one custom attr is before generic attributes
      Ns.int.op.str.get === List(
        (3, true, "b"),
        (5, true, "x")
      )
    }

    "Full scan" >> {

      // Generic attributes without an entity id applied or custom attributes added would
      // cause a full scan of the whole database and is not allowed
      expectCompileError(
        "Ns.e.a.v.t.get",
        "molecule.ops.exception.VerifyRawModelException: " +
          "Molecule with only generic attributes and no entity id(s) applied are not allowed since " +
          "it would cause a full scan of the whole database.")

      // Any filter will prevent a full scan

      Ns.e.a(":Ns/str").v.t.get === List(
        (e1, ":Ns/str", "b", t2),
        (e2, ":Ns/str", "x", t4)
      )

      // Count also involves full scan if no other attribute is present
      expectCompileError(
        "Ns.e(count).get",
        "molecule.ops.exception.VerifyRawModelException: " +
          "Molecule with only generic attributes and no entity id(s) applied are not allowed since " +
          "it would cause a full scan of the whole database.")

      // If some attribute is also asserted, we can use count
      Ns.e(count).int_.get === List(2)
    }


    "Ref" >> {

      // Generic attributes after ref id
      Ns.Ref1.e.get === List(r1)
      Ns.Ref1.a.get === List(":Ref1/str1")
      Ns.Ref1.v.get === List("hello")
      // `ref1` ref datom asserted in tx7
      Ns.Ref1.tx.get === List(tx7)
      Ns.Ref1.t.get === List(t7)
      Ns.Ref1.txInstant.get.toString === List(d7).toString
      Ns.Ref1.op.get === List(true)


      Ns.int.Ref1.e.get === List((5, r1))
      Ns.int.Ref1.a.get === List((5, ":Ref1/str1"))
      Ns.int.Ref1.v.get === List((5, "hello"))
      // `ref1` ref datom asserted in tx7
      Ns.int.Ref1.tx.get === List((5, tx7))
      Ns.int.Ref1.t.get === List((5, t7))
      Ns.int.Ref1.txInstant.get.toString === List((5, d7)).toString
      Ns.int.Ref1.op.get === List((5, true))


      Ns.int.Ref1.e.str1.get === List((5, r1, "hello"))
      Ns.int.Ref1.a.str1.get === List((5, ":Ref1/str1", "hello"))
      Ns.int.Ref1.v.str1.get === List((5, "hello", "hello"))
      // `ref1` ref datom asserted in tx7
      Ns.int.Ref1.tx.str1.get === List((5, tx7, "hello"))
      Ns.int.Ref1.t.str1.get === List((5, t7, "hello"))
      Ns.int.Ref1.txInstant.str1.get.toString === List((5, d7, "hello")).toString
      Ns.int.Ref1.op.str1.get === List((5, true, "hello"))


      Ns.int.Ref1.str1.e.get === List((5, "hello", r1))
      Ns.int.Ref1.str1.a.get === List((5, "hello", ":Ref1/str1"))
      Ns.int.Ref1.str1.v.get === List((5, "hello", "hello"))
      // `str1` datom asserted in tx6
      Ns.int.Ref1.str1.tx.get === List((5, "hello", tx6))
      Ns.int.Ref1.str1.t.get === List((5, "hello", t6))
      Ns.int.Ref1.str1.txInstant.get.toString === List((5, "hello", d6)).toString
      Ns.int.Ref1.str1.op.get === List((5, "hello", true))

      // generic attr before ref
      Ns.int.op.Ref1.str1.op.get === List((5, true, "hello", true))
    }


    "Tacit" >> {
      expectCompileError(
        """m(Ns.int.tx_)""",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Tacit `tx_` can only be used with an applied value i.e. `tx_(<value>)`")
      ok
    }
  }


  "Expressions, mandatory" >> {

    Ns.e(e1).get === List(e1)
    Ns.e(e1, e2).get.sorted === List(e1, e2)
    Ns.e.not(e1).get.sorted === List(e2, r1)
    Ns.e.not(e1, e2).get === List(r1)
    Ns.e.>(e1).get === List(e2, r1)
    Ns.e.>=(e1).get.sorted === List(e1, e2, r1)
    Ns.e.<=(e2).get.sorted === List(e1, e2)
    Ns.e.<(e2).get === List(e1)

    // Only `e` before first custom attribute is allowed
    Ns.e(count).int_.get === List(2)
    Ns.int_.e(count).get === List(2)

    Ns.a(":Ns/str").get === List(":Ns/str")
    Ns.a(":Ns/str", ":Ns/int").get === List(":Ns/int", ":Ns/str")
    Ns.a.not(":Ns/str").get === List(":Ns/int", ":Ref1/str1", ":Ns/ref1")
    Ns.a.not(":Ns/str", ":Ns/ref1").get === List(":Ns/int", ":Ref1/str1")
    Ns.a.>(":Ns/str").get === List(":Ref1/str1")
    Ns.a.>=(":Ns/str").get === List(":Ref1/str1", ":Ns/str")
    Ns.a.<=(":Ns/str").get === List(":Ns/int", ":Ns/ref1", ":Ns/str")
    Ns.a.<(":Ns/str").get === List(":Ns/int", ":Ns/ref1")
    // Range of attribute names
    Ns.a_.>(":Ns/int").a.<=(":Ns/str").get === List(":Ns/ref1", ":Ns/str")

    Ns.int_.a(count).get === List(1)

    Ns.v(3).get === List(3)
    Ns.v("hello").get === List("hello")
    Ns.v("non-existing value").get === Nil
    Ns.v(3, "b").get === List("b", 3)
    Ns.v.not(3).get === List("b", r1, 5, "x", "hello")
    Ns.v.not(3, "b").get === List(r1, 5, "x", "hello")
    expectCompileError(
      """m(Ns.v.>(3))""",
      "molecule.transform.exception.Dsl2ModelException: " +
        "Can't compare generic values being of different types. Found: v.>(3)")

    Ns.tx(tx3).get === List(tx3)
    Ns.tx(tx3, tx5).get === List(tx5, tx3)

    // Note that no current datoms remains from tx1
    Ns.tx.not(tx3).get.sorted === List(tx2, tx4, tx5, tx6, tx7)

    // If we ask the history database though, tx1 will show up too
    Ns.tx.not(tx3).getHistory.sorted === List(tx1, tx2, tx4, tx5, tx6, tx7)

    Ns.tx.not(tx3, tx5).get.sorted === List(tx2, tx4, tx6, tx7)
    Ns.tx.>(tx3).get.sorted === List(tx4, tx5, tx6, tx7)
    Ns.tx.>=(tx3).get.sorted === List(tx3, tx4, tx5, tx6, tx7)
    Ns.tx.<=(tx3).get.sorted === List(tx2, tx3) // excludes tx1 per explanation above
    Ns.tx.<=(tx3).getHistory.sorted === List(tx1, tx2, tx3) // includes tx1 too per explanation above
    Ns.tx.<(tx3).get === List(tx2)
    // Range of transaction entity ids
    Ns.tx_.>(tx2).tx.<=(tx4).get.sorted === List(tx3, tx4)
    Ns.int_.tx(count).get === List(2)

    Ns.t(t3).get === List(t3)
    Ns.t(t3, t5).get.sorted === List(t3, t5)
    Ns.t.not(t3).get.sorted === List(t2, t4, t5, t6, t7)
    Ns.t.not(t3, t5).get.sorted === List(t2, t4, t6, t7)
    Ns.t.>(t3).get.sorted === List(t4, t5, t6, t7)
    Ns.t.>=(t3).get.sorted === List(t3, t4, t5, t6, t7)
    Ns.t.<=(t3).get.sorted === List(t2, t3)
    Ns.t.<=(t3).getHistory.sorted === List(t1, t2, t3)
    Ns.t.<(t3).get === List(t2)
    // Range of transaction t's
    Ns.t_.>(t2).t.<=(t4).get.sorted === List(t3, t4)
    Ns.int_.t(count).get === List(2)

    Ns.txInstant(d2).get === List(d2)
    Ns.txInstant(d2, d3).get.sorted === List(d2, d3)
    Ns.txInstant.not(d2).get.sorted === List(d3, d4, d5, d6, d7)
    Ns.txInstant.not(d2, d3).get.sorted === List(d4, d5, d6, d7)
    Ns.txInstant.>(d3).get.sorted === List(d4, d5, d6, d7)
    Ns.txInstant.>=(d3).get.sorted === List(d3, d4, d5, d6, d7)
    Ns.txInstant.<=(d3).get.sorted === List(d2, d3)
    Ns.txInstant.<=(d3).getHistory.sorted === List(d1, d2, d3)
    Ns.txInstant.<(d3).get.sorted === List(d2)
    // Range of transaction entity ids
    Ns.txInstant_.>(d2).txInstant.<=(d4).get.sorted === List(d3, d4)
    Ns.int_.txInstant(count).get === List(2)

    // No current datoms are retracted
    Ns.op(true).get === List(true)
    Ns.op(true, false).get === List(true)
    Ns.op(false).get === Nil
    // History database contains retracted datoms
    Ns.op(false).getHistory === List(false)

    Ns.op.not(true).get === Nil
    Ns.op.not(true, false).get === Nil

    // Comparing boolean values not that relevant, but hey, here we go:
    Ns.op.>(true).get === Nil
    Ns.op.>(true).getHistory === Nil
    Ns.op.>(false).get === List(true)
    Ns.op.>(false).getHistory === List(true)

    Ns.op.>=(true).get === List(true)
    Ns.op.>=(true).getHistory === List(true)
    Ns.op.>=(false).get === List(true)
    Ns.op.>=(false).getHistory === List(false, true)

    Ns.op.<=(true).get === List(true)
    Ns.op.<=(true).getHistory === List(false, true)
    Ns.op.<=(false).get === Nil
    Ns.op.<=(false).getHistory === List(false)

    Ns.op.<(true).get === Nil
    Ns.op.<(true).getHistory === List(false)
    Ns.op.<(false).get === Nil
    Ns.op.<(false).getHistory === Nil

    Ns.int_.op(count).get === List(1)

    // Generic attributes only allowed to aggregate `count`
    expectCompileError(
      """m(Ns.int.t(max))""",
      "molecule.transform.exception.Dsl2ModelException: " +
        "Generic attributes only allowed to aggregate `count`. Found: `max`")
    ok
  }


  "Expressions, tacit" >> {

    Ns.int.a_(":Ns/int").get === List(3, 5)
    Ns.int.a_(":Ns/str").get === Nil
    Ns.int.a_(":Ns/str", ":Ns/int").get === List(3, 5)
    Ns.int.a_.not(":Ns/str").get === List(3, 5)
    Ns.int.a_.not(":Ns/str", ":Ns/int").get === Nil
    Ns.int.a_.>(":Ns/str").get === Nil
    Ns.int.a_.>=(":Ns/str").get === Nil
    Ns.int.a_.<=(":Ns/str").get === List(3, 5)
    Ns.int.a_.<(":Ns/str").get === List(3, 5)

    Ns.int.v_(3).get === List(3)
    // Value only relates to previous custom datom
    Ns.int.v_("b").get === Nil
    Ns.int.v_(3, "b").get === List(3)
    Ns.int.v_.not(3).get === List(5)
    Ns.int.v_.not(3, "b").get === List(5)
    expectCompileError(
      """m(Ns.int.v_.<=(3))""",
      "molecule.transform.exception.Dsl2ModelException: " +
        "Can't compare generic values being of different types. Found: v_.<=(3)")

    Ns.int.tx_(tx3).get === List(3)
    Ns.int.tx_(tx3, tx5).get === List(3, 5)
    Ns.int.tx_.not(tx3).get === List(5)
    Ns.int.tx_.not(tx3, tx5).get === Nil
    Ns.int.tx_.>(tx3).get === List(5)
    Ns.int.tx_.>=(tx3).get === List(3, 5)
    Ns.int.tx_.<=(tx3).get === List(3)
    Ns.int.tx_.<(tx3).get === Nil
    // Int values withing range of transaction entity ids
    Ns.int.tx_.>(tx2).tx_.<=(tx4).get === List(3)
    Ns.int.tx_.>(tx2).tx_.<=(tx5).get === List(3, 5)

    Ns.int.t_(t3).get === List(3)
    Ns.int.t_(t3, t5).get === List(3, 5)
    Ns.int.t_.not(t3).get === List(5)
    Ns.int.t_.not(t3, t5).get === Nil
    Ns.int.t_.>(t3).get === List(5)
    Ns.int.t_.>=(t3).get === List(3, 5)
    Ns.int.t_.<=(t3).get === List(3)
    Ns.int.t_.<(t3).get === Nil
    // Int values withing range of transaction t's
    Ns.int.t_.>(t2).t_.<=(t4).get === List(3)
    Ns.int.t_.>(t2).t_.<=(t5).get === List(3, 5)

    Ns.int.txInstant_(d3).get === List(3)
    Ns.int.txInstant_(d3, d5).get === List(3, 5)
    Ns.int.txInstant_.not(d3).get === List(5)
    Ns.int.txInstant_.not(d3, d5).get === Nil
    Ns.int.txInstant_.>(d3).get === List(5)
    Ns.int.txInstant_.>=(d3).get === List(3, 5)
    Ns.int.txInstant_.<=(d3).get === List(3)
    Ns.int.txInstant_.<(d3).get === Nil

    // Int values withing range of transaction dates
    Ns.int.txInstant_.>(d2).txInstant_.<=(d4).get === List(3)
    Ns.int.txInstant_.>(d2).txInstant_.<=(d5).get === List(3, 5)

    Ns.int.op_(true).get === List(3, 5)
    Ns.int.op_(true, false).get === List(3, 5)
    Ns.int.op_(false).get === Nil

    // Retracted numbers from history db!
    Ns.int.op_(false).getHistory === List(1, 4)

    Ns.int.op_.not(true).get === Nil
    Ns.int.op_.not(false).get === List(3, 5)
    Ns.int.op_.not(true, false).get === Nil
    // Skipping pretty meaningless boolean comparisons
  }


  "Multiple tx attributes" >> {

    // Tacit attributes can be followed by generic attributes
    Ns(e1).str_.tx.get.head === tx2
    Ns(e1).int_.tx.get.head === tx3

    Ns(e1).str_.tx.int_.tx.get.head === (tx2, tx3)
  }


  "Optional tx data not allowed" >> {
    expectCompileError(
      """m(Ns.int$.tx.str)""",
      "molecule.transform.exception.Dsl2ModelException: Optional attributes (`int$`) can't be followed by generic transaction attributes (`tx`).")

    expectCompileError(
      """m(Ns.int$.t.str)""",
      "molecule.transform.exception.Dsl2ModelException: Optional attributes (`int$`) can't be followed by generic transaction attributes (`t`).")

    expectCompileError(
      """m(Ns.int$.txInstant.str)""",
      "molecule.transform.exception.Dsl2ModelException: Optional attributes (`int$`) can't be followed by generic transaction attributes (`txInstant`).")

    expectCompileError(
      """m(Ns.int$.op.str)""",
      "molecule.transform.exception.Dsl2ModelException: Optional attributes (`int$`) can't be followed by generic transaction attributes (`op`).")

    ok
  }
}