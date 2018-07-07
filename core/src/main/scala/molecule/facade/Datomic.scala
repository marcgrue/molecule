package molecule.facade
import java.util.UUID.randomUUID
import java.util.{List => jList}
import datomic.Peer
import molecule.action.{MoleculeOutBase, TxReport}
import molecule.ast.model.{Model, TxMetaData}
import molecule.ast.transaction.RetractEntity
import molecule.ops.VerifyModel
import molecule.schema.Transaction
import molecule.transform.Model2Transaction
import molecule.util.Debug



/**
  * Facade to Datomic with selected methods
  *
  * @groupname datomic Datomic facade methods
  * @groupprio 10
  **/
trait Datomic {

  // Database operations ..................................................................

  /** Deletes existing database (!) and creates a new empty db with schema from Schema Transaction file
    * <p>
    * A typical development cycle in the initial stages of creating the db schema:
    *
    *  1. Edit schema definition file
    *  1. `sbt compile` to update boilerplate code in generated jars
    *  1. Obtain a fresh connection to new empty db with updated schema:<br>
    *     `implicit val conn = recreateDbFrom(YourDomainSchema)`
    *
    * @group datomic
    * @param schema     Auto-generated YourDomainSchema Transaction object<br>
    *                   (in package yourdomain.schema of generated source jar)
    * @param identifier Optional String identifier to name database (default empty string creates a randomUUID)
    * @param protocol   Datomic protocol. Defaults to "mem" for in-memory database.
    * @return           [[molecule.facade.Conn Conn]]
    */
  def recreateDbFrom(schema: Transaction, identifier: String = "", protocol: String = "mem"): Conn = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val datConn = Peer.connect(uri)
      datConn.transact(schema.partitions)
      datConn.transact(schema.namespaces)
      Conn(datConn)
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }

  /** Deletes existing database (!) and creates a new empty db with schema from schema data structure
    * <p>
    * Schema data structure is a java List of Map's of key/value pairs defining the schema.
    * <br>Can be an [[https://github.com/edn-format/edn EDN]] file like the [[https://github.com/Datomic/mbrainz-sample/blob/master/schema.edn mbrainz example]].
    *
    * @see [[https://docs.datomic.com/on-prem/data-structure-literals.html]]
    *
    * @group datomic
    * @param schemaData  java.util.List of java.util.Maps of key/values defining a Datomic schema
    * @param identifier  Optional String identifier to name database (default empty string creates a randomUUID)
    * @param protocol    Datomic protocol. Defaults to "mem" for in-memory database.
    * @return            [[molecule.facade.Conn Conn]]
    */
  def recreateDbFromRaw(schemaData: java.util.List[_], identifier: String = "", protocol: String = "mem"): Conn = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val datConn = Peer.connect(uri)
      datConn.transact(schemaData).get()
      Conn(datConn)
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }


  // Schema ............................................................................

  // From Molecule auto-generated schema transaction data
  private[molecule] def transactSchema(schema: Transaction, identifier: String, protocol: String = "mem"): Conn = {
    val uri = s"datomic:$protocol://$identifier"
    try {
      val datConn = Peer.connect(uri)
      datConn.transact(schema.partitions)
      datConn.transact(schema.namespaces)
      Conn(datConn)
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }


  // Retracting entities with tx meta data .........................................................

  /** Retract multiple entities with optional transaction data
    *
    * Transaction data can be asserted together with the retraction of entities.
    * <br>Here we retract two comment entities with transaction data telling us that the retractions
    * are part of a usecase "Cleanup noise" and done by user "Ben Goodman":
    * {{{
    *   retract(
    *     Seq(commentEid1, commentEid2),
    *     MetaData.usecase("Cleanup noise").User.name("Ben Goodman")
    *   )
    * }}}
    * We can then later see what comments Ben cleaned up in the past (`op_(false)` means retracted):
    * {{{
    *   Comment.e.text.op_(false).Tx(MetaData.usecase_("Cleanup noise").User.name_("Ben Goodman")).getHistory === List(
    *     (commentEid1, "I like this"),
    *     (commentEid2, "I hate this")
    *   )
    * }}}
    *
    * @param eids    Iterable of entity ids of type Long
    * @param txMeta  Transaction molecule with only tacit attributes
    * @param conn    [[molecule.facade.Conn Conn]]
    * @return        [[molecule.action.TxReport TxReport]] with result of retract
    */
  def retract(eids: Iterable[Long], txMeta: MoleculeOutBase = null)(implicit conn: Conn): TxReport = {
    val retractStmts = eids.toSeq.distinct map RetractEntity

    val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    val stmtss = Seq(retractStmts ++ txMetaStmts)

    conn.transact(stmtss)
  }


  /** Debug retracting multiple entities with optional transaction data
    *
    * Without affecting the database, a multiple entity retract action can be debugged by adding a 'D' to the retract method:
    * Transaction data can be asserted together with the retraction of entities.
    * <br>Here we debug a retraction of two comment entities with transaction data telling us that the retractions
    * are part of a cleanup by Ben Goodman.
    * {{{
    *   retractD(
    *     Seq(commentEid1, commentEid2),
    *     MetaData.usecase("Cleanup noise").User.name("Ben Goodman")
    *   )
    * }}}
    * This will print debugging info about the retraction to output (without affecting the database):
    * {{{
    *   ## 1 ## molecule.Datomic.retractD
    *   ===================================================================================================================
    *   1      Model(
    *     1      TxMetaData(
    *       1      Atom(metaData,usecase,String,1,Eq(List(b)),None,List(),List())))
    *   ------------------------------------------------
    *   2      List(
    *     1      :db/add                  'tx                             :ns/str     Values(Eq(List(b)),None)    Card(1))
    *   ------------------------------------------------
    *   3      List(
    *     1      List(
    *       1      :db.fn/retractEntity                                               42
    *       2      :db.fn/retractEntity                                               43
    *       3      :db/add                #db/id[:db.part/tx -1000097]    :ns/str     b                           Card(1)))
    *   ===================================================================================================================
    * }}}
    *
    * @param eids    Iterable of entity ids of type Long
    * @param txMeta  Transaction molecule with only tacit attributes
    * @param conn    [[molecule.facade.Conn Conn]]
    * @return Unit (prints to output)
    */
  def retractD(eids: Iterable[Long], txMeta: MoleculeOutBase = null)(implicit conn: Conn): Unit = {
    val retractStmts = eids.toSeq.distinct map RetractEntity
    val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)

    val stmts = try {
      Seq(retractStmts ++ transformer.saveStmts())
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("molecule.Datomic.retractD", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("molecule.Datomic.retractD", 1)(1, _model, transformer.stmtsModel, stmts)
  }
}
