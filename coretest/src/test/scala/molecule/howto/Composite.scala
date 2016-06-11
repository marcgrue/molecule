package molecule
package howto
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class Composite extends CoreSpec {


//  "Arities" >> {
//
//    "1 + 1" in new CoreSetup {
//
//
//      // Composite of two single-value molecules
//      val List(e1, e2) = insert(
//        Ref2.int2, Ns.int
//      )(Seq(
//        // Two rows of data
//        (1, 11),
//        (2, 22)
//      ))().eids
//
//      // Two entities created
//      e1.touchList === List(
//        ":db/id" -> 17592186045445L,
//        ":ns/int" -> 11,
//        ":ref2/int2" -> 1
//      )
//      e2.touchList === List(
//        ":db/id" -> 17592186045446L,
//        ":ns/int" -> 22,
//        ":ref2/int2" -> 2
//      )
//
//      // Queries via each namespace
//      m(Ref2.int2).get.sorted === Seq(1, 2)
//      m(Ns.int).get.sorted === Seq(11, 22)
//
//      // Composite query
//      m(Ref2.int2 ~ Ns.int).get.sorted === Seq(
//        (1, 11),
//        (2, 22)
//      )
//    }
//
//
//    "1 + 2" in new CoreSetup {
//
//      // Composite of Molecule2 + Molecule1
//      val List(e1, e2) = insert(
//        Ref2.int2, Ns.int.str
//      )(Seq(
//        // Two rows of data
//        (1, (11, "aa")),
//        (2, (22, "bb"))
//      ))().eids
//
//      // Two entities created
//      e1.touchList === List(
//        ":db/id" -> 17592186045445L,
//        ":ns/int" -> 11,
//        ":ns/str" -> "aa",
//        ":ref2/int2" -> 1
//      )
//      e2.touchList === List(
//        ":db/id" -> 17592186045446L,
//        ":ns/int" -> 22,
//        ":ns/str" -> "bb",
//        ":ref2/int2" -> 2
//      )
//
//      // Queries via each namespace
//      m(Ref2.int2).get.sorted === Seq(
//        1,
//        2
//      )
//      m(Ns.int.str).get.sorted === Seq(
//        (11, "aa"),
//        (22, "bb")
//      )
//
//      // Composite query
//      m(Ref2.int2 ~ Ns.int.str).get.sorted === Seq(
//        (1, (11, "aa")),
//        (2, (22, "bb"))
//      )
//    }
//
//
//    "2 + 1" in new CoreSetup {
//
//      // Composite of Molecule2 + Molecule1
//      val List(e1, e2) = insert(
//        Ref2.int2.str2, Ns.int
//      )(Seq(
//        // Two rows of data
//        ((1, "a"), 11),
//        ((2, "b"), 22)
//      ))().eids
//
//      // Two entities created
//      e1.touchList === List(
//        ":db/id" -> 17592186045445L,
//        ":ns/int" -> 11,
//        ":ref2/int2" -> 1,
//        ":ref2/str2" -> "a"
//      )
//      e2.touchList === List(
//        ":db/id" -> 17592186045446L,
//        ":ns/int" -> 22,
//        ":ref2/int2" -> 2,
//        ":ref2/str2" -> "b"
//      )
//
//      // Queries via each namespace
//      m(Ref2.int2.str2).get.sorted === Seq(
//        (1, "a"),
//        (2, "b")
//      )
//      m(Ns.int).get.sorted === Seq(
//        11,
//        22
//      )
//
//      // Composite query
//      m(Ref2.int2.str2 ~ Ns.int).get.sorted === Seq(
//        ((1, "a"), 11),
//        ((2, "b"), 22)
//      )
//    }
//
//
//    "2 + 2" in new CoreSetup {
//
//      val List(e1, e2) = insert(
//        Ref2.int2.str2, Ns.str.int
//      )(
//        Seq(
//          ((1, "a"), ("aa", 11)),
//          ((2, "b"), ("bb", 22))
//        )
//      )().eids
//
//      // Two entities created
//      e1.touchList === List(
//        ":db/id" -> 17592186045445L,
//        ":ns/int" -> 11,
//        ":ns/str" -> "aa",
//        ":ref2/int2" -> 1,
//        ":ref2/str2" -> "a"
//      )
//      e2.touchList === List(
//        ":db/id" -> 17592186045446L,
//        ":ns/int" -> 22,
//        ":ns/str" -> "bb",
//        ":ref2/int2" -> 2,
//        ":ref2/str2" -> "b"
//      )
//
//      // Queries via each namespace
//      Ref2.int2.str2.get.sorted === List(
//        (1, "a"),
//        (2, "b")
//      )
//      Ns.str.int.get.sorted === List(
//        ("aa", 11),
//        ("bb", 22)
//      )
//
//      // Composite query
//      m(Ref2.int2.str2 ~ Ns.str.int).get.sorted === List(
//        ((1, "a"), ("aa", 11)),
//        ((2, "b"), ("bb", 22))
//      )
//    }
//
//
//    "2 + 3 (2+1tx)" in new CoreSetup {
//
//      // Add transaction meta data
//      val List(e1, e2, txId) = insert(
//        Ref2.int2.str2, Ref1.str1.int1
//      )(
//        Seq(
//          ((1, "a"), ("aa", 11)),
//          ((2, "b"), ("bb", 22))
//        )
//      )(
//        Ns.str_("Tx meta data")
//      ).eids
//
//      // Three (!) entities created
//      e1.touchList === List(
//        ":db/id" -> 17592186045445L,
//        ":ref1/int1" -> 11,
//        ":ref1/str1" -> "aa",
//        ":ref2/int2" -> 1,
//        ":ref2/str2" -> "a"
//      )
//      e2.touchList === List(
//        ":db/id" -> 17592186045446L,
//        ":ref1/int1" -> 22,
//        ":ref1/str1" -> "bb",
//        ":ref2/int2" -> 2,
//        ":ref2/str2" -> "b"
//      )
//      txId.touchList === List(
//        ":db/id" -> 13194139534340L,
//        ":db/txInstant" -> txId(":db/txInstant").get,
//        ":ns/str" -> "Tx meta data")
//
//      // Queries via one namespace
//      Ref2.int2.str2.get.sorted === List(
//        (1, "a"),
//        (2, "b")
//      )
//      // .. including transaction meta data
//      // Note how transaction meta data is fetched for all entities ("rows") saved in the same transaction
//      Ref2.int2.str2.tx_(Ns.str).get.sorted === List(
//        (1, "a", "Tx meta data"),
//        (2, "b", "Tx meta data")
//      )
//
//      // Queries via other namespace
//      Ref1.str1.int1.get.sorted === List(
//        ("aa", 11),
//        ("bb", 22)
//      )
//      // .. including transaction meta data
//      Ref1.str1.int1.tx_(Ns.str).get.sorted === List(
//        ("aa", 11, "Tx meta data"),
//        ("bb", 22, "Tx meta data")
//      )
//
//      // Transaction meta data alone can be accessed through tacet attributes of namespaces
//      Ref2.int2_.tx_(Ns.str).get === List("Tx meta data")
//      Ref1.int1_.tx_(Ns.str).get === List("Tx meta data")
//
//
//      // Composite query
//      m(Ref2.int2.str2 ~ Ref1.str1.int1).get.sorted === List(
//        ((1, "a"), ("aa", 11)),
//        ((2, "b"), ("bb", 22))
//      )
//      // .. including transaction meta data
//      m(Ref2.int2.str2 ~ Ref1.str1.int1.tx_(Ns.str)).get.sorted === List(
//        ((1, "a"), ("aa", 11, "Tx meta data")),
//        ((2, "b"), ("bb", 22, "Tx meta data"))
//      )
//    }
//  }


  "References" >> {

    "Card-one ref" in new CoreSetup {

      val List(e1, r1, e2, r2) = insert(
        Ref2.int2.str2, Ns.str.Ref1.int1
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )().eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/ref1" -> List((":db/id",17592186045446L), (":ref1/int1",11)),
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )
      // First referenced entity (same as we see included above)
      r1.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 11
      )

      // Second entity (including referenced entity)
      e2.touchList === List(
        ":db/id" -> 17592186045447L,
        ":ns/ref1" -> List((":db/id",17592186045448L), (":ref1/int1",22)),
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )
      // Second referenced entity (same as we see included above)
      r2.touchList === List(
        ":db/id" -> 17592186045448L,
        ":ref1/int1" -> 22
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
      m(Ref2.int2.str2 ~ Ns.str.Ref1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "Card-many ref - one value" in new CoreSetup {

      val List(e1, r1, e2, r2) = insert(
        Ref2.int2.str2, Ns.str.Refs1.int1
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )().eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(List((":db/id",17592186045446L), (":ref1/int1",11))),
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )

      // First referenced entity (same as we see included above)
      r1.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 11
      )

      // Second entity (including referenced entity)
      e2.touchList === List(
        ":db/id" -> 17592186045447L,
        ":ns/refs1" -> List(List((":db/id",17592186045448L), (":ref1/int1",22))),
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )
      // Second referenced entity (same as we see included above)
      r2.touchList === List(
        ":db/id" -> 17592186045448L,
        ":ref1/int1" -> 22
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
      m(Ref2.int2.str2 ~ Ns.str.Refs1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }

    "Nested" in new CoreSetup {

      ok
    }

  }
}