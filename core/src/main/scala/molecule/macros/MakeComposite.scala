package molecule.macros
import molecule.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox

/** Macro to make composite molecules. */
class MakeComposite(val c: blackbox.Context) extends Base {
  import c.universe._

  private[this] final def generateCompositeMolecule(dsl: Tree, OutTypes: Type*): Tree = {
    val MoleculeTpe = molecule_o(OutTypes.size)
    val outMolecule = TypeName(c.freshName("compositOutMolecule$"))
    val (model0, _, casts, jsons, _, hasVariables, _, _, _) = getModel(dsl)

    if (hasVariables) {
      q"""
        import molecule.ast.model._
        import molecule.ops.ModelOps._
        private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
        final class $outMolecule extends $MoleculeTpe[..$OutTypes](_resolvedModel, _root_.molecule.transform.Model2Query(_resolvedModel)) {
          final override def castRow(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})

          final override def getJsonFlat(conn: _root_.molecule.facade.Conn, n: Int): String = getJsonComposite(conn, n)
          final override def row2json(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = {..${compositeJsons(jsons)}}
        }
        new $outMolecule
      """
    } else {
      q"""
        import molecule.ast.model._
        final class $outMolecule extends $MoleculeTpe[..$OutTypes]($model0, ${Model2Query(model0)}) {
          final override def castRow(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})

          final override def getJsonFlat(conn: _root_.molecule.facade.Conn, n: Int): String = getJsonComposite(conn, n)
          final override def row2json(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = {..${compositeJsons(jsons)}}
        }
        new $outMolecule
      """
    }
  }

  // Composite molecules ....................................................

  final def from1tuple[T1: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1])
  final def from2tuples[T1: WeakTypeTag, T2: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2])
  final def from3tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3])
  final def from4tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4])
  final def from5tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5])
  final def from6tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6])
  final def from7tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7])
  final def from8tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8])
  final def from9tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9])
  final def from10tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10])
  final def from11tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11])
  final def from12tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12])
  final def from13tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13])
  final def from14tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14])
  final def from15tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15])
  final def from16tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16])
  final def from17tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17])
  final def from18tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag, T18: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18])
  final def from19tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag, T18: WeakTypeTag, T19: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19])
  final def from20tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag, T18: WeakTypeTag, T19: WeakTypeTag, T20: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20])
  final def from21tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag, T18: WeakTypeTag, T19: WeakTypeTag, T20: WeakTypeTag, T21: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21])
  final def from22tuples[T1: WeakTypeTag, T2: WeakTypeTag, T3: WeakTypeTag, T4: WeakTypeTag, T5: WeakTypeTag, T6: WeakTypeTag, T7: WeakTypeTag, T8: WeakTypeTag, T9: WeakTypeTag, T10: WeakTypeTag, T11: WeakTypeTag, T12: WeakTypeTag, T13: WeakTypeTag, T14: WeakTypeTag, T15: WeakTypeTag, T16: WeakTypeTag, T17: WeakTypeTag, T18: WeakTypeTag, T19: WeakTypeTag, T20: WeakTypeTag, T21: WeakTypeTag, T22: WeakTypeTag](dsl: Tree): Tree = generateCompositeMolecule(dsl, weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21], weakTypeOf[T22])
}