package molecule.core.util.testing
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
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
}