package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox

/** Macro to make composite input molecules. */
class MakeComposite_In(val c: blackbox.Context) extends Base {

  import c.universe._

  private[this] final def generateComposite_In_Molecule(dsl: Tree, ObjType: Type, InTypes: Type*)(OutTypes: Type*): Tree = {
    val (
      genericImports, model0, _, castss, obj,
      hasVariables, txMetaCompositesCount, _, _, _, _, _
      )                  = getModel(dsl)
    val imports          = getImports(genericImports)
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, OutTypes.size)
    val OutMoleculeTpe   = molecule_o(OutTypes.size)
    val inputMolecule    = TypeName(c.freshName("compositeInputMolecule$"))
    val outMolecule      = TypeName(c.freshName("compositeOutMolecule$"))

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

    // Methods for applying separate lists of input
    val applySeqs = InTypes match {
      case Seq(it1) => q"" // no extra

      case Seq(it1, it2) =>
        val (i1, i2)                     = (TermName(s"in1"), TermName(s"in2"))
        val (t1, t2)                     = (tq"Seq[$it1]", tq"Seq[$it2]")
        val (inParams, inTerm1, inTerm2) = (Seq(q"$i1: $t1", q"$i2: $t2"), i1, i2)
        q"""
          def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
              final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
            }
            new $outMolecule
          }
        """

      case Seq(it1, it2, it3) =>
        val (i1, i2, i3)                          = (TermName(s"in1"), TermName(s"in2"), TermName(s"in3"))
        val (t1, t2, t3)                          = (tq"Seq[$it1]", tq"Seq[$it2]", tq"Seq[$it3]")
        val (inParams, inTerm1, inTerm2, inTerm3) = (Seq(q"$i1: $t1", q"$i2: $t2", q"$i3: $t3"), i1, i2, i3)
        q"""
          def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2, $inTerm3)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
              final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
            }
            new $outMolecule
          }
        """
    }

    if (hasVariables) {
      q"""
        ..$imports
        private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
        final class $inputMolecule extends $InputMoleculeTpe[$ObjType, ..$InTypes, ..$OutTypes](
          _resolvedModel,
          Model2Query(_resolvedModel)
        ) {
          def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindValues(_rawQuery, args)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
              final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
            }
            new $outMolecule
          }
          $applySeqs
        }
        new $inputMolecule
      """
    } else {
      q"""
        ..$imports
        final class $inputMolecule extends $InputMoleculeTpe[$ObjType, ..$InTypes, ..$OutTypes]($model0, ${Model2Query(model0)}) {
          def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindValues(_rawQuery, args)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = $casts
              final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
            }
            new $outMolecule
          }
          $applySeqs
        }
        new $inputMolecule
      """
    }
  }


  final def await_1_01[Obj: W, I1: W, T1: W                                                                                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1])
  final def await_1_02[Obj: W, I1: W, T1: W, T2: W                                                                                                                                                         ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2])
  final def await_1_03[Obj: W, I1: W, T1: W, T2: W, T3: W                                                                                                                                                  ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3])
  final def await_1_04[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W                                                                                                                                           ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4])
  final def await_1_05[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W                                                                                                                                    ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5])
  final def await_1_06[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W                                                                                                                             ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6])
  final def await_1_07[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W                                                                                                                      ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7])
  final def await_1_08[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W                                                                                                               ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8])
  final def await_1_09[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W                                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9])
  final def await_1_10[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10])
  final def await_1_11[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11])
  final def await_1_12[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12])
  final def await_1_13[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13])
  final def await_1_14[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14])
  final def await_1_15[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15])
  final def await_1_16[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16])
  final def await_1_17[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17])
  final def await_1_18[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18])
  final def await_1_19[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19])
  final def await_1_20[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20])
  final def await_1_21[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21])
  final def await_1_22[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21], w[T22])

  final def await_2_01[Obj: W, I1: W, I2: W, T1: W                                                                                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1])
  final def await_2_02[Obj: W, I1: W, I2: W, T1: W, T2: W                                                                                                                                                         ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2])
  final def await_2_03[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W                                                                                                                                                  ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3])
  final def await_2_04[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W                                                                                                                                           ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4])
  final def await_2_05[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W                                                                                                                                    ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5])
  final def await_2_06[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W                                                                                                                             ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6])
  final def await_2_07[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W                                                                                                                      ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7])
  final def await_2_08[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W                                                                                                               ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8])
  final def await_2_09[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W                                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9])
  final def await_2_10[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10])
  final def await_2_11[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11])
  final def await_2_12[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12])
  final def await_2_13[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13])
  final def await_2_14[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14])
  final def await_2_15[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15])
  final def await_2_16[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16])
  final def await_2_17[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17])
  final def await_2_18[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18])
  final def await_2_19[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19])
  final def await_2_20[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20])
  final def await_2_21[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21])
  final def await_2_22[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21], w[T22])

  final def await_3_01[Obj: W, I1: W, I2: W, I3: W, T1: W                                                                                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1])
  final def await_3_02[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W                                                                                                                                                         ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2])
  final def await_3_03[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W                                                                                                                                                  ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3])
  final def await_3_04[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W                                                                                                                                           ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4])
  final def await_3_05[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W                                                                                                                                    ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5])
  final def await_3_06[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W                                                                                                                             ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6])
  final def await_3_07[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W                                                                                                                      ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7])
  final def await_3_08[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W                                                                                                               ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8])
  final def await_3_09[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W                                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9])
  final def await_3_10[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W                                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10])
  final def await_3_11[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W                                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11])
  final def await_3_12[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W                                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12])
  final def await_3_13[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W                                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13])
  final def await_3_14[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W                                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14])
  final def await_3_15[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W                                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15])
  final def await_3_16[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W                                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16])
  final def await_3_17[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W                                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17])
  final def await_3_18[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W                                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18])
  final def await_3_19[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W                        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19])
  final def await_3_20[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W                ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20])
  final def await_3_21[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W        ](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21])
  final def await_3_22[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, w[Obj], w[I1], w[I2], w[I3])(w[T1], w[T2], w[T3], w[T4], w[T5], w[T6], w[T7], w[T8], w[T9], w[T10], w[T11], w[T12], w[T13], w[T14], w[T15], w[T16], w[T17], w[T18], w[T19], w[T20], w[T21], w[T22])
}