package molecule

/**
  * Molecule API with json enabled
  *
  * To use Molecule with json output, this object is imported
  *
  * ```import molecule.Json._```
  *
  * Enables using Molecule with extra support for json output.
  * If one doesn't need json output, `molecule.Base._` should be imported
  * instead to minimize the generated code.
  **/
object Json extends Base {

  // Marker to signal creating json output code
  implicit override val jsonGetter = molecule.boilerplate.JsonGetter
}