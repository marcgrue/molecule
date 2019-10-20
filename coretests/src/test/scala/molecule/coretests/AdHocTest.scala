package molecule.coretests

//import molecule.api.out5._
import java.util.Date
import molecule.api.in1_out2._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.Query2String
import molecule.util.Helpers

class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {


    ok
  }

}