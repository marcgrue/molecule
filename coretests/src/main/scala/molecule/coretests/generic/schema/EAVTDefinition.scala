package molecule.coretests.generic.schema
import molecule.schema.definition._

@InOut(0, 8)
object EAVTDefinition {

  trait EAVT {
    val dummy = oneInt
  }
}
