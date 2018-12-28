package molecule.facade

import java.util.{Date, Collection => jCollection, List => jList}
import datomic.{Database, Peer}
import molecule.ast.model.Model
import molecule.ast.query.{Query, QueryExpr}
import molecule.ast.tempDb._
import molecule.ast.transactionModel._
import molecule.exceptions._
import molecule.ops.QueryOps._
import molecule.transform.Query2String
import molecule.util.Helpers
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.util.control.NonFatal

/** Factory methods to create facade to Datomic Connection. */
object Conn {
  def apply(uri: String): Conn = new Conn(datomic.Peer.connect(uri))
  def apply(datomicConn: datomic.Connection): Conn = new Conn(datomicConn)

  // Constructor for transaction functions where db is supplied inside transaction by transactor
  def apply(txDb: AnyRef): Conn = new Conn(null) {
    testDb(txDb.asInstanceOf[Database])
  }
}


/** Facade to Datomic Connection.
  *
  * @see [[http://www.scalamolecule.org/manual/time/testing/ Manual]]
  *      | Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbAsOf.scala#L1 testDbAsOf]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbSince.scala#L1 testDbSince]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbWith.scala#L1 testDbWith]],
  **/
class Conn(val datomicConn: datomic.Connection) extends Helpers {

  // Temporary db for ad-hoc queries against time variation dbs
  // (takes precedence over test db)
  private var _adhocDb: Option[TempDb] = None
  private[molecule] def usingTempDb(tempDb: TempDb): Conn = {
    _adhocDb = Some(tempDb)
    this
  }

  // In-memory fixed test db for integration testing of domain model
  // (takes precedence over live db)
  private var _testDb: Option[Database] = None

  /** Manually apply a database to use.
    *
    * @param db
    */
  def testDb(db: Database): Unit = {
    _testDb = Some(db)
  }

  /** Use test database as of time t.
    *
    * @param t Long
    */
  def testDbAsOf(t: Long): Unit = {
    _testDb = Some(datomicConn.db.asOf(t))
  }

  /** Use test database as of date.
    *
    * @param d Date
    */
  def testDbAsOf(d: Date): Unit = {
    _testDb = Some(datomicConn.db.asOf(d))
  }

  /** Use test database as of transaction report.
    *
    * @param txR Transaction report
    */
  def testDbAsOf(txR: TxReport): Unit = {
    _testDb = Some(datomicConn.db.asOf(txR.t))
  }

  /** Use test database as of now. */
  def testDbAsOfNow: Unit = {
    _testDb = Some(datomicConn.db)
  }

  /** Use test database since time t.
    *
    * @param t Long
    */
  def testDbSince(t: Long): Unit = {
    _testDb = Some(datomicConn.db.since(t))
  }

  /** Use test database since date.
    *
    * @param d Date
    */
  def testDbSince(d: Date): Unit = {
    _testDb = Some(datomicConn.db.since(d))
  }

  /** Use test database since transaction report.
    *
    * @param txR Transaction report
    */
  def testDbSince(txR: TxReport): Unit = {
    _testDb = Some(datomicConn.db.since(txR.t))
  }


  /** Use test database with temporary transaction data.
    * <br><br>
    * Transaction data can be supplied from any molecule:
    * {{{
    *   val benId = Person.name("Ben").save.eid
    *
    *   // Use temporary db with given transaction data applied
    *   conn.testDbWith(
    *     Person.name("liz").getSaveTx
    *   )
    *
    *   // Query using temporary database including Liz
    *   Person.name.get === List("Ben", "Liz")
    *
    *   // Multiple transactions can be applied
    *   conn.testDbWith(
    *     Person.name("Joe").getSaveTx,
    *     benId.getRetractTx
    *   )
    *   Person.name.get === List("Liz", "Joe")
    * }}}
    *
    * @param txData List of List of transaction [[molecule.ast.transactionModel.Statement Statement]]'s
    */
  def testDbWith(txData: Seq[Seq[Statement]]*): Unit = {
    val txDataJava: jList[jList[_]] = seqAsJavaListConverter(txData.flatten.flatten.map(_.toJava)).asJava
    _testDb = Some(datomicConn.db.`with`(txDataJava).get(datomic.Connection.DB_AFTER).asInstanceOf[Database])
  }

  /** Use test database with temporary raw Java transaction data. */
  def testDbWith(txDataJava: jList[jList[AnyRef]]): Unit = {
    _testDb = Some(datomicConn.db.`with`(txDataJava).get(datomic.Connection.DB_AFTER).asInstanceOf[Database])
  }

  /* testDbHistory not implemented.
   * Instead, use `testDbAsOfNow`, make changes and get historic data with getHistory calls.
   * */

  /** Get out of test mode and back to live db. */
  def useLiveDb: Unit = {
    _testDb = None
  }

  /** Get current test/live db. Test db has preference. */
  def db: Database = if (_testDb.isDefined) _testDb.get else datomicConn.db


  private[molecule] def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      val baseDb = _testDb.getOrElse(datomicConn.db)
      val adhocDb = _adhocDb.get match {
        case AsOf(TxLong(t))  => baseDb.asOf(t)
        case AsOf(TxDate(d))  => baseDb.asOf(d)
        case Since(TxLong(t)) => baseDb.since(t)
        case Since(TxDate(d)) => baseDb.since(d)
        case With(tx)         => baseDb.`with`(tx).get(datomic.Connection.DB_AFTER).asInstanceOf[Database]
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
      TxReport(datomicConn.transact(javaStmts).get, stmtss)
    }
  }

  private[molecule] def transactAsync(stmtss: Seq[Seq[Statement]])(implicit ec: ExecutionContext): Future[TxReport] = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      Future {
        val baseDb = _testDb.getOrElse(datomicConn.db)
        val adhocDb = _adhocDb.get match {
          case AsOf(TxLong(t))  => baseDb.asOf(t)
          case AsOf(TxDate(d))  => baseDb.asOf(d)
          case Since(TxLong(t)) => baseDb.since(t)
          case Since(TxDate(d)) => baseDb.since(d)
          case With(tx)         => baseDb.`with`(tx).get(datomic.Connection.DB_AFTER).asInstanceOf[Database]
          case History          => baseDb.history()
          case Using(db)        => db
        }
        // Void adhoc db
        _adhocDb = None

        // In-memory "transaction"
        TxReport(adhocDb.`with`(javaStmts), stmtss)
      }

    } else if (_testDb.isDefined) {
      Future {
        // In-memory "transaction"
        val txReport = TxReport(_testDb.get.`with`(javaStmts), stmtss)

        // Continue with updated in-memory db
        // todo: why can't we just say this? Or: why are there 2 db-after db objects?
        //      val dbAfter = txReport.dbAfter
        val dbAfter = txReport.dbAfter.asOf(txReport.t)
        _testDb = Some(dbAfter)
        txReport
      }

    } else {
      // Live transaction
      val moleculeInvocationFuture = try {
        bridgeDatomicFuture(datomicConn.transactAsync(javaStmts))
      } catch {
        case NonFatal(ex) => Future.failed(ex)
      }
      moleculeInvocationFuture map { moleculeInvocationResult: java.util.Map[_, _] =>
        TxReport(moleculeInvocationResult, stmtss)
      }
    }
  }

  /** Transact edn files or other raw transaction data.
    * {{{
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val rawTxStmts = Util.readAll(data_rdr2).get(0).asInstanceOf[java.util.List[Object]]
    *
    *   // transact
    *   val result: TxReport = conn.transact(rawTxStmts)
    * }}}
    *
    * @param rawTxStmts Raw transaction data, typically from edn file.
    * @return [[molecule.facade.TxReport TxReport]]
    */
  def transact(rawTxStmts: jList[AnyRef]): TxReport = if (_testDb.isDefined) {
    // In-memory "transaction"
    val txReport = TxReport(_testDb.get.`with`(rawTxStmts))
    // Continue with updated in-memory db
    _testDb = Some(txReport.dbAfter.asOf(txReport.t))
    txReport
  } else {
    // Live transaction
    TxReport(datomicConn.transact(rawTxStmts).get)
  }

  /** Asynchronously transact edn files or other raw transaction data.
    * {{{
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val rawTxStmts = Util.readAll(data_rdr2).get(0).asInstanceOf[java.util.List[Object]]
    *
    *   // transact
    *   val result: Future[TxReport] = conn.transactAsync(rawTxStmts)
    * }}}
    *
    * @param rawTxStmts Raw transaction data, typically from edn file.
    * @return Future with [[molecule.facade.TxReport TxReport]] with result of transaction
    */
  def transactAsync(rawTxStmts: jList[AnyRef])(implicit ec: ExecutionContext): Future[TxReport] = if (_testDb.isDefined) {
    Future {
      // In-memory "transaction"
      val txReport = TxReport(_testDb.get.`with`(rawTxStmts))

      // Continue with updated in-memory db
      // todo: why can't we just say this? Or: why are there 2 db-after db objects?
      //      val dbAfter = txReport.dbAfter
      val dbAfter = txReport.dbAfter.asOf(txReport.t)
      _testDb = Some(dbAfter)
      txReport
    }
  } else {
    // Live transaction
    val moleculeInvocationFuture = try {
      bridgeDatomicFuture(datomicConn.transactAsync(rawTxStmts))
    } catch {
      case NonFatal(ex) => Future.failed(ex)
    }
    moleculeInvocationFuture map { moleculeInvocationResult: java.util.Map[_, _] =>
      TxReport(moleculeInvocationResult)
    }
  }


  /** Query Datomic directly with optional Scala inputs.
    * {{{
    *   // Sample data
    *   Ns.str.int.get === List(
    *     ("Liz", 37),
    *     ("Ben", 42),
    *   )
    *
    *   // Start out easily with a Datomic query from debug output
    *   Ns.str.int.debugGet // shows datomic query...
    *
    *   // Paste Datomic query into `q` call
    *   conn.q("""[:find  ?b ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin) === List(
    *     List("Liz", 37),
    *     List("Ben", 42)
    *   )
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q("""[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin, 42) === List(
    *     List("Ben", 42)
    *   )
    * }}}
    *
    * @param query  Datomic query string
    * @param inputs Optional input(s) to query
    * @return List[List[AnyRef]]
    **/
  def q(query: String, inputs: Any*): List[List[AnyRef]] = q(db, query, inputs.toSeq)


  /** Query Datomic directly with db value and optional Scala inputs.
    * {{{
    *   // Sample data
    *   Ns.str.int.get === List(
    *     ("Liz", 37),
    *     ("Ben", 42),
    *   )
    *
    *   // Start out easily with a Datomic query from debug output
    *   Ns.str.int.debugGet // shows datomic query...
    *
    *   // Paste Datomic query into `q` call and use some db value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin) === List(
    *     List("Liz", 37),
    *     List("Ben", 42)
    *   )
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin,
    *          Seq(42) // input values in list
    *    ) === List(
    *     List("Ben", 42)
    *   )
    * }}}
    *
    * @param db     Any Datomic Database value (could be asOf(x) etc)
    * @param query  Datomic query string
    * @param inputs Seq of optional input(s) to query
    * @return List[List[AnyRef]]
    **/
  def q(db: Database, query: String, inputs: Seq[Any]): List[List[AnyRef]] =
    collectionAsScalaIterableConverter(qRaw(db, query, inputs)).asScala.toList.map(asScalaBufferConverter(_).asScala.toList)


  /** Query Datomic directly with optional Scala inputs and get raw Java result.
    * {{{
    *   // Sample data
    *   Ns.str.int.get === List(
    *     ("Liz", 37),
    *     ("Ben", 42),
    *   )
    *
    *   // Start out easily with a Datomic query from debug output
    *   Ns.str.int.debugGet // shows datomic query...
    *
    *   // Paste Datomic query into `q` call
    *   conn.q("""[:find  ?b ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin)
    *       .toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q("""[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin, 42).toString === """[["Ben" 42]]"""
    * }}}
    *
    * @param query  Datomic query string
    * @param inputs Optional input(s) to query
    * @return java.util.Collection[java.util.List[AnyRef]]
    **/
  def qRaw(query: String, inputs: Any*): jCollection[jList[AnyRef]] = qRaw(db, query, inputs)


  /** Query Datomic directly with db value and optional Scala inputs and get raw Java result.
    * {{{
    *   // Sample data
    *   Ns.str.int.get === List(
    *     ("Liz", 37),
    *     ("Ben", 42),
    *   )
    *
    *   // Start out easily with a Datomic query from debug output
    *   Ns.str.int.debugGet // shows datomic query...
    *
    *   // Paste Datomic query into `q` call and use some db value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin)
    *       .toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :ns/str ?b]
    *            |        [?a :ns/int ?c]]""".stripMargin,
    *          Seq(42) // input values in list
    *    ).toString === """[["Ben" 42]]"""
    * }}}
    *
    * @param db     Any Datomic Database value (could be asOf(x) etc)
    * @param query  Datomic query string
    * @param inputs Seq of optional input(s) to query
    * @return java.util.Collection[java.util.List[AnyRef]]
    **/
  def qRaw(db: Database, query: String, inputs: Seq[Any]): jCollection[jList[AnyRef]] =
    blocking(Peer.q(query, db +: inputs.asInstanceOf[Seq[AnyRef]]: _*))


  /** Query Datomic with Model and Query to get raw Java data.
    * <br><br>
    * Main query method that Molecule macros use.
    *
    * @param m [[molecule.ast.model.Model Model]] instance
    * @param q [[molecule.ast.query.Query Query]] instance
    * @return java.util.Collection[java.util.List[AnyRef]]
    **/
  def query(m: Model, q: Query): jCollection[jList[AnyRef]] = {
    val p = (expr: QueryExpr) => Query2String(q).p(expr)
    val rules = "[" + (q.i.rules map p mkString " ") + "]"
    val dbUsed = if (_adhocDb.isDefined) {
      val baseDb = _testDb.getOrElse(datomicConn.db)
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
      datomicConn.db
    }

    val first = if (q.i.rules.isEmpty) Seq(dbUsed) else Seq(dbUsed, rules)
    val allInputs: Seq[AnyRef] = first ++ QueryOps(q).inputs
    try {
      blocking {
        Peer.q(q.toMap, allInputs: _*)
      }
    } catch {
      case ex: Throwable if ex.getMessage startsWith "processing" =>
        val builder = Seq.newBuilder[String]
        var e = ex
        while (e.getCause != null) {
          builder += e.getMessage
          e = e.getCause
        }
        throw new QueryException(e, m, q, allInputs, p, builder.result)
      case NonFatal(ex)                                           =>
        throw new QueryException(ex, m, q, allInputs, p)
    }
  }
}
