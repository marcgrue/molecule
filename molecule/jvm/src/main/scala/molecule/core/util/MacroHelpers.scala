package molecule.core.util

import molecule.core.ast.elements._
import molecule.core.exceptions.MoleculeCompileException
import scala.reflect.macros.blackbox
import scala.util.matching

private[molecule] trait MacroHelpers {
  val c: blackbox.Context
  import c.universe._

  type E = Expr[_]
  type W[T] = c.WeakTypeTag[T]
  type PF[A, B] = PartialFunction[A, B]

  def w[T](implicit attag: WeakTypeTag[T]): Type = weakTypeOf[T]

  def expr(tree: Tree): c.Expr[Nothing] = {
    c.Expr(tree)(c.WeakTypeTag(tree.tpe))
  }

  implicit class TreeHelper(tree: Tree) {
    def raw: String = showRaw(tree)
  }

  def abortTree(tree: Tree, msg: String, inspect: Boolean = true) = {
    val e: StackTraceElement = Thread.currentThread.getStackTrace.tail.find(mth => mth.getMethodName != "abortTree").getOrElse {
      throw new MoleculeCompileException("[MacroHelpers:abortTree] Couldn't find method where `abortTree` was called!")
    }
    val tr: String = s"${e.getClassName}   ${e.getMethodName}   line ${e.getLineNumber}"
    val stack: String = if (inspect) Seq("----------", tree.raw, "----------", tr, "----------") ++ Thread.currentThread.getStackTrace mkString "\n" else ""
    throw new MoleculeCompileException(s"$msg:\n$tree \n$stack")
  }

  implicit class Regex(sc: StringContext) {
    def r: matching.Regex = new scala.util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  protected case class InspectMacro(clazz: String, threshold: Int, max: Int = 9999, showStackTrace: Boolean = false) {

    def apply(id: Int, params: Any*): Unit = {
      val stackTrace: String = if (showStackTrace) Thread.currentThread.getStackTrace mkString "\n" else ""

      if (id >= threshold && id <= max) {

        def traverse(x: Any, level: Int, i: Int): String = {
          val indent = if (i == 0) "" else "  " * level + i + "          "
          x match {
            case l: List[_]            => indent + "List(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case l: Map[_, _]          => indent + "Map(\n" + l.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case Nested(bond, nested)  => indent + "Nested(\n" + (bond +: nested).zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case TxMetaData(elements)  => indent + "TxMetaData(\n" + elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case Composite(elements) => indent + "Composite(\n" + elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case m: Model    => indent + "Model(\n" + m.elements.zipWithIndex.map { case (y, j) => traverse(y, level + 1, j + 1) }.mkString("\n") + ")"
            case (a, b)              => {
              val bb = b match {
                case it: Iterable[_] => traverse(it, level, 0)
                case other           => other
              }
              indent + s"$a -> " + bb
            }
            case value                 => indent + value
          }
        }

        c.warning(c.enclosingPosition, s"## $id ## $clazz \n" +
          //        println(s"##$id: $clazz \n" +
          params.toList.zipWithIndex.map { case (e, i) => traverse(e, 0, i + 1) }
            .mkString("\n------------------------------------------------\n") +
          s"\n====================================================== \n$stackTrace")
      }
    }
  }

  object st {
    val stack = collection.mutable.LinkedHashMap[Int, Tree]()
    def apply(i: Int, t: Tree): Unit = {
      stack(i) = t
    }
    override def toString = stack.mkString("\n")
  }
}
