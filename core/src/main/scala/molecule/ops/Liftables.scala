package molecule.ops

import java.net.URI
import java.util.{Date, UUID}
import molecule.ast.model._
import molecule.ast.query._
import molecule.ops.exception.LiftablesException
import molecule.transform.exception.Dsl2ModelException
import molecule.util.MacroHelpers
import scala.collection.immutable.HashSet
import scala.collection.immutable.Set.{Set1, Set2, Set3, Set4}
import scala.reflect.api.Trees
import scala.reflect.macros.blackbox

private[molecule] trait Liftables extends MacroHelpers {
  val c: blackbox.Context
  import c.universe._

  def abort(msg: String) = throw new LiftablesException(msg)

  // General liftables --------------------------------------------------------------

  def mkDate(date: Date): c.universe.Tree = q"new Date(${date.getTime})"
  def mkBigInt(bigInt: BigInt): c.universe.Tree = q"BigInt.apply(${bigInt.toString()})"
  def mkBigDecimal(bigDec: BigDecimal): c.universe.Tree = q"BigDecimal.apply(${bigDec.toString()})"
  def mkUUID(uuid: UUID): c.universe.Tree = q"java.util.UUID.fromString(${uuid.toString})"
  def mkURI(uri: URI): c.universe.Tree = q"new java.net.URI(${uri.getScheme}, ${uri.getUserInfo}, ${uri.getHost}, ${uri.getPort}, ${uri.getPath}, ${uri.getQuery}, ${uri.getFragment})"

  implicit val liftAny: c.universe.Liftable[Any] = Liftable[Any] {
    case Literal(Constant(s: String))  => q"$s"
    case Literal(Constant(i: Int))     => q"$i"
    case Literal(Constant(l: Long))    => q"$l"
    case Literal(Constant(f: Float))   => q"$f"
    case Literal(Constant(d: Double))  => q"$d"
    case Literal(Constant(b: Boolean)) => q"$b"
    case s: String                     => q"$s"
    case i: Int                        => q"$i"
    case l: Long                       => q"$l"
    case f: Float                      => q"$f"
    case d: Double                     => q"$d"
    case b: Boolean                    => q"$b"
    case date: Date                    => mkDate(date)
    case bigInt: BigInt                => mkBigInt(bigInt)
    case bigDec: BigDecimal            => mkBigDecimal(bigDec)
    case uuid: UUID                    => mkUUID(uuid)
    case uri: URI                      => mkURI(uri)
    case qm: Qm.type                   => q"Qm"
    case maybe: Distinct.type          => q"Distinct"
    case entValue: EntValue.type       => q"EntValue"
    case varValue: VarValue.type       => q"VarValue"
    case set: Set[_]                   => set match {
      case s1: Set1[_]   => q"Set(${any(s1.head)})"
      case s2: Set2[_]   => q"Set(..${s2 map any})"
      case s3: Set3[_]   => q"Set(..${s3 map any})"
      case s4: Set4[_]   => q"Set(..${s4 map any})"
      case s: HashSet[_] => q"Set(..${s map any})"
      case emptySet      => q"Set()"
    }
    case q"scala.None"                 => q"None"
    case null                          => q"null"
    case other                         =>
      abort("Can't lift unexpected Any type: " + other.getClass +
        "\nMaybe you are applying some Scala expression to a molecule attribute?" +
        "\nTry to assign the expression to a variable and apply the variable instead.")
  }

  def any(v: Any): c.universe.Tree = v match {
    case Literal(Constant(s: String))  => q"$s"
    case Literal(Constant(i: Int))     => q"$i"
    case Literal(Constant(l: Long))    => q"$l"
    case Literal(Constant(f: Float))   => q"$f"
    case Literal(Constant(d: Double))  => q"$d"
    case Literal(Constant(b: Boolean)) => q"$b"
    case s: String                     => q"$s"
    case i: Int                        => q"$i"
    case l: Long                       => q"$l"
    case f: Float                      => q"$f"
    case d: Double                     => q"$d"
    case b: Boolean                    => q"$b"
    case date: Date                    => mkDate(date)
    case bigInt: BigInt                => mkBigInt(bigInt)
    case bigDec: BigDecimal            => mkBigDecimal(bigDec)
    case uuid: UUID                    => mkUUID(uuid)
    case uri: URI                      => mkURI(uri)
  }

  implicit val liftTuple2: c.universe.Liftable[Product] = Liftable[Product] {
    case (k: String, v: String)         => q"($k, $v)"
    case (k: Int, v: Int)               => q"($k, $v)"
    case (k: Long, v: Long)             => q"($k, $v)"
    case (k: Float, v: Float)           => q"($k, $v)"
    case (k: Double, v: Double)         => q"($k, $v)"
    case (k: Date, v: Date)             => q"(${mkDate(k)}, ${mkDate(v)})"
    case (k: BigInt, v: BigInt)         => q"(${mkBigInt(k)}, ${mkBigInt(v)})"
    case (k: BigDecimal, v: BigDecimal) => q"(${mkBigDecimal(k)}, ${mkBigDecimal(v)})"
    case (k: UUID, v: UUID)             => q"(${mkUUID(k)}, ${mkUUID(v)})"
    case (k: URI, v: URI)               => q"(${mkURI(k)}, ${mkURI(v)})"
    case (a, b)                         => abort(s"Can't lift unexpected Tuple2: ($a, $b)")
    case other                          => abort(s"Can't lift unexpected product type: $other")
  }


  // Liftables for Query --------------------------------------------------------------

  implicit val liftVar   : c.universe.Liftable[Var]  = Liftable[Var] { v => q"Var(${v.v})" }
  implicit val liftVal   : c.universe.Liftable[Val]  = Liftable[Val] { value => q"Val(${value.v})" }
  implicit val liftAttrKW: c.universe.Liftable[KW]   = Liftable[KW] { kw => q"KW(${kw.ns}, ${kw.attr}, ${kw.refNs})" }
  implicit val liftWith  : c.universe.Liftable[With] = Liftable[With] { widh => q"With(Seq(..${widh.variables}))" }

  implicit val liftQueryValue: c.universe.Liftable[QueryValue] = Liftable[QueryValue] {
    case KW(ns, attr, refNs)           => q"KW($ns, $attr, $refNs)"
    case Var(sym)                      => q"Var($sym)"
    case Val(v)                        => q"Val($v)"
    case Pull(e, ns, attr, enumPrefix) => q"Pull($e, $ns, $attr, $enumPrefix)"
    case NoVal                         => q"NoVal"
  }

  implicit val liftDataSource: c.universe.Liftable[DataSource] = Liftable[DataSource] {
    case DS(name) => q"DS($name)"
    case DS       => q"DS"
    case ImplDS   => q"ImplDS"
  }

  implicit val liftQueryTerm: c.universe.Liftable[QueryTerm] = Liftable[QueryTerm] {
    case KW(ns, attr, refNs) => q"KW($ns, $attr, $refNs)"
    case Empty               => q"Empty"
    case NoBinding           => q"NoBinding"
    case Var(sym)            => q"Var($sym)"
    case Val(v)              => q"Val($v)"
    case DS(name)            => q"DS($name)"
    case DS                  => q"DS"
    case ImplDS              => q"ImplDS"
    case t                   => abort("Can't lift query term: " + t)
  }

  implicit val liftOutput: c.universe.Liftable[Output] = Liftable[Output] {
    case Var(sym)                      => q"Var($sym)"
    case Val(v)                        => q"Val($v)"
    case AggrExpr(fn, args, v)         => q"AggrExpr($fn, Seq(..$args), $v)"
    case Pull(e, ns, attr, enumPrefix) => q"Pull($e, $ns, $attr, $enumPrefix)"
    case NoVal                         => q"NoVal"
  }
  implicit val liftFind  : c.universe.Liftable[Find]   = Liftable[Find] { find => q"Find(Seq(..${find.outputs}))" }

  implicit val liftBinding: c.universe.Liftable[Binding] = Liftable[Binding] {
    case NoBinding               => q"NoBinding"
    case ScalarBinding(name)     => q"ScalarBinding($name)"
    case CollectionBinding(name) => q"CollectionBinding($name)"
    case TupleBinding(names)     => q"TupleBinding(Seq(..$names))"
    case RelationBinding(names)  => q"RelationBinding(Seq(..$names))"
  }

  implicit val liftInput: c.universe.Liftable[Input] = Liftable[Input] {
    case InDataSource(ds, argss)           => q"InDataSource($ds, Seq(..${argss.map(args => q"Seq(..$args)")}))"
    case InVar(binding, argss)             => q"InVar($binding, Seq(..${argss.map(args => q"Seq(..$args)")}))"
    case Placeholder(e, kw, v, enumPrefix) => q"Placeholder($e, $kw, $v, $enumPrefix)"
  }

  implicit val liftDataClause    : c.universe.Liftable[DataClause]     = Liftable[DataClause] { dc => q"DataClause(${dc.ds}, ${dc.e}, ${dc.a}, ${dc.v}, ${dc.tx}, ${dc.op})" }
  implicit val liftNotClause     : c.universe.Liftable[NotClause]      = Liftable[NotClause] { nc => q"NotClause(${nc.e}, ${nc.a})" }
  implicit val liftRuleInvocation: c.universe.Liftable[RuleInvocation] = Liftable[RuleInvocation] { ri => q"RuleInvocation(${ri.name}, Seq(..${ri.args}))" }
  implicit val liftFunct         : c.universe.Liftable[Funct]          = Liftable[Funct] { f => q"Funct(${f.name}, Seq(..${f.ins}), ${f.outs})" }

  implicit val liftNotClauses: c.universe.Liftable[NotClauses] = Liftable[NotClauses] { notClauses =>
    val clauses = notClauses.clauses map {
      case cl: DataClause     => q"$cl"
      case cl: NotClause      => q"$cl"
      case cl: RuleInvocation => q"$cl"
      case cl: Funct          => q"$cl"
      case q"$e"              => e
    }
    q"NotClauses(Seq(..$clauses))"
  }

  implicit val liftNotJoinClauses: c.universe.Liftable[NotJoinClauses] = Liftable[NotJoinClauses] { notJoinClauses =>
    val clauses = notJoinClauses.clauses map {
      case cl: DataClause     => q"$cl"
      case cl: NotClause      => q"$cl"
      case cl: RuleInvocation => q"$cl"
      case cl: Funct          => q"$cl"
      case q"$e"              => e
    }
    q"NotJoinClauses(Seq(..${notJoinClauses.nonUnifyingVars}), Seq(..$clauses))"
  }


  implicit val liftListOfClauses: c.universe.Liftable[Seq[Clause]] = Liftable[Seq[Clause]] { clauses =>
    val cls = clauses map {
      case cl: DataClause     => q"$cl"
      case cl: NotClause      => q"$cl"
      case cl: NotClauses     => q"$cl"
      case cl: NotJoinClauses => q"$cl"
      case cl: RuleInvocation => q"$cl"
      case cl: Funct          => q"$cl"
    }
    q"Seq(..$cls)"
  }

  implicit val liftClause: c.universe.Liftable[Clause] = Liftable[Clause] {
    case DataClause(ds, e, a, v, tx, op) => q"DataClause($ds, $e, $a, $v, $tx, $op)"
    case NotClause(e, a)                 => q"NotClause($e, $a)"
    case NotClauses(clauses)             => q"NotClauses($clauses)"
    case NotJoinClauses(vars, clauses)   => q"NotJoinClauses(Seq(..$vars), $clauses)"
    case RuleInvocation(name, args)      => q"RuleInvocation($name, Seq(..$args))"
    case Funct(name, ins, outs)          => q"Funct($name, Seq(..$ins), $outs)"
  }

  implicit val liftRule : c.universe.Liftable[Rule]  = Liftable[Rule] { rd => q"Rule(${rd.name}, Seq(..${rd.args}), Seq(..${rd.clauses}))" }
  implicit val liftIn   : c.universe.Liftable[In]    = Liftable[In] { in => q"In(Seq(..${in.inputs}), Seq(..${in.rules}), Seq(..${in.ds}))" }
  implicit val liftWhere: c.universe.Liftable[Where] = Liftable[Where] { where => q"Where(Seq(..${where.clauses}))" }
  implicit val liftQuery: c.universe.Liftable[Query] = Liftable[Query] { q => q"import molecule.ast.query._; Query(${q.f}, ${q.wi}, ${q.i}, ${q.wh})" }


  // Liftables for Model --------------------------------------------------------------

  implicit val liftBidirectional: c.universe.Liftable[Bidirectional] = Liftable[Bidirectional] {
    case BiSelfRef(card)             => q"BiSelfRef($card)"
    case BiSelfRefAttr(card)         => q"BiSelfRefAttr($card)"
    case BiOtherRef(card, attr)      => q"BiOtherRef($card, $attr)"
    case BiOtherRefAttr(card, attr)  => q"BiOtherRefAttr($card, $attr)"
    case BiEdge                      => q"BiEdge"
    case BiEdgeRef(card, attr)       => q"BiEdgeRef($card, $attr)"
    case BiEdgeRefAttr(card, attr)   => q"BiEdgeRefAttr($card, $attr)"
    case BiEdgePropRef(card)         => q"BiEdgePropRef($card)"
    case BiEdgePropAttr(card)        => q"BiEdgePropAttr($card)"
    case BiEdgePropRefAttr(card)     => q"BiEdgePropRefAttr($card)"
    case BiTargetRef(card, attr)     => q"BiTargetRef($card, $attr)"
    case BiTargetRefAttr(card, attr) => q"BiTargetRefAttr($card, $attr)"
  }

  implicit val liftGenericValue: c.universe.Liftable[GenericValue] = Liftable[GenericValue] {
    case NoValue                     => q"NoValue"
    case Id(eid)                     => q"Id($eid)"
    case Card(card)                  => q"Card($card)"
    case BiSelfRef(card)             => q"BiSelfRef($card)"
    case BiSelfRefAttr(card)         => q"BiSelfRefAttr($card)"
    case BiOtherRef(card, attr)      => q"BiOtherRef($card, $attr)"
    case BiOtherRefAttr(card, attr)  => q"BiOtherRefAttr($card, $attr)"
    case BiEdge                      => q"BiEdge"
    case BiEdgeRef(card, attr)       => q"BiEdgeRef($card, $attr)"
    case BiEdgeRefAttr(card, attr)   => q"BiEdgeRefAttr($card, $attr)"
    case BiEdgePropRef(card)         => q"BiEdgePropRef($card)"
    case BiEdgePropAttr(card)        => q"BiEdgePropAttr($card)"
    case BiEdgePropRefAttr(card)     => q"BiEdgePropRefAttr($card)"
    case BiTargetRef(card, attr)     => q"BiTargetRef($card, $attr)"
    case BiTargetRefAttr(card, attr) => q"BiTargetRefAttr($card, $attr)"
  }

  implicit val liftFn   : c.universe.Liftable[Fn]    = Liftable[Fn] { fn => q"Fn(${fn.name}, ${fn.value})" }
  implicit val liftValue: c.universe.Liftable[Value] = Liftable[Value] {
    case EntValue                     => q"EntValue"
    case VarValue                     => q"VarValue"
    case NoValue                      => q"NoValue"
    case Id(eid)                      => q"Id($eid)"
    case Card(card)                   => q"Card($card)"
    case BiSelfRef(card)              => q"BiSelfRef($card)"
    case BiSelfRefAttr(card)          => q"BiSelfRefAttr($card)"
    case BiOtherRef(card, attr)       => q"BiOtherRef($card, $attr)"
    case BiOtherRefAttr(card, attr)   => q"BiOtherRefAttr($card, $attr)"
    case BiEdge                       => q"BiEdge"
    case BiEdgeRef(card, attr)        => q"BiEdgeRef($card, $attr)"
    case BiEdgeRefAttr(card, attr)    => q"BiEdgeRefAttr($card, $attr)"
    case BiEdgePropRef(card)          => q"BiEdgePropRef($card)"
    case BiEdgePropAttr(card)         => q"BiEdgePropAttr($card)"
    case BiEdgePropRefAttr(card)      => q"BiEdgePropRefAttr($card)"
    case BiTargetRef(card, attr)      => q"BiTargetRef($card, $attr)"
    case BiTargetRefAttr(card, attr)  => q"BiTargetRefAttr($card, $attr)"
    case BackValue(value)             => q"BackValue($value)"
    case EnumVal                      => q"EnumVal"
    case IndexVal                     => q"IndexVal"
    case And(values)                  => q"And(Seq(..$values))"
    case Eq(values) if values.isEmpty => q"Eq(Nil)"
    case Eq(values)                   => q"Eq(Seq(..$values))"
    case Neq(values)                  => q"Neq(Seq(..$values))"
    case Lt(value)                    => q"Lt($value)"
    case Gt(value)                    => q"Gt($value)"
    case Le(value)                    => q"Le($value)"
    case Ge(value)                    => q"Ge($value)"
    case Fn(fn, value)                => q"Fn($fn, $value)"
    case Qm                           => q"Qm"
    case Distinct                     => q"Distinct"
    case Fulltext(search)             => q"Fulltext(Seq(..$search))"
    case AssertValue(values)          => q"AssertValue(Seq(..$values))"
    case ReplaceValue(oldNew)         => q"ReplaceValue(Seq(..$oldNew))"
    case RetractValue(values)         => q"RetractValue(Seq(..$values))"
    case AssertMapPairs(pairs)        => q"AssertMapPairs(Seq(..$pairs))"
    case ReplaceMapPairs(pairs)       => q"ReplaceMapPairs(Seq(..$pairs))"
    case RetractMapKeys(keys)         => q"RetractMapKeys(Seq(..$keys))"
    case MapEq(pairs)                 => q"MapEq(Seq(..$pairs))"
    case MapKeys(keys)                => q"MapKeys(Seq(..$keys))"
  }

  implicit val liftAtom   : c.universe.Liftable[Atom]    = Liftable[Atom] { a => q"Atom(${a.ns}, ${a.attr}, ${a.tpe}, ${a.card}, ${a.value}, ${a.enumPrefix}, Seq(..${a.gvs}), Seq(..${a.keys}))" }
  implicit val liftBond   : c.universe.Liftable[Bond]    = Liftable[Bond] { b => q"Bond(${b.ns}, ${b.refAttr}, ${b.refNs}, ${b.card}, Seq(..${b.gvs}))" }
  implicit val liftReBond : c.universe.Liftable[ReBond]  = Liftable[ReBond] { r => q"ReBond(${r.backRef})" }
  implicit val liftGeneric: c.universe.Liftable[Generic] = Liftable[Generic] { m => q"Generic(${m.ns}, ${m.attr}, ${m.tpe}, ${m.value})" }
  implicit val liftGroup  : c.universe.Liftable[Nested]  = Liftable[Nested] { n0 =>
    val es0: Seq[c.universe.Tree] = n0.elements map {
      case a: Atom    => q"$a"
      case b: Bond    => q"$b"
      case r: ReBond  => q"$r"
      case Self       => q"Self"
      case g: Generic => q"$g"
      case n1: Nested => {
        val es1: Seq[c.universe.Tree] = n1.elements map {
          case a: Atom    => q"$a"
          case b: Bond    => q"$b"
          case r: ReBond  => q"$r"
          case Self       => q"Self"
          case g: Generic => q"$g"
          case n2: Nested => {
            val es2: Seq[c.universe.Tree] = n2.elements map {
              case a: Atom    => q"$a"
              case b: Bond    => q"$b"
              case r: ReBond  => q"$r"
              case Self       => q"Self"
              case g: Generic => q"$g"
              case n3: Nested => {
                val es3: Seq[c.universe.Tree] = n3.elements map {
                  case a: Atom    => q"$a"
                  case b: Bond    => q"$b"
                  case r: ReBond  => q"$r"
                  case Self       => q"Self"
                  case g: Generic => q"$g"
                  case n4: Nested => {
                    val es4: Seq[c.universe.Tree] = n4.elements map {
                      case a: Atom    => q"$a"
                      case b: Bond    => q"$b"
                      case r: ReBond  => q"$r"
                      case Self       => q"Self"
                      case g: Generic => q"$g"
                      case n5: Nested => {
                        val es5: Seq[c.universe.Tree] = n5.elements map {
                          case a: Atom    => q"$a"
                          case b: Bond    => q"$b"
                          case r: ReBond  => q"$r"
                          case Self       => q"Self"
                          case g: Generic => q"$g"
                        }
                        q"Nested(${n5.bond}, Seq(..$es5))"
                      }
                    }
                    q"Nested(${n4.bond}, Seq(..$es4))"
                  }
                }
                q"Nested(${n3.bond}, Seq(..$es3))"
              }
            }
            q"Nested(${n2.bond}, Seq(..$es2))"
          }
        }
        q"Nested(${n1.bond}, Seq(..$es1))"
      }
    }
    q"Nested(${n0.bond}, Seq(..$es0))"
  }

  implicit val liftTxMetaData: c.universe.Liftable[TxMetaData] = Liftable[TxMetaData] { tm =>
    val es = tm.elements map {
      case a: Atom    => q"$a"
      case b: Bond    => q"$b"
      case r: ReBond  => q"$r"
      case Self       => q"Self"
      case g: Generic => q"$g"
      case q"$e"      => e
    }
    q"TxMetaData(Seq(..$es))"
  }

  implicit val liftComposite: c.universe.Liftable[Composite] = Liftable[Composite] { fm =>
    val es = fm.elements map {
      case a: Atom    => q"$a"
      case b: Bond    => q"$b"
      case r: ReBond  => q"$r"
      case Self       => q"Self"
      case g: Generic => q"$g"
      case q"$e"      => e
    }
    q"Composite(Seq(..$es))"
  }

  implicit val liftListOfElements: c.universe.Liftable[Seq[Element]] = Liftable[Seq[Element]] { elements =>
    val es: Seq[c.universe.Tree] = elements map {
      case a: Atom       => q"$a"
      case b: Bond       => q"$b"
      case r: ReBond     => q"$r"
      case Self          => q"Self"
      case n: Nested     => q"$n"
      case g: Generic    => q"$g"
      case t: TxMetaData => q"$t"
      case c: Composite  => q"$c"
    }
    q"Seq(..$es)"
  }

  implicit val liftElement: c.universe.Liftable[Element] = Liftable[Element] {
    case Atom(ns, attr, tpeS, card, value, enumPrefix, gs, keys) => q"Atom($ns, $attr, $tpeS, $card, $value, $enumPrefix, Seq(..$gs), Seq(..$keys))"
    case Bond(ns, refAttr, refNs, card, gs)                      => q"Bond($ns, $refAttr, $refNs, $card, Seq(..$gs))"
    case ReBond(backRef)                                         => q"ReBond($backRef)"
    case Self                                                    => q"Self"
    case Nested(ref, elements)                                   => q"Nested($ref, $elements)"
    case Generic(ns, attr, kind, value)                          => q"Generic($ns, $attr, $kind, $value)"
    case TxMetaData(elements)                                    => q"TxMetaData($elements)"
    case Composite(elements)                                     => q"Composite($elements)"
    case EmptyElement                                            => q"EmptyElement"
  }

  implicit val liftModel: c.universe.Liftable[Model] = Liftable[Model] { model => q"Model(Seq(..${model.elements}))" }
}
