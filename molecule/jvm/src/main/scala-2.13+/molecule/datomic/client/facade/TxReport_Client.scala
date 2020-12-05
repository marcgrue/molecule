package molecule.datomic.client.facade

import java.util.Date
import datomic._
import datomicScala.client.api.sync.{Db, TxReport => clientTxReport}
import datomicScala.client.api.Datom
import molecule.core.ast.transactionModel._
import molecule.core.facade.exception.DatomicFacadeException
import molecule.core.util.Debug
import molecule.datomic.base.facade.TxReport
import scala.jdk.CollectionConverters._

case class TxReport_Client(
  clientTxReport: clientTxReport,
  stmtss: Seq[Seq[Statement]] = Nil
) extends TxReport {

  def eids: List[Long] = {
    val allIds = {
      val datoms = clientTxReport.txData.iterator
      var a      = Array.empty[Long]
      var i      = 0
      datoms.next() // skip first transaction time datom
      while (datoms.hasNext) {
        a = a :+ datoms.next.e
        i += 1
      }
      a.toList
    }
    if (stmtss.isEmpty) {
      allIds.distinct
    } else {
      val flattenStmts = stmtss.flatten
      if (allIds.size != flattenStmts.size)
        throw new DatomicFacadeException(
          s"Unexpected different counts of ${allIds.size} ids and ${flattenStmts.size} stmts."
        )
      val resolvedIds = flattenStmts.zip(allIds).collect {
        case (Add(tempId, _, _, _), eid) if tempId.toString.take(6) == "#db/id" => eid
        case (Add("datomic.tx", _, _, _), eid)                                  => eid
      }.distinct.toList
      resolvedIds
    }
  }

  private def txDataRaw: String = {
    clientTxReport.txData.iterator().asScala
      .map(datom2string)
      .mkString(",\n                   ")
  }

  private def datom2string(d: Datom) =
    s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added}]"

  def debug: Unit = Debug("TxReport", 1)(1, stmtss, clientTxReport)

  override def toString =
    s"""TxReport {
       |  dbBefore  : $dbBefore
       |  dbBefore.t: ${dbBefore.t}
       |  dbAfter   : $dbAfter
       |  dbAfter.t : ${dbAfter.t}
       |  txData    : $txDataRaw
       |  tempids   : ${clientTxReport.tempIds}
       |}""".stripMargin


  def eid: Long = eids.head

  /** Get database value before transaction. */
  def dbBefore: Db = clientTxReport.dbBefore

  /** Get database value after transaction. */
  def dbAfter: Db = clientTxReport.dbAfter

  def t: Long = dbAfter.t

  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  def inst: Date = clientTxReport.txData.iterator().next().v.asInstanceOf[Date]
}
