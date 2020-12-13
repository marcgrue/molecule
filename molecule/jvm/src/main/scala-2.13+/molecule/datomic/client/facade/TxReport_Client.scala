package molecule.datomic.client.facade

import java.util.Date
import datomic.db.DbId
import datomic.Peer
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

  lazy val eids: List[Long] = {
    val allIds = {
      val datoms = clientTxReport.txData.iterator
      var ids    = Array.empty[Long]
      var i      = 0
      datoms.next() // skip first transaction time datom
      while (datoms.hasNext) {
        val datom = datoms.next
        if (datom.added) // only asserted datoms
          ids = ids :+ datom.e
        i += 1
      }
      ids.toList
    }
    if (stmtss.isEmpty) {
      allIds.distinct
    } else {
      val assertStmts = stmtss.flatten.filterNot(_.isInstanceOf[RetractEntity])

      //        println("-------------------------------------------")
      //        txDataRaw.map(datom2string) foreach println
      //        println("--------")
      //        allIds foreach println
      //        println("--------")
      //        stmtss foreach println
      //        println("--------")
      //        assertStmts foreach println

      if (allIds.size != assertStmts.size) {
        throw new DatomicFacadeException(
          s"Unexpected different counts of ${allIds.size} ids and ${assertStmts.size} stmts."
        )
      }
      val resolvedIds = assertStmts.zip(allIds).collect {
        case (Add(_: DbId, _, _, _), eid)      => eid
        case (Add("datomic.tx", _, _, _), eid) => eid
      }.distinct.toList
      resolvedIds
    }
  }

  private lazy val txDataRaw: List[Datom] = clientTxReport.txData.iterator().asScala.toList

  private def datom2string(d: Datom) =
    s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added}]"

  def debug: Unit = Debug("TxReport", 1)(1, stmtss, this)

  override def toString =
    s"""TxReport {
       |  dbBefore  : $dbBefore
       |  dbBefore.t: ${dbBefore.t}
       |  dbAfter   : $dbAfter
       |  dbAfter.t : ${dbAfter.t}
       |  txData    : ${txDataRaw.map(datom2string).mkString("\n              ")}
       |  tempids   : ${clientTxReport.tempIds}
       |  eids      : $eids
       |}""".stripMargin

  def printEidStats() = {
    s"""
       |
       |  txData    : ${txDataRaw.map(datom2string).mkString("\n              ")}
       |  tempids   : ${clientTxReport.tempIds}""".stripMargin
  }

  lazy val eid: Long = eids.head

  /** Get database value before transaction. */
  lazy val dbBefore: Db = clientTxReport.dbBefore

  /** Get database value after transaction. */
  lazy val dbAfter: Db = clientTxReport.dbAfter

  lazy val t: Long = dbAfter.t

  lazy val tx: Long = Peer.toTx(t).asInstanceOf[Long]

  lazy val inst: Date = clientTxReport.txData.iterator().next().v.asInstanceOf[Date]
}
