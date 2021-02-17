package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.out3._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest.{Ref1_int1, _}
import scala.language.dynamics


class Immutable extends TestSpec with Helpers {


  "Basic" in new CoreSetup {

    Ns.int(0).str("x").Ref1.int1(1).save


//    val o = Ns.int.str.Ref1.int1.getObj

    val o = new Ns_int with Ns_str with Ns_Ref1_[Ref1_int1] {
      override val int = 0
      override val str = "x"
      override def Ref1: Ref1_int1 = new Ref1_int1 {
        override val int1: Int = 1
      }
    }



    o.int === 0
    o.str === "x"
    o.Ref1.int1 === 1

  }
}
