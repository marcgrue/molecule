package molecule.datomic.client.facade

import datomic.Util.read
import datomicScala.client.api.async.AsyncDatomic
import datomicScala.client.api.sync.Datomic
import scala.jdk.CollectionConverters._


/**
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

  // Peer server handles 1 database only
  def getDatabaseName(timeout: Int = 0): Option[String] =
    client.listDatabases(timeout).asScala.toList.headOption


  def checkNotLambda: Any => Boolean = {
    val fulltext = read(":db/fulltext")
    (k: Any) => k == fulltext
  }
}

