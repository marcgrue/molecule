package molecule.coretests.expression

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._

class Comparison extends Base {

    "Card one" in new OneSetup {

      Ns.str.<("").get.toSeq.sorted === List()
      Ns.str.<(" ").get.toSeq.sorted === List("")
      Ns.str.<(",").get.toSeq.sorted === List("", " ")
      Ns.str.<(".").get.toSeq.sorted === List("", " ", ",")
      Ns.str.<("?").get.toSeq.sorted === List("", " ", ",", ".")
      Ns.str.<("A").get.toSeq.sorted === List("", " ", ",", ".", "?")
      Ns.str.<("B").get.toSeq.sorted === List("", " ", ",", ".", "?", "A")
      Ns.str.<("a").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B")
      Ns.str.<("b").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
      Ns.str.<("d").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
      Ns.str.<(str1).get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B")

      Ns.str.>("").get.toSeq.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
      Ns.str.>(" ").get.toSeq.sorted === List(",", ".", "?", "A", "B", "a", "b")
      Ns.str.>(",").get.toSeq.sorted === List(".", "?", "A", "B", "a", "b")
      Ns.str.>(".").get.toSeq.sorted === List("?", "A", "B", "a", "b")
      Ns.str.>("?").get.toSeq.sorted === List("A", "B", "a", "b")
      Ns.str.>("A").get.toSeq.sorted === List("B", "a", "b")
      Ns.str.>("B").get.toSeq.sorted === List("a", "b")
      Ns.str.>("C").get.toSeq.sorted === List("a", "b")
      Ns.str.>("a").get.toSeq.sorted === List("b")
      Ns.str.>("b").get.toSeq.sorted === List()
      Ns.str.>(str1).get.toSeq.sorted === List("b")

      Ns.str.<=("").get.toSeq.sorted === List("")
      Ns.str.<=(" ").get.toSeq.sorted === List("", " ")
      Ns.str.<=(",").get.toSeq.sorted === List("", " ", ",")
      Ns.str.<=(".").get.toSeq.sorted === List("", " ", ",", ".")
      Ns.str.<=("?").get.toSeq.sorted === List("", " ", ",", ".", "?")
      Ns.str.<=("A").get.toSeq.sorted === List("", " ", ",", ".", "?", "A")
      Ns.str.<=("B").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B")
      Ns.str.<=("a").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
      Ns.str.<=("b").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
      Ns.str.<=(str1).get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a")

      Ns.str.>=("").get.toSeq.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
      Ns.str.>=(" ").get.toSeq.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
      Ns.str.>=(",").get.toSeq.sorted === List(",", ".", "?", "A", "B", "a", "b")
      Ns.str.>=(".").get.toSeq.sorted === List(".", "?", "A", "B", "a", "b")
      Ns.str.>=("?").get.toSeq.sorted === List("?", "A", "B", "a", "b")
      Ns.str.>=("A").get.toSeq.sorted === List("A", "B", "a", "b")
      Ns.str.>=("B").get.toSeq.sorted === List("B", "a", "b")
      Ns.str.>=("a").get.toSeq.sorted === List("a", "b")
      Ns.str.>=("b").get.toSeq.sorted === List("b")
      Ns.str.>=("c").get.toSeq.sorted === List()
      Ns.str.>=(str1).get.toSeq.sorted === List("a", "b")


      Ns.int.<(-2).get.toSeq.sorted === List()
      Ns.int.<(0).get.toSeq.sorted === List(-2, -1)
      Ns.int.<(2).get.toSeq.sorted === List(-2, -1, 0, 1)
      Ns.int.<(int1).get.toSeq.sorted === List(-2, -1, 0)

      Ns.int.>(2).get.toSeq.sorted === List()
      Ns.int.>(0).get.toSeq.sorted === List(1, 2)
      Ns.int.>(-2).get.toSeq.sorted === List(-1, 0, 1, 2)
      Ns.int.>(int1).get.toSeq.sorted === List(2)

      Ns.int.<=(-2).get.toSeq.sorted === List(-2)
      Ns.int.<=(0).get.toSeq.sorted === List(-2, -1, 0)
      Ns.int.<=(2).get.toSeq.sorted === List(-2, -1, 0, 1, 2)
      Ns.int.<=(int1).get.toSeq.sorted === List(-2, -1, 0, 1)

      Ns.int.>=(2).get.toSeq.sorted === List(2)
      Ns.int.>=(0).get.toSeq.sorted === List(0, 1, 2)
      Ns.int.>=(-2).get.toSeq.sorted === List(-2, -1, 0, 1, 2)
      Ns.int.>=(int1).get.toSeq.sorted === List(1, 2)


      Ns.long.<(-2L).get.toSeq.sorted === List()
      Ns.long.<(0L).get.toSeq.sorted === List(-2L, -1L)
      Ns.long.<(2L).get.toSeq.sorted === List(-2L, -1L, 0L, 1L)
      Ns.long.<(long1).get.toSeq.sorted === List(-2L, -1L, 0L)

      Ns.long.>(2L).get.toSeq.sorted === List()
      Ns.long.>(0L).get.toSeq.sorted === List(1L, 2L)
      Ns.long.>(-2L).get.toSeq.sorted === List(-1L, 0L, 1L, 2L)
      Ns.long.>(long1).get.toSeq.sorted === List(2L)

      Ns.long.<=(-2L).get.toSeq.sorted === List(-2L)
      Ns.long.<=(0L).get.toSeq.sorted === List(-2L, -1L, 0L)
      Ns.long.<=(2L).get.toSeq.sorted === List(-2L, -1L, 0L, 1L, 2L)
      Ns.long.<=(long1).get.toSeq.sorted === List(-2L, -1L, 0L, 1L)

      Ns.long.>=(2L).get.toSeq.sorted === List(2L)
      Ns.long.>=(0L).get.toSeq.sorted === List(0L, 1L, 2L)
      Ns.long.>=(-2L).get.toSeq.sorted === List(-2L, -1L, 0L, 1L, 2L)
      Ns.long.>=(long1).get.toSeq.sorted === List(1L, 2L)


      Ns.float.<(-2f).get.toSeq.sorted === List()
      Ns.float.<(0f).get.toSeq.sorted === List(-2f, -1f)
      Ns.float.<(2f).get.toSeq.sorted === List(-2f, -1f, 0f, 1f)
      Ns.float.<(float1).get.toSeq.sorted === List(-2f, -1f, 0f)

      Ns.float.>(2f).get.toSeq.sorted === List()
      Ns.float.>(0f).get.toSeq.sorted === List(1f, 2f)
      Ns.float.>(-2f).get.toSeq.sorted === List(-1f, 0f, 1f, 2f)
      Ns.float.>(float1).get.toSeq.sorted === List(2f)

      Ns.float.<=(-2f).get.toSeq.sorted === List(-2f)
      Ns.float.<=(0f).get.toSeq.sorted === List(-2f, -1f, 0f)
      Ns.float.<=(2f).get.toSeq.sorted === List(-2f, -1f, 0f, 1f, 2f)
      Ns.float.<=(float1).get.toSeq.sorted === List(-2f, -1f, 0f, 1f)

      Ns.float.>=(2f).get.toSeq.sorted === List(2f)
      Ns.float.>=(0f).get.toSeq.sorted === List(0f, 1f, 2f)
      Ns.float.>=(float1).get.toSeq.sorted === List(1f, 2f)


      Ns.double.<(-2.0).get.toSeq.sorted === List()
      Ns.double.<(0.0).get.toSeq.sorted === List(-2.0, -1.0)
      Ns.double.<(2.0).get.toSeq.sorted === List(-2.0, -1.0, 0.0, 1.0)
      Ns.double.<(double1).get.toSeq.sorted === List(-2.0, -1.0, 0.0)

      Ns.double.>(2.0).get.toSeq.sorted === List()
      Ns.double.>(0.0).get.toSeq.sorted === List(1.0, 2.0)
      Ns.double.>(-2.0).get.toSeq.sorted === List(-1.0, 0.0, 1.0, 2.0)
      Ns.double.>(double1).get.toSeq.sorted === List(2.0)

      Ns.double.<=(-2.0).get.toSeq.sorted === List(-2.0)
      Ns.double.<=(0.0).get.toSeq.sorted === List(-2.0, -1.0, 0.0)
      Ns.double.<=(2.0).get.toSeq.sorted === List(-2.0, -1.0, 0.0, 1.0, 2.0)
      Ns.double.<=(double1).get.toSeq.sorted === List(-2.0, -1.0, 0.0, 1.0)

      Ns.double.>=(2.0).get.toSeq.sorted === List(2.0)
      Ns.double.>=(0.0).get.toSeq.sorted === List(0.0, 1.0, 2.0)
      Ns.double.>=(-2.0).get.toSeq.sorted === List(-2.0, -1.0, 0.0, 1.0, 2.0)
      Ns.double.>=(double1).get.toSeq.sorted === List(1.0, 2.0)


      Ns.bool.<(true).get.toSeq.sorted === List(false)
      Ns.bool.<(false).get.toSeq.sorted === List()
      Ns.bool.<(bool0).get.toSeq.sorted === List()

      Ns.bool.>(true).get.toSeq.sorted === List()
      Ns.bool.>(false).get.toSeq.sorted === List(true)
      Ns.bool.>(bool0).get.toSeq.sorted === List(true)

      Ns.bool.<=(true).get.toSeq.sorted === List(false, true)
      Ns.bool.<=(false).get.toSeq.sorted === List(false)
      Ns.bool.<=(bool0).get.toSeq.sorted === List(false)

      Ns.bool.>=(true).get.toSeq.sorted === List(true)
      Ns.bool.>=(false).get.toSeq.sorted === List(false, true)
      Ns.bool.>=(bool0).get.toSeq.sorted === List(false, true)


      Ns.date.<(date1).get.toSeq.sorted === List(date0)
      Ns.date.<(date2).get.toSeq.sorted === List(date0, date1)

      Ns.date.>(date1).get.toSeq.sorted === List(date2)
      Ns.date.>(date0).get.toSeq.sorted === List(date1, date2)

      Ns.date.<=(date1).get.toSeq.sorted === List(date0, date1)
      Ns.date.<=(date2).get.toSeq.sorted === List(date0, date1, date2)

      Ns.date.>=(date1).get.toSeq.sorted === List(date1, date2)
      Ns.date.>=(date0).get.toSeq.sorted === List(date0, date1, date2)


      // Comparison of random UUIDs omitted...


      // todo when we get a string representation #uri
      //    Ns.uri.<(uri1).get.toSeq.sorted === List(uri0)
      //    Ns.uri.>(uri1).get.toSeq.sorted === List(uri2)
      //    Ns.uri.<=(uri1).get.toSeq.sorted === List(uri0, uri1)
      //    Ns.uri.>=(uri1).get.toSeq.sorted === List(uri1, uri2)


      Ns.enum.<("enum1").get.toSeq.sorted === List("enum0")
      Ns.enum.>("enum1").get.toSeq.sorted === List("enum2")
      Ns.enum.<=("enum1").get.toSeq.sorted === List("enum0", "enum1")
      Ns.enum.>=("enum1").get.toSeq.sorted === List("enum1", "enum2")

      Ns.enum.<(enum1).get.toSeq.sorted === List("enum0")
      Ns.enum.>(enum1).get.toSeq.sorted === List("enum2")
      Ns.enum.<=(enum1).get.toSeq.sorted === List("enum0", "enum1")
      Ns.enum.>=(enum1).get.toSeq.sorted === List("enum1", "enum2")
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
    Ns.str.strs_.>("b").get.toSeq.sorted === List("str2", "str3")
    Ns.str.strs_.>=("b").get.toSeq.sorted === List("str1", "str2", "str3")
    Ns.str.strs_.<=("b").get.toSeq.sorted === List("str1", "str2")
    Ns.str.strs_.<("b").get.toSeq.sorted === List("str1")


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
    Ns.int.ints_.>(2).get.toSeq.sorted === List(2, 3)
    Ns.int.ints_.>=(2).get.toSeq.sorted === List(1, 2, 3)
    Ns.int.ints_.<=(2).get.toSeq.sorted === List(1, 2, 3)
    Ns.int.ints_.<(2).get.toSeq.sorted === List(1)


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
    Ns.float.floats_.>(2f).get.toSeq.sorted === List(2f, 3f)
    Ns.float.floats_.>=(2f).get.toSeq.sorted === List(1f, 2f, 3f)
    Ns.float.floats_.<=(2f).get.toSeq.sorted === List(1f, 2f)
    Ns.float.floats_.<(2f).get.toSeq.sorted === List(1f)


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
    Ns.date.dates_.>(date2).get.toSeq.sorted === List(date2, date3)
    Ns.date.dates_.>=(date2).get.toSeq.sorted === List(date1, date2, date3)
    Ns.date.dates_.<=(date2).get.toSeq.sorted === List(date1, date2, date3)
    Ns.date.dates_.<(date2).get.toSeq.sorted === List(date1)


    // UUID (comparisons not of much relevance though...)

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
    Ns.uuid.uuids_.>(uuid2).get.toSeq.sorted === List(uuid2, uuid3)
    Ns.uuid.uuids_.>=(uuid2).get.toSeq.sorted === List(uuid1, uuid2, uuid3)
    Ns.uuid.uuids_.<=(uuid2).get.toSeq.sorted === List(uuid1, uuid2, uuid3)
    Ns.uuid.uuids_.<(uuid2).get.toSeq.sorted === List(uuid1)
  }
}