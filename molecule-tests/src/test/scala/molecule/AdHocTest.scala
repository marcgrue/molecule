package molecule

import datomicClient.ClojureBridge
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.tests.core.base.dsl.coreTest._


class AdHocTest extends TestSpec with Helpers with ClojureBridge {


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
