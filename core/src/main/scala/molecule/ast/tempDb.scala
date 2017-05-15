package molecule.ast
import java.util.{Date, List => jList, Map => jMap}

import datomic.Database

object tempDb {

  sealed trait TxType
  case class TxDate(d: Date) extends TxType
  case class TxLong(t: Long) extends TxType

  sealed trait TempDb
  case class AsOf(tx: TxType) extends TempDb
  case class Since(tx: TxType) extends TempDb
  case class With(tx: jList[jList[_]]) extends TempDb
  case object History extends TempDb
  case class Using(db: Database) extends TempDb
}
