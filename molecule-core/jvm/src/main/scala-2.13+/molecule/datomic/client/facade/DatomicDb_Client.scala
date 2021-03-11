package molecule.datomic.client.facade

import java.util
import java.util.Date
import java.util.stream.{Stream => jStream}
import datomic.{Peer, Util}
import datomicScala.client.api.sync.Db
import datomicScala.client.api.{Datom => ClientDatom}
import molecule.datomic.base.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}
import scala.jdk.CollectionConverters._

/** Datomic Db facade for client api (peer-server/cloud/dev-local).
 *
 * @param clientDb
 */
case class DatomicDb_Client(clientDb: Db) extends DatomicDb {

  def getDatomicDb: AnyRef = clientDb.datomicDb

  def t: Long = clientDb.t

  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  def txInstant: Date = clientDb.pull("[:db/txInstant]", tx)
    .get(Util.read(":db/txInstant")).asInstanceOf[Date]

  def entity(conn: Conn, id: Any): DatomicEntity = {
    DatomicEntity_Client(conn.asInstanceOf[Conn_Client], id)
  }

  def pull(pattern: String, eid: Any): util.Map[_, _] = {
    clientDb.pull(pattern, eid)
  }

  def datoms(
    index: String,
    components: Seq[Any] = Nil,
    timeout: Int = 0,
    offset: Int = 0,
    limit: Int = 1000
  ): jStream[ClientDatom] = {
    clientDb.datoms(index, components.asJava, timeout, offset, limit)
  }

  def indexRange(
    attrId: String,
    startValue: Option[Any], // inclusive
    endValue: Option[Any], // exclusive
    timeout: Int = 0,
    offset: Int = 0,
    limit: Int = 1000
  ): jStream[ClientDatom] = {
    clientDb.indexRange(attrId, startValue, endValue, timeout, offset, limit)
  }
}
