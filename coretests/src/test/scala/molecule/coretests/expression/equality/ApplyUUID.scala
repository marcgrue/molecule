package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyUUID extends ApplyBase {

  val l1 = List(uuid1)
  val l2 = List(uuid2)
  val l3 = List(uuid3)

  val s1 = Set(uuid1)
  val s2 = Set(uuid2)
  val s3 = Set(uuid3)

  val l11 = List(uuid1, uuid1)
  val l12 = List(uuid1, uuid2)
  val l13 = List(uuid1, uuid3)
  val l23 = List(uuid2, uuid3)

  val s12 = Set(uuid1, uuid2)
  val s13 = Set(uuid1, uuid3)
  val s23 = Set(uuid2, uuid3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.uuid.apply(uuid1).get === List(uuid1)
      Ns.uuid.apply(uuid2).get === List(uuid2)
      Ns.uuid.apply(uuid1, uuid2).get === List(uuid1, uuid2)

      // Explicit `or` semantics
      Ns.uuid.apply(uuid1 or uuid2).get.sortBy(_.toString) === List(uuid2, uuid1).sortBy(_.toString)
      Ns.uuid.apply(uuid1 or uuid2 or uuid3).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)

      // Iterable: List - OR semantics
      Ns.uuid.apply(List(uuid1)).get === List(uuid1)
      Ns.uuid.apply(List(uuid2)).get === List(uuid2)
      Ns.uuid.apply(List(uuid1, uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(List(uuid1), List(uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(List(uuid1, uuid2), List(uuid3)).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)
      Ns.uuid.apply(List(uuid1), List(uuid2, uuid3)).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)

      // mixing Iterable types and value/variable ok
      Ns.uuid.apply(List(uuid1), Set(uuid2, uuid3)).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)

      // Iterable: Set (OR semantics)
      Ns.uuid.apply(Set(uuid1)).get === List(uuid1)
      Ns.uuid.apply(Set(uuid2)).get === List(uuid2)
      Ns.uuid.apply(Set(uuid1, uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(Set(uuid1), Set(uuid2)).get === List(uuid1, uuid2)
      Ns.uuid.apply(Set(uuid1, uuid2), Set(uuid3)).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)
      Ns.uuid.apply(Set(uuid1), Set(uuid2, uuid3)).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)


      // Input

      val inputMolecule = m(Ns.uuid(?))

      inputMolecule.apply(uuid1).get === List(uuid1)
      inputMolecule.apply(uuid2).get === List(uuid2)

      inputMolecule.apply(uuid1, uuid1).get === List(uuid1)
      inputMolecule.apply(uuid1, uuid2).get === List(uuid1, uuid2)

      inputMolecule.apply(List(uuid1)).get === List(uuid1)
      inputMolecule.apply(List(uuid1, uuid1)).get === List(uuid1)
      inputMolecule.apply(List(uuid1, uuid2)).get === List(uuid1, uuid2)

      inputMolecule.apply(Set(uuid1)).get === List(uuid1)
      inputMolecule.apply(Set(uuid1, uuid2)).get === List(uuid1, uuid2)

      inputMolecule.apply(uuid1 or uuid1).get === List(uuid1)
      inputMolecule.apply(uuid1 or uuid2).get === List(uuid1, uuid2)
      inputMolecule.apply(uuid1 or uuid2 or uuid3).get.sortBy(_.toString) === List(uuid1, uuid2, uuid3).sortBy(_.toString)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.uuid_.apply(uuid1).get === List(1)
      Ns.int.uuid_.apply(uuid2).get === List(2)
      Ns.int.uuid_.apply(uuid1, uuid2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.uuid_.apply(uuid1 or uuid2).get === List(1, 2)
      Ns.int.uuid_.apply(uuid1 or uuid2 or uuid3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.uuid_.apply(List(uuid1)).get === List(1)
      Ns.int.uuid_.apply(List(uuid2)).get === List(2)
      Ns.int.uuid_.apply(List(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(List(uuid1), List(uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(List(uuid1, uuid2), List(uuid3)).get === List(1, 2, 3)
      Ns.int.uuid_.apply(List(uuid1), List(uuid2, uuid3)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.uuid_.apply(Set(uuid1)).get === List(1)
      Ns.int.uuid_.apply(Set(uuid2)).get === List(2)
      Ns.int.uuid_.apply(Set(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(Set(uuid1), Set(uuid2)).get === List(1, 2)
      Ns.int.uuid_.apply(Set(uuid1, uuid2), Set(uuid3)).get === List(1, 2, 3)
      Ns.int.uuid_.apply(Set(uuid1), Set(uuid2, uuid3)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.uuid_(?))

      inputMolecule.apply(uuid1).get === List(1)
      inputMolecule.apply(uuid2).get === List(2)

      inputMolecule.apply(uuid1, uuid1).get === List(1)
      inputMolecule.apply(uuid1, uuid2).get === List(1, 2)

      inputMolecule.apply(List(uuid1)).get === List(1)
      inputMolecule.apply(List(uuid1, uuid1)).get === List(1)
      inputMolecule.apply(List(uuid1, uuid2)).get === List(1, 2)

      inputMolecule.apply(Set(uuid1)).get === List(1)
      inputMolecule.apply(Set(uuid1, uuid2)).get === List(1, 2)

      inputMolecule.apply(uuid1 or uuid1).get === List(1)
      inputMolecule.apply(uuid1 or uuid2).get === List(1, 2)
      inputMolecule.apply(uuid1 or uuid2 or uuid3).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.uuids.apply(uuid1).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(uuid2).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(uuid1, uuid2).get === List(Set(uuid1, uuid3, uuid2))

      // Explicit `or` semantics
      Ns.uuids.apply(uuid1 or uuid2).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(uuid1 or uuid2 or uuid3).get === List(Set(uuid1, uuid4, uuid3, uuid2))

      // Iterable: List - OR semantics
      Ns.uuids.apply(List(uuid1)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(List(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1, uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1), Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1, uuid2), List(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
      Ns.uuids.apply(List(uuid1), List(uuid2, uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))

      // mixing Iterable types and value/variable ok
      Ns.uuids.apply(List(uuid1), Set(uuid2, uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.uuids.apply(Set(uuid1)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      Ns.uuids.apply(Set(uuid1, uuid2)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(Set(uuid1, uuid3)).get === Nil
      Ns.uuids.apply(Set(uuid2, uuid3)).get === List(Set(uuid2, uuid3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.uuids.apply(Set(uuid1, uuid2), Set(uuid3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/uuids"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.uuids(uuid1 and uuid2).get === List(Set(uuid1, uuid2))
      Ns.uuids(uuid1 and uuid3).get === Nil


      // Input

      val inputMolecule = m(Ns.uuids(?))

      // AND semantics when applying uuid1 Set of values matching attribute values of uuid1 entity

      inputMolecule.apply(Set(uuid1)).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))

      inputMolecule.apply(Set(uuid1, uuid2)).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(uuid1, uuid3)).get === Nil
      inputMolecule.apply(Set(uuid2, uuid3)).get === List(Set(uuid2, uuid3))
      inputMolecule.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      inputMolecule.apply(List(Set(uuid1))).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(List(Set(uuid2))).get === List(Set(uuid1, uuid3, uuid2))
      inputMolecule.apply(List(Set(uuid1, uuid2))).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(List(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(List(Set(uuid2, uuid3))).get === List(Set(uuid2, uuid3))
      inputMolecule.apply(List(Set(uuid1, uuid2, uuid3))).get === Nil

      inputMolecule.apply(Set(Set(uuid1))).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(Set(uuid2))).get === List(Set(uuid1, uuid3, uuid2))
      inputMolecule.apply(Set(Set(uuid1, uuid2))).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(Set(Set(uuid2, uuid3))).get === List(Set(uuid2, uuid3))
      inputMolecule.apply(Set(Set(uuid1, uuid2, uuid3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uuid1), Set(uuid1)).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(uuid1), Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      inputMolecule.apply(Set(uuid1), Set(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
      inputMolecule.apply(Set(uuid2), Set(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
      inputMolecule.apply(Set(uuid1, uuid2), Set(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))

      inputMolecule.apply(Set(uuid1) or Set(uuid1)).get === List(Set(uuid1, uuid2))
      inputMolecule.apply(Set(uuid1) or Set(uuid2)).get === List(Set(uuid1, uuid3, uuid2))
      inputMolecule.apply(Set(uuid1) or Set(uuid2) or Set(uuid3)).get === List(Set(uuid1, uuid4, uuid3, uuid2))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.uuids_.apply(uuid1).get === List(1)
      Ns.int.uuids_.apply(uuid2).get === List(1, 2)
      Ns.int.uuids_.apply(uuid1, uuid2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.uuids_.apply(uuid1 or uuid2).get === List(1, 2)
      Ns.int.uuids_.apply(uuid1 or uuid2 or uuid3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.uuids_.apply(List(uuid1)).get === List(1)
      Ns.int.uuids_.apply(List(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1, uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1), Set(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(List(uuid1, uuid2), List(uuid3)).get === List(1, 2, 3)
      Ns.int.uuids_.apply(List(uuid1), List(uuid2, uuid3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.uuids_.apply(List(uuid1), Set(uuid2, uuid3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.uuids_.apply(Set(uuid1)).get === List(1)
      Ns.int.uuids_.apply(Set(uuid2)).get === List(1, 2)
      Ns.int.uuids_.apply(Set(uuid1, uuid2)).get === List(1)
      Ns.int.uuids_.apply(Set(uuid1, uuid3)).get === Nil
      Ns.int.uuids_.apply(Set(uuid2, uuid3)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.uuids_.apply(Set(uuid1, uuid2), Set(uuid3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/uuids_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uuids_(uuid1 and uuid2).get === List(1)
      Ns.int.uuids_(uuid1 and uuid3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.uuids_(?))

      // AND semantics when applying uuid1 Set of values matching attribute values of uuid1 entity

      inputMolecule.apply(Set(uuid1)).get === List(1)
      inputMolecule.apply(Set(uuid2)).get === List(1, 2)

      inputMolecule.apply(Set(uuid1, uuid2)).get === List(1)
      inputMolecule.apply(Set(uuid1, uuid3)).get === Nil
      inputMolecule.apply(Set(uuid2, uuid3)).get === List(2)
      inputMolecule.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      inputMolecule.apply(List(Set(uuid1))).get === List(1)
      inputMolecule.apply(List(Set(uuid2))).get === List(1, 2)
      inputMolecule.apply(List(Set(uuid1, uuid2))).get === List(1)
      inputMolecule.apply(List(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(List(Set(uuid2, uuid3))).get === List(2)
      inputMolecule.apply(List(Set(uuid1, uuid2, uuid3))).get === Nil

      inputMolecule.apply(Set(Set(uuid1))).get === List(1)
      inputMolecule.apply(Set(Set(uuid2))).get === List(1, 2)
      inputMolecule.apply(Set(Set(uuid1, uuid2))).get === List(1)
      inputMolecule.apply(Set(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(Set(Set(uuid2, uuid3))).get === List(2)
      inputMolecule.apply(Set(Set(uuid1, uuid2, uuid3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uuid1), Set(uuid1)).get === List(1)
      inputMolecule.apply(Set(uuid1), Set(uuid2)).get === List(1, 2)
      inputMolecule.apply(Set(uuid1), Set(uuid3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(uuid2), Set(uuid3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(uuid1, uuid2), Set(uuid3)).get === List(1, 2, 3)

      inputMolecule.apply(Set(uuid1) or Set(uuid1)).get === List(1)
      inputMolecule.apply(Set(uuid1) or Set(uuid2)).get === List(1, 2)
      inputMolecule.apply(Set(uuid1) or Set(uuid2) or Set(uuid3)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.uuids.apply(uuid1).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(uuid1, uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))

      // Explicit `or` semantics
      Ns.int.uuids.apply(uuid1 or uuid2).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(uuid1 or uuid2 or uuid3).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      // Iterable: List - OR semantics
      Ns.int.uuids.apply(List(uuid1)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(List(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1, uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1), Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(List(uuid1, uuid2), List(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(List(uuid1), List(uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      // mixing Iterable types and value/variable ok
      Ns.int.uuids.apply(List(uuid1), Set(uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      Ns.int.uuids.apply(l1, Set(uuid2, uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.uuids.apply(Set(uuid1)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      Ns.int.uuids.apply(Set(uuid1, uuid2)).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids.apply(Set(uuid1, uuid3)).get === Nil
      Ns.int.uuids.apply(Set(uuid2, uuid3)).get === List((2, Set(uuid2, uuid3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.uuids.apply(Set(uuid1, uuid2), Set(uuid3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/uuids"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uuids(uuid1 and uuid2).get === List((1, Set(uuid1, uuid2)))
      Ns.int.uuids(uuid1 and uuid3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.uuids(?))

      // AND semantics when applying uuid1 Set of values matching attribute values of uuid1 entity

      inputMolecule.apply(Set(uuid1)).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))

      inputMolecule.apply(Set(uuid1, uuid2)).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(uuid1, uuid3)).get === Nil
      inputMolecule.apply(Set(uuid2, uuid3)).get === List((2, Set(uuid2, uuid3)))
      inputMolecule.apply(Set(uuid1, uuid2, uuid3)).get === Nil

      inputMolecule.apply(List(Set(uuid1))).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(List(Set(uuid2))).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      inputMolecule.apply(List(Set(uuid1, uuid2))).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(List(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(List(Set(uuid2, uuid3))).get === List((2, Set(uuid2, uuid3)))
      inputMolecule.apply(List(Set(uuid1, uuid2, uuid3))).get === Nil

      inputMolecule.apply(Set(Set(uuid1))).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(Set(uuid2))).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      inputMolecule.apply(Set(Set(uuid1, uuid2))).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(Set(uuid1, uuid3))).get === Nil
      inputMolecule.apply(Set(Set(uuid2, uuid3))).get === List((2, Set(uuid2, uuid3)))
      inputMolecule.apply(Set(Set(uuid1, uuid2, uuid3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uuid1), Set(uuid1)).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(uuid1), Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      inputMolecule.apply(Set(uuid1), Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      inputMolecule.apply(Set(uuid2), Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
      inputMolecule.apply(Set(uuid1, uuid2), Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))

      inputMolecule.apply(Set(uuid1) or Set(uuid1)).get === List((1, Set(uuid1, uuid2)))
      inputMolecule.apply(Set(uuid1) or Set(uuid2)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)))
      inputMolecule.apply(Set(uuid1) or Set(uuid2) or Set(uuid3)).get === List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
    }
  }
}