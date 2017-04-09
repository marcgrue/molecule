package molecule.api

import molecule.ast.model.{Model, Or, TermValue}
import molecule.ast.query.{Placeholder, Query, Var}


trait InputMolecule {
  val _model: Model
  val _query: Query

  def resolveOr[I1](or: Or[I1]): Seq[I1] = {
    def traverse(expr: Or[I1]): Seq[I1] = expr match {
      case Or(TermValue(v1), TermValue(v2)) => Seq(v1, v2)
      case Or(or1: Or[I1], TermValue(v2))   => traverse(or1) :+ v2
      case Or(TermValue(v1), or2: Or[I1])   => v1 +: traverse(or2)
      case Or(or1: Or[I1], or2: Or[I1])     => traverse(or1) ++ traverse(or2)
      case _                                => sys.error(s"Unexpected expression: " + expr)
    }
    traverse(or)
  }

  def varsAndPrefixes(query: Query) = query.i.inputs.collect {
    case Placeholder(v, kw, enumPrefix, _) => (Var(v), enumPrefix.getOrElse(""))
  }
}