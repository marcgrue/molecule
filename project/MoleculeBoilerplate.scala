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
    //    val path: String =
    raw.collectFirst {
      case r"package (.*)$p\..*" => p
    }.getOrElse {
      sys.error("Found no package statement in definition file")
    }

    // Check input/output arities
    //    val (inArity, outArity) =
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
    //    val domain =
    raw collect {
      case r"object (.*)${name}Definition"      => name
      case r"object (.*)${name}Definition \{"   => name
      case r"object (.*)${name}Definition \{\}" => name
    } match {
      case Nil                      => sys.error("Couldn't find definition object <domain>Definition in " + defFile.getName)
      case l: List[_] if l.size > 1 => sys.error(s"Only one definition object per definition file allowed. Found ${l.size}:" + l.mkString("\n - ", "Definition\n - ", "Definition"))
      case domainNameList           => firstLow(domainNameList.head)
    }


    // Parse ..........................................

    //    def parseOptions(str0: String, acc: Seq[Optional] = Nil, revNs: String = "", revRef0: String = ""): Seq[Optional] = {
    def parseOptions(str0: String, acc: Seq[Optional] = Nil): Seq[Optional] = {
      val indexed = Optional( """":db/index"             , true.asInstanceOf[Object]""", "Indexed")
      val options = str0 match {
        case r"\.doc\((.*)$msg\)(.*)$str" => parseOptions(str, acc :+ Optional( s"""":db/doc"               , $msg""", ""))
        case r"\.fullTextSearch(.*)$str"  => parseOptions(str, acc :+ Optional( """":db/fulltext"          , true.asInstanceOf[Object]""", "FulltextSearch[Ns, In]"))
        case r"\.uniqueValue(.*)$str"     => parseOptions(str, acc :+ Optional( """":db/unique"            , ":db.unique/value"""", "UniqueValue"))
        case r"\.uniqueIdentity(.*)$str"  => parseOptions(str, acc :+ Optional( """":db/unique"            , ":db.unique/identity"""", "UniqueIdentity"))
        case r"\.subComponents(.*)$str"   => parseOptions(str, acc :+ Optional( """":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent"))
        case r"\.subComponent(.*)$str"    => parseOptions(str, acc :+ Optional( """":db/isComponent"       , true.asInstanceOf[Object]""", "IsComponent"))
        case r"\.noHistory(.*)$str"       => parseOptions(str, acc :+ Optional( """":db/noHistory"         , true.asInstanceOf[Object]""", "NoHistory"))
        case r"\.indexed(.*)$str"         => parseOptions(str, acc :+ indexed)
        //        case r"\.bidirectional\.(.*)$revRef\.doc\((.*)$msg\)" => parseOptions(s".doc($msg)", acc :+ Optional(revNs, s"Bidirectional[$revRef"))
        //        case r"\.bidirectional\.doc\((.*)$msg\)"              => parseOptions(s".doc($msg)", acc :+ Optional(revNs, s"Bidirectional[$revRef0"))
        //        case r"\.bidirectional\.(.*)$revRef"                  => parseOptions("", acc :+ Optional(revNs, s"Bidirectional[$revRef"))
        //        case r"\.bidirectional"                               => parseOptions("", acc :+ Optional(revNs, s"Bidirectional[$revRef0"))
        case r"\.bidirectional(.*)$str" => parseOptions(str, acc :+ Optional("", "Bidirectional"))
        case ""                         => acc
        case unexpected                 => sys.error(s"Unexpected options code in ${defFile.getName}:\n" + unexpected)
      }
      if (allIndexed) (options :+ indexed).distinct else options
    }

    def parseAttr(backTics: Boolean, attrClean: String, str: String, curPart: String): Seq[Attr] = {
      val attr = if (backTics) s"`$attrClean`" else attrClean
      val attrK = attrClean + "K"
      str match {
        case r"oneString(.*)$str"  => Seq(Val(attr, attrClean, "OneString", "String", "", "string", parseOptions(str)))
        case r"oneByte(.*)$str"    => Seq(Val(attr, attrClean, "OneByte", "Byte", "", "byte", parseOptions(str)))
        case r"oneShort(.*)$str"   => Seq(Val(attr, attrClean, "OneShort", "Short", "", "short", parseOptions(str)))
        case r"oneInt(.*)$str"     => Seq(Val(attr, attrClean, "OneInt", "Int", "", "long", parseOptions(str)))
        case r"oneLong(.*)$str"    => Seq(Val(attr, attrClean, "OneLong", "Long", "", "long", parseOptions(str)))
        case r"oneFloat(.*)$str"   => Seq(Val(attr, attrClean, "OneFloat", "Float", "", "double", parseOptions(str)))
        case r"oneDouble(.*)$str"  => Seq(Val(attr, attrClean, "OneDouble", "Double", "", "double", parseOptions(str)))
        case r"oneBoolean(.*)$str" => Seq(Val(attr, attrClean, "OneBoolean", "Boolean", "", "boolean", parseOptions(str)))
        case r"oneDate(.*)$str"    => Seq(Val(attr, attrClean, "OneDate", "java.util.Date", "", "instant", parseOptions(str)))
        case r"oneUUID(.*)$str"    => Seq(Val(attr, attrClean, "OneUUID", "java.util.UUID", "", "uuid", parseOptions(str)))
        case r"oneURI(.*)$str"     => Seq(Val(attr, attrClean, "OneURI", "java.net.URI", "", "uri", parseOptions(str)))

        case r"manyString(.*)$str"  => Seq(Val(attr, attrClean, "ManyString", "Set[String]", "String", "string", parseOptions(str)))
        case r"manyInt(.*)$str"     => Seq(Val(attr, attrClean, "ManyInt", "Set[Int]", "Int", "long", parseOptions(str)))
        case r"manyLong(.*)$str"    => Seq(Val(attr, attrClean, "ManyLong", "Set[Long]", "Long", "long", parseOptions(str)))
        case r"manyFloat(.*)$str"   => Seq(Val(attr, attrClean, "ManyFloat", "Set[Float]", "Float", "double", parseOptions(str)))
        case r"manyDouble(.*)$str"  => Seq(Val(attr, attrClean, "ManyDouble", "Set[Double]", "Double", "double", parseOptions(str)))
        case r"manyBoolean(.*)$str" => Seq(Val(attr, attrClean, "ManyBoolean", "Set[Boolean]", "Boolean", "boolean", parseOptions(str)))
        case r"manyDate(.*)$str"    => Seq(Val(attr, attrClean, "ManyDate", "Set[java.util.Date]", "java.util.Date", "instant", parseOptions(str)))
        case r"manyUUID(.*)$str"    => Seq(Val(attr, attrClean, "ManyUUID", "Set[java.util.UUID]", "java.util.UUID", "uuid", parseOptions(str)))
        case r"manyURI(.*)$str"     => Seq(Val(attr, attrClean, "ManyURI", "Set[java.net.URI]", "java.net.URI", "uri", parseOptions(str)))

        case r"mapString(.*)$str"  => Seq(
          Val(attr, attrClean, "MapString", "Map[String, String]", "String", "string", parseOptions(str)),
          Val(attrK, attrK, "OneString", "String", "K", "string", parseOptions(str)))
        case r"mapInt(.*)$str"     => Seq(
          Val(attr, attrClean, "MapInt", "Map[String, Int]", "Int", "string", parseOptions(str)),
          Val(attrK, attrK, "OneInt", "Int", "K", "long", parseOptions(str)))
        case r"mapLong(.*)$str"    => Seq(
          Val(attr, attrClean, "MapLong", "Map[String, Long]", "Long", "string", parseOptions(str)),
          Val(attrK, attrK, "OneLong", "Long", "K", "long", parseOptions(str)))
        case r"mapFloat(.*)$str"   => Seq(
          Val(attr, attrClean, "MapFloat", "Map[String, Float]", "Float", "string", parseOptions(str)),
          Val(attrK, attrK, "OneFloat", "Float", "K", "double", parseOptions(str)))
        case r"mapDouble(.*)$str"  => Seq(
          Val(attr, attrClean, "MapDouble", "Map[String, Double]", "Double", "string", parseOptions(str)),
          Val(attrK, attrK, "OneDouble", "Double", "K", "double", parseOptions(str)))
        case r"mapBoolean(.*)$str" => Seq(
          Val(attr, attrClean, "MapBoolean", "Map[String, Boolean]", "Boolean", "string", parseOptions(str)),
          Val(attrK, attrK, "OneBoolean", "Boolean", "K", "boolean", parseOptions(str)))
        case r"mapDate(.*)$str"    => Seq(
          Val(attr, attrClean, "MapDate", "Map[String, java.util.Date]", "java.util.Date", "string", parseOptions(str)),
          Val(attrK, attrK, "OneDate", "java.util.Date", "K", "instant", parseOptions(str)))
        case r"mapUUID(.*)$str"    => Seq(
          Val(attr, attrClean, "MapUUID", "Map[String, java.util.UUID]", "java.util.UUID", "string", parseOptions(str)),
          Val(attrK, attrK, "OneUUID", "java.util.UUID", "K", "uuid", parseOptions(str)))
        case r"mapURI(.*)$str"     => Seq(
          Val(attr, attrClean, "MapURI", "Map[String, java.net.URI]", "java.net.URI", "string", parseOptions(str)),
          Val(attrK, attrK, "OneURI", "java.net.URI", "K", "uri", parseOptions(str)))

        case r"oneEnum\((.*?)$enums\)(.*)$str"  => Seq(Enum(attr, attrClean, "OneEnum", "String", "", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str)))
        case r"manyEnum\((.*?)$enums\)(.*)$str" => Seq(Enum(attr, attrClean, "ManyEnums", "Set[String]", "String", enums.replaceAll("'", "").split(",").toList.map(_.trim), parseOptions(str)))

        case r"one\[\w*Definition\.([a-z].*)$partref\](.*)$str"  => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", partref.replace(".", "_"), parseOptions(str, Nil)))
        case r"one\[([a-z].*)$partref\](.*)$str"                 => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", partref.replace(".", "_"), parseOptions(str, Nil)))
        case r"one\[(.*)$ref\](.*)$str" if curPart.isEmpty       => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", ref, parseOptions(str, Nil)))
        case r"one\[(.*)$ref\](.*)$str"                          => Seq(Ref(attr, attrClean, "OneRefAttr", "OneRef", "Long", "", curPart + "_" + ref, parseOptions(str, Nil)))
        case r"many\[\w*Definition\.([a-z].*)$partref\](.*)$str" => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", partref.replace(".", "_"), parseOptions(str, Nil)))
        case r"many\[([a-z].*)$partref\](.*)$str"                => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", partref.replace(".", "_"), parseOptions(str, Nil)))
        case r"many\[(.*)$ref\](.*)$str" if curPart.isEmpty      => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", ref, parseOptions(str, Nil)))
        case r"many\[(.*)$ref\](.*)$str"                         => Seq(Ref(attr, attrClean, "ManyRefAttr", "ManyRef", "Set[Long]", "Long", curPart + "_" + ref, parseOptions(str, Nil)))

        case unexpected => sys.error(s"Unexpected attribute code in ${defFile.getName}:\n" + unexpected)
      }
    }

    val definition: Definition = raw.foldLeft(Definition("", Seq(), -1, -1, "", "", Seq())) {
      case (d, line) => line.trim match {
        case r"\/\/.*" /* comments allowed */                 => d
        case r"package (.*)$path\.[\w]*"                      => d.copy(pkg = path)
        case "import molecule.dsl.schemaDefinition._"         => d
        case r"@InOut\((\d+)$inS, (\d+)$outS\)"               => d.copy(in = inS.toString.toInt, out = outS.toString.toInt)
        case r"object (.*)${dmn}Definition \{"                => d.copy(domain = dmn)
        case r"object ([a-z]*)$part\s*\{"                     => d.copy(curPart = part)
        case r"object (\w*)$part\s*\{"                        => sys.error(s"Unexpected partition name '$part' in ${defFile.getName}. Only small letters (a-z) allowed in a partition name.")
        case r"trait ([A-Z]\w*)$ns\s*\{"                      => {
          val partns = if (d.curPart.isEmpty) ns else d.curPart + "_" + ns
          // prepend partition to its namespaces
          d.copy(nss = d.nss :+ Namespace(d.curPart, partns))
        }
        case r"trait (\w*)$ns\s*\{"                           => sys.error(s"Unexpected namespace name '$ns' in ${defFile.getName}. Namespaces have to start with a capital letter [A-Z].")
        case r"val\s*(\`?)$q1(\w*)$a(\`?)$q2\s*\=\s*(.*)$str" => d.addAttr(parseAttr(q1.nonEmpty, a, str, d.curPart))
        case "}"                                              => d
        case ""                                               => d
        case unexpected                                       => sys.error(s"Unexpected definition code in ${defFile.getName}:\n" + unexpected)
      }
    }

    definition
  }

  def resolve(definition: Definition) = {
    val updatedNss2 = definition.nss.foldLeft(definition.nss) { case (updatedNss0, curNs) =>
      val updatedNss1 = resolveBidrectionals(definition.domain, updatedNss0, curNs)
      addBackRefs(updatedNss1, curNs)
    }
    val updatedNss3 = addReverseRefs(definition.domain, updatedNss2)
    definition.copy(nss = updatedNss3)
  }

  def addReverseRefs(domain: String, nss: Seq[Namespace]): Seq[Namespace] = {
    val error = s"Error in ${domain}Definition:"
    //    println(s"--------- $domain ---------")

    // Update reverse refs in other namespaces

    // Gather OneRefs (ManyRefs are treated as nested data structures)
    val revRefMap = nss.flatMap(_.attrs.collect {
      case bidirectRef@Ref(attr, _, _, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("Bidirectional")) =>
        opts.collectFirst {
          case Optional(revRef, clazz) if clazz.startsWith("Bidirectional") =>
            val bidirectParts = clazz.split(':').toList.tail
            val revRefParts = revRef.split('.').toList
            (revRefParts.head -> revRefParts.last) -> s"${bidirectParts.head}.${bidirectParts.last}"
        } get
    }).toMap

    nss.map { ns =>
      val attrs2 = ns.attrs map {
        case oneRef@Ref(revRef, attrClean, _, _, _, "", _, opts) if revRefMap.keys.toList.contains((ns.ns, revRef)) =>
          // Extra check that we are not overwriting a bidirectional definition
          if (opts.exists(_.clazz.startsWith("Bidirectional")))
            sys.error(
              s"""$error
                 |Unexpected reverse ref in optionals:
                 |  ${opts.mkString("\n  ")}
                """.stripMargin)

          val bidirectRef = revRefMap((ns.ns, revRef))
          val reverseRefOptional = Optional(bidirectRef, s"ReverseRef:$revRef")
          oneRef.copy(options = opts :+ reverseRefOptional)

        case otherAttr => otherAttr
      }
      ns.copy(attrs = attrs2)
    }
  }

  def resolveBidrectionals(domain: String, updatedNss0: Seq[Namespace], curNs: Namespace): Seq[Namespace] = {

    val nsLower = firstLow(curNs.ns)
    val error = s"Error in ${domain}Definition:"

    //    println(s"--------- $domain ---------")
    //    println("curNs: " + curNs.ns)

    // Resolve bidirectional refs

    val attrs1 = curNs.attrs map {

      // Bidirectional ref (val knows = one[Person]) pointing to vertex namespace itself
      case bidirectRef@Ref(bidirectAttr, _, _, _, _, _, revNs, opts) if revNs == curNs.ns && opts.exists(_.clazz == "Bidirectional") => {

        val newOpts = opts map {
          case bidirectOps if bidirectOps.clazz == "Bidirectional"          =>
            bidirectOps.copy(datomicKeyValue = s"$bidirectAttr", clazz = s"Bidirectional:$bidirectAttr")
          case bidirectOps if bidirectOps.clazz.startsWith("Bidirectional") => sys.error(
            s"""$error
               |Unexpected bidirectional ref definition:
               |$bidirectOps
                 """.stripMargin)
          case otherOps                                                     => otherOps
        }

        //        println("A bidirectRef: " + bidirectRef)
        //        println("A revNs: " + revNs)
        //        println("A newOpts: " + newOpts)

        bidirectRef.copy(options = newOpts)
      }

      // Bidirectional ref (val knows = one[Knows]) pointing to property edge namespace
      case bidirectRef@Ref(bidirectAttr, _, _, _, _, _, revNs, opts) if opts.exists(_.clazz == "Bidirectional") => {

        //                println("bidirectRef: " + bidirectRef)
        //        println("revNs: " + revNs)

        val revRef = updatedNss0 collectFirst {

          case ns_ if revNs == ns_.ns => {

            val reverseRefs = ns_.attrs collect {

              // Reverse ref pointing back to vertex namespace
              case reverseRef@Ref(attr, _, _, _, _, _, revNs2, opts2) if revNs2 == curNs.ns =>
                // Only define bidirection in vertex namespace
                opts2 collectFirst {
                  case bidirectOps if bidirectOps.clazz.startsWith("Bidirectional") => sys.error(
                    s"""$error
                       |Add the `bidirectional` only to the ref in the "vertex namespace" `${curNs.ns}`
                       |that points to the edge namespace `$revNs`.
                       |In the edge namespace `$revNs` simply omit the `bidirectional` option:
                       |  val $attr = one[${curNs.ns}]
                         """.stripMargin)
                }
                reverseRef

              // Other refs
              case otherRef@Ref(attr, _, _, _, _, baseTpe, refNs, opts2) => {
                val card = if (baseTpe.isEmpty) "one" else "many"
                sys.error(
                  s"""$error
                     |"Property edge" namespace `$revNs` is only allowed to have reverse references
                     |pointing back to namespace `${curNs.ns}` (like `val $nsLower = one[${curNs.ns}]`).
                     |Found un-allowed reference in property edge namespace `$revNs`:
                     |  val $attr = $card[$refNs]
                      """.stripMargin)
              }
            }

            // Expect only 1 reverse ref
            val reverseRef = reverseRefs.length match {
              case 0          => sys.error(
                s"""$error
                   |Can't find  reverse ref in namespace `$revNs` pointing back to `${curNs.ns}`.
                   |Expecting something like:
                   |  val $nsLower = one[${curNs.ns}]
                     """.stripMargin)
              case n if n > 1 =>
                val refs = reverseRefs.map { r =>
                  val card = if (r.baseTpe.isEmpty) "one" else "many"
                  s"val ${r.attr} = $card[${curNs.ns}]"
                }.mkString("\n  ")
                sys.error(
                  s"""$error
                     |Can only have one reverse ref in namespace `$revNs` pointing back to `${curNs.ns}`.
                     |Found $n reverse references pointing back to `${curNs.ns}`:
                     |  $refs
                     |Expected something like:
                     |  val $nsLower = one[${curNs.ns}]
                     """.stripMargin)
              case 1          => reverseRefs.head
            }


            // No edge namespace without edge properties
            if (ns_.attrs.collect { case v: Val => v case e: Enum => e }.isEmpty)
              sys.error(
                s"""$error
                   |Edge namespaces like `$revNs` should have at least 1 edge property (attribute/enum) defined.
                   |If no edge properties are needed, you should skip the separate namespace `$revNs` and simply
                   |only have a bidirectional self-reference in `${curNs.ns}`, like:
                   |  val ${firstLow(revNs)} = one[${curNs.ns}]
                       """.stripMargin)


            //            println("reverseRef : " + reverseRef)

            // Expect only card-one reverse ref
            reverseRef match {
              case Ref(attr_, _, _, _, _, baseTpe, _, _) if baseTpe.nonEmpty =>
                sys.error(
                  s"""$error
                     |Reverse ref `$attr_` in namespace `$revNs` is expected
                     |to be a cardinality one ref. Please change to:
                     |  val $attr_ = one[${curNs.ns}]
                       """.stripMargin)
              case revRef                                                    => revRef.attr
            }
          }
        } get

        //        println("reverseRefName: " + revRef)

        val newOpts = opts map {
          case bidirectOps if bidirectOps.clazz == "Bidirectional"          =>
            bidirectOps.copy(datomicKeyValue = s"$revNs.$revRef", clazz = s"Bidirectional:${curNs.ns}:$bidirectAttr")
          case bidirectOps if bidirectOps.clazz.startsWith("Bidirectional") => sys.error(
            s"""$error
               |Unexpected bidirectional ref definition:
               |$bidirectOps
                 """.stripMargin)
          case otherOps                                                     => otherOps
        }

        bidirectRef.copy(options = newOpts)
      }

      case otherAttr => otherAttr
    }

    // Update bidirectional attributes
    val nssUpdated1 = updatedNss0.map {
      case definingNs if definingNs == curNs => curNs.copy(attrs = attrs1)
      case otherNs                           => otherNs
    }

    nssUpdated1
  }

  def addBackRefs(nss: Seq[Namespace], curNs: Namespace): Seq[Namespace] = {
    // Gather OneRefs (ManyRefs are treated as nested data structures)
    val refs1 = curNs.attrs.collect {
      case ref@Ref(_, refAttr, clazz, _, _, _, refNs, _) => refNs -> ref
    }.toMap

    nss.map {
      case ns2 if refs1.nonEmpty && refs1.keys.toList.contains(ns2.ns) => {
        val attrs2 = refs1.foldLeft(ns2.attrs) { case (attrs, ref) =>
          val Ref(_, refAttr, clazz, _, tpe, _, _, _) = ref._2
          val cleanNs = if (curNs.ns.contains('_')) curNs.ns.split("_").tail.head else curNs.ns
          val backRef = BackRef(s"_$cleanNs", curNs.ns, "BackRefAttr", "BackRef", tpe, "", "") // todo: check not to backreference same-named namespaces in different partitions
          // Exclude self-references (?)
          if (curNs.ns == ns2.ns) attrs else attrs :+ backRef
        }.distinct
        ns2.copy(attrs = attrs2)
      }
      case ns2                                                         => ns2
    }
  }


  // Generate ..........................................

  def schemaBody(d: Definition) = {

    def resolveOptions(options: Seq[Optional]) = options flatMap {
      case Optional(_, clazz) if clazz.startsWith("Bidirectional") => None
      case Optional(_, clazz) if clazz.startsWith("ReverseRef")    => None
      case optional                                                => Some(optional.datomicKeyValue)
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

    val partitions = {
      val ps = d.nss.map(_.part).filter(_.nonEmpty).distinct.map { p =>
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

    val stmts = d.nss map { ns =>
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
        |* AUTO-GENERATED CODE - DON'T CHANGE!
        |*
        |* Manual changes to this file will likely break schema creations!
        |* Instead, change the molecule definition files and recompile your project with `sbt compile`
        |*/
        |package ${d.pkg}.schema
        |import molecule.dsl.Transaction
        |import datomic.{Util, Peer}
        |
        |object ${d.domain}Schema extends Transaction {
        |  $partitions
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

    val maxAttr0 = attrs.map(_.attr.length).max
    val refCode = attrs.foldLeft(Seq("")) {
      case (acc, Ref(attr, attrClean, _, clazz2, _, baseType, refNs, opts)) => {
        val p1 = padS(maxAttr0, attr)
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
          case Optional(_, clazz) if clazz.startsWith("Bidirectional:") =>
            val bidirectRef = clazz.split(':').last
            s"with Bidirectional[$bidirectRef[NS, NS]] "
          case Optional(_, clazz) if clazz.startsWith("ReverseRef:")    =>
            val reverseRef = clazz.split(':').last
            s"with ReverseRef[$reverseRef[NS, NS]] "
        } getOrElse ""
        acc :+ s"def ${attrClean.capitalize} $p1: $clazz2$p2[$ns, $refNs$p3] with $ref $birectional= ???"
      }
      case (acc, BackRef(backAttr, backRef, _, _, _, _, _, _))              =>
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

  //  def namespaceBodies(d: Definition, namespace: Namespace): (String, Seq[(Int, String)]) = {
  //    val inArity = d.in
  //    val outArity = d.out
  //    val Ns = namespace.ns
  //    val attrs = namespace.attrs
  //    val p1 = (s: String) => padS(attrs.map(_.attr).filter(!_.startsWith("_")).map(_.length).max, s)
  //    val p2 = (s: String) => padS(attrs.map(_.clazz).filter(!_.startsWith("Back")).map(_.length).max, s)
  //    def mapType(s: String) = s match {
  //      case "java.util.Date" => "Date"
  //      case "java.util.UUID" => "UUID"
  //      case "java.net.URI"   => "URI"
  //      case other            => other
  //    }
  //
  //    val attrClasses = attrs.flatMap {
  //      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, options) if tpe.take(3) == "Map" =>
  //        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
  //        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")
  //
  //      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, options) if baseTpe == "K" =>
  //        val options2 = options :+ Optional("", "MapAttrK")
  //        val extensions = " with " + options2.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
  //        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")
  //
  //      case Val(attr, _, clazz, _, _, _, options) =>
  //        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
  //        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")
  //
  //      case Enum(attr, _, clazz, _, _, enums, _) =>
  //        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
  //        Seq( s"""class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In] { $enumValues }""")
  //
  //      case Ref(attr, _, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("Bidirectional")) =>
  //        val (revNs, revRef) = opts.collectFirst {
  //          case Optional(revNs0, revRef0) if revRef0.startsWith("Bidirectional") => (revNs0, revRef0.split(':').last)
  //        }.get
  //        val bidirectAttr = if (revNs == Ns) revRef else s"$revNs.$revRef"
  //        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In] with Bidirectional[$bidirectAttr[Ns, In]]")
  //
  //      case Ref(attr, _, clazz, _, _, _, _, _) =>
  //        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]")
  //
  //      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
  //    }.mkString("\n  ").trim
  //
  //    val attrClassesOpt = attrs.flatMap {
  //      case Val(attr, attrClean, clazz, tpe, baseTpe, _, options) if tpe.take(3) == "Map" =>
  //        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
  //        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")
  //
  //      case Val(attr, attrClean, clazz, _, _, _, options) =>
  //        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
  //        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")
  //
  //      case Enum(attr, attrClean, clazz, _, _, enums, _) =>
  //        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
  //        Seq( s"""class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)} { $enumValues }""")
  //
  //      case Ref(attr, attrClean, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("Bidirectional")) =>
  //        val (revNs, revRef) = opts.collectFirst { case Optional(revNs0, revRef0) if revRef0.startsWith("Bidirectional") => (revNs0, revRef0.split(':').last) }.get
  //        val bidirectAttr = if (revNs == Ns) revRef else s"$revNs.$revRef"
  //        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)} with Bidirectional[$bidirectAttr[Ns, In]]")
  //
  //      case Ref(attr, attrClean, clazz, _, _, _, _, _) =>
  //        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}")
  //
  //      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
  //    }.mkString("\n  ").trim
  //
  //    val nsArities = d.nss.map(ns => ns.ns -> ns.attrs.size).toMap
  //
  //    val extraImports0 = attrs.collect {
  //      case Val(_, _, _, tpe, _, _, _) if tpe.take(4) == "java" => tpe
  //    }.distinct
  //    val extraImports = if (extraImports0.isEmpty) "" else extraImports0.mkString(s"\nimport ", "\nimport ", "")
  //
  //    val nsTraitsOut = (0 to outArity).map(nsTrait(namespace, 0, _, inArity, outArity, nsArities)).mkString("\n")
  //    val outFile: String =
  //      s"""/*
  //          |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$Ns`
  //          |*
  //          |* To change:
  //          |* 1. edit schema definition file in `${d.pkg}.schema/`
  //          |* 2. `sbt compile` in terminal
  //          |* 3. Refresh and re-compile project in IDE
  //          |*/
  //          |package ${d.pkg}.dsl.${firstLow(d.domain)}
  //          |import molecule.dsl.schemaDSL._
  //          |import molecule.dsl._$extraImports
  //          |
  //          |
  //          |object $Ns extends ${Ns}_0 with FirstNS {
  //          |  def apply(e: Long): ${Ns}_0 = ???
  //          |}
  //          |
  //          |trait $Ns {
  //          |  $attrClasses
  //          |
  //          |  $attrClassesOpt
  //          |}
  //          |
  //          |$nsTraitsOut""".stripMargin
  //
  //    val nsTraitsIn: Seq[(Int, String)] = if (inArity == 0) Nil
  //    else (1 to inArity).map(in =>
  //      (in, (0 to outArity).map(nsTrait(namespace, in, _, inArity, outArity, nsArities)).mkString("\n"))
  //    )
  //    val inFiles: Seq[(Int, String)] = nsTraitsIn.map { case (in, inTraits) =>
  //      val inFile: String =
  //        s"""/*
  //            |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$Ns`
  //            |*
  //            |* To change:
  //            |* 1. edit schema definition file in `${d.pkg}.schema/`
  //            |* 2. `sbt compile` in terminal
  //            |* 3. Refresh and re-compile project in IDE
  //            |*/
  //            |package ${d.pkg}.dsl.${firstLow(d.domain)}
  //            |import molecule.dsl.schemaDSL._
  //            |import molecule.dsl._$extraImports
  //            |
  //            |$inTraits""".stripMargin
  //
  //      (in, inFile)
  //    }
  //
  //    (outFile, inFiles)
  //  }

  def namespaceBody(d: Definition, namespace: Namespace): String = {
    val inArity = d.in
    val outArity = d.out
    val Ns = namespace.ns
    val attrs = namespace.attrs
    val p1 = (s: String) => padS(attrs.map(_.attr).filter(!_.startsWith("_")).map(_.length).max, s)
    val p2 = (s: String) => padS(attrs.map(_.clazz).filter(!_.startsWith("Back")).map(_.length).max, s)
    def mapType(s: String) = s match {
      case "java.util.Date" => "Date"
      case "java.util.UUID" => "UUID"
      case "java.net.URI"   => "URI"
      case other            => other
    }

    val attrClasses = attrs.flatMap {
      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, options) if tpe.take(3) == "Map" =>
        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Val(attr, _, clazz, tpe, baseTpe, datomicTpe, options) if baseTpe == "K" =>
        val options2 = options :+ Optional("", "MapAttrK")
        val extensions = " with " + options2.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Val(attr, _, clazz, _, _, _, options) =>
        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]$extensions")

      case Enum(attr, _, clazz, _, _, enums, _) =>
        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
        Seq( s"""class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In] { $enumValues }""")

      case Ref(attr, _, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("ReverseRef")) =>
        val bidirectAttr = opts.collectFirst {
          case Optional(bidirectAttr0, refRef) if refRef.startsWith("ReverseRef") => bidirectAttr0
        }.get
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In] with ReverseRef[$bidirectAttr[NS, NS]]")

      case Ref(attr, _, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("Bidirectional")) =>
        val revRef = opts.collectFirst {
          case Optional(revRef0, bidirectRef) if bidirectRef.startsWith("Bidirectional") => revRef0
        }.get
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In] with Bidirectional[$revRef[NS, NS]]")

      case Ref(attr, _, clazz, _, _, _, _, _) =>
        Seq(s"class $attr${p1(attr)}[Ns, In] extends $clazz${p2(clazz)}[Ns, In]")

      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
    }.mkString("\n  ").trim

    val attrClassesOpt = attrs.flatMap {
      case Val(attr, attrClean, clazz, tpe, baseTpe, _, options) if tpe.take(3) == "Map" =>
        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")

      case Val(attr, attrClean, clazz, _, _, _, options) =>
        val extensions = if (options.isEmpty) "" else " with " + options.filter(_.clazz.nonEmpty).map(_.clazz).mkString(" with ")
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}$extensions")

      case Enum(attr, attrClean, clazz, _, _, enums, _) =>
        val enumValues = s"private lazy val ${enums.mkString(", ")} = EnumValue"
        Seq( s"""class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)} { $enumValues }""")

      case Ref(attr, attrClean, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("ReverseRef")) =>
        val bidirectAttr = opts.collectFirst {
          case Optional(bidirectAttr0, refRef) if refRef.startsWith("ReverseRef") => bidirectAttr0
        }.get
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)} with ReverseRef[$bidirectAttr[NS, NS]]")

      case Ref(attr, attrClean, clazz, _, _, _, _, opts) if opts.exists(_.clazz.startsWith("Bidirectional")) =>
        val revRef = opts.collectFirst {
          case Optional(revRef0, bidirectRef) if bidirectRef.startsWith("Bidirectional") => revRef0
        }.get
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)} with Bidirectional[$revRef[NS, NS]]")

      case Ref(attr, attrClean, clazz, _, _, _, _, _) =>
        Seq(s"class $attrClean$$${p1(attrClean)}[Ns, In] extends $clazz$$${p2(clazz)}")

      case BackRef(backAttr, _, clazz, _, _, _, _, _) => Nil
    }.mkString("\n  ").trim

    val nsArities = d.nss.map(ns => ns.ns -> ns.attrs.size).toMap

    val nsTraits = (for {
      in <- 0 to inArity
      out <- 0 to outArity
    } yield nsTrait(namespace, in, out, inArity, outArity, nsArities)).mkString("\n")

    val extraImports0 = attrs.collect {
      case Val(_, _, _, tpe, _, _, _) if tpe.take(4) == "java" => tpe
    }.distinct
    val extraImports = if (extraImports0.isEmpty) "" else extraImports0.mkString(s"\nimport ", "\nimport ", "")

    s"""/*
        |* AUTO-GENERATED Molecule DSL boilerplate code for namespace `$Ns`
        |*
        |* To change:
        |* 1. edit schema definition file in `${d.pkg}.schema/`
        |* 2. `sbt compile` in terminal
        |* 3. Refresh and re-compile project in IDE
        |*/
        |package ${d.pkg}.dsl.${firstLow(d.domain)}
        |import molecule.dsl.schemaDSL._
        |import molecule.dsl._$extraImports
        |
        |
        |object $Ns extends ${Ns}_0 with FirstNS {
        |  def apply(e: Long): ${Ns}_0 = ???
        |}
        |
        |trait $Ns {
        |  $attrClasses
        |
        |  $attrClassesOpt
        |}
        |
        |$nsTraits""".stripMargin
  }

  def apply(codeDir: File, managedDir: File, defDirs: Seq[String], allIndexed: Boolean = true): Seq[File] = {
    // Loop domain directories
    val files = defDirs flatMap { defDir =>
      val definitionFiles = sbt.IO.listFiles(codeDir / defDir / "schema").filter(f => f.isFile && f.getName.endsWith("Definition.scala"))
      assert(definitionFiles.nonEmpty, "\nFound no definition files in path: " + codeDir / defDir +
        "\nOBS: Remember, schema definition file names should end with `<YourDomain...>Definition.scala`")

      // Loop definition files in each domain directory
      definitionFiles flatMap { definitionFile =>
        val d0 = parse(definitionFile, allIndexed)
        val d = resolve(d0)

        // Write schema file
        val schemaFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "schema" / s"${d.domain}Schema.scala"
        IO.write(schemaFile, schemaBody(d))

        //        // Write namespace files
        //        val namespaceFiles = d.nss.flatMap { ns =>
        //          val (outBody, inBodies) = namespaceBodies(d, ns)
        //          val outFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}.scala"
        //          IO.write(outFile, outBody)
        //
        //          val inFiles = inBodies.map { case (i, inBody) =>
        //            val inFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}_in$i.scala"
        //            IO.write(inFile, inBody)
        //            inFile
        //          }
        //          outFile +: inFiles
        //        }

        // Write namespace files
        val namespaceFiles = d.nss.map { ns =>
          val nsFile: File = d.pkg.split('.').toList.foldLeft(managedDir)((dir, pkg) => dir / pkg) / "dsl" / firstLow(d.domain) / s"${ns.ns}.scala"
          val nsBody = namespaceBody(d, ns)
          IO.write(nsFile, nsBody)
          nsFile
        }

        schemaFile +: namespaceFiles
      }
    }
    files
  }
}
