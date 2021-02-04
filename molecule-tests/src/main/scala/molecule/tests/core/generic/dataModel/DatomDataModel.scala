package molecule.tests.core.generic.dataModel

import molecule.core.data.model._

@InOut(3, 22)
object DatomDataModel {

  trait Datom {
    val e         = oneLong.doc("Entity id")
    val a         = oneString.doc("Attribute name")
    val v         = oneAny.doc("Value")
    val t         = oneLong.doc("Transaction time t")
    val tx        = oneLong.doc("Transaction entity id")
    val txInstant = oneDate.doc("Transaction time as java.util.Date")
    val op        = oneBoolean.doc("Transaction operation (add: True or retract: False")
  }
}
