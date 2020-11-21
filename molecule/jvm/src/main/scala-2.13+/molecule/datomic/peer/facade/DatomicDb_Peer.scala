package molecule.datomic.peer.facade

import java.{lang, util}
import datomic.{Database, Datom}
import molecule.core.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}

case class DatomicDb_Peer(peerDb: Database) extends DatomicDb {

  def getDatomicDb: AnyRef = peerDb

  def basisT: Long = peerDb.basisT()

  def entity(conn: Conn, id: Any): DatomicEntity =
    DatomicEntity_Peer(peerDb.entity(id), conn.asInstanceOf[Conn_Peer], id)

  def pull(pattern: String, eid: Any): util.Map[_, _] =
    peerDb.pull(pattern, eid)

  def datoms(o: Any, objects: Any*): lang.Iterable[Datom] =
    peerDb.datoms(o, objects: _*)


  def indexRange(o: Any, o1: Any, o2: Any): lang.Iterable[Datom] =
    peerDb.indexRange(o, o1, o2)
}
