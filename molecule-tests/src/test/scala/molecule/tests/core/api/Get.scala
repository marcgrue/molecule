package molecule.tests.core.api

import java.util.{Collection => jCollection, List => jList}
import molecule.datomic.api.out1._
import molecule.tests.core.base.dsl.CoreTest._
import molecule.TestSpec
import molecule.core.util.JavaUtil
import molecule.datomic.base.util.SystemPeer


class Get extends TestSpec with JavaUtil {

  class Setup extends CoreSetup {
    Ns.int insert List(1, 2, 3)
  }


  "Sync" in new Setup {

    Ns.int.get === List(1, 2, 3)
    Ns.int.getArray === Array(1, 2, 3)
    Ns.int.getIterable.iterator.toList === Iterator(1, 2, 3).toList

    // Raw output has different implementations but same interface
    if (system == SystemPeer) {
      // Interface
      Ns.int.getRaw.isInstanceOf[jCollection[jList[AnyRef]]] === true
      // Implementation: HashSet of PersistentVector of Object
      Ns.int.getRaw.getClass.getName === "java.util.HashSet"
      Ns.int.getRaw.iterator.next.getClass.getName === "clojure.lang.PersistentVector"
      Ns.int.getRaw.toString === "[[1], [2], [3]]"
    } else {
      // Interface
      Ns.int.getRaw.isInstanceOf[jCollection[jList[AnyRef]]] === true
      // Implementation: PersistentVector of PersistentVector of Object
      Ns.int.getRaw.getClass.getName === "clojure.lang.PersistentVector"
      Ns.int.getRaw.iterator.next.getClass.getName === "clojure.lang.PersistentVector"
      Ns.int.getRaw.toString === "[[1] [2] [3]]"
    }
    Ns.int.getRaw.ints === List(1, 2, 3)

    Ns.int.get(2) === List(1, 2)
    Ns.int.getArray(2) === Array(1, 2)
    Ns.int.getRaw(2).ints === List(1, 2)
  }


  "Async" in new Setup {
    await(Ns.int.getAsync) === List(1, 2, 3)
    await(Ns.int.getAsyncArray) === Array(1, 2, 3)
    await(Ns.int.getAsyncIterable).iterator.toList === Iterator(1, 2, 3).toList
    await(Ns.int.getAsyncRaw).ints === List(1, 2, 3)

    await(Ns.int.getAsync(2)) === List(1, 2)
    await(Ns.int.getAsyncArray(2)) === Array(1, 2)
    await(Ns.int.getAsyncRaw(2)).ints === List(1, 2)
  }
}
