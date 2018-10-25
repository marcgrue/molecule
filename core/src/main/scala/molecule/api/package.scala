package molecule


/** Public interfaces to be imported to use Molecule.
  *
  * To make the Molecule macro materializations as fast as possible we try to
  * import as few macro implicits as possible. If your application code build molecules
  * with at the most 10 attributes, then you can do the following import to start using Molecule:
  * {{{
  *   import molecule.api.out10._
  * }}}
  * `out` means "output molecule" , and `10` the maximum arity or number of attributes
  * of your molecules.
  *
  * If you use input molecules awaiting an input then you can add `inX` where X is
  * how many inputs (1, 2 or 3) you will use, for instance:
  * {{{
  *   import molecule.api.in2_out10._
  * }}}
  * This way we keep the implicit macro def lookups to a minimum and compilation speed
  * as fast as possible.
  *
  * Arities can be changed anytime you like. But not to a lower arity than that of the
  * molecules you use in scope of the import.
  * */
package object api
