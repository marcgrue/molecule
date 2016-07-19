package molecule.bidirectional.edgeSelf

import molecule._
import molecule.bidirectional.Setup
import molecule.bidirectional.dsl.bidirectional._
import molecule.util._

class EdgeOneSelfUpdate extends MoleculeSpec {


  "New edge and new target entity" in new Setup {

    val ann = Person.name("Ann").save.eid

    // New edge and new target entity
    // Update Ann with new friendship to new Ben
    Person(ann).Loves.weight(7).Person.name("Ben").update

    Person.name_("Ann").Loves.weight.Person.name.get === List((7, "Ben"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((7, "Ann"))
  }


  "New edge with reference to existing target entity" in new Setup {

    val List(ann, annBen, benAnn, ben) = Person.name("Ann").Loves.weight(5).Person.name("Ben").save.eids
    val joe = Person.name("Joe").save.eid

    Person.name_("Ann").Loves.weight.Person.name.get === List((5, "Ben"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((5, "Ann"))
    Person.name_("Joe").Loves.weight.Person.name.get === List()

    // Ann now loves Joe
    Person(ann).Loves.weight(9).person(joe).update

    // Both bidirectional edges have been added from/to Ann
    Person.name_("Ann").Loves.weight.Person.name.get === List((9, "Joe"))
    Person.name_("Ben").Loves.weight.Person.name.get === List()
    Person.name_("Joe").Loves.weight.Person.name.get === List((9, "Ann"))
  }


  "retract edge" in new Setup {

    val List(ann, annBen, benAnn, ben) = Person.name("Ann").Loves.weight(5).Person.name("Ben").save.eids

    Person.name_("Ann").Loves.weight.Person.name.get === List((5, "Ben"))
    Person.name_("Ben").Loves.weight.Person.name.get === List((5, "Ann"))

    // Retract edge
    annBen.retract

    // Divorce complete
    Person.name_("Ann").Loves.weight.Person.name.get === List()
    Person.name_("Ben").Loves.weight.Person.name.get === List()
  }


  "retract base/target entity" in new Setup {

    val List(ann, annBen, benAnn, ben) = Person.name("Ann").Loves.weight(5).Person.name("Ben").save.eids

    // Retract base entity with single edge
    ben.retract

    // Ann becomes widow
    Person.name("Ann").get === List("Ann")
    Person.name("Ben").get === List()

    Person.name("Ann").Loves.weight.Person.name.get === List()
    Person.name("Ben").Loves.weight.Person.name.get === List()
  }
}
