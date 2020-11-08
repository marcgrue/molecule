package molecule.coretests.api

import java.util.Date
import molecule.datomic.peer.api._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.facade.TxReport


class Since extends CoreSpec {
  sequential

  implicit val conn = recreateDbFrom(CoreTestSchema)

  val tx1: TxReport = Ns.int(1).save
  Thread.sleep(1000)
  val tx2: TxReport = Ns.int(2).save
  Thread.sleep(1000)
  val tx3: TxReport = Ns.int(3).save

  val t1: Long = tx1.t
  val t2: Long = tx2.t
  val t3: Long = tx3.t

  val d1: Date = tx1.inst
  val d2: Date = tx2.inst
  val d3: Date = tx3.inst


  "Since" >> {

    Ns.int.getSince(t3) === List()
    Ns.int.getSince(t2) === List(3)
    Ns.int.getSince(t1) === List(2, 3)
    Ns.int.getSince(t1, 1) === List(2)

    Ns.int.getSince(tx3) === List()
    Ns.int.getSince(tx2) === List(3)
    Ns.int.getSince(tx1) === List(2, 3)
    Ns.int.getSince(tx1, 1) === List(2)

    Ns.int.getSince(d3) === List()
    Ns.int.getSince(d2) === List(3)
    Ns.int.getSince(d1) === List(2, 3)
    Ns.int.getSince(d1, 1) === List(2)


    Ns.int.getArraySince(t3) === Array()
    Ns.int.getArraySince(t2) === Array(3)
    Ns.int.getArraySince(t1) === Array(2, 3)
    Ns.int.getArraySince(t1, 1) === Array(2)

    Ns.int.getArraySince(tx3) === Array()
    Ns.int.getArraySince(tx2) === Array(3)
    Ns.int.getArraySince(tx1) === Array(2, 3)
    Ns.int.getArraySince(tx1, 1) === Array(2)

    Ns.int.getArraySince(d3) === Array()
    Ns.int.getArraySince(d2) === Array(3)
    Ns.int.getArraySince(d1) === Array(2, 3)
    Ns.int.getArraySince(d1, 1) === Array(2)


    Ns.int.getIterableSince(t3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(t2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(t1).iterator.toList === Iterator(2, 3).toList

    Ns.int.getIterableSince(tx3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(tx2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(tx1).iterator.toList === Iterator(2, 3).toList

    Ns.int.getIterableSince(d3).iterator.toList === Iterator().toList
    Ns.int.getIterableSince(d2).iterator.toList === Iterator(3).toList
    Ns.int.getIterableSince(d1).iterator.toList === Iterator(2, 3).toList


    Ns.int.getRawSince(t3).toString === "[]"
    Ns.int.getRawSince(t2).toString === "[[3]]"
    Ns.int.getRawSince(t1).toString === "[[2], [3]]"
    Ns.int.getRawSince(t1, 1).toString === "[[2]]"

    Ns.int.getRawSince(tx3).toString === "[]"
    Ns.int.getRawSince(tx2).toString === "[[3]]"
    Ns.int.getRawSince(tx1).toString === "[[2], [3]]"
    Ns.int.getRawSince(tx1, 1).toString === "[[2]]"

    Ns.int.getRawSince(d3).toString === "[]"
    Ns.int.getRawSince(d2).toString === "[[3]]"
    Ns.int.getRawSince(d1).toString === "[[2], [3]]"
    Ns.int.getRawSince(d1, 1).toString === "[[2]]"


    Ns.int.getJsonSince(t3) === ""

    Ns.int.getJsonSince(t2) ===
      """[
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(t1) ===
      """[
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(t1, 1) ===
      """[
        |{"Ns.int": 2}
        |]""".stripMargin


    Ns.int.getJsonSince(tx3) === ""

    Ns.int.getJsonSince(tx2) ===
      """[
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(tx1) ===
      """[
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(tx1, 1) ===
      """[
        |{"Ns.int": 2}
        |]""".stripMargin


    Ns.int.getJsonSince(d3) === ""

    Ns.int.getJsonSince(d2) ===
      """[
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(d1) ===
      """[
        |{"Ns.int": 2},
        |{"Ns.int": 3}
        |]""".stripMargin

    Ns.int.getJsonSince(d1, 1) ===
      """[
        |{"Ns.int": 2}
        |]""".stripMargin

  }
}
