package molecule.coretests.api

import molecule.api.out1._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema


class GetList extends CoreSpec {
  sequential

  implicit val conn = recreateDbFrom(CoreTestSchema)

  Ns.int insert List(1,2,3)


  "Get" >> {

    Ns.int.get === List(1, 2, 3)
    Ns.int.getArray === Array(1, 2, 3)
    Ns.int.getIterable.iterator.toList === Iterator(1, 2, 3).toList
    Ns.int.getRaw.toString === "[[1], [2], [3]]"
    Ns.int.getJson ===
      """[
        |{"ns.int": 1},
        |{"ns.int": 2},
        |{"ns.int": 3}
        |]""".stripMargin

    Ns.int.get(2) === List(1, 2)
    Ns.int.getArray(2) === Array(1, 2)
    Ns.int.getRaw(2).toString === "[[1], [2]]"
    Ns.int.getJson(2) ===
      """[
        |{"ns.int": 1},
        |{"ns.int": 2}
        |]""".stripMargin
  }
}
