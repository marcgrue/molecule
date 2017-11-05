package molecule.factory

import java.lang.{Boolean => jBoolean, Double => jDouble, Long => jLong}
import java.math.{BigDecimal => jBigDec, BigInteger => jBigInt}
import java.util.{Date, List => jList, Map => jMap}

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

  lazy val flatModel: Seq[Element] = {
    def recurse(element: Element): Seq[Element] = element match {
      case n: Nested                                             => n.elements flatMap recurse
      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
      case a: Atom                                               => Seq(a)
      case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
      case m: Meta                                               => Seq(m)
      case other                                                 => Seq()
    }

    val elements = modelE.elements flatMap recurse
    if (elements.size != queryE.f.outputs.size)
      sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + queryE.f.outputs.size + "):\n" +
        modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + queryE + "\n----------------\n")
    elements
  }

  // Field/Row mapping model
  lazy val fieldIndexes: Map[Int, Seq[(Int, Int, String, String)]] = {

    def recurse(level: Int, i0: Int, j0: Int, elements: Seq[Element]): Seq[(Int, Seq[(Int, Int, String, String)])] =
      elements.foldLeft(i0, j0, Seq.empty[(Int, Seq[(Int, Int, String, String)])]) {

        case ((i, j, acc), Atom(_, attr, tpeS, _, _, _, _, _)) if i == -1 =>
          (i + 1, j + 1, Seq(level -> Seq((i + 1, j + 1, attr, tpeS))))

        case ((i, j, acc), Atom(_, attr, tpeS, _, _, _, _, _)) =>
          (i + 1, j + 1, acc.init :+ level -> (acc.last._2 :+ (i + 1, j + 1, attr, tpeS)))

        case ((i, j, acc), Nested(Bond(_, refAttr, _, _, _), es)) =>
          (i, j, (acc.init :+ level -> (acc.last._2 :+ (i + 1, j + 1, refAttr, "Nested"))) ++ recurse(level + 1, -1, j + 1, es))

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
    case (Meta(_, _, _, _, IndexVal), i) => i
  }.toList

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


  def nestedJson(rows: Iterable[jList[AnyRef]]) = {
    if (rows.isEmpty) {
      ""
    } else {
      nestedJson1(rows)
    }
  }

  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def fieldValue(buf: StringBuilder, field0: String, tpe: String, value: Any) = {
    val optional = field0.endsWith("@")
    val field = if (optional) field0.init else field0

    quote(buf, field)
    buf.append(": ")

    // Add value to buffer
    tpe match {
              case r"Map\[String, _]]]" => jsonOptionMap(buf, value, t)
              case t if t <:< typeOf[Option[Set[_]]]         => jsonOptionSet(buf, value, t)
              case t if t <:< typeOf[Option[_]]              => jsonOptionValue(buf, value, t)
              case t if t <:< typeOf[Map[String, _]]         => jsonMap(buf, value, t)
              case t if t <:< typeOf[Vector[_]]              => jsonVector(buf, value, tpe)
              case t if t <:< typeOf[Stream[_]]              => jsonStream(buf, value, tpe)
              case t if t <:< typeOf[Set[_]]                 => jsonSet(buf, value, tpe)
      case t => jsonValue(buf, t, value)
    }
  }


  def jsonValue(buf: StringBuilder, tpe: String, value: Any) = tpe match {
    case "String"  => quote(buf, value.toString)
    case "Int"     => buf.append(value)
    case "Float"   => buf.append(value)
    case "Date"    =>
      val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
      quote(buf, df.format(value.asInstanceOf[Date]))
    case "UUID"    => quote(buf, value.toString)
    case "URI"     => quote(buf, value.toString)
    case "Boolean" => buf.append(value.toString)
    case number    => buf.append(value.toString)
  }

  def renderArray(buf: Tree, values: Tree, quote: Boolean) = {
    q"""
        var firstInArray = true
        $buf.append("[")
        $values.foreach { value =>
          if (firstInArray) {
            firstInArray = false
          } else {
            $buf.append(",")
          }
          if($quote)
            quote($buf, value.toString)
          else
            $buf.append(value.toString)
        }
        $buf.append("]")
     """
  }

  def jsonSet(buf: Tree, value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Set[String]]  => renderArray(buf, values, true)
      case t if t <:< typeOf[Set[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(buf, q"vs", true)}
         """
      case t if t <:< typeOf[Set[UUID]]    => renderArray(buf, values, true)
      case t if t <:< typeOf[Set[URI]]     => renderArray(buf, values, true)
      case t if t <:< typeOf[Set[Boolean]] => renderArray(buf, values, false)
      case t if t <:< typeOf[Set[_]]       => renderArray(buf, values, false)
    }
  }

  def jsonStream(buf: Tree, value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[LazySeq].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Stream[String]]  => renderArray(buf, values, true)
      case t if t <:< typeOf[Stream[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(buf, q"vs", true)}
         """
      case t if t <:< typeOf[Stream[UUID]]    => renderArray(buf, values, true)
      case t if t <:< typeOf[Stream[URI]]     => renderArray(buf, values, true)
      case t if t <:< typeOf[Stream[Boolean]] => renderArray(buf, values, false)
      case t if t <:< typeOf[Stream[_]]       => renderArray(buf, values, false)
    }
  }

  def jsonVector(buf: Tree, value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentVector].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Vector[String]]  => renderArray(buf, values, true)
      case t if t <:< typeOf[Vector[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(buf, q"vs", true)}
         """
      case t if t <:< typeOf[Vector[UUID]]    => renderArray(buf, values, true)
      case t if t <:< typeOf[Vector[URI]]     => renderArray(buf, values, true)
      case t if t <:< typeOf[Vector[Boolean]] => renderArray(buf, values, false)
      case t if t <:< typeOf[Vector[_]]       => renderArray(buf, values, false)
    }
  }


  def renderObj(buf: Tree, values: Tree, quote: Boolean) = {
    q"""
         var firstInObj = true
          $buf.append("{")
          $values.foreach { case s: String =>
            val p = s.split("@", 2)
            val (k, v) = (p(0), p(1))

            if (firstInObj) {
              firstInObj = false
            } else {
              $buf.append(",")
            }

            $buf.append('"')
            $buf.append(k)
            $buf.append('"')
            $buf.append(':')

            if($quote)
              quote($buf, v)
            else
              $buf.append(v)
          }
          $buf.append("}")
       """
  }

  def jsonMap(buf: Tree, value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Map[String, String]]  => renderObj(buf, values, true)
      case t if t <:< typeOf[Map[String, Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            var firstInObj = true
            $buf.append("{")
            $values.foreach { case s: String =>
              val p = s.split("@", 2)
              val (k, date) = (p(0), df.parse(p(1)))

              if (firstInObj) {
                firstInObj = false
              } else {
                $buf.append(",")
              }

              $buf.append('"')
              $buf.append(k)
              $buf.append('"')
              $buf.append(':')
              quote($buf, df.format(date))
            }
            $buf.append("}")
         """
      case t if t <:< typeOf[Map[String, UUID]]    => renderObj(buf, values, true)
      case t if t <:< typeOf[Map[String, URI]]     => renderObj(buf, values, true)
      case t if t <:< typeOf[Map[String, Boolean]] => renderObj(buf, values, false)
      case t if t <:< typeOf[Map[String, _]]       => renderObj(buf, values, false)
    }
  }


  // Optionals ...................

  def jsonOptionValue(buf: Tree, value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[String]]     =>
      q"""
          $value match {
            case null                                  => $buf.append("null")
//            case s: String                             => quote(buf, s)
            case v if v.toString.contains(":db/ident") => val s = v.toString; quote($buf, s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                     => quote($buf, v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head) // pull result map: {:ns/str "abc"}
          }
         """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
          $value match {
            case null    => $buf.append("null")
//            case v: Date => quote(buf, v.toString)
            case v       =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val date = v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head.asInstanceOf[Date]
              quote($buf, df.format(date))
          }
         """
    case t if t <:< typeOf[Option[UUID]]       =>
      q"""
          $value match {
            case null    => $buf.append("null")
//            case v: UUID => quote(buf, v.toString)
            case v       => quote($buf, v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
          $value match {
            case null   => $buf.append("null")
//            case v: URI => quote(buf, v.toString)
            case v      => quote($buf, v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
          $value match {
            case null        => $buf.append("null")
//            case v: jBoolean => buf.append(v.toString)
            case v           => $buf.append(v.asInstanceOf[jMap[String, jBoolean]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
          $value match {
            case null     => $buf.append("null")
//            case v: jLong => ´buf.append(v.toString)
            case v        => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString) // pull result map: {:ns/int 42}
          }
         """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
          $value match {
            case null       => $buf.append("null")
//            case v: jDouble => buf.append(v.toString)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
          $value match {
            case null                               => $buf.append("null")
//            case v: jLong                           => buf.append(v.toString)
            case v if v.toString.contains(":db/id") => val s = v.toString; $buf.append(s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                  => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
          $value match {
            case null       => $buf.append("null")
//            case v: jDouble => buf.append(v.toString)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
          $value match {
            case null       => $buf.append("null")
//            case v: jBigInt => buf.append(v.toString)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
          $value match {
            case null       => $buf.append("null")
//            case v: jBigDec => buf.append(v.toString)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString)
          }
         """
  }

  def jsonOptionSet(buf: Tree, values: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
          $values match {
            case null                                    => $buf.append("null")
//            case vs: PersistentHashSet                   => {renderArray(buf, q"vs.asScala.toSeq", true)}

            // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
            case vs if vs.toString.contains(":db/ident") =>
              val identMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].asScala.toMap.values.head.getName)
              ${renderArray(buf, q"enums", true)}

            // {:ns/strs ["a" "b" "c"]}
            case vs => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
       """
    case t if t <:< typeOf[Option[Set[Int]]]     =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}

            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            case vs if vs.toString.contains(":db/id") =>
              val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].asScala.toMap.values.head).toSet.asInstanceOf[Set[Long]])

            // {:ns/longs [3 4 5]}
            case vs => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Double]]]  =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", true)}
            case vs                    =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              ${renderArray(buf, q"values.map(v => df.format(v.asInstanceOf[Date]))", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", true)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
          $values match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderArray(buf, q"vs.asScala.toSeq", true)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
  }

  def jsonOptionMap(buf: Tree, value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]] =>
      q"""
          $value match {
            case null                  => $buf.append("null")
//            case vs: PersistentHashSet => {renderObj(buf, q"vs.asScala.toSeq", true)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """

    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   =>
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              var firstInObj = true
              $buf.append("{")
              values.foreach { case s: String =>
                val p = s.split("@", 2)
                val (k, d) = (p(0), df.parse(p(1)))

                if (firstInObj) {
                  firstInObj = false
                } else {
                  $buf.append(",")
                }

                $buf.append('"')
                $buf.append(k)
                $buf.append('"')
                $buf.append(':')
                quote($buf, df.format(d))
              }
              $buf.append("}")
          }
         """
    case t if t <:< typeOf[Option[Map[String, UUID]]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
  }



  def nestedJson1(rows: Iterable[jList[AnyRef]]) = {

    //println("===================================================================================")
    //println(_model)
    //    println(modelE)
    //    println(queryE)
    //println(_queryE.datalog)
    //println("---- ")
    //flatModel foreach println
    //    fieldIndexes foreach println
    //    println("---- " + entityIndexes)
    //    println("---- " + manyRefIndexes)
    //    println("---- " + indexMap)

    val sortedRows = sortRows(rows.toSeq, entityIndexes)

//    sortedRows foreach println

    val rowCount = sortedRows.length

    var firstEntry = true
    val buf0 = new StringBuilder("[")
    val buffers = buf0 :: entityIndexes.tail.map(_ => new StringBuilder(""))
    val descendingLevels = entityIndexes.indices.reverse

    def addPairs(buf: StringBuilder, level: Int, fields: Seq[(Int, Int, String, String)], row: Seq[Any]) {
      buf.append("\n" + "   " * level + "{")
      for {(i, rowIndex, field, tpeS) <- fields} yield {
        if (i > 0) buf.append(", ")

        if (tpeS == "Nested") {
          quote(buf, field)
          buf.append(": [")
        } else {
          fieldValue(buf, field, tpeS, row(rowIndex))
          if (fields.size == i + 1) buf.append("}")
        }
      }
    }

    sortedRows.foldLeft(Seq.empty[Long], 1) { case ((prevEntities, r), row0) =>

      val row = row0.asScala.asInstanceOf[Seq[Any]]
      val curEntities = entityIndexes.map(i => row(i).asInstanceOf[Long])

      //      println("------------------ " + r + " --------------------------------")

      descendingLevels.foreach { level =>
        val buf = buffers(level)
        val fields = fieldIndexes(level)
        level match {

          // Last row --------------------------------------------------------

          // (could be the first row also if only one row exists)
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
              for {(i, rowIndex, field, tpeS) <- fields} yield {
                if (i > 0) buf.append(", ")

                if (tpeS == "Nested") {
                  quote(buf, field)
                  buf.append(": [")
                  // Close nested
                  buf.append(buffers(level + 1).toString)
                  buf.append("]}")
                } else {
                  fieldValue(buf, field, tpeS, row(rowIndex))
                  if (fields.size == i + 1) buf.append("}")
                }
              }
            } else if (prevE != curE) {
              // New pairs
              buf.append(",")
              // Add pairs on this level
              addPairs(buf, l, fields, row)

            } else {
              // No new data on this level: add child
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
        //        println(s"-$level-")
        //        println(buf.toString)

      } // descending levels loop

      (curEntities, r + 1)

    } // rows loop

    buf0.append("\n]").toString()
  }
}