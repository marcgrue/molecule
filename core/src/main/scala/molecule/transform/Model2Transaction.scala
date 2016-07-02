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

    def aRef(gs: Seq[Generic], card: Int = 0) = if (gs.contains(BiRef_) && card > 0) "biRef" + card else if (gs.contains(BiRef_)) "biRef" else ""
    def bRef(meta: String, card: Int) = if (meta == "biRef") "biRef" + card else meta.replace("@", s"@$card@")
    def edgeProp(gs: Seq[Generic]) = if (gs.intersect(Seq(EdgePropAttr, EdgePropRef)).nonEmpty) "edgeProp" else ""

    def resolveElement(eSlot: Any, stmts: Seq[Statement], element: Element): (Any, Seq[Statement]) = (eSlot, element) match {
      case ('_, Meta(ns, "", "e", _, EntValue))             => ('arg, stmts)
      case ('_, Meta(ns, "", "e", _, Eq(Seq(id: Long))))    => (Eid(id), stmts)
      case ('_, Atom(ns, name, _, c, VarValue, _, gs, _))   => ('e, stmts :+ Add('tempId, s":$ns/$name", 'arg, aRef(gs, c)))
      case ('_, Atom(ns, name, _, c, value, prefix, gs, _)) => ('e, stmts :+ Add('tempId, s":$ns/$name", Values(value, prefix), aRef(gs, c)))
      case ('_, Bond(ns, refAttr, refNs, _, _))             => ('v, stmts :+ Add('tempId, s":$ns/$refAttr", s":$refNs"))

      case (e, Nested(Bond(ns, refAttr, _, _, meta), elements)) =>
        val nested = elements.foldLeft('v: Any, Seq[Statement]()) {
          case ((eSlot1, stmts1), element1) => resolveElement(eSlot1, stmts1, element1)
        }._2
        val parentId = if (e == '_) 'parentId else 'e
        ('e, stmts :+ Add(parentId, s":$ns/$refAttr", nested, meta + 2))

      // First with id
      case (Eid(id), Atom(ns, name, _, c, value@Remove(_), prefix, gs, _)) => ('e, stmts :+ Retract(id, s":$ns/$name", Values(value, prefix), aRef(gs, c)))
      case (Eid(id), Atom(ns, name, _, c, value, prefix, gs, _))           => ('e, stmts :+ Add(id, s":$ns/$name", Values(value, prefix), aRef(gs, c)))
      case (Eid(id), Bond(ns, refAttr, refNs, c, meta))                    => ('v, stmts :+ Add(id, s":$ns/$refAttr", 'tempId, bRef(meta, c)))

      // Same namespace
      case ('e, Atom(ns, name, _, _, value@Remove(_), prefix, _, _)) => ('e, stmts :+ Retract('e, s":$ns/$name", Values(value, prefix)))
      case ('e, Atom(ns, name, _, c, VarValue, _, gs, _))            => ('e, stmts :+ Add('e, s":$ns/$name", 'arg, aRef(gs, c)))
      case ('e, Atom(ns, name, _, c, value, prefix, gs, _))          => ('e, stmts :+ Add('e, s":$ns/$name", Values(value, prefix), aRef(gs, c)))
      case ('e, Bond(ns, refAttr, refNs, c, meta))                   => ('v, stmts :+ Add('e, s":$ns/$refAttr", s":$refNs", bRef(meta, c)))

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
      case ('v, Atom(ns, name, _, _, VarValue, _, _, _))    => ('e, stmts :+ Add('v, s":$ns/$name", 'arg))
      case ('v, Atom(ns, name, _, _, value, prefix, gs, _)) => ('e, stmts :+ Add('v, s":$ns/$name", Values(value, prefix), edgeProp(gs)))
      case ('v, Bond(ns, refAttr, _, _, _))                 => ('v, stmts :+ Add('v, s":$ns/$refAttr", 'tempId))

      // Add one extra generic statement to receive the eid arg for the following statement to use
      // (we then discard that temporary statement from the value statements)
      case ('arg, Atom(ns, name, _, _, VarValue, _, _, _)) => ('e, stmts :+ Add('remove_me, s":$ns/$name", 'arg) :+ Add('v, s":$ns/$name", 'arg))
      case ('arg, Bond(ns, refAttr, _, _, _))              => ('v, stmts :+ Add('arg, s":$ns/$refAttr", 'tempId))

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

  private def valueStmts(stmts: Seq[Statement], e: Any, a: String, arg: Any, prefix: Option[String] = None, meta: String = "", otherEdgeId: Option[AnyRef] = None): Seq[Statement] = {
    def p(arg: Any) = if (prefix.isDefined) prefix.get + arg else arg

    val newStmts = if (meta.startsWith("biRef")) arg match {

      // Each bidirectional manipulation takes care of the reverse ref

      case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
        case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
      }
      case Remove(removeRefs) => removeRefs.flatMap {
        case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
      }
      case Eq(refs)           => refs.flatMap { case ref: Long =>
        val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
      }
      case refs: Set[_]       => refs.flatMap { case ref: Long =>
        Seq(Add(ref, a, e), Add(e, a, ref))
      }
      case ref                => {
        val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
      }

    } else if (meta.startsWith("edgeRef")) {
      val List(card, targetAttr) = meta.split("@").toList.tail
      arg match {

        // Each bidirectional manipulation takes care of the reverse ref

        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Remove(removeRefs) => removeRefs.flatMap {
          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Eq(refs)           => refs.flatMap { case ref: Long =>
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
          reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case refs: Set[_]       => refs.flatMap { case ref: Long =>
          Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case ref                => {
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          reverseRetracts ++ Seq(Add(ref, targetAttr, e), Add(e, a, ref))
        }

      }

    } else if (meta.startsWith("edgeProp")) {
      val List(card, targetAttr) = meta.split("@").toList.tail
      arg match {

        // Each bidirectional manipulation takes care of the reverse ref

        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Remove(removeRefs) => removeRefs.flatMap {
          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Eq(refs)           => refs.flatMap { case ref: Long =>
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
          reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case refs: Set[_]       => refs.flatMap { case ref: Long =>
          Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case ref                => {
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          reverseRetracts ++ Seq(Add(ref, targetAttr, e), Add(e, a, ref))
        }

      }

    } else if (meta.startsWith("targetRef")) {
      val List(card, edgeRefAttr) = meta.split("@").toList.tail
      arg match {

        // Each bidirectional manipulation takes care of the reverse ref

        case Remove(Seq())      => attrValues(conn.db, e, a).toSeq.flatMap {
          case ref => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Remove(removeRefs) => removeRefs.flatMap {
          case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref))
        }
        case Eq(refs)           => refs.flatMap { case ref: Long =>
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          if (ref == e) throw new IllegalArgumentException("Current entity and referenced entity ids can't be the same")
          reverseRetracts ++ Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case refs: Set[_]       => refs.flatMap { case ref: Long =>
          Seq(Add(ref, a, e), Add(e, a, ref))
        }
        case ref                => {
          val reverseRetracts = if (meta.endsWith("1")) attrValues(conn.db, e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
          reverseRetracts ++ Seq(Add(ref, edgeRefAttr, e), Add(e, a, ref))
        }
      }

    } else if (prefix.contains("mapping")) arg match {
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

    } else arg match {
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

    stmts ++ newStmts
  }

  def splitStmts(): (Seq[Statement], Seq[Statement]) = {
    val (dataStmts0, txStmts0) = stmtsModel.map {
      case tx@Add('tx, _, Values(vs, prefix), _) => (None, Some(tx))
      case other                                 => (Some(other), None)
    }.unzip
    (dataStmts0.flatten, txStmts0.flatten)
  }

  def lastE(stmts: Seq[Statement], attr: String, nestedE: Any = 0) = {
    if (nestedE != 0)
      nestedE
    else if (stmts.isEmpty)
      tempId(attr)
    else if (stmts.last.e == -1)
      stmts.last.v
    else
      stmts.last.e
  }

  def lastV(stmts: Seq[Statement], attr: String, nestedE: Any = 0) = {
    if (nestedE != 0)
      nestedE
    else if (stmts.isEmpty)
      tempId(attr)
    else
      stmts.last.v
  }

  def matchDataStmt(stmts: Seq[Statement], dataStmt: Statement, arg: Any, cur: Int, next: Int, nestedE: Any = 0) = dataStmt match {
    // Keep current cursor (add no new data in this iteration)
    case Add('tempId, a, refNs: String, _)   => (cur, valueStmts(stmts, tempId(a), a, tempId(refNs)))
    case Add('e, a, refNs: String, "biRef1") => (cur, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs), None, "biRef1"))
    case Add('e, a, refNs: String, "biRef2") => (cur, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs), None, "biRef2"))
    case Add('e, a, refNs: String, _)        => (cur, valueStmts(stmts, lastE(stmts, a, nestedE), a, tempId(refNs)))
    case Add('v, a, 'tempId, _)              => (cur, valueStmts(stmts, stmts.last.v, a, tempId(a)))
    case Retract(e, a, v, _)                 => (cur, stmts)

    // Advance cursor for next value in data row
    case Add('tempId, a, 'arg, _)                                                    => (next, valueStmts(stmts, tempId(a), a, arg))
    case Add('tempId, a, Values(EnumVal, prefix), _)                                 => (next, valueStmts(stmts, tempId(a), a, arg, prefix))
    case Add('tempId, a, Values(vs, prefix), _)                                      => (next, valueStmts(stmts, tempId(a), a, vs, prefix))
    case Add('remove_me, a, 'arg, _)                                                 => (next, valueStmts(stmts, -1, a, arg))
    case Add('arg, a, 'tempId, _)                                                    => (next, valueStmts(stmts, arg, a, tempId(a)))
    case Add('e, a, 'arg, "biRef1")                                                  => (next, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, None, "biRef1"))
    case Add('e, a, 'arg, "biRef2")                                                  => (next, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, None, "biRef2"))
    case Add('e, a, 'arg, _)                                                         => (next, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg))
    case Add('e, a, Values(EnumVal, prefix), _)                                      => (next, valueStmts(stmts, lastE(stmts, a, nestedE), a, arg, prefix))
    case Add('e, a, Values(vs, prefix), _)                                           => (next, valueStmts(stmts, lastE(stmts, a, nestedE), a, vs, prefix))
    case Add('v, a, 'arg, _) if stmts.nonEmpty && stmts.last.v.isInstanceOf[db.DbId] => (next, valueStmts(stmts, stmts.last.v, a, arg))
    case Add('v, a, 'arg, _)                                                         => (next, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg))
    case Add('v, a, Values(EnumVal, prefix), _)                                      => (next, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg, prefix))
    case Add('v, a, Values(vs, prefix), _)                                           => (next, valueStmts(stmts, lastV(stmts, a, nestedE), a, vs, prefix))
    case Add('tx, a, 'arg, _)                                                        => (next, valueStmts(stmts, tempId("tx"), a, arg))
    case Add(e0, ref0, nestedStmts0: Seq[_], _) if arg == Nil                        => (next, stmts)
    case Add(e, ref, nestedStmts: Seq[_], meta)                                      => {
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
      val nestedRows = untupleNestedArgss(nestedStmts, arg)
      val nestedInsertStmts = nestedRows.flatMap { nestedRow =>
        val nestedE = tempId(ref)
        val bondStmt = if (meta == "biRef2")
          Seq(Add(nestedE, ref, parentE), Add(parentE, ref, nestedE))
        else
          Seq(Add(parentE, ref, nestedE))
        val nestedStmtsCasted = nestedStmts map { case s: Statement => s }
        val nestedStmts1 = resolveStmts(nestedStmtsCasted, nestedRow, nestedE)
        bondStmt ++ nestedStmts1
      }
      (next, stmts ++ nestedInsertStmts)
    }

    case unexpected => sys.error("[Model2Transaction:matchDataStmt] Unexpected insert statement: " + unexpected)
  }

  def resolveStmts(genericStmts: Seq[Statement], row: Seq[Any], nestedE0: Any = 0): Seq[Statement] = {
    genericStmts.foldLeft(0, Seq[Statement]()) { case ((cur, stmts0), dataStmt) =>
      val arg = row.get(cur)
      val next = if ((cur + 1) < row.size) cur + 1 else cur
      val (stmts, nestedE) = if (stmts0.isEmpty)
        (stmts0, nestedE0)
      else stmts0.last match {
        case Add('ns, ns, backRef, _) => (stmts0.init, backRef)
        case _                        => (stmts0, nestedE0)
      }
      (arg, dataStmt) match {
        case (null, _)                                                                        => sys.error(
          "[Model2Transaction:insertStmts] null values not allowed. Please use `attr$` for Option[tpe] values.")
        case (_, br@Add('ns, ns, "", _))                                                      => {
          val backRef = stmts.reverse.collectFirst {
            case Add(e, a, v, _) if a.startsWith(ns) && e.isInstanceOf[db.DbId] => e
          }.getOrElse(sys.error(s"[Model2Transaction:insertStmts] Couldn't find namespace `$ns` in any previous Add statements.\n" + stmts.mkString("\n")))
          (cur, stmts :+ Add('ns, ns, backRef))
        }
        case (None, Add('e, a, refNs: String, _))                                             => (cur, valueStmts(stmts, lastE(stmts, a), a, tempId(refNs)))
        case (None, _)                                                                        => (next, stmts)
        case (Some(arg1), _)                                                                  => matchDataStmt(stmts, dataStmt, arg1, cur, next, nestedE)
        case (_, Add('e, a, 'arg, _)) if stmts.nonEmpty && stmts.last.v.isInstanceOf[db.DbId] => (next, valueStmts(stmts, stmts.last.v, a, arg))
        case (_, _)                                                                           => matchDataStmt(stmts, dataStmt, arg, cur, next, nestedE)
      }
    }._2.filterNot(_.e == -1)
  }

  def insertStmts(dataRows: Seq[Seq[Any]]): Seq[Seq[Statement]] = {
    val (dataStmts, txStmts) = splitStmts()
    val dataStmtss: Seq[Seq[Statement]] = dataRows.map(resolveStmts(dataStmts, _))
    val txId = tempId("tx")
    val txStmtss: Seq[Seq[Statement]] = Seq(txStmts.foldLeft(Seq[Statement]()) {
      case (stmts, Add('tx, a, Values(vs, prefix), _)) => valueStmts(stmts, txId, a, vs, prefix)
      case (stmts, unexpected)                         => sys.error("[Model2Transaction:insertStmts:txStmts] Unexpected insert statement: " + unexpected)
    })
    dataStmtss ++ (if (txStmtss.head.isEmpty) Nil else txStmtss)
  }

  def saveStmts(): Seq[Statement] = {
    val txId = tempId("tx")
    stmtsModel.foldLeft(None: Option[AnyRef], Seq[Statement]()) { case ((otherEdgeId, stmts), stmt) =>
      stmt match {
        case Add('tempId, a, Values(vs, prefix), meta)                            => (otherEdgeId, valueStmts(stmts, tempId(a), a, vs, prefix, meta, otherEdgeId))
        case Add('e, a, Values(vs, prefix), meta) if meta.startsWith("edgeRef")   => val id = Some(tempId(a)); (id, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, meta, id))
        case Add('e, a, Values(vs, prefix), meta) if meta.startsWith("targetRef") => (None, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, meta, otherEdgeId))
        case Add('e, a, Values(vs, prefix), meta)                                 => (otherEdgeId, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, meta, otherEdgeId))
        case Add('e, a, 'tempId, meta)                                            => (otherEdgeId, valueStmts(stmts, lastE(stmts, a), a, tempId(a), None, meta, otherEdgeId))
        case Add(id, a, 'tempId, meta)                                            => (otherEdgeId, valueStmts(stmts, id, a, tempId(a), None, meta, otherEdgeId))
        case Add('e, a, refNs: String, meta)                                      => (otherEdgeId, valueStmts(stmts, lastE(stmts, a), a, tempId(refNs), None, meta, otherEdgeId))
        case Add('v, a, Values(vs, prefix), meta)                                 => (otherEdgeId, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, meta, otherEdgeId))
        case Add('tx, a, Values(vs, prefix), meta)                                => (otherEdgeId, valueStmts(stmts, txId, a, vs, prefix, meta, otherEdgeId))
        case Retract(e, a, v, meta)                                               => (otherEdgeId, stmts)
        case Add(id: Long, a, Values(_, _), meta)                                 => sys.error(s"[Model2Transaction:saveStmts] With a given id `$id` please use `update` instead.")
        case Add(_, a, 'arg, meta)                                                => sys.error(s"[Model2Transaction:saveStmts] Attribute `$a` needs a value applied")
        case unexpected                                                           => sys.error("[Model2Transaction:saveStmts] Unexpected save statement: " + unexpected)
      }
    }._2
  }

  def updateStmts(): Seq[Statement] = {
    val (dataStmts0, txStmts0) = splitStmts()
    val dataStmts: Seq[Statement] = dataStmts0.foldLeft(0, Seq[Statement]()) {
      case ((i, stmts), stmt) =>
        val j = i + 1
        stmt match {
          case Add('e, a, Values(vs, prefix), meta)     => (j, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, meta))
          case Add('e, a, 'tempId, meta)                => (i, valueStmts(stmts, lastE(stmts, a), a, tempId(a), None, meta))
          case Add('v, a, Values(vs, prefix), meta)     => (j, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, meta))
          case Add('tx, a, Values(vs, prefix), meta)    => (j, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, meta))
          case Add(e, a, Values(vs, prefix), meta)      => (j, valueStmts(stmts, e, a, vs, prefix, meta))
          case Add(e, a, 'tempId, meta)                 => (j, valueStmts(stmts, e, a, tempId(a), None, meta))
          case Retract('e, a, Values(vs, prefix), meta) => (j, valueStmts(stmts, lastE(stmts, a), a, vs, prefix, meta))
          case Retract(e, a, Values(vs, prefix), meta)  => (j, valueStmts(stmts, e, a, vs, prefix, meta))
          case Add(_, a, 'arg, _)                       => sys.error(s"[Model2Transaction:updateStmts] Attribute `$a` needs a value applied")
          case unexpected                               => sys.error("[Model2Transaction:updateStmts] Unexpected update statement: " + unexpected)
        }
    }._2
    val txId = tempId("tx")
    val txStmts: Seq[Statement] = txStmts0.foldLeft(Seq[Statement]()) {
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