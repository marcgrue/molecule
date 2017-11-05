package molecule.coretests.ref

import molecule.Base._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

// (`Nested` is a model class..)
class Nested_ extends CoreSpec {


  "1 nested attr" in new CoreSetup {
    (Ns.int.str.Refs1 * Ref1.int1) insert List(
      (1, "a", List(10, 11)),
      (2, "b", List(20, 21))
    )

    (Ns.int.str.Refs1 * Ref1.int1).get === List(
      (1, "a", List(10, 11)),
      (2, "b", List(20, 21))
    )
  }

  "Nested enum after ref" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.enum1) insert List(("a", List("enum11")))
    m(Ns.str.Refs1 * Ref1.enum1).get === List(("a", List("enum11")))
  }


  "Nested ref without attribute" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2) insert List(
      ("a", List((11, 12))),
      ("b", List((21, 22))))


    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2).get === List(
      ("a", List(12)),
      ("b", List(22)))

    // We can omit tacet attribute between Ref1 and Ref2
    m(Ns.str.Refs1 * Ref1.Ref2.int2).get === List(
      ("a", List(12)),
      ("b", List(22)))
  }


  "Intermediate references without attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.Ref2.int2) insert List(
      ("a", List(10, 20)),
      ("b", List(30))
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2).get === List(
      ("a", List(10, 20)),
      ("b", List(30))
    )
  }


  "Intermediate references with optional attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2) insert List(
      ("a", List((Some(1), 10), (None, 20))),
      ("b", List((Some(3), 30)))
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2).get === List(
      ("a", List((Some(1), 10), (None, 20))),
      ("b", List((Some(3), 30)))
    )
  }


  "Optional attribute" in new CoreSetup {

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(("a", None, List(2)))

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).get === List(("a", None, List(2)))
    m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(("a", List(2)))
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


  "Flat ManyRef simple" in new CoreSetup {
    Ns.str.Refs1.int1.Refs2.int2.insert("a", 1, 2)

    Ns.str.Refs1.int1.Refs2.int2.get === List(
      ("a", 1, 2)
    )

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).get === List(
      ("a", 1, List(2))
    )

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).get === List(
      (1, List(2))
    )

    m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(
      ("a", List(2))
    )

    m(Ns.str_("a").Refs1.Refs2 * Ref2.int2).get === List(
      List(2)
    )
  }


  "Flat ManyRef + many" in new CoreSetup {
    m(Ns.str.Refs1.*(Ref1.int1.Refs2 * Ref2.int2)) insert List(
      ("a", List(
        (1, List(11)),
        (2, List(21, 22)))),
      ("b", List(
        (3, List(31, 32)),
        (4, List(41))))
    )

    m(Ns.str.Refs1.*(Ref1.int1.Refs2 * Ref2.int2)).get === List(
      ("a", List(
        (1, List(11)),
        (2, List(21, 22)))),
      ("b", List(
        (3, List(31, 32)),
        (4, List(41))))
    )

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).get === List(
      ("a", 1, List(11)),
      ("a", 2, List(21, 22)),
      ("b", 3, List(31, 32)),
      ("b", 4, List(41))
    )

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).get === List(
      (1, List(11)),
      (2, List(21, 22))
    )

    m(Ns.str_("a").Refs1.int1_(2).Refs2 * Ref2.int2).get === List(
      List(21, 22)
    )
  }


  "Flat ManyRef + many with extra attrs" in new CoreSetup {

    m(Ns.str.Refs1.*(Ref1.int1.str1.Refs2 * Ref2.int2.str2)) insert List(
      ("a", List(
        (1, "x", List((11, "xx"), (12, "xxx"))),
        (2, "y", List((21, "yy"), (22, "yyy")))))
    )

    m(Ns.str.Refs1.int1.str1.Refs2 * Ref2.int2.str2).get === List(
      ("a", 1, "x", List((11, "xx"), (12, "xxx"))),
      ("a", 2, "y", List((21, "yy"), (22, "yyy")))
    )
  }


  "None - one" in new CoreSetup {
    m(Ns.str.Refs1.Refs2 * Ref2.int2) insert List(("a", List(2)))

    m(Ns.str.Refs1.Refs2 * Ref2.int2).get === List(("a", List(2)))
  }


  "Refs after nested" in new CoreSetup {
    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)) insert List(
      ("a", List(
        (1, List(11)))),
      ("b", List(
        (2, List(21, 22)),
        (3, List(31))))
    )

    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)).get === List(
      ("a", List(
        (1, List(11)))),
      ("b", List(
        (2, List(21, 22)),
        (3, List(31))))
    )

    m(Ns.str.Refs1.int1.Refs2.int2).get.toSeq.sortBy(r => (r._1, r._2, r._3)) === List(
      ("a", 1, 11),
      ("b", 2, 21),
      ("b", 2, 22),
      ("b", 3, 31)
    )

    m(Ns.str.Refs1 * Ref1.int1.Refs2.int2).get === List(
      ("a", List((1, 11))),
      ("b", List((2, 21))),
      ("b", List((2, 22))),
      ("b", List((3, 31)))
    )

    // Still grouped by ref1 values
    m(Ns.str.Refs1 * Ref1.Refs2.int2).get === List(
      ("a", List(11)),
      ("b", List(21)),
      ("b", List(22)),
      ("b", List(31))
    )

    m(Ns.str.Refs1.Refs2.int2).get.toSeq.sortBy(r => (r._1, r._2)) === List(
      ("a", 11),
      ("b", 21),
      ("b", 22),
      ("b", 31)
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


  "Applied eid" in new CoreSetup {
    val eid = Ns.str.Refs1.*(Ref1.int1).insert("a", List(1, 2)).eid
    Ns(eid).str.get.head === "a"
    Ns(eid).Refs1.*(Ref1.int1).get.head === List(1, 2)
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