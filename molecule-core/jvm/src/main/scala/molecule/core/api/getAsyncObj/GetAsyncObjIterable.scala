package molecule.core.api.getAsyncObj

import java.util.Date
import molecule.core.api.Molecule_0
import molecule.core.api.getObj.GetObjIterable
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions


/** Asynchronous data getter methods on molecules returning `Future[Iterable[Obj]]`.
  * <br><br>
  * Suitable for data sets that are lazily consumed.
  * {{{
  *   for {
  *     personsIterable <- Person.name.age.getAsyncObjIterable
  *   } yield {
  *     val firstPerson = personsIterable.iterator.next
  *     firstPerson.name === "Ben"
  *     firstPerson.age  === 42
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter in a Future. 
  * `getAsyncObjIterableAsOf` thus wraps the result of `getObjIterableAsOf` in a Future and so on.
  * */
trait GetAsyncObjIterable[Obj, Tpl] { self: Molecule_0[Obj, Tpl] with GetObjIterable[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `Iterable` of all rows as objects matching the molecule.
    * <br><br>
    * Rows are lazily type-casted on each call to iterator.next().
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterable(implicit* getIterable]] method.
    *
    * @group getAsync
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterable(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterable(conn))


  // get as of ================================================================================================

  /** Get `Future` with `Iterable` of all rows as objects matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * Call `getObjIterableAsOf` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableAsOf(t:Long)* getObjIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableAsOf(t: Long)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableAsOf(t)(conn))


  /** Get `Future` with `Iterable` of all rows as objects matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getObjIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableAsOf(tx: TxReport)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableAsOf(tx.t)(conn))


  /** Get `Future` with `Iterable` of all rows as objects matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableAsOf(date:java\.util\.Date)* getObjIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableAsOf(date: Date)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableAsOf(date)(conn))


  // get since ================================================================================================

  /** Get `Future` with `Iterable` of all rows as objects matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * Call `getIterableSince` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableSince(t:Long)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableSince(t: Long)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableSince(t)(conn))


  /** Get `Future` with `Iterable` of all rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableSince(tx: TxReport)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableSince(tx.t)(conn))


  /** Get `Future` with `Iterable` of all rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableSince(date:java\.util\.Date)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `Future[Iterable[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjIterableSince(date: Date)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableSince(date)(conn))


  // get with ================================================================================================

  /** Get `Future` with `Iterable` of all rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjIterable.getObjIterableWith(txMolecules* getIterableWith]] method.
    * <br><br>
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncIterableWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    */
  def getAsyncObjIterableWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableWith(txMolecules: _*)(conn))


  /** Get `Future` with `Iterable` of all rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * * <br><br>
    * * For more info and code examples see equivalent synchronous
    * * [[molecule.core.api.getObj.GetObjIterable.getObjIterableWith(txData:java\.util\.List[_])* getIterableWith]] method.
    *
    * @group getAsyncIterableWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    */
  def getAsyncObjIterableWith(txData: java.util.List[_])(implicit conn: Conn): Future[Iterable[Obj]] =
    Future(getObjIterableWith(txData)(conn))


  // get history ================================================================================================

  // Only `getHistory`/`getAsyncHistory` returning List/Future[List] are implemented since it is only meaningful
  // to track the history of one attribute of one entity at a time and a sortable List is therefore preferred.

}
