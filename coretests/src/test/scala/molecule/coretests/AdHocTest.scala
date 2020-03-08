package molecule.coretests

import datomic.Peer
import molecule.api.in1_out6._
import molecule.ast.model._
import molecule.ast.transactionModel.Add
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers
import scala.collection.JavaConverters._

class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {


//    Ns.ref1.debugGet
    Ns.ref1(42L).debugGet


    ok
  }
}