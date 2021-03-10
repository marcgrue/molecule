package molecule.core.macros

import scala.reflect.macros.blackbox

trait TreeTransformer {
  val c: blackbox.Context

  import c.universe._

  object transformer extends Transformer {

    // Recursively ensure that all Ident's are explicit - necessary when working with dynamic code
    // Note that only expected extractors are used. So, some very specialized use case might complain at some point...
    override def transform(tree: Tree): Tree = tree match {
      case Ident(TermName(n))                              => Ident(TermName(n))
      case DefDef(mods, name, tparams, vparamss, tpt, rhs) => DefDef(mods, name, tparams, vparamss.map(params => params.map(transform).asInstanceOf[List[ValDef]]), transform(tpt), transform(rhs))
      case ValDef(mods, name, tpt, rhs)                    => ValDef(mods, name, transform(tpt), transform(rhs))
      case Select(qualifier, name)                         => Select(transform(qualifier), name)
      case Apply(fun, args)                                => Apply(transform(fun), args.map(transform))
      case TypeApply(fun, args)                            => TypeApply(transform(fun), args.map(transform))
      case AssignOrNamedArg(lhs, rhs)                      => AssignOrNamedArg(transform(lhs), transform(rhs))
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
}