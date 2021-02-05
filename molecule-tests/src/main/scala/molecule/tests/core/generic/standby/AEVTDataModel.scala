package molecule.tests.core.generic.standby

import molecule.core.data.model._

@InOut(0, 7)
object AEVTDataModel {

  trait AEVT {
    val e         = oneLong.doc("Entity id")
    val a         = oneString.doc("Attribute name")
    val v         = oneAny.doc("Value")
    val t         = oneLong.doc("Transaction time t")
    val tx        = oneLong.doc("Transaction entity id")
    val txInstant = oneDate.doc("Transaction time as java.util.Date")
    val op        = oneBoolean.doc("Transaction operation (add: True or retract: False")
  }
}
