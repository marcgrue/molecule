package molecule.datomic.client.devLocal.facade

import java.{util, lang}
import datomic.{Database, Datom, Entity}
import datomicScala.client.api.sync.Db
import molecule.datomic.base.facade.DatomicDb

case class Database_DevLocal(clientDb: Db) extends DatomicDb {
//  def asOf(t: Long): DatomicDb = ???
//  def since(t: Long): DatomicDb = ???
//  def widh(stmts: util.List[_]): DatomicDb = ???
//  def history: DatomicDb = ???


  def basisT: Long = ???
  def entity(o: Any): Entity = ???

  def pull(pattern: String, eid: Any): util.Map[_, _] =
    clientDb.pull(pattern, eid)

  def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = ???
  def getDatomicDb: AnyRef = clientDb.datomicDb
  def indexRange(o: Any, o1: Any, o2: Any): lang.Iterable[Datom] = ???
}
