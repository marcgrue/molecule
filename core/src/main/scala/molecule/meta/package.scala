package molecule

/** Interfaces to get meta information about data and schema.
  *
  *  - [[meta.datom Datom]] - Datom attributes in molecules
  *  - [[meta.Log Log]] - Datoms sorted by transaction/time
  *  - [[meta.index.EAVT EAVT]] - Datoms sorted by Entity-Attribute-Value-Transaction
  *  - [[meta.index.AVET AVET]] - Datoms sorted by Attribute-Value-Entity-Transaction
  *  - [[meta.index.AEVT AEVT]] - Datoms sorted by Attribute-Entity-Value-Transaction
  *  - [[meta.index.VAET VAET]] - "Reverse index" for reverse lookup of ref types
  *  - [[meta.schema Schema]] - Meta information about the current database schema.
  * */
package object meta
