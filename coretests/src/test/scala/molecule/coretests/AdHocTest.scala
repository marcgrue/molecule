package molecule.coretests


import datomic.{Peer, Util}
import datomic.Util.{list, read}
import molecule.datomic.api
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.core.util.Helpers
import molecule.datomic.api.out6._

class AdHocTest extends CoreSpec with Helpers {
  sequential


  "adhoc" in new CoreSetup {


    Ns.int(1).save
    val res = Ns.int.get
    res === List(1)
    ok
  }

}