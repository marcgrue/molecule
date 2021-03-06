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

    val xx = InspectMacro("Dsl2Model", 901, 900)
//  val xx = InspectMacro("Dsl2Model", 101, 800)

  override def abort(msg: String): Nothing = throw new Dsl2ModelException(msg)

  private[molecule] final def getModel(dsl: Tree): (
    List[Tree],
      Model,
      List[List[Tree]],
      List[List[Int => Tree]],
      Obj,
      Boolean,
      Int,
      List[Tree],
      List[Int => Tree],
      Boolean,
      Map[Int, List[Int]],
      Map[Int, List[Int]],
    ) = {

    lazy val mandatoryGeneric = Seq("e", "tx", "t", "txInstant", "op", "a", "v", "Self")
    lazy val tacitGeneric     = Seq("e_", "ns_", "a_", "v_", "tx_", "t_", "txInstant_", "op_")
    lazy val datomGeneric     = Seq("e", "e_", "tx", "t", "txInstant", "op", "tx_", "t_", "txInstant_", "op_", "a", "a_", "v", "v_")
    lazy val keywords         = Seq("$qmark", "Nil", "None", "count", "countDistinct", "min", "max", "sum", "avg", "unify", "distinct", "median", "variance", "stddev", "rand", "sample")
    lazy val numberTypes      = Seq("Int", "Long", "Float", "Double", "BigInt", "BigDecimal")
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
    var typess        : List[List[Tree]]        = List(List.empty[Tree])
    var castss        : List[List[Int => Tree]] = List(List.empty[Int => Tree])
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
      val aggrTpe = aggr.map(_._2.toString)
      obj = addNode(obj, Prop(t.nsFull + "_" + t.name.replace('$', '_'), t.name, tpe, cast, aggrTpe), objLevel)
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
        typess = (tpe :: typess.head) +: typess.tail
        castss = (cast :: castss.head) +: castss.tail
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
          typess = (getType(t) :: typess.head) +: typess.tail
          castss = (castLambda(t) :: castss.head) +: castss.tail
          addProp(t, getType(t), castLambda(t))
        }
      }
    }

    def traverseElement(prev: Tree, p: richTree, element: Element): Seq[Element] = {
      if (p.isNS && !p.isFirstNS) {
        //xx(711, prev, p, element, typess, castss)
        resolve(prev) :+ element
      } else {
        //xx(710, element)
        // First element
        Seq(element)
      }
    }

    def traverseElements(prev: Tree, p: richTree, elements: Seq[Element], sameNs: Boolean = false): Seq[Element] = {
      if (isComposite) {
        //xx(640, prev, elements, sameNs, obj)
        val prevElements = resolve(prev)
        if (collectCompositeElements) {
          val result = prevElements :+ Composite(elements)
          //xx(641, prevElements, elements, result, collectCompositeElements, castss, typess, obj, sameNs)
          result
        } else {
          val result = Seq(Composite(prevElements), Composite(elements))
          levelCompositeObj(result, sameNs)
          //xx(642, prevElements, elements, result, collectCompositeElements, castss, typess, obj, sameNs)
          result
        }
      } else {
        if (p.isNS && !p.isFirstNS) {
          //xx(751, elements)
          resolve(prev) ++ elements
        } else {
          //xx(752, elements)
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
        //xx(99, p.tpe_, genericType)
      }

      tree match {
        case q"$prev.$attr" =>
          //xx(100, attr)
          resolveAttr(tree, richTree(tree), prev, richTree(prev), attr.toString())

        case q"$prev.$cur.apply(..$args)" =>
          //xx(200, cur, args)
          resolveApply(tree, richTree(q"$prev.$cur"), prev, richTree(prev), cur.toString(), q"$args")

        case q"$prev.$cur.apply[..$tpes](..$args)" =>
          //xx(300, cur)
          resolveTypedApply(tree, richTree(prev))

        case q"$prev.$op(..$args)" =>
          //xx(400, prev, op)
          resolveOperation(tree)

        case q"$prev.$manyRef.*[..$types]($nested)" =>
          //xx(500, manyRef)
          resolveNested(prev, richTree(prev), manyRef, nested)

        case q"$prev.$manyRef.*?[..$types]($nested)" =>
          //xx(501, manyRef)
          isOptNested = true
          resolveOptNested(prev, richTree(prev), manyRef, nested)

        case q"$prev.+[..$types]($subComposite)" =>
          //xx(600, prev, subComposite, obj)
          resolveComposite(prev, richTree(prev), q"$subComposite")

        case q"$prev.++[..$types]($subComposite)" =>
          //xx(610, prev, subComposite, obj)
          val res = resolveComposite(prev, richTree(prev), q"$subComposite", true)
          res

        case other => abort(s"Unexpected DSL structure: $other\n${showRaw(other)}")
      }
    }

    def levelCompositeObj(subCompositeElements: Seq[Element], sameNs: Boolean = false): Unit = {
      //xx(630, subCompositeElements, sameNs, objCompositesCount)
      val err   = "Unexpectedly couldn't find ns in sub composite:\n  " + subCompositeElements.mkString("\n  ")
      val ns    = subCompositeElements.collectFirst {
        case Atom(ns, _, _, _, _, _, _, _) => ns
        case Bond(nsFull, _, _, _, _)      => nsFull
        case Composite(elements)           => elements.collectFirst {
          case Atom(ns, _, _, _, _, _, _, _) => ns
          case Bond(nsFull, _, _, _, _)      => nsFull
        } getOrElse abort(err)
      } getOrElse abort(err)
      val nsCls = ns + "_"

      // Prepend namespace in obj
      val newProps: List[Node] = if (objCompositesCount > 0) {
        val (props, composites) = obj.props.splitAt(obj.props.length - objCompositesCount)
        val newP: List[Node]    = composites.head match {
          case Obj("Tx_", _, _, _) =>
            // Reset obj composites count
            objCompositesCount = 0
            List(Obj(nsCls, ns, 1, props ++ composites))

          case compositeObj@Obj(compositeCls, _, _, _) if sameNs && nsCls == compositeCls =>
            compositeObj.copy(props = props ++ compositeObj.props) +: composites.tail

          case _ => Obj(nsCls, ns, 1, props) +: composites
        }
        //xx(631, props, composites, newP)
        newP
      } else {
        //xx(632, typess, objCompositesCount, obj, obj.copy(props = List(Obj(nsCls, ns, 1, obj.props))), sameNs)
        List(Obj(nsCls, ns, 1, obj.props))
      }
      obj = obj.copy(props = newProps)
      //xx(633, obj, ns)
    }

    def resolveComposite(prev: Tree, p: richTree, subCompositeTree: Tree, sameNs: Boolean = false): Seq[Element] = {
      //xx(620, prev, subCompositeTree, obj, sameNs)
      post = false
      isComposite = true
      collectCompositeElements = false
      val subCompositeElements = resolve(subCompositeTree)
      // Make sure we continue to collect composite elements
      collectCompositeElements = false
      //xx(621, prev, subCompositeElements, typess, castss, obj, sameNs)

      if (txMetaDataStarted) {
        txMetaCompositesCount = if (txMetaCompositesCount == 0) 2 else txMetaCompositesCount + 1
      }

      // Start new level
      typess = List.empty[Tree] :: typess
      castss = List.empty[Int => Tree] :: castss

      // Make composite in obj
      levelCompositeObj(subCompositeElements, sameNs)
      objCompositesCount += 1

      //xx(622, prev, subCompositeElements, typess, castss, txMetaCompositesCount, obj, sameNs, objCompositesCount)
      val elements = traverseElements(prev, p, subCompositeElements, sameNs)
      collectCompositeElements = true
      //xx(623, prev, subCompositeElements, typess, castss, elements, txMetaCompositesCount, obj, sameNs)
      elements
    }

    def resolveAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = attrStr.last match {
      case '$' =>
        //xx(140, attrStr)
        resolveOptionalAttr(tree, t, prev, p, attrStr)

      case '_' =>
        //xx(160, attrStr)
        resolveTacitAttr(tree, t, prev, p, attrStr)

      case _ =>
        //xx(110, attrStr)
        resolveMandatoryAttrOrRef(tree, t, prev, p, attrStr)
    }

    def resolveMandatoryAttrOrRef(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (genericType == "datom" && mandatoryGeneric.contains(attrStr)) {
        //xx(111, attrStr, t.tpeS)
        resolveMandatoryGenericAttr(t, prev, p, attrStr)

      } else if (genericType == "schema") {
        resolveMandatorySchemaAttr(t, prev, p, attrStr)

      } else if (genericType != "datom") { // Indexes
        //xx(120, genericType, attrStr)
        resolveMandatoryGenericAttr(t, prev, p, attrStr)

      } else if (t.isEnum) {
        //xx(131, t.tpeS)
        if (optNestedLevel > 0) {
          addSpecific(t, castOptNestedEnum(t))
        } else {
          addSpecific(t, castEnum(t))
        }
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        //xx(132, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryMapAttr else castMandatoryMapAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryAttr else castMandatoryAttr, t)
        //xx(133, t.tpeS, t.nsFull, t.name, optNestedLevel)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (attrStr.head == '_') {
        //xx(134, attrStr.tail)
        obj = addNode(obj, Obj("", "", 0, Nil), objLevel)
        objLevel += 1
        //xx(151, obj)
        traverseElement(prev, p, ReBond(attrStr.tail))

      } else if (t.isRef) {
        //xx(135, t.tpeS, t.card, t.refCard, t.refThis, t.refNext, obj)
        // Prepend ref in obj
        val refName = t.name.capitalize
        val refCls  = t.nsFull + "__" + refName
        if (objCompositesCount > 0) {
          val (props, composites) = obj.props.splitAt(obj.props.length - objCompositesCount)
          val newProps            = Obj(refCls, refName, 1, props) +: composites
          obj = obj.copy(props = newProps)
          //xx(152, props, composites, newProps, obj)

        } else if(txMetaDataDone) {
          val props = Obj(refCls, refName, 1, obj.props.init)
          val txMetaObj = obj.props.last
          obj = obj.copy(props = List(props, txMetaObj))
          objLevel = 0
          //xx(153, objLevel, isComposite, refCls, obj)

        } else {
          obj = addRef(obj, refCls, refName, t.card, objLevel)
          //xx(154, objLevel, isComposite, refCls, obj)
          objLevel = (objLevel - 1).max(0)
        }
        //xx(155, objLevel, isComposite, objCompositesCount, refCls, obj)
        traverseElement(prev, p, Bond(t.refThis, firstLow(attrStr), t.refNext, t.refCard, bi(tree, t)))

      } else if (t.isRefAttr) {
        //xx(136, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedMandatoryRefAttr else castMandatoryAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, "ref", t.card, VarValue, gvs = bi(tree, t)))

      } else {
        abort("Unexpected mandatory attribute/reference: " + t)
      }
    }


    def resolveOptionalAttr(tree: Tree, t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      if (genericType == "schema") {
        //xx(112, genericType, attrStr)
        resolveOptionalSchemaAttr(t, prev, p, attrStr)

      } else if (t.isEnum$) {
        //xx(141, t.tpeS, optNestedLevel)
        addCast(if (optNestedLevel > 0) castOptNestedEnumOpt else castEnumOpt, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr$) {
        //xx(142, t.tpeS)
        addCast(if (optNestedLevel > 0) castOptNestedOptionalMapAttr else castOptionalMapAttr, t)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr$) {
        addCast(if (optNestedLevel > 0) castOptNestedOptionalAttr else castOptionalAttr, t)
        //xx(143, t.tpeS, typess, postTypes)
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (t.isRefAttr$) {
        //xx(144, t.tpeS)
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
          castss = (castOptNestedEnumOpt(t) :: castss.head) +: castss.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, EnumVal, Some(t.enumPrefix), bi(tree, t)))

      } else if (t.isMapAttr) {
        if (optNestedLevel > 0)
          castss = (castOptNestedOptionalMapAttr(t) :: castss.head) +: castss.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, 3, VarValue, None, bi(tree, t)))

      } else if (t.isValueAttr) {
        if (optNestedLevel > 0)
          castss = (castOptNestedOptionalAttr(t) :: castss.head) +: castss.tail
        traverseElement(prev, p, Atom(t.nsFull, t.name, t.tpeS, t.card, VarValue, gvs = bi(tree, t)))

      } else if (t.isRefAttr) {
        if (optNestedLevel > 0)
          castss = (castOptNestedOptionalRefAttr(t) :: castss.head) +: castss.tail
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
        case "Self" =>
          obj = addRef(obj, t.nsFull + "_", t.nsFull, 1, objLevel)
          objLevel = (objLevel - 1).max(0)
          traverseElement(prev, p, Self)
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
      //xx(113, attrStr, genericType, p.nsFull, p.nsFull2, obj)
      elements
    }


    def resolveMandatorySchemaAttr(t: richTree, prev: Tree, p: richTree, attrStr: String): Seq[Element] = {
      def castGeneric(tpe: String): Seq[Element] = {
        val tpeOrAggrTpe = if (aggrType.nonEmpty) aggrType else tpe
        addSpecific(t, castOneAttr(tpeOrAggrTpe), Some(tq"${TypeName(tpeOrAggrTpe)}"))
        traverseElement(prev, p, Generic("Schema", attrStr, "schema", NoValue))
      }
      //xx(122, attrStr, p.nsFull, p.nsFull2)
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
      case "id$" | "part$" | "nsFull$" | "ns$" | "attr$" | "tpe$" | "card$" =>
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
        //xx(230, attrStr, genericType, args)
        tree match {
          case q"$prev.$nsFull.apply($pkg.?)"                            => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(Seq(Qm))))
          case q"$prev.$nsFull.apply($eid)" if t.isBiEdge                => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(Seq(extract(eid)))))
          case q"$prev.$nsFull.apply(..$eids)" if genericType != "datom" => traverseElement(prev, p, Generic(nsFull.toString(), "args_", genericType, Eq(resolveValues(q"Seq(..$eids)"))))
          case q"$prev.$nsFull.apply(..$eids)"                           => traverseElement(prev, p, Generic(nsFull.toString(), "e_", genericType, Eq(resolveValues(q"Seq(..$eids)"))))
        }

      } else if (genericType == "datom" && datomGeneric.contains(attrStr)) {
        //xx(240, attrStr, genericType)
        resolveApplyGeneric(prev, p, attrStr, args)

      } else if (genericType != "datom") {
        //xx(250, genericType, attrStr)
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
            val tpeStr = truncTpe(t1.tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString)
            val nsFull = new nsp(t1.tpe.typeSymbol.owner).toString
            //xx(260, attrStr, tpeStr)
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
            //xx(270, attrStr, values)
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
              //xx(251, "aggrType: " + aggrType)
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
      //xx(255, p.nsFull, attrStr, args, element)
      element
    }

    def resolveApplyGeneric(prev: Tree, t: richTree, attrStr: String, args: Tree) = {
      def resolve(value: Value, aggrType: String = ""): Seq[Element] = {
        def casts(mandatory: Boolean, tpe: String, genericAttr: String = ""): Seq[Element] = {
          if (mandatory) {
            //xx(241, "aggrType: " + aggrType, t.nsFull, t.nsFull2, tpe, genericAttr, attrStr, obj)
            if (aggrType == "Int2") {
              // Count of generic attribute values
              addSpecific(t, castOneAttr(aggrType), Some(tq"${TypeName(aggrType)}"), false)
              if (genericAttr.nonEmpty) {
                val newProp = Prop(
                  "Datom_" + genericAttr,
                  genericAttr,
                  tq"${TypeName(tpe)}",
                  castOneAttr(aggrType),
                  optAggrTpe = Some("Int"))
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
              //xx(242, t, t.nsFull, t.name, obj)
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
      //xx(245, t.nsFull, attrStr, args, element, obj)
      element
    }


    def resolveTypedApply(tree: Tree, p: richTree): Seq[Element] = tree match {
      case q"$prev.Tx.apply[..$t]($txMolecule)" =>
        txMetaDataStarted = true

        val txMetaProps = resolve(q"$txMolecule")
        val txMetaData  = TxMetaData(txMetaProps)

        val err = "Unexpectedly couldn't find ns in sub composite:\n  " + txMetaProps.mkString("\n  ")
        val ns  = txMetaProps.collectFirst {
          case Atom(ns, _, _, _, _, _, _, _) => ns
          case Bond(nsFull, _, _, _, _)      => nsFull
          case Composite(elements)           => elements.collectFirst {
            case Atom(ns, _, _, _, _, _, _, _) => ns
            case Bond(nsFull, _, _, _, _)      => nsFull
          } getOrElse abort(err)
        } getOrElse abort(err)
        if (txMetaCompositesCount == 0) {
          // Start non-composite tx meta data with namespace
          // (composite data is already namespaced)
          obj = addRef(obj, ns + "_", ns, 1, objLevel)
          objLevel = (objLevel - 1).max(0)
        }
        // Treat tx meta data as referenced data
        obj = addRef(obj, "Tx_", "Tx", 1, objLevel)
        objLevel = (objLevel - 1).max(0)
        txMetaDataStarted = false
        if (txMetaCompositesCount > 0) {
          // Start new level
          typess = List.empty[Tree] :: typess
          castss = List.empty[Int => Tree] :: castss
        }
        txMetaDataDone = true
        //xx(310, "Tx", prev, txMolecule, txMetaData, typess, castss, txMetaCompositesCount, objCompositesCount, ns, obj)
        traverseElement(prev, p, txMetaData)

      case q"$prev.e.apply[..$types]($nested)" if !p.isRef =>
        //xx(320, "e")
        Seq(Nested(Bond("", "", "", 2), Generic("", "e", "datom", EntValue) +: resolve(q"$nested")))

      case q"$prev.e_.apply[..$types]($nested)" if !p.isRef =>
        //xx(330, "e_")
        Seq(Nested(Bond("", "", "", 2), resolve(q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" if !q"$prev.$manyRef".isRef =>
        //xx(340, manyRef, nested)
        Seq(Nested(Bond("", "", "", 2), nestedElements(q"$prev.$manyRef", manyRef.toString, q"$nested")))

      case q"$prev.$manyRef.apply[..$types]($nested)" =>
        //xx(350, manyRef, nested)
        traverseElement(prev, p, nested1(prev, p, manyRef, nested))
    }


    def resolveOperation(tree: Tree): Seq[Element] = tree match {
      // Attribute map using k/apply
      case t@q"$prev.$keyedAttr.k(..$keys).$op(..$values)" =>
        //xx(410, keyedAttr, richTree(q"$prev.$keyedAttr").tpeS)
        val element = resolveOp(q"$prev.$keyedAttr", richTree(q"$prev.$keyedAttr"), keyedAttr.toString(), q"$op", q"Seq(..$values)") match {
          case a: Atom => a.copy(keys = getValues(q"$keys").asInstanceOf[Seq[String]])
        }
        traverseElement(prev, richTree(prev), element)

      // Keyed attribute map operation
      case t@q"$prev.$keyedAttr.apply($key).$op(..$values)" if q"$prev.$keyedAttr($key)".isMapAttrK =>
        val tpe    = c.typecheck(q"$prev.$keyedAttr($key)").tpe
        val tpeStr = truncTpe(tpe.baseType(weakTypeOf[One[_, _, _]].typeSymbol).typeArgs.last.toString)
        val nsFull = new nsp(tpe.typeSymbol.owner).toString
        //xx(420, nsFull, keyedAttr, tpeStr, obj)
        if (keyedAttr.toString().last != '_') {
          addSpecific(richTree(q"$prev.$keyedAttr"), castKeyedMapAttr(tpeStr), Some(tq"${TypeName(tpeStr)}"), false)
          // Have to add node manually since nsFull is resolved in a special way
          val newProp = Prop(nsFull + "_" + keyedAttr, keyedAttr.toString(), tq"${TypeName(tpeStr)}", castKeyedMapAttr(tpeStr))
          obj = addNode(obj, newProp, objLevel)
          //xx(421, obj)
        }
        traverseElement(prev, richTree(prev),
          Atom(nsFull, keyedAttr.toString, tpeStr, 4, modelValue(op.toString(), t, q"Seq(..$values)"), None, Nil, Seq(extract(q"$key").toString))
        )

      // Attribute operations -----------------------------
      case t@q"$prev.$attr.$op(..$values)" =>
        //xx(430, attr)
        traverseElement(prev, richTree(prev), resolveOp(q"$prev.$attr", richTree(q"$prev.$attr"), attr.toString(), q"$op", q"Seq(..$values)"))
    }


    def resolveOp(tree: Tree, t: richTree, attrStr: String, op: Tree, values0: Tree): Element = {
      val value: Value = modelValue(op.toString(), tree, values0)

      if (attrStr.head.isUpper) {
        //xx(91, attrStr, value)
        if (attrStr == "AVET")
          Generic("AVET", "range", "avet", value)
        else
          Atom(t.name, t.name, t.tpeS, t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (genericType == "datom" && datomGeneric.contains(attrStr)) {
        //xx(92, attrStr, values0, value)
        resolveOpDatom(t, attrStr, value)

      } else if (genericType != "datom") {
        //xx(93, genericType, attrStr)
        genericType match {
          case "schema" => resolveOpSchema(t, attrStr, value)
          case _        => abort("Expressions on index attributes are not allowed. " +
            "Please apply expression to full index result at runtime.")
        }
      } else if (t.isMapAttr) {
        //xx(94, attrStr, value)
        addCast(castMandatoryMapAttr, t)
        Atom(t.nsFull, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isMapAttr$) {
        //xx(95, attrStr, value)
        addCast(castOptionalMapApplyAttr, t)
        Atom(t.nsFull, attrStr, t.tpeS, 3, value, None, bi(tree, t))

      } else if (t.isRefAttr) {
        //xx(96, attrStr, value)
        addAttrOrAggr(attrStr, t, t.tpeS, true)
        Atom(t.nsFull, attrStr, "ref", t.card, value, t.enumPrefixOpt, bi(tree, t))

      } else if (t.isAttr) {
        //xx(97, attrStr, value)
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
      //xx(521, post, prev, manyRef, nested, obj)
      if (isOptNested)
        abort("Optional nested structure can't be mixed with mandatory nested structure.")
      // From now on, elements are part of nested structure
      post = false
      // Add nested elements on current level
      val nestedElement = nested1(prev, p, manyRef, q"$nested")
      // Start new level
      typess = List.empty[Tree] :: typess
      castss = List.empty[Int => Tree] :: castss
      //xx(523, nestedElement, post, prev, manyRef, nested, obj)
      traverseElement(prev, p, nestedElement)
    }

    def resolveOptNested(prev: Tree, p: richTree, manyRef: TermName, nested: Tree): Seq[Element] = {
      //xx(524, post)
      // From now on, elements are part of nested structure
      post = false
      optNestedLevel += 1
      // Add nested elements on current level
      //xx(525, post)
      val nestedElement = nested1(prev, p, manyRef, q"$nested")
      optNestedLevel -= 1
      // Start new level
      typess = List.empty[Tree] :: typess
      castss = List.empty[Int => Tree] :: castss
      //xx(526, nestedElement, post)
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
      //xx(550, q"$prev.$manyRef", prev, manyRef, refNext, parentNs, post, nsFull, refAttr, obj)
      nestedRefAttrs = nestedRefAttrs :+ s"$nsFull.$refAttr"
      // park post props
      val postProps = obj.props
      obj = Obj("", "", 0, Nil)
      val nestedElems = nestedElements(q"$prev.$manyRef", refNext, nestedTree)
      val nestedObj   = Obj(nsFull + "__" + manyRef, manyRef.toString, 2, obj.props)
      obj = obj.copy(props = nestedObj +: postProps)
      //xx(560, prev, manyRef, nestedTree, nsFull, parentNs, nestedRefAttrs, nestedElems, postProps, obj)
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
        //xx(571, manyRef, refNext, nested, nestedNs, nestedElements, refs, refPairs, refPairsFiltered, nestedElements2, obj)
        nestedElements2
      } else {
        //xx(572, manyRef, refNext, nested, nestedNs, nestedElements, obj)
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
        //xx(81, attr)
        if (t.name.last != '$') {
          addCast(castMandatoryAttr, t)
        } else {
          if (apply) addCast(castOptionalApplyAttr, t) else addCast(castOptionalAttr, t)
        }
      } else {
        //xx(82, attr, s"aggrType: '$aggrType'", t.card, t.tpeS, tpeStr)
        attr.last match {
          case '_' | '$' => abort("Only mandatory attributes are allowed to aggregate")
          case _         =>
            val tpe    = TypeName(tpeStr)
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
      //xx(60, op, attr, values0, values, values0.raw)
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
      def aggr(fn: String, aggrTpe: String, value: Option[Int] = None, checkNum: Boolean = false) = if (t != null && t.name.last == '_') {
        abort(s"Aggregated values need to be returned. Please omit underscore from attribute `:${t.nsFull}/${t.name}`")
      } else {
        if (checkNum && !numberTypes.contains(t.tpeS))
          abort(s"Can't apply `$fn` aggregate to non-number attribute `${t.name}` of type `${t.tpeS}`.")
        standard = false
        aggrType = aggrTpe
        aggrFn = fn + value.fold("")(_ => "s")
        Fn(fn, value)
      }

      def keyw(kw: String): Value = kw match {
        case "$qmark"                      => Qm
        case "Nil"                         => Fn("not")
        case "None"                        => Fn("not")
        case "unify" if t.name.last == '_' => Fn("unify")
        case "unify"                       => abort(s"Can only unify on tacit attributes. Please add underscore to attribute: `${t.name}_(unify)`")
        case "min"                         => aggr("min", "single")
        case "max"                         => aggr("max", "single")
        case "rand"                        => aggr("rand", "single")
        case "sample"                      => aggr("sample", "singleSample", Some(1))
        case "sum"                         => aggr("sum", "single", checkNum = true)
        case "median"                      => aggr("median", "single")
        case "count"                       => aggr("count", "int")
        case "countDistinct"               => aggr("count-distinct", "int")
        case "avg"                         => aggr("avg", "double")
        case "variance"                    => aggr("variance", "double")
        case "stddev"                      => aggr("stddev", "double")
        case "distinct"                    => standard = false; aggrType = "listDistinct"; Distinct
      }

      def single(value: Tree) = value match {
        case q"$pkg.$kw" if keywords.contains(kw.toString)      => keyw(kw.toString)
        case q"$pkg.min.apply(${Literal(Constant(i: Int))})"    => aggr("min", "list", Some(i))
        case q"$pkg.max.apply(${Literal(Constant(i: Int))})"    => aggr("max", "list", Some(i))
        case q"$pkg.rand.apply(${Literal(Constant(i: Int))})"   => aggr("rand", "listRand", Some(i))
        case q"$pkg.sample.apply(${Literal(Constant(i: Int))})" => aggr("sample", "list", Some(i))
        case q"$a.and[$tpe]($b).and[$u]($c)"                    => And(resolveValues(q"Seq($a, $b, $c)"))
        case q"$a.and[$tpe]($b)"                                => And(resolveValues(q"Seq($a, $b)"))

        case q"scala.Some.apply[$tpe]($v)" =>
          //xx(10, v)
          v match {
            case vm if vm.tpe <:< weakTypeOf[Map[_, _]] => vm match {
              case Apply(_, pairs) => mapPairs(pairs, t)
              case ident           => mapPairs(Seq(ident), t)
            }
            case ident if t.isMapAttr || t.isMapAttr$   => mapPairs(Seq(ident), t)
            case _                                      => Eq(resolveValues(q"$v"))
          }

        case v if !(v.tpe <:< weakTypeOf[Seq[Nothing]]) && v.tpe <:< weakTypeOf[Seq[(_, _)]] =>
          //xx(11, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Set[Nothing]]) && v.tpe <:< weakTypeOf[Set[_]]
          && v.tpe.typeArgs.head <:< weakTypeOf[(_, _)] =>
          //xx(12, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if !(v.tpe <:< weakTypeOf[Map[Nothing, Nothing]]) && v.tpe <:< weakTypeOf[Map[_, _]] =>
          //xx(13, v)
          v match {
            case Apply(_, pairs) => mapPairs(pairs, t)
            case ident           => mapPairs(Seq(ident), t)
          }

        case v if t == null =>
          //xx(14, v)
          Seq(resolveValues(q"$v"))

        case v if v.tpe <:< weakTypeOf[(_, _)] =>
          //xx(15, v)
          mapPairs(Seq(v), t)

        case v if t.isMapAttr$ =>
          //xx(16, v)
          mapPairs(Seq(v), t)

        case set if t.isMany && set.tpe <:< weakTypeOf[Set[_]] =>
          //xx(17, set)
          Seq(resolveValues(q"$set", t).toSet)

        case vs if t.isMany =>
          //xx(18, vs)
          vs match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              //xx(19, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              //xx(20, vs)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              //xx(21, vs)
              resolveValues(q"$vs", t)
          }
        case other          =>
          //xx(22, other, other.raw)
          resolveValues(q"Seq($other)", t)
      }

      def multiple(values: Seq[Tree]) = values match {
        case vs if t == null =>
          //xx(30, vs)
          vs.flatMap(v => resolveValues(q"$v"))

        case vs if vs.nonEmpty && vs.head.tpe <:< weakTypeOf[(_, _)] =>
          //xx(31, vs)
          mapPairs(vs, t)

        case sets if t.isMany && sets.nonEmpty && sets.head.tpe <:< weakTypeOf[Set[_]] =>
          //xx(32, sets)
          sets.map(set => resolveValues(q"$set", t).toSet)

        case vs if t.isMany && vs.nonEmpty =>
          //xx(31, vs)
          vs.head match {
            case q"$pkg.Seq.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              //xx(32, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case q"$pkg.List.apply[$tpe](..$sets)" if tpe.tpe <:< weakTypeOf[Set[_]] =>
              //xx(33, vs, sets)
              sets.map(set => resolveValues(q"$set", t).toSet)

            case _ =>
              //xx(34, vs)
              vs.flatMap(v => resolveValues(q"$v", t))
          }
        case vs                            =>
          //xx(35, vs)
          vs.flatMap(v => resolveValues(q"$v", t))
      }

      values match {
        case q"Seq($value)" =>
          //xx(1, "single in Seq", value)
          single(value)

        case Apply(_, List(Select(_, TermName("$qmark")))) =>
          //xx(2, "datom")
          Qm

        case q"Seq(..$values)" =>
          //xx(3, "multiple", values)
          multiple(values)

        case other =>
          //xx(4, other)
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
      //xx(40, tree)
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
      //xx(41, tree)
      val at: att = if (t == null) null else t.at
      def noAppliedExpression(expr: String): Nothing = abort(
        s"Can't apply expression `$expr` here. Please assign expression to a variable and apply this instead."
      )
      def resolve(tree0: Tree, values: Seq[Tree] = Seq.empty[Tree]): Seq[Tree] = {
        //xx(42, tree0, tree0.raw)
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
      typess = List(postTypes)
      castss = List(postCasts)
      postTypes = Nil
      postCasts = Nil
    }
    //    //xx(801, elements)
    //    //xx(801, elements, types, casts)
    //xx(801, elements, typess, castss, nestedRefAttrs, hasVariables, txMetaCompositesCount, postTypes, postCasts, post)

    // Return checked model
    (
      genericImports,
      Model(VerifyRawModel(elements, false)),
      typess, castss, obj,
      hasVariables, txMetaCompositesCount,
      postTypes, postCasts,
      isOptNested,
      optNestedRefIndexes, optNestedTacitIndexes
    )
  }
}