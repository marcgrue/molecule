package molecule.tests.core.obj

import java.io.FileReader
import java.util.Date
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.datomic.base.facade.TxReport
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Time extends TestSpec with Helpers {

  class TxsSetup extends CoreSetup {

    val tx1: TxReport = Ns.int(1).save
    // Avoid date ms overlaps (use tx or t instead for precision)
    Thread.sleep(100)
    val tx2: TxReport = Ns.int(2).save
    Thread.sleep(100)
    val tx3: TxReport = Ns.int(3).save

    val t1: Long = tx1.t
    val t2: Long = tx2.t
    val t3: Long = tx3.t

    val d1: Date = tx1.inst
    val d2: Date = tx2.inst
    val d3: Date = tx3.inst
  }
  
  "AsOf" in new TxsSetup {
    Ns.int.getObjListAsOf(t1).map(_.int) === List(1)
    Ns.int.getObjListAsOf(t2).map(_.int) === List(1, 2)
    Ns.int.getObjListAsOf(t3).map(_.int) === List(1, 2, 3)
    Ns.int.getObjListAsOf(t3, 2).map(_.int) === List(1, 2)

    Ns.int.getObjListAsOf(tx1).map(_.int) === List(1)
    Ns.int.getObjListAsOf(tx2).map(_.int) === List(1, 2)
    Ns.int.getObjListAsOf(tx3).map(_.int) === List(1, 2, 3)
    Ns.int.getObjListAsOf(tx3, 2).map(_.int) === List(1, 2)

    Ns.int.getObjListAsOf(d1).map(_.int) === List(1)
    Ns.int.getObjListAsOf(d2).map(_.int) === List(1, 2)
    Ns.int.getObjListAsOf(d3).map(_.int) === List(1, 2, 3)
    Ns.int.getObjListAsOf(d3, 2).map(_.int) === List(1, 2)


    Ns.int.getObjArrayAsOf(t1).map(_.int) === Array(1)
    Ns.int.getObjArrayAsOf(t2).map(_.int) === Array(1, 2)
    Ns.int.getObjArrayAsOf(t3).map(_.int) === Array(1, 2, 3)
    Ns.int.getObjArrayAsOf(t3, 2).map(_.int) === Array(1, 2)

    Ns.int.getObjArrayAsOf(tx1).map(_.int) === Array(1)
    Ns.int.getObjArrayAsOf(tx2).map(_.int) === Array(1, 2)
    Ns.int.getObjArrayAsOf(tx3).map(_.int) === Array(1, 2, 3)
    Ns.int.getObjArrayAsOf(tx3, 2).map(_.int) === Array(1, 2)

    Ns.int.getObjArrayAsOf(d1).map(_.int) === Array(1)
    Ns.int.getObjArrayAsOf(d2).map(_.int) === Array(1, 2)
    Ns.int.getObjArrayAsOf(d3).map(_.int) === Array(1, 2, 3)
    Ns.int.getObjArrayAsOf(d3, 2).map(_.int) === Array(1, 2)


    Ns.int.getObjIterableAsOf(t1).iterator.next().int === 1
    Ns.int.getObjIterableAsOf(t1).iterator.toList.map(_.int) === Iterator(1).toList
    Ns.int.getObjIterableAsOf(t2).iterator.toList.map(_.int) === Iterator(1, 2).toList
    Ns.int.getObjIterableAsOf(t3).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList

    Ns.int.getObjIterableAsOf(tx1).iterator.toList.map(_.int) === Iterator(1).toList
    Ns.int.getObjIterableAsOf(tx2).iterator.toList.map(_.int) === Iterator(1, 2).toList
    Ns.int.getObjIterableAsOf(tx3).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList

    Ns.int.getObjIterableAsOf(d1).iterator.toList.map(_.int) === Iterator(1).toList
    Ns.int.getObjIterableAsOf(d2).iterator.toList.map(_.int) === Iterator(1, 2).toList
    Ns.int.getObjIterableAsOf(d3).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
  }

  
  "Since" in new TxsSetup {
    Ns.int.getObjListSince(t3).map(_.int) === List()
    Ns.int.getObjListSince(t2).map(_.int) === List(3)
    Ns.int.getObjListSince(t1).map(_.int) === List(2, 3)
    Ns.int.getObjListSince(t1, 1).map(_.int) === List(2)

    Ns.int.getObjListSince(tx3).map(_.int) === List()
    Ns.int.getObjListSince(tx2).map(_.int) === List(3)
    Ns.int.getObjListSince(tx1).map(_.int) === List(2, 3)
    Ns.int.getObjListSince(tx1, 1).map(_.int) === List(2)

    Ns.int.getObjListSince(d3).map(_.int) === List()
    Ns.int.getObjListSince(d2).map(_.int) === List(3)
    Ns.int.getObjListSince(d1).map(_.int) === List(2, 3)
    Ns.int.getObjListSince(d1, 1).map(_.int) === List(2)


    Ns.int.getObjArraySince(t3).map(_.int) === Array()
    Ns.int.getObjArraySince(t2).map(_.int) === Array(3)
    Ns.int.getObjArraySince(t1).map(_.int) === Array(2, 3)
    Ns.int.getObjArraySince(t1, 1).map(_.int) === Array(2)

    Ns.int.getObjArraySince(tx3).map(_.int) === Array()
    Ns.int.getObjArraySince(tx2).map(_.int) === Array(3)
    Ns.int.getObjArraySince(tx1).map(_.int) === Array(2, 3)
    Ns.int.getObjArraySince(tx1, 1).map(_.int) === Array(2)

    Ns.int.getObjArraySince(d3).map(_.int) === Array()
    Ns.int.getObjArraySince(d2).map(_.int) === Array(3)
    Ns.int.getObjArraySince(d1).map(_.int) === Array(2, 3)
    Ns.int.getObjArraySince(d1, 1).map(_.int) === Array(2)


    Ns.int.getObjIterableSince(t3).iterator.toList.map(_.int) === Iterator().toList
    Ns.int.getObjIterableSince(t2).iterator.toList.map(_.int) === Iterator(3).toList
    Ns.int.getObjIterableSince(t1).iterator.toList.map(_.int) === Iterator(2, 3).toList

    Ns.int.getObjIterableSince(tx3).iterator.toList.map(_.int) === Iterator().toList
    Ns.int.getObjIterableSince(tx2).iterator.toList.map(_.int) === Iterator(3).toList
    Ns.int.getObjIterableSince(tx1).iterator.toList.map(_.int) === Iterator(2, 3).toList

    Ns.int.getObjIterableSince(d3).iterator.toList.map(_.int) === Iterator().toList
    Ns.int.getObjIterableSince(d2).iterator.toList.map(_.int) === Iterator(3).toList
    Ns.int.getObjIterableSince(d1).iterator.toList.map(_.int) === Iterator(2, 3).toList
  }


  "With" in new CoreSetup {
    Ns.int(1).save

    val saveTx2 = Ns.int(2).getSaveStmts
    val saveTx3 = Ns.int(3).getSaveStmts

    // Tx data from edn file
    // contains: "[{:Ns/int 2} {:Ns/int 3}]"
    val data      = new FileReader("molecule-tests/resources/tests/core/time/save2-3.dtm")
    val txData2_3 = datomic.Util.readAll(data).get(0).asInstanceOf[java.util.List[Object]]
    

    Ns.int.getObjListWith(saveTx2).map(_.int) === List(1, 2)
    Ns.int.getObjListWith(saveTx2, saveTx3).map(_.int) === List(1, 2, 3)
    // Note how the parameter for number of rows returned is first (since we
    // need the vararg for tx molecules last)
    Ns.int.getObjListWith(2, saveTx2, saveTx3).map(_.int) === List(1, 2)
    Ns.int.getObjListWith(txData2_3).map(_.int) === List(1, 2, 3)
    Ns.int.getObjListWith(txData2_3, 2).map(_.int) === List(1, 2)

    Ns.int.getObjArrayWith(saveTx2).map(_.int) === Array(1, 2)
    Ns.int.getObjArrayWith(saveTx2, saveTx3).map(_.int) === Array(1, 2, 3)
    Ns.int.getObjArrayWith(2, saveTx2, saveTx3).map(_.int) === Array(1, 2)
    Ns.int.getObjArrayWith(txData2_3).map(_.int) === Array(1, 2, 3)
    Ns.int.getObjArrayWith(txData2_3, 2).map(_.int) === Array(1, 2)

    Ns.int.getObjIterableWith(saveTx2).iterator.toList.map(_.int) === Iterator(1, 2).toList
    Ns.int.getObjIterableWith(saveTx2, saveTx3).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
    Ns.int.getObjIterableWith(txData2_3).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
  }


  "History" in new CoreSetup {
    val tx1 = Ns.int(1).save
    val e   = tx1.eid
    val tx2 = Ns(e).int(2).update

    Ns(e).int.t.op.getObjListHistory.sortBy(o => (o.t, o.op)).map(o => Vector(o.int, o.t, o.op)) === List(
      Vector(1, tx1.t, true),
      Vector(1, tx2.t, false),
      Vector(2, tx2.t, true)
    )
  }
}
