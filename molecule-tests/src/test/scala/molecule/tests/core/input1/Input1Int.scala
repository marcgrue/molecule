package molecule.tests.core.input1

import molecule.core.input.exception.InputMoleculeException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in1_out2._
import molecule.TestSpec


class Input1Int extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.int$ insert List(
        (str1, Some(int1)),
        (str2, Some(int2)),
        (str3, Some(int3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.int(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(int1)).get === List(int1)
        inputMolecule(List(int1, int1)).get === List(int1)
        inputMolecule(List(int1, int2)).get === List(int1, int2)

        // Varargs
        inputMolecule(int1).get === List(int1)
        inputMolecule(int1, int2).get === List(int1, int2)

        // `or`
        inputMolecule(int1 or int2).get === List(int1, int2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.int.not(?))

        inputMolecule(Nil).get === List(int1, int2, int3)

        inputMolecule(List(int1)).get === List(int2, int3)
        inputMolecule(List(int1, int1)).get === List(int2, int3)
        inputMolecule(List(int1, int2)).get === List(int3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.int.>(?))

        inputMolecule(Nil).get === List(int1, int2, int3)
        inputMolecule(List(int2)).get === List(int3)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.int.>=(?))

        inputMolecule(Nil).get === List(int1, int2, int3)
        inputMolecule(List(int2)).get === List(int2, int3)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.int.<(?))

        inputMolecule(Nil).get === List(int1, int2, int3)
        inputMolecule(List(int2)).get === List(int1)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.int.<=(?))

        inputMolecule(Nil).get === List(int1, int2, int3)
        inputMolecule(List(int2)).get === List(int1, int2)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.int_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(int1)).get === List(str1)
        inputMolecule(List(int1, int1)).get === List(str1)
        inputMolecule(List(int1, int2)).get === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.int_.not(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(int1)).get === List(str2, str3)
        inputMolecule(List(int1, int1)).get === List(str2, str3)
        inputMolecule(List(int1, int2)).get === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.int_.>(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(int2)).get === List(str3)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.int_.>=(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(int2)).get === List(str2, str3)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.int_.<(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(int2)).get === List(str1)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.int_.<=(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(int2)).get === List(str1, str2)
        (inputMolecule(List(int2, int3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.ints$ insert List(
        (int1, Some(Set(int1, int2))),
        (int2, Some(Set(int2, int3))),
        (int3, Some(Set(int3, int4))),
        (int4, Some(Set(int4, int5))),
        (int5, Some(Set(int4, int5, int6))),
        (int6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.int.ints(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Int]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1, int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))

        inputMolecule(List(Set(int1, int2))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1, int3))).get === Nil
        inputMolecule(List(Set(int2, int3))).get === List((int2, Set(int3, int2)))
        inputMolecule(List(Set(int4, int5))).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        // 1 arg
        inputMolecule(Set(int1)).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1, int1)).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1, int2)).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1, int3)).get === Nil
        inputMolecule(Set(int2, int3)).get === List((int2, Set(int3, int2)))
        inputMolecule(Set(int4, int5)).get === List((int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(int1, int2), Set[Int]())).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1), Set(int1))).get === List((int1, Set(int1, int2)))
        inputMolecule(List(Set(int1), Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(List(Set(int1), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(List(Set(int1, int2), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(List(Set(int1), Set(int2, int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List((int1, Set(int1, int2)), (int3, Set(int4, int3)))


        // Multiple varargs
        inputMolecule(Set(int1, int2), Set[Int]()).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1), Set(int1)).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1), Set(int2)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(Set(int1), Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1, int2), Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1), Set(int2, int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(Set(int1), Set(int2), Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1, int2), Set(int3, int4)).get === List((int1, Set(int1, int2)), (int3, Set(int4, int3)))

        // `or`
        inputMolecule(Set(int1, int2) or Set[Int]()).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1) or Set(int1)).get === List((int1, Set(int1, int2)))
        inputMolecule(Set(int1) or Set(int2)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(Set(int1) or Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1, int2) or Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1) or Set(int2, int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)))
        inputMolecule(Set(int1) or Set(int2) or Set(int3)).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)))
        inputMolecule(Set(int1, int2) or Set(int3, int4)).get === List((int1, Set(int1, int2)), (int3, Set(int4, int3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (int1, Set(int1, int2, int3)),
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )
        Ns.int.ints insert all
        val inputMolecule = m(Ns.int.ints.not(?)) // or m(Ns.int.ints.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[Int]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(int1).get === ...
        // inputMolecule(List(int1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(int1)).get === List(
          // (int1, Set(int1, int2, int3)),  // int1 match
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )
        // Same as
        inputMolecule(List(Set(int1))).get === List(
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )

        inputMolecule(Set(int2)).get === List(
          // (int1, Set(int1, int2, int3)),  // int2 match
          // (int2, Set(int2, int3, int4)),  // int2 match
          (int3, Set(int3, int4, int5))
        )

        inputMolecule(Set(int3)).get === Nil // int3 match all


        inputMolecule(Set(int1), Set(int2)).get === List(
          // (int1, Set(int1, int2, int3)),  // int1 match, int2 match
          // (int2, Set(int2, int3, int4)),  // int2 match
          (int3, Set(int3, int4, int5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(int1, int2)).get === List(
          // (int1, Set(int1, int2, int3)),  // int1 AND int2 match
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )


        inputMolecule(Set(int1), Set(int3)).get === Nil // int3 match all
        inputMolecule(Set(int1, int3)).get === List(
          // (int1, Set(int1, int2, int3)),  // int1 AND int3 match
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )


        inputMolecule(Set(int1), Set(int2), Set(int3)).get === Nil // int3 match all
        inputMolecule(Set(int1, int2, int3)).get === List(
          // (int1, Set(int1, int2, int3)),  // int1 AND int2 AND int3 match
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )


        inputMolecule(Set(int1, int2), Set(int1)).get === List(
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )
        inputMolecule(Set(int1, int2), Set(int2)).get === List(
          (int3, Set(int3, int4, int5))
        )
        inputMolecule(Set(int1, int2), Set(int3)).get === Nil
        inputMolecule(Set(int1, int2), Set(int4)).get === Nil
        inputMolecule(Set(int1, int2), Set(int5)).get === List(
          (int2, Set(int2, int3, int4))
        )

        inputMolecule(Set(int1, int2), Set(int2, int3)).get === List(
          (int3, Set(int3, int4, int5))
        )
        inputMolecule(Set(int1, int2), Set(int4, int5)).get === List(
          (int2, Set(int2, int3, int4))
        )
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.int.ints.>(?))

        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        // (int3), (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List((int2, Set(int3)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.int.ints.>=(?))

        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List((int1, Set(int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.int.ints.<(?))

        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        inputMolecule(List(Set(int2))).get === List((int1, Set(int1)))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.int.ints.<=(?))

        inputMolecule(Nil).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))
        inputMolecule(List(Set[Int]())).get === List((int1, Set(int1, int2)), (int2, Set(int3, int2)), (int3, Set(int4, int3)), (int4, Set(int4, int5)), (int5, Set(int4, int6, int5)))

        inputMolecule(List(Set(int2))).get === List((int1, Set(int1, int2)), (int2, Set(int2)))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.ints(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Int]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(int1))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int3))).get === List(Set(int2, int3, int4)) // (int2, int3) + (int3, int4)

        inputMolecule(List(Set(int1, int2))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1, int3))).get === Nil
        inputMolecule(List(Set(int2, int3))).get === List(Set(int2, int3))
        inputMolecule(List(Set(int4, int5))).get === List(Set(int4, int5, int6)) // (int4, int5) + (int4, int5, int6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(int1), Set(int1))).get === List(Set(int1, int2))
        inputMolecule(List(Set(int1), Set(int2))).get === List(Set(int1, int2, int3)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int1), Set(int3))).get === List(Set(int1, int4, int3, int2)) // (int1, int2) + (int2, int3) + (int3, int4)
        inputMolecule(List(Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)

        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(Set(int1, int3, int2)) // (int1, int2) + (int2, int3)
        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(Set(int1, int2, int3, int4)) // (int1, int2) + (int2, int3) + (int3, int4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (int1, Set(int1, int2, int3)),
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )
        Ns.int.ints insert all
        val inputMolecule = m(Ns.ints.not(?)) // or m(Ns.int.ints.!=(?))

        inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5))
        inputMolecule(Set[Int]()).get === List(Set(int1, int2, int3, int4, int5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(int1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(int1)).get === List(Set(int2, int3, int4, int5))
        // Same as
        inputMolecule(List(Set(int1))).get === List(Set(int2, int3, int4, int5))

        inputMolecule(Set(int2)).get === List(Set(int3, int4, int5))
        inputMolecule(Set(int3)).get === Nil // int3 match all

        inputMolecule(Set(int1), Set(int2)).get === List(Set(int3, int4, int5))
        inputMolecule(Set(int1), Set(int3)).get === Nil // int3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(int1, int2)).get === List(Set(int2, int3, int4, int5))
        inputMolecule(Set(int1, int3)).get === List(Set(int2, int3, int4, int5))

        inputMolecule(Set(int1), Set(int2), Set(int3)).get === Nil // int3 match all
        inputMolecule(Set(int1, int2, int3)).get === List(Set(int2, int3, int4, int5))

        inputMolecule(Set(int1, int2), Set(int1)).get === List(Set(int2, int3, int4, int5))
        inputMolecule(Set(int1, int2), Set(int2)).get === List(Set(int3, int4, int5))
        inputMolecule(Set(int1, int2), Set(int3)).get === Nil
        inputMolecule(Set(int1, int2), Set(int4)).get === Nil
        inputMolecule(Set(int1, int2), Set(int5)).get === List(Set(int2, int3, int4))

        inputMolecule(Set(int1, int2), Set(int2, int3)).get === List(Set(int3, int4, int5))
        inputMolecule(Set(int1, int2), Set(int4, int5)).get === List(Set(int2, int3, int4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.ints.>(?))

        inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5, int6))
        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5, int6))

        inputMolecule(List(Set(int2))).get === List(Set(int3, int4, int5, int6))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.ints.>=(?))

        inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5, int6))
        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5, int6))

        inputMolecule(List(Set(int2))).get === List(Set(int2, int3, int4, int5, int6))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.ints.<(?))

        inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5, int6))
        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5, int6))

        inputMolecule(List(Set(int2))).get === List(Set(int1))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.ints.<=(?))

        inputMolecule(Nil).get === List(Set(int1, int2, int3, int4, int5, int6))
        inputMolecule(List(Set[Int]())).get === List(Set(int1, int2, int3, int4, int5, int6))

        inputMolecule(List(Set(int2))).get === List(Set(int1, int2))

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.int.ints_(?))

        inputMolecule(Nil).get === List(int6)
        inputMolecule(List(Set[Int]())).get === List(int6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(int1))).get === List(int1)
        inputMolecule(List(Set(int2))).get === List(int1, int2)
        inputMolecule(List(Set(int3))).get === List(int2, int3)

        inputMolecule(List(Set(int1, int1))).get === List(int1)
        inputMolecule(List(Set(int1, int2))).get === List(int1)
        inputMolecule(List(Set(int1, int3))).get === Nil
        inputMolecule(List(Set(int2, int3))).get === List(int2)
        inputMolecule(List(Set(int4, int5))).get === List(int4, int5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(int1, int2), Set[Int]())).get === List(int1)
        inputMolecule(List(Set(int1), Set(int1))).get === List(int1)
        inputMolecule(List(Set(int1), Set(int2))).get === List(int1, int2)
        inputMolecule(List(Set(int1), Set(int3))).get === List(int1, int2, int3)

        inputMolecule(List(Set(int1, int2), Set(int3))).get === List(int1, int2, int3)
        inputMolecule(List(Set(int1), Set(int2, int3))).get === List(int1, int2)
        inputMolecule(List(Set(int1), Set(int2), Set(int3))).get === List(int1, int2, int3)

        inputMolecule(List(Set(int1, int2), Set(int3, int4))).get === List(int1, int3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (int1, Set(int1, int2, int3)),
          (int2, Set(int2, int3, int4)),
          (int3, Set(int3, int4, int5))
        )
        Ns.int.ints insert all
        val inputMolecule = m(Ns.int.ints_.not(?)) // or m(Ns.int.ints.!=(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(Set[Int]()).get.sorted === List(int1, int2, int3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(int1).get === ...
        // inputMolecule(List(int1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(int1)).get.sorted === List(int2, int3)
        // Same as
        inputMolecule(List(Set(int1))).get.sorted === List(int2, int3)

        inputMolecule(Set(int2)).get === List(int3)
        inputMolecule(Set(int3)).get === Nil // int3 match all


        inputMolecule(Set(int1), Set(int2)).get === List(int3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(int1, int2)).get.sorted === List(int2, int3)

        inputMolecule(Set(int1), Set(int3)).get === Nil // int3 match all
        inputMolecule(Set(int1, int3)).get.sorted === List(int2, int3)

        inputMolecule(Set(int1), Set(int2), Set(int3)).get === Nil // int3 match all
        inputMolecule(Set(int1, int2, int3)).get.sorted === List(int2, int3)

        inputMolecule(Set(int1, int2), Set(int1)).get.sorted === List(int2, int3)
        inputMolecule(Set(int1, int2), Set(int2)).get === List(int3)
        inputMolecule(Set(int1, int2), Set(int3)).get === Nil
        inputMolecule(Set(int1, int2), Set(int4)).get === Nil
        inputMolecule(Set(int1, int2), Set(int5)).get === List(int2)

        inputMolecule(Set(int1, int2), Set(int2, int3)).get === List(int3)
        inputMolecule(Set(int1, int2), Set(int4, int5)).get === List(int2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.int.ints_.>(?))

        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)

        // (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(int2, int3, int4, int5)

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.int.ints_.>=(?))

        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)

        // (int2, int4), (int3, int4), (int4, int5), (int4, int5, int6)
        inputMolecule(List(Set(int2))).get === List(int1, int2, int3, int4, int5)

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.int.ints_.<(?))

        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)

        inputMolecule(List(Set(int2))).get === List(int1)

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.int.ints_.<=(?))

        inputMolecule(Nil).get === List(int1, int2, int3, int4, int5)
        inputMolecule(List(Set[Int]())).get === List(int1, int2, int3, int4, int5)

        inputMolecule(List(Set(int2))).get === List(int1, int2)

        (inputMolecule(List(Set(int2, int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(int2), Set(int3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}