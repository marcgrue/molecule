package molecule.core.api.get

import java.util.{Collections, Collection => jCollection, List => jList}
import java.util
import molecule.core.ast.MoleculeBase
import molecule.core.ast.tempDb._
import molecule.core.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.jdk.CollectionConverters._
import scala.language.implicitConversions


/** Data getter methods on molecules that return raw untyped Datomic data.
  * <br><br>
  * Returns raw untyped `java.util.Collection[java.util.List[Object]]` directly from Datomic and is
  * therefore the fastest (but untyped) way of retrieving data. Can be useful where typed data is
  * not needed.
  * */
trait GetRaw { self: MoleculeBase =>


  // get ================================================================================================

  /** Get `java.util.Collection` of all untyped rows matching molecule.
    * {{{
    *   Person.name.age.getRaw.toString === """[["Ben" 42], ["Liz" 37]]"""
    * }}}
    *
    * Peer returns java.util.HashSet
    * Client returns clojure.lang.PersistentVector
    * We unify the type by copying to an ArrayList
    *
    * @group get
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRaw(implicit* getAsyncRaw]] method.
    */
  def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] =
    conn.query(_model, _query)


  /** Get `java.util.Collection` of n untyped rows matching molecule.
    * {{{
    *   Person.name.age.getRaw(1).toString === """[["Ben" 42]]"""
    * }}}
    *
    * @group get
    * @param n    Number of rows
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRaw(n:Int)* getAsyncRaw]] method.
    */
  def getRaw(n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] = if (n == -1) {
    getRaw(conn)
  } else {
    val jColl = conn.query(_model, _query)
    val size = jColl.size
    val max = if (size < n) size else n
    val it = jColl.iterator
    val a = new java.util.ArrayList[jList[AnyRef]](max)
    var i = 0
    while (it.hasNext && i < max) {
      a.add(it.next)
      i += 1
    }
    a
  }


  // get as of ================================================================================================

  /** Get `java.util.Collection` of all untyped rows matching molecule as of transaction time `t`.
    * <br><br>
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
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
    *   // Get raw data as of transaction t 1028 (after insert)
    *   Person.name.age.getRawAsOf(1028).toString === """[["Ben" 42], ["Liz" 37]]"""
    *
    *   // Get raw data as of transaction t 1031 (after update)
    *   Person.name.age.getRawAsOf(1031).toString === """[["Ben" 43], ["Liz" 37]]""" // Ben now 43
    *
    *   // Get raw data as of transaction t 1032 (after retract)
    *   Person.name.age.getRawAsOf(1032).toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @group getRawAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(t:Long)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(t: Long)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `java.util.Collection` of n untyped rows matching molecule as of transaction time `t`.
    * <br><br>
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
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
    *   // Get all rows of raw data as of transaction t 1031 (after update)
    *   Person.name.age.getRawAsOf(1028).toString === """[["Ben" 43], ["Liz" 37]]"""
    *
    *   // Get n (1) rows of raw data as of transaction t 1031 (after update)
    *   Person.name.age.getRawAsOf(1031, 1).toString === """[["Ben" 43]]"""
    * }}}
    *
    * @group getRawAsOf
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(t:Long,n:Int)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(t: Long, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get `java.util.Collection` of all untyped rows matching molecule as of tx.
    *
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Insert (tx report 1)
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    *   val ben = tx1.eid
    *
    *   // Update (tx report 2)
    *   val tx2 = Person(ben).age(43).update
    *
    *   // Retract (tx report 3)
    *   val tx3 = ben.retract
    *
    *   // Get raw data as of tx1 (after insert)
    *   Person.name.age.getRawAsOf(tx1).toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get raw data as of tx2 (after update)
    *   Person.name.age.getRawAsOf(tx2).toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43
    *
    *   // Get raw data as of tx3 (after retract)
    *   Person.name.age.getRawAsOf(tx3).toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @group getRawAsOf
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(tx:molecule\.facade\.TxReport)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(tx: TxReport)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `java.util.Collection` of n untyped rows matching molecule as of tx.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * getAsync a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * {{{
    *   // Insert (tx report 1)
    *   val tx1 = Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    *   val ben = tx1.eid
    *
    *   // Update (tx report 2)
    *   val tx2 = Person(ben).age(43).update
    *
    *   // Retract (tx report 3)
    *   val tx3 = ben.retract
    *
    *   // Get all rows of raw data as of tx1 (after insert)
    *   Person.name.age.getRawAsOf(tx1).toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get n (1) rows of raw data as of tx1 (after insert)
    *   Person.name.age.getRawAsOf(tx1, 1).toString === """[["Liz" 37]]"""
    * }}}
    *
    * @group getRawAsOf
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(tx:molecule\.facade\.TxReport,n:Int)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(tx: TxReport, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get `java.util.Collection` of all untyped rows matching molecule as of date.
    * <br><br>
    * Call getRawAsOf when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
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
    *   val ben = tx1.eid
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
    *   // Get raw data as of beforeInsert
    *   Person.name.age.getRawAsOf(beforeInsert).toString === """[]"""
    *
    *   // Get raw data as of afterInsert
    *   Person.name.age.getRawAsOf(afterInsert).toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get raw data as of afterUpdate
    *   Person.name.age.getRawAsOf(afterUpdate).toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43
    *
    *   // Get raw data as of afterRetract
    *   Person.name.age.getRawAsOf(afterRetract).toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @group getRawAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(date:java\.util\.Date)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(date: java.util.Date)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(AsOf(TxDate(date))))


  /** Get `java.util.Collection` of n untyped rows matching molecule as of date.
    * <br><br>
    * Call getRawAsOf when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
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
    *   val ben = tx1.eid
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
    *   // Get all rows of raw data as of beforeInsert
    *   Person.name.age.getRawAsOf(beforeInsert).toString === """[]"""
    *
    *   // Get all rows of raw data as of afterInsert
    *   Person.name.age.getRawAsOf(afterInsert).toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get n (1) rows of raw data as of afterInsert
    *   Person.name.age.getRawAsOf(afterInsert, 1).toString === """[["Liz" 37]]"""
    * }}}
    *
    * @group getRawAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawAsOf(date:java\.util\.Date,n:Int)* getAsyncRawAsOf]] method.
    */
  def getRawAsOf(date: java.util.Date, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(AsOf(TxDate(date))))


  // get since ================================================================================================

  /** Get `java.util.Collection` of all untyped rows matching molecule since transaction time `t`.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction):
    * {{{
    *   // 3 transaction times `t`
    *   val t1 = Person.name("Ann").save.t
    *   val t2 = Person.name("Ben").save.t
    *   val t3 = Person.name("Cay").save.t
    *
    *   // Current values as Iterable
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t1
    *   Person.name.getRawSince(t1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since transaction time t2
    *   Person.name.getRawSince(t2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since transaction time t3
    *   Person.name.getRawSince(t3).toString === """[]"""
    * }}}
    *
    * @group getRawSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(t:Long)* getAsyncRawSince]] method.
    */
  def getRawSince(t: Long)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(Since(TxLong(t))))


  /** Get `java.util.Collection` of n untyped rows matching molecule since transaction time `t`.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction):
    * {{{
    *   // 3 transaction times `t`
    *   val t1 = Person.name("Ann").save.t
    *   val t2 = Person.name("Ben").save.t
    *   val t3 = Person.name("Cay").save.t
    *
    *   // Current values as Iterable
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t1
    *   Person.name.getRawSince(t1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Ben and Cay added since transaction time t1 - only n (1) rows returned
    *   Person.name.getRawSince(t1).toString === """[["Ben"]]"""
    * }}}
    *
    * @group getRawSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(t:Long,n:Int)* getAsyncRawSince]] method.
    */
  def getRawSince(t: Long, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(Since(TxLong(t))))


  /** Get `java.util.Collection` of all untyped rows matching molecule since tx.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database value since that transaction (excluding).
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
    *   Person.name.getRawSince(tx1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since tx2
    *   Person.name.getRawSince(tx2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since tx3
    *   Person.name.getRawSince(tx3).toString === """[]"""
    * }}}
    *
    * @group getRawSince
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(tx:molecule\.facade\.TxReport)* getAsyncRawSince]] method.
    */
  def getRawSince(tx: TxReport)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `java.util.Collection` of n untyped rows matching molecule since tx.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database value since that transaction (excluding).
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
    *   Person.name.getRawSince(tx1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Ben and Cay added since tx1 - only n (1) rows returned
    *   Person.name.getRawSince(tx1, 1).toString === """[["Ben"]]"""
    * }}}
    *
    * @group getRawSince
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(tx:molecule\.facade\.TxReport,n:Int)* getAsyncRawSince]] method.
    */
  def getRawSince(tx: TxReport, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get `java.util.Collection` of all untyped rows matching molecule since date.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since date1
    *   Person.name.getRawSince(date1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since date2
    *   Person.name.getRawSince(date2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since date3
    *   Person.name.getRawSince(date3).toString === """[]"""
    * }}}
    *
    * @group getRawSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(date:java\.util\.Date)* getAsyncRawSince]] method.
    */
  def getRawSince(date: java.util.Date)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(Since(TxDate(date))))


  /** Get `java.util.Collection` of n untyped rows matching molecule since date.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   Person.name.get === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since date1
    *   Person.name.getRawSince(date1).toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Ben and Cay added since date1 - only n (1) rows returned
    *   Person.name.getRawSince(date1, 1).toString === """[["Ben"]]"""
    * }}}
    *
    * @group getRawSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawSince(date:java\.util\.Date,n:Int)* getAsyncRawSince]] method.
    */
  def getRawSince(date: java.util.Date, n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(Since(TxDate(date))))


  // get with ================================================================================================

  /** Get `java.util.Collection` of all untyped rows matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.likes.getRawWith(
    *     Person(ben).likes("sushi").getUpdateTx
    *   ).toString ==== """[["Ben" "sushi"]]"""
    *
    *   // Current state is still the same
    *   Person.name.likes.get === List(("Ben", "pasta"))
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getRawWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawWith(txMolecules* getAsyncRawWith]] method.
    */
  def getRawWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get `java.util.Collection` of n untyped rows matching molecule with applied molecule transaction data.
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
    *   Person.name.likes.getRawWith(
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ).toString === """[["Ben" "sushi"], ["Liz" "cake"]]"""
    *
    *   // Same as above, but only n (1) rows returned:
    *   Person.name.likes.getRawWith(
    *     1
    *     Person(ben).likes("sushi").getUpdateTx,
    *     Person(liz).likes("cake").getUpdateTx
    *   ).toString === """[["Ben" "sushi"]]"""
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getRawWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawWith(n:Int,txMolecules* getAsyncRawWith]] method.
    */
  def getRawWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get `java.util.Collection` of all untyped rows matching molecule with applied raw transaction data.
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
    *   Person.name.getRawWith(newDataTx).size === 250
    * }}}
    *
    * @group getRawWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawWith(txData:java\.util\.List[_])* getAsyncRawWith]] method.
    */
  def getRawWith(txData: java.util.List[_])(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  /** Get `java.util.Collection` of n untyped rows matching molecule with applied raw transaction data.
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
    *   Person.name.getRawWith(newDataTx).size === 250
    *
    *   // Imagine future db - Let's just take 10
    *   Person.name.getRawWith(newDataTx, 10).size === 10
    * }}}
    *
    * @group getRawWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    * @see Equivalent asynchronous [[molecule.core.api.getAsync.GetAsyncRaw.getAsyncRawWith(txData:java\.util\.List[_],n:Int)* getAsyncRawWith]] method.
    */
  def getRawWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): jCollection[jList[AnyRef]] =
    getRaw(n)(conn.usingTempDb(With(txData.asInstanceOf[jList[jList[_]]])))


  // get history ================================================================================================

  // Only `getHistory` (returning a List) is implemented since it is only meaningful
  // to track the history of one attribute at a time and a sortable List is therefore preferred.
}
