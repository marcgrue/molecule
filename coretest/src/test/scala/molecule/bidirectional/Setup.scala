package molecule.bidirectional

import molecule._
import molecule.DatomicFacade
import molecule.bidirectional.dsl.bidirectional.{Knows, Person, Quality}
import molecule.bidirectional.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope with DatomicFacade {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}
