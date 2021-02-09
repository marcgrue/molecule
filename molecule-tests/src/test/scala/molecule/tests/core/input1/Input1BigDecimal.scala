package molecule.tests.core.input1

import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.setup.TestSpec


class Input1BigDecimal extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.bigDec$ insert List(
        (str1, Some(bigDec1)),
        (str2, Some(bigDec2)),
        (str3, Some(bigDec3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.apply(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(bigDec1)).get === List(bigDec1)
        inputMolecule(List(bigDec1, bigDec1)).get === List(bigDec1)
        inputMolecule(List(bigDec1, bigDec2)).get.sorted === List(bigDec1, bigDec2)

        // Varargs
        inputMolecule(bigDec1).get === List(bigDec1)
        inputMolecule(bigDec1, bigDec2).get.sorted === List(bigDec1, bigDec2)

        // `or`
        inputMolecule(bigDec1 or bigDec2).get.sorted === List(bigDec1, bigDec2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.not(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)

        inputMolecule(List(bigDec1)).get.sorted === List(bigDec2, bigDec3)
        inputMolecule(List(bigDec1, bigDec1)).get.sorted === List(bigDec2, bigDec3)
        inputMolecule(List(bigDec1, bigDec2)).get.sorted === List(bigDec3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.>(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(List(bigDec2)).get.sorted === List(bigDec3)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.>=(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(List(bigDec2)).get.sorted === List(bigDec2, bigDec3)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.<(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(List(bigDec2)).get === List(bigDec1)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.bigDec.<=(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(List(bigDec2)).get.sorted === List(bigDec1, bigDec2)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(bigDec1)).get === List(str1)
        inputMolecule(List(bigDec1, bigDec1)).get === List(str1)
        inputMolecule(List(bigDec1, bigDec2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigDec1)).get.sorted === List(str2, str3)
        inputMolecule(List(bigDec1, bigDec1)).get.sorted === List(str2, str3)
        inputMolecule(List(bigDec1, bigDec2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigDec2)).get.sorted === List(str3)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigDec2)).get.sorted === List(str2, str3)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigDec2)).get === List(str1)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.bigDec_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(bigDec2)).get.sorted === List(str1, str2)
        (inputMolecule(List(bigDec2, bigDec3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.bigDec.bigDecs$ insert List(
        (bigDec1, Some(Set(bigDec1, bigDec2))),
        (bigDec2, Some(Set(bigDec2, bigDec3))),
        (bigDec3, Some(Set(bigDec3, bigDec4))),
        (bigDec4, Some(Set(bigDec4, bigDec5))),
        (bigDec5, Some(Set(bigDec4, bigDec5, bigDec6))),
        (bigDec6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[BigDecimal]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigDec1))).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(List(Set(bigDec1, bigDec1))).get === List((bigDec1, Set(bigDec1, bigDec2)))

        inputMolecule(List(Set(bigDec1, bigDec2))).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(List(Set(bigDec1, bigDec3))).get === Nil
        inputMolecule(List(Set(bigDec2, bigDec3))).get === List((bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(List(Set(bigDec4, bigDec5))).get === List((bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        // 1 arg
        inputMolecule(Set(bigDec1)).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1, bigDec1)).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1, bigDec2)).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1, bigDec3)).get === Nil
        inputMolecule(Set(bigDec2, bigDec3)).get === List((bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(Set(bigDec4, bigDec5)).get === List((bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigDec1, bigDec2), Set[BigDecimal]())).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(List(Set(bigDec1), Set(bigDec1))).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(List(Set(bigDec1), Set(bigDec2))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(List(Set(bigDec1), Set(bigDec3))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(List(Set(bigDec1, bigDec2), Set(bigDec3))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(List(Set(bigDec1), Set(bigDec2, bigDec3))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(List(Set(bigDec1), Set(bigDec2), Set(bigDec3))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(List(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))


        // Multiple varargs
        inputMolecule(Set(bigDec1, bigDec2), Set[BigDecimal]()).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1), Set(bigDec1)).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1), Set(bigDec2)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(Set(bigDec1), Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1), Set(bigDec2, bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(Set(bigDec1), Set(bigDec2), Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))

        // `or`
        inputMolecule(Set(bigDec1, bigDec2) or Set[BigDecimal]()).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1) or Set(bigDec1)).get === List((bigDec1, Set(bigDec1, bigDec2)))
        inputMolecule(Set(bigDec1) or Set(bigDec2)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(Set(bigDec1) or Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1, bigDec2) or Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1) or Set(bigDec2, bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)))
        inputMolecule(Set(bigDec1) or Set(bigDec2) or Set(bigDec3)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
        inputMolecule(Set(bigDec1, bigDec2) or Set(bigDec3, bigDec4)).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigDec1, Set(bigDec1, bigDec2, bigDec3)),
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        Ns.bigDec.bigDecs insert all
        val inputMolecule = m(Ns.bigDec.bigDecs.not(?)) // or m(Ns.bigDec.bigDecs.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[BigDecimal]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigDec1).get === ...
        // inputMolecule(List(bigDec1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(bigDec1)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec1 match
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        // Same as
        inputMolecule(List(Set(bigDec1))).get === List(
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )

        inputMolecule(Set(bigDec2)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec2 match
          // (bigDec2, Set(bigDec2, bigDec3, bigDec4)),  // bigDec2 match
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )

        inputMolecule(Set(bigDec3)).get === Nil // bigDec3 match all


        inputMolecule(Set(bigDec1), Set(bigDec2)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec1 match, bigDec2 match
          // (bigDec2, Set(bigDec2, bigDec3, bigDec4)),  // bigDec2 match
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigDec1, bigDec2)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec1 AND bigDec2 match
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )


        inputMolecule(Set(bigDec1), Set(bigDec3)).get === Nil // bigDec3 match all
        inputMolecule(Set(bigDec1, bigDec3)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec1 AND bigDec3 match
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )


        inputMolecule(Set(bigDec1), Set(bigDec2), Set(bigDec3)).get === Nil // bigDec3 match all
        inputMolecule(Set(bigDec1, bigDec2, bigDec3)).get === List(
          // (bigDec1, Set(bigDec1, bigDec2, bigDec3)),  // bigDec1 AND bigDec2 AND bigDec3 match
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )


        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec1)).get === List(
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2)).get === List(
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec3)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec5)).get === List(
          (bigDec2, Set(bigDec2, bigDec3, bigDec4))
        )

        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4, bigDec5)).get === List(
          (bigDec2, Set(bigDec2, bigDec3, bigDec4))
        )
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs.>(?))

        inputMolecule(Nil).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))
        inputMolecule(List(Set[BigDecimal]())).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        // (bigDec3, bigDec4), (bigDec4, bigDec5), (bigDec4, bigDec5, bigDec6)
        inputMolecule(List(Set(bigDec2))).get === List((bigDec2, Set(bigDec3)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs.>=(?))

        inputMolecule(Nil).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))
        inputMolecule(List(Set[BigDecimal]())).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        // (bigDec2, bigDec4), (bigDec3, bigDec4), (bigDec4, bigDec5), (bigDec4, bigDec5, bigDec6)
        inputMolecule(List(Set(bigDec2))).get === List((bigDec1, Set(bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs.<(?))

        inputMolecule(Nil).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))
        inputMolecule(List(Set[BigDecimal]())).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        inputMolecule(List(Set(bigDec2))).get === List((bigDec1, Set(bigDec1)))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs.<=(?))

        inputMolecule(Nil).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))
        inputMolecule(List(Set[BigDecimal]())).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec3, bigDec2)), (bigDec3, Set(bigDec4, bigDec3)), (bigDec4, Set(bigDec4, bigDec5)), (bigDec5, Set(bigDec4, bigDec6, bigDec5)))

        inputMolecule(List(Set(bigDec2))).get === List((bigDec1, Set(bigDec1, bigDec2)), (bigDec2, Set(bigDec2)))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigDecs(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[BigDecimal]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigDec1))).get === List(Set(bigDec1, bigDec2))
        inputMolecule(List(Set(bigDec2))).get === List(Set(bigDec1, bigDec2, bigDec3)) // (bigDec1, bigDec2) + (bigDec2, bigDec3)
        inputMolecule(List(Set(bigDec3))).get === List(Set(bigDec2, bigDec3, bigDec4)) // (bigDec2, bigDec3) + (bigDec3, bigDec4)

        inputMolecule(List(Set(bigDec1, bigDec2))).get === List(Set(bigDec1, bigDec2))
        inputMolecule(List(Set(bigDec1, bigDec3))).get === Nil
        inputMolecule(List(Set(bigDec2, bigDec3))).get === List(Set(bigDec2, bigDec3))
        inputMolecule(List(Set(bigDec4, bigDec5))).get === List(Set(bigDec4, bigDec5, bigDec6)) // (bigDec4, bigDec5) + (bigDec4, bigDec5, bigDec6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigDec1), Set(bigDec1))).get === List(Set(bigDec1, bigDec2))
        inputMolecule(List(Set(bigDec1), Set(bigDec2))).get === List(Set(bigDec1, bigDec2, bigDec3)) // (bigDec1, bigDec2) + (bigDec2, bigDec3)
        inputMolecule(List(Set(bigDec1), Set(bigDec3))).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2)) // (bigDec1, bigDec2) + (bigDec2, bigDec3) + (bigDec3, bigDec4)
        inputMolecule(List(Set(bigDec2), Set(bigDec3))).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4)) // (bigDec1, bigDec2) + (bigDec2, bigDec3) + (bigDec3, bigDec4)

        inputMolecule(List(Set(bigDec1, bigDec2), Set(bigDec3))).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4)) // (bigDec1, bigDec2) + (bigDec2, bigDec3) + (bigDec3, bigDec4)
        inputMolecule(List(Set(bigDec1), Set(bigDec2, bigDec3))).get === List(Set(bigDec1, bigDec3, bigDec2)) // (bigDec1, bigDec2) + (bigDec2, bigDec3)
        inputMolecule(List(Set(bigDec1), Set(bigDec2), Set(bigDec3))).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4)) // (bigDec1, bigDec2) + (bigDec2, bigDec3) + (bigDec3, bigDec4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigDec1, Set(bigDec1, bigDec2, bigDec3)),
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        Ns.bigDec.bigDecs insert all
        val inputMolecule = m(Ns.bigDecs.not(?)) // or m(Ns.bigDec.bigDecs.!=(?))

        inputMolecule(Nil).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5))
        inputMolecule(Set[BigDecimal]()).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigDec1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(bigDec1)).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))
        // Same as
        inputMolecule(List(Set(bigDec1))).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))

        inputMolecule(Set(bigDec2)).get === List(Set(bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec3)).get === Nil // bigDec3 match all

        inputMolecule(Set(bigDec1), Set(bigDec2)).get === List(Set(bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec1), Set(bigDec3)).get === Nil // bigDec3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigDec1, bigDec2)).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec1, bigDec3)).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))

        inputMolecule(Set(bigDec1), Set(bigDec2), Set(bigDec3)).get === Nil // bigDec3 match all
        inputMolecule(Set(bigDec1, bigDec2, bigDec3)).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))

        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec1)).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2)).get === List(Set(bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec3)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec5)).get === List(Set(bigDec2, bigDec3, bigDec4))

        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(Set(bigDec3, bigDec4, bigDec5))
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4, bigDec5)).get === List(Set(bigDec2, bigDec3, bigDec4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigDecs.>(?))

        inputMolecule(Nil).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))
        inputMolecule(List(Set[BigDecimal]())).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))

        inputMolecule(List(Set(bigDec2))).get === List(Set(bigDec3, bigDec4, bigDec5, bigDec6))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigDecs.>=(?))

        inputMolecule(Nil).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))
        inputMolecule(List(Set[BigDecimal]())).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))

        inputMolecule(List(Set(bigDec2))).get === List(Set(bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigDecs.<(?))

        inputMolecule(Nil).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))
        inputMolecule(List(Set[BigDecimal]())).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))

        inputMolecule(List(Set(bigDec2))).get === List(Set(bigDec1))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigDecs.<=(?))

        inputMolecule(Nil).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))
        inputMolecule(List(Set[BigDecimal]())).get === List(Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6))

        inputMolecule(List(Set(bigDec2))).get === List(Set(bigDec1, bigDec2))

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs_(?))

        inputMolecule(Nil).get === List(bigDec6)
        inputMolecule(List(Set[BigDecimal]())).get === List(bigDec6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(bigDec1))).get === List(bigDec1)
        inputMolecule(List(Set(bigDec2))).get.sorted === List(bigDec1, bigDec2)
        inputMolecule(List(Set(bigDec3))).get.sorted === List(bigDec2, bigDec3)

        inputMolecule(List(Set(bigDec1, bigDec1))).get === List(bigDec1)
        inputMolecule(List(Set(bigDec1, bigDec2))).get === List(bigDec1)
        inputMolecule(List(Set(bigDec1, bigDec3))).get === Nil
        inputMolecule(List(Set(bigDec2, bigDec3))).get === List(bigDec2)
        inputMolecule(List(Set(bigDec4, bigDec5))).get.sorted === List(bigDec4, bigDec5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(bigDec1, bigDec2), Set[BigDecimal]())).get === List(bigDec1)
        inputMolecule(List(Set(bigDec1), Set(bigDec1))).get === List(bigDec1)
        inputMolecule(List(Set(bigDec1), Set(bigDec2))).get.sorted === List(bigDec1, bigDec2)
        inputMolecule(List(Set(bigDec1), Set(bigDec3))).get.sorted === List(bigDec1, bigDec2, bigDec3)

        inputMolecule(List(Set(bigDec1, bigDec2), Set(bigDec3))).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(List(Set(bigDec1), Set(bigDec2, bigDec3))).get.sorted === List(bigDec1, bigDec2)
        inputMolecule(List(Set(bigDec1), Set(bigDec2), Set(bigDec3))).get.sorted === List(bigDec1, bigDec2, bigDec3)

        inputMolecule(List(Set(bigDec1, bigDec2), Set(bigDec3, bigDec4))).get.sorted === List(bigDec1, bigDec3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (bigDec1, Set(bigDec1, bigDec2, bigDec3)),
          (bigDec2, Set(bigDec2, bigDec3, bigDec4)),
          (bigDec3, Set(bigDec3, bigDec4, bigDec5))
        )
        Ns.bigDec.bigDecs insert all
        val inputMolecule = m(Ns.bigDec.bigDecs_.not(?)) // or m(Ns.bigDec.bigDecs.!=(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3)
        inputMolecule(Set[BigDecimal]()).get.sorted === List(bigDec1, bigDec2, bigDec3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(bigDec1).get === ...
        // inputMolecule(List(bigDec1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(bigDec1)).get.sorted === List(bigDec2, bigDec3)
        // Same as
        inputMolecule(List(Set(bigDec1))).get.sorted === List(bigDec2, bigDec3)

        inputMolecule(Set(bigDec2)).get === List(bigDec3)
        inputMolecule(Set(bigDec3)).get === Nil // bigDec3 match all


        inputMolecule(Set(bigDec1), Set(bigDec2)).get === List(bigDec3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(bigDec1, bigDec2)).get.sorted === List(bigDec2, bigDec3)

        inputMolecule(Set(bigDec1), Set(bigDec3)).get === Nil // bigDec3 match all
        inputMolecule(Set(bigDec1, bigDec3)).get.sorted === List(bigDec2, bigDec3)

        inputMolecule(Set(bigDec1), Set(bigDec2), Set(bigDec3)).get === Nil // bigDec3 match all
        inputMolecule(Set(bigDec1, bigDec2, bigDec3)).get.sorted === List(bigDec2, bigDec3)

        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec1)).get.sorted === List(bigDec2, bigDec3)
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2)).get === List(bigDec3)
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec3)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4)).get === Nil
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec5)).get === List(bigDec2)

        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec2, bigDec3)).get === List(bigDec3)
        inputMolecule(Set(bigDec1, bigDec2), Set(bigDec4, bigDec5)).get === List(bigDec2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs_.>(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)
        inputMolecule(List(Set[BigDecimal]())).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

        // (bigDec3, bigDec4), (bigDec4, bigDec5), (bigDec4, bigDec5, bigDec6)
        inputMolecule(List(Set(bigDec2))).get.sorted === List(bigDec2, bigDec3, bigDec4, bigDec5)

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs_.>=(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)
        inputMolecule(List(Set[BigDecimal]())).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

        // (bigDec2, bigDec4), (bigDec3, bigDec4), (bigDec4, bigDec5), (bigDec4, bigDec5, bigDec6)
        inputMolecule(List(Set(bigDec2))).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs_.<(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)
        inputMolecule(List(Set[BigDecimal]())).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

        inputMolecule(List(Set(bigDec2))).get === List(bigDec1)

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.bigDec.bigDecs_.<=(?))

        inputMolecule(Nil).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)
        inputMolecule(List(Set[BigDecimal]())).get.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

        inputMolecule(List(Set(bigDec2))).get.sorted === List(bigDec1, bigDec2)

        (inputMolecule(List(Set(bigDec2, bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(bigDec2), Set(bigDec3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}