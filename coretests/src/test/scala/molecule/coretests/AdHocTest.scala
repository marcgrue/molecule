package molecule.coretests


import java.util.Date
import datomic.{Peer, Util}
import datomic.Util.{list, read}
import datomicScala.client.api.sync.Db
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.Helpers
import molecule.datomic.api.in1_out6._
import molecule.datomic.client.devLocal.facade.DatomicDb_DevLocal
import molecule.datomic.peer.facade.DatomicDb_Peer

class AdHocTest extends CoreSpec with Helpers {

//  peerOnly = true

  "adhoc" in new CoreSetup {



    ok
  }
}