package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec

class EdgeSelfMany extends MoleculeSpec {


//  "Save" >> {
//
//    // Edge consistency checks.
//    // Any edge should always be connected to both a base and a target entity.
//
//    "base - edge - <missing target>" in new Setup {
//
//      // Can't save edge missing the target namespace (`Person`)
//      // The edge needs to be complete at all times to preserve consistency.
//
//      (living_Person.name("Ann").Knows.weight(5).save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.edgeConsistency]  Missing target namespace after edge namespace `living_Knows`."
//
//      // Same applies when using a reference attribute (`knows`)
//      val edgeId = 42L
//      (living_Person.name("Ann").knows(edgeId).save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.edgeConsistency]  Missing target namespace after edge namespace `living_Knows`."
//    }
//
//
//    "<missing base> - edge - target" in new Setup {
//
//      (living_Knows.weight(7).Person.name("Ben").save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.edgeConsistency]  Missing base namespace before edge namespace `living_Person`."
//
//      val targetId = 42L
//      (living_Knows.weight(7).person(targetId).save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.edgeConsistency]  Missing base namespace before edge namespace `living_Knows`."
//    }
//
//
//    "<missing base> - edge - <missing target>" in new Setup {
//
//      (living_Knows.weight(7).save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.edgeConsistency]  Missing target namespace somewhere after edge property `living_Knows/weight`."
//    }
//
//
//    "new base -- new edge -- new target" in new Setup {
//
//      // Save Ann with weighed relationship to new Ben
//      living_Person.name("Ann").Knows.weight(7).Person.name("Ben").save.eids
//
//      // Bidirectional property edges have been saved
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ann", 7, "Ben"),
//        // Reverse edge:
//        ("Ben", 7, "Ann")
//      )
//
//      // Ann and Ben know each other with a weight of 7
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
//    }
//
//
//    "new base -- new edge -- existing target" in new Setup {
//
//      val ben = living_Person.name.insert("Ben").eid
//
//      // Save Ann with weighed relationship to existing Ben
//      living_Person.name("Ann").Knows.weight(7).person(ben).save.eids
//
//      // Ann and Ben know each other with a weight of 7
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
//    }
//
//
//    "no nesting in save molecules" in new Setup {
//
//      (living_Person.name("Ann").Knows.*(living_Knows.weight(7)).Person.name("Ben").save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"
//
//      // Insert entities, each having one or more connected entities with relationship properties
//      val ben = living_Person.name.insert("Ben").eid
//      (living_Person.name("Ben").Knows.*(living_Knows.weight(7).person(ben)).save must throwA[IllegalArgumentException])
//        .message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in save molecules"
//    }
//
//
//    "edge with multiple properties" in new Setup {
//
//      // We save some qualitites separately first
//      val loves     = living_Quality.name("Love").save.eid
//      val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet
//      val ann       = living_Person.name.insert("Ann").eid
//
//      // Save Ben, Ann and bidirectional edge properties describing their relationship
//      living_Person.name("Ben") // New entity
//        .Knows
//        .weight(7)
//        .howWeMet("inSchool")
//        .commonInterests("Food", "Walking", "Travelling")
//        .commonLicences("climbing", "flying")
//        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
//        .coreQuality(loves)
//        .inCommon(inCommons)
//        .person(ann) // Saving reference to existing Person entity
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
//        ("Ann"
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
//          , "Ann")
//      )
//    }
//  }


//  "Insert" >> {
//
//    "1 new" in new Setup {
//
//      // Insert 1 pair of entities with bidirectional property edge between them
//      living_Person.name.Knows.weight.Person.name.insert("Ann", 7, "Ben")
//
//      // Bidirectional property edge has been inserted
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
//    }
//
//    "1 existing" in new Setup {
//
//      val ben = living_Person.name.insert("Ben").eid
//
//      // Insert Ann with bidirectional property edge to existing Ben
//      living_Person.name.Knows.weight.person.insert("Ann", 7, ben)
//
//      // Bidirectional property edge has been inserted
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
//    }
//
//
//    "multiple new" in new Setup {
//
//      // Insert 2 pair of entities with bidirectional property edge between them
//      living_Person.name.Knows.weight.Person.name insert List(
//        ("Ann", 7, "Joe"),
//        ("Ben", 6, "Tim")
//      )
//
//      // Bidirectional property edges have been inserted
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Joe"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Tim"))
//      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
//      living_Person.name_("Tim").Knows.weight.Person.name.get === List((6, "Ben"))
//    }
//
//    "multiple existing" in new Setup {
//
//      val List(joe, tim) = living_Person.name.insert("Joe", "Tim").eids
//
//      // Insert 2 entities with bidirectional property edges to existing entities
//      living_Person.name.Knows.weight.person insert List(
//        ("Ann", 7, joe),
//        ("Ben", 6, tim)
//      )
//
//      // Bidirectional property edges have been inserted
//      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Joe"))
//      living_Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Tim"))
//      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
//      living_Person.name_("Tim").Knows.weight.Person.name.get === List((6, "Ben"))
//    }
//
//
//    "nested new" in new Setup {
//
//      // Insert molecules allow nested data structures. So we can conveniently
//      // insert 2 entities each connected to 2 target entites via property edges
//      living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
//        ("Ann", List((7, "Ben"), (6, "Joe"))),
//        ("Don", List((8, "Tim"), (9, "Tom")))
//      )
//
//      living_Person.name.Knows.*(living_Knows.weight.Person.name).get.sortBy(_._1) === List(
//        ("Ann", List((7, "Ben"), (6, "Joe"))),
//        ("Ben", List((7, "Ann"))),
//        ("Don", List((8, "Tim"), (9, "Tom"))),
//        ("Joe", List((6, "Ann"))),
//        ("Tim", List((8, "Don"))),
//        ("Tom", List((9, "Don")))
//      )
//
//      // Can't save nested edges without including target entity
//      (living_Person.name.Knows.*(living_Knows.weight).Person.name insert List(
//        ("Ben", List(7, 8), "Joe")
//      ) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
//        s"[molecule.api.CheckModel.noNestedEdgesWithoutTarget]  Nested edge ns `living_Knows` should link to " +
//        s"target ns within the nested group of attributes."
//    }
//
//    "nested existing" in new Setup {
//
//      val List(ben, joe, tim, tom) = living_Person.name.insert("Ben", "Joe", "Tim", "Tom").eids
//
//      // Insert 2 living_Persons and connect them with existing living_Persons
//      living_Person.name.Knows.*(living_Knows.weight.person) insert List(
//        ("Ann", List((7, ben), (6, joe))),
//        ("Don", List((8, tim), (9, tom)))
//      )
//
//      // Bidirectional references have been inserted - not how Ben got 2 (reverse) friendships
//      living_Person.name.Knows.*(living_Knows.weight.Person.name).get.sortBy(_._1) === List(
//        ("Ann", List((7, "Ben"), (6, "Joe"))),
//        ("Ben", List((7, "Ann"))),
//        ("Don", List((8, "Tim"), (9, "Tom"))),
//        ("Joe", List((6, "Ann"))),
//        ("Tim", List((8, "Don"))),
//        ("Tom", List((9, "Don")))
//      )
//    }
//
//
//    "1 large edge to new entity" in new Setup {
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
//          , "Joe")
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
//          , "Joe"),
//        ("Joe"
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
//
//    "multiple large edges to existing entities" in new Setup {
//
//      val List(joe, liz) = living_Person.name.insert("Joe", "Liz").eids
//
//      // Insert new entity with property edges to multiple existing entities
//      living_Person.name.Knows.*(living_Knows
//        .weight
//        .howWeMet
//        .commonInterests
//        .commonLicences
//        .commonScores
//        .CoreQuality.name._Knows
//        .InCommon.*(living_Quality.name)._Knows
//        .person) insert List(
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
//              , joe),
//            (6
//              , "atWork"
//              , Set("Programming", "Molecule")
//              , Set("diving")
//              , Map("dart" -> 4, "running" -> 2)
//              , "Respect"
//              , List("Focus", "Wit")
//              , liz)
//          ))
//      )
//
//      // Ben is connect to Joe and Liz and they are in turn connected to Ben with the same edge properties
//      living_Person.name.Knows.*(living_Knows
//        .weight
//        .howWeMet
//        .commonInterests
//        .commonLicences
//        .commonScores
//        .CoreQuality.name._Knows
//        .InCommon.*(living_Quality.name)._Knows
//        .Person.name).get.sortBy(_._1) === List(
//        ("Ben", List(
//          (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Joe"),
//          (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Liz"))),
//        ("Joe", List(
//          (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Ben"))),
//        ("Liz", List(
//          (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Ben")))
//      )
//    }
//  }




    "Update" >> {



      "add" in new Setup {

        val List(ann, ben, joe, liz, tom) = living_Person.name.insert("Ann", "Ben", "Joe", "Liz", "Tom").eids

        // Add friendships in various ways

        // Single reference
        living_Person(ann).friends.add(ben).update

        // Multiple references (vararg)
        living_Person(ann).friends.add(joe, liz).update

        // Set of references
        living_Person(ann).friends.add(Set(tom)).update

        // Friendships have been added in both directions
        living_Person.name_("Ann").Friends.name.get.sorted === List("Ben", "Joe", "Liz", "Tom")
        living_Person.name_("Ben").Friends.name.get === List("Ann")
        living_Person.name_("Joe").Friends.name.get === List("Ann")
        living_Person.name_("Liz").Friends.name.get === List("Ann")
        living_Person.name_("Tom").Friends.name.get === List("Ann")
      }









      "creating edge to 1 new" in new Setup {

        // We save some qualitites separately first
        val ben       = living_Person.name("Ben").save.eid
        val loves     = living_Quality.name("Love").save.eid
        val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet

        // Update Ben with creation of Joe and bidirectional property edge between them
        living_Person(ben)
          .Knows
          .weight(7)
          .howWeMet("inSchool")
          .commonInterests("Food", "Walking", "Travelling")
          .commonLicences("climbing", "flying")
          .commonScores(Seq("golf" -> 7, "baseball" -> 9))
          .coreQuality(loves)
          .inCommon(inCommons)
          .Person.name("Joe")
          .update

        // Bidirectional property edge to Joe added to Ben
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
            , "Joe"),
          ("Joe"
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
        (living_Person.name("Ben").Knows.weight(7).Person.name("Joe", "Liz").update must throwA[IllegalArgumentException])
          .message === "Got the exception java.lang.IllegalArgumentException: " +
          s"[api.CheckModel.noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
          "  living_Person ... name(Joe, Liz)"


        // Nesting not allowed in update molecules
        (living_Person.name("Ben").Knows.*(living_Knows.weight(4)).Person.name("Joe").update must throwA[IllegalArgumentException])
          .message === "Got the exception java.lang.IllegalArgumentException: " +
          s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"

        (living_Person.name("Ben").Knows.*(living_Knows.weight(4).Person.name("Joe")).update must throwA[IllegalArgumentException])
          .message === "Got the exception java.lang.IllegalArgumentException: " +
          s"[api.CheckModel.noNested] Nested data structures not allowed in save molecules"

        // Each edge has only 1 target entity so we can't use nested structures on the target namespace
        // (living_Person.name("Ben").Knows.weight(7).Person.*(living_Person.name("Joe")).update
        //                                                   ^ nesting of edge target namespace not available

        // So, we can't update multiple referenced entities in one go.
        // If we need to update multiple entities we would need to loop and update each in turn.
      }


      "creating edge to 1 existing" in new Setup {

        val List(ben, joe) = living_Person.name.insert("Ben", "Joe").eids

        // Ben and Joe don't know each other yet
        living_Person.name.Knows.weight.Person.name.get.sorted === List()

        // Update Ben with creation of bidirectional property edge to existing Joe
        living_Person(ben).Knows.weight(7).person(joe).update

        // Now they know each other and we know how well too through the edge property `weight`
        living_Person.name.Knows.weight.Person.name.get.sorted === List(
          ("Ben", 7, "Joe"),
          ("Joe", 7, "Ben")
        )
      }


      "removing edge" in new Setup {

        /*
        Bidirectional relationships create an extra edge entity that points in the reverse direction:

        Ben --> benKnowsJoe (7) -->  Joe
          \                         /
            <-- joeKnowsBen (7) <--

        So therefore, we get 4 entities from the following save:
        */
        val List(ben, benKnowsJoe, joeKnowsBen, joe) = living_Person.name("Ben").Knows.weight(7).Person.name("Joe").save.eids

        // Similarly two edge ids + Liz' id are returned from this update
        val List(benKnowsLiz, lizKnowsBen, liz) = living_Person(ben).Knows.weight(8).Person.name("Liz").update.eids

        // Ben knows Joe and Liz
        living_Person(ben).Knows.weight.Person.name.get === List((7, "Joe"), (8, "Liz"))
        living_Person(joe).Knows.weight.Person.name.get === List((7, "Ben"))
        living_Person(liz).Knows.weight.Person.name.get === List((8, "Ben"))

        // Remove Ben's friendship (edge) to Joe
        living_Person(ben).knows.remove(benKnowsJoe).update
        // Note that we could have alternatively used the reversde edge id `joeKnowsBen`
        // (Molecule looks up the reverse id and retracts that entity too).

        // Ben now only knows Liz
        // (Note that both Joe and Ben now don't know each other (both edges retracted)
        living_Person(ben).Knows.weight.Person.name.get === List((8, "Liz"))
        living_Person(joe).Knows.weight.Person.name.get === List()
        living_Person(liz).Knows.weight.Person.name.get === List((8, "Ben"))
      }

    }

//  import org.specs2.specification.Scope
//  import molecule.bidirectional.schema.BidirectionalSchema
//
//
//    class SampleData extends Scope with DatomicFacade {
//      implicit val conn = recreateDbFrom(BidirectionalSchema)
//
//      // Sample data: Ben knows Joe and Liz
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
//              , "Joe"),
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
//      val ben = living_Person.e.name_("Ben").one
//      val joe = living_Person.e.name_("Joe").one
//      val liz = living_Person.e.name_("Liz").one
//    }
//
//
//    "Update edge properties" >> {
//
//      // Update various types of edge properties
//
//      /*
//
//      -
//      Ns(id).prop(value)
//      Ns.prop
//
//
//      -  Edge.prop(value).Target.e(id)
//      -  Edge.prop(value).Target.e(id).prop(value)
//      -  Edge(id)
//
//      */
//
//      "One Int" in new SampleData {
//
//        // Update Ben with creation of bidirectional reference to existing Joe
//        living_Person(ben).Knows.weight(5).updateD
//        living_Person(ben).Knows.weight(5).update
//
//        living_Person.knows.updateD
//        living_Person.knows().updateD
//        living_Person.knows(42L).updateD
//        living_Person.knows(42L, 43L).updateD
//        living_Person.knows(Set(42L, 43L)).updateD
//        val ids = Set(42L, 43L)
//        living_Person.knows(ids).updateD
//
//
//
//        living_Person.knows$.updateD
//
//        living_Person.knows_().updateD
//        living_Person.knows_.updateD
//
//
//        living_Person.Knows.weight.updateD
//        living_Person.Knows.weight(7).updateD
//
//        living_Person(ben).knows.updateD
//        living_Person(ben).knows(42L).updateD
//        living_Person(ben).Knows.weight.updateD
//        living_Person(ben).Knows.weight(7).updateD
//
//
//        living_Knows(42L).weight(5).updateD
//
//        living_Knows(42L).weight(5).person(joe).updateD
//        living_Knows.weight(5).person(joe).updateD
//
//
//        living_Person(ben).Knows.weight(5).person(joe).updateD
//        living_Person(ben).Knows.weight(5).Person.name("Joe").updateD
//
//        living_Person(ben).Knows.weight(5).person(joe).update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Joe"),
//          ("Joe", "Ben")
//        )
//      }
//
//    }


  //  "Update replacing ref to other existing" in new Setup {
  //
  //    // Save Ben, Joe and bidirectional references between them
  //    val List(ben, joe) = living_Person.name("Ben").Knows.weight(7).Person.name("Joe").save.eids
  //
  //    // Bidirectional references created
  //    living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //      ("Ben", "Joe"),
  //      ("Joe", "Ben")
  //    )
  //
  //    // Update Ben by adding Liz and bidirectional references to/from her
  //    // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
  //    // and will not cause his friendship to Joe to be retracted!
  //    val liz = living_Person.name.insert("Liz").eid
  //    living_Person(ben).Knows.weight(7).person(liz).update
  //
  //    // Bidirectional references to/from Liz has been added.
  //    // OBS: see note above and difference to cardinality-one updates!
  //    living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //      ("Ben", "Joe"),
  //      ("Ben", "Liz"),
  //      ("Joe", "Ben"),
  //      ("Liz", "Ben")
  //    )
  //
  //    // To remove the friendship to Joe we need to remove it manually as we did above
  //    living_Person(ben).Knows.weight(7).person.remove(joe).update
  //
  //    // Now the friendship with Joe has been replaced with the friendship with Liz
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
  //      // Save Ben, Joe and bidirectional references between them
  //      val List(ben, joe) = living_Person.name("Ben").Knows.weight(7).Person.name("Joe").save.eids
  //
  //      // Bidirectional references created
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //        ("Ben", "Joe"),
  //        ("Joe", "Ben")
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
  //      val joe = living_Person(ben).Knows.weight(7).Person.name("Joe").update.eid
  //
  //      living_Person(ben).Knows.weight(7).Person.name.get === List("Joe")
  //      living_Person(joe).Knows.weight(7).Person.name.get === List("Ben")
  //
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List(
  //        ("Ben", "Joe"),
  //        ("Joe", "Ben")
  //      )
  //
  //      // Retract a and all references from/to a
  //      ben.retract
  //
  //      // Woa remains and both references retracted
  //      living_Person.name.get === List("Joe")
  //      living_Person(ben).Knows.weight(7).Person.name.get === List()
  //      living_Person(joe).Knows.weight(7).Person.name.get === List()
  //      living_Person.name.Knows.weight.Person.name.get.sorted === List()
  //    }
}
