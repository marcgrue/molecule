package molecule.factory
import java.net.URI
import java.util.{Date, UUID}

import molecule.ast.model._
import molecule.dsl.schemaDSL._
import molecule.ops.TreeOps
import molecule.transform._

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

trait FactoryBase[Ctx <: Context] extends TreeOps[Ctx] {
  import c.universe._
  val x = DebugMacro("BuildMolecule", 1, 20, false)

  def basics(dsl: c.Expr[NS]) = {

    val model: Model = Dsl2Model(c)(dsl)

    val modelE: Model = {
      def recurse(e: Element): Element = e match {
        case g: Nested => Nested(g.bond, Meta("", "", "e", NoValue, IndexVal) +: (g.elements map recurse))
        case other     => other
      }
      Model(Meta("", "", "e", NoValue, IndexVal) +: (model.elements map recurse))
    }

    val p = dsl.tree.pos
    val dslTailCode = p.source.lineToString(p.line - 1).substring(p.column)
    val checkCorrectModel = dslTailCode match {
      // todo: lift into quasiquotes and check against resolved model
      case r".*[\.|\s]*add.*"    => "check add..."
      case r".*[\.|\s]*insert.*" => "check insert..."
      case r".*[\.|\s]*update.*" => "check update..."
      case _                     => "other..."
    }
    //            x(1, dsl.tree, showRaw(dsl.tree), model, checkCorrectModel)
    //                x(1, dsl.tree, model)

    def mapIdents(idents: Seq[Any]): Seq[(String, Tree)] = idents.flatMap {
      case (key: String, value: String) if key.startsWith("__ident__") && value.startsWith("__ident__") => Seq(key -> q"${TermName(key.substring(9))}", value -> q"${TermName(value.substring(9))}")
      case (key: String, value: Any) if key.startsWith("__ident__")                                     => Seq(key -> q"${TermName(key.substring(9))}")
      case (key: String, value: String) if value.startsWith("__ident__")                                => Seq(value -> q"${TermName(value.substring(9))}")
      case ident: String if ident.startsWith("__ident__")                                               => Seq(ident -> q"${TermName(ident.substring(9))}")
      case _                                                                                            => Nil
    }

    def mapIdentifiers(elements: Seq[Element], identifiers0: Seq[(String, Tree)] = Seq()): Seq[(String, Tree)] = {
      val newIdentifiers = (elements collect {
        case atom@Atom(_, _, _, _, Eq(idents), _, _, keyIdents)     => mapIdents(idents ++ keyIdents)
        case atom@Atom(_, _, _, _, Neq(idents), _, _, keyIdents)    => mapIdents(idents ++ keyIdents)
        case atom@Atom(_, _, _, _, And(idents), _, _, keyIdents)    => mapIdents(idents ++ keyIdents)
        case atom@Atom(_, _, _, _, Lt(ident), _, _, keyIdents)      => mapIdents(ident +: keyIdents)
        case atom@Atom(_, _, _, _, Gt(ident), _, _, keyIdents)      => mapIdents(ident +: keyIdents)
        case atom@Atom(_, _, _, _, Le(ident), _, _, keyIdents)      => mapIdents(ident +: keyIdents)
        case atom@Atom(_, _, _, _, Ge(ident), _, _, keyIdents)      => mapIdents(ident +: keyIdents)
        case atom@Atom(_, _, _, _, Remove(idents), _, _, keyIdents) => mapIdents(idents ++ keyIdents)
        case atom@Atom(_, _, _, _, Mapping(pairs), _, _, keyIdents) => mapIdents(pairs ++ keyIdents)
        case atom@Atom(_, _, _, _, Keys(idents), _, _, keyIdents)   => mapIdents(idents ++ keyIdents)
        case meta@Meta(_, _, _, _, Eq(idents))                      => mapIdents(idents)
        case Nested(_, nestedElements)                              => mapIdentifiers(nestedElements, identifiers0)
        case TxMetaData(txElements)                                 => mapIdentifiers(txElements, identifiers0)
        case TxMetaData_(txElements)                                => mapIdentifiers(txElements, identifiers0)
      }).flatten
      (identifiers0 ++ newIdentifiers).distinct
    }

    val identMap = mapIdentifiers(model.elements).toMap
    //        x(2, identMap)

    q"""
      import molecule._
      import molecule.api._
      import molecule.ast.model._
      import molecule.ast.query._
      import molecule.ops.QueryOps._
      import molecule.transform.{Model2Query, Model2Transaction, Query2String}
      import scala.collection.JavaConversions._
      import scala.collection.JavaConverters._
      import datomic.Connection
      import java.lang.{Long => jLong, Double => jDouble}
      import java.util.{Date, UUID, Map => jMap}
      import java.net.URI
      import java.text.SimpleDateFormat
      import clojure.lang.{PersistentHashSet, PersistentVector, LazySeq, Keyword}

      def flatSeq(a: Any): Seq[Any] = a match {
        case seq: Seq[_] => seq
        case set: Set[_] => set.toSeq
        case value       => Seq(value)
      }

      def getValues(idents: Seq[Any]) = idents.flatMap {
        case (key: String, value: String) if key.startsWith("__ident__") && value.startsWith("__ident__") => Seq(($identMap.get(key).get, $identMap.get(value).get))
        case (key: String, "__pair__")    if key.startsWith("__ident__")                                  => flatSeq($identMap.get(key).get)
        case (key: String, value: Any)    if key.startsWith("__ident__")                                  => Seq(($identMap.get(key).get, value))
        case (key: String, value: String) if value.startsWith("__ident__")                                => Seq((key, $identMap.get(value).get))
        case ident: String                if ident.startsWith("__ident__")                                => flatSeq($identMap.get(ident).get)
        case seq: Seq[_]                                                                                  => seq
        case value                                                                                        => Seq(value)
      }

      def getKeys(keyIdents: Seq[String]): Seq[String] = getValues(keyIdents).flatMap {
        case keys: Seq[_] => keys
        case key          => Seq(key)
      }.asInstanceOf[Seq[String]]

      def resolveIdentifiers(elements: Seq[Element]): Seq[Element] = elements map {
        case atom@Atom(_, _, _, 2, Eq(idents), _, _, keyIdents)      => atom.copy(value = Eq(getValues(idents)))
        case atom@Atom(_, _, _, _, Eq(idents), _, _, keyIdents)      => atom.copy(value = Eq(getValues(idents)), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Neq(idents), _, _, keyIdents)     => atom.copy(value = Neq(getValues(idents)), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, And(idents), _, _, keyIdents)     => atom.copy(value = And(getValues(idents)), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Lt(ident), _, _, keyIdents)       => atom.copy(value = Lt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Gt(ident), _, _, keyIdents)       => atom.copy(value = Gt(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Le(ident), _, _, keyIdents)       => atom.copy(value = Le(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Ge(ident), _, _, keyIdents)       => atom.copy(value = Ge(getValues(Seq(ident)).head), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Remove(idents), _, _, _)          => atom.copy(value = Remove(getValues(idents)))
        case atom@Atom(_, _, _, _, Mapping(idents), _, _, keyIdents) => atom.copy(value = Mapping(getValues(idents).asInstanceOf[Seq[(String, Any)]]), keys = getKeys(keyIdents))
        case atom@Atom(_, _, _, _, Keys(idents), _, _, _)            => atom.copy(value = Keys(getValues(idents).asInstanceOf[Seq[String]]))
        case meta@Meta(_, _, _, _, Eq(idents))                       => meta.copy(value = Eq(getValues(idents)))
        case Nested(ns, nestedElements)                              => Nested(ns, resolveIdentifiers(nestedElements))
        case TxMetaData(txElements)                                  => TxMetaData(resolveIdentifiers(txElements))
        case TxMetaData_(txElements)                                 => TxMetaData_(resolveIdentifiers(txElements))
        case other                                                   => other
      }

      val model: Model = Model(resolveIdentifiers($model.elements))
      val query: Query = Model2Query(model)

      lazy val modelE: Model = Model(resolveIdentifiers($modelE.elements))
      lazy val queryE: Query = Model2Query(modelE)

      def date(s: String): Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(s)

      def debugMolecule(conn: Connection, m: Model, q: Query): Unit = {
        val rows = try {
          results(conn, m, q).take(500)
        } catch {
          case e: Throwable => sys.error(e.toString)
        }
        val ins = inputs(q)
        println(
          "\n--------------------------------------------------------------------------\n" +
          ${show(dsl.tree)} + "\n\n" +
          m + "\n\n" +
          q + "\n\n" +
          q.datalog + "\n\n" +
          "RULES: " + (if (q.i.rules.isEmpty) "none\n\n" else q.i.rules.map(Query2String(q).p(_)).mkString("[\n ", "\n ", "\n]\n\n")) +
          "INPUTS: " + (if (ins.isEmpty) "none\n\n" else ins.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n", "\n", "\n\n")) +
          "OUTPUTS:\n" + rows.toList.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n") + "\n(showing up to 500 rows...)" +
          "\n--------------------------------------------------------------------------\n"
        )
      }
    """
  }

  def castOptMap(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]]  =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Int]]]     =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Long]]]    =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Float]]]   =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Double]]]  =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Boolean]]] =>
      q"""
      if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, Date]]]    =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, UUID]]]    =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap).asInstanceOf[$t]
      """
    case t if t <:< typeOf[Option[Map[String, URI]]]     =>
      q"""
        if($value == null)
          None
        else
          Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap).asInstanceOf[$t]
      """
  }

  def cast(query: Tree, row: Tree, tpe: Type, i: Int): Tree = {
    val xx = q"println($row);7"
    val value: Tree = q"$row.get($i)"
    tpe match {
      case t if t <:< typeOf[Option[Map[String, _]]] => castOptMap(value, t)
      case t if t <:< typeOf[Map[String, String]]    => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Int]]       => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Long]]      => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Float]]     => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Double]]    => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Boolean]]   => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, Date]]      => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, UUID]]      => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Map[String, URI]]       => q"""$value.asInstanceOf[PersistentHashSet].toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap.asInstanceOf[$t]"""
      case t if t <:< typeOf[Set[Int]]               => q"$value.asInstanceOf[PersistentHashSet].toSeq.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[$t]"
      case t if t <:< typeOf[Set[Float]]             => q"$value.asInstanceOf[PersistentHashSet].toSeq.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[$t]"
      case t if t <:< typeOf[Set[_]]                 => q"$value.asInstanceOf[PersistentHashSet].toSet.asInstanceOf[$t]"
      case t if t <:< typeOf[Vector[_]]              => q"$value.asInstanceOf[PersistentVector].toVector.asInstanceOf[$t]"
      case t if t <:< typeOf[Stream[_]]              => q"$value.asInstanceOf[LazySeq].toStream.asInstanceOf[$t]"
      case t if t <:< typeOf[Int]                    =>
        q"""
          if($value.isInstanceOf[jLong])
            $value.asInstanceOf[jLong].toInt.asInstanceOf[$t]
          else if($value.isInstanceOf[String])
            $value.asInstanceOf[String].toInt.asInstanceOf[$t]
          else
            $value.asInstanceOf[$t]
        """
      case t if t <:< typeOf[Float]                  =>
        q"""
          if($value.isInstanceOf[jDouble])
            $value.asInstanceOf[jDouble].toFloat.asInstanceOf[$t]
          else if($value.isInstanceOf[String])
            $value.asInstanceOf[String].toFloat.asInstanceOf[$t]
          else
            $value.asInstanceOf[$t]
        """
      case t if t <:< typeOf[Long]                   => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toLong else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[Double]                 => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toDouble else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[Boolean]                => q"(if($value.isInstanceOf[String]) $value.asInstanceOf[String].toBoolean else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[Date]                   => q"(if($value.isInstanceOf[String]) date($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[UUID]                   => q"(if($value.isInstanceOf[String]) UUID.fromString($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[URI]                    => q"(if($value.isInstanceOf[String]) new URI($value.asInstanceOf[String]) else $value).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Int]]            => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, jLong]].toMap.values.head.toInt)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Float]]          => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, jDouble]].toMap.values.head.toFloat)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Double]]         => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, jDouble]].toMap.values.head.toDouble)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Boolean]]        => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, Boolean]].toMap.values.head)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Date]]           => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, Date]].toMap.values.head)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[UUID]]           => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, UUID]].toMap.values.head)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[URI]]            => q"(if($value == null) None else Some($value.asInstanceOf[jMap[String, URI]].toMap.values.head)).asInstanceOf[$t]"
      case t if t <:< typeOf[Option[Long]]           =>
        q"""
          if($value == null) {
            None
          } else if ($value.toString.contains("{:db/id")) {
            // {:ns/ref1 {:db/id 3}}
            Some($value.toString.substring($value.toString.lastIndexOf(" ")+1).init.init.toLong).asInstanceOf[$t]
            // Or this?: Some(value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[jMap[String, Long]].toMap.values.head)
          } else {
            Some($value.asInstanceOf[jMap[String, jLong]].toMap.values.head.toLong).asInstanceOf[$t]
          }
        """
      case t if t <:< typeOf[Option[String]]         =>
        q"""
          if($value == null) {
            None
          } else if ($value.toString.contains(":db/ident")) {
            // {:ns/enum {:db/ident :ns.enum/enum1}}
            val value = $value.toString
            Some(value.substring(value.lastIndexOf("/")+1).init.init).asInstanceOf[$t]
          } else {
            Some($value.asInstanceOf[jMap[String, String]].toMap.values.head).asInstanceOf[$t]
          }
        """
      case t if t <:< typeOf[Option[Set[Double]]]    =>
        q"""if ($value == null) None else
              Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[Double]])"""
      case t if t <:< typeOf[Option[Set[Date]]]      =>
        q"""if ($value == null) None else
              Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[Date]])"""
      case t if t <:< typeOf[Option[Set[UUID]]]      =>
        q"""if ($value == null) None else
              Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[UUID]])"""
      case t if t <:< typeOf[Option[Set[URI]]]       =>
        q"""
           if ($value == null) None else
           Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[URI]])"""
      case t if t <:< typeOf[Option[Set[Int]]]       =>
        q"""
          if ($value == null) None else {
            val values = $value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq
            Some(values.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[Set[Int]])
          }
        """
      case t if t <:< typeOf[Option[Set[Float]]]     =>
        q"""
          if ($value == null) None else {
            val values = $value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq
            Some(values.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[Set[Float]])
          }
        """
      case t if t <:< typeOf[Option[Set[Long]]]      =>
        q"""
          if ($value == null) {
            None
          } else if ($value.toString.contains("{:db/id")) {
            // {:ns/ref1 [{:db/id 3} {:db/id 4}]}
            val idMaps = $value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq
            Some(idMaps.map(_.asInstanceOf[jMap[String, Long]].toMap.values.head).toSet.asInstanceOf[Set[Long]])
          } else {
            // {:ns/longs [3 4 5]}
            Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[Long]])
          }
        """
      case t if t <:< typeOf[Option[Set[String]]]    =>
        q"""
          if($value == null) {
            None
          } else if ($value.toString.contains(":db/ident")) {
            // {:ns/enums [{:db/ident :ns.enums/enum1} {:db/ident :ns.enums/enum2}]}
            val identMaps = $value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSeq
            val enums = identMaps.map(_.asInstanceOf[jMap[String, Keyword]].toMap.values.head.getName)
            Some(enums.toSet.asInstanceOf[Set[String]])
          } else {
            Some($value.asInstanceOf[jMap[String, PersistentVector]].toMap.values.head.asInstanceOf[PersistentVector].toSet.asInstanceOf[Set[String]])
          }
        """
      case t                                         =>
        q"""
          $query.f.outputs($i) match {
            case AggrExpr("sum",_,_) =>
              ${t.toString} match {
                case "Int"   => if($value.isInstanceOf[jLong]) $value.asInstanceOf[jLong].toInt.asInstanceOf[$t] else $value.asInstanceOf[$t]
                case "Float" => if($value.isInstanceOf[jDouble]) $value.asInstanceOf[jDouble].toFloat.asInstanceOf[$t] else $value.asInstanceOf[$t]
                case _       => $value.asInstanceOf[$t]
              }

            case AggrExpr("median",_,_) =>
              ${t.toString} match {
                case "Int"   => if($value.isInstanceOf[jLong]) $value.asInstanceOf[jLong].toInt.asInstanceOf[$t] else $value.asInstanceOf[$t]
                case "Float" => if($value.isInstanceOf[jDouble]) $value.asInstanceOf[jDouble].toFloat.asInstanceOf[$t] else $value.asInstanceOf[$t]
                case _       => $value.asInstanceOf[$t]
              }

            case other => {
//               sys.error("Default value: " + value.toString)
//               println("Default value type  : " + {t.toString})
//               println("Default value       : " + value.toString)
//               println("Default value casted: " + value.asInstanceOf[t].toString)
              $value.asInstanceOf[$t]
            }
          }
        """
    }
  }

  def castTpl(query: Tree, row: Tree, tpes: Seq[Type]): Seq[Tree] = tpes.zipWithIndex.map { case (tpe, i) => cast(query, row, tpe, i) }

  def castComposite(query: Tree, row: Tree, tupleTypes: Seq[Type]): Seq[Tree] = {

    def castValues(types: Seq[Type], tupleIndex: Int, tupleArity: Int): (Seq[Tree], Int) = {
      val values: Seq[Tree] = types.zipWithIndex.map { case (valueType, i) =>
        cast(query, row, valueType, tupleIndex + i)
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

        case valueType => (Seq(q"${cast(query, row, valueType, tupleIndex)}"), 1)
      }

      (acc :+ q"(..$values).asInstanceOf[$tupleType0]", tupleIndex + arity)
    }._1
  }

  def castTpls(query: Tree, rows: Tree, tpes: Seq[Type]) = q"$rows.map(row => (..${castTpl(query, q"row", tpes)})).toList"


  def resolveNested(query: Tree, tpes: Seq[Type], tpl0: Tree, prevRow: Tree, row: Tree, entityIndex: Int, depth: Int, maxDepth: Tree, shift: Int): Tree = {

    val prevEnt = q"if($prevRow.head == 0L) 0L else $prevRow.apply($entityIndex).asInstanceOf[Long]"
    val curEnt = q"$row.apply($entityIndex).asInstanceOf[Long]"
    val isNewNested = q"if ($prevEnt == 0L) true else $prevEnt != $curEnt"

    def resolve(nestedTpes: Seq[Type], tupleIndex: Int) = {
      val rowIndex = entityIndex + shift + tupleIndex
      q"""
        if ($tpl0.isEmpty || $isNewNested) {
          val nestedTpl = ${resolveNested(query, nestedTpes, q"None: Option[(..$tpes)]", prevRow, row, rowIndex, depth + 1, maxDepth, shift)}
          Seq(nestedTpl)

        // ==========================================================================
        } else if ($tpl0.get.isInstanceOf[Seq[_]]) {
          val nestedTpl = ${
        resolveNested(query, nestedTpes,
          q"Some($tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
          prevRow, row, rowIndex, depth + 1, maxDepth, shift)
      }.asInstanceOf[(..$nestedTpes)]

          val newNested = $prevRow.apply($rowIndex).asInstanceOf[Long] != $row.apply($rowIndex).asInstanceOf[Long]

         val nestedAcc = if (newNested)
            $tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]] :+ nestedTpl
          else
            $tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]].init :+ nestedTpl

          nestedAcc

        // ==========================================================================
        } else {

         val tpl0_1 = $tpl0.get.asInstanceOf[(..$tpes)]
         val tpl0_2 = tpl0_1.productElement($tupleIndex)
         val tpl0_3 = tpl0_2.asInstanceOf[Seq[(..$nestedTpes)]]
         val tpl0_4 = tpl0_3.last
         val tpl0_5: (..$nestedTpes) = tpl0_4.asInstanceOf[(..$nestedTpes)]

          val nestedTpl = ${
        resolveNested(query, nestedTpes,
          q"Some($tpl0.get.asInstanceOf[(..$tpes)].productElement($tupleIndex).asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
          prevRow, row, rowIndex, depth + 1, maxDepth, shift)
      }.asInstanceOf[(..$nestedTpes)]

           val newNested = $prevRow.apply($rowIndex).asInstanceOf[Long] != $row.apply($rowIndex).asInstanceOf[Long] || $depth == $maxDepth

           val nestedAcc = if (newNested)
             tpl0_3 :+ nestedTpl
           else
             tpl0_3.init :+ nestedTpl

          nestedAcc
        }
      """

      //      q"""
      //      //      println("  E tpl: " + tpl.isInstanceOf[Product])
      //      //println(tab + " depth       : " + depth)
      ////      val ei = "  " * entityIndex
      //      val tab = "  " * $rowIndex
      ////      println(tab + "rowIndex : " + rowIndex)
      //      println(tab + "rowIndex: " + $rowIndex + " (" + $entityIndex + "-" + $shift + "-" + $tupleIndex + ")")
      //
      //
      //        // ==========================================================================
      //        if ($tpl0.isEmpty || $isNewNested) {
      //
      //        println(tab + "a tpl0: " + $tpl0)
      //          val nestedTpl = ${resolveNested(query, nestedTpes, q"None: Option[(..$tpes)]", prevRow, row, rowIndex, depth + 1, maxDepth, shift)}
      //        println(tab + "a nestedTpl : Seq(" + nestedTpl + ")")
      //
      //          Seq(nestedTpl)
      //
      //        // ==========================================================================
      //        } else if ($tpl0.get.isInstanceOf[Seq[_]]) {
      //
      //        println(tab + "b tpl0      : " + $tpl0)
      //
      //          val nestedTpl = ${resolveNested(query, nestedTpes,
      //        q"Some($tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
      //        prevRow, row, rowIndex, depth + 1, maxDepth, shift)}.asInstanceOf[(..$nestedTpes)]
      //
      //          val newNested = $prevRow.apply($rowIndex).asInstanceOf[Long] != $row.apply($rowIndex).asInstanceOf[Long]
      //
      //        println(tab + "b nestedTpl : " + nestedTpl)
      //        println(tab + "b newNested : " + newNested + "  " + $row.apply($rowIndex).asInstanceOf[Long] + " " + $prevRow.apply($rowIndex).asInstanceOf[Long])
      //
      //         val nestedAcc = if (newNested)
      //            $tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]] :+ nestedTpl
      //          else
      //            $tpl0.get.asInstanceOf[Seq[(..$nestedTpes)]].init :+ nestedTpl
      //
      //        println(tab + "b nestedAcc : " + nestedAcc)
      //          nestedAcc
      //
      //        // ==========================================================================
      //        } else {
      //
      //        println(tab + "c tpl0      : " + $tpl0)
      //
      //        println(tab + "c tpl0_1 tpe: " + ${tpes.toString})
      //         val tpl0_1 = $tpl0.get.asInstanceOf[(..$tpes)]
      //        println(tab + "c tpl0_1    : " + tpl0_1)
      //
      //         val tpl0_2 = tpl0_1.productElement($tupleIndex)
      //        println(tab + "c tpl0_2    : " + tpl0_2)
      //
      //        println(tab + "c tpl0_3 tpe: " + ${nestedTpes.toString} + "")
      //         val tpl0_3 = tpl0_2.asInstanceOf[Seq[(..$nestedTpes)]]
      //        println(tab + "c tpl0_3    : " + tpl0_3)
      //
      //         val tpl0_4 = tpl0_3.last
      //        println(tab + "c tpl0_4    : " + tpl0_4)
      //
      ////        println(tab + "c tpl0_5 tpe: " + {nestedTpes.toString})
      //         val tpl0_5: (..$nestedTpes) = tpl0_4.asInstanceOf[(..$nestedTpes)]
      //        println(tab + "c tpl0_5    : " + tpl0_5)
      //
      ////          val newTpl0 = Some(tpl0_1.productElement(tpl0_1.productArity - 1).asInstanceOf[Seq[(..nestedTpes)]].last.asInstanceOf[(..nestedTpes)])
      ////          val newTpl0 = Some(tpl0_5)
      //
      ////        println(tab + "c newTpl0   : " + newTpl0)
      //
      //          val nestedTpl = ${resolveNested(query, nestedTpes,
      ////        q"Some(tpl0_5)",
      //        q"Some($tpl0.get.asInstanceOf[(..$tpes)].productElement($tupleIndex).asInstanceOf[Seq[(..$nestedTpes)]].last.asInstanceOf[(..$nestedTpes)])",
      //        prevRow, row, rowIndex, depth + 1, maxDepth, shift)}.asInstanceOf[(..$nestedTpes)]
      //
      //           val newNested = $prevRow.apply($rowIndex).asInstanceOf[Long] != $row.apply($rowIndex).asInstanceOf[Long] || $depth == $maxDepth
      //
      //        println(tab + "c nestedTpl : " + nestedTpl)
      //        println(tab + "c newNested : " + newNested + "   " + $prevRow.apply($rowIndex).asInstanceOf[Long] + "  " + $row.apply($rowIndex).asInstanceOf[Long] + "    " + $depth + "  " + $maxDepth)
      //
      //           val nestedAcc = if (newNested)
      //             tpl0_3 :+ nestedTpl
      //           else
      //             tpl0_3.init :+ nestedTpl
      //
      //        println(tab + "c nestedAcc : " + nestedAcc)
      //          nestedAcc
      //        }
      //      """

      //      q"null.asInstanceOf[Seq[(..$nestedTpes)]]"
    }

    lazy val values = tpes.zipWithIndex.foldLeft(shift, Seq.empty[Tree]) { case ((shift, vs), (t, tupleIndex)) =>
      t match {

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 22, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 21, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 20, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 19, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 18, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 17, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 16, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 15, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 14, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 13, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 12, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 11, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 10, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 9, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 8, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 7, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _, _)].typeSymbol).typeArgs
          (shift + 6, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _, _)].typeSymbol).typeArgs
          (shift + 5, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _, _)].typeSymbol).typeArgs
          (shift + 4, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _, _)].typeSymbol).typeArgs
          (shift + 3, vs :+ resolve(nestedTpes, tupleIndex))

        case tpe if tpe <:< weakTypeOf[Seq[(_, _)]] =>
          val nestedTpes = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head.baseType(weakTypeOf[(_, _)].typeSymbol).typeArgs
          (shift + 2, vs :+ resolve(nestedTpes, tupleIndex))
        //          val resultTree =
        //            q"""
        //            val rowIndex = $entityIndex + $shift + $tupleIndex
        //            val ei = "  " * rowIndex
        //            println(ei + "T2 " + rowIndex + " (" + $entityIndex + "-" + $shift + "-" + $tupleIndex + ")   " + ${tpe.toString})
        //
        //            val result = ${resolve(nestedTpes, tupleIndex)}
        //
        //            println(ei + "T2 " + result)
        //            result
        //          """
        //          (shift + 2, vs :+ resultTree)

        case tpe if tpe <:< weakTypeOf[Seq[_]] =>
          val nestedTpe = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head
          (shift + 1, vs :+ resolve(Seq(nestedTpe), tupleIndex))

        case tpe =>
          (shift, vs :+ cast(query, row, tpe, entityIndex + shift + tupleIndex))
        //          val resultTree =
        //            q"""
        //              val rowIndex = $entityIndex + $shift + $tupleIndex
        //              val ei = "  " * rowIndex
        //              println(ei + "B " + rowIndex + " (" + $entityIndex + "-" + $shift + "-" + $tupleIndex + ")   " + ${tpe.toString})
        //
        //              val result = ${cast(query, row, tpe, entityIndex + shift + tupleIndex)}
        //
        //              println(ei + "B " + result)
        //              result
        //            """
        //          (shift, vs :+ resultTree)
      }
    }._2

    //    q"""
    //      val ei = "  " * $entityIndex
    //
    ////      println(ei + "-------------------------------")
    //      println(ei + "A " + $entityIndex + "-" + $shift + "   " + ${tpes.toString})
    //
    //      val result = (..$values).asInstanceOf[(..$tpes)]
    //
    //      println(ei + "A " + result)
    //      result
    //    """
    q"(..$values).asInstanceOf[(..$tpes)]"
  }

  def castNestedTpls(query: Tree, rows: Tree, tpes: Seq[Type]) = {
    q"""
        if ($rows.isEmpty) {
          Seq[(..$tpes)]()
        } else {
          val flatModel = {
            def recurse(element: Element): Seq[Element] = element match {
              case n: Nested                                             => n.elements flatMap recurse
              case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
              case a: Atom                                               => Seq(a)
              case m: Meta                                               => Seq(m)
              case other                                                 => Seq()
            }
            val elements = modelE.elements flatMap recurse
            if (elements.size != queryE.f.outputs.size)
              sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + queryE.f.outputs.size + "):\n" +
                modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + queryE + "\n----------------\n")
            elements
          }

          val entityIndexes = flatModel.zipWithIndex.collect {
            case  (Meta(_, _, _, _, IndexVal), i) => i
          }
//println("===============================================")
//flatModel foreach println
//println("---- " + entityIndexes)

          val sortedRows = entityIndexes match {
            case List(a)                               => $rows.sortBy(row => row(a).asInstanceOf[Long])
            case List(a, b)                            => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long]))
            case List(a, b, c)                         => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long]))
            case List(a, b, c, d)                      => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long]))
            case List(a, b, c, d, e)                   => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long]))
            case List(a, b, c, d, e, f)                => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long]))
            case List(a, b, c, d, e, f, g)             => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long], row(g).asInstanceOf[Long]))
            case List(a, b, c, d, e, f, g, h)          => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long], row(g).asInstanceOf[Long], row(h).asInstanceOf[Long]))
            case List(a, b, c, d, e, f, g, h, i)       => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long], row(g).asInstanceOf[Long], row(h).asInstanceOf[Long], row(i).asInstanceOf[Long]))
            case List(a, b, c, d, e, f, g, h, i, j)    => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long], row(g).asInstanceOf[Long], row(h).asInstanceOf[Long], row(i).asInstanceOf[Long])).sortBy(row => row(j).asInstanceOf[Long])
            case List(a, b, c, d, e, f, g, h, i, j, k) => $rows.sortBy(row => (row(a).asInstanceOf[Long], row(b).asInstanceOf[Long], row(c).asInstanceOf[Long], row(d).asInstanceOf[Long], row(e).asInstanceOf[Long], row(f).asInstanceOf[Long], row(g).asInstanceOf[Long], row(h).asInstanceOf[Long], row(i).asInstanceOf[Long])).sortBy(row => (row(j).asInstanceOf[Long], row(k).asInstanceOf[Long]))
          }

//sortedRows foreach println

          val rowCount = sortedRows.length

          val casted = sortedRows.foldLeft((Seq[(..$tpes)](), None: Option[(..$tpes)], Seq(0L), Seq[Any](0L), 1)) { case ((accTpls0, tpl0, prevEntities, prevRow, r), row) =>
//println("--- " + r + " ---------------------------------------------------")
            val entities = entityIndexes.map(i => row(i).asInstanceOf[Long])

            val isLastRow = rowCount == r
            val newTpl = prevEntities.head != 0 && entities.head != prevEntities.head

//println("TPL0 " + tpl0)

            val tpl = ${resolveNested(query, tpes, q"tpl0", q"prevRow", q"row", 0, 1, q"entityIndexes.size - 1", 1)}

//println("TPL1 " + tpl)

            val accTpls = if (isLastRow && newTpl) {
              // Add current tuple
//println("TPLS last/new: " + (accTpls0 ++ Seq(tpl0.get, tpl)).toString)
              accTpls0 ++ Seq(tpl0.get, tpl)
            } else if (isLastRow) {
              // Add current tuple
//println("TPLS last    : " + (accTpls0 :+ tpl).toString)
              accTpls0 :+ tpl
            } else if (newTpl) {
              // Add finished previous tuple
//println("TPLS next    : " + (accTpls0 :+ tpl0.get).toString)
              accTpls0 :+ tpl0.get
            } else {
              // Continue building current tuple
//println("TPLS cont    : " + accTpls0.toString)
              accTpls0
            }

//println("TPL          : " + tpl)

            (accTpls, Some(tpl), entities, row, r + 1)
          }._1.toList


//println("Casted:\n" + casted.mkString("\n"))
          casted
        }
      """
  }
}
