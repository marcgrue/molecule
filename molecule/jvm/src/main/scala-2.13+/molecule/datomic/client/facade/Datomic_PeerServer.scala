package molecule.datomic.client.facade

import datomic.Util.read
import datomicScala.client.api.async.AsyncDatomic
import datomicScala.client.api.sync.Datomic
import scala.jdk.CollectionConverters._
import scala.sys.process._


/** Datomic facade for peer-server.
 *
 * @param accessKey
 * @param secret
 * @param endpoint
 * @param validateHostnames
 */
case class Datomic_PeerServer(
  accessKey: String,
  secret: String,
  endpoint: String,
  validateHostnames: Boolean = false
) extends Datomic_Client(
  Datomic.clientPeerServer(accessKey, secret, endpoint, validateHostnames),
  AsyncDatomic.clientPeerServer(accessKey, secret, endpoint, validateHostnames)
) {

  def connectionError: Option[String] = try {
    s"curl -k -S -s https://$endpoint/health".!!.trim match {
      case "ok" => None
      case err  => Some(err)
    }
  } catch {
    case e: Throwable => Some(e.toString)
  }

  def connect(dbName: String): Conn_Client = {
    connectionError.map(err =>
      throw new RuntimeException(
        "\nPeer Server not running. Please start it with something like" +
          "\nbin/run -m datomic.peer-server -h localhost -p 8998 -a key,secret -d db-name,datomic:mem://db-name" +
          "\nhttps://docs.datomic.com/on-prem/peer-server.html\n" + err
      )
    )

    val servedDbs = getServedDatabases()

    if (servedDbs.isEmpty)
      throw new RuntimeException("Found no database served by the Peer Server")

    if (!servedDbs.contains(dbName))
      throw new RuntimeException(
        s"Couldn't find db `$dbName` among databases currently served by the Peer Server:\n" +
          servedDbs.mkString("\n")
      )

    Conn_Client(client, clientAsync, dbName)
  }

  def getServedDatabases(timeout: Int = 0): List[String] = {
    client.listDatabases(timeout).asScala.toList.sorted
  }


  def checkNotLambda: Any => Boolean = {
    val fulltext = read(":db/fulltext")
    (k: Any) => k == fulltext
  }
}

