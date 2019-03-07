package molecule.coretests.transaction

import java.io.FileReader
import java.util
import datomic.Util
import molecule.api.out4._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest.Ns
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class TxRaw extends CoreSpec {


  "Transact external data from file" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    // Add raw data from external file with edn transactional data
    val data = new FileReader("coretests/resources/save2-3.dtm") // contains: "[{:Ns/int 2} {:Ns/int 3}]"
    conn.transact(Util.readAll(data).get(0).asInstanceOf[java.util.List[AnyRef]])

    // Raw data has been added
    Ns.int.get === List(1, 2, 3)
  }


  "Transact local raw transactional data" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    // Add raw transactional data
    // (Scala integers are internally stored as Longs)
    conn.transact(Util.list(
      Util.map(":Ns/int", 2L.asInstanceOf[AnyRef]),
      Util.map(":Ns/int", 3L.asInstanceOf[AnyRef])
    ).asInstanceOf[java.util.List[AnyRef]])

    // Raw data has been added
    Ns.int.get === List(1, 2, 3)
  }


  "Async transaction of raw data" in new CoreSetup {

    // Initial data
    Ns.int(1).save
    Ns.int.get === List(1)

    Await.result(
      // Add raw transactional data
      // (Scala integers are internally stored as Longs)
      conn.transactAsync(
        Util.list(
          Util.map(":Ns/int", 2L.asInstanceOf[AnyRef]),
          Util.map(":Ns/int", 3L.asInstanceOf[AnyRef])
        ).asInstanceOf[java.util.List[AnyRef]]
      ) map { txReport =>
        // Raw data has been added
        Ns.int.get === List(1, 2, 3)
      },
      2.seconds
    )
  }
}
