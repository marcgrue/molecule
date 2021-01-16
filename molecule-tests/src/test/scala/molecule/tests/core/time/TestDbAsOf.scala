package molecule.tests.core.time

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out2._
import molecule.TestSpec
import molecule.tests.core.time.domain.Counter


class TestDbAsOf extends TestSpec {

  class Setup extends CoreSetup {
    val txR1    = Ns.int(1).save
    val txR2    = Ns.int(2).save
    val txR3    = Ns.int(3).save
    val e1      = txR1.eid
    val e2      = txR2.eid
    val e3      = txR3.eid
    val counter = Counter(e1)
  }


  "as of now" in new Setup {

    // Live state
    Ns.int.get === List(1, 2, 3)

    // Use current state as a test db "branch"
    conn.testDbAsOfNow

    // Test state is currently same as live state
    Ns.int.get === List(1, 2, 3)

    // Test operations:

    // Save
    Ns.int(4).save
    Ns.int.get.sorted === List(1, 2, 3, 4)

    // Insert
    Ns.int insert List(5, 6)
    Ns.int.get.sorted === List(1, 2, 3, 4, 5, 6)

    // Update
    Ns(e2).int(0).update
    Ns.int.get.sorted === List(0, 1, 3, 4, 5, 6)

    // Retract
    e3.retract
    Ns.int.get.sorted === List(0, 1, 4, 5, 6)

    // Live state unchanged
    conn.useLiveDb
    Ns.int.get === List(1, 2, 3)
  }


  "as of: input types" in new Setup {
    Thread.sleep(10)
    val txR4 = Ns.int(4).save
    Thread.sleep(10)
    val txR5 = Ns.int(5).save

    // Original state
    Ns.int.get === List(1, 2, 3, 4, 5)

    // as of tx report
    conn.testDbAsOf(txR1)
    Ns.int.get === List(1)

    // as of t
    conn.testDbAsOf(txR2.t)
    Ns.int.get === List(1, 2)

    // as of tx
    conn.testDbAsOf(txR3.tx)
    Ns.int.get === List(1, 2, 3)

    // as of date
    conn.testDbAsOf(txR4.inst)
    Ns.int.get === List(1, 2, 3, 4)

    // Original state unaffected
    conn.useLiveDb
    Ns.int.get === List(1, 2, 3, 4, 5)
  }


  "as of: operations" in new Setup {
    // Live state
    Ns.int.get === List(1, 2, 3)

    // Use state as of tx 2 as a test db "branch"
    conn.testDbAsOf(txR2)

    // Test state
    Ns.int.get === List(1, 2)

    // Test operations:

    // Save
    Ns.int(4).save
    Ns.int.get.sorted === List(1, 2, 4)

    // Insert
    Ns.int insert List(5, 6)
    Ns.int.get.sorted === List(1, 2, 4, 5, 6)

    // Update
    Ns(e2).int(0).update
    Ns.int.get.sorted === List(0, 1, 4, 5, 6)

    // Retract
    e1.retract
    Ns.int.get.sorted === List(0, 4, 5, 6)

    // Live state unchanged
    conn.useLiveDb
    Ns.int.get === List(1, 2, 3)
  }


  "domain: as of now" in new Setup {

    // Live state
    counter.value === 1
    Ns.int.get === List(1, 2, 3)

    // Use current state as a test db "branch"
    conn.testDbAsOfNow

    // Test state is same as live state
    // Notice that the test db value propagates to our domain object
    // through the implicit conn parameter.
    counter.value === 1
    Ns.int.get === List(1, 2, 3)

    // Update test db through domain process
    counter.incr

    // Updated test state
    counter.value === 11
    Ns.int.get === List(2, 3, 11)

    // Discard test db and go back to live db
    conn.useLiveDb

    // Live state unchanged
    counter.value === 1
    Ns.int.get === List(1, 2, 3)
  }


  "domain: as of tx" in new Setup {

    // Live state
    counter.value === 1
    Ns.int.get === List(1, 2, 3)

    // Use state as of tx 2 as a test db "branch"
    conn.testDbAsOf(txR2)
    Ns.int.get === List(1, 2)

    // Test state is now as of tx1!
    // Notice that the test db value propagates to our domain object
    // through the implicit conn parameter.
    counter.value === 1

    // Update test db twice through domain process
    counter.incr === 11
    counter.incr === 21

    // Updated test state
    counter.value === 21
    Ns.int.get === List(2, 21)

    // Discard test db and go back to live db
    conn.useLiveDb

    // Live state unchanged
    counter.value === 1
    Ns.int.get === List(1, 2, 3)
  }
}