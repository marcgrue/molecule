package molecule.core.generic

import molecule.core.dsl.base.FirstNS
import molecule.core.generic.Schema._


/** Container for Schema object.
  *
  * Some Datomic types map to two Scala types:
  *
  * Datomic/Scala types:
  *
  *  - '''string''' - String
  *  - '''boolean''' - Boolean
  *  - '''long''' - Int, Long
  *  - '''float''' - Float
  *  - '''double''' - Double
  *  - '''bigint''' - BigInt
  *  - '''bigdec''' - BigDecimal
  *  - '''instant''' - java.util.Date
  *  - '''uuid''' - java.util.UUID
  *  - '''uri''' - java.net.URI
  *  - '''ref''' - Long
  * */
trait GenericSchema {

  /** Schema object to start Schema molecule. */
  object Schema extends Schema_0_0_L0[Schema_, Nothing] with FirstNS
}