package molecule.coretests.crud

import molecule.core.util.DatomicPeer
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out4._
import scala.concurrent.ExecutionContext.Implicits.global


class UpdateMultipleAttributes extends CoreSpec {

  "Async" in new CoreSetup {

    // todo: remove when async implemented for other systems
    if (system == DatomicPeer) {

      // Update multiple attributes of an entity asynchronously and return Future[TxReport]
      // Calls Datomic's transactAsync API

      // Initial data
      Ns.int.str insertAsync List((1, "a"), (2, "b")) map { tx => // tx report from successful insert transaction
        // 2 inserted entities
        val List(e1, e2) = tx.eids
        Ns.int.get === List((1, "a"), (2, "b"))

        // Update second entity asynchronously
        Ns(e2).int(42).str("hello world").updateAsync.map { tx2 => // tx report from successful update transaction
          // Current data
          Ns.int.get === List((1, "a"), (42, "hello world"))
        }
      }

      // For brevity, the synchronous equivalent `update` is used in the following tests
    }
  }


  "Updating same/new values" >> {

    "both new" in new CoreSetup {
      val eid = Ns.int(1).str("a").save.eid

      // Both int and str asserted
      Ns(eid).int(2).str("b").update

      Ns.int.get === List(2)
      Ns.str.get === List("b")
      Ns.e.int.str.get.head === (eid, 2, "b")
    }

    "first new, last same" in new CoreSetup {
      val eid = Ns.int(1).str("a").save.eid

      // Only int asserted
      Ns(eid).int(2).str("a").update

      Ns.int.get === List(2)
      Ns.str.get === List("a")
      Ns.e.int.str.get.head === (eid, 2, "a")
    }

    "first same, last new" in new CoreSetup {
      val eid = Ns.int(1).str("a").save.eid

      // Only str asserted
      Ns(eid).int(1).str("b").update

      Ns.int.get === List(1)
      Ns.str.get === List("b")
      Ns.e.int.str.get.head === (eid, 1, "b")
    }

    "both same" in new CoreSetup {
      val eid = Ns.int(1).str("a").save.eid

      // No facts asserted!
      Ns(eid).int(1).str("a").update

      Ns.int.get === List(1)
      Ns.str.get === List("a")
      Ns.e.int.str.get.head === (eid, 1, "a")
    }


    "2 new, 1 same" in new CoreSetup {
      val eid = Ns.int(1).str("a").bool(true).save.eid

      Ns(eid).int(2).str("b").bool(true).update

      Ns.int.get === List(2)
      Ns.str.get === List("b")
      Ns.bool.get === List(true)
      Ns.e.int.str.bool.get.head === (eid, 2, "b", true)
    }

    "2 same, 1 new" in new CoreSetup {
      val eid = Ns.int(1).str("a").bool(true).save.eid

      Ns(eid).int(1).str("a").bool(false).update

      Ns.int.get === List(1)
      Ns.str.get === List("a")
      Ns.bool.get === List(false)
      Ns.e.int.str.bool.get.head === (eid, 1, "a", false)
    }
  }


  "Optional values - update or retract" in new CoreSetup {
    val eid = Ns.int(1).str("a").save.eid

    // Both values updated
    Ns(eid).int(2).str$(Some("b")).update
    Ns.int.str$.get.head === (2, Some("b"))

    // str retracted
    Ns(eid).int(3).str$(None).update
    Ns.int.str$.get.head === (3, None)

    // Reversing positions

    Ns(eid).str$(Some("d")).int(4).update
    Ns.str$.int.get.head === (Some("d"), 4)

    Ns(eid).str$(None).int(5).update
    Ns.str$.int.get.head === (None, 5)
  }
}
