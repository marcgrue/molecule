package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1BigInt extends CoreSpec {


  class OneSetup extends CoreSetup {
    Ns.int.bigInt insert List((1, bigInt1), (2, bigInt2), (3, bigInt3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.bigInts insert List((1, Set(bigInt1, bigInt2)), (2, Set(bigInt2, bigInt3)), (3, Set(bigInt3, bigInt4)))
  }


  "1 input parameter" >> {

    "Tacit expressions" in new OneSetup {
      m(Ns.int.bigInt_(?))(bigInt2).get === List(2)
      m(Ns.int.bigInt_.<(?))(bigInt2).get === List(1)
      m(Ns.int.bigInt_.>(?))(bigInt2).get === List(3)
      m(Ns.int.bigInt_.<=(?))(bigInt2).get.sorted === List(1, 2)
      m(Ns.int.bigInt_.>=(?))(bigInt2).get.sorted === List(2, 3)
      m(Ns.int.bigInt_.!=(?))(bigInt2).get.sorted === List(1, 3)
      m(Ns.int.bigInt_.not(?))(bigInt2).get.sorted === List(1, 3)
    }

    "Expressions" in new OneSetup {
      m(Ns.int_.bigInt(?))(bigInt2).get === List(bigInt2)
      m(Ns.int_.bigInt.<(?))(bigInt2).get === List(bigInt1)
      m(Ns.int_.bigInt.>(?))(bigInt2).get === List(bigInt3)
      m(Ns.int_.bigInt.<=(?))(bigInt2).get.sorted === List(bigInt1, bigInt2)
      m(Ns.int_.bigInt.>=(?))(bigInt2).get.sorted === List(bigInt2, bigInt3)
      m(Ns.int_.bigInt.!=(?))(bigInt2).get.sorted === List(bigInt1, bigInt3)
      m(Ns.int_.bigInt.not(?))(bigInt2).get.sorted === List(bigInt1, bigInt3)
    }

    "Cardinality-many expressions" in new ManySetup {
      m(Ns.int.bigInts_(?))(Set(bigInt1)).get.sorted === List(1)
      m(Ns.int.bigInts_(?))(Set(bigInt2)).get.sorted === List(1, 2)
      m(Ns.int.bigInts_(?))(Set(bigInt3)).get.sorted === List(2, 3)
      m(Ns.int.bigInts_(?))(Set(bigInt4)).get.sorted === List(3)
      m(Ns.int.bigInts_.<(?))(Set(bigInt2)).get.sorted === List(1)
      m(Ns.int.bigInts_.>(?))(Set(bigInt2)).get.sorted === List(2, 3)
      m(Ns.int.bigInts_.<=(?))(Set(bigInt2)).get.sorted === List(1, 2)
      m(Ns.int.bigInts_.>=(?))(Set(bigInt2)).get.sorted === List(1, 2, 3)
      m(Ns.int.bigInts_.!=(?))(Set(bigInt2)).get.sorted === List(1, 2, 3)
      m(Ns.int.bigInts_.not(?))(Set(bigInt2)).get.sorted === List(1, 2, 3)
    }

    "OR-logic" in new OneSetup {
      m(Ns.int.bigInt_(?))(bigInt1 or bigInt2).get.sorted === List(1, 2)
      m(Ns.int.bigInt_(?))(bigInt1, bigInt2).get.sorted === List(1, 2)
      m(Ns.int.bigInt_(?))(Seq(bigInt1, bigInt2)).get.sorted === List(1, 2)
      val bigIntList = Seq(bigInt1, bigInt2)
      m(Ns.int.bigInt_(?))(bigIntList).get.sorted === List(1, 2)
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