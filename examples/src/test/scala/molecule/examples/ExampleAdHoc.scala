package molecule.examples

import datomic.Util
import molecule.datomic.api.out3._
import scala.jdk.CollectionConverters._


class ExampleAdHoc extends ExampleSpec {

  //  devLocalOnly = true
  //  omitPeer = true
  omitPeerServer = true


//  "example adhoc" in new SeattleSetup {
//
//
//    //    ok
//  }


  "example adhoc" in new SocialNewsSetup {
    import molecule.examples.dayOfDatomic.dsl.socialNews._



  }
}
