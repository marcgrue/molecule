package molecule.core.generic

import molecule.core.dsl.base.FirstNS
import molecule.core.generic.EAVT._

/** EAVT Index.
  *
  * "The EAVT index provides efficient access to everything about a given entity.
  * Conceptually this is very similar to row access style in a SQL database,
  * except that entities can possess arbitrary attributes rather then being limited
  * to a predefined set of columns."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the EAVT Index in Molecule by instantiating an EAVT object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create EAVT Index molecule with 1 entity id argument
  *   EAVT(e1).e.a.v.t.get === List(
  *     (e1, ":Person/name", "Ben", t1),
  *     (e1, ":Person/age", 42, t2),
  *     (e1, ":Golf/score", 5.7, t2)
  *   )
  *
  *   // Narrow search with multiple arguments
  *   EAVT(e1, ":Person/age").a.v.get === List( (":Person/age", 42) )
  *   EAVT(e1, ":Person/age", 42).a.v.get === List( (":Person/age", 42) )
  *   EAVT(e1, ":Person/age", 42, t1).a.v.get === List( (":Person/age", 42) )
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
  *       So omitting arguments constructing the Index object (like `EAVT.e.a.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.EAVT)`
  * */
trait GenericEAVT {

  /** EAVT Index object to instantiate EAVT Index molecule. */
  object EAVT extends EAVT_0_0_L0[EAVT_, Nothing] with FirstNS {

    /** Unfiltered EAVT Index fetching ALL datoms (!) */
    final def apply                                     : EAVT_0_0_L0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id. */
    final def apply(e: Long)                            : EAVT_0_0_L0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id and namespace-prefixed
      * attribute name (":part_Ns/attr"). */
    final def apply(e: Long, a: String)                 : EAVT_0_0_L0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name and value. */
    final def apply(e: Long, a: String, v: Any)         : EAVT_0_0_L0[EAVT_, Nothing] = ???

    /** Instantiate EAVT Index filtered by entity id, attribute name, value and
      * transaction entity id (`tx`) or point in time (`t`). */
    final def apply(e: Long, a: String, v: Any, t: Long): EAVT_0_0_L0[EAVT_, Nothing] = ???
  }
}