package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMolecule(val c: blackbox.Context) extends Base {

  import c.universe._

  //  val z = InspectMacro("MakeMolecule", 1, 900, mkError = true)
  val z = InspectMacro("MakeMolecule", 1, 900)

  private[this] final def generateMolecule(dsl: Tree, ObjType: Type, TplTypes: Type*): Tree = {
    val (
      model0, typess, castss, obj,
      hasVariables, txMetaCompositesCount,
      postTypes, postCasts, isOptNested,
      optNestedRefIndexes, optNestedTacitIndexes
      )                      = getModel(dsl)
    val OutMoleculeTpe: Tree = molecule_o(TplTypes.size)
    val outMolecule          = TypeName(c.freshName("outMolecule$"))

    //    val q0 = Model2Query(model0)
    //    z(1, model0, q0._1, q0._1.datalog, casts)
    //        z(1, model0, typess, castss, hasTxMetaComposites)

    val t = if (castss.size == 1 || txMetaCompositesCount > 0) {
      val casts = if (txMetaCompositesCount > 0)
        q"(..${topLevel(List(castss.head))}, ..${compositeCasts(castss.tail, castss.head.length)})"
      else
        q"(..${topLevel(castss)})"

      if (hasVariables) {
        q"""
          import molecule.core.ast.elements._
          import molecule.core.ops.ModelOps._
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, _root_.molecule.datomic.base.transform.Model2Query(_resolvedModel)) {
            final override def row2tpl(row: java.util.List[AnyRef]): (..$TplTypes) = $casts
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType      = ???
          }
          new $outMolecule
        """
      } else {


        //        val t1 = "molecule.tests.core.base.dsl.CoreTest.Ns_int"
        //        val t2 = tq"molecule.tests.core.base.dsl.CoreTest.Ns_str"

        //        val t4 = TypeName("DummyProp with molecule.tests.core.base.dsl.CoreTest.Ref1_int1 with molecule.tests.core.base.dsl.CoreTest.Ref1_str1")
        //        val t3 = tq"molecule.tests.core.base.dsl.CoreTest.Ns_Ref1_[$t4]"
        lazy val t1 = tq"Ns_int"
        lazy val t2 = tq"Ns_str"
        lazy val t4 = TypeName("DummyProp with Ref1_int1")
        lazy val t3 = tq"Ns_Ref1_[DummyProp with Ref1_int1]"

        val ts = Seq(
          tq"molecule.tests.core.base.dsl.CoreTest.Ns_int",
          tq"molecule.tests.core.base.dsl.CoreTest.Ns_str"
        )

        val body0 =
          q"""{
              new DummyProp with $t1 with $t2 with $t3 {
                final override lazy val int: Int = castOneInt(row, 0)
                final override lazy val str: String = castOne[String](row, 1)
                final override def Ref1: DummyProp with Ref1_int1 = new DummyProp with Ref1_int1 {
                  final override lazy val int1: Int = 1
                }
              }
            }
             """
        val body1 = objCode(obj, -1)._1

        val tt =
          q"""
          import molecule.core.ast.elements._
          import molecule.core.dsl.base.DummyProp
              import molecule.tests.core.base.dsl.CoreTest._
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)}) {
            final override def row2tpl(row: java.util.List[AnyRef]): (..$TplTypes) = $casts
            final override def row2obj(row: java.util.List[AnyRef]): $ObjType      = //{objCode(obj, -1)} //???
              $body1
          }
          new $outMolecule
        """
        //              new molecule.tests.core.base.dsl.CoreTest.Ns_int with molecule.tests.core.base.dsl.CoreTest.Ns_str {
        //              new A with $ot4 {
        //              new A with $d1 with $d2 {
        //              new molecule.tests.core.base.dsl.CoreTest.Ns_int with molecule.tests.core.base.dsl.CoreTest.Ns_str {
        //              class obj() extends $ObjType {
        //              (new molecule.tests.core.base.dsl.CoreTest.Ns_int {
        //              (new molecule.tests.core.base.dsl.CoreTest.Ns_int {

        //        val obj = Obj("", 0, Seq(
        //          Prop(tq"molecule.tests.core.base.dsl.CoreTest.Ns_int", "int", tq"Int", castOneAttr("Int")),
        //          Prop(tq"molecule.tests.core.base.dsl.CoreTest.Ns_str", "str", tq"String", castOneAttr("String"))
        //        ))
        //
        //        val t0 = tq""

        z(1
          , obj
          , body0
          , body1
//          , ObjType
//          , showRaw(ObjType)
          //          , q"new $t1 with $t2 with $t3 {}"
          //          , getObj(obj, 0)
          , tt
        )

        tt
      }

    } else if (isOptNested) {
      if (hasVariables) {
        q"""
          import molecule.core.ast.elements._
          import molecule.core.ops.ModelOps._
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, _root_.molecule.datomic.base.transform.Model2Query(_resolvedModel)) {
            ..${castOptNestedRows(castss, TplTypes, optNestedRefIndexes, optNestedTacitIndexes)}
          }
          new $outMolecule
        """
      } else {
        q"""
          import molecule.core.ast.elements._
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)}) {
            ..${castOptNestedRows(castss, TplTypes, optNestedRefIndexes, optNestedTacitIndexes)}
          }
          new $outMolecule
        """
      }

    } else {
      // Nested

      if (hasVariables) {
        q"""
          import molecule.core.ast.elements._
          import molecule.core.ops.ModelOps._
          final private val _resolvedModel: Model = resolveIdentifiers($model0, ${mapIdentifiers(model0.elements).toMap})
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes](_resolvedModel, _root_.molecule.datomic.base.transform.Model2Query(_resolvedModel))
            with ${nestedTupleClassX(castss.size)}[$ObjType, (..$TplTypes)] {
            ..${resolveNestedTupleMethods(castss, typess, TplTypes, postTypes, postCasts).get}
          }
          new $outMolecule
        """
      } else {
        q"""
          import molecule.core.ast.elements._
          final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)})
            with ${nestedTupleClassX(castss.size)}[$ObjType, (..$TplTypes)] {
            ..${resolveNestedTupleMethods(castss, typess, TplTypes, postTypes, postCasts).get}
          }
          new $outMolecule
        """
      }
    }
    //            z(2, t, model0, typess, castss)
    t
  }


  final def from01attr[Obj: W, A: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A])
  final def from02attr[Obj: W, A: W, B: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B])
  final def from03attr[Obj: W, A: W, B: W, C: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C])
  final def from04attr[Obj: W, A: W, B: W, C: W, D: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D])
  final def from05attr[Obj: W, A: W, B: W, C: W, D: W, E: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E])
  final def from06attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F])
  final def from07attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G])
  final def from08attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H])
  final def from09attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I])
  final def from10attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J])
  final def from11attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K])
  final def from12attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L])
  final def from13attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M])
  final def from14attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N])
  final def from15attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O])
  final def from16attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P])
  final def from17attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q])
  final def from18attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R])
  final def from19attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S])
  final def from20attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T])
  final def from21attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U])
  final def from22attr[Obj: W, A: W, B: W, C: W, D: W, E: W, F: W, G: W, H: W, I: W, J: W, K: W, L: W, M: W, N: W, O: W, P: W, Q: W, R: W, S: W, T: W, U: W, V: W](dsl: Tree): Tree = generateMolecule(dsl, weakTypeOf[Obj], weakTypeOf[A], weakTypeOf[B], weakTypeOf[C], weakTypeOf[D], weakTypeOf[E], weakTypeOf[F], weakTypeOf[G], weakTypeOf[H], weakTypeOf[I], weakTypeOf[J], weakTypeOf[K], weakTypeOf[L], weakTypeOf[M], weakTypeOf[N], weakTypeOf[O], weakTypeOf[P], weakTypeOf[Q], weakTypeOf[R], weakTypeOf[S], weakTypeOf[T], weakTypeOf[U], weakTypeOf[V])
}
