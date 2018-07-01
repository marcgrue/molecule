package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.imports._


class Input1Long extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.long insert List((1, 1L), (2, 2L), (3, 3L))
  }

  class ManySetup extends CoreSetup {
    Ns.int.longs insert List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.long_.apply(?)).apply(2L).get === List(2)
    m(Ns.int.long_.<(?)).apply(2L).get === List(1)
    m(Ns.int.long_.>(?)).apply(2L).get === List(3)
    m(Ns.int.long_.<=(?)).apply(2L).get.sorted === List(1, 2)
    m(Ns.int.long_.>=(?)).apply(2L).get.sorted === List(2, 3)
    m(Ns.int.long_.!=(?)).apply(2L).get.sorted === List(1, 3)
    m(Ns.int.long_.not(?)).apply(2L).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.long(?))(2L).get === List(2L)
    m(Ns.int_.long.<(?))(2L).get === List(1L)
    m(Ns.int_.long.>(?))(2L).get === List(3L)
    m(Ns.int_.long.<=(?))(2L).get.sorted === List(1L, 2L)
    m(Ns.int_.long.>=(?))(2L).get.sorted === List(2L, 3L)
    m(Ns.int_.long.!=(?))(2L).get.sorted === List(1L, 3L)
    m(Ns.int_.long.not(?))(2L).get.sorted === List(1L, 3L)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.longs_(?))(Set(1L)).get.sorted === List(1)
    m(Ns.int.longs_(?))(Set(2L)).get.sorted === List(1, 2)
    m(Ns.int.longs_(?))(Set(3L)).get.sorted === List(2, 3)
    m(Ns.int.longs_(?))(Set(4L)).get.sorted === List(3)
    m(Ns.int.longs_.<(?))(Set(2L)).get.sorted === List(1)
    m(Ns.int.longs_.>(?))(Set(2L)).get.sorted === List(2, 3)
    m(Ns.int.longs_.<=(?))(Set(2L)).get.sorted === List(1, 2)
    m(Ns.int.longs_.>=(?))(Set(2L)).get.sorted === List(1, 2, 3)
    m(Ns.int.longs_.!=(?))(Set(2L)).get.sorted === List(1, 2, 3)
    m(Ns.int.longs_.not(?))(Set(2L)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.long_(?))(1L or 2L).get.sorted === List(1, 2)
    m(Ns.int.long_(?))(1L, 2L).get.sorted === List(1, 2)
    m(Ns.int.long_(?))(Seq(1L, 2L)).get.sorted === List(1, 2)
    m(Ns.int.long_(?))(long1 or long2).get.sorted === List(1, 2)
    m(Ns.int.long_(?))(long1, long2).get.sorted === List(1, 2)
    m(Ns.int.long_(?))(Seq(long1, long2)).get.sorted === List(1, 2)
    val longList = Seq(long1, long2)
    m(Ns.int.long_(?))(longList).get.sorted === List(1, 2)
  }
}