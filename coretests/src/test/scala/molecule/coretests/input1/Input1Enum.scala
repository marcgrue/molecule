package molecule.coretests.input1

import molecule.core.input.exception.InputMoleculeException
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.in1_out2._


class Input1Enum extends CoreSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.enum$ insert List(
        (str1, Some(enum1)),
        (str2, Some(enum2)),
        (str3, Some(enum3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.enum(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(enum1)).get === List(enum1)
        inputMolecule(List(enum1, enum1)).get === List(enum1)
        inputMolecule(List(enum1, enum2)).get.sorted === List(enum1, enum2)

        // Varargs
        inputMolecule(enum1).get === List(enum1)
        inputMolecule(enum1, enum2).get.sorted === List(enum1, enum2)

        // `or`
        inputMolecule(enum1 or enum2).get.sorted === List(enum1, enum2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.enum.not(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)

        inputMolecule(List(enum1)).get.sorted === List(enum2, enum3)
        inputMolecule(List(enum1, enum1)).get.sorted === List(enum2, enum3)
        inputMolecule(List(enum1, enum2)).get.sorted === List(enum3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.enum.>(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(List(enum2)).get.sorted === List(enum3)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.enum.>=(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(List(enum2)).get.sorted === List(enum2, enum3)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.enum.<(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(List(enum2)).get === List(enum1)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.enum.<=(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(List(enum2)).get.sorted === List(enum1, enum2)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(enum1)).get === List(str1)
        inputMolecule(List(enum1, enum1)).get === List(str1)
        inputMolecule(List(enum1, enum2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(enum1)).get.sorted === List(str2, str3)
        inputMolecule(List(enum1, enum1)).get.sorted === List(str2, str3)
        inputMolecule(List(enum1, enum2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(enum2)).get.sorted === List(str3)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(enum2)).get.sorted === List(str2, str3)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(enum2)).get === List(str1)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.enum_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(enum2)).get.sorted === List(str1, str2)
        (inputMolecule(List(enum2, enum3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.enum.enums$ insert List(
        (enum1, Some(Set(enum1, enum2))),
        (enum2, Some(Set(enum2, enum3))),
        (enum3, Some(Set(enum3, enum4))),
        (enum4, Some(Set(enum4, enum5))),
        (enum5, Some(Set(enum4, enum5, enum6))),
        (enum6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[String]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(enum1))).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(List(Set(enum1, enum1))).get === List((enum1, Set(enum1, enum2)))

        inputMolecule(List(Set(enum1, enum2))).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(List(Set(enum1, enum3))).get === Nil
        inputMolecule(List(Set(enum2, enum3))).get === List((enum2, Set(enum3, enum2)))
        inputMolecule(List(Set(enum4, enum5))).get === List((enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        // 1 arg
        inputMolecule(Set(enum1)).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1, enum1)).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1, enum2)).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1, enum3)).get === Nil
        inputMolecule(Set(enum2, enum3)).get === List((enum2, Set(enum3, enum2)))
        inputMolecule(Set(enum4, enum5)).get === List((enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(enum1, enum2), Set[String]())).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(List(Set(enum1), Set(enum1))).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(List(Set(enum1), Set(enum2))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(List(Set(enum1), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(List(Set(enum1), Set(enum2, enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(List(Set(enum1, enum2), Set(enum3, enum4))).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))


        // Multiple varargs
        inputMolecule(Set(enum1, enum2), Set[String]()).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1), Set(enum1)).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1), Set(enum2)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(Set(enum1), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1, enum2), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1), Set(enum2, enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(Set(enum1), Set(enum2), Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1, enum2), Set(enum3, enum4)).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))

        // `or`
        inputMolecule(Set(enum1, enum2) or Set[String]()).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1) or Set(enum1)).get === List((enum1, Set(enum1, enum2)))
        inputMolecule(Set(enum1) or Set(enum2)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(Set(enum1) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1, enum2) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1) or Set(enum2, enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)))
        inputMolecule(Set(enum1) or Set(enum2) or Set(enum3)).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)))
        inputMolecule(Set(enum1, enum2) or Set(enum3, enum4)).get === List((enum1, Set(enum1, enum2)), (enum3, Set(enum4, enum3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (enum1, Set(enum1, enum2, enum3)),
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )
        Ns.enum.enums insert all
        val inputMolecule = m(Ns.enum.enums.not(?)) // or m(Ns.enum.enums.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[String]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(enum1).get === ...
        // inputMolecule(List(enum1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(enum1)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum1 match
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )
        // Same as
        inputMolecule(List(Set(enum1))).get === List(
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )

        inputMolecule(Set(enum2)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum2 match
          // (enum2, Set(enum2, enum3, enum4)),  // enum2 match
          (enum3, Set(enum3, enum4, enum5))
        )

        inputMolecule(Set(enum3)).get === Nil // enum3 match all


        inputMolecule(Set(enum1), Set(enum2)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum1 match, enum2 match
          // (enum2, Set(enum2, enum3, enum4)),  // enum2 match
          (enum3, Set(enum3, enum4, enum5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(enum1, enum2)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum1 AND enum2 match
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )


        inputMolecule(Set(enum1), Set(enum3)).get === Nil // enum3 match all
        inputMolecule(Set(enum1, enum3)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum1 AND enum3 match
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )


        inputMolecule(Set(enum1), Set(enum2), Set(enum3)).get === Nil // enum3 match all
        inputMolecule(Set(enum1, enum2, enum3)).get === List(
          // (enum1, Set(enum1, enum2, enum3)),  // enum1 AND enum2 AND enum3 match
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )


        inputMolecule(Set(enum1, enum2), Set(enum1)).get === List(
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )
        inputMolecule(Set(enum1, enum2), Set(enum2)).get === List(
          (enum3, Set(enum3, enum4, enum5))
        )
        inputMolecule(Set(enum1, enum2), Set(enum3)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum4)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum5)).get === List(
          (enum2, Set(enum2, enum3, enum4))
        )

        inputMolecule(Set(enum1, enum2), Set(enum2, enum3)).get === List(
          (enum3, Set(enum3, enum4, enum5))
        )
        inputMolecule(Set(enum1, enum2), Set(enum4, enum5)).get === List(
          (enum2, Set(enum2, enum3, enum4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums.>(?))

        inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
        inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        // (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
        inputMolecule(List(Set(enum2))).get === List((enum2, Set(enum3)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums.>=(?))

        inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
        inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        // (enum2, enum4), (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
        inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums.<(?))

        inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
        inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum1)))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums.<=(?))

        inputMolecule(Nil).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))
        inputMolecule(List(Set[String]())).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum3, enum2)), (enum3, Set(enum4, enum3)), (enum4, Set(enum4, enum5)), (enum5, Set(enum4, enum6, enum5)))

        inputMolecule(List(Set(enum2))).get === List((enum1, Set(enum1, enum2)), (enum2, Set(enum2)))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.enums(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[String]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(enum1))).get === List(Set(enum1, enum2))
        inputMolecule(List(Set(enum2))).get === List(Set(enum1, enum2, enum3)) // (enum1, enum2) + (enum2, enum3)
        inputMolecule(List(Set(enum3))).get === List(Set(enum2, enum3, enum4)) // (enum2, enum3) + (enum3, enum4)

        inputMolecule(List(Set(enum1, enum2))).get === List(Set(enum1, enum2))
        inputMolecule(List(Set(enum1, enum3))).get === Nil
        inputMolecule(List(Set(enum2, enum3))).get === List(Set(enum2, enum3))
        inputMolecule(List(Set(enum4, enum5))).get === List(Set(enum4, enum5, enum6)) // (enum4, enum5) + (enum4, enum5, enum6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(enum1), Set(enum1))).get === List(Set(enum1, enum2))
        inputMolecule(List(Set(enum1), Set(enum2))).get === List(Set(enum1, enum2, enum3)) // (enum1, enum2) + (enum2, enum3)
        inputMolecule(List(Set(enum1), Set(enum3))).get === List(Set(enum1, enum4, enum3, enum2)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
        inputMolecule(List(Set(enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)

        inputMolecule(List(Set(enum1, enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
        inputMolecule(List(Set(enum1), Set(enum2, enum3))).get === List(Set(enum1, enum3, enum2)) // (enum1, enum2) + (enum2, enum3)
        inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get === List(Set(enum1, enum2, enum3, enum4)) // (enum1, enum2) + (enum2, enum3) + (enum3, enum4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (enum1, Set(enum1, enum2, enum3)),
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )
        Ns.enum.enums insert all
        val inputMolecule = m(Ns.enums.not(?)) // or m(Ns.enum.enums.!=(?))

        inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5))
        inputMolecule(Set[String]()).get === List(Set(enum1, enum2, enum3, enum4, enum5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(enum1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(enum1)).get === List(Set(enum2, enum3, enum4, enum5))
        // Same as
        inputMolecule(List(Set(enum1))).get === List(Set(enum2, enum3, enum4, enum5))

        inputMolecule(Set(enum2)).get === List(Set(enum3, enum4, enum5))
        inputMolecule(Set(enum3)).get === Nil // enum3 match all

        inputMolecule(Set(enum1), Set(enum2)).get === List(Set(enum3, enum4, enum5))
        inputMolecule(Set(enum1), Set(enum3)).get === Nil // enum3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(enum1, enum2)).get === List(Set(enum2, enum3, enum4, enum5))
        inputMolecule(Set(enum1, enum3)).get === List(Set(enum2, enum3, enum4, enum5))

        inputMolecule(Set(enum1), Set(enum2), Set(enum3)).get === Nil // enum3 match all
        inputMolecule(Set(enum1, enum2, enum3)).get === List(Set(enum2, enum3, enum4, enum5))

        inputMolecule(Set(enum1, enum2), Set(enum1)).get === List(Set(enum2, enum3, enum4, enum5))
        inputMolecule(Set(enum1, enum2), Set(enum2)).get === List(Set(enum3, enum4, enum5))
        inputMolecule(Set(enum1, enum2), Set(enum3)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum4)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum5)).get === List(Set(enum2, enum3, enum4))

        inputMolecule(Set(enum1, enum2), Set(enum2, enum3)).get === List(Set(enum3, enum4, enum5))
        inputMolecule(Set(enum1, enum2), Set(enum4, enum5)).get === List(Set(enum2, enum3, enum4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.enums.>(?))

        inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
        inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

        inputMolecule(List(Set(enum2))).get === List(Set(enum3, enum4, enum5, enum6))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.enums.>=(?))

        inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
        inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

        inputMolecule(List(Set(enum2))).get === List(Set(enum2, enum3, enum4, enum5, enum6))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.enums.<(?))

        inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
        inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

        inputMolecule(List(Set(enum2))).get === List(Set(enum1))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.enums.<=(?))

        inputMolecule(Nil).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))
        inputMolecule(List(Set[String]())).get === List(Set(enum1, enum2, enum3, enum4, enum5, enum6))

        inputMolecule(List(Set(enum2))).get === List(Set(enum1, enum2))

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums_(?))

        inputMolecule(Nil).get === List(enum6)
        inputMolecule(List(Set[String]())).get === List(enum6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(enum1))).get === List(enum1)
        inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2)
        inputMolecule(List(Set(enum3))).get.sorted === List(enum2, enum3)

        inputMolecule(List(Set(enum1, enum1))).get === List(enum1)
        inputMolecule(List(Set(enum1, enum2))).get === List(enum1)
        inputMolecule(List(Set(enum1, enum3))).get === Nil
        inputMolecule(List(Set(enum2, enum3))).get === List(enum2)
        inputMolecule(List(Set(enum4, enum5))).get.sorted === List(enum4, enum5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(enum1, enum2), Set[String]())).get === List(enum1)
        inputMolecule(List(Set(enum1), Set(enum1))).get === List(enum1)
        inputMolecule(List(Set(enum1), Set(enum2))).get.sorted === List(enum1, enum2)
        inputMolecule(List(Set(enum1), Set(enum3))).get.sorted === List(enum1, enum2, enum3)

        inputMolecule(List(Set(enum1, enum2), Set(enum3))).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(List(Set(enum1), Set(enum2, enum3))).get.sorted === List(enum1, enum2)
        inputMolecule(List(Set(enum1), Set(enum2), Set(enum3))).get.sorted === List(enum1, enum2, enum3)

        inputMolecule(List(Set(enum1, enum2), Set(enum3, enum4))).get.sorted === List(enum1, enum3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (enum1, Set(enum1, enum2, enum3)),
          (enum2, Set(enum2, enum3, enum4)),
          (enum3, Set(enum3, enum4, enum5))
        )
        Ns.enum.enums insert all
        val inputMolecule = m(Ns.enum.enums_.not(?)) // or m(Ns.enum.enums.!=(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3)
        inputMolecule(Set[String]()).get.sorted === List(enum1, enum2, enum3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(enum1).get === ...
        // inputMolecule(List(enum1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(enum1)).get.sorted === List(enum2, enum3)
        // Same as
        inputMolecule(List(Set(enum1))).get.sorted === List(enum2, enum3)

        inputMolecule(Set(enum2)).get === List(enum3)
        inputMolecule(Set(enum3)).get === Nil // enum3 match all


        inputMolecule(Set(enum1), Set(enum2)).get === List(enum3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(enum1, enum2)).get.sorted === List(enum2, enum3)

        inputMolecule(Set(enum1), Set(enum3)).get === Nil // enum3 match all
        inputMolecule(Set(enum1, enum3)).get.sorted === List(enum2, enum3)

        inputMolecule(Set(enum1), Set(enum2), Set(enum3)).get === Nil // enum3 match all
        inputMolecule(Set(enum1, enum2, enum3)).get.sorted === List(enum2, enum3)

        inputMolecule(Set(enum1, enum2), Set(enum1)).get.sorted === List(enum2, enum3)
        inputMolecule(Set(enum1, enum2), Set(enum2)).get === List(enum3)
        inputMolecule(Set(enum1, enum2), Set(enum3)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum4)).get === Nil
        inputMolecule(Set(enum1, enum2), Set(enum5)).get === List(enum2)

        inputMolecule(Set(enum1, enum2), Set(enum2, enum3)).get === List(enum3)
        inputMolecule(Set(enum1, enum2), Set(enum4, enum5)).get === List(enum2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums_.>(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
        inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

        // (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
        inputMolecule(List(Set(enum2))).get.sorted === List(enum2, enum3, enum4, enum5)

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums_.>=(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
        inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

        // (enum2, enum4), (enum3, enum4), (enum4, enum5), (enum4, enum5, enum6)
        inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums_.<(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
        inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

        inputMolecule(List(Set(enum2))).get === List(enum1)

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.enum.enums_.<=(?))

        inputMolecule(Nil).get.sorted === List(enum1, enum2, enum3, enum4, enum5)
        inputMolecule(List(Set[String]())).get.sorted === List(enum1, enum2, enum3, enum4, enum5)

        inputMolecule(List(Set(enum2))).get.sorted === List(enum1, enum2)

        (inputMolecule(List(Set(enum2, enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(enum2), Set(enum3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}