package molecule.transform
import java.util.{Date, List => jList}

//import datomic.{Connection, Database, Peer}
import datomic._
//import molecule.DatomicFacade
import molecule.ast.model._
import molecule.ast.transaction._
import molecule.util.{Debug, Helpers}

import scala.collection.JavaConversions._


case class Model2Transaction(conn: Connection, model: Model) extends Helpers {
  val x = Debug("Model2Transaction", 25, 51, false, 6)

  private def tempId(attr: String): AnyRef = attr match {
    case "tx"                 => Peer.tempid(s":db.part/tx")
    case s if s.contains('_') => Peer.tempid(s":" + attr.substring(1).split("(?=_)").head) // extract "partition" from ":partition_Namespace/attr"
    case _                    => Peer.tempid(s":db.part/user")
  }

  private def attrValues(db: Database, id: Any, attr: String) = {
    val query = s"[:find ?values :in $$ ?id :where [?id $attr ?values]]"
    //    x(25, query, id.asInstanceOf[Object], Peer.q(query, db, id.asInstanceOf[Object]))
    Peer.q(query, db, id.asInstanceOf[Object]).map(_.get(0))
  }


  val stmtsModel: Seq[Statement] = {

    def resolveTx(elements: Seq[Element]) = elements.foldLeft('tx: Any, Seq[Statement]()) {
      case ((eSlot1, stmts1), a@Atom(ns, name, _, _, VarValue, _, _, _)) => throw new RuntimeException(
        s"[Model2Transaction:stmtsModel] Please apply transaction meta data directly to transaction attribute: `${ns.capitalize}.$name(<metadata>)`")
      case ((eSlot1, stmts1), element1)                                  => resolveElement(eSlot1, stmts1, element1)
    }._2

    def bi(gs: Seq[Generic]) = gs.collectFirst { case bi: Bidirectional => bi } getOrElse NoValue

    def resolveElement(eSlot: Any, stmts: Seq[Statement], element: Element): (Any, Seq[Statement]) = (eSlot, element) match {
      case ('_, Meta(ns, "", "e", _, EntValue))             => ('arg, stmts)
      case ('_, Meta(ns, "", "e", _, Eq(Seq(id: Long))))    => (Eid(id), stmts)
      case ('_, Atom(ns, name, _, c, VarValue, _, gs, _))   => ('e, stmts :+ Add('tempId, s":$ns/$name", 'arg, bi(gs)))
      case ('_, Atom(ns, name, _, c, value, prefix, gs, _)) => ('e, stmts :+ Add('tempId, s":$ns/$name", Values(value, prefix), bi(gs)))
      case ('_, Bond(ns, refAttr, refNs, c, gs))            => ('v, stmts :+ Add('tempId, s":$ns/$refAttr", s":$refNs", bi(gs)))

      case (e, Nested(Bond(ns, refAttr, _, _, gs), elements)) =>
        val nested = elements.foldLeft('v: Any, Seq[Statement]()) {
          case ((eSlot1, stmts1), element1) => resolveElement(eSlot1, stmts1, element1)
        }._2
        val parentId = if (e == '_) 'parentId else 'e
        ('e, stmts :+ Add(parentId, s":$ns/$refAttr", nested, bi(gs)))

      // First with id
      case (Eid(id), Atom(ns, name, _, c, value@Remove(_), prefix, gs, _)) => ('e, stmts :+ Retract(id, s":$ns/$name", Values(value, prefix), bi(gs)))
      case (Eid(id), Atom(ns, name, _, c, value, prefix, gs, _))           => ('e, stmts :+ Add(id, s":$ns/$name", Values(value, prefix), bi(gs)))
      case (Eid(id), Bond(ns, refAttr, refNs, c, gs))                      => ('v, stmts :+ Add(id, s":$ns/$refAttr", 'tempId, bi(gs)))

      // Same namespace
      case ('e, Atom(ns, name, _, _, value@Remove(_), prefix, gs, _)) => ('e, stmts :+ Retract('e, s":$ns/$name", Values(value, prefix), bi(gs)))
      case ('e, Atom(ns, name, _, c, VarValue, _, gs, _))             => ('e, stmts :+ Add('e, s":$ns/$name", 'arg, bi(gs)))
      case ('e, Atom(ns, name, _, c, value, prefix, gs, _))           => ('e, stmts :+ Add('e, s":$ns/$name", Values(value, prefix), bi(gs)))
      case ('e, Bond(ns, refAttr, refNs, c, gs))                      => ('v, stmts :+ Add('e, s":$ns/$refAttr", s":$refNs", bi(gs)))

      // Transaction annotations
      case ('_, TxMetaData(elements))  => ('e, stmts ++ resolveTx(elements))
      case ('e, TxMetaData(elements))  => ('e, stmts ++ resolveTx(elements))
      case ('_, TxMetaData_(elements)) => ('e, stmts ++ resolveTx(elements))
      case ('e, TxMetaData_(elements)) => ('e, stmts ++ resolveTx(elements))

      // Continue with only transaction Atoms...
      case ('tx, Atom(ns, name, _, _, VarValue, _, _, _))                                           => ('e, stmts :+ Add('e, s":$ns/$name", 'arg))
      case ('tx, Atom(ns, name, _, _, value, prefix, _, _)) if name.last == '_' || name.last == '$' => ('tx, stmts :+ Add('tx, s":$ns/${name.init}", Values(value, prefix)))
      case ('tx, Atom(ns, name, _, _, value, prefix, _, _))                                         => ('tx, stmts :+ Add('tx, s":$ns/$name", Values(value, prefix)))

      // Next namespace
      case ('v, Atom(ns, name, _, _, VarValue, _, gs, _))   => ('e, stmts :+ Add('v, s":$ns/$name", 'arg, bi(gs)))
      case ('v, Atom(ns, name, _, _, value, prefix, gs, _)) => ('e, stmts :+ Add('v, s":$ns/$name", Values(value, prefix), bi(gs)))
      case ('v, Bond(ns, refAttr, _, c, gs))                => ('v, stmts :+ Add('v, s":$ns/$refAttr", 'tempId, bi(gs)))

      // Add one extra generic statement to receive the eid arg for the following statement to use
      // (we then discard that temporary statement from the value statements)
      case ('arg, Atom(ns, name, _, _, VarValue, _, _, _)) => ('e, stmts :+ Add('remove_me, s":$ns/$name", 'arg) :+ Add('v, s":$ns/$name", 'arg))
      case ('arg, Bond(ns, refAttr, _, c, gs))             => ('v, stmts :+ Add('arg, s":$ns/$refAttr", 'tempId, bi(gs)))

      // BackRef
      case (_, ReBond(ns, _, _, _, _)) => ('e, stmts :+ Add('ns, s":$ns", ""))

      case (e, c: Composite) => sys.error(s"[Model2Transaction:stmtsModel] Composites are only for getting data:\nMODEL: $model \nPAIR: ($e, $c)\nSTMTS: $stmts")
      case (e, elem)         => sys.error(s"[Model2Transaction:stmtsModel] Unexpected transformation:\nMODEL: $model \nPAIR: ($e, $elem)\nSTMTS: $stmts")
    }

    def replace$(elements: Seq[Element]): Seq[Element] = elements map {
      case a@Atom(_, attr, _, _, _, Some(enumPrefix), _, _)
        if attr.last == '$' && enumPrefix.init.last == '$'       => a.copy(name = attr.init, enumPrefix = Some(enumPrefix.init.init + "/"))
      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '$' => a.copy(name = attr.init)
      case b@Bond(_, attr, _, _, _) if attr.last == '$'          => b.copy(refAttr = attr.init)
      case t@Transitive(_, attr, _, _, _) if attr.last == '$'    => t.copy(refAttr = attr.init)
      case Nested(ref, es)                                       => Nested(ref, replace$(es))
      case other                                                 => other
    }
    val model1 = Model(replace$(model.elements))
    //    x(50, model, model1)
    model1.elements.foldLeft('_: Any, Seq[Statement]()) {
      case ((eSlot, stmts), element) => resolveElement(eSlot, stmts, element)
    }._2
  }

  // Lookup if key is already populated
  def pairStr(e: Any, a: String, key: String) = {
    val query = s"[:find ?v :in $$ ?e ?a ?key :where [?e ?a ?v][(.startsWith ^String ?v ?key)]]"
    Peer.q(query, conn.db, e.asInstanceOf[Object], a.asInstanceOf[Object], key.asInstanceOf[Object]).map(_.get(0))
  }

  private def valueStmts(stmts: Seq[Statement], e: Any, a: String, arg: Any, prefix: Option[String] = None, bi: Generic = NoValue, otherEdgeId0: Option[AnyRef] = None): Seq[Statement] = {
    def p(arg: Any) = if (prefix.isDefined) prefix.get + arg else arg

    def biSelfRef(card: Int) = arg match {
      case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
        case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
      }
      case Remove(removeRefs) => removeRefs.flatMap {
        case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
      }
      case Eq(refs)           => refs.flatMap { case ref: Long =>
        val reverseRetracts = if (card == 1) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
      }
      case refs: Set[_]       => refs.flatMap { case ref: Long =>
        Seq(Add(ref, a, e), Add(e, a, ref))
      }
      case ref                => {
        val reverseRetracts = if (card == 1) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
      }
    }

    def biOtherRef(card: Int) = biSelfRef(card) // Todo

    def biEdgeRef(card: Int, targetAttr: String) = {
      val edgeB = otherEdgeId0 getOrElse sys.error("[Model2Transaction:valueStmts (biEdgeRef)] Missing id of other edge.")
      //      val List(card, targetAttr) = bi.split("@").toList.tail
      arg match {

        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }
        //        case Remove(removeRefs) => removeRefs.flatMap {
        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }

        //        case Eq(refs)           => refs.flatMap { case ref: Long =>
        ////          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        ////          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
        ////          reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
        //
        //          Seq(Add(otherEdgeId, a, e), Add(e, a, ref))
        //        }

        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
        //          Seq(Add(ref, a, e), Add(e, a, ref))
        //        }
        case edgeA => {
          val reverseRetracts = if (card == 1) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          reverseRetracts ++ Seq(
            // Interlink edge entities so that we later know which other one to update
            Add(edgeA, ":molecule_Meta/otherEdge", edgeB),
            Add(edgeB, ":molecule_Meta/otherEdge", edgeA),
            Add(edgeB, targetAttr, e),
            Add(e, a, edgeA)
          )
        }
      }
    }

    def biEdgeProp(card: Int) = {
      val edgeA = e
      val edgeB = otherEdgeId0 match {
        case Some(otherEdgeId) if otherEdgeId == edgeA => sys.error("[Model2Transaction:valueStmts (biEdgeProp)]  Other edge id is unexpectedly the same as this edge id.")
        case None                                      => sys.error("[Model2Transaction:valueStmts (biEdgeProp)]  Missing id of other edge.")
        case Some(otherEdgeId)                         => otherEdgeId
      }

      if (prefix.contains("mapping")) arg match {
        case Mapping(pairs) => pairs.flatMap {
          case (key, value) => {
            val existing = pairStr(edgeA, a, key)
            existing.size match {
              case 0 => Seq(
                Add(edgeB, a, key + "@" + value),
                Add(edgeA, a, key + "@" + value)
              )
              case 1 => Seq(
                Retract(edgeB, a, existing.head), Add(edgeB, a, key + "@" + value),
                Retract(edgeA, a, existing.head), Add(edgeA, a, key + "@" + value)
              )
              case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
            }
          }
        }
        //            case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
        //            case Remove(removeValues) => removeValues.flatMap { case key: String =>
        //              val existing = pairStr(e, a, key)
        //              existing.size match {
        //                case 0 => None
        //                case 1 => Some(Retract(e, a, existing.head))
        //                case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
        //              }
        //            }

      } else arg match {

        //        case Replace(oldNew)      => oldNew.toSeq.flatMap {
        //          case (oldValue, newValue) => Seq(Retract(e, a, p(oldValue)), Add(e, a, p(newValue)))
        //        }
        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }
        //        case Remove(removeRefs) => removeRefs.flatMap {
        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }
        case Eq(vs) => vs.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))

        //        case Eq(vs)           => vs.flatMap { v =>
        //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        //          reverseRetracts ++ Seq(Add(otherEdgeId, a, e), Add(e, a, ref))
        //        }
        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
        //          Seq(Add(ref, a, e), Add(e, a, ref))
        //        }
        //                case ref                => {
        //                  val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, edgeA, a).toSeq.map(revRef => Retract(revRef, a, edgeA)) else Nil
        //                  reverseRetracts ++ Seq(
        //                    Add(edgeB, a, ref),
        //                    Add(edgeA, a, ref)
        //                  )
        //                }

        case vs: Set[_]   => vs.toSeq.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))
        case m: Map[_, _] => m.flatMap {
          case (k, d: Date) => Seq(
            Add(edgeB, a, k + "@" + format2(d)),
            Add(edgeA, a, k + "@" + format2(d))
          )
          case (k, v)       => Seq(
            Add(edgeB, a, k + "@" + v),
            Add(edgeA, a, k + "@" + v)
          )
        }
        case v :: Nil     => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v)))
        case vs: List[_]  => vs.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))
        case v            => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v)))

      }

    }

    def biTargetRef(card: Int, biEdgeRefAttr: String) = {
      val edgeA = e
      val edgeB = otherEdgeId0 getOrElse sys.error("[Model2Transaction:valueStmts (biTargetRef)]  Missing id of other edge.")
      //      val List(card, "biEdgeRefAttr) = meta.split("@").toList.tail
      //      val biEdgeRefAttr = bi.split("@").toList.last
      arg match {

        // Each bidirectional manipulation takes care of the reverse ref

        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }
        //        case Remove(removeRefs) => removeRefs.flatMap {
        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        //        }
        //        case Eq(refs) => refs.flatMap { case ref: Long =>
        //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        //          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
        //          reverseRetracts ++ Seq(
        //            Add(ref, a, e),
        //            Add(e, a, ref)
        //          )
        //        }
        case Eq(biTargetRef :: Nil) =>
          //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, edgeA, a).toSeq.map(revRef => Retract(revRef, a, edgeA)) else Nil
          //          if (biTargetRef == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
          //          reverseRetracts ++
          Seq(
            Add(biTargetRef, biEdgeRefAttr, edgeB),
            Add(edgeA, a, biTargetRef)
          )
        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
        //          Seq(Add(ref, a, e), Add(e, a, ref))
        //        }

        case biTargetRef => {
          //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          //          reverseRetracts ++
          Seq(
            Add(biTargetRef, biEdgeRefAttr, edgeB),
            Add(edgeA, a, biTargetRef)
          )
        }
      }

    }

    def mapping = arg match {
      case Mapping(pairs)       => pairs.flatMap {
        case (key, value) => {
          val existing = pairStr(e, a, key)
          existing.size match {
            case 0 => Seq(Add(e, a, key + "@" + value))
            case 1 => Seq(Retract(e, a, existing.head), Add(e, a, key + "@" + value))
            case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
          }
        }
      }
      case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
      case Remove(removeValues) => removeValues.flatMap { case key: String =>
        val existing = pairStr(e, a, key)
        existing.size match {
          case 0 => None
          case 1 => Some(Retract(e, a, existing.head))
          case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
        }
      }

    }

    def default = arg match {
      case Replace(oldNew)      => oldNew.toSeq.flatMap {
        case (oldValue, newValue) => Seq(Retract(e, a, p(oldValue)), Add(e, a, p(newValue)))
      }
      case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
      case Remove(removeValues) => removeValues.map(v => Retract(e, a, p(v)))
      case Eq(vs)               => vs.map(v => Add(e, a, p(v)))
      case vs: Set[_]           => vs.map(v => Add(e, a, p(v)))
      case m: Map[_, _]         => m map {
        case (k, d: Date) => Add(e, a, k + "@" + format2(d)) // Need uniform Date format
        case (k, v)       => Add(e, a, k + "@" + v)
      }
      case v :: Nil             => Seq(Add(e, a, p(v)))
      case vs: List[_]          => vs.map(v => Add(e, a, p(v)))
      case v                    => Seq(Add(e, a, p(v)))
    }

    val newStmtsX = bi match {
      case BiSelfRef(card)                 => biSelfRef(card)
      case BiSelfRefAttr(card)             => biSelfRef(card)
      case BiOtherRef(card)                => biOtherRef(card)
      case BiOtherRefAttr(card)            => biOtherRef(card)
      case BiEdgeRef(card, attr)           => biEdgeRef(card, attr)
      case BiEdgeRefAttr(card, attr)       => biEdgeRef(card, attr)
      case BiEdgePropAttr(card)            => biEdgeProp(card)
      case BiEdgePropRefAttr(card)         => biEdgeProp(card)
      case BiEdgePropRef(card)             => biEdgeProp(card)
      case BiTargetRef(card, attr)         => biTargetRef(card, attr)
      case BiTargetRefAttr(card, attr)     => biTargetRef(card, attr)
      case _ if prefix.contains("mapping") => mapping
      case _                               => default
    }

    //    val newStmts = if (bi.startsWith("biSelfRef")) arg match {
    //
    //      // Each bidirectional manipulation takes care of the reverse ref
    //
    //      case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
    //        case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //      }
    //      case Remove(removeRefs) => removeRefs.flatMap {
    //        case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //      }
    //      case Eq(refs)           => refs.flatMap { case ref: Long =>
    //        val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //        if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
    //        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
    //      }
    //      case refs: Set[_]       => refs.flatMap { case ref: Long =>
    //        Seq(Add(ref, a, e), Add(e, a, ref))
    //      }
    //      case ref                => {
    //        val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
    //      }
    //
    //    }
    //    else if (meta.startsWith("biEdgeRef")) {
    //      val edgeB = otherEdgeId0 getOrElse sys.error("[Model2Transaction:valueStmts ("biEdgeRef)]  Missing id of other edge.")
    //      val List(card, targetAttr) = meta.split("@").toList.tail
    //      arg match {
    //
    //        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
    //        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //        //        case Remove(removeRefs) => removeRefs.flatMap {
    //        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //
    //        //        case Eq(refs)           => refs.flatMap { case ref: Long =>
    //        ////          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //        ////          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
    //        ////          reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
    //        //
    //        //          Seq(Add(otherEdgeId, a, e), Add(e, a, ref))
    //        //        }
    //
    //        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
    //        //          Seq(Add(ref, a, e), Add(e, a, ref))
    //        //        }
    //        case edgeA => {
    //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //          reverseRetracts ++ Seq(
    //            // Interlink edge entities so that we later know which other one to update
    //            Add(edgeA, ":molecule_Meta/otherEdge", edgeB),
    //            Add(edgeB, ":molecule_Meta/otherEdge", edgeA),
    //            Add(edgeB, targetAttr, e),
    //            Add(e, a, edgeA)
    //          )
    //        }
    //      }
    //
    //    }
    //    else if (meta.startsWith("biEdgeProp")) {
    //      val edgeA = e
    //      val edgeB = otherEdgeId0 match {
    //        case Some(otherEdgeId) if otherEdgeId == edgeA => sys.error("[Model2Transaction:valueStmts (biEdgeProp)]  Other edge id is unexpectedly the same as this edge id.")
    //        case None                                      => sys.error("[Model2Transaction:valueStmts (biEdgeProp)]  Missing id of other edge.")
    //        case Some(otherEdgeId)                         => otherEdgeId
    //      }
    //
    //      if (prefix.contains("mapping")) arg match {
    //        case Mapping(pairs) => pairs.flatMap {
    //          case (key, value) => {
    //            val existing = pairStr(edgeA, a, key)
    //            existing.size match {
    //              case 0 => Seq(
    //                Add(edgeB, a, key + "@" + value),
    //                Add(edgeA, a, key + "@" + value)
    //              )
    //              case 1 => Seq(
    //                Retract(edgeB, a, existing.head), Add(edgeB, a, key + "@" + value),
    //                Retract(edgeA, a, existing.head), Add(edgeA, a, key + "@" + value)
    //              )
    //              case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
    //            }
    //          }
    //        }
    //        //            case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
    //        //            case Remove(removeValues) => removeValues.flatMap { case key: String =>
    //        //              val existing = pairStr(e, a, key)
    //        //              existing.size match {
    //        //                case 0 => None
    //        //                case 1 => Some(Retract(e, a, existing.head))
    //        //                case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
    //        //              }
    //        //            }
    //
    //      } else arg match {
    //
    //        //        case Replace(oldNew)      => oldNew.toSeq.flatMap {
    //        //          case (oldValue, newValue) => Seq(Retract(e, a, p(oldValue)), Add(e, a, p(newValue)))
    //        //        }
    //        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
    //        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //        //        case Remove(removeRefs) => removeRefs.flatMap {
    //        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //        case Eq(vs) => vs.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))
    //
    //        //        case Eq(vs)           => vs.flatMap { v =>
    //        //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //        //          reverseRetracts ++ Seq(Add(otherEdgeId, a, e), Add(e, a, ref))
    //        //        }
    //        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
    //        //          Seq(Add(ref, a, e), Add(e, a, ref))
    //        //        }
    //        //                case ref                => {
    //        //                  val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, edgeA, a).toSeq.map(revRef => Retract(revRef, a, edgeA)) else Nil
    //        //                  reverseRetracts ++ Seq(
    //        //                    Add(edgeB, a, ref),
    //        //                    Add(edgeA, a, ref)
    //        //                  )
    //        //                }
    //
    //        case vs: Set[_]   => vs.toSeq.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))
    //        case m: Map[_, _] => m.flatMap {
    //          case (k, d: Date) => Seq(
    //            Add(edgeB, a, k + "@" + format2(d)),
    //            Add(edgeA, a, k + "@" + format2(d))
    //          )
    //          case (k, v)       => Seq(
    //            Add(edgeB, a, k + "@" + v),
    //            Add(edgeA, a, k + "@" + v)
    //          )
    //        }
    //        case v :: Nil     => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v)))
    //        case vs: List[_]  => vs.flatMap(v => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v))))
    //        case v            => Seq(Add(edgeB, a, p(v)), Add(edgeA, a, p(v)))
    //
    //      }
    //
    //    }
    //    else if (meta.startsWith("biTargetRef")) {
    //      val edgeA = e
    //      val edgeB = otherEdgeId0 getOrElse sys.error("[Model2Transaction:valueStmts (biTargetRef)]  Missing id of other edge.")
    //      //      val List(card, "biEdgeRefAttr) = meta.split("@").toList.tail
    //      val biEdgeRefAttr = meta.split("@").toList.last
    //      arg match {
    //
    //        // Each bidirectional manipulation takes care of the reverse ref
    //
    //        //        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
    //        //          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //        //        case Remove(removeRefs) => removeRefs.flatMap {
    //        //          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
    //        //        }
    //        //        case Eq(refs) => refs.flatMap { case ref: Long =>
    //        //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //        //          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
    //        //          reverseRetracts ++ Seq(
    //        //            Add(ref, a, e),
    //        //            Add(e, a, ref)
    //        //          )
    //        //        }
    //        case Eq(biTargetRef :: Nil) =>
    //          //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, edgeA, a).toSeq.map(revRef => Retract(revRef, a, edgeA)) else Nil
    //          //          if (biTargetRef == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
    //          //          reverseRetracts ++
    //          Seq(
    //            Add(biTargetRef, "biEdgeRefAttr, edgeB),
    //            Add(edgeA, a, biTargetRef)
    //          )
    //        //        case refs: Set[_]       => refs.flatMap { case ref: Long =>
    //        //          Seq(Add(ref, a, e), Add(e, a, ref))
    //        //        }
    //
    //        case biTargetRef => {
    //          //          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
    //          //          reverseRetracts ++
    //          Seq(
    //            Add(biTargetRef, "biEdgeRefAttr, edgeB),
    //            Add(edgeA, a, biTargetRef)
    //          )
    //        }
    //      }
    //
    //    }
    //    else if (prefix.contains("mapping")) arg match {
    //      case Mapping(pairs)       => pairs.flatMap {
    //        case (key, value) => {
    //          val existing = pairStr(e, a, key)
    //          existing.size match {
    //            case 0 => Seq(Add(e, a, key + "@" + value))
    //            case 1 => Seq(Retract(e, a, existing.head), Add(e, a, key + "@" + value))
    //            case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
    //          }
    //        }
    //      }
    //      case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
    //      case Remove(removeValues) => removeValues.flatMap { case key: String =>
    //        val existing = pairStr(e, a, key)
    //        existing.size match {
    //          case 0 => None
    //          case 1 => Some(Retract(e, a, existing.head))
    //          case _ => sys.error("[Model2Transaction:valueStmts] Unexpected number of mapped values with the same key:\n" + existing.mkString("\n"))
    //        }
    //      }
    //
    //    }
    //    else arg match {
    //      case Replace(oldNew)      => oldNew.toSeq.flatMap {
    //        case (oldValue, newValue) => Seq(Retract(e, a, p(oldValue)), Add(e, a, p(newValue)))
    //      }
    //      case Remove(Seq())        => attrValues(conn.db, e, a).toSeq.map(v => Retract(e, a, p(v)))
    //      case Remove(removeValues) => removeValues.map(v => Retract(e, a, p(v)))
    //      case Eq(vs)               => vs.map(v => Add(e, a, p(v)))
    //      case vs: Set[_]           => vs.map(v => Add(e, a, p(v)))
    //      case m: Map[_, _]         => m map {
    //        case (k, d: Date) => Add(e, a, k + "@" + format2(d)) // Need uniform Date format
    //        case (k, v)       => Add(e, a, k + "@" + v)
    //      }
    //      case v :: Nil             => Seq(Add(e, a, p(v)))
    //      case vs: List[_]          => vs.map(v => Add(e, a, p(v)))
    //      case v                    => Seq(Add(e, a, p(v)))
    //    }

    stmts ++ newStmtsX
  }

  def splitStmts(): (Seq[Statement], Seq[Statement]) = {
    val (genericStmtsOpt, genericTxStmtsOpt) = stmtsModel.map {
      case tx@Add('tx, _, Values(vs, prefix), _) => (None, Some(tx))
      case other                                 => (Some(other), None)
    }.unzip
    (genericStmtsOpt.flatten, genericTxStmtsOpt.flatten)
  }

  def lastE(stmts: Seq[Statement], attr: String, nestedE: Any = 0, bi: Generic = NoValue) = {
    bi match {
      case BiTargetRef(_, _) => {
        val lastEdgeNs = attr.split("/").head
        stmts.reverse.collectFirst {
          case Add(e, a, v, _) if a.startsWith(lastEdgeNs) && e.isInstanceOf[db.DbId] => e
        }.getOrElse(sys.error(s"[Model2Transaction:lastE] Couldn't find namespace `$lastEdgeNs` in any previous Add statements:\n" + stmts.mkString("\n")))
      }
      case _                 => {
        if (nestedE != 0)
          nestedE
        else if (stmts.isEmpty)
          tempId(attr)
        else if (stmts.last.e == -1)
          stmts.last.v
        else
          stmts.last.e
      }
    }
    //    if (bi.startsWith("biTargetRef")) {
    //      val lastEdgeNs = attr.split("/").head
    //      stmts.reverse.collectFirst {
    //        case Add(e, a, v, _) if a.startsWith(lastEdgeNs) && e.isInstanceOf[db.DbId] => e
    //      }.getOrElse(sys.error(s"[Model2Transaction:lastE] Couldn't find namespace `$lastEdgeNs` in any previous Add statements:\n" + stmts.mkString("\n")))
    //    } else if (nestedE != 0)
    //      nestedE
    //    else if (stmts.isEmpty)
    //      tempId(attr)
    //    else if (stmts.last.e == -1)
    //      stmts.last.v
    //    else
    //      stmts.last.e
  }

  def lastV(stmts: Seq[Statement], attr: String, nestedE: Any = 0) = {
    if (nestedE != 0)
      nestedE
    else if (stmts.isEmpty)
      tempId(attr)
    else
      stmts.last.v
  }

  def matchDataStmt(stmts: Seq[Statement], genericStmt: Statement, arg: Any, cur: Int, next: Int, nestedE: Any, edgeB: Option[AnyRef]) = genericStmt match {

    // Keep current cursor (add no new data in this iteration)
    case Add('tempId, a, refNs: String, bi)              => (cur, edgeB, valueStmts(stmts, tempId(a), a, tempId(refNs), None, bi, edgeB))
    case Add('e, a, refNs: String, bi@BiSelfRef(1))      => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs), None, bi, edgeB))
    case Add('e, a, refNs: String, bi@BiSelfRef(2))      => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs), None, bi, edgeB))
    case Add('e, a, refNs: String, bi@BiEdgeRef(_, _))   =>
      val (edgeA, edgeB) = (tempId(refNs), Some(tempId(a)))
      (cur, edgeB, valueStmts(stmts, lastE(stmts, a), a, edgeA, None, bi, edgeB))
    case Add('e, a, refNs: String, bi@BiTargetRef(_, _)) => (cur, None, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(refNs), None, bi, edgeB))
    case Add('e, a, refNs: String, bi)                   => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs), None, bi, edgeB))
    case Add('v, a, 'tempId, bi)                         => (cur, edgeB, valueStmts(stmts, stmts.last.v, a, tempId(a), None, bi, edgeB))
    case Retract(e, a, v, bi)                            => (cur, edgeB, stmts)

    // Advance cursor for next value in data row
    case Add('tempId, a, 'arg, bi)                                                    => (next, edgeB, valueStmts(stmts, tempId(a), a, arg, None, bi, edgeB))
    case Add('tempId, a, Values(EnumVal, prefix), bi)                                 => (next, edgeB, valueStmts(stmts, tempId(a), a, arg, prefix, bi, edgeB))
    case Add('tempId, a, Values(vs, prefix), bi)                                      => (next, edgeB, valueStmts(stmts, tempId(a), a, vs, prefix, bi, edgeB))
    case Add('remove_me, a, 'arg, bi)                                                 => (next, edgeB, valueStmts(stmts, -1, a, arg, None, bi, edgeB))
    case Add('arg, a, 'tempId, bi)                                                    => (next, edgeB, valueStmts(stmts, arg, a, tempId(a), None, bi, edgeB))
    case Add('e, a, 'arg, bi@BiSelfRef(1))                                            => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, None, bi, edgeB))
    case Add('e, a, 'arg, bi@BiSelfRef(2))                                            => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, None, bi, edgeB))
    case Add('e, a, 'arg, bi)                                                         => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, None, bi, edgeB))
    case Add('e, a, Values(EnumVal, prefix), bi)                                      => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, prefix, bi, edgeB))
    case Add('e, a, Values(vs, prefix), bi)                                           => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE), a, vs, prefix, bi, edgeB))
    case Add('v, a, 'arg, bi) if stmts.nonEmpty && stmts.last.v.isInstanceOf[db.DbId] => (next, edgeB, valueStmts(stmts, stmts.last.v, a, arg, None, bi, edgeB))
    case Add('v, a, 'arg, bi)                                                         => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg, None, bi, edgeB))
    case Add('v, a, Values(EnumVal, prefix), bi)                                      => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg, prefix, bi, edgeB))
    case Add('v, a, Values(vs, prefix), bi)                                           => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, vs, prefix, bi, edgeB))
    case Add('tx, a, 'arg, bi)                                                        => (next, edgeB, valueStmts(stmts, tempId("tx"), a, arg, None, bi, edgeB))
    case Add(_, _, nestedGenStmts0: Seq[_], _) if arg == Nil                          => (next, edgeB, stmts)

    // Nested data structures
    case Add(e, ref, nestedGenStmts0: Seq[_], bi) => {
      //      x(51, e, ref, nestedStmts, stmts)
      val parentE = if (e == 'parentId)
        tempId(ref)
      else if (stmts.isEmpty)
        e
      else
        stmts.reverse.collectFirst {
          // Find entity value of Add statement with matching namespace
          case Add(e1, a, _, _) if a.replaceFirst("/.*", "") == ref.replaceFirst("/.*", "") => e1
        }.getOrElse(sys.error("[Model2Transaction:matchDataStmt] Couldn't find previous statement with matching namespace. e: " + e + "  -- ref: " + ref.replaceFirst("/.*", "")))

      val nestedGenStmts = nestedGenStmts0.map { case s: Statement => s }
      val nestedRows = untupleNestedArgss(nestedGenStmts0, arg)

      val nestedInsertStmts: Seq[Statement] = bi match {

        // Bidirectional self references ...........................................................

        // Nested self references
        // living_Person.name.Friends.*(living_Person.name) insert List(...
        case BiSelfRef(2) => nestedRows.flatMap { nestedRow =>
          val nestedE = tempId(ref)
          val bondStmts = Seq(
            Add(nestedE, ref, parentE),
            Add(parentE, ref, nestedE)
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE)
          //            val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE, edgeB) // todo edgeB necessary?
          bondStmts ++ nestedStmts
        }


        // Bidirectional propert edges ...........................................................

        // Nested property edges
        // living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(...
        case BiEdgeRef(2, revAttr) => nestedRows.flatMap { nestedRow =>
          val edgeA = tempId(revAttr)
          val edgeB1 = tempId(ref)
          val bondStmts = Seq(
            Add(edgeA, ":molecule_Meta/otherEdge", edgeB1),
            Add(edgeB1, ":molecule_Meta/otherEdge", edgeA),
            Add(edgeB1, revAttr, parentE),
            Add(parentE, ref, edgeA)
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, edgeA, Some(edgeB1))
          bondStmts ++ nestedStmts
        }

        // Nested edge property values
        // living_Person.name.Knows.weight.InCommon.*(living_Quality.name)._Knows.Person.name insert List(...
        case BiEdgePropRef(2) => nestedRows.flatMap { nestedRow =>
          val edgeA = tempId(ref)
          val bondStmts = Seq(
            Add(edgeB.get, ref, edgeA),
            Add(parentE, ref, edgeA)
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, edgeA)
          //            val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, edgeA, edgeB) // todo edgeB necessary?
          bondStmts ++ nestedStmts
        }


        // Uni-directional (normal) nested rows ...............................................

        // Invoice.id.InvoiceLines * InvoiceLine.no.item.price
        case _ => nestedRows.flatMap { nestedRow =>
          val nestedE = tempId(ref)
          val bondStmt = Add(parentE, ref, nestedE)
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE)
          bondStmt +: nestedStmts
        }
      }

      (next, edgeB, stmts ++ nestedInsertStmts)
    }

    case unexpected => sys.error("[Model2Transaction:matchDataStmt] Unexpected insert statement: " + unexpected)
  }

  def resolveStmts(genericStmts: Seq[Statement], row: Seq[Any], nestedE0: Any = 0, edgeB0: Option[AnyRef] = None): Seq[Statement] = {
    genericStmts.foldLeft(0, edgeB0, Seq[Statement]()) { case ((cur, edgeB, stmts0), genericStmt0) =>
      val arg0 = row.get(cur)
      val next = if ((cur + 1) < row.size) cur + 1 else cur
      val (stmts, nestedE) = if (stmts0.isEmpty)
        (stmts0, nestedE0)
      else stmts0.last match {
        case Add('ns, ns, backRef, _) => (stmts0.init, backRef)
        case _                        => (stmts0, nestedE0)
      }
      (arg0, genericStmt0) match {
        // null values not allowed
        case (null, _) => sys.error("[Model2Transaction:insertStmts] null values not allowed. Please use `attr$` for Option[tpe] values.")

        // Backreference - with mandatory previous ref
        case (_, br@Add('ns, ns, "", _))                                                        => {
          val backRef = stmts.reverse.collectFirst {
            case Add(e, a, v, _) if a.startsWith(ns) && e.isInstanceOf[db.DbId] => e
          }.getOrElse(sys.error(s"[Model2Transaction:insertStmts] Couldn't find namespace `$ns` in any previous Add statements.\n" + stmts.mkString("\n")))
          (cur, edgeB, stmts :+ Add('ns, ns, backRef))
        }
        case (None, Add('e, a, refNs: String, _))                                               => (cur, edgeB, valueStmts(stmts, lastE(stmts, a), a, tempId(refNs)))
        case (None, _)                                                                          => (next, edgeB, stmts)
        case (Some(arg), genericStmt)                                                           => matchDataStmt(stmts, genericStmt, arg, cur, next, nestedE, edgeB)
        case (arg, Add('e, a, 'arg, _)) if stmts.nonEmpty && stmts.last.v.isInstanceOf[db.DbId] => (next, edgeB, valueStmts(stmts, stmts.last.v, a, arg))
        case (arg, genericStmt)                                                                 => matchDataStmt(stmts, genericStmt, arg, cur, next, nestedE, edgeB)
      }
    }._3.filterNot(_.e == -1)
  }

  def insertStmts(dataRows: Seq[Seq[Any]]): Seq[Seq[Statement]] = {
    val (genericStmts, genericTxStmts) = splitStmts()
    val dataStmtss: Seq[Seq[Statement]] = dataRows.map(resolveStmts(genericStmts, _))
    val txId = tempId("tx")
    val txStmtss: Seq[Seq[Statement]] = Seq(genericTxStmts.foldLeft(Seq[Statement]()) {
      case (stmts, Add('tx, a, Values(vs, prefix), _)) => valueStmts(stmts, txId, a, vs, prefix)
      case (stmts, unexpected)                         => sys.error("[Model2Transaction:insertStmts:txStmts] Unexpected insert statement: " + unexpected)
    })
    dataStmtss ++ (if (txStmtss.head.isEmpty) Nil else txStmtss)
  }

  def saveStmts(): Seq[Statement] = {
    val txId = tempId("tx")
    stmtsModel.foldLeft(None: Option[AnyRef], Seq[Statement]()) { case ((edgeB, stmts), stmt) =>
      stmt match {
        case Add('tempId, a, Values(vs, prefix), bi)         => (edgeB, valueStmts(stmts, tempId(a), a, vs, prefix, bi, edgeB))
        case Add('e, a, Values(vs, prefix), bi)              => (edgeB, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, bi, edgeB))
        case Add('e, a, 'tempId, bi)                         => (edgeB, valueStmts(stmts, lastE(stmts, a), a, tempId(a), None, bi, edgeB))
        case Add(id, a, 'tempId, bi)                         => (edgeB, valueStmts(stmts, id, a, tempId(a), None, bi, edgeB))
        case Add('e, a, refNs: String, bi@BiEdgeRef(_, _))   =>
          val (edgeA, edgeB) = (tempId(refNs), Some(tempId(a)))
          (edgeB, valueStmts(stmts, lastE(stmts, a), a, edgeA, None, bi, edgeB))
        case Add('e, a, refNs: String, bi@BiTargetRef(_, _)) => (None, valueStmts(stmts, lastE(stmts, a), a, tempId(refNs), None, bi, edgeB))
        case Add('e, a, refNs: String, bi)                   => (edgeB, valueStmts(stmts, lastE(stmts, a), a, tempId(refNs), None, bi, edgeB))
        case Add('v, a, Values(vs, prefix), bi)              => (edgeB, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, edgeB))
        case Add('tx, a, Values(vs, prefix), bi)             => (edgeB, valueStmts(stmts, txId, a, vs, prefix, bi, edgeB))
        case Add('ns, a, _, _)                               => (edgeB, stmts)
        case Retract(_, _, _, _)                             => (edgeB, stmts)
        case Add(id: Long, a, Values(_, _), _)               => sys.error(s"[Model2Transaction:saveStmts] With a given id `$id` please use `update` instead.")
        case Add(_, a, 'arg, _)                              => sys.error(s"[Model2Transaction:saveStmts] Attribute `$a` needs a value applied")
        case unexpected                                      => sys.error("[Model2Transaction:saveStmts] Unexpected save statement: " + unexpected)
      }
    }._2
  }

  def updateStmts(): Seq[Statement] = {
    val (genericStmts, genericTxStmts) = splitStmts()
    val dataStmts: Seq[Statement] = genericStmts.foldLeft(0, Seq[Statement]()) {
      case ((i, stmts), genericStmt) =>
        val j = i + 1
        genericStmt match {
          case Add('e, a, Values(vs, prefix), bi)     => (j, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, bi))
          case Add('e, a, 'tempId, bi)                => (i, valueStmts(stmts, lastE(stmts, a), a, tempId(a), None, bi))
          case Add('v, a, Values(vs, prefix), bi)     => (j, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi))
          case Add('tx, a, Values(vs, prefix), bi)    => (j, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi))
          case Add(e, a, Values(vs, prefix), bi)      => (j, valueStmts(stmts, e, a, vs, prefix, bi))
          case Add(e, a, 'tempId, bi)                 => (j, valueStmts(stmts, e, a, tempId(a), None, bi))
          case Retract('e, a, Values(vs, prefix), bi) => (j, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, bi))
          case Retract(e, a, Values(vs, prefix), bi)  => (j, valueStmts(stmts, e, a, vs, prefix, bi))
          case Add(_, a, 'arg, _)                     => sys.error(s"[Model2Transaction:updateStmts] Attribute `$a` needs a value applied")
          case unexpected                             => sys.error("[Model2Transaction:updateStmts] Unexpected update statement: " + unexpected)
        }
    }._2
    val txId = tempId("tx")
    val txStmts: Seq[Statement] = genericTxStmts.foldLeft(Seq[Statement]()) {
      case (stmts, Add('tx, a, Values(vs, prefix), _)) => valueStmts(stmts, txId, a, vs, prefix)
      case (stmts, unexpected)                         => sys.error("[Model2Transaction:updateStmts:txStmts] Unexpected insert statement: " + unexpected)
    }
    dataStmts ++ txStmts
  }

  private def untupleNestedArgss(stmts: Seq[Any], arg0: Any): Seq[Seq[Any]] = {
    val (argArity, arg) = arg0 match {
      case a: Seq[_]  => a.head match {
        case None       => sys.error("[Model2Transaction:untupleNestedArgss] Please use `List()` instead of `List(None)` for nested null values.")
        case null       => sys.error("[Model2Transaction:untupleNestedArgss] Please use `List()` instead of `List(null)` for nested null values.")
        case p: Product => (p.productArity, a)
        case l: Seq[_]  => (l.size, a)
        case _          => (1, a)
      }
      case unexpected => sys.error("[Model2Transaction:untupleNestedArgss] Unexpected data: " + unexpected)
    }
    val argStmts = stmts.collect {
      case a@Add(_, _, 'arg, _)                => a
      case a@Add(_, _, Values(vs, prefix), _)  => a
      case a@Add(_, _, nestedStmts: Seq[_], _) => a
    }
    val stmtsSize = argStmts.size
    assert(argArity == stmtsSize, s"[Model2Transaction:untupleNestedArgss] Arity of statements and arguments should match. Found: \n" +
      s"Statements (arity $stmtsSize): " + stmts.mkString("\n  ", "\n  ", "\n") +
      s"Arguments0                  : " + arg0 +
      s"Arguments  (arity $argArity): " + arg.mkString("\n  ", "\n  ", "\n"))

    arg map tupleToSeq
  }
}