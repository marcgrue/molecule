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


//    Ns.uri debugInsert uri1
//    Ns.uri insert uri1

//    com.cognitect.transit.impl.URIImpl

//    m(Ns.uri)
    Ns.uri insert uri1
//    Ns.uri.debugGet
    Ns.uri.get.head === uri1

  }
}