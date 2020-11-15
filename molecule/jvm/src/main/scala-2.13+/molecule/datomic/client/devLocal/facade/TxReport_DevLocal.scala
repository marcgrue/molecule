package molecule.datomic.client.devLocal.facade

import java.util.{Date, List => jList, Map => jMap}
import java.util
import datomic.{Database, _}
import datomic.Connection.TEMPIDS
import datomic.db.Datum
import molecule.core.ast.transactionModel.{Statement, _}
import molecule.core.util.Debug
import molecule.datomic.base.facade.TxReport
import datomicScala.client.api.sync.{TxReport => clientTxReport}
import scala.jdk.CollectionConverters._

/** Facade to Datomic transaction report with convenience methods to access tx data. */
case class TxReport_DevLocal(
//  rawTxReport: jMap[_, _],
  clientTxReport: clientTxReport,
  stmtss: Seq[Seq[Statement]] = Nil
) extends TxReport {

  /** Get List of affected entity ids from transaction */
  def eids: List[Long] = {
//    if (stmtss.isEmpty) {
//      //    val datoms: util.Iterator[_] = rawTxReport.get(Connection.TX_DATA).asInstanceOf[jList[_]].iterator
//      val datoms                   = clientTxReport.txData.iterator
//      var a      = Array.empty[Long]
//      var i      = 0
//      datoms.next() // skip first transaction time datom
//      while (datoms.hasNext) {
//        a = a :+ datoms.next.asInstanceOf[Datom].e().asInstanceOf[Long]
//        i += 1
//      }
//      a.toList.distinct
//
//    } else {
//      val tempIds         = stmtss.flatten.collect {
//        case Add(e, _, _, _) if e.toString.take(6) == "#db/id" => e
//        case Add(_, _, v, _) if v.toString.take(6) == "#db/id" => v
//      }.distinct
//      val txTtempIds: Any = rawTxReport.get(Connection.TEMPIDS)
//      val dbAfter   : Any = rawTxReport.get(Connection.DB_AFTER).asInstanceOf[Database]
//      tempIds.map(tempId =>
//        datomic.Peer.resolveTempid(dbAfter, txTtempIds, tempId).asInstanceOf[Long]
//      ).distinct.toList
//    }

    val datoms                   = clientTxReport.txData.iterator
    var a      = Array.empty[Long]
    var i      = 0
    datoms.next() // skip first transaction time datom
    while (datoms.hasNext) {
      a = a :+ datoms.next.asInstanceOf[Datom].e().asInstanceOf[Long]
      i += 1
    }
    a.toList.distinct
  }

//  private def txDataRaw: List[Datum] = {
//    rawTxReport.get(Connection.TX_DATA)
//      .asInstanceOf[jList[_]].asScala.toList.asInstanceOf[List[Datum]]
//  }

  private def datom2string(d: datomic.db.Datum) =
    s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"

  def debug: Unit = Debug("TxReport", 1)(1, stmtss, clientTxReport)

//  override def toString =
//    s"""TxReport {
//       |  dbBefore       : $dbBefore
//       |  dbBefore.basisT: ${dbBefore.basisT}
//       |  dbAfter        : $dbAfter
//       |  dbAfter.basisT : ${dbAfter.basisT}
//       |  txData         : ${txDataRaw.map(datom2string).mkString(",\n                   ")}
//       |  tempids        : ${rawTxReport.get(TEMPIDS).asInstanceOf[AnyRef]}
//       |}""".stripMargin


  def eid: Long = eids.head

  /** Get database value before transaction. */
//  def dbBefore: Database = rawTxReport.get(Connection.DB_BEFORE).asInstanceOf[Database]
//  def dbBefore: Database = clientTxReport.dbBefore
  def dbBefore: Database = ???

  /** Get database value after transaction. */
//  def dbAfter: Database = rawTxReport.get(Connection.DB_AFTER).asInstanceOf[Database]
  def dbAfter: Database = ???

  /** Get transaction time t. */
  def t: Long = dbAfter.basisT()

  /** Get transaction entity id (Long). */
  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  /** Get transaction entity (datomic.Entity). */
  def txE: datomic.Entity = dbAfter.entity(tx)

  /** Get transaction instant (Date). */
  def inst: Date = txE.get(":db/txInstant").asInstanceOf[Date]
}
