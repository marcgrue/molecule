/*
* AUTO-GENERATED Molecule DSL boilerplate code for namespace `Datom`
*
* To change:
* 1. edit schema definition file in `molecule.coretests.generic.schema/`
* 2. `sbt compile` in terminal
* 3. Refresh and re-compile project in IDE
*/
package molecule.generic.dsl
package datom
import molecule.boilerplate.attributes._
import molecule.generic.GenericNs
import scala.language.higherKinds


trait Datom {
  final class e        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class a        [Ns, In] extends OneString [Ns, In] with Indexed
  final class v        [Ns, In] extends OneAny    [Ns, In] with Indexed
  final class t        [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class tx       [Ns, In] extends OneLong   [Ns, In] with Indexed
  final class txInstant[Ns, In] extends OneDate   [Ns, In] with Indexed
  final class op       [Ns, In] extends OneBoolean[Ns, In] with Indexed
}