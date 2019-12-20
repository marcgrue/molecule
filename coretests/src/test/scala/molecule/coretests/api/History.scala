package molecule.coretests.api

import molecule.api.out3._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema


class History extends CoreSpec {
  sequential


  implicit val conn = recreateDbFrom(CoreTestSchema)

  val e = Ns.int(1).save.eid
  Ns(e).int(2).update


  "History" >> {

    Ns(e).int.t.op.getHistory.sortBy(t => (t._2, t._3)) === List(
      (1, 1037, true),
      (1, 1039, false),
      (2, 1039, true)
    )
  }
}
