package molecule.datomic.client.devLocal.facade

import java.util
import java.util.{Date, Collection => jCollection, List => jList}
import clojure.lang.Keyword
import molecule.core.facade.exception.DatomicFacadeException
//import datomic.Datom
//import datomic.{Connection, Database, Datom, Peer}
//import datomic.Connection.DB_AFTER
import datomic.Peer._
import datomic.Util._
import datomic.db.DbId
import datomicScala.client.api
import datomicScala.client.api.sync.{Client, Db, Datomic => clientDatomic}
import datomicScala.client.api.Datom
import molecule.core.ast.model._
import molecule.core.ast.query.{Query, QueryExpr}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel._
import molecule.core.exceptions._
import molecule.core.ops.QueryOps._
import molecule.core.transform.{Query2String, QueryOptimizer}
import molecule.core.util.{BridgeDatomicFuture, Helpers}
import molecule.datomic.base.facade.{Conn, DatomicDb, TxReport}
import molecule.datomic.peer.facade.{DatomicDb_Peer, TxReport_Peer}
import org.slf4j.LoggerFactory
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal
import datomicScala.client.api.sync
import molecule.core.api.DatomicEntity


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

  def getT(tx: Long): Long = clientConn.db.asOf(tx).asOfT

  def liveDbUsed: Boolean = _adhocDb.isEmpty && _testDb.isEmpty

  //  def testDb(db: Db): Unit = {
  //    _testDb = Some(db)
  //  }
  def testDb(db: DatomicDb): Unit = {
    _testDb = Some(db.asInstanceOf[DatomicDb_DevLocal].clientDb)
  }

  def testDbAsOf(t: Long): Unit = {
    _testDb = Some(clientConn.db.asOf(t))
  }

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
      DatomicDb_DevLocal(getAdhocDb)
    } else if (_testDb.isDefined) {
      // Test db
      DatomicDb_DevLocal(_testDb.get)
    } else {
      // Live db
      DatomicDb_DevLocal(clientConn.db)
    }
  }

  def entity(id: Any): DatomicEntity = db.entity(this, id)


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
    val adhocDb        = _db.getOrElse(db).asInstanceOf[DatomicDb_DevLocal].clientDb
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

  // Datoms API providing direct access to indexes
  def _index(model: Model): jCollection[jList[AnyRef]] = {
    val (api, index, args) = model.elements.head match {
      case Generic("EAVT", _, _, value) =>
        ("datoms", ":eavt", value match {
          case Eq(Seq(e))                => Seq(e.asInstanceOf[Object])
          case Eq(Seq(e, a))             => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object])
          case Eq(Seq(e, a, v))          => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object])
          case Eq(Seq(e, a, v, d: Date)) => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(e, a, v, t))       => Seq(e.asInstanceOf[Object], a.asInstanceOf[Object], v.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("AEVT", _, _, value) =>
        ("datoms", ":aevt", value match {
          case Eq(Seq(a))                => Seq(a.asInstanceOf[Object])
          case Eq(Seq(a, e))             => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object])
          case Eq(Seq(a, e, v))          => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object])
          case Eq(Seq(a, e, v, d: Date)) => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(a, e, v, t))       => Seq(a.asInstanceOf[Object], e.asInstanceOf[Object], v.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("AVET", attr, _, value) =>
        attr match {
          case "range" =>
            ("indexRange", "", value match {
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
            ("datoms", ":avet", value match {
              case Eq(Seq(a))                => Seq(a.asInstanceOf[Object])
              case Eq(Seq(a, v))             => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object])
              case Eq(Seq(a, v, e))          => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object])
              case Eq(Seq(a, v, e, d: Date)) => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object], d.asInstanceOf[Object])
              case Eq(Seq(a, v, e, t))       => Seq(a.asInstanceOf[Object], v.asInstanceOf[Object], e.asInstanceOf[Object], t.asInstanceOf[Object])
            })
        }

      case Generic("VAET", _, _, value) =>
        ("datoms", ":vaet", value match {
          case Eq(Seq(v))                => Seq(v.asInstanceOf[Object])
          case Eq(Seq(v, a))             => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object])
          case Eq(Seq(v, a, e))          => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object])
          case Eq(Seq(v, a, e, d: Date)) => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object], d.asInstanceOf[Object])
          case Eq(Seq(v, a, e, t))       => Seq(v.asInstanceOf[Object], a.asInstanceOf[Object], e.asInstanceOf[Object], t.asInstanceOf[Object])
        })

      case Generic("Log", _, _, value) =>
        val intException = new DatomicFacadeException(
          "Dev local implementation doesn't accept time t. Please use tx or txInst (Date) instead.")
        val txMin        = 1234567890
        ("txRange", ":vaet", value match {
          case Eq(Seq(_: Int))                     => throw intException
          case Eq(Seq(from: Long)) if from < txMin => throw intException

          case Eq(Seq(_: Int, _))                       => throw intException
          case Eq(Seq(_, _: Int))                       => throw intException
          case Eq(Seq(from: Long, _)) if from < txMin   => throw intException
          case Eq(Seq(_, until: Long)) if until < txMin => throw intException

          // All !!
          case Eq(Nil)             => Seq(None, None)
          case Eq(Seq(None, None)) => Seq(None, None)

          // From until end
          case Eq(Seq(from)) => Seq(Some(from), None)

          // Range
          case Eq(Seq(from, until)) => Seq(Some(from), Some(until))

          case Eq(_) => throw new MoleculeException("Args to Log can only be t, tx or txInstant of type Int/Long/Date")
        })

      case other => throw new MoleculeException(s"Only Index queries accepted (EAVT, AEVT, AVET, VAET, Log). Found `$other`")
    }

    val adhocDb = db

    def attrName(e: Long): String = {
      q(
        """[:find  ?fullAttr
          | :in $ ?e
          | :where [?e ?a _]
          |        [?a :db/ident ?ident]
          |        [(str ?ident) ?fullAttr]]""".stripMargin, e
      ).head.head.toString
    }

    def date(tx: Long): Date = {
      q(
        """[:find ?txInstant :in $ ?e :where [?tx :db/txInstant ?txInstant]]""".stripMargin, tx
      ).head.head.asInstanceOf[Date]
    }

    def datomElement(tOpt: Option[Long], attr: String): Datom => Any = attr match {
      case "e"                   => (d: Datom) => d.e
      case "a"                   => (d: Datom) => attrName(d.e)
      case "v"                   => (d: Datom) => d.v
      case "t" if tOpt.isDefined => (_: Datom) => tOpt.get // use provided t
      case "t"                   => (d: Datom) => getT(d.tx) // awful hack until some txToT method is available
      case "tx"                  => (d: Datom) => d.tx
      case "txInstant"           => (d: Datom) => date(d.tx)
      case "op"                  => (d: Datom) => d.added
    }

    val attrs: Seq[String] = model.elements.tail.collect {
      case Generic(_, attr, _, _) => attr
    }

    def datom2row(tOpt: Option[Long]): Datom => jList[AnyRef] = attrs.length match {
      case 1 =>
        val x1 = datomElement(tOpt, attrs.head)
        (d: Datom) =>
          list(x1(d)).asInstanceOf[jList[AnyRef]]

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
      case "datoms"     =>
        val datom2row_ = datom2row(None)
        adhocDb.asInstanceOf[DatomicDb_DevLocal].datoms(index, args: _*)
          .asInstanceOf[java.util.stream.Stream[Datom]].forEach { datom =>
          jColl.add(datom2row_(datom))
        }
      case "indexRange" =>
        val datom2row_ = datom2row(None)
        val (arg1, arg2) = (args(1).asInstanceOf[Long], args(2).asInstanceOf[Long])

        // todo - is it like this?
        val start = if (arg1 == 0) None else Some(arg1)
        val end   = if (arg1 == 0) None else Some(arg2)
        adhocDb.asInstanceOf[DatomicDb_DevLocal].indexRange(args.head.toString, start, end)
          .asInstanceOf[java.util.stream.Stream[Datom]].forEach { datom =>
          jColl.add(datom2row_(datom))
        }
      case "txRange"    =>
        val (from, until) = (args.head.asInstanceOf[Option[Any]], args(1).asInstanceOf[Option[Any]])
        // Flatten transaction datoms to unified tuples return type
        clientConn.txRangeArray(from, until).foreach {
          case (t, datoms) =>
            // Use t from txRange result
            val datom2row_ = datom2row(Some(t))
            datoms.foreach { datom =>
              jColl.add(datom2row_(datom))
            }
        }
    }
    jColl
  }
}