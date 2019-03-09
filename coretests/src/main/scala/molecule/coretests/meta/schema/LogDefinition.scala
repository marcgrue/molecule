package molecule.coretests.meta.schema
import molecule.schema.definition._

@InOut(0, 8)
object LogDefinition {

  trait Log {
    val dummy = oneInt
  }
}
