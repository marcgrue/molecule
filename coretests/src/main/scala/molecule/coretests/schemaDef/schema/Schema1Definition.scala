package molecule.coretests.schemaDef.schema

import molecule.schema.definition._

@InOut(0, 3)
object Schema1Definition {

  trait Person {
    val name = oneString
  }
}