package molecule.factory
import java.net.URI
import java.util.{Date, UUID}

import molecule.ast.model._
import molecule.boilerplate.NS
import molecule.ops.TreeOps
import molecule.transform._

import scala.language.experimental.macros
import scala.language.higherKinds
import scala.reflect.macros.whitebox.Context

trait MakeBase[Ctx <: Context] extends TreeOps[Ctx] {
  import c.universe._
  val x = DebugMacro("BuildMolecule", 1, 20, false)


  def mapIdents(idents: Seq[Any]): Seq[(String, Tree)] = idents.flatMap {
    case (key: String, value: String) if key.startsWith("__ident__") && value.startsWith("__ident__") => Seq(key -> q"convert(${TermName(key.substring(9))})", value -> q"convert(${TermName(value.substring(9))})")
    case (key: String, value: Any) if key.startsWith("__ident__")                                     => Seq(key -> q"convert(${TermName(key.substring(9))})")
    case (key: Any, value: String) if value.startsWith("__ident__")                                   => Seq(value -> q"convert(${TermName(value.substring(9))})")
    case ident: String if ident.startsWith("__ident__")                                               => Seq(ident -> q"convert(${TermName(ident.substring(9))})")
    case other                                                                                        => Nil
  }

  def mapGenerics(gs: Seq[Generic]): Seq[Any] = gs.flatMap {
    case TxTValue(Some(ident))        => Some(ident)
    case TxTValue_(Some(ident))       => Some(ident)
    case TxInstantValue(Some(ident))  => Some(ident)
    case TxInstantValue_(Some(ident)) => Some(ident)
    case OpValue(Some(ident))         => Some(ident)
    case OpValue_(Some(ident))        => Some(ident)
    case _                            => None
  }

  def mapIdentifiers(elements: Seq[Element], identifiers0: Seq[(String, Tree)] = Seq()): Seq[(String, Tree)] = {
    val newIdentifiers = (elements collect {
      case Atom(_, _, _, _, Eq(idents), _, gs, keyIdents)         => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, Neq(idents), _, gs, keyIdents)        => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, And(idents), _, gs, keyIdents)        => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, Lt(ident), _, gs, keyIdents)          => mapIdents(ident +: (mapGenerics(gs) ++ keyIdents))
      case Atom(_, _, _, _, Gt(ident), _, gs, keyIdents)          => mapIdents(ident +: (mapGenerics(gs) ++ keyIdents))
      case Atom(_, _, _, _, Le(ident), _, gs, keyIdents)          => mapIdents(ident +: (mapGenerics(gs) ++ keyIdents))
      case Atom(_, _, _, _, Ge(ident), _, gs, keyIdents)          => mapIdents(ident +: (mapGenerics(gs) ++ keyIdents))
      case Atom(_, _, _, _, Add_(idents), _, gs, keyIdents)       => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, Remove(idents), _, gs, keyIdents)     => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, Replace(idents), _, gs, keyIdents)    => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, MapEq(idents), _, gs, keyIdents)      => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, MapAdd(idents), _, gs, keyIdents)     => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, MapReplace(idents), _, gs, keyIdents) => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, MapRemove(idents), _, gs, keyIdents)  => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, MapKeys(idents), _, gs, keyIdents)    => mapIdents(idents ++ mapGenerics(gs) ++ keyIdents)
      case Atom(_, _, _, _, _, _, gs, keyIdents)                  => mapIdents(mapGenerics(gs) ++ keyIdents)
      case Meta(_, _, _, _, Eq(idents))                           => mapIdents(idents)
      case Meta(_, _, _, Id(eid), _)                              => mapIdents(Seq(eid))
      case Nested(_, nestedElements)                              => mapIdentifiers(nestedElements, identifiers0)
      case Composite(compositeElements)                           => mapIdentifiers(compositeElements, identifiers0)
      case TxMetaData(txElements)                                 => mapIdentifiers(txElements, identifiers0)
      case TxMetaData_(txElements)                                => mapIdentifiers(txElements, identifiers0)
    }).flatten
    (identifiers0 ++ newIdentifiers).distinct
  }

  def makeModelE(model: Model): Model = {
    def recurse(e: Element): Element = e match {
      case g: Nested => Nested(g.bond, Meta("", "", "e", NoValue, IndexVal) +: (g.elements map recurse))
      case other     => other
    }
    val firstMeta = model.elements.head match {
      case Meta(_, _, "e", NoValue, Eq(List(eid))) => Meta("", "", "e", Id(eid), IndexVal)
      case Bond(ns, refAttr, refNs, _, _)          => Meta("", "", "r", NoValue, IndexVal)
      case _                                       => Meta("", "", "e", NoValue, IndexVal)
    }
    Model(firstMeta +: (model.elements map recurse))
  }

  val imports =
    q"""
        import molecule.api._
        import molecule.ast.model._
        import molecule.ast.query._
        import molecule.ops.QueryOps._
        import molecule.transform.{Model2Query, Model2Transaction, Query2String}
        import java.lang.{Long => jLong, Double => jDouble, Boolean => jBoolean}
        import java.util.{Date, UUID, Map => jMap, List => jList}
        import java.net.URI
        import java.math.{BigInteger => jBigInt, BigDecimal => jBigDec}
        import clojure.lang.{PersistentHashSet, PersistentVector, LazySeq, Keyword}
        import scala.collection.JavaConverters._
     """

  def valueResolver(identMap: Map[String, Tree]) =
    q"""
      private def convert(v: Any): Any = v match {
        case seq: Seq[_]   => seq map convert
        case m: Map[_, _]  => m.toSeq map convert
        case (k, v)        => (convert(k), convert(v))
        case Some(v)       => convert(v)
        case f: Float      => f.toDouble
        case unchanged     => unchanged
      }

      private def flatSeq(a: Any): Seq[Any] = (a match {
        case seq: Seq[_] => seq
        case set: Set[_] => set.toSeq
        case v           => Seq(v)
      }) map convert

      private def getValues(idents: Seq[Any]) = idents.flatMap {
        case v: String               if v.startsWith("__ident__")                              => flatSeq($identMap.get(v).get)
        case (k: String, "__pair__") if k.startsWith("__ident__")                              => flatSeq($identMap.get(k).get)
        case (k: String, v: String)  if k.startsWith("__ident__") && v.startsWith("__ident__") => Seq(($identMap.get(k).get, $identMap.get(v).get))
        case (k: String, v: Any)     if k.startsWith("__ident__")                              => Seq(($identMap.get(k).get, convert(v)))
        case (k: Any, v: String)     if v.startsWith("__ident__")                              => Seq((convert(k), $identMap.get(v).get))
        case (k, v)                                                                            => Seq((convert(k), convert(v)))
        case seq: Seq[_]                                                                       => seq map convert
        case v                                                                                 => Seq(convert(v))
      }
     """

  def modelResolver(model: Model, modelE: Model, valueResolver: Tree) =
    q"""
      private object r {
        ..$imports
        ..$valueResolver

        private def getKeys(keyIdents: Seq[String]): Seq[String] = getValues(keyIdents).flatMap {
          case keys: Seq[_] => keys
          case key          => Seq(key)
        }.asInstanceOf[Seq[String]]

        private def getGenerics(gs: Seq[Generic]): Seq[Generic] = gs map {
          case TxTValue(Some(ident))        => TxTValue(Some(getValues(Seq(ident)).head))
          case TxTValue_(Some(ident))       => TxTValue_(Some(getValues(Seq(ident)).head))
          case TxInstantValue(Some(ident))  => TxInstantValue(Some(getValues(Seq(ident)).head))
          case TxInstantValue_(Some(ident)) => TxInstantValue_(Some(getValues(Seq(ident)).head))
          case OpValue(Some(ident))         => OpValue(Some(getValues(Seq(ident)).head))
          case OpValue_(Some(ident))        => OpValue_(Some(getValues(Seq(ident)).head))
          case otherGeneric                 => otherGeneric
        }

        private def resolveIdentifiers(elements: Seq[Element]): Seq[Element] = elements map {
          case atom@Atom(_, _, _, _, MapEq(idents), _, gs2, keyIdents)      => idents match {
            case List((ident, "__pair__"))
              if ident.startsWith("__ident__") && getValues(Seq(ident)) == Seq(None) => atom.copy(value = Fn("not", None),                                           gs = getGenerics(gs2), keys = getKeys(keyIdents))
            case idents                                                              => atom.copy(value = MapEq(getValues(idents).asInstanceOf[Seq[(String, Any)]]), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          }
          case atom@Atom(_, _, _, 2, Eq(idents), _, gs2, keyIdents)         => getValues(idents) match {
            case Seq(None) => atom.copy(value = Fn("not", None), gs = getGenerics(gs2))
            case values    => atom.copy(value = Eq(values)     , gs = getGenerics(gs2))
          }
          case atom@Atom(_, _, _, _, Eq(idents), _, gs2, keyIdents)         => getValues(idents) match {
            case Seq(None) => atom.copy(value = Fn("not", None), gs = getGenerics(gs2), keys = getKeys(keyIdents))
            case values    => atom.copy(value = Eq(values)     , gs = getGenerics(gs2), keys = getKeys(keyIdents))
          }
          case atom@Atom(_, _, _, _, Neq(idents), _, gs2, keyIdents)        => atom.copy(value = Neq(getValues(idents)),         gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, And(idents), _, gs2, keyIdents)        => atom.copy(value = And(getValues(idents)),         gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, Lt(ident), _, gs2, keyIdents)          => atom.copy(value = Lt(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, Gt(ident), _, gs2, keyIdents)          => atom.copy(value = Gt(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, Le(ident), _, gs2, keyIdents)          => atom.copy(value = Le(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, Ge(ident), _, gs2, keyIdents)          => atom.copy(value = Ge(getValues(Seq(ident)).head), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, Add_(idents), _, gs2, _)               => atom.copy(value = Add_(getValues(idents)),        gs = getGenerics(gs2))
          case atom@Atom(_, _, _, _, Remove(idents), _, gs2, _)             => atom.copy(value = Remove(getValues(idents)),      gs = getGenerics(gs2))
          case atom@Atom(_, _, _, _, Replace(oldNew), _, gs2, _)            => atom.copy(value = Replace(getValues(oldNew).asInstanceOf[Seq[(Any, Any)]]),       gs = getGenerics(gs2))
          case atom@Atom(_, _, _, _, MapAdd(idents), _, gs2, keyIdents)     => atom.copy(value = MapAdd(getValues(idents).asInstanceOf[Seq[(String, Any)]]),     gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, MapReplace(idents), _, gs2, keyIdents) => atom.copy(value = MapReplace(getValues(idents).asInstanceOf[Seq[(String, Any)]]), gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, MapRemove(idents), _, gs2, keyIdents)  => atom.copy(value = MapRemove(getValues(idents).map(_.toString)),                   gs = getGenerics(gs2), keys = getKeys(keyIdents))
          case atom@Atom(_, _, _, _, MapKeys(idents), _, gs2, _)            => atom.copy(value = MapKeys(getValues(idents).asInstanceOf[Seq[String]]),           gs = getGenerics(gs2))
          case atom@Atom(_, _, _, _, _, _, gs2, _)                          => atom.copy(gs = getGenerics(gs2))
          case meta@Meta(_, _, _, _, Eq(idents))                            => meta.copy(value = Eq(getValues(idents)))
          case meta@Meta(_, _, _, Id(eid), _)                               => meta.copy(generic = Id(getValues(Seq(eid)).head))
          case Nested(ns, nestedElements)                                   => Nested(ns, resolveIdentifiers(nestedElements))
          case Composite(compositeElements)                                 => Composite(resolveIdentifiers(compositeElements))
          case TxMetaData(txElements)                                       => TxMetaData(resolveIdentifiers(txElements))
          case TxMetaData_(txElements)                                      => TxMetaData_(resolveIdentifiers(txElements))
          case other                                                        => other
        }
        val model: Model = Model(resolveIdentifiers($model.elements))
        val query: Query = Model2Query(model)

        val modelE: Model = Model(resolveIdentifiers($modelE.elements))
        val queryE: Query = Model2Query(modelE)
      }
    """

  def basics(dsl: c.Expr[NS]) = {
    val model = Dsl2Model(c)(dsl)
    val modelE = makeModelE(model)
    val identMap = mapIdentifiers(model.elements).toMap
    val resolverTree = modelResolver(model, modelE, valueResolver(identMap))

    q"""
      ..$resolverTree

      private trait Util { self: molecule.api.MoleculeBase =>

        import molecule.Conn
        import molecule.ops.QueryOps._
        import molecule.transform.Query2String
        import java.text.SimpleDateFormat
        import java.util.Date

        private val m = _model
        private val q = _query

        def date(s: String): Date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(s)

        // Print transitions of a `get` call to console
        def getD(implicit conn: Conn) {
          val rows = try {
            conn.query(m, q).take(500)
          } catch {
            case e: Throwable => sys.error(e.toString)
          }
          val ins = q.inputs
          println(
            "\n--------------------------------------------------------------------------\n" +
            ${show(dsl.tree)} + "\n\n" +
            m + "\n\n" +
            q + "\n\n" +
            q.datalog + "\n\n" +
            "RULES: " + (if (q.i.rules.isEmpty) "none\n\n" else q.i.rules.map(Query2String(q).p(_)).mkString("[\n ", "\n ", "\n]\n\n")) +
            "INPUTS: " + (if (ins.isEmpty) "none\n\n" else ins.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n", "\n", "\n\n")) +
            "OUTPUTS:\n" + rows.zipWithIndex.map(r => (r._2 + 1) + "  " + r._1).mkString("\n") + "\n(showing up to 500 rows...)" +
            "\n--------------------------------------------------------------------------\n"
          )
        }
      }
    """
  }


  def castOptionMap(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Map[String, String]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, String]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1)}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Int]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Int]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toInt}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Long]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Long]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toLong}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Float]]]      =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Float]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toFloat}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Double]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Double]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toDouble}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Boolean]]]    =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Boolean]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> p(1).toBoolean}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, BigInt]]]     =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, BigInt]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> BigInt(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigInt(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, BigDecimal]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, BigDecimal]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> BigDecimal(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> BigDecimal(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, Date]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, Date]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> date(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, UUID]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, UUID]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> UUID.fromString(p(1))}.toMap)
        }
       """
    case t if t <:< typeOf[Option[Map[String, URI]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Map[String, URI]]
          case vs: PersistentHashSet => Some(vs.asScala.map{ case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap)
          case vs                    => Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq.map{case s:String => val p = s.split("@", 2); p(0) -> new URI(p(1))}.toMap)
        }
       """
  }


  def castOptionSet(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[Set[String]]]  =>
      q"""
        $value match {
          case null                                    => Option.empty[Set[String]]
          case vs: PersistentHashSet                   => Some(vs.asScala.map(_.toString).toSet.asInstanceOf[Set[String]])
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
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[Set[Int]])
          case vs                    =>
            val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            Some(values.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[Set[Int]])
        }
       """
    case t if t <:< typeOf[Option[Set[Float]]]   =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Float]]
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[Set[Float]])
          case vs                    =>
            val values = vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSeq
            Some(values.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[Set[Float]])
        }
       """
    case t if t <:< typeOf[Option[Set[Long]]]    =>
      q"""
        $value match {
          case null                                 => Option.empty[Set[Long]]
          case vs: PersistentHashSet                => Some(vs.asScala.map(_.asInstanceOf[jLong].toLong).toSet.asInstanceOf[Set[Long]])
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
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[jDouble].toDouble).toSet.asInstanceOf[Set[Double]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Double]])
        }
       """
    case t if t <:< typeOf[Option[Set[Boolean]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Boolean]]
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[Boolean]).toSet.asInstanceOf[Set[Boolean]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Boolean]])
        }
       """
    case t if t <:< typeOf[Option[Set[BigInt]]]  =>
      q"""
        $value match {
          case null                  => Option.empty[Set[BigInt]]
          case vs: PersistentHashSet => Some(vs.asScala.map(v => BigInt(v.asInstanceOf[jBigInt].toString)).toSet.asInstanceOf[Set[BigInt]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala
            .map(v => BigInt(v.asInstanceOf[jBigInt].toString)).toSet.asInstanceOf[Set[BigInt]])
        }
       """

    case t if t <:< typeOf[Option[Set[BigDecimal]]] =>
      q"""
        $value match {
          case null                  => Option.empty[Set[BigDecimal]]
          case vs: PersistentHashSet => Some(vs.asScala.map(v => BigDecimal(v.asInstanceOf[jBigDec].toString)).toSet.asInstanceOf[Set[BigDecimal]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala
            .map(v => BigDecimal(v.asInstanceOf[jBigDec].toString)).toSet.asInstanceOf[Set[BigDecimal]])
        }
       """
    case t if t <:< typeOf[Option[Set[Date]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Set[Date]]
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[Date]).toSet.asInstanceOf[Set[Date]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[Date]])
        }
       """
    case t if t <:< typeOf[Option[Set[UUID]]]       =>
      q"""
        $value match {
          case null                  => Option.empty[Set[UUID]]
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[UUID]).toSet.asInstanceOf[Set[UUID]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[UUID]])
        }
       """
    case t if t <:< typeOf[Option[Set[URI]]]        =>
      q"""
        $value match {
          case null                  => Option.empty[Set[URI]]
          case vs: PersistentHashSet => Some(vs.asScala.map(_.asInstanceOf[URI]).toSet.asInstanceOf[Set[URI]])
          case vs                    =>
            Some(vs.asInstanceOf[jMap[String, PersistentVector]].asScala.toMap.values.head.asInstanceOf[PersistentVector].asScala.toSet.asInstanceOf[Set[URI]])
        }
       """
  }


  def castOption(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Option[String]]     =>
      q"""
        $value match {
          case null                                  => Option.empty[String]
          case v: String                             => Some(v)
          case v if v.toString.contains(":db/ident") => val v2 = v.toString; Some(v2.substring(v2.lastIndexOf("/")+1).init.init)
          case v                                     => Some(v.asInstanceOf[jMap[String, String]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[Int]]        =>
      q"""
        $value match {
          case null     => Option.empty[Int]
          case v: jLong => Some(v.asInstanceOf[jLong].toInt)
          case v        => Some(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toInt) // pull result map: {:ns/int 42}
        }
       """
    case t if t <:< typeOf[Option[Float]]      =>
      q"""
        $value match {
          case null       => Option.empty[Float]
          case v: jDouble => Some(v.asInstanceOf[jDouble].toFloat)
          case v          => Some(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toFloat)
        }
       """
    case t if t <:< typeOf[Option[Long]]       =>
      q"""
        $value match {
          case null                               => Option.empty[Long]
          case v: jLong                           => Some(v.asInstanceOf[jLong].toLong)
          case v if v.toString.contains(":db/id") => val v2 = v.toString; Some(v2.substring(v2.lastIndexOf(" ")+1).init.init.toLong)
          case v                                  => Some(v.asInstanceOf[jMap[String, jLong]].asScala.toMap.values.head.toLong)
        }
       """
    case t if t <:< typeOf[Option[Double]]     =>
      q"""
        $value match {
          case null       => Option.empty[Double]
          case v: jDouble => Some(v.asInstanceOf[jDouble].toDouble)
          case v          => Some(v.asInstanceOf[jMap[String, jDouble]].asScala.toMap.values.head.toDouble)
        }
       """
    case t if t <:< typeOf[Option[Boolean]]    =>
      q"""
        $value match {
          case null        => Option.empty[Boolean]
          case v: jBoolean => Some(v.asInstanceOf[Boolean])
          case v           => Some(v.asInstanceOf[jMap[String, Boolean]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[BigInt]]     =>
      q"""
        $value match {
          case null       => Option.empty[BigInt]
          case v: jBigInt => Some(BigInt(v.asInstanceOf[jBigInt].toString))
          case v          => Some(BigInt(v.asInstanceOf[jMap[String, jBigInt]].asScala.toMap.values.head.toString))
        }
       """
    case t if t <:< typeOf[Option[BigDecimal]] =>
      q"""
        $value match {
          case null       => Option.empty[BigDecimal]
          case v: jBigDec => Some(BigDecimal(v.asInstanceOf[jBigDec].toString))
          case v          => Some(BigDecimal(v.asInstanceOf[jMap[String, jBigDec]].asScala.toMap.values.head.toString))
        }
       """
    case t if t <:< typeOf[Option[Date]]       =>
      q"""
        $value match {
          case null    => Option.empty[Date]
          case v: Date => Some(v)
          case v       => Some(v.asInstanceOf[jMap[String, Date]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[UUID]]       =>
      q"""
        $value match {
          case null    => Option.empty[UUID]
          case v: UUID => Some(v)
          case v       => Some(v.asInstanceOf[jMap[String, UUID]].asScala.toMap.values.head)
        }
       """
    case t if t <:< typeOf[Option[URI]]        =>
      q"""
        $value match {
          case null   => Option.empty[URI]
          case v: URI => Some(v)
          case v      => Some(v.asInstanceOf[jMap[String, URI]].asScala.toMap.values.head)
        }
       """
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


  def castSet(value: Tree, tpe: Type) = tpe match {
    case t if t <:< typeOf[Set[Int]]        => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map(_.asInstanceOf[jLong].toInt).toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[Float]]      => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map(_.asInstanceOf[jDouble].toFloat).toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[BigInt]]     => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{ case i: jBigInt => BigInt(i)}.toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[BigDecimal]] => q"$value.asInstanceOf[PersistentHashSet].asScala.toSeq.map{ case d: jBigDec => BigDecimal(d.toString)}.toSet.asInstanceOf[$t]"
    case t if t <:< typeOf[Set[_]]          => q"$value.asInstanceOf[PersistentHashSet].asScala.toSet.asInstanceOf[$t]"
  }


  def castType(query: Tree, value: Tree, tpe: Type, i: Int) = tpe match {
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
        $value match {
          case l: jLong  => l.toInt.asInstanceOf[$t]
          case s: String => s.toInt.asInstanceOf[$t]
          case other     => other.asInstanceOf[$t]
        }
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
        $query.f.outputs($i) match {
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
      """
  }

  def cast(query: Tree, row: Tree, tpe: Type, i: Int): Tree = {
    val value: Tree = q"if($i >= $row.size) null else $row.get($i)"
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

  def castTpls(query: Tree, rows: Tree, tpes: Seq[Type]) = q"$rows.map(row => (..${castTpl(query, q"row", tpes)})).asInstanceOf[Seq[(..$tpes)]]"

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

        case tpe if tpe <:< weakTypeOf[Seq[_]] =>
          val nestedTpe = tpe.baseType(weakTypeOf[Seq[_]].typeSymbol).typeArgs.head
          (shift + 1, vs :+ resolve(Seq(nestedTpe), tupleIndex))

        case tpe =>
          (shift, vs :+ cast(query, q"$row.asJava", tpe, entityIndex + shift + tupleIndex))
      }
    }._2

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
              case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
              case m: Meta                                               => Seq(m)
              case other                                                 => Seq()
            }
            val elements = _modelE.elements flatMap recurse
            if (elements.size != _queryE.f.outputs.size)
              sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + _queryE.f.outputs.size + "):\n" +
                _modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + _queryE + "\n----------------\n")
            elements
          }

          val entityIndexes = flatModel.zipWithIndex.collect {
            case  (Meta(_, _, _, _, IndexVal), i) => i
          }
//println("===============================================")
//println(_model)
//println(_modelE)
//println(_queryE)
//println(_queryE.datalog)
//println("---- ")
//flatModel foreach println
//println("---- " + entityIndexes)

          val rowSeq = $rows.toSeq
          val sortedRows = entityIndexes match {
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

//sortedRows foreach println

          val rowCount = sortedRows.length

          val casted = sortedRows.foldLeft((Seq[(..$tpes)](), None: Option[(..$tpes)], Seq(0L), Seq[Any](0L), 1)) { case ((accTpls0, tpl0, prevEntities, prevRow, r), row0) =>

            val row = row0.asScala.asInstanceOf[Seq[Any]]

//println("--- " + r + " ---------------------------------------------------")
            val entities = entityIndexes.map(i => row.apply(i).asInstanceOf[Long])

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

//println("accTpls      : " + accTpls)

            (accTpls, Some(tpl), entities, row, r + 1)
          }._1 //.asInstanceOf[Seq[(..tpes)]]

//println("Casted:\n" + casted.mkString("\n"))
          casted
        }
      """
  }
}