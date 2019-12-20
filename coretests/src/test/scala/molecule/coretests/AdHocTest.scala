package molecule.coretests

import molecule.api.in1_out3._
import molecule.ast.model._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.exception.Dsl2ModelException
import molecule.util.Helpers

class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {



    ok
  }
}