package molecule.factory

import java.net.URI
import java.util.{Date, UUID}

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

trait GetJson[Ctx <: Context] extends Base[Ctx] {
  import c.universe._


  // Without nested/composites ------------------------------------------------------------------


  def json(model: Tree, query: Tree, rows: Tree, tpes: Seq[Type]): Tree = {
    val types = tpes.map(_.toString)
    val typesLength = tpes.length

    q"""
        private val jsEscapeChars: Set[Char] =
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

        private def appendEscapedString(buf: StringBuilder, s: String) {
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

        private def quote(buf: StringBuilder, s: String) {
          buf.append('"') //open quote
          appendEscapedString(buf, s)
          buf.append('"') //close quote
        }

        val fields: Seq[String] = {
          val fields0 = $model.elements.collect {
            case Atom(_, attr, _, _, _, _, _, _) => attr
          }
          if (fields0.length != $typesLength)
            sys.error("Unexpected json field mismatch: model attr count doesn't match output types count." +
              "\nModel attrs  (" + fields0.length + "): " + fields0.mkString(", ") +
              "\nOutput types (" + $typesLength   + "): " + Seq(..$types).mkString(", "))
          fields0
        }

        var firstEntry = true
        val buf = new StringBuilder("[")
        $rows.foreach { row =>
          if (firstEntry) {
            firstEntry = false
          } else {
            buf.append(",")
          }
          buf.append("\n{")
          ..${jsonRow(query, q"fields", q"row", tpes)}
          buf.append("}")
        }
        buf.append("\n]").toString()
     """
  }

  def jsonRow(query: Tree, fields: Tree, row: Tree, tpes: Seq[Type]) = {
    tpes.zipWithIndex.map { case (tpe, i) => toJson(query, fields, row, tpe, i) }
  }


  def renderArray(values: Tree, quote: Boolean) = {
    q"""
        var firstInArray = true
        buf.append("[")
        $values.foreach { value =>
          if (firstInArray) {
            firstInArray = false
          } else {
            buf.append(",")
          }
          if($quote)
            quote(buf, value.toString)
          else
            buf.append(value.toString)
        }
        buf.append("]")
     """
  }

  def jsonSet(value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Set[String]]  => renderArray(values, true)
      case t if t <:< typeOf[Set[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(q"vs", true)}
         """
      case t if t <:< typeOf[Set[UUID]]    => renderArray(values, true)
      case t if t <:< typeOf[Set[URI]]     => renderArray(values, true)
      case t if t <:< typeOf[Set[Boolean]] => renderArray(values, false)
      case t if t <:< typeOf[Set[_]]       => renderArray(values, false)
    }
  }

  def jsonStream(value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[LazySeq].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Stream[String]]  => renderArray(values, true)
      case t if t <:< typeOf[Stream[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(q"vs", true)}
         """
      case t if t <:< typeOf[Stream[UUID]]    => renderArray(values, true)
      case t if t <:< typeOf[Stream[URI]]     => renderArray(values, true)
      case t if t <:< typeOf[Stream[Boolean]] => renderArray(values, false)
      case t if t <:< typeOf[Stream[_]]       => renderArray(values, false)
    }
  }

  def jsonVector(value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentVector].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Vector[String]]  => renderArray(values, true)
      case t if t <:< typeOf[Vector[Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val vs = $values.map(v => df.format(v.asInstanceOf[Date]))
            ${renderArray(q"vs", true)}
         """
      case t if t <:< typeOf[Vector[UUID]]    => renderArray(values, true)
      case t if t <:< typeOf[Vector[URI]]     => renderArray(values, true)
      case t if t <:< typeOf[Vector[Boolean]] => renderArray(values, false)
      case t if t <:< typeOf[Vector[_]]       => renderArray(values, false)
    }
  }

  def renderObj(values: Tree, quote: Boolean) = {
    q"""
         var firstInObj = true
          buf.append("{")
          $values.foreach { case s: String =>
            val p = s.split("@", 2)
            val (k, v) = (p(0), p(1))

            if (firstInObj) {
              firstInObj = false
            } else {
              buf.append(",")
            }

            buf.append('"')
            buf.append(k)
            buf.append('"')
            buf.append(':')

            if($quote)
              quote(buf, v)
            else
              buf.append(v)
          }
          buf.append("}")
       """
  }

  def jsonMap(value: Tree, tpe: Type) = {
    val values = q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq"
    tpe match {
      case t if t <:< typeOf[Map[String, String]]  => renderObj(values, true)
      case t if t <:< typeOf[Map[String, Date]]    =>
        q"""
            val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            var firstInObj = true
            buf.append("{")
            $values.foreach { case s: String =>
              val p = s.split("@", 2)
              val (k, date) = (p(0), df.parse(p(1)))

              if (firstInObj) {
                firstInObj = false
              } else {
                buf.append(",")
              }

              buf.append('"')
              buf.append(k)
              buf.append('"')
              buf.append(':')
              quote(buf, df.format(date))
            }
            buf.append("}")
         """
      case t if t <:< typeOf[Map[String, UUID]]    => renderObj(values, true)
      case t if t <:< typeOf[Map[String, URI]]     => renderObj(values, true)
      case t if t <:< typeOf[Map[String, Boolean]] => renderObj(values, false)
      case t if t <:< typeOf[Map[String, _]]       => renderObj(values, false)
    }
  }


  def toJson(query: Tree, fields: Tree, row: Tree, tpe: Type, i: Int) = {
    val field: Tree =
      q"""
          if($i >= $fields.size)
            "[Unknown]"
          else if(${tpe <:< typeOf[Option[_]]})
            $fields($i).init // skip appended '@' in optional attr name
          else
            $fields($i)
        """
    val value: Tree = q"if($i >= $row.size) null else $row.get($i)"
    q"""
        if($i > 0) buf.append(", ")
        quote(buf, $field)
        buf.append(": ")
        ..${
      tpe match {
        case t if t <:< typeOf[Option[Map[String, _]]] => jsonOptionMap(value, t)
        case t if t <:< typeOf[Option[Set[_]]]         => jsonOptionSet(value, t)
        case t if t <:< typeOf[Option[_]]              => jsonOptionValue(value, t)
        case t if t <:< typeOf[Map[String, _]]         => jsonMap(value, t)
        case t if t <:< typeOf[Vector[_]]              => jsonVector(value, tpe)
        case t if t <:< typeOf[Stream[_]]              => jsonStream(value, tpe)
        case t if t <:< typeOf[Set[_]]                 => jsonSet(value, tpe)
        case t                                         => jsonValue(query, value, t, i)
      }
    }
      """
  }


  def jsonValue(query: Tree, value: Tree, tpe: Type, i: Int) = tpe match {
    case t if t <:< typeOf[String]  =>
      q"""
        $query.f.outputs($i) match {
          case AggrExpr("sum",_,_)    => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) buf.append($value.toString) else quote(buf, $value.toString)
            case "Float" => if($value.isInstanceOf[jDouble]) buf.append($value.toString) else quote(buf, $value.toString)
            case _       => quote(buf, $value.toString)
          }
          case AggrExpr("median",_,_) => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) buf.append($value.toString) else quote(buf, $value.toString)
            case "Float" => if($value.isInstanceOf[jDouble]) buf.append($value.toString) else quote(buf, $value.toString)
            case _       => quote(buf, $value.toString)
          }
          case other                  => quote(buf, $value.toString)
        }
      """
    case t if t <:< typeOf[Int]     =>
      q"""
        $value match {
          case l: jLong  => buf.append(l.toString)
          case s: String => buf.append(s.toString)
          case other     => quote(buf, other.toString)
        }
      """
    case t if t <:< typeOf[Float]   =>
      q"""
        $value match {
          case d: jDouble => buf.append(d.toString)
          case s: String  => buf.append(s.toString)
          case other      => quote(buf, other.toString)
        }
      """
    case t if t <:< typeOf[Date]    =>
      q"""
          val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
          quote(buf, df.format($value.asInstanceOf[Date]).toString)
       """
    case t if t <:< typeOf[UUID]    => q"quote(buf, $value.toString)"
    case t if t <:< typeOf[URI]     => q"quote(buf, $value.toString)"
    case t if t <:< typeOf[Boolean] => q"buf.append($value.toString)"
    case number                     => q"buf.append($value.toString)"
  }


  // Optionals ...................

  def jsonOptionValue(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[String]]     =>
      q"""
          $value match {
            case null                                  => buf.append("null")
//            case s: String                             => quote(buf, s)
            case v if v.toString.contains(":db/ident") => val s = v.toString; quote(buf, s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                     => quote(buf, v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head) // pull result map: {:ns/str "abc"}
          }
         """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
          $value match {
            case null    => buf.append("null")
//            case v: Date => quote(buf, v.toString)
            case v       =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val date = v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head.asInstanceOf[Date]
              quote(buf, df.format(date))
          }
         """
    case t if t <:< typeOf[Option[UUID]]       =>
      q"""
          $value match {
            case null    => buf.append("null")
//            case v: UUID => quote(buf, v.toString)
            case v       => quote(buf, v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
          $value match {
            case null   => buf.append("null")
//            case v: URI => quote(buf, v.toString)
            case v      => quote(buf, v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
          $value match {
            case null        => buf.append("null")
//            case v: jBoolean => buf.append(v.toString)
            case v           => buf.append(v.asInstanceOf[jMap[String, jBoolean]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
          $value match {
            case null     => buf.append("null")
//            case v: jLong => buf.append(v.toString)
            case v        => buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString) // pull result map: {:ns/int 42}
          }
         """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
          $value match {
            case null       => buf.append("null")
//            case v: jDouble => buf.append(v.toString)
            case v          => buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
          $value match {
            case null                               => buf.append("null")
//            case v: jLong                           => buf.append(v.toString)
            case v if v.toString.contains(":db/id") => val s = v.toString; buf.append(s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                  => buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
          $value match {
            case null       => buf.append("null")
//            case v: jDouble => buf.append(v.toString)
            case v          => buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
          $value match {
            case null       => buf.append("null")
//            case v: jBigInt => buf.append(v.toString)
            case v          => buf.append(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
          $value match {
            case null       => buf.append("null")
//            case v: jBigDec => buf.append(v.toString)
            case v          => buf.append(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString)
          }
         """
  }

  def jsonOptionSet(values: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
          $values match {
            case null                                    => buf.append("null")
//            case vs: PersistentHashSet                   => {renderArray(q"vs.asScala.toSeq", true)}

            // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
            case vs if vs.toString.contains(":db/ident") =>
              val identMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].asScala.toMap.values.head.getName)
              ${renderArray(q"enums", true)}

            // {:ns/strs ["a" "b" "c"]}
            case vs => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
       """
    case t if t <:< typeOf[Option[Set[Int]]]     =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}

            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            case vs if vs.toString.contains(":db/id") =>
              val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].asScala.toMap.values.head).toSet.asInstanceOf[Set[Long]])

            // {:ns/longs [3 4 5]}
            case vs => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Double]]]  =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", false)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", true)}
            case vs                    =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              ${renderArray(q"values.map(v => df.format(v.asInstanceOf[Date]))", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", true)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
          $values match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderArray(q"vs.asScala.toSeq", true)}
            case vs                    => ${renderArray(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
  }

  def jsonOptionMap(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]] =>
      q"""
          $value match {
            case null                  => buf.append("null")
//            case vs: PersistentHashSet => {renderObj(q"vs.asScala.toSeq", true)}
            case vs                    => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """

    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   =>
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              var firstInObj = true
              buf.append("{")
              values.foreach { case s: String =>
                val p = s.split("@", 2)
                val (k, d) = (p(0), df.parse(p(1)))

                if (firstInObj) {
                  firstInObj = false
                } else {
                  buf.append(",")
                }

                buf.append('"')
                buf.append(k)
                buf.append('"')
                buf.append(':')
                quote(buf, df.format(d))
              }
              buf.append("}")
          }
         """
    case t if t <:< typeOf[Option[Map[String, UUID]]]       =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
          $value match {
            case null => buf.append("null")
            case vs   => ${renderObj(q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", true)}
          }
         """
  }
}
