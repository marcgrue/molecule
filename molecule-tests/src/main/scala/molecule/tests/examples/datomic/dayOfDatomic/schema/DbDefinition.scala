package molecule.tests.examples.datomic.dayOfDatomic.schema

import molecule.core.data.model._

@InOut(3, 6)
object DbDefinition {

  trait Db {
    val valueType = oneString
  }
}