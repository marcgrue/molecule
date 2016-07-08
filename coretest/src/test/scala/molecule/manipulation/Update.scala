package molecule
package manipulation

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class Update extends CoreSpec {


  "Related" >> {

    "Card many" in new CoreSetup {

      (Ns(42L).str("b").Ref1.int1(2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noRefs] Update molecules can't have references to other namespaces."

      (Ns(42L).str("b").Refs1.int1(2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noRefs] Update molecules can't have references to other namespaces."

      (Ns(42L).str("b").Refs1.*(Ref1.int1(2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noRefs] Update molecules can't have nested data structures."

      (m(Ns(42L).str("b") ~ Ref2.int2(2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"[api.CheckModel.noRefs] Update molecules can't be composites."
    }

  }
}