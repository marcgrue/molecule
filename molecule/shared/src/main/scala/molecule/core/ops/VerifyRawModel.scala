package molecule.core.ops
import molecule.core.ast.elements.{AssertMapPairs, Atom, Bond, Composite, Distinct, Element, EntValue, Fn, Generic, Nested, NoValue, ReBond, ReplaceMapPairs, ReplaceValue, TxMetaData}
import molecule.core.ops.exception.VerifyRawModelException
import molecule.core.util.Helpers

object VerifyRawModel extends Helpers {

  def abort(msg: String) = throw new VerifyRawModelException(msg)

  val mandatoryGenericDatom = Seq("e", "tx", "t", "txInstant", "op", "a", "v")

  def apply(elements: Seq[Element],
            allowTempGenerics: Boolean = true): Seq[Element] = {

    // Model checks - mutable variables used extensively to optimize speed.

    def txError(s: String) = abort(s"Molecule not allowed to have any attributes after transaction or nested data structure. Found" + s)
    def dupValues(pairs: Seq[(Any, Any)]): Seq[String] = pairs.map(_._2.toString).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq
    def dupKeys(pairs: Seq[(Any, Any)]): Seq[Any] = pairs.map(_._1).groupBy(identity).collect { case (v, vs) if vs.size > 1 => v }.toSeq

    val last: Int = elements.length

    // Hierarchy depth of each sub-molecule in composites
    var compLevel = 0

    // Top-level attributes in composites have to be unique across sub-molecules
    var compTopLevelAttrs: List[String] = List.empty[String]

    // Sub-molecules in composites can build hierarchies independently, but with no duplicate attributes within the local branch
    var compLocalAttrs: List[String] = List.empty[String]

    var i              : Int                 = 0
    var after          : Boolean             = false
    var hasMandatory   : Boolean             = false
    var generics       : Seq[String]         = Nil
    var beforeFirstAttr: Boolean             = true
    var isFiltered     : Boolean             = false
    var nsAttrs        : Map[String, String] = Map.empty[String, String]

    elements.foreach { el =>
      i += 1

      el match {
        case e if after => e match {
          // No non-tx generic attributes after TxMetaData/Nested
          case Generic(_, attr, _, _)          => txError(s" generic attribute `$attr`")
          case Atom(_, attr, _, _, _, _, _, _) => txError(s" attribute `$attr`")
          case Bond(_, refAttr, _, _, _)       => txError(s" reference `${refAttr.capitalize}`")
          case _: TxMetaData                   => // ok
          case _                               => txError(s": " + e)
        }

        // Molecule should at least have one mandatory attribute
        case a: Atom =>
          isFiltered = true
          if (a.attr.last != '$')
            hasMandatory = true

          a match {
            case Atom(nsFull, attr, _, _, ReplaceValue(pairs), _, _, _) if dupValues(pairs).nonEmpty =>
              abort(s"Can't replace with duplicate values of attribute `:$nsFull/$attr`:\n" + dupValues(pairs).mkString("\n"))

            case Atom(nsFull, attr, _, _, AssertMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups     = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't assert multiple key/value pairs with the same key for attribute `:$nsFull/$attr`:\n" + dupPairs.mkString("\n"))

            case Atom(nsFull, attr, _, _, ReplaceMapPairs(pairs), _, _, _) if dupKeys(pairs).nonEmpty =>
              val dups     = dupKeys(pairs)
              val dupPairs = pairs.filter(p => dups.contains(p._1)).sortBy(_._1).map { case (k, v) => s"$k -> $v" }
              abort(s"Can't replace multiple key/value pairs with the same key for attribute `:$nsFull/$attr`:\n" + dupPairs.mkString("\n"))

            case Atom(nsFull, attr, _, 2, Distinct, _, _, _) =>
              abort(s"`Distinct` keyword not supported for card many attributes like `:$nsFull/$attr` (card many values already returned as Sets of distinct values).")

            case Atom(_, _, _, 3 | 4, Fn(fn, _), _, _, _) =>
              if (!Seq("not", "unify").contains(fn))
                abort("Only expression keywords `not` and `unify` can be applied to Map attributes.")

            case Atom(nsFull, attr, _, _, _, _, _, _) =>
              if (beforeFirstAttr) {
                if (generics.nonEmpty)
                  abort(s"Can't add first attribute `${a.attr}` after generic attributes (except `e` which is ok to have first). " +
                    s"Please add generic attributes ${generics.map(g => s"`$g`").mkString(", ")} after `${a.attr}`.")
                beforeFirstAttr = false
              }
              nsAttrs = nsAttrs + ((nsFull + "/" + clean(attr)) -> attr)
          }

        case Bond(nsFull, refAttr, _, _, _) =>
          if (i == last)
            abort(s"Molecule not allowed to end with a reference. Please add one or more attribute to the reference.")

          if (nsAttrs.keySet.contains(nsFull + "/" + refAttr))
            abort(s"Instead of getting the ref id with `${nsAttrs.apply(nsFull + "/" + refAttr)}` please get it via the referenced namespace: `${refAttr.capitalize}.e ...`")

          nsAttrs = Map.empty[String, String]
          hasMandatory = true
          beforeFirstAttr = false

        case g: Generic => {
          if (g.attr.last != '_')
            hasMandatory = true
          g.tpe match {
            case "datom" if mandatoryGenericDatom.contains(g.attr) =>
              if (beforeFirstAttr && (g.attr != "e" && g.attr != "e_"))
                generics = generics :+ g.attr
              g.value match {
                case NoValue | EntValue | Fn(_, _) =>
                case _                             => isFiltered = true
              }
            case "schema"                                          => if (g.value != EntValue) isFiltered = true
            case _                                                 => isFiltered = true // indexes
          }
        }

        case _: Nested =>
          hasMandatory = true
          beforeFirstAttr = false

        case _: TxMetaData =>
          after = true

        case c: Composite =>
          hasMandatory = true
          beforeFirstAttr = false

          // Start over for each sub-molecule
          compLevel = 0
          compLocalAttrs = List.empty[String]
          c.elements.foreach {
            case _: Nested =>
              abort("Nested molecules in composites are not implemented")

            case Atom(nsFull, name, _, _, _, _, _, _) =>
              if (compLevel == 0) {
                if (compTopLevelAttrs.contains(nsFull + "/" + clean(name)))
                  abort(s"Composite molecules can't contain the same attribute more than once. Found multiple instances of `:$nsFull/${clean(name)}`")
                compTopLevelAttrs = compTopLevelAttrs :+ (nsFull + "/" + clean(name))
              } else {
                if (compLocalAttrs.contains(nsFull + "/" + clean(name)))
                  abort(s"Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:$nsFull/${clean(name)}`")
                compLocalAttrs = compLocalAttrs :+ clean(nsFull + "/" + clean(name))
              }

            case Bond(nsFull, refAttr, _, _, _) =>
              if (compLevel == 0) {
                if (compTopLevelAttrs.contains(nsFull + "/" + clean(refAttr)))
                  abort(s"Composite molecules can't contain the same ref more than once. Found multiple instances of `:$nsFull/${clean(refAttr)}`")
                compTopLevelAttrs = compTopLevelAttrs :+ (nsFull + "/" + clean(refAttr))
              } else {
                if (compLocalAttrs.contains(nsFull + "/" + clean(refAttr)))
                  abort(s"Composite sub-molecules can't contain the same ref more than once. Found multiple instances of `:$nsFull/${clean(refAttr)}`")
                compLocalAttrs = compLocalAttrs :+ clean(nsFull + "/" + clean(refAttr))
              }
              compLevel += 1

            case _: ReBond => compLevel -= 1
            case _         =>
          }
        case _            =>
      }
    }

    if (!hasMandatory)
      abort(s"Molecule has only optional attributes. Please add one or more mandatory/tacit attributes.")

    // x(802, elements, beforeFirstAttr, isFiltered)

    if (!allowTempGenerics && beforeFirstAttr && !isFiltered)
      abort(s"Molecule with only generic attributes and no entity id(s) applied are not allowed " +
        s"since it would cause a full scan of the whole database.")

    elements
  }
}
