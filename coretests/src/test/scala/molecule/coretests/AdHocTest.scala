package molecule.coretests


import molecule.api.out10._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers


class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {

    Ns.int(1).save

    Ns.int.get === List(1)

    ok
  }
}