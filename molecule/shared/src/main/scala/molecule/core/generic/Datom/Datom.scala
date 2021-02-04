/*
* AUTO-GENERATED Molecule DSL for namespace `Datom`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/DatomDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Datom

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import molecule.core.expression.AttrExpressions.?
import molecule.core.generic.Datom._Datom._
import scala.language.higherKinds

object Datom extends Datom_0_0_L0[Datom_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): Datom_0_0_L0[Datom_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : Datom_0_0_L0[Datom_, Nothing] = ???
  final          def apply(eids: ?)               : Datom_1_0_L0[Datom_, Nothing, Long] = ???
}

// Object interface

trait Datom_[props] { def Datom: props = ??? }

trait Datom_e          { lazy val e         : Long    = ??? }
trait Datom_a          { lazy val a         : String  = ??? }
trait Datom_v          { lazy val v         : Any     = ??? }
trait Datom_t          { lazy val t         : Long    = ??? }
trait Datom_tx         { lazy val tx        : Long    = ??? }
trait Datom_txInstant  { lazy val txInstant : Date    = ??? }
trait Datom_op         { lazy val op        : Boolean = ??? }

trait Datom_e$         { lazy val e$        : Option[Long   ] = ??? }
trait Datom_a$         { lazy val a$        : Option[String ] = ??? }
trait Datom_v$         { lazy val v$        : Option[Any    ] = ??? }
trait Datom_t$         { lazy val t$        : Option[Long   ] = ??? }
trait Datom_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait Datom_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait Datom_op$        { lazy val op$       : Option[Boolean] = ??? }

