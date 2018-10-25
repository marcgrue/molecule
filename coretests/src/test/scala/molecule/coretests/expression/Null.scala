package molecule.coretests.expression
import molecule.api.in1_out4._
import molecule.coretests.util.dsl.coreTest.Ns
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class Null extends CoreSpec {


  "Card one" in new CoreSetup {

    Ns.str.int$ insert List(
      ("a", Some(1)),
      ("b", Some(2)),
      ("c", Some(3)),
      ("d", None)
    )

    // Apply empty value to match entities with non-asserted attributes (null)
    Ns.str.int_().get === List("d")

    // Same as applying empty Iterables
    Ns.str.int_(Nil).get === List("d")
    Ns.str.int_(List()).get === List("d")

    // Applying empty value to mandatory attribute is contradictive and matches no entities.
    Ns.str.int().get === Nil

    // Applying possibly empty list as variable simply yields empty result set
    val emptyList = Nil
    Ns.int(emptyList).get === Nil


    // Can't apply empty Iterable constructor or any Scala expression
    expectCompileError(
      "m(Ns.str.int_(Seq.empty[Int]))",
      "molecule.transform.exception.Dsl2ModelException: Can't lift unexpected Any type: class scala.reflect.internal.Trees$TypeApply\n" +
        "Maybe you are applying some Scala expression to a molecule attribute?\n" +
        "Try to assign the expression to a variable and apply the variable instead.")

    // Apply Nil to tacit attribute of input molecule
    m(Ns.str.int_(?)).apply(Nil).get === List("d")

    // Apply Nil to mandatory attribute of input molecule never matches any entities
    m(Ns.str.int(?)).apply(Nil).get === Nil
  }


  "Card many" in new CoreSetup {

    Ns.int.ints$ insert List(
      (10, Some(Set(1, 2))),
      (20, Some(Set(2, 3))),
      (30, Some(Set(3, 4))),
      (40, None)
    )

    // Apply empty value to match entities with non-asserted attributes (null)
    Ns.int.ints_().get === List(40)

    // Same as applying empty Iterables
    Ns.int.ints_(Nil).get === List(40)
    Ns.int.ints_(List()).get === List(40)

    // Applying empty value to mandatory attribute is contradictive and never matches entities.
    Ns.int.ints().get === Nil

    // Applying possibly empty list as variable simply yields empty result set
    val emptyList = Nil
    Ns.ints(emptyList).get === Nil


    // Can't apply empty Iterable constructor or any Scala expression
    expectCompileError(
      "m(Ns.int.ints_(Seq.empty[Int]))",
      "molecule.transform.exception.Dsl2ModelException: Can't lift unexpected Any type: class scala.reflect.internal.Trees$TypeApply\n" +
        "Maybe you are applying some Scala expression to a molecule attribute?\n" +
        "Try to assign the expression to a variable and apply the variable instead.")

    // Apply Nil to tacit attribute of input molecule
    m(Ns.int.ints_(?)).apply(Nil).get === List(40)

    // Apply Nil to mandatory attribute of input molecule never matches any entities
    m(Ns.int.ints(?)).apply(Nil).get === Nil
  }
}
