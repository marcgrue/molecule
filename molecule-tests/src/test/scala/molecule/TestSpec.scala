package molecule

import _root_.datomic.Peer
import molecule.core.data.SchemaTransaction
import molecule.core.util.testing.MoleculeTestHelper
import molecule.core.util.{System, SystemDevLocal, SystemPeer, SystemPeerServer}
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.{Datomic_DevLocal, Datomic_PeerServer}
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.setup.{CleanPeerServer, SpecHelpers}
import molecule.setup.core.CoreData
import molecule.setup.examples.datomic.dayOfDatomic.SocialNewsData
import molecule.setup.examples.datomic.seattle.SeattleData
import molecule.tests.core.base.schema.CoreTestSchema
import molecule.tests.core.bidirectionals.schema.BidirectionalSchema
import molecule.tests.core.ref.schema.{NestedSchema, SelfJoinSchema}
import molecule.tests.core.schemaDef.schema.PartitionTestSchema
import molecule.tests.examples.datomic.dayOfDatomic.schema._
import molecule.tests.examples.datomic.mbrainz.schema.{MBrainzSchema, MBrainzSchemaLowerToUpper}
import molecule.tests.examples.datomic.seattle.schema.SeattleSchema
import molecule.tests.examples.gremlin.gettingStarted.schema.{ModernGraph1Schema, ModernGraph2Schema}
import moleculeBuildInfo.BuildInfo._
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}


class TestSpec extends Specification with MoleculeTestHelper with CoreData {
  sequential
  var system    : System             = SystemPeer
  var peer      : Datomic_Peer       = null
  var peerServer: Datomic_PeerServer = null
  var devLocal  : Datomic_DevLocal   = null
  val heavyInputTesting              = false
  var setupException                 = Option.empty[Throwable]
  var basisT    : Long               = 0L
  def basisTx: Long = Peer.toTx(basisT).asInstanceOf[Long]

  // What systems to test (can be a single, two or three systems in any order)
  // Set this variable in a test to specify which system should run it
  // 0 1: Peer   2: Peer-server   3: Dev-local
  var tests = 1
  def addSystem(fs: => Fragments, system: String) = fs.mapDescription {
    case Text(t)    => Text(s"$system        $t")
    case otherDescr => otherDescr
  }
  override def map(fs: => Fragments): Fragments = {
    tests.toString.getBytes.map {
      case 49 /* 1 */ => step(setupPeer()) ^ addSystem(fs, "peer       ")
      case 50 /* 2 */ => step(setupPeerServer()) ^ addSystem(fs, "peer-server")
      case 51 /* 3 */ => step(setupDevLocal()) ^ addSystem(fs, "dev-local  ")
    }.reduce(_ ^ _)
  }

  def setupPeer(): Unit = {
    system = SystemPeer
    peer = Datomic_Peer
  }
  def setupPeerServer(): Unit = {
    system = SystemPeerServer
    try {
      peerServer = Datomic_PeerServer("k", "s", "localhost:8998")
    } catch {
      case e: Throwable => setupException = Some(e)
    }
  }
  def setupDevLocal(): Unit = {
    system = SystemDevLocal
    try {
      devLocal = Datomic_DevLocal("datomic-samples", datomicHome)
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
      case SystemPeer =>
        if (recreateDb)
          peer.recreateDbFrom(schema)
        else
          peer.connect(uri, protocol)

      case SystemPeerServer =>
        if (recreateDb) {
          val (conn, newBasisT) = CleanPeerServer.getCleanPeerServerConn(
            peerServer, db, schema, basisT
          )
          basisT = newBasisT
          conn
        } else
          peerServer.connect(db)

      case SystemDevLocal =>
        if (recreateDb)
          devLocal.recreateDbFrom(schema, db)
        else
          devLocal.connect(db)
    }
  }

  // Entry points, core tests
  class CoreSetup extends Scope {
    implicit val conn: Conn = getConn(CoreTestSchema, "m_coretests")
  }
  class BidirectionalSetup extends Scope {
    implicit val conn = getConn(BidirectionalSchema, "m_bidirectional")
  }
  class PartitionSetup extends SpecHelpers with Scope {
    implicit val conn = getConn(PartitionTestSchema, "m_partitions")
  }
  class NestedSetup extends Scope {
    implicit val conn = getConn(NestedSchema, "m_nested")
  }
  class SelfJoinSetup extends Scope {
    implicit val conn = getConn(SelfJoinSchema, "m_selfjoin")
  }

  // Entry points, examples
  class AggregateSetup extends Scope {
    implicit val conn = getConn(AggregatesSchema, "m_aggregates")
  }
  class SocialNewsSetup extends SocialNewsData(
    getConn(SocialNewsSchema, "m_socialNews")) with Scope

  class GraphSetup extends Scope {
    implicit val conn = getConn(GraphSchema, "m_graph")
  }
  class Graph2Setup extends Scope {
    implicit val conn = getConn(Graph2Schema, "m_graph2")
  }
  class ModernGraph1Setup extends Scope {
    implicit val conn = getConn(ModernGraph1Schema, "m_modernGraph1")
  }
  class ModernGraph2Setup extends Scope {
    implicit val conn = getConn(ModernGraph2Schema, "m_modernGraph2")
  }
  class ProductsSetup extends Scope {
    implicit val conn = getConn(ProductsOrderSchema, "m_productsOrder")
  }
  class SeattleSetup extends SeattleData(
    getConn(SeattleSchema, "m_seattle")) with Scope

  class MBrainzSetup extends Scope {
    val dbName = if (system == SystemDevLocal)
      "mbrainz-subset" // dev-local
    else
      "mbrainz-1968-1973" // peer and peer-server
    implicit val conn = getConn(MBrainzSchema,
      dbName,
      false, // don't recreate db
      "localhost:4334/mbrainz-1968-1973", // peer uri to transactor
      //      "free" // if running free transactor
      "dev" // if running pro transactor
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
