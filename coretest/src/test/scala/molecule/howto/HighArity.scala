package molecule
package howto

import molecule.util.dsl.coreTest.Ns
import molecule.util.{CoreSetup, CoreSpec}

class HighArity extends CoreSpec {

  // Compilation times of molecules increase dramatically after about 13-16 attributes length. Hopefully
  // this will get better with faster Scala compilation and hardware over time. We can easily find
  // ourselves in need of inserting a greater chunk of data and transaction meta data that would
  // normally require a long molecule like this one:

  //    val (e1, e2, txId) = Ns.str.int.long.float.double.bool.date.uuid.uri.enum.ref1.refSub1.strs.ints.longs.floats.doubles.bools.dates.uuids.uris.enums.tx_(Ns
  //      .str_(str1)
  //      .int_(int1)
  //      .long_(long1)
  //      .float_(float1)
  //      .double_(double1)
  //      .bool_(bool1)
  //      .date_(date1)
  //      .uuid_(uuid1)
  //      .uri_(uri1)
  //      .enum_(enum1)
  //      .ref1_(10L)
  //      .refSub1_(20L)
  //      .strs_(strs1)
  //      .ints_(ints1)
  //      .longs_(longs1)
  //      .floats_(floats1)
  //      .doubles_(doubles1)
  //      .bools_(bools1)
  //      .dates_(dates1)
  //      .uuids_(uuids1)
  //      .uris_(uris1)
  //      .enums_(enums1)
  //    ) insert List(
  //      (str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1, 11L, 12L, strs1, ints1, longs1, floats1, doubles1, bools1, dates1, uuids1, uris1, enums1),
  //      (str2, int2, long2, float2, double2, bool2, date2, uuid2, uri2, enum2, 10L, 20L, strs2, ints2, longs2, floats2, doubles2, bools2, dates2, uuids2, uris2, enums2)
  //    ) eids

  // It is commented out on purpose since it would take too long to compile and
  // unnecessarily slow down our development (uncomment to see how typeinterference works).

  // In special cases where we need to work with larger entities we can split the data into
  // sub-lists each containing a number of sub-tuples matching a corresponding sub-molecule.
  // As an example, instead of the above mega-molecule and maxed-out 22-arity tuples of
  // insertion data, we can use the `splitInsertTx` method as shown in the following tests:


  // Test reference data
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
    ":ns/ref1" -> ":ns/dateMap",
    ":ns/refSub1" -> ":ns/dateMapK",
    ":ns/str" -> "a",
    ":ns/strs" -> List("b", "c"),
    ":ns/uri" -> uri1,
    ":ns/uris" -> List(uri2, uri3),
    ":ns/uuid" -> uuid1,
    ":ns/uuids" -> List(uuid2)
  )

  "Split + transaction meta data" >> {


    "Split into 2" in new CoreSetup {

      // Inserting high-arity data
      val List(e1, e2, txId) = splitInsertTx(
        // Two sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums.float.floats,
        Ns.int.ints.long.longs.ref1.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Two sub-lists of unzipped data tuples type-safely matching the two molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3"), 1f, Set(2f, 3f)),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"), 4f, Set(5f, 6f))
        ),
        Seq(
          (1, Set(2, 3), 1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (4, Set(5, 6), 4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )

      )(
        // Transaction meta data
        Ns.str_("use case").strs_(Set("John, Lisa"))
      ).eids

      // Note how this creates only two entities and a single transaction entity! 
      // Each matching line of the two sub-lists are merged at runtime to be inserted as
      // one set of attribute values for an entity. Let's confirm it:

      e1.touchList === entity1data

      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 2
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )

      // Transaction meta data
      Ns.str_.tx_(Ns.str).get === List("use case")
    }


    "Split into 3" in new CoreSetup {

      val List(e1, e2, txId) = splitInsertTx(
        // Three sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums,
        Ns.float.floats.int.ints.long.longs.ref1,
        Ns.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Three sub-lists of split data tuples type-safely matching the three sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"))
        ),
        Seq(
          (1f, Set(2f, 3f), 1, Set(2, 3), 1L, Set(2L, 3L), 101L),
          (4f, Set(5f, 6f), 4, Set(5, 6), 4L, Set(5L, 6L), 201L)
        ),
        Seq(
          (102L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (202L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      )(
        // Transaction meta data
        Ns.str_("use case").strs_(Set("John, Lisa"))
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )

      // Transaction meta data
      Ns.str_.tx_(Ns.str).get === List("use case")
    }


    "Split into 4" in new CoreSetup {

      val List(e1, e2, txId) = splitInsertTx(
        // Four sub-molecules
        Ns.bool.bools.date.dates.double.doubles,
        Ns.enum.enums.float.floats.int.ints,
        Ns.long.longs.ref1.refSub1.str.strs,
        Ns.uri.uris.uuid.uuids
      )(
        // Four sub-lists of split data tuples type-safely matching the four sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0)),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0))
        ),
        Seq(
          ("enum1", Set("enum2", "enum3"), 1f, Set(2f, 3f), 1, Set(2, 3)),
          ("enum4", Set("enum5", "enum6"), 4f, Set(5f, 6f), 4, Set(5, 6))
        ),
        Seq(
          (1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c")),
          (4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"))
        ),
        Seq(
          (uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      )(
        Ns.str_("use case").strs_(Set("John, Lisa"))
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )

      // Transaction meta data
      Ns.str_.tx_(Ns.str).get === List("use case")
    }


    "Split into 5" in new CoreSetup {

      val List(e1, e2, txId) = splitInsertTx(
        // Five sub-molecules
        Ns.bool.bools.date.dates,
        Ns.double.doubles.enum.enums,
        Ns.float.floats.int.ints,
        Ns.long.longs.ref1.refSub1.str.strs,
        Ns.uri.uris.uuid.uuids
      )(
        // Five sub-lists of split data tuples type-safely matching the five sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3)),
          (false, Set(false), date4, Set(date5, date6))
        ),
        Seq(
          (1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
          (4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"))
        ),
        Seq(
          (1f, Set(2f, 3f), 1, Set(2, 3)),
          (4f, Set(5f, 6f), 4, Set(5, 6))
        ),
        Seq(
          (1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c")),
          (4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"))
        ),
        Seq(
          (uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      )(
        Ns.str_("use case").strs_(Set("John, Lisa"))
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 2 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )

      // Transaction meta data
      Ns.str_.tx_(Ns.str).get === List("use case")
    }
  }


  "Split" >> {

    "Split into 2" in new CoreSetup {

      // Inserting high-arity data
      val List(e1, e2) = splitInsert(
        // Two sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums.float.floats,
        Ns.int.ints.long.longs.ref1.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Two sub-lists of unzipped data tuples type-safely matching the two molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3"), 1f, Set(2f, 3f)),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"), 4f, Set(5f, 6f))
        ),
        Seq(
          (1, Set(2, 3), 1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (4, Set(5, 6), 4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      ).eids

      // Note how this creates only two entities and a single transaction entity! 
      // Each matching line of the two sub-lists are merged at runtime to be inserted as
      // one set of attribute values for an entity. Let's confirm it:

      e1.touchList === entity1data

      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 2
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )
    }


    "Split into 3" in new CoreSetup {

      val List(e1, e2) = splitInsert(
        // Three sub-molecules
        Ns.bool.bools.date.dates.double.doubles.enum.enums,
        Ns.float.floats.int.ints.long.longs.ref1,
        Ns.refSub1.str.strs.uri.uris.uuid.uuids
      )(
        // Three sub-lists of split data tuples type-safely matching the three sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"))
        ),
        Seq(
          (1f, Set(2f, 3f), 1, Set(2, 3), 1L, Set(2L, 3L), 101L),
          (4f, Set(5f, 6f), 4, Set(5, 6), 4L, Set(5L, 6L), 201L)
        ),
        Seq(
          (102L, "a", Set("b", "c"), uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (202L, "d", Set("e", "f"), uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )
    }


    "Split into 4" in new CoreSetup {

      val List(e1, e2) = splitInsert(
        // Four sub-molecules
        Ns.bool.bools.date.dates.double.doubles,
        Ns.enum.enums.float.floats.int.ints,
        Ns.long.longs.ref1.refSub1.str.strs,
        Ns.uri.uris.uuid.uuids
      )(
        // Four sub-lists of split data tuples type-safely matching the four sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3), 1.0, Set(2.0, 3.0)),
          (false, Set(false), date4, Set(date5, date6), 4.0, Set(5.0, 6.0))
        ),
        Seq(
          ("enum1", Set("enum2", "enum3"), 1f, Set(2f, 3f), 1, Set(2, 3)),
          ("enum4", Set("enum5", "enum6"), 4f, Set(5f, 6f), 4, Set(5, 6))
        ),
        Seq(
          (1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c")),
          (4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"))
        ),
        Seq(
          (uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 1 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )
    }


    "Split into 5" in new CoreSetup {

      val List(e1, e2) = splitInsert(
        // Five sub-molecules
        Ns.bool.bools.date.dates,
        Ns.double.doubles.enum.enums,
        Ns.float.floats.int.ints,
        Ns.long.longs.ref1.refSub1.str.strs,
        Ns.uri.uris.uuid.uuids
      )(
        // Five sub-lists of split data tuples type-safely matching the five sub-molecules above!
        Seq(
          (true, Set(true), date1, Set(date2, date3)),
          (false, Set(false), date4, Set(date5, date6))
        ),
        Seq(
          (1.0, Set(2.0, 3.0), "enum1", Set("enum2", "enum3")),
          (4.0, Set(5.0, 6.0), "enum4", Set("enum5", "enum6"))
        ),
        Seq(
          (1f, Set(2f, 3f), 1, Set(2, 3)),
          (4f, Set(5f, 6f), 4, Set(5, 6))
        ),
        Seq(
          (1L, Set(2L, 3L), 101L, 102L, "a", Set("b", "c")),
          (4L, Set(5L, 6L), 201L, 202L, "d", Set("e", "f"))
        ),
        Seq(
          (uri1, Set(uri2, uri3), uuid1, Set(uuid2)),
          (uri4, Set(uri5, uri6), uuid4, Set(uuid5))
        )
      ).eids

      // Entity 1
      e1.touchList === entity1data

      // Entity 2
      Ns(e2).double.doubles.str.strs.get === List(
        // Values from sub-list 2 have been merged with values from sub-list 3
        (4.0, Set(5.0, 6.0), "d", Set("e", "f"))
      )
    }
  }



  //  "Reference annotations" in new CoreSetup {ok}
  //
  //  "Nested annotations" in new CoreSetup {ok}
  //
  //  "Modifying annotations" in new CoreSetup {ok}
  //
  //  "Retracting annotations" in new CoreSetup {
  //    ok
  //  }
}


//expectCompileError(
//  """Ns.str("str1" and "str2").get""",
//  "[Dsl2Model:apply (4)] Card-one attribute `str` cannot return multiple values.\n"
//    + "A tacet attribute can though have AND expressions to make a self-join.\n"
//    + "If you want this, please make the attribute tacet by appending an underscore: `str_`")