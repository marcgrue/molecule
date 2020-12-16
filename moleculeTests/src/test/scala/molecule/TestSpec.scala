package molecule

import _root_.datomic.Peer
import datomicScala.client.api.sync.{Client, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, System}
import molecule.core.util.testing.MoleculeSpec
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.setup.CoreData
import molecule.testing.TestPeerServer
import molecule.tests.core.base.schema.CoreTestSchema
import molecule.tests.core.bidirectionals.schema.BidirectionalSchema
import molecule.tests.core.nested.schema.NestedSchema
import molecule.tests.core.schemaDef.schema.PartitionTestSchema
import molecule.tests.examples.datomic.dayOfDatomic.SocialNewsData
import molecule.tests.examples.datomic.dayOfDatomic.schema._
import molecule.tests.examples.datomic.mbrainz.schema.{MBrainzSchema, MBrainzSchemaLowerToUpper}
import molecule.tests.examples.datomic.seattle.SeattleData
import molecule.tests.examples.datomic.seattle.schema.SeattleSchema
import molecule.tests.examples.gremlin.gettingStarted.schema.{ModernGraph1Schema, ModernGraph2Schema}
import moleculeBuildInfo.BuildInfo._
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}
import scala.jdk.CollectionConverters._

class TestSpec extends MoleculeSpec with CoreData {
  sequential
  var system: System    = DatomicPeer
  var client: Client    = null // set in setup
  val heavyInputTesting = false
  var setupException    = Option.empty[Throwable]
  var basisT: Long      = 0L
  def basisTx: Long = Peer.toTx(basisT).asInstanceOf[Long]

  val datomicHome = datomicProtocol match {
    case "dev"  => datomicPath + "/datomic-pro-" + datomicPro
    case "free" => datomicPath + "/datomic-free-" + datomicFree
  }

  // What systems to test
  // 1: Peer   2: Peer-server   3: Dev-local
  var tests = 123
  override def map(fs: => Fragments): Fragments = {
    val show = fs.mapDescription(d => Text(s"$system: " + d.show))
    tests.toString.chars.iterator().asScala.map(_.toInt).map {
      case 49 => step(setupPeer()) ^ show
      case 50 => step(setupPeerServer()) ^ show
      case 51 => step(setupDevLocal()) ^ show
    }.reduce(_ ^ _)
  }

  def setupPeer(): Unit = {
    system = DatomicPeer
  }

  def setupPeerServer(): Unit = {
    system = DatomicPeerServer
    try {
      client = Datomic.clientPeerServer("k", "s", "localhost:8998")
    } catch {
      case e: Throwable => setupException = Some(e)
    }
  }
  def setupDevLocal(): Unit = {
    system = DatomicDevLocal
    try {
      client = Datomic.clientDevLocal(
        // "system" - folder where samples reside
        "datomic-samples",
        // "storage-dir" - absolute path to where "system" is
        datomicHome
      )
    } catch {
      case e: Throwable => setupException = Some(e)
    }
  }

  def getConn(
    schema: SchemaTransaction,
    db: String,
    recreateDb: Boolean = true,
    uri: String = "",
    protocol: String = ""
  ): Conn = {
    // Throw potential setup error
    setupException.fold(())(throw _)
    system match {
      case DatomicPeer =>
        if (recreateDb)
          Datomic_Peer.recreateDbFrom(schema)
        else
          Datomic_Peer.connect(uri, protocol)

      case DatomicPeerServer =>
        if (recreateDb) {
          val (conn, newBasisT) = TestPeerServer.getCleanPeerServerConn(
            client, db, schema, basisT
          )
          basisT = newBasisT
          conn
        } else
          Datomic_Client(client).connect(db)

      case DatomicDevLocal =>
        if (recreateDb)
          Datomic_Client(client).recreateDbFrom(schema, db)
        else
          Datomic_Client(client).connect(db)

      // case DatomicCloud      =>
      //   Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  // Entry points, coretests
  class CoreSetup extends Scope {
    implicit val conn: Conn = getConn(CoreTestSchema, "coretests")
  }
  class BidirectionalSetup extends Scope {
    implicit val conn = getConn(BidirectionalSchema, "bidirectional")
  }
  class PartitionSetup extends Scope {
    implicit val conn = getConn(PartitionTestSchema, "partitions")
  }
  class NestedSetup extends Scope {
    implicit val conn = getConn(NestedSchema, "nested")
  }

  // Entry points, examples
  class AggregateSetup extends Scope {
    implicit val conn = getConn(AggregatesSchema, "aggregates")
  }
  class SocialNewsSetup extends SocialNewsData(
    getConn(SocialNewsSchema, "socialNews")) with Scope

  class GraphSetup extends Scope {
    implicit val conn = getConn(GraphSchema, "graph")
  }
  class Graph2Setup extends Scope {
    implicit val conn = getConn(Graph2Schema, "graph2")
  }
  class ModernGraph1Setup extends Scope {
    implicit val conn = getConn(ModernGraph1Schema, "modernGraph1")
  }
  class ModernGraph2Setup extends Scope {
    implicit val conn = getConn(ModernGraph2Schema, "modernGraph2")
  }
  class ProductsSetup extends Scope {
    implicit val conn = getConn(ProductsOrderSchema, "productsOrder")
  }
  class SeattleSetup extends SeattleData(
    getConn(SeattleSchema, "seattle")) with Scope

  class MBrainzSetup extends Scope {
    val dbName = if (system == DatomicDevLocal)
      "mbrainz-subset" // dev-local
    else
      "mbrainz-1968-1973" // peer and peer-server
    implicit val conn = getConn(MBrainzSchema,
      dbName,
      false, // don't recreate db
      "localhost:4334/mbrainz-1968-1973", // peer uri to transactor
      datomicProtocol
    )

    import molecule.datomic.api.out1._

    if (Schema.a(":Artist/name").get.isEmpty) {
      // Add uppercase-namespaced attribute names so that we can access the externally
      // transacted lowercase names with uppercase names of the molecule code.
      println("Converting nss from lower to upper..")
      conn.transact(MBrainzSchemaLowerToUpper.namespaces)
    }
  }
}
