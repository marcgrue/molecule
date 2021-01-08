package molecule.tests.core.generic.schema

import molecule.core.data.model._

@InOut(0, 8)
object LogDefinition {

  trait Log {
    val dummy = oneInt
  }
}
