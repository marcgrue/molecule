package molecule.coretests.bidirectionals.edgeSelf

import molecule.api._
import molecule.coretests.bidirectionals.Setup
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.util._

class EdgeOneSelfUpdate extends MoleculeSpec {

  class setup extends Setup {
    val loveOf = m(Person.name_(?).Loves.weight.Person.name)
    val ann    = Person.name("Ann").save.eid
  }


  "apply edge to new target" in new setup {

    // New edge and new target entity
    Person(ann).Loves.weight(5).Person.name("Ben").update

    // Ann and Ben loves each other
    loveOf("Ann").get === List((5, "Ben"))
    loveOf("Ben").get === List((5, "Ann"))
    loveOf("Joe").get === List() // Joe doesn't exist yet

    // Ann now loves Joe
    Person(ann).Loves.weight(8).Person.name("Joe").update

    // Both bidirectional edges have been added from/to Ann
    loveOf("Ann").get === List((8, "Joe"))
    loveOf("Ben").get === List()
    loveOf("Joe").get === List((8, "Ann"))

    // Even though Ann now loves Joe, Ben still exists
    Person.name.get.sorted === List("Ann", "Ben", "Joe")
  }


  "apply edge to existing target" in new setup {

    val List(ben, joe) = Person.name.insert("Ben", "Joe").eids

    Person(ann).Loves.weight(5).person(ben).update

    loveOf("Ann").get === List((5, "Ben"))
    loveOf("Ben").get === List((5, "Ann"))
    loveOf("Joe").get === List()

    // Ann now loves Joe
    Person(ann).Loves.weight(8).person(joe).update

    loveOf("Ann").get === List((8, "Joe"))
    loveOf("Ben").get === List()
    loveOf("Joe").get === List((8, "Ann"))
  }


  "retract edge" in new setup {

    val List(_, annBen, _, _) = Person.name("Ann").Loves.weight(5).Person.name("Ben").save.eids

    loveOf("Ann").get === List((5, "Ben"))
    loveOf("Ben").get === List((5, "Ann"))

    // Retract edge
    annBen.retract

    // Divorce complete
    loveOf("Ann").get === List()
    loveOf("Ben").get === List()
  }


  "retract base/target entity" in new setup {

    val List(_, _, _, ben) = Person.name("Ann").Loves.weight(5).Person.name("Ben").save.eids

    loveOf("Ann").get === List((5, "Ben"))
    loveOf("Ben").get === List((5, "Ann"))

    // Retract base entity with single edge
    ben.retract

    // Ann becomes widow
    Person.name("Ann").get === List("Ann")
    Person.name("Ben").get === List()

    loveOf("Ann").get === List()
    loveOf("Ben").get === List()
  }
}
