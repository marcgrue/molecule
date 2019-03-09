package molecule.coretests

import molecule.api.in1_out6.m
import molecule.api.in3_out22._
import molecule.ast.model._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.transform.Model2Query
import molecule.util.Helpers


class AdHocTest extends CoreSpec with Helpers {

  sequential

  implicit val conn = recreateDbFrom(CoreTestSchema)


  "adhoc" >> {


    ok
  }

}