package molecule.core.transform

import molecule.core.macros.MakeMoleculeDynamic
import scala.language.dynamics
import scala.language.experimental.macros

trait DynamicMolecule extends Dynamic {
//  def applyDynamic(method: String)(a: Any): Any = ???
//  def applyDynamic[A](method: String)(a: A): Any = ???
//  def applyDynamic[A, B](method: String)(a: A, b: B): Any = ???
//  def applyDynamic(method: String)(a: Any): Any = ???
  def applyDynamic(method: String)(args: Any*): Any = ???
//  def applyDynamic(method: String)(args: Any*): Any = macro MakeMoleculeDynamic.implApply
  def selectDynamic(field: String): Any = ???
}