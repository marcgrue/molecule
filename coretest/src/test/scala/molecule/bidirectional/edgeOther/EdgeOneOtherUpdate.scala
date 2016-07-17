package molecule.bidirectional.edgeOther

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneOtherUpdate extends MoleculeSpec {


  "New edge and new target entity" in new Setup {

    val ben = living_Person.name("Ben").save.eid

    // New edge and new target entity
    // Update Ben with new friendship to new Rex
    living_Person(ben).Favorite.weight(7).Animal.name("Rex").update

    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((7, "Rex"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((7, "Ben"))
  }


  "New edge with reference to existing target entity" in new Setup {

    val List(ben, benRex, rexBen, rex) = living_Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids
    val zip = living_Animal.name("Zip").save.eid

    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((5, "Rex"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((5, "Ben"))
    living_Animal.name_("Zip").Favorite.weight.Person.name.get === List()

    // Ben now loves Zip
    living_Person(ben).Favorite.weight(9).animal(zip).update

    // Both bidirectional edges have been added from/to Ben
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((9, "Zip"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List()
    living_Animal.name_("Zip").Favorite.weight.Person.name.get === List((9, "Ben"))
  }


  "retract edge" in new Setup {

    val List(ben, benRex, rexBen, rex) = living_Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids

    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List((5, "Rex"))
    living_Animal.name_("Rex").Favorite.weight.Person.name.get === List((5, "Ben"))

    // Retract edge
    benRex.retract

    // Rex got another owner
    living_Person.name_("Ben").Favorite.weight.Animal.name.get === List()
    living_Person.name_("Rex").Favorite.weight.Animal.name.get === List()
  }


  "retract base/target entity" in new Setup {

    val List(ben, benRex, rexBen, rex) = living_Person.name("Ben").Favorite.weight(5).Animal.name("Rex").save.eids

    // Retract base entity with single edge
    rex.retract

    // Ben still exists, Rex is gone
    living_Person.name("Ben").get === List("Ben")
    living_Person.name("Rex").get === List()

    living_Person.name("Ben").Favorite.weight.Animal.name.get === List()
    living_Person.name("Rex").Favorite.weight.Animal.name.get === List()
  }
}
