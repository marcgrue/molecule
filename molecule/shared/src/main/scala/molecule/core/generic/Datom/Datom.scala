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
import molecule.core.dsl.attributes._
import scala.language.higherKinds

trait Datom {
  final class e         [Stay_, Next_] extends OneLong   [Stay_, Next_] with Indexed
  final class a         [Stay_, Next_] extends OneString [Stay_, Next_] with Indexed
  final class v         [Stay_, Next_] extends OneAny    [Stay_, Next_] with Indexed
  final class t         [Stay_, Next_] extends OneLong   [Stay_, Next_] with Indexed
  final class tx        [Stay_, Next_] extends OneLong   [Stay_, Next_] with Indexed
  final class txInstant [Stay_, Next_] extends OneDate   [Stay_, Next_] with Indexed
  final class op        [Stay_, Next_] extends OneBoolean[Stay_, Next_] with Indexed
  
  final class e$        [Stay_, Next_] extends OneLong$   [Stay_] with Indexed
  final class a$        [Stay_, Next_] extends OneString$ [Stay_] with Indexed
  final class v$        [Stay_, Next_] extends OneAny$    [Stay_] with Indexed
  final class t$        [Stay_, Next_] extends OneLong$   [Stay_] with Indexed
  final class tx$       [Stay_, Next_] extends OneLong$   [Stay_] with Indexed
  final class txInstant$[Stay_, Next_] extends OneDate$   [Stay_] with Indexed
  final class op$       [Stay_, Next_] extends OneBoolean$[Stay_] with Indexed
}

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

