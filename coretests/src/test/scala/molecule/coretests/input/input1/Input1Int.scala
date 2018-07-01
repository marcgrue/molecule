package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.imports._


class Input1Int extends CoreSpec {

  class OneSetup extends CoreSetup {
    Ns.int.str insert List((1, "a"), (2, "b"), (3, "c"))
  }

  class ManySetup extends CoreSetup {
    Ns.int.strs insert List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
  }

  "Cardinality one" >> {

    "Expressions" in new OneSetup {
      m(Ns.long_.int(?))(2).get === List(2)
      m(Ns.long_.int.<(?))(2).get === List(1)
      m(Ns.long_.int.>(?))(2).get === List(3)
      m(Ns.long_.int.<=(?))(2).get.sorted === List(1, 2)
      m(Ns.long_.int.>=(?))(2).get.sorted === List(2, 3)
      m(Ns.long_.int.!=(?))(2).get.sorted === List(1, 3)
      m(Ns.long_.int.not(?))(2).get.sorted === List(1, 3)
    }

    "Tacit expressions" in new OneSetup {

      m(Ns.long.int_.apply(?)).apply(2).get === List(2L)

      m(Ns.long.int_.<(?))(2).get === List(1L)
      m(Ns.long.int_.>(?))(2).get === List(3L)
      m(Ns.long.int_.<=(?))(2).get.sorted === List(1L, 2L)
      m(Ns.long.int_.>=(?))(2).get.sorted === List(2L, 3L)
      m(Ns.long.int_.!=(?))(2).get.sorted === List(1L, 3L)
      m(Ns.long.int_.not(?))(2).get.sorted === List(1L, 3L)
    }

    "OR-logic" in new OneSetup {
      m(Ns.long.int_(?))(1 or 2).get.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(1, 2).get.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(Seq(1, 2)).get.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(int1 or int2).get.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(int1, int2).get.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(Seq(int1, int2)).get.sorted === List(1L, 2L)
      val intList = Seq(int1, int2)
      m(Ns.long.int_(?))(intList).get.sorted === List(1L, 2L)
    }
  }


  "Cardinality many" >> {

    "Expressions" in new ManySetup {
      m(Ns.long.ints_(?)).apply(Set(1)).get.sorted === List(1L)
      m(Ns.long.ints_(?)).apply(Seq(1)).get.sorted === List(1L)
      m(Ns.long.ints_(?))(Set(2)).get.sorted === List(1L, 2L)
      m(Ns.long.ints_(?))(Set(3)).get.sorted === List(2L, 3L)
      m(Ns.long.ints_(?))(Set(4)).get.sorted === List(3L)
      m(Ns.long.ints_.<(?))(Set(2)).get.sorted === List(1L)
      m(Ns.long.ints_.>(?))(Set(2)).get.sorted === List(2L, 3L)
      m(Ns.long.ints_.<=(?))(Set(2)).get.sorted === List(1L, 2L)
      m(Ns.long.ints_.>=(?))(Set(2)).get.sorted === List(1L, 2L, 3L)
      m(Ns.long.ints_.!=(?))(Set(2)).get.sorted === List(1L, 2L, 3L)
      m(Ns.long.ints_.not(?))(Set(2)).get.sorted === List(1L, 2L, 3L)
    }
  }
}