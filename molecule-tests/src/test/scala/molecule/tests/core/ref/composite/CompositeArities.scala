package molecule.tests.core.ref.composite

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out4._
import molecule.TestSpec

class CompositeArities extends TestSpec {

  "1 + 1" in new CoreSetup {

    // Composite of two single-value molecules
    val List(e1, e2) = Ref2.int2 + Ns.int insert Seq(
      // Two rows of data
      (1, 11),
      (2, 22)
    ) eids

    // Two entities created
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/int" -> 11,
      ":Ref2/int2" -> 1
    )
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/int" -> 22,
      ":Ref2/int2" -> 2
    )

    // Queries via each namespace
    m(Ref2.int2).get.sorted === Seq(1, 2)
    m(Ns.int).get.sorted === Seq(11, 22)

    // Composite query
    m(Ref2.int2 + Ns.int).get.sorted === Seq(
      (1, 11),
      (2, 22)
    )
  }


  "1 + 2" in new CoreSetup {

    // Composite of Molecule1 + Molecule2
    val List(e1, e2) = Ref2.int2 + Ns.int.str insert Seq(
      // Two rows of data
      (1, (11, "aa")),
      (2, (22, "bb"))
    ) eids

    // Two entities created
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/int" -> 11,
      ":Ns/str" -> "aa",
      ":Ref2/int2" -> 1
    )
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/int" -> 22,
      ":Ns/str" -> "bb",
      ":Ref2/int2" -> 2
    )

    // Queries via each namespace
    m(Ref2.int2).get.sorted === Seq(
      1,
      2
    )
    m(Ns.int.str).get.sorted === Seq(
      (11, "aa"),
      (22, "bb")
    )

    // Composite query
    val (i1, (i2, s)): (Int, (Int, String)) = m(Ref2.int2 + Ns.int.str).get.head
    m(Ref2.int2 + Ns.int.str).get.sorted === Seq(
      (1, (11, "aa")),
      (2, (22, "bb"))
    )
  }


  "2 + 1" in new CoreSetup {

    // Composite of Molecule2 + Molecule1
    val List(e1, e2) = Ref2.int2.str2 + Ns.int insert Seq(
      // Two rows of data
      ((1, "a"), 11),
      ((2, "b"), 22)
    ) eids

    // Two entities created
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/int" -> 11,
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/int" -> 22,
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )

    // Queries via each namespace
    m(Ref2.int2.str2).get.sorted === Seq(
      (1, "a"),
      (2, "b")
    )
    m(Ns.int).get.sorted === Seq(
      11,
      22
    )

    // Composite query
    m(Ref2.int2.str2 + Ns.int).get.sorted === Seq(
      ((1, "a"), 11),
      ((2, "b"), 22)
    )
  }


  "2 + 2" in new CoreSetup {

    // Composite of Molecule2 + Molecule2
    val List(e1, e2) = Ref2.int2.str2 + Ns.str.int insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // Two entities created
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ns/int" -> 11,
      ":Ns/str" -> "aa",
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ns/int" -> 22,
      ":Ns/str" -> "bb",
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )

    // Queries via each namespace
    Ref2.int2.str2.get.sorted === List(
      (1, "a"),
      (2, "b")
    )
    Ns.str.int.get.sorted === List(
      ("aa", 11),
      ("bb", 22)
    )

    // Composite query
    m(Ref2.int2.str2 + Ns.str.int).get.sorted === List(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )
  }


  "2 + 3 (2+1tx)" in new CoreSetup {

    // Composite of Molecule2 + Molecule1 + Tx meta data
    // Note that tx meta attributes have underscore/are tacit in order not to affect the type of input
    val List(e1, e2, txId) = Ref2.int2.str2 +
      Ref1.str1.int1.Tx(Ns.str_("Tx meta data")) insert Seq(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    ) eids

    // Three (!) entities created
    e1.touchList === List(
      ":db/id" -> e1,
      ":Ref1/int1" -> 11,
      ":Ref1/str1" -> "aa",
      ":Ref2/int2" -> 1,
      ":Ref2/str2" -> "a"
    )
    e2.touchList === List(
      ":db/id" -> e2,
      ":Ref1/int1" -> 22,
      ":Ref1/str1" -> "bb",
      ":Ref2/int2" -> 2,
      ":Ref2/str2" -> "b"
    )
    txId.touchList === List(
      ":db/id" -> txId,
      ":db/txInstant" -> txId(":db/txInstant").get,
      ":Ns/str" -> "Tx meta data",
    )

    // Queries via one namespace
    Ref2.int2.str2.get.sorted === List(
      (1, "a"),
      (2, "b")
    )
    // .. including transaction meta data
    // Note how transaction meta data is fetched for all entities ("rows") saved in the same transaction
    Ref2.int2.str2.Tx(Ns.str).get.sorted === List(
      (1, "a", "Tx meta data"),
      (2, "b", "Tx meta data")
    )

    // Queries via other namespace
    Ref1.str1.int1.get.sorted === List(
      ("aa", 11),
      ("bb", 22)
    )
    // .. including transaction meta data
    Ref1.str1.int1.Tx(Ns.str).get.sorted === List(
      ("aa", 11, "Tx meta data"),
      ("bb", 22, "Tx meta data")
    )

    // Transaction meta data alone can be accessed through tacit attributes of namespaces
    Ref2.int2_.Tx(Ns.str).get === List("Tx meta data")
    Ref1.int1_.Tx(Ns.str).get === List("Tx meta data")


    // Composite query
    m(Ref2.int2.str2 + Ref1.str1.int1).get.sorted === List(
      ((1, "a"), ("aa", 11)),
      ((2, "b"), ("bb", 22))
    )
    // .. including transaction meta data
    m(Ref2.int2.str2 + Ref1.str1.int1.Tx(Ns.str)).get.sorted === List(
      ((1, "a"), ("aa", 11, "Tx meta data")),
      ((2, "b"), ("bb", 22, "Tx meta data"))
    )
  }
}