package molecule
package transform
import molecule.ast.model._
import molecule.boilerplate.NS
import molecule.boilerplate.attributes._
import molecule.ops.TreeOps

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

private[molecule] trait Dsl2Model[Ctx <: Context] extends TreeOps[Ctx] {
  import c.universe._
  val x = DebugMacro("Dsl2Model", 20)

  def resolve(tree: Tree): Seq[Element] = dslStructure.applyOrElse(
    tree, (t: Tree) => abort(s"[Dsl2Model:resolve] Unexpected tree: $t\nRAW: ${showRaw(t)}"))

  def traverse(prev: Tree, element: Element): Seq[Element] = {
    if (prev.isNS && !q"$prev".isFirstNS) resolve(prev) :+ element else Seq(element)
  }

  def traverse(prev: Tree, elements: Seq[Element]): Seq[Element] = {
    if (prev.isNS && !q"$prev".isFirstNS) resolve(prev) ++ elements else elements
  }

  def kw(kwTree: Tree): (String, String) = (kwTree.toString(): String) match {
    case r""""\:(\w*)$ns0/(\w*)$attr0"""" => (ns0, attr0)
    case otherKw                          => abort("[Dsl2Model:kw] Unrecognized attribute keyword: " + otherKw)
  }

  val dslStructure: PartialFunction[Tree, Seq[Element]] = {

    // Namespace(eid).attr1...
    case q"$prev.$ns.apply($pkg.?)" if q"$prev.$ns".isFirstNS                        => traverse(q"$prev", Meta(firstLow(ns), "eid_", "e", NoValue, Eq(Seq(Qm))))
    case q"$prev.$ns.apply($eid)" if q"$prev.$ns".isFirstNS && q"$prev.$ns".isBiEdge => traverse(q"$prev", Meta(firstLow(ns), "eid_", "e", BiEdge, Eq(Seq(extract(eid)))))
    case q"$prev.$ns.apply(..$eids)" if q"$prev.$ns".isFirstNS                       => traverse(q"$prev", Meta(firstLow(ns), "eid_", "e", NoValue, Eq(resolveValues(q"Seq(..$eids)"))))


    // Functions ---------------------------

    case q"$prev.$cur.length.apply(..$values)" => traverse(q"$prev", resolveOp(q"$prev", q"$cur", q"$prev.$cur", q"count", q"Seq(..$values)"))
    case q"$prev.$cur.length"                  => traverse(q"$prev", resolveOp(q"$prev", q"$cur", q"$prev.$cur", q"count", q"Seq()"))


    // EAV + ns -----------------------------

    case q"$prev.e"  => traverse(q"$prev", Meta("", "eid", "e", NoValue, EntValue))
    case q"$prev.e_" => traverse(q"$prev", Meta("", "eid_", "e", NoValue, EntValue))
    case q"$prev.a"  => traverse(q"$prev", Atom("?", "attr", "a", 1, NoValue))

    case q"$prev.ns.apply(..$values)"  => traverse(q"$prev", Atom("ns", "?", "ns", 1, modelValue("apply", null, q"Seq(..$values)")))
    case q"$prev.ns_.apply(..$values)" => traverse(q"$prev", Atom("ns_", "?", "ns", 1, modelValue("apply", null, q"Seq(..$values)")))
    case q"$prev.ns"                   => traverse(q"$prev", Atom("ns", "?", "ns", 1, NoValue))
    case q"$prev.ns_"                  => abort( s"""[Dsl2Model:dslStructure] Generic namespace value `ns_` can only be used with an applied value i.e. `ns_("someNamespace")`""")

    case q"$prev.v.apply(..$values)"  => traverse(q"$prev", Meta("", "", "v", AttrVar(""), modelValue("apply", null, q"Seq(..$values)")))
    case q"$prev.v_.apply(..$values)" => traverse(q"$prev", Meta("", "", "v", NoValue, modelValue("apply", null, q"Seq(..$values)")))
    case q"$prev.v"                   => traverse(q"$prev", Meta("", "", "v", AttrVar(""), NoValue))
    case q"$prev.v_"                  => abort( s"""[Dsl2Model:dslStructure] Generic attribute value `v_` can only be used with an applied value i.e. `v_("some value")`""")


    // Tx ----------------------------------

    // Internal predefined db functions
    case q"$prev.Db.tx"        => traverse(q"$prev", Atom("db", "tx", "Long", 1, VarValue))
    case q"$prev.Db.t"         => traverse(q"$prev", Atom("db", "txT", "Long", 1, VarValue))
    case q"$prev.Db.txInstant" => traverse(q"$prev", Atom("db", "txInstant", "Long", 1, VarValue))
    case q"$prev.Db.op"        => traverse(q"$prev", Atom("db", "op", "Boolean", 1, VarValue))

    // Transaction meta data
    case q"$prev.Tx.apply[..$t]($txMolecule)" => traverse(q"$prev", TxMetaData(resolve(q"$txMolecule")))

    // ns.txInstant.attr - `txInstant` doesn't relate to any previous attr
    case q"$prev.tx" if !q"$prev".isAttr        => abort(s"[Dsl2Model:dslStructure] Please add `tx` after an attribute or another transaction value")
    case q"$prev.t" if !q"$prev".isAttr         => abort(s"[Dsl2Model:dslStructure] Please add `t` after an attribute or another transaction value")
    case q"$prev.t" if !q"$prev".isAttr         => abort(s"[Dsl2Model:dslStructure] Please add `t` after an attribute or another transaction value")
    case q"$prev.txInstant" if !q"$prev".isAttr => abort(s"[Dsl2Model:dslStructure] Please add `txInstant` after an attribute or another transaction value")
    case q"$prev.op" if !q"$prev".isAttr        => abort(s"[Dsl2Model:dslStructure] Please add `op` after an attribute or another transaction value")

    // Generic attributes not after optional attributes
    case q"$prev.tx" if prev.toString.endsWith("$")        => abort(s"[Dsl2Model:dslStructure] Optional attributes (`" + q"$prev".name + "`) can't be followed by generic attributes (`tx`).")
    case q"$prev.t" if prev.toString.endsWith("$")         => abort(s"[Dsl2Model:dslStructure] Optional attributes (`" + q"$prev".name + "`) can't be followed by generic attributes (`t`).")
    case q"$prev.txInstant" if prev.toString.endsWith("$") => abort(s"[Dsl2Model:dslStructure] Optional attributes (`" + q"$prev".name + "`) can't be followed by generic attributes (`txInstant`).")
    case q"$prev.op" if prev.toString.endsWith("$")        => abort(s"[Dsl2Model:dslStructure] Optional attributes (`" + q"$prev".name + "`) can't be followed by generic attributes (`op`).")

    // ns.attr.txInstant etc.. (transaction related to previous attribute)
    case q"$prev.tx_.apply($t)"           => traverse(q"$prev", Meta("db", "tx", "tx", TxValue_(Some(extract(q"$t"))), NoValue))
    case q"$prev.tx.apply($t)"            => traverse(q"$prev", Meta("db", "tx", "tx", TxValue(Some(extract(q"$t"))), NoValue))
    case q"$prev.tx"                      => traverse(q"$prev", Meta("db", "tx", "tx", TxValue(None), NoValue))
    case q"$prev.t_.apply($t)"            => traverse(q"$prev", Meta("db", "txT", "tx", TxTValue_(Some(extract(q"$t"))), NoValue))
    case q"$prev.t.apply($t)"             => traverse(q"$prev", Meta("db", "txT", "tx", TxTValue(Some(extract(q"$t"))), NoValue))
    case q"$prev.t"                       => traverse(q"$prev", Meta("db", "txT", "tx", TxTValue(None), NoValue))
    case q"$prev.txInstant_.apply($date)" => traverse(q"$prev", Meta("db", "txInstant", "tx", TxInstantValue_(Some(extract(q"$date"))), NoValue))
    case q"$prev.txInstant.apply($date)"  => traverse(q"$prev", Meta("db", "txInstant", "tx", TxInstantValue(Some(extract(q"$date"))), NoValue))
    case q"$prev.txInstant"               => traverse(q"$prev", Meta("db", "txInstant", "tx", TxInstantValue(None), NoValue))
    case q"$prev.op_.apply($added)"       => traverse(q"$prev", Meta("db", "op", "tx", OpValue_(Some(extract(q"$added"))), NoValue))
    case q"$prev.op.apply($added)"        => traverse(q"$prev", Meta("db", "op", "tx", OpValue(Some(extract(q"$added"))), NoValue))
    case q"$prev.op"                      => traverse(q"$prev", Meta("db", "op", "tx", OpValue(None), NoValue))

    // Tacit transaction attributes not allowed
    case q"$prev.tx_"        => abort(s"[Dsl2Model:dslStructure] Tacit `tx_` not allowed since all datoms have a tx value")
    case q"$prev.t_"         => abort(s"[Dsl2Model:dslStructure] Tacit `t_` not allowed since all datoms have a t value")
    case q"$prev.txInstant_" => abort(s"[Dsl2Model:dslStructure] Tacit `txInstant_` not allowed since all datoms have a txInstant value")
    case q"$prev.op_"        => abort(s"[Dsl2Model:dslStructure] Tacit `op_` not allowed since all datoms have a `op value")


    // Self join -----------------------------
    case q"$prev.Self" => traverse(q"$prev", Self)


    // Clean attributes --------------------------------------

    case a@q"$prev.$cur" if a.isEnum      => traverse(q"$prev", Atom(a.ns, a.name, cast(a), a.card, EnumVal, Some(a.enumPrefix), bi(a)))
    case a@q"$prev.$cur" if a.isMapAttr   => walk(q"$prev", a.ns, q"$cur", Atom(a.ns, a.name, cast(a), 3, VarValue, None, bi(a)))
    case a@q"$prev.$cur" if a.isValueAttr => walk(q"$prev", a.ns, q"$cur", Atom(a.ns, a.name, cast(a), a.card, VarValue, gs = bi(a)))


    // Clean optional attributes ----------------------

    case a@q"$prev.$refAttr" if a.isRefAttr$ => traverse(q"$prev", Atom(a.ns, a.name, cast(a), a.card, VarValue, gs = bi(a)))
    case a@q"$prev.$cur" if a.isEnum$        => traverse(q"$prev", Atom(a.ns, a.name, cast(a), a.card, EnumVal, Some(a.enumPrefix), bi(a)))
    case a@q"$prev.$cur" if a.isMapAttr$     => walk(q"$prev", a.ns, q"$cur", Atom(a.ns, a.name, cast(a), 3, VarValue, None, bi(a)))
    case a@q"$prev.$cur" if a.isValueAttr$   => walk(q"$prev", a.ns, q"$cur", Atom(a.ns, a.name, cast(a), a.card, VarValue, gs = bi(a)))

    // Clean references --------------------------------------

    case r@q"$prev.$backRefAttr" if backRefAttr.toString.head == '_' =>
      val backRef = c.typecheck(q"$prev.$backRefAttr").tpe.typeSymbol.name.toString // "partition_Ns_<arity>"
      traverse(q"$prev", ReBond(firstLow(backRef.tail), "")) // "partition_Ns"

    case a@q"$prev.$ref" if a.isRef         => traverse(q"$prev", Bond(a.refThis, firstLow(ref.toString), a.refNext, a.refCard, bi(a)))
    case a@q"$prev.$refAttr" if a.isRefAttr => traverse(q"$prev", Atom(a.ns, a.name, "Long", a.card, VarValue, gs = bi(a)))


    // Attribute map operations -----------------------------------------------------

    // Attribute map using k/apply
    case t@q"$prev.$cur.k(..$keys).$op(..$values)" =>
      val keyList = getValues(q"$keys").asInstanceOf[Seq[String]]
      val element = resolveOp(q"$prev", q"$cur", q"$prev.$cur", q"$op", q"Seq(..$values)") match {
        case a: Atom => a.copy(keys = keyList)
      }
      walk(q"$prev", q"$prev.$cur".ns, q"$cur", element)

    // Keyed attribute map operation
    case t@q"$prev.$cur($key).$op(..$values)" if q"$prev.$cur($key)".isMapAttrK =>
      val value: Value = modelValue(op.toString(), t, q"Seq(..$values)")
      val tpe0 = c.typecheck(q"$prev.$cur($key)").tpe
      val ns = new nsp(tpe0.typeSymbol.owner).toString
      val tpe = tpe0.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last
      val element = Atom(ns, cur.toString, cast(tpe.toString), 4, value, None, Nil, Seq(extract(q"$key").toString))
      walk(q"$prev", ns, q"$cur", element)

    // Keyed attribute map
    case t@q"$prev.$cur($key)" if t.isMapAttrK =>
      val ns = new nsp(t.tpe.typeSymbol.owner).toString
      val tpe = t.tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last
      val element = Atom(ns, cur.toString, cast(tpe.toString), 4, VarValue, None, Nil, Seq(extract(q"$key").toString))
      walk(q"$prev", ns, q"$cur", element)


    // Attribute operations -----------------------------

    case q"$prev.$ref.apply(..$values)" if q"$prev.$ref".isRef => abort(s"[Dsl2Model:dslStructure] Can't apply value to a reference (`$ref`)")
    case t@q"$prev.$cur.$op(..$values)"                        =>
      val element = resolveOp(q"$prev", q"$cur", q"$prev.$cur", q"$op", q"Seq(..$values)")
      walk(q"$prev", q"$prev.$cur".ns, q"$cur", element)


    // Nested ------------------------

    case t@q"$prev.e.apply[..$types]($nested)" if !q"$prev".isRef  => Seq(Nested(Bond("", "", "", 2), Meta("", "", "e", NoValue, EntValue) +: resolve(q"$nested")))
    case t@q"$prev.e_.apply[..$types]($nested)" if !q"$prev".isRef => Seq(Nested(Bond("", "", "", 2), resolve(q"$nested")))
    case t@q"$prev.e.$manyRef.*[..$types]($nested)"                => traverse(q"$prev.e", nested1(prev, manyRef, nested))

    case t@q"$prev.$manyRef.*[..$types]($nested)"                                 => traverse(q"$prev", nested1(prev, manyRef, nested))
    case t@q"$prev.$manyRef.apply[..$types]($nested)" if !q"$prev.$manyRef".isRef => Seq(Nested(Bond("", "", "", 2), nestedElements(q"$prev.$manyRef", firstLow(manyRef.toString), q"$nested")))
    case t@q"$prev.$manyRef.apply[..$types]($nested)"                             => traverse(q"$prev", nested1(prev, manyRef, nested))


    // Composite -----------------------------

    case t@q"$prev.~[..$types]($next)" =>
      val prevElements = resolve(q"$prev")
      val prevComposites = prevElements.collect { case c: Composite => c }
      if (prevComposites.isEmpty) {
        val lookForward = traverse(q"$prev", Seq(Composite(prevElements), Composite(resolve(q"$next"))))
        val composites = lookForward.collect { case fm: Composite => fm }
        composites
      } else {
        prevComposites :+ Composite(resolve(q"$next"))
      }

    case other => abort(s"[Dsl2Model:dslStructure] Unexpected DSL structure: $other\n${showRaw(other)}")
  }


  def walk(prev: Tree, curNs: String, cur: Tree, thisElement: Element) = {
    //    val prevElements = if (q"$prev".isAttr || q"$prev".symbol.isMethod) resolve(prev) else Seq[Element]()
    val prevElements = if (prev.isNS && !q"$prev".isFirstNS) resolve(prev) else Seq[Element]()
    val attr = cur.toString()
    val curIsVarValue = thisElement match {
      case Atom(_, _, _, _, VarValue, _, _, _) => true
      case _                                   => false
    }
    if (prevElements.isEmpty) {
      traverse(q"$prev", thisElement)
    } else {
      prevElements.last match {
        case Atom(`curNs`, prevAttr0, _, _, _, _, _, _) if clean(prevAttr0) == clean(attr) && curIsVarValue =>
          //          x(30, prevElements, thisElement)
          val (_, similarAtoms, transitive) = {
            prevElements.foldRight(prevElements, Seq[Atom](), None: Option[Transitive]) {
              case (prevElement, (previous, similarAtoms1, trans)) =>
                prevElement match {
                  case prevAtom@Atom(`curNs`, prevAttr, _, _, _, _, _, _) if clean(prevAttr) == clean(attr) =>
                    val t = previous.init.reverse.collectFirst {
                      // Find first previous Bond (relating to this attribute)
                      case prevBond@Bond(ns2, refAttr, refNs, _, _) =>
                        Transitive(ns2, refAttr, refNs, 0)
                    } getOrElse {
                      Transitive(curNs, prevAttr, curNs, 0)
                    }
                    (previous.init, similarAtoms1 :+ prevAtom, Some(t))
                  case _                                                                                    =>
                    (previous.init, similarAtoms1, trans)
                }
            }
          }
          similarAtoms.size match {
            case 1 => prevElements :+ transitive.get.copy(depth = 1) :+ thisElement
            case 2 => prevElements :+ transitive.get.copy(depth = 2) :+ thisElement
            case 3 => prevElements :+ transitive.get.copy(depth = 3) :+ thisElement
            case n => abort(s"[Dsl2Model:walk] Unsupported transitive arity: $n")
          }

        case _ => traverse(q"$prev", thisElement)
      }
    }
  }

  def clean(a: String) = if (a.last == '_') firstLow(a.init) else firstLow(a)

  def nested1(prev: Tree, manyRef: TermName, nestedTree: Tree) = {
    val refNext = q"$prev.$manyRef".refNext
    val parentNs = prev match {
      case k@q"$p.apply($value)" if k.isMapAttrK   => new nsp(c.typecheck(k).tpe.typeSymbol.owner)
      case q"$p.apply($value)" if q"$p".isAttr     => q"$p".ns
      case q"$p.apply($value)"                     => q"$p".name
      case p if p.symbol.name.toString.head == '_' => firstLow(prev.tpe.typeSymbol.name.toString.tail)
      case p if p.isAttr                           => p.ns
      case p if p.isRef                            => p.refNext
      case p                                       => p.name
    }
    val nestedElems = nestedElements(q"$prev.$manyRef", refNext, nestedTree)
    val nested = Nested(Bond(parentNs.toString, firstLow(manyRef), refNext, 2, bi(q"$prev.$manyRef")), nestedElems)
    //            x(28, prev, parentNs, nestedElems, nested, refNext, q"$prev.$manyRef", q"$prev.$manyRef".card)
    nested
  }

  def nestedElements(manyRef: Tree, refNext: String, nested: Tree): Seq[Element] = {
    val nestedElements = resolve(nested)
    val nestedNs = curNs(nestedElements.head)
    if (refNext != nestedNs) {
      // Find refs in `manyRef` namespace and match the target type with the first namespace of the first nested element
      val refs = c.typecheck(manyRef).tpe.members.filter(e => e.isModule && e.typeSignature <:< weakTypeOf[Ref[_, _]])
      val refPairs = refs.map(r => r.name -> r.typeSignature.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.last.typeSymbol.name)
      val refPairsFiltered = refPairs.filter(_._2.toString == nestedNs.capitalize)
      if (refPairsFiltered.isEmpty) {
        nestedElements
      } else if (refPairsFiltered.size == 1) {
        val (refAttr, refNs) = refPairsFiltered.head
        Bond(refNext, firstLow(refAttr), firstLow(refNs), 2, bi(manyRef)) +: nestedElements
      } else
        abort(s"[Dsl2Model:dslStructure(nested)] `$manyRef` has more than one ref pointing to `$nestedNs`:\n${refPairs.mkString("\n")}")
    } else {
      nestedElements
    }
  }

  def bi(tree: Tree): Seq[Generic] = tree match {
    case t if t.isBiSelfRef     => Seq(BiSelfRef(t.refCard))
    case t if t.isBiSelfRefAttr => Seq(BiSelfRefAttr(t.card))

    case t if t.isBiOtherRef =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiOtherRef_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiOtherRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case t if t.isBiOtherRefAttr =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiOtherRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiOtherRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case t if t.isBiEdgeRef =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiEdgeRef_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiEdgeRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case t if t.isBiEdgeRefAttr =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiEdgeRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiEdgeRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case t if t.isBiEdgePropRef     => Seq(BiEdgePropRef(t.refCard))
    case t if t.isBiEdgePropAttr    => Seq(BiEdgePropAttr(t.card))
    case t if t.isBiEdgePropRefAttr => Seq(BiEdgePropRefAttr(t.card))

    case t if t.isBiTargetRef =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiTargetRef_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiTargetRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case t if t.isBiTargetRefAttr =>
      val baseType = c.typecheck(t).tpe.baseType(weakTypeOf[BiTargetRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
      Seq(BiTargetRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

    case other => Nil
  }

  def cast(tpe: String): String = tpe match {
    case "Int"   => "Long"
    case "Float" => "Double"
    case other   => other
  }
  def cast(atom: Tree): String = cast(atom.tpeS)

  def resolveOp(previous: Tree, curTree: Tree, attr: Tree, op: Tree, values0: Tree): Element = {
    val value: Value = modelValue(op.toString(), attr, values0)
    val enumPrefix = if (attr.isAnyEnum) Some(attr.at.enumPrefix) else None
    val cur = curTree.toString()
    previous match {
      case prev if cur.head.isUpper          => Atom(attr.name, cur, cast(attr), attr.card, value, enumPrefix, bi(attr))
      case prev if cur == "e" && prev.isRef  => Meta(prev.name, prev.refNext, "e", NoValue, value)
      case prev if cur == "e_" && prev.isRef => Meta(prev.name, prev.refNext, "e", NoValue, value)
      case prev if cur == "e"                => Meta(prev.name, cur, "e", NoValue, value)
      case prev if cur == "e_"               => Meta(prev.name, cur, "e", NoValue, value)
      case prev if cur == "a"                => Atom("?", "attr", "a", 1, value, gs = bi(attr))
      case prev if cur == "a_"               => Atom("?", "attr_", "a", 1, value, gs = bi(attr))
      case prev if cur == "ns"               => Atom("ns", "?", "ns", 1, value, gs = bi(attr))
      case prev if cur == "ns_"              => Atom("ns_", "?", "ns", 1, value, gs = bi(attr))
      case prev if attr.isMapAttr            => Atom(attr.ns, attr.name, cast(attr), 3, value, None, bi(attr))
      case prev if attr.isAttr               => Atom(attr.ns, attr.name, cast(attr), attr.card, value, enumPrefix, bi(attr))
      case prev if prev.isAttr               => Atom(prev.ns, attr.name, cast(attr), attr.card, value, enumPrefix, bi(attr))
      case prev                              => Atom(attr.name, cur, "Int", attr.card, value, enumPrefix, bi(attr))
    }
  }


  // Values ================================================================================

  def modelValue(op: String, attr: Tree, values0: Tree) = {
    def errValue(i: Int, v: Any) = abort(s"[Dsl2Model:modelValue $i] Unexpected resolved model value for `${attr.name}.$op`: $v")
    val values = getValues(values0, attr)
    //        x(10, values0, values, op, attr, attr.tpe)
    op match {
      case "applyKey"    => NoValue
      case "apply"       => values match {
        case resolved: Value                            => resolved
        case vs: Seq[_] if attr == null                 => Eq(vs)
        case vs: Seq[_] if attr.isMapAttr && vs.isEmpty => MapEq(Seq())
        case vs: Seq[_]                                 => Eq(vs)
        case other                                      => errValue(1, other)
      }
      case "k"           => values match {
        case vs: Seq[_] => MapKeys(vs.map(_.asInstanceOf[String]))
        case other      => errValue(2, other)
      }
      case "count"       => values match {case Fn("avg", i) => Length(Some(Fn("avg", i))); case other => errValue(3, other)}
      case "not"         => values match {case qm: Qm.type => Neq(Seq(Qm)); case vs: Seq[_] => Neq(vs)}
      case "$bang$eq"    => values match {case qm: Qm.type => Neq(Seq(Qm)); case vs: Seq[_] => Neq(vs)}
      case "$less"       => values match {case qm: Qm.type => Lt(Qm); case vs: Seq[_] => Lt(vs.head)}
      case "$greater"    => values match {case qm: Qm.type => Gt(Qm); case vs: Seq[_] => Gt(vs.head)}
      case "$less$eq"    => values match {case qm: Qm.type => Le(Qm); case vs: Seq[_] => Le(vs.head)}
      case "$greater$eq" => values match {case qm: Qm.type => Ge(Qm); case vs: Seq[_] => Ge(vs.head)}
      case "contains"    => values match {case qm: Qm.type => Fulltext(Seq(Qm)); case vs: Seq[_] => Fulltext(vs)}
      case "add"         => values match {
        case MapEq(pairs)  => MapAdd(pairs)
        case mapped: Value => mapped
        case vs: Seq[_]    => Add_(vs)
      }
      case "remove"      => values match {
        case vs: Seq[_] if attr.isMapAttr => MapRemove(vs.map(_.toString))
        case vs: Seq[_]                   => Remove(vs)
      }
      case "replace"     => values match {
        case MapEq(keyValues) => MapReplace(keyValues)
        case resolved: Value  => resolved
        case Nil              => Replace(Nil)
      }
      case unexpected    => abort(s"[Dsl2Model:modelValue] Unknown operator '$unexpected'\nattr: $attr \nvalue: $values0")
    }
  }

  def getValues(values: Tree, attr: Tree = null): Any = {
    def aggr(fn: String, value: Option[Int] = None) = if (attr.name.last == '_')
      abort(s"[Dsl2Model:getValues] Aggregated values need to be returned. " +
        s"Please omit underscore from attribute `:${attr.ns}/${attr.name}`")
    else
      Fn(fn, value)

    x(2, attr, values)
    values match {
      case q"Seq($pkg.?)"                                          => Qm
      case q"Seq($pkg.nil)" if attr.name.last == '_'               => Fn("not")
      case q"Seq($pkg.nil)"                                        => abort(s"[Dsl2Model:getValues] Please add underscore to attribute: `${attr.name}_(nil)`")
      case q"Seq($pkg.unify)" if attr.name.last == '_'             => Fn("unify")
      case q"Seq($pkg.unify)"                                      => abort(s"[Dsl2Model:getValues] Can only unify on tacit attributes. Please add underscore to attribute: `${attr.name}_(unify)`")
      case q"Seq($pkg.min.apply(${Literal(Constant(i: Int))}))"    => aggr("min", Some(i))
      case q"Seq($pkg.max.apply(${Literal(Constant(i: Int))}))"    => aggr("max", Some(i))
      case q"Seq($pkg.rand.apply(${Literal(Constant(i: Int))}))"   => aggr("rand", Some(i))
      case q"Seq($pkg.sample.apply(${Literal(Constant(i: Int))}))" => aggr("sample", Some(i))
      case q"Seq($pkg.min)"                                        => aggr("min")
      case q"Seq($pkg.max)"                                        => aggr("max")
      case q"Seq($pkg.distinct)"                                   => Distinct
      case q"Seq($pkg.rand)"                                       => aggr("rand", Some(1))
      case q"Seq($pkg.sample)"                                     => aggr("sample", Some(1))
      case q"Seq($pkg.count)"                                      => aggr("count")
      case q"Seq($pkg.countDistinct)"                              => aggr("count-distinct")
      case q"Seq($pkg.sum)"                                        => aggr("sum")
      case q"Seq($pkg.avg)"                                        => aggr("avg")
      case q"Seq($pkg.median)"                                     => aggr("median")
      case q"Seq($pkg.variance)"                                   => aggr("variance")
      case q"Seq($pkg.stddev)"                                     => aggr("stddev")
      case q"Seq($a.and[$t]($b).and[$u]($c))"                      => And(resolveValues(q"Seq($a, $b, $c)"))
      case q"Seq($a.and[$t]($b))"                                  => And(resolveValues(q"Seq($a, $b)"))
      case q"Seq(scala.None)"                                      => Fn("not")
      case q"Seq(scala.Some.apply[$t]($v))"                        => x(3, v)
        v match {
          case vm if vm.tpe <:< weakTypeOf[Map[_, _]]     => vm match {
            case Apply(_, pairs) =>
              mapPairs(pairs, attr)
            case ident           => mapPairs(Seq(ident), attr)
          }
          case ident if attr.isMapAttr || attr.isMapAttr$ => mapPairs(Seq(ident), attr)
          case _                                          => Eq(resolveValues(q"$v"))
        }
      case q"Seq(..$vs)" if vs.size == 1
        && !(vs.head.tpe <:< weakTypeOf[Seq[Nothing]])
        && vs.head.tpe <:< weakTypeOf[Seq[(_, _)]]                 => x(4, vs);
        vs.head match {
          case Apply(_, pairs) =>
            mapPairs(pairs, attr)
          case ident           => mapPairs(Seq(ident), attr)
        }
      case q"Seq(..$vs)" if vs.size == 1
        && !(vs.head.tpe <:< weakTypeOf[Map[Nothing, Nothing]])
        && vs.head.tpe <:< weakTypeOf[Map[_, _]]                   => x(5, vs);
        vs.head match {
          case Apply(_, pairs) =>
            mapPairs(pairs, attr)
          case ident           => mapPairs(Seq(ident), attr)
        }

      case q"Seq(..$vs)" if attr == null                                      => x(6, vs); vs.flatMap(v => resolveValues(q"$v"))
      case q"Seq(..$vs)" if vs.nonEmpty && vs.head.tpe <:< weakTypeOf[(_, _)] => x(7, vs); mapPairs(vs, attr)
      case q"Seq(..$vs)" if vs.size == 1 && attr.isMapAttr$                   => x(8, vs); mapPairs(vs, attr)
      case q"Seq(..$vs)"                                                      => x(9, vs); vs.flatMap(v => resolveValues(q"$v", att(q"$attr")))
      case other if attr == null                                              => x(10, other); resolveValues(other)
      case other                                                              => x(11, other); resolveValues(other, att(q"$attr"))
    }
  }

  def mapPairs(vs: Seq[Tree], attr: Tree = null) = {
    val keyValues = vs.map {
      case q"scala.Predef.ArrowAssoc[$t1]($k).->[$t2]($v)" => (extract(q"$k"), extract(q"$v"))
      case q"scala.Tuple2.apply[$t1, $t2]($k, $v)"         => (extract(q"$k"), extract(q"$v"))
      case ident                                           => (extract(ident), "__pair__")
    }
    if (attr.isMapAttr || attr.isMapAttr$)
      MapEq(keyValues.map(kv => (kv._1.asInstanceOf[String], kv._2)))
    else
      Replace(keyValues)
  }

  def extract(t: Tree) = t match {
    case Constant(v: String)                            => v
    case Literal(Constant(v: String))                   => v
    case Ident(TermName(v: String))                     => "__ident__" + v
    case Select(This(TypeName(_)), TermName(v: String)) => "__ident__" + v
    case v                                              => v
  }


  def resolveValues(tree: Tree, at: att = null) = {
    def resolve(tree0: Tree, values: Seq[Tree] = Seq()): Seq[Tree] = tree0 match {
      case q"$a.or($b)"             => resolve(b, resolve(a, values))
      case q"${_}.string2Model($v)" => values :+ v
      case Apply(_, vs)             => values ++ vs.flatMap(resolve(_))
      case v                        => values :+ v
    }
    def validateStaticEnums(value: Any) = {
      if (at.isAnyEnum
        && value != "?"
        && !value.toString.startsWith("__ident__")
        && !at.enumValues.contains(value.toString)
      ) abort(s"[Dsl2Model:validateStaticEnums] '$value' is not among available enum values of attribute ${at.kwS}:\n  " +
        at.enumValues.sorted.mkString("\n  "))
      value
    }
    if (at == null)
      resolve(tree) map extract
    else
      resolve(tree) map extract map validateStaticEnums
  }
}

private[molecule] object Dsl2Model {
  def inst(c0: Context) = new {val c: c0.type = c0} with Dsl2Model[c0.type]

  // Main dsl-converter + post-checks of created model
  def apply(c: Context)(dsl: c.Expr[NS]): Model = {

    // Raw model
    val elements0 = inst(c).resolve(dsl.tree)


    // Sanity checks .......................................................................

    def abort(i: Int, msg: String) = c.abort(c.enclosingPosition, s"[Dsl2Model:apply ($i)] " + msg)


    // Avoid ending with a ref
    elements0.last match {
      case b: Bond => abort(1, s"Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
      case _       => "ok"
    }

    // No non-tx meta attributes after TxMetaData
    def txError(s: String) = abort(2, s"Molecule not allowed to have any attributes after transaction annotation. Found" + s)
    elements0.foldLeft(0) {
      case (_, e: TxMetaData)                     => 1
      case (1, Atom(_, "attr", _, _, _, _, _, _)) => txError(s" attribute `a`")
      case (1, Meta(_, _, "e", _, _))             => txError(s" attribute `e`")
      case (1, Meta(_, _, "v", _, _))             => txError(s" attribute `v`")
      case (1, Atom(_, name, _, _, _, _, _, _))   => txError(s" attribute `$name`")
      case (1, Bond(_, name, _, _, _))            => txError(s" reference `${name.capitalize}`")
      case (1, e)                                 => txError(s": " + e)
      case _                                      => 0
    }

    // Molecule should at least have one mandatory attribute
    elements0.collectFirst {
      case a@Atom(_, name, _, _, _, _, _, _) if name.last != '$' => a
      case b: Bond                                               => b
      case g: Nested                                             => g
      case m@Meta(_, "txInstant", _, _, _)                       => m
      case c: Composite                                          => c
    } getOrElse abort(3, s"Molecule is empty or has only meta/optional attributes. Please add one or more attributes.")


    // Only tacit attributes allowed to have AND semantics for self-joins
    def checkAndSemantics(elements: Seq[Element]): Unit = elements foreach {
      case a@Atom(_, name, _, 1, And(_), _, _, _) if name.last != '_' =>
        abort(4, s"Card-one attribute `$name` cannot return multiple values.\n" +
          "A tacit attribute can though have AND expressions to make a self-join.\n" +
          s"If you want this, please make the attribute tacit by appending an underscore: `${name}_`")
      case a@Atom(_, name, _, 3, And(_), _, _, _) if name.last != '_' =>
        abort(5, s"Map attribute `$name` is to be considered a card-one container for keyed variations of one value and " +
          """can semantically therefore not return "multiple values".""" +
          "\nA tacit map attribute can though have AND expressions to make a self-join.\n" +
          s"If you want this, please make the map attribute tacit by appending an underscore: `${name}_`")
      case Nested(bond, elements2)                                    => checkAndSemantics(elements2)
      case _                                                          => "ok"
    }
    checkAndSemantics(elements0)

    // Nested molecules not allowed in composites (for now - todo)
    elements0.collect {
      case c: Composite => c.elements collectFirst {
        case n: Nested => abort(6, "Nested molecules in composites not yet implemented (todo)")
      }
    }

    // Why do we need to extract values at this point?!
    import c.universe._
    def extract(raw: Any) = raw match {
      case Literal(Constant(s: String))  => s
      case Literal(Constant(i: Int))     => i
      case Literal(Constant(l: Long))    => l
      case Literal(Constant(f: Float))   => f
      case Literal(Constant(d: Double))  => d
      case Literal(Constant(b: Boolean)) => b
      case other                         => other
    }
    def vs(values: Seq[Any]) = values map extract
    def dupS(values: Seq[Any]) = vs(values).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq
    def dupValues(pairs: Seq[(Any, Any)]) = vs(pairs.map(_._2)).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq
    def dupKeys(pairs: Seq[(Any, Any)]) = vs(pairs.map(_._1)).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq

    def clean(attr: String) = attr.last match {
      case '_' => attr.init
      case '$' => attr.init
      case _   => attr
    }

    // Catch duplicate update values
    elements0.collectFirst {
      case a@Atom(ns, name, _, _, Add_(vs), _, _, _) if dupS(vs).nonEmpty =>
        abort(11, s"Can't add duplicate values to attribute `:$ns/$name`:\n" + dupS(vs).mkString("\n"))

      case a@Atom(ns, name, _, _, Replace(pairs), _, _, _) if dupValues(pairs).nonEmpty =>
        abort(12, s"Can't replace with duplicate values of attribute `:$ns/$name`:\n" + dupValues(pairs).mkString("\n"))

      case a@Atom(ns, name, _, _, Eq(vs), _, _, _) if dupS(vs).nonEmpty =>
        abort(13, s"Can't apply duplicate values to attribute `:$ns/$name`:\n" + dupS(vs).mkString("\n"))

      case a@Atom(ns, name, _, _, MapAdd(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
        val dups = dupKeys(pairs)
        val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
        abort(14, s"Can't add multiple key/value pairs with the same key for attribute `:$ns/$name`:\n" + dupPairs.mkString("\n"))

      case a@Atom(ns, name, _, _, MapReplace(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
        val dups = dupKeys(pairs)
        val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
        abort(15, s"Can't replace multiple key/value pairs with the same key for attribute `:$ns/$name`:\n" + dupPairs.mkString("\n"))

      case Meta(_, "e_", _, _, EntValue) => abort(16, "Tacit entity only allowed if applying an entity id")

      case Atom(ns, name, _, 2, Distinct, _, _, _) => abort(17,
        s"`Distinct` keyword not supported for card many attributes like `:$ns/$name` (card many values already returned as Sets of distinct values).")
    }

    // Resolve generic elements ............................................................

    // Transfer generic values from Meta elements to Atoms and skip Meta elements
    val elements1: Seq[Element] = elements0.foldRight(Seq[Element](), Seq[Generic](), NoValue: Value) { case (element, (es, gs, v)) =>
      element match {
        case a@Atom(ns1, attr, _, _, _, _, _, _) if es.collectFirst {
          case Bond(ns, refAttr, refNs, _, _) if ns1 == ns && clean(attr) == refAttr => abort(10,
            s"Instead of getting the ref id with `$attr` please get it via the referenced namespace: `${refNs.capitalize}.e ...`")
        }.getOrElse(false)                                                                => abort(42, "we won't get here")
        case a: Atom if a.name != "attr" && gs.contains(NsValue) && !gs.contains(AttrVar) => abort(7, s"`ns` needs to have a generic `a` before")
        case a: Atom if gs.isEmpty                                                        => (a +: es, Nil, NoValue)
        case a: Atom if a.name == "attr"                                                  => (a.copy(gs = a.gs ++ gs, value = v) +: es, Nil, NoValue)
        case a: Atom                                                                      => (a.copy(gs = a.gs ++ gs) +: es, Nil, NoValue)
        case m@Meta(_, _, "e", g, v1)                                                     => (m +: es, g +: gs, v1)
        case Meta(_, _, _, g, v1)                                                         => (es, g +: gs, v1)
        case txmd@TxMetaData(txElems)                                                     => (txmd +: es, TxValue_(None) +: gs, NoValue)
        case tx: TxValue                                                                  => (es, tx +: gs, NoValue)
        case tx: TxValue_                                                                 => (es, tx +: gs, NoValue)
        case com@Composite(compositeElems)                                                => compositeElems.last match {
          case txmd: TxMetaData => {
            // Add TxValue internally to previous Atom
            val baseElems = compositeElems.init
            val atomWithTxValue = baseElems.last match {
              case a: Atom    => a.copy(gs = a.gs :+ TxValue_(None))
              case unexpected => abort(8, "Expected attribute before `tx`. Found: " + unexpected)
            }
            val compositeWithTx = Composite(baseElems.init :+ atomWithTxValue :+ txmd)
            (compositeWithTx +: es, gs, v)
          }
          case other            => (com +: es, gs, v)
        }
        case other                                                                        => (other +: es, gs, v)
      }
    }._1

    //     inst(c).x(30, dsl, elements0, elements1, Model(elements1))
    //         inst(c).x(30, elements0, elements1)
    //        inst(c).x(30, elements1)

    // Can't use generics on multiple attributes - why not?
    //    if (elements1.foldLeft(Seq[Boolean]()) {
    //      case (gss, Atom(_, _, _, _, _, _, gs, _)) if gs.nonEmpty && gs.flatMap {
    //        case b: Bidirectional => None // Bidirectional definitions are ok
    //        case generic          => Some(generic)
    //      }.nonEmpty    => gss :+ true
    //      case (gss, _) => gss
    //    }.length > 1)
    //      abort(16, "Generics (`v`, `tx`, `t`, `txInstant`, `op`) not allowed on multiple attributes")

    // Return checked model
    Model(elements1)
  }
}