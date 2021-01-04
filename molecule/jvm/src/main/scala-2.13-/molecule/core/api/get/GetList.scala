package molecule.core.api.get

import java.util.{List => jList}
import molecule.core.api.Molecule
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel.Statement
import molecule.core.util.Quoted
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions


/** Default data getter methods on molecules that return List[Tpl].
  * <br><br>
  * For expected smaller result sets it's convenient to return Lists of tuples of data.
  * Considered as the default getter, no postfix has been added (`get` instead of `getList`).
  * */
trait GetList[Tpl] extends GetArray[Tpl] with Quoted { self: Molecule[Tpl] =>


  // get ================================================================================================

  /** Get `List` of all rows as tuples matching molecule.
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
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsync(implicit* getAsync]] method.
    */
  def get(implicit conn: Conn): List[Tpl] = {
    val jColl = conn.query(_model, _query)
    val it    = jColl.iterator
    val buf   = new ListBuffer[Tpl]
    while (it.hasNext) {
      buf += castRow(it.next)
    }
    buf.toList
  }

  /** Get quoted output
    *
    * Makes it easy to copy test results into tests for instance.
    *
    * @param conn
    */
  def getQuoted(implicit conn: Conn): String = quote2(get(conn))

  /** Get `List` of n rows as tuples matching molecule.
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
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsync(n:Int)* getAsync]] method.
    */
  def get(n: Int)(implicit conn: Conn): List[Tpl] = if (n == -1) {
    get(conn)
  } else {
    val jColl = conn.query(_model, _query)
    val size  = jColl.size
    val max   = if (size < n) size else n
    if (max == 0) {
      List.empty[Tpl]
    } else {
      val it  = jColl.iterator
      val buf = new ListBuffer[Tpl]
      var i   = 0
      while (it.hasNext && i < max) {
        buf += castRow(it.next)
        i += 1
      }
      buf.toList
    }
  }


  // get as of ================================================================================================

  /** Get `List` of all rows as tuples matching molecule as of transaction time `t`.
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
    * @group getAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(t:Long)* getAsyncAsOf]] method.
    */
  def getAsOf(t: Long)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `List` of n rows as tuples matching molecule as of transaction time `t`.
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
    * @group getAsOf
    * @param t    Long Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(t:Long,n:Int)* getAsyncAsOf]] method.
    */
  def getAsOf(t: Long, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `List` of all rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a
    * database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[TxReport TxReport]] from transaction
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
    * @group getAsOf
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(tx:molecule\.facade\.TxReport)* getAsyncAsOf]] method.
    **/
  def getAsOf(tx: TxReport)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `List` of n rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database
    * value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[TxReport TxReport]] from transaction
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
    * @group getAsOf
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(tx:molecule\.facade\.TxReport,n:Int)* getAsyncAsOf]] method.
    **/
  def getAsOf(tx: TxReport, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `List` of all rows as tuples matching molecule as of date.
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
    * @group getAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(date:java\.util\.Date)* getAsyncAsOf]] method.
    */
  def getAsOf(date: java.util.Date)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(AsOf(TxDate(date))))


  /** Get `List` of n rows as tuples matching molecule as of date.
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
    * @group getAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncAsOf(date:java\.util\.Date,n:Int)* getAsyncAsOf]] method.
    */
  def getAsOf(date: java.util.Date, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(AsOf(TxDate(date))))


  // get since ================================================================================================

  /** Get `List` of all rows as tuples matching molecule since transaction time `t`.
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
    * @group getSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(t:Long)* getAsyncSince]] method.
    */
  def getSince(t: Long)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(Since(TxLong(t))))


  /** Get `List` of n rows as tuples matching molecule since transaction time `t`.
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
    * @group getSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(t:Long,n:Int)* getAsyncSince]] method.
    */
  def getSince(t: Long, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(Since(TxLong(t))))


  /** Get `List` of all rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    * @group getSince
    * @param tx   [[TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(tx:molecule\.facade\.TxReport)* getAsyncSince]] method.
    */
  def getSince(tx: TxReport)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `List` of n rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    * @group getSince
    * @param tx   [[TxReport TxReport]]
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(tx:molecule\.facade\.TxReport,n:Int)* getAsyncSince]] method.
    */
  def getSince(tx: TxReport, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `List` of all rows as tuples matching molecule since date.
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
    * @group getSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(date:java\.util\.Date)* getAsyncSince]] method.
    */
  def getSince(date: java.util.Date)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(Since(TxDate(date))))


  /** Get `List` of n rows as tuples matching molecule since date.
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
    * @group getSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncSince(date:java\.util\.Date,n:Int)* getAsyncSince]] method.
    */
  def getSince(date: java.util.Date, n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(Since(TxDate(date))))


  // get with ================================================================================================

  /** Get `List` of all rows as tuples matching molecule with applied molecule transaction data.
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
    * @group getWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncWith(txMolecules* getAsyncWith]] method.
    */
  def getWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get `List` of n rows as tuples matching molecule with applied molecule transaction data.
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
    *
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    * @group getWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncWith(n:Int,txMolecules* getAsyncWith]] method.
    */
  def getWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get `List` of all rows as tuples matching molecule with applied raw transaction data.
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
    * @group getWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncWith(txData:java\.util\.List[_])* getAsyncWith]] method.
    */
  def getWith(txData: java.util.List[_])(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  /** Get `List` of n rows as tuples matching molecule with applied raw transaction data.
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
    * @group getWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncWith(txData:java\.util\.List[_],n:Int)* getAsyncWith]] method.
    */
  def getWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): List[Tpl] =
    get(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  // get history ================================================================================================

  /** Get history of operations as `List` on an attribute in the db.
    * <br><br>
    * Generic datom attributes that can be called when `getHistory` is called:
    * <br>
    * <br> `e` - Entity id
    * <br> `a` - Attribute name
    * <br> `v` - Attribute value
    * <br> `ns` - Namespace name
    * <br> `tx` - [[TxReport TxReport]]
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
    * @group getHistory
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncList.getAsyncHistory(implicit* getAsyncHistory]] method.
    */
  def getHistory(implicit conn: Conn): List[Tpl] =
    get(conn.usingTempDb(History))

  // `getHistory(n: Int)` is not implemented since the whole data set normally needs to be sorted
  // to give chronological meaningful information.
}
