package molecule.coretests.attr

import molecule._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class Generics extends CoreSpec {

  "Adding not allowed" in new CoreSetup {

    // Todo

    (Ns.str("man").Ref1.e(42L).save must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      s"[molecule.api.CheckModel.noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` not allowed in save molecules. Found `e(42)`"

//    Ns.str("man").Ref1.a("hej").save
//    Ns.str("man").Ref1.v("hej").save
//    Ns.str("man").Ref1.ns("hej").save
//    Ns.str("man").Ref1.tx(42).save
//    Ns.str("man").Ref1.txT(43L).save
//    val now = new java.util.Date()
//    Ns.str("man").Ref1.txInstant(now).save
//    Ns.str("man").Ref1.op(true).save
  }

}