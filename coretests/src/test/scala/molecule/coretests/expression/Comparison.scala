package molecule.coretests.expression

import molecule.api._
import molecule.coretests.util.dsl.coreTest._

class Comparison extends Base {

  "Card one" in new OneSetup {

    Ns.str.<("").get.sorted === List()
    Ns.str.<(" ").get.sorted === List("")
    Ns.str.<(",").get.sorted === List("", " ")
    Ns.str.<(".").get.sorted === List("", " ", ",")
    Ns.str.<("?").get.sorted === List("", " ", ",", ".")
    Ns.str.<("A").get.sorted === List("", " ", ",", ".", "?")
    Ns.str.<("B").get.sorted === List("", " ", ",", ".", "?", "A")
    Ns.str.<("a").get.sorted === List("", " ", ",", ".", "?", "A", "B")
    Ns.str.<("b").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
    Ns.str.<("d").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.<(str1).get.sorted === List("", " ", ",", ".", "?", "A", "B")

    Ns.str.>("").get.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.>(" ").get.sorted === List(",", ".", "?", "A", "B", "a", "b")
    Ns.str.>(",").get.sorted === List(".", "?", "A", "B", "a", "b")
    Ns.str.>(".").get.sorted === List("?", "A", "B", "a", "b")
    Ns.str.>("?").get.sorted === List("A", "B", "a", "b")
    Ns.str.>("A").get.sorted === List("B", "a", "b")
    Ns.str.>("B").get.sorted === List("a", "b")
    Ns.str.>("C").get.sorted === List("a", "b")
    Ns.str.>("a").get.sorted === List("b")
    Ns.str.>("b").get.sorted === List()
    Ns.str.>(str1).get.sorted === List("b")

    Ns.str.<=("").get.sorted === List("")
    Ns.str.<=(" ").get.sorted === List("", " ")
    Ns.str.<=(",").get.sorted === List("", " ", ",")
    Ns.str.<=(".").get.sorted === List("", " ", ",", ".")
    Ns.str.<=("?").get.sorted === List("", " ", ",", ".", "?")
    Ns.str.<=("A").get.sorted === List("", " ", ",", ".", "?", "A")
    Ns.str.<=("B").get.sorted === List("", " ", ",", ".", "?", "A", "B")
    Ns.str.<=("a").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
    Ns.str.<=("b").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.<=(str1).get.sorted === List("", " ", ",", ".", "?", "A", "B", "a")

    Ns.str.>=("").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.>=(" ").get.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.>=(",").get.sorted === List(",", ".", "?", "A", "B", "a", "b")
    Ns.str.>=(".").get.sorted === List(".", "?", "A", "B", "a", "b")
    Ns.str.>=("?").get.sorted === List("?", "A", "B", "a", "b")
    Ns.str.>=("A").get.sorted === List("A", "B", "a", "b")
    Ns.str.>=("B").get.sorted === List("B", "a", "b")
    Ns.str.>=("a").get.sorted === List("a", "b")
    Ns.str.>=("b").get.sorted === List("b")
    Ns.str.>=("c").get.sorted === List()
    Ns.str.>=(str1).get.sorted === List("a", "b")


    Ns.int.<(-2).get.sorted === List()
    Ns.int.<(0).get.sorted === List(-2, -1)
    Ns.int.<(2).get.sorted === List(-2, -1, 0, 1)
    Ns.int.<(int1).get.sorted === List(-2, -1, 0)

    Ns.int.>(2).get.sorted === List()
    Ns.int.>(0).get.sorted === List(1, 2)
    Ns.int.>(-2).get.sorted === List(-1, 0, 1, 2)
    Ns.int.>(int1).get.sorted === List(2)

    Ns.int.<=(-2).get.sorted === List(-2)
    Ns.int.<=(0).get.sorted === List(-2, -1, 0)
    Ns.int.<=(2).get.sorted === List(-2, -1, 0, 1, 2)
    Ns.int.<=(int1).get.sorted === List(-2, -1, 0, 1)

    Ns.int.>=(2).get.sorted === List(2)
    Ns.int.>=(0).get.sorted === List(0, 1, 2)
    Ns.int.>=(-2).get.sorted === List(-2, -1, 0, 1, 2)
    Ns.int.>=(int1).get.sorted === List(1, 2)


    Ns.long.<(-2L).get.sorted === List()
    Ns.long.<(0L).get.sorted === List(-2L, -1L)
    Ns.long.<(2L).get.sorted === List(-2L, -1L, 0L, 1L)
    Ns.long.<(long1).get.sorted === List(-2L, -1L, 0L)

    Ns.long.>(2L).get.sorted === List()
    Ns.long.>(0L).get.sorted === List(1L, 2L)
    Ns.long.>(-2L).get.sorted === List(-1L, 0L, 1L, 2L)
    Ns.long.>(long1).get.sorted === List(2L)

    Ns.long.<=(-2L).get.sorted === List(-2L)
    Ns.long.<=(0L).get.sorted === List(-2L, -1L, 0L)
    Ns.long.<=(2L).get.sorted === List(-2L, -1L, 0L, 1L, 2L)
    Ns.long.<=(long1).get.sorted === List(-2L, -1L, 0L, 1L)

    Ns.long.>=(2L).get.sorted === List(2L)
    Ns.long.>=(0L).get.sorted === List(0L, 1L, 2L)
    Ns.long.>=(-2L).get.sorted === List(-2L, -1L, 0L, 1L, 2L)
    Ns.long.>=(long1).get.sorted === List(1L, 2L)


    Ns.float.<(-2f).get.sorted === List()
    Ns.float.<(0f).get.sorted === List(-2f, -1f)
    Ns.float.<(2f).get.sorted === List(-2f, -1f, 0f, 1f)
    Ns.float.<(float1).get.sorted === List(-2f, -1f, 0f)

    Ns.float.>(2f).get.sorted === List()
    Ns.float.>(0f).get.sorted === List(1f, 2f)
    Ns.float.>(-2f).get.sorted === List(-1f, 0f, 1f, 2f)
    Ns.float.>(float1).get.sorted === List(2f)

    Ns.float.<=(-2f).get.sorted === List(-2f)
    Ns.float.<=(0f).get.sorted === List(-2f, -1f, 0f)
    Ns.float.<=(2f).get.sorted === List(-2f, -1f, 0f, 1f, 2f)
    Ns.float.<=(float1).get.sorted === List(-2f, -1f, 0f, 1f)

    Ns.float.>=(2f).get.sorted === List(2f)
    Ns.float.>=(0f).get.sorted === List(0f, 1f, 2f)
    Ns.float.>=(float1).get.sorted === List(1f, 2f)


    Ns.double.<(-2.0).get.sorted === List()
    Ns.double.<(0.0).get.sorted === List(-2.0, -1.0)
    Ns.double.<(2.0).get.sorted === List(-2.0, -1.0, 0.0, 1.0)
    Ns.double.<(double1).get.sorted === List(-2.0, -1.0, 0.0)

    Ns.double.>(2.0).get.sorted === List()
    Ns.double.>(0.0).get.sorted === List(1.0, 2.0)
    Ns.double.>(-2.0).get.sorted === List(-1.0, 0.0, 1.0, 2.0)
    Ns.double.>(double1).get.sorted === List(2.0)

    Ns.double.<=(-2.0).get.sorted === List(-2.0)
    Ns.double.<=(0.0).get.sorted === List(-2.0, -1.0, 0.0)
    Ns.double.<=(2.0).get.sorted === List(-2.0, -1.0, 0.0, 1.0, 2.0)
    Ns.double.<=(double1).get.sorted === List(-2.0, -1.0, 0.0, 1.0)

    Ns.double.>=(2.0).get.sorted === List(2.0)
    Ns.double.>=(0.0).get.sorted === List(0.0, 1.0, 2.0)
    Ns.double.>=(-2.0).get.sorted === List(-2.0, -1.0, 0.0, 1.0, 2.0)
    Ns.double.>=(double1).get.sorted === List(1.0, 2.0)


    Ns.bool.<(true).get.sorted === List(false)
    Ns.bool.<(false).get.sorted === List()
    Ns.bool.<(bool0).get.sorted === List()

    Ns.bool.>(true).get.sorted === List()
    Ns.bool.>(false).get.sorted === List(true)
    Ns.bool.>(bool0).get.sorted === List(true)

    Ns.bool.<=(true).get.sorted === List(false, true)
    Ns.bool.<=(false).get.sorted === List(false)
    Ns.bool.<=(bool0).get.sorted === List(false)

    Ns.bool.>=(true).get.sorted === List(true)
    Ns.bool.>=(false).get.sorted === List(false, true)
    Ns.bool.>=(bool0).get.sorted === List(false, true)


    Ns.date.<(date1).get.sorted === List(date0)
    Ns.date.<(date2).get.sorted === List(date0, date1)

    Ns.date.>(date1).get.sorted === List(date2)
    Ns.date.>(date0).get.sorted === List(date1, date2)

    Ns.date.<=(date1).get.sorted === List(date0, date1)
    Ns.date.<=(date2).get.sorted === List(date0, date1, date2)

    Ns.date.>=(date1).get.sorted === List(date1, date2)
    Ns.date.>=(date0).get.sorted === List(date0, date1, date2)


    // Comparison of random UUIDs omitted...
    // Comparison of URIs not implemented


    Ns.enum.<("enum1").get.sorted === List("enum0")
    Ns.enum.>("enum1").get.sorted === List("enum2")
    Ns.enum.<=("enum1").get.sorted === List("enum0", "enum1")
    Ns.enum.>=("enum1").get.sorted === List("enum1", "enum2")

    Ns.enum.<(enum1).get.sorted === List("enum0")
    Ns.enum.>(enum1).get.sorted === List("enum2")
    Ns.enum.<=(enum1).get.sorted === List("enum0", "enum1")
    Ns.enum.>=(enum1).get.sorted === List("enum1", "enum2")


    Ns.bigInt.<(bigInt1).get.sorted === List(bigInt0)
    Ns.bigInt.<(bigInt2).get.sorted === List(bigInt0, bigInt1)

    Ns.bigInt.>(bigInt1).get.sorted === List(bigInt2)
    Ns.bigInt.>(bigInt0).get.sorted === List(bigInt1, bigInt2)

    Ns.bigInt.<=(bigInt1).get.sorted === List(bigInt0, bigInt1)
    Ns.bigInt.<=(bigInt2).get.sorted === List(bigInt0, bigInt1, bigInt2)

    Ns.bigInt.>=(bigInt1).get.sorted === List(bigInt1, bigInt2)
    Ns.bigInt.>=(bigInt0).get.sorted === List(bigInt0, bigInt1, bigInt2)


    Ns.bigDec.<(bigDec1).get.sorted === List(bigDec0)
    Ns.bigDec.<(bigDec2).get.sorted === List(bigDec0, bigDec1)

    Ns.bigDec.>(bigDec1).get.sorted === List(bigDec2)
    Ns.bigDec.>(bigDec0).get.sorted === List(bigDec1, bigDec2)

    Ns.bigDec.<=(bigDec1).get.sorted === List(bigDec0, bigDec1)
    Ns.bigDec.<=(bigDec2).get.sorted === List(bigDec0, bigDec1, bigDec2)

    Ns.bigDec.>=(bigDec1).get.sorted === List(bigDec1, bigDec2)
    Ns.bigDec.>=(bigDec0).get.sorted === List(bigDec0, bigDec1, bigDec2)
  }


  "Range" in new OneSetup {
    Ns.int.insert(1, 2, 3, 4)

    Ns.int_.>(1).int.<(4).get === List(2, 3)
    Ns.int_.>(1).int.<=(4).get === List(2, 3, 4)
    Ns.int_.>=(1).int.<(4).get === List(1, 2, 3)
    Ns.int_.>=(1).int.<=(4).get === List(1, 2, 3, 4)


    Ns.int_.<(4).int.>(1).get === List(2, 3)
    Ns.int_.<=(4).int.>(1).get === List(2, 3, 4)
    Ns.int_.<(4).int.>=(1).get === List(1, 2, 3)
    Ns.int_.<=(4).int.>=(1).get === List(1, 2, 3, 4)
  }


  "Card many" in new ManySetup {

    // String

    // Note how 'ba' is "greater than" 'b'
    // (See java.util.String.compareTo)
    Ns.str.strs.>("b").get === List(
      ("str2", Set("c")),
      ("str3", Set("d", "ba"))
    )
    Ns.str.strs.>=("b").get === List(
      ("str1", Set("b")),
      ("str2", Set("b", "c")),
      ("str3", Set("d", "ba"))
    )
    Ns.str.strs.<=("b").get === List(
      ("str1", Set("a", "b")),
      ("str2", Set("b"))
    )
    Ns.str.strs.<("b").get === List(
      ("str1", Set("a"))
    )
    Ns.str.strs_.>("b").get.sorted === List("str2", "str3")
    Ns.str.strs_.>=("b").get.sorted === List("str1", "str2", "str3")
    Ns.str.strs_.<=("b").get.sorted === List("str1", "str2")
    Ns.str.strs_.<("b").get.sorted === List("str1")


    // Int

    Ns.int.ints.>(2).get === List(
      (2, Set(3)),
      (3, Set(4))
    )
    Ns.int.ints.>=(2).get === List(
      (1, Set(2)),
      (2, Set(2, 3)),
      (3, Set(4, 2))
    )
    Ns.int.ints.<=(2).get === List(
      (1, Set(1, 2)),
      (2, Set(2)),
      (3, Set(2))
    )
    Ns.int.ints.<(2).get === List(
      (1, Set(1))
    )
    Ns.int.ints_.>(2).get.sorted === List(2, 3)
    Ns.int.ints_.>=(2).get.sorted === List(1, 2, 3)
    Ns.int.ints_.<=(2).get.sorted === List(1, 2, 3)
    Ns.int.ints_.<(2).get.sorted === List(1)


    // Float

    Ns.float.floats.>(2f).get === List(
      (2f, Set(3f)),
      (3f, Set(4f, 2.5f))
    )
    Ns.float.floats.>=(2f).get === List(
      (1f, Set(2f)),
      (2f, Set(2f, 3f)),
      (3f, Set(4f, 2.5f))
    )
    Ns.float.floats.<=(2f).get === List(
      (1f, Set(1f, 2f)),
      (2f, Set(2f))
    )
    Ns.float.floats.<(2f).get === List(
      (1f, Set(1f))
    )
    Ns.float.floats_.>(2f).get.sorted === List(2f, 3f)
    Ns.float.floats_.>=(2f).get.sorted === List(1f, 2f, 3f)
    Ns.float.floats_.<=(2f).get.sorted === List(1f, 2f)
    Ns.float.floats_.<(2f).get.sorted === List(1f)


    // Date

    Ns.date.dates.>(date2).get === List(
      (date2, Set(date3)),
      (date3, Set(date4))
    )
    Ns.date.dates.>=(date2).get === List(
      (date1, Set(date2)),
      (date2, Set(date2, date3)),
      (date3, Set(date4, date2))
    )
    Ns.date.dates.<=(date2).get === List(
      (date1, Set(date1, date2)),
      (date2, Set(date2)),
      (date3, Set(date2))
    )
    Ns.date.dates.<(date2).get === List(
      (date1, Set(date1))
    )
    Ns.date.dates_.>(date2).get.sorted === List(date2, date3)
    Ns.date.dates_.>=(date2).get.sorted === List(date1, date2, date3)
    Ns.date.dates_.<=(date2).get.sorted === List(date1, date2, date3)
    Ns.date.dates_.<(date2).get.sorted === List(date1)


    // UUID (comparisons not of much relevance - only works here because we sorted the values)

    Ns.uuid.uuids.>(uuid2).get === List(
      (uuid2, Set(uuid3)),
      (uuid3, Set(uuid4))
    )
    Ns.uuid.uuids.>=(uuid2).get === List(
      (uuid1, Set(uuid2)),
      (uuid2, Set(uuid2, uuid3)),
      (uuid3, Set(uuid4, uuid2))
    )
    Ns.uuid.uuids.<=(uuid2).get === List(
      (uuid1, Set(uuid1, uuid2)),
      (uuid2, Set(uuid2)),
      (uuid3, Set(uuid2))
    )
    Ns.uuid.uuids.<(uuid2).get === List(
      (uuid1, Set(uuid1))
    )
    Ns.uuid.uuids_.>(uuid2).get.sorted === List(uuid2, uuid3)
    Ns.uuid.uuids_.>=(uuid2).get.sorted === List(uuid1, uuid2, uuid3)
    Ns.uuid.uuids_.<=(uuid2).get.sorted === List(uuid1, uuid2, uuid3)
    Ns.uuid.uuids_.<(uuid2).get.sorted === List(uuid1)


    Ns.bigInt.bigInts.>(bigInt2).get === List(
      (bigInt2, Set(bigInt3)),
      (bigInt3, Set(bigInt4))
    )
    Ns.bigInt.bigInts.>=(bigInt2).get === List(
      (bigInt1, Set(bigInt2)),
      (bigInt2, Set(bigInt2, bigInt3)),
      (bigInt3, Set(bigInt4, bigInt2))
    )
    Ns.bigInt.bigInts.<=(bigInt2).get === List(
      (bigInt1, Set(bigInt1, bigInt2)),
      (bigInt2, Set(bigInt2)),
      (bigInt3, Set(bigInt2))
    )
    Ns.bigInt.bigInts.<(bigInt2).get === List(
      (bigInt1, Set(bigInt1))
    )
    Ns.bigInt.bigInts_.>(bigInt2).get.sorted === List(bigInt2, bigInt3)
    Ns.bigInt.bigInts_.>=(bigInt2).get.sorted === List(bigInt1, bigInt2, bigInt3)
    Ns.bigInt.bigInts_.<=(bigInt2).get.sorted === List(bigInt1, bigInt2, bigInt3)
    Ns.bigInt.bigInts_.<(bigInt2).get.sorted === List(bigInt1)


    Ns.bigDec.bigDecs.>(bigDec2).get === List(
      (bigDec2, Set(bigDec3)),
      (bigDec3, Set(bigDec4))
    )
    Ns.bigDec.bigDecs.>=(bigDec2).get === List(
      (bigDec1, Set(bigDec2)),
      (bigDec2, Set(bigDec2, bigDec3)),
      (bigDec3, Set(bigDec4, bigDec2))
    )
    Ns.bigDec.bigDecs.<=(bigDec2).get === List(
      (bigDec1, Set(bigDec1, bigDec2)),
      (bigDec2, Set(bigDec2)),
      (bigDec3, Set(bigDec2))
    )
    Ns.bigDec.bigDecs.<(bigDec2).get === List(
      (bigDec1, Set(bigDec1))
    )
    Ns.bigDec.bigDecs_.>(bigDec2).get.sorted === List(bigDec2, bigDec3)
    Ns.bigDec.bigDecs_.>=(bigDec2).get.sorted === List(bigDec1, bigDec2, bigDec3)
    Ns.bigDec.bigDecs_.<=(bigDec2).get.sorted === List(bigDec1, bigDec2, bigDec3)
    Ns.bigDec.bigDecs_.<(bigDec2).get.sorted === List(bigDec1)
  }
}