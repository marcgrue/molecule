package molecule.tests.core.input1

import molecule.core.input.exception.MoleculeException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in1_out2._
import molecule.TestSpec


class Input1Float extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.float$ insert List(
        (str1, Some(float1)),
        (str2, Some(float2)),
        (str3, Some(float3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.float(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(float1)).get === List(float1)
        inputMolecule(List(float1, float1)).get === List(float1)
        inputMolecule(List(float1, float2)).get.sorted === List(float1, float2)

        // Varargs
        inputMolecule(float1).get === List(float1)
        inputMolecule(float1, float2).get.sorted === List(float1, float2)

        // `or`
        inputMolecule(float1 or float2).get.sorted === List(float1, float2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.float.not(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)

        inputMolecule(List(float1)).get.sorted === List(float2, float3)
        inputMolecule(List(float1, float1)).get.sorted === List(float2, float3)
        inputMolecule(List(float1, float2)).get.sorted === List(float3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.float.>(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)
        inputMolecule(List(float2)).get.sorted === List(float3)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.float.>=(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)
        inputMolecule(List(float2)).get.sorted === List(float2, float3)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.float.<(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)
        inputMolecule(List(float2)).get === List(float1)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.float.<=(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)
        inputMolecule(List(float2)).get.sorted === List(float1, float2)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.float_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(float1)).get === List(str1)
        inputMolecule(List(float1, float1)).get === List(str1)
        inputMolecule(List(float1, float2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.float_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(float1)).get.sorted === List(str2, str3)
        inputMolecule(List(float1, float1)).get.sorted === List(str2, str3)
        inputMolecule(List(float1, float2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.float_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(float2)).get.sorted === List(str3)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.float_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(float2)).get.sorted === List(str2, str3)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.float_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(float2)).get === List(str1)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.float_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(float2)).get.sorted === List(str1, str2)
        (inputMolecule(List(float2, float3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.float.floats$ insert List(
        (float1, Some(Set(float1, float2))),
        (float2, Some(Set(float2, float3))),
        (float3, Some(Set(float3, float4))),
        (float4, Some(Set(float4, float5))),
        (float5, Some(Set(float4, float5, float6))),
        (float6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.float.floats(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Float]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(float1))).get === List((float1, Set(float1, float2)))
        inputMolecule(List(Set(float1, float1))).get === List((float1, Set(float1, float2)))

        inputMolecule(List(Set(float1, float2))).get === List((float1, Set(float1, float2)))
        inputMolecule(List(Set(float1, float3))).get === Nil
        inputMolecule(List(Set(float2, float3))).get === List((float2, Set(float3, float2)))
        inputMolecule(List(Set(float4, float5))).get === List((float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        // 1 arg
        inputMolecule(Set(float1)).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1, float1)).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1, float2)).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1, float3)).get === Nil
        inputMolecule(Set(float2, float3)).get === List((float2, Set(float3, float2)))
        inputMolecule(Set(float4, float5)).get === List((float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(float1, float2), Set[Float]())).get === List((float1, Set(float1, float2)))
        inputMolecule(List(Set(float1), Set(float1))).get === List((float1, Set(float1, float2)))
        inputMolecule(List(Set(float1), Set(float2))).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(List(Set(float1), Set(float3))).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(List(Set(float1, float2), Set(float3))).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(List(Set(float1), Set(float2, float3))).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(List(Set(float1), Set(float2), Set(float3))).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(List(Set(float1, float2), Set(float3, float4))).get === List((float1, Set(float1, float2)), (float3, Set(float4, float3)))


        // Multiple varargs
        inputMolecule(Set(float1, float2), Set[Float]()).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1), Set(float1)).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1), Set(float2)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(Set(float1), Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1, float2), Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1), Set(float2, float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(Set(float1), Set(float2), Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1, float2), Set(float3, float4)).get === List((float1, Set(float1, float2)), (float3, Set(float4, float3)))

        // `or`
        inputMolecule(Set(float1, float2) or Set[Float]()).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1) or Set(float1)).get === List((float1, Set(float1, float2)))
        inputMolecule(Set(float1) or Set(float2)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(Set(float1) or Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1, float2) or Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1) or Set(float2, float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)))
        inputMolecule(Set(float1) or Set(float2) or Set(float3)).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)))
        inputMolecule(Set(float1, float2) or Set(float3, float4)).get === List((float1, Set(float1, float2)), (float3, Set(float4, float3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (float1, Set(float1, float2, float3)),
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )
        Ns.float.floats insert all
        val inputMolecule = m(Ns.float.floats.not(?)) // or m(Ns.float.floats.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[Float]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(float1).get === ...
        // inputMolecule(List(float1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(float1)).get === List(
          // (float1, Set(float1, float2, float3)),  // float1 match
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )
        // Same as
        inputMolecule(List(Set(float1))).get === List(
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )

        inputMolecule(Set(float2)).get === List(
          // (float1, Set(float1, float2, float3)),  // float2 match
          // (float2, Set(float2, float3, float4)),  // float2 match
          (float3, Set(float3, float4, float5))
        )

        inputMolecule(Set(float3)).get === Nil // float3 match all


        inputMolecule(Set(float1), Set(float2)).get === List(
          // (float1, Set(float1, float2, float3)),  // float1 match, float2 match
          // (float2, Set(float2, float3, float4)),  // float2 match
          (float3, Set(float3, float4, float5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(float1, float2)).get === List(
          // (float1, Set(float1, float2, float3)),  // float1 AND float2 match
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )


        inputMolecule(Set(float1), Set(float3)).get === Nil // float3 match all
        inputMolecule(Set(float1, float3)).get === List(
          // (float1, Set(float1, float2, float3)),  // float1 AND float3 match
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )


        inputMolecule(Set(float1), Set(float2), Set(float3)).get === Nil // float3 match all
        inputMolecule(Set(float1, float2, float3)).get === List(
          // (float1, Set(float1, float2, float3)),  // float1 AND float2 AND float3 match
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )


        inputMolecule(Set(float1, float2), Set(float1)).get === List(
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )
        inputMolecule(Set(float1, float2), Set(float2)).get === List(
          (float3, Set(float3, float4, float5))
        )
        inputMolecule(Set(float1, float2), Set(float3)).get === Nil
        inputMolecule(Set(float1, float2), Set(float4)).get === Nil
        inputMolecule(Set(float1, float2), Set(float5)).get === List(
          (float2, Set(float2, float3, float4))
        )

        inputMolecule(Set(float1, float2), Set(float2, float3)).get === List(
          (float3, Set(float3, float4, float5))
        )
        inputMolecule(Set(float1, float2), Set(float4, float5)).get === List(
          (float2, Set(float2, float3, float4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.float.floats.>(?))

        inputMolecule(Nil).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))
        inputMolecule(List(Set[Float]())).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        // (float3, float4), (float4, float5), (float4, float5, float6)
        inputMolecule(List(Set(float2))).get === List((float2, Set(float3)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.float.floats.>=(?))

        inputMolecule(Nil).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))
        inputMolecule(List(Set[Float]())).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        // (float2, float4), (float3, float4), (float4, float5), (float4, float5, float6)
        inputMolecule(List(Set(float2))).get === List((float1, Set(float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.float.floats.<(?))

        inputMolecule(Nil).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))
        inputMolecule(List(Set[Float]())).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        inputMolecule(List(Set(float2))).get === List((float1, Set(float1)))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.float.floats.<=(?))

        inputMolecule(Nil).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))
        inputMolecule(List(Set[Float]())).get === List((float1, Set(float1, float2)), (float2, Set(float3, float2)), (float3, Set(float4, float3)), (float4, Set(float4, float5)), (float5, Set(float4, float6, float5)))

        inputMolecule(List(Set(float2))).get === List((float1, Set(float1, float2)), (float2, Set(float2)))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.floats(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Float]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(float1))).get === List(Set(float1, float2))
        inputMolecule(List(Set(float2))).get === List(Set(float1, float2, float3)) // (float1, float2) + (float2, float3)
        inputMolecule(List(Set(float3))).get === List(Set(float2, float3, float4)) // (float2, float3) + (float3, float4)

        inputMolecule(List(Set(float1, float2))).get === List(Set(float1, float2))
        inputMolecule(List(Set(float1, float3))).get === Nil
        inputMolecule(List(Set(float2, float3))).get === List(Set(float2, float3))
        inputMolecule(List(Set(float4, float5))).get === List(Set(float4, float5, float6)) // (float4, float5) + (float4, float5, float6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(float1), Set(float1))).get === List(Set(float1, float2))
        inputMolecule(List(Set(float1), Set(float2))).get === List(Set(float1, float2, float3)) // (float1, float2) + (float2, float3)
        inputMolecule(List(Set(float1), Set(float3))).get === List(Set(float1, float4, float3, float2)) // (float1, float2) + (float2, float3) + (float3, float4)
        inputMolecule(List(Set(float2), Set(float3))).get === List(Set(float1, float2, float3, float4)) // (float1, float2) + (float2, float3) + (float3, float4)

        inputMolecule(List(Set(float1, float2), Set(float3))).get === List(Set(float1, float2, float3, float4)) // (float1, float2) + (float2, float3) + (float3, float4)
        inputMolecule(List(Set(float1), Set(float2, float3))).get === List(Set(float1, float3, float2)) // (float1, float2) + (float2, float3)
        inputMolecule(List(Set(float1), Set(float2), Set(float3))).get === List(Set(float1, float2, float3, float4)) // (float1, float2) + (float2, float3) + (float3, float4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (float1, Set(float1, float2, float3)),
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )
        Ns.float.floats insert all
        val inputMolecule = m(Ns.floats.not(?)) // or m(Ns.float.floats.!=(?))

        inputMolecule(Nil).get === List(Set(float1, float2, float3, float4, float5))
        inputMolecule(Set[Float]()).get === List(Set(float1, float2, float3, float4, float5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(float1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(float1)).get === List(Set(float2, float3, float4, float5))
        // Same as
        inputMolecule(List(Set(float1))).get === List(Set(float2, float3, float4, float5))

        inputMolecule(Set(float2)).get === List(Set(float3, float4, float5))
        inputMolecule(Set(float3)).get === Nil // float3 match all

        inputMolecule(Set(float1), Set(float2)).get === List(Set(float3, float4, float5))
        inputMolecule(Set(float1), Set(float3)).get === Nil // float3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(float1, float2)).get === List(Set(float2, float3, float4, float5))
        inputMolecule(Set(float1, float3)).get === List(Set(float2, float3, float4, float5))

        inputMolecule(Set(float1), Set(float2), Set(float3)).get === Nil // float3 match all
        inputMolecule(Set(float1, float2, float3)).get === List(Set(float2, float3, float4, float5))

        inputMolecule(Set(float1, float2), Set(float1)).get === List(Set(float2, float3, float4, float5))
        inputMolecule(Set(float1, float2), Set(float2)).get === List(Set(float3, float4, float5))
        inputMolecule(Set(float1, float2), Set(float3)).get === Nil
        inputMolecule(Set(float1, float2), Set(float4)).get === Nil
        inputMolecule(Set(float1, float2), Set(float5)).get === List(Set(float2, float3, float4))

        inputMolecule(Set(float1, float2), Set(float2, float3)).get === List(Set(float3, float4, float5))
        inputMolecule(Set(float1, float2), Set(float4, float5)).get === List(Set(float2, float3, float4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.floats.>(?))

        inputMolecule(Nil).get === List(Set(float1, float2, float3, float4, float5, float6))
        inputMolecule(List(Set[Float]())).get === List(Set(float1, float2, float3, float4, float5, float6))

        inputMolecule(List(Set(float2))).get === List(Set(float3, float4, float5, float6))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.floats.>=(?))

        inputMolecule(Nil).get === List(Set(float1, float2, float3, float4, float5, float6))
        inputMolecule(List(Set[Float]())).get === List(Set(float1, float2, float3, float4, float5, float6))

        inputMolecule(List(Set(float2))).get === List(Set(float2, float3, float4, float5, float6))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.floats.<(?))

        inputMolecule(Nil).get === List(Set(float1, float2, float3, float4, float5, float6))
        inputMolecule(List(Set[Float]())).get === List(Set(float1, float2, float3, float4, float5, float6))

        inputMolecule(List(Set(float2))).get === List(Set(float1))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.floats.<=(?))

        inputMolecule(Nil).get === List(Set(float1, float2, float3, float4, float5, float6))
        inputMolecule(List(Set[Float]())).get === List(Set(float1, float2, float3, float4, float5, float6))

        inputMolecule(List(Set(float2))).get === List(Set(float1, float2))

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.float.floats_(?))

        inputMolecule(Nil).get === List(float6)
        inputMolecule(List(Set[Float]())).get === List(float6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(float1))).get === List(float1)
        inputMolecule(List(Set(float2))).get.sorted === List(float1, float2)
        inputMolecule(List(Set(float3))).get.sorted === List(float2, float3)

        inputMolecule(List(Set(float1, float1))).get === List(float1)
        inputMolecule(List(Set(float1, float2))).get === List(float1)
        inputMolecule(List(Set(float1, float3))).get === Nil
        inputMolecule(List(Set(float2, float3))).get === List(float2)
        inputMolecule(List(Set(float4, float5))).get.sorted === List(float4, float5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(float1, float2), Set[Float]())).get === List(float1)
        inputMolecule(List(Set(float1), Set(float1))).get === List(float1)
        inputMolecule(List(Set(float1), Set(float2))).get.sorted === List(float1, float2)
        inputMolecule(List(Set(float1), Set(float3))).get.sorted === List(float1, float2, float3)

        inputMolecule(List(Set(float1, float2), Set(float3))).get.sorted === List(float1, float2, float3)
        inputMolecule(List(Set(float1), Set(float2, float3))).get.sorted === List(float1, float2)
        inputMolecule(List(Set(float1), Set(float2), Set(float3))).get.sorted === List(float1, float2, float3)

        inputMolecule(List(Set(float1, float2), Set(float3, float4))).get.sorted === List(float1, float3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (float1, Set(float1, float2, float3)),
          (float2, Set(float2, float3, float4)),
          (float3, Set(float3, float4, float5))
        )
        Ns.float.floats insert all
        val inputMolecule = m(Ns.float.floats_.not(?)) // or m(Ns.float.floats.!=(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3)
        inputMolecule(Set[Float]()).get.sorted === List(float1, float2, float3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(float1).get === ...
        // inputMolecule(List(float1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(float1)).get.sorted === List(float2, float3)
        // Same as
        inputMolecule(List(Set(float1))).get.sorted === List(float2, float3)

        inputMolecule(Set(float2)).get === List(float3)
        inputMolecule(Set(float3)).get === Nil // float3 match all


        inputMolecule(Set(float1), Set(float2)).get === List(float3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(float1, float2)).get.sorted === List(float2, float3)

        inputMolecule(Set(float1), Set(float3)).get === Nil // float3 match all
        inputMolecule(Set(float1, float3)).get.sorted === List(float2, float3)

        inputMolecule(Set(float1), Set(float2), Set(float3)).get === Nil // float3 match all
        inputMolecule(Set(float1, float2, float3)).get.sorted === List(float2, float3)

        inputMolecule(Set(float1, float2), Set(float1)).get.sorted === List(float2, float3)
        inputMolecule(Set(float1, float2), Set(float2)).get === List(float3)
        inputMolecule(Set(float1, float2), Set(float3)).get === Nil
        inputMolecule(Set(float1, float2), Set(float4)).get === Nil
        inputMolecule(Set(float1, float2), Set(float5)).get === List(float2)

        inputMolecule(Set(float1, float2), Set(float2, float3)).get === List(float3)
        inputMolecule(Set(float1, float2), Set(float4, float5)).get === List(float2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.float.floats_.>(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3, float4, float5)
        inputMolecule(List(Set[Float]())).get.sorted === List(float1, float2, float3, float4, float5)

        // (float3, float4), (float4, float5), (float4, float5, float6)
        inputMolecule(List(Set(float2))).get.sorted === List(float2, float3, float4, float5)

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.float.floats_.>=(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3, float4, float5)
        inputMolecule(List(Set[Float]())).get.sorted === List(float1, float2, float3, float4, float5)

        // (float2, float4), (float3, float4), (float4, float5), (float4, float5, float6)
        inputMolecule(List(Set(float2))).get.sorted === List(float1, float2, float3, float4, float5)

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.float.floats_.<(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3, float4, float5)
        inputMolecule(List(Set[Float]())).get.sorted === List(float1, float2, float3, float4, float5)

        inputMolecule(List(Set(float2))).get === List(float1)

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.float.floats_.<=(?))

        inputMolecule(Nil).get.sorted === List(float1, float2, float3, float4, float5)
        inputMolecule(List(Set[Float]())).get.sorted === List(float1, float2, float3, float4, float5)

        inputMolecule(List(Set(float2))).get.sorted === List(float1, float2)

        (inputMolecule(List(Set(float2, float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(float2), Set(float3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}