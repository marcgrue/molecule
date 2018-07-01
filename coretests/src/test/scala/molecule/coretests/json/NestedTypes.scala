package molecule.coretests.json

import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}


class NestedTypes extends CoreSpec {


  "value" in new CoreSetup {

    Ns.str.Refs1.*(Ref1.int1).insert(str1, Seq(1))
    Ns.int.Refs1.*(Ref1.int1).insert(1, Seq(1))
    Ns.long.Refs1.*(Ref1.int1).insert(1L, Seq(1))
    Ns.float.Refs1.*(Ref1.int1).insert(1.0f, Seq(1))
    Ns.double.Refs1.*(Ref1.int1).insert(1.0, Seq(1))
    Ns.bool.Refs1.*(Ref1.int1).insert(true, Seq(1))
    Ns.date.Refs1.*(Ref1.int1).insert(date1, Seq(1))
    Ns.uuid.Refs1.*(Ref1.int1).insert(uuid1, Seq(1))
    Ns.uri.Refs1.*(Ref1.int1).insert(uri1, Seq(1))
    Ns.enum.Refs1.*(Ref1.int1).insert(enum1, Seq(1))

    Ns.str.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.str": "a", "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.int.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 1, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.long.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.long": 1, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.float.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.float": 1.0, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.double.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.double": 1.0, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.bool.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bool": true, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.date.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.date": "1970-01-01T01:00:01.000+01:00", "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uuid.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uuid": "$uuid1", "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uri.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uri": "$uri1", "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.enum.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.enum": "enum1", "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin
  }


  "set" in new CoreSetup {

    Ns.strs.Refs1.*(Ref1.int1).insert(Set("", "a", "b"), Seq(1))
    Ns.ints.Refs1.*(Ref1.int1).insert(Set(1, 2), Seq(1))
    Ns.longs.Refs1.*(Ref1.int1).insert(Set(1L, 2L), Seq(1))
    Ns.floats.Refs1.*(Ref1.int1).insert(Set(1.0f, 2.0f), Seq(1))
    Ns.doubles.Refs1.*(Ref1.int1).insert(Set(1.0, 2.0), Seq(1))
    Ns.bools.Refs1.*(Ref1.int1).insert(Set(true, false), Seq(1))
    Ns.dates.Refs1.*(Ref1.int1).insert(Set(date1, date2), Seq(1))
    Ns.uuids.Refs1.*(Ref1.int1).insert(Set(uuid1), Seq(1))
    Ns.uris.Refs1.*(Ref1.int1).insert(Set(uri1, uri2), Seq(1))
    Ns.enums.Refs1.*(Ref1.int1).insert(Set(enum1, enum2), Seq(1))
    Ns.bigInts.Refs1.*(Ref1.int1).insert(Set(bigInt1, bigInt2), Seq(1))
    Ns.bigDecs.Refs1.*(Ref1.int1).insert(Set(bigDec1, bigDec2), Seq(1))

    Ns.strs.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.strs": ["", "a", "b"], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.ints.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.ints": [1, 2], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.longs.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.longs": [1, 2], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    // (Order is not guaranteed in Sets)
    Ns.floats.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.floats": [2.0, 1.0], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.doubles.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.doubles": [2.0, 1.0], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.bools.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bools": [true, false], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.dates.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"], "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uuids.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uuids": ["$uuid1"], "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uris.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uris": ["$uri1", "$uri2"], "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.enums.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.enums": ["$enum1", "$enum2"], "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.bigInts.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bigInts": [1, 2], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.bigDecs.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bigDecs": [2.0, 1.0], "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin
  }


  "map" in new CoreSetup {

    Ns.strMap.Refs1.*(Ref1.int1).insert(Map("a" -> "A", "b" -> "B"), Seq(1))
    Ns.intMap.Refs1.*(Ref1.int1).insert(Map("a" -> 1, "b" -> 2), Seq(1))
    Ns.longMap.Refs1.*(Ref1.int1).insert(Map("a" -> 1L, "b" -> 2L), Seq(1))
    Ns.floatMap.Refs1.*(Ref1.int1).insert(Map("a" -> 1.0f, "b" -> 2.0f), Seq(1))
    Ns.doubleMap.Refs1.*(Ref1.int1).insert(Map("a" -> 1.0, "b" -> 2.0), Seq(1))
    Ns.boolMap.Refs1.*(Ref1.int1).insert(Map("a" -> true, "b" -> false), Seq(1))
    Ns.dateMap.Refs1.*(Ref1.int1).insert(Map("a" -> date1, "b" -> date2), Seq(1))
    Ns.uuidMap.Refs1.*(Ref1.int1).insert(Map("a" -> uuid1), Seq(1))
    Ns.uriMap.Refs1.*(Ref1.int1).insert(Map("a" -> uri1, "b" -> uri2), Seq(1))
    Ns.bigIntMap.Refs1.*(Ref1.int1).insert(Map("a" -> bigInt1, "b" -> bigInt2), Seq(1))
    Ns.bigDecMap.Refs1.*(Ref1.int1).insert(Map("a" -> bigDec1, "b" -> bigDec2), Seq(1))

    Ns.strMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.strMap": {"b":"B", "a":"A"}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.intMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.intMap": {"b":2, "a":1}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.longMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.longMap": {"b":2, "a":1}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.floatMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.floatMap": {"b":2.0, "a":1.0}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.doubleMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.doubleMap": {"b":2.0, "a":1.0}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.boolMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.boolMap": {"b":false, "a":true}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.dateMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.dateMap": {"b":"1970-01-01T01:00:02.000+01:00", "a":"1970-01-01T01:00:01.000+01:00"}, "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uuidMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uuidMap": {"a":"$uuid1"}, "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.uriMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.uriMap": {"b":"$uri2", "a":"$uri1"}, "ns.refs1": [
         |   {"ref1.int1": 1}]}
         |]""".stripMargin

    Ns.bigIntMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bigIntMap": {"b":2, "a":1}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin

    Ns.bigDecMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.bigDecMap": {"b":2.0, "a":1.0}, "ns.refs1": [
        |   {"ref1.int1": 1}]}
        |]""".stripMargin
  }


  "Optional value" in new CoreSetup {

    Ns.int.str$.Refs1.*(Ref1.int1).insert(List((1, None, Seq(1)), (1, Some(""), Seq(2)), (1, Some("c"), Seq(3))))
    Ns.long.int$.Refs1.*(Ref1.int1).insert(List((2, None, Seq(1)), (2, Some(2), Seq(2))))
    Ns.int.long$.Refs1.*(Ref1.int1).insert(List((3, None, Seq(1)), (3, Some(20L), Seq(2))))
    Ns.int.float$.Refs1.*(Ref1.int1).insert(List((4, None, Seq(1)), (4, Some(2.0f), Seq(2))))
    Ns.int.double$.Refs1.*(Ref1.int1).insert(List((5, None, Seq(1)), (5, Some(2.0), Seq(2))))
    Ns.int.bool$.Refs1.*(Ref1.int1).insert(List((6, None, Seq(1)), (6, Some(true), Seq(2)), (6, Some(false), Seq(3))))
    Ns.int.date$.Refs1.*(Ref1.int1).insert(List((7, None, Seq(1)), (7, Some(date2), Seq(2))))
    Ns.int.uuid$.Refs1.*(Ref1.int1).insert(List((8, None, Seq(1)), (8, Some(uuid2), Seq(2))))
    Ns.int.uri$.Refs1.*(Ref1.int1).insert(List((9, None, Seq(1)), (9, Some(uri2), Seq(2))))
    Ns.int.enum$.Refs1.*(Ref1.int1).insert(List((10, None, Seq(1)), (10, Some(enum2), Seq(2))))
    Ns.int.bigInt$.Refs1.*(Ref1.int1).insert(List((11, None, Seq(1)), (11, Some(bigInt2), Seq(2))))
    Ns.int.bigDec$.Refs1.*(Ref1.int1).insert(List((12, None, Seq(1)), (12, Some(bigDec2), Seq(2))))

    Ns.int(1).str$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 1, "ns.str": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 1, "ns.str": "", "ns.refs1": [
        |   {"ref1.int1": 2}]},
        |{"ns.int": 1, "ns.str": "c", "ns.refs1": [
        |   {"ref1.int1": 3}]}
        |]""".stripMargin

    Ns.long(2L).int$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.long": 2, "ns.int": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.long": 2, "ns.int": 2, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(3).long$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 3, "ns.long": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 3, "ns.long": 20, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(4).float$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 4, "ns.float": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 4, "ns.float": 2.0, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(5).double$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 5, "ns.double": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 5, "ns.double": 2.0, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(6).bool$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 6, "ns.bool": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 6, "ns.bool": true, "ns.refs1": [
        |   {"ref1.int1": 2}]},
        |{"ns.int": 6, "ns.bool": false, "ns.refs1": [
        |   {"ref1.int1": 3}]}
        |]""".stripMargin

    Ns.int(7).date$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 7, "ns.date": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 7, "ns.date": "1970-01-01T01:00:02.000+01:00", "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuid$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuid": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 8, "ns.uuid": "$uuid2", "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uri$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 9, "ns.uri": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 9, "ns.uri": "$uri2", "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(10).enum$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 10, "ns.enum": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 10, "ns.enum": "$enum2", "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigInt$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigInt": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 11, "ns.bigInt": $bigInt2, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDec$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDec": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 12, "ns.bigDec": $bigDec2, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin
  }


  "Optional set" in new CoreSetup {

    Ns.int.strs$.Refs1.*(Ref1.int1).insert(List((1, None, Seq(1)), (1, Some(Set("", "b")), Seq(2))))
    Ns.long.ints$.Refs1.*(Ref1.int1).insert(List((2, None, Seq(1)), (2, Some(Set(1, 2)), Seq(2))))
    Ns.int.longs$.Refs1.*(Ref1.int1).insert(List((3, None, Seq(1)), (3, Some(Set(21L, 22L)), Seq(2))))
    Ns.int.floats$.Refs1.*(Ref1.int1).insert(List((4, None, Seq(1)), (4, Some(Set(1.0f, 2.0f)), Seq(2))))
    Ns.int.doubles$.Refs1.*(Ref1.int1).insert(List((5, None, Seq(1)), (5, Some(Set(1.0, 2.0)), Seq(2))))
    Ns.int.bools$.Refs1.*(Ref1.int1).insert(List((6, None, Seq(1)), (6, Some(Set(true, false)), Seq(2))))
    Ns.int.dates$.Refs1.*(Ref1.int1).insert(List((7, None, Seq(1)), (7, Some(Set(date1, date2)), Seq(2))))
    Ns.int.uuids$.Refs1.*(Ref1.int1).insert(List((8, None, Seq(1)), (8, Some(Set(uuid1, uuid2)), Seq(2))))
    Ns.int.uris$.Refs1.*(Ref1.int1).insert(List((9, None, Seq(1)), (9, Some(Set(uri1, uri2)), Seq(2))))
    Ns.int.enums$.Refs1.*(Ref1.int1).insert(List((10, None, Seq(1)), (10, Some(Set(enum1, enum2)), Seq(2))))
    Ns.int.bigInts$.Refs1.*(Ref1.int1).insert(List((11, None, Seq(1)), (11, Some(Set(bigInt1, bigInt2)), Seq(2))))
    Ns.int.bigDecs$.Refs1.*(Ref1.int1).insert(List((12, None, Seq(1)), (12, Some(Set(bigDec1, bigDec2)), Seq(2))))

    Ns.int(1).strs$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 1, "ns.strs": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 1, "ns.strs": ["", "b"], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.long(2L).ints$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.long": 2, "ns.ints": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.long": 2, "ns.ints": [1, 2], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(3).longs$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 3, "ns.longs": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 3, "ns.longs": [21, 22], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(4).floats$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 4, "ns.floats": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 4, "ns.floats": [1.0, 2.0], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(5).doubles$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 5, "ns.doubles": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 5, "ns.doubles": [1.0, 2.0], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    // OBS!: Sets of booleans truncate to one value!
    Ns.int(6).bools$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 6, "ns.bools": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 6, "ns.bools": [true], "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(7).dates$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 7, "ns.dates": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 7, "ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuids$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuids": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 8, "ns.uuids": ["$uuid1", "$uuid2"], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uris$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 9, "ns.uris": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 9, "ns.uris": ["$uri1", "$uri2"], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(10).enums$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 10, "ns.enums": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 10, "ns.enums": ["$enum1", "$enum2"], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigInts$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigInts": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 11, "ns.bigInts": [$bigInt1, $bigInt2], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDecs$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDecs": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 12, "ns.bigDecs": [$bigDec1, $bigDec2], "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin
  }


  "Optional map" in new CoreSetup {

    Ns.int.strMap$.Refs1.*(Ref1.int1).insert(List((1, None, Seq(1)), (1, Some(Map("a" -> "A", "b" -> "B")), Seq(2))))
    Ns.long.intMap$.Refs1.*(Ref1.int1).insert(List((2, None, Seq(1)), (2, Some(Map("a" -> 1, "b" -> 2)), Seq(2))))
    Ns.int.longMap$.Refs1.*(Ref1.int1).insert(List((3, None, Seq(1)), (3, Some(Map("a" -> 1L, "b" -> 2L)), Seq(2))))
    Ns.int.floatMap$.Refs1.*(Ref1.int1).insert(List((4, None, Seq(1)), (4, Some(Map("a" -> 1.0f, "b" -> 2.0f)), Seq(2))))
    Ns.int.doubleMap$.Refs1.*(Ref1.int1).insert(List((5, None, Seq(1)), (5, Some(Map("a" -> 1.0, "b" -> 2.0)), Seq(2))))
    Ns.int.boolMap$.Refs1.*(Ref1.int1).insert(List((6, None, Seq(1)), (6, Some(Map("a" -> true, "b" -> false)), Seq(2))))
    Ns.int.dateMap$.Refs1.*(Ref1.int1).insert(List((7, None, Seq(1)), (7, Some(Map("a" -> date1, "b" -> date2)), Seq(2))))
    Ns.int.uuidMap$.Refs1.*(Ref1.int1).insert(List((8, None, Seq(1)), (8, Some(Map("a" -> uuid1, "b" -> uuid2)), Seq(2))))
    Ns.int.uriMap$.Refs1.*(Ref1.int1).insert(List((9, None, Seq(1)), (9, Some(Map("a" -> uri1, "b" -> uri2)), Seq(2))))
    Ns.int.bigIntMap$.Refs1.*(Ref1.int1).insert(List((11, None, Seq(1)), (11, Some(Map("a" -> bigInt1, "b" -> bigInt2)), Seq(2))))
    Ns.int.bigDecMap$.Refs1.*(Ref1.int1).insert(List((12, None, Seq(1)), (12, Some(Map("a" -> bigDec1, "b" -> bigDec2)), Seq(2))))

    Ns.int.apply(1).strMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 1, "ns.strMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 1, "ns.strMap": {"a":"A", "b":"B"}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.long(2L).intMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.long": 2, "ns.intMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.long": 2, "ns.intMap": {"a":1, "b":2}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(3).longMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 3, "ns.longMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 3, "ns.longMap": {"a":1, "b":2}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(4).floatMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 4, "ns.floatMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 4, "ns.floatMap": {"a":1.0, "b":2.0}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(5).doubleMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 5, "ns.doubleMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 5, "ns.doubleMap": {"a":1.0, "b":2.0}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(6).boolMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ns.int": 6, "ns.boolMap": null, "ns.refs1": [
        |   {"ref1.int1": 1}]},
        |{"ns.int": 6, "ns.boolMap": {"a":true, "b":false}, "ns.refs1": [
        |   {"ref1.int1": 2}]}
        |]""".stripMargin

    Ns.int(7).dateMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 7, "ns.dateMap": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 7, "ns.dateMap": {"a":"1970-01-01T01:00:01.000+01:00", "b":"1970-01-01T01:00:02.000+01:00"}, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuidMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuidMap": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 8, "ns.uuidMap": {"a":"$uuid1", "b":"$uuid2"}, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uriMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 9, "ns.uriMap": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 9, "ns.uriMap": {"a":"$uri1", "b":"$uri2"}, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigIntMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigIntMap": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 11, "ns.bigIntMap": {"a":$bigInt1, "b":$bigInt2}, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDecMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDecMap": null, "ns.refs1": [
         |   {"ref1.int1": 1}]},
         |{"ns.int": 12, "ns.bigDecMap": {"a":$bigDec1, "b":$bigDec2}, "ns.refs1": [
         |   {"ref1.int1": 2}]}
         |]""".stripMargin
  }
}
