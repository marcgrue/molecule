package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util._
import org.specs2.specification.Scope

class EdgeOneOtherUpdateProps extends MoleculeSpec {

  class Setup extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(BidirectionalSchema)

    val love                    = Quality.name("Love").save.eid
    val List(patience, humor)   = Quality.name.insert("Patience", "Humor").eids
    val List(ben, benRex, _, _) = Person.name("Ben")
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
        , "Ben")
    )
  }


  "Card-one" >> {

    "value" in new Setup {

      // Updating edge properties from the base entity is not allowed
      (Person(ben).Favorite.howWeMet("inSchool").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Can't update edge `Favorite` " +
        s"of base entity `Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `Favorite(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))

      // Apply new value
      Favorite(benRex).weight(2).update
      Person.name_("Ben").Favorite.weight.Animal.name.get === List((2, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((2, "Ben"))

      // Retract value
      Favorite(benRex).weight().update
      Person.name("Ben").Favorite.weight.get === List()
      Animal.name("Rex").Favorite.weight.get === List()
    }


    "enum" in new Setup {

      // Current howWeMet enum value
      Person.name_("Ben").Favorite.howWeMet.Animal.name.get === List(("atWork", "Rex"))
      Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("atWork", "Ben"))

      // Apply new enum value
      Favorite(benRex).howWeMet("throughFriend").update
      Person.name_("Ben").Favorite.howWeMet.Animal.name.get === List(("throughFriend", "Rex"))
      Animal.name_("Rex").Favorite.howWeMet.Person.name.get === List(("throughFriend", "Ben"))

      // Retract enum value
      Favorite(benRex).howWeMet().update
      Person.name("Ben").Favorite.howWeMet.get === List()
      Animal.name("Rex").Favorite.howWeMet.get === List()
    }


    "ref" in new Setup {

      // Current value
      Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Love", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Love", "Ben"))

      // We can't update across namespaces
      (Favorite(benRex).CoreQuality.name("Compassion").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      Quality(love).name("Compassion").update

      // Same reference, new value
      Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Compassion", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Compassion", "Ben"))


      // 2. Update reference

      val trust = Quality.name("Trust").save.eid
      Favorite(benRex).coreQuality(trust).update

      // New reference/value
      Person.name_("Ben").Favorite.CoreQuality.name._Favorite.Animal.name.get === List(("Trust", "Rex"))
      Animal.name_("Rex").Favorite.CoreQuality.name._Favorite.Person.name.get === List(("Trust", "Ben"))


      // Retract reference
      Favorite(benRex).coreQuality().update
      Favorite(benRex).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new Setup {

      // Current values
      Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Food", "Travelling", "Walking"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Food", "Travelling", "Walking"), "Ben"))

      // Replace
      Favorite(benRex).commonInterests.replace("Food" -> "Cuisine").update
      Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Ben"))

      // Remove
      Favorite(benRex).commonInterests.remove("Travelling").update
      Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine"), "Ben"))

      // Add
      Favorite(benRex).commonInterests.add("Meditating").update
      Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Ben"))

      // Apply new values
      Favorite(benRex).commonInterests("Running", "Cycling").update
      Person.name_("Ben").Favorite.commonInterests.Animal.name.get === List((Set("Running", "Cycling"), "Rex"))
      Animal.name_("Rex").Favorite.commonInterests.Person.name.get === List((Set("Running", "Cycling"), "Ben"))

      // Retract all
      Favorite(benRex).commonInterests().update
      Person.name_("Ben").Favorite.commonInterests.get === List()
      Animal.name_("Rex").Favorite.commonInterests.get === List()
    }


    "enums" in new Setup {

      // Current enum values
      Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Replace
      Favorite(benRex).commonLicences.replace("flying" -> "diving").update
      Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "diving"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "diving"), "Ben"))

      // Remove
      Favorite(benRex).commonLicences.remove("climbing").update
      Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("diving"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving"), "Ben"))

      // Add
      Favorite(benRex).commonLicences.add("parachuting").update
      Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("diving", "parachuting"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("diving", "parachuting"), "Ben"))

      // Apply new values
      Favorite(benRex).commonLicences("climbing", "flying").update
      Person.name_("Ben").Favorite.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").Favorite.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Retract all
      Favorite(benRex).commonLicences().update
      Person.name_("Ben").Favorite.commonLicences.get === List()
      Animal.name_("Rex").Favorite.commonLicences.get === List()
      Person.name("Ben" or "Rex").Favorite.commonLicences.get === List()
    }


    "refs" in new Setup {

      // Current value
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Patience", "Humor"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Patience", "Humor"), "Ben"))

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      Quality(patience).name("Waiting ability").update
      Quality(humor).name("Funny").update

      // Same references, new value(s)
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Funny"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Funny"), "Ben"))


      // 2. Update reference(s)

      val sporty = Quality.name("Sporty").save.eid

      // replace
      Favorite(benRex).inCommon.replace(humor -> sporty).update
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // remove
      Favorite(benRex).inCommon.remove(patience).update
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Sporty"), "Ben"))

      // add
      Favorite(benRex).inCommon.add(patience).update
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // Apply new values
      Favorite(benRex).inCommon(sporty, humor).update
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List((Seq("Funny", "Sporty"), "Rex"))
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List((Seq("Funny", "Sporty"), "Ben"))

      // Retract all references
      Favorite(benRex).inCommon().update
      Person.name_("Ben").Favorite.InCommon.*(Quality.name)._Favorite.Animal.name.get === List()
      Animal.name_("Rex").Favorite.InCommon.*(Quality.name)._Favorite.Person.name.get === List()
    }
  }


  "Map" in new Setup {

    // Current values
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Ben"))

    // Replace values by key
    Favorite(benRex).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Ben"))

    // Remove by key
    Favorite(benRex).commonScores.remove("golf").update
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8), "Ben"))

    // Add
    Favorite(benRex).commonScores.add("parachuting" -> 4).update
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Ben"))

    // Apply new values (replacing all current values!)
    Favorite(benRex).commonScores("volleball" -> 4, "handball" -> 5).update
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Rex"))
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Ben"))

    // Delete all
    Favorite(benRex).commonScores().update
    Person.name_("Ben").Favorite.commonScores.Animal.name.get === List()
    Animal.name_("Rex").Favorite.commonScores.Person.name.get === List()
  }
}
