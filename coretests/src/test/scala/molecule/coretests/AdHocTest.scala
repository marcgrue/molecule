package molecule.coretests


import datomic.Peer
import molecule.datomic.peer.api.out3._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.Helpers


class AdHocTest extends CoreSpec with Helpers {
  sequential

  "Adhoc" in new CoreSetup {

    ok
  }



  "dev-local" >> {

//    val client = Datomic.clientDevLocal("Hello system name")
//    client.createDatabase("hello")
//    implicit val conn: Connection = client.connect("hello")


    implicit val conn = recreateDbFrom(CoreTestSchema)



    ok
  }
}