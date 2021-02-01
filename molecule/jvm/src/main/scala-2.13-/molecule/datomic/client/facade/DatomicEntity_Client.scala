package molecule.datomic.client.facade

import java.net.URI
import java.util.{Date, UUID, Collection => jCollection}
import clojure.lang.{MapEntry, PersistentArrayMap, PersistentVector}
import com.cognitect.transit.impl.URIImpl
import datomic.Util
import datomicClient.anomaly.Fault
import molecule.core._4_api.api.exception.EntityException
import molecule.core.util.RegexMatching
import molecule.datomic.base.api.DatomicEntity
import scala.collection.JavaConverters._
import scala.language.existentials

/** Datomic Entity facade for client api (peer-server/cloud/dev-local).
 *
 * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] in scope
 * @param eid    Entity id of type Object
 * @param showKW
 */
case class DatomicEntity_Client(
  conn: Conn_Client,
  eid: Any,
  showKW: Boolean = true
) extends DatomicEntity(conn, eid) with RegexMatching {

  private def getThisLevel(eid: Any): Map[String, Any] = {
    val res = conn.q(
      """[:find ?a2 ?v
        | :in $ ?eid
        | :where
        |   [?eid ?a ?v]
        |   [?a :db/ident ?a1]
        |   [(str ?a1) ?a2]
        | ]""".stripMargin, eid)
      .map(l => (l.head.toString, l(1)))
      .toMap + (":db/id" -> eid)
    res
  }

  lazy val map: Map[String, Any] = {
    val res = try {
      conn.db.pull("[*]", eid).asScala.toMap.map {
        case (k, v) => (k.toString, v)
      }
    } catch {
      // Fetch top level only for cyclic graphs
      case _: StackOverflowError                                    =>
        getThisLevel(eid)
      case Fault("java.lang.StackOverflowError with empty message") =>
        getThisLevel(eid)

      case e: Throwable => throw e
    }
    res
  }

  def keySet: Set[String] = map.keySet
  def keys: List[String] = map.keySet.toList

  def rawValue(key: String): Any = {
    key match {
      case r":[^/]+/_.+" =>
        // reverse lookup
        Some(
          conn.db.pull(s"[$key]", eid)
            .asInstanceOf[PersistentArrayMap].values().iterator().next()
            .asInstanceOf[PersistentVector].asScala.toList
            .map(_.asInstanceOf[PersistentArrayMap].get(Util.read(":db/id")).asInstanceOf[Long])
        )

      case k => map.apply(k)
    }
  }


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
    val value = vOpt.getOrElse(rawValue(key))
    value match {
      case Some(v)                  => v
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
      case u: URIImpl               => new URI(u.toString)
      case bi: java.math.BigInteger => BigInt(bi)
      case bi: clojure.lang.BigInt  => BigInt(bi.toString)
      case bd: java.math.BigDecimal => BigDecimal(bd)
      case bytes: Array[Byte]       => bytes

      case kw: clojure.lang.Keyword if showKW =>
        kw.toString

      case kw: clojure.lang.Keyword =>
        conn.db.entity(conn, kw).rawValue(":db/id")

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
              case "Map" =>
                val map = ent.asMap(depth + 1, maxDepth)
                if (map.contains(":db/ident"))
                  map(":db/ident") // enum
                else
                  map

              case "List" => ent.asList(depth + 1, maxDepth) match {
                case List((":db/id", _), (":db/ident", enum)) => enum
                case other                                    => other
              }
            }

          case m =>
            val id = m.iterator().next().asInstanceOf[MapEntry].getValue.asInstanceOf[Long]
            conn.db.entity(conn, id).apply(":db/ident").getOrElse(id)
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

      case None => throw new EntityException("Unexpectedly received null")
      case null => throw new EntityException("Unexpectedly received null")

      case unexpected => throw new EntityException(
        "Unexpected Datalog type to convert: " + unexpected.getClass.toString)
    }
  }
}