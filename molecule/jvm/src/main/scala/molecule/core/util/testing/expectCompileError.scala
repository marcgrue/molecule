package molecule.core.util.testing

import molecule.core.util.MacroHelpers
import scala.language.experimental.macros
import scala.reflect.macros.{blackbox, TypecheckException}

// Modified `illTyped` macro by Stefan Zeiger (@StefanZeiger)
// See also shapeless.test.illTyped

private[molecule] object expectCompileError {

  def apply(code: String): Unit = macro CompileChecker.applyImplNoExp
  def apply(code: String, expected: String): Unit = macro CompileChecker.applyImpl

  class CompileChecker(val c: blackbox.Context) extends MacroHelpers {
    import c.universe._
    val x = InspectMacro("CompileChecker", 1)

    def applyImplNoExp(code: c.Expr[String]) = applyImpl(code, null)

    def applyImpl(code: c.Expr[String], expected: c.Expr[String]): c.Expr[Unit] = {
      val codeStr = code.tree match {
        case Literal(Constant(s: String)) => s.stripMargin.trim
        case x                            => c.abort(c.enclosingPosition, "Unknown code tree in compile check: " + showRaw(x))
      }
      val (expPat, expMsg) = expected match {
        case null                               => (null, "EXPECTED SOME ERROR!")
        case Expr(Literal(Constant(s: String))) => val exp = s.stripMargin.trim; (exp, "EXPECTED ERROR:\n" + exp)
      }

      try {
        val dummy0 = TermName(c.freshName())
        val dummy1 = TermName(c.freshName())
        c.typecheck(c.parse(s"object $dummy0 { val $dummy1 = { $codeStr } }"))
        c.abort(c.enclosingPosition,
          s"""Type-checking succeeded unexpectedly!!!
             |CODE:
             |$codeStr
             |$expMsg
             |CODE:
             |${show(c.typecheck(c.parse("{ " + codeStr + " }")))}
             |--------------------
             |AST:
             |${showRaw(c.typecheck(c.parse("{ " + codeStr + " }")))}
             |--------------------
         """.stripMargin
        )
      } catch {
        case e: TypecheckException =>
          val msgLines = e.getMessage.split("\n")
          val msg = msgLines.size match {
            case 1 => msgLines.head
            case _ => msgLines.tail.takeWhile(!_.startsWith("\tat ")).mkString("\n")
          }
          x(0, e)

          if ((expected ne null) && !msg.startsWith(expPat))
            c.abort(c.enclosingPosition,
              s"""Type-checking failed in an unexpected way.
                 |CODE:
                 |$codeStr
                 |$expMsg
                 |ACTUAL ERROR:
                 |$msg
                 |--------------------
              """.stripMargin)
      }

      reify(())
    }
  }
}