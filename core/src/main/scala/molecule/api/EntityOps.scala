package molecule.api
import molecule.ast.MoleculeBase
import molecule.ast.model.{Model, TxMetaData}
import molecule.ast.transactionModel.RetractEntity
import molecule.facade.{Conn, TxReport}
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.concurrent.{ExecutionContext, Future}

/** Operations on multiple entities.
  *
  * @groupname entityOps    Entity operations
  * @groupprio 44
  */
trait EntityOps {

  /** Long -> [[molecule.api.Entity Entity]] api implicit.
    * <br><br>
    * Convenience implicit to allow calling [[molecule.api.Entity Entity]] methods directly on entity Long value.
    * {{{
    *   // Get entity id of Ben
    *   val benId = Person.e.name_("Ben").get.head
    *
    *   // Retract Ben entity directly on his entity id
    *   benId.retract
    * }}}
    *
    * @group entityOps
    * @param id   Entity id of type Long
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return
    */
  implicit final def long2Entity(id: Long)(implicit conn: Conn): Entity = Entity(conn.db.entity(id), conn, id.asInstanceOf[Object])

  /** Retract multiple entities with optional transaction meta data.
    * <br><br>
    * 0 or more transaction meta data molecules can be asserted together with a retraction of entities.
    * <br><br>
    * Here we retract two comment entities with transaction meta data asserting that the retraction was done by Ben Goodman:
    * {{{
    *   retract(Seq(commentEid1, commentEid2), MetaData.user("Ben Goodman"))
    * }}}
    * We can then later see what comments Ben Goodman retracted (`op_(false)`):
    * {{{
    *   Comment.e.text.op_(false).Tx(MetaData.user_("Ben Goodman")).getHistory === List(
    *     (commentEid1, "I like this"),
    *     (commentEid2, "I hate this")
    *   )
    * }}}
    * @see [[http://www.scalamolecule.org/manual/crud/retract/ Manual]]
    *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/Retract.scala#L1 Test]]
    * @group entityOps
    * @param eids                Iterable of entity ids of type Long
    * @param txMetaDataMolecules Zero or more transaction meta data molecules
    * @param conn                Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return [[molecule.facade.TxReport TxReport]] with result of retract
    */
  def retract(eids: Iterable[Long], txMetaDataMolecules: MoleculeBase*)(implicit conn: Conn): TxReport = {
    val retractStmts = eids.toSeq.distinct map RetractEntity

    val txMetaDataStmts = if (txMetaDataMolecules.isEmpty) {
      Nil
    } else if (txMetaDataMolecules.size == 1) {
      val txMetaDataModel = Model(Seq(TxMetaData(txMetaDataMolecules.head._model.elements)))
      VerifyModel(txMetaDataModel, "save")
      Model2Transaction(conn, txMetaDataModel).saveStmts()
    } else {
      val txMetaDataModel = Model(
        txMetaDataMolecules.map(m => TxMetaData(m._model.elements))
      )
      VerifyModel(txMetaDataModel, "save")
      Model2Transaction(conn, txMetaDataModel).saveStmts()
    }

    val stmtss = Seq(retractStmts ++ txMetaDataStmts)
    conn.transact(stmtss)
  }

  /** Asynchronously retract multiple entities with optional transaction meta data.
    * <br><br>
    * 0 or more transaction meta data molecules can be asserted together with a retraction of entities.
    * <br><br>
    * Here we asynchronously retract two comment entities with transaction meta data asserting that the retraction was done by Ben Goodman:
    * {{{
    *   retractAsync(Seq(commentEid1, commentEid2), MetaData.user("Ben Goodman"))
    * }}}
    * We can then later see what comments Ben Goodman retracted (`op_(false)`):
    * {{{
    *   Comment.e.text.op_(false).Tx(MetaData.user_("Ben Goodman")).getHistory === List(
    *     (commentEid1, "I like this"),
    *     (commentEid2, "I hate this")
    *   )
    * }}}
    * @see [[http://www.scalamolecule.org/manual/crud/retract/ Manual]]
    *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/Retract.scala#L1 Test]]
    * @group entityOps
    * @param eids                Iterable of entity ids of type Long
    * @param txMetaDataMolecules Zero or more transaction meta data molecules
    * @param conn                Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return [[molecule.facade.TxReport TxReport]] with result of retract
    */
  def retractAsync(eids: Iterable[Long], txMetaDataMolecules: MoleculeBase*)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = {
    val retractStmts = eids.toSeq.distinct map RetractEntity

    val txMetaDataStmts = if (txMetaDataMolecules.isEmpty) {
      Nil
    } else if (txMetaDataMolecules.size == 1) {
      val txMetaDataModel = Model(Seq(TxMetaData(txMetaDataMolecules.head._model.elements)))
      VerifyModel(txMetaDataModel, "save")
      Model2Transaction(conn, txMetaDataModel).saveStmts()
    } else {
      val txMetaDataModel = Model(
        txMetaDataMolecules.map(m => TxMetaData(m._model.elements))
      )
      VerifyModel(txMetaDataModel, "save")
      Model2Transaction(conn, txMetaDataModel).saveStmts()
    }

    val stmtss = Seq(retractStmts ++ txMetaDataStmts)
    conn.transactAsync(stmtss)
  }


  /** Debug retracting multiple entities with optional transaction meta data.
    * <br><br>
    * Without affecting the database, a multiple entity retract action can be debugged by adding a 'D' (for 'Debug') to the retract method.
    * <br><br>
    * Here we debug a possible retraction of two comment entities with transaction meta data asserting that the retraction was done by Ben Goodman:
    * {{{
    *   debugRetract(Seq(commentEid1, commentEid2), MetaData.user("Ben Goodman"))
    * }}}
    * This will print debugging info about the retraction to output (without affecting the database):
    * {{{
    *   ## 1 ## molecule.Datomic.debugRetract
    *   ===================================================================================================================
    *   1      Model(
    *     1      TxMetaData(
    *       1      Atom(metaData,user,String,1,Eq(List(Ben Goodman)),None,List(),List())))
    *   ------------------------------------------------
    *   2      List(
    *     1      :db/add     'tx                             :metaData/user     Values(Eq(List(Ben Goodman)),None)   Card(1))
    *   ------------------------------------------------
    *   3      List(
    *     1      List(
    *       1      :db.fn/retractEntity   17592186045445
    *       2      :db.fn/retractEntity   17592186045446
    *       3      :db/add   #db/id[:db.part/tx -1000097]    :metaData/user     b                                    Card(1)))
    *   ===================================================================================================================
    * }}}
    *
    * @group entityOps
    * @param eids                Iterable of entity ids of type Long
    * @param txMetaDataMolecules Zero or more transaction meta data molecules
    * @param conn                Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Unit (prints to output)
    */
  def debugRetract(eids: Iterable[Long], txMetaDataMolecules: MoleculeBase*)(implicit conn: Conn): Unit = {
    val retractStmts = eids.toSeq.distinct map RetractEntity

    val txMetaDataModel = if (txMetaDataMolecules.isEmpty) {
      Model(Nil)
    } else if (txMetaDataMolecules.size == 1) {
      val txMetaDataModel = Model(Seq(TxMetaData(txMetaDataMolecules.head._model.elements)))
      VerifyModel(txMetaDataModel, "save")
      txMetaDataModel
    } else {
      val txMetaDataModel = Model(
        txMetaDataMolecules.map(m => TxMetaData(m._model.elements))
      )
      VerifyModel(txMetaDataModel, "save")
      txMetaDataModel
    }

    val transformer = Model2Transaction(conn, txMetaDataModel)

    val stmts = try {
      Seq(retractStmts ++ transformer.saveStmts())
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("molecule.Datomic.debugRetract", 1)(1, txMetaDataModel, transformer.stmtsModel)
        throw e
    }
    Debug("molecule.Datomic.debugRetract", 1)(1, txMetaDataModel, transformer.stmtsModel, stmts)
  }
}
