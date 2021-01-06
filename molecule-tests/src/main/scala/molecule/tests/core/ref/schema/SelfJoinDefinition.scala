package molecule.tests.core.ref.schema

import molecule.core.schema.definition._

@InOut(0, 5)
object SelfJoinDefinition {

  trait Person {
    val name  = oneString
    val nameL = mapString
    val age   = oneInt
    val likes = many[Score]
  }

  trait Score {
    val beverage = oneString
    val rating   = oneInt
  }
}