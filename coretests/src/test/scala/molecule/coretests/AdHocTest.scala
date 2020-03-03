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

    Ns.int(1).ints(7, 9, 13).save

    Ns.int.ints(count).get === List((1, 3))

    Ns.e.int.ints(count).debugGet


    ok
  }
}