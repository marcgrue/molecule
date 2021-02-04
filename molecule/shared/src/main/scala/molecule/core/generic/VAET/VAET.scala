/*
* AUTO-GENERATED Molecule DSL for namespace `VAET`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/VAETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.VAET
import molecule.core.generic.VAET._VAET._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object VAET extends VAET_0_0_L0[VAET_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): VAET_0_0_L0[VAET_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : VAET_0_0_L0[VAET_, Nothing] = ???
}

// Object interface

trait VAET_[props] { def VAET: props = ??? }

trait VAET_e          { lazy val e         : Long    = ??? }
trait VAET_a          { lazy val a         : String  = ??? }
trait VAET_v          { lazy val v         : Any     = ??? }
trait VAET_t          { lazy val t         : Long    = ??? }
trait VAET_tx         { lazy val tx        : Long    = ??? }
trait VAET_txInstant  { lazy val txInstant : Date    = ??? }
trait VAET_op         { lazy val op        : Boolean = ??? }

trait VAET_e$         { lazy val e$        : Option[Long   ] = ??? }
trait VAET_a$         { lazy val a$        : Option[String ] = ??? }
trait VAET_v$         { lazy val v$        : Option[Any    ] = ??? }
trait VAET_t$         { lazy val t$        : Option[Long   ] = ??? }
trait VAET_tx$        { lazy val tx$       : Option[Long   ] = ??? }
trait VAET_txInstant$ { lazy val txInstant$: Option[Date   ] = ??? }
trait VAET_op$        { lazy val op$       : Option[Boolean] = ??? }

