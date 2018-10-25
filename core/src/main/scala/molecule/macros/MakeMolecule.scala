package molecule.macros
import molecule.transform.Model2Query
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMolecule(val c: blackbox.Context) extends Base {
  import c.universe._

  private[this] final def generateMolecule(dsl: Tree, OutTypes: Type*): Tree = {
    val OutMoleculeTpe: Tree = molecule_o(OutTypes.size)
    val outMolecule = TypeName(c.freshName("outMolecule$"))
    val (model0, types, casts, jsons, nestedRefAttrs, hasVariables, postTypes, postCasts, postJsons) = getModel2(dsl)

    if (casts.size == 1) {
      if (hasVariables) {
        q"""
          import molecule.ast.model._
          import molecule.ops.ModelOps._
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](_resolvedModel, _root_.molecule.transform.Model2Query(_resolvedModel)) {
            final override def castRow(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
            final override def row2json(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = {..${topLevelJson(jsons)}}
          }
          new $outMolecule
        """
      } else {
        q"""
          {
            import molecule.ast.model._
            final class $outMolecule extends $OutMoleculeTpe[..$OutTypes]($model0, ${Model2Query(model0)}) {
              final override def castRow(row: java.util.List[AnyRef]): (..$OutTypes) = (..${topLevel(casts)})
              final override def row2json(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = {..${topLevelJson(jsons)}}
            }
            new $outMolecule
          }
        """
      }

    } else {
      // Nested

      if (hasVariables) {
        q"""
          import molecule.ast.model._
          import molecule.ops.ModelOps._
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[..$OutTypes](_resolvedModel, _root_.molecule.transform.Model2Query(_resolvedModel))
            with ${nestedJsonClassX(casts.size)}[(..$OutTypes)] {
            ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
            ..${resolveNestedJsonMethods(jsons, nestedRefAttrs, postJsons).get}
          }
          new $outMolecule
        """
      } else {
        q"""
          import molecule.ast.model._
          final class $outMolecule extends $OutMoleculeTpe[..$OutTypes]($model0, ${Model2Query(model0)})
            with ${nestedJsonClassX(casts.size)}[(..$OutTypes)] {
            ..${resolveNestedTupleMethods(casts, types, OutTypes, postTypes, postCasts).get}
            ..${resolveNestedJsonMethods(jsons, nestedRefAttrs, postJsons).get}
          }
          new $outMolecule
        """
      }
    }
  }

  final def from1attr[Ns1[_], Ns2[_, _], In1_1[_, _], In1_2[_, _, _], A: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A])
  final def from2attr[Ns2[_, _], Ns3[_, _, _], In1_2[_, _, _], In1_3[_, _, _, _], A: WeakTypeTag, B: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B])
  final def from3attr[Ns3[_, _, _], Ns4[_, _, _, _], In1_3[_, _, _, _], In1_4[_, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C])
  final def from4attr[Ns4[_, _, _, _], Ns5[_, _, _, _, _], In1_4[_, _, _, _, _], In1_5[_, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D])
  final def from5attr[Ns5[_, _, _, _, _], Ns6[_, _, _, _, _, _], In1_5[_, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E])
  final def from6attr[Ns6[_, _, _, _, _, _], Ns7[_, _, _, _, _, _, _], In1_6[_, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F])
  final def from7attr[Ns7[_, _, _, _, _, _, _], Ns8[_, _, _, _, _, _, _, _], In1_7[_, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G])
  final def from8attr[Ns8[_, _, _, _, _, _, _, _], Ns9[_, _, _, _, _, _, _, _, _], In1_8[_, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H])
  final def from9attr[Ns9[_, _, _, _, _, _, _, _, _], Ns10[_, _, _, _, _, _, _, _, _, _], In1_9[_, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I])
  final def from10attr[Ns10[_, _, _, _, _, _, _, _, _, _], Ns11[_, _, _, _, _, _, _, _, _, _, _], In1_10[_, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J])
  final def from11attr[Ns11[_, _, _, _, _, _, _, _, _, _, _], Ns12[_, _, _, _, _, _, _, _, _, _, _, _], In1_11[_, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K])
  final def from12attr[Ns12[_, _, _, _, _, _, _, _, _, _, _, _], Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_12[_, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L])
  final def from13attr[Ns13[_, _, _, _, _, _, _, _, _, _, _, _, _], Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_13[_, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M])
  final def from14attr[Ns14[_, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N])
  final def from15attr[Ns15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O])
  final def from16attr[Ns16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P])
  final def from17attr[Ns17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q])
  final def from18attr[Ns18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag, R: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R])
  final def from19attr[Ns19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag, R: WeakTypeTag, S: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S])
  final def from20attr[Ns20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag, R: WeakTypeTag, S: WeakTypeTag, T: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T])
  final def from21attr[Ns21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag, R: WeakTypeTag, S: WeakTypeTag, T: WeakTypeTag, U: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U])
  final def from22attr[Ns22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], Ns23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], In1_23[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _], A: WeakTypeTag, B: WeakTypeTag, C: WeakTypeTag, D: WeakTypeTag, E: WeakTypeTag, F: WeakTypeTag, G: WeakTypeTag, H: WeakTypeTag, I: WeakTypeTag, J: WeakTypeTag, K: WeakTypeTag, L: WeakTypeTag, M: WeakTypeTag, N: WeakTypeTag, O: WeakTypeTag, P: WeakTypeTag, Q: WeakTypeTag, R: WeakTypeTag, S: WeakTypeTag, T: WeakTypeTag, U: WeakTypeTag, V: WeakTypeTag](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U], weakTypeOf[V])
}
