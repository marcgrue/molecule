package molecule.coretests

import molecule.api.in3_out22._
import molecule.ast.model.NoValue
import molecule.ast.transactionModel.Add
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.transform.Model2Query
import molecule.util.Helpers

class AdHocTest extends CoreSpec with Helpers {

  sequential


  "Adhoc" in new CoreSetup {


//    Ns.int(7).debugSave

    Ns.int.long$.insert(7, Some(8L))

//    Ns.int.long$ debugInsert List(
//      (1, Some(1L)),
//      (2, Some(2L)),
//      (3, Some(3L)),
//      (4, None)
//    )

    ok
  }

}