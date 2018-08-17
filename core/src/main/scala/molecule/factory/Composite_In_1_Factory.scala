package molecule.factory

import molecule.composition.Composite_In_1._
import molecule.input.InputMolecule_1._
import molecule.macros.MakeComposite_In._
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
  * "sub-molecules" are tied together with `~` methods to form a composite molecule.
  *
  * == Composite input molecules ==
  * Composite input molecules awaiting 1 input have one attribute with `?` applied to mark that
  * it awaits an input at runtime for that attribute. Once the input molecule has been resolved
  * with input, a normal molecule is returned that we can query against the Datomic database.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  *
  * @see [[http://www.scalamolecule.org/manual/relationships/composites/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/ref/Composite.scala#L1 Tests]]
  * @groupname composite1 Factory methods to create composite input molecules awaiting 1 input.
  * @groupprio composite1 61
  */
trait Composite_In_1_Factory {

  /** Macro creation of composite input molecule awaiting 1 input from user-defined DSL with 1 output group (arity 1).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `~` method to form "composite molecules" that is basically just attributes
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
    *   val personsWithScore = m(Person.name ~ Tag.score_(?))
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
    *   A.a1       ~ B.b1_(?)      =>    a1
    *   A.a1.a2    ~ B.b1_(?)      =>    (a1, a2)
    *   A.a1.a2.a3 ~ B.b1_(?)      =>    (a1, a2, a3)
    *
    *   A.a1_(?) ~ B.b1            =>    b1
    *   A.a1_(?) ~ B.b1.b2         =>    (b1, b2)
    *   A.a1_(?) ~ B.b1.b2.b3      =>    (b1, b2, b3)
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_(?).a2_ ~ B.b1_ ~ C.c1.c2_.c3     =>    (c1, c3)
    * }}}
    * So, given two output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age ~ Tag.score_(?))(7).get.head === ("Ben", 42)
    *   //  A   . a1 . a2 ~  B .   b1_(?)               => (  a1 , a2)
    * }}}
    * @group composite1
    * @param inputDsl User-defined DSL structure modelling the composite input molecule awaiting 1 input
    * @tparam I1 Type of input attribute 1 (`score`: Int)
    * @tparam T1 Type of output group
    * @return Composite input molecule awaiting 1 input
    */
  def m[I1, T1](inputDsl: Composite_In_1_01[I1, T1]): InputMolecule_1_01[I1, T1] = macro await_1_1[I1, T1]


  /** Macro creation of composite input molecule awaiting 1 input from user-defined DSL with 2 output groups (arity 2).
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `~` method to form "composite molecules" that is basically just attributes
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
    *   val personsWithScore = m(Person.name ~ Tag.score(?))
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
    *   A.a1    ~ B.b1(?)           =>    (a1, b1)
    *   A.a1    ~ B.b1(?).b2        =>    (a1, (b1, b2))
    *   A.a1.a2 ~ B.b1(?)           =>    ((a1, a2), b1)
    *   A.a1.a2 ~ B.b1(?).b2        =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2 ~ B.b1.b2 ~ C.c1_(?)     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name     ~ Tag.score(?)      )(7).get.head === ("Ben", 7)
    *   m(Person.name     ~ Tag.score(?).flags)(7).get.head === ("Ben", (7, 3))
    *   m(Person.name.age ~ Tag.score(?)      )(7).get.head === (("Ben", 42), 7)
    *   m(Person.name.age ~ Tag.score(?).flags)(7).get.head === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age ~
    *     Tag.score.flags ~
    *     Cat.name_(?))("pitcher").get.head === (("Ben", 42), (7, 3))
    * }}}
    * @group composite1
    * @param inputDsl User-defined DSL structure modelling the composite input molecule awaiting 1 input
    * @tparam I1 Type of input attribute 1 (`score`: Int)
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite input molecule awaiting 1 input
    */
  def m[I1, T1, T2](inputDsl: Composite_In_1_02[I1, T1, T2]): InputMolecule_1_02[I1, T1, T2] = macro await_1_2[I1, T1, T2]


  def m[I1, T1, T2, T3](inputDsl: Composite_In_1_03[I1, T1, T2, T3]): InputMolecule_1_03[I1, T1, T2, T3] = macro await_1_3[I1, T1, T2, T3]
  def m[I1, T1, T2, T3, T4](inputDsl: Composite_In_1_04[I1, T1, T2, T3, T4]): InputMolecule_1_04[I1, T1, T2, T3, T4] = macro await_1_4[I1, T1, T2, T3, T4]
  def m[I1, T1, T2, T3, T4, T5](inputDsl: Composite_In_1_05[I1, T1, T2, T3, T4, T5]): InputMolecule_1_05[I1, T1, T2, T3, T4, T5] = macro await_1_5[I1, T1, T2, T3, T4, T5]
  def m[I1, T1, T2, T3, T4, T5, T6](inputDsl: Composite_In_1_06[I1, T1, T2, T3, T4, T5, T6]): InputMolecule_1_06[I1, T1, T2, T3, T4, T5, T6] = macro await_1_6[I1, T1, T2, T3, T4, T5, T6]
  def m[I1, T1, T2, T3, T4, T5, T6, T7](inputDsl: Composite_In_1_07[I1, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_1_07[I1, T1, T2, T3, T4, T5, T6, T7] = macro await_1_7[I1, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8](inputDsl: Composite_In_1_08[I1, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_1_08[I1, T1, T2, T3, T4, T5, T6, T7, T8] = macro await_1_8[I1, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9](inputDsl: Composite_In_1_09[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_1_09[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro await_1_9[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](inputDsl: Composite_In_1_10[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_1_10[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro await_1_10[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](inputDsl: Composite_In_1_11[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_1_11[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro await_1_11[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](inputDsl: Composite_In_1_12[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_1_12[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro await_1_12[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](inputDsl: Composite_In_1_13[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_1_13[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro await_1_13[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](inputDsl: Composite_In_1_14[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_1_14[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro await_1_14[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](inputDsl: Composite_In_1_15[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_1_15[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro await_1_15[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](inputDsl: Composite_In_1_16[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_1_16[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro await_1_16[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](inputDsl: Composite_In_1_17[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_1_17[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro await_1_17[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](inputDsl: Composite_In_1_18[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_1_18[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro await_1_18[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](inputDsl: Composite_In_1_19[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_1_19[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro await_1_19[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](inputDsl: Composite_In_1_20[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_1_20[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro await_1_20[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](inputDsl: Composite_In_1_21[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_1_21[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro await_1_21[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  def m[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](inputDsl: Composite_In_1_22[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): InputMolecule_1_22[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro await_1_22[I1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
