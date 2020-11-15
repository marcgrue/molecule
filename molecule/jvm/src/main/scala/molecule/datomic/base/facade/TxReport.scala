package molecule.datomic.base.facade

import java.util.{Date, List => jList, Map => jMap}
import datomic.{Database, _}
import datomic.Connection.TEMPIDS
import datomic.db.Datum
import molecule.core.ast.transactionModel.{Statement, _}
import molecule.core.util.Debug
import scala.jdk.CollectionConverters._

/** Facade to Datomic transaction report with convenience methods to access tx data. */
trait TxReport {

  /** Get List of affected entity ids from transaction */
  def eids: List[Long]

  /** Get Set of affected entity ids from transaction. */
  def eidSet: Set[Long] = eids.toSet

  /** Convenience method to get single affected entity id from transaction.
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
  def eid: Long

  def debug: Unit

  /** Get database value before transaction. */
  def dbBefore: Database

  /** Get database value after transaction. */
  def dbAfter: Database

  /** Get transaction time t. */
  def t: Long

  /** Get transaction entity id (Long). */
  def tx: Long

  /** Get transaction entity (datomic.Entity). */
  def txE: datomic.Entity

  /** Get transaction instant (Date). */
  def inst: Date
}
