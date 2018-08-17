package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyURI extends ApplyBase {

  val l1 = List(uri1)
  val l2 = List(uri2)
  val l3 = List(uri3)

  val s1 = Set(uri1)
  val s2 = Set(uri2)
  val s3 = Set(uri3)

  val l11 = List(uri1, uri1)
  val l12 = List(uri1, uri2)
  val l13 = List(uri1, uri3)
  val l23 = List(uri2, uri3)

  val s12 = Set(uri1, uri2)
  val s13 = Set(uri1, uri3)
  val s23 = Set(uri2, uri3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.uri.apply(uri1).get === List(uri1)
      Ns.uri.apply(uri2).get === List(uri2)
      Ns.uri.apply(uri1, uri2).get === List(uri1, uri2)

      // Explicit `or` semantics
      Ns.uri.apply(uri1 or uri2).get.sortBy(_.toString) === List(uri1, uri2).sortBy(_.toString)
      Ns.uri.apply(uri1 or uri2 or uri3).get.sortBy(_.toString) === List(uri1, uri2, uri3).sortBy(_.toString)

      // Iterable: List - OR semantics
      Ns.uri.apply(List(uri1)).get === List(uri1)
      Ns.uri.apply(List(uri2)).get === List(uri2)
      Ns.uri.apply(List(uri1, uri2)).get === List(uri1, uri2)
      Ns.uri.apply(List(uri1), List(uri2)).get === List(uri1, uri2)
      Ns.uri.apply(List(uri1, uri2), List(uri3)).get === List(uri3, uri1, uri2)
      Ns.uri.apply(List(uri1), List(uri2, uri3)).get === List(uri3, uri1, uri2)

      // mixing Iterable types and value/variable ok
      Ns.uri.apply(List(uri1), Set(uri2, uri3)).get === List(uri3, uri1, uri2)

      // Iterable: Set (OR semantics)
      Ns.uri.apply(Set(uri1)).get === List(uri1)
      Ns.uri.apply(Set(uri2)).get === List(uri2)
      Ns.uri.apply(Set(uri1, uri2)).get === List(uri1, uri2)
      Ns.uri.apply(Set(uri1), Set(uri2)).get === List(uri1, uri2)
      Ns.uri.apply(Set(uri1, uri2), Set(uri3)).get === List(uri3, uri1, uri2)
      Ns.uri.apply(Set(uri1), Set(uri2, uri3)).get === List(uri3, uri1, uri2)


      // Input

      val inputMolecule = m(Ns.uri(?))

      inputMolecule.apply(uri1).get === List(uri1)
      inputMolecule.apply(uri2).get === List(uri2)

      inputMolecule.apply(uri1, uri1).get === List(uri1)
      inputMolecule.apply(uri1, uri2).get === List(uri1, uri2)

      inputMolecule.apply(List(uri1)).get === List(uri1)
      inputMolecule.apply(List(uri1, uri1)).get === List(uri1)
      inputMolecule.apply(List(uri1, uri2)).get === List(uri1, uri2)

      inputMolecule.apply(Set(uri1)).get === List(uri1)
      inputMolecule.apply(Set(uri1, uri2)).get === List(uri1, uri2)

      inputMolecule.apply(uri1 or uri1).get === List(uri1)
      inputMolecule.apply(uri1 or uri2).get === List(uri1, uri2)
      inputMolecule.apply(uri1 or uri2 or uri3).get === List(uri3, uri1, uri2)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.uri_.apply(uri1).get === List(1)
      Ns.int.uri_.apply(uri2).get === List(2)
      Ns.int.uri_.apply(uri1, uri2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.uri_.apply(uri1 or uri2).get === List(1, 2)
      Ns.int.uri_.apply(uri1 or uri2 or uri3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.uri_.apply(List(uri1)).get === List(1)
      Ns.int.uri_.apply(List(uri2)).get === List(2)
      Ns.int.uri_.apply(List(uri1, uri2)).get === List(1, 2)
      Ns.int.uri_.apply(List(uri1), List(uri2)).get === List(1, 2)
      Ns.int.uri_.apply(List(uri1, uri2), List(uri3)).get === List(1, 2, 3)
      Ns.int.uri_.apply(List(uri1), List(uri2, uri3)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.uri_.apply(Set(uri1)).get === List(1)
      Ns.int.uri_.apply(Set(uri2)).get === List(2)
      Ns.int.uri_.apply(Set(uri1, uri2)).get === List(1, 2)
      Ns.int.uri_.apply(Set(uri1), Set(uri2)).get === List(1, 2)
      Ns.int.uri_.apply(Set(uri1, uri2), Set(uri3)).get === List(1, 2, 3)
      Ns.int.uri_.apply(Set(uri1), Set(uri2, uri3)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.uri_(?))

      inputMolecule.apply(uri1).get === List(1)
      inputMolecule.apply(uri2).get === List(2)

      inputMolecule.apply(uri1, uri1).get === List(1)
      inputMolecule.apply(uri1, uri2).get === List(1, 2)

      inputMolecule.apply(List(uri1)).get === List(1)
      inputMolecule.apply(List(uri1, uri1)).get === List(1)
      inputMolecule.apply(List(uri1, uri2)).get === List(1, 2)

      inputMolecule.apply(Set(uri1)).get === List(1)
      inputMolecule.apply(Set(uri1, uri2)).get === List(1, 2)

      inputMolecule.apply(uri1 or uri1).get === List(1)
      inputMolecule.apply(uri1 or uri2).get === List(1, 2)
      inputMolecule.apply(uri1 or uri2 or uri3).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.uris.apply(uri1).get === List(Set(uri1, uri2))
      Ns.uris.apply(uri2).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(uri1, uri2).get === List(Set(uri1, uri3, uri2))

      // Explicit `or` semantics
      Ns.uris.apply(uri1 or uri2).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(uri1 or uri2 or uri3).get === List(Set(uri1, uri4, uri3, uri2))

      // Iterable: List - OR semantics
      Ns.uris.apply(List(uri1)).get === List(Set(uri1, uri2))
      Ns.uris.apply(List(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1, uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1), Set(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(List(uri1, uri2), List(uri3)).get === List(Set(uri1, uri4, uri3, uri2))
      Ns.uris.apply(List(uri1), List(uri2, uri3)).get === List(Set(uri1, uri4, uri3, uri2))

      // mixing Iterable types and value/variable ok
      Ns.uris.apply(List(uri1), Set(uri2, uri3)).get === List(Set(uri1, uri4, uri3, uri2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.uris.apply(Set(uri1)).get === List(Set(uri1, uri2))
      Ns.uris.apply(Set(uri2)).get === List(Set(uri1, uri3, uri2))
      Ns.uris.apply(Set(uri1, uri2)).get === List(Set(uri1, uri2))
      Ns.uris.apply(Set(uri1, uri3)).get === Nil
      Ns.uris.apply(Set(uri2, uri3)).get === List(Set(uri2, uri3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.uris.apply(Set(uri1, uri2), Set(uri3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/uris"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.uris(uri1 and uri2).get === List(Set(uri1, uri2))
      Ns.uris(uri1 and uri3).get === Nil


      // Input

      val inputMolecule = m(Ns.uris(?))

      // AND semantics when applying uri1 Set of values matching attribute values of uri1 entity

      inputMolecule.apply(Set(uri1)).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(uri2)).get === List(Set(uri1, uri3, uri2))

      inputMolecule.apply(Set(uri1, uri2)).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(uri1, uri3)).get === Nil
      inputMolecule.apply(Set(uri2, uri3)).get === List(Set(uri2, uri3))
      inputMolecule.apply(Set(uri3, uri1, uri2)).get === Nil

      inputMolecule.apply(List(Set(uri1))).get === List(Set(uri1, uri2))
      inputMolecule.apply(List(Set(uri2))).get === List(Set(uri1, uri3, uri2))
      inputMolecule.apply(List(Set(uri1, uri2))).get === List(Set(uri1, uri2))
      inputMolecule.apply(List(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(List(Set(uri2, uri3))).get === List(Set(uri2, uri3))
      inputMolecule.apply(List(Set(uri3, uri1, uri2))).get === Nil

      inputMolecule.apply(Set(Set(uri1))).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(Set(uri2))).get === List(Set(uri1, uri3, uri2))
      inputMolecule.apply(Set(Set(uri1, uri2))).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(Set(Set(uri2, uri3))).get === List(Set(uri2, uri3))
      inputMolecule.apply(Set(Set(uri3, uri1, uri2))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uri1), Set(uri1)).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(uri1), Set(uri2)).get === List(Set(uri1, uri3, uri2))
      inputMolecule.apply(Set(uri1), Set(uri3)).get === List(Set(uri1, uri4, uri3, uri2))
      inputMolecule.apply(Set(uri2), Set(uri3)).get === List(Set(uri1, uri4, uri3, uri2))
      inputMolecule.apply(Set(uri1, uri2), Set(uri3)).get === List(Set(uri1, uri4, uri3, uri2))

      inputMolecule.apply(Set(uri1) or Set(uri1)).get === List(Set(uri1, uri2))
      inputMolecule.apply(Set(uri1) or Set(uri2)).get === List(Set(uri1, uri3, uri2))
      inputMolecule.apply(Set(uri1) or Set(uri2) or Set(uri3)).get === List(Set(uri1, uri4, uri3, uri2))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.uris_.apply(uri1).get === List(1)
      Ns.int.uris_.apply(uri2).get === List(1, 2)
      Ns.int.uris_.apply(uri1, uri2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.uris_.apply(uri1 or uri2).get === List(1, 2)
      Ns.int.uris_.apply(uri1 or uri2 or uri3).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.uris_.apply(List(uri1)).get === List(1)
      Ns.int.uris_.apply(List(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1, uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1), Set(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(List(uri1, uri2), List(uri3)).get === List(1, 2, 3)
      Ns.int.uris_.apply(List(uri1), List(uri2, uri3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.uris_.apply(List(uri1), Set(uri2, uri3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.uris_.apply(Set(uri1)).get === List(1)
      Ns.int.uris_.apply(Set(uri2)).get === List(1, 2)
      Ns.int.uris_.apply(Set(uri1, uri2)).get === List(1)
      Ns.int.uris_.apply(Set(uri1, uri3)).get === Nil
      Ns.int.uris_.apply(Set(uri2, uri3)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.uris_.apply(Set(uri1, uri2), Set(uri3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/uris_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uris_(uri1 and uri2).get === List(1)
      Ns.int.uris_(uri1 and uri3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.uris_(?))

      // AND semantics when applying uri1 Set of values matching attribute values of uri1 entity

      inputMolecule.apply(Set(uri1)).get === List(1)
      inputMolecule.apply(Set(uri2)).get === List(1, 2)

      inputMolecule.apply(Set(uri1, uri2)).get === List(1)
      inputMolecule.apply(Set(uri1, uri3)).get === Nil
      inputMolecule.apply(Set(uri2, uri3)).get === List(2)
      inputMolecule.apply(Set(uri3, uri1, uri2)).get === Nil

      inputMolecule.apply(List(Set(uri1))).get === List(1)
      inputMolecule.apply(List(Set(uri2))).get === List(1, 2)
      inputMolecule.apply(List(Set(uri1, uri2))).get === List(1)
      inputMolecule.apply(List(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(List(Set(uri2, uri3))).get === List(2)
      inputMolecule.apply(List(Set(uri3, uri1, uri2))).get === Nil

      inputMolecule.apply(Set(Set(uri1))).get === List(1)
      inputMolecule.apply(Set(Set(uri2))).get === List(1, 2)
      inputMolecule.apply(Set(Set(uri1, uri2))).get === List(1)
      inputMolecule.apply(Set(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(Set(Set(uri2, uri3))).get === List(2)
      inputMolecule.apply(Set(Set(uri3, uri1, uri2))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uri1), Set(uri1)).get === List(1)
      inputMolecule.apply(Set(uri1), Set(uri2)).get === List(1, 2)
      inputMolecule.apply(Set(uri1), Set(uri3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(uri2), Set(uri3)).get === List(1, 2, 3)
      inputMolecule.apply(Set(uri1, uri2), Set(uri3)).get === List(1, 2, 3)

      inputMolecule.apply(Set(uri1) or Set(uri1)).get === List(1)
      inputMolecule.apply(Set(uri1) or Set(uri2)).get === List(1, 2)
      inputMolecule.apply(Set(uri1) or Set(uri2) or Set(uri3)).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.uris.apply(uri1).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(uri1, uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))

      // Explicit `or` semantics
      Ns.int.uris.apply(uri1 or uri2).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(uri1 or uri2 or uri3).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      // Iterable: List - OR semantics
      Ns.int.uris.apply(List(uri1)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(List(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1, uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1), Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(List(uri1, uri2), List(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(List(uri1), List(uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      // mixing Iterable types and value/variable ok
      Ns.int.uris.apply(List(uri1), Set(uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      Ns.int.uris.apply(l1, Set(uri2, uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.uris.apply(Set(uri1)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      Ns.int.uris.apply(Set(uri1, uri2)).get === List((1, Set(uri1, uri2)))
      Ns.int.uris.apply(Set(uri1, uri3)).get === Nil
      Ns.int.uris.apply(Set(uri2, uri3)).get === List((2, Set(uri2, uri3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.uris.apply(Set(uri1, uri2), Set(uri3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/uris"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.uris(uri1 and uri2).get === List((1, Set(uri1, uri2)))
      Ns.int.uris(uri1 and uri3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.uris(?))

      // AND semantics when applying uri1 Set of values matching attribute values of uri1 entity

      inputMolecule.apply(Set(uri1)).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))

      inputMolecule.apply(Set(uri1, uri2)).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(uri1, uri3)).get === Nil
      inputMolecule.apply(Set(uri2, uri3)).get === List((2, Set(uri2, uri3)))
      inputMolecule.apply(Set(uri3, uri1, uri2)).get === Nil

      inputMolecule.apply(List(Set(uri1))).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(List(Set(uri2))).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      inputMolecule.apply(List(Set(uri1, uri2))).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(List(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(List(Set(uri2, uri3))).get === List((2, Set(uri2, uri3)))
      inputMolecule.apply(List(Set(uri3, uri1, uri2))).get === Nil

      inputMolecule.apply(Set(Set(uri1))).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(Set(uri2))).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      inputMolecule.apply(Set(Set(uri1, uri2))).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(Set(uri1, uri3))).get === Nil
      inputMolecule.apply(Set(Set(uri2, uri3))).get === List((2, Set(uri2, uri3)))
      inputMolecule.apply(Set(Set(uri3, uri1, uri2))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(uri1), Set(uri1)).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(uri1), Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      inputMolecule.apply(Set(uri1), Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      inputMolecule.apply(Set(uri2), Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
      inputMolecule.apply(Set(uri1, uri2), Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))

      inputMolecule.apply(Set(uri1) or Set(uri1)).get === List((1, Set(uri1, uri2)))
      inputMolecule.apply(Set(uri1) or Set(uri2)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)))
      inputMolecule.apply(Set(uri1) or Set(uri2) or Set(uri3)).get === List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
    }
  }
}