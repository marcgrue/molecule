package molecule.datomic.base.ast
import java.util.{Date, List => jList}

private[molecule] object tempDb {

  sealed trait TxType
  case class TxDate(d: Date) extends TxType
  case class TxLong(t: Long) extends TxType

  sealed trait TempDb
  case class AsOf(tx: TxType) extends TempDb
  case class Since(tx: TxType) extends TempDb
  case class With(tx: jList[jList[_]]) extends TempDb
  case object History extends TempDb
}
