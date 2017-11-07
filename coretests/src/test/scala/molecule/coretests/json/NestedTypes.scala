package molecule.coretests.json

import molecule.Imports._
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
        |{"str": "a", "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.int.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 1, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.long.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"long": 1, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.float.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"float": 1.0, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.double.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"double": 1.0, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.bool.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bool": true, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.date.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"date": "1970-01-01T01:00:01.000+01:00", "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uuid.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uuid": "$uuid1", "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uri.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uri": "$uri1", "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.enum.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"enum": "enum1", "refs1": [
        |   {"int1": 1}]}
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
        |{"strs": ["", "a", "b"], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.ints.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"ints": [1, 2], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.longs.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"longs": [1, 2], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    // (Order is not guaranteed in Sets)
    Ns.floats.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"floats": [2.0, 1.0], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.doubles.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"doubles": [2.0, 1.0], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.bools.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bools": [true, false], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.dates.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"], "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uuids.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uuids": ["$uuid1"], "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uris.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uris": ["$uri1", "$uri2"], "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.enums.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"enums": ["$enum1", "$enum2"], "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.bigInts.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bigInts": [1, 2], "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.bigDecs.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bigDecs": [2.0, 1.0], "refs1": [
        |   {"int1": 1}]}
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
        |{"strMap": {"b":"B", "a":"A"}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.intMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"intMap": {"b":2, "a":1}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.longMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"longMap": {"b":2, "a":1}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.floatMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"floatMap": {"b":2.0, "a":1.0}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.doubleMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"doubleMap": {"b":2.0, "a":1.0}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.boolMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"boolMap": {"b":false, "a":true}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.dateMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"dateMap": {"b":"1970-01-01T01:00:02.000+01:00", "a":"1970-01-01T01:00:01.000+01:00"}, "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uuidMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uuidMap": {"a":"$uuid1"}, "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.uriMap.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"uriMap": {"b":"$uri2", "a":"$uri1"}, "refs1": [
         |   {"int1": 1}]}
         |]""".stripMargin

    Ns.bigIntMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bigIntMap": {"b":2, "a":1}, "refs1": [
        |   {"int1": 1}]}
        |]""".stripMargin

    Ns.bigDecMap.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"bigDecMap": {"b":2.0, "a":1.0}, "refs1": [
        |   {"int1": 1}]}
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
        |{"int": 1, "str": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 1, "str": "", "refs1": [
        |   {"int1": 2}]},
        |{"int": 1, "str": "c", "refs1": [
        |   {"int1": 3}]}
        |]""".stripMargin

    Ns.long(2L).int$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"long": 2, "int": null, "refs1": [
        |   {"int1": 1}]},
        |{"long": 2, "int": 2, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(3).long$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 3, "long": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 3, "long": 20, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(4).float$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 4, "float": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 4, "float": 2.0, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(5).double$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 5, "double": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 5, "double": 2.0, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(6).bool$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 6, "bool": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 6, "bool": true, "refs1": [
        |   {"int1": 2}]},
        |{"int": 6, "bool": false, "refs1": [
        |   {"int1": 3}]}
        |]""".stripMargin

    Ns.int(7).date$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 7, "date": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 7, "date": "1970-01-01T01:00:02.000+01:00", "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuid$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 8, "uuid": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 8, "uuid": "$uuid2", "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uri$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 9, "uri": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 9, "uri": "$uri2", "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(10).enum$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 10, "enum": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 10, "enum": "$enum2", "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigInt$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 11, "bigInt": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 11, "bigInt": $bigInt2, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDec$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 12, "bigDec": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 12, "bigDec": $bigDec2, "refs1": [
         |   {"int1": 2}]}
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
        |{"int": 1, "strs": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 1, "strs": ["", "b"], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.long(2L).ints$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"long": 2, "ints": null, "refs1": [
        |   {"int1": 1}]},
        |{"long": 2, "ints": [1, 2], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(3).longs$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 3, "longs": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 3, "longs": [21, 22], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(4).floats$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 4, "floats": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 4, "floats": [1.0, 2.0], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(5).doubles$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 5, "doubles": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 5, "doubles": [1.0, 2.0], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    // OBS!: Sets of booleans truncate to one value!
    Ns.int(6).bools$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 6, "bools": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 6, "bools": [true], "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(7).dates$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 7, "dates": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 7, "dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"], "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuids$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 8, "uuids": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 8, "uuids": ["$uuid1", "$uuid2"], "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uris$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 9, "uris": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 9, "uris": ["$uri1", "$uri2"], "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(10).enums$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 10, "enums": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 10, "enums": ["$enum1", "$enum2"], "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigInts$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 11, "bigInts": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 11, "bigInts": [$bigInt1, $bigInt2], "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDecs$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 12, "bigDecs": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 12, "bigDecs": [$bigDec1, $bigDec2], "refs1": [
         |   {"int1": 2}]}
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
        |{"int": 1, "strMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 1, "strMap": {"a":"A", "b":"B"}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.long(2L).intMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"long": 2, "intMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"long": 2, "intMap": {"a":1, "b":2}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(3).longMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 3, "longMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 3, "longMap": {"a":1, "b":2}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(4).floatMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 4, "floatMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 4, "floatMap": {"a":1.0, "b":2.0}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(5).doubleMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 5, "doubleMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 5, "doubleMap": {"a":1.0, "b":2.0}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(6).boolMap$.Refs1.*(Ref1.int1).getJson ===
      """[
        |{"int": 6, "boolMap": null, "refs1": [
        |   {"int1": 1}]},
        |{"int": 6, "boolMap": {"a":true, "b":false}, "refs1": [
        |   {"int1": 2}]}
        |]""".stripMargin

    Ns.int(7).dateMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 7, "dateMap": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 7, "dateMap": {"a":"1970-01-01T01:00:01.000+01:00", "b":"1970-01-01T01:00:02.000+01:00"}, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(8).uuidMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 8, "uuidMap": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 8, "uuidMap": {"a":"$uuid1", "b":"$uuid2"}, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(9).uriMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 9, "uriMap": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 9, "uriMap": {"a":"$uri1", "b":"$uri2"}, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(11).bigIntMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 11, "bigIntMap": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 11, "bigIntMap": {"a":$bigInt1, "b":$bigInt2}, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin

    Ns.int(12).bigDecMap$.Refs1.*(Ref1.int1).getJson ===
      s"""[
         |{"int": 12, "bigDecMap": null, "refs1": [
         |   {"int1": 1}]},
         |{"int": 12, "bigDecMap": {"a":$bigDec1, "b":$bigDec2}, "refs1": [
         |   {"int1": 2}]}
         |]""".stripMargin
  }

  // Todo: Aggregate types (Vector, Stream)
}
