package molecule.core._3_dsl2molecule.factory

import molecule.core._2_dsl.composition.Composite_3._
import molecule.core._3_dsl2molecule.input.InputMolecule_3._
import molecule.core._3_dsl2molecule.macros.MakeComposite_In
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create composite input molecules of arity 1-22 awaiting 3 inputs.
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
  * Composite input molecules awaiting 3 inputs have 3 attributes with `?` applied to mark that
  * it awaits inputs at runtime for those attributes. Once the input molecule has been resolved
  * with inputs, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @groupname composite3 Factory methods to create composite input molecule awaiting 3 inputs.
  * @groupprio composite3 63
  */
trait CompositeFactory_3_2 {

  /** Macro creation of composite input molecule awaiting 3 inputs from user-defined DSL with 1 output group (arity 1).
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
    *   // Apply `?` to `age`, `score` and `flags` attributes to create composite input molecule
    *   val personsAgeScoreFlags = m(Person.name.age_(?) + Tag.score_(?).flags_(?))
    *
    *   // At runtime, `age`, `score` and `flags` values are applied to get the Person's name
    *   personsAgeScoreFlags(42, 7, 3).get.head === "Ben"
    * }}}
    * Composite input molecules of arity 1 has only one sub-molecule with output attribute(s).
    * If the sub-molecule has multiple output attributes, a tuple is returned, otherwise
    * just the single value:
    * {{{
    *   Composite input molecule                  Composite type (1 output group)
    *
    *   A.a1(?)       + B.b1_(?).b2_(?)     =>    a1
    *   A.a1.a2(?)    + B.b1_(?).b2_(?)     =>    (a1, a2)
    *   A.a1.a2.a3(?) + B.b1_(?).b2_(?)     =>    (a1, a2, a3)
    *
    *   A.a1_(?).a2_(?) + B.b1(?)           =>    b1
    *   A.a1_(?).a2_(?) + B.b1.b2(?)        =>    (b1, b2)
    *   A.a1_(?).a2_(?) + B.b1.b2.b3(?)     =>    (b1, b2, b3)
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_(?).a2_ + B.b1_(?) + C.c1.c2_(?).c3     =>    (c1, c3)
    * }}}
    * So, given 2 output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age(?) + Tag.score_(?).flags_(?))(42, 7, 3).get.head === ("Ben", 42)
    *   //  A   . a1 . a2(?) +  B .   b1_(?).   b2_(?)                      => (  a1 , a2)
    * }}}
    *
    * @group composite3
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 2 inputs
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam I3 Type of input attribute 3 (`flags`: Int)
    * @tparam T1 Type of output group
    * @return Composite input molecule awaiting 3 inputs
    */
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]


  /** Macro creation of composite input molecule awaiting 3 inputs from user-defined DSL with 2 output groups (arity 2).
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
    *   // Apply `?` to `age`, `score` and `flags` attributes to create composite input molecule
    *   val personsAgeScoreFlags = m(Person.name.age_(?) + Tag.score_(?).flags_(?))
    *
    *   // At runtime, `age`, `score` and `flags` values are applied to get the Person's name
    *   personsAgeScoreFlags(42, 7, 3).get.head === "Ben"
    * }}}
    * Composite molecules of arity 2 has two sub-molecules with output attribute(s). If a sub-molecule
    * has multiple output attributes, a tuple is returned, otherwise just the single value. The two
    * groups of either a single type or tuple are then tied together in an outer composite tuple:
    * {{{
    *   Composite input molecule               Composite type (2 output groups)
    *
    *   A.a1.a2_(?) + B.b1(?).b2_(?)     =>    (a1, b1)
    *   A.a1.a2_(?) + B.b1(?).b2(?)      =>    (a1, (b1, b2))
    *   A.a1.a2(?)  + B.b1(?).b2_(?)     =>    ((a1, a2), b1)
    *   A.a1.a2(?)  + B.b1(?).b2(?)      =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2(?) + B.b1(?).b2 + C.c1_(?)     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name.age_(?) + Tag.score(?).flags_(?))(42, 7, 3).get.head === ("Ben", 7)
    *   m(Person.name.age_(?) + Tag.score(?).flags(?) )(42, 7, 3).get.head === ("Ben", (7, 3))
    *   m(Person.name.age(?)  + Tag.score(?).flags_(?))(42, 7, 3).get.head === (("Ben", 42), 7)
    *   m(Person.name.age(?)  + Tag.score(?).flags(?) )(42, 7, 3).get.head === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age(?) +
    *     Tag.score(?).flags +
    *     Cat.name_(?))(42, 7, "pitcher").get.head === (("Ben", 42), (7, 3))
    * }}}
    *
    * @group composite3
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 3 inputs
    * @tparam I1 Type of input attribute 1 (`age`: Int)
    * @tparam I2 Type of input attribute 2 (`score`: Int)
    * @tparam I3 Type of input attribute 3 (`flags`: Int)
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite input molecule awaiting 3 inputs
    */
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
}

trait CompositeFactory_3_1 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
}

trait CompositeFactory_3_3 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
}

trait CompositeFactory_3_4 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
}

trait CompositeFactory_3_5 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
}

trait CompositeFactory_3_6 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
}

trait CompositeFactory_3_7 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
}

trait CompositeFactory_3_8 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
}

trait CompositeFactory_3_9 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

trait CompositeFactory_3_10 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

trait CompositeFactory_3_11 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

trait CompositeFactory_3_12 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

trait CompositeFactory_3_13 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

trait CompositeFactory_3_14 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

trait CompositeFactory_3_15 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

trait CompositeFactory_3_16 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

trait CompositeFactory_3_17 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

trait CompositeFactory_3_18 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_3_18[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

trait CompositeFactory_3_19 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_3_18[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_3_19[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

trait CompositeFactory_3_20 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_3_18[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_3_19[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_3_20[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

trait CompositeFactory_3_21 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_3_18[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_3_19[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_3_20[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_3_21[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_3_21[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_3_21[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

trait CompositeFactory_3_22 {
  def m[obj[_], props, I1, I2, I3, T1](dsl: Composite_3_01[obj, props, I1, I2, I3, T1]): InputMolecule_3_01[props, I1, I2, I3, T1] = macro MakeComposite_In.await_3_1[props, I1, I2, I3, T1]
  def m[obj[_], props, I1, I2, I3, T1, T2](dsl: Composite_3_02[obj, props, I1, I2, I3, T1, T2]): InputMolecule_3_02[props, I1, I2, I3, T1, T2] = macro MakeComposite_In.await_3_2[props, I1, I2, I3, T1, T2]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3](dsl: Composite_3_03[obj, props, I1, I2, I3, T1, T2, T3]): InputMolecule_3_03[props, I1, I2, I3, T1, T2, T3] = macro MakeComposite_In.await_3_3[props, I1, I2, I3, T1, T2, T3]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4](dsl: Composite_3_04[obj, props, I1, I2, I3, T1, T2, T3, T4]): InputMolecule_3_04[props, I1, I2, I3, T1, T2, T3, T4] = macro MakeComposite_In.await_3_4[props, I1, I2, I3, T1, T2, T3, T4]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5](dsl: Composite_3_05[obj, props, I1, I2, I3, T1, T2, T3, T4, T5]): InputMolecule_3_05[props, I1, I2, I3, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_3_5[props, I1, I2, I3, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6](dsl: Composite_3_06[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6]): InputMolecule_3_06[props, I1, I2, I3, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_3_6[props, I1, I2, I3, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_3_07[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_3_07[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_3_7[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_3_08[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_3_08[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_3_8[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_3_09[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_3_09[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_3_9[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_3_10[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_3_10[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_3_11[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_3_11[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_3_12[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_3_12[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_3_13[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_3_13[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_3_14[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_3_14[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_3_15[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_3_15[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_3_16[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_3_16[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_3_17[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_3_17[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_3_18[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_3_18[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_3_19[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_3_19[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_3_20[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_3_20[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_3_21[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_3_21[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_3_21[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  def m[obj[_], props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite_3_22[obj, props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): InputMolecule_3_22[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite_In.await_3_22[props, I1, I2, I3, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
