package molecule.coretests.bidirectionals.edgeOther

import molecule._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherUpdateProps extends MoleculeSpec {

  class setup extends Setup {
    
    val love                  = Quality.name("Love").save.eid
    val List(patience, humor) = Quality.name.insert("Patience", "Humor").eids
    val rex                   = Animal.name.insert("Rex").eid

    val List(ann, annRex, rexAnn) = Person.name("Ann") // New entity
      .CloseTo
      .weight(7)
      .howWeMet("inSchool")
      .commonInterests("Food", "Walking", "Travelling")
      .commonLicences("climbing", "flying")
      .commonScores(Seq("golf" -> 7, "baseball" -> 9))
      .coreQuality(love)
      .inCommon(Seq(patience, humor))
      .animal(rex) // Saving reference to existing Person entity
      .save eids
  }

  "base data" in new setup {

    // All edge properties have been inserted in both directions:

    // Person -> Animal
    Person.name
      .CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(Quality.name)._CloseTo
      .Animal.name
      .get === List(
      ("Ann"
        , 7
        , "inSchool"
        , Set("Food", "Walking", "Travelling")
        , Set("climbing", "flying")
        , Map("baseball" -> 9, "golf" -> 7)
        , "Love"
        , List("Patience", "Humor")
        , "Rex")
    )

    // Animal -> Person
    Animal.name
      .CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(Quality.name)._CloseTo
      .Person.name
      .get === List(
      ("Rex"
        , 7
        , "inSchool"
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
      (Person(ann).CloseTo.howWeMet("inSchool").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_edgeComplete]  Can't update edge `CloseTo` " +
        s"of base entity `Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `CloseTo(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      Person.name_("Ann").CloseTo.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ann"))

      // Apply new value
      CloseTo(annRex).weight(2).update
      Person.name_("Ann").CloseTo.weight.Animal.name.get === List((2, "Rex"))
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List((2, "Ann"))

      // Retract value
      CloseTo(annRex).weight().update
      Person.name_("Ann").CloseTo.weight.Animal.name.get === List()
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    }


    "enum" in new setup {

      // Current howWeMet enum value
      Person.name_("Ann").CloseTo.howWeMet.Animal.name.get === List(("inSchool", "Rex"))
      Animal.name_("Rex").CloseTo.howWeMet.Person.name.get === List(("inSchool", "Ann"))

      // Apply new enum value
      CloseTo(annRex).howWeMet("throughFriend").update
      Person.name_("Ann").CloseTo.howWeMet.Animal.name.get === List(("throughFriend", "Rex"))
      Animal.name_("Rex").CloseTo.howWeMet.Person.name.get === List(("throughFriend", "Ann"))

      // Retract enum value
      CloseTo(annRex).howWeMet().update
      Person.name("Ann").CloseTo.howWeMet.get === List()
      Animal.name("Rex").CloseTo.howWeMet.get === List()
    }


    "ref" in new setup {

      // Current value
      Person.name_("Ann").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Love", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Love", "Ann"))

      // We can't update across namespaces
      (CloseTo(annRex).CoreQuality.name("Compassion").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      Quality(love).name("Compassion").update

      // Same reference, new value
      Person.name_("Ann").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Compassion", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Compassion", "Ann"))


      // 2. Update reference

      val trust = Quality.name("Trust").save.eid
      CloseTo(annRex).coreQuality(trust).update

      // New reference/value
      Person.name_("Ann").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Trust", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Trust", "Ann"))


      // Retract reference
      CloseTo(annRex).coreQuality().update
      CloseTo(annRex).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new setup {

      // Current values
      Person.name_("Ann").CloseTo.commonInterests.Animal.name.get === List((Set("Food", "Travelling", "Walking"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Food", "Travelling", "Walking"), "Ann"))

      // Replace
      CloseTo(annRex).commonInterests.replace("Food" -> "Cuisine").update
      Person.name_("Ann").CloseTo.commonInterests.Animal.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Ann"))

      // Remove
      CloseTo(annRex).commonInterests.remove("Travelling").update
      Person.name_("Ann").CloseTo.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Walking", "Cuisine"), "Ann"))

      // Add
      CloseTo(annRex).commonInterests.add("Meditating").update
      Person.name_("Ann").CloseTo.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Ann"))

      // Apply new values
      CloseTo(annRex).commonInterests("Running", "Cycling").update
      Person.name_("Ann").CloseTo.commonInterests.Animal.name.get === List((Set("Running", "Cycling"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Running", "Cycling"), "Ann"))

      // Retract all
      CloseTo(annRex).commonInterests().update
      Person.name_("Ann").CloseTo.commonInterests.get === List()
      Animal.name_("Rex").CloseTo.commonInterests.get === List()
    }


    "enums" in new setup {

      // Current enum values
      Person.name_("Ann").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ann"))

      // Replace
      CloseTo(annRex).commonLicences.replace("flying" -> "diving").update
      Person.name_("Ann").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "diving"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "diving"), "Ann"))

      // Remove
      CloseTo(annRex).commonLicences.remove("climbing").update
      Person.name_("Ann").CloseTo.commonLicences.Animal.name.get === List((Set("diving"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("diving"), "Ann"))

      // Add
      CloseTo(annRex).commonLicences.add("parachuting").update
      Person.name_("Ann").CloseTo.commonLicences.Animal.name.get === List((Set("diving", "parachuting"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("diving", "parachuting"), "Ann"))

      // Apply new values
      CloseTo(annRex).commonLicences("climbing", "flying").update
      Person.name_("Ann").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ann"))

      // Retract all
      CloseTo(annRex).commonLicences().update
      Person.name_("Ann").CloseTo.commonLicences.get === List()
      Animal.name_("Rex").CloseTo.commonLicences.get === List()
      Person.name("Ann" or "Rex").CloseTo.commonLicences.get === List()
    }


    "refs" in new setup {

      // Current value
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Patience", "Humor"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Patience", "Humor"), "Ann"))

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      Quality(patience).name("Waiting ability").update
      Quality(humor).name("Funny").update

      // Same references, new value(s)
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Funny"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Funny"), "Ann"))


      // 2. Update reference(s)

      val sporty = Quality.name("Sporty").save.eid

      // replace
      CloseTo(annRex).inCommon.replace(humor -> sporty).update
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ann"))

      // remove
      CloseTo(annRex).inCommon.remove(patience).update
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Sporty"), "Ann"))

      // add
      CloseTo(annRex).inCommon.add(patience).update
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ann"))

      // Apply new values
      CloseTo(annRex).inCommon(sporty, humor).update
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Funny", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Funny", "Sporty"), "Ann"))

      // Retract all references
      CloseTo(annRex).inCommon().update
      Person.name_("Ann").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List()
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List()
    }
  }


  "Map" in new setup {

    // Current values
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Ann"))

    // Replace values by key
    CloseTo(annRex).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Ann"))

    // Remove by key
    CloseTo(annRex).commonScores.remove("golf").update
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8), "Ann"))

    // Add
    CloseTo(annRex).commonScores.add("parachuting" -> 4).update
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Ann"))

    // Apply new values (replacing all current values!)
    CloseTo(annRex).commonScores("volleball" -> 4, "handball" -> 5).update
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Ann"))

    // Delete all
    CloseTo(annRex).commonScores().update
    Person.name_("Ann").CloseTo.commonScores.Animal.name.get === List()
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List()
  }
}
