package molecule.coretests.attrMap

import molecule.api.in1_out2._
import molecule.coretests.util.dsl.coreTest._

class InputKeyed extends Base {


  "Equality" in new Setup {

    // Input molecule waiting for a key/value pair
    val inputMolecule = m(Ns.int.strMapK("en").apply(?))

    // We then apply a Map with a value
    inputMolecule.apply("Hello").get === List(
      (3, "Hello")
    )

    // We can use a tacit attribute map (to only return the Int value):
    m(Ns.int.strMapK_("en")(?)).apply("Hello").get === List(3)

    // Note that text searches for attribute maps are case-sensitive
    m(Ns.int.strMapK_("en")(?))("hello").get === List()

    m(Ns.int.intMapK("en")(?))(10).get === List(
      (2, 10),
      (1, 10)
    )
    m(Ns.int.intMapK_("en")(?))(10).get === List(1, 2)
  }


  "Regex/partial search" in new Setup {

    // Regex for value
    m(Ns.int.strMapK("en")(?))(".*He.*").get === List(
      (3, "Hello")
    )

    // Regex for key too
    m(Ns.int.strMapK(".*")(?))(".*He.*").get === List(
      (3, "Hello"),
      (3, "Hej"),
      (4, "Hej"),
      (1, "Hejsa")
    )
    m(Ns.int.strMapK_(".*")(?))(".*He.*").get === List(1, 3, 4)

    // Retrieving a specific value for any key
    m(Ns.int.strMapK(".*")(?))("Hello").get === List(
      (3, "Hello")
    )

    // Since other types than String cannot use regexes we can only
    // search for values equal to our needle:
    m(Ns.int.intMapK(".*")(?))(10).get === List(
      (2, 10),
      (1, 10)
    )
    m(Ns.int.intMapK_(".*")(?))(10).get === List(1, 2)
  }


  "Negation" in new Setup {

    m(Ns.int(2).strMapK(".*").!=(?))("Bon giorno").get === List(
      (2, "Oh, Hi"),
      (2, "Hilser"),
      (2, "Bonjour")
    )

    // Negate multiple values
    // Although not negating pair-wise, in most cases this would probably work instead:
    m(Ns.int(2).strMapK("fr|it").!=(?))("Bonjour|Bon giorno").get === List(
      (2, "Oh, Hi"),
      (2, "Hilser")
    )
    m(Ns.int(2).strMapK_("fr|it").!=(?))("Bonjour|Bon giorno").get === List(2)


    m(Ns.int.intMapK(".*").!=(?))(30).get === List(
      (2, 20),
      (2, 10),
      (1, 10)
    )
    m(Ns.int.intMapK_(".*").!=(?))(30).get === List(1, 2)

    // We can't negate multiple non-string values on input molecules
    // since we can't use regexes on other types than String.
  }


  "Comparison" in new Setup {

    m(Ns.int.strMapK(".*").>(?))("Hej").get === List(
      (2, "Oh, Hi"),
      (2, "Hilser"),
      (1, "Hi there"),
      (3, "Hello"),
      (1, "Hejsa")
    )

    m(Ns.int.strMapK(".*").>=(?))("Hej").get === List(
      (2, "Oh, Hi"),
      (2, "Hilser"),
      (1, "Hi there"),
      (3, "Hello"),
      (3, "Hej"),
      (4, "Hej"),
      (1, "Hejsa")
    )

    m(Ns.int.strMapK(".*").<=(?))("Hej").get === List(
      (2, "Bon giorno"),
      (2, "Bonjour"),
      (3, "Hej"),
      (4, "Hej")
    )

    m(Ns.int.strMapK(".*").<(?))("Hej").get === List(
      (2, "Bon giorno"),
      (2, "Bonjour")
    )

    m(Ns.int.strMapK_(".*").>(?))("Hej").get.sorted === List(1, 2, 3)
    m(Ns.int.strMapK_(".*").>=(?))("Hej").get.sorted === List(1, 2, 3, 4)
    m(Ns.int.strMapK_(".*").<=(?))("Hej").get.sorted === List(2, 3, 4)
    m(Ns.int.strMapK_(".*").<(?))("Hej").get.sorted === List(2)


    // Int

    m(Ns.int.intMapK(".*").>(?))(20).get === List(
      (4, 30),
      (3, 30),
      (2, 30),
      (1, 30)
    )
    m(Ns.int.intMapK(".*").>=(?))(20).get === List(
      (4, 30),
      (3, 30),
      (2, 30),
      (2, 20),
      (1, 30)
    )
    m(Ns.int.intMapK(".*").<=(?))(20).get === List(
      (2, 20),
      (2, 10),
      (1, 10)
    )
    m(Ns.int.intMapK(".*").<(?))(20).get === List(
      (2, 10),
      (1, 10)
    )

    m(Ns.int.intMapK(".*").>(?))(-10).get === List(
      (4, 30),
      (3, 30),
      (2, 30),
      (1, 30),
      (2, 20),
      (2, 10),
      (1, 10)
    )

    m(Ns.int.intMapK_(".*").>(?))(20).get === List(1, 2, 3, 4)
    m(Ns.int.intMapK_(".*").>=(?))(20).get === List(1, 2, 3, 4)
    m(Ns.int.intMapK_(".*").<=(?))(20).get === List(1, 2)
    m(Ns.int.intMapK_(".*").<(?))(20).get === List(1, 2)


    // Date

    m(Ns.int.dateMapK(".*").>(?))(date2).get === List(
      (4, date3),
      (3, date3),
      (2, date3),
      (1, date3),
    )
    m(Ns.int.dateMapK(".*").>=(?))(date2).get === List(
      (4, date3),
      (3, date3),
      (2, date3),
      (1, date3),
      (2, date2),
    )
    m(Ns.int.dateMapK(".*").<=(?))(date2).get === List(
      (1, date1),
      (2, date1),
      (2, date2),
    )
    m(Ns.int.dateMapK(".*").<(?))(date2).get === List(
      (1, date1),
      (2, date1),
    )

    m(Ns.int.dateMapK_(".*").>(?))(date2).get === List(1, 2, 3, 4)
    m(Ns.int.dateMapK_(".*").>=(?))(date2).get === List(1, 2, 3, 4)
    m(Ns.int.dateMapK_(".*").<=(?))(date2).get === List(1, 2)
    m(Ns.int.dateMapK_(".*").<(?))(date2).get === List(1, 2)
  }


  "Different keys, multiple values" in new Setup {

    val hehe = List(
      (3, "Hello"),
      (3, "Hej"),
      (4, "Hej")
    )

    // Retrieve multiple values
    m(Ns.int.strMapK(".*")(?))("Hello" or "Hej").get === hehe
    m(Ns.int.strMapK(".*")(?))("Hello", "Hej").get === hehe
    m(Ns.int.strMapK(".*")(?))("Hello|Hej").get === hehe

    m(Ns.int.strMapK_(".*")(?))("Hello" or "Hej").get === List(3, 4)
    m(Ns.int.strMapK_(".*")(?))("Hello", "Hej").get === List(3, 4)
    m(Ns.int.strMapK_(".*")(?))("Hello|Hej").get === List(3, 4)

    // Using `contains` is redundant since it translates to the same as above
    m(Ns.int.strMapK(".*").contains(?))("Hello|Hej").get === hehe
    m(Ns.int.strMapK_(".*").contains(?))("Hello|Hej").get === List(3, 4)


    m(Ns.int.intMapK("en|fr")(?))(10 or 20).get === List(
      (2, 20),
      (2, 10),
      (1, 10)
    )
    m(Ns.int.intMapK_("en|fr")(?))(10, 20).get === List(1, 2)
  }


  "One key, multiple values" in new Setup {

    val hi_he = List(
      (2, "Oh, Hi"),
      (1, "Hi there"),
      (3, "Hello")
    )
    val values = Seq(".*Hi.*", ".*He.*")

    m(Ns.int.strMapK("en")(?))(".*Hi.*" or ".*He.*").get === hi_he
    m(Ns.int.strMapK("en")(?))(".*Hi.*", ".*He.*").get === hi_he
    m(Ns.int.strMapK("en")(?))(Seq(".*Hi.*", ".*He.*")).get === hi_he
    m(Ns.int.strMapK("en")(?))(values).get === hi_he
    m(Ns.int.strMapK("en")(?))(".*Hi.*|.*He.*").get === hi_he

    m(Ns.int.strMapK_("en")(?))(".*Hi.*" or ".*He.*").get === List(1,2,3)
    m(Ns.int.strMapK_("en")(?))(".*Hi.*", ".*He.*").get === List(1,2,3)
    m(Ns.int.strMapK_("en")(?))(Seq(".*Hi.*", ".*He.*")).get === List(1,2,3)
    m(Ns.int.strMapK_("en")(?))(values).get === List(1,2,3)
    m(Ns.int.strMapK_("en")(?))(".*Hi.*|.*He.*").get === List(1,2,3)


    // With other types we need to apply multiple Maps if we have multiple
    // identical keys with varying values to lookup
    m(Ns.int.intMapK("en")(?))(10, 30).get === List(
      (3, 30),
      (2, 10),
      (1, 10)
    )
  }
}

