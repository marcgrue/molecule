package molecule.coretests.bidirectionals.edgeOther

import molecule.api.out9._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.ops.exception.VerifyModelException
import molecule.util._

class EdgeOneOtherUpdateProps extends MoleculeSpec {


  class setup extends Setup {
    
    val love                    = Quality.name("Love").save.eid
    val List(patience, humor)   = Quality.name.insert("Patience", "Humor").eids

    val List(ann, annRex, _, _) = Person.name("Ann")
      .Favorite
      .weight(7)
      .howWeMet("atWork")
      .commonInterests("Food", "Walking", "Travelling")
      .commonLicences("climbing", "flying")
      .commonScores(Seq("golf" -> 7, "baseball" -> 9))
      .coreQuality(love)
      .inCommon(Seq(patience, humor))
      .Animal.name("Rex")
      .save.eids
  }

  "base data" in new setup {

    // All edge properties have been inserted in both directions:

    // Person -> Animal
    Person.name
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(Quality.name)._Favorite
      .Animal.name
      .get === List(
      ("Ann"
        , 7
        , "atWork"
        , Set("Food", "Travelling", "Walking")
        , Set("climbing", "flying")
        , Map("baseball" -> 9, "golf" -> 7)
        , "Love"
        , List("Patience", "Humor")
        , "Rex")
    )

    // Animal -> Person
    Animal.name
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(Quality.name)._Favorite
      .Person.name
      .get === List(
      ("Rex"
        , 7
        , "atWork"
        , Set("Food", "Travelling", "Walking")
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
      (Person(ann).Favorite.howWeMet("inSchool").update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_edgeComplete]  Can't update edge `Favorite` " +
        s"of base entity `Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `Favorite(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      Person.name_("Ann").Favorite.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ann"))

      // Apply new value
      Favorite(annRex).weight(2).update
      Person.name_("Ann").Favorite.weight.Animal.name.get === List((2, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((2, "Ann"))

      // Retract value
      Favorite(annRex).weight().update
      Person.name("Ann").Favorite.weight.get === List()
      Animal.name("Rex").Favorite.weight.get === List()
    }


    "enum" in new setup {

      // Current howWeMet enum value
      Person.name_("Ann").Favorite.howWeMet.Animal.name.get === List(("atWork", "Rex"))
      Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("atWork", "Ann"))

      // Apply new enum value
      Favorite(annRex).howWeMet("throughFriend").update
      Person.name_("Ann").Favorite.howWeMet.Animal.name.get === List(("throughFriend", "Rex"))
      Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("throughFriend", "Ann"))

      // Retract enum value
      Favorite(annRex).howWeMet().update
      Person.name("Ann").Favorite.howWeMet.get === List()
      Animal.name("Rex").Favorite.howWeMet.get === List()
    }


    "ref" in new setup {

      // Current value
      Person.name_("Ann").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Love", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Love", "Ann"))

      // We can't update across namespaces
      (Favorite(annRex).CoreQuality.name("Compassion").update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_onlyOneNs]  Update molecules can't span multiple namespaces like `Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      Quality(love).name("Compassion").update

      // Same reference, new value
      Person.name_("Ann").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Compassion", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Compassion", "Ann"))


      // 2. Update reference

      val trust = Quality.name("Trust").save.eid
      Favorite(annRex).coreQuality(trust).update

      // New reference/value
      Person.name_("Ann").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Trust", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Trust", "Ann"))


      // Retract reference
      Favorite(annRex).coreQuality().update
      Favorite(annRex).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new setup {

      // Current values
      Person.name_("Ann").Favorite.commonInterests.Animal.name.get === List((Set("Food", "Travelling", "Walking"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Food", "Travelling", "Walking"), "Ann"))

      // Replace
      Favorite(annRex).commonInterests.replace("Food" -> "Cuisine").update
      Person.name_("Ann").Favorite.commonInterests.Animal.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Ann"))

      // Remove
      Favorite(annRex).commonInterests.retract("Travelling").update
      Person.name_("Ann").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine"), "Ann"))

      // Add
      Favorite(annRex).commonInterests.assert("Meditating").update
      Person.name_("Ann").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Ann"))

      // Apply new values
      Favorite(annRex).commonInterests("Running", "Cycling").update
      Person.name_("Ann").Favorite.commonInterests.Animal.name.get === List((Set("Running", "Cycling"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Running", "Cycling"), "Ann"))

      // Retract all
      Favorite(annRex).commonInterests().update
      Person.name_("Ann").Favorite.commonInterests.get === List()
      Animal.name_("Rex").Favorite.commonInterests.get === List()
    }


    "enums" in new setup {

      // Current enum values
      Person.name_("Ann").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ann"))

      // Replace
      Favorite(annRex).commonLicences.replace("flying" -> "diving").update
      Person.name_("Ann").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "diving"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "diving"), "Ann"))

      // Remove
      Favorite(annRex).commonLicences.retract("climbing").update
      Person.name_("Ann").Favorite.commonLicences.Animal.name.get === List((Set("diving"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving"), "Ann"))

      // Add
      Favorite(annRex).commonLicences.assert("parachuting").update
      Person.name_("Ann").Favorite.commonLicences.Animal.name.get === List((Set("diving", "parachuting"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving", "parachuting"), "Ann"))

      // Apply new values
      Favorite(annRex).commonLicences("climbing", "flying").update
      Person.name_("Ann").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ann"))

      // Retract all
      Favorite(annRex).commonLicences().update
      Person.name_("Ann").Favorite.commonLicences.get === List()
      Animal.name_("Rex").Favorite.commonLicences.get === List()
      Person.name("Ann" or "Rex").Favorite.commonLicences.get === List()
    }


    "refs" in new setup {

      // Current value
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Patience", "Humor"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Patience", "Humor"), "Ann"))

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      Quality(patience).name("Waiting ability").update
      Quality(humor).name("Funny").update

      // Same references, new value(s)
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Funny"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Funny"), "Ann"))


      // 2. Update reference(s)

      val sporty = Quality.name("Sporty").save.eid

      // replace
      Favorite(annRex).inCommon.replace(humor -> sporty).update
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ann"))

      // remove
      Favorite(annRex).inCommon.retract(patience).update
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Sporty"), "Ann"))

      // add
      Favorite(annRex).inCommon.assert(patience).update
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ann"))

      // Apply new values
      Favorite(annRex).inCommon(sporty, humor).update
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Funny", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Funny", "Sporty"), "Ann"))

      // Retract all references
      Favorite(annRex).inCommon().update
      Person.name_("Ann").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List()
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List()
    }
  }


  "Map" in new setup {

    // Current values
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Ann"))

    // Replace values by key
    Favorite(annRex).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Ann"))

    // Remove by key
    Favorite(annRex).commonScores.retract("golf").update
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8), "Ann"))

    // Add
    Favorite(annRex).commonScores.assert("parachuting" -> 4).update
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Ann"))

    // Apply new values (replacing all current values!)
    Favorite(annRex).commonScores("volleball" -> 4, "handball" -> 5).update
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Ann"))

    // Delete all
    Favorite(annRex).commonScores().update
    Person.name_("Ann").Favorite.commonScores.Animal.name.get === List()
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List()
  }
}
