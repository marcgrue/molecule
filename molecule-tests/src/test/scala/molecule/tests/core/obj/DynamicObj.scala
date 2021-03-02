package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.core.util.testing.expectCompileError
import molecule.datomic.api.in1_out5._
import molecule.datomic.base.facade.Conn
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


/**
  * Testing dynamic molecule objects.
  *
  * Dynamically applying a body of method(s) to a molecule allows it to
  * resemble a case class definition body as though you instantiated a domain class
  * with already populated data according to the molecule structure.
  *
  * Properties are available inside the bode through the `self` reference to the
  * object (you can give it other names too).
  *
  * Note that the molecule needs to be created explicitly with `m`
  *
  */
class DynamicObj extends TestSpec with Helpers {

  val baz = 2

  "Basics" in new CoreSetup {

    val foo = 5

    Ns.int(42).Ref1.int1(43).save

    // Molecule needs to be created explicitly with `m`
    val obj = m(Ns.int.Ref1.int1) { self =>
      // External references
      val bar = foo + baz

      // val
      val v1 = bar + bar

      // def
      def a1: Int = v1 + 1
      def a2(): Int = a1 + 1
      def a3(i: Int): Int = a2() + i
      def a4[A](): Int = a3(2)
      def a5[A](v: A): String = v.toString
      def a6(i: Int): Int = i + a1
      def a7(i: Int, s: String): String = s + (i + self.int)
      def a8(ii: List[Int]): Int = ii.length
      def a9(ii: List[Conn]): Int = ii.length
    }

    // Dynamic calls
    obj.v1 === 14
    obj.a1 === 15
    obj.a2() === 16
    obj.a3(1) === 17
    obj.a4() === 18
    obj.a5(1) === "1"
    obj.a5(true) === "true"
    obj.a6(1) === 16
    obj.a7(1, "hi") === "hi43"
    obj.a8(List(1, 2, 3)) === 3
    obj.a9(List(conn)) === 1

    // Object properties
    obj.int === 42
    obj.Ref1.int1 === 43
  }


  "Local logic" in new SelfJoinSetup {
    import molecule.tests.core.ref.dsl.SelfJoin._
    Person.name("Ben").age(23).save

    val person = m(Person.name.age) { person =>
      def nextAge = person.age + 1
    }

    person.name === "Ben"
    person.age === 23
    person.nextAge === 24

  }

  "Mandatory implementation" in new CoreSetup {
    expectCompileError(
      "m(Ns.int) {self => }",
      "molecule.core.exceptions.package$MoleculeException: " +
        "Body of dynamic molecule can't be empty. Please define some val or def"
    )
  }

  "Not for input molecules" in new CoreSetup {
    val inputMolecule = m(Ns.str.int(?))
    expectCompileError(
      """inputMolecule(42).apply { self =>
        def x = 7
      }""",
      "molecule.core.exceptions.package$MoleculeException: " +
        "Can't use input molecules as dynamic molecules"
    )
  }
}
