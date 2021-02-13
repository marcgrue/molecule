package molecule.tests.core.input1

import java.util.Date
import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.setup.TestSpec


class Input1Date extends TestSpec {

  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.date$ insert List(
        (str1, Some(date1)),
        (str2, Some(date2)),
        (str3, Some(date3)),
        (str4, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.date(?))

        inputMolecule(Nil).get === Nil

        inputMolecule(List(date1)).get === List(date1)
        inputMolecule(List(date1, date1)).get === List(date1)
        inputMolecule(List(date1, date2)).get.sorted === List(date1, date2)

        // Varargs
        inputMolecule(date1).get === List(date1)
        inputMolecule(date1, date2).get.sorted === List(date1, date2)

        // `or`
        inputMolecule(date1 or date2).get.sorted === List(date1, date2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.date.not(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)

        inputMolecule(List(date1)).get.sorted === List(date2, date3)
        inputMolecule(List(date1, date1)).get.sorted === List(date2, date3)
        inputMolecule(List(date1, date2)).get.sorted === List(date3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.date.>(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)
        inputMolecule(List(date2)).get.sorted === List(date3)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.date.>=(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)
        inputMolecule(List(date2)).get.sorted === List(date2, date3)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.date.<(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)
        inputMolecule(List(date2)).get === List(date1)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.date.<=(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)
        inputMolecule(List(date2)).get.sorted === List(date1, date2)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new OneSetup {
        val inputMolecule = m(Ns.str.date_(?))

        inputMolecule(Nil).get === List(str4)
        inputMolecule(List(date1)).get === List(str1)
        inputMolecule(List(date1, date1)).get === List(str1)
        inputMolecule(List(date1, date2)).get.sorted === List(str1, str2)
      }


      "!=" in new OneSetup {
        val inputMolecule = m(Ns.str.date_.not(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(date1)).get.sorted === List(str2, str3)
        inputMolecule(List(date1, date1)).get.sorted === List(str2, str3)
        inputMolecule(List(date1, date2)).get.sorted === List(str3)
      }


      ">" in new OneSetup {
        val inputMolecule = m(Ns.str.date_.>(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(date2)).get.sorted === List(str3)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new OneSetup {
        val inputMolecule = m(Ns.str.date_.>=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(date2)).get.sorted === List(str2, str3)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new OneSetup {
        val inputMolecule = m(Ns.str.date_.<(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(date2)).get === List(str1)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new OneSetup {
        val inputMolecule = m(Ns.str.date_.<=(?))

        inputMolecule(Nil).get.sorted === List(str1, str2, str3)
        inputMolecule(List(date2)).get.sorted === List(str1, str2)
        (inputMolecule(List(date2, date3)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.date.dates$ insert List(
        (date1, Some(Set(date1, date2))),
        (date2, Some(Set(date2, date3))),
        (date3, Some(Set(date3, date4))),
        (date4, Some(Set(date4, date5))),
        (date5, Some(Set(date4, date5, date6))),
        (date6, None)
      )
    }

    "Mandatory" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.date.dates(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Date]())).get === Nil


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(date1))).get === List((date1, Set(date1, date2)))
        inputMolecule(List(Set(date1, date1))).get === List((date1, Set(date1, date2)))

        inputMolecule(List(Set(date1, date2))).get === List((date1, Set(date1, date2)))
        inputMolecule(List(Set(date1, date3))).get === Nil
        inputMolecule(List(Set(date2, date3))).get === List((date2, Set(date3, date2)))
        inputMolecule(List(Set(date4, date5))).get === List((date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        // 1 arg
        inputMolecule(Set(date1)).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1, date1)).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1, date2)).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1, date3)).get === Nil
        inputMolecule(Set(date2, date3)).get === List((date2, Set(date3, date2)))
        inputMolecule(Set(date4, date5)).get === List((date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(date1, date2), Set[Date]())).get === List((date1, Set(date1, date2)))
        inputMolecule(List(Set(date1), Set(date1))).get === List((date1, Set(date1, date2)))
        inputMolecule(List(Set(date1), Set(date2))).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(List(Set(date1), Set(date3))).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(List(Set(date1, date2), Set(date3))).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(List(Set(date1), Set(date2, date3))).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(List(Set(date1), Set(date2), Set(date3))).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(List(Set(date1, date2), Set(date3, date4))).get === List((date1, Set(date1, date2)), (date3, Set(date4, date3)))


        // Multiple varargs
        inputMolecule(Set(date1, date2), Set[Date]()).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1), Set(date1)).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1), Set(date2)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(Set(date1), Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1, date2), Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1), Set(date2, date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(Set(date1), Set(date2), Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1, date2), Set(date3, date4)).get === List((date1, Set(date1, date2)), (date3, Set(date4, date3)))

        // `or`
        inputMolecule(Set(date1, date2) or Set[Date]()).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1) or Set(date1)).get === List((date1, Set(date1, date2)))
        inputMolecule(Set(date1) or Set(date2)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(Set(date1) or Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1, date2) or Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1) or Set(date2, date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)))
        inputMolecule(Set(date1) or Set(date2) or Set(date3)).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)))
        inputMolecule(Set(date1, date2) or Set(date3, date4)).get === List((date1, Set(date1, date2)), (date3, Set(date4, date3)))
      }


      "!=" in new CoreSetup {

        val all = List(
          (date1, Set(date1, date2, date3)),
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )
        Ns.date.dates insert all
        val inputMolecule = m(Ns.date.dates.not(?)) // or m(Ns.date.dates.!=(?))

        inputMolecule(Nil).get === all
        inputMolecule(Set[Date]()).get === all

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(date1).get === ...
        // inputMolecule(List(date1)).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(date1)).get === List(
          // (date1, Set(date1, date2, date3)),  // date1 match
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )
        // Same as
        inputMolecule(List(Set(date1))).get === List(
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )

        inputMolecule(Set(date2)).get === List(
          // (date1, Set(date1, date2, date3)),  // date2 match
          // (date2, Set(date2, date3, date4)),  // date2 match
          (date3, Set(date3, date4, date5))
        )

        inputMolecule(Set(date3)).get === Nil // date3 match all


        inputMolecule(Set(date1), Set(date2)).get === List(
          // (date1, Set(date1, date2, date3)),  // date1 match, date2 match
          // (date2, Set(date2, date3, date4)),  // date2 match
          (date3, Set(date3, date4, date5))
        )
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(date1, date2)).get === List(
          // (date1, Set(date1, date2, date3)),  // date1 AND date2 match
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )


        inputMolecule(Set(date1), Set(date3)).get === Nil // date3 match all
        inputMolecule(Set(date1, date3)).get === List(
          // (date1, Set(date1, date2, date3)),  // date1 AND date3 match
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )


        inputMolecule(Set(date1), Set(date2), Set(date3)).get === Nil // date3 match all
        inputMolecule(Set(date1, date2, date3)).get === List(
          // (date1, Set(date1, date2, date3)),  // date1 AND date2 AND date3 match
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )


        inputMolecule(Set(date1, date2), Set(date1)).get === List(
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )
        inputMolecule(Set(date1, date2), Set(date2)).get === List(
          (date3, Set(date3, date4, date5))
        )
        inputMolecule(Set(date1, date2), Set(date3)).get === Nil
        inputMolecule(Set(date1, date2), Set(date4)).get === Nil
        inputMolecule(Set(date1, date2), Set(date5)).get === List(
          (date2, Set(date2, date3, date4))
        )

        inputMolecule(Set(date1, date2), Set(date2, date3)).get === List(
          (date3, Set(date3, date4, date5))
        )
        inputMolecule(Set(date1, date2), Set(date4, date5)).get === List(
          (date2, Set(date2, date3, date4))
        )
      }
      

      ">" in new ManySetup {
        val inputMolecule = m(Ns.date.dates.>(?))

        inputMolecule(Nil).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))
        inputMolecule(List(Set[Date]())).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        // (date3, date4), (date4, date5), (date4, date5, date6)
        inputMolecule(List(Set(date2))).get === List((date2, Set(date3)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.date.dates.>=(?))

        inputMolecule(Nil).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))
        inputMolecule(List(Set[Date]())).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        // (date2, date4), (date3, date4), (date4, date5), (date4, date5, date6)
        inputMolecule(List(Set(date2))).get === List((date1, Set(date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.date.dates.<(?))

        inputMolecule(Nil).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))
        inputMolecule(List(Set[Date]())).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        inputMolecule(List(Set(date2))).get === List((date1, Set(date1)))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.date.dates.<=(?))

        inputMolecule(Nil).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))
        inputMolecule(List(Set[Date]())).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date3)), (date4, Set(date4, date5)), (date5, Set(date4, date6, date5)))

        inputMolecule(List(Set(date2))).get === List((date1, Set(date1, date2)), (date2, Set(date2)))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Mandatory, single attr coalesce" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.dates(?))

        inputMolecule(Nil).get === Nil
        inputMolecule(List(Set[Date]())).get === Nil

        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(date1))).get === List(Set(date1, date2))
        inputMolecule(List(Set(date2))).get === List(Set(date1, date2, date3)) // (date1, date2) + (date2, date3)
        inputMolecule(List(Set(date3))).get === List(Set(date2, date3, date4)) // (date2, date3) + (date3, date4)

        inputMolecule(List(Set(date1, date2))).get === List(Set(date1, date2))
        inputMolecule(List(Set(date1, date3))).get === Nil
        inputMolecule(List(Set(date2, date3))).get === List(Set(date2, date3))
        inputMolecule(List(Set(date4, date5))).get === List(Set(date4, date5, date6)) // (date4, date5) + (date4, date5, date6)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(date1), Set(date1))).get === List(Set(date1, date2))
        inputMolecule(List(Set(date1), Set(date2))).get === List(Set(date1, date2, date3)) // (date1, date2) + (date2, date3)
        inputMolecule(List(Set(date1), Set(date3))).get === List(Set(date1, date4, date3, date2)) // (date1, date2) + (date2, date3) + (date3, date4)
        inputMolecule(List(Set(date2), Set(date3))).get === List(Set(date1, date2, date3, date4)) // (date1, date2) + (date2, date3) + (date3, date4)

        inputMolecule(List(Set(date1, date2), Set(date3))).get === List(Set(date1, date2, date3, date4)) // (date1, date2) + (date2, date3) + (date3, date4)
        inputMolecule(List(Set(date1), Set(date2, date3))).get === List(Set(date1, date3, date2)) // (date1, date2) + (date2, date3)
        inputMolecule(List(Set(date1), Set(date2), Set(date3))).get === List(Set(date1, date2, date3, date4)) // (date1, date2) + (date2, date3) + (date3, date4)
      }


      "!=" in new CoreSetup {

        val all = List(
          (date1, Set(date1, date2, date3)),
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )
        Ns.date.dates insert all
        val inputMolecule = m(Ns.dates.not(?)) // or m(Ns.date.dates.!=(?))

        inputMolecule(Nil).get === List(Set(date1, date2, date3, date4, date5))
        inputMolecule(Set[Date]()).get === List(Set(date1, date2, date3, date4, date5))

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(date1).get === ...

        // Set semantics omit the whole set with one or more matching values
        inputMolecule(Set(date1)).get === List(Set(date2, date3, date4, date5))
        // Same as
        inputMolecule(List(Set(date1))).get === List(Set(date2, date3, date4, date5))

        inputMolecule(Set(date2)).get === List(Set(date3, date4, date5))
        inputMolecule(Set(date3)).get === Nil // date3 match all

        inputMolecule(Set(date1), Set(date2)).get === List(Set(date3, date4, date5))
        inputMolecule(Set(date1), Set(date3)).get === Nil // date3 match all

        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(date1, date2)).get === List(Set(date2, date3, date4, date5))
        inputMolecule(Set(date1, date3)).get === List(Set(date2, date3, date4, date5))

        inputMolecule(Set(date1), Set(date2), Set(date3)).get === Nil // date3 match all
        inputMolecule(Set(date1, date2, date3)).get === List(Set(date2, date3, date4, date5))

        inputMolecule(Set(date1, date2), Set(date1)).get === List(Set(date2, date3, date4, date5))
        inputMolecule(Set(date1, date2), Set(date2)).get === List(Set(date3, date4, date5))
        inputMolecule(Set(date1, date2), Set(date3)).get === Nil
        inputMolecule(Set(date1, date2), Set(date4)).get === Nil
        inputMolecule(Set(date1, date2), Set(date5)).get === List(Set(date2, date3, date4))

        inputMolecule(Set(date1, date2), Set(date2, date3)).get === List(Set(date3, date4, date5))
        inputMolecule(Set(date1, date2), Set(date4, date5)).get === List(Set(date2, date3, date4))
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.dates.>(?))

        inputMolecule(Nil).get === List(Set(date1, date2, date3, date4, date5, date6))
        inputMolecule(List(Set[Date]())).get === List(Set(date1, date2, date3, date4, date5, date6))

        inputMolecule(List(Set(date2))).get === List(Set(date3, date4, date5, date6))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.dates.>=(?))

        inputMolecule(Nil).get === List(Set(date1, date2, date3, date4, date5, date6))
        inputMolecule(List(Set[Date]())).get === List(Set(date1, date2, date3, date4, date5, date6))

        inputMolecule(List(Set(date2))).get === List(Set(date2, date3, date4, date5, date6))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.dates.<(?))

        inputMolecule(Nil).get === List(Set(date1, date2, date3, date4, date5, date6))
        inputMolecule(List(Set[Date]())).get === List(Set(date1, date2, date3, date4, date5, date6))

        inputMolecule(List(Set(date2))).get === List(Set(date1))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.dates.<=(?))

        inputMolecule(Nil).get === List(Set(date1, date2, date3, date4, date5, date6))
        inputMolecule(List(Set[Date]())).get === List(Set(date1, date2, date3, date4, date5, date6))

        inputMolecule(List(Set(date2))).get === List(Set(date1, date2))

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }


    "Tacit" >> {

      "Eq" in new ManySetup {
        val inputMolecule = m(Ns.date.dates_(?))

        inputMolecule(Nil).get === List(date6)
        inputMolecule(List(Set[Date]())).get === List(date6)


        // Values of 1 Set match values of 1 card-many attribute at a time

        inputMolecule(List(Set(date1))).get === List(date1)
        inputMolecule(List(Set(date2))).get.sorted === List(date1, date2)
        inputMolecule(List(Set(date3))).get.sorted === List(date2, date3)

        inputMolecule(List(Set(date1, date1))).get === List(date1)
        inputMolecule(List(Set(date1, date2))).get === List(date1)
        inputMolecule(List(Set(date1, date3))).get === Nil
        inputMolecule(List(Set(date2, date3))).get === List(date2)
        inputMolecule(List(Set(date4, date5))).get.sorted === List(date4, date5)


        // Values of each Set matches values of 1 card-many attributes respectively

        inputMolecule(List(Set(date1, date2), Set[Date]())).get === List(date1)
        inputMolecule(List(Set(date1), Set(date1))).get === List(date1)
        inputMolecule(List(Set(date1), Set(date2))).get.sorted === List(date1, date2)
        inputMolecule(List(Set(date1), Set(date3))).get.sorted === List(date1, date2, date3)

        inputMolecule(List(Set(date1, date2), Set(date3))).get.sorted === List(date1, date2, date3)
        inputMolecule(List(Set(date1), Set(date2, date3))).get.sorted === List(date1, date2)
        inputMolecule(List(Set(date1), Set(date2), Set(date3))).get.sorted === List(date1, date2, date3)

        inputMolecule(List(Set(date1, date2), Set(date3, date4))).get.sorted === List(date1, date3)
      }


      "!=" in new CoreSetup {

        val all = List(
          (date1, Set(date1, date2, date3)),
          (date2, Set(date2, date3, date4)),
          (date3, Set(date3, date4, date5))
        )
        Ns.date.dates insert all
        val inputMolecule = m(Ns.date.dates_.not(?)) // or m(Ns.date.dates.!=(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3)
        inputMolecule(Set[Date]()).get.sorted === List(date1, date2, date3)

        // Vararg/List(args*) syntax/semantics not available for card-many attributes of input molecules
        // inputMolecule(date1).get === ...
        // inputMolecule(List(date1)).get === ...

        // Set semantics omit the whole set with one or more matching values

        inputMolecule(Set(date1)).get.sorted === List(date2, date3)
        // Same as
        inputMolecule(List(Set(date1))).get.sorted === List(date2, date3)

        inputMolecule(Set(date2)).get === List(date3)
        inputMolecule(Set(date3)).get === Nil // date3 match all


        inputMolecule(Set(date1), Set(date2)).get === List(date3)
        // Multiple values in a Set matches matches set-wise
        inputMolecule(Set(date1, date2)).get.sorted === List(date2, date3)

        inputMolecule(Set(date1), Set(date3)).get === Nil // date3 match all
        inputMolecule(Set(date1, date3)).get.sorted === List(date2, date3)

        inputMolecule(Set(date1), Set(date2), Set(date3)).get === Nil // date3 match all
        inputMolecule(Set(date1, date2, date3)).get.sorted === List(date2, date3)

        inputMolecule(Set(date1, date2), Set(date1)).get.sorted === List(date2, date3)
        inputMolecule(Set(date1, date2), Set(date2)).get === List(date3)
        inputMolecule(Set(date1, date2), Set(date3)).get === Nil
        inputMolecule(Set(date1, date2), Set(date4)).get === Nil
        inputMolecule(Set(date1, date2), Set(date5)).get === List(date2)

        inputMolecule(Set(date1, date2), Set(date2, date3)).get === List(date3)
        inputMolecule(Set(date1, date2), Set(date4, date5)).get === List(date2)
      }


      ">" in new ManySetup {
        val inputMolecule = m(Ns.date.dates_.>(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3, date4, date5)
        inputMolecule(List(Set[Date]())).get.sorted === List(date1, date2, date3, date4, date5)

        // (date3, date4), (date4, date5), (date4, date5, date6)
        inputMolecule(List(Set(date2))).get.sorted === List(date2, date3, date4, date5)

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      ">=" in new ManySetup {
        val inputMolecule = m(Ns.date.dates_.>=(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3, date4, date5)
        inputMolecule(List(Set[Date]())).get.sorted === List(date1, date2, date3, date4, date5)

        // (date2, date4), (date3, date4), (date4, date5), (date4, date5, date6)
        inputMolecule(List(Set(date2))).get.sorted === List(date1, date2, date3, date4, date5)

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<" in new ManySetup {
        val inputMolecule = m(Ns.date.dates_.<(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3, date4, date5)
        inputMolecule(List(Set[Date]())).get.sorted === List(date1, date2, date3, date4, date5)

        inputMolecule(List(Set(date2))).get === List(date1)

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "<=" in new ManySetup {
        val inputMolecule = m(Ns.date.dates_.<=(?))

        inputMolecule(Nil).get.sorted === List(date1, date2, date3, date4, date5)
        inputMolecule(List(Set[Date]())).get.sorted === List(date1, date2, date3, date4, date5)

        inputMolecule(List(Set(date2))).get.sorted === List(date1, date2)

        (inputMolecule(List(Set(date2, date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."

        (inputMolecule(List(Set(date2), Set(date3))).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }
    }
  }
}