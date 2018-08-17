package molecule.coretests.runtime

import molecule.api._

import java.net.URI
import java.util.{Date, UUID}

import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class EntityAPI extends CoreSpec {

  // See also molecule.examples.dayOfDatomic.ProductsAndOrders

  "touch Map" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touch === Map(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> Map(
        ":db/id" -> 17592186045446L,
        ":ref1/str1" -> "Hollywood Rd"),
      ":ns/str" -> "Ben"
    )

    eid.touchQuoted ===
      """Map(
        |  ":db/id" -> 17592186045445L,
        |  ":ns/int" -> 42,
        |  ":ns/ref1" -> Map(
        |    ":db/id" -> 17592186045446L,
        |    ":ref1/str1" -> "Hollywood Rd"),
        |  ":ns/str" -> "Ben")""".stripMargin

    // Level

    eid.touchMax(2) === Map(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> Map(":db/id" -> 17592186045446L, ":ref1/str1" -> "Hollywood Rd"),
      ":ns/str" -> "Ben"
    )
    eid.touchMax(1) === Map(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> 17592186045446L,
      ":ns/str" -> "Ben"
    )

    eid.touchQuotedMax(1) ===
      """Map(
        |  ":db/id" -> 17592186045445L,
        |  ":ns/int" -> 42,
        |  ":ns/ref1" -> 17592186045446L,
        |  ":ns/str" -> "Ben")""".stripMargin
  }


  "touch List" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid.touchList === List(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> List((":db/id", 17592186045446L), (":ref1/str1", "Hollywood Rd")),
      ":ns/str" -> "Ben"
    )

    eid.touchListQuoted ===
      """List(
        |  ":db/id" -> 17592186045445L,
        |  ":ns/int" -> 42,
        |  ":ns/ref1" -> List(
        |    ":db/id" -> 17592186045446L,
        |    ":ref1/str1" -> "Hollywood Rd"),
        |  ":ns/str" -> "Ben")""".stripMargin

    eid.touchListMax(3) === List(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> List((":db/id", 17592186045446L), (":ref1/str1", "Hollywood Rd")),
      ":ns/str" -> "Ben"
    )
    eid.touchListMax(1) === List(
      ":db/id" -> 17592186045445L,
      ":ns/int" -> 42,
      ":ns/ref1" -> 17592186045446L,
      ":ns/str" -> "Ben"
    )


    eid.touchListQuotedMax(1) ===
      """List(
        |  ":db/id" -> 17592186045445L,
        |  ":ns/int" -> 42,
        |  ":ns/ref1" -> 17592186045446L,
        |  ":ns/str" -> "Ben")""".stripMargin
  }


  "apply typed" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    // Level 1
    eid[String](":ns/str") === Some("Ben")
    eid[Int](":ns/int") === Some(42)

    // Level 2
    // Type casting necessary to get right value type from Map[String, Any]
    val refMap = eid[Map[String, Any]](":ns/ref1").getOrElse(Map.empty[String, Any])
    refId[String](":ref1/str1") === Some("Hollywood Rd")


    // Non-asserted or non-existing attribute returns None
    eid[Int](":ns/non-existing-attribute") === None
    eid[Int](":ns/existing-but-non-asserted-attribute") === None
  }


  "apply untyped" in new CoreSetup {

    val List(eid, refId) = Ns.str.int.Ref1.str1.insert("Ben", 42, "Hollywood Rd").eids

    eid(":ns/str", ":ns/int", ":ns/ref1") === List(
      Some("Ben"),
      Some(42),
      Some(
        Map(
          ":db/id" -> 17592186045446L,
          ":ref1/str1" -> "Hollywood Rd"
        )
      )
    )

    // Type ascription is still unchecked since it is eliminated by erasure so compile warnings are emitted
    val List(
    optName: Option[String],
    optAge: Option[Int],
    optAddress: Option[Map[String, Any]]
    ) = eid(
      ":ns/str",
      ":ns/int",
      ":ns/ref1"
    )

    val name: String = optName.getOrElse("no name")

    val address: Map[String, Any] = optAddress.getOrElse(Map.empty[String, Any])
    val street : String           = address.getOrElse(":ref1/str1", "no street").asInstanceOf[String]

    name === "Ben"
    street === "Hollywood Rd"
  }
}
