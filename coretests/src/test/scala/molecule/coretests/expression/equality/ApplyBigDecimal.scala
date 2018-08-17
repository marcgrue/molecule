package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyBigDecimal extends ApplyBase {

  val l1 = List(bigDec1)
  val l2 = List(bigDec2)
  val l3 = List(bigDec3)

  val s1 = Set(bigDec1)
  val s2 = Set(bigDec2)
  val s3 = Set(bigDec3)

  val l11 = List(bigDec1, bigDec1)
  val l12 = List(bigDec1, bigDec2)
  val l13 = List(bigDec1, bigDec3)
  val l23 = List(bigDec2, bigDec3)

  val s12 = Set(bigDec1, bigDec2)
  val s13 = Set(bigDec1, bigDec3)
  val s23 = Set(bigDec2, bigDec3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.bigDec.apply(bigDec1).get === List(bigDec1)
      Ns.bigDec.apply(bigDec2).get === List(bigDec2)
      Ns.bigDec.apply(bigDec1, bigDec2).get === List(bigDec1, bigDec2)

      // Explicit `or` semantics
      Ns.bigDec.apply(bigDec1 or bigDec2).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(bigDec1 or bigDec2 or bigDec3).get === List(bigDec3, bigDec1, bigDec2)

      // Iterable: List - OR semantics
      Ns.bigDec.apply(List(bigDec1)).get === List(bigDec1)
      Ns.bigDec.apply(List(bigDec2)).get === List(bigDec2)
      Ns.bigDec.apply(List(bigDec1, bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1), List(bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(bigDec3, bigDec1, bigDec2)
      Ns.bigDec.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(bigDec3, bigDec1, bigDec2)

      // mixing Iterable types and value/variable ok
      Ns.bigDec.apply(List(bigDec1), Set(bigDec2, bigDec3)).get === List(bigDec3, bigDec1, bigDec2)

      // Iterable: Set (OR semantics)
      Ns.bigDec.apply(Set(bigDec1)).get === List(bigDec1)
      Ns.bigDec.apply(Set(bigDec2)).get === List(bigDec2)
      Ns.bigDec.apply(Set(bigDec1, bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(Set(bigDec1), Set(bigDec2)).get === List(bigDec1, bigDec2)
      Ns.bigDec.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(bigDec3, bigDec1, bigDec2)
      Ns.bigDec.apply(Set(bigDec1), Set(bigDec2, bigDec3)).get === List(bigDec3, bigDec1, bigDec2)


      // Input

      val inputMolecule = m(Ns.bigDec(?))

      inputMolecule.apply(bigDec1).get === List(bigDec1)
      inputMolecule.apply(bigDec2).get === List(bigDec2)

      inputMolecule.apply(bigDec1, bigDec1).get === List(bigDec1)
      inputMolecule.apply(bigDec1, bigDec2).get === List(bigDec1, bigDec2)

      inputMolecule.apply(List(bigDec1)).get === List(bigDec1)
      inputMolecule.apply(List(bigDec1, bigDec1)).get === List(bigDec1)
      inputMolecule.apply(List(bigDec1, bigDec2)).get === List(bigDec1, bigDec2)

      inputMolecule.apply(Set(bigDec1)).get === List(bigDec1)
      inputMolecule.apply(Set(bigDec1, bigDec2)).get === List(bigDec1, bigDec2)

      inputMolecule.apply(bigDec1 or bigDec1).get === List(bigDec1)
      inputMolecule.apply(bigDec1 or bigDec2).get === List(bigDec1, bigDec2)
      inputMolecule.apply(bigDec1 or bigDec2 or bigDec3).get === List(bigDec3, bigDec1, bigDec2)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.bigDec_.apply(bigDec1).get === List(1)
      Ns.int.bigDec_.apply(bigDec2).get === List(2)
      Ns.int.bigDec_.apply(bigDec1, bigDec2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.bigDec_.apply(bigDec1 or bigDec2).get === List(1, 2)
      Ns.int.bigDec_.apply(bigDec1 or bigDec2 or bigDec3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.bigDec_.apply(List(bigDec1)).get === List(1)
      Ns.int.bigDec_.apply(List(bigDec2)).get === List(2)
      Ns.int.bigDec_.apply(List(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(List(bigDec1), List(bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDec_.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.bigDec_.apply(Set(bigDec1)).get === List(1)
      Ns.int.bigDec_.apply(Set(bigDec2)).get === List(2)
      Ns.int.bigDec_.apply(Set(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(Set(bigDec1), Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDec_.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDec_.apply(Set(bigDec1), Set(bigDec2, bigDec3)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.bigDec_(?))

      inputMolecule.apply(bigDec1).get === List(1)
      inputMolecule.apply(bigDec2).get === List(2)

      inputMolecule.apply(bigDec1, bigDec1).get === List(1)
      inputMolecule.apply(bigDec1, bigDec2).get === List(1, 2)

      inputMolecule.apply(List(bigDec1)).get === List(1)
      inputMolecule.apply(List(bigDec1, bigDec1)).get === List(1)
      inputMolecule.apply(List(bigDec1, bigDec2)).get === List(1, 2)

      inputMolecule.apply(Set(bigDec1)).get === List(1)
      inputMolecule.apply(Set(bigDec1, bigDec2)).get === List(1, 2)

      inputMolecule.apply(bigDec1 or bigDec1).get === List(1)
      inputMolecule.apply(bigDec1 or bigDec2).get === List(1, 2)
      inputMolecule.apply(bigDec1 or bigDec2 or bigDec3).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.bigDecs.apply(bigDec1).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(bigDec1, bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))

      // Explicit `or` semantics
      Ns.bigDecs.apply(bigDec1 or bigDec2).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(bigDec1 or bigDec2 or bigDec3).get === List(Set(bigDec4, bigDec3, bigDec2, bigDec1))

      // Iterable: List - OR semantics
      Ns.bigDecs.apply(List(bigDec1)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(List(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1), Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
      Ns.bigDecs.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))

      // mixing Iterable types and value/variable ok
      Ns.bigDecs.apply(List(bigDec1), Set(bigDec2, bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.bigDecs.apply(Set(bigDec1)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      Ns.bigDecs.apply(Set(bigDec1, bigDec2)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.bigDecs.apply(Set(bigDec2, bigDec3)).get === List(Set(bigDec2, bigDec3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bigDecs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bigDecs(bigDec1 and bigDec2).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs(bigDec1 and bigDec3).get === Nil


      // Input

      val inputMolecule = m(Ns.bigDecs(?))

      // AND semantics when applying bigDec1 Set of values matching attribute values of bigDec1 entity

      inputMolecule.apply(Set(bigDec1)).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))

      inputMolecule.apply(Set(bigDec1, bigDec2)).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(bigDec1, bigDec3)).get === Nil
      inputMolecule.apply(Set(bigDec2, bigDec3)).get === List(Set(bigDec2, bigDec3))
      inputMolecule.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      inputMolecule.apply(List(Set(bigDec1))).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(List(Set(bigDec2))).get === List(Set(bigDec1, bigDec3, bigDec2))
      inputMolecule.apply(List(Set(bigDec1, bigDec2))).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(List(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(List(Set(bigDec2, bigDec3))).get === List(Set(bigDec2, bigDec3))
      inputMolecule.apply(List(Set(bigDec1, bigDec2, bigDec3))).get === Nil

      inputMolecule.apply(Set(Set(bigDec1))).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(Set(bigDec2))).get === List(Set(bigDec1, bigDec3, bigDec2))
      inputMolecule.apply(Set(Set(bigDec1, bigDec2))).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(Set(Set(bigDec2, bigDec3))).get === List(Set(bigDec2, bigDec3))
      inputMolecule.apply(Set(Set(bigDec1, bigDec2, bigDec3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigDec1), Set(bigDec1)).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(bigDec1), Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      inputMolecule.apply(Set(bigDec1), Set(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
      inputMolecule.apply(Set(bigDec2), Set(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
      inputMolecule.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))

      inputMolecule.apply(Set(bigDec1) or Set(bigDec1)).get === List(Set(bigDec1, bigDec2))
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2)).get === List(Set(bigDec1, bigDec3, bigDec2))
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2) or Set(bigDec3)).get === List(Set(bigDec1, bigDec4, bigDec3, bigDec2))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.bigDecs_.apply(bigDec1).get === List(1)
      Ns.int.bigDecs_.apply(bigDec2).get === List(1, 2)
      Ns.int.bigDecs_.apply(bigDec1, bigDec2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.bigDecs_.apply(bigDec1 or bigDec2).get === List(1, 2)
      Ns.int.bigDecs_.apply(bigDec1 or bigDec2 or bigDec3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.bigDecs_.apply(List(bigDec1)).get === List(1)
      Ns.int.bigDecs_.apply(List(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1), Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List(1, 2, 3)
      Ns.int.bigDecs_.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.bigDecs_.apply(List(bigDec1), Set(bigDec2, bigDec3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bigDecs_.apply(Set(bigDec1)).get === List(1)
      Ns.int.bigDecs_.apply(Set(bigDec2)).get === List(1, 2)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2)).get === List(1)
      Ns.int.bigDecs_.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.int.bigDecs_.apply(Set(bigDec2, bigDec3)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bigDecs_.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/bigDecs_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigDecs_(bigDec1 and bigDec2).get === List(1)
      Ns.int.bigDecs_(bigDec1 and bigDec3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.bigDecs_(?))

      // AND semantics when applying bigDec1 Set of values matching attribute values of bigDec1 entity

      inputMolecule.apply(Set(bigDec1)).get === List(1)
      inputMolecule.apply(Set(bigDec2)).get === List(1, 2)

      inputMolecule.apply(Set(bigDec1, bigDec2)).get === List(1)
      inputMolecule.apply(Set(bigDec1, bigDec3)).get === Nil
      inputMolecule.apply(Set(bigDec2, bigDec3)).get === List(2)
      inputMolecule.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      inputMolecule.apply(List(Set(bigDec1))).get === List(1)
      inputMolecule.apply(List(Set(bigDec2))).get === List(1, 2)
      inputMolecule.apply(List(Set(bigDec1, bigDec2))).get === List(1)
      inputMolecule.apply(List(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(List(Set(bigDec2, bigDec3))).get === List(2)
      inputMolecule.apply(List(Set(bigDec1, bigDec2, bigDec3))).get === Nil

      inputMolecule.apply(Set(Set(bigDec1))).get === List(1)
      inputMolecule.apply(Set(Set(bigDec2))).get === List(1, 2)
      inputMolecule.apply(Set(Set(bigDec1, bigDec2))).get === List(1)
      inputMolecule.apply(Set(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(Set(Set(bigDec2, bigDec3))).get === List(2)
      inputMolecule.apply(Set(Set(bigDec1, bigDec2, bigDec3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigDec1), Set(bigDec1)).get === List(1)
      inputMolecule.apply(Set(bigDec1), Set(bigDec2)).get === List(1, 2)
      inputMolecule.apply(Set(bigDec1), Set(bigDec3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(bigDec2), Set(bigDec3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List(1, 2, 3)

      inputMolecule.apply(Set(bigDec1) or Set(bigDec1)).get === List(1)
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2)).get === List(1, 2)
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2) or Set(bigDec3)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.bigDecs.apply(bigDec1).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(bigDec1, bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))

      // Explicit `or` semantics
      Ns.int.bigDecs.apply(bigDec1 or bigDec2).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(bigDec1 or bigDec2 or bigDec3).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      // Iterable: List - OR semantics
      Ns.int.bigDecs.apply(List(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(List(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1, bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1), Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(List(bigDec1, bigDec2), List(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(List(bigDec1), List(bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      // mixing Iterable types and value/variable ok
      Ns.int.bigDecs.apply(List(bigDec1), Set(bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      Ns.int.bigDecs.apply(l1, Set(bigDec2, bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bigDecs.apply(Set(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec2)).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs.apply(Set(bigDec1, bigDec3)).get === Nil
      Ns.int.bigDecs.apply(Set(bigDec2, bigDec3)).get === List((2, Set(bigDec2, bigDec3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bigDecs.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bigDecs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigDecs(bigDec1 and bigDec2).get === List((1, Set(bigDec1, bigDec2)))
      Ns.int.bigDecs(bigDec1 and bigDec3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.bigDecs(?))

      // AND semantics when applying bigDec1 Set of values matching attribute values of bigDec1 entity

      inputMolecule.apply(Set(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))

      inputMolecule.apply(Set(bigDec1, bigDec2)).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(bigDec1, bigDec3)).get === Nil
      inputMolecule.apply(Set(bigDec2, bigDec3)).get === List((2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(Set(bigDec1, bigDec2, bigDec3)).get === Nil

      inputMolecule.apply(List(Set(bigDec1))).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(List(Set(bigDec2))).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(List(Set(bigDec1, bigDec2))).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(List(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(List(Set(bigDec2, bigDec3))).get === List((2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(List(Set(bigDec1, bigDec2, bigDec3))).get === Nil

      inputMolecule.apply(Set(Set(bigDec1))).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(Set(bigDec2))).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(Set(Set(bigDec1, bigDec2))).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(Set(bigDec1, bigDec3))).get === Nil
      inputMolecule.apply(Set(Set(bigDec2, bigDec3))).get === List((2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(Set(Set(bigDec1, bigDec2, bigDec3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigDec1), Set(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(bigDec1), Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(Set(bigDec1), Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      inputMolecule.apply(Set(bigDec2), Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
      inputMolecule.apply(Set(bigDec1, bigDec2), Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))

      inputMolecule.apply(Set(bigDec1) or Set(bigDec1)).get === List((1, Set(bigDec1, bigDec2)))
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)))
      inputMolecule.apply(Set(bigDec1) or Set(bigDec2) or Set(bigDec3)).get === List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
    }
  }
}