package molecule.datomic.client.devLocal.facade

import java.util.UUID.randomUUID
import datomic.Peer
import datomicScala.client.api.sync.Client
import molecule.core.facade.exception.DatomicFacadeException
import molecule.datomic.base.facade.Conn
import molecule.core.schema.SchemaTransaction
import molecule.datomic.peer.facade.Datomic_Peer
import scala.jdk.CollectionConverters._


/** Facade to Datomic Peer with selected methods.
  *
  * @see [[http://www.scalamolecule.org/manual/schema/transaction/ Manual]]
  * @groupname database  Database operations
  * @groupprio 10
  * */
case class Datomic_DevLocal(client: Client) {

  def getDatabaseNames(
    timeout: Int = 0,
    offset: Int = 0,
    limit: Int = 1000
  ): List[String] = {
    client.listDatabases(timeout, offset, limit).asScala.toList
  }

  def createDatabase(dbName: String, timeout: Int = 0): Boolean = try {
    client.createDatabase(dbName, timeout)
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  def deleteDatabase(dbName: String, timeout: Int = 0): Boolean = try {
    client.deleteDatabase(dbName, timeout)
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

  def connect(dbName: String): Conn_DevLocal = try {
    Conn_DevLocal(client, dbName)
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
    * @param schema Auto-generated YourDomainSchema Transaction object<br>
    *               (in package yourdomain.schema of generated source jar)
    * @param dbName Database name
    * @return [[molecule.datomic.base.facade.Conn Conn]]
    */
  def recreateDbFrom(schema: SchemaTransaction, dbName: String): Conn_DevLocal = try {
    deleteDatabase(dbName)
    createDatabase(dbName)
    val conn = connect(dbName)
    if (schema.partitions.size() > 0)
      conn.transact(schema.partitions)
    conn.transact(schema.namespaces)
    conn
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }

  /** Deletes existing database (!) and creates a new empty db with schema from schema data structure.
    * <br><br>
    * Schema data structure is a java List of Map's of key/value pairs defining the schema.
    * <br><br>
    * Can be an [[https://github.com/edn-format/edn EDN]] file like the [[https://github.com/Datomic/mbrainz-sample/blob/master/schema.edn mbrainz example]].
    *
    * @see [[https://docs.datomic.com/on-prem/data-structure-literals.html]]
    * @group database
    * @param schemaData java.util.List of java.util.Maps of key/values defining a Datomic schema
    * @param dbName     Optional String identifier of database (default empty string creates a randomUUID)
    * @return [[molecule.datomic.base.facade.Conn Conn]]
    */
  def recreateDbFromRaw(schemaData: java.util.List[_], dbName: String): Conn_DevLocal = try {
    deleteDatabase(dbName)
    createDatabase(dbName)
    val conn = connect(dbName)
    conn.transact(schemaData)
    conn
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }


  // Schema ............................................................................

  /** Transact schema from auto-generated schema transaction data.
    *
    * @group database
    * @param schema sbt-plugin auto-generated Transaction file path.to.schema.YourDomainSchema
    * @param dbName Database name
    * @return
    */
  def transactSchema(
    schema: SchemaTransaction,
    dbName: String
  ): Conn_DevLocal = try {
    val conn = connect(dbName)
    if (schema.partitions.size() > 0)
      conn.transact(schema.partitions)
    conn.transact(schema.namespaces)
    conn
  } catch {
    case e: Throwable => throw new DatomicFacadeException(e.getCause.toString)
  }
}

