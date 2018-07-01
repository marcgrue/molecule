package molecule.coretests.attr

import molecule.imports._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._

class Attribute extends CoreSpec {


  "Single cardinality-1 attribute - one entity" in new CoreSetup {

    // Insert single value for one cardinality-1 attribute
    Ns.str insert str1
    Ns.int insert int1
    Ns.long insert long1
    Ns.float insert float1
    Ns.double insert double1
    Ns.bool insert bool1
    Ns.date insert date1
    Ns.uuid insert uuid1
    Ns.uri insert uri1
    Ns.enum insert enum1

    // Calling `get` on explicit molecule
    m(Ns.str).get === List(str1)

    // Calling `get` on implicit molecule
    Ns.str.get === List(str1)
    Ns.str.get(1) === List(str1)

    // Get optional value
    Ns.str.get.headOption === Some(str1)

    // Single value from list
    Ns.str.get.head === str1
    // same as
    Ns.str.get.head === str1

    // Get one value (RuntimeException if no value)
    Ns.str.get.head === str1
    Ns.int.get.head === int1
    Ns.long.get.head === long1
    Ns.float.get.head === float1
    Ns.double.get.head === double1
    Ns.bool.get.head === bool1
    Ns.date.get.head === date1
    Ns.uuid.get.head === uuid1
    Ns.uri.get.head === uri1
    Ns.enum.get.head === enum1
  }

  "Single cardinality-2 attribute - one entity" in new CoreSetup {

    // Insert single Set of value(s) for one cardinality-2 attribute
    Ns.strs insert strs1 // Set("str1")
    Ns.ints insert ints1
    Ns.longs insert longs1
    Ns.floats insert floats1
    Ns.doubles insert doubles1
    // Set of boolean values doesn't make sense
    Ns.dates insert dates1
    Ns.uuids insert uuids1
    Ns.uris insert uris1
    Ns.enums insert enums1

    // Get one value (RuntimeException if no value)
    Ns.strs.get.head === strs1
    Ns.ints.get.head === ints1
    Ns.longs.get.head === longs1
    Ns.floats.get.head === floats1
    Ns.doubles.get.head === doubles1
    // No Set of boolean values
    Ns.dates.get.head === dates1
    Ns.uuids.get.head === uuids1
    Ns.uris.get.head === uris1
    Ns.enums.get.head === enums1
  }

//
//  "Single cardinality-1 attribute - multiple entities" in new CoreSetup {
//
//    // Insert multiple values for one cardinality-1 attribute
//    Ns.str insert List(str1, str2)
//    Ns.int insert List(int1, int2)
//    Ns.long insert List(long1, long2)
//    Ns.float insert List(float1, float2)
//    Ns.double insert List(double1, double2)
//    Ns.bool insert List(bool1, bool2)
//    Ns.date insert List(date1, date2)
//    Ns.uuid insert List(uuid1, uuid2)
//    Ns.uri insert List(uri1, uri2)
//    Ns.enum insert List(enum1, enum2)
//
//    // Get attribute values as tuples (order not guaranteed)
//    Ns.str.get === List(str1, str2)
//    Ns.int.get === List(int1, int2)
//    Ns.long.get === List(long1, long2)
//    Ns.float.get === List(float1, float2)
//    Ns.double.get === List(double1, double2)
//    Ns.bool.get === List(bool2, bool1)
//    Ns.date.get.sorted === List(date1, date2)
//    Ns.uuid.get.sortBy(_.toString) === List(uuid1, uuid2)
//    Ns.uri.get === List(uri1, uri2)
//    Ns.enum.get === List(enum2, enum1)
//  }
//
//  "Single cardinality-2 attribute - multiple entities" in new CoreSetup {
//
//    // Insert multiple values for one cardinality-2 attribute
//    Ns.strs insert List(strs1, strs2) // List(Set("str1"), Set("str1", "str2"))
//    Ns.ints insert List(ints1, ints2)
//    Ns.longs insert List(longs1, longs2)
//    Ns.floats insert List(floats1, floats2)
//    Ns.doubles insert List(doubles1, doubles2)
//    // No Set of boolean values
//    Ns.dates insert List(dates1, dates2)
//    Ns.uuids insert List(uuids1, uuids2)
//    Ns.uris insert List(uris1, uris2)
//    Ns.enums insert List(enums1, enums2)
//
//    // Retrieving Sets only will retrieve one Set of distinct values for each attribute
//    Ns.strs.get === List(strs2) // List(Set("str1", "str2"))
//    Ns.ints.get === List(ints2)
//    Ns.longs.get === List(longs2)
//    Ns.floats.get === List(floats2)
//    Ns.doubles.get === List(doubles2)
//    // No Set of boolean values
//    Ns.dates.get === List(dates2)
//    Ns.uuids.get === List(uuids2)
//    Ns.uris.get === List(uris2)
//    Ns.enums.get === List(enums2)
//  }
//
//
//  "Multiple cardinality-1 attributes - one entity" in new CoreSetup {
//
//    // Insert single molecule with comma-separated values
//    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.insert(
//      str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1)
//
//    // Get single molecule as tuple of values
//    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.head ===(
//      str1, int1, long1, float1, double1, bool1, date1, uuid1, uri1, enum1)
//  }
//
//  "Multiple cardinality-2 attributes - one entity" in new CoreSetup {
//
//    // Insert single molecule with comma-separated Sets of values
//    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.insert(
//      strs1, ints1, longs1, floats1, doubles1, dates1, uuids1, uris1, enums1)
//
//    // Get single molecule as tuple of Sets of values
//    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head ===(
//      strs1, ints1, longs1, floats1, doubles1, dates1, uuids1, uris1, enums1)
//  }
//
//
//  "Multiple cardinality-1 attributes - multiple entities" in new CoreSetup {
//
//    // Insert two molecules with tuples of values
//    Ns.str.int.long.float.double.date.uuid.uri.enum insert List(
//      (str1, int1, long1, float1, double1, date1, uuid1, uri1, enum1),
//      (str2, int2, long2, float2, double2, date2, uuid2, uri2, enum2)
//    )
//
//    // Get two molecules as tuples of values
//    Ns.str.int.long.float.double.date.uuid.uri.enum.get.sortBy(_._1.toString) === List(
//      (str1, int1, long1, float1, double1, date1, uuid1, uri1, enum1),
//      (str2, int2, long2, float2, double2, date2, uuid2, uri2, enum2)
//    )
//  }
//
//  "Multiple cardinality-2 attributes - multiple entities" in new CoreSetup {
//
//    // Insert two molecules with tuples of Sets of values
//    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums insert List(
//      (strs1, ints1, longs1, floats1, doubles1, dates1, uuids1, uris1, enums1),
//      (strs2, ints2, longs2, floats2, doubles2, dates2, uuids2, uris2, enums2)
//    )
//
//    // Retrieving Sets only will retrieve one Set of distinct values for each attribute
//    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get === List(
//      (strs2, ints2, longs2, floats2, doubles2, dates2, uuids2, uris2, enums2)
//    )
//  }
//
//
//  "Mixed cardinality attributes - one entity" in new CoreSetup {
//    Ns.str.ints.bool.insert("foo", Set(1, 2, 3), true)
//    Ns.str.ints.bool.get === List(("foo", Set(1, 2, 3), true))
//  }
//
//  "Mixed cardinality attributes - multiple entities" in new CoreSetup {
//    Ns.str.ints.bool insert List(("foo", Set(1, 2), true), ("bar", Set(2, 3, 4), false))
//
//    // (order not guaranteed)
//    Ns.str.ints.bool.get === List(("bar", Set(2, 3, 4), false), ("foo", Set(1, 2), true))
//  }
}