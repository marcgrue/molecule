package molecule.datomic.client.devLocal.facade

import java.{util, lang}
import datomic.{Database, Datom, Entity}
import datomicScala.client.api.sync.Db
import molecule.datomic.base.facade.DatomicDb

case class Database_DevLocal(clientDb: Db) extends DatomicDb {
  def asOf(t: Long): DatomicDb = ???
  def since(t: Long): DatomicDb = ???
  def widh(stmts: util.List[_]): DatomicDb = ???
  def history: DatomicDb = ???
  override def entity(o: Any): Entity = ???
  override def pull(o: Any, o1: Any): util.Map[_, _] = ???
  override def datoms(o: Any, objects: Any*): lang.Iterable[Datom] = ???
  override def getDatomicDb: AnyRef = clientDb.datomicDb
}
