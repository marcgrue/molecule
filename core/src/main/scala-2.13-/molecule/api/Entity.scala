package molecule.api

import java.util.{Date, UUID, Collection => jCollection}
import molecule.api.exception.EntityException
import molecule.ast.MoleculeBase
import molecule.ast.model.{Model, TxMetaData}
import molecule.ast.transactionModel.RetractEntity
import molecule.facade.{Conn, TxReport}
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future, blocking}
import scala.language.{existentials, higherKinds}


/** Entity wrapper with actions on entity.
  *
  * @see [[http://www.scalamolecule.org/manual/entities/ Manual]]
  *      | Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/runtime/EntityAPI.scala#L1 entity api]]
  * @groupname retract Entity retraction
  * @groupprio retract 1
  * @groupname tx Entity retraction with transaction meta data
  * @groupprio tx 2
  * @groupname entityApi Traverse entity graph
  * @groupprio entityApi 3
  * @groupname touch Touch entity graph
  * @groupprio touch 3
  * @param entity datomic.Entity
  * @param conn   Implicit [[molecule.facade.Conn Conn]] in scope
  * @param id     Entity id of type Object
  */
class Entity(entity: datomic.Entity, conn: Conn, id: Object) {

  // Entity retraction =======================================================================

  /** Retract single entity using entity id.
    * <br><br>
    * Given the implicit conversion of Long's in [[molecule.api.EntityOps]] to an [[molecule.api.Entity Entity]] we can
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
    * `retract(eids, txMetaDataMolecules*)` in [[molecule.api.EntityOps]].
    *
    * @group retract
    * @return [[molecule.facade.TxReport]] with result of retraction
    */
  def retract: TxReport = conn.transact(getRetractTx)


  /** Asynchronously retract single entity using entity id.
    * <br><br>
    * Given the implicit conversion of Long's in [[molecule.api.EntityOps]] to an [[molecule.api.Entity Entity]] we can
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
    * `retract(eids, txMetaDataMolecules*)` in [[molecule.api.EntityOps]].
    *
    * @group retract
    * @return [[molecule.facade.TxReport]] with result of retraction
    */
  def retractAsync(implicit ec: ExecutionContext): Future[TxReport] = conn.transactAsync(getRetractTx)

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
  def getRetractTx: List[List[RetractEntity]] = List(List(RetractEntity(id)))

  /** Debug entity transaction data of method `retract` without affecting the database.
    * {{{
    *   // Debug retraction of an entity
    *   eid.debugRetract
    * }}}
    * This will print generated Datomic transaction statements in a readable format to output:
    * {{{
    *   ## 1 ## Debug `retract` on entity
    *   ========================================================================
    *   1          List(
    *     1          List(
    *       1          :db.fn/retractEntity   17592186045445))
    *   ========================================================================
    * }}}
    *
    * @group retract
    */
  def debugRetract: Unit = Debug("Debug `retract` on entity", 1)(1, getRetractTx)


  // Entity retraction with tx meta data =======================================================================

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
    * @return [[molecule.api.Entity.RetractMolecule RetractMolecule]] - a simple wrapper for adding retraction tx meta data
    */
  def Tx(metaMolecule: MoleculeBase): RetractMolecule = RetractMolecule(metaMolecule)

  /** Wrapper to add retract methods on entity with transaction meta data.
    * <br><br>
    * [[molecule.api.Entity.RetractMolecule RetractMolecule]] is created from calling `Tx`:
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
    private val retractStmts = Seq(RetractEntity(id))

    private val _model = Model(Seq(TxMetaData(txMeta._model.elements)))
    VerifyModel(_model, "save")
    private val txMetaStmts = Model2Transaction(conn, _model).saveStmts()

    private val stmtss = Seq(retractStmts ++ txMetaStmts)

    /** Perform retraction of entity with added transaction meta data against database.
      *
      * @return [[molecule.facade.TxReport TxReport]] with result of transaction
      */
    def retract: TxReport = conn.transact(stmtss)

    /** Perform asynchronous retraction of entity with added transaction meta data against database.
      *
      * @return Future[molecule.facade.TxReport] with result of transaction
      */
    def retractAsync(implicit ec: ExecutionContext): Future[TxReport] = conn.transactAsync(stmtss)

    /** Debug entity retraction with transaction meta data.
      * {{{
      *   eid.Tx(MyMetaData.action("moved away")).debugRetract
      * }}}
      * This will print generated Datomic transaction statements in a readable format to output:
      * {{{
      *   ## 1 ## Debug `retract` on entity with tx meta data
      *   ========================================================================
      *   1          List(
      *     1          List(
      *       1          :db.fn/retractEntity   17592186045445
      *       2          :db/add   #db/id[:db.part/tx -1000100]  :myMetaData/action   moved away   Card(1)))
      *   ========================================================================
      * }}}
      */
    def debugRetract: Unit = Debug("Debug `retract` on entity with tx meta data", 1)(1, stmtss)
  }


  // Entity api ================================================================================================


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
    * @param kw Namespaced attribute: ":[namespace with lowercase first letter]/[attribute name]"
    * @tparam T Type of attribute
    * @return Optional typed attribute value
    */
  def apply[T](kw: String): Option[T] = entity.get(kw) match {
    case null                                    => Option.empty[T]
    case results: clojure.lang.PersistentHashSet => results.asScala.head match {
      case ent: datomic.Entity => Some(results.asScala.toList.map(_.asInstanceOf[datomic.Entity].get(":db/id").asInstanceOf[Long]).sorted.asInstanceOf[T])
      case manyValue           => Some(results.asScala.toList.map(toScala(_)).toSet.asInstanceOf[T])
    }
    case result                                  => Some(toScala(result).asInstanceOf[T])
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


  // Touch - traverse entity attributes ...........................................................

  /** Get entity graph as Map.
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    * the value is a Map itself of the referenced entity attributes, etc.
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
    * the value is a Map itself of the referenced entity attributes, etc.
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
    * the value is a Map itself of the referenced entity attributes, etc.
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
  def touchQuoted: String = format(asMap(1, 5))

  /** Get entity graph to some depth as Map-string (for presentation).
    * <br><br>
    * To show the entity graph, this method quotes all text strings so that you can paste the whole graph
    * into any presentation. Pasting it into test code is less useful, since the order of key/value pairs in
    * a Map is not guaranteed. In that case, `touchListQuoted(maxLevel)` is recommended since a List guarantees order.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    * the value is a Map itself of the referenced entity attributes, etc.
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
  def touchQuotedMax(maxDepth: Int): String = format(asMap(1, maxDepth))


  /** Get entity graph as List.
    * <br><br>
    * If entity has reference(s) to other entities it can be a nested graph. Default max levels retrieved is 5.
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    * the value is a Map itself of the referenced entity attributes, etc.
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
    * the value is a Map itself of the referenced entity attributes, etc.
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
    * the value is a Map itself of the referenced entity attributes, etc.
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
  def touchListQuoted: String = format(asList(1, 5))

  /** Get entity graph to some depth as List-string (for tests).
    *
    *  - Keys of returned Map are namespaced names of attributes
    *  - Values of returned Map are untyped attribute values. For references to other entities,
    * the value is a Map itself of the referenced entity attributes, etc.
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
  def touchListQuotedMax(maxDepth: Int): String = format(asList(1, maxDepth))


  // Private helper methods ...........................................................................

  final protected lazy val sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  final protected def format2(date: Date): String = sdf.format(date)

  private def format(value: Any): String = {
    val sb = new StringBuilder
    def traverse(value: Any, tabs: Int): Unit = {
      val t = "  " * tabs
      var i = 0
      value match {
        case s: String                => sb.append(s""""$s"""")
        case l: Long                  => if (l > Int.MaxValue) sb.append(s"${l}L") else sb.append(l) // Int/Long hack
        case d: Double                => sb.append(d)
        case f: Float                 => sb.append(f)
        case bi: java.math.BigInteger => sb.append(bi)
        case bd: java.math.BigDecimal => sb.append(bd)
        case b: Boolean               => sb.append(b)
        case d: Date                  => sb.append(s""""${format2(d)}"""")
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
        case other                    => throw new EntityException("Unexpected element traversed in Entity#format: " + other)
      }
    }
    traverse(value, 1)
    sb.result()
  }

  private def asMap(depth: Int, maxDepth: Int): Map[String, Any] = {
    val builder = Map.newBuilder[String, Any]
    val iter    = blocking {entity.keySet}.asScala.toList.sorted.asJava.iterator()

    // Add id also
    builder += ":db/id" -> entity.get(":db/id")
    while (iter.hasNext) {
      val key         = iter.next()
      val scalaValue  = toScala(entity.get(key), depth, maxDepth)
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

  private def asList(depth: Int, maxDepth: Int): List[(String, Any)] = {
    val builder = List.newBuilder[(String, Any)]
    val iter    = blocking {entity.keySet}.asScala.toList.sorted.asJava.iterator()

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

  private[molecule] def toScala(v: Any, depth: Int = 1, maxDepth: Int = 5, tpe: String = "Map"): Any = v match {
    case s: java.lang.String /* :db.type/string */              => s
    case i: java.lang.Integer /* attribute id */                => i.toLong: Long
    case l: java.lang.Long /* :db.type/long */                  => l: Long
    case f: java.lang.Float /* :db.type/float */                => f: Float
    case d: java.lang.Double /* :db.type/double */              => d: Double
    case b: java.lang.Boolean /* :db.type/boolean */            => b: Boolean
    case d: Date /* :db.type/instant */                         => d
    case u: UUID /* :db.type/uuid */                            => u
    case u: java.net.URI /* :db.type/uri */                     => u
    case bi: java.math.BigInteger /* :db.type/bigint */         => BigInt(bi)
    case bd: java.math.BigDecimal /* :db.type/bigdec */         => BigDecimal(bd)
    case bytes: Array[Byte] /* :db.type/bytes */                => bytes
    case kw: clojure.lang.Keyword /* :db.type/keyword */        => kw.toString // Clojure Keywords not used in Molecule
    case e: datomic.Entity if depth < maxDepth && tpe == "Map"  => Entity(e, conn, e.get(":db/id")).asMap(depth + 1, maxDepth)
    case e: datomic.Entity if depth < maxDepth && tpe == "List" => Entity(e, conn, e.get(":db/id")).asList(depth + 1, maxDepth)
    case e: datomic.Entity                                      => e.get(":db/id").asInstanceOf[Long]
    case set: clojure.lang.PersistentHashSet                    => set.asScala.toList.map(toScala(_, depth, maxDepth, tpe))
    case coll: jCollection[_]                                   =>
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

    case unexpected => throw new EntityException("Unexpected Datalog type to convert: " + unexpected.getClass.toString)
  }
}

/** Entity wrapper factory. */
object Entity {
  /** Entity wrapper factory method
    *
    * @param entity Instantiated datomic.Entity from id
    * @param conn   Implicit [[molecule.facade.Conn Conn]] in scope
    * @param id
    * @return
    */
  def apply(entity: datomic.Entity, conn: Conn, id: Object) = new Entity(entity, conn, id)
}