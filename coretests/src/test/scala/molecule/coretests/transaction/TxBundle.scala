package molecule.coretests.transaction

import molecule.api.out4._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest.Ns
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TxBundle extends CoreSpec {


  "Transact multiple molecules" in new CoreSetup {

    // Initial data
    val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

    // Transact multiple molecule statements in one bundled transaction
transact(
  // retract
  e1.getRetractTx,
  // save
  Ns.int(4).getSaveTx,
  // insert
  Ns.int.getInsertTx(List(5, 6)),
  // update
  Ns(e2).int(20).getUpdateTx
)

    // Data after group transaction
    Ns.int.get.sorted === List(
      // 1 retracted
      3, // unchanged
      4, // saved
      5, 6, // inserted
      20 // 2 updated
    )

    // Can't transact conflicting datoms
    (transact(
      Ns(e3).int(31).getUpdateTx,
      Ns(e3).int(32).getUpdateTx
    ) must throwA[java.util.concurrent.ExecutionException]).message ===
      "Got the exception java.util.concurrent.ExecutionException: java.lang.IllegalArgumentException: " +
        ":db.error/datoms-conflict Two datoms in the same transaction conflict\n" +
        "{:d1 [17592186045447 :ns/int 31 13194139534348 true],\n" +
        " :d2 [17592186045447 :ns/int 32 13194139534348 true]}\n"
  }


  "Asynchronous" in new CoreSetup {

    Await.result(
      transactAsync(
        Ns.int(1).getSaveTx,
        Ns.str("a").getSaveTx
      ) map { txReport =>
        Ns.int.get === List(1)
        Ns.str.get === List("a")
      },
      2.seconds
    )
  }


  "Debug" in new CoreSetup {

    // Initial data
    val List(e1, e2, e3) = Ns.int insert List(1, 2, 3) eids

    // Print debug info for group transaction without affecting live db
    debugTransact(
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
      ========================================================================
      1          ArrayBuffer(
        1          List(
          1          :db.fn/retractEntity   17592186045445)
        2          List(
          1          :db/add       #db/id[:db.part/user -1000247]     :ns/int          4           Card(1))
        3          List(
          1          :db/add       #db/id[:db.part/user -1000252]     :ns/int          5           Card(1))
        4          List(
          1          :db/add       #db/id[:db.part/user -1000253]     :ns/int          6           Card(1))
        5          List(
          1          :db/add       17592186045446                     :ns/int          20          Card(1)))
      ------------------------------------------------
      2          List(
        1    1     added: true ,   t: 13194139534345,   e: 13194139534345,   a: 50,   v: Wed Nov 14 23:38:15 CET 2018

        2    2     added: false,  -t: 13194139534345,  -e: 17592186045445,  -a: 64,  -v: 1

        3    3     added: true ,   t: 13194139534345,   e: 17592186045450,   a: 64,   v: 4

        4    4     added: true ,   t: 13194139534345,   e: 17592186045451,   a: 64,   v: 5

        5    5     added: true ,   t: 13194139534345,   e: 17592186045452,   a: 64,   v: 6

        6    6     added: true ,   t: 13194139534345,   e: 17592186045446,   a: 64,   v: 20
             7     added: false,  -t: 13194139534345,  -e: 17592186045446,  -a: 64,  -v: 2)
      ========================================================================
    */

    // Live data unchanged
    Ns.int.get === List(1, 2, 3)

    // If a real group transaction is invoked, the resulting tx report can also be debugged
    val tx = transact(
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
