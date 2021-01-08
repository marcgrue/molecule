package molecule.setup

import molecule.core.util.testing.{MoleculeTestHelper, TxCountSchema}
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.schema.CoreTestSchema
import molecule.tests.core.bidirectionals.schema.BidirectionalSchema
import molecule.tests.core.ref.schema.{NestedSchema, SelfJoinSchema}
import molecule.tests.core.schemaDef.schema.PartitionTestSchema
import molecule.tests.examples.datomic.dayOfDatomic.schema._
import molecule.tests.examples.datomic.seattle.schema.SeattleSchema
import molecule.tests.examples.gremlin.gettingStarted.schema._
import moleculeBuildInfo.BuildInfo._
import org.specs2.mutable.Specification


class RecreateTestDbs extends Specification with MoleculeTestHelper {


  "Recreate test dbs" >> {
    List(
      "m_txCount" -> TxCountSchema,
      "m_coretests" -> CoreTestSchema,
      "m_bidirectional" -> BidirectionalSchema,
      "m_partitions" -> PartitionTestSchema,
      "m_nested" -> NestedSchema,
      "m_selfjoin" -> SelfJoinSchema,
      "m_aggregates" -> AggregatesSchema,
      "m_socialNews" -> SocialNewsSchema,
      "m_graph" -> GraphSchema,
      "m_graph2" -> Graph2Schema,
      "m_modernGraph1" -> ModernGraph1Schema,
      "m_modernGraph2" -> ModernGraph2Schema,
      "m_productsOrder" -> ProductsOrderSchema,
      "m_seattle" -> SeattleSchema,
    ).foreach {
      case (db, schema) =>
        println("Recreating db " + db)
        Datomic_Peer.recreateDbFrom(schema, "localhost:4334/" + db, datomicProtocol)
    }

    ok
  }
}