package molecule.core.util.testing
import molecule.core._2_dsl.boilerplate.attributes._
import molecule.core._2_dsl.boilerplate.base._
import molecule.core.util.testing.txCount.TxCount_0
import scala.language.higherKinds



object TxCount extends TxCount_0 with FirstNS {
  final override def apply(eid: Long, eids: Long*): TxCount_0 = ???
  final override def apply(eids: Iterable[Long])  : TxCount_0 = ???
}

trait TxCount {
  final class db     [Ns, In] extends OneString[Ns, In] with Indexed
  final class basisT [Ns, In] extends OneLong  [Ns, In] with Indexed

  final class db$    [Ns, In] extends OneString$[Ns] with Indexed
  final class basisT$[Ns, In] extends OneLong$  [Ns] with Indexed

  trait TxCount_db      { val db     : String         }
  trait TxCount_db$     { val db$    : Option[String] }
  trait TxCount_basisT  { val basisT : Long           }
  trait TxCount_basisT$ { val basisT$: Option[Long]   }
}