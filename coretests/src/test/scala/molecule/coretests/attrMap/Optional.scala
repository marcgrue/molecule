package molecule.coretests.attrMap

import molecule._
import molecule.coretests.util.CoreSetup
import molecule.coretests.util.dsl.coreTest._
import molecule.ops.MapOps

class Optional extends Base with MapOps {


  "Post-process keys (String)" in new CoreSetup {

    Ns.int.strMap$ insert List(
      (1, Some(Map("en" -> "Hi there"))),
      (2, Some(Map("fr" -> "Bonjour", "en" -> "Oh, Hi"))),
      (3, Some(Map("en" -> "Hello"))),
      (4, Some(Map("da" -> "Hej"))),
      (5, None)
    )

    // All (5th entity has no strMap)
    Ns.int.strMap$.get === List(
      (1, Some(Map("en" -> "Hi there"))),
      (2, Some(Map("fr" -> "Bonjour", "en" -> "Oh, Hi"))),
      (3, Some(Map("en" -> "Hello"))),
      (4, Some(Map("da" -> "Hej"))),
      (5, None)
    )

    // Values with "en" key
    // We can't apply values to optional values (like `Ns.int.strMap$("en")`)
    // Instead we process the results:
    Ns.int.strMap$.get.map { case (i, s) => (i, s.flatMap(_.get("en"))) } === List(
      (1, Some("Hi there")),
      (2, Some("Oh, Hi")),
      (3, Some("Hello")),
      (4, None),
      (5, None)
    )

    // Or we can use implicit functions for convenience
    Ns.int.strMap$.get.map { case (i, s) => (i, s.at("en")) } === List(
      (1, Some("Hi there")),
      (2, Some("Oh, Hi")),
      (3, Some("Hello")),
      (4, None),
      (5, None)
    )

    // Get filtered map with certain keys
    Ns.int.strMap$.get.map { case (i, s) => (i, s.mapAt("fr", "da")) } === List(
      (1, Map()),
      (2, Map("fr" -> "Bonjour")),
      (3, Map()),
      (4, Map("da" -> "Hej")),
      (5, Map())
    )
  }


  "Post-process keys (Int)" in new CoreSetup {

    Ns.int.intMap$ insert List(
      (1, Some(Map("en" -> 10))),
      (2, Some(Map("fr" -> 20, "en" -> 10))),
      (3, Some(Map("en" -> 30))),
      (4, Some(Map("da" -> 30))),
      (5, None)
    )

    // All (5th entity has no strMap)
    Ns.int.intMap$.get === List(
      (1, Some(Map("en" -> 10))),
      (2, Some(Map("fr" -> 20, "en" -> 10))),
      (3, Some(Map("en" -> 30))),
      (4, Some(Map("da" -> 30))),
      (5, None)
    )

    // Values with "en" key
    // We can't apply values to optional values (like `Ns.int.strMap$("en")`)
    // Instead we process the results:
    Ns.int.intMap$.get.map { case (i, s) => (i, s.flatMap(_.get("en"))) } === List(
      (1, Some(10)),
      (2, Some(10)),
      (3, Some(30)),
      (4, None),
      (5, None)
    )

    // Or we can use implicit functions for convenience
    Ns.int.intMap$.get.map { case (i, s) => (i, s.at("en")) } === List(
      (1, Some(10)),
      (2, Some(10)),
      (3, Some(30)),
      (4, None),
      (5, None)
    )

    // Or we can use implicit functions for convenience
    Ns.int.intMap$.get.map { case (i, s) => (i, s.atOrElse("en", -7)) } === List(
      (1, 10),
      (2, 10),
      (3, 30),
      (4, -7),
      (5, -7)
    )

    // Get filtered map with certain keys
    Ns.int.intMap$.get.map { case (i, s) => (i, s.mapAt("fr", "da")) } === List(
      (1, Map()),
      (2, Map("fr" -> 20)),
      (3, Map()),
      (4, Map("da" -> 30)),
      (5, Map())
    )
  }


  "Post-process values (String)" in new CoreSetup {

    Ns.int.strMap$ insert List(
      (1, Some(Map("en" -> "Hi there"))),
      (2, Some(Map("fr" -> "Bonjour", "en" -> "Oh, Hi"))),
      (3, Some(Map("en" -> "Hello"))),
      (4, Some(Map("da" -> "Hej"))),
      (5, None)
    )

    // Maps with values containing "Hi"
    Ns.int.strMap$.get.map { case (i, s) => (i, s.values("Hi")) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map()),
      (4, Map()),
      (5, Map())
    )

    // Searches are Case insensitive ("there" is included)
    Ns.int.strMap$.get.map { case (i, s) => (i, s.values("He")) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map()),
      (3, Map("en" -> "Hello")),
      (4, Map("da" -> "Hej")),
      (5, Map())
    )

    // Search multiple values. Multiple values have OR semantics:
    // value("hi" OR "he")
    Ns.int.strMap$.get.map { case (i, s) => (i, s.values("hi", "he")) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello")),
      (4, Map("da" -> "Hej")),
      (5, Map())
    )
  }

  "Post-process values (Int)" in new CoreSetup {

    Ns.int.intMap$ insert List(
      (1, Some(Map("en" -> 10))),
      (2, Some(Map("fr" -> 20, "en" -> 10))),
      (3, Some(Map("en" -> 30))),
      (4, Some(Map("da" -> 30))),
      (5, None)
    )

    // Maps with value 10
    Ns.int.intMap$.get.map { case (i, s) => (i, s.values(10)) } === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map()),
      (4, Map()),
      (5, Map())
    )


    // Search value is by default Case insensitive ("there" is included)
    Ns.int.intMap$.get.map { case (i, s) => (i, s.values(30)) } === List(
      (1, Map()),
      (2, Map()),
      (3, Map("en" -> 30)),
      (4, Map("da" -> 30)),
      (5, Map())
    )

    // Search multiple values. Multiple values have OR semantics:
    // value("hi" OR "he")
    Ns.int.intMap$.get.map { case (i, s) => (i, s.values(20, 30)) } === List(
      (1, Map()),
      (2, Map("fr" -> 20)),
      (3, Map("en" -> 30)),
      (4, Map("da" -> 30)),
      (5, Map())
    )
  }


  "Post-process key/value pairs (String)" in new CoreSetup {

    Ns.int.strMap$ insert List(
      (1, Some(Map("en" -> "Hi there"))),
      (2, Some(Map("fr" -> "Bonjour", "en" -> "Oh, Hi"))),
      (3, Some(Map("en" -> "Hello"))),
      (4, Some(Map("da" -> "Hej"))),
      (5, None)
    )

    // Search combination of key and value (Danish now excluded). A key -> value pair has AND semantics:
    // key("en") AND value("He")
    Ns.int.strMap$.get.map { case (i, s) => (i, s.keyValue("en" -> "He")) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map()),
      (3, Map("en" -> "Hello")),
      (4, Map()),
      (5, Map())
    )

    // Search combination of key and multiple values. AND and OR semantics combined:
    // key("en") AND value("he" OR "hi")
    Ns.int.strMap$.get.map { case (i, s) => (i, s.keyValues("en" -> Seq("he", "hi"))) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello")),
      (4, Map()),
      (5, Map())
    )

    // Search combination of multiple keys and values. OR semantics between key/value pairs:
    // (key("en") AND value("he" OR "hi)) OR
    // (key("fr") AND value("bo"))
    Ns.int.strMap$.get.map { case (i, s) => (i, s.keyValues("en" -> Seq("he", "hi"), "fr" -> Seq("bo"))) } === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("fr" -> "Bonjour", "en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello")),
      (4, Map()),
      (5, Map())
    )
  }

  "Post-process key/value pairs (Int)" in new CoreSetup {

    Ns.int.intMap$ insert List(
      (1, Some(Map("en" -> 10))),
      (2, Some(Map("fr" -> 20, "en" -> 10))),
      (3, Some(Map("en" -> 30))),
      (4, Some(Map("da" -> 30))),
      (5, None)
    )

    // Search combination of key and value (Danish now excluded).
    // key -> value pairs have AND semantics:
    // key("en") AND value(10)
    Ns.int.intMap$.get.map { case (i, s) => (i, s.keyValue("en" -> 10)) } === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map()),
      (4, Map()),
      (5, Map())
    )

    // Search combination of key and multiple values. AND and OR semantics combined:
    // key("en") AND value("he" OR "hi")
    Ns.int.intMap$.get.map { case (i, s) => (i, s.keyValues("en" -> Seq(10, 30))) } === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map("en" -> 30)),
      (4, Map()),
      (5, Map())
    )

    // Search combination of multiple keys and values. OR semantics between key/value pairs:
    // (key("en") AND value("he" OR "hi)) OR
    // (key("fr") AND value("bo"))
    Ns.int.intMap$.get.map { case (i, s) => (i, s.keyValues("en" -> Seq(10, 30), "fr" -> Seq(20))) } === List(
      (1, Map("en" -> 10)),
      (2, Map("fr" -> 20, "en" -> 10)),
      (3, Map("en" -> 30)),
      (4, Map()),
      (5, Map())
    )
  }
}