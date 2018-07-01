package molecule.ast
import molecule.transform.Query2String

private[molecule] object query {

  trait QueryExpr

  case class Query(f: Find, wi: With, i: In, wh: Where) extends QueryExpr {
    lazy val print = Query2String(this)
    def toList: String = print.toList
    def toMap: String = print.toMap
    def datalog(maxLength: Int = 30): String = print.multiLine(maxLength)
    def datalog: String = datalog(30)

    override def toString = {
      val sep = ",\n    "
      val widh = if (wi.variables.isEmpty) "" else wi.variables.mkString("\n  With(List(\n    ", sep, ")),")
      val in = if (i.inputs.isEmpty && i.rules.isEmpty) "" else "\n  In(" +
        "\n    List(" + (if(i.inputs.isEmpty) ")," else i.inputs.mkString("\n      ", "\n      ", "),")) +
        "\n    List(" + (if(i.rules.isEmpty) ")," else i.rules.mkString("\n      ", "\n      ", "),"))
      s"""|Query(
          |  Find(List(
          |    ${f.outputs.mkString(sep)})),$widh$in
          |  Where(List(
          |    ${wh.clauses.mkString(sep)})))""".stripMargin
    }
  }

  case class Find(outputs: Seq[Output]) extends QueryExpr
  case class With(variables: Seq[String]) extends QueryExpr
  case class In(inputs: Seq[Input], rules: Seq[Rule] = Seq(), ds: Seq[DataSource] = Seq(DS)) extends QueryExpr
  case class Where(clauses: Seq[Clause]) extends QueryExpr

  trait QueryTerm extends QueryExpr
  case object Empty extends QueryTerm

  trait Output extends QueryExpr
  case class AggrExpr(fn: String, args: Seq[Any], v: Var) extends Output

  case class KW(ns: String, attr: String, refNs: String = "") extends QueryTerm

  sealed trait QueryValue extends QueryTerm

  case class Var(v: String) extends QueryValue with Output
  case class Val(v: Any) extends QueryValue with Output
  case class Pull(e: String, ns: String, attr: String, enumPrefix: Option[String] = None) extends QueryValue with Output
  case object NoVal extends QueryValue with Output
  case object Dummy extends QueryValue


  sealed trait DataSource extends QueryTerm
  case class DS(name: String = "") extends DataSource
  case object DS extends DataSource
  case object ImplDS extends DataSource

  case class Rule(name: String, args: Seq[QueryValue], clauses: Seq[Clause]) extends QueryTerm

  trait Input extends QueryTerm
  case class InDataSource(ds: DataSource, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input
  case class InVar(binding: Binding, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input
  case class Placeholder(v: String, kw: KW, enumPrefix: Option[String] = None, e: String = "") extends Input


  sealed trait Binding extends QueryTerm
  case object NoBinding extends Binding
  case class ScalarBinding(v: Var) extends Binding
  case class CollectionBinding(v: Var) extends Binding
  case class TupleBinding(vs: Seq[Var]) extends Binding
  case class RelationBinding(vs: Seq[Var]) extends Binding

  sealed trait Clause extends QueryExpr

  case class DataClause(ds: DataSource, e: Var, a: KW, v: QueryValue, tx: QueryTerm, op: QueryTerm = NoBinding) extends Clause
  case class NotClause(ds: DataSource, e: Var, a: KW) extends Clause
  case class RuleInvocation(name: String, args: Seq[QueryTerm]) extends Clause

  sealed trait ExpressionClause extends Clause
//  case class Predicate(name: String, args: Seq[QueryTerm]) extends ExpressionClause
  case class Funct(name: String, ins: Seq[QueryTerm], outs: Binding) extends ExpressionClause


  // Convenience constructors (for tests mainly) ........................................

  object Query {
    def apply(find: Find, where: Where) = new Query(find, With(Seq()), In(Seq()), where)
    def apply(find: Find, in: In, where: Where) = new Query(find, With(Seq()), in, where)
    def apply(find: Find) = new Query(find, With(Seq()), In(Seq()), Where(List()))
    def apply() = new Query(Find(Seq()), With(Seq()), In(Seq()), Where(List()))
  }
  object DataClause {
    def apply(e: String, attr: KW, v: String) = new DataClause(ImplDS, Var(e), attr, Var(v), Empty)
    def apply(e: String, attr: KW, value: Val) = new DataClause(ImplDS, Var(e), attr, value, Empty)
  }
}