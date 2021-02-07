package molecule.tests.core.input2

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in2_out3._
import molecule.TestSpec


class OneOne extends TestSpec {

  "Pairs" >> {

    class Setup extends CoreSetup {
      Ns.int.str$.long$ insert List(
        (1, Some("a"), Some(1L)),
        (2, Some("a"), Some(2L)),
        (3, Some("b"), Some(2L)),
        (4, None, None)
      )

      // Cardinality one + one input molecule
      val im = m(Ns.int.str_(?).long_(?))
    }

    "4 syntaxes" in new Setup {
      // 4 syntaxes for applying a pair of values
      im("b", 2L).get === List(3) // when only 1 pair
      im(("a", 3L), ("b", 1L)).get === Nil
      im(("a" and 3L) or ("b" and 1L)).get === Nil
      im(List(("a", 3L), ("b", 1L))).get === Nil
    }


    "0 pairs" in new Setup {
      // No input pairs match pairs of non-asserted attributes
      im(Nil).get === List(4)
    }


    "1 pair" in new Setup {

      im("a", 1L).get === List(1)
      im("a", 2L).get === List(2)

      im("b", 1L).get === Nil
      im("b", 2L).get === List(3)
    }


    "Multiple pairs" in new Setup {

      val (p1, p2, p0, p3) = (
        ("a", 1L),
        ("a", 2L),
        ("b", 1L),
        ("b", 2L)
      )

      // Redundant pairs are truncated
      im(p1, p1).get === List(1)
      // Becomes ame as
      im(p1).get === List(1)


      im(p1, p2).get === List(1, 2)
      im(p1, p0).get === List(1)
      im(p1, p3).get === List(1, 3)

      im(p2, p1).get === List(1, 2)
      im(p2, p0).get === List(2)
      im(p2, p3).get === List(2, 3)

      im(p0, p1).get === List(1)
      im(p0, p2).get === List(2)
      im(p0, p3).get === List(3)

      im(p3, p1).get === List(1, 3)
      im(p3, p2).get === List(2, 3)
      im(p3, p0).get === List(3)
    }
  }


  "Groups" >> {

    class Setup extends CoreSetup {
      Ns.int.str$.long$ insert List(
        (0, None, None),
        (1, Some("a"), Some(1L)),
        (2, Some("a"), Some(2L)),
        (3, Some("b"), Some(2L)),
        (4, Some("a"), None),
        (5, Some("b"), None),
        (6, Some("b"), None),
        (7, None, Some(1L)),
        (8, None, Some(2L)),
        (9, None, Some(2L))
      )

      // Cardinality one + one input molecule
      val im = m(Ns.int.str_(?).long_(?))
    }


    "Empty group(s)" in new Setup {

      // Empty group matches non-asserted attribute

      im(Nil, Nil).get === List(0)

      im(List("a"), Nil).get === List(4)
      im(List("b"), Nil).get === List(5, 6)
      im(List("a", "b"), Nil).get === List(4, 5, 6)

      im(Nil, List(1L)).get === List(7)
      im(Nil, List(2L)).get === List(8, 9)
      im(Nil, List(1L, 2L)).get === List(7, 8, 9)
    }


    "AND" in new Setup {

      // AND semantics between groups

      im(List("a"), List(1L)).get === List(1)
      im(List("a"), List(2L)).get === List(2)
      im(List("a"), List(1L, 2L)).get === List(1, 2)

      im(List("b"), List(1L)).get === Nil
      im(List("b"), List(2L)).get === List(3)
      im(List("b"), List(1L, 2L)).get === List(3)

      im(List("a", "b"), List(1L)).get === List(1)
      im(List("a", "b"), List(2L)).get === List(2, 3)
      im(List("a", "b"), List(1L, 2L)).get === List(1, 2, 3)


      // Same, with Logic syntax

      im("a" and 1L).get === List(1)
      im("a" and 2L).get === List(2)
      im("a" and (1L or 2L)).get === List(1, 2)
      im("b" and 1L).get === Nil
      im("b" and 2L).get === List(3)
      im("b" and (1L or 2L)).get === List(3)
      im(("a" or "b") and 1L).get === List(1)
      im(("a" or "b") and 2L).get === List(2, 3)
      im(("a" or "b") and (1L or 2L)).get === List(1, 2, 3)
    }
  }
}