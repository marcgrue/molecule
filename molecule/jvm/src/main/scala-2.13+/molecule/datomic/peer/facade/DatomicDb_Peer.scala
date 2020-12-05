package molecule.datomic.peer.facade

import java.{lang, util}
import java.util.Date
import datomic.{Database, Peer, Util, Datom => PeerDatom}
import molecule.core.api.DatomicEntity
import molecule.datomic.base.facade.{Conn, DatomicDb}

case class DatomicDb_Peer(peerDb: Database) extends DatomicDb {

  def getDatomicDb: AnyRef = peerDb

  def t: Long = peerDb.basisT()

  def tx: Long = Peer.toTx(t).asInstanceOf[Long]

  def txInstant: Date = peerDb.pull("[:db/txInstant]", tx)
    .get(Util.read(":db/txInstant")).asInstanceOf[Date]

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
