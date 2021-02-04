/*
* AUTO-GENERATED Molecule DSL for namespace `EAVT`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/EAVTDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.EAVT

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object EAVT extends EAVT_0_0_L0[EAVT_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): EAVT_0_0_L0[EAVT_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : EAVT_0_0_L0[EAVT_, Nothing] = ???
}

// Object interface

trait EAVT_[props] { def EAVT: props = ??? }

trait EAVT_e          { lazy val e         : Long    = ??? }
trait EAVT_a          { lazy val a         : String  = ??? }
trait EAVT_v          { lazy val v         : Any     = ??? }
trait EAVT_t          { lazy val t         : Long    = ??? }
trait EAVT_tx         { lazy val tx        : Long    = ??? }
trait EAVT_txInstant  { lazy val txInstant : Date    = ??? }
trait EAVT_op         { lazy val op        : Boolean = ??? }

trait EAVT_e$         { lazy val e$        : Option[Long   ] = ??? }
trait EAVT_a$         { lazy val a$        : Option[String ] = ??? }
trait EAVT_v$         { lazy val v$        : Option[Any    ] = ??? }
trait EAVT_t$         { lazy val t$        : Option[Long   ] = ??? }
trait EAVT_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait EAVT_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait EAVT_op$        { lazy val op$       : Option[Boolean] = ??? }

