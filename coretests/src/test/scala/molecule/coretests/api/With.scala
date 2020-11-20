package molecule.coretests.api

import java.io.FileReader
import datomic.Util
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.api.out1._
import molecule.datomic.peer.facade.Datomic_Peer._


class With extends CoreSpec {

  implicit val conn = recreateDbFrom(CoreTestSchema)

  Ns.int(1).save

  val saveTx2 = Ns.int(2).getSaveTx
  val saveTx3 = Ns.int(3).getSaveTx

  val data = new FileReader("coretests/resources/save2-3.dtm") // contains: "[{:Ns/int 2} {:Ns/int 3}]"
  val txData2_3 = Util.readAll(data).get(0).asInstanceOf[java.util.List[Object]]


  "With" >> {

    Ns.int.getWith(saveTx2) === List(1, 2)
    Ns.int.getWith(saveTx2, saveTx3) === List(1, 2, 3)

    // Note how the parameter for number of rows returned is first (since we need the vararg for tx molecules last)
    Ns.int.getWith(2, saveTx2, saveTx3) === List(1, 2)

    Ns.int.getWith(txData2_3) === List(1, 2, 3)
    Ns.int.getWith(txData2_3, 2) === List(1, 2)


    Ns.int.getArrayWith(saveTx2) === Array(1, 2)
    Ns.int.getArrayWith(saveTx2, saveTx3) === Array(1, 2, 3)
    Ns.int.getArrayWith(2, saveTx2, saveTx3) === Array(1, 2)

    Ns.int.getArrayWith(txData2_3) === Array(1, 2, 3)
    Ns.int.getArrayWith(txData2_3, 2) === Array(1, 2)


    Ns.int.getIterableWith(saveTx2).iterator.toList === Iterator(1, 2).toList
    Ns.int.getIterableWith(saveTx2, saveTx3).iterator.toList === Iterator(1, 2, 3).toList

    Ns.int.getIterableWith(txData2_3).iterator.toList === Iterator(1, 2, 3).toList


    Ns.int.getRawWith(saveTx2).toString === "[[1], [2]]"
    Ns.int.getRawWith(saveTx2, saveTx3).toString === "[[1], [2], [3]]"
    Ns.int.getRawWith(2, saveTx2, saveTx3).toString === "[[1], [2]]"

    Ns.int.getRawWith(txData2_3).toString === "[[1], [2], [3]]"
    Ns.int.getRawWith(txData2_3, 2).toString === "[[1], [2]]"


    Ns.int.getJsonWith(saveTx2) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2}
        |]""".stripMargin

    Ns.int.getJsonWith(saveTx2, saveTx3) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonWith(2, saveTx2, saveTx3) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2}
        |]""".stripMargin


    Ns.int.getJsonWith(txData2_3) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonWith(txData2_3, 2) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2}
        |]""".stripMargin

  }
}
