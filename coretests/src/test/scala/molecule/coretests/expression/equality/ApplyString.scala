package molecule.coretests.expression.equality

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Model2QueryException

class ApplyString extends ApplyBase {

  val l1 = List("a")
  val l2 = List("b")
  val l3 = List("c")

  val s1 = Set("a")
  val s2 = Set("b")
  val s3 = Set("c")

  val l11 = List("a", "a")
  val l12 = List("a", "b")
  val l13 = List("a", "c")
  val l23 = List("b", "c")

  val s12 = Set("a", "b")
  val s13 = Set("a", "c")
  val s23 = Set("b", "c")

  "Card one" >> {

    "Special characters" in new CoreSetup {
      Ns.str insert List("", " ", ",", ".", "?", "A", "B", "a", "b")

      // Empty string can be saved
      Ns.str("").get === List("")

      // White space only can be saved
      Ns.str(" ").get === List(" ")

      // Characters and symbols
      Ns.str(",").get === List(",")
      Ns.str(".").get === List(".")
      Ns.str("?").get === List("?")
      Ns.str("A").get === List("A")
      Ns.str("B").get === List("B")
      Ns.str("a").get === List("a")
      Ns.str("b").get === List("b")
    }


    "Mandatory" in new OneStringSetup {

      // Vararg (OR semantics)
      Ns.str.apply("a").get === List("a")
      Ns.str.apply("b").get === List("b")
      Ns.str.apply("a", "b").get === List("a", "b")

      // Explicit `or` semantics
      Ns.str.apply("a" or "b").get === List("a", "b")
      Ns.str.apply("a" or "b" or "c").get === List("a", "b", "c")

      // Iterable: List - OR semantics
      Ns.str.apply(List("a")).get === List("a")
      Ns.str.apply(List("b")).get === List("b")
      Ns.str.apply(List("a", "b")).get === List("a", "b")
      Ns.str.apply(List("a"), List("b")).get === List("a", "b")
      Ns.str.apply(List("a", "b"), List("c")).get === List("a", "b", "c")
      Ns.str.apply(List("a"), List("b", "c")).get === List("a", "b", "c")

      // mixing Iterable types and value/variable ok
      Ns.str.apply(List(str1), Set("b", str3)).get === List("a", "b", "c")

      // Iterable: Set (OR semantics)
      Ns.str.apply(Set("a")).get === List("a")
      Ns.str.apply(Set("b")).get === List("b")
      Ns.str.apply(Set("a", "b")).get === List("a", "b")
      Ns.str.apply(Set("a"), Set("b")).get === List("a", "b")
      Ns.str.apply(Set("a", "b"), Set("c")).get === List("a", "b", "c")
      Ns.str.apply(Set("a"), Set("b", "c")).get === List("a", "b", "c")


      // Input

      val inputMolecule = m(Ns.str(?))

      inputMolecule.apply("a").get === List("a")
      inputMolecule.apply("b").get === List("b")

      inputMolecule.apply("a", "a").get === List("a")
      inputMolecule.apply("a", "b").get === List("a", "b")

      inputMolecule.apply(List("a")).get === List("a")
      inputMolecule.apply(List("a", "a")).get === List("a")
      inputMolecule.apply(List("a", "b")).get === List("a", "b")

      inputMolecule.apply(Set("a")).get === List("a")
      inputMolecule.apply(Set("a", "b")).get === List("a", "b")

      inputMolecule.apply("a" or "a").get === List("a")
      inputMolecule.apply("a" or "b").get === List("a", "b")
      inputMolecule.apply("a" or "b" or "c").get === List("a", "b", "c")
    }


    "Tacit" in new OneStringSetup {

      // Vararg (OR semantics)
      Ns.int.str_.apply("a").get === List(1)
      Ns.int.str_.apply("b").get === List(2)
      Ns.int.str_.apply("a", "b").get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.str_.apply("a" or "b").get === List(1, 2)
      Ns.int.str_.apply("a" or "b" or "c").get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.str_.apply(List("a")).get === List(1)
      Ns.int.str_.apply(List("b")).get === List(2)
      Ns.int.str_.apply(List("a", "b")).get === List(1, 2)
      Ns.int.str_.apply(List("a"), List("b")).get === List(1, 2)
      Ns.int.str_.apply(List("a", "b"), List("c")).get === List(1, 2, 3)
      Ns.int.str_.apply(List("a"), List("b", "c")).get === List(1, 2, 3)

      // Iterable: Set (OR semantics)
      Ns.int.str_.apply(Set("a")).get === List(1)
      Ns.int.str_.apply(Set("b")).get === List(2)
      Ns.int.str_.apply(Set("a", "b")).get === List(1, 2)
      Ns.int.str_.apply(Set("a"), Set("b")).get === List(1, 2)
      Ns.int.str_.apply(Set("a", "b"), Set("c")).get === List(1, 2, 3)

      Ns.int.str_.apply(Set("a"), Set("b", "c")).get === List(1, 2, 3)

      // Input

      val inputMolecule = m(Ns.int.str_(?))

      inputMolecule.apply("a").get === List(1)
      inputMolecule.apply("b").get === List(2)

      inputMolecule.apply("a", "a").get === List(1)
      inputMolecule.apply("a", "b").get === List(1, 2)

      inputMolecule.apply(List("a")).get === List(1)
      inputMolecule.apply(List("a", "a")).get === List(1)
      inputMolecule.apply(List("a", "b")).get === List(1, 2)

      inputMolecule.apply(Set("a")).get === List(1)
      inputMolecule.apply(Set("a", "b")).get === List(1, 2)

      inputMolecule.apply("a" or "a").get === List(1)
      inputMolecule.apply("a" or "b").get === List(1, 2)
      inputMolecule.apply("a" or "b" or "c").get === List(1, 2, 3)
    }
  }


  "Card many" >> {

    "Mandatory (single attr coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.strs.apply("a").get === List(Set("a", "b"))
      Ns.strs.apply("b").get === List(Set("a", "c", "b"))
      Ns.strs.apply("a", "b").get === List(Set("a", "c", "b"))

      // Explicit `or` semantics
      Ns.strs.apply("a" or "b").get === List(Set("a", "c", "b"))
      Ns.strs.apply("a" or "b" or "c").get === List(Set("a", "d", "c", "b"))

      // Iterable: List - OR semantics
      Ns.strs.apply(List("a")).get === List(Set("a", "b"))
      Ns.strs.apply(List("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a", "b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a"), List("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(List("a", "b"), List("c")).get === List(Set("a", "d", "c", "b"))
      Ns.strs.apply(List("a"), List("b", "c")).get === List(Set("a", "d", "c", "b"))

      // mixing Iterable types and value/variable ok
      Ns.strs.apply(List(str1), Set("b", str3)).get === List(Set("a", "d", "c", "b"))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.strs.apply(Set("a")).get === List(Set("a", "b"))
      Ns.strs.apply(Set("b")).get === List(Set("a", "c", "b"))
      Ns.strs.apply(Set("a", "b")).get === List(Set("a", "b"))
      Ns.strs.apply(Set("a", "c")).get === Nil
      Ns.strs.apply(Set("b", "c")).get === List(Set("b", "c"))

      // Can't match multiple Sets (if needed, do multiple queries)
      (m(Ns.strs.apply(Set("a", "b"), Set("c"))).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/strs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.strs(str1 and str2).get === List(Set(str1, str2))
      Ns.strs(str1 and str3).get === Nil


      // Input

      val inputMolecule = m(Ns.strs(?))

      // AND semantics when applying "a" Set of values matching attribute values of "a" entity

      inputMolecule.apply(Set("a")).get === List(Set("a", "b"))
      inputMolecule.apply(Set("b")).get === List(Set("a", "c", "b"))

      inputMolecule.apply(Set("a", "b")).get === List(Set("a", "b"))
      inputMolecule.apply(Set("a", "c")).get === Nil
      inputMolecule.apply(Set("b", "c")).get === List(Set("b", "c"))
      inputMolecule.apply(Set("a", "b", "c")).get === Nil

      inputMolecule.apply(List(Set("a"))).get === List(Set("a", "b"))
      inputMolecule.apply(List(Set("b"))).get === List(Set("a", "c", "b"))
      inputMolecule.apply(List(Set("a", "b"))).get === List(Set("a", "b"))
      inputMolecule.apply(List(Set("a", "c"))).get === Nil
      inputMolecule.apply(List(Set("b", "c"))).get === List(Set("b", "c"))
      inputMolecule.apply(List(Set("a", "b", "c"))).get === Nil

      inputMolecule.apply(Set(Set("a"))).get === List(Set("a", "b"))
      inputMolecule.apply(Set(Set("b"))).get === List(Set("a", "c", "b"))
      inputMolecule.apply(Set(Set("a", "b"))).get === List(Set("a", "b"))
      inputMolecule.apply(Set(Set("a", "c"))).get === Nil
      inputMolecule.apply(Set(Set("b", "c"))).get === List(Set("b", "c"))
      inputMolecule.apply(Set(Set("a", "b", "c"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("a"), Set("a")).get === List(Set("a", "b"))
      inputMolecule.apply(Set("a"), Set("b")).get === List(Set("a", "c", "b"))
      inputMolecule.apply(Set("a"), Set("c")).get === List(Set("a", "d", "c", "b"))
      inputMolecule.apply(Set("b"), Set("c")).get === List(Set("a", "d", "c", "b"))
      inputMolecule.apply(Set("a", "b"), Set("c")).get === List(Set("a", "d", "c", "b"))

      inputMolecule.apply(Set("a") or Set("a")).get === List(Set("a", "b"))
      inputMolecule.apply(Set("a") or Set("b")).get === List(Set("a", "c", "b"))
      inputMolecule.apply(Set("a") or Set("b") or Set("c")).get === List(Set("a", "d", "c", "b"))
    }


    "Tacit" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.strs_.apply("a").get === List(1)
      Ns.int.strs_.apply("b").get === List(1, 2)
      Ns.int.strs_.apply("a", "b").get === List(1, 2)

      // Explicit `or` semantics
      Ns.int.strs_.apply("a" or "b").get === List(1, 2)
      Ns.int.strs_.apply("a" or "b" or "c").get === List(1, 2, 3)

      // Iterable: List - OR semantics
      Ns.int.strs_.apply(List("a")).get === List(1)
      Ns.int.strs_.apply(List("b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a", "b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a"), List("b")).get === List(1, 2)
      Ns.int.strs_.apply(List("a", "b"), List("c")).get === List(1, 2, 3)
      Ns.int.strs_.apply(List("a"), List("b", "c")).get === List(1, 2, 3)

      // mixing Iterable types and value/variable ok
      Ns.int.strs_.apply(List(str1), Set("b", str3)).get === List(1, 2, 3)

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.strs_.apply(Set("a")).get === List(1)
      Ns.int.strs_.apply(Set("b")).get === List(1, 2)
      Ns.int.strs_.apply(Set("a", "b")).get === List(1)
      Ns.int.strs_.apply(Set("a", "c")).get === Nil
      Ns.int.strs_.apply(Set("b", "c")).get === List(2)

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.strs_.apply(Set("a", "b"), Set("c")).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom_ (tacit)] Can only apply a single Set of values for attribute :ns/strs_"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.strs_(str1 and str2).get === List(1)
      Ns.int.strs_(str1 and str3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.strs_(?))

      // AND semantics when applying "a" Set of values matching attribute values of "a" entity

      inputMolecule.apply(Set("a")).get === List(1)
      inputMolecule.apply(Set("b")).get === List(1, 2)

      inputMolecule.apply(Set("a", "b")).get === List(1)
      inputMolecule.apply(Set("a", "c")).get === Nil
      inputMolecule.apply(Set("b", "c")).get === List(2)
      inputMolecule.apply(Set("a", "b", "c")).get === Nil

      inputMolecule.apply(List(Set("a"))).get === List(1)
      inputMolecule.apply(List(Set("b"))).get === List(1, 2)
      inputMolecule.apply(List(Set("a", "b"))).get === List(1)
      inputMolecule.apply(List(Set("a", "c"))).get === Nil
      inputMolecule.apply(List(Set("b", "c"))).get === List(2)
      inputMolecule.apply(List(Set("a", "b", "c"))).get === Nil

      inputMolecule.apply(Set(Set("a"))).get === List(1)
      inputMolecule.apply(Set(Set("b"))).get === List(1, 2)
      inputMolecule.apply(Set(Set("a", "b"))).get === List(1)
      inputMolecule.apply(Set(Set("a", "c"))).get === Nil
      inputMolecule.apply(Set(Set("b", "c"))).get === List(2)
      inputMolecule.apply(Set(Set("a", "b", "c"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("a"), Set("a")).get === List(1)
      inputMolecule.apply(Set("a"), Set("b")).get === List(1, 2)
      inputMolecule.apply(Set("a"), Set("c")).get === List(1, 2, 3)
      inputMolecule.apply(Set("b"), Set("c")).get === List(1, 2, 3)
      inputMolecule.apply(Set("a", "b"), Set("c")).get === List(1, 2, 3)

      inputMolecule.apply(Set("a") or Set("a")).get === List(1)
      inputMolecule.apply(Set("a") or Set("b")).get === List(1, 2)
      inputMolecule.apply(Set("a") or Set("b") or Set("c")).get === List(1, 2, 3)
    }


    "Mandatory unifying by other attribute (avoiding coalesce)" in new ManySetup {

      // Vararg (OR semantics)
      Ns.int.strs.apply("a").get === List((1, Set("a", "b")))
      Ns.int.strs.apply("b").get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply("a", "b").get === List((1, Set("a", "b")), (2, Set("b", "c")))

      // Explicit `or` semantics
      Ns.int.strs.apply("a" or "b").get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply("a" or "b" or "c").get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      // Iterable: List - OR semantics
      Ns.int.strs.apply(List("a")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(List("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a", "b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a"), List("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(List("a", "b"), List("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      Ns.int.strs.apply(List("a"), List("b", "c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      // mixing Iterable types and value/variable ok
      Ns.int.strs.apply(List(str1), Set("b", str3)).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      Ns.int.strs.apply(l1, Set("b", str3)).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      // Iterable: Set - AND semantics matching all values of card-many attribute per entity
      Ns.int.strs.apply(Set("a")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      Ns.int.strs.apply(Set("a", "b")).get === List((1, Set("a", "b")))
      Ns.int.strs.apply(Set("a", "c")).get === Nil
      Ns.int.strs.apply(Set("b", "c")).get === List((2, Set("b", "c")))

      // Can't match multiple Sets (if needed, do multiple queries)
      (Ns.int.strs.apply(Set("a", "b"), Set("c")).get must throwA[Model2QueryException])
        .message === "Got the exception molecule.transform.exception.Model2QueryException: " +
        "[Atom (mandatory)] Can only apply a single Set of values for attribute :ns/strs"

      // Explicit `and` semantics (maximum 2 `and` implemented: `v1 and v2 and v3`)
      Ns.int.strs(str1 and str2).get === List((1, Set(str1, str2)))
      Ns.int.strs(str1 and str3).get === Nil


      // Input

      val inputMolecule = m(Ns.int.strs(?))

      // AND semantics when applying "a" Set of values matching attribute values of "a" entity

      inputMolecule.apply(Set("a")).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))

      inputMolecule.apply(Set("a", "b")).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set("a", "c")).get === Nil
      inputMolecule.apply(Set("b", "c")).get === List((2, Set("b", "c")))
      inputMolecule.apply(Set("a", "b", "c")).get === Nil

      inputMolecule.apply(List(Set("a"))).get === List((1, Set("a", "b")))
      inputMolecule.apply(List(Set("b"))).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      inputMolecule.apply(List(Set("a", "b"))).get === List((1, Set("a", "b")))
      inputMolecule.apply(List(Set("a", "c"))).get === Nil
      inputMolecule.apply(List(Set("b", "c"))).get === List((2, Set("b", "c")))
      inputMolecule.apply(List(Set("a", "b", "c"))).get === Nil

      inputMolecule.apply(Set(Set("a"))).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set(Set("b"))).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      inputMolecule.apply(Set(Set("a", "b"))).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set(Set("a", "c"))).get === Nil
      inputMolecule.apply(Set(Set("b", "c"))).get === List((2, Set("b", "c")))
      inputMolecule.apply(Set(Set("a", "b", "c"))).get === Nil


      // OR semantics when applying multiple Sets - all values are flattened

      inputMolecule.apply(Set("a"), Set("a")).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set("a"), Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      inputMolecule.apply(Set("a"), Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      inputMolecule.apply(Set("b"), Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
      inputMolecule.apply(Set("a", "b"), Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))

      inputMolecule.apply(Set("a") or Set("a")).get === List((1, Set("a", "b")))
      inputMolecule.apply(Set("a") or Set("b")).get === List((1, Set("a", "b")), (2, Set("b", "c")))
      inputMolecule.apply(Set("a") or Set("b") or Set("c")).get === List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
    }
  }


  "Variable resolution" in new OneSetup {

    Ns.str.apply(str1, str2).get === List("a", "b")

    Ns.str.apply(List(str1, str2), List(str3)).get === List("a", "b", "c")
    Ns.str.apply(l12, l3).get === List("a", "b", "c")

    Ns.str.apply(Set(str1, str2), Set(str3)).get === List("a", "b", "c")
    Ns.str.apply(s12, s3).get === List("a", "b", "c")
  }
}