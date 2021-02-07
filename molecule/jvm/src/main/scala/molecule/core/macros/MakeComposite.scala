package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox

/** Macro to make composite molecules. */
class MakeComposite(val c: blackbox.Context) extends Base {
  import c.universe._

  private[this] final def generateCompositeMolecule(dsl: Tree, ObjType: Type, OutTypes: Type*): Tree = {
    val MoleculeTpe = molecule_o(OutTypes.size)
    val outMolecule = TypeName(c.freshName("compositOutMolecule$"))
    val (model0, _, casts, hasVariables, _, _, _, _, _) = getModel(dsl)

    if (hasVariables) {
      q"""
        import molecule.core.ast.elements._
        import molecule.core.ops.ModelOps._
        import molecule.datomic.base.transform.Model2Query

        private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
        final class $outMolecule extends $MoleculeTpe[$ObjType, ..$OutTypes](_resolvedModel, Model2Query(_resolvedModel)) {
          final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
        }
        new $outMolecule
      """
    } else {
      q"""
        import molecule.core.ast.elements._
        final class $outMolecule extends $MoleculeTpe[$ObjType, ..$OutTypes]($model0, ${Model2Query(model0)}) {
          final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
        }
        new $outMolecule
      """
    }
  }

  // Composite molecules ....................................................

  final def from01tuples[Obj: W, T1: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1])
  final def from02tuples[Obj: W, T1: W, T2: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2])
  final def from03tuples[Obj: W, T1: W, T2: W, T3: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3])
  final def from04tuples[Obj: W, T1: W, T2: W, T3: W, T4: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4])
  final def from05tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5])
  final def from06tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6])
  final def from07tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7])
  final def from08tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8])
  final def from09tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9])
  final def from10tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10])
  final def from11tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11])
  final def from12tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12])
  final def from13tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13])
  final def from14tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14])
  final def from15tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15])
  final def from16tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16])
  final def from17tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17])
  final def from18tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18])
  final def from19tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19])
  final def from20tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20])
  final def from21tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21])
  final def from22tuples[Obj: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[Obj], weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21], weakTypeOf[T22])
}