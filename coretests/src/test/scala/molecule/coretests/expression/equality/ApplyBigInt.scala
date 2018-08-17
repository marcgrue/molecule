package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyBigInt extends ApplyBase {

  val l1 = List(bigInt1)
  val l2 = List(bigInt2)
  val l3 = List(bigInt3)

  val s1 = Set(bigInt1)
  val s2 = Set(bigInt2)
  val s3 = Set(bigInt3)

  val l11 = List(bigInt1, bigInt1)
  val l12 = List(bigInt1, bigInt2)
  val l13 = List(bigInt1, bigInt3)
  val l23 = List(bigInt2, bigInt3)

  val s12 = Set(bigInt1, bigInt2)
  val s13 = Set(bigInt1, bigInt3)
  val s23 = Set(bigInt2, bigInt3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg
      Ns.bigInt.apply(bigInt1).get === List(bigInt1)
      Ns.bigInt.apply(bigInt2).get === List(bigInt2)
      Ns.bigInt.apply(bigInt1, bigInt2).get === List(bigInt1, bigInt2)

      // Explicit `or` semantics
      Ns.bigInt.apply(bigInt1 or bigInt2).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(bigInt1 or bigInt2 or bigInt3).get === List(bigInt1, bigInt2, bigInt3)

      // Iterable: List - OR semantics
      Ns.bigInt.apply(List(bigInt1)).get === List(bigInt1)
      Ns.bigInt.apply(List(bigInt2)).get === List(bigInt2)
      Ns.bigInt.apply(List(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(List(bigInt1), List(bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.bigInt.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)

      // mixing Iterable types and value/variable ok
      Ns.bigInt.apply(List(bigInt1), Set(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)

      // Iterable: Set
      Ns.bigInt.apply(Set(bigInt1)).get === List(bigInt1)
      Ns.bigInt.apply(Set(bigInt2)).get === List(bigInt2)
      Ns.bigInt.apply(Set(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(Set(bigInt1), Set(bigInt2)).get === List(bigInt1, bigInt2)
      Ns.bigInt.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.bigInt.apply(Set(bigInt1), Set(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)


      // Input

      val inputMolecule = m(Ns.bigInt(?))

      inputMolecule.apply(bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt2).get === List(bigInt2)

      inputMolecule.apply(bigInt1, bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt1, bigInt2).get === List(bigInt1, bigInt2)

      inputMolecule.apply(List(bigInt1)).get === List(bigInt1)
      inputMolecule.apply(List(bigInt1, bigInt1)).get === List(bigInt1)
      inputMolecule.apply(List(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)

      inputMolecule.apply(Set(bigInt1)).get === List(bigInt1)
      inputMolecule.apply(Set(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)

      inputMolecule.apply(bigInt1 or bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt1 or bigInt2).get === List(bigInt1, bigInt2)
      inputMolecule.apply(bigInt1 or bigInt2 or bigInt3).get === List(bigInt1, bigInt2, bigInt3)
    }


    "Tacit" in new OneSetup {

      // Vararg
      Ns.int.bigInt_.apply(bigInt1).get === List(bigInt1)
      Ns.int.bigInt_.apply(bigInt2).get === List(bigInt2)
      Ns.int.bigInt_.apply(bigInt1, bigInt2).get === List(bigInt1, bigInt2)

      // Explicit `or` semantics
      Ns.int.bigInt_.apply(bigInt1 or bigInt2).get === List(bigInt1, bigInt2)
      Ns.int.bigInt_.apply(bigInt1 or bigInt2 or bigInt3).get === List(bigInt1, bigInt2, bigInt3)

      // Iterable: List - OR semantics
      Ns.int.bigInt_.apply(List(bigInt1)).get === List(bigInt1)
      Ns.int.bigInt_.apply(List(bigInt2)).get === List(bigInt2)
      Ns.int.bigInt_.apply(List(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)
      Ns.int.bigInt_.apply(List(bigInt1), List(bigInt2)).get === List(bigInt1, bigInt2)
      Ns.int.bigInt_.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.int.bigInt_.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)

      // Iterable: Set
      Ns.int.bigInt_.apply(Set(bigInt1)).get === List(bigInt1)
      Ns.int.bigInt_.apply(Set(bigInt2)).get === List(bigInt2)
      Ns.int.bigInt_.apply(Set(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)
      Ns.int.bigInt_.apply(Set(bigInt1), Set(bigInt2)).get === List(bigInt1, bigInt2)
      Ns.int.bigInt_.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(bigInt1, bigInt2, bigInt3)
      Ns.int.bigInt_.apply(Set(bigInt1), Set(bigInt2, bigInt3)).get === List(bigInt1, bigInt2, bigInt3)


      // Input

      val inputMolecule = m(Ns.int.bigInt_(?))

      inputMolecule.apply(bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt2).get === List(bigInt2)

      inputMolecule.apply(bigInt1, bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt1, bigInt2).get === List(bigInt1, bigInt2)

      inputMolecule.apply(List(bigInt1)).get === List(bigInt1)
      inputMolecule.apply(List(bigInt1, bigInt1)).get === List(bigInt1)
      inputMolecule.apply(List(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)

      inputMolecule.apply(Set(bigInt1)).get === List(bigInt1)
      inputMolecule.apply(Set(bigInt1, bigInt2)).get === List(bigInt1, bigInt2)

      inputMolecule.apply(bigInt1 or bigInt1).get === List(bigInt1)
      inputMolecule.apply(bigInt1 or bigInt2).get === List(bigInt1, bigInt2)
      inputMolecule.apply(bigInt1 or bigInt2 or bigInt3).get === List(bigInt1, bigInt2, bigInt3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg
      Ns.bigInts.apply(bigInt1).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(bigInt1, bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))

      // Explicit `or` semantics
      Ns.bigInts.apply(bigInt1 or bigInt2).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(bigInt1 or bigInt2 or bigInt3).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))

      // Iterable: List - OR semantics
      Ns.bigInts.apply(List(bigInt1)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(List(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1), Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
      Ns.bigInts.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))

      // mixing Iterable types and value/variable ok
      Ns.bigInts.apply(List(bigInt1), Set(bigInt2, bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.bigInts.apply(Set(bigInt1)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      Ns.bigInts.apply(Set(bigInt1, bigInt2)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.bigInts.apply(Set(bigInt2, bigInt3)).get === List(Set(bigInt2, bigInt3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bigInts"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.bigInts(bigInt1 and bigInt2).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts(bigInt1 and bigInt3).get === Nil


      // Input

      val inputMolecule = m(Ns.bigInts(?))

      // AND semantics when applying bigInt1 Set of values matching attribute values of bigInt1 entity

      inputMolecule.apply(Set(bigInt1)).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))

      inputMolecule.apply(Set(bigInt1, bigInt2)).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(bigInt1, bigInt3)).get === Nil
      inputMolecule.apply(Set(bigInt2, bigInt3)).get === List(Set(bigInt2, bigInt3))
      inputMolecule.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      inputMolecule.apply(List(Set(bigInt1))).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(List(Set(bigInt2))).get === List(Set(bigInt1, bigInt3, bigInt2))
      inputMolecule.apply(List(Set(bigInt1, bigInt2))).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(List(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(List(Set(bigInt2, bigInt3))).get === List(Set(bigInt2, bigInt3))
      inputMolecule.apply(List(Set(bigInt1, bigInt2, bigInt3))).get === Nil

      inputMolecule.apply(Set(Set(bigInt1))).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(Set(bigInt2))).get === List(Set(bigInt1, bigInt3, bigInt2))
      inputMolecule.apply(Set(Set(bigInt1, bigInt2))).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(Set(Set(bigInt2, bigInt3))).get === List(Set(bigInt2, bigInt3))
      inputMolecule.apply(Set(Set(bigInt1, bigInt2, bigInt3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigInt1), Set(bigInt1)).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(bigInt1), Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      inputMolecule.apply(Set(bigInt1), Set(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
      inputMolecule.apply(Set(bigInt2), Set(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
      inputMolecule.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))

      inputMolecule.apply(Set(bigInt1) or Set(bigInt1)).get === List(Set(bigInt1, bigInt2))
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2)).get === List(Set(bigInt1, bigInt3, bigInt2))
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2) or Set(bigInt3)).get === List(Set(bigInt1, bigInt4, bigInt3, bigInt2))
    }


    "Tacit" in new ManySetup {

      // Vararg
      Ns.int.bigInts_.apply(bigInt1).get === List(1)
      Ns.int.bigInts_.apply(bigInt2).get === List(1, 2)
      Ns.int.bigInts_.apply(bigInt1, bigInt2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.bigInts_.apply(bigInt1 or bigInt2).get === List(1, 2)
      Ns.int.bigInts_.apply(bigInt1 or bigInt2 or bigInt3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.bigInts_.apply(List(bigInt1)).get === List(1)
      Ns.int.bigInts_.apply(List(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1), Set(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List(1, 2, 3)
      Ns.int.bigInts_.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.bigInts_.apply(List(bigInt1), Set(bigInt2, bigInt3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bigInts_.apply(Set(bigInt1)).get === List(1)
      Ns.int.bigInts_.apply(Set(bigInt2)).get === List(1, 2)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt2)).get === List(1)
      Ns.int.bigInts_.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.int.bigInts_.apply(Set(bigInt2, bigInt3)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bigInts_.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/bigInts_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigInts_(bigInt1 and bigInt2).get === List(1)
      Ns.int.bigInts_(bigInt1 and bigInt3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.bigInts_(?))

      // AND semantics when applying bigInt1 Set of values matching attribute values of bigInt1 entity

      inputMolecule.apply(Set(bigInt1)).get === List(1)
      inputMolecule.apply(Set(bigInt2)).get === List(1, 2)

      inputMolecule.apply(Set(bigInt1, bigInt2)).get === List(1)
      inputMolecule.apply(Set(bigInt1, bigInt3)).get === Nil
      inputMolecule.apply(Set(bigInt2, bigInt3)).get === List(2)
      inputMolecule.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      inputMolecule.apply(List(Set(bigInt1))).get === List(1)
      inputMolecule.apply(List(Set(bigInt2))).get === List(1, 2)
      inputMolecule.apply(List(Set(bigInt1, bigInt2))).get === List(1)
      inputMolecule.apply(List(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(List(Set(bigInt2, bigInt3))).get === List(2)
      inputMolecule.apply(List(Set(bigInt1, bigInt2, bigInt3))).get === Nil

      inputMolecule.apply(Set(Set(bigInt1))).get === List(1)
      inputMolecule.apply(Set(Set(bigInt2))).get === List(1, 2)
      inputMolecule.apply(Set(Set(bigInt1, bigInt2))).get === List(1)
      inputMolecule.apply(Set(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(Set(Set(bigInt2, bigInt3))).get === List(2)
      inputMolecule.apply(Set(Set(bigInt1, bigInt2, bigInt3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigInt1), Set(bigInt1)).get === List(1)
      inputMolecule.apply(Set(bigInt1), Set(bigInt2)).get === List(1, 2)
      inputMolecule.apply(Set(bigInt1), Set(bigInt3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(bigInt2), Set(bigInt3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List(1, 2, 3)

      inputMolecule.apply(Set(bigInt1) or Set(bigInt1)).get === List(1)
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2)).get === List(1, 2)
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2) or Set(bigInt3)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg
      Ns.int.bigInts.apply(bigInt1).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(bigInt1, bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))

      // Explicit `or` semantics
      Ns.int.bigInts.apply(bigInt1 or bigInt2).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(bigInt1 or bigInt2 or bigInt3).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      // Iterable: List - OR semantics
      Ns.int.bigInts.apply(List(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(List(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1, bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1), Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(List(bigInt1, bigInt2), List(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(List(bigInt1), List(bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      // mixing Iterable types and value/variable ok
      Ns.int.bigInts.apply(List(bigInt1), Set(bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      Ns.int.bigInts.apply(l1, Set(bigInt2, bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.bigInts.apply(Set(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt2)).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts.apply(Set(bigInt1, bigInt3)).get === Nil
      Ns.int.bigInts.apply(Set(bigInt2, bigInt3)).get === List((2, Set(bigInt2, bigInt3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.bigInts.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/bigInts"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.bigInts(bigInt1 and bigInt2).get === List((1, Set(bigInt1, bigInt2)))
      Ns.int.bigInts(bigInt1 and bigInt3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.bigInts(?))

      // AND semantics when applying bigInt1 Set of values matching attribute values of bigInt1 entity

      inputMolecule.apply(Set(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))

      inputMolecule.apply(Set(bigInt1, bigInt2)).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(bigInt1, bigInt3)).get === Nil
      inputMolecule.apply(Set(bigInt2, bigInt3)).get === List((2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(Set(bigInt1, bigInt2, bigInt3)).get === Nil

      inputMolecule.apply(List(Set(bigInt1))).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(List(Set(bigInt2))).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(List(Set(bigInt1, bigInt2))).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(List(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(List(Set(bigInt2, bigInt3))).get === List((2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(List(Set(bigInt1, bigInt2, bigInt3))).get === Nil

      inputMolecule.apply(Set(Set(bigInt1))).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(Set(bigInt2))).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(Set(Set(bigInt1, bigInt2))).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(Set(bigInt1, bigInt3))).get === Nil
      inputMolecule.apply(Set(Set(bigInt2, bigInt3))).get === List((2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(Set(Set(bigInt1, bigInt2, bigInt3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(bigInt1), Set(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(bigInt1), Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(Set(bigInt1), Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      inputMolecule.apply(Set(bigInt2), Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
      inputMolecule.apply(Set(bigInt1, bigInt2), Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))

      inputMolecule.apply(Set(bigInt1) or Set(bigInt1)).get === List((1, Set(bigInt1, bigInt2)))
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)))
      inputMolecule.apply(Set(bigInt1) or Set(bigInt2) or Set(bigInt3)).get === List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
    }
  }
}