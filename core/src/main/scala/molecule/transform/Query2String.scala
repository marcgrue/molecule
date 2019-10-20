package molecule.transform
import java.util.{Date, UUID}
import java.net.URI
import molecule.ast.query._
import molecule.transform.exception.Query2StringException
import molecule.util.Helpers
import scala.language.implicitConversions


/** Query to Datomic query string transformation.
  * <br><br>
  * Third transformation in Molecules series of transformations from
  * custom boilerplate DSL constructs to Datomic queries:
  * <br><br>
  * Custom DSL molecule --> Model --> Query --> Datomic query string
  *
  * @see [[http://www.scalamolecule.org/dev/transformation/]]
  * */
case class Query2String(q: Query) extends Helpers {

  // Ugly convenience hack to switch BigInt representation
  var asN = false

  def p(expr: QueryExpr): String = expr match {
    case Query(find, widh, in, where)                     => pp(find, widh, in, where)
    case Find(outputs)                                    => ":find  " + (outputs map p mkString " ")
    case With(variables)                                  => if (variables.isEmpty) "" else ":with  " + (variables map s mkString " ")
    case in@In(_, _, _)                                   => mkIn(in, false)
    case Where(clauses)                                   => ":where " + (clauses map p mkString " ")
    case KW("?", attr, _)                                 => s"?$attr"
    case KW(nsFull, "?", _)                               => s"?$nsFull"
    case KW(nsFull, attr, _)                              => s":$nsFull/$attr"
    case AggrExpr(fn, args, v)                            => s"($fn " + ((args :+ p(v)) mkString " ") + ")"
    case Var("?")                                         => "?"
    case Var("_")                                         => "_"
    case Var(eid) if eid matches """\d+"""                => eid
    case Var(v)                                           => "?" + v
    case Val(v: Int)                                      => v.toString
    case Val(v: Long)                                     => v.toString
    case Val(v: Float)                                    => v.toString
    case Val(v: Double)                                   => v.toString
    case Val(v: Boolean)                                  => v.toString
    case Val(v: BigInt) if asN                            => v.toString + "N"
    case Val(v: BigInt)                                   => s"(biginteger $v)"
    case Val(v: BigDecimal) if v.toString().contains(".") => v.toString + "M"
    case Val(v: BigDecimal)                               => v.toString + ".0M"
    case Val(date: Date)                                  => "#inst \"" + date2datomicStr(date) + "\""
    case Val(v: UUID)                                     => "#uuid \"" + v + "\""
    case Val(v)                                           => "\"" + v + "\""
    case Pull(e, nsFull, attr, Some(prefix))              => s"(pull ?$e [{:$nsFull/$attr [:db/ident]}])"
    case Pull(e, nsFull, attr, _)                         => s"(pull ?$e [:$nsFull/$attr])"
    case NoVal                                            => ""
    case DS(name)                                         => "$" + name
    case DS                                               => "$"
    case Empty                                            => ""
    case ImplDS                                           => ""
    case InDataSource(ds, _)                              => p(ds)
    case InVar(binding, _)                                => p(binding)
    case Placeholder(_, _, Var(v), _)                     => "?" + v
    case NoBinding                                        => ""
    case ScalarBinding(v)                                 => p(v)
    case CollectionBinding(v)                             => "[" + p(v) + " ...]"
    case TupleBinding(vs)                                 => "[ " + (vs map p mkString " ") + " ]"
    case RelationBinding(vs)                              => "[[ " + (vs map p mkString " ") + " ]]"
    case DataClause(ds, e, a, v@Val(bi: BigInt), tx, op)  => asN = true; val dc = pp(ds, e, a, v, tx, op); asN = false; dc
    case DataClause(ds, e, a, v, tx, op)                  => pp(ds, e, a, v, tx, op)
    case NotClause(e, a)                                  => s"(not [" + p(e) + " " + p(a) + "])"
    case NotClauses(cls)                                  => s"(not " + (cls map p mkString " ") + ")"
    case NotJoinClauses(vars, cls)                        => s"(not-join [" + (vars map p mkString " ") + "]\n          " + (cls map p mkString "\n          ") + ")"
    case Funct(name, ins, outs)                           => ((s"[($name " + (ins map p mkString " ")).trim + ") " + p(outs)).trim + "]"
    case RuleInvocation(name, args)                       => s"($name " + (args map p mkString " ") + ")"
    case Rule(name, args, clauses) if clauses.size > 1    => asN = true; val rc = clauses map p mkString "\n   "; asN = false; s"[($name " + (args map p mkString " ") + ")\n   " + rc + "]"
    case Rule(name, args, clauses)                        => asN = true; val rc = clauses map p mkString " "; asN = false; s"[($name " + (args map p mkString " ") + ") " + rc + "]"
    case unresolvedQuery                                  => throw new Query2StringException(s"\nUNRESOLVED query expression: $unresolvedQuery")
  }

  def pp(es: QueryExpr*): String = es.toList.map(p).filter(_.trim.nonEmpty).mkString("[", " ", "]")

  def s(str: String) = "?" + str

  def mkIn(in: In, bracket: Boolean) = {
    val (l, r) = if (bracket) (":in    [ ", " ]") else (":in    ", "")
    if (in.inputs.isEmpty && in.rules.isEmpty) ""
    else
      l + ((in.ds map p mkString("", " ", " "))
        + (if (in.rules.isEmpty) "" else "% ")
        + (in.inputs map p mkString " ")).trim + r
  }

  def toList: String = p(q)

  def toMap: String = {
    s"""{ :find [ ${q.f.outputs map p mkString " "} ]""" +
      (if (q.wi.variables.isEmpty) "" else s" :with [ ${q.wi.variables map s mkString " "} ]") +
      mkIn(q.i, true) +
      s""" :where [ ${q.wh.clauses map p mkString " "} ] }"""
  }

  def multiLine(maxLength: Int = 30): String = {
    val queryString         = p(q)
    val (firstParts, where) = (List(p(q.f), p(q.wi), p(q.i)).filter(_.trim.nonEmpty), p(q.wh))
    if (queryString.length > maxLength) {
      firstParts.mkString("[", "\n ", "\n ") +
        (if (where.length > maxLength)
          q.wh.clauses.map(p).mkString(":where ", "\n        ", "")
        else where) + "]"
    } else {
      queryString
    }
  }
}