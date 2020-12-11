package molecule.setup

import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.nested.schema.NestedSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.schema.{CoreTestSchema, TxCountSchema}
import molecule.datomic.peer.facade.Datomic_Peer
import org.specs2.mutable.Specification


class PeerServerReset extends Specification {
  sequential

  // 1. Stop Peer Server process (ctrl-c), if running.

  // 2. Run test here to create databases needed for tests
  "Create databases for peer-server" >> {

    val pro = true
    val protocol = if(pro) "dev" else "free"

    Datomic_Peer.recreateDbFrom(TxCountSchema, "localhost:4334/txCount", protocol)
    Datomic_Peer.recreateDbFrom(CoreTestSchema, "localhost:4334/coretests", protocol)
    Datomic_Peer.recreateDbFrom(BidirectionalSchema, "localhost:4334/bidirectional", protocol)
    Datomic_Peer.recreateDbFrom(PartitionTestSchema, "localhost:4334/partitions", protocol)
    Datomic_Peer.recreateDbFrom(NestedSchema, "localhost:4334/nested", protocol)

    val dbs = Datomic_Peer.getDatabaseNames(protocol)

    dbs.contains("txCount") === true
    dbs.contains("coretests") === true
    dbs.contains("bidirectional") === true
    dbs.contains("partitions") === true
    dbs.contains("nested") === true
  }

  // 3. Start Peer Server:

  // via localhost
  // bin/run -m datomic.peer-server -h localhost -p 8998 -a k,s -d txCount,datomic:dev://txCount -d coretests,datomic:dev://coretests -d bidirectional,datomic:dev://bidirectional -d partitions,datomic:dev://partitions -d nested,datomic:dev://nested

  // or

  // in-mem
  // bin/run -m datomic.peer-server -a k,s -d txCount,datomic:mem://txCount -d coretests,datomic:mem://coretests -d bidirectional,datomic:mem://bidirectional -d partitions,datomic:mem://partitions -d nested,datomic:mem://nested


}
