package molecule.examples

import datomicClient._
import datomicClient.anomaly.CognitectAnomaly
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util.{DatomicDevLocal, DatomicPeer, DatomicPeerServer, MoleculeSpec, System}
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.devLocal.facade.Datomic_DevLocal
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.examples.dayOfDatomic.schema.{AggregatesSchema, GraphSchema, ProductsOrderSchema, SocialNewsSchema}
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}

class ExampleSpec  extends MoleculeSpec with ClojureBridge {
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
        println(e.getMessage)
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
  class AggregateSetup extends Scope {
    implicit val conn: Conn = recreatedDbConn(AggregatesSchema)
  }
  class SocialSetup extends Scope {
    implicit val conn = recreatedDbConn(SocialNewsSchema)
  }
  class GraphSetup extends Scope {
    implicit val conn = recreatedDbConn(GraphSchema)
  }
  class ProductsSetup extends Scope {
    implicit val conn = recreatedDbConn(ProductsOrderSchema)
  }

}
