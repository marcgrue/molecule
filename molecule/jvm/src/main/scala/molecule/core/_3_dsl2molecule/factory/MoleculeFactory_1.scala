package molecule.core._3_dsl2molecule.factory

import molecule.core._2_dsl.boilerplate.base._
import molecule.core._3_dsl2molecule.input.InputMolecule_1._
import molecule.core._3_dsl2molecule.macros.MakeMolecule_In
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
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @return Input molecule ready to be resolved
    */
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]


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
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @tparam B Type of output attribute 2 (`score`: Int)
    * @return Input molecule ready to be resolved
    */
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
}

trait Molecule_In_1_Factory1 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
}

trait Molecule_In_1_Factory3 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
}

trait Molecule_In_1_Factory4 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
}

trait Molecule_In_1_Factory5 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
}

trait Molecule_In_1_Factory6 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
}

trait Molecule_In_1_Factory7 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
}

trait Molecule_In_1_Factory8 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
}

trait Molecule_In_1_Factory9 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
}

trait Molecule_In_1_Factory10 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
}

trait Molecule_In_1_Factory11 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
}

trait Molecule_In_1_Factory12 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
}

trait Molecule_In_1_Factory13 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
}

trait Molecule_In_1_Factory14 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
}

trait Molecule_In_1_Factory15 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
}

trait Molecule_In_1_Factory16 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
}

trait Molecule_In_1_Factory17 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
}

trait Molecule_In_1_Factory18 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
}

trait Molecule_In_1_Factory19 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
}

trait Molecule_In_1_Factory20 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_1_20[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
}

trait Molecule_In_1_Factory21 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_1_20[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: NS_1_21[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_1_21[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
}

trait Molecule_In_1_Factory22 {
  def m[obj[_], props, I1, A](dsl: NS_1_01[obj, props, I1, A]): InputMolecule_1_01[props, I1, A] = macro MakeMolecule_In.await_1_1[props, I1, A]
  def m[obj[_], props, I1, A, B](dsl: NS_1_02[obj, props, I1, A, B]): InputMolecule_1_02[props, I1, A, B] = macro MakeMolecule_In.await_1_2[props, I1, A, B]
  def m[obj[_], props, I1, A, B, C](dsl: NS_1_03[obj, props, I1, A, B, C]): InputMolecule_1_03[props, I1, A, B, C] = macro MakeMolecule_In.await_1_3[props, I1, A, B, C]
  def m[obj[_], props, I1, A, B, C, D](dsl: NS_1_04[obj, props, I1, A, B, C, D]): InputMolecule_1_04[props, I1, A, B, C, D] = macro MakeMolecule_In.await_1_4[props, I1, A, B, C, D]
  def m[obj[_], props, I1, A, B, C, D, E](dsl: NS_1_05[obj, props, I1, A, B, C, D, E]): InputMolecule_1_05[props, I1, A, B, C, D, E] = macro MakeMolecule_In.await_1_5[props, I1, A, B, C, D, E]
  def m[obj[_], props, I1, A, B, C, D, E, F](dsl: NS_1_06[obj, props, I1, A, B, C, D, E, F]): InputMolecule_1_06[props, I1, A, B, C, D, E, F] = macro MakeMolecule_In.await_1_6[props, I1, A, B, C, D, E, F]
  def m[obj[_], props, I1, A, B, C, D, E, F, G](dsl: NS_1_07[obj, props, I1, A, B, C, D, E, F, G]): InputMolecule_1_07[props, I1, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_1_7[props, I1, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H](dsl: NS_1_08[obj, props, I1, A, B, C, D, E, F, G, H]): InputMolecule_1_08[props, I1, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_1_8[props, I1, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I](dsl: NS_1_09[obj, props, I1, A, B, C, D, E, F, G, H, I]): InputMolecule_1_09[props, I1, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_1_9[props, I1, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J](dsl: NS_1_10[obj, props, I1, A, B, C, D, E, F, G, H, I, J]): InputMolecule_1_10[props, I1, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_1_10[props, I1, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_1_11[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K]): InputMolecule_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_1_11[props, I1, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_1_12[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L]): InputMolecule_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_1_12[props, I1, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_1_13[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]): InputMolecule_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_1_13[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_1_14[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): InputMolecule_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_1_14[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_1_15[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): InputMolecule_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_1_15[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_1_16[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): InputMolecule_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_1_16[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_1_17[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): InputMolecule_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_1_17[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_1_18[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): InputMolecule_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_1_18[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_1_19[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): InputMolecule_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_1_19[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_1_20[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): InputMolecule_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_1_20[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: NS_1_21[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): InputMolecule_1_21[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_1_21[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def m[obj[_], props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: NS_1_22[obj, props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): InputMolecule_1_22[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeMolecule_In.await_1_22[props, I1, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
