package molecule.coretests


import datomic.Peer
import molecule.coretests.util.CoreSpec3
//import molecule.datomic.peer.api.out3._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.Helpers


class AdHocTest extends CoreSpec3 with Helpers {
  sequential


  "adhoc" in new CoreSetup {

    Ns.int.apply(1).save
    Ns.int.get === List(1)
    ok
  }

}