/*
* AUTO-GENERATED Molecule DSL for namespace `AVET`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/AVETDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.AVET

import java.util.Date
import molecule.core.dsl.attributes._
import molecule.core.generic.GenericNs
import scala.language.higherKinds

trait AVET extends GenericNs {
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

trait AVET_[props] { def AVET: props = ??? }

trait AVET_e          { lazy val e         : Long    = ??? }
trait AVET_a          { lazy val a         : String  = ??? }
trait AVET_v          { lazy val v         : Any     = ??? }
trait AVET_t          { lazy val t         : Long    = ??? }
trait AVET_tx         { lazy val tx        : Long    = ??? }
trait AVET_txInstant  { lazy val txInstant : Date    = ??? }
trait AVET_op         { lazy val op        : Boolean = ??? }

// Please note that `$` has been subsituted with `_` to allow packaging to jars.
// To be interpreted as optional and not tacit
trait AVET_e_         { lazy val e$        : Option[Long   ] = ??? }
trait AVET_a_         { lazy val a$        : Option[String ] = ??? }
trait AVET_v_         { lazy val v$        : Option[Any    ] = ??? }
trait AVET_t_         { lazy val t$        : Option[Long   ] = ??? }
trait AVET_tx_        { lazy val tx$       : Option[Long   ] = ??? }
trait AVET_txInstant_ { lazy val txInstant$: Option[Date   ] = ??? }
trait AVET_op_        { lazy val op$       : Option[Boolean] = ??? }

