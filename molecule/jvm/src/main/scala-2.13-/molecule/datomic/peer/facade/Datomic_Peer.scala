package molecule.datomic.peer.facade

import java.util.UUID.randomUUID
import datomic.Peer
import molecule.core.facade.exception.DatomicFacadeException
import molecule.core.data.SchemaTransaction
import scala.collection.JavaConverters._


/** Facade to Datomic Peer with selected methods.
  *
  * @groupname database  Database operations
  * @groupprio 10
  * */
trait Datomic_Peer {

  def getDatabaseNames(protocol: String = "mem", host: String = "localhost:4334/"): List[String] = {
    Peer.getDatabaseNames(s"datomic:$protocol://$host*").asScala.toList
  }

  def createDatabase(dbIdentifier: String, protocol: String = "mem"): Boolean = try {
    Peer.createDatabase(s"datomic:$protocol://$dbIdentifier")
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  def deleteDatabase(dbIdentifier: String, protocol: String = "mem"): Boolean = try {
    Peer.deleteDatabase(s"datomic:$protocol://$dbIdentifier")
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  def renameDatabase(
    dbIdentifier: String,
    newDbName: String,
    protocol: String = "mem"
  ): Boolean = try {
    Peer.renameDatabase(s"datomic:$protocol://$dbIdentifier", newDbName)
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  def connect(dbIdentifier: String, protocol: String = "mem"): Conn_Peer = try {
    Conn_Peer(Peer.connect(s"datomic:$protocol://$dbIdentifier"))
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  /** Deletes existing database (!) and creates a new empty db with schema from Schema Transaction file.
    * <br><br>
    * A typical development cycle in the initial stages of creating the db schema:
    *
    *  1. Edit schema definition file
    *  1. `sbt compile` to update boilerplate code in generated jars
    *  1. Obtain a fresh connection to new empty db with updated schema:<br>
    *     `implicit val conn = recreateDbFrom(YourDomainSchema)`
    *
    * @group database
    * @param schema       Auto-generated YourDomainSchema Transaction object<br>
    *                     (in package yourdomain.schema of generated source jar)
    * @param dbIdentifier Optional String identifier to name database (default empty string creates a randomUUID)
    * @param protocol     Datomic protocol. Defaults to "mem" for in-memory database.
    * @return [[molecule.datomic.base.facade.Conn Conn]]
    */
  def recreateDbFrom(
    schema: SchemaTransaction,
    dbIdentifier: String = "",
    protocol: String = "mem"
  ): Conn_Peer = {
    val id = if (dbIdentifier == "") randomUUID().toString else dbIdentifier
    try {
      deleteDatabase(id, protocol)
      createDatabase(id, protocol)
      val conn = connect(id, protocol)
      if (schema.partitions.size() > 0)
        conn.transact(schema.partitions)
      conn.transact(schema.namespaces)
      conn
    } catch {
      case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
    }
  }

  /** Deletes existing database (!) and creates a new empty db with schema from schema data structure.
    * <br><br>
    * Schema data structure is a java List of Map's of key/value pairs defining the schema.
    * <br><br>
    * Can be an [[https://github.com/edn-format/edn EDN]] file like the [[https://github.com/Datomic/mbrainz-sample/blob/master/schema.edn mbrainz example]].
    *
    * @see [[https://docs.datomic.com/on-prem/data-structure-literals.html]]
    * @group database
    * @param schemaData   java.util.List of java.util.Maps of key/values defining a Datomic schema
    * @param dbIdentifier Optional String identifier of database (default empty string creates a randomUUID)
    * @see [[https://docs.datomic.com/on-prem/javadoc/datomic/Peer.html#connect-java.lang.Object-]]
    * @param protocol Datomic protocol. Defaults to "mem" for in-memory database.
    * @return [[molecule.datomic.base.facade.Conn Conn]]
    */
  def recreateDbFromRaw(
    schemaData: java.util.List[_],
    dbIdentifier: String = "",
    protocol: String = "mem"
  ): Conn_Peer = {
    val id = if (dbIdentifier == "") randomUUID().toString else dbIdentifier
    try {
      deleteDatabase(id, protocol)
      createDatabase(id, protocol)
      val conn = connect(id, protocol)
      conn.transact(schemaData)
      conn
    } catch {
      case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
    }
  }


  // Schema ............................................................................

  /** Transact schema from auto-generated schema transaction data.
    *
    * @group database
    * @param schema sbt-plugin auto-generated Transaction file path.to.schema.YourDomainSchema
    * @param dbIdentifier
    * @param protocol
    * @return
    */
  def transactSchema(
    schema: SchemaTransaction,
    dbIdentifier: String,
    protocol: String = "mem"
  ): Conn_Peer = try {
    val conn = connect(dbIdentifier, protocol)
    if (schema.partitions.size() > 0)
      conn.transact(schema.partitions)
    conn.transact(schema.namespaces)
    conn
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }
}

object Datomic_Peer extends Datomic_Peer
