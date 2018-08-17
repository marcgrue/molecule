package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1BigDecimal extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.bigDec insert List((1, bigDec1), (2, bigDec2), (3, bigDec3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.bigDecs insert List((1, Set(bigDec1, bigDec2)), (2, Set(bigDec2, bigDec3)), (3, Set(bigDec3, bigDec4)))
  }


  "1 input parameter" >> {

    "Tacit expressions" in new OneSetup {
      m(Ns.int.bigDec_(?))(bigDec2).get === List(2)
      m(Ns.int.bigDec_.<(?))(bigDec2).get === List(1)
      m(Ns.int.bigDec_.>(?))(bigDec2).get === List(3)
      m(Ns.int.bigDec_.<=(?))(bigDec2).get.sorted === List(1, 2)
      m(Ns.int.bigDec_.>=(?))(bigDec2).get.sorted === List(2, 3)
      m(Ns.int.bigDec_.!=(?))(bigDec2).get.sorted === List(1, 3)
      m(Ns.int.bigDec_.not(?))(bigDec2).get.sorted === List(1, 3)
    }

    "Expressions" in new OneSetup {
      m(Ns.int_.bigDec(?))(bigDec2).get === List(bigDec2)
      m(Ns.int_.bigDec.<(?))(bigDec2).get === List(bigDec1)
      m(Ns.int_.bigDec.>(?))(bigDec2).get === List(bigDec3)
      m(Ns.int_.bigDec.<=(?))(bigDec2).get.sorted === List(bigDec1, bigDec2)
      m(Ns.int_.bigDec.>=(?))(bigDec2).get.sorted === List(bigDec2, bigDec3)
      m(Ns.int_.bigDec.!=(?))(bigDec2).get.sorted === List(bigDec1, bigDec3)
      m(Ns.int_.bigDec.not(?))(bigDec2).get.sorted === List(bigDec1, bigDec3)
    }

    "Cardinality-many expressions" in new ManySetup {
      m(Ns.int.bigDecs_(?))(Set(bigDec1)).get.sorted === List(1)
      m(Ns.int.bigDecs_(?))(Set(bigDec2)).get.sorted === List(1, 2)
      m(Ns.int.bigDecs_(?))(Set(bigDec3)).get.sorted === List(2, 3)
      m(Ns.int.bigDecs_(?))(Set(bigDec4)).get.sorted === List(3)
      m(Ns.int.bigDecs_.<(?))(Set(bigDec2)).get.sorted === List(1)
      m(Ns.int.bigDecs_.>(?))(Set(bigDec2)).get.sorted === List(2, 3)
      m(Ns.int.bigDecs_.<=(?))(Set(bigDec2)).get.sorted === List(1, 2)
      m(Ns.int.bigDecs_.>=(?))(Set(bigDec2)).get.sorted === List(1, 2, 3)
      m(Ns.int.bigDecs_.!=(?))(Set(bigDec2)).get.sorted === List(1, 2, 3)
      m(Ns.int.bigDecs_.not(?))(Set(bigDec2)).get.sorted === List(1, 2, 3)
    }

    "OR-logic" in new OneSetup {
      m(Ns.int.bigDec_(?))(bigDec1 or bigDec2).get.sorted === List(1, 2)
      m(Ns.int.bigDec_(?))(bigDec1, bigDec2).get.sorted === List(1, 2)
      m(Ns.int.bigDec_(?))(Seq(bigDec1, bigDec2)).get.sorted === List(1, 2)
      val bigDecList = Seq(bigDec1, bigDec2)
      m(Ns.int.bigDec_(?))(bigDecList).get.sorted === List(1, 2)
    }
  }

  "2 input parameters" >> {
    // todo: also AND semantics of supplying tuples of values
    ok
  }

  "3 input parameters" >> {
    // todo
    ok
  }
}