package molecule.ref

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class Composite extends CoreSpec {

  "Arities" >> {

    "1 + 1" in new CoreSetup {

      // Composite of two single-value molecules
      val List(e1, e2) = insert(
        Ref2.int2, Ns.int
      )(Seq(
        // Two rows of data
        (1, 11),
        (2, 22)
      ))().eids

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
      m(Ref2.int2 ~ Ns.int).get.sorted === Seq(
        (1, 11),
        (2, 22)
      )
    }


    "1 + 2" in new CoreSetup {

      // Composite of Molecule2 + Molecule1
      val List(e1, e2) = insert(
        Ref2.int2, Ns.int.str
      )(Seq(
        // Two rows of data
        (1, (11, "aa")),
        (2, (22, "bb"))
      ))().eids

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
      m(Ref2.int2 ~ Ns.int.str).get.sorted === Seq(
        (1, (11, "aa")),
        (2, (22, "bb"))
      )
    }


    "2 + 1" in new CoreSetup {

      // Composite of Molecule2 + Molecule1
      val List(e1, e2) = insert(
        Ref2.int2.str2, Ns.int
      )(Seq(
        // Two rows of data
        ((1, "a"), 11),
        ((2, "b"), 22)
      ))().eids

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
      m(Ref2.int2.str2 ~ Ns.int).get.sorted === Seq(
        ((1, "a"), 11),
        ((2, "b"), 22)
      )
    }


    "2 + 2" in new CoreSetup {

      val List(e1, e2) = insert(
        Ref2.int2.str2, Ns.str.int
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )().eids

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
      m(Ref2.int2.str2 ~ Ns.str.int).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "2 + 3 (2+1tx)" in new CoreSetup {

      // Add transaction meta data
      val List(e1, e2, txId) = insert(
        Ref2.int2.str2, Ref1.str1.int1
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )(
        Ns.str_("Tx meta data")
      ).eids

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
      Ref2.int2.str2.tx_(Ns.str).get.sorted === List(
        (1, "a", "Tx meta data"),
        (2, "b", "Tx meta data")
      )

      // Queries via other namespace
      Ref1.str1.int1.get.sorted === List(
        ("aa", 11),
        ("bb", 22)
      )
      // .. including transaction meta data
      Ref1.str1.int1.tx_(Ns.str).get.sorted === List(
        ("aa", 11, "Tx meta data"),
        ("bb", 22, "Tx meta data")
      )

      // Transaction meta data alone can be accessed through tacet attributes of namespaces
      Ref2.int2_.tx_(Ns.str).get === List("Tx meta data")
      Ref1.int1_.tx_(Ns.str).get === List("Tx meta data")


      // Composite query
      m(Ref2.int2.str2 ~ Ref1.str1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
      // .. including transaction meta data
      m(Ref2.int2.str2 ~ Ref1.str1.int1.tx_(Ns.str)).get.sorted === List(
        ((1, "a"), ("aa", 11, "Tx meta data")),
        ((2, "b"), ("bb", 22, "Tx meta data"))
      )
    }
  }


  "Splitting high-arity data" >> {

    // Compilation times of molecules increase dramatically after about 13-16 attributes length. Hopefully
    // this will get better with faster Scala compilation and hardware over time. We can find ourselves in
    // need of inserting a greater chunk of data and transaction meta data that would normally require a
    // long molecule like this arity-22 molecule:
    //
    //    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums insert List(
    //      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L, strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
    //      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 10L, 20L, strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
    //    )
    //
    // This compile-time checks but is commented out on purpose since it would take too long to compile!
    //
    // In special cases where we need to work with larger high-arity entities we can split the molecule
    // into a composite molecule containing any number of sub-molecules matching sub-tuples of our base data


    // Test reference base data
    val entity1data = List(
      ":db/id" -> 17592186045445L,
      ":ns/bool" -> true,
      ":ns/bools" -> List(true),
      ":ns/date" -> date1,
      ":ns/dates" -> List(date2, date3),
      ":ns/double" -> 1.0,
      ":ns/doubles" -> List(2.0, 3.0),
      ":ns/enum" -> ":ns.enum/enum1",
      ":ns/enums" -> List(":ns.enums/enum3", ":ns.enums/enum2"),
      ":ns/float" -> 1.0,
      ":ns/floats" -> List(2.0, 3.0),
      ":ns/int" -> 1,
      ":ns/ints" -> List(3, 2),
      ":ns/long" -> 1,
      ":ns/longs" -> List(3, 2),
      ":ns/ref1" -> ":db.install/partition",
      ":ns/refSub1" -> ":db.install/valueType",
      ":ns/str" -> "a",
      ":ns/strs" -> List("b", "c"),
      ":ns/uri" -> uri1,
      ":ns/uris" -> List(uri2, uri3),
      ":ns/uuid" -> uuid1,
      ":ns/uuids" -> List(uuid2)
    )

    "Split into 2" in new CoreSetup {

      // Inserting high-arity data as a composite
      val List(e1, e2) = insert(
        // 2 sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums.float.floats,
        Ns.int.ints.long.longs.ref1.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Two rows with tuples of 2 tuples that type-safely match the 2 molecules above!
        Seq(
          (
            (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3"), 1f, Set(2f, 3f)),
            (1, Set(2, 3), 1L, Set(2L, 3L), 11L, 12L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2))
            ),
          (
            (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"), 4f, Set(5f, 6f)),
            (4, Set(5, 6), 4L, Set(5L, 6L), 21L, 22L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
            )
        )
      )().eids

      // First entity has all 22 values
      e1.touchList === entity1data
    }


    "Split into 3" in new CoreSetup {

      val List(e1, e2) = insert(
        // 3 sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums,
        Ns.float.floats.int.ints.long.longs.ref1,
        Ns.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Two rows with tuples of 3 tuples that type-safely match the 3 molecules above!
        Seq(
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
        )
      )().eids

      // First entity has all 22 values
      e1.touchList === entity1data
    }

    // Etc... We can make composites of up to 22 molecules with each having up to 22 attributes
    // adding up to a whopping arity-484 attribute composite!
  }


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
      m(Ref2.int2.str2 ~ Ns.str.Ref1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "Card-many ref - one value" in new CoreSetup {

      val List(e1, r11, e2, r22) = insert(
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
      m(Ref2.int2.str2 ~ Ns.str.Refs1.int1).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
    }


    "Nested" in new CoreSetup {

      // We can insert composites with nested data

      val List(e1, r11, r12, e2, r22, r23) = insert(
        Ref2.int2.str2, Ns.str.Refs1.*(Ref1.int1)
      )(
        Seq(
          ((1, "a"), ("aa", Seq(11, 12))),
          ((2, "b"), ("bb", Seq(22, 23)))
        )
      )().eids

      // First entity (including referenced entity)
      e1.touchList === List(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(
          List((":db/id", 17592186045446L), (":ref1/int1", 11)),
          List((":db/id", 17592186045447L), (":ref1/int1", 12))
        ),
        ":ns/str" -> "aa",
        ":ref2/int2" -> 1,
        ":ref2/str2" -> "a"
      )

      // Nested entities (same as we see included above)
      r11.touchList === List(":db/id" -> 17592186045446L, ":ref1/int1" -> 11)
      r12.touchList === List(":db/id" -> 17592186045447L, ":ref1/int1" -> 12)

      // Second entity (including referenced entity)
      e2.touchList === List(
        ":db/id" -> 17592186045448L,
        ":ns/refs1" -> List(
          List((":db/id", 17592186045449L), (":ref1/int1", 22)),
          List((":db/id", 17592186045450L), (":ref1/int1", 23))
        ),
        ":ns/str" -> "bb",
        ":ref2/int2" -> 2,
        ":ref2/str2" -> "b"
      )

      // Nested entities (same as we see included above)
      r22.touchList === List(":db/id" -> 17592186045449L, ":ref1/int1" -> 22)
      r23.touchList === List(":db/id" -> 17592186045450L, ":ref1/int1" -> 23)

      // Queries via each namespace
      Ref2.int2.str2.get.sorted === List(
        (1, "a"),
        (2, "b")
      )

      // Fetching composites with nested data not yet supported
      expectCompileError(
        "m(Ref2.int2.str2 ~ Ns.str.Refs1.*(Ref1.int1))",
        "[Dsl2Model:apply (6)] Nested molecules in composites not yet implemented (todo)"
      )

      // We can though fetch the nested data separately - although that wouldn't take the
      // first part (`Ref2.int2.str2`) of the inserted composite into consideration!
      Ns.str.Refs1.*(Ref1.int1).get === List(
        ("aa", Seq(11, 12)),
        ("bb", Seq(22, 23))
      )

      // This becomes obvious if we add some more nested data in isolation:
      m(Ns.str.Refs1.*(Ref1.int1)) insert List(
        ("cc", Seq(33, 34))
      )

      // Isolated retrieval will get all the nested data
      Ns.str.Refs1.*(Ref1.int1).get === List(
        ("aa", Seq(11, 12)),
        ("bb", Seq(22, 23)),
        ("cc", Seq(33, 34))
      )

      // .. but a flat composite retrieval (without the nested data grouped)
      // only get the data associated with the first part of the composite too:
      m(Ref2.int2.str2 ~ Ns.str.Refs1.int1).get === List(
        ((2, "b"), ("bb", 22)),
        ((2, "b"), ("bb", 23)),
        ((1, "a"), ("aa", 12)),
        ((1, "a"), ("aa", 11))
      )
    }
  }


  "Duplicate attributes" >> {

    "Twice on top level not allowed" in new CoreSetup {

      // Since the `insert` method will compose the two molecules into one
      // composite molecule this will have two conflicting `Ns.int` attributes
      (insert(
        Ns.int, Ns.int
      )(Seq(
        (1, 11),
        (2, 22)
      ))() must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
        s"[DatomicFacade:insertMerged] Can't insert same attribute :ns/int twice"

      /*
      If we didn't catch the duplicate in the model before trying to insert data, Datomic would
      have warned us with a

      java.lang.IllegalArgumentException: :db.error/datoms-conflict Two datoms in the same transaction conflict
      {:d1 [17592186045445 :ns/int 1 13194139534340 true],
       :d2 [17592186045445 :ns/int 11 13194139534340 true]}
      */
    }


    "Twice on different levels ok" in new CoreSetup {

      // Okay to repeat attribute in _referenced_ namespace

      val List(e1, r11, e2, r22) = insert(
        Ref1.int1, Ns.str.Ref1.int1
      )(Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      ))().eids

      m(Ns.str).get.sorted === Seq("aa", "bb")
      m(Ns.str.Ref1.int1).get.sorted === Seq(("aa", 11), ("bb", 22))

      // Note how 4 Ref1.int1 values have been inserted!
      m(Ref1.int1).get.sorted === Seq(1, 2, 11, 22)

      // Composite query
      m(Ref1.int1 ~ Ns.str.Ref1.int1).get.sorted === Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      )
    }


    "Twice in referenced namespace ok" in new CoreSetup {

      // The same composite can't make the same reference twice
      (insert(
        Ns.bool.Ref1.int1, Ns.str.Ref1.int1
      )(Seq(
        ((true, 1), ("aa", 11)),
        ((false, 2), ("bb", 22))
      ))() must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
        s"[DatomicFacade:insertMerged] Can't insert with same reference :ns/ref1 twice"
    }
  }


  "Expressions" >> {

    "Constraints apply to merged model" in new CoreSetup {

      insert(
        Ref2.int2.str2, Ns.str.int
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )().eids

      // Without constraints
      m(Ref2.int2.str2 ~ Ns.str.int).get.sorted === List(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )

      // One constraint
      m(Ref2.int2.>(1).str2 ~ Ns.str.int).get.sorted === List(
        ((2, "b"), ("bb", 22))
      )
      // Other constraint
      m(Ref2.int2.str2 ~ Ns.str.int.<(20)).get.sorted === List(
        ((1, "a"), ("aa", 11))
      )

      // Mutually exclusive constraints
      m(Ref2.int2.>(1).str2 ~ Ns.str.int.<(20)).get.sorted === List()

      // Constraints with all-matching data
      m(Ref2.int2.<(2).str2 ~ Ns.str.int.<(20)).get.sorted === List(
        ((1, "a"), ("aa", 11))
      )
    }


    "Null values" in new CoreSetup {

      // Composite data
      insert(
        Ref2.int2.str2, Ns.str.int
      )(
        Seq(
          ((1, "a"), ("aa", 11)),
          ((2, "b"), ("bb", 22))
        )
      )().eids

      // Add partial composite data
      insert(
        Ref2.int2.str2, Ns.int
      )(
        Seq(
          ((3, "c"), 33)
        )
      )().eids

      // Non-composite query gets all data
      m(Ref2.int2.str2).get.sorted === Seq(
        (1, "a"),
        (2, "b"),
        (3, "c")
      )

      // Composite query gets composite data only that has a `Ns.str` value too
      m(Ref2.int2.str2 ~ Ns.str.int).get.sorted === Seq(
        ((1, "a"), ("aa", 11)),
        ((2, "b"), ("bb", 22))
      )
      m(Ref2.int2.str2 ~ Ns.str).get.sorted === Seq(
        ((1, "a"), "aa"),
        ((2, "b"), "bb")
      )

      // Composite data with `Ns.int` values retrieve all 3 entities
      m(Ref2.int2.str2 ~ Ns.int).get.sorted === Seq(
        ((1, "a"), 11),
        ((2, "b"), 22),
        ((3, "c"), 33)
      )

      // Composite data that _doesn't_ have a `Ns.str` value retrieve only the last entity
      m(Ref2.int2.str2 ~ Ns.str_(nil).int).get.sorted === Seq(
        ((3, "c"), 33)
      )
    }
  }
}