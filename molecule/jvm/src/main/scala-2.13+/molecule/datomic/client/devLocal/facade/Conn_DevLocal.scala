package molecule.datomic.client.devLocal.facade

import java.util
import java.util.{Date, Collection => jCollection, List => jList}
import clojure.lang.Keyword
import datomic.{Connection, Database, Datom, Peer}
import datomic.Peer._
import datomic.Util._
import datomicScala.client.api.sync.Client
import molecule.core.ast.model._
import molecule.core.ast.query.{Query, QueryExpr}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel._
import molecule.core.exceptions._
import molecule.core.ops.QueryOps._
import molecule.core.transform.{Query2String, QueryOptimizer}
import molecule.core.util.{BridgeDatomicFuture, Helpers}
import molecule.datomic.base.facade.{Conn, DatomicDb, TxReport}
import molecule.datomic.peer.facade.TxReport_Peer
import org.slf4j.LoggerFactory
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal
import datomicScala.client.api.sync.{Datomic => clientDatomic}
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


  val peerConn: Connection = null

  def usingTempDb(tempDb: TempDb): Conn = ???
  def liveDbUsed: Boolean = ???
  def testDb(db: Database): Unit = ???
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
  def db: DatomicDb = {
    Database_DevLocal(clientConn.db)
  }

  def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)
    TxReport_DevLocal(clientConn.transact(javaStmts), stmtss)
  }


  def transactAsync(stmtss: Seq[Seq[Statement]])(implicit ec: ExecutionContext): Future[TxReport] = ???

  def transact(rawTxStmts: jList[_]): TxReport = {
//    val clientTxReport = clientConn.transact(rawTxStmts)
    //
    //    if (_testDb.isDefined) {
    //      // In-memory "transaction"
    //      val txReport = TxReport_Peer(_testDb.get.`with`(rawTxStmts))
    //      // Continue with updated in-memory db
    //      _testDb = Some(txReport.dbAfter.asOf(txReport.t))
    //      txReport
    //    } else {
    //      // Live transaction
    //      TxReport_Peer(peerConn.transact(rawTxStmts).get)
    //    }

    TxReport_DevLocal(clientConn.transact(rawTxStmts))
  }

  //  def transact(rawTxStmts: jList[AnyRef]): TxReport = ???

  def transactAsync(rawTxStmts: jList[_])(implicit ec: ExecutionContext): Future[TxReport] = ???

//  def q(query: String, inputs: Any*): List[List[AnyRef]] =
//    q(db, query, inputs.toSeq)
//
//  def q(db: DatomicDb, query: String, inputs: Seq[Any]): List[List[AnyRef]] =
//    qRaw(db, query, inputs).asScala.toList
//      .map(_.asScala.toList
//        .map {
//          case set: clojure.lang.PersistentHashSet => set.asScala.toSet
//          case other                               => other
//        }
//      )

//  def qRaw(query: String, inputs: Any*): jCollection[jList[AnyRef]] = ???
  def qRaw(db: DatomicDb, query: String, inputs0: Seq[Any]): jCollection[jList[AnyRef]] = {
    val inputs = inputs0.map {
      case it: Iterable[_] => it.asJava
      case v               => v
    }
//    blocking(Peer.q(query, db.getDatomicDb +: inputs.asInstanceOf[Seq[AnyRef]]: _*))
//    blocking(Peer.q(query, db.getDatomicDb +: inputs.asInstanceOf[Seq[AnyRef]]: _*))



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
    val optimizedQuery         = QueryOptimizer(query)
    val p                      = (expr: QueryExpr) => Query2String(optimizedQuery).p(expr)
    val rules                  = "[" + (query.i.rules map p mkString " ") + "]"
    val adhocDb                = _db.getOrElse(db).getDatomicDb
    val first                  = if (query.i.rules.isEmpty) Seq(adhocDb) else Seq(adhocDb, rules)
    val allInputs: Seq[AnyRef] = first ++ QueryOps(query).inputs
    try {
      blocking {
        //        Peer.q(query.toMap, allInputs: _*)
        clientDatomic.q(query.toMap, clientConn.db, allInputs)
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