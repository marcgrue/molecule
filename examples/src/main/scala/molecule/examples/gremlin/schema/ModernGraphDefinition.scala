package molecule.examples.gremlin.schema

import molecule.dsl.schemaDefinition._

@InOut(0, 5)
object ModernGraphDefinition {

  // Vertex namespace
  trait Person {
    val name    = oneString
    val age     = oneInt
    val created = many[Created]

    val knows0   = many[Person]
    val knows   = manyBi[Person]


    // Bidirectional card-many ref (self-reference)
    val friends = manyBi[Person]

    // Bidirectional card-one ref (self-reference)
    val spouse  = oneBi[Person]
    val spouses = manyBi[Person]
  }
//  object Person extends Person

  trait Software {
    val name = oneString
    val lang = oneString
  }

  // Property edge namespace
  trait Knows {
    val person = one[Person]
    val weight = oneDouble
  }
  object Knows extends Knows

  trait Created {
    val software = one[Software]
    val weight   = oneDouble
  }
}