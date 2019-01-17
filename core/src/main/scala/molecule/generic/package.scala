package molecule

/** Interfaces to get generic information about data and schema.
  *
  *  - [[generic.datom Datom]] - Generic Datom attributes in molecules
  *  - [[generic.Log Log]] - Datoms sorted by transaction/time
  *  - [[generic.index.EAVT EAVT]] - Datoms sorted by Entity-Attribute-Value-Transaction
  *  - [[generic.index.AVET AVET]] - Datoms sorted by Attribute-Value-Entity-Transaction
  *  - [[generic.index.AEVT AEVT]] - Datoms sorted by Attribute-Entity-Value-Transaction
  *  - [[generic.index.VAET VAET]] - "Reverse index" for reverse lookup of ref types
  *  - [[generic.schema Schema]] - Meta information about the current database schema.
  * */
package object generic
