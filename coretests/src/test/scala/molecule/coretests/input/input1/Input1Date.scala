package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1Date extends CoreSpec {


  class OneSetup extends CoreSetup {
    Ns.int.date insert List((1, date1), (2, date2), (3, date3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.dates insert List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.date_(?))(date2).get === List(2)
    m(Ns.int.date_.<(?))(date2).get === List(1)
    m(Ns.int.date_.>(?))(date2).get === List(3)
    m(Ns.int.date_.<=(?))(date2).get.sorted === List(1, 2)
    m(Ns.int.date_.>=(?))(date2).get.sorted === List(2, 3)
    m(Ns.int.date_.!=(?))(date2).get.sorted === List(1, 3)
    m(Ns.int.date_.not(?))(date2).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.date(?))(date2).get === List(date2)
    m(Ns.int_.date.<(?))(date2).get === List(date1)
    m(Ns.int_.date.>(?))(date2).get === List(date3)
    m(Ns.int_.date.<=(?))(date2).get.sorted === List(date1, date2)
    m(Ns.int_.date.>=(?))(date2).get.sorted === List(date2, date3)
    m(Ns.int_.date.!=(?))(date2).get.sorted === List(date1, date3)
    m(Ns.int_.date.not(?))(date2).get.sorted === List(date1, date3)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.dates_(?))(Set(date1)).get.sorted === List(1)
    m(Ns.int.dates_(?))(Set(date2)).get.sorted === List(1, 2)
    m(Ns.int.dates_(?))(Set(date3)).get.sorted === List(2, 3)
    m(Ns.int.dates_(?))(Set(date4)).get.sorted === List(3)
    m(Ns.int.dates_.<(?))(Set(date2)).get.sorted === List(1)
    m(Ns.int.dates_.>(?))(Set(date2)).get.sorted === List(2, 3)
    m(Ns.int.dates_.<=(?))(Set(date2)).get.sorted === List(1, 2)
    m(Ns.int.dates_.>=(?))(Set(date2)).get.sorted === List(1, 2, 3)
    m(Ns.int.dates_.!=(?))(Set(date2)).get.sorted === List(1, 2, 3)
    m(Ns.int.dates_.not(?))(Set(date2)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.date_(?))(date1 or date2).get.sorted === List(1, 2)
    m(Ns.int.date_(?))(date1, date2).get.sorted === List(1, 2)
    m(Ns.int.date_(?))(Seq(date1, date2)).get.sorted === List(1, 2)
    val dateList = Seq(date1, date2)
    m(Ns.int.date_(?))(dateList).get.sorted === List(1, 2)
  }
}