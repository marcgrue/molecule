package molecule.core.ast

import java.util.Date
import molecule.core.ast.exception.ModelException
import molecule.core.util.Helpers

/** AST for molecule Model representation.
  * <br><br>
  * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
  * <br><br>
  * Custom DSL molecule --> Model --> Query --> Datomic query string
  * */
object elements extends Helpers {

  /** Molecule Model representation.
    * <br><br>
    * Molecule transforms custom boilerplate DSL constructs to Datomic queries in 3 steps:
    * <br><br>
    * Custom DSL molecule --> Model --> Query --> Datomic query string
    * <br><br>
    * Model is thus derived from custom meta-DSL constructs ("molecules").
    *
    * @param elements Elements of the model
    */
  case class Model(elements: Seq[Element]) {
    override def toString: String = {
      def draw(elements: Seq[Element], indent: Int): Seq[String] = {
        val s = "  " * indent
        elements map {
          case Nested(bond, nestedElements) =>
            s"""|Nested(
                |$s  $bond,
                |$s  List(
                |$s    ${draw(nestedElements, indent + 2).mkString(s",\n$s  ")}))""".stripMargin
          case TxMetaData(nestedElements)   =>
            s"""|TxMetaData(List(
                |$s  ${draw(nestedElements, indent + 1).mkString(s",\n$s  ")}))""".stripMargin
          case Composite(elements)          =>
            s"""|Composite(List(
                |$s  ${draw(elements, indent + 1).mkString(s",\n$s  ")}))""".stripMargin
          case other                        => s"$other"
        }
      }
      "Model(List(\n  " + draw(elements, 1).mkString(",\n  ") + "))"
    }
  }

  trait Element

  trait GenericAtom extends Element {
    val nsFull: String
    val attr  : String
    val tpe   : String
    val value : Value
  }

  case class Generic(
    nsFull: String,
    attr: String,
    tpe: String,
    value: Value) extends GenericAtom {
    override def toString: String = s"""Generic("$nsFull", "$attr", "$tpe", $value)"""
  }

  case class Atom(
    nsFull: String,
    attr: String,
    tpe: String,
    card: Int,
    value: Value,
    enumPrefix: Option[String] = None,
    gvs: Seq[GenericValue] = Nil,
    keys: Seq[String] = Nil) extends GenericAtom {
    override def toString: String =
      s"""Atom("$nsFull", "$attr", "$tpe", $card, ${tv(tpe, value)}, ${o(enumPrefix)}, ${seq(gvs)}, ${seq(keys)})"""
  }

  case class Bond(
    nsFull: String,
    refAttr: String,
    refNs: String = "",
    card: Int,
    gvs: Seq[GenericValue] = Nil) extends Element {
    override def toString: String = s"""Bond("$nsFull", "$refAttr", "$refNs", $card, ${seq(gvs)})"""
  }

  case class ReBond(backRef: String) extends Element {
    override def toString: String = s"""ReBond("$backRef")"""
  }

  case class Nested(bond: Bond, elements: Seq[Element]) extends Element

  case class TxMetaData(elements: Seq[Element]) extends Element
  case class Composite(elements: Seq[Element]) extends Element
  case object Self extends Element
  case object EmptyElement extends Element


  sealed trait Value

  // Value
  case object EntValue extends Value
  case object VarValue extends Value
  case class BackValue(backNs: String) extends Value {override def toString: String = s"""BackValue("$backNs")"""}
  case object EnumVal extends Value
  case object IndexVal extends Value

  // Function
  case class Fulltext(search: Seq[Any]) extends Value
  case class Fn(name: String, value: Option[Int] = None) extends Value {override def toString: String = s"""Fn("$name", ${o(value)})"""}

  // Logic
  case class And(values: Seq[Any]) extends Value {override def toString: String = s"And(${seq(values)})"}

  // Comparison (== != < > <= >=)
  case class Eq(values: Seq[Any]) extends Value {override def toString: String = s"Eq(${seq(values)})"}
  case class Neq(values: Seq[Any]) extends Value {override def toString: String = s"Neq(${seq(values)})"}
  case class Lt(value: Any) extends Value {override def toString: String = s"Lt(${cast(value)})"}
  case class Gt(value: Any) extends Value {override def toString: String = s"Gt(${cast(value)})"}
  case class Le(value: Any) extends Value {override def toString: String = s"Le(${cast(value)})"}
  case class Ge(value: Any) extends Value {override def toString: String = s"Ge(${cast(value)})"}

  // Question mark placeholder for input molecules
  case object Qm extends Value
  case object Distinct extends Value

  // Card-many attribute operations
  case class AssertValue(values: Seq[Any]) extends Value {override def toString: String = s"AssertValue(${seq(values)})"}
  case class ReplaceValue(oldNew: Seq[(Any, Any)]) extends Value {override def toString: String = s"ReplaceValue(${seq(oldNew)})"}
  case class RetractValue(values: Seq[Any]) extends Value {override def toString: String = s"RetractValue(${seq(values)})"}

  // Map attribute operations
  case class AssertMapPairs(pairs: Seq[(String, Any)]) extends Value {override def toString: String = s"AssertMapPairs(${seq(pairs)})"}
  case class ReplaceMapPairs(pairs: Seq[(String, Any)]) extends Value {override def toString: String = s"ReplaceMapPairs(${seq(pairs)})"}
  case class RetractMapKeys(keys: Seq[String]) extends Value {override def toString: String = s"RetractMapKeys(${seq(keys)})"}
  case class MapEq(pairs: Seq[(String, Any)]) extends Value {override def toString: String = s"MapEq(${seq(pairs)})"}
  case class MapKeys(keys: Seq[String]) extends Value {override def toString: String = s"MapKeys(${seq(keys)})"}


  sealed trait GenericValue extends Value

  case object NoValue extends GenericValue
  case class Id(eid: Any) extends GenericValue
  case class Card(card: Int) extends GenericValue {override def toString: String = s"Card($card)"}


  sealed trait Bidirectional extends GenericValue

  case class BiSelfRef(card: Int) extends Bidirectional {override def toString: String = s"BiSelfRef($card)"}
  case class BiSelfRefAttr(card: Int) extends Bidirectional {override def toString: String = s"BiSelfRefAttr($card)"}

  case class BiOtherRef(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiOtherRef($card, "$attr")"""}
  case class BiOtherRefAttr(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiOtherRefAttr($card, "$attr")"""}

  case object BiEdge extends Bidirectional
  case class BiEdgeRef(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiEdgeRef($card, "$attr")"""}
  case class BiEdgeRefAttr(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiEdgeRefAttr($card, "$attr")"""}

  case class BiEdgePropAttr(card: Int) extends Bidirectional {override def toString: String = s"BiEdgePropAttr($card)"}
  case class BiEdgePropRefAttr(card: Int) extends Bidirectional {override def toString: String = s"BiEdgePropRefAttr($card)"}
  case class BiEdgePropRef(card: Int) extends Bidirectional {override def toString: String = s"BiEdgePropRef($card)"}

  case class BiTargetRef(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiTargetRef($card, "$attr")"""}
  case class BiTargetRefAttr(card: Int, attr: String) extends Bidirectional {override def toString: String = s"""BiTargetRefAttr($card, "$attr")"""}


  /** Expression AST for building OR/AND expressions.
    * {{{
    *   // `or` method allows OR-logic to be applied to `name` attribute
    *   Person.name_("Ben" or "Liz").age.get === List(42, 37)
    *
    *   // Given an input molecule awaiting 2 inputs, we can apply AND-pairs to OR expression:
    *   val persons = m(Person.name_(?).age(?))
    *   persons(("Ben" and 42) or ("Liz" and 37)).get === List(42, 37)
    * }}}
    */
  trait Expression

  trait Exp1[T1] extends Expression {
    def or(b: Exp1[T1]): Or[T1] = Or(this, b)
    def and[T2](b: Exp1[T2]): And2[T1, T2] = And2(this, b)
  }
  case class TermValue[T1](v: T1) extends Exp1[T1]
  case class Not[T1](e: Exp1[T1]) extends Exp1[T1]
  case class Or[T1](e1: Exp1[T1], e2: Exp1[T1]) extends Exp1[T1]

  trait Exp2[T1, T2] extends Expression
  case class And2[T1, T2](e1: Exp1[T1], e2: Exp1[T2]) extends Exp2[T1, T2] {
    def and[T3](e3: Exp1[T3]): And3[T1, T2, T3] = And3(e1, e2, e3)
    def or(that: And2[T1, T2]): Or2[T1, T2] = Or2(this, that)
  }
  case class Or2[T1, T2](e1: Exp2[T1, T2], e2: Exp2[T1, T2]) extends Exp2[T1, T2] {
    def or(e3: Exp2[T1, T2]): Or2[T1, T2] = Or2(e1, Or2(e2, e3))
  }

  trait Exp3[T1, T2, T3] extends Expression
  case class And3[T1, T2, T3](e1: Exp1[T1], e2: Exp1[T2], e3: Exp1[T3]) extends Exp3[T1, T2, T3] {
    def or(that: And3[T1, T2, T3]): Or3[T1, T2, T3] = Or3(this, this, that) // todo: how to nest properly without duplicating?
  }
  case class Or3[T1, T2, T3](e1: Exp3[T1, T2, T3], e2: Exp3[T1, T2, T3], e3: Exp3[T1, T2, T3]) extends Exp3[T1, T2, T3] {
    def or(e4: Exp3[T1, T2, T3]): Or3[T1, T2, T3] = Or3(e1, e2, Or3(e3, e3, e4)) // todo: how to nest properly without duplicating?
  }


  def curNs(e: Element): String = e match {
    case Atom(nsFull, _, _, _, _, _, _, _)   => nsFull
    case Bond(nsFull, _, _, _, _)            => nsFull
    case Nested(Bond(nsFull, _, _, _, _), _) => nsFull
    case Generic(nsFull, _, _, _)            => nsFull
    case unexpected                          =>
      throw new ModelException("Unexpected element: " + unexpected)
  }


  // Correct output of values given scala-js type mixtures (+ hack for javascript decimal handling)

  final def cast2(tpe: String, value: Any): String = (tpe, value) match {
    case ("Long", v)                                               => v.toString + "L"
    case ("Float" | "Double", v) if v.toString.startsWith("__n__") => v.toString.drop(5)
    case ("Float" | "Double", v)                                   => v.toString
    case ("Date", date: Date)                                      => "\"" + date2str(date) + "\""
    case ("String", s: String)                                     => "\"" + escStr(s) + "\""
    case ("UUID" | "URI", v)                                       => "\"" + v + "\""
    case (_, v)                                                    => v.toString
  }

  final def getSeq2[T](tpe: String, values: Seq[T]): String =
    values.map {
      case set: Set[_] => set.map(cast2(tpe, _)).mkString("Set(", ", ", ")")
      case seq: Seq[_] => seq.map(cast2(tpe, _)).mkString("Seq(", ", ", ")")
      case (a, b)      => s"${cast2(tpe, a)} -> ${cast2(tpe, b)}"
      case v           => cast2(tpe, v)
    }.mkString("Seq(", ", ", ")")

  final protected def tv(tpe: String, value: Value): String = {
    value match {
      case VarValue                              => "VarValue"
      case Eq(Nil)                               => s"Eq(Seq())"
      case Eq((vs: Seq[_]) :: Nil) if vs.isEmpty => s"Eq(${getSeq2(tpe, vs)})"
      case Eq((vs: Seq[_]) :: Nil)               => s"Eq(${getSeq2(tpe, vs)})"
      case Eq(vs)                                => s"Eq(${getSeq2(tpe, vs)})"
      case Neq(vs)                               => s"Neq(${getSeq2(tpe, vs)})"
      case Gt(v)                                 => s"Gt(${cast2(tpe, v)})"
      case Ge(v)                                 => s"Ge(${cast2(tpe, v)})"
      case Lt(v)                                 => s"Lt(${cast2(tpe, v)})"
      case Le(v)                                 => s"Le(${cast2(tpe, v)})"
      case other                                 => other.toString
    }
  }
}

