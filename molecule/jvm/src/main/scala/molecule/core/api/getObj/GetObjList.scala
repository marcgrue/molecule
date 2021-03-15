package molecule.core.api.getObj

import java.util.{Date, List => jList}
import molecule.core.api.Molecule_0
import molecule.core.util.{JavaUtil, Quoted}
import molecule.datomic.base.ast.tempDb._
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.collection.mutable.ListBuffer
import scala.language.implicitConversions


/** Data getter methods on molecules that return List[Obj].
  * <br><br>
  * For expected smaller result sets it's convenient to return Lists of objects of data.
  * */
trait GetObjList[Obj, Tpl] extends GetObjArray[Obj, Tpl] with JavaUtil with Quoted { self: Molecule_0[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `List` of all rows as objects matching molecule.
    * {{{
    *   val List(p1, p2) = Person.name.age.getObjList
    *   p1.name === "Ben"
    *   p1.age  === 42
    *   p2.name === "Liz"
    *   p2.age  === 37
    * }}}
    *
    * @group get
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object with properties matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjList(implicit* getAsyncObjList]] method.
    */
  def getObjList(implicit conn: Conn): List[Obj] = {
    val jColl = conn.query(_model, _query)
    val it    = jColl.iterator
    val buf   = new ListBuffer[Obj]
    while (it.hasNext) {
      buf += row2obj(it.next)
    }
    buf.toList
  }


  /** Get `List` of n rows as objects matching molecule.
    * <br><br>
    * Only n rows are type-casted.
    * {{{
    *   val List(p1) = Person.name.age.getObjList(1)
    *   p1.name === "Ben"
    *   p1.age  === 42
    * }}}
    *
    * @group get
    * @param n    Int Number of rows returned. If n == -1, all rows are returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object with properties matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjList(n:Int)* getAsyncObjList]] method.
    */
  def getObjList(n: Int)(implicit conn: Conn): List[Obj] = if (n == -1) {
    getObjList(conn)
  } else {
    val jColl = conn.query(_model, _query)
    val size  = jColl.size
    val max   = if (size < n) size else n
    if (max == 0) {
      List.empty[Obj]
    } else {
      val it  = jColl.iterator
      val buf = new ListBuffer[Obj]
      var i   = 0
      while (it.hasNext && i < max) {
        buf += row2obj(it.next)
        i += 1
      }
      buf.toList
    }
  }


  /** Convenience method to get head of list of objects matching molecule.
    * {{{
    *   val person = Person.name.age.getObj
    *   person.name === "Ben"
    *   person.age  === 42
    * }}}
    *
    * @group get
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object with properties matching the attributes of the molecule
    */
  def getObj(implicit conn: Conn): Obj = getObjList(conn).head


  // get as of ================================================================================================

  /** Get `List` of all rows as objects matching molecule as of transaction time `t`.
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
    *   // Get List of data as of transaction t 1028 (after insert)
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(1028)
    *   a1.name === "Ben"
    *   a1.age  === 42
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of data as of transaction t 1031 (after update)
    *   val List(b1, b2) = Person.name.age.getObjListAsOf(1031)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    *   b2.name === "Liz"
    *   b2.age  === 37
    *
    *   // Get List of all rows as of transaction t 1032 (after retract)
    *   val List(c1) = Person.name.age.getObjListAsOf(1032)
    *   c1.name === "Liz"
    *   c1.age  === 37
    * }}}
    *
    * @group getAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(t:Long)* getAsyncObjListAsOf]] method.
    */
  def getObjListAsOf(t: Long)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `List` of n rows as objects matching molecule as of transaction time `t`.
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
    *   // Get List of all rows as of transaction t 1031 (after update)
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(1031)
    *   a1.name === "Ben"
    *   a1.age  === 43 // <-- updated
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of n rows as of transaction t 1031 (after update)
    *   val List(b1) = Person.name.age.getObjListAsOf(1031)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    * }}}
    *
    * @group getAsOf
    * @param t    Long Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(t:Long,n:Int)* getAsyncObjListAsOf]] method.
    */
  def getObjListAsOf(t: Long, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `List` of all rows as objects matching molecule as of tx.
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
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(tx1)
    *   a1.name === "Ben"
    *   a1.age  === 42
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of all rows as of tx2 (after update)
    *   val List(b1, b2) = Person.name.age.getObjListAsOf(tx2)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    *   b2.name === "Liz"
    *   b2.age  === 37
    *
    *   // Get List of all rows as of tx3 (after retract)
    *   val List(c1) = Person.name.age.getObjListAsOf(tx3)
    *   c1.name === "Liz"
    *   c1.age  === 37
    * }}}
    *
    * @group getAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncObjListAsOf]] method.
    * */
  def getObjListAsOf(tx: TxReport)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `List` of n rows as objects matching molecule as of tx.
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
    *
    *   // Get List of all rows as of tx2 (after update)
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(tx2)
    *   a1.name === "Ben"
    *   a1.age  === 43 // <-- updated
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of n rows as of tx2 (after update)
    *   val List(b1) = Person.name.age.getObjListAsOf(tx2, 1)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    * }}}
    *
    * @group getAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncObjListAsOf]] method.
    * */
  def getObjListAsOf(tx: TxReport, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `List` of all rows as objects matching molecule as of date.
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
    *
    *   // No data yet before insert
    *   Person.name.age.getObjListAsOf(beforeInsert) === List()
    *
    *   // Get List of all rows as of afterInsert
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(afterInsert)
    *   a1.name === "Ben"
    *   a1.age  === 42
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of all rows as of afterUpdate
    *   val List(b1, b2) = Person.name.age.getObjListAsOf(afterUpdate)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    *   b2.name === "Liz"
    *   b2.age  === 37
    *
    *   // Get List of all rows as of afterRetract
    *   val List(c1) = Person.name.age.getObjListAsOf(afterRetract)
    *   c1.name === "Liz"
    *   c1.age  === 37
    * }}}
    *
    * @group getAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(date:java\.util\.Date)* getAsyncObjListAsOf]] method.
    */
  def getObjListAsOf(date: Date)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(AsOf(TxDate(date))))


  /** Get `List` of n rows as objects matching molecule as of date.
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
    *
    *   // Get List of all rows as of afterUpdate
    *   val List(a1, a2) = Person.name.age.getObjListAsOf(afterUpdate)
    *   a1.name === "Ben"
    *   a1.age  === 43 // <-- updated
    *   a2.name === "Liz"
    *   a2.age  === 37
    *
    *   // Get List of n rows as of afterUpdate
    *   val List(b1) = Person.name.age.getObjListAsOf(afterUpdate, 1)
    *   b1.name === "Ben"
    *   b1.age  === 43 // <-- updated
    * }}}
    *
    * @group getAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListAsOf(date:java\.util\.Date,n:Int)* getAsyncObjListAsOf]] method.
    */
  def getObjListAsOf(date: Date, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(AsOf(TxDate(date))))


  // get since ================================================================================================

  /** Get `List` of all rows as objects matching molecule since transaction time `t`.
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
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1
    *   val List(b1, b2) = Person.name.getObjListSince(t1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Cay added since transaction time t2
    *   val List(c1) = Person.name.getObjListSince(t2)
    *   c1.name === "Cay"
    *
    *   // Nothing added since transaction time t3
    *   Person.name.getObjListSince(t3) === List()
    * }}}
    *
    * @group getSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(t:Long)* getAsyncObjListSince]] method.
    */
  def getObjListSince(t: Long)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(Since(TxLong(t))))


  /** Get `List` of n rows as objects matching molecule since transaction time `t`.
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
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1
    *   val List(b1, b2) = Person.name.getObjListSince(t1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Ben and Cay added since transaction time t1 - only n (1) rows returned
    *   val List(c1) = Person.name.getObjListSince(t1, 1)
    *   c1.name === "Ben"
    * }}}
    *
    * @group getSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(t:Long,n:Int)* getAsyncObjListSince]] method.
    */
  def getObjListSince(t: Long, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(Since(TxLong(t))))


  /** Get `List` of all rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsyncObjList a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Get tx reports for 3 transactions
    *   val tx1 = Person.name("Ann").save
    *   val tx2 = Person.name("Ben").save
    *   val tx3 = Person.name("Cay").save
    *
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since transaction time tx1
    *   val List(b1, b2) = Person.name.getObjListSince(tx1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Cay added since transaction time tx2
    *   val List(c1) = Person.name.getObjListSince(tx2)
    *   c1.name === "Cay"
    *
    *   // Nothing added since transaction time tx3
    *   Person.name.getObjListSince(tx3) === List()
    * }}}
    *
    * @group getSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsyncObjListSince]] method.
    */
  def getObjListSince(tx: TxReport)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `List` of n rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsyncObjList a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Get tx reports for 3 transactions
    *   val tx1 = Person.name("Ann").save
    *   val tx2 = Person.name("Ben").save
    *   val tx3 = Person.name("Cay").save
    *
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since tx1
    *   val List(b1, b2) = Person.name.getObjListSince(tx1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   val List(c1) = Person.name.getObjListSince(tx1, 1)
    *   c1.name === "Ben"
    * }}}
    *
    * @group getSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsyncObjListSince]] method.
    */
  def getObjListSince(tx: TxReport, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `List` of all rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since date1
    *   val List(b1, b2) = Person.name.getObjListSince(date1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Cay added since transaction time date2
    *   val List(c1) = Person.name.getObjListSince(date2)
    *   c1.name === "Cay"
    *
    *   // Nothing added since transaction time date3
    *   Person.name.getObjListSince(date3) === List()
    * }}}
    *
    * @group getSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(date:java\.util\.Date)* getAsyncObjListSince]] method.
    */
  def getObjListSince(date: Date)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(Since(TxDate(date))))


  /** Get `List` of n rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time/Date from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *
    *   // Current values
    *   val List(a1, a2, a3) = Person.name.getObjList
    *   a1.name === "Ann"
    *   a2.name === "Ben"
    *   a3.name === "Cay"
    *
    *   // Ben and Cay added since date1
    *   val List(b1, b2) = Person.name.getObjListSince(date1)
    *   b1.name === "Ben"
    *   b2.name === "Cay"
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   val List(c1) = Person.name.getObjListSince(date1, 1)
    *   c1.name === "Ben"
    * }}}
    *
    * @group getSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListSince(date:java\.util\.Date,n:Int)* getAsyncObjListSince]] method.
    */
  def getObjListSince(date: Date, n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(Since(TxDate(date))))


  // get with ================================================================================================

  /** Get `List` of all rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   val List(p0) = Person.name.likes.getObjList
    *   p0.name  === "Ben"
    *   p0.likes === "pasta"
    *
    *   // Test adding transaction data
    *   val List(pTest) = Person.name.likes.getObjListWith(
    *     // Additional transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   )
    *   // Effect: Ben would like sushi if tx was applied
    *   pTest.name  === "Ben"
    *   pTest.likes === "sushi"
    *
    *   // Current state is still the same
    *   val List(pAfter) = Person.name.likes.getObjList
    *   pAfter.name  === "Ben"
    *   pAfter.likes === "pasta"
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListWith(txMolecules* getAsyncObjListWith]] method.
    */
  def getObjListWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(With(toJavaList(txMolecules.flatten.flatten.map(_.toJava)))))


  /** Get `List` of n rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val List(ben, liz) = Person.name.likes.insert(
    *     ("Ben", "pasta"),
    *     ("Liz", "pizza")
    *   ).eids
    *   val List(p1, p2) = Person.name.likes.getObjList
    *   p1.name  === "Ben"
    *   p1.likes === "pasta"
    *   p2.name  === "Liz"
    *   p2.likes === "pizza"
    *
    *
    *   // Test multiple transactions
    *   val List(testP1, testP2) = Person.name.likes.getObjListWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   )
    *   // Effect: sushi and cake now liked
    *   testP1.name  === "Ben"
    *   testP1.likes === "sushi"
    *   testP2.name  === "Liz"
    *   testP2.likes === "cake"
    *
    *   // Same as above, but only n (1) rows returned:
    *   val List(oneTestP) = Person.name.likes.getObjListWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   )
    *   // Effect: sushi and cake now liked (but only Ben returned)
    *   oneTestP.name  === "Ben"
    *   oneTestP.likes === "sushi"
    *
    *   // Current state is still the same
    *   val List(p3, p4) = Person.name.likes.getObjList
    *   p3.name  === "Ben"
    *   p3.likes === "pasta"
    *   p4.name  === "Liz"
    *   p4.likes === "pizza"
    * }}}
    *
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    * @group getWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListWith(n:Int,txMolecules* getAsyncObjListWith]] method.
    */
  def getObjListWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(With(toJavaList(txMolecules.flatten.flatten.map(_.toJava)))))


  /** Get `List` of all rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Live size of Person db
    *   Person.name.get.size === 150
    *
    *   // Read some transaction data from file
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val newDataTx = Util.readAll(data_rdr2).getObjList(0).asInstanceOf[java.util.List[Object]]
    *
    *   // Imagine future db - 100 persons would be added, apparently
    *   Person.name.getWith(newDataTx).size === 250
    * }}}
    *
    * @group getWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListWith(txData:java\.util\.List[_])* getAsyncObjListWith]] method.
    */
  def getObjListWith(txData: java.util.List[_])(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  /** Get `List` of n rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Live size of Person db
    *   Person.name.get.size === 150
    *
    *   // Read some transaction data from file
    *   val data_rdr2 = new FileReader("examples/resources/seattle/seattle-data1a.dtm")
    *   val newDataTx = Util.readAll(data_rdr2).getObjList(0).asInstanceOf[java.util.List[Object]]
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
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListWith(txData:java\.util\.List[_],n:Int)* getAsyncObjListWith]] method.
    */
  def getObjListWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): List[Obj] =
    getObjList(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  // get history ================================================================================================

  /** Get history of operations as `List` on an attribute in the db.
    * <br><br>
    * Generic datom attributes that can be called when `getHistory` is called:
    * <br>
    * <br> `e` - Entity id
    * <br> `a` - Attribute name
    * <br> `v` - Attribute value
    * <br> `ns` - Namespace name
    * <br> `tx` - [[molecule.datomic.base.facade.TxReport TxReport]]
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
    *   Person(ben).age.t.op.getObjListHistory
    *     .sortBy(o => (o.t, o.op))
    *     .map(o => s"${o.age} ${o.t} ${o.op}") === List(
    *     "42 1028 true",  // Insert:  42 asserted
    *     "42 1031 false", // Update:  42 retracted
    *     "43 1031 true",  //          43 asserted
    *     "43 1032 false"  // Retract: 43 retracted
    *   )
    * }}}
    *
    * @group getHistory
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Obj] where Obj is an object type having property types matching the attributes of the molecule
    * @see Equivalent asynchronous [[molecule.core.api.getAsyncObj.GetAsyncObjList.getAsyncObjListHistory(implicit* getAsyncObjListHistory]] method.
    */
  def getObjListHistory(implicit conn: Conn): List[Obj] =
    getObjList(conn.usingTempDb(History))

  // `getHistory(n: Int)` is not implemented since the whole data set normally needs to be sorted
  // to give chronological meaningful information.
}
