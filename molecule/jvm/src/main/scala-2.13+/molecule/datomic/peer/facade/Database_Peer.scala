package molecule.datomic.peer.facade

import java.{lang, util}
import datomic.{Database, Datom, Entity}
import molecule.datomic.base.facade.DatomicDb

case class Database_Peer(peerDb: Database) extends DatomicDb {
  def asOf(t: Long): DatomicDb = Database_Peer(peerDb.asOf(t))
  def since(t: Long): DatomicDb = Database_Peer(peerDb.since(t))

  def widh(stmts: util.List[_]): DatomicDb = ???
  //  def widh(stmts: util.List[_]): DatomicDb = Database_Peer(peerDb.`with`(stmts))

  def history: DatomicDb = Database_Peer(peerDb.history())
  override def entity(o: Any): Entity = peerDb.entity(o)
  override def pull(o: Any, o1: Any): util.Map[_, _] = peerDb.pull(o, o1)
  override def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = peerDb.datoms(o, objects)
  override def getDatomicDb: AnyRef = peerDb
}
