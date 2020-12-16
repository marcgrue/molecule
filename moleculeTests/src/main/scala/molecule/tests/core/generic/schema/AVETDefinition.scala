package molecule.tests.core.generic.schema

import molecule.core.schema.definition._

@InOut(0, 8)
object AVETDefinition {

  trait AVET {
    val dummy = oneInt
  }
}
