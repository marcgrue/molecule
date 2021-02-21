package molecule.core.transform

import molecule.core.ast.elements._
import molecule.core.dsl.attributes._
import molecule.core.generic.AEVT._
import molecule.core.generic.AVET._
import molecule.core.generic.EAVT._
import molecule.core.generic.Log._
import molecule.core.generic.Schema._
import molecule.core.generic.VAET._
import molecule.core.generic._
import molecule.core.macros.ObjBuilder
import molecule.core.ops.VerifyRawModel
import molecule.core.transform.exception.Dsl2ModelException
import scala.language.experimental.macros
import scala.reflect.macros.blackbox


private[molecule] trait Dsl2Model extends ObjBuilder {
  val c: blackbox.Context

  import c.universe._

  val x = InspectMacro("Dsl2Model", 901, 900)
  //    val x = InspectMacro("Dsl2Model", 1, 900)

  override def abort(msg: String): Nothing = throw new Dsl2ModelException(msg)

  private[molecule] final def getModel(dsl: Tree): (
    List[Tree],
      Model,
      List[List[Tree]],
      List[List[Int => Tree]],
      Obj,
      //      List[List[Int => Tree]],
      //      List[String],
      Boolean,
      Int,
      List[Tree],
      List[Int => Tree],
      //      List[Int => Tree],
      Boolean,
      Map[Int, List[Int]],
      Map[Int, List[Int]],
    ) = {

    val mandatoryGeneric = Seq("e", "tx", "t", "txInstant", "op", "a", "v", "Self")
    val tacitGeneric     = Seq("e_", "ns_", "a_", "v_", "tx_", "t_", "txInstant_", "op_")
    val datomGeneric     = Seq("e", "e_", "tx", "t", "txInstant", "op", "tx_", "t_", "txInstant_", "op_", "a", "a_", "v", "v_")
    val keywords         = Seq("$qmark", "Nil", "None", "count", "countDistinct", "min", "max", "sum", "avg", "unify", "distinct", "median", "variance", "stddev", "rand", "sample")
    def badFn(fn: TermName) = List("countDistinct", "distinct", "max", "min", "rand", "sample", "avg", "median", "stddev", "sum", "variance").contains(fn.toString())

    var txMetaDataStarted       : Boolean = false
    var txMetaDataDone          : Boolean = false
    var txMetaCompositesCount   : Int     = 0
    var objCompositesCount      : Int     = 0
    var isComposite             : Boolean = false
    var collectCompositeElements: Boolean = false

    var post     : Boolean           = true
    var postTypes: List[Tree]        = List.empty[Tree]
    var postCasts: List[Int => Tree] = List.empty[Int => Tree]

    var isOptNested          : Boolean             = false
    var optNestedLevel       : Int                 = 0
    var optNestedRefIndexes  : Map[Int, List[Int]] = Map.empty[Int, List[Int]]
    var optNestedTacitIndexes: Map[Int, List[Int]] = Map.empty[Int, List[Int]]

    var genericImports: List[Tree]              = List(q"import molecule.core.generic.Datom._")
    var types         : List[List[Tree]]        = List(List.empty[Tree])
    var casts         : List[List[Int => Tree]] = List(List.empty[Int => Tree])
    var obj           : Obj                     = Obj("", "", 0, Nil)
    var objLevel      : Int                     = 0
    var nestedRefAttrs: List[String]            = List.empty[String]

    var hasVariables: Boolean = false
    var standard    : Boolean = true
    var aggrType    : String  = ""
    var aggrFn      : String  = ""

    var first      : Boolean = true
    var genericType: String  = "datom"

    def getType(t: richTree): Tree = {
      if (t.name.last == '$')
        t.card match {
          case 1 => tq"Option[${TypeName(t.tpeS)}]"
          case 2 => tq"Option[Set[${TypeName(t.tpeS)}]]"
          case 3 => tq"Option[Map[String, ${TypeName(t.tpeS)}]]"
          case 4 => tq"Option[${TypeName(t.tpeS)}]"
        }
      else
        t.card match {
          case 1 => tq"${TypeName(t.tpeS)}"
          case 2 => tq"Set[${TypeName(t.tpeS)}]"
          case 3 => tq"Map[String, ${TypeName(t.tpeS)}]"
          case 4 => tq"${TypeName(t.tpeS)}"
        }
    }

    def addProp(t: richTree, tpe: Tree, cast: Int => Tree, aggr: Option[(String, Tree)] = None): Unit = {
      obj = addNode(obj, Prop(t.nsFull + "_" + t.name, t.name, tpe, cast, aggr), objLevel)
    }


    def addSpecific(
      t: richTree,
      cast: Int => Tree,
      optTpe: Option[Tree] = None,
      doAddProp: Boolean = true,
      optAggr: Option[(String, Tree)] = None
    ): Unit = {
      val tpe = optTpe.getOrElse(optAggr match {
        case Some((_, aggrTpe)) => aggrTpe
        case None               => getType(t)
      })
      if (post) {
        postTypes = tpe +: postTypes
        postCasts = cast +: postCasts
        if (doAddProp)
          addProp(t, tpe, cast, optAggr)
      } else {
        types = (tpe :: types.head) +: types.tail
        casts = (cast :: casts.head) +: casts.tail
        if (doAddProp)
          addProp(t, tpe, cast, optAggr)
      }
    }

    def addCast(castLambda: richTree => Int => Tree, t: richTree): Unit = {
      if (t.name.last != '_') {
        if (post) {
          postTypes = getType(t) +: postTypes
          postCasts = castLambda(t) +: postCasts
          addProp(t, getType(t), castLambda(t))
        } else {
          types = (getType(t) :: types.head) +: types.tail
          casts = (castLambda(t) :: casts.head) +: casts.tail
          addProp(t, getType(t), castLambda(t))
        }
      }
    }

    def traverseElement(prev: Tree, p: richTree, element: Element): Seq[Element] = {
      if (p.isNS && !p.isFirstNS) {
        if (txMetaDataDone && txMetaCompositesCount > 0) {
          // Start new level
          types = List.empty[Tree] :: types
          casts = List.empty[Int => Tree] :: casts
        }
        txMetaDataDone = false
        x(711, prev, p, element, types, casts)
        resolve(prev) :+ element
      } else {
        x(710, element)
        // First element
        Seq(element)
      }
    }

    def levelComposite(subCompositeElements: Seq[Element]): Unit = {
      val err = "Unexpectedly couldn't find ns in sub composite:\n  " + subCompositeElements.mkString("\n  ")
      val ns  = subCompositeElements.collectFirst {
        case Atom(ns, _, _, _, _, _, _, _) => ns
        case Bond(nsFull, _, _, _, _)      => nsFull
        case Composite(elements)           => elements.collectFirst {
          case Atom(ns, _, _, _, _, _, _, _) => ns
          case Bond(nsFull, _, _, _, _)      => nsFull
        } getOrElse abort(err)
      } getOrElse abort(err)

      val nsCls    = ns + "_"
      // Prepend namespace in obj
      val newProps = if (objCompositesCount > 0) {
        val (props, composites) = obj.props.splitAt(obj.props.length - objCompositesCount)
        val newP                = Obj(nsCls, ns, 1, props) +: composites
        //        if (compositeCount == 4)
        x(746, types, objCompositesCount, obj, props, composites, obj.copy(props = newP))
        newP
      } else {
        x(747, types, objCompositesCount, obj, obj.copy(props = List(Obj(nsCls, ns, 1, obj.props))))
        List(Obj(nsCls, ns, 1, obj.props))
      }
      obj = obj.copy(props = newProps)
    }

    def traverseElements(prev: Tree, p: richTree, elements: Seq[Element]): Seq[Element] = {
      if (isComposite) {
        x(741, prev, elements)
        val prevElements = resolve(prev)
        if (collectCompositeElements) {
          val result = prevElements :+ Composite(elements)
          val obj0   = obj
          //          levelComposite(result)
          x(745, prevElements, elements, result, collectCompositeElements, casts, types, obj0, obj)
          result
        } else {
          val result = Seq(Composite(prevElements), Composite(elements))
          val obj0   = obj
          levelComposite(result)
          x(744, prevElements, elements, result, collectCompositeElements, casts, types, obj0, obj)
          result
        }
      } else {
        if (p.isNS && !p.isFirstNS) {
          x(751, elements)
          resolve(prev) ++ elements
        } else {
          x(752, elements)
          // First elements
          elements
        }
      }
    }

    def resolve(tree: Tree): Seq[Element] = {
      if (first) {
        val p = richTree(tree)
        if (p.tpe_ <:< typeOf[GenericNs]) p.tpe_ match {
          case t if t <:< typeOf[Schema] =>
            genericType = "schema"
            genericImports = genericImports :+ q"import molecule.core.generic.Schema._"

          case t if t <:< typeOf[EAVT] =>
            genericType = "eavt"
            genericImports = genericImports :+ q"import molecule.core.generic.EAVT._"

          case t if t <:< typeOf[AEVT] =>
            genericType = "aevt"
            genericImports = genericImports :+ q"import molecule.core.generic.AEVT._"

          case t if t <:< typeOf[AVET] =>
            genericType = "avet"
            genericImports = genericImports :+ q"import molecule.core.generic.AVET._"

          case t if t <:< typeOf[VAET] =>
            genericType = "vaet"
            genericImports = genericImports :+ q"import molecule.core.generic.VAET._"

          case t if t <:< typeOf[Log] =>
            genericType = "log"
            genericImports = genericImports :+ q"import molecule.core.generic.Log._"
        }
        first = false
        x(99, p.tpe_, genericType)
      }

      tree match {
        case q"$prev.$attr" =>
          x(100, attr)
          resolveAttr(tree, richTree(tree), prev, richTree(prev), attr.toString())

        case q"$prev.$cur.apply(..$args)" =>
          x(200, cur, args)
          resolveApply(tree, richTree(q"$prev.$cur"), prev, richTree(prev), cur.toString(), q"$args")

        case q"$prev.$cur.apply[..$tpes](..$args)" =>
          x(300, cur)
          resolveTypedApply(tree, richTree(prev))

        case q"$prev.$op(..$args)" =>
          x(400, prev, op)
          resolveOperation(tree)

        case q"$prev.$manyRef.*[..$types]($nested)" =>
          x(500, manyRef)
          resolveNested(prev, richTree(prev), manyRef, nested)

        case q"$prev.$manyRef.*?[..$types]($nested)" =>
          x(501, manyRef)
          isOptNested = true
          resolveOptNested(prev, richTree(prev), manyRef, nested)

        case q"$prev.+[..$types]($subComposite)" =>
          x(600, prev, subComposite)
          resolveComposite(prev, richTree(prev), q"$subComposite")

        case other => abort(s"Unexpected DSL structure: $other\n${showRaw(other)}")
      }
    }


    def resolveComposite(prev: Tree, p: richTree, subCompositeTree: Tree): Seq[Element] = {
      x(601, prev, subCompositeTree)
      post = false
      isComposite = true
      collectCompositeElements = false
      val subCompositeElements = resolve(subCompositeTree)
      x(602, prev, subCompositeElements, types, casts, obj)

      if (txMetaDataStarted) {
        txMetaCompositesCount = if (txMetaCompositesCount == 0) 2 else txMetaCompositesCount + 1
      }

      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts

      // Make composite in obj
      levelComposite(subCompositeElements)
      objCompositesCount += 1

      x(603, prev, subCompositeElements, types, casts, txMetaCompositesCount, obj)
      val elements = traverseElements(prev, p, subCompositeElements)
      x(604, prev, subCompositeElements, types, casts, elements, txMetaCompositesCount, obj)
      collectCompositeElements = true
      elements
    }

    def resolveAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = attrStr.last match {
      case '$' =>
        x(140, attrStr)
        resolveOptionalAttr(tree, t, prev, p, attrStr)

      case '_' =>
        x(160, attrStr)
        resolveTacitAttr(tree, t, prev, p, attrStr)

      case _ =>
        x(110, attrStr)
        resolveMandatoryAttrOrRef(tree, t, prev, p, attrStr)
    }

    def resolveMandatoryAttrOrRef(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (genericType == "datom" && mandatoryGeneric.contains(attrStr)) {
        x(111, attrStr, t.tpeS)
        resolveMandatoryGenericAttr(t, prev, p, attrStr)

      } else if (genericType == "schema") {
        resolveMandatorySchemaAttr(t, prev, p, attrStr)

      } else if (genericType != "datom") { // Indexes
        x(120, genericType, attrStr)
        resolveMandatoryGenericAttr(t, prev, p, attrStr)

      } else if (t.isEnum) {
        x(131, t.tpeS)
        if (optNestedLevel > 0) {
          addSpecific(t, castOptNestedEnum(t))
        } else {
          addSpecific(t, castEnum(t))
        }
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        x(132, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryMapAttr else castMandatoryMapAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryAttr else castMandatoryAttr, t)
        x(133, t.tpeS, t.nsFull, t.name, optNestedLevel)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (attrStr.head == '_') {
        x(134, attrStr.tail)
        obj = addNode(obj, Obj("", "", 0, Nil), objLevel)
        objLevel += 1
        x(151, obj)
        traverseElement(prev, p, ReBond(attrStr.tail))

      } else if (t.isRef) {
        x(135, t.tpeS, t.card, t.refCard, t.refThis, t.refNext)
        // Prepend ref in obj
        val refName = t.name.capitalize
        val refCls  = t.nsFull + "_" + refName + "_"
        obj = addRef(obj, refCls, refName, t.card, objLevel)
        objLevel = (objLevel - 1).max(0)
        x(152, obj)
        traverseElement(prev, p, Bond(t.refThis, firstLow(attrStr), t.refNext, t.refCard, bi(tree, t)))

      } else if (t.isRefAttr) {
        x(136, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryRefAttr else castMandatoryAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, "ref", t.card, VarValue, gvs = bi(tree, t)))

      } else {
        abort("Unexpected mandatory attribute/reference: " + t)
      }
    }


    def resolveOptionalAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (genericType == "schema") {
        x(112, genericType, attrStr)
        resolveOptionalSchemaAttr(t, prev, p, attrStr)

      } else if (t.isEnum$) {
        x(141, t.tpeS, optNestedLevel)
        addCast(if (optNestedLevel > 0) castOptNestedEnumOpt else castEnumOpt, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr$) {
        x(142, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedOptionalMapAttr else castOptionalMapAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr$) {
        addCast(if (optNestedLevel > 0) castOptNestedOptionalAttr else castOptionalAttr, t)
        x(143, t.tpeS, types, postTypes)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (t.isRefAttr$) {
        x(144, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedOptionalRefAttr else castOptionalRefAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, "ref", t.card, VarValue, gvs = bi(tree, t)))

      } else {
        abort("Unexpected optional attribute: " + t)
      }
    }


    def resolveTacitAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (genericType == "datom" && tacitGeneric.contains(attrStr)) {
        abort(s"Tacit `$attrStr` can only be used with an applied value i.e. `$attrStr(<value>)`")

      } else if (genericType == "schema") {
        traverseElement(prev, p, Generic("Schema", attrStr, "schema", NoValue))

      } else if (t.isEnum) {
        if (optNestedLevel > 0)
          casts = (castOptNestedEnumOpt(t) :: casts.head) +: casts.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        if (optNestedLevel > 0)
          casts = (castOptNestedOptionalMapAttr(t) :: casts.head) +: casts.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        if (optNestedLevel > 0)
          casts = (castOptNestedOptionalAttr(t) :: casts.head) +: casts.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (t.isRefAttr) {
        if (optNestedLevel > 0)
          casts = (castOptNestedOptionalRefAttr(t) :: casts.head) +: casts.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, "ref", t.card, VarValue, gvs = bi(tree, t)))

      } else {
        abort("Unexpected tacit attribute: " + t)
      }
    }

    def resolveMandatoryGenericAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      val genericNs = genericType match {
        case "schema" => "Schema"
        case "eavt"   => "EAVT"
        case "aevt"   => "AEVT"
        case "avet"   => "AVET"
        case "vaet"   => "VAET"
        case "log"    => "Log"
        case _        => p.nsFull2
      }
      def castGeneric(tpe: String, value: Value): Seq[Element] = {
        val tpeOrAggrTpe = if (aggrType.nonEmpty) aggrType else tpe
        addSpecific(t, castOneAttr(tpeOrAggrTpe), Some(tq"${TypeName(tpeOrAggrTpe)}"))
        traverseElement(prev, p, Generic(genericNs, attrStr, genericType, value))
      }
      val elements = attrStr match {
        case "e"    => castGeneric("Long", EntValue)
        case "v"    => castGeneric("Any", NoValue)
        case "a"    => castGeneric("String", NoValue)
        case "Self" => traverseElement(prev, p, Self)
        case tx     =>
          if (prev.toString.endsWith("$"))
            abort(s"Optional attributes (`${p.name}`) can't be followed by generic transaction attributes (`$attrStr`).")
          tx match {
            case "t"         => castGeneric("Long", NoValue)
            case "tx"        => castGeneric("Long", NoValue)
            case "txInstant" => castGeneric("Date", NoValue)
            case "op"        => castGeneric("Boolean", NoValue)
          }
      }
      x(113, attrStr, genericType, p.nsFull, p.nsFull2, obj)
      elements
    }


    def resolveMandatorySchemaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      def castGeneric(tpe: String): Seq[Element] = {
        val tpeOrAggrTpe = if (aggrType.nonEmpty) aggrType else tpe
        addSpecific(t, castOneAttr(tpeOrAggrTpe), Some(tq"${TypeName(tpeOrAggrTpe)}"))
        traverseElement(prev, p, Generic("Schema", attrStr, "schema", NoValue))
      }
      x(122, attrStr, p.nsFull, p.nsFull2)
      attrStr match {
        case "id"          => castGeneric("Long")
        case "a"           => castGeneric("String")
        case "part"        => castGeneric("String")
        case "nsFull"      => castGeneric("String")
        case "ns"          => castGeneric("String")
        case "attr"        => castGeneric("String")
        case "tpe"         => castGeneric("String")
        case "card"        => castGeneric("String")
        case "doc"         => castGeneric("String")
        case "index"       => castGeneric("Boolean")
        case "unique"      => castGeneric("String")
        case "fulltext"    => castGeneric("Boolean")
        case "isComponent" => castGeneric("Boolean")
        case "noHistory"   => castGeneric("Boolean")
        case "enum"        => castGeneric("String")
        case "t"           => castGeneric("Long")
        case "tx"          => castGeneric("Long")
        case "txInstant"   => castGeneric("Date")
      }
    }


    def resolveOptionalSchemaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = attrStr match {
      case "id$" | "ident$" | "part$" | "nsFull$" | "ns$" | "attr$" | "tpe$" | "card$" =>
        abort("Schema attributes that are present with all attribute definitions are not allowed to be optional.")

      case "unique$" =>
        addCast(castEnumOpt, t)
        traverseElement(prev, p, Generic("Schema", attrStr, "schema", NoValue))

      case optionalSchemaAttr =>
        addCast(castOptionalAttr, t)
        traverseElement(prev, p, Generic("Schema", attrStr, "schema", NoValue))
    }


    def resolveApply(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String, args: Tree): Seq[Element] = {
      if (t.isFirstNS) {
        x(230, attrStr, genericType, args)
        tree match {
          case q"$prev.$nsFull.apply($pkg.?)"                            => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(Seq(Qm))))
          case q"$prev.$nsFull.apply($eid)" if t.isBiEdge                => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(Seq(extract(eid)))))
          case q"$prev.$nsFull.apply(..$eids)" if genericType != "datom" => traverseElement(prev, p, Generic(nsFull.toString(), "args_", genericType, Eq(resolveValues(q"Seq(..$eids)"))))
          case q"$prev.$nsFull.apply(..$eids)"                           => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(resolveValues(q"Seq(..$eids)"))))
        }

      } else if (genericType == "datom" && datomGeneric.contains(attrStr)) {
        x(240, attrStr, genericType)
        resolveApplyGeneric(prev, p, attrStr, args)

      } else if (genericType != "datom") {
        x(250, genericType, attrStr)
        genericType match {
          case "schema" => resolveApplySchema(t, prev, p, attrStr, args)

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
          case t1@q"$prev1.$mapAttr.apply($key)" =>
            val tpeStr = t1.tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString
            val nsFull = new nsp(t1.tpe.typeSymbol.owner).toString
            x(260, attrStr, tpeStr)
            if (attrStr.last != '_') {
              addSpecific(p, castKeyedMapAttr(tpeStr), Some(tq"${TypeName(tpeStr)}"), false)
              // Have to add node manually since nsFull is resolved in a special way
              val newProp = Prop(nsFull + "_" + attrStr, attrStr, tq"${TypeName(tpeStr)}", castKeyedMapAttr(tpeStr))
              obj = addNode(obj, newProp, objLevel)
            }
            traverseElement(prev1, p, Atom(nsFull, mapAttr.toString, tpeStr, 4, VarValue, None, Nil, Seq(extract(q"$key").toString)))
        }

      } else {
        tree match {
          case q"$prev.$ref.apply(..$values)" if t.isRef =>
            abort(s"Can't apply value to a reference (`$ref`)")
          case q"$prev.$attr.apply(..$values)"           =>
            x(270, attrStr, values)
            traverseElement(prev, p,
              resolveOp(q"$prev.$attr", richTree(q"$prev.$attr"), attr.toString(), q"apply", q"Seq(..$values)"))
        }
      }
    }

    def resolveApplySchema(t: richTree, prev: Tree, p: richTree, attrStr: String, args: Tree) = {
      def resolve(value: Value, aggrType: String = ""): Seq[Element] = {
        def casts(mode: String, tpe: String): Seq[Element] = {
          mode match {
            case "mandatory" =>
              x(251, "aggrType: " + aggrType)
              if (aggrType.nonEmpty) {
                // Aggregate
                addSpecific(t, castOneAttr(aggrType), Some(tq"${TypeName(aggrType)}"))
                traverseElement(prev, p, Generic("Schema", attrStr, "schema", value))
              } else {
                // Clean/comparison
                addSpecific(t, castOneAttr(tpe), Some(tq"${TypeName(tpe)}"))
                traverseElement(prev, p, Generic("Schema", attrStr, "schema", value))
              }
            case "tacit"     =>
              if (aggrType.isEmpty) {
                traverseElement(prev, p, Generic("Schema", attrStr, "schema", value))
              } else {
                abort(s"Can only apply `count` to mandatory generic attribute. Please remove underscore from `$attrStr`")
              }
            case "optional"  =>
              if (aggrType.isEmpty) {
                addCast(castOptionalApplyAttr, t)
                traverseElement(prev, p, Generic("Schema", attrStr, "schema", value))
              } else {
                abort(s"Can only apply `count` to mandatory generic attribute. Please remove `$$` from `$attrStr`")
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
          case "txInstant"   => casts("mandatory", "Date")

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
          case "txInstant_"   => casts("tacit", "Date")

          case "doc$"         => casts("optional", "String")
          case "index$"       => casts("optional", "Boolean")
          case "unique$"      => casts("optional", "String")
          case "fulltext$"    => casts("optional", "Boolean")
          case "isComponent$" => casts("optional", "Boolean")
          case "noHistory$"   => casts("optional", "Boolean")
        }
      }
      val element = args match {
        case q"scala.collection.immutable.List($pkg.count)" => resolve(Fn("count"), "Int2")
        case q"scala.collection.immutable.List($pkg.?)"     => abort("Generic input attributes not implemented.")
        case q"scala.collection.immutable.List(scala.None)" => resolve(Fn("not"))
        case q"scala.collection.immutable.List($v)"         => resolve(modelValue("apply", null, v))
        case q"scala.collection.immutable.List(..$vs)"      => resolve(modelValue("apply", null, q"Seq(..$vs)"))
        case _                                              => abort("Unexpected value applied to generic attribute: " + args)
      }
      x(255, p.nsFull, attrStr, args, element)
      element
    }

    def resolveApplyGeneric(prev: Tree, t: richTree, attrStr: String, args: Tree) = {
      def resolve(value: Value, aggrType: String = ""): Seq[Element] = {
        def casts(mandatory: Boolean, tpe: String, genericAttr: String = ""): Seq[Element] = {
          if (mandatory) {
            x(241, "aggrType: " + aggrType, t.nsFull, t.nsFull2, tpe, genericAttr, attrStr, obj)
            if (aggrType == "Int2") {
              // Count of generic attribute values
              addSpecific(t, castOneAttr(aggrType), Some(tq"${TypeName(aggrType)}"), false)
              if (genericAttr.nonEmpty) {
                val newProp = Prop(
                  "Datom_" + genericAttr,
                  genericAttr,
                  tq"${TypeName(tpe)}",
                  castOneAttr(aggrType),
                  optAggr = Some((genericAttr + "_count", tq"Int")))
                obj = addNode(obj, newProp, objLevel)
              }
              traverseElement(prev, t, Generic(t.nsFull2, attrStr, genericType, value))
            } else {
              // Clean/comparison
              addSpecific(t, castOneAttr(tpe), Some(tq"${TypeName(tpe)}"), false)
              if (genericAttr.nonEmpty) {
                val newProp = Prop("Datom_" + genericAttr, genericAttr, tq"${TypeName(tpe)}", castOneAttr(tpe))
                obj = addNode(obj, newProp, objLevel)
              }
              x(242, t, t.nsFull, t.name, obj)
              traverseElement(prev, t, Generic(t.nsFull, attrStr, genericType, value))
            }
          } else {
            // Tacit
            if (aggrType.isEmpty) {
              traverseElement(prev, t, Generic(t.nsFull, attrStr, genericType, value))
            } else {
              abort(s"Can only apply `count` to mandatory generic attribute. Please remove underscore from `$attrStr`")
            }
          }
        }
        attrStr match {
          case "e"         => casts(true, "Long", "e")
          case "a"         => casts(true, "String", "a")
          case "v"         => casts(true, "Any", "v")
          case "t"         => casts(true, "Long", "t")
          case "tx"        => casts(true, "Long", "tx")
          case "txInstant" => casts(true, "Date", "txInstant")
          case "op"        => casts(true, "Boolean", "op")

          case "e_"         => casts(false, "Long")
          case "a_"         => casts(false, "String")
          case "v_"         => casts(false, "Any")
          case "t_"         => casts(false, "Long")
          case "tx_"        => casts(false, "Long")
          case "txInstant_" => casts(false, "Date")
          case "op_"        => casts(false, "Boolean")
        }
      }
      val element = args match {
        case q"scala.collection.immutable.List($pkg.count)"            => resolve(Fn("count"), "Int2")
        case q"scala.collection.immutable.List($pkg.?)"                => abort("Generic input attributes not implemented.")
        case q"scala.collection.immutable.List($pkg.$fn)" if badFn(fn) => abort(s"Generic attributes only allowed to aggregate `count`. Found: `$fn`")
        case q"scala.collection.immutable.List($v)"                    => resolve(modelValue("apply", null, v))
        case q"scala.collection.immutable.List(..$vs)"                 => resolve(modelValue("apply", null, q"Seq(..$vs)"))
        case _                                                         => abort("Unexpected value applied to generic attribute: " + args)
      }
      x(245, t.nsFull, attrStr, args, element, obj)
      element
    }


    def resolveTypedApply(tree: Tree, p: richTree): Seq[Element] = tree match {
      case q"$prev.Tx.apply[..$t]($txMolecule)" =>
        txMetaDataStarted = true
        val txMetaData = TxMetaData(resolve(q"$txMolecule"))
        txMetaDataStarted = false
        txMetaDataDone = true
        x(310, "Tx", prev, txMolecule, txMetaData, types, casts, txMetaCompositesCount)
        traverseElement(prev, p, txMetaData)

      case q"$prev.e.apply[..$types]($nested)" if !p.isRef =>
        x(320, "e")
        Seq(Nested(Bond("", "", "", 2), Generic("", "e", "datom", EntValue) +: resolve(q"$nested")))

      case q"$prev.e_.apply[..$types]($nested)" if !p.isRef =>
        x(330, "e_")
        Seq(Nested(Bond("", "", "", 2), resolve(q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" if !q"$prev.$manyRef".isRef =>
        x(340, manyRef, nested)
        Seq(Nested(Bond("", "", "", 2), nestedElements(q"$prev.$manyRef", manyRef.toString, q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" =>
        x(350, manyRef, nested)
        traverseElement(prev, p, nested1(prev, p, manyRef, nested))
    }


    def resolveOperation(tree: Tree): Seq[Element] = tree match {

      // Attribute map using k/apply
      case t@q"$prev.$keyedAttr.k(..$keys).$op(..$values)" =>
        x(410, keyedAttr, richTree(q"$prev.$keyedAttr").tpeS)
        val element = resolveOp(q"$prev.$keyedAttr", richTree(q"$prev.$keyedAttr"), keyedAttr.toString(), q"$op", q"Seq(..$values)") match {
          case a: Atom => a.copy(keys = getValues(q"$keys").asInstanceOf[Seq[String]])
        }
        traverseElement(prev, richTree(prev), element)

      // Keyed attribute map operation
      case t@q"$prev.$keyedAttr.apply($key).$op(..$values)" if q"$prev.$keyedAttr($key)".isMapAttrK =>
        val tpe    = c.typecheck(q"$prev.$keyedAttr($key)").tpe
        val tpeStr = tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString
        val nsFull = new nsp(tpe.typeSymbol.owner).toString
        x(420, nsFull, keyedAttr, tpeStr, obj)
        if (keyedAttr.toString().last != '_') {
          addSpecific(richTree(q"$prev.$keyedAttr"), castKeyedMapAttr(tpeStr), Some(tq"${TypeName(tpeStr)}"), false)
          // Have to add node manually since nsFull is resolved in a special way
          val newProp = Prop(nsFull + "_" + keyedAttr, keyedAttr.toString(), tq"${TypeName(tpeStr)}", castKeyedMapAttr(tpeStr))
          obj = addNode(obj, newProp, objLevel)
          x(421, obj)
        }
        traverseElement(prev, richTree(prev),
          Atom(nsFull, keyedAttr.toString, tpeStr, 4, modelValue(op.toString(), t, q"Seq(..$values)"), None, Nil, Seq(extract(q"$key").toString))
        )

      // Attribute operations -----------------------------
      case t@q"$prev.$attr.$op(..$values)" =>
        x(430, attr)
        traverseElement(prev, richTree(prev), resolveOp(q"$prev.$attr", richTree(q"$prev.$attr"), attr.toString(), q"$op", q"Seq(..$values)"))
    }


    def resolveOp(tree: Tree, t: richTree, attrStr: String, op: Tree, values0: Tree): Element = {
      val value: Value = modelValue(op.toString(), tree, values0)

      if (attrStr.head.isUpper) {
        x(91, attrStr, value)
        if (attrStr == "AVET")
          Generic("AVET", "range", "avet", value)
        else
          Atom(t.name, t.name, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (genericType == "datom" && datomGeneric.contains(attrStr)) {
        x(92, attrStr, values0, value)
        resolveOpDatom(t, attrStr, value)

      } else if (genericType != "datom") {
        x(93, genericType, attrStr)
        genericType match {
          case "schema" => resolveOpSchema(t, attrStr, value)
          case _        => abort("Expressions on index attributes are not allowed. " +
            "Please apply expression to full index result at runtime.")
        }
      } else if (t.isMapAttr) {
        x(94, attrStr, value)
        addCast(castMandatoryMapAttr, t)
        Atom(t.nsFull, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isMapAttr$) {
        x(95, attrStr, value)
        addCast(castOptionalMapApplyAttr, t)
        Atom(t.nsFull, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isRefAttr) {
        x(96, attrStr, value)
        addAttrOrAggr(attrStr, t, t.tpeS, true)
        Atom(t.nsFull, attrStr, "ref", t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (t.isAttr) {
        x(97, attrStr, value)
        addAttrOrAggr(attrStr, t, t.tpeS, true)
        Atom(t.nsFull, attrStr, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else {
        abort(s"Unexpected attribute operation for `$attrStr` having value: " + value)
      }
    }

    def resolveOpSchema(t: richTree, attrStr: String, value: Value) = {
      def resolve(tpe: String): Generic = {
        addAttrOrAggr(attrStr, t, tpe)
        Generic("Schema", attrStr, "schema", value)
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
        case "txInstant" | "txInstant_"     => resolve("Date")
      }
    }

    def resolveOpDatom(t: richTree, attrStr: String, value: Value) = {
      def resolve(tpe: String): Generic = {
        addAttrOrAggr(attrStr, t, tpe)
        Generic(t.nsFull, attrStr, genericType, value)
      }
      attrStr match {
        case "e" | "e_"                 => resolve("Long")
        case "a" | "a_"                 => resolve("String")
        case "v" | "v_"                 => value match {
          case Gt(v) => abort(s"Can't compare generic values being of different types. Found: $attrStr.>($v)")
          case Ge(v) => abort(s"Can't compare generic values being of different types. Found: $attrStr.>=($v)")
          case Le(v) => abort(s"Can't compare generic values being of different types. Found: $attrStr.<=($v)")
          case Lt(v) => abort(s"Can't compare generic values being of different types. Found: $attrStr.<($v)")
          case _     => resolve("Any")
        }
        case "tx" | "tx_"               => resolve("Long")
        case "t" | "t_"                 => resolve("Long")
        case "txInstant" | "txInstant_" => resolve("Date")
        case "op" | "op_"               => resolve("Boolean")
      }
    }


    def resolveNested(prev: Tree, p: richTree, manyRef: TermName, nested: Tree): Seq[Element] = {
      x(521, post)
      if (isOptNested)
        abort("Optional nested structure can't be mixed with mandatory nested structure.")
      // From now on, elements are part of nested structure
      post = false
      // Add nested elements on current level
      val nestedElement = nested1(prev, p, manyRef, q"$nested")
      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts
      x(523, nestedElement, post)
      traverseElement(prev, p, nestedElement)
    }

    def resolveOptNested(prev: Tree, p: richTree, manyRef: TermName, nested: Tree): Seq[Element] = {
      x(524, post)
      // From now on, elements are part of nested structure
      post = false
      optNestedLevel += 1
      // Add nested elements on current level
      x(525, post)
      val nestedElement = nested1(prev, p, manyRef, q"$nested")
      optNestedLevel -= 1
      // Start new level
      types = List.empty[Tree] :: types
      casts = List.empty[Int => Tree] :: casts
      x(526, nestedElement, post)
      traverseElement(prev, p, nestedElement)
    }

    def nested1(prev: Tree, p: richTree, manyRef: TermName, nestedTree: Tree) = {
      val refNext           = q"$prev.$manyRef".refNext
      val parentNs          = prev match {
        case q"$pre.apply($value)" if p.isMapAttrK      => new nsp(c.typecheck(prev).tpe.typeSymbol.owner)
        case q"$pre.apply($value)" if p.isAttr          => richTree(pre).nsFull
        case q"$pre.apply($value)"                      => richTree(pre).name.capitalize
        case _ if prev.symbol.name.toString.head == '_' => prev.tpe.typeSymbol.name.toString.split("_\\d", 2).head
        case q"$pre.e" if p.isAttr                      => q"$pre".symbol.name
        case _ if p.isAttr                              => p.nsFull
        case _ if p.isRef                               => p.refNext
        case _                                          => p.name.capitalize
      }
      val opt               = if (isOptNested) "$" else ""
      val (nsFull, refAttr) = (parentNs.toString, firstLow(manyRef))
      x(550, q"$prev.$manyRef", prev, manyRef, refNext, parentNs, post, nsFull, refAttr)
      nestedRefAttrs = nestedRefAttrs :+ s"$nsFull.$refAttr"
      val nestedElems = nestedElements(q"$prev.$manyRef", refNext, nestedTree)
      x(560, prev, manyRef, nestedTree, nsFull, parentNs, nestedRefAttrs, nestedElems)
      Nested(Bond(nsFull, refAttr + opt, refNext, 2, bi(q"$prev.$manyRef", richTree(q"$prev.$manyRef"))), nestedElems)
    }

    def nestedElements(manyRef: Tree, refNext: String, nested: Tree): Seq[Element] = {
      val nestedElements = resolve(nested)
      val nestedNs       = curNs(nestedElements.head)
      if (refNext != nestedNs) {
        // Find refs in `manyRef` namespace and match the target type with the first namespace of the first nested element
        val refs             = c.typecheck(manyRef).tpe.members.filter(e => e.isMethod && e.asMethod.returnType <:< weakTypeOf[Ref[_, _]])
        val refPairs         = refs.map(r => r.name -> r.typeSignature.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.last.typeSymbol.name.toString.init)
        val refPairsFiltered = refPairs.filter(_._2 == nestedNs.capitalize)
        val nestedElements2  = if (refPairsFiltered.isEmpty) {
          nestedElements
        } else if (refPairsFiltered.size == 1) {
          val (refAttr, refNs) = refPairsFiltered.head
          val opt              = if (isOptNested) "$" else ""
          Bond(refNext, firstLow(refAttr) + opt, refNs, 2, bi(manyRef, richTree(manyRef))) +: nestedElements
        } else {
          abort(s"`$manyRef` has more than one ref pointing to `$nestedNs`:\n${refPairs.mkString("\n")}")
        }
        x(570, manyRef, refNext, nested, refs, refPairs, refPairsFiltered, nestedElements, nestedElements2)
        nestedElements2
      } else {
        nestedElements
      }
    }

    def bi(tree: Tree, t: richTree): Seq[GenericValue] = if (t.isBidirectional) {
      if (t.isBiSelfRef) {
        Seq(BiSelfRef(t.refCard))

      } else if (t.isBiSelfRefAttr) {
        Seq(BiSelfRefAttr(t.card))

      } else if (t.isBiOtherRef) {
        Seq(BiOtherRef(t.refCard, extractNsAttr(weakTypeOf[BiOtherRef_[_]], tree)))

      } else if (t.isBiOtherRefAttr) {
        Seq(BiOtherRefAttr(t.card, extractNsAttr(weakTypeOf[BiOtherRefAttr_[_]], tree)))

      } else if (t.isBiEdgeRef) {
        Seq(BiEdgeRef(t.refCard, extractNsAttr(weakTypeOf[BiEdgeRef_[_]], tree)))

      } else if (t.isBiEdgeRefAttr) {
        Seq(BiEdgeRefAttr(t.card, extractNsAttr(weakTypeOf[BiEdgeRefAttr_[_]], tree)))

      } else if (t.isBiEdgePropRef) {
        Seq(BiEdgePropRef(t.refCard))

      } else if (t.isBiEdgePropAttr) {
        Seq(BiEdgePropAttr(t.card))

      } else if (t.isBiEdgePropRefAttr) {
        Seq(BiEdgePropRefAttr(t.card))

      } else if (t.isBiTargetRef) {
        Seq(BiTargetRef(t.refCard, extractNsAttr(weakTypeOf[BiTargetRef_[_]], tree)))

      } else if (t.isBiTargetRefAttr) {
        Seq(BiTargetRefAttr(t.card, extractNsAttr(weakTypeOf[BiTargetRefAttr_[_]], tree)))

      } else {
        throw new Dsl2ModelException("Unexpected Bidirectional: " + t)
      }
    } else {
      Seq.empty[GenericValue]
    }

    def addAttrOrAggr(attr: String, t: richTree, tpeStr: String = "", apply: Boolean = false): Unit = {
      if (standard) {
        x(81, attr)
        if (t.name.last != '$') {
          addCast(castMandatoryAttr, t)
        } else {
          if (apply) addCast(castOptionalApplyAttr, t) else addCast(castOptionalAttr, t)
        }
      } else {
        x(82, attr, s"aggrType: '$aggrType'", t.card, t.tpeS, tpeStr)
        attr.last match {
          case '_' | '$' => abort("Only mandatory attributes are allowed to aggregate")
          case _         =>
            val tpe = TypeName(tpeStr)
            val propFn = attr + "_" + aggrFn
            t.card match {
              case 2 => aggrType match {
                case "int"          => addSpecific(t, castAggrInt, Some(tq"Set[$tpe]"), optAggr = Some((propFn, tq"${TypeName("Int")}")))
                case "double"       => addSpecific(t, castAggrDouble, Some(tq"Set[$tpe]"), optAggr = Some((propFn, tq"${TypeName("Double")}")))
                case "list"         => addSpecific(t, castAggrManyList(tpeStr), Some(tq"Set[$tpe]"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "listDistinct" => addSpecific(t, castAggrManyListDistinct(tpeStr), Some(tq"$tpe"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "listRand"     => addSpecific(t, castAggrManyListRand(tpeStr), Some(tq"$tpe"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "single"       => addSpecific(t, castAggrManySingle(tpeStr), optAggr = Some((propFn, tq"Set[$tpe]")))
              }
              case _ => aggrType match {
                case "int"          => addSpecific(t, castAggrInt, Some(tq"$tpe"), optAggr = Some((propFn, tq"${TypeName("Int")}")))
                case "double"       => addSpecific(t, castAggrDouble, Some(tq"$tpe"), optAggr = Some((propFn, tq"${TypeName("Double")}")))
                case "list"         => addSpecific(t, castAggrOneList(tpeStr), Some(tq"$tpe"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "listDistinct" => addSpecific(t, castAggrOneListDistinct(tpeStr), Some(tq"$tpe"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "listRand"     => addSpecific(t, castAggrOneListRand(tpeStr), Some(tq"$tpe"), optAggr = Some((propFn, tq"List[$tpe]")))
                case "singleSample" => addSpecific(t, castAggrSingleSample(tpeStr), optAggr = Some((propFn, tq"$tpe")))
                case "single"       => addSpecific(t, castAggrOneSingle(tpeStr), optAggr = Some((propFn, tq"$tpe")))
              }
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
      x(60, op, attr, values0, values, values0.raw)
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
      def aggr(fn: String, value: Option[Int] = None) = if (t != null && t.name.last == '_') {
        abort(s"Aggregated values need to be returned. Please omit underscore from attribute `:${t.nsFull}/${t.name}`")
      } else {
        aggrFn = fn + value.fold("")(_ => "s")
        Fn(fn, value)
      }

      def keyw(kw: String): Value = kw match {
        case "$qmark"                      => Qm
        case "Nil"                         => Fn("not")
        case "None"                        => Fn("not")
        case "unify" if t.name.last == '_' => Fn("unify")
        case "unify"                       => abort(s"Can only unify on tacit attributes. Please add underscore to attribute: `${t.name}_(unify)`")
        case "min"                         => standard = false; aggrType = "single"; aggr("min")
        case "max"                         => standard = false; aggrType = "single"; aggr("max")
        case "rand"                        => standard = false; aggrType = "single"; aggr("rand")
        case "sample"                      => standard = false; aggrType = "singleSample"; aggr("sample", Some(1))
        case "sum"                         => standard = false; aggrType = "single"; aggr("sum")
        case "median"                      => standard = false; aggrType = "single"; aggr("median")
        case "distinct"                    => standard = false; aggrType = "listDistinct"; Distinct
        case "count"                       => standard = false; aggrType = "int"; aggr("count")
        case "countDistinct"               => standard = false; aggrType = "int"; aggr("count-distinct")
        case "avg"                         => standard = false; aggrType = "double"; aggr("avg")
        case "variance"                    => standard = false; aggrType = "double"; aggr("variance")
        case "stddev"                      => standard = false; aggrType = "double"; aggr("stddev")
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
          x(10, v)
          v match {
            case vm if vm.tpe <:< weakTypeOf[Map[_, _]] => vm match {
              case Apply(_, pairs) => mapPairs(pairs, t)
              case ident           => mapPairs(Seq(ident), t)
            }
            case ident if t.isMapAttr || t.isMapAttr$   => mapPairs(Seq(ident), t)
            case _                                      => Eq(resolveValues(q"$v"))
          }

        case v if !(v.tpe <:< weakTypeOf[Seq[Nothing]]) && v.tpe <:< weakTypeOf[Seq[(_, _)]] =>
          x(11, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Set[Nothing]]) && v.tpe <:< weakTypeOf[Set[_]]
          && v.tpe.typeArgs.head <:< weakTypeOf[(_, _)] =>
          x(12, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Map[Nothing, Nothing]]) && v.tpe <:< weakTypeOf[Map[_, _]] =>
          x(13, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if t == null =>
          x(14, v)
          Seq(resolveValues(q"$v"))

        case v if v.tpe <:< weakTypeOf[(_, _)] =>
          x(15, v)
          mapPairs(Seq(v), t)

        case v if t.isMapAttr$ =>
          x(16, v)
          mapPairs(Seq(v), t)

        case set if t.isMany && set.tpe <:< weakTypeOf[Set[_]] =>
          x(17, set)
          Seq(resolveValues(q"$set", t).toSet)

        case vs if t.isMany =>
          x(18, vs)
          vs match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              x(19, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              x(20, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              x(21, vs)
              resolveValues(q"$vs", t)
          }
        case other          =>
          x(22, other, other.raw)
          resolveValues(q"Seq($other)", t)
      }

      def multiple(values: Seq[Tree]) = values match {
        case vs if t == null =>
          x(30, vs)
          vs.flatMap(v => resolveValues(q"$v"))

        case vs if vs.nonEmpty && vs.head.tpe <:< weakTypeOf[(_, _)] =>
          x(31, vs)
          mapPairs(vs, t)

        case sets if t.isMany && sets.nonEmpty && sets.head.tpe <:< weakTypeOf[Set[_]] =>
          x(32, sets)
          sets.map(set => resolveValues(q"$set", t).toSet)

        case vs if t.isMany && vs.nonEmpty =>
          x(31, vs)
          vs.head match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              x(32, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              x(33, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              x(34, vs)
              vs.flatMap(v => resolveValues(q"$v", t))
          }
        case vs                            =>
          x(35, vs)
          vs.flatMap(v => resolveValues(q"$v", t))
      }

      values match {
        case q"Seq($value)" =>
          x(1, "single in Seq", value)
          single(value)

        case Apply(_, List(Select(_, TermName("$qmark")))) =>
          x(2, "datom")
          Qm

        case q"Seq(..$values)" =>
          x(3, "multiple", values)
          multiple(values)

        case other =>
          x(4, other)
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
      x(40, tree)
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

        // Implicit widening conversions of variables
        case Select(Select(This(TypeName(_)), TermName(v)),
        TermName("toFloat" | "toDouble" | "toLong")) =>
          hasVariables = true
          "__ident__" + v

        case other => other
      }
    }

    def resolveValues(tree: Tree, t: richTree = null): Seq[Any] = {
      x(41, tree)
      val at: att = if (t == null) null else t.at
      def noAppliedExpression(expr: String): Nothing = abort(
        s"Can't apply expression `$expr` here. Please assign expression to a variable and apply this instead."
      )
      def resolve(tree0: Tree, values: Seq[Tree] = Seq.empty[Tree]): Seq[Tree] = {
        x(42, tree0, tree0.raw)
        tree0 match {
          case q"$a.or($b)"             => resolve(b, resolve(a, values))
          case q"${_}.string2Model($v)" => values :+ v

          case q"scala.StringContext.apply(..$tokens).s(..$variables)" => abort(
            "Can't use string interpolation for applied values. Please assign the interpolated value to a single variable and apply that instead.")

          // Preventing simple arithmetic operation
          case q"$pre.$a.+(..$b)" => noAppliedExpression(s"$a + $b")
          case q"$pre.$a.-(..$b)" => noAppliedExpression(s"$a - $b")
          case q"$pre.$a.*(..$b)" => noAppliedExpression(s"$a * $b")
          case q"$pre.$a./($b)"   => noAppliedExpression(s"$a / $b")
          case Apply(_, vs)       => values ++ vs.flatMap(resolve(_))
          case v                  => values :+ v
        }
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


    // Post-process optional nested structures

    if (isOptNested) {
      def markRefIndexes(elements: Seq[Element], level: Int): (Int, Int) = {
        elements.foldLeft(level, 0) {
          case ((_, _), Nested(Bond(_, refAttr, _, _, _), _)) if !refAttr.endsWith("$") =>
            abort("Optional nested structure can't be mixed with mandatory nested structure.")

          case ((l, _), Nested(_, es))                                 => markRefIndexes(es, l + 1)
          case ((0, _), _)                                             => (0, 0)
          case ((_, _), b@Bond(_, _, _, 2, _))                         =>
            abort(s"Flat card many ref not allowed with optional nesting. Found: $b")
          case ((l, i), _: Bond)                                       =>
            optNestedRefIndexes +=
              level -> (optNestedRefIndexes.getOrElse(l, Nil) :+ i)
            (l, i + 1)
          case ((l, i), Atom(_, _, _, _, VarValue | EnumVal, _, _, _)) => (l, i + 1)
          case ((_, _), a@Atom(_, _, _, _, value, _, _, _))            =>
            value match {
              case Qm | Eq(Seq(Qm)) | Neq(Seq(Qm)) | Lt(Qm) | Gt(Qm) | Le(Qm) | Ge(Qm) | Fulltext(Seq(Qm)) =>
                abort(s"Input not allowed in optional nested structures. Found: $a")
              case _                                                                                       =>
                abort(s"Expressions not allowed in optional nested structures. Found: $a")
            }
          case ((_, _), e)                                             =>
            abort(s"Expressions not allowed in optional nested structures. Found: $e")
        }
      }
      markRefIndexes(elements, 0)

      def markTacitIndexes(elements: Seq[Element], level: Int): (Int, Int) = {
        elements.foldLeft(level, 0) {
          case ((l, _), Nested(_, es)) => markTacitIndexes(es, l + 1)
          case ((0, _), _)             => (0, 0)

          case ((l, i), Atom(_, attr, _, _, _, _, _, _)) if attr.endsWith("_") =>
            optNestedTacitIndexes +=
              level -> (optNestedTacitIndexes.getOrElse(l, Nil) :+ i)
            (l, i + 1)

          case ((l, i), _: Atom) => (l, i + 1)
          case ((l, i), _)       => (l, i)

        }
      }
      markTacitIndexes(elements, 0)
    }

    if (post) {
      // no nested, so transfer
      types = List(postTypes)
      casts = List(postCasts)
      postTypes = Nil
      postCasts = Nil
    }
    //    x(801, elements)
    //    x(801, elements, types, casts)
    x(801, elements, types, casts, nestedRefAttrs,
      hasVariables, txMetaCompositesCount,
      postTypes, postCasts, post)

    // Return checked model
    (
      genericImports,
      Model(VerifyRawModel(elements, false)),
      types, casts, obj,
      hasVariables, txMetaCompositesCount,
      postTypes, postCasts,
      isOptNested,
      optNestedRefIndexes, optNestedTacitIndexes
    )
  }
}