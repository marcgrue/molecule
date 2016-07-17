package molecule.bidirectional

import molecule._
import molecule.DatomicFacade
import molecule.bidirectional.dsl.bidirectional.{living_Knows, living_Person, living_Quality}
import molecule.bidirectional.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope with DatomicFacade {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}
