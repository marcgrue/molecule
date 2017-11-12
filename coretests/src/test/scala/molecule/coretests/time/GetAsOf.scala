package molecule.coretests.time

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import org.specs2.mutable.Specification
import org.specs2.specification.Scope


class GetAsOf extends Specification with Scope {

  sequential

  // Create new db from schema
  implicit val conn = recreateDbFrom(CoreTestSchema)

  // Db before testing

  // tx1: initial insert
  val tx1 = Ns.int(1).save
  val eid = tx1.eid

  // tx2: update
  val tx2 = Ns(eid).int(2).update


  "tx" >> {
    // Value as of tx1
    Ns(eid).int.getAsOf(tx1) === List(1)

    // Current value
    Ns(eid).int.get === List(2)

    // Value as of tx2 (current value)
    Ns(eid).int.getAsOf(tx2) === List(2)
  }


  "t" >> {
    val t1 = tx1.t
    val t2 = tx2.t

    // Value as of tx1
    Ns(eid).int.getAsOf(t1) === List(1)

    // Current value
    Ns(eid).int.get === List(2)

    // Value as of tx2 (current value)
    Ns(eid).int.getAsOf(t2) === List(2)
  }


  "Date" >> {
    val date1 = tx1.inst
    val date2 = tx2.inst

    // Value as of tx1
    Ns(eid).int.getAsOf(date1) === List(1)

    // Current value
    Ns(eid).int.get === List(2)

    // Value as of tx2 (current value)
    Ns(eid).int.getAsOf(date2) === List(2)
  }
}