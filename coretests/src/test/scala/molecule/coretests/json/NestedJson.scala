package molecule.coretests.json

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}


class NestedJson extends CoreSpec {

  "1 nested attr" in new CoreSetup {
    (Ns.int.str.Refs1 * Ref1.int1) insert List(
      (1, "a", List(10, 11)),
      (2, "b", List(20, 21))
    )

    (Ns.int.str.Refs1 * Ref1.int1).getJson ===
      """[
        |{"int": 1, "str": "a", "refs1": [
        |   {"int1": 10},
        |   {"int1": 11}]},
        |{"int": 2, "str": "b", "refs1": [
        |   {"int1": 20},
        |   {"int1": 21}]}
        |]""".stripMargin
  }

  "Nested enum after ref" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.enum1) insert List(("a", List("enum11")))
    m(Ns.str.Refs1 * Ref1.enum1).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"enum1": "enum11"}]}
        |]""".stripMargin
  }


  "Nested ref without attribute" in new CoreSetup {
    m(Ns.str.Refs1 * Ref1.int1.Ref2.int2) insert List(
      ("a", List((11, 12))),
      ("b", List((21, 22))))


    m(Ns.str.Refs1 * Ref1.int1_.Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int2": 12}]},
        |{"str": "b", "refs1": [
        |   {"int2": 22}]}
        |]""".stripMargin

    // We can omit tacet attribute between Ref1 and Ref2
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int2": 12}]},
        |{"str": "b", "refs1": [
        |   {"int2": 22}]}
        |]""".stripMargin
  }


  "Intermediate references without attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.Ref2.int2) insert List(
      ("a", List(10, 20)),
      ("b", List(30))
    )
    m(Ns.str.Refs1 * Ref1.Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int2": 10},
        |   {"int2": 20}]},
        |{"str": "b", "refs1": [
        |   {"int2": 30}]}
        |]""".stripMargin
  }


  "Intermediate references with optional attributes" in new CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2) insert List(
      ("a", List((Some(1), 10), (None, 20))),
      ("b", List((Some(3), 30)))
    )

    m(Ns.str.Refs1 * Ref1.int1$.Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int1": 1, "int2": 10},
        |   {"int1": null, "int2": 20}]},
        |{"str": "b", "refs1": [
        |   {"int1": 3, "int2": 30}]}
        |]""".stripMargin
  }


  "Optional attribute" in new CoreSetup {

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2) insert List(
      ("a", Some(2), List(20)),
      ("b", None, List(10, 11))
    )

    m(Ns.str.Refs1.int1$.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "int1": 2, "refs2": [
        |   {"int2": 20}]},
        |{"str": "b", "int1": null, "refs2": [
        |   {"int2": 10},
        |   {"int2": 11}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs2": [
        |   {"int2": 20}]},
        |{"str": "b", "refs2": [
        |   {"int2": 10},
        |   {"int2": 11}]}
        |]""".stripMargin
  }


  "One - one" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "int1": 1, "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Ref1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin
  }


  "One - one - many" in new CoreSetup {
    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2) insert List(("a", 1, List(Set(2, 3))))

    m(Ns.str.Ref1.int1.Refs2 * Ref2.ints2).getJson ===
      """[
        |{"str": "a", "int1": 1, "refs2": [
        |   {"ints2": #{3 2}}]}
        |]""".stripMargin

//    m(Ns.str.Ref1.Refs2 * Ref2.ints2).getJson ===
//      """[
//        |{"str": "a", "refs2": [
//        |   {"ints2": #{3 2}}]}
//        |]""".stripMargin
  }


  "Many - one" in new CoreSetup {
    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2) insert List(("a", 1, List(2)))

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "int1": 1, "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin
  }


  "Flat ManyRef simple" in new CoreSetup {
    Ns.str.Refs1.int1.Refs2.int2.insert("a", 1, 2)

    Ns.str.Refs1.int1.Refs2.int2.getJson ===
      """[
        |{"str": "a", "int1": 1, "int2": 2}
        |]""".stripMargin

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "int1": 1, "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"int1": 1, "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin
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

    m(Ns.str.Refs1.*(Ref1.int1.Refs2 * Ref2.int2)).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int1": 1, "refs2": [
        |      {"int2": 11}]},
        |   {"int1": 2, "refs2": [
        |      {"int2": 21},
        |      {"int2": 22}]}]},
        |{"str": "b", "refs1": [
        |   {"int1": 3, "refs2": [
        |      {"int2": 31},
        |      {"int2": 32}]},
        |   {"int1": 4, "refs2": [
        |      {"int2": 41}]}]}
        |]""".stripMargin

    m(Ns.str.Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "int1": 1, "refs2": [
        |   {"int2": 11},
        |   {"int2": 21},
        |   {"int2": 22}]},
        |{"str": "b", "int1": 3, "refs2": [
        |   {"int2": 31},
        |   {"int2": 32},
        |   {"int2": 41}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.int1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"int1": 1, "refs2": [
        |   {"int2": 11},
        |   {"int2": 21},
        |   {"int2": 22}]}
        |]""".stripMargin

    m(Ns.str_("a").Refs1.int1_(2).Refs2 * Ref2.int2).getJson ===
      """[
        |{"refs2": [
        |   {"int2": 21},
        |   {"int2": 22}]}
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
        |{"str": "a", "int1": 1, "str1": "x", "refs2": [
        |   {"int2": 11, "str2": "xx"},
        |   {"int2": 12, "str2": "xxx"},
        |   {"int2": 21, "str2": "yy"},
        |   {"int2": 22, "str2": "yyy"}]}
        |]""".stripMargin
  }


  "None - one" in new CoreSetup {
    m(Ns.str.Refs1.Refs2 * Ref2.int2) insert List(("a", List(2)))

    m(Ns.str.Refs1.Refs2 * Ref2.int2).getJson ===
      """[
        |{"str": "a", "refs2": [
        |   {"int2": 2}]}
        |]""".stripMargin
  }


  "Refs after nested" in new CoreSetup {
    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)) insert List(
      ("a", List(
        (1, List(11)))),
      ("b", List(
        (2, List(21, 22)),
        (3, List(31))))
    )

    m(Ns.str.Refs1 * (Ref1.int1.Refs2 * Ref2.int2)).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int1": 1, "refs2": [
        |      {"int2": 11}]}]},
        |{"str": "b", "refs1": [
        |   {"int1": 2, "refs2": [
        |      {"int2": 21},
        |      {"int2": 22}]},
        |   {"int1": 3, "refs2": [
        |      {"int2": 31}]}]}
        |]""".stripMargin

    m(Ns.str.Refs1 * Ref1.int1.Refs2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int1": 1, "int2": 11}]},
        |{"str": "b", "refs1": [
        |   {"int1": 2, "int2": 21},
        |   {"int1": 3, "int2": 31}]}
        |]""".stripMargin

    // Still grouped by ref1 values
    m(Ns.str.Refs1 * Ref1.Refs2.int2).getJson ===
      """[
        |{"str": "a", "refs1": [
        |   {"int2": 11}]},
        |{"str": "b", "refs1": [
        |   {"int2": 21},
        |   {"int2": 31}]}
        |]""".stripMargin
  }


  "Back ref" >> {

    "Nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1) insert List(("book", "John", List("Marc")))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).getJson ===
        """[
          |{"str": "book", "str1": "John", "refs1": [
          |   {"str1": "Marc"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1).getJson ===
        """[
          |{"str": "book", "str1": "John", "str1": "Marc"}
          |]""".stripMargin
    }

    "Nested + adjacent" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2) insert List(("book", "John", List(("Marc", "Musician"))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1.Refs2.str2).getJson ===
        """[
          |{"str": "book", "str1": "John", "refs1": [
          |   {"str1": "Marc", "str2": "Musician"}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"str": "book", "str1": "John", "str1": "Marc", "str2": "Musician"}
          |]""".stripMargin
    }

    "Nested + nested" in new CoreSetup {
      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)) insert List(("book", "John", List(("Marc", List("Musician")))))

      m(Ns.str.Ref1.str1._Ns.Refs1 * (Ref1.str1.Refs2 * Ref2.str2)).getJson ===
        """[
          |{"str": "book", "str1": "John", "refs1": [
          |   {"str1": "Marc", "refs2": [
          |      {"str2": "Musician"}]}]}
          |]""".stripMargin

      m(Ns.str.Ref1.str1._Ns.Refs1.str1.Refs2.str2).getJson ===
        """[
          |{"str": "book", "str1": "John", "str1": "Marc", "str2": "Musician"}
          |]""".stripMargin
    }
  }


  "Attributes after nested group" in new CoreSetup {
    m(Ns.long.double.Refs1.*(Ref1.str1.int1)._Ns.bool) insert List(
      (100L, 200.0, List(("aaa", 300), ("bbb", 400)), true),
      (111L, 222.0, List(("xxx", 333), ("yyy", 444)), false)
    )

    (m(Ns.long.double.Refs1.*(Ref1.str1.int1)._Ns.bool).getJson must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "To get nested json Molecule expects the nested attributes to be last in the molecule. Found:\n" +
      "Atom(ns,bool,Boolean,1,VarValue,None,List(),List())"
  }


  "Applied eid" in new CoreSetup {
    val eid = Ns.str.Refs1.*(Ref1.int1).insert("a", List(1, 2)).eid
    Ns(eid).Refs1.*(Ref1.int1).getJson ===
      """[
        |{"refs1": [
        |   {"int1": 1},
        |   {"int1": 2}]}
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
        |{"str1": "r1a", "refs2": [
        |   {"str2": "r2a"},
        |   {"str2": "r2b"}]},
        |{"str1": "r1b", "refs2": [
        |   {"str2": "r2c"},
        |   {"str2": "r2d"}]}
        |]""".stripMargin

    // With Ns
    // "Implicit" reference from Ns to Ref1 (without any attributes) implies that
    // some Ns entity is referencing some Ref1 entity.
    // This excludes "r1b" since no Ns entities reference it.
    Ns.Refs1.str1.Refs2.*(Ref2.str2).getJson ===
      """[
        |{"str1": "r1a", "refs2": [
        |   {"str2": "r2a"},
        |   {"str2": "r2b"}]}
        |]""".stripMargin
  }
}
