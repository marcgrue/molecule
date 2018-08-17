package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyLong extends ApplyBase {

  val l1 = List(1L)
  val l2 = List(2L)
  val l3 = List(3L)

  val s1 = Set(1L)
  val s2 = Set(2L)
  val s3 = Set(3L)

  val l11 = List(1L, 1L)
  val l12 = List(1L, 2L)
  val l13 = List(1L, 3L)
  val l23 = List(2L, 3L)

  val s12 = Set(1L, 2L)
  val s13 = Set(1L, 3L)
  val s23 = Set(2L, 3L)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.long.apply(1L).get === List(1L)
      Ns.long.apply(2L).get === List(2L)
      Ns.long.apply(1L, 2L).get === List(1L, 2L)

      // Explicit `or` semantics
      Ns.long.apply(1L or 2L).get === List(1L, 2L)
      Ns.long.apply(1L or 2L or 3L).get === List(1L, 2L, 3L)

      // Iterable: List - OR semantics
      Ns.long.apply(List(1L)).get === List(1L)
      Ns.long.apply(List(2L)).get === List(2L)
      Ns.long.apply(List(1L, 2L)).get === List(1L, 2L)
      Ns.long.apply(List(1L), List(2L)).get === List(1L, 2L)
      Ns.long.apply(List(1L, 2L), List(3L)).get === List(1L, 2L, 3L)
      Ns.long.apply(List(1L), List(2L, 3L)).get === List(1L, 2L, 3L)

      // mixing Iterable types and value/variable ok
      Ns.long.apply(List(long1), Set(2L, long3)).get === List(1L, 2L, 3L)

      // Iterable: Set (OR semantics)
      Ns.long.apply(Set(1L)).get === List(1L)
      Ns.long.apply(Set(2L)).get === List(2L)
      Ns.long.apply(Set(1L, 2L)).get === List(1L, 2L)
      Ns.long.apply(Set(1L), Set(2L)).get === List(1L, 2L)
      Ns.long.apply(Set(1L, 2L), Set(3L)).get === List(1L, 2L, 3L)
      Ns.long.apply(Set(1L), Set(2L, 3L)).get === List(1L, 2L, 3L)


      // Input

      val inputMolecule = m(Ns.long(?))

      inputMolecule.apply(1L).get === List(1L)
      inputMolecule.apply(2L).get === List(2L)

      inputMolecule.apply(1L, 1L).get === List(1L)
      inputMolecule.apply(1L, 2L).get === List(1L, 2L)

      inputMolecule.apply(List(1L)).get === List(1L)
      inputMolecule.apply(List(1L, 1L)).get === List(1L)
      inputMolecule.apply(List(1L, 2L)).get === List(1L, 2L)

      inputMolecule.apply(Set(1L)).get === List(1L)
      inputMolecule.apply(Set(1L, 2L)).get === List(1L, 2L)

      inputMolecule.apply(1L or 1L).get === List(1L)
      inputMolecule.apply(1L or 2L).get === List(1L, 2L)
      inputMolecule.apply(1L or 2L or 3L).get === List(1L, 2L, 3L)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.long_.apply(1L).get === List(1)
      Ns.int.long_.apply(2L).get === List(2)
      Ns.int.long_.apply(1L, 2L).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.long_.apply(1L or 2L).get === List(1, 2)
      Ns.int.long_.apply(1L or 2L or 3L).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.long_.apply(List(1L)).get === List(1)
      Ns.int.long_.apply(List(2L)).get === List(2)
      Ns.int.long_.apply(List(1L, 2L)).get === List(1, 2)
      Ns.int.long_.apply(List(1L), List(2L)).get === List(1, 2)
      Ns.int.long_.apply(List(1L, 2L), List(3L)).get === List(1, 2, 3)
      Ns.int.long_.apply(List(1L), List(2L, 3L)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.long_.apply(Set(1L)).get === List(1)
      Ns.int.long_.apply(Set(2L)).get === List(2)
      Ns.int.long_.apply(Set(1L, 2L)).get === List(1, 2)
      Ns.int.long_.apply(Set(1L), Set(2L)).get === List(1, 2)
      Ns.int.long_.apply(Set(1L, 2L), Set(3L)).get === List(1, 2, 3)
      Ns.int.long_.apply(Set(1L), Set(2L, 3L)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int.long_(?))

      inputMolecule.apply(1L).get === List(1)
      inputMolecule.apply(2L).get === List(2)

      inputMolecule.apply(1L, 1L).get === List(1)
      inputMolecule.apply(1L, 2L).get === List(1, 2)

      inputMolecule.apply(List(1L)).get === List(1)
      inputMolecule.apply(List(1L, 1L)).get === List(1)
      inputMolecule.apply(List(1L, 2L)).get === List(1, 2)

      inputMolecule.apply(Set(1L)).get === List(1)
      inputMolecule.apply(Set(1L, 2L)).get === List(1, 2)

      inputMolecule.apply(1L or 1L).get === List(1)
      inputMolecule.apply(1L or 2L).get === List(1, 2)
      inputMolecule.apply(1L or 2L or 3L).get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.longs.apply(1L).get === List(Set(1L, 2L))
      Ns.longs.apply(2L).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(1L, 2L).get === List(Set(1L, 3L, 2L))

      // Explicit `or` semantics
      Ns.longs.apply(1L or 2L).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(1L or 2L or 3L).get === List(Set(1L, 4L, 3L, 2L))

      // Iterable: List - OR semantics
      Ns.longs.apply(List(1L)).get === List(Set(1L, 2L))
      Ns.longs.apply(List(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L, 2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L), List(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(List(1L, 2L), List(3L)).get === List(Set(1L, 4L, 3L, 2L))
      Ns.longs.apply(List(1L), List(2L, 3L)).get === List(Set(1L, 4L, 3L, 2L))

      // mixing Iterable types and value/variable ok
      Ns.longs.apply(List(long1), Set(2L, long3)).get === List(Set(1L, 4L, 3L, 2L))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.longs.apply(Set(1L)).get === List(Set(1L, 2L))
      Ns.longs.apply(Set(2L)).get === List(Set(1L, 3L, 2L))
      Ns.longs.apply(Set(1L, 2L)).get === List(Set(1L, 2L))
      Ns.longs.apply(Set(1L, 3L)).get === Nil
      Ns.longs.apply(Set(2L, 3L)).get === List(Set(2L, 3L))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.longs.apply(Set(1L, 2L), Set(3L))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/longs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.longs(1L and 2L).get === List(Set(1L, 2L))
      Ns.longs(1L and 3L).get === Nil


      // Input

      val inputMolecule = m(Ns.longs(?))

      // AND semantics when applying 1L Set of values matching attribute values of 1L entity

      inputMolecule.apply(Set(1L)).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(2L)).get === List(Set(1L, 3L, 2L))

      inputMolecule.apply(Set(1L, 2L)).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(1L, 3L)).get === Nil
      inputMolecule.apply(Set(2L, 3L)).get === List(Set(2L, 3L))
      inputMolecule.apply(Set(1L, 2L, 3L)).get === Nil

      inputMolecule.apply(List(Set(1L))).get === List(Set(1L, 2L))
      inputMolecule.apply(List(Set(2L))).get === List(Set(1L, 3L, 2L))
      inputMolecule.apply(List(Set(1L, 2L))).get === List(Set(1L, 2L))
      inputMolecule.apply(List(Set(1L, 3L))).get === Nil
      inputMolecule.apply(List(Set(2L, 3L))).get === List(Set(2L, 3L))
      inputMolecule.apply(List(Set(1L, 2L, 3L))).get === Nil

      inputMolecule.apply(Set(Set(1L))).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(Set(2L))).get === List(Set(1L, 3L, 2L))
      inputMolecule.apply(Set(Set(1L, 2L))).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(Set(1L, 3L))).get === Nil
      inputMolecule.apply(Set(Set(2L, 3L))).get === List(Set(2L, 3L))
      inputMolecule.apply(Set(Set(1L, 2L, 3L))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1L), Set(1L)).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(1L), Set(2L)).get === List(Set(1L, 3L, 2L))
      inputMolecule.apply(Set(1L), Set(3L)).get === List(Set(1L, 4L, 3L, 2L))
      inputMolecule.apply(Set(2L), Set(3L)).get === List(Set(1L, 4L, 3L, 2L))
      inputMolecule.apply(Set(1L, 2L), Set(3L)).get === List(Set(1L, 4L, 3L, 2L))

      inputMolecule.apply(Set(1L) or Set(1L)).get === List(Set(1L, 2L))
      inputMolecule.apply(Set(1L) or Set(2L)).get === List(Set(1L, 3L, 2L))
      inputMolecule.apply(Set(1L) or Set(2L) or Set(3L)).get === List(Set(1L, 4L, 3L, 2L))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.longs_.apply(1L).get === List(1)
      Ns.int.longs_.apply(2L).get === List(1, 2)
      Ns.int.longs_.apply(1L, 2L).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.longs_.apply(1L or 2L).get === List(1, 2)
      Ns.int.longs_.apply(1L or 2L or 3L).get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.longs_.apply(List(1L)).get === List(1)
      Ns.int.longs_.apply(List(2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L, 2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L), List(2L)).get === List(1, 2)
      Ns.int.longs_.apply(List(1L, 2L), List(3L)).get === List(2, 1, 3)
      Ns.int.longs_.apply(List(1L), List(2L, 3L)).get === List(2, 1, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.longs_.apply(List(long1), Set(2L, long3)).get === List(2, 1, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.longs_.apply(Set(1L)).get === List(1)
      Ns.int.longs_.apply(Set(2L)).get === List(1, 2)
      Ns.int.longs_.apply(Set(1L, 2L)).get === List(1)
      Ns.int.longs_.apply(Set(1L, 3L)).get === Nil
      Ns.int.longs_.apply(Set(2L, 3L)).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.longs_.apply(Set(1L, 2L), Set(3L)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/longs_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.longs_(1L and 2L).get === List(1)
      Ns.int.longs_(1L and 3L).get === Nil


      // Input

      val inputMolecule = m(Ns.int.longs_(?))

      // AND semantics when applying 1L Set of values matching attribute values of 1L entity

      inputMolecule.apply(Set(1L)).get === List(1)
      inputMolecule.apply(Set(2L)).get === List(1, 2)

      inputMolecule.apply(Set(1L, 2L)).get === List(1)
      inputMolecule.apply(Set(1L, 3L)).get === Nil
      inputMolecule.apply(Set(2L, 3L)).get === List(2)
      inputMolecule.apply(Set(1L, 2L, 3L)).get === Nil

      inputMolecule.apply(List(Set(1L))).get === List(1)
      inputMolecule.apply(List(Set(2L))).get === List(1, 2)
      inputMolecule.apply(List(Set(1L, 2L))).get === List(1)
      inputMolecule.apply(List(Set(1L, 3L))).get === Nil
      inputMolecule.apply(List(Set(2L, 3L))).get === List(2)
      inputMolecule.apply(List(Set(1L, 2L, 3L))).get === Nil

      inputMolecule.apply(Set(Set(1L))).get === List(1)
      inputMolecule.apply(Set(Set(2L))).get === List(1, 2)
      inputMolecule.apply(Set(Set(1L, 2L))).get === List(1)
      inputMolecule.apply(Set(Set(1L, 3L))).get === Nil
      inputMolecule.apply(Set(Set(2L, 3L))).get === List(2)
      inputMolecule.apply(Set(Set(1L, 2L, 3L))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1L), Set(1L)).get === List(1)
      inputMolecule.apply(Set(1L), Set(2L)).get === List(1, 2)
      inputMolecule.apply(Set(1L), Set(3L)).get === List(2, 1, 3)
      inputMolecule.apply(Set(2L), Set(3L)).get === List(2, 1, 3)
      inputMolecule.apply(Set(1L, 2L), Set(3L)).get === List(2, 1, 3)

      inputMolecule.apply(Set(1L) or Set(1L)).get === List(1)
      inputMolecule.apply(Set(1L) or Set(2L)).get === List(1, 2)
      inputMolecule.apply(Set(1L) or Set(2L) or Set(3L)).get === List(2, 1, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.longs.apply(1L).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(1L, 2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))

      // Explicit `or` semantics
      Ns.int.longs.apply(1L or 2L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(1L or 2L or 3L).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      // Iterable: List - OR semantics
      Ns.int.longs.apply(List(1L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(List(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L, 2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L), List(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(List(1L, 2L), List(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(List(1L), List(2L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      // mixing Iterable types and value/variable ok
      Ns.int.longs.apply(List(long1), Set(2L, long3)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      Ns.int.longs.apply(l1, Set(2L, long3)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.longs.apply(Set(1L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      Ns.int.longs.apply(Set(1L, 2L)).get === List((1, Set(1L, 2L)))
      Ns.int.longs.apply(Set(1L, 3L)).get === Nil
      Ns.int.longs.apply(Set(2L, 3L)).get === List((2, Set(2L, 3L)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.longs.apply(Set(1L, 2L), Set(3L)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/longs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.longs(1L and 2L).get === List((1, Set(1L, 2L)))
      Ns.int.longs(1L and 3L).get === Nil


      // Input

      val inputMolecule = m(Ns.int.longs(?))

      // AND semantics when applying 1L Set of values matching attribute values of 1L entity

      inputMolecule.apply(Set(1L)).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))

      inputMolecule.apply(Set(1L, 2L)).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(1L, 3L)).get === Nil
      inputMolecule.apply(Set(2L, 3L)).get === List((2, Set(2L, 3L)))
      inputMolecule.apply(Set(1L, 2L, 3L)).get === Nil

      inputMolecule.apply(List(Set(1L))).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(List(Set(2L))).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      inputMolecule.apply(List(Set(1L, 2L))).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(List(Set(1L, 3L))).get === Nil
      inputMolecule.apply(List(Set(2L, 3L))).get === List((2, Set(2L, 3L)))
      inputMolecule.apply(List(Set(1L, 2L, 3L))).get === Nil

      inputMolecule.apply(Set(Set(1L))).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(Set(2L))).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      inputMolecule.apply(Set(Set(1L, 2L))).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(Set(1L, 3L))).get === Nil
      inputMolecule.apply(Set(Set(2L, 3L))).get === List((2, Set(2L, 3L)))
      inputMolecule.apply(Set(Set(1L, 2L, 3L))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1L), Set(1L)).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(1L), Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      inputMolecule.apply(Set(1L), Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      inputMolecule.apply(Set(2L), Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
      inputMolecule.apply(Set(1L, 2L), Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

      inputMolecule.apply(Set(1L) or Set(1L)).get === List((1, Set(1L, 2L)))
      inputMolecule.apply(Set(1L) or Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
      inputMolecule.apply(Set(1L) or Set(2L) or Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.long.apply(long1, long2).get === List(1L, 2L)

    Ns.long.apply(List(long1, long2), List(long3)).get === List(1L, 2L, 3L)
    Ns.long.apply(l12, l3).get === List(1L, 2L, 3L)

    Ns.long.apply(Set(long1, long2), Set(long3)).get === List(1L, 2L, 3L)
    Ns.long.apply(s12, s3).get === List(1L, 2L, 3L)

    // mixing ok
    Ns.long.apply(List(long1), Set(2L, long3)).get === List(1L, 2L, 3L)
  }
}