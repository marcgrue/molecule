package molecule.api
import molecule.ast.model._

case class CheckModel(model: Model, op: String) {

  op match {
    case "save"   => checkSave()
    case "insert" => checkInsert()
    case "update" => checkUpdate()
  }

  private def checkSave() {
    noAppliedId
    noGenericsInTail
    noTacetAttrs
    noTransitiveAttrs
    noOrphanRefs
    noConflictingCardOneValues
    noNested
    noEdgePropRefs
  }

  private def checkInsert() {
    noAppliedId
    noGenericsInTail
    noTacetAttrs
    noTransitiveAttrs
    noOrphanRefs
    noNestedEdgesWithoutTarget
  }

  private def checkUpdate() {
    noRefs
    noConflictingCardOneValues
    noEdgePropRefs // ??
  }


  // todo - avoid
  //  living_Person.knows(nil).update
  //  living_Person.knows(count).updateD

  private def abort(method: String, msg: String) = {
    throw new IllegalArgumentException(s"[api.CheckModel.$method] $msg")
  }

  // Avoid mixing insert/update style
  private def noAppliedId = model.elements.head match {
    case Meta(ns, _, "e", NoValue, Eq(List(eid))) => abort("noAppliedId",
      s"Can't $op molecule with an applied eid as in `${ns.capitalize}(eid)`. " +
        s"""Applying an eid is for updates, like `${ns.capitalize}(johnId).likes("pizza").update`""")
    case ok                                       => ok
  }

  private def noGenericsInTail = model.elements.tail.collectFirst {
    case Meta(_, _, "e", _, Eq(List(eid))) => abort("noGenerics",
      s"Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in $op molecules. Found `e($eid)`")
  }

  private def noTacetAttrs {
    def detectTacetAttrs(elements: Seq[Element]): Seq[Element] = elements flatMap {
      case a: Atom if a.name.last == '_' => abort("noTacetAttrs",
        s"[api.CheckModel.noTacetAttrs] Tacet attributes like `${a.name}` not allowed in $op molecules.")
      case Nested(ref, es)               => detectTacetAttrs(es)
      case Composite(es)                 => detectTacetAttrs(es)
      case e: Element                    => Seq(e)
    }
    detectTacetAttrs(model.elements)
  }

  private def noTransitiveAttrs = model.elements collectFirst {
    case t: Transitive => abort("noTransitiveAttrs", s"Can't $op transitive attribute values (repeated attributes).")
  }

  private def noOrphanRefs {
    // An insert molecule can only build on with a ref if it has already at least one mandatory attribute
    def abortNs(i: Int, ns: String) = abort("noOrphanRefs",
      s"Namespace `$ns` in $op molecule has no mandatory attributes. Please add at least one.")

    def getNs(ns: String) = if (ns.contains("_")) ns else ns.capitalize

    def avoidMissingAttrs(elements: Seq[Element]): (Element, Seq[Element], String) = elements.foldLeft((null: Element, Seq[Element](), "")) {
      case ((prevElem, attrs, ns0), e) =>
        e match {
          case a: Atom if a.name.last != '$'                    => (a, attrs :+ a, getNs(a.ns))
          case a: Atom                                          => (a, attrs, getNs(a.ns))
          case b: Bond if attrs.isEmpty                         => abortNs(1, getNs(b.ns))
          case b: Bond                                          => (b, Nil, getNs(b.ns))
          case r@ReBond(ns1, _, _, _, _)                        => (r, attrs :+ r, getNs(ns1))
          case g@Nested(ref, es) if prevElem.isInstanceOf[Bond] => abortNs(2, prevElem.asInstanceOf[Bond].refAttr.capitalize)
          case g@Nested(ref, es)                                => {
            val (_, nested, ns1) = avoidMissingAttrs(es)
            if (nested.isEmpty) abortNs(3, ns1) else (g, nested, ns1)
          }
          case m@Meta(ns1, _, "e", NoValue, _)                  => (m, attrs :+ m, getNs(ns1))
          case Composite(es)                                    => avoidMissingAttrs(es)
          case _                                                => (e, attrs, ns0)
        }
    }
    val (_, elements, ns) = avoidMissingAttrs(model.elements)
    if (elements.isEmpty) abortNs(4, ns)
  }

  private def noConflictingCardOneValues {
    //    def abort(i: Int, ns: String, attr: String, values: Seq[Any]) =

    def catchConflictingCardOneValues(elements: Seq[Element]): Unit = elements.collectFirst {
      case Atom(ns, attr, _, 1, Eq(vs), _, _, _) if vs.length > 1 => abort("noConflictingCardOneValues",
        s"""Can't $op multiple values for cardinality-one attribute:
            |  $ns ... $attr(${vs.mkString(", ")})""".stripMargin)
      case Nested(ref, es)                                        => catchConflictingCardOneValues(es)
      case Composite(es)                                          => catchConflictingCardOneValues(es)
    }
    catchConflictingCardOneValues(model.elements)
  }

  private def noRefs = model.elements.collectFirst {
    case b: Bond      => abort("noRefs", op.capitalize + " molecules can't have references to other namespaces.")
    case n: Nested    => abort("noRefs", op.capitalize + " molecules can't have nested data structures.")
    case c: Composite => abort("noRefs", op.capitalize + " molecules can't be composites.")
  }

  private def noNested {
    def checkNested(elements: Seq[Element]): Unit = elements.collectFirst {
      case n: Nested     => abort("noNested", s"Nested data structures not allowed in $op molecules")
      case Composite(es) => checkNested(es)
    }
    checkNested(model.elements)
  }

  // Todo: Might be possible to implement if we control that the molecule doesn't build further out
  private def noEdgePropRefs = model.elements.collectFirst {
    case Bond(ns, refAttr, _, _, Seq(BiEdgePropRef(_))) => abort("noEdgePropRefs",
      s"Building on to another namespace from a property edge of a $op molecule not allowed. " +
        s"Please create the referenced entity sepearately and apply the created ids to a ref attr instead, like `.$refAttr(<refIds>)`")
  }

  private def noNestedEdgesWithoutTarget {
    def checkNested(elements: Seq[Element]): Unit = elements.collectFirst {

      case Nested(Bond(baseNs, _, refNs, _, Seq(BiEdgeRef(_, _))), es)
        if !es.collectFirst {
          // One of those is expected
          case Bond(_, _, _, _, Seq(BiTargetRef(_, attr))) if attr.split(":").last.split("/").head == baseNs              => true
          case Atom(_, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) if attr.split(":").last.split("/").head == baseNs => true
        }.getOrElse(false) => abort("noNestedEdgesWithoutTarget",
        s"Nested edge ns `$refNs` should link to target ns within the nested group of attributes.")
    }
    checkNested(model.elements)
  }

  //  private def edgeConsistency {
  //    def checkTargetExists(elements: Seq[Element]): Boolean = elements.collectFirst {
  //      // One of those is expected
  //      case Bond(_, _, _, _, Seq(BiTargetRef(_, attr))) if attr.split(":").last.split("/").head == baseNs              => true
  //      case Atom(_, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) if attr.split(":").last.split("/").head == baseNs => true
  //    } getOrElse abort(
  //      s"[api.CheckModel.noNestedEdgesWithoutTarget] Nested edge ns `$refNs` should link to target ns within the nested group of attributes.")
  //
  //    def findEdgeRefs(elements: Seq[Element]): Unit = elements.collectFirst {
  //      case Bond(baseNs, _, refNs, _, Seq(BiEdgeRef(_, _))) =>
  //
  //      case Nested(Bond(baseNs, _, refNs, _, Seq(BiEdgeRef(_, _))), es)
  //        if !es.collectFirst {
  //          // One of those is expected
  //          case Bond(_, _, _, _, Seq(BiTargetRef(_, attr))) if attr.split(":").last.split("/").head == baseNs              => true
  //          case Atom(_, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) if attr.split(":").last.split("/").head == baseNs => true
  //        }.getOrElse(false) => abort(
  //        s"[api.CheckModel.noNestedEdgesWithoutTarget] Nested edge ns `$refNs` should link to target ns within the nested group of attributes.")
  //    }
  //
  //    checkNested(_elements)
  //  }

}
