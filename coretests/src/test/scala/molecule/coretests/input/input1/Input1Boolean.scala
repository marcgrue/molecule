package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.imports._


class Input1Boolean extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.bool insert List((0, false), (1, true))
  }

  class ManySetup extends CoreSetup {
    Ns.int.bools insert List((0, Set(false)), (1, Set(true)), (2, Set(false, true)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.bool_(?))(false).get === List(0)
    m(Ns.int.bool_.<(?))(false).get === List()
    m(Ns.int.bool_.>(?))(false).get === List(1)
    m(Ns.int.bool_.<=(?))(false).get === List(0)
    m(Ns.int.bool_.>=(?))(false).get.sorted === List(0, 1)
    m(Ns.int.bool_.!=(?))(false).get === List(1)
    m(Ns.int.bool_.not(?))(false).get === List(1)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.bool(?))(false).get === List(false)
    m(Ns.int_.bool.<(?))(false).get === List()
    m(Ns.int_.bool.>(?))(false).get === List(true)
    m(Ns.int_.bool.<=(?))(false).get === List(false)
    m(Ns.int_.bool.>=(?))(false).get.sorted === List(false, true)
    m(Ns.int_.bool.!=(?))(false).get === List(true)
    m(Ns.int_.bool.not(?))(false).get === List(true)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.bools_(?))(Set(false)).get.sorted === List(0, 2)
    m(Ns.int.bools_.<(?))(Set(false)).get.sorted === List()
    m(Ns.int.bools_.>(?))(Set(false)).get.sorted === List(1, 2)
    m(Ns.int.bools_.<=(?))(Set(false)).get.sorted === List(0, 2)
    m(Ns.int.bools_.>=(?))(Set(false)).get.sorted === List(0, 1, 2)
    m(Ns.int.bools_.!=(?))(Set(false)).get.sorted === List(1, 2)
    m(Ns.int.bools_.not(?))(Set(false)).get.sorted === List(1, 2)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.bool_(?))(true or false).get.sorted === List(0, 1)
    m(Ns.int.bool_(?))(true, false).get.sorted === List(0, 1)
    m(Ns.int.bool_(?))(Seq(true, false)).get.sorted === List(0, 1)
    m(Ns.int.bool_(?))(bool0 or bool1).get.sorted === List(0, 1)
    m(Ns.int.bool_(?))(bool0, bool1).get.sorted === List(0, 1)
    m(Ns.int.bool_(?))(Seq(bool0, bool1)).get.sorted === List(0, 1)
    val boolList = Seq(bool0, bool1)
    m(Ns.int.bool_(?))(boolList).get.sorted === List(0, 1)
  }
}