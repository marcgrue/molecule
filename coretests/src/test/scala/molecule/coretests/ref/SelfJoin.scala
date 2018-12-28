package molecule.coretests.ref

import molecule.api.out6._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class SelfJoin extends CoreSpec {


  class Setup extends CoreSetup {

    // age - name - beverage - rating
    m(Ns.int.str.Refs1 * Ref1.str1.int1) insert List(
      (23, "Joe", List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
      (25, "Ben", List(("Coffee", 2), ("Tea", 3))),
      (23, "Liz", List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))
  }


  "AND, unify attributes" in new Setup {

    // Normally we ask for values accross attributes like
    // attr1 AND attr2 AND etc as in
    // age==23 AND name AND rating==Pepsi
    Ns.int_(23).str.Refs1.str1_("Pepsi").get === List("Liz", "Joe")

    // But when we need to compare values of the same attribute
    // across entities we need self-joins.

    // Here's an example of a self-join where we take pairs of entities
    // where one is 23 years old and the other 25 years old and then see
    // which of those pairs have a shared preferred beverage. We say that
    // we "unify" by the beverage attribute value (Refs1.str1) - the values
    // that the two entities have in common.

    // What beverages do pairs of 23- AND 25-year-olds like in common?
    // (unifying on Refs1.str1)
    Ns.int_(23 and 25).Refs1.str1.get === List("Coffee", "Tea")

    // Does 23- and 25-years-old have some common beverage ratings?
    // (unifying on Refs1.int1)
    Ns.int_(23 and 25).Refs1.int1.get === List(2, 3)

    // Any 23- and 25-year-olds with the same name? (no)
    // (unifying on Ns.str)
    Ns.int_(23 and 25).str.get === List()


    // Which beverages do Joe AND Liz both like?
    // (unifying on Refs1.str1)
    Ns.str_("Joe" and "Liz").Refs1.str1.get === List("Pepsi", "Coffee")

    // Do Joe AND Liz have some common ratings?
    // (unifying on Refs1.int1)
    Ns.str_("Joe" and "Liz").Refs1.int1.get === List(3)

    // Do Joe AND Liz have a shared age?
    // (unifying on Ns.int)
    Ns.str_("Joe" and "Liz").int.get === List(23)


    // Who likes both Coffee AND Tea?
    // (unifying on Ns.str)
    Ns.str.Refs1.str1_("Coffee" and "Tea").get === List("Ben", "Liz")

    // What ages have those who like both Coffe and Tea?
    // (unifying on Ns.int)
    Ns.int.Refs1.str1_("Coffee" and "Tea").get === List(23, 25)

    // What shared ratings do Coffee and Tea have?
    // (unifying on Ref1.int1)
    Ref1.str1_("Coffee" and "Tea").int1.get === List(3)


    // Who rated both 2 AND 3?
    // (unifying on Ns.str)
    Ns.str.Refs1.int1_(2 and 3).get === List("Ben", "Joe")

    // What ages have those who rated both 2 AND 3?
    // (unifying on Ns.int)
    Ns.int.Refs1.int1_(2 and 3).get === List(23, 25)

    // Which beverages are rated 2 AND 3?
    // (unifying on Refs1.str1)
    Ref1.int1_(2 and 3).str1.get === List("Coffee")


    // Unifying by 2 attributes

    // Which 23- AND 25-year-olds with the same name like the same beverage? (none)
    // (unifying on Ns.str AND Refs1.str1)
    Ns.int_(23 and 25).str.Refs1.str1.get === List()

    // Do Joe and Liz share age and beverage preferences? (yes)
    // (unifying on Ns.int AND Refs1.str1)
    Ns.int.str_("Joe" and "Liz").Refs1.str1.get === List(
      (23, "Coffee"),
      (23, "Pepsi"))


    // Multiple ANDs

    Ns.str_("Joe" and "Ben" and "Liz").Refs1.str1.get === List("Coffee")
  }


  "AND, unify attribute maps" in new CoreSetup {

    // age - name(in various languages) - beverage - rating
    m(Ns.int.strMap.Refs1 * Ref1.str1.int1) insert List(
      (23, Map("en" -> "Joe", "da" -> "Jonas"), List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
      (25, Map("en" -> "Ben", "es" -> "Benito"), List(("Coffee", 2), ("Tea", 3))),
      (23, Map("en" -> "Liz", "da" -> "Lis"), List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))


    // What beverages do 23- AND 25-year-olds like in common?
    // (unifying on Refs1.str1)
    Ns.int_(23 and 25).Refs1.str1.get === List("Coffee", "Tea")

    // Does 23- and 25-years-old have some common beverage ratings?
    // (unifying on Refs1.int1)
    Ns.int_(23 and 25).Refs1.int1.get === List(2, 3)

    // Any 23- and 25-year-olds with the same name? (no)
    // (unifying on Ns.strMap)
    Ns.int_(23 and 25).strMap.get === List()


    // Which beverages do Joe AND Liz both like?
    // (unifying on Refs1.str1)
    // Given the names are saved in an attribute map we can retrieve
    // all combinations of names in different languages.
    // Note that this `and` notation is to only create a self-join
    // - not to retrieve multiple values from one strMap!
    Ns.strMap_("Joe" and "Liz").Refs1.str1.get === List("Pepsi", "Coffee")
    Ns.strMap_("Joe" and "Lis").Refs1.str1.get === List("Pepsi", "Coffee")
    Ns.strMap_("Jonas" and "Liz").Refs1.str1.get === List("Pepsi", "Coffee")
    Ns.strMap_("Jonas" and "Lis").Refs1.str1.get === List("Pepsi", "Coffee")

    // Do Joe AND Liz have some common ratings?
    // (unifying on Refs1.int1)
    Ns.strMap_("Joe" and "Liz").Refs1.int1.get === List(3)
    Ns.strMap_("Joe" and "Lis").Refs1.int1.get === List(3)
    Ns.strMap_("Jonas" and "Liz").Refs1.int1.get === List(3)
    Ns.strMap_("Jonas" and "Lis").Refs1.int1.get === List(3)

    // Do Joe AND Liz have a shared age?
    // (unifying on Ns.int)
    Ns.strMap_("Joe" and "Liz").int.get === List(23)
    Ns.strMap_("Joe" and "Lis").int.get === List(23)
    Ns.strMap_("Jonas" and "Liz").int.get === List(23)
    Ns.strMap_("Jonas" and "Lis").int.get === List(23)


    // Who likes both Coffee AND Tea?
    // (unifying on Ns.strMap)
    Ns.strMap.Refs1.str1_("Coffee" and "Tea").get === List(
      Map(
        "en" -> "Liz",
        "da" -> "Lis",
        "es" -> "Benito"
      )
    )
    // Since the merged Map of results contains multiple pairs
    // with the same key, the pairs coalesce to a smaller set:
    Map(
      "en" -> "Ben", // Same key - this one went
      "en" -> "Liz", // Same key
      "da" -> "Lis",
      "es" -> "Benito"
    )
    // Instead we could unify also by the entity id and then filter it out
    // to get a more useful result:
    Ns.e.strMap.Refs1.str1_("Coffee" and "Tea").get.map(_._2) === List(
      Map("en" -> "Ben", "es" -> "Benito"),
      Map("en" -> "Liz", "da" -> "Lis")
    )

    // What ages have those who like both Coffe and Tea?
    // (unifying on Ns.int)
    Ns.int.Refs1.str1_("Coffee" and "Tea").get === List(23, 25)

    // What shared ratings do Coffee and Tea have?
    // (unifying on Ref1.int1)
    Ref1.str1_("Coffee" and "Tea").int1.get === List(3)


    // Who rated both 2 AND 3?
    // (unifying on Ns.strMap)
    // Using entity id againg to get uncoalesced strMap values
    Ns.e.strMap.Refs1.int1_(2 and 3).get.map(_._2) === List(
      Map("en" -> "Joe", "da" -> "Jonas"),
      Map("en" -> "Ben", "es" -> "Benito")
    )

    // What ages have those who rated both 2 AND 3?
    // (unifying on Ns.int)
    Ns.int.Refs1.int1_(2 and 3).get === List(23, 25)

    // Which beverages are rated 2 AND 3?
    // (unifying on Refs1.str1)
    Ref1.int1_(2 and 3).str1.get === List("Coffee")
  }


  "AND, unify keyed attribute maps" in new CoreSetup {

    // age - name(in various languages) - beverage - rating
    m(Ns.int.strMap.Refs1 * Ref1.str1.int1) insert List(
      (23, Map("en" -> "Joe", "da" -> "Jonas"), List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
      (25, Map("en" -> "Ben", "es" -> "Benito"), List(("Coffee", 2), ("Tea", 3))),
      (23, Map("en" -> "Liz", "da" -> "Lis"), List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))

    // Who likes both Coffee AND Tea?
    // Unifying on Ns.strMapK("en")
    Ns.strMapK("en").Refs1.str1_("Coffee" and "Tea").get === List("Ben", "Liz")

    // Who rated both 2 AND 3?
    // Unifying on Ns.strMapK("en")
    Ns.strMapK("en").Refs1.int1_(2 and 3).get === List("Ben", "Joe")
  }


  "Explicit self-join" in new Setup {

    // All the examples above use the AND notation to construct
    // simple self-joins. Any of them could be re-written to use
    // a more powerful and precise Self-notation:

    Ns.int_(23 and 25).Refs1.str1.get === List("Coffee", "Tea")
    // ..can be re-written to:
    Ns.int_(23).Refs1.str1._Ns.Self
      .int_(25).Refs1.str1_(unify).get === List("Coffee", "Tea")

    // Let's walk through that one...

    // First we ask for a tacit age of 23 being asserted with one person (entity).
    // After asking for the beverage value (Refs1.str1) of the first person we
    // "go back" with `_Ns` to the initial namespace `Ns` and then say that we
    // want to make a self-join with `Self` to start defining another person/entity.
    // We want the other person to be 25 years old.
    // When we define the beverage value for the other person we tell molecule
    // to "unify" that value with the equivalent beverage value of the
    // first person.

    // This second notation gives us freedom to fetch more values that
    // shouldn't be unified. Say for instance that we want to know the names
    // of 23-/25-year-olds sharing a beverage preference:

    Ns.int_(23).str.Refs1.str1._Ns.Self
      .int_(25).str.Refs1.str1_(unify).get.sorted === List(
      ("Joe", "Coffee", "Ben"),
      ("Liz", "Coffee", "Ben"),
      ("Liz", "Tea", "Ben")
    )
    // Now we also fetch the name of beverage (`str`) which is not being unified between the two entities.

    // Let's add the ratings too
    Ns.int_(23).str.Refs1.int1.str1._Ns.Self
      .int_(25).str.Refs1.str1_(unify).int1.get.sorted === List(
      ("Joe", 3, "Coffee", "Ben", 2),
      ("Liz", 1, "Coffee", "Ben", 2),
      ("Liz", 3, "Tea", "Ben", 3)
    )

    // We can arrange the attributes in the previous molecule in other orders too: (internal sanity checks)
    Ns.int_(23).str.Refs1.int1.str1._Ns.Self
      .int_(25).str.Refs1.int1.str1_(unify).get.sorted === List(
      ("Joe", 3, "Coffee", "Ben", 2),
      ("Liz", 1, "Coffee", "Ben", 2),
      ("Liz", 3, "Tea", "Ben", 3)
    )
    // or
    Ns.int_(23).str.Refs1.str1.int1._Ns.Self
      .int_(25).str.Refs1.str1_(unify).int1.get.sorted === List(
      ("Joe", "Coffee", 3, "Ben", 2),
      ("Liz", "Coffee", 1, "Ben", 2),
      ("Liz", "Tea", 3, "Ben", 3)
    )
    // or
    Ns.int_(23).str.Refs1.str1.int1._Ns.Self
      .int_(25).str.Refs1.int1.str1_(unify).get.sorted === List(
      ("Joe", "Coffee", 3, "Ben", 2),
      ("Liz", "Coffee", 1, "Ben", 2),
      ("Liz", "Tea", 3, "Ben", 3)
    )

    // Only higher rated beverages
    Ns.int_(23).str.Refs1.int1.>(1).str1._Ns.Self
      .int_(25).str.Refs1.int1.>(1).str1_(unify).get.sorted === List(
      ("Joe", 3, "Coffee", "Ben", 2),
      ("Liz", 3, "Tea", "Ben", 3)
    )

    // Only highest rated beverages
    Ns.int_(23).str.Refs1.int1(3).str1._Ns.Self
      .int_(25).str.Refs1.int1(3).str1_(unify).get.sorted === List(
      ("Liz", 3, "Tea", "Ben", 3)
    )

    // Common beverage of 23-year-old with weak preference and
    // 25-year-old with good preference
    Ns.int_(23).str.Refs1.int1(1).str1._Ns.Self
      .int_(25).str.Refs1.int1(2).str1_(unify).get.sorted === List(
      ("Liz", 1, "Coffee", "Ben", 2)
    )

    // Any 23- and 25-year-olds wanting to drink tea together?
    Ns.int_(23).str.Refs1.str1_("Tea")._Ns.Self
      .int_(25).str.Refs1.str1_("Tea").get === List(("Liz", "Ben"))

    // Any 23-year old Tea drinker and a 25-year-old Coffee drinker?
    Ns.int_(23).str.Refs1.str1_("Tea")._Ns.Self
      .int_(25).str.Refs1.str1_("Coffee").get === List(("Liz", "Ben"))

    // Any pair of young persons drinking respectively Tea and Coffee?
    Ns.int_.<(24).str.Refs1.str1_("Tea")._Ns.Self
      .int_.<(24).str.Refs1.str1_("Coffee").get === List(
      ("Liz", "Joe"),
      ("Liz", "Liz")
    )

    // Since Liz is under 24 and drinks both Tea and Coffee she
    // shows up as two persons (one drinking Tea, the other Coffee).
    // We can filter the result to only get different persons:
    Ns.e.int_.<(24).str.Refs1.str1_("Tea")._Ns.Self
      .e.int_.<(24).str.Refs1.str1_("Coffee").get
      .filter(r => r._1 != r._3).map(r => (r._2, r._4)) === List(
      ("Liz", "Joe")
    )

    // Unifying attributes should be tacit
    // Grab the value from the first attribute that it unifies with (if needed)

    expectCompileError(
      "m(Ns.int_(23).Refs1.str1._Ns.Self.int_(25).Refs1.str1(unify))",
      "molecule.transform.exception.Dsl2ModelException: Can only unify on tacit attributes. Please add underscore to attribute: `str1_(unify)`")
  }


  "Multiple explicit self-joins" in new Setup {

    Ns.str_("Joe" and "Ben" and "Liz").Refs1.str1.get === List("Coffee")

    // Beverages liked by all 3 different people
    Ns.str_("Joe").Refs1.str1._Ns.Self
      .str_("Ben").Refs1.str1_(unify)._Ns.Self
      .str_("Liz").Refs1.str1_(unify).get === List("Coffee")
  }
}