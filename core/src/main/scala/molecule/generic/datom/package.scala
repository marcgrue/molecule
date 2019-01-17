package molecule.generic


/** Generic Datom attribute interfaces of all arities.
  *
  * "Generic attributes" are special pre-defined attributes that can
  * be combined with custom attributes in molecules to return
  * meta data:
  * {{{
  *   // Get id of Ben entity with `e`
  *   Person.e.name.get.head === (benEntityId, "Ben")
  *
  *   // When was Ben's age updated? Using `txInstant`
  *   Person(benEntityId).age.txInstant.get.head === (42, <April 4, 2019>) // (Date)
  *
  *   // With a history db we can access the transaction number `t` and
  *   // assertion/retraction statusses with `op`
  *   Person(benEntityId).age.t.op.getHistory === List(
  *     (41, t1, true),  // age 41 asserted in transaction t1
  *     (41, t2, false), // age 41 retracted in transaction t2
  *     (42, t2, true)   // age 42 asserted in transaction t2
  *   )
  * }}}
  * Available generic attributes:
  *
  *  - '''`e`''' - Entity id (Long)
  *  - '''`a`''' - Full attribute name like ":person/name" (String)
  *  - '''`v`''' - Value of Datoms (Any)
  *  - '''`t`''' - Transaction pointer (Long/Int)
  *  - '''`tx`''' - Transaction entity id (Long)
  *  - '''`txInstant`''' - Transaction wall clock time (java.util.Date)
  *  - '''`op`''' - Operation status: assertion (true) / retraction (false)
  *
  * @see [[https://github.com/scalamolecule/molecule/blob/master/coretests/src/test/scala/molecule/coretests/generic/Datom.scala#L1 Tests]]
  *     for more generic attribute query examples.
  */
package object datom
