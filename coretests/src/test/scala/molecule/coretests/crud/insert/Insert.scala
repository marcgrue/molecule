package molecule.coretests.crud.insert

import molecule.api.out10._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.facade.TxReport
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


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
      Ns.strs.get.head.toSeq.sorted === List("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")


      Ns.ints.insert(Set(1))
      Ns.ints.insert(Set(2, 3))
      Ns.ints.insert(Set(4), Set(5))
      Ns.ints.insert(List(Set(6)))
      Ns.ints.insert(List(Set(7, 8)))
      Ns.ints.insert(List(Set(9), Set(10)))
      Ns.ints.get.head.toSeq.sorted === List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

      Ns.longs.insert(Set(1L))
      Ns.longs.insert(Set(2L, 3L))
      Ns.longs.insert(Set(4L), Set(5L))
      Ns.longs.insert(List(Set(6L)))
      Ns.longs.insert(List(Set(7L, 8L)))
      Ns.longs.insert(List(Set(9L), Set(10L)))
      Ns.longs.get.head.toSeq.sorted === List(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)

      Ns.floats.insert(Set(1.0f))
      Ns.floats.insert(Set(2.0f, 3.0f))
      Ns.floats.insert(Set(4.0f), Set(5.0f))
      Ns.floats.insert(List(Set(6.0f)))
      Ns.floats.insert(List(Set(7.0f, 8.0f)))
      Ns.floats.insert(List(Set(9.0f), Set(10.0f)))
      Ns.floats.get.head.toSeq.sorted === List(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f)

      Ns.doubles.insert(Set(1.0))
      Ns.doubles.insert(Set(2.0, 3.0))
      Ns.doubles.insert(Set(4.0), Set(5.0))
      Ns.doubles.insert(List(Set(6.0)))
      Ns.doubles.insert(List(Set(7.0, 8.0)))
      Ns.doubles.insert(List(Set(9.0), Set(10.0)))
      Ns.doubles.get.head.toSeq.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)

      Ns.dates.insert(Set(date1))
      Ns.dates.insert(Set(date2, date3))
      Ns.dates.insert(Set(date4), Set(date5))
      Ns.dates.insert(List(Set(date6)))
      Ns.dates.insert(List(Set(date7, date8)))
      Ns.dates.insert(List(Set(date9), Set(date10)))
      Ns.dates.get.head.toSeq.sorted === List(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10)

      Ns.uuids.insert(Set(uuid1))
      Ns.uuids.insert(Set(uuid2, uuid3))
      Ns.uuids.insert(Set(uuid4), Set(uuid5))
      Ns.uuids.insert(List(Set(uuid6)))
      Ns.uuids.insert(List(Set(uuid7, uuid8)))
      Ns.uuids.insert(List(Set(uuid9), Set(uuid10)))
      Ns.uuids.get.head.toSeq.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7, uuid8, uuid9, uuid10)

      Ns.uris.insert(Set(uri1))
      Ns.uris.insert(Set(uri2, uri3))
      Ns.uris.insert(Set(uri4), Set(uri5))
      Ns.uris.insert(List(Set(uri6)))
      Ns.uris.insert(List(Set(uri7, uri8)))
      Ns.uris.insert(List(Set(uri9), Set(uri10)))
      Ns.uris.get.head.toSeq.sortBy(_.toString) === List(uri1, uri10, uri2, uri3, uri4, uri5, uri6, uri7, uri8, uri9)

      Ns.enums.insert(Set(enum1))
      Ns.enums.insert(Set(enum2, enum3))
      Ns.enums.insert(Set(enum4), Set(enum5))
      Ns.enums.insert(List(Set(enum6)))
      Ns.enums.insert(List(Set(enum7, enum8)))
      Ns.enums.insert(List(Set(enum9), Set(enum0)))
      Ns.enums.get.head.toSeq.sorted === List(enum0, enum1, enum2, enum3, enum4, enum5, enum6, enum7, enum8, enum9)
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
      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head === (
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


  "Optional values" >> {

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

      Ns.date.longs$.get.sortBy(_._1) === List(
        (date1, None),
        (date2, Some(Set(20L, 21L))),
      )

      Ns.date.longs.get === List(
        (date2, Set(20L, 21L))
      )
    }
  }


  ">22 inserts/upserts" in new CoreSetup {

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