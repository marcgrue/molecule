/*
* AUTO-GENERATED Molecule DSL for namespace `AVET`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/AVETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AVET

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object AVET extends AVET_0_0_L0[AVET_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): AVET_0_0_L0[AVET_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : AVET_0_0_L0[AVET_, Nothing] = ???
}

// Object interface

trait AVET_[props] { def AVET: props = ??? }

trait AVET_e          { lazy val e         : Long    = ??? }
trait AVET_a          { lazy val a         : String  = ??? }
trait AVET_v          { lazy val v         : Any     = ??? }
trait AVET_t          { lazy val t         : Long    = ??? }
trait AVET_tx         { lazy val tx        : Long    = ??? }
trait AVET_txInstant  { lazy val txInstant : Date    = ??? }
trait AVET_op         { lazy val op        : Boolean = ??? }

trait AVET_e$         { lazy val e$        : Option[Long   ] = ??? }
trait AVET_a$         { lazy val a$        : Option[String ] = ??? }
trait AVET_v$         { lazy val v$        : Option[Any    ] = ??? }
trait AVET_t$         { lazy val t$        : Option[Long   ] = ??? }
trait AVET_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait AVET_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait AVET_op$        { lazy val op$       : Option[Boolean] = ??? }

