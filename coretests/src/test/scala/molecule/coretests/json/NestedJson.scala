package molecule.coretests.json

import molecule.core.macros.exception.NestedJsonException
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out5._


class NestedJson extends CoreSpec {

  "Optional nested not implemented" in new CoreSetup {

    (m(Ns.int.str.Refs1 *? Ref1.int1).getJson
      must throwA[NestedJsonException]).message ===
      "Got the exception molecule.core.macros.exception.NestedJsonException: " +
      "Optional nested data as json not implemented"
  }


  "1 nested attr" in new CoreSetup {
    (Ns.int.str.Refs1 * Ref1.int1) insert List(
      (1, "a", List(10, 11)),
      (2, "b", List(20, 21))
    )

    m(Ns.int.str.Refs1 * Ref1.int1).getJson ===
      """[
        |{"Ns.int": 1, "Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.int1": 10},
        |   {"Ref1.int1": 11}]},
        |{"Ns.int": 2, "Ns.str": "b", "Ns.refs1": [
        |   {"Ref1.int1": 20},
        |   {"Ref1.int1": 21}]}
        |]""".stripMargin
  }


  "Nested enum after ref" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.enum1) insert List(("a", List("enum11")))
    m(Ns.str.Refs1 * Ref1.enum1).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.enum1": "enum11"}]}
        |]""".stripMargin
  }


  "Nested ref without attribute" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2) insert List(
      ("a", List((11, 12))),
      ("b", List((21, 22))))


    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 12}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 22}]}
        |]""".stripMargin

    // We can omit tacit attribute between Ref1 and Ref2
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 12}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 22}]}
        |]""".stripMargin
  }


  "Intermediate references without attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.Ref2.int2) insert List(
      ("a", List(10, 20)),
      ("b", List(30))
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 10},
        |   {"ref2.Ref2.int2": 20}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"ref2.Ref2.int2": 30}]}
        |]""".stripMargin
  }


  "Intermediate references with optional attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2) insert List(
      ("a", List((Some(1), 10), (None, 20))),
      ("b", List((Some(3), 30)))
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.int1": 1, "ref2.Ref2.int2": 10},
        |   {"Ref1.int1": null, "ref2.Ref2.int2": 20}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"Ref1.int1": 3, "ref2.Ref2.int2": 30}]}
        |]""".stripMargin
  }


  "Optional attribute" in new CoreSetup {

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(
      ("a", Some(2), List(20)),
      ("b", None, List(10, 11))
    )

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 2, "Ref1.refs2": [
        |   {"Ref2.int2": 20}]},
        |{"Ns.str": "b", "refs1.Ref1.int1": null, "Ref1.refs2": [
        |   {"Ref2.int2": 10},
        |   {"Ref2.int2": 11}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 20}]},
        |{"Ns.str": "b", "Ref1.refs2": [
        |   {"Ref2.int2": 10},
        |   {"Ref2.int2": 11}]}
        |]""".stripMargin
  }


  "One - one" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "ref1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Ref1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin
  }


  "One - one - many" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2) insert List(("a", 1, List(Set(2, 3))))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2).getJson ===
      """[
        |{"Ns.str": "a", "ref1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.ints2": [3, 2]}]}
        |]""".stripMargin
  }


  "Many - one" in new CoreSetup {
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin
  }


  "Flat ManyRef simple" in new CoreSetup {
    Ns.str.Refs1.int1.Refs2.int2.insert("a", 1, 2)

    Ns.str.Refs1.int1.Refs2.int2.getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "refs2.Ref2.int2": 2}
        |]""".stripMargin

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
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
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "refs1.Ref1.str1": "x", "Ref1.refs2": [
        |   {"Ref2.int2": 11, "Ref2.str2": "xx"},
        |   {"Ref2.int2": 12, "Ref2.str2": "xxx"}]},
        |{"Ns.str": "a", "refs1.Ref1.int1": 2, "refs1.Ref1.str1": "y", "Ref1.refs2": [
        |   {"Ref2.int2": 21, "Ref2.str2": "yy"},
        |   {"Ref2.int2": 22, "Ref2.str2": "yyy"}]}
        |]""".stripMargin
  }


  "None - one" in new CoreSetup {
    m(Ns.str.Refs1.Refs2 * Ref2.int2) insert List(("a", List(2)))

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 2}]}
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
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.int1": 1, "Ref1.refs2": [
        |      {"Ref2.int2": 11},
        |      {"Ref2.int2": 12}]},
        |   {"Ref1.int1": 2, "Ref1.refs2": [
        |      {"Ref2.int2": 21},
        |      {"Ref2.int2": 22}]}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"Ref1.int1": 3, "Ref1.refs2": [
        |      {"Ref2.int2": 31},
        |      {"Ref2.int2": 32}]},
        |   {"Ref1.int1": 4, "Ref1.refs2": [
        |      {"Ref2.int2": 41},
        |      {"Ref2.int2": 42}]}]}
        |]""".stripMargin


    // Semi-nested A
    Ns.str.Refs1.*(Ref1.int1.Refs2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.int1": 1, "refs2.Ref2.int2": 11},
        |   {"Ref1.int1": 1, "refs2.Ref2.int2": 12},
        |   {"Ref1.int1": 2, "refs2.Ref2.int2": 22},
        |   {"Ref1.int1": 2, "refs2.Ref2.int2": 21}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"Ref1.int1": 3, "refs2.Ref2.int2": 32},
        |   {"Ref1.int1": 3, "refs2.Ref2.int2": 31},
        |   {"Ref1.int1": 4, "refs2.Ref2.int2": 41},
        |   {"Ref1.int1": 4, "refs2.Ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested A without intermediary attr `int1`
    Ns.str.Refs1.*(Ref1.Refs2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ns.refs1": [
        |   {"refs2.Ref2.int2": 11},
        |   {"refs2.Ref2.int2": 12},
        |   {"refs2.Ref2.int2": 22},
        |   {"refs2.Ref2.int2": 21}]},
        |{"Ns.str": "b", "Ns.refs1": [
        |   {"refs2.Ref2.int2": 31},
        |   {"refs2.Ref2.int2": 32},
        |   {"refs2.Ref2.int2": 41},
        |   {"refs2.Ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested B
    Ns.str.Refs1.int1.Refs2.*(Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 11},
        |   {"Ref2.int2": 12}]},
        |{"Ns.str": "a", "refs1.Ref1.int1": 2, "Ref1.refs2": [
        |   {"Ref2.int2": 21},
        |   {"Ref2.int2": 22}]},
        |{"Ns.str": "b", "refs1.Ref1.int1": 3, "Ref1.refs2": [
        |   {"Ref2.int2": 31},
        |   {"Ref2.int2": 32}]},
        |{"Ns.str": "b", "refs1.Ref1.int1": 4, "Ref1.refs2": [
        |   {"Ref2.int2": 41},
        |   {"Ref2.int2": 42}]}
        |]""".stripMargin


    // Semi-nested B without intermediary attr `int1`
    Ns.str.Refs1.Refs2.*(Ref2.int2).getJson ===
      """[
        |{"Ns.str": "a", "Ref1.refs2": [
        |   {"Ref2.int2": 11},
        |   {"Ref2.int2": 12},
        |   {"Ref2.int2": 21},
        |   {"Ref2.int2": 22}]},
        |{"Ns.str": "b", "Ref1.refs2": [
        |   {"Ref2.int2": 31},
        |   {"Ref2.int2": 32},
        |   {"Ref2.int2": 41},
        |   {"Ref2.int2": 42}]}
        |]""".stripMargin


    // Tacit filter
    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs1.Ref1.int1": 1, "Ref1.refs2": [
        |   {"Ref2.int2": 11},
        |   {"Ref2.int2": 12}]},
        |{"refs1.Ref1.int1": 2, "Ref1.refs2": [
        |   {"Ref2.int2": 21},
        |   {"Ref2.int2": 22}]}
        |]""".stripMargin

    // Tacit filters
    m(Ns.str_("a").Refs1.int1_(2).Refs2 * Ref2.int2).getJson ===
      """[
        |{"Ref1.refs2": [
        |   {"Ref2.int2": 21},
        |   {"Ref2.int2": 22}]}
        |]""".stripMargin


    // Flat
    m(Ns.str.Refs1.int1.Refs2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs1.Ref1.int1": 2, "refs2.Ref2.int2": 21},
        |{"Ns.str": "a", "refs1.Ref1.int1": 2, "refs2.Ref2.int2": 22},
        |{"Ns.str": "b", "refs1.Ref1.int1": 4, "refs2.Ref2.int2": 42},
        |{"Ns.str": "b", "refs1.Ref1.int1": 4, "refs2.Ref2.int2": 41},
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "refs2.Ref2.int2": 12},
        |{"Ns.str": "a", "refs1.Ref1.int1": 1, "refs2.Ref2.int2": 11},
        |{"Ns.str": "b", "refs1.Ref1.int1": 3, "refs2.Ref2.int2": 31},
        |{"Ns.str": "b", "refs1.Ref1.int1": 3, "refs2.Ref2.int2": 32}
        |]""".stripMargin


    // Flat without intermediary attr `int1`
    m(Ns.str.Refs1.Refs2.int2).getJson ===
      """[
        |{"Ns.str": "a", "refs2.Ref2.int2": 21},
        |{"Ns.str": "a", "refs2.Ref2.int2": 22},
        |{"Ns.str": "b", "refs2.Ref2.int2": 41},
        |{"Ns.str": "b", "refs2.Ref2.int2": 42},
        |{"Ns.str": "a", "refs2.Ref2.int2": 11},
        |{"Ns.str": "a", "refs2.Ref2.int2": 12},
        |{"Ns.str": "b", "refs2.Ref2.int2": 31},
        |{"Ns.str": "b", "refs2.Ref2.int2": 32}
        |]""".stripMargin
  }


  "Back ref" >> {

    "Nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1) insert List(("book", "John", List("Marc")))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "Ns.refs1": [
          |   {"Ref1.str1": "Marc"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "refs1.Ref1.str1": "Marc"}
          |]""".stripMargin
    }

    "Nested + adjacent" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2) insert List(("book", "John", List(("Marc", "Musician"))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "Ns.refs1": [
          |   {"Ref1.str1": "Marc", "refs2.Ref2.str2": "Musician"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "refs1.Ref1.str1": "Marc", "refs2.Ref2.str2": "Musician"}
          |]""".stripMargin
    }

    "Nested + nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)) insert List(("book", "John", List(("Marc", List("Musician")))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "Ns.refs1": [
          |   {"Ref1.str1": "Marc", "Ref1.refs2": [
          |      {"Ref2.str2": "Musician"}]}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"Ns.str": "book", "ref1.Ref1.str1": "John", "refs1.Ref1.str1": "Marc", "refs2.Ref2.str2": "Musician"}
          |]""".stripMargin
    }
  }


  "Applied eid" in new CoreSetup {
    val eid = Ns.str.Refs1.*(Ref1.int1).insert("a", List(1, 2)).eid
    Ns(eid).Refs1.*(Ref1.int1).getJson ===
      """[
        |{"Ns.refs1": [
        |   {"Ref1.int1": 1},
        |   {"Ref1.int1": 2}]}
        |]""".stripMargin
  }


  "Post attributes after nested" in new CoreSetup {
    Ns.float.str.Refs1.*(Ref1.int1).insert(1f, "a", Seq(11, 12))

    Ns.float.str.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"Ns.float": 1.0, "Ns.str": "a", "Ns.refs1": [
        |   {"Ref1.int1": 11},
        |   {"Ref1.int1": 12}]}
        |]""".stripMargin

    Ns.float.Refs1.*(Ref1.int1)._Ns.str.getJson ===
      """[
        |{"Ns.float": 1.0, "Ns.refs1": [
        |   {"Ref1.int1": 11},
        |   {"Ref1.int1": 12}], "Ns.str": "a"}
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
        |{"Ref1.str1": "r1a", "Ref1.refs2": [
        |   {"Ref2.str2": "r2a"},
        |   {"Ref2.str2": "r2b"}]},
        |{"Ref1.str1": "r1b", "Ref1.refs2": [
        |   {"Ref2.str2": "r2c"},
        |   {"Ref2.str2": "r2d"}]}
        |]""".stripMargin

    // With Ns
    // "Implicit" reference from Ns to Ref1 (without any attributes) implies that
    // some Ns entity is referencing some Ref1 entity.
    // This excludes "r1b" since no Ns entities reference it.
    Ns.Refs1.str1.Refs2.*(Ref2.str2).getJson ===
      """[
        |{"refs1.Ref1.str1": "r1a", "Ref1.refs2": [
        |   {"Ref2.str2": "r2a"},
        |   {"Ref2.str2": "r2b"}]}
        |]""".stripMargin
  }
}
