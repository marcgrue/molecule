package molecule.generic.dsl
package schema
import molecule.boilerplate.attributes._
import molecule.boilerplate.base._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait GenericSchema {
  object Schema extends Schema_0 with FirstNS {
    final override def apply(eid: Long, eids: Long*): Schema_0 = ???
    final override def apply(eids: Iterable[Long]): Schema_0 = ???
  }
}

trait Schema extends GenericNs {
  final class id           [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class ident        [Ns, In] extends OneString [Ns, In] with Indexed
  final class part         [Ns, In] extends OneString [Ns, In] with Indexed
  final class ns           [Ns, In] extends OneString [Ns, In] with Indexed
  final class nsFull       [Ns, In] extends OneString [Ns, In] with Indexed
  final class a            [Ns, In] extends OneString [Ns, In] with Indexed
  final class tpe          [Ns, In] extends OneString [Ns, In] with Indexed
  final class card         [Ns, In] extends OneString [Ns, In] with Indexed
  final class doc          [Ns, In] extends OneString [Ns, In] with Indexed with Fulltext[Ns, In]
  final class index        [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class unique       [Ns, In] extends OneString [Ns, In] with Indexed
  final class fulltext     [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class isComponent  [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class noHistory    [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class enum         [Ns, In] extends OneString [Ns, In] with Indexed
  final class t            [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class tx           [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class txInstant    [Ns, In] extends OneDate   [Ns, In] with Indexed

  final class doc$         [Ns, In] extends OneString$ [Ns] with Indexed with Fulltext[Ns, In]
  final class index$       [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class unique$      [Ns, In] extends OneString$ [Ns] with Indexed
  final class fulltext$    [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class isComponent$ [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class noHistory$   [Ns, In] extends OneBoolean$[Ns] with Indexed
}