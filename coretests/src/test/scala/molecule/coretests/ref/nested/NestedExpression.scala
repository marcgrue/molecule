package molecule.coretests.ref.nested

import molecule.api.in3_out4._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError


class NestedExpression extends CoreSpec {

  val one = 1

  "Before nested" in new CoreSetup {

    m(Ns.int.Refs1 * Ref1.int1) insert List(
      (1, List(1, 2)),
      (2, List(2, 3)),
      (3, List())
    )

    m(Ns.int.>(1).Refs1 * Ref1.int1).get === List(
      (2, List(2, 3)),
    )
    m(Ns.int.>(1).Refs1 *? Ref1.int1).get === List(
      (2, List(2, 3)),
      (3, List())
    )

    // Using variable
    m(Ns.int.>(one).Refs1 * Ref1.int1).get === List(
      (2, List(2, 3)),
    )
    m(Ns.int.>(one).Refs1 *? Ref1.int1).get === List(
      (2, List(2, 3)),
      (3, List())
    )
  }


  "In nested" in new CoreSetup {

    m(Ns.int.Refs1 * Ref1.int1) insert List(
      (1, List(1, 2)),
      (2, List(2, 3)),
      (3, List())
    )

    m(Ns.int.Refs1 * Ref1.int1(1)).get === List(
      (1, List(1)),
    )
    m(Ns.int.Refs1 * Ref1.int1(2)).get === List(
      (1, List(2)),
      (2, List(2)),
    )

    m(Ns.int.Refs1 * Ref1.int1.>(1)).get === List(
      (1, List(2)),
      (2, List(2, 3))
    )
    m(Ns.int.Refs1 * Ref1.int1.>(2)).get === List(
      (2, List(3))
    )

    // Using variable
    m(Ns.int.Refs1 * Ref1.int1(one)).get === List(
      (1, List(1)),
    )
    m(Ns.int.Refs1 * Ref1.int1.>(one)).get === List(
      (1, List(2)),
      (2, List(2, 3))
    )

    // Expressions not allowed in optional nested structures
    expectCompileError(
      "m(Ns.int.Refs1 *? Ref1.int1(1))",
      "molecule.transform.exception.Dsl2ModelException: " +
        "Expressions not allowed in optional nested structures. " +
        """Found: Atom("Ref1", "int1", "Int", 1, Eq(Seq(1)), None, Seq(), Seq())""")
  }

}