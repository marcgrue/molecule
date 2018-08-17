package sbtmolecule

import java.io.File
import scala.io.Source
import sbt._

object MoleculeBoilerplate {

  class SchemaDefinitionException(message: String) extends RuntimeException(message)

  // Definition AST .......................................

  case class Definition(pkg: String, in: Int, out: Int, domain: String, curPart: String, curPartDescr: String, nss: Seq[Namespace]) {
    def addAttr(attrs: Seq[DefAttr]) = {
      val previousNs = nss.init
      val lastNs = nss.last
      copy(nss = previousNs :+ lastNs.copy(attrs = lastNs.attrs ++ attrs))
    }
  }

  case class Namespace(part: String, partDescr: Option[String], ns: String, nsDescr: Option[String], opt: Option[Extension] = None, attrs: Seq[DefAttr] = Seq()) {
    override def toString =
      s"""Namespace(
         |   part     : $part
         |   partDescr: $partDescr
         |   ns       : $ns
         |   nsDescr  : $nsDescr
         |   opt      : $opt
         |   attrs    :
         |     ${attrs.mkString("\n     ")}
         |)""".stripMargin
  }

  sealed trait Extension
  case object Edge extends Extension

  sealed trait DefAttr {
    val attr     : String
    val attrClean: String
    val clazz    : String
    val tpe      : String
    val baseTpe  : String
    val options  : Seq[Optional]
    val attrGroup: Option[String]
  }


  case class Val(attr: String, attrClean: String, clazz: String, tpe: String, baseTpe: String, datomicTpe: String,
                 options: Seq[Optional] = Seq(), bi: Option[String] = None, revRef: String = "", attrGroup: Option[String] = None) extends DefAttr

  case class Enum(attr: String, attrClean: String, clazz: String, tpe: String, baseTpe: String, enums: Seq[String],
                  options: Seq[Optional] = Seq(), bi: Option[String] = None, revRef: String = "", attrGroup: Option[String] = None) extends DefAttr

  case class Ref(attr: String, attrClean: String, clazz: String, clazz2: String, tpe: String, baseTpe: String, refNs: String,
                 options: Seq[Optional] = Seq(), bi: Option[String] = None, revRef: String = "", attrGroup: Option[String] = None) extends DefAttr

  case class BackRef(attr: String, attrClean: String, clazz: String, clazz2: String, tpe: String, baseTpe: String, backRefNs: String,
                     options: Seq[Optional] = Seq(), attrGroup: Option[String] = None) extends DefAttr

  case class Optional(datomicKeyValue: String, clazz: String)


  // Helpers ..........................................

  private def padS(longest: Int, str: String) = pad(longest, str.length)
  private def pad(longest: Int, shorter: Int) = if (longest > shorter) " " * (longest - shorter) else ""
  private def padI(n: Int) = if (n < 10) s"0$n" else s"$n"
  private def firstLow(str: Any) = str.toString.head.toLower + str.toString.tail
  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }


  // Parse ..........................................

  def parse(defFile: File, allIndexed: Boolean = true) = {
    val raw: List[String] = Source.fromFile(defFile).getLines().map(_.trim).toList

    // Checks .......................................................................

    // Check package statement
    raw.collectFirst {
      case r"package (.*)$p\..*" => p
    }.getOrElse {
      throw new SchemaDefinitionException("Found no package statement in definition file")
    }

    // Check input/output arities
    raw collect {
      case r"@InOut\((\d+)$in, (\d+)$out\)" => (in.toString.toInt, out.toString.toInt) match {
        case (i: Int, _) if i < 0 || i > 3  => throw new SchemaDefinitionException(s"Input arity in '${defFile.getName}' was $in. It should be in the range 0-3")
        case (_, o: Int) if o < 1 || o > 22 => throw new SchemaDefinitionException(s"Output arity of '${defFile.getName}' was $out. It should be in the range 1-22")
        case (i: Int, o: Int)               => (i, o)
      }
    } match {
      case Nil           => throw new SchemaDefinitionException(
        """Please annotate the first namespace definition with '@InOut(inArity, outArity)' where:
          |inArity is a number between 1-3 for how many inputs molecules of this schema can await
          |outArity is a number between 1-22 for how many output attributes molecules of this schema can have""".stripMargin)
      case h :: t :: Nil => throw new SchemaDefinitionException(
        """
          |Only the first namespace should be annotated with @InOut since all namespaces in a schema will need
          |to share the same arities to be able to carry over type information uniformly across namespaces.""".stripMargin)
      case annotations   => annotations.head
    }

    // Check domain name
    raw.collectFirst {
      case r"class (.*)${dmn}Definition"        => throw new SchemaDefinitionException(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"class (.*)${dmn}Definition \{"     => throw new SchemaDefinitionException(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"class (.*)${dmn}Definition \{ *\}" => throw new SchemaDefinitionException(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition"        => throw new SchemaDefinitionException(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition \{"     => throw new SchemaDefinitionException(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition \{ *\}" => throw new SchemaDefinitionException(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
    }

    raw.collect {
      case r"object (.*)${name}Definition"      => name
      case r"object (.*)${name}Definition \{"   => name
      case r"object (.*)${name}Definition \{\}" => name
    } match {
      case Nil                      => throw new SchemaDefinitionException("Couldn't find definition object <domain>Definition in " + defFile.getName)
      case l: List[_] if l.size > 1 => throw new SchemaDefinitionException(s"Only one definition object per definition file allowed. Found ${l.size}:" + l.mkString("\n - ", "Definition\n - ", "Definition"))
      case domainNameList           => firstLow(domainNameList.head)
    }


    // Parse ..........................................

    def parseOptions(str0: String, acc: Seq[Optional] = Nil, attr: String, curFullNs: String = ""): Seq[Optional] = {
      val indexed = Optional( """":db/index"             , true.asInstanceOf[Object]""", "Indexed")
      val options = str0 match {
        case r"\.doc\((.*)$msg\)(.*)$str" => parseOptions(str, acc :+ Optional(s"""":db/doc"               , $msg""", ""), attr, curFullNs)
        case r"\.fulltextSearch(.*)$str"  => parseOptions(str, acc :+ Optional("""":db/fulltext"          , true.asInstanceOf[Object]""", "FulltextSearch[Ns, In]"), attr, curFullNs)
        case r"\.uniqueValue(.*)$str"     => parseOptions(str, acc :+ Optional("""":db/unique"            , ":db.unique/value"""", "UniqueValue"), attr, curFullNs)
        case r"\.uniqueIdentity(.*)$str"  => parseOptions(str, acc :+ Optional("""":db/unique"            , ":db.unique/identity"""", "UniqueIdentity"), attr, curFullNs)
        case r"\.isComponent(.*)$str"     => parseOptions(str, acc :+ Optional("""":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent"), attr, curFullNs)
        case r"\.noHistory(.*)$str"       => parseOptions(str, acc :+ Optional("""":db/noHistory"         , true.asInstanceOf[Object]""", "NoHistory"), attr, curFullNs)
        case r"\.indexed(.*)$str"         => parseOptions(str, acc :+ indexed, attr, curFullNs)
        case ""                           => acc
        case unexpected                   => throw new SchemaDefinitionException(s"Unexpected options code for attribute `$attr` in namespace `$curFullNs` in ${defFile.getName}:\n" + unexpected)
      }
      if (allIndexed) (options :+ indexed).distinct else options
    }
    val isComponent = Optional("""":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent")

    def parseAttr(backTics: Boolean, attrClean: String, str: String, curPart: String, curFullNs: String, attrGroup0: Option[String]): Seq[DefAttr] = {
      val attr = if (backTics) s"`$attrClean`" else attrClean
      val attrK = attrClean + "K"
      val curNs = if (curFullNs.contains('_')) curFullNs.split("_").last else curFullNs
      val curPartDotNs = if (curFullNs.contains('_')) curFullNs.replace("_", ".") else curFullNs

      str match {
        case r"oneString(.*)$str"     => Seq(Val(attr, attrClean, "OneString", "String", "", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneInt(.*)$str"        => Seq(Val(attr, attrClean, "OneInt", "Int", "", "long", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneLong(.*)$str"       => Seq(Val(attr, attrClean, "OneLong", "Long", "", "long", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneFloat(.*)$str"      => Seq(Val(attr, attrClean, "OneFloat", "Float", "", "double", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneDouble(.*)$str"     => Seq(Val(attr, attrClean, "OneDouble", "Double", "", "double", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneBigInt(.*)$str"     => Seq(Val(attr, attrClean, "OneBigInt", "BigInt", "", "bigint", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneBigDecimal(.*)$str" => Seq(Val(attr, attrClean, "OneBigDecimal", "BigDecimal", "", "bigdec", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneByte(.*)$str"       => Seq(Val(attr, attrClean, "OneByte", "Byte", "", "bytes", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneBoolean(.*)$str"    => Seq(Val(attr, attrClean, "OneBoolean", "Boolean", "", "boolean", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneDate(.*)$str"       => Seq(Val(attr, attrClean, "OneDate", "Date", "", "instant", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneUUID(.*)$str"       => Seq(Val(attr, attrClean, "OneUUID", "UUID", "", "uuid", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"oneURI(.*)$str"        => Seq(Val(attr, attrClean, "OneURI", "URI", "", "uri", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))

        case r"manyString(.*)$str"     => Seq(Val(attr, attrClean, "ManyString", "Set[String]", "String", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyInt(.*)$str"        => Seq(Val(attr, attrClean, "ManyInt", "Set[Int]", "Int", "long", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyLong(.*)$str"       => Seq(Val(attr, attrClean, "ManyLong", "Set[Long]", "Long", "long", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyFloat(.*)$str"      => Seq(Val(attr, attrClean, "ManyFloat", "Set[Float]", "Float", "double", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyDouble(.*)$str"     => Seq(Val(attr, attrClean, "ManyDouble", "Set[Double]", "Double", "double", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyBigInt(.*)$str"     => Seq(Val(attr, attrClean, "ManyBigInt", "Set[BigInt]", "BigInt", "bigint", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyBigDecimal(.*)$str" => Seq(Val(attr, attrClean, "ManyBigDecimal", "Set[BigDecimal]", "BigDecimal", "bigdec", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyByte(.*)$str"       => Seq(Val(attr, attrClean, "ManyByte", "Set[Byte]", "Byte", "bytes", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyBoolean(.*)$str"    => Seq(Val(attr, attrClean, "ManyBoolean", "Set[Boolean]", "Boolean", "boolean", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyDate(.*)$str"       => Seq(Val(attr, attrClean, "ManyDate", "Set[Date]", "Date", "instant", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyUUID(.*)$str"       => Seq(Val(attr, attrClean, "ManyUUID", "Set[UUID]", "UUID", "uuid", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyURI(.*)$str"        => Seq(Val(attr, attrClean, "ManyURI", "Set[URI]", "URI", "uri", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))

        case r"mapString(.*)$str" => Seq(
          Val(attr, attrClean, "MapString", "Map[String, String]", "String", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneString", "String", "K", "string", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapInt(.*)$str" => Seq(
          Val(attr, attrClean, "MapInt", "Map[String, Int]", "Int", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneInt", "Int", "K", "long", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapLong(.*)$str" => Seq(
          Val(attr, attrClean, "MapLong", "Map[String, Long]", "Long", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneLong", "Long", "K", "long", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapFloat(.*)$str" => Seq(
          Val(attr, attrClean, "MapFloat", "Map[String, Float]", "Float", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneFloat", "Float", "K", "double", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapDouble(.*)$str" => Seq(
          Val(attr, attrClean, "MapDouble", "Map[String, Double]", "Double", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneDouble", "Double", "K", "double", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBigInt(.*)$str" => Seq(
          Val(attr, attrClean, "MapBigInt", "Map[String, BigInt]", "BigInt", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneBigInt", "BigInt", "K", "bigint", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBigDecimal(.*)$str" => Seq(
          Val(attr, attrClean, "MapBigDecimal", "Map[String, BigDecimal]", "BigDecimal", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneBigDecimal", "BigDecimal", "K", "bigdec", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapByte(.*)$str" => Seq(
          Val(attr, attrClean, "MapByte", "Map[String, Byte]", "Byte", "bytes", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneByte", "Byte", "K", "bytes", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBoolean(.*)$str" => Seq(
          Val(attr, attrClean, "MapBoolean", "Map[String, Boolean]", "Boolean", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneBoolean", "Boolean", "K", "boolean", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapDate(.*)$str" => Seq(
          Val(attr, attrClean, "MapDate", "Map[String, Date]", "Date", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneDate", "Date", "K", "instant", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapUUID(.*)$str" => Seq(
          Val(attr, attrClean, "MapUUID", "Map[String, UUID]", "UUID", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneUUID", "UUID", "K", "uuid", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapURI(.*)$str" => Seq(
          Val(attr, attrClean, "MapURI", "Map[String, URI]", "URI", "string", parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0),
          Val(attrK, attrK, "OneURI", "URI", "K", "uri", parseOptions(str, Nil, attr, curFullNs)))

        case r"oneEnum\((.*?)$enums\)(.*)$str"  => Seq(Enum(attr, attrClean, "OneEnum", "String", "", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"manyEnum\((.*?)$enums\)(.*)$str" => Seq(Enum(attr, attrClean, "ManyEnums", "Set[String]", "String", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))


        // Bidirectional edge ref

        case r"oneBiEdge\[(.*)$biRef\](.*)$str" =>
          val (refNs, revRef) = parseBiEdgeRefTypeArg("one", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", refNs, parseOptions(str, Nil, attr, curFullNs) :+ isComponent, Some("BiEdgeRef_"), revRef, attrGroup = attrGroup0))

        case r"manyBiEdge\[(.*)$biRef\](.*)$str" =>
          val (refNs, revRef) = parseBiEdgeRefTypeArg("many", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", refNs, parseOptions(str, Nil, attr, curFullNs) :+ isComponent, Some("BiEdgeRef_"), revRef, attrGroup = attrGroup0))


        // Bidirectional ref

        case r"oneBi\[(.*)$biRef\](.*)$str" =>
          val (refNs, bi, revRef) = parseBiRefTypeArg("one", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", refNs, parseOptions(str, Nil, attr, curFullNs), Some(bi), revRef, attrGroup = attrGroup0))

        case r"manyBi\[(.*)$biRef\](.*)$str" =>
          val (refNs, bi, revRef) = parseBiRefTypeArg("many", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", refNs, parseOptions(str, Nil, attr, curFullNs), Some(bi), revRef, attrGroup = attrGroup0))

        // Bidirectional edge target
        case r"target\[(.*)$biTargetRef\](.*)$str" =>
          val (targetNs, revRef) = parseTargetRefTypeArg(biTargetRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", targetNs, parseOptions(str, Nil, attr, curFullNs), Some("BiTargetRef_"), revRef, attrGroup = attrGroup0))


        // Reference

        case r"one\[(.*)$ref\](.*)$str"  => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", parseRefTypeArg(ref, curPart), parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))
        case r"many\[(.*)$ref\](.*)$str" => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", parseRefTypeArg(ref, curPart), parseOptions(str, Nil, attr, curFullNs), attrGroup = attrGroup0))


        // Missing ref type args

        case r"oneBi(.*)$str" => throw new SchemaDefinitionException(
          s"""Type arg missing for bidirectional ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
             |Please add something like:
             |  val $attr = oneBi[$curNs] // for bidirectional self-reference, or:
             |  val $attr = oneBi[<otherNamespace>.<revRefAttr>.type] // for "outgoing" bidirectional reference to other namespace""".stripMargin)

        case r"manyBi(.*)$str" => throw new SchemaDefinitionException(
          s"""Type arg missing for bidirectional ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
             |Please add something like:
             |  val $attr = manyBi[$curNs] // for bidirectional self-reference, or:
             |  val $attr = manyBi[<otherNamespace>.<revRefAttr>.type] // for "outgoing" bidirectional reference to other namespace""".stripMargin)

        case r"rev(.*)$str" => throw new SchemaDefinitionException(
          s"""Type arg missing for bidirectional reverse ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
             |Please add the namespace where the bidirectional ref pointing to this attribute was defined:
             |  val $attr = rev[<definingNamespace>]""".stripMargin)

        case r"one(.*)$str" => throw new SchemaDefinitionException(
          s"""Type arg missing for ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
             |Please add something like:
             |  val $attr = one[$curNs] // for self-reference, or
             |  val $attr = one[<otherNamespace>] // for ref towards other namespace""".stripMargin)

        case r"many(.*)$str" => throw new SchemaDefinitionException(
          s"""Type arg missing for ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
             |Please add something like:
             |  val $attr = many[$curNs] // for self-reference, or
             |  val $attr = many[<otherNamespace>] // for ref towards other namespace""".stripMargin)

        case unexpected => throw new SchemaDefinitionException(s"Unexpected attribute code in ${defFile.getName}:\n" + unexpected)
      }
    }

    def parseRefTypeArg(refStr: String, curPartition: String = "") = refStr match {
      case r"\w*Definition\.([a-z].*)$partref"  => partref.replace(".", "_")
      case r"([a-z]\w*)$part\.(.*)$ref"         => part + "_" + ref
      case r"(.*)$ref" if curPartition.nonEmpty => curPartition + "_" + ref
      case r"(.*)$ref"                          => ref
    }

    def parseBiEdgeRefTypeArg(card: String, refStr: String, baseAttr: String, basePart: String = "", baseFullNs: String = "") = {

      refStr match {

        // With MyDefinition .......................................

        // val selfRef = oneBi[MyDomainDefinition.ThisPartition.ThisNamespace.selfRef.type]  // or manyBi
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" if s"${part}_$edgeNs" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outRefAttr = oneBi[MyDomainDefinition.ThisPartition.OtherNamespace.revRefAttr.type]  // or manyBi
        // should be only
        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" if part == basePart =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} should have " +
            s"only the namespace prefix in the type argument:\n  val $baseAttr = ${card}Bi[$edgeNs.$targetAttr.type]")

        // val outRefAttr = oneBi[MyDomainDefinition.SomePartition.OtherNamespace.toRefAttr.type]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" => (s"${part}_$edgeNs", targetAttr)


        // With partition .......................................

        // val selfRef = oneBi[ThisPartition.ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" if s"${part}_$edgeNs" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and can't have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val selfRef = oneBi[ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if basePart.nonEmpty && s"${basePart}_$edgeNs" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outgoingRef = oneBi[SomePartition.OtherNamespace.toRefAttr.type]
        case r"([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" => (s"${part}_$edgeNs", targetAttr)


        // With edge ns .......................................

        // val selfRef = oneBi[ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if edgeNs == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outRefAttr = oneBi[OtherNamespace.toRefAttr.type]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if basePart.nonEmpty => (s"${basePart}_$edgeNs", targetAttr)

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" => (edgeNs, targetAttr)

        // Incorrect edge definition
        // val selfRef = oneBi[selfRef.type] // presuming it hasn't been imported from another namespace
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$a\.type" if a == baseAttr =>
          val ns = if (basePart.nonEmpty) baseFullNs.split("_").last else baseFullNs
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and only needs the current namespace as type argument:\n  val $baseAttr = ${card}Bi[$ns]")
      }
    }

    def parseTargetRefTypeArg(refStr: String, baseAttr: String, basePart: String = "", baseFullNs: String = "") = {
      refStr match {

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$targetNs\.(.*)$targetAttr\.type" if basePart.nonEmpty => (s"${basePart}_$targetNs", targetAttr)

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$targetNs\.(.*)$targetAttr\.type" => (targetNs, targetAttr)

        case other =>
          throw new SchemaDefinitionException(
            s"""Target reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} should have a type arg pointing to
               |the attribute that points to this. Something like:
               |  val $baseAttr: AnyRef = target[<baseNs>.<biAttr>.type]
               |(Since this is a recursive definitionn, we need to add a return type)""".stripMargin)
      }
    }

    def parseBiRefTypeArg(card: String, refStr: String, baseAttr: String, basePart: String = "", baseFullNs: String = "") = {
      //            println(s"basePart baseFullNs baseAttr: $basePart      $baseFullNs      $baseAttr")

      refStr match {

        // val selfRef = oneBi[MyDomainDefinition.ThisPartition.ThisNamespace.selfRef.type]  // or manyBi
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$otherNs\.(.*)$targetAttr\.type" if s"${part}_$otherNs" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$otherNs]")

        // val selfRef = oneBi[ThisPartition.ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"([a-z]\w*)$part\.(.*)$otherNs\.(.*)$targetAttr\.type" if s"${part}_$otherNs" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$otherNs]")

        // val selfRef = oneBi[ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$otherNs\.(.*)$targetAttr\.type" if otherNs == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$otherNs]")


        // val otherRef = oneBi[MyDomainDefinition.SomePartition.OtherNamespace.toRefAttr.type]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$otherNs\.(.*)$targetAttr\.type" => (s"${part}_$otherNs", "BiOtherRef_", targetAttr)

        // val otherRef = oneBi[SomePartition.OtherNamespace.revRefAttr.type]
        case r"([a-z]\w*)$part\.(.*)$otherNs\.(.*)$targetAttr\.type" => (s"${part}_$otherNs", "BiOtherRef_", targetAttr)

        // val otherRef = oneBi[OtherNamespace.toRefAttr.type]
        case r"(.*)$otherNs\.(.*)$targetAttr\.type" if basePart.nonEmpty => (s"${basePart}_$otherNs", "BiOtherRef_", targetAttr)

        // val otherRef = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$otherNs\.(.*)$targetAttr\.type" => (otherNs, "BiOtherRef_", targetAttr)


        // val selfRef = oneBi[MyDomainDefinition.ThisPartition.ThisNamespace] // or manyBi
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$selfRef" if s"${part}_$selfRef" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$selfRef]")

        // val selfRef = oneBi[ThisPartition.ThisNamespace]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"([a-z]\w*)$part\.(.*)$selfRef" if s"${part}_$selfRef" == baseFullNs =>
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have partition prefix specified. This is enough:\n  val $baseAttr = ${card}Bi[$selfRef]")

        // val selfRef = oneBi[ThisNamespace]
        case selfNs if basePart.nonEmpty && s"${basePart}_$selfNs" == baseFullNs => (s"${basePart}_$selfNs", "BiSelfRef_", "")

        // val selfRef = oneBi[ThisNamespace]
        case selfNs if selfNs == baseFullNs => (selfNs, "BiSelfRef_", "")

        // val selfRef = oneBi[OtherNamespace]
        case dodgyNs =>
          val part = if (basePart.nonEmpty) s"$basePart." else ""
          throw new SchemaDefinitionException(s"Bidirectional reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is ambiguous. " +
            s"\nPlease choose from one of those 2 options:" +
            s"\n1. Self-reference : val $baseAttr = ${card}Bi[${baseFullNs.replace("_", ".")}]" +
            s"\n2. Other-reference: val $baseAttr = ${card}Bi[$part$dodgyNs.<reverseRefAttr>.type]" +
            s"\nwhere <reverseRefAttr> is a ref in the other namespace that points back to this ref attribute like:" +
            s"\nval reverseRefAttr = ${card}Bi[${baseFullNs.replace("_", ".")}.$baseAttr.type]"
          )
      }
    }

    def someDescr(descr: String) = if (descr.nonEmpty) Some(descr) else None
    def attrCmt(space: Int, comment: String) = (space, comment) match {
      case (0, "")  => None
      case (1, "")  => Some("")
      case (_, cmt) => Some(cmt)
    }

    val definition: Definition = raw.foldLeft(0, "", Definition("", -1, -1, "", "", "", Seq())) {
      case ((s, cmt, d), line) => line.trim match {
        case r"\/\/\s*val .*"                                              => (s, "", d)
        case r"\/\/\s*(.*?)$comment\s*-*"                                  => (s, comment, d)
        case r"package (.*)$pkg\.schema"                                   => (0, "", d.copy(pkg = pkg))
        case "import molecule.schema.definition._"                         => (0, "", d)
        case r"@InOut\((\d+)$inS, (\d+)$outS\)"                            => (0, "", d.copy(in = inS.toString.toInt, out = outS.toString.toInt))
        case r"object\s+([A-Z][a-zA-Z0-9]*)${dmn}Definition \{"            => (0, "", d.copy(domain = dmn))
        case r"object\s+([a-z]\w*)$part\s*\{"                              => (0, "", d.copy(curPart = part, curPartDescr = cmt))
        case r"object\s+(\w*)$part\s*\{"                                   => throw new SchemaDefinitionException(s"Partition name '$part' in ${defFile.getName} should start with a lowercase letter")
        case r"trait\s+([A-Z]\w*)$ns\s*\{" if d.curPart.nonEmpty           => (0, "", d.copy(nss = d.nss :+ Namespace(d.curPart, someDescr(d.curPartDescr), d.curPart + "_" + ns, someDescr(cmt))))
        case r"trait\s+([A-Z]\w*)$ns\s*\{"                                 => (0, "", d.copy(nss = d.nss :+ Namespace("", None, ns, someDescr(cmt))))
        case r"trait\s+(\w*)$ns\s*\{"                                      => throw new SchemaDefinitionException(s"Unexpected namespace name '$ns' in ${defFile.getName}. Namespaces have to start with a capital letter [A-Z].")
        case r"val\s+(\`?)$q1(\w*)$a(\`?)$q2\s*:\s*AnyRef\s*\=\s*(.*)$str" => (0, "", d.addAttr(parseAttr(q1.nonEmpty, a, str, d.curPart, d.nss.last.ns, attrCmt(s, cmt))))
        case r"val\s+(\`?)$q1(\w*)$a(\`?)$q2\s*\=\s*(.*)$str"              => (0, "", d.addAttr(parseAttr(q1.nonEmpty, a, str, d.curPart, d.nss.last.ns, attrCmt(s, cmt))))
        case "}"                                                           => (0, "", d)
        case ""                                                            => (1, cmt, d)
        case r"object .* extends .*"                                       => (0, "", d)
        case unexpected                                                    => throw new SchemaDefinitionException(s"Unexpected definition code in ${defFile.getName}:\n" + unexpected)
      }
    }._3

    definition
  }

  def resolve(definition: Definition) = {
    val updatedNss1 = markBidrectionalEdgeProperties(definition.nss)
    val updatedNss3 = definition.nss.foldLeft(updatedNss1) { case (updatedNss2, curNs) =>
      addBackRefs(updatedNss2, curNs)
    }
    val updatedNss4 = resolveEdgeToOther(updatedNss3)
    definition.copy(nss = updatedNss4)
  }

  def resolveEdgeToOther(nss: Seq[Namespace]): Seq[Namespace] = nss.map { ns =>
    val isBaseEntity = ns.attrs.collectFirst {
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiEdgeRef_"), revRef, _) => true
    } getOrElse false

    if (isBaseEntity) {
      //      println("")
      //      println(s"=============== ${ns.ns} =================")
      val newAttrs: Seq[DefAttr] = ns.attrs.map {
        case biEdgeRefAttr@Ref(attr1, _, _, _, _, _, edgeNs1, _, Some("BiEdgeRef_"), revRef1, _) =>
          //          println("")
          //          println(attr1 + "     -----     " + edgeNs1 + "     -----     " + revRef1)
          nss.collectFirst {
            case Namespace(part2, _, ns2, _, _, attrs2) if part2 == ns.part && ns2 == edgeNs1 =>
              //              println(s"   $ns2 --------------------------------------------------------------------")
              attrs2.collectFirst {
                case ref4@Ref(attr3, _, _, _, _, _, refNs3, _, Some("BiTargetRef_"), revRef3, _) if refNs3 == ns.ns =>
                  //                  println("      " + attr3 + "     -----     " + refNs3 + "     -----     " + revRef3)
                  biEdgeRefAttr.copy(revRef = attr3)
              } getOrElse {
                val baseNs = ns.ns.replace("_", ".")
                throw new SchemaDefinitionException(s"Couldn't find target reference in edge namespace `${edgeNs1.replace("_", ".")}` that points back to `$baseNs.$attr1`. " +
                  s"Expecting something like:\nval ${firstLow(baseNs.split('.').last)} = target[${baseNs.split('.').last}.$attr1.type]")
              }
          } getOrElse {
            val baseNs = ns.ns.replace("_", ".")
            throw new SchemaDefinitionException(s"Couldn't find target reference in edge namespace `${edgeNs1.replace("_", ".")}` that points back to `$baseNs.$attr1`. " +
              s"Expecting something like:\nval ${firstLow(baseNs.split('.').last)} = target[${baseNs.split('.').last}.$attr1.type]")
          }
        case other                                                                               => other
      }
      ns.copy(attrs = newAttrs)
    } else {
      ns
    }
  }

  def markBidrectionalEdgeProperties(nss: Seq[Namespace]): Seq[Namespace] = nss.map { ns =>

    val isEdge = ns.attrs.collectFirst {
      case Ref(_, _, _, _, _, _, _, _, Some("BiTargetRef_"), _, _) => true
    } getOrElse false

    if (isEdge) {
      val newAttrs: Seq[DefAttr] = ns.attrs.map {
        case biEdgeRefAttr@Ref(_, _, _, _, _, _, _, _, Some("BiEdgeRefAttr_"), refRef, _) => biEdgeRefAttr

        case biTargetRef@Ref(_, _, _, _, _, _, _, _, Some("BiTargetRef_"), _, _) => biTargetRef

        case Ref(attr, _, _, _, _, _, _, _, Some(bi), _, _) if bi.substring(6, 10) != "Prop" => throw new SchemaDefinitionException(
          s"""Namespace `${ns.ns}` is already defined as a "property edge" and can't also define a bidirectional reference `$attr`.""")

        case ref: Ref   => ref.copy(bi = Some("BiEdgePropRef_"))
        case enum: Enum => enum.copy(bi = Some("BiEdgePropAttr_"))
        case value: Val => value.copy(bi = Some("BiEdgePropAttr_"))
        case other      => other
      }
      ns.copy(opt = Some(Edge), attrs = newAttrs)
    } else {
      ns
    }
  }

  def addBackRefs(nss: Seq[Namespace], curNs: Namespace): Seq[Namespace] = {
    // Gather OneRefs (ManyRefs are treated as nested data structures)
    val refMap = curNs.attrs.collect {
      case outRef@Ref(_, _, _, _, _, _, refNs, _, _, _, _) => refNs -> outRef
    }.toMap

    nss.map {
      case ns2 if refMap.nonEmpty && refMap.keys.toList.contains(ns2.ns) => {
        val attrs2 = refMap.foldLeft(ns2.attrs) { case (attrs, (refNs, outRef@Ref(_, _, _, _, tpe, _, _, _, _, _, _))) =>
          val cleanNs = if (curNs.ns.contains('_')) curNs.ns.split("_").tail.head else curNs.ns
          // todo: check not to backreference same-named namespaces in different partitions
          curNs.ns match {
            case ns1 if ns1 == ns2.ns => attrs
            case other                => attrs :+ BackRef(s"_$cleanNs", "", "", "", "", "", curNs.ns)
          }
        }.distinct
        ns2.copy(attrs = attrs2)
      }
      case ns2                                                           => ns2
    }
  }


  // Generate ..........................................

  def schemaBody(d: Definition) = {

    def attrStmts(ns: String, a: DefAttr) = {
      val ident = s"""":db/ident"             , ":${firstLow(ns)}/${a.attrClean}""""
      def tpe(t: String) = s"""":db/valueType"         , ":db.type/$t""""
      def card(c: String) = s"""":db/cardinality"       , ":db.cardinality/$c""""
      val stmts = a match {
        case Val(_, _, clazz, _, _, t, options, _, _, _) if clazz.take(3) == "One" => Seq(tpe(t), card("one")) ++ options.map(_.datomicKeyValue)
        case Val(_, _, _, _, _, t, options, _, _, _)                               => Seq(tpe(t), card("many")) ++ options.map(_.datomicKeyValue)
        case a: DefAttr if a.clazz.take(3) == "One"                                => Seq(tpe("ref"), card("one")) ++ a.options.map(_.datomicKeyValue)
        case a: DefAttr                                                            => Seq(tpe("ref"), card("many")) ++ a.options.map(_.datomicKeyValue)
        case unexpected                                                            => throw new SchemaDefinitionException(s"Unexpected attribute statement:\n" + unexpected)
      }
      s"Util.map(${(ident +: stmts).mkString(",\n             ")})"
    }

    def enums(part: String, ns: String, a: String, es: Seq[String]) = {
      val partition = if (part.isEmpty) ":db.part/user" else s":$part"
      es.map(e =>
        s"""Util.map(":db/id", Peer.tempid("$partition"), ":db/ident", ":${firstLow(ns)}.$a/$e")""").mkString(",\n    ")
    }

    // Prepare schema for edge interlink meta data if a property edge is defined
    val (partitions, nss) = {
      val parts = d.nss.map(_.part).filter(_.nonEmpty).distinct
      if (parts.contains("molecule"))
        throw new SchemaDefinitionException("Partition name `molecule` is reserved by Molecule. Please choose another partition name.")
      d.nss.collectFirst {
        case ns if ns.attrs.collectFirst {
          case Ref(_, _, _, _, _, _, _, _, Some("BiTargetRef_"), _, _) => true
        }.getOrElse(false) => {
          val moleculeMetaNs = Namespace("molecule", None, "molecule_Meta", None, None, Seq(
            Ref("otherEdge", "otherEdge", "OneRefAttr", "OneRef", "Long", "", "molecule_Meta", Seq(
              Optional("""":db/index"             , true.asInstanceOf[Object]""", "Indexed"),
              // Is component so that retracts automatically retracts the other edge
              Optional("""":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent")
            ))))
          (parts :+ "molecule", d.nss :+ moleculeMetaNs)
        }
      } getOrElse(parts, d.nss)
    }

    val partitionsList = {
      val ps = partitions.map { p =>
        s"""|Util.map(":db/ident"             , ":$p",
            |             ":db/id"                , Peer.tempid(":db.part/db"),
            |             ":db.install/_partition", ":db.part/db")""".stripMargin
      }
      if (ps.nonEmpty) {
        s"""|
            |  lazy val partitions = Util.list(
            |
            |    ${ps.mkString(",\n\n    ")}
            |  )
            |""".stripMargin
      } else "\n  lazy val partitions = Util.list()\n"
    }

    val stmts = nss map { ns =>
      val exts = ns.opt.getOrElse("").toString
      val header = "\n    // " + ns.ns + exts + " " + ("-" * (65 - (ns.ns.length + exts.length)))
      val attrs = ns.attrs.flatMap { a =>
        val attr = attrStmts(ns.ns, a)
        a match {
          case e: Enum     => Seq(attr, enums(ns.part, ns.ns, a.attrClean, e.enums))
          case br: BackRef => Nil
          case _           => Seq(attr)
        }
      }
      header + "\n\n    " + attrs.mkString(",\n\n    ")
    }

    s"""|/*
        |* AUTO-GENERATED Molecule DSL schema boilerplate code
        |*
        |* To change:
        |* 1. edit schema definition file in `${d.pkg}.schema/`
        |* 2. `sbt compile` in terminal
        |* 3. Refresh and re-compile project in IDE
        |*/
        |package ${d.pkg}.schema
        |import molecule.schema.SchemaTransaction
        |import datomic.{Util, Peer}
        |
        |object ${d.domain}Schema extends SchemaTransaction {
        |  $partitionsList
        |  lazy val namespaces = Util.list(
        |    ${stmts.mkString(",\n    ")}
        |  )
        |}""".stripMargin
  }

  def indexedFirst(opts: Seq[Optional]) = {
    val classes = opts.filter(_.clazz.nonEmpty).map(_.clazz)
    if (classes.contains("Indexed"))
      "Indexed" +: classes.filterNot(_ == "Indexed")
    else
      classes
  }

  def getCrossAttrs(d: Definition) = {
    (for {
      ns <- d.nss
      attr <- ns.attrs
    } yield attr).collect {
      case Ref(_, _, _, _, _, _, edgeNs, _, Some("BiEdgeRef_"), revRef, _)  => s"$edgeNs $revRef"
      case Ref(_, _, _, _, _, _, refNs, _, Some("BiTargetRef_"), revRef, _) => s"$refNs $revRef"
      case Ref(_, _, _, _, _, _, refNs, _, Some("BiOtherRef_"), revRef, _)  => s"$refNs $revRef"
    }.distinct.sorted
  }


  def nsTrait(domain0: String, namesp: Namespace, in: Int, out: Int, maxIn: Int, maxOut: Int, nsArities: Map[String, Int], crossAttrs: Seq[String]) = {
    val (domain, ns, option, attrs) = (firstLow(domain0), namesp.ns, namesp.opt, namesp.attrs)

    val InTypes = (0 until in) map (n => "I" + (n + 1))
    val OutTypes = (0 until out) map (n => (n + 'A').toChar.toString)

    val maxTpe = attrs.filter(!_.attr.startsWith("_")).map(_.tpe.length).max

    val maxAttr = attrs.map(_.attr).filter(!_.startsWith("_")).map(_.length).max
    val maxAttr4 = {
      val lengths = attrs.filter(!_.clazz.contains("Ref")).map(_.attr.length)
      if (lengths.isEmpty) 0 else lengths.max
    }
    val maxClazz = attrs.map(_.clazz.length).max

    val (maxAttrK, maxClazzK, maxTpeK) = {
      val (attrKs, clazzKs, tpeKs) = attrs.map {
        case Val(_, attr, clazz, tpe, "K", _, _, _, _, _) if !attr.startsWith("_") => (Some(attr.length), Some(clazz.length), Some(tpe.length))
        case other                                                                 => (None, None, None)
      }.unzip3
      (
        if (attrKs.flatten.isEmpty) 0 else attrKs.flatten.max,
        if (clazzKs.flatten.isEmpty) 0 else clazzKs.flatten.max,
        if (tpeKs.flatten.isEmpty) 0 else tpeKs.flatten.max
      )
    }

    def exts(a: DefAttr, Ns: String, In: String) = {
      def resolveFS(opts: Seq[String]) = opts.map {
        case "FulltextSearch[Ns, In]" => s"FulltextSearch[$Ns, $In]"
        case other                    => other
      }
      val extensions0 = a match {
        case Val(_, _, _, tpe, _, _, opts, bi, _, _) if tpe.take(3) == "Map" => resolveFS(indexedFirst(opts)) ++ bi.toList
        case Val(_, _, _, _, baseTpe, _, opts, bi, _, _) if baseTpe == "K"   => resolveFS(indexedFirst(opts)) ++ bi.toList :+ "MapAttrK"
        case Val(_, _, _, _, _, _, opts, bi, _, _)                           => resolveFS(indexedFirst(opts)) ++ bi.toList
        case Enum(_, attrClean, _, _, _, _, opts, bi, _, _)                  => indexedFirst(opts) ++ bi.toList :+ s"${attrClean}__enums__"
        case Ref(_, _, _, _, _, _, refNs, opts, bi, revRef, _)               => indexedFirst(opts) ++ (bi match {
          case Some("BiSelfRef_")     => Seq(s"BiSelfRefAttr_")
          case Some("BiOtherRef_")    => Seq(s"BiOtherRefAttr_[$domain.$refNs.$revRef]")
          case Some("BiEdgeRef_")     => Seq(s"BiEdgeRefAttr_[$domain.$refNs.$revRef]")
          case Some("BiEdgePropAttr") => Seq(s"BiEdgePropAttr_")
          case Some("BiEdgePropRef_") => Seq(s"BiEdgePropRefAttr_")
          case Some("BiTargetRef_")   => Seq(s"BiTargetRefAttr_[$domain.$refNs.$revRef]")
          case other                  => Nil
        })
      }
      if (extensions0.isEmpty) "" else " with " + extensions0.mkString(" with ")
    }

    val (attrVals0, attrVals0_, attrValsK0, attrValsK0_) = attrs.flatMap {
      case BackRef(_, _, _, _, _, _, _, _, _) => None
      case a                                  => {
        val (attr, attrClean, clazz, tpe, opts) = (a.attr, a.attrClean, a.clazz, a.tpe, a.options)
        val p1 = padS(maxAttr, attr)
        val p2 = padS(maxAttr, attrClean)
        val p3 = padS(maxTpe, tpe)
        //        val p4 = padS(maxAttr4, attr)
        val p5 = padS(maxAttrK, attr)
        //        val p6 = padS(maxClazzK, clazz)
        val p7 = padS(maxTpeK, tpe)
        val p8 = padS(maxClazz, clazz)


        val (nextNS, nextNSK, nextNSShort, thisNS) = (in, out) match {
          case (0, 0) => (
            s"${ns}_1[$tpe$p3]",
            s"${ns}_1[$tpe$p7]",
            s"${ns}_1[$tpe]",
            s"${ns}_0")

          case (0, o) => (
            s"${ns}_${o + 1}[${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_${o + 1}[${(OutTypes :+ tpe) mkString ", "}$p7]",
            s"${ns}_${o + 1}[${(OutTypes :+ tpe) mkString ", "}]",
            s"${ns}_$o[${OutTypes mkString ", "}]")

          case (i, o) => (
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ tpe) mkString ", "}$p7]",
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ tpe) mkString ", "}]",
            s"${ns}_In_${i}_$o[${(InTypes ++ OutTypes) mkString ", "}]")
        }

        val (nextIn, nextInK, nextInShort, thisIn) = if (maxIn == 0 || in == maxIn) {
          val (n1, n2) = (out + in + 1, out + in + 2)
          val (t1, t2) = ((1 to n1).map(i => "_").mkString(","), (1 to n2).map(i => "_").mkString(","))
          (s"P$n2[$t2]", s"P$n2[$t2]", s"P$n2[$t2]", s"P$n1[$t1]")
        } else (in, out) match {
          case (0, 0) => (
            s"${ns}_In_1_1[$tpe$p3, $tpe$p3]",
            s"${ns}_In_1_1[$tpe$p7, $tpe$p7]",
            s"${ns}_In_1_1[$tpe, $tpe]",
            s"${ns}_In_1_0[$tpe$p3]")

          case (0, o) => (
            s"${ns}_In_1_${o + 1}[$tpe$p3, ${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_1_${o + 1}[$tpe$p7, ${(OutTypes :+ tpe) mkString ", "}$p7]",
            s"${ns}_In_1_${o + 1}[$tpe, ${(OutTypes :+ tpe) mkString ", "}]",
            s"${ns}_In_1_$o[$tpe$p3, ${OutTypes mkString ", "}]")

          case (i, 0) => (
            s"${ns}_In_${i + 1}_1[${(InTypes :+ tpe) mkString ", "}$p3, $tpe$p3]",
            s"${ns}_In_${i + 1}_1[${(InTypes :+ tpe) mkString ", "}$p7, $tpe$p7]",
            s"${ns}_In_${i + 1}_1[${(InTypes :+ tpe) mkString ", "}, $tpe]",
            s"${ns}_In_${i + 1}_0[${(InTypes :+ tpe) mkString ", "}$p3]")

          case (i, o) => (
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ tpe) mkString ", "}$p3, ${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ tpe) mkString ", "}$p7, ${(OutTypes :+ tpe) mkString ", "}$p7]",
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ tpe) mkString ", "}, ${(OutTypes :+ tpe) mkString ", "}]",
            s"${ns}_In_${i + 1}_$o[${(InTypes :+ tpe) mkString ", "}$p3, ${OutTypes mkString ", "}]")
        }

        val (thisInK, thisInShort) = if (maxIn == 0 || in == maxIn) {
          val n1 = out + in + 1
          val t1 = (1 to n1).map(i => "_").mkString(",")
          (s"P$n1[$t1]", s"P$n1[$t1]")
        } else (in, out) match {
          case (0, 0) => (
            s"${ns}_In_1_0[$tpe$p7]",
            s"${ns}_In_1_0[$tpe]")
          case (0, o) => (
            s"${ns}_In_1_$o[$tpe$p7, ${OutTypes mkString ", "}]",
            s"${ns}_In_1_$o[$tpe, ${OutTypes mkString ", "}]")
          case (i, 0) => (
            s"${ns}_In_${i + 1}_0[${(InTypes :+ tpe) mkString ", "}$p7]",
            s"${ns}_In_${i + 1}_0[${(InTypes :+ tpe) mkString ", "}]")
          case (i, o) => (
            s"${ns}_In_${i + 1}_$o[${(InTypes :+ tpe) mkString ", "}$p7, ${OutTypes mkString ", "}]",
            s"${ns}_In_${i + 1}_$o[${(InTypes :+ tpe) mkString ", "}, ${OutTypes mkString ", "}]")
        }

        val (attrVal, attrValK) = a match {
          case _ if a.baseTpe == "K" =>
            (None, Some(s"""override def $attr$p5(key: String): $nextNSK with $clazz$p8[$nextNSK, $nextInK]${exts(a, nextNSShort, nextInShort)} = ???"""))
          case _                     =>
            (Some(s"""override def $attr $p1 : $nextNS with $clazz$p8[$nextNS, $nextIn]${exts(a, nextNSShort, nextInShort)} = ???"""), None)
        }

        val (attrVal_, attrValK_) = a match {
          case _: Val if a.baseTpe == "K" =>
            (None, Some(s"override def ${attrClean}_$p5(key: String): $thisNS with $clazz$p8[$thisNS, $thisInK]${exts(a, thisNS, thisInShort)} = ???"))
          case _                          =>
            (Some(s"override def ${attrClean}_$p2 : $thisNS with $clazz$p8[$thisNS, $thisIn]${exts(a, thisNS, thisInShort)} = ???"), None)
        }

        Some(List(attrVal, attrVal_, attrValK, attrValK_))
      }
    }.foldLeft(Seq.empty[Option[String]], Seq.empty[Option[String]], Seq.empty[Option[String]], Seq.empty[Option[String]]) {
      case ((aa, bb, cc, dd), a :: b :: c :: d :: Nil) => (aa :+ a, bb :+ b, cc :+ c, dd :+ d)
      case ((aa, bb, cc, dd), other)                   => (aa, bb, cc, dd)
    }

    // Group mandatory and tacit, each in standard/keyed subgroups
    val (attrValsK0flat, attrValsK0flat_) = (attrValsK0.flatten, attrValsK0_.flatten)
    val attrVals = if (attrValsK0flat.isEmpty) attrVals0.flatten else attrVals0.flatten ++ Seq("") ++ attrValsK0flat
    val attrVals_ = if (attrValsK0flat_.isEmpty) attrVals0_.flatten else attrVals0_.flatten ++ Seq("") ++ attrValsK0flat_

    val attrValsOpt = attrs.flatMap {
      case BackRef(_, _, _, _, _, _, _, _, _) => None
      case a                                  => {
        val (attr, attrClean, clazz, tpe) = (a.attr, a.attrClean, a.clazz, a.tpe)
        val p2 = padS(maxAttr, attrClean)
        val p3 = padS(maxTpe, tpe)
        val p8 = padS(maxClazz, clazz)

        val (nextNS, nextNSShort) = (in, out) match {
          case (0, 0) => (
            s"${ns}_1[Option[$tpe]$p3]",
            s"${ns}_1[Option[$tpe]]")
          case (0, o) => (
            s"${ns}_${o + 1}[${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]",
            s"${ns}_${o + 1}[${(OutTypes :+ s"Option[$tpe]") mkString ", "}]")
          case (i, o) => (
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]",
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ s"Option[$tpe]") mkString ", "}]")
        }
        val (nextIn, nextInShort) = if (maxIn == 0 || in == maxIn) {
          val n2 = out + in + 2
          val t2 = (1 to n2).map(i => "_").mkString(",")
          (s"P$n2[$t2]", s"P$n2[$t2]")
        } else (in, out) match {
          case (0, 0) => (
            s"${ns}_In_1_1[Option[$tpe]$p3, Option[$tpe]$p3]",
            s"${ns}_In_1_1[Option[$tpe], Option[$tpe]]")
          case (0, o) => (
            s"${ns}_In_1_${o + 1}[Option[$tpe]$p3, ${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]",
            s"${ns}_In_1_${o + 1}[Option[$tpe], ${(OutTypes :+ s"Option[$tpe]") mkString ", "}]"
          )
          case (i, 0) => (
            s"${ns}_In_${i + 1}_1[${(InTypes :+ s"Option[$tpe]") mkString ", "}$p3, Option[$tpe]$p3]",
            s"${ns}_In_${i + 1}_1[${(InTypes :+ s"Option[$tpe]") mkString ", "}, Option[$tpe]]")
          case (i, o) => (
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ s"Option[$tpe]") mkString ", "}$p3, ${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]",
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ s"Option[$tpe]") mkString ", "}, ${(OutTypes :+ s"Option[$tpe]") mkString ", "}]")
        }

        a match {
          case a: Val if a.baseTpe == "K" => None
          case a                          => Some(s"override def $attrClean$$$p2 : $nextNS with $clazz$$$p8[$nextNS]${exts(a, nextNSShort, nextInShort)} = ???")
        }
      }
    }

    val (maxClazz2, maxRefNs, maxNs) = attrs.map {
      case Ref(_, ns, _, clazz2, _, _, refNs, _, _, _, _)   => (clazz2.length, refNs.length, ns.length)
      case BackRef(backRef, _, _, _, _, _, backRefNs, _, _) => (backRef.length, backRefNs.length, 0)
      case other                                            => (0, 0, 0)
    }.unzip3

    val maxAttr0 = attrs.map(_.attrClean.length).max

    val bidirectionals: Map[String, String] = attrs.flatMap {
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiSelfRef_"), revRef, _)     => Some(attr -> s" with BiSelfRef_")
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiOtherRef_"), revRef, _)    => Some(attr -> s" with BiOtherRef_[$domain.$refNs.$revRef]")
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiEdgePropRef_"), revRef, _) => Some(attr -> s" with BiEdgePropRef_")
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiEdgeRef_"), revRef, _)     => Some(attr -> s" with BiEdgeRef_[$domain.$refNs.$revRef]")
      case Ref(attr, _, _, _, _, _, refNs, _, Some("BiTargetRef_"), revRef, _)   => Some(attr -> s" with BiTargetRef_[$domain.$refNs.$revRef]")
      case other                                                                 => None
    }.toMap
    val maxBidirectionals = bidirectionals.values.map(_.length)

    val refCode = attrs.foldLeft(Seq("")) {
      case (acc, Ref(attr, attrClean, _, clazz2, _, baseTpe, refNs, opts, _, _, _)) => {
        val p1 = padS(maxAttr0 + 1, attrClean)
        val p2 = padS("ManyRef".length, clazz2)
        val p3 = padS(maxRefNs.max, refNs)
        val p4 = padS(maxNs.max, attrClean)

        val bidirectional = if (bidirectionals.nonEmpty && bidirectionals.contains(attr))
          bidirectionals(attr) + padS(maxBidirectionals.max, bidirectionals(attr))
        else ""

        val ref = (in, out) match {
          case (0, 0) if baseTpe.isEmpty                => s"${refNs}_0$p3$bidirectional"
          case (0, 0) if maxIn == 3                     => s"${refNs}_0$p3$bidirectional with Nested00[${refNs}_1$p3, ${refNs}_In_1_1$p3, ${refNs}_In_2_1$p3, ${refNs}_In_3_1$p3]"
          case (0, 0) if maxIn == 2                     => s"${refNs}_0$p3$bidirectional with Nested00[${refNs}_1$p3, ${refNs}_In_1_1$p3, ${refNs}_In_2_1$p3, P4]"
          case (0, 0) if maxIn == 1                     => s"${refNs}_0$p3$bidirectional with Nested00[${refNs}_1$p3, ${refNs}_In_1_1$p3, P3, P4]"
          case (0, 0) /* maxIn == 0 */                  => s"${refNs}_0$p3$bidirectional with Nested00[${refNs}_1$p3, P2, P3, P4]"
          case (0, o) if baseTpe.isEmpty || o == maxOut => s"${refNs}_$o$p3[${OutTypes mkString ", "}]$bidirectional"
          case (0, o) if maxIn == 3                     => s"${refNs}_$o$p3[${OutTypes mkString ", "}]$bidirectional with Nested${padI(o)}[${refNs}_${o + 1}$p3, ${refNs}_In_1_${o + 1}$p3, ${refNs}_In_2_${o + 1}$p3, ${refNs}_In_3_${o + 1}$p3, ${OutTypes mkString ", "}]"
          case (0, o) if maxIn == 2                     => s"${refNs}_$o$p3[${OutTypes mkString ", "}]$bidirectional with Nested${padI(o)}[${refNs}_${o + 1}$p3, ${refNs}_In_1_${o + 1}$p3, ${refNs}_In_2_${o + 1}$p3, P${o + 4}, ${OutTypes mkString ", "}]"
          case (0, o) if maxIn == 1                     => s"${refNs}_$o$p3[${OutTypes mkString ", "}]$bidirectional with Nested${padI(o)}[${refNs}_${o + 1}$p3, ${refNs}_In_1_${o + 1}$p3, P${o + 3}, P${o + 4}, ${OutTypes mkString ", "}]"
          case (0, o) /* maxIn == 0 */                  => s"${refNs}_$o$p3[${OutTypes mkString ", "}]$bidirectional with Nested${padI(o)}[${refNs}_${o + 1}$p3, P${o + 2}, P${o + 3}, P${o + 4}, ${OutTypes mkString ", "}]"

          case (1, 0) if baseTpe.isEmpty                => s"${refNs}_In_1_0$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (1, 0) if maxIn == 3                     => s"${refNs}_In_1_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_1_00[${refNs}_In_1_1$p3, ${refNs}_In_2_1$p3, ${refNs}_In_3_1$p3, ${InTypes mkString ", "}]"
          case (1, 0) if maxIn == 2                     => s"${refNs}_In_1_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_1_00[${refNs}_In_1_1$p3, ${refNs}_In_2_1$p3, P4, ${InTypes mkString ", "}]"
          case (1, 0) /* maxIn == 1 */                  => s"${refNs}_In_1_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_1_00[${refNs}_In_1_1$p3, P3, P4, ${InTypes mkString ", "}]"
          case (1, o) if baseTpe.isEmpty || o == maxOut => s"${refNs}_In_1_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (1, o) if maxIn == 3                     => s"${refNs}_In_1_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_1_${padI(o)}[${refNs}_In_1_${o + 1}$p3, ${refNs}_In_2_${o + 1}$p3, ${refNs}_In_3_${o + 1}$p3, ${(InTypes ++ OutTypes) mkString ", "}]"
          case (1, o) if maxIn == 2                     => s"${refNs}_In_1_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_1_${padI(o)}[${refNs}_In_1_${o + 1}$p3, ${refNs}_In_2_${o + 1}$p3, P${o + 4}, ${(InTypes ++ OutTypes) mkString ", "}]"
          case (1, o) /* maxIn == 1 */                  => s"${refNs}_In_1_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_1_${padI(o)}[${refNs}_In_1_${o + 1}$p3, P${o + 3}, P${o + 4}, ${(InTypes ++ OutTypes) mkString ", "}]"

          case (2, 0) if baseTpe.isEmpty                => s"${refNs}_In_2_0$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (2, 0) if maxIn == 3                     => s"${refNs}_In_2_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_2_00[${refNs}_In_2_1$p3, ${refNs}_In_3_1$p3, ${InTypes mkString ", "}]"
          case (2, 0) /* maxIn == 2 */                  => s"${refNs}_In_2_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_2_00[${refNs}_In_2_1$p3, P4, ${InTypes mkString ", "}]"
          case (2, o) if baseTpe.isEmpty || o == maxOut => s"${refNs}_In_2_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (2, o) if maxIn == 3                     => s"${refNs}_In_2_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_2_${padI(o)}[${refNs}_In_2_${o + 1}$p3, ${refNs}_In_3_${o + 1}$p3, ${(InTypes ++ OutTypes) mkString ", "}]"
          case (2, o) /* maxIn == 2 */                  => s"${refNs}_In_2_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_2_${padI(o)}[${refNs}_In_2_${o + 1}$p3, P${o + 4}, ${(InTypes ++ OutTypes) mkString ", "}]"

          case (3, 0) if baseTpe.isEmpty                => s"${refNs}_In_3_0$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (3, 0) /* maxIn == 3 */                  => s"${refNs}_In_3_0$p3[${InTypes mkString ", "}]$bidirectional with Nested_In_3_00[${refNs}_In_3_1$p3, ${InTypes mkString ", "}]"
          case (3, o) if baseTpe.isEmpty || o == maxOut => s"${refNs}_In_3_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional"
          case (3, o) /* maxIn == 3 */                  => s"${refNs}_In_3_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]$bidirectional with Nested_In_3_${padI(o)}[${refNs}_In_3_${o + 1}$p3, ${(InTypes ++ OutTypes) mkString ", "}]"
        }
        acc :+ s"object ${attrClean.capitalize} $p4 extends ${attrClean.capitalize}__$p4[$ns, $refNs$p3] with $ref"
      }
      case (acc, BackRef(backRef, _, _, _, _, _, backRefNs, _, _))               =>
        val p1 = padS(maxAttr0, backRef)
        val p2 = padS(maxClazz2.max, backRefNs)
        val backRefNsTyped = (in, out) match {
          case (0, 0) => s"${backRefNs}_0$p2"
          case (0, o) => s"${backRefNs}_$o$p2[${OutTypes mkString ", "}]"
          case (i, o) => s"${backRefNs}_In_${i}_$o$p2[${(InTypes ++ OutTypes) mkString ", "}]"
        }
        acc :+ s"object $backRef$p1 extends $backRef$p1 with $backRefNsTyped"
      case (acc, _)                                                                 => acc
    }.distinct

    (in, out) match {
      // First output trait
      case (0, 0) =>
        val (thisIn, nextIn) = if (maxIn == 0 || in == maxIn) ("P" + (out + in + 1), "P" + (out + in + 2)) else (s"${ns}_In_1_0", s"${ns}_In_1_1")
        s"""trait ${ns}_0 extends $ns with Out_0[${ns}_0, ${ns}_1, $thisIn, $nextIn] {
           |  ${(attrVals ++ Seq("") ++ attrValsOpt ++ Seq("") ++ attrVals_ ++ refCode).mkString("\n  ").trim}
           |}
         """.stripMargin

      // Last output trait
      case (0, o) if o == maxOut =>
        val thisIn = if (maxIn == 0 || in == maxIn) "P" + (out + in + 1) else s"${ns}_In_1_$o"
        val types = OutTypes mkString ", "
        s"""trait ${ns}_$o[$types] extends $ns with Out_$o[${ns}_$o, P${out + in + 1}, $thisIn, P${out + in + 2}, $types] {
           |  ${(attrVals_ ++ refCode).mkString("\n  ").trim}
           |}""".stripMargin

      // Other output traits
      case (0, o) =>
        val (thisIn, nextIn) = if (maxIn == 0 || in == maxIn) ("P" + (out + in + 1), "P" + (out + in + 2)) else (s"${ns}_In_1_$o", s"${ns}_In_1_${o + 1}")
        val types = OutTypes mkString ", "
        s"""trait ${ns}_$o[$types] extends $ns with Out_$o[${ns}_$o, ${ns}_${o + 1}, $thisIn, $nextIn, $types] {
           |  ${(attrVals ++ Seq("") ++ attrValsOpt ++ Seq("") ++ attrVals_ ++ refCode).mkString("\n  ").trim}
           |
           |  object Self extends Self with ${ns}_$o[$types]
           |}
         """.stripMargin


      // First input trait
      case (i, 0) =>
        val s = if (in > 1) "s" else ""
        val (thisIn, nextIn) = if (maxIn == 0 || in == maxIn) ("P" + (out + in + 1), "P" + (out + in + 2)) else (s"${ns}_In_${i + 1}_0", s"${ns}_In_${i + 1}_1")
        val types = InTypes mkString ", "
        s"""
           |
           |/********* Input molecules awaiting $i input$s *******************************/
           |
           |trait ${ns}_In_${i}_0[$types] extends $ns with In_${i}_0[${ns}_In_${i}_0, ${ns}_In_${i}_1, $thisIn, $nextIn, $types] {
           |  ${(attrVals ++ Seq("") ++ attrValsOpt ++ Seq("") ++ attrVals_ ++ refCode).mkString("\n  ").trim}
           |}
         """.stripMargin

      // Last input trait
      case (i, o) if i <= maxIn && o == maxOut =>
        val thisIn = if (maxIn == 0 || i == maxIn) "P" + (out + in + 1) else s"${ns}_In_${i + 1}_$o"
        val types = (InTypes ++ OutTypes) mkString ", "
        s"""trait ${ns}_In_${i}_$o[$types] extends $ns with In_${i}_$o[${ns}_In_${i}_$o, P${out + in + 1}, $thisIn, P${out + in + 2}, $types] {
           |  ${(attrVals_ ++ refCode).mkString("\n  ").trim}
           |}""".stripMargin

      // Max input traits
      case (i, o) if i == maxIn =>
        val types = (InTypes ++ OutTypes) mkString ", "
        s"""trait ${ns}_In_${i}_$o[$types] extends $ns with In_${i}_$o[${ns}_In_${i}_$o, ${ns}_In_${i}_${o + 1}, P${out + in + 1}, P${out + in + 2}, $types] {
           |  ${(attrVals ++ Seq("") ++ attrValsOpt ++ Seq("") ++ attrVals_ ++ refCode).mkString("\n  ").trim}
           |}
         """.stripMargin

      // Other input traits
      case (i, o) =>
        val (thisIn, nextIn) = if (i == maxIn) ("P" + (out + in + 1), "P" + (out + in + 2)) else (s"${ns}_In_${i + 1}_$o", s"${ns}_In_${i + 1}_${o + 1}")
        val types = (InTypes ++ OutTypes) mkString ", "
        s"""trait ${ns}_In_${i}_$o[$types] extends $ns with In_${i}_$o[${ns}_In_${i}_$o, ${ns}_In_${i}_${o + 1}, $thisIn, $nextIn, $types] {
           |  ${(attrVals ++ Seq("") ++ attrValsOpt ++ Seq("") ++ attrVals_ ++ refCode).mkString("\n  ").trim}
           |
           |  object Self extends Self with ${ns}_In_${i}_$o[$types]
           |}
         """.stripMargin
    }
  }


  // doc helpers ------------------------------------------------------------------------------

  def mkAttr(attr: String) = if (attr.contains('`')) s"'''`'${attr.tail.init}'`'''" else s"'''`$attr`'''"
  def card(implicit many: Boolean) = if (many) "many" else "one"
  def value(implicit many: Boolean) = if (many) "values" else "a value"
  def aValue(implicit many: Boolean) = if (many) s"values" else s"a value"
  def is(implicit many: Boolean) = if (many) "are" else "is"
  def has(implicit many: Boolean) = if (many) "have" else "has"


  def resolveNs(d: Definition, namespace: Namespace, crossAttrs: Seq[String]) = {
    val domain = firstLow(d.domain)
    val inArity = d.in
    val outArity = d.out
    val Ns = namespace.ns
    val attrs = namespace.attrs
    val ext = namespace.opt match {
      case Some(Edge) => "extends BiEdge_ "
      case _          => ""
    }
    val p1 = (s: String) => padS(attrs.map(_.attr).filter(!_.startsWith("_")).map(_.length).max, s)
    val p2 = (s: String) => padS(attrs.map(_.clazz).filter(!_.startsWith("Back")).map(_.length).max, s)


    def doc(opts: Seq[Optional]) = opts.collectFirst {
      case Optional(kv, _) if kv.startsWith("""":db/doc"""") =>
        val doc = kv.substring(27).init
        s"""== $doc ==
           |    * """.stripMargin
    }.getOrElse("")


    val attrClasses0 = attrs.map {

      case Val(attr, attrClean, clazz, tpe, baseTpe, _, opts, bi, _, _) if tpe.take(3) == "Map" =>
        val extensions0 = indexedFirst(opts) ++ bi.toList
        val extensions = if (extensions0.isEmpty) "" else " with " + extensions0.mkString(" with ")
        implicit val isMany = baseTpe.nonEmpty
        val attr1 = mkAttr(attr)
        s"""
           |  // $attr -----------------------------------------------------------------------------------
           |
           |  /** ${doc(opts)}Adds mandatory Map attribute $attr1 of type `$tpe` to the molecule.
           |    * <br><br>
           |    * Semantics of $attr1 being mandatory:
           |    * <br>- If molecule is queried, only entities having $attr1 value pairs asserted will be returned.
           |    * <br>- If insert, save or update is executed on the molecule, the mandatory $attr1 attribute
           |    * needs a Map of key/value(s) applied.
           |    *
           |    * @note Internally, Molecule maps all pairs of the Map back and forth to individual datoms of
           |    * a cardinality many attribute $attr1. For the end user the $attr1 attribute is treated as a
           |    * cardinality one value of type $tpe.
           |    */
           |  def $attr: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds optional Map attribute $attr1 of type `$tpe` to the molecule..
           |    * <br><br>
           |    * Semantics of $attr1 being optional:
           |    * <br>- If molecule is queried, $attr1 value pairs asserted with entities matching the molecule are returned as
           |    * Some(map: $tpe), otherwise None. Similar to allowing null values.
           |    * <br>- If insert, save or update is executed on the molecule, Some(map: $tpe) or None can
           |    * be applied to $attr1.
           |    */
           |  def $attrClean$$: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds tacit Map attribute $attr1 of type `$tpe` to the molecule.
           |    * <br><br>
           |    * Semantics of $attr1 being tacit:
           |    * <br>- If molecule is queried, only entities having $attr1 values asserted will match,
           |    * but $attr1 values are not returned in the result set (are 'tacit').
           |    */
           |  def ${attrClean}_ : AnyRef = ???
           |  """.stripMargin


      case Val(attr, attrClean, clazz, tpe, baseTpe, datomicTpe, opts, bi, revRef, _) if baseTpe == "K" =>
        val extensions0 = indexedFirst(opts) ++ bi.toList :+ "MapAttrK"
        val extensions = " with " + extensions0.mkString(" with ")
        implicit val isMany = baseTpe.nonEmpty
        val attr1 = mkAttr(attr)
        s"""
           |  // $attr -----------------------------------------------------------------------------------
           |
           |  /** ${doc(opts)}Adds mandatory "keyed Map attribute" $attr1 to the molecule.
           |    * <br>$attr1 is automatically generated from the base attribute  '''`${attr.init}`'''.
           |    * <br><br>
           |    * Given a String key, it returns the associated '''`${attr.init}`''' value of type `$tpe` while still allowing us to continue adding
           |    * further attributes to the molecule:
           |    * {{{
           |    *   $Ns.$attr("someKey").otherAttr.get === List((<$attr-value>, <otherAttr-value>), ...)
           |    * }}}
           |    */
           |  def $attr(key: String): AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds tacit "keyed Map attribute" $attr1 to the molecule.
           |    * <br>$attr1 is automatically generated from the base attribute  '''`${attr.init}`'''.
           |    * <br><br>
           |    * Given a String key, it ensures that the associated '''`${attr.init}`''' value has been asserted while still allowing us to continue adding
           |    * further attributes to the molecule. Being tacit means that the value is not returned as part of query results:
           |    * {{{
           |    *   // $attr values ommitted from result (are tacit)
           |    *   $Ns.otherAttr.$attr("someKey").get === List(<otherAttr-value>)
           |    * }}}
           |    */
           |  def ${attrClean}_(key: String): AnyRef = ???
           |  """.stripMargin


      case Val(attr, attrClean, clazz, tpe, baseTpe, _, opts, bi, revRef, _) =>
        val extensions0 = indexedFirst(opts) ++ bi.toList
        val extensions = if (extensions0.isEmpty) "" else " with " + extensions0.mkString(" with ")
        implicit val isMany = baseTpe.nonEmpty
        val attr1 = mkAttr(attr)
        s"""
           |  // $attr -----------------------------------------------------------------------------------
           |
           |  /** ${doc(opts)}Adds mandatory cardinality-$card attribute $attr1 of type `$tpe` to the molecule.
           |    * <br><br>
           |    * Semantics of $attr1 being mandatory:
           |    * <br>- If molecule is queried, only entities having $aValue for $attr1 will be returned.
           |    * <br>- If insert, save or update is executed on the molecule, the mandatory $attr1 attribute
           |    * needs value(s) applied.
           |    */
           |  def $attr: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds optional cardinality-$card attribute $attr1 of type `$tpe` to the molecule.
           |    * <br><br>
           |    * Semantics of $attr1 being optional:
           |    * <br>- If molecule is queried, $attr1 values asserted with entities matching the molecule are returned as
           |    * Some(value), otherwise None. Similar to allowing null values.
           |    * <br>- If insert, save or update is executed on the molecule, Some(value(s)) or None can
           |    * be applied to $attr1.
           |    */
           |  def $attrClean$$: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds tacit cardinality-$card attribute $attr1 of type `$tpe` to the molecule.
           |    * <br><br>
           |    * Semantics of $attr1 being tacit:
           |    * <br>- If molecule is queried, only entities having $attr1 values asserted will match,
           |    * but $attr1 values are not returned in the result set (are 'tacit').
           |    */
           |  def ${attrClean}_ : AnyRef = ???
           |  """.stripMargin


      case Enum(attr, attrClean, clazz, tpe, baseTpe, enums, opts, bi, revRef, _) =>
        val extensions0 = indexedFirst(opts) ++ bi.toList
        val extensions = if (extensions0.isEmpty) "" else " with " + extensions0.mkString(" with ")
        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
        implicit val isMany = baseTpe.nonEmpty
        val attr1 = mkAttr(attr)
        s"""
           |  // $attr -----------------------------------------------------------------------------------
           |
           |  trait ${attrClean}__enums__{ $enumValues }
           |
           |  /** ${doc(opts)}Adds mandatory cardinality-$card enum attribute $attr1 of type String to the molecule.
           |    * <br><br>
           |    * Enums available:
           |    * <br> - ${enums.mkString("\n    * <br> - ")}
           |    * <br><br>
           |    * Semantics of $attr1 being mandatory:
           |    * <br>- If molecule is queried, only entities having $aValue for $attr1 will be returned.
           |    * <br>- If insert, save or update is executed on the molecule, the mandatory $attr1 attribute
           |    * needs value(s) applied.
           |    */
           |  def $attr: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds optional cardinality-$card enum attribute $attr1 of type String to the molecule.
           |    * <br><br>
           |    * Enums available:
           |    * <br> - ${enums.mkString("\n    * <br> - ")}
           |    * <br><br>
           |    * Semantics of $attr1 being optional:
           |    * <br>- If molecule is queried, $attr1 values asserted with entities matching the molecule are returned as
           |    * Some(value), otherwise None. Similar to allowing null values.
           |    * <br>- If insert, save or update is executed on the molecule, Some(value(s)) or None can
           |    * be applied to $attr1.
           |    */
           |  def $attrClean$$: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds tacit cardinality-$card enum attribute $attr1 of type String to the molecule.
           |    * <br><br>
           |    * Enums available:
           |    * <br> - ${enums.mkString("\n    * <br> - ")}
           |    * <br><br>
           |    * Semantics of $attr1 being tacit:
           |    * <br>- If molecule is queried, only entities having $attr1 values asserted will match,
           |    * but $attr1 values are not returned in the result set (are 'tacit').
           |    */
           |  def ${attrClean}_ : AnyRef = ???
           |  """.stripMargin


      case Ref(attr, attrClean, clazz, clazz2, tpe, baseTpe, refNs, opts, bi, revRef, _) =>
        val extensions0 = indexedFirst(opts) ++ (bi match {
          case Some("BiSelfRef_")     => Seq(s"BiSelfRefAttr_")
          case Some("BiOtherRef_")    => Seq(s"BiOtherRefAttr_[$domain.$refNs.$revRef[NS, NS]]")
          case Some("BiEdgeRef_")     => Seq(s"BiEdgeRefAttr_[$domain.$refNs.$revRef[NS, NS]]")
          case Some("BiEdgePropAttr") => Seq(s"BiEdgePropAttr_")
          case Some("BiEdgePropRef_") => Seq(s"BiEdgePropRefAttr_")
          case Some("BiTargetRef_")   => Seq(s"BiTargetRefAttr_[$domain.$refNs.$revRef[NS, NS]]")
          case other                  => Nil
        })
        val extensions = if (extensions0.isEmpty) "" else " with " + extensions0.mkString(" with ")
        implicit val isMany = baseTpe.nonEmpty
        val attr1 = mkAttr(attr)

        val crossTypeDef = if (crossAttrs.contains(s"$Ns $attr")) {
          s"""
             |  // Internal type to connect BiEdges at compile time
             |  type $attr
           """.stripMargin
        } else ""

        s"""
           |  // $attr -----------------------------------------------------------------------------------
           |
           |  /** ${doc(opts)}Adds mandatory cardinality-$card ref attribute $attr1 of type `Long` to the molecule.
           |    * <br>Ref values point to entities with attributes in namespace `$refNs` (and possibly other namespaces).
           |    * <br><br>
           |    * Semantics of $attr1 being mandatory:
           |    * <br>- If molecule is queried, only entities having $aValue for $attr1 will be returned.
           |    * <br>- If insert, save or update is executed on the molecule, the mandatory $attr1 attribute
           |    * needs value(s) applied.
           |    */
           |  def $attr: AnyRef = ???
           |  $crossTypeDef
           |
           |  /** ${doc(opts)}Adds optional cardinality-$card ref attribute $attr1 of type `Long` to the molecule.
           |    * <br>Ref values point to entities with attributes in namespace `$refNs` (and possibly other namespaces).
           |    * <br><br>
           |    * Semantics of $attr1 being optional:
           |    * <br>- If molecule is queried, $attr1 values asserted with entities matching the molecule are returned as
           |    * Some(value), otherwise None. Similar to allowing null values.
           |    * <br>- If insert, save or update is executed on the molecule, Some(value(s)) or None can
           |    * be applied to $attr1.
           |    */
           |  def $attrClean$$: AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds tacit cardinality-$card ref attribute $attr1 of type `Long` to the molecule.
           |    * <br>Ref values point to entities with attributes in namespace `$refNs` (and possibly other namespaces).
           |    * <br><br>
           |    * Semantics of $attr1 being tacit:
           |    * <br>- If molecule is queried, only entities having $attr1 values asserted will match,
           |    * but $attr1 values are not returned in the result set (are 'tacit').
           |    */
           |  def ${attrClean}_ : AnyRef = ???
           |
           |
           |  /** ${doc(opts)}Adds reference '''`${attrClean.capitalize}`''' pointing to namespace `$refNs` to the molecule.
           |    * <br><br>
           |    * @return Unit (tacit ref id - relationship is ensured to exist but is not returned)
           |    */
           |  protected trait ${attrClean.capitalize}__[Ns, In] extends $clazz2[Ns, In]
           |  """.stripMargin


      case BackRef(backRef, _, _, _, _, _, backRefNs, _, _) =>
        s"""
           |  // BACKREF $backRef -----------------------------------------------------------------------------------
           |
           |  /** Adds back reference '''`$backRef`''' pointing back to namespace `$backRefNs` to the molecule.
           |    * <br>A back reference allow us to continue adding attributes from the previous namespace to the molecule:
           |    * {{{
           |    *   Order.date.LineItems.qty.item.price._Order.orderComment.get === ...
           |    * }}}
           |    */
           |  protected trait $backRef
           |  """.stripMargin
    }

    val selfJoin =
      s"""
         |  // SELF JOIN -----------------------------------------------------------------------------------
         |
         |  /** Adds Self join to the molecule.
         |    * <br><br>
         |    * Attributes before '''`Self`''' are joined with attributes added after '''`Self`''' by values that can unify:
         |    * <br><br>
         |    * Find 23-year olds liking the same beverage as 25-year olds (unifying by beverage):
         |    * {{{
         |    *   Person.name.age(23).Drinks.beverage._Person.Self // create self join
         |    *         .name.age(25).Drinks.beverage_(unify)      // unify by beverage
         |    *         .get === List(
         |    *           ("Joe", 23, "Coffee", "Ben", 25),  // Joe (23) and Ben(25) both like coffee
         |    *           ("Liz", 23, "Coffee", "Ben", 25),  // Liz (23) and Ben(25) both like coffee
         |    *           ("Liz", 23, "Tea", "Ben", 25)      // Liz (23) and Ben(25) both like tea
         |    *         )
         |    * }}}
         |    */
         |  protected trait Self extends SelfJoin
         |
         |  """.stripMargin

    val attrClasses = (attrClasses0.distinct :+ selfJoin).mkString("\n  ").trim

    val nsArities = d.nss.map(ns => ns.ns -> ns.attrs.size).toMap

    val nestedImports = if (
      attrs.exists {
        case Ref(_, _, _, _, _, baseTpe, _, _, _, _, _) if baseTpe.nonEmpty => true
        case _                                                              => false
      }
    ) List("Nested", "Nested_In_1", "Nested_In_2", "Nested_In_3").map("molecule.composition." + _ + "._").take(inArity + 1) else Nil

    val extraImports0 = attrs.collect {
      case Val(_, _, _, "Date", _, _, _, _, _, _) => "java.util.Date"
      case Val(_, _, _, "UUID", _, _, _, _, _, _) => "java.util.UUID"
      case Val(_, _, _, "URI", _, _, _, _, _, _)  => "java.net.URI"
    }.distinct ++ nestedImports

    val extraImports = if (extraImports0.isEmpty) "" else extraImports0.mkString(s"\nimport ", "\nimport ", "")

    (inArity, outArity, Ns, attrs, ext, attrClasses, nsArities, extraImports)
  }


  def namespaceBodies(d: Definition, namespace: Namespace, crossAttrs: Seq[String]): (String, Seq[(Int, String)]) = {

    val (inArity, outArity, ns, attrs, ext, attrClasses, nsArities, extraImports) = resolveNs(d, namespace, crossAttrs)

    val (inputEids, inputSpace) = if (inArity > 0)
      (s"\n  override def apply(eids: ?)               : ${ns}_In_1_0[Long] = ???", "           ")
    else
      ("", "")

    val nsTraitsOut = (0 to outArity).map(nsTrait(d.domain, namespace, 0, _, inArity, outArity, nsArities, crossAttrs)).mkString("\n")

    val outFile: String =
      s"""/*
         |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$ns`
         |*
         |* To change:
         |* 1. edit schema definition file in `${d.pkg}.schema/`
         |* 2. `sbt compile` in terminal
         |* 3. Refresh and re-compile project in IDE
         |*/
         |package ${d.pkg}.dsl
         |package ${firstLow(d.domain)}$extraImports
         |import molecule.boilerplate.attributes._
         |import molecule.boilerplate.base._
         |import molecule.boilerplate.dummyTypes._
         |import molecule.boilerplate.in1._
         |import molecule.boilerplate.in2._
         |import molecule.boilerplate.in3._
         |import molecule.boilerplate.out._
         |import molecule.api._
         |
         |
         |/** == Namespace `$ns` == */
         |object $ns extends ${ns}_0 with FirstNS {
         |  override def apply(eid: Long, eids: Long*): ${ns}_0 $inputSpace= ???
         |  override def apply(eids: Iterable[Long])  : ${ns}_0 $inputSpace= ???$inputEids
         |}
         |
         |trait $ns {
         |
         |  $attrClasses
         |}
         |
         |$nsTraitsOut""".stripMargin

    val nsTraitsIn: Seq[(Int, String)] = if (inArity == 0) Nil
    else (1 to inArity).map(in =>
      (in, (0 to outArity).map(nsTrait(d.domain, namespace, in, _, inArity, outArity, nsArities, crossAttrs)).mkString("\n"))
    )
    val inFiles: Seq[(Int, String)] = nsTraitsIn.map { case (in, inTraits) =>
      val inFile: String =
        s"""/*
           |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$ns`
           |*
           |* To change:
           |* 1. edit schema definition file in `${d.pkg}.schema/`
           |* 2. `sbt compile` in terminal
           |* 3. Refresh and re-compile project in IDE
           |*/
           |package ${d.pkg}.dsl
           |package ${firstLow(d.domain)}$extraImports
           |import molecule.boilerplate.attributes._
           |import molecule.boilerplate.base._
           |import molecule.boilerplate.dummyTypes._
           |import molecule.boilerplate.in1._
           |import molecule.boilerplate.in2._
           |import molecule.boilerplate.in3._
           |import molecule.boilerplate.out._
           |import molecule.api._
           |
           |$inTraits""".stripMargin

      (in, inFile)
    }

    (outFile, inFiles)
  }

  def namespaceBody(d: Definition, namespace: Namespace, crossAttrs: Seq[String]): String = {

    val (inArity, outArity, ns, attrs, ext, attrClasses, nsArities, extraImports) = resolveNs(d, namespace, crossAttrs)

    val (inputEids, inputSpace) = if (inArity > 0)
      (s"\n  override def apply(eids: ?)               : ${ns}_In_1_0[Long] = ???", "           ")
    else
      ("", "")

    val nsTraits = (for {
      in <- 0 to inArity
      out <- 0 to outArity
    } yield nsTrait(d.domain, namespace, in, out, inArity, outArity, nsArities, crossAttrs)).mkString("\n")

    s"""/*
       |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$ns`
       |*
       |* To change:
       |* 1. edit schema definition file in `${d.pkg}.schema/`
       |* 2. `sbt compile` in terminal
       |* 3. Refresh and re-compile project in IDE
       |*/
       |package ${d.pkg}.dsl
       |package ${firstLow(d.domain)}$extraImports
       |import molecule.boilerplate.attributes._
       |import molecule.boilerplate.base._
       |import molecule.boilerplate.dummyTypes._
       |import molecule.boilerplate.in1._
       |import molecule.boilerplate.in2._
       |import molecule.boilerplate.in3._
       |import molecule.boilerplate.out._
       |import molecule.api._
       |
       |
       |/** == Namespace `$ns` == */
       |object $ns extends ${ns}_0 with FirstNS {
       |  override def apply(eid: Long, eids: Long*): ${ns}_0 $inputSpace= ???
       |  override def apply(eids: Iterable[Long])  : ${ns}_0 $inputSpace= ???$inputEids
       |}
       |
       |trait $ns $ext{
       |
       |  $attrClasses
       |}
       |
       |$nsTraits""".stripMargin
  }


  def apply(codeDir: File, managedDir: File, defDirs: Seq[String], separateInFiles: Boolean = false, allIndexed: Boolean = true): Seq[File] = {
    // Loop domain directories
    val files = defDirs flatMap { defDir =>

      val schemaDirs = sbt.IO.listFiles(codeDir / defDir)
      assert(schemaDirs.exists(f => f.isDirectory && f.getName == "schema"), "\nMissing `schema` package inside any defined moleculeSchemas. Found:\n"
        + schemaDirs.mkString("\n"))

      val definitionFiles = sbt.IO.listFiles(codeDir / defDir / "schema").filter(f => f.isFile && f.getName.endsWith("Definition.scala"))
      assert(definitionFiles.nonEmpty, "\nFound no definition files in path: " + codeDir / defDir +
        "\nSchema definition file names should end with `<YourDomain...>Definition.scala`")

      // Loop definition files in each domain directory
      definitionFiles flatMap { definitionFile =>
        val d0 = parse(definitionFile, allIndexed)
        val d = resolve(d0)

        // Write schema file
        val schemaFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "schema" / s"${d.domain}Schema.scala"
        IO.write(schemaFile, schemaBody(d))

        val namespaceFiles = if (separateInFiles) {
          val crossAttrs = getCrossAttrs(d)
          d.nss.flatMap { ns =>
            val (outBody, inBodies) = namespaceBodies(d, ns, crossAttrs)

            // Output namespace files
            val nsOutFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}.scala"
            IO.write(nsOutFile, outBody)

            // Input namespace files
            val nsInFiles = inBodies.map { case (i, inBody) =>
              val inFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}_in$i.scala"
              IO.write(inFile, inBody)
              inFile
            }
            nsOutFile +: nsInFiles
          }

        } else {

          // Namespace files (including output and input)
          // Those could become too big so that you want to separate them
          val crossAttrs = getCrossAttrs(d)
          d.nss.map { ns =>
            val nsFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}.scala"
            val nsBody = namespaceBody(d, ns, crossAttrs)
            IO.write(nsFile, nsBody)
            nsFile
          }
        }

        schemaFile +: namespaceFiles
      }
    }
    files
  }
}
