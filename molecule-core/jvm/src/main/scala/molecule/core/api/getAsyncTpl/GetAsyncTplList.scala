package molecule.core.api.getAsyncTpl

import java.util.Date
import molecule.core.api.Molecule_0
import molecule.core.api.getTpl.GetTplList
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions


/** Default asynchronous data getter methods on molecules returning `Future[List[Tpl]]`.
  * <br><br>
  * For expected smaller result sets it's convenient to return Lists of tuples of data.
  * Considered as the default getter, no postfix has been added (`getAsync` instead of `getAsyncList`).
  * {{{
  *   val futureList: Future[List[(String, Int)]] = Person.name.age.getAsync
  *   for {
  *     list <- futureList
  *   } yield {
  *     list === List(
  *       ("Ben", 42),
  *       ("Liz", 37)
  *     )
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter (in the
  * `get` package) in a Future. `getAsyncAsOf` thus wraps the result of `getAsOf` in a Future and so on.
  * */
trait GetAsyncTplList[Obj, Tpl] { self: Molecule_0[Obj, Tpl] with GetAsyncTplArray[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `List` of all rows as tuples matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.get(implicit* get]] method.
    * <br><br>
    * Since retrieving a List is considered the default fetch format, the getter method is
    * simply named `get` (and not `getList`).
    *
    * @group getAsync
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsync(implicit conn: Conn): Future[List[Tpl]] =
    Future(get(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.get(n:Int)* get]] method.
    * <br><br>
    * Since retrieving a List is considered the default fetch format, the getter method is
    * simply named `get` (and not `getList`).
    *
    * @group getAsync
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsync(n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(get(n)(conn))


  // get as of ================================================================================================

  /** Get `Future` with `List` of all rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getAsOf(t:Long)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncAsOf(t: Long)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(t)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getAsOf(t:Long,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param t    Long Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncAsOf(t: Long, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(t, n)(conn))


  /** Get `Future` with `List` of all rows as tuples matching molecule as of tx.
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
    * [[GetTplList.getAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    * */
  def getAsyncAsOf(tx: TxReport)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(tx.t)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule as of tx.
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
    * [[GetTplList.getAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    * */
  def getAsyncAsOf(tx: TxReport, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(tx.t, n)(conn))


  /** Get `Future` with `List` of all rows as tuples matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getAsOf(date:java\.util\.Date)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncAsOf(date: Date)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(date)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getAsOf(date:java\.util\.Date,n:Int)* getAsOf]] method.
    *
    * @group getAsyncAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncAsOf(date: Date, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getAsOf(date, n)(conn))


  // get since ================================================================================================

  /** Get `Future` with `List` of all rows as tuples matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(t:Long)* getSince]] method.
    *
    * @group getAsyncSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(t: Long)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(t)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(t:Long,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(t: Long, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(t, n)(conn))


  /** Get `Future` with `List` of all rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getSince]] method.
    *
    * @group getAsyncSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(tx: TxReport)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(tx.t)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(tx: TxReport, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(tx.t, n)(conn))


  /** Get `Future` with `List` of all rows as tuples matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(date:java\.util\.Date)* getSince]] method.
    *
    * @group getAsyncSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(date: Date)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(date)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getSince(date:java\.util\.Date,n:Int)* getSince]] method.
    *
    * @group getAsyncSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncSince(date: Date, n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getSince(date, n)(conn))


  // get with ================================================================================================

  /** Get `Future` with `List` of all rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getWith(txMolecules* getWith]] method.
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getWith(txMolecules: _*)(conn))


  /** Get `Future` with `List` of n rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getWith(n:Int,txMolecules* getWith]] method.
    *
    * @group getAsyncWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    */
  def getAsyncWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getWith(n, txMolecules: _*)(conn))


  /** Get `Future` with `List` of all rows as tuples matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getWith(txData:java\.util\.List[_])* getWith]] method.
    *
    * @group getAsyncWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncWith(txData: java.util.List[_])(implicit conn: Conn): Future[List[Tpl]] =
    Future(getWith(txData)(conn))

  /** Get `Future` with `List` of n rows as tuples matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getWith(txData:java\.util\.List[_],n:Int)* getWith]] method.
    *
    * @group getAsyncWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[List[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): Future[List[Tpl]] =
    Future(getWith(txData, n)(conn))


  // get history ================================================================================================

  /** Get `Future` with history of operations as `List` on an attribute in the db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetTplList.getHistory(implicit* getHistory]] method.
    *
    * @group getHistory
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return List[Tpl] where Tpl is tuple of data matching molecule
    */
  def getAsyncHistory(implicit conn: Conn): Future[List[Tpl]] =
    Future(getHistory(conn))


  // `getAsyncHistory(n: Int)` is not implemented since the whole data set normally needs to be sorted
  // to give chronological meaningful information.
}
