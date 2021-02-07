package molecule.tests.core.crud.insert

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out2._
import molecule.datomic.base.facade.TxReport
import molecule.TestSpec
import molecule.datomic.base.util.SystemPeer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class InsertAsync extends TestSpec {

  "Async insert" in new CoreSetup {

    // todo: remove when async implemented for other systems
    if (system == SystemPeer) {
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
}