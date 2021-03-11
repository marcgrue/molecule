package molecule.core.api

/** Asynchronous getter methods to retrieve data as objects.
  *
  * For convenience, all synchronous getter methods from the `get` package are here wrapped in Futures.
  *
  * The Datomic On-Prem(ises) server model provides a Peer that returns data synchronously.
  * The Peer which lives in application memory caches data aggressively and for data
  * fitting in memory, latency can be extremely low and queries return very fast. And
  * even when access to disk is needed, clever branching is used. Memcached is also
  * an option.
  *
  * The Datomic Cloud model data returns data asynchronously. If Datomic creates a
  * Java API for the Cloud model, Molecule could relatively easy adapt to this model too.
  * In the meanwhile, Future-wrapped methods in this package can be used.
  *
  * Molecule has 4 groups of asynchronous getters for objects, each returning Futures of data in various formats:
  *
  *  - [[GetAsyncObjArray]] - fastest retrieved typed data set. Can be traversed with a fast `while` loop
  *  - [[GetAsyncObjIterable]] - for lazily traversing row by row
  *  - [[GetAsyncObjList]] - default getter returning Lists of objects. Convenient typed data, suitable for smaller data sets
  *  - [[GetAsyncRaw]] - fastest retrieved raw un-typed data from Datomic
  *
  * Getters in each of the 4 groups come with 5 time-dependent variations:
  *
  *  - getAsync [current data]
  *  - getAsyncAsOf
  *  - getAsyncSince
  *  - getAsyncWith
  *  - getAsyncHistory
  *
  * Each time variation has various overloads taking different parameters (see each group for more info).
  *
  * @see equivalent synchronous getters in the [[getTpl]] package.
  * */
package object getAsyncObj