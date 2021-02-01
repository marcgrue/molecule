package molecule.tests.core.schemaDef.dataModel

import molecule.core._1_dataModel.data.model._

@InOut(0, 3)
object Schema1DataModel {

  trait Person {
    val name = oneString
  }
}