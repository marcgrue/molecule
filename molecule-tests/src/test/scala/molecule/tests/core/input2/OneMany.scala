package molecule.tests.core.input2

import molecule.core.exceptions.MoleculeException
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in2_out3._
import molecule.core.api.exception.Molecule_2_Exception
import molecule.setup.TestSpec


class OneMany extends TestSpec {

  "Pairs" >> {

    class Setup extends CoreSetup {
      Ns.int.str$.longs$ insert List(
        (1, Some("a"), Some(Set(1L, 2L))),
        (2, Some("a"), Some(Set(2L, 3L))),
        (3, Some("a"), Some(Set(3L, 4L))),

        (4, Some("b"), Some(Set(1L, 2L))),
        (5, Some("b"), Some(Set(2L, 3L))),
        (6, Some("b"), Some(Set(3L, 4L))),

        (7, Some("a"), None),
        (8, None, None)
      )

      // Cardinality one + many input molecule
      val im = m(Ns.int.str_(?).longs_(?))
    }

    "Equality" >> {

      "4 syntaxes" in new Setup {

        // 1 pair
        im("a", Set(1L)).get === List(1) // only for 1 pair
        im(("a", Set(1L))).get === List(1)
        im(List(("a", Set(1L)))).get === List(1)
        im("a" and Set(1L)).get === List(1)

        // 2 pairs
        im(("a", Set(1L)), ("b", Set(1L))).get === List(1, 4)
        im(List(("a", Set(1L)), ("b", Set(1L)))).get === List(1, 4)
        im(("a" and Set(1L)) or ("b" and Set(1L))).get === List(1, 4)

        // 3 pairs etc..
        im(("a", Set(1L)), ("b", Set(1L)), ("b", Set(2L))).get === List(1, 4, 5)
        im(List(("a", Set(1L)), ("b", Set(1L)), ("b", Set(2L)))).get === List(1, 4, 5)
        im(("a" and Set(1L)) or ("b" and Set(1L)) or ("b" and Set(2L))).get === List(1, 4, 5)
      }


      "Set semantics" in new Setup {

        // All Set values returned in equality matches
        m(Ns.int.str_(?).longs(?)).apply("a", Set(1L)).get === List((1, Set(1L, 2L)))
      }


      "0 pairs" in new Setup {

        // If both attributes are tacit, entities with pairs of non-asserted attributes can be returned
        m(Ns.int.str_(?).longs_(?)).apply(Nil).get === List(8)

        // Mandatory attribute will not match Nil
        (m(Ns.int.str_(?).longs(?)).apply(Nil).get must throwA[Molecule_2_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_2_Exception: " +
          "Can only apply empty list of pairs (Nil) to two tacit attributes"
      }


      "1 pair" in new Setup {

        // Single Set value

        im("a", Set(1L)).get === List(1)
        im("a", Set(2L)).get === List(1, 2)
        im("a", Set(3L)).get === List(2, 3)
        im("a", Set(4L)).get === List(3)

        // Multiple Set values

        im("a", Set(1L, 2L)).get === List(1)
        im("a", Set(1L, 3L)).get === Nil
        im("a", Set(2L, 3L)).get === List(2)

        // Empty Set matches missing assertions
        im("a", Set[Long]()).get === List(7)

        // Non-matching value
        im("a", Set(5L)).get === Nil

        // Must match all values in Set
        im("a", Set(1L, 5L)).get === Nil
      }


      "Multiple pairs with non-matching pair(s)" in new Setup {

        im(("x", Set(0L)), ("a", Set(1L))).get === List(1) //     () + (1)
        im(("x", Set(0L)), ("a", Set(2L))).get === List(1, 2) //  () + (1, 2)
        im(("x", Set(0L)), ("a", Set(3L))).get === List(2, 3) //  () + (2, 3)
        im(("x", Set(0L)), ("a", Set(4L))).get === List(3) //     () + (3)
        im(("x", Set(0L)), ("a", Set(5L))).get === Nil //         () + ()

        im(("x", Set(0L)), ("a", Set(1L, 2L))).get === List(1) // () + (1)
        im(("x", Set(0L)), ("a", Set(1L, 3L))).get === Nil //     () + ()
        im(("x", Set(0L)), ("a", Set(2L, 3L))).get === List(2) // () + (2)
      }


      "Multiple pairs with shared card-one value (a)" in new Setup {

        im(("a", Set(1L)), ("a", Set(2L))).get === List(1, 2) //         (1) + (1, 2)
        im(("a", Set(1L)), ("a", Set(3L))).get === List(1, 2, 3) //      (1) + (2, 3)
        im(("a", Set(1L)), ("a", Set(4L))).get === List(1, 3) //         (1) + (3)
        im(("a", Set(1L)), ("a", Set(5L))).get === List(1) //            (1) + ()
        im(("a", Set(1L)), ("a", Set[Long]())).get === List(1, 7) //     (1) + (7)

        im(("a", Set(1L, 2L)), ("a", Set(1L))).get === List(1) //        (1) + (1)
        im(("a", Set(1L, 2L)), ("a", Set(2L))).get === List(1, 2) //     (1) + (1, 2)
        im(("a", Set(1L, 2L)), ("a", Set(3L))).get === List(1, 2, 3) //  (1) + (2, 3)
        im(("a", Set(1L, 2L)), ("a", Set(4L))).get === List(1, 3) //     (1) + (3)
        im(("a", Set(1L, 2L)), ("a", Set(5L))).get === List(1) //        (1) + ()

        im(("a", Set(2L)), ("a", Set(1L))).get === List(1, 2) //         (1, 2) + (1)
        im(("a", Set(2L)), ("a", Set(3L))).get === List(1, 2, 3) //      (1, 2) + (2, 3)
        im(("a", Set(2L)), ("a", Set(4L))).get === List(1, 2, 3) //      (1, 2) + (3)
        im(("a", Set(2L)), ("a", Set(5L))).get === List(1, 2) //         (1, 2) + ()


        im(("a", Set(1L)), ("a", Set(1L, 2L))).get === List(1) //        (1) + (1)
        im(("a", Set(1L)), ("a", Set(1L, 3L))).get === List(1) //        (1) + ()
        im(("a", Set(1L)), ("a", Set(2L, 3L))).get === List(1, 2) //     (1) + (2)
        im(("a", Set(1L)), ("a", Set(3L, 4L))).get === List(1, 3) //     (1) + (3)

        im(("a", Set(1L, 2L)), ("a", Set(1L, 3L))).get === List(1) //    (1) + ()
        im(("a", Set(1L, 2L)), ("a", Set(2L, 3L))).get === List(1, 2) // (1) + (2)
        im(("a", Set(1L, 2L)), ("a", Set(3L, 4L))).get === List(1, 3) // (1) + (3)

        im(("a", Set(2L)), ("a", Set(1L, 2L))).get === List(1, 2) //     (1, 2) + (1)
        im(("a", Set(2L)), ("a", Set(1L, 3L))).get === List(1, 2) //     (1, 2) + ()
        im(("a", Set(2L)), ("a", Set(2L, 3L))).get === List(1, 2) //     (1, 2) + (2)
        im(("a", Set(2L)), ("a", Set(3L, 4L))).get === List(1, 2, 3) //  (1, 2) + (3)
      }


      "Multiple pairs" in new Setup {

        im(("a", Set(1L)), ("b", Set(2L))).get === List(1, 4, 5) //      (1) + (4, 5)
        im(("a", Set(1L)), ("b", Set(3L))).get === List(1, 5, 6) //      (1) + (5, 6)
        im(("a", Set(1L)), ("b", Set(4L))).get === List(1, 6) //         (1) + (6)
        im(("a", Set(1L)), ("b", Set(5L))).get === List(1) //            (1) + ()

        im(("a", Set(1L, 2L)), ("b", Set(1L))).get === List(1, 4) //     (1) + (4)
        im(("a", Set(1L, 2L)), ("b", Set(2L))).get === List(1, 4, 5) //  (1) + (4, 5)
        im(("a", Set(1L, 2L)), ("b", Set(3L))).get === List(1, 5, 6) //  (1) + (5, 6)
        im(("a", Set(1L, 2L)), ("b", Set(4L))).get === List(1, 6) //     (1) + (6)
        im(("a", Set(1L, 2L)), ("b", Set(5L))).get === List(1) //        (1) + ()

        im(("a", Set(2L)), ("b", Set(1L))).get === List(1, 2, 4) //      (1, 2) + (4)
        im(("a", Set(2L)), ("b", Set(3L))).get === List(1, 2, 5, 6) //   (1, 2) + (5, 6)
        im(("a", Set(2L)), ("b", Set(4L))).get === List(1, 2, 6) //      (1, 2) + (6)
        im(("a", Set(2L)), ("b", Set(5L))).get === List(1, 2) //         (1, 2) + ()


        im(("a", Set(1L)), ("b", Set(1L, 2L))).get === List(1, 4) //     (1) + (4)
        im(("a", Set(1L)), ("b", Set(1L, 3L))).get === List(1) //        (1) + ()
        im(("a", Set(1L)), ("b", Set(2L, 3L))).get === List(1, 5) //     (1) + (5)
        im(("a", Set(1L)), ("b", Set(3L, 4L))).get === List(1, 6) //     (1) + (6)

        im(("a", Set(1L, 2L)), ("b", Set(1L, 3L))).get === List(1) //    (1) + ()
        im(("a", Set(1L, 2L)), ("b", Set(2L, 3L))).get === List(1, 5) // (1) + (5)
        im(("a", Set(1L, 2L)), ("b", Set(3L, 4L))).get === List(1, 6) // (1) + (6)

        im(("a", Set(2L)), ("b", Set(1L, 2L))).get === List(1, 2, 4) //  (1, 2) + (4)
        im(("a", Set(2L)), ("b", Set(1L, 3L))).get === List(1, 2) //     (1, 2) + ()
        im(("a", Set(2L)), ("b", Set(2L, 3L))).get === List(1, 2, 5) //  (1, 2) + (5)
        im(("a", Set(2L)), ("b", Set(3L, 4L))).get === List(1, 2, 6) //  (1, 2) + (6)
      }


      "Pairs in different namespaces" in new CoreSetup {

        Ns.int.str.Refs1.ints1 insert List(
          (1, "a", Set(1, 2)),
          (2, "a", Set(2, 3)),
          (3, "a", Set(3, 4)),

          (4, "b", Set(1, 2)),
          (5, "b", Set(2, 3)),
          (6, "b", Set(3, 4))
        )

        Ns.int.str$ insert List(
          (7, Some("a")),
          (8, None)
        )

        // Cardinality one + many input molecule
        val im = m(Ns.int.str_(?).Refs1.ints1_(?))

        im(("a", Set(1)), ("b", Set(2))).get === List(1, 4, 5) //      (1) + (4, 5)
        im(("a", Set(1)), ("b", Set(3))).get === List(1, 5, 6) //      (1) + (5, 6)
        im(("a", Set(1)), ("b", Set(4))).get === List(1, 6) //         (1) + (6)
        im(("a", Set(1)), ("b", Set(5))).get === List(1) //            (1) + ()

        im(("a", Set(1, 2)), ("b", Set(1))).get === List(1, 4) //     (1) + (4)
        im(("a", Set(1, 2)), ("b", Set(2))).get === List(1, 4, 5) //  (1) + (4, 5)
        im(("a", Set(1, 2)), ("b", Set(3))).get === List(1, 5, 6) //  (1) + (5, 6)
        im(("a", Set(1, 2)), ("b", Set(4))).get === List(1, 6) //     (1) + (6)
        im(("a", Set(1, 2)), ("b", Set(5))).get === List(1) //        (1) + ()

        im(("a", Set(2)), ("b", Set(1))).get === List(1, 2, 4) //      (1, 2) + (4)
        im(("a", Set(2)), ("b", Set(3))).get === List(1, 2, 5, 6) //   (1, 2) + (5, 6)
        im(("a", Set(2)), ("b", Set(4))).get === List(1, 2, 6) //      (1, 2) + (6)
        im(("a", Set(2)), ("b", Set(5))).get === List(1, 2) //         (1, 2) + ()


        im(("a", Set(1)), ("b", Set(1, 2))).get === List(1, 4) //     (1) + (4)
        im(("a", Set(1)), ("b", Set(1, 3))).get === List(1) //        (1) + ()
        im(("a", Set(1)), ("b", Set(2, 3))).get === List(1, 5) //     (1) + (5)
        im(("a", Set(1)), ("b", Set(3, 4))).get === List(1, 6) //     (1) + (6)

        im(("a", Set(1, 2)), ("b", Set(1, 3))).get === List(1) //    (1) + ()
        im(("a", Set(1, 2)), ("b", Set(2, 3))).get === List(1, 5) // (1) + (5)
        im(("a", Set(1, 2)), ("b", Set(3, 4))).get === List(1, 6) // (1) + (6)

        im(("a", Set(2)), ("b", Set(1, 2))).get === List(1, 2, 4) //  (1, 2) + (4)
        im(("a", Set(2)), ("b", Set(1, 3))).get === List(1, 2) //     (1, 2) + ()
        im(("a", Set(2)), ("b", Set(2, 3))).get === List(1, 2, 5) //  (1, 2) + (5)
        im(("a", Set(2)), ("b", Set(3, 4))).get === List(1, 2, 6) //  (1, 2) + (6)
      }
    }


    "Expressions" >> {

      "Negation" in new Setup {

        m(Ns.int.str_(?).longs.not(?))("a", Set(1L)).get === List((2, Set(2L, 3L)), (3, Set(3L, 4L)))
        m(Ns.int.str_(?).longs.not(?))("a", Set(2L)).get === List((3, Set(3L, 4L)))
        m(Ns.int.str_(?).longs.not(?))("a", Set(1L, 2L)).get === List((2, Set(2L, 3L)), (3, Set(3L, 4L)))
        m(Ns.int.str_(?).longs.not(?))("a", Set(1L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
        // Same as
        m(Ns.int.str_.not(?).longs.not(?))("b", Set(1L)).get === List((2, Set(2L, 3L)), (3, Set(3L, 4L)))
        m(Ns.int.str_.not(?).longs.not(?))("b", Set(2L)).get === List((3, Set(3L, 4L)))
        m(Ns.int.str_.not(?).longs.not(?))("b", Set(1L, 2L)).get === List((2, Set(2L, 3L)), (3, Set(3L, 4L)))
        m(Ns.int.str_.not(?).longs.not(?))("b", Set(1L, 3L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))

        m(Ns.int.str_.not(?).longs(?))("b", Set(1L)).get === List((1, Set(1L, 2L)))
        m(Ns.int.str_.not(?).longs(?))("b", Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
        m(Ns.int.str_.not(?).longs(?))("b", Set(1L, 2L)).get === List((1, Set(1L, 2L)))
        m(Ns.int.str_.not(?).longs(?))("b", Set(1L, 3L)).get === Nil


        // Applying multiple pairs to input molecules with expression not allowed

        // Card one
        (m(Ns.int.str_.not(?).longs(?))(Seq(("a", Set(1L)), ("b", Set(2L)))).get must throwA[Molecule_2_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_2_Exception: " +
          "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"

        // Card many
        (m(Ns.int.str_(?).longs.not(?))(Seq(("a", Set(1L)), ("b", Set(2L)))).get must throwA[Molecule_2_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_2_Exception: " +
          "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"

        // Card one + many
        (m(Ns.int.str_.not(?).longs.not(?))(Seq(("a", Set(1L)), ("b", Set(2L)))).get must throwA[Molecule_2_Exception])
          .message === "Got the exception molecule.core.input.exception.Molecule_2_Exception: " +
          "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"
      }


      "Comparison" in new Setup {

        // 1 comparison on card-one attribute

        // "a"
        m(Ns.int.str_.<(?).longs(?))("b", Set(1L)).get === List((1, Set(1L, 2L)))
        m(Ns.int.str_.<(?).longs(?))("b", Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)))
        m(Ns.int.str_.<(?).longs(?))("b", Set(1L, 2L)).get === List((1, Set(1L, 2L)))

        // "a" + "b"
        m(Ns.int.str_.<=(?).longs(?))("b", Set(1L)).get === List((1, Set(1L, 2L)), (4, Set(1L, 2L)))
        m(Ns.int.str_.<=(?).longs(?))("b", Set(2L)).get === List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (4, Set(1L, 2L)), (5, Set(2L, 3L)))
        m(Ns.int.str_.<=(?).longs(?))("b", Set(1L, 2L)).get === List((1, Set(1L, 2L)), (4, Set(1L, 2L)))


        // 1 comparison on card-many attribute

        m(Ns.int.str_(?).longs.<(?))("a", Set(1L)).get === Nil
        m(Ns.int.str_(?).longs.<(?))("a", Set(2L)).get === List((1, Set(1L))) // Note that 2L is filtered out
        m(Ns.int.str_(?).longs.<(?))("a", Set(3L)).get === List((1, Set(1L, 2L)), (2, Set(2L)))

        // Can't apply multiple values to comparison function
        (m(Ns.int.str_(?).longs.<(?))("a", Set(2L, 3L)).get must throwA[MoleculeException])
          .message === "Got the exception molecule.core.input.exception.MoleculeException: " +
          "Can't apply multiple values to comparison function."
      }


      "Multiple expressions" in new Setup {

        m(Ns.int.str_.>(?).longs.>=(?))("a", Set(1L)).get === List((4, Set(1L, 2L)), (5, Set(2L, 3L)), (6, Set(3L, 4L)))
        m(Ns.int.str_.>(?).longs.>=(?))("a", Set(2L)).get === List((4, Set(2L)), (5, Set(2L, 3L)), (6, Set(3L, 4L)))
        m(Ns.int.str_.>(?).longs.>=(?))("a", Set(3L)).get === List((5, Set(3L)), (6, Set(3L, 4L)))
        m(Ns.int.str_.>("a").longs.>=(3L)).get === List((5, Set(3L)), (6, Set(3L, 4L)))

        // Filtered values returned in comparison matches
        m(Ns.int.str_(?).longs.<(?))("a", Set(2L)).get === List((1, Set(1L))) // 2 not returned
      }
    }
  }


  "Groups" >> {

    class Setup extends CoreSetup {
      Ns.int.str$.longs$ insert List(
        (1, Some("a"), Some(Set(1L))),
        (2, Some("a"), Some(Set(2L))),
        (3, Some("b"), Some(Set(2L))),
        (4, Some("a"), None),
        (5, Some("b"), None),
        (6, Some("b"), None),
        (7, None, Some(Set(1L))),
        (8, None, Some(Set(2L))),
        (9, None, Some(Set(2L))),
        (10, None, None)
      )

      // Cardinality one + many input molecule
      val im = m(Ns.int.str_(?).longs_(?))
    }


    "Empty group(s)" in new Setup {

      // Empty group matches non-asserted attribute

      im(Nil, Nil).get === List(10)

      im(List("a"), Nil).get === List(4)
      im(List("b"), Nil).get === List(5, 6)
      im(List("a", "b"), Nil).get === List(4, 5, 6)

      im(Nil, List(Set(1L))).get === List(7)
      im(Nil, List(Set(2L))).get === List(8, 9)
      im(Nil, List(Set(1L), Set(2L))).get === List(7, 8, 9)
    }


    "AND" in new Setup {

      // AND semantics between groups

      im(List("a"), List(Set(1L))).get === List(1)
      im(List("a"), List(Set(2L))).get === List(2)
      im(List("a"), List(Set(1L, 2L))).get === Nil
      im(List("a"), List(Set(1L), Set(2L))).get === List(1, 2)

      im(List("b"), List(Set(1L))).get === Nil
      im(List("b"), List(Set(2L))).get === List(3)
      im(List("b"), List(Set(1L, 2L))).get === Nil

      im(List("b"), List(Set(1L), Set(2L))).get === List(3)

      im(List("a", "b"), List(Set(1L))).get === List(1)
      im(List("a", "b"), List(Set(2L))).get === List(2, 3)
      im(List("a", "b"), List(Set(1L), Set(2L))).get === List(1, 2, 3)


      // Same, with Logic syntax

      im("a" and Set(1L)).get === List(1)
      im("a" and Set(2L)).get === List(2)
      im("a" and (Set(1L) or Set(2L))).get === List(1, 2)
      im("b" and Set(1L)).get === Nil
      im("b" and Set(2L)).get === List(3)
      im("b" and (Set(1L) or Set(2L))).get === List(3)
      im(("a" or "b") and Set(1L)).get === List(1)
      im(("a" or "b") and Set(2L)).get === List(2, 3)
      im(("a" or "b") and (Set(1L) or Set(2L))).get === List(1, 2, 3)
    }
  }
}