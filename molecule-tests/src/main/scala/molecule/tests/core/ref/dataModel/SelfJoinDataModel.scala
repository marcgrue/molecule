package molecule.tests.core.ref.dataModel

import molecule.core._1_dataModel.data.model._

@InOut(0, 5)
object SelfJoinDataModel {

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