//package molecule
//import java.util.UUID._
//import java.util.{Collection => jColl, List => jList, Map => jMap}
//
//import datomic._
//import molecule.api.{Molecule0, MoleculeOut, MoleculeOutBase}
//import molecule.ast.db._
//import molecule.ast.model.{Atom, Bond, Model, TxMetaData}
//import molecule.ast.query._
//import molecule.exceptions._
//import molecule.facade.{Conn, TxReport}
//import molecule.ops.QueryOps._
//import molecule.schema.Transaction
//import molecule.transform._
//import molecule.util._
//import org.specs2.main.ArgProperties
//
//import scala.collection.JavaConverters._
//
//// ArgProperties for some reason makes FactoryBase happy when creating nested molecules
//// outside the Specs2 framework (in non-test code). Todo: Do without ArgProperties
//
//
//trait DatomicFacade extends Helpers with ArgProperties {
////  private val x = Debug("DatomicFacade", 1, 99, false, 3)
//
//
//  // Database operations ..................................................................
//
////  def recreateDbFrom(tx: Transaction, identifier: String = "", protocol: String = "mem"): Connection = {
////    val id = if (identifier == "") randomUUID() else identifier
////    val uri = s"datomic:$protocol://$id"
////    try {
////      Peer.deleteDatabase(uri)
////      Peer.createDatabase(uri)
////      val conn = Peer.connect(uri)
////      conn.transact(tx.partitions)
////      conn.transact(tx.namespaces)
////      conn
////    } catch {
////      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
////    }
////  }
////
//////  def recreateDbFrom2(tx: Transaction, identifier: String = "", protocol: String = "mem"): Conn = {
//////    val id = if (identifier == "") randomUUID() else identifier
//////    val uri = s"datomic:$protocol://$id"
//////    try {
//////      Peer.deleteDatabase(uri)
//////      Peer.createDatabase(uri)
//////      val connection = Peer.connect(uri)
//////      connection.transact(tx.partitions)
//////      connection.transact(tx.namespaces)
//////      Conn(connection)
//////    } catch {
//////      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
//////    }
//////  }
////
////  def loadList(txlist: jList[_], identifier: String = "", protocol: String = "mem"): Connection = {
////    val id = if (identifier == "") randomUUID() else identifier
////    val uri = s"datomic:$protocol://$id"
////    try {
////      Peer.deleteDatabase(uri)
////      Peer.createDatabase(uri)
////      val conn = Peer.connect(uri)
////      conn.transact(txlist).get()
////      conn
////    } catch {
////      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
////    }
////  }
////
////
////  // Query ...............................................................................
////
////  private[molecule] var db: TempDb = null
////
////  def results(conn: Conn, model: Model, query: Query): Iterable[jList[AnyRef]] = {
////    val p = (expr: QueryExpr) => Query2String(query).p(expr)
////    val rules = "[" + (query.i.rules map p mkString " ") + "]"
////    val dbUsed = db match {
////      case Saved(db_)       => db_
////      case AsOf(TxLong(t))  => conn.db.asOf(t)
////      case AsOf(TxDate(d))  => conn.db.asOf(d)
////      case Since(TxLong(t)) => conn.db.since(t)
////      case Since(TxDate(d)) => conn.db.since(d)
////      case ast.db.With(tx)  => conn.db.`with`(tx).get(Connection.DB_AFTER).asInstanceOf[AnyRef]
////      case History          => conn.db.history()
////      case _                => conn.db
////    }
////
////    // reset db settings
////    db = null
////
////    val first = if (query.i.rules.isEmpty) Seq(dbUsed) else Seq(dbUsed, rules)
////    val allInputs: Seq[AnyRef] = first ++ query.inputs
////
////    try {
////      Peer.q(query.toMap, allInputs: _*).asScala
////    } catch {
////      case e: Throwable => throw QueryException(e, model, query, allInputs, p)
////    }
////  }
////
////
////  // Manipulation ..........................................................................
////
////  protected[molecule] def save(conn: Conn, model: Model): TxReportOLD = {
////    val transformer = Model2Transaction(conn, model)
////    val stmts = transformer.saveStmts()
////    //        x(6, model, transformer.stmtsModel, stmts)
////    TxReportOLD(conn, Seq(stmts)).transact
////  }
////
////  protected[molecule] def update(conn: Conn, model: Model, db: Database = null): TxReportOLD = {
////    val transformer = Model2Transaction(conn, model)
////    val stmts = transformer.updateStmts()
////    //            x(7, model, transformer.stmtsModel, stmts)
////    TxReportOLD(conn, Seq(stmts), db).transact
////  }
////
////  private[molecule] def _insert(conn: Conn, model: Model, dataRows: Seq[Seq[Any]] = Seq()): TxReportOLD = {
////    val transformer = Model2Transaction(conn, model)
////    val stmtss = transformer.insertStmts(dataRows)
////    //        x(2, model, transformer.stmtsModel, dataRows, stmtss)
////    TxReportOLD(conn, stmtss).transact
////  }
////
////
////  // Composite inserts .......................................................................
////
////  private def mkStmtss(conn: Conn, model: Model, data: Seq[Any]) = {
////    val transformer = Model2Transaction(conn, model)
////    val seqData = data map tupleToSeq
////    val stmtss = transformer.insertStmts(seqData)
////    //    x(3, mo._model, transformer.stmtsModel, data, seqData, stmtss)
////    stmtss
////  }
////
////  private def insertMerged(molecules: MoleculeOutBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[Molecule0])(implicit conn: Conn): TxReportOLD = {
////    val dataModel = Model(molecules.flatMap(_._model.elements))
////
////    // Prevent attribute conflicts
////    dataModel.elements.foldLeft(Seq.empty[String]) {
////      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
////        throw new RuntimeException(s"[DatomicFacade:insertMerged] Can't insert same attribute :$ns/$attr twice")
////      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
////      case (attrs, _)                                                                => Nil // Start over within next namespace
////    }
////
////    // Prevent reference conflicts
////    dataModel.elements.foldLeft(Seq.empty[String]) {
////      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
////        throw new RuntimeException(s"[DatomicFacade:insertMerged] Can't insert with same reference :$ns/$refAttr twice")
////      case (refs, Bond(ns, refAttr, _, _, _))                                   => refs :+ s"$ns/$refAttr"
////      case (refs, _)                                                            => refs
////    }
////
////    val data = (tupleTuples map tupleToSeq).map(_ flatMap tupleToSeq)
////    val dataStmtss = mkStmtss(conn, dataModel, data)
////    val dataTransformer = Model2Transaction(conn, dataModel)
////
////    val txStmts = if (txMolecules.nonEmpty) {
////      val txElements = txMolecules.flatMap(_._model.elements)
////      val txModel = Model(Seq(TxMetaData(txElements)))
////      val txTransformer = Model2Transaction(conn, txModel)
////      txTransformer.saveStmts()
////    } else Nil
////
////    val stmtss = dataStmtss :+ txStmts
////    //    x(4, stmtss)
////    TxReportOLD(conn, stmtss).transact
////  }
////
////  def insert[T1, T2](m1: MoleculeOut[T1], m2: MoleculeOut[T2])
////    (data: Seq[(T1, T2)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3])
////    (data: Seq[(T1, T2, T3)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4])
////    (data: Seq[(T1, T2, T3, T4)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5])
////    (data: Seq[(T1, T2, T3, T4, T5)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6])
////    (data: Seq[(T1, T2, T3, T4, T5, T6)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21)(data)(txMolecules)(conn)
////
////  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21], m22: MoleculeOut[T22])
////    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReportOLD =
////    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22)(data)(txMolecules)(conn)
//}

//  datomic.Peer.q(
//    """
//      |[:find  ?b (distinct ?c)
//      | :where [?a :ns/int ?b]
//      |        [(fulltext $ :ns/strMap "da@Hej") [[ ?a ?c ]]]]
//    """.stripMargin, conn.db) === 987
