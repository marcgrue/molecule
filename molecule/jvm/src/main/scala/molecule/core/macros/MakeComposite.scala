package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox

/** Macro to make composite molecules. */
class MakeComposite(val c: blackbox.Context) extends Base {

  import c.universe._

//  val z = InspectMacro("MakeMolecule", 1, 9, mkError = true)
//  val z = InspectMacro("MakeMolecule", 1, 9)
  val z = InspectMacro("MakeMolecule", 9, 8)


  private[this] final def generateCompositeMolecule(dsl: Tree, ObjType: Type, OutTypes: Type*): Tree = {
    val (
      genericImports, model0, typess, castss, obj,
      hasVariables, txMetaCompositesCount, _, _, _, _, _
      )             = getModel(dsl)
    val imports     = getImports(genericImports)
    val MoleculeTpe = molecule_o(OutTypes.size)
    val outMolecule = TypeName(c.freshName("compositOutMolecule$"))

    val casts = if (txMetaCompositesCount > 0) {
      val ordinaryComposites = castss.take(castss.length - txMetaCompositesCount)
      val txMetaComposites   = castss.takeRight(txMetaCompositesCount)
      val firstComposites    = ordinaryComposites.init
      val lastComposite      = ordinaryComposites.last
      val lastOffset         = firstComposites.flatten.length
      val metaOffset         = ordinaryComposites.flatten.length

      val first = compositeCasts(firstComposites)
      val last  = topLevel(List(lastComposite), lastOffset) ++ compositeCasts(txMetaComposites, metaOffset)

      //      z(1, model0, types, castss, first, last)
      (first, last) match {
        case (Nil, last)   => q"(..$last)"
        case (first, Nil)  => q"(..$first)"
        case (first, last) => q"(..$first, (..$last))"
      }
    } else {
      q"(..${compositeCasts(castss)})"
    }

    val t = if (hasVariables) {
      q"""
        ..$imports
        private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
        final class $outMolecule extends $MoleculeTpe[$ObjType, ..$OutTypes](_resolvedModel, Model2Query(_resolvedModel)) {
          final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
          final override def row2obj(row: java.util.List[AnyRef]): DynamicProp with $ObjType = ${objCode(obj)._1}
        }
        new $outMolecule
      """
    } else {
      q"""
        ..$imports
        final class $outMolecule extends $MoleculeTpe[$ObjType, ..$OutTypes]($model0, ${Model2Query(model0)}) {
          final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
          final override def row2obj(row: java.util.List[AnyRef]): DynamicProp with $ObjType = ${objCode(obj)._1}
        }
        new $outMolecule
      """
    }
    //    val q0 = Model2Query(model0)
    z(3
      , typess
      , obj
      , objCode(obj)._1
    )
    t
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