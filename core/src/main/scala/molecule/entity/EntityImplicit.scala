package molecule.entity

import molecule.facade.Conn


/**
  * Entity facade implicit
  *
  * Implicit conversion of entity ids to EntityFacades to allow accessing
  * the Entity API directly from an entity id (of type Long).
  **/
trait EntityImplicit {

  // Long -> Entity api
  implicit def long2Entity(id: Long)(implicit conn: Conn): EntityFacade = EntityFacade(conn.db.entity(id), conn, id.asInstanceOf[Object])
}
