package molecule.coretests.expression

import molecule.api.out4._
import molecule.coretests.util.dsl.coreTest._

class Fulltext extends Base {


  "Card one" in new CoreSetup {

    Ns.str insert List("The quick fox jumps", "Ten slow monkeys")

    // Trivial words like "The" not indexed
    Ns.str.contains("The").get === List()
    Ns.str.contains("Ten").get === List("Ten slow monkeys")

    /*
    Non-indexed words:

    "a", "an", "and", "are", "as", "at", "be", "but", "by",
    "for", "if", "in", "into", "is", "it",
    "no", "not", "of", "on", "or", "such",
    "that", "the", "their", "then", "there", "these",
    "they", "this", "to", "was", "will", "with"

    See https://docs.datomic.com/on-prem/schema.html
    */

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


  "Quoting" in new CoreSetup {

    Ns.int(1).str("""Hi "Ann"""").save

    Ns.str.contains("Hi").get === List("""Hi "Ann"""")
    Ns.str.contains(""""Ann"""").get === List("""Hi "Ann"""")

    val needle1 = "Hi"
    val needle2 = """"Ann""""
    Ns.str.contains(needle1).get === List("""Hi "Ann"""")
    Ns.str.contains(needle2).get === List("""Hi "Ann"""")

    Ns.int.str_.contains("""Hi "Ann"""").get === List(1)
    Ns.int.str_.contains(needle1).get === List(1)
    Ns.int.str_.contains(needle2).get === List(1)
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

    Ns.int.strs_.contains("fox").get === List(1)
    Ns.int.strs_.contains("slow").get === List(1, 2)
    Ns.int.strs_.contains("fox", "slow").get === List(1)
  }
}