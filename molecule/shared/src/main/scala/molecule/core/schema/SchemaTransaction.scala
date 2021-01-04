package molecule.core.schema


/** Schema transaction interface for auto-generated schema transaction data.
  * */
trait SchemaTransaction {

  /** Auto-generated partition transaction data. */
  val partitions: java.util.List[_]

  /** Auto-generated namespace and attribute transaction data. */
  val namespaces: java.util.List[_]
}