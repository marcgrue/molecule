package molecule.core.api.getAsync

import java.util.Date
import molecule.core.api.Molecule_0
import molecule.core.api.get.GetArray
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.reflect.ClassTag


/** Asynchronous data getter methods on molecules returning `Future[Array[Tpl]]`.
  * <br><br>
  * The fastest way of getting a large typed data set since data is applied to a super fast pre-allocated Array.
  * The Array can then be traversed with a fast `while` loop.
  * {{{
  *   // Map over the Future
  *   Ns.int.getAsyncArray.map { result =>
  *     result === Array(1, 2, 3)
  *
  *     // Fast while loop
  *     var i = 0
  *     val length = result.length
  *     while(i < length) {
  *       println(result(i)) // Do stuff with row...
  *       i += 1
  *     }
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter (in the
  * `get` package) in a Future. `getAsyncArrayAsOf` thus wraps the result of `getArrayAsOf` in a Future and so on.
  * */
trait GetAsyncArray[Obj, Tpl] { self: Molecule_0[Obj, Tpl] with GetArray[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `Array` of all rows as tuples matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArray(implicit* getArray]] method.
    *
    * @group getAsync
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return `Future[Array[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncArray(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArray(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArray(n:Int)* getArray]] method.
    *
    * @group getAsync
    * @param n       Number of rows
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return `Future[Array[Tpl]]` where Tpl is a tuple of types matching the attributes of the molecule
    */
  def getAsyncArray(n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArray(n)(conn, objType, tplType))


  // get as of ================================================================================================

  /** Get `Future` with `Array` of all rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayAsOf(t:Long)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayAsOf(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayAsOf(t:Long,n:Int)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArrayAsOf
    * @param t       Long Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayAsOf(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as tuples matching molecule as of tx.
    * <br><br>
    * Datomic's internal `asOf` method can take a transaction entity id as argument to retrieve
    * a database value as of that transaction (including).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]]
    * that contains the transaction entity id (which is used as argument to Datomic internally). This is more
    * convenient when using Molecule since we get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction
    * operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    **/
  def getAsyncArrayAsOf(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(tx.t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule as of tx.
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
    * [[GetArray.getArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    **/
  def getAsyncArrayAsOf(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(tx.t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as tuples matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayAsOf(date:java\.util\.Date)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayAsOf(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(date)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayAsOf(date:java\.util\.Date,n:Int)* getArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArrayAsOf
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayAsOf(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayAsOf(date, n)(conn, objType, tplType))


  // get since ================================================================================================

  /** Get `Future` with `Array` of all rows as tuples matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(t:Long)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArraySince(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(t:Long,n:Int)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArraySince
    * @param t       Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArraySince(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(tx:molecule\.datomic\.base\.facade\.TxReport)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArraySince(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(tx.t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    **/
  def getAsyncArraySince(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(tx.t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as tuples matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(date:java\.util\.Date)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArraySince(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(date)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArraySince(date:java\.util\.Date,n:Int)* getArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArraySince
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArraySince(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArraySince(date, n)(conn, objType, tplType))


  // get with ================================================================================================

  /** Get `Future` with `Array` of all rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayWith(txMolecules* getArrayWith]] method.
    * <br><br>
    * Multiple transactions can be applied to test more complex what-if scenarios!
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayWith
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayWith(txMolecules: _*)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayWith(n:Int,txMolecules* getArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArrayWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    */
  def getAsyncArrayWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayWith(n, txMolecules: _*)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as tuples matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayWith(txData:java\.util\.List[_])* getArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayWith(txData: java.util.List[_])(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayWith(txData)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as tuples matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[GetArray.getArrayWith(txData:java\.util\.List[_],n:Int)* getArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted tuples.
    *
    * @group getAsyncArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Tpl]` to capture Tuple type for Array
    * @return Array[Tpl] where Tpl is a tuple of data matching molecule
    */
  def getAsyncArrayWith(txData: java.util.List[_], n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Tpl]] =
    Future(getArrayWith(txData, n)(conn, objType, tplType))


  // get history ================================================================================================

  // Only `getHistory`/`getAsyncHistory` returning List/Future[List] are implemented since it is only meaningful
  // to track the history of one attribute of one entity at a time and a sortable List is therefore preferred.

}
