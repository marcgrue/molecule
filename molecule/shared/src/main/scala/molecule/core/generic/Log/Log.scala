/*
* AUTO-GENERATED Molecule DSL for namespace `Log`
*
* To change:
* 1. Edit data model in molecule.core._2_dsl.generic.dataModel/LogDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Log

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object Log extends Log_0_0_L0[Log_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): Log_0_0_L0[Log_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : Log_0_0_L0[Log_, Nothing] = ???
}

// Object interface

trait Log_[props] { def Log: props = ??? }

trait Log_e          { lazy val e         : Long    = ??? }
trait Log_a          { lazy val a         : String  = ??? }
trait Log_v          { lazy val v         : Any     = ??? }
trait Log_t          { lazy val t         : Long    = ??? }
trait Log_tx         { lazy val tx        : Long    = ??? }
trait Log_txInstant  { lazy val txInstant : Date    = ??? }
trait Log_op         { lazy val op        : Boolean = ??? }

trait Log_e$         { lazy val e$        : Option[Long   ] = ??? }
trait Log_a$         { lazy val a$        : Option[String ] = ??? }
trait Log_v$         { lazy val v$        : Option[Any    ] = ??? }
trait Log_t$         { lazy val t$        : Option[Long   ] = ??? }
trait Log_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait Log_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait Log_op$        { lazy val op$       : Option[Boolean] = ??? }

