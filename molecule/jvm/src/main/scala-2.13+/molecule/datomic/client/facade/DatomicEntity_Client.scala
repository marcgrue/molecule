package molecule.datomic.client.facade

import java.util.{Date, UUID, Collection => jCollection}
import clojure.lang.MapEntry
import molecule.core.api.exception.EntityException
import molecule.core.api.DatomicEntity
import scala.jdk.CollectionConverters._
import scala.language.existentials


case class DatomicEntity_Client(
  conn: Conn_Client,
  eid: Any,
  showKW: Boolean = true
) extends DatomicEntity(conn, eid) {

  private def getThisLevel(eid: Any): Map[String, Any] = {
    conn.q(
      """[:find ?a2 ?v
        | :in $ ?eid
        | :where
        |   [?eid ?a ?v]
        |   [?a :db/ident ?a1]
        |   [(str ?a1) ?a2]
        | ]""".stripMargin, eid)
      .map(l => (l.head.toString, l(1)))
      .toMap + (":db/id" -> eid)
  }

  lazy val map: Map[String, Any] = {
    val res = try {
      conn.db.pull("[*]", eid).asScala.toMap.map {
        case (k, v) => (k.toString, v)
      }
    } catch {
      case _: StackOverflowError =>
        // Fetch top level only for cyclic graphs
        getThisLevel(eid)

      case e: Throwable => throw e
    }
    res
  }

  def keySet: Set[String] = map.keySet
  def keys: List[String] = map.keySet.toList

  def apply(key: String): Any = map(key)
  def value(key: String): Any = map(key)

  def isAttrDef(entityMap: Map[String, Any]): Boolean = {
    entityMap.keys.toList.intersect(List(
      ":db/ident",
      ":db/valueType",
      ":db/cardinality",
      ":db/doc",
    )).nonEmpty
  }

  private[molecule] def toScala(
    key: String,
    vOpt: Option[Any],
    depth: Int = 1,
    maxDepth: Int = 5,
    tpe: String = "Map"
  ): Any = {
    vOpt.getOrElse(value(key)) match {
      case s: java.lang.String      => s
      case i: java.lang.Integer     => i.toLong: Long
      case l: java.lang.Long        =>
        if (depth < maxDepth && key != ":db/id") {
          val entityMap = DatomicEntity_Client(conn, l).asMap(depth + 1, maxDepth)
          // Return number if unintended entity map is returned
          if (
            entityMap.size == 1 // Empty entity (contains only :db/id)
              || isAttrDef(entityMap) // Attribute definitions
          ) l else entityMap
        } else {
          l: Long
        }
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
        set.asScala.toList.map(v1 =>
          toScala(key, Some(v1), depth, maxDepth, tpe)
        )

      case map0: clojure.lang.PersistentArrayMap => {
        map0 match {
          case m if m.size() == 2 && m.containsKey(ident) =>
            m.get(ident).toString

          case m if depth < maxDepth =>
            val ent = DatomicEntity_Client(
              conn,
              m.iterator().next().asInstanceOf[MapEntry].getValue
            )
            tpe match {
              case "Map"  => ent.asMap(depth + 1, maxDepth)
              case "List" => ent.asList(depth + 1, maxDepth)
            }

          case m =>
            m.iterator().next().asInstanceOf[MapEntry].getValue.asInstanceOf[Long]
        }
      }

      case vec: clojure.lang.PersistentVector =>
        sortList(
          vec.asScala.toList.map(v1 =>
            toScala(key, Some(v1), depth, maxDepth, tpe)
          )
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

      case null => throw new EntityException("Unexpectedly received null")

      case unexpected => throw new EntityException(
        "Unexpected Datalog type to convert: " + unexpected.getClass.toString)
    }
  }
}