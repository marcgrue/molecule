package molecule
package facade
import java.util.UUID.randomUUID
import java.util.{Collection => jColl, List => jList, Map => jMap}

import datomic.Peer
import molecule.schema.Transaction

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

  // Migration ............................................................................

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
}
