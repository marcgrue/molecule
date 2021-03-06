package molecule.tests.core.api

import java.util.Date
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out1._
import molecule.datomic.base.facade.TxReport
import molecule.core.util.JavaUtil
import molecule.setup.TestSpec


class AsOf extends TestSpec with JavaUtil {

  class Setup extends CoreSetup {

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


  "AsOf" in new Setup {

    Ns.int.getAsOf(t1) === List(1)
    Ns.int.getAsOf(t2) === List(1, 2)
    Ns.int.getAsOf(t3) === List(1, 2, 3)
    Ns.int.getAsOf(t3, 2) === List(1, 2)

    Ns.int.getAsOf(tx1) === List(1)
    Ns.int.getAsOf(tx2) === List(1, 2)
    Ns.int.getAsOf(tx3) === List(1, 2, 3)
    Ns.int.getAsOf(tx3, 2) === List(1, 2)

    Ns.int.getAsOf(d1) === List(1)
    Ns.int.getAsOf(d2) === List(1, 2)
    Ns.int.getAsOf(d3) === List(1, 2, 3)
    Ns.int.getAsOf(d3, 2) === List(1, 2)


    Ns.int.getArrayAsOf(t1) === Array(1)
    Ns.int.getArrayAsOf(t2) === Array(1, 2)
    Ns.int.getArrayAsOf(t3) === Array(1, 2, 3)
    Ns.int.getArrayAsOf(t3, 2) === Array(1, 2)

    Ns.int.getArrayAsOf(tx1) === Array(1)
    Ns.int.getArrayAsOf(tx2) === Array(1, 2)
    Ns.int.getArrayAsOf(tx3) === Array(1, 2, 3)
    Ns.int.getArrayAsOf(tx3, 2) === Array(1, 2)

    Ns.int.getArrayAsOf(d1) === Array(1)
    Ns.int.getArrayAsOf(d2) === Array(1, 2)
    Ns.int.getArrayAsOf(d3) === Array(1, 2, 3)
    Ns.int.getArrayAsOf(d3, 2) === Array(1, 2)


    Ns.int.getIterableAsOf(t1).iterator.toList === Iterator(1).toList
    Ns.int.getIterableAsOf(t2).iterator.toList === Iterator(1, 2).toList
    Ns.int.getIterableAsOf(t3).iterator.toList === Iterator(1, 2, 3).toList

    Ns.int.getIterableAsOf(tx1).iterator.toList === Iterator(1).toList
    Ns.int.getIterableAsOf(tx2).iterator.toList === Iterator(1, 2).toList
    Ns.int.getIterableAsOf(tx3).iterator.toList === Iterator(1, 2, 3).toList

    Ns.int.getIterableAsOf(d1).iterator.toList === Iterator(1).toList
    Ns.int.getIterableAsOf(d2).iterator.toList === Iterator(1, 2).toList
    Ns.int.getIterableAsOf(d3).iterator.toList === Iterator(1, 2, 3).toList


    Ns.int.getRawAsOf(t1).ints === List(1)
    Ns.int.getRawAsOf(t2).ints === List(1, 2)
    Ns.int.getRawAsOf(t3).ints === List(1, 2, 3)
    Ns.int.getRawAsOf(t3, 2).ints === List(1, 2)

    Ns.int.getRawAsOf(tx1).ints === List(1)
    Ns.int.getRawAsOf(tx2).ints === List(1, 2)
    Ns.int.getRawAsOf(tx3).ints === List(1, 2, 3)
    Ns.int.getRawAsOf(tx3, 2).ints === List(1, 2)

    Ns.int.getRawAsOf(d1).ints === List(1)
    Ns.int.getRawAsOf(d2).ints === List(1, 2)
    Ns.int.getRawAsOf(d3).ints === List(1, 2, 3)
    Ns.int.getRawAsOf(d3, 2).ints === List(1, 2)
  }
}
