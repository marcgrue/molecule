//package molecule.coretests.json
//
//import molecule.Json._
//import molecule.coretests.util.dsl.coreTest._
//import molecule.coretests.util.{CoreSetup, CoreSpec}
//
//
//class Attributes extends CoreSpec {
//
//
//  "Without implicit json getter " in new CoreSetup {
//
//    Ns.int.str.insert(1, "a")
//
//    // Missing implicit getJson marker
//    (Ns.int.str.getJson must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
//      "Please add an `implicit val useJson = JsonGetter` in scope to use `getJson`."
//  }
//
//
//  "value" in new CoreSetup {
//
//    // Insert single value for one cardinality-1 attribute
//    Ns.str insert str1
//    Ns.int insert 1
//    Ns.long insert 1L
//    Ns.float insert 1.0f
//    Ns.double insert 1.0
//    Ns.bool insert true
//    Ns.date insert date1
//    Ns.uuid insert uuid1
//    Ns.uri insert uri1
//    Ns.enum insert enum1
//
//    Ns.str.getJson ===
//      """[
//        |{"str": "a"}
//        |]""".stripMargin
//
//    Ns.int.getJson ===
//      """[
//        |{"int": 1}
//        |]""".stripMargin
//
//    Ns.long.getJson ===
//      """[
//        |{"long": 1}
//        |]""".stripMargin
//
//    Ns.float.getJson ===
//      """[
//        |{"float": 1.0}
//        |]""".stripMargin
//
//    Ns.double.getJson ===
//      """[
//        |{"double": 1.0}
//        |]""".stripMargin
//
//    Ns.bool.getJson ===
//      """[
//        |{"bool": true}
//        |]""".stripMargin
//
//    Ns.date.getJson ===
//      s"""[
//         |{"date": "1970-01-01T01:00:01.000+01:00"}
//         |]""".stripMargin
//
//    Ns.uuid.getJson ===
//      s"""[
//         |{"uuid": "$uuid1"}
//         |]""".stripMargin
//
//    Ns.uri.
//      getJson ===
//      s"""[
//         |{"uri": "$uri1"}
//         |]""".stripMargin
//
//    Ns.enum.getJson ===
//      """[
//        |{"enum": "enum1"}
//        |]""".stripMargin
//  }
//
//
//  "set" in new CoreSetup {
//
//    Ns.strs insert Set("", "a", "b")
//    Ns.ints insert Set(1, 2)
//    Ns.longs insert Set(1L, 2L)
//    Ns.floats insert Set(1.0f, 2.0f)
//    Ns.doubles insert Set(1.0, 2.0)
//    Ns.bools insert Set(true, false)
//    Ns.dates insert Set(date1, date2)
//    Ns.uuids insert Set(uuid1)
//    Ns.uris insert Set(uri1, uri2)
//    Ns.enums insert Set(enum1, enum2)
//    Ns.bigInts insert Set(bigInt1, bigInt2)
//    Ns.bigDecs insert Set(bigDec1, bigDec2)
//
//    Ns.strs.getJson ===
//      """[
//        |{"strs": ["","a","b"]}
//        |]""".stripMargin
//
//    Ns.ints.getJson ===
//      """[
//        |{"ints": [1,2]}
//        |]""".stripMargin
//
//    Ns.longs.getJson ===
//      """[
//        |{"longs": [1,2]}
//        |]""".stripMargin
//
//    // (Order is not guaranteed in Sets)
//    Ns.floats.getJson ===
//      """[
//        |{"floats": [2.0,1.0]}
//        |]""".stripMargin
//
//    Ns.doubles.getJson ===
//      """[
//        |{"doubles": [2.0,1.0]}
//        |]""".stripMargin
//
//    Ns.bools.getJson ===
//      """[
//        |{"bools": [true,false]}
//        |]""".stripMargin
//
//    Ns.dates.getJson ===
//      s"""[
//         |{"dates": ["1970-01-01T01:00:01.000+01:00","1970-01-01T01:00:02.000+01:00"]}
//         |]""".stripMargin
//
//    Ns.uuids.getJson ===
//      s"""[
//         |{"uuids": ["$uuid1"]}
//         |]""".stripMargin
//
//    Ns.uris.getJson ===
//      s"""[
//         |{"uris": ["$uri1","$uri2"]}
//         |]""".stripMargin
//
//    Ns.enums.getJson ===
//      s"""[
//         |{"enums": ["$enum1","$enum2"]}
//         |]""".stripMargin
//
//    Ns.bigInts.getJson ===
//      """[
//        |{"bigInts": [1,2]}
//        |]""".stripMargin
//
//    Ns.bigDecs.getJson ===
//      """[
//        |{"bigDecs": [2.0,1.0]}
//        |]""".stripMargin
//  }
//
//
//  "map" in new CoreSetup {
//
//    Ns.strMap insert Map("a" -> "A", "b" -> "B")
//    Ns.intMap insert Map("a" -> 1, "b" -> 2)
//    Ns.longMap insert Map("a" -> 1L, "b" -> 2L)
//    Ns.floatMap insert Map("a" -> 1.0f, "b" -> 2.0f)
//    Ns.doubleMap insert Map("a" -> 1.0, "b" -> 2.0)
//    Ns.boolMap insert Map("a" -> true, "b" -> false)
//    Ns.dateMap insert Map("a" -> date1, "b" -> date2)
//    Ns.uuidMap insert Map("a" -> uuid1)
//    Ns.uriMap insert Map("a" -> uri1, "b" -> uri2)
//    Ns.bigIntMap insert Map("a" -> bigInt1, "b" -> bigInt2)
//    Ns.bigDecMap insert Map("a" -> bigDec1, "b" -> bigDec2)
//
//    Ns.strMap.getJson ===
//      """[
//        |{"strMap": {"b":"B","a":"A"}}
//        |]""".stripMargin
//
//    Ns.intMap.getJson ===
//      """[
//        |{"intMap": {"b":2,"a":1}}
//        |]""".stripMargin
//
//    Ns.longMap.getJson ===
//      """[
//        |{"longMap": {"b":2,"a":1}}
//        |]""".stripMargin
//
//    Ns.floatMap.getJson ===
//      """[
//        |{"floatMap": {"b":2.0,"a":1.0}}
//        |]""".stripMargin
//
//    Ns.doubleMap.getJson ===
//      """[
//        |{"doubleMap": {"b":2.0,"a":1.0}}
//        |]""".stripMargin
//
//    Ns.boolMap.getJson ===
//      """[
//        |{"boolMap": {"b":false,"a":true}}
//        |]""".stripMargin
//
//    Ns.dateMap.getJson ===
//      s"""[
//         |{"dateMap": {"b":"1970-01-01T01:00:02.000+01:00","a":"1970-01-01T01:00:01.000+01:00"}}
//         |]""".stripMargin
//
//    Ns.uuidMap.getJson ===
//      s"""[
//         |{"uuidMap": {"a":"$uuid1"}}
//         |]""".stripMargin
//
//    Ns.uriMap.getJson ===
//      s"""[
//         |{"uriMap": {"b":"$uri2","a":"$uri1"}}
//         |]""".stripMargin
//
//    Ns.bigIntMap.getJson ===
//      """[
//        |{"bigIntMap": {"b":2,"a":1}}
//        |]""".stripMargin
//
//    Ns.bigDecMap.getJson ===
//      """[
//        |{"bigDecMap": {"b":2.0,"a":1.0}}
//        |]""".stripMargin
//  }
//
//
//  "Optional value" in new CoreSetup {
//
//    Ns.int.str$ insert List((1, None), (1, Some("")), (1, Some("c")))
//    Ns.long.int$ insert List((2, None), (2, Some(2)))
//    Ns.int.long$ insert List((3, None), (3, Some(20L)))
//    Ns.int.float$ insert List((4, None), (4, Some(2.0f)))
//    Ns.int.double$ insert List((5, None), (5, Some(2.0)))
//    Ns.int.bool$ insert List((6, None), (6, Some(true)), (6, Some(false)))
//    Ns.int.date$ insert List((7, None), (7, Some(date2)))
//    Ns.int.uuid$ insert List((8, None), (8, Some(uuid2)))
//    Ns.int.uri$ insert List((9, None), (9, Some(uri2)))
//    Ns.int.enum$ insert List((10, None), (10, Some(enum2)))
//    Ns.int.bigInt$ insert List((11, None), (11, Some(bigInt2)))
//    Ns.int.bigDec$ insert List((12, None), (12, Some(bigDec2)))
//
//    Ns.int(1).str$.getJson ===
//      """[
//        |{"int": 1, "str": null},
//        |{"int": 1, "str": ""},
//        |{"int": 1, "str": "c"}
//        |]""".stripMargin
//
//    //      Ns.long(2L).int$.get foreach println
//
//    Ns.long(2L).int$.getJson ===
//      """[
//        |{"long": 2, "int": null},
//        |{"long": 2, "int": 2}
//        |]""".stripMargin
//
//    Ns.int(3).long$.getJson ===
//      """[
//        |{"int": 3, "long": null},
//        |{"int": 3, "long": 20}
//        |]""".stripMargin
//
//    Ns.int(4).float$.getJson ===
//      """[
//        |{"int": 4, "float": null},
//        |{"int": 4, "float": 2.0}
//        |]""".stripMargin
//
//    Ns.int(5).double$.getJson ===
//      """[
//        |{"int": 5, "double": null},
//        |{"int": 5, "double": 2.0}
//        |]""".stripMargin
//
//    Ns.int(6).bool$.getJson ===
//      """[
//        |{"int": 6, "bool": null},
//        |{"int": 6, "bool": true},
//        |{"int": 6, "bool": false}
//        |]""".stripMargin
//
//    Ns.int(7).date$.getJson ===
//      s"""[
//         |{"int": 7, "date": null},
//         |{"int": 7, "date": "1970-01-01T01:00:02.000+01:00"}
//         |]""".stripMargin
//
//    Ns.int(8).uuid$.getJson ===
//      s"""[
//         |{"int": 8, "uuid": null},
//         |{"int": 8, "uuid": "$uuid2"}
//         |]""".stripMargin
//
//    Ns.int(9).uri$.getJson ===
//      s"""[
//         |{"int": 9, "uri": null},
//         |{"int": 9, "uri": "$uri2"}
//         |]""".stripMargin
//
//    Ns.int(10).enum$.getJson ===
//      s"""[
//         |{"int": 10, "enum": null},
//         |{"int": 10, "enum": "$enum2"}
//         |]""".stripMargin
//
//    Ns.int(11).bigInt$.getJson ===
//      s"""[
//         |{"int": 11, "bigInt": null},
//         |{"int": 11, "bigInt": $bigInt2}
//         |]""".stripMargin
//
//    Ns.int(12).bigDec$.getJson ===
//      s"""[
//         |{"int": 12, "bigDec": null},
//         |{"int": 12, "bigDec": $bigDec2}
//         |]""".stripMargin
//  }
//
//
//  "Optional set" in new CoreSetup {
//
//    Ns.int.strs$ insert List((1, None), (1, Some(Set("", "b"))))
//    Ns.long.ints$ insert List((2, None), (2, Some(Set(1, 2))))
//    Ns.int.longs$ insert List((3, None), (3, Some(Set(21L, 22L))))
//    Ns.int.floats$ insert List((4, None), (4, Some(Set(1.0f, 2.0f))))
//    Ns.int.doubles$ insert List((5, None), (5, Some(Set(1.0, 2.0))))
//    Ns.int.bools$ insert List((6, None), (6, Some(Set(true, false))))
//    Ns.int.dates$ insert List((7, None), (7, Some(Set(date1, date2))))
//    Ns.int.uuids$ insert List((8, None), (8, Some(Set(uuid1, uuid2))))
//    Ns.int.uris$ insert List((9, None), (9, Some(Set(uri1, uri2))))
//    Ns.int.enums$ insert List((10, None), (10, Some(Set(enum1, enum2))))
//    Ns.int.bigInts$ insert List((11, None), (11, Some(Set(bigInt1, bigInt2))))
//    Ns.int.bigDecs$ insert List((12, None), (12, Some(Set(bigDec1, bigDec2))))
//
//    Ns.int(1).strs$.getJson ===
//      """[
//        |{"int": 1, "strs": null},
//        |{"int": 1, "strs": ["","b"]}
//        |]""".stripMargin
//
//    Ns.long(2L).ints$.getJson ===
//      """[
//        |{"long": 2, "ints": null},
//        |{"long": 2, "ints": [1,2]}
//        |]""".stripMargin
//
//    Ns.int(3).longs$.getJson ===
//      """[
//        |{"int": 3, "longs": null},
//        |{"int": 3, "longs": [21,22]}
//        |]""".stripMargin
//
//    Ns.int(4).floats$.getJson ===
//      """[
//        |{"int": 4, "floats": null},
//        |{"int": 4, "floats": [1.0,2.0]}
//        |]""".stripMargin
//
//    Ns.int(5).doubles$.getJson ===
//      """[
//        |{"int": 5, "doubles": null},
//        |{"int": 5, "doubles": [1.0,2.0]}
//        |]""".stripMargin
//
//    // OBS!: Sets of booleans truncate to one value!
//    Ns.int(6).bools$.getJson ===
//      """[
//        |{"int": 6, "bools": [true]},
//        |{"int": 6, "bools": null}
//        |]""".stripMargin
//
//    Ns.int(7).dates$.getJson ===
//      s"""[
//         |{"int": 7, "dates": null},
//         |{"int": 7, "dates": ["1970-01-01T01:00:01.000+01:00","1970-01-01T01:00:02.000+01:00"]}
//         |]""".stripMargin
//
//    Ns.int(8).uuids$.getJson ===
//      s"""[
//         |{"int": 8, "uuids": null},
//         |{"int": 8, "uuids": ["$uuid1","$uuid2"]}
//         |]""".stripMargin
//
//    Ns.int(9).uris$.getJson ===
//      s"""[
//         |{"int": 9, "uris": null},
//         |{"int": 9, "uris": ["$uri1","$uri2"]}
//         |]""".stripMargin
//
//    Ns.int(10).enums$.getJson ===
//      s"""[
//         |{"int": 10, "enums": null},
//         |{"int": 10, "enums": ["$enum1","$enum2"]}
//         |]""".stripMargin
//
//    Ns.int(11).bigInts$.getJson ===
//      s"""[
//         |{"int": 11, "bigInts": null},
//         |{"int": 11, "bigInts": [$bigInt1,$bigInt2]}
//         |]""".stripMargin
//
//    Ns.int(12).bigDecs$.getJson ===
//      s"""[
//         |{"int": 12, "bigDecs": null},
//         |{"int": 12, "bigDecs": [$bigDec1,$bigDec2]}
//         |]""".stripMargin
//  }
//
//
//  "Optional map" in new CoreSetup {
//
//    Ns.int.strMap$ insert List((1, None), (1, Some(Map("a" -> "A", "b" -> "B"))))
//    Ns.long.intMap$ insert List((2, None), (2, Some(Map("a" -> 1, "b" -> 2))))
//    Ns.int.longMap$ insert List((3, None), (3, Some(Map("a" -> 1L, "b" -> 2L))))
//    Ns.int.floatMap$ insert List((4, None), (4, Some(Map("a" -> 1.0f, "b" -> 2.0f))))
//    Ns.int.doubleMap$ insert List((5, None), (5, Some(Map("a" -> 1.0, "b" -> 2.0))))
//    Ns.int.boolMap$ insert List((6, None), (6, Some(Map("a" -> true, "b" -> false))))
//    Ns.int.dateMap$ insert List((7, None), (7, Some(Map("a" -> date1, "b" -> date2))))
//    Ns.int.uuidMap$ insert List((8, None), (8, Some(Map("a" -> uuid1, "b" -> uuid2))))
//    Ns.int.uriMap$ insert List((9, None), (9, Some(Map("a" -> uri1, "b" -> uri2))))
//    Ns.int.bigIntMap$ insert List((11, None), (11, Some(Map("a" -> bigInt1, "b" -> bigInt2))))
//    Ns.int.bigDecMap$ insert List((12, None), (12, Some(Map("a" -> bigDec1, "b" -> bigDec2))))
//
//    Ns.int(1).strMap$.getJson ===
//      """[
//        |{"int": 1, "strMap": null},
//        |{"int": 1, "strMap": {"a":"A","b":"B"}}
//        |]""".stripMargin
//
//    Ns.long(2L).intMap$.getJson ===
//      """[
//        |{"long": 2, "intMap": null},
//        |{"long": 2, "intMap": {"a":1,"b":2}}
//        |]""".stripMargin
//
//    Ns.int(3).longMap$.getJson ===
//      """[
//        |{"int": 3, "longMap": null},
//        |{"int": 3, "longMap": {"a":1,"b":2}}
//        |]""".stripMargin
//
//    Ns.int(4).floatMap$.getJson ===
//      """[
//        |{"int": 4, "floatMap": null},
//        |{"int": 4, "floatMap": {"a":1.0,"b":2.0}}
//        |]""".stripMargin
//
//    Ns.int(5).doubleMap$.getJson ===
//      """[
//        |{"int": 5, "doubleMap": null},
//        |{"int": 5, "doubleMap": {"a":1.0,"b":2.0}}
//        |]""".stripMargin
//
//    Ns.int(6).boolMap$.getJson ===
//      """[
//        |{"int": 6, "boolMap": {"a":true,"b":false}},
//        |{"int": 6, "boolMap": null}
//        |]""".stripMargin
//
//    Ns.int(7).dateMap$.getJson ===
//      s"""[
//         |{"int": 7, "dateMap": null},
//         |{"int": 7, "dateMap": {"a":"1970-01-01T01:00:01.000+01:00","b":"1970-01-01T01:00:02.000+01:00"}}
//         |]""".stripMargin
//
//    Ns.int(8).uuidMap$.getJson ===
//      s"""[
//         |{"int": 8, "uuidMap": null},
//         |{"int": 8, "uuidMap": {"a":"$uuid1","b":"$uuid2"}}
//         |]""".stripMargin
//
//    Ns.int(9).uriMap$.getJson ===
//      s"""[
//         |{"int": 9, "uriMap": null},
//         |{"int": 9, "uriMap": {"a":"$uri1","b":"$uri2"}}
//         |]""".stripMargin
//
//    Ns.int(11).bigIntMap$.getJson ===
//      s"""[
//         |{"int": 11, "bigIntMap": null},
//         |{"int": 11, "bigIntMap": {"a":$bigInt1,"b":$bigInt2}}
//         |]""".stripMargin
//
//    Ns.int(12).bigDecMap$.getJson ===
//      s"""[
//         |{"int": 12, "bigDecMap": null},
//         |{"int": 12, "bigDecMap": {"a":$bigDec1,"b":$bigDec2}}
//         |]""".stripMargin
//  }
//}
