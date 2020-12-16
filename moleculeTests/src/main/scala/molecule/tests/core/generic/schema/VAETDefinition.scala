package molecule.tests.core.generic.schema

import molecule.core.schema.definition._

@InOut(0, 8)
object VAETDefinition {

  trait VAET {
    val dummy = oneInt
  }
}
