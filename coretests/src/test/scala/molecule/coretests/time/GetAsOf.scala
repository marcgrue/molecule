package molecule.coretests.time

import molecule.datomic.peer.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec


class GetAsOf extends CoreSpec {

  sequential

  "t (from history)" in new CoreSetup {

    val tx1 = Ns.str.int insert List(
      ("Ben", 42),
      ("Liz", 37),
    )
    val List(ben, liz) = tx1.eids

    val tx2 = Ns(ben).int(43).update

    val tx3 = ben.retract

    // See history of Ben
    Ns(ben).int.t.op.getHistory.sortBy(r => (r._2, r._3)) === List(
      (42, tx1.t, true), // Insert:  42 asserted
      (42, tx2.t, false), // Update:  42 retracted
      (43, tx2.t, true), //          43 asserted
      (43, tx3.t, false) // Retract: 43 retracted
    )

    // Data after insertion
    Ns.str.int.getAsOf(tx1.t) === List(
      ("Liz", 37),
      ("Ben", 42)
    )

    // Data after update
    Ns.str.int.getAsOf(tx2.t) === List(
      ("Liz", 37),
      ("Ben", 43) // Ben now 43
    )

    // Data after retraction
    Ns.str.int.getAsOf(tx3.t) === List(
      ("Liz", 37) // Ben gone
    )
  }


  "t (from tx)" in new CoreSetup {

    // tx1: initial insert
    val tx1 = Ns.int(1).save
    val eid = tx1.eid

    // tx2: update
    val tx2 = Ns(eid).int(2).update

    val t1 = tx1.t
    val t2 = tx2.t

    // Value as of tx1
    Ns(eid).int.getAsOf(t1) === List(1)

    // Current value
    Ns(eid).int.get === List(2)

    // Value as of tx2 (current value)
    Ns(eid).int.getAsOf(t2) === List(2)
  }


  "tx entity" in new CoreSetup {

    val eid = Ns.int(1).save.eid

    // Get tx entity of last transaction involving Ns.int
    val tx1 = Ns.int_.tx.get.head

    Ns(eid).int(2).update

    // Get tx entity of last transaction involving Ns.int
    val tx2 = Ns.int_.tx.get.head

    // Value as of tx1
    Ns(eid).int.getAsOf(tx1) === List(1)

    // Current value
    Ns(eid).int.get === List(2)

    // Value as of tx2 (current value)
    Ns(eid).int.getAsOf(tx2) === List(2)
  }


  "tx report" in new CoreSetup {

    // Insert (tx report 1)
    val tx1 = Ns.str.int insert List(
      ("Ben", 42),
      ("Liz", 37),
    )
    val ben = tx1.eid

    // Update (tx report 2)
    val tx2 = Ns(ben).int(43).update

    // Retract (tx report 3)
    val tx3 = ben.retract

    Ns.str.int.getAsOf(tx1) === List(
      ("Liz", 37),
      ("Ben", 42)
    )

    Ns.str.int.getAsOf(tx2) === List(
      ("Liz", 37),
      ("Ben", 43) // Ben now 43
    )

    Ns.str.int.getAsOf(tx3) === List(
      ("Liz", 37) // Ben gone
    )
  }


  "Date" in new CoreSetup {

    val beforeInsert = new java.util.Date
    // We slow down to allow new Date times to catch up (avoid using Date for precision times!)
    Thread.sleep(1000)

    // Insert
    val tx1 = Ns.str.int insert List(
      ("Ben", 42),
      ("Liz", 37),
    )
    val ben = tx1.eid
    val afterInsert = new java.util.Date
    Thread.sleep(1000)

    // Update
    val tx2 = Ns(ben).int(43).update
    val afterUpdate = new java.util.Date
    Thread.sleep(1000)

    // Retract
    val tx3 = ben.retract
    val afterRetract = new java.util.Date

    // No data yet before insert
    Ns.str.int.getAsOf(beforeInsert) === Nil

    Ns.str.int.getAsOf(afterInsert) === List(
      ("Liz", 37),
      ("Ben", 42)
    )

    Ns.str.int.getAsOf(afterUpdate) === List(
      ("Liz", 37),
      ("Ben", 43) // Ben now 43
    )

    Ns.str.int.getAsOf(afterRetract) === List(
      ("Liz", 37) // Ben gone
    )
  }


  "Iterable" in new CoreSetup {

    // Call getIterableAsOf for large result sets to maximize runtime performance.
    // Data is lazily type-casted on each call to `next` on the iterator.

    // Insert (tx report 1)
    val tx1 = Ns.str.int insert List(
      ("Ben", 42),
      ("Liz", 37),
    )
    val ben = tx1.eid

    // Update (tx report 2)
    val tx2 = Ns(ben).int(43).update

    // Retract (tx report 3)
    val tx3 = ben.retract

    val iterable1: Iterable[(String, Int)] = Ns.str.int.getIterableAsOf(tx1)
    val iterator1: Iterator[(String, Int)] = iterable1.iterator

    // Type casting lazily performed with each call to `next`
    iterator1.next() === ("Liz", 37)
    iterator1.next() === ("Ben", 42)

    val iterable2: Iterable[(String, Int)] = Ns.str.int.getIterableAsOf(tx2)
    val iterator2: Iterator[(String, Int)] = iterable2.iterator

    // Type casting lazily performed with each call to `next`
    iterator2.next() === ("Liz", 37)
    iterator2.next() === ("Ben", 43) // Ben now 43

    val iterable3: Iterable[(String, Int)] = Ns.str.int.getIterableAsOf(tx3)
    val iterator3: Iterator[(String, Int)] = iterable3.iterator

    // Type casting lazily performed with each call to `next`
    iterator3.next() === ("Liz", 37)
    iterator3.hasNext === false // Ben gone
  }


  "Raw" in new CoreSetup {

    // Insert (tx report 1)
    val tx1 = Ns.str.int insert List(
      ("Ben", 42),
      ("Liz", 37),
    )
    val ben = tx1.eid

    // Update (tx report 2)
    val tx2 = Ns(ben).int(43).update

    // Retract (tx report 3)
    val tx3 = ben.retract

    val raw1: java.util.Collection[java.util.List[AnyRef]] = Ns.str.int.getRawAsOf(tx1)
    raw1.toString === """[["Liz" 37], ["Ben" 42]]"""

    val raw2: java.util.Collection[java.util.List[AnyRef]] = Ns.str.int.getRawAsOf(tx2)
    raw2.toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43

    val raw3: java.util.Collection[java.util.List[AnyRef]] = Ns.str.int.getRawAsOf(tx3)
    raw3.toString === """[["Liz" 37]]""" // Ben gone
  }
}