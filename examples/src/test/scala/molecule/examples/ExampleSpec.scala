package molecule.examples

import datomic.Peer
import datomicClient._
import datomicClient.anomaly.CognitectAnomaly
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, System}
import molecule.core.util.testing.MoleculeSpec
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema, SocialNewsSchema}
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
  var omitPeerServer    = false
  val heavyInputTesting = false
  var setupException    = Option.empty[Throwable]
  var doInstallSchema   = true // set to true to initiate Peer Server schema installation
  var basisT: Long      = 0L
  def basisTx: Long = Peer.toTx(basisT).asInstanceOf[Long]

  override def map(fs: => Fragments): Fragments = {
    if (peerOnly) {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (devLocalOnly) {
      step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (peerServerOnly) {
      step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (omitPeerServer) {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    } else {
      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupPeerServer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show))
    }
  }

  def setupPeer(): Unit = {
    system = DatomicPeer
  }

  def setupPeerServer(): Unit = {
    system = DatomicPeerServer
    try {
      client = Datomic.clientPeerServer("k", "s", "localhost:8998")
      client.connect("coretests")
    } catch {
      case e: Throwable =>
        // Catch error from setup (suppressed by specs2 during setup)
        setupException = Some(e)
    }
  }

  def setupDevLocal(): Unit = {
    system = DatomicDevLocal
    client = Datomic.clientDevLocal("Some system name")
  }

  def freshConn(schema: SchemaTransaction, dbIdentifier: String = "") = {
    // Throw potential setup error
    setupException.fold(())(throw _)
    system match {
      case DatomicPeer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DatomicPeerServer =>
        val (conn, newBasisT) = TestPeerServer.getCleanPeerServerConn(
          client, dbIdentifier, doInstallSchema, schema, basisT
        )
        basisT = newBasisT
        doInstallSchema = false
        conn

      case DatomicDevLocal =>
        Datomic_Client(client).recreateDbFrom(schema, dbIdentifier)

      // case DatomicCloud      =>
      //   Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  // Entry points
  class AggregateSetup extends Scope {
    implicit val conn: Conn = freshConn(AggregatesSchema, "aggregates")
  }
  class SocialSetup extends Scope {
    implicit val conn = freshConn(SocialNewsSchema, "socialNews")
  }
  class GraphSetup extends Scope {
    implicit val conn = freshConn(GraphSchema, "graph")
  }
  class ProductsSetup extends Scope {
    implicit val conn = freshConn(ProductsOrderSchema, "productsOrder")
  }
}
