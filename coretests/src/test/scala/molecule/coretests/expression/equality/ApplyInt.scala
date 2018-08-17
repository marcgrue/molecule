package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyInt extends ApplyBase {

  val l1 = List(1)
  val l2 = List(2)
  val l3 = List(3)

  val s1 = Set(1)
  val s2 = Set(2)
  val s3 = Set(3)

  val l11 = List(1, 1)
  val l12 = List(1, 2)
  val l13 = List(1, 3)
  val l23 = List(2, 3)

  val s12 = Set(1, 2)
  val s13 = Set(1, 3)
  val s23 = Set(2, 3)

  "Card one" >> {

    "Mandatory" in new OneSetup {

      // Vararg (OR semantics)
      Ns.int.apply(1).get === List(1)
      Ns.int.apply(2).get === List(2)
      Ns.int.apply(1, 2).get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.apply(1 or 2).get === List(1, 2)
      Ns.int.apply(1 or 2 or 3).get === List(1, 2, 3)

      // Iterable: List (OR semantics)
      Ns.int.apply(List(1)).get === List(1)
      Ns.int.apply(List(2)).get === List(2)
      Ns.int.apply(List(1, 2)).get === List(1, 2)
      Ns.int.apply(List(1), List(2)).get === List(1, 2)
      Ns.int.apply(List(1, 2), List(3)).get === List(1, 2, 3)
      Ns.int.apply(List(1), List(2, 3)).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.apply(List(int1), Set(2, int3)).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.apply(Set(1)).get === List(1)
      Ns.int.apply(Set(2)).get === List(2)
      Ns.int.apply(Set(1, 2)).get === List(1, 2)
      Ns.int.apply(Set(1), Set(2)).get === List(1, 2)
      Ns.int.apply(Set(1, 2), Set(3)).get === List(1, 2, 3)
      Ns.int.apply(Set(1), Set(2, 3)).get === List(1, 2, 3)


      // Input

      val inputMolecule = m(Ns.int(?))

      inputMolecule.apply(1).get === List(1)
      inputMolecule.apply(2).get === List(2)

      inputMolecule.apply(1, 1).get === List(1)
      inputMolecule.apply(1, 2).get === List(1, 2)

      inputMolecule.apply(List(1)).get === List(1)
      inputMolecule.apply(List(1, 1)).get === List(1)
      inputMolecule.apply(List(1, 2)).get === List(1, 2)

      inputMolecule.apply(Set(1)).get === List(1)
      inputMolecule.apply(Set(1, 2)).get === List(1, 2)

      inputMolecule.apply(1 or 1).get === List(1)
      inputMolecule.apply(1 or 2).get === List(1, 2)
      inputMolecule.apply(1 or 2 or 3).get === List(1, 2, 3)
    }


    "Tacit" in new OneSetup {

      // Vararg (OR semantics)
      Ns.str.int_.apply(1).get === List("a")
      Ns.str.int_.apply(2).get === List("b")
      Ns.str.int_.apply(1, 2).get === List("a", "b")

      // Explicit `or` semantics
      Ns.str.int_.apply(1 or 2).get === List("a", "b")
      Ns.str.int_.apply(1 or 2 or 3).get === List("a", "b", "c")

      // Iterable: List - OR semantics
      Ns.str.int_.apply(List(1)).get === List("a")
      Ns.str.int_.apply(List(2)).get === List("b")
      Ns.str.int_.apply(List(1, 2)).get === List("a", "b")
      Ns.str.int_.apply(List(1), List(2)).get === List("a", "b")
      Ns.str.int_.apply(List(1, 2), List(3)).get === List("a", "b", "c")
      Ns.str.int_.apply(List(1), List(2, 3)).get === List("a", "b", "c")

      // Iterable: Set (OR semantics)
      Ns.str.int_.apply(Set(1)).get === List("a")
      Ns.str.int_.apply(Set(2)).get === List("b")
      Ns.str.int_.apply(Set(1, 2)).get === List("a", "b")
      Ns.str.int_.apply(Set(1), Set(2)).get === List("a", "b")
      Ns.str.int_.apply(Set(1, 2), Set(3)).get === List("a", "b", "c")
      Ns.str.int_.apply(Set(1), Set(2, 3)).get === List("a", "b", "c")


      // Input

      val inputMolecule = m(Ns.str.int_(?))

      inputMolecule.apply(1).get === List("a")
      inputMolecule.apply(2).get === List("b")

      inputMolecule.apply(1, 2).get === List("a", "b")

      inputMolecule.apply(List(1)).get === List("a")
      inputMolecule.apply(List(1, 2)).get === List("a", "b")

      inputMolecule.apply(Set(1)).get === List("a")
      inputMolecule.apply(Set(1, 2)).get === List("a", "b")

      inputMolecule.apply(1 or 2).get === List("a", "b")
      inputMolecule.apply(1 or 2 or 3).get === List("a", "b", "c")

      // Redundant duplicate values are omitted
      inputMolecule.apply(1, 1).get === List("a")
      inputMolecule.apply(List(1, 1)).get === List("a")
      inputMolecule.apply(1 or 1).get === List("a")
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.ints.apply(1).get === List(Set(1, 2))
      Ns.ints.apply(2).get === List(Set(1, 3, 2))
      Ns.ints.apply(1, 2).get === List(Set(1, 3, 2))

      // Explicit `or` semantics
      Ns.ints.apply(1, 2).get === List(Set(1, 3, 2))
      Ns.ints.apply(1, 2, 3).get === List(Set(1, 4, 3, 2))

      // Iterable: List - OR semantics
      Ns.ints.apply(List(1)).get === List(Set(1, 2))
      Ns.ints.apply(List(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1, 2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1), List(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(List(1, 2), List(3)).get === List(Set(1, 4, 3, 2))
      Ns.ints.apply(List(1), List(2, 3)).get === List(Set(1, 4, 3, 2))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.ints.apply(Set(1)).get === List(Set(1, 2))
      Ns.ints.apply(Set(2)).get === List(Set(1, 3, 2))
      Ns.ints.apply(Set(1, 2)).get === List(Set(1, 2))
      Ns.ints.apply(Set(1, 3)).get === Nil
      Ns.ints.apply(Set(2, 3)).get === List(Set(2, 3))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.ints.apply(Set(1, 2), Set(3))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.ints(1 and 2).get === List(Set(1, 2))
      Ns.ints(1 and 3).get === Nil


      // Input

      val inputMolecule = m(Ns.ints(?))

      // AND semantics when applying 1 Set of values matching attribute values of 1 entity

      inputMolecule.apply(Set(1)).get === List(Set(1, 2))
      inputMolecule.apply(Set(2)).get === List(Set(1, 3, 2))

      inputMolecule.apply(Set(1, 2)).get === List(Set(1, 2))
      inputMolecule.apply(Set(1, 3)).get === Nil
      inputMolecule.apply(Set(2, 3)).get === List(Set(2, 3))
      inputMolecule.apply(Set(1, 2, 3)).get === Nil

      inputMolecule.apply(List(Set(1))).get === List(Set(1, 2))
      inputMolecule.apply(List(Set(2))).get === List(Set(1, 3, 2))
      inputMolecule.apply(List(Set(1, 2))).get === List(Set(1, 2))
      inputMolecule.apply(List(Set(1, 3))).get === Nil
      inputMolecule.apply(List(Set(2, 3))).get === List(Set(2, 3))
      inputMolecule.apply(List(Set(1, 2, 3))).get === Nil

      inputMolecule.apply(Set(Set(1))).get === List(Set(1, 2))
      inputMolecule.apply(Set(Set(2))).get === List(Set(1, 3, 2))
      inputMolecule.apply(Set(Set(1, 2))).get === List(Set(1, 2))
      inputMolecule.apply(Set(Set(1, 3))).get === Nil
      inputMolecule.apply(Set(Set(2, 3))).get === List(Set(2, 3))
      inputMolecule.apply(Set(Set(1, 2, 3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1), Set(1)).get === List(Set(1, 2))
      inputMolecule.apply(Set(1), Set(2)).get === List(Set(1, 3, 2))
      inputMolecule.apply(Set(1), Set(3)).get === List(Set(1, 4, 3, 2))
      inputMolecule.apply(Set(2), Set(3)).get === List(Set(1, 4, 3, 2))
      inputMolecule.apply(Set(1, 2), Set(3)).get === List(Set(1, 4, 3, 2))

      inputMolecule.apply(Set(1) or Set(1)).get === List(Set(1, 2))
      inputMolecule.apply(Set(1) or Set(2)).get === List(Set(1, 3, 2))
      inputMolecule.apply(Set(1) or Set(2) or Set(3)).get === List(Set(1, 4, 3, 2))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.ints_.apply(1).get === List(10)
      Ns.int.ints_.apply(2).get === List(20, 10)
      Ns.int.ints_.apply(1, 2).get === List(20, 10)

      // Explicit `or` semantics
      Ns.int.ints_.apply(1, 2).get === List(20, 10)
      Ns.int.ints_.apply(1, 2, 3).get === List(20, 10, 30)

      // Iterable: List - OR semantics
      Ns.int.ints_.apply(List(1)).get === List(10)
      Ns.int.ints_.apply(List(2)).get === List(20, 10)
      Ns.int.ints_.apply(List(1, 2)).get === List(20, 10)
      Ns.int.ints_.apply(List(1), List(2)).get === List(20, 10)
      Ns.int.ints_.apply(List(1, 2), List(3)).get === List(20, 10, 30)
      Ns.int.ints_.apply(List(1), List(2, 3)).get === List(20, 10, 30)

      // mixing Iterable types and value/variable ok
      Ns.int.ints_.apply(List(int1), Set(2, int3)).get === List(20, 10, 30)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.ints_.apply(Set(1)).get === List(10)
      Ns.int.ints_.apply(Set(2)).get === List(20, 10)
      Ns.int.ints_.apply(Set(1, 2)).get === List(10)
      Ns.int.ints_.apply(Set(1, 3)).get === Nil
      Ns.int.ints_.apply(Set(2, 3)).get === List(20)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.ints_.apply(Set(1, 2), Set(3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/ints_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.ints_(1 and 2).get === List(10)
      Ns.int.ints_(1 and 3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.ints_(?))

      // AND semantics when applying 1 Set of values matching attribute values of 1 entity

      inputMolecule.apply(Set(1)).get === List(10)
      inputMolecule.apply(Set(2)).get === List(20, 10)

      inputMolecule.apply(Set(1, 2)).get === List(10)
      inputMolecule.apply(Set(1, 3)).get === Nil
      inputMolecule.apply(Set(2, 3)).get === List(20)
      inputMolecule.apply(Set(1, 2, 3)).get === Nil

      inputMolecule.apply(List(Set(1))).get === List(10)
      inputMolecule.apply(List(Set(2))).get === List(20, 10)
      inputMolecule.apply(List(Set(1, 2))).get === List(10)
      inputMolecule.apply(List(Set(1, 3))).get === Nil
      inputMolecule.apply(List(Set(2, 3))).get === List(20)
      inputMolecule.apply(List(Set(1, 2, 3))).get === Nil

      inputMolecule.apply(Set(Set(1))).get === List(10)
      inputMolecule.apply(Set(Set(2))).get === List(20, 10)
      inputMolecule.apply(Set(Set(1, 2))).get === List(10)
      inputMolecule.apply(Set(Set(1, 3))).get === Nil
      inputMolecule.apply(Set(Set(2, 3))).get === List(20)
      inputMolecule.apply(Set(Set(1, 2, 3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1), Set(1)).get === List(10)
      inputMolecule.apply(Set(1), Set(2)).get === List(20, 10)
      inputMolecule.apply(Set(1), Set(3)).get === List(20, 10, 30)
      inputMolecule.apply(Set(2), Set(3)).get === List(20, 10, 30)
      inputMolecule.apply(Set(1, 2), Set(3)).get === List(20, 10, 30)

      inputMolecule.apply(Set(1) or Set(1)).get === List(10)
      inputMolecule.apply(Set(1) or Set(2)).get === List(20, 10)
      inputMolecule.apply(Set(1) or Set(2) or Set(3)).get === List(20, 10, 30)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.ints.apply(1).get === List((10, Set(1, 2)))
      Ns.int.ints.apply(2).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(1, 2).get === List((10, Set(1, 2)), (20, Set(2, 3)))

      // Explicit `or` semantics
      Ns.int.ints.apply(1, 2).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(1, 2, 3).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))

      // Iterable: List - OR semantics
      Ns.int.ints.apply(List(1)).get === List((10, Set(1, 2)))
      Ns.int.ints.apply(List(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(List(1, 2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(List(1), List(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(List(1, 2), List(3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))
      Ns.int.ints.apply(List(1), List(2, 3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))

      // mixing Iterable types and value/variable ok
      Ns.int.ints.apply(List(int1), Set(2, int3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))
      Ns.int.ints.apply(l1, Set(2, int3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))


      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.ints.apply(Set(1)).get === List((10, Set(1, 2)))
      Ns.int.ints.apply(Set(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      Ns.int.ints.apply(Set(1, 2)).get === List((10, Set(1, 2)))
      Ns.int.ints.apply(Set(1, 3)).get === Nil
      Ns.int.ints.apply(Set(2, 3)).get === List((20, Set(2, 3)))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.ints.apply(Set(1, 2), Set(3)).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/ints"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.ints(1 and 2).get === List((10, Set(1, 2)))
      Ns.int.ints(1 and 3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.ints(?))

      // AND semantics when applying 1 Set of values matching attribute values of 1 entity

      inputMolecule.apply(Set(1)).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))

      inputMolecule.apply(Set(1, 2)).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(1, 3)).get === Nil
      inputMolecule.apply(Set(2, 3)).get === List((20, Set(2, 3)))
      inputMolecule.apply(Set(1, 2, 3)).get === Nil

      inputMolecule.apply(List(Set(1))).get === List((10, Set(1, 2)))
      inputMolecule.apply(List(Set(2))).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      inputMolecule.apply(List(Set(1, 2))).get === List((10, Set(1, 2)))
      inputMolecule.apply(List(Set(1, 3))).get === Nil
      inputMolecule.apply(List(Set(2, 3))).get === List((20, Set(2, 3)))
      inputMolecule.apply(List(Set(1, 2, 3))).get === Nil

      inputMolecule.apply(Set(Set(1))).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(Set(2))).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      inputMolecule.apply(Set(Set(1, 2))).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(Set(1, 3))).get === Nil
      inputMolecule.apply(Set(Set(2, 3))).get === List((20, Set(2, 3)))
      inputMolecule.apply(Set(Set(1, 2, 3))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set(1), Set(1)).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(1), Set(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      inputMolecule.apply(Set(1), Set(3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))
      inputMolecule.apply(Set(2), Set(3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))
      inputMolecule.apply(Set(1, 2), Set(3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))

      inputMolecule.apply(Set(1) or Set(1)).get === List((10, Set(1, 2)))
      inputMolecule.apply(Set(1) or Set(2)).get === List((10, Set(1, 2)), (20, Set(2, 3)))
      inputMolecule.apply(Set(1) or Set(2) or Set(3)).get === List((10, Set(1, 2)), (20, Set(2, 3)), (30, Set(3, 4)))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.int.apply(int1, int2).get === List(1, 2)

    Ns.int.apply(List(int1, int2), List(int3)).get === List(1, 2, 3)
    Ns.int.apply(l12, l3).get === List(1, 2, 3)

    Ns.int.apply(Set(int1, int2), Set(int3)).get === List(1, 2, 3)
    Ns.int.apply(s12, s3).get === List(1, 2, 3)

    // mixing ok
    Ns.int.apply(List(int1), Set(2, int3)).get === List(1, 2, 3)
  }
}