package molecule.core.api.get

import java.util.{List => jList}
import molecule.core.api.Molecule
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.collection.JavaConverters._
import scala.language.implicitConversions
import scala.reflect.ClassTag


/** Data getter methods on molecules returning `Array[Tpl]`.
  * <br><br>
  * The fastest way of getting a large typed data set. Can then be traversed with a fast `while` loop.
  * */
trait GetArray[Tpl] { self: Molecule[Tpl] =>


  // get ================================================================================================

  /** Get `Array` of all rows as tuples matching molecule.
    * {{{
    *   Person.name.age.getArray === Array(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group get
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArray(implicit* getAsyncArray]] method.
    */
  def getArray(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] = {
    val jColl = conn.query(_model, _query)
    val it = jColl.iterator
    val a = new Array[Tpl](jColl.size)
    var i = 0
    while (it.hasNext) {
      a(i) = castRow(it.next)
      i += 1
    }
    a
  }

  /** Get `Array` of n rows as tuples matching molecule.
    * {{{
    *   Person.name.age.getArray(1) === Array(
    *     ("Ben", 42)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples. Setting n to -1 fetches
    * all rows (same as calling `getArray` without any number of rows parameter).
    *
    * @group get
    * @param n       Number of rows. If -1, all rows are fetched.
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArray(n:Int)* getAsyncArray]] method.
    */
  def getArray(n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] = if (n == -1) {
    getArray(conn, tplType)
  } else {
    val jColl = conn.query(_model, _query)
    val size = jColl.size
    val max = if (size < n) size else n
    if (max == 0) {
      new Array[Tpl](0)
    } else {
      val it = jColl.iterator
      val a = new Array[Tpl](max)
      var i = 0
      while (it.hasNext && i < max) {
        a(i) = castRow(it.next)
        i += 1
      }
      a
    }
  }


  // get as of ================================================================================================

  /** Get `Array` of all rows as tuples matching molecule as of transaction time `t`.
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
    *   // Get Array of data as of transaction t 1028 (after insert)
    *   Person.name.age.getArrayAsOf(1028) === Array(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of transaction t 1031 (after update)
    *   Person.name.age.getArrayAsOf(1031) === Array(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of transaction t 1032 (after retract)
    *   Person.name.age.getArrayAsOf(1032) === Array(
    *     ("Liz", 37)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArrayAsOf
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(t:Long)* getAsyncArrayAsOf]] method.
    */
  def getArrayAsOf(t: Long)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxLong(t))), tplType)


  /** Get `Array` of n rows as tuples matching molecule as of transaction time `t`.
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
    *   // Get Array of all rows as of transaction t 1031 (after update)
    *   Person.name.age.getArrayAsOf(1031) === Array(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of n rows as of transaction t 1031 (after update)
    *   Person.name.age.getArrayAsOf(1031, 1) === Array(
    *     ("Ben", 43)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArrayAsOf
    * @param t       Long Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(t:Long,n:Int)* getAsyncArrayAsOf]] method.
    */
  def getArrayAsOf(t: Long, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxLong(t))), tplType)


  /** Get `Array` of all rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve
    * a database value as of that transaction (including).
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
    *   // Retract (tx report 3)
    *   val tx3 = ben.retract
    *
    *   // Get Array of all rows as of tx1 (after insert)
    *   Person.name.age.getArrayAsOf(tx1) === Array(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of tx2 (after update)
    *   Person.name.age.getArrayAsOf(tx2) === Array(
    *     ("Ben", 43), // Ben now 43
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of tx3 (after retract)
    *   Person.name.age.getArrayAsOf(tx3) === Array(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArrayAsOf
    * @param tx      [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(tx:molecule\.facade\.TxReport)* getAsyncArrayAsOf]] method.
    **/
  def getArrayAsOf(tx: TxReport)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxLong(tx.t))), tplType)
  // Fully qualifying tx parameter (and other parameters below) for Scala Docs to be able to pick it up
  // when referencing the method from the asynchronous cousin methods.

  /** Get `Array` of n rows as tuples matching molecule as of tx.
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
    *   // Get Array of all rows as of tx2 (after update)
    *   Person.name.age.getArrayAsOf(tx2) === Array(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of n rows as of tx2 (after update)
    *   Person.name.age.getArrayAsOf(tx2, 1) === Array(
    *     ("Ben", 43)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArrayAsOf
    * @param tx      [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(tx:molecule\.facade\.TxReport,n:Int)* getAsyncArrayAsOf]] method.
    **/
  def getArrayAsOf(tx: TxReport, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxLong(tx.t))), tplType)


  /** Get `Array` of all rows as tuples matching molecule as of date.
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
    *   // Retract
    *   val tx3 = ben.retract
    *   val afterRetract = new java.util.Date
    *
    *   // No data yet before insert
    *   Person.name.age.getArrayAsOf(beforeInsert) === Array()
    *
    *   // Get Array of all rows as of afterInsert
    *   Person.name.age.getArrayAsOf(afterInsert) === Array(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of afterUpdate
    *   Person.name.age.getArrayAsOf(afterUpdate) === Array(
    *     ("Ben", 43), // Ben now 43
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of all rows as of afterRetract
    *   Person.name.age.getArrayAsOf(afterRetract) === Array(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArrayAsOf
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(date:java\.util\.Date)* getAsyncArrayAsOf]] method.
    */
  def getArrayAsOf(date: java.util.Date)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(AsOf(TxDate(date))), tplType)


  /** Get `Array` of n rows as tuples matching molecule as of date.
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
    *   // Get Array of all rows as of afterUpdate
    *   Person.name.age.getArrayAsOf(afterUpdate) === Array(
    *     ("Ben", 43),
    *     ("Liz", 37)
    *   )
    *
    *   // Get Array of n rows as of afterUpdate
    *   Person.name.age.getArrayAsOf(afterUpdate, 1) === Array(
    *     ("Ben", 43)
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArrayAsOf
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayAsOf(date:java\.util\.Date,n:Int)* getAsyncArrayAsOf]] method.
    */
  def getArrayAsOf(date: java.util.Date, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(AsOf(TxDate(date))), tplType)


  // get since ================================================================================================

  /** Get `Array` of all rows as tuples matching molecule since transaction time `t`.
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
    *   Person.name.getArraySince(t1) === Array("Ben", "Cay")
    *
    *   // Cay added since transaction time t 1030
    *   Person.name.getArraySince(t2) === Array("Cay")
    *
    *   // Nothing added since transaction time t 1032
    *   Person.name.getArraySince(t3) === Nil
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArraySince
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(t:Long)* getAsyncArraySince]] method.
    */
  def getArraySince(t: Long)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(Since(TxLong(t))), tplType)

  /** Get `Array` of n rows as tuples matching molecule since transaction time `t`.
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
    *   Person.name.getArraySince(t1) === Array("Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t 1028 - only n (1) rows returned
    *   Person.name.getArraySince(t1, 1) === Array("Ben")
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArraySince
    * @param t       Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(t:Long,n:Int)* getAsyncArraySince]] method.
    */
  def getArraySince(t: Long, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxLong(t))), tplType)


  /** Get `Array` of all rows as tuples matching molecule since tx.
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
    *   Person.name.getArraySince(tx1) === Array("Ben", "Cay")
    *
    *   // Cay added since tx2
    *   Person.name.getArraySince(tx2) === Array("Cay")
    *
    *   // Nothing added since tx3
    *   Person.name.getArraySince(tx3) === Nil
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArraySince
    * @param tx      [[TxReport TxReport]]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(tx:molecule\.facade\.TxReport)* getAsyncArraySince]] method.
    */
  def getArraySince(tx: TxReport)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(Since(TxLong(tx.t))), tplType)


  /** Get `Array` of n rows as tuples matching molecule since tx.
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
    *   Person.name.getArraySince(tx1) === Array("Ben", "Cay")
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   Person.name.getArraySince(tx1, 1) === Array("Ben")
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArraySince
    * @param tx      [[TxReport TxReport]]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(tx:molecule\.facade\.TxReport,n:Int)* getAsyncArraySince]] method.
    **/
  def getArraySince(tx: TxReport, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxLong(tx.t))), tplType)


  /** Get `Array` of all rows as tuples matching molecule since date.
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
    *   Person.name.getArraySince(date1) === Array("Ben", "Cay")
    *
    *   // Cay added since date2
    *   Person.name.getArraySince(date2) === Array("Cay")
    *
    *   // Nothing added since date3
    *   Person.name.getArraySince(date3) === Nil
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArraySince
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(date:java\.util\.Date)* getAsyncArraySince]] method.
    */
  def getArraySince(date: java.util.Date)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(Since(TxDate(date))), tplType)


  /** Get `Array` of n rows as tuples matching molecule since date.
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
    *   Person.name.getArraySince(date1) === Array("Ben", "Cay")
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   Person.name.getArraySince(date1, 1) === Array("Ben")
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArraySince
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArraySince(date:java\.util\.Date,n:Int)* getAsyncArraySince]] method.
    */
  def getArraySince(date: java.util.Date, n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(Since(TxDate(date))), tplType)


  // get with ================================================================================================

  /** Get `Array` of all rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.likes.getArrayWith(
    *     // apply imaginary transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   ) === Array(
    *     // Effect: Ben would like sushi if tx was applied
    *     ("Ben", "sushi")
    *   )
    *
    *   // Current state is still the same
    *   Person.name.likes.get === Array(
    *     ("Ben", "pasta")
    *   )
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArrayWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayWith(txMolecules* getAsyncArrayWith]] method.
    */
  def getArrayWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), tplType)


  /** Get `Array` of n rows as tuples matching molecule with applied molecule transaction data.
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
    *   Person.name.likes.getArrayWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) === Array(
    *     ("Ben", "sushi")
    *     ("Liz", "cake")
    *   )
    *
    *   // Same as above, but only n (1) rows returned:
    *   Person.name.likes.getArrayWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ) === Array(
    *     ("Ben", "sushi")
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArrayWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayWith(n:Int,txMolecules* getAsyncArrayWith]] method.
    */
  def getArrayWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)), tplType)


  /** Get `Array` of all rows as tuples matching molecule with applied raw transaction data.
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
    *   Person.name.getArrayWith(newDataTx).size === 250
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayWith(txData:java\.util\.List[_])* getAsyncArrayWith]] method.
    */
  def getArrayWith(txData: jList[_])(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), tplType)


  /** Get `Array` of n rows as tuples matching molecule with applied raw transaction data.
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
    *   Person.name.getArrayWith(newDataTx).size === 250
    *
    *   // Imagine future db - Let's just take 10
    *   Person.name.getArrayWith(newDataTx, 10).size === 10
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncArray.getAsyncArrayWith(txData:java\.util\.List[_],n:Int)* getAsyncArrayWith]] method.
    */
  def getArrayWith(txData: jList[_], n: Int)(implicit conn: Conn, tplType: ClassTag[Tpl]): Array[Tpl] =
    getArray(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), tplType)


  // get history ================================================================================================

  // Only `getHistory` (returning a List) is implemented since it is only meaningful
  // to track the history of one attribute at a time and a sortable List is therefore preferred.
}
