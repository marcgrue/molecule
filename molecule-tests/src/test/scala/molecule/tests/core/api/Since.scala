package molecule.tests.core.api

import java.util.Date
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out1._
import molecule.datomic.base.facade.TxReport
import molecule.core.util.JavaUtil
import molecule.setup.TestSpec


class Since extends TestSpec with JavaUtil {

  class Setup extends CoreSetup {

    // Ensure Date ms precision
    val tx1: TxReport = Ns.int(1).save
    Thread.sleep(10)
    val tx2: TxReport = Ns.int(2).save
    Thread.sleep(10)
    val tx3: TxReport = Ns.int(3).save

    val t1: Long = tx1.t
    val t2: Long = tx2.t
    val t3: Long = tx3.t

    val d1: Date = tx1.inst
    val d2: Date = tx2.inst
    val d3: Date = tx3.inst
  }


  "Since" in new Setup {

    Ns.int.getSince(t3) === List()
    Ns.int.getSince(t2) === List(3)
    Ns.int.getSince(t1) === List(2, 3)
    Ns.int.getSince(t1, 1) === List(2)

    Ns.int.getSince(tx3) === List()
    Ns.int.getSince(tx2) === List(3)
    Ns.int.getSince(tx1) === List(2, 3)
    Ns.int.getSince(tx1, 1) === List(2)

    Ns.int.getSince(d3) === List()
    Ns.int.getSince(d2) === List(3)
    Ns.int.getSince(d1) === List(2, 3)
    Ns.int.getSince(d1, 1) === List(2)


    Ns.int.getArraySince(t3) === Array()
    Ns.int.getArraySince(t2) === Array(3)
    Ns.int.getArraySince(t1) === Array(2, 3)
    Ns.int.getArraySince(t1, 1) === Array(2)

    Ns.int.getArraySince(tx3) === Array()
    Ns.int.getArraySince(tx2) === Array(3)
    Ns.int.getArraySince(tx1) === Array(2, 3)
    Ns.int.getArraySince(tx1, 1) === Array(2)

    Ns.int.getArraySince(d3) === Array()
    Ns.int.getArraySince(d2) === Array(3)
    Ns.int.getArraySince(d1) === Array(2, 3)
    Ns.int.getArraySince(d1, 1) === Array(2)


    Ns.int.getIterableSince(t3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(t2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(t1).iterator.toList === Iterator(2, 3).toList

    Ns.int.getIterableSince(tx3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(tx2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(tx1).iterator.toList === Iterator(2, 3).toList

    Ns.int.getIterableSince(d3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(d2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(d1).iterator.toList === Iterator(2, 3).toList


    Ns.int.getRawSince(t3).ints === List()
    Ns.int.getRawSince(t2).ints === List(3)
    Ns.int.getRawSince(t1).ints === List(2, 3)
    Ns.int.getRawSince(t1, 1).ints === List(2)

    Ns.int.getRawSince(tx3).ints === List()
    Ns.int.getRawSince(tx2).ints === List(3)
    Ns.int.getRawSince(tx1).ints === List(2, 3)
    Ns.int.getRawSince(tx1, 1).ints === List(2)

    Ns.int.getRawSince(d3).ints === List()
    Ns.int.getRawSince(d2).ints === List(3)
    Ns.int.getRawSince(d1).ints === List(2, 3)
    Ns.int.getRawSince(d1, 1).ints === List(2)
  }
}
