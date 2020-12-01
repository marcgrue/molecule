package molecule.coretests.util

import datomic.Util.{list, read}
import datomicClient.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.api.TxFunctions.buildTxFnInstall
import molecule.core.schema.SchemaTransaction
import molecule.core.util._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}
import molecule.datomic.api.in1_out6._
import molecule.coretests.util.dsl.coreTest._

class CoreSpec extends MoleculeSpec with CoreData with ClojureBridge {
  sequential

  var system     : System      = DatomicPeer
  var client     : Client      = null // set in setup
  var asyncClient: AsyncClient = null // set in setup
  var connection : Connection  = null // set in setup

  // Do or skip looping input tests that take a few minutes
  val heavyInputTesting = false

  var peerOnly       = false
  var devLocalOnly   = false
//  var peerServerOnly = true
  var peerServerOnly = false
  var omitPeerServer = false

  var setupException = Option.empty[Throwable]
  var installSchema  = true // set to true to initiate Peer Server schema installation

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
//      step(setupPeer()) ^ fs.mapDescription(d => Text(s"$system: " + d.show)) ^
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

  def freshConn(
    schema: SchemaTransaction,
    dbIdentifier: String = ""
  ): Conn = {

    // Throw potential setup error
    setupException.fold(())(throw _)

    system match {
      case DatomicPeer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DatomicPeerServer =>
        val cl   = Datomic_Client(client)
        val conn = cl.connect(dbIdentifier)
        val log  = conn.clientConn.txRange(None, None)

        // Check only once per test file
        val log2 = if (installSchema && log.isEmpty) {
          println("Installing Peer Server schema...")
          if (schema.partitions.size() > 0)
            conn.transact(cl.allowedClientDefinitions(schema.partitions))
          conn.transact(cl.allowedClientDefinitions(schema.namespaces))
          installSchema = false

          // updated log
          conn.clientConn.txRange(None, None)
        } else log

        if (log2.size > 2) { // partition + attributes transactions (could be 1)
          throw new RuntimeException(
            "Live Peer Server seems to have been modified unexpectedly by a test " +
              "that has switched back to live data. Please find and modify this " +
              "test and restart the Peer server process to test again.")
        }

        // Always use an empty withDb
        conn.testDbAsOfNow
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
    implicit val conn = freshConn(PartitionTestSchema, "coretests")
  }
}