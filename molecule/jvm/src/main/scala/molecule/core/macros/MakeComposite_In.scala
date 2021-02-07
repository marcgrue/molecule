package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox

/** Macro to make composite input molecules. */
class MakeComposite_In(val c: blackbox.Context) extends Base {
  import c.universe._

  private[this] final def generateComposite_In_Molecule(dsl: Tree, ObjType: Type, InTypes: Type*)(OutTypes: Type*): Tree = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, OutTypes.size)
    val OutMoleculeTpe = molecule_o(OutTypes.size)
    val inputMolecule = TypeName(c.freshName("compositeInputMolecule$"))
    val outMolecule = TypeName(c.freshName("compositeOutMolecule$"))
    val (model0, _, casts, hasVariables, _, _, _, _, _) = getModel(dsl)

    // Methods for applying separate lists of input
    val applySeqs = InTypes match {
      case Seq(it1) => q"" // no extra

      case Seq(it1, it2) =>
        val (i1, i2) = (TermName(s"in1"), TermName(s"in2"))
        val (t1, t2) = (tq"Seq[$it1]", tq"Seq[$it2]")
        val (inParams, inTerm1, inTerm2) = (Seq(q"$i1: $t1", q"$i2: $t2"), i1, i2)
        q"""
          def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
            }
            new $outMolecule
          }
        """

      case Seq(it1, it2, it3) =>
        val (i1, i2, i3) = (TermName(s"in1"), TermName(s"in2"), TermName(s"in3"))
        val (t1, t2, t3) = (tq"Seq[$it1]", tq"Seq[$it2]", tq"Seq[$it3]")
        val (inParams, inTerm1, inTerm2, inTerm3) = (Seq(q"$i1: $t1", q"$i2: $t2", q"$i3: $t3"), i1, i2, i3)
        q"""
          def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2, $inTerm3)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
            }
            new $outMolecule
          }
        """
    }

    if (hasVariables) {
      q"""
        import molecule.core.ast.elements._
        import molecule.core.ops.ModelOps._
        import molecule.datomic.base.transform.{Model2Query, QueryOptimizer}
        import molecule.datomic.base.facade.Conn

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
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
            }
            new $outMolecule
          }
          $applySeqs
        }
        new $inputMolecule
      """
    } else {
      q"""
        import molecule.core.ast.elements._
        import molecule.datomic.base.transform.QueryOptimizer
        import molecule.datomic.base.facade.Conn

        final class $inputMolecule extends $InputMoleculeTpe[$ObjType, ..$InTypes, ..$OutTypes]($model0, ${Model2Query(model0)}) {
          def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[$ObjType, ..$OutTypes] = {
            val boundRawQuery = bindValues(_rawQuery, args)
            final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$OutTypes](
              _model,
              (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
            ) {
              final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${compositeCasts(casts)})
            }
            new $outMolecule
          }
          $applySeqs
        }
        new $inputMolecule
      """
    }
  }


  final def await_1_1[Obj: W, I1: W, T1: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1])
  final def await_1_2[Obj: W, I1: W, T1: W, T2: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2])
  final def await_1_3[Obj: W, I1: W, T1: W, T2: W, T3: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3])
  final def await_1_4[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4])
  final def await_1_5[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5])
  final def await_1_6[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6])
  final def await_1_7[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7])
  final def await_1_8[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8])
  final def await_1_9[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9])
  final def await_1_10[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10])
  final def await_1_11[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11])
  final def await_1_12[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12])
  final def await_1_13[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13])
  final def await_1_14[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14])
  final def await_1_15[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15])
  final def await_1_16[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16])
  final def await_1_17[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17])
  final def await_1_18[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18])
  final def await_1_19[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19])
  final def await_1_20[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20])
  final def await_1_21[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21])
  final def await_1_22[Obj: W, I1: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21], weakTypeOf[T22])

  final def await_2_1[Obj: W, I1: W, I2: W, T1: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1])
  final def await_2_2[Obj: W, I1: W, I2: W, T1: W, T2: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2])
  final def await_2_3[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3])
  final def await_2_4[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4])
  final def await_2_5[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5])
  final def await_2_6[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6])
  final def await_2_7[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7])
  final def await_2_8[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8])
  final def await_2_9[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9])
  final def await_2_10[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10])
  final def await_2_11[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11])
  final def await_2_12[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12])
  final def await_2_13[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13])
  final def await_2_14[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14])
  final def await_2_15[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15])
  final def await_2_16[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16])
  final def await_2_17[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17])
  final def await_2_18[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18])
  final def await_2_19[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19])
  final def await_2_20[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20])
  final def await_2_21[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21])
  final def await_2_22[Obj: W, I1: W, I2: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21], weakTypeOf[T22])

  final def await_3_1[Obj: W, I1: W, I2: W, I3: W, T1: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1])
  final def await_3_2[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2])
  final def await_3_3[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3])
  final def await_3_4[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4])
  final def await_3_5[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5])
  final def await_3_6[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6])
  final def await_3_7[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7])
  final def await_3_8[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8])
  final def await_3_9[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9])
  final def await_3_10[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10])
  final def await_3_11[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11])
  final def await_3_12[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12])
  final def await_3_13[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13])
  final def await_3_14[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14])
  final def await_3_15[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15])
  final def await_3_16[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16])
  final def await_3_17[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17])
  final def await_3_18[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18])
  final def await_3_19[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19])
  final def await_3_20[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20])
  final def await_3_21[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21])
  final def await_3_22[Obj: W, I1: W, I2: W, I3: W, T1: W, T2: W, T3: W, T4: W, T5: W, T6: W, T7: W, T8: W, T9: W, T10: W, T11: W, T12: W, T13: W, T14: W, T15: W, T16: W, T17: W, T18: W, T19: W, T20: W, T21: W, T22: W](dsl: Tree): Tree = generateComposite_In_Molecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[T1], weakTypeOf[T2], weakTypeOf[T3], weakTypeOf[T4], weakTypeOf[T5], weakTypeOf[T6], weakTypeOf[T7], weakTypeOf[T8], weakTypeOf[T9], weakTypeOf[T10], weakTypeOf[T11], weakTypeOf[T12], weakTypeOf[T13], weakTypeOf[T14], weakTypeOf[T15], weakTypeOf[T16], weakTypeOf[T17], weakTypeOf[T18], weakTypeOf[T19], weakTypeOf[T20], weakTypeOf[T21], weakTypeOf[T22])
}