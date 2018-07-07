package molecule.facade

import java.util.{Date, Collection => jCollection, List => jList}
import datomic.{Connection, Database, Peer}
import molecule.action.TxReport
import molecule.ast.model.Model
import molecule.ast.query.{Query, QueryExpr}
import molecule.ast.tempDb._
import molecule.ast.transaction._
import molecule.exceptions.QueryException
import scala.concurrent.blocking
//import molecule.facade._
import molecule.ops.QueryOps._
import molecule.transform.Query2String
import scala.collection.JavaConverters._

object Conn {
  def apply(uri: String): Conn = new Conn(datomic.Peer.connect(uri))
  def apply(datConn: datomic.Connection): Conn = new Conn(datConn)
}

class Conn(datConn: datomic.Connection) {

  // Temporary db for ad-hoc queries against time variation dbs
  // (takes precedence over test db)
  private var _adhocDb: Option[TempDb] = None
  private[molecule] def usingTempDb(tempDb: TempDb) = {
    _adhocDb = Some(tempDb)
    this
  }

  // In-memory fixed test db for integration testing of domain model
  // (takes precedence over live db)
  private var _testDb: Option[Database] = None
  def testDb(db: Database) {
    _testDb = Some(db)
  }

  def testDbAsOf(t: Long) {
    _testDb = Some(datConn.db.asOf(t))
  }
  def testDbAsOf(d: Date) {
    _testDb = Some(datConn.db.asOf(d))
  }
  def testDbAsOf(txR: TxReport) {
    _testDb = Some(datConn.db.asOf(txR.t))
  }
  def testDbAsOfNow {
    _testDb = Some(datConn.db)
  }

  def testDbSince(t: Long) {
    _testDb = Some(datConn.db.since(t))
  }
  def testDbSince(d: Date) {
    _testDb = Some(datConn.db.since(d))
  }
  def testDbSince(txR: TxReport) {
    _testDb = Some(datConn.db.since(txR.t))
  }

  def testDbWith(txMolecules: Seq[Seq[Statement]]*) {
    val txData = txMolecules.flatten.flatten.map(_.toJava).asJava
    _testDb = Some(datConn.db.`with`(txData).get(Connection.DB_AFTER).asInstanceOf[Database])
  }
  def testDbWith(txData: jList[jList[AnyRef]]) {
    _testDb = Some(datConn.db.`with`(txData).get(Connection.DB_AFTER).asInstanceOf[Database])
  }

  /* testDbHistory not implemented.
   * Instead, use `testDbAsOfNow`, make changes and get historic data with getHistory calls.
  **/

  // Get out of test mode and back to live db
  def useLiveDb {
    _testDb = None
  }

  // Get current test/live db. Test db has preference.
  def db = if (_testDb.isDefined) _testDb.get else datConn.db


  private[molecule] def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)
    if (_adhocDb.isDefined) {
      val baseDb = _testDb.getOrElse(datConn.db)
      val adhocDb = _adhocDb.get match {
        case AsOf(TxLong(t))  => baseDb.asOf(t)
        case AsOf(TxDate(d))  => baseDb.asOf(d)
        case Since(TxLong(t)) => baseDb.since(t)
        case Since(TxDate(d)) => baseDb.since(d)
        case With(tx)         => baseDb.`with`(tx).get(Connection.DB_AFTER).asInstanceOf[Database]
        case History          => baseDb.history()
        case Using(db)        => db
      }
      // Void adhoc db
      _adhocDb = None
      // In-memory "transaction"
      TxReport(adhocDb.`with`(javaStmts), stmtss)
    } else if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport(_testDb.get.`with`(javaStmts), stmtss)

      // Continue with updated in-memory db
      // todo: why can't we just say this? Or: why are there 2 db-after db objects?
      //      val dbAfter = txReport.dbAfter
      val dbAfter = txReport.dbAfter.asOf(txReport.t)
      _testDb = Some(dbAfter)
      txReport
    } else {
      // Live transaction
      TxReport(datConn.transact(javaStmts).get, stmtss)
    }
  }

  // Transact edn files or other raw transaction data
  def transact(tx: jList[AnyRef]): TxReport = if (_testDb.isDefined) {
    // In-memory "transaction"
    val txReport = TxReport(_testDb.get.`with`(tx))
    // Continue with updated in-memory db
    _testDb = Some(txReport.dbAfter.asOf(txReport.t))
    txReport
  } else {
    // Live transaction
    TxReport(datConn.transact(tx).get)
  }

  // Convenience function for querying directly against Datomic (untyped)
  // Note how we can still use a test db!
  def q(query: String, inputs: AnyRef*): jCollection[jList[AnyRef]] = q(query, db, inputs: _*)
  def q(query: String, db: Database, inputs: AnyRef*): jCollection[jList[AnyRef]] = Peer.q(query, db +: inputs.toSeq: _*)

  def query(m: Model, q: Query): jCollection[jList[AnyRef]] = {
    val p = (expr: QueryExpr) => Query2String(q).p(expr)
    val rules = "[" + (q.i.rules map p mkString " ") + "]"
    val dbUsed = if (_adhocDb.isDefined) {
      //
      val baseDb = _testDb.getOrElse(datConn.db)
      val adhocDb = _adhocDb.get match {
        case AsOf(TxLong(t))  => baseDb.asOf(t)
        case AsOf(TxDate(d))  => baseDb.asOf(d)
        case Since(TxLong(t)) => baseDb.since(t)
        case Since(TxDate(d)) => baseDb.since(d)
        case With(tx)         => {
          val txReport = TxReport(baseDb.`with`(tx))
          txReport.dbAfter.asOf(txReport.t)
        }
        case History          => baseDb.history()
        case Using(db)        => db
      }
      // Void adhoc db
      _adhocDb = None
      adhocDb
    } else if (_testDb.isDefined) {
      // Test db
      _testDb.get
    } else {
      // Live db
      datConn.db
    }

    val first = if (q.i.rules.isEmpty) Seq(dbUsed) else Seq(dbUsed, rules)
    val allInputs: Seq[AnyRef] = first ++ q.inputs

    try {
      blocking {
        Peer.q(q.toMap, allInputs: _*)
      }
    } catch {
      case e: Throwable => throw QueryException(e, m, q, allInputs, p)
    }
  }
}
