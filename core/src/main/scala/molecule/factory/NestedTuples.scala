package molecule.factory

import java.util.{List => jList}

import molecule.ast.model._
import molecule.ast.query._


private[molecule] case class NestedTuples(modelE: Model, queryE: Query) {

  lazy val flatModel: Seq[Element] = {
    def recurse(element: Element): Seq[Element] = element match {
      case n: Nested                                             => n.elements flatMap recurse
      case a@Atom(_, attr, _, _, _, _, _, _) if attr.last == '_' => Seq()
      case a: Atom                                               => Seq(a)
      case Meta(_, _, "e", NoValue, Eq(List(eid)))               => Seq()
      case m: Meta                                               => Seq(m)
      case other                                                 => Seq()
    }

    val elements = modelE.elements flatMap recurse
    if (elements.size != queryE.f.outputs.size)
      sys.error("[FactoryBase:castNestedTpls]  Flattened model elements (" + elements.size + ") don't match query outputs (" + queryE.f.outputs.size + "):\n" +
        modelE + "\n----------------\n" + elements.mkString("\n") + "\n----------------\n" + queryE + "\n----------------\n")
    elements
  }

  lazy val entityIndexes: List[Int] = flatModel.zipWithIndex.collect {
    case (Meta(_, _, _, _, IndexVal), i) => i
  }.toList

  lazy val manyRefIndexes: Seq[Int] = flatModel.zipWithIndex.collect {
    case (Meta(_, "many-ref", _, _, IndexVal), i) => i
  }

  lazy val indexMap: Map[Int, Int] = flatModel.zipWithIndex.foldLeft(0, Seq.empty[(Int, Int)]) {
    case ((rawIndex, indexMap), (meta, i)) => meta match {
      case Meta(_, "many-ref", _, _, IndexVal) => (rawIndex, indexMap :+ (rawIndex, i))
      case Meta(_, _, _, _, IndexVal)          => (rawIndex + 1, indexMap :+ (rawIndex, i))
      case _                                   => (rawIndex + 1, indexMap :+ (rawIndex, i))
    }
  }._2.toMap

  def sortRows(rows: Seq[jList[AnyRef]], entityIndexes: Seq[Int]): Seq[jList[AnyRef]] = entityIndexes match {
    case List(a)                               => rows.sortBy(row => row.get(a).asInstanceOf[Long])
    case List(a, b)                            => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long]))
    case List(a, b, c)                         => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long]))
    case List(a, b, c, d)                      => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long]))
    case List(a, b, c, d, e)                   => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long]))
    case List(a, b, c, d, e, f)                => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g)             => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h)          => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h, i)       => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long]))
    case List(a, b, c, d, e, f, g, h, i, j)    => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => row.get(j).asInstanceOf[Long])
    case List(a, b, c, d, e, f, g, h, i, j, k) => rows.sortBy(row => (row.get(a).asInstanceOf[Long], row.get(b).asInstanceOf[Long], row.get(c).asInstanceOf[Long], row.get(d).asInstanceOf[Long], row.get(e).asInstanceOf[Long], row.get(f).asInstanceOf[Long], row.get(g).asInstanceOf[Long], row.get(h).asInstanceOf[Long], row.get(i).asInstanceOf[Long])).sortBy(row => (row.get(j).asInstanceOf[Long], row.get(k).asInstanceOf[Long]))
  }
}