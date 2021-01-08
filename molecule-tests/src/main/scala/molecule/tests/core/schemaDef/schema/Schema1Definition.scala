package molecule.tests.core.schemaDef.schema

import molecule.core.data.model._

@InOut(0, 3)
object Schema1Definition {

  trait Person {
    val name = oneString
  }
}