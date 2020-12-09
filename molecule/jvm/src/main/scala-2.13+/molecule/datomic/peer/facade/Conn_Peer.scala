package molecule.datomic.peer.facade

import java.{lang, util}
import java.util.{Date, Collection => jCollection, List => jList}
import datomic.{Database, Datom, ListenableFuture, Peer}
import datomic.Connection.DB_AFTER
import datomic.Peer._
import datomic.Util._
import datomicScala.client.api.sync.Db
import molecule.core.api.DatomicEntity
import molecule.core.ast.model._
import molecule.core.ast.query.{Query, QueryExpr}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel._
import molecule.core.exceptions._
import molecule.core.transform.{Query2String, QueryOptimizer}
import molecule.core.util.{BridgeDatomicFuture, Helpers, QueryOpsClojure}
import molecule.datomic.base.facade.{Conn, DatomicDb, TxReport}
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal

/** Factory methods to create facade to Datomic Connection. */
object Conn_Peer {
  def apply(uri: String): Conn_Peer = new Conn_Peer(datomic.Peer.connect(uri))
  def apply(datomicConn: datomic.Connection): Conn_Peer = new Conn_Peer(datomicConn)

  // Constructor for transaction functions where db is supplied inside transaction by transactor
  def apply(txDb: AnyRef): Conn_Peer = new Conn_Peer(null) {
    testDb(DatomicDb_Peer(txDb.asInstanceOf[Database]))
  }
}


/** Facade to Datomic Connection.
  *
  * @see [[http://www.scalamolecule.org/manual/time/testing/ Manual]]
  *      | Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbAsOf.scala#L1 testDbAsOf]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbSince.scala#L1 testDbSince]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbWith.scala#L1 testDbWith]],
  * */
class Conn_Peer(val peerConn: datomic.Connection)
  extends Conn with Helpers with BridgeDatomicFuture {

  // Temporary db for ad-hoc queries against time variation dbs
  // (takes precedence over test db)
  protected var _adhocDb: Option[TempDb] = None

  // In-memory fixed test db for integration testing of domain model
  // (takes precedence over live db)
  protected var _testDb: Option[Database] = None


  def usingTempDb(tempDb: TempDb): Conn = {
    _adhocDb = Some(tempDb)
    this
  }

  /** Flag to indicate if live database is used */
  def liveDbUsed: Boolean = _adhocDb.isEmpty && _testDb.isEmpty

  /** Manually apply a database to use.
    *
    * @param db
    */
  def testDb(db: DatomicDb): Unit = {
    _testDb = Some(db.asInstanceOf[DatomicDb_Peer].peerDb)
  }

  /** Use test database as of time t / tx id.
    *
    * @param t Long Time t or tx id
    */
  def testDbAsOf(t: Long): Unit = {
    _testDb = Some(peerConn.db.asOf(t))
  }

  /** Use test database as of date.
    *
    * @param d Date
    */
  def testDbAsOf(d: Date): Unit = {
    _testDb = Some(peerConn.db.asOf(d))
  }

  /** Use test database as of transaction report.
    *
    * @param txR Transaction report
    */
  def testDbAsOf(txR: TxReport): Unit = {
    _testDb = Some(peerConn.db.asOf(txR.t))
  }

  /** Use test database as of now. */
  def testDbAsOfNow: Unit = {
    _testDb = Some(peerConn.db)
  }

  /** Use test database since time t.
    *
    * @param t Long
    */
  def testDbSince(t: Long): Unit = {
    _testDb = Some(peerConn.db.since(t))
  }

  /** Use test database since date.
    *
    * @param d Date
    */
  def testDbSince(d: Date): Unit = {
    _testDb = Some(peerConn.db.since(d))
  }

  /** Use test database since transaction report.
    *
    * @param txR Transaction report
    */
  def testDbSince(txR: TxReport): Unit = {
    _testDb = Some(peerConn.db.since(txR.t))
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
    val txDataJava: jList[jList[_]] = txData.flatten.flatten.map(_.toJava).asJava
    _testDb = Some(peerConn.db.`with`(txDataJava).get(DB_AFTER).asInstanceOf[Database])
  }

  /** Use test database with temporary raw Java transaction data. */
  def testDbWith(txDataJava: jList[jList[AnyRef]]): Unit = {
    _testDb = Some(peerConn.db.`with`(txDataJava).get(DB_AFTER).asInstanceOf[Database])
  }

  /* testDbHistory not implemented.
   * Instead, use `testDbAsOfNow`, make changes and get historic data with getHistory calls.
   * */

  /** Get out of test mode and back to live db. */
  def useLiveDb: Unit = {
    _testDb = None
  }

  private def getAdhocDb: Database = {
    val baseDb : Database = _testDb.getOrElse(peerConn.db)
    val adhocDb: Database = _adhocDb.get match {
      case AsOf(TxLong(t))  => baseDb.asOf(t)
      case AsOf(TxDate(d))  => baseDb.asOf(d)
      case Since(TxLong(t)) => baseDb.since(t)
      case Since(TxDate(d)) => baseDb.since(d)
      case With(tx)         => {
        val txReport = TxReport_Peer(baseDb.`with`(tx))
        val db = txReport.dbAfter.asOf(txReport.t)
        db
      }
      case History          => baseDb.history()
    }
    _adhocDb = None
    adhocDb
  }

  /** Get current test/live db. Test db has preference. */
  def db: DatomicDb = {
    if (_adhocDb.isDefined) {
      // Return singleton adhoc db
      DatomicDb_Peer(getAdhocDb)
    } else if (_testDb.isDefined) {
      // Test db
      DatomicDb_Peer(_testDb.get)
    } else {
      // Live db
      DatomicDb_Peer(peerConn.db)
    }
  }

  def entity(id: Any): DatomicEntity = db.entity(this, id)

  /** Transact Seq of Seqs of [[Statement]]s
    *
    * @param stmtss
    * @return [[TxReport TxReport]]
    */
  def transact(stmtss: Seq[Seq[Statement]]): TxReport = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      // In-memory "transaction"
      TxReport_Peer(getAdhocDb.`with`(javaStmts), stmtss)

    } else if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport_Peer(_testDb.get.`with`(javaStmts), stmtss)

      // Continue with updated in-memory db
      // For some reason we need to "cast" it to time t
      val dbAfter = txReport.dbAfter.asOf(txReport.t)
      _testDb = Some(dbAfter)
      txReport

    } else {
      // Live transaction
      TxReport_Peer(peerConn.transact(javaStmts).get, stmtss)
    }
  }


  def transactAsync(stmtss: Seq[Seq[Statement]])
                   (implicit ec: ExecutionContext): Future[TxReport] = {
    val javaStmts: jList[jList[_]] = toJava(stmtss)

    if (_adhocDb.isDefined) {
      Future {
        TxReport_Peer(getAdhocDb.`with`(javaStmts), stmtss)
      }

    } else if (_testDb.isDefined) {
      Future {
        // In-memory "transaction"
        val txReport = TxReport_Peer(_testDb.get.`with`(javaStmts), stmtss)

        // Continue with updated in-memory db
        // For some reason we need to "cast" it to time t
        val dbAfter = txReport.dbAfter.asOf(txReport.t)
        _testDb = Some(dbAfter)
        txReport
      }

    } else {
      // Live transaction
      val moleculeInvocationFuture = try {
        bridgeDatomicFuture(peerConn.transactAsync(javaStmts))
      } catch {
        case NonFatal(ex) => Future.failed(ex)
      }
      moleculeInvocationFuture map { moleculeInvocationResult: java.util.Map[_, _] =>
        TxReport_Peer(moleculeInvocationResult, stmtss)
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
  def transact(rawTxStmts: jList[_]): TxReport = {
    if (_testDb.isDefined) {
      // In-memory "transaction"
      val txReport = TxReport_Peer(_testDb.get.`with`(rawTxStmts))
      // Continue with updated in-memory db
      _testDb = Some(txReport.dbAfter.asOf(txReport.t))
      txReport
    } else {
      // Live transaction
      TxReport_Peer(peerConn.transact(rawTxStmts).get)
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
  def transactAsync(rawTxStmts: jList[_])
                   (implicit ec: ExecutionContext): Future[TxReport] = {

    if (_testDb.isDefined) {
      Future {
        // In-memory "transaction"
        val txReport = TxReport_Peer(_testDb.get.`with`(rawTxStmts))

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
        bridgeDatomicFuture(peerConn.transactAsync(rawTxStmts))
      } catch {
        case NonFatal(ex) => Future.failed(ex)
      }
      moleculeInvocationFuture map { moleculeInvocationResult: java.util.Map[_, _] =>
        TxReport_Peer(moleculeInvocationResult)
      }
    }
  }


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
  def qRaw(db: DatomicDb, query: String, inputs0: Seq[Any]): jCollection[jList[AnyRef]] = {
    val inputs = inputs0.map {
      case it: Iterable[_] => it.asJava
      case v               => v
    }
    blocking(Peer.q(query, db.getDatomicDb +: inputs.asInstanceOf[Seq[AnyRef]]: _*))
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
    * */
  def query(model: Model, query: Query): jCollection[jList[AnyRef]] = model.elements.head match {
    case Generic("Log" | "EAVT" | "AEVT" | "AVET" | "VAET", _, _, _) => _index(model)
    case _                                                           => _query(model, query)
  }

  // Datalog query execution
  def _query(model: Model, query: Query, _db: Option[DatomicDb] = None): jCollection[jList[AnyRef]] = {
    val optimizedQuery  = QueryOptimizer(query)
    val p               = (expr: QueryExpr) => Query2String(optimizedQuery).p(expr)
    val rules           = "[" + (query.i.rules map p mkString " ") + "]"
    val adhocDb         = _db.getOrElse(db).getDatomicDb
    val first           = if (query.i.rules.isEmpty) Seq(adhocDb) else Seq(adhocDb, rules)
    val inputsEvaluated = QueryOpsClojure(query).inputsWithKeyword
    val allInputs       = first ++ inputsEvaluated
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
  def _index(model: Model): jCollection[jList[AnyRef]] = {
    val (api, index, args) = model.elements.head match {
      case Generic("EAVT", _, _, value) =>
        ("datoms", datomic.Database.EAVT, value match {
          case NoValue                   => Seq()
          case Eq(Seq(e))                => Seq(e)
          case Eq(Seq(e, a))             => Seq(e, a)
          case Eq(Seq(e, a, v))          => Seq(e, a, v)
          case Eq(Seq(e, a, v, d: Date)) => Seq(e, a, v, d)
          case Eq(Seq(e, a, v, t))       => Seq(e, a, v, t)
        })

      case Generic("AEVT", _, _, value) =>
        ("datoms", datomic.Database.AEVT, value match {
          case NoValue                   => Seq()
          case EntValue                  => Seq()
          case Eq(Seq(a))                => Seq(a)
          case Eq(Seq(a, e))             => Seq(a, e)
          case Eq(Seq(a, e, v))          => Seq(a, e, v)
          case Eq(Seq(a, e, v, d: Date)) => Seq(a, e, v, d)
          case Eq(Seq(a, e, v, t))       => Seq(a, e, v, t)
        })

      case Generic("AVET", attr, _, value) =>
        attr match {
          case "range" =>
            ("indexRange", "", value match {
              case Eq(Seq(a, None, None))  => Seq(a, null, null)
              case Eq(Seq(a, from, None))  => Seq(a, from, null)
              case Eq(Seq(a, None, until)) => Seq(a, null, until)
              case Eq(Seq(a, from, until)) =>
                if (from.getClass != until.getClass)
                  throw new MoleculeException("Please supply range arguments of same type as attribute.")
                Seq(a, from, until)
            })
          case _       =>
            ("datoms", datomic.Database.AVET, value match {
              case NoValue                   => Seq()
              case Eq(Seq(a))                => Seq(a)
              case Eq(Seq(a, v))             => Seq(a, v)
              case Eq(Seq(a, v, e))          => Seq(a, v, e)
              case Eq(Seq(a, v, e, d: Date)) => Seq(a, v, e, d)
              case Eq(Seq(a, v, e, t))       => Seq(a, v, e, t)
            })
        }

      case Generic("VAET", _, _, value) =>
        ("datoms", datomic.Database.VAET, value match {
          case NoValue                   => Seq()
          case Eq(Seq(v))                => Seq(v)
          case Eq(Seq(v, a))             => Seq(v, a)
          case Eq(Seq(v, a, e))          => Seq(v, a, e)
          case Eq(Seq(v, a, e, d: Date)) => Seq(v, a, e, d)
          case Eq(Seq(v, a, e, t))       => Seq(v, a, e, t)
        })

      case Generic("Log", _, _, value) =>
        ("txRange", "", value match {
          case Eq(Seq(from: Int, until: Int))   => Seq(from, until)
          case Eq(Seq(from: Int, until: Long))  => Seq(from, until)
          case Eq(Seq(from: Int, until: Date))  => Seq(from, until)
          case Eq(Seq(from: Long, until: Int))  => Seq(from, until)
          case Eq(Seq(from: Long, until: Long)) => Seq(from, until)
          case Eq(Seq(from: Long, until: Date)) => Seq(from, until)
          case Eq(Seq(from: Date, until: Int))  => Seq(from, until)
          case Eq(Seq(from: Date, until: Long)) => Seq(from, until)
          case Eq(Seq(from: Date, until: Date)) => Seq(from, until)

          case Eq(Seq(from: Int, None))  => Seq(from, null)
          case Eq(Seq(from: Long, None)) => Seq(from, null)
          case Eq(Seq(from: Date, None)) => Seq(from, null)

          case Eq(Seq(None, until: Int))  => Seq(null, until)
          case Eq(Seq(None, until: Long)) => Seq(null, until)
          case Eq(Seq(None, until: Date)) => Seq(null, until)

          // All !!
          case Eq(Seq(None, None)) => Seq(null, null)

          // From until end
          case Eq(Seq(from: Int))  => Seq(from, null)
          case Eq(Seq(from: Long)) => Seq(from, null)
          case Eq(Seq(from: Date)) => Seq(from, null)

          // All !!
          case Eq(Nil) => Seq(null, null)

          case Eq(other) => throw new MoleculeException(
            "Args to Log can only be t, tx or txInstant of type Int/Long/Date. Found: " + other)
        })

      case other => throw new MoleculeException(s"Only Index queries accepted (EAVT, AEVT, AVET, VAET, Log). Found `$other`")
    }

    // This one is important for Peer to keep db stable when
    // mixing filters with getHistory!
    val adhocDb = db

    def datomElement(tOpt: Option[Long], attr: String): Datom => Any = attr match {
      case "e"                   => (d: Datom) => d.e
      case "a"                   => (d: Datom) => adhocDb.getDatomicDb.asInstanceOf[Database].ident(d.a).toString
      case "v"                   => (d: Datom) => d.v
      case "t" if tOpt.isDefined => (_: Datom) => tOpt.get
      case "t"                   => (d: Datom) => toT(d.tx)
      case "tx"                  => (d: Datom) => d.tx
      case "txInstant"           => (d: Datom) => adhocDb.entity(this, d.tx).value(":db/txInstant").asInstanceOf[Date]
      case "op"                  => (d: Datom) => d.added
    }

    val attrs: Seq[String] = model.elements.collect {
      case Generic(_, attr, _, _)
        if attr != "args_" && attr != "range" => attr
    }

    def datom2row(tOpt: Option[Long]): Datom => jList[AnyRef] = attrs.length match {
      case 1 =>
        val x1 = datomElement(tOpt, attrs.head)
        (d: Datom) => list(x1(d)).asInstanceOf[jList[AnyRef]]

      case 2 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        (d: Datom) => list(x1(d), x2(d)).asInstanceOf[jList[AnyRef]]

      case 3 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        val x3 = datomElement(tOpt, attrs(2))
        (d: Datom) => list(x1(d), x2(d), x3(d)).asInstanceOf[jList[AnyRef]]

      case 4 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        val x3 = datomElement(tOpt, attrs(2))
        val x4 = datomElement(tOpt, attrs(3))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d)).asInstanceOf[jList[AnyRef]]

      case 5 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        val x3 = datomElement(tOpt, attrs(2))
        val x4 = datomElement(tOpt, attrs(3))
        val x5 = datomElement(tOpt, attrs(4))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d)).asInstanceOf[jList[AnyRef]]

      case 6 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        val x3 = datomElement(tOpt, attrs(2))
        val x4 = datomElement(tOpt, attrs(3))
        val x5 = datomElement(tOpt, attrs(4))
        val x6 = datomElement(tOpt, attrs(5))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d), x6(d)).asInstanceOf[jList[AnyRef]]

      case 7 =>
        val x1 = datomElement(tOpt, attrs.head)
        val x2 = datomElement(tOpt, attrs(1))
        val x3 = datomElement(tOpt, attrs(2))
        val x4 = datomElement(tOpt, attrs(3))
        val x5 = datomElement(tOpt, attrs(4))
        val x6 = datomElement(tOpt, attrs(5))
        val x7 = datomElement(tOpt, attrs(6))
        (d: Datom) => list(x1(d), x2(d), x3(d), x4(d), x5(d), x6(d), x7(d)).asInstanceOf[jList[AnyRef]]
    }

    // Convert Datoms to standard list of rows so that we can use the same Molecule query API
    val jColl: jCollection[jList[AnyRef]] = new util.ArrayList[jList[AnyRef]]()
    api match {
      case "datoms" =>
        val datom2row_ : Datom => jList[AnyRef] = datom2row(None)
        val raw        : lang.Iterable[Datom]   =
          adhocDb.asInstanceOf[DatomicDb_Peer].datoms(index, args: _*)
        raw.forEach { datom =>
          jColl.add(datom2row_(datom))
        }

      case "indexRange" =>
        val datom2row_ : Datom => jList[AnyRef] = datom2row(None)
        val attrId     : String                 = args.head.toString
        val startValue : Any                    = args(1)
        val endValue   : Any                    = args(2)
        val raw        : lang.Iterable[Datom]   =
          adhocDb.asInstanceOf[DatomicDb_Peer].indexRange(attrId, startValue, endValue)
        raw.forEach { datom =>
          jColl.add(datom2row_(datom))
        }

      case "txRange" =>
        val raw = peerConn.log.txRange(args.head, args(1))
        raw.forEach { txMap =>
          // Flatten transaction datoms to unified tuples return type
          txMap.get(datomic.Log.DATA).asInstanceOf[jList[Datom]].forEach { datom =>
            val datom2row_ = datom2row(Some(txMap.get(datomic.Log.T).asInstanceOf[Long]))
            jColl.add(datom2row_(datom))
          }
        }
    }
    jColl
  }

  def sync: ListenableFuture[Database] = peerConn.sync()
  def sync(t: Long): ListenableFuture[Database] = peerConn.sync(t)
  def syncIndex(t: Long): ListenableFuture[Database] = peerConn.syncIndex(t)
}
