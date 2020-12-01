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
import molecule.coretests.bidirectionals.dsl.bidirectional._


class AdHocTest extends CoreSpec with Helpers {

  //  peerOnly = true
//  peerServerOnly = true


//  "adhoc" in new BidirectionalSetup {
  "adhoc" in new CoreSetup {

    Ns.int.Ref1.str1 insert List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )

    Ns.e(count).int_.>(1).debugGet
    Ns.e(count).int_.>(1).get.head === 2


    ok
  }
}