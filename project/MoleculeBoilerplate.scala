import java.io.File

import sbt.{IO, _}

object MoleculeBoilerplate {

  // Definition AST .......................................

  case class Definition(pkg: String, imports: Seq[String], in: Int, out: Int, domain: String, curPart: String, nss: Seq[Namespace]) {
    def addAttr(attrs: Seq[Attr]) = {
      val previousNs = nss.init
      val lastNs = nss.last
      copy(nss = previousNs :+ lastNs.copy(attrs = lastNs.attrs ++ attrs))
    }
  }

  case class Namespace(part: String, ns: String, opt: Option[Extension] = None, attrs: Seq[Attr] = Seq())

  sealed trait Extension
  case object Edge extends Extension

  sealed trait Attr {
    val attr     : String
    val attrClean: String
    val clazz    : String
    val tpe      : String
    val baseTpe  : String
    val options  : Seq[Optional]
  }
  case class Val(attr: String, attrClean: String, clazz: String, tpe: String, baseTpe: String, datomicTpe: String, options: Seq[Optional] = Seq()) extends Attr
  case class Enum(attr: String, attrClean: String, clazz: String, tpe: String, baseTpe: String, enums: Seq[String], options: Seq[Optional] = Seq()) extends Attr
  case class Ref(attr: String, attrClean: String, clazz: String, clazz2: String, tpe: String, baseTpe: String, refNs: String, options: Seq[Optional] = Seq()) extends Attr
  case class BackRef(attr: String, attrClean: String, clazz: String, clazz2: String, tpe: String, baseTpe: String, backRef: String, options: Seq[Optional] = Seq()) extends Attr

  case class Optional(datomicKeyValue: String, clazz: String)


  // Helpers ..........................................

  private def padS(longest: Int, str: String) = pad(longest, str.length)
  private def pad(longest: Int, shorter: Int) = if (longest > shorter) " " * (longest - shorter) else ""
  private def firstLow(str: Any) = str.toString.head.toLower + str.toString.tail
  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }


  // Parse ..........................................

  private def parse(defFile: File, allIndexed: Boolean) = {
    val raw = IO.readLines(defFile) filterNot (_.isEmpty) map (_.trim)

    // Checks .......................................................................

    // Check package statement
    raw.collectFirst {
      case r"package (.*)$p\..*" => p
    }.getOrElse {
      sys.error("Found no package statement in definition file")
    }

    // Check input/output arities
    raw collect {
      case r"@InOut\((\d+)$in, (\d+)$out\)" => (in.toString.toInt, out.toString.toInt) match {
        case (i: Int, _) if i < 0 || i > 3  => sys.error(s"Input arity in '${defFile.getName}' was $in. It should be in the range 0-3")
        case (_, o: Int) if o < 1 || o > 22 => sys.error(s"Output arity of '${defFile.getName}' was $out. It should be in the range 1-22")
        case (i: Int, o: Int)               => (i, o)
      }
    } match {
      case Nil           => sys.error(
        """Please annotate the first namespace definition with '@InOut(inArity, outArity)' where:
          |inArity is a number between 1-3 for how many inputs molecules of this schema can await
          |outArity is a number between 1-22 for how many output attributes molecules of this schema can have""".stripMargin)
      case h :: t :: Nil => sys.error(
        """
          |Only the first namespace should be annotated with @InOut since all namespaces in a schema will need
          |to share the same arities to be able to carry over type information uniformly across namespaces.""".stripMargin)
      case annotations   => annotations.head
    }

    // Check domain name
    raw.collectFirst {
      case r"class (.*)${dmn}Definition"        => sys.error(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"class (.*)${dmn}Definition \{"     => sys.error(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"class (.*)${dmn}Definition \{ *\}" => sys.error(s"Can't use class as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition"        => sys.error(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition \{"     => sys.error(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
      case r"trait (.*)${dmn}Definition \{ *\}" => sys.error(s"Can't use trait as definition container in ${defFile.getName}. Please use an object:\nobject ${dmn}Definiton { ...")
    }

    raw.collect {
      case r"object (.*)${name}Definition"      => name
      case r"object (.*)${name}Definition \{"   => name
      case r"object (.*)${name}Definition \{\}" => name
    } match {
      case Nil                      => sys.error("Couldn't find definition object <domain>Definition in " + defFile.getName)
      case l: List[_] if l.size > 1 => sys.error(s"Only one definition object per definition file allowed. Found ${l.size}:" + l.mkString("\n - ", "Definition\n - ", "Definition"))
      case domainNameList           => firstLow(domainNameList.head)
    }


    // Parse ..........................................

    def parseOptions(str0: String, acc: Seq[Optional] = Nil, attr: String, curFullNs: String = ""): Seq[Optional] = {
      val indexed = Optional( """":db/index"             , true.asInstanceOf[Object]""", "Indexed")
      val options = str0 match {
        case r"\.doc\((.*)$msg\)(.*)$str" => parseOptions(str, acc :+ Optional(s"""":db/doc"               , $msg""", ""), attr, curFullNs)
        case r"\.fullTextSearch(.*)$str"  => parseOptions(str, acc :+ Optional("""":db/fulltext"          , true.asInstanceOf[Object]""", "FulltextSearch[Ns, In]"), attr, curFullNs)
        case r"\.uniqueValue(.*)$str"     => parseOptions(str, acc :+ Optional("""":db/unique"            , ":db.unique/value"""", "UniqueValue"), attr, curFullNs)
        case r"\.uniqueIdentity(.*)$str"  => parseOptions(str, acc :+ Optional("""":db/unique"            , ":db.unique/identity"""", "UniqueIdentity"), attr, curFullNs)
        case r"\.subComponents(.*)$str"   => parseOptions(str, acc :+ Optional("""":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent"), attr, curFullNs)
        case r"\.subComponent(.*)$str"    => parseOptions(str, acc :+ Optional("""":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent"), attr, curFullNs)
        case r"\.noHistory(.*)$str"       => parseOptions(str, acc :+ Optional("""":db/noHistory"         , true.asInstanceOf[Object]""", "NoHistory"), attr, curFullNs)
        case r"\.indexed(.*)$str"         => parseOptions(str, acc :+ indexed, attr, curFullNs)
        case ""                           => acc
        case unexpected                   => sys.error(s"Unexpected options code for attribute `$attr` in namespace `$curFullNs` in ${defFile.getName}:\n" + unexpected)
      }
      if (allIndexed) (options :+ indexed).distinct else options
    }

    def parseAttr(backTics: Boolean, attrClean: String, str: String, curPart: String, curFullNs: String): Seq[Attr] = {
      val attr = if (backTics) s"`$attrClean`" else attrClean
      val attrK = attrClean + "K"
      val curNs = if (curFullNs.contains('_')) curFullNs.split("_").last else curFullNs
      val curPartDotNs = if (curFullNs.contains('_')) curFullNs.replace("_", ".") else curFullNs

      str match {
        case r"oneString(.*)$str"  => Seq(Val(attr, attrClean, "OneString", "String", "", "string", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneInt(.*)$str"     => Seq(Val(attr, attrClean, "OneInt", "Int", "", "long", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneLong(.*)$str"    => Seq(Val(attr, attrClean, "OneLong", "Long", "", "long", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneFloat(.*)$str"   => Seq(Val(attr, attrClean, "OneFloat", "Float", "", "double", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneDouble(.*)$str"  => Seq(Val(attr, attrClean, "OneDouble", "Double", "", "double", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneBigInt(.*)$str"  => Seq(Val(attr, attrClean, "OneBigInt", "BigInt", "", "bigint", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneBigDec(.*)$str"  => Seq(Val(attr, attrClean, "OneBigDec", "BigDecimal", "", "bigdec", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneByte(.*)$str"    => Seq(Val(attr, attrClean, "OneByte", "Byte", "", "bytes", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneBoolean(.*)$str" => Seq(Val(attr, attrClean, "OneBoolean", "Boolean", "", "boolean", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneDate(.*)$str"    => Seq(Val(attr, attrClean, "OneDate", "java.util.Date", "", "instant", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneUUID(.*)$str"    => Seq(Val(attr, attrClean, "OneUUID", "java.util.UUID", "", "uuid", parseOptions(str, Nil, attr, curFullNs)))
        case r"oneURI(.*)$str"     => Seq(Val(attr, attrClean, "OneURI", "java.net.URI", "", "uri", parseOptions(str, Nil, attr, curFullNs)))

        case r"manyString(.*)$str"  => Seq(Val(attr, attrClean, "ManyString", "Set[String]", "String", "string", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyInt(.*)$str"     => Seq(Val(attr, attrClean, "ManyInt", "Set[Int]", "Int", "long", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyLong(.*)$str"    => Seq(Val(attr, attrClean, "ManyLong", "Set[Long]", "Long", "long", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyFloat(.*)$str"   => Seq(Val(attr, attrClean, "ManyFloat", "Set[Float]", "Float", "double", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyDouble(.*)$str"  => Seq(Val(attr, attrClean, "ManyDouble", "Set[Double]", "Double", "double", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyBigInt(.*)$str"  => Seq(Val(attr, attrClean, "ManyBigInt", "Set[BigInt]", "BigInt", "bigint", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyBigDec(.*)$str"  => Seq(Val(attr, attrClean, "ManyBigDec", "Set[BigDecimal]", "BigDecimal", "bigdec", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyByte(.*)$str"    => Seq(Val(attr, attrClean, "ManyByte", "Set[Byte]", "Byte", "bytes", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyBoolean(.*)$str" => Seq(Val(attr, attrClean, "ManyBoolean", "Set[Boolean]", "Boolean", "boolean", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyDate(.*)$str"    => Seq(Val(attr, attrClean, "ManyDate", "Set[java.util.Date]", "java.util.Date", "instant", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyUUID(.*)$str"    => Seq(Val(attr, attrClean, "ManyUUID", "Set[java.util.UUID]", "java.util.UUID", "uuid", parseOptions(str, Nil, attr, curFullNs)))
        case r"manyURI(.*)$str"     => Seq(Val(attr, attrClean, "ManyURI", "Set[java.net.URI]", "java.net.URI", "uri", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapString(.*)$str"  => Seq(
          Val(attr, attrClean, "MapString", "Map[String, String]", "String", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneString", "String", "K", "string", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapInt(.*)$str"     => Seq(
          Val(attr, attrClean, "MapInt", "Map[String, Int]", "Int", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneInt", "Int", "K", "long", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapLong(.*)$str"    => Seq(
          Val(attr, attrClean, "MapLong", "Map[String, Long]", "Long", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneLong", "Long", "K", "long", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapFloat(.*)$str"   => Seq(
          Val(attr, attrClean, "MapFloat", "Map[String, Float]", "Float", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneFloat", "Float", "K", "double", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapDouble(.*)$str"  => Seq(
          Val(attr, attrClean, "MapDouble", "Map[String, Double]", "Double", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneDouble", "Double", "K", "double", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBigInt(.*)$str"  => Seq(
          Val(attr, attrClean, "MapBigInt", "Map[String, BigInt]", "BigInt", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneBigInt", "BigInt", "K", "bigint", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBigDec(.*)$str"  => Seq(
          Val(attr, attrClean, "MapBigDec", "Map[String, BigDecimal]", "BigDecimal", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneBigDec", "BigDecimal", "K", "bigdec", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapByte(.*)$str"   => Seq(
          Val(attr, attrClean, "MapByte", "Map[String, Byte]", "Byte", "bytes", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneByte", "Byte", "K", "bytes", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapBoolean(.*)$str" => Seq(
          Val(attr, attrClean, "MapBoolean", "Map[String, Boolean]", "Boolean", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneBoolean", "Boolean", "K", "boolean", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapDate(.*)$str"    => Seq(
          Val(attr, attrClean, "MapDate", "Map[String, java.util.Date]", "java.util.Date", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneDate", "java.util.Date", "K", "instant", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapUUID(.*)$str"    => Seq(
          Val(attr, attrClean, "MapUUID", "Map[String, java.util.UUID]", "java.util.UUID", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneUUID", "java.util.UUID", "K", "uuid", parseOptions(str, Nil, attr, curFullNs)))

        case r"mapURI(.*)$str"     => Seq(
          Val(attr, attrClean, "MapURI", "Map[String, java.net.URI]", "java.net.URI", "string", parseOptions(str, Nil, attr, curFullNs)),
          Val(attrK, attrK, "OneURI", "java.net.URI", "K", "uri", parseOptions(str, Nil, attr, curFullNs)))

        case r"oneEnum\((.*?)$enums\)(.*)$str"  => Seq(Enum(attr, attrClean, "OneEnum", "String", "", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str, Nil, attr, curFullNs)))
        case r"manyEnum\((.*?)$enums\)(.*)$str" => Seq(Enum(attr, attrClean, "ManyEnums", "Set[String]", "String", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str, Nil, attr, curFullNs)))

        // Bidirectional_ start

        case r"oneBi\[(.*)$biRef\](.*)$str" =>
          val (refNs, refOptional) = parseBiRefTypeArg_("one", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", refNs, parseOptions(str, Nil, attr, curFullNs) :+ refOptional))

        case r"manyBi\[(.*)$biRef\](.*)$str" =>
          val (refNs, refOptional) = parseBiRefTypeArg_("many", biRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", refNs, parseOptions(str, Nil, attr, curFullNs) :+ refOptional))


        // Bidirectional_ end

        case r"target\[(.*)$biTargetRef\](.*)$str" =>
          val (targetNs, targetOptional) = parseTargetRefTypeArg(biTargetRef, attr, curPart, curFullNs)
          Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", targetNs, parseOptions(str, Nil, attr, curFullNs) :+ targetOptional))


        // References

        case r"one\[(.*)$ref\](.*)$str"  => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", parseRefTypeArg(ref, curPart), parseOptions(str, Nil, attr, curFullNs)))
        case r"many\[(.*)$ref\](.*)$str" => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", parseRefTypeArg(ref, curPart), parseOptions(str, Nil, attr, curFullNs)))


        // Missing ref type args

        case r"oneBi(.*)$str" => sys.error(
          s"""Type arg missing for bidirectional ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
              |Please add something like:
              |  val $attr = oneBi[$curNs] // for bidirectional self-reference, or:
              |  val $attr = oneBi[<otherNamespace>.<revRefAttr>.type] // for "outgoing" bidirectional reference to other namespace""".stripMargin)

        case r"manyBi(.*)$str" => sys.error(
          s"""Type arg missing for bidirectional ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
              |Please add something like:
              |  val $attr = manyBi[$curNs] // for bidirectional self-reference, or:
              |  val $attr = manyBi[<otherNamespace>.<revRefAttr>.type] // for "outgoing" bidirectional reference to other namespace""".stripMargin)

        case r"rev(.*)$str" => sys.error(
          s"""Type arg missing for bidirectional reverse ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
              |Please add the namespace where the bidirectional ref pointing to this attribute was defined:
              |  val $attr = rev[<definingNamespace>]""".stripMargin)

        case r"one(.*)$str" => sys.error(
          s"""Type arg missing for ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
              |Please add something like:
              |  val $attr = one[$curNs] // for self-reference, or
              |  val $attr = one[<otherNamespace>] // for ref towards other namespace""".stripMargin)

        case r"many(.*)$str" => sys.error(
          s"""Type arg missing for ref definition `$attr` in `$curPartDotNs` of ${defFile.getName}.
              |Please add something like:
              |  val $attr = many[$curNs] // for self-reference, or
              |  val $attr = many[<otherNamespace>] // for ref towards other namespace""".stripMargin)

        case unexpected => sys.error(s"Unexpected attribute code in ${defFile.getName}:\n" + unexpected)
      }
    }

    def parseRefTypeArg(refStr: String, curPartition: String = "") = refStr match {
      case r"\w*Definition\.([a-z].*)$partref"  => partref.replace(".", "_")
      case r"([a-z]\w*)$part\.(.*)$ref"         => part + "_" + ref
      case r"(.*)$ref" if curPartition.nonEmpty => curPartition + "_" + ref
      case r"(.*)$ref"                          => ref
    }

    def parseTargetRefTypeArg(refStr: String, baseAttr: String, basePart: String = "", baseFullNs: String = "") = {
      refStr match {

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$targetNs\.(.*)$targetAttr\.type" if basePart.nonEmpty =>
          //          println(s"Target 1:  $basePart      $baseFullNs      $baseAttr                 ---    $targetNs      $targetAttr")
          (s"${basePart}_$targetNs", Optional(s"${basePart}_$targetNs.$targetAttr", "BiTargetRef_"))

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$targetNs\.(.*)$targetAttr\.type" =>
          //          println(s"Target 2:  $basePart      $baseFullNs      $baseAttr                 ---    $targetNs      $targetAttr")
          (targetNs, Optional(s"$targetNs.$targetAttr", "BiTargetRef_"))

        case other =>
          sys.error(
            s"""Target reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} should have a type arg pointing to
                |the attribute that points to this. Something like:
                |  val $baseAttr: AnyRef = target[<baseNs>.<biAttr>.type]
                |(Since this is a recursive definitionn, we need to add a return type)""".stripMargin)
      }
    }

    def parseBiRefTypeArg_(card: String, refStr: String, baseAttr: String, basePart: String = "", baseFullNs: String = "") = {
      //            println(s"basePart baseFullNs baseAttr: $basePart      $baseFullNs      $baseAttr")

      refStr match {

        // Edge ref -------------------------------------------------------------------

        // With MyDefinition .......................................

        // val selfRef = oneBi[MyDomainDefinition.ThisPartition.ThisNamespace.selfRef.type]  // or manyBi
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttra\.type" if s"${part}_$edgeNs" == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outRefAttr = oneBi[MyDomainDefinition.ThisPartition.OtherNamespace.revRefAttr.type]  // or manyBi
        // should be only
        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" if part == basePart =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} should have " +
            s"only the namespace prefix in the type argument:\n  val $baseAttr = ${card}Bi[$edgeNs.$targetAttr.type]")

        // val outRefAttr = oneBi[MyDomainDefinition.SomePartition.OtherNamespace.toRefAttr.type]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" =>
          //          println(s"1  $basePart      $baseFullNs      $baseAttr                 ---    $part      $edgeNs      $targetAttr")
          (s"${part}_$edgeNs", Optional(s"${part}_$edgeNs.$targetAttr", s"BiEdgeRef_:$baseFullNs.$baseAttr"))


        // With partition .......................................

        // val selfRef = oneBi[ThisPartition.ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" if s"${part}_$edgeNs" == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and can't have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val selfRef = oneBi[ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if basePart.nonEmpty && s"${basePart}_$edgeNs" == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outgoingRef = oneBi[SomePartition.OtherNamespace.toRefAttr.type]
        case r"([a-z]\w*)$part\.(.*)$edgeNs\.(.*)$targetAttr\.type" =>
          //          println(s"2  $basePart      $baseFullNs      $baseAttr                 ---    $part      $edgeNs      $targetAttr")
          (s"${part}_$edgeNs", Optional(s"${part}_$edgeNs.$targetAttr", s"BiEdgeRef_:$baseFullNs.$baseAttr"))


        // With edge ns .......................................

        // val selfRef = oneBi[ThisNamespace.selfRef.type]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if edgeNs == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$edgeNs]")

        // val outRefAttr = oneBi[OtherNamespace.toRefAttr.type]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" if basePart.nonEmpty =>
          //          println(s"3  $basePart      $baseFullNs      $baseAttr                 ---    $edgeNs      $targetAttr")
          (s"${basePart}_$edgeNs", Optional(s"${basePart}_$edgeNs.$targetAttr", s"BiEdgeRef_:$baseFullNs.$baseAttr"))

        // val outRefAttr = oneBi[OtherNamespace.revRefAttr.type]
        case r"(.*)$edgeNs\.(.*)$targetAttr\.type" =>
          //          println(s"4  $basePart      $baseFullNs      $baseAttr                 ---    $edgeNs      $targetAttr")
          (edgeNs, Optional(s"$edgeNs.$targetAttr", s"BiEdgeRef_:$baseFullNs.$baseAttr"))

        // Incorrect edge definition
        // val selfRef = oneBi[selfRef.type] // presuming it hasn't been imported from another namespace
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"(.*)$a\.type" if a == baseAttr =>
          val ns = if (basePart.nonEmpty) baseFullNs.split("_").last else baseFullNs
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and only needs the current namespace as type argument:\n  val $baseAttr = ${card}Bi[$ns]")



        // Bidirectional_ self-reference -------------------------------------------------------------------

        // val selfRef = oneBi[MyDomainDefinition.ThisPartition.ThisNamespace] // or manyBi
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"\w*Definition\.([a-z]\w*)$part\.(.*)$selfRef" if s"${part}_$selfRef" == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$selfRef]")

        // val selfRef = oneBi[ThisPartition.ThisNamespace]
        // should be only
        // val selfRef = oneBi[ThisNamespace]
        case r"([a-z]\w*)$part\.(.*)$selfRef" if s"${part}_$selfRef" == baseFullNs =>
          sys.error(s"Bidirectional_ reference `$baseAttr` in `$baseFullNs` of ${defFile.getName} is a self-reference " +
            s"and doesn't need to have the attribute name specified. This is enough:\n  val $baseAttr = ${card}Bi[$selfRef]")

        // val spouse = oneBi[Person]
        case selfRef if basePart.nonEmpty && s"${basePart}_$selfRef" == baseFullNs =>
          //          println(s"5  $basePart      $baseFullNs      $baseAttr                 ---    $selfRef")
          (s"${basePart}_$selfRef", Optional("", "BiSelfRef_"))

        // val spouse = oneBi[Person]
        case selfRef if selfRef == baseFullNs =>
          //          println(s"6  $basePart      $baseFullNs      $baseAttr                 ---    $selfRef")
          (selfRef, Optional("", "BiSelfRef_"))



        // Bidirectional_ to other namespace -------------------------------------------------------------------

        case otherRef if basePart.nonEmpty =>
          //          println(s"7  $basePart      $baseFullNs      $baseAttr                 ---    $otherRef")
          (s"${basePart}_$otherRef", Optional(s"${basePart}_$otherRef.$baseAttr", "BiOtherRef_"))

        case otherRef =>
          //          println(s"8  $basePart      $baseFullNs      $baseAttr                 ---    $otherRef")
          (otherRef, Optional(s"$otherRef.$baseAttr", "BiOtherRef_"))
      }
    }

    val definition: Definition = raw.foldLeft(Definition("", Seq(), -1, -1, "", "", Seq())) {
      case (d, line) => line.trim match {
        case r"\/\/.*" /* comments allowed */                         => d
        case r"package (.*)$path\.[\w]*"                              => d.copy(pkg = path)
        case "import molecule.schema.definition._"                    => d
        case r"@InOut\((\d+)$inS, (\d+)$outS\)"                       => d.copy(in = inS.toString.toInt, out = outS.toString.toInt)
        case r"object (.*)${dmn}Definition \{"                        => d.copy(domain = dmn)
        case r"object ([a-z]\w*)$part\s*\{"                           => d.copy(curPart = part)
        case r"object (\w*)$part\s*\{"                                => sys.error(s"Partition name '$part' in ${defFile.getName} should start with a lowercase letter")
        case r"trait ([A-Z]\w*)$ns\s*\{" if d.curPart.nonEmpty        => d.copy(nss = d.nss :+ Namespace(d.curPart, d.curPart + "_" + ns))
        case r"trait ([A-Z]\w*)$ns\s*\{"                              => d.copy(nss = d.nss :+ Namespace("", ns))
        case r"trait (\w*)$ns\s*\{"                                   => sys.error(s"Unexpected namespace name '$ns' in ${defFile.getName}. Namespaces have to start with a capital letter [A-Z].")
        case r"val\s*(\`?)$q1(\w*)$a(\`?)$q2: AnyRef\s*\=\s*(.*)$str" => d.addAttr(parseAttr(q1.nonEmpty, a, str, d.curPart, d.nss.last.ns))
        case r"val\s*(\`?)$q1(\w*)$a(\`?)$q2\s*\=\s*(.*)$str"         => d.addAttr(parseAttr(q1.nonEmpty, a, str, d.curPart, d.nss.last.ns))
        case "}"                                                      => d
        case ""                                                       => d
        case r"object .* extends .*"                                  => d
        case unexpected                                               => sys.error(s"Unexpected definition code in ${defFile.getName}:\n" + unexpected)
      }
    }

    definition
  }

  def resolve(definition: Definition) = {
    val updatedNss1 = markBidrectionalEdgeProperties_(definition.nss)
    val updatedNss3 = definition.nss.foldLeft(updatedNss1) { case (updatedNss2, curNs) =>
      addBackRefs(updatedNss2, curNs)
    }
    definition.copy(nss = updatedNss3)
  }

  def markBidrectionalEdgeProperties_(nss: Seq[Namespace]): Seq[Namespace] = nss.map { ns =>
    val isEdge = ns.attrs.collectFirst {
      case ref: Ref if ref.options.exists(_.clazz.startsWith("BiTargetRef_")) => true
    } getOrElse false

    if (isEdge) {
      val newAttrs: Seq[Attr] = ns.attrs.map {
        case biTargetRef: Ref if biTargetRef.options.exists(_.clazz.startsWith("BiTargetRef_")) => biTargetRef

        case biRef: Ref if biRef.options.exists(opt => opt.clazz.startsWith("Bi") && opt.clazz.substring(6, 10) != "Prop") => sys.error(
          s"""Namespace `${ns.ns}` is already defined as a "property edge" and can't also define a bidirectional reference `${biRef.attr}`.""")

        case ref: Ref   => ref.copy(options = ref.options :+ Optional("", "BiEdgePropRef"))
        case enum: Enum => enum.copy(options = enum.options :+ Optional("", "BiEdgePropAttr_"))
        case value: Val => value.copy(options = value.options :+ Optional("", "BiEdgePropAttr_"))
        case other      => other
      }
      ns.copy(opt = Some(Edge), attrs = newAttrs)
    } else
      ns
  }

  def addBackRefs(nss: Seq[Namespace], curNs: Namespace): Seq[Namespace] = {
    // Gather OneRefs (ManyRefs are treated as nested data structures)
    val refMap = curNs.attrs.collect {
      case outRef@Ref(_, _, _, _, _, _, refNs, _) => refNs -> outRef
    }.toMap

    nss.map {
      case ns2 if refMap.nonEmpty && refMap.keys.toList.contains(ns2.ns) => {
        val attrs2 = refMap.foldLeft(ns2.attrs) { case (attrs, (refNs, outRef@Ref(_, _, _, _, tpe, _, _, opts))) =>
          val cleanNs = if (curNs.ns.contains('_')) curNs.ns.split("_").tail.head else curNs.ns
          // todo: check not to backreference same-named namespaces in different partitions
          curNs.ns match {
            case ns1 if ns1 == ns2.ns => attrs
            case other                => attrs :+ BackRef(s"_$cleanNs", curNs.ns, "BackRefAttr", "BackRef", tpe, "", "")
          }
        }.distinct
        ns2.copy(attrs = attrs2)
      }
      case ns2                                                           => ns2
    }
  }


  // Generate ..........................................

  def schemaBody(d: Definition) = {

    def resolveOptions(options: Seq[Optional]) = options flatMap {
      case Optional(_, clazz) if clazz.startsWith("Bi") => None
      case optional                                     => Some(optional.datomicKeyValue)
    }

    def attrStmts(ns: String, a: Attr) = {
      val ident = s"""":db/ident"             , ":${firstLow(ns)}/${a.attrClean}""""
      def tpe(t: String) = s"""":db/valueType"         , ":db.type/$t""""
      def card(c: String) = s"""":db/cardinality"       , ":db.cardinality/$c""""
      val stmts = a match {
        case Val(_, _, clazz, _, _, t, options) if clazz.take(3) == "One" => Seq(tpe(t), card("one")) ++ resolveOptions(options)
        case Val(_, _, _, _, _, t, options)                               => Seq(tpe(t), card("many")) ++ resolveOptions(options)
        case a: Attr if a.clazz.take(3) == "One"                          => Seq(tpe("ref"), card("one")) ++ resolveOptions(a.options)
        case a: Attr                                                      => Seq(tpe("ref"), card("many")) ++ resolveOptions(a.options)
        case unexpected                                                   => sys.error(s"Unexpected attribute statement:\n" + unexpected)
      }
      val all = (ident +: stmts) ++ Seq(
        """":db/id"                , Peer.tempid(":db.part/db")""",
        """":db.install/_attribute", ":db.part/db""""
      )
      s"Util.map(${all.mkString(",\n             ")})"
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
        throw new IllegalArgumentException("Partition name `molecule` is reserved by Molecule. Please choose another partition name.")
      d.nss.collectFirst {
        case ns if ns.attrs.collectFirst {
          case biTargetRef: Ref if biTargetRef.options.exists(_.clazz.startsWith("BiTargetRef_")) => true
        }.getOrElse(false) => {
          val moleculeMetaNs = Namespace("molecule", "molecule_Meta", None, Seq(
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
        |import molecule.dsl.Transaction
        |import datomic.{Util, Peer}
        |
        |object ${d.domain}Schema extends Transaction {
        |  $partitionsList
        |  lazy val namespaces = Util.list(
        |    ${stmts.mkString(",\n    ")}
        |  )
        |}""".stripMargin
  }

  def nsTrait(namesp: Namespace, in: Int, out: Int, maxIn: Int, maxOut: Int, nsArities: Map[String, Int]) = {
    val (ns, option, attrs) = (namesp.ns, namesp.opt, namesp.attrs)

    val InTypes = (0 until in) map (n => "I" + (n + 1))
    val OutTypes = (0 until out) map (n => (n + 'A').toChar.toString)

    val maxTpe = attrs.filter(!_.attr.startsWith("_")).map(_.tpe.length).max

    val maxAttr = attrs.map(_.attr).filter(!_.startsWith("_")).map(_.length).max
    val maxAttr4 = {
      val lengths = attrs.filter(!_.clazz.contains("Ref")).map(_.attr.length)
      if (lengths.isEmpty) 0 else lengths.max
    }
    val maxAttr5 = attrs.map {
      case Val(_, attr, _, _, "K", _, _) => s"String => " + attr
      case other                         => other.attr
    }.filter(!_.startsWith("_")).map(_.length).max

    val maxAttr6 = {
      val lengths = attrs.filter(!_.clazz.contains("Ref")).map {
        case Val(_, attr, _, _, "K", _, _) => s"(key: String) => " + attr
        case other                         => other.attr
      }.map(_.length)
      if (lengths.isEmpty) 0 else lengths.max
    }

    val (attrVals, attrVals_) = attrs.flatMap {
      case BackRef(_, _, _, _, _, _, _, _) => None
      case a                               => {
        val (attr, attrClean, tpe) = (a.attr, a.attrClean, a.tpe)
        val p3 = padS(maxTpe, tpe)

        val (nextNS, thisNS) = (in, out) match {
          case (0, 0) => (
            s"${ns}_1[$tpe$p3]",
            s"${ns}_0")

          case (0, o) => (
            s"${ns}_${o + 1}[${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_$o[${OutTypes mkString ", "}]")

          case (i, o) => (
            s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_${i}_$o[${(InTypes ++ OutTypes) mkString ", "}]")
        }

        val (nextIn, thisIn) = if (maxIn == 0 || in == maxIn) {
          val (n1, n2) = (out + in + 1, out + in + 2)
          val (t1, t2) = ((1 to n1).map(i => "_").mkString(","), (1 to n2).map(i => "_").mkString(","))
          (s"P$n2[$t2]", s"P$n1[$t1]")
        } else (in, out) match {
          case (0, 0) => (
            s"${ns}_In_1_1[$tpe$p3, $tpe$p3]",
            s"${ns}_In_1_0[$tpe$p3]")

          case (0, o) => (
            s"${ns}_In_1_${o + 1}[$tpe$p3, ${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_1_$o[$tpe$p3, ${OutTypes mkString ", "}]")

          case (i, 0) => (
            s"${ns}_In_${i + 1}_1[${(InTypes :+ tpe) mkString ", "}$p3, $tpe$p3]",
            s"${ns}_In_${i + 1}_0[${(InTypes :+ tpe) mkString ", "}$p3]")

          case (i, o) => (
            s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ tpe) mkString ", "}$p3, ${(OutTypes :+ tpe) mkString ", "}$p3]",
            s"${ns}_In_${i + 1}_$o[${(InTypes :+ tpe) mkString ", "}$p3, ${OutTypes mkString ", "}]")
        }

        val p1 = padS(maxAttr, attr)
        val p4 = padS(maxAttr4, attr)

        val p5 = padS(maxAttr5, attr)
        val p6 = padS(maxAttr6, attr)
        val p7 = padS(maxAttr5, "String => " + attr)
        val p8 = padS(maxAttr6, "(key: String) => " + attr)

        val t = s"[$nextNS, $nextIn] with $nextNS"

        val attrVal = a match {
          case valueAttr: Val if a.baseTpe == "K" && in == 0 && out == 0 => s"""lazy val $attr  $p1: String => $attr$p7$t = (key: String) => new $attr$p8$t { override val _kw = ":${firstLow(ns)}/$attr" }"""
          case enumAttr: Enum if a.baseTpe == "K" && in == 0 && out == 0 => s"""lazy val $attr  $p1: String => $attr$p7$t = (key: String) => new $attr$p8$t { override val _kw = ":${firstLow(ns)}/$attr" }"""
          case _ if a.baseTpe == "K"                                     => s"""lazy val $attr  $p1: String => $attr$p7$t = ???"""
          case valueAttr: Val if in == 0 && out == 0                     => s"""lazy val $attr  $p1: $attr$p5$t = new $attr$p6$t { override val _kw = ":${firstLow(ns)}/$attr" }"""
          case enumAttr: Enum if in == 0 && out == 0                     => s"""lazy val $attr  $p1: $attr$p5$t = new $attr$p6$t { override val _kw = ":${firstLow(ns)}/$attr" }"""
          case _                                                         => s"""lazy val $attr  $p1: $attr$p5$t = ???"""
        }

        val p2 = padS(maxAttr, attrClean)
        val attrVal_ = a match {
          case valueAttr: Val if a.baseTpe == "K" => s"lazy val ${attrClean}_ $p2: String => $attr$p7[$thisNS, $thisIn] with $thisNS = ???"
          case _                                  => s"lazy val ${attrClean}_ $p2: $attr$p5[$thisNS, $thisIn] with $thisNS = ???"
        }

        Some((attrVal, attrVal_))
      }
    }.unzip

    val attrValsOpt = attrs.flatMap {
      case BackRef(_, _, _, _, _, _, _, _) => None
      case a                               => {
        val (attr, attrClean, tpe) = (a.attr, a.attrClean, a.tpe)
        val p2 = padS(maxAttr, attrClean)
        val p3 = padS(maxTpe, tpe)
        val nextNS = (in, out) match {
          case (0, 0) => s"${ns}_1[Option[$tpe]$p3]"
          case (0, o) => s"${ns}_${o + 1}[${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]"
          case (i, o) => s"${ns}_In_${i}_${o + 1}[${(InTypes ++ OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]"
        }
        val nextIn = if (maxIn == 0 || in == maxIn) {
          val n2 = out + in + 2
          val t2 = (1 to n2).map(i => "_").mkString(",")
          s"P$n2[$t2]"
        } else (in, out) match {
          case (0, 0) => s"${ns}_In_1_1[Option[$tpe]$p3, Option[$tpe]$p3]"
          case (0, o) => s"${ns}_In_1_${o + 1}[Option[$tpe]$p3, ${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]"
          case (i, 0) => s"${ns}_In_${i + 1}_1[${(InTypes :+ s"Option[$tpe]") mkString ", "}$p3, Option[$tpe]$p3]"
          case (i, o) => s"${ns}_In_${i + 1}_${o + 1}[${(InTypes :+ s"Option[$tpe]") mkString ", "}$p3, ${(OutTypes :+ s"Option[$tpe]") mkString ", "}$p3]"
        }

        a match {
          case valueAttr: Val if a.baseTpe == "K" => None
          case _                                  => Some(s"lazy val $attrClean$$ $p2: $attrClean$$$p2[$nextNS, $nextIn] with $nextNS = ???")
        }
      }
    }

    val (maxClazz2, maxRefNs, maxNs) = attrs.map {
      case Ref(_, _, _, clazz2, _, _, refNs, _)       => (clazz2.length, refNs.length, 0)
      case BackRef(_, clazz2, _, _, _, _, backRef, _) => (clazz2.length, backRef.length, ns.length)
      case other                                      => (0, 0, 0)
    }.unzip3

    //    val maxAttr0 = attrs.map(_.attr.length).max
    val maxAttr0 = attrs.map(_.attrClean.length).max
    val refCode = attrs.foldLeft(Seq("")) {
      case (acc, Ref(attr, attrClean, _, clazz2, _, baseType, refNs, opts)) => {
        //        val p1 = padS(maxAttr0, attr)
        val p1 = padS(maxAttr0, attrClean)
        val p2 = padS("ManyRef".length, clazz2)
        val p3 = padS(maxRefNs.max, refNs)
        val ref = (in, out) match {
          case (0, 0) if baseType.isEmpty                => s"${refNs}_0$p3"
          case (0, 0)                                    => s"${refNs}_0$p3 with Nested0[${refNs}_0$p3, ${refNs}_1$p3]"
          case (0, o) if o == maxOut || baseType.isEmpty => s"${refNs}_$o$p3[${OutTypes mkString ", "}]"
          case (0, o)                                    => s"${refNs}_$o$p3[${OutTypes mkString ", "}] with Nested$o[${refNs}_$o$p3, ${refNs}_${o + 1}$p3, ${OutTypes mkString ", "}]"
          case (i, o)                                    => s"${refNs}_In_${i}_$o$p3[${(InTypes ++ OutTypes) mkString ", "}]"
        }
        val birectional = opts.collectFirst {
          case Optional(_, clazz) if clazz.startsWith("BiSelfRef_")    => s"with BiSelfRef_ "
          case Optional(_, clazz) if clazz.startsWith("BiOtherRef_")   => s"with BiOtherRef_ "
          case Optional(_, clazz) if clazz.startsWith("BiEdgePropRef") => s"with BiEdgePropRef_ "
          //          case Optional(_, clazz) if clazz.startsWith("BiEdgePropAttr_")         => s"with BiEdgePropAttr_ "
          //          case Optional(_, clazz) if clazz.startsWith("BiEdgePropRefAttr_")      => s"with BiEdgePropRefAttr_ "
          case Optional(biEdgeAttr, clazz) if clazz.startsWith("BiEdgeRef_:")    => s"with BiEdgeRef_[$biEdgeAttr[NS, NS]] "
          case Optional(biTargetAttr, clazz) if clazz.startsWith("BiTargetRef_") => s"with BiTargetRef_[$biTargetAttr[NS, NS]] "
        } getOrElse ""
        acc :+ s"def ${attrClean.capitalize} $p1: $clazz2$p2[$ns, $refNs$p3] with $ref $birectional= ???"
      }
      case (acc, BackRef(backAttr, backRef, _, _, _, _, _, opts))           =>
        val p1 = padS(maxAttr0, backAttr)
        val p2 = padS(maxClazz2.max, backRef)
        val ref = (in, out) match {
          case (0, 0) => s"${backRef}_0$p2"
          case (0, o) => s"${backRef}_$o$p2[${OutTypes mkString ", "}]"
          case (i, o) => s"${backRef}_In_${i}_$o$p2[${(InTypes ++ OutTypes) mkString ", "}]"
        }
        acc :+ s"def $backAttr $p1: $ref = ???"
      case (acc, _)                                                         => acc
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
            |  def Self: ${ns}_$o[$types] with SelfJoin = ???
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
            |  def Self: ${ns}_In_${i}_$o[$types] with SelfJoin = ???
            |}
         """.stripMargin
    }
  }


  def resolveNs(d: Definition, namespace: Namespace) = {
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
    def mapType(s: String) = s match {
      case "java.util.Date" => "Date"
      case "java.util.UUID" => "UUID"
      case "java.net.URI"   => "URI"
      case other            => other
    }

    val attrClasses = attrs.flatMap {

      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, opts) if tpe.take(3) == "Map" =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, opts) if baseTpe == "K" =>
        val options2 = opts :+ Optional("", "MapAttrK")
        val extensions = " with " + options2.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Val(attr, _, clazz, _, _, _, opts) =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Enum(attr, _, clazz, _, _, enums, opts) =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
        Seq( s"""class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions { $enumValues }""")

      case Ref(attr, _, clazz, _, _, _, _, opts) =>
        val extensions = opts.map {
          case Optional(_, bi) if bi.startsWith("BiSelfRef_")     => "BiSelfRefAttr_"
          case Optional(_, bi) if bi.startsWith("BiOtherRef_")    => "BiOtherRefAttr_"
          case Optional(a, bi) if bi.startsWith("BiEdgeRef_")     => "BiEdgeRefAttr_[" + a + "[NS, NS]]"
          case Optional(_, bi) if bi.startsWith("BiEdgePropAttr") => "BiEdgePropAttr_"
          case Optional(_, bi) if bi.startsWith("BiEdgePropRef")  => "BiEdgePropRefAttr_"
          case Optional(a, bi) if bi.startsWith("BiTargetRef_")   => "BiTargetRefAttr_[" + a + "[NS, NS]]"
          case Optional(_, other)                                 => other
        }.mkString(" with ", " with ", "")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
    }.mkString("\n  ").trim


    val attrClassesOpt = attrs.flatMap {

      case Val(attr, attrClean, clazz, tpe, baseTpe, _, opts) if tpe.take(3) == "Map" =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")

      case Val(attr, attrClean, clazz, _, _, _, opts) =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")

      case Enum(attr, attrClean, clazz, _, _, enums, opts) =>
        val extensions = if (opts.isEmpty) "" else " with " + opts.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
        Seq( s"""class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions { $enumValues }""")

      case Ref(attr, attrClean, clazz, _, _, _, _, opts) =>
        val extensions = opts.map {
          case Optional(_, bi) if bi.startsWith("BiSelfRef_")     => "BiSelfRefAttr_"
          case Optional(_, bi) if bi.startsWith("BiOtherRef_")    => "BiOtherRefAttr_"
          case Optional(a, bi) if bi.startsWith("BiEdgeRef_")     => "BiEdgeRefAttr_[" + a + "[NS, NS]]"
          case Optional(_, bi) if bi.startsWith("BiEdgePropAttr") => "BiEdgePropAttr_"
          case Optional(_, bi) if bi.startsWith("BiEdgePropRef")  => "BiEdgePropRefAttr_"
          case Optional(a, bi) if bi.startsWith("BiTargetRef_")   => "BiTargetRefAttr_[" + a + "[NS, NS]]"
          case Optional(_, other)                                 => other
        }.mkString(" with ", " with ", "")
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")

      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
    }.mkString("\n  ").trim

    val nsArities = d.nss.map(ns => ns.ns -> ns.attrs.size).toMap

    val extraImports0 = attrs.collect {
      case Val(_, _, _, tpe, _, _, _) if tpe.take(4) == "java" => tpe
    }.distinct
    val extraImports = if (extraImports0.isEmpty) "" else extraImports0.mkString(s"\nimport ", "\nimport ", "")

    (inArity, outArity, Ns, attrs, ext, attrClasses, attrClassesOpt, nsArities, extraImports)
  }


  def namespaceBodies(d: Definition, namespace: Namespace): (String, Seq[(Int, String)]) = {

    val (inArity, outArity, ns, attrs, ext, attrClasses, attrClassesOpt, nsArities, extraImports) = resolveNs(d, namespace)

    val nsTraitsOut = (0 to outArity).map(nsTrait(namespace, 0, _, inArity, outArity, nsArities)).mkString("\n")
    val outFile: String =
      s"""/*
          |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$ns`
          |*
          |* To change:
          |* 1. edit schema definition file in `${d.pkg}.schema/`
          |* 2. `sbt compile` in terminal
          |* 3. Refresh and re-compile project in IDE
          |*/
          |package ${d.pkg}.dsl.${firstLow(d.domain)}
          |import molecule.dsl.actions._
          |import molecule.dsl._$extraImports
          |
          |
          |object $ns extends ${ns}_0 with FirstNS {
          |  def apply(e: Long): ${ns}_0 = ???
          |}
          |
          |trait $ns {
          |  $attrClasses
          |
          |  $attrClassesOpt
          |}
          |
          |$nsTraitsOut""".stripMargin

    val nsTraitsIn: Seq[(Int, String)] = if (inArity == 0) Nil
    else (1 to inArity).map(in =>
      (in, (0 to outArity).map(nsTrait(namespace, in, _, inArity, outArity, nsArities)).mkString("\n"))
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
            |package ${d.pkg}.dsl.${firstLow(d.domain)}
            |import molecule.dsl.actions._
            |import molecule.dsl._$extraImports
            |
            |$inTraits""".stripMargin

      (in, inFile)
    }

    (outFile, inFiles)
  }


  def namespaceBody(d: Definition, namespace: Namespace): String = {

    val (inArity, outArity, ns, attrs, ext, attrClasses, attrClassesOpt, nsArities, extraImports) = resolveNs(d, namespace)

    val nsTraits = (for {
      in <- 0 to inArity
      out <- 0 to outArity
    } yield nsTrait(namespace, in, out, inArity, outArity, nsArities)).mkString("\n")

    s"""/*
        |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$ns`
        |*
        |* To change:
        |* 1. edit schema definition file in `${d.pkg}.schema/`
        |* 2. `sbt compile` in terminal
        |* 3. Refresh and re-compile project in IDE
        |*/
        |package ${d.pkg}.dsl.${firstLow(d.domain)}
        |import molecule.dsl.actions._
        |import molecule.dsl._$extraImports
        |
        |
        |object $ns extends ${ns}_0 with FirstNS {
        |  def apply(e: Long): ${ns}_0 = ???
        |}
        |
        |trait $ns $ext{
        |  $attrClasses
        |
        |  $attrClassesOpt
        |}
        |
        |$nsTraits""".stripMargin
  }


  def apply(codeDir: File, managedDir: File, defDirs: Seq[String], separateInFiles: Boolean = false, allIndexed: Boolean = true): Seq[File] = {
    // Loop domain directories
    val files = defDirs flatMap { defDir =>
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

          d.nss.flatMap { ns =>
            val (outBody, inBodies) = namespaceBodies(d, ns)

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
          d.nss.map { ns =>
            val nsFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}.scala"
            val nsBody = namespaceBody(d, ns)
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
