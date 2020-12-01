package molecule.coretests.time

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out5._


class TestDbAsOf_PeerServer extends CoreSpec {

  // Since we testDbAsOfNow for Peer Server, we organize the tests a bit differently.
  peerServerOnly = true

  class Setup extends CoreSetup {

    // All operations happen within conn.testDbAsOfNow on empty database

    //    // Empty "live" state
    //    Ns.int.get === Nil
    //    val txR1    = Ns.int(1).save
    //    val eid     = txR1.eid
    //    val counter = domain.Counter(eid)
    //    val tx1     = txR1.tx
    //    val t1      = txR1.t
    //    val date1   = txR1.inst
    //    val tx2     = Ns(eid).int(2).update.tx
  }


  "Local molecules" >> {

//    "as of now" in new Setup {
//      // db as of now already set in setup
//      // conn.testDbAsOfNow
//
//      // Live state
//      Ns.int.get === Nil
//      Ref4.int4.get === List(1, 2, 3)
//
//      // do 2 txs in test state
//
//      val e1 = Ns.int(1).save.eid
//      Ns.int.get === List(1)
//
//      // Update test db
//      Ns(e1).int(2).update
//      Ns.int.get === List(2)
//
//      // Discard test db and go back to live db
//      conn.useLiveDb
//
//      // empty live state unchanged
//      Ns.int.get === Nil
//    }

    "as of tx report" in new Setup {
      // db as of now already set in setup
      // conn.testDbAsOfNow

//      conn.useLiveDb

      // Live state (from before dbAsOfNow)
      Ref4.int4.get === List(1, 2, 3)

      val (e2, tx2) = Ref4.int4_(2).e.tx.get.head

      conn.testDbAsOf(tx2)

      Ref4.int4.get === List(1, 2)

      Ref4(e2).int4(-2).update
      Ref4.int4.get === List(1, -2)

      Ref4.int4(4).save

      Log(None, None).tx.e.a.v.get.takeRight(10) foreach println

      Ref4.int4.get === List(1, 2, 4)

      // Back to live state
      conn.useLiveDb
      Ref4.int4.get === List(1, 2, 3)

      //      conn.testDbAsOfNow

      //      //      conn.testDbAsOf(tx1)
      //      conn.testDbAsOf2(tx1)
      //
      //      // Test state is now as of tx1!
      //      //      Ns.int.get === List(2)
      //      Ns.int.get === List(1)
      //
      //      // Updated test state
      //      Ns(eid).int(3).update
      //
      //      // Updated test state
      //      Ns.int.get === List(3)
      //
      //      // Discard test db and go back to live db
      //      conn.useLiveDb
      //
      //      // Live state unchanged
      //      Ns.int.get === Nil
    }

    //    "as of tx" in new Setup {
    //      // Live state
    //      Ns.int.get === List(2)
    //
    //      // Use state as of tx1 as a test db "branch"
    //      conn.useLiveDb
    //      //      conn.testDbAsOf(tx1)
    //
    //      // Test state is now as of tx1!
    //      Ns.int.get === List()
    //      Ns.int.get === List(1)
    //
    //      // Updated test state
    //      Ns(eid).int(3).update
    //
    //      // Updated test state
    //      Ns.int.get === List(3)
    //
    //      // Discard test db and go back to live db
    //      conn.useLiveDb
    //
    //      // Live state unchanged
    //      Ns.int.get === List(2)
    //    }
    //
    //    "as of t" in new Setup {
    //      // Live state
    //      Ns.int.get === List(2)
    //
    //      // Use state as of t1 as a test db "branch"
    //      conn.testDbAsOf(t1)
    //
    //      // Test state is now as of t1!
    //      Ns.int.get === List(1)
    //
    //      // Update test db
    //      Ns(eid).int(3).update
    //
    //      // Updated test state
    //      Ns.int.get === List(3)
    //
    //      // Discard test db and go back to live db
    //      conn.useLiveDb
    //
    //      // Live state unchanged
    //      Ns.int.get === List(2)
    //    }
    //
    //    "as of date" in new Setup {
    //      // Live state
    //      Ns.int.get === List(2)
    //
    //      // Use state as of date1 as a test db "branch"
    //      conn.testDbAsOf(date1)
    //
    //      // Test state is now as of date1!
    //      Ns.int.get === List(1)
    //
    //      // Update test db
    //      Ns(eid).int(3).update
    //
    //      // Updated test state
    //      Ns.int.get === List(3)
    //
    //      // Discard test db and go back to live db
    //      conn.useLiveDb
    //
    //      // Live state unchanged
    //      Ns.int.get === List(2)
    //    }
  }


  //  "Molecules in domain objects" >> {
  //
  //    "as of now" in new Setup {
  //      // Live state
  //      counter.value === 2
  //      Ns.int.get === List(2)
  //
  //      // Use current state as a test db "branch"
  //      conn.testDbAsOfNow
  //
  //      // Test state is same as live state
  //      // Notice that the test db value propagates to our domain object
  //      // through the implicit conn parameter.
  //      counter.value === 2
  //      Ns.int.get === List(2)
  //
  //      // Update test db through domain process
  //      counter.incr
  //
  //      // Updated test state
  //      counter.value === 3
  //      Ns.int.get === List(3)
  //
  //      // Discard test db and go back to live db
  //      conn.useLiveDb
  //
  //      // Live state unchanged
  //      counter.value === 2
  //      Ns.int.get === List(2)
  //    }
  //
  //    "as of tx" in new Setup {
  //      // Live state
  //      counter.value === 2
  //      Ns.int.get === List(2)
  //
  //      // Use state as of tx1 as a test db "branch"
  //      conn.testDbAsOf(txR1)
  //
  //      // Test state is now as of tx1!
  //      // Notice that the test db value propagates to our domain object
  //      // through the implicit conn parameter.
  //      counter.value === 1
  //
  //      // Update test db twice through domain process
  //      counter.incr === 2
  //      counter.incr === 3
  //
  //      // Updated test state
  //      counter.value === 3
  //      Ns.int.get === List(3)
  //
  //      // Discard test db and go back to live db
  //      conn.useLiveDb
  //
  //      // Live state unchanged
  //      counter.value === 2
  //      Ns.int.get === List(2)
  //    }
  //  }
}