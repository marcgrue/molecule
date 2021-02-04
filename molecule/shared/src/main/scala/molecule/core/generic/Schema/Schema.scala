/*
* AUTO-GENERATED Molecule DSL for namespace `Schema`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/SchemaDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Schema
import molecule.core.generic.Schema._Schema._

import java.util.Date
import molecule.core.boilerplate.api._
import molecule.core.boilerplate.attributes._
import molecule.core.boilerplate.base._
import molecule.core.boilerplate.dummyTypes._
import scala.language.higherKinds

object Schema extends Schema_0_0_L0[Schema_, Nothing] with FirstNS {
  final override def apply(eid: Long, eids: Long*): Schema_0_0_L0[Schema_, Nothing] = ???
  final override def apply(eids: Iterable[Long])  : Schema_0_0_L0[Schema_, Nothing] = ???
}

// Object interface

trait Schema_[props] { def Schema: props = ??? }

trait Schema_id           { lazy val id          : Long    = ??? }
trait Schema_ident        { lazy val ident       : String  = ??? }
trait Schema_part         { lazy val part        : String  = ??? }
trait Schema_nsFull       { lazy val nsFull      : String  = ??? }
trait Schema_ns           { lazy val ns          : String  = ??? }
trait Schema_a            { lazy val a           : String  = ??? }
trait Schema_tpe          { lazy val tpe         : String  = ??? }
trait Schema_card         { lazy val card        : String  = ??? }
trait Schema_doc          { lazy val doc         : String  = ??? }
trait Schema_index        { lazy val index       : Boolean = ??? }
trait Schema_unique       { lazy val unique      : String  = ??? }
trait Schema_fulltext     { lazy val fulltext    : Boolean = ??? }
trait Schema_isComponent  { lazy val isComponent : Boolean = ??? }
trait Schema_noHistory    { lazy val noHistory   : Boolean = ??? }
trait Schema_enum         { lazy val enum        : String  = ??? }
trait Schema_t            { lazy val t           : Long    = ??? }
trait Schema_tx           { lazy val tx          : Long    = ??? }
trait Schema_txInstant    { lazy val txInstant   : Date    = ??? }

trait Schema_id$          { lazy val id$         : Option[Long   ] = ??? }
trait Schema_ident$       { lazy val ident$      : Option[String ] = ??? }
trait Schema_part$        { lazy val part$       : Option[String ] = ??? }
trait Schema_nsFull$      { lazy val nsFull$     : Option[String ] = ??? }
trait Schema_ns$          { lazy val ns$         : Option[String ] = ??? }
trait Schema_a$           { lazy val a$          : Option[String ] = ??? }
trait Schema_tpe$         { lazy val tpe$        : Option[String ] = ??? }
trait Schema_card$        { lazy val card$       : Option[String ] = ??? }
trait Schema_doc$         { lazy val doc$        : Option[String ] = ??? }
trait Schema_index$       { lazy val index$      : Option[Boolean] = ??? }
trait Schema_unique$      { lazy val unique$     : Option[String ] = ??? }
trait Schema_fulltext$    { lazy val fulltext$   : Option[Boolean] = ??? }
trait Schema_isComponent$ { lazy val isComponent$: Option[Boolean] = ??? }
trait Schema_noHistory$   { lazy val noHistory$  : Option[Boolean] = ??? }
trait Schema_enum$        { lazy val enum$       : Option[String ] = ??? }
trait Schema_t$           { lazy val t$          : Option[Long   ] = ??? }
trait Schema_tx$          { lazy val tx$         : Option[Long   ] = ??? }
trait Schema_txInstant$   { lazy val txInstant$  : Option[Date   ] = ??? }

