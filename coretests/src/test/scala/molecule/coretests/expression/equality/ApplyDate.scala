package molecule.coretests.expression.equality

import java.util.Date
import molecule.api._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class ApplyDate extends CoreSpec     {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.date$ insert List(
        (1, Some(date1)),
        (2, Some(date2)),
        (3, Some(date3)),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.date.apply(date1).get === List(date1)
      Ns.date.apply(date2).get === List(date2)
      Ns.date.apply(date1, date2).get === List(date1, date2)

      // `or`
      Ns.date.apply(date1 or date2).get === List(date1, date2)
      Ns.date.apply(date1 or date2 or date3).get === List(date1, date3, date2)

      // Seq
      Ns.date.apply().get === Nil
      Ns.date.apply(Nil).get === Nil
      Ns.date.apply(List(date1)).get === List(date1)
      Ns.date.apply(List(date2)).get === List(date2)
      Ns.date.apply(List(date1, date2)).get === List(date1, date2)
      Ns.date.apply(List(date1), List(date2)).get === List(date1, date2)
      Ns.date.apply(List(date1, date2), List(date3)).get === List(date1, date3, date2)
      Ns.date.apply(List(date1), List(date2, date3)).get === List(date1, date3, date2)
      Ns.date.apply(List(date1, date2, date3)).get === List(date1, date3, date2)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.date_.apply(date1).get === List(1)
      Ns.int.date_.apply(date2).get === List(2)
      Ns.int.date_.apply(date1, date2).get === List(1, 2)

      // `or`
      Ns.int.date_.apply(date1 or date2).get === List(1, 2)
      Ns.int.date_.apply(date1 or date2 or date3).get === List(1, 2, 3)

      // Seq
      Ns.int.date_.apply().get === List(4)
      Ns.int.date_.apply(Nil).get === List(4)
      Ns.int.date_.apply(List(date1)).get === List(1)
      Ns.int.date_.apply(List(date2)).get === List(2)
      Ns.int.date_.apply(List(date1, date2)).get === List(1, 2)
      Ns.int.date_.apply(List(date1), List(date2)).get === List(1, 2)
      Ns.int.date_.apply(List(date1, date2), List(date3)).get === List(1, 2, 3)
      Ns.int.date_.apply(List(date1), List(date2, date3)).get === List(1, 2, 3)
      Ns.int.date_.apply(List(date1, date2, date3)).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.dates$ insert List(
        (1, Some(Set(date1, date2))),
        (2, Some(Set(date2, date3))),
        (3, Some(Set(date3, date4))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.dates.apply(date1).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(date1, date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))

      // `or`
      Ns.int.dates.apply(date1 or date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(date1 or date2 or date3).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      // Seq
      Ns.int.dates.apply().get === Nil
      Ns.int.dates.apply(Nil).get === Nil
      Ns.int.dates.apply(List(date1)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(List(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1, date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1), List(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1, date2), List(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      Ns.int.dates.apply(List(date1), List(date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      Ns.int.dates.apply(List(date1, date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))


      // AND semantics

      // Set
      Ns.int.dates.apply(Set[Date]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.dates.apply(Set(date1)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(Set(date1, date2)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date1, date3)).get === Nil
      Ns.int.dates.apply(Set(date2, date3)).get === List((2, Set(date2, date3)))
      Ns.int.dates.apply(Set(date1, date2, date3)).get === Nil

      Ns.int.dates.apply(Set(date1, date2), Set[Date]()).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date1, date2), Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(Set(date1, date2), Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      Ns.int.dates.apply(Set(date1, date2), Set(date4)).get === List((1, Set(date1, date2)), (3, Set(date3, date4)))
      Ns.int.dates.apply(Set(date1, date2), Set(date2), Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      Ns.int.dates.apply(Set(date1, date2), Set(date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(Set(date1, date2), Set(date2, date4)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date1, date2), Set(date3, date4)).get === List((1, Set(date1, date2)), (3, Set(date3, date4)))

      // `and`
      Ns.int.dates.apply(date1 and date2).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(date1 and date3).get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.dates.apply(date1).get === List(Set(date1, date2))
      Ns.dates.apply(date2).get === List(Set(date1, date3, date2))
      Ns.dates.apply(date1, date2).get === List(Set(date1, date3, date2))

      // `or`
      Ns.dates.apply(date1 or date2).get === List(Set(date1, date3, date2))
      Ns.dates.apply(date1 or date2 or date3).get === List(Set(date1, date4, date3, date2))

      // Seq
      Ns.dates.apply().get === Nil
      Ns.dates.apply(Nil).get === Nil
      Ns.dates.apply(List(date1)).get === List(Set(date1, date2))
      Ns.dates.apply(List(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1, date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1), List(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1, date2), List(date3)).get === List(Set(date1, date4, date3, date2))
      Ns.dates.apply(List(date1), List(date2, date3)).get === List(Set(date1, date4, date3, date2))
      Ns.dates.apply(List(date1, date2, date3)).get === List(Set(date1, date4, date3, date2))


      // AND semantics

      // Set
      Ns.dates.apply(Set[Date]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.dates.apply(Set(date1)).get === List(Set(date1, date2))
      Ns.dates.apply(Set(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(Set(date1, date2)).get === List(Set(date1, date2))
      Ns.dates.apply(Set(date1, date3)).get === Nil
      Ns.dates.apply(Set(date2, date3)).get === List(Set(date2, date3))
      Ns.dates.apply(Set(date1, date2, date3)).get === Nil

      Ns.dates.apply(Set(date1, date2), Set(date2)).get === List(Set(date1, date2, date3))
      Ns.dates.apply(Set(date1, date2), Set(date3)).get === List(Set(date1, date2, date3, date4))
      Ns.dates.apply(Set(date1, date2), Set(date4)).get === List(Set(date1, date2, date3, date4))
      Ns.dates.apply(Set(date1, date2), Set(date2), Set(date3)).get === List(Set(date1, date2, date3, date4))

      Ns.dates.apply(Set(date1, date2), Set(date2, date3)).get === List(Set(date1, date2, date3))
      Ns.dates.apply(Set(date1, date2), Set(date2, date4)).get === List(Set(date1, date2))
      Ns.dates.apply(Set(date1, date2), Set(date3, date4)).get === List(Set(date1, date2, date3, date4))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.dates.apply(date1 and date2).get === List(Set(date1, date2))
      Ns.dates.apply(date1 and date3).get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.dates_.apply(date1).get === List(1)
      Ns.int.dates_.apply(date2).get === List(1, 2)
      Ns.int.dates_.apply(date1, date2).get === List(1, 2)

      // `or`
      Ns.int.dates_.apply(date1 or date2).get === List(1, 2)
      Ns.int.dates_.apply(date1 or date2 or date3).get === List(1, 2, 3)

      // Seq
      Ns.int.dates_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.dates_.apply(Nil).get === List(4)
      Ns.int.dates_.apply(List(date1)).get === List(1)
      Ns.int.dates_.apply(List(date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1, date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1), List(date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1, date2), List(date3)).get === List(1, 2, 3)
      Ns.int.dates_.apply(List(date1), List(date2, date3)).get === List(1, 2, 3)
      Ns.int.dates_.apply(List(date1, date2, date3)).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.dates_.apply(Set[Date]()).get === List(4)
      Ns.int.dates_.apply(Set(date1)).get === List(1)
      Ns.int.dates_.apply(Set(date2)).get === List(1, 2)
      Ns.int.dates_.apply(Set(date1, date2)).get === List(1)
      Ns.int.dates_.apply(Set(date1, date3)).get === Nil
      Ns.int.dates_.apply(Set(date2, date3)).get === List(2)
      Ns.int.dates_.apply(Set(date1, date2, date3)).get === Nil

      Ns.int.dates_.apply(Set(date1, date2), Set(date2)).get === List(1, 2)
      Ns.int.dates_.apply(Set(date1, date2), Set(date3)).get === List(1, 2, 3)
      Ns.int.dates_.apply(Set(date1, date2), Set(date4)).get === List(1, 3)
      Ns.int.dates_.apply(Set(date1, date2), Set(date2), Set(date3)).get === List(1, 2, 3)

      Ns.int.dates_.apply(Set(date1, date2), Set(date2, date3)).get === List(1, 2)
      Ns.int.dates_.apply(Set(date1, date2), Set(date2, date4)).get === List(1)
      Ns.int.dates_.apply(Set(date1, date2), Set(date3, date4)).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.dates_.apply(date1 and date2).get === List(1)
      Ns.int.dates_.apply(date1 and date3).get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[Date]()

      val l1 = List(date1)
      val l2 = List(date2)

      val s1 = Set(date1)
      val s2 = Set(date2)

      val l12 = List(date1, date2)
      val l23 = List(date2, date3)

      val s12 = Set(date1, date2)
      val s23 = Set(date2, date3)


      // OR semantics

      // Vararg
      Ns.int.dates_.apply(date1, date2).get === List(1, 2)

      // `or`
      Ns.int.dates_.apply(date1 or date2).get === List(1, 2)

      // Seq
      Ns.int.dates_.apply(seq0).get === List(4)
      Ns.int.dates_.apply(List(date1), List(date2)).get === List(1, 2)
      Ns.int.dates_.apply(l1, l2).get === List(1, 2)
      Ns.int.dates_.apply(List(date1, date2)).get === List(1, 2)
      Ns.int.dates_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.dates_.apply(set0).get === List(4)

      Ns.int.dates_.apply(Set(date1)).get === List(1)
      Ns.int.dates_.apply(s1).get === List(1)

      Ns.int.dates_.apply(Set(date2)).get === List(1, 2)
      Ns.int.dates_.apply(s2).get === List(1, 2)

      Ns.int.dates_.apply(Set(date1, date2)).get === List(1)
      Ns.int.dates_.apply(s12).get === List(1)

      Ns.int.dates_.apply(Set(date2, date3)).get === List(2)
      Ns.int.dates_.apply(s23).get === List(2)

      Ns.int.dates_.apply(Set(date1, date2), Set(date2, date3)).get === List(1, 2)
      Ns.int.dates_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.dates_.apply(date1 and date2).get === List(1)
    }
  }
}
