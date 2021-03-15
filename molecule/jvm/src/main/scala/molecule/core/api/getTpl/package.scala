package molecule.core.api

/** Synchronous getter methods to retrieve data as tuples.
  *
  * The Datomic On-Prem(ises) server model provides a Peer that returns data synchronously.
  * The Peer which lives in application memory caches data aggressively and for data
  * fitting in memory latency can be extremely low and queries return very fast. And
  * even when access to disk is needed, clever branching is used. Memcached is also
  * an option.
  *
  * Molecule has 4 groups of synchronous tuple getters, each returning data in various formats:
  *
  *  - [[GetTplArray]] - fastest retrieved typed data set. Can be traversed with a fast `while` loop
  *  - [[GetTplIterable]] - for lazily traversing row by row
  *  - [[GetTplList]] - default getter returning Lists of tuples. Convenient typed data, suitable for smaller data sets
  *  - [[GetRaw]] - fastest retrieved raw un-typed data from Datomic
  *
  * Getters in each of the 4 groups come with 5 time-dependent variations:
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
package object getTpl
