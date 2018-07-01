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
  **/

trait Datomic {

  // Database operations ..................................................................

  // From Molecule auto-generated schema transaction data
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

  // From raw data like EDN file
  def recreateDbFromRaw(schemaData: jList[_], identifier: String = "", protocol: String = "mem"): Conn = {
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
  def transactSchema(schema: Transaction, identifier: String, protocol: String = "mem"): Conn = {
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

  def retract(eids: Iterable[Long], txMeta: MoleculeOutBase = null)(implicit conn: Conn): TxReport = {
    val retractStmts = eids.toSeq.distinct map RetractEntity

    val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    val stmtss = Seq(retractStmts ++ txMetaStmts)

    conn.transact(stmtss)
  }

  def retractD(eids: Iterable[Long], txMeta: MoleculeOutBase = null)(implicit conn: Conn) {
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

  // Hacks to allow multiple overloaded methods with default argument
  object retract {
    def apply(eid: Long, txMeta: MoleculeOutBase = null)(implicit conn: Conn): TxReport = retract(Seq(eid), txMeta)(conn)
  }
  object retractD {
    def apply(eid: Long, txMeta: MoleculeOutBase = null)(implicit conn: Conn): Unit = retractD(Seq(eid), txMeta)(conn)
  }

}
