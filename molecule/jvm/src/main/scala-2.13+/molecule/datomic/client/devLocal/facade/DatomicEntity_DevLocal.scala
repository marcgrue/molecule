package molecule.datomic.client.devLocal.facade

import java.util.{Date, UUID, Collection => jCollection}
import clojure.lang.MapEntry
import molecule.core.api.exception.EntityException
import molecule.core.api.DatomicEntity
import scala.jdk.CollectionConverters._
import scala.language.existentials


case class DatomicEntity_DevLocal(
  conn: Conn_DevLocal,
  eid: Any,
  showKW: Boolean = true
) extends DatomicEntity(conn, eid) {

  val map: Map[String, Any] = conn.db.pull("[*]", eid).asScala.toMap.map {
    case (k, v) =>
      (k.toString, v)
  }

  def keySet: Set[String] = map.keySet
  def keys: List[String] =
    map.keySet.toList

  def apply(key: String): Any = map(key)
  def value(key: String): Any = map(key)

  private[molecule] def toScala(
    v: Any,
    depth: Int = 1,
    maxDepth: Int = 5,
    tpe: String = "Map"
  ): Any = v match {
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

    case kw: clojure.lang.Keyword if showKW =>
      kw.toString

    case kw: clojure.lang.Keyword =>
      conn.db.entity(conn, kw).value(":db/id")

    case set: clojure.lang.PersistentHashSet =>
      set.asScala.toList.map(toScala(_, depth, maxDepth, tpe))

    case map0: clojure.lang.PersistentArrayMap => {
      map0 match {
        case m if m.size() == 2 && m.containsKey(ident) =>
          m.get(ident).toString

        case m if depth < maxDepth =>
          val e = DatomicEntity_DevLocal(
            conn,
            m.iterator().next().asInstanceOf[MapEntry].getValue
          )
          tpe match {
            case "Map"  =>
              e.asMap(depth + 1, maxDepth)
            case "List" =>
              e.asList(depth + 1, maxDepth)
          }

        case m =>
          m.iterator().next().asInstanceOf[MapEntry].getValue.asInstanceOf[Long]
      }
    }

    case vec: clojure.lang.PersistentVector =>
      sortList(
        vec.asScala.toList.map(toScala(_, depth, maxDepth, tpe))
      )

    case col: jCollection[_] =>
      new Iterable[Any] {
        override def iterator = new Iterator[Any] {
          private val jIter = col.iterator.asInstanceOf[java.util.Iterator[AnyRef]]
          override def hasNext = jIter.hasNext
          override def next() = if (depth < maxDepth)
            toScala(jIter.next(), depth, maxDepth, tpe)
          else
            jIter.next()
        }
        override def isEmpty = col.isEmpty
        override def size = col.size
        override def toString = col.toString
      }

    case null => throw new EntityException("Unexpectedly received null")

    case unexpected => throw new EntityException(
      "Unexpected Datalog type to convert: " + unexpected.getClass.toString)
  }
}