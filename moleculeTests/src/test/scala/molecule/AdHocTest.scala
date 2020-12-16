package molecule

import datomicClient.ClojureBridge
import molecule.core.util.Helpers


class AdHocTest extends TestSpec with Helpers with ClojureBridge {

  //  peerOnly = true
  //  peerServerOnly = true
  //  devLocalOnly = true


  "adhoc" in new CoreSetup {



    ok
  }


  //  "adhoc" in new BidirectionalSetup {
  //import molecule.tests.core.bidirectionals.dsl.bidirectional._
  //
  //  }

  //  "adhoc" in new PartitionSetup {

  //  }


  //  "example adhoc" in new SeattleSetup {
  //
  //
  //    //    ok
  //  }


//  "example adhoc" in new SocialNewsSetup {
//
//
//
//    ok
//  }
}
