package molecule.coretests.attr

import molecule._

import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.coretests.util.dsl.coreTest._


class Input extends CoreSpec {


  // Parameterized molecules have a `?` placeholder for an expected input value
  // and we call them "Input-molecules".

  // Input-molecules have the benefit that you can assign them to a variable
  // and then re-use them with various input values. This also allows Datomic
  // to cache and optimize the query and thereby improve runtime performance.


  "Introduction" in new CoreSetup {

    Ns.str.int insert List(("John", 37), ("Lisa", 28), ("Ben", 28), ("Ann", 14))


    // "Input-molecule" with a `?` placeholder for an expected input value
    // Often we are not interested in returning the input value so
    // we make the integer attribute tacet by adding an underscore
    val personOfAge = m(Ns.str.int_(?))

    // We can now use the input-molecule as a query template

    // Equality
    personOfAge(37).get === List("John")
    personOfAge(28).get === List("Ben", "Lisa")
    personOfAge(10).get === List()

    val personsYoungerThan = m(Ns.str.int_.<(?))

    // Apply expression value
    personsYoungerThan(30).get === List("Ann", "Ben", "Lisa")

    // We don't have to assign an input-molecule to a variable
    m(Ns.str.int_.<(?))(30).get === List("Ann", "Ben", "Lisa")
    // Although then it would be easier to just say
    Ns.str.int_.<(30).get === List("Ann", "Ben", "Lisa")

    // For brevity we test some more expressions in the short form
    m(Ns.str.int_.>(?))(30).get === List("John")
    m(Ns.str.int_.<=(?))(28).get === List("Ann", "Ben", "Lisa")
    m(Ns.str.int_.>=(?))(28).get === List("John", "Ben", "Lisa")
    m(Ns.str.int_.!=(?))(30).get === List("Ann", "John", "Ben", "Lisa")
    m(Ns.str.int_.!=(?))(28).get === List("Ann", "John")
    m(Ns.str.int_.not(?))(28).get === List("Ann", "John")
  }

  class OneSetup extends CoreSetup {
    Ns.int.str insert List((1, "a"), (2, "b"), (3, "c"))
    Ns.long.int insert List((1L, 1), (2L, 2), (3L, 3))
    Ns.int.long insert List((1, 1L), (2, 2L), (3, 3L))
    Ns.int.float insert List((1, 1.0f), (2, 2.0f), (3, 3.0f))
    Ns.int.double insert List((1, 1.0), (2, 2.0), (3, 3.0))
    Ns.int.bool insert List((0, false), (1, true))
    Ns.int.date insert List((1, date1), (2, date2), (3, date3))
    Ns.int.uuid insert List((1, uuid1), (2, uuid2), (3, uuid3))
    Ns.int.uri insert List((1, uri1), (2, uri2), (3, uri3))
    Ns.int.enum insert List((1, "enum1"), (2, "enum2"), (3, "enum3"))
  }

  class ManySetup extends CoreSetup {
    Ns.int.strs insert List((1, Set("a", "b")), (2, Set("b", "c")), (3, Set("c", "d")))
    Ns.long.ints insert List((1L, Set(1, 2)), (2L, Set(2, 3)), (3L, Set(3, 4)))
    Ns.int.longs insert List((1, Set(1L, 2L)), (2, Set(2L, 3L)), (3, Set(3L, 4L)))
    Ns.int.floats insert List((1, Set(1.0f, 2.0f)), (2, Set(2.0f, 3.0f)), (3, Set(3.0f, 4.0f)))
    Ns.int.doubles insert List((1, Set(1.0, 2.0)), (2, Set(2.0, 3.0)), (3, Set(3.0, 4.0)))
    Ns.int.bools insert List((0, Set(false)), (1, Set(true)), (2, Set(false, true)))
    Ns.int.dates insert List((1, Set(date1, date2)), (2, Set(date2, date3)), (3, Set(date3, date4)))
    Ns.int.uuids insert List((1, Set(uuid1, uuid2)), (2, Set(uuid2, uuid3)), (3, Set(uuid3, uuid4)))
    Ns.int.uris insert List((1, Set(uri1, uri2)), (2, Set(uri2, uri3)), (3, Set(uri3, uri4)))
    Ns.int.enums insert List((1, Set("enum1", "enum2")), (2, Set("enum2", "enum3")), (3, Set("enum3", "enum4")))
  }


  "1 input parameter" >> {

    "Tacet expressions" in new OneSetup {
      m(Ns.int.str_(?))("b").get === List(2)
      m(Ns.int.str_.<(?))("b").get === List(1)
      m(Ns.int.str_.>(?))("b").get === List(3)
      m(Ns.int.str_.<=(?))("b").get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_.>=(?))("b").get.toSeq.sorted === List(2, 3)
      m(Ns.int.str_.!=(?))("b").get.toSeq.sorted === List(1, 3)
      m(Ns.int.str_.not(?))("b").get.toSeq.sorted === List(1, 3)

      m(Ns.long.int_(?))(2).get === List(2L)
      m(Ns.long.int_.<(?))(2).get === List(1L)
      m(Ns.long.int_.>(?))(2).get === List(3L)
      m(Ns.long.int_.<=(?))(2).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_.>=(?))(2).get.toSeq.sorted === List(2L, 3L)
      m(Ns.long.int_.!=(?))(2).get.toSeq.sorted === List(1L, 3L)
      m(Ns.long.int_.not(?))(2).get.toSeq.sorted === List(1L, 3L)

      m(Ns.int.long_(?))(2L).get === List(2)
      m(Ns.int.long_.<(?))(2L).get === List(1)
      m(Ns.int.long_.>(?))(2L).get === List(3)
      m(Ns.int.long_.<=(?))(2L).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_.>=(?))(2L).get.toSeq.sorted === List(2, 3)
      m(Ns.int.long_.!=(?))(2L).get.toSeq.sorted === List(1, 3)
      m(Ns.int.long_.not(?))(2L).get.toSeq.sorted === List(1, 3)

      m(Ns.int.float_(?))(2.0f).get === List(2)
      m(Ns.int.float_.<(?))(2.0f).get === List(1)
      m(Ns.int.float_.>(?))(2.0f).get === List(3)
      m(Ns.int.float_.<=(?))(2.0f).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_.>=(?))(2.0f).get.toSeq.sorted === List(2, 3)
      m(Ns.int.float_.!=(?))(2.0f).get.toSeq.sorted === List(1, 3)
      m(Ns.int.float_.not(?))(2.0f).get.toSeq.sorted === List(1, 3)

      m(Ns.int.double_(?))(2.0).get === List(2)
      m(Ns.int.double_.<(?))(2.0).get === List(1)
      m(Ns.int.double_.>(?))(2.0).get === List(3)
      m(Ns.int.double_.<=(?))(2.0).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_.>=(?))(2.0).get.toSeq.sorted === List(2, 3)
      m(Ns.int.double_.!=(?))(2.0).get.toSeq.sorted === List(1, 3)
      m(Ns.int.double_.not(?))(2.0).get.toSeq.sorted === List(1, 3)

      m(Ns.int.bool_(?))(true).get === List(1)
      m(Ns.int.bool_.<(?))(true).get === List(0)
      m(Ns.int.bool_.>(?))(true).get === List()
      m(Ns.int.bool_.<=(?))(true).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_.>=(?))(true).get === List(1)
      m(Ns.int.bool_.!=(?))(true).get === List(0)
      m(Ns.int.bool_.not(?))(true).get === List(0)

      m(Ns.int.bool_(?))(false).get === List(0)
      m(Ns.int.bool_.<(?))(false).get === List()
      m(Ns.int.bool_.>(?))(false).get === List(1)
      m(Ns.int.bool_.<=(?))(false).get === List(0)
      m(Ns.int.bool_.>=(?))(false).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_.!=(?))(false).get === List(1)
      m(Ns.int.bool_.not(?))(false).get === List(1)

      m(Ns.int.date_(?))(date2).get === List(2)
      m(Ns.int.date_.<(?))(date2).get === List(1)
      m(Ns.int.date_.>(?))(date2).get === List(3)
      m(Ns.int.date_.<=(?))(date2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.date_.>=(?))(date2).get.toSeq.sorted === List(2, 3)
      m(Ns.int.date_.!=(?))(date2).get.toSeq.sorted === List(1, 3)
      m(Ns.int.date_.not(?))(date2).get.toSeq.sorted === List(1, 3)

      m(Ns.int.uuid_(?))(uuid2).get === List(2)
      m(Ns.int.uuid_.<(?))(uuid2).get === List(1)
      m(Ns.int.uuid_.>(?))(uuid2).get === List(3)
      m(Ns.int.uuid_.<=(?))(uuid2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uuid_.>=(?))(uuid2).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uuid_.!=(?))(uuid2).get.toSeq.sorted === List(1, 3)
      m(Ns.int.uuid_.not(?))(uuid2).get.toSeq.sorted === List(1, 3)

      m(Ns.int.uri_(?))(uri2).get === List(2)
      m(Ns.int.uri_.<(?))(uri2).get === List(1)
      m(Ns.int.uri_.>(?))(uri2).get === List(3)
      m(Ns.int.uri_.<=(?))(uri2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uri_.>=(?))(uri2).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uri_.!=(?))(uri2).get.toSeq.sorted === List(1, 3)
      m(Ns.int.uri_.not(?))(uri2).get.toSeq.sorted === List(1, 3)

      m(Ns.int.enum_(?))(enum2).get === List(2)
      m(Ns.int.enum_.<(?))(enum2).get === List(1)
      m(Ns.int.enum_.>(?))(enum2).get === List(3)
      m(Ns.int.enum_.<=(?))(enum2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.enum_.>=(?))(enum2).get.toSeq.sorted === List(2, 3)
      m(Ns.int.enum_.!=(?))(enum2).get.toSeq.sorted === List(1, 3)
      m(Ns.int.enum_.not(?))(enum2).get.toSeq.sorted === List(1, 3)
    }

    "Expressions" in new OneSetup {
      m(Ns.int_.str(?))("b").get === List("b")
      m(Ns.int_.str.<(?))("b").get === List("a")
      m(Ns.int_.str.>(?))("b").get === List("c")
      m(Ns.int_.str.<=(?))("b").get === List("a", "b")
      m(Ns.int_.str.>=(?))("b").get === List("b", "c")
      m(Ns.int_.str.!=(?))("b").get === List("a", "c")
      m(Ns.int_.str.not(?))("b").get === List("a", "c")

      m(Ns.long_.int(?))(2).get === List(2)
      m(Ns.long_.int.<(?))(2).get === List(1)
      m(Ns.long_.int.>(?))(2).get === List(3)
      m(Ns.long_.int.<=(?))(2).get.toSeq.sorted === List(1, 2)
      m(Ns.long_.int.>=(?))(2).get.toSeq.sorted === List(2, 3)
      m(Ns.long_.int.!=(?))(2).get.toSeq.sorted === List(1, 3)
      m(Ns.long_.int.not(?))(2).get.toSeq.sorted === List(1, 3)

      m(Ns.int_.long(?))(2L).get === List(2L)
      m(Ns.int_.long.<(?))(2L).get === List(1L)
      m(Ns.int_.long.>(?))(2L).get === List(3L)
      m(Ns.int_.long.<=(?))(2L).get.toSeq.sorted === List(1L, 2L)
      m(Ns.int_.long.>=(?))(2L).get.toSeq.sorted === List(2L, 3L)
      m(Ns.int_.long.!=(?))(2L).get.toSeq.sorted === List(1L, 3L)
      m(Ns.int_.long.not(?))(2L).get.toSeq.sorted === List(1L, 3L)

      m(Ns.int_.float(?))(2.0f).get === List(2.0f)
      m(Ns.int_.float.<(?))(2.0f).get === List(1.0f)
      m(Ns.int_.float.>(?))(2.0f).get === List(3.0f)
      m(Ns.int_.float.<=(?))(2.0f).get.toSeq.sorted === List(1.0f, 2.0f)
      m(Ns.int_.float.>=(?))(2.0f).get.toSeq.sorted === List(2.0f, 3.0f)
      m(Ns.int_.float.!=(?))(2.0f).get.toSeq.sorted === List(1.0f, 3.0f)
      m(Ns.int_.float.not(?))(2.0f).get.toSeq.sorted === List(1.0f, 3.0f)

      m(Ns.int_.double(?))(2.0).get === List(2.0)
      m(Ns.int_.double.<(?))(2.0).get === List(1.0)
      m(Ns.int_.double.>(?))(2.0).get === List(3.0)
      m(Ns.int_.double.<=(?))(2.0).get.toSeq.sorted === List(1.0, 2.0)
      m(Ns.int_.double.>=(?))(2.0).get.toSeq.sorted === List(2.0, 3.0)
      m(Ns.int_.double.!=(?))(2.0).get.toSeq.sorted === List(1.0, 3.0)
      m(Ns.int_.double.not(?))(2.0).get.toSeq.sorted === List(1.0, 3.0)

      m(Ns.int_.bool(?))(true).get === List(true)
      m(Ns.int_.bool.<(?))(true).get === List(false)
      m(Ns.int_.bool.>(?))(true).get === List()
      m(Ns.int_.bool.<=(?))(true).get.toSeq.sorted === List(false, true)
      m(Ns.int_.bool.>=(?))(true).get === List(true)
      m(Ns.int_.bool.!=(?))(true).get === List(false)
      m(Ns.int_.bool.not(?))(true).get === List(false)

      m(Ns.int_.bool(?))(false).get === List(false)
      m(Ns.int_.bool.<(?))(false).get === List()
      m(Ns.int_.bool.>(?))(false).get === List(true)
      m(Ns.int_.bool.<=(?))(false).get === List(false)
      m(Ns.int_.bool.>=(?))(false).get.toSeq.sorted === List(false, true)
      m(Ns.int_.bool.!=(?))(false).get === List(true)
      m(Ns.int_.bool.not(?))(false).get === List(true)

      m(Ns.int_.date(?))(date2).get === List(date2)
      m(Ns.int_.date.<(?))(date2).get === List(date1)
      m(Ns.int_.date.>(?))(date2).get === List(date3)
      m(Ns.int_.date.<=(?))(date2).get.toSeq.sorted === List(date1, date2)
      m(Ns.int_.date.>=(?))(date2).get.toSeq.sorted === List(date2, date3)
      m(Ns.int_.date.!=(?))(date2).get.toSeq.sorted === List(date1, date3)
      m(Ns.int_.date.not(?))(date2).get.toSeq.sorted === List(date1, date3)

      m(Ns.int_.uuid(?))(uuid2).get === List(uuid2)
      m(Ns.int_.uuid.<(?))(uuid2).get === List(uuid1)
      m(Ns.int_.uuid.>(?))(uuid2).get === List(uuid3)
      m(Ns.int_.uuid.<=(?))(uuid2).get.toSeq.sorted === List(uuid1, uuid2)
      m(Ns.int_.uuid.>=(?))(uuid2).get.toSeq.sorted === List(uuid2, uuid3)
      m(Ns.int_.uuid.!=(?))(uuid2).get.toSeq.sorted === List(uuid1, uuid3)
      m(Ns.int_.uuid.not(?))(uuid2).get.toSeq.sorted === List(uuid1, uuid3)

      m(Ns.int_.uri(?))(uri2).get === List(uri2)
      m(Ns.int_.uri.<(?))(uri2).get === List(uri1)
      m(Ns.int_.uri.>(?))(uri2).get === List(uri3)
      m(Ns.int_.uri.<=(?))(uri2).get.toSeq.sorted === List(uri1, uri2)
      m(Ns.int_.uri.>=(?))(uri2).get.toSeq.sorted === List(uri2, uri3)
      m(Ns.int_.uri.!=(?))(uri2).get.toSeq.sorted === List(uri1, uri3)
      m(Ns.int_.uri.not(?))(uri2).get.toSeq.sorted === List(uri1, uri3)

      m(Ns.int_.enum(?))(enum2).get === List(enum2)
      m(Ns.int_.enum.<(?))(enum2).get === List(enum1)
      m(Ns.int_.enum.>(?))(enum2).get === List(enum3)
      m(Ns.int_.enum.<=(?))(enum2).get.toSeq.sorted === List(enum1, enum2)
      m(Ns.int_.enum.>=(?))(enum2).get.toSeq.sorted === List(enum2, enum3)
      m(Ns.int_.enum.!=(?))(enum2).get.toSeq.sorted === List(enum1, enum3)
      m(Ns.int_.enum.not(?))(enum2).get.toSeq.sorted === List(enum1, enum3)
    }

    "Cardinality-many expressions" in new ManySetup {

      // Retrieving sets of Strings containing a specific value
      //    m(Ns.int_.strs(?))(Set("a")).debug

      // Asking for a card-many value gives us just that value
      m(Ns.int.strs(?))(Set("b")).get === List((1, Set("b")), (2, Set("b")))

      // So we will more likely use the tacet notation and skip the value itself
      m(Ns.int.strs_(?))(Set("b")).get.toSeq.sorted === List(1, 2)

      // If we want the full sets containing the matching value we can't use an
      // input-molecule anymore but will have to map a full result set
      Ns.int.strs.get.filter(_._2.contains("b")) === List((1, Set("a", "b")), (2, Set("b", "c")))


      // Input-molecules with tacet expression

      m(Ns.int.strs_(?))(Set("a")).get.toSeq.sorted === List(1)
      m(Ns.int.strs_(?))(Set("b")).get.toSeq.sorted === List(1, 2)
      m(Ns.int.strs_(?))(Set("c")).get.toSeq.sorted === List(2, 3)
      m(Ns.int.strs_(?))(Set("d")).get.toSeq.sorted === List(3)
      m(Ns.int.strs_.<(?))(Set("b")).get.toSeq.sorted === List(1)
      m(Ns.int.strs_.>(?))(Set("b")).get.toSeq.sorted === List(2, 3)
      m(Ns.int.strs_.<=(?))(Set("b")).get.toSeq.sorted === List(1, 2)
      m(Ns.int.strs_.>=(?))(Set("b")).get.toSeq.sorted === List(1, 2, 3)
      // All sets have some value not being "b"
      m(Ns.int.strs_.!=(?))(Set("b")).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.strs_.not(?))(Set("b")).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.long.ints_(?))(Set(1)).get.toSeq.sorted === List(1L)
      m(Ns.long.ints_(?))(Set(2)).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.ints_(?))(Set(3)).get.toSeq.sorted === List(2L, 3L)
      m(Ns.long.ints_(?))(Set(4)).get.toSeq.sorted === List(3L)
      m(Ns.long.ints_.<(?))(Set(2)).get.toSeq.sorted === List(1L)
      m(Ns.long.ints_.>(?))(Set(2)).get.toSeq.sorted === List(2L, 3L)
      m(Ns.long.ints_.<=(?))(Set(2)).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.ints_.>=(?))(Set(2)).get.toSeq.sorted === List(1L, 2L, 3L)
      m(Ns.long.ints_.!=(?))(Set(2)).get.toSeq.sorted === List(1L, 2L, 3L)
      m(Ns.long.ints_.not(?))(Set(2)).get.toSeq.sorted === List(1L, 2L, 3L)

      m(Ns.int.floats_(?))(Set(1.0f)).get.toSeq.sorted === List(1)
      m(Ns.int.floats_(?))(Set(2.0f)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.floats_(?))(Set(3.0f)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.floats_(?))(Set(4.0f)).get.toSeq.sorted === List(3)
      m(Ns.int.floats_.<(?))(Set(2.0f)).get.toSeq.sorted === List(1)
      m(Ns.int.floats_.>(?))(Set(2.0f)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.floats_.<=(?))(Set(2.0f)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.floats_.>=(?))(Set(2.0f)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.floats_.!=(?))(Set(2.0f)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.floats_.not(?))(Set(2.0f)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.doubles_(?))(Set(1.0)).get.toSeq.sorted === List(1)
      m(Ns.int.doubles_(?))(Set(2.0)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.doubles_(?))(Set(3.0)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.doubles_(?))(Set(4.0)).get.toSeq.sorted === List(3)
      m(Ns.int.doubles_.<(?))(Set(2.0)).get.toSeq.sorted === List(1)
      m(Ns.int.doubles_.>(?))(Set(2.0)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.doubles_.<=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.doubles_.>=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.doubles_.!=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.doubles_.not(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.doubles_(?))(Set(1.0)).get.toSeq.sorted === List(1)
      m(Ns.int.doubles_(?))(Set(2.0)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.doubles_(?))(Set(3.0)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.doubles_(?))(Set(4.0)).get.toSeq.sorted === List(3)
      m(Ns.int.doubles_.<(?))(Set(2.0)).get.toSeq.sorted === List(1)
      m(Ns.int.doubles_.>(?))(Set(2.0)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.doubles_.<=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.doubles_.>=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.doubles_.!=(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.doubles_.not(?))(Set(2.0)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.bools_(?))(Set(true)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.bools_.<(?))(Set(true)).get.toSeq.sorted === List(0, 2)
      m(Ns.int.bools_.>(?))(Set(true)).get.toSeq.sorted === List()
      m(Ns.int.bools_.<=(?))(Set(true)).get.toSeq.sorted === List(0, 1, 2)
      m(Ns.int.bools_.>=(?))(Set(true)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.bools_.!=(?))(Set(true)).get.toSeq.sorted === List(0, 2)
      m(Ns.int.bools_.not(?))(Set(true)).get.toSeq.sorted === List(0, 2)

      m(Ns.int.bools_(?))(Set(false)).get.toSeq.sorted === List(0, 2)
      m(Ns.int.bools_.<(?))(Set(false)).get.toSeq.sorted === List()
      m(Ns.int.bools_.>(?))(Set(false)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.bools_.<=(?))(Set(false)).get.toSeq.sorted === List(0, 2)
      m(Ns.int.bools_.>=(?))(Set(false)).get.toSeq.sorted === List(0, 1, 2)
      m(Ns.int.bools_.!=(?))(Set(false)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.bools_.not(?))(Set(false)).get.toSeq.sorted === List(1, 2)

      m(Ns.int.dates_(?))(Set(date1)).get.toSeq.sorted === List(1)
      m(Ns.int.dates_(?))(Set(date2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.dates_(?))(Set(date3)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.dates_(?))(Set(date4)).get.toSeq.sorted === List(3)
      m(Ns.int.dates_.<(?))(Set(date2)).get.toSeq.sorted === List(1)
      m(Ns.int.dates_.>(?))(Set(date2)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.dates_.<=(?))(Set(date2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.dates_.>=(?))(Set(date2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.dates_.!=(?))(Set(date2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.dates_.not(?))(Set(date2)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.uuids_(?))(Set(uuid1)).get.toSeq.sorted === List(1)
      m(Ns.int.uuids_(?))(Set(uuid2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uuids_(?))(Set(uuid3)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uuids_(?))(Set(uuid4)).get.toSeq.sorted === List(3)
      m(Ns.int.uuids_.<(?))(Set(uuid2)).get.toSeq.sorted === List(1)
      m(Ns.int.uuids_.>(?))(Set(uuid2)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uuids_.<=(?))(Set(uuid2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uuids_.>=(?))(Set(uuid2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.uuids_.!=(?))(Set(uuid2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.uuids_.not(?))(Set(uuid2)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.uris_(?))(Set(uri1)).get.toSeq.sorted === List(1)
      m(Ns.int.uris_(?))(Set(uri2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uris_(?))(Set(uri3)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uris_(?))(Set(uri4)).get.toSeq.sorted === List(3)
      m(Ns.int.uris_.<(?))(Set(uri2)).get.toSeq.sorted === List(1)
      m(Ns.int.uris_.>(?))(Set(uri2)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.uris_.<=(?))(Set(uri2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uris_.>=(?))(Set(uri2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.uris_.!=(?))(Set(uri2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.uris_.not(?))(Set(uri2)).get.toSeq.sorted === List(1, 2, 3)

      m(Ns.int.enums_(?))(Set(enum1)).get.toSeq.sorted === List(1)
      m(Ns.int.enums_(?))(Set(enum2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.enums_(?))(Set(enum3)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.enums_(?))(Set(enum4)).get.toSeq.sorted === List(3)
      m(Ns.int.enums_.<(?))(Set(enum2)).get.toSeq.sorted === List(1)
      m(Ns.int.enums_.>(?))(Set(enum2)).get.toSeq.sorted === List(2, 3)
      m(Ns.int.enums_.<=(?))(Set(enum2)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.enums_.>=(?))(Set(enum2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.enums_.!=(?))(Set(enum2)).get.toSeq.sorted === List(1, 2, 3)
      m(Ns.int.enums_.not(?))(Set(enum2)).get.toSeq.sorted === List(1, 2, 3)
    }

    "OR-logic" in new OneSetup {
      m(Ns.int.str_(?))("a" or "b").get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_(?))("a", "b").get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_(?))(Seq("a", "b")).get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_(?))(str1 or str2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_(?))(str1, str2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.str_(?))(Seq(str1, str2)).get.toSeq.sorted === List(1, 2)
      val ab = Seq(str1, str2)
      m(Ns.int.str_(?))(ab).get.toSeq.sorted === List(1, 2)

      // Order of input of no importance
      m(Ns.int.str_(?))("b", "a").get.toSeq.sorted === List(1, 2)

      m(Ns.long.int_(?))(1 or 2).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(1, 2).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(Seq(1, 2)).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(int1 or int2).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(int1, int2).get.toSeq.sorted === List(1L, 2L)
      m(Ns.long.int_(?))(Seq(int1, int2)).get.toSeq.sorted === List(1L, 2L)
      val intList = Seq(int1, int2)
      m(Ns.long.int_(?))(intList).get.toSeq.sorted === List(1L, 2L)

      //      m(Ns.int.long_(?))(1L or 2L).debug
      m(Ns.int.long_(?))(1L or 2L).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_(?))(1L, 2L).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_(?))(Seq(1L, 2L)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_(?))(long1 or long2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_(?))(long1, long2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.long_(?))(Seq(long1, long2)).get.toSeq.sorted === List(1, 2)
      val longList = Seq(long1, long2)
      m(Ns.int.long_(?))(longList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.float_(?))(1.0f or 2.0f).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_(?))(1.0f, 2.0f).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_(?))(Seq(1.0f, 2.0f)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_(?))(float1 or float2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_(?))(float1, float2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.float_(?))(Seq(float1, float2)).get.toSeq.sorted === List(1, 2)
      val floatList = Seq(float1, float2)
      m(Ns.int.float_(?))(floatList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.double_(?))(1.0 or 2.0).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_(?))(1.0, 2.0).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_(?))(Seq(1.0, 2.0)).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_(?))(double1 or double2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_(?))(double1, double2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.double_(?))(Seq(double1, double2)).get.toSeq.sorted === List(1, 2)
      val doubleList = Seq(double1, double2)
      m(Ns.int.double_(?))(doubleList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.bool_(?))(true or false).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_(?))(true, false).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_(?))(Seq(true, false)).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_(?))(bool0 or bool1).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_(?))(bool0, bool1).get.toSeq.sorted === List(0, 1)
      m(Ns.int.bool_(?))(Seq(bool0, bool1)).get.toSeq.sorted === List(0, 1)
      val boolList = Seq(bool0, bool1)
      m(Ns.int.bool_(?))(boolList).get.toSeq.sorted === List(0, 1)

      m(Ns.int.date_(?))(date1 or date2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.date_(?))(date1, date2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.date_(?))(Seq(date1, date2)).get.toSeq.sorted === List(1, 2)
      val dateList = Seq(date1, date2)
      m(Ns.int.date_(?))(dateList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.uuid_(?))(uuid1 or uuid2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uuid_(?))(uuid1, uuid2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uuid_(?))(Seq(uuid1, uuid2)).get.toSeq.sorted === List(1, 2)
      val uuidList = Seq(uuid1, uuid2)
      m(Ns.int.uuid_(?))(uuidList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.uri_(?))(uri1 or uri2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uri_(?))(uri1, uri2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.uri_(?))(Seq(uri1, uri2)).get.toSeq.sorted === List(1, 2)
      val uriList = Seq(uri1, uri2)
      m(Ns.int.uri_(?))(uriList).get.toSeq.sorted === List(1, 2)

      m(Ns.int.enum_(?))(enum1 or enum2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.enum_(?))(enum1, enum2).get.toSeq.sorted === List(1, 2)
      m(Ns.int.enum_(?))(Seq(enum1, enum2)).get.toSeq.sorted === List(1, 2)
      val enumList = Seq(enum1, enum2)
      m(Ns.int.enum_(?))(enumList).get.toSeq.sorted === List(1, 2)
    }

    "Fulltext search" in new CoreSetup {
      Ns.str insert List("The quick fox jumps", "Ten slow monkeys")
      m(Ns.str.contains(?))("jumps").get === List("The quick fox jumps")
    }
  }

  "2 input parameters" >> {
    // todo: also AND semantics of supplying tuples of values
    ok
  }

  "3 input parameters" >> {
    // todo
    ok
  }
}