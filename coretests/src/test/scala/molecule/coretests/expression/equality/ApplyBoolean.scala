package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyBoolean extends ApplyBase {

  val l1 = List(true)
  val l2 = List(false)
  val l3 = List(true)

  val s1 = Set(true)
  val s2 = Set(false)
  val s3 = Set(true)

  val l11 = List(true, true)
  val l12 = List(true, false)
  val l13 = List(true, true)
  val l23 = List(false, true)

  val s12 = Set(true, false)
  val s13 = Set(true, true)
  val s23 = Set(false, true)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.bool.apply(true).get === List(true)
      Ns.bool.apply(false).get === List(false)
      Ns.bool.apply(true, false).get === List(false, true)

      // Explicit `or` semantics
      Ns.bool.apply(true or false).get === List(false, true)

      // Iterable: List - OR semantics
      Ns.bool.apply(List(true)).get === List(true)
      Ns.bool.apply(List(false)).get === List(false)
      Ns.bool.apply(List(true, false)).get === List(false, true)
      Ns.bool.apply(List(true), List(false)).get === List(false, true)
      Ns.bool.apply(List(true, false), List(true)).get === List(false, true)
      Ns.bool.apply(List(true), List(false, true)).get === List(false, true)

      // mixing Iterable types and value/variable ok
      Ns.bool.apply(List(bool1), Set(false, bool3)).get === List(false, true)

      // Iterable: Set (OR semantics)
      Ns.bool.apply(Set(true)).get === List(true)
      Ns.bool.apply(Set(false)).get === List(false)
      Ns.bool.apply(Set(true, false)).get === List(false, true)
      Ns.bool.apply(Set(true), Set(false)).get === List(false, true)
      Ns.bool.apply(Set(true, false), Set(true)).get === List(false, true)
      Ns.bool.apply(Set(true), Set(false, true)).get === List(false, true)


      // Input

      val inputMolecule = m(Ns.bool(?))

      inputMolecule.apply(true).get === List(true)
      inputMolecule.apply(false).get === List(false)

      inputMolecule.apply(true, true).get === List(true)
      inputMolecule.apply(true, false).get === List(false, true)

      inputMolecule.apply(List(true)).get === List(true)
      inputMolecule.apply(List(true, true)).get === List(true)
      inputMolecule.apply(List(true, false)).get === List(false, true)

      inputMolecule.apply(Set(true)).get === List(true)
      inputMolecule.apply(Set(true, false)).get === List(false, true)

      inputMolecule.apply(true or true).get === List(true)
      inputMolecule.apply(true or false).get === List(false, true)
      inputMolecule.apply(true or false or true).get === List(false, true)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.bool_.apply(true).get === List(1)
      Ns.int.bool_.apply(false).get === List(2)
      Ns.int.bool_.apply(true, false).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.bool_.apply(true or false).get === List(1, 2)

      // Iterable: List - OR semantics
      Ns.int.bool_.apply(List(true)).get === List(1)
      Ns.int.bool_.apply(List(false)).get === List(2)
      Ns.int.bool_.apply(List(true, false)).get === List(1, 2)
      Ns.int.bool_.apply(List(true), List(false)).get === List(1, 2)
      Ns.int.bool_.apply(List(true, false), List(true)).get === List(1, 2)
      Ns.int.bool_.apply(List(true), List(false, true)).get === List(1, 2)

      // Iterable: Set (OR semantics)
      Ns.int.bool_.apply(Set(true)).get === List(1)
      Ns.int.bool_.apply(Set(false)).get === List(2)
      Ns.int.bool_.apply(Set(true, false)).get === List(1, 2)
      Ns.int.bool_.apply(Set(true), Set(false)).get === List(1, 2)
      Ns.int.bool_.apply(Set(true, false), Set(true)).get === List(1, 2)
      Ns.int.bool_.apply(Set(true), Set(false, true)).get === List(1, 2)


      // Input

      val inputMolecule = m(Ns.int.bool_(?))

      inputMolecule.apply(true).get === List(1)
      inputMolecule.apply(false).get === List(2)

      inputMolecule.apply(true, true).get === List(1)
      inputMolecule.apply(true, false).get === List(1, 2)

      inputMolecule.apply(List(true)).get === List(1)
      inputMolecule.apply(List(true, true)).get === List(1)
      inputMolecule.apply(List(true, false)).get === List(1, 2)

      inputMolecule.apply(Set(true)).get === List(1)
      inputMolecule.apply(Set(true, false)).get === List(1, 2)

      inputMolecule.apply(true or true).get === List(1)
      inputMolecule.apply(true or false).get === List(1, 2)
      inputMolecule.apply(true or false or true).get === List(1, 2)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.bools.apply(true).get === List(Set(true, false))
      Ns.bools.apply(false).get === List(Set(true, false))
      Ns.bools.apply(true, false).get === List(Set(true, false))

      // Explicit `or` semantics
      Ns.bools.apply(true or false).get === List(Set(true, false))

      // Iterable: List - OR semantics
      Ns.bools.apply(List(true)).get === List(Set(true, false))
      Ns.bools.apply(List(false)).get === List(Set(true, false))
      Ns.bools.apply(List(true, false)).get === List(Set(true, false))
      Ns.bools.apply(List(true), List(false)).get === List(Set(true, false))
      Ns.bools.apply(List(true, false), List(true)).get === List(Set(true, false))
      Ns.bools.apply(List(true), List(false, true)).get === List(Set(true, false))

      // mixing Iterable types and value/variable ok
      Ns.bools.apply(List(bool1), Set(false, bool3)).get === List(Set(true, false))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.bools.apply(Set(true)).get === List(Set(true, false))
      Ns.bools.apply(Set(false)).get === List(Set(true, false))
      Ns.bools.apply(Set(true, false)).get === List(Set(true, false))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.bools.apply(Set(true, false), Set(true))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bools"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bools(true and false).get === List(Set(true, false))


      // Input

      val inputMolecule = m(Ns.bools(?))

      // AND semantics when applying true Set of values matching attribute values of true entity

      inputMolecule.apply(Set(true)).get === List(Set(true, false))
      inputMolecule.apply(Set(false)).get === List(Set(true, false))

      inputMolecule.apply(Set(true, false)).get === List(Set(true, false))

      inputMolecule.apply(List(Set(true))).get === List(Set(true, false))
      inputMolecule.apply(List(Set(false))).get === List(Set(true, false))
      inputMolecule.apply(List(Set(true, false))).get === List(Set(true, false))

      inputMolecule.apply(Set(Set(true))).get === List(Set(true, false))
      inputMolecule.apply(Set(Set(false))).get === List(Set(true, false))
      inputMolecule.apply(Set(Set(true, false))).get === List(Set(true, false))


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(true), Set(false)).get === List(Set(true, false))
      inputMolecule.apply(Set(true) or Set(false)).get === List(Set(true, false))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.bools_.apply(true).get === List(1, 3)
      Ns.int.bools_.apply(false).get === List(2, 3)
      Ns.int.bools_.apply(true, false).get === List(1, 2, 3)

      // Explicit `or` semantics
      Ns.int.bools_.apply(true or false).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.bools_.apply(List(true)).get === List(1, 3)
      Ns.int.bools_.apply(List(false)).get === List(2, 3)
      Ns.int.bools_.apply(List(true, false)).get === List(1, 2, 3)
      Ns.int.bools_.apply(List(true), List(false)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bools_.apply(Set(true)).get === List(1, 3)
      Ns.int.bools_.apply(Set(false)).get === List(2, 3)
      Ns.int.bools_.apply(Set(true, false)).get === List(3)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bools_.apply(Set(true), Set(false)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/bools_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bools_(true and false).get === List(3)


      // Input

      val inputMolecule = m(Ns.int.bools_(?))

      // AND semantics when applying true Set of values matching attribute values of true entity

      inputMolecule.apply(Set(true)).get === List(1, 3)
      inputMolecule.apply(Set(false)).get === List(2, 3)
      inputMolecule.apply(Set(true, false)).get === List(3)

      inputMolecule.apply(List(Set(true))).get === List(1, 3)
      inputMolecule.apply(List(Set(false))).get === List(2, 3)
      inputMolecule.apply(List(Set(true, false))).get === List(3)

      inputMolecule.apply(Set(Set(true))).get === List(1, 3)
      inputMolecule.apply(Set(Set(false))).get === List(2, 3)
      inputMolecule.apply(Set(Set(true, false))).get === List(3)


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(true), Set(true)).get === List(1, 3)
      inputMolecule.apply(Set(false), Set(false)).get === List(2, 3)
      inputMolecule.apply(Set(true), Set(false)).get === List(1, 2, 3)

      inputMolecule.apply(Set(true) or Set(true)).get === List(1, 3)
      inputMolecule.apply(Set(false) or Set(false)).get === List(2, 3)
      inputMolecule.apply(Set(true) or Set(false)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.bools.apply(true).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(false).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(true, false).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      // Explicit `or` semantics
      Ns.int.bools.apply(true or false).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      // Iterable: List - OR semantics
      Ns.int.bools.apply(List(true)).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(List(false)).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(List(true, false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(List(true), List(false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bools.apply(Set(true)).get === List((1, Set(true)), (3, Set(true, false)))
      Ns.int.bools.apply(Set(false)).get === List((2, Set(false)), (3, Set(true, false)))
      Ns.int.bools.apply(Set(true, false)).get === List((3, Set(true, false)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bools.apply(Set(true), Set(false)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bools"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bools(true and false).get === List((3, Set(true, false)))


      // Input

      val inputMolecule = m(Ns.int.bools(?))

      // AND semantics when applying true Set of values matching attribute values of true entity

      inputMolecule.apply(Set(true)).get === List((1, Set(true)), (3, Set(true, false)))
      inputMolecule.apply(Set(false)).get === List((2, Set(false)), (3, Set(true, false)))
      inputMolecule.apply(Set(true, false)).get === List((3, Set(true, false)))

      inputMolecule.apply(List(Set(true))).get === List((1, Set(true)), (3, Set(true, false)))
      inputMolecule.apply(List(Set(false))).get === List((2, Set(false)), (3, Set(true, false)))
      inputMolecule.apply(List(Set(true, false))).get === List((3, Set(true, false)))

      inputMolecule.apply(Set(Set(true))).get === List((1, Set(true)), (3, Set(true, false)))
      inputMolecule.apply(Set(Set(false))).get === List((2, Set(false)), (3, Set(true, false)))
      inputMolecule.apply(Set(Set(true, false))).get === List((3, Set(true, false)))


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(true), Set(true)).get === List((1, Set(true)), (3, Set(true, false)))
      inputMolecule.apply(Set(false), Set(false)).get === List((2, Set(false)), (3, Set(true, false)))
      inputMolecule.apply(Set(true), Set(false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))

      inputMolecule.apply(Set(true) or Set(true)).get === List((1, Set(true)), (3, Set(true, false)))
      inputMolecule.apply(Set(false) or Set(false)).get === List((2, Set(false)), (3, Set(true, false)))
      inputMolecule.apply(Set(true) or Set(false)).get === List((1, Set(true)), (2, Set(false)), (3, Set(true, false)))
    }
  }


  "Variable resolution" in new OneSetup {
    Ns.bool.apply(bool1, bool2).get === List(false, true)

    Ns.bool.apply(List(bool1, bool2), List(bool3)).get === List(false, true)
    Ns.bool.apply(l12, l3).get === List(false, true)

    Ns.bool.apply(Set(bool1, bool2), Set(bool3)).get === List(false, true)
    Ns.bool.apply(s12, s3).get === List(false, true)
  }
}