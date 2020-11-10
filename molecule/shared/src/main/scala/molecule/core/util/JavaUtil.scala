package molecule.core.util
import java.util.Collections

trait JavaUtil {

  object Util {
    def list(items: AnyRef*): java.util.List[AnyRef] = {
      if (items == null) {
        new java.util.ArrayList[AnyRef]
      } else {
        val list: java.util.List[AnyRef] = new java.util.ArrayList[AnyRef](items.length)
        var i   : Int                    = 0
        while (i < items.length) {
          list.add(items(i))
          i += 1
          i
        }
        Collections.unmodifiableList(list)
      }
    }
  }
}
