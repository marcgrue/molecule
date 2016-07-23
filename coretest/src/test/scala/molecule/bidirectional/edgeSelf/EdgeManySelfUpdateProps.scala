package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._


class EdgeManySelfUpdateProps extends MoleculeSpec {

  class setup extends Setup {
    val love                    = Quality.name("Love").save.eid
    val List(patience, humor)   = Quality.name.insert("Patience", "Humor").eids
    val List(ann, annBen, _, _) = Person.name("Ann")
      .Knows
      .weight(7)
      .howWeMet("atWork")
      .commonInterests("Food", "Walking", "Travelling")
      .commonLicences("climbing", "flying")
      .commonScores(Seq("golf" -> 7, "baseball" -> 9))
      .coreQuality(love)
      .inCommon(Seq(patience, humor))
      .Person.name("Ben")
      .save.eids
  }

  "base data" in new setup {

    // All edge properties have been saved in both directions
    Person.name.Knows
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Knows
      .InCommon.*(Quality.name)._Knows
      .Person.name
      .get === List(
      ("Ann"
        , 7
        , "atWork"
        , Set("Food", "Walking", "Travelling")
        , Set("climbing", "flying")
        , Map("baseball" -> 9, "golf" -> 7)
        , "Love"
        , List("Patience", "Humor")
        , "Ben"),
      ("Ben"
        , 7
        , "atWork"
        , Set("Food", "Walking", "Travelling")
        , Set("climbing", "flying")
        , Map("baseball" -> 9, "golf" -> 7)
        , "Love"
        , List("Patience", "Humor")
        , "Ann")
    )
  }

  "Card-one" >> {

    "value" in new setup {

      // Updating edge properties from the base entity is not allowed
      (Person(ann).Knows.howWeMet("inSchool").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_edgeComplete]  Can't update edge `Knows` " +
        s"of base entity `Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `Knows(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      Person.name("Ann" or "Ben").Knows.weight.get.sortBy(_._1) === List(
        ("Ann", 7),
        ("Ben", 7)
      )

      // Apply new value
      Knows(annBen).weight(2).update
      Person.name("Ann" or "Ben").Knows.weight.get.sortBy(_._1) === List(
        ("Ann", 2),
        ("Ben", 2)
      )

      // Retract value
      Knows(annBen).weight().update
      Person.name("Ann" or "Ben").Knows.weight.get === List()
    }


    "enum" in new setup {

      // Current howWeMet enum value
      Person.name("Ann" or "Ben").Knows.howWeMet.get.sortBy(_._1) === List(
        ("Ann", "atWork"),
        ("Ben", "atWork")
      )

      // Apply new enum value
      Knows(annBen).howWeMet("throughFriend").update
      Person.name("Ann" or "Ben").Knows.howWeMet.get.sortBy(_._1) === List(
        ("Ann", "throughFriend"),
        ("Ben", "throughFriend")
      )

      // Retract enum value
      Knows(annBen).howWeMet().update
      Person.name("Ann" or "Ben").Knows.howWeMet.get === List()
    }


    "ref" in new setup {

      // Current value
      Person.name("Ann" or "Ben").Knows.CoreQuality.name.get.sortBy(_._1) === List(
        ("Ann", "Love"),
        ("Ben", "Love")
      )

      // We can't update across namespaces
      (Knows(annBen).CoreQuality.name("Compassion").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      Quality(love).name("Compassion").update

      // Same reference, new value
      Person.name("Ann" or "Ben").Knows.CoreQuality.name.get.sortBy(_._1) === List(
        ("Ann", "Compassion"),
        ("Ben", "Compassion")
      )


      // 2. Update reference

      val trust = Quality.name("Trust").save.eid
      Knows(annBen).coreQuality(trust).update

      // New reference/value
      Person.name("Ann" or "Ben").Knows.CoreQuality.name.get.sortBy(_._1) === List(
        ("Ann", "Trust"),
        ("Ben", "Trust")
      )

      // Retract reference
      Knows(annBen).coreQuality().update
      Knows(annBen).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new setup {

      val commonInterestsOf = m(Person.name(?).Knows.commonInterests)

      // Current values
      commonInterestsOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("Food", "Walking", "Travelling")),
        ("Ben", Set("Food", "Walking", "Travelling"))
      )

      // Replace
      Knows(annBen).commonInterests.replace("Food" -> "Cuisine").update
      commonInterestsOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("Cuisine", "Walking", "Travelling")),
        ("Ben", Set("Cuisine", "Walking", "Travelling"))
      )

      // Remove
      Knows(annBen).commonInterests.remove("Travelling").update
      commonInterestsOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("Cuisine", "Walking")),
        ("Ben", Set("Cuisine", "Walking"))
      )

      // Add
      Knows(annBen).commonInterests.add("Meditating").update
      commonInterestsOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("Cuisine", "Walking", "Meditating")),
        ("Ben", Set("Cuisine", "Walking", "Meditating"))
      )

      // Apply new values
      Knows(annBen).commonInterests("Running", "Cycling").update
      commonInterestsOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("Running", "Cycling")),
        ("Ben", Set("Running", "Cycling"))
      )

      // Retract all
      Knows(annBen).commonInterests().update
      commonInterestsOf("Ann" or "Ben").get === List()
    }


    "enums" in new setup {

      val commonLicencesOf = m(Person.name(?).Knows.commonLicences)

      // Current enum values
      commonLicencesOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("climbing", "flying")),
        ("Ben", Set("climbing", "flying"))
      )

      // Replace
      Knows(annBen).commonLicences.replace("flying" -> "diving").update
      commonLicencesOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("climbing", "diving")),
        ("Ben", Set("climbing", "diving"))
      )

      // Remove
      Knows(annBen).commonLicences.remove("climbing").update
      commonLicencesOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("diving")),
        ("Ben", Set("diving"))
      )

      // Add
      Knows(annBen).commonLicences.add("parachuting").update
      commonLicencesOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("diving", "parachuting")),
        ("Ben", Set("diving", "parachuting"))
      )

      // Apply new values
      Knows(annBen).commonLicences("climbing", "flying").update
      commonLicencesOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", Set("climbing", "flying")),
        ("Ben", Set("climbing", "flying"))
      )

      // Retract all
      Knows(annBen).commonLicences().update
      commonLicencesOf.apply("Ann" or "Ben").get === List()
    }


    "refs" in new setup {

      val inCommonOf = m(Person.name(?).Knows.InCommon.*(Quality.name))

      // Current value
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Patience", "Humor")),
        ("Ben", List("Patience", "Humor"))
      )

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      Quality(patience).name("Waiting ability").update
      Quality(humor).name("Funny").update

      // Same references, new value(s)
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Waiting ability", "Funny")),
        ("Ben", List("Waiting ability", "Funny"))
      )


      // 2. Update reference(s)

      val sporty = Quality.name("Sporty").save.eid

      // replace
      Knows(annBen).inCommon.replace(humor -> sporty).update
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Waiting ability", "Sporty")),
        ("Ben", List("Waiting ability", "Sporty"))
      )

      // remove
      Knows(annBen).inCommon.remove(patience).update
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Sporty")),
        ("Ben", List("Sporty"))
      )

      // add
      Knows(annBen).inCommon.add(patience).update
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Waiting ability", "Sporty")),
        ("Ben", List("Waiting ability", "Sporty"))
      )

      // Apply new values
      Knows(annBen).inCommon(sporty, humor).update
      inCommonOf("Ann" or "Ben").get.sortBy(_._1) === List(
        ("Ann", List("Funny", "Sporty")),
        ("Ben", List("Funny", "Sporty"))
      )

      // Retract all references
      Knows(annBen).inCommon().update
      inCommonOf("Ann" or "Ben").get === List()
    }
  }


  "Map" in new setup {

    val commonScoresOf = m(Person.name(?).Knows.commonScores)

    // Current values
    commonScoresOf("Ann" or "Ben").get.sortBy(_._1) === List(
      ("Ann", Map("baseball" -> 9, "golf" -> 7)),
      ("Ben", Map("baseball" -> 9, "golf" -> 7))
    )

    // Replace values by key
    Knows(annBen).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    commonScoresOf("Ann" or "Ben").get.sortBy(_._1) === List(
      ("Ann", Map("baseball" -> 8, "golf" -> 6)),
      ("Ben", Map("baseball" -> 8, "golf" -> 6))
    )

    // Remove by key
    Knows(annBen).commonScores.remove("golf").update
    commonScoresOf("Ann" or "Ben").get.sortBy(_._1) === List(
      ("Ann", Map("baseball" -> 8)),
      ("Ben", Map("baseball" -> 8))
    )

    // Add
    Knows(annBen).commonScores.add("parachuting" -> 4).update
    commonScoresOf("Ann" or "Ben").get.sortBy(_._1) === List(
      ("Ann", Map("baseball" -> 8, "parachuting" -> 4)),
      ("Ben", Map("baseball" -> 8, "parachuting" -> 4))
    )

    // Apply new values (replacing all current values!)
    Knows(annBen).commonScores("volleball" -> 4, "handball" -> 5).update
    commonScoresOf("Ann" or "Ben").get.sortBy(_._1) === List(
      ("Ann", Map("volleball" -> 4, "handball" -> 5)),
      ("Ben", Map("volleball" -> 4, "handball" -> 5))
    )

    // Delete all
    Knows(annBen).commonScores().update
    commonScoresOf("Ann" or "Ben").get === List()
  }
}
