package molecule.facade
import java.util.UUID.randomUUID
import datomic.Peer
import molecule.facade.exception.DatomicFacadeException
import molecule.schema.SchemaTransaction


/** Facade to Datomic with selected methods.
  *
  * @see [[http://www.scalamolecule.org/manual/schema/transaction/ Manual]]
  * @groupname database  Database operations
  * @groupprio 10
  * */
trait Datomic {

  /** Deletes existing database (!) and creates a new empty db with schema from Schema Transaction file.
    * <br><br>
    * A typical development cycle in the initial stages of creating the db schema:
    *
    *  1. Edit schema definition file
    *  1. `sbt compile` to update boilerplate code in generated jars
    *  1. Obtain a fresh connection to new empty db with updated schema:<br>
    * `implicit val conn = recreateDbFrom(YourDomainSchema)`
    *
    * @group database
    * @param schema     Auto-generated YourDomainSchema Transaction object<br>
    *                   (in package yourdomain.schema of generated source jar)
    * @param identifier Optional String identifier to name database (default empty string creates a randomUUID)
    * @param protocol   Datomic protocol. Defaults to "mem" for in-memory database.
    * @return [[molecule.facade.Conn Conn]]
    */
  def recreateDbFrom(schema: SchemaTransaction, identifier: String = "", protocol: String = "mem"): Conn = {
    val id  = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val datomicConn = Peer.connect(uri)
      if (schema.partitions.size() > 0)
        datomicConn.transact(schema.partitions)
      datomicConn.transact(schema.namespaces)
      Conn(datomicConn)
    } catch {
      case e: Throwable => throw new DatomicFacadeException("@@@@@@@@@@ " + e.getCause)
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
    * @param schemaData java.util.List of java.util.Maps of key/values defining a Datomic schema
    * @param identifier Optional String identifier to name database (default empty string creates a randomUUID)
    * @param protocol   Datomic protocol. Defaults to "mem" for in-memory database.
    * @return [[molecule.facade.Conn Conn]]
    */
  def recreateDbFromRaw(schemaData: java.util.List[_], identifier: String = "", protocol: String = "mem"): Conn = {
    val id  = if (identifier == "") randomUUID() else identifier
    val uri = s"datomic:$protocol://$id"
    try {
      Peer.deleteDatabase(uri)
      Peer.createDatabase(uri)
      val datomicConn = Peer.connect(uri)
      datomicConn.transact(schemaData).get()
      Conn(datomicConn)
    } catch {
      case e: Throwable => throw new DatomicFacadeException("@@@@@@@@@@ " + e.getCause)
    }
  }


  // Schema ............................................................................

  /** Transact schema from auto-generated schema transaction data.
    *
    * @group database
    * @param schema sbt-plugin auto-generated Transaction file path.to.schema.YourDomainSchema
    * @param identifier
    * @param protocol
    * @return
    */
  def transactSchema(schema: SchemaTransaction, identifier: String, protocol: String = "mem"): Conn = {
    val uri = s"datomic:$protocol://$identifier"
    try {
      val datomicConn = Peer.connect(uri)
      if (schema.partitions.size() > 0)
        datomicConn.transact(schema.partitions)
      datomicConn.transact(schema.namespaces)
      Conn(datomicConn)
    } catch {
      case e: Throwable => throw new DatomicFacadeException("@@@@@@@@@@ " + e.getCause)
    }
  }

}
