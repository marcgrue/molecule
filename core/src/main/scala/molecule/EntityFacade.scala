package molecule
import java.util.{Date, UUID, Collection => jCollection, List => jList, Map => jMap}

import datomic._
import molecule.util.Debug

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.language.{existentials, higherKinds}

case class EntityFacade(entity: datomic.Entity, conn: Connection, id: Object) {
  private val x = Debug("EntityFacade", 1, 99, false, 3)


  // Retraction ......................................................................

  def retract = conn.transact(Util.list(Util.list(":db.fn/retractEntity", id))).get()


  // Touch - traverse entity attributes ...........................................................

  // Default to Map - useful for lookup
  def touch: Map[String, Any] = asMap()
  def touch(maxDepth: Int = 5): Map[String, Any] = asMap(1, maxDepth)

  def touchQ: String = touchQ()
  def touchQ(maxDepth: Int = 5): String = asMap(1, maxDepth).map(p => s""""${p._1}" -> ${formatEntity(p._2)}""").mkString("Map(\n  ", ",\n  ", "\n)")

  // Lists keep order - useful for tests
  def touchList: List[(String, Any)] = asList()
  def touchList(maxDepth: Int = 5): List[(String, Any)] = asList(1, maxDepth)

  // Quote output for tests...
  def touchListQ: String = touchListQ()
  def touchListQ(maxDepth: Int = 5): String = asList(1, maxDepth).map(p => s""""${p._1}" -> ${formatEntity(p._2)}""").mkString("List(\n  ", ",\n  ", "\n)")

  private def formatEntity(value: Any): Any = value match {
    case s: String               => s""""$s""""
    case l: Long                 => if (l > Int.MaxValue) s"${l}L" else l // presuming we used Int... - todo: how to get Int from touch?
    case s: Set[_]               => s map formatEntity
    case l: Seq[_]               => l map formatEntity
    case m: Map[_, _]            => "\n" + m.map(p => s""""${p._1}"""" -> formatEntity(p._2))
    case (s: String, value: Any) => s""""$s"""" -> formatEntity(value)
    case other                   => other
  }

  def asMap(depth: Int = 1, maxDepth: Int = 5): Map[String, Any] = {
    val builder = Map.newBuilder[String, Any]
    val iter = entity.keySet.toList.sorted.asJava.iterator()

    // Add id also
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key = iter.next()
      val scalaValue = toScala(entity.get(key), depth, maxDepth, "Map")
      val sortedValue = scalaValue match {
        case l: Seq[_] => l.head match {
          case m1: Map[_, _] if m1.containsKey(":db/id") =>
            val indexedRefMaps: Seq[(Long, Map[String, Any])] = l.map {
              case m2: Map[_, _] => m2.get(":db/id").asInstanceOf[Long] -> m2.asInstanceOf[Map[String, Any]]
            }
            indexedRefMaps.sortBy(_._1).map(_._2)
          case _                                         => l
        }
        case other     => other
      }
      builder += (key -> sortedValue)
    }
    builder.result()
  }

  private def asList(depth: Int = 1, maxDepth: Int = 5): List[(String, Any)] = {
    val builder = List.newBuilder[(String, Any)]
    val iter = entity.keySet.toList.sorted.asJava.iterator()

    // Add id first
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key = iter.next()
      val scalaValue = toScala(entity.get(key), depth, maxDepth, "List")
      val sortedValue = scalaValue match {
        case l: Seq[_] => l.head match {
          case m1: Map[_, _] if m1.containsKey(":db/id") =>
            val indexedRefMaps: Seq[(Long, Map[String, Any])] = l.map {
              case m2: Map[_, _] => m2.get(":db/id").asInstanceOf[Long] -> m2.asInstanceOf[Map[String, Any]]
            }
            indexedRefMaps.sortBy(_._1).map(_._2)
          case _                                         => l
        }
        case other     => other
      }
      builder += (key -> sortedValue)
    }
    builder.result()
  }


  // Entity api from ValueAttribute (typed) .................................................................

  import dsl.schemaDSL._
  type VA[Out] = ValueAttr[_, _, _, Out]
  type Op[T] = Option[T]

  // Single value
  def apply[A](attr: VA[A]): Option[A] = attr match {
    case oneInt: OneInt[_, _]     => entity.get(attr._kw) match {
      case null    => None
      case oneInt_ => Some(toScala(oneInt_).asInstanceOf[Long].toInt.asInstanceOf[A])
    }
    case oneFloat: OneFloat[_, _] => entity.get(attr._kw) match {
      case null      => None
      case oneFloat_ => Some(toScala(oneFloat_).asInstanceOf[Double].toFloat.asInstanceOf[A])
    }
    case oneOther: One[_, _, _]   => entity.get(attr._kw) match {
      case null     => None
      case oneValue => Some(toScala(oneValue).asInstanceOf[A])
    }
    case many: Many[_, _, _, _]   => entity.get(attr._kw) match {
      case null                                    => None
      case results: clojure.lang.PersistentHashSet => results.head match {
        case ent: datomic.Entity => Some(results.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted.asInstanceOf[A])
        case manySet             => Some(results.toList.map(toScala(_)).toSet.asInstanceOf[A])
      }
      case shouldWeEverGetHere_?                   => Some(toScala(shouldWeEverGetHere_?).asInstanceOf[A])
    }

    // How to treat map attributes?
    case mapped: MapAttr[_, _, _, _] => entity.get(attr._kw) match {
      case null                                    => None
      case results: clojure.lang.PersistentHashSet => results.head match {
        case ent: datomic.Entity => Some(results.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted.asInstanceOf[A])
        case manySet             => Some(results.toList.map(toScala(_)).toSet.asInstanceOf[A])
      }
      case shouldWeEverGetHere_?                   => Some(toScala(shouldWeEverGetHere_?).asInstanceOf[A])
    }

    case _ => None
  }

  // Tuples, arity 2-22
  def apply[A, B](a: VA[A], b: VA[B]): (Op[A], Op[B]) = (apply(a), apply(b))
  def apply[A, B, C](a: VA[A], b: VA[B], c: VA[C]): (Op[A], Op[B], Op[C]) = (apply(a), apply(b), apply(c))
  def apply[A, B, C, D](a: VA[A], b: VA[B], c: VA[C], d: VA[D]): (Op[A], Op[B], Op[C], Op[D]) = (apply(a), apply(b), apply(c), apply(d))
  def apply[A, B, C, D, E](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E]): (Op[A], Op[B], Op[C], Op[D], Op[E]) = (apply(a), apply(b), apply(c), apply(d), apply(e))
  def apply[A, B, C, D, E, F](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f))
  def apply[A, B, C, D, E, F, G](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g))
  def apply[A, B, C, D, E, F, G, H](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h))
  def apply[A, B, C, D, E, F, G, H, I](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i))
  def apply[A, B, C, D, E, F, G, H, I, J](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j))
  def apply[A, B, C, D, E, F, G, H, I, J, K](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q], r: VA[R]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q], Op[R]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q), apply(r))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q], r: VA[R], s: VA[S]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q], Op[R], Op[S]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q), apply(r), apply(s))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q], r: VA[R], s: VA[S], t: VA[T]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q], Op[R], Op[S], Op[T]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q), apply(r), apply(s), apply(t))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q], r: VA[R], s: VA[S], t: VA[T], u: VA[U]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q], Op[R], Op[S], Op[T], Op[U]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q), apply(r), apply(s), apply(t), apply(u))
  def apply[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](a: VA[A], b: VA[B], c: VA[C], d: VA[D], e: VA[E], f: VA[F], g: VA[G], h: VA[H], i: VA[I], j: VA[J], k: VA[K], l: VA[L], m: VA[M], n: VA[N], o: VA[O], p: VA[P], q: VA[Q], r: VA[R], s: VA[S], t: VA[T], u: VA[U], v: VA[V]): (Op[A], Op[B], Op[C], Op[D], Op[E], Op[F], Op[G], Op[H], Op[I], Op[J], Op[K], Op[L], Op[M], Op[N], Op[O], Op[P], Op[Q], Op[R], Op[S], Op[T], Op[U], Op[V]) = (apply(a), apply(b), apply(c), apply(d), apply(e), apply(f), apply(g), apply(h), apply(i), apply(j), apply(k), apply(l), apply(m), apply(n), apply(o), apply(p), apply(q), apply(r), apply(s), apply(t), apply(u), apply(v))


  // Entity api from string (typed) .................................................................

  def getTyped[T](kw: String): Option[T] = entity.get(kw) match {
    case null                                    => None
    case results: clojure.lang.PersistentHashSet => results.head match {
      case ent: datomic.Entity => Some(results.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted.asInstanceOf[T])
      case manyValue           => Some(results.toList.map(toScala(_)).toSet.asInstanceOf[T])
    }
    case result                                  => Some(toScala(result).asInstanceOf[T])
  }

  // Entity api from string (untyped) .................................................................

  def apply(kw1: String, kw2: String, kwx: String*): Seq[Option[Any]] = {
    (kw1 +: kw2 +: kwx.toList) map apply
  }

  def apply(kw: String): Option[Any] = entity.get(kw) match {
    case null                                    => None
    case results: clojure.lang.PersistentHashSet => results.head match {
      case ent: datomic.Entity => Some(results.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted)
      case manyValue           => Some(results.toList.map(toScala(_)).toSet)
    }
    case result                                  => Some(toScala(result))
  }


  // Conversions ..........................................................................

  private[molecule] def toScala(v: Any, depth: Int = 1, maxDepth: Int = 5, tpe: String = "Map"): Any = v match {
    // :db.type/string
    case s: java.lang.String => s
    // :db.type/boolean
    case b: java.lang.Boolean => b: Boolean
    // :db.type/long
    case l: java.lang.Long => l: Long
    // attribute id
    case i: java.lang.Integer => i.toLong: Long
    // :db.type/float
    case f: java.lang.Float => f: Float
    // :db.type/double
    case d: java.lang.Double => d: Double
    // :db.type/bigint
    case bi: java.math.BigInteger => BigInt(bi)
    // :db.type/bigdec
    case bd: java.math.BigDecimal => BigDecimal(bd)
    // :db.type/instant
    case d: Date => d
    // :db.type/uuid
    case u: UUID => u
    // :db.type/uri
    case u: java.net.URI => u
    // :db.type/keyword
    case kw: clojure.lang.Keyword => kw.toString // Clojure Keywords not used in Molecule

    // :db.type/bytes
    case bytes: Array[Byte] => bytes
    // an entity map
    case e: datomic.Entity if depth < maxDepth && tpe == "Map"  => new EntityFacade(e, conn, e.get(":db/id")).asMap(depth + 1, maxDepth)
    case e: datomic.Entity if depth < maxDepth && tpe == "List" => new EntityFacade(e, conn, e.get(":db/id")).asList(depth + 1, maxDepth)
    case e: datomic.Entity                                      => e.get(":db/id").asInstanceOf[Long]

    // :db.type/keyword
    case set: clojure.lang.PersistentHashSet if depth < maxDepth => set.toList.map(toScala(_, depth, maxDepth, tpe))
    case set: clojure.lang.PersistentHashSet                     => set.toList.map(toScala(_, depth, maxDepth, tpe).asInstanceOf[Long]).toSet

    // a collection
    case coll: java.util.Collection[_] =>
      new Iterable[Any] {
        override def iterator = new Iterator[Any] {
          private val jIter = coll.iterator.asInstanceOf[java.util.Iterator[AnyRef]]
          override def hasNext = jIter.hasNext
          override def next() = if (depth < maxDepth)
            toScala(jIter.next(), depth, maxDepth, tpe)
          else
            jIter.next()
        }
        override def isEmpty = coll.isEmpty
        override def size = coll.size
        override def toString = coll.toString
      }

    case unexpected => throw new RuntimeException("[DatomicFacade:Convert:toScala] Unexpected Datalog type to convert: " + unexpected.getClass.toString)
  }
}
