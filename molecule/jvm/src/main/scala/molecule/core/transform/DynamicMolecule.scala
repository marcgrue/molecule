package molecule.core.transform

import scala.language.dynamics

trait DynamicMolecule extends Dynamic {
  def applyDynamic(method: String)(a: Any): Any = ???
  def selectDynamic(field: String): Any = ???
}