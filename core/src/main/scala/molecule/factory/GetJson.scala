package molecule.factory

import java.net.URI
import java.util.{Date, UUID}

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

private[molecule] trait GetJson[Ctx <: Context] extends Base[Ctx] {
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
          val fields0_OLD = $model.elements.collect {
            case Atom(_, attr, _, _, _, _, _, _) => attr
          }
          val fields0 = $model.elements.foldLeft(Seq.empty[String], Seq.empty[String]) {
            case ((Nil, fields), Meta(ns, _, "e", _, _))            => (Nil, fields :+ ns + ".e")
            case ((Nil, fields), Atom(ns, attr, _, _, _, _, _, _))  => (Nil, fields :+ ns + "." + attr)
            case ((refs, fields), Meta(ns, _, "e", _, _))           => (refs, fields :+ refs.last + "." + ns + ".e")
            case ((refs, fields), Atom(ns, attr, _, _, _, _, _, _)) => (refs, fields :+ refs.last + "." + ns + "." + attr)
            case ((refs, fields), Bond(ns, refAttr, refNs, _, _))   => (refs :+ refAttr, fields)
            case ((refs, fields), r: ReBond)                        => (refs.init, fields)
            case ((refs, fields), other)                            => throw new IllegalArgumentException("Unexpected Molecule element: " + other)
          }._2

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
          ..${jsonPairs(q"buf", query, q"fields", q"row", tpes)}
          buf.append("}")
        }
        buf.append("\n]").toString()
     """
  }


  def jsonPairs(buf: Tree, query: Tree, fields: Tree, row: Tree, tpes: Seq[Type], offset: Int = 0) = {
    tpes.zipWithIndex.map { case (tpe, i) => jsonPair(buf, query, fields, row, tpe, offset, i) }
  }


  def jsonPair(buf: Tree, query: Tree, fields: Tree, row: Tree, tpe: Type, offset: Int, index0: Int) = {
    val index = offset + index0
    val field: Tree =
      q"""
          if($index >= $fields.size)
            "[Unknown]"
          else if(${tpe <:< typeOf[Option[_]]})
            $fields($index).init // skip appended '$$' in optional attr name
          else
            $fields($index)
        """
    val value: Tree = q"if($index >= $row.size) null else $row.get($index)"
    q"""
        if($index0 > 0) $buf.append(", ")
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
        case t                                         => jsonValue(buf, query, value, t, index)
      }
    }
      """
  }


  def jsonValue(buf: Tree, query: Tree, value: Tree, tpe: Type, rowIndex: Int) = tpe match {
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
                $buf.append(", ")
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
            case v: String                             => quote($buf, v)
            case v if v.toString.contains(":db/ident") => val s = v.toString; quote($buf, s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                     => quote($buf, v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head) // pull result map: {:ns/str "abc"}
          }
         """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
          $value match {
            case null     => $buf.append("null")
            case v: jLong => $buf.append(v)
            case v        => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString) // pull result map: {:ns/int 42}
          }
         """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
          $value match {
            case null       => $buf.append("null")
            case v: jDouble => $buf.append(v)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
          $value match {
            case null                               => $buf.append("null")
            case v: jLong                           => $buf.append(v)
            case v if v.toString.contains(":db/id") => val s = v.toString; $buf.append(s.substring(s.lastIndexOf("/")+1).init.init)
            case v                                  => $buf.append(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
          $value match {
            case null       => $buf.append("null")
            case v: jDouble => $buf.append(v)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
          $value match {
            case null        => $buf.append("null")
            case v: jBoolean => $buf.append(v)
            case v           => $buf.append(v.asInstanceOf[jMap[String, jBoolean]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
          $value match {
            case null       => $buf.append("null")
            case v: jBigInt => $buf.append(v)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
          $value match {
            case null       => $buf.append("null")
            case v: jBigDec => $buf.append(v)
            case v          => $buf.append(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
          $value match {
            case null    => $buf.append("null")
            case v: Date =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              quote($buf, df.format(v.asInstanceOf[Date]))
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
            case v: UUID => quote($buf, v.toString)
            case v       => quote($buf, v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head.toString)
          }
         """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
          $value match {
            case null   => $buf.append("null")
            case v: URI => quote($buf, v.toString)
            case v      => quote($buf, v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head.toString)
          }
         """
  }

  def jsonOptionSet(buf: Tree, values: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", true)}

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
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}

            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            case vs if vs.toString.contains(":db/id") =>
              val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala.toSeq
              val ids = idMaps.map(_.asInstanceOf[jMap[clojure.lang.Keyword, Any]].asScala.toSeq.collectFirst{case (keyw, id) if keyw.toString == ":db/id" => id.asInstanceOf[Long]}.get)
              Some(ids.toSet.asInstanceOf[Set[Long]])

            // {:ns/longs [3 4 5]}
            case vs => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Double]]]  =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala", false)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val values = vs.asScala.map(_.asInstanceOf[Date])
              ${renderArray(buf, q"values.map(v => df.format(v.asInstanceOf[Date]))", true)}
            case vs                    =>
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala
              ${renderArray(buf, q"values.map(v => df.format(v.asInstanceOf[Date]))", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala.map(_.toString)", true)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
          $values match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderArray(buf, q"vs.asScala.map(_.toString)", true)}
            case vs                    => ${renderArray(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
  }

  def jsonOptionMap(buf: Tree, value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]] =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", true)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """

    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala", false)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", false)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet =>
              val values = vs.asScala
              val df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
              var firstInObj = true
              $buf.append("{")
              values.foreach { case s: String =>
                val p = s.split("@", 2)
                val (k, d) = (p(0), df.parse(p(1)))

                if (firstInObj) {
                  firstInObj = false
                } else {
                  $buf.append(", ")
                }

                $buf.append('"')
                $buf.append(k)
                $buf.append('"')
                $buf.append(':')
                quote($buf, df.format(d))
              }
              $buf.append("}")

            case vs                    =>
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
                  $buf.append(", ")
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
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala.map(_.toString)", true)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
          $value match {
            case null                  => $buf.append("null")
            case vs: PersistentHashSet => ${renderObj(buf, q"vs.asScala.map(_.toString)", true)}
            case vs                    => ${renderObj(buf, q"vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asScala", true)}
          }
         """
  }


  // Composites ------------------------------------------------------------------

  def compositeJson(model: Tree, query: Tree, rows: Tree, outerTypes: Seq[Type]): Tree = {

    def walkOuterTuple(buf: Tree, fields: Tree, row: Tree) = outerTypes.foldLeft(Seq.empty[Tree], 0) {
      case ((accTree, offset), tupleType) if tupleType <:< weakTypeOf[Product] =>
        val valueTypes = tupleType.typeArgs
        val tree =
          q"""
           if($offset > 0) $buf.append(", ")
           $buf.append("{")
           ..${jsonPairs(buf, query, fields, row, valueTypes, offset)}
           $buf.append("}")
         """
        (accTree :+ tree, offset + valueTypes.size)

      case ((accTree, offset), valueType) =>
        val tree =
          q"""
           if($offset > 0) $buf.append(", ")
           $buf.append("{")
           ..${jsonPair(buf, query, fields, row, valueType, offset, 0)}
           $buf.append("}")
         """
        (accTree :+ tree, offset + 1)
    }._1

    q"""
        ..$jsonBase

        def getFieldNames(es: Seq[Element], tx: Boolean = false): Seq[String] = es.collect {
          case Composite(subElements)                 => getFieldNames(subElements)
          case TxMetaData(txElements)                 => getFieldNames(Meta("", "tx", "tx", NoValue, EntValue) +: txElements, true)
          case TxMetaData_(txElements)                => getFieldNames(txElements, true)
          case Meta(_, _, _, _, _) if tx              => Seq("tx")
          case Meta(_, _, kind, _, _)                 => Seq(kind)
          case Atom(ns, attr, _, _, _, _, _, _) if tx => Seq("tx." + ns + "." + attr)
          case Atom(ns, attr, _, _, _, _, _, _)       => Seq(ns + "." + attr)
        }.flatten

        val fields = getFieldNames($model.elements)

        var firstRow = true
        val buf = new StringBuilder("[")
        $rows.foreach { row =>
          if (firstRow) {
            firstRow = false
          } else {
            buf.append(",")
          }
          buf.append("\n[")
          ..${walkOuterTuple(q"buf", q"fields", q"row")}
          buf.append("]")
        }
        buf.append("\n]").toString()
     """
  }
}
