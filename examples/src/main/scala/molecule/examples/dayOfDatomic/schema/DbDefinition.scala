package molecule.examples.dayOfDatomic.schema
import molecule.core.schema.definition._

@InOut(3, 6)
object DbDefinition {

  trait Db {
    val valueType = oneString
  }
}