package molecule.transform
import molecule.ast.model._
import molecule.boilerplate.attributes._
import molecule.macros.{Cast, Json}
import molecule.meta.index.{AEVT, AVET, EAVT, VAET}
import molecule.meta.schema.Schema
import molecule.meta.{Log, MetaNs}
import molecule.transform.exception.Dsl2ModelException
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


private[molecule] trait Dsl2Model extends Cast with Json {
  val c: blackbox.Context
  import c.universe._

  val x = DebugMacro("Dsl2Model", 901, 900)


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

    val mandatoryDatomMeta = Seq("e", "tx", "t", "txInstant", "op", "a", "v")
    val mandatoryMeta = Seq("e", "tx", "t", "txInstant", "op", "a", "v", "Self")
    val tacitMeta = Seq("e_", "ns_", "a_", "v_", "tx_", "t_", "txInstant_", "op_")
    val datomMeta = Seq("e", "e_", "tx", "t", "txInstant", "op", "tx_", "t_", "txInstant_", "op_", "a", "a_", "v", "v_")
    val keywords = Seq("$qmark", "Nil", "None", "count", "countDistinct", "min", "max", "sum", "avg", "unify", "distinct", "median", "variance", "stddev", "rand", "sample")
    def badFn(fn: TermName) = List("countDistinct", "distinct", "max", "min", "rand", "sample", "avg", "median", "stddev", "sum", "variance").contains(fn.toString())

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

    var first: Boolean = true
    var metaType: String = "datom"

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
        // x(711, prev, element)
        resolve(prev) :+ element
      } else {
        // x(710, element)
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

    def resolve(tree: Tree): Seq[Element] = {
      if (first) {
        val p = richTree(tree)
        if (p.tpe_ <:< typeOf[MetaNs]) p.tpe_ match {
          case t if t <:< typeOf[Schema] => metaType = "schema"
          case t if t <:< typeOf[EAVT]   => metaType = "eavt"
          case t if t <:< typeOf[AEVT]   => metaType = "aevt"
          case t if t <:< typeOf[AVET]   => metaType = "avet"
          case t if t <:< typeOf[VAET]   => metaType = "vaet"
          case t if t <:< typeOf[Log]    => metaType = "log"
        }
        first = false
        // x(99, p.tpe_, metaType)
      }

      tree match {
        case q"$prev.$attr" =>
          // x(100, attr)
          resolveAttr(tree, richTree(tree), prev, richTree(prev), attr.toString())

        case q"$prev.$cur.apply(..$vs)" =>
          // x(200, cur, vs)
          resolveApply(tree, richTree(q"$prev.$cur"), prev, richTree(prev), cur.toString(), q"$vs")

        case q"$prev.$cur.apply[..$tpes](..$vs)" =>
          // x(300, cur)
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
    }


    def resolveComposite(prev: Tree, p: richTree, subCompositeTree: Tree): Seq[Element] = {
      // x(601, prev, subCompositeTree)
      post = false
      isComposite = true
      collectCompositeElements = false
      val subCompositeElements = resolve(subCompositeTree)
      addJsonLambdas
      // x(602, prev, subCompositeElements)

      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts
      jsons = List.empty[Int => Tree] :: jsons

      val elements = traverseElements(prev, p, subCompositeElements)
      // x(603, elements)
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
      if (metaType == "datom" && mandatoryMeta.contains(attrStr)) {
        // x(111, attrStr, t.tpeS)
        resolveMandatoryMetaAttr(t, prev, p, attrStr)

      } else if (metaType == "schema") {
        resolveMandatorySchemaAttr(t, prev, p, attrStr)

      } else if (metaType != "datom") { // Indexes
        // x(120, metaType, attrStr)
        if (p.isFirstNS)
          abort("Non-filtered Indexes returning the whole database not allowed in Molecule.\n" +
            "  Please apply one or more arguments to the Index. For full indexes, use Datomic:\n" +
            s"  `conn.db.datoms(datomic.Database.${metaType.toUpperCase})`"
          )
        resolveMandatoryMetaAttr(t, prev, p, attrStr)

      } else if (t.isEnum) {
        // x(131, t.tpeS)
        addSpecific(castEnum(t), "String")
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        // x(132, t.tpeS)
        addCast(castMandatoryMapAttr, t)
        addJson1(jsonMandatoryMapAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        addCast(castMandatoryAttr, t)
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        // x(133, t.tpeS, jsons, postJsons, tempJsons)
        traverseElement(prev, p, Atom(t.ns, t.name, t.tpeS, t.card, VarValue, gs = bi(tree, t)))

      } else if (attrStr.head == '_') {
        // x(134, t.tpeS)
        traverseElement(prev, p, ReBond(c.typecheck(tree).tpe.typeSymbol.name.toString.replaceFirst("_[0-9]+$", ""), ""))

      } else if (t.isRef) {
        // x(135, t.tpeS, t.card, t.refCard)
        addNamespacedJsonLambdas(firstLow(attrStr) + ".")
        traverseElement(prev, p, Bond(t.refThis, firstLow(attrStr), t.refNext, t.refCard, bi(tree, t)))

      } else if (t.isRefAttr) {
        // x(136, t.tpeS)
        addCast(castMandatoryAttr, t)
        addJsonCard(jsonOneAttr, jsonManyAttr, t)
        traverseElement(prev, p, Atom(t.ns, t.name, "Long", t.card, VarValue, gs = bi(tree, t)))

      } else {
        abort("Unexpected mandatory attribute/reference: " + t)
      }
    }


    def resolveOptionalAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (metaType == "schema") {
        // x(112, metaType, attrStr)
        resolveOptionalSchemaAttr(t, prev, p, attrStr)

      } else if (t.isEnum$) {
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
      if (metaType == "datom" && tacitMeta.contains(attrStr)) {
        abort(s"Tacit `$attrStr` can only be used with an applied value i.e. `$attrStr(<value>)`")

      } else if (metaType == "schema") {
        traverseElement(prev, p, Meta("Schema", attrStr, "schema", NoValue))

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

    def resolveMandatoryMetaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      val metaNs = metaType match {
        case "schema" => "Schema"
        case "eavt"   => "EAVT"
        case "aevt"   => "AEVT"
        case "avet"   => "AVET"
        case "vaet"   => "VAET"
        case "log"    => "Log"
        case _        => p.ns2
      }
      def castMeta(tpe: String, value: Value): Seq[Element] = {
        val tpeOrAggrTpe = if (aggrType.nonEmpty) aggrType else tpe
        addSpecific(castOneAttr(tpeOrAggrTpe), tpeOrAggrTpe)
        addJson(jsonOneAttr, tpeOrAggrTpe, metaType + "." + attrStr)
        traverseElement(prev, p, Meta(metaNs, attrStr, metaType, value))
      }
      // x(113, attrStr, metaType, p.ns, p.ns2)
      attrStr match {
        case "e"    => castMeta("Long", EntValue)
        case "v"    => castMeta("Any", NoValue)
        case "a"    => castMeta("String", NoValue)
        case "Self" => traverseElement(prev, p, Self)
        case tx     =>
          if (prev.toString.endsWith("$"))
            abort(s"Optional attributes (`${p.name}`) can't be followed by meta transaction attributes (`$attrStr`).")
          tx match {
            case "t"         => castMeta("Long", NoValue)
            case "tx"        => castMeta("Long", NoValue)
            case "txInstant" => castMeta("java.util.Date", NoValue)
            case "op"        => castMeta("Boolean", NoValue)
          }
      }
    }


    def resolveMandatorySchemaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      def castMeta(tpe: String): Seq[Element] = {
        val tpeOrAggrTpe = if (aggrType.nonEmpty) aggrType else tpe
        addSpecific(castOneAttr(tpeOrAggrTpe), tpeOrAggrTpe)
        addJson(jsonOneAttr, tpeOrAggrTpe, "schema." + attrStr)
        traverseElement(prev, p, Meta("Schema", attrStr, "schema", NoValue))
      }
      // x(122, attrStr, p.ns, p.ns2)
      attrStr match {
        case "id"          => castMeta("Long")
        case "a"           => castMeta("String")
        case "part"        => castMeta("String")
        case "nsFull"      => castMeta("String")
        case "ns"          => castMeta("String")
        case "attr"        => castMeta("String")
        case "tpe"         => castMeta("String")
        case "card"        => castMeta("String")
        case "doc"         => castMeta("String")
        case "index"       => castMeta("Boolean")
        case "unique"      => castMeta("String")
        case "fulltext"    => castMeta("Boolean")
        case "isComponent" => castMeta("Boolean")
        case "noHistory"   => castMeta("Boolean")
        case "enum"        => castMeta("String")
        case "t"           => castMeta("Long")
        case "tx"          => castMeta("Long")
        case "txInstant"   => castMeta("java.util.Date")
      }
    }


    def resolveOptionalSchemaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = attrStr match {
      case "id$" | "ident$" | "part$" | "nsFull$" | "ns$" | "attr$" | "tpe$" | "card$" =>
        abort("Schema attributes that are present with all attribute definitions are not allowed to be optional.")

      case "unique$" =>
        addCast(castEnumOpt, t)
        addJsonCard(jsonOptOneEnum, jsonOptManyEnum, t)
        traverseElement(prev, p, Meta("Schema", attrStr, "schema", NoValue))

      case optionalSchemaAttr =>
        addCast(castOptionalAttr, t)
        addJsonCard(jsonOptOneAttr, jsonOptManyAttr, t)
        traverseElement(prev, p, Meta("Schema", attrStr, "schema", NoValue))
    }


    def resolveApply(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String, vs: Tree): Seq[Element] = {
      if (t.isFirstNS) {
        // x(230, attrStr, metaType)
        tree match {
          case q"$prev.$ns.apply($pkg.?)"                         => traverseElement(prev, p, Meta(ns.toString(), "e_", metaType, Eq(Seq(Qm))))
          case q"$prev.$ns.apply($eid)" if t.isBiEdge             => traverseElement(prev, p, Meta(ns.toString(), "e_", metaType, Eq(Seq(extract(eid)))))
          case q"$prev.$ns.apply(..$eids)" if metaType != "datom" => traverseElement(prev, p, Meta(ns.toString(), "args_", metaType, Eq(resolveValues(q"Seq(..$eids)"))))
          case q"$prev.$ns.apply(..$eids)"                        => traverseElement(prev, p, Meta(ns.toString(), "e_", metaType, Eq(resolveValues(q"Seq(..$eids)"))))
        }

      } else if (metaType == "datom" && datomMeta.contains(attrStr)) {
        resolveApplyMeta(prev, p, attrStr, vs)

      } else if (metaType != "datom") {
        // x(123, metaType, attrStr)
        metaType match {
          case "schema" => resolveApplySchema(t, prev, p, attrStr, vs)

          case "log" => abort("Log attributes not allowed to have values applied.\n" +
            "Log only accepts range arguments: `Log(from, until)`.")

          case "eavt" => abort("EAVT index attributes not allowed to have values applied.\n" +
            "EAVT index only accepts datom arguments: `EAVT(<e/a/v/t>)`.")

          case "aevt" => abort("AEVT index attributes not allowed to have values applied.\n" +
            "AEVT index only accepts datom arguments: `AEVT(<a/e/v/t>)`.")

          case "avet" => abort("AVET index attributes not allowed to have values applied.\n" +
            "AVET index only accepts datom arguments: `AVET(<a/v/e/t>)` or range arguments: `AVET.range(a, from, until)`.")

          case "vaet" => abort("VAET index attributes not allowed to have values applied.\n" +
            "VAET index only accepts datom arguments: `VAET(<v/a/e/t>)`.")
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

    def resolveApplySchema(t: richTree, prev: Tree, p: richTree, attrStr: String, vs: Tree) = {
      def resolve(value: Value, aggrType: String = ""): Seq[Element] = {
        def casts(mode: String, tpe: String): Seq[Element] = {
          mode match {
            case "mandatory" =>
              // x(225, "aggrType: " + aggrType)
              if (aggrType.nonEmpty) {
                // Aggregate
                addSpecific(castOneAttr(aggrType), aggrType)
                addJson(jsonOneAttr, aggrType, "schema." + attrStr)
                traverseElement(prev, p, Meta("Schema", attrStr, "schema", value))
              } else {
                // Clean/comparison
                addSpecific(castOneAttr(tpe), tpe)
                addJson(jsonOneAttr, tpe, "schema." + attrStr)
                traverseElement(prev, p, Meta("Schema", attrStr, "schema", value))
              }
            case "tacit"     =>
              if (aggrType.isEmpty) {
                traverseElement(prev, p, Meta("Schema", attrStr, "schema", value))
              } else {
                abort(s"Can only apply `count` to mandatory meta attribute. Please remove underscore from `$attrStr`")
              }
            case "optional"  =>
              if (aggrType.isEmpty) {
                addCast(castOptionalApplyAttr, t)
                addJsonCard(jsonOptOneAttr, jsonOptManyAttr, t)
                traverseElement(prev, p, Meta("Schema", attrStr, "schema", value))
              } else {
                abort(s"Can only apply `count` to mandatory meta attribute. Please remove `$$` from `$attrStr`")
              }
          }
        }
        // Sorted by usage likelihood
        attrStr match {
          case "id"          => casts("mandatory", "Long")
          case "a"           => casts("mandatory", "String")
          case "part"        => casts("mandatory", "String")
          case "nsFull"      => casts("mandatory", "String")
          case "ns"          => casts("mandatory", "String")
          case "attr"        => casts("mandatory", "String")
          case "tpe"         => casts("mandatory", "String")
          case "card"        => casts("mandatory", "String")
          case "doc"         => casts("mandatory", "String")
          case "index"       => casts("mandatory", "Boolean")
          case "unique"      => casts("mandatory", "String")
          case "fulltext"    => casts("mandatory", "Boolean")
          case "isComponent" => casts("mandatory", "Boolean")
          case "noHistory"   => casts("mandatory", "Boolean")
          case "enum"        => casts("mandatory", "String")
          case "t"           => casts("mandatory", "Long")
          case "tx"          => casts("mandatory", "Long")
          case "txInstant"   => casts("mandatory", "java.util.Date")

          case "id_"          => casts("tacit", "Long")
          case "a_"           => casts("tacit", "String")
          case "part_"        => casts("tacit", "String")
          case "nsFull_"      => casts("tacit", "String")
          case "ns_"          => casts("tacit", "String")
          case "attr_"        => casts("tacit", "String")
          case "tpe_"         => casts("tacit", "String")
          case "card_"        => casts("tacit", "String")
          case "doc_"         => casts("tacit", "String")
          case "index_"       => casts("tacit", "Boolean")
          case "unique_"      => casts("tacit", "String")
          case "fulltext_"    => casts("tacit", "Boolean")
          case "isComponent_" => casts("tacit", "Boolean")
          case "noHistory_"   => casts("tacit", "Boolean")
          case "enum_"        => casts("tacit", "String")
          case "t_"           => casts("tacit", "Long")
          case "tx_"          => casts("tacit", "Long")
          case "txInstant_"   => casts("tacit", "java.util.Date")

          case "doc$"         => casts("optional", "String")
          case "index$"       => casts("optional", "Boolean")
          case "unique$"      => casts("optional", "String")
          case "fulltext$"    => casts("optional", "Boolean")
          case "isComponent$" => casts("optional", "Boolean")
          case "noHistory$"   => casts("optional", "Boolean")
        }
      }
      val element = vs match {
        case q"scala.collection.immutable.List($pkg.count)" => resolve(Fn("count"), "Int2")
        case q"scala.collection.immutable.List($pkg.?)"     => abort("Meta input attributes not implemented.")
        case q"scala.collection.immutable.List(scala.None)" => resolve(Fn("not"))
        case q"scala.collection.immutable.List($v)"         => resolve(modelValue("apply", null, v))
        case q"scala.collection.immutable.List(..$vs)"      => resolve(modelValue("apply", null, q"Seq(..$vs)"))
        case _                                              => abort("Unexpected value applied to meta attribute: " + vs)
      }
      // x(220, p.ns, attrStr, vs, element)
      element
    }

    def resolveApplyMeta(prev: Tree, p: richTree, attrStr: String, vs: Tree) = {
      def resolve(value: Value, aggrType: String = ""): Seq[Element] = {
        def casts(mandatory: Boolean, tpe: String): Seq[Element] = {
          if (prev.toString.endsWith("$"))
            abort(s"Optional attributes (`${p.name}`) can't be followed by meta attribute (`$attrStr`).")
          if (mandatory) {
            // x(225, "aggrType: " + aggrType)
            if (aggrType.nonEmpty) {
              // Aggregate
              addSpecific(castOneAttr(aggrType), aggrType)
              addJson(jsonOneAttr, aggrType, p.ns + "." + attrStr)
              traverseElement(prev, p, Meta(p.ns, attrStr, metaType, value))
            } else {
              // Clean/comparison
              addSpecific(castOneAttr(tpe), tpe)
              addJson(jsonOneAttr, tpe, p.ns + "." + attrStr)
              traverseElement(prev, p, Meta(p.ns, attrStr, metaType, value))
            }
          } else {
            // Tacit
            if (aggrType.isEmpty) {
              traverseElement(prev, p, Meta(p.ns, attrStr, metaType, value))
            } else {
              abort(s"Can only apply `count` to mandatory meta attribute. Please remove underscore from `$attrStr`")
            }
          }
        }
        attrStr match {
          case "e"         => casts(true, "Long")
          case "a"         => casts(true, "String")
          case "v"         => casts(true, "Any")
          case "t"         => casts(true, "Long")
          case "tx"        => casts(true, "Long")
          case "txInstant" => casts(true, "java.util.Date")
          case "op"        => casts(true, "Boolean")

          case "e_"         => casts(false, "Long")
          case "a_"         => casts(false, "String")
          case "v_"         => casts(false, "Any")
          case "t_"         => casts(false, "Long")
          case "tx_"        => casts(false, "Long")
          case "txInstant_" => casts(false, "java.util.Date")
          case "op_"        => casts(false, "Boolean")
        }
      }
      val element = vs match {
        case q"scala.collection.immutable.List($pkg.count)"            => resolve(Fn("count"), "Int2")
        case q"scala.collection.immutable.List($pkg.?)"                => abort("Meta input attributes not implemented.")
        case q"scala.collection.immutable.List($pkg.$fn)" if badFn(fn) => abort(s"Meta attributes only allowed to aggregate `count`. Found: `$fn`")
        case q"scala.collection.immutable.List($v)"                    => resolve(modelValue("apply", null, v))
        case q"scala.collection.immutable.List(..$vs)"                 => resolve(modelValue("apply", null, q"Seq(..$vs)"))
        case _                                                         => abort("Unexpected value applied to meta attribute: " + vs)
      }
      // x(220, p.ns, attrStr, vs, element)
      element
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
        Seq(Nested(Bond("", "", "", 2), Meta("", "e", "datom", EntValue) +: resolve(q"$nested")))

      case q"$prev.e_.apply[..$types]($nested)" if !p.isRef =>
        // x(330, "e_")
        Seq(Nested(Bond("", "", "", 2), resolve(q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" if !q"$prev.$manyRef".isRef =>
        // x(340, manyRef, nested)
        Seq(Nested(Bond("", "", "", 2), nestedElements(q"$prev.$manyRef", manyRef.toString, q"$nested")))

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


    def resolveOp(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String, op: Tree, values0: Tree): Element = {
      val value: Value = modelValue(op.toString(), tree, values0)

      if (attrStr.head.isUpper) {
        // x(91, attrStr, value)
        if (attrStr == "AVET")
          Meta("AVET", "range", "avet", value)
        else
          Atom(t.name, t.name, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (metaType == "datom" && datomMeta.contains(attrStr)) {
        // x(92, attrStr, values0, value)
        resolveOpDatom(t, attrStr, value)

      } else if (metaType != "datom") {
        // x(93, metaType, attrStr)
        metaType match {
          case "schema" => resolveOpSchema(t, attrStr, value)
        }
      } else if (t.isMapAttr) {
        // x(94, attrStr, value)
        addCast(castMandatoryMapAttr, t)
        addJson1(jsonMandatoryMapAttr, t)
        Atom(t.ns, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isMapAttr$) {
        // x(95, attrStr, value)
        addCast(castOptionalMapApplyAttr, t)
        addJson1(jsonOptionalMapAttr, t)
        Atom(t.ns, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isAttr) {
        // x(96, attrStr, value)
        addAttrOrAggr(attrStr, t, t.tpeS, true)
        Atom(t.ns, attrStr, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else {
        abort(s"Unexpected attribute operation for `$attrStr` having value: " + value)
      }
    }

    def resolveOpSchema(t: richTree, attrStr: String, value: Value) = {
      def resolve(tpe: String): Meta = {
        addAttrOrAggr(attrStr, t, tpe)
        Meta("Schema", attrStr, "schema", value)
      }
      attrStr match {
        case "id" | "id_"                   => resolve("Long")
        case "a" | "a_"                     => resolve("String")
        case "part" | "part_"               => resolve("String")
        case "nsFull" | "nsFull_"           => resolve("String")
        case "ns" | "ns_"                   => resolve("String")
        case "attr" | "attr_"               => resolve("String")
        case "tpe" | "tpe_"                 => resolve("String")
        case "card" | "card_"               => resolve("String")
        case "doc" | "doc_"                 => resolve("String")
        case "index" | "index_"             => resolve("Boolean")
        case "unique" | "unique_"           => resolve("String")
        case "fulltext" | "fulltext_"       => resolve("Boolean")
        case "isComponent" | "isComponent_" => resolve("Boolean")
        case "noHistory" | "noHistory_"     => resolve("Boolean")
        case "enum" | "enum_"               => resolve("String")
        case "t" | "t_"                     => resolve("Long")
        case "tx" | "tx_"                   => resolve("Long")
        case "txInstant" | "txInstant_"     => resolve("java.util.Date")
      }
    }

    def resolveOpDatom(t: richTree, attrStr: String, value: Value) = {
      def resolve(tpe: String): Meta = {
        addAttrOrAggr(attrStr, t, tpe)
        Meta(t.ns, attrStr, metaType, value)
      }
      attrStr match {
        case "e" | "e_"                 => resolve("Long")
        case "a" | "a_"                 => resolve("String")
        case "v" | "v_"                 => value match {
          case Gt(v) => abort(s"Can't compare meta values being of different types. Found: $attrStr.>($v)")
          case Ge(v) => abort(s"Can't compare meta values being of different types. Found: $attrStr.>=($v)")
          case Le(v) => abort(s"Can't compare meta values being of different types. Found: $attrStr.<=($v)")
          case Lt(v) => abort(s"Can't compare meta values being of different types. Found: $attrStr.<($v)")
          case _     => resolve("Any")
        }
        case "tx" | "tx_"               => resolve("Long")
        case "t" | "t_"                 => resolve("Long")
        case "txInstant" | "txInstant_" => resolve("java.util.Date")
        case "op" | "op_"               => resolve("Boolean")
      }
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
        case q"$pre.apply($value)"                      => richTree(pre).name.capitalize
        case _ if prev.symbol.name.toString.head == '_' => prev.tpe.typeSymbol.name.toString.replaceFirst("_[0-9]+$", "")
        case q"$pre.e" if p.isAttr                      => q"$pre".symbol.name
        case _ if p.isAttr                              => p.ns
        case _ if p.isRef                               => p.refNext
        case _                                          => p.name.capitalize
      }
      // x(510, q"$prev.$manyRef", prev, manyRef, refNext, parentNs, jsons, tempJsons, post, postJsons)
      val (ns, refAttr) = (parentNs.toString, firstLow(manyRef))
      nestedRefAttrs = nestedRefAttrs :+ s"$ns.$refAttr"
      val nestedElems = nestedElements(q"$prev.$manyRef", refNext, nestedTree)
      addJsonLambdas
      // x(511, ns, nestedRefAttrs, nestedElems, jsons, tempJsons, post, postJsons)
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
          Bond(refNext, firstLow(refAttr), refNs.toString, 2, bi(manyRef, richTree(manyRef))) +: nestedElements
        } else
          abort(s"`$manyRef` has more than one ref pointing to `$nestedNs`:\n${refPairs.mkString("\n")}")
      } else {
        nestedElements
      }
    }

    def bi(tree: Tree, t: richTree): Seq[MetaValue] = if (t.isBidirectional) {
      if (t.isBiSelfRef) {
        Seq(BiSelfRef(t.refCard))

      } else if (t.isBiSelfRefAttr) {
        Seq(BiSelfRefAttr(t.card))

      } else if (t.isBiOtherRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiOtherRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiOtherRef(t.refCard, ":" + baseType.owner.name + "/" + baseType.name))

      } else if (t.isBiOtherRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiOtherRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiOtherRefAttr(t.card, ":" + baseType.owner.name + "/" + baseType.name))

      } else if (t.isBiEdgeRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiEdgeRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiEdgeRef(t.refCard, ":" + baseType.owner.name + "/" + baseType.name))

      } else if (t.isBiEdgeRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiEdgeRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiEdgeRefAttr(t.card, ":" + baseType.owner.name + "/" + baseType.name))

      } else if (t.isBiEdgePropRef) {
        Seq(BiEdgePropRef(t.refCard))

      } else if (t.isBiEdgePropAttr) {
        Seq(BiEdgePropAttr(t.card))

      } else if (t.isBiEdgePropRefAttr) {
        Seq(BiEdgePropRefAttr(t.card))

      } else if (t.isBiTargetRef) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiTargetRef_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiTargetRef(t.refCard, ":" + baseType.owner.name + "/" + baseType.name))

      } else if (t.isBiTargetRefAttr) {
        val baseType = c.typecheck(tree).tpe.baseType(weakTypeOf[BiTargetRefAttr_[_]].typeSymbol).typeArgs.head.typeSymbol
        Seq(BiTargetRefAttr(t.card, ":" + baseType.owner.name + "/" + baseType.name))

      } else {
        throw new Dsl2ModelException("Unexpected Bidirectional: " + t)
      }
    } else {
      Seq.empty[MetaValue]
    }

    def bi2(tree: Tree, t: richTree): Value = bi(tree, t).headOption.getOrElse(NoValue)

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
        case "range"       => values match {
          case vs: Seq[_] => Eq(vs)
        }
        case unexpected    => abort(s"Unknown operator '$unexpected'\nattr: ${t.name} \nvalue: $values0")
      }
    }


    def getValues(values: Tree, t: richTree = null): Any = {
      def aggr(fn: String, value: Option[Int] = None) = if (t != null && t.name.last == '_')
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

      def single(value: Tree) = value match {
        case q"$pkg.$kw" if keywords.contains(kw.toString)      => keyw(kw.toString)
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
          Seq(resolveValues(q"$set", t).toSet)

        case vs if t.isMany =>
          // x(18, vs)
          vs match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(19, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(20, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              // x(21, vs)
              resolveValues(q"$vs", t)
          }
        case other          =>
          // x(22, other, other.raw)
          resolveValues(q"Seq($other)", t)
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
          sets.map(set => resolveValues(q"$set", t).toSet)

        case vs if t.isMany && vs.nonEmpty =>
          // x(31, vs)
          vs.head match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(32, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              // x(33, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              // x(34, vs)
              vs.flatMap(v => resolveValues(q"$v", t))
          }
        case vs                            =>
          // x(35, vs)
          vs.flatMap(v => resolveValues(q"$v", t))
      }

      values match {
        case q"Seq($value)" =>
          // x(1, "single in Seq", value)
          single(value)

        case Apply(_, List(Select(_, TermName("$qmark")))) =>
          // x(2, "datom")
          Qm

        case q"Seq(..$values)" =>
          // x(3, "multiple", values)
          multiple(values)

        case other =>
          // x(4, other)
          resolveValues(other, t)
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

    def resolveValues(tree: Tree, t: richTree = null): Seq[Any] = {
      val at: att = if (t == null) null else t.at
      def resolve(tree0: Tree, values: Seq[Tree] = Seq.empty[Tree]): Seq[Tree] = tree0 match {
        case q"$a.or($b)"                                            => resolve(b, resolve(a, values))
        case q"${_}.string2Model($v)"                                => values :+ v
        case q"scala.StringContext.apply(..$tokens).s(..$variables)" => abort(
          "Can't use string interpolation for applied values. Please assign the interpolated value to a single variable and apply that instead.")
        case Apply(_, vs)                                            => values ++ vs.flatMap(resolve(_))
        case v                                                       => values :+ v
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

    val elements: Seq[Element] = resolve(dsl)
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
    // x(801, elements)


    // Model checks - mutable variables used extensively to optimize speed.

    def txError(s: String) = abort(s"Molecule not allowed to have any attributes after transaction or nested data structure. Found" + s)
    def dupValues(pairs: Seq[(Any, Any)]): Seq[String] = pairs.map(_._2.toString).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq
    def dupKeys(pairs: Seq[(Any, Any)]): Seq[Any] = pairs.map(_._1).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq

    val last: Int = elements.length

    // Hierarchy depth of each sub-molecule in composites
    var compLevel = 0
    // Top-level attributes in composites have to be unique across sub-molecules
    var compTopLevelAttrs: List[String] = List.empty[String]
    // Sub-molecules in composites can build hierarchies independently, but with no duplicate attributes within the local branch
    var compLocalAttrs: List[String] = List.empty[String]

    var i: Int = 0
    var after: Boolean = false
    var hasMandatory: Boolean = false
    var metas: Seq[String] = Nil
    var beforeFirstAttr: Boolean = true
    var isFiltered: Boolean = false
    var nsAttrs: Map[String, String] = Map.empty[String, String]

    elements.foreach { el =>
      i += 1

      el match {
        case e if after => e match {
          // No non-tx meta attributes after TxMetaData/Nested
          case Meta(_, attr, _, _)             => txError(s" meta attribute `$attr`")
          case Atom(_, attr, _, _, _, _, _, _) => txError(s" attribute `$attr`")
          case Bond(_, refAttr, _, _, _)       => txError(s" reference `${refAttr.capitalize}`")
          case _: TxMetaData                   => // ok
          case _                               => txError(s": " + e)
        }

        // Molecule should at least have one mandatory attribute
        case a: Atom =>
          isFiltered = true
          if (a.attr.last != '$')
            hasMandatory = true

          a match {
            case a@Atom(ns, attr, _, _, ReplaceValue(pairs), _, _, _) if dupValues(pairs).nonEmpty =>
              abort(s"Can't replace with duplicate values of attribute `:$ns/$attr`:\n" + dupValues(pairs).mkString("\n"))

            case a@Atom(ns, attr, _, _, AssertMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't assert multiple key/value pairs with the same key for attribute `:$ns/$attr`:\n" + dupPairs.mkString("\n"))

            case a@Atom(ns, attr, _, _, ReplaceMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't replace multiple key/value pairs with the same key for attribute `:$ns/$attr`:\n" + dupPairs.mkString("\n"))

            case Atom(ns, attr, _, 2, Distinct, _, _, _) =>
              abort(s"`Distinct` keyword not supported for card many attributes like `:$ns/$attr` (card many values already returned as Sets of distinct values).")

            case Atom(ns, attr, _, _, _, _, _, _) =>
              if (beforeFirstAttr) {
                if (metas.nonEmpty)
                  abort(s"Can't add first attribute `${a.attr}` after meta attributes (except `e` which is ok to have first). " +
                    s"Please add meta attributes ${metas.map(g => s"`$g`").mkString(", ")} after `${a.attr}`.")
                beforeFirstAttr = false
              }
              nsAttrs = nsAttrs + ((ns + "/" + clean(attr)) -> attr)
          }

        case Bond(ns, refAttr, _, _, _) =>
          if (i == last)
            abort(s"Molecule not allowed to end with a reference. Please add one or more attribute to the reference.")

          if (nsAttrs.keySet.contains(ns + "/" + refAttr))
            abort(s"Instead of getting the ref id with `${nsAttrs.apply(ns + "/" + refAttr)}` please get it via the referenced namespace: `${refAttr.capitalize}.e ...`")

          nsAttrs = Map.empty[String, String]
          hasMandatory = true
          beforeFirstAttr = false

        case m: Meta => {
          if (m.attr.last != '_') hasMandatory = true
          m.tpe match {
            case "datom" if mandatoryDatomMeta.contains(m.attr) =>
              if (beforeFirstAttr && (m.attr != "e" && m.attr != "e_"))
                metas = metas :+ m.attr
              m.value match {
                case NoValue | EntValue | Fn(_, _) =>
                case _                             => isFiltered = true
              }
            case "schema"                                       => if (m.value != EntValue) isFiltered = true
            case _                                              => isFiltered = true // indexes
          }
        }

        case _: Nested =>
          hasMandatory = true
          beforeFirstAttr = false

        case _: TxMetaData =>
          after = true

        case c: Composite =>
          hasMandatory = true
          beforeFirstAttr = false

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
        case _            =>
      }
    }

    if (!hasMandatory)
      abort(s"Molecule has only optional attributes. Please add one or more mandatory/tacit attributes.")

    // x(802, elements, beforeFirstAttr, isFiltered)

    if (beforeFirstAttr && !isFiltered)
      abort(s"Molecule with only meta attributes and no entity id(s) applied are not allowed " +
        s"since it would cause a full scan of the whole database.")

    // Return checked model
    (Model(elements), types, casts, jsons, nestedRefAttrs, hasVariables, postTypes, postCasts, postJsons)
  }
}