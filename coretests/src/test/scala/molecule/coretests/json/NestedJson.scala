package molecule.coretests.json

import molecule.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec


class NestedJson extends CoreSpec {


  "1 nested attr" in new CoreSetup {
    (Ns.int.str.Refs1 * Ref1.int1) insert List(
      (1, "a", List(10, 11)),
      (2, "b", List(20, 21))
    )

    m(Ns.int.str.Refs1 * Ref1.int1).getJson ===
      """[
        |{"ns.int": 1, "ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 10},
        |   {"ref1.int1": 11}]},
        |{"ns.int": 2, "ns.str": "b", "ns.refs1": [
        |   {"ref1.int1": 20},
        |   {"ref1.int1": 21}]}
        |]""".stripMargin
  }

  "Nested enum after ref" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.enum1) insert List(("a", List("enum11")))
    m(Ns.str.Refs1 * Ref1.enum1).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref1.enum1": "enum11"}]}
        |]""".stripMargin
  }


  "Nested ref without attribute" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2) insert List(
      ("a", List((11, 12))),
      ("b", List((21, 22))))


    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref2.ref2.int2": 12}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref2.ref2.int2": 22}]}
        |]""".stripMargin

    // We can omit tacit attribute between Ref1 and Ref2
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref2.ref2.int2": 12}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref2.ref2.int2": 22}]}
        |]""".stripMargin
  }


  "Intermediate references without attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.Ref2.int2) insert List(
      ("a", List(10, 20)),
      ("b", List(30))
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref2.ref2.int2": 10},
        |   {"ref2.ref2.int2": 20}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref2.ref2.int2": 30}]}
        |]""".stripMargin
  }


  "Intermediate references with optional attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2) insert List(
      ("a", List((Some(1), 10), (None, 20))),
      ("b", List((Some(3), 30)))
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 1, "ref2.ref2.int2": 10},
        |   {"ref1.int1": null, "ref2.ref2.int2": 20}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref1.int1": 3, "ref2.ref2.int2": 30}]}
        |]""".stripMargin
  }


  "Optional attribute" in new CoreSetup {

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(
      ("a", Some(2), List(20)),
      ("b", None, List(10, 11))
    )

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 2, "ref1.refs2": [
        |   {"ref2.int2": 20}]},
        |{"ns.str": "b", "refs1.ref1.int1": null, "ref1.refs2": [
        |   {"ref2.int2": 10},
        |   {"ref2.int2": 11}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 20}]},
        |{"ns.str": "b", "ref1.refs2": [
        |   {"ref2.int2": 10},
        |   {"ref2.int2": 11}]}
        |]""".stripMargin
  }


  "One - one" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Ref1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin
  }


  "One - one - many" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2) insert List(("a", 1, List(Set(2, 3))))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2).getJson ===
      """[
        |{"ns.str": "a", "ref1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.ints2": [3, 2]}]}
        |]""".stripMargin
  }


  "Many - one" in new CoreSetup {
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin
  }


  "Flat ManyRef simple" in new CoreSetup {
    Ns.str.Refs1.int1.Refs2.int2.insert("a", 1, 2)

    Ns.str.Refs1.int1.Refs2.int2.getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 1, "refs2.ref2.int2": 2}
        |]""".stripMargin

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin
  }


  "Flat ManyRef + many with extra attrs" in new CoreSetup {

    m(Ns.str.Refs1.*(Ref1.int1.str1.Refs2 * Ref2.int2.str2)) insert List(
      ("a", List(
        (1, "x", List((11, "xx"), (12, "xxx"))),
        (2, "y", List((21, "yy"), (22, "yyy")))))
    )

    m(Ns.str.Refs1.int1.str1.Refs2 * Ref2.int2.str2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 1, "refs1.ref1.str1": "x", "ref1.refs2": [
        |   {"ref2.int2": 11, "ref2.str2": "xx"},
        |   {"ref2.int2": 12, "ref2.str2": "xxx"}]},
        |{"ns.str": "a", "refs1.ref1.int1": 2, "refs1.ref1.str1": "y", "ref1.refs2": [
        |   {"ref2.int2": 21, "ref2.str2": "yy"},
        |   {"ref2.int2": 22, "ref2.str2": "yyy"}]}
        |]""".stripMargin
  }


  "None - one" in new CoreSetup {
    m(Ns.str.Refs1.Refs2 * Ref2.int2) insert List(("a", List(2)))

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 2}]}
        |]""".stripMargin
  }


  "Flat ManyRef + many" in new CoreSetup {
    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2)) insert List(
      ("a", List(
        (1, List(11, 12)),
        (2, List(21, 22)))),
      ("b", List(
        (3, List(31, 32)),
        (4, List(41, 42))))
    )

    // Fully nested
    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2)).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 1, "ref1.refs2": [
        |      {"ref2.int2": 11},
        |      {"ref2.int2": 12}]},
        |   {"ref1.int1": 2, "ref1.refs2": [
        |      {"ref2.int2": 21},
        |      {"ref2.int2": 22}]}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref1.int1": 3, "ref1.refs2": [
        |      {"ref2.int2": 31},
        |      {"ref2.int2": 32}]},
        |   {"ref1.int1": 4, "ref1.refs2": [
        |      {"ref2.int2": 41},
        |      {"ref2.int2": 42}]}]}
        |]""".stripMargin


    // Semi-nested A
    Ns.str.Refs1.*(Ref1.int1.Refs2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 1, "refs2.ref2.int2": 11},
        |   {"ref1.int1": 1, "refs2.ref2.int2": 12},
        |   {"ref1.int1": 2, "refs2.ref2.int2": 21},
        |   {"ref1.int1": 2, "refs2.ref2.int2": 22}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"ref1.int1": 3, "refs2.ref2.int2": 31},
        |   {"ref1.int1": 3, "refs2.ref2.int2": 32},
        |   {"ref1.int1": 4, "refs2.ref2.int2": 41},
        |   {"ref1.int1": 4, "refs2.ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested A without intermediary attr `int1`
    Ns.str.Refs1.*(Ref1.Refs2.int2).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"refs2.ref2.int2": 11},
        |   {"refs2.ref2.int2": 12},
        |   {"refs2.ref2.int2": 22},
        |   {"refs2.ref2.int2": 21}]},
        |{"ns.str": "b", "ns.refs1": [
        |   {"refs2.ref2.int2": 32},
        |   {"refs2.ref2.int2": 31},
        |   {"refs2.ref2.int2": 41},
        |   {"refs2.ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested B
    Ns.str.Refs1.int1.Refs2.*(Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 11},
        |   {"ref2.int2": 12}]},
        |{"ns.str": "a", "refs1.ref1.int1": 2, "ref1.refs2": [
        |   {"ref2.int2": 21},
        |   {"ref2.int2": 22}]},
        |{"ns.str": "b", "refs1.ref1.int1": 3, "ref1.refs2": [
        |   {"ref2.int2": 31},
        |   {"ref2.int2": 32}]},
        |{"ns.str": "b", "refs1.ref1.int1": 4, "ref1.refs2": [
        |   {"ref2.int2": 41},
        |   {"ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested B without intermediary attr `int1`
    Ns.str.Refs1.Refs2.*(Ref2.int2).getJson ===
      """[
        |{"ns.str": "a", "ref1.refs2": [
        |   {"ref2.int2": 11},
        |   {"ref2.int2": 12},
        |   {"ref2.int2": 21},
        |   {"ref2.int2": 22}]},
        |{"ns.str": "b", "ref1.refs2": [
        |   {"ref2.int2": 31},
        |   {"ref2.int2": 32},
        |   {"ref2.int2": 41},
        |   {"ref2.int2": 42}]}
        |]""".stripMargin


    // Tacit filter
    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs1.ref1.int1": 1, "ref1.refs2": [
        |   {"ref2.int2": 11},
        |   {"ref2.int2": 12}]},
        |{"refs1.ref1.int1": 2, "ref1.refs2": [
        |   {"ref2.int2": 21},
        |   {"ref2.int2": 22}]}
        |]""".stripMargin

    // Tacit filters
    m(Ns.str_("a").Refs1.int1_(2).Refs2 * Ref2.int2).getJson ===
      """[
        |{"ref1.refs2": [
        |   {"ref2.int2": 21},
        |   {"ref2.int2": 22}]}
        |]""".stripMargin


    // Flat
    m(Ns.str.Refs1.int1.Refs2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs1.ref1.int1": 2, "refs2.ref2.int2": 21},
        |{"ns.str": "a", "refs1.ref1.int1": 2, "refs2.ref2.int2": 22},
        |{"ns.str": "b", "refs1.ref1.int1": 4, "refs2.ref2.int2": 42},
        |{"ns.str": "b", "refs1.ref1.int1": 4, "refs2.ref2.int2": 41},
        |{"ns.str": "a", "refs1.ref1.int1": 1, "refs2.ref2.int2": 12},
        |{"ns.str": "a", "refs1.ref1.int1": 1, "refs2.ref2.int2": 11},
        |{"ns.str": "b", "refs1.ref1.int1": 3, "refs2.ref2.int2": 31},
        |{"ns.str": "b", "refs1.ref1.int1": 3, "refs2.ref2.int2": 32}
        |]""".stripMargin


    // Flat without intermediary attr `int1`
    m(Ns.str.Refs1.Refs2.int2).getJson ===
      """[
        |{"ns.str": "a", "refs2.ref2.int2": 21},
        |{"ns.str": "a", "refs2.ref2.int2": 22},
        |{"ns.str": "b", "refs2.ref2.int2": 41},
        |{"ns.str": "b", "refs2.ref2.int2": 42},
        |{"ns.str": "a", "refs2.ref2.int2": 11},
        |{"ns.str": "a", "refs2.ref2.int2": 12},
        |{"ns.str": "b", "refs2.ref2.int2": 31},
        |{"ns.str": "b", "refs2.ref2.int2": 32}
        |]""".stripMargin
  }


  "Back ref" >> {

    "Nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1) insert List(("book", "John", List("Marc")))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "ns.refs1": [
          |   {"ref1.str1": "Marc"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "refs1.ref1.str1": "Marc"}
          |]""".stripMargin
    }

    "Nested + adjacent" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2) insert List(("book", "John", List(("Marc", "Musician"))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "ns.refs1": [
          |   {"ref1.str1": "Marc", "refs2.ref2.str2": "Musician"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "refs1.ref1.str1": "Marc", "refs2.ref2.str2": "Musician"}
          |]""".stripMargin
    }

    "Nested + nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)) insert List(("book", "John", List(("Marc", List("Musician")))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "ns.refs1": [
          |   {"ref1.str1": "Marc", "ref1.refs2": [
          |      {"ref2.str2": "Musician"}]}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"ns.str": "book", "ref1.ref1.str1": "John", "refs1.ref1.str1": "Marc", "refs2.ref2.str2": "Musician"}
          |]""".stripMargin
    }
  }


  "Applied eid" in new CoreSetup {
    val eid = Ns.str.Refs1.*(Ref1.int1).insert("a", List(1, 2)).eid
    Ns(eid).Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.refs1": [
        |   {"ref1.int1": 1},
        |   {"ref1.int1": 2}]}
        |]""".stripMargin
  }


  "Post attributes after nested" in new CoreSetup {
    Ns.float.str.Refs1.*(Ref1.int1).insert(1f, "a", Seq(11, 12))

    Ns.float.str.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.float": 1.0, "ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 11},
        |   {"ref1.int1": 12}]}
        |]""".stripMargin

    Ns.float.Refs1.*(Ref1.int1)._Ns.str.getJson ===
      """[
        |{"ns.float": 1.0, "ns.refs1": [
        |   {"ref1.int1": 11},
        |   {"ref1.int1": 12}], "ns.str": "a"}
        |]""".stripMargin
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
    Ref1.str1.Refs2.*(Ref2.str2).getJson ===
      """[
        |{"ref1.str1": "r1a", "ref1.refs2": [
        |   {"ref2.str2": "r2a"},
        |   {"ref2.str2": "r2b"}]},
        |{"ref1.str1": "r1b", "ref1.refs2": [
        |   {"ref2.str2": "r2c"},
        |   {"ref2.str2": "r2d"}]}
        |]""".stripMargin

    // With Ns
    // "Implicit" reference from Ns to Ref1 (without any attributes) implies that
    // some Ns entity is referencing some Ref1 entity.
    // This excludes "r1b" since no Ns entities reference it.
    Ns.Refs1.str1.Refs2.*(Ref2.str2).getJson ===
      """[
        |{"refs1.ref1.str1": "r1a", "ref1.refs2": [
        |   {"ref2.str2": "r2a"},
        |   {"ref2.str2": "r2b"}]}
        |]""".stripMargin
  }
}
