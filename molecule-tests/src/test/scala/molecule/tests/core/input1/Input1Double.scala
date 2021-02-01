package molecule.tests.core.input1

import molecule.core._3_dsl2molecule.input.exception.InputMoleculeException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in1_out2._
import molecule.TestSpec


class Input1Double extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.double$ insert List(
        (str1, Some(double1)),
        (str2, Some(double2)),
        (str3, Some(double3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.double(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(double1)).get === List(double1)
        inputMolecule(List(double1, double1)).get === List(double1)
        inputMolecule(List(double1, double2)).get.sorted === List(double1, double2)

        // Varargs
        inputMolecule(double1).get === List(double1)
        inputMolecule(double1, double2).get.sorted === List(double1, double2)

        // `or`
        inputMolecule(double1 or double2).get.sorted === List(double1, double2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.double.not(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)

        inputMolecule(List(double1)).get.sorted === List(double2, double3)
        inputMolecule(List(double1, double1)).get.sorted === List(double2, double3)
        inputMolecule(List(double1, double2)).get.sorted === List(double3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.double.>(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)
        inputMolecule(List(double2)).get.sorted === List(double3)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.double.>=(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)
        inputMolecule(List(double2)).get.sorted === List(double2, double3)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.double.<(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)
        inputMolecule(List(double2)).get === List(double1)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.double.<=(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)
        inputMolecule(List(double2)).get.sorted === List(double1, double2)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.double_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(double1)).get === List(str1)
        inputMolecule(List(double1, double1)).get === List(str1)
        inputMolecule(List(double1, double2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.double_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(double1)).get.sorted === List(str2, str3)
        inputMolecule(List(double1, double1)).get.sorted === List(str2, str3)
        inputMolecule(List(double1, double2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.double_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(double2)).get.sorted === List(str3)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.double_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(double2)).get.sorted === List(str2, str3)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.double_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(double2)).get === List(str1)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.double_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(double2)).get.sorted === List(str1, str2)
        (inputMolecule(List(double2, double3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.double.doubles$ insert List(
        (double1, Some(Set(double1, double2))),
        (double2, Some(Set(double2, double3))),
        (double3, Some(Set(double3, double4))),
        (double4, Some(Set(double4, double5))),
        (double5, Some(Set(double4, double5, double6))),
        (double6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Double]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(double1))).get === List((double1, Set(double1, double2)))
        inputMolecule(List(Set(double1, double1))).get === List((double1, Set(double1, double2)))

        inputMolecule(List(Set(double1, double2))).get === List((double1, Set(double1, double2)))
        inputMolecule(List(Set(double1, double3))).get === Nil
        inputMolecule(List(Set(double2, double3))).get === List((double2, Set(double3, double2)))
        inputMolecule(List(Set(double4, double5))).get === List((double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        // 1 arg
        inputMolecule(Set(double1)).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1, double1)).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1, double2)).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1, double3)).get === Nil
        inputMolecule(Set(double2, double3)).get === List((double2, Set(double3, double2)))
        inputMolecule(Set(double4, double5)).get === List((double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(double1, double2), Set[Double]())).get === List((double1, Set(double1, double2)))
        inputMolecule(List(Set(double1), Set(double1))).get === List((double1, Set(double1, double2)))
        inputMolecule(List(Set(double1), Set(double2))).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(List(Set(double1), Set(double3))).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(List(Set(double1, double2), Set(double3))).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(List(Set(double1), Set(double2, double3))).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(List(Set(double1), Set(double2), Set(double3))).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(List(Set(double1, double2), Set(double3, double4))).get === List((double1, Set(double1, double2)), (double3, Set(double4, double3)))


        // Multiple varargs
        inputMolecule(Set(double1, double2), Set[Double]()).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1), Set(double1)).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1), Set(double2)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(Set(double1), Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1, double2), Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1), Set(double2, double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(Set(double1), Set(double2), Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1, double2), Set(double3, double4)).get === List((double1, Set(double1, double2)), (double3, Set(double4, double3)))

        // `or`
        inputMolecule(Set(double1, double2) or Set[Double]()).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1) or Set(double1)).get === List((double1, Set(double1, double2)))
        inputMolecule(Set(double1) or Set(double2)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(Set(double1) or Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1, double2) or Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1) or Set(double2, double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)))
        inputMolecule(Set(double1) or Set(double2) or Set(double3)).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)))
        inputMolecule(Set(double1, double2) or Set(double3, double4)).get === List((double1, Set(double1, double2)), (double3, Set(double4, double3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (double1, Set(double1, double2, double3)),
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )
        Ns.double.doubles insert all
        val inputMolecule = m(Ns.double.doubles.not(?)) // or m(Ns.double.doubles.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[Double]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(double1).get === ...
        // inputMolecule(List(double1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(double1)).get === List(
          // (double1, Set(double1, double2, double3)),  // double1 match
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )
        // Same as
        inputMolecule(List(Set(double1))).get === List(
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )

        inputMolecule(Set(double2)).get === List(
          // (double1, Set(double1, double2, double3)),  // double2 match
          // (double2, Set(double2, double3, double4)),  // double2 match
          (double3, Set(double3, double4, double5))
        )

        inputMolecule(Set(double3)).get === Nil // double3 match all


        inputMolecule(Set(double1), Set(double2)).get === List(
          // (double1, Set(double1, double2, double3)),  // double1 match, double2 match
          // (double2, Set(double2, double3, double4)),  // double2 match
          (double3, Set(double3, double4, double5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(double1, double2)).get === List(
          // (double1, Set(double1, double2, double3)),  // double1 AND double2 match
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )


        inputMolecule(Set(double1), Set(double3)).get === Nil // double3 match all
        inputMolecule(Set(double1, double3)).get === List(
          // (double1, Set(double1, double2, double3)),  // double1 AND double3 match
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )


        inputMolecule(Set(double1), Set(double2), Set(double3)).get === Nil // double3 match all
        inputMolecule(Set(double1, double2, double3)).get === List(
          // (double1, Set(double1, double2, double3)),  // double1 AND double2 AND double3 match
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )


        inputMolecule(Set(double1, double2), Set(double1)).get === List(
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )
        inputMolecule(Set(double1, double2), Set(double2)).get === List(
          (double3, Set(double3, double4, double5))
        )
        inputMolecule(Set(double1, double2), Set(double3)).get === Nil
        inputMolecule(Set(double1, double2), Set(double4)).get === Nil
        inputMolecule(Set(double1, double2), Set(double5)).get === List(
          (double2, Set(double2, double3, double4))
        )

        inputMolecule(Set(double1, double2), Set(double2, double3)).get === List(
          (double3, Set(double3, double4, double5))
        )
        inputMolecule(Set(double1, double2), Set(double4, double5)).get === List(
          (double2, Set(double2, double3, double4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles.>(?))

        inputMolecule(Nil).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))
        inputMolecule(List(Set[Double]())).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        // (double3, double4), (double4, double5), (double4, double5, double6)
        inputMolecule(List(Set(double2))).get === List((double2, Set(double3)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles.>=(?))

        inputMolecule(Nil).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))
        inputMolecule(List(Set[Double]())).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        // (double2, double4), (double3, double4), (double4, double5), (double4, double5, double6)
        inputMolecule(List(Set(double2))).get === List((double1, Set(double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles.<(?))

        inputMolecule(Nil).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))
        inputMolecule(List(Set[Double]())).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        inputMolecule(List(Set(double2))).get === List((double1, Set(double1)))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles.<=(?))

        inputMolecule(Nil).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))
        inputMolecule(List(Set[Double]())).get === List((double1, Set(double1, double2)), (double2, Set(double3, double2)), (double3, Set(double4, double3)), (double4, Set(double4, double5)), (double5, Set(double4, double6, double5)))

        inputMolecule(List(Set(double2))).get === List((double1, Set(double1, double2)), (double2, Set(double2)))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.doubles(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Double]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(double1))).get === List(Set(double1, double2))
        inputMolecule(List(Set(double2))).get === List(Set(double1, double2, double3)) // (double1, double2) + (double2, double3)
        inputMolecule(List(Set(double3))).get === List(Set(double2, double3, double4)) // (double2, double3) + (double3, double4)

        inputMolecule(List(Set(double1, double2))).get === List(Set(double1, double2))
        inputMolecule(List(Set(double1, double3))).get === Nil
        inputMolecule(List(Set(double2, double3))).get === List(Set(double2, double3))
        inputMolecule(List(Set(double4, double5))).get === List(Set(double4, double5, double6)) // (double4, double5) + (double4, double5, double6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(double1), Set(double1))).get === List(Set(double1, double2))
        inputMolecule(List(Set(double1), Set(double2))).get === List(Set(double1, double2, double3)) // (double1, double2) + (double2, double3)
        inputMolecule(List(Set(double1), Set(double3))).get === List(Set(double1, double4, double3, double2)) // (double1, double2) + (double2, double3) + (double3, double4)
        inputMolecule(List(Set(double2), Set(double3))).get === List(Set(double1, double2, double3, double4)) // (double1, double2) + (double2, double3) + (double3, double4)

        inputMolecule(List(Set(double1, double2), Set(double3))).get === List(Set(double1, double2, double3, double4)) // (double1, double2) + (double2, double3) + (double3, double4)
        inputMolecule(List(Set(double1), Set(double2, double3))).get === List(Set(double1, double3, double2)) // (double1, double2) + (double2, double3)
        inputMolecule(List(Set(double1), Set(double2), Set(double3))).get === List(Set(double1, double2, double3, double4)) // (double1, double2) + (double2, double3) + (double3, double4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (double1, Set(double1, double2, double3)),
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )
        Ns.double.doubles insert all
        val inputMolecule = m(Ns.doubles.not(?)) // or m(Ns.double.doubles.!=(?))

        inputMolecule(Nil).get === List(Set(double1, double2, double3, double4, double5))
        inputMolecule(Set[Double]()).get === List(Set(double1, double2, double3, double4, double5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(double1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(double1)).get === List(Set(double2, double3, double4, double5))
        // Same as
        inputMolecule(List(Set(double1))).get === List(Set(double2, double3, double4, double5))

        inputMolecule(Set(double2)).get === List(Set(double3, double4, double5))
        inputMolecule(Set(double3)).get === Nil // double3 match all

        inputMolecule(Set(double1), Set(double2)).get === List(Set(double3, double4, double5))
        inputMolecule(Set(double1), Set(double3)).get === Nil // double3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(double1, double2)).get === List(Set(double2, double3, double4, double5))
        inputMolecule(Set(double1, double3)).get === List(Set(double2, double3, double4, double5))

        inputMolecule(Set(double1), Set(double2), Set(double3)).get === Nil // double3 match all
        inputMolecule(Set(double1, double2, double3)).get === List(Set(double2, double3, double4, double5))

        inputMolecule(Set(double1, double2), Set(double1)).get === List(Set(double2, double3, double4, double5))
        inputMolecule(Set(double1, double2), Set(double2)).get === List(Set(double3, double4, double5))
        inputMolecule(Set(double1, double2), Set(double3)).get === Nil
        inputMolecule(Set(double1, double2), Set(double4)).get === Nil
        inputMolecule(Set(double1, double2), Set(double5)).get === List(Set(double2, double3, double4))

        inputMolecule(Set(double1, double2), Set(double2, double3)).get === List(Set(double3, double4, double5))
        inputMolecule(Set(double1, double2), Set(double4, double5)).get === List(Set(double2, double3, double4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.doubles.>(?))

        inputMolecule(Nil).get === List(Set(double1, double2, double3, double4, double5, double6))
        inputMolecule(List(Set[Double]())).get === List(Set(double1, double2, double3, double4, double5, double6))

        inputMolecule(List(Set(double2))).get === List(Set(double3, double4, double5, double6))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.doubles.>=(?))

        inputMolecule(Nil).get === List(Set(double1, double2, double3, double4, double5, double6))
        inputMolecule(List(Set[Double]())).get === List(Set(double1, double2, double3, double4, double5, double6))

        inputMolecule(List(Set(double2))).get === List(Set(double2, double3, double4, double5, double6))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.doubles.<(?))

        inputMolecule(Nil).get === List(Set(double1, double2, double3, double4, double5, double6))
        inputMolecule(List(Set[Double]())).get === List(Set(double1, double2, double3, double4, double5, double6))

        inputMolecule(List(Set(double2))).get === List(Set(double1))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.doubles.<=(?))

        inputMolecule(Nil).get === List(Set(double1, double2, double3, double4, double5, double6))
        inputMolecule(List(Set[Double]())).get === List(Set(double1, double2, double3, double4, double5, double6))

        inputMolecule(List(Set(double2))).get === List(Set(double1, double2))

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles_(?))

        inputMolecule(Nil).get === List(double6)
        inputMolecule(List(Set[Double]())).get === List(double6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(double1))).get === List(double1)
        inputMolecule(List(Set(double2))).get.sorted === List(double1, double2)
        inputMolecule(List(Set(double3))).get.sorted === List(double2, double3)

        inputMolecule(List(Set(double1, double1))).get === List(double1)
        inputMolecule(List(Set(double1, double2))).get === List(double1)
        inputMolecule(List(Set(double1, double3))).get === Nil
        inputMolecule(List(Set(double2, double3))).get === List(double2)
        inputMolecule(List(Set(double4, double5))).get.sorted === List(double4, double5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(double1, double2), Set[Double]())).get === List(double1)
        inputMolecule(List(Set(double1), Set(double1))).get === List(double1)
        inputMolecule(List(Set(double1), Set(double2))).get.sorted === List(double1, double2)
        inputMolecule(List(Set(double1), Set(double3))).get.sorted === List(double1, double2, double3)

        inputMolecule(List(Set(double1, double2), Set(double3))).get.sorted === List(double1, double2, double3)
        inputMolecule(List(Set(double1), Set(double2, double3))).get.sorted === List(double1, double2)
        inputMolecule(List(Set(double1), Set(double2), Set(double3))).get.sorted === List(double1, double2, double3)

        inputMolecule(List(Set(double1, double2), Set(double3, double4))).get.sorted === List(double1, double3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (double1, Set(double1, double2, double3)),
          (double2, Set(double2, double3, double4)),
          (double3, Set(double3, double4, double5))
        )
        Ns.double.doubles insert all
        val inputMolecule = m(Ns.double.doubles_.not(?)) // or m(Ns.double.doubles.!=(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3)
        inputMolecule(Set[Double]()).get.sorted === List(double1, double2, double3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(double1).get === ...
        // inputMolecule(List(double1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(double1)).get.sorted === List(double2, double3)
        // Same as
        inputMolecule(List(Set(double1))).get.sorted === List(double2, double3)

        inputMolecule(Set(double2)).get === List(double3)
        inputMolecule(Set(double3)).get === Nil // double3 match all


        inputMolecule(Set(double1), Set(double2)).get === List(double3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(double1, double2)).get.sorted === List(double2, double3)

        inputMolecule(Set(double1), Set(double3)).get === Nil // double3 match all
        inputMolecule(Set(double1, double3)).get.sorted === List(double2, double3)

        inputMolecule(Set(double1), Set(double2), Set(double3)).get === Nil // double3 match all
        inputMolecule(Set(double1, double2, double3)).get.sorted === List(double2, double3)

        inputMolecule(Set(double1, double2), Set(double1)).get.sorted === List(double2, double3)
        inputMolecule(Set(double1, double2), Set(double2)).get === List(double3)
        inputMolecule(Set(double1, double2), Set(double3)).get === Nil
        inputMolecule(Set(double1, double2), Set(double4)).get === Nil
        inputMolecule(Set(double1, double2), Set(double5)).get === List(double2)

        inputMolecule(Set(double1, double2), Set(double2, double3)).get === List(double3)
        inputMolecule(Set(double1, double2), Set(double4, double5)).get === List(double2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles_.>(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3, double4, double5)
        inputMolecule(List(Set[Double]())).get.sorted === List(double1, double2, double3, double4, double5)

        // (double3, double4), (double4, double5), (double4, double5, double6)
        inputMolecule(List(Set(double2))).get.sorted === List(double2, double3, double4, double5)

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles_.>=(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3, double4, double5)
        inputMolecule(List(Set[Double]())).get.sorted === List(double1, double2, double3, double4, double5)

        // (double2, double4), (double3, double4), (double4, double5), (double4, double5, double6)
        inputMolecule(List(Set(double2))).get.sorted === List(double1, double2, double3, double4, double5)

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles_.<(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3, double4, double5)
        inputMolecule(List(Set[Double]())).get.sorted === List(double1, double2, double3, double4, double5)

        inputMolecule(List(Set(double2))).get === List(double1)

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.double.doubles_.<=(?))

        inputMolecule(Nil).get.sorted === List(double1, double2, double3, double4, double5)
        inputMolecule(List(Set[Double]())).get.sorted === List(double1, double2, double3, double4, double5)

        inputMolecule(List(Set(double2))).get.sorted === List(double1, double2)

        (inputMolecule(List(Set(double2, double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(double2), Set(double3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}