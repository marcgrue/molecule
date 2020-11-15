package molecule.core.api.getAsync

import molecule.core.api.Molecule
import molecule.core.api.get.GetIterable
import molecule.core.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions


/** Asynchronous data getter methods on molecules returning `Future[Iterable[Tpl]]`.
  * <br><br>
  * Suitable for data sets that are lazily consumed.
  * {{{
  *   val iterableFuture: Future[Iterable[(String, Int)]] = Person.name.age.getAsyncIterable
  *   for {
  *     iterable <- iterableFuture
  *   } yield {
  *     iterable.iterator.next === ("Ben" 42)
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter (in the
  * `get` package) in a Future. `getAsyncIterableAsOf` thus wraps the result of `getIterableAsOf` in a Future and so on.
  * */
trait GetAsyncIterable[Tpl] { self: Molecule[Tpl] with GetIterable[Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `Iterable` of all rows as tuples matching the molecule.
    * <br><br>
    * Rows are lazily type-casted on each call to iterator.next().
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterable(implicit* getIterable]] method.
    *
    * @group getAsync
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncIterable(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterable(conn))


  // get as of ================================================================================================

  /** Get `Future` with `Iterable` of all rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * Call `getIterableAsOf` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableAsOf(t:Long)* getIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableAsOf(t: Long)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableAsOf(t)(conn))


  /** Get `Future` with `Iterable` of all rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableAsOf(tx:molecule\.facade\.TxReport)* getIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param tx   [[TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableAsOf(tx: TxReport)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableAsOf(tx.t)(conn))


  /** Get `Future` with `Iterable` of all rows as tuples matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableAsOf(date:java\.util\.Date)* getIterableAsOf]] method.
    *
    * @group getAsyncIterableAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableAsOf(date: java.util.Date)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableAsOf(date)(conn))


  // get since ================================================================================================

  /** Get `Future` with `Iterable` of all rows as tuples matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * Call `getIterableSince` for large result sets to maximize runtime performance.
    * Data is lazily type-casted on each call to `next` on the iterator.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableSince(t:Long)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableSince(t: Long)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableSince(t)(conn))


  /** Get `Future` with `Iterable` of all rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableSince(tx:molecule\.facade\.TxReport)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param tx   [[TxReport TxReport]]
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableSince(tx: TxReport)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableSince(tx.t)(conn))


  /** Get `Future` with `Iterable` of all rows as tuples matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableSince(date:java\.util\.Date)* getIterableSince]] method.
    *
    * @group getAsyncIterableSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable[Tpl] where Tpl is tuple of data matching molecule
    * @see [[http://www.scalamolecule.org/manual/time/asof-since/ Manual]] on `asof`/`since`
    */
  def getAsyncIterableSince(date: java.util.Date)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableSince(date)(conn))


  // get with ================================================================================================

  /** Get `Future` with `Iterable` of all rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetIterable.getIterableWith(txMolecules* getIterableWith]] method.
    * <br><br>
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncIterableWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    */
  def getAsyncIterableWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableWith(txMolecules: _*)(conn))


  /** Get `Future` with `Iterable` of all rows as tuples matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * * <br><br>
    * * For more info and code examples see equivalent synchronous
    * * [[molecule.core.api.get.GetIterable.getIterableWith(txData:java\.util\.List[_])* getIterableWith]] method.
    *
    * @group getAsyncIterableWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return Iterable of molecule data
    * @see [[http://www.scalamolecule.org/manual/time/with/ Manual]] on `with`
    */
  def getAsyncIterableWith(txData: java.util.List[_])(implicit conn: Conn): Future[Iterable[Tpl]] =
    Future(getIterableWith(txData)(conn))


  // get history ================================================================================================

  // Only `getHistory`/`getAsyncHistory` returning List/Future[List] are implemented since it is only meaningful
  // to track the history of one attribute of one entity at a time and a sortable List is therefore preferred.

}
