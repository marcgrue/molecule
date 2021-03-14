package molecule.core.api.getObj

import java.util.{Date, List => jList}
import molecule.core.api.Molecule_0
import molecule.core.util.JavaUtil
import molecule.datomic.base.ast.tempDb._
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.language.implicitConversions
import scala.reflect.ClassTag


/** Data getter methods on molecules returning `Array[Obj]`.
  * <br><br>
  * The fastest way of getting a large typed data set. Can then be traversed with a fast `while` loop.
  * */
trait GetObjArray[Obj, Tpl] extends JavaUtil { self: Molecule_0[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Array` of all rows as objects matching molecule.
    * {{{
    *   val persons = Person.name.age.getObjArray
    *
    *   // Fast while loop
    *   var i = 0
    *   val length = persons.length
    *   while(i < length) {
    *     val p = persons(i)
    *     println(s"${p.name} is ${p.age} years old") // Do stuff with row object...
    *     i += 1
    *   }
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group get
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArray(implicit* getAsyncObjArray]] method.
    */
  def getObjArray(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] = {
    val jColl = conn.query(_model, _query)
    val it = jColl.iterator
    val a = new Array[Obj](jColl.size)
    var i = 0
    while (it.hasNext) {
      a(i) = row2obj(it.next)
      i += 1
    }
    a
  }

  /** Get `Array` of n rows as objects matching molecule.
    * {{{
    *   Person.name.age.getObjArray(1).map(p => s"${p.name} is ${p.age} years old")) === List(
    *     "Ben is 42 years old"
    *   )
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects. Setting n to -1 fetches
    * all rows (same as calling `getObjArray` without any number of rows parameter).
    *
    * @group get
    * @param n       Number of rows. If -1, all rows are fetched.
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArray(n:Int)* getAsyncArray]] method.
    */
  def getObjArray(n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] = if (n == -1) {
    getObjArray(conn, objType, tplType)
  } else {
    val jColl = conn.query(_model, _query)
    val size = jColl.size
    val max = if (size < n) size else n
    if (max == 0) {
      new Array[Obj](0)
    } else {
      val it = jColl.iterator
      val a = new Array[Obj](max)
      var i = 0
      while (it.hasNext && i < max) {
        a(i) = row2obj(it.next)
        i += 1
      }
      a
    }
  }


  // get as of ================================================================================================

  /** Get `Array` of all rows as objects matching molecule as of transaction time `t`.
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
    *   val persons = Person.name.age.getObjArrayAsOf(1028)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 42
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of data as of transaction t 1031 (after update)
    *   val persons = Person.name.age.getObjArrayAsOf(1031)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of all rows as of transaction t 1032 (after retract)
    *   val persons = Person.name.age.getObjArrayAsOf(1032)
    *   persons(0).name === "Liz"
    *   persons(0).age  === 37
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the typed data set.
    *
    * @group getObjArrayAsOf
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(t:Long)* getAsyncArrayAsOf]] method.
    */
  def getObjArrayAsOf(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(AsOf(TxLong(t))), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule as of transaction time `t`.
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
    *   val persons = Person.name.age.getObjArrayAsOf(1031)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of n rows as of transaction t 1031 (after update)
    *   val persons = Person.name.age.getObjArrayAsOf(1031)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArrayAsOf
    * @param t       Long Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(t:Long,n:Int)* getAsyncArrayAsOf]] method.
    */
  def getObjArrayAsOf(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(AsOf(TxLong(t))), objType, tplType)


  /** Get `Array` of all rows as objects matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve
    * a database value as of that transaction (including).
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
    *   // Get Array of all rows as of tx1 (after insert)
    *   val persons = Person.name.age.getObjArrayAsOf(tx1)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 42
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of all rows as of tx2 (after update)
    *   val persons = Person.name.age.getObjArrayAsOf(tx2)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of all rows as of tx3 (after retract)
    *   val persons = Person.name.age.getObjArrayAsOf(tx3)
    *   persons(0).name === "Liz"
    *   persons(0).age  === 37
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncArrayAsOf]] method.
    **/
  def getObjArrayAsOf(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(AsOf(TxLong(tx.t))), objType, tplType)
  // Fully qualifying tx parameter (and other parameters below) for Scala Docs to be able to pick it up
  // when referencing the method from the asynchronous cousin methods.

  /** Get `Array` of n rows as objects matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database
    * value as of that transaction (including).
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
    *   // Get Array of all rows as of tx2 (after update)
    *   val persons = Person.name.age.getObjArrayAsOf(tx2)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of n rows as of tx2 (after update)
    *   val persons = Person.name.age.getObjArrayAsOf(tx2, 1)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncArrayAsOf]] method.
    **/
  def getObjArrayAsOf(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(AsOf(TxLong(tx.t))), objType, tplType)


  /** Get `Array` of all rows as objects matching molecule as of date.
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
    *   Person.name.age.getObjArrayAsOf(beforeInsert) === Array()
    *
    *   // Get Array of all rows as of afterInsert
    *   val persons = Person.name.age.getObjArrayAsOf(afterInsert)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 42
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of all rows as of afterUpdate
    *   val persons = Person.name.age.getObjArrayAsOf(afterUpdate)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of all rows as of afterRetract
    *   val persons = Person.name.age.getObjArrayAsOf(afterRetract)
    *   persons(0).name === "Liz"
    *   persons(0).age  === 37
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArrayAsOf
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(date:java\.util\.Date)* getAsyncArrayAsOf]] method.
    */
  def getObjArrayAsOf(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(AsOf(TxDate(date))), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule as of date.
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
    *   val persons = Person.name.age.getObjArrayAsOf(afterUpdate)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    *   persons(1).name === "Liz"
    *   persons(1).age  === 37
    *
    *   // Get Array of n rows as of afterUpdate
    *   val persons = Person.name.age.getObjArrayAsOf(afterUpdate, 1)
    *   persons(0).name === "Ben"
    *   persons(0).age  === 43 // <-- updated
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArrayAsOf
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayAsOf(date:java\.util\.Date,n:Int)* getAsyncArrayAsOf]] method.
    */
  def getObjArrayAsOf(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(AsOf(TxDate(date))), objType, tplType)


  // get since ================================================================================================

  /** Get `Array` of all rows as objects matching molecule since transaction time `t`.
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
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1
    *   val persons = Person.name.getObjArraySince(t1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Cay added since transaction time t2
    *   val persons = Person.name.getObjArraySince(t2)
    *   persons(0).name === "Cay"
    *
    *   // Nothing added since transaction time t3
    *   Person.name.getObjArraySince(t3) === Array()
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArraySince
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(t:Long)* getAsyncArraySince]] method.
    */
  def getObjArraySince(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(Since(TxLong(t))), objType, tplType)

  /** Get `Array` of n rows as objects matching molecule since transaction time `t`.
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
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1
    *   val persons = Person.name.getObjArraySince(t1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1 - only n (1) rows returned
    *   val persons = Person.name.getObjArraySince(t1, 1)
    *   persons(0).name === "Ben"
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArraySince
    * @param t       Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(t:Long,n:Int)* getAsyncArraySince]] method.
    */
  def getObjArraySince(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(Since(TxLong(t))), objType, tplType)


  /** Get `Array` of all rows as objects matching molecule since tx.
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
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since transaction time tx1
    *   val persons = Person.name.getObjArraySince(tx1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Cay added since transaction time tx2
    *   val persons = Person.name.getObjArraySince(tx2)
    *   persons(0).name === "Cay"
    *
    *   // Nothing added since transaction time tx3
    *   Person.name.getObjArraySince(tx3) === Array()
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncArraySince]] method.
    */
  def getObjArraySince(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(Since(TxLong(tx.t))), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule since tx.
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
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since tx1
    *   val persons = Person.name.getObjArraySince(tx1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   val persons = Person.name.getObjArraySince(tx1, 1)
    *   persons(0).name === "Ben"
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncArraySince]] method.
    **/
  def getObjArraySince(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(Since(TxLong(tx.t))), objType, tplType)


  /** Get `Array` of all rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since date1
    *   val persons = Person.name.getObjArraySince(date1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Cay added since transaction time date2
    *   val persons = Person.name.getObjArraySince(date2)
    *   persons(0).name === "Cay"
    *
    *   // Nothing added since transaction time date3
    *   Person.name.getObjArraySince(date3) === Array()
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArraySince
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(date:java\.util\.Date)* getAsyncArraySince]] method.
    */
  def getObjArraySince(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(Since(TxDate(date))), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   val persons = Person.name.getObjArray
    *   persons(0).name === "Ann"
    *   persons(1).name === "Ben"
    *   persons(2).name === "Cay"
    *
    *   // Ben and Cay added since date1
    *   val persons = Person.name.getObjArraySince(date1)
    *   persons(0).name === "Ben"
    *   persons(1).name === "Cay"
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   val persons = Person.name.getObjArraySince(date1, 1)
    *   persons(0).name === "Ben"
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArraySince
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArraySince(date:java\.util\.Date,n:Int)* getAsyncArraySince]] method.
    */
  def getObjArraySince(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(Since(TxDate(date))), objType, tplType)


  // get with ================================================================================================

  /** Get `Array` of all rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   val persons = Person.name.likes.getObjArray
    *   persons(0).name  === "Ben"
    *   persons(0).likes === "pasta"
    *
    *   // Test adding transaction data
    *   val personsTest = Person.name.likes.getObjArrayWith(
    *     // Additional transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   )
    *
    *   // Effect: Ben would like sushi if tx was applied
    *   personTest(0).name  === "Ben"
    *   personTest(0).likes === "sushi"
    *
    *   // Current state is still the same
    *   val personsAfter = Person.name.likes.getObjArray
    *   personsAfter(0).name  === "Ben"
    *   personsAfter(0).likes === "pasta"
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArrayWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayWith(txMolecules* getAsyncArrayWith]] method.
    */
  def getObjArrayWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(With(toJavaList(txMolecules.flatten.flatten.map(_.toJava)))), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val List(ben, liz) = Person.name.likes.insert(
    *     ("Ben", "pasta"),
    *     ("Liz", "pizza")
    *   ).eids
    *
    *   // Current state
    *   val persons = Person.name.likes.getObjArray
    *   persons(0).name  === "Ben"
    *   persons(0).likes === "pasta"
    *   persons(1).name  === "Liz"
    *   persons(1).likes === "pizza"
    *
    *   // Test adding transaction data
    *   val personsTest = Person.name.likes.getObjArrayWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   )
    *   // Effect: sushi and cake now liked
    *   personsTest(0).name  === "Ben"
    *   personsTest(0).likes === "sushi"
    *   personsTest(1).name  === "Liz"
    *   personsTest(1).likes === "cake"
    *
    *   // Same as above, but only n (1) rows returned:
    *   val personsTest1 = Person.name.likes.getObjArrayWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   )
    *   // Effect: sushi and cake now liked (but only Ben returned)
    *   personsTest1(0).name  === "Ben"
    *   personsTest1(0).likes === "sushi"
    *
    *   // Current state is still the same
    *   val personsAfter = Person.name.likes.getObjArray
    *   personsAfter(0).name  === "Ben"
    *   personsAfter(0).likes === "pasta"
    *   personsAfter(1).name  === "Liz"
    *   personsAfter(1).likes === "pizza"
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArrayWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayWith(n:Int,txMolecules* getAsyncArrayWith]] method.
    */
  def getObjArrayWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(With(toJavaList(txMolecules.flatten.flatten.map(_.toJava)))), objType, tplType)


  /** Get `Array` of all rows as objects matching molecule with applied raw transaction data.
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
    *   Person.name.getObjArrayWith(newDataTx).size === 250
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getObjArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayWith(txData:java\.util\.List[_])* getAsyncArrayWith]] method.
    */
  def getObjArrayWith(txData: jList[_])(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), objType, tplType)


  /** Get `Array` of n rows as objects matching molecule with applied raw transaction data.
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
    *   Person.name.getObjArrayWith(newDataTx).size === 250
    *
    *   // Imagine future db - Let's just take 10
    *   Person.name.getObjArrayWith(newDataTx, 10).size === 10
    * }}}
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getObjArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return Array[Obj] where Obj is an object of data matching molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjArray.getAsyncObjArrayWith(txData:java\.util\.List[_],n:Int)* getAsyncArrayWith]] method.
    */
  def getObjArrayWith(txData: jList[_], n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Array[Obj] =
    getObjArray(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])), objType, tplType)


  // get history ================================================================================================

  // Only `getHistory` (returning a List) is implemented since it is only meaningful
  // to track the history of one attribute at a time and a sortable List is therefore preferred.
}
