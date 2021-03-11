package molecule.core.api

/** Synchronous getter methods to retrieve data as objects.
  *
  * The Datomic On-Prem(ises) server model provides a Peer that returns data synchronously.
  * The Peer which lives in application memory caches data aggressively and for data
  * fitting in memory latency can be extremely low and queries return very fast. And
  * even when access to disk is needed, clever branching is used. Memcached is also
  * an option.
  *
  * The Datomic Cloud model data returns data asynchronously. If Datomic creates a
  * Java API for the Cloud model, Molecule could relatively easy adapt to this model too.
  * In the meanwhile, Future-wrapped methods in this package can be used.
  *
  * Molecule has 3 groups of synchronous object getters, each returning data in various formats:
  *
  *  - [[GetObjArray]] - fastest retrieved typed data set. Can be traversed with a fast `while` loop
  *  - [[GetObjIterable]] - for lazily traversing row by row
  *  - [[GetObjList]] - default getter returning Lists of objects. Convenient typed data, suitable for smaller data sets
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
  *
  * @see equivalent asynchronous getters in the [[getAsyncTpl]] package.
  * */
package object getObj
