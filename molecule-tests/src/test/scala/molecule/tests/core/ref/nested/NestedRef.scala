package molecule.tests.core.ref.nested

import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.setup.TestSpec


class NestedRef extends TestSpec {

  "ref + opt attr" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2$.str2) insert List(
      ("a", List((Some(11), Some(12), "aa"))),
      ("b", List((Some(13), None, "bb"))),
      ("c", List((None, Some(14), "cc"))),
      ("d", List())
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2$.str2).get === List(
      ("a", List((Some(11), Some(12), "aa"))),
      ("b", List((Some(13), None, "bb"))),
      ("c", List((None, Some(14), "cc"))),
    )
    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2.str2).get === List(
      ("a", List((Some(11), 12, "aa"))),
      ("c", List((None, 14, "cc"))),
    )
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2$.str2).get === List(
      ("a", List((11, Some(12), "aa"))),
      ("b", List((13, None, "bb"))),
    )
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2.str2).get === List(
      ("a", List((11, 12, "aa"))),
    )


    m(Ns.str.Refs1 *? Ref1.int1$.Ref2.int2$.str2).get.sortBy(_._1) === List(
      ("a", List((Some(11), Some(12), "aa"))),
      ("b", List((Some(13), None, "bb"))),
      ("c", List((None, Some(14), "cc"))),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1$.Ref2.int2.str2).get.sortBy(_._1) === List(
      ("a", List((Some(11), 12, "aa"))),
      ("b", List()),
      ("c", List((None, 14, "cc"))),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1.Ref2.int2$.str2).get.sortBy(_._1) === List(
      ("a", List((11, Some(12), "aa"))),
      ("b", List((13, None, "bb"))),
      ("c", List()),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1.Ref2.int2.str2).get.sortBy(_._1) === List(
      ("a", List((11, 12, "aa"))),
      ("b", List()),
      ("c", List()),
      ("d", List())
    )
  }


  "ref + tacit attr" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2$.str2) insert List(
      ("a", List((Some(11), Some(12), "aa"))),
      ("b", List((Some(13), None, "bb"))),
      ("c", List((None, Some(14), "cc"))),
      ("d", List())
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2_.str2).get === List(
      ("a", List((Some(11), "aa"))),
      ("c", List((None, "cc"))),
    )
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2_.str2).get === List(
      ("a", List((11, "aa"))),
    )
    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2$.str2).get === List(
      ("a", List((Some(12), "aa"))),
      ("b", List((None, "bb"))),
    )
    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2.str2).get === List(
      ("a", List((12, "aa"))),
    )
    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2_.str2).get === List(
      ("a", List("aa")),
    )

    m(Ns.str.Refs1 *? Ref1.int1$.Ref2.int2_.str2).get.sortBy(_._1) === List(
      ("a", List((Some(11), "aa"))),
      ("b", List()),
      ("c", List((None, "cc"))),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1.Ref2.int2_.str2).get.sortBy(_._1) === List(
      ("a", List((11, "aa"))),
      ("b", List()),
      ("c", List()),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1_.Ref2.int2$.str2).get.sortBy(_._1) === List(
      ("a", List((Some(12), "aa"))),
      ("b", List((None, "bb"))),
      ("c", List()),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1_.Ref2.int2.str2).get.sortBy(_._1) === List(
      ("a", List((12, "aa"))),
      ("b", List()),
      ("c", List()),
      ("d", List())
    )
    m(Ns.str.Refs1 *? Ref1.int1_.Ref2.int2_.str2).get.sortBy(_._1) === List(
      ("a", List("aa")),
      ("b", List()),
      ("c", List()),
      ("d", List())
    )
  }


  "Intermediate references without attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.Ref2.int2.str2$) insert List(
      ("a", List((10, Some("a")), (20, None))),
      ("b", List())
    )

    m(Ns.str.Refs1 * Ref1.Ref2.int2.str2$).get === List(
      ("a", List((10, Some("a")), (20, None))),
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2.str2).get === List(
      ("a", List((10, "a"))),
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2.str2_).get === List(
      ("a", List(10)),
    )

    m(Ns.str.Refs1 *? Ref1.Ref2.int2.str2$).get.sortBy(_._1) === List(
      ("a", List((10, Some("a")), (20, None))),
      ("b", List())
    )
    m(Ns.str.Refs1 *? Ref1.Ref2.int2.str2).get.sortBy(_._1) === List(
      ("a", List((10, "a"))),
      ("b", List())
    )
    m(Ns.str.Refs1 *? Ref1.Ref2.int2.str2_).get.sortBy(_._1) === List(
      ("a", List(10)),
      ("b", List())
    )
  }


  "Flat card many refs" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2)) insert List(
      ("a", List(
        (1, List(11, 12)),
        (2, List()),
      )),
      ("b", List())
    )

    // Flat card many ref allowed in mandatory nested structure
    Ns.str.Refs1.*(Ref1.int1.Refs2.int2).get.map(p => (p._1, p._2.sorted)) === List(
      ("a", List(
        (1, 11),
        (1, 12),
      ))
    )

    // Flat card many refs not allowed in optional nested structure
    expectCompileError(
      "m(Ns.str.Refs1.*?(Ref1.int1.Refs2.int2))",
      "molecule.core.transform.exception.Dsl2ModelException: " +
        "Flat card many ref not allowed with optional nesting. " +
        """Found: Bond("Ref1", "refs2", "Ref2", 2, Seq())""")


    // Flat card many refs before nested structure
    Ns.str.Refs1.int1.Refs2.*(Ref2.int2).get === List(
      ("a", 1, List(11, 12))
    )
    Ns.str.Refs1.int1.Refs2.*?(Ref2.int2).get === List(
      ("a", 1, List(11, 12)),
      ("a", 2, List()),
    )

    // Implicit ref
    Ns.str.Refs1.Refs2.*?(Ref2.int2).get.sortBy(_._2.size) === List(
      ("a", List()),
      ("a", List(11, 12))
    )
    Ns.str.Refs1.Refs2.*(Ref2.int2).get === List(
      ("a", List(11, 12))
    )
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


  "Flat ref before nested" in new CoreSetup {

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(
      ("a", Some(1), List(1)),
      ("b", None, List(2)),
      ("c", Some(3), List()),
      ("d", None, List()),
    )

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).get === List(
      ("a", Some(1), List(1)),
      ("b", None, List(2)),
    )
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).get === List(
      ("a", 1, List(1)),
    )
    m(Ns.str.Refs1.int1_.Refs2 * Ref2.int2).get === List(
      ("a", List(1)),
    )
    m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(
      ("a", List(1)),
      ("b", List(2)),
    )

    m(Ns.str.Refs1.int1$.Refs2 *? Ref2.int2).get.sortBy(_._1) === List(
      ("a", Some(1), List(1)),
      ("b", None, List(2)),
      ("c", Some(3), List()),

      // Note that since Ref1.int1 is not asserted,
      // neither is then any ref to Ref2 possible
      // ("d", None, List()),
    )
    m(Ns.str.Refs1.int1.Refs2 *? Ref2.int2).get.sortBy(_._1) === List(
      ("a", 1, List(1)),
      ("c", 3, List()),
    )
    m(Ns.str.Refs1.int1_.Refs2 *? Ref2.int2).get.sortBy(_._1) === List(
      ("a", List(1)),
      ("c", List()),
    )
    m(Ns.str.Refs1.Refs2 *? Ref2.int2).get.sortBy(_._1) === List(
      ("a", List(1)),
      ("b", List(2)),
      ("c", List()),
    )
  }


  "Implicit initial namespace" in new CoreSetup {

    val List(ref1a, _, _, _, _, _) = Ref1.str1.Refs2.*(Ref2.str2) insert List(
      ("r1a", List("r2a", "r2b")),
      ("r1b", List("r2c", "r2d")) // <-- will not be referenced from Ns
    ) eids

    // Both Ns entities reference the same Ref1 entity
    Ns.str.refs1 insert List(
      ("a", Set(ref1a)),
      ("b", Set(ref1a))
    ) eids

    // Without Ns
    Ref1.str1.Refs2.*(Ref2.str2).get === List(
      ("r1a", List("r2a", "r2b")),
      ("r1b", List("r2c", "r2d"))
    )

    // With Ns
    // "Implicit" reference from Ns to Ref1 (without any attributes) implies that
    // some Ns entity is referencing some Ref1 entity.
    // This excludes "r1b" since no Ns entities reference it.
    Ns.Refs1.str1.Refs2.*(Ref2.str2).get === List(
      ("r1a", List("r2a", "r2b"))
    )
  }
}