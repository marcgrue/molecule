package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyDouble extends ApplyBase {

  val l1 = List(1.0)
  val l2 = List(2.0)
  val l3 = List(3.0)

  val s1 = Set(1.0)
  val s2 = Set(2.0)
  val s3 = Set(3.0)

  val l11 = List(1.0, 1.0)
  val l12 = List(1.0, 2.0)
  val l13 = List(1.0, 3.0)
  val l23 = List(2.0, 3.0)

  val s12 = Set(1.0, 2.0)
  val s13 = Set(1.0, 3.0)
  val s23 = Set(2.0, 3.0)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.double.apply(1.0).get === List(1.0)
      Ns.double.apply(2.0).get === List(2.0)
      Ns.double.apply(1.0, 2.0).get === List(2.0, 1.0)

      // Explicit `or` semantics
      Ns.double.apply(1.0 or 2.0).get === List(2.0, 1.0)
      Ns.double.apply(1.0 or 2.0 or 3.0).get === List(3.0, 1.0, 2.0)

      // Iterable: List - OR semantics
      Ns.double.apply(List(1.0)).get === List(1.0)
      Ns.double.apply(List(2.0)).get === List(2.0)
      Ns.double.apply(List(1.0, 2.0)).get === List(2.0, 1.0)
      Ns.double.apply(List(1.0), List(2.0)).get === List(2.0, 1.0)
      Ns.double.apply(List(1.0, 2.0), List(3.0)).get === List(3.0, 1.0, 2.0)
      Ns.double.apply(List(1.0), List(2.0, 3.0)).get === List(3.0, 1.0, 2.0)

      // mixing Iterable types and value/variable ok
      Ns.double.apply(List(double1), Set(2.0, double3)).get === List(3.0, 1.0, 2.0)

      // Iterable: Set (OR semantics)
      Ns.double.apply(Set(1.0)).get === List(1.0)
      Ns.double.apply(Set(2.0)).get === List(2.0)
      Ns.double.apply(Set(1.0, 2.0)).get === List(2.0, 1.0)
      Ns.double.apply(Set(1.0), Set(2.0)).get === List(2.0, 1.0)
      Ns.double.apply(Set(1.0, 2.0), Set(3.0)).get === List(3.0, 1.0, 2.0)
      Ns.double.apply(Set(1.0), Set(2.0, 3.0)).get === List(3.0, 1.0, 2.0)


      // Input

      val inputMolecule = m(Ns.double(?))

      inputMolecule.apply(1.0).get === List(1.0)
      inputMolecule.apply(2.0).get === List(2.0)

      inputMolecule.apply(1.0, 1.0).get === List(1.0)
      inputMolecule.apply(1.0, 2.0).get === List(2.0, 1.0)

      inputMolecule.apply(List(1.0)).get === List(1.0)
      inputMolecule.apply(List(1.0, 1.0)).get === List(1.0)
      inputMolecule.apply(List(1.0, 2.0)).get === List(2.0, 1.0)

      inputMolecule.apply(Set(1.0)).get === List(1.0)
      inputMolecule.apply(Set(1.0, 2.0)).get === List(2.0, 1.0)

      inputMolecule.apply(1.0 or 1.0).get === List(1.0)
      inputMolecule.apply(1.0 or 2.0).get === List(2.0, 1.0)
      inputMolecule.apply(1.0 or 2.0 or 3.0).get === List(3.0, 1.0, 2.0)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.double_.apply(1.0).get === List(1)
      Ns.int.double_.apply(2.0).get === List(2)
      Ns.int.double_.apply(1.0, 2.0).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.double_.apply(1.0 or 2.0).get === List(1, 2)
      Ns.int.double_.apply(1.0 or 2.0 or 3.0).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.double_.apply(List(1.0)).get === List(1)
      Ns.int.double_.apply(List(2.0)).get === List(2)
      Ns.int.double_.apply(List(1.0, 2.0)).get === List(2, 1)
      Ns.int.double_.apply(List(1.0), List(2.0)).get === List(2, 1)
      Ns.int.double_.apply(List(1.0, 2.0), List(3.0)).get === List(1, 2, 3)
      Ns.int.double_.apply(List(1.0), List(2.0, 3.0)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.double_.apply(Set(1.0)).get === List(1)
      Ns.int.double_.apply(Set(2.0)).get === List(2)
      Ns.int.double_.apply(Set(1.0, 2.0)).get === List(2, 1)
      Ns.int.double_.apply(Set(1.0), Set(2.0)).get === List(2, 1)
      Ns.int.double_.apply(Set(1.0, 2.0), Set(3.0)).get === List(1, 2, 3)
      Ns.int.double_.apply(Set(1.0), Set(2.0, 3.0)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.double_(?))

      inputMolecule.apply(1.0).get === List(1)
      inputMolecule.apply(2.0).get === List(2)

      inputMolecule.apply(1.0, 1.0).get === List(1)
      inputMolecule.apply(1.0, 2.0).get === List(2, 1)

      inputMolecule.apply(List(1.0)).get === List(1)
      inputMolecule.apply(List(1.0, 1.0)).get === List(1)
      inputMolecule.apply(List(1.0, 2.0)).get === List(2, 1)

      inputMolecule.apply(Set(1.0)).get === List(1)
      inputMolecule.apply(Set(1.0, 2.0)).get === List(2, 1)

      inputMolecule.apply(1.0 or 1.0).get === List(1)
      inputMolecule.apply(1.0 or 2.0).get === List(2, 1)
      inputMolecule.apply(1.0 or 2.0 or 3.0).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.doubles.apply(1.0).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(2.0).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(1.0, 2.0).get === List(Set(1.0, 3.0, 2.0))

      // Explicit `or` semantics
      Ns.doubles.apply(1.0 or 2.0).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(1.0 or 2.0 or 3.0).get === List(Set(1.0, 4.0, 3.0, 2.0))

      // Iterable: List - OR semantics
      Ns.doubles.apply(List(1.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(List(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0, 2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0), List(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0, 2.0), List(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
      Ns.doubles.apply(List(1.0), List(2.0, 3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))

      // mixing Iterable types and value/variable ok
      Ns.doubles.apply(List(double1), Set(2.0, double3)).get === List(Set(1.0, 4.0, 3.0, 2.0))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.doubles.apply(Set(1.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(Set(2.0)).get === List(Set(1.0, 3.0, 2.0))
      Ns.doubles.apply(Set(1.0, 2.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(Set(1.0, 3.0)).get === Nil
      Ns.doubles.apply(Set(2.0, 3.0)).get === List(Set(2.0, 3.0))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.doubles.apply(Set(1.0, 2.0), Set(3.0))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/doubles"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.doubles(1.0 and 2.0).get === List(Set(1.0, 2.0))
      Ns.doubles(1.0 and 3.0).get === Nil


      // Input

      val inputMolecule = m(Ns.doubles(?))

      // AND semantics when applying 1.0 Set of values matching attribute values of 1.0 entity

      inputMolecule.apply(Set(1.0)).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(2.0)).get === List(Set(1.0, 3.0, 2.0))

      inputMolecule.apply(Set(1.0, 2.0)).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(1.0, 3.0)).get === Nil
      inputMolecule.apply(Set(2.0, 3.0)).get === List(Set(2.0, 3.0))
      inputMolecule.apply(Set(1.0, 2.0, 3.0)).get === Nil

      inputMolecule.apply(List(Set(1.0))).get === List(Set(1.0, 2.0))
      inputMolecule.apply(List(Set(2.0))).get === List(Set(1.0, 3.0, 2.0))
      inputMolecule.apply(List(Set(1.0, 2.0))).get === List(Set(1.0, 2.0))
      inputMolecule.apply(List(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(List(Set(2.0, 3.0))).get === List(Set(2.0, 3.0))
      inputMolecule.apply(List(Set(1.0, 2.0, 3.0))).get === Nil

      inputMolecule.apply(Set(Set(1.0))).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(Set(2.0))).get === List(Set(1.0, 3.0, 2.0))
      inputMolecule.apply(Set(Set(1.0, 2.0))).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(Set(Set(2.0, 3.0))).get === List(Set(2.0, 3.0))
      inputMolecule.apply(Set(Set(1.0, 2.0, 3.0))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0), Set(1.0)).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(1.0), Set(2.0)).get === List(Set(1.0, 3.0, 2.0))
      inputMolecule.apply(Set(1.0), Set(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
      inputMolecule.apply(Set(2.0), Set(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
      inputMolecule.apply(Set(1.0, 2.0), Set(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))

      inputMolecule.apply(Set(1.0) or Set(1.0)).get === List(Set(1.0, 2.0))
      inputMolecule.apply(Set(1.0) or Set(2.0)).get === List(Set(1.0, 3.0, 2.0))
      inputMolecule.apply(Set(1.0) or Set(2.0) or Set(3.0)).get === List(Set(1.0, 4.0, 3.0, 2.0))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.doubles_.apply(1.0).get === List(1)
      Ns.int.doubles_.apply(2.0).get === List(1, 2)
      Ns.int.doubles_.apply(1.0, 2.0).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.doubles_.apply(1.0 or 2.0).get === List(1, 2)
      Ns.int.doubles_.apply(1.0 or 2.0 or 3.0).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.doubles_.apply(List(1.0)).get === List(1)
      Ns.int.doubles_.apply(List(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0, 2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0), List(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(List(1.0, 2.0), List(3.0)).get === List(1, 2, 3)
      Ns.int.doubles_.apply(List(1.0), List(2.0, 3.0)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.doubles_.apply(List(double1), Set(2.0, double3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.doubles_.apply(Set(1.0)).get === List(1)
      Ns.int.doubles_.apply(Set(2.0)).get === List(1, 2)
      Ns.int.doubles_.apply(Set(1.0, 2.0)).get === List(1)
      Ns.int.doubles_.apply(Set(1.0, 3.0)).get === Nil
      Ns.int.doubles_.apply(Set(2.0, 3.0)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.doubles_.apply(Set(1.0, 2.0), Set(3.0)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/doubles_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.doubles_(1.0 and 2.0).get === List(10)
      Ns.int.doubles_(1.0 and 3.0).get === Nil


      // Input

      val inputMolecule = m(Ns.int.doubles_(?))

      // AND semantics when applying 1.0 Set of values matching attribute values of 1.0 entity

      inputMolecule.apply(Set(1.0)).get === List(1)
      inputMolecule.apply(Set(2.0)).get === List(1, 2)

      inputMolecule.apply(Set(1.0, 2.0)).get === List(1)
      inputMolecule.apply(Set(1.0, 3.0)).get === Nil
      inputMolecule.apply(Set(2.0, 3.0)).get === List(2)
      inputMolecule.apply(Set(1.0, 2.0, 3.0)).get === Nil

      inputMolecule.apply(List(Set(1.0))).get === List(1)
      inputMolecule.apply(List(Set(2.0))).get === List(1, 2)
      inputMolecule.apply(List(Set(1.0, 2.0))).get === List(1)
      inputMolecule.apply(List(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(List(Set(2.0, 3.0))).get === List(2)
      inputMolecule.apply(List(Set(1.0, 2.0, 3.0))).get === Nil

      inputMolecule.apply(Set(Set(1.0))).get === List(1)
      inputMolecule.apply(Set(Set(2.0))).get === List(1, 2)
      inputMolecule.apply(Set(Set(1.0, 2.0))).get === List(1)
      inputMolecule.apply(Set(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(Set(Set(2.0, 3.0))).get === List(2)
      inputMolecule.apply(Set(Set(1.0, 2.0, 3.0))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0), Set(1.0)).get === List(1)
      inputMolecule.apply(Set(1.0), Set(2.0)).get === List(1, 2)
      inputMolecule.apply(Set(1.0), Set(3.0)).get === List(1, 2, 3)
      inputMolecule.apply(Set(2.0), Set(3.0)).get === List(1, 2, 3)
      inputMolecule.apply(Set(1.0, 2.0), Set(3.0)).get === List(1, 2, 3)

      inputMolecule.apply(Set(1.0) or Set(1.0)).get === List(1)
      inputMolecule.apply(Set(1.0) or Set(2.0)).get === List(1, 2)
      inputMolecule.apply(Set(1.0) or Set(2.0) or Set(3.0)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.doubles.apply(1.0).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(1.0, 2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))

      // Explicit `or` semantics
      Ns.int.doubles.apply(1.0 or 2.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(1.0 or 2.0 or 3.0).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      // Iterable: List - OR semantics
      Ns.int.doubles.apply(List(1.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(List(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0, 2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0), List(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(List(1.0, 2.0), List(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(List(1.0), List(2.0, 3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      // mixing Iterable types and value/variable ok
      Ns.int.doubles.apply(List(double1), Set(2.0, double3)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      Ns.int.doubles.apply(l1, Set(2.0, double3)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.doubles.apply(Set(1.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      Ns.int.doubles.apply(Set(1.0, 2.0)).get === List((1, Set(1.0, 2.0)))
      Ns.int.doubles.apply(Set(1.0, 3.0)).get === Nil
      Ns.int.doubles.apply(Set(2.0, 3.0)).get === List((2, Set(2.0, 3.0)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.doubles.apply(Set(1.0, 2.0), Set(3.0)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/doubles"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)

      Ns.int.doubles(1.0 and 2.0).get === List((1, Set(2.0, 1.0)))
      Ns.int.doubles(1.0 and 3.0).get === Nil


      // Input

      val inputMolecule = m(Ns.int.doubles(?))

      // AND semantics when applying 1.0 Set of values matching attribute values of 1.0 entity

      inputMolecule.apply(Set(1.0)).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))

      inputMolecule.apply(Set(1.0, 2.0)).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(1.0, 3.0)).get === Nil
      inputMolecule.apply(Set(2.0, 3.0)).get === List((2, Set(2.0, 3.0)))
      inputMolecule.apply(Set(1.0, 2.0, 3.0)).get === Nil

      inputMolecule.apply(List(Set(1.0))).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(List(Set(2.0))).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      inputMolecule.apply(List(Set(1.0, 2.0))).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(List(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(List(Set(2.0, 3.0))).get === List((2, Set(2.0, 3.0)))
      inputMolecule.apply(List(Set(1.0, 2.0, 3.0))).get === Nil

      inputMolecule.apply(Set(Set(1.0))).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(Set(2.0))).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      inputMolecule.apply(Set(Set(1.0, 2.0))).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(Set(1.0, 3.0))).get === Nil
      inputMolecule.apply(Set(Set(2.0, 3.0))).get === List((2, Set(2.0, 3.0)))
      inputMolecule.apply(Set(Set(1.0, 2.0, 3.0))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1.0), Set(1.0)).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(1.0), Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      inputMolecule.apply(Set(1.0), Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      inputMolecule.apply(Set(2.0), Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
      inputMolecule.apply(Set(1.0, 2.0), Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))

      inputMolecule.apply(Set(1.0) or Set(1.0)).get === List((1, Set(1.0, 2.0)))
      inputMolecule.apply(Set(1.0) or Set(2.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)))
      inputMolecule.apply(Set(1.0) or Set(2.0) or Set(3.0)).get === List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.double.apply(double1, double2).get === List(2.0, 1.0)

    Ns.double.apply(List(double1, double2), List(double3)).get === List(3.0, 1.0, 2.0)
    Ns.double.apply(l12, l3).get === List(3.0, 1.0, 2.0)

    Ns.double.apply(Set(double1, double2), Set(double3)).get === List(3.0, 1.0, 2.0)
    Ns.double.apply(s12, s3).get === List(3.0, 1.0, 2.0)
  }
}