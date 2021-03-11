package molecule.tests.core.obj

import java.io.FileReader
import java.util.Date
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.datomic.base.facade.TxReport
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class TimeAsync extends TestSpec with Helpers {

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
    await(Ns.int.getAsyncObjListAsOf(t1)).map(_.int) === List(1)
    await(Ns.int.getAsyncObjListAsOf(t2)).map(_.int) === List(1, 2)
    await(Ns.int.getAsyncObjListAsOf(t3)).map(_.int) === List(1, 2, 3)
    await(Ns.int.getAsyncObjListAsOf(t3, 2)).map(_.int) === List(1, 2)

    await(Ns.int.getAsyncObjListAsOf(tx1)).map(_.int) === List(1)
    await(Ns.int.getAsyncObjListAsOf(tx2)).map(_.int) === List(1, 2)
    await(Ns.int.getAsyncObjListAsOf(tx3)).map(_.int) === List(1, 2, 3)
    await(Ns.int.getAsyncObjListAsOf(tx3, 2)).map(_.int) === List(1, 2)

    await(Ns.int.getAsyncObjListAsOf(d1)).map(_.int) === List(1)
    await(Ns.int.getAsyncObjListAsOf(d2)).map(_.int) === List(1, 2)
    await(Ns.int.getAsyncObjListAsOf(d3)).map(_.int) === List(1, 2, 3)
    await(Ns.int.getAsyncObjListAsOf(d3, 2)).map(_.int) === List(1, 2)


    await(Ns.int.getAsyncObjArrayAsOf(t1)).map(_.int) === Array(1)
    await(Ns.int.getAsyncObjArrayAsOf(t2)).map(_.int) === Array(1, 2)
    await(Ns.int.getAsyncObjArrayAsOf(t3)).map(_.int) === Array(1, 2, 3)
    await(Ns.int.getAsyncObjArrayAsOf(t3, 2)).map(_.int) === Array(1, 2)

    await(Ns.int.getAsyncObjArrayAsOf(tx1)).map(_.int) === Array(1)
    await(Ns.int.getAsyncObjArrayAsOf(tx2)).map(_.int) === Array(1, 2)
    await(Ns.int.getAsyncObjArrayAsOf(tx3)).map(_.int) === Array(1, 2, 3)
    await(Ns.int.getAsyncObjArrayAsOf(tx3, 2)).map(_.int) === Array(1, 2)

    await(Ns.int.getAsyncObjArrayAsOf(d1)).map(_.int) === Array(1)
    await(Ns.int.getAsyncObjArrayAsOf(d2)).map(_.int) === Array(1, 2)
    await(Ns.int.getAsyncObjArrayAsOf(d3)).map(_.int) === Array(1, 2, 3)
    await(Ns.int.getAsyncObjArrayAsOf(d3, 2)).map(_.int) === Array(1, 2)


    await(Ns.int.getAsyncObjIterableAsOf(t1)).iterator.next().int === 1
    await(Ns.int.getAsyncObjIterableAsOf(t1)).iterator.toList.map(_.int) === Iterator(1).toList
    await(Ns.int.getAsyncObjIterableAsOf(t2)).iterator.toList.map(_.int) === Iterator(1, 2).toList
    await(Ns.int.getAsyncObjIterableAsOf(t3)).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList

    await(Ns.int.getAsyncObjIterableAsOf(tx1)).iterator.toList.map(_.int) === Iterator(1).toList
    await(Ns.int.getAsyncObjIterableAsOf(tx2)).iterator.toList.map(_.int) === Iterator(1, 2).toList
    await(Ns.int.getAsyncObjIterableAsOf(tx3)).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList

    await(Ns.int.getAsyncObjIterableAsOf(d1)).iterator.toList.map(_.int) === Iterator(1).toList
    await(Ns.int.getAsyncObjIterableAsOf(d2)).iterator.toList.map(_.int) === Iterator(1, 2).toList
    await(Ns.int.getAsyncObjIterableAsOf(d3)).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
  }

  
  "Since" in new TxsSetup {
    await(Ns.int.getAsyncObjListSince(t3)).map(_.int) === List()
    await(Ns.int.getAsyncObjListSince(t2)).map(_.int) === List(3)
    await(Ns.int.getAsyncObjListSince(t1)).map(_.int) === List(2, 3)
    await(Ns.int.getAsyncObjListSince(t1, 1)).map(_.int) === List(2)

    await(Ns.int.getAsyncObjListSince(tx3)).map(_.int) === List()
    await(Ns.int.getAsyncObjListSince(tx2)).map(_.int) === List(3)
    await(Ns.int.getAsyncObjListSince(tx1)).map(_.int) === List(2, 3)
    await(Ns.int.getAsyncObjListSince(tx1, 1)).map(_.int) === List(2)

    await(Ns.int.getAsyncObjListSince(d3)).map(_.int) === List()
    await(Ns.int.getAsyncObjListSince(d2)).map(_.int) === List(3)
    await(Ns.int.getAsyncObjListSince(d1)).map(_.int) === List(2, 3)
    await(Ns.int.getAsyncObjListSince(d1, 1)).map(_.int) === List(2)


    await(Ns.int.getAsyncObjArraySince(t3)).map(_.int) === Array()
    await(Ns.int.getAsyncObjArraySince(t2)).map(_.int) === Array(3)
    await(Ns.int.getAsyncObjArraySince(t1)).map(_.int) === Array(2, 3)
    await(Ns.int.getAsyncObjArraySince(t1, 1)).map(_.int) === Array(2)

    await(Ns.int.getAsyncObjArraySince(tx3)).map(_.int) === Array()
    await(Ns.int.getAsyncObjArraySince(tx2)).map(_.int) === Array(3)
    await(Ns.int.getAsyncObjArraySince(tx1)).map(_.int) === Array(2, 3)
    await(Ns.int.getAsyncObjArraySince(tx1, 1)).map(_.int) === Array(2)

    await(Ns.int.getAsyncObjArraySince(d3)).map(_.int) === Array()
    await(Ns.int.getAsyncObjArraySince(d2)).map(_.int) === Array(3)
    await(Ns.int.getAsyncObjArraySince(d1)).map(_.int) === Array(2, 3)
    await(Ns.int.getAsyncObjArraySince(d1, 1)).map(_.int) === Array(2)


    await(Ns.int.getAsyncObjIterableSince(t3)).iterator.toList.map(_.int) === Iterator().toList
    await(Ns.int.getAsyncObjIterableSince(t2)).iterator.toList.map(_.int) === Iterator(3).toList
    await(Ns.int.getAsyncObjIterableSince(t1)).iterator.toList.map(_.int) === Iterator(2, 3).toList

    await(Ns.int.getAsyncObjIterableSince(tx3)).iterator.toList.map(_.int) === Iterator().toList
    await(Ns.int.getAsyncObjIterableSince(tx2)).iterator.toList.map(_.int) === Iterator(3).toList
    await(Ns.int.getAsyncObjIterableSince(tx1)).iterator.toList.map(_.int) === Iterator(2, 3).toList

    await(Ns.int.getAsyncObjIterableSince(d3)).iterator.toList.map(_.int) === Iterator().toList
    await(Ns.int.getAsyncObjIterableSince(d2)).iterator.toList.map(_.int) === Iterator(3).toList
    await(Ns.int.getAsyncObjIterableSince(d1)).iterator.toList.map(_.int) === Iterator(2, 3).toList
  }


  "With" in new CoreSetup {
    Ns.int(1).save

    val saveTx2 = Ns.int(2).getSaveStmts
    val saveTx3 = Ns.int(3).getSaveStmts

    // Tx data from edn file
    // contains: "[{:Ns/int 2} {:Ns/int 3}]"
    val data      = new FileReader("molecule-tests/resources/tests/core/time/save2-3.dtm")
    val txData2_3 = datomic.Util.readAll(data).get(0).asInstanceOf[java.util.List[Object]]
    

    await(Ns.int.getAsyncObjListWith(saveTx2)).map(_.int) === List(1, 2)
    await(Ns.int.getAsyncObjListWith(saveTx2, saveTx3)).map(_.int) === List(1, 2, 3)
    // Note how the parameter for number of rows returned is first (since we
    // need the vararg for tx molecules last)
    await(Ns.int.getAsyncObjListWith(2, saveTx2, saveTx3)).map(_.int) === List(1, 2)
    await(Ns.int.getAsyncObjListWith(txData2_3)).map(_.int) === List(1, 2, 3)
    await(Ns.int.getAsyncObjListWith(txData2_3, 2)).map(_.int) === List(1, 2)

    await(Ns.int.getAsyncObjArrayWith(saveTx2)).map(_.int) === Array(1, 2)
    await(Ns.int.getAsyncObjArrayWith(saveTx2, saveTx3)).map(_.int) === Array(1, 2, 3)
    await(Ns.int.getAsyncObjArrayWith(2, saveTx2, saveTx3)).map(_.int) === Array(1, 2)
    await(Ns.int.getAsyncObjArrayWith(txData2_3)).map(_.int) === Array(1, 2, 3)
    await(Ns.int.getAsyncObjArrayWith(txData2_3, 2)).map(_.int) === Array(1, 2)

    await(Ns.int.getAsyncObjIterableWith(saveTx2)).iterator.toList.map(_.int) === Iterator(1, 2).toList
    await(Ns.int.getAsyncObjIterableWith(saveTx2, saveTx3)).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
    await(Ns.int.getAsyncObjIterableWith(txData2_3)).iterator.toList.map(_.int) === Iterator(1, 2, 3).toList
  }


  "History" in new CoreSetup {
    val tx1 = Ns.int(1).save
    val e   = tx1.eid
    val tx2 = Ns(e).int(2).update

    await(Ns(e).int.t.op.getAsyncObjListHistory).sortBy(o => (o.t, o.op)).map(o => Vector(o.int, o.t, o.op)) === List(
      Vector(1, tx1.t, true),
      Vector(1, tx2.t, false),
      Vector(2, tx2.t, true)
    )
  }
}
