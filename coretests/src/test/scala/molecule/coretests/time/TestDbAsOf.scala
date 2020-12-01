package molecule.coretests.time

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out2._
import org.specs2.mutable._
import org.specs2.specification.Scope
import molecule.datomic.peer.facade.Datomic_Peer._


class TestDbAsOf extends CoreSpec {

  // Since we already use a testDbAsOfNow for Peer Server, we don't test it here.
  // See special TestDbAsOf_PeerServer
  omitPeerServer = true

  class Setup extends CoreSetup {
    val txR1    = Ns.int(1).save
    val eid     = txR1.eid
    val counter = domain.Counter(eid)
    val tx1      = txR1.tx
    val t1      = txR1.t
    val date1   = txR1.inst
    val txR2    = Ns(eid).int(2).update
  }


  "Local molecules" >> {

    "as of now" in new Setup {
      // Live state
      Ns.int.get === List(2)

      // Use current state as a test db "branch"
      conn.testDbAsOfNow

      // Test state is same as live state
      Ns.int.get === List(2)

      // Update test db
      Ns(eid).int(3).update
      Ns.int.get === List(3)

      Ns.int(4).save
      Ns.int.get === List(3, 4)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      Ns.int.get === List(2)
    }

    "as of tx report" in new Setup {
      // Live state
      Ns.int.get === List(2)

      // Use state as of tx1 as a test db "branch"
      conn.testDbAsOf(txR1)

      // Test state is now as of tx1!
      Ns.int.get === List(1)

      // Updated test state
      Ns(eid).int(3).update

      // Updated test state
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      Ns.int.get === List(2)
    }

    "as of tx" in new Setup {
      // Live state
      Ns.int.get === List(2)

      // Use state as of tx1 as a test db "branch"
      conn.testDbAsOf(tx1)

      // Test state is now as of tx1!
      Ns.int.get === List(1)

      // Updated test state
      Ns(eid).int(3).update

      // Updated test state
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      Ns.int.get === List(2)
    }

    "as of t" in new Setup {
      // Live state
      Ns.int.get === List(2)

      // Use state as of t1 as a test db "branch"
      conn.testDbAsOf(t1)

      // Test state is now as of t1!
      Ns.int.get === List(1)

      // Update test db
      Ns(eid).int(3).update

      // Updated test state
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      Ns.int.get === List(2)
    }

    "as of date" in new Setup {
      // Live state
      Ns.int.get === List(2)

      // Use state as of date1 as a test db "branch"
      conn.testDbAsOf(date1)

      // Test state is now as of date1!
      Ns.int.get === List(1)

      // Update test db
      Ns(eid).int(3).update

      // Updated test state
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      Ns.int.get === List(2)
    }
  }


  "Molecules in domain objects" >> {

    "as of now" in new Setup {
      // Live state
      counter.value === 2
      Ns.int.get === List(2)

      // Use current state as a test db "branch"
      conn.testDbAsOfNow

      // Test state is same as live state
      // Notice that the test db value propagates to our domain object
      // through the implicit conn parameter.
      counter.value === 2
      Ns.int.get === List(2)

      // Update test db through domain process
      counter.incr

      // Updated test state
      counter.value === 3
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      counter.value === 2
      Ns.int.get === List(2)
    }

    "as of tx" in new Setup {
      // Live state
      counter.value === 2
      Ns.int.get === List(2)

      // Use state as of tx1 as a test db "branch"
      conn.testDbAsOf(tx1)

      // Test state is now as of tx1!
      // Notice that the test db value propagates to our domain object
      // through the implicit conn parameter.
      counter.value === 1

      // Update test db twice through domain process
      counter.incr === 2
      counter.incr === 3

      // Updated test state
      counter.value === 3
      Ns.int.get === List(3)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Live state unchanged
      counter.value === 2
      Ns.int.get === List(2)
    }
  }
}