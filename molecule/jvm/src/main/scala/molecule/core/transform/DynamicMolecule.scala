package molecule.core.transform

import scala.language.dynamics

trait DynamicMolecule extends Dynamic {
  def applyDynamic(method: String)(args: Any*): Any = ???
  def selectDynamic(field: String): Any = ???
}