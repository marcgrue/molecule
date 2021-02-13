package molecule.tests.core.input1

import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.setup.TestSpec


class Input1BigInt extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.bigInt$ insert List(
        (str1, Some(bigInt1)),
        (str2, Some(bigInt2)),
        (str3, Some(bigInt3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.bigInt(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(bigInt1)).get === List(bigInt1)
        inputMolecule(List(bigInt1, bigInt1)).get === List(bigInt1)
        inputMolecule(List(bigInt1, bigInt2)).get.sorted === List(bigInt1, bigInt2)

        // Varargs
        inputMolecule(bigInt1).get === List(bigInt1)
        inputMolecule(bigInt1, bigInt2).get.sorted === List(bigInt1, bigInt2)

        // `or`
        inputMolecule(bigInt1 or bigInt2).get.sorted === List(bigInt1, bigInt2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.bigInt.not(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)

        inputMolecule(List(bigInt1)).get.sorted === List(bigInt2, bigInt3)
        inputMolecule(List(bigInt1, bigInt1)).get.sorted === List(bigInt2, bigInt3)
        inputMolecule(List(bigInt1, bigInt2)).get.sorted === List(bigInt3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.bigInt.>(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(List(bigInt2)).get.sorted === List(bigInt3)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.bigInt.>=(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(List(bigInt2)).get.sorted === List(bigInt2, bigInt3)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.bigInt.<(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(List(bigInt2)).get === List(bigInt1)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.bigInt.<=(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(List(bigInt2)).get.sorted === List(bigInt1, bigInt2)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(bigInt1)).get === List(str1)
        inputMolecule(List(bigInt1, bigInt1)).get === List(str1)
        inputMolecule(List(bigInt1, bigInt2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigInt1)).get.sorted === List(str2, str3)
        inputMolecule(List(bigInt1, bigInt1)).get.sorted === List(str2, str3)
        inputMolecule(List(bigInt1, bigInt2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigInt2)).get.sorted === List(str3)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigInt2)).get.sorted === List(str2, str3)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigInt2)).get === List(str1)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigInt_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigInt2)).get.sorted === List(str1, str2)
        (inputMolecule(List(bigInt2, bigInt3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.bigInt.bigInts$ insert List(
        (bigInt1, Some(Set(bigInt1, bigInt2))),
        (bigInt2, Some(Set(bigInt2, bigInt3))),
        (bigInt3, Some(Set(bigInt3, bigInt4))),
        (bigInt4, Some(Set(bigInt4, bigInt5))),
        (bigInt5, Some(Set(bigInt4, bigInt5, bigInt6))),
        (bigInt6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[BigInt]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigInt1))).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(List(Set(bigInt1, bigInt1))).get === List((bigInt1, Set(bigInt1, bigInt2)))

        inputMolecule(List(Set(bigInt1, bigInt2))).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(List(Set(bigInt1, bigInt3))).get === Nil
        inputMolecule(List(Set(bigInt2, bigInt3))).get === List((bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(List(Set(bigInt4, bigInt5))).get === List((bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        // 1 arg
        inputMolecule(Set(bigInt1)).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1, bigInt1)).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1, bigInt2)).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1, bigInt3)).get === Nil
        inputMolecule(Set(bigInt2, bigInt3)).get === List((bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(Set(bigInt4, bigInt5)).get === List((bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigInt1, bigInt2), Set[BigInt]())).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(List(Set(bigInt1), Set(bigInt1))).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(List(Set(bigInt1), Set(bigInt2))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(List(Set(bigInt1), Set(bigInt3))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(List(Set(bigInt1, bigInt2), Set(bigInt3))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(List(Set(bigInt1), Set(bigInt2, bigInt3))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(List(Set(bigInt1), Set(bigInt2), Set(bigInt3))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(List(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))


        // Multiple varargs
        inputMolecule(Set(bigInt1, bigInt2), Set[BigInt]()).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1), Set(bigInt1)).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1), Set(bigInt2)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(Set(bigInt1), Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1), Set(bigInt2, bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(Set(bigInt1), Set(bigInt2), Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))

        // `or`
        inputMolecule(Set(bigInt1, bigInt2) or Set[BigInt]()).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1) or Set(bigInt1)).get === List((bigInt1, Set(bigInt1, bigInt2)))
        inputMolecule(Set(bigInt1) or Set(bigInt2)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(Set(bigInt1) or Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1, bigInt2) or Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1) or Set(bigInt2, bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)))
        inputMolecule(Set(bigInt1) or Set(bigInt2) or Set(bigInt3)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
        inputMolecule(Set(bigInt1, bigInt2) or Set(bigInt3, bigInt4)).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigInt1, Set(bigInt1, bigInt2, bigInt3)),
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        Ns.bigInt.bigInts insert all
        val inputMolecule = m(Ns.bigInt.bigInts.not(?)) // or m(Ns.bigInt.bigInts.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[BigInt]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigInt1).get === ...
        // inputMolecule(List(bigInt1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(bigInt1)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt1 match
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        // Same as
        inputMolecule(List(Set(bigInt1))).get === List(
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )

        inputMolecule(Set(bigInt2)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt2 match
          // (bigInt2, Set(bigInt2, bigInt3, bigInt4)),  // bigInt2 match
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )

        inputMolecule(Set(bigInt3)).get === Nil // bigInt3 match all


        inputMolecule(Set(bigInt1), Set(bigInt2)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt1 match, bigInt2 match
          // (bigInt2, Set(bigInt2, bigInt3, bigInt4)),  // bigInt2 match
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigInt1, bigInt2)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt1 AND bigInt2 match
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )


        inputMolecule(Set(bigInt1), Set(bigInt3)).get === Nil // bigInt3 match all
        inputMolecule(Set(bigInt1, bigInt3)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt1 AND bigInt3 match
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )


        inputMolecule(Set(bigInt1), Set(bigInt2), Set(bigInt3)).get === Nil // bigInt3 match all
        inputMolecule(Set(bigInt1, bigInt2, bigInt3)).get === List(
          // (bigInt1, Set(bigInt1, bigInt2, bigInt3)),  // bigInt1 AND bigInt2 AND bigInt3 match
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )


        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt1)).get === List(
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2)).get === List(
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt3)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt5)).get === List(
          (bigInt2, Set(bigInt2, bigInt3, bigInt4))
        )

        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4, bigInt5)).get === List(
          (bigInt2, Set(bigInt2, bigInt3, bigInt4))
        )
      }

      
      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts.>(?))

        inputMolecule(Nil).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))
        inputMolecule(List(Set[BigInt]())).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        // (bigInt3, bigInt4), (bigInt4, bigInt5), (bigInt4, bigInt5, bigInt6)
        inputMolecule(List(Set(bigInt2))).get === List((bigInt2, Set(bigInt3)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts.>=(?))

        inputMolecule(Nil).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))
        inputMolecule(List(Set[BigInt]())).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        // (bigInt2, bigInt4), (bigInt3, bigInt4), (bigInt4, bigInt5), (bigInt4, bigInt5, bigInt6)
        inputMolecule(List(Set(bigInt2))).get === List((bigInt1, Set(bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts.<(?))

        inputMolecule(Nil).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))
        inputMolecule(List(Set[BigInt]())).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        inputMolecule(List(Set(bigInt2))).get === List((bigInt1, Set(bigInt1)))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts.<=(?))

        inputMolecule(Nil).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))
        inputMolecule(List(Set[BigInt]())).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt3, bigInt2)), (bigInt3, Set(bigInt4, bigInt3)), (bigInt4, Set(bigInt4, bigInt5)), (bigInt5, Set(bigInt4, bigInt6, bigInt5)))

        inputMolecule(List(Set(bigInt2))).get === List((bigInt1, Set(bigInt1, bigInt2)), (bigInt2, Set(bigInt2)))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigInts(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[BigInt]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigInt1))).get === List(Set(bigInt1, bigInt2))
        inputMolecule(List(Set(bigInt2))).get === List(Set(bigInt1, bigInt2, bigInt3)) // (bigInt1, bigInt2) + (bigInt2, bigInt3)
        inputMolecule(List(Set(bigInt3))).get === List(Set(bigInt2, bigInt3, bigInt4)) // (bigInt2, bigInt3) + (bigInt3, bigInt4)

        inputMolecule(List(Set(bigInt1, bigInt2))).get === List(Set(bigInt1, bigInt2))
        inputMolecule(List(Set(bigInt1, bigInt3))).get === Nil
        inputMolecule(List(Set(bigInt2, bigInt3))).get === List(Set(bigInt2, bigInt3))
        inputMolecule(List(Set(bigInt4, bigInt5))).get === List(Set(bigInt4, bigInt5, bigInt6)) // (bigInt4, bigInt5) + (bigInt4, bigInt5, bigInt6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigInt1), Set(bigInt1))).get === List(Set(bigInt1, bigInt2))
        inputMolecule(List(Set(bigInt1), Set(bigInt2))).get === List(Set(bigInt1, bigInt2, bigInt3)) // (bigInt1, bigInt2) + (bigInt2, bigInt3)
        inputMolecule(List(Set(bigInt1), Set(bigInt3))).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2)) // (bigInt1, bigInt2) + (bigInt2, bigInt3) + (bigInt3, bigInt4)
        inputMolecule(List(Set(bigInt2), Set(bigInt3))).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4)) // (bigInt1, bigInt2) + (bigInt2, bigInt3) + (bigInt3, bigInt4)

        inputMolecule(List(Set(bigInt1, bigInt2), Set(bigInt3))).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4)) // (bigInt1, bigInt2) + (bigInt2, bigInt3) + (bigInt3, bigInt4)
        inputMolecule(List(Set(bigInt1), Set(bigInt2, bigInt3))).get === List(Set(bigInt1, bigInt3, bigInt2)) // (bigInt1, bigInt2) + (bigInt2, bigInt3)
        inputMolecule(List(Set(bigInt1), Set(bigInt2), Set(bigInt3))).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4)) // (bigInt1, bigInt2) + (bigInt2, bigInt3) + (bigInt3, bigInt4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigInt1, Set(bigInt1, bigInt2, bigInt3)),
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        Ns.bigInt.bigInts insert all
        val inputMolecule = m(Ns.bigInts.not(?)) // or m(Ns.bigInt.bigInts.!=(?))

        inputMolecule(Nil).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5))
        inputMolecule(Set[BigInt]()).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigInt1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(bigInt1)).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))
        // Same as
        inputMolecule(List(Set(bigInt1))).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))

        inputMolecule(Set(bigInt2)).get === List(Set(bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt3)).get === Nil // bigInt3 match all

        inputMolecule(Set(bigInt1), Set(bigInt2)).get === List(Set(bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt1), Set(bigInt3)).get === Nil // bigInt3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigInt1, bigInt2)).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt1, bigInt3)).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))

        inputMolecule(Set(bigInt1), Set(bigInt2), Set(bigInt3)).get === Nil // bigInt3 match all
        inputMolecule(Set(bigInt1, bigInt2, bigInt3)).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))

        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt1)).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2)).get === List(Set(bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt3)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt5)).get === List(Set(bigInt2, bigInt3, bigInt4))

        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(Set(bigInt3, bigInt4, bigInt5))
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4, bigInt5)).get === List(Set(bigInt2, bigInt3, bigInt4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigInts.>(?))

        inputMolecule(Nil).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))
        inputMolecule(List(Set[BigInt]())).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))

        inputMolecule(List(Set(bigInt2))).get === List(Set(bigInt3, bigInt4, bigInt5, bigInt6))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigInts.>=(?))

        inputMolecule(Nil).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))
        inputMolecule(List(Set[BigInt]())).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))

        inputMolecule(List(Set(bigInt2))).get === List(Set(bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigInts.<(?))

        inputMolecule(Nil).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))
        inputMolecule(List(Set[BigInt]())).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))

        inputMolecule(List(Set(bigInt2))).get === List(Set(bigInt1))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigInts.<=(?))

        inputMolecule(Nil).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))
        inputMolecule(List(Set[BigInt]())).get === List(Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6))

        inputMolecule(List(Set(bigInt2))).get === List(Set(bigInt1, bigInt2))

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts_(?))

        inputMolecule(Nil).get === List(bigInt6)
        inputMolecule(List(Set[BigInt]())).get === List(bigInt6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigInt1))).get === List(bigInt1)
        inputMolecule(List(Set(bigInt2))).get.sorted === List(bigInt1, bigInt2)
        inputMolecule(List(Set(bigInt3))).get.sorted === List(bigInt2, bigInt3)

        inputMolecule(List(Set(bigInt1, bigInt1))).get === List(bigInt1)
        inputMolecule(List(Set(bigInt1, bigInt2))).get === List(bigInt1)
        inputMolecule(List(Set(bigInt1, bigInt3))).get === Nil
        inputMolecule(List(Set(bigInt2, bigInt3))).get === List(bigInt2)
        inputMolecule(List(Set(bigInt4, bigInt5))).get.sorted === List(bigInt4, bigInt5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigInt1, bigInt2), Set[BigInt]())).get === List(bigInt1)
        inputMolecule(List(Set(bigInt1), Set(bigInt1))).get === List(bigInt1)
        inputMolecule(List(Set(bigInt1), Set(bigInt2))).get.sorted === List(bigInt1, bigInt2)
        inputMolecule(List(Set(bigInt1), Set(bigInt3))).get.sorted === List(bigInt1, bigInt2, bigInt3)

        inputMolecule(List(Set(bigInt1, bigInt2), Set(bigInt3))).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(List(Set(bigInt1), Set(bigInt2, bigInt3))).get.sorted === List(bigInt1, bigInt2)
        inputMolecule(List(Set(bigInt1), Set(bigInt2), Set(bigInt3))).get.sorted === List(bigInt1, bigInt2, bigInt3)

        inputMolecule(List(Set(bigInt1, bigInt2), Set(bigInt3, bigInt4))).get.sorted === List(bigInt1, bigInt3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigInt1, Set(bigInt1, bigInt2, bigInt3)),
          (bigInt2, Set(bigInt2, bigInt3, bigInt4)),
          (bigInt3, Set(bigInt3, bigInt4, bigInt5))
        )
        Ns.bigInt.bigInts insert all
        val inputMolecule = m(Ns.bigInt.bigInts_.not(?)) // or m(Ns.bigInt.bigInts.!=(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3)
        inputMolecule(Set[BigInt]()).get.sorted === List(bigInt1, bigInt2, bigInt3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigInt1).get === ...
        // inputMolecule(List(bigInt1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(bigInt1)).get.sorted === List(bigInt2, bigInt3)
        // Same as
        inputMolecule(List(Set(bigInt1))).get.sorted === List(bigInt2, bigInt3)

        inputMolecule(Set(bigInt2)).get === List(bigInt3)
        inputMolecule(Set(bigInt3)).get === Nil // bigInt3 match all


        inputMolecule(Set(bigInt1), Set(bigInt2)).get === List(bigInt3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigInt1, bigInt2)).get.sorted === List(bigInt2, bigInt3)

        inputMolecule(Set(bigInt1), Set(bigInt3)).get === Nil // bigInt3 match all
        inputMolecule(Set(bigInt1, bigInt3)).get.sorted === List(bigInt2, bigInt3)

        inputMolecule(Set(bigInt1), Set(bigInt2), Set(bigInt3)).get === Nil // bigInt3 match all
        inputMolecule(Set(bigInt1, bigInt2, bigInt3)).get.sorted === List(bigInt2, bigInt3)

        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt1)).get.sorted === List(bigInt2, bigInt3)
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2)).get === List(bigInt3)
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt3)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4)).get === Nil
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt5)).get === List(bigInt2)

        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt2, bigInt3)).get === List(bigInt3)
        inputMolecule(Set(bigInt1, bigInt2), Set(bigInt4, bigInt5)).get === List(bigInt2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts_.>(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)
        inputMolecule(List(Set[BigInt]())).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

        // (bigInt3, bigInt4), (bigInt4, bigInt5), (bigInt4, bigInt5, bigInt6)
        inputMolecule(List(Set(bigInt2))).get.sorted === List(bigInt2, bigInt3, bigInt4, bigInt5)

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts_.>=(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)
        inputMolecule(List(Set[BigInt]())).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

        // (bigInt2, bigInt4), (bigInt3, bigInt4), (bigInt4, bigInt5), (bigInt4, bigInt5, bigInt6)
        inputMolecule(List(Set(bigInt2))).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts_.<(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)
        inputMolecule(List(Set[BigInt]())).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

        inputMolecule(List(Set(bigInt2))).get === List(bigInt1)

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigInt.bigInts_.<=(?))

        inputMolecule(Nil).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)
        inputMolecule(List(Set[BigInt]())).get.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

        inputMolecule(List(Set(bigInt2))).get.sorted === List(bigInt1, bigInt2)

        (inputMolecule(List(Set(bigInt2, bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigInt2), Set(bigInt3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}