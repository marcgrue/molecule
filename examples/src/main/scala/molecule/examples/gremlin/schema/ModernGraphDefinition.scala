package molecule.examples.gremlin.schema
import molecule.schema.definition._

@InOut(0, 5)
object ModernGraphDefinition {

  // Vertex namespace
  object Person extends Person
  trait Person {
    val name    = oneString
    val age     = oneInt

    // Normal reference to a namespace
    val created = many[Created]

    // Reference to property edge
    val knows: AnyRef   = manyBiEdge[Knows.person.type]


    // Bidirectional card-many ref (self-reference)
    val friends = manyBi[Person]
  }

  trait Software {
    val name = oneString
    val lang = oneString
  }

  // Property edge namespace
  object Knows extends Knows
  trait Knows {

    val person: AnyRef = target[Person.friends.type]


    val weight = oneDouble
  }

  trait Created {
    val software = one[Software]
    val weight   = oneDouble
  }
}