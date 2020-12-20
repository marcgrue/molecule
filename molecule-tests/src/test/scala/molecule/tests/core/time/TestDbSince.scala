package molecule.tests.core.time

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out1._
import molecule.TestSpec
import molecule.tests.core.time.domain.Crud


class TestDbSince extends TestSpec {

  class Setup extends CoreSetup {
    val txR1 = Ns.int(1).save
    val txR2 = Ns.int(2).save
    val txR3 = Ns.int(3).save
    val e2   = txR2.eid
    val e3   = txR3.eid
  }

  // Seems like a bug that we can't apply filter to with-db with peer-server
  tests = 13

  "since: input types" in new Setup {

    // Ensure to match dates beeing at least 1 ms apart
    Thread.sleep(1)
    val txR4 = Ns.int(4).save
    val txR5 = Ns.int(5).save

    // Live state
    Ns.int.get === List(1, 2, 3, 4, 5)

    // since tx report
    conn.testDbSince(txR1)
    Ns.int.get === List(2, 3, 4, 5)

    // since t
    conn.testDbSince(txR2.t)
    Ns.int.get === List(3, 4, 5)

    // since tx
    conn.testDbSince(txR3.tx)
    Ns.int.get === List(4, 5)

    // since date
    conn.testDbSince(txR4.inst)
    Ns.int.get === List(5)

    // Live state unaffected
    conn.useLiveDb
    Ns.int.get === List(1, 2, 3, 4, 5)
  }


  "since: operations" in new Setup {

    // Live state
    Ns.int.get.sorted === List(1, 2, 3)

    // Use state accumulated since tx1 (exclusive) as a test db "branch"
    conn.testDbSince(txR1)

    // Test state since tx1 (exclusive)
    Ns.int.get.sorted === List(2, 3)

    // Test operations:

    // Save
    Ns.int(4).save
    Ns.int.get.sorted === List(2, 3, 4)

    // Insert
    Ns.int insert List(5, 6)
    Ns.int.get.sorted === List(2, 3, 4, 5, 6)

    // Update
    Ns(e2).int(0).update
    Ns.int.get.sorted === List(0, 3, 4, 5, 6)

    // Retract
    e3.retract
    Ns.int.get.sorted === List(0, 4, 5, 6)

    // Live state unaffected
    conn.useLiveDb
    Ns.int.get.sorted === List(1, 2, 3)
  }


  "since: testing domain" in new Setup {

    // Some domain object
    val crud = Crud

    // Live state
    crud.read === List(1, 2, 3)

    // Use state accumulated since tx1 (exclusive) as a test db "branch"
    conn.testDbSince(txR1)

    // Test state since tx1 (exclusive)
    crud.read === List(2, 3)

    // Update test db through domain process
    crud.create(4, 5)

    // Test db now has 4 values
    crud.read === List(2, 3, 4, 5)

    // Mutate test db through domain object
    crud.delete(1) // not from test db
    crud.delete(3, 4) // from test db

    // Updated test state
    crud.read === List(2, 5)

    // Discard test db and go back to live db
    conn.useLiveDb

    // Live state unchanged
    crud.read === List(1, 2, 3)
  }
}