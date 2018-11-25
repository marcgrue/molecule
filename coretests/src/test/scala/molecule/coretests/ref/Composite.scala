package molecule.coretests.ref

import molecule.api.in3_out22._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.util.expectCompileError

class Composite extends CoreSpec {


  "Arities" >> {

    "1 + 1" in new CoreSetup {

      // Composite of two single-value molecules
      val List(e1, e2) = Ref2.int2 + Ns.int insert Seq(
        // Two rows of data
        (1, 11),
        (2, 22)
      ) eids

      // Two entities created
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/int" -> 11,
        ":ref2/int2" -> 1
      )
      e2.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ns/int" -> 22,
        ":ref2/int2" -> 2
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
        ":db/id" -> 17592186045445L,
        ":ns/int" -> 11,
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1
      )
      e2.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ns/int" -> 22,
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2
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
        ":db/id" -> 17592186045445L,
        ":ns/int" -> 11,
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )
      e2.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ns/int" -> 22,
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
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
        ":db/id" -> 17592186045445L,
        ":ns/int" -> 11,
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )
      e2.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ns/int" -> 22,
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
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
      val List(e1, e2, txId) = Ref2.int2.str2 + Ref1.str1.int1.Tx(Ns.str_("Tx meta data")) insert Seq(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      ) eids

      // Three (!) entities created
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ref1/int1" -> 11,
        ":ref1/str1" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )
      e2.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 22,
        ":ref1/str1" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )
      txId.touchList === List(
        ":db/id" -> 13194139534340L,
        ":db/txInstant" -> txId(":db/txInstant").get,
        ":ns/str" -> "Tx meta data")

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


  "tacits" in new CoreSetup {

    Ref2.int2.str2$ + Ref1.int1.str1$ + Ns.int.str$ insert Seq(
      ((1, Some("a")), (11, Some("aa")), (111, Some("aaa"))),
      ((2, Some("b")), (22, Some("bb")), (222, None)),
      ((3, Some("c")), (33, None), (333, None)),
      ((4, None), (44, None), (444, None)),
    )


    // Same namespace

    m(Ref2.int2.str2).get.sorted === List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )
    // When 1 + 1 attribute, this outcome will be the same
    m(Ref2.int2 + Ref2.str2).get.sorted === List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )

    m(Ref2.int2).get.sorted === List(1, 2, 3, 4)

    m(Ref2.int2 + Ref2.str2_).get.sorted === List(1, 2, 3)
    // Order irrelevant
    m(Ref2.str2_ + Ref2.int2).get.sorted === List(1, 2, 3)

    m(Ref1.int1 + Ref1.str1_).get.sorted === List(11, 22)

    m(Ns.int + Ns.str_).get === List(111)


    // 2 namespaces, 1 tacit

    m(Ref2.int2 + Ref1.str1_).get.sorted === List(1, 2)
    m(Ref2.int2 + Ns.str_).get === List(1)
    m(Ref1.int1 + Ns.str_).get === List(11)


    // 3 namespaces, 2 tacits

    m(Ref2.int2 + Ref1.str1_ + Ns.str_).get === List(1)
    m(Ref2.str2_ + Ref1.int1 + Ns.str_).get === List(11)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int).get.sorted === List(111, 222)


    // 3 namespaces, 3 tacits, 4 composite parts (to test second `+` method)

    m(Ref2.int2 + Ref1.str1_ + Ns.int_ + Ns.str_).get === List(1)
    m(Ref2.str2_ + Ref1.int1 + Ns.int_ + Ns.str_).get === List(11)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int + Ns.str_).get === List(111)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int_ + Ns.str).get === List("aaa")
  }


  "Arity 22+" in new CoreSetup {

    // A single molecule can have up to 22 attributes
    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums insert List(
      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L, strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 10L, 20L, strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
    )

    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.get === List(
      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L, strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 10L, 20L, strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
    )

    // Molecules needing more than 22 attributes can be composed as composites. Here we compose a composite molecule with 22 + 9 = 31 attributes in total:
    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums +
      Ref1.str1.int1.enum1.ref2.refSub2.strs1.ints1.refs2.refsSub2 insert List(
      (
        (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, long3, long4, strs1, ints1, longs0, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
        (str2, int2, enum11, long5, long6, strs2, ints2, longs1, longs2)
      )
    )


    // Retrieving composite data as Lists of tuples of two sub tuples, first of arity 22, and second of arity 9
    m(Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums +
      Ref1.str1.int1.enum1.ref2.refSub2.strs1.ints1.refs2.refsSub2).get === List(
      (
        (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, long3, long4, strs1, ints1, longs0, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
        (str2, int2, enum11, long5, long6, strs2, ints2, longs1, longs2)
      )
    )
  }


  "Split into 3" in new CoreSetup {

    // Insert composite data with 3 sub-molecules
    Ns.bool.bools.date.dates.double.doubles.enum.enums +
      Ns.float.floats.int.ints.long.longs.ref1 +
      Ns.refSub1.str.strs.uri.uris.uuid.uuids insert Seq(
      // Two rows with tuples of 3 sub-tuples that type-safely match the 3 sub-molecules above
      (
        (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
        (1f, Set(2f, 3f), 1, Set(2, 3), 1L, Set(2L, 3L), 11L),
        (12L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2))
      ),
      (
        (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6")),
        (4f, Set(5f, 6f), 4, Set(5, 6), 4L, Set(5L, 6L), 21L),
        (22L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
      )
    ) eids

    // Retrieve composite data with the same 3 sub-molecules
    m(Ns.bool.bools.date.dates.double.doubles.enum.enums +
      Ns.float.floats.int.ints.long.longs.ref1 +
      Ns.refSub1.str.strs.uri.uris.uuid.uuids).get === Seq(
      (
        (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6")),
        (4f, Set(5f, 6f), 4, Set(5, 6), 4L, Set(5L, 6L), 21L),
        (22L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
      ),
      (
        (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
        (1f, Set(2f, 3f), 1, Set(2, 3), 1L, Set(2L, 3L), 11L),
        (12L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2))
      )
    )

    // Subsets of the composite data can be retrieved too
    m(Ns.bool.bools.date.dates +
      Ns.float.floats.int +
      Ns.refSub1.str.strs).get === Seq(
      (
        (false, Set(false), date4, Set(date5, date6)),
        (4f, Set(5f, 6f), 4),
        (22L, "d", Set("e", "f"))
      ),
      (
        (true, Set(true), date1, Set(date2, date3)),
        (1f, Set(2f, 3f), 1),
        (12L, "a", Set("b", "c"))
      )
    )
  }

  // Etc... We can make composites of up to 22 molecules with each having up to 22 attributes
  // adding up to a whopping arity-484 attribute composite!


  "References" >> {

    "Card-one ref" in new CoreSetup {

      val List(e1, r1, e2, r2) = Ref2.int2.str2 + Ns.str.Ref1.int1 insert Seq(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      ) eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/ref1" -> List((":db/id", 17592186045446L), (":ref1/int1", 11)),
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
        ":ns/ref1" -> List((":db/id", 17592186045448L), (":ref1/int1", 22)),
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
      m(Ref2.int2.str2 + Ns.str.Ref1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "Card-many ref - one value" in new CoreSetup {

      val List(e1, r11, e2, r22) = Ref2.int2.str2 + Ns.str.Refs1.int1 insert Seq(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      ) eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(List((":db/id", 17592186045446L), (":ref1/int1", 11))),
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )

      // First referenced entity (same as we see included above)
      r11.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 11
      )

      // Second entity (including referenced entity)
      e2.touchList === List(
        ":db/id" -> 17592186045447L,
        ":ns/refs1" -> List(List((":db/id", 17592186045448L), (":ref1/int1", 22))),
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )
      // Second referenced entity (same as we see included above)
      r22.touchList === List(
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
      m(Ref2.int2.str2 + Ns.str.Refs1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "Card-many ref - one value 2" in new CoreSetup {

      val List(e1, r11, e2, r22) = Ref2.int2.str2 + Ns.Refs1.int1 insert Seq(
        ((1, "a"), 11),
        ((2, "b"), 22)
      ) eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(List((":db/id", 17592186045446L), (":ref1/int1", 11))),
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )

      // First referenced entity (same as we see included above)
      r11.touchList === List(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 11
      )

      // Second entity (including referenced entity)
      e2.touchList === List(
        ":db/id" -> 17592186045447L,
        ":ns/refs1" -> List(List((":db/id", 17592186045448L), (":ref1/int1", 22))),
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )
      // Second referenced entity (same as we see included above)
      r22.touchList === List(
        ":db/id" -> 17592186045448L,
        ":ref1/int1" -> 22
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
        ((1, "a"), Set(17592186045446L)),
        ((2, "b"), Set(17592186045448L))
      )
    }
  }


  "Duplicate attributes" >> {

    "Duplicate attrs/refs with same entity on top-level not allowed" in new CoreSetup {

      // 1 + 1 attr
      expectCompileError(
        "m(Ns.int + Ns.int)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:ns/int`")

      // 0 + 2 attr
      expectCompileError(
        "m(Ns.int + Ns.str.str)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:ns/str`")

      // 1 + 1 ref
      expectCompileError(
        "m(Ns.bool.Ref1.int1 + Ns.str.Ref1.int1)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite molecules can't contain the same ref more than once. Found multiple instances of `:ns/ref1`")

      // 0 + 2 attr after backref
      expectCompileError(
        "m(Ns.int + Ref1.int1.Ref2.int2._Ref1.int1)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:ref1/int1`")

      // 0 + 2 ref after backref
      expectCompileError(
        "m(Ns.int + Ref1.int1.Ref2.int2._Ref1.str1.Ref2.str2)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite molecules can't contain the same ref more than once. Found multiple instances of `:ref1/ref2`")

      ok
    }


    "Duplicate attrs/refs with same entity within sub-molecule not allowed" in new CoreSetup {

      // 2 attr
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.int1)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:ref1/int1`")

      // 2 attr after backref
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.Ref2.int2._Ref1.int1)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:ref1/int1`")

      // 2 ref
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.Ref2.int2._Ref1.str1.Ref2.str2)",
        "molecule.transform.exception.Dsl2ModelException: " +
          "Composite sub-molecules can't contain the same ref more than once. Found multiple instances of `:ref1/ref2`")

      ok
    }


    "Twice on different levels ok" in new CoreSetup {

      // Okay to repeat attribute in _referenced_ namespace

      val List(e1, r11, e2, r22) = Ref1.int1 + Ns.str.Ref1.int1 insert Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      ) eids

      m(Ns.str).get.sorted === Seq("aa", "bb")
      m(Ns.str.Ref1.int1).get.sorted === Seq(("aa", 11), ("bb", 22))

      // Note how 4 Ref1.int1 values have been inserted!
      m(Ref1.int1).get.sorted === Seq(1, 2, 11, 22)

      // Composite query
      m(Ref1.int1 + Ns.str.Ref1.int1).get.sorted === Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      )
    }
  }


  "Expressions" >> {

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

  "Input" >> {

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


  "Save" in new CoreSetup {
    // 1 + 1
    m(Ns.int(1) + Ref2.int2(11)).save
    m(Ns.int(1) + Ref2.int2(11)).get.head === (1, 11)

    // n + 1
    m(Ns.int(2).str("b") + Ref2.int2(22)).save
    m(Ns.int(2).str("b") + Ref2.int2(22)).get.head === ((2, "b"), 22)

    // 1 + n
    m(Ns.int(3) + Ref2.int2(33).str2("cc")).save
    m(Ns.int(3) + Ref2.int2(33).str2("cc")).get.head === (3, (33, "cc"))

    // n + n
    m(Ns.int(4).str("d") + Ref2.int2(44).str2("dd")).save
    m(Ns.int(4).str("d") + Ref2.int2(44).str2("dd")).get.head === ((4, "d"), (44, "dd"))

    // All sub-molecules share the same entity id
    val e5 = m(Ns.int(5).Ref1.int1(55) + Ref2.int2(555)).save.eid
    Ns(e5).int.Ref1.int1.get.head === (5, 55)
    Ref2(e5).int2.get.head === 555
    m(Ns(e5).int.Ref1.int1 + Ref2.int2).get.head === ((5, 55), 555)

    // Sub-molecules can point straight to other entities (without any attributes of their own)
    val e6 = m(Ns.Ref1.int1(6) + Ref2.int2(66)).save.eid
    m(Ns(e6).Ref1.int1).get.head === 6
    m(Ref2(e6).int2).get.head === 66
    m(Ns(e6).Ref1.int1 + Ref2.int2).get.head === (6, 66)
  }
}