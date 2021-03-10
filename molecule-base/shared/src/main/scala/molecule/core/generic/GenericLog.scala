package molecule.core.generic

import molecule.core.dsl.base.{FirstNS, Init}
import molecule.core.generic.Log._

/** Log interface.
  *<br><br>
  * Datomic's database log is a recording of all transaction data in historic order,
  * organized for efficient access by transaction.
  *<br><br>
  * Instantiate Log object with tx range arguments between `from` (inclusive) and `until` (exclusive),
  * and add Log attributes to be returned as tuples of data:
  * {{{
  *   // Data from transaction t1 until t4 (exclusive)
  *   Log(Some(t1), Some(t4)).t.e.a.v.op.get === List(
  *     (t1, e1, ":Person/name", "Ben", true),
  *     (t1, e1, ":Person/age", 41, true),
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true),
  *
  *     (t3, e1, ":Person/age", 41, false),
  *     (t3, e1, ":Person/age", 42, true)
  *   )
  *
  *   // If `from` is None, the range starts from the beginning
  *   Log(None, Some(t3)).v.e.t.get === List(
  *     (t1, e1, ":Person/name", "Ben", true),
  *     (t1, e1, ":Person/age", 41, true),
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true)
  *
  *     // t3 not included
  *   )
  *
  *   // If `until` is None, the range goes to the end
  *   Log(Some(t2), None).v.e.t.get === List(
  *     // t1 not included
  *
  *     (t2, e2, ":Person/name", "Liz", true),
  *     (t2, e2, ":Person/age", 37, true),
  *
  *     (t3, e1, ":Person/age", 41, false),
  *     (t3, e1, ":Person/age", 42, true)
  *   )
  * }}}
  *
  * Log attributes available:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":Person/name" (String)
  *  - '''`v`''' - Value of Datoms (Any)
  *  - '''`t`''' - Transaction pointer (Long/Int)
  *  - '''`tx`''' - Transaction entity id (Long)
  *  - '''`txInstant`''' - Transaction wall clock time (java.util.Date)
  *  - '''`op`''' - Operation status: assertion (true) / retraction (false)
  *
  * @note Contrary to the Datomic Log which is map of individual transactions
  * the Molecule Log implementation is flattened to be one continuous list
  * of transaction data. This is to have a transparent unified return type
  * as all other molecules returning data. Data can always be grouped if needed.
  */
trait GenericLog {

  /** Log object to start Log molecule. */
  object Log extends Log_0_0_L0[Log_, Init] with FirstNS {

    /** Range of transactions (using Datomic's Log.txRange API)
      *
      * Returns transactions between transaction points in time `from` (inclusive)
      * and `until` (exclusive).
      *
      * Option args can be
      *
      * - Transaction point in time (`t`) of type Long/Int
      * - Transaction entity id (`tx`) of type Long
      * - Transaction wall clock time (`txInstant`) of type java.util.Date
      *
      * If `from` is None, transactions from beginning are fetched.<br>
      * If `until` is None, transaction until end are fetched.<br>
      *
      * Molecule doesn't allow both arguments to be None since it would return the
      * whole database. (Use Datomic raw access if that is needed)
      *
      * @param from First tx (inclusive). Beginning if None
      * @param until Last tx (exclusive). End if None
      * @return Log builder object to add generic datom attributes
      */
    final def apply(from: Option[Any], until: Option[Any]): Log_0_0_L0[Log_, Init] = ???

    final def apply(from: Option[Any]): Log_0_0_L0[Log_, Init] = ???

    final def apply(): Log_0_0_L0[Log_, Init] = ???
  }
}