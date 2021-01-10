package molecule.datomic.base.api

import java.net.URI
import java.util.{Date, UUID}
import clojure.lang.{Keyword, PersistentArrayMap}
import molecule.core.ast.MoleculeBase
import molecule.core.ast.elements.{Model, TxMetaData}
import molecule.core.ast.transactionModel.RetractEntity
import molecule.core.ops.VerifyModel
import molecule.core.transform.Model2Transaction
import molecule.core.util.{Inspect, Quoted}
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.language.existentials


/** Entity wrapper with actions on entity.
  *
  * @groupname retract Entity retraction
  * @groupprio retract 1
  * @groupname tx Entity retraction with transaction meta data
  * @groupprio tx 2
  * @groupname entityApi Traverse entity graph
  * @groupprio entityApi 3
  * @groupname touch Touch entity graph
  * @groupprio touch 3
  * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] in scope
  * @param eid    Entity id of type Object
  */
abstract class DatomicEntity(conn: Conn, eid: Any) extends Quoted {

  // Get ================================================================

  def keySet: Set[String]
  def keys: List[String]

  def rawValue(key: String): Any

  /** Get typed attribute value of entity.
    * <br><br>
    * Apply namespaced attribute name with a type parameter to return an optional typed value.
    * <br><br>
    * Note how referenced entities are returned as a Map so that we can continue traverse the entity graph.
    * {{{
    *   val List(benId, benAddressId) = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eids
    *
    *   // Level 1
    *   benId[String](":Person/name") === Some("Ben")
    *   benId[Int](":Person/age") === Some(42)
    *
    *   // Level 2
    *   val refMap = benId[Map[String, Any]](":Person/address").getOrElse(Map.empty[String, Any])
    *   benAddressId[String](":Address/street") === Some("Hollywood Rd")
    *
    *   // Non-asserted or non-existing attribute returns None
    *   benId[Int](":Person/non-existing-attribute") === None
    *   benId[Int](":Person/existing-but-non-asserted-attribute") === None
    * }}}
    *
    * @group entityApi
    * @param key Attribute: ":ns/attr"
    * @tparam T Type of attribute
    * @return Optional typed attribute value
    */
  def apply[T](key: String): Option[T] = {
    try {
      val rawV = rawValue(key)
      rawV match {
        case None    => None
        case Some(v) => Some(v.asInstanceOf[T])
        case null    => Option.empty[T]

        case results: clojure.lang.PersistentHashSet =>
          results.asScala.head match {
            case _: datomic.Entity =>
              Some(results.asScala.toList
                .map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long])
                .sorted.asInstanceOf[T])

            case _ =>
              Some(results.asScala.toList.map(v1 =>
                toScala(key, Some(v1))).asInstanceOf[T]
              )
          }

        case result =>
          Some(toScala(key, Some(result)).asInstanceOf[T])
      }
    } catch {
      case _: NoSuchElementException => Option.empty[T]
    }
  }


  /** Get List of two or more unchecked/untyped attribute values of entity.
    * <br><br>
    * Apply two or more namespaced attribute names to return a List of unchecked/untyped optional attribute values.
    * <br><br>
    * Referenced entities can be cast to an Option[Map[String, Any]].
    * {{{
    *   val List(benId, benAddressId) = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eids
    *
    *   // Type ascription is still unchecked since it is eliminated by erasure
    *   val List(
    *     optName: Option[String],
    *     optAge: Option[Int],
    *     optAddress: Option[Map[String, Any]]
    *   ) = benId(
    *     ":Person/name",
    *     ":Person/age",
    *     ":Person/address"
    *   )
    *
    *   val name: String = optName.getOrElse("no name")
    *
    *   // Type casting necessary to get right value type from Map[String, Any]
    *   val address: Map[String, Any] = optAddress.getOrElse(Map.empty[String, Any])
    *   val street: String = address.getOrElse(":Address/street", "no street").asInstanceOf[String]
    *
    *   name === "Ben"
    *   street === "Hollywood Rd"
    * }}}
    * Typed apply is likely more convenient if typed values are required.
    *
    * @group entityApi
    * @param kw1 First namespaced attribute name: ":[namespace with lowercase first letter]/[attribute name]"
    * @param kw2 Second namespaced attribute name
    * @param kws Further namespaced attribute names
    * @return List of optional unchecked/untyped attribute values
    */
  def apply(kw1: String, kw2: String, kws: String*): List[Option[Any]] = {
    (kw1 +: kw2 +: kws.toList) map apply[Any]
  }


  // Retract =========================================================

  /** Retract single entity using entity id.
    * <br><br>
    * Given the implicit conversion of Long's in [[molecule.datomic.base.api.EntityOps]] to an [[molecule.datomic.base.api.DatomicEntity Entity]] we can
    * can call `retract` on an entity id directly:
    * {{{
    *   // Get entity id of Ben
    *   val benId = Person.e.name_("Ben").get.head
    *
    *   // Retract Ben entity
    *   benId.retract
    * }}}
    *
    * To retract single entity id with tx meta data, use<br>
    * `eid.Tx(MyMetaData.action("my meta data")).retract`
    * <br><br>
    * To retract multiple entities (with or without tx meta data), use<br>
    * `retract(eids, txMetaDataMolecules*)` in [[molecule.datomic.base.api.EntityOps]].
    *
    * @group retract
    * @return [[molecule.datomic.base.facade.TxReport]] with result of retraction
    */
  def retract: TxReport = conn.transact(getRetractTx)

  /** Asynchronously retract single entity using entity id.
    * <br><br>
    * Given the implicit conversion of Long's in [[molecule.datomic.base.api.EntityOps]] to an [[molecule.datomic.base.api.DatomicEntity Entity]] we can
    * can call `retractAsync` on an entity id directly:
    * {{{
    *   // Get entity id of Ben
    *   val benId = Person.e.name_("Ben").get.head
    *
    *   // Retract Ben entity asynchronously
    *   benId.retractAsync.map { tx =>
    *     // ..ben was retracted
    *   }
    * }}}
    *
    * To retract single entity id with tx meta data, use<br>
    * `eid.Tx(MyMetaData.action("my meta data")).retract`
    * <br><br>
    * To retract multiple entities (with or without tx meta data), use<br>
    * `retract(eids, txMetaDataMolecules*)` in [[molecule.datomic.base.api.EntityOps]].
    *
    * @group retract
    * @return [[molecule.datomic.base.facade.TxReport]] with result of retraction
    */
  def retractAsync(implicit ec: ExecutionContext): Future[TxReport] =
    conn.transactAsync(getRetractTx)

  /** Get entity retraction transaction data without affecting the database.
    * <br><br>
    * Call `getRetractTx` to retrieve the generated transaction data of the method `retract` on an entity
    * {{{
    *   // Get entity id of Ben
    *   val benId = Person.e.name_("Ben").get.head
    *
    *   // Retraction transaction data
    *   benId.getRetractTx === List(List(RetractEntity(17592186045445)))
    * }}}
    *
    * @group retract
    * @return List[List[Retractentity[Long]]]
    * */
  def getRetractTx: List[List[RetractEntity]] = List(List(RetractEntity(eid)))

  /** Inspect entity transaction data of method `retract` without affecting the database.
    * {{{
    *   // Inspect retraction of an entity
    *   eid.inspectRetract
    * }}}
    * This will print generated Datomic transaction statements in a readable format to output:
    * {{{
    *   ## 1 ## Inspect `retract` on entity
    *   ========================================================================
    *   1          List(
    *     1          List(
    *       1          :db/retractEntity   17592186045445))
    *   ========================================================================
    * }}}
    *
    * @group retract
    */
  def inspectRetract: Unit = Inspect("Inspect `retract` on entity", 1)(1, getRetractTx)

  /** Entity retraction transaction meta data constructor.
    * <br><br>
    * Build on from entity with `Tx` and apply a transaction meta data molecule to
    * save transaction meta data with retraction of the entity.
    * {{{
    *   val benId = Person.name("Ben").Tx(MyMetaData.action("add member")).save.eid
    *
    *   // Retract entity with tx meta data
    *   benId.Tx(MyMetaData.action("moved away")).retract
    *
    *   // Query for Ben's history and why he was retracted
    *   Person(benId).name.t.op.Tx(MyMetaData.action).getHistory === List(
    *     ("Ben", 1028, true, "add member"), // Ben added as member
    *     ("Ben", 1030, false, "moved away") // Ben retracted since he moved away
    *   )
    * }}}
    *
    * @group tx
    * @param metaMolecule Transaction meta data molecule
    * @return [[RetractMolecule RetractMolecule]] - a simple wrapper for adding retraction tx meta data
    */
  def Tx(txMeta: MoleculeBase): RetractMolecule = RetractMolecule(txMeta)

  /** Wrapper to add retract methods on entity with transaction meta data.
    * <br><br>
    * [[RetractMolecule RetractMolecule]] is created from calling `Tx`:
    * {{{
    *   val benId = Person.name("Ben").Tx(MyMetaData.action("add member")).save.eid
    *
    *   // Retract entity with tx meta data
    *   benId.Tx(MyMetaData.action("moved away")).retract
    *
    *   // Query for Ben's history and why he was retracted
    *   Person(benId).name.t.op.Tx(MyMetaData.action).getHistory === List(
    *     ("Ben", 1028, true, "add member"), // Ben added as member
    *     ("Ben", 1030, false, "moved away") // Ben retracted since he moved away
    *   )
    * }}}
    *
    * @group tx
    * @param txMeta A molecule with transaction meta data to be saved with entity retraction
    */
  case class RetractMolecule(txMeta: MoleculeBase) {
    private val retractStmts = Seq(RetractEntity(eid))

    private val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    private val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    private val stmtss = Seq(retractStmts ++ txMetaStmts)

    /** Perform retraction of entity with added transaction meta data against database.
      *
      * @return [[molecule.datomic.base.facade.TxReport TxReport]] with result of transaction
      */
    def retract: TxReport = conn.transact(stmtss)

    /** Perform asynchronous retraction of entity with added transaction meta data against database.
      *
      * @return Future[molecule.facade.TxReport] with result of transaction
      */
    def retractAsync(implicit ec: ExecutionContext): Future[TxReport] =
      conn.transactAsync(stmtss)

    /** Inspect entity retraction with transaction meta data.
      * {{{
      *   eid.Tx(MyMetaData.action("moved away")).inspectRetract
      * }}}
      * This will print generated Datomic transaction statements in a readable format to output:
      * {{{
      *   ## 1 ## Inspect `retract` on entity with tx meta data
      *   ========================================================================
      *   1          List(
      *     1          List(
      *       1          :db/retractEntity   17592186045445
      *       2          :db/add   #db/id[:db.part/tx -1000100]  :myMetaData/action   moved away   Card(1)))
      *   ========================================================================
      * }}}
      */
    def inspectRetract: Unit =
      Inspect("Inspect `retract` on entity with tx meta data", 1)(1, stmtss)
  }

  // Touch - traverse entity attributes ========================================

  /** Get entity graph as Map.
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   benId.touch === Map(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> Map(
    *       ":db/id" -> 17592186045446L,
    *       ":Address/street" -> "Hollywood Rd"),
    *     ":Person/name" -> "Ben"
    *   )
    * }}}
    *
    * @group touch
    * @return Map[key: String, value: Any] where value can be a primitive or another nested Map of the entity graph
    */
  def touch: Map[String, Any] = asMap(1, 5)

  /** Get entity graph to some depth as Map.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   // 2 levels returned
    *   benId.touchMax(2) === Map(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> Map(
    *       ":db/id" -> 17592186045446L,
    *       ":Address/street" -> "Hollywood Rd"),
    *     ":Person/name" -> "Ben"
    *   )
    *
    *   // 1 level returned
    *   benId.touchMax(1) === Map(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> 17592186045446L // Only reference returned
    *     ":Person/name" -> "Ben"
    *   )
    * }}}
    *
    * @group touch
    * @return Map[key: String, value: Any] where value can be a primitive or another nested Map of the entity graph
    */
  def touchMax(maxDepth: Int): Map[String, Any] = asMap(1, maxDepth)

  /** Get entity graph as Map-string (for presentation).
    * <br><br>
    * To show the entity graph, this method quotes all text strings so that you can paste the whole graph
    * into any presentation. Pasting it into test code is less useful, since the order of key/value pairs in
    * a Map is not guaranteed. In that case, `touchListQuoted` is recommended since a List guarantees order.
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   benId.touchQuoted ===
    *     """Map(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> Map(
    *       |    ":db/id" -> 17592186045446L,
    *       |    ":Address/street" -> "Hollywood Rd"),
    *       |  ":Person/name" -> "Ben")""".stripMargin
    * }}}
    *
    * @group touch
    * @return String
    */
  def touchQuoted: String = quote(asMap(1, 5))

  /** Get entity graph to some depth as Map-string (for presentation).
    * <br><br>
    * To show the entity graph, this method quotes all text strings so that you can paste the whole graph
    * into any presentation. Pasting it into test code is less useful, since the order of key/value pairs in
    * a Map is not guaranteed. In that case, `touchListQuoted(maxLevel)` is recommended since a List guarantees order.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   // 2 levels (in this case all levels) returned
    *   benId.touchQuotedMax(2) ===
    *     """Map(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> Map(
    *       |    ":db/id" -> 17592186045446L,
    *       |    ":Address/street" -> "Hollywood Rd"),
    *       |  ":Person/name" -> "Ben")""".stripMargin
    *
    *   // 1 level returned
    *   // Note that only reference to Address entity on level 2 is returned
    *   benId.touchQuotedMax(1) ===
    *     """Map(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> 17592186045446L,
    *       |  ":Person/name" -> "Ben")""".stripMargin
    * }}}
    *
    * @group touch
    * @return String
    */
  def touchQuotedMax(maxDepth: Int): String = quote(asMap(1, maxDepth))


  /** Get entity graph as List.
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   benId.touchList === List(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> List(
    *       ":db/id" -> 17592186045446L,
    *       ":Address/street" -> "Hollywood Rd"),
    *     ":Person/name" -> "Ben"
    *   )
    * }}}
    *
    * @group touch
    * @return List[(key: String, value: Any)] where value can be a primitive or another nested List of the entity graph
    */
  def touchList: List[(String, Any)] = asList(1, 5)

  /** Get entity graph to some depth as List.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   // 2 levels returned
    *   benId.touchListMax(2) === List(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> List(
    *       ":db/id" -> 17592186045446L,
    *       ":Address/street" -> "Hollywood Rd"),
    *     ":Person/name" -> "Ben"
    *   )
    *
    *   // 1 level returned
    *   benId.touchListMax(1) === List(
    *     ":db/id" -> 17592186045445L,
    *     ":Person/age" -> 42,
    *     ":Person/address" -> 17592186045446L // Only reference returned
    *     ":Person/name" -> "Ben"
    *   )
    * }}}
    *
    * @todo remove overload hack (by avoiding implicit apply method of scala.collection.LinearSeqOptimized ?)
    * @group touch
    * @return List[(key: String, value: Any)] where value can be a primitive or another nested Map of the entity graph
    */
  def touchListMax(maxDepth: Int): List[(String, Any)] = asList(1, maxDepth)

  /** Get entity graph as List-string (for tests).
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   benId.touchListQuoted ===
    *     """List(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> List(
    *       |    ":db/id" -> 17592186045446L,
    *       |    ":Address/street" -> "Hollywood Rd"),
    *       |  ":Person/name" -> "Ben")""",stripMargin
    * }}}
    *
    * @group touch
    * @return String
    */
  def touchListQuoted: String = quote(asList(1, 5))

  /** Get entity graph to some depth as List-string (for tests).
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    *    the value is a Map itself of the referenced entity attributes, etc.
    *
    * {{{
    *   val benId = Person.name.age.Address.street.insert("Ben", 42, "Hollywood Rd").eid
    *
    *   // 2 levels (in this case all levels) returned
    *   benId.touchListQuotedMax(2) ===
    *     """List(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> List(
    *       |    ":db/id" -> 17592186045446L,
    *       |    ":Address/street" -> "Hollywood Rd"),
    *       |  ":Person/name" -> "Ben")""",stripMargin
    *
    *   // 1 level returned
    *   // Note that only reference to Address entity on level 2 is returned
    *   benId.touchListQuotedMax(1) ===
    *     """List(
    *       |  ":db/id" -> 17592186045445L,
    *       |  ":Person/age" -> 42,
    *       |  ":Person/address" -> 17592186045446L,
    *       |  ":Person/name" -> "Ben")""",stripMargin
    * }}}
    *
    * @group touch
    * @return String
    */
  def touchListQuotedMax(maxDepth: Int): String = quote(asList(1, maxDepth))


  private[molecule] def toScala(
    key: String,
    vOpt: Option[Any],
    depth: Int = 1,
    maxDepth: Int = 5,
    tpe: String = "Map"
  ): Any


  lazy protected val ident = Keyword.intern("db", "ident")

  protected def asMap(depth: Int, maxDepth: Int): Map[String, Any] = {
    val builder    = Map.newBuilder[String, Any]
    val keysSorted = keys.sortWith((x, y) => x.toLowerCase < y.toLowerCase)
    if (keysSorted.head != ":db/id")
      builder += ":db/id" -> rawValue(":db/id")
    keysSorted.foreach { key =>
      val scalaValue  = toScala(key, None, depth, maxDepth)
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


  protected def asList(depth: Int, maxDepth: Int): List[(String, Any)] = {
    val builder    = List.newBuilder[(String, Any)]
    val keys2      = keys
    val keysSorted = keys2.sortWith((x, y) => x.toLowerCase < y.toLowerCase)
    if (keysSorted.head != ":db/id")
      builder += ":db/id" -> rawValue(":db/id")
    keysSorted.foreach { key =>
      val scalaValue  = toScala(key, None, depth, maxDepth, "List")
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

  def sortList(l: List[Any]): List[Any] = l.head match {
    case _: String               => l.asInstanceOf[List[String]].sorted
    case _: Long                 => l.asInstanceOf[List[Long]].sorted
    case _: Float                => l.asInstanceOf[List[Float]].sorted
    case _: Double               => l.asInstanceOf[List[Double]].sorted
    case _: Boolean              => l.asInstanceOf[List[Boolean]].sorted
    case _: Date                 => l.asInstanceOf[List[Date]].sorted
    case _: UUID                 => l.asInstanceOf[List[UUID]].sorted
    case _: URI                  => l.asInstanceOf[List[URI]].sorted
    case _: java.math.BigInteger => l.asInstanceOf[List[java.math.BigInteger]].map(BigInt(_)).sorted
    case _: java.math.BigDecimal => l.asInstanceOf[List[java.math.BigDecimal]].map(BigDecimal(_)).sorted
    case _: BigInt               => l.asInstanceOf[List[BigInt]].sorted
    case _: BigDecimal           => l.asInstanceOf[List[BigDecimal]].sorted

    case m: PersistentArrayMap if m.containsKey(ident) =>
      l.asInstanceOf[List[PersistentArrayMap]].map(pam =>
        pam.get(ident).toString
      ).sorted

    case _ => l
  }
}