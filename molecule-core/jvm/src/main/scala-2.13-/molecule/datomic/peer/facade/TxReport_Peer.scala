package molecule.datomic.peer.facade

import java.util.{Date, List => jList, Map => jMap}
import datomic.Connection.TEMPIDS
import datomic.db.{Datum, DbId}
import datomic.{Database, _}
import molecule.datomic.base.ast.transactionModel._
import molecule.datomic.base.facade.TxReport
import molecule.datomic.base.facade.exception.DatomicFacadeException
import molecule.datomic.base.util.Inspect
import scala.collection.JavaConverters._

/** Datomic TxReport facade for peer api.
 *
 * @param rawTxReport
 * @param stmtss
 */
case class TxReport_Peer(
  rawTxReport: jMap[_, _],
  stmtss: Seq[Seq[Statement]] = Nil
) extends TxReport {

  lazy val eids: List[Long] = {
    val allIds = {
      val datoms = rawTxReport.get(Connection.TX_DATA).asInstanceOf[jList[_]].iterator
      var ids    = Array.empty[Long]
      var i      = 0
      datoms.next() // skip first transaction time datom
      while (datoms.hasNext) {
        val datom = datoms.next.asInstanceOf[Datom]
        if (datom.added()) // only asserted datoms
          ids = ids :+ datom.e().asInstanceOf[Long]
        i += 1
      }
      ids.toList
    }
    if (stmtss.isEmpty) {
      allIds.distinct
    } else {
      val assertStmts = stmtss.flatten.filterNot(_.isInstanceOf[RetractEntity])

      //      println("-------------------------------------------")
      //      txDataRaw.map(datom2string) foreach println
      //      println("--------")
      //      allIds foreach println
      //      println("--------")
      //      stmtss foreach println
      //      println("--------")
      //      assertStmts foreach println

      if (allIds.size != assertStmts.size)
        throw new DatomicFacadeException(
          s"Unexpected different counts of ${allIds.size} ids and ${assertStmts.size} stmts."
        )
      val resolvedIds = assertStmts.zip(allIds).collect {
        case (Add(_: DbId, _, _, _), eid)      => eid
        case (Add("datomic.tx", _, _, _), eid) => eid
      }.distinct.toList
      resolvedIds
    }
  }

  private lazy val txDataRaw: List[Datum] =
    rawTxReport.get(Connection.TX_DATA)
      .asInstanceOf[jList[_]].asScala.toList.asInstanceOf[List[Datum]]

  private def datom2string(d: datomic.db.Datum) =
    s"[${d.e}   ${d.a}   ${d.v}       ${d.tx}  ${d.added()}]"

  def inspect: Unit = Inspect("TxReport", 1)(1, stmtss, txDataRaw)

  override def toString =
    s"""TxReport {
       |  dbBefore  : $dbBefore
       |  dbBefore.t: ${dbBefore.basisT}
       |  dbAfter   : $dbAfter
       |  dbAfter.t : ${dbAfter.basisT}
       |  txData    : ${txDataRaw.map(datom2string).mkString(",\n              ")}
       |  tempids   : ${rawTxReport.get(TEMPIDS).asInstanceOf[AnyRef]}
       |  eids      : $eids
       |}""".stripMargin

  lazy val eid: Long = eids.head

  /** Get database value before transaction. */
  lazy val dbBefore: Database = rawTxReport.get(Connection.DB_BEFORE).asInstanceOf[Database]

  /** Get database value after transaction. */
  lazy val dbAfter: Database = rawTxReport.get(Connection.DB_AFTER).asInstanceOf[Database]

  lazy val t: Long = dbAfter.basisT

  lazy val tx: Long = Peer.toTx(t).asInstanceOf[Long]

  lazy val inst: Date = dbAfter.entity(tx).get(":db/txInstant").asInstanceOf[Date]
}
