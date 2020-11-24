package molecule.datomic.base.facade

import java.util
import datomic.Datom
import molecule.core.api.DatomicEntity

trait DatomicDb {

  def getDatomicDb: AnyRef

  def basisT: Long

  def entity(conn: Conn, id: Any): DatomicEntity

  def pull(pattern: String, eid: Any): util.Map[_, _]

//  def datoms(index: Any, objects: Any*): AnyRef

//  def indexRange(attrId: String, start: Any, end: Any): AnyRef
}
