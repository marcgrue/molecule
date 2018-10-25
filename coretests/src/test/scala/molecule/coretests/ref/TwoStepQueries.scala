package molecule.coretests.ref

import molecule.api.out3._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class TwoStepQueries extends CoreSpec {


  "AND, unify attributes" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.str1) insert List(
      ("a", List("r1", "r2", "r3")),
      ("b", List("r1", "r2", "r4")),
      ("c", List("r1", "r4", "r5"))
    )

    // 1. Ns strings with "r2" references
    // "a" and "b"
    val strs_with_r2s = Ns.str.Refs1.str1_("r2").get.toSeq

    // 2. Ns strings having but omitting "r2" reference
    Ns.str(strs_with_r2s).Refs1.*(Ref1.str1.not("r2")).get === List(
      ("a", List("r1", "r3")),
      ("b", List("r1", "r4"))
    )


    // Using eid

    // 1. Entities with "r2" references
    val eids_with_r2s = Ns.e.Refs1.str1_("r2").get

    // Todo
    // 2. Entities having but omitting "r2" reference
//    Ns(eids_with_r2s).str.Refs1.*(Ref1.str1.not("r2")).get === List(
//      ("a", List("r1", "r3")),
//      ("b", List("r1", "r4"))
//    )

    ok
  }
}