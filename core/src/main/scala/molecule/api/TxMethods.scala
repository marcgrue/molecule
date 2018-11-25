package molecule.api

import java.util
import java.util.concurrent.{ExecutionException => ExecutionExc}
import datomic.Peer.function
import datomic.Util
import datomic.Util.{list, read}
import molecule.ast.MoleculeBase
import molecule.ast.model.{Composite, Model, TxMetaData}
import molecule.ast.transactionModel.Statement
import molecule.facade.{Conn, TxReport}
import molecule.macros.TxFunctionCall
import molecule.macros.exception.TxFnException
import molecule.transform.Model2Transaction
import molecule.util.Helpers
import scala.concurrent.{ExecutionContext, Future, Promise, blocking}
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}
import scala.util.control.NonFatal


/** Transactional methods for bundled transactions and tx functions
  *
  * @groupname bundled Bundled transactions
  * @groupdesc bundled Multiple molecule operations in one atomic transaction.
  * @groupprio bundled 1
  * @groupname txfn Transaction functions
  * @groupdesc txfn Atomic transaction logic with access to tx database value.
  * @groupprio txfn 2
  */
trait TxMethods {

  /** Transact bundled transaction statements
    * <br><br>
    * Supply transaction statements of one or more molecule actions to perform a single atomic transaction.
    * {{{
    *   transact(
    *     // retract entity
    *     e1.getRetractTx,
    *     // save new entity
    *     Ns.int(4).getSaveTx,
    *     // insert multiple new entities
    *     Ns.int.getInsertTx(List(5, 6)),
    *     // update entity
    *     Ns(e2).int(20).getUpdateTx
    *   )
    * }}}
    *
    * @group bundled
    * @param stmtss [[molecule.ast.transactionModel.Statement Statement]]'s from multiple molecule operations
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return [[molecule.facade.TxReport TxReport]] with result of transaction
    */
  def transact(stmtss: Seq[Seq[Statement]]*)(implicit conn: Conn): TxReport =
    conn.transact(stmtss.flatten)


  /** Asynchronously transact bundled transaction statements
    *
    * Supply transaction statements of one or more molecule actions to asynchronously
    * transact a single atomic transaction.
    * {{{
    *   Await.result(
    *     transactAsync(
    *       e1.getRetractTx,
    *       Ns.int(4).getSaveTx,
    *       Ns.int.getInsertTx(List(5, 6)),
    *       Ns(e2).int(20).getUpdateTx
    *     ) map { bundleTx =>
    *       Ns.int.getAsync map { queryResult =>
    *         queryResult === List(3, 4, 5, 6, 20)
    *       }
    *     },
    *     2.seconds
    *   )
    * }}}
    *
    * @group bundled
    * @param stmtss [[molecule.ast.transactionModel.Statement Statement]]'s from multiple molecule operations
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Future with [[molecule.facade.TxReport TxReport]] with result of transaction
    */
  def transactAsync(stmtss: Seq[Seq[Statement]]*)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] =
    conn.transactAsync(stmtss.flatten)


  /** Debug bundled transaction statements
    * <br><br>
    * Add transaction statements from one or more molecule actions to `debugTransact`
    * to see the bundled transaction statements.
    * {{{
    * debugTransact(
    *   // retract
    *   e1.getRetractTx,
    *   // save
    *   Ns.int(4).getSaveTx,
    *   // insert
    *   Ns.int.getInsertTx(List(5, 6)),
    *   // update
    *   Ns(e2).int(20).getUpdateTx
    * )
    *
    * // Prints transaction data to output:
    * /*
    *   ## 1 ## TxReport
    *   ========================================================================
    *   1          ArrayBuffer(
    *     1          List(
    *       1          :db.fn/retractEntity   17592186045445)
    *     2          List(
    *       1          :db/add       #db/id[:db.part/user -1000247]     :ns/int          4           Card(1))
    *     3          List(
    *       1          :db/add       #db/id[:db.part/user -1000252]     :ns/int          5           Card(1))
    *     4          List(
    *       1          :db/add       #db/id[:db.part/user -1000253]     :ns/int          6           Card(1))
    *     5          List(
    *       1          :db/add       17592186045446                     :ns/int          20          Card(1)))
    *   ------------------------------------------------
    *   2          List(
    *     1    1     added: true ,   t: 13194139534345,   e: 13194139534345,   a: 50,   v: Wed Nov 14 23:38:15 CET 2018
    *
    *     2    2     added: false,  -t: 13194139534345,  -e: 17592186045445,  -a: 64,  -v: 1
    *
    *     3    3     added: true ,   t: 13194139534345,   e: 17592186045450,   a: 64,   v: 4
    *
    *     4    4     added: true ,   t: 13194139534345,   e: 17592186045451,   a: 64,   v: 5
    *
    *     5    5     added: true ,   t: 13194139534345,   e: 17592186045452,   a: 64,   v: 6
    *
    *     6    6     added: true ,   t: 13194139534345,   e: 17592186045446,   a: 64,   v: 20
    *          7     added: false,  -t: 13194139534345,  -e: 17592186045446,  -a: 64,  -v: 2)
    *   ========================================================================
    * */
    * }}}
    *
    * @group bundled
    * @param stmtss [[molecule.ast.transactionModel.Statement Statement]]'s from multiple molecule operations
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugTransact(stmtss: Seq[Seq[Statement]]*)(implicit conn: Conn): Unit = {
    // Use temporary branch of db to not changing any live data
    conn.testDbWith()
    // Print tx report to console
    conn.transact(stmtss.flatten).debug
    conn.useLiveDb
  }


  /** Transact tx function invocation
    * <br><br>
    * Macro that takes a tx function invocation itself as its argument. The tx function is analyzed
    * by the macro and the necessary transaction preparations done at compile time.
    * <br><br>
    * At runtime, the returned statements from the tx function is transacted as
    * one atomic transaction.
    * {{{
    * val txReport = transact(transfer(fromAccount, toAccount, 20))
    * }}}
    * Transaction meta data molecules can be added
    * {{{
    * // Add tx meta data that John did the transfer and that it is a scheduled transfer
    * transact(
    *   transfer(fromAccount, toAccount, 20),
    *   Person.name("John"),
    *   UseCase.name("Scheduled transfer"))
    *
    * // Query multiple Tx meta data molecules
    * Account(fromAccount).balance
    *   .Tx(Person.name_("John"))
    *   .Tx(UseCase.name_("Scheduled transfer")).get.head === 80
    * Account(toAccount).balance
    *   .Tx(Person.name_("John"))
    *   .Tx(UseCase.name_("Scheduled transfer")).get.head === 720
    * }}}
    *
    * @group txfn
    * @param txFnCall    Tx function invocation
    * @param txMolecules Optional tx meta data molecules
    * @return [[molecule.facade.TxReport TxReport]] with result of transaction
    */
  def transact(txFnCall: Seq[Seq[Statement]], txMolecules: MoleculeBase*): TxReport = macro TxFunctionCall.txFnCall


  /** Asynchronously transact tx function invocation
    * <br><br>
    * Macro that takes a tx function invocation itself as its argument. The tx function is analyzed
    * by the macro and the necessary transaction preparations done at compile time.
    * <br><br>
    * At runtime, the returned statements from the tx function is asynchronously transacted as
    * one atomic transaction using Datomic's asynchronous API.
    * {{{
    * Await.result(
    *   transactAsync(transfer(fromAccount, toAccount, 20)) map { txReport =>
    *     Account(fromAccount).balance.get.head === 80 // (could be asynchronous too)
    *     Account(toAccount).balance.get.head === 720
    *   },
    *   2.seconds
    * )
    * }}}
    * Additional transaction meta data can be added
    * {{{
    * Await.result(
    *   transactAsync(
    *     transfer(fromAccount, toAccount, 20),
    *     Person.name("John"),
    *     UseCase.name("Scheduled transfer")) map { txReport =>
    *       Account(fromAccount).balance
    *       .Tx(Person.name_("John"))
    *       .Tx(UseCase.name_("Scheduled transfer"))
    *       .get.head === 80 // (could be asynchronous too)
    *   },
    *   2.seconds
    * )
    * }}}
    *
    * @group txfn
    * @param txFnCall    Tx function invocation
    * @param txMolecules Optional tx meta data molecules
    * @return Future with [[molecule.facade.TxReport TxReport]] with result of transaction
    */
  def transactAsync(txFnCall: Seq[Seq[Statement]], txMolecules: MoleculeBase*): TxReport = macro TxFunctionCall.asyncTxFnCall


  /** Debug tx function invocation
    * <br><br>
    * Print transaction statements to output of a tx function invocation
    * without affecting the live database.
    * {{{
    * // Print debug info for tx function invocation
    * debugTransact(transfer(fromAccount, toAccount, 20))
    *
    * // Prints produced tx statements to output:
    * /*
    * ## 1 ## TxReport
    * ========================================================================
    * 1          ArrayBuffer(
    *   1          List(
    *     1          :db/add       17592186045445       :account/balance    80        Card(1))
    *   2          List(
    *     1          :db/add       17592186045447       :account/balance    720       Card(1)))
    * ------------------------------------------------
    * 2          List(
    *   1    1     added: true ,   t: 13194139534345,   e: 13194139534345,   a: 50,   v: Thu Nov 22 16:23:09 CET 2018
    *
    *   2    2     added: true ,   t: 13194139534345,   e: 17592186045445,   a: 64,   v: 80
    *        3     added: false,  -t: 13194139534345,  -e: 17592186045445,  -a: 64,  -v: 100
    *
    *   3    4     added: true ,   t: 13194139534345,   e: 17592186045447,   a: 64,   v: 720
    *        5     added: false,  -t: 13194139534345,  -e: 17592186045447,  -a: 64,  -v: 700)
    * ========================================================================
    * */
    * }}}
    *
    * @group txfn
    * @param txFnCall    Tx function invocation
    * @param txMolecules Optional tx meta data molecules
    */
  def debugTransact(txFnCall: Seq[Seq[Statement]], txMolecules: MoleculeBase*): Unit = macro TxFunctionCall.debugTxFnCall
}


object TxMethods extends Helpers {

  def excMissingScalaJar(e: Throwable): String =
    s"""The Datomic transactor needs any dependencies in transactor
       |functions to be available on its classpath. Please copy the scala-library jar to the Datomic transactor lib.
       |Supposing $$DATOMIC_HOME points to your Datomic distribution folder you can likely run this command (on a mac):
       |cp ~/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-${moleculeBuildInfo.BuildInfo.scalaVersion}.jar $$DATOMIC_HOME/lib/
       |You might also want to copy the molecule library:
       |cp ~/.ivy2/cache/org.scalamolecule/molecule_2.12/${moleculeBuildInfo.BuildInfo.version}/jars/molecule_2.12.jar $$DATOMIC_HOME/lib/
       |You need to then restart the Datomic transactor.
       |${e.getMessage}""".stripMargin

  def excMissingMoleculeClass(e: Throwable): String =
    s"""The Datomic transactor needs any dependencies in transactor
       |functions to be available on its classpath. Please copy the Molecule library jar to the Datomic transactor lib.
       |Supposing $$DATOMIC_HOME points to your Datomic distribution folder you can likely run this command (on a mac):
       |cp ~/.ivy2/cache/org.scalamolecule/molecule_2.12/${moleculeBuildInfo.BuildInfo.version}/jars/molecule_2.12.jar $$DATOMIC_HOME/lib/
       |You need to then restart the Datomic transactor.
       |${e.getMessage}""".stripMargin

  def excMissingMoleculeMethod(e: Throwable): String = {
    val err = e.getMessage
    val method = err.substring(29, err.indexOf("("))
    s"""The Datomic transactor needs all used Scala transaction methods to be in its scope.
       |The Scala transaction method `$method` is not found by the transactor. So you may need to restart
       |the transactor (if using classpath) or make an updated jar with all transaction functions for the transactor lib.
       |${e.getMessage}""".stripMargin
  }

  def txFnException(e: Throwable): String = "Unexpected error when invoking transaction function:\n" + e.getCause


  def tryTransactTxFn(body: => TxReport) = try {
    blocking {
      body
    }
  } catch {
    case e: ExecutionExc => e.getMessage match {
      case msg if msg.startsWith("java.lang.NoClassDefFoundError: scala")    => throw new TxFnException(excMissingScalaJar(e))
      case msg if msg.startsWith("java.lang.NoClassDefFoundError: molecule") => throw new TxFnException(excMissingMoleculeClass(e))
      case msg if msg.startsWith("java.lang.NoSuchMethodError: molecule")    => throw new TxFnException(excMissingMoleculeMethod(e))
      case _                                                                 => throw new TxFnException(txFnException(e))
    }
    case NonFatal(e)     => throw new TxFnException(txFnException(e))
  }

  def buildTxFnInstall(txFn: String, args: Seq[Any]): util.Map[_, _] = {
    val params = args.indices.map(i => ('a' + i).toChar.toString)
    Util.map(
      read(":db/ident"), read(s":$txFn"),
      read(":db/fn"), function(Util.map(
        read(":lang"), "java",
        read(":params"), list(read("txDb") +: read("txMetaData") +: params.map(read): _*),
        read(":code"), s"return $txFn(txDb, txMetaData, ${params.mkString(", ")});"
      )),
    )
  }


  private[this] def txStmts(txMolecules: Seq[MoleculeBase], conn: Conn): Seq[Statement] = if (txMolecules.nonEmpty) {
    val txElements = txMolecules.flatMap { mol =>
      mol._model.elements.flatMap {
        case Composite(elements) => elements
        case element             => Seq(element)
      }
    }
    val txModel = Model(Seq(TxMetaData(txElements)))
    val txTransformer = Model2Transaction(conn, txModel)
    txTransformer.saveStmts()
  } else Nil


  /** Invoke transaction function call synchronously (blocks)
    *
    * See also non-blocking asynchronous implementation
    * */
  private[molecule] def txFnCall(txFn: String, txMolecules: Seq[MoleculeBase], args: Any*)(implicit conn: Conn): TxReport = tryTransactTxFn {

    // Install transaction function if not installed yet
    if (conn.datomicConn.db.entity(s":$txFn") == null)
      conn.datomicConn.transact(list(buildTxFnInstall(txFn, args))).get

    // Build transaction function call clause
    val txFnInvocationClause = list(list(read(txFn) +: txStmts(txMolecules, conn) +: args.map(_.asInstanceOf[AnyRef]): _*))

    // Invoke transaction function and retrieve result from ListenableFuture synchronously
    TxReport(conn.datomicConn.transact(txFnInvocationClause).get)
  }


  private[molecule] def debugTxFnCall(txFn: String, txMolecules: Seq[MoleculeBase], args: Any*)(implicit conn: Conn): Unit = {
    // Use temporary branch of db to not changing any live data
    conn.testDbWith()
    // Print tx report to console
    txFnCall(txFn, txMolecules, args: _*)(conn).debug
    conn.useLiveDb
  }


  private[molecule] def asyncTxFnCall(txFn: String, txMolecules: Seq[MoleculeBase], args: Any*)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = {
    val txFnInvocationfuture = try {
      val txFnInstallFuture = {
        // Install transaction function if not installed yet
        // todo: entity call is blocking - can we make it non-blocking too?
        if (conn.datomicConn.db.entity(s":$txFn") == null) {
          // Install tx function
          bridgeDatomicFuture(conn.datomicConn.transactAsync(list(buildTxFnInstall(txFn, args))))
        } else {
          // Tx function already installed
          Future.unit
        }
      }
      txFnInstallFuture flatMap { txFnInstall =>
        val p = Promise[java.util.Map[_, _]]
        val txFnInvocationClause = list(list(read(txFn) +: txStmts(txMolecules, conn) +: args.map(_.asInstanceOf[AnyRef]): _*))
        val txFnInvocationListenable = conn.datomicConn.transactAsync(txFnInvocationClause)
        txFnInvocationListenable.addListener(
          new java.lang.Runnable {
            override def run: Unit = {
              try {
                p.success(txFnInvocationListenable.get())
              } catch {
                case e: ExecutionExc => e.getMessage match {
                  case msg if msg.startsWith("java.lang.NoClassDefFoundError: scala")    => p.failure(throw new TxFnException(excMissingScalaJar(e)))
                  case msg if msg.startsWith("java.lang.NoClassDefFoundError: molecule") => p.failure(throw new TxFnException(excMissingMoleculeClass(e)))
                  case msg if msg.startsWith("java.lang.NoSuchMethodError: molecule")    => p.failure(throw new TxFnException(excMissingMoleculeMethod(e)))
                  case _                                                                 => p.failure(throw new TxFnException(txFnException(e)))
                }
                case NonFatal(e)     => p.failure(throw new TxFnException(txFnException(e)))
              }
              ()
            }
          },
          (arg0: Runnable) => ec.execute(arg0)
        )
        p.future
      }
    } catch {
      case NonFatal(e) => Future.failed(e)
    }

    // Wrap in TxReport
    txFnInvocationfuture map { txFnInvocationResult: java.util.Map[_, _] =>
      TxReport(txFnInvocationResult)
    }
  }
}

