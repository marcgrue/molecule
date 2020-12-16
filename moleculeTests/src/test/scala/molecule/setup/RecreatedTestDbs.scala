package molecule.setup

import molecule.core.util.testing.TxCountSchema
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.schema.CoreTestSchema
import molecule.tests.core.bidirectionals.schema.BidirectionalSchema
import molecule.tests.core.nested.schema.NestedSchema
import molecule.tests.core.schemaDef.schema.PartitionTestSchema
import molecule.tests.examples.datomic.dayOfDatomic.schema._
import molecule.tests.examples.datomic.mbrainz.schema.MBrainzSchema
import molecule.tests.examples.datomic.seattle.schema.SeattleSchema
import molecule.tests.examples.gremlin.gettingStarted.schema._
import moleculeBuildInfo.BuildInfo.datomicProtocol


object RecreatedTestDbs extends App {

  Datomic_Peer.recreateDbFrom(TxCountSchema, "localhost:4334/txCount", datomicProtocol)

  Datomic_Peer.recreateDbFrom(CoreTestSchema, "localhost:4334/coretests", datomicProtocol)
  Datomic_Peer.recreateDbFrom(BidirectionalSchema, "localhost:4334/bidirectional", datomicProtocol)
  Datomic_Peer.recreateDbFrom(PartitionTestSchema, "localhost:4334/partitions", datomicProtocol)
  Datomic_Peer.recreateDbFrom(NestedSchema, "localhost:4334/nested", datomicProtocol)

  Datomic_Peer.recreateDbFrom(AggregatesSchema, "localhost:4334/aggregates", datomicProtocol)
  Datomic_Peer.recreateDbFrom(SocialNewsSchema, "localhost:4334/socialNews", datomicProtocol)
  Datomic_Peer.recreateDbFrom(GraphSchema, "localhost:4334/graph", datomicProtocol)
  Datomic_Peer.recreateDbFrom(Graph2Schema, "localhost:4334/graph2", datomicProtocol)
  Datomic_Peer.recreateDbFrom(ModernGraph1Schema, "localhost:4334/modernGraph1", datomicProtocol)
  Datomic_Peer.recreateDbFrom(ModernGraph2Schema, "localhost:4334/modernGraph2", datomicProtocol)
  Datomic_Peer.recreateDbFrom(ProductsOrderSchema, "localhost:4334/productsOrder", datomicProtocol)
  Datomic_Peer.recreateDbFrom(SeattleSchema, "localhost:4334/seattle", datomicProtocol)

  // Mock mbrainz db visible to dev-local
  // Peer and Peer Server will use restored mbranz-1968-1973
  Datomic_Peer.recreateDbFrom(MBrainzSchema, "localhost:4334/mbrainz-subset", datomicProtocol)



}