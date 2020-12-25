package molecule.core.util

import java.util.Collections
import java.util.{Collection => jCollection, List => jList}


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


  // Raw output has different implementations but same interface. Printing
  // output in tests is therefore formatted differently in tests and this
  // transformer can unify the tested results (here just Int's)

  // Interface: jCollection[jList[AnyRef]]

  // Implementations:

  // Peer:
  // java.util.HashSet[clojure.lang.PersistentVector<Object>]

  // Peer-server and dev-local:
  // clojure.lang.PersistentVector[clojure.lang.PersistentVector<Object>]

  implicit class raw2list(raw: jCollection[jList[AnyRef]]) {
    var intList = List.empty[Int]
    def ints: List[Int] = {
      raw.forEach(row => intList = intList :+ row.get(0).toString.toInt)
      intList
    }

    var strList = List.empty[String]
    def strs: List[String] = {
      raw.forEach(row => strList = strList :+ row.get(0).toString)
      strList
    }

    var strIntList = List.empty[(String, Int)]
    def strInts: List[(String, Int)] = {
      raw.forEach(row => strIntList = strIntList :+ (row.get(0).toString, row.get(1).toString.toInt))
      strIntList
    }
  }
}
