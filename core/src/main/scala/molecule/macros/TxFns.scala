package molecule.macros

import molecule.util.MacroHelpers
import scala.language.experimental.macros
import scala.reflect.macros.blackbox

/** Transaction function container object annotation
  *
  * Annotate objects containing tx function methods with `@TxFns`. A Scala macro then
  * makes the necessary preparations to use the functions in Datomic transactions.
  */
class TxFns extends scala.annotation.StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro TxFnMacro.prepareForDatalog
}

private[molecule] final class TxFnMacro(val c: blackbox.Context) extends MacroHelpers {
  import c.universe._
  val x = DebugMacro("TxFns", 1)

  def prepareForDatalog(annottees: Tree*): Tree = annottees match {
    case List(obj: ModuleDef) => obj match {
      case q"object $txFnContainer { ..$typedTxFns }" =>
        // Add untyped tx functions with extra initial db arg
        q"""
         object $txFnContainer {
           ..$typedTxFns
           ..${typedTxFns.map(untypedTxFn(_))}
         }
       """

      case _ => c.abort(c.enclosingPosition, s"No self-type allowed in @TxFns-annotated container.")
    }

    case _ => c.abort(c.enclosingPosition, s"Only object definition containing tx functions should be annotated with @TxFns")
  }

  def untypedTxFn(element: Tree): Tree = element match {

    case q"def $txFn(..$args)(implicit $conn: Conn): $jLists = {..$txFnBody}" =>
      q"""
        def ${TermName(txFn.toString + "__txfn")}(txDb: AnyRef, txMetaData: AnyRef, ..${args.map(untypedParam(_))}): AnyRef = {
          implicit val conn: Conn = Conn(txDb)
          ..${args.map(typedParam(_))}
          ..${txFnBody.init}
          val _txFnStmts = ${txFnBody.last}
          val _txMetaDataStmts = txMetaData.asInstanceOf[Seq[molecule.ast.transactionModel.Statement]]
          molecule.ast.transactionModel.toJava(_txFnStmts :+ _txMetaDataStmts)
        }
      """

    case other => c.abort(c.enclosingPosition,
      s"""@txFns-annotated container only allows tx functions with the following signature constraints:
         |def <txFnName>(<args..>)(implicit conn: Conn): java.util.List[java.util.List[_]] = { <body> }
         |Found:
         |$other
      """.stripMargin)
  }

  def untypedParam(arg: Tree): Tree = arg match {
    case ValDef(_, TermName(param), _, _) => q"val ${TermName(param + "0")}: AnyRef"
    case other                            => c.abort(c.enclosingPosition, s"Unrecognized parameter of tx fn: " + other)
  }

  def typedParam(arg: Tree): Tree = arg match {
    case ValDef(_, TermName(param), tpe, _) => q"val ${TermName(param)}: $tpe = ${TermName(param + "0")}.asInstanceOf[$tpe]"
    case other                              => c.abort(c.enclosingPosition, s"Unrecognized parameter of tx fn: " + other)
  }
}
