package molecule.coretests.expression

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class Logic extends Base {

  "Card one - OR" in new OneSetup {

    // 3 ways of applying the same OR-semantics:

    // 1. `or`-separated values
    Ns.str("c" or "a").get === List("a")

    // 2. Comma-separated values
    Ns.str("c", "a").get === List("a")

    // 3. List of values
    Ns.str(List("c", "a")).get.toSeq.sorted === List("a")


    // order of values not relevant
    Ns.str("a", "c").get === List("a")
    Ns.str("a" or "c").get === List("a")
    Ns.str(List("a", "c")).get === List("a")

    // No limit on number of applied values
    Ns.str("a", "b", "c", "d").get.toSeq.sorted === List("a", "b")
    Ns.str("a" or "b" or "c" or "d").get.toSeq.sorted === List("a", "b")
    Ns.str(List("a", "b", "c", "d")).get.toSeq.sorted === List("a", "b")


    // Can't apply same value

    expectCompileError(
      """Ns.str("a", "a").get""",
      "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/str`:" +
        "\na")

    expectCompileError(
      """Ns.str("a" or "a").get""",
      "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/str`:" +
        "\na")

    expectCompileError(
      """Ns.str(List("a", "a")).get""",
      "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/str`:" +
        "\na")


    // Applying non-matching values returns empty result
    Ns.str("c", "d").get === List()
    Ns.str("c" or "d").get === List()
    Ns.str(List("c", "d")).get === List()

    // Empty/space character values treated as strings too
    Ns.str("", "    ").get === List("")
    Ns.str("", " ").get.toSeq.sorted === List("", " ")

    // We can apply values assigned to variables
    Ns.str(str1 or str2).get.toSeq.sorted === List("a", "b")
    Ns.str(str1, str2).get.toSeq.sorted === List("a", "b")
    Ns.str(List(str1, str2)).get.toSeq.sorted === List("a", "b")
    val strList = List(str1, str2)
    Ns.str(strList).get.toSeq.sorted === List("a", "b")

    // We can mix variables and static values
    Ns.str(str1 or "b").get.toSeq.sorted === List("a", "b")
    Ns.str("a", str2).get.toSeq.sorted === List("a", "b")
    Ns.str(List(str1, "b")).get.toSeq.sorted === List("a", "b")
    val strListMixed = List("a", str2)
    Ns.str(strListMixed).get.toSeq.sorted === List("a", "b")


    Ns.int(-1, 0, 1).get.toSeq.sorted === List(-1, 0, 1)
    Ns.int(0, 1, 2).get.toSeq.sorted === List(0, 1, 2)
    Ns.int(1, 2, 3).get.toSeq.sorted === List(1, 2)
    Ns.int(2, 3, 4).get.toSeq.sorted === List(2)
    Ns.int(3, 4, 5).get.toSeq.sorted === List()
    Ns.int(int1, int2).get.toSeq.sorted === List(1, 2)
    Ns.int(int1 or int2).get.toSeq.sorted === List(1, 2)
    Ns.int(List(int1, int2)).get.toSeq.sorted === List(1, 2)
    val intList = List(int1, int2)
    Ns.int(intList).get.toSeq.sorted === List(1, 2)


    Ns.long(-1L, 0L, 1L).get.toSeq.sorted === List(-1L, 0L, 1L)
    Ns.long(0L, 1L, 2L).get.toSeq.sorted === List(0L, 1L, 2L)
    Ns.long(1L, 2L, 3L).get.toSeq.sorted === List(1L, 2L)
    Ns.long(2L, 3L, 4L).get.toSeq.sorted === List(2L)
    Ns.long(3L, 4L, 5L).get.toSeq.sorted === List()
    Ns.long(long1, long2).get.toSeq.sorted === List(1L, 2L)
    Ns.long(long1 or long2).get.toSeq.sorted === List(1L, 2L)
    Ns.long(List(long1, long2)).get.toSeq.sorted === List(1L, 2L)
    val longList = List(long1, long2)
    Ns.long(longList).get.toSeq.sorted === List(1L, 2L)


    Ns.float(-1f, 0f, 1f).get.toSeq.sorted === List(-1f, 0f, 1f)
    Ns.float(0f, 1f, 2f).get.toSeq.sorted === List(0f, 1f, 2f)
    Ns.float(1f, 2f, 3f).get.toSeq.sorted === List(1f, 2f)
    Ns.float(2f, 3f, 4f).get.toSeq.sorted === List(2f)
    Ns.float(3f, 4f, 5f).get.toSeq.sorted === List()
    Ns.float(float1, float2).get.toSeq.sorted === List(1f, 2f)
    Ns.float(float1 or float2).get.toSeq.sorted === List(1f, 2f)
    Ns.float(List(float1, float2)).get.toSeq.sorted === List(1f, 2f)
    val floatList = List(float1, float2)
    Ns.float(floatList).get.toSeq.sorted === List(1.0f, 2.0f)


    Ns.double(-1.0, 0.0, 1.0).get.toSeq.sorted === List(-1.0, 0.0, 1.0)
    Ns.double(0.0, 1.0, 2.0).get.toSeq.sorted === List(0.0, 1.0, 2.0)
    Ns.double(1.0, 2.0, 3.0).get.toSeq.sorted === List(1.0, 2.0)
    Ns.double(2.0, 3.0, 4.0).get.toSeq.sorted === List(2.0)
    Ns.double(3.0, 4.0, 5.0).get.toSeq.sorted === List()
    Ns.double(double1, double2).get.toSeq.sorted === List(1.0, 2.0)
    Ns.double(double1 or double2).get.toSeq.sorted === List(1.0, 2.0)
    Ns.double(List(double1, double2)).get.toSeq.sorted === List(1.0, 2.0)
    val doubleList = List(double1, double2)
    Ns.double(doubleList).get.toSeq.sorted === List(1.0, 2.0)


    // Weird case though to apply OR-semantics to Boolean attribute...
    Ns.bool(true or false).get === List(false, true)
    Ns.bool(true, false).get === List(false, true)
    Ns.bool(bool0, bool1).get === List(false, true)
    Ns.bool(bool0 or bool1).get === List(false, true)
    Ns.bool(List(bool0, bool1)).get === List(false, true)
    val boolList = List(bool1, bool2)
    Ns.bool(boolList).get.toSeq.sorted === List(false, true)


    val now = new java.util.Date()
    Ns.date(date1, now).get === List(date1)
    Ns.date(date1, date2).get.toSeq.sorted === List(date1, date2)
    Ns.date(date1 or date2).get.toSeq.sorted === List(date1, date2)
    Ns.date(List(date1, date2)).get.toSeq.sorted === List(date1, date2)
    val dateList = List(date1, date2)
    Ns.date(dateList).get.toSeq.sorted === List(date1, date2)


    Ns.uuid(uuid1, uuid2).get.toSeq.sortBy(_.toString) === List(uuid1, uuid2)
    Ns.uuid(uuid1 or uuid2).get.toSeq.sortBy(_.toString) === List(uuid1, uuid2)
    Ns.uuid(List(uuid1, uuid2)).get.toSeq.sortBy(_.toString) === List(uuid1, uuid2)
    val uuidList = List(uuid1, uuid2)
    Ns.uuid(uuidList).get.toSeq.sortBy(_.toString) === List(uuid1, uuid2)


    Ns.uri(uri1, uri2).get.toSeq.sortBy(_.toString) === List(uri1, uri2)
    Ns.uri(uri1 or uri2).get.toSeq.sortBy(_.toString) === List(uri1, uri2)
    Ns.uri(List(uri1, uri2)).get.toSeq.sortBy(_.toString) === List(uri1, uri2)
    val uriList = List(uri1, uri2)
    Ns.uri(uriList).get.toSeq.sortBy(_.toString) === List(uri1, uri2)


    Ns.enum("enum1", "enum2").get.toSeq.sorted === List("enum1", "enum2")
    Ns.enum(enum1, enum2).get.toSeq.sorted === List("enum1", "enum2")
    Ns.enum(enum1 or enum2).get.toSeq.sorted === List("enum1", "enum2")
    Ns.enum(List(enum1, enum2)).get.toSeq.sorted === List("enum1", "enum2")
    val enumList = List(enum1, enum2)
    Ns.enum(enumList).get.toSeq.sorted === List(enum1, enum2)
  }


  "Card many - OR" in new ManySetup {

    // If no additional attribute is present to unify, multiple sets are
    // coalesced to one set with unique values:
    // Set("a", "b") + Set("b", "c") -> Set("a", "b", "c")

    // 3 ways of applying the same OR-semantics:

    // 1. `or`-separated values
    Ns.strs("a" or "c").get === List(Set("a", "b", "c"))

    // 2. Comma-separated values
    Ns.strs("a", "b").get === List(Set("a", "b", "c"))
    Ns.strs("a", "c").get === List(Set("a", "b", "c"))
    Ns.strs("a", "d").get === List(Set("d", "a", "ba", "b"))

    // 3. Set of values (note that this differs from card-one attributes that use Seq/List)
    Ns.strs(Set("a", "c")).get === List(Set("a", "b", "c"))

    // Unifying results by another attribute prevents the card-many attribute values to coalesce
    Ns.str.strs("c" or "d").get === List(
      ("str2", Set("b", "c")),
      ("str3", Set("d", "ba"))
    )

    // Using variables
    Ns.strs(a or c).get === List(Set("a", "b", "c"))
    Ns.strs(a, c).get === List(Set("a", "b", "c"))
    Ns.strs(Set(a, c)).get === List(Set("a", "b", "c"))
    val strSet = Set(a, c)
    Ns.strs(strSet).get === List(Set("a", "b", "c"))

    // We can even apply multiple comma-separated Sets of search values
    Ns.strs(Set(a, c), Set(d)).get === List(Set("ba", "a", "b", "c", "d"))

    // (variables with variables are resolved too)
    Ns.strs.apply(strSet, Set(d)).get === List(Set("ba", "a", "b", "c", "d"))


    Ns.ints(1 or 3).get === List(Set(1, 2, 3))
    Ns.ints(Set(1, 3)).get === List(Set(1, 2, 3))
    Ns.ints(1, 2).get === List(Set(1, 4, 3, 2))
    Ns.ints(1, 3).get === List(Set(1, 2, 3))
    Ns.ints(1, 4).get === List(Set(1, 2, 4))

    Ns.ints(int1 or int3).get === List(Set(1, 2, 3))
    Ns.ints(Set(int1, int3)).get === List(Set(1, 2, 3))
    Ns.ints(int1, int3).get === List(Set(1, 2, 3))
    val intSet = Set(int1, int3)
    Ns.ints(intSet).get === List(Set(1, 2, 3))


    Ns.longs(1L or 3L).get === List(Set(1L, 2L, 3L))
    Ns.longs(Set(1L, 3L)).get === List(Set(1L, 2L, 3L))
    Ns.longs(1L, 2L).get === List(Set(1L, 4L, 3L, 2L))
    Ns.longs(1L, 3L).get === List(Set(1L, 2L, 3L))
    Ns.longs(1L, 4L).get === List(Set(1L, 2L, 4L))

    Ns.longs(long1 or long3).get === List(Set(1L, 2L, 3L))
    Ns.longs(Set(long1, long3)).get === List(Set(1L, 2L, 3L))
    Ns.longs(long1, long3).get === List(Set(1L, 2L, 3L))
    val longSet = Set(long1, long3)
    Ns.longs(longSet).get === List(Set(1L, 2L, 3L))


    Ns.floats(1.0f or 3.0f).get === List(Set(1.0f, 2.0f, 3.0f))
    Ns.floats(Set(1.0f, 3.0f)).get === List(Set(1.0f, 2.0f, 3.0f))
    Ns.floats(1.0f, 2.0f).get === List(Set(2.0f, 1.0f, 3.0f))
    Ns.floats(1.0f, 3.0f).get === List(Set(1.0f, 2.0f, 3.0f))
    Ns.floats(1.0f, 4.0f).get === List(Set(2.0f, 4.0f, 1.0f, 2.5f))

    Ns.floats(float1 or float3).get === List(Set(1.0f, 2.0f, 3.0f))
    Ns.floats(Set(float1, float3)).get === List(Set(1.0f, 2.0f, 3.0f))
    Ns.floats(float1, float3).get === List(Set(1.0f, 2.0f, 3.0f))
    val floatSet = Set(float1, float3)
    Ns.floats(floatSet).get === List(Set(1.0f, 2.0f, 3.0f))


    Ns.doubles(1.0 or 3.0).get === List(Set(1.0, 2.0, 3.0))
    Ns.doubles(Set(1.0, 3.0)).get === List(Set(1.0, 2.0, 3.0))
    Ns.doubles(1.0, 2.0).get === List(Set(2.0, 1.0, 3.0))
    Ns.doubles(1.0, 3.0).get === List(Set(1.0, 2.0, 3.0))
    Ns.doubles(1.0, 4.0).get === List(Set(2.0, 4.0, 1.0, 2.5))

    Ns.doubles(double1 or double3).get === List(Set(1.0, 2.0, 3.0))
    Ns.doubles(Set(double1, double3)).get === List(Set(1.0, 2.0, 3.0))
    Ns.doubles(double1, double3).get === List(Set(1.0, 2.0, 3.0))
    val doubleSet = Set(double1, double3)
    Ns.doubles(doubleSet).get === List(Set(1.0, 2.0, 3.0))


    // Cardinality-many attribute for boolean values not implemented (doesn't make sense)


    Ns.dates(date1 or date3).get === List(Set(date1, date2, date3))
    Ns.dates(Set(date1, date3)).get === List(Set(date1, date2, date3))
    Ns.dates(date1, date2).get === List(Set(date1, date4, date3, date2))
    Ns.dates(date1, date3).get === List(Set(date1, date2, date3))
    Ns.dates(date1, date4).get === List(Set(date1, date2, date4))
    val dateSet = Set(date1, date3)
    Ns.dates(dateSet).get === List(Set(date1, date2, date3))


    //    Ns.uris(uri1 or uri2).get === List(Set(uri1, uri4, uri3, uri2))
    //    Ns.uris(Set(uri1, uri2)).get === List(Set(uri1, uri4, uri3, uri2))
    //    Ns.uris(uri1, uri2).get === List(Set(uri1, uri4, uri3, uri2))
    //    Ns.uris(uri1, uri3).get === List(Set(uri1, uri2, uri3))
    //    Ns.uris(uri1, uri4).get === List(Set(uri1, uri2, uri4))
    //    val uriSet = Set(uri1, uri3)
    //    Ns.uris(uriSet).get === List(Set(uri1, uri2, uri3))


    Ns.uuids(uuid1 or uuid3).get === List(Set(uuid1, uuid2, uuid3))
    Ns.uuids(Set(uuid1, uuid3)).get === List(Set(uuid1, uuid2, uuid3))
    Ns.uuids(uuid1, uuid2).get === List(Set(uuid1, uuid4, uuid3, uuid2))
    Ns.uuids(uuid1, uuid3).get === List(Set(uuid1, uuid2, uuid3))
    Ns.uuids(uuid1, uuid4).get === List(Set(uuid1, uuid2, uuid4))
    val uuidSet = Set(uuid1, uuid3)
    Ns.uuids(uuidSet).get === List(Set(uuid1, uuid2, uuid3))


    Ns.enums("enum1" or "enum3").get === List(Set("enum1", "enum3", "enum2"))
    Ns.enums(Set("enum1", "enum3")).get === List(Set("enum1", "enum3", "enum2"))
    Ns.enums(enum1, enum2).get === List(Set(enum1, enum4, enum3, enum2))
    Ns.enums(enum1, enum3).get === List(Set(enum1, enum2, enum3))
    Ns.enums(enum1, enum4).get === List(Set(enum1, enum2, enum4))
    val enumSet = Set(enum1, enum3)
    Ns.enums(enumSet).get === List(Set(enum1, enum2, enum3))
  }


  "Card many - AND" in new ManySetup {

    Ns.str.strs("a" and "b").get === List(("str1", Set("a", "b")))
    Ns.str.strs(a and b).get === List(("str1", Set("a", "b")))

    // a and c are not present in any one set
    Ns.str.strs("a" and "c").get === List()

    Ns.int.ints(1 and 2).get === List((1, Set(1, 2)))
    Ns.int.ints(1 and 3).get === List()

    Ns.long.longs(1L and 2L).get === List((1L, Set(1L, 2L)))
    Ns.long.longs(1L and 3L).get === List()

    Ns.float.floats(1f and 2f).get === List((1f, Set(1f, 2f)))
    Ns.float.floats(1f and 3f).get === List()

    Ns.double.doubles(1.0 and 2.0).get === List((1.0, Set(1.0, 2.0)))
    Ns.double.doubles(1.0 and 3.0).get === List()

    Ns.date.dates(date1 and date2).get === List((date1, Set(date1, date2)))
    Ns.date.dates(date1 and date3).get === List()

    Ns.uuid.uuids(uuid1 and uuid2).get === List((uuid1, Set(uuid1, uuid2)))
    Ns.uuid.uuids(uuid1 and uuid3).get === List()

    Ns.enum.enums(enum1 and enum2).get === List((enum1, Set(enum1, enum2)))
    Ns.enum.enums(enum1 and enum3).get === List()


    // Multiple AND's (max 2 AND's implemented at the moment)
    Ns.int.strs insert List(
      (1, Set("a", "b", "c", "d")),
      (2, Set("a", "b", "c", "e")),
      (3, Set("a", "b", "x", "e"))
    )
    Ns.int.strs("a" and "b" and "c").get === List(
      (1, Set("a", "b", "c", "d")),
      (2, Set("a", "b", "c", "e"))
    )
    Ns.int.strs("a" and "b" and "d").get === List(
      (1, Set("a", "b", "c", "d"))
    )
  }
}