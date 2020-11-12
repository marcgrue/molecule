package molecule.coretests.ref.composite

import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.datomic.api.in3_out9._

class CompositeInput extends CoreSpec {

  "1" in new CoreSetup {

    Ref2.int2 + Ref1.int1 + Ns.int insert Seq(
      (1, 11, 111),
      (2, 22, 222)
    )

    m(Ref2.int2 + Ref1.int1 + Ns.int).get.sorted === List(
      (1, 11, 111),
      (2, 22, 222)
    )

    // 1 + 0 + 0
    m(Ref2.int2(?) + Ref1.int1 + Ns.int).apply(1).get === List(
      (1, 11, 111)
    )
    // 0 + 1 + 0
    m(Ref2.int2 + Ref1.int1(?) + Ns.int).apply(11).get === List(
      (1, 11, 111)
    )
    // 0 + 0 + 1
    m(Ref2.int2 + Ref1.int1 + Ns.int(?)).apply(111).get === List(
      (1, 11, 111)
    )

    // 1 + 1 + 0
    m(Ref2.int2(?) + Ref1.int1(?) + Ns.int).apply(1, 11).get === List(
      (1, 11, 111)
    )
    // 1 + 0 + 1
    m(Ref2.int2(?) + Ref1.int1 + Ns.int(?)).apply(1, 111).get === List(
      (1, 11, 111)
    )
    // 0 + 1 + 1
    m(Ref2.int2 + Ref1.int1(?) + Ns.int(?)).apply(11, 111).get === List(
      (1, 11, 111)
    )

    // 1 + 1 + 1
    m(Ref2.int2(?) + Ref1.int1(?) + Ns.int(?)).apply(1, 11, 111).get === List(
      (1, 11, 111)
    )
  }


  "2" in new CoreSetup {

    Ref2.int2.str2 + Ref1.int1.str1 + Ns.int.str insert Seq(
      ((1, "a"), (11, "aa"), (111, "aaa")),
      ((2, "b"), (22, "bb"), (222, "bbb"))
    )

    // 2 + 0 + 0
    m(Ref2.int2.str2 + Ref1.int1.str1 + Ns.int.str).get.sorted === List(
      ((1, "a"), (11, "aa"), (111, "aaa")),
      ((2, "b"), (22, "bb"), (222, "bbb"))
    )

    // 2 + 0 + 0
    m(Ref2.int2(?).str2(?) + Ref1.int1.str1 + Ns.int.str).apply(1, "a").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )

    // 0 + 2 + 0
    m(Ref2.int2.str2 + Ref1.int1(?).str1(?) + Ns.int.str).apply(11, "aa").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )

    // 0 + 0 + 2
    m(Ref2.int2.str2 + Ref1.int1.str1 + Ns.int(?).str(?)).apply(111, "aaa").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )

    // 2 + 1 + 0
    m(Ref2.int2(?).str2(?) + Ref1.int1(?).str1 + Ns.int.str).apply(1, "a", 11).get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )
    // 2 + 0 + 1
    m(Ref2.int2(?).str2(?) + Ref1.int1.str1 + Ns.int(?).str).apply(1, "a", 111).get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )

    // 1 + 2 + 0
    m(Ref2.int2(?).str2 + Ref1.int1(?).str1(?) + Ns.int.str).apply(1, 11, "aa").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )
    // 0 + 2 + 1
    m(Ref2.int2.str2 + Ref1.int1(?).str1(?) + Ns.int(?).str).apply(11, "aa", 111).get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )

    // 1 + 0 + 2
    m(Ref2.int2(?).str2 + Ref1.int1.str1 + Ns.int(?).str(?)).apply(1, 111, "aaa").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )
    // 0 + 1 + 2
    m(Ref2.int2.str2 + Ref1.int1(?).str1 + Ns.int(?).str(?)).apply(11, 111, "aaa").get === List(
      ((1, "a"), (11, "aa"), (111, "aaa"))
    )
  }


  "3" in new CoreSetup {

    Ref2.int2.str2.enum2 + Ref1.int1.str1.enum1 + Ns.int.str.enum insert Seq(
      ((1, "a", "enum21"), (11, "aa", "enum11"), (111, "aaa", "enum1")),
      ((2, "b", "enum22"), (22, "bb", "enum11"), (222, "bbb", "enum2"))
    )

    m(Ref2.int2.str2.enum2 + Ref1.int1.str1.enum1 + Ns.int.str.enum).get.sorted === List(
      ((1, "a", "enum21"), (11, "aa", "enum11"), (111, "aaa", "enum1")),
      ((2, "b", "enum22"), (22, "bb", "enum11"), (222, "bbb", "enum2"))
    )

    // 3 + 0 + 0
    m(Ref2.int2(?).str2(?).enum2(?) + Ref1.int1.str1.enum1 + Ns.int.str.enum).apply(1, "a", "enum21").get === List(
      ((1, "a", "enum21"), (11, "aa", "enum11"), (111, "aaa", "enum1"))
    )
    // 0 + 3 + 0
    m(Ref2.int2.str2.enum2 + Ref1.int1(?).str1(?).enum1(?) + Ns.int.str.enum).apply(11, "aa", "enum11").get === List(
      ((1, "a", "enum21"), (11, "aa", "enum11"), (111, "aaa", "enum1"))
    )
    // 0 + 0 + 3
    m(Ref2.int2.str2.enum2 + Ref1.int1.str1.enum1 + Ns.int(?).str(?).enum(?)).apply(111, "aaa", "enum1").get === List(
      ((1, "a", "enum21"), (11, "aa", "enum11"), (111, "aaa", "enum1"))
    )
  }
}