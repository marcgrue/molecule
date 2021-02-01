package molecule.core._3_dsl2molecule.factory

import molecule.core._2_dsl.composition.Composite_2._
import molecule.core._3_dsl2molecule.input.InputMolecule_2._
import molecule.core._3_dsl2molecule.macros.MakeComposite_In
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create composite input molecules of arity 1-22 awaiting 2 inputs.
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
  * == Composite molecules ==
  * Composite molecules model entities with attributes from different namespaces that are
  * not necessarily related. Each group of attributes is modelled by a molecule and these
  * "sub-molecules" are tied together with `+` methods to form a composite molecule.
  *
  * == Composite input molecules ==
  * Composite input molecules awaiting 2 inputs have 2 attributes with `?` applied to mark that
  * it awaits inputs at runtime for those attributes. Once the input molecule has been resolved
  * with inputs, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @groupname composite2 Factory methods to create composite input molecule awaiting 2 inputs.
  * @groupprio composite2 62
  */
trait CompositeFactory_2_2 {

  /** Macro creation of composite input molecule awaiting 2 inputs from user-defined DSL with 1 output group (arity 1).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Applying the `?` marker to attributes changes the semantics of the composite
    * molecule to become a "composite input molecule" that awaits input at runtime for the
    * attributes marked with `?`.
    * <br><br>
    * Once the composite input molecule models the desired data structure and has been resolved with input
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` and `score` attributes to create composite input molecule
    *   val personsAgeScore = m(Person.name.age_(?) + Tag.score_(?))
    *
    *   // At runtime, `age` and `score` values are applied to get the Person's name
    *   personsAgeScore(42, 7).get.head === "Ben"
    * }}}
    * Composite input molecules of arity 1 has only one sub-molecule with output attribute(s).
    * If the sub-molecule has multiple output attributes, a tuple is returned, otherwise
    * just the single value:
    * {{{
    *   Composite input molecule            Composite type (1 output group)
    *
    *   A.a1(?)       + B.b1_(?)      =>    a1
    *   A.a1.a2(?)    + B.b1_(?)      =>    (a1, a2)
    *   A.a1.a2.a3(?) + B.b1_(?)      =>    (a1, a2, a3)
    *
    *   A.a1_(?) + B.b1(?)            =>    b1
    *   A.a1_(?) + B.b1.b2(?)         =>    (b1, b2)
    *   A.a1_(?) + B.b1.b2.b3(?)      =>    (b1, b2, b3)
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_(?).a2_ + B.b1_(?) + C.c1.c2_.c3     =>    (c1, c3)
    * }}}
    * So, given 2 output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age(?) + Tag.score_(?))(42, 7).get.head === ("Ben", 42)
    *   //  A   . a1 . a2(?) +  B .   b1_(?)                   => (  a1 , a2)
    * }}}
    *
    * @group composite2
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 2 inputs
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam T1 Type of output group
    * @return Composite input molecule awaiting 2 inputs
    */
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]


  /** Macro creation of composite input molecule awaiting 2 inputs from user-defined DSL with 2 output groups (arity 2).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Applying the `?` marker to attributes changes the semantics of the composite
    * molecule to become a "composite input molecule" that awaits input at runtime for the
    * attributes marked with `?`.
    * <br><br>
    * Once the composite input molecule models the desired data structure and has been resolved with input
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `age` and `score` attributes to create composite input molecule
    *   val personsAgeScore = m(Person.name.age_(?) + Tag.score(?))
    *
    *   // At runtime, `age` and `score` values are applied to get the Person's name
    *   personsAgeScore(42, 7).get.head === ("Ben", 7)
    * }}}
    * Composite input molecules of arity 2 has two sub-molecules with output attribute(s). If a sub-molecule
    * has multiple output attributes, a tuple is returned, otherwise just the single value. The two
    * groups of either a single type or tuple are then tied together in an outer composite tuple:
    * {{{
    *   Composite input molecule          Composite type (2 output groups)
    *
    *   A.a1(?)    + B.b1(?)        =>    (a1, b1)
    *   A.a1(?)    + B.b1(?).b2     =>    (a1, (b1, b2))
    *   A.a1.a2(?) + B.b1(?)        =>    ((a1, a2), b1)
    *   A.a1.a2(?) + B.b1(?).b2     =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2 + B.b1(?).b2 + C.c1_(?)     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name(?)     + Tag.score(?)      )("Ben", 7).get.head === ("Ben", 7)
    *   m(Person.name(?)     + Tag.score(?).flags)("Ben", 7).get.head === ("Ben", (7, 3))
    *   m(Person.name.age(?) + Tag.score(?)      )(42, 7)   .get.head === (("Ben", 42), 7)
    *   m(Person.name.age(?) + Tag.score(?).flags)(42, 7)   .get.head === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age +
    *     Tag.score(?).flags +
    *     Cat.name_(?))(7, "pitcher").get.head === (("Ben", 42), (7, 3))
    * }}}
    *
    * @group composite2
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 2 inputs
    * @tparam I1 Type of input attribute 1 (`name`: String or `age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite input molecule awaiting 2 inputs
    */
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
}

trait CompositeFactory_2_1 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
}

trait CompositeFactory_2_3 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
}

trait CompositeFactory_2_4 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
}

trait CompositeFactory_2_5 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
}

trait CompositeFactory_2_6 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
}

trait CompositeFactory_2_7 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
}

trait CompositeFactory_2_8 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
}

trait CompositeFactory_2_9 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

trait CompositeFactory_2_10 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

trait CompositeFactory_2_11 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

trait CompositeFactory_2_12 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

trait CompositeFactory_2_13 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

trait CompositeFactory_2_14 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

trait CompositeFactory_2_15 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

trait CompositeFactory_2_16 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

trait CompositeFactory_2_17 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

trait CompositeFactory_2_18 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_2_18[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

trait CompositeFactory_2_19 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_2_18[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_2_19[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

trait CompositeFactory_2_20 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_2_18[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_2_19[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_2_20[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

trait CompositeFactory_2_21 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_2_18[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_2_19[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_2_20[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_2_21[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_2_21[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_2_21[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

trait CompositeFactory_2_22 {
  def m[obj[_], props, I1, I2, T1](dsl: Composite_2_01[obj, props, I1, I2, T1]): InputMolecule_2_01[props, I1, I2, T1] = macro MakeComposite_In.await_2_1[props, I1, I2, T1]
  def m[obj[_], props, I1, I2, T1, T2](dsl: Composite_2_02[obj, props, I1, I2, T1, T2]): InputMolecule_2_02[props, I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[props, I1, I2, T1, T2]
  def m[obj[_], props, I1, I2, T1, T2, T3](dsl: Composite_2_03[obj, props, I1, I2, T1, T2, T3]): InputMolecule_2_03[props, I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[props, I1, I2, T1, T2, T3]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4](dsl: Composite_2_04[obj, props, I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[props, I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[props, I1, I2, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5](dsl: Composite_2_05[obj, props, I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[props, I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[props, I1, I2, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_2_06[obj, props, I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[props, I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[props, I1, I2, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_2_07[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[props, I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[props, I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_2_08[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_2_09[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_2_10[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_2_11[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_2_12[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_2_13[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_2_14[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_2_15[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_2_16[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_2_17[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_2_18[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_2_19[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_2_20[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_2_21[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_2_21[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_2_21[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  def m[obj[_], props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite_2_22[obj, props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): InputMolecule_2_22[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite_In.await_2_22[props, I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
