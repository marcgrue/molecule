package molecule
package bidirectional

import molecule._
import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util.MoleculeSpec
import org.specs2.specification.Scope

class EdgeSelfMany extends MoleculeSpec {


  //  "Save" >> {
  //
  //    "2 new entities with edge properties" in new Setup {
  //
  //      // We save some qualitites separately first
  //      val loves     = living_Quality.name("Love").save.eid
  //      val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet
  //
  //      // Save Ben, Ida and bidirectional edge properties describing their relationship
  //      living_Person.name("Ben") // New entity
  //        .Knows
  //        .weight(7)
  //        .howWeMet("inSchool")
  //        .commonInterests("Food", "Walking", "Travelling")
  //        .commonLicences("climbing", "flying")
  //        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
  //        .coreQuality(loves)
  //        .inCommon(inCommons)
  //        .Person.name("Ida") // Creating new person
  //        .save
  //
  //      // Reference is bidirectional - both edges point to each other and have all properties
  //      living_Person.name.Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name
  //        .get === List(
  //        // "Original" edge
  //        ("Ben"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ida"),
  //
  //        // Managed reverse edge
  //        ("Ida"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ben")
  //      )
  //
  //      // Does Ida know someone well that she has fun with?
  //      living_Person.name_("Ida").Knows.weight_.>(6).InCommon.name_("Humor")._Knows.Person.name.get === List("Ben")
  //
  //      // We can't build into related namespaces from a property edge since it would become unwieldy to keep track of.
  //      // Instead we save the related entity and apply the ids in an edge property as we did above with `coreQuality(love)`.
  //      (living_Person.name("Ben").Knows.weight(7).CoreQuality.name("Love")._Knows.Person.name("Ida").save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: [api.CheckModel.noEdgePropRefs] Building on to another namespace " +
  //        "from a property edge of a save molecule not allowed. " +
  //        "Please create the referenced entity sepearately and apply the created ids to a ref attr instead, like `.coreQuality(<refIds>)`"
  //    }
  //
  //
  //    "1 new entity with property edge to exisiting entity" in new Setup {
  //
  //      // We save some qualitites separately first
  //      val loves     = living_Quality.name("Love").save.eid
  //      val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet
  //      val ida       = living_Person.name.insert("Ida").eid
  //
  //
  //      // Save Ben, Ida and bidirectional edge properties describing their relationship
  //      living_Person.name("Ben") // New entity
  //        .Knows
  //        .weight(7)
  //        .howWeMet("inSchool")
  //        .commonInterests("Food", "Walking", "Travelling")
  //        .commonLicences("climbing", "flying")
  //        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
  //        .coreQuality(loves)
  //        .inCommon(inCommons)
  //        .person(ida) // Saving reference to existing Person entity
  //        .save
  //
  //      // Reference is bidirectional - both edges point to each other and have all properties
  //      living_Person.name
  //        .Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name
  //        .get === List(
  //        ("Ida"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ben"),
  //        ("Ben"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ida")
  //      )
  //    }
  //
  //
  //    "no nesting in save molecules" in new Setup {
  //
  //      (living_Person.name("Ben").Knows.*(living_Knows.weight(4)).Person.name("Ida").save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"
  //
  //      // Insert entities, each having one or more connected entities with relationship properties
  //      (living_Person.name("Ben").Knows.*(living_Knows.weight(4).Person.name("Ida")).save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"
  //    }
  //  }
  //
  //
  //  "Insert new" >> {
  //
  //    "new edge new" in new Setup {
  //
  //      living_Person.name
  //        .Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name insert List(
  //        ("Ben"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ida")
  //      )
  //
  //      living_Person.name
  //        .Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name
  //        .get === List(
  //        ("Ben"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ida"),
  //        ("Ida"
  //          , 7
  //          , "inSchool"
  //          , Set("Food", "Walking", "Travelling")
  //          , Set("climbing", "flying")
  //          , Map("baseball" -> 9, "golf" -> 7)
  //          , "Love"
  //          , List("Patience", "Humor")
  //          , "Ben")
  //      )
  //    }
  //
  //    "new *(edge) new" in new Setup {
  //
  //      // Can't make multiple edges to the same target entity
  //      (living_Person.name.Knows.*(living_Knows.weight).Person.name insert List(
  //        ("Ben", List(7, 8), "Ida")
  //      ) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[api.CheckModel.noNestedEdgesWithoutTarget] Nested edge ns `living_Knows` should link " +
  //        s"to target ns within the nested group of attributes."
  //    }
  //
  //
  //    "new *(edge new)" in new Setup {
  //
  //      // Insert entities, each having one or more connected entities with relationship properties
  //      living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
  //        ("Ben", List((7, "Ida"), (6, "Liz"))),
  //        ("Bob", List((8, "Ann")))
  //      )
  //
  //      // Ben is connect to Ida and Liz and they are in turn connected to Ben
  //      living_Person.name.Knows.*(living_Knows.weight.Person.name).get === List(
  //        ("Ben", List((7, "Ida"), (6, "Liz"))),
  //        ("Ida", List((7, "Ben"))),
  //        ("Liz", List((6, "Ben"))),
  //        ("Bob", List((8, "Ann"))),
  //        ("Ann", List((8, "Bob")))
  //      )
  //    }
  //
  //    "new *(edge new) - full" in new Setup {
  //
  //      // Insert entity having multiple connected entities
  //      living_Person.name.Knows.*(living_Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name) insert List(
  //        (
  //          "Ben",
  //          List(
  //            (7
  //              , "inSchool"
  //              , Set("Food", "Walking", "Travelling")
  //              , Set("climbing", "flying")
  //              , Map("baseball" -> 9, "golf" -> 7)
  //              , "Love"
  //              , List("Patience", "Humor")
  //              , "Ida"),
  //            (6
  //              , "atWork"
  //              , Set("Programming", "Molecule")
  //              , Set("diving")
  //              , Map("dart" -> 4, "running" -> 2)
  //              , "Respect"
  //              , List("Focus", "Wit")
  //              , "Liz")
  //          ))
  //      )
  //
  //      // Ben is connect to Ida and Liz and they are in turn connected to Ben with the same edge properties
  //      living_Person.name.Knows.*(living_Knows
  //        .weight
  //        .howWeMet
  //        .commonInterests
  //        .commonLicences
  //        .commonScores
  //        .CoreQuality.name._Knows
  //        .InCommon.*(living_Quality.name)._Knows
  //        .Person.name).get === List(
  //        ("Ben", List(
  //          (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Ida"),
  //          (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Liz"))),
  //        ("Ida", List(
  //          (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Ben"))),
  //        ("Liz", List(
  //          (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Ben")))
  //      )
  //
  //    }
  //  }


  //  "Insert existing" >> {
  //
  //    "new - edge - 1 existing" in new Setup {
  //
  //      val List(ida, tim) = living_Person.name insert List("Ida", "Tim") eids
  //
  //      // Insert 2 living_Persons and befriend them with existing living_Persons
  //      living_Person.name.Knows.weight.person insert List(
  //        ("Ben", 6, ida),
  //        ("Don", 7, tim)
  //      )
  //
  //      // Bidirectional references have been inserted
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //        ("Ben", 6, "Ida"),
  //        ("Don", 7, "Tim"),
  //        // Reverse refs:
  //        ("Ida", 6, "Ben"),
  //        ("Tim", 7, "Don")
  //      )
  //
  //      // Don and Tim knows each other with a weight of 7
  //      living_Person.name_("Don").Knows.weight.Person.name.get === List((7, "Tim"))
  //      living_Person.name_("Tim").Knows.weight.Person.name.get === List((7, "Don"))
  //    }
  //
  //
  //    "new - ref - n existing" in new Setup {
  //
  //      val List(ida, liz, tim) = living_Person.name insert List("Ida", "Liz", "Tim") eids
  //
  //      // Insert 2 living_Persons and connect them with existing persons
  //      living_Person.name.Knows.*(living_Knows.weight.person) insert List(
  //        ("Ben", List((7, ida), (6, liz))),
  //        ("Bob", List((8, tim)))
  //      )
  //
  //      // Now we can get a nice list of who each person knows and how well
  //      living_Person.name.Knows.*(living_Knows.weight.Person.name).get === List(
  //        ("Ida", List((7, "Ben"))),
  //        ("Liz", List((6, "Ben"))),
  //        ("Tim", List((8, "Bob"))),
  //        ("Ben", List((7, "Ida"), (6, "Liz"))),
  //        ("Bob", List((8, "Tim")))
  //      )
  //    }
  //  }


  "Update edge" >> {

    "creating edge to 1 new" in new Setup {

      // We save some qualitites separately first
      val ben       = living_Person.name("Ben").save.eid
      val loves     = living_Quality.name("Love").save.eid
      val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet

      // Update Ben with creation of Ida and bidirectional property edge between them
      living_Person(ben)
        .Knows
        .weight(7)
        .howWeMet("inSchool")
        .commonInterests("Food", "Walking", "Travelling")
        .commonLicences("climbing", "flying")
        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
        .coreQuality(loves)
        .inCommon(inCommons)
        .Person.name("Ida")
        .update

      // Bidirectional property edge to Ida added to Ben
      living_Person.name.Knows
        .weight
        .howWeMet
        .commonInterests
        .commonLicences
        .commonScores
        .CoreQuality.name._Knows
        .InCommon.*(living_Quality.name)._Knows
        .Person.name
        .get === List(
        ("Ben"
          , 7
          , "inSchool"
          , Set("Food", "Walking", "Travelling")
          , Set("climbing", "flying")
          , Map("baseball" -> 9, "golf" -> 7)
          , "Love"
          , List("Patience", "Humor")
          , "Ida"),
        ("Ida"
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


    "creating edge to multiple new" in new Setup {

      val ben = living_Person.name.insert("Ben").eid


      // Can't update multiple values of cardinality-one attribute `name`
      (living_Person.name("Ben").Knows.weight(7).Person.name("Ida", "Liz").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
        "  living_Person ... name(Ida, Liz)"


      // Nesting not allowed in update molecules
      (living_Person.name("Ben").Knows.*(living_Knows.weight(4)).Person.name("Ida").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"

      (living_Person.name("Ben").Knows.*(living_Knows.weight(4).Person.name("Ida")).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"

      // Each edge has only 1 target entity so we can't use nested structures on the target namespace
      // (living_Person.name("Ben").Knows.weight(7).Person.*(living_Person.name("Ida")).update
      //                                                   ^ nesting of edge target namespace not available

      // So, we can't update multiple referenced entities in one go.
      // If we need to update multiple entities we would need to loop and update each in turn.
    }


    "creating edge to 1 existing" in new Setup {

      val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids

      // Ben and Ida don't know each other yet
      living_Person.name.Knows.weight.Person.name.get.sorted === List()

      // Update Ben with creation of bidirectional property edge to existing Ida
      living_Person(ben).Knows.weight(7).person(ida).update

      // Now they know each other and we know how well too through the edge property `weight`
      living_Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ben", 7, "Ida"),
        ("Ida", 7, "Ben")
      )
    }


    "removing edge" in new Setup {

      /*
      Bidirectional relationships create an extra edge entity that points in the reverse direction:

      Ben --> benKnowsIda (7) -->  Ida
        \                         /
          <-- idaKnowsBen (7) <--

      So therefore, we get 4 entities from the following save:
      */
      val List(ben, benKnowsIda, idaKnowsBen, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids

      // Similarly two edge ids + Liz' id are returned from this update
      val List(benKnowsLiz, lizKnowsBen, liz) = living_Person(ben).Knows.weight(8).Person.name("Liz").update.eids

      // Ben knows Ida and Liz
      living_Person(ben).Knows.weight.Person.name.get === List((7, "Ida"), (8, "Liz"))
      living_Person(ida).Knows.weight.Person.name.get === List((7, "Ben"))
      living_Person(liz).Knows.weight.Person.name.get === List((8, "Ben"))

      // Remove Ben's friendship (edge) to Ida
      living_Person(ben).knows.remove(benKnowsIda).update
      // Note that we could have alternatively used the reversde edge id `idaKnowsBen`
      // (Molecule looks up the reverse id and retracts that entity too).

      // Ben now only knows Liz
      // (Note that both Ida and Ben now don't know each other (both edges retracted)
      living_Person(ben).Knows.weight.Person.name.get === List((8, "Liz"))
      living_Person(ida).Knows.weight.Person.name.get === List()
      living_Person(liz).Knows.weight.Person.name.get === List((8, "Ben"))
    }

  }

  class SampleData extends Scope with DatomicFacade {
    implicit val conn = recreateDbFrom(BidirectionalSchema)

    // Sample data: Ben knows Ida and Liz
    living_Person.name.Knows.*(living_Knows
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Knows
      .InCommon.*(living_Quality.name)._Knows
      .Person.name) insert List(
      (
        "Ben",
        List(
          (7
            , "inSchool"
            , Set("Food", "Walking", "Travelling")
            , Set("climbing", "flying")
            , Map("baseball" -> 9, "golf" -> 7)
            , "Love"
            , List("Patience", "Humor")
            , "Ida"),
          (6
            , "atWork"
            , Set("Programming", "Molecule")
            , Set("diving")
            , Map("dart" -> 4, "running" -> 2)
            , "Respect"
            , List("Focus", "Wit")
            , "Liz")
        ))
    )

    val ben = living_Person.e.name_("Ben").one
    val ida = living_Person.e.name_("Ida").one
    val liz = living_Person.e.name_("Liz").one
  }


  "Update edge properties" >> {

    // Update various types of edge properties

    /*

    -
    Ns(id).prop(value)
    Ns.prop


    -  Edge.prop(value).Target.e(id)
    -  Edge.prop(value).Target.e(id).prop(value)
    -  Edge(id)

    */

    "One Int" in new SampleData {

      // Update Ben with creation of bidirectional reference to existing Ida
//      living_Person(ben).Knows.weight(5).updateD
//      living_Person(ben).Knows.weight(5).update

//      living_Person.knows.updateD
//      living_Person.knows().updateD
//      living_Person.knows(42L).updateD
//      living_Person.knows(42L, 43L).updateD
//      living_Person.knows(Set(42L, 43L)).updateD
//      val ids = Set(42L, 43L)
//      living_Person.knows(ids).updateD
//
//
//
//      living_Person.knows$.updateD
//
//      living_Person.knows_().updateD
//      living_Person.knows_.updateD
//
//
//      living_Person.Knows.weight.updateD
//      living_Person.Knows.weight(7).updateD
//
//      living_Person(ben).knows.updateD
//      living_Person(ben).knows(42L).updateD
//      living_Person(ben).Knows.weight.updateD
//      living_Person(ben).Knows.weight(7).updateD
//
//
//      living_Knows(42L).weight(5).updateD
//
//      living_Knows(42L).weight(5).person(ida).updateD
//      living_Knows.weight(5).person(ida).updateD
//
//
//      living_Person(ben).Knows.weight(5).person(ida).updateD
//      living_Person(ben).Knows.weight(5).Person.name("Ida").updateD
//
//      living_Person(ben).Knows.weight(5).person(ida).update

      living_Person.name.Knows.weight.Person.name.get.sorted === List(
        ("Ben", "Ida"),
        ("Ida", "Ben")
      )
    }

  }


//  "Update replacing ref to other existing" in new Setup {
//
//    // Save Ben, Ida and bidirectional references between them
//    val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//    // Bidirectional references created
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ida", "Ben")
//    )
//
//    // Update Ben by adding Liz and bidirectional references to/from her
//    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//    // and will not cause his friendship to Ida to be retracted!
//    val liz = living_Person.name.insert("Liz").eid
//    living_Person(ben).Knows.weight(7).person(liz).update
//
//    // Bidirectional references to/from Liz has been added.
//    // OBS: see note above and difference to cardinality-one updates!
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Ida"),
//      ("Ben", "Liz"),
//      ("Ida", "Ben"),
//      ("Liz", "Ben")
//    )
//
//    // To remove the friendship to Ida we need to remove it manually as we did above
//    living_Person(ben).Knows.weight(7).person.remove(ida).update
//
//    // Now the friendship with Ida has been replaced with the friendship with Liz
//    living_Person.name.Knows.weight.Person.name.get.sorted === List(
//      ("Ben", "Liz"),
//      ("Liz", "Ben")
//    )
//  }
//
//
//  "Update removing all references" >> {
//
//    "retracting ref" in new Setup {
//
//      // Save Ben, Ida and bidirectional references between them
//      val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//      // Bidirectional references created
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//
//      // Retract ref between them by applying no value
//      living_Person(ben).Knows.weight(7).Person().update
//
//      // Bidirectional references retracted - Ben has no friends anymore
//      living_Person.name.Knows.weight.Person.name.get.sorted === List()
//    }
//  }
  //
  //
  //    "Retract" in new Setup {
  //
  //      // (Same as for cardinality-one)
  //
  //      val ben = living_Person.name.insert("Ben").eid
  //
  //      // Create and reference b to a
  //      val ida = living_Person(ben).Knows.weight(7).Person.name("Ida").update.eid
  //
  //      living_Person(ben).Knows.weight(7).Person.name.get === List("Ida")
  //      living_Person(ida).Knows.weight(7).Person.name.get === List("Ben")
  //
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //        ("Ben", "Ida"),
  //        ("Ida", "Ben")
  //      )
  //
  //      // Retract a and all references from/to a
  //      ben.retract
  //
  //      // Woa remains and both references retracted
  //      living_Person.name.get === List("Ida")
  //      living_Person(ben).Knows.weight(7).Person.name.get === List()
  //      living_Person(ida).Knows.weight(7).Person.name.get === List()
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List()
  //    }
}
