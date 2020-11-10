package molecule.coretests.api

import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.peer.api.out1._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class Get extends CoreSpec {
  sequential

  implicit val conn = recreateDbFrom(CoreTestSchema)

  Ns.int insert List(1, 2, 3)


  "Sync" >> {

    Ns.int.get === List(1, 2, 3)
    Ns.int.getArray === Array(1, 2, 3)
    Ns.int.getIterable.iterator.toList === Iterator(1, 2, 3).toList
    Ns.int.getRaw.toString === "[[1], [2], [3]]"
    Ns.int.getJson ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.get(2) === List(1, 2)
    Ns.int.getArray(2) === Array(1, 2)
    Ns.int.getRaw(2).toString === "[[1], [2]]"
    Ns.int.getJson(2) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2}
        |]""".stripMargin
  }


  "Async" >> {

    // Variations on getting data with Future[Array[Int]]

    Ns.int.getAsyncArray.map { result =>
      result === Array(1, 2, 3)

      // Fast while loop
      var i = 0
      val length = result.length
      while (i < length) {
        println(result(i)) // Do stuff with row...
        i += 1
      }
    }

    Await.result(
      Ns.int.getAsyncArray,
      1 second
    ) === Array(1, 2, 3)

    await(Ns.int.getAsync) === List(1, 2, 3)


    await(Ns.int.getAsyncArray) === Array(1, 2, 3)
    await(Ns.int.getAsyncIterable).iterator.toList === Iterator(1, 2, 3).toList
    await(Ns.int.getAsyncRaw).toString === "[[1], [2], [3]]"
    await(Ns.int.getAsyncJson) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    await(Ns.int.getAsync(2)) === List(1, 2)
    await(Ns.int.getAsyncArray(2)) === Array(1, 2)
    await(Ns.int.getAsyncRaw(2)).toString === "[[1], [2]]"
    await(Ns.int.getAsyncJson(2)) ===
      """[
        |{"Ns.int": 1},
        |{"Ns.int": 2}
        |]""".stripMargin
  }
}
