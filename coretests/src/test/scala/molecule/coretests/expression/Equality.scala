package molecule.coretests.expression

import java.util.Date

import molecule._
import molecule.coretests.util.CoreSetup
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class Equality extends Base {

  "Card one" in new OneSetup {

    Ns.str("").get === List("") // same as `Ns.str.apply("").get`
    Ns.str(" ").get === List(" ")
    Ns.str(",").get === List(",")
    Ns.str(".").get === List(".")
    Ns.str("?").get === List("?")
    Ns.str("A").get === List("A")
    Ns.str("B").get === List("B")
    Ns.str("a").get === List("a")
    Ns.str("b").get === List("b")

    // Apply value assigned to a variable
    Ns.str(str1).get === List("a")

    Ns.int(1).get === List(1)
    Ns.int(0).get === List(0)
    Ns.int(-1).get === List(-1)
    Ns.int(int1).get === List(1)


    Ns.long(1L).get === List(1L)
    Ns.long(0L).get === List(0L)
    Ns.long(-1L).get === List(-1L)
    Ns.long(long1).get === List(1L)

    // We can also apply Int values to a Long Attribute (using Scala's implicit conversions?)
    Ns.long(1).get === List(1)


    Ns.float(1f).get === List(1f)
    Ns.float(0f).get === List(0f)
    Ns.float(-1f).get === List(-1f)
    Ns.float(float1).get === List(1f)


    Ns.double(1.0).get === List(1.0)
    Ns.double(0.0).get === List(0.0)
    Ns.double(-1.0).get === List(-1.0)
    Ns.double(double1).get === List(1.0)

    Ns.bool(true).get === List(true)
    Ns.bool(bool1).get === List(true)
    Ns.bool(false).get === List(false)

    val now = new Date()
    Ns.date(now).get === List()
    Ns.date(date1).get === List(date1)
    Ns.date(date2).get === List(date2)

    // Todo Allow runtime constructs
    //    Ns.date(new Date()).get === List()


    Ns.uuid(uuid1).get === List(uuid1)
    Ns.uuid(uuid2).get === List(uuid2)


    Ns.uri(uri1).get === List(uri1)
    Ns.uri(uri2).get === List(uri2)


    Ns.enum("enum1").get === List("enum1")
    Ns.enum("enum2").get === List("enum2")
    Ns.enum(enum2).get === List("enum2")

    // Applying a non-existing enum value ("zzz") won't compile!
    expectCompileError(
      """m(Ns.enum("zzz"))""",
      """
        |[Dsl2Model:validateStaticEnums] 'zzz' is not among available enum values of attribute :ns/enum:
        |  enum0
        |  enum1
        |  enum2
        |  enum3
        |  enum4
        |  enum5
        |  enum6
        |  enum7
        |  enum8
        |  enum9
      """)
  }


  "Card many" in new ManySetup {

    // Coalesce all sets having a value into one set (no order guaranteed)
    Ns.strs("a").get === List(Set("b", "a"))

    // "b" is in all 3 entities, so all values are coalesced to one set
    Ns.strs("b").get === List(Set("a", "b", "c"))
    Ns.strs("c").get === List(Set("c", "b"))
    Ns.strs("d").get === List(Set("d", "ba"))

    // Using value assigned to variable
    Ns.strs(d).get === List(Set("d", "ba"))


    Ns.ints(1).get === List(Set(1, 2))
    Ns.ints(2).get === List(Set(1, 4, 3, 2))
    Ns.ints(3).get === List(Set(3, 2))
    Ns.ints(4).get === List(Set(4, 2))
    Ns.ints(int1).get === List(Set(1, 2))


    Ns.longs(1).get === List(Set(1L, 2L))
    Ns.longs(2).get === List(Set(1L, 4L, 3L, 2L))
    Ns.longs(3).get === List(Set(3L, 2L))
    Ns.longs(4).get === List(Set(4L, 2L))
    Ns.longs(long1).get === List(Set(1L, 2L))


    Ns.floats(1).get === List(Set(1.0f, 2.0f))
    Ns.floats(2).get === List(Set(2.0f, 1.0f, 3.0f))
    Ns.floats(3).get === List(Set(3.0f, 2.0f))
    Ns.floats(4).get === List(Set(4.0f, 2.5f))
    Ns.floats(float1).get === List(Set(1.0f, 2.0f))


    Ns.doubles(1).get === List(Set(1.0, 2.0))
    Ns.doubles(2).get === List(Set(2.0, 1.0, 3.0))
    Ns.doubles(3).get === List(Set(3.0, 2.0))
    Ns.doubles(4).get === List(Set(4.0, 2.5))
    Ns.doubles(double1).get === List(Set(1.0, 2.0))


    // Cardinality-many attribute for boolean values are maybe not so useful
    Ns.bools(false).get === List(Set(true, false))
    Ns.bools(true).get === List(Set(true, false))


    Ns.dates(date1).get === List(Set(date1, date2))
    Ns.dates(date2).get === List(Set(date1, date4, date3, date2))
    Ns.dates(date3).get === List(Set(date3, date2))
    Ns.dates(date4).get === List(Set(date4, date2))


    Ns.uuids(uuid1).get === List(Set(uuid1, uuid2))
    Ns.uuids(uuid2).get === List(Set(uuid1, uuid4, uuid3, uuid2))
    Ns.uuids(uuid3).get === List(Set(uuid3, uuid2))
    Ns.uuids(uuid4).get === List(Set(uuid4, uuid2))


    // Todo: card-many URI
    //    Ns.uris(uri1).get === List(Set(uri1, uri2))
    //    Ns.uris(uri2).get === List(Set(uri1, uri4, uri3, uri2))
    //    Ns.uris(uri3).get === List(Set(uri3, uri2))
    //    Ns.uris(uri4).get === List(Set(uri4, uri2))


    Ns.enums("enum1").get === List(Set("enum1", "enum2"))
    Ns.enums("enum2").get === List(Set("enum1", "enum4", "enum3", "enum2"))
    Ns.enums(enum3).get === List(Set(enum3, enum2))
    Ns.enums(enum4).get === List(Set(enum4, enum2))
  }


  "Card many - group by other attribute" in new ManySetup {

    // Card-many values can group by card-one attributes
    Ns.str.strs("a").get === List(("str1", Set("a", "b")))
    Ns.str.strs("b").get === List(("str1", Set("a", "b")), ("str2", Set("b", "c")))
    Ns.str.strs("c").get === List(("str2", Set("b", "c")))
    Ns.str.strs("d").get === List(("str3", Set("d", "ba")))
    Ns.str.strs(d).get === List(("str3", Set("d", "ba")))


    Ns.int.ints(1).get === List((1, Set(1, 2)))
    Ns.int.ints(2).get === List((1, Set(1, 2)), (2, Set(3, 2)), (3, Set(4, 2)))
    Ns.int.ints(3).get === List((2, Set(3, 2)))
    Ns.int.ints(4).get === List((3, Set(4, 2)))
    Ns.int.ints(int3).get === List((2, Set(3, 2)))


    Ns.long.longs(1L).get === List((1L, Set(1L, 2L)))
    Ns.long.longs(2L).get === List((1L, Set(1L, 2L)), (2L, Set(3L, 2L)), (3L, Set(4L, 2L)))
    Ns.long.longs(3L).get === List((2L, Set(3L, 2L)))
    Ns.long.longs(4L).get === List((3L, Set(4L, 2L)))
    Ns.long.longs(long3).get === List((2L, Set(3L, 2L)))


    Ns.float.floats(1.0f).get === List((1.0f, Set(1.0f, 2.0f)))
    Ns.float.floats(2.0f).get === List((1.0f, Set(1.0f, 2.0f)), (2.0f, Set(3.0f, 2.0f)))
    Ns.float.floats(3.0f).get === List((2.0f, Set(3.0f, 2.0f)))
    Ns.float.floats(4.0f).get === List((3.0f, Set(4.0f, 2.5f)))
    Ns.float.floats(float3).get === List((2.0f, Set(3.0f, 2.0f)))


    Ns.double.doubles(1.0).get === List((1.0, Set(1.0, 2.0)))
    Ns.double.doubles(2.0).get === List((1.0, Set(1.0, 2.0)), (2.0, Set(3.0, 2.0)))
    Ns.double.doubles(3.0).get === List((2.0, Set(3.0, 2.0)))
    Ns.double.doubles(4.0).get === List((3.0, Set(4.0, 2.5)))
    Ns.double.doubles(double3).get === List((2.0, Set(3.0, 2.0)))


    Ns.bool.bools(false).get === List((false, Set(false)), (true, Set(true, false)))
    Ns.bool.bools(true).get === List((true, Set(false, true)))


    Ns.date.dates(date1).get === List((date1, Set(date1, date2)))
    Ns.date.dates(date2).get === List((date1, Set(date1, date2)), (date2, Set(date3, date2)), (date3, Set(date4, date2)))
    Ns.date.dates(date3).get === List((date2, Set(date3, date2)))
    Ns.date.dates(date4).get === List((date3, Set(date4, date2)))


    Ns.uuid.uuids(uuid1).get.toSeq.sortBy(_.toString) === List((uuid1, Set(uuid1, uuid2)))
    Ns.uuid.uuids(uuid2).get.toSeq.sortBy(_.toString) === List((uuid1, Set(uuid2, uuid1)), (uuid2, Set(uuid3, uuid2)), (uuid3, Set(uuid4, uuid2)))
    Ns.uuid.uuids(uuid3).get.toSeq.sortBy(_.toString) === List((uuid2, Set(uuid2, uuid3)))
    Ns.uuid.uuids(uuid4).get.toSeq.sortBy(_.toString) === List((uuid3, Set(uuid2, uuid4)))


    // Todo: card-many URI
    //    Ns.uri.uris(uri1).getD
    //    Ns.uri.uris(uri1).get === List((uri1, Set(uri1, uri2)))
    //    Ns.uri.uris(uri2).get === List((uri1, Set(uri1, uri2)), (uri2, Set(uri3, uri2)), (uri3, Set(uri4, uri2)))
    //    Ns.uri.uris(uri3).get === List((uri2, Set(uri3, uri2)))
    //    Ns.uri.uris(uri4).get === List((uri3, Set(uri4, uri2)))


    Ns.enum.enums("enum1").get === List(("enum1", Set("enum1", "enum2")))
    Ns.enum.enums("enum2").get === List(("enum1", Set("enum1", "enum2")), ("enum2", Set("enum3", "enum2")), ("enum3", Set("enum4", "enum2")))
    Ns.enum.enums(enum3).get === List((enum2, Set(enum3, enum2)))
    Ns.enum.enums(enum4).get === List((enum3, Set(enum4, enum2)))
  }


  "Card many - group by other attribute value" in new ManySetup {

    // We could also match the card-one value to get the corresponding card-many set of values
    Ns.str("str1").strs.get === List(("str1", Set("a", "b")))
    Ns.str("str2").strs.get === List(("str2", Set("b", "c")))
    Ns.str("str3").strs.get === List(("str3", Set("ba", "d")))
    // etc...
  }


  "Entity id" in new CoreSetup {

    val List(e1, e2, e3, e4) = Ns.int insert List(1, 2, 3, 4) eids
    val seq                  = Set(e1, e2)
    val set                  = Seq(e3, e4)
    val iterable             = Iterable(e3, e4)

    Ns.int.get === List(1, 2, 3, 4)

    // Single eid
    Ns(e1).int.get === List(1)

    // Vararg
    Ns(e1, e2).int.get === List(1, 2)

    // Seq
    Ns(Seq(e1, e2)).int.get === List(1, 2)
    Ns(seq).int.get === List(1, 2)

    // Set
    Ns(Set(e3, e4)).int.get === List(3, 4)
    Ns(set).int.get === List(3, 4)

    // Iterable
    Ns(Iterable(e3, e4)).int.get === List(3, 4)
    Ns(iterable).int.get === List(3, 4)
  }
}