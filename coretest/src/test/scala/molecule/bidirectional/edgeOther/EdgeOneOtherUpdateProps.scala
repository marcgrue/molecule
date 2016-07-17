package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util._
import org.specs2.specification.Scope

class EdgeOneOtherUpdateProps extends MoleculeSpec {

  class Setup extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(BidirectionalSchema)

    val love                    = living_Quality.name("Love").save.eid
    val List(patience, humor)   = living_Quality.name.insert("Patience", "Humor").eids
    val List(ben, benRex, _, _) = living_Person.name("Ben")
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

    // All edge properties have been inserted in both directions:

    // Person -> Animal
    living_Person.name
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(living_Quality.name)._Favorite
      .Animal.name
      .get === List(
      ("Ben"
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
    living_Animal.name
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(living_Quality.name)._Favorite
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
        , "Ben")
    )
  }


  "Card-one" >> {

    "value" in new Setup {

      // Updating edge properties from the base entity is not allowed
      (living_Person(ben).Favorite.howWeMet("inSchool").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Can't update edge `living_Favorite` " +
        s"of base entity `living_Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `living_Favorite(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))

      // Apply new value
      living_Favorite(benRex).weight(2).update
      living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((2, "Rex"))
      living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((2, "Ben"))

      // Retract value
      living_Favorite(benRex).weight().update
      living_Person.name("Ben").Favorite.weight.get === List()
      living_Animal.name("Rex").Favorite.weight.get === List()
    }


    "enum" in new Setup {

      // Current howWeMet enum value
      living_Person.name_("Ben").Favorite.howWeMet.Animal.name.get === List(("atWork", "Rex"))
      living_Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("atWork", "Ben"))

      // Apply new enum value
      living_Favorite(benRex).howWeMet("throughFriend").update
      living_Person.name_("Ben").Favorite.howWeMet.Animal.name.get === List(("throughFriend", "Rex"))
      living_Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("throughFriend", "Ben"))

      // Retract enum value
      living_Favorite(benRex).howWeMet().update
      living_Person.name("Ben").Favorite.howWeMet.get === List()
      living_Animal.name("Rex").Favorite.howWeMet.get === List()
    }


    "ref" in new Setup {

      // Current value
      living_Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Love", "Rex"))
      living_Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Love", "Ben"))

      // We can't update across namespaces
      (living_Favorite(benRex).CoreQuality.name("Compassion").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `living_Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      living_Quality(love).name("Compassion").update

      // Same reference, new value
      living_Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Compassion", "Rex"))
      living_Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Compassion", "Ben"))


      // 2. Update reference

      val trust = living_Quality.name("Trust").save.eid
      living_Favorite(benRex).coreQuality(trust).update

      // New reference/value
      living_Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Trust", "Rex"))
      living_Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Trust", "Ben"))


      // Retract reference
      living_Favorite(benRex).coreQuality().update
      living_Favorite(benRex).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new Setup {

      // Current values
      living_Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Food", "Travelling", "Walking"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Food", "Travelling", "Walking"), "Ben"))

      // Replace
      living_Favorite(benRex).commonInterests.replace("Food" -> "Cuisine").update
      living_Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Ben"))

      // Remove
      living_Favorite(benRex).commonInterests.remove("Travelling").update
      living_Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine"), "Ben"))

      // Add
      living_Favorite(benRex).commonInterests.add("Meditating").update
      living_Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Ben"))

      // Apply new values
      living_Favorite(benRex).commonInterests("Running", "Cycling").update
      living_Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Running", "Cycling"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Running", "Cycling"), "Ben"))

      // Retract all
      living_Favorite(benRex).commonInterests().update
      living_Person.name_("Ben").Favorite.commonInterests.get === List()
      living_Animal.name_("Rex").Favorite.commonInterests.get === List()
    }


    "enums" in new Setup {

      // Current enum values
      living_Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Replace
      living_Favorite(benRex).commonLicences.replace("flying" -> "diving").update
      living_Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "diving"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "diving"), "Ben"))

      // Remove
      living_Favorite(benRex).commonLicences.remove("climbing").update
      living_Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("diving"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving"), "Ben"))

      // Add
      living_Favorite(benRex).commonLicences.add("parachuting").update
      living_Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("diving", "parachuting"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving", "parachuting"), "Ben"))

      // Apply new values
      living_Favorite(benRex).commonLicences("climbing", "flying").update
      living_Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      living_Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Retract all
      living_Favorite(benRex).commonLicences().update
      living_Person.name_("Ben").Favorite.commonLicences.get === List()
      living_Animal.name_("Rex").Favorite.commonLicences.get === List()
      living_Person.name("Ben" or "Rex").Favorite.commonLicences.get === List()
    }


    "refs" in new Setup {

      // Current value
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Patience", "Humor"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Patience", "Humor"), "Ben"))

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      living_Quality(patience).name("Waiting ability").update
      living_Quality(humor).name("Funny").update

      // Same references, new value(s)
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Funny"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Funny"), "Ben"))


      // 2. Update reference(s)

      val sporty = living_Quality.name("Sporty").save.eid

      // replace
      living_Favorite(benRex).inCommon.replace(humor -> sporty).update
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // remove
      living_Favorite(benRex).inCommon.remove(patience).update
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Sporty"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Sporty"), "Ben"))

      // add
      living_Favorite(benRex).inCommon.add(patience).update
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // Apply new values
      living_Favorite(benRex).inCommon(sporty, humor).update
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List((Seq("Funny", "Sporty"), "Rex"))
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List((Seq("Funny", "Sporty"), "Ben"))

      // Retract all references
      living_Favorite(benRex).inCommon().update
      living_Person.name_("Ben").Favorite.InCommon.*(living_Quality.name)._Favorite.Animal.name.get === List()
      living_Animal.name_("Rex").Favorite.InCommon.*(living_Quality.name)._Favorite.Person.name.get === List()
    }
  }


  "Map" in new Setup {

    // Current values
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Rex"))
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Ben"))

    // Replace values by key
    living_Favorite(benRex).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Rex"))
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Ben"))

    // Remove by key
    living_Favorite(benRex).commonScores.remove("golf").update
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8), "Rex"))
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8), "Ben"))

    // Add
    living_Favorite(benRex).commonScores.add("parachuting" -> 4).update
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Rex"))
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Ben"))

    // Apply new values (replacing all current values!)
    living_Favorite(benRex).commonScores("volleball" -> 4, "handball" -> 5).update
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Rex"))
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Ben"))

    // Delete all
    living_Favorite(benRex).commonScores().update
    living_Person.name_("Ben").Favorite.commonScores.Animal.name.get === List()
    living_Animal.name_("Rex").Favorite.commonScores.Person.name.get === List()
  }
}
