package molecule.tests.core.input3

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.in3_out4._
import molecule.TestSpec


class Input3examples extends TestSpec {


  "6 ways to apply input to arity-3 input molecule" in new CoreSetup {

    Ns.str.int.long.double insert List(
      ("Ann", 37, 5L, 1.0),
      ("Ben", 28, 5L, 1.0),
      ("Joe", 28, 4L, 1.0),
      ("Liz", 28, 3L, 2.0)
    )

    val inputMolecule = m(Ns.str.int_(?).long_(?).double_(?))


    // Triples of input ..............................................................

    // Varargs (single triple)
    inputMolecule.apply(37, 5L, 1.0).get === List("Ann")

    // One or more triples
    inputMolecule.apply((37, 5L, 1.0)).get === List("Ann")
    inputMolecule.apply((37, 5L, 1.0), (28, 5L, 1.0)).get.sorted === List("Ann", "Ben")

    // One or more logical triples
    inputMolecule.apply((37 and 5L and 1.0) or (28 and 4L and 1.0)).get.sorted === List("Ann", "Joe")
    inputMolecule.apply((37 and 5L and 1.0) or (28 and 4L and 1.0) or (28 and 3L and 2.0)).get.sorted === List("Ann", "Joe", "Liz")

    // List of triples
    inputMolecule.apply(Seq((37, 5L, 1.0))).get === List("Ann")
    inputMolecule.apply(Seq((37, 5L, 1.0), (28, 5L, 1.0))).get.sorted === List("Ann", "Ben")


    // 3 groups of input, each group matches an input attribute ......................

    inputMolecule.apply(Seq(37), Seq(5L), Seq(1.0)).get === List("Ann")

    inputMolecule.apply(Seq(37, 28), Seq(5L), Seq(1.0)).get.sorted === List("Ann", "Ben")
    inputMolecule.apply(Seq(37), Seq(5L, 4L), Seq(1.0)).get.sorted === List("Ann")
    inputMolecule.apply(Seq(37), Seq(5L), Seq(1.0, 2.0)).get.sorted === List("Ann")

    inputMolecule.apply(Seq(37, 28), Seq(5L, 4L), Seq(1.0)).get.sorted === List("Ann", "Ben", "Joe")
    inputMolecule.apply(Seq(37, 28), Seq(5L), Seq(1.0, 2.0)).get.sorted === List("Ann", "Ben")
    inputMolecule.apply(Seq(28), Seq(5L, 4L), Seq(1.0, 2.0)).get.sorted === List("Ben", "Joe")

    inputMolecule.apply(Seq(37, 28), Seq(5L, 4L), Seq(1.0, 2.0)).get.sorted === List("Ann", "Ben", "Joe")

    // Nil as any input returns Nil
    inputMolecule.apply(Seq(37), Seq(5L), Nil).get === Nil
    inputMolecule.apply(Seq(37), Nil, Seq(1.0)).get === Nil
    inputMolecule.apply(Nil, Seq(5L), Seq(1.0)).get === Nil


    // 3 groups all with 1 value
    inputMolecule.apply(37 and 5L and 1.0).get === List("Ann")

    // 1 group with 2 values
    inputMolecule.apply((37 or 28) and 5L and 1.0).get.sorted === List("Ann", "Ben")
    inputMolecule.apply(37 and (5L or 4L) and 1.0).get.sorted === List("Ann")
    inputMolecule.apply(37 and 5L and (1.0 or 2.0)).get.sorted === List("Ann")

    // 2 groups with 2 values
    inputMolecule.apply((37 or 28) and (5L or 4L) and 1.0).get.sorted === List("Ann", "Ben", "Joe")
    inputMolecule.apply((37 or 28) and 5L and (1.0 or 2.0)).get.sorted === List("Ann", "Ben")
    inputMolecule.apply(37 and (5L or 4L) and (1.0 or 2.0)).get.sorted === List("Ann")

    // 3 groups with 2 values
    inputMolecule.apply((37 or 28) and (5L or 4L) and (1.0 or 2.0)).get.sorted === List("Ann", "Ben", "Joe")
  }


  "expressions" in new CoreSetup {

    Ns.str.int.long.double insert List(
      ("a", 1, 1L, 1.0),
      ("b", 1, 1L, 1.0),
      ("c", 2, 1L, 1.0),
      ("d", 3, 2L, 1.0),
      ("e", 4, 3L, 2.0)
    )

    val inputExpression = m(Ns.str.int_.not(?).long_.>(?).double_.<=(?))

    // Individual args matching input attributes
    inputExpression.apply(1, 0L, 1.0).get === List("c", "d")
    inputExpression.apply(1, 1L, 2.0).get === List("d", "e")

    // Triple matching input attributes
    inputExpression.apply((1, 0L, 1.0)).get === List("c", "d")
    inputExpression.apply((1, 1L, 2.0)).get === List("d", "e")

    // Seq of values matching input attributes
    inputExpression.apply(Seq((1, 0L, 1.0))).get === List("c", "d")
    inputExpression.apply(Seq((1, 1L, 2.0))).get === List("d", "e")


    // <expression1> and <expression2> and <expression3>
    inputExpression.apply(1 and 0L and 1.0).get === List("c", "d")
    inputExpression.apply(1 and 1L and 2.0).get === List("d", "e")

    // Two sequences, each matching corresponding input attribute
    inputExpression.apply(Seq(1), Seq(0L), Seq(1.0)).get === List("c", "d")
    inputExpression.apply(Seq(1), Seq(1L), Seq(2.0)).get === List("d", "e")
  }
}