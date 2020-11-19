package molecule.datomic.client.devLocal.facade

import java.util
import java.util.{Date, Collection => jCollection, List => jList}
import clojure.lang.Keyword
import datomic.{Connection, Database, Datom, Peer}
import datomic.Connection.DB_AFTER
import datomic.Peer._
import datomic.Util._
import datomic.db.DbId
import datomicScala.client.api.sync.{Client, Db, Datomic => clientDatomic}
import molecule.core.ast.model._
import molecule.core.ast.query.{Query, QueryExpr}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel._
import molecule.core.exceptions._
import molecule.core.ops.QueryOps._
import molecule.core.transform.{Query2String, QueryOptimizer}
import molecule.core.util.{BridgeDatomicFuture, Helpers}
import molecule.datomic.base.facade.{Conn, DatomicDb, TxReport}
import molecule.datomic.peer.facade.{Database_Peer, TxReport_Peer}
import org.slf4j.LoggerFactory
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal
import datomicScala.client.api.sync


/** Facade to Datomic dev-local connection.
  *
  * @see [[http://www.scalamolecule.org/manual/time/testing/ Manual]]
  *      | Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbAsOf.scala#L1 testDbAsOf]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbSince.scala#L1 testDbSince]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbWith.scala#L1 testDbWith]],
  * */
case class Conn_DevLocal(client: Client, dbName: String)
  extends Conn with Helpers with BridgeDatomicFuture {

  val clientConn: sync.Connection = client.connect(dbName)


  // Temporary db for ad-hoc queries against time variation dbs
  // (takes precedence over test db)
  protected var _adhocDb: Option[TempDb] = None

  // In-memory fixed test db for integration testing of domain model
  // (takes precedence over live db)
  protected var _testDb: Option[Db] = None

  def usingTempDb(tempDb: TempDb): Conn = {
    _adhocDb = Some(tempDb)
    this
  }

  def liveDbUsed: Boolean = _adhocDb.isEmpty && _testDb.isEmpty

  //  def testDb(db: Db): Unit = {
  //    _testDb = Some(db)
  //  }
  def testDb(db: DatomicDb): Unit = {
    _testDb = Some(db.asInstanceOf[Database_DevLocal].clientDb)
  }

  def testDbAsOf(t: Long): Unit = ???

  def testDbAsOf(d: Date): Unit = ???

  def testDbAsOf(txR: TxReport): Unit = ???

  def testDbAsOfNow: Unit = ???

  def testDbSince(t: Long): Unit = ???

  def testDbSince(d: Date): Unit = ???

  def testDbSince(txR: TxReport): Unit = ???

  def testDbWith(txData: Seq[Seq[Statement]]*): Unit = ???

  def testDbWith(txDataJava: jList[jList[AnyRef]]): Unit = ???

  def useLiveDb: Unit = ???


  private def getAdhocDb: Db = {
    val baseDb : Db = _testDb.getOrElse(clientConn.db)
    val adhocDb: Db = _adhocDb.get match {
      case AsOf(TxLong(t))  => baseDb.asOf(t)
      case AsOf(TxDate(d))  => baseDb.asOf(d)
      case Since(TxLong(t)) => baseDb.since(t)
      case Since(TxDate(d)) => baseDb.since(d)
      //      case With(tx)         => Db(baseDb.`with`(clientConn.db, tx))
      case With(tx) => Db(baseDb.`with`(baseDb, tx))
      case History  => baseDb.history
    }
    _adhocDb = None
    adhocDb
  }

  def db: DatomicDb = {
    if (_adhocDb.isDefined) {
      // Return singleton adhoc db
      Database_DevLocal(getAdhocDb)
    } else if (_testDb.isDefined) {
      // Test db
      Database_DevLocal(_testDb.get)
    } else {
      // Live db
      Database_DevLocal(clientConn.db)
    }
  }

  def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)
    if (_adhocDb.isDefined) {
      // In-memory "transaction"
      val adHocDb = getAdhocDb
      TxReport_DevLocal(adHocDb.`with`(adHocDb, javaStmts), stmtss)

    } else if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport_DevLocal(_testDb.get.`with`(_testDb, javaStmts), stmtss)

      // Continue with updated in-memory db
      // todo: why can't we just say this? Or: why are there 2 db-after db objects?
      //      val dbAfter = txReport.dbAfter
      val dbAfter = txReport.dbAfter.asOf(txReport.t)
      _testDb = Some(dbAfter)
      txReport

    } else {

      // Live transaction
      TxReport_DevLocal(clientConn.transact(javaStmts), stmtss)
    }
  }

  def transactAsync(stmtss: Seq[Seq[Statement]])
                   (implicit ec: ExecutionContext): Future[TxReport] = {
    //    val javaStmts: jList[jList[_]] = toJava(stmtss)
    //
    //    if (_adhocDb.isDefined) {
    //      Future {
    //        val adHocDb = getAdhocDb
    //        TxReport_DevLocal(adHocDb.`with`(adHocDb, javaStmts), stmtss)
    //      }
    //
    //    } else if (_testDb.isDefined) {
    //      Future {
    //        // In-memory "transaction"
    //        val txReport = TxReport_DevLocal(_testDb.get.`with`(_testDb, javaStmts), stmtss)
    //
    //        // Continue with updated in-memory db
    //        // todo: why can't we just say this? Or: why are there 2 db-after db objects?
    //        //      val dbAfter = txReport.dbAfter
    //        val dbAfter = txReport.dbAfter.asOf(txReport.t)
    //        _testDb = Some(dbAfter)
    //        txReport
    //      }
    //
    //    } else {
    //      // Live transaction
    //      val moleculeInvocationFuture = try {
    //        bridgeDatomicFuture(clientConn.transactAsync(javaStmts))
    //      } catch {
    //        case NonFatal(ex) => Future.failed(ex)
    //      }
    //      moleculeInvocationFuture map { moleculeInvocationResult: java.util.Map[_, _] =>
    //        TxReport_Peer(moleculeInvocationResult, stmtss)
    //      }
    //    }

    //    Future[TxReport]{}
    //    Future(TxReport_DevLocal(TxReport_DevLocal()))
    ???
  }

  def transact(rawTxStmts: jList[_]): TxReport = {
    if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport_DevLocal(_testDb.get.`with`(_testDb, rawTxStmts))
      // Continue with updated in-memory db
      _testDb = Some(txReport.dbAfter.asOf(txReport.t))
      txReport
    } else {
      // Live transaction
      TxReport_DevLocal(clientConn.transact(rawTxStmts))
    }
  }


  def transactAsync(rawTxStmts: jList[_])(implicit ec: ExecutionContext): Future[TxReport] = {
    ???
  }


  def qRaw(db: DatomicDb, query: String, inputs0: Seq[Any]): jCollection[jList[AnyRef]] = {
    val inputs = inputs0.map {
      case it: Iterable[_] => it.asJava
      case dbId: DbId      => dbId.idx.toString
      case v               => v
    }
    blocking(
      clientDatomic.q(query, clientConn.db, inputs.asInstanceOf[Seq[AnyRef]]: _*)
    )
  }

  def query(model: Model, query: Query): jCollection[jList[AnyRef]] = {
    model.elements.head match {
      case Generic("Log" | "EAVT" | "AEVT" | "AVET" | "VAET", _, _, _) => _index(model)
      case _                                                           => _query(model, query)
    }
  }

  def _query(model: Model, query: Query, _db: Option[DatomicDb]): jCollection[jList[AnyRef]] = {
    val adhocDb        = _db.getOrElse(db).asInstanceOf[Database_DevLocal].clientDb
    val optimizedQuery = QueryOptimizer(query)
    val p              = (expr: QueryExpr) => Query2String(optimizedQuery).p(expr)
    val rules          = if (query.i.rules.isEmpty) Nil else
      Seq("[" + (query.i.rules map p mkString " ") + "]")
    val allInputs      = rules ++ QueryOps(query).inputs
    try {
      blocking {
        clientDatomic.q(query.toMap, adhocDb, allInputs: _*)
      }
    } catch {
      case ex: Throwable if ex.getMessage startsWith "processing" =>
        val builder = Seq.newBuilder[String]
        var e       = ex
        while (e.getCause != null) {
          builder += e.getMessage
          e = e.getCause
        }
        throw new QueryException(e, model, query, allInputs, p, builder.result())
      case NonFatal(ex)                                           =>
        throw new QueryException(ex, model, query, allInputs, p)
    }
  }

  def _index(model: Model): jCollection[jList[AnyRef]] = ???
}