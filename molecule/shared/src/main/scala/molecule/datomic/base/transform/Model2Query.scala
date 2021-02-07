package molecule.datomic.base.transform

import molecule.core.ast.elements._
import molecule.datomic.base.ast.query._
import molecule.datomic.base.ops.QueryOps._
import molecule.datomic.base.transform.exception.Model2QueryException
import molecule.core.util.Helpers


/** Model to Query transformation.
 * <br><br>
 * Second transformation in Molecules series of transformations from
 * custom boilerplate DSL constructs to Datomic queries:
 * <br><br>
 * Custom DSL molecule --> Model --> Query --> Datomic query string
 *
 * */
object Model2Query extends Helpers {

  var nestedEntityClauses: List[Funct] = List.empty[Funct]
  var nestedEntityVars   : List[Var]   = List.empty[Var]
  var nestedLevel        : Int         = 1
  var _model             : Model       = null
  val datomGeneric                     =
    Seq("e", "e_", "tx", "t", "txInstant", "op", "tx_", "t_", "txInstant_", "op_", "a", "a_", "v", "v_")

  def abort(msg: String): Nothing = throw new Model2QueryException(msg)


  def apply(model: Model): (Query, Option[Query], Query, Option[Query]) = {

    // reset on each apply
    nestedEntityClauses = Nil
    nestedLevel = 1
    _model = model

    // Resolve elements
    val query = model.elements.foldLeft((Query(), "a", "b", "", "", "")) {
      case ((query_, e, v, prevNs, prevAttr, prevRefNs), element) => make(model, query_, element, e, v, prevNs, prevAttr, prevRefNs)
    }._1

    // Resolve AND clauses
    val query2 = postProcess(model, query)

    // Resolve redundant `with:` variables
    val withVars = query2.wi.variables
    val query3   = if (withVars.isEmpty) query2 else {
      val outVars              = query2.f.outputs.collect { case Var(v) => v }
      val nonRedundantWithVars = withVars.diff(outVars)
      query2.copy(wi = With(nonRedundantWithVars))
    }

    // Resolve nested
    val query3nested  : Query         = query3.copy(
      f = Find(nestedEntityVars ++ query2.f.outputs),
      wh = Where(query2.wh.clauses ++ nestedEntityClauses)
    )
    val quer3nestedOpt: Option[Query] = if (nestedEntityClauses.nonEmpty) Some(query3nested) else None

    (QueryOptimizer(query3), quer3nestedOpt.map(QueryOptimizer.apply), query3, quer3nestedOpt)
  }


  // Make =======================================================================================================

  def make(model: Model, query: Query, element: Element, e: String, v: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val (w, y) = (nextChar(v, 1), nextChar(v, 2))
    element match {
      case atom: Atom             => makeAtom(query, atom, e, v, w, prevNs, prevAttr, prevRefNs)
      case bond: Bond             => makeBond(query, bond, e, v, w, prevNs, prevAttr, prevRefNs)
      case rb: ReBond             => makeReBond(model, query, rb, v)
      case g: Generic             => makeGeneric(query, g, e, v, w, y, prevNs, prevAttr, prevRefNs)
      case txMetaData: TxMetaData => makeTxMetaData(model, query, txMetaData, w, prevNs, prevAttr, prevRefNs)
      case nested: Nested         =>
        if (!nested.bond.refAttr.endsWith("$")) {
          if (nestedEntityClauses.isEmpty) {
            nestedEntityVars = List(Var("sort0"))
            nestedEntityClauses = List(Funct("identity", Seq(Var(e)), ScalarBinding(Var("sort0"))))
          }
          // Next level
          nestedEntityVars = nestedEntityVars :+ Var("sort" + nestedEntityClauses.size)
          nestedEntityClauses = nestedEntityClauses :+ Funct("identity", Seq(Var(w)), ScalarBinding(Var("sort" + nestedEntityClauses.size)))
        }
        makeNested(model, query, nested, e, v, w, prevNs, prevAttr, prevRefNs)
      case composite: Composite   => makeComposite(model, query, composite, e, v, prevNs, prevAttr, prevRefNs)
      case Self                   => (query, w, y, prevNs, prevAttr, prevRefNs)
      case other                  => abort("Unresolved query variables from model: " + (other, e, v, prevNs, prevAttr, prevRefNs))
    }
  }

  def makeAtom(query: Query, atom: Atom, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val (nsFull, attr) = (atom.nsFull, atom.attr)
    atom match {
      case Atom(_, _, _, _, Fn("unify", _), _, _, _) => makeAtomUnify(query, atom, nsFull, attr, e, v, w, prevNs)
      case Atom(_, _, "a", _, _, _, _, _)            => (resolve(query, e, v, atom), e, w, nsFull, attr, "")
      case Atom(_, _, "ns", _, _, _, _, _)           => (resolve(query, e, v, atom), e, w, nsFull, attr, "")
      case _ if prevRefNs == "IndexVal"              => (resolve(query, e, w, atom), e, w, nsFull, attr, "")
      case Atom(`prevRefNs`, _, _, _, _, _, _, _)    => (resolve(query, v, w, atom), v, w, nsFull, attr, "")
      case Atom(`prevAttr`, _, _, _, _, _, _, _)     => (resolve(query, v, w, atom), v, w, nsFull, attr, "")
      case Atom(`prevNs`, _, _, _, _, _, _, _)       => (resolve(query, e, w, atom), e, w, nsFull, attr, "")
      case _ if datomGeneric.contains(prevAttr)      => (resolve(query, e, w, atom), e, w, nsFull, attr, "")
      case _                                         => (resolve(query, e, v, atom), e, v, nsFull, attr, "")
    }
  }

  def makeBond(query: Query, bond: Bond, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = bond match {
    case Bond(`prevNs`, `prevAttr`, refNs, _, bi: Bidirectional)               => (resolve(query, v, w, bond), v, w, prevNs, prevAttr, refNs)
    case Bond(`prevNs`, `prevAttr`, refNs, _, _)                               => (resolve(query, v, w, bond), v, w, prevNs, prevAttr, refNs)
    case Bond(`prevNs`, refAttr, refNs, _, _)                                  => (resolve(query, e, w, bond), e, w, prevNs, refAttr, refNs)
    case Bond(`prevAttr`, refAttr, refNs, _, _)                                => (resolve(query, v, w, bond), v, w, prevAttr, refAttr, refNs)
    case Bond(`prevRefNs`, refAttr, refNs, _, _)                               => (resolve(query, v, w, bond), v, w, prevRefNs, refAttr, refNs)
    case Bond(nsFull, refAttr, refNs, _, _) if datomGeneric.contains(prevAttr) => (resolve(query, e, w, bond), e, w, nsFull, refAttr, refNs)
    case Bond(nsFull, refAttr, refNs, _, _)                                    => (resolve(query, e, v, bond), e, v, nsFull, refAttr, refNs)
  }

  def makeAtomUnify(query: Query, a: Atom, nsFull: String, attr: String, e: String, v: String, w: String, prevNs: String)
  : (Query, String, String, String, String, String) = {
    val attr1 = if (attr.last == '_') attr.init else attr
    // Find previous matching value that we want to unify with (from an identical attribute)
    query.wh.clauses.reverse.collectFirst {
      // Having a value var to unify with
      case DataClause(_, _, KW(ns0, attr0, _), Var(v0), _, _) if ns0 == nsFull && attr0 == attr1 => nsFull match {
        case `prevNs` => (resolve(query, e, v0, a), e, v, nsFull, attr, "")
        case _        => (resolve(query, v, v0, a), v, v, nsFull, attr, "")
      }

      // Missing value var to unify with
      case dc@DataClause(_, _, KW(ns0, attr0, _), _, _, _) if ns0 == nsFull && attr0 == attr1 =>
        // Add initial clause to have a var to unify with
        val initialClause = dc.copy(v = Var(w))
        val newWhere      = query.wh.copy(clauses = query.wh.clauses :+ initialClause)
        (resolve(query.copy(wh = newWhere), v, w, a), v, w, nsFull, attr, "")
    } getOrElse {
      abort(s"Can't find previous attribute matching unifying attribute `$nsFull.$attr` in query so far:\n$query\nATOM: $a")
    }
  }

  def makeReBond(model: Model, query: Query, rb: ReBond, v: String)
  : (Query, String, String, String, String, String) = {
    val backRef  = rb.backRef
    val backRefE = query.wh.clauses.reverse.collectFirst {
      case DataClause(_, Var(backE), a, Var(_), _, _) if a.nsFull == backRef => backE
    } getOrElse {
      abort(s"Can't find back reference namespace `$backRef` in query so far:\n$model\n---------\n$query\n---------\n$rb")
    }
    (query, backRefE, v, backRef, "", "")
  }

  def makeGeneric(query: Query, g: Generic, e: String, v: String, w: String, y: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = if (prevRefNs.nonEmpty) {
    // Advance variable letters to next namespace
    (resolve(query, v, w, g), v, y, g.tpe, g.attr, "")
  } else {
    (resolve(query, e, v, g), e, v, g.tpe, g.attr, "")
  }

  def makeTxMetaData(model: Model, query0: Query, txMetaData: TxMetaData, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    // Ensure tx variable is present in previous DataClause
    val (query, txV) = {
      val (cls1, txV) = query0.wh.clauses.foldRight(Seq.empty[Clause], "") {
        case (dcl@DataClause(_, _, KW("db", "ident", _), _, _, _), (cls, "")) => (dcl +: cls, "")
        case (dcl@DataClause(_, _, _, Var(v), Var("_"), _), (cls, ""))        => (dcl.copy(tx = Var(v + "_tx")) +: cls, v + "_tx")
        case (dcl@DataClause(_, _, _, _, Var(tx), _), (cls, ""))              => (dcl +: cls, tx)
        case (dcl: DataClause, (cls, ""))                                     => (dcl.copy(tx = Var("tx")) +: cls, "tx")
        case (cl, (cls, tx))                                                  => (cl +: cls, tx)
      }
      if (txV.isEmpty)
        abort("Couldn't attach tx to any DataClause in query:\n" + query0)
      (query0.copy(wh = Where(cls1)), txV)
    }

    val (q2, e2, v2, prevNs2, prevAttr2, prevRefNs2) = txMetaData.elements.foldLeft((query, txV, w, prevNs, prevAttr, prevRefNs)) {
      case ((q1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element) => make(model, q1, element, e1, v1, prevNs1, prevAttr1, prevRefNs1)
    }
    (q2, e2, nextChar(v2, 1), prevNs2, prevAttr2, prevRefNs2)
  }

  def makeNested(model: Model, query: Query, nested: Nested, e: String, v: String, w: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    val Nested(b@Bond(nsFull, refAttr, _, _, _), elements) = nested
    if (refAttr.endsWith("$")) {
      // Optional nested values - pull

      def optTac(attr: String): Boolean = attr.endsWith("$") || attr.endsWith("_")

      // Recursively resolve whole nested pull structure here
      def getNestedAttrs(elements: Seq[Element]): Seq[PullAttrSpec] = {
        elements.foldLeft(Seq.empty[PullAttrSpec], elements) {
          // Abort recursion when done on this level
          case ((acc, Nil), _) => (acc, Nil)

          // Nested attributes
          case ((acc, rem), Atom(nsFull1, attr, _, _, _, None, _, _)) if optTac(attr) => (acc :+ PullAttr(nsFull1, clean(attr), true), rem.tail)
          case ((acc, rem), Atom(nsFull1, attr, _, _, _, None, _, _))                 => (acc :+ PullAttr(nsFull1, attr, false), rem.tail)
          case ((acc, rem), Atom(nsFull1, attr, _, _, _, _, _, _)) if optTac(attr)    => (acc :+ PullEnum(nsFull1, clean(attr), true), rem.tail)
          case ((acc, rem), Atom(nsFull1, attr, _, _, _, _, _, _))                    => (acc :+ PullEnum(nsFull1, attr, false), rem.tail)

          // Card 1 ref within nested structure is represented as a nested structure itself
          case ((acc, rem), Bond(prevNs, refAttr, _, _, _)) =>
            nestedLevel += 1
            (acc :+ NestedAttrs(nestedLevel, prevNs, clean(refAttr), getNestedAttrs(rem.tail)), Nil)

          // Always last
          case ((acc, _), Nested(Bond(nsFull1, refAttr1, _, _, _), elements1)) =>
            nestedLevel += 1
            (acc :+ NestedAttrs(nestedLevel, nsFull1, clean(refAttr1), getNestedAttrs(elements1)), Nil)
        }
      }._1

      val nestedE    = if (model.elements.init.last.isInstanceOf[Bond]) v else e
      val pullScalar = nestedE + "__" + query.f.outputs.length
      val pullNested = PullNested(pullScalar, NestedAttrs(nestedLevel, nsFull, clean(refAttr), getNestedAttrs(elements)))
      val find2      = query.f.copy(outputs = query.f.outputs :+ pullNested)
      val q2         = query
        .copy(f = find2)
        .func("identity", Seq(Var(nestedE)), ScalarBinding(Var(pullScalar)))
      (q2, "", "", "", "", "")
    } else {
      // Mandatory nested values - where clauses
      val (e2, elements2)                 = if (nsFull == "") (e, elements) else (w, b +: elements)
      val (q2, _, v2, ns2, attr2, refNs2) = elements2.foldLeft((query, e, v, prevNs, prevAttr, prevRefNs)) {
        case ((query1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element1) =>
          make(model, query1, element1, e1, v1, prevNs1, prevAttr1, prevRefNs1)
      }
      (q2, e2, nextChar(v2, 1), ns2, attr2, refNs2)
    }
  }

  def makeComposite(model: Model, query: Query, composite: Composite, e: String, v: String, prevNs: String, prevAttr: String, prevRefNs: String)
  : (Query, String, String, String, String, String) = {
    def getFirstEid(clauses: Seq[Clause]): String = clauses.head match {
      case DataClause(_, Var(compositeEid), KW(nsFull, _, _), _, _, _) if nsFull != "db" => compositeEid
      case _: Funct                                                                      => getFirstEid(clauses.tail)
      case other                                                                         =>
        abort(s"Unexpected first clause of composite query: " + other + "\n" + query.wh.clauses.mkString("\n"))
    }
    val eid: String = if (query.wh.clauses.isEmpty) e else getFirstEid(query.wh.clauses)

    val (q2, e2, v2, prevNs2, prevAttr2, prevRefNs2) = composite.elements.foldLeft((query, eid, v, prevNs, prevAttr, prevRefNs)) {
      case ((q1, e1, v1, prevNs1, prevAttr1, prevRefNs1), element) => make(model, q1, element, e1, v1, prevNs1, prevAttr1, prevRefNs1)
    }
    (q2, e2, nextChar(v2, 1), prevNs2, prevAttr2, prevRefNs2)
  }


  // Resolve =======================================================================================================

  def resolve(q: Query, e: String, v: String, element: Element): Query = {
    val (v1: String, v2: String, v3: String) = (v + 1, v + 2, v + 3)
    element match {
      case atom: Atom                         => resolveAtom(q, e, atom, v, v1, v2, v3)
      case Bond(nsFull, refAttr, refNs, _, _) => q.ref(e, nsFull, refAttr, v, refNs)
      case generic: Generic                   => resolveGeneric(q, e, generic, v, v1, v2, v3)
      case ReBond(backRef)                    => q.ref(e, backRef, "", v, "")
      case unresolved                         => abort("Unresolved model: " + unresolved)
    }
  }

  def resolveAtom(q: Query, e: String, a: Atom, v: String, v1: String, v2: String, v3: String): Query = {
    val (opt: Boolean, tacit: Boolean) = (a.attr.last == '$', a.attr.last == '_')
    a match {
      // Manipulation (not relevant to queries, only to transactions)
      case Atom(_, _, _, _, AssertValue(_) | ReplaceValue(_) | RetractValue(_) | AssertMapPairs(_) | ReplaceMapPairs(_) | RetractMapKeys(_), _, _, _) => q

      // Enum
      case a@Atom(_, _, _, 2, _, Some(prefix), _, _) if opt       => resolveEnumOptional2(q, e, a, v, v2)
      case a@Atom(_, _, _, 1, _, Some(prefix), _, _) if opt       => resolveEnumOptional1(q, e, a, v, v2, prefix)
      case a@Atom(_, _, _, 1 | 2, _, Some(prefix), _, _) if tacit => resolveEnumTacit(q, e, a, v, v2, v3, prefix)
      case a@Atom(_, _, _, 2, _, Some(prefix), _, _)              => resolveEnumMandatory2(q, e, a, v, v2, v3, prefix)
      case a@Atom(_, _, _, 1, _, Some(prefix), _, _)              => resolveEnumMandatory1(q, e, a, v, v2, v3, prefix)

      // Atom
      case a@Atom(_, _, _, 2, _, _, _, _) if opt       => resolveAtomOptional2(q, e, a, v, v1)
      case a@Atom(_, _, _, 1, _, _, _, _) if opt       => resolveAtomOptional1(q, e, a, v, v1)
      case a@Atom(_, _, _, 1 | 2, _, _, _, _) if tacit => resolveAtomTacit(q, e, a, v, v1)
      case a@Atom(_, _, _, 2, _, _, _, _)              => resolveAtomMandatory2(q, e, a, v, v1)
      case a@Atom(_, _, _, 1, _, _, _, _)              => resolveAtomMandatory1(q, e, a, v, v1)

      // Mapped attributes
      case a@Atom(_, _, _, 3, _, _, _, _) if opt      => resolveAtomMapOptional(q, e, a, v)
      case a@Atom(_, _, _, 3, _, _, _, keys) if tacit => resolveAtomMapTacit(q, e, a, v, keys)
      case a@Atom(_, _, _, 3, _, _, _, keys)          => resolveAtomMapMandatory(q, e, a, v, keys)

      // Keyed mapped attributes
      case a@Atom(_, _, _, 4, _, _, _, _) if opt            => resolveAtomKeyedMapOptional(q, e, a)
      case a@Atom(_, _, _, 4, _, _, _, key :: Nil) if tacit => resolveAtomKeyedMapTacit(q, e, a, v, v1, v2, key)
      case a@Atom(_, _, _, 4, _, _, _, key :: Nil)          => resolveAtomKeyedMapMandatory(q, e, a, v, v1, v2, v3, key)
      case a                                                => abort("Unexpected Atom: " + a)
    }
  }


  def resolveGeneric(q: Query, e: String, g: Generic, v: String, v1: String, v2: String, v3: String): Query = g.tpe match {
    case "schema" => resolveSchema(q, g)
    case "datom"  => resolveDatom(q, e, g, v, v1)
    case _        => q // Indexes are handled in Conn directly from Model elements
  }


  // Schema ....................................................................................

  def resolveSchema(q: Query, g: Generic): Query = g.attr match {
    case "id"          => resolveSchemaMandatory(g, q.schema, "Long")
    case "a"           => resolveSchemaMandatory(g, q.schemaA, "String")
    case "part"        => resolveSchemaMandatory(g, q.schema, "String")
    case "nsFull"      => resolveSchemaMandatory(g, q.schema, "String")
    case "ns"          => resolveSchemaMandatory(g, q.schema, "String")
    case "attr"        => resolveSchemaMandatory(g, q.schemaAttr, "String")
    case "tpe"         => resolveSchemaMandatory(g, q.schemaTpe, "String")
    case "card"        => resolveSchemaMandatory(g, q.schemaCard, "String")
    case "doc"         => resolveSchemaMandatory(g, q.schemaDoc, "String")
    case "index"       => resolveSchemaMandatory(g, q.schemaIndex, "Boolean")
    case "unique"      => resolveSchemaMandatory(g, q.schemaUnique, "String")
    case "fulltext"    => resolveSchemaMandatory(g, q.schemaFulltext, "Boolean")
    case "isComponent" => resolveSchemaMandatory(g, q.schemaIsComponent, "Boolean")
    case "noHistory"   => resolveSchemaMandatory(g, q.schemaNoHistory, "Boolean")
    case "enum"        => resolveSchemaMandatory(g, q.schemaEnum, "String")
    case "t"           => resolveSchemaMandatory(g, q.schemaT, "Long")
    case "tx"          => resolveSchemaMandatory(g, q.schema, "Long")
    case "txInstant"   => resolveSchemaMandatory(g, q.schemaTxInstant, "java.util.Date")

    case "id_"          => resolveSchemaTacit(g, q.schema, "Long")
    case "a_"           => resolveSchemaTacit(g, q.schemaA, "String")
    case "part_"        => resolveSchemaTacit(g, q.schema, "String")
    case "nsFull_"      => resolveSchemaTacit(g, q.schema, "String")
    case "ns_"          => resolveSchemaTacit(g, q.schema, "String")
    case "attr_"        => resolveSchemaTacit(g, q.schemaAttr, "String")
    case "tpe_"         => resolveSchemaTacit(g, q.schemaTpe, "String")
    case "card_"        => resolveSchemaTacit(g, q.schemaCard, "String")
    case "doc_"         => resolveSchemaTacit(g, q.schemaDoc, "String")
    case "index_"       => resolveSchemaTacit(g, q.schemaIndex, "Boolean")
    case "unique_"      => resolveSchemaTacit(g, q.schemaUnique, "String")
    case "fulltext_"    => resolveSchemaTacit(g, q.schemaFulltext, "Boolean")
    case "isComponent_" => resolveSchemaTacit(g, q.schemaIsComponent, "Boolean")
    case "noHistory_"   => resolveSchemaTacit(g, q.schemaNoHistory, "Boolean")
    case "enum_"        => resolveSchemaTacit(g, q.schemaEnum, "String")
    case "t_"           => resolveSchemaTacit(g, q.schemaT, "Long")
    case "tx_"          => resolveSchemaTacit(g, q.schema, "Long")
    case "txInstant_"   => resolveSchemaTacit(g, q.schemaTxInstant, "java.util.Date")

    case "unique$" => resolveSchemaOptionalUnique(g, q)
    case optional  => resolveSchemaOptional(g, q)
  }

  def resolveSchemaMandatory(g: Generic, q: Query, tpe: String): Query = {
    val v = g.attr
    g.value match {
      case NoValue                        => q.find(v)
      case Eq(args)                       => q.find(v).in(args, v)
      case Neq(args)                      => q.find(v).compareToMany2("!=", v, args)
      case Gt(arg)                        => q.find(v).compareTo2(">", tpe, v, Val(arg), q.wh.clauses.length)
      case Ge(arg)                        => q.find(v).compareTo2(">=", tpe, v, Val(arg), q.wh.clauses.length)
      case Lt(arg)                        => q.find(v).compareTo2("<", tpe, v, Val(arg), q.wh.clauses.length)
      case Le(arg)                        => q.find(v).compareTo2("<=", tpe, v, Val(arg), q.wh.clauses.length)
      case Fn("count", _)                 => q.find("count", Nil, v)
      case Fulltext((arg: String) :: Nil) => q.find(v + "Value").schemaDocFulltext(arg)
      case Fulltext(_)                    => abort("Fulltext search can only be performed with 1 search phrase.")
      case other                          => abort(s"Unexpected value for mandatory schema attribute `$v`: $other")
    }
  }

  def resolveSchemaTacit(g: Generic, q: Query, tpe: String): Query = {
    val v = g.attr.init
    g.value match {
      case NoValue                        => q
      case Eq(args)                       => q.in(args, v)
      case Neq(args)                      => q.compareToMany2("!=", v, args)
      case Gt(arg)                        => q.compareTo2(">", tpe, v, Val(arg), q.wh.clauses.length)
      case Ge(arg)                        => q.compareTo2(">=", tpe, v, Val(arg), q.wh.clauses.length)
      case Lt(arg)                        => q.compareTo2("<", tpe, v, Val(arg), q.wh.clauses.length)
      case Le(arg)                        => q.compareTo2("<=", tpe, v, Val(arg), q.wh.clauses.length)
      case Fulltext((arg: String) :: Nil) => q.schemaDocFulltext(arg)
      case Fulltext(_)                    => abort("Fulltext search can only be performed with 1 search phrase.")
      case other                          => abort(s"Unexpected value for tacit schema attribute `${g.attr}`: $other")
    }
  }

  def resolveSchemaOptional(g: Generic, q: Query): Query = {
    val v = g.attr.init
    g.value match {
      case NoValue        => q.schemaPull(v)
      case Eq(arg :: Nil) => q.find(v).where(Var("id"), KW("db", v), v).where("id", "db", v, Val(arg), "")
      case Fn("not", _)   => q.schemaPull(v).not(v) // None
      case other          => abort(s"Unexpected value for optional schema attribute `${g.attr}`: " + other)
    }
  }

  def resolveSchemaOptionalUnique(g: Generic, q: Query): Query = {
    val v = g.attr.init
    g.value match {
      case NoValue        => q.schemaPullUnique(v)
      case Eq(arg :: Nil) =>
        q.find(v + 2)
          .where(Var("id"), KW("db", v), v)
          .ident(v, v + 1)
          .kw(v + 1, v + 2)
          .func("=", Seq(Var(v + 2), Val(arg)))
      case Fn("not", _)   => q.schemaPull(v).not(v) // None
      case other          => abort(s"Unexpected value for optional schema attribute `${g.attr}`: " + other)
    }
  }


  // Datom ....................................................................................

  def resolveDatom(q: Query, e: String, g: Generic, v: String, v1: String): Query = g.attr match {
    case "e"         =>
      val q1 = q.datomE(e, v, v1, _model.elements.size == 1)
      val w  = if (q1.wh.clauses.exists {
        case DataClause(_, Var(`e`), KW(_, _, refNs), _, _, _) if refNs.nonEmpty => true
        case _                                                                   => false
      }) v else e
      resolveDatomMandatory(q1, g, "Long", "", w)
    case "tx"        => resolveDatomMandatory(q.datomTx(e, v, v1), g, "Long", v)
    case "t"         => resolveDatomMandatory(q.datomT(e, v, v1), g, "Long", v)
    case "txInstant" => resolveDatomMandatory(q.datomTxInstant(e, v, v1), g, "java.util.Date", v)
    case "op"        => resolveDatomMandatory(q.datomOp(e, v, v1), g, "Boolean", v)
    case "a"         => resolveDatomMandatory(q.datomA(e, v, v1), g, "String", v)
    case "v"         =>
      val q1 = q.datomV(e, v, v1)
      val w  = if (q1.wh.clauses.exists {
        case DataClause(_, _, KW(_, attr, _), _, _, _) if attr == e + "_attr" => true
        case _                                                                => false
      }) v else v + "_v"
      resolveDatomMandatory(q1, g, "Any", "", w)

    case "e_"         =>
      val q1 = q.datomE(e, v, v1)
      val w  = if (q1.wh.clauses.exists {
        case DataClause(_, Var(`e`), KW(_, _, refNs), _, _, _) if refNs.nonEmpty => true
        case _                                                                   => false
      }) v else e
      resolveDatomTacit(q1, e, g, "Long", "", w)
    case "tx_"        => resolveDatomTacit(q.datomTx(e, v, v1), e, g, "Long", v)
    case "t_"         => resolveDatomTacit(q.datomT(e, v, v1), e, g, "Long", v)
    case "txInstant_" => resolveDatomTacit(q.datomTxInstant(e, v, v1), e, g, "java.util.Date", v)
    case "op_"        => resolveDatomTacit(q.datomOp(e, v, v1), e, g, "Boolean", v)
    case "a_"         => resolveDatomTacit(q.datomA(e, v, v1), e, g, "String", v)
    case "v_"         =>
      val q1 = q.datomV(e, v, v1)
      val w  = if (q1.wh.clauses.exists {
        case DataClause(_, _, KW(_, attr, _), _, _, _) if attr == e + "_attr" => true
        case _                                                                => false
      }) v else v + "_v"
      resolveDatomTacit(q1, e, g, "Any", "", w)
  }

  def resolveDatomMandatory(q: Query, g: Generic, tpe: String, v0: String, w: String = ""): Query = {
    val v = if (w.nonEmpty) w else v0 + "_" + g.attr
    g.value match {
      case NoValue | EntValue => q.find(v)
      case Eq(args)           => q.find(v).in(args, v)
      case Neq(args)          => q.find(v).compareToMany2("!=", v, args)
      case Gt(arg)            => q.find(v).compareTo2(">", tpe, v, Val(arg), q.wh.clauses.length)
      case Ge(arg)            => q.find(v).compareTo2(">=", tpe, v, Val(arg), q.wh.clauses.length)
      case Lt(arg)            => q.find(v).compareTo2("<", tpe, v, Val(arg), q.wh.clauses.length)
      case Le(arg)            => q.find(v).compareTo2("<=", tpe, v, Val(arg), q.wh.clauses.length)
      case Fn("count", _)     => q.find("count", Nil, v)
      case other              => abort("Unexpected value: " + other)
    }
  }

  def resolveDatomTacit(q: Query, e: String, g: Generic, tpe: String, v0: String, w: String = ""): Query = {
    val v = if (w.nonEmpty) w else v0 + "_" + g.attr.init // skip underscore at end
    g.value match {
      case NoValue | EntValue => q
      case Eq(Seq(Qm))        => q.in(v, g.tpe, g.attr, e)
      case Eq(args)           => q.in(args, v)
      case Neq(args)          => q.compareToMany2("!=", v, args)
      case Gt(arg)            => q.compareTo2(">", tpe, v, Val(arg), q.wh.clauses.length)
      case Ge(arg)            => q.compareTo2(">=", tpe, v, Val(arg), q.wh.clauses.length)
      case Lt(arg)            => q.compareTo2("<", tpe, v, Val(arg), q.wh.clauses.length)
      case Le(arg)            => q.compareTo2("<=", tpe, v, Val(arg), q.wh.clauses.length)
      case other              => abort("Unexpected value: " + other)
    }
  }


  // Map ....................................................................................

  def resolveAtomKeyedMapOptional(q: Query, e: String, a0: Atom): Query = {
    val a = a0.copy(attr = a0.attr.slice(0, a0.attr.length - 2))
    a.value match {
      case VarValue     => q.pull(e, a)
      case Fn("not", _) => q.pull(e, a).not(e, a) // None
      case other        => abort("Unresolved optional mapped Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomKeyedMapTacit(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, key: String): Query = {
    val a = a0.copy(attr = a0.attr.slice(0, a0.attr.length - 2))
    a.value match {
      case Qm => q
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Fulltext(Seq(Qm)) => q
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Neq(Seq(Qm)) => q
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"(?!($key)@("), Var(v + "Value"), Val(")$).*")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))

      case Gt(Qm) => q.mapIn2(e, a, v).mapInCompareToK(">", a, v, key)
      case Ge(Qm) => q.mapIn2(e, a, v).mapInCompareToK(">=", a, v, key)
      case Lt(Qm) => q.mapIn2(e, a, v).mapInCompareToK("<", a, v, key)
      case Le(Qm) => q.mapIn2(e, a, v).mapInCompareToK("<=", a, v, key)

      case Gt(arg) => q.mapCompareTo(">", e, a, v, Seq(key), arg)
      case Ge(arg) => q.mapCompareTo(">=", e, a, v, Seq(key), arg)
      case Lt(arg) => q.mapCompareTo("<", e, a, v, Seq(key), arg)
      case Le(arg) => q.mapCompareTo("<=", e, a, v, Seq(key), arg)

      case VarValue => q
        .where(e, a, v)
        .func(".startsWith ^String", Seq(Var(v), Val(key + "@")), NoBinding)
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v1)))
        .func("second", Seq(Var(v1)), ScalarBinding(Var(v2)))

      case Fulltext(args) => q
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@.*(" + args.map(f).mkString("|") + ").*$")))

      case Eq(arg :: Nil) => q
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@" + f(arg))))

      case Eq(args) => q
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@(" + args.map(f).mkString("|") + ")$")))

      case other => abort(s"Unresolved tacit mapped Atom_:\nAtom_   : $a\nElement: $other")
    }
  }

  def resolveAtomKeyedMapMandatory(q: Query, e: String, a0: Atom, v: String, v1: String, v2: String, v3: String, key: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case Qm => q
        .find(v3)
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Fulltext(Seq(Qm)) => q
        .find(v3)
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"($key)@("), Var(v + "Value"), Val(")")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Neq(Seq(Qm)) => q
        .find(v3)
        .in(e, a, None, v + "Value")
        .where(e, a, v)
        .func("str", Seq(Val(s"(?!($key)@("), Var(v + "Value"), Val(")$).*")), ScalarBinding(Var(v1)))
        .func(".matches ^String", Seq(Var(v), Var(v1)))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 2)))
        .func("second", Seq(Var(v + 2)), ScalarBinding(Var(v + 3)))

      case Gt(Qm) => q.find(v2).mapIn2(e, a, v).mapInCompareToK(">", a, v, key)
      case Ge(Qm) => q.find(v2).mapIn2(e, a, v).mapInCompareToK(">=", a, v, key)
      case Lt(Qm) => q.find(v2).mapIn2(e, a, v).mapInCompareToK("<", a, v, key)
      case Le(Qm) => q.find(v2).mapIn2(e, a, v).mapInCompareToK("<=", a, v, key)

      case Gt(arg) => q.find(v2).mapCompareTo(">", e, a, v, Seq(key), arg)
      case Ge(arg) => q.find(v2).mapCompareTo(">=", e, a, v, Seq(key), arg)
      case Lt(arg) => q.find(v2).mapCompareTo("<", e, a, v, Seq(key), arg)
      case Le(arg) => q.find(v2).mapCompareTo("<=", e, a, v, Seq(key), arg)

      case VarValue => q
        .find(v2)
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@.*")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v1)))
        .func("second", Seq(Var(v1)), ScalarBinding(Var(v2)))

      case Fulltext(args) => q
        .find(v2)
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@.*(" + args.map(f).mkString("|") + ").*$")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Eq(arg :: Nil) => q
        .find(v2)
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val(s"($key)@" + f(arg))))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Eq(args) => q
        .find(v2)
        .where(e, a, v)
        .func(".matches ^String", Seq(Var(v), Val("(" + key + ")@(" + args.map(f).mkString("|") + ")$")))
        .func(".split ^String", Seq(Var(v), Val("@"), Val(2)), ScalarBinding(Var(v + 1)))
        .func("second", Seq(Var(v + 1)), ScalarBinding(Var(v + 2)))

      case Neq(args) => q.find(v).where(e, a, v).matches(v, Seq(key), "(?!(" + args.map(f).mkString("|") + ")$).*")

      case other => abort(s"Unresolved mapped Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveAtomMapOptional(q: Query, e: String, a0: Atom, v: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case VarValue           => q.pull(e, a)
      case Fn("not", _)       => q.pull(e, a).not(e, a)
      case MapEq(pair :: Nil) => q.findD(v).where(e, a, v).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")$")
      case MapEq(pairs)       => q.findD(v).where(e, a, v).mappings(v, a, pairs)
      case other              => abort("Unresolved optional mapped Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveAtomMapTacit(q: Query, e: String, a0: Atom, v: String, keys: Seq[String]): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case Qm                       => q.mapIn(e, a, v).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Fulltext(Seq(Qm))        => q.mapIn(e, a, v).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Neq(Seq(Qm))             => q.mapIn(e, a, v).matchRegEx(v, Seq(Val("(?!("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")$).*")))
      case Gt(Qm)                   => q.mapIn(e, a, v).mapInCompareTo(">", a, v)
      case Ge(Qm)                   => q.mapIn(e, a, v).mapInCompareTo(">=", a, v)
      case Lt(Qm)                   => q.mapIn(e, a, v).mapInCompareTo("<", a, v)
      case Le(Qm)                   => q.mapIn(e, a, v).mapInCompareTo("<=", a, v)
      case Gt(arg)                  => q.mapCompareTo(">", e, a, v, keys, arg)
      case Ge(arg)                  => q.mapCompareTo(">=", e, a, v, keys, arg)
      case Lt(arg)                  => q.mapCompareTo("<", e, a, v, keys, arg)
      case Le(arg)                  => q.mapCompareTo("<=", e, a, v, keys, arg)
      case VarValue                 => q.where(e, a, v)
      case Fulltext(arg :: Nil)     => q.where(e, a, v).matches(v, keys, ".*" + f(arg) + ".*")
      case Fulltext(args)           => q.where(e, a, v).matches(v, keys, ".*(" + args.map(f).mkString("|") + ").*")
      case Eq((set: Set[_]) :: Nil) => q.where(e, a, v).matches(v, keys, "(" + set.toSeq.map(f).mkString("|") + ")$")
      case Eq(arg :: Nil)           => q.where(e, a, v).matches(v, keys, "(" + f(arg) + ")")
      case Eq(args)                 => q.where(e, a, v).matches(v, keys, "(" + args.map(f).mkString("|") + ")$")
      case Neq(args)                => q.where(e, a, v).matches(v, keys, "(?!(" + args.map(f).mkString("|") + ")$).*")
      case MapKeys(arg :: Nil)      => q.where(e, a, v).func(".startsWith ^String", Seq(Var(v), Val(arg + "@")), NoBinding)
      case MapKeys(args)            => q.where(e, a, v).matches(v, "(" + args.mkString("|") + ")@.*")
      case MapEq(pair :: Nil)       => q.where(e, a, v).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")")
      case MapEq(pairs)             => q.where(e, a, v).mappings(v, a, pairs)
      case And(args)                => q.where(e, a, v).matches(v, keys, "(" + args.head + ")$") // (post-processed)
      case Fn("not", _)             => q.not(e, a)
      case other                    => abort(s"Unresolved tacit mapped Atom_:\nAtom_   : $a\nElement: $other")
    }
  }

  def resolveAtomMapMandatory(q: Query, e: String, a: Atom, v: String, keys: Seq[String]): Query = {
    a.value match {
      case Qm                       => q.findD(v).mapIn(e, a, v).matchRegEx(v, Seq(Val("("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")")))
      case Fulltext(Seq(Qm))        => q.findD(v).mapIn(e, a, v).matchRegEx(v, Seq(Val(".+@("), Var(v + "Value"), Val(")")))
      case Neq(Seq(Qm))             => q.findD(v).mapIn(e, a, v).matchRegEx(v, Seq(Val("(?!("), Var(v + "Key"), Val(")@("), Var(v + "Value"), Val(")$).*")))
      case Gt(Qm)                   => q.findD(v).mapIn(e, a, v).mapInCompareTo(">", a, v)
      case Ge(Qm)                   => q.findD(v).mapIn(e, a, v).mapInCompareTo(">=", a, v)
      case Lt(Qm)                   => q.findD(v).mapIn(e, a, v).mapInCompareTo("<", a, v)
      case Le(Qm)                   => q.findD(v).mapIn(e, a, v).mapInCompareTo("<=", a, v)
      case Gt(arg)                  => q.findD(v).mapCompareTo(">", e, a, v, keys, arg)
      case Ge(arg)                  => q.findD(v).mapCompareTo(">=", e, a, v, keys, arg)
      case Lt(arg)                  => q.findD(v).mapCompareTo("<", e, a, v, keys, arg)
      case Le(arg)                  => q.findD(v).mapCompareTo("<=", e, a, v, keys, arg)
      case VarValue                 => q.findD(v).where(e, a, v)
      case Fulltext(arg :: Nil)     => q.findD(v).where(e, a, v).matches(v, keys, ".*" + f(arg) + ".*")
      case Fulltext(args)           => q.findD(v).where(e, a, v).matches(v, keys, ".*(" + args.map(f).mkString("|") + ").*")
      case Eq((set: Set[_]) :: Nil) => q.findD(v).where(e, a, v).matches(v, keys, "(" + set.toSeq.map(f).mkString("|") + ")$")
      case Eq(arg :: Nil)           => q.findD(v).where(e, a, v).matches(v, keys, "(" + f(arg) + ")")
      case Eq(args)                 => q.findD(v).where(e, a, v).matches(v, keys, "(" + args.map(f).mkString("|") + ")$")
      case Neq(args)                => q.findD(v).where(e, a, v).matches(v, keys, "(?!(" + args.map(f).mkString("|") + ")$).*")
      case MapKeys(arg :: Nil)      => q.findD(v).where(e, a, v).func(".startsWith ^String", Seq(Var(v), Val(arg + "@")), NoBinding)
      case MapKeys(args)            => q.findD(v).where(e, a, v).matches(v, "(" + args.mkString("|") + ")@.*")
      case MapEq(pair :: Nil)       => q.findD(v).where(e, a, v).matches(v, "(" + pair._1 + ")@(" + pair._2 + ")$")
      case MapEq(pairs)             => q.findD(v).where(e, a, v).mappings(v, a, pairs)
      case And(args)                => q.findD(v).whereAnd(e, a, v, args)
      case Fn("not", _)             => q.findD(v).where(e, a, v).not(e, a)
      case other                    => abort(s"Unresolved mapped Atom:\nAtom   : $a\nElement: $other")
    }
  }


  // Enum ....................................................................................

  def resolveEnumOptional2(q: Query, e: String, a0: Atom, v: String, v2: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case EnumVal      => q.pullEnum(e, a)
      case Fn("not", _) => q.pullEnum(e, a).not(e, a) // None
      case Eq(args)     => q.findD(v2).enum(e, a, v).orRules(e, a, args)
      case other        => abort("Unresolved optional cardinality-many enum Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveEnumOptional1(q: Query, e: String, a0: Atom, v: String, v2: String, prefix: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case EnumVal        => q.pullEnum(e, a)
      case Fn("not", _)   => q.pullEnum(e, a).not(e, a) // None
      case Eq(arg :: Nil) => q.find(v2).enum(e, a, v).where(e, a, Val("__enum__" + prefix + arg))
      case Eq(args)       => q.find(v2).enum(e, a, v).orRules(e, a, args)
      case other          => abort("Unresolved optional cardinality-one enum Atom$:\nAtom$   : " + s"$a\nElement: $other")
    }
  }

  def resolveEnumTacit(q: Query, e: String, a0: Atom, v: String, v2: String, v3: String, prefix: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    a.value match {
      case Qm                                      => q.enum(e, a, v).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                            => q.enum(e, a, v).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                  => q.enum(e, a, v).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                  => q.enum(e, a, v).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                  => q.enum(e, a, v).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                  => q.enum(e, a, v).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                 => q.enum(e, a, v)
      case Neq(args)                               => q.enum(e, a, v).compareToMany("!=", a, v2, args)
      case Fn("not", _)                            => q.not(e, a)
      case Eq(Nil)                                 => q.not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty => q.not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.not(e, a)
      case Eq((set: Set[_]) :: Nil)                => q.enum(e, a, v).whereAnd(e, a, v, set.toSeq.map("__enum__" + prefix + _))
      case Eq(arg :: Nil)                          => q.where(e, a, Val("__enum__" + prefix + arg))
      case Eq(args)                                => q.orRules(e, a, args)
      case And(args) if a.card == 2                => q.enum(e, a, v).whereAnd(e, a, v, args.map("__enum__" + prefix + _))
      case Gt(arg)                                 => q.enum(e, a, v).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                 => q.enum(e, a, v).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                 => q.enum(e, a, v).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                 => q.enum(e, a, v).compareTo("<=", a, v2, Val(arg), 1)
      case other                                   => abort(s"Unresolved tacit enum Atom_:\nAtom_  : $a\nElement: $other")
    }
  }

  def resolveEnumMandatory2(q: Query, e: String, a: Atom, v: String, v2: String, v3: String, prefix: String): Query = {
    a.value match {
      case Qm                                          => q.findD(v2).enum(e, a, v).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                                => q.findD(v2).enum(e, a, v).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                      => q.findD(v2).enum(e, a, v).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                      => q.findD(v2).enum(e, a, v).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                      => q.findD(v2).enum(e, a, v).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                      => q.findD(v2).enum(e, a, v).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                     => q.findD(v2).enum(e, a, v)
      case Fn("not", _)                                => q.findD(v2).enum(e, a, v).not(e, a)
      case Eq(Nil)                                     => q.findD(v2).enum(e, a, v).not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty     => q.findD(v2).enum(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty     => q.findD(v2).enum(e, a, v).not(e, a)
      case Eq((set: Set[_]) :: Nil)                    => q.findD(v2).enum(e, a, v).whereAnd(e, a, v, set.toSeq.map("__enum__" + prefix + _))
      case Eq(args)                                    => q.findD(v2).enum(e, a, v).orRules(e, a, args)
      case And(args)                                   => q.findD(v2).whereAndEnum(e, a, v, prefix, args)
      case Neq(Nil)                                    => q.findD(v2).enum(e, a, v)
      case Neq(sets) if sets.head.isInstanceOf[Set[_]] => q.findD(v2).enum(e, a, v).nots(e, a, v2, sets)
      case Neq(arg :: Nil) if uri(a.tpe)               => q.findD(v2).enum(e, a, v).compareTo("!=", a, v2, Val(arg))
      case Neq(arg :: Nil)                             => q.findD(v2).enum(e, a, v).compareTo("!=", a, v2, Val(arg))
      case Neq(args)                                   => q.findD(v2).enum(e, a, v).compareToMany("!=", a, v2, args)
      case Gt(arg)                                     => q.findD(v2).enum(e, a, v).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                     => q.findD(v2).enum(e, a, v).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                     => q.findD(v2).enum(e, a, v).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                     => q.findD(v2).enum(e, a, v).compareTo("<=", a, v2, Val(arg), 1)
      case Fn(fn, Some(i))                             => q.find(fn, Seq(i), v2).enum(e, a, v)
      case Fn(fn, _) if coalesce(fn)                   => q.aggrV(a).fold(q.find(fn, Nil, v2).enum(e, a, v).widh(e))(q.find(fn, Nil, _).widh(e))
      case Fn(fn, _)                                   => q.find(fn, Nil, v2).enum(e, a, v)
      case other                                       => abort(s"Unresolved cardinality-many enum Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveEnumMandatory1(q: Query, e: String, a: Atom, v: String, v2: String, v3: String, prefix: String): Query = {
    a.value match {
      case Qm                                      => q.find(v2).enum(e, a, v).in(e, a, Some(prefix), v2)
      case Neq(Seq(Qm))                            => q.find(v2).enum(e, a, v).compareTo("!=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Gt(Qm)                                  => q.find(v2).enum(e, a, v).compareTo(">", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Ge(Qm)                                  => q.find(v2).enum(e, a, v).compareTo(">=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Lt(Qm)                                  => q.find(v2).enum(e, a, v).compareTo("<", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case Le(Qm)                                  => q.find(v2).enum(e, a, v).compareTo("<=", a, v2, Var(v3), 1).in(e, a, Some(prefix), v3)
      case EnumVal                                 => q.find(v2).enum(e, a, v)
      case Fn("not", _)                            => q.find(v2).enum(e, a, v).not(e, a)
      case Eq(Nil)                                 => q.find(v2).enum(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.find(v2).enum(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil)                => q.find(v2).enum(e, a, v).orRules(e, a, seq)
      case Eq(arg :: Nil)                          => q.find(v2).enum(e, a, v).where(e, a, Val("__enum__" + prefix + arg))
      case Eq(args)                                => q.find(v2).enum(e, a, v).orRules(e, a, args)
      case Neq(args)                               => q.find(v2).enum(e, a, v).compareToMany("!=", a, v2, args)
      case Gt(arg)                                 => q.find(v2).enum(e, a, v).compareTo(">", a, v2, Val(arg), 1)
      case Ge(arg)                                 => q.find(v2).enum(e, a, v).compareTo(">=", a, v2, Val(arg), 1)
      case Lt(arg)                                 => q.find(v2).enum(e, a, v).compareTo("<", a, v2, Val(arg), 1)
      case Le(arg)                                 => q.find(v2).enum(e, a, v).compareTo("<=", a, v2, Val(arg), 1)
      case Fn(fn, Some(i))                         => q.find(fn, Seq(i), v2).enum(e, a, v)
      case Fn(fn, _) if coalesce(fn)               => q.aggrV(a).fold(q.find(fn, Nil, v2).enum(e, a, v).widh(e))(q.find(fn, Nil, _).widh(e))
      case Fn(fn, _)                               => q.find(fn, Nil, v2).enum(e, a, v)
      case other                                   => abort(s"Unresolved cardinality-one enum Atom:\nAtom   : $a\nElement: $other")
    }
  }


  // Atom ....................................................................................

  def resolveAtomOptional2(q: Query, e: String, a0: Atom, v: String, v1: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    val t = a.tpe
    a.value match {
      case VarValue                 => q.pull(e, a)
      case Fn("not", _)             => q.pull(e, a).not(e, a) // None
      case Eq(arg :: Nil) if uri(t) => q.findD(v).func(s"""ground (java.net.URI. "$arg")""", Empty, v).where(e, a, v)
      case Eq(arg :: Nil)           => q.findD(v).where(e, a, Val(arg)).where(e, a, v)
      case Eq(args)                 => q.findD(v).where(e, a, v).orRules(e, a, args, u(t, v))
      case Fulltext(arg :: Nil)     => q.findD(v).fulltext(e, a, v, arg.toString)
      case Fulltext(args)           => q.findD(v).fulltext(e, a, v, Var(v1)).orRules(v1, a, args, "", true)
      case other                    => abort("Unresolved optional cardinality-many Atom$:\nAtom$   : " + s"$a0\nElement: $other")
    }
  }
  def resolveAtomOptional1(q: Query, e: String, a0: Atom, v: String, v1: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    val t = a.tpe
    a.value match {
      case VarValue                 => q.pull(e, a)
      case Fn("not", _)             => q.pull(e, a).not(e, a) // None
      case Eq(arg :: Nil) if uri(t) => q.find(v).func(s"""ground (java.net.URI. "$arg")""", Empty, v).where(e, a, v)
      case Eq(arg :: Nil)           => q.find(v).where(e, a, Val(arg)).where(e, a, v)
      case Eq(args)                 => q.find(v).where(e, a, v).orRules(e, a, args, u(t, v))
      case Fulltext(arg :: Nil)     => q.find(v).fulltext(e, a, v, arg.toString)
      case Fulltext(args)           => q.find(v).fulltext(e, a, v, Var(v1)).orRules(v1, a, args, "", true)
      case other                    => abort("Unresolved optional cardinality-one Atom$:\nAtom$   : " + s"$a0\nElement: $other")
    }
  }


  def resolveAtomTacit(q: Query, e: String, a0: Atom, v: String, v1: String): Query = {
    val a = a0.copy(attr = a0.attr.init)
    val t = a.tpe
    a.value match {
      case Qm                                      => q.where(e, a, v).in(e, a, None, v)
      case Neq(Seq(Qm))                            => q.where(e, a, v).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                  => q.where(e, a, v).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                  => q.where(e, a, v).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                  => q.where(e, a, v).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                  => q.where(e, a, v).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                       => q.fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case VarValue                                => q.where(e, a, v)
      case Fn("not", _)                            => q.not(e, a)
      case Eq(Nil)                                 => q.not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty => q.not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.not(e, a)
      case Eq((set: Set[_]) :: Nil)                => q.whereAnd(e, a, v, set.toSeq, u(t, v))
      case Eq(arg :: Nil) if uri(t)                => q.where(e, a, v).func(s"""ground (java.net.URI. "$arg")""", Empty, v)
      case Eq(arg :: Nil)                          => q.ground(a, arg, v).where(e, a, v)
      case Eq(args)                                => q.orRules(e, a, args, u(t, v))
      case Neq(args)                               => q.where(e, a, v).compareToMany("!=", a, v, args)
      case Gt(arg)                                 => q.where(e, a, v).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                 => q.where(e, a, v).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                 => q.where(e, a, v).compareTo("<", a, v, Val(arg))
      case Le(arg)                                 => q.where(e, a, v).compareTo("<=", a, v, Val(arg))
      case And(args) if a.card == 2                => q.whereAnd(e, a, v, args, u(t, v))
      case And(args)                               => q.where(e, a, Val(args.head))
      case Fn("unify", _)                          => q.where(e, a, v)
      case Fulltext(arg :: Nil)                    => q.fulltext(e, a, "_", arg.toString)
      case Fulltext(args)                          => q.where(e, a, v).orRules(e, a, args, "", true)
      case other                                   => abort(s"Unresolved tacit Atom_:\nAtom_  : $a\nElement: $other")
    }
  }

  def resolveAtomMandatory2(q: Query, e: String, a: Atom, v: String, v1: String): Query = {
    val t = a.tpe
    a.value match {
      case Qm                                          => q.findD(v).where(e, a, v).in(e, a, None, v)
      case Neq(Seq(Qm))                                => q.findD(v).where(e, a, v).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                      => q.findD(v).where(e, a, v).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                      => q.findD(v).where(e, a, v).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                      => q.findD(v).where(e, a, v).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                      => q.findD(v).where(e, a, v).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                           => q.findD(v).fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case VarValue                                    => q.findD(v).where(e, a, v)
      case Fn("not", _)                                => q.findD(v).where(e, a, v).not(e, a)
      case Eq(Nil)                                     => q.findD(v).where(e, a, v).not(e, a)
      case Eq((set: Set[_]) :: Nil) if set.isEmpty     => q.findD(v).where(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty     => q.findD(v).where(e, a, v).not(e, a)
      case Eq((set: Set[_]) :: Nil)                    => q.findD(v).whereAnd(e, a, v, set.toSeq, u(t, v))
      case Eq(arg :: Nil) if uri(t)                    => q.findD(v).where(e, a, v).where(e, a, v + "_uri").func(s"""ground (java.net.URI. "$arg")""", Empty, v + "_uri")
      case Eq(arg :: Nil) if a.tpe == "Float"          => q.findD(v).where(e, a, Val(arg.toString.toFloat)).where(e, a, v)
      case Eq(arg :: Nil) if a.tpe == "Double"         => q.findD(v).where(e, a, Val(arg.toString.toDouble)).where(e, a, v)
      case Eq(arg :: Nil) if a.tpe == "BigDecimal"     => q.findD(v).where(e, a, Val(BigDecimal(withDecimal(arg)))).where(e, a, v)
      case Eq(arg :: Nil)                              => q.findD(v).where(e, a, Val(arg)).where(e, a, v)
      case Eq(args)                                    => q.findD(v).where(e, a, v).orRules(e, a, args, u(t, v))
      case Neq(Nil)                                    => q.findD(v).where(e, a, v)
      case Neq(sets) if sets.head.isInstanceOf[Set[_]] => q.findD(v).where(e, a, v).nots(e, a, v, sets)
      case Neq(arg :: Nil)                             => q.findD(v).where(e, a, v).compareTo("!=", a, v, Val(arg))
      case Neq(args)                                   => q.findD(v).where(e, a, v).compareToMany("!=", a, v, args)
      case Gt(arg)                                     => q.findD(v).where(e, a, v).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                     => q.findD(v).where(e, a, v).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                     => q.findD(v).where(e, a, v).compareTo("<", a, v, Val(arg))
      case Le(arg)                                     => q.findD(v).where(e, a, v).compareTo("<=", a, v, Val(arg))
      case And(args)                                   => q.findD(v).whereAnd(e, a, v, args, u(t, v))
      case Fn(fn, Some(i))                             => q.find(fn, Seq(i), v).where(e, a, v)
      case Fn(fn, _) if coalesce(fn)                   => q.aggrV(a).fold(q.find(fn, Nil, v).where(e, a, v).widh(e))(q.find(fn, Nil, _).widh(e))
      case Fn(fn, _)                                   => q.find(fn, Nil, v).where(e, a, v)
      case Fulltext(args)                              => q.findD(v).where(e, a, v).orRules(e, a, args, "", true)
      case other                                       => abort(s"Unresolved cardinality-many Atom:\nAtom   : $a\nElement: $other")
    }
  }

  def resolveAtomMandatory1(q: Query, e: String, a: Atom, v: String, v1: String): Query = {
    val t = a.tpe
    a.value match {
      case Qm                                      => q.find(v).where(e, a, v).in(e, a, None, v)
      case Neq(Seq(Qm))                            => q.find(v).where(e, a, v).compareTo("!=", a, v, Var(v1)).in(e, a, None, v1)
      case Gt(Qm)                                  => q.find(v).where(e, a, v).compareTo(">", a, v, Var(v1)).in(e, a, None, v1)
      case Ge(Qm)                                  => q.find(v).where(e, a, v).compareTo(">=", a, v, Var(v1)).in(e, a, None, v1)
      case Lt(Qm)                                  => q.find(v).where(e, a, v).compareTo("<", a, v, Var(v1)).in(e, a, None, v1)
      case Le(Qm)                                  => q.find(v).where(e, a, v).compareTo("<=", a, v, Var(v1)).in(e, a, None, v1)
      case Fulltext(Seq(Qm))                       => q.find(v).fulltext(e, a, v, Var(v1)).in(e, a, None, v1)
      case EntValue                                => q.find(e)
      case VarValue                                => q.find(v).where(e, a, v)
      case NoValue                                 => q.find(NoVal).where(e, a, v)
      case Distinct                                => q.find("distinct", Nil, v).where(e, a, v).widh(e)
      case BackValue(backNs)                       => q.find(e).where(v, a.nsFull, a.attr, Var(e), backNs)
      case Fn("not", _)                            => q.find(v).where(e, a, v).not(e, a)
      case Eq(Nil)                                 => q.find(v).where(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil) if seq.isEmpty => q.find(v).where(e, a, v).not(e, a)
      case Eq((seq: Seq[_]) :: Nil)                => q.find(v).where(e, a, v).orRules(e, a, seq, u(t, v))
      case Eq(arg :: Nil)                          => q.find(v).ground(a, arg, v).where(e, a, v)
      case Eq(args)                                => q.find(v).where(e, a, v).orRules(e, a, args, u(t, v))
      case Neq(args)                               => q.find(v).where(e, a, v).compareToMany("!=", a, v, args)
      case Gt(arg)                                 => q.find(v).where(e, a, v).compareTo(">", a, v, Val(arg))
      case Ge(arg)                                 => q.find(v).where(e, a, v).compareTo(">=", a, v, Val(arg))
      case Lt(arg)                                 => q.find(v).where(e, a, v).compareTo("<", a, v, Val(arg))
      case Le(arg)                                 => q.find(v).where(e, a, v).compareTo("<=", a, v, Val(arg))
      case Fn(fn, Some(i))                         => q.find(fn, Seq(i), v).where(e, a, v)
      case Fn(fn, _) if coalesce(fn)               => q.aggrV(a).fold(q.find(fn, Nil, v).where(e, a, v).widh(e))(q.find(fn, Nil, _).widh(e))
      case Fn(fn, _)                               => q.find(fn, Nil, v).where(e, a, v)
      case Fulltext(arg :: Nil)                    => q.find(v).fulltext(e, a, v, arg.toString)
      case Fulltext(args)                          => q.find(v).fulltext(e, a, v, Var(v1)).orRules(v1, a, args, "", true)
      case other                                   => abort(s"Unresolved cardinality-one Atom:\nAtom   : $a\nElement: $other")
    }
  }


  def coalesce(fn: String): Boolean = Seq("sum", "count", "count-distinct", "median", "avg", "variance", "stddev").contains(fn)

  def uri(t: String): Boolean = t.contains("java.net.URI")

  def u(t: String, v: String): String = if (t.contains("java.net.URI")) v else ""

  def nextChar(str: String, inc: Int): String = {
    val chars      = str.toCharArray
    val (pre, cur) = if (chars.size == 2) (chars.head, chars.last) else ('-', chars.head)
    (pre, cur, inc) match {
      case (_, _, i) if i > 2 => abort("Can't increment more than 2")
      case ('-', 'y', 2)      => "aa"
      case ('-', 'z', 2)      => "ab"
      case ('-', 'z', 1)      => "aa"
      case ('-', c, i)        => (c + i).toChar.toString
      case ('z', _, _)        => abort("Ran out of vars...")
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
      abort("For now, only 1 And-expression can be used. Found: " + andAtoms)

    if (andAtoms.size == 1) {
      val clauses                                               = q.wh.clauses
      val andAtom                                               = andAtoms.head
      val Atom(nsFull, attr0, _, card, And(andValues), _, _, _) = andAtom
      val attr                                                  = if (attr0.last == '_') attr0.init else attr0
      val unifyAttrs                                            = model.elements.collect {
        case a@Atom(nsFull1, attr1, _, _, _, _, _, _) if a != andAtom => (nsFull1, if (attr1.last == '_') attr1.init else attr1)
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
          case _                     => qt
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
          case DataClause(ds, e@Var(_), a@KW(ns2, attr2, _), Var(v), tx, op) if (nsFull, attr) == (ns2, attr2) && card == 3 =>
            // Add next And-value
            Seq(
              DataClause(ds, vi(e), a, Var(v + "_" + i), queryTerm(tx), queryTerm(op)),
              Funct(".matches ^String", List(Var(v + "_" + i), Val(".+@(" + andValue + ")$")), NoBinding)
            )

          case DataClause(ds, e@Var(_), a@KW(ns2, attr2, _), _, tx, op) if (nsFull, attr) == (ns2, attr2) =>
            // Add next And-value
            Seq(DataClause(ds, vi(e), a, Val(andValue), queryTerm(tx), queryTerm(op)))

          case DataClause(ds, e@Var(_), a@KW(ns2, attr2, _), v, tx, op) if unifyAttrs.contains((ns2, attr2)) =>
            // Keep value-position value to unify
            Seq(DataClause(ds, vi(e), a, v, queryTerm(tx), queryTerm(op)))

          case DataClause(ds, e@Var(_), a, v, tx, op) =>
            // Add i to variables
            Seq(DataClause(ds, vi(e), a, queryValue(v), queryTerm(tx), queryTerm(op)))

          case dc => abort("Unexpected DataClause: " + dc)
        }
        def makeSelfJoinClauses(expr: QueryExpr): Seq[Clause] = expr match {
          case dc: DataClause                                      => dataClauses(dc)
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