package molecule.tests.core.transaction

import molecule.core.util.DatomicPeer
import molecule.datomic.api.out1._
import molecule.TestSpec
import molecule.tests.core.base.dsl.coreTest.Ns
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TxBundle extends TestSpec {

  "Transact multiple molecules" in new CoreSetup {

    // Initial data
    val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

    // State before
    Ns.int.get.sorted === List(1, 2, 3)

    // Transact multiple molecule statements in one bundled transaction
    transactBundle(
      // retract
      e1.getRetractTx,
      // save
      Ns.int(4).getSaveTx,
      // insert
      Ns.int.getInsertTx(List(5, 6)),
      // update
      Ns(e2).int(20).getUpdateTx
    )

    // State after transaction bundle
    Ns.int.get.sorted === List(
      // 1 retracted
      3, // unchanged
      4, // saved
      5, 6, // inserted
      20 // 2 updated
    )

    // Can't transact conflicting datoms
    // (different systems throws different exceptions)
    if (system == DatomicPeer) {
      (transactBundle(
        Ns(e3).int(31).getUpdateTx,
        Ns(e3).int(32).getUpdateTx
      ) must throwA[java.util.concurrent.ExecutionException]).message ===
        "Got the exception java.util.concurrent.ExecutionException: java.lang.IllegalArgumentException: " +
          ":db.error/datoms-conflict Two datoms in the same transaction conflict\n" +
          "{:d1 [17592186045455 :Ns/int 31 13194139534356 true],\n" +
          " :d2 [17592186045455 :Ns/int 32 13194139534356 true]}\n"
    }
  }


    "Asynchronous" in new CoreSetup {

      // todo: Async implementation for systems other than Peer
      if (system == DatomicPeer) {
        Await.result(
          transactBundleAsync(
            Ns.int(1).getSaveTx,
            Ns.str("a").getSaveTx
          ) map { txReport =>
            Ns.int.get === List(1)
            Ns.str.get === List("a")
          },
          2.seconds
        )
      }
    }


    "Debug" in new CoreSetup {

      // Initial data
      val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

      // Print debug info for group transaction without affecting live db
      debugTransactBundle(
        // retract
        e1.getRetractTx,
        // save
        Ns.int(4).getSaveTx,
        // insert
        Ns.int.getInsertTx(List(5, 6)),
        // update
        Ns(e2).int(20).getUpdateTx
      )

      // Prints something like this:
      // 1st group: List of generic statements
      // 2nd group: tx timestamp
      //            tx datoms from dbAfter (op - tx id - entity - attribute - value)
      //            (minus added to help indicate retractions)
      /*
  ## 1 ## TxReport
  ================================================================================================================
  1          ArrayBuffer(
    1          List(
      1          :db.fn/retractEntity     17592186045454)
    2          List(
      1          :db/add      #db/id[:db.part/user -1000267]     :Ns/int                4)
    3          List(
      1          :db/add      #db/id[:db.part/user -1000271]     :Ns/int                5)
    4          List(
      1          :db/add      #db/id[:db.part/user -1000274]     :Ns/int                6)
    5          List(
      1          :db/add      17592186045455                     :Ns/int                20))
  ----------------------------------------------------------------------------------------------------------------
  2          List(
    1    1     added: true ,   t: 13194139534354,   e: 13194139534354,   a: 50,   v: Thu Dec 19 20:34:15 CET 2019

    2    2     added: false,  -t: 13194139534354,  -e: 17592186045454,  -a: 64,  -v: 1

    3    3     added: true ,   t: 13194139534354,   e: 17592186045459,   a: 64,   v: 4

    4    4     added: true ,   t: 13194139534354,   e: 17592186045460,   a: 64,   v: 5

    5    5     added: true ,   t: 13194139534354,   e: 17592186045461,   a: 64,   v: 6

    6    6     added: true ,   t: 13194139534354,   e: 17592186045455,   a: 64,   v: 20
         7     added: false,  -t: 13194139534354,  -e: 17592186045455,  -a: 64,  -v: 2)
  ================================================================================================================
      */

      // Live data unchanged
      Ns.int.get === List(1, 2, 3)

      // If a real group transaction is invoked, the resulting tx report can also be debugged
      val tx = transactBundle(
        // retract
        e1.getRetractTx,
        // save
        Ns.int(4).getSaveTx,
        // insert
        Ns.int.getInsertTx(List(5, 6)),
        // update
        Ns(e2).int(20).getUpdateTx
      )

      // Will print the same as calling `debugTransact(...)`
      tx.debug

      // Live data has now changed
      Ns.int.get.sorted === List(
        // 1 retracted
        3, // unchanged
        4, // saved
        5, 6, // inserted
        20 // 2 updated
      )
    }
}
