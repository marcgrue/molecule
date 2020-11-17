package molecule.datomic.base.facade

import datomic.Util.read
import datomicClojure.{ErrorMsg, Invoke}
import datomicScala.client.api.sync.Db
import java.util.{Date, List => jList, Map => jMap}
import datomic.{Datom, Entity}

trait DatomicDb {

  def getDatomicDb: AnyRef

//  def asOf(t: Long): DatomicDb
//
//  def since(t: Long): DatomicDb
//
//  def widh(stmts: jList[_]): DatomicDb
//
//  def history: DatomicDb

  def entity(o: Any): Entity

  def pull(o: Any, o1: Any): java.util.Map[_, _]

  def datoms(o: Any, objects: Any*): java.lang.Iterable[Datom]

  def indexRange(o: Any, o1: Any, o2: Any): java.lang.Iterable[Datom]


}
