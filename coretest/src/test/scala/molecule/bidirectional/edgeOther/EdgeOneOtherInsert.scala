package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneOtherInsert extends MoleculeSpec {


  "1 new" in new Setup {

    // Insert 1 pair of entities with bidirectional property edge between them
    living_Person.name.Favorite.weight.Animal.name.insert("Ben", 7, "Rex")

    // Bidirectional property edge has been inserted
    // Note how we query differently from each end
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
  }

  "1 existing" in new Setup {

    val ben = living_Person.name.insert("Ben").eid

    // We can insert from the other end as well
    living_Animal.name.Favorite.weight.person.insert("Rex", 7, ben)

    // Bidirectional property edge has been inserted
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
  }


  "multiple new" in new Setup {

    // Insert 2 pair of entities with bidirectional property edge between them
    living_Person.name.Favorite.weight.Animal.name insert List(
      ("Ben", 7, "Rex"),
      ("Kim", 6, "Zip")
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    living_Person.name_("Kim").Favorite.weight.Animal.name.get === List((6, "Zip"))

    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Zip").Favorite.weight.Person.name.get === List((6, "Kim"))
  }

  "multiple existing" in new Setup {

    val List(rex, zip) = living_Animal.name.insert("Rex", "Zip").eids

    // Insert 2 entities with bidirectional property edges to existing entities
    living_Person.name.Favorite.weight.animal insert List(
      ("Ben", 7, rex),
      ("Kim", 6, zip)
    )

    // Bidirectional property edges have been inserted
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    living_Person.name_("Kim").Favorite.weight.Animal.name.get === List((6, "Zip"))

    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
    living_Animal.name_("Zip").Favorite.weight.Person.name.get === List((6, "Kim"))
  }


  "1 large edge to new entity" in new Setup {

    living_Person.name
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(living_Quality.name)._Favorite
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
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(living_Quality.name)._Favorite
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
      .Favorite
      .weight
      .howWeMet
      .commonInterests
      .commonLicences
      .commonScores
      .CoreQuality.name._Favorite
      .InCommon.*(living_Quality.name)._Favorite
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
