package molecule
package ref

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

// (`Nested` is a model class..)
class NestedTests extends CoreSpec {

  "Nested enum after ref" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.enum1) insert List(("d", List("enum11")))
    m(Ns.str.Refs1 * Ref1.enum1).get === List(("d", List("enum11")))
  }

  "Nested enum after attr" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.enum1) insert List(("e", List((12, "enum12"))))
    m(Ns.str.Refs1 * Ref1.int1.enum1).get === List(("e", List((12, "enum12"))))
  }

  "Nested ref without attribute" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2) insert List(
      ("a", List((11, 12))),
      ("b", List((21, 22))))


    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2).get === List(
      ("a", List(12)),
      ("b", List(22)))

    // We can omit non-fetching attribute between Ref1 and Ref2
    m(Ns.str.Refs1 * Ref1.Ref2.int2).get === List(
      ("a", List(12)),
      ("b", List(22)))

    // But in insert molecules we don't want to create referenced orphan entities
    (m(Ns.str.Refs1 * Ref1.Ref2.int2).insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[output.Molecule:noOrphanRefs (1)] Namespace `Ref1` in insert molecule has no mandatory attributes. Please add at least one."
  }

  "No mandatory attributes after nested" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1) insert List(
      ("a", List(11)),
      ("b", List(21)))

    m(Ns.str.Refs1 * Ref1.int1$).get === List(
      ("a", List(Some(11))),
      ("b", List(Some(21))))

    // But in insert molecules we don't want to create referenced orphan entities
    (m(Ns.str.Refs1 * Ref1.int1$).insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[output.Molecule:noOrphanRefs (3)] Namespace `Ref1` in insert molecule has no mandatory attributes. Please add at least one."
  }


  "Optional attribute" in new CoreSetup {
    // Todo
    //      m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(("a", None, List(2)))
    //      m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).get === List(("a", None, List(2)))
    //      m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(("a", List(2)))
    ok
  }

  "One - one" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))
    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2).get === List(("a", 1, List(2)))
    m(Ns.str.Ref1.Refs2 * Ref2.int2).get === List(("a", List(2)))
  }

  "One - one - many" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2) insert List(("a", 1, List(Set(2))))
    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2).get === List(("a", 1, List(Set(2))))
    m(Ns.str.Ref1.Refs2 * Ref2.ints2).get === List(("a", List(Set(2))))
  }

  "Many - one" in new CoreSetup {
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).get === List(("a", 1, List(2)))
    m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(("a", List(2)))
  }

  "Missing many attribute" in new CoreSetup {
    (m(Ns.str.Refs1.Refs2 * Ref2.int2).insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[output.Molecule:noOrphanRefs (2)] Namespace `Refs1` in insert molecule has no mandatory attributes. Please add at least one."
  }

  "Refs after nested" in new CoreSetup {
    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)) insert List(
      ("a", List(
        (1, List(11)))),
      ("b", List(
        (2, List(21, 22)),
        (3, List(31)))))

    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)).get === List(
      ("a", List(
        (1, List(11)))),
      ("b", List(
        (2, List(21, 22)),
        (3, List(31)))))


    m(Ns.str.Refs1.int1.Refs2.int2).get.sortBy(r => (r._1, r._2, r._3)) === List(
      ("a", 1, 11),
      ("b", 2, 21),
      ("b", 2, 22),
      ("b", 3, 31))

    m(Ns.str.Refs1 * Ref1.int1.Refs2.int2).get === List(
      ("a", List((1, 11))),
      ("b", List((2, 22), (2, 21), (3, 31))))


    m(Ns.str.Refs1.Refs2.int2).get.sortBy(r => (r._1, r._2)) === List(
      ("a", 11),
      ("b", 21),
      ("b", 22),
      ("b", 31))

    m(Ns.str.Refs1 * Ref1.Refs2.int2).get === List(
      ("a", List(11)),
      ("b", List(22, 21, 31)))
  }


  "Back ref" >> {

    "Nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1) insert List(("book", "John", List("Marc")))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).get === List(("book", "John", List("Marc")))
      m(Ns.str.Ref1.str1._Ns.Refs1.str1).get === List(("book", "John", "Marc"))
    }

    "Nested + adjacent" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2) insert List(("book", "John", List(("Marc", "Musician"))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2).get === List(("book", "John", List(("Marc", "Musician"))))
      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).get === List(("book", "John", "Marc", "Musician"))
    }

    "Nested + nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)) insert List(("book", "John", List(("Marc", List("Musician")))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)).get === List(("book", "John", List(("Marc", List("Musician")))))
      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).get === List(("book", "John", "Marc", "Musician"))
    }
  }


  "Attributes after nested group" in new CoreSetup {
    m(Ns.long.double.Refs1.*(Ref1.str1.int1)._Ns.bool) insert List(
      (100L, 200.0, List(("aaa", 300), ("bbb", 400)), true),
      (111L, 222.0, List(("xxx", 333), ("yyy", 444)), false)
    )
    m(Ns.long.double.Refs1.*(Ref1.str1.int1)._Ns.bool).get === List(
      (100L, 200.0, List(("aaa", 300), ("bbb", 400)), true),
      (111L, 222.0, List(("xxx", 333), ("yyy", 444)), false)
    )
  }
}