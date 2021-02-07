/*
* AUTO-GENERATED Molecule DSL for namespace `Log`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/LogDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Log

import java.net.URI
import java.util.Date
import java.util.UUID
import molecule.core.composition.CompositeInit_0._
import molecule.core.composition.CompositeInit_1._
import molecule.core.composition.CompositeInit_2._
import molecule.core.composition.CompositeInit_3._
import molecule.core.composition.nested._
import molecule.core.composition.Nested_1._
import molecule.core.composition.Nested_2._
import molecule.core.composition.Nested_3._
import molecule.core.composition.Tx._
import molecule.core.dsl.attributes._
import molecule.core.dsl.base._
import molecule.core.expression._
import molecule.core.expression.AttrExpressions.?
import molecule.core.generic.Datom._
import molecule.core.generic.GenericNs
import scala.language.higherKinds

trait Log extends GenericNs {
  final class e         [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class a         [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class v         [Stay, Next] extends OneAny    [Stay, Next] with Indexed
  final class t         [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class tx        [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class txInstant [Stay, Next] extends OneDate   [Stay, Next] with Indexed
  final class op        [Stay, Next] extends OneBoolean[Stay, Next] with Indexed
  
  final class e$        [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class a$        [Stay, Next] extends OneString$ [Stay] with Indexed
  final class v$        [Stay, Next] extends OneAny$    [Stay] with Indexed
  final class t$        [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class tx$       [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class txInstant$[Stay, Next] extends OneDate$   [Stay] with Indexed
  final class op$       [Stay, Next] extends OneBoolean$[Stay] with Indexed
}

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

