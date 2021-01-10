package molecule.core.api
import molecule.core.api.get._
import molecule.core.api.getAsync._
import molecule.core.ast.MoleculeBase
import molecule.core.ast.elements._
import molecule.core.ast.query.Query
import molecule.core.ast.transactionModel.Statement
import molecule.core.ops.VerifyModel
import molecule.core.transform.{CastHelpers, JsonBuilder, Model2Transaction}
import molecule.core.util.Inspect
import molecule.datomic.base.api.{DatomicEntity, EntityOps}
import molecule.datomic.base.facade.{Conn, TxReport}
import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions


/** Core molecule interface defining actions that can be called on molecules.
  *
  * Groups of interfaces:
  * <table>
  * <tr>
  * <td><b>get</b><td>
  * <td><b>getAsync</b><td>
  * <td>Get molecule data.</td>
  * </tr>
  * <tr>
  * <td><b>getAsOf</b><td>
  * <td><b>getAsyncAsOf</b><td>
  * <td>Get molecule data <i>asOf</i> point in time.</td>
  * </tr>
  * <tr>
  * <td><b>getSince</b><td>
  * <td><b>getAsyncSince</b><td>
  * <td>Get molecule data <i>since</i> point in time.</td>
  * </tr>
  * <tr>
  * <td><b>getWith</b><td>
  * <td><b>getAsyncWith</b><td>
  * <td>Get molecule data <i>with</i> given data set.</td>
  * </tr>
  * <tr>
  * <td><b>getHistory</b><td>
  * <td><b>getAsyncHistory</b> &nbsp;&nbsp;&nbsp;<td>
  * <td>Get molecule data from <i>history</i> of database.</td>
  * </tr>
  * <tr>
  * <td><b>save</b><td>
  * <td><b>saveAsync</b><td>
  * <td>Save molecule with applied data.</td>
  * </tr>
  * <tr>
  * <td><b>insert</b><td>
  * <td><b>insertAsync</b><td>
  * <td>Insert multiple rows of data matching molecule.</td>
  * </tr>
  * <tr>
  * <td><b>update</b><td>
  * <td><b>updateAsync</b><td>
  * <td>Update molecule with applied data.</td>
  * </tr>
  * <tr>
  * <tr>
  * <td><b>tx</b><td>
  * <td><td>
  * <td>Molecule transaction data (input to `getWith`).</td>
  * </tr>
  * <tr>
  * <td><b>inspect get</b><td>
  * <td><td>
  * <td>Inspect calling get method on molecule.</td>
  * </tr>
  * <tr>
  * <td><b>inspect operation</b> &nbsp;&nbsp;&nbsp;<td>
  * <td><td>
  * <td>Inspect calling save/insert/update method on molecule.</td>
  * </tr>
  * </table>
  *
  * @tparam Tpl Type of molecule (tuple of its attribute types)
  * @see For retract ("delete") methods, see [[molecule.datomic.base.api.EntityOps EntityOps]] and [[molecule.datomic.base.api.DatomicEntity Entity]].
  * @groupname get
  * @groupprio get 10
  * @groupname getAsync
  * @groupprio getAsync 11
  * @groupname getAsOf
  * @groupprio getAsOf 110
  * @groupname getArrayAsOf
  * @groupprio getArrayAsOf 120
  * @groupname getIterableAsOf
  * @groupprio getIterableAsOf 130
  * @groupname getJsonAsOf
  * @groupprio getJsonAsOf 140
  * @groupname getRawAsOf
  * @groupprio getRawAsOf 150
  * @groupname getAsyncAsOf
  * @groupprio getAsyncAsOf 111
  * @groupname getAsyncArrayAsOf
  * @groupprio getAsyncArrayAsOf 121
  * @groupname getAsyncIterableAsOf
  * @groupprio getAsyncIterableAsOf 131
  * @groupname getAsyncJsonAsOf
  * @groupprio getAsyncJsonAsOf 141
  * @groupname getAsyncRawAsOf
  * @groupprio getAsyncRawAsOf 151
  * @groupname getSince
  * @groupprio getSince 210
  * @groupname getArraySince
  * @groupprio getArraySince 220
  * @groupname getIterableSince
  * @groupprio getIterableSince 230
  * @groupname getJsonSince
  * @groupprio getJsonSince 240
  * @groupname getRawSince
  * @groupprio getRawSince 250
  * @groupname getAsyncSince
  * @groupprio getAsyncSince 211
  * @groupname getAsyncArraySince
  * @groupprio getAsyncArraySince 221
  * @groupname getAsyncIterableSince
  * @groupprio getAsyncIterableSince 231
  * @groupname getAsyncJsonSince
  * @groupprio getAsyncJsonSince 241
  * @groupname getAsyncRawSince
  * @groupprio getAsyncRawSince 251
  * @groupname getWith
  * @groupprio getWith 310
  * @groupname getArrayWith
  * @groupprio getArrayWith 320
  * @groupname getIterableWith
  * @groupprio getIterableWith 330
  * @groupname getJsonWith
  * @groupprio getJsonWith 340
  * @groupname getRawWith
  * @groupprio getRawWith 350
  * @groupname getAsyncWith
  * @groupprio getAsyncWith 311
  * @groupname getAsyncArrayWith
  * @groupprio getAsyncArrayWith 321
  * @groupname getAsyncIterableWith
  * @groupprio getAsyncIterableWith 331
  * @groupname getAsyncJsonWith
  * @groupprio getAsyncJsonWith 341
  * @groupname getAsyncRawWith
  * @groupprio getAsyncRawWith 351
  * @groupname getHistory
  * @groupdesc getHistory (only implemented to return List of tuples)
  * @groupprio getHistory 410
  * @groupname save save
  * @groupprio save 510
  * @groupname insert insert
  * @groupprio insert 520
  * @groupname update update
  * @groupprio update 530
  * @groupname getTx Transaction data (input to getWith).
  * @groupprio getTx 610
  * @groupname inspectGet Inspect get
  * @groupdesc inspectGet Molecule getter inspecting methods.
  * @groupprio inspectGet 620
  * @groupname inspectOp Inspect operation
  * @groupdesc inspectOp Molecule operation inspecting methods (no effect on live db).
  * @groupprio inspectOp 630
  * @groupname internal Internal (but public) model/query representations
  * @groupprio internal 710
  **/
trait Molecule[Tpl] extends MoleculeBase with CastHelpers[Tpl] with JsonBuilder
  with GetArray[Tpl]
  with GetIterable[Tpl]
  with GetList[Tpl]
  with GetRaw
  with GetJson
  with GetAsyncArray[Tpl]
  with GetAsyncIterable[Tpl]
  with GetAsyncList[Tpl]
  with GetAsyncRaw
  with GetAsyncJson
  with ShowInspect[Tpl] {

  // Save ============================================================================================================================

  /** Save data applied to molecule attributes.
    * <br><br>
    * Returns [[molecule.datomic.base.facade.TxReport TxReport]] having info about
    * the result of the save transaction.
    * {{{
    *   val txReport = Person.name("Ben").age(42).save
    *
    *   // Data has been saved in db
    *   Person.name.age.get === List(("Ben", 42))
    * }}}
    * The save operation is synchronous and blocking. Use `saveAsync` for non-blocking asynchronous saves.
    *
    * @group save
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return [[molecule.datomic.base.facade.TxReport TxReport]] with info about the result of the `save` transaction.
    */
  def save(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "save")
    conn.transact(Seq(Model2Transaction(conn, _model).saveStmts()))
  }


  /** Asynchronously save data applied to molecule attributes.
    * <br><br>
    * Returns `Future` with [[molecule.datomic.base.facade.TxReport TxReport]] having info about
    * the result of the save transaction.
    * {{{
    *   val futureSave: Future[TxReport] = Person.name("Ben").age(42).saveAsync
    *
    *   for {
    *     _ <- futureSave
    *     result <- Person.name.age.getAsync
    *   } yield {
    *     // Data was saved
    *     result.head === ("Ben", 42)
    *   }
    * }}}
    * The save operation is asynchronous and non-blocking. Internally calls Datomic's asynchronous API.
    *
    * @group save
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return [[molecule.datomic.base.facade.TxReport TxReport]] with info about the result of the `save` transaction.
    */
  def saveAsync(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = {
    VerifyModel(_model, "save")
    conn.transactAsync(Seq(Model2Transaction(conn, _model).saveStmts()))
  }


  /** Get transaction statements of a call to `save` on a molecule (without affecting the db).
    *
    * @group getTx
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
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
        Inspect("output.Molecule.getSaveTx", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }


  // Insert ============================================================================================================================

  /** Insert one or more rows of data matching molecule.
    * <br><br>
    * Returns `Future` with [[molecule.datomic.base.facade.TxReport TxReport]] having info about
    * the result of the insert transaction.
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
    *     ("Liz", 37)
    *   )
    *
    *   // Data was inserted
    *   Person.name.age.get.sorted === List(
    *     ("Ann", 28),
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    * }}}
    * Each insert apply method returns a [[molecule.datomic.base.facade.TxReport TxReport]] with info about the result of the transaction.
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
    *       ("Liz", 37)
    *     )
    *   )
    *
    *   // Data was inserted
    *   Person.name.age.get.sorted === List(
    *     ("Ann", 28),
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    * }}}
    * The insert operation is synchronous and blocking. Use `insertAsync` for non-blocking asynchronous inserts.
    *
    * @group insert
    */
  trait insert


  /** Asynchronously insert one or more rows of data matching molecule.
    * <br><br>
    * Returns `Future` with [[molecule.datomic.base.facade.TxReport TxReport]] having info about
    * the result of the insert transaction.
    * <br><br>
    * Data matching the types of the molecule can be inserted either as individual args
    * or an Iterable (List, Set etc) of tuples:
    * {{{
    *   // Insert single row of data with individual args
    *   val singleInsertFuture: Future[TxReport] = Person.name.age.insertAsync("Ann", 28)
    *
    *   // Insert multiple rows of data. Accepts Iterable[Tpl]
    *   val multipleInsertFuture: Future[TxReport] = Person.name.age insertAsync List(
    *     ("Ben", 42),
    *     ("Liz", 37)
    *   )
    *
    *   for {
    *     _ <- singleInsertFuture
    *     _ <- multipleInsertFuture
    *     result <- Person.name.age.getAsync
    *   } yield {
    *     // Both inserts applied
    *     result === List(
    *       ("Ann", 28),
    *       ("Ben", 42),
    *       ("Liz", 37)
    *     )
    *   }
    * }}}
    *
    * The insert operation is asynchronous and non-blocking. Internally calls Datomic's asynchronous API.
    *
    * @group insert
    */
  trait insertAsync


  /** Get transaction statements of a call to `insert` on a molecule (without affecting the db).
    *
    * @group getTx
    * @return Transaction statements
    */
  trait getInsertTx


  /** Inspect call to `insert` on a molecule (without affecting the db).
    * <br><br>
    * Prints internal molecule transformation representations to output:
    * <br><br>
    * Model --> Generic statements --> Datomic statements
    *
    * @group inspectOp
    */
  trait inspectInsert


  /** Insert data is verified on instantiation of `insert` object in each arity molecule */
  protected trait checkInsertModel {
    VerifyModel(_model, "insert")
  }

  protected def untupled(rawData: Iterable[Seq[Any]]): Seq[Seq[Any]] = {
    if (this.toString.contains("compositOutMolecule"))
      (rawData.toSeq map tupleToSeq).map(_ flatMap tupleToSeq)
    else
      rawData.toSeq
  }

  protected def _insert(conn: Conn, model: Model, dataRows: Iterable[Seq[Any]]): TxReport = {
    conn.transact(Model2Transaction(conn, model).insertStmts(untupled(dataRows)))
  }

  protected def _insertAsync(conn: Conn, model: Model, dataRows: Iterable[Seq[Any]])(implicit ec: ExecutionContext): Future[TxReport] = {
    conn.transactAsync(Model2Transaction(conn, model).insertStmts(untupled(dataRows)))
  }

  protected def _getInsertTx(conn: Conn, dataRows: Iterable[Seq[Any]]): Seq[Seq[Statement]] = {
    val transformer = Model2Transaction(conn, _model)
    val stmtss: Seq[Seq[Statement]] = try {
      transformer.insertStmts(untupled(dataRows))
    } catch {
      case e: Throwable =>
        println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Error - data processed so far:  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n")
        Inspect("output.Molecule._inspectInsert", 1)(1, _model, transformer.stmtsModel, dataRows, untupled(dataRows))
        throw e
    }
    stmtss
  }

  // Update ============================================================================================================================

  /** Update entity with data applied to molecule attributes.
    * <br><br>
    * Returns [[molecule.datomic.base.facade.TxReport TxReport]] with info about the result of the update transaction.
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
    * The update operation is synchronous and blocking. Use `updateAsync` for non-blocking asynchronous updates.
    *
    * @group update
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return [[molecule.datomic.base.facade.TxReport TxReport]]
    */
  def update(implicit conn: Conn): TxReport = {
    VerifyModel(_model, "update")
    conn.transact(Seq(Model2Transaction(conn, _model).updateStmts()))
  }

  /** Asynchronously update entity with data applied to molecule attributes.
    * Returns `Future` with [[molecule.datomic.base.facade.TxReport TxReport]] having info about
    * the result of the update transaction.
    * {{{
    *   for {
    *     saveTx <- Person.name("Ben").age(42).saveAsync
    *     benId = saveTx.eid
    *     updateTx <- Person(benId).age(43).updateAsync
    *     result <- Person.name.age.getAsync
    *   } yield {
    *     // Ben is now 43
    *     result.head === ("ben", 43)
    *   }
    * }}}
    * The update operation is asynchronous and non-blocking. Internally calls Datomic's asynchronous API.
    *
    * @group update
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
    * @return [[molecule.datomic.base.facade.TxReport TxReport]]
    */
  def updateAsync(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = {
    VerifyModel(_model, "update")
    conn.transactAsync(Seq(Model2Transaction(conn, _model).updateStmts()))
  }


  /** Get transaction statements of a call to `update` on a molecule (without affecting the db).
    *
    * @group getTx
    * @param conn Implicit [[molecule.datomic.base.facade.Conn Conn]] value in scope
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
        Inspect("output.Molecule.inspectUpdate", 1)(1, _model, transformer.stmtsModel)
        throw e
    }
    Seq(stmts)
  }
}


/** Arity 1-22 molecule implementation interfaces. */
object Molecule {

  abstract class Molecule01[A](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[A] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): TxReport = _insert(conn, _model, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[A])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(Seq(_)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, ax: A*)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[A])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(Seq(_)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): Unit = _inspectInsert(conn, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[A])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(Seq(_)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, ax: A*)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, (a +: ax.toList).map(Seq(_)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[A])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(Seq(_)))
    }
  }

  abstract class Molecule02[A, B](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2)))
    }
  }


  abstract class Molecule03[A, B, C](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3)))
    }
  }


  abstract class Molecule04[A, B, C, D](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4)))
    }
  }


  abstract class Molecule05[A, B, C, D, E](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5)))
    }
  }


  abstract class Molecule06[A, B, C, D, E, F](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6)))
    }
  }

  abstract class Molecule07[A, B, C, D, E, F, G](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7)))
    }
  }


  abstract class Molecule08[A, B, C, D, E, F, G, H](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8)))
    }
  }


  abstract class Molecule09[A, B, C, D, E, F, G, H, I](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9)))
    }
  }


  abstract class Molecule10[A, B, C, D, E, F, G, H, I, J](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10)))
    }
  }


  abstract class Molecule11[A, B, C, D, E, F, G, H, I, J, K](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11)))
    }
  }


  abstract class Molecule12[A, B, C, D, E, F, G, H, I, J, K, L](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12)))
    }
  }

  abstract class Molecule13[A, B, C, D, E, F, G, H, I, J, K, L, M](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13)))
    }
  }


  abstract class Molecule14[A, B, C, D, E, F, G, H, I, J, K, L, M, N](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14)))
    }
  }


  abstract class Molecule15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15)))
    }
  }


  abstract class Molecule16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16)))
    }
  }


  abstract class Molecule17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17)))
    }
  }


  abstract class Molecule18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18)))
    }
  }


  abstract class Molecule19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19)))
    }
  }


  abstract class Molecule20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20)))
    }
  }


  abstract class Molecule21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21)))
    }
  }


  abstract class Molecule22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](val _model: Model, queryData: (Query, Option[Query], Query, Option[Query])) extends Molecule[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
    val (_query, _nestedQuery, _rawQuery, _rawNestedQuery) = queryData

    /** See [[molecule.core.api.Molecule.insert insert]] */
    object insert extends insert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): TxReport = _insert(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.core.api.Molecule.insert insert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): TxReport = _insert(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }

    /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
    object insertAsync extends insertAsync with checkInsertModel {

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.core.api.Molecule.insertAsync insertAsync]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn, ec: ExecutionContext): Future[TxReport] = _insertAsync(conn, _model, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }

    /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
    object inspectInsert extends inspectInsert with checkInsertModel {

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Unit = _inspectInsert(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.core.api.Molecule.inspectInsert inspectInsert]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): Unit = _inspectInsert(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }

    /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
    object getInsertTx extends getInsertTx with checkInsertModel {

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J, k: K, l: L, m: M, n: N, o: O, p: P, q: Q, r: R, s: S, t: T, u: U, v: V)(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, Seq(Seq(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v)))

      /** See [[molecule.core.api.Molecule.getInsertTx getInsertTx]] */
      def apply(data: Iterable[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)])(implicit conn: Conn): Seq[Seq[Statement]] = _getInsertTx(conn, data.map(d => Seq(d._1, d._2, d._3, d._4, d._5, d._6, d._7, d._8, d._9, d._10, d._11, d._12, d._13, d._14, d._15, d._16, d._17, d._18, d._19, d._20, d._21, d._22)))
    }
  }
}