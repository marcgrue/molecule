package molecule.coretests.time

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import org.specs2.mutable._
import org.specs2.specification.Scope
import molecule.facade.Conn


class TestDbSince extends Specification {

  sequential

  def init(implicit conn: Conn) = {
    val tx1 = Ns.int(1).save
    val tx2 = Ns.int(2).save
    val tx3 = Ns.int(3).save
    val e1 = tx1.eid
    val e2 = tx2.eid
    val e3 = tx3.eid
    (tx1, tx2, tx3, e1, e2, e3)
  }


  "Local molecules" >> {

    "as of tx" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, tx2, tx3, e1, e2, e3) = init

      "Current live state" >> {
        Ns.int.get === List(1, 2, 3)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated since tx1 (exclusive) as a test db "branch"
        conn.testDbSince(tx1)

        // Test state since tx1 (exclusive)
        Ns.int.get === List(2, 3)

        // Add more test data
        Ns.int(4).save
        Ns.int insert List(5, 6)

        // Updated test state
        Ns.int.get === List(2, 3, 4, 5, 6)
      }

      "Live state is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(1, 2, 3)
      }
    }


    "as of t" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, tx2, tx3, e1, e2, e3) = init

      // Sequential transaction number t
      val t1 = tx1.t

      "Current live state" >> {
        Ns.int.get === List(1, 2, 3)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated since t1 (exclusive) as a test db "branch"
        conn.testDbSince(t1)

        // Test state since t1 (exclusive)
        Ns.int.get === List(2, 3)

        // retract data
        tx1.eid.retract // not from the test db
        tx3.eid.retract // from the test db

        // Updated test state
        Ns.int.get === List(2)
      }

      "Live state is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(1, 2, 3)
      }
    }


    "as of date" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, tx2, tx3, e1, e2, e3) = init

      // Transaction date
      val date1 = tx1.inst

      "Current live state" >> {
        Ns.int.get === List(1, 2, 3)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated since date1 (exclusive) as a test db "branch"
        conn.testDbSince(date1)

        // Test state since date1 (exclusive)
        Ns.int.get === List(2, 3)

        // Add and update data
        Ns(e1).int(11).update // not in test db
        Ns(e3).int(13).update // in test db

        // Updated test state
        Ns.int.get === List(2, 11, 13)
      }

      "Live state is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(1, 2, 3)
      }
    }
  }


  "Molecules in domain objects" >> {

    "as of tx" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, tx2, tx3, e1, e2, e3) = init

      // Some domain object
      val crud = domain.Crud

      "Current live state" >> {
        crud.read === List(1, 2, 3)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated since tx1 (exclusive) as a test db "branch"
        conn.testDbSince(tx1)

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
      }

      "Live state is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        crud.read === List(1, 2, 3)
      }
    }
  }
}