package molecule.coretests.crud.insert

import molecule.core.facade.TxReport
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.datomic.peer.api.out2._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class InsertAsync extends CoreSpec {

  "Async insert" in new CoreSetup {

    // Insert single row of data with individual args
    val singleInsertFuture: Future[TxReport] = Ns.str.int.insertAsync("Ann", 28)

    // Insert Iterable of multiple rows of data
    val multipleInsertFuture: Future[TxReport] = Ns.str.int insertAsync List(
      ("Ben", 42),
      ("Liz", 37))

    for {
      _ <- singleInsertFuture
      _ <- multipleInsertFuture
      result <- Ns.str.int.getAsync
    } yield {
      // Both inserts applied
      result === List(
        ("Ann", 28),
        ("Ben", 42),
        ("Liz", 37)
      )
    }

    // For brevity, the synchronous equivalent `insert` is used in the following tests
  }
}