package molecule.examples

import datomic.Util
import molecule.datomic.api.out3._
import scala.jdk.CollectionConverters._
import java.util.{Collections, Date, UUID, Collection => jCollection, List => jList}
import java.util
import clojure.lang.PersistentArrayMap
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
    import molecule.examples.dayOfDatomic.dsl.socialNews._

    ok
  }
}
