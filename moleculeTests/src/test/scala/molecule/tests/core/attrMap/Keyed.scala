package molecule.tests.core.attrMap

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out2._

class Keyed extends Base {

  val en_da       = Seq("en", "da")
  val hi_he       = Seq("Hi there", "Hello")
  val _10_30      = Seq(10, 30)
  val date1_date3 = Seq(date1, date3)


  "Key only" in new Setup {
    Ns.strMapK("fr").get === List("Bonjour")
    Ns.intMapK("fr").get === List(20)
    Ns.longMapK("fr").get === List(20L)
    Ns.floatMapK("fr").get === List(20.0f)
    Ns.doubleMapK("fr").get === List(20.0)
    Ns.boolMapK("fr").get === List(false)
    Ns.dateMapK("fr").get === List(date2)
    Ns.uuidMapK("fr").get === List(uuid2)
    Ns.uriMapK("fr").get === List(uri2)
  }


  "Single value" in new Setup {

    // Whereas attribute maps return Maps
    Ns.int.strMap("en" -> "Hi there").get === List((1, Map("en" -> "Hi there")))
    Ns.int.strMap.k("en").apply("Hi there").get === List((1, Map("en" -> "Hi there")))

    // ..the special "K"-appended (for Keyed) attribute returns the value only:
    Ns.int.strMapK("en")("Hi there").get === List((1, "Hi there"))
  }


  "Multiple values" in new Setup {

    val hi_hello = List(
      (1, "Hi there"),
      (3, "Hello")
    )
    Ns.int.strMapK("en")("Hi there" or "Hello").get === hi_hello
    Ns.int.strMapK("en")("Hi there", "Hello").get === hi_hello
    Ns.int.strMapK("en")(Seq("Hi there", "Hello")).get === hi_hello
    Ns.int.strMapK(en)(hi_he).get === hi_hello

    Ns.int.strMapK_("en")("Hi there" or "Hello").get === List(1, 3)
    Ns.int.strMapK_("en")("Hi there", "Hello").get === List(1, 3)
    Ns.int.strMapK_("en")(Seq("Hi there", "Hello")).get === List(1, 3)
    Ns.int.strMapK_(en)(hi_he).get === List(1, 3)


    val en_10_30 = List(
      (3, 30),
      (2, 10),
      (1, 10)
    )
    Ns.int.intMapK("en")(10 or 30).get === en_10_30
    Ns.int.intMapK("en")(10, 30).get === en_10_30
    Ns.int.intMapK("en")(Seq(10, 30)).get === en_10_30
    Ns.int.intMapK("en")(_10_30).get === en_10_30

    Ns.int.intMapK_("en")(10 or 30).get === List(1, 2, 3)
    Ns.int.intMapK_("en")(10, 30).get === List(1, 2, 3)
    Ns.int.intMapK_("en")(Seq(10, 30)).get === List(1, 2, 3)
    Ns.int.intMapK_("en")(_10_30).get === List(1, 2, 3)


    val en_date1_date3 = List(
      (3, date3),
      (2, date1),
      (1, date1),
    )
    Ns.int.dateMapK("en")(date1 or date3).get === en_date1_date3
    Ns.int.dateMapK("en")(date1, date3).get === en_date1_date3
    Ns.int.dateMapK("en")(Seq(date1, date3)).get === en_date1_date3
    Ns.int.dateMapK("en")(date1_date3).get === en_date1_date3

    Ns.int.dateMapK_("en")(date1 or date3).get === List(1, 2, 3)
    Ns.int.dateMapK_("en")(date1, date3).get === List(1, 2, 3)
    Ns.int.dateMapK_("en")(Seq(date1, date3)).get === List(1, 2, 3)
    Ns.int.dateMapK_("en")(date1_date3).get === List(1, 2, 3)
  }


  "Regular expressions" in new Setup {

    // String attribute maps allow us to use regexes
    // Note that searches are case-sensitive ("Hi there" not matched)
    Ns.int.strMapK("en")(".*He.*").get === List((3, "Hello"))
    Ns.int.strMapK_("en")(".*He.*").get === List(3)
  }


  "Multiple keys" in new Setup {

    val en_da_hi = List(
      (2, "Oh, Hi"),
      (2, "Hilser"),
      (1, "Hi there")
    )

    // We can get values with multiple keys by using a regex for the key
    Ns.int.strMapK("en|da")(".*Hi.*").get === en_da_hi
    Ns.int.strMapK_("en|da")(".*Hi.*").get === List(1, 2)

    // Freetext searches can also be done with `contains(needle)`
    Ns.int.strMapK("en|da").contains("Hi").get === en_da_hi
    Ns.int.strMapK_("en|da").contains("Hi").get === List(1, 2)


    Ns.int.intMapK("en|da")(30).get === List(
      (4, 30),
      (3, 30),
      (1, 30)
    )
    Ns.int.intMapK_("en|da")(30).get === List(1, 3, 4)


    Ns.int.dateMapK("en|da")(date3).get === List(
      (1, date3),
      (3, date3),
      (4, date3),
    )
    Ns.int.dateMapK_("en|da")(date3).get === List(1, 3, 4)
  }


  "Multiple keys, multiple values" in new Setup {

    val en_da_oh_he = List(
      (2, "Oh, Hi"),
      (3, "Hello"),
      (3, "Hej"),
      (4, "Hej"),
      (1, "Hejsa")
    )
    Ns.int.strMapK("en|da")("Oh.*" or ".*He.*").get === en_da_oh_he
    Ns.int.strMapK_("en|da")("Oh.*" or ".*He.*").get === List(1, 2, 3, 4)


    val en_da_10_30 = List(
      (4, 30),
      (3, 30),
      (1, 30),
      (2, 10),
      (1, 10)
    )
    Ns.int.intMapK("en|da")(10 or 30).get === en_da_10_30
    Ns.int.intMapK_("en|da")(10 or 30).get === List(1, 2, 3, 4)


    val en_da_date1_date3 = List(
      (1, date3),
      (3, date3),
      (4, date3),
      (2, date1),
      (1, date1),
    )
    Ns.int.dateMapK("en|da")(date1 or date3).get === en_da_date1_date3
    Ns.int.dateMapK_("en|da")(date1 or date3).get === List(1, 2, 3, 4)
  }


  "Value comparison" in new Setup {

    Ns.int.strMapK("en").<("Hi").get === List(
      (3, "Hello")
    )

    Ns.int.strMapK("en").>("Hi").get === List(
      (2, "Oh, Hi"),
      (1, "Hi there")
    )

    Ns.int.strMapK_("en").<("Hi").get === List(3)
    Ns.int.strMapK_("en").>("Hi").get === List(1, 2)

    // Multiple keys

    Ns.int.strMapK("en|da").<("Hi").get === List(
      (3, "Hello"),
      (3, "Hej"),
      (4, "Hej"),
      (1, "Hejsa")
    )

    Ns.int.strMapK("en|da").>("Hi").get === List(
      (2, "Oh, Hi"),
      (2, "Hilser"),
      (1, "Hi there")
    )


    Ns.int.intMapK_("en|da").>(20).get === List(1, 3, 4)
    Ns.int.intMapK_("en|da").>=(20).get === List(1, 3, 4)
    Ns.int.intMapK_("en|da").<=(20).get === List(1, 2)
    Ns.int.intMapK_("en|da").<(20).get === List(1, 2)
    Ns.int.intMapK_("en|da").>(-10).get === List(1, 2, 3, 4)


    Ns.int.dateMapK("en|da").>(date2).get === List(
      (1, date3),
      (3, date3),
      (4, date3),
    )
    Ns.int.dateMapK("en|da").>=(date2).get === List(
      (1, date3),
      (3, date3),
      (4, date3),
    )
    Ns.int.dateMapK("en|da").<=(date2).get === List(
      (2, date1),
      (1, date1),
    )
    Ns.int.dateMapK("en|da").<(date2).get === List(
      (2, date1),
      (1, date1),
    )

    Ns.int.dateMapK_("en|da").>(date2).get === List(1, 3, 4)
    Ns.int.dateMapK_("en|da").>=(date2).get === List(1, 3, 4)
    Ns.int.dateMapK_("en|da").<=(date2).get === List(1, 2)
    Ns.int.dateMapK_("en|da").<(date2).get === List(1, 2)
  }
}