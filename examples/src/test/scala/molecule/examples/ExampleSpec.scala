package molecule.examples

import datomic.Peer
import datomicScala.client.api.sync.{Client, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, System}
import molecule.core.util.testing.MoleculeSpec
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.examples.dayOfDatomic.schema._
import molecule.examples.dayOfDatomic.SocialNewsData
import molecule.examples.gremlin.schema.{ModernGraph1Schema, ModernGraph2Schema}
import molecule.examples.mbrainz.schema.MBrainzSchema
import molecule.examples.seattle.SeattleData
import molecule.examples.seattle.schema.SeattleSchema
import molecule.testing.TestPeerServer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}

class ExampleSpec extends MoleculeSpec {
  sequential
  var system: System    = DatomicPeer
  var client: Client    = null // set in setup
  var peerOnly          = false
  var devLocalOnly      = false
  var peerServerOnly    = false
  var omitPeer          = false
  var omitPeerServer    = false
  var omitDevLocal      = false
  val heavyInputTesting = false

  // Catch error from setup (suppressed by specs2 during setup)
  var setupException = Option.empty[Throwable]
  var basisT: Long   = 0L
  def basisTx: Long = Peer.toTx(basisT).asInstanceOf[Long]

  override def map(fs: => Fragments): Fragments = {
    if (peerOnly) {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (devLocalOnly) {
      step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (peerServerOnly) {
      step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (omitPeer) {
      step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (omitPeerServer) {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (omitDevLocal) {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    }
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
        "/Users/mg/lib/datomic/datomic-pro-1.0.6222"
      )
      //            client = Datomic.clientDevLocal("samples", "/Users/mg/lib/datomic/datomic-pro-1.0.6222")
//      client.deleteDatabase("mbrainz-1968-1973")
      //                  client.createDatabase("mbrainz-1968-1973")
      println(client.listDatabases())
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

  // Entry points
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
      "dev" // protocol for :pro - could be "free" for Datomic :free
    )
  }
}
