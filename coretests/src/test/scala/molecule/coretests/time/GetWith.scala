package molecule.coretests.time

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out2._
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import molecule.datomic.peer.facade.Datomic_Peer._

class GetWith extends CoreSpec {

  class Setup extends CoreSetup {
    // Current state
    val eid = Ns.str("a").int(1).save.eid
  }


  "getSaveTx" in new Setup {

    Ns.int.getWith(Ns.int(2).getSaveTx) === List(1, 2)

    Ns.int.getWith(Ns.str("b").getSaveTx) === List(1)

    Ns.int.getWith(Ns.str("b").int(2).getSaveTx) === List(1, 2)

    Ns.str.getWith(Ns.str("b").int(2).getSaveTx) === List("a", "b")

    Ns.str$.int.getWith(
      Ns.int(2).getSaveTx
    ).sortBy(_._2) === List((Some("a"), 1), (None, 2))

    Ns.str.int.getWith(
      Ns.str("b").int(2).getSaveTx
    ).sortBy(_._2) === List(("a", 1), ("b", 2))

    // Current state unchanged
    Ns.str.int.get === List(("a", 1))
  }


  "getInsertTx" in new Setup {

    Ns.int.getWith(
      Ns.int.getInsertTx(2, 3)
    ) === List(1, 2, 3)

    Ns.str.getWith(
      Ns.str$.int.getInsertTx(Seq(
        (Some("b"), 2),
        (None, 3)
      ))
    ) === List("a", "b")

    Ns.str$.int.getWith(
      Ns.int.getInsertTx(2, 3)
    ).sortBy(_._2) === List(
      (Some("a"), 1),
      (None, 2),
      (None, 3)
    )

    Ns.str$.int.getWith(
      Ns.str$.int.getInsertTx(Seq(
        (Some("b"), 2),
        (None, 3)
      ))
    ).sortBy(_._2) === List(
      (Some("a"), 1),
      (Some("b"), 2),
      (None, 3)
    )

    // Current state unchanged
    Ns.str.int.get === List(("a", 1))
  }


  "getUpdateTx" in new Setup {

    Ns.int.getWith(Ns(eid).int(2).getUpdateTx) === List(2)

    Ns.int.getWith(Ns(eid).str("b").int(2).getUpdateTx) === List(2)

    Ns.str.getWith(Ns(eid).str("b").int(2).getUpdateTx) === List("b")

    Ns.str.int.getWith(Ns(eid).int(2).getUpdateTx) === List(("a", 2))

    Ns.str.int.getWith(Ns(eid).str("b").int(2).getUpdateTx) === List(("b", 2))

    // Current state unchanged
    Ns.str.int.get === List(("a", 1))
  }


  "getRetractTx" in new Setup {

    val eid2: Long = Ns.str("b").int(2).save.eid

    // Current state
    Ns.str.int.get.sortBy(_._2) === List(
      ("a", 1),
      ("b", 2)
    )

    // Test retracting an entity id
    Ns.str.int.getWith(eid2.getRetractTx) === List(
      ("a", 1)
    )

    // Live state is unchanged
    Ns.str.int.get.sortBy(_._2) === List(
      ("a", 1),
      ("b", 2)
    )
  }


  "Combination example" in new Setup {

    // Clean initial state
    Ns.e.str_.get.map(_.retract)
    val fred = Ns.str("Fred").int(42).save.eid

    // Current state
    Ns.str.int.get === List(("Fred", 42))

    Ns.str.int.getWith(
      Ns(fred).int(43).getUpdateTx
    ) === List(("Fred", 43))

    // Production value intact
    Ns.str.int.get === List(("Fred", 42))

    Ns.str.int.getWith(
      Ns.str("John").int(44).getSaveTx
    ).sorted === List(
      ("Fred", 42), // production value
      ("John", 44) // insertion worked
    )

    // Production value intact
    Ns.str.int.get === List(("Fred", 42))


    Ns.str.int.getWith(
      Ns.str("John").int(44).getSaveTx,
      Ns.str.int getInsertTx List(
        ("Lisa", 23),
        ("Pete", 24)
      ),
      Ns(fred).int(43).getUpdateTx
    ).sorted === List(
      ("Fred", 43), // Updated
      ("John", 44), // Saved
      ("Lisa", 23), // Inserted
      ("Pete", 24) // Inserted
    )

    val saveJohn      = Ns.str("John").int(44).getSaveTx
    val insertMembers = Ns.str.int getInsertTx List(("Lisa", 23), ("Pete", 24))
    val updateFred    = Ns(fred).int(43).getUpdateTx

    Ns.str.int.getWith(
      saveJohn,
      insertMembers,
      updateFred
    ).sorted === List(
      ("Fred", 43), // Updated
      ("John", 44), // Saved
      ("Lisa", 23), // Inserted
      ("Pete", 24) // Inserted
    )
  }
}