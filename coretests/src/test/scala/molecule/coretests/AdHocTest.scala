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
import molecule.coretests.time.domain
import molecule.datomic.api.in1_out6._
import molecule.datomic.client.facade.DatomicDb_Client
import molecule.datomic.peer.facade.DatomicDb_Peer

class AdHocTest extends CoreSpec with Helpers {

  //  peerOnly = true
  peerServerOnly = true


  "adhoc" in new CoreSetup {

    Ns.int.get === List()

//    conn.testDbAsOfNow

    Ns.int.get === List()

    val e1 = Ns.int(1).save.eid
    println(e1)

    Ns.e.int.get === List((e1, 1))

    Ns(e1).int(2).update

    Ns.e.int.get === List((e1, 2))

    //
    //    val e2 = Ns.int(3).save.eid
    //    Ns.int.get.sorted === List(2, 3)
    //
    //    Ns(e2).int(4).update
    //    Ns.int.get.sorted === List(2, 4)
    //
    //
    //    conn.useLiveDb
    //    Ns.int.get === List(1)
  }


  //  "adhoc" in new CoreSetup {
  //
  //    Ns.int.get === List(1)
  //
  //    conn.testDbAsOfNow
  //    Ns.int.get === List(1)
  //    val eid = Ns.e.int_.get.head
  //    println(eid)
  //
  //    Ns(eid).int(2).update
  //    Ns.int.get === List(2)
  //
  //    val e2 = Ns.int(3).save.eid
  //    println(e2)
  //    Ns.int.get.sorted === List(2, 3)
  //
  //    Ns(e2).int(4).update
  //    Ns.int.get.sorted === List(2, 4)
  //
  //
  //    conn.useLiveDb
  //    Ns.int.get === List(1)
  //
  //
  //    //    val tx1     = Ns.int(1).save
  //    //    val eid     = tx1.eid
  //    //    val counter = domain.Counter(eid)
  //    //    val t1      = tx1.t
  //    //    val date1   = tx1.inst
  //    //
  //    //
  //    //    conn.testDbAsOfNow
  //    //    val tx2     = Ns(eid).int(2).update
  //    //
  //    //    // Live state
  //    //    Ns.int.get === List(2)
  //    //
  //    //    // Use current state as a test db "branch"
  //    ////    conn.testDbAsOfNow
  //    //
  //    //    // Test state is same as live state
  //    //    Ns.int.get === List(2)
  //    //
  //    //    // Update test db
  //    //    Ns(eid).int(3).update
  //    //
  //    //    // Updated test state
  //    //    Ns.int.get === List(3)
  //    //
  //    //    // Discard test db and go back to live db
  //    //    conn.useLiveDb
  //    //
  //    //    // Live state unchanged
  //    //    Ns.int.get === List(2)
  //
  //
  //    ok
  //  }
}