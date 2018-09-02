package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class ApplyDouble extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.double$ insert List(
        (1, Some(1.0)),
        (2, Some(2.0)),
        (3, Some(3.0)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.double.apply(1.0).get === List(1.0)
      Ns.double.apply(2.0).get === List(2.0)
      Ns.double.apply(1.0, 2.0).get === List(1.0, 2.0)

      // `or`
      Ns.double.apply(1.0 or 2.0).get === List(1.0, 2.0)
      Ns.double.apply(1.0 or 2.0 or 3.0).get === List(3.0, 1.0, 2.0)

      // Seq
      Ns.double.apply().get === Nil
      Ns.double.apply(Nil).get === Nil
      Ns.double.apply(List(1.0)).get === List(1.0)
      Ns.double.apply(List(2.0)).get === List(2.0)
      Ns.double.apply(List(1.0, 2.0)).get === List(1.0, 2.0)
      Ns.double.apply(List(1.0), List(2.0)).get === List(1.0, 2.0)
      Ns.double.apply(List(1.0, 2.0), List(3.0)).get === List(3.0, 1.0, 2.0)
      Ns.double.apply(List(1.0), List(2.0, 3.0)).get === List(3.0, 1.0, 2.0)
      Ns.double.apply(List(1.0, 2.0, 3.0)).get === List(3.0, 1.0, 2.0)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.double_.apply(1.0).get === List(1)
      Ns.int.double_.apply(2.0).get === List(2)
      Ns.int.double_.apply(1.0, 2.0).get === List(1, 2)

      // `or`
      Ns.int.double_.apply(1.0 or 2.0).get === List(1, 2)
      Ns.int.double_.apply(1.0 or 2.0 or 3.0).get === List(1, 2, 3)

      // Seq
      Ns.int.double_.apply().get === List(4)
      Ns.int.double_.apply(Nil).get === List(4)
      Ns.int.double_.apply(List(1.0)).get === List(1)
      Ns.int.double_.apply(List(2.0)).get === List(2)
      Ns.int.double_.apply(List(1.0, 2.0)).get === List(1, 2)
      Ns.int.double_.apply(List(1.0), List(2.0)).get === List(1, 2)
      Ns.int.double_.apply(List(1.0, 2.0), List(3.0)).get === List(1, 2, 3)
      Ns.int.double_.apply(List(1.0), List(2.0, 3.0)).get === List(1, 2, 3)
      Ns.int.double_.apply(List(1.0, 2.0, 3.0)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.doubles$ insert List(
        (1, Some(Set(1.0, 2.0))),
        (2, Some(Set(2.0, 3.0))),
        (3, Some(Set(3.0, 4.0))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.doubles.apply(1.0).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(1.0, 2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))

      // `or`
      Ns.int.doubles.apply(1.0 or 2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(1.0 or 2.0 or 3.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      // Seq
      Ns.int.doubles.apply().get === Nil
      Ns.int.doubles.apply(Nil).get === Nil
      Ns.int.doubles.apply(List(1.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(List(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0, 2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0), List(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0, 2.0), List(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(List(1.0), List(2.0, 3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(List(1.0, 2.0, 3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))


      // AND semantics

      // Set
      Ns.int.doubles.apply(Set[Double]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.doubles.apply(Set(1.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(1.0, 3.0)).get === Nil
      Ns.int.doubles.apply(Set(2.0, 3.0)).get === List((2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0, 3.0)).get === Nil

      Ns.int.doubles.apply(Set(1.0, 2.0), Set[Double]()).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(4.0)).get === List((1, Set(1.0, 2.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(2.0), Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      Ns.int.doubles.apply(Set(1.0, 2.0), Set(2.0, 3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(2.0, 4.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0), Set(3.0, 4.0)).get === List((1, Set(1.0, 2.0)), (3, Set(3.0, 4.0)))

      // `and`
      Ns.int.doubles.apply(1.0 and 2.0).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(1.0 and 3.0).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.doubles.apply(1.0).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(2.0).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(1.0, 2.0).get === List(Set(1.0, 3.0, 2.0))

      // `or`
      Ns.doubles.apply(1.0 or 2.0).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(1.0 or 2.0 or 3.0).get === List(Set(1.0, 4.0, 3.0, 2.0))

      // Seq
      Ns.doubles.apply().get === Nil
      Ns.doubles.apply(Nil).get === Nil
      Ns.doubles.apply(List(1.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(List(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0, 2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0), List(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0, 2.0), List(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0), List(2.0, 3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0, 2.0, 3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))


      // AND semantics

      // Set
      Ns.doubles.apply(Set[Double]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.doubles.apply(Set(1.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(Set(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(Set(1.0, 2.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(Set(1.0, 3.0)).get === Nil
      Ns.doubles.apply(Set(2.0, 3.0)).get === List(Set(2.0, 3.0))
      Ns.doubles.apply(Set(1.0, 2.0, 3.0)).get === Nil

      Ns.doubles.apply(Set(1.0, 2.0), Set(2.0)).get === List(Set(1.0, 2.0, 3.0))
      Ns.doubles.apply(Set(1.0, 2.0), Set(3.0)).get === List(Set(1.0, 2.0, 3.0, 4.0))
      Ns.doubles.apply(Set(1.0, 2.0), Set(4.0)).get === List(Set(1.0, 2.0, 3.0, 4.0))
      Ns.doubles.apply(Set(1.0, 2.0), Set(2.0), Set(3.0)).get === List(Set(1.0, 2.0, 3.0, 4.0))

      Ns.doubles.apply(Set(1.0, 2.0), Set(2.0, 3.0)).get === List(Set(1.0, 2.0, 3.0))
      Ns.doubles.apply(Set(1.0, 2.0), Set(2.0, 4.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(Set(1.0, 2.0), Set(3.0, 4.0)).get === List(Set(1.0, 2.0, 3.0, 4.0))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.doubles.apply(1.0 and 2.0).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(1.0 and 3.0).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.doubles_.apply(1.0).get === List(1)
      Ns.int.doubles_.apply(2.0).get === List(1, 2)
      Ns.int.doubles_.apply(1.0, 2.0).get === List(1, 2)

      // `or`
      Ns.int.doubles_.apply(1.0 or 2.0).get === List(1, 2)
      Ns.int.doubles_.apply(1.0 or 2.0 or 3.0).get === List(1, 2, 3)

      // Seq
      Ns.int.doubles_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.doubles_.apply(Nil).get === List(4)
      Ns.int.doubles_.apply(List(1.0)).get === List(1)
      Ns.int.doubles_.apply(List(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0, 2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0), List(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0, 2.0), List(3.0)).get === List(1, 2, 3)
      Ns.int.doubles_.apply(List(1.0), List(2.0, 3.0)).get === List(1, 2, 3)
      Ns.int.doubles_.apply(List(1.0, 2.0, 3.0)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.doubles_.apply(Set[Double]()).get === List(4)
      Ns.int.doubles_.apply(Set(1.0)).get === List(1)
      Ns.int.doubles_.apply(Set(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(Set(1.0, 2.0)).get === List(1)
      Ns.int.doubles_.apply(Set(1.0, 3.0)).get === Nil
      Ns.int.doubles_.apply(Set(2.0, 3.0)).get === List(2)
      Ns.int.doubles_.apply(Set(1.0, 2.0, 3.0)).get === Nil

      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(3.0)).get === List(1, 2, 3)
      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(4.0)).get === List(1, 3)
      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(2.0), Set(3.0)).get === List(1, 2, 3)

      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(2.0, 3.0)).get === List(1, 2)
      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(2.0, 4.0)).get === List(1)
      Ns.int.doubles_.apply(Set(1.0, 2.0), Set(3.0, 4.0)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.doubles_.apply(1.0 and 2.0).get === List(1)
      Ns.int.doubles_.apply(1.0 and 3.0).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Double]()

      val l1 = List(double1)
      val l2 = List(double2)

      val s1 = Set(double1)
      val s2 = Set(double2)

      val l12 = List(double1, double2)
      val l23 = List(double2, double3)

      val s12 = Set(double1, double2)
      val s23 = Set(double2, double3)


      // OR semantics

      // Vararg
      Ns.int.doubles_.apply(double1, double2).get === List(1, 2)

      // `or`
      Ns.int.doubles_.apply(double1 or double2).get === List(1, 2)

      // Seq
      Ns.int.doubles_.apply(seq0).get === List(4)
      Ns.int.doubles_.apply(List(double1), List(double2)).get === List(1, 2)
      Ns.int.doubles_.apply(l1, l2).get === List(1, 2)
      Ns.int.doubles_.apply(List(double1, double2)).get === List(1, 2)
      Ns.int.doubles_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.doubles_.apply(set0).get === List(4)

      Ns.int.doubles_.apply(Set(double1)).get === List(1)
      Ns.int.doubles_.apply(s1).get === List(1)

      Ns.int.doubles_.apply(Set(double2)).get === List(1, 2)
      Ns.int.doubles_.apply(s2).get === List(1, 2)

      Ns.int.doubles_.apply(Set(double1, double2)).get === List(1)
      Ns.int.doubles_.apply(s12).get === List(1)

      Ns.int.doubles_.apply(Set(double2, double3)).get === List(2)
      Ns.int.doubles_.apply(s23).get === List(2)

      Ns.int.doubles_.apply(Set(double1, double2), Set(double2, double3)).get === List(1, 2)
      Ns.int.doubles_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.doubles_.apply(double1 and double2).get === List(1)
    }
  }
}