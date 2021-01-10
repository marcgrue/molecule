package molecule.datomic.client.facade

import java.util
import java.util.{List => jList, Map => jMap}
import datomic.Util.read
import datomicClient.ClojureBridge
import datomicScala.client.api.async.AsyncClient
import datomicScala.client.api.sync.Client
import molecule.core.facade.exception.DatomicFacadeException
import molecule.core.data.SchemaTransaction
import scala.jdk.CollectionConverters._

/** Base Datomic facade for client api (peer-server/cloud/dev-local).
 *
 * @groupname database  Database operations
 * @groupprio 10
 *
 * @param client
 * @param clientAsync
 */
abstract class Datomic_Client(val client: Client, val clientAsync: AsyncClient)
  extends ClojureBridge {

  def connect(dbName: String): Conn_Client = Conn_Client(client, clientAsync, dbName)

  protected def checkNotLambda: Any => Boolean

  def allowedClientDefinitions(nss: jList[_]): util.List[jMap[Object, Object]] = {
    val nss2     = new util.ArrayList[jMap[Object, Object]]()
    val bytes    = read(":db.type/bytes")
    val checkNot = checkNotLambda
    nss.asInstanceOf[jList[jMap[Object, Object]]].asScala.foreach { map0 =>
      val map1     : Map[Object, Object] = map0.asScala.toMap
      val validType: Boolean             = map1.collect {
        case (k, v) if v == bytes => "bytes not implemented for dev-local"
      }.isEmpty
      if (validType) {
        val map2: jMap[Object, Object] = map1.filterNot {
          // Indexing and fulltext not implemented
          case (k, _) =>
            val res = checkNot(k)
            res
        }.asJava
        nss2.add(map2)
      }
    }
    nss2
  }

  /** Transact schema from auto-generated schema transaction data.
    *
    * @group database
    * @param schema sbt-plugin auto-generated Transaction file path.to.schema.YourDomainSchema
    * @param dbName Database name
    * @return [[Conn_Client]]
    */
  def transactSchema(schema: SchemaTransaction, dbName: String): Conn_Client = try {
    val conn = connect(dbName)
    if (schema.partitions.size() > 0)
      conn.transact(allowedClientDefinitions(schema.partitions))
    conn.transact(allowedClientDefinitions(schema.namespaces))
    conn
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.toString)
  }
}

