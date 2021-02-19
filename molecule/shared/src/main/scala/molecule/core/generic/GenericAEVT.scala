package molecule.core.generic

import molecule.core.dsl.base.{FirstNS, Init}
import molecule.core.generic.AEVT
import molecule.core.generic.AEVT._

/** AEVT Index.
  *
  * "The AEVT index provides efficient access to all values for a given attribute,
  * comparable to traditional column access style."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the AEVT Index in Molecule by instantiating an AEVT object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create AEVT Index molecule with 1 entity id argument
  *   AEVT(":Person/name").e.v.t.get === List(
  *     (e1, "Ben", t2),
  *     (e2, "Liz", t5)
  *   )
  *
  *   // Narrow search with multiple arguments
  *   AEVT(":Person/name", e1).e.v.get === List( (e1, "Ben") )
  *   AEVT(":Person/name", e1, "Ben").e.v.get === List( (e1, "Ben") )
  *   AEVT(":Person/name", e1, "Ben", t2).e.v.get === List( (e1, "Ben") )
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
  *       So omitting arguments constructing the Index object (like `AEVT.a.e.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.AEVT)`
  * */
trait GenericAEVT {

  /** AEVT Index object to start AEVT Index molecule. */
  object AEVT extends AEVT_0_0_L0[AEVT_, Init] with FirstNS {

    /** Unfiltered AEVT Index fetching ALL datoms (!) */
    final def apply                                     : AEVT_0_0_L0[AEVT_, Init] = ???

    /** Instantiate AEVT Index filtered by namespace-prefixed attribute name (":part_Ns/attr"). */
    final def apply(a: String)                          : AEVT_0_0_L0[AEVT_, Init] = ???

    /** Instantiate AEVT Index filtered by attribute name and entity id. */
    final def apply(a: String, e: Long)                 : AEVT_0_0_L0[AEVT_, Init] = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id and value. */
    final def apply(a: String, e: Long, v: Any)         : AEVT_0_0_L0[AEVT_, Init] = ???

    /** Instantiate AEVT Index filtered by attribute name, entity id, value and
      * transaction entity id (`tx`) or point in time (`t`).*/
    final def apply(a: String, e: Long, v: Any, t: Long): AEVT_0_0_L0[AEVT_, Init] = ???
  }
}