package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneSelfInsert extends MoleculeSpec {


  "1 new" in new Setup {

    // Insert 1 pair of entities with bidirectional property edge between them
    Person.name.Loves.weight.Person.name.insert("Ann", 7, "Ben")

    // Bidirectional property edge has been inserted
    Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Ben"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((7, "Ann"))
  }

  "1 existing" in new Setup {

    val ben = Person.name.insert("Ben").eid

    // Insert Ann with bidirectional property edge to existing Ben
    Person.name.Loves.weight.person.insert("Ann", 7, ben)

    // Bidirectional property edge has been inserted
    Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Ben"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((7, "Ann"))
  }


  "multiple new" in new Setup {

    // Insert 2 pair of entities with bidirectional property edge between them
    Person.name.Loves.weight.Person.name insert List(
      ("Ann", 7, "Joe"),
      ("Ben", 6, "Tim")
    )

    // Bidirectional property edges have been inserted
    Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Joe"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((6, "Tim"))
    Person.name_("Joe").Loves.weight.Person.name.get === List((7, "Ann"))
    Person.name_("Tim").Loves.weight.Person.name.get === List((6, "Ben"))
  }

  "multiple existing" in new Setup {

    val List(joe, tim) = Person.name.insert("Joe", "Tim").eids

    // Insert 2 entities with bidirectional property edges to existing entities
    Person.name.Loves.weight.person insert List(
      ("Ann", 7, joe),
      ("Ben", 6, tim)
    )

    // Bidirectional property edges have been inserted
    Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Joe"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((6, "Tim"))
    Person.name_("Joe").Loves.weight.Person.name.get === List((7, "Ann"))
    Person.name_("Tim").Loves.weight.Person.name.get === List((6, "Ben"))
  }


  "1 large edge to new entity" in new Setup {

    Person.name
      .Loves
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Loves
      .InCommon.*(Quality.name)._Loves
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

    Person.name
      .Loves
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Loves
      .InCommon.*(Quality.name)._Loves
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
