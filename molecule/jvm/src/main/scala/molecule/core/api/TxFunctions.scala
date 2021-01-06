package molecule.core.api

import java.util
import java.util.concurrent.{ExecutionException => ExecutionExc}
import datomic.Peer.function
import datomic.Util
import datomic.Util.{list, read}
import molecule.core.ast.MoleculeBase
import molecule.core.ast.model.{Composite, Model, TxMetaData}
import molecule.core.ast.transactionModel.Statement
import molecule.core.macros.TxFunctionCall
import molecule.core.macros.exception.TxFnException
import molecule.core.transform.Model2Transaction
import molecule.core.util.{BridgeDatomicFuture, Helpers}
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.language.experimental.macros
import scala.language.implicitConversions
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
trait TxFunctions {

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
    * @return [[molecule.datomic.base.facade.TxReport TxReport]] with result of transaction
    */
  def transactFn(
    txFnCall: Seq[Seq[Statement]],
    txMolecules: MoleculeBase*
  ): TxReport = macro TxFunctionCall.txFnCall


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
    * @return Future with [[molecule.datomic.base.facade.TxReport TxReport]] with result of transaction
    */
  def transactFnAsync(
    txFnCall: Seq[Seq[Statement]],
    txMolecules: MoleculeBase*
  ): Future[TxReport] = macro TxFunctionCall.asyncTxFnCall


  /** Inspect tx function invocation
    * <br><br>
    * Print transaction statements to output of a tx function invocation
    * without affecting the live database.
    * {{{
    * // Print inspect info for tx function invocation
    * inspectTransact(transfer(fromAccount, toAccount, 20))
    *
    * // Prints produced tx statements to output:
    * /*
    * ## 1 ## TxReport
    * ========================================================================
    * 1          ArrayBuffer(
    *   1          List(
    *     1          :db/add       17592186045445       :Account/balance    80        Card(1))
    *   2          List(
    *     1          :db/add       17592186045447       :Account/balance    720       Card(1)))
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
  def inspectTransactFn(
    txFnCall: Seq[Seq[Statement]],
    txMolecules: MoleculeBase*
  ): Unit = macro TxFunctionCall.inspectTxFnCall
}


object TxFunctions extends Helpers with BridgeDatomicFuture {
  def excMissingScalaJar(e: Throwable): String =
    s"""The Datomic transactor needs any dependencies in transactor
       |functions to be available on its classpath. Please copy the scala-library jar to the Datomic transactor lib.
       |Supposing $$DATOMIC_HOME points to your Datomic distribution folder you can likely run this command (on a mac):
       |cp ~/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-<scala-version>.jar $$DATOMIC_HOME/lib/
       |You might also want to copy the molecule library:
       |cp ~/.ivy2/cache/org.scalamolecule/molecule_2.13/<project-version>/jars/molecule_2.13.jar $$DATOMIC_HOME/lib/
       |You need to then restart the Datomic transactor.
       |${e.getMessage}""".stripMargin

  def excMissingMoleculeClass(e: Throwable): String =
    s"""The Datomic transactor needs any dependencies in transactor
       |functions to be available on its classpath. Please copy the Molecule library jar to the Datomic transactor lib.
       |Supposing $$DATOMIC_HOME points to your Datomic distribution folder you can likely run this command (on a mac):
       |cp ~/.ivy2/cache/org.scalamolecule/molecule_2.13/<project-version>/jars/molecule_2.13.jar $$DATOMIC_HOME/lib/
       |You need to then restart the Datomic transactor.
       |${e.getMessage}""".stripMargin

  def excMissingMoleculeMethod(e: Throwable): String = {
    val err    = e.getMessage
    val method = err.substring(29, err.indexOf("("))
    s"""The Datomic transactor needs all used Scala transaction methods to be in its scope.
       |The Scala transaction method `$method` is not found by the transactor. So you may need to restart
       |the transactor (if using classpath) or make an updated jar with all transaction functions for the transactor lib.
       |${e.getMessage}""".stripMargin
  }

  def txFnException(e: Throwable): String = "Unexpected error when invoking transaction function:\n" + e.getCause

  val redundant = "molecule.core.macros.exception.TxFnException: ".size

  def tryTransactTxFn(body: => TxReport): TxReport = try {
    blocking {
      body
    }
  } catch {
    case e: ExecutionExc => e.getMessage match {
      case msg if msg.startsWith("java.lang.NoClassDefFoundError: scala") =>
        throw new TxFnException(excMissingScalaJar(e))

      case msg if msg.startsWith("java.lang.NoClassDefFoundError: molecule") =>
        throw new TxFnException(excMissingMoleculeClass(e))

      case msg if msg.startsWith("java.lang.NoSuchMethodError: molecule") =>
        throw new TxFnException(excMissingMoleculeMethod(e))

      case _ => throw new TxFnException(e.getMessage.drop(redundant))
    }

    case NonFatal(e) => throw new TxFnException(e.getMessage.drop(redundant))
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


  private[this] def txStmts(
    txMolecules: Seq[MoleculeBase],
    conn: Conn
  ): Seq[Statement] = if (txMolecules.nonEmpty) {
    val txElements    = txMolecules.flatMap { mol =>
      mol._model.elements.flatMap {
        case Composite(elements) => elements
        case element             => Seq(element)
      }
    }
    val txModel       = Model(Seq(TxMetaData(txElements)))
    val txTransformer = Model2Transaction(conn, txModel)
    txTransformer.saveStmts()
  } else Nil


  /** Invoke transaction function call synchronously (blocks)
    *
    * See also non-blocking asynchronous implementation
    * */
  private[molecule] def txFnCall(
    txFn: String,
    txMolecules: Seq[MoleculeBase],
    args: Any*
  )(implicit conn: Conn): TxReport = tryTransactTxFn {

    // Install transaction function if not installed yet
    if (conn.db.pull("[*]", read(s":$txFn")).size() == 1) {
      conn.transact(list(buildTxFnInstall(txFn, args)))
    }

    // Build transaction function call clause
    val txFnInvocationClauses = list(list(read(txFn) +:
      txStmts(txMolecules, conn) +: args.map(_.asInstanceOf[AnyRef]): _*))

    // Invoke transaction function and retrieve result from ListenableFuture synchronously
    conn.transact(txFnInvocationClauses)
  }


  private[molecule] def inspectTxFnCall(
    txFn: String,
    txMolecules: Seq[MoleculeBase],
    args: Any*
  )(implicit conn: Conn): Unit = {
    // Use temporary branch of db to not changing any live data
    conn.testDbWith()
    // Print tx report to console
    txFnCall(txFn, txMolecules, args: _*)(conn).inspect
    conn.useLiveDb
  }


  private[molecule] def asyncTxFnCall(
    txFn: String,
    txMolecules: Seq[MoleculeBase],
    args: Any*
  )(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = try {
    val txFnInstallFuture: Future[Any] = {
      // Install transaction function if not installed yet
      // todo: pull call is blocking - can we make it non-blocking too?
      if (conn.db.pull("[*]", read(s":$txFn")).size() == 1) {
        // Install tx function
        conn.transactAsync(list(buildTxFnInstall(txFn, args)))
      } else {
        // Tx function already installed
        Future.unit
      }
    }

    txFnInstallFuture flatMap { txFnInstalled =>
      // Build transaction function call clause
      val txFnInvocationClause = list(list(read(txFn) +:
        txStmts(txMolecules, conn) +: args.map(_.asInstanceOf[AnyRef]): _*))
      conn.transactAsync(txFnInvocationClause)
    }
  } catch {
    case NonFatal(e) => Future.failed(e)
  }
}

