package molecule.coretests.generic.schema
import molecule.schema.definition._

@InOut(0, 22)
object DatomDefinition {

  trait Datom {
    val e         = oneLong.doc("Entity id")
    val ns        = oneString.doc("Full namespace name including partition prefix if partitions are defined")
    val a         = oneString.doc("Attribute name")
    val v         = oneAny.doc("Value")
    val t         = oneLong.doc("Transaction time t")
    val tx        = oneLong.doc("Transaction entity id")
    val txInstant = oneDate.doc("Transaction time as java.util.Date")
    val op        = oneBoolean.doc("Transaction operation (add: True or retract: False")
  }
}
