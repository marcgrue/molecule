//package molecule.bidirectional.edgeOther
//
//import molecule._
//import molecule.bidirectional.Setup
//import molecule.bidirectional.dsl.bidirectional._
//import molecule.bidirectional.schema.BidirectionalSchema
//import molecule.util._
//import org.specs2.specification.Scope
//
//class EdgeManyOtherInsert extends MoleculeSpec {
//
//
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
//}
