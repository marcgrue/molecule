package molecule
import java.util.UUID._
import java.util.{Date, List => jList, Map => jMap}

import datomic._
import datomic.db.Db
import molecule.api.{Molecule0, MoleculeOut, MoleculeOutBase}
import molecule.ast.model.{Atom, Bond, Model, TxMetaData}
import molecule.ast.query._
import molecule.ast.transaction.{Statement, _}
import molecule.dsl.Transaction
import molecule.ops.QueryOps._
import molecule.transform._
import molecule.util._
import org.specs2.main.ArgProperties

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

// ArgProperties for some reason makes FactoryBase happy when creating nested molecules
// outside the Specs2 framework (in non-test code). Todo: Do without ArgProperties


trait DatomicFacade extends Helpers with ArgProperties {
  private val x = Debug("DatomicFacade", 1, 99, false, 3)
  type KeepQueryOpsWhenFormatting = KeepQueryOps

  // Create database and load schema ========================================

  def recreateDbFrom(tx: Transaction, identifier: String = "", protocol: String = "mem"): Connection = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val conn = Peer.connect(uri)
      conn.transact(tx.partitions)
      conn.transact(tx.namespaces)
      conn
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }

  def loadList(txlist: java.util.List[_], identifier: String = "", protocol: String = "mem"): Connection = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val conn = Peer.connect(uri)
      conn.transact(txlist).get()
      conn
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }


  // Query ==================================================================

  sealed trait TxType
  case class txDate(txInstant: Date) extends TxType
  case class txLong(t: Long) extends TxType
  case class txlObj(tx: java.util.List[Object]) extends TxType

  sealed trait DbOp
  case class AsOf(tx: TxType) extends DbOp
  case class Since(date: Date) extends DbOp
  case class Imagine(tx: java.util.List[Object]) extends DbOp
  case object History extends DbOp

  private[molecule] var dbOp: DbOp = null

  private def cast(a: Any) = a match {
    case i: Int   => i.toLong.asInstanceOf[Object]
    case f: Float => f.toDouble.asInstanceOf[Object]
    case other    => other.asInstanceOf[Object]
  }

  def inputs(query: Query) = query.i.inputs.map {
    case InVar(RelationBinding(_), argss)   => Util.list(argss.map(args => Util.list(args map cast: _*)).asJava: _*)
    case InVar(CollectionBinding(_), argss) => Util.list(argss.head map cast: _*)
    case InVar(_, argss)                    => cast(argss.head.head)
    case InDataSource(_, argss)             => cast(argss.head.head)
    case other                              => sys.error(s"[DatomicFacade] UNEXPECTED inputs: $other")
  }

  def results(conn: Connection, model: Model, query: Query): List[jList[AnyRef]] = {
    val p = (expr: QueryExpr) => Query2String(query).p(expr)
    val rules = "[" + (query.i.rules map p mkString " ") + "]"
    val db = dbOp match {
      case AsOf(txDate(txInstant)) => conn.db.asOf(txInstant)
      case AsOf(txLong(t))         => conn.db.asOf(t)
      case AsOf(txlObj(tx))        => conn.db.asOf(tx)
      case Since(date)             => conn.db.since(date)
      case Imagine(tx)             => conn.db.`with`(tx).get(Connection.DB_AFTER).asInstanceOf[AnyRef]
      case History                 => conn.db.history()
      case _                       => conn.db
    }

    // reset db settings
    dbOp = null

    val first = if (query.i.rules.isEmpty) Seq(db) else Seq(db, rules)
    val allInputs = first ++ inputs(query)

    try {
      Peer.q(query.toMap, allInputs: _*).toList
    } catch {
      case e: Throwable => throw new RuntimeException(
        s"""
           |#############################################################################
           |$e
           |
           |$model
           |
           |$query
           |
           |${query.datalog}
           |
           |RULES: ${if (query.i.rules.isEmpty) "none" else query.i.rules map p mkString("[\n ", "\n ", "\n]")}
           |
           |INPUTS: ${allInputs.zipWithIndex.map(e => "\n" + (e._2 + 1) + " " + e._1)}
           |#############################################################################
         """.stripMargin)
    }
  }


  // Manipulation ..........................................................................

  private[molecule] def insert_(conn: Connection, model: Model, dataRows: Seq[Seq[Any]] = Seq()): Tx = {
    val transformer = Model2Transaction(conn, model)
    val stmtss = transformer.insertStmts(dataRows)
    //        x(2, model, transformer.stmtsModel, dataRows, stmtss)
    //        x(2, model, transformer.stmtsModel, stmtss)
//            x(2, transformer.stmtsModel, stmtss)
    Tx(conn, stmtss)
  }


  // Composite inserts .................................

  private def mkStmtss(conn: Connection, model: Model, data: Seq[Any]) = {
    val transformer = Model2Transaction(conn, model)
    val seqData = data map tupleToSeq
    val stmtss = transformer.insertStmts(seqData)
    //    x(3, mo._model, transformer.stmtsModel, data, seqData, stmtss)
    stmtss
  }

  private def insertMerged(molecules: MoleculeOutBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[Molecule0])(implicit conn: Connection): Tx = {
    val dataModel = Model(molecules.flatMap(_._model.elements))

    // Prevent attribute conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
        throw new RuntimeException(s"[DatomicFacade:insertMerged] Can't insert same attribute :$ns/$attr twice")
      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
      case (attrs, _)                                                                => Nil // Start over within next namespace
    }

    // Prevent reference conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
        throw new RuntimeException(s"[DatomicFacade:insertMerged] Can't insert with same reference :$ns/$refAttr twice")
      case (refs, Bond(ns, refAttr, _, _, _))                                   => refs :+ s"$ns/$refAttr"
      case (refs, _)                                                            => refs
    }

    val data = (tupleTuples map tupleToSeq).map(_ flatMap tupleToSeq)
    val dataStmtss = mkStmtss(conn, dataModel, data)
    val dataTransformer = Model2Transaction(conn, dataModel)

    val txStmts = if (txMolecules.nonEmpty) {
      val txElements = txMolecules.flatMap(_._model.elements)
      val txModel = Model(Seq(TxMetaData(txElements)))
      val txTransformer = Model2Transaction(conn, txModel)
      txTransformer.saveStmts()
    } else Nil

    val stmtss = dataStmtss :+ txStmts

    //    x(4, stmtss)
    //    x(4, molecules(0)._model, molecules(1)._model, dataModel)
    Tx(conn, stmtss)
  }

  def insert[T1, T2](m1: MoleculeOut[T1], m2: MoleculeOut[T2])
    (data: Seq[(T1, T2)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2)(data)(txMolecules)(conn)

  def insert[T1, T2, T3](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3])
    (data: Seq[(T1, T2, T3)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4])
    (data: Seq[(T1, T2, T3, T4)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5])
    (data: Seq[(T1, T2, T3, T4, T5)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6])
    (data: Seq[(T1, T2, T3, T4, T5, T6)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21], m22: MoleculeOut[T22])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)])(txMolecules: Molecule0*)(implicit conn: Connection): Tx =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22)(data)(txMolecules)(conn)


  // Other manipulations

  protected[molecule] def save(conn: Connection, model: Model): Tx = {
    val transformer = Model2Transaction(conn, model)
    val stmts = transformer.saveStmts()
    //        x(6, model, transformer.stmtsModel, stmts)
    Tx(conn, Seq(stmts))
  }

  protected[molecule] def update(conn: Connection, model: Model): Tx = {
    val transformer = Model2Transaction(conn, model)
    val stmts = transformer.updateStmts()
    //            x(7, model, transformer.stmtsModel, stmts)
    //            x(7, transformer.stmtsModel, stmts)
    Tx(conn, Seq(stmts))
  }
}

object DatomicFacade extends DatomicFacade


case class Tx(conn: Connection, stmtss: Seq[Seq[Statement]]) {
  private val x = Debug("Tx", 1, 99, false, 3)

  val flatStmts = stmtss.flatten.map {
    case Add(e, a, i: Int, meta)   => Add(e, a, i.toLong: java.lang.Long, meta).toJava
    case Add(e, a, f: Float, meta) => Add(e, a, f.toDouble: java.lang.Double, meta).toJava
    //    case Add(e, a, d: Date)  => Add(e, a, d).toJava
    //    case Add(e, a, v: UUID)  => Add(e, a, v).toJava
    //    case Add(e, a, v: URI)   => Add(e, a, v).toJava
    case other => other.toJava
  }.asJava
  //      x(7, stmtss, flatStmts)

  val txResult: jMap[_, _] = conn.transact(flatStmts).get

  def eids: List[Long] = {
    val txData = txResult.get(Connection.TX_DATA)

    // Omit first transaction datom
    val datoms = txData.asInstanceOf[java.util.Collection[Datom]].toList.tail

    val tempIds = stmtss.flatten.collect {
      case Add(e, _, _, meta) if e.toString.take(6) == "#db/id" => e
      case Add(_, _, v, meta) if v.toString.take(6) == "#db/id" => v
    }.distinct

    val txTtempIds = txResult.get(Connection.TEMPIDS)
    val dbAfter = txResult.get(Connection.DB_AFTER).asInstanceOf[Db]
    val ids: Seq[Long] = tempIds.map(tempId => datomic.Peer.resolveTempid(dbAfter, txTtempIds, tempId).asInstanceOf[Long]).distinct
//        x(1, transformer.stmtsModel, stmtss, datoms, ids)
//        x(1, datoms, tempIds, ids)
    ids.toList
  }

  def eidSet: Set[Long] = eids.toSet
  def eid: Long = eids.head
  def db: Db = txResult.get(Connection.DB_AFTER).asInstanceOf[Db]
  def t: Long = db.basisT()
  def tx: datomic.Entity = db.entity(Peer.toTx(t))
  def inst: Date = tx.get(":db/txInstant").asInstanceOf[Date]
}

//import scala.language.existentials
//  private def cast(a: Any) = a match {
//    case i: Int   => i.toLong.asInstanceOf[Object]
//    case f: Float => f.toDouble.asInstanceOf[Object]
//    case other    => other.asInstanceOf[Object]
//  }
//  val argssRaw     = Seq(Seq("(.*)", "(?i)(H)"))
//  val argssJava = Seq(datomic.Util.list(argssRaw.map(args => datomic.Util.list(args map cast: _*)).asJava: _*))
//  val argss1    = conn.db +: argssJava
//
//  datomic.Peer.q(
//    """
//      |[:find  ?b (distinct ?c)
//      | :where [?a :ns/int ?b]
//      |        [(fulltext $ :ns/strMap "da@Hej") [[ ?a ?c ]]]]
//    """.stripMargin, conn.db) === 987
//
//  datomic.Peer.q(
//    """
//      |[:find  ?b (distinct ?c)
//      | :in    $ [[ ?cKey ?cValue ]]
//      | :where [?a :ns/int ?b]
//      |        [?a :ns/strMap ?c]
//      |        [(str "(?i)(" ?cKey ")@.*") ?c1]
//      |        [(.matches ^String ?c ?c1)]
//      |        [(.split ^String ?c "@" 2) ?c2]
//      |        [(second ?c2) ?c3]
//      |        [(.compareTo ^String ?c3 ?cValue) ?c3_1]
//      |        [(> ?c3_1 0)]]
//    """.stripMargin, argss1: _*) === 77