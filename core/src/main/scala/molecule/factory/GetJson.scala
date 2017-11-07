package molecule.factory

import java.net.URI
import java.util.{Date, UUID}

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

trait GetJson[Ctx <: Context] extends Base[Ctx] {
  import c.universe._

  // Shamelessly adopted from lift-json:
  // https://github.com/lift/framework/blob/db05d863c290c5fd1081a7632263433153fc9fe3/core/json/src/main/scala/net/liftweb/json/JsonAST.scala#L813-L883

  def jsonBase =
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
     """


  // Without nested/composites ------------------------------------------------------------------

  def json(model: Tree, query: Tree, rows: Tree, tpes: Seq[Type]): Tree = {
    val types = tpes.map(_.toString)
    val typesLength = tpes.length

    q"""
        ..$jsonBase

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
          ..${jsonRow(q"buf", query, q"fields", q"row", tpes)}
          buf.append("}")
        }
        buf.append("\n]").toString()
     """
  }


  def jsonRow(buf: Tree, query: Tree, fields: Tree, row: Tree, tpes: Seq[Type]) = {
    tpes.zipWithIndex.map { case (tpe, i) => toJson(buf, query, fields, row, tpe, q"$i", i) }
  }


  def toJson(buf: Tree, query: Tree, fields: Tree, row: Tree, tpe: Type, rowIndex: Tree, fieldIndex: Int) = {
    val field: Tree =
      q"""
          if($fieldIndex >= $fields.size)
            "[Unknown]"
          else if(${tpe <:< typeOf[Option[_]]})
            $fields($fieldIndex).init // skip appended '$$' in optional attr name
          else
            $fields($fieldIndex)
        """
    val value: Tree = q"if($rowIndex >= $row.size) null else $row.get($rowIndex)"
    q"""
        if($fieldIndex > 0) $buf.append(", ")
        quote($buf, $field)
        $buf.append(": ")
        ..${
      tpe match {
        case t if t <:< typeOf[Option[Map[String, _]]] => jsonOptionMap(buf, value, t)
        case t if t <:< typeOf[Option[Set[_]]]         => jsonOptionSet(buf, value, t)
        case t if t <:< typeOf[Option[_]]              => jsonOptionValue(buf, value, t)
        case t if t <:< typeOf[Map[String, _]]         => jsonMap(buf, value, t)
        case t if t <:< typeOf[Vector[_]]              => jsonVector(buf, value, tpe)
        case t if t <:< typeOf[Stream[_]]              => jsonStream(buf, value, tpe)
        case t if t <:< typeOf[Set[_]]                 => jsonSet(buf, value, tpe)
        case t                                         => jsonValue(buf, query, value, t, rowIndex)
      }
    }
      """
  }


  def jsonValue(buf: Tree, query: Tree, value: Tree, tpe: Type, rowIndex: Tree) = tpe match {
    case t if t <:< typeOf[String]  =>
      q"""
        $query.f.outputs($rowIndex) match {
          case AggrExpr("sum",_,_)    => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) $buf.append($value.toString) else quote($buf, $value.toString)
            case "Float" => if($value.isInstanceOf[jDouble]) $buf.append($value.toString) else quote($buf, $value.toString)
            case _       => quote($buf, $value.toString)
          }
          case AggrExpr("median",_,_) => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) $buf.append($value.toString) else quote($buf, $value.toString)
            case "Float" => if($value.isInstanceOf[jDouble]) $buf.append($value.toString) else quote($buf, $value.toString)
            case _       => quote($buf, $value.toString)
          }
          case other                  => quote($buf, $value.toString)
        }
      """
    case t if t <:< typeOf[Int]     =>
      q"""
        $value match {
          case l: jLong  => $buf.append(l.toString)
          case s: String => $buf.append(s.toString)
          case other     => quote($buf, other.toString)
        }
      """
    case t if t <:< typeOf[Float]   =>
      q"""
        $value match {
          case d: jDouble => $buf.append(d.toString)
          case s: String  => $buf.append(s.toString)
          case other      => quote($buf, other.toString)
        }
      """
    case t if t <:< typeOf[Date]    =>
      q"""
          val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
          quote($buf, df.format($value.asInstanceOf[Date]).toString)
       """
    case t if t <:< typeOf[UUID]    => q"quote($buf, $value.toString)"
    case t if t <:< typeOf[URI]     => q"quote($buf, $value.toString)"
    case t if t <:< typeOf[Boolean] => q"$buf.append($value.toString)"
    case other                      => q"$buf.append($value.toString)"
  }


  def renderArray(buf: Tree, values: Tree, quote: Boolean) = {
    q"""
        var firstInArray = true
        $buf.append("[")
        $values.foreach { value =>
          if (firstInArray) {
            firstInArray = false
          } else {
            $buf.append(", ")
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
    val values = q"$value.asInstanceOf[LazySeq].asScala"
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
    val values = q"$value.asInstanceOf[PersistentVector].asScala"
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
              $buf.append(", ")
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
            case v if v.toString.contains(":db/ident") => val s = v.toString; quote($buf, s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                     => quote($buf, v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head) // pull result map: {:ns/str "abc"}
          }
         """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val date = v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head.asInstanceOf[Date]
              quote($buf, df.format(date))
          }
         """
    case t if t <:< typeOf[Option[UUID]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => quote($buf, v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => quote($buf, v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jBoolean]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString) // pull result map: {:ns/int 42}
          }
         """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
          $value match {
            case null                               => $buf.append("null")
            case v if v.toString.contains(":db/id") => val s = v.toString; $buf.append(s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                  => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
          $value match {
            case null => $buf.append("null")
            case v    => $buf.append(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString)
          }
         """
  }

  def jsonOptionSet(buf: Tree, values: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
          $values match {
            case null                                    => $buf.append("null")

            // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
            case vs if vs.toString.contains(":db/ident") =>
              val identMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
              val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].asScala.toMap.values.head.getName)
              ${renderArray(buf, q"enums", true)}

            // {:ns/strs ["a" "b" "c"]}
            case vs => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
       """
    case t if t <:< typeOf[Option[Set[Int]]]     =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
          $values match {
            case null                  => $buf.append("null")

            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            case vs if vs.toString.contains(":db/id") =>
              val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
              Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].asScala.toMap.values.head).toSet.asInstanceOf[Set[Long]])

            // {:ns/longs [3 4 5]}
            case vs => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Double]]]  =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
              ${renderArray(buf, q"values.map(v => df.format(v.asInstanceOf[Date]))", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
          $values match {
            case null => $buf.append("null")
            case vs   => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
  }

  def jsonOptionMap(buf: Tree, value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]] =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """

    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   =>
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
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
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
          $value match {
            case null => $buf.append("null")
            case vs   => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
  }


  // Nested ------------------------------------------------------------------

//  def jsonBaseNested =
//    q"""
//      object jsonBaseNested {
//
//        val jsEscapeChars: Set[Char] =
//          List(('\u00ad', '\u00ad'),
//                ('\u0600', '\u0604'),
//                ('\u070f', '\u070f'),
//                ('\u17b4', '\u17b5'),
//                ('\u200c', '\u200f'),
//                ('\u2028', '\u202f'),
//                ('\u2060', '\u206f'),
//                ('\ufeff', '\ufeff'),
//                ('\ufff0', '\uffff'))
//            .foldLeft(Set[Char]()) {
//              case (set, (start, end)) =>
//                set ++ (start to end).toSet
//            }
//
//        def appendEscapedString(buf: StringBuilder, s: String): Unit = {
//          s.foreach { c =>
//            val strReplacement = c match {
//              case '"'  => "\\\""
//              case '\\' => "\\\\"
//              case '\b' => "\\b"
//              case '\f' => "\\f"
//              case '\n' => "\\n"
//              case '\r' => "\\r"
//              case '\t' => "\\t"
//              // Set.contains will cause boxing of c to Character, try and avoid this
//              case c if (c >= '\u0000' && c < '\u0020') || jsEscapeChars.contains(c) =>
//                "\\u%04x".format(c: Int)
//
//              case _ => ""
//            }
//
//            // Use Char version of append if we can, as it's cheaper.
//            if (strReplacement.isEmpty) {
//              buf.append(c)
//            } else {
//              buf.append(strReplacement)
//            }
//          }
//        }
//
//        def quote(buf: StringBuilder, s: String): Unit = {
//          buf.append('"') //open quote
//          appendEscapedString(buf, s)
//          buf.append('"') //close quote
//        }
//
//        lazy val flatModel: Seq[Element] = {
//          def recurse(element: Element): Seq[Element] = element match {
//            case n: Nested                                             => n.elements flatMap recurse
//            case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
//            case a: Atom                                               => Seq(a)
//            case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
//            case m: Meta                                               => Seq(m)
//            case other                                                 => Seq()
//          }
//          val elements = _modelE.elements flatMap recurse
//          if (elements.size != _queryE.f.outputs.size)
//            sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + _queryE.f.outputs.size + "):\n" +
//              _modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + _queryE + "\n----------------\n")
//          elements
//        }
//
//       lazy val levelFields = $
//
//        lazy val entityIndexes: List[Int] = flatModel.zipWithIndex.collect {
//          case  (Meta(_, _, _, _, IndexVal), i) => i
//        }.toList
//
//        lazy val manyRefIndexes: Seq[Int] = flatModel.zipWithIndex.collect {
//          case  (Meta(_, "many-ref", _, _, IndexVal), i) => i
//        }
//
//        lazy val indexMap: Map[Int, Int] = flatModel.zipWithIndex.foldLeft(0, Seq.empty[(Int, Int)]) {
//          case ((rawIndex, indexMap), (meta, i)) => meta match {
//            case Meta(_, "many-ref", _, _, IndexVal) => (rawIndex, indexMap :+ (rawIndex, i))
//            case Meta(_, _, _, _, IndexVal)          => (rawIndex + 1, indexMap :+ (rawIndex, i))
//            case _                                   => (rawIndex + 1, indexMap :+ (rawIndex, i))
//          }
//        }._2.toMap
//
//        def sortRows(rowSeq: Seq[jList[AnyRef]], entityIndexes: Seq[Int]): Seq[jList[AnyRef]] = entityIndexes match {
//          case List(a)                               => rowSeq.sortBy(row => row.get(a).asInstanceOf[Long])
//          case List(a, b)                            => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long]))
//          case List(a, b, c)                         => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long]))
//          case List(a, b, c, d)                      => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long]))
//          case List(a, b, c, d, e)                   => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long]))
//          case List(a, b, c, d, e, f)                => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long]))
//          case List(a, b, c, d, e, f, g)             => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long]))
//          case List(a, b, c, d, e, f, g, h)          => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long]))
//          case List(a, b, c, d, e, f, g, h, i)       => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long]))
//          case List(a, b, c, d, e, f, g, h, i, j)    => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => row.get(j).asInstanceOf[Long])
//          case List(a, b, c, d, e, f, g, h, i, j, k) => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => (row.get(j).asInstanceOf[Long], row.get(k).asInstanceOf[Long]))
//       }
//
//      } // object jsonBaseNested
//     """
//
//    import molecule.ast.model._
//  import molecule.ast.query._
//  import java.util.{List => jList}
//
//
//  case class JsonBaseNested(_modelE: Model, _queryE: Query) {
//
//
//    val jsEscapeChars: Set[Char] =
//      List(('\u00ad', '\u00ad'),
//            ('\u0600', '\u0604'),
//            ('\u070f', '\u070f'),
//            ('\u17b4', '\u17b5'),
//            ('\u200c', '\u200f'),
//            ('\u2028', '\u202f'),
//            ('\u2060', '\u206f'),
//            ('\ufeff', '\ufeff'),
//            ('\ufff0', '\uffff'))
//        .foldLeft(Set[Char]()) {
//          case (set, (start, end)) =>
//            set ++ (start to end).toSet
//        }
//
//    def appendEscapedString(buf: StringBuilder, s: String): Unit = {
//      s.foreach { c =>
//        val strReplacement = c match {
//          case '"'  => "\\\""
//          case '\\' => "\\\\"
//          case '\b' => "\\b"
//          case '\f' => "\\f"
//          case '\n' => "\\n"
//          case '\r' => "\\r"
//          case '\t' => "\\t"
//          // Set.contains will cause boxing of c to Character, try and avoid this
//          case c if (c >= '\u0000' && c < '\u0020') || jsEscapeChars.contains(c) =>
//            "\\u%04x".format(c: Int)
//
//          case _ => ""
//        }
//
//        // Use Char version of append if we can, as it's cheaper.
//        if (strReplacement.isEmpty) {
//          buf.append(c)
//        } else {
//          buf.append(strReplacement)
//        }
//      }
//    }
//
//    def quote(buf: StringBuilder, s: String): Unit = {
//      buf.append('"') //open quote
//      appendEscapedString(buf, s)
//      buf.append('"') //close quote
//    }
//
//    lazy val flatModel: Seq[Element] = {
//      def recurse(element: Element): Seq[Element] = element match {
//        case n: Nested                                             => n.elements flatMap recurse
//        case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
//        case a: Atom                                               => Seq(a)
//        case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
//        case m: Meta                                               => Seq(m)
//        case other                                                 => Seq()
//      }
//      val elements = _modelE.elements flatMap recurse
//      if (elements.size != _queryE.f.outputs.size)
//        sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + _queryE.f.outputs.size + "):\n" +
//          _modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + _queryE + "\n----------------\n")
//      elements
//    }
//
////    def levelFields(elements: Seq[Element]): Map[Int, Seq[String]] = {
//    lazy val levelFields: Map[Int, Seq[String]] = {
//        /*
//          m(Ns.int.str * Refs1.int1)
//
//          Seq(
//            0 -> "int",
//            0 -> "str",
//            0 -> "refs1",
//            1 -> "int1"
//          )
//        */
//        def recurse(level: Int, element: Element): Seq[(Int, String)] = element match {
//          case Atom(_, attr, _, _, _, _, _, _)             => Seq(level -> attr)
//          case Nested(Bond(_, refAttr, _, _, _), elements) => (level -> refAttr) +: elements.flatMap(e => recurse(level + 1, e))
//          case other                                       => Nil
//        }
//        val levelFields0: Seq[(Int, String)] = _modelE.elements.flatMap(e => recurse(0, e))
//
//        /*
//          Map(
//            0 -> Seq("int", "str", "refs1"),
//            1 -> Seq("int1")
//          )
//        */
//        levelFields0.foldLeft(0, Seq.empty[(Int, Seq[String])]) {
//          case ((prevLevel, acc), (level, field)) if acc.isEmpty        => (level, Seq(0 -> Seq(field)))
//          case ((prevLevel, acc), (level, field)) if prevLevel != level => (level, acc :+ (level -> Seq(field)))
//          case ((prevLevel, acc), (level, field))                       => (level, acc.init :+ (acc.last._1 -> (acc.last._2 :+ field)))
//        }._2.toMap
//      }
//
//    lazy val entityIndexes: List[Int] = flatModel.zipWithIndex.collect {
//      case  (Meta(_, _, _, _, IndexVal), i) => i
//    }.toList
//
//    lazy val manyRefIndexes: Seq[Int] = flatModel.zipWithIndex.collect {
//      case  (Meta(_, "many-ref", _, _, IndexVal), i) => i
//    }
//
//    lazy val indexMap: Map[Int, Int] = flatModel.zipWithIndex.foldLeft(0, Seq.empty[(Int, Int)]) {
//      case ((rawIndex, indexMap), (meta, i)) => meta match {
//        case Meta(_, "many-ref", _, _, IndexVal) => (rawIndex, indexMap :+ (rawIndex, i))
//        case Meta(_, _, _, _, IndexVal)          => (rawIndex + 1, indexMap :+ (rawIndex, i))
//        case _                                   => (rawIndex + 1, indexMap :+ (rawIndex, i))
//      }
//    }._2.toMap
//
//    def sortRows(rowSeq: Seq[jList[AnyRef]], entityIndexes: Seq[Int]): Seq[jList[AnyRef]] = entityIndexes match {
//      case List(a)                               => rowSeq.sortBy(row => row.get(a).asInstanceOf[Long])
//      case List(a, b)                            => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long]))
//      case List(a, b, c)                         => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long]))
//      case List(a, b, c, d)                      => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long]))
//      case List(a, b, c, d, e)                   => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f)                => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g)             => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h)          => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h, i)       => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long]))
//      case List(a, b, c, d, e, f, g, h, i, j)    => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => row.get(j).asInstanceOf[Long])
//      case List(a, b, c, d, e, f, g, h, i, j, k) => rowSeq.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => (row.get(j).asInstanceOf[Long], row.get(k).asInstanceOf[Long]))
//   }
//
//  } // JsonBaseNested



//  def nestedJson(query: Tree, rows: Tree, tpes: Seq[Type]) = {
//    q"""
//      if ($rows.isEmpty) {
//        ""
//      } else {
//        ..${nestedJson1(query, rows, tpes)}
//        nestedJson1.get
//      }
//     """
//  }
//
////  import molecule.ast.model._
////  import molecule.ast.query._
////val zz = JsonBaseNested(null: Model, null: Query)
////  import zz._
////  indexMap
//// Warning:scala: skipping Scala files without a Scala SDK in module(s) moleculeCore
//
//  def nestedJson1(query: Tree, rows: Tree, tpes: Seq[Type]) = {
//    val typeCount = tpes.size
//    q"""
//        import molecule.factory.JsonBaseNested
//        val jsonBaseNested = JsonBaseNested(_modelE, _queryE)
//        import jsonBaseNested._
//
//        object nestedJson1 {
//
////println("===================================================================================")
////println(_model)
////println(_modelE)
////println(_queryE)
////println(_queryE.datalog)
////println("---- ")
////flatModel foreach println
//levelFields foreach println
//println("---- " + entityIndexes)
//println("---- " + manyRefIndexes)
//println("---- " + indexMap)
//
//        val sortedRows = sortRows($rows.toSeq, entityIndexes)
//
//sortedRows foreach println
//
//        val rowCount = sortedRows.length
//
//        var firstEntry = true
//        val buf0 = new StringBuilder("[")
//        val buffers = buf0 :: entityIndexes.tail.map(_ => new StringBuilder(""))
//
//        sortedRows.foldLeft(Seq.empty[Long], 1) { case ((prevEntities, r), row0) =>
//
//          val row = row0.asScala.asInstanceOf[Seq[Any]]
//          val curEntities = entityIndexes.map(i => row(i).asInstanceOf[Long])
//
//println("------------------ " + r + " --------------------------------")
//
//          (0 until entityIndexes.size).reverse.foreach { level =>
//
//            level match {
//              case level if r == 1  => {
//
//              }
//
//              case 0     => {
//                val (prevE, curE) = (prevEntities(0), curEntities(0))
//                if(prevE != curE) {
//                  println("level 0: new")
//                } else {
//                  println("level 0: same")
//                }
//              }
//
//              case level => {
//                val (prevParentE, curParentE) = (prevEntities(level - 1), curEntities(level - 1))
//                val (prevE, curE) = (prevEntities(level), curEntities(level))
//
//                if(prevE != curE) {
//                  println("level " + level + ": new")
//                } else {
//                  println("level " + level + ": same")
//                }
//                if(prevParentE != curParentE) {
//                  println("parent : new")
//                } else {
//                  println("parent : same")
//                }
//              }
//            }
//
//          }
//
//          (curEntities, r + 1)
//
//        } // rows loop
//
//        def get = buf0.append("\n]").toString()
//
//      } //object nestedJson1
//      """
//
//  }
//
//  def jsonResolveNested(buf: Tree, fields: Tree,
//                        query: Tree, refAttr: Tree, elements: Tree, tpes: Seq[Type],
//                        prevTpl: Tree, prevRow: Tree, row: Tree, entityIndex: Int, depth: Int, maxDepth: Tree, shift: Int): Tree = {
//
//    def resolve(nestedTpes: Seq[Type], typeIndex: Int, nestedBuf: Tree): Tree = {
//      val rowIndex = entityIndex + shift + typeIndex
//      //println(tab + "rowIndex : " + $rowIndex + " (" + $entityIndex + "+" + $shift + "+" + $typeIndex + ")")
//      q"""
//        object resolve {
//          val prevEnt = if($prevRow.head == 0L) 0L else $prevRow.apply($entityIndex).asInstanceOf[Long]
//          val curEnt = $row.apply($entityIndex).asInstanceOf[Long]
//          val isNewNested = if (prevEnt == 0L) true else prevEnt != curEnt
//          val isNewManyRef = manyRefIndexes.nonEmpty && prevEnt != 0L && $prevRow.apply(manyRefIndexes.head) != $row.apply(manyRefIndexes.head)
//          val (refAttr, nestedElements): (String, Seq[Element]) = $elements.collectFirst {
//            case Nested(Bond(_, refAttr, _, _, _), elements) => (refAttr, elements)
//          }.get
//
//val tab = "  " * $rowIndex
////println(tab + "entities : " + prevEnt + "   " + curEnt + "   " + isNewNested + "   " + isNewManyRef)
////println(tab + "refAttr  : " + refAttr)
////println(tab + "elements : " + nestedElements.mkString("\n" + tab, "\n" + tab, ""))
//
//          val result = if ($prevTpl.isEmpty || isNewNested|| isNewManyRef) {
//
//            // ==========================================================================
//
//            val toAdd = ${jsonResolveNested(nestedBuf, fields, query, q"refAttr", q"elements", nestedTpes, q"Option.empty[String]", prevRow, row, rowIndex, depth + 1, maxDepth, shift)}
//println(tab + "a toAdd  : " + toAdd)
////println(tab + "a added  : " + Seq(toAdd))
//
////            Seq(toAdd)
//            toAdd
//
//          } else if ($prevTpl.isInstanceOf[Seq[_]]) {
//
//            // ==========================================================================
//println(tab + "b prevTpl: " + $prevTpl)
//
//            val toAdd = ${jsonResolveNested(nestedBuf, fields, query, q"refAttr", q"elements", nestedTpes, prevTpl, prevRow, row, rowIndex, depth + 1, maxDepth, shift)}
//println(tab + "b toAdd  : " + toAdd)
//
//            val added = $prevTpl + toAdd
//println(tab + "b added  : " + added)
//            added
//
//          } else {
//
//            // ==========================================================================
//println(tab + "c prevTpl: " + $prevTpl)
//
//            val tpl = $nestedBuf.toString
//println(tab + "c tpl    : " + tpl)
//
//            val toAdd = ${jsonResolveNested(nestedBuf, fields, query, q"refAttr", q"elements", nestedTpes, prevTpl, prevRow, row, rowIndex, depth + 1, maxDepth, shift)}
//
//            val adjustedIndex = indexMap($rowIndex)
//            val newNested = $prevRow.apply(adjustedIndex).asInstanceOf[Long] != $row.apply(adjustedIndex).asInstanceOf[Long] || $depth == $maxDepth
//            val isNewManyRef = manyRefIndexes.nonEmpty && $prevRow.apply(manyRefIndexes.head) != $row.apply(manyRefIndexes.head)
//println(tab + "c toAdd  : " + toAdd + "    " + newNested + "    " + isNewManyRef)
//
//            val added = tpl + toAdd
////            if (newNested) {
////              tpl + toAdd
////            } else {
////              tpl.init :+ toAdd
////            }
//println(tab + "c added  : " + added)
//            added
//          }
//        }
//
//        resolve.result
//       """
//    }
//
//
//    q"""
//      $buf.append("\n")
//      $buf.append("   " * ${depth - 1})
//      $buf.append("{")
//      ..${
//      tpes.zipWithIndex.foldLeft((shift, q"")) { case ((shift, accTree), (t, typeIndex)) =>
//        t match {
//          case tpe if tpe <:< weakTypeOf[Seq[Product]] =>
//            val nestedTpes = tpe.typeArgs.head.typeArgs
//            val newTree =
//              q"""
//                ..$accTree
//                val nestedBuf = new StringBuilder(", ")
//                quote(nestedBuf, $fields.last)
//                nestedBuf.append(": [")
//                ${resolve(nestedTpes, typeIndex, q"nestedBuf")}
//                nestedBuf.append("]")
//                $buf.append(nestedBuf.toString)
//              """
//            (shift + nestedTpes.length, newTree)
//
//          case tpe if tpe <:< weakTypeOf[Seq[_]] =>
//            val nestedTpe = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head
//            val newTree =
//              q"""
//                ..$accTree
//                val nestedBuf = new StringBuilder(", ")
//                quote(nestedBuf, $fields.last)
//                nestedBuf.append(": [")
//                ${resolve(Seq(nestedTpe), typeIndex, q"nestedBuf")}
////                nestedBuf.append("]")
//                $buf.append(nestedBuf.toString)
//              """
//            (shift + 1, newTree)
//
//          case tpe =>
//            val newTree =
//              q"""
//                ..$accTree
//                ${toJson(buf, query, fields, q"$row.asJava", tpe, q"indexMap(${entityIndex + shift + typeIndex})", typeIndex)}
//              """
//            (shift, newTree)
//        }
//      }._2
//    }
////       buf.append("}")
//      """
//  }
}
