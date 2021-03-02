package molecule.core.macros

import molecule.core.exceptions.MoleculeException
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make output molecules. */
class MakeMoleculeDynamic(val c: blackbox.Context) extends Base {

  import c.universe._

  override def abort(msg: String): Nothing = throw new MoleculeException(msg)

  val zz = InspectMacro("MakeMolecule", 9, 8)
  //    val zz = InspectMacro("MakeMoleculeDynamic", 1, 10)
  //  val zz = InspectMacro("MakeMoleculeDynamic", 2, 10, mkError = true)

  // Recursively ensure that all Ident's are explicit - necessary when working with dynamic code
  // Note that only expected extractors are used. So, some very specialized use case might complain at some point...
  object transformer extends Transformer {
    override def transform(tree: Tree): Tree = tree match {
      case Ident(TermName(n))                              => Ident(TermName(n))
      case DefDef(mods, name, tparams, vparamss, tpt, rhs) => DefDef(mods, name, tparams, vparamss.map(params => params.map(transform).asInstanceOf[List[ValDef]]), transform(tpt), transform(rhs))
      case ValDef(mods, name, tpt, rhs)                    => ValDef(mods, name, transform(tpt), transform(rhs))
      case Select(qualifier, name)                         => Select(transform(qualifier), name)
      case Apply(fun, args)                                => Apply(transform(fun), args.map(transform))
      case TypeApply(fun, args)                            => TypeApply(transform(fun), args.map(transform))
      case NamedArg(lhs, rhs)                              => NamedArg(transform(lhs), transform(rhs))
      case Assign(lhs, rhs)                                => Assign(transform(lhs), transform(rhs))
      case Function(vparams, body)                         => Function(vparams.map(transform).asInstanceOf[List[ValDef]], transform(body))
      case Bind(name, body)                                => Bind(name, transform(body))
      case Star(elem)                                      => Star(transform(elem))
      case Alternative(trees)                              => Alternative(trees.map(transform))
      case CaseDef(pat, guard, body)                       => CaseDef(transform(pat), transform(guard), transform(body))
      case Block(stats, expr)                              => Block(stats.map(transform), transform(expr))
      case If(cond, thenp, elsep)                          => If(transform(cond), transform(thenp), transform(elsep))
      case ExistentialTypeTree(tpt, whereClauses)          => ExistentialTypeTree(transform(tpt), whereClauses.map(transform).asInstanceOf[List[MemberDef]])
      case TypeBoundsTree(lo, hi)                          => TypeBoundsTree(transform(lo), transform(hi))
      case Typed(expr, tpt)                                => Typed(transform(expr), transform(tpt))
      case New(tpt)                                        => New(transform(tpt))
      case Throw(expr)                                     => Throw(transform(expr))
      case Try(block, catches, finalizer)                  => Try(transform(block), catches.map(transform).asInstanceOf[List[CaseDef]], transform(finalizer))
      case Match(selector, cases)                          => Match(transform(selector), cases.map(transform).asInstanceOf[List[CaseDef]])
      case Return(expr)                                    => Return(transform(expr))
      case UnApply(fun, args)                              => UnApply(transform(fun), args.map(transform))
      case ModuleDef(mods, name, impl)                     => ModuleDef(mods, name, transform(impl).asInstanceOf[Template])
      case Template(parents, self, body)                   => Template(parents.map(transform), transform(self).asInstanceOf[ValDef], body.map(transform))
      case TypeDef(mods, name, tparams, rhs)               => TypeDef(mods, name, tparams.map(transform).asInstanceOf[List[TypeDef]], transform(rhs))
      case ClassDef(mods, name, tparams, impl)             => ClassDef(mods, name, tparams.map(transform).asInstanceOf[List[TypeDef]], transform(impl).asInstanceOf[Template])
      case RefTree(qualifier, name)                        => RefTree(transform(qualifier), name)
      case LabelDef(name, params, rhs)                     => LabelDef(name, params.map(transform).asInstanceOf[List[Ident]], transform(rhs))
      case t                                               => super.transform(t)
    }
  }

  final def apply[Obj: W](body: Tree): Tree = {
    val (self, rawBodyTree) = body match {
      case Function(List(ValDef(_, self, _, _)), Block(rawBodyTree, _)) => (self, rawBodyTree)
      case Function(List(ValDef(_, _, _, _)), Literal(Constant(())))    =>
        abort(s"Body of dynamic molecule can't be empty. Please define some val or def")
      case other                                                        =>
        abort(s"Unexpected body of dynamic molecule:\n$other\n" + showRaw(other))
    }

    val baseMolecule  = c.prefix.tree
    val bodyElements  = rawBodyTree.map(transformer.transform)
    var fieldClauses  = Seq.empty[Tree]
    var methodClauses = Seq.empty[Tree]

    val moleculeBody: List[Tree] = c.prefix.tree match {
      case Typed(Block(body, _), _)                                          => body
      case Apply(_, List(Select(This(TypeName("$anon")), TermName("conn")))) =>
        abort("Can't use input molecules as dynamic molecules")
      case other                                                             =>
        abort(s"Unexpected tree for dynamic molecule:\n$other\n" + showRaw(other))
    }

    val ClassDef(_, _, _, Template(_, _, List(constructor, row2tpl, row2obj)))                           = moleculeBody.last
    val DefDef(_, _, _, _, _, Block(List(ClassDef(_, _, _, Template(objTypes, _, objBodyElements))), _)) = row2obj

    val propTypes = objTypes.drop(2)
    var props     = List.empty[Tree]
    var refs      = List.empty[Tree]
    objBodyElements.tail.foreach {
      case ValDef(_, prop, tpt, _) =>
        props = props :+ q"final override lazy val $prop: $tpt = __obj.$prop"

      case DefDef(_, refName, _, _, refTpe, _) =>
        refs = refs :+ q"final override def $refName: $refTpe = __obj.$refName"

      case _ =>
    }

    rawBodyTree.foreach {
      case ValDef(_, TermName(field), _, _) =>
        fieldClauses = fieldClauses :+ cq"$field => ${TermName(field)}"

      case DefDef(_, TermName(method), _, Nil, _, _) =>
        fieldClauses = fieldClauses :+ cq"$method => ${TermName(method)}"

      case DefDef(_, TermName(method), _, List(Nil), _, _) =>
        methodClauses = methodClauses :+ cq"$method => ${TermName(method)}()"

      case DefDef(_, TermName(method), _, List(params), _, _) =>
        val args = params.zipWithIndex.map {
          case (ValDef(_, _, Ident(TypeName(_)), _), i) => q"args($i)"
          case (ValDef(_, _, tpt, _), i)                => q"args($i).asInstanceOf[$tpt]"
        }
        methodClauses = methodClauses :+ cq"$method => ${TermName(method)}(..$args)"

      case _ =>
    }

    val selectDynamic = if (fieldClauses.isEmpty) Nil else Seq(
      q"""
        override def selectDynamic(name: String): Any = name match {
          case ..$fieldClauses
          case other => throw new IllegalArgumentException("Please implement `" + other + "`.")
        }
       """)

    val applyDynamic = if (methodClauses.isEmpty) Nil else Seq(
      q"""
        override def applyDynamic(method: String)(args: Any*): Any = method match {
          case ..$methodClauses
          case other => throw new IllegalArgumentException("Please implement `" + other + "`.")
        }
       """)

    lazy val objBody =
      q"""
          val __obj = $baseMolecule.getObj
          val $self = __obj
          ..$props
          ..$refs
          ..$bodyElements
          ..${selectDynamic ++ applyDynamic}
         """

    lazy val t0 = propTypes match {
      case List(a)                                                                => q"new DynamicMolecule with Init with $a { ..$objBody }"
      case List(a, b)                                                             => q"new DynamicMolecule with Init with $a with $b { ..$objBody }"
      case List(a, b, c)                                                          => q"new DynamicMolecule with Init with $a with $b with $c { ..$objBody }"
      case List(a, b, c, d)                                                       => q"new DynamicMolecule with Init with $a with $b with $c with $d { ..$objBody }"
      case List(a, b, c, d, e)                                                    => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e { ..$objBody }"
      case List(a, b, c, d, e, f)                                                 => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f { ..$objBody }"
      case List(a, b, c, d, e, f, g)                                              => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g { ..$objBody }"
      case List(a, b, c, d, e, f, g, h)                                           => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i)                                        => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j)                                     => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k)                                  => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l)                               => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m)                            => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n)                         => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)                      => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p)                   => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q)                => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r)             => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s)          => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t)       => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u)    => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u { ..$objBody }"
      case List(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v) => q"new DynamicMolecule with Init with $a with $b with $c with $d with $e with $f with $g with $h with $i with $j with $k with $l with $m with $n with $o with $p with $q with $r with $s with $t with $u with $v { ..$objBody }"
      case list                                                                   => abort("Unexpected list of types:\n  " + list.mkString("\n  "))
    }

    val t =
      q"""
         import molecule.core.transform.DynamicMolecule
         import molecule.core.dsl.base.Init
         $t0
         """

    zz(1, t)
    t
  }
}
