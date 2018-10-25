package molecule.coretests.expression

import molecule.api.out4._
import molecule.coretests.util.CoreSetup
import molecule.coretests.util.dsl.coreTest._

class Eid extends Base {


  "Entity id" in new CoreSetup {

    val List(e1, e2, e3, e4) = Ns.int insert List(1, 2, 3, 4) eids
    val seq                  = Set(e1, e2)
    val set                  = Seq(e3, e4)
    val iterable             = Iterable(e3, e4)

    Ns.int.get === List(1, 2, 3, 4)

    // Single eid
    Ns(e1).int.get === List(1)

    // Vararg
    Ns(e1, e2).int.get === List(1, 2)

    // Seq
    Ns(Seq(e1, e2)).int.get === List(1, 2)
    Ns(seq).int.get === List(1, 2)

    // Set
    Ns(Set(e3, e4)).int.get === List(3, 4)
    Ns(set).int.get === List(3, 4)

    // Iterable
    Ns(Iterable(e3, e4)).int.get === List(3, 4)
    Ns(iterable).int.get === List(3, 4)
  }
}