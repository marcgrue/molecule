package molecule.tests.core.ref.nested

import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out3._
import molecule.setup.TestSpec


class NestedMultipleLevels extends TestSpec {

  "Optional/mandatory" in new CoreSetup {

    // Can't mix mandatory/optional nested structures

    expectCompileError(
      "m(Ns.str.Refs1.*?(Ref1.int1.Refs2.*(Ref2.int2)))",
      "molecule.core.transform.exception.Dsl2ModelException: " +
        "Optional nested structure can't be mixed with mandatory nested structure.")

    expectCompileError(
      "m(Ns.str.Refs1.*(Ref1.int1.Refs2.*?(Ref2.int2)))",
      "molecule.core.transform.exception.Dsl2ModelException: " +
        "Optional nested structure can't be mixed with mandatory nested structure.")
  }


  "2 levels" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2)) insert List(
      ("a", List(
        (1, List(11, 12)),
        (2, List()),
      )),
      ("b", List())
    )

    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2)).get === List(
      ("a", List(
        (1, List(11, 12)),
      ))
    )

    Ns.str.Refs1.*?(Ref1.int1.Refs2.*?(Ref2.int2)).get.sortBy(_._1) === List(
      ("a", List(
        (1, List(11, 12)),
        (2, List()),
      )),
      ("b", List())
    )

    Ns.str.Refs1.*?(Ref1.int1).get.sortBy(_._1) === List(
      ("a", List(1, 2)),
      ("b", List())
    )

    Ref1.int1.Refs2.*?(Ref2.int2).get.sortBy(_._1) === List(
      (1, List(11, 12)),
      (2, List()),
    )
  }


  "2 levels, opt attrs" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.int1.str1$.Refs2.*(Ref2.int2.str2$)) insert List(
      ("A", List(
        (1, Some("a1"), List(
          (11, Some("a11")),
          (12, None),
        )),
        (2, None, List(
          (21, Some("a21")),
          (22, None),
        )),
        (3, Some("a3"), List()),
        (4, None, List()),
      )),
      ("B", List())
    )

    // optional - optional
    Ns.str.Refs1.*(Ref1.int1.str1$.Refs2.*(Ref2.int2.str2$)).get === List(
      ("A", List(
        (1, Some("a1"), List(
          (11, Some("a11")),
          (12, None),
        )),
        (2, None, List(
          (21, Some("a21")),
          (22, None),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1$.Refs2.*?(Ref2.int2.str2$)).get.sortBy(_._1) === List(
      ("A", List(
        (1, Some("a1"), List(
          (11, Some("a11")),
          (12, None),
        )),
        (2, None, List(
          (21, Some("a21")),
          (22, None),
        )),
        (3, Some("a3"), List()),
        (4, None, List()),
      )),
      ("B", List()),
    )

    // optional - mandatory
    Ns.str.Refs1.*(Ref1.int1.str1$.Refs2.*(Ref2.int2.str2)).get === List(
      ("A", List(
        (1, Some("a1"), List(
          (11, "a11"),
        )),
        (2, None, List(
          (21, "a21"),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1$.Refs2.*?(Ref2.int2.str2)).get.sortBy(_._1) === List(
      ("A", List(
        (1, Some("a1"), List(
          (11, "a11"),
        )),
        (2, None, List(
          (21, "a21"),
        )),
        (3, Some("a3"), List()),
        (4, None, List()),
      )),
      ("B", List()),
    )

    // optional - tacit
    Ns.str.Refs1.*(Ref1.int1.str1$.Refs2.*(Ref2.int2.str2_)).get === List(
      ("A", List(
        (1, Some("a1"), List(11)),
        (2, None, List(21)),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1$.Refs2.*?(Ref2.int2.str2_)).get.sortBy(_._1) === List(
      ("A", List(
        (1, Some("a1"), List(11)),
        (2, None, List(21)),
        (3, Some("a3"), List()),
        (4, None, List()),
      )),
      ("B", List()),
    )

    // mandatory - optional
    Ns.str.Refs1.*(Ref1.int1.str1.Refs2.*(Ref2.int2.str2$)).get === List(
      ("A", List(
        (1, "a1", List(
          (11, Some("a11")),
          (12, None),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1.Refs2.*?(Ref2.int2.str2$)).get.sortBy(_._1) === List(
      ("A", List(
        (1, "a1", List(
          (11, Some("a11")),
          (12, None),
        )),
        (3, "a3", List()),
      )),
      ("B", List()),
    )

    // mandatory - mandatory
    Ns.str.Refs1.*(Ref1.int1.str1.Refs2.*(Ref2.int2.str2)).get === List(
      ("A", List(
        (1, "a1", List(
          (11, "a11"),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1.Refs2.*?(Ref2.int2.str2)).get.sortBy(_._1) === List(
      ("A", List(
        (1, "a1", List(
          (11, "a11"),
        )),
        (3, "a3", List()),
      )),
      ("B", List()),
    )

    // mandatory - tacit
    Ns.str.Refs1.*(Ref1.int1.str1.Refs2.*(Ref2.int2.str2_)).get === List(
      ("A", List(
        (1, "a1", List(11)),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1.Refs2.*?(Ref2.int2.str2_)).get.sortBy(_._1) === List(
      ("A", List(
        (1, "a1", List(11)),
        (3, "a3", List()),
      )),
      ("B", List()),
    )

    // tacit - optional
    Ns.str.Refs1.*(Ref1.int1.str1_.Refs2.*(Ref2.int2.str2$)).get === List(
      ("A", List(
        (1, List(
          (11, Some("a11")),
          (12, None),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1_.Refs2.*?(Ref2.int2.str2$)).get.sortBy(_._1) === List(
      ("A", List(
        (1, List(
          (11, Some("a11")),
          (12, None),
        )),
        (3, List()),
      )),
      ("B", List()),
    )

    // tacit - mandatory
    Ns.str.Refs1.*(Ref1.int1.str1_.Refs2.*(Ref2.int2.str2)).get === List(
      ("A", List(
        (1, List(
          (11, "a11"),
        )),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1_.Refs2.*?(Ref2.int2.str2)).get.sortBy(_._1) === List(
      ("A", List(
        (1, List(
          (11, "a11"),
        )),
        (3, List()),
      )),
      ("B", List()),
    )

    // tacit - tacit
    Ns.str.Refs1.*(Ref1.int1.str1_.Refs2.*(Ref2.int2.str2_)).get === List(
      ("A", List(
        (1, List(11)),
      )),
    )
    Ns.str.Refs1.*?(Ref1.int1.str1_.Refs2.*?(Ref2.int2.str2_)).get.sortBy(_._1) === List(
      ("A", List(
        (1, List(11)),
        (3, List()),
      )),
      ("B", List()),
    )
  }


  "3 levels" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2.Refs3.*(Ref3.int3))) insert List(
      ("a", List(
        (1, List(
          (11, List(111, 112)),
          (12, List()),
        )),
        (2, List()),
      )),
      ("b", List())
    )

    Ns.str.Refs1.*(Ref1.int1.Refs2.*(Ref2.int2.Refs3.*(Ref3.int3))).get === List(
      ("a", List(
        (1, List(
          (11, List(111, 112)),
        ))
      ))
    )

    Ns.str.Refs1.*?(Ref1.int1.Refs2.*?(Ref2.int2.Refs3.*?(Ref3.int3))).get.sortBy(_._1) === List(
      ("a", List(
        (1, List(
          (11, List(111, 112)),
          (12, List()),
        )),
        (2, List()),
      )),
      ("b", List()),
    )
  }
}