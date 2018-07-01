package molecule.coretests.time

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import org.specs2.mutable._
import org.specs2.specification.Scope
import molecule.facade.Conn


class TestDbAsOf extends Specification with Scope {

  sequential

  def init(implicit conn: Conn) = {
    val tx1 = Ns.int(1).save
    val eid = tx1.eid
    val tx2 = Ns(eid).int(2).update
    (tx1, eid, tx2)
  }

  "Local molecules" >> {

    "as of now" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      "Current live state" >> {
        Ns.int.get === List(2)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use current state as a test db "branch"
        conn.testDbAsOfNow

        // Test state is same as live state
        Ns.int.get === List(2)

        // Update test db
        Ns(eid).int(3).update

        // Updated test state
        Ns.int.get === List(3)
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(2)
      }
    }


    "as of tx" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      "Current live state" >> {
        Ns.int.get === List(2)
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated up to tx1 (inclusive) as a test db "branch"
        conn.testDbAsOf(tx1)

        // Test state is now as of tx1!
        Ns.int.get === List(1)

        // Updated test state
        Ns(eid).int(3).update

        // Updated test state
        Ns.int.get === List(3)
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(2)
      }
    }


    "as of t" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      // Sequential transaction number t
      val t1 = tx1.t


      "Current live state" >> {
        Ns.int.get === List(2)
      }

      "As of t" >> {
        // Use state accumulated up to t1 (inclusive) as a test db "branch"
        conn.testDbAsOf(t1)

        // Test state is now as of t1!
        Ns.int.get === List(1)

        // Update test db
        Ns(eid).int(3).update

        // Updated test state
        Ns.int.get === List(3)
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(2)
      }
    }


    "as of date" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      // Transaction date
      val date1 = tx1.inst

      "Current live state" >> {
        Ns.int.get === List(2)
      }

      "As of date" >> {
        // Use state accumulated up to date1 (inclusive) as a test db "branch"
        conn.testDbAsOf(date1)

        // Test state is now as of date1!
        Ns.int.get === List(1)

        // Update test db
        Ns(eid).int(3).update

        // Updated test state
        Ns.int.get === List(3)
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        Ns.int.get === List(2)
      }
    }
  }


  "Molecules in domain objects" >> {

    "as of now" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      // Some domain object
      val counter = domain.Counter(eid)

      "Current live state" >> {
        // Domain object molecules retrieves db value using the implicit connection parameter
        counter.value === 2
      }

      "Use test db for multiple queries/transactions" >> {
        // Use current state as a test db "branch"
        conn.testDbAsOfNow

        // Test state is same as live state
        // Notice that the test db value propagates to our domain object
        // through the implicit conn parameter.
        counter.value === 2

        // Update test db through domain process
        counter.incr

        // Updated test state
        counter.value === 3
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        counter.value === 2
      }
    }


    "as of tx" >> {
      implicit val conn = recreateDbFrom(CoreTestSchema)
      val (tx1, eid, tx2) = init

      // Some domain object
      val counter = domain.Counter(eid)

      "Current live state" >> {
        // Domain object molecules retrieves db value using the implicit connection parameter
        counter.value === 2
      }

      "Use test db for multiple queries/transactions" >> {
        // Use state accumulated up to tx1 (inclusive) as a test db "branch"
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
      }

      "Live db is unchanged" >> {
        // Discard test db and go back to live db
        conn.useLiveDb

        // Current live state is correctly unchanged
        counter.value === 2
      }
    }
  }
}