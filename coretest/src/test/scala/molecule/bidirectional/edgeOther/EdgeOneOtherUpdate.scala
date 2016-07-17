//package molecule.bidirectional.edgeOther
//
//import molecule._
//import molecule.bidirectional.Setup
//import molecule.bidirectional.dsl.bidirectional._
//import molecule.util._
//
//class EdgeOneOtherUpdate extends MoleculeSpec {
//
//
//  "New edge and new target entity" in new Setup {
//
//    val ann = living_Person.name("Ann").save.eid
//
//    // New edge and new target entity
//    // Update Ann with new friendship to new Ben
//    living_Person(ann).Favorite.weight(7).Person.name("Ben").update
//
//    living_Person.name_("Ann").Favorite.weight.Person.name.get === List((7, "Ben"))
//    living_Person.name_("Ben").Favorite.weight.Person.name.get === List((7, "Ann"))
//  }
//
//
//  "New edge with reference to existing target entity" in new Setup {
//
//    val List(ann, annBen, benAnn, ben) = living_Person.name("Ann").Favorite.weight(5).Person.name("Ben").save.eids
//    val joe = living_Person.name("Joe").save.eid
//
//    living_Person.name_("Ann").Favorite.weight.Person.name.get === List((5, "Ben"))
//    living_Person.name_("Ben").Favorite.weight.Person.name.get === List((5, "Ann"))
//    living_Person.name_("Joe").Favorite.weight.Person.name.get === List()
//
//    // Ann now loves Joe
//    living_Person(ann).Favorite.weight(9).person(joe).update
//
//    // Both bidirectional edges have been added from/to Ann
//    living_Person.name_("Ann").Favorite.weight.Person.name.get === List((9, "Joe"))
//    living_Person.name_("Ben").Favorite.weight.Person.name.get === List()
//    living_Person.name_("Joe").Favorite.weight.Person.name.get === List((9, "Ann"))
//  }
//
//
//  "retract edge" in new Setup {
//
//    val List(ann, annBen, benAnn, ben) = living_Person.name("Ann").Favorite.weight(5).Person.name("Ben").save.eids
//
//    living_Person.name_("Ann").Favorite.weight.Person.name.get === List((5, "Ben"))
//    living_Person.name_("Ben").Favorite.weight.Person.name.get === List((5, "Ann"))
//
//    // Retract edge
//    annBen.retract
//
//    // Divorce complete
//    living_Person.name_("Ann").Favorite.weight.Person.name.get === List()
//    living_Person.name_("Ben").Favorite.weight.Person.name.get === List()
//  }
//
//
//  "retract base/target entity" in new Setup {
//
//    val List(ann, annBen, benAnn, ben) = living_Person.name("Ann").Favorite.weight(5).Person.name("Ben").save.eids
//
//    // Retract base entity with single edge
//    ben.retract
//
//    // Ann becomes widow
//    living_Person.name("Ann").get === List("Ann")
//    living_Person.name("Ben").get === List()
//
//    living_Person.name("Ann").Favorite.weight.Person.name.get === List()
//    living_Person.name("Ben").Favorite.weight.Person.name.get === List()
//  }
//}
