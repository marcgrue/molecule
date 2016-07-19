package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util._
import org.specs2.specification.Scope

class EdgeManyOtherUpdateProps extends MoleculeSpec {

  class Setup extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(BidirectionalSchema)

    val love                  = Quality.name("Love").save.eid
    val List(patience, humor) = Quality.name.insert("Patience", "Humor").eids
    val rex                   = Animal.name.insert("Rex").eid

    val List(ben, benRex, rexBen) = Person.name("Ben") // New entity
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
      ("Ben"
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
        , "Ben")
    )
  }


  "Card-one" >> {

    "value" in new Setup {

      // Updating edge properties from the base entity is not allowed
      (Person(ben).CloseTo.howWeMet("inSchool").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Can't update edge `CloseTo` " +
        s"of base entity `Person` without knowing which target entity the edge is pointing too. " +
        s"Please update the edge itself, like `CloseTo(<edgeId>).edgeProperty(<new value>).update`."

      // Instead update the edge entity itself:

      // Current weight value
      Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))

      // Apply new value
      CloseTo(benRex).weight(2).update
      Person.name_("Ben").CloseTo.weight.Animal.name.get === List((2, "Rex"))
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List((2, "Ben"))

      // Retract value
      CloseTo(benRex).weight().update
      Person.name_("Ben").CloseTo.weight.Animal.name.get === List()
      Animal.name_("Rex").CloseTo.weight.Person.name.get === List()
    }


    "enum" in new Setup {

      // Current howWeMet enum value
      Person.name_("Ben").CloseTo.howWeMet.Animal.name.get === List(("inSchool", "Rex"))
      Animal.name_("Rex").CloseTo.howWeMet.Person.name.get === List(("inSchool", "Ben"))

      // Apply new enum value
      CloseTo(benRex).howWeMet("throughFriend").update
      Person.name_("Ben").CloseTo.howWeMet.Animal.name.get === List(("throughFriend", "Rex"))
      Animal.name_("Rex").CloseTo.howWeMet.Person.name.get === List(("throughFriend", "Ben"))

      // Retract enum value
      CloseTo(benRex).howWeMet().update
      Person.name("Ben").CloseTo.howWeMet.get === List()
      Animal.name("Rex").CloseTo.howWeMet.get === List()
    }


    "ref" in new Setup {

      // Current value
      Person.name_("Ben").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Love", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Love", "Ben"))

      // We can't update across namespaces
      (CloseTo(benRex).CoreQuality.name("Compassion").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Quality`."

      // Instead we can either update the referenced entity or replace the reference to another existing Quality entity

      // 1. Update referenced value

      Quality(love).name("Compassion").update

      // Same reference, new value
      Person.name_("Ben").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Compassion", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Compassion", "Ben"))


      // 2. Update reference

      val trust = Quality.name("Trust").save.eid
      CloseTo(benRex).coreQuality(trust).update

      // New reference/value
      Person.name_("Ben").CloseTo.CoreQuality.name._CloseTo.Animal.name.get === List(("Trust", "Rex"))
      Animal.name_("Rex").CloseTo.CoreQuality.name._CloseTo.Person.name.get === List(("Trust", "Ben"))


      // Retract reference
      CloseTo(benRex).coreQuality().update
      CloseTo(benRex).CoreQuality.name.get === List()
    }
  }


  "Card-many" >> {

    "values" in new Setup {

      // Current values
      Person.name_("Ben").CloseTo.commonInterests.Animal.name.get === List((Set("Food", "Travelling", "Walking"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Food", "Travelling", "Walking"), "Ben"))

      // Replace
      CloseTo(benRex).commonInterests.replace("Food" -> "Cuisine").update
      Person.name_("Ben").CloseTo.commonInterests.Animal.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Travelling", "Walking", "Cuisine"), "Ben"))

      // Remove
      CloseTo(benRex).commonInterests.remove("Travelling").update
      Person.name_("Ben").CloseTo.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Walking", "Cuisine"), "Ben"))

      // Add
      CloseTo(benRex).commonInterests.add("Meditating").update
      Person.name_("Ben").CloseTo.commonInterests.Animal.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Walking", "Cuisine", "Meditating"), "Ben"))

      // Apply new values
      CloseTo(benRex).commonInterests("Running", "Cycling").update
      Person.name_("Ben").CloseTo.commonInterests.Animal.name.get === List((Set("Running", "Cycling"), "Rex"))
      Animal.name_("Rex").CloseTo.commonInterests.Person.name.get === List((Set("Running", "Cycling"), "Ben"))

      // Retract all
      CloseTo(benRex).commonInterests().update
      Person.name_("Ben").CloseTo.commonInterests.get === List()
      Animal.name_("Rex").CloseTo.commonInterests.get === List()
    }


    "enums" in new Setup {

      // Current enum values
      Person.name_("Ben").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Replace
      CloseTo(benRex).commonLicences.replace("flying" -> "diving").update
      Person.name_("Ben").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "diving"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "diving"), "Ben"))

      // Remove
      CloseTo(benRex).commonLicences.remove("climbing").update
      Person.name_("Ben").CloseTo.commonLicences.Animal.name.get === List((Set("diving"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("diving"), "Ben"))

      // Add
      CloseTo(benRex).commonLicences.add("parachuting").update
      Person.name_("Ben").CloseTo.commonLicences.Animal.name.get === List((Set("diving", "parachuting"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("diving", "parachuting"), "Ben"))

      // Apply new values
      CloseTo(benRex).commonLicences("climbing", "flying").update
      Person.name_("Ben").CloseTo.commonLicences.Animal.name.get === List((Set("climbing", "flying"), "Rex"))
      Animal.name_("Rex").CloseTo.commonLicences.Person.name.get === List((Set("climbing", "flying"), "Ben"))

      // Retract all
      CloseTo(benRex).commonLicences().update
      Person.name_("Ben").CloseTo.commonLicences.get === List()
      Animal.name_("Rex").CloseTo.commonLicences.get === List()
      Person.name("Ben" or "Rex").CloseTo.commonLicences.get === List()
    }


    "refs" in new Setup {

      // Current value
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Patience", "Humor"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Patience", "Humor"), "Ben"))

      // As with card-one references we have two choices to change referenced value(s)

      // 1. Update referenced value(s)

      Quality(patience).name("Waiting ability").update
      Quality(humor).name("Funny").update

      // Same references, new value(s)
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Funny"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Funny"), "Ben"))


      // 2. Update reference(s)

      val sporty = Quality.name("Sporty").save.eid

      // replace
      CloseTo(benRex).inCommon.replace(humor -> sporty).update
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // remove
      CloseTo(benRex).inCommon.remove(patience).update
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Sporty"), "Ben"))

      // add
      CloseTo(benRex).inCommon.add(patience).update
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Waiting ability", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Waiting ability", "Sporty"), "Ben"))

      // Apply new values
      CloseTo(benRex).inCommon(sporty, humor).update
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List((Seq("Funny", "Sporty"), "Rex"))
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List((Seq("Funny", "Sporty"), "Ben"))

      // Retract all references
      CloseTo(benRex).inCommon().update
      Person.name_("Ben").CloseTo.InCommon.*(Quality.name)._CloseTo.Animal.name.get === List()
      Animal.name_("Rex").CloseTo.InCommon.*(Quality.name)._CloseTo.Person.name.get === List()
    }
  }


  "Map" in new Setup {

    // Current values
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 9, "golf" -> 7), "Ben"))

    // Replace values by key
    CloseTo(benRex).commonScores.replace("baseball" -> 8, "golf" -> 6).update
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8, "golf" -> 6), "Ben"))

    // Remove by key
    CloseTo(benRex).commonScores.remove("golf").update
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8), "Ben"))

    // Add
    CloseTo(benRex).commonScores.add("parachuting" -> 4).update
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("baseball" -> 8, "parachuting" -> 4), "Ben"))

    // Apply new values (replacing all current values!)
    CloseTo(benRex).commonScores("volleball" -> 4, "handball" -> 5).update
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Rex"))
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List((Map("volleball" -> 4, "handball" -> 5), "Ben"))

    // Delete all
    CloseTo(benRex).commonScores().update
    Person.name_("Ben").CloseTo.commonScores.Animal.name.get === List()
    Animal.name_("Rex").CloseTo.commonScores.Person.name.get === List()
  }
}
