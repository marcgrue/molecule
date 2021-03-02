package molecule.core.macros

import molecule.core.exceptions.MoleculeException
import molecule.datomic.base.transform.Model2Query
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMoleculeDynamic(val c: blackbox.Context) extends Base {

  import c.universe._
  //  import scala.reflect.runtime.universe.Flag.PARAM
  //  import scala.reflect.runtime.universe._

  override def abort(msg: String): Nothing = throw new MoleculeException(msg)

  //        val zz = InspectMacro("MakeMolecule", 9, 8)
  val zz = InspectMacro("MakeMoleculeDynamic", 1, 10)
  //  val zz = InspectMacro("MakeMolecule", 1, 8, mkError = true)


  final def apply[Obj: W](body: Tree): Tree = {


    object transformer extends Transformer {
      override def transform(tree: Tree): Tree = tree match {
        case t@Ident(TermName(n))                              => zz(100, t); Ident(TermName(n))
        case t@DefDef(mods, name, tparams, vparamss, tpt, rhs) => zz(11, t); DefDef(mods, name, tparams, vparamss.map(params => params.map(transform).asInstanceOf[List[ValDef]]), transform(tpt), transform(rhs))
        case t@ValDef(mods, name, tpt, rhs)                    => zz(12, t); ValDef(mods, name, transform(tpt), transform(rhs))
        case t@Select(qualifier, name)                         => zz(13, t); Select(transform(qualifier), name)
        case t@Apply(fun, args)                                => zz(14, t); Apply(transform(fun), args.map(transform))
        case t@TypeApply(fun, args)                            => zz(15, t); TypeApply(transform(fun), args.map(transform))
        case t@NamedArg(lhs, rhs)                              => zz(16, t); NamedArg(transform(lhs), transform(rhs))
        case t@Assign(lhs, rhs)                                => zz(17, t); Assign(transform(lhs), transform(rhs))
        case t@Function(vparams, body)                         => zz(18, t); Function(vparams.map(transform).asInstanceOf[List[ValDef]], transform(body))
        case t@Bind(name, body)                                => zz(19, t); Bind(name, transform(body))
        case t@Star(elem)                                      => zz(20, t); Star(transform(elem))
        case t@Alternative(trees)                              => zz(21, t); Alternative(trees.map(transform))
        case t@CaseDef(pat, guard, body)                       => zz(22, t); CaseDef(transform(pat), transform(guard), transform(body))
        case t@Block(stats, expr)                              => zz(23, t); Block(stats.map(transform), transform(expr))
        case t@If(cond, thenp, elsep)                          => zz(24, t); If(transform(cond), transform(thenp), transform(elsep))
        case t@ExistentialTypeTree(tpt, whereClauses)          => zz(25, t); ExistentialTypeTree(transform(tpt), whereClauses.map(transform).asInstanceOf[List[MemberDef]])
        case t@TypeBoundsTree(lo, hi)                          => zz(26, t); TypeBoundsTree(transform(lo), transform(hi))
        case t@Typed(expr, tpt)                                => zz(27, t); Typed(transform(expr), transform(tpt))
        case t@New(tpt)                                        => zz(28, t); New(transform(tpt))
        case t@Throw(expr)                                     => zz(29, t); Throw(transform(expr))
        case t@Try(block, catches, finalizer)                  => zz(30, t); Try(transform(block), catches.map(transform).asInstanceOf[List[CaseDef]], transform(finalizer))
        case t@Match(selector, cases)                          => zz(31, t); Match(transform(selector), cases.map(transform).asInstanceOf[List[CaseDef]])
        case t@Return(expr)                                    => zz(32, t); Return(transform(expr))
        case t@UnApply(fun, args)                              => zz(33, t); UnApply(transform(fun), args.map(transform))
        case t@ModuleDef(mods, name, impl)                     => zz(34, t); ModuleDef(mods, name, transform(impl).asInstanceOf[Template])
        case t@Template(parents, self, body)                   => zz(35, t); Template(parents.map(transform), transform(self).asInstanceOf[ValDef], body.map(transform))
        case t@TypeDef(mods, name, tparams, rhs)               => zz(36, t); TypeDef(mods, name, tparams.map(transform).asInstanceOf[List[TypeDef]], transform(rhs))
        case t@ClassDef(mods, name, tparams, impl)             => zz(37, t); ClassDef(mods, name, tparams.map(transform).asInstanceOf[List[TypeDef]], transform(impl).asInstanceOf[Template])
        case t@RefTree(qualifier, name)                        => zz(38, t); RefTree(transform(qualifier), name)
        case t@LabelDef(name, params, rhs)                     => zz(39, t); LabelDef(name, params.map(transform).asInstanceOf[List[Ident]], transform(rhs))
        case t                                                 => zz(101, t); super.transform(tree)
      }
    }

    val Function(vparams, Block(rawBodyTree, _)) = body
    val self                                     = transformer.transform(vparams.head)
    val bodyElements                             = rawBodyTree.map(transformer.transform)

    var fieldClauses  = Seq.empty[Tree]
    var methodClauses = Seq.empty[Tree]


    //    Block(
    //      List(
    //        Import(Select(Select(Ident(TermName("molecule")), TermName("core")), TermName("transform")), List(ImportSelector(TermName("DynamicMolecule"), -1, TermName("DynamicMolecule"), -1)))),
    //      Block(
    //        List(
    //          ClassDef(Modifiers(FINAL), TypeName("$anon"), List(), Template(
    //            List(Ident(TypeName("DynamicMolecule")), Ident(TypeName("Init")), Ident(TypeName("Ns_int"))),
    //            noSelfType,
    //            List(
    //              DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(), Block(List(pendingSuperCall), Literal(Constant(())))),
    //              ValDef(Modifiers(OVERRIDE | FINAL | LAZY), TermName("int"), Ident(TypeName("Int")), Literal(Constant(7))),
    //              ValDef(Modifiers(), TermName("bar"), TypeTree(), Ident(TermName("doe"))),
    //              ValDef(Modifiers(), TermName("v1"), TypeTree(), Apply(Select(Ident(TermName("bar")), TermName("$plus")), List(Ident(TermName("bar"))))),
    //              DefDef(Modifiers(), TermName("a1"), List(), List(), TypeTree(), Apply(Select(Ident(TermName("v1")), TermName("$plus")), List(Literal(Constant(1))))),
    //              DefDef(Modifiers(), TermName("a2"), List(), List(List()), TypeTree(), Apply(Select(Ident(TermName("a1")), TermName("$plus")), List(Literal(Constant(1))))),
    //              DefDef(Modifiers(), TermName("a3"), List(), List(List(
    //                ValDef(Modifiers(PARAM), TermName("i"), TypeTree().setOriginal(Select(Ident(scala), scala.Int)), EmptyTree))), TypeTree(), Apply(Select(Apply(Ident(TermName("a2")), List()), TermName("$plus")), List(Ident(TermName("i"))))),
    //              DefDef(Modifiers(), TermName("a4"), List(TypeDef(Modifiers(PARAM), TypeName("A"), List(), TypeTree().setOriginal(TypeBoundsTree(TypeTree(), TypeTree())))), List(List()), TypeTree(), Apply(Ident(TermName("a3")), List(Literal(Constant(2))))),
    //
    //              DefDef(Modifiers(), TermName("a5"), List(TypeDef(Modifiers(PARAM), TypeName("A"), List(), TypeTree().setOriginal(TypeBoundsTree(TypeTree(), TypeTree())))), List(
    //                List(
    //                  ValDef(Modifiers(PARAM), TermName("v"),
    //                    TypeTree().setOriginal(Ident(TypeName("A"))), EmptyTree))),
    //                TypeTree().setOriginal(Select(Select(Ident(scala), scala.Predef), TypeName("String"))), Apply(Select(Ident(TermName("v")), TermName("toString")), List())),
    //
    //              DefDef(Modifiers(), TermName("a6"), List(), List(
    //                List(
    //                  ValDef(Modifiers(PARAM), TermName("i"),
    //                    TypeTree().setOriginal(Select(Ident(scala), scala.Int)), EmptyTree))),
    //                TypeTree().setOriginal(Select(Ident(scala), scala.Int)), Apply(Select(Ident(TermName("i")), TermName("$plus")), List(Ident(TermName("a1"))))),
    //
    //              DefDef(Modifiers(), TermName("a7"), List(), List(List(
    //                ValDef(Modifiers(PARAM), TermName("i"), TypeTree().setOriginal(Select(Ident(scala), scala.Int)), EmptyTree),
    //                ValDef(Modifiers(PARAM), TermName("s"), TypeTree().setOriginal(Select(Select(Ident(scala), scala.Predef), TypeName("String"))), EmptyTree))),
    //                TypeTree(), Apply(Select(Ident(TermName("s")), TermName("$plus")), List(Ident(TermName("i"))))),
    //
    //              DefDef(Modifiers(OVERRIDE), TermName("applyDynamic"), List(), List(List(ValDef(Modifiers(PARAM), TermName("method"), Ident(TypeName("String")), EmptyTree)), List(ValDef(Modifiers(PARAM), TermName("args"), AppliedTypeTree(Select(Select(Ident(termNames.ROOTPKG), TermName("scala")), TypeName("<repeated>")), List(Ident(TypeName("Any")))), EmptyTree))), Ident(TypeName("Any")), Match(Ident(TermName("method")), List(
    //                CaseDef(Literal(Constant("a2")), EmptyTree, Apply(Ident(TermName("a2")), List())),
    //                CaseDef(Literal(Constant("a3")), EmptyTree, Apply(Ident(TermName("a3")), List(TypeApply(Select(Select(Ident(TermName("args")), TermName("head")), TermName("asInstanceOf")), List(Ident(TypeName("Int"))))))),
    //                CaseDef(Literal(Constant("a4")), EmptyTree, Apply(Ident(TermName("a4")), List())),
    //                CaseDef(Literal(Constant("a5")), EmptyTree, Apply(Ident(TermName("a5")), List(Select(Ident(TermName("args")), TermName("head"))))),
    //                CaseDef(Literal(Constant("a6")), EmptyTree, Apply(Ident(TermName("a6")), List(
    //                  TypeApply(Select(Select(Ident(TermName("args")), TermName("head")), TermName("asInstanceOf")), List(Ident(TypeName("Int"))))))),
    //                CaseDef(Literal(Constant("a7")), EmptyTree, Apply(Ident(TermName("a7")), List(
    //                  TypeApply(Select(Select(Ident(TermName("args")), TermName("head")), TermName("asInstanceOf")), List(Ident(TypeName("Int")))),
    //                  TypeApply(Select(Apply(Ident(TermName("args")), List(Literal(Constant(1)))), TermName("asInstanceOf")), List(Ident(TypeName("String"))))))),
    //                CaseDef(Bind(TermName("other"), Ident(termNames.WILDCARD)), EmptyTree, Throw(Apply(Select(New(Ident(TypeName("IllegalArgumentException"))), termNames.CONSTRUCTOR), List(Apply(Select(Apply(Select(Literal(Constant("Please implement `")), TermName("$plus")), List(Ident(TermName("other")))), TermName("$plus")), List(Literal(Constant("`."))))))))))),
    //
    //              DefDef(Modifiers(OVERRIDE), TermName("selectDynamic"), List(), List(List(ValDef(Modifiers(PARAM), TermName("name"), Ident(TypeName("String")), EmptyTree))), Ident(TypeName("Any")), Match(Ident(TermName("name")), List(
    //                CaseDef(Literal(Constant("v1")), EmptyTree, Ident(TermName("v1"))),
    //                CaseDef(Literal(Constant("a1")), EmptyTree, Ident(TermName("a1"))),
    //                CaseDef(Bind(TermName("other"), Ident(termNames.WILDCARD)), EmptyTree, Throw(Apply(Select(New(Ident(TypeName("IllegalArgumentException"))), termNames.CONSTRUCTOR), List(Apply(Select(Apply(Select(Literal(Constant("Please implement `")), TermName("$plus")), List(Ident(TermName("other")))), TermName("$plus")), List(Literal(Constant("`."))))))))))))))),
    //        Apply(Select(New(Ident(TypeName("$anon"))), termNames.CONSTRUCTOR), List())))

    rawBodyTree.foreach {
      case ValDef(_, TermName(field), _, _) =>
        fieldClauses = fieldClauses :+ cq"$field => ${TermName(field)}"

      case DefDef(mods, TermName(method), tparams, vparamss, tpt, rhs) => vparamss match {
        case Nil          => fieldClauses = fieldClauses :+ cq"$method => ${TermName(method)}"
        case List(Nil)    => methodClauses = methodClauses :+ cq"$method => ${TermName(method)}()"
        case List(params) =>
          val args = params.zipWithIndex.map {
            case (ValDef(_, _, Ident(TypeName(_)), _), i) => q"args($i)"
            case (ValDef(_, _, tpt, _), i)                => q"args($i).asInstanceOf[$tpt]"
          }
          methodClauses = methodClauses :+ cq"$method => ${TermName(method)}(..$args)"
      }
    }

    //    body match {
    //      case Function(_, Block(elements, _)) => elements.map {
    //        case vd@ValDef(a, field, c, d) =>
    //          fields = fields :+ field
    //
    //          val v2 = transformer.transform(d)
    //          //          ValDef(a, field, c, d)
    //          //          ValDef(a, field, c, v2)
    //          ValDef(a, field, c, reify(reify(d).splice).tree)
    //        //          transformer.transform(vd)
    //
    //        case DefDef(a, method, c, paramss, e, f) =>
    //          methods = paramss match {
    //            case Nil          => methods
    //            case List(Nil)    => methods
    //            case List(params) =>
    //              methods :+ (method, params.map {
    //                case ValDef(_, param, _, _) => param
    //              })
    //          }
    //
    //          val paramss2: List[List[ValDef]] = paramss match {
    //            case Nil          => List()
    //            case List(Nil)    => List(List())
    //            case List(params) => List(params.map {
    //              case ValDef(a, b, c, d) => ValDef(a, b, c, d)
    //            })
    //          }
    //          DefDef(a, method, c, paramss2, e, f)
    //
    //        case other => abort("Expecting only `def` and `val`s in molecule body. Found:\n" + other)
    //      }
    //    }

    val selectDynamic = if (fieldClauses.isEmpty) Nil else {
      Seq(
        q"""
          override def selectDynamic(name: String): Any = name match {
            case ..$fieldClauses
            case other => throw new IllegalArgumentException("Please implement `" + other + "`.")
          }
         """)
    }

    val applyDynamic = if (methodClauses.isEmpty) Nil else {
      Seq(
        q"""
          override def applyDynamic(method: String)(args: Any*): Any = method match {
            case ..$methodClauses
            case other => throw new IllegalArgumentException("Please implement `" + other + "`.")
          }
         """)
    }


    val t =
      q"""
        import molecule.core.transform.DynamicMolecule
        new DynamicMolecule with Init with Ns_int {
          final override lazy val int: Int = 7

          ..$bodyElements

          ..${selectDynamic ++ applyDynamic}
        }
       """

    zz(4
      //      , c.prefix
      //      , body
      //      , showRaw(body)
      //      , showRaw(bodyTree)
      //      , showRaw(defAndVals)
      //      , showRaw(a2)
      //      , showRaw(q"def a6(i: Int): Int = i + 1")
      , t
      , showRaw(q"def a5[A](v: A): String = v.toString")
      //      , showRaw(t)
    )
    t
  }


  val self: Tree = c.prefix.tree

  def implApply(method: Tree)(args: Tree*): Tree = {
    val q"${methodName: String}" = method
    val name                     = TermName(methodName.replaceAll("Macro$", ""))
    q"$self.$name(..$args)"
  }

  final def mkObj(dsl: Tree, body: Tree, ObjType: Type, TplTypes: Type*): Tree = {
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

    val casts = if (txMetaCompositesCount > 0) {
      // Treat tx meta data as composite
      q"(..${topLevel(List(castss.head))}, ..${compositeCasts(castss.tail, castss.head.length)})"
    } else {
      q"(..${topLevel(castss)})"
    }

    val Function(self, Block(defAndVals, _)) = body

    q"""
      ..$imports
      final class $outMolecule extends $OutMoleculeTpe[$ObjType, ..$TplTypes]($model0, ${Model2Query(model0)}) {
        final override def row2tpl(row: java.util.List[AnyRef]): (..$TplTypes) = $casts
        final override def row2obj(row: java.util.List[AnyRef]): $ObjType = ${objCode(obj)._1}
      }

      new DynamicMolecule with Init with Ns_int {
        lazy val __obj = (new $outMolecule).getObj
        final override lazy val int: Int = __obj.int

        ..$defAndVals
        //def a6(i: Int): Int = i + 1

        override def applyDynamic(method: String)(a: Any): Any = method match {
          case "a6"  => a6(a.asInstanceOf[Int])
          case other => throw new IllegalArgumentException("Please implement role method `" + other + "`.")
        }
      }
    """

  }
  final def from01attr[Obj: W, A: W](dsl: Tree)(body: Tree): Tree = mkObj(dsl, body, weakTypeOf[Obj], weakTypeOf[A])

}
