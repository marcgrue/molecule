package molecule.transform

import java.util.Date
import datomic._
import molecule.api.Entity
import molecule.ast.model._
import molecule.ast.transactionModel._
import molecule.facade.Conn
import molecule.transform.exception.Model2TransactionException
import molecule.util.{Debug, Helpers}
import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._


/** Model to transaction transformation.
  *
  * @see [[http://www.scalamolecule.org/dev/transformation/]]
  **/
case class Model2Transaction(conn: Conn, model: Model) extends Helpers {
  val x = Debug("Model2Transaction", 1, 51, false, 6)

  private def err(method: String, msg: String) = {
    throw new Model2TransactionException(s"[$method]  $msg")
  }

  val stmtsModel: Seq[Statement] = {

    def resolveTx(elements: Seq[Element]): Seq[Statement] = elements.foldLeft("tx": Any, Seq[Statement]()) {
      case (_, Atom(nsFull, name, _, _, VarValue, _, _, _)) => err("stmtsModel",
        s"Please apply transaction meta data directly to transaction attribute: `${nsFull.capitalize}.$name(<metadata>)`")
      case ((eSlot1, stmts1), element1)                                      => resolveElement(eSlot1, stmts1, element1)
    }._2

    def bi(gs: Seq[GenericValue], card: Int) = gs.collectFirst {
      case bi: Bidirectional => bi
    } getOrElse Card(card)

    def resolveElement(eSlot: Any, stmts: Seq[Statement], element: Element): (Any, Seq[Statement]) = (eSlot, element) match {
      // None
      case (eids@Eids(ids), Atom(nsFull, name, _, c, Fn("not", _), _, gs, _)) => (eids, stmts ++ ids.map(Retract(_, s":$nsFull/$name", Values(Eq(Nil)), bi(gs, c))))
      case (eid@Eid(id), Atom(nsFull, name, _, c, Fn("not", _), _, gs, _))    => (eid, stmts :+ Add(id, s":$nsFull/$name", Values(Eq(Nil)), bi(gs, c)))
      case (e, Atom(_, _, _, _, Fn("not", _), _, _, _))                       => (e, stmts)
      case (e, Atom(_, _, _, _, Eq(Seq(None)), _, _, _))                      => (e, stmts)

      // First
      case ("_", Generic(_, "e" | "e_", _, EntValue))            => ("__arg", stmts)
      case ("_", Generic(_, "e" | "e_", _, Eq(Seq(id: Long))))   => (Eid(id), stmts)
      case ("_", Generic(_, "e" | "e_", _, Eq(ids: Seq[_])))     => (Eids(ids), stmts)
      case ("_", Atom(nsFull, name, _, c, VarValue, _, gs, _))   => ("e", stmts :+ Add("__tempId", s":$nsFull/$name", "__arg", bi(gs, c)))
      case ("_", Atom(nsFull, name, _, c, value, prefix, gs, _)) => ("e", stmts :+ Add("__tempId", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("_", Bond(nsFull, refAttr, refNs, c, gs))            => ("v", stmts :+ Add("__tempId", s":$nsFull/$refAttr", s":$refNs", bi(gs, c)))

      case (e, Nested(Bond(nsFull, refAttr, _, c, gs), elements)) =>
        val parentId = if (e == "_") "parentId" else "e"
        val nested   = elements.foldLeft("v": Any, Seq[Statement]()) {
          case ((eSlot1, stmts1), element1) => resolveElement(eSlot1, stmts1, element1)
        }._2
        ("e", stmts :+ Add(parentId, s":$nsFull/$refAttr", nested, bi(gs, c)))

      case (_, Composite(elements)) =>
        // Mark the first stmt as having the shared entity for all composite sub molecules
        // Then we can go back and pick up that entity as base for the newt sub-molecule
        val associated = elements.foldLeft("ec": Any, Seq[Statement]()) {
          case ((eSlot1, stmts1), element1) => resolveElement(eSlot1, stmts1, element1)
        }._2
        ("ec", stmts ++ associated)

      case ("ec", Generic(_, "e" | "e_", _, EntValue))          => ("__arg", stmts)
      case ("ec", Generic(_, "e" | "e_", _, Eq(Seq(id: Long)))) => (Eid(id), stmts)
      case ("ec", Generic(_, "e" | "e_", _, Eq(ids: Seq[_])))   => (Eids(ids), stmts)

      // Entity ids applied to initial namespace
      case (eids@Eids(ids), Atom(nsFull, name, _, c, value@RetractValue(_), prefix, gs, _)) => (eids, stmts ++ ids.map(Retract(_, s":$nsFull/$name", Values(value, prefix), bi(gs, c))))
      case (eids@Eids(ids), Atom(nsFull, name, _, c, value, prefix, gs, _))                 => (eids, stmts ++ ids.map(Add(_, s":$nsFull/$name", Values(value, prefix), bi(gs, c))))
      case (Eids(ids), Bond(nsFull, refAttr, refNs, c, gs))                                 => ("v", stmts ++ ids.map(Add(_, s":$nsFull/$refAttr", "__tempId", bi(gs, c))))

      // Entity id applied to initial namespace
      case (eid@Eid(id), Atom(nsFull, name, _, c, value@RetractValue(_), prefix, gs, _)) => (eid, stmts :+ Retract(id, s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case (eid@Eid(id), Atom(nsFull, name, _, c, value, prefix, gs, _))                 => (eid, stmts :+ Add(id, s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case (Eid(id), Bond(nsFull, refAttr, refNs, c, gs))                                => ("v", stmts :+ Add(id, s":$nsFull/$refAttr", "__tempId", bi(gs, c)))


      // Same namespace
      case ("e", Atom(nsFull, name, _, c, value@RetractValue(_), prefix, gs, _))   => ("e", stmts :+ Retract("e", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("e", Atom(nsFull, name, _, c, VarValue, _, gs, _)) if name.last == '$' => ("e", stmts :+ Add("e", s":$nsFull/${name.init}", "__arg", bi(gs, c)))
      case ("e", Atom(nsFull, name, _, c, VarValue, _, gs, _))                     => ("e", stmts :+ Add("e", s":$nsFull/$name", "__arg", bi(gs, c)))
      case ("e", Atom(nsFull, name, _, c, value, prefix, gs, _))                   => ("e", stmts :+ Add("e", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("e", Bond(nsFull, refAttr, refNs, c, gs))                              => ("v", stmts :+ Add("e", s":$nsFull/$refAttr", s":$refNs", bi(gs, c)))

      // Same namespace - composite
      case ("ec", Atom(nsFull, name, _, c, value@RetractValue(_), prefix, gs, _))   => ("e", stmts :+ Retract("ec", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("ec", Atom(nsFull, name, _, c, VarValue, _, gs, _)) if name.last == '$' => ("e", stmts :+ Add("ec", s":$nsFull/${name.init}", "__arg", bi(gs, c)))
      case ("ec", Atom(nsFull, name, _, c, VarValue, _, gs, _))                     => ("e", stmts :+ Add("ec", s":$nsFull/$name", "__arg", bi(gs, c)))
      case ("ec", Atom(nsFull, name, _, c, value, prefix, gs, _))                   => ("e", stmts :+ Add("ec", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("ec", Bond(nsFull, refAttr, refNs, c, gs))                              => ("v", stmts :+ Add("ec", s":$nsFull/$refAttr", s":$refNs", bi(gs, c)))

      // Transaction annotations
      case ("_", TxMetaData(elements))     => ("e", stmts ++ resolveTx(elements))
      case ("e", TxMetaData(elements))     => ("e", stmts ++ resolveTx(elements))
      case (Eid(id), TxMetaData(elements)) => ("e", stmts ++ resolveTx(elements))

      // Continue with only transaction Atoms...
      case ("tx", Atom(nsFull, name, _, c, value, prefix, _, _)) if name.last == '_' || name.last == '$' => ("tx", stmts :+ Add("tx", s":$nsFull/${name.init}", Values(value, prefix), Card(c)))
      case ("tx", Atom(nsFull, name, _, c, value, prefix, _, _))                                         => ("tx", stmts :+ Add("tx", s":$nsFull/$name", Values(value, prefix), Card(c)))
      case ("tx", Bond(nsFull, refAttr, refNs, c, gs))                                                   => ("tx", stmts :+ Add("tx", s":$nsFull/$refAttr", s":$refNs", bi(gs, c)))
      case ("tx", other)                                                                                 =>
        err("stmtsModel", s"Transaction data can only have references and attributes with applied value:\nMODEL: $model \nFOUND: $other\nSTMTS: $stmts")

      // Next namespace
      case ("v", Atom(nsFull, name, _, c, VarValue, _, gs, _))   => ("e", stmts :+ Add("v", s":$nsFull/$name", "__arg", bi(gs, c)))
      case ("v", Atom(nsFull, name, _, c, value, prefix, gs, _)) => ("e", stmts :+ Add("v", s":$nsFull/$name", Values(value, prefix), bi(gs, c)))
      case ("v", Bond(nsFull, refAttr, _, c, gs))                => ("v", stmts :+ Add("v", s":$nsFull/$refAttr", "__tempId", bi(gs, c)))

      // Add one extra generic statement to receive the eid arg for the following statement to use
      // (we then discard that temporary statement from the value statements)
      case ("__arg", Atom(nsFull, name, _, c, VarValue, _, _, _))   => ("e", stmts :+ Add("remove_me", s":$nsFull/$name", "__arg", Card(c)) :+ Add("v", s":$nsFull/$name", "__arg", Card(c)))
      case ("__arg", Atom(nsFull, name, _, c, value, prefix, _, _)) => ("e", stmts :+ Add("remove_me", s":$nsFull/$name", "__arg", Card(c)) :+ Add("v", s":$nsFull/$name", Values(value, prefix), Card(c)))
      case ("__arg", Bond(nsFull, refAttr, _, c, gs))               => ("v", stmts :+ Add("__arg", s":$nsFull/$refAttr", "__tempId", bi(gs, c)))

      // BackRef
      case (_, ReBond(backRef)) => ("e", stmts :+ Add("nsFull", s":$backRef", "", NoValue))

      case (e, elem) => err("stmtsModel", s"Unexpected transformation:\nMODEL: $model \nPAIR: ($e, $elem)\nSTMTS: $stmts")
    }

    def replace$(elements: Seq[Element]): Seq[Element] = elements map {
      case a@Atom(_, attr, _, _, _, Some(enumPrefix), _, _)
        if attr.last == '$' && enumPrefix.init.last == '$'       => a.copy(attr = attr.init, enumPrefix = Some(enumPrefix.init.init + "/"))
      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '$' => a.copy(attr = attr.init)
      case b@Bond(_, attr, _, _, _) if attr.last == '$'          => b.copy(refAttr = attr.init)
      case Nested(ref, es)                                       => Nested(ref, replace$(es))
      case other                                                 => other
    }

    val model1 = Model(replace$(model.elements))
    //    x(50, model, model1)
    model1.elements.foldLeft("_": Any, Seq[Statement]()) {
      case ((eSlot, stmts), element) => resolveElement(eSlot, stmts, element)
    }._2
  }

  private def tempId(attr: String): AnyRef = attr match {
    case null                 => err("__tempId", "Attribute name unexpectedly null.")
    case "tx"                 => Peer.tempid(s":db.part/tx")
    case s if s.contains('_') => Peer.tempid(s":" + attr.substring(1).split("(?=_)").head) // extract "partition" from ":partition_Namespace/attr"
    case _                    => Peer.tempid(s":db.part/user")
  }

  // Lookup if key is already populated
  def pairStrs(e: Any, a: String, key: String) = {
    val query = s"[:find ?v :in $$ ?e ?a ?key :where [?e ?a ?v][(.startsWith ^String ?v ?key)]]"
    Peer.q(query, conn.db, e.asInstanceOf[Object], a.asInstanceOf[Object], key.asInstanceOf[Object]).asScala.map(_.get(0))
  }

  def getPairs(e: Any, a: String, key: String = "") = {
    val strs = if (key.isEmpty) {
      val query  = s"[:find ?v :in $$ ?e ?a :where [?e ?a ?v]]"
      val result = Peer.q(query, conn.db, e.asInstanceOf[Object], a.asInstanceOf[Object]).asScala.map(_.get(0))
      result
    } else {
      val query  = s"[:find ?v :in $$ ?e ?a ?key :where [?e ?a ?v][(.startsWith ^String ?v ?key)]]"
      val result = Peer.q(query, conn.db, e.asInstanceOf[Object], a.asInstanceOf[Object], key.asInstanceOf[Object]).asScala.map(_.get(0))
      result
    }
    strs.map {
      case str: String =>
        val Seq(key: String, value: String) = str.split("@", 2).toSeq
        key -> value
    }.toMap
  }

  private def valueStmts(stmts: Seq[Statement],
                         e: Any,
                         a: String,
                         arg: Any,
                         prefix: Option[String],
                         bidirectional: GenericValue,
                         otherEdgeId: Option[AnyRef]): Seq[Statement] = {

    def p(v: Any): Any = v match {
      case f: Float              => f.toString.toDouble
      case _ if prefix.isDefined => prefix.get + v
      case _                     => v
    }

    def d(v: Any) = v match {
      case d: Date => date2str(d)
      case other   => other
    }

    def attrValues(id: Any, attr: String) = {
      val query = if (prefix.isDefined)
        s"""[:find ?enums
           | :in $$ ?id
           | :where [?id $attr ?a]
           |        [?a :db/ident ?b]
           |        [(name ?b) ?enums]
           |]""".stripMargin
      else
        s"[:find ?values :in $$ ?id :where [?id $attr ?values]]"

      Peer.q(query, conn.db, id.asInstanceOf[Object]).asScala.map(_.get(0))
    }

    def edgeAB(edge1: Any, targetAttr: String): (Any, Any) = {
      val edge2 = otherEdge(edge1)

      // Edges to other ns already has one ref extra in one direction
      val s1 = conn.db.entity(edge1).keySet().size()
      val s2 = conn.db.entity(edge2).keySet().size()

      if (s1 > s2)
        (edge1, edge2)
      else if (s1 < s2)
        (edge2, edge1)
      else if (conn.db.entity(edge1).get(targetAttr) == null)
        (edge2, edge1)
      else
        (edge1, edge2)
    }

    def otherEdge(edgeA: Any): AnyRef = {
      val query  = s"[:find ?edgeB :in $$ ?edgeA :where [?edgeA :molecule_Meta/otherEdge ?edgeB]]"
      val result = Peer.q(query, conn.db, edgeA.asInstanceOf[Object]).asScala.map(_.get(0))
      result match {
        case ArrayBuffer(edgeB) => edgeB.asInstanceOf[Object]
        case Nil        =>
          val otherId = Entity(conn.db.entity(edgeA), conn, edgeA.asInstanceOf[Object])
          err("valueStmts:biEdgeRef", s"Supplied id $edgeA doesn't appear to be a property edge id (couldn't find reverse edge id). " +
            s"Could it be another entity?:\n" + otherId.touchQuotedMax(2) +
            s"\nSpooky id: $otherId" +
            "\n" + stmts.size + " statements so far:\n" + stmts.mkString("\n")
          )
        case ids        =>
          err("valueStmts:biEdgeRef", "Unexpectedly found multiple reverse edge ids:\n" + ids.mkString("\n"))
      }
    }

    def checkDupKeys(pairs: Seq[(String, Any)], host: String, op: String): Unit = {
      val dupKeys: Seq[String] = pairs.map(_._1).groupBy(identity).collect { case (key, keys) if keys.size > 1 => key }.toSeq
      if (dupKeys.nonEmpty) {
        val dupPairs = pairs.filter(p => dupKeys.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
        err(s"valueStmts:$host", s"Can't $op multiple key/value pairs with the same key for attribute `$a`:\n" + dupPairs.mkString("\n"))
      }
    }

    def checkDupValuesOfPairs(pairs: Seq[(Any, Any)], host: String, op: String): Unit = {
      val dups = pairs.map(_._2).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }
      if (dups.nonEmpty)
        err(s"valueStmts:$host", s"Can't replace with duplicate new values of attribute `$a`:\n" + dups.mkString("\n"))
    }

    def checkDupValues(values: Seq[Any], host: String, op: String): Unit = {
      val dups = values.groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }
      if (dups.nonEmpty)
        err(s"valueStmts:$host", s"Can't $op duplicate new values to attribute `$a`:\n" + dups.mkString("\n"))
    }

    def checkUnknownKeys(newPairs: Seq[(String, Any)], curKeys: Seq[String], host: String): Unit = {
      val unknownKeys = newPairs.map(_._1).diff(curKeys)
      if (unknownKeys.nonEmpty)
        err(s"valueStmts:$host", s"Can't replace non-existing keys of map attribute `$a`:\n" + unknownKeys.mkString("\n") +
          "\nYou might want to apply the values instead to replace all current values?")
    }


    // Bidirectional self-ref operations ------------------------------------------------------------------

    def biSelf(card: Int): Iterable[Statement] = arg match {

      case AssertValue(refs) => refs.flatMap { case ref: Long =>
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        if (ref == e) err("valueStmts:biSelfRef", "Current entity and referenced entity ids can't be the same")
        reverseRetracts ++ Seq(Add(ref, a, e, Card(card)), Add(e, a, ref, Card(card)))
      }

      case ReplaceValue(oldNew) => attrValues(e, a).toSeq.flatMap { case revRef =>
        oldNew.flatMap {
          case (oldRef, newRef) if oldRef == revRef => Seq(
            // This entity e now has ref to newRef instead of oldRef
            Retract(e, a, oldRef), Add(e, a, newRef, Card(card)),
            // RevRef no longer has a ref to this entity e
            // Instead newRef has a ref to this entity e
            Retract(revRef, a, e), Add(newRef, a, e, Card(card))
          )
          case _                                    => Nil
        }
      }

      case RetractValue(removeRefs) => removeRefs.flatMap { case ref: Long => Seq(Retract(ref, a, e), Retract(e, a, ref)) }

      case Eq(newRefs) => {
        if (newRefs.contains(e)) err("valueStmts:biSelfRef", "Current entity and referenced entity ids can't be the same.")
        val oldRefs  = attrValues(e, a).toSeq
        val retracts = oldRefs.flatMap {
          case oldRef if newRefs.contains(oldRef) => Nil
          case obsoleteRef                        => Seq(Retract(obsoleteRef, a, e), Retract(e, a, obsoleteRef))
        }
        val adds     = newRefs.flatMap {
          case newRef if oldRefs.contains(newRef) => Nil
          case newRef                             => Seq(Add(newRef, a, e, Card(card)), Add(e, a, newRef, Card(card)))
        }
        retracts ++ adds
      }

      case refs: Set[_] => refs.flatMap { case ref: Long => Seq(Add(ref, a, e, Card(card)), Add(e, a, ref, Card(card))) }
      case ref          => {
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        reverseRetracts ++ Seq(Add(ref, a, e, Card(card)), Add(e, a, ref, Card(card)))
      }
    }


    // Bidirectional other-ref operations ------------------------------------------------------------------

    def biOther(card: Int, revRefAttr: String): Iterable[Statement] = arg match {

      case AssertValue(refs) => refs.flatMap { case ref: Long =>
        if (ref == e) err("valueStmts:biOther", "Current entity and referenced entity ids can't be the same")
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(revRef => Retract(revRef, a, e)) else Nil
        reverseRetracts ++ Seq(Add(ref, revRefAttr, e, Card(card)), Add(e, a, ref, Card(card)))
      }

      case ReplaceValue(oldNew) => attrValues(e, a).toSeq.flatMap { case revRef =>
        oldNew.flatMap {
          case (oldRef, newRef) if oldRef == revRef => Seq(
            // This entity e now has ref to newRef instead of oldRef
            Retract(e, a, oldRef), Add(e, a, newRef, Card(card)),
            // RevRef no longer has a ref to this entity e
            // Instead newRef has a ref to this entity e
            Retract(revRef, revRefAttr, e, Card(card)), Add(newRef, revRefAttr, e, Card(card))
          )
          case _                                    => Nil
        }
      }

      case RetractValue(removeRefs) => removeRefs.flatMap { case ref: Long => Seq(Retract(ref, revRefAttr, e), Retract(e, a, ref)) }

      case Eq(newRefs) => {
        if (newRefs.contains(e)) err("valueStmts:biSelfRef", "Current entity and referenced entity ids can't be the same.")
        val oldRefs  = attrValues(e, a).toSeq
        val retracts = oldRefs.flatMap {
          case oldRef if newRefs.contains(oldRef) => Nil
          case obsoleteRef                        => Seq(Retract(obsoleteRef, revRefAttr, e), Retract(e, a, obsoleteRef))
        }
        val adds     = newRefs.flatMap {
          case newRef if oldRefs.contains(newRef) => Nil
          case newRef                             => Seq(Add(newRef, revRefAttr, e, Card(card)), Add(e, a, newRef, Card(card)))
        }
        retracts ++ adds
      }

      case refs: Set[_] => refs.flatMap { case ref: Long => Seq(Add(ref, revRefAttr, e, Card(card)), Add(e, a, ref, Card(card))) }
      case ref          => {
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(revRef => Retract(revRef, revRefAttr, e)) else Nil
        reverseRetracts ++ Seq(Add(ref, revRefAttr, e, Card(card)), Add(e, a, ref, Card(card)))
      }
    }


    // Bidirectional edge operations -------------------------------------------------------------------

    def biEdgeRefAttr(card: Int, targetAttr: String): Iterable[Statement] = arg match {

      case AssertValue(edges) =>
        if (edges.contains(e))
          err("valueStmts:biEdgeRefAttr", "Current entity and referenced entity ids can't be the same.")
        checkDupValues(edges, "biEdgeRefAttr", "assert")
        edges.flatMap { edge =>
          val (edgeA, edgeB) = edgeAB(edge, targetAttr)
          Seq(Add(edgeB, targetAttr, e, Card(card)), Add(e, a, edgeA, Card(card)))
        }

      case ReplaceValue(oldNewEdges) =>
        checkDupValuesOfPairs(oldNewEdges, "biEdgeRefAttr", "replace")
        oldNewEdges.flatMap { case (oldEdge, newEdge) =>
          val (edgeA, edgeB) = edgeAB(newEdge, targetAttr)
          Seq(
            RetractEntity(oldEdge),
            Add(edgeB, targetAttr, e, Card(card)),
            Add(e, a, edgeA, Card(card))
          )
        }

      case RetractValue(edges) => edges map RetractEntity

      case Eq(newEdges) =>
        if (newEdges.contains(e))
          err("valueStmts:biEdgeRefAttr", "Current entity and referenced entity ids can't be the same.")
        checkDupValues(newEdges, "biEdgeRefAttr", "apply")
        val oldEdges = attrValues(e, a).toSeq
        val retracts = oldEdges.flatMap {
          case oldEdgeA if newEdges.contains(oldEdgeA) => Nil
          case oldEdgeA                                => Seq(RetractEntity(oldEdgeA))
        }
        val adds     = newEdges.flatMap {
          case edge if oldEdges.contains(edge) => Nil
          case edge                            =>
            val (edgeA, edgeB) = edgeAB(edge, targetAttr)
            Seq(
              Add(edgeB, targetAttr, e, Card(card)),
              Add(e, a, edgeA, Card(card))
            )
        }
        retracts ++ adds

      case edges: Set[_] if card == 2 => edges.flatMap { case edge: Long =>
        val (edgeA, edgeB) = edgeAB(edge, targetAttr)
        Seq(Add(edgeB, targetAttr, e, Card(card)), Add(e, a, edgeA, Card(card)))
      }

      case edge: Long =>
        val (edgeA, edgeB)  = edgeAB(edge, targetAttr)
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(oldEdgeA => RetractEntity(oldEdgeA)) else Nil
        reverseRetracts ++ Seq(Add(edgeB, targetAttr, e, Card(card)), Add(e, a, edgeA, Card(card)))
    }


    def biEdgeRef(card: Int, targetAttr: String): Seq[Statement] = arg match {

      case RetractValue(edges) => edges map RetractEntity

      case Eq(newEdges) =>
        if (newEdges.contains(e))
          err("valueStmts:biSelfRef", "Current entity and referenced entity ids can't be the same.")
        checkDupValues(newEdges, "biEdgeRef", "apply")
        val oldEdges = attrValues(e, a).toSeq
        val retracts = oldEdges.flatMap {
          case oldEdgeA if newEdges.contains(oldEdgeA) => Nil
          case oldEdgeA                                => Seq(RetractEntity(oldEdgeA))
        }
        val adds     = newEdges.flatMap {
          case edge if oldEdges.contains(edge) => Nil
          case edge                            =>
            val (edgeA, edgeB) = edgeAB(edge, targetAttr)
            Seq(
              Add(edgeA, ":molecule_Meta/otherEdge", edgeB, Card(card)),
              Add(edgeB, ":molecule_Meta/otherEdge", edgeA, Card(card)),
              Add(edgeB, targetAttr, e, Card(card)),
              Add(e, a, edgeA, Card(card))
            )
        }
        retracts ++ adds

      case edgeA => {
        val edgeB           = otherEdgeId getOrElse err("valueStmts:biEdgeRef", "Missing id of other edge.")
        val reverseRetracts = if (card == 1) attrValues(e, a).toSeq.map(oldEdgeA => RetractEntity(oldEdgeA)) else Nil
        reverseRetracts ++ Seq(
          // Interlink edge entities so that we later know which other one to update
          Add(edgeA, ":molecule_Meta/otherEdge", edgeB, Card(card)),
          Add(edgeB, ":molecule_Meta/otherEdge", edgeA, Card(card)),
          Add(edgeB, targetAttr, e, Card(card)),
          Add(e, a, edgeA, Card(card))
        )
      }
    }


    def biEdgeProp(card: Int): Seq[Statement] = {

      val edgeA = e
      val edgeB = otherEdgeId match {
        case Some(eid) if eid == edgeA => err("valueStmts:biEdgeProp", "Other edge id is unexpectedly the same as this edge id.")
        case Some(eid)                 => eid
        case None                      => otherEdge(edgeA)
      }

      val stmt = arg match {

        case AssertMapPairs(newPairs) =>
          checkDupKeys(newPairs, "biEdgeProp", "assert")
          //          val curPairs: Map[(String, String), String] = getPairs(edgeA, a)
          val curPairs = getPairs(edgeA, a)
          val curKeys  = curPairs.keys
          newPairs.flatMap {
            case (k, v) if curPairs.asJavaCollection.contains((k, d(v).toString)) => Nil
            case (k, v) if curKeys.asJavaCollection.contains(k)                   => Seq(
              Retract(edgeB, a, k + "@" + curPairs(k)), Add(edgeB, a, k + "@" + d(v), Card(card)),
              Retract(edgeA, a, k + "@" + curPairs(k)), Add(edgeA, a, k + "@" + d(v), Card(card))
            )
            case (k, v)                                                           => Seq(
              Add(edgeB, a, k + "@" + d(v), Card(card)),
              Add(edgeA, a, k + "@" + d(v), Card(card))
            )
          }

        case ReplaceMapPairs(newPairs) =>
          checkDupKeys(newPairs, "biEdgeProp", "replace")
          val curPairs = getPairs(edgeA, a)
          val curKeys  = curPairs.keys
          checkUnknownKeys(newPairs, curKeys.toSeq, "biEdgeProp")
          newPairs.flatMap {
            case (k, v) if curPairs.asJavaCollection.contains((k, d(v).toString)) => Nil
            case (k, v) if curKeys.asJavaCollection.contains(k)                   => Seq(
              Retract(edgeB, a, k + "@" + curPairs(k)), Add(edgeB, a, k + "@" + d(v), Card(card)),
              Retract(edgeA, a, k + "@" + curPairs(k)), Add(edgeA, a, k + "@" + d(v), Card(card))
            )
            case (k, v)                                                           => Seq(
              Add(edgeB, a, k + "@" + d(v), Card(card)),
              Add(edgeA, a, k + "@" + d(v), Card(card))
            )
          }

        case RetractMapKeys(removeKeys0) =>
          val removeKeys = removeKeys0.distinct
          val oldPairs   = getPairs(edgeA, a)
          oldPairs.flatMap {
            case (oldK, oldV) if removeKeys.contains(oldK) => Seq(Retract(edgeB, a, s"$oldK@$oldV"), Retract(edgeA, a, s"$oldK@$oldV"))
            case (oldK, oldV)                              => Nil
          }

        case MapEq(newPairs) =>
          checkDupKeys(newPairs, "biEdgeProp", "apply")
          edgeA match {
            case updateE: Long => {
              val oldPairs  = getPairs(edgeA, a)
              val newPairs1 = newPairs.map { case (k, v) => (k, d(v).toString) }
              val retracts  = oldPairs.flatMap {
                case (oldK, oldV) if newPairs1.contains((oldK, oldV)) => Nil
                case (oldK, oldV)                                     => Seq(Retract(edgeB, a, s"$oldK@$oldV"), Retract(edgeA, a, s"$oldK@$oldV"))
              }
              val adds      = newPairs.flatMap { case (k, v) => Seq(
                Add(edgeB, a, k + "@" + d(v), Card(card)),
                Add(edgeA, a, k + "@" + d(v), Card(card))
              )
              }
              retracts ++ adds
            }
            case newE          =>
              newPairs.flatMap { case (k, v) => Seq(
                Add(edgeB, a, k + "@" + d(v), Card(card)),
                Add(edgeA, a, k + "@" + d(v), Card(card))
              )
              }
          }

        case AssertValue(values) =>
          values.distinct.flatMap(v => Seq(Add(edgeB, a, p(v), Card(card)), Add(edgeA, a, p(v), Card(card))))

        case ReplaceValue(oldNew) =>
          checkDupValuesOfPairs(oldNew, "biEdgeProp", "replace")
          oldNew.flatMap { case (oldValue, newValue) => Seq(
            Retract(edgeB, a, p(oldValue)), Add(edgeB, a, p(newValue), Card(card)),
            Retract(edgeA, a, p(oldValue)), Add(edgeA, a, p(newValue), Card(card)))
          }

        case RetractValue(removeValues) =>
          removeValues.distinct.flatMap(v => Seq(Retract(edgeB, a, p(v)), Retract(edgeA, a, p(v))))

        case Eq(newValues) =>
          if (newValues.contains(e))
            err("valueStmts:biSelfRef", "Current entity and referenced entity ids can't be the same.")
          val curValues       = attrValues(edgeA, a).toSeq
          val newValueStrings = newValues.map(_.toString)
          val curValueStrings = curValues.map(_.toString)
          val retracts        = curValues.flatMap {
            case curValue if newValueStrings.contains(curValue.toString) => Nil
            case obsoleteValue                                           => Seq(Retract(edgeB, a, p(obsoleteValue)), Retract(edgeA, a, p(obsoleteValue)))
          }
          val adds            = newValues.flatMap {
            case newValue if curValueStrings.contains(newValue.toString) => Nil
            case newValue                                                => Seq(
              Add(edgeB, a, p(newValue), Card(card)),
              Add(edgeA, a, p(newValue), Card(card))
            )
          }
          retracts ++ adds

        case m: Map[_, _] => m.flatMap { case (k, v) => Seq(Add(edgeB, a, s"$k@${d(v)}", Card(card)), Add(edgeA, a, s"$k@${d(v)}", Card(card))) }
        case vs: Set[_]   => vs.toSeq.flatMap(v => Seq(Add(edgeB, a, p(v), Card(card)), Add(edgeA, a, p(v), Card(card))))
        case v :: Nil     => Seq(Add(edgeB, a, p(v), Card(card)), Add(edgeA, a, p(v), Card(card)))
        case vs: List[_]  => vs.flatMap(v => Seq(Add(edgeB, a, p(v), Card(card)), Add(edgeA, a, p(v), Card(card))))
        case v            =>
          Seq(Add(edgeB, a, p(v), Card(card)), Add(edgeA, a, p(v), Card(card)))
      }

      val edgeConnectors: Seq[Add] = stmts.collectFirst {
        case Add(_, ":molecule_Meta/otherEdge", _, _) => Nil
      } getOrElse Seq(
        Add(edgeA, ":molecule_Meta/otherEdge", edgeB, Card(card)),
        Add(edgeB, ":molecule_Meta/otherEdge", edgeA, Card(card))
      )
      edgeConnectors ++ stmt
    }

    def biTarget(card: Int, biEdgeRefAttr: String): Seq[Add] = {
      val edgeA = e
      val edgeB = otherEdgeId getOrElse err("valueStmts:biTargetRef", "Missing id of other edge.")
      arg match {
        case Eq(biTargetRef :: Nil) => Seq(
          Add(biTargetRef, biEdgeRefAttr, edgeB, Card(card)),
          Add(edgeA, a, biTargetRef, Card(card))
        )
        case biTargetRef            => Seq(
          Add(biTargetRef, biEdgeRefAttr, edgeB, Card(card)),
          Add(edgeA, a, biTargetRef, Card(card))
        )
      }
    }

    // Default operations -------------------------------------------------------------------

    def default(card: Int): Iterable[Statement] = arg match {

      case AssertMapPairs(newPairs) =>
        checkDupKeys(newPairs, "default", "assert")
        val curPairs = getPairs(e, a)
        val curKeys  = curPairs.keys
        newPairs.flatMap {
          case (k, v) if curPairs.asJavaCollection.contains((k, d(v).toString)) => Nil
          case (k, v) if curKeys.asJavaCollection.contains(k)                   => Seq(Retract(e, a, s"$k@${curPairs(k)}"), Add(e, a, s"$k@${d(v)}", Card(card)))
          case (k, v)                                                           => Seq(Add(e, a, s"$k@${d(v)}", Card(card)))
        }

      case ReplaceMapPairs(newPairs) =>
        checkDupKeys(newPairs, "default", "replace")
        val curPairs = getPairs(e, a)
        val curKeys  = curPairs.keys.toSeq
        checkUnknownKeys(newPairs, curKeys, "default")
        newPairs.flatMap {
          case (k, v) if curPairs.asJavaCollection.contains((k, d(v).toString)) => Nil
          case (k, v) if curKeys.asJavaCollection.contains(k)                   => Seq(Retract(e, a, s"$k@${curPairs(k)}"), Add(e, a, s"$k@${d(v)}", Card(card)))
          case (k, v)                                                           => Seq(Add(e, a, s"$k@${d(v)}", Card(card)))
        }

      case RetractMapKeys(removeKeys0) =>
        val removeKeys = removeKeys0.distinct
        val oldPairs   = getPairs(e, a)
        oldPairs.flatMap {
          case (oldK, oldV) if removeKeys.contains(oldK) => Seq(Retract(e, a, s"$oldK@$oldV"))
          case (oldK, oldV)                              => Nil
        }

      case MapEq(newPairs) =>
        checkDupKeys(newPairs, "default", "apply")
        e match {
          case updateE: Long => {
            val oldPairs  = getPairs(e, a)
            val newPairs1 = newPairs.map { case (k, v) => (k, d(v).toString) }
            val retracts  = oldPairs.flatMap {
              case (oldK, oldV) if newPairs1.contains((oldK, oldV)) => Nil
              case (oldK, oldV)                                     => Seq(Retract(e, a, s"$oldK@$oldV"))
            }
            val adds      = newPairs.map { case (k, v) => Add(e, a, k + "@" + d(v), Card(card)) }
            retracts ++ adds
          }
          case newE          =>
            newPairs.map { case (k, v) => Add(e, a, k + "@" + d(v), Card(card)) }
        }

      case AssertValue(values0) =>
        val values = flatten(values0)
        values.distinct.map(v => Add(e, a, p(v), Card(card)))

      case ReplaceValue(oldNew) =>
        checkDupValuesOfPairs(oldNew, "default", "replace")
        oldNew.flatMap { case (oldValue, newValue) => Seq(Retract(e, a, p(oldValue)), Add(e, a, p(newValue), Card(card))) }

      case RetractValue(removeValues0) =>
        flatten(removeValues0).distinct.map(v => Retract(e, a, p(v)))

      case Eq(newValues0) =>
        val newValues       = flatten(newValues0).distinct
        val curValues       = if (e == "datomic.tx") Nil else attrValues(e, a).toSeq
        val newValueStrings = newValues.map(_.toString)
        val curValueStrings = curValues.map(_.toString)
        val retracts        = if (card == 2 || (newValues.isEmpty && curValues.nonEmpty))
          curValues.flatMap {
            case curValue if newValueStrings.contains(curValue.toString) => Nil
            case obsoleteValue                                           => Seq(Retract(e, a, p(obsoleteValue)))
          }
        else
          Nil
        val adds            = newValues.flatMap {
          case newValue if curValueStrings.contains(newValue.toString) => Nil
          case newValue                                                => Seq(Add(e, a, p(newValue), Card(card)))
        }
        retracts ++ adds

      case m: Map[_, _] => m.map { case (k, v) => Add(e, a, s"$k@${d(v)}", Card(card)) }
      case vs: Set[_]   => vs.map(v => Add(e, a, p(v), Card(card)))
      case v :: Nil     => Seq(Add(e, a, p(v), Card(card)))
      case vs: List[_]  => vs.map(v => Add(e, a, p(v), Card(card)))
      case v            => Seq(Add(e, a, p(v), Card(card)))
    }

    val newStmts = bidirectional match {
      case BiSelfRef(card)             => biSelf(card)
      case BiSelfRefAttr(card)         => biSelf(card)
      case BiOtherRef(card, attr)      => biOther(card, attr)
      case BiOtherRefAttr(card, attr)  => biOther(card, attr)
      case BiEdgeRefAttr(card, attr)   => biEdgeRefAttr(card, attr)
      case BiEdgeRef(card, attr)       => biEdgeRef(card, attr)
      case BiEdgePropAttr(card)        => biEdgeProp(card)
      case BiEdgePropRefAttr(card)     => biEdgeProp(card)
      case BiEdgePropRef(card)         => biEdgeProp(card)
      case BiTargetRef(card, attr)     => biTarget(card, attr)
      case BiTargetRefAttr(card, attr) => biTarget(card, attr)
      case Card(card)                  => default(card)
      case other                       => throw new Model2TransactionException(
        s"""Unexpected or missing Generic `$other`:
           |e  : $e
           |a  : $a
           |arg: $arg
         """.stripMargin)
    }

    stmts ++ newStmts
  }

  def flatten(vs: Seq[Any]): Seq[Any] = vs.flatMap {
    case set: Set[_] => set.toSeq
    case v           => List(v)
  }

  def splitStmts(): (Seq[Statement], Seq[Statement]) = stmtsModel.foldLeft(Seq.empty[Statement], Seq.empty[Statement]) {
    case ((stmts, txStmts), txStmt@Add("tx", _, _, _)) => (stmts, txStmts :+ txStmt)
    case ((stmts, txStmts), stmt)                      => (stmts :+ stmt, txStmts)
  }

  def lastE(stmts: Seq[Statement], attr: String, nestedE: Any, bi: GenericValue, composite: Any = "e"): Any = {
    bi match {
      case BiTargetRef(_, _) => {
        val lastEdgeNs = attr.split("/").head
        stmts.reverse.collectFirst {
          case Add(e: db.DbId, a, _, _) if a.startsWith(lastEdgeNs) => e
        } getOrElse err("lastE", s"Couldn't find namespace `$lastEdgeNs` in any previous Add statements:\n" + stmts.mkString("\n"))
      }
      case _                 => {
        if (nestedE != 0)
          nestedE
        else if (stmts.isEmpty)
          tempId(attr)
        else if (stmts.last.e == -1 || stmts.last.e.toString.startsWith("#db/id[:db.part/tx") && stmts.last.v.toString.startsWith("#db/id[:db.part/"))
          stmts.last.v
        else if (composite == "ec")
          stmts.head.e
        else
          stmts.last.e
      }
    }
  }

  def lastV(stmts: Seq[Statement], attr: String, nestedE: Any = 0): Any = {
    if (nestedE != 0)
      nestedE
    else if (stmts.isEmpty)
      tempId(attr)
    else
      stmts.last.v
  }

  def eidV(stmts: Seq[Statement]): Boolean = stmts.nonEmpty && stmts.last.v.isInstanceOf[db.DbId]

  def matchDataStmt(stmts: Seq[Statement], genericStmt: Statement, arg: Any, cur: Int, next: Int, nestedE: Any, edgeB: Option[AnyRef])
  : (Int, Option[AnyRef], Seq[Statement]) = genericStmt match {

    // Keep current cursor (add no new data in this iteration)
    case Add("__tempId", a, refNs: String, bi) if !refNs.startsWith("__")                       => (cur, edgeB, valueStmts(stmts, tempId(a), a, tempId(refNs), None, bi, edgeB))
    case Add(e@("e" | "ec"), a, refNs: String, bi@BiSelfRef(1)) if !refNs.startsWith("__")      => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, tempId(refNs), None, bi, edgeB))
    case Add(e@("e" | "ec"), a, refNs: String, bi@BiSelfRef(2)) if !refNs.startsWith("__")      => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, tempId(refNs), None, bi, edgeB))
    case Add(e@("e" | "ec"), a, refNs: String, bi@BiEdgeRef(_, _)) if !refNs.startsWith("__")   => val edgeB1 = Some(tempId(a)); (cur, edgeB1, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, tempId(refNs), None, bi, edgeB1))
    case Add(e@("e" | "ec"), a, refNs: String, bi@BiTargetRef(_, _)) if !refNs.startsWith("__") => (cur, None, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, tempId(refNs), None, bi, edgeB))
    case Add(e@("e" | "ec"), a, refNs: String, bi) if !refNs.startsWith("__")                   => (cur, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, tempId(refNs), None, bi, edgeB))
    case Add("v", a, refNs: String, bi) if !refNs.startsWith("__")                              => (cur, edgeB, valueStmts(stmts, stmts.last.v, a, tempId(refNs), None, bi, edgeB))
    case Add("v", a, "__tempId", bi) if nestedE != 0                                            => (cur, edgeB, valueStmts(stmts, nestedE, a, tempId(a), None, bi, edgeB))
    case Add("v", a, "__tempId", bi)                                                            => (cur, edgeB, valueStmts(stmts, stmts.last.v, a, tempId(a), None, bi, edgeB))
    case r: Retract                                                                             => (cur, edgeB, stmts)

    // Advance cursor for next value in data row
    case Add("__tempId", a, "__arg", bi@BiEdgePropAttr(_))                 => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, arg, None, bi, edgeB1))
    case Add("__tempId", a, "__arg", bi@BiTargetRefAttr(_, _))             => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, arg, None, bi, edgeB1))
    case Add("__tempId", a, Values(EnumVal, pf), bi@BiEdgePropAttr(_))     => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, arg, pf, bi, edgeB1))
    case Add("__tempId", a, Values(EnumVal, pf), bi@BiTargetRefAttr(_, _)) => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, arg, pf, bi, edgeB1))
    case Add("__tempId", a, Values(vs, pf), bi@BiEdgePropAttr(_))          => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, vs, pf, bi, edgeB1))
    case Add("__tempId", a, Values(vs, pf), bi@BiTargetRefAttr(_, _))      => val edgeB1 = Some(tempId(a)); (next, edgeB1, valueStmts(stmts, tempId(a), a, vs, pf, bi, edgeB1))
    case Add("__tempId", a, "__arg", bi)                                   => (next, edgeB, valueStmts(stmts, tempId(a), a, arg, None, bi, edgeB))
    case Add("__tempId", a, Values(EnumVal, pf), bi)                       => (next, edgeB, valueStmts(stmts, tempId(a), a, arg, pf, bi, edgeB))
    case Add("__tempId", a, Values(vs, pf), bi)                            => (next, edgeB, valueStmts(stmts, tempId(a), a, vs, pf, bi, edgeB))
    case Add("remove_me", a, "__arg", bi)                                  => (next, edgeB, valueStmts(stmts, -1, a, arg, None, bi, edgeB))
    case Add("__arg", a, "__tempId", bi)                                   => (next, edgeB, valueStmts(stmts, arg, a, tempId(a), None, bi, edgeB))
    case Add(e@("e" | "ec"), a, "__arg", bi)                               => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, arg, None, bi, edgeB))
    case Add(e@("e" | "ec"), a, Values(EnumVal, prefix), bi)               => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, arg, prefix, bi, edgeB))
    case Add(e@("e" | "ec"), a, Values(vs, prefix), bi)                    => (next, edgeB, valueStmts(stmts, lastE(stmts, a, nestedE, bi, e), a, vs, prefix, bi, edgeB))
    case Add("v", a, "__arg", bi) if eidV(stmts)                           => (next, edgeB, valueStmts(stmts, stmts.last.v, a, arg, None, bi, edgeB))
    case Add("v", a, "__arg", bi)                                          => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg, None, bi, edgeB))
    case Add("v", a, Values(EnumVal, prefix), bi)                          => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, arg, prefix, bi, edgeB))
    case Add("v", a, Values(vs, prefix), bi)                               => (next, edgeB, valueStmts(stmts, lastV(stmts, a, nestedE), a, vs, prefix, bi, edgeB))
    case Add("tx", a, "__arg", bi)                                         => (next, edgeB, valueStmts(stmts, tempId("tx"), a, arg, None, bi, edgeB))
    case Add(_, _, nestedGenStmts0: Seq[_], _) if arg == Nil               => (next, edgeB, stmts)

    // Nested data structures
    case Add(e, ref, nestedGenStmts0: Seq[_], bi) => {
      val parentE = if (e == "parentId")
        tempId(ref)
      else if (stmts.isEmpty)
        e
      else
        stmts.reverse.collectFirst {
          // Find entity value of Add statement with matching namespace
          case add: Add if eidV(stmts)                                                      => stmts.last.v
          case Add(e1, a, _, _) if a.replaceFirst("/.*", "") == ref.replaceFirst("/.*", "") => e1
        }.getOrElse(err("", "[Model2Transaction:matchDataStmt] Couldn't find previous statement with matching namespace." +
          "\ne  : " + e +
          "\nref: " + ref))

      val nestedGenStmts = nestedGenStmts0.map { case s: Statement => s }
      val nestedRows     = untupleNestedArgss(nestedGenStmts0, arg)

      val nestedInsertStmts: Seq[Statement] = bi match {

        // Bidirectional self references ...........................................................

        // Nested self references
        // living_Person.name.Friends.*(living_Person.name) insert List(...
        case BiSelfRef(2) => nestedRows.flatMap { nestedRow =>
          val nestedE     = tempId(ref)
          val bondStmts   = Seq(
            Add(nestedE, ref, parentE, Card(2)),
            Add(parentE, ref, nestedE, Card(2))
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE)
          bondStmts ++ nestedStmts
        }

        // Bidirectional other references ...........................................................

        // Nested other references
        // living_Person.name.Buddies.*(living_Animal.name) insert List(...
        case BiOtherRef(2, revRefAttr) => nestedRows.flatMap { nestedRow =>
          val nestedE     = tempId(ref)
          val bondStmts   = Seq(
            Add(nestedE, revRefAttr, parentE, Card(2)),
            Add(parentE, ref, nestedE, Card(2))
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE)
          bondStmts ++ nestedStmts
        }


        // Bidirectional property edges ...........................................................

        // Nested property edges
        // living_Person.name.Knows.*(living_Knows.weight.Person.name) insert List(...
        case BiEdgeRef(2, revAttr) => nestedRows.flatMap { nestedRow =>
          val edgeA       = tempId(revAttr)
          val edgeB1      = tempId(ref)
          val bondStmts   = Seq(
            Add(edgeA, ":molecule_Meta/otherEdge", edgeB1, Card(2)),
            Add(edgeB1, ":molecule_Meta/otherEdge", edgeA, Card(2)),
            Add(edgeB1, revAttr, parentE, Card(2)),
            Add(parentE, ref, edgeA, Card(2))
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, edgeA, Some(edgeB1))
          bondStmts ++ nestedStmts
        }

        // Nested edge property values
        // living_Person.name.Knows.weight.InCommon.*(living_Quality.name)._Knows.Person.name insert List(...
        case BiEdgePropRef(2) => nestedRows.flatMap { nestedRow =>
          val edgeA       = tempId(ref)
          val bondStmts   = Seq(
            Add(edgeB.get, ref, edgeA, Card(2)),
            Add(parentE, ref, edgeA, Card(2))
          )
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, edgeA)
          bondStmts ++ nestedStmts
        }


        // Uni-directional (normal) nested rows ...............................................

        // Invoice.id.InvoiceLines * InvoiceLine.no.item.price
        case _ => nestedRows.flatMap { nestedRow =>
          val nestedE     = tempId(ref)
          val bondStmt    = Add(parentE, ref, nestedE, Card(2))
          val nestedStmts = resolveStmts(nestedGenStmts, nestedRow, nestedE)
          bondStmt +: nestedStmts
        }
      }

      (next, edgeB, stmts ++ nestedInsertStmts)
    }

    case unexpected => err("matchDataStmt", s"Unexpected insert statement: $unexpected\nGenericStmt: $genericStmt\nStmts:\n" + stmts.mkString("\n"))
  }


  def resolveStmts(genericStmts: Seq[Statement], row: Seq[Any], nestedE0: Any = 0, edgeB0: Option[AnyRef] = None): Seq[Statement] = {
    genericStmts.foldLeft(0, edgeB0, Seq[Statement]()) { case ((cur, edgeB, stmts0), genericStmt0) =>
      val arg0      = row.asJava.get(cur)
      val next: Int = if ((cur + 1) < row.size) cur + 1 else cur

      val (stmts, nestedE) = if (stmts0.isEmpty)
        (stmts0, nestedE0)
      else stmts0.last match {
        case Add("nsFull", _, backRef, _) => (stmts0.init, backRef)
        case _                            => (stmts0, nestedE0)
      }

      (arg0, genericStmt0) match {

        // null values not allowed
        case (null, _) => err("resolveStmts", "null values not allowed. Please use `attr$` for Option[tpe] values.")

        // Backreference - with mandatory previous ref
        case (_, Add("nsFull", nsFull, "", bi)) =>
          val backRef = stmts.reverse.collectFirst {
            case Add(e: db.DbId, a, _, _) if a.startsWith(nsFull) => e
          } getOrElse err("resolveStmts", s"Couldn't find namespace `$nsFull` in any previous Add statements.\n" + stmts.mkString("\n"))
          (cur, edgeB, stmts :+ Add("nsFull", nsFull, backRef, bi))

        case (None, Add("e", a, refNs: String, bi@BiEdgeRef(_, _))) if !refNs.startsWith("__") =>
          val (edgeA, edgeB1) = (tempId(refNs), Some(tempId(a)))
          (cur, edgeB1, valueStmts(stmts, lastE(stmts, a, 0, bi), a, edgeA, None, bi, edgeB1))

        case (None, Add("e", a, refNs: String, bi)) if !refNs.startsWith("__")                      =>
          (cur, edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(refNs), None, bi, edgeB))
        case (None, _)                                                                              =>
          (next, edgeB, stmts)
        case (Some(arg), genericStmt)                                                               =>
          matchDataStmt(stmts, genericStmt, arg, cur, next, nestedE, edgeB)
        case (arg, Add("e", a, "__arg", bi)) if eidV(stmts)                                         =>
          (next, edgeB, valueStmts(stmts, stmts.last.v, a, arg, None, bi, edgeB))
        case (arg, genStmt@Add("e", _, refNs: String, _)) if !refNs.startsWith("__") && eidV(stmts) =>
          matchDataStmt(stmts, genStmt.copy(e = "v"), arg, cur, next, nestedE, edgeB)
        case (arg, genStmt)                                                                         =>
          matchDataStmt(stmts, genStmt, arg, cur, next, nestedE, edgeB)
      }
    }._3.filterNot(_.e == -1)
  }

  def txRefAttr(stmts: Seq[Statement]): Boolean = stmts.nonEmpty && stmts.last.v.toString.startsWith("#db/id[:db.part/")

  def insertStmts(dataRows: Seq[Seq[Any]]): Seq[Seq[Statement]] = {
    val (genericStmts, genericTxStmts)  = splitStmts()
    val dataStmtss: Seq[Seq[Statement]] = dataRows.map(resolveStmts(genericStmts, _))
    val txId                            = tempId("tx")
    val txStmtss  : Seq[Seq[Statement]] = Seq(genericTxStmts.foldLeft(Seq[Statement]()) {
      case (stmts, Add("tx", a, Values(vs, prefix), bi)) if txRefAttr(stmts)   => valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, None)
      case (stmts, Add("tx", a, Values(vs, prefix), bi))                       => valueStmts(stmts, txId, a, vs, prefix, bi, None)
      case (stmts, Add("tx", a, refNs: String, bi)) if !refNs.startsWith("__") => valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(refNs), None, bi, None)
      case (stmts, Add("v", a, Values(vs, prefix), bi))                        => valueStmts(stmts, txId, a, vs, prefix, bi, None)
      case (stmts, unexpected)                                                 => err("insertStmts", "Unexpected insert statement: " + unexpected)
    })
    dataStmtss ++ (if (txStmtss.head.isEmpty) Nil else txStmtss)
  }


  def saveStmts(): Seq[Statement] = {
    val txId = "datomic.tx"
    stmtsModel.foldLeft(None: Option[AnyRef], Seq[Statement]()) { case ((edgeB, stmts), genericStmt) =>
      genericStmt match {
        case Add("__tempId", a, Values(vs, pf), bi@BiEdgePropAttr(_))                               => val edgeB1 = Some(tempId(a)); (edgeB1, valueStmts(stmts, tempId(a), a, vs, pf, bi, edgeB1))
        case Add("__tempId", a, Values(vs, pf), bi)                                                 => (edgeB, valueStmts(stmts, tempId(a), a, vs, pf, bi, edgeB))
        case Add("e", a, "__tempId", bi)                                                            => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(a), None, bi, edgeB))
        case Add(e, a, "__tempId", bi)                                                              => (edgeB, valueStmts(stmts, e, a, tempId(a), None, bi, edgeB))
        case Add(e@("e" | "ec"), a, Values(vs, prefix), bi)                                         => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, vs, prefix, bi, edgeB))
        case Add(e@("e" | "ec"), a, refNs: String, bi@BiEdgeRef(_, _)) if !refNs.startsWith("__")   => val edgeB1 = Some(tempId(a)); (edgeB1, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, tempId(refNs), None, bi, edgeB1))
        case Add(e@("e" | "ec"), a, refNs: String, bi@BiTargetRef(_, _)) if !refNs.startsWith("__") => (None, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, tempId(refNs), None, bi, edgeB))
        case Add(e@("e" | "ec"), a, refNs: String, bi) if !refNs.startsWith("__")                   => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi, e), a, tempId(refNs), None, bi, edgeB))
        case Add("v", a, Values(vs, prefix), bi)                                                    => (edgeB, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, edgeB))
        case Add("tx", a, Values(vs, prefix), bi) if txRefAttr(stmts)                               => (edgeB, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, edgeB))
        case Add("tx", a, Values(vs, prefix), bi)                                                   => (edgeB, valueStmts(stmts, txId, a, vs, prefix, bi, edgeB))
        case Add("tx", a, refNs: String, bi) if !refNs.startsWith("__")                             => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(refNs), None, bi, edgeB))
        case Add("nsFull", a, _, _)                                                                 => (edgeB, stmts)
        case Retract(_, _, _, _)                                                                    => (edgeB, stmts)
        case Add(id: Long, _, Values(_, _), _)                                                      => err("saveStmts", s"With a given id `$id` please use `update` instead.")
        case Add(_, a, "__arg", _)                                                                  => err("saveStmts", s"Attribute `$a` needs a value applied")
        case unexpected                                                                             => err("saveStmts", s"Unexpected save statement: $unexpected\nStatements so far:\n" + stmts.mkString("\n"))
      }
    }._2
  }


  def updateStmts(): Seq[Statement] = {
    val (genericStmts, genericTxStmts) = splitStmts()
    val dataStmts: Seq[Statement]      = genericStmts.foldLeft(
      None: Option[AnyRef],
      Seq.empty[Statement],
      0L
    ) { case ((edgeB, stmts, prevE), genericStmt) =>
      genericStmt match {
        case Add("e", a, "__tempId", bi)                                                 => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(a), None, bi, edgeB), 0L)
        case Add(e, a, "__tempId", bi@BiEdgeRef(_, _))                                   => val edgeB1 = Some(tempId(a)); (edgeB1, valueStmts(stmts, e, a, tempId(a), None, bi, edgeB1), 0L)
        case Add(e, a, "__tempId", bi)                                                   => (edgeB, valueStmts(stmts, e, a, tempId(a), None, bi, edgeB), 0L)
        case Add("e", a, Values(vs, prefix), bi) if prevE == 0L                          => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, vs, prefix, bi, edgeB), 0L)
        case Add("e", a, Values(vs, prefix), bi)                                         =>
          val addStmts = valueStmts(stmts, prevE, a, vs, prefix, bi, edgeB)
          if (addStmts.isEmpty)
            (edgeB, Nil, prevE)
          else
            (edgeB, addStmts, 0L)
        case Add("e", a, refNs: String, bi@BiTargetRef(_, _)) if !refNs.startsWith("__") => (None, valueStmts(stmts, lastE(stmts, a, 0, bi), a, tempId(refNs), None, bi, edgeB), 0L)
        case Add("v", a, Values(vs, prefix), bi)                                         => (edgeB, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, edgeB), 0L)
        case Add("tx", a, Values(vs, prefix), bi)                                        => (edgeB, valueStmts(stmts, stmts.last.v.asInstanceOf[Object], a, vs, prefix, bi, edgeB), 0L)
        case Add(e, a, Values(vs, prefix), bi)                                           =>
          val addStmts = valueStmts(stmts, e, a, vs, prefix, bi, edgeB)
          if (addStmts.isEmpty)
            (edgeB, Nil, e.asInstanceOf[Long]) // pass eid so that we have it for subsequent stmts
          else
            (edgeB, addStmts, 0L)
        case Retract("e", a, Values(vs, prefix), bi)                                     => (edgeB, valueStmts(stmts, lastE(stmts, a, 0, bi), a, vs, prefix, bi, edgeB), 0L)
        case Retract(e, a, Values(vs, prefix), bi)                                       => (edgeB, valueStmts(stmts, e, a, vs, prefix, bi, edgeB), 0L)
        case Add(_, a, "__arg", _)                                                       => err("updateStmts", s"Attribute `$a` needs a value applied")
        case unexpected                                                                  => err("updateStmts", "Unexpected update statement: " + unexpected)
      }
    }._2
    val txId                           = "datomic.tx"
    val txStmts  : Seq[Statement]      = genericTxStmts.foldLeft(Seq[Statement]()) {
      case (stmts, Add("tx", a, Values(vs, prefix), bi)) => valueStmts(stmts, txId, a, vs, prefix, bi, None)
      case (_, unexpected)                               => err("updateStmts", "Unexpected insert statement: " + unexpected)
    }
    dataStmts ++ txStmts
  }


  private def untupleNestedArgss(stmts: Seq[Any], arg0: Any): Seq[Seq[Any]] = {
    val (argArity, arg) = arg0 match {
      case a: Seq[_]  => a.head match {
        case None       => err("untupleNestedArgss", "Please use `List()` instead of `List(None)` for nested null values.")
        case null       => err("untupleNestedArgss", "Please use `List()` instead of `List(null)` for nested null values.")
        case p: Product => (p.productArity, a)
        case l: Seq[_]  => (l.size, a)
        case _          => (1, a)
      }
      case unexpected => err("untupleNestedArgss", "Unexpected data: " + unexpected)
    }
    val argStmts        = stmts.collect {
      case a@Add(_, _, "__arg", _)             => a
      case a@Add(_, _, Values(_, _), _)        => a
      case a@Add(_, _, nestedStmts: Seq[_], _) => a
    }
    val stmtsSize       = argStmts.size
    if (argArity != stmtsSize) err("untupleNestedArgss", "Arity of statements and arguments should match. Found: \n" +
      s"Statements (arity $stmtsSize): " + stmts.mkString("\n  ", "\n  ", "\n") +
      s"Arguments0                  : " + arg0 +
      s"Arguments  (arity $argArity): " + arg.mkString("\n  ", "\n  ", "\n"))

    arg map tupleToSeq
  }
}