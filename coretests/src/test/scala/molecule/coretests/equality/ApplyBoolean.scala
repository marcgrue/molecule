package molecule.coretests.equality

import molecule.api._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class ApplyBoolean extends CoreSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.bool$ insert List(
        (1, Some(true)),
        (2, Some(false)),
        (3, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.bool.apply(true).get === List(true)
      Ns.bool.apply(false).get === List(false)
      Ns.bool.apply(true, false).get === List(false, true)

      // `or`
      Ns.bool.apply(true or false).get === List(false, true)

      // Seq
      Ns.bool.apply().get === Nil
      Ns.bool.apply(Nil).get === Nil
      Ns.bool.apply(List(true)).get === List(true)
      Ns.bool.apply(List(false)).get === List(false)
      Ns.bool.apply(List(true, false)).get === List(false, true)
      Ns.bool.apply(List(true), List(false)).get === List(false, true)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.bool_.apply(true).get === List(1)
      Ns.int.bool_.apply(false).get === List(2)
      Ns.int.bool_.apply(true, false).get === List(1, 2)

      // `or`
      Ns.int.bool_.apply(true or false).get === List(1, 2)

      // Seq
      Ns.int.bool_.apply(List(true)).get === List(1)
      Ns.int.bool_.apply(List(false)).get === List(2)
      Ns.int.bool_.apply(List(true, false)).get === List(1, 2)
      Ns.int.bool_.apply(List(true), List(false)).get === List(1, 2)
      Ns.int.bool_.apply(List(true, false), List(true)).get === List(1, 2)
      Ns.int.bool_.apply(List(true), List(false, true)).get === List(1, 2)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.bools$ insert List(
        (1, Some(Set(true))),
        (2, Some(Set(false))),
        (3, Some(Set(false, true))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bools.apply(true).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(false).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(true, false).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      // `or`
      Ns.int.bools.apply(true or false).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      // Seq
      Ns.int.bools.apply().get === Nil
      Ns.int.bools.apply(Nil).get === Nil
      Ns.int.bools.apply(List(true)).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(List(false)).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(List(true, false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(List(true), List(false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))


      // AND semantics

      // Set
      Ns.int.bools.apply(Set[Boolean]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.bools.apply(Set(true)).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(Set(false)).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(Set(true, false)).get === List((3, Set(true, false)))

      Ns.int.bools.apply(Set(true, false), Set[Boolean]()).get === List((3, Set(true, false)))
      Ns.int.bools.apply(Set(true, false), Set(false)).get === List((2, Set(false)), (3, Set(true, false)))

      // `and`
      Ns.int.bools.apply(true and false).get === List((3, Set(true, false)))
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.bools.apply(true).get === List(Set(true, false))
      Ns.bools.apply(false).get === List(Set(true, false))
      Ns.bools.apply(true, false).get === List(Set(true, false))

      // `or`
      Ns.bools.apply(true or false).get === List(Set(true, false))

      // Seq
      Ns.bools.apply().get === Nil
      Ns.bools.apply(Nil).get === Nil
      Ns.bools.apply(List(true)).get === List(Set(true, false))
      Ns.bools.apply(List(false)).get === List(Set(true, false))
      Ns.bools.apply(List(true, false)).get === List(Set(true, false))
      Ns.bools.apply(List(true), List(false)).get === List(Set(true, false))
      Ns.bools.apply(List(true, false), List(true)).get === List(Set(true, false))
      Ns.bools.apply(List(true), List(false, true)).get === List(Set(true, false))


      // AND semantics

      // Set
      Ns.bools.apply(Set[Boolean]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.bools.apply(Set(true)).get === List(Set(true, false))
      Ns.bools.apply(Set(false)).get === List(Set(true, false))
      Ns.bools.apply(Set(true, false)).get === List(Set(true, false))

      Ns.bools.apply(Set(true, false), Set(false)).get === List(Set(true, false))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bools.apply(true and false).get === List(Set(true, false))
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.bools_.apply(true).get === List(1, 3)
      Ns.int.bools_.apply(false).get === List(2, 3)
      Ns.int.bools_.apply(true, false).get === List(1, 2, 3)

      // `or`
      Ns.int.bools_.apply(true or false).get === List(1, 2, 3)

      // Seq
      Ns.int.bools_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.bools_.apply(Nil).get === List(4)
      Ns.int.bools_.apply(List(true)).get === List(1, 3)
      Ns.int.bools_.apply(List(false)).get === List(2, 3)
      Ns.int.bools_.apply(List(true, false)).get === List(1, 2, 3)
      Ns.int.bools_.apply(List(true), List(false)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.bools_.apply(Set[Boolean]()).get === List(4)
      Ns.int.bools_.apply(Set(true)).get === List(1, 3)
      Ns.int.bools_.apply(Set(false)).get === List(2, 3)
      Ns.int.bools_.apply(Set(true, false)).get === List(3)

      Ns.int.bools_.apply(Set(true, false), Set(true)).get === List(1, 3)
      Ns.int.bools_.apply(Set(true, false), Set(false)).get === List(2, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bools_.apply(true and false).get === List(3)
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Boolean]()

      val l1 = List(true)
      val l2 = List(false)

      val s1 = Set(true)
      val s2 = Set(false)

      val l12 = List(true, false)

      val s12 = Set(true, false)


      // OR semantics

      // Vararg
      Ns.int.bools_.apply(bool1, bool2).get === List(1, 2, 3)

      // `or`
      Ns.int.bools_.apply(bool1 or bool2).get === List(1, 2, 3)

      // Seq
      Ns.int.bools_.apply(seq0).get === List(4)
      Ns.int.bools_.apply(List(bool1), List(bool2)).get === List(1, 2, 3)
      Ns.int.bools_.apply(l1, l2).get === List(1, 2, 3)
      Ns.int.bools_.apply(List(bool1, bool2)).get === List(1, 2, 3)
      Ns.int.bools_.apply(l12).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.bools_.apply(set0).get === List(4)

      Ns.int.bools_.apply(Set(bool1)).get === List(1, 3)
      Ns.int.bools_.apply(s1).get === List(1, 3)

      Ns.int.bools_.apply(Set(bool2)).get === List(2, 3)
      Ns.int.bools_.apply(s2).get === List(2, 3)

      Ns.int.bools_.apply(Set(bool1, bool2)).get === List(3)
      Ns.int.bools_.apply(s12).get === List(3)

      // `and`
      Ns.int.bools_.apply(bool1 and bool2).get === List(3)
    }
  }
}