package molecule.coretests.util

import datomicClojure.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import datomicScala.CognitectAnomaly
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

  var peerOnly     = false
  var devLocalOnly = false

  override def map(fs: => Fragments): Fragments = {
    if (peerOnly) {
      step(setupPeer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
    } else if (devLocalOnly) {
      step(setupDevLocal()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))
    } else {

      //      step(setupPeer()) ^
      //        fs.mapDescription(d => Text(s"$system: " + d.show))

      //          step(setupDevLocal()) ^
      //            fs.mapDescription(d => Text(s"$system: " + d.show))

      step(setupPeer()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show)) ^
        step(setupDevLocal()) ^
        fs.mapDescription(d => Text(s"$system: " + d.show))

      //    step(setupPeer()) ^
      //      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
      //      step(setupDevLocal()) ^
      //      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
      //      step(setupPeerServer()) ^
      //      fs.mapDescription(d => Text(s"$system: " + d.show))
    }
  }


  def setupPeer(): Unit = {
    system = DatomicPeer
  }

  def setupPeerServer(): Unit = {
    system = DatomicPeerServer
    //    client = Datomic.clientPeerServer("myaccesskey", "mysecret", "localhost:8998")
    client = Datomic.clientPeerServer("myaccesskey", "mysecret", "datomic:mem://coretests")
    //    asyncClient = AsyncDatomic.clientPeerServer("myaccesskey", "mysecret", "datomic:mem://coretests")
    connection = try {
      //      asyncClient.connect("coretests")
      client.connect("coretests")
    } catch {
      case e: CognitectAnomaly =>
        println(e)
        println(e.msg)
        throw e
      case t: Throwable        => throw t
    }
  }

  def setupDevLocal(): Unit = {
    system = DatomicDevLocal
    client = Datomic.clientDevLocal("coretests")
    //    asyncClient = AsyncDatomic.clientDevLocal("coretests")
  }

  def recreatedDbConn(
    schema: SchemaTransaction,
    dbIdentifier: String = ""
  ) = {
    system match {
      case DatomicPeer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DatomicPeerServer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DatomicDevLocal =>
        Datomic_DevLocal(client).recreateDbFrom(schema, dbIdentifier)

      // case DatomicCloud      =>
      //   Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  // Entry points
  class CoreSetup extends Scope {
    implicit val conn: Conn = recreatedDbConn(CoreTestSchema)
  }
  class BidirectionalSetup extends Scope {
    implicit val conn = recreatedDbConn(BidirectionalSchema)
  }
  class PartitionSetup extends Scope {
    implicit val conn = recreatedDbConn(PartitionTestSchema)
  }
}