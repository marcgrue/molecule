package molecule.facade

import java.util.{Date, List => jList, Map => jMap}

import datomic._
import datomic.Database
import molecule.ast.transaction.{Statement, _}
import molecule.util._

import scala.collection.JavaConverters._


case class TxReport(txResult: jMap[_, _], stmtss: Seq[Seq[Statement]] = Nil) {

  def eids: List[Long] = {
    val txData = txResult.get(Connection.TX_DATA)

    // Omit first transaction datom
    val datoms = txData.asInstanceOf[java.util.Collection[Datom]].asScala.toList.tail

    val tempIds = stmtss.flatten.collect {
      case Add(e, _, _, meta) if e.toString.take(6) == "#db/id" => e
      case Add(_, _, v, meta) if v.toString.take(6) == "#db/id" => v
    }.distinct

    val txTtempIds = txResult.get(Connection.TEMPIDS)
    val dbAfter = txResult.get(Connection.DB_AFTER).asInstanceOf[Database]
    val ids: Seq[Long] = tempIds.map(tempId => datomic.Peer.resolveTempid(dbAfter, txTtempIds, tempId).asInstanceOf[Long]).distinct
    ids.toList
  }

  def eidSet: Set[Long] = eids.toSet
  def eid: Long = eids.head
  def dbBefore: Database = txResult.get(Connection.DB_BEFORE).asInstanceOf[Database]
  def dbAfter: Database = txResult.get(Connection.DB_AFTER).asInstanceOf[Database]

  def t: Long = dbAfter.basisT()
  def tx: Long = Peer.toTx(t).asInstanceOf[Long]
//  def tx: Object = Peer.toTx(t)
  def txE: datomic.Entity = dbAfter.entity(tx)
  def inst: Date = txE.get(":db/txInstant").asInstanceOf[Date]
}
