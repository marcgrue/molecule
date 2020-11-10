package molecule.coretests.bidirectionals.edgeOther

import molecule.core.util._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.datomic.peer.api.in1_out3._

class EdgeOneOtherUpdate extends MoleculeSpec {

  class setup extends Setup {
    val ann    = Person.name("Ann").save.eid
    
    val favoriteAnimalOf = m(Person.name_(?).Favorite.weight.Animal.name)
    val favoritePersonOf = m(Animal.name_(?).Favorite.weight.Person.name)
  }


  "apply edge to new target" in new setup {

    // New edge and new target entity
    Person(ann).Favorite.weight(5).Animal.name("Rex").update

    // Ann and Rex favorite each other
    favoriteAnimalOf("Ann").get === List((5, "Rex"))
    favoritePersonOf("Rex").get === List((5, "Ann"))
    favoritePersonOf("Zup").get === List() // Zup doesn't exist yet

    // Ann now favorite Zup
    Person(ann).Favorite.weight(8).Animal.name("Zup").update

    // Both bidirectional edges have been added from/to Ann
    favoriteAnimalOf("Ann").get === List((8, "Zup"))
    favoritePersonOf("Rex").get === List()
    favoritePersonOf("Zup").get === List((8, "Ann"))

    // Even though Ann now favorite Zup, Rex still exists
    Person.name.get.sorted === List("Ann")
    Animal.name.get.sorted === List("Rex", "Zup")
  }


  "apply edge to existing target" in new setup {

    val List(rex, zup) = Animal.name.insert("Rex", "Zup").eids

    Person(ann).Favorite.weight(5).animal(rex).update

    favoriteAnimalOf("Ann").get === List((5, "Rex"))
    favoritePersonOf("Rex").get === List((5, "Ann"))
    favoritePersonOf("Zup").get === List()

    // Ann now favorite Zup
    Person(ann).Favorite.weight(8).animal(zup).update

    favoriteAnimalOf("Ann").get === List((8, "Zup"))
    favoritePersonOf("Rex").get === List()
    favoritePersonOf("Zup").get === List((8, "Ann"))
  }


  "retract edge" in new setup {

    val List(_, annRex, _, _) = Person.name("Ann").Favorite.weight(5).Animal.name("Rex").save.eids

    favoriteAnimalOf("Ann").get === List((5, "Rex"))
    favoritePersonOf("Rex").get === List((5, "Ann"))

    // Retract edge
    annRex.retract

    // Divorce complete

    Person.name.Favorite.weight.Animal.name.get.sorted === List()
  }


  "retract base/target entity" in new setup {

    val List(_, _, _, rex) = Person.name("Ann").Favorite.weight(5).Animal.name("Rex").save.eids

    favoriteAnimalOf("Ann").get === List((5, "Rex"))
    favoritePersonOf("Rex").get === List((5, "Ann"))

    // Retract base entity with single edge
    rex.retract

    // Ann becomes widow
    Person.name("Ann").get === List("Ann")
    Animal.name("Rex").get === List()

    favoriteAnimalOf("Ann").get === List()
    favoritePersonOf("Rex").get === List()
  }
}
