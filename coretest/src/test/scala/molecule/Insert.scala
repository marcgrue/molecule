package molecule

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class Insert extends CoreSpec {


  "Single attribute" >> {

    "Card one" in new CoreSetup {

      // The `insert` method performs the compile time analysis of the molecule
      // The `apply` method inserts the type-inferred data at runtime
      Ns.str.insert.apply("a")

      // We can enter data for one attribute in 4 different ways:

      // 1. Comma-separated list
      Ns.str insert "b"
      Ns.str.insert("c", "d")

      // 2. List of values
      Ns.str insert List("e")
      Ns.str insert List("f", "g")

      // All values inserted
      Ns.str.get.sorted === List("a", "b", "c", "d", "e", "f", "g")


      Ns.int.insert(1)
      Ns.int.insert(2, 3)
      Ns.int.insert(List(4))
      Ns.int.insert(List(5, 1))
      // Unique values coalesced
      Ns.int.get.sorted === List(1, 2, 3, 4, 5)


      Ns.long.insert(1L)
      Ns.long.insert(2L, 3L)
      Ns.long.insert(List(4L))
      Ns.long.insert(List(5L, 1L))
      // Unique values coalesced
      Ns.long.get.sorted === List(1L, 2L, 3L, 4L, 5L)


      Ns.float.insert(1.0f)
      Ns.float.insert(2.0f, 3.0f)
      Ns.float.insert(List(4.0f))
      Ns.float.insert(List(5.0f, 1.0f))
      // Unique values coalesced
      Ns.float.get.sorted === List(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)


      Ns.double.insert(1.0)
      Ns.double.insert(2.0, 3.0)
      Ns.double.insert(List(4.0))
      Ns.double.insert(List(5.0, 1.0))
      // Unique values coalesced
      Ns.double.get.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0)


      Ns.bool.insert(true)
      Ns.bool.insert(true, false)
      Ns.bool.insert(List(true))
      Ns.bool.insert(List(true, false))
      // Unique values coalesced
      Ns.bool.get.sorted === List(false, true)


      Ns.date.insert(date1)
      Ns.date.insert(date2, date3)
      Ns.date.insert(List(date4))
      Ns.date.insert(List(date5, date1))
      // Unique values coalesced
      Ns.date.get.sorted === List(date1, date2, date3, date4, date5)


      Ns.uuid.insert(uuid1)
      Ns.uuid.insert(uuid2, uuid3)
      Ns.uuid.insert(List(uuid4))
      Ns.uuid.insert(List(uuid5, uuid1))
      // Unique values coalesced
      Ns.uuid.get.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5)


      Ns.uri.insert(uri1)
      Ns.uri.insert(uri2, uri3)
      Ns.uri.insert(List(uri4))
      Ns.uri.insert(List(uri5, uri1))
      // Unique values coalesced
      Ns.uri.get.sorted === List(uri1, uri2, uri3, uri4, uri5)


      Ns.enum.insert("enum1")
      Ns.enum.insert("enum2", "enum3")
      Ns.enum.insert(List("enum4"))
      Ns.enum.insert(List("enum5", "enum1"))
      // Unique values coalesced
      Ns.enum.get.sorted === List(enum1, enum2, enum3, enum4, enum5)
    }


    "Card many" in new CoreSetup {

      // The `insert` method performs the compile time analysis of the molecule
      // The `apply` method inserts the type-inferred data at runtime
      Ns.strs.insert.apply(Set("a"))

      // We can enter data for one attribute in 3 different ways:

      // 1. Set of values
      Ns.strs insert Set("b")
      Ns.strs insert Set("c", "d")

      // 2. Comma-separated list of sets of values
      Ns.strs.insert(Set("e"), Set("f"))

      // 3. List of sets of values
      Ns.strs insert List(Set("g"))
      Ns.strs insert List(Set("h", "i"))
      Ns.strs insert List(Set("j"), Set("k"))

      // All values inserted
      Ns.strs.one.toSeq.sorted === List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")


      Ns.ints.insert(Set(1))
      Ns.ints.insert(Set(2, 3))
      Ns.ints.insert(Set(4), Set(5))
      Ns.ints.insert(List(Set(6)))
      Ns.ints.insert(List(Set(7, 8)))
      Ns.ints.insert(List(Set(9), Set(10)))
      Ns.ints.one.toSeq.sorted === List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

      Ns.longs.insert(Set(1L))
      Ns.longs.insert(Set(2L, 3L))
      Ns.longs.insert(Set(4L), Set(5L))
      Ns.longs.insert(List(Set(6L)))
      Ns.longs.insert(List(Set(7L, 8L)))
      Ns.longs.insert(List(Set(9L), Set(10L)))
      Ns.longs.one.toSeq.sorted === List(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)

      Ns.floats.insert(Set(1.0f))
      Ns.floats.insert(Set(2.0f, 3.0f))
      Ns.floats.insert(Set(4.0f), Set(5.0f))
      Ns.floats.insert(List(Set(6.0f)))
      Ns.floats.insert(List(Set(7.0f, 8.0f)))
      Ns.floats.insert(List(Set(9.0f), Set(10.0f)))
      Ns.floats.one.toSeq.sorted === List(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f)

      Ns.doubles.insert(Set(1.0))
      Ns.doubles.insert(Set(2.0, 3.0))
      Ns.doubles.insert(Set(4.0), Set(5.0))
      Ns.doubles.insert(List(Set(6.0)))
      Ns.doubles.insert(List(Set(7.0, 8.0)))
      Ns.doubles.insert(List(Set(9.0), Set(10.0)))
      Ns.doubles.one.toSeq.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)

      Ns.dates.insert(Set(date1))
      Ns.dates.insert(Set(date2, date3))
      Ns.dates.insert(Set(date4), Set(date5))
      Ns.dates.insert(List(Set(date6)))
      Ns.dates.insert(List(Set(date7, date8)))
      Ns.dates.insert(List(Set(date9), Set(date10)))
      Ns.dates.one.toSeq.sorted === List(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10)

      Ns.uuids.insert(Set(uuid1))
      Ns.uuids.insert(Set(uuid2, uuid3))
      Ns.uuids.insert(Set(uuid4), Set(uuid5))
      Ns.uuids.insert(List(Set(uuid6)))
      Ns.uuids.insert(List(Set(uuid7, uuid8)))
      Ns.uuids.insert(List(Set(uuid9), Set(uuid10)))
      Ns.uuids.one.toSeq.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7, uuid8, uuid9, uuid10)

      Ns.uris.insert(Set(uri1))
      Ns.uris.insert(Set(uri2, uri3))
      Ns.uris.insert(Set(uri4), Set(uri5))
      Ns.uris.insert(List(Set(uri6)))
      Ns.uris.insert(List(Set(uri7, uri8)))
      Ns.uris.insert(List(Set(uri9), Set(uri10)))
      Ns.uris.one.toSeq.sortBy(_.toString) === List(uri1, uri10, uri2, uri3, uri4, uri5, uri6, uri7, uri8, uri9)

      Ns.enums.insert(Set(enum1))
      Ns.enums.insert(Set(enum2, enum3))
      Ns.enums.insert(Set(enum4), Set(enum5))
      Ns.enums.insert(List(Set(enum6)))
      Ns.enums.insert(List(Set(enum7, enum8)))
      Ns.enums.insert(List(Set(enum9), Set(enum0)))
      Ns.enums.one.toSeq.sorted === List(enum0, enum1, enum2, enum3, enum4, enum5, enum6, enum7, enum8, enum9)
    }
  }


  "Multiple attributes" >> {

    "Card one" in new CoreSetup {

      // Insert 3 entities as tuples of values
      // Note that values are typechecked against the attribute types of the molecule
      Ns.str.int.long.float.double.bool.date.uuid.uri.enum insert List(
        (" ", 0, 0L, 0.0f, 0.0, false, date0, uuid0, uri0, "enum0"),
        ("a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1"),
        ("b", 2, 2L, 2.0f, 2.0, false, date2, uuid2, uri2, "enum2")
      )

      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.sortBy(_._1) === List(
        (" ", 0, 0L, 0.0f, 0.0, false, date0, uuid0, uri0, "enum0"),
        ("a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1"),
        ("b", 2, 2L, 2.0f, 2.0, false, date2, uuid2, uri2, "enum2")
      )
    }


    "Card many" in new CoreSetup {

      // Insert 3 entities as tuples of values
      // Note that values are typechecked against the attribute types of the molecule
      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums insert List(
        (Set("a", "b"),
          Set(1, 2),
          Set(1L, 2L),
          Set(1.0f, 2.0f),
          Set(1.0, 2.0),
          Set(date1, date2),
          Set(uuid1, uuid2),
          Set(uri1, uri2),
          Set("enum1", "enum2")),
        (Set("c", "d"),
          Set(3, 4),
          Set(3L, 4L),
          Set(3.0f, 4.0f),
          Set(3.0, 4.0),
          Set(date3, date4),
          Set(uuid3, uuid4),
          Set(uri3, uri4),
          Set("enum3", "enum4"))
      )

      // Unique values coalesced
      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.one ===(
        Set("d", "a", "b", "c"),
        Set(1, 4, 3, 2),
        Set(1L, 4L, 3L, 2L),
        Set(2.0f, 4.0f, 1.0f, 3.0f),
        Set(2.0, 4.0, 1.0, 3.0),
        Set(date4, date1, date2, date3),
        Set(uuid3, uuid2, uuid4, uuid1),
        Set(uri1, uri2, uri3, uri4),
        Set("enum4", "enum1", "enum3", "enum2"))
    }
  }


  "Relationships" >> {

    "Basics" in new CoreSetup {

      // Asserting a fact in the `Ref1` namespace is the same as creating
      // one in the `Ns` namespace (no references between the two are made):

      val a0 = Ns.str.insert("a0").eid
      a0.touch === Map(
        ":db/id" -> 17592186045445L,
        ":ns/str" -> "a0")

      val b0 = Ref1.str1.insert("b0").eid
      b0.touch === Map(
        ":db/id" -> 17592186045447L,
        ":ref1/str1" -> "b0")

      // We don't want orphan entities (with no attributes asserted) having references to other entities
      "RuntimeException when inserting" in new CoreSetup {
        (Ns.Ref1.str1.insert must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
          "[output.Molecule:modelCheck (2)] Namespace `Ns` in insert molecule has no mandatory attributes. Please add at least one."
      }
      "RuntimeException when inserting" in new CoreSetup {
        (Ns.str.Ref1.Ref2.str2.insert must throwA[RuntimeException]).message === "Got the exception java.lang.RuntimeException: " +
          "[output.Molecule:modelCheck (2)] Namespace `Ref1` in insert molecule has no mandatory attributes. Please add at least one."
      }


      // If we also assert a fact in `Ns` we will get an entity with
      // a :ns/str assertion ("a0") of namespace `Ns` and a reference to an entity
      // with another :ref1/str assertion ("b1") in namespace `Ref1`:

      val a0b1 = Ns.str.Ref1.str1.insert("a0", "b1").eid
      a0b1.touch === Map(
        ":db/id" -> 17592186045449L,
        ":ns/str" -> "a0",
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045450L,
          ":ref1/str1" -> "b1"))


      // We can expand our graph one level deeper

      val a0b1c2 = Ns.str.Ref1.str1.Ref2.str2.insert("a0", "b1", "c2").eid
      a0b1c2.touch === Map(
        ":db/id" -> 17592186045452L,
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045453L,
          ":ref1/ref2" -> Map(
            ":db/id" -> 17592186045454L,
            ":ref2/str2" -> "c2"),
          ":ref1/str1" -> "b1"),
        ":ns/str" -> "a0")


      // We can limit the depth of the retrieved graph

      a0b1c2.touch(3) === Map(
        ":db/id" -> 17592186045452L,
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045453L,
          ":ref1/ref2" -> Map(
            ":db/id" -> 17592186045454L,
            ":ref2/str2" -> "c2"),
          ":ref1/str1" -> "b1"),
        ":ns/str" -> "a0")

      a0b1c2.touch(2) === Map(
        ":db/id" -> 17592186045452L,
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045453L,
          ":ref1/ref2" -> 17592186045454L,
          ":ref1/str1" -> "b1"),
        ":ns/str" -> "a0")

      a0b1c2.touch(1) === Map(
        ":db/id" -> 17592186045452L,
        ":ns/ref1" -> 17592186045453L,
        ":ns/str" -> "a0")

      // Use `touchQuoted` to generate a quoted graph that you can paste into your tests
      a0b1c2.touchQuoted(1) === Map(
        "\n\":db/id\"" -> "17592186045452L",
        "\n\":ns/ref1\"" -> "17592186045453L",
        "\n\":ns/str\"" -> "\"a0\"")
    }


    "Multiple values across namespaces" in new CoreSetup {

      Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.insert("a0", 0, "b1", 1, "c2", 2)
      Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.one ===("a0", 0, "b1", 1, "c2", 2)

      Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.insert(Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))
      Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.one ===(Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))

      // Address example
      val address = Ns.str.Ref1.int1.str1.Ref2.str2.insert("273 Broadway", 10700, "New York", "USA").eid
      address.touch === Map(
        ":db/id" -> 17592186045453L,
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045454L,
          ":ref1/int1" -> 10700,
          ":ref1/ref2" -> Map(":db/id" -> 17592186045455L, ":ref2/str2" -> "USA"),
          ":ref1/str1" -> "New York"),
        ":ns/str" -> "273 Broadway")
    }


    "Card many references" in new CoreSetup {

      val id = Ns.int.Refs1.str1.insert(42, "r").eid
      id.touch === Map(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(// <-- notice we have a list of references now (with one ref here)
          Map(":db/id" -> 17592186045446L, ":ref1/str1" -> "r")),
        ":ns/int" -> 42
      )


      // Note that applying multiple values creates multiple base entities with a
      // reference to each new `:ref1/str` assertion, so that we get the following:

      val List(id1, ref1, id2, ref2) = Ns.int.Refs1.str1.insert.apply(Seq((1, "r"), (2, "s"))).eids
      id1.touch === Map(
        ":db/id" -> id1,
        ":ns/refs1" -> List(
          Map(":db/id" -> ref1, ":ref1/str1" -> "r")),
        ":ns/int" -> 1
      )
      id2.touch === Map(
        ":db/id" -> id2,
        ":ns/refs1" -> List(
          Map(":db/id" -> ref2, ":ref1/str1" -> "s")),
        ":ns/int" -> 2
      )
    }


    "Sub-molecules" in new CoreSetup {

      // If we want to create two references from the same base entity we
      // can us "group" notation `*` after our cardinality-many reference
      // and then define what sub-attributes we want to add.

      // Note that the "sub-molecule" we apply is treated as a single type
      // - when more than 1 attribute, like a tuple.

      Ns.Refs1.*(Ref1.str1).insert(List("r1", "r2")).eid.touch === Map(
        ":db/id" -> 17592186045445L,
        ":ns/refs1" -> List(
          Map(":db/id" -> 17592186045446L, ":ref1/str1" -> "r1"),
          Map(":db/id" -> 17592186045447L, ":ref1/str1" -> "r2")))

      // Like the classical order/products example
      // Note how our "sub-molecule" `Ref1.int.str` is regarded as
      // one type `Seq[(Int, String)]` by the outer molecule

      val order = m(Ns.str.Refs1 * Ref1.int1.str1).insert("order", List((4, "product1"), (7, "product2"))).eid
      order.touch === Map(
        ":db/id" -> 17592186045449L,
        ":ns/refs1" -> List(
          Map(":db/id" -> 17592186045450L, ":ref1/int1" -> 4, ":ref1/str1" -> "product1"),
          Map(":db/id" -> 17592186045451L, ":ref1/int1" -> 7, ":ref1/str1" -> "product2")),
        ":ns/str" -> "order")
    }
  }


  "Data-molecule" >> {

    "Card one attr" in new CoreSetup {

      // Construct a "Data-Molecule" with an attribute value and add it to the database

      Ns.str("a").add
      Ns.int(1).add
      Ns.long(1L).add
      Ns.float(1.0f).add
      Ns.double(1.0).add
      Ns.bool(true).add
      Ns.date(date1).add
      Ns.uuid(uuid1).add
      Ns.uri(uri1).add
      Ns.enum("enum1").add

      Ns.str.one === "a"
      Ns.int.one === 1
      Ns.long.one === 1L
      Ns.float.one === 1.0f
      Ns.double.one === 1.0
      Ns.bool.one === true
      Ns.date.one === date1
      Ns.uuid.one === uuid1
      Ns.uri.one === uri1
      Ns.enum.one === enum1
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
        .enums("enum1", "enum2").add

      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.one ===(
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

      Ns.str("a").int(1).long(1L).float(1.0f).double(1.0).bool(true).date(date1).uuid(uuid1).uri(uri1).enum("enum1").add

      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.one ===(
        "a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1")
    }


    "Card many attrs" in new CoreSetup {

      // Construct a "Data-Molecule" with an attribute value and add it to the database

      Ns.strs("a").add
      Ns.strs("b", "c").add
      Ns.strs.one === Set("a", "b", "c")

      Ns.ints(1).add
      Ns.ints(2, 3).add
      Ns.ints.one === Set(1, 2, 3)

      Ns.longs(1L).add
      Ns.longs(2L, 3L).add
      Ns.longs.one === Set(1L, 2L, 3L)

      Ns.floats(1.0f).add
      Ns.floats(2.0f, 3.0f).add
      Ns.floats.one === Set(1.0f, 2.0f, 3.0f)

      Ns.doubles(1.0).add
      Ns.doubles(2.0, 3.0).add
      Ns.doubles.one === Set(1.0, 2.0, 3.0)

      // Ns.bools not implemented...

      Ns.dates(date1).add
      Ns.dates(date2, date3).add
      Ns.dates.one === Set(date1, date2, date3)

      Ns.uuids(uuid1).add
      Ns.uuids(uuid2, uuid3).add
      Ns.uuids.one === Set(uuid1, uuid2, uuid3)

      Ns.uris(uri1).add
      Ns.uris(uri2, uri3).add
      Ns.uris.one === Set(uri1, uri2, uri3)

      Ns.enums("enum1").add
      Ns.enums("enum2", "enum3").add
      Ns.enums.one === Set("enum1", "enum2", "enum3")
    }


    "Relationships" in new CoreSetup {

      val address = Ns.str("273 Broadway").Ref1.int1(10700).str1("New York").Ref2.str2("USA").add.eid
      address.touch === Map(
        ":db/id" -> 17592186045445L,
        ":ns/ref1" -> Map(
          ":db/id" -> 17592186045446L,
          ":ref1/int1" -> 10700,
          ":ref1/ref2" -> Map(":db/id" -> 17592186045447L, ":ref2/str2" -> "USA"),
          ":ref1/str1" -> "New York"),
        ":ns/str" -> "273 Broadway")

      Ns.str.Ref1.int1.str1.Ref2.str2.one ===("273 Broadway", 10700, "New York", "USA")
    }
  }


  "Insert-molecule (2-step insertion)" >> {

    "Card one" in new CoreSetup {

      // 1. Define "Insert-molecule"
      val insertStr = Ns.str.insert

      // 2. Re-use Insert-molecule to insert values
      insertStr("a")
      insertStr("b")
      insertStr("c")

      Ns.str.get.sorted === List("a", "b", "c")


      val insertAll = Ns.str.int.long.float.double.bool.date.uuid.uri.enum.insert

      insertAll(" ", 0, 0L, 0.0f, 0.0, false, date0, uuid0, uri0, "enum0")
      insertAll(List(
        ("a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1"),
        ("b", 2, 2L, 2.0f, 2.0, false, date2, uuid2, uri2, "enum2")
      ))

      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.sortBy(_._1) === List(
        (" ", 0, 0L, 0.0f, 0.0, false, date0, uuid0, uri0, "enum0"),
        ("a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1"),
        ("b", 2, 2L, 2.0f, 2.0, false, date2, uuid2, uri2, "enum2")
      )
    }


    "Card many" in new CoreSetup {

      // 1. Define "Insert-molecule"
      val insertStrs = Ns.strs.insert

      // 2. Re-use Insert-molecule to insert values
      insertStrs(Set("a"))
      insertStrs(Set("b", "c"))

      Ns.strs.one === List("a", "b", "c")


      val insertAlls = Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.insert

      insertAlls(Set(" "), Set(0), Set(0L), Set(0.0f), Set(0.0), Set(date0), Set(uuid0), Set(uri0), Set("enum0"))
      insertAlls(List(
        (Set("a"), Set(1), Set(1L), Set(1.0f), Set(1.0), Set(date1), Set(uuid1), Set(uri1), Set("enum1")),
        (Set("b"), Set(2), Set(2L), Set(2.0f), Set(2.0), Set(date2), Set(uuid2), Set(uri2), Set("enum2"))
      ))

      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.one ===(
        Set("a", "b", " "),
        Set(0, 1, 2),
        Set(0L, 1L, 2L),
        Set(0.0f, 2.0f, 1.0f),
        Set(0.0, 2.0, 1.0),
        Set(date0, date1, date2),
        Set(uuid0, uuid1, uuid2),
        Set(uri0, uri1, uri2),
        Set("enum1", "enum0", "enum2"))
    }


    "Relationships" in new CoreSetup {

      // 1. Define Input-molecule
      val insertAddress = Ns.str.Ref1.int1.str1.Ref2.str2.insert

      // 2. Insert data using input molecule as template
      insertAddress("273 Broadway", 10700, "New York", "USA")
      insertAddress("2054, 5th Ave", 10800, "New York", "USA")

      Ns.str.Ref1.int1.str1.Ref2.str2.get === List(
        ("2054, 5th Ave", 10800, "New York", "USA"),
        ("273 Broadway", 10700, "New York", "USA")
      )
    }
  }


  "Optional inserts" >> {

    "Card one attrs" in new CoreSetup {

      Ns.int.str$ insert List(
        (1, None),
        (2, Some("a"))
      )

      Ns.int.str$.get === List(
        (1, None),
        (2, Some("a"))
      )

      Ns.int.str.get === List(
        (2, "a")
      )
    }


    "Card many attrs" in new CoreSetup {

      Ns.int.strs$ insert List(
        (1, None),
        (2, Some(Set("a", "b")))
      )

      Ns.int.strs$.get === List(
        (1, None),
        (2, Some(Set("a", "b")))
      )

      Ns.int.strs.get === List(
        (2, Set("a", "b"))
      )

      // Alternatively we can apply typed empty Sets

      Ns.date.longs insert List(
        (date1, Set[Long]()),
        (date2, Set(20L, 21L))
      )

      Ns.date.longs$.get === List(
        (date1, None),
        (date2, Some(Set(20L, 21L)))
      )

      Ns.date.longs.get === List(
        (date2, Set(20L, 21L))
      )
    }
  }

  "Additional inserts/upserts" in new CoreSetup {

    // If we need to insert more than 22 facts for a single namespace
    // we can start asserting those 22 facts, then use the returned eid
    // to continue (here shown with only 1 fact):

    // Insert maximum of 22 facts
    val eid = Ns.str.insert("a").eid

    // Use entity id to continue adding more values
    Ns.e.int.insert(eid, 42)

    // Only a single entity has been created
    Ns.e.str.int.get === List(
      (eid, "a", 42)
    )

    // Optional attribute after `e` works too
    Ns.e.long$.bool.insert(eid, None, true)

    // Only a single entity has been created
    Ns.e.str.int.long$.bool.get === List(
      (eid, "a", 42, None, true)
    )
  }
}