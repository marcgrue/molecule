package molecule
package api
import molecule.facade.EntityFacade

trait EntityImplicit {

  // Long -> Entity api
  implicit def long2Entity(id: Long)(implicit conn: Conn): EntityFacade = EntityFacade(conn.db.entity(id), conn, id.asInstanceOf[Object])
}
