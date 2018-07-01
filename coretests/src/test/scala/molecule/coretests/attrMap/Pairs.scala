package molecule.coretests.attrMap

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._

class Pairs extends Base {

  // (Variables for testing resolving identifiers)
  val en      = "en"
  val fr      = "fr"
  val hiThere = "Hi there"
  val pair1   = "en" -> "Hi there"
  val pair2   = "en" -> hiThere
  val pair3   = en -> "Hi there"
  val pair4   = en -> hiThere
  val pair5   = ("en", "Hi there")
  val pair6   = ("en", hiThere)
  val pair7   = (en, "Hi there")
  val pair8   = (en, hiThere)
  val pair9   = (fr, "Bonjour")


  // Attribute maps offer to work with pairs of key/values in various ways.

  "Key -> value" in new Setup {

    val en_hi_there = List((1, Map("en" -> "Hi there")))

    // Find a specific key/value pair
    Ns.int.strMap("en" -> "Hi there").get === en_hi_there
    Ns.int.strMap(("en", "Hi there")).get === en_hi_there


    // Variables sanity checks

    Ns.int.strMap("en" -> hiThere).get === en_hi_there
    Ns.int.strMap(en -> "Hi there").get === en_hi_there
    Ns.int.strMap(en -> hiThere).get === en_hi_there

    // Alternative tuple notation
    Ns.int.strMap(("en", hiThere)).get === en_hi_there
    Ns.int.strMap((en, "Hi there")).get === en_hi_there
    Ns.int.strMap((en, hiThere)).get === en_hi_there

    Ns.int.strMap(pair1).get === en_hi_there
    Ns.int.strMap(pair2).get === en_hi_there
    Ns.int.strMap(pair3).get === en_hi_there
    Ns.int.strMap(pair4).get === en_hi_there
    Ns.int.strMap(pair5).get === en_hi_there
    Ns.int.strMap(pair6).get === en_hi_there
    Ns.int.strMap(pair7).get === en_hi_there
    Ns.int.strMap(pair8).get === en_hi_there


    Ns.int.intMap("en" -> 10).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10))
    )
  }


  "Key -> value with regex" in new Setup {

    // We can widen the values search with a regex
    Ns.int.strMap("en" -> ".*Hi.*").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi"))
    )
    // Even for the key
    Ns.int.strMap("en|da" -> ".*Hi.*").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser"))
    )
  }


  "Multiple key/value pairs" in new Setup {

    // Multiple pairs
    Ns.int.strMap("en" -> "Hi there" or "fr" -> "Bonjour").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("fr" -> "Bonjour"))
    )
    // Same as
    Ns.int.strMap("en" -> "Hi there", "fr" -> "Bonjour").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("fr" -> "Bonjour"))
    )

    // To have full control of matching keys and values we
    // can use multiple pairs

    // OR semantics

    // Comma-separated pairs have OR-semantics (can't use 'or'-notation for pairs)
    Ns.int.strMap("en" -> "Hi there", "da" -> "Hej").get === List(
      (1, Map("en" -> "Hi there")),
      (3, Map("da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    Ns.int.intMap("en" -> 10, "fr" -> 20).get === List(
      (1, Map("en" -> 10)),
      (2, Map("fr" -> 20, "en" -> 10))
    )


    // Variables sanity checks

    val en_hi_fr_bonjour = List((1, Map("en" -> "Hi there")), (2, Map("fr" -> "Bonjour")))

    Ns.int.strMap("en" -> hiThere, fr -> "Bonjour").get === en_hi_fr_bonjour
    Ns.int.strMap("en" -> hiThere, pair9).get === en_hi_fr_bonjour
    Ns.int.strMap(pair2, fr -> "Bonjour").get === en_hi_fr_bonjour
    Ns.int.strMap(pair2, pair9).get === en_hi_fr_bonjour

    Ns.int.strMap(Seq("en" -> hiThere, fr -> "Bonjour")).get === en_hi_fr_bonjour
    Ns.int.strMap(Seq("en" -> hiThere, pair9)).get === en_hi_fr_bonjour
    Ns.int.strMap(Seq(pair2, fr -> "Bonjour")).get === en_hi_fr_bonjour
    Ns.int.strMap(Seq(pair2, pair9)).get === en_hi_fr_bonjour

    val pairs1 = Seq("en" -> hiThere, fr -> "Bonjour")
    val pairs2 = Seq("en" -> hiThere, pair9)
    val pairs3 = Seq(pair2, fr -> "Bonjour")
    val pairs4 = Seq(pair2, pair9)
    Ns.int.strMap(pairs1).get === en_hi_fr_bonjour
    Ns.int.strMap(pairs2).get === en_hi_fr_bonjour
    Ns.int.strMap(pairs3).get === en_hi_fr_bonjour
    Ns.int.strMap(pairs4).get === en_hi_fr_bonjour
  }
}