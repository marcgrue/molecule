package molecule.coretests.util

import clojure.lang.ILookup
import datomic.Util.{list, read}
import datomicClient.{ClojureBridge, Invoke}
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.schema.SchemaTransaction
import molecule.core.util._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.api.in1_out6._
import molecule.datomic.base.facade.Conn
import molecule.datomic.client.facade.Datomic_Client
import molecule.datomic.peer.facade.Datomic_Peer
import org.specs2.specification.Scope
import org.specs2.specification.core.{Fragments, Text}
import scala.collection.mutable

abstract class CoreSpec extends MoleculeSpec with CoreData with ClojureBridge {
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

  lazy val readE = read(":e")
  lazy val readA = read(":a")
  lazy val readV = read(":v")
  lazy val readTx = read(":tx")
  lazy val readOp = read(":added")

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
        val cl = Datomic_Client(client)
        implicit val conn = cl.connect(dbIdentifier)
        val log = conn.clientConn.txRange(None, None)

        // Check only once per test file
        if (installSchema && log.isEmpty) {
          println("Installing Peer Server schema...")
          if (schema.partitions.size() > 0)
            conn.transact(cl.allowedClientDefinitions(schema.partitions))
          conn.transact(cl.allowedClientDefinitions(schema.namespaces))
          installSchema = false

          // updated log
          conn.clientConn.txRange(None, None)
        } else {
          // Since we can't recreate a Peer Server from here, we apply this
          // hack of simply retracting all entities created in previous tests.
          val eids   = mutable.Set[Long]()
          val datoms = Invoke.datoms(conn.db.getDatomicDb, ":eavt", list(), limit = -1)
            .asInstanceOf[java.lang.Iterable[_]]
          datoms.forEach { d =>
            val da     = d.asInstanceOf[ILookup]
            val eid    = da.valAt(readE).toString.toLong
            val attrId = da.valAt(readA).toString.toInt

//            val v = da.valAt(readV).toString
//            val tx = da.valAt(readTx).toString
//            val op = da.valAt(readOp).toString
            // Exclude txs and enum idents

//            println(s"$eid  $attrId  $v   $tx   $op")

            // todo: can't hardcode id switch...
            if (eid > 15194139534365L && attrId != 10) {
              eids += eid
            }
          }
//          println("======= " + eids.size)
          if (eids.nonEmpty) {
            retract(eids)
          }
        }
        conn

      case DatomicDevLocal =>
        Datomic_Client(client).recreateDbFrom(schema, dbIdentifier, false)

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
}