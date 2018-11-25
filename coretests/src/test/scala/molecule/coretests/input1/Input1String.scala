package molecule.coretests.input1

import molecule.api.in1_out2._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.input.exception.InputMoleculeException


class Input1String extends CoreSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.str$ insert List(
        (int1, Some(str1)),
        (int2, Some(str2)),
        (int3, Some(str3)),
        (int4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(str1)).get === List(str1)
        inputMolecule(List(str1, str1)).get === List(str1)
        inputMolecule(List(str1, str2)).get.sorted === List(str1, str2)

        // Varargs
        inputMolecule(str1).get === List(str1)
        inputMolecule(str1, str2).get.sorted === List(str1, str2)

        // `or`
        inputMolecule(str1 or str2).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)

        inputMolecule(List(str1)).get.sorted === List(str2, str3)
        inputMolecule(List(str1, str1)).get.sorted === List(str2, str3)

        inputMolecule(List(str1, str2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(str2)).get.sorted === List(str3)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(str2)).get.sorted === List(str2, str3)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(str2)).get === List(str1)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(str2)).get.sorted === List(str1, str2)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.int.str_(?))

        inputMolecule(Nil).get === List(int4)
        inputMolecule(List(str1)).get === List(int1)
        inputMolecule(List(str1, str1)).get === List(int1)
        inputMolecule(List(str1, str2)).get.sorted === List(int1, int2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.int.str_.not(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(List(str1)).get.sorted === List(int2, int3)
        inputMolecule(List(str1, str1)).get.sorted === List(int2, int3)
        inputMolecule(List(str1, str2)).get.sorted === List(int3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.int.str_.>(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(List(str2)).get.sorted === List(int3)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.int.str_.>=(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(List(str2)).get.sorted === List(int2, int3)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.int.str_.<(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(List(str2)).get === List(int1)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.int.str_.<=(?))

        inputMolecule(Nil).get.sorted === List(int1, int2, int3)
        inputMolecule(List(str2)).get.sorted === List(int1, int2)
        (inputMolecule(List(str2, str3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.str.strs$ insert List(
        (str1, Some(Set(str1, str2))),
        (str2, Some(Set(str2, str3))),
        (str3, Some(Set(str3, str4))),
        (str4, Some(Set(str4, str5))),
        (str5, Some(Set(str4, str5, str6))),
        (str6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.str.strs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[String]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(str1))).get === List((str1, Set(str1, str2)))
        inputMolecule(List(Set(str1, str1))).get === List((str1, Set(str1, str2)))

        inputMolecule(List(Set(str1, str2))).get === List((str1, Set(str1, str2)))
        inputMolecule(List(Set(str1, str3))).get === Nil
        inputMolecule(List(Set(str2, str3))).get === List((str2, Set(str3, str2)))
        inputMolecule(List(Set(str4, str5))).get === List((str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        // 1 arg
        inputMolecule(Set(str1)).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1, str1)).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1, str2)).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1, str3)).get === Nil
        inputMolecule(Set(str2, str3)).get === List((str2, Set(str3, str2)))
        inputMolecule(Set(str4, str5)).get === List((str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(str1, str2), Set[String]())).get === List((str1, Set(str1, str2)))
        inputMolecule(List(Set(str1), Set(str1))).get === List((str1, Set(str1, str2)))
        inputMolecule(List(Set(str1), Set(str2))).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(List(Set(str1), Set(str3))).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(List(Set(str1, str2), Set(str3))).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(List(Set(str1), Set(str2, str3))).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(List(Set(str1), Set(str2), Set(str3))).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(List(Set(str1, str2), Set(str3, str4))).get === List((str1, Set(str1, str2)), (str3, Set(str4, str3)))


        // Multiple varargs
        inputMolecule(Set(str1, str2), Set[String]()).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1), Set(str1)).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1), Set(str2)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(Set(str1), Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1, str2), Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1), Set(str2, str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(Set(str1), Set(str2), Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1, str2), Set(str3, str4)).get === List((str1, Set(str1, str2)), (str3, Set(str4, str3)))

        // `or`
        inputMolecule(Set(str1, str2) or Set[String]()).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1) or Set(str1)).get === List((str1, Set(str1, str2)))
        inputMolecule(Set(str1) or Set(str2)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(Set(str1) or Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1, str2) or Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1) or Set(str2, str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)))
        inputMolecule(Set(str1) or Set(str2) or Set(str3)).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)))
        inputMolecule(Set(str1, str2) or Set(str3, str4)).get === List((str1, Set(str1, str2)), (str3, Set(str4, str3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (str1, Set(str1, str2, str3)),
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )
        Ns.str.strs insert all
        val inputMolecule = m(Ns.str.strs.not(?)) // or m(Ns.str.strs.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[String]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(str1).get === ...
        // inputMolecule(List(str1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(str1)).get === List(
          // (str1, Set(str1, str2, str3)),  // str1 match
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )
        // Same as
        inputMolecule(List(Set(str1))).get === List(
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )

        inputMolecule(Set(str2)).get === List(
          // (str1, Set(str1, str2, str3)),  // str2 match
          // (str2, Set(str2, str3, str4)),  // str2 match
          (str3, Set(str3, str4, str5))
        )

        inputMolecule(Set(str3)).get === Nil // str3 match all


        inputMolecule(Set(str1), Set(str2)).get === List(
          // (str1, Set(str1, str2, str3)),  // str1 match, str2 match
          // (str2, Set(str2, str3, str4)),  // str2 match
          (str3, Set(str3, str4, str5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(str1, str2)).get === List(
          // (str1, Set(str1, str2, str3)),  // str1 AND str2 match
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )


        inputMolecule(Set(str1), Set(str3)).get === Nil // str3 match all
        inputMolecule(Set(str1, str3)).get === List(
          // (str1, Set(str1, str2, str3)),  // str1 AND str3 match
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )


        inputMolecule(Set(str1), Set(str2), Set(str3)).get === Nil // str3 match all
        inputMolecule(Set(str1, str2, str3)).get === List(
          // (str1, Set(str1, str2, str3)),  // str1 AND str2 AND str3 match
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )


        inputMolecule(Set(str1, str2), Set(str1)).get === List(
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )
        inputMolecule(Set(str1, str2), Set(str2)).get === List(
          (str3, Set(str3, str4, str5))
        )
        inputMolecule(Set(str1, str2), Set(str3)).get === Nil
        inputMolecule(Set(str1, str2), Set(str4)).get === Nil
        inputMolecule(Set(str1, str2), Set(str5)).get === List(
          (str2, Set(str2, str3, str4))
        )

        inputMolecule(Set(str1, str2), Set(str2, str3)).get === List(
          (str3, Set(str3, str4, str5))
        )
        inputMolecule(Set(str1, str2), Set(str4, str5)).get === List(
          (str2, Set(str2, str3, str4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.str.strs.>(?))

        inputMolecule(Nil).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))
        inputMolecule(List(Set[String]())).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        // (str3, str4), (str4, str5), (str4, str5, str6)
        inputMolecule(List(Set(str2))).get === List((str2, Set(str3)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.str.strs.>=(?))

        inputMolecule(Nil).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))
        inputMolecule(List(Set[String]())).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        // (str2, str4), (str3, str4), (str4, str5), (str4, str5, str6)
        inputMolecule(List(Set(str2))).get === List((str1, Set(str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.str.strs.<(?))

        inputMolecule(Nil).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))
        inputMolecule(List(Set[String]())).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        inputMolecule(List(Set(str2))).get === List((str1, Set(str1)))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.str.strs.<=(?))

        inputMolecule(Nil).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))
        inputMolecule(List(Set[String]())).get === List((str1, Set(str1, str2)), (str2, Set(str3, str2)), (str3, Set(str4, str3)), (str4, Set(str4, str5)), (str5, Set(str4, str6, str5)))

        inputMolecule(List(Set(str2))).get === List((str1, Set(str1, str2)), (str2, Set(str2)))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.strs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[String]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(str1))).get === List(Set(str1, str2))
        inputMolecule(List(Set(str2))).get === List(Set(str1, str2, str3)) // (str1, str2) + (str2, str3)
        inputMolecule(List(Set(str3))).get === List(Set(str2, str3, str4)) // (str2, str3) + (str3, str4)

        inputMolecule(List(Set(str1, str2))).get === List(Set(str1, str2))
        inputMolecule(List(Set(str1, str3))).get === Nil
        inputMolecule(List(Set(str2, str3))).get === List(Set(str2, str3))
        inputMolecule(List(Set(str4, str5))).get === List(Set(str4, str5, str6)) // (str4, str5) + (str4, str5, str6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(str1), Set(str1))).get === List(Set(str1, str2))
        inputMolecule(List(Set(str1), Set(str2))).get === List(Set(str1, str2, str3)) // (str1, str2) + (str2, str3)
        inputMolecule(List(Set(str1), Set(str3))).get === List(Set(str1, str4, str3, str2)) // (str1, str2) + (str2, str3) + (str3, str4)
        inputMolecule(List(Set(str2), Set(str3))).get === List(Set(str1, str2, str3, str4)) // (str1, str2) + (str2, str3) + (str3, str4)

        inputMolecule(List(Set(str1, str2), Set(str3))).get === List(Set(str1, str2, str3, str4)) // (str1, str2) + (str2, str3) + (str3, str4)
        inputMolecule(List(Set(str1), Set(str2, str3))).get === List(Set(str1, str3, str2)) // (str1, str2) + (str2, str3)
        inputMolecule(List(Set(str1), Set(str2), Set(str3))).get === List(Set(str1, str2, str3, str4)) // (str1, str2) + (str2, str3) + (str3, str4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (str1, Set(str1, str2, str3)),
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )
        Ns.str.strs insert all
        val inputMolecule = m(Ns.strs.not(?)) // or m(Ns.str.strs.!=(?))

        inputMolecule(Nil).get === List(Set(str1, str2, str3, str4, str5))
        inputMolecule(Set[String]()).get === List(Set(str1, str2, str3, str4, str5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(str1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(str1)).get === List(Set(str2, str3, str4, str5))
        // Same as
        inputMolecule(List(Set(str1))).get === List(Set(str2, str3, str4, str5))

        inputMolecule(Set(str2)).get === List(Set(str3, str4, str5))
        inputMolecule(Set(str3)).get === Nil // str3 match all

        inputMolecule(Set(str1), Set(str2)).get === List(Set(str3, str4, str5))
        inputMolecule(Set(str1), Set(str3)).get === Nil // str3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(str1, str2)).get === List(Set(str2, str3, str4, str5))
        inputMolecule(Set(str1, str3)).get === List(Set(str2, str3, str4, str5))

        inputMolecule(Set(str1), Set(str2), Set(str3)).get === Nil // str3 match all
        inputMolecule(Set(str1, str2, str3)).get === List(Set(str2, str3, str4, str5))

        inputMolecule(Set(str1, str2), Set(str1)).get === List(Set(str2, str3, str4, str5))
        inputMolecule(Set(str1, str2), Set(str2)).get === List(Set(str3, str4, str5))
        inputMolecule(Set(str1, str2), Set(str3)).get === Nil
        inputMolecule(Set(str1, str2), Set(str4)).get === Nil
        inputMolecule(Set(str1, str2), Set(str5)).get === List(Set(str2, str3, str4))

        inputMolecule(Set(str1, str2), Set(str2, str3)).get === List(Set(str3, str4, str5))
        inputMolecule(Set(str1, str2), Set(str4, str5)).get === List(Set(str2, str3, str4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.strs.>(?))

        inputMolecule(Nil).get === List(Set(str1, str2, str3, str4, str5, str6))
        inputMolecule(List(Set[String]())).get === List(Set(str1, str2, str3, str4, str5, str6))

        inputMolecule(List(Set(str2))).get === List(Set(str3, str4, str5, str6))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.strs.>=(?))

        inputMolecule(Nil).get === List(Set(str1, str2, str3, str4, str5, str6))
        inputMolecule(List(Set[String]())).get === List(Set(str1, str2, str3, str4, str5, str6))

        inputMolecule(List(Set(str2))).get === List(Set(str2, str3, str4, str5, str6))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.strs.<(?))

        inputMolecule(Nil).get === List(Set(str1, str2, str3, str4, str5, str6))
        inputMolecule(List(Set[String]())).get === List(Set(str1, str2, str3, str4, str5, str6))

        inputMolecule(List(Set(str2))).get === List(Set(str1))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.strs.<=(?))

        inputMolecule(Nil).get === List(Set(str1, str2, str3, str4, str5, str6))
        inputMolecule(List(Set[String]())).get === List(Set(str1, str2, str3, str4, str5, str6))

        inputMolecule(List(Set(str2))).get === List(Set(str1, str2))

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.str.strs_(?))

        inputMolecule(Nil).get === List(str6)
        inputMolecule(List(Set[String]())).get === List(str6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(str1))).get === List(str1)
        inputMolecule(List(Set(str2))).get.sorted === List(str1, str2)
        inputMolecule(List(Set(str3))).get.sorted === List(str2, str3)

        inputMolecule(List(Set(str1, str1))).get === List(str1)
        inputMolecule(List(Set(str1, str2))).get === List(str1)
        inputMolecule(List(Set(str1, str3))).get === Nil
        inputMolecule(List(Set(str2, str3))).get === List(str2)
        inputMolecule(List(Set(str4, str5))).get.sorted === List(str4, str5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(str1, str2), Set[String]())).get === List(str1)
        inputMolecule(List(Set(str1), Set(str1))).get === List(str1)
        inputMolecule(List(Set(str1), Set(str2))).get.sorted === List(str1, str2)
        inputMolecule(List(Set(str1), Set(str3))).get.sorted === List(str1, str2, str3)

        inputMolecule(List(Set(str1, str2), Set(str3))).get.sorted === List(str1, str2, str3)
        inputMolecule(List(Set(str1), Set(str2, str3))).get.sorted === List(str1, str2)
        inputMolecule(List(Set(str1), Set(str2), Set(str3))).get.sorted === List(str1, str2, str3)

        inputMolecule(List(Set(str1, str2), Set(str3, str4))).get.sorted === List(str1, str3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (str1, Set(str1, str2, str3)),
          (str2, Set(str2, str3, str4)),
          (str3, Set(str3, str4, str5))
        )
        Ns.str.strs insert all
        val inputMolecule = m(Ns.str.strs_.not(?)) // or m(Ns.str.strs.!=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(Set[String]()).get.sorted === List(str1, str2, str3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(str1).get === ...
        // inputMolecule(List(str1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(str1)).get.sorted === List(str2, str3)
        // Same as
        inputMolecule(List(Set(str1))).get.sorted === List(str2, str3)

        inputMolecule(Set(str2)).get === List(str3)
        inputMolecule(Set(str3)).get === Nil // str3 match all


        inputMolecule(Set(str1), Set(str2)).get === List(str3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(str1, str2)).get.sorted === List(str2, str3)

        inputMolecule(Set(str1), Set(str3)).get === Nil // str3 match all
        inputMolecule(Set(str1, str3)).get.sorted === List(str2, str3)

        inputMolecule(Set(str1), Set(str2), Set(str3)).get === Nil // str3 match all
        inputMolecule(Set(str1, str2, str3)).get.sorted === List(str2, str3)

        inputMolecule(Set(str1, str2), Set(str1)).get.sorted === List(str2, str3)
        inputMolecule(Set(str1, str2), Set(str2)).get === List(str3)
        inputMolecule(Set(str1, str2), Set(str3)).get === Nil
        inputMolecule(Set(str1, str2), Set(str4)).get === Nil
        inputMolecule(Set(str1, str2), Set(str5)).get === List(str2)

        inputMolecule(Set(str1, str2), Set(str2, str3)).get === List(str3)
        inputMolecule(Set(str1, str2), Set(str4, str5)).get === List(str2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.str.strs_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3, str4, str5)
        inputMolecule(List(Set[String]())).get.sorted === List(str1, str2, str3, str4, str5)

        // (str3, str4), (str4, str5), (str4, str5, str6)
        inputMolecule(List(Set(str2))).get.sorted === List(str2, str3, str4, str5)

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.str.strs_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3, str4, str5)
        inputMolecule(List(Set[String]())).get.sorted === List(str1, str2, str3, str4, str5)

        // (str2, str4), (str3, str4), (str4, str5), (str4, str5, str6)
        inputMolecule(List(Set(str2))).get.sorted === List(str1, str2, str3, str4, str5)

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.str.strs_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3, str4, str5)
        inputMolecule(List(Set[String]())).get.sorted === List(str1, str2, str3, str4, str5)

        inputMolecule(List(Set(str2))).get === List(str1)

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.str.strs_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3, str4, str5)
        inputMolecule(List(Set[String]())).get.sorted === List(str1, str2, str3, str4, str5)

        inputMolecule(List(Set(str2))).get.sorted === List(str1, str2)

        (inputMolecule(List(Set(str2, str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(str2), Set(str3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}