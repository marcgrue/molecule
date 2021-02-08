/*
* AUTO-GENERATED Molecule DSL for namespace `Schema`
*
* To change:
* 1. Edit data model in molecule.core.generic.dataModel/SchemaDataModel
* 2. `sbt clean compile`
* 3. Re-compile project in IDE
*/
package molecule.core.generic.Schema

import java.util.Date
import molecule.core.dsl.attributes._
import molecule.core.generic.GenericNs

trait Schema extends GenericNs {
  final class id          [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class ident       [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class part        [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class nsFull      [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class ns          [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class a           [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class tpe         [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class card        [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class doc         [Stay, Next] extends OneString [Stay, Next] with Indexed with Fulltext[Stay, Next]
  final class index       [Stay, Next] extends OneBoolean[Stay, Next] with Indexed
  final class unique      [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class fulltext    [Stay, Next] extends OneBoolean[Stay, Next] with Indexed
  final class isComponent [Stay, Next] extends OneBoolean[Stay, Next] with Indexed
  final class noHistory   [Stay, Next] extends OneBoolean[Stay, Next] with Indexed
  final class enum        [Stay, Next] extends OneString [Stay, Next] with Indexed
  final class t           [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class tx          [Stay, Next] extends OneLong   [Stay, Next] with Indexed
  final class txInstant   [Stay, Next] extends OneDate   [Stay, Next] with Indexed
  
  final class id$         [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class ident$      [Stay, Next] extends OneString$ [Stay] with Indexed
  final class part$       [Stay, Next] extends OneString$ [Stay] with Indexed
  final class nsFull$     [Stay, Next] extends OneString$ [Stay] with Indexed
  final class ns$         [Stay, Next] extends OneString$ [Stay] with Indexed
  final class a$          [Stay, Next] extends OneString$ [Stay] with Indexed
  final class tpe$        [Stay, Next] extends OneString$ [Stay] with Indexed
  final class card$       [Stay, Next] extends OneString$ [Stay] with Indexed
  final class doc$        [Stay, Next] extends OneString$ [Stay] with Indexed with Fulltext[Stay, Next]
  final class index$      [Stay, Next] extends OneBoolean$[Stay] with Indexed
  final class unique$     [Stay, Next] extends OneString$ [Stay] with Indexed
  final class fulltext$   [Stay, Next] extends OneBoolean$[Stay] with Indexed
  final class isComponent$[Stay, Next] extends OneBoolean$[Stay] with Indexed
  final class noHistory$  [Stay, Next] extends OneBoolean$[Stay] with Indexed
  final class enum$       [Stay, Next] extends OneString$ [Stay] with Indexed
  final class t$          [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class tx$         [Stay, Next] extends OneLong$   [Stay] with Indexed
  final class txInstant$  [Stay, Next] extends OneDate$   [Stay] with Indexed
}

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

