package molecule.action.get

import java.util.{Date, List => jList}
import molecule.action.Molecule
import molecule.ast.tempDb._
import molecule.ast.transaction.Statement
import molecule.facade.{Conn, TxReport}
import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.collection.JavaConverters._


/** Default data getter methods on molecules that return List[Tpl].
  * <br><br>
  * For expected smaller result sets it's convenient to return Lists of tuples of data.
  * Considered as the default getter, no postfix has been added (`get` instead of `getList`).
  * */
trait GetList[Tpl] extends GetArray[Tpl] { self: Molecule[Tpl] =>


  // get ================================================================================================

  /** Get List of all rows as tuples matching molecule.
    * {{{
    *   Person.name.age.get === List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    * }}}
    * Since retrieving a List is considered the default fetch format, the getter method is
    * simply named `get` (and not `getList`).
    *
    * @group get
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def get(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn, ev).to[List]

  val xx = Array(2)

  xx(0) = 7

  /** Get List of n rows as tuples matching molecule.
    * <br><br>
    * Only n rows are type-casted.
    * {{{
    *   Person.name.age.get(1) === List(
    *     ("Ben", 42)
    *   )
    * }}}
    * Since retrieving a List is considered the default fetch format, the getter method is
    * simply named `get` (and not `getList`).
    *
    * @group get
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def get(n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn, ev).to[List]


  // get as of ================================================================================================

  /** Get List of all rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction):
    * {{{
    *   // Insert (t 1028)
    *   val List(ben, liz) = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
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
    *   // Get List of all rows as of transaction t 1028 (after insert)
    *   Person.name.age.getAsOf(1028) === List(
    *     ("Liz", 37),
    *     ("Ben", 42)
    *   )
    *
    *   // Get List of all rows as of transaction t 1031 (after update)
    *   Person.name.age.getAsOf(1031) === List(
    *     ("Liz", 37),
    *     ("Ben", 43)
    *   )
    *
    *   // Get List of all rows as of transaction t 1032 (after retract)
    *   Person.name.age.getAsOf(1032) === List(
    *     ("Liz", 37)
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsOf(t: Long)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxLong(t))), ev).to[List]


  /** Get List of n rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction):
    * {{{
    *   // Insert (t 1028)
    *   val List(ben, liz) = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
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
    *   // Get List of all all rows as of transaction t 1031 (after update)
    *   Person.name.age.getAsOf(1031) === List(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of n rows as of transaction t 1031 (after update)
    *   Person.name.age.getAsOf(1031, 1) === List(
    *     ("Ben", 43)
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param t    Long Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsOf(t: Long, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxLong(t))), ev).to[List]


  /** Get List of all rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a
    * database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Insert (tx report 1)
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    *   val List(ben, liz) = tx1.eids
    *
    *   // Update (tx report 2)
    *   val tx2 = Person(ben).age(43).update
    *
    *   // Retract (tx report 3)
    *   val tx3 = ben.retract
    *
    *   // Get List of all rows as of tx1 (after insert)
    *   Person.name.age.getAsOf(tx1) === List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of all rows as of tx2 (after update)
    *   Person.name.age.getAsOf(tx2) === List(
    *     ("Ben", 43), // Ben now 43
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of all rows as of tx3 (after retract)
    *   Person.name.age.getAsOf(tx3) === List(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * */
  def getAsOf(tx: TxReport)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxLong(tx.t))), ev).to[List]


  /** Get List of n rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database
    * value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.facade.TxReport TxReport]] from transaction
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
    *   // Current data
    *   Person.name.age.get === List(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of all rows as of tx2 (after update)
    *   Person.name.age.getAsOf(tx2) === List(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of n rows as of tx2 (after update)
    *   Person.name.age.getAsOf(tx2, 1) === List(
    *     ("Ben", 43)
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * */
  def getAsOf(tx: TxReport, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxLong(tx.t))), ev).to[List]


  /** Get List of all rows as tuples matching molecule as of date.
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
    *   Person.name.age.getAsOf(beforeInsert) === Nil
    *
    *   // Get List of all rows as of afterInsert
    *   Person.name.age.getAsOf(afterInsert) === List(
    *     ("Ben", 42),
    *     ("Liz", 37)´
    *   )
    *
    *   // Get List of all rows as of afterUpdate
    *   Person.name.age.getAsOf(afterUpdate) === List(
    *     ("Ben", 43), // Ben now 43
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of all rows as of afterRetract
    *   Person.name.age.getAsOf(afterRetract) === List(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsOf(date: Date)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxDate(date))), ev).to[List]


  /** Get List of n rows as tuples matching molecule as of date.
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
    *   Person.name.age.getAsOf(afterUpdate) === List(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get List of n rows as of afterUpdate
    *   Person.name.age.getAsOf(afterUpdate, 1) === List(
    *     ("Ben", 43)
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsOf(date: Date, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxDate(date))), ev).to[List]


  // get since ================================================================================================

  /** Get List of all rows as tuples matching molecule since transaction time `t`.
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
    *   Person.name.getSince(t1) === List("Ben", "Cay")
    *
    *   // Cay added since transaction time t 1030
    *   Person.name.getSince(t2) === List("Cay")
    *
    *   // Nothing added since transaction time t 1032
    *   Person.name.getSince(t3) === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(t: Long)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(Since(TxLong(t))), ev).to[List]


  /** Get List of n rows as tuples matching molecule since transaction time `t`.
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
    *   Person.name.getSince(t1) === List("Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t 1028 - only n (1) rows returned
    *   Person.name.getSince(t1, 1) === List("Ben")
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(t: Long, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxLong(t))), ev).to[List]


  /** Get List of all rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    *   Person.name.getSince(tx1) === List("Ben", "Cay")
    *
    *   // Cay added since tx2
    *   Person.name.getSince(tx2) === List("Cay")
    *
    *   // Nothing added since tx3
    *   Person.name.getSince(tx3) === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(tx: TxReport)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(Since(TxLong(tx.t))), ev).to[List]


  /** Get List of n rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    *   Person.name.getSince(tx1) === List("Ben", "Cay")
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   Person.name.getSince(tx1, 1) === List("Ben")
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(tx: TxReport, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxLong(tx.t))), ev).to[List]


  /** Get List of all rows as tuples matching molecule since date.
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
    *   Person.name.getSince(date1) === List("Ben", "Cay")
    *
    *   // Cay added since date2
    *   Person.name.getSince(date2) === List("Cay")
    *
    *   // Nothing added since date3
    *   Person.name.getSince(date3) === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(date: Date)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(Since(TxDate(date))), ev).to[List]


  /** Get List of n rows as tuples matching molecule since date.
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
    *   Person.name.getSince(date1) === List("Ben", "Cay")
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   Person.name.getSince(date1, 1) === List("Ben")
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getSince(date: Date, n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxDate(date))), ev).to[List]


  // get with ================================================================================================

  /** Get List of all rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.likes.getWith(
    *     // apply imaginary transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   ) === List(
    *     // Effect: Ben would like sushi if tx was applied
    *     ("Ben", "sushi")
    *   )
    *
    *   // Current state is still the same
    *   Person.name.likes.get === List(("Ben", "pasta"))
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), ev).to[List]


  /** Get List of n rows as tuples matching molecule with applied molecule transaction data.
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
    *   Person.name.likes.getWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) === List(
    *     ("Ben", "sushi")
    *     ("Liz", "cake")
    *   )
    *
    *   // Same as above, but only n (1) rows returned:
    *   Person.name.likes.getWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) === List(
    *     ("Ben", "sushi")
    *   )
    * }}}
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), ev).to[List]


  /** Get List of all rows as tuples matching molecule with applied raw transaction data.
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
    *   Person.name.getWith(newDataTx).size === 250
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getWith(txData: jList[_])(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), ev).to[List]


  /** Get List of n rows as tuples matching molecule with applied raw transaction data.
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
    *   Person.name.getWith(newDataTx).size === 250
    *
    *   // Imagine future db - Let's just take 10
    *   Person.name.getWith(newDataTx, 10).size === 10
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getWith(txData: jList[_], n: Int)(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), ev).to[List]


  // get history ================================================================================================

  /** Get history of operations as List on an attribute in the db.
    * <br><br>
    * Generic datom attributes that can be called when `getHistory` is called:
    * <br>
    * <br> `e` - Entity id
    * <br> `a` - Attribute name
    * <br> `v` - Attribute value
    * <br> `ns` - Namespace name
    * <br> `tx` - [[molecule.facade.TxReport TxReport]]
    * <br> `t` - Transaction time t
    * <br> `txInstant` - Transaction time as java.util.Date
    * <br> `op` - Operation: true (add) or false (retract)
    * <br><br>
    * Example:
    * {{{
    *   // Insert (t 1028)
    *   val List(ben, liz) = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
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
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/history/ manual]] for more info on generic attributes.
    * @group history
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getHistory(implicit conn: Conn, ev: ClassTag[Tpl]): List[Tpl] =
    getArray(conn.usingTempDb(History), ev).to[List]

  // `getHistory(n: Int)` is not implemented since the whole data set normally needs to be sorted
  // to give chronological meaningful information.
}
