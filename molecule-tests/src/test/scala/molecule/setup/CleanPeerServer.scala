package molecule.setup

import molecule.datomic.base.ast.transactionModel.{Retract, RetractEntity, Statement}
import molecule.core.data.SchemaTransaction
import molecule.core.util.testing.{TxCount, TxCountSchema}
import molecule.datomic.api.out3._
import molecule.datomic.client.facade.{Conn_Client, Datomic_PeerServer}
import molecule.datomic.peer.facade.Datomic_Peer
import moleculeBuildInfo.BuildInfo.datomicProtocol
import scala.collection.mutable

// Hack to empty a database on a Peer Server since we can't recreate it without
// restarting the Peer Server which is not an option running big test suites.

object CleanPeerServer {

  def getCleanPeerServerConn(
    peerServer: Datomic_PeerServer,
    dbIdentifier: String,
    schema: SchemaTransaction,
    basisT0: Long
  ): (Conn_Client, Long) = {
    var basisT   = basisT0
    val dataConn = peerServer.connect(dbIdentifier)

    // Uncomment and run, if m_txCount database hasn't been created yet
    //    Datomic_Peer.recreateDbFrom(
    //      TxCountSchema,
    //      datomicProtocol, // has to match transactor: 'free' or 'dev' (pro)
    //      "localhost:4334/m_txCount"
    //    )

    val txCountConn = peerServer.connect("m_txCount")
    val log         = dataConn.clientConn.txRange(Some(1000), Some(1002))

    if (log.isEmpty) {
      println("Installing Peer Server schema...")
      if (schema.partitions.size() > 0)
        dataConn.transact(peerServer.allowedClientDefinitions(schema.partitions))
      dataConn.transact(peerServer.allowedClientDefinitions(schema.namespaces))
      basisT = dataConn.db.t + 1
      txCountConn.transact(peerServer.allowedClientDefinitions(TxCountSchema.namespaces))
      TxCount.db(dbIdentifier).basisT(basisT).save(txCountConn)

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

      if (retractionCount > 1000)
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
    (dataConn, basisT)
  }
}
