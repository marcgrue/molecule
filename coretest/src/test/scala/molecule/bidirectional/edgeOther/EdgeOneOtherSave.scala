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

      (Person.name("Ben").Favorite.weight(5).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `Favorite`."

      // Same applies when using a reference attribute (`Loves`)
      val edgeId = 42L
      (Person.name("Ben").favorite(edgeId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `Favorite`."
    }


    "<missing base> - edge - target" in new Setup {

      (Favorite.weight(7).Animal.name("Rex").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `Animal`."

      val targetId = 42L
      (Favorite.weight(7).animal(targetId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `Favorite`."
    }


    "<missing base> - edge - <missing target>" in new Setup {

      (Favorite.weight(7).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace somewhere after edge property `Favorite/weight`."
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
      val List(ben, benFavoriteRex, rexFavoriteBen, rex) = Person.name("Ben").Favorite.weight(7).Animal.name("Rex").save.eids

      // Bidirectional property edges have been saved
      Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    }


    "new base -- new edge -- existing target" in new Setup {

      val rex = Animal.name.insert("Rex").eid

      // Save Ben with weighed relationship to existing Rex
      Person.name("Ben").Favorite.weight(7).animal(rex).save.eids

      // Ben and Rex know each other with a weight of 7
      Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
      Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    }


    "edge with multiple properties" in new Setup {

      // We save some qualitites separately first
      val loves     = Quality.name("Love").save.eid
      val inCommons = Quality.name.insert("Patience", "Humor").eids
      val rex       = Animal.name.insert("Rex").eid

      // Save Rex, Ben and bidirectional edge properties describing their relationship
      Person.name("Ben") // New entity
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
