package molecule.factory

import java.net.URI
import java.util.{Date, UUID}

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

trait GetTuples[Ctx <: Context] extends GetJson[Ctx] {
  import c.universe._


  // Without nested/composites ------------------------------------------------------------------

  def tuple(query: Tree, row: Tree, tpes: Seq[Type]): Seq[Tree] = tpes.zipWithIndex.map { case (tpe, i) => cast(query, row, tpe, q"$i") }

  def cast(query: Tree, row: Tree, tpe: Type, i: Tree): Tree = {
    val value: Tree =
      q"""
         val value = if($i >= $row.size) null else $row.get($i)
//         println(i + ": " + value.toString)
         value
        """
    tpe match {
      case t if t <:< typeOf[Option[Map[String, _]]] => castOptionMap(value, t)
      case t if t <:< typeOf[Option[Set[_]]]         => castOptionSet(value, t)
      case t if t <:< typeOf[Option[_]]              => castOption(value, t)
      case t if t <:< typeOf[Map[String, _]]         => castMap(value, t)
      case t if t <:< typeOf[Vector[_]]              => q"$value.asInstanceOf[PersistentVector].asScala.toVector.asInstanceOf[$t]"
      case t if t <:< typeOf[Stream[_]]              => q"$value.asInstanceOf[LazySeq].asScala.toStream.asInstanceOf[$t]"
      case t if t <:< typeOf[Set[_]]                 => castSet(value, t)
      case t                                         => castType(query, value, t, i)
    }
  }

  def castType(query: Tree, value: Tree, tpe: Type, i: Tree) = tpe match {
    case t if t <:< typeOf[Long]       => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toLong else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[Double]     => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toDouble else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[Boolean]    => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toBoolean else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[BigInt]     => q"BigInt($value.toString).asInstanceOf[$t]"
    case t if t <:< typeOf[BigDecimal] => q"BigDecimal($value.toString).asInstanceOf[$t]"
    case t if t <:< typeOf[Date]       => q"(if($value.isInstanceOf[String]) date($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[UUID]       => q"(if($value.isInstanceOf[String]) UUID.fromString($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[URI]        => q"(if($value.isInstanceOf[String]) new URI($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
    case t if t <:< typeOf[Int]        =>
      q"""
        val i = $value match {
          case l: jLong  => l.toInt.asInstanceOf[$t]
          case s: String => s.toInt.asInstanceOf[$t]
          case other     => other.asInstanceOf[$t]
        }

//          println("int: " + i + "    (" + value.toString + ")")
        i
      """
    case t if t <:< typeOf[Float]      =>
      q"""
        $value match {
          case d: jDouble => d.toFloat.asInstanceOf[$t]
          case s: String  => s.toFloat.asInstanceOf[$t]
          case other      => other.asInstanceOf[$t]
        }
      """
    case t /* String */                =>
      q"""

        val s = $query.f.outputs($i) match {
          case AggrExpr("sum",_,_)    => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) $value.asInstanceOf[jLong].toInt.asInstanceOf[$t] else $value.asInstanceOf[$t]
            case "Float" => if($value.isInstanceOf[jDouble]) $value.asInstanceOf[jDouble].toFloat.asInstanceOf[$t] else $value.asInstanceOf[$t]
            case _       => $value.asInstanceOf[$t]
          }
          case AggrExpr("median",_,_) => ${t.toString} match {
            case "Int"   => if($value.isInstanceOf[jLong]) $value.asInstanceOf[jLong].toInt.asInstanceOf[$t] else $value.asInstanceOf[$t]
            case "Float" => if($value.isInstanceOf[jDouble]) $value.asInstanceOf[jDouble].toFloat.asInstanceOf[$t] else $value.asInstanceOf[$t]
            case _       => $value.asInstanceOf[$t]
          }
          case other                  => $value.asInstanceOf[$t]
        }

//        println("str: " + s + "    (" + value.toString + ")")
        s
      """
  }

  def castSet(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Set[Int]]        => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[Float]]      => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[BigInt]]     => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{ case i: jBigInt => BigInt(i)}.toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[BigDecimal]] => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{ case d: jBigDec => BigDecimal(d.toString)}.toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[_]]          => q"$value.asInstanceOf[PersistentHashSet].asScala.toSet.asInstanceOf[$t]"
  }

  def castMap(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Map[String, String]]     => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Int]]        => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Long]]       => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Float]]      => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Double]]     => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Boolean]]    => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, BigInt]]     => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigInt(p(1).toString)}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, BigDecimal]] => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigDecimal(p(1).toString)}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, Date]]       => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, UUID]]       => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap.asInstanceOf[$t]"""
    case t if t <:< typeOf[Map[String, URI]]        => q"""$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap.asInstanceOf[$t]"""
  }


  // Optionals ...................

  def castOption(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[String]]     =>
      q"""
        $value match {
          case null                                  => Option.empty[String]
//          case v: String                             => Some(v)
          case v if v.toString.contains(":db/ident") => val v2 = v.toString; Some(v2.substring(v2.lastIndexOf("/")+1).init.init)
          case v                                     => Some(v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
        $value match {
          case null     => Option.empty[Int]
//          case v: jLong => Some(v.asInstanceOf[jLong].toInt)
          case v        => Some(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toInt) // pull result map: {:ns/int 42}
        }
       """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
        $value match {
          case null       => Option.empty[Float]
//          case v: jDouble => Some(v.asInstanceOf[jDouble].toFloat)
          case v          => Some(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toFloat)
        }
       """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
        $value match {
          case null                               => Option.empty[Long]
//          case v: jLong                           => Some(v.asInstanceOf[jLong].toLong)
          case v if v.toString.contains(":db/id") => val v2 = v.toString; Some(v2.substring(v2.lastIndexOf(" ")+1).init.init.toLong)
          case v                                  => Some(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toLong)
        }
       """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
        $value match {
          case null       => Option.empty[Double]
//          case v: jDouble => Some(v.asInstanceOf[jDouble].toDouble)
          case v          => Some(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toDouble)
        }
       """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
        $value match {
          case null        => Option.empty[Boolean]
//          case v: jBoolean => Some(v.asInstanceOf[Boolean])
          case v           => Some(v.asInstanceOf[jMap[String, Boolean]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
        $value match {
          case null       => Option.empty[BigInt]
//          case v: jBigInt => Some(BigInt(v.asInstanceOf[jBigInt].toString))
          case v          => Some(BigInt(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString))
        }
       """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
        $value match {
          case null       => Option.empty[BigDecimal]
//          case v: jBigDec => Some(BigDecimal(v.asInstanceOf[jBigDec].toString))
          case v          => Some(BigDecimal(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString))
        }
       """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
        $value match {
          case null    => Option.empty[Date]
//          case v: Date => Some(v)
          case v       => Some(v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[UUID]]       =>
      q"""
        $value match {
          case null    => Option.empty[UUID]
//          case v: UUID => Some(v)
          case v       => Some(v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
        $value match {
          case null   => Option.empty[URI]
//          case v: URI => Some(v)
          case v      => Some(v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head)
        }
       """
  }

  def castOptionSet(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
        $value match {
          case null                                    => Option.empty[Set[String]]
//          case vs: PersistentHashSet                   => Some(vs.asScala.map(_.toString).toSet.asInstanceOf[Set[String]])
          case vs if vs.toString.contains(":db/ident") =>
            // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
            val identMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].asScala.toMap.values.head.getName)
            Some(enums.toSet.asInstanceOf[Set[String]])
          case vs                                      =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[String]])
        }
       """
    case t if t <:< typeOf[Option[Set[Int]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Int]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[Set[Int]])
          case vs                    =>
            val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            Some(values.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[Set[Int]])
        }
       """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Float]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[Set[Float]])
          case vs                    =>
            val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            Some(values.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[Set[Float]])
        }
       """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
        $value match {
          case null                                 => Option.empty[Set[Long]]
//          case vs: PersistentHashSet                => Some(vs.asScala.map(_.asInstanceOf[jLong].toLong).toSet.asInstanceOf[Set[Long]])
          case vs if vs.toString.contains(":db/id") =>
            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            val idMaps = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].asScala.toMap.values.head).toSet.asInstanceOf[Set[Long]])
          case vs                                   =>
            // {:ns/longs [3 4 5]}
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Long]])
        }
       """
    case t if t <:< typeOf[Option[Set[Double]]]  =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Double]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jDouble].toDouble).toSet.asInstanceOf[Set[Double]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Double]])
        }
       """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Boolean]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[Boolean]).toSet.asInstanceOf[Set[Boolean]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Boolean]])
        }
       """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
        $value match {
          case null                  => Option.empty[Set[BigInt]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(v => BigInt(v.asInstanceOf[jBigInt].toString)).toSet.asInstanceOf[Set[BigInt]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala
            .map(v => BigInt(v.asInstanceOf[jBigInt].toString)).toSet.asInstanceOf[Set[BigInt]])
        }
       """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Set[BigDecimal]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(v => BigDecimal(v.asInstanceOf[jBigDec].toString)).toSet.asInstanceOf[Set[BigDecimal]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala
            .map(v => BigDecimal(v.asInstanceOf[jBigDec].toString)).toSet.asInstanceOf[Set[BigDecimal]])
        }
       """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Date]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[Date]).toSet.asInstanceOf[Set[Date]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Date]])
        }
       """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Set[UUID]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[UUID]).toSet.asInstanceOf[Set[UUID]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[UUID]])
        }
       """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Set[URI]]
//          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[URI]).toSet.asInstanceOf[Set[URI]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[URI]])
        }
       """
  }

  def castOptionMap(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, String]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Int]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Long]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Float]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Double]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Boolean]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, BigInt]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> BigInt(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigInt(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, BigDecimal]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> BigDecimal(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigDecimal(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Date]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, UUID]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, UUID]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, URI]]
//          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap)
        }
       """
  }


  // Nested ------------------------------------------------------------------


  def nestedTuples(query: Tree, rows: Tree, tpes: Seq[Type]) = {
    q"""
      if ($rows.isEmpty) {
        Seq[(..$tpes)]()
      } else {
        ..${nestedTuples1(query, rows, tpes)}
        nestedTuples1.get
      }
     """
  }

  def nestedTuples1(query: Tree, rows: Tree, tpes: Seq[Type]) = {
    q"""
        import molecule.factory.TupleBaseNested
        val tupleBaseNested = TupleBaseNested(_modelE, _queryE)
        import tupleBaseNested._

        val doDebug = false
        def debug(s: String): Unit = {
          if(doDebug)
            println(s)
        }

        object nestedTuples1 {

//debug("===================================================================================")
//debug(_model)
//debug(_modelE)
//debug(_queryE)
//debug(_queryE.datalog)
//debug("---- ")
//flatModel foreach debug
//debug("---- " + entityIndexes)
//debug("---- " + indexMap)

         val sortedRows = sortRows($rows.toSeq, entityIndexes)

//sortedRows foreach debug

          val rowCount = sortedRows.length

          val tuples = sortedRows.foldLeft((Seq[(..$tpes)](), None: Option[(..$tpes)], Seq(0L), Seq[Any](0L), 1)) { case ((accTpls0, prevTpl, prevEntities, prevRow, r), row0) =>

            val row = row0.asScala.asInstanceOf[Seq[Any]]

//debug("--- " + r + " ---------------------------------------------------")
            val currentEntities = entityIndexes.map(i => row.apply(i).asInstanceOf[Long])
            val manyRefEntities = manyRefIndexes.map(i => row.apply(i).asInstanceOf[Long])


            val isLastRow = rowCount == r
            val isNewTpl = prevEntities.head != 0 && currentEntities.head != prevEntities.head
            val isNewManyRef = prevEntities.head != 0 && manyRefEntities.nonEmpty && !prevEntities.contains(manyRefEntities.head)
//debug("currentEntities: " + currentEntities)
//debug("manyRefEntities: " + manyRefEntities)
//debug("TPL0 " + prevTpl + "    " + isNewTpl + "    " + isNewManyRef)

            val tpl = ${resolveNested(query, tpes, q"prevTpl", q"prevRow", q"row", 0, 1, q"entityIndexes.size - 1", 1)}

//debug("TPL1 " + tpl)

            val accTpls = if (isLastRow && (isNewTpl || isNewManyRef)) {
              // Add previous and current tuple
//debug("TPL2 " + (accTpls0 ++ Seq(prevTpl.get, tpl)).toString)
              accTpls0 ++ Seq(prevTpl.get, tpl)

            } else if (isLastRow) {
              // Add current tuple
//debug("TPL3 " + (accTpls0 :+ tpl).toString)
              accTpls0 :+ tpl

            } else if (isNewTpl || isNewManyRef) {
              // Add finished previous tuple
//debug("TPL4 " + (accTpls0 :+ prevTpl.get).toString)
              accTpls0 :+ prevTpl.get

            } else {
              // Continue building current tuple
//debug("TPL5 " + accTpls0.toString)
              accTpls0
            }

//debug("ACC " + accTpls)

            (accTpls, Some(tpl), currentEntities, row, r + 1)
          }._1

          def get = tuples

        } // object nestedTuples1
      """
  }

  def resolveNested(query: Tree, tpes: Seq[Type], prevTpl: Tree, prevRow: Tree, row: Tree, entityIndex: Int, depth: Int, maxDepth: Tree, shift: Int): Tree = {

    def resolve(nestedTpes: Seq[Type], typeIndex: Int): Tree = {
      val rowIndex = entityIndex + shift + typeIndex
//val tab = "  " * $rowIndex
//debug(tab + "rowIndex : " + $rowIndex + " (" + $entityIndex + "+" + $shift + "+" + $typeIndex + ")")
      q"""
        object resolve {
          val prevEnt = if($prevRow.head == 0L) 0L else $prevRow.apply($entityIndex).asInstanceOf[Long]
          val curEnt = $row.apply($entityIndex).asInstanceOf[Long]
          val isNewNested = if (prevEnt == 0L) true else prevEnt != curEnt
          val isNewManyRef = manyRefIndexes.nonEmpty && prevEnt != 0L && $prevRow.apply(manyRefIndexes.head) != $row.apply(manyRefIndexes.head)

//debug(tab + "entities : " + prevEnt + "   " + curEnt + "   " + isNewNested + "   " + isNewManyRef)


          val result = if ($prevTpl.isEmpty || isNewNested|| isNewManyRef) {
            // ==========================================================================

//debug(tab + "a prevTpl: " + prevTpl)

            val toAdd = ${
        resolveNested(query, nestedTpes, q"None: Option[(..$tpes)]",
          prevRow, row, rowIndex, depth + 1, maxDepth, shift)
      }
//debug(tab + "a toAdd  : " + toAdd)
//debug(tab + "a added  : " + Seq(toAdd))

            Seq(toAdd)


          } else if ($prevTpl.isDefined && $prevTpl.get.isInstanceOf[::[_]]) {
            // ==========================================================================

//debug(tab + "b prevTpl: " + prevTpl + "    " + prevTpl.getOrElse(null).getClass)
//debug(tab + "b prevTpl: " + prevTpl.getOrElse(null).isInstanceOf[Seq[_]])
//debug(tab + "b prevTpl: " + prevTpl.getOrElse(null).isInstanceOf[::[_]])

//debug(tab + "c tpes   : " + ..{tpes.map(_.toString)})
//debug(tab + "c nestedt: " + ..{nestedTpes.map(_.toString)})


            val tpl1: (..$tpes) = $prevTpl.get.asInstanceOf[(..$tpes)]
//debug(tab + "c tpl1   : " + tpl1 + "    " + prevTpl.get.getClass + "    " + tpl1.getClass)

            val toAdd = ${
        resolveNested(query, nestedTpes,
          q"Some($prevTpl.get.asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
          prevRow, row, rowIndex, depth + 1, maxDepth, shift)
      }.asInstanceOf[(..$nestedTpes)]
//debug(tab + "b toAdd  : " + toAdd)

            val added = $prevTpl.get.asInstanceOf[Seq[(..$nestedTpes)]] :+ toAdd
//debug(tab + "b added  : " + added)
            added


          } else {
            // ==========================================================================
//debug(tab + "c prevTpl: " + prevTpl)

            ${
        if(tpes.size == 1) {
          // Even though we never get here, the compiler only accepts this
          q"null.asInstanceOf[(..$nestedTpes)]"
        } else {
          q"""
            val tpl1 = $prevTpl.get.asInstanceOf[(..$tpes)]
//debug(tab + "c tpl1   : " + tpl1 + "    " + prevTpl.getClass + "    " + tpl1.getClass)

            val tpl2 = tpl1.productElement($typeIndex).asInstanceOf[Seq[(..$nestedTpes)]]
//debug(tab + "c tpl2   : " + tpl2)

            val toAdd = ${
            resolveNested(query, nestedTpes,
              q"Some($prevTpl.get.asInstanceOf[(..$tpes)].productElement($typeIndex).asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
              prevRow, row, rowIndex, depth + 1, maxDepth, shift)
          }.asInstanceOf[(..$nestedTpes)]

            val adjustedIndex = indexMap($rowIndex)
            val newNested = $prevRow.apply(adjustedIndex).asInstanceOf[Long] != $row.apply(adjustedIndex).asInstanceOf[Long] || $depth == $maxDepth
            val isNewManyRef = manyRefIndexes.nonEmpty && $prevRow.apply(manyRefIndexes.head) != $row.apply(manyRefIndexes.head)
//debug(tab + "c toAdd  : " + toAdd)

            val added = if (newNested) {
              tpl2 :+ toAdd
            } else {
              tpl2.init :+ toAdd
            }
//debug(tab + "c added  : " + added + "    " + newNested + "    " + isNewManyRef)
            added
           """
        }
      }
          }
        }
        resolve.result
       """
    }

    val values = tpes.zipWithIndex.foldLeft(shift, Seq.empty[Tree]) { case ((shift, vs), (t, typeIndex)) =>
      t match {

        case tpe if tpe <:< weakTypeOf[Seq[Product]] =>
          val nestedTpes = tpe.typeArgs.head.typeArgs
//          x(2, tpe, nestedTpes)
          (shift + nestedTpes.length, vs :+ resolve(nestedTpes, typeIndex))

        case tpe if tpe <:< weakTypeOf[Seq[_]] =>
          val nestedTpe = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head
//          x(3, tpe, nestedTpe)
          (shift + 1, vs :+ resolve(Seq(nestedTpe), typeIndex))

        case tpe =>
//          x(4, tpe)
          (shift, vs :+ cast(query, q"$row.asJava", tpe, q"indexMap(${entityIndex + shift + typeIndex})"))
      }
    }._2

    q"(..$values).asInstanceOf[(..$tpes)]"
  }


  // Composites ------------------------------------------------------------------

  def compositeTuple(query: Tree, row: Tree, tupleTypes: Seq[Type]): Seq[Tree] = {

    def castValues(types: Seq[Type], tupleIndex: Int, tupleArity: Int): (Seq[Tree], Int) = {
      val values: Seq[Tree] = types.zipWithIndex.map { case (valueType, i) =>
        cast(query, row, valueType, q"${tupleIndex + i}")
      }
      (values, tupleArity)
    }

    tupleTypes.foldLeft(Seq.empty[Tree], 0) { case ((acc, tupleIndex), tupleType0) =>

      val (values, arity) = tupleType0 match {

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 22)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 21)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 20)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 19)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 18)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 17)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 16)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 15)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 14)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 13)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 12)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 11)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 10)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 9)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 8)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 7)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 6)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _, _)].typeSymbol).typeArgs, tupleIndex, 5)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _, _)].typeSymbol).typeArgs, tupleIndex, 4)

        case tupleType if tupleType <:< weakTypeOf[(_, _, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _, _)].typeSymbol).typeArgs, tupleIndex, 3)

        case tupleType if tupleType <:< weakTypeOf[(_, _)] =>
          castValues(tupleType.baseType(weakTypeOf[(_, _)].typeSymbol).typeArgs, tupleIndex, 2)

        case valueType => (Seq(q"${cast(query, row, valueType, q"$tupleIndex")}"), 1)
      }

      (acc :+ q"(..$values).asInstanceOf[$tupleType0]", tupleIndex + arity)
    }._1
  }
}
