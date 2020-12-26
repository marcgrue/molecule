package molecule.tests.core.attrMap

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out2._

class Keys extends Base {

  "One key" in new Setup {

    Ns.int.strMap.k("en").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello"))
    )
    Ns.int.strMap_.k("en").get === List(1, 2, 3)

    Ns.int.intMap.k("en").get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map("en" -> 30))
    )
    Ns.int.intMap_.k("en").get === List(1, 2, 3)

    // OBS: Since a map attribute returns a map, we have to beware that
    // key/value pairs are coalesced if there are no other attributes and
    // we end up with one random pair:
    Ns.strMap.k("en").get === List(
      Map("en" -> "Oh, Hi") // random pair...
    )

    // A workaround to get all the mapped values only is to add the `e`
    // entity attribute (which always has a value) and then filter it out
    // from the result set:
    Ns.e.strMap.k("en").get.map(_._2) === List(
      Map("en" -> "Hi there"),
      Map("en" -> "Oh, Hi"),
      Map("en" -> "Hello")
    )
    Ns.e.intMap.k("en").get.map(_._2) === List(
      Map("en" -> 10),
      Map("en" -> 10),
      Map("en" -> 30)
    )

    // Then we might as well return only the values
    Ns.e.strMap.k("en").get.map(_._2("en")) === List(
      "Hi there",
      "Oh, Hi",
      "Hello"
    )

    Ns.e.intMap.k("en").get.map(_._2("en")) === List(
      10,
      10,
      30
    )
  }


  "Multiple keys (OR semantics)" in new Setup {

    // Variables
    val seq1 = Seq("en", "fr")
    val seq2 = Seq(en, fr)

    val en_fr_str = List(
      (1, Map("en" -> "Hi there")),
      (2, Map("fr" -> "Bonjour", "en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello"))
    )
    Ns.int.strMap.k("en" or "fr").get === en_fr_str
    Ns.int.strMap.k("en", "fr").get === en_fr_str
    Ns.int.strMap.k(Seq("en", "fr")).get === en_fr_str
    Ns.int.strMap.k(Seq(en, fr)).get === en_fr_str
    Ns.int.strMap.k(seq1).get === en_fr_str
    Ns.int.strMap.k(seq2).get === en_fr_str

    Ns.int.strMap_.k("en" or "fr").get === List(1,2,3)
    Ns.int.strMap_.k("en", "fr").get === List(1,2,3)
    Ns.int.strMap_.k(Seq("en", "fr")).get === List(1, 2, 3)
    Ns.int.strMap_.k(Seq(en, fr)).get === List(1, 2, 3)
    Ns.int.strMap_.k(seq1).get === List(1, 2, 3)
    Ns.int.strMap_.k(seq2).get === List(1, 2, 3)


    val en_fr_int = List(
      (1, Map("en" -> 10)),
      (2, Map("fr" -> 20, "en" -> 10)),
      (3, Map("en" -> 30))
    )
    Ns.int.intMap.k("en" or "fr").get === en_fr_int
    Ns.int.intMap.k("en", "fr").get === en_fr_int
    Ns.int.intMap.k(Seq("en", "fr")).get === en_fr_int
    Ns.int.intMap.k(Seq(en, fr)).get === en_fr_int
    Ns.int.intMap.k(seq1).get === en_fr_int
    Ns.int.intMap.k(seq2).get === en_fr_int

    Ns.int.intMap_.k("en" or "fr").get === List(1,2,3)
    Ns.int.intMap_.k("en", "fr").get === List(1,2,3)
    Ns.int.intMap_.k(Seq("en", "fr")).get === List(1, 2, 3)
    Ns.int.intMap_.k(Seq(en, fr)).get === List(1, 2, 3)
    Ns.int.intMap_.k(seq1).get === List(1, 2, 3)
    Ns.int.intMap_.k(seq2).get === List(1, 2, 3)


    val en_fr_date = List(
      (1, Map("en" -> date1)),
      (2, Map("fr" -> date2, "en" -> date1)),
      (3, Map("en" -> date3))
    )
    Ns.int.dateMap.k("en" or "fr").get === en_fr_date
    Ns.int.dateMap.k("en", "fr").get === en_fr_date
    Ns.int.dateMap.k(Seq("en", "fr")).get === en_fr_date
    Ns.int.dateMap.k(Seq(en, fr)).get === en_fr_date
    Ns.int.dateMap.k(seq1).get === en_fr_date
    Ns.int.dateMap.k(seq2).get === en_fr_date

    Ns.int.dateMap_.k("en" or "fr").get === List(1,2,3)
    Ns.int.dateMap_.k("en", "fr").get === List(1,2,3)
    Ns.int.dateMap_.k(Seq("en", "fr")).get === List(1, 2, 3)
    Ns.int.dateMap_.k(Seq(en, fr)).get === List(1, 2, 3)
    Ns.int.dateMap_.k(seq1).get === List(1, 2, 3)
    Ns.int.dateMap_.k(seq2).get === List(1, 2, 3)
  }

  // AND semantics aren't relevant for attribute maps since we should
  // consider them as card-one containers of one "value with variation".
  // When we need to work with those variations we can narrow those
  // by OR semantics.
}