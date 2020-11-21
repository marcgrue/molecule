package molecule.datomic.base.facade

import java.util
import datomic.Datom
import molecule.core.api.DatomicEntity

trait DatomicDb {

  def getDatomicDb: AnyRef

  def basisT: Long

  def entity(conn: Conn, id: Any): DatomicEntity

  def pull(pattern: String, eid: Any): util.Map[_, _]

  def datoms(o: Any, objects: Any*): java.lang.Iterable[Datom]

  def indexRange(o: Any, o1: Any, o2: Any): java.lang.Iterable[Datom]


}
