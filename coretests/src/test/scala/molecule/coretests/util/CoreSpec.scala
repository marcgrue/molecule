package molecule.coretests.util

import datomicClojure.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import datomicScala.CognitectAnomaly
import molecule.core.schema.SchemaTransaction
import molecule.core.util.MoleculeSpec
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.devLocal.facade.Datomic_DevLocal
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.datomic.peer.facade.Datomic_Peer.recreateDbFrom
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}


class CoreSpec extends MoleculeSpec with CoreData with ClojureBridge {
  sequential

  sealed trait System
  case object Peer extends System
  case object PeerServer extends System
  case object DevLocal extends System
  //  case object Cloud extends System // DevLocal should cover this

  var system     : System      = Peer
  var client     : Client      = null // set in setup
  var asyncClient: AsyncClient = null // set in setup
  var connection : Connection  = null // set in setup

  // Do or skip looping input tests that take a few minutes
  val heavyInputTesting = false

  var peerOnly = false

  override def map(fs: => Fragments): Fragments = {
    if (peerOnly) {
      step(setupPeer()) ^
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
    system = Peer
  }

  def setupPeerServer(): Unit = {
    system = PeerServer
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
    system = DevLocal
    client = Datomic.clientDevLocal("coretests")
    //    asyncClient = AsyncDatomic.clientDevLocal("coretests")
  }

  def recreatedDbConn(
    schema: SchemaTransaction,
    dbIdentifier: String = ""
  ) = {
    system match {
      case Peer =>
        Datomic_Peer.recreateDbFrom(schema)

      case PeerServer =>
        Datomic_Peer.recreateDbFrom(schema)

      case DevLocal =>
        Datomic_DevLocal(client).recreateDbFrom(CoreTestSchema_DevLocal, dbIdentifier)

      //      case Cloud      =>
      //        Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  class CoreSetup extends Scope {
    // Entry point for Molecule to all systems
    implicit val conn: Conn = recreatedDbConn(CoreTestSchema)
  }

  class BidirectionalSetup extends Scope {
    implicit val conn = recreateDbFrom(BidirectionalSchema)
  }
}