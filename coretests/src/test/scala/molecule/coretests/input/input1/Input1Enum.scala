package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1Enum extends CoreSpec {


  class OneSetup extends CoreSetup {
    Ns.int.enum insert List((1, enum1), (2, enum2), (3, enum3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.enums insert List((1, Set(enum1, enum2)), (2, Set(enum2, enum3)), (3, Set(enum3, enum4)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.enum_(?))(enum2).get === List(2)
    m(Ns.int.enum_.<(?))(enum2).get === List(1)
    m(Ns.int.enum_.>(?))(enum2).get === List(3)
    m(Ns.int.enum_.<=(?))(enum2).get.sorted === List(1, 2)
    m(Ns.int.enum_.>=(?))(enum2).get.sorted === List(2, 3)
    m(Ns.int.enum_.!=(?))(enum2).get.sorted === List(1, 3)
    m(Ns.int.enum_.not(?))(enum2).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.enum(?))(enum2).get === List(enum2)
    m(Ns.int_.enum.<(?))(enum2).get === List(enum1)
    m(Ns.int_.enum.>(?))(enum2).get === List(enum3)
    m(Ns.int_.enum.<=(?))(enum2).get.sorted === List(enum1, enum2)
    m(Ns.int_.enum.>=(?))(enum2).get.sorted === List(enum2, enum3)
    m(Ns.int_.enum.!=(?))(enum2).get.sorted === List(enum1, enum3)
    m(Ns.int_.enum.not(?))(enum2).get.sorted === List(enum1, enum3)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.enums_(?))(Set(enum1)).get.sorted === List(1)
    m(Ns.int.enums_(?))(Set(enum2)).get.sorted === List(1, 2)
    m(Ns.int.enums_(?))(Set(enum3)).get.sorted === List(2, 3)
    m(Ns.int.enums_(?))(Set(enum4)).get.sorted === List(3)
    m(Ns.int.enums_.<(?))(Set(enum2)).get.sorted === List(1)
    m(Ns.int.enums_.>(?))(Set(enum2)).get.sorted === List(2, 3)
    m(Ns.int.enums_.<=(?))(Set(enum2)).get.sorted === List(1, 2)
    m(Ns.int.enums_.>=(?))(Set(enum2)).get.sorted === List(1, 2, 3)
    m(Ns.int.enums_.!=(?))(Set(enum2)).get.sorted === List(1, 2, 3)
    m(Ns.int.enums_.not(?))(Set(enum2)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.enum_(?))(enum1 or enum2).get.sorted === List(1, 2)
    m(Ns.int.enum_(?))(enum1, enum2).get.sorted === List(1, 2)
    m(Ns.int.enum_(?))(Seq(enum1, enum2)).get.sorted === List(1, 2)
    val enumList = Seq(enum1, enum2)
    m(Ns.int.enum_(?))(enumList).get.sorted === List(1, 2)
  }
}