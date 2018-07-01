package molecule.entity

import java.util.{Date, UUID, Collection => jCollection}
import molecule.action.{MoleculeOutBase, TxReport}
import molecule.ast.model.{Model, TxMetaData}
import molecule.ast.transaction.RetractEntity
import molecule.facade.Conn
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.collection.JavaConverters._
import scala.language.{existentials, higherKinds}

case class EntityFacade(entity: datomic.Entity, conn: Conn, id: Object) {
  private val x = Debug("EntityFacade", 1, 99, false, 3)


  // Retract entity ...............................................................................
  // To retract with tx meta data, use retract(eid, txMetaMolecule) in Datomic

  def retractTx = Seq(Seq(RetractEntity(id)))
  def retract: TxReport = conn.transact(retractTx)

  def retractD {x(1, retractTx)}

  // Retract entity with tx meta data .............................................................

  def tx(metaMolecule: MoleculeOutBase) = RetractMolecule(metaMolecule)

  case class RetractMolecule(txMeta: MoleculeOutBase) {
    val retractStmts = Seq(RetractEntity(id))

    val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    val stmtss = Seq(retractStmts ++ txMetaStmts)

    // Retract
    def retract: TxReport = conn.transact(stmtss)

    // Debug retract - for semantic consistency
    def retractD: Unit = x(2, stmtss)
  }


  // Touch - traverse entity attributes ...........................................................

  // Default to Map - useful for lookup
  def touch: Map[String, Any] = asMap()
  def touch(maxDepth: Int = 5): Map[String, Any] = asMap(1, maxDepth)

  // Touch quoted for quoted output that can be pasted into tests
  def touchQ: String = touchQ()
  def touchQ(maxDepth: Int = 5): String = asMap(1, maxDepth).map(p => s""""${p._1}" -> ${formatEntity(p._2)}""").mkString("Map(\n  ", ",\n  ", "\n)")

  // Lists keep order - useful for tests.
  def touchList: List[(String, Any)] = asList(1, 5)
  // Todo: avoid implicit apply method of scala.collection.LinearSeqOptimized
  def touchList(maxDepth: Int = 5, overloadHack: Int = 42): List[(String, Any)] = asList(1, maxDepth)

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

  private def asMap(depth: Int = 1, maxDepth: Int = 5): Map[String, Any] = {
    val builder = Map.newBuilder[String, Any]
    val iter = entity.keySet.asScala.toList.sorted.asJava.iterator()

    // Add id also
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key = iter.next()
      val scalaValue = toScala(entity.get(key), depth, maxDepth)
      val sortedValue = scalaValue match {
        case l: Seq[_] => l.head match {
          case m1: Map[_, _] if m1.asInstanceOf[Map[String, Any]].isDefinedAt(":db/id") =>
            val indexedRefMaps: Seq[(Long, Map[String, Any])] = l.map {
              case m2: Map[_, _] => m2.asInstanceOf[Map[String, Any]].apply(":db/id").asInstanceOf[Long] -> m2.asInstanceOf[Map[String, Any]]
            }
            indexedRefMaps.sortBy(_._1).map(_._2)
          case _                                                                        => l
        }
        case other     => other
      }
      builder += (key -> sortedValue)
    }
    builder.result()
  }

  private def asList(depth: Int = 1, maxDepth: Int = 5): List[(String, Any)] = {
    val builder = List.newBuilder[(String, Any)]
    val iter = entity.keySet.asScala.toList.sorted.asJava.iterator()

    // Add id first
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key = iter.next()
      val scalaValue = toScala(entity.get(key), depth, maxDepth, "List")
      val sortedValue = scalaValue match {
        case l: Seq[_] => l.head match {
          case l0: Seq[_] => l0.head match {
            case pair: (_, _) => // Now we now we have a Seq of Seq with pairs
              // Make typed Seq
              val typedSeq: Seq[Seq[(String, Any)]] = l.collect {
                case l1: Seq[_] => l1.collect {
                  case (k: String, v) => (k, v)
                }
              }
              if (typedSeq.head.map(_._1).contains(":db/id")) {
                // We now know we have :db/id's to sort on
                val indexedRefLists: Seq[(Long, Seq[(String, Any)])] = typedSeq.map {
                  subSeq => subSeq.toMap.apply(":db/id").asInstanceOf[Long] -> subSeq
                }
                // Sort sub Seq's by :db/id
                indexedRefLists.sortBy(_._1).map(_._2)
              } else {
                typedSeq
              }
          }
          case _          => l
        }
        case other     => other
      }
      builder += (key -> sortedValue)
    }
    builder.result()
  }


  // Entity api from string (typed) .................................................................

  def apply[T](kw: String): Option[T] = entity.get(kw) match {
    case null                                    => Option.empty[T]
    case results: clojure.lang.PersistentHashSet => results.asScala.head match {
      case ent: datomic.Entity => Some(results.asScala.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted.asInstanceOf[T])
      case manyValue           => Some(results.asScala.toList.map(toScala(_)).toSet.asInstanceOf[T])
    }
    case result                                  => Some(toScala(result).asInstanceOf[T])
  }

  // untyped

  def apply(kw1: String, kw2: String, kwx: String*): Seq[Option[Any]] = {
    (kw1 +: kw2 +: kwx.toList) map apply[Any]
  }


  // Conversions ..........................................................................

  private[molecule] def toScala(v: Any, depth: Int = 1, maxDepth: Int = 5, tpe: String = "Map"): Any = v match {
    case s: java.lang.String /* :db.type/string */       => s
    case b: java.lang.Boolean /* :db.type/boolean */     => b: Boolean
    case l: java.lang.Long /* :db.type/long */           => l: Long
    case i: java.lang.Integer /* attribute id */         => i.toLong: Long
    case f: java.lang.Float /* :db.type/float */         => f: Float
    case d: java.lang.Double /* :db.type/double */       => d: Double
    case bi: java.math.BigInteger /* :db.type/bigint */  => BigInt(bi)
    case bd: java.math.BigDecimal /* :db.type/bigdec */  => BigDecimal(bd)
    case d: Date /* :db.type/instant */                  => d
    case u: UUID /* :db.type/uuid */                     => u
    case u: java.net.URI /* :db.type/uri */              => u
    case kw: clojure.lang.Keyword /* :db.type/keyword */ => kw.toString // Clojure Keywords not used in Molecule
    case bytes: Array[Byte] /* :db.type/bytes */         => bytes

    // an entity map
    case e: datomic.Entity if depth < maxDepth && tpe == "Map"  => EntityFacade(e, conn, e.get(":db/id")).asMap(depth + 1, maxDepth)
    case e: datomic.Entity if depth < maxDepth && tpe == "List" => EntityFacade(e, conn, e.get(":db/id")).asList(depth + 1, maxDepth)
    case e: datomic.Entity                                      => e.get(":db/id").asInstanceOf[Long]

    // :db.type/keyword
    case set: clojure.lang.PersistentHashSet if depth < maxDepth => set.asScala.toList.map(toScala(_, depth, maxDepth, tpe))
    case set: clojure.lang.PersistentHashSet                     => set.asScala.toList.map(toScala(_, depth, maxDepth, tpe).asInstanceOf[Long]).toSet

    // a collection
    case coll: jCollection[_] =>
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

    case unexpected => throw new RuntimeException("[EntityFacade:Convert:toScala] Unexpected Datalog type to convert: " + unexpected.getClass.toString)
  }
}
