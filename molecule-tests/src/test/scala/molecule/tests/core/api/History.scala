package molecule.tests.core.api

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out3._
import molecule.setup.TestSpec


class History extends TestSpec {

  class Setup extends CoreSetup {
    val tx1 = Ns.int(1).save
    val e   = tx1.eid
    val tx2 = Ns(e).int(2).update
  }


  "History" in new Setup {

    Ns(e).int.t.op.getHistory.sortBy(t => (t._2, t._3)) === List(
      (1, tx1.t, true),
      (1, tx2.t, false),
      (2, tx2.t, true)
    )
  }
}
