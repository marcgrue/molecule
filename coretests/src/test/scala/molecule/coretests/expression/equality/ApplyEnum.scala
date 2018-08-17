package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException
import molecule.util.expectCompileError

class ApplyEnum extends ApplyBase {

  val l1 = List("enum1")
  val l2 = List("enum2")
  val l3 = List("enum3")

  val s1 = Set("enum1")
  val s2 = Set("enum2")
  val s3 = Set("enum3")

  val l11 = List("enum1", "enum1")
  val l12 = List("enum1", "enum2")
  val l13 = List("enum1", "enum3")
  val l23 = List("enum2", "enum3")

  val s12 = Set("enum1", "enum2")
  val s13 = Set("enum1", "enum3")
  val s23 = Set("enum2", "enum3")

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.enum.apply("enum1").get === List("enum1")
      Ns.enum.apply("enum2").get === List("enum2")
      Ns.enum.apply("enum1", "enum2").get === List("enum2", "enum1")

      // Explicit `or` semantics
      Ns.enum.apply("enum1" or "enum2").get === List("enum2", "enum1")
      Ns.enum.apply("enum1" or "enum2" or "enum3").get === List("enum3", "enum2", "enum1")

      // Iterable: List - OR semantics
      Ns.enum.apply(List("enum1")).get === List("enum1")
      Ns.enum.apply(List("enum2")).get === List("enum2")
      Ns.enum.apply(List("enum1", "enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(List("enum1"), List("enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(List("enum1", "enum2"), List("enum3")).get === List("enum3", "enum2", "enum1")
      Ns.enum.apply(List("enum1"), List("enum2", "enum3")).get === List("enum3", "enum2", "enum1")

      // mixing Iterable types and value/variable ok
      Ns.enum.apply(List(enum1), Set("enum2", enum3)).get === List("enum3", "enum2", "enum1")

      // Iterable: Set
      Ns.enum.apply(Set("enum1")).get === List("enum1")
      Ns.enum.apply(Set("enum2")).get === List("enum2")
      Ns.enum.apply(Set("enum1", "enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(Set("enum1"), Set("enum2")).get === List("enum2", "enum1")
      Ns.enum.apply(Set("enum1", "enum2"), Set("enum3")).get === List("enum3", "enum2", "enum1")
      Ns.enum.apply(Set("enum1"), Set("enum2", "enum3")).get === List("enum3", "enum2", "enum1")


      // Input

      val inputMolecule = m(Ns.enum(?))

      inputMolecule.apply("enum1").get === List("enum1")
      inputMolecule.apply("enum2").get === List("enum2")

      // Only distinct values applied
      inputMolecule.apply("enum1", "enum1").get === List("enum1")
      inputMolecule.apply("enum1", "enum2").get === List("enum2", "enum1")

      inputMolecule.apply(List("enum1")).get === List("enum1")
      inputMolecule.apply(List("enum1", "enum1")).get === List("enum1")
      inputMolecule.apply(List("enum1", "enum2")).get === List("enum2", "enum1")

      inputMolecule.apply(Set("enum1")).get === List("enum1")
      inputMolecule.apply(Set("enum1", "enum2")).get === List("enum2", "enum1")

      inputMolecule.apply("enum1" or "enum1").get === List("enum1")
      inputMolecule.apply("enum1" or "enum2").get === List("enum2", "enum1")
      inputMolecule.apply("enum1" or "enum2" or "enum3").get === List("enum3", "enum2", "enum1")


      // Applying a non-existing enum value ("zzz") won't compile!
      expectCompileError(
        """m(Ns.enum("zzz"))""",
        """
          |[Dsl2Model:validateStaticEnums] 'zzz' is not among available enum values of attribute :ns/enum:
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

      // Vararg
      Ns.int.enum_.apply("enum1").get === List(1)
      Ns.int.enum_.apply("enum2").get === List(2)
      Ns.int.enum_.apply("enum1", "enum2").get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.enum_.apply("enum1" or "enum2").get === List(1, 2)
      Ns.int.enum_.apply("enum1" or "enum2" or "enum3").get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.enum_.apply(List("enum1")).get === List(1)
      Ns.int.enum_.apply(List("enum2")).get === List(2)
      Ns.int.enum_.apply(List("enum1", "enum2")).get === List(1, 2)
      Ns.int.enum_.apply(List("enum1"), List("enum2")).get === List(1, 2)
      Ns.int.enum_.apply(List("enum1", "enum2"), List("enum3")).get === List(1, 2, 3)
      Ns.int.enum_.apply(List("enum1"), List("enum2", "enum3")).get === List(1, 2, 3)

      // Iterable: Set
      Ns.int.enum_.apply(Set("enum1")).get === List(1)
      Ns.int.enum_.apply(Set("enum2")).get === List(2)
      Ns.int.enum_.apply(Set("enum1", "enum2")).get === List(1, 2)
      Ns.int.enum_.apply(Set("enum1"), Set("enum2")).get === List(1, 2)
      Ns.int.enum_.apply(Set("enum1", "enum2"), Set("enum3")).get === List(1, 2, 3)
      Ns.int.enum_.apply(Set("enum1"), Set("enum2", "enum3")).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.enum_(?))

      inputMolecule.apply("enum1").get === List(1)
      inputMolecule.apply("enum2").get === List(2)

      inputMolecule.apply("enum1", "enum1").get === List(1)
      inputMolecule.apply("enum1", "enum2").get === List(1, 2)

      inputMolecule.apply(List("enum1")).get === List(1)
      inputMolecule.apply(List("enum1", "enum1")).get === List(1)
      inputMolecule.apply(List("enum1", "enum2")).get === List(1, 2)

      inputMolecule.apply(Set("enum1")).get === List(1)
      inputMolecule.apply(Set("enum1", "enum2")).get === List(1, 2)

      inputMolecule.apply("enum1" or "enum1").get === List(1)
      inputMolecule.apply("enum1" or "enum2").get === List(1, 2)
      inputMolecule.apply("enum1" or "enum2" or "enum3").get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg
      Ns.enums.apply("enum1").get === List(Set("enum1", "enum2"))
      Ns.enums.apply("enum2").get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply("enum1", "enum2").get === List(Set("enum1", "enum3", "enum2"))

      // Explicit `or` semantics
      Ns.enums.apply("enum1" or "enum2").get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply("enum1" or "enum2" or "enum3").get === List(Set("enum1", "enum4", "enum3", "enum2"))

      // Iterable: List - OR semantics
      Ns.enums.apply(List("enum1")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(List("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1", "enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1"), List("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(List("enum1", "enum2"), List("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
      Ns.enums.apply(List("enum1"), List("enum2", "enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))

      // mixing Iterable types and value/variable ok
      Ns.enums.apply(List(enum1), Set("enum2", enum3)).get === List(Set("enum1", "enum4", "enum3", "enum2"))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.enums.apply(Set("enum1")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(Set("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      Ns.enums.apply(Set("enum1", "enum2")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(Set("enum1", "enum3")).get === Nil
      Ns.enums.apply(Set("enum2", "enum3")).get === List(Set("enum2", "enum3"))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.enums.apply(Set("enum1", "enum2"), Set("enum3"))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Enum Atom (mandatory)] Can only apply a single Set of values for enum attribute :ns.enums"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.enums(enum1 and enum2).get === List(Set(enum1, enum2))
      Ns.enums(enum1 and enum3).get === Nil


      // Input

      val inputMolecule = m(Ns.enums(?))

      // AND semantics when applying "enum1" Set of values matching attribute values of "enum1" entity

      inputMolecule.apply(Set("enum1")).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set("enum2")).get === List(Set("enum1", "enum3", "enum2"))

      inputMolecule.apply(Set("enum1", "enum2")).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set("enum1", "enum3")).get === Nil
      inputMolecule.apply(Set("enum2", "enum3")).get === List(Set("enum2", "enum3"))
      inputMolecule.apply(Set("enum1", "enum2", "enum3")).get === Nil

      inputMolecule.apply(List(Set("enum1"))).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(List(Set("enum2"))).get === List(Set("enum1", "enum3", "enum2"))
      inputMolecule.apply(List(Set("enum1", "enum2"))).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(List(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(List(Set("enum2", "enum3"))).get === List(Set("enum2", "enum3"))
      inputMolecule.apply(List(Set("enum1", "enum2", "enum3"))).get === Nil

      inputMolecule.apply(Set(Set("enum1"))).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set(Set("enum2"))).get === List(Set("enum1", "enum3", "enum2"))
      inputMolecule.apply(Set(Set("enum1", "enum2"))).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(Set(Set("enum2", "enum3"))).get === List(Set("enum2", "enum3"))
      inputMolecule.apply(Set(Set("enum1", "enum2", "enum3"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("enum1"), Set("enum1")).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set("enum1"), Set("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      inputMolecule.apply(Set("enum1"), Set("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
      inputMolecule.apply(Set("enum2"), Set("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
      inputMolecule.apply(Set("enum1", "enum2"), Set("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))

      inputMolecule.apply(Set("enum1") or Set("enum1")).get === List(Set("enum1", "enum2"))
      inputMolecule.apply(Set("enum1") or Set("enum2")).get === List(Set("enum1", "enum3", "enum2"))
      inputMolecule.apply(Set("enum1") or Set("enum2") or Set("enum3")).get === List(Set("enum1", "enum4", "enum3", "enum2"))
    }


    "Tacit" in new ManySetup {

      // Vararg
      Ns.int.enums_.apply("enum1").get === List(1)
      Ns.int.enums_.apply("enum2").get === List(1, 2)
      Ns.int.enums_.apply("enum1", "enum2").get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.enums_.apply("enum1" or "enum2").get === List(1, 2)
      Ns.int.enums_.apply("enum1" or "enum2" or "enum3").get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.enums_.apply(List("enum1")).get === List(1)
      Ns.int.enums_.apply(List("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1", "enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1"), List("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(List("enum1", "enum2"), List("enum3")).get === List(1, 2, 3)
      Ns.int.enums_.apply(List("enum1"), List("enum2", "enum3")).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.enums_.apply(List(enum1), Set("enum2", enum3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.enums_.apply(Set("enum1")).get === List(1)
      Ns.int.enums_.apply(Set("enum2")).get === List(1, 2)
      Ns.int.enums_.apply(Set("enum1", "enum2")).get === List(1)
      Ns.int.enums_.apply(Set("enum1", "enum3")).get === Nil
      Ns.int.enums_.apply(Set("enum2", "enum3")).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.enums_.apply(Set("enum1", "enum2"), Set("enum3")).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Enum Atom_ (tacit)] Can only apply a single Set of values for enum attribute :ns.enums_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.enums_(enum1 and enum2).get === List(1)
      Ns.int.enums_(enum1 and enum3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.enums_(?))

      // AND semantics when applying "enum1" Set of values matching attribute values of "enum1" entity

      inputMolecule.apply(Set("enum1")).get === List(1)
      inputMolecule.apply(Set("enum2")).get === List(1, 2)

      inputMolecule.apply(Set("enum1", "enum2")).get === List(1)
      inputMolecule.apply(Set("enum1", "enum3")).get === Nil
      inputMolecule.apply(Set("enum2", "enum3")).get === List(2)
      inputMolecule.apply(Set("enum1", "enum2", "enum3")).get === Nil

      inputMolecule.apply(List(Set("enum1"))).get === List(1)
      inputMolecule.apply(List(Set("enum2"))).get === List(1, 2)
      inputMolecule.apply(List(Set("enum1", "enum2"))).get === List(1)
      inputMolecule.apply(List(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(List(Set("enum2", "enum3"))).get === List(2)
      inputMolecule.apply(List(Set("enum1", "enum2", "enum3"))).get === Nil

      inputMolecule.apply(Set(Set("enum1"))).get === List(1)
      inputMolecule.apply(Set(Set("enum2"))).get === List(1, 2)
      inputMolecule.apply(Set(Set("enum1", "enum2"))).get === List(1)
      inputMolecule.apply(Set(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(Set(Set("enum2", "enum3"))).get === List(2)
      inputMolecule.apply(Set(Set("enum1", "enum2", "enum3"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("enum1"), Set("enum1")).get === List(1)
      inputMolecule.apply(Set("enum1"), Set("enum2")).get === List(1, 2)
      inputMolecule.apply(Set("enum1"), Set("enum3")).get === List(1, 2, 3)
      inputMolecule.apply(Set("enum2"), Set("enum3")).get === List(1, 2, 3)
      inputMolecule.apply(Set("enum1", "enum2"), Set("enum3")).get === List(1, 2, 3)

      inputMolecule.apply(Set("enum1") or Set("enum1")).get === List(1)
      inputMolecule.apply(Set("enum1") or Set("enum2")).get === List(1, 2)
      inputMolecule.apply(Set("enum1") or Set("enum2") or Set("enum3")).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg
      Ns.int.enums.apply("enum1").get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply("enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply("enum1", "enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))

      // Explicit `or` semantics
      Ns.int.enums.apply("enum1" or "enum2").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply("enum1" or "enum2" or "enum3").get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      // Iterable: List - OR semantics
      Ns.int.enums.apply(List("enum1")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(List("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1", "enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1"), List("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(List("enum1", "enum2"), List("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(List("enum1"), List("enum2", "enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      // mixing Iterable types and value/variable ok
      Ns.int.enums.apply(List(enum1), Set("enum2", enum3)).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      Ns.int.enums.apply(l1, Set("enum2", enum3)).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.enums.apply(Set("enum1")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      Ns.int.enums.apply(Set("enum1", "enum2")).get === List((1, Set("enum1", "enum2")))
      Ns.int.enums.apply(Set("enum1", "enum3")).get === Nil
      Ns.int.enums.apply(Set("enum2", "enum3")).get === List((2, Set("enum2", "enum3")))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.enums.apply(Set("enum1", "enum2"), Set("enum3")).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Enum Atom (mandatory)] Can only apply a single Set of values for enum attribute :ns.enums"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.enums(enum1 and enum2).get === List((1, Set(enum1, enum2)))
      Ns.int.enums(enum1 and enum3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.enums(?))

      // AND semantics when applying "enum1" Set of values matching attribute values of "enum1" entity

      inputMolecule.apply(Set("enum1")).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))

      inputMolecule.apply(Set("enum1", "enum2")).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set("enum1", "enum3")).get === Nil
      inputMolecule.apply(Set("enum2", "enum3")).get === List((2, Set("enum2", "enum3")))
      inputMolecule.apply(Set("enum1", "enum2", "enum3")).get === Nil

      inputMolecule.apply(List(Set("enum1"))).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(List(Set("enum2"))).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      inputMolecule.apply(List(Set("enum1", "enum2"))).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(List(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(List(Set("enum2", "enum3"))).get === List((2, Set("enum2", "enum3")))
      inputMolecule.apply(List(Set("enum1", "enum2", "enum3"))).get === Nil

      inputMolecule.apply(Set(Set("enum1"))).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set(Set("enum2"))).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      inputMolecule.apply(Set(Set("enum1", "enum2"))).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set(Set("enum1", "enum3"))).get === Nil
      inputMolecule.apply(Set(Set("enum2", "enum3"))).get === List((2, Set("enum2", "enum3")))
      inputMolecule.apply(Set(Set("enum1", "enum2", "enum3"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("enum1"), Set("enum1")).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set("enum1"), Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      inputMolecule.apply(Set("enum1"), Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      inputMolecule.apply(Set("enum2"), Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
      inputMolecule.apply(Set("enum1", "enum2"), Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))

      inputMolecule.apply(Set("enum1") or Set("enum1")).get === List((1, Set("enum1", "enum2")))
      inputMolecule.apply(Set("enum1") or Set("enum2")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")))
      inputMolecule.apply(Set("enum1") or Set("enum2") or Set("enum3")).get === List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.enum.apply(enum1, enum2).get === List("enum2", "enum1")

    Ns.enum.apply(List(enum1, enum2), List(enum3)).get === List("enum3", "enum2", "enum1")
    Ns.enum.apply(l12, l3).get === List("enum3", "enum2", "enum1")

    Ns.enum.apply(Set(enum1, enum2), Set(enum3)).get === List("enum3", "enum2", "enum1")
    Ns.enum.apply(s12, s3).get === List("enum3", "enum2", "enum1")
  }
}