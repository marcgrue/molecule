package molecule.coretests

import molecule.api.in3_out22._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers

class AdHocTest extends CoreSpec with Helpers {

  sequential

  "Adhoc" in new CoreSetup {


    Ns.int.Ref1.int1._Ns.str.debugGet


    Ns.int.RefSub1.int1._Ns.str.debugGet

    Ns.int.RefSub1.int1.RefSub2.int2._Ref1._Ns.str.debugGet

    ok
  }

}