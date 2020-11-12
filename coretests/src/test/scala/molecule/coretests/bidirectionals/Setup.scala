package molecule.coretests.bidirectionals

import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.datomic.api.out1._
import org.specs2.specification.Scope
import molecule.datomic.peer.facade.Datomic_Peer._


class Setup extends Scope {
  implicit val conn = recreateDbFrom(BidirectionalSchema)
}


