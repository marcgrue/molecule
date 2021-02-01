package molecule

import datomicClient.ClojureBridge
import molecule.core._4_api.api.Molecule
import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.datomic.base.facade.Conn
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.dsl.coreTest.{Ns, _}
import molecule.tests.core.base.schema.CoreTestSchema
import org.specs2.mutable.Specification


//class AdHocTest extends TestSpec with Helpers with ClojureBridge {
class AdHocTest extends Specification {


//  "adhoc" in new CoreSetup {
  "adhoc" >> {

    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)


//    x.st

//    Ns.int(1).save
//    Ns.int.get.head === 1

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
