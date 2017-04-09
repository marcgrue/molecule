package molecule
package attr

import molecule.util.{CoreSetup, CoreSpec}
import molecule.util.dsl.coreTest._

class EntitySelection extends CoreSpec {

  "Applied eid" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get === List(1, 2, 3)

    Ns(e1).int.get === List(1)

    Ns(e1, e2).int.get === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns(e23).int.get === List(2, 3)

    val e23s = Set(e2, e3)
    Ns(e23s).int.get === List(2, 3)
  }


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