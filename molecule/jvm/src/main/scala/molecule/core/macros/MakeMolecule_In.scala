package molecule.core.macros

import molecule.datomic.transform.Model2Query
import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make input molecules. */
class MakeMolecule_In(val c: blackbox.Context) extends Base {
  import c.universe._

  private[this] final def generateInputMolecule(dsl: Tree, ObjType: Type, InTypes: Type*)(OutTypes: Type*): Tree = {
    val InputMoleculeTpe = inputMolecule_i_o(InTypes.size, OutTypes.size)
    val OutMoleculeTpe = molecule_o(OutTypes.size)
    val inputMolecule = TypeName(c.freshName("inputMolecule$"))
    val outMolecule = TypeName(c.freshName("outMolecule$"))
    val (model0, types, casts, hasVariables, postTypes, postCasts, _, _, _) = getModel(dsl)
    val flat = casts.size == 1

    // Methods for applying separate lists of input
    val applySeqs = InTypes match {
      case Seq(it1) => q"" // no extra

      case Seq(it1, it2) =>
        val (i1, i2) = (TermName(s"in1"), TermName(s"in2"))
        val (t1, t2) = (tq"Seq[$it1]", tq"Seq[$it2]")
        val (inParams, inTerm1, inTerm2) = (Seq(q"$i1: $t1", q"$i2: $t2"), i1, i2)
        if (flat) {
          q"""
            def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
              ) {
                final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
              }
              new $outMolecule
            }
          """
        } else {
          q"""
            def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2)
              val boundRawNestedQuery = bindSeqs(_rawNestedQuery.get, $inTerm1, $inTerm2)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), Some(QueryOptimizer(boundRawNestedQuery)),
                  boundRawQuery, Some(boundRawNestedQuery))
              ) with ${nestedTupleClassX(casts.size)}[(..$OutTypes)] {
                ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
              }
              new $outMolecule
            }
          """
        }

      case Seq(it1, it2, it3) =>
        val (i1, i2, i3) = (TermName(s"in1"), TermName(s"in2"), TermName(s"in3"))
        val (t1, t2, t3) = (tq"Seq[$it1]", tq"Seq[$it2]", tq"Seq[$it3]")
        val (inParams, inTerm1, inTerm2, inTerm3) = (Seq(q"$i1: $t1", q"$i2: $t2", q"$i3: $t3"), i1, i2, i3)
        if (flat) {
          q"""
            def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2, $inTerm3)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
              ) {
                final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
              }
              new $outMolecule
            }
          """
        } else {
          q"""
            def apply(..$inParams)(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindSeqs(_rawQuery, $inTerm1, $inTerm2, $inTerm3)
              val boundRawNestedQuery = bindSeqs(_rawNestedQuery.get, $inTerm1, $inTerm2, $inTerm3)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), Some(QueryOptimizer(boundRawNestedQuery)),
                  boundRawQuery, Some(boundRawNestedQuery))
              ) with ${nestedTupleClassX(casts.size)}[(..$OutTypes)] {
                ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
              }
              new $outMolecule
            }
          """
        }
    }

    if (flat) {
      if (hasVariables) {
        q"""
          import molecule.core.ast.elements._
          import molecule.core.ops.ModelOps._
          import molecule.core.transform.{Model2Query, QueryOptimizer}
          import molecule.datomic.base.facade.Conn

          private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $inputMolecule extends $InputMoleculeTpe[..$InTypes, ..$OutTypes](
            _resolvedModel, Model2Query(_resolvedModel)
          ) {
            def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindValues(_rawQuery, args)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
              ) {
                final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
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
          import molecule.core.transform.QueryOptimizer
          import molecule.datomic.base.facade.Conn

          final class $inputMolecule extends $InputMoleculeTpe[..$InTypes, ..$OutTypes]($model0, ${Model2Query(model0)}) {
            def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindValues(_rawQuery, args)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), None, boundRawQuery, None)
              ) {
                final override def row2tpl(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
              }
              new $outMolecule
            }
            $applySeqs
          }
          new $inputMolecule
        """
      }

    } else {

      if (hasVariables) {
        q"""
          import molecule.core.ast.elements._
          import molecule.core.ops.ModelOps._
          import molecule.core.transform.{Model2Query, QueryOptimizer}
          import molecule.datomic.base.facade.Conn

          private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $inputMolecule extends $InputMoleculeTpe[..$InTypes, ..$OutTypes](
            _resolvedModel, Model2Query(_resolvedModel)
          ) {
            def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindValues(_rawQuery, args)
              val boundRawNestedQuery = bindValues(_rawNestedQuery.get, args)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), Some(QueryOptimizer(boundRawNestedQuery)),
                  boundRawQuery, Some(boundRawNestedQuery))
              ) with ${nestedTupleClassX(casts.size)}[(..$OutTypes)] {
                ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
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
          import molecule.core.transform.QueryOptimizer
          import molecule.datomic.base.facade.Conn

          final class $inputMolecule extends $InputMoleculeTpe[..$InTypes, ..$OutTypes]($model0, ${Model2Query(model0)}) {
            def apply(args: Seq[(..$InTypes)])(implicit conn: Conn): $OutMoleculeTpe[..$OutTypes] = {
              val boundRawQuery = bindValues(_rawQuery, args)
              val boundRawNestedQuery = bindValues(_rawNestedQuery.get, args)
              final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](
                _model,
                (QueryOptimizer(boundRawQuery), Some(QueryOptimizer(boundRawNestedQuery)),
                  boundRawQuery, Some(boundRawNestedQuery))
              ) with ${nestedTupleClassX(casts.size)}[(..$OutTypes)] {
                ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
              }
              new $outMolecule
            }
            $applySeqs
          }
          new $inputMolecule
        """
      }
    }
  }


  // Input molecules with 1 input and 1-22 outputs

  final def await_1_1[Obj: W, I1: W, A: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A])
  final def await_1_2[Obj: W, I1: W, A: W, B: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B])
  final def await_1_3[Obj: W, I1: W, A: W, B: W, C: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C])
  final def await_1_4[Obj: W, I1: W, A: W, B: W, C: W, D: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D])
  final def await_1_5[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E])
  final def await_1_6[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F])
  final def await_1_7[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G])
  final def await_1_8[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H])
  final def await_1_9[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I])
  final def await_1_10[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J])
  final def await_1_11[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K])
  final def await_1_12[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L])
  final def await_1_13[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M])
  final def await_1_14[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N])
  final def await_1_15[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O])
  final def await_1_16[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P])
  final def await_1_17[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q])
  final def await_1_18[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R])
  final def await_1_19[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S])
  final def await_1_20[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T])
  final def await_1_21[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U])
  final def await_1_22[Obj: W, I1: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W, V: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U], weakTypeOf[V])


  // Input molecules with 2 inputs and 1-22 outputs

  final def await_2_1[Obj: W, I1: W, I2: W, A: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A])
  final def await_2_2[Obj: W, I1: W, I2: W, A: W, B: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B])
  final def await_2_3[Obj: W, I1: W, I2: W, A: W, B: W, C: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C])
  final def await_2_4[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D])
  final def await_2_5[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E])
  final def await_2_6[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F])
  final def await_2_7[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G])
  final def await_2_8[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H])
  final def await_2_9[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I])
  final def await_2_10[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J])
  final def await_2_11[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K])
  final def await_2_12[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L])
  final def await_2_13[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M])
  final def await_2_14[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N])
  final def await_2_15[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O])
  final def await_2_16[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P])
  final def await_2_17[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q])
  final def await_2_18[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R])
  final def await_2_19[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S])
  final def await_2_20[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T])
  final def await_2_21[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U])
  final def await_2_22[Obj: W, I1: W, I2: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W, V: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U], weakTypeOf[V])


  // Input molecules with 3 inputs and 1-22 outputs

  final def await_3_1[Obj: W, I1: W, I2: W, I3: W, A: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A])
  final def await_3_2[Obj: W, I1: W, I2: W, I3: W, A: W, B: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B])
  final def await_3_3[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C])
  final def await_3_4[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D])
  final def await_3_5[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E])
  final def await_3_6[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F])
  final def await_3_7[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G])
  final def await_3_8[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H])
  final def await_3_9[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I])
  final def await_3_10[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J])
  final def await_3_11[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K])
  final def await_3_12[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L])
  final def await_3_13[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M])
  final def await_3_14[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N])
  final def await_3_15[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O])
  final def await_3_16[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P])
  final def await_3_17[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q])
  final def await_3_18[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R])
  final def await_3_19[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S])
  final def await_3_20[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T])
  final def await_3_21[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U])
  final def await_3_22[Obj: W, I1: W, I2: W, I3: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W, V: W](dsl: Tree): Tree = generateInputMolecule(dsl, weakTypeOf[Obj], weakTypeOf[I1], weakTypeOf[I2], weakTypeOf[I3])(weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U], weakTypeOf[V])
}