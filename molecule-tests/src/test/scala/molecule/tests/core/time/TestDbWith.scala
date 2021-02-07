package molecule.tests.core.time

import molecule.datomic.api.out1._
import molecule.tests.core.base.dsl.CoreTest._
import molecule.TestSpec
import molecule.tests.core.time.domain.Crud


class TestDbWith extends TestSpec {

  class Setup extends CoreSetup {
    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids
  }

  "with single tx" in new Setup {

    // Current live state
    Ns.int.get === List(1, 2, 3)

    // Use current state with extra save tx as a test db "branch"
    conn.testDbWith(
      Ns.int(4).getSaveStmts
    )

    // Adjusted test state to work on
    Ns.int.get === List(1, 2, 3, 4)

    // Update
    Ns(e1).int(0).update.tx
    Ns.int.get.sorted === List(0, 2, 3, 4)

    // Retract
    e2.retract
    Ns.int.get.sorted === List(0, 3, 4)

    // Save
    Ns.int(5).save
    Ns.int.get.sorted === List(0, 3, 4, 5)

    // Discard test db and go back to live db
    conn.useLiveDb

    // Current live state is correctly unchanged
    Ns.int.get.sorted === List(1, 2, 3)
  }

  "with multiple txs" in new Setup {

    // Current live state
    Ns.int.get.sorted === List(1, 2, 3)

    // Apply a set of transactions to get a
    // test db in a certain state
    conn.testDbWith(
      // --> List(1, 2, 3, 4)
      Ns.int(4).getSaveStmts,

      // --> List(1, 2, 3, 4, 5, 6)
      Ns.int getInsertStmts List(5, 6),

      // --> List(0, 2, 3, 4, 5, 6)
      Ns(e1).int(0).getUpdateStmts,

      // --> List(0, 3, 4, 5, 6)
      e2.getRetractStmts
    )

    // Adjusted test state to work on
    Ns.int.get.sorted === List(0, 3, 4, 5, 6)


    // 3 -> 7
    Ns(e3).int(7).update

    // Updated test state
    Ns.int.get.sorted === List(0, 4, 5, 6, 7)

    // add 8
    Ns.int(8).save.eid
    Ns.int.get.sorted === List(0, 4, 5, 6, 7, 8)

    // remove entity 1 (value 0)
    Ns(e1).int().update
    Ns.int.get.sorted === List(4, 5, 6, 7, 8)

    // retract entity 3 (value 7)
    e3.retract
    Ns.int.get.sorted === List(4, 5, 6, 8)

    // Etc...

    // Discard test db and go back to live db
    conn.useLiveDb

    // Current live state is correctly unchanged
    Ns.int.get.sorted === List(1, 2, 3)
  }


  "with multiple modularized txs" in new Setup {

    // Current live state
    Ns.int.get.sorted === List(1, 2, 3)


    val save    = Ns.int(4).getSaveStmts
    val insert  = Ns.int getInsertStmts List(5, 6)
    val update  = Ns(e1).int(0).getUpdateStmts
    val retract = e2.getRetractStmts

    // Apply a set of saved modularized transactions to get a
    // test db in a certain state.
    // Could be combined/ordered flexibly to bring the test db in various states.
    conn.testDbWith(
      save,
      insert,
      update,
      retract
    )

    // Adjusted test state to work on
    Ns.int.get.sorted === List(0, 3, 4, 5, 6)

    // 3 -> 7
    Ns(e3).int(7).update
    Ns.int.get.sorted === List(0, 4, 5, 6, 7)

    // add 8
    Ns.int(8).save.eid
    Ns.int.get.sorted === List(0, 4, 5, 6, 7, 8)

    // remove entity 1 (value 0)
    Ns(e1).int().update
    Ns.int.get.sorted === List(4, 5, 6, 7, 8)

    // retract entity 3 (value 7)
    e3.retract
    Ns.int.get.sorted === List(4, 5, 6, 8)

    // Etc...

    // Discard test db and go back to live db
    conn.useLiveDb

    // Current live state is correctly unchanged
    Ns.int.get.sorted === List(1, 2, 3)
  }


  "Molecules in domain objects" >> {

    "with single tx" in new Setup {

      // Some domain object
      val crud = Crud

      // Current live state
      crud.read === List(1, 2, 3)

      // Use state with extra save tx as a test db "branch"
      conn.testDbWith(
        Ns.int(4).getSaveStmts
      )

      // Adjusted test state to work on
      crud.read === List(1, 2, 3, 4)

      // Update test db
      crud.update(1 -> 0)

      // Updated test state
      crud.read === List(0, 2, 3, 4)

      // Discard test db and go back to live db
      conn.useLiveDb

      // Current live state is correctly unchanged
      crud.read === List(1, 2, 3)
    }


    "with multiple txs" in new Setup {

      // Some domain object
      val crud = domain.Crud

      // Current live state
      crud.read === List(1, 2, 3)

      // Apply a set of transactions to get a
      // test db in a certain state
      conn.testDbWith(
        // --> List(1, 2, 3, 4)
        Ns.int(4).getSaveStmts,

        // --> List(1, 2, 3, 4, 5, 6)
        Ns.int getInsertStmts List(5, 6),

        // --> List(0, 2, 3, 4, 5, 6)
        Ns(e1).int(0).getUpdateStmts,

        // --> List(0, 3, 4, 5, 6)
        e2.getRetractStmts
      )

      // Adjusted test state to work on
      crud.read === List(0, 3, 4, 5, 6)


      // 3 -> 7
      crud.update(3 -> 7)

      // Updated test state
      crud.read === List(0, 4, 5, 6, 7)

      // add 8
      crud.create(8)
      crud.read === List(0, 4, 5, 6, 7, 8)

      // remove value 0
      crud.delete(0)
      crud.read === List(4, 5, 6, 7, 8)

      // retract entity 3 (value 7)
      // We can mix domain updates with local changes here
      e3.retract
      crud.read === List(4, 5, 6, 8)

      // Test updating non-existing value
      (crud.update(2 -> 42) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        "Old number (2) doesn't exist in db."

      // Etc...

      // Discard test db and go back to live db
      conn.useLiveDb

      // Current live state is correctly unchanged
      crud.read === List(1, 2, 3)
    }
  }
}