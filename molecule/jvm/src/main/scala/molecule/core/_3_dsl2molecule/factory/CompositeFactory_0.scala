package molecule.core._3_dsl2molecule.factory

import molecule.core._4_api.api.Molecule._
import molecule.core._2_dsl.composition.Composite_0._
import molecule.core._3_dsl2molecule.macros.MakeComposite
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Implicit composite molecule factory methods of arity 1-22.
  * <br><br>
  * Composite molecules model entities with attributes from different namespaces that are
  * not necessarily related. Each group of attributes is modelled by a molecule and these
  * "sub-molecules" are tied together with `+` methods to form a composite molecule.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  * @groupname composite Implicit factory methods to create composite molecules.
  * @groupprio composite 60
  */
trait CompositeFactory_0_2 {

  /** Macro creation of composite molecule from user-defined DSL structure with 1 output group.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Once the composite molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Explicitly calling `m` to create composite molecule
    *   // with 1 output attribute (`name`) and 1 tacit attribute (`score`).
    *   m(Person.name + Tag.score_).get.head === "Ben"
    *
    *   // Alternatively we can create the composite molecule implicitly
    *   Person.name.+(Tag.score_).get.head === "Ben"
    * }}}
    * Composite molecules of arity 1 has only one sub-molecule with output attribute(s).
    * If the sub-molecule has multiple output attributes, a tuple is returned, otherwise
    * just the single value:
    * {{{
    *   Composite molecule           Composite type (1 output group)
    *
    *   A.a1       + B.b1_     =>    a1
    *   A.a1.a2    + B.b1_     =>    (a1, a2)
    *   A.a1.a2.a3 + B.b1_     =>    (a1, a2, a3) etc...
    *
    *   A.a1_ + B.b1           =>    b1
    *   A.a1_ + B.b1.b2        =>    (b1, b2)
    *   A.a1_ + B.b1.b2.b3)    =>    (b1, b2, b3) etc...
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_.a2_ + B.b1_ + C.c1.c2_.c3     =>    (c1, c3) etc...
    * }}}
    * So, given two output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age + Tag.score_).get.head === ("Ben", 42)
    *   //  A   . a1 . a2 +  B .  b1              => (  a1 , a2)
    * }}}
    * @group composite
    * @param dsl User-defined DSL structure modelling the composite molecule
    * @tparam T1 Type of output group 1
    * @return Composite molecule
    */
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]


  /** Macro creation of composite molecule from user-defined DSL structure with 2 output groups.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `+` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Once the composite molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Explicitly calling `m` to create composite molecule with 2 output attributes
    *   m(Person.name + Tag.score).get.head === ("Ben", 7)
    *
    *   // Alternatively we can create the composite molecule implicitly
    *   Person.name.+(Tag.score).get.head === ("Ben", 7)
    * }}}
    * Composite molecules of arity 2 has two sub-molecules with output attribute(s). If a sub-molecule
    * has multiple output attributes, a tuple is returned, otherwise just the single value. The two
    * groups of either a single type or tuple are then tied together in an outer composite tuple:
    * {{{
    *   Composite molecule          Composite type (2 output groups)
    *
    *   A.a1    + B.b1        =>    (a1, b1)
    *   A.a1    + B.b1.b2     =>    (a1, (b1, b2))
    *   A.a1.a2 + B.b1        =>    ((a1, a2), b1)
    *   A.a1.a2 + B.b1.b2     =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2 + B.b1.b2 + C.c1_     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name + Tag.score.flags).get.head                         === ("Ben", (7, 3))
    *   m(Person.name.age + Tag.score).get.head                           === (("Ben", 42), 7)
    *   m(Person.name.age + Tag.score.flags).get.head                     === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age +
    *     Tag.score.flags +
    *     Cat.name_("pitcher")).get.head === (("Ben", 42), (7, 3))
    * }}}
    * @group composite
    * @param dsl User-defined DSL structure modelling the composite molecule
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite molecule
    */
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
}

trait CompositeFactory_0_1 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
}

trait CompositeFactory_0_3 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
}

trait CompositeFactory_0_4 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
}

trait CompositeFactory_0_5 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
}

trait CompositeFactory_0_6 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
}

trait CompositeFactory_0_7 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
}

trait CompositeFactory_0_8 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
}

trait CompositeFactory_0_9 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

trait CompositeFactory_0_10 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

trait CompositeFactory_0_11 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

trait CompositeFactory_0_12 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

trait CompositeFactory_0_13 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

trait CompositeFactory_0_14 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

trait CompositeFactory_0_15 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

trait CompositeFactory_0_16 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

trait CompositeFactory_0_17 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

trait CompositeFactory_0_18 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_0_18[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

trait CompositeFactory_0_19 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_0_18[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_0_19[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

trait CompositeFactory_0_20 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_0_18[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_0_19[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_0_20[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

trait CompositeFactory_0_21 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_0_18[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_0_19[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_0_20[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_0_21[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite.from21tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

trait CompositeFactory_0_22 {
  implicit final def m[obj[_], props, T1](dsl: Composite_0_01[obj, props, T1]): Molecule01[props, T1] = macro MakeComposite.from1tuple[props, T1]
  implicit final def m[obj[_], props, T1, T2](dsl: Composite_0_02[obj, props, T1, T2]): Molecule02[props, T1, T2] = macro MakeComposite.from2tuples[props, T1, T2]
  implicit final def m[obj[_], props, T1, T2, T3](dsl: Composite_0_03[obj, props, T1, T2, T3]): Molecule03[props, T1, T2, T3] = macro MakeComposite.from3tuples[props, T1, T2, T3]
  implicit final def m[obj[_], props, T1, T2, T3, T4](dsl: Composite_0_04[obj, props, T1, T2, T3, T4]): Molecule04[props, T1, T2, T3, T4] = macro MakeComposite.from4tuples[props, T1, T2, T3, T4]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5](dsl: Composite_0_05[obj, props, T1, T2, T3, T4, T5]): Molecule05[props, T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[props, T1, T2, T3, T4, T5]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6](dsl: Composite_0_06[obj, props, T1, T2, T3, T4, T5, T6]): Molecule06[props, T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[props, T1, T2, T3, T4, T5, T6]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7](dsl: Composite_0_07[obj, props, T1, T2, T3, T4, T5, T6, T7]): Molecule07[props, T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[props, T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite_0_08[obj, props, T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[props, T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[props, T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite_0_09[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[props, T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite_0_10[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite_0_11[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite_0_12[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite_0_13[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite_0_14[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite_0_15[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite_0_16[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite_0_17[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite_0_18[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite_0_19[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite_0_20[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite_0_21[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite.from21tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  implicit final def m[obj[_], props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite_0_22[obj, props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Molecule22[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite.from22tuples[props, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
