package molecule.facade

import java.util.{Date, List => jList, Map => jMap}
import clojure.lang.Keyword
import datomic.Connection.TEMPIDS
import datomic.db.Datum
import datomic.{Database, _}
import molecule.ast.model.NoValue
import molecule.ast.transactionModel.{Statement, _}
import molecule.util.Debug
import scala.collection.JavaConverters._

/** Facade to Datomic transaction report facade with convenience methods to access tx data. */
case class TxReport(rawTxReport: jMap[_, _], stmtss: Seq[Seq[Statement]] = Nil) {

  /** Get List of affected entity ids from transaction */
  def eids: List[Long] = if (stmtss.isEmpty) {
    val datoms = rawTxReport.get(Connection.TX_DATA).asInstanceOf[jList[_]].iterator
    var a = Array.empty[Long]
    var i = 0
    datoms.next() // skip first transaction time datom
    while (datoms.hasNext) {
      a = a :+ datoms.next.asInstanceOf[Datom].e().asInstanceOf[Long]
      i += 1
    }
    a.toList.distinct

  } else {
    val tempIds = stmtss.flatten.collect {
      case Add(e, _, _, _) if e.toString.take(6) == "#db/id" => e
      case Add(_, _, v, _) if v.toString.take(6) == "#db/id" => v
    }.distinct
    val txTtempIds = rawTxReport.get(Connection.TEMPIDS)
    val dbAfter = rawTxReport.get(Connection.DB_AFTER).asInstanceOf[Database]
    val ids: Seq[Long] = tempIds.map(tempId => datomic.Peer.resolveTempid(dbAfter, txTtempIds, tempId).asInstanceOf[Long]).distinct
    ids.toList
  }

  def txDataRaw: List[Datum] = rawTxReport.get(Connection.TX_DATA).asInstanceOf[jList[_]].asScala.toList.asInstanceOf[List[Datum]]

  private def attr(d: Datum) = {
    val kw = dbAfter.entity(d.a).get(":db/ident").asInstanceOf[Keyword]
    s":${kw.getNamespace}/${kw.getName}"
    kw.toString
  }


  def debug: Unit = Debug("TxReport", 1)(1, stmtss, txDataRaw)

  private val tempids = rawTxReport.get(TEMPIDS).asInstanceOf[AnyRef]

  override def toString =
    s"""TxReport {
       |  dbBefore       : $dbBefore
       |  dbBefore.basisT: ${dbBefore.basisT}
       |  dbAfter        : $dbAfter
       |  dbAfter.basisT : ${dbAfter.basisT}
       |  txData         : ${txDataRaw.mkString(",\n                   ")}
       |  tempids        : $tempids
       |}""".stripMargin


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
  def dbBefore: Database = rawTxReport.get(Connection.DB_BEFORE).asInstanceOf[Database]

  /** Get database value after transaction. */
  def dbAfter: Database = rawTxReport.get(Connection.DB_AFTER).asInstanceOf[Database]

  /** Get transaction time t. */
  def t: Long = dbAfter.basisT()

  /** Get transaction entity id (Long). */
  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  /** Get transaction entity (datomic.Entity). */
  def txE: datomic.Entity = dbAfter.entity(tx)

  /** Get transaction instant (Date). */
  def inst: Date = txE.get(":db/txInstant").asInstanceOf[Date]
}
