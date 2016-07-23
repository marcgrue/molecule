package molecule.examples.gremlin.schema
import molecule.schema.definition._

@InOut(0, 5)
object ModernGraph1Definition {

  trait Person {
    val name = oneString
    val age  = oneInt

    // Normal (uni-directional) reference
    val software = many[Software]

    // Bidirectional self-reference
    val friends = manyBi[Person]
  }

  trait Software {
    val name = oneString
    val lang = oneString
  }
}