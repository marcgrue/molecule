package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1UUID extends CoreSpec {


  class OneSetup extends CoreSetup {
    Ns.int.uuid insert List((1, uuid1), (2, uuid2), (3, uuid3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.uuids insert List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.uuid_(?))(uuid2).get === List(2)
    m(Ns.int.uuid_.<(?))(uuid2).get === List(1)
    m(Ns.int.uuid_.>(?))(uuid2).get === List(3)
    m(Ns.int.uuid_.<=(?))(uuid2).get.sorted === List(1, 2)
    m(Ns.int.uuid_.>=(?))(uuid2).get.sorted === List(2, 3)
    m(Ns.int.uuid_.!=(?))(uuid2).get.sorted === List(1, 3)
    m(Ns.int.uuid_.not(?))(uuid2).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.uuid(?))(uuid2).get === List(uuid2)
    m(Ns.int_.uuid.<(?))(uuid2).get === List(uuid1)
    m(Ns.int_.uuid.>(?))(uuid2).get === List(uuid3)
    m(Ns.int_.uuid.<=(?))(uuid2).get.sorted === List(uuid1, uuid2)
    m(Ns.int_.uuid.>=(?))(uuid2).get.sorted === List(uuid2, uuid3)
    m(Ns.int_.uuid.!=(?))(uuid2).get.sorted === List(uuid1, uuid3)
    m(Ns.int_.uuid.not(?))(uuid2).get.sorted === List(uuid1, uuid3)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.uuids_(?))(Set(uuid1)).get.sorted === List(1)
    m(Ns.int.uuids_(?))(Set(uuid2)).get.sorted === List(1, 2)
    m(Ns.int.uuids_(?))(Set(uuid3)).get.sorted === List(2, 3)
    m(Ns.int.uuids_(?))(Set(uuid4)).get.sorted === List(3)
    m(Ns.int.uuids_.<(?))(Set(uuid2)).get.sorted === List(1)
    m(Ns.int.uuids_.>(?))(Set(uuid2)).get.sorted === List(2, 3)
    m(Ns.int.uuids_.<=(?))(Set(uuid2)).get.sorted === List(1, 2)
    m(Ns.int.uuids_.>=(?))(Set(uuid2)).get.sorted === List(1, 2, 3)
    m(Ns.int.uuids_.!=(?))(Set(uuid2)).get.sorted === List(1, 2, 3)
    m(Ns.int.uuids_.not(?))(Set(uuid2)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.uuid_(?))(uuid1 or uuid2).get.sorted === List(1, 2)
    m(Ns.int.uuid_(?))(uuid1, uuid2).get.sorted === List(1, 2)
    m(Ns.int.uuid_(?))(Seq(uuid1, uuid2)).get.sorted === List(1, 2)
    val uuidList = Seq(uuid1, uuid2)
    m(Ns.int.uuid_(?))(uuidList).get.sorted === List(1, 2)
  }
}