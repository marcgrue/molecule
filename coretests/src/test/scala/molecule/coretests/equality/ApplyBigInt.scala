package molecule.coretests.equality

import molecule.datomic.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class ApplyBigInt extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.bigInt$ insert List(
        (1, Some(bigInt1)),
        (2, Some(bigInt2)),
        (3, Some(bigInt3)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.bigInt.apply(bigInt1).get === List(bigInt1)
      Ns.bigInt.apply(bigInt2).get === List(bigInt2)
      Ns.bigInt.apply(bigInt1, bigInt2).get === List(bigInt1, bigInt2)

      // `or`
      Ns.bigInt.apply(bigInt1 or bigInt2).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(bigInt1 or bigInt2 or bigInt3).get === List(bigInt1, bigInt2, bigInt3)

      // Seq
      Ns.bigInt.apply().get === Nil
      Ns.bigInt.apply(Nil).get === Nil
      Ns.bigInt.apply(List(bigInt1)).get === List(bigInt1)
      Ns.bigInt.apply(List(bigInt2)).get === List(bigInt2)
      Ns.bigInt.apply(List(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(List(bigInt1), List(bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.bigInt.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.bigInt.apply(List(bigInt1, bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.bigInt_.apply(bigInt1).get === List(1)
      Ns.int.bigInt_.apply(bigInt2).get === List(2)
      Ns.int.bigInt_.apply(bigInt1, bigInt2).get === List(1, 2)

      // `or`
      Ns.int.bigInt_.apply(bigInt1 or bigInt2).get === List(1, 2)
      Ns.int.bigInt_.apply(bigInt1 or bigInt2 or bigInt3).get === List(1, 2, 3)

      // Seq
      Ns.int.bigInt_.apply().get === List(4)
      Ns.int.bigInt_.apply(Nil).get === List(4)
      Ns.int.bigInt_.apply(List(bigInt1)).get === List(1)
      Ns.int.bigInt_.apply(List(bigInt2)).get === List(2)
      Ns.int.bigInt_.apply(List(bigInt1, bigInt2)).get === List(1, 2)
      Ns.int.bigInt_.apply(List(bigInt1), List(bigInt2)).get === List(1, 2)
      Ns.int.bigInt_.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInt_.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInt_.apply(List(bigInt1, bigInt2, bigInt3)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.bigInts$ insert List(
        (1, Some(Set(bigInt1, bigInt2))),
        (2, Some(Set(bigInt2, bigInt3))),
        (3, Some(Set(bigInt3, bigInt4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bigInts.apply(bigInt1).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(bigInt1, bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))

      // `or`
      Ns.int.bigInts.apply(bigInt1 or bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(bigInt1 or bigInt2 or bigInt3).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      // Seq
      Ns.int.bigInts.apply().get === Nil
      Ns.int.bigInts.apply(Nil).get === Nil
      Ns.int.bigInts.apply(List(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(List(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1, bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1), List(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(List(bigInt1, bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))


      // AND semantics

      // Set
      Ns.int.bigInts.apply(Set[BigInt]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.bigInts.apply(Set(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.int.bigInts.apply(Set(bigInt2, bigInt3)).get === List((2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set[BigInt]()).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt4)).get === List((1, Set(bigInt1, bigInt2)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2), Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt4)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4)).get === List((1, Set(bigInt1, bigInt2)), (3, Set(bigInt3, bigInt4)))

      // `and`
      Ns.int.bigInts.apply(bigInt1 and bigInt2).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(bigInt1 and bigInt3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.bigInts.apply(bigInt1).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(bigInt1, bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))

      // `or`
      Ns.bigInts.apply(bigInt1 or bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(bigInt1 or bigInt2 or bigInt3).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))

      // Seq
      Ns.bigInts.apply().get === Nil
      Ns.bigInts.apply(Nil).get === Nil
      Ns.bigInts.apply(List(bigInt1)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(List(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1), List(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt2, bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))


      // AND semantics

      // Set
      Ns.bigInts.apply(Set[BigInt]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.bigInts.apply(Set(bigInt1)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(Set(bigInt1, bigInt2)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.bigInts.apply(Set(bigInt2, bigInt3)).get === List(Set(bigInt2, bigInt3))
      Ns.bigInts.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2)).get === List(Set(bigInt1, bigInt2, bigInt3))
      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4))
      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt4)).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4))
      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2), Set(bigInt3)).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4))

      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(Set(bigInt1, bigInt2, bigInt3))
      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt4)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4)).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bigInts.apply(bigInt1 and bigInt2).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(bigInt1 and bigInt3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bigInts_.apply(bigInt1).get === List(1)
      Ns.int.bigInts_.apply(bigInt2).get === List(1, 2)
      Ns.int.bigInts_.apply(bigInt1, bigInt2).get === List(1, 2)

      // `or`
      Ns.int.bigInts_.apply(bigInt1 or bigInt2).get === List(1, 2)
      Ns.int.bigInts_.apply(bigInt1 or bigInt2 or bigInt3).get === List(1, 2, 3)

      // Seq
      Ns.int.bigInts_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.bigInts_.apply(Nil).get === List(4)
      Ns.int.bigInts_.apply(List(bigInt1)).get === List(1)
      Ns.int.bigInts_.apply(List(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1), List(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInts_.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2, bigInt3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.bigInts_.apply(Set[BigInt]()).get === List(4)
      Ns.int.bigInts_.apply(Set(bigInt1)).get === List(1)
      Ns.int.bigInts_.apply(Set(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2)).get === List(1)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.int.bigInts_.apply(Set(bigInt2, bigInt3)).get === List(2)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt4)).get === List(1, 3)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt2), Set(bigInt3)).get === List(1, 2, 3)

      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(1, 2)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt4)).get === List(1)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigInts_.apply(bigInt1 and bigInt2).get === List(1)
      Ns.int.bigInts_.apply(bigInt1 and bigInt3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[BigInt]()

      val l1 = List(bigInt1)
      val l2 = List(bigInt2)

      val s1 = Set(bigInt1)
      val s2 = Set(bigInt2)

      val l12 = List(bigInt1, bigInt2)
      val l23 = List(bigInt2, bigInt3)

      val s12 = Set(bigInt1, bigInt2)
      val s23 = Set(bigInt2, bigInt3)


      // OR semantics

      // Vararg
      Ns.int.bigInts_.apply(bigInt1, bigInt2).get === List(1, 2)

      // `or`
      Ns.int.bigInts_.apply(bigInt1 or bigInt2).get === List(1, 2)

      // Seq
      Ns.int.bigInts_.apply(seq0).get === List(4)
      Ns.int.bigInts_.apply(List(bigInt1), List(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(l1, l2).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.bigInts_.apply(set0).get === List(4)

      Ns.int.bigInts_.apply(Set(bigInt1)).get === List(1)
      Ns.int.bigInts_.apply(s1).get === List(1)

      Ns.int.bigInts_.apply(Set(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(s2).get === List(1, 2)

      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2)).get === List(1)
      Ns.int.bigInts_.apply(s12).get === List(1)

      Ns.int.bigInts_.apply(Set(bigInt2, bigInt3)).get === List(2)
      Ns.int.bigInts_.apply(s23).get === List(2)

      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(1, 2)
      Ns.int.bigInts_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.bigInts_.apply(bigInt1 and bigInt2).get === List(1)
    }
  }
}