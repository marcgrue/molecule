package molecule.ops
import molecule.ast.query._
import molecule.boilerplate.attributes._
import molecule.boilerplate.{FirstNS, NS}
import molecule.composition.OneOptional
import scala.language.existentials
//import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context


private[molecule] trait TreeOps[Ctx <: Context] extends Liftables[Ctx] {
  import c.universe._

  def firstLow(str: Any) = str.toString.head.toLower + str.toString.tail

  implicit class richTree(t: Tree) {
    lazy val tpe_       = if (t == null) abort("[molecule.ops.TreeOps.richTree] Can't handle null.") else c.typecheck(t).tpe
    lazy val at         = att(t)
    lazy val ns         = at.ns.toString
    lazy val name       = at.toString
    lazy val tpeS       = at.tpeS
    lazy val card       = at.card
    lazy val enumPrefix = at.enumPrefix

    def isNS = tpe_ <:< typeOf[NS]
    def isFirstNS = tpe_ <:< typeOf[FirstNS]
    def owner = t.symbol.typeSignature.typeParams.head.name.toString
    def alias = t.symbol.typeSignature.typeParams.head.name.toString

    def refThis = firstLow(tpe_.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.head.typeSymbol.name.toString)
    def refNext = firstLow(tpe_.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.last.typeSymbol.name.toString)

    def isBiSelfRef = tpe_ <:< weakTypeOf[BiSelfRef_]
    def isBiSelfRefAttr = tpe_ <:< weakTypeOf[BiSelfRefAttr_]

    def isBiOtherRef = tpe_ <:< weakTypeOf[BiOtherRef_[_]]
    def isBiOtherRefAttr = tpe_ <:< weakTypeOf[BiOtherRefAttr_[_]]

    def isBiEdge = tpe_ <:< weakTypeOf[BiEdge_]
    def isBiEdgeRef = tpe_ <:< weakTypeOf[BiEdgeRef_[_]]
    def isBiEdgeRefAttr = tpe_ <:< weakTypeOf[BiEdgeRefAttr_[_]]

    def isBiEdgePropRef = tpe_ <:< weakTypeOf[BiEdgePropRef_]
    def isBiEdgePropAttr = tpe_ <:< weakTypeOf[BiEdgePropAttr_]
    def isBiEdgePropRefAttr = tpe_ <:< weakTypeOf[BiEdgePropRefAttr_]

    def isBiTargetRefAttr = tpe_ <:< weakTypeOf[BiTargetRefAttr_[_]]
    def isBiTargetRef = tpe_ <:< weakTypeOf[BiTargetRef_[_]]

    def isRef = tpe_ <:< weakTypeOf[Ref[_, _]]
    def isOneRef = tpe_ <:< weakTypeOf[OneRef[_, _]]
    def isManyRef = tpe_ <:< weakTypeOf[ManyRef[_, _]]
    def refCard = if (tpe_ <:< weakTypeOf[ManyRef[_, _]]) 2 else 1

    def isAttr = tpe_ <:< typeOf[Attr]

    def isOneOptional = tpe_ <:< typeOf[OneOptional[_, _]]

    def isRefAttr$ = tpe_ <:< weakTypeOf[RefAttr$[_, _]]
    def isRefAttr = tpe_ <:< weakTypeOf[RefAttr[_, _]]
    def isOneRefAttr = tpe_ <:< weakTypeOf[OneRefAttr[_, _]]
    def isManyRefAttr = tpe_ <:< weakTypeOf[ManyRefAttr[_, _]]


    def isValueAttr = tpe_ <:< weakTypeOf[ValueAttr[_, _, _, _]]
    def isValueAttr$ = tpe_ <:< weakTypeOf[ValueAttr$[_]]
    def isMapAttrK = tpe_ <:< typeOf[MapAttrK]
    def isMapAttr = tpe_ <:< weakTypeOf[MapAttr[_, _, _, _]]
    def isMapAttr$ = tpe_ <:< weakTypeOf[MapAttr$[_, _]]
    def isOne = tpe_ <:< weakTypeOf[One[_, _, _]]
    def isMany = tpe_ <:< weakTypeOf[Many[_, _, _, _]]
    def isEnum = tpe_ <:< weakTypeOf[Enum]
    def isEnum$ = tpe_ <:< weakTypeOf[Enum$[_, _]]
    def isAnyEnum = isEnum || isEnum$
    def isOneEnum = tpe_ <:< weakTypeOf[OneEnum[_, _]]
    def isManyEnum = tpe_ <:< weakTypeOf[ManyEnums[_, _]]
    def isOneURI = tpe_ <:< weakTypeOf[OneURI[_, _]]
    def isManyURI = tpe_ <:< weakTypeOf[ManyURI[_, _]]
    override def toString = t.tpe.typeSymbol.name.toString
  }
  def nsString(ns: String): String = ns
  def nsString(ns: Tree): String = nsString(ns.symbol.name.toString)
  def nsString(ns: Name): String = nsString(ns.decodedName.toString)


  // Todo more types...
  def tpe(s: String) = s match {
    case "String" => tq"String"
    case "Int"    => tq"Int"
    case unknown  => abort(s"[TreeOps:tpe] Unknown type: $unknown")
  }

  def inputMolecule_i_o(inArity: Int, outArity: Int) = (inArity, outArity) match {
    case (1, 0)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_00"
    case (1, 1)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_01"
    case (1, 2)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_02"
    case (1, 3)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_03"
    case (1, 4)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_04"
    case (1, 5)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_05"
    case (1, 6)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_06"
    case (1, 7)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_07"
    case (1, 8)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_08"
    case (1, 9)  => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_09"
    case (1, 10) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_10"
    case (1, 11) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_11"
    case (1, 12) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_12"
    case (1, 13) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_13"
    case (1, 14) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_14"
    case (1, 15) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_15"
    case (1, 16) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_16"
    case (1, 17) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_17"
    case (1, 18) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_18"
    case (1, 19) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_19"
    case (1, 20) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_20"
    case (1, 21) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_21"
    case (1, 22) => tq"molecule.composition.input.InputMolecule_1.InputMolecule_1_22"

    case (2, 0)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_00"
    case (2, 1)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_01"
    case (2, 2)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_02"
    case (2, 3)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_03"
    case (2, 4)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_04"
    case (2, 5)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_05"
    case (2, 6)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_06"
    case (2, 7)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_07"
    case (2, 8)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_08"
    case (2, 9)  => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_09"
    case (2, 10) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_10"
    case (2, 11) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_11"
    case (2, 12) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_12"
    case (2, 13) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_13"
    case (2, 14) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_14"
    case (2, 15) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_15"
    case (2, 16) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_16"
    case (2, 17) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_17"
    case (2, 18) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_18"
    case (2, 19) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_19"
    case (2, 20) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_20"
    case (2, 21) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_21"
    case (2, 22) => tq"molecule.composition.input.InputMolecule_2.InputMolecule_2_22"

    case (3, 0)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_00"
    case (3, 1)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_01"
    case (3, 2)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_02"
    case (3, 3)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_03"
    case (3, 4)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_04"
    case (3, 5)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_05"
    case (3, 6)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_06"
    case (3, 7)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_07"
    case (3, 8)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_08"
    case (3, 9)  => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_09"
    case (3, 10) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_10"
    case (3, 11) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_11"
    case (3, 12) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_12"
    case (3, 13) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_13"
    case (3, 14) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_14"
    case (3, 15) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_15"
    case (3, 16) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_16"
    case (3, 17) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_17"
    case (3, 18) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_18"
    case (3, 19) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_19"
    case (3, 20) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_20"
    case (3, 21) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_21"
    case (3, 22) => tq"molecule.composition.input.InputMolecule_3.InputMolecule_3_22"

    case (i, o) => abort(s"[TreeOps:inputMolecule_i_o] Unsupported arity combination: $i in, $o out")
  }

  def molecule_o(outArity: Int) = outArity match {
    case 1  => tq"molecule.action.MoleculeOut.Molecule01"
    case 2  => tq"molecule.action.MoleculeOut.Molecule02"
    case 3  => tq"molecule.action.MoleculeOut.Molecule03"
    case 4  => tq"molecule.action.MoleculeOut.Molecule04"
    case 5  => tq"molecule.action.MoleculeOut.Molecule05"
    case 6  => tq"molecule.action.MoleculeOut.Molecule06"
    case 7  => tq"molecule.action.MoleculeOut.Molecule07"
    case 8  => tq"molecule.action.MoleculeOut.Molecule08"
    case 9  => tq"molecule.action.MoleculeOut.Molecule09"
    case 10 => tq"molecule.action.MoleculeOut.Molecule10"
    case 11 => tq"molecule.action.MoleculeOut.Molecule11"
    case 12 => tq"molecule.action.MoleculeOut.Molecule12"
    case 13 => tq"molecule.action.MoleculeOut.Molecule13"
    case 14 => tq"molecule.action.MoleculeOut.Molecule14"
    case 15 => tq"molecule.action.MoleculeOut.Molecule15"
    case 16 => tq"molecule.action.MoleculeOut.Molecule16"
    case 17 => tq"molecule.action.MoleculeOut.Molecule17"
    case 18 => tq"molecule.action.MoleculeOut.Molecule18"
    case 19 => tq"molecule.action.MoleculeOut.Molecule19"
    case 20 => tq"molecule.action.MoleculeOut.Molecule20"
    case 21 => tq"molecule.action.MoleculeOut.Molecule21"
    case 22 => tq"molecule.action.MoleculeOut.Molecule22"
    case o  => abort(s"[TreeOps:molecule_o] Unsupported arity for MoleculeX: $o")
  }

  def namespaceSymbol(tree: Tree) = {
    def traverse(t: Tree): Symbol = t match {
      case q"$a.and($b)"                               => traverse(a)
      case q"$a.and[..$ts]($b)"                        => traverse(a)
      case q"$a.or($b)"                                => traverse(a)
      case q"$a.eqs($b)"                               => traverse(a)
      case q"$a.or[..$ts]($b)"                         => traverse(a)
      case q"TermValue.apply($a)"                      => traverse(a)
      case q"immutable.this.List.apply[$tpe](..$a)"    => traverse(a.head)
      case q"collection.this.Seq.apply[$tpe](..$a)"    => traverse(a.head)
      case Select(ns, attr) if ns.tpe <:< typeOf[NS]   => ns.tpe.typeSymbol
      case ns@Select(_, name) if ns.tpe <:< typeOf[NS] => ns.tpe.typeSymbol
      case _                                           => (t collect {
        case ns@Select(_, name) if ns.tpe <:< typeOf[NS] => ns.tpe.typeSymbol
      }).distinct.reverse.head
    }

    traverse(tree)
  }

  class nsp(val sym: Symbol) {
    val x = DebugMacro("ModelOps:nsp", 1)

    lazy val nsType = sym match {
      case s: TermSymbol if s.isPublic                => s.typeSignature.typeSymbol.typeSignature
      case s: MethodSymbol
        if s.asMethod.returnType <:< weakTypeOf[Attr] => s.asMethod.returnType
      case s: ClassSymbol if s.toType <:< typeOf[NS]  => s.toType
      case unexpected                                 =>
        abortTree(q"$unexpected", s"[TreeOps:nsp] Unexpected namespace symbol")
    }

    override def toString = {
      val s = sym.name.toString
      val first = s.split("_(\\d+|In_.*)").head
      first.head.toLower + first.tail
    }

    def attrs = {


      nsType.members.collect {
        case s: TermSymbol if s.isPublic                                   => new att(s)
        case s: MethodSymbol if s.asMethod.returnType <:< weakTypeOf[Attr] => new att(s)
      }.toList.reverse
    }

    def enums = {
      val enums1 = attrs.filter(_.isAnyEnum).distinct

      //      x(1,
      ////        attrs,
      //        attrs(101),
      //        attrs(101).attrType,
      //        attrs(101).attrType.typeSymbol,
      //        attrs(101).attrType.typeSymbol.asType,
      //        attrs(101).attrType.typeSymbol.asType.toType,
      //        attrs(101).attrType.typeSymbol.asType.toType <:< weakTypeOf[Enum],
      //        attrs(101).attrType.baseClasses,
      //        attrs(101).attrType.baseClasses.exists(t => t.asType.toType <:< weakTypeOf[Enum]),
      ////        attrs(101).attrType.dealias,
      ////        attrs(101).attrType.decls,
      ////        attrs(101).attrType.erasure,
      ////        attrs(101).attrType.typeArgs,
      ////        attrs(101).attrType.companion,
      ////        attrs(101).attrType.etaExpand,
      ////        attrs(101).attrType.resultType,
      ////        attrs(101).attrType.widen,
      ////        attrs(101).attrType.termSymbol,
      ////        attrs(101).attrType.typeSymbol,
      ////        attrs(101).attrType.typeSymbol.typeSignature,
      ////        attrs(101).attrType.typeSymbol.isModule,
      ////        attrs(101).attrType.typeConstructor,
      //        attrs(101).isAnyEnum
      //      )
      //      x(2,
      ////        attrs,
      //        attrs(113),
      //        attrs(113).attrType,
      //        attrs(113).attrType.typeSymbol,
      //        attrs(113).attrType.typeSymbol.typeSignature,
      //        attrs(113).isAnyEnum
      //      )

      enums1
    }
    def isNamespace = true
  }

  object nsp {
    def apply(tree: Tree) = new nsp(namespaceSymbol(tree))
    def apply(symbol: Symbol) = new nsp(symbol)
  }

  class att(val sym: Symbol) {
    val x = DebugMacro("TreeOps:att", 1)

    lazy val attrType = sym match {
      case t: ModuleSymbol                                               => t.moduleClass.asType.toType
      case t: TermSymbol                                                 => t.typeSignature.typeSymbol.typeSignature
      case t: MethodSymbol if t.asMethod.returnType <:< weakTypeOf[Attr] => t.asMethod.returnType
      case unexpected                                                    =>
        abortTree(q"$unexpected", s"[TreeOps:attrType] Unexpected attribute symbol")
    }

    lazy val tpe = sym match {
      case t: TermSymbol if t.isPublic && t.typeSignature.typeSymbol.asType.toType <:< weakTypeOf[Ref[_, _]]             => typeOf[Long]
      case t: TermSymbol if t.isPublic && t.typeSignature.typeSymbol.asType.toType <:< weakTypeOf[RefAttr[_, _]]         => typeOf[Long]
      case t: TermSymbol if t.isPublic && t.typeSignature.typeSymbol.asType.toType <:< weakTypeOf[Enum]                  => typeOf[String]
      case t: TermSymbol if t.isPublic && t.typeSignature.typeSymbol.asType.toType <:< weakTypeOf[ValueAttr[_, _, _, _]] =>
        t.typeSignature.baseType(weakTypeOf[ValueAttr[_, _, _, _]].typeSymbol).typeArgs.init.last
      case t: TermSymbol if t.isPublic && t.typeSignature.typeSymbol.asType.toType <:< weakTypeOf[OneOptional[_, _]]     =>
        t.typeSignature.baseType(weakTypeOf[OneOptional[_, _]].typeSymbol).typeArgs.last
      case t: TermSymbol if t.isPublic                                                                                   => NoType
      case t: MethodSymbol if t.asMethod.returnType <:< weakTypeOf[Ref[_, _]]                                            => typeOf[Long]
      case unexpected                                                                                                    =>
        abortTree(q"$unexpected", s"[TreeOps:tpe] ModelOps.att(sym) can only take an Attr symbol")
    }

    def owner = attrType.typeSymbol.owner
    def ns = new nsp(owner)

    def name = TermName(toString)
    def fullName = attrType.typeSymbol.fullName

    def tpeS = if (tpe =:= NoType) "" else tpe.toString

    def contentType = tpe

    def isOne = attrType <:< weakTypeOf[One[_, _, _]] ||
      attrType <:< weakTypeOf[OneValueAttr$[_, _]] ||
      attrType <:< weakTypeOf[OneEnum$[_]] ||
      attrType <:< weakTypeOf[OneRefAttr[_, _]] ||
      attrType <:< weakTypeOf[OneRefAttr$[_]]

    def isMany = attrType <:< weakTypeOf[Many[_, _, _, _]] ||
      attrType <:< weakTypeOf[ManyValueAttr$[_, _]] ||
      attrType <:< weakTypeOf[ManyEnums[_, _]] ||
      attrType <:< weakTypeOf[ManyEnums$[_]] ||
      attrType <:< weakTypeOf[ManyRefAttr[_, _]] ||
      attrType <:< weakTypeOf[ManyRefAttr$[_]]

    def isMap = attrType <:< weakTypeOf[MapAttr[_, _, _, _]] ||
      attrType <:< weakTypeOf[MapAttr$[_, _]]

    def isMapK = attrType <:< weakTypeOf[MapAttrK]

    def card = if (isMapK) 4 else if (isMap) 3 else if (isMany) 2 else 1

    def isValue = attrType <:< weakTypeOf[One[_, _, _]] ||
      attrType <:< weakTypeOf[Many[_, _, _, _]] ||
      attrType <:< weakTypeOf[OneEnum[_, _]]

    def isAnyEnum = attrType <:< weakTypeOf[Enum] || attrType <:< weakTypeOf[Enum$[_, _]]


    def keyw = KW(ns.toString, this.toString)
    def kw = KW(ns.toString, this.toString)
    def kwS = s":$ns/$name"

    def enumValues = {
//      val name0 = toString.last
//      val name1 = if (name0 == '_' || name0 == '$') name.toString.init else name
      val attr = toString
      val attrClean = if (attr.last == '_' || attr.last == '$') attr.init else attr
//      val enumsx = attrType.baseClasses.find { cl => cl.isClass && !cl.isModuleClass && cl.name.toString == attrClean + "__enums__" }
//      x(6,
//        attrType,
//        toString,
//        attrClean,
//        enumPrefix,
//        enumsx,
////        enumsx.get.asClass,
////        enumsx.get.asClass.toType.members,
//        enumsx.get.asClass.toType.members.collect {
//          case v: TermSymbol if v.isPrivate && v.isLazy && v.typeSignature.typeSymbol.asType.toType =:= typeOf[EnumValue.type] => v.name.decodedName.toString.trim
//        }.toList.reverse
//      )
      attrType.baseClasses.find { cl => cl.isClass && !cl.isModuleClass && cl.name.toString == attrClean + "__enums__" }.get.asClass.toType.members.collect {
        case v: TermSymbol if v.isPrivate && v.isLazy && v.typeSignature.typeSymbol.asType.toType =:= typeOf[EnumValue.type] => v.name.decodedName.toString.trim
      }.toList.reverse
    }

    //    def enumValues = attrType.members.collect {
    //      case v: TermSymbol if v.isPrivate && v.isLazy && v.typeSignature.typeSymbol.asType.toType =:= typeOf[EnumValue.type] => v.name.decodedName.toString.trim
    //    }.toList.reverse

    def hasEnum(enumCandidate: String) = enumValues.contains(enumCandidate)
    def enumPrefix = ns.enums.size match {
      case 0 => ""
      case _ =>
        val last = name.toString.last
        val name0 = if (last == '_' || last == '$') name.toString.init else name
        s":$ns.$name0/"
    }

    override def toString = sym.name.toString.head.toLower + sym.name.toString.tail
  }

  object att {
    def apply(attr: Tree) = new att(c.typecheck(attr).symbol)
    def apply(ts: TermSymbol) = new att(ts)
    def apply(tpe: Type) = new att(tpe.typeSymbol)
  }
}
