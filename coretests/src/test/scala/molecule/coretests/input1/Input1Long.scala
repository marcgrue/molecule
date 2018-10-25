package molecule.coretests.input1

import molecule.api.in1_out2._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.exception.InputMoleculeException


class Input1Long extends CoreSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.long$ insert List(
        (str1, Some(long1)),
        (str2, Some(long2)),
        (str3, Some(long3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.long(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(long1)).get === List(long1)
        inputMolecule(List(long1, long1)).get === List(long1)
        inputMolecule(List(long1, long2)).get === List(long1, long2)

        // Varargs
        inputMolecule(long1).get === List(long1)
        inputMolecule(long1, long2).get === List(long1, long2)

        // `or`
        inputMolecule(long1 or long2).get === List(long1, long2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.long.not(?))

        inputMolecule(Nil).get === List(long1, long2, long3)

        inputMolecule(List(long1)).get === List(long2, long3)
        inputMolecule(List(long1, long1)).get === List(long2, long3)
        inputMolecule(List(long1, long2)).get === List(long3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.long.>(?))

        inputMolecule(Nil).get === List(long1, long2, long3)
        inputMolecule(List(long2)).get === List(long3)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.long.>=(?))

        inputMolecule(Nil).get === List(long1, long2, long3)
        inputMolecule(List(long2)).get === List(long2, long3)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.long.<(?))

        inputMolecule(Nil).get === List(long1, long2, long3)
        inputMolecule(List(long2)).get === List(long1)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.long.<=(?))

        inputMolecule(Nil).get === List(long1, long2, long3)
        inputMolecule(List(long2)).get === List(long1, long2)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.long_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(long1)).get === List(str1)
        inputMolecule(List(long1, long1)).get === List(str1)
        inputMolecule(List(long1, long2)).get === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.long_.not(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(long1)).get === List(str2, str3)
        inputMolecule(List(long1, long1)).get === List(str2, str3)
        inputMolecule(List(long1, long2)).get === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.long_.>(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(long2)).get === List(str3)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.long_.>=(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(long2)).get === List(str2, str3)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.long_.<(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(long2)).get === List(str1)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.long_.<=(?))

        inputMolecule(Nil).get === List(str1, str2, str3)
        inputMolecule(List(long2)).get === List(str1, str2)
        (inputMolecule(List(long2, long3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.long.longs$ insert List(
        (long1, Some(Set(long1, long2))),
        (long2, Some(Set(long2, long3))),
        (long3, Some(Set(long3, long4))),
        (long4, Some(Set(long4, long5))),
        (long5, Some(Set(long4, long5, long6))),
        (long6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.long.longs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Long]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(long1))).get === List((long1, Set(long1, long2)))
        inputMolecule(List(Set(long1, long1))).get === List((long1, Set(long1, long2)))

        inputMolecule(List(Set(long1, long2))).get === List((long1, Set(long1, long2)))
        inputMolecule(List(Set(long1, long3))).get === Nil
        inputMolecule(List(Set(long2, long3))).get === List((long2, Set(long3, long2)))
        inputMolecule(List(Set(long4, long5))).get === List((long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        // 1 arg
        inputMolecule(Set(long1)).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1, long1)).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1, long2)).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1, long3)).get === Nil
        inputMolecule(Set(long2, long3)).get === List((long2, Set(long3, long2)))
        inputMolecule(Set(long4, long5)).get === List((long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(long1, long2), Set[Long]())).get === List((long1, Set(long1, long2)))
        inputMolecule(List(Set(long1), Set(long1))).get === List((long1, Set(long1, long2)))
        inputMolecule(List(Set(long1), Set(long2))).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(List(Set(long1), Set(long3))).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(List(Set(long1, long2), Set(long3))).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(List(Set(long1), Set(long2, long3))).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(List(Set(long1), Set(long2), Set(long3))).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(List(Set(long1, long2), Set(long3, long4))).get === List((long1, Set(long1, long2)), (long3, Set(long4, long3)))


        // Multiple varargs
        inputMolecule(Set(long1, long2), Set[Long]()).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1), Set(long1)).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1), Set(long2)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(Set(long1), Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1, long2), Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1), Set(long2, long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(Set(long1), Set(long2), Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1, long2), Set(long3, long4)).get === List((long1, Set(long1, long2)), (long3, Set(long4, long3)))

        // `or`
        inputMolecule(Set(long1, long2) or Set[Long]()).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1) or Set(long1)).get === List((long1, Set(long1, long2)))
        inputMolecule(Set(long1) or Set(long2)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(Set(long1) or Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1, long2) or Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1) or Set(long2, long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)))
        inputMolecule(Set(long1) or Set(long2) or Set(long3)).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)))
        inputMolecule(Set(long1, long2) or Set(long3, long4)).get === List((long1, Set(long1, long2)), (long3, Set(long4, long3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (long1, Set(long1, long2, long3)),
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )
        Ns.long.longs insert all
        val inputMolecule = m(Ns.long.longs.not(?)) // or m(Ns.long.longs.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[Long]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(long1).get === ...
        // inputMolecule(List(long1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(long1)).get === List(
          // (long1, Set(long1, long2, long3)),  // long1 match
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )
        // Same as
        inputMolecule(List(Set(long1))).get === List(
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )

        inputMolecule(Set(long2)).get === List(
          // (long1, Set(long1, long2, long3)),  // long2 match
          // (long2, Set(long2, long3, long4)),  // long2 match
          (long3, Set(long3, long4, long5))
        )

        inputMolecule(Set(long3)).get === Nil // long3 match all


        inputMolecule(Set(long1), Set(long2)).get === List(
          // (long1, Set(long1, long2, long3)),  // long1 match, long2 match
          // (long2, Set(long2, long3, long4)),  // long2 match
          (long3, Set(long3, long4, long5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(long1, long2)).get === List(
          // (long1, Set(long1, long2, long3)),  // long1 AND long2 match
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )


        inputMolecule(Set(long1), Set(long3)).get === Nil // long3 match all
        inputMolecule(Set(long1, long3)).get === List(
          // (long1, Set(long1, long2, long3)),  // long1 AND long3 match
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )


        inputMolecule(Set(long1), Set(long2), Set(long3)).get === Nil // long3 match all
        inputMolecule(Set(long1, long2, long3)).get === List(
          // (long1, Set(long1, long2, long3)),  // long1 AND long2 AND long3 match
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )


        inputMolecule(Set(long1, long2), Set(long1)).get === List(
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )
        inputMolecule(Set(long1, long2), Set(long2)).get === List(
          (long3, Set(long3, long4, long5))
        )
        inputMolecule(Set(long1, long2), Set(long3)).get === Nil
        inputMolecule(Set(long1, long2), Set(long4)).get === Nil
        inputMolecule(Set(long1, long2), Set(long5)).get === List(
          (long2, Set(long2, long3, long4))
        )

        inputMolecule(Set(long1, long2), Set(long2, long3)).get === List(
          (long3, Set(long3, long4, long5))
        )
        inputMolecule(Set(long1, long2), Set(long4, long5)).get === List(
          (long2, Set(long2, long3, long4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.long.longs.>(?))

        inputMolecule(Nil).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))
        inputMolecule(List(Set[Long]())).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        // (long3, long4), (long4, long5), (long4, long5, long6)
        inputMolecule(List(Set(long2))).get === List((long2, Set(long3)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.long.longs.>=(?))

        inputMolecule(Nil).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))
        inputMolecule(List(Set[Long]())).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        // (long2, long4), (long3, long4), (long4, long5), (long4, long5, long6)
        inputMolecule(List(Set(long2))).get === List((long1, Set(long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.long.longs.<(?))

        inputMolecule(Nil).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))
        inputMolecule(List(Set[Long]())).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        inputMolecule(List(Set(long2))).get === List((long1, Set(long1)))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.long.longs.<=(?))

        inputMolecule(Nil).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))
        inputMolecule(List(Set[Long]())).get === List((long1, Set(long1, long2)), (long2, Set(long3, long2)), (long3, Set(long4, long3)), (long4, Set(long4, long5)), (long5, Set(long4, long6, long5)))

        inputMolecule(List(Set(long2))).get === List((long1, Set(long1, long2)), (long2, Set(long2)))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.longs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Long]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(long1))).get === List(Set(long1, long2))
        inputMolecule(List(Set(long2))).get === List(Set(long1, long2, long3)) // (long1, long2) + (long2, long3)
        inputMolecule(List(Set(long3))).get === List(Set(long2, long3, long4)) // (long2, long3) + (long3, long4)

        inputMolecule(List(Set(long1, long2))).get === List(Set(long1, long2))
        inputMolecule(List(Set(long1, long3))).get === Nil
        inputMolecule(List(Set(long2, long3))).get === List(Set(long2, long3))
        inputMolecule(List(Set(long4, long5))).get === List(Set(long4, long5, long6)) // (long4, long5) + (long4, long5, long6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(long1), Set(long1))).get === List(Set(long1, long2))
        inputMolecule(List(Set(long1), Set(long2))).get === List(Set(long1, long2, long3)) // (long1, long2) + (long2, long3)
        inputMolecule(List(Set(long1), Set(long3))).get === List(Set(long1, long4, long3, long2)) // (long1, long2) + (long2, long3) + (long3, long4)
        inputMolecule(List(Set(long2), Set(long3))).get === List(Set(long1, long2, long3, long4)) // (long1, long2) + (long2, long3) + (long3, long4)

        inputMolecule(List(Set(long1, long2), Set(long3))).get === List(Set(long1, long2, long3, long4)) // (long1, long2) + (long2, long3) + (long3, long4)
        inputMolecule(List(Set(long1), Set(long2, long3))).get === List(Set(long1, long3, long2)) // (long1, long2) + (long2, long3)
        inputMolecule(List(Set(long1), Set(long2), Set(long3))).get === List(Set(long1, long2, long3, long4)) // (long1, long2) + (long2, long3) + (long3, long4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (long1, Set(long1, long2, long3)),
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )
        Ns.long.longs insert all
        val inputMolecule = m(Ns.longs.not(?)) // or m(Ns.long.longs.!=(?))

        inputMolecule(Nil).get === List(Set(long1, long2, long3, long4, long5))
        inputMolecule(Set[Long]()).get === List(Set(long1, long2, long3, long4, long5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(long1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(long1)).get === List(Set(long2, long3, long4, long5))
        // Same as
        inputMolecule(List(Set(long1))).get === List(Set(long2, long3, long4, long5))

        inputMolecule(Set(long2)).get === List(Set(long3, long4, long5))
        inputMolecule(Set(long3)).get === Nil // long3 match all

        inputMolecule(Set(long1), Set(long2)).get === List(Set(long3, long4, long5))
        inputMolecule(Set(long1), Set(long3)).get === Nil // long3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(long1, long2)).get === List(Set(long2, long3, long4, long5))
        inputMolecule(Set(long1, long3)).get === List(Set(long2, long3, long4, long5))

        inputMolecule(Set(long1), Set(long2), Set(long3)).get === Nil // long3 match all
        inputMolecule(Set(long1, long2, long3)).get === List(Set(long2, long3, long4, long5))

        inputMolecule(Set(long1, long2), Set(long1)).get === List(Set(long2, long3, long4, long5))
        inputMolecule(Set(long1, long2), Set(long2)).get === List(Set(long3, long4, long5))
        inputMolecule(Set(long1, long2), Set(long3)).get === Nil
        inputMolecule(Set(long1, long2), Set(long4)).get === Nil
        inputMolecule(Set(long1, long2), Set(long5)).get === List(Set(long2, long3, long4))

        inputMolecule(Set(long1, long2), Set(long2, long3)).get === List(Set(long3, long4, long5))
        inputMolecule(Set(long1, long2), Set(long4, long5)).get === List(Set(long2, long3, long4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.longs.>(?))

        inputMolecule(Nil).get === List(Set(long1, long2, long3, long4, long5, long6))
        inputMolecule(List(Set[Long]())).get === List(Set(long1, long2, long3, long4, long5, long6))

        inputMolecule(List(Set(long2))).get === List(Set(long3, long4, long5, long6))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.longs.>=(?))

        inputMolecule(Nil).get === List(Set(long1, long2, long3, long4, long5, long6))
        inputMolecule(List(Set[Long]())).get === List(Set(long1, long2, long3, long4, long5, long6))

        inputMolecule(List(Set(long2))).get === List(Set(long2, long3, long4, long5, long6))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.longs.<(?))

        inputMolecule(Nil).get === List(Set(long1, long2, long3, long4, long5, long6))
        inputMolecule(List(Set[Long]())).get === List(Set(long1, long2, long3, long4, long5, long6))

        inputMolecule(List(Set(long2))).get === List(Set(long1))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.longs.<=(?))

        inputMolecule(Nil).get === List(Set(long1, long2, long3, long4, long5, long6))
        inputMolecule(List(Set[Long]())).get === List(Set(long1, long2, long3, long4, long5, long6))

        inputMolecule(List(Set(long2))).get === List(Set(long1, long2))

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.long.longs_(?))

        inputMolecule(Nil).get === List(long6)
        inputMolecule(List(Set[Long]())).get === List(long6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(long1))).get === List(long1)
        inputMolecule(List(Set(long2))).get === List(long1, long2)
        inputMolecule(List(Set(long3))).get === List(long2, long3)

        inputMolecule(List(Set(long1, long1))).get === List(long1)
        inputMolecule(List(Set(long1, long2))).get === List(long1)
        inputMolecule(List(Set(long1, long3))).get === Nil
        inputMolecule(List(Set(long2, long3))).get === List(long2)
        inputMolecule(List(Set(long4, long5))).get === List(long4, long5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(long1, long2), Set[Long]())).get === List(long1)
        inputMolecule(List(Set(long1), Set(long1))).get === List(long1)
        inputMolecule(List(Set(long1), Set(long2))).get === List(long1, long2)
        inputMolecule(List(Set(long1), Set(long3))).get === List(long1, long2, long3)

        inputMolecule(List(Set(long1, long2), Set(long3))).get === List(long1, long2, long3)
        inputMolecule(List(Set(long1), Set(long2, long3))).get === List(long1, long2)
        inputMolecule(List(Set(long1), Set(long2), Set(long3))).get === List(long1, long2, long3)

        inputMolecule(List(Set(long1, long2), Set(long3, long4))).get === List(long1, long3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (long1, Set(long1, long2, long3)),
          (long2, Set(long2, long3, long4)),
          (long3, Set(long3, long4, long5))
        )
        Ns.long.longs insert all
        val inputMolecule = m(Ns.long.longs_.not(?)) // or m(Ns.long.longs.!=(?))

        inputMolecule(Nil).get.sorted === List(long1, long2, long3)
        inputMolecule(Set[Long]()).get.sorted === List(long1, long2, long3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(long1).get === ...
        // inputMolecule(List(long1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(long1)).get.sorted === List(long2, long3)
        // Same as
        inputMolecule(List(Set(long1))).get.sorted === List(long2, long3)

        inputMolecule(Set(long2)).get === List(long3)
        inputMolecule(Set(long3)).get === Nil // long3 match all


        inputMolecule(Set(long1), Set(long2)).get === List(long3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(long1, long2)).get.sorted === List(long2, long3)

        inputMolecule(Set(long1), Set(long3)).get === Nil // long3 match all
        inputMolecule(Set(long1, long3)).get.sorted === List(long2, long3)

        inputMolecule(Set(long1), Set(long2), Set(long3)).get === Nil // long3 match all
        inputMolecule(Set(long1, long2, long3)).get.sorted === List(long2, long3)

        inputMolecule(Set(long1, long2), Set(long1)).get.sorted === List(long2, long3)
        inputMolecule(Set(long1, long2), Set(long2)).get === List(long3)
        inputMolecule(Set(long1, long2), Set(long3)).get === Nil
        inputMolecule(Set(long1, long2), Set(long4)).get === Nil
        inputMolecule(Set(long1, long2), Set(long5)).get === List(long2)

        inputMolecule(Set(long1, long2), Set(long2, long3)).get === List(long3)
        inputMolecule(Set(long1, long2), Set(long4, long5)).get === List(long2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.long.longs_.>(?))

        inputMolecule(Nil).get === List(long1, long2, long3, long4, long5)
        inputMolecule(List(Set[Long]())).get === List(long1, long2, long3, long4, long5)

        // (long3, long4), (long4, long5), (long4, long5, long6)
        inputMolecule(List(Set(long2))).get === List(long2, long3, long4, long5)

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.long.longs_.>=(?))

        inputMolecule(Nil).get === List(long1, long2, long3, long4, long5)
        inputMolecule(List(Set[Long]())).get === List(long1, long2, long3, long4, long5)

        // (long2, long4), (long3, long4), (long4, long5), (long4, long5, long6)
        inputMolecule(List(Set(long2))).get === List(long1, long2, long3, long4, long5)

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.long.longs_.<(?))

        inputMolecule(Nil).get === List(long1, long2, long3, long4, long5)
        inputMolecule(List(Set[Long]())).get === List(long1, long2, long3, long4, long5)

        inputMolecule(List(Set(long2))).get === List(long1)

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.long.longs_.<=(?))

        inputMolecule(Nil).get === List(long1, long2, long3, long4, long5)
        inputMolecule(List(Set[Long]())).get === List(long1, long2, long3, long4, long5)

        inputMolecule(List(Set(long2))).get === List(long1, long2)

        (inputMolecule(List(Set(long2, long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(long2), Set(long3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}