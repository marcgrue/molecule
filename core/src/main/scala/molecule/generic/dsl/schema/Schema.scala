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
  final class id            [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class ident         [Ns, In] extends OneString [Ns, In] with Indexed
  final class ns            [Ns, In] extends OneString [Ns, In] with Indexed
  final class a             [Ns, In] extends OneString [Ns, In] with Indexed
  final class tpe           [Ns, In] extends OneString [Ns, In] with Indexed
  final class card          [Ns, In] extends OneString [Ns, In] with Indexed
  final class doc           [Ns, In] extends OneString [Ns, In] with Indexed with FulltextSearch[Ns, In]
  final class indexed       [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class unique        [Ns, In] extends OneString [Ns, In] with Indexed
  final class fulltextSearch[Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class isComponent   [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class noHistory     [Ns, In] extends OneBoolean[Ns, In] with Indexed
  final class enums         [Ns, In] extends ManyString[Ns, In] with Indexed
  final class t             [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class tx            [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class txInstant     [Ns, In] extends OneDate   [Ns, In] with Indexed

  final class id$            [Ns, In] extends OneLong$   [Ns] with Indexed
  final class ident$         [Ns, In] extends OneString$ [Ns] with Indexed
  final class ns$            [Ns, In] extends OneString$ [Ns] with Indexed
  final class a$             [Ns, In] extends OneString$ [Ns] with Indexed
  final class tpe$           [Ns, In] extends OneString$ [Ns] with Indexed
  final class card$          [Ns, In] extends OneString$ [Ns] with Indexed
  final class doc$           [Ns, In] extends OneString$ [Ns] with Indexed with FulltextSearch[Ns, In]
  final class indexed$       [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class unique$        [Ns, In] extends OneString$ [Ns] with Indexed
  final class fulltextSearch$[Ns, In] extends OneBoolean$[Ns] with Indexed
  final class isComponent$   [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class noHistory$     [Ns, In] extends OneBoolean$[Ns] with Indexed
  final class enums$         [Ns, In] extends ManyString$[Ns] with Indexed
  final class t$             [Ns, In] extends OneLong$   [Ns] with Indexed
  final class tx$            [Ns, In] extends OneLong$   [Ns] with Indexed
  final class txInstant$     [Ns, In] extends OneDate$   [Ns] with Indexed
}