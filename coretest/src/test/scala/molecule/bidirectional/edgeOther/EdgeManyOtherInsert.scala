package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeManyOtherInsert extends MoleculeSpec {


  "1 new" in new Setup {

    // Insert 1 pair of entities with bidirectional property edge between them
    living_Person.name.CloseTo.weight.Animal.name.insert("Ben", 7, "Rex")

    // Bidirectional property edge has been inserted
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
  }

  "1 existing" in new Setup {

    val rex = living_Animal.name.insert("Rex").eid

    // Insert Ben with bidirectional property edge to existing Rex
    living_Person.name.CloseTo.weight.animal.insert("Ben", 7, rex)

    // Bidirectional property edge has been inserted
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
  }


  "multiple new" in new Setup {

    // Insert 2 pair of entities with bidirectional property edge between them
    living_Person.name.CloseTo.weight.Animal.name insert List(
      ("Ben", 7, "Rex"),
      ("Kim", 6, "Zip")
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    living_Person.name_("Kim").CloseTo.weight.Animal.name.get === List((6, "Zip"))

    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((6, "Kim"))
  }

  "multiple existing" in new Setup {

    val List(rex, zip) = living_Animal.name.insert("Rex", "Zip").eids

    // Insert 2 entities with bidirectional property edges to existing entities
    living_Person.name.CloseTo.weight.animal insert List(
      ("Ben", 7, rex),
      ("Kim", 6, zip)
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ben").CloseTo.weight.Animal.name.get === List((7, "Rex"))
    living_Person.name_("Kim").CloseTo.weight.Animal.name.get === List((6, "Zip"))

    living_Animal.name_("Rex").CloseTo.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Zip").CloseTo.weight.Person.name.get === List((6, "Kim"))
  }


  "nested new" in new Setup {

    // Insert molecules allow nested data structures. So we can conveniently
    // insert 2 entities each connected to 2 target entites via property edges
    living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name) insert List(
      ("Ben", List((7, "Gus"), (6, "Leo"))),
      ("Don", List((8, "Rex"), (9, "Zip")))
    )

    living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name).get.sortBy(_._1) === List(
      ("Ben", List((7, "Gus"), (6, "Leo"))),
      ("Don", List((8, "Rex"), (9, "Zip")))
    )

    living_Animal.name.CloseTo.*(living_CloseTo.weight.Person.name).get.sortBy(_._1) === List(
      ("Gus", List((7, "Ben"))),
      ("Leo", List((6, "Ben"))),
      ("Rex", List((8, "Don"))),
      ("Zip", List((9, "Don")))
    )

    // Can't save nested edges without including target entity
    (living_Person.name.CloseTo.*(living_CloseTo.weight).Animal.name insert List(
      ("Rex", List(7, 8), "Joe")
    ) must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noNestedEdgesWithoutTarget]  Nested edge ns `living_CloseTo` should link to " +
      s"target ns within the nested group of attributes."
  }

  "nested existing" in new Setup {

    val List(gus, leo, rex, zip) = living_Animal.name.insert("Gus", "Leo", "Rex", "Zip").eids

    // Insert 2 living_Persons and connect them with existing living_Persons
    living_Person.name.CloseTo.*(living_CloseTo.weight.animal) insert List(
      ("Ben", List((7, gus), (6, leo))),
      ("Don", List((8, rex), (9, zip)))
    )

    living_Person.name.CloseTo.*(living_CloseTo.weight.Animal.name).get.sortBy(_._1) === List(
      ("Ben", List((7, "Gus"), (6, "Leo"))),
      ("Don", List((8, "Rex"), (9, "Zip")))
    )

    living_Animal.name.CloseTo.*(living_CloseTo.weight.Person.name).get.sortBy(_._1) === List(
      ("Gus", List((7, "Ben"))),
      ("Leo", List((6, "Ben"))),
      ("Rex", List((8, "Don"))),
      ("Zip", List((9, "Don")))
    )
  }


  "1 large edge to new entity" in new Setup {

    living_Person.name
      .CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
      .Animal.name insert List(
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

    // All edge properties have been inserted in both directions:

    // Person -> Animal
    living_Person.name
      .CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
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
      .CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
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


  "multiple large edges to existing entities" in new Setup {

    val List(rex, zip) = living_Animal.name.insert("Rex", "Zip").eids

    // Insert new entity with property edges to multiple existing entities
    living_Person.name.CloseTo.*(living_CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
      .animal) insert List(
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
            , rex),
          (6
            , "atWork"
            , Set("Programming", "Molecule")
            , Set("diving")
            , Map("dart" -> 4, "running" -> 2)
            , "Respect"
            , List("Focus", "Wit")
            , zip)
        ))
    )

    // Rex is connect to Joe and Liz and they are in turn connected to Rex with the same edge properties

    // Person -> Animal
    living_Person.name.CloseTo.*(living_CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
      .Animal.name).get.sortBy(_._1) === List(
      ("Ben", List(
        (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Rex"),
        (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Zip")))
    )

    // Animal -> Person
    living_Animal.name.CloseTo.*(living_CloseTo
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._CloseTo
      .InCommon.*(living_Quality.name)._CloseTo
      .Person.name).get.sortBy(_._1) === List(
      ("Rex", List(
        (7, "inSchool", Set("Food", "Walking", "Travelling"), Set("climbing", "flying"), Map("baseball" -> 9, "golf" -> 7), "Love", List("Patience", "Humor"), "Ben"))),
      ("Zip", List(
        (6, "atWork", Set("Programming", "Molecule"), Set("diving"), Map("dart" -> 4, "running" -> 2), "Respect", List("Focus", "Wit"), "Ben")))
    )
  }
}
