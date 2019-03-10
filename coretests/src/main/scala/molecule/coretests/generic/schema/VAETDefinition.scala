package molecule.coretests.generic.schema
import molecule.schema.definition._

@InOut(0, 8)
object VAETDefinition {

  trait VAET {
    val dummy = oneInt
  }
}
