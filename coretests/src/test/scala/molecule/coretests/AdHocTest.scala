package molecule.coretests


import datomicClient.ClojureBridge
import molecule.core.util.Helpers
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.in1_out6._


class AdHocTest extends CoreSpec with Helpers with ClojureBridge {

  //  peerOnly = true
  //  peerServerOnly = true
  //  devLocalOnly = true


  "adhoc" in new CoreSetup {
    import molecule.coretests.util.dsl.coreTest._



    ok
  }


  //  "adhoc" in new BidirectionalSetup {
  //import molecule.coretests.bidirectionals.dsl.bidirectional._
  //
  //  }

  //  "adhoc" in new PartitionSetup {

  //  }
}
