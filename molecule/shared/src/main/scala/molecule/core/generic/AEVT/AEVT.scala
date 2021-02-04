/*
* AUTO-GENERATED Molecule DSL for namespace `AEVT`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/AEVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AEVT

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object AEVT extends AEVT_0_0_L0[AEVT_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): AEVT_0_0_L0[AEVT_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : AEVT_0_0_L0[AEVT_, Nothing] = ???
}

// Object interface

trait AEVT_[props] { def AEVT: props = ??? }

trait AEVT_e          { lazy val e         : Long    = ??? }
trait AEVT_a          { lazy val a         : String  = ??? }
trait AEVT_v          { lazy val v         : Any     = ??? }
trait AEVT_t          { lazy val t         : Long    = ??? }
trait AEVT_tx         { lazy val tx        : Long    = ??? }
trait AEVT_txInstant  { lazy val txInstant : Date    = ??? }
trait AEVT_op         { lazy val op        : Boolean = ??? }

trait AEVT_e$         { lazy val e$        : Option[Long   ] = ??? }
trait AEVT_a$         { lazy val a$        : Option[String ] = ??? }
trait AEVT_v$         { lazy val v$        : Option[Any    ] = ??? }
trait AEVT_t$         { lazy val t$        : Option[Long   ] = ??? }
trait AEVT_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait AEVT_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait AEVT_op$        { lazy val op$       : Option[Boolean] = ??? }

