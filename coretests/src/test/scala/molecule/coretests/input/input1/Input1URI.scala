package molecule.coretests.input.input1

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.api._


class Input1URI extends CoreSpec {


  class OneSetup extends CoreSetup {
    Ns.int.uri insert List((1, uri1), (2, uri2), (3, uri3))
  }

  class ManySetup extends CoreSetup {
    Ns.int.uris insert List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
  }

  "Tacit expressions" in new OneSetup {
    m(Ns.int.uri_(?))(uri2).get === List(2)
    m(Ns.int.uri_.<(?))(uri2).get === List(1)
    m(Ns.int.uri_.>(?))(uri2).get === List(3)
    m(Ns.int.uri_.<=(?))(uri2).get.sorted === List(1, 2)
    m(Ns.int.uri_.>=(?))(uri2).get.sorted === List(2, 3)
    m(Ns.int.uri_.!=(?))(uri2).get.sorted === List(1, 3)
    m(Ns.int.uri_.not(?))(uri2).get.sorted === List(1, 3)
  }

  "Expressions" in new OneSetup {
    m(Ns.int_.uri(?))(uri2).get === List(uri2)
    m(Ns.int_.uri.<(?))(uri2).get === List(uri1)
    m(Ns.int_.uri.>(?))(uri2).get === List(uri3)
    m(Ns.int_.uri.<=(?))(uri2).get.sorted === List(uri1, uri2)
    m(Ns.int_.uri.>=(?))(uri2).get.sorted === List(uri2, uri3)
    m(Ns.int_.uri.!=(?))(uri2).get.sorted === List(uri1, uri3)
    m(Ns.int_.uri.not(?))(uri2).get.sorted === List(uri1, uri3)
  }

  "Cardinality-many expressions" in new ManySetup {
    m(Ns.int.uris_(?))(Set(uri1)).get.sorted === List(1)
    m(Ns.int.uris_(?))(Set(uri2)).get.sorted === List(1, 2)
    m(Ns.int.uris_(?))(Set(uri3)).get.sorted === List(2, 3)
    m(Ns.int.uris_(?))(Set(uri4)).get.sorted === List(3)
    m(Ns.int.uris_.<(?))(Set(uri2)).get.sorted === List(1)
    m(Ns.int.uris_.>(?))(Set(uri2)).get.sorted === List(2, 3)
    m(Ns.int.uris_.<=(?))(Set(uri2)).get.sorted === List(1, 2)
    m(Ns.int.uris_.>=(?))(Set(uri2)).get.sorted === List(1, 2, 3)
    m(Ns.int.uris_.!=(?))(Set(uri2)).get.sorted === List(1, 2, 3)
    m(Ns.int.uris_.not(?))(Set(uri2)).get.sorted === List(1, 2, 3)
  }

  "OR-logic" in new OneSetup {
    m(Ns.int.uri_(?))(uri1 or uri2).get.sorted === List(1, 2)
    m(Ns.int.uri_(?))(uri1, uri2).get.sorted === List(1, 2)
    m(Ns.int.uri_(?))(Seq(uri1, uri2)).get.sorted === List(1, 2)
    val uriList = Seq(uri1, uri2)
    m(Ns.int.uri_(?))(uriList).get.sorted === List(1, 2)
  }
}