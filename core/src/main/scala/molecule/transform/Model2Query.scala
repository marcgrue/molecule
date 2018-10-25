package molecule
package transform
import molecule.ast.model.{Transitive, _}
import molecule.ast.query.{Val, _}
import molecule.ops.QueryOps._
import molecule.transform.exception.Model2QueryException
import molecule.util.{Debug, Helpers}


/** Model to Query transformation.
  * <br><br>
  * Second transformation in Molecules series of transformations from
  * custom boilerplate DSL constructs to Datomic queries:
  * <br><br>
  * Custom DSL molecule --> Model --> Query --> Datomic query string
  *
  * @see [[http://www.scalamolecule.org/dev/transformation/]]
  * */
object Model2Query extends Helpers {
  val x = Debug("Model2Query", 1, 19)

  var nestedEntityClauses: List[Funct] = List.empty[Funct]
  var nestedEntityVars   : List[Var]   = List.empty[Var]

  def apply(model: Model): (Query, Option[Query]) = {
    // reset on each apply
    nestedEntityClauses = Nil
    val query = model.elements.foldLeft((Query(), "a", "b", "", "", "")) {
      case ((query_, e, v, prevNs, prevAttr, prevRefNs), element) => make(model, query_, element, e, v, prevNs, prevAttr, prevRefNs)
    }._1
    if (nestedEntityClauses.isEmpty) {
      (postProcess(model, query), None)
    } else {
      val query2 = postProcess(model, query)
      (query2, Some(query2.copy(f = Find(nestedEntityVars ++ query2.f.outputs), wh = Where(query2.wh.clauses ++ nestedEntityClauses))))
    }
  }

  def make(model: Model, query: Query, element: Element, e: String, v: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val (w, y) = (nextChar(v, 1), nextChar(v, 2))
    element match {
      case atom: Atom             => makeAtom(model, query, atom, e, v, w, prevNs, prevAttr, prevRefNs)
      case bond: Bond             => makeBond(model, query, bond, e, v, w, prevNs, prevAttr, prevRefNs)
      case rb: ReBond             => makeReBond(model, query, rb, v)
      case meta: Meta             => makeMeta(model, query, meta, e, v, w, y, prevNs, prevAttr, prevRefNs)
      case txMetaData: TxMetaData => makeTxMetaData(model, query, txMetaData, w, prevNs, prevAttr, prevRefNs)
      case nested: Nested         =>
        if (nestedEntityClauses.isEmpty) {
          nestedEntityVars = List(Var("sort0"))
          nestedEntityClauses = List(Funct("molecule.util.JavaFunctions/bind", Seq(Var(e)), ScalarBinding(Var("sort0"))))
        }
        // Next level
        nestedEntityVars = nestedEntityVars :+ Var("sort" + nestedEntityClauses.size)
        nestedEntityClauses = nestedEntityClauses :+ Funct("molecule.util.JavaFunctions/bind", Seq(Var(w)), ScalarBinding(Var("sort" + nestedEntityClauses.size)))
        makeNested(model, query, nested, e, v, w, prevNs, prevAttr, prevRefNs)
      case composite: Composite   => makeComposite(model, query, composite, e, v, prevNs, prevAttr, prevRefNs)
      case transitive: Transitive => makeTransitive(model, query, transitive, v, w)
      case Self                   => (query, w, y, prevNs, prevAttr, prevRefNs)
      case other                  => throw new Model2QueryException("Unresolved query variables from model: " + (other, e, v, prevNs, prevAttr, prevRefNs))
    }
  }

  def makeAtom(model: Model, query: Query, atom: Atom, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val (ns, attr) = (atom.ns, atom.name)
    atom match {
      case Atom(_, _, _, _, Fn("unify", _), _, _, _) => makeAtomUnify(model, query, atom, ns, attr, e, v, w, prevNs)
      case Atom(_, _, "a", _, _, _, _, _)            => (resolve(query, e, v, atom), e, w, ns, attr, "")
      case Atom(_, _, "ns", _, _, _, _, _)           => (resolve(query, e, v, atom), e, w, ns, attr, "")
      case _ if prevRefNs == "IndexVal"              => (resolve(query, e, w, atom), e, w, ns, attr, "")
      case Atom(`prevRefNs`, _, _, _, _, _, _, _)    => (resolve(query, v, w, atom), v, w, ns, attr, "")
      case Atom(`prevAttr`, _, _, _, _, _, _, _)     => (resolve(query, v, w, atom), v, w, ns, attr, "")
      case Atom(`prevNs`, _, _, _, _, _, _, _)       => (resolve(query, e, w, atom), e, w, ns, attr, "")
      case _                                         => (resolve(query, e, v, atom), e, v, ns, attr, "")
    }
  }

  def makeBond(model: Model, query: Query, bond: Bond, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = bond match {
    case Bond(`prevNs`, `prevAttr`, refNs, _, bi: Bidirectional) => (resolve(query, v, w, bond), v, w, prevNs, prevAttr, refNs)
    case Bond(`prevNs`, `prevAttr`, refNs, _, _)                 => (resolve(query, v, w, bond), v, w, prevNs, prevAttr, refNs)
    case Bond(`prevNs`, refAttr, refNs, _, _)                    => (resolve(query, e, w, bond), e, w, prevNs, refAttr, refNs)
    case Bond(`prevAttr`, refAttr, refNs, _, _)                  => (resolve(query, v, w, bond), v, w, prevAttr, refAttr, refNs)
    case Bond(`prevRefNs`, refAttr, refNs, _, _)                 => (resolve(query, v, w, bond), v, w, prevRefNs, refAttr, refNs)
    case Bond(ns, refAttr, refNs, _, _)                          => (resolve(query, e, v, bond), e, v, ns, refAttr, refNs)
  }

  def makeAtomUnify(model: Model, query: Query, a: Atom, ns: String, attr: String, e: String, v: String, w: String, prevNs: String)
  : (Query, String, String, String, String, String) = {
    val attr1 = if (attr.last == '_') attr.init else attr
    // Find previous matching value that we want to unify with (from an identical attribute)
    query.wh.clauses.reverse.collectFirst {
      // Having a value var to unify with
      case dc@DataClause(_, _, KW(ns0, attr0, _), Var(v0), _, _) if ns0 == ns && attr0 == attr1 => ns match {
        case `prevNs` => (resolve(query, e, v0, a), e, v, ns, attr, "")
        case _        => (resolve(query, v, v0, a), v, v, ns, attr, "")
      }

      // Missing value var to unify with
      case dc@DataClause(_, _, KW(ns0, attr0, _), _, _, _) if ns0 == ns && attr0 == attr1 =>
        // Add initial clause to have a var to unify with
        val initialClause = dc.copy(v = Var(w))
        val newWhere = query.wh.copy(clauses = query.wh.clauses :+ initialClause)
        (resolve(query.copy(wh = newWhere), v, w, a), v, w, ns, attr, "")
    } getOrElse {
      throw new Model2QueryException(s"Can't find previous attribute matching unifying attribute `$ns.$attr` in query so far:\n$query\nATOM: $a")
    }
  }

  def makeReBond(model: Model, query: Query, rb: ReBond, v: String)
  : (Query, String, String, String, String, String) = {
    val backRef = rb.backRef
    val backRefE = query.wh.clauses.reverse.collectFirst {
      case DataClause(_, backE, a, Var(backV), _, _) if a.ns == backRef => backE.v
    } getOrElse {
      throw new Model2QueryException(s"Can't find back reference namespace `$backRef` in query so far:\n$model\n---------\n$query\n---------\n$rb")
    }
    (query, backRefE, v, backRef, "", "")
  }

  def makeMeta(model: Model, query: Query, meta: Meta, e: String, v: String, w: String, y: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = meta match {
    case Meta(ns, attr, "e", NoValue, Eq(Seq(Qm)))                  => (resolve(query, e, v, meta), e, v, ns, attr, prevRefNs)
    case Meta(ns, attr, "e", NoValue, Eq(eids))                     => (resolve(query, e, v, meta), e, v, ns, attr, prevRefNs)
    case Meta(ns, attr, "e", _, IndexVal) if prevRefNs == ""        => (resolve(query, e, v, meta), e, w, ns, attr, "")
    case Meta(ns, attr, "e", _, IndexVal)                           => (resolve(query, v, w, meta), v, y, ns, attr, "IndexVal")
    case Meta(ns, attr, "r", _, IndexVal)                           => (resolve(query, w, v, meta), e, w, ns, attr, "IndexVal")
    case Meta(ns, attr, "e", NoValue, _) if prevRefNs == ""         => (resolve(query, e, v, meta), e, w, ns, attr, "")
    case Meta(ns, attr, "e", NoValue, _) if prevRefNs == "IndexVal" => (resolve(query, e, y, meta), e, y, ns, attr, "")
    case Meta(ns, attr, "e", NoValue, EntValue)                     => (resolve(query, v, w, meta), v, w, ns, attr, "")
    //    case Meta(ns, attr, "e", NoValue, _)                            => (resolve(query, v, w, meta), e, w, ns, attr, "")
    case Meta(ns, attr, _, _, _) => (resolve(query, e, v, meta), e, v, ns, attr, "")
  }

  def makeTxMetaData(model: Model, query: Query, txMetaData: TxMetaData, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val (q2, e2, v2, prevNs2, prevAttr2, prevRefNs2) = txMetaData.elements.foldLeft((query, "tx", w, prevNs, prevAttr, prevRefNs)) {
      case ((q1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element) => make(model, q1, element, e1, v1, prevNs1, prevAttr1, prevRefNs1)
    }
    (q2, e2, nextChar(v2, 1), prevNs2, prevAttr2, prevRefNs2)
  }

  def makeNested(model: Model, query: Query, nested: Nested, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val Nested(b@Bond(ns, _, _, _, _), elements) = nested
    val (e2, elements2) = if (ns == "") (e, elements) else (w, b +: elements)
    val (q2, _, v2, ns2, attr2, refNs2) = elements2.foldLeft((query, e, v, prevNs, prevAttr, prevRefNs)) {
      case ((query1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element1) =>
        make(model, query1, element1, e1, v1, prevNs1, prevAttr1, prevRefNs1)
    }
    (q2, e2, nextChar(v2, 1), ns2, attr2, refNs2)
  }

  def makeComposite(model: Model, query: Query, composite: Composite, e: String, v: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val eid: String = if (query.wh.clauses.isEmpty) {
      e
    } else {
      query.wh.clauses.reverse.collectFirst {
        case DataClause(_, Var(lastE), KW(ns, _, _), _, _, _) if ns != "db" => lastE
      } getOrElse query.wh.clauses.reverse.collectFirst {
        case Funct(_, Seq(Var(lastE)), _) => lastE
      }.getOrElse(throw new Model2QueryException(s"Couldn't find `e` from last data clause"))
    }

    val (q2, e2, v2, prevNs2, prevAttr2, prevRefNs2) = composite.elements.foldLeft((query, eid, v, prevNs, prevAttr, prevRefNs)) {
      case ((q1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element) => make(model, q1, element, e1, v1, prevNs1, prevAttr1, prevRefNs1)
    }
    (q2, e2, nextChar(v2, 1), prevNs2, prevAttr2, prevRefNs2)
  }

  def makeTransitive(model: Model, query: Query, transitive: Transitive, v: String, w: String)
  : (Query, String, String, String, String, String) = {
    val Transitive(backRef, refAttr, refNs, _, _) = transitive
    val (backRefE, backRefV) = query.wh.clauses.reverse.collectFirst {
      case DataClause(_, backE, a, Var(backV), _, _) if a.ns == backRef => (backE.v, backV)
    } getOrElse {
      throw new Model2QueryException(s"Can't find back reference namespace `$backRef` in query so far:\n$query")
    }
    val backRefElement = transitive.copy(prevVar = backRefV)
    (resolve(query, backRefE, w, backRefElement), v, w, backRef, refAttr, refNs)
  }


  def resolve(q: Query, e: String, v: String, element: Element): Query = {
    val (v1: String, v2: String, v3: String) = (v + 1, v + 2, v + 3)
    element match {
      case atom: Atom                                      => resolveAtom(q, e, atom, v, v1, v2, v3)
      case Bond(ns, refAttr, refNs, _, _)                  => q.ref(e, ns, refAttr, v, refNs)
      case meta: Meta                                      => resolveMeta(q, e, meta, v, v1, v2, v3)
      case ReBond(backRef, refAttr, refNs, _, _)           => q.ref(e, backRef, refAttr, v, refNs)
      case Transitive(backRef, refAttr, _, depth, prevVar) => q.transitive(backRef, refAttr, prevVar, v, depth)
      case unresolved                                      => throw new Model2QueryException("Unresolved model: " + unresolved)
    }
  }

  def resolveAtom(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = {
    val (opt: Boolean, tacit: Boolean) = (a.name.last == '$', a.name.last == '_')
    a match {
      // Manipulation (not relevant to queries)
      case Atom(_, _, _, _, AssertValue(_) | ReplaceValue(_) | RetractValue(_) | AssertMapPairs(_) | ReplaceMapPairs(_) | RetractMapKeys(_), _, _, _) => q

      // Generic
      case a@Atom("?", "attr_", _, _, _, _, _, _) => resolveGenericAttrTacit(q, e, a, v, v1, v2, v3)
      case a@Atom("?", "attr", _, _, _, _, _, _)  => resolveGenericAttrMandatory(q, e, a, v, v1, v2, v3)
      case a@Atom("ns_", "?", _, _, _, _, _, _)   => resolveGenericNsTacit(q, e, a, v, v1, v2, v3)
      case a@Atom("ns", "?", _, _, _, _, _, _)    => resolveGenericNsMandatory(q, e, a, v, v1, v2, v3)

      // Enum
      case a@Atom(_, _, _, 2, _, Some(prefix), _, _) if opt       => resolveAtomEnumOptional2(q, e, a, v, v1, v2, prefix)
      case a@Atom(_, _, _, 1, _, Some(prefix), _, _) if opt       => resolveAtomEnumOptional1(q, e, a, v, v1, v2, prefix)
      case a@Atom(_, _, _, 1 | 2, _, Some(prefix), _, _) if tacit => resolveAtomEnumTacit(q, e, a, v, v1, v2, v3, prefix)
      case a@Atom(_, _, _, 2, _, Some(prefix), _, _)              => resolveAtomEnumMandatory2(q, e, a, v, v1, v2, v3, prefix)
      case a@Atom(_, _, _, 1, _, Some(prefix), _, _)              => resolveAtomEnumMandatory1(q, e, a, v, v1, v2, v3, prefix)

      // Atom
      case a@Atom(_, _, _, 2, _, _, _, _) if opt       => resolveAtomOptional2(q, e, a, v)
      case a@Atom(_, _, _, 1, _, _, _, _) if opt       => resolveAtomOptional1(q, e, a, v)
      case a@Atom(_, _, _, 1 | 2, _, _, _, _) if tacit => resolveAtomTacit(q, e, a, v, v1)
      case a@Atom(_, _, _, 2, _, _, _, _)              => resolveAtomMandatory2(q, e, a, v, v1, v2)
      case a@Atom(_, _, _, 1, _, _, _, _)              => resolveAtomMandatory1(q, e, a, v, v1, v2)

      // Mapped attributes
      case a@Atom(_, _, _, 3, _, _, _, _) if opt      => resolveAtomMapOptional(q, e, a, v)
      case a@Atom(_, _, _, 3, _, _, _, keys) if tacit => resolveAtomMapTacit(q, e, a, v, keys)
      case a@Atom(_, _, _, 3, _, _, _, keys)          => resolveAtomMapMandatory(q, e, a, v, keys)

      // Keyed mapped attributes
      case a@Atom(_, _, _, 4, _, _, _, _) if opt            => resolveAtomKeyedMapOptional(q, e, a)
      case a@Atom(_, _, _, 4, _, _, _, key :: Nil) if tacit => resolveAtomKeyedMapTacit(q, e, a, v, v1, v2, key)
      case a@Atom(_, _, _, 4, _, _, _, key :: Nil)          => resolveAtomKeyedMapMandatory(q, e, a, v, v1, v2, v3, key)
    }
  }

  def resolveMeta(q: Query, e: String, meta: Meta, v: String, v1: String, v2: String, v3: String): Query = meta match {
    case Meta(_, _, "e", _, Fn("count", Some(i)))                => q.find("count", Seq(i), e, Nil)
    case Meta(_, _, "e", _, Fn("count", _))                      => q.find("count", Nil, e, Nil)
    case Meta(_, _, "e", _, Length(Some(Fn(_, _))))              => q.find(e, Nil)
    case Meta(ns, attr, "e", _, Eq(Seq(Qm))) if attr.last == '_' => q.in(e, ns, attr, e)
    case Meta(_, _, "e", _, Eq(Seq(Qm)))                         => q.find(e, Nil).in(e)
    case Meta(_, attr, "e", _, Eq(eids)) if attr.last == '_'     => q.in(eids, e)
    case Meta(_, _, "e", _, Eq(eids))                            => q.find(e, Nil).in(eids, e)
    case Meta(_, _, "r", _, IndexVal)                            => q.find(v, Nil).func("molecule.util.JavaFunctions/bind", Seq(Var(e)), ScalarBinding(Var(v)))
    case Meta(_, _, _, Id(eid), IndexVal)                        => q.find(v, Nil).func("molecule.util.JavaFunctions/bind", Seq(Val(eid)), ScalarBinding(Var(v)))
    case Meta(_, _, _, _, IndexVal)                              => q.find(v, Nil).func("molecule.util.JavaFunctions/bind", Seq(Var(e)), ScalarBinding(Var(v)))
    case Meta(_, attr, _, _, EntValue) if attr.last == '_'       => q
    case Meta(_, _, _, _, EntValue)                              => q.find(e, Nil)
    case Meta(_, _, _, _, _)                                     => q
  }


  def resolveGenericAttrTacit(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = a.value match {
    case Distinct                  => q.attr(e, Var(v), v1, v2, a.gs)
    case Fn(fn, Some(i))           => q.attr(e, Var(v), v1, v2, a.gs)
    case Fn(fn, _)                 => q.attr(e, Var(v), v1, v2, a.gs)
    case Length(Some(Fn(fn, _)))   => q.attr(e, Var(v), v1, v2, a.gs).func("count", Var(v2), v3)
    case Length(_)                 => q.attr(e, Var(v), v1, v2, a.gs)
    case Eq(args) if args.size > 1 => q.attr(e, Var(v), v1, v2, a.gs)
    case Eq((arg: String) :: Nil)  => q.attr(e, Var(v3), v1, v2, a.gs).func("=", Seq(Var(v3), Val(arg)))
    case _                         => q.attr(e, Var(v), v1, v2, a.gs)
  }

  def resolveGenericAttrMandatory(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = {
    val gs = a.gs
    a.value match {
      case Distinct                  => q.attr(e, Var(v), v1, v2, gs).find("distinct", Nil, v2, gs, v).widh(e)
      case Fn(fn, Some(i))           => q.attr(e, Var(v), v1, v2, gs).find(fn, Seq(i), v2, gs)
      case Fn(fn, _)                 => q.attr(e, Var(v), v1, v2, gs).find(fn, Nil, v2, gs)
      case Length(Some(Fn(fn, _)))   => q.attr(e, Var(v), v1, v2, gs).func("count", Var(v2), v3).find(fn, Nil, v3, gs)
      case Length(_)                 => q.attr(e, Var(v), v1, v2, gs).find("count", Nil, v2, gs)
      case Eq(args) if args.size > 1 => q.attr(e, Var(v), v1, v2, gs).find(v2, gs, v)
      case Eq((arg: String) :: Nil)  => q.attr(e, Var(v3), v1, v2, gs).func("=", Seq(Var(v3), Val(arg))).find(v2, gs, v3)
      case _                         => q.attr(e, Var(v), v1, v2, gs).find(v2, gs, v)
    }
  }

  def resolveGenericNsTacit(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = {
    val gs = a.gs
    a.value match {
      case Qm                        => q.ns(e, Var(v), v1, v2, gs).in(v2, "ns", "?", v2)
      case Distinct                  => q.ns(e, Var(v), v1, v2, gs)
      case Fn(fn, Some(i))           => q.ns(e, Var(v), v1, v2, gs)
      case Fn(fn, _)                 => q.ns(e, Var(v), v1, v2, gs)
      case Length(Some(Fn(fn, _)))   => q.ns(e, Var(v), v1, v2, gs).func("count", Var(v2), v3)
      case Length(_)                 => q.ns(e, Var(v), v1, v2, gs)
      case Eq(args) if args.size > 1 => q.ns(e, Var(v), v1, v2, gs)
      case Eq((arg: String) :: Nil)  => q.ns(e, Var(v), v1, v2, gs).func("=", Seq(Var(v2), Val(arg)))
      case _                         => q.ns(e, Var(v), v1, v2, gs)
    }
  }

  def resolveGenericNsMandatory(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = {
    val gs = a.gs
    a.value match {
      case Distinct                  => q.ns(e, Var(v), v1, v2, gs).find("distinct", Nil, v2, gs, v)
      case Fn(fn, Some(i))           => q.ns(e, Var(v), v1, v2, gs).find(fn, Seq(i), v2, gs)
      case Fn(fn, _)                 => q.ns(e, Var(v), v1, v2, gs).find(fn, Nil, v2, gs)
      case Length(Some(Fn(fn, _)))   => q.ns(e, Var(v), v1, v2, gs).func("count", Var(v2), v3).find(fn, Nil, v3, gs)
      case Length(_)                 => q.ns(e, Var(v), v1, v2, gs).find("count", Nil, v2, gs)
      case Eq(args) if args.size > 1 => q.ns(e, Var(v), v1, v2, gs).find(v2, gs, v)
      case Eq((arg: String) :: Nil)  => q.ns(e, Var(v), v1, v2, gs).func("=", Seq(Var(v2), Val(arg))).find(v2, gs, v3)
      case _                         => q.ns(e, Var(v), v1, v2, gs).find(v2, gs, v)
    }
  }

  def resolveAtomKeyedMapOptional(q: Query, e: String, a0: Atom): Query = {
    val a = a0.copy(name = a0.name.slice(0, a0.name.length - 2))
    val gs = a.gs
    a.value match {
      case VarValue     => q.pull(e, a)
      case Fn("not", _) => q.pull(e, a).not(e, a) // None
      case other        => throw new Model2QueryException("Unresolved optional mapped Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomKeyedMapTacit(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, key: String): Query = {
    val a = a0.copy(name = a0.name.slice(0, a0.name.length - 2))
    val gs = a.gs
    a.value match {
      case Qm => q
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Fulltext(Seq(Qm)) => q
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Neq(Seq(Qm)) => q
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"(?!($key)@("), Var(v + "Value"), Val(")$).*")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Gt(Qm) => q.mapIn2(e, a, v, gs).mapInCompareToK(">", e, a, v, key, gs)
      case Ge(Qm) => q.mapIn2(e, a, v, gs).mapInCompareToK(">=", e, a, v, key, gs)
      case Lt(Qm) => q.mapIn2(e, a, v, gs).mapInCompareToK("<", e, a, v, key, gs)
      case Le(Qm) => q.mapIn2(e, a, v, gs).mapInCompareToK("<=", e, a, v, key, gs)

      case Gt(arg) => q.mapCompareTo(">", e, a, v, Seq(key), arg, gs)
      case Ge(arg) => q.mapCompareTo(">=", e, a, v, Seq(key), arg, gs)
      case Lt(arg) => q.mapCompareTo("<", e, a, v, Seq(key), arg, gs)
      case Le(arg) => q.mapCompareTo("<=", e, a, v, Seq(key), arg, gs)

      case VarValue => q
        .where(e, a, v, gs)
        .func(".startsWith ^String", Seq(Var(v), Val(key + "@")), NoBinding)
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v1)))
        .func("second", Seq(Var(v1)), ScalarBinding(Var(v2)))

      case Fulltext(args) => q
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@.*(" + args.map(f).mkString("|") + ").*$")))

      case Eq(arg :: Nil) => q
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@" + f(arg))))

      case Eq(args) => q
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@(" + args.map(f).mkString("|") + ")$")))

      case other => throw new Model2QueryException(s"Unresolved tacit mapped Atom_:\nAtom_   : $a\nElement: $other")
    }
  }

  def resolveAtomKeyedMapMandatory(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, v3: String, key: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case Qm => q
        .find(v3, gs)
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Fulltext(Seq(Qm)) => q
        .find(v3, gs)
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Neq(Seq(Qm)) => q
        .find(v3, gs)
        .in(e, a, None, v + "Value")
        .where(e, a, v, gs)
        .func("str", Seq(Val(s"(?!($key)@("), Var(v + "Value"), Val(")$).*")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Gt(Qm) => q.find(v2, gs).mapIn2(e, a, v, gs).mapInCompareToK(">", e, a, v, key, gs)
      case Ge(Qm) => q.find(v2, gs).mapIn2(e, a, v, gs).mapInCompareToK(">=", e, a, v, key, gs)
      case Lt(Qm) => q.find(v2, gs).mapIn2(e, a, v, gs).mapInCompareToK("<", e, a, v, key, gs)
      case Le(Qm) => q.find(v2, gs).mapIn2(e, a, v, gs).mapInCompareToK("<=", e, a, v, key, gs)

      case Gt(arg) => q.find(v2, gs).mapCompareTo(">", e, a, v, Seq(key), arg, gs)
      case Ge(arg) => q.find(v2, gs).mapCompareTo(">=", e, a, v, Seq(key), arg, gs)
      case Lt(arg) => q.find(v2, gs).mapCompareTo("<", e, a, v, Seq(key), arg, gs)
      case Le(arg) => q.find(v2, gs).mapCompareTo("<=", e, a, v, Seq(key), arg, gs)

      case VarValue => q
        .find(v2, gs)
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@.*")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v1)))
        .func("second", Seq(Var(v1)), ScalarBinding(Var(v2)))

      case Fulltext(args) => q
        .find(v2, gs)
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@.*(" + args.map(f).mkString("|") + ").*$")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Eq(arg :: Nil) => q
        .find(v2, gs)
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@" + f(arg))))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Eq(args) => q
        .find(v2, gs)
        .where(e, a, v, gs)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@(" + args.map(f).mkString("|") + ")$")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Neq(args) => q.find(v, gs).where(e, a, v, gs).matches(v, Seq(key), "(?!(" + args.map(f).mkString("|") + ")$).*")

      case other => throw new Model2QueryException(s"Unresolved mapped Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveAtomMapOptional(q: Query, e: String, a0: Atom, v: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case VarValue           => q.pull(e, a)
      case Fn("not", _)       => q.pull(e, a).not(e, a)
      case MapEq(pair :: Nil) => q.findD(v, gs).where(e, a, v, gs).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")$")
      case MapEq(pairs)       => q.findD(v, gs).where(e, a, v, gs).mappings(v, a, pairs)
      case other              => throw new Model2QueryException("Unresolved optional mapped Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomMapTacit(q: Query, e: String, a0: Atom, v: String, keys: Seq[String]): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case Qm                       => q.mapIn(e, a, v, gs).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Fulltext(Seq(Qm))        => q.mapIn(e, a, v, gs).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Neq(Seq(Qm))             => q.mapIn(e, a, v, gs).matchRegEx(v, Seq(Val("(?!("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")$).*")))
      case Gt(Qm)                   => q.mapIn(e, a, v, gs).mapInCompareTo(">", e, a, v, gs)
      case Ge(Qm)                   => q.mapIn(e, a, v, gs).mapInCompareTo(">=", e, a, v, gs)
      case Lt(Qm)                   => q.mapIn(e, a, v, gs).mapInCompareTo("<", e, a, v, gs)
      case Le(Qm)                   => q.mapIn(e, a, v, gs).mapInCompareTo("<=", e, a, v, gs)
      case Gt(arg)                  => q.mapCompareTo(">", e, a, v, keys, arg, gs)
      case Ge(arg)                  => q.mapCompareTo(">=", e, a, v, keys, arg, gs)
      case Lt(arg)                  => q.mapCompareTo("<", e, a, v, keys, arg, gs)
      case Le(arg)                  => q.mapCompareTo("<=", e, a, v, keys, arg, gs)
      case VarValue                 => q.where(e, a, v, gs)
      case Fulltext(arg :: Nil)     => q.where(e, a, v, gs).matches(v, keys, ".*" + f(arg) + ".*")
      case Fulltext(args)           => q.where(e, a, v, gs).matches(v, keys, ".*(" + args.map(f).mkString("|") + ").*")
      case Eq((set: Set[_]) :: Nil) => q.where(e, a, v, gs).matches(v, keys, "(" + set.toSeq.map(f).mkString("|") + ")$")
      case Eq(arg :: Nil)           => q.where(e, a, v, gs).matches(v, keys, "(" + f(arg) + ")")
      case Eq(args)                 => q.where(e, a, v, gs).matches(v, keys, "(" + args.map(f).mkString("|") + ")$")
      case Neq(args)                => q.where(e, a, v, gs).matches(v, keys, "(?!(" + args.map(f).mkString("|") + ")$).*")
      case MapKeys(arg :: Nil)      => q.where(e, a, v, gs).func(".startsWith ^String", Seq(Var(v), Val(arg + "@")), NoBinding)
      case MapKeys(args)            => q.where(e, a, v, gs).matches(v, "(" + args.mkString("|") + ")@.*")
      case MapEq(pair :: Nil)       => q.where(e, a, v, gs).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")")
      case MapEq(pairs)             => q.where(e, a, v, gs).mappings(v, a, pairs.toSeq)
      case And(args)                => q.where(e, a, v, gs).matches(v, keys, "(" + args.head + ")$") // (post-processed)
      case Fn("not", _)             => q.not(e, a)
      case other                    => throw new Model2QueryException(s"Unresolved tacit mapped Atom_:\nAtom_   : $a\nElement: $other")
    }
  }

  def resolveAtomMapMandatory(q: Query, e: String, a: Atom, v: String, keys: Seq[String]): Query = {
    val gs = a.gs
    a.value match {
      case Qm                       => q.findD(v, gs).mapIn(e, a, v, gs).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Fulltext(Seq(Qm))        => q.findD(v, gs).mapIn(e, a, v, gs).matchRegEx(v, Seq(Val(".+@("), Var(v + "Value"), Val(")")))
      case Neq(Seq(Qm))             => q.findD(v, gs).mapIn(e, a, v, gs).matchRegEx(v, Seq(Val("(?!("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")$).*")))
      case Gt(Qm)                   => q.findD(v, gs).mapIn(e, a, v, gs).mapInCompareTo(">", e, a, v, gs)
      case Ge(Qm)                   => q.findD(v, gs).mapIn(e, a, v, gs).mapInCompareTo(">=", e, a, v, gs)
      case Lt(Qm)                   => q.findD(v, gs).mapIn(e, a, v, gs).mapInCompareTo("<", e, a, v, gs)
      case Le(Qm)                   => q.findD(v, gs).mapIn(e, a, v, gs).mapInCompareTo("<=", e, a, v, gs)
      case Gt(arg)                  => q.findD(v, gs).mapCompareTo(">", e, a, v, keys, arg, gs)
      case Ge(arg)                  => q.findD(v, gs).mapCompareTo(">=", e, a, v, keys, arg, gs)
      case Lt(arg)                  => q.findD(v, gs).mapCompareTo("<", e, a, v, keys, arg, gs)
      case Le(arg)                  => q.findD(v, gs).mapCompareTo("<=", e, a, v, keys, arg, gs)
      case VarValue                 => q.findD(v, gs).where(e, a, v, gs)
      case Fulltext(arg :: Nil)     => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, ".*" + f(arg) + ".*")
      case Fulltext(args)           => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, ".*(" + args.map(f).mkString("|") + ").*")
      case Eq((set: Set[_]) :: Nil) => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, "(" + set.toSeq.map(f).mkString("|") + ")$")
      case Eq(arg :: Nil)           => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, "(" + f(arg) + ")")
      case Eq(args)                 => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, "(" + args.map(f).mkString("|") + ")$")
      case Neq(args)                => q.findD(v, gs).where(e, a, v, gs).matches(v, keys, "(?!(" + args.map(f).mkString("|") + ")$).*")
      case MapKeys(arg :: Nil)      => q.findD(v, gs).where(e, a, v, gs).func(".startsWith ^String", Seq(Var(v), Val(arg + "@")), NoBinding)
      case MapKeys(args)            => q.findD(v, gs).where(e, a, v, gs).matches(v, "(" + args.mkString("|") + ")@.*")
      case MapEq(pair :: Nil)       => q.findD(v, gs).where(e, a, v, gs).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")$")
      case MapEq(pairs)             => q.findD(v, gs).where(e, a, v, gs).mappings(v, a, pairs)
      case And(args)                => q.findD(v, gs).whereAnd(e, a, v, args)
      case other                    => throw new Model2QueryException(s"Unresolved mapped Atom:\nAtom   : $a\nElement: $other")
    }
  }


  def resolveAtomEnumOptional2(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, prefix: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case EnumVal      => q.pullEnum(e, a)
      case Fn("not", _) => q.pullEnum(e, a).not(e, a) // None
      case Eq(args)     => q.findD(v2, gs).enum(e, a, v, gs).orRules(e, a, args)
      case other        => throw new Model2QueryException("Unresolved optional cardinality-many enum Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomEnumOptional1(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, prefix: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case EnumVal        => q.pullEnum(e, a)
      case Fn("not", _)   => q.pullEnum(e, a).not(e, a) // None
      case Eq(arg :: Nil) => q.find(v2, gs).enum(e, a, v, gs).where(e, a, Val(prefix + arg), gs)
      case Eq(args)       => q.find(v2, gs).enum(e, a, v, gs).orRules(e, a, args)
      case other          => throw new Model2QueryException("Unresolved optional cardinality-one enum Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomEnumTacit(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, v3: String, prefix: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val gs = a.gs
    a.value match {
      case Qm                                      => q.enum(e, a, v, gs).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                            => q.enum(e, a, v, gs).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                  => q.enum(e, a, v, gs).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                  => q.enum(e, a, v, gs).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                  => q.enum(e, a, v, gs).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                  => q.enum(e, a, v, gs).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                 => q.enum(e, a, v, gs)
      case Neq(args)                               => q.enum(e, a, v, gs).compareToMany("!=", a, v2, args)
      case Fn("not", _)                            => q.not(e, a)
      case Eq(Nil)                                 => q.not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty => q.not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.not(e, a)
      case Eq((set: Set[_]) :: Nil)                => q.enum(e, a, v, gs).whereAnd(e, a, v, set.toSeq.map(prefix + _))
      case Eq(arg :: Nil)                          => q.where(e, a, Val(prefix + arg), gs)
      case Eq(args)                                => q.orRules(e, a, args)
      case And(args) if a.card == 2                => q.enum(e, a, v, gs).whereAnd(e, a, v, args.map(prefix + _))
      case Gt(arg)                                 => q.enum(e, a, v, gs).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                 => q.enum(e, a, v, gs).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                 => q.enum(e, a, v, gs).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                 => q.enum(e, a, v, gs).compareTo("<=", a, v2, Val(arg), 1)
      case other                                   => throw new Model2QueryException(s"Unresolved tacit enum Atom_:\nAtom_  : $a\nElement: $other")
    }
  }

  def resolveAtomEnumMandatory2(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String, prefix: String): Query = {
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case Qm                                          => q.findD(v2, gs).enum(e, a, v, gs).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                                => q.findD(v2, gs).enum(e, a, v, gs).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                      => q.findD(v2, gs).enum(e, a, v, gs).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                      => q.findD(v2, gs).enum(e, a, v, gs).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                      => q.findD(v2, gs).enum(e, a, v, gs).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                      => q.findD(v2, gs).enum(e, a, v, gs).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                     => q.findD(v2, gs).enum(e, a, v, gs)
      case Fn("not", _)                                => q.findD(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq(Nil)                                     => q.findD(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty     => q.findD(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty     => q.findD(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq((set: Set[_]) :: Nil)                    => q.findD(v2, gs).enum(e, a, v, gs).whereAnd(e, a, v, set.toSeq.map(prefix + _))
      case Eq(args)                                    => q.findD(v2, gs).enum(e, a, v, gs).orRules(e, a, args)
      case And(args)                                   => q.findD(v2, gs).whereAndEnum(e, a, v, prefix, args)
      case Neq(Nil)                                    => q.findD(v2, gs).enum(e, a, v, gs)
      case Neq(sets) if sets.head.isInstanceOf[Set[_]] => q.findD(v2, gs).enum(e, a, v, gs).nots(e, a, v2, sets)
      case Neq(arg :: Nil) if uri(t)                   => q.findD(v2, gs).enum(e, a, v, gs).compareTo("!=", a, v2, Val(arg))
      case Neq(arg :: Nil)                             => q.findD(v2, gs).enum(e, a, v, gs).compareTo("!=", a, v2, Val(arg))
      case Neq(args)                                   => q.findD(v2, gs).enum(e, a, v, gs).compareToMany("!=", a, v2, args)
      case Gt(arg)                                     => q.findD(v2, gs).enum(e, a, v, gs).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                     => q.findD(v2, gs).enum(e, a, v, gs).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                     => q.findD(v2, gs).enum(e, a, v, gs).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                     => q.findD(v2, gs).enum(e, a, v, gs).compareTo("<=", a, v2, Val(arg), 1)
      case other                                       => throw new Model2QueryException(s"Unresolved cardinality-many enum Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveAtomEnumMandatory1(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String, prefix: String): Query = {
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case Qm                                      => q.find(v2, gs).enum(e, a, v, gs).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                            => q.find(v2, gs).enum(e, a, v, gs).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                  => q.find(v2, gs).enum(e, a, v, gs).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                  => q.find(v2, gs).enum(e, a, v, gs).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                  => q.find(v2, gs).enum(e, a, v, gs).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                  => q.find(v2, gs).enum(e, a, v, gs).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                 => q.find(v2, gs).enum(e, a, v, gs)
      case Fn("not", _)                            => q.find(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq(Nil)                                 => q.find(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.find(v2, gs).enum(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil)                => q.find(v2, gs).enum(e, a, v, gs).orRules(e, a, seq)
      case Eq(arg :: Nil)                          => q.find(v2, gs).enum(e, a, v, gs).where(e, a, Val(prefix + arg), gs)
      case Eq(args)                                => q.find(v2, gs).enum(e, a, v, gs).orRules(e, a, args)
      case Neq(args)                               => q.find(v2, gs).enum(e, a, v, gs).compareToMany("!=", a, v2, args)
      case Gt(arg)                                 => q.find(v2, gs).enum(e, a, v, gs).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                 => q.find(v2, gs).enum(e, a, v, gs).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                 => q.find(v2, gs).enum(e, a, v, gs).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                 => q.find(v2, gs).enum(e, a, v, gs).compareTo("<=", a, v2, Val(arg), 1)
      case other                                   => throw new Model2QueryException(s"Unresolved cardinality-one enum Atom:\nAtom   : $a\nElement: $other")
    }
  }
  def resolveAtomOptional2(q: Query, e: String, a0: Atom, v: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case VarValue                 => q.pull(e, a)
      case Fn("not", _)             => q.pull(e, a).not(e, a) // None
      case Eq(arg :: Nil) if uri(t) => q.findD(v, gs).func( s"""ground (java.net.URI. "$arg")""", Empty, v).where(e, a, v, Nil)
      case Eq(arg :: Nil)           => q.findD(v, gs).where(e, a, Val(arg), gs).where(e, a, v, Nil)
      case Eq(args)                 => q.findD(v, gs).where(e, a, v, gs).orRules(e, a, args, u(t, v))
      case other                    => throw new Model2QueryException("Unresolved optional cardinality-many Atom$:\nAtom$   : " + s"$a0\nElement: $other")
    }
  }
  def resolveAtomOptional1(q: Query, e: String, a0: Atom, v: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case VarValue                 => q.pull(e, a)
      case Fn("not", _)             => q.pull(e, a).not(e, a) // None
      case Eq(arg :: Nil) if uri(t) => q.find(v, gs).func( s"""ground (java.net.URI. "$arg")""", Empty, v).where(e, a, v, Nil)
      case Eq(arg :: Nil)           => q.find(v, gs).where(e, a, Val(arg), gs).where(e, a, v, Nil)
      case Eq(args)                 => q.find(v, gs).where(e, a, v, gs).orRules(e, a, args, u(t, v))
      case other                    => throw new Model2QueryException("Unresolved optional cardinality-one Atom$:\nAtom$   : " + s"$a0\nElement: $other")
    }
  }


  def resolveAtomTacit(q: Query, e: String, a0: Atom, v: String, v1: String): Query = {
    val a = a0.copy(name = a0.name.init)
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case Qm                                      => q.where(e, a, v, gs).in(e, a, None, v)
      case Neq(Seq(Qm))                            => q.where(e, a, v, gs).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                  => q.where(e, a, v, gs).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                  => q.where(e, a, v, gs).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                  => q.where(e, a, v, gs).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                  => q.where(e, a, v, gs).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                       => q.fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case VarValue                                => q.where(e, a, v, gs).find(gs)
      case Fn("not", _)                            => q.not(e, a)
      case Eq(Nil)                                 => q.not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty => q.not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.not(e, a)
      case Eq((set: Set[_]) :: Nil)                => q.whereAnd(e, a, v, set.toSeq, u(t, v))
      case Eq(arg :: Nil) if uri(t)                => q.where(e, a, v, gs).func( s"""ground (java.net.URI. "$arg")""", Empty, v).find(gs)
      case Eq(arg :: Nil)                          => q.where(e, a, Val(arg), gs).find(gs)
      case Eq(args)                                => q.orRules(e, a, args, u(t, v))
      case Neq(args)                               => q.where(e, a, v, gs).compareToMany("!=", a, v, args)
      case Gt(arg)                                 => q.where(e, a, v, gs).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                 => q.where(e, a, v, gs).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                 => q.where(e, a, v, gs).compareTo("<", a, v, Val(arg))
      case Le(arg)                                 => q.where(e, a, v, gs).compareTo("<=", a, v, Val(arg))
      case And(args) if a.card == 2                => q.whereAnd(e, a, v, args, u(t, v))
      case And(args)                               => q.where(e, a, Val(args.head), gs)
      case Fn("unify", _)                          => q.where(e, a, v, gs)
      case Fulltext(arg :: Nil)                    => q.fulltext(e, a, v, Val(arg))
      case Fulltext(args)                          => q.where(e, a, v, gs).orRules(e, a, args, "", true)
      case other                                   => throw new Model2QueryException(s"Unresolved tacit Atom_:\nAtom_  : $a\nElement: $other")
    }
  }

  def resolveAtomMandatory2(q: Query, e: String, a: Atom, v: String, v1: String, v2: String): Query = {
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case Qm                                          => q.findD(v, gs).where(e, a, v, gs).in(e, a, None, v)
      case Neq(Seq(Qm))                                => q.findD(v, gs).where(e, a, v, gs).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                      => q.findD(v, gs).where(e, a, v, gs).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                      => q.findD(v, gs).where(e, a, v, gs).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                      => q.findD(v, gs).where(e, a, v, gs).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                      => q.findD(v, gs).where(e, a, v, gs).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                           => q.findD(v, gs).fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case VarValue                                    => q.findD(v, gs).where(e, a, v, gs)
      case Fn("not", _)                                => q.findD(v, gs).where(e, a, v, gs).not(e, a)
      case Eq(Nil)                                     => q.findD(v, gs).where(e, a, v, gs).not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty     => q.findD(v, gs).where(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty     => q.findD(v, gs).where(e, a, v, gs).not(e, a)
      case Eq((set: Set[_]) :: Nil)                    => q.findD(v, gs).whereAnd(e, a, v, set.toSeq, u(t, v))
      case Eq(arg :: Nil) if uri(t)                    => q.findD(v, gs).where(e, a, v, Nil).where(e, a, v + "_uri", Nil).func( s"""ground (java.net.URI. "$arg")""", Empty, v + "_uri")
      case Eq(arg :: Nil)                              => q.findD(v, gs).where(e, a, Val(arg), gs).where(e, a, v, Nil)
      case Eq(args)                                    => q.findD(v, gs).where(e, a, v, gs).orRules(e, a, args, u(t, v))
      case Neq(Nil)                                    => q.findD(v, gs).where(e, a, v, gs)
      case Neq(sets) if sets.head.isInstanceOf[Set[_]] => q.findD(v, gs).where(e, a, v, gs).nots(e, a, v, sets)
      case Neq(arg :: Nil) if uri(t)                   => q.findD(v, gs).where(e, a, v, gs).compareTo("!=", a, v, Val(arg))
      case Neq(arg :: Nil)                             => q.findD(v, gs).where(e, a, v, gs).compareTo("!=", a, v, Val(arg))
      case Neq(args)                                   => q.findD(v, gs).where(e, a, v, gs).compareToMany("!=", a, v, args)
      case Gt(arg)                                     => q.findD(v, gs).where(e, a, v, gs).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                     => q.findD(v, gs).where(e, a, v, gs).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                     => q.findD(v, gs).where(e, a, v, gs).compareTo("<", a, v, Val(arg))
      case Le(arg)                                     => q.findD(v, gs).where(e, a, v, gs).compareTo("<=", a, v, Val(arg))
      case And(args)                                   => q.findD(v, gs).whereAnd(e, a, v, args, u(t, v))
      case Fn(fn, _)                                   => q.find(fn, Nil, v, gs).where(e, a, v, gs)
      case Fulltext(args)                              => q.findD(v, gs).where(e, a, v, gs).orRules(e, a, args, "", true)
      case other                                       => throw new Model2QueryException(s"Unresolved cardinality-many Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveAtomMandatory1(q: Query, e: String, a: Atom, v: String, v1: String, v2: String): Query = {
    val (gs, t) = (a.gs, a.tpeS)
    a.value match {
      case Qm                                      => q.find(v, gs).where(e, a, v, gs).in(e, a, None, v)
      case Neq(Seq(Qm))                            => q.find(v, gs).where(e, a, v, gs).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                  => q.find(v, gs).where(e, a, v, gs).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                  => q.find(v, gs).where(e, a, v, gs).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                  => q.find(v, gs).where(e, a, v, gs).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                  => q.find(v, gs).where(e, a, v, gs).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                       => q.find(v, gs).fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case EntValue                                => q.find(e, gs)
      case VarValue                                => q.find(v, gs).where(e, a, v, gs)
      case NoValue                                 => q.find(NoVal, gs).where(e, a, v, gs)
      case Distinct                                => q.find("distinct", Nil, v, gs).where(e, a, v, gs).widh(e)
      case BackValue(backNs)                       => q.find(e, gs).where(v, a.ns, a.name, Var(e), backNs, gs)
      case Fn("not", _)                            => q.find(v, gs).where(e, a, v, gs).not(e, a)
      case Eq(Nil)                                 => q.find(v, gs).where(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.find(v, gs).where(e, a, v, gs).not(e, a)
      case Eq((seq: Seq[_]) :: Nil)                => q.find(v, gs).where(e, a, v, gs).orRules(e, a, seq, u(t, v))
      case Eq(arg :: Nil) if uri(t)                => q.find(v, gs).where(e, a, v, Nil).func( s"""ground (java.net.URI. "$arg")""", Empty, v)
      case Eq(arg :: Nil)                          => q.find(v, gs).where(e, a, v, gs).compareTo("=", a, v, Val(arg))
      case Eq(args)                                => q.find(v, gs).where(e, a, v, gs).orRules(e, a, args, u(t, v))
      case Neq(args)                               => q.find(v, gs).where(e, a, v, gs).compareToMany("!=", a, v, args)
      case Gt(arg)                                 => q.find(v, gs).where(e, a, v, gs).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                 => q.find(v, gs).where(e, a, v, gs).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                 => q.find(v, gs).where(e, a, v, gs).compareTo("<", a, v, Val(arg))
      case Le(arg)                                 => q.find(v, gs).where(e, a, v, gs).compareTo("<=", a, v, Val(arg))
      case Fn(fn, Some(i))                         => q.find(fn, Seq(i), v, gs).where(e, a, v, gs)
      case Fn(fn, _) if coalesce(fn)               => q.find(fn, Nil, v, gs).where(e, a, v, gs).widh(e)
      case Fn(fn, _)                               => q.find(fn, Nil, v, gs).where(e, a, v, gs)
      case Fulltext(arg :: Nil)                    => q.find(v, gs).fulltext(e, a, v, Val(arg))
      case Fulltext(args)                          => q.find(v, gs).fulltext(e, a, v, Var(v1)).orRules(v1, a, args, "", true)
      case Length(Some(Fn(fn, Some(i))))           => q.find(v2, gs).where(e, a, v, gs).cast(v, v1).func("count", Var(v1), v2)
      case Length(Some(Fn(fn, _)))                 => q.find(fn, Nil, v2, gs).where(e, a, v, gs).cast(v, v1).func("count", Var(v1), v2)
      case Length(_)                               => q.find(v2, gs).where(e, a, v, gs).cast(v, v1).func("count", Var(v1), v2)
      case other                                   => throw new Model2QueryException(s"Unresolved cardinality-one Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def coalesce(fn: String): Boolean = Seq("sum", "count", "count-distinct", "median", "avg", "variance", "stddev").contains(fn)

  def uri(t: String): Boolean = t.contains("java.net.URI")

  def u(t: String, v: String): String = if (t.contains("java.net.URI")) v else ""

  def nextChar(str: String, inc: Int): String = {
    val chars = str.toCharArray
    val (pre, cur) = if (chars.size == 2) (chars.head, chars.last) else ('-', chars.head)
    (pre, cur, inc) match {
      case (_, _, i) if i > 2 => throw new Model2QueryException("Can't increment more than 2")
      case ('-', 'y', 2)      => "aa"
      case ('-', 'z', 2)      => "ab"
      case ('-', 'z', 1)      => "aa"
      case ('-', c, i)        => (c + i).toChar.toString
      case ('z', _, _)        => throw new Model2QueryException("Ran out of vars...")
      case (p, 'y', 2)        => (p + 1).toChar.toString + "a"
      case (p, 'z', 2)        => (p + 1).toChar.toString + "b"
      case (p, 'z', 1)        => (p + 1).toChar.toString + "a"
      case (p, c, i)          => p.toString + (c + i).toChar
    }
  }

  // Process And-semantics (self-joins)
  def postProcess(model: Model, q: Query): Query = {

    val andAtoms: Seq[Atom] = model.elements.collect {
      case a@Atom(_, _, _, card, And(_), _, _, _) if card == 1 || card == 3 => a
    }

    if (andAtoms.size > 1)
      throw new Model2QueryException("For now, only 1 And-expression can be used. Found: " + andAtoms)

    if (andAtoms.size == 1) {
      val clauses = q.wh.clauses
      val andAtom = andAtoms.head
      val Atom(ns, attr0, _, card, And(andValues), _, _, _) = andAtom
      val attr = if (attr0.last == '_') attr0.init else attr0
      val unifyAttrs = model.elements.collect {
        case a@Atom(ns1, attr1, _, _, _, _, _, _) if a != andAtom => (ns1, if (attr1.last == '_') attr1.init else attr1)
      }

      // The first arg is already modelled in the query
      val selfJoinClauses = andValues.zipWithIndex.tail.flatMap { case (andValue, i) =>

        // Todo: complete matches...
        def vi(v0: Var) = Var(v0.v + "_" + i)
        def queryValue(qv: QueryValue): QueryValue = qv match {
          case Var(v) => vi(Var(v))
          case _      => qv
        }
        def queryTerm(qt: QueryTerm): QueryTerm = qt match {
          case Rule(name, args, cls) => Rule(name, args map queryValue, cls flatMap clause)
          case InVar(b, argss)       => InVar(binding(b), argss)
          case qv: QueryValue        => queryValue(qv)
          case other                 => qt
        }
        def binding(b: Binding): Binding = b match {
          case ScalarBinding(v)     => ScalarBinding(vi(v))
          case CollectionBinding(v) => CollectionBinding(vi(v))
          case TupleBinding(vs)     => TupleBinding(vs map vi)
          case RelationBinding(vs)  => RelationBinding(vs map vi)
          case _                    => b
        }
        def clause(cl: Clause): Seq[Clause] = cl match {
          case dc: DataClause => dataClauses(dc)
          case other          => makeSelfJoinClauses(other)
        }
        def dataClauses(dc: DataClause): Seq[Clause] = dc match {
          case DataClause(ds, e, a@KW(ns2, attr2, _), Var(v), tx, op) if (ns, attr) == (ns2, attr2) && card == 3 =>
            // Add next And-value
            Seq(
              DataClause(ds, vi(e), a, Var(v + "_" + i), queryTerm(tx), queryTerm(op)),
              Funct(".matches ^String", List(Var(v + "_" + i), Val(".+@(" + andValue + ")$")), NoBinding)
            )

          case DataClause(ds, e, a@KW(ns2, attr2, _), _, tx, op) if (ns, attr) == (ns2, attr2) =>
            // Add next And-value
            Seq(DataClause(ds, vi(e), a, Val(andValue), queryTerm(tx), queryTerm(op)))

          case DataClause(ds, e, a@KW(ns2, attr2, _), v, tx, op) if unifyAttrs.contains((ns2, attr2)) =>
            // Keep value-position value to unify
            Seq(DataClause(ds, vi(e), a, v, queryTerm(tx), queryTerm(op)))

          case DataClause(ds, e, a, v, tx, op) =>
            // Add i to variables
            Seq(DataClause(ds, vi(e), a, queryValue(v), queryTerm(tx), queryTerm(op)))
        }
        def makeSelfJoinClauses(expr: QueryExpr): Seq[Clause] = expr match {
          case dc@DataClause(ds, e, a, v, tx, op)                  => dataClauses(dc)
          case RuleInvocation(name, args)                          => Seq(RuleInvocation(name, args map queryTerm))
          case Funct(".startsWith ^String", List(_, _), NoBinding) => Nil
          case Funct(".matches ^String", List(_, _), NoBinding)    => Nil
          case Funct("second", ins, outSame)                       => Seq(Funct("second", ins map queryTerm, outSame))
          case Funct(name, ins, outs)                              => Seq(Funct(name, ins map queryTerm, binding(outs)))
        }
        clauses flatMap makeSelfJoinClauses
      }
      q.copy(wh = Where(q.wh.clauses ++ selfJoinClauses))
    } else q
  }
}