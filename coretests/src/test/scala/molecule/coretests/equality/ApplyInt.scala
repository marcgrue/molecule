package molecule.coretests.equality

import molecule.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class ApplyInt extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.int$ insert List(
        ("a", Some(1)),
        ("b", Some(2)),
        ("c", Some(3)),
        ("d", None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.int.apply(1).get === List(1)
      Ns.int.apply(2).get === List(2)
      Ns.int.apply(1, 2).get === List(1, 2)

      // `or`
      Ns.int.apply(1 or 2).get === List(1, 2)
      Ns.int.apply(1 or 2 or 3).get === List(1, 2, 3)

      // Seq
      Ns.int.apply().get === Nil
      Ns.int.apply(Nil).get === Nil
      Ns.int.apply(List(1)).get === List(1)
      Ns.int.apply(List(2)).get === List(2)
      Ns.int.apply(List(1, 2)).get === List(1, 2)
      Ns.int.apply(List(1), List(2)).get === List(1, 2)
      Ns.int.apply(List(1, 2), List(3)).get === List(1, 2, 3)
      Ns.int.apply(List(1), List(2, 3)).get === List(1, 2, 3)
      Ns.int.apply(List(1, 2, 3)).get === List(1, 2, 3)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.str.int_.apply(1).get === List("a")
      Ns.str.int_.apply(2).get === List("b")
      Ns.str.int_.apply(1, 2).get === List("a", "b")

      // `or`
      Ns.str.int_.apply(1 or 2).get === List("a", "b")
      Ns.str.int_.apply(1 or 2 or 3).get === List("a", "b", "c")

      // Seq
      Ns.str.int_.apply().get === List("d")
      Ns.str.int_.apply(Nil).get === List("d")
      Ns.str.int_.apply(List(1)).get === List("a")
      Ns.str.int_.apply(List(2)).get === List("b")
      Ns.str.int_.apply(List(1, 2)).get === List("a", "b")
      Ns.str.int_.apply(List(1), List(2)).get === List("a", "b")
      Ns.str.int_.apply(List(1, 2), List(3)).get === List("a", "b", "c")
      Ns.str.int_.apply(List(1), List(2, 3)).get === List("a", "b", "c")
      Ns.str.int_.apply(List(1, 2, 3)).get === List("a", "b", "c")
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.ints$ insert List(
        (1, Some(Set(1, 2))),
        (2, Some(Set(2, 3))),
        (3, Some(Set(3, 4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.ints.apply(1).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(2).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(1, 2).get === List((1, Set(1, 2)), (2, Set(2, 3)))

      // `or`
      Ns.int.ints.apply(1 or 2).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(1 or 2 or 3).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))

      // Seq
      Ns.int.ints.apply().get === Nil
      Ns.int.ints.apply(Nil).get === Nil
      Ns.int.ints.apply(List(1)).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(List(2)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(List(1, 2)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(List(1), List(2)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(List(1, 2), List(3)).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))
      Ns.int.ints.apply(List(1), List(2, 3)).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))
      Ns.int.ints.apply(List(1, 2, 3)).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))


      // AND semantics

      // Set
      Ns.int.ints.apply(Set[Int]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.ints.apply(Set(1)).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(Set(2)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(Set(1, 2)).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(Set(1, 3)).get === Nil
      Ns.int.ints.apply(Set(2, 3)).get === List((2, Set(2, 3)))
      Ns.int.ints.apply(Set(1, 2, 3)).get === Nil

      Ns.int.ints.apply(Set(1, 2), Set[Int]()).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(Set(1, 2), Set(2)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(Set(1, 2), Set(3)).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))
      Ns.int.ints.apply(Set(1, 2), Set(4)).get === List((1, Set(1, 2)), (3, Set(3, 4)))
      Ns.int.ints.apply(Set(1, 2), Set(2), Set(3)).get === List((1, Set(1, 2)), (2, Set(2, 3)), (3, Set(3, 4)))

      Ns.int.ints.apply(Set(1, 2), Set(2, 3)).get === List((1, Set(1, 2)), (2, Set(2, 3)))
      Ns.int.ints.apply(Set(1, 2), Set(2, 4)).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(Set(1, 2), Set(3, 4)).get === List((1, Set(1, 2)), (3, Set(3, 4)))

      // `and`
      Ns.int.ints.apply(1 and 2).get === List((1, Set(1, 2)))
      Ns.int.ints.apply(1 and 3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.ints.apply(1).get === List(Set(1, 2))
      Ns.ints.apply(2).get === List(Set(1, 3, 2))
      Ns.ints.apply(1, 2).get === List(Set(1, 3, 2))

      // `or`
      Ns.ints.apply(1 or 2).get === List(Set(1, 3, 2))
      Ns.ints.apply(1 or 2 or 3).get === List(Set(1, 4, 3, 2))

      // Seq
      Ns.ints.apply().get === Nil
      Ns.ints.apply(Nil).get === Nil
      Ns.ints.apply(List(1)).get === List(Set(1, 2))
      Ns.ints.apply(List(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1, 2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1), List(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1, 2), List(3)).get === List(Set(1, 4, 3, 2))
      Ns.ints.apply(List(1), List(2, 3)).get === List(Set(1, 4, 3, 2))
      Ns.ints.apply(List(1, 2, 3)).get === List(Set(1, 4, 3, 2))


      // AND semantics

      // Set
      Ns.ints.apply(Set[Int]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.ints.apply(Set(1)).get === List(Set(1, 2))
      Ns.ints.apply(Set(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(Set(1, 2)).get === List(Set(1, 2))
      Ns.ints.apply(Set(1, 3)).get === Nil
      Ns.ints.apply(Set(2, 3)).get === List(Set(2, 3))
      Ns.ints.apply(Set(1, 2, 3)).get === Nil

      Ns.ints.apply(Set(1, 2), Set(2)).get === List(Set(1, 2, 3))
      Ns.ints.apply(Set(1, 2), Set(3)).get === List(Set(1, 2, 3, 4))
      Ns.ints.apply(Set(1, 2), Set(4)).get === List(Set(1, 2, 3, 4))
      Ns.ints.apply(Set(1, 2), Set(2), Set(3)).get === List(Set(1, 2, 3, 4))

      Ns.ints.apply(Set(1, 2), Set(2, 3)).get === List(Set(1, 2, 3))
      Ns.ints.apply(Set(1, 2), Set(2, 4)).get === List(Set(1, 2))
      Ns.ints.apply(Set(1, 2), Set(3, 4)).get === List(Set(1, 2, 3, 4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.ints.apply(1 and 2).get === List(Set(1, 2))
      Ns.ints.apply(1 and 3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.ints_.apply(1).get === List(1)
      Ns.int.ints_.apply(2).get === List(1, 2)
      Ns.int.ints_.apply(1, 2).get === List(1, 2)

      // `or`
      Ns.int.ints_.apply(1 or 2).get === List(1, 2)
      Ns.int.ints_.apply(1 or 2 or 3).get === List(1, 2, 3)

      // Seq
      Ns.int.ints_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.ints_.apply(Nil).get === List(4)
      Ns.int.ints_.apply(List(1)).get === List(1)
      Ns.int.ints_.apply(List(2)).get === List(1, 2)
      Ns.int.ints_.apply(List(1, 2)).get === List(1, 2)
      Ns.int.ints_.apply(List(1), List(2)).get === List(1, 2)
      Ns.int.ints_.apply(List(1, 2), List(3)).get === List(1, 2, 3)
      Ns.int.ints_.apply(List(1), List(2, 3)).get === List(1, 2, 3)
      Ns.int.ints_.apply(List(1, 2, 3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.ints_.apply(Set[Int]()).get === List(4)
      Ns.int.ints_.apply(Set(1)).get === List(1)
      Ns.int.ints_.apply(Set(2)).get === List(1, 2)
      Ns.int.ints_.apply(Set(1, 2)).get === List(1)
      Ns.int.ints_.apply(Set(1, 3)).get === Nil
      Ns.int.ints_.apply(Set(2, 3)).get === List(2)
      Ns.int.ints_.apply(Set(1, 2, 3)).get === Nil

      Ns.int.ints_.apply(Set(1, 2), Set(2)).get === List(1, 2)
      Ns.int.ints_.apply(Set(1, 2), Set(3)).get === List(1, 2, 3)
      Ns.int.ints_.apply(Set(1, 2), Set(4)).get === List(1, 3)
      Ns.int.ints_.apply(Set(1, 2), Set(2), Set(3)).get === List(1, 2, 3)

      Ns.int.ints_.apply(Set(1, 2), Set(2, 3)).get === List(1, 2)
      Ns.int.ints_.apply(Set(1, 2), Set(2, 4)).get === List(1)
      Ns.int.ints_.apply(Set(1, 2), Set(3, 4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.ints_.apply(1 and 2).get === List(1)
      Ns.int.ints_.apply(1 and 3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Int]()

      val l1 = List(1)
      val l2 = List(2)

      val s1 = Set(1)
      val s2 = Set(2)

      val l12 = List(1, 2)
      val l23 = List(2, 3)

      val s12 = Set(1, 2)
      val s23 = Set(2, 3)


      // OR semantics

      // Vararg
      Ns.int.ints_.apply(int1, int2).get === List(1, 2)

      // `or`
      Ns.int.ints_.apply(int1 or int2).get === List(1, 2)

      // Seq
      Ns.int.ints_.apply(seq0).get === List(4)
      Ns.int.ints_.apply(List(int1), List(int2)).get === List(1, 2)
      Ns.int.ints_.apply(l1, l2).get === List(1, 2)
      Ns.int.ints_.apply(List(int1, int2)).get === List(1, 2)
      Ns.int.ints_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.ints_.apply(set0).get === List(4)

      Ns.int.ints_.apply(Set(int1)).get === List(1)
      Ns.int.ints_.apply(s1).get === List(1)

      Ns.int.ints_.apply(Set(int2)).get === List(1, 2)
      Ns.int.ints_.apply(s2).get === List(1, 2)

      Ns.int.ints_.apply(Set(int1, int2)).get === List(1)
      Ns.int.ints_.apply(s12).get === List(1)

      Ns.int.ints_.apply(Set(int2, int3)).get === List(2)
      Ns.int.ints_.apply(s23).get === List(2)

      Ns.int.ints_.apply(Set(int1, int2), Set(int2, int3)).get === List(1, 2)
      Ns.int.ints_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.ints_.apply(int1 and int2).get === List(1)
    }
  }
}