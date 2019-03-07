package molecule.coretests.runtime

import molecule.api.out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec

class EntityAPI extends CoreSpec {

  // See also molecule.examples.dayOfDatomic.ProductsAndOrders

  "touch Map" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touch === Map(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045446L,
        ":Ref1/str1" -> "Hollywood Rd"),
      ":Ns/str" -> "Ben"
    )

    eid.touchQuoted ===
      """Map(
        |  ":db/id" -> 17592186045445L,
        |  ":Ns/int" -> 42,
        |  ":Ns/ref1" -> Map(
        |    ":db/id" -> 17592186045446L,
        |    ":Ref1/str1" -> "Hollywood Rd"),
        |  ":Ns/str" -> "Ben")""".stripMargin

    // Level

    eid.touchMax(2) === Map(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> Map(":db/id" -> 17592186045446L, ":Ref1/str1" -> "Hollywood Rd"),
      ":Ns/str" -> "Ben"
    )
    eid.touchMax(1) === Map(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> 17592186045446L,
      ":Ns/str" -> "Ben"
    )

    eid.touchQuotedMax(1) ===
      """Map(
        |  ":db/id" -> 17592186045445L,
        |  ":Ns/int" -> 42,
        |  ":Ns/ref1" -> 17592186045446L,
        |  ":Ns/str" -> "Ben")""".stripMargin
  }


  "touch List" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touchList === List(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> List((":db/id", 17592186045446L), (":Ref1/str1", "Hollywood Rd")),
      ":Ns/str" -> "Ben"
    )

    eid.touchListQuoted ===
      """List(
        |  ":db/id" -> 17592186045445L,
        |  ":Ns/int" -> 42,
        |  ":Ns/ref1" -> List(
        |    ":db/id" -> 17592186045446L,
        |    ":Ref1/str1" -> "Hollywood Rd"),
        |  ":Ns/str" -> "Ben")""".stripMargin

    eid.touchListMax(3) === List(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> List((":db/id", 17592186045446L), (":Ref1/str1", "Hollywood Rd")),
      ":Ns/str" -> "Ben"
    )
    eid.touchListMax(1) === List(
      ":db/id" -> 17592186045445L,
      ":Ns/int" -> 42,
      ":Ns/ref1" -> 17592186045446L,
      ":Ns/str" -> "Ben"
    )


    eid.touchListQuotedMax(1) ===
      """List(
        |  ":db/id" -> 17592186045445L,
        |  ":Ns/int" -> 42,
        |  ":Ns/ref1" -> 17592186045446L,
        |  ":Ns/str" -> "Ben")""".stripMargin
  }


  "apply typed" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    // Level 1
    eid[String](":Ns/str") === Some("Ben")
    eid[Int](":Ns/int") === Some(42)

    // Level 2
    // Type casting necessary to get right value type from Map[String, Any]
    val refMap = eid[Map[String, Any]](":Ns/ref1").getOrElse(Map.empty[String, Any])
    refId[String](":Ref1/str1") === Some("Hollywood Rd")


    // Non-asserted or non-existing attribute returns None
    eid[Int](":Ns/non-existing-attribute") === None
    eid[Int](":Ns/existing-but-non-asserted-attribute") === None
  }


  "apply untyped" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid(":Ns/str", ":Ns/int", ":Ns/ref1") === List(
      Some("Ben"),
      Some(42),
      Some(
        Map(
          ":db/id" -> 17592186045446L,
          ":Ref1/str1" -> "Hollywood Rd"
        )
      )
    )

    // Type ascription is still unchecked since it is eliminated by erasure so compile warnings are emitted
    val List(
    optName: Option[String],
    optAge: Option[Int],
    optAddress: Option[Map[String, Any]]
    ) = eid(
      ":Ns/str",
      ":Ns/int",
      ":Ns/ref1"
    )

    val name: String = optName.getOrElse("no name")

    val address: Map[String, Any] = optAddress.getOrElse(Map.empty[String, Any])
    val street : String           = address.getOrElse(":Ref1/str1", "no street").asInstanceOf[String]

    name === "Ben"
    street === "Hollywood Rd"
  }
}
