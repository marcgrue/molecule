package molecule
package bidirectional

import molecule._
import molecule.bidirectional.dsl.bidirectional._
import molecule.util.MoleculeSpec

class EdgeSelfMany extends MoleculeSpec {


    "Save" >> {

      "2 new entities with edge properties" in new Setup {

        // We save some qualitites separately first
        val loves     = living_Quality.name("Love").save.eid
        val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet

        // Save Ben, Ida and bidirectional edge properties describing their relationship
        living_Person.name("Ben") // New entity
          .Knows
          .weight(7)
          .howWeMet("inSchool")
          .commonInterests("Food", "Walking", "Travelling")
          .commonLicences("climbing", "flying")
          .commonScores(Seq("golf" -> 7, "baseball" -> 9))
          .coreQuality(loves)
          .inCommon(inCommons)
          .Person.name("Ida") // Creating new person
          .save

        // Reference is bidirectional - both edges point to each other and have all properties
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
          // "Original" edge
          ("Ben"
            , 7
            , "inSchool"
            , Set("Food", "Walking", "Travelling")
            , Set("climbing", "flying")
            , Map("baseball" -> 9, "golf" -> 7)
            , "Love"
            , List("Patience", "Humor")
            , "Ida"),

          // Managed reverse edge
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

        // We can't build into related namespaces from a property edge since it would become unwieldy to keep track of.
        // Instead we save the related entity and apply the ids in an edge property as we did above with `coreQuality(love)`.
        (living_Person.name("Ben").Knows.weight(7).CoreQuality.name("Love")._Knows.Person.name("Ida").save must throwA[IllegalArgumentException])
          .message === "Got the exception java.lang.IllegalArgumentException: [output.Molecule.noEdgePropRefs] Building on to another namespace " +
          "from a property edge of a save molecule not allowed. " +
          "Please create the referenced entity sepearately and apply the created ids to a ref attr instead, like `.coreQuality(<refIds>)`"
      }


      "1 new entity with property edge to exisiting entity" in new Setup {

        // We save some qualitites separately first
        val loves     = living_Quality.name("Love").save.eid
        val inCommons = living_Quality.name.insert("Patience", "Humor").eidSet
        val ida       = living_Person.name.insert("Ida").eid


        // Save Ben, Ida and bidirectional edge properties describing their relationship
        living_Person.name("Ben") // New entity
          .Knows
          .weight(7)
          .howWeMet("inSchool")
          .commonInterests("Food", "Walking", "Travelling")
          .commonLicences("climbing", "flying")
          .commonScores(Seq("golf" -> 7, "baseball" -> 9))
          .coreQuality(loves)
          .inCommon(inCommons)
          .person(ida) // Saving reference to existing Person entity
          .save

        // Reference is bidirectional - both edges point to each other and have all properties
        living_Person.name
          .Knows
          .weight
          .howWeMet
          .commonInterests
          .commonLicences
          .commonScores
          .CoreQuality.name._Knows
          .InCommon.*(living_Quality.name)._Knows
          .Person.name
          .get === List(
          ("Ida"
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
            , "Ida")
        )
      }
    }


  "Insert new" >> {

        "new edge new" in new Setup {

          living_Person.name
            .Knows
            .weight
            .howWeMet
            .commonInterests
            .commonLicences
            .commonScores
            .CoreQuality.name._Knows
            .InCommon.*(living_Quality.name)._Knows
            .Person.name insert List(
            ("Ben"
              , 7
              , "inSchool"
              , Set("Food", "Walking", "Travelling")
              , Set("climbing", "flying")
              , Map("baseball" -> 9, "golf" -> 7)
              , "Love"
              , List("Patience", "Humor")
              , "Ida")
          )

          living_Person.name
            .Knows
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

    "new *(edge) new" in new Setup {

      // Can't make multiple edges to the same target entity
      (living_Person.name.Knows.*(living_Knows.weight).Person.name insert List(
        ("Ben", List(7, 8), "Ida")
      ) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[output.Molecule.noNestedEdgesWithoutTarget] Nested edge ns `living_Knows` should link " +
        s"to target ns within the nested group of attributes."
    }


    "new *(edge new)" in new Setup {

      // Insert entity having multiple connected entities
      living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(
        ("Ben", List(
          (7, "Ida"),
          (6, "Liz")
        ))
      )

      // Ben is connect to Ida and Liz and they are in turn connected to Ben
      living_Person.name.Knows.*(living_Knows.weight.Person.name).get === List(
        ("Ben", List((7, "Ida"), (6, "Liz"))),
        ("Ida", List((7, "Ben"))),
        ("Liz", List((6, "Ben")))
      )
    }


    //        "new *(edge new)" in new Setup {
    ////      living_Person.name
    ////        .Knows
    ////        .*(living_Knows
    ////          .weight
    ////          .howWeMet
    ////          .commonInterests
    ////          .commonLicences
    ////          .commonScores
    ////          .CoreQuality.name._Knows
    ////          .InCommon.*(living_Quality.name)
    ////        )
    ////        .Person.name insert List(
    ////        ("Ben"
    ////          , 7
    ////          , "inSchool"
    ////          , Set("Food", "Walking", "Travelling")
    ////          , Set("climbing", "flying")
    ////          , Map("baseball" -> 9, "golf" -> 7)
    ////          , "Love"
    ////          , List("Patience", "Humor")
    ////          , "Ida")
    ////      )
    ////
    ////
    ////      living_Person.name
    ////        .Knows
    ////        .*(living_Knows
    ////          .weight
    ////          .howWeMet
    ////          .commonInterests
    ////          .commonLicences
    ////          .commonScores
    ////          .CoreQuality.name._Knows
    ////          .InCommon.*(living_Quality.name)._Knows
    ////          .Person.name
    ////        ) insert List(
    ////        ("Ben"
    ////          , 7
    ////          , "inSchool"
    ////          , Set("Food", "Walking", "Travelling")
    ////          , Set("climbing", "flying")
    ////          , Map("baseball" -> 9, "golf" -> 7)
    ////          , "Love"
    ////          , List("Patience", "Humor")
    ////          , "Ida")
    ////      )
    ////
    ////      living_Person.name
    ////        .Knows
    ////        .weight
    ////        .howWeMet
    ////        .commonInterests
    ////        .commonLicences
    ////        .commonScores
    ////        .CoreQuality.name._Knows
    ////        .InCommon.*(living_Quality.name)._Knows
    ////        .Person.name
    ////        .get === List(
    ////        ("Ben"
    ////          , 7
    ////          , "inSchool"
    ////          , Set("Food", "Walking", "Travelling")
    ////          , Set("climbing", "flying")
    ////          , Map("baseball" -> 9, "golf" -> 7)
    ////          , "Love"
    ////          , List("Patience", "Humor")
    ////          , "Ida"),
    ////        ("Ida"
    ////          , 7
    ////          , "inSchool"
    ////          , Set("Food", "Walking", "Travelling")
    ////          , Set("climbing", "flying")
    ////          , Map("baseball" -> 9, "golf" -> 7)
    ////          , "Love"
    ////          , List("Patience", "Humor")
    ////          , "Ben")
    ////      )
    //    }
  }


//    "Insert existing" >> {
//
//      "new - ref - 1 existing" in new Setup {
//
//        val List(ida, tim) = living_Person.name insert List("Ida", "Tim") eids
//
//        // Insert 2 living_Persons and befriend them with existing living_Persons
//        living_Person.name.friends insert List(
//          ("Ben", Set(ida, 41L)),
//          ("Don", Set(tim))
//        )
//
//        // Bidirectional references have been inserted
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Don", "Tim"),
//          // Reverse refs:
//          ("Ida", "Ben"),
//          ("Tim", "Don")
//        )
//      }
//
//      "new - ref - n existing" in new Setup {
//
//        val List(ida, liz, tim) = living_Person.name insert List("Ida", "Liz", "Tim") eids
//
//        // Insert 2 living_Persons and befriend them with existing living_Persons
//        living_Person.name.friends insert List(
//          ("Ben", Set(ida, liz)),
//          ("Don", Set(ida, tim))
//        )
//
//        // Bidirectional references have been inserted - not how Ida got 2 (reverse) friendships
//        living_Person.name.Knows.weight.Person.*(Person.name).get.sortBy(_._1) === List(
//          ("Ben", List("Ida", "Liz")),
//          ("Don", List("Ida", "Tim")),
//          ("Ida", List("Ben", "Don")),
//          ("Liz", List("Ben")),
//          ("Tim", List("Don"))
//        )
//
//        // Bidirectional references have been inserted
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ben", "Liz"),
//          ("Don", "Ida"),
//          ("Don", "Tim"),
//          ("Ida", "Ben"),
//          ("Ida", "Don"),
//          ("Liz", "Ben"),
//          ("Tim", "Don")
//        )
//      }
//    }
//
//
//    "Update new" >> {
//
//      "creating ref to 1 new" in new Setup {
//
//        val ben = living_Person.name.insert("Ben").eid
//
//        // Update a with creation of b and bidirectional reference between a and b
//        living_Person(ben).Knows.weight(7).Person.name("Ida").update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", 7, "Ida"),
//          ("Ida", 7, "Ben")
//        )
//      }
//
//
//      "creating refs to multiple new" in new Setup {
//
//        val ben = living_Person.name.insert("Ben").eid
//
//
//        // Can't update multiple values of cardinality-one attribute `name`
//        (Person.name("Ben").Knows.weight(7).Person.name("Ida", "Liz").update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//          s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
//          "  living_Person ... name(Ida, Liz)"
//
//        // Can't update nested data structures
//        (Person.name("Ben").Knows.weight(7).Person.*(Person.name("Ida")).update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//          s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"
//
//        // So, we can't create multiple referenced entities with the `update` command.
//        // Update ref attribute with already created entity ids instead (see below).
//      }
//
//
//      "replacing ref to 1 new" in new Setup {
//
//        val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//        // Bidirectional references created
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ida", "Ben")
//        )
//
//        // Update Ben with creation of Liz and bidirectional references to/from her.
//        // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//        // and will not cause his friendship to Ida to be retracted as it would be with a
//        // cardinality one reference!
//        living_Person(ben).Knows.weight(7).Person.name("Liz").update
//
//        // Bidirectional references to/from Liz has been added.
//        // OBS: see note above and difference to cardinality-one updates!
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ben", "Liz"),
//          ("Ida", "Ben"),
//          ("Liz", "Ben")
//        )
//
//        // To remove the friendship to Ida we need to remove it manually
//        living_Person(ben).Knows.weight(7).Person.remove(ida).update
//
//        // Now the friendship with Ida has been replaced with the friendship with Liz
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Liz"),
//          ("Liz", "Ben")
//        )
//      }
//    }
//
//
//    "replacing refs to multiple new" in new Setup {
//
//      val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//      // Bidirectional references created
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ida", "Ben")
//      )
//
//      // Update Ben with creation of Liz and bidirectional references to/from her
//      // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//      // and will not cause his friendship to Ida to be retracted!
//      living_Person(ben).Knows.weight(7).Person.name("Liz").update
//
//      // Bidirectional references to/from Liz has been added.
//      // OBS: see note above and difference to cardinality-one updates!
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//
//      // Can't update multiple values of cardinality-one attribute `name`
//      (Person(ben).Knows.weight(7).Person.name("Ida", "Liz").update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule:noConflictingCardOneValues (1)] Can't update multiple values for cardinality-one attribute:\n" +
//        "  living_Person ... name(Ida, Liz)"
//
//      // Can't update nested data structures
//      (Person(ben).Knows.weight(7).Person.*(Person.name("Ida")).update must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
//        s"[output.Molecule.noNested] Nested data structures not allowed in update molecules"
//
//      // So, we can't create multiple referenced entities with the `update` command.
//      // Update ref attribute with existing created entity ids instead (see below).
//    }
//
//
//    "Update existing" >> {
//
//      "creating ref to 1 existing" in new Setup {
//
//        val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids
//
//        // Update a with creation of bidirectional reference to existing b
//        living_Person(ben).Knows.weight(7).person(ida).update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ida", "Ben")
//        )
//      }
//
//      "creating ref to 1 existing with explicit `add`" in new Setup {
//
//        val List(ben, ida) = living_Person.name.insert("Ben", "Ida").eids
//
//        // Update a with creation of bidirectional reference to existing b
//        living_Person(ben).Knows.weight(7).Person.add(ida).update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ida", "Ben")
//        )
//      }
//
//
//      "creating refs to multiple existing, vararg" in new Setup {
//
//        val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids
//
//        // Update Ben with two new friendships
//        living_Person(ben).Knows.weight(7).person(ida, liz).update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ben", "Liz"),
//          ("Ida", "Ben"),
//          ("Liz", "Ben")
//        )
//      }
//
//
//      "creating refs to multiple existing, Set" in new Setup {
//
//        val List(ben, ida, liz) = living_Person.name.insert("Ben", "Ida", "Liz").eids
//
//        // Update Ben with two new friendships supplied as a Set
//        living_Person(ben).Knows.weight(7).person(Set(ida, liz)).update
//
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ben", "Liz"),
//          ("Ida", "Ben"),
//          ("Liz", "Ben")
//        )
//      }
//    }
//
//
//    "Update replacing ref to other existing" in new Setup {
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
//      // Update Ben by adding Liz and bidirectional references to/from her
//      // Note that since `Friends` is a cardinality many, Liz is _added_ as a friend to Ben
//      // and will not cause his friendship to Ida to be retracted!
//      val liz = living_Person.name.insert("Liz").eid
//      living_Person(ben).Knows.weight(7).person(liz).update
//
//      // Bidirectional references to/from Liz has been added.
//      // OBS: see note above and difference to cardinality-one updates!
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Ida"),
//        ("Ben", "Liz"),
//        ("Ida", "Ben"),
//        ("Liz", "Ben")
//      )
//
//      // To remove the friendship to Ida we need to remove it manually as we did above
//      living_Person(ben).Knows.weight(7).person.remove(ida).update
//
//      // Now the friendship with Ida has been replaced with the friendship with Liz
//      living_Person.name.Knows.weight.Person.name.get.sorted === List(
//        ("Ben", "Liz"),
//        ("Liz", "Ben")
//      )
//    }
//
//
//    "Update removing all references" >> {
//
//      "retracting ref" in new Setup {
//
//        // Save Ben, Ida and bidirectional references between them
//        val List(ben, ida) = living_Person.name("Ben").Knows.weight(7).Person.name("Ida").save.eids
//
//        // Bidirectional references created
//        living_Person.name.Knows.weight.Person.name.get.sorted === List(
//          ("Ben", "Ida"),
//          ("Ida", "Ben")
//        )
//
//        // Retract ref between them by applying no value
//        living_Person(ben).Knows.weight(7).Person().update
//
//        // Bidirectional references retracted - Ben has no friends anymore
//        living_Person.name.Knows.weight.Person.name.get.sorted === List()
//      }
//    }
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
