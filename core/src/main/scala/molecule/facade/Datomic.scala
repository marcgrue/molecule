package molecule
package facade
import java.util.UUID.randomUUID
import java.util.{Collection => jColl, List => jList, Map => jMap}

import datomic.Peer
import molecule.schema.Transaction

trait Datomic {

  // Database operations ..................................................................

  def recreateDbFrom(tx: Transaction, identifier: String = "", protocol: String = "mem"): Conn = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val connection = Peer.connect(uri)
      connection.transact(tx.partitions)
      connection.transact(tx.namespaces)
      Conn(connection)
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }

  def loadList(txlist: jList[_], identifier: String = "", protocol: String = "mem"): Conn = {
    val id = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val conn = Peer.connect(uri)
      conn.transact(txlist).get()
      Conn(conn)
    } catch {
      case e: Throwable => sys.error("@@@@@@@@@@ " + e.getCause)
    }
  }
}
