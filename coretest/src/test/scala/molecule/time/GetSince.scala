package molecule.time

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.schema.CoreTestSchema
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class GetSince extends Specification with Scope {

  sequential

  class Setup extends Scope {
    implicit val conn = recreateDbFrom(CoreTestSchema)
  }


  "Appended" in new Setup {

    val tx1 = Ns.int(1).save
    val tx2 = Ns.int(2).save
    val tx3 = Ns.int(3).save

    // Current values
    Ns.int.get === List(1, 2, 3)

    // Exclussive tx1 value
    Ns.int.getSince(tx1) === List(2, 3)

    Ns.int.getSince(tx2) === List(3)
    Ns.int.getSince(tx3) === List()
  }


  "Appended and updated" in new Setup {

    val tx1 = Ns.int(1).save
    val tx2 = Ns.int(2).save
    val tx3 = Ns.int(3).save

    val e2  = tx2.eid
    val tx4 = Ns(e2).int(4).update

    // Current values
    Ns.int.get === List(1, 3, 4)

    Ns.int.getSince(tx1) === List(3, 4)
    Ns.int.getSince(tx2) === List(3, 4)
    Ns.int.getSince(tx3) === List(4)
    Ns.int.getSince(tx4) === List()
  }


  "Appended and retracted" in new Setup {

    val tx1 = Ns.int(1).save
    val tx2 = Ns.int(2).save
    val tx3 = Ns.int(3).save
    val tx4 = Ns.int(4).save
    val tx5 = tx2.eid.retract

    // Current values
    Ns.int.get === List(1, 3, 4)

    Ns.int.getSince(tx1) === List(3, 4)
    Ns.int.getSince(tx2) === List(3, 4)
    Ns.int.getSince(tx3) === List(4)
    Ns.int.getSince(tx4) === List()
  }
}