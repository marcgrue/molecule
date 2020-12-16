package molecule

import datomicClient.ClojureBridge

class ExampleAdHoc extends ExampleSpec with ClojureBridge {

  devLocalOnly = true
  //  omitPeer = true
  //  omitPeerServer = true


  //  "example adhoc" in new SeattleSetup {
  //
  //
  //    //    ok
  //  }


  "example adhoc" in new SocialNewsSetup {

    ok
  }
}
