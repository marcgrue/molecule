package molecule.datomic.peer.facade


import java.util.{Date, UUID, Collection => jCollection}
import molecule.core.api.exception.EntityException
import molecule.core.api.Entity
import molecule.core.ast.MoleculeBase
import molecule.core.ast.model.{Model, TxMetaData}
import molecule.core.ast.transactionModel.RetractEntity
import molecule.core.ops.VerifyModel
import molecule.core.transform.Model2Transaction
import molecule.core.util.{DateHandling, Debug}
import molecule.core.util.fns.date2str
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.{blocking, ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.language.existentials


case class Entity_Peer(
  entity: datomic.Entity,
  conn: Conn,
  id: Object,
  showKW: Boolean = true
) extends Entity {

  // Entity retraction =========================================================

  def retract: TxReport = conn.transact(getRetractTx)

  def retractAsync(implicit ec: ExecutionContext): Future[TxReport] =
    conn.transactAsync(getRetractTx)


  def getRetractTx: List[List[RetractEntity]] = List(List(RetractEntity(id)))

  def debugRetract: Unit = Debug("Debug `retract` on entity", 1)(1, getRetractTx)


  // Entity retraction with tx meta data =======================================

  def Tx(metaMolecule: MoleculeBase): RetractMolecule =
    RetractMolecule_Peer(metaMolecule)

  case class RetractMolecule_Peer(txMeta: MoleculeBase) extends RetractMolecule {
    private val retractStmts = Seq(RetractEntity(id))

    private val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    private val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    private val stmtss = Seq(retractStmts ++ txMetaStmts)

    def retract: TxReport = conn.transact(stmtss)

    def retractAsync(implicit ec: ExecutionContext): Future[TxReport] =
      conn.transactAsync(stmtss)

    def debugRetract: Unit =
      Debug("Debug `retract` on entity with tx meta data", 1)(1, stmtss)
  }


  // Entity api ================================================================

  def apply[T](kw: String): Option[T] = entity.get(kw) match {
    case null => Option.empty[T]

    case results: clojure.lang.PersistentHashSet => results.asScala.head match {
      case _: datomic.Entity =>
        Some(results.asScala.toList
          .map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long])
          .sorted.asInstanceOf[T])

      case _ => Some(results.asScala.toList.map(toScala(_)).toSet.asInstanceOf[T])
    }

    case result => Some(toScala(result).asInstanceOf[T])
  }

  def apply(kw1: String, kw2: String, kws: String*): List[Option[Any]] = {
    (kw1 +: kw2 +: kws.toList) map apply[Any]
  }


  // Touch - traverse entity attributes ........................................

  def touch: Map[String, Any] = asMap(1, 5)

  def touchMax(maxDepth: Int): Map[String, Any] = asMap(1, maxDepth)

  def touchQuoted: String = format(asMap(1, 5))

  def touchQuotedMax(maxDepth: Int): String = format(asMap(1, maxDepth))

  def touchList: List[(String, Any)] = asList(1, 5)

  def touchListMax(maxDepth: Int): List[(String, Any)] = asList(1, maxDepth)

  def touchListQuoted: String = format(asList(1, 5))

  def touchListQuotedMax(maxDepth: Int): String = format(asList(1, maxDepth))


  // Private helper methods ....................................................

  private def format(value: Any): String = {
    val sb = new StringBuilder
    def traverse(value: Any, tabs: Int): Unit = {
      val t = "  " * tabs
      var i = 0
      value match {
        case s: String                => sb.append(s""""$s"""")
        case l: Long                  =>
          if (l > Int.MaxValue) sb.append(s"${l}L") else sb.append(l) // Int/Long hack
        case d: Double                => sb.append(d)
        case f: Float                 => sb.append(f)
        case bi: java.math.BigInteger => sb.append(bi)
        case bd: java.math.BigDecimal => sb.append(bd)
        case b: Boolean               => sb.append(b)
        case d: Date                  => sb.append(s""""${date2str(d)}"""")
        case u: UUID                  => sb.append(s""""$u"""")
        case u: java.net.URI          => sb.append(s""""$u"""")
        case s: Set[_]                =>
          sb.append("Set(")
          s.foreach { v =>
            if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")
        case l: Seq[_]                =>
          sb.append("List(")
          l.foreach {
            case (k, v) =>
              if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
              sb.append(s""""$k" -> """)
              traverse(v, tabs + 1)
              i += 1
            case v      =>
              if (i > 0) sb.append(s", ")
              traverse(v, tabs) // no line break
              i += 1
          }
          sb.append(")")
        case m: Map[_, _]             =>
          sb.append("Map(")
          m.foreach { case (k, v) =>
            if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
            sb.append(s""""$k" -> """)
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")
        case (k: String, v: Any)      =>
          sb.append(s""""$k" -> """)
          traverse(v, tabs)
        case other                    =>
          throw new EntityException(
            "Unexpected element traversed in Entity#format: " + other)
      }
    }
    traverse(value, 1)
    sb.result()
  }

  private def asMap(depth: Int, maxDepth: Int): Map[String, Any] = {
    val builder = Map.newBuilder[String, Any]
    val iter    = blocking {
      entity.keySet
    }.asScala.toList.sorted.asJava.iterator()

    // Add id also
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key         = iter.next()
      val scalaValue  = toScala(entity.get(key), depth, maxDepth)
      val sortedValue = scalaValue match {
        case l: Seq[_] => l.head match {
          case m1: Map[_, _]
            if m1.asInstanceOf[Map[String, Any]].isDefinedAt(":db/id") =>
            val indexedRefMaps: Seq[(Long, Map[String, Any])] = l.map {
              case m2: Map[_, _] =>
                m2.asInstanceOf[Map[String, Any]]
                  .apply(":db/id").asInstanceOf[Long] ->
                  m2.asInstanceOf[Map[String, Any]]
            }
            indexedRefMaps.sortBy(_._1).map(_._2)

          case _ => l
        }
        case other     => other
      }
      builder += (key -> sortedValue)
    }
    builder.result()
  }

  private def asList(depth: Int, maxDepth: Int): List[(String, Any)] = {
    val builder = List.newBuilder[(String, Any)]
    val iter    = blocking {
      entity.keySet
    }.asScala.toList.sorted.asJava.iterator()

    // Add id first
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key         = iter.next()
      val rawValue    = entity.get(key)
      val scalaValue  = toScala(rawValue, depth, maxDepth, "List")
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

  private[molecule] def toScala(
    v: Any,
    depth: Int = 1,
    maxDepth: Int = 5,
    tpe: String = "Map"
  ): Any = v match {
    case s: java.lang.String /* :db.type/string */                 => s
    case i: java.lang.Integer /* attribute id */                   => i.toLong: Long
    case l: java.lang.Long /* :db.type/long */                     => l: Long
    case f: java.lang.Float /* :db.type/float */                   => f: Float
    case d: java.lang.Double /* :db.type/double */                 => d: Double
    case b: java.lang.Boolean /* :db.type/boolean */               => b: Boolean
    case d: Date /* :db.type/instant */                            => d
    case u: UUID /* :db.type/uuid */                               => u
    case u: java.net.URI /* :db.type/uri */                        => u
    case bi: java.math.BigInteger /* :db.type/bigint */            => BigInt(bi)
    case bd: java.math.BigDecimal /* :db.type/bigdec */            => BigDecimal(bd)
    case bytes: Array[Byte] /* :db.type/bytes */                   => bytes
    case kw: clojure.lang.Keyword if showKW /* :db.type/keyword */ => kw.toString // Clojure Keyword String

    case kw: clojure.lang.Keyword /* :db.type/keyword */           => conn.db.entity(kw).get(":db/id") // Clojure Keyword as Long

    case e: datomic.Entity if depth < maxDepth && tpe == "Map"     => Entity(e, conn, e.get(":db/id")).asMap(depth + 1, maxDepth)
    case e: datomic.Entity if depth < maxDepth && tpe == "List"    => Entity(e, conn, e.get(":db/id")).asList(depth + 1, maxDepth)
    case e: datomic.Entity                                         => e.get(":db/id").asInstanceOf[Long]
    case set: clojure.lang.PersistentHashSet                       => set.asScala.toList.map(toScala(_, depth, maxDepth, tpe))
    case coll: jCollection[_]                                      =>
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

    case unexpected => throw new EntityException(
      "Unexpected Datalog type to convert: " + unexpected.getClass.toString)
  }
}