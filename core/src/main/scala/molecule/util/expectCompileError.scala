package molecule.util

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.{Context => MacroContext}
import scala.reflect.macros.TypecheckException

// Modified `illTyped` macro by Stefan Zeiger (@StefanZeiger)

private[molecule] object expectCompileError {
  def apply(code: String): Unit = macro applyImplNoExp

  def apply(code: String, expected: String): Unit = macro applyImpl

  def applyImplNoExp(c: MacroContext)(code: c.Expr[String]) = applyImpl(c)(code, null)

  def applyImpl(c: MacroContext)(code: c.Expr[String], expected: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._

    val codeStr = code.tree match {
      case Literal(Constant(s: String)) => s.stripMargin.trim
      case x                            => c.abort(c.enclosingPosition, "Unknown code tree in compile check: " + showRaw(x))
    }

    val (expPat, expMsg) = expected match {
      case null                               => (null, "EXPECTED SOME ERROR!")
      case Expr(Literal(Constant(s: String))) => val exp = s.stripMargin.trim; (exp, "EXPECTED ERROR:\n" + exp)
    }

    try {
      val dummy = TermName(c.freshName)
      c.typecheck(c.parse(s"{ val $dummy = { $codeStr } ; () }"))
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
//        s"""Type-checking succeeded unexpectedly!!!
//          |CODE:
//          |$codeStr
//          |$expMsg
//          |--------------------
//         """.stripMargin
      )
    } catch {
      case e: TypecheckException =>
        val msg = e.getMessage.trim
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
