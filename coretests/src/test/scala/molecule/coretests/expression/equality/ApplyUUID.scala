package molecule.coretests.expression.equality

import java.util.UUID
import molecule.api._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class ApplyUUID extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.uuid$ insert List(
        (1, Some(uuid1)),
        (2, Some(uuid2)),
        (3, Some(uuid3)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.uuid.apply(uuid1).get === List(uuid1)
      Ns.uuid.apply(uuid2).get === List(uuid2)
      Ns.uuid.apply(uuid1, uuid2).get === List(uuid1, uuid2)

      // `or`
      Ns.uuid.apply(uuid1 or uuid2).get === List(uuid1, uuid2)
      Ns.uuid.apply(uuid1 or uuid2 or uuid3).get === List(uuid1, uuid2, uuid3)

      // Seq
      Ns.uuid.apply().get === Nil
      Ns.uuid.apply(Nil).get === Nil
      Ns.uuid.apply(List(uuid1)).get === List(uuid1)
      Ns.uuid.apply(List(uuid2)).get === List(uuid2)
      Ns.uuid.apply(List(uuid1, uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(List(uuid1), List(uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(List(uuid1, uuid2), List(uuid3)).get === List(uuid1, uuid2, uuid3)
      Ns.uuid.apply(List(uuid1), List(uuid2, uuid3)).get === List(uuid1, uuid2, uuid3)
      Ns.uuid.apply(List(uuid1, uuid2, uuid3)).get === List(uuid1, uuid2, uuid3)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.uuid_.apply(uuid1).get === List(1)
      Ns.int.uuid_.apply(uuid2).get === List(2)
      Ns.int.uuid_.apply(uuid1, uuid2).get === List(1, 2)

      // `or`
      Ns.int.uuid_.apply(uuid1 or uuid2).get === List(1, 2)
      Ns.int.uuid_.apply(uuid1 or uuid2 or uuid3).get === List(1, 2, 3)

      // Seq
      Ns.int.uuid_.apply().get === List(4)
      Ns.int.uuid_.apply(Nil).get === List(4)
      Ns.int.uuid_.apply(List(uuid1)).get === List(1)
      Ns.int.uuid_.apply(List(uuid2)).get === List(2)
      Ns.int.uuid_.apply(List(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(List(uuid1), List(uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(List(uuid1, uuid2), List(uuid3)).get === List(1, 2, 3)
      Ns.int.uuid_.apply(List(uuid1), List(uuid2, uuid3)).get === List(1, 2, 3)
      Ns.int.uuid_.apply(List(uuid1, uuid2, uuid3)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.uuids$ insert List(
        (1, Some(Set(uuid1, uuid2))),
        (2, Some(Set(uuid2, uuid3))),
        (3, Some(Set(uuid3, uuid4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.uuids.apply(uuid1).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(uuid1, uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))

      // `or`
      Ns.int.uuids.apply(uuid1 or uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(uuid1 or uuid2 or uuid3).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      // Seq
      Ns.int.uuids.apply().get === Nil
      Ns.int.uuids.apply(Nil).get === Nil
      Ns.int.uuids.apply(List(uuid1)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(List(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1, uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1), List(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1, uuid2), List(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(List(uuid1), List(uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(List(uuid1, uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))


      // AND semantics

      // Set
      Ns.int.uuids.apply(Set[UUID]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.uuids.apply(Set(uuid1)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(Set(uuid1, uuid2)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid1, uuid3)).get === Nil
      Ns.int.uuids.apply(Set(uuid2, uuid3)).get === List((2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      Ns.int.uuids.apply(Set(uuid1, uuid2), Set[UUID]()).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid4)).get === List((1, Set(uuid1, uuid2)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid2), Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid2, uuid4)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid3, uuid4)).get === List((1, Set(uuid1, uuid2)), (3, Set(uuid3, uuid4)))

      // `and`
      Ns.int.uuids.apply(uuid1 and uuid2).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(uuid1 and uuid3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.uuids.apply(uuid1).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(uuid2).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(uuid1, uuid2).get === List(Set(uuid1, uuid3, uuid2))

      // `or`
      Ns.uuids.apply(uuid1 or uuid2).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(uuid1 or uuid2 or uuid3).get === List(Set(uuid1, uuid4, uuid3, uuid2))

      // Seq
      Ns.uuids.apply().get === Nil
      Ns.uuids.apply(Nil).get === Nil
      Ns.uuids.apply(List(uuid1)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(List(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1, uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1), List(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1, uuid2), List(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1), List(uuid2, uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1, uuid2, uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))


      // AND semantics

      // Set
      Ns.uuids.apply(Set[UUID]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.uuids.apply(Set(uuid1)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(Set(uuid1, uuid2)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(Set(uuid1, uuid3)).get === Nil
      Ns.uuids.apply(Set(uuid2, uuid3)).get === List(Set(uuid2, uuid3))
      Ns.uuids.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid2)).get === List(Set(uuid1, uuid2, uuid3))
      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid3)).get === List(Set(uuid1, uuid2, uuid3, uuid4))
      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid4)).get === List(Set(uuid1, uuid2, uuid3, uuid4))
      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid2), Set(uuid3)).get === List(Set(uuid1, uuid2, uuid3, uuid4))

      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(Set(uuid1, uuid2, uuid3))
      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid2, uuid4)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid3, uuid4)).get === List(Set(uuid1, uuid2, uuid3, uuid4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.uuids.apply(uuid1 and uuid2).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(uuid1 and uuid3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.uuids_.apply(uuid1).get === List(1)
      Ns.int.uuids_.apply(uuid2).get === List(1, 2)
      Ns.int.uuids_.apply(uuid1, uuid2).get === List(1, 2)

      // `or`
      Ns.int.uuids_.apply(uuid1 or uuid2).get === List(1, 2)
      Ns.int.uuids_.apply(uuid1 or uuid2 or uuid3).get === List(1, 2, 3)

      // Seq
      Ns.int.uuids_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.uuids_.apply(Nil).get === List(4)
      Ns.int.uuids_.apply(List(uuid1)).get === List(1)
      Ns.int.uuids_.apply(List(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1), List(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1, uuid2), List(uuid3)).get === List(1, 2, 3)
      Ns.int.uuids_.apply(List(uuid1), List(uuid2, uuid3)).get === List(1, 2, 3)
      Ns.int.uuids_.apply(List(uuid1, uuid2, uuid3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.uuids_.apply(Set[UUID]()).get === List(4)
      Ns.int.uuids_.apply(Set(uuid1)).get === List(1)
      Ns.int.uuids_.apply(Set(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(Set(uuid1, uuid2)).get === List(1)
      Ns.int.uuids_.apply(Set(uuid1, uuid3)).get === Nil
      Ns.int.uuids_.apply(Set(uuid2, uuid3)).get === List(2)
      Ns.int.uuids_.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid3)).get === List(1, 2, 3)
      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid4)).get === List(1, 3)
      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid2), Set(uuid3)).get === List(1, 2, 3)

      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(1, 2)
      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid2, uuid4)).get === List(1)
      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid3, uuid4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uuids_.apply(uuid1 and uuid2).get === List(1)
      Ns.int.uuids_.apply(uuid1 and uuid3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[UUID]()

      val l1 = List(uuid1)
      val l2 = List(uuid2)

      val s1 = Set(uuid1)
      val s2 = Set(uuid2)

      val l12 = List(uuid1, uuid2)
      val l23 = List(uuid2, uuid3)

      val s12 = Set(uuid1, uuid2)
      val s23 = Set(uuid2, uuid3)


      // OR semantics

      // Vararg
      Ns.int.uuids_.apply(uuid1, uuid2).get === List(1, 2)

      // `or`
      Ns.int.uuids_.apply(uuid1 or uuid2).get === List(1, 2)

      // Seq
      Ns.int.uuids_.apply(seq0).get === List(4)
      Ns.int.uuids_.apply(List(uuid1), List(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(l1, l2).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.uuids_.apply(set0).get === List(4)

      Ns.int.uuids_.apply(Set(uuid1)).get === List(1)
      Ns.int.uuids_.apply(s1).get === List(1)

      Ns.int.uuids_.apply(Set(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(s2).get === List(1, 2)

      Ns.int.uuids_.apply(Set(uuid1, uuid2)).get === List(1)
      Ns.int.uuids_.apply(s12).get === List(1)

      Ns.int.uuids_.apply(Set(uuid2, uuid3)).get === List(2)
      Ns.int.uuids_.apply(s23).get === List(2)

      Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(1, 2)
      Ns.int.uuids_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.uuids_.apply(uuid1 and uuid2).get === List(1)
    }
  }
}