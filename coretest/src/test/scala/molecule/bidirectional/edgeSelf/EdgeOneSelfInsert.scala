package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneSelfInsert extends MoleculeSpec {


  "1 new" in new Setup {

    // Insert 1 pair of entities with bidirectional property edge between them
    living_Person.name.Loves.weight.Person.name.insert("Ann", 7, "Ben")

    // Bidirectional property edge has been inserted
    living_Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Ben"))
    living_Person.name_("Ben").Loves.weight.Person.name.get === List((7, "Ann"))
  }

  "1 existing" in new Setup {

    val ben = living_Person.name.insert("Ben").eid

    // Insert Ann with bidirectional property edge to existing Ben
    living_Person.name.Loves.weight.person.insert("Ann", 7, ben)

    // Bidirectional property edge has been inserted
    living_Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Ben"))
    living_Person.name_("Ben").Loves.weight.Person.name.get === List((7, "Ann"))
  }


  "multiple new" in new Setup {

    // Insert 2 pair of entities with bidirectional property edge between them
    living_Person.name.Loves.weight.Person.name insert List(
      ("Ann", 7, "Joe"),
      ("Ben", 6, "Tim")
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Joe"))
    living_Person.name_("Ben").Loves.weight.Person.name.get === List((6, "Tim"))
    living_Person.name_("Joe").Loves.weight.Person.name.get === List((7, "Ann"))
    living_Person.name_("Tim").Loves.weight.Person.name.get === List((6, "Ben"))
  }

  "multiple existing" in new Setup {

    val List(joe, tim) = living_Person.name.insert("Joe", "Tim").eids

    // Insert 2 entities with bidirectional property edges to existing entities
    living_Person.name.Loves.weight.person insert List(
      ("Ann", 7, joe),
      ("Ben", 6, tim)
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Joe"))
    living_Person.name_("Ben").Loves.weight.Person.name.get === List((6, "Tim"))
    living_Person.name_("Joe").Loves.weight.Person.name.get === List((7, "Ann"))
    living_Person.name_("Tim").Loves.weight.Person.name.get === List((6, "Ben"))
  }


  "1 large edge to new entity" in new Setup {

    living_Person.name
      .Loves
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Loves
      .InCommon.*(living_Quality.name)._Loves
      .Person.name insert List(
      ("Ben"
        , 7
        , "inSchool"
        , Set("Food", "Walking", "Travelling")
        , Set("climbing", "flying")
        , Map("baseball" -> 9, "golf" -> 7)
        , "Love"
        , List("Patience", "Humor")
        , "Joe")
    )

    living_Person.name
      .Loves
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Loves
      .InCommon.*(living_Quality.name)._Loves
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
}
