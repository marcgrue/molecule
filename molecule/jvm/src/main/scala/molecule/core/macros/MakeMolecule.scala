package molecule.core.macros

import molecule.datomic.base.transform.Model2Query
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMolecule(val c: blackbox.Context) extends Base {

  import c.universe._

  //        val z = InspectMacro("MakeMolecule", 9, 8)
  val z = InspectMacro("MakeMolecule", 1, 8)
  //  val z = InspectMacro("MakeMolecule", 1, 8, mkError = true)

  object traverser extends Traverser {
//       var applies = List[Apply]()
       override def traverse(tree: Tree): Unit = tree match {
//         case app @ Apply(fun, args) =>
//           applies = app :: applies
//           super.traverse(fun)
//           super.traverseTrees(args)
         case _ => super.traverse(tree)
       }
     }

  final def apply[Obj: W](body: Tree): Tree = {

    var fields  = Seq.empty[TermName]
    var methods = Seq.empty[(TermName, Seq[TermName])]

    val bodyTree = body match {
      case Function(_, Block(elements, _)) => elements.map {
        case ValDef(a, field, c, d) =>
          fields = fields :+ field
          ValDef(a, field, c, d)

        case DefDef(a, method, c, paramss, e, f) =>
          methods = paramss match {
            case Nil          => methods
            case List(Nil)    => methods
            case List(params) =>
              methods :+ (method, params.map {
                case ValDef(_, param, _, _) => param
              })
          }

          val paramss2: List[List[ValDef]] = paramss match {
            case Nil          => List()
            case List(Nil)    => List(List())
            case List(params) => List(params.map {
                case ValDef(a, b, c, d) => ValDef(a, b, c, d)
              })
          }
          DefDef(a, method, c, paramss2, e, f)

        case other => abort("Expecting only `def` and `val`s in molecule body. Found:\n" + other)
      }
    }

    //    List(
    //      DefDef(Modifiers(), TermName("a1"), List(), List(), TypeTree(), Literal(Constant(5))),
    //      DefDef(Modifiers(), TermName("a2"), List(), List(List()), TypeTree(), Literal(Constant(5))),
    //      DefDef(Modifiers(), TermName("a3"), List(), List(List(ValDef(Modifiers(PARAM), TermName("i"), TypeTree().setOriginal(Select(Ident(scala), scala.Int)), EmptyTree))), TypeTree(), Literal(Constant(5))),
    //      DefDef(Modifiers(), TermName("a4"), List(TypeDef(Modifiers(PARAM), TypeName("A"), List(), TypeTree().setOriginal(TypeBoundsTree(TypeTree(), TypeTree())))), List(List()), TypeTree(), Literal(Constant(5))),
    //      DefDef(
    //        Modifiers(),
    //        TermName("a5"),
    //        List(TypeDef(Modifiers(PARAM), TypeName("A"), List(), TypeTree().setOriginal(TypeBoundsTree(TypeTree(), TypeTree())))),
    //        List(List(ValDef(Modifiers(PARAM), TermName("i"), TypeTree().setOriginal(Select(Ident(scala), scala.Int)), EmptyTree))),
    //        TypeTree(),
    //        Literal(Constant(5))),
    //
    //      ValDef(Modifiers(MUTABLE), TermName("b"), TypeTree(), Literal(Constant(5))),
    //      ValDef(Modifiers(), TermName("hej"), TypeTree(), Literal(Constant(5))))



    val selectDynamic = if(fields.isEmpty) Nil else {
//      val caseClauses = fields.map(field => cq"${field.toString} => $field")
      Seq(q"""
          override def selectDynamic(name: String): Any = name match {
//            case ..caseClauses
            case other => throw new IllegalArgumentException("Please implement role method `" + other + "`.")
          }
       """)
    }

    val applyDynamic = if(methods.isEmpty) Nil else {
//      val caseClauses = methods.map{
//        case (method, params) => cq"${method.toString} => $method(..$params)"
//      }
      Seq(q"""
           override def applyDynamic(method: String)(i: Any): Any = method match {
//            case ..caseClauses
            case "a6" => a6(i.asInstanceOf[Int])
            case other => throw new IllegalArgumentException("Please implement role method `" + other + "`.")
          }
       """)
    }

    val t =
      q"""
        import molecule.core.transform.DynamicMolecule
        new DynamicMolecule with Init with Ns_int {
          final override lazy val int: Int = 7
          ..$bodyTree
          override def applyDynamic(method: String)(a: Any): Any = method match {
            case "a6"  => a6(a.asInstanceOf[Int])
            case other => throw new IllegalArgumentException("Please implement role method `" + other + "`.")
          }

          override def selectDynamic(name: String): Any = name match {
            case "a1"  => a1
            case other => throw new IllegalArgumentException("Please implement role method `" + other + "`.")
          }
//          ..{selectDynamic ++ applyDynamic}
        }
       """

    z(4
      //      , body
      , showRaw(body)
      //      , y
      //      , bodyElements
      //      , showRaw(bodyElements.head)
      //      , bodyElements.map(showRaw(_)) //.mkString("\n")
      //      , x
      , t
      //      , t2
    )
    t
  }


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
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = ${apply(ObjType, q"body")}
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = macro MakeMolecule(c).apply[$ObjType](body)
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = apply[$ObjType](body)
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = ${doApply(ObjType, q"body")}
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = new DynamicProp with Init with Ns_int {
        //              final override lazy val int: Int = 7
        //
        //              body
        //            }
        //            final override def apply(body: $ObjType => Unit): DynamicProp with $ObjType = macro MakeMolecule.apply[$ObjType]
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
