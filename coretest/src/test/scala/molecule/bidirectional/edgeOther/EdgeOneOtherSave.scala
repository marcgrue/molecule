package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util._
import org.specs2.specification.Scope

class EdgeOneOtherSave extends MoleculeSpec {


    // Edge consistency checks.
    // Any edge should always be connected to both a base and a target entity.

    "base - edge - <missing target>" in new Setup {

      // Can't save edge missing the target namespace (`Person`)
      // The edge needs to be complete at all times to preserve consistency.

      (living_Person.name("Ben").Favorite.weight(5).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `living_Favorite`."

      // Same applies when using a reference attribute (`loves`)
      val edgeId = 42L
      (living_Person.name("Ben").favorite(edgeId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `living_Favorite`."
    }


    "<missing base> - edge - target" in new Setup {

      (living_Favorite.weight(7).Animal.name("Rex").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `living_Animal`."

      val targetId = 42L
      (living_Favorite.weight(7).animal(targetId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `living_Favorite`."
    }


    "<missing base> - edge - <missing target>" in new Setup {

      (living_Favorite.weight(7).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace somewhere after edge property `living_Favorite/weight`."
    }


    "new base -- new edge -- new target" in new Setup {

      /*
          When a "property edge" is created, Molecule automatically creates a reverse reference in the opposite direction:

          Ben --> benFavoriteRex (7) -->  Rex
            \                         /
              <-- rexFavoriteBen (7) <--

          This allow us to query from Ben to Rex and Rex to Ben in a uniform way.

          So we get 4 entities:
      */
      val List(ben, benFavoriteRex, rexFavoriteBen, rex) = living_Person.name("Ben").Favorite.weight(7).Animal.name("Rex").save.eids

      // Bidirectional property edges have been saved
      living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    }


    "new base -- new edge -- existing target" in new Setup {

      val rex = living_Animal.name.insert("Rex").eid

      // Save Ben with weighed relationship to existing Rex
      living_Person.name("Ben").Favorite.weight(7).animal(rex).save.eids

      // Ben and Rex know each other with a weight of 7
      living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    }


    "edge with multiple properties" in new Setup {

      // We save some qualitites separately first
      val loves     = living_Quality.name("Love").save.eid
      val inCommons = living_Quality.name.insert("Patience", "Humor").eids
      val rex       = living_Animal.name.insert("Rex").eid

      // Save Rex, Ben and bidirectional edge properties describing their relationship
      living_Person.name("Ben") // New entity
        .Favorite
        .weight(7)
        .howWeMet("inSchool")
        .commonInterests("Food", "Walking", "Travelling")
        .commonLicences("climbing", "flying")
        .commonScores(Seq("baseball" -> 9, "golf" -> 7))
        .coreQuality(loves)
        .inCommon(inCommons)
        .animal(rex) // Saving reference to existing other entity
        .save

      // All edge properties have been saved in both directions:

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
          , "inSchool"
          , Set("Food", "Walking", "Travelling")
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
          , "inSchool"
          , Set("Food", "Walking", "Travelling")
          , Set("climbing", "flying")
          , Map("baseball" -> 9, "golf" -> 7)
          , "Love"
          , List("Patience", "Humor")
          , "Ben")
      )
    }

}
