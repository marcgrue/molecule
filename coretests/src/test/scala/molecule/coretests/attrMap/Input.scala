package molecule.coretests.attrMap

import molecule.coretests.util.dsl.coreTest._
import molecule.datomic.api.in1_out2._

class Input extends Base {

  "Equality" in new Setup {

    // Input molecule waiting for a key/value pair
    val inputMolecule = m(Ns.int.strMap(?))

    // We then apply a Map with a key/value pair
    inputMolecule.apply(Map("en" -> "Hello")).get === List(
      (3, Map("en" -> "Hello"))
    )

    // We can use a tacit attribute map (to only return the Int value):
    m(Ns.int.strMap_(?)).apply(Map("en" -> "Hello")).get === List(3)

    // Note that text searches for attribute maps are case-sensitive
    m(Ns.int.strMap_(?))(Map("en" -> "hello")).get === List()


    m(Ns.int.intMap(?))(Map("en" -> 10)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10))
    )
    m(Ns.int.intMap_(?))(Map("en" -> 10)).get === List(1, 2)
  }


  "Regex/partial search" in new Setup {

    // Attribute maps allow us to search for a value needle without the key:
    Ns.int.strMap(".*He.*").get === List(
      (1, Map("da" -> "Hejsa")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    // Attribute maps in input molecules though require Maps as needles.
    // So, to search for a regex value we could make the key part accepting anything:
    m(Ns.int.strMap(?))(Map(".*" -> ".*He.*")).get === List(
      (1, Map("da" -> "Hejsa")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )
    m(Ns.int.strMap_(?))(Map(".*" -> ".*He.*")).get === List(1, 3, 4)

    // Retrieving a specific value for any key
    m(Ns.int.strMap(?))(Map(".*" -> "Hello")).get === List(
      (3, Map("en" -> "Hello"))
    )

    // Since other types than String cannot use regexes we can only
    // search for values equal to our needle:
    m(Ns.int.intMap(?))(Map(".*" -> 10)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10))
    )
    m(Ns.int.intMap_(?))(Map(".*" -> 10)).get === List(1, 2)
  }


  "Negation" in new Setup {

    m(Ns.int(2).strMap.!=(?))(Map(".*" -> "Bon giorno")).get === List(
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour"))
    )

    // Negate multiple values
    // OBS: each key/value pair is tried and generates a set of subresults where
    // that pair is not present. But now that we have two pairs, and each subresult
    // contains the other value, they will both be present in the final coalesced set!
    // So we were not expecting both values to be returned:
    m(Ns.int(2).strMap.!=(?))(Map("fr" -> "Bonjour", "it" -> "Bon giorno")).get === List(
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser", "fr" -> "Bonjour", "it" -> "Bon giorno"))
    )
    // Although not negating pair-wise, in most cases this would probably work instead:
    m(Ns.int(2).strMap.!=(?))(Map("fr|it" -> "Bonjour|Bon giorno")).get === List(
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser"))
    )
    m(Ns.int(2).strMap_.!=(?))(Map("fr|it" -> "Bonjour|Bon giorno")).get === List(2)


    m(Ns.int.intMap.!=(?))(Map(".*" -> 30)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10, "fr" -> 20))
    )
    m(Ns.int.intMap_.!=(?))(Map(".*" -> 30)).get === List(1, 2)

    // We can't negate multiple non-string values on input molecules
    // since we can't use regexes on other types than String.
  }


  "Comparison" in new Setup {

    m(Ns.int.strMap.>(?))(Map(".*" -> "Hej")).get === List(
      (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser")),
      (3, Map("en" -> "Hello"))
    )

    m(Ns.int.strMap.>=(?))(Map(".*" -> "Hej")).get === List(
      (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      (2, Map("en" -> "Oh, Hi", "da" -> "Hilser")),
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    m(Ns.int.strMap.<=(?))(Map(".*" -> "Hej")).get === List(
      (2, Map("fr" -> "Bonjour", "it" -> "Bon giorno")),
      (3, Map("da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    m(Ns.int.strMap.<(?))(Map(".*" -> "Hej")).get === List(
      (2, Map("fr" -> "Bonjour", "it" -> "Bon giorno"))
    )

    m(Ns.int.strMap_.>(?))(Map(".*" -> "Hej")).get.sorted === List(1, 2, 3)
    m(Ns.int.strMap_.>=(?))(Map(".*" -> "Hej")).get.sorted === List(1, 2, 3, 4)
    m(Ns.int.strMap_.<=(?))(Map(".*" -> "Hej")).get.sorted === List(2, 3, 4)
    m(Ns.int.strMap_.<(?))(Map(".*" -> "Hej")).get.sorted === List(2)


    // Int

    m(Ns.int.intMap.>(?))(Map(".*" -> 20)).get === List(
      (1, Map("da" -> 30)),
      (2, Map("it" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )
    m(Ns.int.intMap.>=(?))(Map(".*" -> 20)).get === List(
      (1, Map("da" -> 30)),
      (2, Map("fr" -> 20, "it" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )
    m(Ns.int.intMap.<=(?))(Map(".*" -> 20)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10, "fr" -> 20))
    )
    m(Ns.int.intMap.<(?))(Map(".*" -> 20)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10, "da" -> 10))
    )

    m(Ns.int.intMap.>(?))(Map(".*" -> -10)).get === List(
      (1, Map("en" -> 10, "da" -> 30)),
      (2, Map("en" -> 10, "da" -> 10, "fr" -> 20, "it" -> 30)),
      (3, Map("en" -> 30, "da" -> 30)),
      (4, Map("da" -> 30))
    )

    m(Ns.int.intMap_.>(?))(Map(".*" -> 20)).get === List(1, 2, 3, 4)
    m(Ns.int.intMap_.>=(?))(Map(".*" -> 20)).get === List(1, 2, 3, 4)
    m(Ns.int.intMap_.<=(?))(Map(".*" -> 20)).get === List(1, 2)
    m(Ns.int.intMap_.<(?))(Map(".*" -> 20)).get === List(1, 2)


    // Date
    m(Ns.int.dateMap.>(?))(Map(".*" -> date2)).get === List(
      (1, Map("da" -> date3)),
      (2, Map("it" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    m(Ns.int.dateMap.>=(?))(Map(".*" -> date2)).get === List(
      (1, Map("da" -> date3)),
      (2, Map("fr" -> date2, "it" -> date3)),
      (3, Map("en" -> date3, "da" -> date3)),
      (4, Map("da" -> date3))
    )
    m(Ns.int.dateMap.<=(?))(Map(".*" -> date2)).get === List(
      (1, Map("en" -> date1)),
      (2, Map("en" -> date1, "da" -> date1, "fr" -> date2))
    )
    m(Ns.int.dateMap.<(?))(Map(".*" -> date2)).get === List(
      (1, Map("en" -> date1)),
      (2, Map("en" -> date1, "da" -> date1))
    )

    m(Ns.int.dateMap_.>(?))(Map(".*" -> date2)).get === List(1, 2, 3, 4)
    m(Ns.int.dateMap_.>=(?))(Map(".*" -> date2)).get === List(1, 2, 3, 4)
    m(Ns.int.dateMap_.<=(?))(Map(".*" -> date2)).get === List(1, 2)
    m(Ns.int.dateMap_.<(?))(Map(".*" -> date2)).get === List(1, 2)
  }


  "Different keys, multiple values" in new Setup {

    // Retrieve multiple values
    m(Ns.int.strMap(?))(Map("en" -> "Hello", "da" -> "Hej")).get === List(
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )
    // Or tie them together with a regex
    m(Ns.int.strMap(?))(Map(".*" -> "Hello|Hej")).get === List(
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )

    // Using `contains` is redundant since it translates to the same as above
    m(Ns.int.strMap.contains(?))(Map(".*" -> "Hello|Hej")).get === List(
      (3, Map("en" -> "Hello", "da" -> "Hej")),
      (4, Map("da" -> "Hej"))
    )
    m(Ns.int.strMap_.contains(?))(Map(".*" -> "Hello|Hej")).get === List(3, 4)

    // We can also mix varying pairs with regexes
    m(Ns.int.strMap(?))(Map("en" -> ".*Hi.*|.*He.*", "fr" -> "Bo.*")).get === List(
      (1, Map("en" -> "Hi there")),
      (2, Map("fr" -> "Bonjour", "en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello"))
    )


    m(Ns.int.intMap(?))(Map("en" -> 10, "fr" -> 20)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("fr" -> 20, "en" -> 10))
    )
    m(Ns.int.intMap_(?))(Map("en" -> 10, "fr" -> 20)).get === List(1, 2)
  }


  "One key, multiple values" in new Setup {

    val hi_he = List(
      (1, Map("en" -> "Hi there")),
      (2, Map("en" -> "Oh, Hi")),
      (3, Map("en" -> "Hello"))
    )

    // OBS: Can't use one map for same-key pairs since they will silently coalesce!
    // m(Ns.int.strMap(?)).apply(Map("en" -> "Hi", "en" -> "He"))

    // Instead we can supply 2 Maps
    m(Ns.int.strMap(?))(Map("en" -> ".*Hi.*"), Map("en" -> ".*He.*")).get === hi_he

    // or a Seq of Maps (useful if saved in a variable)
    m(Ns.int.strMap(?))(Seq(Map("en" -> ".*Hi.*"), Map("en" -> ".*He.*"))).get === hi_he
    val maps = Seq(Map("en" -> ".*Hi.*"), Map("en" -> ".*He.*"))
    m(Ns.int.strMap(?))(maps).get === hi_he

    // The easiest solution is though to use a regEx
    m(Ns.int.strMap(?))(Map("en" -> ".*Hi.*|.*He.*")).get === hi_he


    // With other types we need to apply multiple Maps if we have multiple
    // identical keys with varying values to lookup
    m(Ns.int.intMap(?))(Map("en" -> 10), Map("en" -> 30)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("en" -> 10)),
      (3, Map("en" -> 30))
    )

    // OBS: Can't use one map for same-key pairs since they will silently coalesce!
    // m(Ns.int.intMap(?))(Map("en" -> 10, "en" -> 30))

    // 3 pairs
    m(Ns.int.intMap(?))(Map("en" -> 10), Map("en" -> 30), Map("fr" -> 20)).get === List(
      (1, Map("en" -> 10)),
      (2, Map("fr" -> 20, "en" -> 10)),
      (3, Map("en" -> 30))
    )
  }
}

