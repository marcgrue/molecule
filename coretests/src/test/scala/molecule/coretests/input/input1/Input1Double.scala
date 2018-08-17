package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1Double extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.double insert List((1, 1.0), (2, 2.0), (3, 3.0))
  }

  class ManySetup extends CoreSetup {
    Ns.int.doubles insert List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.double_(?))(2.0).get === List(2)
    m(Ns.int.double_.<(?))(2.0).get === List(1)
    m(Ns.int.double_.>(?))(2.0).get === List(3)
    m(Ns.int.double_.<=(?))(2.0).get.sorted === List(1, 2)
    m(Ns.int.double_.>=(?))(2.0).get.sorted === List(2, 3)
    m(Ns.int.double_.!=(?))(2.0).get.sorted === List(1, 3)
    m(Ns.int.double_.not(?))(2.0).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.double(?))(2.0).get === List(2.0)
    m(Ns.int_.double.<(?))(2.0).get === List(1.0)
    m(Ns.int_.double.>(?))(2.0).get === List(3.0)
    m(Ns.int_.double.<=(?))(2.0).get.sorted === List(1.0, 2.0)
    m(Ns.int_.double.>=(?))(2.0).get.sorted === List(2.0, 3.0)
    m(Ns.int_.double.!=(?))(2.0).get.sorted === List(1.0, 3.0)
    m(Ns.int_.double.not(?))(2.0).get.sorted === List(1.0, 3.0)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.doubles_(?))(Set(1.0)).get.sorted === List(1)
    m(Ns.int.doubles_(?))(Set(2.0)).get.sorted === List(1, 2)
    m(Ns.int.doubles_(?))(Set(3.0)).get.sorted === List(2, 3)
    m(Ns.int.doubles_(?))(Set(4.0)).get.sorted === List(3)
    m(Ns.int.doubles_.<(?))(Set(2.0)).get.sorted === List(1)
    m(Ns.int.doubles_.>(?))(Set(2.0)).get.sorted === List(2, 3)
    m(Ns.int.doubles_.<=(?))(Set(2.0)).get.sorted === List(1, 2)
    m(Ns.int.doubles_.>=(?))(Set(2.0)).get.sorted === List(1, 2, 3)
    m(Ns.int.doubles_.!=(?))(Set(2.0)).get.sorted === List(1, 2, 3)
    m(Ns.int.doubles_.not(?))(Set(2.0)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.double_(?))(1.0 or 2.0).get.sorted === List(1, 2)
    m(Ns.int.double_(?))(1.0, 2.0).get.sorted === List(1, 2)
    m(Ns.int.double_(?))(Seq(1.0, 2.0)).get.sorted === List(1, 2)
    m(Ns.int.double_(?))(double1 or double2).get.sorted === List(1, 2)
    m(Ns.int.double_(?))(double1, double2).get.sorted === List(1, 2)
    m(Ns.int.double_(?))(Seq(double1, double2)).get.sorted === List(1, 2)
    val doubleList = Seq(double1, double2)
    m(Ns.int.double_(?))(doubleList).get.sorted === List(1, 2)
  }
}