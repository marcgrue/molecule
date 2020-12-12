package molecule.testing

import datomicScala.client.api.sync.Client
import molecule.core.ast.transactionModel.{Retract, RetractEntity, Statement}
import molecule.core.schema.SchemaTransaction
import molecule.core.util.testing.{TxCount, TxCountSchema}
import molecule.datomic.client.facade.{Conn_Client, Datomic_Client}
import scala.collection.mutable
import molecule.datomic.api.out3._


object TestPeerServer {

  def getCleanPeerServerConn(
    client: Client,
    dbIdentifier: String,
    schema: SchemaTransaction,
    basisT0: Long
  ): (Conn_Client, Long) = {
    var basisT = basisT0
    val cl          = Datomic_Client(client)
    val dataConn    = cl.connect(dbIdentifier)
    val txCountConn = cl.connect("txCount")
    val log         = dataConn.clientConn.txRange(Some(1000), Some(1002))

    if (log.isEmpty) {
      println("Installing Peer Server schema...")
      if (schema.partitions.size() > 0)
        dataConn.transact(cl.allowedClientDefinitions(schema.partitions))
      dataConn.transact(cl.allowedClientDefinitions(schema.namespaces))
      basisT = dataConn.db.t + 1
      txCountConn.transact(cl.allowedClientDefinitions(TxCountSchema.namespaces))
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
