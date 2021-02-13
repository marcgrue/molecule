package molecule.core.ops

import molecule.core.dsl.attributes._
import molecule.core.dsl.base.{FirstNS, NS}
import molecule.core.ops.exception.TreeOpsException
import molecule.datomic.base.ast.query._
import scala.language.existentials
import scala.reflect.macros.blackbox


private[molecule] trait TreeOps extends Liftables {
  val c: blackbox.Context
  import c.universe._


  override def abort(msg: String) = throw new TreeOpsException(msg)

  def firstLow(str: Any): String = str.toString.head.toLower.toString + str.toString.tail

  def clean(attr: String): String = attr.last match {
    case '_' => attr.init
    case '$' => attr.init
    case _   => attr
  }

  implicit class richTree(val t: Tree) {
    //    val zz = InspectMacro("TreeOps", 1)
    lazy val tpe_         : Type           = if (t == null) abort("[molecule.ops.TreeOps.richTree] Can't handle null.") else c.typecheck(t).tpe
    lazy val at           : att            = att(t)
    lazy val nsFull       : String         = if (t.isFirstNS) t.symbol.name.toString else at.nsFull.toString
    lazy val name         : String         = at.toString
    lazy val nameClean    : String         = clean(at.toString)
    lazy val tpeS         : String         = at.tpeS
    lazy val card         : Int            = at.card
    lazy val enumPrefix   : String         = at.enumPrefix
    lazy val enumPrefixOpt: Option[String] = if (isAnyEnum) Some(at.enumPrefix) else None

    def nsFull2: String = tpe_.baseClasses.foldLeft("") {
      case ("", s: ClassSymbol) if s.toType =:= typeOf[NS] => "NS"
      case ("NS", nsFull)                                  => nsFull.name.toString
      case (nsFull, _)                                     => nsFull
    }

    def isNS: Boolean = tpe_ <:< typeOf[NS]
    def isFirstNS: Boolean = tpe_ <:< typeOf[FirstNS]
    def owner: String = t.symbol.typeSignature.typeParams.head.name.toString
    def alias: String = t.symbol.typeSignature.typeParams.head.name.toString

    def refThis: String = tpe_.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.head.typeSymbol.name.toString.init
    def refNext: String = tpe_.baseType(weakTypeOf[Ref[_, _]].typeSymbol).typeArgs.last.typeSymbol.name.toString.init

    def isBidirectional: Boolean = tpe_ <:< weakTypeOf[Bidirectional_]
    def isBiSelfRef: Boolean = tpe_ <:< weakTypeOf[BiSelfRef_]
    def isBiSelfRefAttr: Boolean = tpe_ <:< weakTypeOf[BiSelfRefAttr_]

    def isBiOtherRef: Boolean = tpe_ <:< weakTypeOf[BiOtherRef_[_]]
    def isBiOtherRefAttr: Boolean = tpe_ <:< weakTypeOf[BiOtherRefAttr_[_]]

    def isBiEdge: Boolean = tpe_ <:< weakTypeOf[BiEdge_]
    def isBiEdgeRef: Boolean = tpe_ <:< weakTypeOf[BiEdgeRef_[_]]
    def isBiEdgeRefAttr: Boolean = tpe_ <:< weakTypeOf[BiEdgeRefAttr_[_]]

    def isBiEdgePropRef: Boolean = tpe_ <:< weakTypeOf[BiEdgePropRef_]
    def isBiEdgePropAttr: Boolean = tpe_ <:< weakTypeOf[BiEdgePropAttr_]
    def isBiEdgePropRefAttr: Boolean = tpe_ <:< weakTypeOf[BiEdgePropRefAttr_]

    def isBiTargetRefAttr: Boolean = tpe_ <:< weakTypeOf[BiTargetRefAttr_[_]]
    def isBiTargetRef: Boolean = tpe_ <:< weakTypeOf[BiTargetRef_[_]]

    def isRef: Boolean = tpe_ <:< weakTypeOf[Ref[_, _]]
    def refCard: Int = if (tpe_ <:< weakTypeOf[ManyRef[_, _]]) 2 else 1

    def isAttr: Boolean = tpe_ <:< typeOf[Attr]

    def isRefAttr$: Boolean = tpe_ <:< weakTypeOf[RefAttr$[_]]
    def isRefAttr: Boolean = tpe_ <:< weakTypeOf[RefAttr[_]]

    def isValueAttr: Boolean = tpe_ <:< weakTypeOf[ValueAttr[_, _, _, _]]
    def isValueAttr$: Boolean = tpe_ <:< weakTypeOf[ValueAttr$[_]]
    def isMapAttrK: Boolean = tpe_ <:< typeOf[MapAttrK]
    def isMapAttr: Boolean = tpe_ <:< weakTypeOf[MapAttr[_, _, _, _]]
    def isMapAttr$: Boolean = tpe_ <:< weakTypeOf[MapAttr$[_, _, _]]
    def isOne: Boolean = tpe_ <:< weakTypeOf[One[_, _, _]]
    def isMany: Boolean = tpe_ <:< weakTypeOf[Many[_, _, _, _]]
    def isEnum: Boolean = tpe_ <:< weakTypeOf[Enum]
    def isEnum$: Boolean = tpe_ <:< weakTypeOf[Enum$[_, _]]
    def isAnyEnum: Boolean = isEnum || isEnum$
  }
  def nsString(nsFull: String): String = nsFull
  def nsString(nsTree: Tree): String = nsString(nsTree.symbol.name.toString)
  def nsString(nsName: Name): String = nsString(nsName.decodedName.toString)

  def extractNsAttr(tpe: Type, tree: Tree) = {
    val ss = c.typecheck(tree).tpe.baseType(tpe.typeSymbol).typeArgs.head.typeSymbol.name.toString.split('_')
    // part_Ns or Ns
    ":" + ss.init.mkString("_") + "/" + ss.last
  }

  // Todo more types...
  def tpe(s: String): Tree = s match {
    case "String" => tq"String"
    case "Int"    => tq"Int"
    case unknown  => abort(s"[TreeOps:tpe] Unknown type: $unknown")
  }

  def inputMolecule_i_o(inArity: Int, outArity: Int): Tree = inArity match {
    case 1 => outArity match {
      case 1  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_01"
      case 2  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_02"
      case 3  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_03"
      case 4  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_04"
      case 5  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_05"
      case 6  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_06"
      case 7  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_07"
      case 8  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_08"
      case 9  => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_09"
      case 10 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_10"
      case 11 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_11"
      case 12 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_12"
      case 13 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_13"
      case 14 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_14"
      case 15 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_15"
      case 16 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_16"
      case 17 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_17"
      case 18 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_18"
      case 19 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_19"
      case 20 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_20"
      case 21 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_21"
      case 22 => tq"_root_.molecule.core.api.Molecule_1.Molecule_1_22"
    }
    case 2 => outArity match {
      case 1  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_01"
      case 2  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_02"
      case 3  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_03"
      case 4  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_04"
      case 5  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_05"
      case 6  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_06"
      case 7  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_07"
      case 8  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_08"
      case 9  => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_09"
      case 10 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_10"
      case 11 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_11"
      case 12 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_12"
      case 13 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_13"
      case 14 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_14"
      case 15 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_15"
      case 16 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_16"
      case 17 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_17"
      case 18 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_18"
      case 19 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_19"
      case 20 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_20"
      case 21 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_21"
      case 22 => tq"_root_.molecule.core.api.Molecule_2.Molecule_2_22"
    }
    case 3 => outArity match {
      case 1  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_01"
      case 2  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_02"
      case 3  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_03"
      case 4  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_04"
      case 5  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_05"
      case 6  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_06"
      case 7  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_07"
      case 8  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_08"
      case 9  => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_09"
      case 10 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_10"
      case 11 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_11"
      case 12 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_12"
      case 13 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_13"
      case 14 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_14"
      case 15 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_15"
      case 16 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_16"
      case 17 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_17"
      case 18 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_18"
      case 19 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_19"
      case 20 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_20"
      case 21 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_21"
      case 22 => tq"_root_.molecule.core.api.Molecule_3.Molecule_3_22"
    }
  }

  def molecule_o(outArity: Int): Tree = outArity match {
    case 1  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_01"
    case 2  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_02"
    case 3  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_03"
    case 4  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_04"
    case 5  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_05"
    case 6  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_06"
    case 7  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_07"
    case 8  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_08"
    case 9  => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_09"
    case 10 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_10"
    case 11 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_11"
    case 12 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_12"
    case 13 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_13"
    case 14 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_14"
    case 15 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_15"
    case 16 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_16"
    case 17 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_17"
    case 18 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_18"
    case 19 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_19"
    case 20 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_20"
    case 21 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_21"
    case 22 => tq"_root_.molecule.core.api.Molecule_0.Molecule_0_22"
    case o  => abort(s"[TreeOps:molecule_o] Unsupported arity for MoleculeX: $o")
  }

  def nestedTupleClassX(allLevels: Int): Tree = allLevels match {
    case 1 => q""
    case 2 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples1"
    case 3 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples2"
    case 4 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples3"
    case 5 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples4"
    case 6 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples5"
    case 7 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples6"
    case 8 => tq"_root_.molecule.core.macros.NestedTuples.NestedTuples7"
    case o => abort(s"Unsupported arity for NestedTuplesX: $o")
  }

  def namespaceSymbol(tree: Tree): Symbol = {
    def traverse(t: Tree): Symbol = t match {
      case q"$a.and($b)  "                                     => traverse(a)
      case q"$a.and[..$ts]($b)  "                              => traverse(a)
      case q"$a.or($b)  "                                      => traverse(a)
      case q"$a.eqs($b)  "                                     => traverse(a)
      case q"$a.or[..$ts]($b)  "                               => traverse(a)
      case q"TermValue.apply($a)  "                            => traverse(a)
      case q"immutable.this.List.apply[$tpe](..$a)  "          => traverse(a.head)
      case q"collection.this.Seq.apply[$tpe](..$a)  "          => traverse(a.head)
      case Select(nsFull, attr) if nsFull.tpe <:< typeOf[NS]   => nsFull.tpe.typeSymbol
      case nsFull@Select(_, name) if nsFull.tpe <:< typeOf[NS] => nsFull.tpe.typeSymbol
      case _                                                   => (t collect {
        case nsFull@Select(_, name) if nsFull.tpe <:< typeOf[NS] => nsFull.tpe.typeSymbol
      }).distinct.reverse.head
    }

    traverse(tree)
  }

  class nsp(val sym: Symbol) {

    lazy val nsType: Type = sym match {
      case s: TermSymbol if s.isPublic                => s.typeSignature.typeSymbol.typeSignature
      case s: MethodSymbol
        if s.asMethod.returnType <:< weakTypeOf[Attr] => s.asMethod.returnType
      case s: ClassSymbol if s.toType <:< typeOf[NS]  => s.toType
      case unexpected                                 =>
        abortTree(q"$unexpected", s"[TreeOps:nsp] Unexpected namespace symbol")
    }

    override def toString: String = {
      val s     = sym.name.toString
      val first = s.split("_(\\d+|In_.*)").head
      first
    }

    def attrs: List[att] = nsType.members.collect {
      case s: TermSymbol if s.isPublic                                   => new att(s)
      case s: MethodSymbol if s.asMethod.returnType <:< weakTypeOf[Attr] => new att(s)
    }.toList.reverse


    def enums: List[att] = attrs.filter(_.isAnyEnum).distinct
    def isNamespace: Boolean = true
  }

  object nsp {
    def apply(tree: Tree) = new nsp(namespaceSymbol(tree))
    def apply(symbol: Symbol) = new nsp(symbol)
  }

  class att(val sym: Symbol) {

    lazy val attrType: Type = sym match {
      case t: ModuleSymbol                                               => t.moduleClass.asType.toType
      case t: TermSymbol                                                 => t.typeSignature.typeSymbol.typeSignature
      case t: MethodSymbol if t.asMethod.returnType <:< weakTypeOf[Attr] => t.asMethod.returnType
      case unexpected                                                    =>
        abortTree(q"$unexpected", s"[TreeOps:attrType] Unexpected attribute symbol")
    }

    lazy val tpe: Type = sym match {
      case t: TermSymbol if t.isPublic =>
        val tpe = t.typeSignature.typeSymbol.asType.toType
        t match {
          case _ if tpe <:< weakTypeOf[Ref[_, _]]             => typeOf[Long]
          case _ if tpe <:< weakTypeOf[RefAttr[_]]            => typeOf[Long]
          case _ if tpe <:< weakTypeOf[RefAttr$[_]]           => typeOf[Long]
          case _ if tpe <:< weakTypeOf[Enum]                  => typeOf[String]
          case _ if tpe <:< weakTypeOf[ValueAttr[_, _, _, _]] => t.typeSignature.baseType(weakTypeOf[ValueAttr[_, _, _, _]].typeSymbol).typeArgs.last
          case _ if tpe <:< weakTypeOf[ValueAttr$[_]]         => t.typeSignature.baseType(weakTypeOf[ValueAttr$[_]].typeSymbol).typeArgs.head
          case _ if tpe <:< weakTypeOf[MapAttr[_, _, _, _]]   => t.typeSignature.baseType(weakTypeOf[MapAttr[_, _, _, _]].typeSymbol).typeArgs.last
          case _ if tpe <:< weakTypeOf[MapAttr$[_, _, _]]     => t.typeSignature.baseType(weakTypeOf[MapAttr$[_, _, _]].typeSymbol).typeArgs.last
          case _                                              => NoType
        }
      case unexpected                  =>
        abortTree(q"$unexpected", s"[TreeOps:tpe] ModelOps.att(sym) can only take an Attr symbol")
    }

    def owner: Symbol = attrType.typeSymbol.owner
    def nsFull: nsp = new nsp(owner)

    def name: TermName = TermName(toString)
    def fullName: String = attrType.typeSymbol.fullName

    def tpeS: String = if (tpe =:= NoType) "" else tpe.toString

    def contentType: Type = tpe

    lazy val isOne: Boolean = attrType <:< weakTypeOf[One[_, _, _]] ||
      attrType <:< weakTypeOf[OneValueAttr$[_, _]] ||
      attrType <:< weakTypeOf[OneEnum$[_]] ||
      attrType <:< weakTypeOf[OneRefAttr[_, _]] ||
      attrType <:< weakTypeOf[OneRefAttr$[_]]

    lazy val isMany: Boolean = attrType <:< weakTypeOf[Many[_, _, _, _]] ||
      attrType <:< weakTypeOf[ManyValueAttr$[_, _, _]] ||
      attrType <:< weakTypeOf[ManyEnums[_, _]] ||
      attrType <:< weakTypeOf[ManyEnums$[_]] ||
      attrType <:< weakTypeOf[ManyRefAttr[_, _]] ||
      attrType <:< weakTypeOf[ManyRefAttr$[_]]

    lazy val isMap: Boolean = attrType <:< weakTypeOf[MapAttr[_, _, _, _]] ||
      attrType <:< weakTypeOf[MapAttr$[_, _, _]]

    lazy val isMapK: Boolean = attrType <:< weakTypeOf[MapAttrK]

    def card: Int = if (isMapK) 4 else if (isMap) 3 else if (isMany) 2 else 1

    def isValue: Boolean = attrType <:< weakTypeOf[One[_, _, _]] ||
      attrType <:< weakTypeOf[Many[_, _, _, _]] ||
      attrType <:< weakTypeOf[OneEnum[_, _]]

    def isAnyEnum: Boolean = attrType <:< weakTypeOf[Enum]


    def keyw: KW = KW(nsFull.toString, this.toString)
    def kw: KW = KW(nsFull.toString, this.toString)
    def kwS: String = s":$nsFull/$name"

    def enumValues: List[String] = {
      val attrName = if (toString.last == '_') toString.init else toString
      attrType.baseClasses.find {
        cl => cl.isClass && !cl.isModuleClass && cl.name.toString == attrName
      }.get.asClass.toType.members.collect {
        case v: TermSymbol if v.isPrivate && v.isLazy && v.typeSignature.typeSymbol.asType.toType =:= typeOf[EnumValue.type] => v.name.decodedName.toString.trim
      }.toList.reverse
    }

    def hasEnum(enumCandidate: String): Boolean = enumValues.contains(enumCandidate)
    def enumPrefix: String = nsFull.enums.size match {
      case 0 => ""
      case _ =>
        val last  = name.toString.last
        val name0 = if (last == '_' || last == '$') name.toString.init else name
        s":$nsFull.$name0/"
    }

    override def toString: String = sym.name.toString.head.toLower.toString + sym.name.toString.tail
  }

  object att {
    def apply(tree: Tree): att = tree match {
      case q"$prev.apply(..$vs)"          => new att(c.typecheck(prev).symbol)
      case q"$prev.apply[..$tpes](..$vs)" => new att(c.typecheck(prev).symbol)
      case q"$prev.$op(..$vs)"            => new att(c.typecheck(prev).symbol)
      case t                              => new att(c.typecheck(t).symbol)
    }
    def apply(ts: TermSymbol): att = new att(ts)
    def apply(tpe: Type): att = new att(tpe.typeSymbol)
  }
}
