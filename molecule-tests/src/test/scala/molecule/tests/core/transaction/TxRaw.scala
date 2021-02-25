package molecule.tests.core.transaction

import java.io.FileReader
import datomic.Util
import molecule.datomic.api.out1._
import molecule.datomic.base.util.SystemPeer
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TxRaw extends TestSpec {

  "Transact external data from file" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    if (system == SystemPeer) {
      // Add raw data from external file with edn transactional data
      val data = new FileReader("molecule-tests/resources/tests/core/time/save2-3.dtm")
      // contains: "[{:Ns/int 2} {:Ns/int 3}]"
      conn.transact(Util.readAll(data).get(0).asInstanceOf[java.util.List[AnyRef]])

      // Raw data has been added
      Ns.int.get === List(1, 2, 3)
    }
  }


  "Transact local raw transactional data" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    // Add raw transactional data
    // (Scala integers are internally stored as Longs)
    conn.transact(Util.list(
      Util.map(Util.read(":Ns/int"), 2L.asInstanceOf[AnyRef]),
      Util.map(Util.read(":Ns/int"), 3L.asInstanceOf[AnyRef])
    ).asInstanceOf[java.util.List[AnyRef]])

    // Raw data has been added
    Ns.int.get === List(1, 2, 3)
  }


  "Async transaction of raw data" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    // todo: remove once peer-server allows async
    if (system == SystemPeer) {
      Await.result(
        // Add raw transactional data
        // (Scala integers are internally stored as Longs)
        conn.transactAsync(
          Util.list(
            Util.map(Util.read(":Ns/int"), 2L.asInstanceOf[AnyRef]),
            Util.map(Util.read(":Ns/int"), 3L.asInstanceOf[AnyRef])
          ).asInstanceOf[java.util.List[AnyRef]]
        ) map { txReport =>
          // Raw data has been added
          Ns.int.get === List(1, 2, 3)
        },
        2.seconds
      )
    }
  }
}