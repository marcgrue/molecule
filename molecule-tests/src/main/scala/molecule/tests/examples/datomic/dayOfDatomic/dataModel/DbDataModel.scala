package molecule.tests.examples.datomic.dayOfDatomic.dataModel

import molecule.core._1_dataModel.data.model._

@InOut(3, 6)
object DbDataModel {

  trait Db {
    val valueType = oneString
  }
}