package molecule.coretests.util

import datomic.Peer
import datomicScala.client.api.sync.{Client, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util._
import molecule.core.util.testing.MoleculeSpec
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.nested.schema.NestedSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.testing.TestPeerServer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}


abstract class CoreSpec extends MoleculeSpec with CoreData {
  sequential
  var system: System    = DatomicPeer
  var client: Client    = null // set in setup
  var peerOnly          = false
  var devLocalOnly      = false
  var peerServerOnly    = false
  var omitPeerServer    = false
  var omitDevLocal      = false
  val heavyInputTesting = false
  var setupException    = Option.empty[Throwable]
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
      case e: Throwable =>
        // Catch error from setup (suppressed by specs2 during setup)
        setupException = Some(e)
    }
  }

  def setupDevLocal(): Unit = {
    system = DatomicDevLocal
    client = Datomic.clientDevLocal("Some system name")
  }

  def freshConn(schema: SchemaTransaction, dbIdentifier: String): Conn = {
    // Throw potential setup error
    setupException.fold(())(throw _)
    system match {
      case DatomicPeer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DatomicPeerServer =>
        val (conn, newBasisT) = TestPeerServer.getCleanPeerServerConn(
          client, dbIdentifier, schema, basisT
        )
        basisT = newBasisT
        conn

      case DatomicDevLocal =>
        Datomic_Client(client).recreateDbFrom(schema, dbIdentifier)

      // case DatomicCloud      =>
      //   Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  // Entry points
  class CoreSetup extends Scope {
    implicit val conn: Conn = freshConn(CoreTestSchema, "coretests")
  }
  class BidirectionalSetup extends Scope {
    implicit val conn = freshConn(BidirectionalSchema, "bidirectional")
  }
  class PartitionSetup extends Scope {
    implicit val conn = freshConn(PartitionTestSchema, "partitions")
  }
  class NestedSetup extends Scope {
    implicit val conn = freshConn(NestedSchema, "nested")
  }
}