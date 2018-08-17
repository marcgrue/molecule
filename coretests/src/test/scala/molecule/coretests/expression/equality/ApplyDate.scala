package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyDate extends ApplyBase {

  val l1 = List(date1)
  val l2 = List(date2)
  val l3 = List(date3)

  val s1 = Set(date1)
  val s2 = Set(date2)
  val s3 = Set(date3)

  val l11 = List(date1, date1)
  val l12 = List(date1, date2)
  val l13 = List(date1, date3)
  val l23 = List(date2, date3)

  val s12 = Set(date1, date2)
  val s13 = Set(date1, date3)
  val s23 = Set(date2, date3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.date.apply(date1).get === List(date1)
      Ns.date.apply(date2).get === List(date2)
      Ns.date.apply(date1, date2).get === List(date1, date2)

      // Explicit `or` semantics
      Ns.date.apply(date1 or date2).get === List(date1, date2)
      Ns.date.apply(date1 or date2 or date3).get === List(date1, date3, date2)

      // Iterable: List - OR semantics
      Ns.date.apply(List(date1)).get === List(date1)
      Ns.date.apply(List(date2)).get === List(date2)
      Ns.date.apply(List(date1, date2)).get === List(date1, date2)
      Ns.date.apply(List(date1), List(date2)).get === List(date1, date2)
      Ns.date.apply(List(date1, date2), List(date3)).get=== List(date1, date3, date2)
      Ns.date.apply(List(date1), List(date2, date3)).get=== List(date1, date3, date2)

      // mixing Iterable types and value/variable ok
      Ns.date.apply(List(date1), Set(date2, date3)).get=== List(date1, date3, date2)

      // Iterable: Set (OR semantics)
      Ns.date.apply(Set(date1)).get === List(date1)
      Ns.date.apply(Set(date2)).get === List(date2)
      Ns.date.apply(Set(date1, date2)).get === List(date1, date2)
      Ns.date.apply(Set(date1), Set(date2)).get === List(date1, date2)
      Ns.date.apply(Set(date1, date2), Set(date3)).get=== List(date1, date3, date2)
      Ns.date.apply(Set(date1), Set(date2, date3)).get=== List(date1, date3, date2)


      // Input

      val inputMolecule = m(Ns.date(?))

      inputMolecule.apply(date1).get === List(date1)
      inputMolecule.apply(date2).get === List(date2)

      inputMolecule.apply(date1, date1).get === List(date1)
      inputMolecule.apply(date1, date2).get === List(date1, date2)

      inputMolecule.apply(List(date1)).get === List(date1)
      inputMolecule.apply(List(date1, date1)).get === List(date1)
      inputMolecule.apply(List(date1, date2)).get === List(date1, date2)

      inputMolecule.apply(Set(date1)).get === List(date1)
      inputMolecule.apply(Set(date1, date2)).get === List(date1, date2)

      inputMolecule.apply(date1 or date1).get === List(date1)
      inputMolecule.apply(date1 or date2).get === List(date1, date2)
      inputMolecule.apply(date1 or date2 or date3).get=== List(date1, date3, date2)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.date_.apply(date1).get === List(1)
      Ns.int.date_.apply(date2).get === List(2)
      Ns.int.date_.apply(date1, date2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.date_.apply(date1 or date2).get === List(1, 2)
      Ns.int.date_.apply(date1 or date2 or date3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.date_.apply(List(date1)).get === List(1)
      Ns.int.date_.apply(List(date2)).get === List(2)
      Ns.int.date_.apply(List(date1, date2)).get === List(1, 2)
      Ns.int.date_.apply(List(date1), List(date2)).get === List(1, 2)
      Ns.int.date_.apply(List(date1, date2), List(date3)).get === List(1, 2, 3)
      Ns.int.date_.apply(List(date1), List(date2, date3)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.date_.apply(Set(date1)).get === List(1)
      Ns.int.date_.apply(Set(date2)).get === List(2)
      Ns.int.date_.apply(Set(date1, date2)).get === List(1, 2)
      Ns.int.date_.apply(Set(date1), Set(date2)).get === List(1, 2)
      Ns.int.date_.apply(Set(date1, date2), Set(date3)).get === List(1, 2, 3)
      Ns.int.date_.apply(Set(date1), Set(date2, date3)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.date_(?))

      inputMolecule.apply(date1).get === List(1)
      inputMolecule.apply(date2).get === List(2)

      inputMolecule.apply(date1, date1).get === List(1)
      inputMolecule.apply(date1, date2).get === List(1, 2)

      inputMolecule.apply(List(date1)).get === List(1)
      inputMolecule.apply(List(date1, date1)).get === List(1)
      inputMolecule.apply(List(date1, date2)).get === List(1, 2)

      inputMolecule.apply(Set(date1)).get === List(1)
      inputMolecule.apply(Set(date1, date2)).get === List(1, 2)

      inputMolecule.apply(date1 or date1).get === List(1)
      inputMolecule.apply(date1 or date2).get === List(1, 2)
      inputMolecule.apply(date1 or date2 or date3).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.dates.apply(date1).get === List(Set(date1, date2))
      Ns.dates.apply(date2).get === List(Set(date1, date3, date2))
      Ns.dates.apply(date1, date2).get === List(Set(date1, date3, date2))

      // Explicit `or` semantics
      Ns.dates.apply(date1 or date2).get === List(Set(date1, date3, date2))
      Ns.dates.apply(date1 or date2 or date3).get === List(Set(date1, date4, date3, date2))

      // Iterable: List - OR semantics
      Ns.dates.apply(List(date1)).get === List(Set(date1, date2))
      Ns.dates.apply(List(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1, date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1), Set(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(List(date1, date2), List(date3)).get === List(Set(date1, date4, date3, date2))
      Ns.dates.apply(List(date1), List(date2, date3)).get === List(Set(date1, date4, date3, date2))

      // mixing Iterable types and value/variable ok
      Ns.dates.apply(List(date1), Set(date2, date3)).get === List(Set(date1, date4, date3, date2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.dates.apply(Set(date1)).get === List(Set(date1, date2))
      Ns.dates.apply(Set(date2)).get === List(Set(date1, date3, date2))
      Ns.dates.apply(Set(date1, date2)).get === List(Set(date1, date2))
      Ns.dates.apply(Set(date1, date3)).get === Nil
      Ns.dates.apply(Set(date2, date3)).get === List(Set(date2, date3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.dates.apply(Set(date1, date2), Set(date3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/dates"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.dates(date1 and date2).get === List(Set(date1, date2))
      Ns.dates(date1 and date3).get === Nil


      // Input

      val inputMolecule = m(Ns.dates(?))

      // AND semantics when applying date1 Set of values matching attribute values of date1 entity

      inputMolecule.apply(Set(date1)).get === List(Set(date1, date2))
      inputMolecule.apply(Set(date2)).get === List(Set(date1, date3, date2))

      inputMolecule.apply(Set(date1, date2)).get === List(Set(date1, date2))
      inputMolecule.apply(Set(date1, date3)).get === Nil
      inputMolecule.apply(Set(date2, date3)).get === List(Set(date2, date3))
      inputMolecule.apply(Set(date1, date2, date3)).get === Nil

      inputMolecule.apply(List(Set(date1))).get === List(Set(date1, date2))
      inputMolecule.apply(List(Set(date2))).get === List(Set(date1, date3, date2))
      inputMolecule.apply(List(Set(date1, date2))).get === List(Set(date1, date2))
      inputMolecule.apply(List(Set(date1, date3))).get === Nil
      inputMolecule.apply(List(Set(date2, date3))).get === List(Set(date2, date3))
      inputMolecule.apply(List(Set(date1, date2, date3))).get === Nil

      inputMolecule.apply(Set(Set(date1))).get === List(Set(date1, date2))
      inputMolecule.apply(Set(Set(date2))).get === List(Set(date1, date3, date2))
      inputMolecule.apply(Set(Set(date1, date2))).get === List(Set(date1, date2))
      inputMolecule.apply(Set(Set(date1, date3))).get === Nil
      inputMolecule.apply(Set(Set(date2, date3))).get === List(Set(date2, date3))
      inputMolecule.apply(Set(Set(date1, date2, date3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(date1), Set(date1)).get === List(Set(date1, date2))
      inputMolecule.apply(Set(date1), Set(date2)).get === List(Set(date1, date3, date2))
      inputMolecule.apply(Set(date1), Set(date3)).get === List(Set(date1, date4, date3, date2))
      inputMolecule.apply(Set(date2), Set(date3)).get === List(Set(date1, date4, date3, date2))
      inputMolecule.apply(Set(date1, date2), Set(date3)).get === List(Set(date1, date4, date3, date2))

      inputMolecule.apply(Set(date1) or Set(date1)).get === List(Set(date1, date2))
      inputMolecule.apply(Set(date1) or Set(date2)).get === List(Set(date1, date3, date2))
      inputMolecule.apply(Set(date1) or Set(date2) or Set(date3)).get === List(Set(date1, date4, date3, date2))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.dates_.apply(date1).get === List(1)
      Ns.int.dates_.apply(date2).get === List(1, 2)
      Ns.int.dates_.apply(date1, date2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.dates_.apply(date1 or date2).get === List(1, 2)
      Ns.int.dates_.apply(date1 or date2 or date3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.dates_.apply(List(date1)).get === List(1)
      Ns.int.dates_.apply(List(date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1, date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1), Set(date2)).get === List(1, 2)
      Ns.int.dates_.apply(List(date1, date2), List(date3)).get === List(1, 2, 3)
      Ns.int.dates_.apply(List(date1), List(date2, date3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.dates_.apply(List(date1), Set(date2, date3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.dates_.apply(Set(date1)).get === List(1)
      Ns.int.dates_.apply(Set(date2)).get === List(1, 2)
      Ns.int.dates_.apply(Set(date1, date2)).get === List(1)
      Ns.int.dates_.apply(Set(date1, date3)).get === Nil
      Ns.int.dates_.apply(Set(date2, date3)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.dates_.apply(Set(date1, date2), Set(date3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/dates_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.dates_(date1 and date2).get === List(1)
      Ns.int.dates_(date1 and date3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.dates_(?))

      // AND semantics when applying date1 Set of values matching attribute values of date1 entity

      inputMolecule.apply(Set(date1)).get === List(1)
      inputMolecule.apply(Set(date2)).get === List(1, 2)

      inputMolecule.apply(Set(date1, date2)).get === List(1)
      inputMolecule.apply(Set(date1, date3)).get === Nil
      inputMolecule.apply(Set(date2, date3)).get === List(2)
      inputMolecule.apply(Set(date1, date2, date3)).get === Nil

      inputMolecule.apply(List(Set(date1))).get === List(1)
      inputMolecule.apply(List(Set(date2))).get === List(1, 2)
      inputMolecule.apply(List(Set(date1, date2))).get === List(1)
      inputMolecule.apply(List(Set(date1, date3))).get === Nil
      inputMolecule.apply(List(Set(date2, date3))).get === List(2)
      inputMolecule.apply(List(Set(date1, date2, date3))).get === Nil

      inputMolecule.apply(Set(Set(date1))).get === List(1)
      inputMolecule.apply(Set(Set(date2))).get === List(1, 2)
      inputMolecule.apply(Set(Set(date1, date2))).get === List(1)
      inputMolecule.apply(Set(Set(date1, date3))).get === Nil
      inputMolecule.apply(Set(Set(date2, date3))).get === List(2)
      inputMolecule.apply(Set(Set(date1, date2, date3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(date1), Set(date1)).get === List(1)
      inputMolecule.apply(Set(date1), Set(date2)).get === List(1, 2)
      inputMolecule.apply(Set(date1), Set(date3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(date2), Set(date3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(date1, date2), Set(date3)).get === List(1, 2, 3)

      inputMolecule.apply(Set(date1) or Set(date1)).get === List(1)
      inputMolecule.apply(Set(date1) or Set(date2)).get === List(1, 2)
      inputMolecule.apply(Set(date1) or Set(date2) or Set(date3)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.dates.apply(date1).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(date1, date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))

      // Explicit `or` semantics
      Ns.int.dates.apply(date1 or date2).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(date1 or date2 or date3).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      // Iterable: List - OR semantics
      Ns.int.dates.apply(List(date1)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(List(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1, date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1), Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(List(date1, date2), List(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      Ns.int.dates.apply(List(date1), List(date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      // mixing Iterable types and value/variable ok
      Ns.int.dates.apply(List(date1), Set(date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      Ns.int.dates.apply(l1, Set(date2, date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.dates.apply(Set(date1)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      Ns.int.dates.apply(Set(date1, date2)).get === List((1, Set(date1, date2)))
      Ns.int.dates.apply(Set(date1, date3)).get === Nil
      Ns.int.dates.apply(Set(date2, date3)).get === List((2, Set(date2, date3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.dates.apply(Set(date1, date2), Set(date3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/dates"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.dates(date1 and date2).get === List((1, Set(date1, date2)))
      Ns.int.dates(date1 and date3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.dates(?))

      // AND semantics when applying date1 Set of values matching attribute values of date1 entity

      inputMolecule.apply(Set(date1)).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))

      inputMolecule.apply(Set(date1, date2)).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(date1, date3)).get === Nil
      inputMolecule.apply(Set(date2, date3)).get === List((2, Set(date2, date3)))
      inputMolecule.apply(Set(date1, date2, date3)).get === Nil

      inputMolecule.apply(List(Set(date1))).get === List((1, Set(date1, date2)))
      inputMolecule.apply(List(Set(date2))).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      inputMolecule.apply(List(Set(date1, date2))).get === List((1, Set(date1, date2)))
      inputMolecule.apply(List(Set(date1, date3))).get === Nil
      inputMolecule.apply(List(Set(date2, date3))).get === List((2, Set(date2, date3)))
      inputMolecule.apply(List(Set(date1, date2, date3))).get === Nil

      inputMolecule.apply(Set(Set(date1))).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(Set(date2))).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      inputMolecule.apply(Set(Set(date1, date2))).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(Set(date1, date3))).get === Nil
      inputMolecule.apply(Set(Set(date2, date3))).get === List((2, Set(date2, date3)))
      inputMolecule.apply(Set(Set(date1, date2, date3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(date1), Set(date1)).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(date1), Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      inputMolecule.apply(Set(date1), Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      inputMolecule.apply(Set(date2), Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
      inputMolecule.apply(Set(date1, date2), Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))

      inputMolecule.apply(Set(date1) or Set(date1)).get === List((1, Set(date1, date2)))
      inputMolecule.apply(Set(date1) or Set(date2)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)))
      inputMolecule.apply(Set(date1) or Set(date2) or Set(date3)).get === List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
    }
  }
}