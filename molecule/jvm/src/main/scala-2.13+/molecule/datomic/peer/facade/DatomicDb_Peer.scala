package molecule.datomic.peer.facade

import java.{lang, util}
import datomic.{Database, Datom => PeerDatom}
import molecule.core.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}

case class DatomicDb_Peer(peerDb: Database) extends DatomicDb {

  def getDatomicDb: AnyRef = peerDb

  def basisT: Long = peerDb.basisT()

  def entity(conn: Conn, id: Any): DatomicEntity = {
    DatomicEntity_Peer(peerDb.entity(id), conn.asInstanceOf[Conn_Peer], id)
  }

  def pull(pattern: String, eid: Any): util.Map[_, _] =
    peerDb.pull(pattern, eid)

  def datoms(index: Any, objects: Any*): lang.Iterable[PeerDatom] = {
    peerDb.datoms(index, objects: _*)
  }

  def indexRange(
    attrId: String,
    start: Any,
    end: Any
  ): lang.Iterable[PeerDatom] = {
    peerDb.indexRange(attrId, start, end)
  }
}
