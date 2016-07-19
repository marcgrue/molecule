package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneOtherUpdate extends MoleculeSpec {


  "New edge and new target entity" in new Setup {

    val ben = Person.name("Ben").save.eid

    // New edge and new target entity
    // Update Ben with new friendship to new Rex
    Person(ben).Favorite.weight(7).Animal.name("Rex").update

    Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
  }


  "New edge with reference to existing target entity" in new Setup {

    val List(ben, benRex, rexBen, rex) = Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids
    val zip = Animal.name("Zip").save.eid

    Person.name_("Ben").Favorite.weight.Animal.name.get === List((5, "Rex"))
    Animal.name_("Rex").Favorite.weight.Person.name.get === List((5, "Ben"))
    Animal.name_("Zip").Favorite.weight.Person.name.get === List()

    // Ben now loves Zip
    Person(ben).Favorite.weight(9).animal(zip).update

    // Both bidirectional edges have been added from/to Ben
    Person.name_("Ben").Favorite.weight.Animal.name.get === List((9, "Zip"))
    Animal.name_("Rex").Favorite.weight.Person.name.get === List()
    Animal.name_("Zip").Favorite.weight.Person.name.get === List((9, "Ben"))
  }


  "retract edge" in new Setup {

    val List(ben, benRex, rexBen, rex) = Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids

    Person.name_("Ben").Favorite.weight.Animal.name.get === List((5, "Rex"))
    Animal.name_("Rex").Favorite.weight.Person.name.get === List((5, "Ben"))

    // Retract edge
    benRex.retract

    // Rex got another owner
    Person.name_("Ben").Favorite.weight.Animal.name.get === List()
    Person.name_("Rex").Favorite.weight.Animal.name.get === List()
  }


  "retract base/target entity" in new Setup {

    val List(ben, benRex, rexBen, rex) = Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids

    // Retract base entity with single edge
    rex.retract

    // Ben still exists, Rex is gone
    Person.name("Ben").get === List("Ben")
    Person.name("Rex").get === List()

    Person.name("Ben").Favorite.weight.Animal.name.get === List()
    Person.name("Rex").Favorite.weight.Animal.name.get === List()
  }
}
