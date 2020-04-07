package molecule.coretests


import molecule.api.in1_out6._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers


class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {


    Ns.int.debugGet

    ok
  }
}