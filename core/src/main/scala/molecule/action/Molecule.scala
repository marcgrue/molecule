package molecule.action

import java.util.Date
import molecule.ast.model.Model
import molecule.ast.query.Query
import molecule.ast.tempDb._
import molecule.ast.transaction.Statement
import molecule.facade.Conn
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.collection.JavaConverters._

trait Molecule extends MoleculeBase {

  protected type lObj   = java.util.List[Object]
  protected type SS     = Seq[Seq[Statement]]
  protected type LL     = java.util.List[java.util.List[_]]


  // Manipulation .............................................................

  // Insert data is applied in each arity molecule (see below)
  protected trait checkInsertModel {
    VerifyModel(_model, "insert")
  }

  private[molecule] def _insert(conn: Conn, model: Model, dataRows: Iterable[Seq[Any]] = Seq()): TxReport = {
    val stmtss = Model2Transaction(conn, model).insertStmts(dataRows.toSeq)
    conn.transact(stmtss)
  }

  def save(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "save")
    val stmtss = Seq(Model2Transaction(conn, _model).saveStmts())
    conn.transact(stmtss)
  }

  def update(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "update")
    val stmtss = Seq(Model2Transaction(conn, _model).updateStmts())
    conn.transact(stmtss)
  }


  // Debug ....................................................................

  // Append "D" to commands to debug (prints to console)
  def getD                        (implicit conn: Conn): Unit = ???
  def getAsOfD (t: Long)          (implicit conn: Conn): Unit = getD(conn.usingTempDb(AsOf(TxLong(t))))
  def getAsOfD (txR: TxReport)    (implicit conn: Conn): Unit = getD(conn.usingTempDb(AsOf(TxLong(txR.t))))
  def getAsOfD (d: Date)          (implicit conn: Conn): Unit = getD(conn.usingTempDb(AsOf(TxDate(d))))
  def getSinceD(t: Long)          (implicit conn: Conn): Unit = getD(conn.usingTempDb(Since(TxLong(t))))
  def getSinceD(txR: TxReport)    (implicit conn: Conn): Unit = getD(conn.usingTempDb(Since(TxLong(txR.t))))
  def getSinceD(d: Date)          (implicit conn: Conn): Unit = getD(conn.usingTempDb(Since(TxDate(d))))
  def getWithD (txMolecules: SS*) (implicit conn: Conn): Unit = getD(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))
  def getWithD (txData: lObj)     (implicit conn: Conn): Unit = getD(conn.usingTempDb(With(txData.asInstanceOf[LL])))
  def getHistoryD                 (implicit conn: Conn): Unit = getD(conn.usingTempDb(History))

  protected def _insertD(conn: Conn, data: Iterable[Seq[Any]]) {
    VerifyModel(_model, "insert")
    val transformer = Model2Transaction(conn, _model)
    val stmtss = try {
      transformer.insertStmts(data.toSeq)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data, stmtss)
  }

  def saveD(implicit conn: Conn) {
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.saveD", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.saveD", 1)(1, _model, transformer.stmtsModel, stmts)
  }

  def updateD(implicit conn: Conn) {
    VerifyModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.updateD", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.updateD", 1)(1, _model, transformer.stmtsModel, stmts)
  }


  // Transaction data ....................................................................

  protected def _insertTx(conn: Conn, data: Iterable[Seq[Any]]): Seq[Seq[Statement]] = {
    VerifyModel(_model, "insert")
    val transformer = Model2Transaction(conn, _model)
    val stmtss: Seq[Seq[Statement]] = try {
      transformer.insertStmts(data.toSeq)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    stmtss
  }

  def saveTx(implicit conn: Conn): Seq[Seq[Statement]] = {
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.saveTx", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }

  def updateTx(implicit conn: Conn): Seq[Seq[Statement]] = {
    VerifyModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.updateD", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }

  override def toString: String = _query.toList
}
