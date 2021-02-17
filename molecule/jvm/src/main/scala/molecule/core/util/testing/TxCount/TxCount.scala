/*
* AUTO-GENERATED Molecule DSL for namespace `TxCount`
*
* To change:
* 1. Edit data model in app.dsl.dataModel/TxCountDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.util.testing.TxCount

import java.net.URI
import java.util.Date
import java.util.UUID
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import scala.language.higherKinds

object TxCount extends TxCount_0_0_L0[TxCount_, DummyProp] with FirstNS {
  final override def apply(eid: Long, eids: Long*): TxCount_0_0_L0[TxCount_, DummyProp] = ???
  final override def apply(eids: Iterable[Long])  : TxCount_0_0_L0[TxCount_, DummyProp] = ???
}

trait _TxCount_ {
  final class db     [Stay, Next] extends OneString[Stay, Next] with Indexed
  final class basisT [Stay, Next] extends OneLong  [Stay, Next] with Indexed
  
  final class db$    [Stay, Next] extends OneString$[Stay] with Indexed
  final class basisT$[Stay, Next] extends OneLong$  [Stay] with Indexed
}

trait TxCount_[props] { def TxCount: props = ??? }

trait TxCount_db      { lazy val db     : String = ??? }
trait TxCount_basisT  { lazy val basisT : Long   = ??? }

trait TxCount_db$     { lazy val db$    : Option[String] = ??? }
trait TxCount_basisT$ { lazy val basisT$: Option[Long  ] = ??? }

