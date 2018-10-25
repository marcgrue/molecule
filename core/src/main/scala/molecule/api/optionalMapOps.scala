package molecule.api
import molecule.action.OptionalMapOps


/** Optional implicit operations for optional Map attributes
  *
  * Is not imported in the default Molecule api imports since they
  * are rather specialized. If needed, they can be made available with
  * the following aditional import:
  * {{{
  *   import molecule.api.optionalMapOps._
  *   import molecule.api.out8._ // Standard api import with any arity
  * }}}
  * Since this is a rather specialized
  * */
object optionalMapOps extends OptionalMapOps
