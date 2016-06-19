package molecule.examples.gremlin.schema

import molecule.dsl.schemaDefinition._


@InOut(0, 4)
object ModernGraphDefinition {

  // Vertex namespaces
  trait Person {
    val name    = oneString
    val age     = oneInt
    val knows   = many[Knows]
    val created = many[Created]
  }
  trait Software {
    val name = oneString
    val lang = oneString
  }

  // Property edges
  trait Knows {
    val person = one[Person]
    val weight = oneDouble
  }
  trait Created {
    val software = one[Software]
    val weight   = oneDouble
  }
}