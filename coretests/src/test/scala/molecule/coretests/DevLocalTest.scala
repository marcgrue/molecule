package molecule.coretests

import molecule.core.util.Helpers
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.peer.api.out3._


class DevLocalTest extends CoreSpec with Helpers {
  sequential



  "dev-local" >> {

//    val client = Datomic.clientDevLocal("Hello system name")
//    client.createDatabase("hello")
//    implicit val conn: Connection = client.connect("hello")


    implicit val conn = recreateDbFrom(CoreTestSchema)



    ok
  }
}