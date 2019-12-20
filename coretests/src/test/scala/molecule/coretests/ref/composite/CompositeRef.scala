package molecule.coretests.ref.composite

import molecule.api.in3_out22._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class CompositeRef extends CoreSpec {


  "Card-one ref" in new CoreSetup {

    val List(e1, r1, e2, r2) = Ref2.int2.str2 + Ns.str.Ref1.int1 insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // First entity (including referenced entity)
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/ref1" -> List((":db/id", r1), (":Ref1/int1", 11)),
      ":Ns/str" -> "aa",
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )
    // First referenced entity (same as we see included above)
    r1.touchList === List(
      ":db/id" -> r1,
      ":Ref1/int1" -> 11
    )

    // Second entity (including referenced entity)
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/ref1" -> List((":db/id", r2), (":Ref1/int1", 22)),
      ":Ns/str" -> "bb",
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )
    // Second referenced entity (same as we see included above)
    r2.touchList === List(
      ":db/id" -> r2,
      ":Ref1/int1" -> 22
    )

    // Queries via each namespace
    Ref2.int2.str2.get.sorted === List(
      (1, "a"),
      (2, "b")
    )
    Ns.str.Ref1.int1.get.sorted === List(
      ("aa", 11),
      ("bb", 22)
    )

    // Composite query
    m(Ref2.int2.str2 + Ns.str.Ref1.int1).get.sorted === List(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )
  }


  "Card-many ref - one value" in new CoreSetup {

    val List(e1, r1, e2, r2) = Ref2.int2.str2 + Ns.str.Refs1.int1 insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // First entity (including referenced entity)
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/refs1" -> List(List((":db/id", r1), (":Ref1/int1", 11))),
      ":Ns/str" -> "aa",
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )

    // First referenced entity (same as we see included above)
    r1.touchList === List(
      ":db/id" -> r1,
      ":Ref1/int1" -> 11
    )

    // Second entity (including referenced entity)
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/refs1" -> List(List((":db/id", r2), (":Ref1/int1", 22))),
      ":Ns/str" -> "bb",
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )
    // Second referenced entity (same as we see included above)
    r2.touchList === List(
      ":db/id" -> r2,
      ":Ref1/int1" -> 22
    )

    // Queries via each namespace
    Ref2.int2.str2.get.sorted === List(
      (1, "a"),
      (2, "b")
    )
    Ns.str.Refs1.int1.get.sorted === List(
      ("aa", 11),
      ("bb", 22)
    )

    // Composite query
    m(Ref2.int2.str2 + Ns.str.Refs1.int1).get.sorted === List(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )
  }


  "Card-many ref - one value 2" in new CoreSetup {

    val List(e1, r1, e2, r2) = Ref2.int2.str2 + Ns.Refs1.int1 insert Seq(
      ((1, "a"), 11),
      ((2, "b"), 22)
    ) eids

    // First entity (including referenced entity)
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/refs1" -> List(List((":db/id", r1), (":Ref1/int1", 11))),
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )

    // First referenced entity (same as we see included above)
    r1.touchList === List(
      ":db/id" -> r1,
      ":Ref1/int1" -> 11
    )

    // Second entity (including referenced entity)
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/refs1" -> List(List((":db/id", r2), (":Ref1/int1", 22))),
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )
    // Second referenced entity (same as we see included above)
    r2.touchList === List(
      ":db/id" -> r2,
      ":Ref1/int1" -> 22
    )

    // Queries via each namespace
    Ref2.int2.str2.get.sorted === List(
      (1, "a"),
      (2, "b")
    )

    // Composite query
    m(Ref2.int2.str2 + Ns.Refs1.int1).get.sorted === List(
      ((1, "a"), 11),
      ((2, "b"), 22)
    )
    m(Ref2.int2.str2 + Ns.refs1).get === List(
      ((1, "a"), Set(r1)),
      ((2, "b"), Set(r2))
    )
  }
}