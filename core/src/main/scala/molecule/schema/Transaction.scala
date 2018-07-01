package molecule.schema

private[molecule] trait Transaction {
  val partitions: java.util.List[_]
  val namespaces: java.util.List[_]
}