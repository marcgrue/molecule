package molecule.coretests


import datomic.{Peer, Util}
import datomic.Util.{list, read}
import datomicScala.client.api.sync.Db
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.Helpers
import molecule.datomic.api.out6._
import molecule.datomic.client.devLocal.facade.Database_DevLocal
import molecule.datomic.peer.facade.Database_Peer

class AdHocTest extends CoreSpec with Helpers {


  "adhoc" in new CoreSetup {

//    Ns.int(1).save.eid
//    Ns.int.get === List(1)

    Ns.int.enum$ insert List((1, Some("enum1")), (2, None))
    m(Ns.int(1).enum$(Some("enum1"))).get === List((1, Some("enum1")))

    ok
  }
}