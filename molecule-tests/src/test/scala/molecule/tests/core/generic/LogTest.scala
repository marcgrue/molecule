package molecule.tests.core.generic

import molecule.core.exceptions.MoleculeException
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out5._
import molecule.datomic.base.facade.TxReport
import molecule.datomic.base.util.{SystemDevLocal, SystemPeer}
import molecule.setup.TestSpec


class LogTest extends TestSpec {

  class Setup extends CoreSetup {
    // Generally use `t` or `tx` to identify transaction and `txInstant` only to get
    // the wall clock time since Date's are only precise to ms.

    // First entity

    val txR1: TxReport = Ns.str("a").int(1).save
    val tx1            = txR1.tx
    val e1             = txR1.eid
    val t1             = txR1.t
    val d1             = txR1.inst

    Thread.sleep(5) // make sure to separate dates a few ms
    val txR2 = Ns(e1).str("b").update
    val tx2  = txR2.tx
    val t2   = txR2.t
    val d2   = txR2.inst

    Thread.sleep(5)
    val txR3 = Ns(e1).int(2).update
    val tx3  = txR3.tx
    val t3   = txR3.t
    val d3   = txR3.inst


    // Second entity

    Thread.sleep(5)
    val txR4 = Ns.str("x").int(4).save
    val tx4  = txR4.tx
    val e2   = txR4.eid
    val t4   = txR4.t
    val d4   = txR4.inst

    Thread.sleep(5)
    val txR5 = Ns(e2).int(5).update
    val tx5  = txR5.tx
    val t5   = txR5.t
    val d5   = txR5.inst


    // Relationship

    Thread.sleep(5)
    val txR6 = Ref1.str1("hello").save
    val tx6  = txR6.tx
    val t6   = txR6.t
    val d6   = txR6.inst
    val e3   = txR6.eid

    Thread.sleep(5)
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
  }


  "Basics" in new Setup {

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


    Log(Some(tx1), Some(tx4)).t.e.a.v.op.get === List(
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

    Log(Some(tx2), Some(tx4)).t.e.a.v.op.get === List(
      (t2, tx2, ":db/txInstant", d2, true),
      (t2, e1, ":Ns/str", "b", true),
      (t2, e1, ":Ns/str", "a", false),

      (t3, tx3, ":db/txInstant", d3, true),
      (t3, e1, ":Ns/int", 2, true),
      (t3, e1, ":Ns/int", 1, false)
    )
  }


  "Grouped" in new Setup {

    // Resembling the original structure of the Datomic Log
    val logMap = Log(Some(tx1), Some(tx4)).t.e.a.v.op.get.groupBy(_._1)

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


  "Args" in new Setup {

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
      "molecule.core.transform.exception.Dsl2ModelException: Log attributes not allowed to have values applied.\n" +
        "Log only accepts range arguments: `Log(from, until)`.")


    (Log(Some(tx1), Some("unexpected string")).tx.get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Args to Log can only be t, tx or txInstant of type Int/Long/Date. " +
      s"Found: List($tx1, unexpected string)"

    (Log(Some(t1), Some("unexpected string")).t.get must throwA[MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Args to Log can only be t, tx or txInstant of type Int/Long/Date. " +
      s"Found: List($t1, unexpected string)"
  }


  "Start/End" in new Setup {

    // tx12 (inclusive) - end
    Log(Some(t12), None).t.e.a.v.op.get === List(
      (t12, tx12, ":db/txInstant", d12, true),
      (t12, e2, ":Ns/ref1", e4, true),
      (t12, e2, ":Ns/ref1", e3, false),

      (t13, tx13, ":db/txInstant", d13, true),
      (t13, e2, ":Ns/refs1", e4, true)
    )

    // Single parameter is `from`
    Log(Some(tx12)).t.get === Log(Some(tx12), None).t.get

    if (system == SystemPeer) {
      // Start - t3 (exclusive)
      // Includes all Datomic database bootstrapping and schema transactions
      Log(None, Some(tx3)).t.get.size === 426

      // Start - end !! The whole database!
      Log(None, None).t.get.size === 457
      // Same as this shortcut
      Log().t.get.size === 457

    } else if (system == SystemDevLocal) {

      // Start - t3 (exclusive)
      // Includes all Datomic database bootstrapping and schema transactions
      Log(None, Some(tx3)).t.get.size === 552

      // Start - end !! The whole database!
      Log(None, None).t.get.size === 583
      // Same as this shortcut
      Log().t.get.size === 583
    }
  }


  "Queries" in new Setup {

    // Number of transactions between tx1 and tx12
    Log(Some(tx1), Some(tx12)).t.get.distinct.size === 11


    // Entities involved in tx1-tx2
    Log(Some(tx1), Some(tx3)).e.get.distinct.sorted === List(tx1, tx2, e1).sorted

    // Entities involved in tx1-tx4
    Log(Some(tx1), Some(tx5)).e.get.distinct.sorted === List(tx1, tx2, tx3, tx4, e1, e2).sorted


    // Attributes involved in transactions tx1-tx2
    Log(Some(tx1), Some(tx3)).a.get.distinct.sorted === List(":Ns/int", ":Ns/str", ":db/txInstant")

    // Attributes involved in transactions tx1-tx6
    Log(Some(tx1), Some(tx7)).a.get.distinct.sorted === List(":Ns/int", ":Ns/str", ":Ref1/str1", ":db/txInstant")


    // Values asserted in transactions tx1-tx2
    Log(Some(tx1), Some(tx3)).v.get.distinct === List(d1, "a", 1, d2, "b")

    // Values asserted in transactions tx1-tx3
    Log(Some(tx1), Some(tx4)).v.get.distinct === List(d1, "a", 1, d2, "b", d3, 2)


    // Assertions and retractions in transactions tx1-tx2
    Log(Some(tx1), Some(tx3)).op.get === List(true, true, true, true, true, false)

    // Number of assertions and retractions in transactions tx1-tx2
    Log(Some(tx1), Some(tx3)).op.get.groupBy {
      case true  => "assertions"
      case false => "retractions"
    }.toList.sortBy(_._1).map { case (k, vs) => k -> vs.length } === List("assertions" -> 5, "retractions" -> 1)

    // Number of assertions and retractions in transactions tx1-tx13
    Log(Some(tx1), Some(tx13)).op.get.groupBy {
      case true  => "assertions"
      case false => "retractions"
    }.toList.sortBy(_._1).map { case (k, vs) => k -> vs.length } === List("assertions" -> 28, "retractions" -> 7)


    // Transactions from wall clock time d1-d2
    Log(Some(d1), Some(d3)).tx.get.distinct.sorted === List(tx1, tx2)

    // Same as quering the history for all transactions within the time range
    Ns.tx.txInstant_.>=(d1).txInstant_.<(d3).getHistory.sorted === List(tx1, tx2)
  }


  "History" in new Setup {

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