package molecule.macros

import molecule.util.MacroHelpers
import scala.language.higherKinds
import scala.reflect.macros.blackbox


/** Macro to make transaction function invocations. */
class TxFunctionCall(val c: blackbox.Context) extends MacroHelpers {
  import c.universe._
  val q = DebugMacro("TxFunctionCall", 1)

  private[this] def resolve(txFnCall: Tree, txMolecules: Seq[Tree], op: String): Tree = {
    val q"$owner.$txFn(..$args)(..$conn)" = txFnCall
    val ownerType = owner.tpe.toString
    val txFnIdentifier = ownerType.take(ownerType.length - 4) + txFn + "__txfn"
    op match {
      case "sync"  => q"_root_.molecule.api.TxMethods.txFnCall($txFnIdentifier, Seq(..$txMolecules), ..$args)"
      case "async" => q"_root_.molecule.api.TxMethods.asyncTxFnCall($txFnIdentifier, Seq(..$txMolecules), ..$args)"
      case "debug" => q"_root_.molecule.api.TxMethods.debugTxFnCall($txFnIdentifier, Seq(..$txMolecules), ..$args)"
    }
  }

  final def txFnCall(txFnCall: Tree, txMolecules: Tree*): Tree = resolve(txFnCall, txMolecules, "sync")

  final def asyncTxFnCall(txFnCall: Tree, txMolecules: Tree*): Tree = resolve(txFnCall, txMolecules, "async")

  final def debugTxFnCall(txFnCall: Tree, txMolecules: Tree*): Tree = resolve(txFnCall, txMolecules, "debug")

}