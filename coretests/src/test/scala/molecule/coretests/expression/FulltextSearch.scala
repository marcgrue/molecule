package molecule.coretests.expression

import molecule.api._
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

  "Card many" in new CoreSetup {

    Ns.int.strs insert List(
      (1, Set("The quick fox jumps", "Ten slow monkeys")),
      (2, Set("lorem ipsum", "Going slow"))
    )

    Ns.int.strs.contains("fox").get === List(
      (1, Set("The quick fox jumps", "Ten slow monkeys"))
    )
    Ns.int.strs.contains("slow").get === List(
      (1, Set("The quick fox jumps", "Ten slow monkeys")),
      (2, Set("lorem ipsum", "Going slow"))
    )
    Ns.int.strs.contains("fox", "slow").get === List(
      (1, Set("The quick fox jumps", "Ten slow monkeys"))
    )

    Ns.strs.contains("fox").get === List(Set("The quick fox jumps", "Ten slow monkeys"))
    Ns.strs.contains("slow").get === List(Set("The quick fox jumps", "Ten slow monkeys", "lorem ipsum", "Going slow"))
    Ns.strs.contains("fox", "slow").get === List(Set("The quick fox jumps", "Ten slow monkeys"))

    Ns.int.strs_.contains("fox").get ===        List(1)
    Ns.int.strs_.contains("slow").get ===        List(1, 2)
    Ns.int.strs_.contains("fox", "slow").get === List(1)
  }
}