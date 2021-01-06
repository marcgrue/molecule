package molecule.core.macros

import molecule.core.util.MacroHelpers
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

  val x = InspectMacro("TxFns", 1)

  def prepareForDatalog(annottees: Tree*): Tree = annottees match {
    case List(obj: ModuleDef) => obj match {
      case q"object $txFnContainer { ..$typedTxFns }" =>
        // Add untyped tx functions with extra initial db arg
        q"""
         object $txFnContainer {
           // import molecule.datomic.peer.facade.Database_Peer
           // import molecule.datomic.peer.facade.Conn_Peer
           // import molecule.datomic.client.facade.Database_DevLocal
           // import molecule.datomic.client.facade.Conn_Client
           // import datomicScala.client.api.sync.Db
           ..$typedTxFns
           ..${typedTxFns.map(untypedTxFn(_))}
         }
       """

      case _ =>
        c.abort(c.enclosingPosition, s"No self-type allowed in @TxFns-annotated container.")
    }

    case _ => c.abort(
      c.enclosingPosition,
      s"Only object definition containing tx functions should be annotated with @TxFns")
  }

  def untypedTxFn(element: Tree): Tree = element match {

    case q"def $txFn(..$args)(implicit $conn: Conn): $jLists = {..$txFnBody}" =>
      val txFnName = TermName(txFn.toString + "__txfn")

      /** Pass `conn` as arg if more systems are to use tx functions... something like below.
        * [[molecule.core.api.TxFunctions.txFnCall()]] would need to change signature accordingly.
        */
      //      q"""
      //        def $txFnName(
      //          conn0: AnyRef,
      //          txDb: AnyRef,
      //          txMetaData: AnyRef,
      //          ..${args.map(untypedParam(_))}
      //        ): AnyRef = {
      //          implicit val conn: Conn = txDb match {
      //            case db: datomic.db.Db      =>
      //              val c = conn0.asInstanceOf[Conn_Peer]
      //              conn.testDb(Database_Peer(db))
      //              c
      //            case db: datomic.core.db.Db =>
      //              val c = conn0.asInstanceOf[Conn_Client]
      //              conn.testDb(Database_DevLocal(Db(db)))
      //              c
      //            case db                     => throw new RuntimeException(
      //              "Tx function invoked with Unexpected db: " + db.getClass)
      //          }
      //
      //          ..${args.map(typedParam(_))}
      //          ..${txFnBody.init}
      //          val _txFnStmts = ${txFnBody.last}
      //          val _txMetaDataStmts = txMetaData.asInstanceOf[Seq[molecule.core.ast.transactionModel.Statement]]
      //          molecule.core.ast.transactionModel.toJava(_txFnStmts :+ _txMetaDataStmts)
      //        }
      //      """

      /** Transaction function is invoked by Datomic with the current
        * database `txDb` invoked as first argument:
        * */
      q"""
        def $txFnName(
          txDb: AnyRef,
          txMetaData: AnyRef,
          ..${args.map(untypedParam(_))}
        ): AnyRef = {
          implicit val conn: Conn = molecule.datomic.peer.facade.Conn_Peer(txDb)
          ..${args.map(typedParam(_))}
          ..${txFnBody.init}
          val _txFnStmts = ${txFnBody.last}
          val _txMetaDataStmts = txMetaData.asInstanceOf[Seq[molecule.core.ast.transactionModel.Statement]]
          molecule.core.ast.transactionModel.toJava(_txFnStmts :+ _txMetaDataStmts)
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
