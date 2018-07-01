package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.imports._


class Input1Float extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.float insert List((1, 1.0f), (2, 2.0f), (3, 3.0f))
  }

  class ManySetup extends CoreSetup {
    Ns.int.floats insert List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.float_(?))(2.0f).get === List(2)
    m(Ns.int.float_.<(?))(2.0f).get === List(1)
    m(Ns.int.float_.>(?))(2.0f).get === List(3)
    m(Ns.int.float_.<=(?))(2.0f).get.sorted === List(1, 2)
    m(Ns.int.float_.>=(?))(2.0f).get.sorted === List(2, 3)
    m(Ns.int.float_.!=(?))(2.0f).get.sorted === List(1, 3)
    m(Ns.int.float_.not(?))(2.0f).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.float(?))(2.0f).get === List(2.0f)
    m(Ns.int_.float.<(?))(2.0f).get === List(1.0f)
    m(Ns.int_.float.>(?))(2.0f).get === List(3.0f)
    m(Ns.int_.float.<=(?))(2.0f).get.sorted === List(1.0f, 2.0f)
    m(Ns.int_.float.>=(?))(2.0f).get.sorted === List(2.0f, 3.0f)
    m(Ns.int_.float.!=(?))(2.0f).get.sorted === List(1.0f, 3.0f)
    m(Ns.int_.float.not(?))(2.0f).get.sorted === List(1.0f, 3.0f)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.floats_(?))(Set(1.0f)).get.sorted === List(1)
    m(Ns.int.floats_(?))(Set(2.0f)).get.sorted === List(1, 2)
    m(Ns.int.floats_(?))(Set(3.0f)).get.sorted === List(2, 3)
    m(Ns.int.floats_(?))(Set(4.0f)).get.sorted === List(3)
    m(Ns.int.floats_.<(?))(Set(2.0f)).get.sorted === List(1)
    m(Ns.int.floats_.>(?))(Set(2.0f)).get.sorted === List(2, 3)
    m(Ns.int.floats_.<=(?))(Set(2.0f)).get.sorted === List(1, 2)
    m(Ns.int.floats_.>=(?))(Set(2.0f)).get.sorted === List(1, 2, 3)
    m(Ns.int.floats_.!=(?))(Set(2.0f)).get.sorted === List(1, 2, 3)
    m(Ns.int.floats_.not(?))(Set(2.0f)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.float_(?))(1.0f or 2.0f).get.sorted === List(1, 2)
    m(Ns.int.float_(?))(1.0f, 2.0f).get.sorted === List(1, 2)
    m(Ns.int.float_(?))(Seq(1.0f, 2.0f)).get.sorted === List(1, 2)
    m(Ns.int.float_(?))(float1 or float2).get.sorted === List(1, 2)
    m(Ns.int.float_(?))(float1, float2).get.sorted === List(1, 2)
    m(Ns.int.float_(?))(Seq(float1, float2)).get.sorted === List(1, 2)
    val floatList = Seq(float1, float2)
    m(Ns.int.float_(?))(floatList).get.sorted === List(1, 2)
  }
}