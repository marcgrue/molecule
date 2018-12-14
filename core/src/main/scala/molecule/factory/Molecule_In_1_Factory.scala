package molecule.factory

import molecule.boilerplate.in1._
import molecule.boilerplate.base._
import molecule.input.InputMolecule_1._
import molecule.macros.MakeMolecule_In
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create input molecules of arity 1-22 awaiting 1 input.
  * == Molecules ==
  * Molecules are type-safe custom Scala models of data structures in a Datomic database.
  * <br><br>
  * Molecules are build with your custom meta-DSL that is auto-generated from your Schema Definition file.
  * Calling `m` on your modelled DSL structure lets Molecule macros create a custom molecule,
  * ready for retrieving or manipulating data in the Datomic database.
  * <br><br>
  * Each molecule consists of one or more attributes that can have values or expressions applied.
  * The arity of a molecule is determined by the number of attributes that will return data when the
  * molecule is queried against the Datomic database. Attributes returning data are called "output attributes".
  *
  * == Input molecules ==
  * Input molecules awaiting 1 input have one attribute with `?` applied to mark that
  * it awaits an input at runtime for that attribute. Once the input molecule has been resolved
  * with input, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @see [[http://www.scalamolecule.org/manual/attributes/parameterized/ Manual]]
  * @groupname input1 Factory methods to create input molecules awaiting 1 input.
  * @groupprio input1 51
  */
trait Molecule_In_1_Factory2 {

  /** Macro creation of input molecule awaiting 1 input from user-defined DSL structure with 1 output attribute (arity 1).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attribute marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` attribute to create input molecule
    *   val personOfAge = m(Person.name.age_(?))
    *
    *   // At runtime, an `age` value is applied to get the Person's name
    *   personOfAge(42).get.head === "Ben"
    * }}}
    * For arity-many molecules, data structures are returned as tuples. But for arity-1
    * molecules (like the example having only 1 output attribute, `name`) there's no need for
    * a tuple, so values type-safely matching the attribute are returned directly in the list.
    *
    * @group input1
    * @param dsl User-defined DSL structure modelling the input molecule
    * @tparam In1_1 Internal builder pattern type
    * @tparam In1_2 Internal builder pattern type
    * @tparam In2_1 Internal builder pattern type
    * @tparam In2_2 Internal builder pattern type
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @return Input molecule ready to be resolved
    */
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]


  /** Macro creation of input molecule awaiting 1 input from user-defined DSL structure with 2 output attributes (arity 2).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attribute marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` attribute to create input molecule
    *   val personOfAge = m(Person.name.age_(?).score)
    *
    *   // At runtime, an `age` value is applied to get the Person's name and score
    *   personOfAge(42).get.head === ("Ben", 7)
    * }}}
    * @group input1
    * @param dsl User-defined DSL structure modelling the input molecule
    * @tparam In1_2 Internal builder pattern type
    * @tparam In1_3 Internal builder pattern type
    * @tparam In2_2 Internal builder pattern type
    * @tparam In2_3 Internal builder pattern type
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @tparam B Type of output attribute 2 (`score`: Int)
    * @return Input molecule ready to be resolved
    */
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
}
object Molecule_In_1_Factory2 extends Molecule_In_1_Factory2

object Molecule_In_1_Factory1 extends Molecule_In_1_Factory1
trait Molecule_In_1_Factory1 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
}

object Molecule_In_1_Factory3 extends Molecule_In_1_Factory3
trait Molecule_In_1_Factory3 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
}

object Molecule_In_1_Factory4 extends Molecule_In_1_Factory4
trait Molecule_In_1_Factory4 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
}

object Molecule_In_1_Factory5 extends Molecule_In_1_Factory5
trait Molecule_In_1_Factory5 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
}

object Molecule_In_1_Factory6 extends Molecule_In_1_Factory6
trait Molecule_In_1_Factory6 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
}

object Molecule_In_1_Factory7 extends Molecule_In_1_Factory7
trait Molecule_In_1_Factory7 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
}

object Molecule_In_1_Factory8 extends Molecule_In_1_Factory8
trait Molecule_In_1_Factory8 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
}

object Molecule_In_1_Factory9 extends Molecule_In_1_Factory9
trait Molecule_In_1_Factory9 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
}

object Molecule_In_1_Factory10 extends Molecule_In_1_Factory10
trait Molecule_In_1_Factory10 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
}

object Molecule_In_1_Factory11 extends Molecule_In_1_Factory11
trait Molecule_In_1_Factory11 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
}

object Molecule_In_1_Factory12 extends Molecule_In_1_Factory12
trait Molecule_In_1_Factory12 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
}

object Molecule_In_1_Factory13 extends Molecule_In_1_Factory13
trait Molecule_In_1_Factory13 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
}

object Molecule_In_1_Factory14 extends Molecule_In_1_Factory14
trait Molecule_In_1_Factory14 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
}

object Molecule_In_1_Factory15 extends Molecule_In_1_Factory15
trait Molecule_In_1_Factory15 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
}

object Molecule_In_1_Factory16 extends Molecule_In_1_Factory16
trait Molecule_In_1_Factory16 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
}

object Molecule_In_1_Factory17 extends Molecule_In_1_Factory17
trait Molecule_In_1_Factory17 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
}

object Molecule_In_1_Factory18 extends Molecule_In_1_Factory18
trait Molecule_In_1_Factory18 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
}

object Molecule_In_1_Factory19 extends Molecule_In_1_Factory19
trait Molecule_In_1_Factory19 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
}

object Molecule_In_1_Factory20 extends Molecule_In_1_Factory20
trait Molecule_In_1_Factory20 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: IN1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
}

object Molecule_In_1_Factory21 extends Molecule_In_1_Factory21
trait Molecule_In_1_Factory21 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: IN1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: IN1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
}

object Molecule_In_1_Factory22 extends Molecule_In_1_Factory22
trait Molecule_In_1_Factory22 {
  def m[I1, A](dsl: IN1_01[I1, A]): InputMolecule_1_01[I1, A] = macro MakeMolecule_In.await_1_1[I1, A]
  def m[I1, A, B](dsl: IN1_02[I1, A, B]): InputMolecule_1_02[I1, A, B] = macro MakeMolecule_In.await_1_2[I1, A, B]
  def m[I1, A, B, C](dsl: IN1_03[I1, A, B, C]): InputMolecule_1_03[I1, A, B, C] = macro MakeMolecule_In.await_1_3[I1, A, B, C]
  def m[I1, A, B, C, D](dsl: IN1_04[I1, A, B, C, D]): InputMolecule_1_04[I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[I1, A, B, C, D]
  def m[I1, A, B, C, D, E](dsl: IN1_05[I1, A, B, C, D, E]): InputMolecule_1_05[I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[I1, A, B, C, D, E]
  def m[I1, A, B, C, D, E, F](dsl: IN1_06[I1, A, B, C, D, E, F]): InputMolecule_1_06[I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[I1, A, B, C, D, E, F]
  def m[I1, A, B, C, D, E, F, G](dsl: IN1_07[I1, A, B, C, D, E, F, G]): InputMolecule_1_07[I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[I1, A, B, C, D, E, F, G]
  def m[I1, A, B, C, D, E, F, G, H](dsl: IN1_08[I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[I1, A, B, C, D, E, F, G, H]
  def m[I1, A, B, C, D, E, F, G, H, I](dsl: IN1_09[I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[I1, A, B, C, D, E, F, G, H, I]
  def m[I1, A, B, C, D, E, F, G, H, I, J](dsl: IN1_10[I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[I1, A, B, C, D, E, F, G, H, I, J]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K](dsl: IN1_11[I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: IN1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: IN1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: IN1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: IN1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: IN1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: IN1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: IN1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: IN1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: IN1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: IN1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_1_21[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def m[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: IN1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeMolecule_In.await_1_22[I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
