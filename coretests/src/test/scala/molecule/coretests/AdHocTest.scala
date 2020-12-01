package molecule.coretests


import java.util.Date
import datomic.{Peer, Util}
import datomic.Util.{list, read}
import datomicScala.client.api.sync.Db
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, Helpers}
import molecule.datomic.api.in1_out6._
import molecule.datomic.client.facade.DatomicDb_Client
import molecule.datomic.peer.facade.DatomicDb_Peer

class AdHocTest extends CoreSpec with Helpers {

  //  peerOnly = true
//  peerServerOnly = true


  "adhoc" in new CoreSetup {

    val eid = Ns.bigInt(bigInt2).save.eid

    // Delete value (apply no value)
    Ns(eid).bigInt().update


  }
}