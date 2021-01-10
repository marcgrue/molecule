package molecule.tests.core.schemaDef.dataModel

import molecule.core.data.model._

@InOut(0, 3)
object Schema1DataModel {

  trait Person {
    val name = oneString
  }
}