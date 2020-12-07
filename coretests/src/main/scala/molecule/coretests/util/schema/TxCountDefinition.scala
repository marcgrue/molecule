package molecule.coretests.util.schema

import molecule.core.schema.definition._

@InOut(0, 2)
object TxCountDefinition {

  trait TxCount {
    val db     = oneString.doc("Database observed.")
    val basisT = oneLong.doc("Holder of current db basis t for peer-server tests")
  }
}