package molecule.examples.dayOfDatomic.schema
import molecule.schema.definition._

@InOut(3, 6)
object DbDefinition {

  trait Db {
    val valueType = oneString
  }
}