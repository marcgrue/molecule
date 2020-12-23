package molecule.datomic.peer.facade

import java.util.{Date, UUID, Collection => jCollection}
import molecule.core.api.exception.EntityException
import molecule.datomic.base.api.DatomicEntity
import scala.collection.JavaConverters._
import scala.language.existentials


case class DatomicEntity_Peer(
  entity: datomic.Entity,
  conn: Conn_Peer,
  eid: Any,
  showKW: Boolean = true
) extends DatomicEntity(conn, eid) {

  def keySet: Set[String] = entity.keySet().asScala.toSet
  def keys: List[String] = entity.keySet().asScala.toList

  def rawValue(key: String): Any = {
    val raw = entity.get(key)
    val b = raw
    b
  }

  private[molecule] def toScala(
    key: String,
    vOpt: Option[Any],
    depth: Int = 1,
    maxDepth: Int = 5,
    tpe: String = "Map"
  ): Any = {
    vOpt.getOrElse(rawValue(key)) match {
      case s: java.lang.String      => s
      case i: java.lang.Integer     => i.toLong: Long
      case l: java.lang.Long        => l: Long
      case f: java.lang.Float       => f: Float
      case d: java.lang.Double      => d: Double
      case b: java.lang.Boolean     => b: Boolean
      case d: Date                  => d
      case u: UUID                  => u
      case u: java.net.URI          => u
      case bi: java.math.BigInteger => BigInt(bi)
      case bd: java.math.BigDecimal => BigDecimal(bd)
      case bytes: Array[Byte]       => bytes

      case kw: clojure.lang.Keyword =>
        if (showKW)
          kw.toString
        else
          conn.db.entity(conn, kw).rawValue(":db/id")

      case e: datomic.Entity =>
        if (depth < maxDepth) {
          val ent = DatomicEntity_Peer(e, conn, e.get(":db/id"))
          tpe match {
            case "Map"  => ent.asMap(depth + 1, maxDepth)
            case "List" => ent.asList(depth + 1, maxDepth)
          }
        } else {
          e.get(":db/id").asInstanceOf[Long]
        }

      case set: clojure.lang.PersistentHashSet =>
        sortList(set.asScala.toList.map(v1 =>
          toScala(key, Some(v1), depth, maxDepth, tpe))
        )

      case col: jCollection[_] =>
        new Iterable[Any] {
          override def iterator = new Iterator[Any] {
            private val jIter = col.iterator.asInstanceOf[java.util.Iterator[AnyRef]]
            override def hasNext = jIter.hasNext
            override def next() = if (depth < maxDepth)
              toScala(key, Some(jIter.next()), depth, maxDepth, tpe)
            else
              jIter.next()
          }
          override def isEmpty = col.isEmpty
          override def size = col.size
          override def toString = col.toString
        }

      case unexpected => throw new EntityException(
        "Unexpected Datalog type to convert: " + unexpected.getClass.toString)
    }
  }
}