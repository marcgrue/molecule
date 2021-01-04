package molecule.core.factory

import molecule.core.api.Molecule._
import molecule.core.composition.Composite._
import molecule.core.macros.MakeComposite
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
trait Composite_Factory2 {

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
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]


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
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
}
object Composite_Factory2 extends Composite_Factory2

object Composite_Factory1 extends Composite_Factory1
trait Composite_Factory1 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
}

object Composite_Factory3 extends Composite_Factory3
trait Composite_Factory3 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
}

object Composite_Factory4 extends Composite_Factory4
trait Composite_Factory4 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
}

object Composite_Factory5 extends Composite_Factory5
trait Composite_Factory5 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
}

object Composite_Factory6 extends Composite_Factory6
trait Composite_Factory6 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
}

object Composite_Factory7 extends Composite_Factory7
trait Composite_Factory7 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
}

object Composite_Factory8 extends Composite_Factory8
trait Composite_Factory8 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
}

object Composite_Factory9 extends Composite_Factory9
trait Composite_Factory9 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
}

object Composite_Factory10 extends Composite_Factory10
trait Composite_Factory10 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
}

object Composite_Factory11 extends Composite_Factory11
trait Composite_Factory11 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
}

object Composite_Factory12 extends Composite_Factory12
trait Composite_Factory12 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
}

object Composite_Factory13 extends Composite_Factory13
trait Composite_Factory13 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
}

object Composite_Factory14 extends Composite_Factory14
trait Composite_Factory14 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
}

object Composite_Factory15 extends Composite_Factory15
trait Composite_Factory15 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
}

object Composite_Factory16 extends Composite_Factory16
trait Composite_Factory16 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
}

object Composite_Factory17 extends Composite_Factory17
trait Composite_Factory17 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
}

object Composite_Factory18 extends Composite_Factory18
trait Composite_Factory18 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
}

object Composite_Factory19 extends Composite_Factory19
trait Composite_Factory19 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
}

object Composite_Factory20 extends Composite_Factory20
trait Composite_Factory20 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
}

object Composite_Factory21 extends Composite_Factory21
trait Composite_Factory21 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite.from21tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
}

object Composite_Factory22 extends Composite_Factory22
trait Composite_Factory22 {
  implicit final def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeComposite.from1tuple[T1]
  implicit final def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeComposite.from2tuples[T1, T2]
  implicit final def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeComposite.from3tuples[T1, T2, T3]
  implicit final def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeComposite.from4tuples[T1, T2, T3, T4]
  implicit final def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeComposite.from5tuples[T1, T2, T3, T4, T5]
  implicit final def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeComposite.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeComposite.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeComposite.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeComposite.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeComposite.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeComposite.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeComposite.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeComposite.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeComposite.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeComposite.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeComposite.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeComposite.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeComposite.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeComposite.from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeComposite.from20tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeComposite.from21tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  implicit final def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Molecule22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeComposite.from22tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
