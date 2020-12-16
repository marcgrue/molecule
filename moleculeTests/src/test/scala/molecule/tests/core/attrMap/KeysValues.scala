package molecule.tests.core.attrMap

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out2._

class KeysValues extends Base {

  val en_da       = Seq("en", "da")
  val hi_he       = Seq("Hi there", "Hello")
  val _10_30      = Seq(10, 30)
  val date1_date3 = Seq(date1, date3)


  "(key)(value)" in new Setup {

    // As an alternative to pair notation we can use key/value notation.
    // The following two molecules accomplish the same:
    Ns.int.strMap("en" -> "Hi there").get === List((1, Map("en" -> "Hi there")))
    Ns.int.strMap.k("en")("Hi there").get === List((1, Map("en" -> "Hi there")))


    // Multiple values

    val hi_hello = List(
      (1, Map("en" -> "Hi there")),
      (3, Map("en" -> "Hello"))
    )
    // The k/apply syntax is suitable when we want to treat
    // key and value independently. So, instead of repeating the key:
    Ns.int.strMap("en" -> "Hi there", "en" -> "Hello").get === hi_hello

    // ..we can let the key "en" be applied to both values:
    Ns.int.strMap.k("en")("Hi there" or "Hello").get === hi_hello
    Ns.int.strMap.k("en")("Hi there", "Hello").get === hi_hello
    Ns.int.strMap.k("en")(Seq("Hi there", "Hello")).get === hi_hello
    Ns.int.strMap.k(en)(hi_he).get === hi_hello

    Ns.int.strMap_.k("en")("Hi there" or "Hello").get === List(1, 3)
    Ns.int.strMap_.k("en")("Hi there", "Hello").get === List(1, 3)
    Ns.int.strMap_.k("en")(Seq("Hi there", "Hello")).get === List(1, 3)
    Ns.int.strMap_.k(en)(hi_he).get === List(1, 3)


    val en_10_30 = List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map("en" -> 30))
    )
    Ns.int.intMap.k("en")(10 or 30).get === en_10_30
    Ns.int.intMap.k("en")(10, 30).get === en_10_30
    Ns.int.intMap.k("en")(Seq(10, 30)).get === en_10_30
    Ns.int.intMap.k("en")(_10_30).get === en_10_30

    Ns.int.intMap_.k("en")(10 or 30).get === List(1, 2, 3)
    Ns.int.intMap_.k("en")(10, 30).get === List(1, 2, 3)
    Ns.int.intMap_.k("en")(Seq(10, 30)).get === List(1, 2, 3)
    Ns.int.intMap_.k("en")(_10_30).get === List(1, 2, 3)


    val en_date1_date3 = List(
      (1, Map("en" -> date1)),
      (2, Map("en" -> date1)),
      (3, Map("en" -> date3))
    )
    Ns.int.dateMap.k("en")(date1 or date3).get === en_date1_date3
    Ns.int.dateMap.k("en")(date1, date3).get === en_date1_date3
    Ns.int.dateMap.k("en")(Seq(date1, date3)).get === en_date1_date3
    Ns.int.dateMap.k("en")(date1_date3).get === en_date1_date3

    Ns.int.dateMap_.k("en")(date1 or date3).get === List(1, 2, 3)
    Ns.int.dateMap_.k("en")(date1, date3).get === List(1, 2, 3)
    Ns.int.dateMap_.k("en")(Seq(date1, date3)).get === List(1, 2, 3)
    Ns.int.dateMap_.k("en")(date1_date3).get === List(1, 2, 3)
  }


  "Regular expressions" in new Setup {

    // String attribute maps allow us to use regexes
    // Note that searches are case-insensitive

    Ns.int.strMap.k("en")(".*Hi.*").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi"))
    )
    Ns.int.strMap_.k("en")(".*Hi.*").get === List(1, 2)
  }


  "Multiple keys" in new Setup {

    val en_da_hi = List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser"))
    )
    val reg_ex   = ".*Hi.*"

    Ns.int.strMap.k("en" or "da")(".*Hi.*").get === en_da_hi
    Ns.int.strMap.k("en", "da")(".*Hi.*").get === en_da_hi
    Ns.int.strMap.k(Seq("en", "da"))(".*Hi.*").get === en_da_hi
    Ns.int.strMap.k(en_da)(reg_ex).get === en_da_hi

    Ns.int.strMap_.k("en" or "da")(".*Hi.*").get === List(1, 2)
    Ns.int.strMap_.k("en", "da")(".*Hi.*").get === List(1, 2)
    Ns.int.strMap_.k(Seq("en", "da"))(".*Hi.*").get === List(1, 2)
    Ns.int.strMap_.k(en_da)(reg_ex).get === List(1, 2)

    // Freetext searches can also be done with `contains(needle)`
    Ns.int.strMap.k("en" or "da").contains("Hi").get === en_da_hi
    Ns.int.strMap_.k("en" or "da").contains("Hi").get === List(1, 2)


    val en_da_30 = List(
      (1, Map("da" -> 30L)),
      (3, Map("en" -> 30L, "da" -> 30L)),
      (4, Map("da" -> 30L))
    )
    Ns.int.intMap.k("en" or "da")(30).get === en_da_30
    Ns.int.intMap.k("en", "da")(30).get === en_da_30
    Ns.int.intMap.k(Seq("en", "da"))(30).get === en_da_30
    Ns.int.intMap.k(en_da)(30).get === en_da_30

    Ns.int.intMap_.k("en" or "da")(30).get === List(1, 3, 4)
    Ns.int.intMap_.k("en", "da")(30).get === List(1, 3, 4)
    Ns.int.intMap_.k(Seq("en", "da"))(30).get === List(1, 3, 4)
    Ns.int.intMap_.k(en_da)(30).get === List(1, 3, 4)


    val en_da_date3 = List(
      (1, Map("da" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    Ns.int.dateMap.k("en" or "da")(date3).get === en_da_date3
    Ns.int.dateMap.k("en", "da")(date3).get === en_da_date3
    Ns.int.dateMap.k(Seq("en", "da"))(date3).get === en_da_date3
    Ns.int.dateMap.k(en_da)(date3).get === en_da_date3

    Ns.int.dateMap_.k("en" or "da")(date3).get === List(1, 3, 4)
    Ns.int.dateMap_.k("en", "da")(date3).get === List(1, 3, 4)
    Ns.int.dateMap_.k(Seq("en", "da"))(date3).get === List(1, 3, 4)
    Ns.int.dateMap_.k(en_da)(date3).get === List(1, 3, 4)
  }


  "Multiple keys, multiple values" in new Setup {

    val oh_he       = Seq("Oh.*", ".*He.*")
    val en_da_oh_he = List(
      (1, Map("da" -> "Hejsa")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )
    Ns.int.strMap.k("en" or "da")("Oh.*" or ".*He.*").get === en_da_oh_he
    Ns.int.strMap.k("en", "da")("Oh.*", ".*He.*").get === en_da_oh_he
    Ns.int.strMap.k(en_da)(Seq("Oh.*", ".*He.*")).get === en_da_oh_he
    Ns.int.strMap.k(en_da)(oh_he).get === en_da_oh_he

    Ns.int.strMap_.k("en" or "da")("Oh.*" or ".*He.*").get === List(1, 2, 3, 4)
    Ns.int.strMap_.k("en", "da")("Oh.*", ".*He.*").get === List(1, 2, 3, 4)
    Ns.int.strMap_.k(en_da)(Seq("Oh.*", ".*He.*")).get === List(1, 2, 3, 4)
    Ns.int.strMap_.k(en_da)(oh_he).get === List(1, 2, 3, 4)


    val en_da_10_30 = List(
      (1, Map("en" -> 10, "da" -> 30)),
      (2, Map("en" -> 10, "da" -> 10)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )
    Ns.int.intMap.k("en" or "da")(10 or 30).get === en_da_10_30
    Ns.int.intMap.k("en", "da")(10, 30).get === en_da_10_30
    Ns.int.intMap.k(Seq("en", "da"))(Seq(10, 30)).get === en_da_10_30
    Ns.int.intMap.k(en_da)(_10_30).get === en_da_10_30

    Ns.int.intMap_.k("en" or "da")(10 or 30).get === List(1, 2, 3, 4)
    Ns.int.intMap_.k("en", "da")(10, 30).get === List(1, 2, 3, 4)
    Ns.int.intMap_.k(Seq("en", "da"))(Seq(10, 30)).get === List(1, 2, 3, 4)
    Ns.int.intMap_.k(en_da)(_10_30).get === List(1, 2, 3, 4)


    val en_da_date1_date3 = List(
      (1, Map("en" -> date1, "da" -> date3)),
      (2, Map("en" -> date1, "da" -> date1)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    Ns.int.dateMap.k("en" or "da")(date1 or date3).get === en_da_date1_date3
    Ns.int.dateMap.k("en", "da")(date1, date3).get === en_da_date1_date3
    Ns.int.dateMap.k(Seq("en", "da"))(Seq(date1, date3)).get === en_da_date1_date3
    Ns.int.dateMap.k(en_da)(date1_date3).get === en_da_date1_date3


    Ns.int.dateMap_.k("en" or "da")(date1 or date3).get === List(1, 2, 3, 4)
    Ns.int.dateMap_.k("en", "da")(date1, date3).get === List(1, 2, 3, 4)
    Ns.int.dateMap_.k(Seq("en", "da"))(Seq(date1, date3)).get === List(1, 2, 3, 4)
    Ns.int.dateMap_.k(en_da)(date1_date3).get === List(1, 2, 3, 4)
  }


  "Value negation" in new Setup {

    Ns.int.strMap_.k("en").not("Hello").get === List(1, 2)
    Ns.int.strMap.k("en").not("Hello").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi"))
    )

    // Multiple value filters (OR semantics)
    Ns.int.strMap_.k("en").not("Hello", "Hi there").get === List(2)


    // Same as

    Ns.int.strMap_.k("en").!=("Hello").get === List(1, 2)
    Ns.int.strMap.k("en").!=("Hello").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi"))
    )

    // Multiple value filters (OR semantics)
    Ns.int.strMap_.k("en").!=("Hello", "Hi there").get === List(2)
  }


  "Value comparison" in new Setup {

    Ns.int.strMap.k("en").<("Hi").get === List(
      (3, Map("en" -> "Hello"))
    )

    Ns.int.strMap.k("en").>("Hi").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi"))
    )

    Ns.int.strMap_.k("en").<("Hi").get === List(3)
    Ns.int.strMap_.k("en").>("Hi").get === List(1,2)


    // Multiple keys

    Ns.int.strMap.k("en" or "da").<("Hi").get === List(
      (1, Map("da" -> "Hejsa")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    Ns.int.strMap.k("en" or "da").>("Hi").get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser"))
    )

    Ns.int.strMap_.k("en" or "da").<("Hi").get === List(1,3,4)
    Ns.int.strMap_.k("en" or "da").>("Hi").get === List(1,2)


    Ns.int.intMap.k("en" or "da").>(20).get === List(
      (1, Map("da" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )
    Ns.int.intMap.k("en" or "da").>=(20).get === List(
      (1, Map("da" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )
    Ns.int.intMap.k("en" or "da").<=(20).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10))
    )
    Ns.int.intMap.k("en" or "da").<(20).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10))
    )

    Ns.int.intMap.k("en" or "da").>(-10).get === List(
      (1, Map("en" -> 10, "da" -> 30)),
      (2, Map("en" -> 10, "da" -> 10)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )

    Ns.int.intMap_.k("en" or "da").>(20).get === List(1,3,4)
    Ns.int.intMap_.k("en" or "da").>=(20).get === List(1,3,4)
    Ns.int.intMap_.k("en" or "da").<=(20).get === List(1,2)
    Ns.int.intMap_.k("en" or "da").<(20).get === List(1,2)
    Ns.int.intMap_.k("en" or "da").>(-10).get === List(1,2,3,4)


    Ns.int.dateMap.k("en" or "da").>(date2).get === List(
      (1, Map("da" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    Ns.int.dateMap.k("en" or "da").>=(date2).get === List(
      (1, Map("da" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    Ns.int.dateMap.k("en" or "da").<=(date2).get === List(
      (1, Map("en" -> date1)),
      (2, Map("en" -> date1, "da" -> date1))
    )
    Ns.int.dateMap.k("en" or "da").<(date2).get === List(
      (1, Map("en" -> date1)),
      (2, Map("en" -> date1, "da" -> date1))
    )

    Ns.int.dateMap_.k("en" or "da").>(date2).get === List(1,3,4)
    Ns.int.dateMap_.k("en" or "da").>=(date2).get === List(1,3,4)
    Ns.int.dateMap_.k("en" or "da").<=(date2).get === List(1,2)
    Ns.int.dateMap_.k("en" or "da").<(date2).get === List(1,2)
  }
}