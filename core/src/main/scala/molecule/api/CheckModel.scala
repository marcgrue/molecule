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
    edgeConsistency
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

  private def iae(method: String, msg: String) = {
    throw new IllegalArgumentException(s"[molecule.api.CheckModel.$method]  $msg")
  }

  // Avoid mixing insert/update style
  private def noAppliedId = model.elements.head match {
    case Meta(ns, _, "e", NoValue, Eq(List(eid))) => iae("noAppliedId",
      s"Can't $op molecule with an applied eid as in `${ns.capitalize}(eid)`. " +
        s"""Applying an eid is for updates, like `${ns.capitalize}(johnId).likes("pizza").update`""")
    case ok                                       => ok
  }

  private def noGenericsInTail = model.elements.tail.collectFirst {
    case Meta(_, _, "e", _, Eq(List(eid))) => iae("noGenerics",
      s"Generic elements `e`, `a`, `v`, `ns`, `tx`, `txT`, `txInstant` and `op` " +
        s"not allowed in $op molecules. Found `e($eid)`")
  }

  private def noTacetAttrs {
    def detectTacetAttrs(elements: Seq[Element]): Seq[Element] = elements flatMap {
      case a: Atom if a.name.last == '_' => iae("noTacetAttrs",
        s"[api.CheckModel.noTacetAttrs] Tacet attributes like `${a.name}` not allowed in $op molecules.")
      case Nested(ref, es)               => detectTacetAttrs(es)
      case Composite(es)                 => detectTacetAttrs(es)
      case e: Element                    => Seq(e)
    }
    detectTacetAttrs(model.elements)
  }

  private def noTransitiveAttrs = model.elements collectFirst {
    case t: Transitive => iae("noTransitiveAttrs", s"Can't $op transitive attribute values (repeated attributes).")
  }

  private def noOrphanRefs {
    // An insert molecule can only build on with a ref if it has already at least one mandatory attribute
    def abortNs(i: Int, ns: String) = iae("noOrphanRefs",
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
      case Atom(ns, attr, _, 1, Eq(vs), _, _, _) if vs.length > 1 => iae("noConflictingCardOneValues",
        s"""Can't $op multiple values for cardinality-one attribute:
            |  $ns ... $attr(${vs.mkString(", ")})""".stripMargin)
      case Nested(ref, es)                                        => catchConflictingCardOneValues(es)
      case Composite(es)                                          => catchConflictingCardOneValues(es)
    }
    catchConflictingCardOneValues(model.elements)
  }

  private def noRefs = model.elements.collectFirst {
    case b: Bond      => iae("noRefs", op.capitalize + " molecules can't have references to other namespaces.")
    case n: Nested    => iae("noRefs", op.capitalize + " molecules can't have nested data structures.")
    case c: Composite => iae("noRefs", op.capitalize + " molecules can't be composites.")
  }

  private def noNested {
    def checkNested(elements: Seq[Element]): Unit = elements.collectFirst {
      case n: Nested     => iae("noNested", s"Nested data structures not allowed in $op molecules")
      case Composite(es) => checkNested(es)
    }
    checkNested(model.elements)
  }

  // Todo: Might be possible to implement if we control that the molecule doesn't build further out
  private def noEdgePropRefs = model.elements.collectFirst {
    case Bond(ns, refAttr, _, _, Seq(BiEdgePropRef(_))) => iae("noEdgePropRefs",
      s"Building on to another namespace from a property edge of a $op molecule not allowed. " +
        s"Please create the referenced entity sepearately and apply the created ids to a ref attr instead, like `.$refAttr(<refIds>)`")
  }

  private def ns(fullAttr: String) = fullAttr.split(":").last.split("/").head

  private def noNestedEdgesWithoutTarget {
    def checkNested(elements: Seq[Element]): Unit = elements.collectFirst {

      case Nested(Bond(baseNs, _, refNs, _, Seq(BiEdgeRef(_, _))), es)
        if !es.collectFirst {
          // One of those is expected
          case Bond(_, _, _, _, Seq(BiTargetRef(_, attr))) if ns(attr) == baseNs              => true
          case Atom(_, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) if ns(attr) == baseNs => true
        }.getOrElse(false) => iae("noNestedEdgesWithoutTarget",
        s"Nested edge ns `$refNs` should link to target ns within the nested group of attributes.")
    }
    checkNested(model.elements)
  }


  private def edgeConsistency {
    /*
      In order to maintain data consistency for bidirectional edges we need to ensure
      that no edge is connected to only the base or target entity.

      base <--> edge <--> target
    */

    def missingBase(elements: Seq[Element]): Unit = {

      def hasBase(es: Seq[Element], edgeNs: String) = elements.collectFirst {
        // Base.attr.Edge ...
        case Bond(_, _, edgeNs1, _, Seq(BiEdgeRef(_, _))) if edgeNs1 == edgeNs => true
        // Base.attr.edge ...
        case Atom(_, _, _, _, _, _, Seq(BiEdgeRefAttr(_, edgeRefAttr)), _) if ns(edgeRefAttr) == edgeNs => true
      }

      elements.collectFirst {
        // ?.. Target
        case Bond(edgeNs, _, refNs, _, Seq(BiTargetRef(_, x))) => hasBase(elements, edgeNs) getOrElse
          iae("edgeConsistency", s"Missing base namespace before edge namespace `$refNs`.")
        // ?.. target
        case Atom(edgeNs, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) => hasBase(elements, edgeNs) getOrElse
          iae("edgeConsistency", s"Missing base namespace before edge namespace `${ns(edgeNs)}`.")
      }
    }

    def missingTarget(elements: Seq[Element]): Unit = {

      def hasTarget(es: Seq[Element], edgeNs: String) = elements.collectFirst {
        // ... Target
        case Bond(edgeNs1, _, _, _, Seq(BiTargetRef(_, _))) if edgeNs1 == edgeNs => true
        // ... target
        case Atom(edgeNs1, _, _, _, _, _, Seq(BiTargetRefAttr(_, attr)), _) if edgeNs1 == edgeNs => true
        // ... <nested>
        case Nested(Bond(edgeNs1, _, _, _, _), _) if edgeNs1 == edgeNs => iae("edgeConsistency",
          s"Missing target namespace after edge namespace `$edgeNs`.")
      }

      elements.collectFirst {
        // Base.attr.Edge ..?
        case Bond(baseNs, _, edgeNs, _, Seq(BiEdgeRef(_, _))) => hasTarget(elements, edgeNs) getOrElse
          iae("edgeConsistency", s"Missing target namespace after edge namespace `$edgeNs`.")
        // Base.attr.edge ..?
        case Atom(baseNs, _, _, _, _, _, Seq(BiEdgeRefAttr(_, edgeRefAttr)), _) => hasTarget(elements, ns(edgeRefAttr)) getOrElse
          iae("edgeConsistency", s"Missing target namespace after edge namespace `${ns(edgeRefAttr)}`.")
        // Base.attr.Edge.prop ..?
        case Atom(edgeNs, prop, _, _, _, _, Seq(BiEdgePropAttr(_)), _) => hasTarget(elements, ns(prop)) getOrElse
          iae("edgeConsistency", s"Missing target namespace somewhere after edge property `$edgeNs/${ns(prop)}`.")
      }
    }

    missingBase(model.elements)
    missingTarget(model.elements)
  }

}
