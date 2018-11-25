package molecule.coretests.input1

import molecule.api.in1_out2._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec


class Input1Boolean extends CoreSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.bool$ insert List(
        (1, Some(true)),
        (2, Some(false)),
        (3, Some(true)),
        (4, Some(false)),
        (5, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.bool(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(true)).get === List(true)
        inputMolecule(List(true, true)).get === List(true)
        inputMolecule(List(true, false)).get === List(false, true)

        // Varargs
        inputMolecule(true).get === List(true)
        inputMolecule(true, false).get.sorted === List(false, true)

        // `or`
        inputMolecule(true or false).get.sorted === List(false, true)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.bool.not(?))

        inputMolecule(Nil).get.sorted === List(false, true)

        inputMolecule(List(true)).get.sorted === List(false)
        inputMolecule(List(true, true)).get.sorted === List(false)
        inputMolecule(List(true, false)).get.sorted === Nil
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.int.bool_(?))

        inputMolecule(Nil).get === List(5)
        inputMolecule(List(true)).get === List(1, 3)
        inputMolecule(List(true, true)).get === List(1, 3)
        inputMolecule(List(true, false)).get.sorted === List(1, 2, 3, 4)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.int.bool_.not(?))

        inputMolecule(Nil).get.sorted === List(1, 2, 3, 4)
        inputMolecule(List(true)).get.sorted === List(2, 4)
        inputMolecule(List(true, true)).get.sorted === List(2, 4)
        inputMolecule(List(true, false)).get.sorted === Nil // No other values to negate
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.bools$ insert List(
        (1, Some(Set(true))),
        (2, Some(Set(false))),
        (3, Some(Set(true, false))),
        (4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.int.bools(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Boolean]())).get === Nil

        inputMolecule(List(Set(true))).get === List((1, Set(true)), (3, Set(true, false)))
        inputMolecule(List(Set(false))).get === List((2, Set(false)), (3, Set(true, false)))
        inputMolecule(List(Set(true, false))).get === List((3, Set(true, false)))
      }


      "!=" in new ManySetup {
        val inputMolecule = m(Ns.int.bools.not(?)) // or m(Ns.int.bools.!=(?))

        inputMolecule(Nil).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))
        inputMolecule(List(Set[Boolean]())).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

        inputMolecule(List(Set(true))).get === List((2, Set(false)))
        inputMolecule(List(Set(false))).get === List((1, Set(true)))
        inputMolecule(List(Set(true, false))).get === List((1, Set(true)), (2, Set(false)))
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bools(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Boolean]())).get === Nil

        inputMolecule(List(Set(true))).get === List(Set(true, false)) // (true) + (true, false)
        inputMolecule(List(Set(false))).get === List(Set(true, false)) // (false) + (true, false)
        inputMolecule(List(Set(true, false))).get === List(Set(true, false)) // (true, false)

      }


      "!=" in new ManySetup {
        val inputMolecule = m(Ns.bools.not(?)) // or m(Ns.bools.!=(?))

        inputMolecule(Nil).get === List(Set(true, false))
        inputMolecule(List(Set[Boolean]())).get === List(Set(true, false))

        inputMolecule(List(Set(true))).get === List(Set(false))
        inputMolecule(List(Set(false))).get === List(Set(true))
        inputMolecule(List(Set(true, false))).get === List(Set(true, false))
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.int.bools_(?))

        inputMolecule(Nil).get === List(4)
        inputMolecule(List(Set[Boolean]())).get === List(4)

        inputMolecule(List(Set(true))).get === List(1, 3)
        inputMolecule(List(Set(false))).get.sorted === List(2, 3)
        inputMolecule(List(Set(true, false))).get.sorted === List(3)
      }


      "!=" in new ManySetup {
        val inputMolecule = m(Ns.int.bools_.not(?)) // or m(Ns.int.bools_.!=(?))

        inputMolecule(Nil).get.sorted === List(1, 2, 3)
        inputMolecule(List(Set[Boolean]())).get.sorted === List(1, 2, 3)

        inputMolecule(List(Set(true))).get.sorted === List(2)
        inputMolecule(List(Set(false))).get.sorted === List(1)
        inputMolecule(List(Set(true, false))).get.sorted === List(1, 2)
      }
    }
  }
}