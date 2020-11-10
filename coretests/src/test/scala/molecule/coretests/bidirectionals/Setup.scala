package molecule.coretests.bidirectionals

import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.datomic.peer.api.out1._
import org.specs2.specification.Scope


class Setup extends Scope {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}


