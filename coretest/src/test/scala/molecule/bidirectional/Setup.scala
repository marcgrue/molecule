package molecule
package bidirectional

import molecule.DatomicFacade
import molecule.bidirectional.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope with DatomicFacade {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}