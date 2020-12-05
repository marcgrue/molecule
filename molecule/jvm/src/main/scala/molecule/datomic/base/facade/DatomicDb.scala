package molecule.datomic.base.facade

import java.util
import java.util.Date
import datomic.Datom
import molecule.core.api.DatomicEntity

trait DatomicDb {

  def getDatomicDb: AnyRef

  def t: Long

  def tx: Long

  def txInstant: Date

  def entity(conn: Conn, id: Any): DatomicEntity

  def pull(pattern: String, eid: Any): util.Map[_, _]

//  def datoms(index: Any, objects: Any*): AnyRef

//  def indexRange(attrId: String, start: Any, end: Any): AnyRef
}
