package molecule.coretests.crud

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.ops.exception.VerifyModelException

class UpdateRef extends CoreSpec {


  "Related" >> {

    "Card many" in new CoreSetup {

      // Molecule doesn't allow to update across namespaces.

      // A referenced entity can be referenced from many other entities and it
      // therefore doesn't make sense to allow any one of those to update it.
      // Instead you need to expressively change the referenced entity itself with
      // its own entity id.

      (Ns(42L).str("b").Ref1.int1(2).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."

      (Ns(42L).str("b").Refs1.int1(2).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."

      (Ns(42L).str("b").Refs1.*(Ref1.int1(2)).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_onlyOneNs]  Update molecules can't have nested data structures like `Ref1`."

      (m(Ns(42L).str("b") ~ Ref2.int2(2)).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        s"[update_onlyOneNs]  Update molecules can't be composites."
    }

  }
}