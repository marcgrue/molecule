package molecule.coretests


import java.util.Date
import datomic.{Peer, Util}
import datomic.Util.{list, read}
import datomicClient.ClojureBridge
import datomicClient.Invoke.syncFn
import datomicScala.client.api.sync.Db
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, Helpers, Timer}
import molecule.datomic.api.in1_out6._
import molecule.datomic.client.facade.DatomicDb_Client
import molecule.datomic.peer.facade.DatomicDb_Peer
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.datomic.base.facade.TxReport
import scala.jdk.CollectionConverters._
import scala.util.Random


class AdHocTest extends CoreSpec with Helpers with ClojureBridge {

//  peerOnly = true
  peerServerOnly = true
//  devLocalOnly = true


  //  "adhoc" in new BidirectionalSetup {
  //  "adhoc" in new PartitionSetup {
  "adhoc" in new CoreSetup {


//    AEVT.e.a.v.t.debugGet

//    EAVT.a.get.size === 686
//    AEVT.a.get.size === 686
//    VAET.a.get.size === 347
//    AVET.a.get.size === 197
//
//    EAVT.a.get.size === 546
//    AEVT.a.get.size === 546
//    VAET.a.get.size === 315
//    AVET.a.get.size === 546

    println(EAVT.a.get.size)
    println(AEVT.a.get.size)
    println(VAET.a.get.size)
    println(AVET.a.get.size)


    ok
  }
}