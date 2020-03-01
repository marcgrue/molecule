package molecule.coretests.api

import molecule.api.out3._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema


class History extends CoreSpec {
  sequential


  implicit val conn = recreateDbFrom(CoreTestSchema)

  val tx1 = Ns.int(1).save
  val e = tx1.eid

  val tx2 = Ns(e).int(2).update


  "History" >> {

    Ns(e).int.t.op.getHistory.sortBy(t => (t._2, t._3)) === List(
      (1, tx1.t, true),
      (1, tx2.t, false),
      (2, tx2.t, true)
    )
  }
}
