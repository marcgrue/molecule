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


//  "adhoc" in new CoreSetup {
  "adhoc" in new BidirectionalSetup {

    val List(ann, annLovesBen, benLovesAnn, ben) =
      Person.name("Ann").Loves.weight(7).Person.name("Ben").save.eids

    // Bidirectional property edges have been saved
    Person.name.Loves.weight.Person.name.get.sorted === List(
      ("Ann", 7, "Ben"),
      // Reverse edge:
      ("Ben", 7, "Ann")
    )

    ann.touchMax(1) === Map(
      ":db/id" -> ann,
      ":Person/loves" -> annLovesBen,
      ":Person/name" -> "Ann"
    )

    ok
  }
}