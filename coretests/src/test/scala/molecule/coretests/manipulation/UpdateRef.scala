package molecule.coretests.manipulation

import molecule.Imports._

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class UpdateRef extends CoreSpec {


  "Related" >> {

    "Card many" in new CoreSetup {

      // Molecule doesn't allow to update across namespaces.

      // A referenced entity can be referenced from many other entities and it
      // therefore doesn't make sense to allow any one of those to update it.
      // Instead you need to expressively change the referenced entity itself with
      // its own entity id.

      (Ns(42L).str("b").Ref1.int1(2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."

      (Ns(42L).str("b").Refs1.int1(2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."

      (Ns(42L).str("b").Refs1.*(Ref1.int1(2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't have nested data structures like `Ref1`."

      (m(Ns(42L).str("b") ~ Ref2.int2(2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[molecule.ops.VerifyModel.update_onlyOneNs]  Update molecules can't be composites."
    }

  }
}