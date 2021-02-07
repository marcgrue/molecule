package molecule.tests.core.generic.dataModel

import molecule.core.data.model._

@InOut(3, 22)
object TxCountDataModel {

  trait TxCount {
    val db     = oneString.doc("Database name")
    val basisT = oneLong.doc("Datomic basis T")
  }
}
