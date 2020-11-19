package molecule.datomic.peer.facade

import java.{lang, util}
import datomic.{Database, Datom, Entity}
import molecule.datomic.base.facade.DatomicDb

case class Database_Peer(peerDb: Database) extends DatomicDb {

//  def asOf(t: Long): DatomicDb = Database_Peer(peerDb.asOf(t))
//  def asOf(t: Date): DatomicDb = Database_Peer(peerDb.asOf(t))
//
//  def since(t: Long): DatomicDb = Database_Peer(peerDb.since(t))
//  def since(t: Date): DatomicDb = Database_Peer(peerDb.since(t))
//
//  def widh(stmts: util.List[_]): DatomicDb = ???
//  def `with`(stmts: util.List[_]): DatomicDb = Database_Peer(peerDb.`with`(stmts))
//
//  def history: DatomicDb = Database_Peer(peerDb.history())

  def basisT: Long = peerDb.basisT()

  def entity(o: Any): Entity = peerDb.entity(o)

  def pull(pattern: String, eid: Any): util.Map[_, _] =
    peerDb.pull(pattern, eid)

  def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = peerDb.datoms(o, objects: _*)

  def getDatomicDb: AnyRef = peerDb

  def indexRange(o: Any, o1: Any, o2: Any): lang.Iterable[Datom] = peerDb.indexRange(o, o1, o2)
}
