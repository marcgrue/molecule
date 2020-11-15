package molecule.datomic.peer.facade

import java.{lang, util}
import datomic.{Database, Datom, Entity}
import molecule.datomic.base.facade.DatomicDb

case class Database_Peer(peerDb: Database) extends DatomicDb {
  def asOf(t: Long): DatomicDb = ???
  def since(t: Long): DatomicDb = ???
  def widh(stmts: util.List[_]): DatomicDb = ???
  def history: DatomicDb = ???
  override def entity(o: Any): Entity = ???
  override def pull(o: Any, o1: Any): util.Map[_, _] = ???
  override def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = ???
  override def getDatomicDb: AnyRef = peerDb
}
