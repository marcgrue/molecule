package molecule.core.api.getAsyncObj

import java.util.Date
import molecule.core.api.Molecule_0
import molecule.core.api.getObj.GetObjArray
import molecule.datomic.base.ast.transactionModel.Statement
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.reflect.ClassTag


/** Asynchronous data getter methods on molecules returning `Future[Array[Obj]]`.
  * <br><br>
  * The fastest way of getting a large typed data set since data is applied to a super fast pre-allocated Array.
  * The Array can then be traversed with a fast `while` loop.
  * {{{
  *   Ns.int.insert(1, 2, 3)
  *   
  *   // Map over Future
  *   Ns.int.getAsyncObjArray.map { rowObjects =>
  *     rowObjects.map(_.int) === Array(1, 2, 3)
  *
  *     // Fast while loop
  *     var i = 0
  *     val length = rowObjects.length
  *     while(i < length) {
  *       println(rowObjects(i).int) // Do stuff with row object...
  *       i += 1
  *     }
  *   }
  * }}}
  * Each asynchronous getter in this package simply wraps the result of its equivalent synchronous getter (in the
  * `get` package) in a Future. `getAsyncObjArrayAsOf` thus wraps the result of `getObjArrayAsOf` in a Future and so on.
  * */
trait GetAsyncObjArray[Obj, Tpl] { self: Molecule_0[Obj, Tpl] with GetObjArray[Obj, Tpl] =>


  // get ================================================================================================

  /** Get `Future` with `Array` of all rows as objects matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArray(implicit* getObjArray]] method.
    *
    * @group getAsync
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArray(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArray(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArray(n:Int)* getObjArray]] method.
    *
    * @group getAsync
    * @param n       Number of rows
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArray(n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArray(n)(conn, objType, tplType))


  // get as of ================================================================================================

  /** Get `Future` with `Array` of all rows as objects matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(t:Long)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayAsOf(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule as of transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved in a getHistory call for an attribute and then be
    * used to get data as of that point in time (including that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(t:Long,n:Int)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArrayAsOf
    * @param t       Long Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayAsOf(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as objects matching molecule as of tx.
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
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    **/
  def getAsyncObjArrayAsOf(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(tx.t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule as of tx.
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
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArrayAsOf
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]] (returned from all molecule transaction operations)
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    **/
  def getAsyncObjArrayAsOf(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(tx.t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as objects matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(date:java\.util\.Date)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayAsOf
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayAsOf(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(date)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule as of date.
    * <br><br>
    * Get data at a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayAsOf(date:java\.util\.Date,n:Int)* getObjArrayAsOf]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArrayAsOf
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayAsOf(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayAsOf(date, n)(conn, objType, tplType))


  // get since ================================================================================================

  /** Get `Future` with `Array` of all rows as objects matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(t:Long)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param t       Transaction time t
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArraySince(t: Long)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule since transaction time `t`.
    * <br><br>
    * Transaction time `t` is an auto-incremented transaction number assigned internally by Datomic.
    * <br><br>
    * `t` can for instance be retrieved by calling `t` on the tx report returned from transactional operations
    * and then be used to get data since that point in time (excluding that transaction).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(t:Long,n:Int)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArraySince
    * @param t       Transaction time t
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArraySince(t: Long, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(tx:molecule\.datomic\.base\.facade\.TxReport)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArraySince(tx: TxReport)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(tx.t)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule since tx.
    * <br><br>
    * Datomic's internal `since` can take a transaction entity id as argument to retrieve a database
    * value since that transaction (excluding the transaction itself).
    * <br><br>
    * Instead of supplying the transaction entity id, in Molecule we supply a [[molecule.datomic.base.facade.TxReport TxReport]] that contains
    * the transaction entity id (which is used as argument to Datomic internally). This is more convenient when using Molecule since we
    * get a [[molecule.datomic.base.facade.TxReport TxReport]] from transaction operations like `get`, `update`, `retract` etc.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(tx:molecule\.datomic\.base\.facade\.TxReport,n:Int)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArraySince
    * @param tx      [[molecule.datomic.base.facade.TxReport TxReport]]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    **/
  def getAsyncObjArraySince(tx: TxReport, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(tx.t, n)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(date:java\.util\.Date)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArraySince
    * @param date    java.util.Date
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArraySince(date: Date)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(date)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule since date.
    * <br><br>
    * Get data added/retracted since a human point in time (a java.util.Date).
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArraySince(date:java\.util\.Date,n:Int)* getObjArraySince]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArraySince
    * @param date    java.util.Date
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArraySince(date: Date, n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArraySince(date, n)(conn, objType, tplType))


  // get with ================================================================================================

  /** Get `Future` with `Array` of all rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayWith(txMolecules* getObjArrayWith]] method.
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
    * @param tplType     Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayWith(txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayWith(txMolecules: _*)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule with applied molecule transaction data.
    * <br><br>
    * Apply one or more molecule transactions to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayWith(n:Int,txMolecules* getObjArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArrayWith
    * @param n           Int Number of rows returned
    * @param txMolecules Transaction statements from applied Molecules with test data
    * @param conn        Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType     Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    * @note Note how the `n` parameter has to come before the `txMolecules` vararg.
    */
  def getAsyncObjArrayWith(n: Int, txMolecules: Seq[Seq[Statement]]*)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayWith(n, txMolecules: _*)(conn, objType, tplType))


  /** Get `Future` with `Array` of all rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayWith(txData:java\.util\.List[_])* getObjArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    *
    * @group getAsyncArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayWith(txData: java.util.List[_])(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayWith(txData)(conn, objType, tplType))


  /** Get `Future` with `Array` of n rows as objects matching molecule with applied raw transaction data.
    * <br><br>
    * Apply raw transaction data to in-memory "branch" of db without affecting db.
    * <br><br>
    * For more info and code examples see equivalent synchronous
    * [[molecule.core.api.getObj.GetObjArray.getObjArrayWith(txData:java\.util\.List[_],n:Int)* getObjArrayWith]] method.
    * <br><br>
    * Getting a pre-allocated Array populated with typed data is the fastest way to query
    * Datomic with Molecule. Looping the Array in a while loop with a mutable index pointer will
    * also be the fastest way to traverse the data set.
    * <br><br>
    * The Array is only populated with n rows of type-casted objects.
    *
    * @group getAsyncArrayWith
    * @param txData  Raw transaction data as java.util.List[Object]
    * @param n       Int Number of rows returned
    * @param conn    Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @param tplType Implicit `ClassTag[Obj]` to capture the object type for Array
    * @return `Future[Array[Obj]]` where Obj is an object type having property types matching the attributes of the molecule
    */
  def getAsyncObjArrayWith(txData: java.util.List[_], n: Int)(implicit conn: Conn, objType: ClassTag[Obj], tplType: ClassTag[Tpl]): Future[Array[Obj]] =
    Future(getObjArrayWith(txData, n)(conn, objType, tplType))


  // get history ================================================================================================

  // Only `getHistory`/`getAsyncHistory` returning List/Future[List] are implemented since it is only meaningful
  // to track the history of one attribute of one entity at a time and a sortable List is therefore preferred.

}
