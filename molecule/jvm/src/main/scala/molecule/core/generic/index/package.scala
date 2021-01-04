package molecule.core.generic


/** Datomic Index APIs in Molecule.
  * <br><br>
  * Datomic maintains four indexes that contain ordered sets of datoms.
  * Each of these indexes is named based on the sort order used:
  *
  *  - [[index.EAVT EAVT]] - Datoms sorted by Entity-Attribute-Value-Transaction
  *  - [[index.AVET AVET]] - Datoms sorted by Attribute-Value-Entity-Transaction
  *  - [[index.AEVT AEVT]] - Datoms sorted by Attribute-Entity-Value-Transaction
  *  - [[index.VAET VAET]] - "Reverse index" for reverse lookup of ref types
  *
  * Create an Index molecule by instantiating an Index object
  * with one or more arguments in the order of the Index's elements. Datoms are
  * returned as tuples of data depending of which generic attributes you add to the
  * Index molecule:
  * {{{
  *   // Create EAVT Index molecule with 1 entity id argument
  *   EAVT(e1).e.a.v.t.get === List(
  *     (e1, ":Person/name", "Ben", t1),
  *     (e1, ":Person/age", 42, t2),
  *     (e1, ":Golf/score", 5.7, t2)
  *   )
  *
  *   // Maybe we are only interested in the attribute/value pairs:
  *   EAVT(e1).a.v.get === List(
  *     (":Person/name", "Ben"),
  *     (":Person/age", 42),
  *     (":Golf/score", 5.7)
  *   )
  *
  *   // Two arguments to narrow the search
  *   EAVT(e1, ":Person/age").a.v.get === List(
  *     (":Person/age", 42)
  *   )
  * }}}
  *
  * @note The Molecule Index API's don't allow returning the whole Index/the whole database.
  *       So omitting arguments constructing the Index object (like `EAVT.e.a.v.t.get`)
  *       will throw an exception.<br>
  *       Please use Datomics API if you need to return the whole database Index:<br>
  *       `conn.db.datoms(datomic.Database.EAVT)`
  */
package object index
