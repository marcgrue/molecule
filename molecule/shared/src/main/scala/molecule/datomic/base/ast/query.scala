package molecule.datomic.base.ast
import molecule.core.ast.elements.Model
import molecule.datomic.base.transform.Query2String
import molecule.core.util.Helpers

/** AST for molecule [[molecule.datomic.base.ast.query.Query Query]] representation.
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
    * Query is thus derived from [[Model Model]] representation.
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
    def rules: String = if (i.rules.isEmpty) "none\n\n" else "[\n " + i.rules.map(Query2String(this).p(_)).mkString("\n ") + "\n]"
    def inspect: String =
      s"""$datalog
         |
         |RULES: $rules
         |""".stripMargin

    override def toString: String = {
      val sep  = ",\n    "
      val widh = if (wi.variables.isEmpty) "" else wi.variables.mkString("\n  With(List(\n    ", sep, ")),")
      val in   = if (i.inputs.isEmpty && i.rules.isEmpty) "" else "\n  In(" +
        "\n    List(" + (if (i.inputs.isEmpty) ")," else i.inputs.mkString("\n      ", ",\n      ", "),")) +
        "\n    List(" + (if (i.rules.isEmpty) ")," else i.rules.mkString("\n      ", ",\n      ", "),")) +
        "\n    List(" + (if (i.ds.size == 1) i.ds.head.toString + "))," else i.ds.mkString("\n      ", ",\n      ", ")),"))
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

  sealed trait Output extends QueryExpr
  case class AggrExpr(fn: String, args: Seq[Any], v: Var) extends Output {
    override def toString: String = s"""AggrExpr("$fn", ${seq(args)}, $v)"""
  }

  sealed trait QueryValue extends QueryTerm

  case class KW(nsFull: String, attr: String, refNs: String = "") extends QueryValue {
    override def toString: String = s"""KW("$nsFull", "$attr", "$refNs")"""
  }
  case class Var(v: String) extends QueryValue with Output {
    override def toString: String = s"""Var("$v")"""
  }
  case class Val(v: Any) extends QueryValue with Output {
    override def toString: String = s"""Val(${cast(v)})"""
  }

  sealed trait PullAttrSpec extends QueryValue with Output
  case class PullAttr(nsFull: String, attr: String, opt: Boolean) extends PullAttrSpec {
    override def toString: String = s"""PullAttr("$nsFull", "$attr", $opt)"""
  }
  case class PullEnum(nsFull: String, attr: String, opt: Boolean) extends PullAttrSpec {
    override def toString: String = s"""PullEnum("$nsFull", "$attr", $opt)"""
  }
  case class NestedAttrs(
    level: Int,
    nsFull: String,
    attr: String,
    attrSpecs: Seq[PullAttrSpec]
  ) extends PullAttrSpec {
    override def toString: String = {
      def draw(elements: Seq[PullAttrSpec], indent: Int): Seq[String] = {
        val s = "  " * indent
        elements map {
          case NestedAttrs(level, nsFull, attr, attrSpecs) =>
            s"""$s  NestedAttrs($level, "$nsFull", "$attr", Seq(""" + "\n" +
              draw(attrSpecs, indent + 1).mkString(s",\n") + "))"
          case other                                =>
            s"$s  $other"
        }
      }
      s"""NestedAttrs($level, "$nsFull", "$attr", Seq(""" + "\n" + draw(attrSpecs, 3).mkString(",\n") + "))"
    }
  }

  case class PullNested(e: String, nestedAttrs: NestedAttrs) extends QueryValue with Output {
    override def toString: String = s"""PullNested("$e",""" + "\n      " + nestedAttrs + ")"
  }
  case class Pull(e: String, nsFull: String, attr: String, enumPrefix: Option[String] = None) extends QueryValue with Output {
    override def toString: String = s"""Pull("$e", "$nsFull", "$attr", ${o(enumPrefix)})"""
  }

  case object NoVal extends QueryValue with Output


  sealed trait DataSource extends QueryTerm
  case class DS(name: String = "") extends DataSource {override def toString = s"""DS("$name")"""}
  case object DS extends DataSource
  case object ImplDS extends DataSource

  case class Rule(name: String, args: Seq[QueryValue], clauses: Seq[Clause]) extends QueryTerm {
    override def toString: String = s"""Rule("$name", ${seq(args)}, Seq(""" + clauses.mkString("\n        ", ",\n        ", "))")
  }

  trait Input extends QueryTerm
  case class InDataSource(ds: DataSource, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input {
    override def toString: String = s"""InDataSource($ds, ${seq(argss)})"""
  }
  case class InVar(binding: Binding, argss: Seq[Seq[Any]] = Seq(Seq())) extends Input {
    override def toString: String = s"""InVar($binding, ${seq(argss)})"""
  }
  case class Placeholder(e: Var, kw: KW, v: Var, enumPrefix: Option[String] = None) extends Input {
    override def toString: String = s"""Placeholder($e, $kw, $v, ${o(enumPrefix)})"""
  }


  sealed trait Binding extends QueryTerm
  case object NoBinding extends Binding
  case class ScalarBinding(v: Var) extends Binding
  case class CollectionBinding(v: Var) extends Binding
  case class TupleBinding(vs: Seq[Var]) extends Binding
  case class RelationBinding(vs: Seq[Var]) extends Binding

  sealed trait Clause extends QueryExpr

  case class DataClause(ds: DataSource, e: QueryValue, a: KW, v: QueryValue, tx: QueryTerm, op: QueryTerm = NoBinding) extends Clause {
    override def toString: String = s"""DataClause($ds, $e, $a, $v, $tx, $op)"""
  }
  case class NotClause(e: Var, a: KW) extends Clause {
    override def toString: String = s"""NotClause($e, $a)"""
  }
  case class NotClauses(clauses: Seq[Clause]) extends Clause {
    override def toString: String = "NotClauses(Seq(\n      " + clauses.mkString(",\n      ") + "))"
  }
  case class NotJoinClauses(nonUnifyingVars: Seq[Var], clauses: Seq[Clause]) extends Clause {
    override def toString: String = "NotJoinClauses(Seq(" + nonUnifyingVars.mkString(", ") + "), Seq(\n      " + clauses.mkString(",\n      ") + "))"
  }
  case class RuleInvocation(name: String, args: Seq[QueryTerm]) extends Clause {
    override def toString: String = s"""RuleInvocation("$name", ${seq(args)})"""
  }

  sealed trait ExpressionClause extends Clause
  case class Funct(name: String, ins: Seq[QueryTerm], outs: Binding) extends ExpressionClause {
    override def toString: String =
      if (name.contains("\""))
        s"""Funct(\"\"\"$name\"\"\", ${seq(ins)}, $outs)"""
      else
        s"""Funct("$name", ${seq(ins)}, $outs)"""
  }


  // Convenience constructors (for tests mainly) ........................................

  object Query {
    def apply(find: Find, where: Where): Query = new Query(find, With(Seq()), In(Seq()), where)
    def apply(find: Find, in: In, where: Where): Query = new Query(find, With(Seq()), in, where)
    def apply(find: Find): Query = new Query(find, With(Seq()), In(Seq()), Where(List()))
    def apply(): Query = new Query(Find(Seq()), With(Seq()), In(Seq()), Where(List()))
  }
  object DataClause {
    def apply(e: String, attr: KW, v: String): DataClause = new DataClause(ImplDS, Var(e), attr, Var(v), Empty)
    def apply(e: String, attr: KW, value: Val): DataClause = new DataClause(ImplDS, Var(e), attr, value, Empty)
  }
}