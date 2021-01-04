package molecule.core.api.getAsync

import java.util.{Collection => jCollection, List => jList}
import molecule.core.api.get.GetRaw
import molecule.core.ast.MoleculeBase
import molecule.core.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions


/** Asynchronous data getter methods on molecules returning raw untyped Datomic data.
  * <br><br>
  * Returns a `Future` with raw untyped `java.util.Collection[java.util.List[Object]]` directly from Datomic and is
  * therefore the fastest (but untyped) way of retrieving data. Can be useful where typed data is
  * not needed.
  * {{{
  *   val rawDataFuture: Future[java.util.Colleciton[java.util.List[Object]] = Person.name.age.getAsyncRaw
  *   for {
  *     rawData <- rawDataFuture
  *   } yield {
  *     rawData.toString === """[["Ben" 42]["Liz" 37]]"""
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter (in the
  * `get` package) in a Future. `getAsyncRawAsOf` thus wraps the result of `getRawAsOf` in a Future and so on.
  * */
trait GetAsyncRaw { self: MoleculeBase with GetRaw =>


  // get ================================================================================================

  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule.
    * {{{
    *   Person.name.age.getRaw.toString === """`[["Ben" 42], ["Liz" 37]]"""
    * }}}
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRaw(implicit* getRaw]] method.
    *
    * @group getAsync
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRaw(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRaw(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule.
    * {{{
    *   Person.name.age.getRaw(1).toString === """[["Ben" 42]]"""
    * }}}
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRaw(n:Int)* getRaw]] method.
    *
    * @group getAsync
    * @param n    Number of rows
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRaw(n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRaw(n)(conn))


  // get as of ================================================================================================

  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule as of transaction time `t`.
    * <br><br>
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(t:Long)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(t: Long)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(t)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule as of transaction time `t`.
    * <br><br>
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(t:Long,n:Int)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(t: Long, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(t, n)(conn))


  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule as of tx.
    *
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(tx: TxReport)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(tx.t)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule as of tx.
    * <br><br>
    * Call `getRawAsOf` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(tx: TxReport, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(tx.t, n)(conn))


  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule as of date.
    * <br><br>
    * Call getRawAsOf when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(date:java\.util\.Date)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(date: java.util.Date)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(date)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule as of date.
    * <br><br>
    * Call getRawAsOf when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawAsOf(date:java\.util\.Date,n:Int)* getRawAsOf]] method.
    *
    * @group getAsyncRawAsOf
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawAsOf(date: java.util.Date, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawAsOf(date, n)(conn))


  // get since ================================================================================================

  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule since transaction time `t`.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(t:Long)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param t    Transaction time t
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(t: Long)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(t)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule since transaction time `t`.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(t:Long,n:Int)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param t    Transaction time t
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(t: Long, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(t, n)(conn))


  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule since tx.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database value since that transaction (excluding).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(tx:molecule\.datomic\.base\.facade\.TxReport)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(tx: TxReport)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(tx.t)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule since tx.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Datomic's internal `since` method can take a transaction entity id as argument to retrieve a database value since that transaction (excluding).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param tx   [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(tx: TxReport, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(tx.t, n)(conn))


  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule since date.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(date:java\.util\.Date)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param date java.util.Date
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(date: java.util.Date)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(date)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule since date.
    * <br><br>
    * Call `getRawSince` when data doesn't need to be type-casted. Datomic's raw data is returned as-is.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawSince(date:java\.util\.Date,n:Int)* getRawSince]] method.
    *
    * @group getAsyncRawSince
    * @param date java.util.Date
    * @param n    Int Number of rows returned
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawSince(date: java.util.Date, n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawSince(date, n)(conn))


  // get with ================================================================================================

  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawWith(txMolecules* getRawWith]] method.
    * <br><br>
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncRawWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawWith(txMolecules: _*)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawWith(n:Int,txMolecules* getRawWith]] method.
    * <br><br>
    * Multiple transactions can be applied to test more complex what-if scenarios!
    *
    * @group getAsyncRawWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    */
  def getAsyncRawWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawWith(n, txMolecules: _*)(conn))


  /** Get `Future` with `java.util.Collection` of all untyped rows matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawWith(txData:java\.util\.List[_])* getRawWith]] method.
    *
    * @group getAsyncRawWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawWith(txData: java.util.List[_])(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawWith(txData)(conn))


  /** Get `Future` with `java.util.Collection` of n untyped rows matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.get.GetRaw.getRawWith(txData:java\.util\.List[_],n:Int)* getRawWith]] method.
    *
    * @group getAsyncRawWith
    * @param txData Raw transaction data as java.util.List[Object]
    * @param n      Int Number of rows returned
    * @param conn   Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return `java.util.Collection[java.util.List[AnyRef]]`
    */
  def getAsyncRawWith(txData: java.util.List[_], n: Int)(implicit conn: Conn): Future[jCollection[jList[AnyRef]]] =
    Future(getRawWith(txData, n)(conn))


  // get history ================================================================================================

  // Only `getHistory`/`getAsyncHistory` returning List/Future[List] are implemented since it is only meaningful
  // to track the history of one attribute of one entity at a time and a sortable List is therefore preferred.

}
