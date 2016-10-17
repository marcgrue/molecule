package molecule
package ref

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class TwoStepQueries extends CoreSpec {


  class Setup extends CoreSetup {

    // age - name - beverage - rating
    //    m(Ns.int.str.Refs1 * Ref1.str1.int1) insert List(
    //      (23, "Joe", List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
    //      (25, "Ben", List(("Coffee", 2), ("Tea", 3))),
    //      (23, "Liz", List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))
  }


  "AND, unify attributes" in new Setup {

    Ns.str.Refs1.*(Ref1.str1) insert List(
      ("a", List("r1", "r2", "r3")),
      ("b", List("r1", "r2", "r4")),
      ("c", List("r1", "r4", "r5"))
    )

    // 1. Ns strings with "r2" references
    // "a" and "b"
    val strs_with_r2s = Ns.str.Refs1.str1_("r2").get

    // 2. Ns strings having but omitting "r2" reference
    Ns.str(strs_with_r2s).Refs1.*(Ref1.str1.not("r2")).getD

    Ns.str(strs_with_r2s).Refs1.*(Ref1.str1.not("r2")).get === List(
      ("a", List("r1", "r3")),
      ("b", List("r1", "r4"))
    )

    val xx = m(Ns.str(?).Refs1.*(Ref1.str1.not("r2")))
//    xx(strs_with_r2s).getD

    xx(strs_with_r2s).get === List(
      ("a", List("r1", "r3")),
      ("b", List("r1", "r4"))
    )

    // Using eid

    // 1. Entities with "r2" references
    val eids_with_r2s_ = Ns.e.Refs1.str1_("r2").get

    // 2. Entities having but omitting "r2" reference
    //    Ns.e_.apply(have_r2s_).str.Refs1.*(Ref1.str1.not("r2")).get === List(
    Ns.e_(eids_with_r2s_).str.Refs1.*(Ref1.str1.not("r2")).getD

    Ns.e_(eids_with_r2s_).str.Refs1.*(Ref1.str1.not("r2")).get === List(
      ("a", List("r1", "r3")),
      ("b", List("r1", "r4"))
    )

    //  def apply(eids: Seq[Long]): Ns_0 = ???

  }
}