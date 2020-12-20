package molecule.tests.core.crud.insert

import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out10._
import molecule.TestSpec


class InsertInput extends TestSpec {

  "Card one" in new CoreSetup {

    // 1. Define "Insert-molecule"
    val insertStr = Ns.str.insert

    // 2. Re-use Insert-molecule to insert values
    insertStr("a")
    insertStr("b")
    insertStr("c")

    Ns.str.get.sorted === List("a", "b", "c")


    val insertAll = Ns.str.int.long.float.double.bool.date.uuid.uri.enum.insert

    // Var-arg for single entity
    insertAll(" ", 0, 0L, 0.0f, 0.0, false, date0, uuid0, uri0, "enum0")

    // List of tuples for multiple entities
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

    Ns.strs.get.head === Set("a", "b", "c")


    val insertAlls = Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.insert

    insertAlls(Set(" "), Set(0), Set(0L), Set(0.0f), Set(0.0), Set(date0), Set(uuid0), Set(uri0), Set("enum0"))
    insertAlls(List(
      (Set("a"), Set(1), Set(1L), Set(1.0f), Set(1.0), Set(date1), Set(uuid1), Set(uri1), Set("enum1")),
      (Set("b"), Set(2), Set(2L), Set(2.0f), Set(2.0), Set(date2), Set(uuid2), Set(uri2), Set("enum2"))
    ))

    Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head === (
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