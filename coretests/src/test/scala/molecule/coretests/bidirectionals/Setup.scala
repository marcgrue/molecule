package molecule.coretests.bidirectionals

import molecule.datomic.peer.api._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import org.specs2.specification.Scope


class Setup extends Scope {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}


