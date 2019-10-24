package molecule.factory

import molecule.composition.Composite_In_2._
import molecule.input.InputMolecule_2._
import molecule.macros.MakeComposite_In
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
  * @see [[http://www.scalamolecule.org/manual/relationships/composites/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/ref/Composite.scala#L1 Tests]]
  * @groupname composite2 Factory methods to create composite input molecule awaiting 2 inputs.
  * @groupprio composite2 62
  */
trait Composite_In_2_Factory2 {

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
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]


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
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
}
object Composite_In_2_Factory2 extends Composite_In_2_Factory2

object Composite_In_2_Factory1 extends Composite_In_2_Factory1
trait Composite_In_2_Factory1 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
}

object Composite_In_2_Factory3 extends Composite_In_2_Factory3
trait Composite_In_2_Factory3 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
}

object Composite_In_2_Factory4 extends Composite_In_2_Factory4
trait Composite_In_2_Factory4 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
}

object Composite_In_2_Factory5 extends Composite_In_2_Factory5
trait Composite_In_2_Factory5 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
}

object Composite_In_2_Factory6 extends Composite_In_2_Factory6
trait Composite_In_2_Factory6 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
}

object Composite_In_2_Factory7 extends Composite_In_2_Factory7
trait Composite_In_2_Factory7 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
}

object Composite_In_2_Factory8 extends Composite_In_2_Factory8
trait Composite_In_2_Factory8 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
}

object Composite_In_2_Factory9 extends Composite_In_2_Factory9
trait Composite_In_2_Factory9 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

object Composite_In_2_Factory10 extends Composite_In_2_Factory10
trait Composite_In_2_Factory10 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

object Composite_In_2_Factory11 extends Composite_In_2_Factory11
trait Composite_In_2_Factory11 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

object Composite_In_2_Factory12 extends Composite_In_2_Factory12
trait Composite_In_2_Factory12 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

object Composite_In_2_Factory13 extends Composite_In_2_Factory13
trait Composite_In_2_Factory13 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

object Composite_In_2_Factory14 extends Composite_In_2_Factory14
trait Composite_In_2_Factory14 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

object Composite_In_2_Factory15 extends Composite_In_2_Factory15
trait Composite_In_2_Factory15 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

object Composite_In_2_Factory16 extends Composite_In_2_Factory16
trait Composite_In_2_Factory16 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

object Composite_In_2_Factory17 extends Composite_In_2_Factory17
trait Composite_In_2_Factory17 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

object Composite_In_2_Factory18 extends Composite_In_2_Factory18
trait Composite_In_2_Factory18 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

object Composite_In_2_Factory19 extends Composite_In_2_Factory19
trait Composite_In_2_Factory19 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_In_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

object Composite_In_2_Factory20 extends Composite_In_2_Factory20
trait Composite_In_2_Factory20 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_In_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_In_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

object Composite_In_2_Factory21 extends Composite_In_2_Factory21
trait Composite_In_2_Factory21 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_In_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_In_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_In_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

object Composite_In_2_Factory22 extends Composite_In_2_Factory22
trait Composite_In_2_Factory22 {
  def m[I1, I2, T1](dsl: Composite_In_2_01[I1, I2, T1]): InputMolecule_2_01[I1, I2, T1] = macro MakeComposite_In.await_2_1[I1, I2, T1]
  def m[I1, I2, T1, T2](dsl: Composite_In_2_02[I1, I2, T1, T2]): InputMolecule_2_02[I1, I2, T1, T2] = macro MakeComposite_In.await_2_2[I1, I2, T1, T2]
  def m[I1, I2, T1, T2, T3](dsl: Composite_In_2_03[I1, I2, T1, T2, T3]): InputMolecule_2_03[I1, I2, T1, T2, T3] = macro MakeComposite_In.await_2_3[I1, I2, T1, T2, T3]
  def m[I1, I2, T1, T2, T3, T4](dsl: Composite_In_2_04[I1, I2, T1, T2, T3, T4]): InputMolecule_2_04[I1, I2, T1, T2, T3, T4] = macro MakeComposite_In.await_2_4[I1, I2, T1, T2, T3, T4]
  def m[I1, I2, T1, T2, T3, T4, T5](dsl: Composite_In_2_05[I1, I2, T1, T2, T3, T4, T5]): InputMolecule_2_05[I1, I2, T1, T2, T3, T4, T5] = macro MakeComposite_In.await_2_5[I1, I2, T1, T2, T3, T4, T5]
  def m[I1, I2, T1, T2, T3, T4, T5, T6](dsl: Composite_In_2_06[I1, I2, T1, T2, T3, T4, T5, T6]): InputMolecule_2_06[I1, I2, T1, T2, T3, T4, T5, T6] = macro MakeComposite_In.await_2_6[I1, I2, T1, T2, T3, T4, T5, T6]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_In_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7]): InputMolecule_2_07[I1, I2, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite_In.await_2_7[I1, I2, T1, T2, T3, T4, T5, T6, T7]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_In_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]): InputMolecule_2_08[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite_In.await_2_8[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_In_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]): InputMolecule_2_09[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite_In.await_2_9[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_In_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): InputMolecule_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite_In.await_2_10[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_In_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): InputMolecule_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite_In.await_2_11[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_In_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): InputMolecule_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite_In.await_2_12[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_In_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): InputMolecule_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite_In.await_2_13[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_In_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): InputMolecule_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite_In.await_2_14[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_In_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): InputMolecule_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite_In.await_2_15[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_In_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): InputMolecule_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite_In.await_2_16[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_In_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): InputMolecule_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite_In.await_2_17[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_In_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): InputMolecule_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite_In.await_2_18[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_In_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): InputMolecule_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite_In.await_2_19[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_In_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): InputMolecule_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite_In.await_2_20[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_In_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): InputMolecule_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite_In.await_2_21[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  def m[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite_In_2_22[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): InputMolecule_2_22[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite_In.await_2_22[I1, I2, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
