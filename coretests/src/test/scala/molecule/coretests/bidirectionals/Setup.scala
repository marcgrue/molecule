package molecule.coretests.bidirectionals

import molecule.Imports._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}


