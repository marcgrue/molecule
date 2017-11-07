package molecule.factory

import java.lang.{Boolean => jBoolean, Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.net.URI
import java.util.{Date, UUID, List => jList, Map => jMap}

import clojure.lang.{Keyword, LazySeq, PersistentHashSet, PersistentVector}
import molecule.ast.model._
import molecule.ast.query._

import scala.collection.JavaConverters._


case class JsonBaseNested(modelE: Model, queryE: Query) {

  val jsEscapeChars: Set[Char] =
    List(('\u00ad', '\u00ad'),
      ('\u0600', '\u0604'),
      ('\u070f', '\u070f'),
      ('\u17b4', '\u17b5'),
      ('\u200c', '\u200f'),
      ('\u2028', '\u202f'),
      ('\u2060', '\u206f'),
      ('\ufeff', '\ufeff'),
      ('\ufff0', '\uffff'))
      .foldLeft(Set[Char]()) {
        case (set, (start, end)) =>
          set ++ (start to end).toSet
      }

  def appendEscapedString(buf: StringBuilder, s: String): Unit = {
    s.foreach { c =>
      val strReplacement = c match {
        case '"'  => "\\\""
        case '\\' => "\\\\"
        case '\b' => "\\b"
        case '\f' => "\\f"
        case '\n' => "\\n"
        case '\r' => "\\r"
        case '\t' => "\\t"
        // Set.contains will cause boxing of c to Character, try and avoid this
        case c if (c >= '\u0000' && c < '\u0020') || jsEscapeChars.contains(c) =>
          "\\u%04x".format(c: Int)

        case _ => ""
      }

      // Use Char version of append if we can, as it's cheaper.
      if (strReplacement.isEmpty) {
        buf.append(c)
      } else {
        buf.append(strReplacement)
      }
    }
  }

  def quote(buf: StringBuilder, s: String): Unit = {
    buf.append('"') //open quote
    appendEscapedString(buf, s)
    buf.append('"') //close quote
  }

  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

  def fieldValue(buf: StringBuilder, field0: String, tpe: String, card: Int, value: Any) = {
    val optional = field0.endsWith("$")
    val field = if (optional) field0.init else field0

    quote(buf, field)
    buf.append(": ")

    // Add value to buffer
    tpe match {
      case r"Map\[String,(.*)$t\]" if optional => jsonOptionMap(buf, value, t)
      case r"Set\[(.*)$t\]" if optional         => jsonOptionSet(buf, value, t)

      case t if optional && card == 3           => jsonOptionMap(buf, value, t)
      case t if optional && card == 2           => jsonOptionSet(buf, value, t)
      case t if optional                        => jsonOptionValue(buf, value, t)

      case r"Map\[String,(.*)$t\]" => jsonMap(buf, value, t)
      case r"Vector\[(.*)$t\]"      => jsonVector(buf, value, t)
      case r"Stream\[(.*)$t\]"      => jsonStream(buf, value, t)
      case r"Set\[(.*)$t\]"         => jsonSet(buf, value, t)

      case t if card == 3 => jsonMap(buf, value, t)
      case t if card == 2 => jsonSet(buf, value, t)
      case t              => jsonValue(buf, value, t)
    }
  }


  def jsonValue(buf: StringBuilder, value: Any, tpe: String) = tpe match {
    case "String"         => quote(buf, value.toString)
    case "Int"            => buf.append(value)
    case "Float"          => buf.append(value)
    case "java.util.Date" => quote(buf, df.format(value.asInstanceOf[Date]))
    case "java.util.UUID" => quote(buf, value.toString)
    case "java.net.URI"   => quote(buf, value.toString)
    case "Boolean"        => buf.append(value.toString)
    case number           => buf.append(value.toString)
  }

  def renderArray(buf: StringBuilder, values: Seq[Any], quoting: Boolean) = {
    var firstInArray = true
    buf.append("[")
    values.foreach { value =>
      if (firstInArray) {
        firstInArray = false
      } else {
        buf.append(", ")
      }
      if (quoting)
        quote(buf, value.toString)
      else
        buf.append(value.toString)
    }
    buf.append("]")
  }

  def jsonSet(buf: StringBuilder, value: Any, tpe: String) = {
    val values = value.asInstanceOf[PersistentHashSet].asScala.toSeq
    tpe match {
      case "String"         => renderArray(buf, values, true)
      case "java.util.Date" => renderArray(buf, values.map(v => df.format(v.asInstanceOf[Date])), true)
      case "java.util.UUID" => renderArray(buf, values, true)
      case "java.net.URI"   => renderArray(buf, values, true)
      case "Boolean"        => renderArray(buf, values, false)
      case _                => renderArray(buf, values, false)
    }
  }

  def jsonStream(buf: StringBuilder, value: Any, tpe: String) = {
    val values = value.asInstanceOf[LazySeq].asScala
    tpe match {
      case "String"         => renderArray(buf, values, true)
      case "java.util.Date" => renderArray(buf, values.map(v => df.format(v.asInstanceOf[Date])), true)
      case "java.util.UUID" => renderArray(buf, values, true)
      case "java.net.URI"   => renderArray(buf, values, true)
      case "Boolean"        => renderArray(buf, values, false)
      case _                => renderArray(buf, values, false)
    }
  }

  def jsonVector(buf: StringBuilder, value: Any, tpe: String) = {
    val values = value.asInstanceOf[PersistentVector].asScala
    tpe match {
      case "String"         => renderArray(buf, values, true)
      case "java.util.Date" => renderArray(buf, values.map(v => df.format(v.asInstanceOf[Date])), true)
      case "java.util.UUID" => renderArray(buf, values, true)
      case "java.net.URI"   => renderArray(buf, values, true)
      case "Boolean"        => renderArray(buf, values, false)
      case _                => renderArray(buf, values, false)
    }
  }


  def renderObj(buf: StringBuilder, values: Seq[Any], quoting: Boolean) = {
    var firstInObj = true
    buf.append("{")
    values.foreach { case s: String =>
      val p = s.split("@", 2)
      val (k, v) = (p(0), p(1))

      if (firstInObj) {
        firstInObj = false
      } else {
        buf.append(", ")
      }

      buf.append('"')
      buf.append(k)
      buf.append('"')
      buf.append(':')

      if (quoting)
        quote(buf, v)
      else
        buf.append(v)
    }
    buf.append("}")
  }

  def jsonMap(buf: StringBuilder, value: Any, tpe: String) = {
    val values = value.asInstanceOf[PersistentHashSet].asScala.toSeq
    tpe match {
      case "String"         => renderObj(buf, values, true)
      case "java.util.Date" =>
        var firstInObj = true
        buf.append("{")
        values.foreach { case s: String =>
          val p = s.split("@", 2)
          val (k, date) = (p(0), df.parse(p(1)))

          if (firstInObj) {
            firstInObj = false
          } else {
            buf.append(", ")
          }

          buf.append('"')
          buf.append(k)
          buf.append('"')
          buf.append(':')
          quote(buf, df.format(date))
        }
        buf.append("}")
      case "java.util.UUID" => renderObj(buf, values, true)
      case "java.net.URI"   => renderObj(buf, values, true)
      case "Boolean"        => renderObj(buf, values, false)
      case _                => renderObj(buf, values, false)
    }
  }


  // Optionals ...................

  def jsonOptionValue(buf: StringBuilder, value: Any, tpe: String) = tpe match {
    case "String" => value match {
      case null                                  => buf.append("null")
      case v if v.toString.contains(":db/ident") => val s = v.toString; quote(buf, s.substring(s.lastIndexOf("/") + 1).init.init)
      case v                                     => quote(buf, v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head) // pull result map: {:ns/str "abc"}
    }

    case "java.util.Date" => value match {
      case null => buf.append("null")
      case v    => quote(buf, df.format(v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head))
    }

    case "java.util.UUID" => value match {
      case null => buf.append("null")
      case v    => quote(buf, v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head.toString)
    }

    case "java.net.URI" => value match {
      case null => buf.append("null")
      case v    => quote(buf, v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head.toString)
    }

    case "Boolean" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jBoolean]].asScala.toMap.values.head.toString)
    }

    case "Int" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString) // pull result map: {:ns/int 42}
    }

    case "Float" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
    }

    case "Long" => value match {
      case null                               => buf.append("null")
      case v if v.toString.contains(":db/id") => val s = v.toString; buf.append(s.substring(s.lastIndexOf("/") + 1).init.init)
      case v                                  => buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString)
    }

    case "Double" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
    }

    case "BigInt" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString)
    }

    case "BigDecimal" => value match {
      case null => buf.append("null")
      case v    => buf.append(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString)
    }
  }


  def jsonOptionSet(buf: StringBuilder, values: Any, tpe: String) = tpe match {
    case "String" => values match {
      case null => buf.append("null")

      // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
      case vs if vs.toString.contains(":db/ident") =>
        val identMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
        val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].asScala.toMap.values.head.getName)
        renderArray(buf, enums, true)

      // {:ns/strs ["a" "b" "c"]}
      case vs => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }

    case "Int" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Float" => values match {
      case null => buf.append("null")
      case vs   =>
        renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Long" => values match {
      case null => buf.append("null")

      // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
      case vs if vs.toString.contains(":db/id") =>
        val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
        Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].asScala.toMap.values.head).toSet)

      // {:ns/longs [3 4 5]}
      case vs => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Double" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Boolean" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "BigInt" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }


    case "BigDecimal" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "java.util.Date" => values match {
      case null => buf.append("null")
      case vs   =>
        val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
        renderArray(buf, values.map(v => df.format(v.asInstanceOf[Date])), true)
    }

    case "java.util.UUID" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }

    case "java.net.URI" => values match {
      case null => buf.append("null")
      case vs   => renderArray(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }
  }


  def jsonOptionMap(buf: StringBuilder, value: Any, tpe: String) = tpe match {
    case "String" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }

    case "Int" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Long" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Float" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Double" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "Boolean" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "BigInt" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "BigDecimal" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, false)
    }

    case "java.util.Date" => value match {
      case null => buf.append("null")
      case vs   =>
        val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
        var firstInObj = true
        buf.append("{")
        values.foreach { case s: String =>
          val p = s.split("@", 2)
          val (k, d) = (p(0), df.parse(p(1)))

          if (firstInObj) {
            firstInObj = false
          } else {
            buf.append(", ")
          }

          buf.append('"')
          buf.append(k)
          buf.append('"')
          buf.append(':')
          quote(buf, df.format(d))
        }
        buf.append("}")
    }

    case "java.util.UUID" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }

    case "java.net.URI" => value match {
      case null => buf.append("null")
      case vs   => renderObj(buf, vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala, true)
    }
  }


  lazy val flatModel: Seq[Element] = {
    def recurse(element: Element): Seq[Element] = element match {
      case n: Nested                                             => n.elements flatMap recurse
      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
      case a: Atom                                               => Seq(a)
      case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
      case m: Meta                                               => Seq(m)
      case other                                                 => Seq()
    }

    modelE.elements.last match {
      case _: Nested => "Nested attributes are expected to be last in the molecule"
      case other     => throw new IllegalArgumentException(
        "To get nested json Molecule expects the nested attributes to be last in the molecule. Found:\n" + other)
    }

    val elements = modelE.elements flatMap recurse
    if (elements.size != queryE.f.outputs.size)
      sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + queryE.f.outputs.size + "):\n" +
        modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + queryE + "\n----------------\n")

    elements
  }

  // Field/Row mapping model
  lazy val fieldIndexes: Map[Int, Seq[(Int, Int, String, String, Int)]] = {

    def recurse(level: Int, i0: Int, j0: Int, elements: Seq[Element]): Seq[(Int, Seq[(Int, Int, String, String, Int)])] =
      elements.foldLeft(i0, j0, Seq.empty[(Int, Seq[(Int, Int, String, String, Int)])]) {

        case ((i, j, acc), Atom(_, attr, tpeS, card, _, _, _, _)) if i == -1 && attr.last != '_' =>
          (i + 1, j + 1, Seq(level -> Seq((i + 1, j + 1, attr, tpeS, card))))

        case ((i, j, acc), Atom(_, attr, tpeS, card, _, _, _, _)) if attr.last != '_' =>
          (i + 1, j + 1, acc.init :+ level -> (acc.last._2 :+ (i + 1, j + 1, attr, tpeS, card)))

        case ((i, j, acc), Nested(Bond(_, refAttr, _, _, _), es)) if acc.isEmpty =>
          (i, j, Seq(level -> Seq((i + 1, j + 1, refAttr, "Nested", 2))) ++ recurse(level + 1, -1, j + 1, es))

        case ((i, j, acc), Nested(Bond(_, refAttr, _, _, _), es)) =>
          (i, j, (acc.init :+ level -> (acc.last._2 :+ (i + 1, j + 1, refAttr, "Nested", 2))) ++ recurse(level + 1, -1, j + 1, es))

        case ((i, j, acc), Meta(_, "many-ref", _, _, IndexVal)) =>
          (i, j + 1, acc)

        case ((i, j, acc), other) =>
          (i, j, acc)
      }._3

    /*
      m(Ns.int.str * Refs1.int1) becomes:

      Map(
        0 -> Seq((0, 1, "int", "Int"), (1, 2, "str", "String"), (2, 3, "refs1", "Ref")),
        1 -> Seq((0, 4, "int1", "Int"))
      )
     */
    recurse(0, -1, 0, modelE.elements).toMap
  }


  lazy val entityIndexes: List[Int] = flatModel.zipWithIndex.collect {
    case (Meta(_, "many-ref", _, _, IndexVal), _) => None
    case (Meta(_, _, _, _, IndexVal), i)          => Some(i)
  }.flatten.toList

  lazy val manyRefIndexes: Seq[Int] = flatModel.zipWithIndex.collect {
    case (Meta(_, "many-ref", _, _, IndexVal), i) => i
  }

  lazy val indexMap: Map[Int, Int] = flatModel.zipWithIndex.foldLeft(0, Seq.empty[(Int, Int)]) {
    case ((rawIndex, indexMap), (meta, i)) => meta match {
      case Meta(_, "many-ref", _, _, IndexVal) => (rawIndex, indexMap :+ (rawIndex, i))
      case Meta(_, _, _, _, IndexVal)          => (rawIndex + 1, indexMap :+ (rawIndex, i))
      case _                                   => (rawIndex + 1, indexMap :+ (rawIndex, i))
    }
  }._2.toMap

  def sortRows(rowSeq: Seq[jList[AnyRef]], entityIndexes: Seq[Int]): Seq[jList[AnyRef]] = entityIndexes match {
    case List(a)                               => rowSeq.sortBy(row => row.get(a).asInstanceOf[Long])
    case List(a, b)                            => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long]))
    case List(a, b, c)                         => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long]))
    case List(a, b, c, d)                      => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long]))
    case List(a, b, c, d, e)                   => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long]))
    case List(a, b, c, d, e, f)                => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g)             => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h)          => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h, i)       => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h, i, j)    => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => row.get(j).asInstanceOf[Long])
    case List(a, b, c, d, e, f, g, h, i, j, k) => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => (row.get(j).asInstanceOf[Long], row.get(k).asInstanceOf[Long]))
  }

  val doDebug = true
  def debug(s: String): Unit = {
    if (doDebug)
      println(s)
  }

  def nestedJson(rows: Iterable[jList[AnyRef]]) = {
    if (rows.isEmpty) {
      ""
    } else {
      nestedJson1(rows)
    }
  }

  def nestedJson1(rows: Iterable[jList[AnyRef]]) = {

    debug("===================================================================================")
    //debug(_model)
    //    debug(modelE)
    //    debug(queryE)
    //debug(_queryE.datalog)
    flatModel.foreach(e => debug(e.toString))
    debug("---- ")
    fieldIndexes.foreach(e => debug(e.toString))
    //    debug("---- ")
    debug("---- " + entityIndexes)
    debug("---- " + manyRefIndexes)
    debug("---- " + indexMap)

    val sortedRows = sortRows(rows.toSeq, entityIndexes)
    val rowCount = sortedRows.length

    sortedRows.foreach(r => debug(r.toString))

    var firstEntry = true
    val buf0 = new StringBuilder("[")
    val buffers = buf0 :: fieldIndexes.keys.toList.sorted.tail.map(_ => new StringBuilder(""))
    val descendingLevels = fieldIndexes.keys.toSeq.sorted.reverse

    def addPairs(buf: StringBuilder, level: Int, fields: Seq[(Int, Int, String, String, Int)], row: Seq[Any]) {
      buf.append("\n" + "   " * level + "{")
      for {(i, rowIndex, field, tpeS, card) <- fields} yield {
        if (i > 0) buf.append(", ")

        if (tpeS == "Nested") {
          quote(buf, field)
          buf.append(": [")
        } else {
          fieldValue(buf, field, tpeS, card, row(rowIndex))
          if (fields.size == i + 1) buf.append("}")
        }
      }
    }

    sortedRows.foldLeft(Seq.empty[Long], 1) { case ((prevEntities, r), row0) =>

      val row = row0.asScala.asInstanceOf[Seq[Any]]
      val curEntities = entityIndexes.map(i => row(i).asInstanceOf[Long])

      debug("------------------ " + r + " --------------------------------")

      descendingLevels.foreach { level =>
        val buf = buffers(level)
        val fields = fieldIndexes(level)
        level match {

          // Single row --------------------------------------------------------

          case l if rowCount == 1 => {
            if (l == entityIndexes.size - 1) {
              addPairs(buf, l, fields, row)
            } else {
              buf.append("\n" + "   " * level + "{")
              for {(i, rowIndex, field, tpeS, card) <- fields} yield {
                if (i > 0) buf.append(", ")

                if (tpeS == "Nested") {
                  quote(buf, field)
                  buf.append(": [")
                  // Close nested
                  buf.append(buffers(level + 1).toString)
                  buf.append("]}")
                } else {
                  fieldValue(buf, field, tpeS, card, row(rowIndex))
                  if (fields.size == i + 1) buf.append("}")
                }
              }
            }
          }


          // Last row --------------------------------------------------------

          case l if r == rowCount => {
            val (prevParentE, curParentE) = if (l == 0) (0, 0) else (prevEntities(l - 1), curEntities(l - 1))
            val (prevE, curE) = (prevEntities(l), curEntities(l))

            if (prevParentE != curParentE) {
              // New parent - add previously accumulated on this level to parent

              val parentBuffer = buffers(l - 1)
              // Append accumulated from this level
              parentBuffer.append(buf.toString + "]")
              // Round up parent level
              parentBuffer.append("}")

              // Start over with clean buffer on this level
              buf.clear

              // Add pairs on this level
              buf.append("\n" + "   " * level + "{")
              for {(i, rowIndex, field, tpeS, card) <- fields} yield {
                if (i > 0) buf.append(", ")

                if (tpeS == "Nested") {
                  quote(buf, field)
                  buf.append(": [")
                  // Add nested
                  buf.append(buffers(level + 1).toString)
                  buf.append("]}")
                } else {
                  fieldValue(buf, field, tpeS, card, row(rowIndex))
                  if (fields.size == i + 1) buf.append("}")
                }
              }
            } else if (prevE != curE) {
              // New pairs
              buf.append(",")
              // Add pairs on this level
              addPairs(buf, l, fields, row)
              if (l < entityIndexes.size - 1) {
                // Add nested
                buf.append(buffers(level + 1).toString)
                buf.append("]}")
              }

            } else {
              // No new data on this level: add nested
              buf.append(buffers(level + 1).toString)
              buf.append("]}")
            }
          }

          // First row  -------------------------------------------------------

          case l if r == 1 => {
            addPairs(buf, l, fields, row)
          }


          // Intermediate rows  -----------------------------------------------

          case l => {
            val (prevParentE, curParentE) = if (l == 0) (0, 0) else (prevEntities(l - 1), curEntities(l - 1))
            val (prevE, curE) = (prevEntities(l), curEntities(l))

            if (prevParentE != curParentE) {
              // New parent - add previously accumulated on this level to parent

              val parentBuffer = buffers(l - 1)
              // Append accumulated from this level
              parentBuffer.append(buf.toString + "]")
              // Round up parent level
              parentBuffer.append("}")

              // Start over with clean buffer on this level
              buf.clear

              // Add pairs on this level
              addPairs(buf, l, fields, row)

            } else if (prevE != curE) {
              // New on this level

              buf.append(",")
              // Add pairs on this level
              addPairs(buf, l, fields, row)

            } else {
              // No new data on this level: do nothing...
            }
          }
        }
        debug(s"-$level-")
        debug(buf.toString)

      } // descending levels loop

      (curEntities, r + 1)

    } // rows loop

    buf0.append("\n]").toString()
  }
}