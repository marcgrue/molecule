package molecule.coretests.schemaDef.schema

import molecule.core.schema.definition._

@InOut(0, 3)
object Schema1Definition {

  trait Person {
    val name = oneString
  }
}