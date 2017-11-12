package molecule.coretests.attr

import molecule.Imports._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class EntitySelection extends CoreSpec {

  "Applied eid to namespace" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get === List(1, 2, 3)

    Ns(e1).int.get === List(1)

    Ns(e1, e2).int.get === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns(e23).int.get === List(2, 3)

    val e23s = Set(e2, e3)
    Ns(e23s).int.get === List(2, 3)
  }

  // Not supported (todo?)
//  "Applied eid to `e`" in new CoreSetup {
//
//    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids
//
//    Ns.int.get === List(1, 2, 3)
//
//    Ns.e(e1).int.get === List(1)
//
//    Ns.e(e1, e2).int.get === List(1, 2)
//
//    val e23 = Seq(e2, e3)
//    Ns.e(e23).int.get === List(2, 3)
//
//    val e23s = Set(e2, e3)
//    Ns.e(e23s).int.get === List(2, 3)
//  }


  "Input molecule" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    val ints = m(Ns(?).int)

    ints(e1).get === List(1)

    ints(e1, e2).get === List(1, 2)

    val e23 = Seq(e2, e3)
    ints.apply(e23).get === List(2, 3)

    val e23s = Set(e2, e3)
    ints.apply(e23s).get === List(2, 3)
  }
}