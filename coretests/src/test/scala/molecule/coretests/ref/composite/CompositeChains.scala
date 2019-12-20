package molecule.coretests.ref.composite

import molecule.api.in3_out22._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class CompositeChains extends CoreSpec {


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

}