package molecule.core.factory

import molecule.core.api.Molecule_1._
import molecule.core.composition.Composite_1._
import molecule.core.macros.MakeComposite_In
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Factory methods to create composite input molecules of arity 1-22 awaiting 1 input.
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
  * Composite input molecules awaiting 1 input have one attribute with `?` applied to mark that
  * it awaits an input at runtime for that attribute. Once the input molecule has been resolved
  * with input, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @groupname composite1 Factory methods to create composite input molecules awaiting 1 input.
  * @groupprio composite1 61
  */
trait CompositeFactory_1_2 {

  /** Macro creation of composite input molecule awaiting 1 input from user-defined DSL with 1 output group (arity 1).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of the composite
    * molecule to become a "composite input molecule" that awaits input at runtime for the
    * attribute marked with `?`.
    * <br><br>
    * Once the composite input molecule models the desired data structure and has been resolved with input
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `score` attribute to create composite input molecule
    *   val personsWithScore = m(Person.name + Tag.score_(?))
    *
    *   // At runtime, a `score` value is applied to get the Person's name
    *   personsWithScore(7).get.head === "Ben"
    * }}}
    * Composite input molecules of arity 1 has only one sub-molecule with output attribute(s).
    * If the sub-molecule has multiple output attributes, a tuple is returned, otherwise
    * just the single value:
    * {{{
    *   Composite input molecule         Composite type (1 output group)
    *
    *   A.a1       + B.b1_(?)      =>    a1
    *   A.a1.a2    + B.b1_(?)      =>    (a1, a2)
    *   A.a1.a2.a3 + B.b1_(?)      =>    (a1, a2, a3)
    *
    *   A.a1_(?) + B.b1            =>    b1
    *   A.a1_(?) + B.b1.b2         =>    (b1, b2)
    *   A.a1_(?) + B.b1.b2.b3      =>    (b1, b2, b3)
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_(?).a2_ + B.b1_ + C.c1.c2_.c3     =>    (c1, c3)
    * }}}
    * So, given two output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age + Tag.score_(?))(7).get.head === ("Ben", 42)
    *   //  A   . a1 . a2 +  B .   b1_(?)               => (  a1 , a2)
    * }}}
    * @group composite1
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 1 input
    * @tparam I1 Type of input attribute 1 (`score`: Int)
    * @tparam T1 Type of output group
    * @return Composite input molecule awaiting 1 input
    */
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]


  /** Macro creation of composite input molecule awaiting 1 input from user-defined DSL with 2 output groups (arity 2).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Applying the `?` marker to an attribute changes the semantics of the composite
    * molecule to become a "composite input molecule" that awaits input at runtime for the
    * attribute marked with `?`.
    * <br><br>
    * Once the composite input molecule models the desired data structure and has been resolved with input
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Apply `?` to `score` attribute to create composite input molecule
    *   val personsWithScore = m(Person.name + Tag.score(?))
    *
    *   // At runtime, a `score` value is applied to get the Person's name
    *   personsWithScore(7).get.head === ("Ben", 7)
    * }}}
    * Composite input molecules of arity 2 has two sub-molecules with output attribute(s). If a sub-molecule
    * has multiple output attributes, a tuple is returned, otherwise just the single value. The two
    * groups of either a single type or tuple are then tied together in an outer composite tuple:
    * {{{
    *   Composite input molecule          Composite type (2 output groups)
    *
    *   A.a1    + B.b1(?)           =>    (a1, b1)
    *   A.a1    + B.b1(?).b2        =>    (a1, (b1, b2))
    *   A.a1.a2 + B.b1(?)           =>    ((a1, a2), b1)
    *   A.a1.a2 + B.b1(?).b2        =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2 + B.b1.b2 + C.c1_(?)     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name     + Tag.score(?)      )(7).get.head === ("Ben", 7)
    *   m(Person.name     + Tag.score(?).flags)(7).get.head === ("Ben", (7, 3))
    *   m(Person.name.age + Tag.score(?)      )(7).get.head === (("Ben", 42), 7)
    *   m(Person.name.age + Tag.score(?).flags)(7).get.head === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age +
    *     Tag.score.flags +
    *     Cat.name_(?))("pitcher").get.head === (("Ben", 42), (7, 3))
    * }}}
    * @group composite1
    * @param dsl User-defined DSL structure modelling the composite input molecule awaiting 1 input
    * @tparam I1 Type of input attribute 1 (`score`: Int)
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite input molecule awaiting 1 input
    */
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
}

trait CompositeFactory_1_1 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
}

trait CompositeFactory_1_3 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
}

trait CompositeFactory_1_4 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
}

trait CompositeFactory_1_5 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
}

trait CompositeFactory_1_6 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
}

trait CompositeFactory_1_7 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
}

trait CompositeFactory_1_8 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
}

trait CompositeFactory_1_9 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

trait CompositeFactory_1_10 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

trait CompositeFactory_1_11 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

trait CompositeFactory_1_12 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

trait CompositeFactory_1_13 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

trait CompositeFactory_1_14 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

trait CompositeFactory_1_15 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

trait CompositeFactory_1_16 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

trait CompositeFactory_1_17 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

trait CompositeFactory_1_18 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_1_18[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

trait CompositeFactory_1_19 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_1_18[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_1_19[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

trait CompositeFactory_1_20 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_1_18[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_1_19[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_1_20[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

trait CompositeFactory_1_21 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_1_18[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_1_19[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_1_20[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_1_21[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule_1_21[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_1_21[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

trait CompositeFactory_1_22 {
  def m[obj[_], props, I1, T1](dsl: Composite_1_01[obj, props, I1, T1]): Molecule_1_01[props, I1, T1] = macro MakeComposite_In.await_1_1[props, I1, T1]
  def m[obj[_], props, I1, T1, T2](dsl: Composite_1_02[obj, props, I1, T1, T2]): Molecule_1_02[props, I1, T1, T2] = macro MakeComposite_In.await_1_2[props, I1, T1, T2]
  def m[obj[_], props, I1, T1, T2, T3](dsl: Composite_1_03[obj, props, I1, T1, T2, T3]): Molecule_1_03[props, I1, T1, T2, T3] = macro MakeComposite_In.await_1_3[props, I1, T1, T2, T3]
  def m[obj[_], props, I1, T1, T2, T3, T4](dsl: Composite_1_04[obj, props, I1, T1, T2, T3, T4]): Molecule_1_04[props, I1, T1, T2, T3, T4] = macro MakeComposite_In.await_1_4[props, I1, T1, T2, T3, T4]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5](dsl: Composite_1_05[obj, props, I1, T1, T2, T3, T4, T5]): Molecule_1_05[props, I1, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_1_5[props, I1, T1, T2, T3, T4, T5]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6](dsl: Composite_1_06[obj, props, I1, T1, T2, T3, T4, T5, T6]): Molecule_1_06[props, I1, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_1_6[props, I1, T1, T2, T3, T4, T5, T6]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_1_07[obj, props, I1, T1, T2, T3, T4, T5, T6, T7]): Molecule_1_07[props, I1, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_1_7[props, I1, T1, T2, T3, T4, T5, T6, T7]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_1_08[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule_1_08[props, I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_1_8[props, I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_1_09[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule_1_09[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_1_9[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_1_10[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_1_10[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_1_11[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_1_11[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_1_12[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_1_12[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_1_13[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_1_13[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_1_14[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_1_14[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_1_15[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_1_15[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_1_16[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_1_16[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_1_17[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_1_17[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_1_18[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_1_18[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_1_19[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_1_19[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_1_20[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_1_20[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_1_21[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule_1_21[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_1_21[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  def m[obj[_], props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite_1_22[obj, props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Molecule_1_22[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite_In.await_1_22[props, I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
