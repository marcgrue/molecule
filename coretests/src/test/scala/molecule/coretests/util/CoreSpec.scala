package molecule.coretests.util

import datomic.Peer
import datomicClient.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.{Client, Connection, Datomic}
import molecule.core.ast.transactionModel.{Retract, RetractEntity, Statement}
import molecule.core.schema.SchemaTransaction
import molecule.core.util._
import molecule.coretests.bidirectionals.schema.BidirectionalSchema
import molecule.coretests.schemaDef.schema.PartitionTestSchema
import molecule.coretests.util.dsl.txCount._
import molecule.coretests.util.schema.{CoreTestSchema, TxCountSchema}
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

  var basisT: Long = 0L
  def basisTx: Long = Peer.toTx(basisT).asInstanceOf[Long]

  // Do or skip looping input tests that take a few minutes
  val heavyInputTesting = false

  var peerOnly       = false
  var devLocalOnly   = false
  var peerServerOnly = true
  //  var peerServerOnly = false
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
        val cl          = Datomic_Client(client)
        val dataConn    = cl.connect(dbIdentifier)
        val txCountConn = cl.connect("txCount")
        val log         = dataConn.clientConn.txRange(Some(1000), Some(1002))
        val empty       = if (installSchema) log.isEmpty else false

        // Check only once per test file
        if (installSchema && empty) {
          println("Installing Peer Server schema...")
          if (schema.partitions.size() > 0)
            dataConn.transact(cl.allowedClientDefinitions(schema.partitions))
          dataConn.transact(cl.allowedClientDefinitions(schema.namespaces))
          basisT = dataConn.db.t + 1
          txCountConn.transact(cl.allowedClientDefinitions(TxCountSchema.namespaces))
          TxCount.db(dbIdentifier).basisT(basisT).save(txCountConn)
          installSchema = false

        } else {
          basisT = TxCount.db_(dbIdentifier).basisT.get(txCountConn).head
          //          println(s"basisT: $dbIdentifier  " + basisT)

          // Since we can't recreate a Peer Server from here, we simply retract
          // all datoms created in previous tests.
          val cleanupRetractions = mutable.Set.empty[Statement]
          Log(Some(basisT)).e.a.v.get(dataConn).foreach {
            case (_, ":db/txInstant", _)            => // Can't change tx time
            case (_, ":TxCount/basisT", _)          => // Keep basisT
            case (tx, a, v) if tx < 17000000000000L => cleanupRetractions += Retract(tx, a, v)
            case (e, _, _)                          => cleanupRetractions += RetractEntity(e)
          }
          val retractionCount = cleanupRetractions.size

          //          Log(Some(basisT)).t.op.e.a.v.get(dataConn) foreach println
          //          println("-------------------")
          //          cleanupRetractions.toSeq.sortBy(_.e.toString.toLong) foreach println
          //          println("-------------------")
          //          println(s"Retraction count: $retractionCount")

          if (retractionCount > 100)
            throw new RuntimeException(s"Unexpectedly gathered $retractionCount retractions...")

          if (retractionCount > 0) {
            val txd = dataConn.transact(Seq(cleanupRetractions.toSeq))
            //            println(txd)

            val tx = TxCount.e.db_(dbIdentifier).get(txCountConn).head
            basisT = dataConn.db.t + 1
            TxCount(tx).basisT(basisT).update(txCountConn)
            //            println(s"basisT: $tx  $basisT  " + TxCount.db(dbIdentifier).basisT.get(txCountConn).head)
          } else {
            //            println("No rectractions necessary (could be a reload) ...")
          }
        }
        dataConn

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