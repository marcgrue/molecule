package molecule.action
import molecule.action.get._

/** Package with various synchronous getter methods to retrieve data from Datomic with molecules.
  *
  * For the Datomic Peer model, Datomic returns data synchronously.
  * The Peer lives in application memory and caches data aggressively to save roundtrips to
  * disk or memcached. Query results that can fit in memory will therefore have near-zero latency
  * and return extremely fast.
  *
  * Molecule has not yet addressed the Client api although the query engine would be the same
  * as for the current Peer version.
  *
  * Molecule has 5 groups of getters each returning data in various formats:
  *
  *  - [[GetList]] - default getter returning Lists of tuples. Convenient typed data, suitable for smaller data sets
  *  - [[GetArray]] - fastest retrieved typed data set. Can be traversed with a fast `while` loop
  *  - [[GetIterable]] - for lazily traversing row by row
  *  - [[GetRaw]] - fastest retrieved raw un-typed data from Datomic
  *  - [[GetJson]] - data formatted as Json string
  *
  * Getters in each of the 5 groups come with 5 time-dependent variations:
  *
  *  - get [current data]
  *  - getAsOf
  *  - getSince
  *  - getWith
  *  - getHistory
  *
  * Each time variation has various overloads taking different parameters (see each group for more info).
  * */
package object get
