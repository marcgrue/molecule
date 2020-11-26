package molecule.datomic.client.facade

import java.util
import java.util.stream.{Stream => jStream}
import datomicScala.client.api.{Datom => ClientDatom}
import datomicScala.client.api.sync.Db
import molecule.core.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}
import scala.jdk.CollectionConverters._

case class DatomicDb_Client(clientDb: Db) extends DatomicDb {

  def getDatomicDb: AnyRef = clientDb.datomicDb

  def basisT: Long = clientDb.basisT

  def entity(conn: Conn, id: Any): DatomicEntity = {
    DatomicEntity_Client(conn.asInstanceOf[Conn_Client], id)
  }

  def pull(pattern: String, eid: Any): util.Map[_, _] = {
    clientDb.pull(pattern, eid)
  }

  def datoms(index: String, components: Any*): jStream[ClientDatom] = {
    clientDb.datoms(index, components.toSeq.asJava)
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
