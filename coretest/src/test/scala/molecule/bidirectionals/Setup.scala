package molecule.bidirectionals

import molecule._
import molecule.bidirectionals.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}


