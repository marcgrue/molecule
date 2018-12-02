package molecule.transform
import molecule.ast.model._
import molecule.boilerplate.attributes._
import molecule.macros.{Cast, Json}
import molecule.transform.exception.Dsl2ModelException
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


private[molecule] trait Dsl2Model extends Cast with Json {
  val c: blackbox.Context
  import c.universe._
  val x = DebugMacro("Dsl2Model", 125, 124)

  override def abort(msg: String): Nothing = throw new Dsl2ModelException(msg)

  private[molecule] final def getModel(dsl: Tree): (
    Model,
      List[List[Tree]],
      List[List[Int => Tree]],
      List[List[Int => Tree]],
      List[String],
      Boolean,
      List[Tree],
      List[Int => Tree],
      List[Int => Tree]
    ) = {

    var isComposite: Boolean = false
    var collectCompositeElements: Boolean = false

    var post: Boolean = true
    var postTypes: List[Tree] = List.empty[Tree]
    var postCasts: List[Int => Tree] = List.empty[Int => Tree]
    var postJsons: List[Int => Tree] = List.empty[Int => Tree]

    var types: List[List[Tree]] = List(List.empty[Tree])
    var casts: List[List[Int => Tree]] = List(List.empty[Int => Tree])
    var jsons: List[List[Int => Tree]] = List(List.empty[Int => Tree])
    var tempJsons: List[((String, String) => Int => Tree, String, String)] = List.empty[((String, String) => Int => Tree, String, String)]
    var nestedRefAttrs: List[String] = List.empty[String]

    var tx: String = ""
    var hasVariables: Boolean = false
    var standard: Boolean = true
    var aggrType: String = ""

    def addSpecific(castLambda: Int => Tree, tpeStr: String): Unit = {
      if (post) {
        postTypes = tq"${TypeName(tpeStr)}" +: postTypes
        postCasts = castLambda +: postCasts
      } else {
        types = (tq"${TypeName(tpeStr)}" :: types.head) +: types.tail
        casts = (castLambda :: casts.head) +: casts.tail
      }
    }

    def addCast(castLambda: richTree => Int => Tree, t: richTree): Unit = if (t.name.last != '_') {
      val tpe = if (t.name.last == '$') t.card match {
        case 1 => tq"Option[${TypeName(t.tpeS)}]"
        case 2 => tq"Option[Set[${TypeName(t.tpeS)}]]"
        case 3 => tq"Option[Map[String, ${TypeName(t.tpeS)}]]"
        case 4 => tq"Option[${TypeName(t.tpeS)}]"
      } else t.card match {
        case 1 => tq"${TypeName(t.tpeS)}"
        case 2 => tq"Set[${TypeName(t.tpeS)}]"
        case 3 => tq"Map[String, ${TypeName(t.tpeS)}]"
        case 4 => tq"${TypeName(t.tpeS)}"
      }
      if (post) {
        postTypes = tpe +: postTypes
        postCasts = castLambda(t) +: postCasts
      } else {
        types = (tpe :: types.head) +: types.tail
        casts = (castLambda(t) :: casts.head) +: casts.tail
      }
    }

    def addJson(fn: (String, String) => Int => Tree, tpe: String, field: String): Unit = {
      tempJsons = (fn, tpe, field) +: tempJsons
    }

    def addJson1(fn1: (String, String) => Int => Tree, t: richTree): Unit = if (t.name.last != '_') {
      addJson(fn1, t.tpeS, t.ns + "." + t.nameClean)
    }

    def addJsonCard(fn1: (String, String) => Int => Tree, fn2: (String, String) => Int => Tree, t: richTree): Unit = if (t.name.last != '_') {
      addJson(if (t.card == 1) fn1 else fn2, t.tpeS, t.ns + "." + t.nameClean)
    }

    def addNamespacedJsonLambdas(refAttrPrefix: String): Unit = if (tempJsons.nonEmpty) {
      val prefix = tx + refAttrPrefix
      val resolved: List[Int => Tree] = tempJsons.map { case (fn, tpe, field) =>
        fn(tpe, prefix + field)
      }
      if (post) {
        postJsons = resolved ++ postJsons
      } else {
        jsons = (resolved ++ jsons.head) +: jsons.tail
      }
      // Reset for next Ref namespace
      tempJsons = List.empty[((String, String) => Int => Tree, String, String)]
    }

    def addJsonLambdas: Unit = if (tempJsons.nonEmpty) {
      val resolved: List[Int => Tree] = tempJsons.map { case (fn, tpe, field) =>
        fn(tpe, tx + field)
      }
      if (post) {
        postJsons = resolved ++ postJsons
      } else {
        jsons = (resolved ++ jsons.head) +: jsons.tail
      }
      // Reset for next Ref namespace
      tempJsons = List.empty[((String, String) => Int => Tree, String, String)]
    }


    def traverseElement(prev: Tree, p: richTree, element: Element): Seq[Element] = {
      if (p.isNS && !p.isFirstNS) {
        // x(710, prev, element)
        resolve(prev) :+ element
      } else {
        // x(711, element)
        // First element
        Seq(element)
      }
    }

    def traverseElements(prev: Tree, p: richTree, elements: Seq[Element]): Seq[Element] = {
      if (isComposite) {
        // x(741, prev, elements)
        val prevElements = resolve(prev)
        if (collectCompositeElements) {
          val result = prevElements :+ Composite(elements)
          // x(745, prevElements, elements, result, collectCompositeElements, casts, types)
          result
        } else {
          val result = Seq(Composite(prevElements), Composite(elements))
          // x(744, prevElements, elements, result, collectCompositeElements, casts, types)
          result
        }
      } else {
        if (p.isNS && !p.isFirstNS) {
          // x(751, elements)
          resolve(prev) ++ elements
        } else {
          // x(752, elements)
          // First elements
          elements
        }
      }
    }

    def resolve(tree: Tree): Seq[Element] = tree match {
      case q"$prev.$attr" =>
        // x(100, attr)
        resolveAttr(tree, richTree(tree), prev, richTree(prev), attr.toString())

      case q"$prev.$attr.apply(..$vs)" =>
        // x(200, attr, vs)
        resolveApply(tree, richTree(q"$prev.$attr"), prev, richTree(prev), attr.toString(), q"$vs")

      case q"$prev.$attr.apply[..$tpes](..$vs)" =>
        // x(300, attr)
        resolveTypedApply(tree, richTree(prev))

      case q"$prev.$op(..$vs)" =>
        // x(400, prev, op)
        resolveOperation(tree)

      case q"$prev.$manyRef.*[..$types]($nested)" =>
        // x(500, manyRef)
        resolveNested(prev, richTree(prev), manyRef, nested)

      case q"$prev.+[..$types]($subComposite)" =>
        // x(600, prev)
        resolveComposite(prev, richTree(prev), q"$subComposite")

      case other => abort(s"Unexpected DSL structure: $other\n${showRaw(other)}")
    }

    def resolveComposite(prev: Tree, p: richTree, subCompositeTree: Tree): Seq[Element] = {
      // x(801, prev, subCompositeTree)
      post = false
      isComposite = true
      collectCompositeElements = false
      val subCompositeElements = resolve(subCompositeTree)
      addJsonLambdas
      // x(802, prev, subCompositeElements)

      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts
      jsons = List.empty[Int => Tree] :: jsons

      val elements = traverseElements(prev, p, subCompositeElements)
      // x(803, elements)
      collectCompositeElements = true
      elements
    }

    def resolveAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = attrStr.last match {
      case '$' =>
        // x(140, attrStr)
        resolveOptionalAttr(tree, t, prev, p, attrStr)

      case '_' =>
        // x(160, attrStr)
        resolveTacitAttr(tree, t, prev, p, attrStr)

      case _ =>
        // x(110, attrStr)
        resolveMandatoryAttrOrRef(tree, t, prev, p, attrStr)
    }


    def resolveMandatoryAttrOrRef(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (Seq("e", "tx", "t", "txInstant", "op", "a", "v", "ns", "Self").contains(attrStr)) {
        // x(111, attrStr, t.tpeS)
        resolveMandatoryGenericAttr(tree, p)

      } else if (t.isEnum) {
        // x(121, t.tpeS)
        addSpecific(castEnum(t), "String")
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        // x(122, t.tpeS)
        addCast(castMandatoryMapAttr, t)
        addJson1(jsonMandatoryMapAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        addCast(castMandatoryAttr, t)
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        // x(123, t.tpeS, jsons, postJsons, tempJsons)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, VarValue, gs = bi(tree, t)))

      } else if (attrStr.head == '_') {
         x(124, t.tpeS)
        traverseElement(prev, p, ReBond(firstLow(c.typecheck(tree).tpe.typeSymbol.name.toString.replaceFirst("_[0-9]+$", "")), ""))

      } else if (t.isRef) {
        // x(125, t.tpeS, t.card, t.refCard)
        addNamespacedJsonLambdas(firstLow(attrStr) + ".")
        traverseElement(prev, p, Bond(t.refThis, firstLow(attrStr), t.refNext, t.refCard, bi(tree, t)))

      } else if (t.isRefAttr) {
        // x(126, t.tpeS)
        addCast(castMandatoryAttr, t)
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, "Long", t.card, VarValue, gs = bi(tree, t)))

      } else {
        abort("Unexpected mandatory attribute/reference: " + t)
      }
    }

    def resolveOptionalAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (t.isEnum$) {
        // x(141, t.tpeS)
        addCast(castEnumOpt, t)
        addJsonCard(jsonOptOneEnum, jsonOptManyEnum, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr$) {
        // x(142, t.tpeS)
        addCast(castOptionalMapAttr, t)
        addJson1(jsonOptionalMapAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr$) {
        addCast(castOptionalAttr, t)
        addJsonCard(jsonOptOneAttr, jsonOptManyAttr, t)
        // x(143, t.tpeS, types, postTypes)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, VarValue, gs = bi(tree, t)))

      } else if (t.isRefAttr$) {
        // x(144, t.tpeS)
        addCast(castOptionalRefAttr, t)
        addJsonCard(jsonOptOneAttr, jsonOptManyAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, "Long", t.card, VarValue, gs = bi(tree, t)))

      } else {
        abort("Unexpected optional attribute: " + t)
      }
    }

    def resolveTacitAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (Seq("e_", "ns_", "v_", "tx_", "t_", "txInstant_", "op_").contains(attrStr)) {
        resolveTacitGenericAttr(tree, p)

      } else if (t.isEnum) {
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isValueAttr) {
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, VarValue, gs = bi(tree, t)))

      } else if (t.isRefAttr) {
        traverseElement(prev, p, Atom(t.ns, t.name, "Long", t.card, VarValue, gs = bi(tree, t)))

      } else if (t.isMapAttr) {
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else {
        abort("Unexpected tacit attribute: " + t)
      }
    }

    def resolveMandatoryGenericAttr(tree: Tree, p: richTree): Seq[Element] = tree match {
      case q"$prev.e" =>
        // x(99, prev)
        addSpecific(castOneAttr("Long"), "Long")
        addJson(jsonOneAttr, "Long", p.ns + ".e")
        traverseElement(q"$prev", p, Meta("", "eid", "e", NoValue, EntValue))

      case q"$prev.a" =>
        addSpecific(castOneAttr("String"), "String")
        addJson(jsonOneAttr, "String", p.ns + ".a")
        traverseElement(prev, p, Atom("?", "attr", "a", 1, NoValue))

      case q"$prev.ns" =>
        addSpecific(castOneAttr("String"), "String")
        addJson(jsonOneAttr, "String", p.ns + ".ns")
        traverseElement(prev, p, Atom("ns", "?", "ns", 1, NoValue))

      case q"$prev.v" =>
        addSpecific(castOneAttr("Any"), "Any")
        addJson(jsonOneAttr, "Any", p.ns + ".v")
        traverseElement(prev, p, Meta("", "", "v", AttrVar(""), NoValue))

      // Internal predefined db functions
      case q"$prev.Db.tx" =>
        addSpecific(castOneAttr("Long"), "Long")
        addJson(jsonOneAttr, "Long", "db.tx")
        traverseElement(prev, p, Atom("db", "tx", "Long", 1, VarValue))

      case q"$prev.Db.t" =>
        addSpecific(castOneAttr("Long"), "Long")
        addJson(jsonOneAttr, "Long", "db.t")
        traverseElement(prev, p, Atom("db", "txT", "Long", 1, VarValue))

      case q"$prev.Db.txInstant" =>
        addSpecific(castOneAttr("java.util.Date"), "java.util.Date")
        addJson(jsonOneAttr, "java.util.Date", "db.txInstant")
        traverseElement(prev, p, Atom("db", "txInstant", "Long", 1, VarValue))

      case q"$prev.Db.op" =>
        addSpecific(castOneAttr("Boolean"), "Boolean")
        addJson(jsonOneAttr, "Boolean", "db.op")
        traverseElement(prev, p, Atom("db", "op", "Boolean", 1, VarValue))

      // ns.txInstant.attr - `txInstant` doesn't relate to any previous attr
      case q"$prev.tx" if !p.isAttr        => abort(s"Please add `tx` after an attribute or another transaction value")
      case q"$prev.t" if !p.isAttr         => abort(s"Please add `t` after an attribute or another transaction value")
      case q"$prev.t" if !p.isAttr         => abort(s"Please add `t` after an attribute or another transaction value")
      case q"$prev.txInstant" if !p.isAttr => abort(s"Please add `txInstant` after an attribute or another transaction value")
      case q"$prev.op" if !p.isAttr        => abort(s"Please add `op` after an attribute or another transaction value")

      // Generic attributes not after optional attributes
      case q"$prev.tx" if prev.toString.endsWith("$")        => abort(s"Optional attributes (`" + p.name + "`) can't be followed by generic attributes (`tx`).")
      case q"$prev.t" if prev.toString.endsWith("$")         => abort(s"Optional attributes (`" + p.name + "`) can't be followed by generic attributes (`t`).")
      case q"$prev.txInstant" if prev.toString.endsWith("$") => abort(s"Optional attributes (`" + p.name + "`) can't be followed by generic attributes (`txInstant`).")
      case q"$prev.op" if prev.toString.endsWith("$")        => abort(s"Optional attributes (`" + p.name + "`) can't be followed by generic attributes (`op`).")

      case q"$prev.tx" =>
        addSpecific(castOneAttr("Long"), "Long")
        addJson(jsonOneAttr, "Long", p.ns + ".tx")
        traverseElement(prev, p, Meta("db", "tx", "tx", TxValue(None), NoValue))

      case q"$prev.t" =>
        addSpecific(castOneAttr("Long"), "Long")
        addJson(jsonOneAttr, "Long", p.ns + ".t")
        traverseElement(prev, p, Meta("db", "txT", "tx", TxTValue(None), NoValue))

      case q"$prev.txInstant" =>
        addSpecific(castOneAttr("java.util.Date"), "java.util.Date")
        addJson(jsonOneAttr, "java.util.Date", p.ns + ".txInstant")
        traverseElement(prev, p, Meta("db", "txInstant", "tx", TxInstantValue(None), NoValue))

      case q"$prev.op" =>
        addSpecific(castOneAttr("Boolean"), "Boolean")
        addJson(jsonOneAttr, "Boolean", p.ns + ".op")
        traverseElement(prev, p, Meta("db", "op", "tx", OpValue(None), NoValue))

      // Self join -----------------------------
      case q"$prev.Self" => traverseElement(prev, p, Self)
    }

    def resolveTacitGenericAttr(tree: Tree, p: richTree): Seq[Element] = tree match {
      case q"$prev.e_"         => traverseElement(prev, p, Meta("", "eid_", "e", NoValue, EntValue))
      case q"$prev.ns_"        => abort( s"""Generic namespace value `ns_` can only be used with an applied value i.e. `ns_("someNamespace")`""")
      case q"$prev.v_"         => abort( s"""Generic attribute value `v_` can only be used with an applied value i.e. `v_("some value")`""")
      case q"$prev.tx_"        => abort(s"Tacit `tx_` not allowed since all datoms have a tx value")
      case q"$prev.t_"         => abort(s"Tacit `t_` not allowed since all datoms have a t value")
      case q"$prev.txInstant_" => abort(s"Tacit `txInstant_` not allowed since all datoms have a txInstant value")
      case q"$prev.op_"        => abort(s"Tacit `op_` not allowed since all datoms have a `op value")
    }

    def resolveApply(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String, vs: Tree): Seq[Element] = {
      if (Seq("tx", "t", "txInstant", "op", "tx_", "t_", "txInstant_", "op_", "v", "v_", "ns", "ns_").contains(attrStr)) {
        def singleValue = vs match {
          case q"scala.collection.immutable.List($pkg.?)" => Qm
          case q"scala.collection.immutable.List($v)"     => extract(v)
          case _                                          => abort("Generic attributes only accept 1 argument. Found: " + vs)
        }
        // x(220, attrStr, vs, singleValue)
        attrStr match {
          case "tx" =>
            addSpecific(castOneAttr("Long"), "Long")
            addJson(jsonOneAttr, "Long", p.ns + ".tx")
            traverseElement(prev, p, Meta("db", "tx", "tx", TxValue(Some(singleValue)), NoValue))

          case "t" =>
            addSpecific(castOneAttr("Long"), "Long")
            addJson(jsonOneAttr, "Long", p.ns + ".t")
            traverseElement(prev, p, Meta("db", "txT", "tx", TxTValue(Some(singleValue)), NoValue))

          case "txInstant" =>
            addSpecific(castOneAttr("java.util.Date"), "java.util.Date")
            addJson(jsonOneAttr, "java.util.Date", p.ns + ".txInstant")
            traverseElement(prev, p, Meta("db", "txInstant", "tx", TxInstantValue(Some(singleValue)), NoValue))

          case "op" =>
            addSpecific(castOneAttr("Boolean"), "Boolean")
            addJson(jsonOneAttr, "Boolean", p.ns + ".op")
            traverseElement(prev, p, Meta("db", "op", "tx", OpValue(Some(singleValue)), NoValue))

          case "v" =>
            addSpecific(castOneAttr("Any"), "Any")
            addJson(jsonOneAttr, "Any", p.ns + ".v")
            traverseElement(prev, p, Meta("", "", "v", AttrVar(""), modelValue("apply", null, vs)))

          case "ns" =>
            addSpecific(castOneAttr("String"), "String")
            addJson(jsonOneAttr, "String", p.ns + ".ns")
            traverseElement(prev, p, Atom("ns", "?", "ns", 1, modelValue("apply", null, vs)))

          case "tx_"        => traverseElement(prev, p, Meta("db", "tx", "tx", TxValue_(Some(singleValue)), NoValue))
          case "t_"         => traverseElement(prev, p, Meta("db", "txT", "tx", TxTValue_(Some(singleValue)), NoValue))
          case "txInstant_" => traverseElement(prev, p, Meta("db", "txInstant", "tx", TxInstantValue_(Some(singleValue)), NoValue))
          case "op_"        => traverseElement(prev, p, Meta("db", "op", "tx", OpValue_(Some(singleValue)), NoValue))
          case "v_"         => traverseElement(prev, p, Meta("", "", "v", NoValue, modelValue("apply", null, vs)))
          case "ns_"        => traverseElement(prev, p, Atom("ns_", "?", "ns", 1, modelValue("apply", null, vs)))
        }

      } else if (t.isFirstNS) {
        // x(230, attrStr)
        tree match {
          case q"$prev.$ns.apply($pkg.?)"             => traverseElement(prev, p, Meta(firstLow(ns), "eid_", "e", NoValue, Eq(Seq(Qm))))
          case q"$prev.$ns.apply($eid)" if t.isBiEdge => traverseElement(prev, p, Meta(firstLow(ns), "eid_", "e", BiEdge, Eq(Seq(extract(eid)))))
          case q"$prev.$ns.apply(..$eids)"            => traverseElement(prev, p, Meta(firstLow(ns), "eid_", "e", NoValue, Eq(resolveValues(q"Seq(..$eids)"))))
        }
      } else if (tree.isMapAttrK) {
        tree match {
          case t@q"$prev.$mapAttr.apply($key)" =>
            val tpeStr = t.tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString
            val ns = new nsp(t.tpe.typeSymbol.owner).toString
            // // x(240, attrStr, tpeStr)
            if (attrStr.last != '_') {
              addSpecific(castKeyedMapAttr(tpeStr), tpeStr)
              addJson(jsonKeyedMapAttr, tpeStr, ns + "." + clean(mapAttr.toString))
            }
            traverseElement(prev, p, Atom(ns, mapAttr.toString, tpeStr, 4, VarValue, None, Nil, Seq(extract(q"$key").toString)))
        }
      } else {
        tree match {
          case q"$prev.$ref.apply(..$values)" if t.isRef => abort(s"Can't apply value to a reference (`$ref`)")
          case tr@q"$prev.$attr.apply(..$values)"        =>
            // x(260, attrStr)
            traverseElement(prev, p, resolveOp(q"$prev.$attr", richTree(q"$prev.$attr"), prev, p, attr.toString(), q"apply", q"Seq(..$values)"))

        }
      }
    }

    def resolveTypedApply(tree: Tree, p: richTree): Seq[Element] = tree match {
      case q"$prev.Tx.apply[..$t]($txMolecule)" =>
        // x(310, "Tx", txMolecule)
        // tx prefix for json field names. Available for tx molecule resolve. But needs to be blanked for appending attributes of the main molecule.
        tx = "tx."
        val txMetaData = TxMetaData(resolve(q"$txMolecule"))
        addJsonLambdas
        tx = ""
        traverseElement(prev, p, txMetaData)

      case q"$prev.e.apply[..$types]($nested)" if !p.isRef =>
        // x(320, "e")
        Seq(Nested(Bond("", "", "", 2), Meta("", "", "e", NoValue, EntValue) +: resolve(q"$nested")))

      case q"$prev.e_.apply[..$types]($nested)" if !p.isRef =>
        // x(330, "e_")
        Seq(Nested(Bond("", "", "", 2), resolve(q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" if !q"$prev.$manyRef".isRef =>
        // x(340, manyRef, nested)
        Seq(Nested(Bond("", "", "", 2), nestedElements(q"$prev.$manyRef", firstLow(manyRef.toString), q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" =>
        // x(350, manyRef, nested)
        traverseElement(prev, p, nested1(prev, p, manyRef, nested))
    }

    def resolveOperation(tree: Tree): Seq[Element] = tree match {

      // Attribute map using k/apply
      case t@q"$prev.$keyedAttr.k(..$keys).$op(..$values)" =>
        // x(410, keyedAttr, richTree(q"$prev.$keyedAttr").tpeS)
        val element = resolveOp(q"$prev.$keyedAttr", richTree(q"$prev.$keyedAttr"), prev, richTree(prev), keyedAttr.toString(), q"$op", q"Seq(..$values)") match {
          case a: Atom => a.copy(keys = getValues(q"$keys").asInstanceOf[Seq[String]])
        }
        traverseElement(prev, richTree(prev), element)

      // Keyed attribute map operation
      case t@q"$prev.$keyedAttr.apply($key).$op(..$values)" if q"$prev.$keyedAttr($key)".isMapAttrK =>
        val tpe = c.typecheck(q"$prev.$keyedAttr($key)").tpe
        val tpeStr = tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString
        // x(420, keyedAttr, tpeStr)
        if (keyedAttr.toString().last != '_') {
          addSpecific(castKeyedMapAttr(tpeStr), tpeStr)
          addJson(jsonKeyedMapAttr, tpeStr, t.ns + "." + clean(keyedAttr.toString))
        }
        traverseElement(prev, richTree(prev),
          Atom(new nsp(tpe.typeSymbol.owner).toString, keyedAttr.toString, tpeStr, 4, modelValue(op.toString(), t, q"Seq(..$values)"), None, Nil, Seq(extract(q"$key").toString))
        )

      // Attribute operations -----------------------------
      case t@q"$prev.$attr.$op(..$values)" =>
        // x(430, attr)
        traverseElement(prev, richTree(prev), resolveOp(q"$prev.$attr", richTree(q"$prev.$attr"), prev, richTree(prev), attr.toString(), q"$op", q"Seq(..$values)"))
    }

    def resolveNested(prev: Tree, p: richTree, manyRef: TermName, nested: Tree): Seq[Element] = {
      // x(521, jsons, tempJsons, post, postJsons)
      // From now on, elements are part of nested structure
      // Transfer any tempJson lambdas to postJson lambdas
      addJsonLambdas
      post = false
      // Add nested elements on current level
      // x(522, jsons, tempJsons, post, postJsons)
      val nestedElement = nested1(prev, p, manyRef, q"$nested")
      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts
      jsons = List.empty[Int => Tree] :: jsons
      // x(523, nestedElement, jsons, tempJsons, post, postJsons)
      traverseElement(prev, p, nestedElement)
    }

    def nested1(prev: Tree, p: richTree, manyRef: TermName, nestedTree: Tree) = {
      val refNext = q"$prev.$manyRef".refNext
      val parentNs = prev match {
        case q"$pre.apply($value)" if p.isMapAttrK      => new nsp(c.typecheck(prev).tpe.typeSymbol.owner)
        case q"$pre.apply($value)" if p.isAttr          => richTree(pre).ns
        case q"$pre.apply($value)"                      => richTree(pre).name
        case _ if prev.symbol.name.toString.head == '_' => firstLow(prev.tpe.typeSymbol.name.toString.replaceFirst("_[0-9]+$", ""))
        case q"$pre.e" if p.isAttr                      => q"$pre".symbol.name
        case _ if p.isAttr                              => p.ns
        case _ if p.isRef                               => p.refNext
        case _                                          => p.name
      }
       x(510, prev, parentNs, jsons, tempJsons, post, postJsons


       )
      val (ns, refAttr) = (firstLow(parentNs.toString), firstLow(manyRef))
      nestedRefAttrs = nestedRefAttrs :+ s"$ns.$refAttr"
      val nestedElems = nestedElements(q"$prev.$manyRef", refNext, nestedTree)
      addJsonLambdas
      // x(511, nestedRefAttrs, nestedElems, jsons, tempJsons, post, postJsons)
      Nested(Bond(ns, refAttr, refNext, 2, bi(q"$prev.$manyRef", richTree(q"$prev.$manyRef"))), nestedElems)
    }

    def nestedElements(manyRef: Tree, refNext: String, nested: Tree): Seq[Element] = {
      val nestedElements = resolve(nested)
      val nestedNs = curNs(nestedElements.head)
      if (refNext != nestedNs) {
        // Find refs in `manyRef` namespace and match the target type with the first namespace of the first nested element
        val refs = c.typecheck(manyRef).tpe.members.filter(e => e.isMethod && e.asMethod.returnType <:< weakTypeOf[Ref[_, _]])
        val refPairs = refs.map(r => r.name -> r.typeSignature.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.last.typeSymbol.name)
        val refPairsFiltered = refPairs.filter(_._2.toString == nestedNs.capitalize)
        if (refPairsFiltered.isEmpty) {
          nestedElements
        } else if (refPairsFiltered.size == 1) {
          val (refAttr, refNs) = refPairsFiltered.head
          Bond(refNext, firstLow(refAttr), firstLow(refNs), 2, bi(manyRef, richTree(manyRef))) +: nestedElements
        } else
          abort(s"`$manyRef` has more than one ref pointing to `$nestedNs`:\n${refPairs.mkString("\n")}")
      } else {
        nestedElements
      }
    }

    def bi(tree: Tree, t: richTree): Seq[Generic] = if (t.isBidirectional) {
      if (t.isBiSelfRef) {
        Seq(BiSelfRef(t.refCard))

      } else if (t.isBiSelfRefAttr) {
        Seq(BiSelfRefAttr(t.card))

      } else if (t.isBiOtherRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiOtherRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiOtherRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else if (t.isBiOtherRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiOtherRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiOtherRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else if (t.isBiEdgeRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiEdgeRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiEdgeRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else if (t.isBiEdgeRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiEdgeRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiEdgeRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else if (t.isBiEdgePropRef) {
        Seq(BiEdgePropRef(t.refCard))

      } else if (t.isBiEdgePropAttr) {
        Seq(BiEdgePropAttr(t.card))

      } else if (t.isBiEdgePropRefAttr) {
        Seq(BiEdgePropRefAttr(t.card))

      } else if (t.isBiTargetRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiTargetRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiTargetRef(t.refCard, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else if (t.isBiTargetRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiTargetRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiTargetRefAttr(t.card, ":" + firstLow(baseType.owner.name) + "/" + baseType.name))

      } else {
        throw new Dsl2ModelException("Unexpected Bidirectional: " + t)
      }
    } else {
      Seq.empty[Generic]
    }

    def addAttrOrAggr(attr: String, t: richTree, tpeStr: String = "", apply: Boolean = false): Unit = {
      if (standard) {
        // x(81, attr)
        if (t.name.last != '$') {
          addCast(castMandatoryAttr, t)
          addJsonCard(jsonOneAttr, jsonManyAttr, t)
        } else {
          if (apply) addCast(castOptionalApplyAttr, t) else addCast(castOptionalAttr, t)
          addJsonCard(jsonOptOneAttr, jsonOptManyAttr, t)
        }
      } else {
        // x(82, attr, s"aggrType: '$aggrType'")
        attr.last match {
          case '_' | '$' => abort("Only mandatory attributes are allowed to aggregate")
          case _         => aggrType match {
            case "Int" =>
              addSpecific(castAggrInt, s"Int")
              addJson(jsonOneAttr, "Int", t.ns + "." + t.nameClean)

            case "Double" =>
              addSpecific(castAggrDouble, s"Double")
              addJson(jsonOneAttr, "Double", t.ns + "." + t.nameClean)

            case "list" =>
              addSpecific(castAggrListVector(tpeStr), s"List[$tpeStr]")
              addJson(jsonAggrListVector, tpeStr, t.ns + "." + t.nameClean)

            case "listSet" =>
              addSpecific(castAggrListHashSet(tpeStr), s"List[$tpeStr]") // Ns.str.int(distinct).get
              addJson(jsonAggrListVector, tpeStr, t.ns + "." + t.nameClean)

            case "listRand" =>
              addSpecific(castAggrListLazySeq(tpeStr), s"List[$tpeStr]")
              addJson(jsonAggrListLazySeq, tpeStr, t.ns + "." + t.nameClean)

            case "lazySeqHead" =>
              addSpecific(castAggrLazySeq(tpeStr), tpeStr)
              addJson(jsonAggrLazySeq, tpeStr, t.ns + "." + t.nameClean)

            case "vectorHead" =>
              addSpecific(castAggrVector(tpeStr), tpeStr)
              addJson(jsonAggrVector, tpeStr, t.ns + "." + t.nameClean)

            case _ =>
              addSpecific(castAggr(tpeStr), tpeStr)
              addJson(jsonAggr, tpeStr, t.ns + "." + t.nameClean)
          }
        }
        standard = true
        aggrType = ""
      }
    }

    def resolveOp(tree: Tree, t: richTree, prev: Tree, p: richTree, attr: String, op: Tree, values0: Tree): Element = {
      val value: Value = modelValue(op.toString(), tree, values0)

      if (attr.head.isUpper) {
        // x(91, attr, value)
        Atom(t.name, t.name, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (List("e", "e_", "a", "a_", "ns", "ns_").contains(attr)) {
        // x(92, attr, values0, value)
        attr match {
          case "e" if p.isRef  => addAttrOrAggr(attr, t, "Long"); Meta(p.name, p.refNext, "e", NoValue, value)
          case "e_" if p.isRef => addAttrOrAggr(attr, t, "Long"); Meta(p.name, p.refNext, "e", NoValue, value)
          case "e"             => addAttrOrAggr(attr, t, "Long"); Meta(p.name, attr, "e", NoValue, value)
          case "e_"            => addAttrOrAggr(attr, t, "Long"); Meta(p.name, attr, "e", NoValue, value)
          case "a"             => addAttrOrAggr(attr, t, "String"); Atom("?", "attr", "a", 1, value, gs = bi(tree, t))
          case "a_"            => addAttrOrAggr(attr, t, "String"); Atom("?", "attr_", "a", 1, value, gs = bi(tree, t))
          case "ns"            => addAttrOrAggr(attr, t, "String"); Atom("ns", "?", "ns", 1, value, gs = bi(tree, t))
          case "ns_"           => addAttrOrAggr(attr, t, "String"); Atom("ns_", "?", "ns", 1, value, gs = bi(tree, t))
        }

      } else if (t.isMapAttr) {
        // x(93, attr, value)
        addCast(castMandatoryMapAttr, t)
        addJson1(jsonMandatoryMapAttr, t)
        Atom(t.ns, attr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isMapAttr$) {
        // x(94, attr, value)
        addCast(castOptionalMapApplyAttr, t)
        addJson1(jsonOptionalMapAttr, t)
        Atom(t.ns, attr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isAttr) {
        // x(95, attr, value)
        addAttrOrAggr(attr, t, t.tpeS, true)
        Atom(t.ns, attr, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else {
        abort(s"Unexpected attribute operation for `$attr` having value: " + value)
      }
    }


    // Values ================================================================================

    def modelValue(op: String, attr: Tree, values0: Tree): Value = {
      val t = if (attr == null) null else richTree(attr)
      def errValue(i: Int, v: Any) = abort(s"Unexpected resolved model value for `${t.name}.$op`: $v")
      val values = getValues(values0, t)
      // x(60, op, attr, values0, values, values0.raw)
      op match {
        case "applyKey"    => NoValue
        case "apply"       => values match {
          case resolved: Value                         => resolved
          case vs: Seq[_] if t == null                 => Eq(vs)
          case vs: Seq[_] if t.isMapAttr && vs.isEmpty => MapEq(Seq())
          case vs: Seq[_]                              => Eq(vs)
          case other                                   => errValue(1, other)
        }
        case "k"           => values match {
          case vs: Seq[_] => MapKeys(vs.map(_.asInstanceOf[String]))
          case other      => errValue(2, other)
        }
        case "count"       => values match {
          case Fn("avg", i) => Length(Some(Fn("avg", i)))
          case other        => errValue(3, other)
        }
        case "not"         => values match {
          case qm: Qm.type                         => Neq(Seq(Qm))
          case Fn("not", None)                     => Neq(Nil)
          case (set: Set[_]) :: Nil if set.isEmpty => Neq(Nil)
          case vs: Seq[_] if vs.isEmpty            => Neq(Nil)
          case vs: Seq[_]                          => Neq(vs)
        }
        case "$bang$eq"    => values match {
          case qm: Qm.type => Neq(Seq(Qm))
          case vs: Seq[_]  => Neq(vs)
        }
        case "$less"       => values match {
          case qm: Qm.type => Lt(Qm)
          case vs: Seq[_]  => Lt(vs.head)
        }
        case "$greater"    => values match {
          case qm: Qm.type => Gt(Qm)
          case vs: Seq[_]  => Gt(vs.head)
        }
        case "$less$eq"    => values match {
          case qm: Qm.type => Le(Qm)
          case vs: Seq[_]  => Le(vs.head)
        }
        case "$greater$eq" => values match {
          case qm: Qm.type => Ge(Qm)
          case vs: Seq[_]  => Ge(vs.head)
        }
        case "contains"    => values match {
          case qm: Qm.type => Fulltext(Seq(Qm))
          case vs: Seq[_]  => Fulltext(vs)
        }
        case "assert"      => values match {
          case MapEq(pairs)  => AssertMapPairs(pairs)
          case mapped: Value => mapped
          case vs: Seq[_]    => AssertValue(vs)
        }
        case "retract"     => values match {
          case vs: Seq[_] if t.isMapAttr => RetractMapKeys(vs.map(_.toString))
          case vs: Seq[_]                => RetractValue(vs)
        }
        case "replace"     => values match {
          case MapEq(keyValues) => ReplaceMapPairs(keyValues)
          case resolved: Value  => resolved
          case Nil              => ReplaceValue(Nil)
        }
        case unexpected    => abort(s"Unknown operator '$unexpected'\nattr: ${t.name} \nvalue: $values0")
      }
    }


    def getValues(values: Tree, t: richTree = null): Any = {
      def aggr(fn: String, value: Option[Int] = None) = if (t.name.last == '_')
        abort(s"Aggregated values need to be returned. Please omit underscore from attribute `:${t.ns}/${t.name}`")
      else
        Fn(fn, value)

      def keyw(kw: String): Value = kw match {
        case "$qmark"                      => Qm
        case "Nil"                         => Fn("not")
        case "None"                        => Fn("not")
        case "unify" if t.name.last == '_' => Fn("unify")
        case "unify"                       => abort(s"Can only unify on tacit attributes. Please add underscore to attribute: `${t.name}_(unify)`")
        case "min"                         => standard = false; aggrType = "vectorHead"; aggr("min", Some(1))
        case "max"                         => standard = false; aggrType = "vectorHead"; aggr("max", Some(1))
        case "rand"                        => standard = false; aggrType = "lazySeqHead"; aggr("rand", Some(1))
        case "sample"                      => standard = false; aggrType = "vectorHead"; aggr("sample", Some(1))
        case "sum"                         => standard = false; aggr("sum")
        case "median"                      => standard = false; aggr("median")
        case "distinct"                    => standard = false; aggrType = "listSet"; Distinct
        case "count"                       => standard = false; aggrType = "Int"; aggr("count")
        case "countDistinct"               => standard = false; aggrType = "Int"; aggr("count-distinct")
        case "avg"                         => standard = false; aggrType = "Double"; aggr("avg")
        case "variance"                    => standard = false; aggrType = "Double"; aggr("variance")
        case "stddev"                      => standard = false; aggrType = "Double"; aggr("stddev")
      }

      def keyword(v: String): Boolean =
        Seq("$qmark", "Nil", "None", "count", "countDistinct", "min", "max", "sum", "avg", "unify", "distinct", "median", "variance", "stddev", "rand", "sample").contains(v)

      def single(value: Tree) = value match {
        case q"$pkg.$kw" if keyword(kw.toString)                => keyw(kw.toString)
        case q"$pkg.min.apply(${Literal(Constant(i: Int))})"    => standard = false; aggrType = "list"; aggr("min", Some(i))
        case q"$pkg.max.apply(${Literal(Constant(i: Int))})"    => standard = false; aggrType = "list"; aggr("max", Some(i))
        case q"$pkg.rand.apply(${Literal(Constant(i: Int))})"   => standard = false; aggrType = "listRand"; aggr("rand", Some(i))
        case q"$pkg.sample.apply(${Literal(Constant(i: Int))})" => standard = false; aggrType = "list"; aggr("sample", Some(i))
        case q"$a.and[$tpe]($b).and[$u]($c)"                    => And(resolveValues(q"Seq($a, $b, $c)"))
        case q"$a.and[$tpe]($b)"                                => And(resolveValues(q"Seq($a, $b)"))

        case q"scala.Some.apply[$tpe]($v)" =>
          // x(10, v)
          v match {
            case vm if vm.tpe <:< weakTypeOf[Map[_, _]] => vm match {
              case Apply(_, pairs) => mapPairs(pairs, t)
              case ident           => mapPairs(Seq(ident), t)
            }
            case ident if t.isMapAttr || t.isMapAttr$   => mapPairs(Seq(ident), t)
            case _                                      => Eq(resolveValues(q"$v"))
          }

        case v if !(v.tpe <:< weakTypeOf[Seq[Nothing]]) && v.tpe <:< weakTypeOf[Seq[(_, _)]] =>
          // x(11, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Set[Nothing]]) && v.tpe <:< weakTypeOf[Set[_]]
          && v.tpe.typeArgs.head <:< weakTypeOf[(_, _)] =>
          // x(12, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Map[Nothing, Nothing]]) && v.tpe <:< weakTypeOf[Map[_, _]] =>
          // x(13, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if t == null =>
          // x(14, v)
          Seq(resolveValues(q"$v"))

        case v if v.tpe <:< weakTypeOf[(_, _)] =>
          // x(15, v)
          mapPairs(Seq(v), t)

        case v if t.isMapAttr$ =>
          // x(16, v)
          mapPairs(Seq(v), t)

        case set if t.isMany && set.tpe <:< weakTypeOf[Set[_]] =>
          // x(17, set)
          Seq(resolveValues(q"$set", t.at).toSet)

        case vs if t.isMany =>
          // x(18, vs)
          vs match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(19, vs)
              sets.map(set => resolveValues(q"$set", t.at).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(20, vs)
              sets.map(set => resolveValues(q"$set", t.at).toSet)

            case _ =>
              // x(21, vs)
              resolveValues(q"$vs", t.at)
          }
        case other          =>
          // x(22, other)
          resolveValues(q"Seq($other)", t.at)
      }

      def multiple(values: Seq[Tree]) = values match {
        case vs if t == null =>
          // x(30, vs)
          vs.flatMap(v => resolveValues(q"$v"))

        case vs if vs.nonEmpty && vs.head.tpe <:< weakTypeOf[(_, _)] =>
          // x(31, vs)
          mapPairs(vs, t)

        case sets if t.isMany && sets.nonEmpty && sets.head.tpe <:< weakTypeOf[Set[_]] =>
          // x(32, sets)
          sets.map(set => resolveValues(q"$set", t.at).toSet)

        case vs if t.isMany && vs.nonEmpty =>
          // x(31, vs)
          vs.head match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(32, vs, sets)
              sets.map(set => resolveValues(q"$set", t.at).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(33, vs, sets)
              sets.map(set => resolveValues(q"$set", t.at).toSet)

            case _ =>
              // x(34, vs)
              vs.flatMap(v => resolveValues(q"$v", t.at))
          }
        case vs                            =>
          // x(35, vs)
          vs.flatMap(v => resolveValues(q"$v", t.at))
      }

      values match {
        case q"Seq($value)" =>
          // x(1, "single", value)
          single(value)

        case Apply(_, List(Select(_, TermName("$qmark")))) =>
          // x(2, "?")
          Qm

        case q"Seq(..$values)" =>
          // x(3, "multiple", values)
          multiple(values)

        case other if t == null =>
          // x(4, other)
          resolveValues(other)

        case other =>
          // x(5, other)
          resolveValues(other, t.at)
      }
    }

    def mapPairs(vs: Seq[Tree], t: richTree = null): Value = {
      def keyValues = vs.map {
        case q"scala.Predef.ArrowAssoc[$t1]($k).->[$t2]($v)" => (extract(q"$k"), extract(q"$v"))
        case q"scala.Tuple2.apply[$t1, $t2]($k, $v)"         => (extract(q"$k"), extract(q"$v"))
        case ident                                           => (extract(ident), "__pair__")
      }
      if (t.isMapAttr || t.isMapAttr$)
        MapEq(keyValues.map(kv => (kv._1.asInstanceOf[String], kv._2)))
      else
        ReplaceValue(keyValues)
    }

    def extract(tree: Tree) = {
      // x(40, tree)
      tree match {
        case Constant(v: String)                            => v
        case Literal(Constant(s: String))                   => s
        case Literal(Constant(i: Int))                      => i
        case Literal(Constant(l: Long))                     => l
        case Literal(Constant(f: Float))                    => f
        case Literal(Constant(d: Double))                   => d
        case Literal(Constant(b: Boolean))                  => b
        case Ident(TermName(v: String))                     => hasVariables = true; "__ident__" + v
        case Select(This(TypeName(_)), TermName(v: String)) => hasVariables = true; "__ident__" + v
        case other                                          => other
      }
    }

    def resolveValues(tree: Tree, at: att = null): Seq[Any] = {
      def resolve(tree0: Tree, values: Seq[Tree] = Seq.empty[Tree]): Seq[Tree] = tree0 match {
        case q"$a.or($b)"             => resolve(b, resolve(a, values))
        case q"${_}.string2Model($v)" => values :+ v
        case Apply(_, vs)             => values ++ vs.flatMap(resolve(_))
        case v                        => values :+ v
      }
      def validateStaticEnums(value: Any, enumValues: Seq[String]) = {
        if (value != "?" && !value.toString.startsWith("__ident__") && !enumValues.contains(value.toString))
          abort(s"'$value' is not among available enum values of attribute ${at.kwS}:\n  " +
            at.enumValues.sorted.mkString("\n  "))
        value
      }
      if (at == null || !at.isAnyEnum) {
        resolve(tree).map(extract).distinct
      } else {
        resolve(tree).map(extract).distinct.map(value => validateStaticEnums(value, at.enumValues))
      }
    }


    // Init ======================================================================================================

    val rawElements: Seq[Element] = resolve(dsl)
    // x(801, rawElements, casts, jsons, post, postCasts, postJsons)
    addJsonLambdas
    if (post) {
      // no nested, so transfer
      types = List(postTypes)
      casts = List(postCasts)
      jsons = List(postJsons)
      postTypes = Nil
      postCasts = Nil
      postJsons = Nil
    }
    // x(802, rawElements, casts, jsons, post, postCasts, postJsons)


    // Sanity checks .......................................................................

    // We need to perform checks on 3 different loops of the Model elements:
    // - loop
    // - reversed loop
    // - recursive loop
    // Mutable variables are used extensively to optimize speed by only using fast `foreach` (`with`) loops.

    def txError(s: String) = abort(s"Molecule not allowed to have any attributes after transaction or nested data structure. Found" + s)
    def dupValues(pairs: Seq[(Any, Any)]): Seq[String] = pairs.map(_._2.toString).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq
    def dupKeys(pairs: Seq[(Any, Any)]): Seq[Any] = pairs.map(_._1).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq

    val last: Int = rawElements.length
    var reversedElements: List[Element] = List.empty[Element]

    // Hieararchy depth of each sub-molecule in composites
    var compLevel = 0
    // Top-level attributes in composites have to be unique accross sub-molecules
    var compTopLevelAttrs: List[String] = List.empty[String]
    // Sub-molecules in composites can build hierarchies independently, but with no duplicate attributes within the local branch
    var compLocalAttrs: List[String] = List.empty[String]

    var i: Int = 0
    var after: Boolean = false
    var hasMandatory: Boolean = false
    rawElements.foreach { el =>
      i += 1
      // build reversed elements now that we loop the elements anyway (avoiding looping again)
      reversedElements ::= el

      el match {
        case n: Nested     => hasMandatory = true
        case e: TxMetaData => after = true
        case e if after    => e match {
          // No non-tx meta attributes after TxMetaData/Nested
          case Atom(_, "attr", _, _, _, _, _, _) => txError(s" attribute `a`")
          case Meta(_, _, "e", _, _)             => txError(s" attribute `e`")
          case Meta(_, _, "v", _, _)             => txError(s" attribute `v`")
          case Atom(_, name, _, _, _, _, _, _)   => txError(s" attribute `$name`")
          case Bond(_, name, _, _, _)            => txError(s" reference `${name.capitalize}`")
          case _                                 => txError(s": " + e)
        }

        // Molecule should at least have one mandatory attribute
        case a: Atom                       =>
          if (a.name.last != '$')
            hasMandatory = true

          //          if (isComposite && duplAtoms.contains(clean(a.name)))
          //            abort(s"Comosite molecules can't contain the same attribute more than once. Found multiple instances of `${a.name}`")
          //          else
          //            duplAtoms = duplAtoms :+ clean(a.name)

          a match {
            case a@Atom(ns, name, _, _, ReplaceValue(pairs), _, _, _) if dupValues(pairs).nonEmpty =>
              abort(s"Can't replace with duplicate values of attribute `:$ns/$name`:\n" + dupValues(pairs).mkString("\n"))

            case a@Atom(ns, name, _, _, AssertMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't assert multiple key/value pairs with the same key for attribute `:$ns/$name`:\n" + dupPairs.mkString("\n"))

            case a@Atom(ns, name, _, _, ReplaceMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't replace multiple key/value pairs with the same key for attribute `:$ns/$name`:\n" + dupPairs.mkString("\n"))

            case Atom(ns, name, _, 2, Distinct, _, _, _) =>
              abort(s"`Distinct` keyword not supported for card many attributes like `:$ns/$name` (card many values already returned as Sets of distinct values).")

            case _ =>
          }
        case b: Bond if i == last          => abort(s"Molecule not allowed to end with a reference. Please add one or more attribute to the reference.")
        case b: Bond                       => hasMandatory = true
        case Meta(_, "txInstant", _, _, _) => hasMandatory = true
        case c: Composite                  =>
          hasMandatory = true
          // Start over for each sub-molecule
          compLevel = 0
          compLocalAttrs = List.empty[String]
          c.elements.foreach {
            case _: Nested =>
              abort("Nested molecules in composites are not implemented")

            case Atom(ns, name, _, _, _, _, _, _) =>
              if (compLevel == 0) {
                if (compTopLevelAttrs.contains(ns + "/" + clean(name)))
                  abort(s"Composite molecules can't contain the same attribute more than once. Found multiple instances of `:$ns/${clean(name)}`")
                compTopLevelAttrs = compTopLevelAttrs :+ (ns + "/" + clean(name))
              } else {
                if (compLocalAttrs.contains(ns + "/" + clean(name)))
                  abort(s"Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:$ns/${clean(name)}`")
                compLocalAttrs = compLocalAttrs :+ clean(ns + "/" + clean(name))
              }

            case Bond(ns, refAttr, _, _, _) =>
              if (compLevel == 0) {
                if (compTopLevelAttrs.contains(ns + "/" + clean(refAttr)))
                  abort(s"Composite molecules can't contain the same ref more than once. Found multiple instances of `:$ns/${clean(refAttr)}`")
                compTopLevelAttrs = compTopLevelAttrs :+ (ns + "/" + clean(refAttr))
              } else {
                if (compLocalAttrs.contains(ns + "/" + clean(refAttr)))
                  abort(s"Composite sub-molecules can't contain the same ref more than once. Found multiple instances of `:$ns/${clean(refAttr)}`")
                compLocalAttrs = compLocalAttrs :+ clean(ns + "/" + clean(refAttr))
              }
              compLevel += 1

            case _: ReBond => compLevel -= 1
            case _         =>
          }
        case Meta(_, "e_", _, _, EntValue) => abort("Tacit entity only allowed if applying an entity id")
        case _                             =>
      }
    }
    if (!hasMandatory) abort(s"Molecule is empty or has only meta/optional attributes. Please add one or more attributes.")


    // Resolve generic elements ............................................................

    // Transfer generic values from Meta elements to Atoms and skip Meta elements
    var es: List[Element] = List.empty[Element]
    var gs: List[Generic] = List.empty[Generic]
    var lastBondNs: String = ""
    var lastBondAttr: String = ""
    var v: Value = NoValue
    reversedElements.foreach {
      case a@Atom(ns, attr, _, _, _, _, _, _) if ns == lastBondNs && clean(attr) == lastBondAttr =>
        abort(s"Instead of getting the ref id with `$attr` please get it via the referenced namespace: `${lastBondAttr.capitalize}.e ...`")
      case a: Atom                                                                               =>
        if (a.name != "attr" && gs.contains(NsValue) && !gs.contains(AttrVar)) {
          abort(s"`ns` needs to have a generic `a` before")
        } else if (gs.isEmpty) {
          es = a :: es
        } else if (a.name == "attr") {
          es = a.copy(gs = a.gs ++ gs, value = v) :: es
          gs = Nil
        } else {
          es = a.copy(gs = a.gs ++ gs) :: es
          gs = Nil
        }
      case m@Meta(_, _, "e", g, v1)                                                              => es = m :: es; gs = g :: gs; v = v1
      case Meta(_, _, _, g, v1)                                                                  => gs = g :: gs; v = v1
      case txmd@TxMetaData(_)                                                                    => es = txmd :: es; gs = TxValue_(None) :: gs
      case tx: TxValue                                                                           => gs = tx :: gs
      case tx: TxValue_                                                                          => gs = tx :: gs
      case com@Composite(compositeElements)                                                      => compositeElements.last match {
        case txmd: TxMetaData => {
          // Find first Atom (possibly before further TxMetaData elements
          val markedInitElements = compositeElements.init.foldRight(1, Seq.empty[Element]) {
            case (a: Atom, (1, elems))          => (0, a.copy(gs = a.gs :+ TxValue_(None)) +: elems)
            case (txmd: TxMetaData, (1, elems)) => (1, txmd +: elems)
            case (unexpected, (1, _))           => abort("Expected attribute before `Tx`. Found: " + unexpected)
            case (other, (_, elems))            => (0, other +: elems)
          }._2
          val compositeWithTx = Composite(markedInitElements :+ txmd)
          es = compositeWithTx :: es
        }
        case _                => es = com :: es
      }
      case b@Bond(ns, refAttr, refNs, _, _)                                                      =>
        lastBondNs = ns
        lastBondAttr = refAttr
        es = b :: es
      case other                                                                                 => es = other :: es
    }


    // Recursively remove duplicate values and check logic in self-joins
    def removeDuplicateValues(elements: Seq[Element]): Seq[Element] = elements.map {
      case a: Atom                      => a match {
        // Only tacit attributes allowed to have AND semantics for self-joins
        case a@Atom(_, name, _, 1, And(_), _, _, _) if name.last != '_' =>
          abort(s"Card-one attribute `$name` cannot return multiple values.\n" +
            "A tacit attribute can though have AND expressions to make a self-join.\n" +
            s"If you want this, please make the attribute tacit by appending an underscore: `${name}_`")
        case a@Atom(_, name, _, 3, And(_), _, _, _) if name.last != '_' =>
          abort(s"Map attribute `$name` is to be considered a card-one container for keyed variations of one value and " +
            """can semantically therefore not return "multiple values".""" +
            "\nA tacit map attribute can though have AND expressions to make a self-join.\n" +
            s"If you want this, please make the map attribute tacit by appending an underscore: `${name}_`")

        // Recursively remove redundant duplicate values
        case a@Atom(_, _, _, _, And(vs), _, _, _)         => a.copy(value = And(vs.distinct))
        case a@Atom(_, _, _, _, AssertValue(vs), _, _, _) => a.copy(value = AssertValue(vs.distinct)) // (only on 1st level in updates)
        case a@Atom(_, _, _, _, Eq(vs), _, _, _)          => a.copy(value = Eq(vs.distinct))
        case a@Atom(_, _, _, _, Neq(vs), _, _, _)         => a.copy(value = Neq(vs.distinct))
        case a@Atom(_, _, _, _, Fulltext(vs), _, _, _)    => a.copy(value = Fulltext(vs.distinct))
        case _                                            => a
      }
      case Nested(bond, nestedElements) => Nested(bond, removeDuplicateValues(nestedElements))
      case other                        => other
    }

    // x(803, rawElements, es, casts, types, jsons, nestedRefAttrs, postTypes, postCasts, postJsons)

    // Return checked model
    (Model(removeDuplicateValues(es)), types, casts, jsons, nestedRefAttrs, hasVariables, postTypes, postCasts, postJsons)
  }
}