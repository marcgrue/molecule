package molecule.factory

import molecule.action.Molecule._
import molecule.composition.Composite._
import molecule.macros.MakeComposite._
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}


/** Implicit composite molecule factory methods of arity 1-22.
  * <br><br>
  * Composite molecules model entities with attributes from different namespaces that are
  * not necessarily related. Each group of attributes is modelled by a molecule and these
  * "sub-molecules" are tied together with `~` methods to form a composite molecule.
  * <br><br>
  * For brevity, only arity 1 and 2 method signatures are shown. Arity 3-22 follow the same pattern.
  * @see [[http://www.scalamolecule.org/manual/relationships/composites/ Manual]]
  *     | [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/ref/Composite.scala#L1 Tests]]
  * @groupname composite Implicit factory methods to create composite molecules.
  * @groupprio composite 60
  */
trait Composite_Factory {

  /** Macro creation of composite molecule from user-defined DSL structure with 1 output group.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `~` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Once the composite molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Explicitly calling `m` to create composite molecule
    *   // with 1 output attribute (`name`) and 1 tacit attribute (`score`).
    *   m(Person.name ~ Tag.score_).get.head === "Ben"
    *
    *   // Alternatively we can create the composite molecule implicitly
    *   Person.name.~(Tag.score_).get.head === "Ben"
    * }}}
    * Composite molecules of arity 1 has only one sub-molecule with output attribute(s).
    * If the sub-molecule has multiple output attributes, a tuple is returned, otherwise
    * just the single value:
    * {{{
    *   Composite molecule           Composite type (1 output group)
    *
    *   A.a1       ~ B.b1_     =>    a1
    *   A.a1.a2    ~ B.b1_     =>    (a1, a2)
    *   A.a1.a2.a3 ~ B.b1_     =>    (a1, a2, a3) etc...
    *
    *   A.a1_ ~ B.b1           =>    b1
    *   A.a1_ ~ B.b1.b2        =>    (b1, b2)
    *   A.a1_ ~ B.b1.b2.b3)    =>    (b1, b2, b3) etc...
    *
    *   We could even have multiple tacit sub-molecules with multiple tacit attributes
    *   A.a1_.a2_ ~ B.b1_ ~ C.c1.c2_.c3     =>    (c1, c3) etc...
    * }}}
    * So, given two output attributes, a tuple is returned:
    * {{{
    *   m(Person.name.age ~ Tag.score_).get.head === ("Ben", 42)
    *   //  A   . a1 . a2 ~  B .  b1              => (  a1 , a2)
    * }}}
    * @group composite
    * @param dsl User-defined DSL structure modelling the composite molecule
    * @tparam T1 Type of output group 1
    * @return Composite molecule
    */
  implicit def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro from1tuple[T1]


  /** Macro creation of composite molecule from user-defined DSL structure with 2 output groups.
    * <br><br>
    * The builder pattern is used to add one or more attributes to an initial namespace
    * like `Person` from the example below. Further non-related attributes can be tied together
    * with the `~` method to form "composite molecules" that is basically just attributes
    * sharing the same entity id.
    * <br><br>
    * Once the composite molecule models the desired data structure
    * we can call various actions on it, like `get` that retrieves matching data from the database.
    * {{{
    *   // Explicitly calling `m` to create composite molecule with 2 output attributes
    *   m(Person.name ~ Tag.score).get.head === ("Ben", 7)
    *
    *   // Alternatively we can create the composite molecule implicitly
    *   Person.name.~(Tag.score).get.head === ("Ben", 7)
    * }}}
    * Composite molecules of arity 2 has two sub-molecules with output attribute(s). If a sub-molecule
    * has multiple output attributes, a tuple is returned, otherwise just the single value. The two
    * groups of either a single type or tuple are then tied together in an outer composite tuple:
    * {{{
    *   Composite molecule          Composite type (2 output groups)
    *
    *   A.a1    ~ B.b1        =>    (a1, b1)
    *   A.a1    ~ B.b1.b2     =>    (a1, (b1, b2))
    *   A.a1.a2 ~ B.b1        =>    ((a1, a2), b1)
    *   A.a1.a2 ~ B.b1.b2     =>    ((a1, a2), (b1, b2)) etc...
    *
    *   We could even have additional non-output sub-molecules:
    *   A.a1.a2 ~ B.b1.b2 ~ C.c1_     =>    ((a1, a2), (b1, b2)) etc...
    * }}}
    * Translating into the example:
    * {{{
    *   m(Person.name ~ Tag.score.flags).get.head                         === ("Ben", (7, 3))
    *   m(Person.name.age ~ Tag.score).get.head                           === (("Ben", 42), 7)
    *   m(Person.name.age ~ Tag.score.flags).get.head                     === (("Ben", 42), (7, 3))
    *
    *   m(Person.name.age ~
    *     Tag.score.flags ~
    *     Cat.name_("pitcher")).get.head === (("Ben", 42), (7, 3))
    * }}}
    * @group composite
    * @param dsl User-defined DSL structure modelling the composite molecule
    * @tparam T1 Type of output group 1
    * @tparam T2 Type of output group 2
    * @return Composite molecule
    */
  implicit def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro from2tuples[T1, T2]


  implicit def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro from3tuples[T1, T2, T3]
  implicit def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro from4tuples[T1, T2, T3, T4]
  implicit def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro from5tuples[T1, T2, T3, T4, T5]
  implicit def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro from6tuples[T1, T2, T3, T4, T5, T6]
  implicit def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro from20tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro from21tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Molecule22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro from22tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
