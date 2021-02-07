package molecule.tests.core.ref.composite

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.TestSpec

class CompositeExpression extends TestSpec {

  "Constraints apply to merged model" in new CoreSetup {

    Ref2.int2.str2 + Ns.str.int insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // Without constraints
    m(Ref2.int2.str2 + Ns.str.int).get.sorted === List(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )

    // One constraint
    m(Ref2.int2.>(1).str2 + Ns.str.int).get.sorted === List(
      ((2, "b"), ("bb", 22))
    )
    // Other constraint
    m(Ref2.int2.str2 + Ns.str.int.<(20)).get.sorted === List(
      ((1, "a"), ("aa", 11))
    )

    // Mutually exclusive constraints
    m(Ref2.int2.>(1).str2 + Ns.str.int.<(20)).get.sorted === List()

    // Constraints with all-matching data
    m(Ref2.int2.<(2).str2 + Ns.str.int.<(20)).get.sorted === List(
      ((1, "a"), ("aa", 11))
    )

    m(Ref2.int2_.<(2).str2 + Ns.int_.<(20)).get.sorted === List(
      "a"
    )

    m(Ref2.int2_.<(2) + Ns.str.int_.<(20)).get.sorted === List(
      "aa"
    )
  }


  "Null values" in new CoreSetup {

    // Composite data
    Ref2.int2.str2 + Ns.str.int insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // Add partial composite data
    Ref2.int2.str2 + Ns.int insert Seq(
      ((3, "c"), 33)
    ) eids

    // Non-composite query gets all data
    m(Ref2.int2.str2).get.sorted === Seq(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )

    // Composite query gets composite data only that has a `Ns.str` value too
    m(Ref2.int2.str2 + Ns.str.int).get.sorted === Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )
    m(Ref2.int2.str2 + Ns.str).get.sorted === Seq(
      ((1, "a"), "aa"),
      ((2, "b"), "bb")
    )

    // Composite data with `Ns.int` values retrieve all 3 entities
    m(Ref2.int2.str2 + Ns.int).get.sorted === Seq(
      ((1, "a"), 11),
      ((2, "b"), 22),
      ((3, "c"), 33)
    )

    // Composite data that _doesn't_ have a `Ns.str` value retrieve only the last entity
    m(Ref2.int2.str2 + Ns.str_(Nil).int).get.sorted === Seq(
      ((3, "c"), 33)
    )
  }
}