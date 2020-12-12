package molecule.examples

import molecule.core.util.testing.TxCountSchema
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.examples.dayOfDatomic.schema._
import molecule.examples.gremlin.schema.{ModernGraph1Schema, ModernGraph2Schema}
import molecule.examples.mbrainz.schema.MBrainzSchema
import molecule.examples.seattle.schema.SeattleSchema
import org.specs2.mutable.Specification

class SetupPeerServerDbs extends Specification {
  sequential

  // 1. Stop Peer Server process (ctrl-c), if running.
  // 2. Start transactor

  // 3. Run test here to create databases needed for tests
  "Create databases for peer-server" >> {

    val pro      = true
    val protocol = if (pro) "dev" else "free"

    Datomic_Peer.recreateDbFrom(TxCountSchema, "localhost:4334/txCount", protocol)
    Datomic_Peer.recreateDbFrom(AggregatesSchema, "localhost:4334/aggregates", protocol)
    Datomic_Peer.recreateDbFrom(SocialNewsSchema, "localhost:4334/socialNews", protocol)
    Datomic_Peer.recreateDbFrom(GraphSchema, "localhost:4334/graph", protocol)
    Datomic_Peer.recreateDbFrom(Graph2Schema, "localhost:4334/graph2", protocol)
    Datomic_Peer.recreateDbFrom(ModernGraph1Schema, "localhost:4334/modernGraph1", protocol)
    Datomic_Peer.recreateDbFrom(ModernGraph2Schema, "localhost:4334/modernGraph2", protocol)
    Datomic_Peer.recreateDbFrom(ProductsOrderSchema, "localhost:4334/productsOrder", protocol)
    Datomic_Peer.recreateDbFrom(SeattleSchema, "localhost:4334/seattle", protocol)
    Datomic_Peer.recreateDbFrom(MBrainzSchema, "localhost:4334/mbrainz-1968-1973", protocol)

    val dbs = Datomic_Peer.getDatabaseNames(protocol)

    dbs.contains("txCount") === true
    dbs.contains("aggregates") === true
    dbs.contains("socialNews") === true
    dbs.contains("graph") === true
    dbs.contains("graph2") === true
    dbs.contains("modernGraph1") === true
    dbs.contains("modernGraph2") === true
    dbs.contains("productsOrder") === true
    dbs.contains("seattle") === true
    dbs.contains("mbrainz-1968-1973") === true
  }

  // 4. Start Peer Server:

  /*

    // via localhost
    bin/run -m datomic.peer-server -h localhost -p 8998 -a k,s \
    -d txCount,datomic:dev://txCount \
    -d aggregates,datomic:dev://aggregates \
    -d socialNews,datomic:dev://socialNews \
    -d graph,datomic:dev://graph \
    -d graph2,datomic:dev://graph2 \
    -d modernGraph1,datomic:dev://modernGraph1 \
    -d modernGraph2,datomic:dev://modernGraph2 \
    -d productsOrder,datomic:dev://productsOrder \
    -d seattle,datomic:dev://seattle \
    -d mbrainz-1968-1973,datomic:dev://mbrainz-1968-1973


    // or

    // in-mem
    bin/run -m datomic.peer-server -a k,s \
    -d txCount,datomic:mem://txCount \
    -d aggregates,datomic:mem://aggregates \
    -d socialNews,datomic:mem://socialNews \
    -d graph,datomic:mem://graph \
    -d graph2,datomic:mem://graph2 \
    -d modernGraph1,datomic:mem://modernGraph1 \
    -d modernGraph2,datomic:mem://modernGraph2 \
    -d productsOrder,datomic:mem://productsOrder \
    -d seattle,datomic:mem://seattle \
    -d mbrainz-1968-1973,datomic:mem://mbrainz-1968-1973

   */
}