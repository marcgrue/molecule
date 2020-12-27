package molecule.tests.core.expression

import java.net.URI
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out2._

class Negation extends Base {

  "Card one" in new OneSetup {

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
    // Intellij shows error although it is fine
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
    // Intellij shows error although it is fine
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


  "Quoting" in new CoreSetup {

    Ns.int(1).str("""Hi "Ann"""").save

    Ns.str.not("""Hi "Ben"""").get === List("""Hi "Ann"""")

    val str2: String = """Hi "Ben""""
    Ns.str.not(str2).get === List("""Hi "Ann"""")

    Ns.int.str_.not("""Hi "Ben"""").get === List(1)
    Ns.int.str_.not(str2).get === List(1)
  }


  "Card many - coalesce 1 attr" in new ManySetup {

    // Negation of a single cardinality-many attribute value is rather useless since it
    // will just return the coalesced set of values minus the excluded value
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


  "Card many" in new CoreSetup {

    val all = List(
      (1, Set(1, 2, 3)),
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )

    Ns.int.ints insert all

    Ns.int.ints.not(Nil).get === all
    Ns.int.ints.not(Set[Int]()).get === all

    Ns.int.ints.not(1).get === List(
      (1, Set(2, 3)),
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )
    // Same as
    Ns.int.ints.not(List(1)).get === List(
      (1, Set(2, 3)),
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )

    // Set semantics omit the whole set with one or more matching values
    Ns.int.ints.not(Set(1)).get === List(
      // (1, Set(1, 2, 3)),  // 1 match
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )
    // Same as
    Ns.int.ints.not(List(Set(1))).get === List(
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )

    Ns.int.ints.not(2).get === List(
      (1, Set(1, 3)),
      (2, Set(3, 4)),
      (3, Set(3, 4, 5))
    )
    Ns.int.ints.not(Set(2)).get === List(
      // (1, Set(1, 2, 3)),  // 2 match
      // (2, Set(2, 3, 4)),  // 2 match
      (3, Set(3, 4, 5))
    )

    Ns.int.ints.not(3).get === List(
      (1, Set(1, 2)),
      (2, Set(4, 2)),
      (3, Set(4, 5))
    )
    Ns.int.ints.not(Set(3)).get === Nil // 3 match all


    Ns.int.ints.not(1, 2).get === List(
      (1, Set(3)),
      (2, Set(3, 4)),
      (3, Set(3, 4, 5))
    )
    // Same as
    Ns.int.ints.not(List(1, 2)).get === List(
      (1, Set(3)),
      (2, Set(3, 4)),
      (3, Set(3, 4, 5))
    )

    Ns.int.ints.not(Set(1), Set(2)).get === List(
      // (1, Set(1, 2, 3)),  // 1 match, 2 match
      // (2, Set(2, 3, 4)),  // 2 match
      (3, Set(3, 4, 5))
    )
    // Multiple values in a Set matches matches set-wise
    Ns.int.ints.not(Set(1, 2)).get === List(
      // (1, Set(1, 2, 3)),  // 1 AND 2 match
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )


    Ns.int.ints.not(1, 3).get === List(
      (1, Set(2)),
      (2, Set(2, 4)),
      (3, Set(4, 5))
    )
    Ns.int.ints.not(Set(1), Set(3)).get === Nil // 3 match all
    Ns.int.ints.not(Set(1, 3)).get === List(
      // (1, Set(1, 2, 3)),  // 1 AND 3 match
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )


    Ns.int.ints.not(1, 2, 3).get === List(
      // (1, Set(1, 2, 3)),  // 1 match, 2 match, 3 match
      (2, Set(4)),
      (3, Set(4, 5))
    )
    Ns.int.ints.not(Set(1), Set(2), Set(3)).get === Nil // 3 match all
    Ns.int.ints.not(Set(1, 2, 3)).get === List(
      // (1, Set(1, 2, 3)),  // 1 AND 2 AND 3 match
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )


    Ns.int.ints.not(Set(1, 2), Set(1)).get === List(
      (2, Set(2, 3, 4)),
      (3, Set(3, 4, 5))
    )
    Ns.int.ints.not(Set(1, 2), Set(2)).get === List(
      (3, Set(3, 4, 5))
    )
    Ns.int.ints.not(Set(1, 2), Set(3)).get === Nil
    Ns.int.ints.not(Set(1, 2), Set(4)).get === Nil
    Ns.int.ints.not(Set(1, 2), Set(5)).get === List(
      (2, Set(2, 3, 4)),
    )

    Ns.int.ints.not(Set(1, 2), Set(2, 3)).get === List(
      (3, Set(3, 4, 5))
    )
    Ns.int.ints.not(Set(1, 2), Set(4, 5)).get === List(
      (2, Set(2, 3, 4))
    )


    // Variables/values can be mixed

    val l2 = List((2, Set(2, 3, 4)))

    Ns.int.ints.not(Set(1, 2), Set(4, 5)).get === l2
    Ns.int.ints.not(Set(1, int2), Set(int4, 5)).get === l2

    val set45 = Set(4, 5)
    Ns.int.ints.not(Set(1, int2), set45).get === l2

    // Seq of Sets
    Ns.int.ints.not(List(Set(1, int2), set45)).get === l2

    val sets = List(Set(1, int2), set45)
    Ns.int.ints.not(sets).get === l2
  }


  "Card many, enum" in new CoreSetup {

    val all = List(
      (1, Set("enum1", "enum2", "enum3")),
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )

    Ns.int.enums insert all

    Ns.int.enums.not(Nil).get === all
    Ns.int.enums.not(Set[String]()).get === all

    Ns.int.enums.not("enum1").get === List(
      (1, Set("enum2", "enum3")),
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )
    // Same as
    Ns.int.enums.not(List("enum1")).get === List(
      (1, Set("enum2", "enum3")),
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )

    // Set semantics omit the whole set with one or more matching values
    Ns.int.enums.not(Set("enum1")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" match
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )
    // Same as
    Ns.int.enums.not(List(Set("enum1"))).get === List(
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )

    Ns.int.enums.not("enum2").get === List(
      (1, Set("enum1", "enum3")),
      (2, Set("enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum2")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum2" match
      // (2, Set("enum2", "enum3", "enum4")),  // "enum2" match
      (3, Set("enum3", "enum4", "enum5"))
    )

    Ns.int.enums.not("enum3").get === List(
      (1, Set("enum1", "enum2")),
      (2, Set("enum4", "enum2")),
      (3, Set("enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum3")).get === Nil // "enum3" match all


    Ns.int.enums.not("enum1", "enum2").get === List(
      (1, Set("enum3")),
      (2, Set("enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )
    // Same as
    Ns.int.enums.not(List("enum1", "enum2")).get === List(
      (1, Set("enum3")),
      (2, Set("enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )

    Ns.int.enums.not(Set("enum1"), Set("enum2")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" match, "enum2" match
      // (2, Set("enum2", "enum3", "enum4")),  // "enum2" match
      (3, Set("enum3", "enum4", "enum5"))
    )
    // Multiple values in a Set matches matches set-wise
    Ns.int.enums.not(Set("enum1", "enum2")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" AND "enum2" match
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )


    Ns.int.enums.not("enum1", "enum3").get === List(
      (1, Set("enum2")),
      (2, Set("enum2", "enum4")),
      (3, Set("enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum1"), Set("enum3")).get === Nil // "enum3" match all
    Ns.int.enums.not(Set("enum1", "enum3")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" AND "enum3" match
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )


    Ns.int.enums.not("enum1", "enum2", "enum3").get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" match, "enum2" match, "enum3" match
      (2, Set("enum4")),
      (3, Set("enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum1"), Set("enum2"), Set("enum3")).get === Nil // "enum3" match all
    Ns.int.enums.not(Set("enum1", "enum2", "enum3")).get === List(
      // (1, Set("enum1", "enum2", "enum3")),  // "enum1" AND "enum2" AND "enum3" match
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )


    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum1")).get === List(
      (2, Set("enum2", "enum3", "enum4")),
      (3, Set("enum3", "enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum2")).get === List(
      (3, Set("enum3", "enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum3")).get === Nil
    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum4")).get === Nil
    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum5")).get === List(
      (2, Set("enum2", "enum3", "enum4")),
    )

    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum2", "enum3")).get === List(
      (3, Set("enum3", "enum4", "enum5"))
    )
    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum4", "enum5")).get === List(
      (2, Set("enum2", "enum3", "enum4"))
    )


    // Variables/values can be mixed

    val l2 = List((2, Set("enum2", "enum3", "enum4")))

    Ns.int.enums.not(Set("enum1", "enum2"), Set("enum4", "enum5")).get === l2
    Ns.int.enums.not(Set("enum1", enum2), Set(enum4, "enum5")).get === l2

    val set45 = Set("enum4", "enum5")
    Ns.int.enums.not(Set("enum1", enum2), set45).get === l2

    // Seq of Sets
    Ns.int.enums.not(List(Set("enum1", enum2), set45)).get === l2

    val sets = List(Set("enum1", enum2), set45)
    Ns.int.enums.not(sets).get === l2
  }


  "Card many, uri" in new CoreSetup {

    val all = List(
      (1, Set(uri1, uri2, uri3)),
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )

    Ns.int.uris insert all

    Ns.int.uris.not(Nil).get === all
    Ns.int.uris.not(Set[URI]()).get === all

    Ns.int.uris.not(uri1).get === List(
      (1, Set(uri2, uri3)),
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )
    // Same as
    Ns.int.uris.not(List(uri1)).get === List(
      (1, Set(uri2, uri3)),
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )

    // Set semantics omit the whole set with one or more matching values
    Ns.int.uris.not(Set(uri1)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 match
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )
    // Same as
    Ns.int.uris.not(List(Set(uri1))).get === List(
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )

    Ns.int.uris.not(uri2).get === List(
      (1, Set(uri1, uri3)),
      (2, Set(uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )
    Ns.int.uris.not(Set(uri2)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri2 match
      // (2, Set(uri2, uri3, uri4)),  // uri2 match
      (3, Set(uri3, uri4, uri5))
    )

    Ns.int.uris.not(uri3).get === List(
      (1, Set(uri1, uri2)),
      (2, Set(uri4, uri2)),
      (3, Set(uri4, uri5))
    )
    Ns.int.uris.not(Set(uri3)).get === Nil // uri3 match all


    Ns.int.uris.not(uri1, uri2).get === List(
      (1, Set(uri3)),
      (2, Set(uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )
    // Same as
    Ns.int.uris.not(List(uri1, uri2)).get === List(
      (1, Set(uri3)),
      (2, Set(uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )

    Ns.int.uris.not(Set(uri1), Set(uri2)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 match, uri2 match
      // (2, Set(uri2, uri3, uri4)),  // uri2 match
      (3, Set(uri3, uri4, uri5))
    )
    // Multiple values in a Set matches matches set-wise
    Ns.int.uris.not(Set(uri1, uri2)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 AND uri2 match
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )


    Ns.int.uris.not(uri1, uri3).get === List(
      (1, Set(uri2)),
      (2, Set(uri2, uri4)),
      (3, Set(uri4, uri5))
    )
    Ns.int.uris.not(Set(uri1), Set(uri3)).get === Nil // uri3 match all
    Ns.int.uris.not(Set(uri1, uri3)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 AND uri3 match
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )


    Ns.int.uris.not(uri1, uri2, uri3).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 match, uri2 match, uri3 match
      (2, Set(uri4)),
      (3, Set(uri4, uri5))
    )
    Ns.int.uris.not(Set(uri1), Set(uri2), Set(uri3)).get === Nil // uri3 match all
    Ns.int.uris.not(Set(uri1, uri2, uri3)).get === List(
      // (1, Set(uri1, uri2, uri3)),  // uri1 AND uri2 AND uri3 match
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )


    Ns.int.uris.not(Set(uri1, uri2), Set(uri1)).get === List(
      (2, Set(uri2, uri3, uri4)),
      (3, Set(uri3, uri4, uri5))
    )
    Ns.int.uris.not(Set(uri1, uri2), Set(uri2)).get === List(
      (3, Set(uri3, uri4, uri5))
    )
    Ns.int.uris.not(Set(uri1, uri2), Set(uri3)).get === Nil
    Ns.int.uris.not(Set(uri1, uri2), Set(uri4)).get === Nil
    Ns.int.uris.not(Set(uri1, uri2), Set(uri5)).get === List(
      (2, Set(uri2, uri3, uri4)),
    )

    Ns.int.uris.not(Set(uri1, uri2), Set(uri2, uri3)).get === List(
      (3, Set(uri3, uri4, uri5))
    )
    Ns.int.uris.not(Set(uri1, uri2), Set(uri4, uri5)).get === List(
      (2, Set(uri2, uri3, uri4))
    )
  }
}