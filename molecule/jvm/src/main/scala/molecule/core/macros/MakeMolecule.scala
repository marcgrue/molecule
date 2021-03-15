package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMolecule(val c: blackbox.Context) extends Base {

  import c.universe._

  val z = InspectMacro("MakeMolecule", 9, 8)
  //  val z = InspectMacro("MakeMolecule", 1, 8)
  //  val z = InspectMacro("MakeMolecule", 1, 8, mkError = true)


  private[this] final def generateMolecule(dsl: Tree, ObjType: Type, TplTypes: Type*): Tree = {
    val (
      genericImports, model0, typess, castss, obj,
      hasVariables, txMetaCompositesCount,
      postTypes, postCasts, isOptNested,
      optNestedRefIndexes, optNestedTacitIndexes
      )                      = getModel(dsl)
    val imports              = getImports(genericImports)
    val OutMoleculeTpe: Tree = molecule_o(TplTypes.size)
    val outMolecule          = TypeName(c.freshName("outMolecule$"))
    lazy val tpl = if (TplTypes.length == 1)
      q"val tpl: Product = Tuple1(row2tpl(row))"
    else
      q"val tpl: Product = row2tpl(row)"

    val t = if (castss.size == 1 || txMetaCompositesCount > 0) {
      val casts = if (txMetaCompositesCount > 0) {
        // Treat tx meta data as composite
        q"(..${topLevel(List(castss.head))}, ..${compositeCasts(castss.tail, castss.head.length)})"
      } else {
        q"(..${topLevel(castss)})"
      }

      if (hasVariables) {
        q"""
          ..$imports
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, Model2Query(_resolvedModel)) {
            final override def row2tpl(row: java.util.List[AnyRef]): (..$TplTypes) = $casts
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
          }
          new $outMolecule
        """
      } else {
        q"""
          ..$imports
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)}) {
            final override def row2tpl(row: java.util.List[AnyRef]): (..$TplTypes) = $casts
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
          }
          new $outMolecule
        """
      }

    } else if (isOptNested) {
      if (hasVariables) {
        q"""
          ..$imports
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, Model2Query(_resolvedModel)) {
            ..${castOptNestedRows(castss, TplTypes, optNestedRefIndexes, optNestedTacitIndexes)}
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType = {
              $tpl
              ${objCode(obj, isNested = true)._1}
            }
          }
          new $outMolecule
        """
      } else {
        q"""
          ..$imports
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)}) {
            ..${castOptNestedRows(castss, TplTypes, optNestedRefIndexes, optNestedTacitIndexes)}
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType = {
              $tpl
              ${objCode(obj, isNested = true)._1}
            }
          }
          new $outMolecule
        """
      }

    } else {
      // Nested

      if (hasVariables) {
        q"""
          ..$imports
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, Model2Query(_resolvedModel))
            with ${nestedTupleClassX(castss.size)}[$ObjType, (..$TplTypes)] {
            ..${resolveNestedTupleMethods(castss, typess, TplTypes, postTypes, postCasts).get}
            final override def outerTpl2obj(tpl: (..$TplTypes)): $ObjType = {
              $tpl
              ${objCode(obj, isNested = true)._1}
            }
          }
          new $outMolecule
        """
      } else {
        q"""
          ..$imports
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)})
            with ${nestedTupleClassX(castss.size)}[$ObjType, (..$TplTypes)] {
            ..${resolveNestedTupleMethods(castss, typess, TplTypes, postTypes, postCasts).get}
            final override def outerTpl2obj(tpl: (..$TplTypes)): $ObjType = {
              $tpl
              ${objCode(obj, isNested = true)._1}
            }
          }
          new $outMolecule
        """
      }
    }

    z(3
      , model0
      //      , typess
      //      , castss
      , obj
      , objCode(obj)._1
      //      , objCode(obj, isNested = true)._1
      , t
    )
    t
  }


  final def from01attr[Obj: W, A: W                                                                                                                              ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A])
  final def from02attr[Obj: W, A: W, B: W                                                                                                                        ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B])
  final def from03attr[Obj: W, A: W, B: W, C: W                                                                                                                  ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C])
  final def from04attr[Obj: W, A: W, B: W, C: W, D: W                                                                                                            ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D])
  final def from05attr[Obj: W, A: W, B: W, C: W, D: W, E: W                                                                                                      ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E])
  final def from06attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W                                                                                                ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F])
  final def from07attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W                                                                                          ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G])
  final def from08attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W                                                                                    ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H])
  final def from09attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W                                                                              ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I])
  final def from10attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W                                                                        ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J])
  final def from11attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W                                                                  ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K])
  final def from12attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W                                                            ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L])
  final def from13attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W                                                      ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M])
  final def from14attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W                                                ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N])
  final def from15attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W                                          ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O])
  final def from16attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W                                    ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P])
  final def from17attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W                              ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q])
  final def from18attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W                        ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q], w[R])
  final def from19attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W                  ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q], w[R], w[S])
  final def from20attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W            ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q], w[R], w[S], w[T])
  final def from21attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W      ](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q], w[R], w[S], w[T], w[U])
  final def from22attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W, V: W](dsl: Tree): Tree = generateMolecule(dsl, w[Obj], w[A], w[B], w[C], w[D], w[E], w[F], w[G], w[H], w[I], w[J], w[K], w[L], w[M], w[N], w[O], w[P], w[Q], w[R], w[S], w[T], w[U], w[V])
}
