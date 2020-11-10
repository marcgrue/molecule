package molecule.coretests.input1

import java.util.UUID
import molecule.core.input.exception.InputMoleculeException
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.in1_out2._


class Input1UUID extends CoreSpec {
  sequential

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.uuid$ insert List(
        (str1, Some(uuid1)),
        (str2, Some(uuid2)),
        (str3, Some(uuid3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.uuid(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(uuid1)).get === List(uuid1)
        inputMolecule(List(uuid1, uuid1)).get === List(uuid1)
        inputMolecule(List(uuid1, uuid2)).get.sorted === List(uuid1, uuid2)

        // Varargs
        inputMolecule(uuid1).get === List(uuid1)
        inputMolecule(uuid1, uuid2).get.sorted === List(uuid1, uuid2)

        // `or`
        inputMolecule(uuid1 or uuid2).get.sorted === List(uuid1, uuid2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.uuid.not(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)

        inputMolecule(List(uuid1)).get.sorted === List(uuid2, uuid3)
        inputMolecule(List(uuid1, uuid1)).get.sorted === List(uuid2, uuid3)
        inputMolecule(List(uuid1, uuid2)).get.sorted === List(uuid3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.uuid.>(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(List(uuid2)).get.sorted === List(uuid3)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.uuid.>=(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(List(uuid2)).get.sorted === List(uuid2, uuid3)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.uuid.<(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(List(uuid2)).get === List(uuid1)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.uuid.<=(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(List(uuid2)).get.sorted === List(uuid1, uuid2)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(uuid1)).get === List(str1)
        inputMolecule(List(uuid1, uuid1)).get === List(str1)
        inputMolecule(List(uuid1, uuid2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uuid1)).get.sorted === List(str2, str3)
        inputMolecule(List(uuid1, uuid1)).get.sorted === List(str2, str3)
        inputMolecule(List(uuid1, uuid2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uuid2)).get.sorted === List(str3)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uuid2)).get.sorted === List(str2, str3)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uuid2)).get === List(str1)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.uuid_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(uuid2)).get.sorted === List(str1, str2)
        (inputMolecule(List(uuid2, uuid3)).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.uuid.uuids$ insert List(
        (uuid1, Some(Set(uuid1, uuid2))),
        (uuid2, Some(Set(uuid2, uuid3))),
        (uuid3, Some(Set(uuid3, uuid4))),
        (uuid4, Some(Set(uuid4, uuid5))),
        (uuid5, Some(Set(uuid4, uuid5, uuid6))),
        (uuid6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[UUID]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uuid1))).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(List(Set(uuid1, uuid1))).get === List((uuid1, Set(uuid1, uuid2)))

        inputMolecule(List(Set(uuid1, uuid2))).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(List(Set(uuid1, uuid3))).get === Nil
        inputMolecule(List(Set(uuid2, uuid3))).get === List((uuid2, Set(uuid3, uuid2)))
        inputMolecule(List(Set(uuid4, uuid5))).get === List((uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        // 1 arg
        inputMolecule(Set(uuid1)).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1, uuid1)).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1, uuid2)).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1, uuid3)).get === Nil
        inputMolecule(Set(uuid2, uuid3)).get === List((uuid2, Set(uuid3, uuid2)))
        inputMolecule(Set(uuid4, uuid5)).get === List((uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uuid1, uuid2), Set[UUID]())).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(List(Set(uuid1), Set(uuid1))).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(List(Set(uuid1), Set(uuid2))).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(List(Set(uuid1), Set(uuid3))).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(List(Set(uuid1, uuid2), Set(uuid3))).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(List(Set(uuid1), Set(uuid2, uuid3))).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(List(Set(uuid1), Set(uuid2), Set(uuid3))).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(List(Set(uuid1, uuid2), Set(uuid3, uuid4))).get === List((uuid1, Set(uuid1, uuid2)), (uuid3, Set(uuid4, uuid3)))


        // Multiple varargs
        inputMolecule(Set(uuid1, uuid2), Set[UUID]()).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1), Set(uuid1)).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1), Set(uuid2)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(Set(uuid1), Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1, uuid2), Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1), Set(uuid2, uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(Set(uuid1), Set(uuid2), Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1, uuid2), Set(uuid3, uuid4)).get === List((uuid1, Set(uuid1, uuid2)), (uuid3, Set(uuid4, uuid3)))

        // `or`
        inputMolecule(Set(uuid1, uuid2) or Set[UUID]()).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1) or Set(uuid1)).get === List((uuid1, Set(uuid1, uuid2)))
        inputMolecule(Set(uuid1) or Set(uuid2)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(Set(uuid1) or Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1, uuid2) or Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1) or Set(uuid2, uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)))
        inputMolecule(Set(uuid1) or Set(uuid2) or Set(uuid3)).get === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)))
        inputMolecule(Set(uuid1, uuid2) or Set(uuid3, uuid4)).get === List((uuid1, Set(uuid1, uuid2)), (uuid3, Set(uuid4, uuid3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (uuid1, Set(uuid1, uuid2, uuid3)),
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        Ns.uuid.uuids insert all
        val inputMolecule = m(Ns.uuid.uuids.not(?)) // or m(Ns.uuid.uuids.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[UUID]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uuid1).get === ...
        // inputMolecule(List(uuid1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(uuid1)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid1 match
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        // Same as
        inputMolecule(List(Set(uuid1))).get === List(
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )

        inputMolecule(Set(uuid2)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid2 match
          // (uuid2, Set(uuid2, uuid3, uuid4)),  // uuid2 match
          (uuid3, Set(uuid3, uuid4, uuid5))
        )

        inputMolecule(Set(uuid3)).get === Nil // uuid3 match all


        inputMolecule(Set(uuid1), Set(uuid2)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid1 match, uuid2 match
          // (uuid2, Set(uuid2, uuid3, uuid4)),  // uuid2 match
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uuid1, uuid2)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid1 AND uuid2 match
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )


        inputMolecule(Set(uuid1), Set(uuid3)).get === Nil // uuid3 match all
        inputMolecule(Set(uuid1, uuid3)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid1 AND uuid3 match
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )


        inputMolecule(Set(uuid1), Set(uuid2), Set(uuid3)).get === Nil // uuid3 match all
        inputMolecule(Set(uuid1, uuid2, uuid3)).get === List(
          // (uuid1, Set(uuid1, uuid2, uuid3)),  // uuid1 AND uuid2 AND uuid3 match
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )


        inputMolecule(Set(uuid1, uuid2), Set(uuid1)).get === List(
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        inputMolecule(Set(uuid1, uuid2), Set(uuid2)).get === List(
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        inputMolecule(Set(uuid1, uuid2), Set(uuid3)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid4)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid5)).get === List(
          (uuid2, Set(uuid2, uuid3, uuid4))
        )

        inputMolecule(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        inputMolecule(Set(uuid1, uuid2), Set(uuid4, uuid5)).get === List(
          (uuid2, Set(uuid2, uuid3, uuid4))
        )
      }

      
      ">" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids.>(?))

        inputMolecule(Nil).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))
        inputMolecule(List(Set[UUID]())).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        // (uuid3, uuid4), (uuid4, uuid5), (uuid4, uuid5, uuid6)
        inputMolecule(List(Set(uuid2))).get.sortBy(_._1.toString) === List((uuid2, Set(uuid3)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids.>=(?))

        inputMolecule(Nil).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))
        inputMolecule(List(Set[UUID]())).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        // (uuid2, uuid4), (uuid3, uuid4), (uuid4, uuid5), (uuid4, uuid5, uuid6)
        inputMolecule(List(Set(uuid2))).get.sortBy(_._1.toString) === List((uuid1, Set(uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids.<(?))

        inputMolecule(Nil).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))
        inputMolecule(List(Set[UUID]())).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        inputMolecule(List(Set(uuid2))).get === List((uuid1, Set(uuid1)))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids.<=(?))

        inputMolecule(Nil).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))
        inputMolecule(List(Set[UUID]())).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid3)), (uuid4, Set(uuid4, uuid5)), (uuid5, Set(uuid4, uuid6, uuid5)))

        inputMolecule(List(Set(uuid2))).get.sortBy(_._1.toString) === List((uuid1, Set(uuid1, uuid2)), (uuid2, Set(uuid2)))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uuids(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[UUID]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uuid1))).get === List(Set(uuid1, uuid2))
        inputMolecule(List(Set(uuid2))).get === List(Set(uuid1, uuid2, uuid3)) // (uuid1, uuid2) + (uuid2, uuid3)
        inputMolecule(List(Set(uuid3))).get === List(Set(uuid2, uuid3, uuid4)) // (uuid2, uuid3) + (uuid3, uuid4)

        inputMolecule(List(Set(uuid1, uuid2))).get === List(Set(uuid1, uuid2))
        inputMolecule(List(Set(uuid1, uuid3))).get === Nil
        inputMolecule(List(Set(uuid2, uuid3))).get === List(Set(uuid2, uuid3))
        inputMolecule(List(Set(uuid4, uuid5))).get === List(Set(uuid4, uuid5, uuid6)) // (uuid4, uuid5) + (uuid4, uuid5, uuid6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uuid1), Set(uuid1))).get === List(Set(uuid1, uuid2))
        inputMolecule(List(Set(uuid1), Set(uuid2))).get === List(Set(uuid1, uuid2, uuid3)) // (uuid1, uuid2) + (uuid2, uuid3)
        inputMolecule(List(Set(uuid1), Set(uuid3))).get === List(Set(uuid1, uuid4, uuid3, uuid2)) // (uuid1, uuid2) + (uuid2, uuid3) + (uuid3, uuid4)
        inputMolecule(List(Set(uuid2), Set(uuid3))).get === List(Set(uuid1, uuid2, uuid3, uuid4)) // (uuid1, uuid2) + (uuid2, uuid3) + (uuid3, uuid4)

        inputMolecule(List(Set(uuid1, uuid2), Set(uuid3))).get === List(Set(uuid1, uuid2, uuid3, uuid4)) // (uuid1, uuid2) + (uuid2, uuid3) + (uuid3, uuid4)
        inputMolecule(List(Set(uuid1), Set(uuid2, uuid3))).get === List(Set(uuid1, uuid3, uuid2)) // (uuid1, uuid2) + (uuid2, uuid3)
        inputMolecule(List(Set(uuid1), Set(uuid2), Set(uuid3))).get === List(Set(uuid1, uuid2, uuid3, uuid4)) // (uuid1, uuid2) + (uuid2, uuid3) + (uuid3, uuid4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (uuid1, Set(uuid1, uuid2, uuid3)),
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        Ns.uuid.uuids insert all
        val inputMolecule = m(Ns.uuids.not(?)) // or m(Ns.uuid.uuids.!=(?))

        inputMolecule(Nil).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5))
        inputMolecule(Set[UUID]()).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uuid1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(uuid1)).get === List(Set(uuid2, uuid3, uuid4, uuid5))
        // Same as
        inputMolecule(List(Set(uuid1))).get === List(Set(uuid2, uuid3, uuid4, uuid5))

        inputMolecule(Set(uuid2)).get === List(Set(uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid3)).get === Nil // uuid3 match all

        inputMolecule(Set(uuid1), Set(uuid2)).get === List(Set(uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid1), Set(uuid3)).get === Nil // uuid3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uuid1, uuid2)).get === List(Set(uuid2, uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid1, uuid3)).get === List(Set(uuid2, uuid3, uuid4, uuid5))

        inputMolecule(Set(uuid1), Set(uuid2), Set(uuid3)).get === Nil // uuid3 match all
        inputMolecule(Set(uuid1, uuid2, uuid3)).get === List(Set(uuid2, uuid3, uuid4, uuid5))

        inputMolecule(Set(uuid1, uuid2), Set(uuid1)).get === List(Set(uuid2, uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid1, uuid2), Set(uuid2)).get === List(Set(uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid1, uuid2), Set(uuid3)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid4)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid5)).get === List(Set(uuid2, uuid3, uuid4))

        inputMolecule(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(Set(uuid3, uuid4, uuid5))
        inputMolecule(Set(uuid1, uuid2), Set(uuid4, uuid5)).get === List(Set(uuid2, uuid3, uuid4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.uuids.>(?))

        inputMolecule(Nil).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))
        inputMolecule(List(Set[UUID]())).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))

        inputMolecule(List(Set(uuid2))).get === List(Set(uuid3, uuid4, uuid5, uuid6))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uuids.>=(?))

        inputMolecule(Nil).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))
        inputMolecule(List(Set[UUID]())).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))

        inputMolecule(List(Set(uuid2))).get === List(Set(uuid2, uuid3, uuid4, uuid5, uuid6))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uuids.<(?))

        inputMolecule(Nil).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))
        inputMolecule(List(Set[UUID]())).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))

        inputMolecule(List(Set(uuid2))).get === List(Set(uuid1))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uuids.<=(?))

        inputMolecule(Nil).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))
        inputMolecule(List(Set[UUID]())).get === List(Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6))

        inputMolecule(List(Set(uuid2))).get === List(Set(uuid1, uuid2))

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids_(?))

        inputMolecule(Nil).get === List(uuid6)
        inputMolecule(List(Set[UUID]())).get === List(uuid6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(uuid1))).get === List(uuid1)
        inputMolecule(List(Set(uuid2))).get.sorted === List(uuid1, uuid2)
        inputMolecule(List(Set(uuid3))).get.sorted === List(uuid2, uuid3)

        inputMolecule(List(Set(uuid1, uuid1))).get === List(uuid1)
        inputMolecule(List(Set(uuid1, uuid2))).get === List(uuid1)
        inputMolecule(List(Set(uuid1, uuid3))).get === Nil
        inputMolecule(List(Set(uuid2, uuid3))).get === List(uuid2)
        inputMolecule(List(Set(uuid4, uuid5))).get.sorted === List(uuid4, uuid5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(uuid1, uuid2), Set[UUID]())).get === List(uuid1)
        inputMolecule(List(Set(uuid1), Set(uuid1))).get === List(uuid1)
        inputMolecule(List(Set(uuid1), Set(uuid2))).get.sorted === List(uuid1, uuid2)
        inputMolecule(List(Set(uuid1), Set(uuid3))).get.sorted === List(uuid1, uuid2, uuid3)

        inputMolecule(List(Set(uuid1, uuid2), Set(uuid3))).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(List(Set(uuid1), Set(uuid2, uuid3))).get.sorted === List(uuid1, uuid2)
        inputMolecule(List(Set(uuid1), Set(uuid2), Set(uuid3))).get.sorted === List(uuid1, uuid2, uuid3)

        inputMolecule(List(Set(uuid1, uuid2), Set(uuid3, uuid4))).get.sorted === List(uuid1, uuid3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (uuid1, Set(uuid1, uuid2, uuid3)),
          (uuid2, Set(uuid2, uuid3, uuid4)),
          (uuid3, Set(uuid3, uuid4, uuid5))
        )
        Ns.uuid.uuids insert all
        val inputMolecule = m(Ns.uuid.uuids_.not(?)) // or m(Ns.uuid.uuids.!=(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3)
        inputMolecule(Set[UUID]()).get.sorted === List(uuid1, uuid2, uuid3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(uuid1).get === ...
        // inputMolecule(List(uuid1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(uuid1)).get.sorted === List(uuid2, uuid3)
        // Same as
        inputMolecule(List(Set(uuid1))).get.sorted === List(uuid2, uuid3)

        inputMolecule(Set(uuid2)).get === List(uuid3)
        inputMolecule(Set(uuid3)).get === Nil // uuid3 match all


        inputMolecule(Set(uuid1), Set(uuid2)).get === List(uuid3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(uuid1, uuid2)).get.sorted === List(uuid2, uuid3)

        inputMolecule(Set(uuid1), Set(uuid3)).get === Nil // uuid3 match all
        inputMolecule(Set(uuid1, uuid3)).get.sorted === List(uuid2, uuid3)

        inputMolecule(Set(uuid1), Set(uuid2), Set(uuid3)).get === Nil // uuid3 match all
        inputMolecule(Set(uuid1, uuid2, uuid3)).get.sorted === List(uuid2, uuid3)

        inputMolecule(Set(uuid1, uuid2), Set(uuid1)).get.sorted === List(uuid2, uuid3)
        inputMolecule(Set(uuid1, uuid2), Set(uuid2)).get === List(uuid3)
        inputMolecule(Set(uuid1, uuid2), Set(uuid3)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid4)).get === Nil
        inputMolecule(Set(uuid1, uuid2), Set(uuid5)).get === List(uuid2)

        inputMolecule(Set(uuid1, uuid2), Set(uuid2, uuid3)).get === List(uuid3)
        inputMolecule(Set(uuid1, uuid2), Set(uuid4, uuid5)).get === List(uuid2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids_.>(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)
        inputMolecule(List(Set[UUID]())).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)

        // (uuid3, uuid4), (uuid4, uuid5), (uuid4, uuid5, uuid6)
        inputMolecule(List(Set(uuid2))).get.sorted === List(uuid2, uuid3, uuid4, uuid5)

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids_.>=(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)
        inputMolecule(List(Set[UUID]())).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)

        // (uuid2, uuid4), (uuid3, uuid4), (uuid4, uuid5), (uuid4, uuid5, uuid6)
        inputMolecule(List(Set(uuid2))).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids_.<(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)
        inputMolecule(List(Set[UUID]())).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)

        inputMolecule(List(Set(uuid2))).get === List(uuid1)

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.uuid.uuids_.<=(?))

        inputMolecule(Nil).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)
        inputMolecule(List(Set[UUID]())).get.sorted === List(uuid1, uuid2, uuid3, uuid4, uuid5)

        inputMolecule(List(Set(uuid2))).get.sorted === List(uuid1, uuid2)

        (inputMolecule(List(Set(uuid2, uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(uuid2), Set(uuid3))).get must throwA[InputMoleculeException])
          .message === "Got the exception molecule.core.input.exception.InputMoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}