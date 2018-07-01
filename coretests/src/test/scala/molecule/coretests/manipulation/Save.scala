package molecule.coretests.manipulation

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class Save extends CoreSpec {

  "Card one attr" in new CoreSetup {

    // Construct a "Data-Molecule" with an attribute value and add it to the database

    Ns.str("a").save
    Ns.int(1).save
    Ns.long(1L).save
    Ns.float(1.0f).save
    Ns.double(1.0).save
    Ns.bool(true).save
    Ns.date(date1).save
    Ns.uuid(uuid1).save
    Ns.uri(uri1).save
    Ns.enum("enum1").save

    Ns.str.get.head === "a"
    Ns.int.get.head === 1
    Ns.long.get.head === 1L
    Ns.float.get.head === 1.0f
    Ns.double.get.head === 1.0
    Ns.bool.get.head === true
    Ns.date.get.head === date1
    Ns.uuid.get.head === uuid1
    Ns.uri.get.head === uri1
    Ns.enum.get.head === enum1

    // Applying multiple values to card-one attr not allowed when saving

    (Ns.str("a", "b").save must throwA[IllegalArgumentException])
      .message === "Got the exception java.lang.IllegalArgumentException: " +
      "[molecule.ops.VerifyModel.noConflictingCardOneValues]  Can't save multiple values for cardinality-one attribute:" +
      s"\n  Ns ... str(a, b)"
  }

  "Card many attr" in new CoreSetup {

    // Construct a "Data-Molecule" with multiple attributes populated with data and add it to the database

    Ns.strs("a", "b")
      .ints(1, 2)
      .longs(1L, 2L)
      .floats(1.0f, 2.0f)
      .doubles(1.0, 2.0)
      .dates(date1, date2)
      .uuids(uuid1, uuid2)
      .uris(uri1, uri2)
      .enums("enum1", "enum2").save

    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head === (
      Set("a", "b"),
      Set(1, 2),
      Set(1L, 2L),
      Set(1.0f, 2.0f),
      Set(1.0, 2.0),
      Set(date1, date2),
      Set(uuid1, uuid2),
      Set(uri1, uri2),
      Set("enum1", "enum2"))
  }


  "Card one attrs" in new CoreSetup {

    // Construct a "Data-Molecule" with multiple attributes populated with data and add it to the database

    Ns.str("a").int(1).long(1L).float(1.0f).double(1.0).bool(true).date(date1).uuid(uuid1).uri(uri1).enum("enum1").save

    Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.head === (
      "a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1")
  }


  "Card many attrs" >> {

    "Vararg, various types" in new CoreSetup {

      Ns.strs("a").save
      Ns.strs("a", "b", "c").save
      Ns.strs.get.head === Set("a", "b", "c")

      Ns.longs(1L).save
      Ns.longs(2L, 3L).save
      Ns.longs.get.head === Set(1L, 2L, 3L)

      Ns.floats(1.0f).save
      Ns.floats(2.0f, 3.0f).save
      Ns.floats.get.head === Set(1.0f, 2.0f, 3.0f)

      Ns.doubles(1.0).save
      Ns.doubles(2.0, 3.0).save
      Ns.doubles.get.head === Set(1.0, 2.0, 3.0)

      // Ns.bools not implemented...

      Ns.dates(date1).save
      Ns.dates(date2, date3).save
      Ns.dates.get.head === Set(date1, date2, date3)

      Ns.uuids(uuid1).save
      Ns.uuids(uuid2, uuid3).save
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3)

      Ns.uris(uri1).save
      Ns.uris(uri2, uri3).save
      Ns.uris.get.head === Set(uri1, uri2, uri3)

      Ns.enums("enum1").save
      Ns.enums("enum2", "enum3").save
      Ns.enums.get.head === Set("enum1", "enum2", "enum3")
    }

    "Vararg" in new CoreSetup {

      // Applying empty arg asserts nothing
      Ns.ints().save
      // Nothing asserted
      Ns.ints.get === List()

      // Entity 1
      Ns.ints(1).save

      // Saving an applied empty arg doesn't affect other entities
      Ns.ints().save
      Ns.ints.get === List(Set(1))

      // Entity 2
      Ns.ints(2, 3).save
      // Values of all entities are unified
      Ns.ints.get.head === Set(1, 2, 3)

      // Can't apply duplicate values
      expectCompileError(
        """Ns.int(1).ints(1, 2, 2).save""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n2")
    }


    "Vararg, as variables" in new CoreSetup {

      Ns.ints(int1).save
      Ns.ints(int2, int3).save
      Ns.ints.get.head === Set(int1, int2, int3)


      // Can't apply duplicate values

      expectCompileError(
        """Ns.ints(int1, int2, int2).save""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n__ident__int2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other2 = 2
      (Ns.ints(int1, int2, other2).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/ints`:" +
        "\n2"
    }


    "Iterables" in new CoreSetup {

      // Set
      Ns.int(1).ints(Set(1, 2)).save
      Ns.int_(1).ints.get.head === Set(1, 2)

      // Seq
      Ns.int(2).ints(Seq(1, 2)).save
      // Output is always a Set for card-many attribute values
      Ns.int_(2).ints.get.head === Set(1, 2)

      // List
      Ns.int(2).ints(List(1, 2)).save
      Ns.int_(2).ints.get.head === Set(1, 2)

      // Iterable
      Ns.int(4).ints(Iterable(1, 2)).save
      Ns.int_(4).ints.get.head === Set(1, 2)

      // Set with duplicate values not allowed
      expectCompileError(
        """Ns.int(5).ints(Set(1, 2, 2)).save""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n2")

      // Multiple Iterables
      Ns.int(6).ints(Set(1, 2), List(3, 4)).save
      Ns.int_(6).ints.get.head === Set(1, 2, 3, 4)

      // Sets with duplicate values not allowed
      expectCompileError(
        """Ns.int(7).ints(Set(1, 2), Set(2, 3)).save""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n2")
    }

    "Iterables as variable" in new CoreSetup {

      val set0   = Set.empty[Int]
      val set12  = Set(1, 2)
      val seq122 = Seq(1, 2, 2) // Seq with duplicate values
      val set23 = Set(2, 3)
      val set45 = Set(4, 5)

      // Empty Set - no facts asserted
      Ns.int(0).ints(set0).save
      // `int` was asserted!
      Ns.int(0).ints$.get === List((0, None))

      // 1 Set
      Ns.int(1).ints(set12).save
      Ns.int_(1).ints.get.head === Set(1, 2)

      // Multiple Iterables
      // - mixing static/variable data
      // - empty sets ignored
      Ns.int(2).ints(Seq(1, 2), set0, set45).save
      Ns.int_(2).ints.get.head === Set(1, 2, 4, 5)


      // Duplicate values in Seq rejected at runtime
      (Ns.ints(seq122).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/ints`:" +
        "\n2"

      // Can't apply duplicate arguments
      expectCompileError(
        """Ns.ints(set12, set12, set23).save""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n__ident__set12")

      // Duplicate values across multiple Seqs rejected at runtime
      (Ns.ints(set12, set23).save must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/ints`:" +
        "\n2"
    }
  }


  "Relationships" in new CoreSetup {

    val address = Ns.str("273 Broadway").Ref1.int1(10700).str1("New York").Ref2.str2("USA").save.eid
    address.touch === Map(
      ":db/id" -> 17592186045445L,
      ":ns/ref1" -> Map(
        ":db/id" -> 17592186045446L,
        ":ref1/int1" -> 10700,
        ":ref1/ref2" -> Map(":db/id" -> 17592186045447L, ":ref2/str2" -> "USA"),
        ":ref1/str1" -> "New York"),
      ":ns/str" -> "273 Broadway")

    Ns.str.Ref1.int1.str1.Ref2.str2.get.head === ("273 Broadway", 10700, "New York", "USA")
  }


  "Optional card-one" >> {

    "syntax overlaps" in new CoreSetup {

      // Equal empty results
      Ns.str("a").save
      Ns.str.int_(nil).get === List("a")
      Ns.str.int$(None).get.head === ("a", None)

      // Equal mandatory results
      Ns.str("b").int(2).save
      Ns.str.int(2).get.head === ("b", 2)
      Ns.str.int$(Some(2)).get.head === ("b", Some(2))
    }

    "data/variable" in new CoreSetup {

      Ns.str("a").int$(Some(1)).save
      Ns.str("a").int$.get.head === ("a", Some(1))

      val some2 = Some(2)
      Ns.str("b").int$(some2).save
      Ns.str("b").int$.get.head === ("b", Some(2))

      Ns.str("c").int$(None).save
      Ns.str("c").int$.get.head === ("c", None)

      val none = None
      Ns.str("d").int$(none).save
      Ns.str("d").int$.get.head === ("d", None)
    }


    "attributes" in new CoreSetup {
      Ns.ints(1).str$(Some("a")).save
      Ns.ints(2).int$(Some(1)).save
      Ns.ints(3).long$(Some(1L)).save
      Ns.ints(4).float$(Some(1f)).save
      Ns.ints(5).double$(Some(1.0)).save
      Ns.ints(6).bool$(Some(true)).save
      Ns.ints(7).bigInt$(Some(bigInt1)).save
      Ns.ints(8).bigDec$(Some(bigDec1)).save
      Ns.ints(9).date$(Some(date1)).save
      Ns.ints(10).uuid$(Some(uuid1)).save
      Ns.ints(11).uri$(Some(uri1)).save
      Ns.ints(12).enum$(Some(enum1)).save
      Ns.ints(13).ref1$(Some(1L)).save

      Ns.ints(1).str$(Some("a")).get.head === (Set(1), Some("a"))
      Ns.ints(2).int$(Some(1)).get.head === (Set(2), Some(1))
      Ns.ints(3).long$(Some(1L)).get.head === (Set(3), Some(1L))
      Ns.ints(4).float$(Some(1f)).get.head === (Set(4), Some(1f))
      Ns.ints(5).double$(Some(1.0)).get.head === (Set(5), Some(1.0))
      Ns.ints(6).bool$(Some(true)).get.head === (Set(6), Some(true))
      Ns.ints(7).bigInt$(Some(bigInt1)).get.head === (Set(7), Some(bigInt1))
      Ns.ints(8).bigDec$(Some(bigDec1)).get.head === (Set(8), Some(bigDec1))
      Ns.ints(9).date$(Some(date1)).get.head === (Set(9), Some(date1))
      Ns.ints(10).uuid$(Some(uuid1)).get.head === (Set(10), Some(uuid1))
      Ns.ints(11).uri$(Some(uri1)).get.head === (Set(11), Some(uri1))
      Ns.ints(12).enum$(Some(enum1)).get.head === (Set(12), Some(enum1))
      Ns.ints(13).ref1$(Some(1L)).get.head === (Set(13), Some(1L))
    }


    "attributes multiple" in new CoreSetup {
      Ns.ints(1)
        .str$(Some("a"))
        .int$(Some(1))
        .long$(Some(1L))
        .float$(Some(1f))
        .double$(Some(1.0))
        .bool$(Some(true))
        .bigInt$(Some(bigInt1))
        .bigDec$(Some(bigDec1))
        .date$(Some(date1))
        .uuid$(Some(uuid1))
        .uri$(Some(uri1))
        .enum$(Some(enum1))
        .ref1$(Some(1L))
        .save

      Ns.ints(1).str.int.long.float.double.bool.bigInt.bigDec.date.uuid.uri.enum.ref1.get.head === (
        Set(1), "a", 1, 1L, 1f, 1.0, true, bigInt1, bigDec1, date1, uuid1, uri1, enum1, 1L
      )

      Ns.ints(1)
        .str$(Some("a"))
        .int$(Some(1))
        .long$(Some(1L))
        .float$(Some(1f))
        .double$(Some(1.0))
        .bool$(Some(true))
        .bigInt$(Some(bigInt1))
        .bigDec$(Some(bigDec1))
        .date$(Some(date1))
        .uuid$(Some(uuid1))
        .uri$(Some(uri1))
        .enum$(Some(enum1))
        .ref1$(Some(1L))
        .get.head === (
        Set(1),
        Some("a"),
        Some(1),
        Some(1L),
        Some(1f),
        Some(1.0),
        Some(true),
        Some(bigInt1),
        Some(bigDec1),
        Some(date1),
        Some(uuid1),
        Some(uri1),
        Some(enum1),
        Some(1L)
      )

      Ns.ints(2)
        .str$(None)
        .int$(None)
        .long$(None)
        .float$(None)
        .double$(None)
        .bool$(None)
        .bigInt$(None)
        .bigDec$(None)
        .date$(None)
        .uuid$(None)
        .uri$(None)
        .enum$(None)
        .ref1$(None)
        .save

      Ns.ints(2).str$.int$.long$.float$.double$.bool$.bigInt$.bigDec$.date$.uuid$.uri$.enum$.ref1$.get.head === (
        Set(2), None, None, None, None, None, None, None, None, None, None, None, None, None
      )
    }

    "ref" in new CoreSetup {

      Ns.int(1).Ref1.str1("a").int1$.apply(Some(10)).save
      Ns.int(1).Ref1.str1.int1.get.head === (1, "a", 10)

      Ns.int(2).Ref1.str1("b").int1$.apply(None).save
      Ns.int(2).Ref1.str1.int1$.get.head === (2, "b", None)
    }
  }

  "Optional card-many" >> {

    "syntax overlaps" in new CoreSetup {

      // Equal empty results
      Ns.str("a").save
      Ns.str.ints_(nil).get === List("a")
      Ns.str.ints$(None).get.head === ("a", None)

      // Equal mandatory results
      Ns.str("b").ints(2).save
      Ns.str.ints(2).get.head === ("b", Set(2))
      Ns.str.ints(Set(2)).get.head === ("b", Set(2))
      Ns.str.ints$(Some(Set(2))).get.head === ("b", Some(Set(2)))
    }


    "data/variable" in new CoreSetup {

      Ns.str("a").ints$.apply(Some(Set(1, 2))).save
      Ns.str("a").ints$.get.head === ("a", Some(Set(1, 2)))

      val some2 = Some(Set(3, 4))
      Ns.str("b").ints$(some2).save
      Ns.str("b").ints$.get.head === ("b", Some(Set(3, 4)))

      Ns.str("c").ints$(None).save
      Ns.str("c").ints$.get.head === ("c", None)

      val none = None
      Ns.str("d").ints$(none).save
      Ns.str("d").ints$.get.head === ("d", None)
    }


    "attributes" in new CoreSetup {
      Ns.int(11).strs$(Some(Set("a", "b"))).save
      Ns.int(12).ints$(Some(Set(1, 2))).save
      Ns.int(13).longs$(Some(Set(1L, 2L))).save
      Ns.int(14).floats$(Some(Set(1f, 2f))).save
      Ns.int(15).doubles$(Some(Set(1.0, 2.0))).save
      Ns.int(16).bools$(Some(Set(true, false))).save
      Ns.int(17).bigInts$(Some(Set(bigInt1, bigInt2))).save
      Ns.int(18).bigDecs$(Some(Set(bigDec1, bigDec2))).save
      Ns.int(19).dates$(Some(Set(date1, date2))).save
      Ns.int(20).uuids$(Some(Set(uuid1, uuid2))).save
      Ns.int(21).uris$(Some(Set(uri1, uri2))).save
      Ns.int(22).enums$(Some(Set(enum1, enum2))).save
      Ns.int(23).refs1$(Some(Set(1L, 2L))).save

      Ns.int(11).strs$(Some(Set("a", "b"))).get.head === (11, Some(Set("a", "b")))
      Ns.int(12).ints$(Some(Set(1, 2))).get.head === (12, Some(Set(1, 2)))
      Ns.int(13).longs$(Some(Set(1L, 2L))).get.head === (13, Some(Set(1L, 2L)))
      Ns.int(14).floats$(Some(Set(1f, 2f))).get.head === (14, Some(Set(1f, 2f)))
      Ns.int(15).doubles$(Some(Set(1.0, 2.0))).get.head === (15, Some(Set(1.0, 2.0)))
      Ns.int(16).bools$(Some(Set(true, false))).get.head === (16, Some(Set(true, false)))
      Ns.int(17).bigInts$(Some(Set(bigInt1, bigInt2))).get.head === (17, Some(Set(bigInt1, bigInt2)))
      Ns.int(18).bigDecs$(Some(Set(bigDec1, bigDec2))).get.head === (18, Some(Set(bigDec1, bigDec2)))
      Ns.int(19).dates$(Some(Set(date1, date2))).get.head === (19, Some(Set(date1, date2)))
      Ns.int(20).uuids$(Some(Set(uuid1, uuid2))).get.head === (20, Some(Set(uuid1, uuid2)))
      Ns.int(21).uris$(Some(Set(uri1, uri2))).get.head === (21, Some(Set(uri1, uri2)))
      Ns.int(22).enums$(Some(Set(enum1, enum2))).get.head === (22, Some(Set(enum1, enum2)))
      Ns.int(23).refs1$(Some(Set(1L, 2L))).get.head === (23, Some(Set(1L, 2L)))
    }


    "attributes, multiple" in new CoreSetup {

      Ns.int(1)
        .strs$(Some(Set("a", "b")))
        .ints$(Some(Set(1, 2)))
        .longs$(Some(Set(1L, 2L)))
        .floats$(Some(Set(1f, 2f)))
        .doubles$(Some(Set(1.0, 2.0)))
        .bools$(Some(Set(true, false)))
        .bigInts$(Some(Set(bigInt1, bigInt2)))
        .bigDecs$(Some(Set(bigDec1, bigDec2)))
        .dates$(Some(Set(date1, date2)))
        .uuids$(Some(Set(uuid1, uuid2)))
        .uris$(Some(Set(uri1, uri2)))
        .enums$(Some(Set(enum1, enum2)))
        .refs1$(Some(Set(1L, 2L)))
        .save

      Ns.int(1).strs.ints.longs.floats.doubles.bools.bigInts.bigDecs.dates.uuids.uris.enums.refs1.get.head === (
        1,
        Set("a", "b"),
        Set(1, 2),
        Set(1L, 2L),
        Set(2f, 1f),
        Set(2.0, 1.0),
        Set(true, false),
        Set(bigInt1, bigInt2),
        Set(bigDec2, bigDec1),
        Set(date1, date2),
        Set(uuid1, uuid2),
        Set(uri1, uri2),
        Set(enum1, enum2),
        Set(1, 2)
      )

      Ns.int(2)
        .strs$(None)
        .ints$(None)
        .longs$(None)
        .floats$(None)
        .doubles$(None)
        .bools$(None)
        .bigInts$(None)
        .bigDecs$(None)
        .dates$(None)
        .uuids$(None)
        .uris$(None)
        .enums$(None)
        .refs1$(None)
        .save

      Ns.int(2).strs$.ints$.longs$.floats$.doubles$.bools$.bigInts$.bigDecs$.dates$.uuids$.uris$.enums$.refs1$.get.head === (
        2, None, None, None, None, None, None, None, None, None, None, None, None, None
      )
    }
  }


  "Optional map attribute" >> {

    "syntax overlaps" in new CoreSetup {

      // Equal empty results
      Ns.str("a").save
      Ns.str.intMap_(nil).get === List("a")
      Ns.str.intMap$(None).get.head === ("a", None)

      // Equal mandatory results
      Ns.str("b").intMap$(Some(Map("c" -> 2))).save
      Ns.str.intMapK("c").get.head === ("b", 2)
      Ns.str.intMap$(Some(Map("c" -> 2))).get.head === ("b", Some(Map("c" -> 2)))
    }


    "data/variable" in new CoreSetup {

      Ns.int(1).intMap$(Some(Map("a" -> 11, "b" -> 12))).save
      Ns.int(1).intMap.get.head === (1, Map("a" -> 11, "b" -> 12))
      Ns.int(1).intMap$.get.head === (1, Some(Map("a" -> 11, "b" -> 12)))
      Ns.int.intMap$(Some(Map("a" -> 11, "b" -> 12))).get.head === (1, Some(Map("a" -> 11, "b" -> 12)))

      val some2 = Some(Map("a" -> 21, "b" -> 22))
      Ns.int(2).intMap$(some2).save
      Ns.int(2).intMap.get.head === (2, Map("a" -> 21, "b" -> 22))
      Ns.int(2).intMap$.get.head === (2, Some(Map("a" -> 21, "b" -> 22)))
      Ns.int.intMap$(some2).get.head === (2, Some(Map("a" -> 21, "b" -> 22)))

      val map3 = Map("a" -> 31, "b" -> 32)
      Ns.int(3).intMap$(Some(map3)).save
      Ns.int(3).intMap.get.head === (3, Map("a" -> 31, "b" -> 32))
      Ns.int(3).intMap$.get.head === (3, Some(Map("a" -> 31, "b" -> 32)))
      Ns.int.intMap$(Some(map3)).get.head === (3, Some(Map("a" -> 31, "b" -> 32)))

      val pair = "a" -> 41
      Ns.int(4).intMap$(Some(Map(pair, "b" -> 42))).save
      Ns.int(4).intMap.get.head === (4, Map("a" -> 41, "b" -> 42))
      Ns.int(4).intMap$.get.head === (4, Some(Map("a" -> 41, "b" -> 42)))
      Ns.int.intMap$(Some(Map(pair, "b" -> 42))).get.head === (4, Some(Map("a" -> 41, "b" -> 42)))

      val (a, i52) = ("a", 52)
      Ns.int(5).intMap$(Some(Map(a -> 51, "b" -> i52))).save
      Ns.int(5).intMap.get.head === (5, Map("a" -> 51, "b" -> 52))
      Ns.int(5).intMap$.get.head === (5, Some(Map("a" -> 51, "b" -> 52)))
      Ns.int.intMap$(Some(Map(a -> 51, "b" -> i52))).get.head === (5, Some(Map("a" -> 51, "b" -> 52)))

      Ns.int(6).intMap$(None).save
      Ns.int(6).intMap.get === Nil
      Ns.int(6).intMap$.get.head === (6, None)
      Ns.int.intMap$(None).get.head === (6, None)

      val none = None
      Ns.int(7).intMap$(none).save
      Ns.int(7).intMap.get === Nil
      Ns.int(7).intMap$.get.head === (7, None)
      Ns.int.intMap$(none).get === List((6, None), (7, None))
    }
  }
}