package molecule.action

import molecule.action.get._
import molecule.ast.MoleculeBase
import molecule.ast.model._
import molecule.ast.query.Query
import molecule.ast.transaction.Statement
import molecule.facade.{Conn, TxReport}
import molecule.ops.VerifyModel
import molecule.transform.{CastHelpers, JsonBuilder, Model2Transaction}
import molecule.util.Debug
import scala.language.implicitConversions
import scala.reflect.ClassTag


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
  * <td><b>getHistory</b><td>
  * <td>Get molecule data from <i>history</i> of database.</td>
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
  * <tr>
  * <tr>
  * <td><b>tx</b><td>
  * <td>Molecule transaction data (input to `getWith`).</td>
  * </tr>
  * <tr>
  * <td><b>debug get</b><td>
  * <td>Debug calling get method on molecule.</td>
  * </tr>
  * <tr>
  * <td><b>debug action</b> &nbsp;&nbsp;&nbsp;<td>
  * <td>Debug calling save/insert/update method on molecule.</td>
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
  *
  * @groupname get get
  * @groupprio get 1
  *
  * @groupname asof `getAsOf``
  * @groupprio asof 11
  * @groupname arrayAsOf getArrayAsOf
  * @groupprio arrayAsOf 12
  * @groupname iterableAsOf getIterableAsOf
  * @groupprio iterableAsOf 13
  * @groupname jsonAsOf getJsonAsOf
  * @groupprio jsonAsOf 14
  * @groupname rawAsOf getRawAsOf
  * @groupprio rawAsOf 15
  *
  * @groupname since getSince
  * @groupprio since 21
  * @groupname arraySince getArraySince
  * @groupprio arraySince 22
  * @groupname iterableSince getIterableSince
  * @groupprio iterableSince 23
  * @groupname jsonSince getJsonSince
  * @groupprio jsonSince 24
  * @groupname rawSince getRawSince
  * @groupprio rawSince 25
  *
  * @groupname with getWith
  * @groupprio with 31
  * @groupname arrayWith getArrayWith
  * @groupprio arrayWith 32
  * @groupname iterableWith getIterableWith
  * @groupprio iterableWith 33
  * @groupname jsonWith getJsonWith
  * @groupprio jsonWith 34
  * @groupname rawWith getRawWith
  * @groupprio rawWith 35
  *
  * @groupname history getHistory
  * @groupdesc history (only implemented to return default List of tuples)
  * @groupprio history 41
  *
  * @groupname save save
  * @groupprio save 51
  *
  * @groupname insert insert
  * @groupprio insert 52
  *
  * @groupname update update
  * @groupprio update 53
  *
  * @groupname tx Transaction data (input to `getWith`).
  * @groupprio tx 61
  *
  * @groupname debugGet Debug get
  * @groupdesc debugGet Molecule getter debugging methods.
  * @groupprio debugGet 62
  * @groupname debugAction Debug action
  * @groupdesc debugAction Molecule action debugging methods (no effect on live db).
  * @groupprio debugAction 63
  *
  * @groupname internal Internal (but public) model/query representations
  * @groupprio internal 71
  **/
trait Molecule[Tpl] extends MoleculeBase with CastHelpers[Tpl] with JsonBuilder
  with GetArray[Tpl]
  with GetIterable[Tpl]
  with GetList[Tpl]
  with GetRaw
  with GetJson
  with ShowDebug[Tpl] {

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
    * @group tx
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


  // Insert ============================================================================================================================

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
    * @group tx
    * @return Transaction statements
    */
  trait getInsertTx


  /** Debug call to `insert` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group debugAction
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
    * @group tx
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
}


/** Arity 1-22 molecule implementation interfaces. */
object Molecule {

  abstract class Molecule01[A](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[A] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, ax: A*)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[A])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(Seq(_)))
    }
  }

  abstract class Molecule02[A, B](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2)))
    }
  }


  abstract class Molecule03[A, B, C](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3)))
    }
  }


  abstract class Molecule04[A, B, C, D](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }
  }


  abstract class Molecule05[A, B, C, D, E](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }
  }


  abstract class Molecule06[A, B, C, D, E, F](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }
  }

  abstract class Molecule07[A, B, C, D, E, F, G](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }
  }


  abstract class Molecule08[A, B, C, D, E, F, G, H](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }
  }


  abstract class Molecule09[A, B, C, D, E, F, G, H, I](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }
  }


  abstract class Molecule10[A, B, C, D, E, F, G, H, I, J](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }
  }


  abstract class Molecule11[A, B, C, D, E, F, G, H, I, J, K](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }
  }


  abstract class Molecule12[A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }
  }

  abstract class Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }
  }


  abstract class Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }
  }


  abstract class Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }
  }


  abstract class Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }
  }


  abstract class Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }
  }


  abstract class Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }
  }


  abstract class Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }
  }


  abstract class Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }
  }


  abstract class Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }
  }


  abstract class Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, queryData: (Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
    val (_query, _nestedQuery) = queryData

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
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.action.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }
  }
}