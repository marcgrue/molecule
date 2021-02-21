package molecule.core.transform
import scala.language.dynamics

trait DynamicProp extends Dynamic {
  def selectDynamic(name: String): Any = throw new RuntimeException(
    s"Dynamic property `$name` not available. Dynamic properties are only " +
      s"available when applying aggregate moderators that change the return type."
  )
}