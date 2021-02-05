package molecule.core.generic

import molecule.core.dsl.base.FirstNS
import molecule.core.generic.VAET._

/** VAET reverse Index.
  *
  * "The VAET index contains all and only datoms whose attribute has a :db/valueType of :db.type/ref.
  * This is also known as the reverse index, since it allows efficient navigation of relationships in reverse."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the VAET Index in Molecule by instantiating a VAET object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Say we have 3 entities pointing to one entity:
  *   Release.e.name.Artists.e.name.get === List(
  *     (r1, "Abbey Road", a1, "The Beatles"),
  *     (r2, "Magical Mystery Tour", a1, "The Beatles"),
  *     (r3, "Let it be", a1, "The Beatles"),
  *   )
  *
  *   // .. then we can get the reverse relationships with the VAET Index:
  *   VAET(a1).v.a.e.get === List(
  *     (a1, ":Release/artists", r1),
  *     (a1, ":Release/artists", r2),
  *     (a1, ":Release/artists", r3),
  *   )
  *
  *   // Narrow search with multiple arguments
  *   VAET(a1, ":Release/artist").e.get === List(r1, r2, r3)
  *   VAET(a1, ":Release/artist", r2).e.get === List(r2)
  *   VAET(a1, ":Release/artist", r2, t7).e.get === List(r2)
  * }}}
  *
  * Index attributes available:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":Person/name" (String)
  *  - '''`v`''' - Value of Datoms (Any)
  *  - '''`t`''' - Transaction pointer (Long/Int)
  *  - '''`tx`''' - Transaction entity id (Long)
  *  - '''`txInstant`''' - Transaction wall clock time (java.util.Date)
  *  - '''`op`''' - Operation status: assertion (true) / retraction (false)
  *
  * @note The Molecule Index API's don't allow returning the whole Index/the whole database.
  *       So omitting arguments constructing the Index object (like `VAET.v.a.e.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.VAET)`
  * */
trait GenericVAET {

  /** VAET Index object to start VAET reverse Index molecule. */
  object VAET extends VAET_0_0_L0[VAET_, Nothing] with FirstNS {

    /** Unfiltered VAET Index fetching ALL datoms (!) */
    final def apply                                          : VAET_0_0_L0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value. */
    final def apply(refId: Long)                             : VAET_0_0_L0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value and attribute name. */
    final def apply(refId: Long, a: String)                  : VAET_0_0_L0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name and entity id. */
    final def apply(refId: Long, a: String, e: Long)         : VAET_0_0_L0[VAET_, Nothing] = ???

    /** Instantiate VAET reverse Index filtered by ref entity id value, attribute name, entity id and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(refId: Long, a: String, e: Long, t: Long): VAET_0_0_L0[VAET_, Nothing] = ???
  }
}