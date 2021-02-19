package molecule.core.generic

import molecule.core.dsl.base.{FirstNS, Init}
import molecule.core.generic.AVET._

/** AVET Index.
  *
  * "The AVET index provides efficient access to particular combinations of attribute and value."
  * (from [[https://docs.datomic.com/on-prem/indexes.html Datomic documentation]])
  *
  * Access the AVET Index in Molecule by instantiating an AVET object with one
  * or more arguments and then add generic attributes:
  * {{{
  *   // Create AVET Index molecule with 1 entity id argument
  *   AVET(":Person/age").e.v.t.get === List(
  *     (e1, 42, t2),
  *     (e2, 37, t5)
  *     (e3, 14, t7),
  *   )
  *
  *   // Narrow search with multiple arguments
  *   AVET(":Person/age", 42).e.t.get === List( (e1, t2) )
  *   AVET(":Person/age", 42, e1).e.v.get === List( (e1, t2) )
  *   AVET(":Person/age", 42, e1, t2).e.v.get === List( (e1, t2) )
  * }}}
  *
  * The AVET Index can be filtered by a range of values between `from` (inclusive) and
  * `until` (exclusive) for an attribute:
  * {{{
  *   AVET.range(":Person/age", Some(14), Some(37)).v.e.t.get === List(
  *     (14, e4, t7) // 14 is included in value range
  *                  // 37 not included in value range
  *                  // 42 outside value range
  *   )
  *
  *   // If `from` is None, the range starts from the beginning
  *   AVET.range(":Person/age", None, Some(40)).v.e.t.get === List(
  *     (14, e3, t7),
  *     (37, e2, t5),
  *   )
  *
  *   // If `until` is None, the range goes to the end
  *   AVET.range(":Person/age", Some(20), None).v.e.t.get === List(
  *     (37, e2, t5),
  *     (42, e1, t2)
  *   )
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
  *       So omitting arguments constructing the Index object (like `AVET.a.v.e.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.AVET)`
  *       <br><br>
  *       `from` and `until` cannot both be None since Molecule doesn't allow returning all datoms.
  * */
trait GenericAVET {

  /** AVET Index object to start AVET Index molecule. */
  object AVET extends AVET_0_0_L0[AVET_, Init] with FirstNS {

    /** Unfiltered AVET Index fetching ALL datoms (!) */
    final def apply                                     : AVET_0_0_L0[AVET_, Init] = ???

    /** Instantiate AVET Index filtered by namespace-prefixed attribute name (":part_Ns/attr"). */
    final def apply(a: String)                          : AVET_0_0_L0[AVET_, Init] = ???

    /** Instantiate AVET Index filtered by attribute name and value. */
    final def apply(a: String, v: Any)                  : AVET_0_0_L0[AVET_, Init] = ???

    /** Instantiate AVET Index filtered by attribute name, value and entity id. */
    final def apply(a: String, v: Any, e: Long)         : AVET_0_0_L0[AVET_, Init] = ???

    /** Instantiate AVET Index filtered by attribute name, value, entity id and
      * transaction entity id (`tx`) or point in time (`t`).*/
    final def apply(a: String, v: Any, e: Long, t: Long): AVET_0_0_L0[AVET_, Init] = ???


    /** Range of values (using Datomic's Database.indexRange API)
      *
      * Values between `from` (inclusive) and `until` (exclusive)
      *
      *  - From beginning if `from` is None
      *  - To end if `until` is None
      *
      * */
    final def range(a: String, from: Option[Any], until: Option[Any]): AVET_0_0_L0[AVET_, Init] = ???
  }
}