package molecule.core.facade

import java.util
import java.util.{Date, Collection => jCollection, List => jList}
import datomic.Peer._
import datomic.Util._
import datomic.{Database, Datom, Peer}
import molecule.core.ast.model._
import molecule.core.ast.query.{Query, QueryExpr}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel._
import molecule.core.exceptions._
import molecule.core.ops.QueryOps._
import molecule.core.transform.{Query2String, QueryOptimizer}
import molecule.core.util.{BridgeDatomicFuture, Helpers}
import molecule.datomic.base.facade.TxReport
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._
import scala.concurrent.{blocking, ExecutionContext, Future}
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
  * */
class Conn(val datomicConn: datomic.Connection)
  extends Helpers with BridgeDatomicFuture {

  val log = LoggerFactory.getLogger(getClass)

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

  /** Flag to indicate if live database is used */
  def liveDbUsed: Boolean = _adhocDb.isEmpty && _testDb.isEmpty

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
    * @param txData List of List of transaction [[molecule.core.ast.transactionModel.Statement Statement]]'s
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
  def db: Database = if (_adhocDb.isDefined) {
    val baseDb  = _testDb.getOrElse(datomicConn.db)
    val adhocDb = _adhocDb.get match {
      case AsOf(TxLong(t))  =>
        baseDb.asOf(t)
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

    // Return singleton adhoc db
    adhocDb
  } else if (_testDb.isDefined) {
    // Test db
    _testDb.get
  } else {
    // Live db
    datomicConn.db
  }


  def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      val baseDb  = _testDb.getOrElse(datomicConn.db)
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
      // println("---------\n" + javaStmts)
      // Live transaction
      TxReport(datomicConn.transact(javaStmts).get, stmtss)
    }
  }

  def transactAsync(stmtss: Seq[Seq[Statement]])
                   (implicit ec: ExecutionContext): Future[TxReport] = {

    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      Future {
        val baseDb  = _testDb.getOrElse(datomicConn.db)
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
    * @return [[TxReport TxReport]]
    */
  def transact(rawTxStmts: jList[AnyRef]): TxReport = {

    if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport(_testDb.get.`with`(rawTxStmts))
      // Continue with updated in-memory db
      _testDb = Some(txReport.dbAfter.asOf(txReport.t))
      txReport
    } else {
      // Live transaction
      TxReport(datomicConn.transact(rawTxStmts).get)
    }
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
    * @return Future with [[TxReport TxReport]] with result of transaction
    */
  def transactAsync(rawTxStmts: jList[AnyRef])
                   (implicit ec: ExecutionContext): Future[TxReport] = {

    if (_testDb.isDefined) {
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
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin) === List(
    *     List("Liz", 37),
    *     List("Ben", 42)
    *   )
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q("""[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin, 42) === List(
    *     List("Ben", 42)
    *   )
    * }}}
    *
    * @param query  Datomic query string
    * @param inputs Optional input(s) to query
    * @return List[List[AnyRef]]
    * */
  def q(query: String, inputs: Any*): List[List[AnyRef]] =
    q(db, query, inputs.toSeq)


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
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin) === List(
    *     List("Liz", 37),
    *     List("Ben", 42)
    *   )
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin,
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
    collectionAsScalaIterableConverter(qRaw(db, query, inputs)).asScala.toList
      .map(asScalaBufferConverter(_).asScala.toList
        .map {
          case set: clojure.lang.PersistentHashSet => asScalaSetConverter(set).asScala.toSet
          case other                               => other
        }
      )


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
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin)
    *       .toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q("""[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin, 42).toString === """[["Ben" 42]]"""
    * }}}
    *
    * @param query  Datomic query string
    * @param inputs Optional input(s) to query
    * @return java.util.Collection[java.util.List[AnyRef]]
    * */
  def qRaw(query: String, inputs: Any*): jCollection[jList[AnyRef]] =
    qRaw(db, query, inputs)


  /** Query Datomic directly with db value and optional Scala inputs and get raw Java result.
    * {{{
    *   // Sample data
    *   Ns.str.int.get === List(
    *     ("Liz", 37),
    *     ("Ben", 42),
    *   )
    *
    *   // Get some Datomic query from debug output
    *   Ns.str.int.debugGet // shows datomic query...
    *
    *   // Paste Datomic query into `q` call and use some db value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin)
    *       .toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Modify Datomic query to see result, for instance
    *   // by adding input to query and applying input value
    *   conn.q(conn.db,
    *          """[:find  ?b ?c
    *            | :in    $ ?c
    *            | :where [?a :Ns/str ?b]
    *            |        [?a :Ns/int ?c]]""".stripMargin,
    *          Seq(42) // input values in list
    *    ).toString === """[["Ben" 42]]"""
    * }}}
    *
    * @param db      Any Datomic Database value (could be asOf(x) etc)
    * @param query   Datomic query string
    * @param inputs0 Seq of optional input(s) to query
    * @return java.util.Collection[java.util.List[AnyRef]]
    * */
  def qRaw(db: Database, query: String, inputs0: Seq[Any]): jCollection[jList[AnyRef]] = {
    val inputs = inputs0.map {
      case it: Iterable[_] => it.asJava
      case v               => v
    }
    blocking(Peer.q(query, db +: inputs.asInstanceOf[Seq[AnyRef]]: _*))
  }


  /** Query Datomic with Model and Query to get raw Java data.
    * <br><br>
    * Will transparently relegate query depending on Model to:
    *
    * - Datalog query execution
    * - Datoms API accessing index
    * - Log API accessing log
    *
    * Return type (tuple matching the molecule) is the same for all 3 APIs so that
    * application code can query and access data of all molecules the same way.
    *
    * @param model [[molecule.core.ast.model.Model Model]] instance
    * @param query [[molecule.core.ast.query.Query Query]] instance
    * @return java.util.Collection[java.util.List[AnyRef]]
    **/
  def query(model: Model, query: Query): jCollection[jList[AnyRef]] = model.elements.head match {
    case Generic("Log" | "EAVT" | "AEVT" | "AVET" | "VAET", _, _, _) => _index(model)
    case _                                                           => _query(model, query)
  }

  // Datalog query execution
  private[molecule] def _query(model: Model, query: Query, _db: Option[Database] = None): jCollection[jList[AnyRef]] = {
    val optimizedQuery         = QueryOptimizer(query)
    val p                      = (expr: QueryExpr) => Query2String(optimizedQuery).p(expr)
    val rules                  = "[" + (query.i.rules map p mkString " ") + "]"
    val adhocDb                = _db.getOrElse(db)
    val first                  = if (query.i.rules.isEmpty) Seq(adhocDb) else Seq(adhocDb, rules)
    val allInputs: Seq[AnyRef] = first ++ QueryOps(query).inputs
    try {
      blocking {
        Peer.q(query.toMap, allInputs: _*)
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

  // Datoms API providing direct access to indexes
  private[molecule] def _index(model: Model): jCollection[jList[AnyRef]] = {
    val (api, index, args) = model.elements.head match {
      case Generic("EAVT", _, _, value) =>
        ("datoms", datomic.Database.EAVT, value match {
          case Eq(Seq(e))                => Seq(e.asInstanceOf[Object])
          case Eq(Seq(e, a))             => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object])
          case Eq(Seq(e, a, v))          => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object])
          case Eq(Seq(e, a, v, d: Date)) => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(e, a, v, t))       => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("AEVT", _, _, value) =>
        ("datoms", datomic.Database.AEVT, value match {
          case Eq(Seq(a))                => Seq(a.asInstanceOf[Object])
          case Eq(Seq(a, e))             => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object])
          case Eq(Seq(a, e, v))          => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object])
          case Eq(Seq(a, e, v, d: Date)) => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(a, e, v, t))       => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("AVET", attr, _, value) =>
        attr match {
          case "range" =>
            ("indexRange", datomic.Database.AVET, value match {
              case Eq(Seq(a, None, None))  => throw new MoleculeException(
                "Molecule not allowing returning from start to end (the whole database!).\n" +
                  "If you need this, please use raw Datomic access:\n" +
                  "`conn.db.datoms(datomic.Database.AVET)`")
              case Eq(Seq(a, from, None))  => Seq(a.asInstanceOf[Object], from.asInstanceOf[Object], null)
              case Eq(Seq(a, None, until)) => Seq(a.asInstanceOf[Object], null, until.asInstanceOf[Object])
              case Eq(Seq(a, from, until)) =>
                if (from.getClass != until.getClass)
                  throw new MoleculeException("Please supply range arguments of same type as attribute.")
                Seq(a.asInstanceOf[Object], from.asInstanceOf[Object], until.asInstanceOf[Object])
            })
          case _       =>
            ("datoms", datomic.Database.AVET, value match {
              case Eq(Seq(a))                => Seq(a.asInstanceOf[Object])
              case Eq(Seq(a, v))             => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object])
              case Eq(Seq(a, v, e))          => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object])
              case Eq(Seq(a, v, e, d: Date)) => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object], d.asInstanceOf[Object])
              case Eq(Seq(a, v, e, t))       => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object], t.asInstanceOf[Object])
            })
        }

      case Generic("VAET", _, _, value) =>
        ("datoms", datomic.Database.VAET, value match {
          case Eq(Seq(v))                => Seq(v.asInstanceOf[Object])
          case Eq(Seq(v, a))             => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object])
          case Eq(Seq(v, a, e))          => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object])
          case Eq(Seq(v, a, e, d: Date)) => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(v, a, e, t))       => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("Log", _, _, value) =>
        ("txRange", datomic.Database.VAET, value match {
          case Eq(Seq(from: Int, until: Int))   => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Int, until: Long))  => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Int, until: Date))  => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Long, until: Int))  => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Long, until: Long)) => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Long, until: Date)) => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Date, until: Int))  => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Date, until: Long)) => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])
          case Eq(Seq(from: Date, until: Date)) => Seq(from.asInstanceOf[Object], until.asInstanceOf[Object])

          case Eq(Seq(from: Int, None))  => Seq(from.asInstanceOf[Object], null)
          case Eq(Seq(from: Long, None)) => Seq(from.asInstanceOf[Object], null)
          case Eq(Seq(from: Date, None)) => Seq(from.asInstanceOf[Object], null)

          case Eq(Seq(None, until: Int))  => Seq(null, until.asInstanceOf[Object])
          case Eq(Seq(None, until: Long)) => Seq(null, until.asInstanceOf[Object])
          case Eq(Seq(None, until: Date)) => Seq(null, until.asInstanceOf[Object])

          case Eq(Seq(None, None)) =>
            throw new MoleculeException(
              "Molecule not allowing returning from start to end (the whole database!).\n" +
                "If you need this, please use raw Datomic access:\n" +
                "`conn.datomicConn.log.txRange(tx1, tx2)`")

          case Eq(Seq(_, _)) => throw new MoleculeException("Args to Log can only be t, tx or txInstant of type Int/Long/Date")
        })

      case other => throw new MoleculeException(s"Only Index queries accepted (EAVT, AEVT, AVET, VAET, Log). Found `$other`")
    }


    val adhocDb = db
    def date(d: Datom): Date = adhocDb.entity(d.tx).get(":db/txInstant").asInstanceOf[Date]
    def ident(d: Datom): AnyRef = adhocDb.ident(d.a).toString
    def t(d: Datom): AnyRef = toT(d.tx).asInstanceOf[AnyRef]
    def op(d: Datom): AnyRef = d.added.asInstanceOf[AnyRef]

    def datomElement(attr: String): Datom => AnyRef = attr match {
      case "e"         => (d: Datom) => d.e
      case "a"         => (d: Datom) => ident(d)
      case "v"         => (d: Datom) => d.v
      case "t"         => (d: Datom) => t(d)
      case "tx"        => (d: Datom) => d.tx
      case "txInstant" => (d: Datom) => date(d)
      case "op"        => (d: Datom) => op(d)
    }

    val attrs: Seq[String] = model.elements.tail.collect {
      case Generic(_, attr, _, _) => attr
    }

    val datom2row: Datom => jList[AnyRef] = attrs.length match {
      case 1 =>
        val x1 = datomElement(attrs.head)
        (d: Datom) => list(x1(d)).asInstanceOf[jList[AnyRef]]

      case 2 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        (d: Datom) => list(x1(d), x2(d)).asInstanceOf[jList[AnyRef]]

      case 3 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        val x3 = datomElement(attrs(2))
        (d: Datom) => list(x1(d), x2(d), x3(d)).asInstanceOf[jList[AnyRef]]

      case 4 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        val x3 = datomElement(attrs(2))
        val x4 = datomElement(attrs(3))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d)).asInstanceOf[jList[AnyRef]]

      case 5 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        val x3 = datomElement(attrs(2))
        val x4 = datomElement(attrs(3))
        val x5 = datomElement(attrs(4))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d)).asInstanceOf[jList[AnyRef]]

      case 6 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        val x3 = datomElement(attrs(2))
        val x4 = datomElement(attrs(3))
        val x5 = datomElement(attrs(4))
        val x6 = datomElement(attrs(5))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d), x6(d)).asInstanceOf[jList[AnyRef]]

      case 7 =>
        val x1 = datomElement(attrs.head)
        val x2 = datomElement(attrs(1))
        val x3 = datomElement(attrs(2))
        val x4 = datomElement(attrs(3))
        val x5 = datomElement(attrs(4))
        val x6 = datomElement(attrs(5))
        val x7 = datomElement(attrs(6))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d), x6(d), x7(d)).asInstanceOf[jList[AnyRef]]
    }

    // Convert Datoms to standard list of rows so that we can use the same Molecule query API
    val jColl: jCollection[jList[AnyRef]] = new util.ArrayList[jList[AnyRef]]()
    api match {
      case "datoms"     =>
        adhocDb.datoms(index, args: _*).forEach { datom =>
          jColl.add(datom2row(datom))
        }
      case "indexRange" =>
        adhocDb.indexRange(args.head, args(1), args(2)).forEach { datom =>
          jColl.add(datom2row(datom))
        }
      case "txRange"    =>
        datomicConn.log.txRange(args.head, args(1)).forEach { txMap =>
          // Flatten transaction datoms to unified tuples return type
          txMap.get(datomic.Log.DATA).asInstanceOf[jList[Datom]].forEach { datom =>
            jColl.add(datom2row(datom))
          }
        }
    }
    jColl
  }
}
