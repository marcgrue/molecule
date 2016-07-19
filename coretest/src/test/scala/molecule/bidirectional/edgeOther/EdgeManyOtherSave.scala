package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherSave extends MoleculeSpec {


  // Edge consistency checks.
  // Any edge should always be connected to both a base and a target entity.

  "base - edge - <missing target>" in new Setup {

    // Can't save edge missing the target namespace (`Person`)
    // The edge needs to be complete at all times to preserve consistency.

    (Person.name("Ben").CloseTo.weight(5).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `CloseTo`."

    // Same applies when using a reference attribute (`closeTo`)
    val edgeId = 42L
    (Person.name("Ben").closeTo(edgeId).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace after edge namespace `CloseTo`."
  }


  "<missing base> - edge - target" in new Setup {

    (CloseTo.weight(7).Animal.name("Rex").save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `Animal`."

    val targetId = 42L
    (CloseTo.weight(7).animal(targetId).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.save_edgeComplete]  Missing base namespace before edge namespace `CloseTo`."
  }


  "<missing base> - edge - <missing target>" in new Setup {

    (CloseTo.weight(7).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.save_edgeComplete]  Missing target namespace somewhere after edge property `CloseTo/weight`."
  }


  "new base -- new edge -- new target" in new Setup {

    /*
        When a "property edge" is created, Molecule automatically creates a reverse reference in the opposite direction:

        Ben --> benCloseToRex (7) -->  Rex
          \                         /
            <-- rexCloseToBen (7) <--

        This allow us to query from Ben to Rex and Rex to Ben in a uniform way.

        So we get 4 entities:
    */
    val List(ben, benCloseToRex, rexCloseToBen, rex) = Person.name("Ben").CloseTo.weight(7).Animal.name("Rex").save.eids

    // Bidirectional property edges have been saved
    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
  }


  "new base -- new edge -- existing target" in new Setup {

    val rex = Animal.name.insert("Rex").eid

    // Save Ben with weighed relationship to existing Rex
    Person.name("Ben").CloseTo.weight(7).animal(rex).save.eids

    // Ben and Rex know each other with a weight of 7
    Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
  }


  "no nesting in save molecules" in new Setup {

    (Person.name("Ben").CloseTo.*(CloseTo.weight(7)).Animal.name("Rex").save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"

    // Insert entities, each having one or more connected entities with relationship properties
    val rex = Person.name.insert("Rex").eid
    (Person.name("Rex").CloseTo.*(CloseTo.weight(7).animal(rex)).save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"
  }


  "edge with multiple properties" in new Setup {

    // We save some qualitites separately first
    val love      = Quality.name("Love").save.eid
    val inCommons = Quality.name.insert("Patience", "Humor").eids
    val rex       = Animal.name.insert("Rex").eid

    // Save Rex, Ben and bidirectional edge properties describing their relationship
    Person.name("Ben") // New entity
      .CloseTo
      .weight(7)
      .howWeMet("inSchool")
      .commonInterests("Food", "Walking", "Travelling")
      .commonLicences("climbing", "flying")
      .commonScores(Seq("golf" -> 7, "baseball" -> 9))
      .coreQuality(love)
      .inCommon(inCommons)
      .animal(rex) // Saving reference to existing Person entity
      .save

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

}
