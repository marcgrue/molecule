package molecule
package api

import java.util.Date

import datomic.Connection
import molecule.ast.model._
import molecule.ast.query.Query
import molecule.dsl.actions.ValueAttr
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.collection.JavaConverters._


trait Molecule extends DatomicFacade {
  val _model: Model
  val _query: Query

  protected type lObj = java.util.List[Object]
  protected type SL = Seq[java.util.List[_]]
  protected type LL = java.util.List[java.util.List[_]]
  protected type W[Out] = ValueAttr[_, _, _, Out]
  protected type Op[T] = Option[T]


  // Time .....................................................................

  protected def asOf_[M <: Molecule](thisMolecule: M, d: Date) = {dbOp = AsOf(txDate(d)); thisMolecule}
  protected def asOf_[M <: Molecule](thisMolecule: M, t: Long) = {dbOp = AsOf(txLong(t)); thisMolecule}
  protected def asOf_[M <: Molecule](thisMolecule: M, tx: LL) = {dbOp = AsOf(txlObj(tx)); thisMolecule}
  protected def since_[M <: Molecule](thisMolecule: M, d: Date) = {dbOp = Since(txDate(d)); thisMolecule}
  protected def since_[M <: Molecule](thisMolecule: M, t: Long) = {dbOp = Since(txLong(t)); thisMolecule}
  protected def since_[M <: Molecule](thisMolecule: M, tx: LL) = {dbOp = Since(txlObj(tx)); thisMolecule}
  protected def history_[M <: Molecule](thisMolecule: M) = {dbOp = History; thisMolecule}
  protected def imagine_[M <: Molecule](thisMolecule: M, tx: LL) = {dbOp = Imagine(tx); thisMolecule}


  // Manipulation .............................................................

  // Insert data is applied in each arity molecule (see below)
  protected trait checkInsertModel {
    CheckModel(_model, "insert")
  }

  def save(implicit conn: Connection): Tx = {
    CheckModel(_model, "save")
    save(conn, _model)
  }

  def update(implicit conn: Connection): Tx = {
    CheckModel(_model, "update")
    update(conn, _model)
  }


  // Debug ....................................................................

  // Append "D" to basic commands `get`, `save`, `insert` and `update` to debug (prints to console)

  def getD(implicit conn: Connection): Unit

  protected def _insertD(conn: Connection, data: Seq[Seq[Any]]) {
    CheckModel(_model, "insert")
    val transformer = Model2Transaction(conn, _model)
    val stmtss = try {
      transformer.insertStmts(data)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data, stmtss)
  }

  def saveD(implicit conn: Connection) {
    CheckModel(_model, "save")
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

  def updateD(implicit conn: Connection) {
    CheckModel(_model, "update")
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

  protected def _insertTx(conn: Connection, data: Seq[Seq[Any]]): Seq[java.util.List[_]] = {
    CheckModel(_model, "insert")
    val transformer = Model2Transaction(conn, _model)
    val stmtss = try {
      transformer.insertStmts(data)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._insertD", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    Tx(conn, stmtss).flatStmts
  }

  def saveTx(implicit conn: Connection): Seq[java.util.List[_]] = {
    CheckModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.saveTx", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Tx(conn, Seq(stmts)).flatStmts
  }

  def updateTx(implicit conn: Connection): Seq[java.util.List[_]] = {
    CheckModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.updateD", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Tx(conn, Seq(stmts)).flatStmts
  }


  override def toString: String = _query.toList
}


trait MoleculeOutBase {
  val _model: Model
  val _query: Query
}

trait MoleculeOut[T] extends MoleculeOutBase

abstract class Molecule0(val _model: Model, val _query: Query) extends Molecule

abstract class Molecule1[A](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[A] {
  def get        (implicit conn: Connection): Seq[A]
  def get(n: Int)(implicit conn: Connection): Seq[A]    = get(conn).take(n)
  def one        (implicit conn: Connection): A         = get(conn).head
  def some       (implicit conn: Connection): Option[A] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, ax: A*)(implicit conn: Connection) = _insertD(conn, (a +: ax.toList).map(Seq(_)))
    def apply(data: Seq[A])(implicit conn: Connection) = _insertD(conn, data.map(Seq(_)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, ax: A*)(implicit conn: Connection) = _insertTx(conn, (a +: ax.toList).map(Seq(_)))
    def apply(data: Seq[A])(implicit conn: Connection) = _insertTx(conn, data.map(Seq(_)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, ax: A*)(implicit conn: Connection): Tx = insert_(conn, _model, (a +: ax.toList).map(Seq(_)))
    def apply(data: Seq[A])(implicit conn: Connection): Tx = insert_(conn, _model, data.map(Seq(_)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule2[A, B](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B)] {
  def get        (implicit conn: Connection): Seq[(A, B)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B)       (implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b)))
    def apply(data: Seq[(A, B)])(implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B)       (implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b)))
    def apply(data: Seq[(A, B)])(implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B)       (implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b)))
    def apply(data: Seq[(A, B)])(implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule3[A, B, C](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C)] {
  def get        (implicit conn: Connection): Seq[(A, B, C)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C)    (implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c)))
    def apply(data: Seq[(A, B, C)])(implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C)    (implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c)))
    def apply(data: Seq[(A, B, C)])(implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C)    (implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c)))
    def apply(data: Seq[(A, B, C)])(implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule4[A, B, C, D](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D) (implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d)))
    def apply(data: Seq[(A, B, C, D)])(implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D) (implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d)))
    def apply(data: Seq[(A, B, C, D)])(implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D) (implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d)))
    def apply(data: Seq[(A, B, C, D)])(implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule5[A, B, C, D, E](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e)))
    def apply(data: Seq[(A, B, C, D, E)])  (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e)))
    def apply(data: Seq[(A, B, C, D, E)])  (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e)))
    def apply(data: Seq[(A, B, C, D, E)])  (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule6[A, B, C, D, E, F](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Seq[(A, B, C, D, E, F)])     (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Seq[(A, B, C, D, E, F)])     (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f)))
    def apply(data: Seq[(A, B, C, D, E, F)])     (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule7[A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Seq[(A, B, C, D, E, F, G)])        (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Seq[(A, B, C, D, E, F, G)])        (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g)))
    def apply(data: Seq[(A, B, C, D, E, F, G)])        (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule8[A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H)])           (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H)])           (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H)])           (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule9[A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I)])              (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I)])              (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I)])              (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule10[A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J)])                 (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J)])                 (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J)])                 (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule11[A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K)])                    (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K)])                    (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K)])                    (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule12[A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L)])                       (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L)])                       (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L)])                       (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                          (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                          (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M)])                          (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                             (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                             (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])                             (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                                (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                                (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])                                (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                                   (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                                   (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])                                   (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                      (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                      (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])                                      (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                         (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                         (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])                                         (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                            (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                            (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])                                            (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                               (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                               (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])                                               (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                                  (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                                  (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])                                                  (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
  }
  protected def getE(implicit conn: Connection): Seq[(Long, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)]
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}

abstract class Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends Molecule with MoleculeOut[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
  def get        (implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]
  def get(n: Int)(implicit conn: Connection): Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)]    = get(conn).take(n)
  def one        (implicit conn: Connection): (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)         = get(conn).head
  def some       (implicit conn: Connection): Option[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] = get(conn).headOption
  object insertD extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Connection) = _insertD(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                     (implicit conn: Connection) = _insertD(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
  object insertTx extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Connection) = _insertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                     (implicit conn: Connection) = _insertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
  object insert extends checkInsertModel {
    def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Connection): Tx = insert_(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))
    def apply(data: Seq[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])                                                     (implicit conn: Connection): Tx = insert_(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
  }
  def asOf(d: Date)      = asOf_   (this, d)
  def asOf(t: Long)      = asOf_   (this, t)
  def asOf(tx: LL)       = asOf_   (this, tx)
  def since(d: Date)     = since_  (this, d)
  def since(t: Long)     = since_  (this, t)
  def since(tx: LL)      = since_  (this, tx)
  def history            = history_(this)
  def imagine(txs: SL*)  = imagine_(this, txs.reduce(_ ++ _).asJava)
  def imagine(txs: lObj) = imagine_(this, txs.asInstanceOf[LL])
}
