package molecule.coretests


import java.util.Date
import datomic.{Database, Peer, Util}
import datomic.Util.{list, read}
import datomicClient.ClojureBridge
import datomicClient.Invoke.syncFn
import datomicScala.client.api.sync.{Datomic, Db}
import molecule.core.ast.tempDb
import molecule.core.transform.{CastHelpers, CastHelpersOptNested}
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, Helpers, Timer}
import molecule.datomic.api.in1_out6._
import molecule.datomic.client.facade.{Conn_Client, DatomicDb_Client}
import molecule.datomic.peer.facade.{DatomicDb_Peer, TxReport_Peer}
import molecule.coretests.bidirectionals.dsl.bidirectional._
import molecule.datomic.api.out1.debugTransactBundle
import molecule.datomic.base.facade.TxReport
import scala.jdk.CollectionConverters._
import scala.util.Random


class AdHocTest extends CoreSpec with Helpers with ClojureBridge {

  //  peerOnly = true
  //  peerServerOnly = true
  //  devLocalOnly = true


  //  "adhoc" in new BidirectionalSetup {
  //  "adhoc" in new PartitionSetup {
  "adhoc" in new CoreSetup {


    ok
  }
}
