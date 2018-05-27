package molecule.api
import molecule.ast.model.{Atom, Bond, Model, TxMetaData}
import molecule.facade.{Conn, TxReport}
import molecule.transform.Model2Transaction
import molecule.util.{Debug, Helpers}

trait CompositeInserts extends Helpers {


  // Composite inserts .......................................................................

  private def mkStmtss(conn: Conn, model: Model, data: Seq[Any]) = {
    val transformer = Model2Transaction(conn, model)
    val seqData = data map tupleToSeq
    val stmtss = transformer.insertStmts(seqData)
    //    x(3, mo._model, transformer.stmtsModel, data, seqData, stmtss)
    stmtss
  }

  private def insertMerged(molecules: MoleculeOutBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[Molecule0])(implicit conn: Conn): TxReport = {
    val dataModel = Model(molecules.flatMap(_._model.elements))

    // Prevent attribute conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
        throw new RuntimeException(s"[CompositeInserts:insertMerged] Can't insert same attribute :$ns/$attr twice")
      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
      case (attrs, _)                                                                => Nil // Start over within next namespace
    }

    // Prevent reference conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
        throw new RuntimeException(s"[CompositeInserts:insertMerged] Can't insert with same reference :$ns/$refAttr twice")
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
//        x(4, stmtss)
    //    TxReport(conn, stmtss).transact

    conn.transact(stmtss)
  }

  private def insertMergedD(molecules: MoleculeOutBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[Molecule0])(implicit conn: Conn) {
    val dataModel = Model(molecules.flatMap(_._model.elements))

    // Prevent attribute conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
        throw new RuntimeException(s"[CompositeInserts:insertMerged] Can't insert same attribute :$ns/$attr twice")
      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
      case (attrs, _)                                                                => Nil // Start over within next namespace
    }

    // Prevent reference conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
        throw new RuntimeException(s"[CompositeInserts:insertMerged] Can't insert with same reference :$ns/$refAttr twice")
      case (refs, Bond(ns, refAttr, _, _, _))                                   => refs :+ s"$ns/$refAttr"
      case (refs, _)                                                            => refs
    }

    val data = (tupleTuples map tupleToSeq).map(_ flatMap tupleToSeq)
    val dataStmtss = mkStmtss(conn, dataModel, data)
    val dataTransformer = Model2Transaction(conn, dataModel)

    Debug("output.Molecule.insertMergedD-dataStmtss", 1)(1, dataModel, dataTransformer.stmtsModel, dataStmtss)

    if (txMolecules.nonEmpty) {
      val txElements = txMolecules.flatMap(_._model.elements)
      val txModel = Model(Seq(TxMetaData(txElements)))
      val txTransformer = Model2Transaction(conn, txModel)
      val txStmtss = txTransformer.saveStmts()
      Debug("output.Molecule.insertMergedD-txStmtss", 1)(1, txModel, txTransformer.stmtsModel, txStmtss)
    }
  }

  def insert[T1, T2](m1: MoleculeOut[T1], m2: MoleculeOut[T2])
    (data: Seq[(T1, T2)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2)(data)(txMolecules)(conn)

  def insertD[T1, T2](m1: MoleculeOut[T1], m2: MoleculeOut[T2])
    (data: Seq[(T1, T2)])(txMolecules: Molecule0*)(implicit conn: Conn): Unit =
    insertMergedD(m1, m2)(data)(txMolecules)(conn)

  def insert[T1, T2, T3](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3])
    (data: Seq[(T1, T2, T3)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3)(data)(txMolecules)(conn)

  def insertD[T1, T2, T3](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3])
    (data: Seq[(T1, T2, T3)])(txMolecules: Molecule0*)(implicit conn: Conn): Unit =
    insertMergedD(m1, m2, m3)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4])
    (data: Seq[(T1, T2, T3, T4)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5])
    (data: Seq[(T1, T2, T3, T4, T5)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6])
    (data: Seq[(T1, T2, T3, T4, T5, T6)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21)(data)(txMolecules)(conn)

  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](m1: MoleculeOut[T1], m2: MoleculeOut[T2], m3: MoleculeOut[T3], m4: MoleculeOut[T4], m5: MoleculeOut[T5], m6: MoleculeOut[T6], m7: MoleculeOut[T7], m8: MoleculeOut[T8], m9: MoleculeOut[T9], m10: MoleculeOut[T10], m11: MoleculeOut[T11], m12: MoleculeOut[T12], m13: MoleculeOut[T13], m14: MoleculeOut[T14], m15: MoleculeOut[T15], m16: MoleculeOut[T16], m17: MoleculeOut[T17], m18: MoleculeOut[T18], m19: MoleculeOut[T19], m20: MoleculeOut[T20], m21: MoleculeOut[T21], m22: MoleculeOut[T22])
    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)])(txMolecules: Molecule0*)(implicit conn: Conn): TxReport =
    insertMerged(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22)(data)(txMolecules)(conn)

}
