package molecule.coretests.expression

import molecule.api._
import java.util.Date
import java.util.UUID._
import java.net.URI
import datomic.Peer
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class Negation extends Base {


  "Exclude 1 or more card one values" in new OneSetup {

    Ns.str.not("").get.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.not(" ").get.sorted === List("", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.not(",").get.sorted === List("", " ", ".", "?", "A", "B", "a", "b")
    Ns.str.not(".").get.sorted === List("", " ", ",", "?", "A", "B", "a", "b")
    Ns.str.not("?").get.sorted === List("", " ", ",", ".", "A", "B", "a", "b")
    Ns.str.not("A").get.sorted === List("", " ", ",", ".", "?", "B", "a", "b")
    Ns.str.not("B").get.sorted === List("", " ", ",", ".", "?", "A", "a", "b")
    Ns.str.not("a").get.sorted === List("", " ", ",", ".", "?", "A", "B", "b")
    Ns.str.not("b").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
    Ns.str.not("C").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.not("c").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")

    // Same as
    Ns.str.!=("").get.sorted === List(" ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.!=(" ").get.sorted === List("", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.!=(",").get.sorted === List("", " ", ".", "?", "A", "B", "a", "b")
    Ns.str.!=(".").get.sorted === List("", " ", ",", "?", "A", "B", "a", "b")
    Ns.str.!=("?").get.sorted === List("", " ", ",", ".", "A", "B", "a", "b")
    Ns.str.!=("A").get.sorted === List("", " ", ",", ".", "?", "B", "a", "b")
    Ns.str.!=("B").get.sorted === List("", " ", ",", ".", "?", "A", "a", "b")
    Ns.str.!=("a").get.sorted === List("", " ", ",", ".", "?", "A", "B", "b")
    Ns.str.!=("b").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a")
    Ns.str.!=("C").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")
    Ns.str.!=("c").get.sorted === List("", " ", ",", ".", "?", "A", "B", "a", "b")

    Ns.str.not(str1).get.sorted === List("", " ", ",", ".", "?", "A", "B", "b")
    Ns.str.!=(str1).get.sorted === List("", " ", ",", ".", "?", "A", "B", "b")

    // Negate multiple values ("NOR"-logic: not 'a NOR 'b NOR 'c...)
    Ns.str.not("", " ").get.sorted === List(",", ".", "?", "A", "B", "a", "b")
    Ns.str.not("", " ", ",", ".", "?").get.sorted === List("A", "B", "a", "b")
    Ns.str.not("", " ", ",", ".", "?", "A", "B").get.sorted === List("a", "b")
    Ns.str.not("", " ", ",", ".", "?", "A", "B", "a", "b").get.sorted === List()


    Ns.int.not(7).get.sorted === List(-2, -1, 0, 1, 2)
    Ns.int.not(1).get.sorted === List(-2, -1, 0, 2)
    Ns.int.not(-1, 0, 1).get.sorted === List(-2, 2)
    Ns.int.not(Seq(-1, 0, 1)).get.sorted === List(-2, 2)
    Ns.int.not(int1).get.sorted === List(-2, -1, 0, 2)
    Ns.int.not(int1, int2).get.sorted === List(-2, -1, 0)
    Ns.int.not(Seq(int1, int2)).get.sorted === List(-2, -1, 0)
    val ints = Seq(int1, int2)
    Ns.int.not(ints).get.sorted === List(-2, -1, 0)

    // Same as
    Ns.int.!=(7).get.sorted === List(-2, -1, 0, 1, 2)
    Ns.int.!=(1).get.sorted === List(-2, -1, 0, 2)
    Ns.int.!=(-1, 0, 1).get.sorted === List(-2, 2)
    Ns.int.!=(Seq(-1, 0, 1)).get.sorted === List(-2, 2)
    Ns.int.!=(int1).get.sorted === List(-2, -1, 0, 2)
    Ns.int.!=(int1, int2).get.sorted === List(-2, -1, 0)
    Ns.int.!=(Seq(int1, int2)).get.sorted === List(-2, -1, 0)
    Ns.int.!=(ints).get.sorted === List(-2, -1, 0)


    Ns.long.not(7L).get.sorted === List(-2L, -1L, 0L, 1L, 2L)
    Ns.long.not(1L).get.sorted === List(-2L, -1L, 0L, 2L)
    Ns.long.not(-1L, 0L, 1L).get.sorted === List(-2L, 2L)
    Ns.long.not(Seq(-1L, 0L, 1L)).get.sorted === List(-2L, 2L)
    Ns.long.not(long1).get.sorted === List(-2L, -1L, 0L, 2L)
    Ns.long.not(long1, long1).get.sorted === List(-2L, -1L, 0L, 2L)
    Ns.long.not(Seq(long1, long2)).get.sorted === List(-2L, -1L, 0L)
    val longs = Seq(long1, long2)
    Ns.long.not(longs).get.sorted === List(-2L, -1L, 0L)


    Ns.float.not(7f).get.sorted === List(-2f, -1f, 0f, 1f, 2f)
    Ns.float.not(1f).get.sorted === List(-2f, -1f, 0f, 2f)
    Ns.float.not(-1f, 0f, 1f).get.sorted === List(-2f, 2f)
    Ns.float.not(Seq(-1f, 0f, 1f)).get.sorted === List(-2f, 2f)
    Ns.float.not(float1).get.sorted === List(-2f, -1f, 0f, 2f)
    Ns.float.not(float1, float2).get.sorted === List(-2f, -1f, 0f)
    Ns.float.not(Seq(float1, float2)).get.sorted === List(-2f, -1f, 0f)
    val floats = Seq(float1, float2)
    Ns.float.not(floats).get.sorted === List(-2f, -1f, 0f)


    Ns.double.not(7.0).get.sorted === List(-2.0, -1.0, 0.0, 1.0, 2.0)
    Ns.double.not(1.0).get.sorted === List(-2.0, -1.0, 0.0, 2.0)
    Ns.double.not(-1.0, 0.0, 1.0).get.sorted === List(-2.0, 2.0)
    Ns.double.not(Seq(-1.0, 0.0, 1.0)).get.sorted === List(-2.0, 2.0)
    Ns.double.not(double1).get.sorted === List(-2.0, -1.0, 0.0, 2.0)
    Ns.double.not(double1, double2).get.sorted === List(-2.0, -1.0, 0.0)
    Ns.double.not(Seq(double1, double2)).get.sorted === List(-2.0, -1.0, 0.0)
    val doubles = Seq(double1, double2)
    Ns.double.not(doubles).get.sorted === List(-2.0, -1.0, 0.0)


    Ns.bool.not(true).get === List(false)
    Ns.bool.not(false).get === List(true)
    // Multiple boolean values not so useful...
    Ns.bool.not(false, true).get === List()


    Ns.date.not(date3).get.sorted === List(date0, date1, date2)
    Ns.date.not(date0).get.sorted === List(date1, date2)
    Ns.date.not(date0, date1).get.sorted === List(date2)
    Ns.date.not(Seq(date0, date1)).get.sorted === List(date2)
    val dates = Seq(date0, date1)
    Ns.date.not(dates).get.sorted === List(date2)


    Ns.uuid.not(uuid3).get.sortBy(_.toString) === List(uuid0, uuid1, uuid2)
    Ns.uuid.not(uuid0).get.sortBy(_.toString) === List(uuid1, uuid2)
    Ns.uuid.not(uuid0, uuid1).get.sortBy(_.toString) === List(uuid2)
    Ns.uuid.not(Seq(uuid0, uuid1)).get.sortBy(_.toString) === List(uuid2)
    val uuids = Seq(uuid0, uuid1)
    Ns.uuid.not(uuids).get.sortBy(_.toString) === List(uuid2)


    Ns.uri.not(uri3).get.sortBy(_.toString) === List(uri0, uri1, uri2)
    Ns.uri.not(uri0).get.sortBy(_.toString) === List(uri1, uri2)
    Ns.uri.not(uri0, uri1).get.sortBy(_.toString) === List(uri2)
    Ns.uri.not(Seq(uri0, uri1)).get.sortBy(_.toString) === List(uri2)
    val uris = Seq(uri0, uri1)
    Ns.uri.not(uris).get.sortBy(_.toString) === List(uri2)


    Ns.enum.not("enum0").get.sorted === List(enum1, enum2)
    Ns.enum.not("enum0", "enum1").get.sorted === List(enum2)
    Ns.enum.not(Seq("enum0", "enum1")).get.sorted === List(enum2)
    Ns.enum.not(enum0).get.sorted === List(enum1, enum2)
    Ns.enum.not(enum0, enum1).get.sorted === List(enum2)
    Ns.enum.not(Seq(enum0, enum1)).get.sorted === List(enum2)
    val enums = Seq(enum0, enum1)
    Ns.enum.not(enums).get.sorted === List(enum2)


    Ns.bigInt.not(bigInt3).get.sortBy(_.toString) === List(bigInt0, bigInt1, bigInt2)
    Ns.bigInt.not(bigInt0).get.sortBy(_.toString) === List(bigInt1, bigInt2)
    Ns.bigInt.not(bigInt0, bigInt1).get.sortBy(_.toString) === List(bigInt2)
    Ns.bigInt.not(Seq(bigInt0, bigInt1)).get.sortBy(_.toString) === List(bigInt2)
    val bigInts = Seq(bigInt0, bigInt1)
    Ns.bigInt.not(bigInts).get.sortBy(_.toString) === List(bigInt2)


    Ns.bigDec.not(bigDec3).get.sortBy(_.toString) === List(bigDec0, bigDec1, bigDec2)
    Ns.bigDec.not(bigDec0).get.sortBy(_.toString) === List(bigDec1, bigDec2)
    Ns.bigDec.not(bigDec0, bigDec1).get.sortBy(_.toString) === List(bigDec2)
    Ns.bigDec.not(Seq(bigDec0, bigDec1)).get.sortBy(_.toString) === List(bigDec2)
    val bigDecs = Seq(bigDec0, bigDec1)
    Ns.bigDec.not(bigDecs).get.sortBy(_.toString) === List(bigDec2)
  }


  "Exclude 1 or more card many values" in new ManySetup {

    // Negation of a cardinality-many attribute value is rather useless since it
    // will just return the coalesced set minus the excluded value
    Ns.strs.not("b").get === List(Set("d", "a", "ba", "c"))

    // We could group by another attribute but that still leave us with filtered sets
    Ns.str.strs.not("a").get === List(("str1", Set("b")), ("str2", Set("b", "c")), ("str3", Set("d", "ba")))
    Ns.str.strs.not("b").get === List(("str1", Set("a")), ("str2", Set("c")), ("str3", Set("d", "ba")))
    Ns.str.strs.not("c").get === List(("str1", Set("a", "b")), ("str2", Set("b")), ("str3", Set("d", "ba")))
    Ns.str.strs.not("d").get === List(("str1", Set("a", "b")), ("str2", Set("b", "c")), ("str3", Set("ba")))

    // What we probably want to do is to filter out full sets having the negation value:
    Ns.str.strs.get.filterNot(_._2.contains("a")) === List(("str2", Set("b", "c")), ("str3", Set("d", "ba")))
    Ns.str.strs.get.filterNot(_._2.contains("b")) === List(("str3", Set("d", "ba")))
    Ns.str.strs.get.filterNot(_._2.contains("c")) === List(("str1", Set("a", "b")), ("str3", Set("d", "ba")))
    Ns.str.strs.get.filterNot(_._2.contains("d")) === List(("str1", Set("a", "b")), ("str2", Set("b", "c")))
  }
}