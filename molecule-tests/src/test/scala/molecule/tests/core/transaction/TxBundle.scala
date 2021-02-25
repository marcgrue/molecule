package molecule.tests.core.transaction

import molecule.datomic.api.out1._
import molecule.datomic.base.util.SystemPeer
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._
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
      e1.getRetractStmts,
      // save
      Ns.int(4).getSaveStmts,
      // insert
      Ns.int.getInsertStmts(List(5, 6)),
      // update
      Ns(e2).int(20).getUpdateStmts
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
    if (system == SystemPeer) {
      (transactBundle(
        Ns(e3).int(31).getUpdateStmts,
        Ns(e3).int(32).getUpdateStmts
      ) must throwA[java.util.concurrent.ExecutionException]).message ===
        "Got the exception java.util.concurrent.ExecutionException: java.lang.IllegalArgumentException: " +
          ":db.error/datoms-conflict Two datoms in the same transaction conflict\n" +
          "{:d1 [17592186045455 :Ns/int 31 13194139534356 true],\n" +
          " :d2 [17592186045455 :Ns/int 32 13194139534356 true]}\n"
    }
  }


    "Asynchronous" in new CoreSetup {

      // todo: Async implementation for systems other than Peer
      if (system == SystemPeer) {
        Await.result(
          transactBundleAsync(
            Ns.int(1).getSaveStmts,
            Ns.str("a").getSaveStmts
          ) map { txReport =>
            Ns.int.get === List(1)
            Ns.str.get === List("a")
          },
          2.seconds
        )
      }
    }


    "Inspect" in new CoreSetup {

      // Initial data
      val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

      // Print inspect info for group transaction without affecting live db
      inspectTransactBundle(
        // retract
        e1.getRetractStmts,
        // save
        Ns.int(4).getSaveStmts,
        // insert
        Ns.int.getInsertStmts(List(5, 6)),
        // update
        Ns(e2).int(20).getUpdateStmts
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

      // If a real group transaction is invoked, the resulting tx report can also be inspected
      val tx = transactBundle(
        // retract
        e1.getRetractStmts,
        // save
        Ns.int(4).getSaveStmts,
        // insert
        Ns.int.getInsertStmts(List(5, 6)),
        // update
        Ns(e2).int(20).getUpdateStmts
      )

      // Will print the same as calling `inspectTransact(...)`
      tx.inspect

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
