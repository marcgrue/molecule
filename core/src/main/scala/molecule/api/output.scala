package molecule.api

import java.util.Date

import molecule.facade.Conn
import molecule.ast.tempDb._
import molecule.ast.model._
import molecule.ast.query.Query
import molecule.ast.transaction._
import molecule.facade.TxReport
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug

import scala.collection.JavaConverters._


trait Molecule extends MoleculeBase {
  val _model: Model
  val _query: Query

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
  def getD                        (implicit conn: Conn): Unit // implemented by factory.MakeBase.Debugger
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


trait MoleculeOutBase extends MoleculeBase with Molecule

trait MoleculeOut[T] extends MoleculeOutBase with Molecule {
  def get                        (implicit conn: Conn): Iterable[T]
  def get     (n: Int)           (implicit conn: Conn): Iterable[T]
  def getAsOf (t: Long)          (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(AsOf(TxLong(t))))
  def getAsOf (txR: TxReport)    (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(AsOf(TxLong(txR.t))))
  def getAsOf (d: Date)          (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(AsOf(TxDate(d))))
  def getSince(t: Long)          (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(Since(TxLong(t))))
  def getSince(txR: TxReport)    (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(Since(TxLong(txR.t))))
  def getSince(d: Date)          (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(Since(TxDate(d))))
  def getWith (txMolecules: SS*) (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))
  def getWith (txData: lObj)     (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(With(txData.asInstanceOf[LL])))
  def getHistory                 (implicit conn: Conn): Iterable[T] = get(conn.usingTempDb(History))

  def getRaw                        (implicit conn: Conn): Iterable[lObj]
  def getRaw     (n: Int)           (implicit conn: Conn): Iterable[lObj]
  def getRawAsOf (t: Long)          (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(AsOf(TxLong(t))))
  def getRawAsOf (d: Date)          (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(AsOf(TxDate(d))))
  def getRawSince(t: Long)          (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(Since(TxLong(t))))
  def getRawSince(d: Date)          (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(Since(TxDate(d))))
  def getRawWith (txMolecules: SS*) (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))
  def getRawWith (txs: lObj)        (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(With(txs.asInstanceOf[LL])))
  def getRawHistory                 (implicit conn: Conn): Iterable[lObj] = getRaw(conn.usingTempDb(History))

  def getJson        (implicit conn: Conn): String
  def getJson(n: Int)(implicit conn: Conn): String
}

abstract class Molecule0(val _model: Model, val _query: Query) extends MoleculeOutBase

abstract class Molecule1[A](val _model: Model, val _query: Query) extends MoleculeOut[A] {
  object insert extends checkInsertModel {
    def apply(a: A, ax: A*)     (implicit conn: Conn): TxReport = _insert(conn, _model, (a +: ax.toList).map(Seq(_)))
    def apply(data: Iterable[A])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(Seq(_)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, ax: A*)     (implicit conn: Conn): Unit = _insertD(conn, (a +: ax.toList).map(Seq(_)))
    def apply(data: Iterable[A])(implicit conn: Conn): Unit = _insertD(conn, data.map(Seq(_)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, ax: A*)     (implicit conn: Conn): SS = _insertTx(conn, (a +: ax.toList).map(Seq(_)))
    def apply(data: Iterable[A])(implicit conn: Conn): SS = _insertTx(conn, data.map(Seq(_)))
  }
}

abstract class Molecule2[A, B](val _model: Model, val _query: Query) extends MoleculeOut[(A, B)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B)            (implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b)))
    def apply(data: Iterable[(A, B)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B)            (implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b)))
    def apply(data: Iterable[(A, B)])(implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B)            (implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b)))
    def apply(data: Iterable[(A, B)])(implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2)))
  }
}

abstract class Molecule3[A, B, C](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C)         (implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c)))
    def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C)         (implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c)))
    def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C)         (implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c)))
    def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3)))
  }
}

abstract class Molecule4[A, B, C, D](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D)      (implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d)))
    def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D)      (implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d)))
    def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D)      (implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d)))
    def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
}

abstract class Molecule5[A, B, C, D, E](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)   (implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e)))
    def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)   (implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e)))
    def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)   (implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e)))
    def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
}

abstract class Molecule6[A, B, C, D, E, F](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
}

abstract class Molecule7[A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Iterable[(A, B, C, D, E, F, G)])   (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Iterable[(A, B, C, D, E, F, G)])   (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Iterable[(A, B, C, D, E, F, G)])   (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
}

abstract class Molecule8[A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H)])      (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H)])      (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H)])      (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
}

abstract class Molecule9[A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])         (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])         (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])         (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
}

abstract class Molecule10[A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])            (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])            (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])            (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
}

abstract class Molecule11[A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])               (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])               (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])               (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
}

abstract class Molecule12[A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])                  (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])                  (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])                  (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
}

abstract class Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                     (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                     (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                     (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
}

abstract class Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                        (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                        (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                        (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
}

abstract class Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                           (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                           (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                           (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
}

abstract class Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                              (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                              (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                              (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
}

abstract class Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                 (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                 (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                 (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
}

abstract class Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                    (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                    (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                    (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
}

abstract class Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                       (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                       (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                       (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
}

abstract class Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                          (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                          (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                          (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
}

abstract class Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                             (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                             (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                             (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
}

abstract class Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                (implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Unit = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                (implicit conn: Conn): Unit = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): SS = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                (implicit conn: Conn): SS = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
}
