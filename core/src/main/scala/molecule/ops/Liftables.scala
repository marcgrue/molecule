package molecule
package ops
import java.net.URI
import java.util.{Date, UUID}

import molecule.ast.model._
import molecule.ast.query._
import molecule.util.MacroHelpers

import scala.reflect.macros.whitebox.Context

trait Liftables[Ctx <: Context] extends MacroHelpers[Ctx] {
  import c.universe._
  val z = DebugMacro("Liftables", 1, 10, true)


  // General liftables --------------------------------------------------------------

  def mkDate(date: Date) = q"new Date(${date.getTime})"
  def mkUUID(uuid: UUID) = q"java.util.UUID.fromString(${uuid.toString})"
  def mkURI(uri: URI) = q"new java.net.URI(${uri.getScheme}, ${uri.getUserInfo}, ${uri.getHost}, ${uri.getPort}, ${uri.getPath}, ${uri.getQuery}, ${uri.getFragment})"

  implicit val liftAny = Liftable[Any] {
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
    case uuid: UUID                    => mkUUID(uuid)
    case uri: URI                      => mkURI(uri)
    case qm: Qm.type                   => q"Qm"
    case maybe: Distinct.type          => q"Distinct"
    case entValue: EntValue.type       => q"EntValue"
    case varValue: VarValue.type       => q"VarValue"
    case other                         => abort("[Liftables:liftAny] Can't lift unexpected Any type: " + other.getClass)
  }

  implicit val liftTuple2 = Liftable[Product] {
    case (k: String, v: String) => q"($k, $v)"
    case (k: Int, v: Int)       => q"($k, $v)"
    case (k: Long, v: Long)     => q"($k, $v)"
    case (k: Float, v: Float)   => q"($k, $v)"
    case (k: Double, v: Double) => q"($k, $v)"
    case (k: Date, v: Date)     => q"(${mkDate(k)}, ${mkDate(v)})"
    case (k: UUID, v: UUID)     => q"(${mkUUID(k)}, ${mkUUID(v)})"
    case (k: URI, v: URI)       => q"(${mkURI(k)}, ${mkURI(v)})"
    case (a, b)                 => abort(s"[Liftables:liftTuple2] Can't lift unexpected Tuple2: ($a, $b)")
    case other                  => abort(s"[Liftables:Product] Can't lift unexpected product type: $other")
  }


  // Liftables for Query --------------------------------------------------------------

  implicit val liftVar    = Liftable[Var] { v => q"Var(${v.v})" }
  implicit val liftVal    = Liftable[Val] { value => q"Val(${value.v})" }
  implicit val liftAttrKW = Liftable[KW] { kw => q"KW(${kw.ns}, ${kw.attr}, ${kw.refNs})" }
  implicit val liftWith   = Liftable[With] { widh => q"With(Seq(..${widh.variables}))" }

  implicit val liftQueryValue = Liftable[QueryValue] {
    case Var(sym)                      => q"Var($sym)"
    case Val(v)                        => q"Val($v)"
    case Pull(e, ns, attr, enumPrefix) => q"Pull($e, $ns, $attr, $enumPrefix)"
    case Dummy                         => q"Dummy"
    case NoVal                         => q"NoVal"
  }

  implicit val liftDataSource = Liftable[DataSource] {
    case DS(name) => q"DS($name)"
    case DS       => q"DS"
    case ImplDS   => q"ImplDS"
  }

  implicit val liftQueryTerm = Liftable[QueryTerm] {
    case KW(ns, attr, refNs) => q"KW($ns, $attr, $refNs)"
    case Empty               => q"Empty"
    case NoBinding           => q"NoBinding"
    case Var(sym)            => q"Var($sym)"
    case Val(v)              => q"Val($v)"
    case DS(name)            => q"DS($name)"
    case DS                  => q"DS"
    case ImplDS              => q"ImplDS"
    case t                   => abort("[Liftables:liftQueryTerm] Can't lift query term: " + t)
  }

  implicit val liftOutput = Liftable[Output] {
    case AggrExpr(fn, args, v) => q"AggrExpr($fn, Seq(..$args), $v)"
    case Var(sym)              => q"Var($sym)"
  }
  implicit val liftFind   = Liftable[Find] { find => q"Find(Seq(..${find.outputs}))" }

  implicit val liftBinding = Liftable[Binding] {
    case NoBinding               => q"NoBinding"
    case ScalarBinding(name)     => q"ScalarBinding($name)"
    case CollectionBinding(name) => q"CollectionBinding($name)"
    case TupleBinding(names)     => q"TupleBinding(Seq(..$names))"
    case RelationBinding(names)  => q"RelationBinding(Seq(..$names))"
  }

  implicit val liftDataClause = Liftable[DataClause] { cl =>
    q"DataClause(${cl.ds}, ${cl.e}, ${cl.a}, ${cl.v}, ${cl.tx}, ${cl.op})"
  }


  implicit val liftInput = Liftable[Input] {
    case InDataSource(ds, argss)           => q"InDataSource($ds, Seq(...$argss))"
    case InVar(binding, argss)             => q"InVar($binding, Seq(...$argss))"
    case Placeholder(v, kw, enumPrefix, e) => q"Placeholder($v, $kw, $enumPrefix, $e)"
  }

  implicit val liftClause = Liftable[Clause] {
    case DataClause(ds, e, a, v, tx, op) => q"DataClause($ds, $e, $a, $v, $tx, $op)"
    case NotClause(ds, e, a)             => q"NotClause($ds, $e, $a)"
    case RuleInvocation(name, args)      => q"RuleInvocation($name, Seq(..$args))"
    case Funct(name, ins, outs)          => q"Funct($name, Seq(..$ins), $outs)"
  }

  implicit val liftRule = Liftable[Rule] { rd =>
    q"Rule(${rd.name}, Seq(..${rd.args}), Seq(..${rd.clauses}))"
  }

  implicit val liftIn    = Liftable[In] { in => q"In(Seq(..${in.inputs}), Seq(..${in.rules}), Seq(..${in.ds}))" }
  implicit val liftWhere = Liftable[Where] { where => q"Where(Seq(..${where.clauses}))" }
  implicit val liftQuery = Liftable[Query] { q =>
    q"import molecule.ast.query._; Query(${q.f}, ${q.wi}, ${q.i}, ${q.wh})"
  }


  // Liftables for Model --------------------------------------------------------------

  implicit val liftGeneric = Liftable[Generic] {
    case AttrVar(v)      => q"AttrVar($v)"
    case TxValue         => q"TxValue"
    case TxValue_        => q"TxValue_"
    case TxTValue        => q"TxTValue"
    case TxInstantValue  => q"TxInstantValue"
    case OpValue         => q"OpValue"
    case NsValue(values) => q"NsValue(Seq(..$values))"
    case NoValue         => q"NoValue"
  }

  implicit val liftFn    = Liftable[Fn] { fn => q"Fn(${fn.name}, ${fn.value})" }
  implicit val liftValue = Liftable[Value] {
    case EntValue         => q"EntValue"
    case VarValue         => q"VarValue"
    case NoValue          => q"NoValue"
    case AttrVar(v)       => q"AttrVar($v)"
    case TxValue          => q"TxValue"
    case TxValue_         => q"TxValue_"
    case TxTValue         => q"TxTValue"
    case TxInstantValue   => q"TxInstantValue"
    case NsValue(values)  => q"NsValue(Seq(..$values))"
    case OpValue          => q"OpValue"
    case BackValue(value) => q"BackValue($value)"
    case EnumVal          => q"EnumVal"
    case IndexVal         => q"IndexVal"
    case And(values)      => q"And(Seq(..$values))"
    case Eq(values)       => q"Eq(Seq(..$values))"
    case Neq(values)      => q"Neq(Seq(..$values))"
    case Lt(value)        => q"Lt($value)"
    case Gt(value)        => q"Gt($value)"
    case Le(value)        => q"Le($value)"
    case Ge(value)        => q"Ge($value)"
    case Fn(fn, value)    => q"Fn($fn, $value)"
    case Length(fn)       => q"Length($fn)"
    case Qm               => q"Qm"
    case Distinct         => q"Distinct"
    case Fulltext(search) => q"Fulltext(Seq(..$search))"
    case Replace(values)  => q"Replace($values)"
    case Remove(values)   => q"Remove(Seq(..$values))"
    case Mapping(pairs)   => q"Mapping(Seq(..$pairs))"
    case Keys(ks)         => q"Keys(Seq(..$ks))"
  }

  implicit val liftAtom       = Liftable[Atom] { a => q"Atom(${a.ns}, ${a.name}, ${a.tpeS}, ${a.card}, ${a.value}, ${a.enumPrefix}, Seq(..${a.gs}), Seq(..${a.keys}))" }
  implicit val liftBond       = Liftable[Bond] { b => q"Bond(${b.ns}, ${b.refAttr}, ${b.refNs}, ${b.card})" }
  implicit val liftReBond     = Liftable[ReBond] { r => q"ReBond(${r.backRef}, ${r.refAttr}, ${r.refNs}, ${r.distinct}, ${r.prevVar})" }
  implicit val liftTransitive = Liftable[Transitive] { r => q"Transitive(${r.backRef}, ${r.refAttr}, ${r.refNs}, ${r.depth}, ${r.prevVar})" }
  implicit val liftMeta       = Liftable[Meta] { m => q"Meta(${m.ns}, ${m.attr}, ${m.kind}, ${m.generic}, ${m.value})" }
  implicit val liftGroup      = Liftable[Group] { g0 =>
    val es0 = g0.elements map {
      case a: Atom       => q"$a"
      case b: Bond       => q"$b"
      case r: ReBond     => q"$r"
      case r: Transitive => q"$r"
      case Self          => q"Self"
      case m: Meta       => q"$m"
      case g1: Group     => {
        val es1 = g1.elements map {
          case a: Atom       => q"$a"
          case b: Bond       => q"$b"
          case r: ReBond     => q"$r"
          case r: Transitive => q"$r"
          case Self          => q"Self"
          case m: Meta       => q"$m"
        }
        q"Group(${g1.ref}, Seq(..$es1))"
      }
    }
    q"Group(${g0.ref}, Seq(..$es0))"
  }
  implicit val liftTxModel    = Liftable[TxModel] { g =>
    val es = g.elements map { case q"$e" => e }
    q"TxModel(Seq(..$es))"
  }

  implicit val liftListOfElements = Liftable[Seq[Element]] { elements =>
    val es = elements map {
      case a: Atom       => q"$a"
      case b: Bond       => q"$b"
      case r: ReBond     => q"$r"
      case r: Transitive => q"$r"
      case Self          => q"Self"
      case g: Group      => q"$g"
      case m: Meta       => q"$m"
      case t: TxModel    => q"$t"
    }
    q"Seq(..$es)"
  }

  implicit val liftElement = Liftable[Element] {
    case Atom(ns, name, tpeS, card, value, enumPrefix, gs, keys) => q"Atom($ns, $name, $tpeS, $card, $value, $enumPrefix, Seq(..$gs), Seq(..$keys))"
    case Bond(ns, refAttr, refNs, card)                          => q"Bond($ns, $refAttr, $refNs, $card)"
    case ReBond(backRef, refAttr, refNs, distinct, prevVar)      => q"ReBond($backRef, $refAttr, $refNs, $distinct, $prevVar)"
    case Transitive(backRef, refAttr, refNs, level, prevVar)     => q"Transitive($backRef, $refAttr, $refNs, $level, $prevVar)"
    case Self                                                    => q"Self"
    case Group(ref, elements)                                    => q"Group($ref, $elements)"
    case Meta(ns, attr, kind, generic, value)                    => q"Meta($ns, $attr, $kind, $generic, $value)"
    case TxModel(elements)                                       => q"TxModel($elements)"
    case EmptyElement                                            => q"EmptyElement"
  }

  implicit val liftModel = Liftable[Model] { model => q"Model(Seq(..${model.elements}))" }
}
