package molecule.ast
import java.net.URI
import java.util.{Date, UUID}
import molecule.transform.Query2String
import molecule.util.Helpers

/** AST for molecule [[molecule.ast.query.Query Query]] representation.
  * <br><br>
  * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
  * <br><br>
  * Custom DSL molecule --> Model --> Query --> Datomic query string
  */
object query extends Helpers {

  trait QueryExpr

  /** Molecule Query representation.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    * <br><br>
    * Query is thus derived from [[molecule.ast.model.Model Model]] representation.
    *
    * @param f  Find parameters
    * @param wi With parameters
    * @param i  Input parameters
    * @param wh Where parameters: data clauses
    */
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
        "\n    List(" + (if (i.inputs.isEmpty) ")," else i.inputs.mkString("\n      ", ",\n      ", "),")) +
        "\n    List(" + (if (i.rules.isEmpty) ")," else i.rules.mkString("\n      ", ",\n      ", "),")) +
        "\n    List(" + (if (i.ds.size == 1) i.ds.head + "))," else i.ds.mkString("\n      ", ",\n      ", ")),"))
      s"""|Query(
          |  Find(List(
          |    ${f.outputs.mkString(sep)})),$widh$in
          |  Where(List(
          |    ${wh.clauses.mkString(sep)})))""".stripMargin
    }
  }

  def cast(value: Any): String = value match {
    case v: Long    => v + "L"
    case v: Float   => v + "f"
    case date: Date => "\"" + format(date) + "\""
    case v: String  => "\"" + v + "\""
    case v: UUID    => "\"" + v + "\""
    case v: URI     => "\"" + v + "\""
    case v          => v.toString
  }
  def o(opt: Option[String]): String = if (opt.isDefined) s"""Some("$opt")""" else "None"
  def seq[T](values: Seq[T]) = values.map {
    case seq: Seq[_] => seq.map(cast).mkString("Seq(", ", ", ")")
    case v           => cast(v)
  }.mkString("Seq(", ", ", ")")

  case class Find(outputs: Seq[Output]) extends QueryExpr
  case class With(variables: Seq[String]) extends QueryExpr
  case class In(inputs: Seq[Input], rules: Seq[Rule] = Seq(), ds: Seq[DataSource] = Seq(DS)) extends QueryExpr
  case class Where(clauses: Seq[Clause]) extends QueryExpr

  trait QueryTerm extends QueryExpr
  case object Empty extends QueryTerm

  trait Output extends QueryExpr
  case class AggrExpr(fn: String, args: Seq[Any], v: Var) extends Output {
    override def toString = s"""AggrExpr("$fn", ${seq(args)}, $v)"""
  }

  case class KW(ns: String, attr: String, refNs: String = "") extends QueryTerm {
    override def toString = s"""KW("$ns", "$attr", "$refNs")"""
  }

  sealed trait QueryValue extends QueryTerm

  case class Var(v: String) extends QueryValue with Output {
    override def toString = s"""Var("$v")"""
  }
  case class Val(v: Any) extends QueryValue with Output {
    override def toString = s"""Val(${cast(v)})"""
  }
  case class Pull(e: String, ns: String, attr: String, enumPrefix: Option[String] = None) extends QueryValue with Output {
    override def toString = s"""Pull("$e", "$ns", "$attr", ${o(enumPrefix)})"""
  }
  case object NoVal extends QueryValue with Output
  case object Dummy extends QueryValue


  sealed trait DataSource extends QueryTerm
  case class DS(name: String = "") extends DataSource {override def toString = s"""DS("$name")"""}
  case object DS extends DataSource
  case object ImplDS extends DataSource

  case class Rule(name: String, args: Seq[QueryValue], clauses: Seq[Clause]) extends QueryTerm {
    override def toString = s"""Rule("$name", ${seq(args)}, Seq(""" + clauses.mkString("\n        ", ",\n        ", "))")
  }

  trait Input extends QueryTerm
  case class InDataSource(ds: DataSource, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input {
    override def toString = s"""InDataSource($ds, ${seq(argss)})"""
  }
  case class InVar(binding: Binding, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input {
    override def toString = s"""InVar($binding, ${seq(argss)})"""
  }
  case class Placeholder(e: Var, kw: KW, v: Var, enumPrefix: Option[String] = None) extends Input {
    override def toString = s"""Placeholder($e, $kw, $v, ${o(enumPrefix)})"""
  }


  sealed trait Binding extends QueryTerm
  case object NoBinding extends Binding
  case class ScalarBinding(v: Var) extends Binding
  case class CollectionBinding(v: Var) extends Binding
  case class TupleBinding(vs: Seq[Var]) extends Binding
  case class RelationBinding(vs: Seq[Var]) extends Binding

  sealed trait Clause extends QueryExpr

  case class DataClause(ds: DataSource, e: Var, a: KW, v: QueryValue, tx: QueryTerm, op: QueryTerm = NoBinding) extends Clause {
    override def toString = s"""DataClause($ds, $e, $a, $v, $tx, $op)"""
  }
  case class NotClause(e: Var, a: KW) extends Clause {
    override def toString = s"""NotClause($e, $a)"""
  }
  case class NotClauses(clauses: Seq[Clause]) extends Clause {
    override def toString = "NotClauses(Seq(\n      " + clauses.mkString(",\n      ") + "))"

  }
  case class RuleInvocation(name: String, args: Seq[QueryTerm]) extends Clause {
    override def toString = s"""RuleInvocation("$name", ${seq(args)})"""
  }

  sealed trait ExpressionClause extends Clause
  case class Funct(name: String, ins: Seq[QueryTerm], outs: Binding) extends ExpressionClause {
    override def toString = s"""Funct("$name", ${seq(ins)}, $outs)"""
  }


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