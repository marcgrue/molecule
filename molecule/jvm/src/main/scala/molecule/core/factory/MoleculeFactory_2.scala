package molecule.core.factory

import molecule.core.api.Molecule_2._
import molecule.core.dsl.base._
import molecule.core.macros.MakeMolecule_In
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create input molecules of arity 1-22 awaiting 2 inputs.
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
  * Input molecules awaiting 2 inputs have one attribute with `?` applied to mark that
  * it awaits inputs at runtime for those attributes. Once the input molecule has been resolved
  * with inputs, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @groupname input2 Factory methods to create input molecules awaiting 2 inputs.
  * @groupprio input2 52
  */
trait Molecule_In_2_Factory2 {

  /** Macro creation of input molecule awaiting 2 inputs from user-defined DSL structure with 1 output attribute (arity 1).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to attributes changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attributes marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` and `score` attributes to create input molecule.
    *   val personAgeScore = m(Person.name.age_(?).score_(?))
    *
    *   // At runtime `age` and `score` values are applied to get the Person's name.
    *   personAgeScore(42, 7).get.head === "Ben"
    * }}}
    * For arity-many molecules, data structures are returned as tuples. But for arity-1
    * molecules (like the example having only 1 output attribute, `name`) there's no need for
    * a tuple, so values type-safely matching the attribute are returned directly in the list.
    *
    * @group input2
    * @param dsl User-defined DSL structure modelling the input molecule
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @return Input molecule ready to be resolved
    */
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]


  /** Macro creation of input molecule awaiting 2 inputs from user-defined DSL structure with 2 output attributes (arity 2).
    * <br><br>
    * Molecules are build by adding one or more attributes to an initial namespace
    * like `Person` from the example below.
    * <br><br>
    * Applying the `?` marker to attributes changes the semantics of a molecule to become
    * an "input molecule" that awaits input at runtime for the attributes marked with `?`.
    * <br><br>
    * Once the input molecule has been resolved with input, we can call various
    * actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` and `score` attributes to create input molecule.
    *   // Input attributes can be tacit or mandatory
    *   val personAgeScore = m(Person.name.age_(?).score(?))
    *
    *   // At runtime `age` and `score` values are applied to get the Person's name and score.
    *   // Since `score` was mandatory (without underscore), its value is also returned.
    *   personAgeScore(42, 7).get.head === ("Ben", 7)
    * }}}
    * @group input2
    * @param dsl User-defined DSL structure modelling the input molecule
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam A Type of output attribute 1 (`name`: String)
    * @tparam B Type of output attribute 2 (`score`: Int)
    * @return Input molecule ready to be resolved
    */
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
}

trait Molecule_In_2_Factory1 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
}

trait Molecule_In_2_Factory3 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
}

trait Molecule_In_2_Factory4 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
}

trait Molecule_In_2_Factory5 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
}

trait Molecule_In_2_Factory6 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
}

trait Molecule_In_2_Factory7 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
}

trait Molecule_In_2_Factory8 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
}

trait Molecule_In_2_Factory9 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
}

trait Molecule_In_2_Factory10 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
}

trait Molecule_In_2_Factory11 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
}

trait Molecule_In_2_Factory12 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
}

trait Molecule_In_2_Factory13 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
}

trait Molecule_In_2_Factory14 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
}

trait Molecule_In_2_Factory15 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
}

trait Molecule_In_2_Factory16 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
}

trait Molecule_In_2_Factory17 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
}

trait Molecule_In_2_Factory18 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
}

trait Molecule_In_2_Factory19 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
}

trait Molecule_In_2_Factory20 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
}

trait Molecule_In_2_Factory21 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: NS_2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): Molecule_2_21[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_2_21[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
}

trait Molecule_In_2_Factory22 {
  def m[obj[_], props, I1, I2, A](dsl: NS_2_01[obj, props, I1, I2, A]): Molecule_2_01[props, I1, I2, A] = macro MakeMolecule_In.await_2_1[props, I1, I2, A]
  def m[obj[_], props, I1, I2, A, B](dsl: NS_2_02[obj, props, I1, I2, A, B]): Molecule_2_02[props, I1, I2, A, B] = macro MakeMolecule_In.await_2_2[props, I1, I2, A, B]
  def m[obj[_], props, I1, I2, A, B, C](dsl: NS_2_03[obj, props, I1, I2, A, B, C]): Molecule_2_03[props, I1, I2, A, B, C] = macro MakeMolecule_In.await_2_3[props, I1, I2, A, B, C]
  def m[obj[_], props, I1, I2, A, B, C, D](dsl: NS_2_04[obj, props, I1, I2, A, B, C, D]): Molecule_2_04[props, I1, I2, A, B, C, D] = macro MakeMolecule_In.await_2_4[props, I1, I2, A, B, C, D]
  def m[obj[_], props, I1, I2, A, B, C, D, E](dsl: NS_2_05[obj, props, I1, I2, A, B, C, D, E]): Molecule_2_05[props, I1, I2, A, B, C, D, E] = macro MakeMolecule_In.await_2_5[props, I1, I2, A, B, C, D, E]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F](dsl: NS_2_06[obj, props, I1, I2, A, B, C, D, E, F]): Molecule_2_06[props, I1, I2, A, B, C, D, E, F] = macro MakeMolecule_In.await_2_6[props, I1, I2, A, B, C, D, E, F]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G](dsl: NS_2_07[obj, props, I1, I2, A, B, C, D, E, F, G]): Molecule_2_07[props, I1, I2, A, B, C, D, E, F, G] = macro MakeMolecule_In.await_2_7[props, I1, I2, A, B, C, D, E, F, G]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H](dsl: NS_2_08[obj, props, I1, I2, A, B, C, D, E, F, G, H]): Molecule_2_08[props, I1, I2, A, B, C, D, E, F, G, H] = macro MakeMolecule_In.await_2_8[props, I1, I2, A, B, C, D, E, F, G, H]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I](dsl: NS_2_09[obj, props, I1, I2, A, B, C, D, E, F, G, H, I]): Molecule_2_09[props, I1, I2, A, B, C, D, E, F, G, H, I] = macro MakeMolecule_In.await_2_9[props, I1, I2, A, B, C, D, E, F, G, H, I]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J](dsl: NS_2_10[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J]): Molecule_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J] = macro MakeMolecule_In.await_2_10[props, I1, I2, A, B, C, D, E, F, G, H, I, J]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K](dsl: NS_2_11[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]): Molecule_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K] = macro MakeMolecule_In.await_2_11[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L](dsl: NS_2_12[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]): Molecule_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L] = macro MakeMolecule_In.await_2_12[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M](dsl: NS_2_13[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]): Molecule_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M] = macro MakeMolecule_In.await_2_13[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N](dsl: NS_2_14[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]): Molecule_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N] = macro MakeMolecule_In.await_2_14[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O](dsl: NS_2_15[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]): Molecule_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O] = macro MakeMolecule_In.await_2_15[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P](dsl: NS_2_16[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]): Molecule_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P] = macro MakeMolecule_In.await_2_16[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q](dsl: NS_2_17[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]): Molecule_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q] = macro MakeMolecule_In.await_2_17[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R](dsl: NS_2_18[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]): Molecule_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R] = macro MakeMolecule_In.await_2_18[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S](dsl: NS_2_19[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]): Molecule_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S] = macro MakeMolecule_In.await_2_19[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T](dsl: NS_2_20[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]): Molecule_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T] = macro MakeMolecule_In.await_2_20[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U](dsl: NS_2_21[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]): Molecule_2_21[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U] = macro MakeMolecule_In.await_2_21[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]
  def m[obj[_], props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V](dsl: NS_2_22[obj, props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]): Molecule_2_22[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V] = macro MakeMolecule_In.await_2_22[props, I1, I2, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]
}
