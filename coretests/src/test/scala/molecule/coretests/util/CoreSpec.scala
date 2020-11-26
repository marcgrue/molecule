package molecule.coretests.util

import datomicClient.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.devLocal.facade.Datomic_DevLocal
import molecule.datomic.peer.facade.Datomic_Peer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}

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
  var peerServerOnly = false

  var setupException = Option.empty[Throwable]

  override def map(fs: => Fragments): Fragments = {
    if (peerOnly) {
      step(setupPeer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (devLocalOnly) {
      step(setupDevLocal()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (peerServerOnly) {
      step(setupPeerServer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
    } else {

      //      step(setupPeer()) ^
      //        fs.mapDescription(d => Text(s"$system: " + d.show))

      //          step(setupDevLocal()) ^
      //            fs.mapDescription(d => Text(s"$system: " + d.show))

      //      step(setupPeer()) ^
      //        fs.mapDescription(d => Text(s"$system: " + d.show)) ^
      //        step(setupDevLocal()) ^
      //        fs.mapDescription(d => Text(s"$system: " + d.show))

      step(setupPeer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupPeerServer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
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
        // Catch error from setup (suppressed during setup)
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
        val devL = Datomic_DevLocal(client)
        val conn = devL.connect(dbIdentifier)
        if (schema.partitions.size() > 0)
          conn.transact(devL.allowedDevLocalDefinitions(schema.partitions))
        conn.transact(devL.allowedDevLocalDefinitions(schema.namespaces))
        conn

      case DatomicDevLocal =>
        Datomic_DevLocal(client).recreateDbFrom(schema, dbIdentifier)

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