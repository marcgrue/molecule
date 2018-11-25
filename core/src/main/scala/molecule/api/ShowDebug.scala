package molecule.api

import java.util.{Date, List => jList}
import molecule.ast.query.QueryExpr
import molecule.ast.tempDb._
import molecule.ast.transactionModel.Statement
import molecule.exceptions.QueryException
import molecule.facade.{Conn, TxReport}
import molecule.ops.QueryOps._
import molecule.ops.VerifyModel
import molecule.transform._
import molecule.util.Debug
import scala.collection.JavaConverters._
import scala.language.implicitConversions

/** Debug methods
  *
  * Call a debug method on a molecule to see the internal transformations and
  * produced transaction statements or sample data.
  * */
trait ShowDebug[Tpl] { self: Molecule[Tpl] =>


  /** Debug call to `get` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGet(implicit conn: Conn): Unit = {
    val ins = QueryOps(_query).inputs
    val p = (expr: QueryExpr) => Query2String(_query).p(expr)
    val rules = "[" + (_query.i.rules map p mkString " ") + "]"
    val first = if (_query.i.rules.isEmpty) Seq(conn.db) else Seq(conn.db, rules)
    val allInputs: Seq[AnyRef] = first ++ ins
    val rows = try {
      conn.query(_model, _query).asScala.take(500)
    } catch {
      case ex: Throwable => throw new QueryException(ex, _model, _query, allInputs, p)
    }
    val rulesOut: String = if (_query.i.rules.isEmpty) "none\n\n" else "[\n " + _query.i.rules.map(Query2String(_query).p(_)).mkString("\n ") + "\n]\n\n"
    val inputs: String = if (ins.isEmpty) "none\n\n" else "\n" + ins.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n") + "\n\n"
    val outs: String = rows.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n")
    println(
      "\n--------------------------------------------------------------------------\n" +
        _model + "\n\n" +
        _query + "\n\n" +
        _query.datalog + "\n\n" +
        "RULES: " + rulesOut +
        "INPUTS: " + inputs +
        "OUTPUTS:\n" + outs + "\n(showing up to 500 rows...)" +
        "\n--------------------------------------------------------------------------\n"
    )
  }


  /** Debug call to `getAsOf(t)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(t: Long)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxLong(t))))


  /** Debug call to `getAsOf(tx)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(tx: TxReport)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Debug call to `getAsOf(date)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(date: Date)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxDate(date))))


  /** Debug call to `getSince(t)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(t: Long)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxLong(t))))


  /** Debug call to `getSince(tx)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(tx: TxReport)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Debug call to `getSince(date)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(date: Date)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxDate(date))))


  /** Debug call to `getWith(txMolecules)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    * <br><br>
    * 3. Transactions of applied transaction molecules.
    *
    * @group debugGet
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Unit = {
    debugGet(conn.usingTempDb(With(seqAsJavaListConverter(txMolecules.flatten.flatten.map(_.toJava)).asJava)))
    txMolecules.zipWithIndex foreach { case (stmts, i) =>
      Debug(s"Transaction molecule ${i + 1} statements:", 1)(i + 1, stmts)
    }
  }


  /** Debug call to `getWith(txData)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    * <br><br>
    * 3. Transactions of applied transaction data.
    *
    * @group debugGet
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetWith(txData: jList[jList[_]])(implicit conn: Conn): Unit = {
    debugGet(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))
    println("Transaction data:\n========================================================================")
    txData.toArray foreach println
  }


  /** Debug call to `getHistory` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetHistory(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(History))


  /** Debug call to `save` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group debugOp
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Unit
    */
  def debugSave(implicit conn: Conn) {
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.debugSave", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.debugSave", 1)(1, _model, transformer.stmtsModel, stmts)
  }


  protected def _debugInsert(conn: Conn, dataRows: Iterable[Seq[Any]]) {
    val transformer = Model2Transaction(conn, _model)
    val data = untupled(dataRows)
    val stmtss = try {
      transformer.insertStmts(data)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._debugInsert", 1)(1, _model, transformer.stmtsModel, dataRows, data)
        throw e
    }
    Debug("output.Molecule._debugInsert", 1)(1, _model, transformer.stmtsModel, dataRows, data, stmtss)
  }


  /** Debug call to `update` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group debugOp
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return
    */
  def debugUpdate(implicit conn: Conn) {
    VerifyModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.debugUpdate", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.debugUpdate", 1)(1, _model, transformer.stmtsModel, stmts)
  }
}
