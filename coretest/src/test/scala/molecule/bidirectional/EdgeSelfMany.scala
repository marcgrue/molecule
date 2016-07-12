package molecule
package bidirectional

import molecule.bidirectional.dsl.bidirectional._
import molecule.bidirectional.schema.BidirectionalSchema
import molecule.util.MoleculeSpec
import org.specs2.specification.Scope

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
  //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Missing target namespace after edge namespace `living_Knows`."
  //
  //      // Same applies when using a reference attribute (`knows`)
  //      val edgeId = 42L
  //      (living_Person.name("Ann").knows(edgeId).save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Missing target namespace after edge namespace `living_Knows`."
  //    }
  //
  //
  //    "<missing base> - edge - target" in new Setup {
  //
  //      (living_Knows.weight(7).Person.name("Ben").save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Missing base namespace before edge namespace `living_Person`."
  //
  //      val targetId = 42L
  //      (living_Knows.weight(7).person(targetId).save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Missing base namespace before edge namespace `living_Knows`."
  //    }
  //
  //
  //    "<missing base> - edge - <missing target>" in new Setup {
  //
  //      (living_Knows.weight(7).save must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Missing target namespace somewhere after edge property `living_Knows/weight`."
  //    }
  //
  //
  //    "new base -- new edge -- new target" in new Setup {
  //
  //      /*
  //          When a "property edge" is created, Molecule automatically creates a reverse reference in the opposite direction:
  //
  //          Ann --> annKnowsBen (7) -->  Ben
  //            \                         /
  //              <-- benKnowsAnn (7) <--
  //
  //          This allow us to query from Ann to Ben and Ben to Ann in a uniform way.
  //
  //          So we get 4 entities:
  //      */
  //      val List(ann, annKnowsBen, benKnowsAnn, ben) = living_Person.name("Ann").Knows.weight(7).Person.name("Ben").save.eids
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
  //
  //
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
  //
  //
  //  "Update edges" >> {
  //
  //    "add" in new Setup {
  //
  //      // In order to maintain data consistency we can't create property edges in isolation.
  //      // We can therefore not "add" them to existing entities as we could with simple
  //      // reference values as we saw in the update:replace1/multiple tests in `SelfMany`.
  //      (living_Person(ann).knows.add(42L).update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Adding edge ids with " +
  //        s"`edgeAttr.add(someEdgeId)` is not allowed. It could be an indication that you are trying to " +
  //        s"use an existing edge twice which is not allowed."
  //
  //      // Adding an edge from an existing entity to another entity therefore involves
  //      // creating the edge entity itself having a reference to either an existing target
  //      // entity or a newly created one.
  //
  //      val ann = living_Person.name("Ann").save.eid
  //
  //      // New edge and new target entity
  //      // Update Ann with new friendship to new Ben
  //      living_Person(ann).Knows.weight(7).Person.name("Ben").update
  //
  //      // New edge with reference to existing target entity
  //      val joe = living_Person.name("Joe").save.eid
  //      // Update Ann with new friendship to existing Joe
  //      living_Person(ann).Knows.weight(6).person(joe).update
  //
  //      // Both bidirectional edges have been added from/to Ann
  //      living_Person.name_("Ann").Knows.weight.Person.name.get === List((7, "Ben"), (6, "Joe"))
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((7, "Ann"))
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((6, "Ann"))
  //
  //
  //      // Note that an edge always have only one target entity.
  //      // So we can't add multiple (won't compile)
  //      // living_Person(ann).Knows.weight(6).person(42L, 43L).update
  //
  //      // Can't update multiple values of cardinality-one attribute `name`
  //      (living_Person(ann).Knows.weight(7).Person.name("Joe", "Liz").update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:\n" +
  //        "  living_Person ... name(Joe, Liz)"
  //
  //      // As with save molecules nesting is not allowed in update molecules
  //      (living_Person(ann).Knows.*(living_Knows.weight(4)).Person.name("Joe").update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in update molecules"
  //
  //      (living_Person(ann).Knows.*(living_Knows.weight(4)).person(joe).update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in update molecules"
  //
  //      (living_Person(ann).Knows.*(living_Knows.weight(4).Person.name("Joe")).update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in update molecules"
  //
  //      (living_Person(ann).Knows.*(living_Knows.weight(4).person(joe)).update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.api.CheckModel.noNested]  Nested data structures not allowed in update molecules"
  //
  //      // Each edge has only 1 target entity so we can't use nested structures on the target namespace
  //      // (living_Person.name("Ben").Knows.weight(7).Person.*(living_Person.name("Joe")).update
  //      //                                                   ^ nesting of edge target namespace not available
  //    }
  //
  //
  //    "remove" in new Setup {
  //
  //      living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
  //        ("Ann", List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf")))
  //      )
  //
  //      // Entities
  //      val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)
  //
  //      // Edges
  //      val List(annBen, annJoe, annLiz, annTom, annUlf) = living_Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)
  //
  //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (4, "Liz"), (8, "Tom"), (9, "Ulf"))
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
  //      living_Person.name_("Liz").Knows.weight.Person.name.get === List((4, "Ann"))
  //      living_Person.name_("Tom").Knows.weight.Person.name.get === List((8, "Ann"))
  //      living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))
  //
  //      // Remove single edge
  //      living_Person(ann).knows.remove(annBen).update
  //
  //      // Remove multiple edges
  //      living_Person(ann).knows.remove(annJoe, annLiz).update
  //
  //      // Remove Set of edges
  //      living_Person(ann).knows.remove(Set(annTom)).update
  //
  //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((9, "Ulf"))
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Liz").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Tom").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))
  //
  //      // Remove all edges
  //      living_Person(ann).knows().update
  //
  //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List()
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Liz").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Tom").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Ulf").Knows.weight.Person.name.get === List()
  //    }
  //
  //
  //    "replace" in new Setup {
  //
  //      val ids = living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
  //        ("Ann", List((6, "Ben"), (7, "Joe"))),
  //        ("Liz", List((9, "Tom")))
  //      ) eids
  //
  //      // Entities
  //      val List(ann, ben, joe, liz, tom) = living_Person.e.name.get.sortBy(_._2).map(_._1)
  //
  //      // Edges
  //      val List(annBen, _, annJoe, _, lizTom, _) = living_Knows.e.weight_.get
  //
  //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((6, "Ben"), (7, "Joe"))
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Ann"))
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
  //      living_Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"))
  //      living_Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))
  //
  //      // edgeAttr.apply(old -> new) not available for edges
  //      // To enforce consistency, edges are not allowed to be replaced with each other
  //      (living_Person(ann).knows(annBen -> lizTom).update must throwA[IllegalArgumentException])
  //        .message === "Got the exception java.lang.IllegalArgumentException: " +
  //        s"[molecule.transform.Model2Transaction.valueStmts:biEdgeRefAttr]  Replacing edge ids " +
  //        s"with `edgeAttr.apply(old -> new)` is not allowed. It could be an indication that you are " +
  //        s"trying to replace the old edge with an existing edge which is not allowed."
  //
  //      // Replace edge in 2 steps:
  //
  //      // 1. Remove friendship with Ben
  //      living_Person(ann).knows.remove(annBen).update
  //
  //      // 2. Update Ann with new friendship to existing Liz
  //      living_Person(ann).Knows.weight(8).person(liz).update
  //
  //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((7, "Joe"), (8, "Liz"))
  //      living_Person.name_("Ben").Knows.weight.Person.name.get === List()
  //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
  //      living_Person.name_("Liz").Knows.weight.Person.name.get === List((9, "Tom"), (8, "Ann"))
  //      living_Person.name_("Tom").Knows.weight.Person.name.get === List((9, "Liz"))
  //    }
  //  }


  "Update edge properties" >> {

    //    "add" in new Setup {
    //
    //      val ids    = living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
    //        ("Ann", List((5, "Ben"), (7, "Joe")))
    //      ) eids
    //      val ann    = ids.head
    //      val annBen = living_Person(ann).Knows.e.Person.name_("Ben").one
    //
    //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((5, "Ben"), (7, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((5, "Ann"))
    //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    //
    //      // Updating edge properties from the base entity is not allowed
    //      (living_Person(ann).Knows.howWeMet("inSchool").update must throwA[IllegalArgumentException])
    //        .message === "Got the exception java.lang.IllegalArgumentException: " +
    //        s"[molecule.api.CheckModel.save_edgeCompleteª]  Can't update edge `living_Knows` " +
    //        s"of base entity `living_Person` without knowing which target entity the edge is pointing too. " +
    //        s"Please update the edge itself, like `living_Knows(<edgeId>).edgeProperty(<new value>).update`."
    //
    //      // Instead update the edge entity itself:
    //
    //      // Add property of known edge
    //      // Upgrade friendship between Ann and Ben with info of how they met
    //      living_Knows(annBen).howWeMet("inSchool").update
    //
    //      // Additional edge property value has been added between Ann and Ben
    //      living_Person.name_("Ann").Knows.weight.howWeMet$.Person.name.get.sorted === List((5, Some("inSchool"), "Ben"), (7, None, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.howWeMet$.Person.name.get === List((5, Some("inSchool"), "Ann"))
    //      living_Person.name_("Joe").Knows.weight.howWeMet$.Person.name.get === List((7, None, "Ann"))
    //    }


    class InitialValues extends Scope with DatomicFacade {
      implicit val conn = recreateDbFrom(BidirectionalSchema)

      val love                    = living_Quality.name("Love").save.eid
      val List(patience, humor)   = living_Quality.name.insert("Patience", "Humor").eids
      val List(ann, annBen, _, _) = living_Person.name("Ann")
        .Knows
        .weight(7)
        .howWeMet("atWork")
        .commonInterests("Food", "Walking", "Travelling")
        .commonLicences("climbing", "flying")
        .commonScores(Seq("golf" -> 7, "baseball" -> 9))
        .coreQuality(love)
        .inCommon(Set(patience, humor))
        .Person.name("Ben")
        .save.eids

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
      //        ("Ann"
      //          , 7
      //          , "atWork"
      //          , Set("Food", "Walking", "Travelling")
      //          , Set("climbing", "flying")
      //          , Map("baseball" -> 9, "golf" -> 7)
      //          , "Love"
      //          , List("Patience", "Humor")
      //          , "Ben"),
      //        ("Ben"
      //          , 7
      //          , "atWork"
      //          , Set("Food", "Walking", "Travelling")
      //          , Set("climbing", "flying")
      //          , Map("baseball" -> 9, "golf" -> 7)
      //          , "Love"
      //          , List("Patience", "Humor")
      //          , "Ann")
      //      )
    }


    //    "Card-one value" in new InitialValues {
    //
    //      living_Knows(annBen).weight.one === 7
    //
    //      // To confirm that edge property values have been added to edges in
    //      // both directions we query for both edges
    //      living_Person.name("Ann" or "Ben").Knows.weight.get.sortBy(_._1) === List(
    //        ("Ann", 7),
    //        ("Ben", 7)
    //      )
    //
    //      // Replace
    //      living_Knows(annBen).weight(2).update
    //      living_Person.name("Ann" or "Ben").Knows.weight.get.sortBy(_._1) === List(
    //        ("Ann", 2),
    //        ("Ben", 2)
    //      )
    //
    //      // Delete
    //      living_Knows(annBen).weight().update
    //      living_Person.name("Ann" or "Ben").Knows.weight.get === List()
    //
    //      // New
    //      living_Knows(annBen).weight(8).update
    //      living_Person.name("Ann" or "Ben").Knows.weight.get.sortBy(_._1) === List(
    //        ("Ann", 8),
    //        ("Ben", 8)
    //      )
    //    }


    //    "Card-one enum" in new InitialValues {
    //
    //      living_Knows(annBen).howWeMet.one === "atWork"
    //      living_Person.name("Ann" or "Ben").Knows.howWeMet.get.sortBy(_._1) === List(
    //        ("Ann", "atWork"),
    //        ("Ben", "atWork")
    //      )
    //
    //      // Replace
    //      living_Knows(annBen).howWeMet("throughFriend").update
    //      living_Person.name("Ann" or "Ben").Knows.howWeMet.get.sortBy(_._1) === List(
    //        ("Ann", "throughFriend"),
    //        ("Ben", "throughFriend")
    //      )
    //
    //      // Delete
    //      living_Knows(annBen).howWeMet().update
    //      living_Person.name("Ann" or "Ben").Knows.howWeMet.get === List()
    //
    //      // New
    //      living_Knows(annBen).howWeMet("inSchool").update
    //      living_Person.name("Ann" or "Ben").Knows.howWeMet.get.sortBy(_._1) === List(
    //        ("Ann", "inSchool"),
    //        ("Ben", "inSchool")
    //      )
    //    }


        "Card-many value" in new InitialValues {

          living_Knows(annBen).commonInterests.one === Set("Food", "Walking", "Travelling")
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Food", "Walking", "Travelling")),
            ("Ben", Set("Food", "Walking", "Travelling"))
          )

          // Replace
          living_Knows(annBen).commonInterests.replace("Food" -> "Cuisine").update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Cuisine", "Walking", "Travelling")),
            ("Ben", Set("Cuisine", "Walking", "Travelling"))
          )

          // Remove
          living_Knows(annBen).commonInterests.remove("Travelling").update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Cuisine", "Walking")),
            ("Ben", Set("Cuisine", "Walking"))
          )

          // Add
          living_Knows(annBen).commonInterests.add("Meditating").update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Cuisine", "Walking", "Meditating")),
            ("Ben", Set("Cuisine", "Walking", "Meditating"))
          )

          // Apply value
          living_Knows(annBen).commonInterests("Running").update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Running")),
            ("Ben", Set("Running"))
          )

          // Apply values
          living_Knows(annBen).commonInterests("Walking", "Cycling").update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Walking", "Cycling")),
            ("Ben", Set("Walking", "Cycling"))
          )

          // Delete all
          living_Knows(annBen).commonInterests().update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get === List()

          // Apply Set of values
          living_Knows(annBen).commonInterests(Set("Cuisine", "Walking", "Travelling")).update
          living_Person.name("Ann" or "Ben").Knows.commonInterests.get.sortBy(_._1) === List(
            ("Ann", Set("Cuisine", "Walking", "Travelling")),
            ("Ben", Set("Cuisine", "Walking", "Travelling"))
          )
        }


        "Card-many enum" in new InitialValues {

          living_Knows(annBen).commonLicences.one === Set("climbing", "flying")
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("climbing", "flying")),
            ("Ben", Set("climbing", "flying"))
          )

          // Replace
          living_Knows(annBen).commonLicences.replace("flying" -> "diving").update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("climbing", "diving")),
            ("Ben", Set("climbing", "diving"))
          )

          // Remove
          living_Knows(annBen).commonLicences.remove("climbing").update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("diving")),
            ("Ben", Set("diving"))
          )

          // Add
          living_Knows(annBen).commonLicences.add("parachuting").update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("diving", "parachuting")),
            ("Ben", Set("diving", "parachuting"))
          )

          // Apply value
          living_Knows(annBen).commonLicences("diving").update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("diving")),
            ("Ben", Set("diving"))
          )

          // Apply values
          living_Knows(annBen).commonLicences("climbing", "flying").update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("climbing", "flying")),
            ("Ben", Set("climbing", "flying"))
          )

          // Delete all
          living_Knows(annBen).commonLicences().update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get === List()

          // Apply Set of values
          living_Knows(annBen).commonLicences(Set("climbing")).update
          living_Person.name("Ann" or "Ben").Knows.commonLicences.get.sortBy(_._1) === List(
            ("Ann", Set("climbing")),
            ("Ben", Set("climbing"))
          )
        }


    "Map" in new InitialValues {

      living_Knows(annBen).commonScores.one === Map("baseball" -> 9, "golf" -> 7)
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("baseball" -> 9, "golf" -> 7)),
        ("Ben", Map("baseball" -> 9, "golf" -> 7))
      )

      // Replace value with given key
      living_Knows(annBen).commonScores.replace("golf" -> 8).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("baseball" -> 9, "golf" -> 8)),
        ("Ben", Map("baseball" -> 9, "golf" -> 8))
      )

      // Replace key/value pair
      living_Knows(annBen).commonScores.replace(("baseball" -> 9, "volleyball" -> 6)).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("volleyball" -> 6, "golf" -> 8)),
        ("Ben", Map("volleyball" -> 6, "golf" -> 8))
      )

      // Remove
      living_Knows(annBen).commonScores.remove("flying").update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("baseball" -> 9, "golf" -> 8)),
        ("Ben", Map("baseball" -> 9, "golf" -> 8))
      )

      // Add
      living_Knows(annBen).commonScores.add("parachuting" -> 4).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("baseball" -> 9, "golf" -> 8, "parachuting" -> 4)),
        ("Ben", Map("baseball" -> 9, "golf" -> 8, "parachuting" -> 4))
      )

      // Apply value (replacing all current values!)
      living_Knows(annBen).commonScores("football" -> 5).updateD
      living_Knows(annBen).commonScores("football" -> 5).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("football" -> 5)),
        ("Ben", Map("football" -> 5))
      )

      // Apply values (replacing all current values!)
      living_Knows(annBen).commonScores("volleball" -> 4, "handball" -> 5).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("volleball" -> 4, "handball" -> 5)),
        ("Ben", Map("volleball" -> 4, "handball" -> 5))
      )

      // Delete all
      living_Knows(annBen).commonScores().update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get === List()

      // Apply Set of values
      living_Knows(annBen).commonScores.apply(Seq("jogging" -> 7, "cooking" -> 8)).update
      living_Person.name("Ann" or "Ben").Knows.commonScores.get.sortBy(_._1) === List(
        ("Ann", Map("jogging" -> 7, "cooking" -> 8)),
        ("Ben", Map("jogging" -> 7, "cooking" -> 8))
      )
    }

    //    "Card-one ref" in new InitialValues {
    //
    //      living_Knows(annBen).CoreQuality.name.one === "Love"
    //
    //      // Replace
    //      living_Knows(annBen).CoreQuality.name("Reading").update
    //      living_Knows(annBen).CoreQuality.name.one === 2
    //
    //      // Delete
    //      living_Knows(annBen).CoreQuality.name().update
    //      living_Knows(annBen).CoreQuality.name.get === List()
    //
    //      // New
    //      living_Knows(annBen).CoreQuality.name("lkjlkjl").update
    //      living_Knows(annBen).CoreQuality.name.one === "lkjl"
    //    }

    // Replace edge property values

    //      val spirituality = living_Quality.name("Spirituality").save.eid
    //      val compassion   = living_Quality.name("Compassion").save.eid

    // Various kinds of updates:
    //      living_Knows(annBen)
    //        .weight(8) // replace card-one value
    //        .howWeMet("inSchool") // new property
    //        .commonInterests("Food" -> "Cuisine") // replace card-many value
    //        .commonLicences.remove("flying") // remove one enum (card-many)
    //        .commonScores() // remove all
    //        .coreQuality(spirituality) // replace card-one ref
    //        .inCommon.add(compassion) // add card-many ref
    //        .update
    //
    //      // All edge properties have been updated
    //      living_Person.name.Knows
    //        .weight
    //        .howWeMet
    //        .commonInterests
    //        .commonLicences
    //        .commonScores$
    //        .CoreQuality.name._Knows
    //        .InCommon.*(living_Quality.name)._Knows
    //        .Person.name
    //        .get === List(
    //        ("Ann"
    //          , 8
    //          , "inSchool"
    //          , Set("Cuisine", "Walking", "Travelling")
    //          , Set("climbing")
    //          , None
    //          , "Spirituality"
    //          , List("Patience", "Humor", "Compassion")
    //          , "Ben"),
    //        ("Ben"
    //          , 8
    //          , "inSchool"
    //          , Set("Cuisine", "Walking", "Travelling")
    //          , Set("climbing")
    //          , None
    //          , "Spirituality"
    //          , List("Patience", "Humor", "Compassion")
    //          , "Ann")
    //      )

    //
    //
    //    "replace property value in all edges of base entity" in new Setup {
    //
    //      val ids = living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
    //        ("Ann", List((5, "Ben"), (7, "Joe")))
    //      ) eids
    //      val ann = ids.head
    //      val annKnowsBen = living_Person(ann).Knows.e.Person.name_("Ben").one
    //
    //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((5, "Ben"), (7, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((5, "Ann"))
    //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    //
    //      // Update weight of known edge
    //      living_Knows(annKnowsBen).weight(6).update
    //
    //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((6, "Ben"), (7, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((6, "Ann"))
    //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((7, "Ann"))
    //
    //      // Update all(!) Ann's friendship weights to 8
    //      living_Person(ann).Knows.weight(8).update
    //
    //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((8, "Ben"), (8, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((8, "Ann"))
    //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((8, "Ann"))
    //
    //
    //      living_Knows.weight(1).update
    //
    //      living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((8, "Ben"), (8, "Joe"))
    //      living_Person.name_("Ben").Knows.weight.Person.name.get === List((8, "Ann"))
    //      living_Person.name_("Joe").Knows.weight.Person.name.get === List((8, "Ann"))
    //    }
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


  //      "Retract edges" in new Setup {
  //
  //       "retract multiple edges" in new Setup {
  //
  //        living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
  //          ("Ann", List((1, "Ben"), (2, "Joe"), (5, "Liz"), (5, "Tom"), (9, "Ulf")))
  //        )
  //
  //        // Entities
  //        val List(ann, ben, joe, liz, tom, ulf) = living_Person.e.name.get.sortBy(_._2).map(_._1)
  //
  //        // Edges
  //        val List(annBen, annJoe, annLiz, annTom, annUlf) = living_Person(ann).Knows.e.Person.name.get.sortBy(_._2).map(_._1)
  //
  //        living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((1, "Ben"), (2, "Joe"), (5, "Liz"), (5, "Tom"), (9, "Ulf"))
  //        living_Person.name_("Ben").Knows.weight.Person.name.get === List((1, "Ann"))
  //        living_Person.name_("Joe").Knows.weight.Person.name.get === List((2, "Ann"))
  //        living_Person.name_("Liz").Knows.weight.Person.name.get === List((5, "Ann"))
  //        living_Person.name_("Tom").Knows.weight.Person.name.get === List((5, "Ann"))
  //        living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))
  //
  //
  //        // Retract edge
  //        // Each edge has an `isComponent` reference to the reverse edge and will
  //        // therefore automatically cause the reverse edge to be retracted too.
  //        living_Person.name_("Ann").Knows.e.Person.name_("Ben").one.retract
  //
  //        // Retract edge in reverse direction
  //        living_Person.name_("Joe").Knows.e.Person.name_("Ann").one.retract
  //
  //        living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((5, "Liz"), (8, "Tom"), (9, "Ulf"))
  //        living_Person.name_("Ben").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Joe").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Liz").Knows.weight.Person.name.get === List((5, "Ann"))
  //        living_Person.name_("Tom").Knows.weight.Person.name.get === List((5, "Ann"))
  //        living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))
  //
  //
  //        // Retract multiple edges using filters on edge properties
  //        living_Person.name_("Joe").Knows.e.weight_(5).get.map(_.retract)
  //
  //        // Liz and Tim no longer friends
  //        living_Person.name_("Ann").Knows.weight.Person.name.get.sorted === List((9, "Ulf"))
  //        living_Person.name_("Ben").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Joe").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Liz").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Tom").Knows.weight.Person.name.get === List()
  //        living_Person.name_("Ulf").Knows.weight.Person.name.get === List((9, "Ann"))
  //      }
  //
  //   }
}
