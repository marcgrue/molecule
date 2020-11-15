package molecule.coretests.util

import clojure.lang.Keyword
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import datomicScala.CognitectAnomaly
import molecule.core.util.MoleculeSpec
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.datomic.client.devLocal.facade.Datomic_DevLocal
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}
import molecule.core.schema.SchemaTransaction
import molecule.datomic.base.facade.Conn
import scala.jdk.CollectionConverters._

class CoreSpec extends MoleculeSpec with CoreData {

  sealed trait System
  case object Peer extends System
  case object PeerServer extends System

  // Only testing cloud client with dev-local, presuming they behave identically
  case object DevLocal extends System
  //  case object Cloud extends System

  var system    : System     = Peer
  var client    : Client     = null // set in setup
  var connection: Connection = null // set in setup

  override def map(fs: => Fragments): Fragments = {
    step(setupPeer()) ^
      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
      step(setupDevLocal()) ^
      fs.mapDescription(d => Text(s"$system: " + d.show))
    //      fs.mapDescription(d => Text(s"$system: " + d.show)) ^
    //      step(setupPeerServer()) ^
    //      fs.mapDescription(d => Text(s"$system: " + d.show))
  }


  def setupPeer(): Unit = {
    system = Peer
  }

  def setupPeerServer(): Unit = {
    system = PeerServer
    //    client = Datomic.clientPeerServer("myaccesskey", "mysecret", "localhost:8998")
    client = Datomic.clientPeerServer("myaccesskey", "mysecret", "datomic:mem://coretests")
    connection = try {
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
  }

  def recreatedDbConn(
    schema: SchemaTransaction = CoreTestSchema,
    dbIdentifier: String = ""
  ) = {
    system match {
      case Peer =>
        Datomic_Peer.recreateDbFrom(schema, dbIdentifier)

      case PeerServer =>
        Datomic_Peer.recreateDbFrom(schema, dbIdentifier)

      case DevLocal =>
        Datomic_DevLocal(client).recreateDbFrom(CoreTestSchema_DevLocal, dbIdentifier)

      //      case Cloud      =>
      //        Datomic_Peer.recreateDbFrom(schema, dbIdentifier)
    }
  }

  class CoreSetup extends Scope {
    // Entry point for Molecule to all systems
    implicit val conn: Conn = recreatedDbConn()
  }
}