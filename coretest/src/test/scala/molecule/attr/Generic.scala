package molecule
package attr

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class Generic extends CoreSpec {

  "Adding not allowed" in new CoreSetup {

    // Todo

    // Add man with ref to existing woman
    (Ns.str("man").Ref1.e(42L).add must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
      s"[output.Molecule.noGenerics] Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
      s"not allowed in add molecules. Found `e`."

    Ns.str("man").Ref1.a("hej").add
    Ns.str("man").Ref1.v("hej").add
    Ns.str("man").Ref1.ns("hej").add
    Ns.str("man").Ref1.tx(42).add
    Ns.str("man").Ref1.txT(43L).add
    val now = new java.util.Date()
    Ns.str("man").Ref1.txInstant(now).add
    Ns.str("man").Ref1.op(true).add
  }

}