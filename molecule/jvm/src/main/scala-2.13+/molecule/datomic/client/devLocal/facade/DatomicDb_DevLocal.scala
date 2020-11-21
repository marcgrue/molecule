package molecule.datomic.client.devLocal.facade

import java.{lang, util}
import datomic.Datom
import datomicScala.client.api.sync.Db
import molecule.core.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}

case class DatomicDb_DevLocal(clientDb: Db) extends DatomicDb {

  def getDatomicDb: AnyRef = clientDb.datomicDb

  def basisT: Long = ???

  def entity(conn: Conn, id: Any): DatomicEntity =
    DatomicEntity_DevLocal(conn.asInstanceOf[Conn_DevLocal], id)

  def pull(pattern: String, eid: Any): util.Map[_, _] =
    clientDb.pull(pattern, eid)

  def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = ???


  def indexRange(o: Any, o1: Any, o2: Any): lang.Iterable[Datom] = ???
}
