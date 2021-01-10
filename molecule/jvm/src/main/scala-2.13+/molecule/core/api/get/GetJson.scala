package molecule.core.api.get

import java.util.{Collection => jCollection, List => jList}
import molecule.core.ast.MoleculeBase
import molecule.core.ast.elements.{Bond, Nested}
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import molecule.core.macros.exception.NestedJsonException
import molecule.core.transform.JsonBuilder
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions


/** Data getter methods on molecules that return a Json String.
  * <br><br>
  * Molecule builds a Json String directly from the untyped raw Datomic data.
  * <br><br>
  * Attributes names are used as Json field names. In order to distinguish
  * fields from each other, all attribute names are prepended with the namespace
  * name (in lowercase). For a namespace `Person` with an attribute `name` we get:
  *
  *  - "person.name"
  *
  * To distinguis fields of multiple relationships to the same namespace like `friends` and
  * `enemies` pointing to other `Person`'s require us to add a relationship name prefix too:
  *
  *  - "friends.person.name"
  *  - "enemies.person.name"
  *
  * Furthermore, if the attribute is part of a transaction meta-data molecule, we prefix that with `tx` too:
  *
  *  - "tx.person.name"
  *  - "tx.friends.person.name"
  *
  **/
trait GetJson { self: MoleculeBase with JsonBuilder =>


  // get ================================================================================================

  /** Get json data for all rows matching molecule.
    * {{{
    *   Person.name.age.getJson ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 42},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    * }}}
    * Namespace.Attribute is used as json fields. Values are
    * quoted when necessary. Nested data becomes json objects etc.
    *
    * @group get
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJson(implicit* getAsyncJson]] method.
    */
  def getJson(implicit conn: Conn): String =
    getJsonFlat(conn, -1)


  /** Get json data for n rows matching molecule
    * {{{
    *   Person.name.age.getJson(1) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 42}
    *       |]""".stripMargin
    * }}}
    * Namespace.Attribute is used as json fields. Values are
    * quoted when necessary. Nested data becomes json objects etc.
    *
    * @group get
    * @param n    Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJson(n:Int)* getAsyncJson]] method.
    */
  def getJson(n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn, n)


  // get as of ================================================================================================

  /** Get json data for all rows matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction):
    * {{{
    *   // Insert (t 1028)
    *   val List(ben, liz) = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   ) eids
    *
    *   // Update (t 1031)
    *   Person(ben).age(43).update
    *
    *   // Retract (t 1032)
    *   ben.retract
    *
    *   // History of Ben
    *   Person(ben).age.t.op.getHistory.sortBy(r => (r._2, r._3)) === List(
    *     (42, 1028, true),  // Insert:  42 asserted
    *     (42, 1031, false), // Update:  42 retracted
    *     (43, 1031, true),  //          43 asserted
    *     (43, 1032, false)  // Retract: 43 retracted
    *   )
    *
    *   // Get json for all rows as of transaction t 1028 (after insert)
    *   Person.name.age.getJsonAsOf(1028) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 42},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for all rows as of transaction t 1031 (after update) - Ben now 43
    *   Person.name.age.getJsonAsOf(1031) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for all rows as of transaction t 1032 (after retract) - Ben gone
    *   Person.name.age.getJsonAsOf(1032) ===
    *     """[
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(t:Long)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(t: Long)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxLong(t))), -1)


  /** Get json data for n rows matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction):
    * {{{
    *   // Insert (t 1028)
    *   val List(ben, liz) = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   ) eids
    *
    *   // Update (t 1031)
    *   Person(ben).age(43).update
    *
    *   // History of Ben
    *   Person(ben).age.t.op.getHistory.sortBy(r => (r._2, r._3)) === List(
    *     (42, 1028, true),  // Insert:  42 asserted
    *     (42, 1031, false), // Update:  42 retracted
    *     (43, 1031, true),  //          43 asserted
    *   )
    *
    *   // Get json for all rows as of transaction t 1031 (after update)
    *   Person.name.age.getJsonAsOf(1031) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for n (1) rows as of transaction t 1031 (after update)
    *   Person.name.age.getJsonAsOf(1031, 1) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(t:Long,n:Int)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(t: Long, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxLong(t))), n)


  /** Get json data for all rows matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a
    * database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Insert (tx report 1)
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *   val List(ben, liz) = tx1.eids
    *
    *   // Update (tx report 2)
    *   val tx2 = Person(ben).age(43).update
    *
    *   // Retract (tx report 3)
    *   val tx3 = ben.retract
    *
    *   // Get json for all rows as of transaction tx1 (after insert)
    *   Person.name.age.getJsonAsOf(tx1) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 42},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for all rows as of transaction tx2 (after update) - Ben now 43
    *   Person.name.age.getJsonAsOf(tx2) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for all rows as of transaction tx3 (after retract) - Ben gone
    *   Person.name.age.getJsonAsOf(tx3) ===
    *     """[
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(tx: TxReport)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxLong(tx.t))), -1)


  /** Get json data for n rows matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a
    * database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Insert (tx report 1)
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *   val List(ben, liz) = tx1.eids
    *
    *   // Update (tx report 2)
    *   val tx2 = Person(ben).age(43).update
    *
    *   // Get json for all rows as of transaction tx2 (after update) - Ben now 43
    *   Person.name.age.getJsonAsOf(tx2) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get json for n rows as of transaction tx2 (after update) - Ben now 43
    *   Person.name.age.getJsonAsOf(tx2, 1) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(tx: TxReport, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxLong(tx.t))), n)


  /** Get json data for all rows matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * {{{
    *   val beforeInsert = new java.util.Date
    *
    *   // Insert
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    *   val List(ben, liz) = tx1.eids
    *   val afterInsert = new java.util.Date
    *
    *   // Update
    *   val tx2 = Person(ben).age(43).update
    *   val afterUpdate = new java.util.Date
    *
    *   // Retract
    *   val tx3 = ben.retract
    *   val afterRetract = new java.util.Date
    *
    *   // No data yet before insert
    *   Person.name.age.getJsonAsOf(beforeInsert) === ""
    *
    *   // Get List of all rows as of afterInsert
    *   Person.name.age.getJsonAsOf(afterInsert) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 42},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get List of all rows as of afterUpdate
    *   Person.name.age.getJsonAsOf(afterUpdate) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get List of all rows as of afterRetract
    *   Person.name.age.getJsonAsOf(afterRetract) ===
    *     """[
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(date:java\.util\.Date)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(date: java.util.Date)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxDate(date))), -1)


  /** Get json data for n rows matching molecule as of tx.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * {{{
    *   val beforeInsert = new java.util.Date
    *
    *   // Insert
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *   val List(ben, liz) = tx1.eids
    *   val afterInsert = new java.util.Date
    *
    *   // Update
    *   val tx2 = Person(ben).age(43).update
    *   val afterUpdate = new java.util.Date
    *
    *   // Get List of all rows as of afterUpdate
    *   Person.name.age.getJsonAsOf(afterUpdate) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43},
    *       |{"person.name": "Liz", "person.age": 37}
    *       |]""".stripMargin
    *
    *   // Get List of n rows as of afterUpdate
    *   Person.name.age.getJsonAsOf(afterUpdate, 1) ===
    *     """[
    *       |{"person.name": "Ben", "person.age": 43}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonAsOf(date:java\.util\.Date,n:Int)* getAsyncJsonAsOf]] method.
    */
  def getJsonAsOf(date: java.util.Date, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(AsOf(TxDate(date))), n)


  // get since ================================================================================================

  /** Get json data for all rows matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction):
    * {{{
    *   // 3 transaction times `t`
    *   val t1 = Person.name("Ann").save.t
    *   val t2 = Person.name("Ben").save.t
    *   val t3 = Person.name("Cay").save.t
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t 1028
    *   Person.name.getJsonSince(t1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Cay added since transaction time t 1030
    *   Person.name.getJsonSince(t2) ===
    *     """[
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Nothing added since transaction time t 1032
    *   Person.name.getJsonSince(t3) === ""
    * }}}
    *
    * @group getJsonSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(t:Long)* getAsyncJsonSince]] method.
    */
  def getJsonSince(t: Long)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxLong(t))), -1)


  /** Get json data for n rows matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction):
    * {{{
    *   // 3 transaction times `t`
    *   val t1 = Person.name("Ann").save.t
    *   val t2 = Person.name("Ben").save.t
    *   val t3 = Person.name("Cay").save.t
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t 1028
    *   Person.name.getJsonSince(t1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Ben and Cay added since transaction time t 1028 - only n (1) rows returned
    *   Person.name.getJsonSince(t1, 1) ===
    *     """[
    *       |{"person.name": "Ben"}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(t:Long,n:Int)* getAsyncJsonSince]] method.
    */
  def getJsonSince(t: Long, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxLong(t))), n)


  /** Get json data for all rows matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Get tx reports for 3 transactions
    *   val tx1 = Person.name("Ann").save
    *   val tx2 = Person.name("Ben").save
    *   val tx3 = Person.name("Cay").save
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since tx1
    *   Person.name.getJsonSince(tx1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Cay added since tx2
    *   Person.name.getJsonSince(tx2) ===
    *     """[
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Nothing added since tx3
    *   Person.name.getJsonSince(tx3) === ""
    * }}}
    *
    * @group getJsonSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncJsonSince]] method.
    */
  def getJsonSince(tx: TxReport)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxLong(tx.t))), -1)


  /** Get json data for n rows matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Get tx reports for 3 transactions
    *   val tx1 = Person.name("Ann").save
    *   val tx2 = Person.name("Ben").save
    *   val tx3 = Person.name("Cay").save
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since tx1
    *   Person.name.getJsonSince(tx1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   Person.name.getJsonSince(tx1, 1) ===
    *     """[
    *       |{"person.name": "Ben"}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncJsonSince]] method.
    */
  def getJsonSince(tx: TxReport, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxLong(tx.t))), n)


  /** Get json data for all rows matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since date1
    *   Person.name.getJsonSince(date1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Cay added since date2
    *   Person.name.getJsonSince(date2) ===
    *     """[
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Nothing added since date3
    *   Person.name.getJsonSince(date3) === ""
    * }}}
    *
    * @group getJsonSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(date:java\.util\.Date)* getAsyncJsonSince]] method.
    */
  def getJsonSince(date: java.util.Date)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxDate(date))), -1)


  /** Get json data for n rows matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since date1
    *   Person.name.getJsonSince(date1) ===
    *     """[
    *       |{"person.name": "Ben"},
    *       |{"person.name": "Cay"}
    *       |]""".stripMargin
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   Person.name.getJsonSince(date1, 1) ===
    *     """[
    *       |{"person.name": "Ben"}
    *       |]""".stripMargin
    * }}}
    *
    * @group getJsonSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonSince(date:java\.util\.Date,n:Int)* getAsyncJsonSince]] method.
    */
  def getJsonSince(date: java.util.Date, n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(Since(TxDate(date))), n)


  // get with ================================================================================================

  /** Get json data for all rows matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.likes.getJsonWith(
    *     // apply imaginary transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   ) ===
    *     """[
    *       |{"person.name": "Ben", "person.likes": "sushi"}
    *       |]""".stripMargin
    *
    *   // Current state is still the same
    *   Person.name.likes.get === List(("Ben", "pasta"))
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getJsonWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonWith(txMolecules* getAsyncJsonWith]] method.
    */
  def getJsonWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), -1)


  /** Get json data for all rows matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val List(ben, liz) = Person.name.likes.insert(
    *     ("Ben", "pasta"),
    *     ("Liz", "pizza")
    *   ).eids
    *
    *   // Test multiple transactions
    *   Person.name.likes.getJsonWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) ===
    *     """[
    *       |{"person.name": "Ben", "person.likes": "sushi"},
    *       |{"person.name": "Liz", "person.likes": "cake"}
    *       |]""".stripMargin
    *
    *   // Same as above, but only n (1) rows returned:
    *   Person.name.likes.getJsonWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) ===
    *     """[
    *       |{"person.name": "Ben", "person.likes": "sushi"}
    *       |]""".stripMargin
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getJsonWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonWith(n:Int,txMolecules* getAsyncJsonWith]] method.
    */
  def getJsonWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), n)


  /** Get json data for all rows matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Live size of Person db
    *   Person.name.get.size === 150
    *
    *   // Read some transaction data from file
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val newDataTx = Util.readAll(data_rdr2).get(0).asInstanceOf[java.util.List[Object]]
    *
    *   // Imagine future db - 100 persons would be added, apparently
    *   Person.name.getJsonWith(newDataTx).size === 250
    * }}}
    *
    * @group getJsonWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonWith(txData:java\.util\.List[_])* getAsyncJsonWith]] method.
    */
  def getJsonWith(txData: java.util.List[_])(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), -1)


  /** Get json data for n rows matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Live size of Person db
    *   Person.name.get.size === 150
    *
    *   // Read some transaction data from file
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val newDataTx = Util.readAll(data_rdr2).get(0).asInstanceOf[java.util.List[Object]]
    *
    *   // Imagine future db - 100 persons would be added, apparently
    *   Person.name.getJsonWith(newDataTx).size === 250
    *
    *   // Imagine future db - Let's just take 10
    *   Person.name.getJsonWith(newDataTx, 10).size === 10
    * }}}
    *
    * @group getJsonWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return String of json
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncJson.getAsyncJsonWith(txData:java\.util\.List[_],n:Int)* getAsyncJsonWith]] method.
    */
  def getJsonWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): String =
    getJsonFlat(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), n)


  // get history ================================================================================================

  // Only `getHistory` (returning a List) is implemented since it is only meaningful
  // to track the history of one attribute at a time and a sortable List is therefore preferred.


  // Builder implementations ---------------------------------------------

  // Default implementation for flat (non-nested) rows
  // The `row2json` doing the heavy lifting is materialized by macros when `molecule.api.json._` is imported
  // Composite molecules override this method to use `getJsonComposite` (see below) instead.
  protected def getJsonFlat(conn: Conn, n: Int): String = {
    if (
      _model.elements.exists {
        case Nested(Bond(_, refAttr, _, _, _), _) if refAttr.endsWith("$") => true
        case _                                                             => false
      }
    ) throw new NestedJsonException("Optional nested data as json not implemented")

    val jColl: jCollection[jList[AnyRef]] = conn.query(_model, _query)
    if (jColl.size == 0) {
      ""
    } else {
      var subsequent = false
      val it         = jColl.iterator()
      val sb         = new StringBuilder("[")
      if (n == -1) {
        // All
        while (it.hasNext) {
          if (subsequent) {
            sb.append(",")
          } else {
            subsequent = true
          }
          sb.append("\n{")
          row2json(sb, it.next())
          sb.append("}")
        }
      } else {
        // n rows
        var i = 0
        while (it.hasNext && i < n) {
          if (subsequent) {
            sb.append(",")
          } else {
            subsequent = true
          }
          sb.append("\n{")
          row2json(sb, it.next())
          sb.append("}")
          i += 1
        }
      }
      sb.append("\n]").toString()
    }
  }


  protected def getJsonComposite(conn: Conn, n: Int): String = {
    val jColl: jCollection[jList[AnyRef]] = conn.query(_model, _query)
    if (jColl.size == 0) {
      ""
    } else {
      var subsequent = false
      val it         = jColl.iterator()
      val sb         = new StringBuilder("[")
      if (n == -1) {
        // All
        while (it.hasNext) {
          if (subsequent) {
            sb.append(",")
          } else {
            subsequent = true
          }
          sb.append("\n[")
          row2json(sb, it.next())
          sb.append("]")
        }
      } else {
        // n rows
        var i = 0
        while (it.hasNext && i < n) {
          if (subsequent) {
            sb.append(",")
          } else {
            subsequent = true
          }
          sb.append("\n[")
          row2json(sb, it.next())
          sb.append("]")
          i += 1
        }
      }
      sb.append("\n]").toString()
    }
  }
}
