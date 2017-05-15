package molecule.schema

trait Transaction {
  val partitions: java.util.List[_]
  val namespaces: java.util.List[_]
}