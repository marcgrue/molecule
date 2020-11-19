package molecule.datomic.base.facade

import datomic.Util.read
import datomicClojure.{ErrorMsg, Invoke}
import datomicScala.client.api.sync.Db
import java.util.{Date, List => jList, Map => jMap}
import java.util
import datomic.{Datom, Entity}
import molecule.datomic.peer.facade.Database_Peer

trait DatomicDb {

  def getDatomicDb: AnyRef

//  def asOf(t: Long): DatomicDb
//  def asOf(t: Date): DatomicDb
//
//  def since(t: Long): DatomicDb
//  def since(t: Date): DatomicDb
//
//  def widh(stmts: jList[_]): DatomicDb
//  def `with`(stmts: util.List[_]): DatomicDb
//
//  def history: DatomicDb

  def basisT: Long

  def entity(o: Any): Entity

  def pull(pattern: String, eid: Any): util.Map[_, _]

  def datoms(o: Any, objects: Any*): java.lang.Iterable[Datom]

  def indexRange(o: Any, o1: Any, o2: Any): java.lang.Iterable[Datom]


}
