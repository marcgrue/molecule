package molecule.core.macros

import molecule.core.ast.model._
import molecule.core.exceptions.MoleculeException
import molecule.core.transform.Dsl2Model
import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import scala.reflect.macros.blackbox


private[molecule] trait Base extends Dsl2Model {
  val c: blackbox.Context

  import c.universe._

  val w = InspectMacro("Base", 1)

  def mapIdents(idents: Seq[Any]): Seq[(String, Tree)] = idents.flatMap {
    case (key: String, v: String) if key.startsWith("__ident__") && v.startsWith("__ident__") => Seq(ArrowAssoc(key) -> q"convert(${TermName(key.substring(9))})", ArrowAssoc(v) -> q"convert(${TermName(v.substring(9))})")
    case (key: String, v: Any) if key.startsWith("__ident__")                                 => Seq(ArrowAssoc(key) -> q"convert(${TermName(key.substring(9))})")
    case (key: Any, v: String) if v.startsWith("__ident__")                                   => Seq(ArrowAssoc(v) -> q"convert(${TermName(v.substring(9))})")
    case ident: String if ident.startsWith("__ident__")                                       => Seq(ArrowAssoc(ident) -> q"convert(${TermName(ident.substring(9))})")
    case set: Set[_] if set.nonEmpty                                                          => set.flatMap {
      case ident if ident.toString.startsWith("__ident__") => Seq(ArrowAssoc(ident.toString) -> q"convert(${TermName(ident.toString.substring(9))})")
      case value                                           => Nil
    }
    case other                                                                                => Nil
  }

  def mapIdentifiers(elements: Seq[Element], identifiers0: Seq[(String, Tree)] = Seq()): Seq[(String, Tree)] = {
    val newIdentifiers = (elements collect {
      case Atom(_, _, _, _, Eq(idents), _, _, keyIdents)              => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, Neq(idents), _, _, keyIdents)             => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, And(idents), _, _, keyIdents)             => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, Lt(ident), _, _, keyIdents)               => mapIdents(ident +: keyIdents)
      case Atom(_, _, _, _, Gt(ident), _, _, keyIdents)               => mapIdents(ident +: keyIdents)
      case Atom(_, _, _, _, Le(ident), _, _, keyIdents)               => mapIdents(ident +: keyIdents)
      case Atom(_, _, _, _, Ge(ident), _, _, keyIdents)               => mapIdents(ident +: keyIdents)
      case Atom(_, _, _, _, Fulltext(idents), _, _, keyIdents)        => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, AssertValue(idents), _, _, keyIdents)     => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, RetractValue(idents), _, _, keyIdents)    => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, ReplaceValue(idents), _, _, keyIdents)    => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, MapEq(idents), _, _, keyIdents)           => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, AssertMapPairs(idents), _, _, keyIdents)  => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, ReplaceMapPairs(idents), _, _, keyIdents) => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, RetractMapKeys(idents), _, _, keyIdents)  => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, MapKeys(idents), _, _, keyIdents)         => mapIdents(idents ++ keyIdents)
      case Atom(_, _, _, _, _, _, _, keyIdents)                       => mapIdents(keyIdents)
      case Generic(_, _, _, Eq(idents))                               => mapIdents(idents)
      case Generic(_, _, _, Neq(idents))                              => mapIdents(idents)
      case Generic(_, _, _, Lt(ident))                                => mapIdents(Seq(ident))
      case Generic(_, _, _, Gt(ident))                                => mapIdents(Seq(ident))
      case Generic(_, _, _, Le(ident))                                => mapIdents(Seq(ident))
      case Generic(_, _, _, Ge(ident))                                => mapIdents(Seq(ident))
      case Nested(_, nestedElements)                                  => mapIdentifiers(nestedElements, identifiers0)
      case Composite(compositeElements)                               => mapIdentifiers(compositeElements, identifiers0)
      case TxMetaData(txElements)                                     => mapIdentifiers(txElements, identifiers0)
    }).flatten
    (identifiers0 ++ newIdentifiers).distinct
  }

  def compositeCasts(castss: List[List[Int => Tree]]): Seq[Tree] = {
    var i              = -1
    var subTupleFields = Seq.empty[Tree]
    val subTuples      = castss.flatMap {
      case Nil   => None
      case casts =>
        subTupleFields = casts.map { c =>
          i += 1
          c(i)
        }
        Some(q"(..$subTupleFields)")
    }
    subTuples
  }

  def compositeJsons(jsons: List[List[Int => Tree]]): ListBuffer[Tree] = {
    var fieldIndex = -1
    var firstGroup = true
    var firstPair  = true
    val buf        = new ListBuffer[Tree]
    jsons.foreach { groupLambdas =>
      if (firstGroup) firstGroup = false else buf.append(q"""sb.append(", ")""")
      buf.append(q"""sb.append("{")""")
      firstPair = true
      groupLambdas.foreach { jsonLambda =>
        fieldIndex += 1
        if (firstPair) firstPair = false else buf.append(q"""sb.append(", ")""")
        buf.append(jsonLambda(fieldIndex))
      }
      buf.append(q"""sb.append("}")""")
    }
    buf
  }

  def topLevel(casts: List[List[Int => Tree]]): List[Tree] =
    casts.head.zipWithIndex.map {
      case (cast, i) => cast(i)
    }

  def topLevelJson(casts: List[List[Int => Tree]]): List[Tree] =
    casts.head.zipWithIndex.flatMap {
      case (cast, 0) => Seq(cast(0))
      case (cast, i) => Seq(q"""sb.append(", ")""", cast(i))
    }


  // Optional nested -----------------------------------------------------------

  def castOptNestedRows(
    levelCasts: List[List[Int => Tree]],
    OutTypes: Seq[Type],
    optNestedRefIndexes: Map[Int, List[Int]],
    optNestedTacitIndexes: Map[Int, List[Int]],
  ): Tree = {
    def getLambdas(casts: List[Int => Tree], level: Int): List[Tree] = {
      casts.zipWithIndex.foldLeft(0, List.empty[Tree], 0) {
        case ((0, acc, _), (cast, i))
          if optNestedRefIndexes.keySet.contains(level) &&
            optNestedRefIndexes(level).contains(i)  => (1, acc :+ cast(level * 100 + i), i)
        case ((0, acc, 0), (cast, i)) if level == 0 => (0, acc :+ cast(i), 0)
        case ((0, acc, 0), (cast, _))               => (0, acc :+ cast(0), 0)
        case ((1, acc, refIndex), (cast, _))        => (1, acc :+ cast(level * 100 + refIndex), refIndex)
        case cast                                   => abort("Unexpected cast data: " + cast)
      }._2
    }

    val lastLevel = levelCasts.length - 1
    val lambdas   = levelCasts.zipWithIndex.foldLeft(List.empty[Tree]) {
      case (acc, (_, 0))         => acc
      case (acc, (casts, level)) =>
        val nested_x = TermName("nested_" + level)
        val nested_y = TermName("nested_" + (level + 1))

        val iterators = if (!optNestedRefIndexes.keySet.contains(level)) {
          q"val it0 = sub.next.asInstanceOf[PersistentArrayMap].valIterator()"
        } else {
          val extraIterators = optNestedRefIndexes(level).map { refIndex =>
            val itX = TermName("it" + (level * 100 + refIndex))
            q"""
               lazy val $itX = {
                 val valueMap = it0.next.asInstanceOf[PersistentArrayMap]
                 if (${casts.size} != $refIndex + valueMap.size)
                   throw new RuntimeException("Missing value in: " + valueMap)
                 valueMap.valIterator()
               }
             """
          }
          q"""
            val vs0 = sub.next.asInstanceOf[PersistentArrayMap]
            val it0 = vs0.valIterator()
            ..$extraIterators
           """
        }

        val subTuple = if (lastLevel == level)
          q"(..${getLambdas(casts, level)})"
        else
          q"(..${getLambdas(casts, level)}, $nested_y(it0.next))"

        val addTuple = {
          if (!optNestedTacitIndexes.keySet.contains(level)) {
            q"subTuples :+ $subTuple"
          } else {
            val tacitIndexes: List[Int] = optNestedTacitIndexes(level)
            val (checks, outputs)       = casts.zipWithIndex.foldLeft(
              Seq.empty[Tree], Seq.empty[Tree]
            ) {
              case ((checks, outputs), (_, i)) if tacitIndexes.contains(i) =>
                (checks :+ q"t.${TermName(s"_${i + 1}")}", outputs)

              case ((checks, outputs), (_, i)) =>
                (checks, outputs :+ q"t.${TermName(s"_${i + 1}")}")
            }

            val outputs2 = if (lastLevel == level) outputs else
              outputs :+ q"t.${TermName(s"_${casts.length + 1}")}"

            q"""
              val t = $subTuple
              if (Seq(..$checks).forall(_.nonEmpty))
                subTuples :+ (..$outputs2)
              else
                subTuples
             """
          }
        }

        val (none, subIterator) = if (level == 1)
          (q"null",
            q"""
              v.asInstanceOf[PersistentArrayMap].iterator.next
              .asInstanceOf[MapEntry].getValue
              .asInstanceOf[PersistentVector].iterator()
             """)
        else
          (q""""__none__"""",
            q"v.asInstanceOf[PersistentVector].iterator()")

        q"""
          private val $nested_x = (subData: Any) => subData match {
            case $none => List.empty[Any]
            case v     => {
              val sub = $subIterator
              var subTuples = List.empty[Any]
              while (sub.hasNext) {
                ..$iterators
                subTuples = try {
                  ..$addTuple
                } catch {
                  case e: Throwable =>
                    // Discard non-matching tuple on this level
                    subTuples
                }
              }
              subTuples
            }
          }
         """ :: acc
    }

    val tree =
      q"""
        import clojure.lang.{Keyword, LazySeq, MapEntry, PersistentArrayMap, PersistentHashSet, PersistentVector}
        import java.net.URI
        import java.util.{Date, UUID, List => jList, Map => jMap, Iterator => jIterator}

        ..$lambdas

        // Attributes before nested processed with normal lambdas using jList data
        private val nested_0 = (row: jList[AnyRef]) => (
          ..${getLambdas(levelCasts.head, 0)},
          nested_1(row.get(${levelCasts.head.length}))
        )

        final override def castRow(row: jList[AnyRef]): (..$OutTypes) =
          nested_0(row).asInstanceOf[(..$OutTypes)]
       """
    //    println(tree)
    tree
  }


  case class resolveNestedTupleMethods(
    casts: List[List[Int => Tree]],
    types: List[List[Tree]],
    OutTypes: Seq[Type],
    postTypes: List[Tree],
    postCasts: List[Int => Tree]
  ) {
    val levels = casts.size
    lazy val t1: Tree = tq"List[(..${if (levels == 2) types(1) else types(1) :+ t2})]"
    lazy val t2: Tree = tq"List[(..${if (levels == 3) types(2) else types(2) :+ t3})]"
    lazy val t3: Tree = tq"List[(..${if (levels == 4) types(3) else types(3) :+ t4})]"
    lazy val t4: Tree = tq"List[(..${if (levels == 5) types(4) else types(4) :+ t5})]"
    lazy val t5: Tree = tq"List[(..${if (levels == 6) types(5) else types(5) :+ t6})]"
    lazy val t6: Tree = tq"List[(..${if (levels == 7) types(6) else types(6) :+ t7})]"
    lazy val t7: Tree = tq"List[(..${types(7)})]"

    var fieldIndex = levels - 1
    def castLevel(level: Int): List[Tree] = casts(level).map { castLambda =>
      fieldIndex += 1
      castLambda(fieldIndex)
    }

    def branch0until(subLevels: () => Tree) = if (postCasts.isEmpty) {
      q"""
         final override def castBranch0(row: java.util.List[AnyRef], leafs: List[Any]): (..$OutTypes) = (..${castLevel(0)}, leafs.asInstanceOf[$t1])
         ..${subLevels()}
       """
    } else {
      // Ensuring that post fields are last
      val pre        = castLevel(0)
      val subCastes  = subLevels()
      val postFields = postCasts.map { postCastLambda =>
        fieldIndex += 1
        postCastLambda(fieldIndex)
      }
      q"""
         final override def castBranch0(row: java.util.List[AnyRef], leafs: List[Any]): (..$OutTypes) = (..$pre, leafs.asInstanceOf[$t1], ..$postFields)
         ..$subCastes
       """
    }

    lazy val level1: () => Tree = () =>
      q"final override def castLeaf1(row: java.util.List[AnyRef]): Any = (..${castLevel(1)})"

    lazy val level2: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castLeaf2(row: java.util.List[AnyRef]): Any = (..${castLevel(2)})
        """

    lazy val level3: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castBranch2(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(2)}, leafs.asInstanceOf[$t3])
         final override def castLeaf3(row: java.util.List[AnyRef]): Any = (..${castLevel(3)})
       """
    lazy val level4: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castBranch2(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(2)}, leafs.asInstanceOf[$t3])
         final override def castBranch3(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(3)}, leafs.asInstanceOf[$t4])
         final override def castLeaf4(row: java.util.List[AnyRef]): Any = (..${castLevel(4)})
       """
    lazy val level5: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castBranch2(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(2)}, leafs.asInstanceOf[$t3])
         final override def castBranch3(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(3)}, leafs.asInstanceOf[$t4])
         final override def castBranch4(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(4)}, leafs.asInstanceOf[$t5])
         final override def castLeaf5(row: java.util.List[AnyRef]): Any = (..${castLevel(5)})
       """
    lazy val level6: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castBranch2(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(2)}, leafs.asInstanceOf[$t3])
         final override def castBranch3(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(3)}, leafs.asInstanceOf[$t4])
         final override def castBranch4(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(4)}, leafs.asInstanceOf[$t5])
         final override def castBranch5(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(5)}, leafs.asInstanceOf[$t6])
         final override def castLeaf6(row: java.util.List[AnyRef]): Any = (..${castLevel(6)})
       """
    lazy val level7: () => Tree = () =>
      q"""
         final override def castBranch1(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(1)}, leafs.asInstanceOf[$t2])
         final override def castBranch2(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(2)}, leafs.asInstanceOf[$t3])
         final override def castBranch3(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(3)}, leafs.asInstanceOf[$t4])
         final override def castBranch4(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(4)}, leafs.asInstanceOf[$t5])
         final override def castBranch5(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(5)}, leafs.asInstanceOf[$t6])
         final override def castBranch6(row: java.util.List[AnyRef], leafs: List[Any]): Any = (..${castLevel(6)}, leafs.asInstanceOf[$t7])
         final override def castLeaf7(row: java.util.List[AnyRef]): Any = (..${castLevel(7)})
       """

    def get: Tree = levels match {
      case 2 => branch0until(level1)
      case 3 => branch0until(level2)
      case 4 => branch0until(level3)
      case 5 => branch0until(level4)
      case 6 => branch0until(level5)
      case 7 => branch0until(level6)
      case 8 => branch0until(level7)
    }
  }


  case class resolveNestedJsonMethods(
    jsons: List[List[Int => Tree]],
    nestedRefAttrs: List[String],
    postJsons: List[Int => Tree]
  ) {
    val levels = jsons.size

    var fieldIndex = jsons.size - 1
    def jsonLevel(level: Int): List[Tree] = {
      var first = true
      jsons(level) match {
        case Nil         => List(q"sb")
        case jsonLambdas => jsonLambdas.flatMap { jsonLambda =>
          fieldIndex += 1
          if (first) {
            first = false
            List(jsonLambda(fieldIndex))
          } else {
            List(q"""sb.append(", ")""", jsonLambda(fieldIndex))
          }
        }
      }
    }

    def branch0until(subLevels: () => Tree): Tree = if (postJsons.isEmpty) {
      q"""
         final override def jsonBranch0(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(0, sb, {..${jsonLevel(0)}}, ${nestedRefAttrs.head}, leaf)
         ..${subLevels()}
       """
    } else {
      // Ensuring that post fields are last
      val pre        = jsonLevel(0)
      val subJsones  = subLevels()
      val postFields = postJsons.flatMap { portJsonLambda =>
        fieldIndex += 1
        List(q"""sb.append(", ")""", portJsonLambda(fieldIndex))
      }
      q"""
         final override def jsonBranch0(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(0, sb, {..$pre}, ${nestedRefAttrs.head}, leaf, {..$postFields})
         ..$subJsones
       """
    }

    lazy val level1: () => Tree = () =>
      q"""
         final override def jsonLeaf1(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(1, sb, {..${jsonLevel(1)}})
       """
    lazy val level2: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonLeaf2(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(2, sb, {..${jsonLevel(2)}})
       """
    lazy val level3: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonBranch2(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(2, sb, {..${jsonLevel(2)}}, ${nestedRefAttrs(2)}, leaf)
         final override def jsonLeaf3(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(3, sb, {..${jsonLevel(3)}})
       """
    lazy val level4: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonBranch2(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(2, sb, {..${jsonLevel(2)}}, ${nestedRefAttrs(2)}, leaf)
         final override def jsonBranch3(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(3, sb, {..${jsonLevel(3)}}, ${nestedRefAttrs(3)}, leaf)
         final override def jsonLeaf4(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(4, sb, {..${jsonLevel(4)}})
       """
    lazy val level5: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonBranch2(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(2, sb, {..${jsonLevel(2)}}, ${nestedRefAttrs(2)}, leaf)
         final override def jsonBranch3(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(3, sb, {..${jsonLevel(3)}}, ${nestedRefAttrs(3)}, leaf)
         final override def jsonBranch4(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(4, sb, {..${jsonLevel(4)}}, ${nestedRefAttrs(4)}, leaf)
         final override def jsonLeaf5(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(5, sb, {..${jsonLevel(5)}})
       """
    lazy val level6: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonBranch2(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(2, sb, {..${jsonLevel(2)}}, ${nestedRefAttrs(2)}, leaf)
         final override def jsonBranch3(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(3, sb, {..${jsonLevel(3)}}, ${nestedRefAttrs(3)}, leaf)
         final override def jsonBranch4(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(4, sb, {..${jsonLevel(4)}}, ${nestedRefAttrs(4)}, leaf)
         final override def jsonBranch5(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(5, sb, {..${jsonLevel(5)}}, ${nestedRefAttrs(5)}, leaf)
         final override def jsonLeaf6(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(6, sb, {..${jsonLevel(6)}})
       """
    lazy val level7: () => Tree = () =>
      q"""
         final override def jsonBranch1(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(1, sb, {..${jsonLevel(1)}}, ${nestedRefAttrs(1)}, leaf)
         final override def jsonBranch2(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(2, sb, {..${jsonLevel(2)}}, ${nestedRefAttrs(2)}, leaf)
         final override def jsonBranch3(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(3, sb, {..${jsonLevel(3)}}, ${nestedRefAttrs(3)}, leaf)
         final override def jsonBranch4(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(4, sb, {..${jsonLevel(4)}}, ${nestedRefAttrs(4)}, leaf)
         final override def jsonBranch5(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(5, sb, {..${jsonLevel(5)}}, ${nestedRefAttrs(5)}, leaf)
         final override def jsonBranch6(sb: StringBuilder, row: java.util.List[AnyRef], leaf: StringBuilder): StringBuilder = branch(6, sb, {..${jsonLevel(6)}}, ${nestedRefAttrs(6)}, leaf)
         final override def jsonLeaf7(sb: StringBuilder, row: java.util.List[AnyRef]): StringBuilder = leaf(7, sb, {..${jsonLevel(7)}})
       """

    def get: Tree = levels match {
      case 2 => branch0until(level1)
      case 3 => branch0until(level2)
      case 4 => branch0until(level3)
      case 5 => branch0until(level4)
      case 6 => branch0until(level5)
      case 7 => branch0until(level6)
      case 8 => branch0until(level7)
    }
  }
}
