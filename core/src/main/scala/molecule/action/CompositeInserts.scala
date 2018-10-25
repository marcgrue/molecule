package molecule.action
import molecule.action.exception.CompositeException
import molecule.ast.MoleculeBase
import molecule.ast.model.{Atom, Bond, Model, TxMetaData}
import molecule.exceptions.MoleculeException
import molecule.facade.{Conn, TxReport}
import molecule.transform.Model2Transaction
import molecule.util.{Debug, Helpers}


/** Insert methods to create composite entities containing multiple sub-molecules/tuples.
  *
  * == Insert composite data ==
  * {{{
  *   insert(
  *     // 2 sub-molecules
  *     Article.name.author, Tag.name.weight
  *   )(
  *     // Rows of tuples, each with 2 sub-tuples of data matching sub-molecules
  *     List(
  *       (("Battle of Waterloo", "Ben Bridge"), ("serious", 5)),
  *       (("Best jokes ever", "John Cleese"), ("fun", 3))
  *     )
  *   )(
  *     // Optional transaction meta data molecule saved with the tx entity created
  *     MetaData.submitter("Brenda Johnson").usecase("AddReviews")
  *   )
  * }}}
  *
  *
  * == Query inserted composite data ==
  * We can then query the composite data:
  * {{{
  *   // Important articles submitted by Brenda Johnson
  *   // In queries we tie composite sub-molecules together with `~`
  *   m(Article.name.author ~ Tag.weight.>=(4).Tx(MetaData.submitter_("Brenda Johnson"))).get === List(
  *     (("Battle of Waterloo", "Ben Bridge"), 5)
  *   )
  * }}}
  *
  * == Debug a composite insert ==
  *
  * Without affecting the database, a composite insert can be debugged with `debugInsert` to see the produced output:
  * {{{
  *   debugInsert(
  *     Article.name.author, Tag.name.weight
  *   )(
  *     List(
  *       (("Battle of Waterloo", "Ben Bridge"), ("serious", 5)),
  *       (("Best jokes ever", "John Cleese"), ("fun", 3))
  *     )
  *   )(
  *     MetaData.submitter("Brenda Johnson").usecase("AddReviews")
  *   )
  *
  *   outputs:
  *
  *   ## 1 ## Composite arity-2 insert debug
  *   ========================================================================
  *   1      Model(
  *     1      Atom(article,name,String,1,VarValue,None,List(),List())
  *     2      Atom(article,author,String,1,VarValue,None,List(),List())
  *     3      Atom(tag,name,String,1,VarValue,None,List(),List())
  *     4      Atom(tag,weight,Long,1,VarValue,None,List(),List())
  *     5      TxMetaData(
  *       1      Atom(metaData,submitter,String,1,Eq(List(Brenda Johnson)),None,List(),List())
  *       2      Atom(metaData,usecase,String,1,Eq(List(AddReviews)),None,List(),List())))
  *   ------------------------------------------------
  *   2      List(
  *     1      :db/add    'tempId    :article/name          'arg                                     Card(1)
  *     2      :db/add    'e         :article/author        'arg                                     Card(1)
  *     3      :db/add    'e         :tag/name              'arg                                     Card(1)
  *     4      :db/add    'e         :tag/weight            'arg                                     Card(1)
  *     5      :db/add    'tx        :metaData/submitter    Values(Eq(List(Brenda Johnson)),None)    Card(1)
  *     6      :db/add    'tx        :metaData/usecase      Values(Eq(List(AddReviews)),None)        Card(1))
  *   ------------------------------------------------
  *   3      List(
  *     1      List(
  *       1      :db/add    #db/id[:db.part/user -1000097]    :article/name          Battle of Waterloo    Card(1)
  *       2      :db/add    #db/id[:db.part/user -1000097]    :article/author        Ben Bridge            Card(1)
  *       3      :db/add    #db/id[:db.part/user -1000097]    :tag/name              serious               Card(1)
  *       4      :db/add    #db/id[:db.part/user -1000097]    :tag/weight            5                     Card(1))
  *     2      List(
  *       1      :db/add    #db/id[:db.part/user -1000098]    :article/name          Best jokes ever       Card(1)
  *       2      :db/add    #db/id[:db.part/user -1000098]    :article/author        John Cleese           Card(1)
  *       3      :db/add    #db/id[:db.part/user -1000098]    :tag/name              fun                   Card(1)
  *       4      :db/add    #db/id[:db.part/user -1000098]    :tag/weight            3                     Card(1))
  *     3      List(
  *       1      :db/add    #db/id[:db.part/tx -1000100]      :metaData/submitter    Brenda Johnson        Card(1)
  *       2      :db/add    #db/id[:db.part/tx -1000100]      :metaData/usecase      AddReviews            Card(1)))
  *   ========================================================================
  * }}}
  *
  * The two first sections are internal Molecule representations.
  * <br><br>
  * The third section shows the transactional data sent to Datomic (except the line indexing numbers in the
  * left column) - in this case two rows of data and one row of transaction data.
  *
  * @see Manual: [[http://www.scalamolecule.org/manual/crud/composite-insert/ composite inserts]]
  *      | [[http://www.scalamolecule.org/manual/relationships/composites/ composites]]
  * @see Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/ref/Composite.scala#L1 composite inserts]]
  * @groupname insert Composite inserts (arity 2-22)
  * @groupprio insert 40
  * @groupdesc insert Insert composite data with optional transaction data. See [[molecule.action.CompositeInserts Composite insert]]
  * @groupname debugInsert Composite inserts debugging (arity 2-22)
  * @groupprio debugInsert 41
  * @groupdesc debugInsert Debug any composite insert by adding a 'D' to the insert method name in your code.
  *            <br>Corresponding internal Molecule representations and Datomic transactions are then printed to output.
  *            <br>See `debugInsert[T1, T2]` for full signature info example.
  */
trait CompositeInserts extends Helpers {

  private def mkStmtss(conn: Conn, model: Model, data: Seq[Any]) = {
    val transformer = Model2Transaction(conn, model)
    val seqData = data map tupleToSeq
    val stmtss = transformer.insertStmts(seqData)
    stmtss
  }

  private def _insert(molecules: MoleculeBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[MoleculeBase])(implicit conn: Conn): TxReport = {
    val dataModel = Model(molecules.flatMap(_._model.elements))

    // Prevent attribute conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
        throw new CompositeException(s"Can't insert same attribute :$ns/$attr twice")
      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
      case (attrs, _)                                                                => Nil // Start over within next namespace
    }

    // Prevent reference conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
        throw new CompositeException(s"Can't insert with same reference :$ns/$refAttr twice")
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

    conn.transact(stmtss)
  }

  private def _debugInsert(molecules: MoleculeBase*)(tupleTuples: Seq[Any])(txMolecules: Seq[MoleculeBase])(implicit conn: Conn) {
    val dataModel = Model(molecules.flatMap(_._model.elements))

    // Prevent attribute conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (attrs, Atom(ns, attr, _, _, _, _, _, _)) if attrs.contains(s"$ns/$attr") =>
        throw new CompositeException(s"Can't insert same attribute :$ns/$attr twice")
      case (attrs, Atom(ns, attr, _, _, _, _, _, _))                                 => attrs :+ s"$ns/$attr"
      case (attrs, _)                                                                => Nil // Start over within next namespace
    }

    // Prevent reference conflicts
    dataModel.elements.foldLeft(Seq.empty[String]) {
      case (refs, Bond(ns, refAttr, _, _, _)) if refs.contains(s"$ns/$refAttr") =>
        throw new CompositeException(s"Can't insert with same reference :$ns/$refAttr twice")
      case (refs, Bond(ns, refAttr, _, _, _))                                   => refs :+ s"$ns/$refAttr"
      case (refs, _)                                                            => refs
    }

    val data = (tupleTuples map tupleToSeq).map(_ flatMap tupleToSeq)
    val dataStmtss = mkStmtss(conn, dataModel, data)
    val dataTransformer = Model2Transaction(conn, dataModel)

    val arity = molecules.size

    if (txMolecules.nonEmpty) {
      val txElements = txMolecules.flatMap(_._model.elements)
      val txModel = Model(Seq(TxMetaData(txElements)))
      val txTransformer = Model2Transaction(conn, txModel)
      val txStmts = txTransformer.saveStmts()
      Debug(s"Composite arity-$arity insert debug", 1)(1, Model(dataModel.elements ++ txModel.elements), dataTransformer.stmtsModel ++ txTransformer.stmtsModel, dataStmtss :+ txStmts)
    } else {
      Debug(s"Composite arity-$arity insert debug", 1)(1, dataModel, dataTransformer.stmtsModel, dataStmtss)
    }
  }

  /** Composite insert with 2 sub-molecules/tuples:
    * {{{
    *   insert(
    *     Article.name.author, Tag.name.weight
    *   )(
    *     List(
    *       (("Battle of Waterloo", "Ben Bridge"), ("serious", 5)),
    *       (("Best jokes ever", "John Cleese"), ("fun", 3))
    *     )
    *   )(
    *     MetaData.submitter("Brenda Johnson").usecase("AddReviews")
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/crud/composite-insert/ Manual: Composite inserts]]
    * @param m1          First molecule of type [[molecule.action.Molecule Molecule[T1]]]
    * @param m2          Second molecule of type [[molecule.action.Molecule Molecule[T2]]]
    * @param data        List of tuples (rows) of sub-tuples, type: `Seq[(T1, T2)]`
    * @param txMolecules Optional transaction molecule(s) with tx meta data applied to tx attributes
    * @param conn        Implicit [[molecule.facade.Conn Conn]] in scope
    * @tparam T1 Type of first data tuple (can be single type too)
    * @tparam T2 Type of second data tuple (can be single type too)
    * @return [[molecule.facade.TxReport TxReport]] with result of insert
    * @group insert
    * */
  def insert[T1, T2](m1: Molecule[T1], m2: Molecule[T2])
                    (data: Seq[(T1, T2)])
                    (txMolecules: MoleculeBase*)
                    (implicit conn: Conn): TxReport = _insert(m1, m2)(data)(txMolecules)(conn)

  /* Composite insert with 3 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3])
                        (data: Seq[(T1, T2, T3)])
                        (txMolecules: MoleculeBase*)
                        (implicit conn: Conn): TxReport = _insert(m1, m2, m3)(data)(txMolecules)(conn)

  /* Composite insert with 4 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4])
                            (data: Seq[(T1, T2, T3, T4)])
                            (txMolecules: MoleculeBase*)
                            (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4)(data)(txMolecules)(conn)

  /* Composite insert with 5 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5])
                                (data: Seq[(T1, T2, T3, T4, T5)])
                                (txMolecules: MoleculeBase*)
                                (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5)(data)(txMolecules)(conn)

  /* Composite insert with 6 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6])
                                    (data: Seq[(T1, T2, T3, T4, T5, T6)])
                                    (txMolecules: MoleculeBase*)
                                    (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6)(data)(txMolecules)(conn)

  /* Composite insert with 7 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7])
                                        (data: Seq[(T1, T2, T3, T4, T5, T6, T7)])
                                        (txMolecules: MoleculeBase*)
                                        (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7)(data)(txMolecules)(conn)

  /* Composite insert with 8 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8])
                                            (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8)])
                                            (txMolecules: MoleculeBase*)
                                            (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8)(data)(txMolecules)(conn)

  /* Composite insert with 9 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9])
                                                (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9)])
                                                (txMolecules: MoleculeBase*)
                                                (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9)(data)(txMolecules)(conn)

  /* Composite insert with 10 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10])
                                                     (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)])
                                                     (txMolecules: MoleculeBase*)
                                                     (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)(data)(txMolecules)(conn)

  /* Composite insert with 11 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11])
                                                          (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)])
                                                          (txMolecules: MoleculeBase*)
                                                          (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11)(data)(txMolecules)(conn)

  /* Composite insert with 12 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12])
                                                               (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)])
                                                               (txMolecules: MoleculeBase*)
                                                               (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)(data)(txMolecules)(conn)

  /* Composite insert with 13 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13])
                                                                    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)])
                                                                    (txMolecules: MoleculeBase*)
                                                                    (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13)(data)(txMolecules)(conn)

  /* Composite insert with 14 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14])
                                                                         (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)])
                                                                         (txMolecules: MoleculeBase*)
                                                                         (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14)(data)(txMolecules)(conn)

  /* Composite insert with 15 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15])
                                                                              (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)])
                                                                              (txMolecules: MoleculeBase*)
                                                                              (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)(data)(txMolecules)(conn)

  /* Composite insert with 16 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16])
                                                                                   (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)])
                                                                                   (txMolecules: MoleculeBase*)
                                                                                   (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16)(data)(txMolecules)(conn)

  /* Composite insert with 17 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17])
                                                                                        (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)])
                                                                                        (txMolecules: MoleculeBase*)
                                                                                        (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17)(data)(txMolecules)(conn)

  /* Composite insert with 18 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18])
                                                                                             (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)])
                                                                                             (txMolecules: MoleculeBase*)
                                                                                             (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18)(data)(txMolecules)(conn)

  /* Composite insert with 19 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19])
                                                                                                  (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)])
                                                                                                  (txMolecules: MoleculeBase*)
                                                                                                  (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19)(data)(txMolecules)(conn)

  /* Composite insert with 20 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20])
                                                                                                       (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)])
                                                                                                       (txMolecules: MoleculeBase*)
                                                                                                       (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20)(data)(txMolecules)(conn)

  /* Composite insert with 21 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20], m21: Molecule[T21])
                                                                                                            (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)])
                                                                                                            (txMolecules: MoleculeBase*)
                                                                                                            (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21)(data)(txMolecules)(conn)

  /* Composite insert with 22 sub-molecules/tuples - see `insert[T1, T2]` for signature info example.
    * @group insert */
  def insert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20], m21: Molecule[T21], m22: Molecule[T22])
                                                                                                                 (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)])
                                                                                                                 (txMolecules: MoleculeBase*)
                                                                                                                 (implicit conn: Conn): TxReport = _insert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22)(data)(txMolecules)(conn)


  /** Debug composite insert with 2 sub-molecules/tuples
    *
    * A composite insert can be debugged to see the expected transaction in advance without affecting the database:
    * {{{
    *   debugInsert(
    *     Article.name.author, Tag.name.weight
    *   )(
    *     List(
    *       (("Battle of Waterloo", "Ben Bridge"), ("serious", 5)),
    *       (("Best jokes ever", "John Cleese"), ("fun", 3))
    *     )
    *   )(
    *     MetaData.submitter("Brenda Johnson").usecase("AddReviews")
    *   )
    * }}}
    *
    * This will print debugging info about the composite insert to output (without affecting the database):
    *
    * {{{
    *   ## 1 ## Composite arity-2 insert debug
    *   =================================================================================================================
    *   1      Model(
    *     1      Atom(article,name,String,1,VarValue,None,List(),List())
    *     2      Atom(article,author,String,1,VarValue,None,List(),List())
    *     3      Atom(tag,name,String,1,VarValue,None,List(),List())
    *     4      Atom(tag,weight,Long,1,VarValue,None,List(),List())
    *     5      TxMetaData(
    *       1      Atom(metaData,submitter,String,1,Eq(List(Brenda Johnson)),None,List(),List())
    *       2      Atom(metaData,usecase,String,1,Eq(List(AddReviews)),None,List(),List())))
    *   ------------------------------------------------
    *   2      List(
    *     1      :db/add    'tempId    :article/name          'arg                                     Card(1)
    *     2      :db/add    'e         :article/author        'arg                                     Card(1)
    *     3      :db/add    'e         :tag/name              'arg                                     Card(1)
    *     4      :db/add    'e         :tag/weight            'arg                                     Card(1)
    *     5      :db/add    'tx        :metaData/submitter    Values(Eq(List(Brenda Johnson)),None)    Card(1)
    *     6      :db/add    'tx        :metaData/usecase      Values(Eq(List(AddReviews)),None)        Card(1))
    *   ------------------------------------------------
    *   3      List(
    *     1      List(
    *       1      :db/add    #db/id[:db.part/user -1000097]    :article/name          Battle of Waterloo    Card(1)
    *       2      :db/add    #db/id[:db.part/user -1000097]    :article/author        Ben Bridge            Card(1)
    *       3      :db/add    #db/id[:db.part/user -1000097]    :tag/name              serious               Card(1)
    *       4      :db/add    #db/id[:db.part/user -1000097]    :tag/weight            5                     Card(1))
    *     2      List(
    *       1      :db/add    #db/id[:db.part/user -1000098]    :article/name          Best jokes ever       Card(1)
    *       2      :db/add    #db/id[:db.part/user -1000098]    :article/author        John Cleese           Card(1)
    *       3      :db/add    #db/id[:db.part/user -1000098]    :tag/name              fun                   Card(1)
    *       4      :db/add    #db/id[:db.part/user -1000098]    :tag/weight            3                     Card(1))
    *     3      List(
    *       1      :db/add    #db/id[:db.part/tx -1000100]      :metaData/submitter    Brenda Johnson        Card(1)
    *       2      :db/add    #db/id[:db.part/tx -1000100]      :metaData/usecase      AddReviews            Card(1)))
    *   =================================================================================================================
    * }}}
    *
    * The two first sections are internal Molecule representations.
    * <br><br>
    * The third shows the transactional data sent to Datomic, in this case two rows of data (each with 4 datoms) and one row of transaction data (2 datoms).
    *
    * @see [[http://www.scalamolecule.org/manual/crud/composite-insert/ Manual: Composite inserts]]
    * @param m1          First molecule of type [[molecule.action.Molecule Molecule[T1]]]
    * @param m2          Second molecule of type [[molecule.action.Molecule Molecule[T2]]]
    * @param data        List of tuples (rows) of sub-tuples, type: `Seq[(T1, T2)]`
    * @param txMolecules Optional transaction molecule(s) with tx meta data applied to tx attributes.
    * @param conn        Implicit [[molecule.facade.Conn Conn]] in scope
    * @tparam T1 Type of first data tuple (can be single type too)
    * @tparam T2 Type of second data tuple (can be single type too)
    * @return Unit (prints to output)
    * @group insert
    * */
  def debugInsert[T1, T2](m1: Molecule[T1], m2: Molecule[T2])
                         (data: Seq[(T1, T2)])
                         (txMolecules: MoleculeBase*)
                         (implicit conn: Conn): Unit = _debugInsert(m1, m2)(data)(txMolecules)(conn)


  /* Composite insert debugging with 3 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert
    */
  def debugInsert[T1, T2, T3](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3])
                             (data: Seq[(T1, T2, T3)])
                             (txMolecules: MoleculeBase*)
                             (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3)(data)(txMolecules)(conn)


  /* Composite insert debugging with 4 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4])
                                 (data: Seq[(T1, T2, T3, T4)])
                                 (txMolecules: MoleculeBase*)
                                 (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4)(data)(txMolecules)(conn)

  /* Composite insert debugging with 5 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5])
                                     (data: Seq[(T1, T2, T3, T4, T5)])
                                     (txMolecules: MoleculeBase*)
                                     (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5)(data)(txMolecules)(conn)

  /* Composite insert debugging with 6 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6])
                                         (data: Seq[(T1, T2, T3, T4, T5, T6)])
                                         (txMolecules: MoleculeBase*)
                                         (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6)(data)(txMolecules)(conn)

  /* Composite insert debugging with 7 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7])
                                             (data: Seq[(T1, T2, T3, T4, T5, T6, T7)])
                                             (txMolecules: MoleculeBase*)
                                             (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7)(data)(txMolecules)(conn)

  /* Composite insert debugging with 8 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8])
                                                 (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8)])
                                                 (txMolecules: MoleculeBase*)
                                                 (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8)(data)(txMolecules)(conn)

  /* Composite insert debugging with 9 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9])
                                                     (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9)])
                                                     (txMolecules: MoleculeBase*)
                                                     (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9)(data)(txMolecules)(conn)

  /* Composite insert debugging with 10 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10])
                                                          (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)])
                                                          (txMolecules: MoleculeBase*)
                                                          (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10)(data)(txMolecules)(conn)

  /* Composite insert debugging with 11 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11])
                                                               (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)])
                                                               (txMolecules: MoleculeBase*)
                                                               (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11)(data)(txMolecules)(conn)

  /* Composite insert debugging with 12 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12])
                                                                    (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)])
                                                                    (txMolecules: MoleculeBase*)
                                                                    (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12)(data)(txMolecules)(conn)

  /* Composite insert debugging with 13 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13])
                                                                         (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)])
                                                                         (txMolecules: MoleculeBase*)
                                                                         (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13)(data)(txMolecules)(conn)

  /* Composite insert debugging with 14 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14])
                                                                              (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)])
                                                                              (txMolecules: MoleculeBase*)
                                                                              (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14)(data)(txMolecules)(conn)

  /* Composite insert debugging with 15 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15])
                                                                                   (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)])
                                                                                   (txMolecules: MoleculeBase*)
                                                                                   (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15)(data)(txMolecules)(conn)

  /* Composite insert debugging with 16 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16])
                                                                                        (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)])
                                                                                        (txMolecules: MoleculeBase*)
                                                                                        (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16)(data)(txMolecules)(conn)

  /* Composite insert debugging with 17 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17])
                                                                                             (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)])
                                                                                             (txMolecules: MoleculeBase*)
                                                                                             (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17)(data)(txMolecules)(conn)

  /* Composite insert debugging with 18 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18])
                                                                                                  (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)])
                                                                                                  (txMolecules: MoleculeBase*)
                                                                                                  (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18)(data)(txMolecules)(conn)

  /* Composite insert debugging with 19 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19])
                                                                                                       (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)])
                                                                                                       (txMolecules: MoleculeBase*)
                                                                                                       (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19)(data)(txMolecules)(conn)

  /* Composite insert debugging with 20 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20])
                                                                                                            (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)])
                                                                                                            (txMolecules: MoleculeBase*)
                                                                                                            (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20)(data)(txMolecules)(conn)

  /* Composite insert debugging with 21 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20], m21: Molecule[T21])
                                                                                                                 (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)])
                                                                                                                 (txMolecules: MoleculeBase*)
                                                                                                                 (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21)(data)(txMolecules)(conn)

  /* Composite insert debugging with 22 sub-molecules/tuples - see `debugInsert[T1, T2]` for signature info example.
    * @group debugInsert */
  def debugInsert[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](m1: Molecule[T1], m2: Molecule[T2], m3: Molecule[T3], m4: Molecule[T4], m5: Molecule[T5], m6: Molecule[T6], m7: Molecule[T7], m8: Molecule[T8], m9: Molecule[T9], m10: Molecule[T10], m11: Molecule[T11], m12: Molecule[T12], m13: Molecule[T13], m14: Molecule[T14], m15: Molecule[T15], m16: Molecule[T16], m17: Molecule[T17], m18: Molecule[T18], m19: Molecule[T19], m20: Molecule[T20], m21: Molecule[T21], m22: Molecule[T22])
                                                                                                                      (data: Seq[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)])
                                                                                                                      (txMolecules: MoleculeBase*)
                                                                                                                      (implicit conn: Conn): Unit = _debugInsert(m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22)(data)(txMolecules)(conn)
}
