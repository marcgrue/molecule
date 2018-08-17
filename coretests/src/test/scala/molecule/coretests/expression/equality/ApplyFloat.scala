package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyFloat extends ApplyBase {

  val l1 = List(1.0f)
  val l2 = List(2.0f)
  val l3 = List(3.0f)

  val s1 = Set(1.0f)
  val s2 = Set(2.0f)
  val s3 = Set(3.0f)

  val l11 = List(1.0f, 1.0f)
  val l12 = List(1.0f, 2.0f)
  val l13 = List(1.0f, 3.0f)
  val l23 = List(2.0f, 3.0f)

  val s12 = Set(1.0f, 2.0f)
  val s13 = Set(1.0f, 3.0f)
  val s23 = Set(2.0f, 3.0f)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.float.apply(1.0f).get === List(1.0f)
      Ns.float.apply(2.0f).get === List(2.0f)
      Ns.float.apply(1.0f, 2.0f).get === List(1.0f, 2.0f)

      // Explicit `or` semantics
      Ns.float.apply(1.0f or 2.0f).get === List(1.0f, 2.0f)
      Ns.float.apply(1.0f or 2.0f or 3.0f).get === List(3.0f, 1.0f, 2.0f)

      // Iterable: List - OR semantics
      Ns.float.apply(List(1.0f)).get === List(1.0f)
      Ns.float.apply(List(2.0f)).get === List(2.0f)
      Ns.float.apply(List(1.0f, 2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(List(1.0f), List(2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(List(1.0f, 2.0f), List(3.0f)).get === List(3.0f, 1.0f, 2.0f)
      Ns.float.apply(List(1.0f), List(2.0f, 3.0f)).get === List(3.0f, 1.0f, 2.0f)

      // mixing Iterable types and value/variable ok
      Ns.float.apply(List(float1), Set(2.0f, float3)).get === List(3.0f, 1.0f, 2.0f)

      // Iterable: Set (OR semantics)
      Ns.float.apply(Set(1.0f)).get === List(1.0f)
      Ns.float.apply(Set(2.0f)).get === List(2.0f)
      Ns.float.apply(Set(1.0f, 2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(Set(1.0f), Set(2.0f)).get === List(1.0f, 2.0f)
      Ns.float.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(3.0f, 1.0f, 2.0f)
      Ns.float.apply(Set(1.0f), Set(2.0f, 3.0f)).get === List(3.0f, 1.0f, 2.0f)


      // Input

      val inputMolecule = m(Ns.float(?))

      inputMolecule.apply(1.0f).get === List(1.0f)
      inputMolecule.apply(2.0f).get === List(2.0f)

      inputMolecule.apply(1.0f, 1.0f).get === List(1.0f)
      inputMolecule.apply(1.0f, 2.0f).get === List(1.0f, 2.0f)

      inputMolecule.apply(List(1.0f)).get === List(1.0f)
      inputMolecule.apply(List(1.0f, 1.0f)).get === List(1.0f)
      inputMolecule.apply(List(1.0f, 2.0f)).get === List(1.0f, 2.0f)

      inputMolecule.apply(Set(1.0f)).get === List(1.0f)
      inputMolecule.apply(Set(1.0f, 2.0f)).get === List(1.0f, 2.0f)

      inputMolecule.apply(1.0f or 1.0f).get === List(1.0f)
      inputMolecule.apply(1.0f or 2.0f).get === List(1.0f, 2.0f)
      inputMolecule.apply(1.0f or 2.0f or 3.0f).get === List(3.0f, 1.0f, 2.0f)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.float_.apply(1.0f).get === List(1)
      Ns.int.float_.apply(2.0f).get === List(2)
      Ns.int.float_.apply(1.0f, 2.0f).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.float_.apply(1.0f or 2.0f).get === List(1, 2)
      Ns.int.float_.apply(1.0f or 2.0f or 3.0f).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.float_.apply(List(1.0f)).get === List(1)
      Ns.int.float_.apply(List(2.0f)).get === List(2)
      Ns.int.float_.apply(List(1.0f, 2.0f)).get === List(1, 2)
      Ns.int.float_.apply(List(1.0f), List(2.0f)).get === List(1, 2)
      Ns.int.float_.apply(List(1.0f, 2.0f), List(3.0f)).get === List(1, 2, 3)
      Ns.int.float_.apply(List(1.0f), List(2.0f, 3.0f)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.float_.apply(Set(1.0f)).get === List(1)
      Ns.int.float_.apply(Set(2.0f)).get === List(2)
      Ns.int.float_.apply(Set(1.0f, 2.0f)).get === List(1, 2)
      Ns.int.float_.apply(Set(1.0f), Set(2.0f)).get === List(1, 2)
      Ns.int.float_.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(1, 2, 3)
      Ns.int.float_.apply(Set(1.0f), Set(2.0f, 3.0f)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.float_(?))

      inputMolecule.apply(1.0f).get === List(1)
      inputMolecule.apply(2.0f).get === List(2)

      inputMolecule.apply(1.0f, 1.0f).get === List(1)
      inputMolecule.apply(1.0f, 2.0f).get === List(1, 2)

      inputMolecule.apply(List(1.0f)).get === List(1)
      inputMolecule.apply(List(1.0f, 1.0f)).get === List(1)
      inputMolecule.apply(List(1.0f, 2.0f)).get === List(1, 2)

      inputMolecule.apply(Set(1.0f)).get === List(1)
      inputMolecule.apply(Set(1.0f, 2.0f)).get === List(1, 2)

      inputMolecule.apply(1.0f or 1.0f).get === List(1)
      inputMolecule.apply(1.0f or 2.0f).get === List(1, 2)
      inputMolecule.apply(1.0f or 2.0f or 3.0f).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.floats.apply(1.0f).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(2.0f).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(1.0f, 2.0f).get === List(Set(1.0f, 3.0f, 2.0f))

      // Explicit `or` semantics
      Ns.floats.apply(1.0f or 2.0f).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(1.0f or 2.0f or 3.0f).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))

      // Iterable: List - OR semantics
      Ns.floats.apply(List(1.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(List(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f), List(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 2.0f), List(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
      Ns.floats.apply(List(1.0f), List(2.0f, 3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))

      // mixing Iterable types and value/variable ok
      Ns.floats.apply(List(float1), Set(2.0f, float3)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.floats.apply(Set(1.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(Set(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      Ns.floats.apply(Set(1.0f, 2.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.floats.apply(Set(2.0f, 3.0f)).get === List(Set(2.0f, 3.0f))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.floats.apply(Set(1.0f, 2.0f), Set(3.0f))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/floats"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.floats(1.0f and 2.0f).get === List(Set(1.0f, 2.0f))
      Ns.floats(1.0f and 3.0f).get === Nil


      // Input

      val inputMolecule = m(Ns.floats(?))

      // AND semantics when applying 1.0f Set of values matching attribute values of 1.0f entity

      inputMolecule.apply(Set(1.0f)).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))

      inputMolecule.apply(Set(1.0f, 2.0f)).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(1.0f, 3.0f)).get === Nil
      inputMolecule.apply(Set(2.0f, 3.0f)).get === List(Set(2.0f, 3.0f))
      inputMolecule.apply(Set(3.0f, 1.0f, 2.0f)).get === Nil

      inputMolecule.apply(List(Set(1.0f))).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(List(Set(2.0f))).get === List(Set(1.0f, 3.0f, 2.0f))
      inputMolecule.apply(List(Set(1.0f, 2.0f))).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(List(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(List(Set(2.0f, 3.0f))).get === List(Set(2.0f, 3.0f))
      inputMolecule.apply(List(Set(3.0f, 1.0f, 2.0f))).get === Nil

      inputMolecule.apply(Set(Set(1.0f))).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(Set(2.0f))).get === List(Set(1.0f, 3.0f, 2.0f))
      inputMolecule.apply(Set(Set(1.0f, 2.0f))).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(Set(Set(2.0f, 3.0f))).get === List(Set(2.0f, 3.0f))
      inputMolecule.apply(Set(Set(3.0f, 1.0f, 2.0f))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0f), Set(1.0f)).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(1.0f), Set(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      inputMolecule.apply(Set(1.0f), Set(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
      inputMolecule.apply(Set(2.0f), Set(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
      inputMolecule.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))

      inputMolecule.apply(Set(1.0f) or Set(1.0f)).get === List(Set(1.0f, 2.0f))
      inputMolecule.apply(Set(1.0f) or Set(2.0f)).get === List(Set(1.0f, 3.0f, 2.0f))
      inputMolecule.apply(Set(1.0f) or Set(2.0f) or Set(3.0f)).get === List(Set(1.0f, 4.0f, 3.0f, 2.0f))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.floats_.apply(1.0f).get === List(1)
      Ns.int.floats_.apply(2.0f).get === List(1, 2)
      Ns.int.floats_.apply(1.0f, 2.0f).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.floats_.apply(1.0f or 2.0f).get === List(1, 2)
      Ns.int.floats_.apply(1.0f or 2.0f or 3.0f).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.floats_.apply(List(1.0f)).get === List(1)
      Ns.int.floats_.apply(List(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f, 2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f), List(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(List(1.0f, 2.0f), List(3.0f)).get === List(1, 2, 3)
      Ns.int.floats_.apply(List(1.0f), List(2.0f, 3.0f)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.floats_.apply(List(float1), Set(2.0f, float3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.floats_.apply(Set(1.0f)).get === List(1)
      Ns.int.floats_.apply(Set(2.0f)).get === List(1, 2)
      Ns.int.floats_.apply(Set(1.0f, 2.0f)).get === List(1)
      Ns.int.floats_.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.int.floats_.apply(Set(2.0f, 3.0f)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.floats_.apply(Set(1.0f, 2.0f), Set(3.0f)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/floats_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.floats_(1.0f and 2.0f).get === List(1)
      Ns.int.floats_(1.0f and 3.0f).get === Nil


      // Input

      val inputMolecule = m(Ns.int.floats_(?))

      // AND semantics when applying 1.0f Set of values matching attribute values of 1.0f entity

      inputMolecule.apply(Set(1.0f)).get === List(1)
      inputMolecule.apply(Set(2.0f)).get === List(1, 2)

      inputMolecule.apply(Set(1.0f, 2.0f)).get === List(1)
      inputMolecule.apply(Set(1.0f, 3.0f)).get === Nil
      inputMolecule.apply(Set(2.0f, 3.0f)).get === List(2)
      inputMolecule.apply(Set(3.0f, 1.0f, 2.0f)).get === Nil

      inputMolecule.apply(List(Set(1.0f))).get === List(1)
      inputMolecule.apply(List(Set(2.0f))).get === List(1, 2)
      inputMolecule.apply(List(Set(1.0f, 2.0f))).get === List(1)
      inputMolecule.apply(List(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(List(Set(2.0f, 3.0f))).get === List(2)
      inputMolecule.apply(List(Set(3.0f, 1.0f, 2.0f))).get === Nil

      inputMolecule.apply(Set(Set(1.0f))).get === List(1)
      inputMolecule.apply(Set(Set(2.0f))).get === List(1, 2)
      inputMolecule.apply(Set(Set(1.0f, 2.0f))).get === List(1)
      inputMolecule.apply(Set(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(Set(Set(2.0f, 3.0f))).get === List(2)
      inputMolecule.apply(Set(Set(3.0f, 1.0f, 2.0f))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0f), Set(1.0f)).get === List(1)
      inputMolecule.apply(Set(1.0f), Set(2.0f)).get === List(1, 2)
      inputMolecule.apply(Set(1.0f), Set(3.0f)).get === List(1, 2, 3)
      inputMolecule.apply(Set(2.0f), Set(3.0f)).get === List(1, 2, 3)
      inputMolecule.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List(1, 2, 3)

      inputMolecule.apply(Set(1.0f) or Set(1.0f)).get === List(1)
      inputMolecule.apply(Set(1.0f) or Set(2.0f)).get === List(1, 2)
      inputMolecule.apply(Set(1.0f) or Set(2.0f) or Set(3.0f)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.floats.apply(1.0f).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(1.0f, 2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))

      // Explicit `or` semantics
      Ns.int.floats.apply(1.0f or 2.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(1.0f or 2.0f or 3.0f).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      // Iterable: List - OR semantics
      Ns.int.floats.apply(List(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(List(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f, 2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f), List(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(List(1.0f, 2.0f), List(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(List(1.0f), List(2.0f, 3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      // mixing Iterable types and value/variable ok
      Ns.int.floats.apply(List(float1), Set(2.0f, float3)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      Ns.int.floats.apply(l1, Set(2.0f, float3)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.floats.apply(Set(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      Ns.int.floats.apply(Set(1.0f, 2.0f)).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats.apply(Set(1.0f, 3.0f)).get === Nil
      Ns.int.floats.apply(Set(2.0f, 3.0f)).get === List((2, Set(2.0f, 3.0f)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.floats.apply(Set(1.0f, 2.0f), Set(3.0f)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/floats"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.floats(1.0f and 2.0f).get === List((1, Set(1.0f, 2.0f)))
      Ns.int.floats(1.0f and 3.0f).get === Nil


      // Input

      val inputMolecule = m(Ns.int.floats(?))

      // AND semantics when applying 1.0f Set of values matching attribute values of 1.0f entity

      inputMolecule.apply(Set(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))

      inputMolecule.apply(Set(1.0f, 2.0f)).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(1.0f, 3.0f)).get === Nil
      inputMolecule.apply(Set(2.0f, 3.0f)).get === List((2, Set(2.0f, 3.0f)))
      inputMolecule.apply(Set(3.0f, 1.0f, 2.0f)).get === Nil

      inputMolecule.apply(List(Set(1.0f))).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(List(Set(2.0f))).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      inputMolecule.apply(List(Set(1.0f, 2.0f))).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(List(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(List(Set(2.0f, 3.0f))).get === List((2, Set(2.0f, 3.0f)))
      inputMolecule.apply(List(Set(3.0f, 1.0f, 2.0f))).get === Nil

      inputMolecule.apply(Set(Set(1.0f))).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(Set(2.0f))).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      inputMolecule.apply(Set(Set(1.0f, 2.0f))).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(Set(1.0f, 3.0f))).get === Nil
      inputMolecule.apply(Set(Set(2.0f, 3.0f))).get === List((2, Set(2.0f, 3.0f)))
      inputMolecule.apply(Set(Set(3.0f, 1.0f, 2.0f))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0f), Set(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(1.0f), Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      inputMolecule.apply(Set(1.0f), Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      inputMolecule.apply(Set(2.0f), Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
      inputMolecule.apply(Set(1.0f, 2.0f), Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))

      inputMolecule.apply(Set(1.0f) or Set(1.0f)).get === List((1, Set(1.0f, 2.0f)))
      inputMolecule.apply(Set(1.0f) or Set(2.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)))
      inputMolecule.apply(Set(1.0f) or Set(2.0f) or Set(3.0f)).get === List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.float.apply(float1, float2).get === List(1.0f, 2.0f)

    Ns.float.apply(List(float1, float2), List(float3)).get === List(3.0f, 1.0f, 2.0f)
    Ns.float.apply(l12, l3).get === List(3.0f, 1.0f, 2.0f)

    Ns.float.apply(Set(float1, float2), Set(float3)).get === List(3.0f, 1.0f, 2.0f)
    Ns.float.apply(s12, s3).get === List(3.0f, 1.0f, 2.0f)
  }
}