package molecule.core.util

import java.util.{Date, UUID}
import molecule.core._4_api.api.exception.EntityException
import molecule.core.util.fns.date2str

trait Quoted {

  protected def quote(value: Any): String = {
    val sb = new StringBuilder
    def traverse(value: Any, tabs: Int): Unit = {
      val t = "  " * tabs
      var i = 0
      value match {
        case s: String                => sb.append(s""""$s"""")
        case l: Long                  =>
          if (l > Int.MaxValue) sb.append(s"${l}L") else sb.append(l) // Int/Long hack
        case d: Double                => sb.append(d)
        case f: Float                 => sb.append(f)
        case bi: java.math.BigInteger => sb.append(bi)
        case bd: java.math.BigDecimal => sb.append(bd)
        case b: Boolean               => sb.append(b)
        case d: Date                  => sb.append(s""""${date2str(d)}"""")
        case u: UUID                  => sb.append(s""""$u"""")
        case u: java.net.URI          => sb.append(s""""$u"""")
        case s: Set[_]                =>
          sb.append("Set(")
          s.foreach { v =>
            if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")
        case l: Seq[_]                =>
          sb.append("List(")
          l.foreach {
            case (k, v) =>
              if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
              sb.append(s""""$k" -> """)
              traverse(v, tabs + 1)
              i += 1
            case v      =>
              if (i > 0) sb.append(s", ")
              traverse(v, tabs) // no line break
              i += 1
          }
          sb.append(")")
        case m: Map[_, _]             =>
          sb.append("Map(")
          m.foreach { case (k, v) =>
            if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
            sb.append(s""""$k" -> """)
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")
        case (k: String, v: Any)      =>
          sb.append(s""""$k" -> """)
          traverse(v, tabs)
        case other                    =>
          throw new EntityException(
            "Unexpected element traversed in Quoted#quote: " + other)
      }
    }
    traverse(value, 1)
    sb.result()
  }

  protected def quote2(value: Any): String = {
    val sb = new StringBuilder
    def traverse(value: Any, tabs: Int): Unit = {
      val t = "  " * tabs
      var i = 0
      value match {
        case l: List[_] =>
          sb.append("List(")
          if (l.nonEmpty) {
            l.head match {
              case _: Product =>
                sb.append(s"\n$t")
                traverse(l.head, tabs + 1)
                l.tail.foreach { e =>
                  sb.append(s",\n$t")
                  traverse(e, tabs + 1)
                }
              case _          =>
                traverse(l.head, tabs)
                l.tail.foreach { e =>
                  sb.append(", ")
                  traverse(e, tabs)
                }
            }
          }
          sb.append(")")

        case m: Map[_, _] =>
          sb.append("Map(")
          m.foreach { case (k, v) =>
            if (i > 0)
              sb.append(s", ")
            sb.append(s""""$k" -> """)
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")


        case Some(v) =>
          sb.append("Some(")
          traverse(v, tabs)
          sb.append(")")

        case None =>
          sb.append("None")

        case tuple: Product =>
          sb.append("(")
          val it   = tuple.productIterator
          var tail = false
          while (it.hasNext) {
            if (tail) {
              sb.append(", ")
            }
            traverse(it.next(), tabs)
            tail = true
          }
          sb.append(")")

        case s: String                => sb.append(s""""$s"""")
        case l: Long                  =>
          if (l > Int.MaxValue) sb.append(s"${l}L") else sb.append(l) // Int/Long hack
        case d: Double                => sb.append(d)
        case f: Float                 => sb.append(f)
        case bi: java.math.BigInteger => sb.append(bi)
        case bd: java.math.BigDecimal => sb.append(bd)
        case b: Boolean               => sb.append(b)
        case d: Date                  => sb.append(s""""${date2str(d)}"""")
        case u: UUID                  => sb.append(s""""$u"""")
        case u: java.net.URI          => sb.append(s""""$u"""")

        case s: Set[_] =>
          sb.append("\nSet(")
          s.foreach { v =>
            if (i > 0) sb.append(s",\n$t") else sb.append(s"\n$t")
            traverse(v, tabs + 1)
            i += 1
          }
          sb.append(")")


        case other =>
          throw new EntityException(
            "Unexpected element traversed in Quoted#quote2: " + other)
      }
    }
    traverse(value, 1)
    sb.result()
  }
}
