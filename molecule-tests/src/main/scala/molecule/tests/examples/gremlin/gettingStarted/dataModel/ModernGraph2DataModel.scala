package molecule.tests.examples.gremlin.gettingStarted.dataModel

import molecule.core.data.model._

@InOut(0, 5)
object ModernGraph2DataModel {

  // Base entity
  object Person extends Person
  trait Person {
    val name = oneString
    val age  = oneInt

    // Start of bidirectional property edge
    val knows: AnyRef = manyBiEdge[Knows.person.type]

    // Normal uni-directional reference to a namespace
    val created = many[Created]
  }

  // Bidirectional property edge pointing back to Person
  object Knows extends Knows
  trait Knows {
    // Other end
    val person: AnyRef = target[Person.knows.type]

    // Property
    val weight = oneDouble
  }

  // Unidirectional "edge" pointing on to Software
  trait Created {
    val software = one[Software]
    val weight   = oneDouble
  }

  trait Software {
    val name = oneString
    val lang = oneString
  }
}