package molecule.util

import molecule.ast.model._
import scala.reflect.macros.whitebox.Context

trait MacroHelpers[Ctx <: Context] {
  val c: Ctx
  import c.universe._

  type E = Expr[_]
  type W[T] = c.WeakTypeTag[T]
  type PF[A, B] = PartialFunction[A, B]

  def expr(tree: Tree) = {
    val typeCheckedTree = c.typecheck(tree.duplicate)
    c.Expr(typeCheckedTree)(c.WeakTypeTag(typeCheckedTree.tpe))
  }

  implicit class TreeHelper(tree: Tree) {
    def raw: String = showRaw(tree)
  }

  def abort(t: Any, i: Int = 0) = {
    val j = if (i > 0) s"($i) " else ""
    c.abort(c.enclosingPosition, j + t.toString.trim)
  }
  def warn(t: Any, i: Int = 0) = {
    val j = if (i > 0) s"($i) " else ""
    c.warning(c.enclosingPosition, j + t.toString.trim)
  }

  def abortTree(tree: Tree, msg: String, debug: Boolean = true) = {
    val e = Thread.currentThread.getStackTrace.tail.find(mth => mth.getMethodName != "abortTree").getOrElse {
      abort("[MacroHelpers:abortTree] Couldn't find method where `abortTree` was called!")
    }
    val tr = s"${e.getClassName}   ${e.getMethodName}   line ${e.getLineNumber}"
    val stack = if (debug) Seq("----------", tree.raw, "----------", tr, "----------") ++ Thread.currentThread.getStackTrace mkString "\n" else ""
    abort(s"$msg:\n$tree \n$stack")
  }

  implicit class Regex(sc: StringContext) {
    def r = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  protected case class DebugMacro(clazz: String, threshold: Int, max: Int = 9999, showStackTrace: Boolean = false) {

    def apply(id: Int, params: Any*): Unit = {
      val stackTrace = if (showStackTrace) Thread.currentThread.getStackTrace mkString "\n" else ""

      if (id >= threshold && id <= max) {

        def traverse(x: Any, level: Int, i: Int): String = {
          val indent = if (i == 0) "" else "  " * level + i + "          "
          x match {
            case l: List[_]           => indent + "List(\n" + l.zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case l: Map[_, _]         => indent + "Map(\n" + l.zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case Group(bond, nested)  => indent + "Group(\n" + (bond +: nested).zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case TxModel(nested)      => indent + "TxModel(\n" + nested.zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case FreeModel(elements)  => indent + "FreeModel(\n" + elements.zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case m: Model             => indent + "Model(\n" + m.elements.zipWithIndex.map {case (y, j) => traverse(y, level + 1, j + 1)}.mkString("\n") + ")"
            case (a, b)               => {
              val bb = b match {
                case it: Iterable[_] => traverse(it, level, 0)
                case other           => other
              }
              indent + s"$a -> " + bb
            }
            case value                => indent + value
          }
        }

        c.warning(c.enclosingPosition, s"## $id ## $clazz \n" +
          //        println(s"##$id: $clazz \n" +
          params.toList.zipWithIndex.map {case (e, i) => traverse(e, 0, i + 1)}
            .mkString("\n------------------------------------------------\n") +
          s"\n====================================================== \n$stackTrace")
      }
    }
  }

  object st {
    val stack = collection.mutable.LinkedHashMap[Int, Tree]()
    def apply(i: Int, t: Tree) {
      stack(i) = t
    }
    override def toString = stack.mkString("\n")
  }
}
