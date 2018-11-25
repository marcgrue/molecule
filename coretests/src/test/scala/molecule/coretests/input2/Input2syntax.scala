package molecule.coretests.input2

import molecule.api.in2_out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.input.exception.{InputMoleculeException, InputMolecule_2_Exception}


class Input2syntax extends CoreSpec {


  "Card one + one" >> {

    class OneSetup extends CoreSetup {
      Ns.str.int$.long$ insert List(
        ("Ann", Some(37), Some(5L)),
        ("Ben", Some(28), Some(5L)),
        ("Joe", Some(28), Some(4L)),
        ("Liz", Some(28), Some(3L)),
        ("Stu", Some(28), None),
        ("Tim", None, Some(3L)),
        ("Uma", None, None)
      )
      val personOfAgeAndStatus = m(Ns.str.int_(?).long_(?))
    }


    "Pairs" in new OneSetup {

      // Match specific pairs of input

      // 0 pairs (both input attributes non-asserted)
      personOfAgeAndStatus.apply(Nil).get === List("Uma")

      // 1 Pair
      personOfAgeAndStatus.apply(37, 5L).get === List("Ann")
      personOfAgeAndStatus.apply((37, 5L)).get === List("Ann")
      personOfAgeAndStatus.apply(Seq((37, 5L))).get === List("Ann")

      // 2 pairs
      personOfAgeAndStatus.apply((37, 5L), (28, 5L)).get.sorted === List("Ann", "Ben")
      personOfAgeAndStatus.apply(Seq((37, 5L), (28, 5L))).get.sorted === List("Ann", "Ben")
      personOfAgeAndStatus.apply((37 and 5L) or (28 and 5L)).get.sorted === List("Ann", "Ben")

      // 3 pairs
      personOfAgeAndStatus.apply((37, 5L), (28, 5L), (28, 4L)).get.sorted === List("Ann", "Ben", "Joe")
      personOfAgeAndStatus.apply(Seq((37, 5L), (28, 5L), (28, 4L))).get.sorted === List("Ann", "Ben", "Joe")
      personOfAgeAndStatus.apply((37 and 5L) or (28 and 5L) or (28 and 4L)).get.sorted === List("Ann", "Ben", "Joe")

      // etc..
    }


    "2 groups - one way to see it" in new OneSetup {

      // Match any combination of first and second values


      // Explicit `and` semantics between 2 logical groups, respectively matching each input attribute independently
      // Each group matches value1 `or` value2 `or` etc..

      personOfAgeAndStatus.apply(28 and 5L).get === List("Ben")
      personOfAgeAndStatus.apply(28 and (5L or 4L)).get === List("Ben", "Joe")
      personOfAgeAndStatus.apply(28 and (5L or 4L or 3L)).get === List("Ben", "Liz", "Joe")

      personOfAgeAndStatus.apply(37 and 5L).get === List("Ann")
      personOfAgeAndStatus.apply((37 or 28) and 5L).get.sorted === List("Ann", "Ben")
      personOfAgeAndStatus.apply((37 or 28) and 4L).get.sorted === List("Joe")
      personOfAgeAndStatus.apply((37 or 28) and 3L).get.sorted === List("Liz")

      personOfAgeAndStatus.apply((37 or 28) and (5L or 4L)).get.sorted === List("Ann", "Ben", "Joe")
      personOfAgeAndStatus.apply((37 or 28) and (5L or 4L or 3L)).get.sorted === List("Ann", "Ben", "Joe", "Liz")


      // 2 lists of values, respectively matching each input attribute

      personOfAgeAndStatus.apply(Seq(28), Seq(5L)).get === List("Ben")
      personOfAgeAndStatus.apply(Seq(28), Seq(5L, 4L)).get === List("Ben", "Joe")
      personOfAgeAndStatus.apply(Seq(28), Seq(5L, 4L, 3L)).get === List("Ben", "Liz", "Joe")

      personOfAgeAndStatus.apply(Seq(37), Seq(5L)).get === List("Ann")
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L)).get.sorted === List("Ann", "Ben")
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(4L)).get.sorted === List("Joe")
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(3L)).get.sorted === List("Liz")

      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L, 4L)).get.sorted === List("Ann", "Ben", "Joe")
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L, 4L, 3L)).get.sorted === List("Ann", "Ben", "Joe", "Liz")


      // No input returns Nil
      personOfAgeAndStatus.apply(Seq(28), Nil).get === List("Stu")
      personOfAgeAndStatus.apply(Nil, Seq(3L)).get === List("Tim")
      personOfAgeAndStatus.apply(Nil, Nil).get === List("Uma")
    }


    "2 groups - another way to see it" in new OneSetup {

      // Match any combination of first and second values

      // 1 + 1
      // --------
      // 37-5 Ann
      personOfAgeAndStatus.apply(Seq(37), Seq(5L)).get === List("Ann")
      personOfAgeAndStatus.apply(37 and 5L).get === List("Ann")

      // 2 + 1
      // --------
      // 37-5 Ann
      // 28-5 Ben
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L)).get.sorted === List("Ann", "Ben")
      personOfAgeAndStatus.apply((37 or 28) and 5L).get.sorted === List("Ann", "Ben")

      // 1 + 2
      // --------
      // 28-5 Ben
      // 28-4 Joe
      personOfAgeAndStatus.apply(Seq(28), Seq(5L, 4L)).get.sorted === List("Ben", "Joe")
      personOfAgeAndStatus.apply(28 and (5L or 4L)).get === List("Ben", "Joe")

      // 2 + 2
      // --------
      // 37-5 Ann
      // 37-4
      // 28-5 Ben
      // 28-4 Joe
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L, 4L)).get.sorted === List("Ann", "Ben", "Joe")
      personOfAgeAndStatus.apply((37 or 28) and (5L or 4L)).get.sorted === List("Ann", "Ben", "Joe")

      // 2 + 3
      // --------
      // 37-5 Ann
      // 37-4
      // 37-3
      // 28-5 Ben
      // 28-4 Joe
      // 28-3 Liz
      personOfAgeAndStatus.apply(Seq(37, 28), Seq(5L, 4L, 3L)).get.sorted === List("Ann", "Ben", "Joe", "Liz")
      personOfAgeAndStatus.apply((37 or 28) and (5L or 4L or 3L)).get.sorted === List("Ann", "Ben", "Joe", "Liz")


      // No input returns Nil
      personOfAgeAndStatus.apply(Seq(28), Nil).get === List("Stu")
      personOfAgeAndStatus.apply(Nil, Seq(3L)).get === List("Tim")
      personOfAgeAndStatus.apply(Nil, Nil).get === List("Uma")
    }


    "expressions" in new CoreSetup {

      Ns.str.int.long insert List(
        ("a", 1, 1L),
        ("b", 1, 1L),
        ("c", 2, 1L),
        ("d", 3, 2L),
        ("e", 4, 3L)
      )

      val inputExpression = m(Ns.str.int_.>(?).long_.<=(?))

      // 1 pair

      inputExpression.apply(1, 2L).get === List("c", "d")
      inputExpression.apply(2, 3L).get === List("d", "e")

      inputExpression.apply((1, 2L)).get === List("c", "d")
      inputExpression.apply((2, 3L)).get === List("d", "e")

      inputExpression.apply(Seq((1, 2L))).get === List("c", "d")
      inputExpression.apply(Seq((2, 3L))).get === List("d", "e")

      // Applying no pairs returns empty result
      inputExpression.apply(Nil).get === Nil

      // Multiple pairs
      // Compare functions expects only one argument, so multiple input pairs are not allowed
      (inputExpression.apply((1, 2L), (1, 3L)).get must throwA[InputMolecule_2_Exception])
        .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
        "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"
    }
  }


  "Card one + many" >> {

    class OneManySetup extends CoreSetup {
      Ns.str.long$.ints$ insert List(
        ("a", Some(1L), Some(Set(1, 2))),
        ("b", Some(1L), Some(Set(2, 3))),
        ("c", Some(1L), Some(Set(3, 4))),
        ("d", Some(1L), Some(Set(3, 4, 5))),
        ("e", Some(1L), None),
        ("f", Some(2L), Some(Set(1, 2))),
        ("g", Some(2L), Some(Set(2, 3))),
        ("h", Some(2L), Some(Set(3, 4))),
        ("i", Some(2L), Some(Set(3, 4, 5))),
        ("j", Some(2L), None),
        ("k", None, Some(Set(6, 7))),
        ("l", None, None)
      )
      val inputMolecule = m(Ns.str.long_(?).ints_(?))
    }


    "Pairs" in new OneManySetup {

      // 0 pairs ...................................................

      // Both input attributes non-asserted)
      inputMolecule.apply(Nil).get === List("l")


      // 1 pair ...................................................

      inputMolecule.apply(1L, Set(1)).get === List("a")
      inputMolecule.apply(1L, Set(2)).get === List("a", "b")
      inputMolecule.apply(1L, Set(3)).get === List("b", "c", "d")
      inputMolecule.apply(1L, Set(4)).get === List("c", "d")
      inputMolecule.apply(1L, Set(5)).get === List("d")

      // Empty Set matches non-asserted card-many attribute
      inputMolecule.apply(1L, Set[Int]()).get === List("e")

      inputMolecule.apply(1L, Set(1, 2)).get === List("a")
      inputMolecule.apply(1L, Set(1, 3)).get === Nil
      inputMolecule.apply(1L, Set(2, 3)).get === List("b")
      inputMolecule.apply(1L, Set(3, 4)).get === List("c", "d")
      inputMolecule.apply(1L, Set(3, 4, 5)).get === List("d")


      // Multiple pairs ...................................................

      // Duplicate pairs coalesce
      inputMolecule.apply((1L, Set(1, 2)), (1L, Set(1, 2))).get === List("a")

      inputMolecule.apply((1L, Set(2)), (1L, Set(2, 3))).get === List("a", "b")
      inputMolecule.apply((1L, Set(2)), (1L, Set(3, 4))).get === List("a", "b", "c", "d")
      inputMolecule.apply((1L, Set(2)), (1L, Set(3, 4, 5))).get === List("a", "b", "d")

      inputMolecule.apply((1L, Set(1, 2)), (1L, Set(3, 4, 5))).get === List("a", "d")
      inputMolecule.apply((1L, Set(1, 2)), (1L, Set(3, 4))).get === List("a", "c", "d")
      inputMolecule.apply((1L, Set(1, 2)), (1L, Set(2, 3))).get === List("a", "b")
      inputMolecule.apply((1L, Set(1, 2)), (1L, Set[Int]())).get === List("a", "e")
      inputMolecule.apply((1L, Set(1, 3)), (1L, Set[Int]())).get === List("e")
      inputMolecule.apply((1L, Set(1, 3)), (2L, Set[Int]())).get === List("j")
      inputMolecule.apply((1L, Set(1, 3)), (3L, Set[Int]())).get === Nil


      // Explicit `or` semantics between pairs (each pair matches value1 `and` value2)
      inputMolecule.apply((1L and Set(2)) or (1L and Set(3, 4, 5))).get === List("a", "b", "d")

      // Seq of pairs
      inputMolecule.apply(Seq((1L, Set(2)), (1L, Set(3, 4, 5)))).get === List("a", "b", "d")
    }


    "Groups" in new OneManySetup {

      // Explicit `and` semantics between 2 logical groups, respectively matching each input attribute
      // Each group matches value1 `or` value2 `or` etc..

      inputMolecule.apply(1L and Set(1)).get === List("a")

      inputMolecule.apply(1L and Set(1, 2)).get === List("a")
      inputMolecule.apply(1L and (Set(1) or Set(2))).get === List("a", "b")

      inputMolecule.apply(1L and Set(1, 2, 3)).get === Nil
      inputMolecule.apply(1L and (Set(1, 2) or Set(3))).get === List("a", "b", "c", "d")
      inputMolecule.apply(1L and (Set(1) or Set(2, 3))).get === List("a", "b")
      inputMolecule.apply(1L and (Set(1) or Set(2) or Set(3))).get === List("a", "b", "c", "d")


      inputMolecule.apply((1L or 2L) and Set(1)).get === List("a", "f")

      inputMolecule.apply((1L or 2L) and Set(1, 2)).get === List("a", "f")
      inputMolecule.apply((1L or 2L) and (Set(1) or Set(2))).get === List("a", "b", "f", "g")

      inputMolecule.apply((1L or 2L) and Set(1, 2, 3)).get === Nil
      inputMolecule.apply((1L or 2L) and (Set(1, 2) or Set(3))).get === List("a", "b", "c", "d", "f", "g", "h", "i")
      inputMolecule.apply((1L or 2L) and (Set(1) or Set(2, 3))).get === List("a", "b", "f", "g")
      inputMolecule.apply((1L or 2L) and (Set(1) or Set(2) or Set(3))).get === List("a", "b", "c", "d", "f", "g", "h", "i")


      // 2 lists of values, respectively matching each input attribute

      inputMolecule.apply(Seq(1L), Seq(Set(1))).get === List("a")
      inputMolecule.apply(Seq(1L), Seq(Set(1, 2))).get === List("a")

      // Nil matches non-asserted attributes
      inputMolecule.apply(Seq(1L), Nil).get === List("e")
      inputMolecule.apply(Nil, Seq(Set(6, 7))).get === List("k")
      inputMolecule.apply(Nil, Nil).get === List("l")
    }


    "expressions" in new OneManySetup {

      val inputExpression = m(Ns.str.long_.>=(?).ints_.<(?))

      // 1 pair of input values allowed

      inputExpression.apply(2L, Set(1)).get === Nil
      inputExpression.apply(2L, Set(2)).get === List("f")


      // Comparison functions set limit on inputs

      (inputExpression.apply(2L, Set(1, 2)).get must throwA[InputMoleculeException])
        .message === "Got the exception molecule.input.exception.InputMoleculeException: " +
        "Can't apply multiple values to comparison function."

      (inputExpression.apply((1L, Set(1)), (2L, Set(2))).get must throwA[InputMolecule_2_Exception])
        .message === "Got the exception molecule.input.exception.InputMolecule_2_Exception: " +
        "Can't apply multiple pairs to input attributes with one or more expressions (<, >, <=, >=, !=)"


      // Mixing comparison/equality

      val inputExpression2 = m(Ns.str.long_.>=(?).ints_(?))

      inputExpression2.apply(1L, Set(1, 2)).get === List("a", "f")
    }
  }
}