package molecule.facade

import java.util.{Date, List => jList, Map => jMap}

import datomic._
import datomic.Database
import molecule.ast.transaction.{Statement, _}
import molecule.util._

import scala.collection.JavaConverters._

/** Facade to Datomic transaction report facade with convenience methods to access tx data. */
case class TxReport(txResult: jMap[_, _], stmtss: Seq[Seq[Statement]] = Nil) {

  /** Get List of affected entity ids from transaction */
  def eids: List[Long] = {
    val tempIds = stmtss.flatten.collect {
      case Add(e, _, _, meta) if e.toString.take(6) == "#db/id" => e
      case Add(_, _, v, meta) if v.toString.take(6) == "#db/id" => v
    }.distinct
    val txTtempIds = txResult.get(Connection.TEMPIDS)
    val dbAfter = txResult.get(Connection.DB_AFTER).asInstanceOf[Database]
    val ids: Seq[Long] = tempIds.map(tempId => datomic.Peer.resolveTempid(dbAfter, txTtempIds, tempId).asInstanceOf[Long]).distinct
    ids.toList
  }

  /** Get Set of affected entity ids from transaction. */
  def eidSet: Set[Long] = eids.toSet

  /** Convenience method to get last affected entity id from transaction.
    *
    * Often useful when you know only one entity was affected:
    * {{{
    *   val benId = Person.name("Ben").eid
    *
    *   // We could have said
    *   val lizId = Person.name("Liz").eids.head
    * }}}
    *
    * @return
    */
  def eid: Long = eids.head

  /** Get database value before transaction. */
  def dbBefore: Database = txResult.get(Connection.DB_BEFORE).asInstanceOf[Database]

  /** Get database value after transaction. */
  def dbAfter: Database = txResult.get(Connection.DB_AFTER).asInstanceOf[Database]

  /** Get transaction time t. */
  def t: Long = dbAfter.basisT()

  /** Get transaction entity id (Long). */
  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  /** Get transaction entity (datomic.Entity). */
  def txE: datomic.Entity = dbAfter.entity(tx)

  /** Get transaction instant (Date). */
  def inst: Date = txE.get(":db/txInstant").asInstanceOf[Date]
}
