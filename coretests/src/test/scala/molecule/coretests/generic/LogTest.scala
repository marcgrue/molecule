package molecule.coretests.generic

import molecule.datomic.peer.api._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.exceptions.MoleculeException
import molecule.facade.TxReport
import molecule.util.expectCompileError


class LogTest extends CoreSpec {
  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

  // Generally use `t` or `tx` to identify transaction and `txInstant` only to get
  // the wall clock time since Date's are a bit unreliable for precision.


  // First entity

  val txR1: TxReport = Ns.str("a").int(1).save
  val tx1            = txR1.tx
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
  val tx12  = txR12.tx
  val t12   = txR12.t
  val d12   = txR12.inst

  // e1 also points to e4
  val txR13 = Ns(e2).refs1(e4).update
  val tx13  = txR13.tx
  val t13   = txR13.t
  val d13   = txR13.inst

  // Inline descriptions taken from the manual:
  // https://docs.datomic.com/on-prem/indexes.html


  "Basics" >> {

    // Apply attribute name and `from` + `until` value range arguments

    // Datoms with attribute :Ns/int having a value between 2 until 6 (not included)
    Log(Some(tx1), Some(tx2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    Log(Some(tx1), Some(tx3)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true),

      (t2, tx2, ":db/txInstant", d2, true),
      (t2, e1, ":Ns/str", "b", true),
      (t2, e1, ":Ns/str", "a", false)
    )

    Log(Some(t1), Some(t4)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true),

      (t2, tx2, ":db/txInstant", d2, true),
      (t2, e1, ":Ns/str", "b", true),
      (t2, e1, ":Ns/str", "a", false),

      (t3, tx3, ":db/txInstant", d3, true),
      (t3, e1, ":Ns/int", 2, true),
      (t3, e1, ":Ns/int", 1, false)
    )

    Log(Some(t2), Some(t4)).t.e.a.v.op.get === List(
      (t2, tx2, ":db/txInstant", d2, true),
      (t2, e1, ":Ns/str", "b", true),
      (t2, e1, ":Ns/str", "a", false),

      (t3, tx3, ":db/txInstant", d3, true),
      (t3, e1, ":Ns/int", 2, true),
      (t3, e1, ":Ns/int", 1, false)
    )
  }


  "Grouped" >> {

    //Resembling the original structure of the Datomic Log
    val logMap = Log(Some(t1), Some(t4)).t.e.a.v.op.get.groupBy(_._1)

    logMap === Map(
      t1 -> List(
        (t1, tx1, ":db/txInstant", d1, true),
        (t1, e1, ":Ns/str", "a", true),
        (t1, e1, ":Ns/int", 1, true)
      ),
      t2 -> List(
        (t2, tx2, ":db/txInstant", d2, true),
        (t2, e1, ":Ns/str", "b", true),
        (t2, e1, ":Ns/str", "a", false)
      ),
      t3 -> List(
        (t3, tx3, ":db/txInstant", d3, true),
        (t3, e1, ":Ns/int", 2, true),
        (t3, e1, ":Ns/int", 1, false)
      )
    )

    // Individual transactions within the range can then be retrieved
    logMap(t2) === List(
      (t2, tx2, ":db/txInstant", d2, true),
      (t2, e1, ":Ns/str", "b", true),
      (t2, e1, ":Ns/str", "a", false)
    )
  }


  "Args" >> {

    // t - t
    Log(Some(t1), Some(t2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // t - tx
    Log(Some(t1), Some(tx2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // t - txInstant
    Log(Some(t1), Some(d2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // tx - t
    Log(Some(tx1), Some(t2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // tx - tx
    Log(Some(tx1), Some(tx2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // tx - txInstant
    Log(Some(tx1), Some(d2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // txInstant - t
    Log(Some(d1), Some(t2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // txInstant - tx
    Log(Some(d1), Some(tx2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // txInstant - tx
    Log(Some(d1), Some(d2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    // Applying values to Schema attributes not allowed
    expectCompileError(
      "m(Log.e(e1).a.v.t)",
      "molecule.transform.exception.Dsl2ModelException: Log attributes not allowed to have values applied.\n" +
        "Log only accepts range arguments: `Log(from, until)`.")

    (Log(Some(t1), Some("unexpect string")).t.get must throwA[MoleculeException])
      .message === "Got the exception molecule.exceptions.package$MoleculeException: " +
      "Args to Log can only be t, tx or txInstant of type Int/Long/Date"

  }


  "Start/End" >> {

    // t12 (inclusive) - end
    Log(Some(t12), None).t.e.a.v.op.get === List(
      (t12, tx12, ":db/txInstant", d12, true),
      (t12, e2, ":Ns/ref1", e4, true),
      (t12, e2, ":Ns/ref1", e3, false),

      (t13, tx13, ":db/txInstant", d13, true),
      (t13, e2, ":Ns/refs1", e4, true)
    )

    // Start - t3 (exclusive)
    // Includes all Datomic database bootstrapping and schema transactions
    Log(None, Some(t3)).t.get.size === 426

    // Start - end
    // Molecule disallow returning from beginning to end (the whole database!)
    (Log(None, None).t.get must throwA[MoleculeException])
      .message === "Got the exception molecule.exceptions.package$MoleculeException: " +
      "Molecule not allowing returning from start to end (the whole database!).\n" +
      "If you need this, please use raw Datomic access:\n" +
      "`conn.datomicConn.log.txRange(tx1, tx2)`"
  }


  "Queries" >> {

    // Number of transactions between tx1 and tx12
    Log(Some(tx1), Some(tx12)).t.get.distinct.size === 11


    // Entities involved in t1-t2
    Log(Some(t1), Some(t3)).e.get.distinct.sorted === List(tx1, tx2, e1)

    // Entities involved in t1-t4
    Log(Some(t1), Some(t5)).e.get.distinct.sorted === List(tx1, tx2, tx3, tx4, e1, e2)


    // Attributes involved in transactions t1-t2
    Log(Some(t1), Some(t3)).a.get.distinct.sorted === List(":Ns/int", ":Ns/str", ":db/txInstant")

    // Attributes involved in transactions t1-t6
    Log(Some(t1), Some(t7)).a.get.distinct.sorted === List(":Ns/int", ":Ns/str", ":Ref1/str1", ":db/txInstant")


    // Values asserted in transactions t1-t2
    Log(Some(t1), Some(t3)).v.get.distinct === List(d1, "a", 1, d2, "b")

    // Values asserted in transactions t1-t3
    Log(Some(t1), Some(t4)).v.get.distinct === List(d1, "a", 1, d2, "b", d3, 2)


    // Assertions and retractions in transactions t1-t2
    Log(Some(t1), Some(t3)).op.get === List(true, true, true, true, true, false)

    // Number of assertions and retractions in transactions t1-t2
    Log(Some(t1), Some(t3)).op.get.groupBy {
      case true  => "assertions"
      case false => "retractions"
    }.toList.sortBy(_._1).map { case (k, vs) => k -> vs.length } === List("assertions" -> 5, "retractions" -> 1)

    // Number of assertions and retractions in transactions t1-t13
    Log(Some(t1), Some(t13)).op.get.groupBy {
      case true  => "assertions"
      case false => "retractions"
    }.toList.sortBy(_._1).map { case (k, vs) => k -> vs.length } === List("assertions" -> 28, "retractions" -> 7)


    // Transactions from wall clock time d1-d2
    Log(Some(d1), Some(d3)).t.get.distinct.sorted === List(t1, t2)

    // Same as quering the history for all transactions within the time range
    Ns.t.txInstant_.>=(d1).txInstant_.<(d3).getHistory === List(t1, t2)
  }


  "History" >> {

    // Since we get the Log from the Connection and not the Database,
    // any time filter getter will make no difference

    Log(Some(tx1), Some(tx2)).t.e.a.v.op.get === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    Log(Some(tx1), Some(tx2)).t.e.a.v.op.getHistory === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )

    Log(Some(tx1), Some(tx2)).t.e.a.v.op.getAsOf(t7) === List(
      (t1, tx1, ":db/txInstant", d1, true),
      (t1, e1, ":Ns/str", "a", true),
      (t1, e1, ":Ns/int", 1, true)
    )
  }
}