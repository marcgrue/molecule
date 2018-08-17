package molecule.action

import java.util.{Date, Collection => jCollection, List => jList}
import molecule.ast.MoleculeBase
import molecule.ast.model.Model
import molecule.ast.query.Query
import molecule.ast.tempDb._
import molecule.ast.transaction.Statement
import molecule.facade.TxReport
import molecule.facade.Conn
import molecule.ops.VerifyModel
import molecule.transform.Model2Transaction
import molecule.util.Debug
import scala.collection.JavaConverters._

/** Core molecule interface defining actions that can be called on molecules.
  *
  * Groups of interfaces:
  * <table>
  * <tr>
  * <td><b>get</b><td>
  * <td>Get molecule data.</td>
  * </tr>
  * <tr>
  * <td><b>getAsOf</b><td>
  * <td>Get molecule data <i>asOf</i> point in time.</td>
  * </tr>
  * <tr>
  * <td><b>getSince</b><td>
  * <td>Get molecule data <i>since</i> point in time.</td>
  * </tr>
  * <tr>
  * <td><b>getWith</b><td>
  * <td>Get molecule data <i>with</i> given data set.</td>
  * </tr>
  * <tr>
  * <td><b>getHistory</b> &nbsp;&nbsp;&nbsp;<td>
  * <td>Get molecule data from <i>history</i> of database.</td>
  * </tr>
  * <tr>
  * <td><b>debugGet</b><td>
  * <td>Debug getter methods.</td>
  * </tr>
  * <tr>
  * <td><b>getJson</b><td>
  * <td>Get molecule data as json.</td>
  * </tr>
  * <tr>
  * <td><b>save</b><td>
  * <td>Save molecule with applied data.</td>
  * </tr>
  * <tr>
  * <td><b>insert</b><td>
  * <td>Insert multiple rows of data matching molecule.</td>
  * </tr>
  * <tr>
  * <td><b>update</b><td>
  * <td>Update molecule with applied data.</td>
  * </tr>
  * </table>
  *
  * @tparam Tpl Type of molecule (tuple of its attribute types)
  * @see For retract ("delete") methods, see [[molecule.action.EntityOps EntityOps]] and [[molecule.action.Entity Entity]].
  * @see Manual: [[http://www.scalamolecule.org/manual/crud/get/ get]],
  *      [[http://www.scalamolecule.org/manual/time/ time]],
  *      [[http://www.scalamolecule.org/manual/time/asof-since/ asOf/since]],
  *      [[http://www.scalamolecule.org/manual/time/history/ history]],
  *      [[http://www.scalamolecule.org/manual/time/with/ with]],
  *      [[http://www.scalamolecule.org/manual/crud/getjson/ getJson]],
  *      [[http://www.scalamolecule.org/manual/time/testing/ debug/test]],
  *      [[http://www.scalamolecule.org/manual/crud/save/ save]],
  *      [[http://www.scalamolecule.org/manual/crud/insert/ insert]],
  *      [[http://www.scalamolecule.org/manual/crud/update/ update]],
  *      [[http://www.scalamolecule.org/manual/crud/retract/ retract]]
  * @see Tests: [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/attr/Attribute.scala#L1 get]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/GetAsOf.scala#L1 asOf]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/GetSince.scala#L1 since]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/GetHistory.scala#L1 history]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/GetWith.scala#L1 with]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbAsOf.scala#L1 test asOf]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbSince.scala#L1 test since]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/time/TestDbWith.scala#L1 test with]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/json/Attributes.scala#L1 getJson]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/Save.scala#L1 save]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/Insert.scala#L1 insert]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/Retract.scala#L1 retract]],
  * @see [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/update update]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/updateMap update map]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/UpdateRef.scala#L1 update ref]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/UpdateMultipleAttributes.scala#L1 update multiple attributes]],
  *      [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/crud/UpdateMultipleEntities.scala#L1 update multiple entities]]
  * @groupname get get
  * @groupdesc get Get molecule data.
  * @groupprio get 1
  * @groupname asof getAsOf
  * @groupdesc asof Get molecule data ''asOf'' point in time.
  * @groupprio asof 2
  * @groupname since getSince
  * @groupdesc since Get molecule data ''since'' point in time.
  * @groupprio since 3
  * @groupname with getWith
  * @groupdesc with Get molecule data ''with'' given data set.
  * @groupprio with 4
  * @groupname history getHistory
  * @groupdesc history Get molecule data from ''history'' of database.
  * @groupprio history 5
  * @groupname debugGet debugGet
  * @groupdesc debugGet Debug getter methods.
  * @groupprio debugGet 6
  * @groupname json getJson
  * @groupdesc json Get molecule data as json.
  * @groupprio json 7
  * @groupname save save
  * @groupdesc save Save molecule with applied data.
  * @groupprio save 8
  * @groupname insert insert
  * @groupdesc insert Insert multiple rows of data matching molecule.
  * @groupprio insert 9
  * @groupname update update
  * @groupdesc update Update molecule with applied data.
  * @groupprio update 10
  */
trait Molecule[Tpl] extends MoleculeBase {

  protected type Objs = java.util.List[Object]
  protected type Stmtss = Seq[Seq[Statement]]
  protected type Lists = java.util.List[java.util.List[_]]


  // get ------------------------

  /** Get List of data matching the molecule.
    *
    * @group get
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def get(implicit conn: Conn): List[Tpl] = getIterable.to[List]


  /** Get List of n rows of data matching the molecule.
    * <br><br>
    * Only n rows are type-casted.
    *
    * @group get
    * @param n    Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def get(n: Int)(implicit conn: Conn): List[Tpl] = getIterable(n).to[List] // convenience method, still lazily evaluating n elements of IterableLike


  /** Get Iterable of data matching the molecule.
    * <br><br>
    * Rows are lazily type-casted on each call to iterator.next().
    * <br><br>
    * This is the base getter for all the getter variations.
    *
    * @group get
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getIterable(implicit conn: Conn): Iterable[Tpl] = ???


  /** Get Iterable of n rows of data matching the molecule.
    * <br><br>
    * Rows are lazily type-casted on each call to iterator.next().
    *
    * @group get
    * @param n    Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getIterable(n: Int)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn).take(n) // convenience method, lazily evaluating n elements of IterableLike


  /** Get untyped molecule data from Datomic matching the molecule.
    *
    * @group get
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRaw(implicit conn: Conn): jCollection[jList[AnyRef]] = ???


  // get as of ------------------------

  /** Get List of molecule data as of transaction time `t`.
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
    *   Person.name.age.getAsOf(1028) === List(
    *     ("Liz", 37),
    *     ("Ben", 42)
    *   )
    *
    *   // Get List of data as of transaction t 1031 (after update)
    *   Person.name.age.getAsOf(1031) === List(
    *     ("Liz", 37),
    *     ("Ben", 43)
    *   )
    *
    *   // Get List of data as of transaction t 1032 (after retract)
    *   Person.name.age.getAsOf(1032) === List(
    *     ("Liz", 37)
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getAsOf(t: Long)(implicit conn: Conn): List[Tpl] = getIterableAsOf(t).to[List]


  /** Get List of molecule data as of tx.
    * <br><br>
    * Datomic's internal asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    *   // Get List of data as of tx1 (after insert)
    *   Person.name.age.getAsOf(tx1) === List(
    *     ("Liz", 37),
    *     ("Ben", 42)
    *   )
    *
    *   // Get List of data as of tx2 (after update)
    *   Person.name.age.getAsOf(tx2) === List(
    *     ("Liz", 37),
    *     ("Ben", 43) // Ben now 43
    *   )
    *
    *   // Get List of data as of tx3 (after retract)
    *   Person.name.age.getAsOf(tx3) === List(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    * See [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    *
    * @group asof
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getAsOf(tx: TxReport)(implicit conn: Conn): List[Tpl] = getIterableAsOf(tx).to[List]


  /** Get List of molecule data as of date.
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
    *   // No data yet before insert
    *   Person.name.age.getAsOf(beforeInsert) === Nil
    *
    *   // Get List of data as of afterInsert
    *   Person.name.age.getAsOf(afterInsert) === List(
    *     ("Liz", 37),
    *     ("Ben", 42)
    *   )
    *
    *   // Get List of data as of afterUpdate
    *   Person.name.age.getAsOf(afterUpdate) === List(
    *     ("Liz", 37),
    *     ("Ben", 43) // Ben now 43
    *   )
    *
    *   // Get List of data as of afterRetract
    *   Person.name.age.getAsOf(afterRetract) === List(
    *     ("Liz", 37) // Ben gone
    *   )
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getAsOf(date: Date)(implicit conn: Conn): List[Tpl] = getIterableAsOf(date).to[List]


  /** Get Iterable of molecule data as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * Call `getIterableAsOf` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
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
    *   // Get Iterable of data as of transaction t 1028 (after insert)
    *   val iterable1: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(1028)
    *   val iterator1: Iterator[(String, Int)] = iterable1.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator1.next === ("Liz", 37)
    *   iterator1.next === ("Ben", 42)
    *
    *   // Get Iterable of data as of transaction t 1031 (after update)
    *   val iterable2: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(1031)
    *   val iterator2: Iterator[(String, Int)] = iterable2.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator2.next === ("Liz", 37)
    *   iterator2.next === ("Ben", 43) // Ben now 43
    *
    *   // Get Iterable of data as of transaction t 1032 (after retract)
    *   val iterable3: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(1032)
    *   val iterator3: Iterator[(String, Int)] = iterable3.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator3.next === ("Liz", 37)
    *   iterator3.hasNext === false // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableAsOf(t: Long)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get Iterable of molecule data as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    *   // Get Iterable of data as of tx1 (after insert)
    *   val iterable1: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(tx1)
    *   val iterator1: Iterator[(String, Int)] = iterable1.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator1.next === ("Liz", 37)
    *   iterator1.next === ("Ben", 42)
    *
    *   // Get Iterable of data as of tx2 (after update)
    *   val iterable2: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(tx2)
    *   val iterator2: Iterator[(String, Int)] = iterable2.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator2.next === ("Liz", 37)
    *   iterator2.next === ("Ben", 43) // Ben now 43
    *
    *   // Get Iterable of data as of tx3 (after retract)
    *   val iterable3: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(tx3)
    *   val iterator3: Iterator[(String, Int)] = iterable3.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator3.next === ("Liz", 37)
    *   iterator3.hasNext === false // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableAsOf(tx: TxReport)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get Iterable of molecule data as of date.
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
    *   // Get Iterable as of beforeInsert
    *   val iterable0: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(beforeInsert)
    *   val iterator0: Iterator[(String, Int)] = iterable0.iterator
    *   iterator0.hasNext === false // Nothing yet
    *
    *   // Get Iterable as of afterInsert
    *   val iterable1: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(afterInsert)
    *   val iterator1: Iterator[(String, Int)] = iterable1.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator1.next === ("Liz", 37)
    *   iterator1.next === ("Ben", 42)
    *
    *   // Get Iterable as of afterUpdate
    *   val iterable2: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(afterUpdate)
    *   val iterator2: Iterator[(String, Int)] = iterable2.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator2.next === ("Liz", 37)
    *   iterator2.next === ("Ben", 43) // Ben now 43
    *
    *   // Get Iterable as of afterRetract
    *   val iterable3: Iterable[(String, Int)] = Person.name.age.getIterableAsOf(afterRetract)
    *   val iterator3: Iterator[(String, Int)] = iterable3.iterator
    *
    *   // Type casting lazily performed with each call to `next`
    *   iterator3.next === ("Liz", 37)
    *   iterator3.hasNext === false // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableAsOf(date: Date)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(AsOf(TxDate(date))))


  /** Get untyped molecule data as of transaction time `t`.
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
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(1028)
    *   raw1.toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get raw data as of transaction t 1031 (after update)
    *   val raw2: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(1031)
    *   raw2.toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43
    *
    *   // Get raw data as of transaction t 1032 (after retract)
    *   val raw3: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(1032)
    *   raw3.toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawAsOf(t: Long)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(AsOf(TxLong(t))))


  /** Get untyped molecule data as of tx.
    *
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
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
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(tx1)
    *   raw1.toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get raw data as of tx2 (after update)
    *   val raw2: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(tx2)
    *   raw2.toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43
    *
    *   // Get raw data as of tx3 (after retract)
    *   val raw3: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(tx3)
    *   raw3.toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawAsOf(tx: TxReport)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Get untyped molecule data as of date.
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
    *   val raw0: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(beforeInsert)
    *   raw0.toString === """[]"""
    *
    *   // Get raw data as of afterInsert
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(afterInsert)
    *   raw1.toString === """[["Liz" 37], ["Ben" 42]]"""
    *
    *   // Get raw data as of afterUpdate
    *   val raw2: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(afterUpdate)
    *   raw2.toString === """[["Liz" 37], ["Ben" 43]]""" // Ben now 43
    *
    *   // Get raw data as of afterRetract
    *   val raw3: java.util.Collection[java.util.List[AnyRef]] = Person.name.age.getRawAsOf(afterRetract)
    *   raw3.toString === """[["Liz" 37]]""" // Ben gone
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group asof
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawAsOf(date: Date)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(AsOf(TxDate(date))))


  // get since ------------------------

  /** Get List of molecule data since transaction time `t`.
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
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getSince(t: Long)(implicit conn: Conn): List[Tpl] = getIterableSince(t).to[List]


  /** Get List of molecule data since tx.
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
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getSince(tx: TxReport)(implicit conn: Conn): List[Tpl] = getIterableSince(tx).to[List]


  /** Get List of molecule data since date.
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
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getSince(date: Date)(implicit conn: Conn): List[Tpl] = getIterableSince(date).to[List]


  /** Get Iterable of molecule data since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * Call `getIterableSince` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
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
    *   Person.name.getIterable.iterator.toList === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since transaction time t1
    *   Person.name.getIterableSince(t1).iterator.toList === List("Ben", "Cay")
    *
    *   // Cay added since transaction time t2
    *   Person.name.getIterableSince(t2).iterator.toList === List("Cay")
    *
    *   // Nothing added since transaction time t3
    *   Person.name.getIterableSince(t3).iterator.toList === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableSince(t: Long)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(Since(TxLong(t))))


  /** Get Iterable of molecule data since tx.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database
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
    *   Person.name.getIterable.iterator.toList === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since tx1
    *   Person.name.getIterableSince(tx1).iterator.toList === List("Ben", "Cay")
    *
    *   // Cay added since tx2
    *   Person.name.getIterableSince(tx2).iterator.toList === List("Cay")
    *
    *   // Nothing added since tx3
    *   Person.name.getIterableSince(tx3).iterator.toList === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableSince(tx: TxReport)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get Iterable of molecule data since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * {{{
    *   // Transact 3 times (`inst` retrieves transaction time from tx report)
    *   val date1 = Person.name("Ann").save.inst
    *   val date2 = Person.name("Ben").save.inst
    *   val date3 = Person.name("Cay").save.inst
    *
    *   // Current values
    *   Person.name.getIterable.iterator.toList === List("Ann", "Ben", "Cay")
    *
    *   // Ben and Cay added since date1
    *   Person.name.getIterableSince(date1).iterator.toList === List("Ben", "Cay")
    *
    *   // Cay added since date2
    *   Person.name.getIterableSince(date2).iterator.toList === List("Cay")
    *
    *   // Nothing added since date3
    *   Person.name.getIterableSince(date3).iterator.toList === Nil
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `since`
    * @group since
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    */
  def getIterableSince(date: Date)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(Since(TxDate(date))))


  /** Get untyped molecule data since transaction time `t`.
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
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.getRawSince(t1)
    *   raw1.toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since transaction time t2
    *   Person.name.getRawSince(t2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since transaction time t3
    *   Person.name.getRawSince(t3).toString === """[]"""
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group since
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawSince(t: Long)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(Since(TxLong(t))))


  /** Get untyped molecule data since tx.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database value since that transaction (excluding).
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
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.getRawSince(tx1)
    *   raw1.toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since tx2
    *   Person.name.getRawSince(tx2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since tx3
    *   Person.name.getRawSince(tx3).toString === """[]"""
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group since
    * @param tx   [[molecule.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawSince(tx: TxReport)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Get untyped molecule data since date.
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
    *   val raw1: java.util.Collection[java.util.List[AnyRef]] = Person.name.getRawSince(date1)
    *   raw1.toString === """[["Ben"], ["Cay"]]"""
    *
    *   // Cay added since date2
    *   Person.name.getRawSince(date2).toString === """[["Cay"]]"""
    *
    *   // Nothing added since date3
    *   Person.name.getRawSince(date3).toString === """[]"""
    * }}}
    *
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `getAsOf`
    * @group since
    * @param date java.util.Date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawSince(date: Date)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(Since(TxDate(date))))


  // get with ------------------------

  /** Get List of molecule data with applied molecule transaction data.
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
    *   Person.name.likes.get === List(
    *     ("Ben", "pasta")
    *   )
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    *
    * @group with
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List of molecule data
    */
  def getWith(txMolecules: Stmtss*)(implicit conn: Conn): List[Tpl] = getIterableWith(txMolecules: _*).to[List]


  /** Get List of molecule data with applied raw transaction data.
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
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    *
    * @group with
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List of molecule data
    */
  def getWith(txData: Objs)(implicit conn: Conn): List[Tpl] = getIterableWith(txData).to[List]


  /** Get Iterable of molecule data with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.likes.getIterableWith(
    *     // apply imaginary transaction data
    *     Person(ben).likes("sushi").getUpdateTx
    *   ).iterator.toList === List(
    *     // Effect: Ben would like sushi if tx was applied
    *     ("Ben", "sushi")
    *   )
    *
    *   // Current state is still the same
    *   Person.name.likes.get === List(
    *     ("Ben", "pasta")
    *   )
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    * 
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    */
  def getIterableWith(txMolecules: Stmtss*)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get Iterable of molecule data with applied raw transaction data.
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
    *   Person.name.getIterableWith(newDataTx).size === 250
    * }}}
    * 
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] 
    * @group with
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    */
  def getIterableWith(txData: Objs)(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(With(txData.asInstanceOf[Lists])))


  /** Get untyped molecule data with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db to see how it would then look:
    * {{{
    *   // Current state
    *   val ben = Person.name("Ben").likes("pasta").save.eid
    *
    *   // Base data
    *   Person.name.age.getRawWith(
    *     Person(ben).age(2).getUpdateTx
    *   ).toString ==== """[["Ben" 2]]"""
    *
    *   // Current state is still the same
    *   Person.name.age.get === List(("Ben", 1))
    * }}}
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] 
    * @group with
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawWith(txMolecules: Stmtss*)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))


  /** Get untyped molecule data with applied raw transaction data.
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
    *   Person.name.getIterableWith(newDataTx).size === 250
    * }}}
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]]
    * @group with
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawWith(txData: Objs)(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(With(txData.asInstanceOf[Lists])))


  // get history ------------------------

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
    * @see [[http://www.scalamolecule.org/manual/time/history/ manual]] for more info on generic attributes.
    * @group history
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getHistory(implicit conn: Conn): List[Tpl] = getIterableHistory.to[List]


  /** Get history of operations as Iterable on an attribute in the db.
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
    *   // History of Ben as Iterable (converted to List for convenience here)
    *   Person(ben).age.t.op.getIterableHistory.iterator.toList.sortBy(r => (r._2, r._3)) === List(
    *     (42, 1028, true),  // Insert:  42 asserted
    *     (42, 1031, false), // Update:  42 retracted
    *     (43, 1031, true),  //          43 asserted
    *     (43, 1032, false)  // Retract: 43 retracted
    *   )
    * }}}
    * @see [[http://www.scalamolecule.org/manual/time/history/ manual]] for more info on generic attributes.
    * @group history
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data (including generic values)
    */
  def getIterableHistory(implicit conn: Conn): Iterable[Tpl] = getIterable(conn.usingTempDb(History))


  /** Get history of operations as untyped data on an attribute in the db.
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
    *   // History of Ben as raw data
    *   Person(ben).age.t.op.getRawHistory.toString ===
    *     """[[43 1031 true], [42 1028 true], [43 1032 false], [42 1031 false]]"""
    * }}}
    * @see [[http://www.scalamolecule.org/manual/time/history/ manual]] for more info on generic attributes.
    * @group history
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getRawHistory(implicit conn: Conn): jCollection[jList[AnyRef]] = getRaw(conn.usingTempDb(History))


  // debug getters ------------------------

  /** Debug call to `get` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGet(implicit conn: Conn): Unit = ???


  /** Debug call to `getAsOf(t)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(t: Long)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxLong(t))))


  /** Debug call to `getAsOf(tx)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(tx: TxReport)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxLong(tx.t))))


  /** Debug call to `getAsOf(date)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetAsOf(date: Date)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(AsOf(TxDate(date))))


  /** Debug call to `getSince(t)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param t
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(t: Long)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxLong(t))))


  /** Debug call to `getSince(tx)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param tx   [[molecule.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(tx: TxReport)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxLong(tx.t))))


  /** Debug call to `getSince(date)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param date
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetSince(date: Date)(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(Since(TxDate(date))))


  /** Debug call to `getWith(txMolecules)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    * <br><br>
    * 3. Transactions of applied transaction molecules.
    *
    * @group debugGet
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetWith(txMolecules: Stmtss*)(implicit conn: Conn): Unit = {
    debugGet(conn.usingTempDb(With(txMolecules.flatten.flatten.map(_.toJava).asJava)))
    txMolecules.zipWithIndex foreach { case (stmts, i) =>
      Debug(s"Transaction molecule ${i + 1} statements:", 1)(i + 1, stmts)
    }
  }


  /** Debug call to `getWith(txData)` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    * <br><br>
    * 3. Transactions of applied transaction data.
    *
    * @group debugGet
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetWith(txData: Objs)(implicit conn: Conn): Unit = {
    debugGet(conn.usingTempDb(With(txData.asInstanceOf[Lists])))
    println("Transaction data:\n========================================================================")
    txData.toArray foreach println
  }


  /** Debug call to `getHistory` on a molecule (without affecting the db).
    * <br><br>
    * Prints the following to output:
    * <br><br>
    * 1. Internal molecule transformation representations:
    * <br>Molecule DSL --> Model --> Query --> Datomic query
    * <br><br>
    * 2. Data returned from get query (max 500 rows).
    *
    * @group debugGet
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    */
  def debugGetHistory(implicit conn: Conn): Unit = debugGet(conn.usingTempDb(History))


  // Get json ============================================================================================================================

  /** Get data matching molecule as json string.
    * <br><br>
    * Namespace.Attribute is used as json fields. Values are
    * quoted when necessary. Nested data becomes json objects etc.
    *
    * @see [[http://www.scalamolecule.org/manual/crud/getjson/ Manual on getJson]]
    * @group json
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return String of json
    */
  def getJson(implicit conn: Conn): String = ???


  /** Get n rows of data matching molecule as json string.
    * <br><br>
    * Namespace.Attribute is used as json fields. Values are
    * quoted when necessary. Nested data becomes json objects etc.
    *
    * @see [[http://www.scalamolecule.org/manual/crud/getjson/ Manual on getJson]]
    * @group json
    * @param n    Number of rows returned
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return String of json
    */
  def getJson(n: Int)(implicit conn: Conn): String = ???


  // Save ============================================================================================================================

  /** Save data applied to molecule attributes.
    * {{{
    *   val txReport = Person.name("Ben").age(42).save
    *
    *   // Data has been saved in db
    *   Person.name.age.get === List(("Ben", 42))
    * }}}
    * A [[molecule.facade.TxReport TxReport]] is returned with info about the result of the `save` transaction.
    * <br><br>
    * The save operation is synchronous and can be wrapped in a Future if asynchronicity is required.
    *
    * @group save
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return [[molecule.facade.TxReport TxReport]]
    */
  def save(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "save")
    val stmtss = Seq(Model2Transaction(conn, _model).saveStmts())
    conn.transact(stmtss)
  }


  /** Get transaction statements of a call to `save` on a molecule (without affecting the db).
    *
    * @group save
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Transaction statements
    */
  def getSaveTx(implicit conn: Conn): Seq[Seq[Statement]] = {
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.getSaveTx", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }


  /** Debug call to `save` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group save
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return Unit
    */
  def debugSave(implicit conn: Conn) {
    VerifyModel(_model, "save")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.saveStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.debugSave", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.debugSave", 1)(1, _model, transformer.stmtsModel, stmts)
  }


  // Inserts ============================================================================================================================

  /** Insert one or more rows of data matching molecule.
    * <br><br>
    * Data matching the types of the molecule can be inserted either as individual args
    * or an Iterable (List, Set etc) of tuples:
    * {{{
    *   // Insert single row of data with individual args
    *   Person.name.age.insert("Ann", 28)
    *
    *   // Insert multiple rows of data. Accepts Iterable[Tpl]
    *   Person.name.age insert List(
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    *
    *   // Data was inserted
    *   Person.name.age.get.sorted === List(
    *     ("Ann", 28),
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    * }}}
    * Each insert apply method returns a [[molecule.facade.TxReport TxReport]] with info about the result of the transaction.
    * <br><br>
    * Since `insert` is an object of each arity 1-22 Molecule implementation, we can make an "insert-molecule" at compile time
    * that we can re-use for inserting data at runtime matching the molecule type:
    * {{{
    *   // At compiletime:
    *
    *   // Make insert-molecule
    *   val insertPersonsWithAge = Person.name.age.insert
    *
    *   // At runtime:
    *
    *   // Apply individual args matching insert-molecule to insert single row
    *   insertPersonsWithAge("Ann", 28)
    *
    *   // .. or apply Iterable of tuples matching insert-molecule to insert multiple rows
    *   insertPersonsWithAge(
    *     List(
    *       ("Ben", 42),
    *       ("Liz", 37),
    *     )
    *   )
    *
    *   // Data was inserted
    *   Person.name.age.get.sorted === List(
    *     ("Ann", 28),
    *     ("Ben", 42),
    *     ("Liz", 37),
    *   )
    * }}}
    * The insert operation is synchronous and can be wrapped in a Future if asynchronicity is required.
    * <br><br>
    * (This trait is simply a marker being extend to by insert objects in each arity 1-22 Molecule
    * implementation to make this documentation available).
    *
    * @group insert
    */
  trait insert


  /** Get transaction statements of a call to `insert` on a molecule (without affecting the db).
    *
    * @group insert
    * @return Transaction statements
    */
  trait getInsertTx


  /** Debug call to `insert` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group insert
    */
  trait debugInsert


  /** Insert data is verified on instantiation of `insert` object in each arity molecule */
  protected trait checkInsertModel {
    VerifyModel(_model, "insert")
  }

  protected def _insert(conn: Conn, model: Model, dataRows: Iterable[Seq[Any]] = Seq()): TxReport = {
    val stmtss = Model2Transaction(conn, model).insertStmts(dataRows.toSeq)
    conn.transact(stmtss)
  }

  protected def _getInsertTx(conn: Conn, data: Iterable[Seq[Any]]): Seq[Seq[Statement]] = {
    val transformer = Model2Transaction(conn, _model)
    val stmtss: Seq[Seq[Statement]] = try {
      transformer.insertStmts(data.toSeq)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._debugInsert", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    stmtss
  }

  protected def _debugInsert(conn: Conn, data: Iterable[Seq[Any]]) {
    val transformer = Model2Transaction(conn, _model)
    val stmtss = try {
      transformer.insertStmts(data.toSeq)
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule._debugInsert", 1)(1, _model, transformer.stmtsModel, data)
        throw e
    }
    Debug("output.Molecule._debugInsert", 1)(1, _model, transformer.stmtsModel, data, stmtss)
  }


  // Update ============================================================================================================================

  /** Update entity with data applied to molecule attributes.
    * {{{
    *   // Current data
    *   val ben = Person.name("Ben").age(42).save.eid
    *
    *   // Update entity of of Ben with new age value
    *   Person(ben).age(43).update
    *
    *   // Ben is now 43
    *   Person.name.age.get === List(("ben", 43))
    * }}}
    * A [[molecule.facade.TxReport TxReport]] is returned with info about the result of the `update` transaction.
    * <br><br>
    * The update operation is synchronous and can be wrapped in a Future if asynchronicity is required.
    *
    * @group update
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return [[molecule.facade.TxReport TxReport]]
    */
  def update(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "update")
    val stmtss = Seq(Model2Transaction(conn, _model).updateStmts())
    conn.transact(stmtss)
  }


  /** Get transaction statements of a call to `update` on a molecule (without affecting the db).
    *
    * @group update
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return
    */
  def getUpdateTx(implicit conn: Conn): Seq[Seq[Statement]] = {
    VerifyModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.debugUpdate", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }

  /** Debug call to `update` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group update
    * @param conn Implicit [[molecule.facade.Conn Conn]] value in scope
    * @return
    */
  def debugUpdate(implicit conn: Conn) {
    VerifyModel(_model, "update")
    val transformer = Model2Transaction(conn, _model)
    val stmts = try {
      transformer.updateStmts()
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Debug("output.Molecule.debugUpdate", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Debug("output.Molecule.debugUpdate", 1)(1, _model, transformer.stmtsModel, stmts)
  }
}


/** Arity 1-22 molecule implementation interfaces. */
object Molecule {

  abstract class Molecule01[A](val _model: Model, val _query: Query) extends Molecule[A] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): TxReport = _insert(conn, _model, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[A])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(Seq(_)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): Unit = _debugInsert(conn, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[A])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(Seq(_)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): Stmtss = _getInsertTx(conn, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[A])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(Seq(_)))
    }
  }

  abstract class Molecule02[A, B](val _model: Model, val _query: Query) extends Molecule[(A, B)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2)))
    }
  }


  abstract class Molecule03[A, B, C](val _model: Model, val _query: Query) extends Molecule[(A, B, C)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3)))
    }
  }


  abstract class Molecule04[A, B, C, D](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }
  }


  abstract class Molecule05[A, B, C, D, E](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }
  }


  abstract class Molecule06[A, B, C, D, E, F](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }
  }

  abstract class Molecule07[A, B, C, D, E, F, G](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }
  }


  abstract class Molecule08[A, B, C, D, E, F, G, H](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }
  }


  abstract class Molecule09[A, B, C, D, E, F, G, H, I](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }
  }


  abstract class Molecule10[A, B, C, D, E, F, G, H, I, J](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }
  }


  abstract class Molecule11[A, B, C, D, E, F, G, H, I, J, K](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }
  }


  abstract class Molecule12[A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }
  }

  abstract class Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }
  }


  abstract class Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }
  }


  abstract class Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }
  }


  abstract class Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }
  }


  abstract class Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }
  }


  abstract class Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }
  }


  abstract class Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }
  }


  abstract class Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }
  }


  abstract class Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }
  }


  abstract class Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, val _query: Query) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {

    /** See [[molecule.action.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.action.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }

    /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
    object debugInsert extends debugInsert with checkInsertModel {

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Unit = _debugInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.action.Molecule.debugInsert debugInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): Unit = _debugInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }

    /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Stmtss = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): Stmtss = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }
  }
}