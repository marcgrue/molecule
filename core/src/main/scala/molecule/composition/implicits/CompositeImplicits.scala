package molecule.composition.implicits

import molecule.action.MoleculeOut._
import molecule.composition.composite.Composite._
import molecule.factory.MakeMolecule
import scala.language.experimental.macros
import scala.language.{higherKinds, implicitConversions}



trait CompositeImplicits {

  implicit def m[T1](dsl: Composite01[T1]): Molecule01[T1] = macro MakeMolecule.from1tuple[T1]
  implicit def m[T1, T2](dsl: Composite02[T1, T2]): Molecule02[T1, T2] = macro MakeMolecule.from2tuples[T1, T2]
  implicit def m[T1, T2, T3](dsl: Composite03[T1, T2, T3]): Molecule03[T1, T2, T3] = macro MakeMolecule.from3tuples[T1, T2, T3]
  implicit def m[T1, T2, T3, T4](dsl: Composite04[T1, T2, T3, T4]): Molecule04[T1, T2, T3, T4] = macro MakeMolecule.from4tuples[T1, T2, T3, T4]
  implicit def m[T1, T2, T3, T4, T5](dsl: Composite05[T1, T2, T3, T4, T5]): Molecule05[T1, T2, T3, T4, T5] = macro MakeMolecule.from5tuples[T1, T2, T3, T4, T5]
  implicit def m[T1, T2, T3, T4, T5, T6](dsl: Composite06[T1, T2, T3, T4, T5, T6]): Molecule06[T1, T2, T3, T4, T5, T6] = macro MakeMolecule.from6tuples[T1, T2, T3, T4, T5, T6]
  implicit def m[T1, T2, T3, T4, T5, T6, T7](dsl: Composite07[T1, T2, T3, T4, T5, T6, T7]): Molecule07[T1, T2, T3, T4, T5, T6, T7] = macro MakeMolecule.from7tuples[T1, T2, T3, T4, T5, T6, T7]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8](dsl: Composite08[T1, T2, T3, T4, T5, T6, T7, T8]): Molecule08[T1, T2, T3, T4, T5, T6, T7, T8] = macro MakeMolecule.from8tuples[T1, T2, T3, T4, T5, T6, T7, T8]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9](dsl: Composite09[T1, T2, T3, T4, T5, T6, T7, T8, T9]): Molecule09[T1, T2, T3, T4, T5, T6, T7, T8, T9] = macro MakeMolecule.from9tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](dsl: Composite10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]): Molecule10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = macro MakeMolecule.from10tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](dsl: Composite11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]): Molecule11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = macro MakeMolecule.from11tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](dsl: Composite12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]): Molecule12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = macro MakeMolecule.from12tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](dsl: Composite13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]): Molecule13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = macro MakeMolecule.from13tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](dsl: Composite14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]): Molecule14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = macro MakeMolecule.from14tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](dsl: Composite15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]): Molecule15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = macro MakeMolecule.from15tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](dsl: Composite16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]): Molecule16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = macro MakeMolecule.from16tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](dsl: Composite17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]): Molecule17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = macro MakeMolecule.from17tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](dsl: Composite18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]): Molecule18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = macro MakeMolecule.from18tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](dsl: Composite19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]): Molecule19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = macro MakeMolecule.from19tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](dsl: Composite20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]): Molecule20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = macro MakeMolecule.from20tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](dsl: Composite21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]): Molecule21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = macro MakeMolecule.from21tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21]
  implicit def m[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](dsl: Composite22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]): Molecule22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22] = macro MakeMolecule.from22tuples[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22]
}
