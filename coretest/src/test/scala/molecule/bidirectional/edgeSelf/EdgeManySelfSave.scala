package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util._
import org.specs2.specification.Scope

class EdgeManySelfSave extends MoleculeSpec {



    // Edge consistency checks.
    // Any edge should always be connected to both a base and a target entity.

    "base - edge - <missing target>" in new Setup {

      // Can't save edge missing the target namespace (`Person`)
      // The edge needs to be complete at all times to preserve consistency.

      (Person.name("Ann").Knows.weight(5).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `Knows`."

      // Same applies when using a reference attribute (`knows`)
      val edgeId = 42L
      (Person.name("Ann").knows(edgeId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `Knows`."
    }


    "<missing base> - edge - target" in new Setup {

      (Knows.weight(7).Person.name("Ben").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `Person`."

      val targetId = 42L
      (Knows.weight(7).person(targetId).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `Knows`."
    }


    "<missing base> - edge - <missing target>" in new Setup {

      (Knows.weight(7).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace somewhere after edge property `Knows/weight`."
    }


    "new base -- new edge -- new target" in new Setup {

      /*
          When a "property edge" is created, Molecule automatically creates a reverse reference in the opposite direction:

          Ann --> annKnowsBen (7) -->  Ben
            \                         /
              <-- benKnowsAnn (7) <--

          This allow us to query from Ann to Ben and Ben to Ann in a uniform way.

          So we get 4 entities:
      */
      val List(ann, annKnowsBen, benKnowsAnn, ben) = Person.name("Ann").Knows.weight(7).Person.name("Ben").save.eids

      // Bidirectional property edges have been saved
      Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ann", 7, "Ben"),
        // Reverse edge:
        ("Ben", 7, "Ann")
      )

      // Ann and Ben know each other with a weight of 7
      Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
      Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
    }


    "new base -- new edge -- existing target" in new Setup {

      val ben = Person.name.insert("Ben").eid

      // Save Ann with weighed relationship to existing Ben
      Person.name("Ann").Knows.weight(7).person(ben).save.eids

      // Ann and Ben know each other with a weight of 7
      Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
      Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
    }


    "no nesting in save molecules" in new Setup {

      (Person.name("Ann").Knows.*(Knows.weight(7)).Person.name("Ben").save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

      // Insert entities, each having one or more connected entities with relationship properties
      val ben = Person.name.insert("Ben").eid
      (Person.name("Ben").Knows.*(Knows.weight(7).person(ben)).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"
    }


    "edge with multiple properties" in new Setup {

      // We save some qualitites separately first
      val loves     = Quality.name("Love").save.eid
      val inCommons = Quality.name.insert("Patience", "Humor").eids
      val ann       = Person.name.insert("Ann").eid

      // Save Ben, Ann and bidirectional edge properties describing their relationship
      Person.name("Ben") // New entity
        .Knows
        .weight(7)
        .howWeMet("inSchool")
        .commonInterests("Food", "Walking", "Travelling")
        .commonLicences("climbing", "flying")
        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
        .coreQuality(loves)
        .inCommon(inCommons)
        .person(ann) // Saving reference to existing Person entity
        .save

      // Reference is bidirectional - both edges point to each other and have all properties
      Person.name
        .Knows
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
          , "inSchool"
          , Set("Food", "Walking", "Travelling")
          , Set("climbing", "flying")
          , Map("baseball" -> 9, "golf" -> 7)
          , "Love"
          , List("Patience", "Humor")
          , "Ben"),
        ("Ben"
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

}
