package molecule.core.api.getAsyncObj

import java.util.Date
import molecule.core.api.Molecule_0
import molecule.core.api.getObj.GetObjList
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions


/** Default asynchronous data getter methods on molecules returning `Future[List[Obj]]`.
  * <br><br>
  * For expected smaller result sets it's convenient to return Lists of objects of data.
  * {{{
  *   for {
  *     persons <- Person.name.age.getAsyncObjList
  *   } yield {
  *     persons.map(p => s"${p.name} is ${p.age} years old")) === List(
  *       "Ben is 42 years old",
  *       "Liz is 37 years old"
  *     )
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter in a Future.
  * `getAsyncObjListAsOf` thus wraps the result of `getObjListAsOf` in a Future and so on.
  * */
trait GetAsyncObjList[Obj, Tpl] { self: Molecule_0[Obj, Tpl] with GetAsyncObjArray[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `List` of all rows as objects matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjList(implicit* get]] method.
    *
    * @group getAsync
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjList(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjList(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjList(n:Int)* get]] method.
    *
    * @group getAsync
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjList(n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjList(n)(conn))


  // get as of ================================================================================================

  /** Get `Future` with `List` of all rows as objects matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(t:Long)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListAsOf(t: Long)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(t)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(t:Long,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param t    Long Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListAsOf(t: Long, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(t, n)(conn))


  /** Get `Future` with `List` of all rows as objects matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a
    * database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    * */
  def getAsyncObjListAsOf(tx: TxReport)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(tx.t)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database
    * value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    * */
  def getAsyncObjListAsOf(tx: TxReport, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(tx.t, n)(conn))


  /** Get `Future` with `List` of all rows as objects matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(date:java\.util\.Date)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListAsOf(date: Date)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(date)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListAsOf(date:java\.util\.Date,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListAsOf(date: Date, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListAsOf(date, n)(conn))


  // get since ================================================================================================

  /** Get `Future` with `List` of all rows as objects matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(t:Long)* getSince]] method.
    *
    * @group getAsyncSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(t: Long)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(t)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(t:Long,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(t: Long, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(t, n)(conn))


  /** Get `Future` with `List` of all rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getSince]] method.
    *
    * @group getAsyncSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(tx: TxReport)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(tx.t)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(tx: TxReport, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(tx.t, n)(conn))


  /** Get `Future` with `List` of all rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(date:java\.util\.Date)* getSince]] method.
    *
    * @group getAsyncSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(date: Date)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(date)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListSince(date:java\.util\.Date,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListSince(date: Date, n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListSince(date, n)(conn))


  // get with ================================================================================================

  /** Get `Future` with `List` of all rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListWith(txMolecules* getWith]] method.
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListWith(txMolecules: _*)(conn))


  /** Get `Future` with `List` of n rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListWith(n:Int,txMolecules* getWith]] method.
    *
    * @group getAsyncWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    */
  def getAsyncObjListWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListWith(n, txMolecules: _*)(conn))


  /** Get `Future` with `List` of all rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListWith(txData:java\.util\.List[_])* getWith]] method.
    *
    * @group getAsyncWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListWith(txData: java.util.List[_])(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListWith(txData)(conn))

  /** Get `Future` with `List` of n rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListWith(txData:java\.util\.List[_],n:Int)* getWith]] method.
    *
    * @group getAsyncWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListWith(txData, n)(conn))


  // get history ================================================================================================

  /** Get `Future` with history of operations as `List` on an attribute in the db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetObjList.getObjListHistory(implicit* getHistory]] method.
    *
    * @group getHistory
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjListHistory(implicit conn: Conn): Future[List[Obj]] =
    Future(getObjListHistory(conn))


  // `getAsyncHistory(n: Int)` is not implemented since the whole data set normally needs to be sorted
  // to give chronological meaningful information.
}
