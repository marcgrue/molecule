package molecule.tests.core.equality

import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.setup.TestSpec

class ApplyEnum extends TestSpec {


  "Card one" >> {

    class OneSetup extends CoreSetup {
      Ns.int.enum$ insert List(
        (1, Some("enum1")),
        (2, Some("enum2")),
        (3, Some("enum3")),
        (4, None)
      )
    }

    // OR semantics only for card-one attributes

    "Mandatory" in new OneSetup {

      // Varargs
      Ns.enum.apply("enum1").get === List("enum1")
      Ns.enum.apply("enum2").get === List("enum2")
      Ns.enum.apply("enum1", "enum2").get === List("enum2", "enum1")

      // `or`
      Ns.enum.apply("enum1" or "enum2").get === List("enum2", "enum1")
      Ns.enum.apply("enum1" or "enum2" or "enum3").get === List("enum3", "enum2", "enum1")

      // Seq
      Ns.enum.apply().get === Nil
      Ns.enum.apply(Nil).get === Nil
      Ns.enum.apply(List("enum1")).get === List("enum1")
      Ns.enum.apply(List("enum2")).get === List("enum2")
      Ns.enum.apply(List("enum1", "enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(List("enum1"), List("enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(List("enum1", "enum2"), List("enum3")).get === List("enum3", "enum2", "enum1")
      Ns.enum.apply(List("enum1"), List("enum2", "enum3")).get === List("enum3", "enum2", "enum1")
      Ns.enum.apply(List("enum1", "enum2", "enum3")).get === List("enum3", "enum2", "enum1")

      // Applying a non-existing enum value ("zzz") won't compile!
      expectCompileError(
        """m(Ns.enum("zzz"))""",
        """
          |molecule.core.transform.exception.Dsl2ModelException: 'zzz' is not among available enum values of attribute :Ns/enum:
          |  enum0
          |  enum1
          |  enum2
          |  enum3
          |  enum4
          |  enum5
          |  enum6
          |  enum7
          |  enum8
          |  enum9
        """)
    }


    "Tacit" in new OneSetup {

      // Varargs
      Ns.int.enum_.apply("enum1").get === List(1)
      Ns.int.enum_.apply("enum2").get === List(2)
      Ns.int.enum_.apply("enum1", "enum2").get === List(1, 2)

      // `or`
      Ns.int.enum_.apply("enum1" or "enum2").get === List(1, 2)
      Ns.int.enum_.apply("enum1" or "enum2" or "enum3").get === List(1, 2, 3)

      // Seq
      Ns.int.enum_.apply().get === List(4)
      Ns.int.enum_.apply(Nil).get === List(4)
      Ns.int.enum_.apply(List("enum1")).get === List(1)
      Ns.int.enum_.apply(List("enum2")).get === List(2)
      Ns.int.enum_.apply(List("enum1", "enum2")).get === List(1, 2)
      Ns.int.enum_.apply(List("enum1"), List("enum2")).get === List(1, 2)
      Ns.int.enum_.apply(List("enum1", "enum2"), List("enum3")).get === List(1, 2, 3)
      Ns.int.enum_.apply(List("enum1"), List("enum2", "enum3")).get === List(1, 2, 3)
      Ns.int.enum_.apply(List("enum1", "enum2", "enum3")).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    class ManySetup extends CoreSetup {
      Ns.int.enums$ insert List(
        (1, Some(Set("enum1", "enum2"))),
        (2, Some(Set("enum2", "enum3"))),
        (3, Some(Set("enum3", "enum4"))),
        (4, None)
      )
    }

    "Mandatory" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.enums.apply("enum1").get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply("enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply("enum1", "enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))

      // `or`
      Ns.int.enums.apply("enum1" or "enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply("enum1" or "enum2" or "enum3").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      // Seq
      Ns.int.enums.apply().get === Nil
      Ns.int.enums.apply(Nil).get === Nil
      Ns.int.enums.apply(List("enum1")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(List("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1", "enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1"), List("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1", "enum2"), List("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(List("enum1"), List("enum2", "enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(List("enum1", "enum2", "enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))


      // AND semantics

      // Set
      Ns.int.enums.apply(Set[String]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.int.enums.apply(Set("enum1")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(Set("enum1", "enum2")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum1", "enum3")).get === Nil
      Ns.int.enums.apply(Set("enum2", "enum3")).get === List((2, Set("enum2", "enum3")))
      Ns.int.enums.apply(Set("enum1", "enum2", "enum3")).get === Nil

      Ns.int.enums.apply(Set("enum1", "enum2"), Set[String]()).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum4")).get === List((1, Set("enum1", "enum2")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum2"), Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum2", "enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum2", "enum4")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum3", "enum4")).get === List((1, Set("enum1", "enum2")), (3, Set("enum3", "enum4")))

      // `and`
      Ns.int.enums.apply("enum1" and "enum2").get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply("enum1" and "enum3").get === Nil
    }


    "Mandatory, single attr coalesce" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.enums.apply("enum1").get === List(Set("enum1", "enum2"))
      Ns.enums.apply("enum2").get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply("enum1", "enum2").get === List(Set("enum1", "enum3", "enum2"))

      // `or`
      Ns.enums.apply("enum1" or "enum2").get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply("enum1" or "enum2" or "enum3").get === List(Set("enum1", "enum4", "enum3", "enum2"))

      // Seq
      Ns.enums.apply().get === Nil
      Ns.enums.apply(Nil).get === Nil
      Ns.enums.apply(List("enum1")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(List("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1", "enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1"), List("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1", "enum2"), List("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
      Ns.enums.apply(List("enum1"), List("enum2", "enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
      Ns.enums.apply(List("enum1", "enum2", "enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))


      // AND semantics

      // Set
      Ns.enums.apply(Set[String]()).get === Nil // entities with no card-many values asserted can't also return values
      Ns.enums.apply(Set("enum1")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(Set("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(Set("enum1", "enum2")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(Set("enum1", "enum3")).get === Nil
      Ns.enums.apply(Set("enum2", "enum3")).get === List(Set("enum2", "enum3"))
      Ns.enums.apply(Set("enum1", "enum2", "enum3")).get === Nil

      Ns.enums.apply(Set("enum1", "enum2"), Set("enum2")).get === List(Set("enum1", "enum2", "enum3"))
      Ns.enums.apply(Set("enum1", "enum2"), Set("enum3")).get === List(Set("enum1", "enum2", "enum3", "enum4"))
      Ns.enums.apply(Set("enum1", "enum2"), Set("enum4")).get === List(Set("enum1", "enum2", "enum3", "enum4"))
      Ns.enums.apply(Set("enum1", "enum2"), Set("enum2"), Set("enum3")).get === List(Set("enum1", "enum2", "enum3", "enum4"))

      Ns.enums.apply(Set("enum1", "enum2"), Set("enum2", "enum3")).get === List(Set("enum1", "enum2", "enum3"))
      Ns.enums.apply(Set("enum1", "enum2"), Set("enum2", "enum4")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(Set("enum1", "enum2"), Set("enum3", "enum4")).get === List(Set("enum1", "enum2", "enum3", "enum4"))


      // Explicit `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.enums.apply("enum1" and "enum2").get === List(Set("enum1", "enum2"))
      Ns.enums.apply("enum1" and "enum3").get === Nil
    }


    "Tacit" in new ManySetup {

      // OR semantics

      // Varargs
      Ns.int.enums_.apply("enum1").get === List(1)
      Ns.int.enums_.apply("enum2").get === List(1, 2)
      Ns.int.enums_.apply("enum1", "enum2").get === List(1, 2)

      // `or`
      Ns.int.enums_.apply("enum1" or "enum2").get === List(1, 2)
      Ns.int.enums_.apply("enum1" or "enum2" or "enum3").get === List(1, 2, 3)

      // Seq
      Ns.int.enums_.apply().get === List(4) // entities with no card-many values asserted
      Ns.int.enums_.apply(Nil).get === List(4)
      Ns.int.enums_.apply(List("enum1")).get === List(1)
      Ns.int.enums_.apply(List("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1", "enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1"), List("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1", "enum2"), List("enum3")).get === List(1, 2, 3)
      Ns.int.enums_.apply(List("enum1"), List("enum2", "enum3")).get === List(1, 2, 3)
      Ns.int.enums_.apply(List("enum1", "enum2", "enum3")).get === List(1, 2, 3)


      // AND semantics

      // Set
      Ns.int.enums_.apply(Set[String]()).get === List(4)
      Ns.int.enums_.apply(Set("enum1")).get === List(1)
      Ns.int.enums_.apply(Set("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(Set("enum1", "enum2")).get === List(1)
      Ns.int.enums_.apply(Set("enum1", "enum3")).get === Nil
      Ns.int.enums_.apply(Set("enum2", "enum3")).get === List(2)
      Ns.int.enums_.apply(Set("enum1", "enum2", "enum3")).get === Nil

      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum3")).get === List(1, 2, 3)
      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum4")).get === List(1, 3)
      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum2"), Set("enum3")).get === List(1, 2, 3)

      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum2", "enum3")).get === List(1, 2)
      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum2", "enum4")).get === List(1)
      Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum3", "enum4")).get === List(1, 3)


      // `and` (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.enums_.apply("enum1" and "enum2").get === List(1)
      Ns.int.enums_.apply("enum1" and "enum3").get === Nil
    }


    "Variable resolution" in new ManySetup {

      val seq0 = Nil
      val set0 = Set[String]()

      val l1 = List(enum1)
      val l2 = List(enum2)

      val s1 = Set(enum1)
      val s2 = Set(enum2)

      val l12 = List(enum1, enum2)
      val l23 = List(enum2, enum3)

      val s12 = Set(enum1, enum2)
      val s23 = Set(enum2, enum3)


      // OR semantics

      // Vararg
      Ns.int.enums_.apply(enum1, enum2).get === List(1, 2)

      // `or`
      Ns.int.enums_.apply(enum1 or enum2).get === List(1, 2)

      // Seq
      Ns.int.enums_.apply(seq0).get === List(4)
      Ns.int.enums_.apply(List(enum1), List(enum2)).get === List(1, 2)
      Ns.int.enums_.apply(l1, l2).get === List(1, 2)
      Ns.int.enums_.apply(List(enum1, enum2)).get === List(1, 2)
      Ns.int.enums_.apply(l12).get === List(1, 2)


      // AND semantics

      // Set
      Ns.int.enums_.apply(set0).get === List(4)

      Ns.int.enums_.apply(Set(enum1)).get === List(1)
      Ns.int.enums_.apply(s1).get === List(1)

      Ns.int.enums_.apply(Set(enum2)).get === List(1, 2)
      Ns.int.enums_.apply(s2).get === List(1, 2)

      Ns.int.enums_.apply(Set(enum1, enum2)).get === List(1)
      Ns.int.enums_.apply(s12).get === List(1)

      Ns.int.enums_.apply(Set(enum2, enum3)).get === List(2)
      Ns.int.enums_.apply(s23).get === List(2)

      Ns.int.enums_.apply(Set(enum1, enum2), Set(enum2, enum3)).get === List(1, 2)
      Ns.int.enums_.apply(s12, s23).get === List(1, 2)

      // `and`
      Ns.int.enums_.apply(enum1 and enum2).get === List(1)
    }
  }
}