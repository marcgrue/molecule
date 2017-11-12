package molecule.coretests.expression

import molecule.Imports._
import java.util.Date
import java.util.UUID._
import java.net.URI
import datomic.Peer
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class FulltextSearch extends Base {

  "Card one" in new CoreSetup {

    Ns.str insert List("The quick fox jumps", "Ten slow monkeys")

    // Trivial words like "The" not indexed
    Ns.str.contains("The").get === List()
    Ns.str.contains("Ten").get === List("Ten slow monkeys")

    // Only full words counted
    Ns.str.contains("jumps").get === List("The quick fox jumps")
    Ns.str.contains("jump").get === List()

    // Searches are case-insensitive
    Ns.str.contains("Jumps").get === List("The quick fox jumps")
    Ns.str.contains("JuMpS").get === List("The quick fox jumps")

    // Empty spaces ignored
    Ns.str.contains("slow ").get === List("Ten slow monkeys")
    Ns.str.contains(" slow").get === List("Ten slow monkeys")
    Ns.str.contains(" slow ").get === List("Ten slow monkeys")
    Ns.str.contains("  slow  ").get === List("Ten slow monkeys")

    // Words are searched individually - order and spaces ignored
    Ns.str.contains("slow     monkeys").get === List("Ten slow monkeys")
    Ns.str.contains("monkeys slow").get === List("Ten slow monkeys")
    Ns.str.contains("monkeys quick").get === List("Ten slow monkeys", "The quick fox jumps")
    Ns.str.contains("quick monkeys").get === List("Ten slow monkeys", "The quick fox jumps")
  }

  "Card many" in new ManySetup {

    // Searching for text strings in cardinality-many attribute values is rather useless since the
    // coalesed set of values is searched and not the original sets of values
    Ns.strs.contains("c").get === List(Set("c"))

    // What we want is probably rather to group by another attribute to
    // find the cardinality-many sets of values matching the search string:
    Ns.str.strs.get.filter(_._2.contains("c")) === List(("str2", Set("b", "c")))
    // etc...
  }
}