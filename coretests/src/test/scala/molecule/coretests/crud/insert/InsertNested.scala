package molecule.coretests.crud.insert

import molecule.datomic.peer.api._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.facade.TxReport
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class InsertNested extends CoreSpec {


  "Mandatory" in new CoreSetup {

    // If we want to create two references from the same base entity we
    // can us "group" notation `*` after our cardinality-many reference
    // and then define what sub-attributes we want to add.

    // Note that the "sub-molecule" we apply is treated as a single type
    // - when more than 1 attribute, like a tuple.

    val List(e, r1, r2) = Ns.Refs1.*(Ref1.str1).insert(List("r1", "r2")).eids

    e.touch === Map(
      ":db/id" -> e,
      ":Ns/refs1" -> List(
        Map(":db/id" -> r1, ":Ref1/str1" -> "r1"),
        Map(":db/id" -> r2, ":Ref1/str1" -> "r2")
      ))

    // Like the classical order/products example
    // Note how our "sub-molecule" `Ref1.int.str` is regarded as
    // one type `Seq[(Int, String)]` by the outer molecule
    val List(order, p1, p2) = m(Ns.str.Refs1 * Ref1.int1.str1).insert(
      "order",
      List((4, "product1"), (7, "product2"))
    ).eids

    order.touch === Map(
      ":db/id" -> order,
      ":Ns/refs1" -> List(
        Map(":db/id" -> p1, ":Ref1/int1" -> 4, ":Ref1/str1" -> "product1"),
        Map(":db/id" -> p2, ":Ref1/int1" -> 7, ":Ref1/str1" -> "product2")
      ),
      ":Ns/str" -> "order"
    )
  }

}