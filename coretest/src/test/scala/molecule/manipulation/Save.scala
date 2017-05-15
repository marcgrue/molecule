package molecule.manipulation

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class Save extends CoreSpec {


  "Data-molecule" >> {

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
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't save multiple values for cardinality-one attribute:" +
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

      Ns.strs.ints.longs.floats.doubles.dates.uuids.uris.enums.get.head ===(
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

      Ns.str.int.long.float.double.bool.date.uuid.uri.enum.get.head ===(
        "a", 1, 1L, 1.0f, 1.0, true, date1, uuid1, uri1, "enum1")
    }


    "Card many attrs" in new CoreSetup {

      // Construct a "Data-Molecule" with an attribute value and add it to the database

      Ns.strs("a").save
      Ns.strs("b", "c").save
      Ns.strs.get.head === Set("a", "b", "c")

      Ns.ints(1).save
      Ns.ints(2, 3).save
      Ns.ints.get.head === Set(1, 2, 3)

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

      Ns.str.Ref1.int1.str1.Ref2.str2.get.head ===("273 Broadway", 10700, "New York", "USA")
    }
  }
}