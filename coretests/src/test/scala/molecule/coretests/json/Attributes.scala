package molecule.coretests.json

import molecule.api.out2._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec


class Attributes extends CoreSpec {


  "value" in new CoreSetup {

    // Insert single value for one cardinality-1 attribute
    Ns.str insert str1
    Ns.int insert 1
    Ns.long insert 1L
    Ns.float insert 1.0f
    Ns.double insert 1.0
    Ns.bool insert true
    Ns.date insert date1
    Ns.uuid insert uuid1
    Ns.uri insert uri1
    Ns.enum insert enum1

    Ns.str.getJson ===
      """[
        |{"ns.str": "a"}
        |]""".stripMargin

    Ns.int.getJson ===
      """[
        |{"ns.int": 1}
        |]""".stripMargin

    Ns.long.getJson ===
      """[
        |{"ns.long": 1}
        |]""".stripMargin

    Ns.float.getJson ===
      """[
        |{"ns.float": 1.0}
        |]""".stripMargin

    Ns.double.getJson ===
      """[
        |{"ns.double": 1.0}
        |]""".stripMargin

    Ns.bool.getJson ===
      """[
        |{"ns.bool": true}
        |]""".stripMargin

    Ns.date.getJson ===
      s"""[
         |{"ns.date": "1970-01-01T01:00:01.000+01:00"}
         |]""".stripMargin

    Ns.uuid.getJson ===
      s"""[
         |{"ns.uuid": "$uuid1"}
         |]""".stripMargin

    Ns.uri.getJson ===
      s"""[
         |{"ns.uri": "$uri1"}
         |]""".stripMargin

    Ns.enum.getJson ===
      """[
        |{"ns.enum": "enum1"}
        |]""".stripMargin
  }


  "set" in new CoreSetup {

    Ns.strs insert Set("", "a", "b")
    Ns.ints insert Set(1, 2)
    Ns.longs insert Set(1L, 2L)
    Ns.floats insert Set(1.0f, 2.0f)
    Ns.doubles insert Set(1.0, 2.0)
    Ns.bools insert Set(true, false)
    Ns.dates insert Set(date1, date2)
    Ns.uuids insert Set(uuid1)
    Ns.uris insert Set(uri1, uri2)
    Ns.enums insert Set(enum1, enum2)
    Ns.bigInts insert Set(bigInt1, bigInt2)
    Ns.bigDecs insert Set(bigDec1, bigDec2)

    Ns.strs.getJson ===
      """[
        |{"ns.strs": ["", "a", "b"]}
        |]""".stripMargin

    Ns.ints.getJson ===
      """[
        |{"ns.ints": [1, 2]}
        |]""".stripMargin

    Ns.longs.getJson ===
      """[
        |{"ns.longs": [1, 2]}
        |]""".stripMargin

    // (Order is not guaranteed in Sets)
    Ns.floats.getJson ===
      """[
        |{"ns.floats": [2.0, 1.0]}
        |]""".stripMargin

    Ns.doubles.getJson ===
      """[
        |{"ns.doubles": [2.0, 1.0]}
        |]""".stripMargin

    Ns.bools.getJson ===
      """[
        |{"ns.bools": [true, false]}
        |]""".stripMargin

    Ns.dates.getJson ===
      s"""[
         |{"ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"]}
         |]""".stripMargin

    Ns.uuids.getJson ===
      s"""[
         |{"ns.uuids": ["$uuid1"]}
         |]""".stripMargin

    Ns.uris.getJson ===
      s"""[
         |{"ns.uris": ["$uri1", "$uri2"]}
         |]""".stripMargin

    Ns.enums.getJson ===
      s"""[
         |{"ns.enums": ["$enum1", "$enum2"]}
         |]""".stripMargin

    Ns.bigInts.getJson ===
      """[
        |{"ns.bigInts": [1, 2]}
        |]""".stripMargin

    Ns.bigDecs.getJson ===
      """[
        |{"ns.bigDecs": [2.0, 1.0]}
        |]""".stripMargin
  }


  "map" in new CoreSetup {

    Ns.strMap insert Map("a" -> "A", "b" -> "B")
    Ns.intMap insert Map("a" -> 1, "b" -> 2)
    Ns.longMap insert Map("a" -> 1L, "b" -> 2L)
    Ns.floatMap insert Map("a" -> 1.0f, "b" -> 2.0f)
    Ns.doubleMap insert Map("a" -> 1.0, "b" -> 2.0)
    Ns.boolMap insert Map("a" -> true, "b" -> false)
    Ns.dateMap insert Map("a" -> date1, "b" -> date2)
    Ns.uuidMap insert Map("a" -> uuid1)
    Ns.uriMap insert Map("a" -> uri1, "b" -> uri2)
    Ns.bigIntMap insert Map("a" -> bigInt1, "b" -> bigInt2)
    Ns.bigDecMap insert Map("a" -> bigDec1, "b" -> bigDec2)

    Ns.strMap.getJson ===
      """[
        |{"ns.strMap": {"b": "B", "a": "A"}}
        |]""".stripMargin

    Ns.intMap.getJson ===
      """[
        |{"ns.intMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.longMap.getJson ===
      """[
        |{"ns.longMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.floatMap.getJson ===
      """[
        |{"ns.floatMap": {"b": 2.0, "a": 1.0}}
        |]""".stripMargin

    Ns.doubleMap.getJson ===
      """[
        |{"ns.doubleMap": {"b": 2.0, "a": 1.0}}
        |]""".stripMargin

    Ns.boolMap.getJson ===
      """[
        |{"ns.boolMap": {"b": false, "a": true}}
        |]""".stripMargin

    Ns.dateMap.getJson ===
      s"""[
         |{"ns.dateMap": {"b": "1970-01-01T01:00:02.000+01:00", "a": "1970-01-01T01:00:01.000+01:00"}}
         |]""".stripMargin

    Ns.uuidMap.getJson ===
      s"""[
         |{"ns.uuidMap": {"a": "$uuid1"}}
         |]""".stripMargin

    Ns.uriMap.getJson ===
      s"""[
         |{"ns.uriMap": {"b": "$uri2", "a": "$uri1"}}
         |]""".stripMargin

    Ns.bigIntMap.getJson ===
      """[
        |{"ns.bigIntMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.bigDecMap.getJson ===
      """[
        |{"ns.bigDecMap": {"b": 2.0, "a": 1.0}}
        |]""".stripMargin
  }


  "Optional value" in new CoreSetup {

    Ns.int.str$ insert List((1, None), (1, Some("")), (1, Some("c")))
    Ns.long.int$ insert List((2, None), (2, Some(2)))
    Ns.int.long$ insert List((3, None), (3, Some(20L)))
    Ns.int.float$ insert List((4, None), (4, Some(2.0f)))
    Ns.int.double$ insert List((5, None), (5, Some(2.0)))
    Ns.int.bool$ insert List((6, None), (6, Some(true)), (6, Some(false)))
    Ns.int.date$ insert List((7, None), (7, Some(date2)))
    Ns.int.uuid$ insert List((8, None), (8, Some(uuid2)))
    Ns.int.uri$ insert List((9, None), (9, Some(uri2)))
    Ns.int.enum$ insert List((10, None), (10, Some(enum2)))
    Ns.int.bigInt$ insert List((11, None), (11, Some(bigInt2)))
    Ns.int.bigDec$ insert List((12, None), (12, Some(bigDec2)))

    Ns.int(1).str$.getJson ===
      """[
        |{"ns.int": 1, "ns.str": null},
        |{"ns.int": 1, "ns.str": ""},
        |{"ns.int": 1, "ns.str": "c"}
        |]""".stripMargin

    Ns.long(2L).int$.getJson ===
      """[
        |{"ns.long": 2, "ns.int": null},
        |{"ns.long": 2, "ns.int": 2}
        |]""".stripMargin

    Ns.int(3).long$.getJson ===
      """[
        |{"ns.int": 3, "ns.long": null},
        |{"ns.int": 3, "ns.long": 20}
        |]""".stripMargin

    Ns.int(4).float$.getJson ===
      """[
        |{"ns.int": 4, "ns.float": null},
        |{"ns.int": 4, "ns.float": 2.0}
        |]""".stripMargin

    Ns.int(5).double$.getJson ===
      """[
        |{"ns.int": 5, "ns.double": null},
        |{"ns.int": 5, "ns.double": 2.0}
        |]""".stripMargin

    Ns.int(6).bool$.getJson ===
      """[
        |{"ns.int": 6, "ns.bool": null},
        |{"ns.int": 6, "ns.bool": true},
        |{"ns.int": 6, "ns.bool": false}
        |]""".stripMargin

    Ns.int(7).date$.getJson ===
      s"""[
         |{"ns.int": 7, "ns.date": null},
         |{"ns.int": 7, "ns.date": "1970-01-01T01:00:02.000+01:00"}
         |]""".stripMargin

    Ns.int(8).uuid$.getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuid": null},
         |{"ns.int": 8, "ns.uuid": "$uuid2"}
         |]""".stripMargin

    Ns.int(9).uri$.getJson ===
      s"""[
         |{"ns.int": 9, "ns.uri": null},
         |{"ns.int": 9, "ns.uri": "$uri2"}
         |]""".stripMargin

    Ns.int(10).enum$.getJson ===
      s"""[
         |{"ns.int": 10, "ns.enum": null},
         |{"ns.int": 10, "ns.enum": "$enum2"}
         |]""".stripMargin

    Ns.int(11).bigInt$.getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigInt": null},
         |{"ns.int": 11, "ns.bigInt": $bigInt2}
         |]""".stripMargin

    Ns.int(12).bigDec$.getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDec": null},
         |{"ns.int": 12, "ns.bigDec": $bigDec2}
         |]""".stripMargin
  }


  "Optional set" in new CoreSetup {

    Ns.int.strs$ insert List((1, None), (1, Some(Set("", "b"))))
    Ns.long.ints$ insert List((2, None), (2, Some(Set(1, 2))))
    Ns.int.longs$ insert List((3, None), (3, Some(Set(21L, 22L))))
    Ns.int.floats$ insert List((4, None), (4, Some(Set(1.0f, 2.0f))))
    Ns.int.doubles$ insert List((5, None), (5, Some(Set(1.0, 2.0))))
    Ns.int.bools$ insert List((6, None), (6, Some(Set(true, false))))
    Ns.int.dates$ insert List((7, None), (7, Some(Set(date1, date2))))
    Ns.int.uuids$ insert List((8, None), (8, Some(Set(uuid1, uuid2))))
    Ns.int.uris$ insert List((9, None), (9, Some(Set(uri1, uri2))))
    Ns.int.enums$ insert List((10, None), (10, Some(Set(enum1, enum2))))
    Ns.int.bigInts$ insert List((11, None), (11, Some(Set(bigInt1, bigInt2))))
    Ns.int.bigDecs$ insert List((12, None), (12, Some(Set(bigDec1, bigDec2))))

    Ns.int(1).strs$.getJson ===
      """[
        |{"ns.int": 1, "ns.strs": null},
        |{"ns.int": 1, "ns.strs": ["", "b"]}
        |]""".stripMargin

    Ns.long(2L).ints$.getJson ===
      """[
        |{"ns.long": 2, "ns.ints": null},
        |{"ns.long": 2, "ns.ints": [1, 2]}
        |]""".stripMargin

    Ns.int(3).longs$.getJson ===
      """[
        |{"ns.int": 3, "ns.longs": null},
        |{"ns.int": 3, "ns.longs": [21, 22]}
        |]""".stripMargin

    Ns.int(4).floats$.getJson ===
      """[
        |{"ns.int": 4, "ns.floats": null},
        |{"ns.int": 4, "ns.floats": [1.0, 2.0]}
        |]""".stripMargin

    Ns.int(5).doubles$.getJson ===
      """[
        |{"ns.int": 5, "ns.doubles": null},
        |{"ns.int": 5, "ns.doubles": [1.0, 2.0]}
        |]""".stripMargin

    // OBS!: Sets of booleans truncate to one value!
    Ns.int(6).bools$.getJson ===
      """[
        |{"ns.int": 6, "ns.bools": [true]},
        |{"ns.int": 6, "ns.bools": null}
        |]""".stripMargin

    Ns.int(7).dates$.getJson ===
      s"""[
         |{"ns.int": 7, "ns.dates": null},
         |{"ns.int": 7, "ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"]}
         |]""".stripMargin

    Ns.int(8).uuids$.getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuids": null},
         |{"ns.int": 8, "ns.uuids": ["$uuid1", "$uuid2"]}
         |]""".stripMargin

    Ns.int(9).uris$.getJson ===
      s"""[
         |{"ns.int": 9, "ns.uris": null},
         |{"ns.int": 9, "ns.uris": ["$uri1", "$uri2"]}
         |]""".stripMargin

    Ns.int(10).enums$.getJson ===
      s"""[
         |{"ns.int": 10, "ns.enums": null},
         |{"ns.int": 10, "ns.enums": ["$enum1", "$enum2"]}
         |]""".stripMargin

    Ns.int(11).bigInts$.getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigInts": null},
         |{"ns.int": 11, "ns.bigInts": [$bigInt1, $bigInt2]}
         |]""".stripMargin

    Ns.int(12).bigDecs$.getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDecs": null},
         |{"ns.int": 12, "ns.bigDecs": [$bigDec1, $bigDec2]}
         |]""".stripMargin
  }


  "Optional map" in new CoreSetup {

    Ns.int.strMap$ insert List((1, None), (1, Some(Map("a" -> "A", "b" -> "B"))))
    Ns.long.intMap$ insert List((2, None), (2, Some(Map("a" -> 1, "b" -> 2))))
    Ns.int.longMap$ insert List((3, None), (3, Some(Map("a" -> 1L, "b" -> 2L))))
    Ns.int.floatMap$ insert List((4, None), (4, Some(Map("a" -> 1.0f, "b" -> 2.0f))))
    Ns.int.doubleMap$ insert List((5, None), (5, Some(Map("a" -> 1.0, "b" -> 2.0))))
    Ns.int.boolMap$ insert List((6, None), (6, Some(Map("a" -> true, "b" -> false))))
    Ns.int.dateMap$ insert List((7, None), (7, Some(Map("a" -> date1, "b" -> date2))))
    Ns.int.uuidMap$ insert List((8, None), (8, Some(Map("a" -> uuid1, "b" -> uuid2))))
    Ns.int.uriMap$ insert List((9, None), (9, Some(Map("a" -> uri1, "b" -> uri2))))
    Ns.int.bigIntMap$ insert List((11, None), (11, Some(Map("a" -> bigInt1, "b" -> bigInt2))))
    Ns.int.bigDecMap$ insert List((12, None), (12, Some(Map("a" -> bigDec1, "b" -> bigDec2))))

    Ns.int(1).strMap$.getJson ===
      """[
        |{"ns.int": 1, "ns.strMap": null},
        |{"ns.int": 1, "ns.strMap": {"a": "A", "b": "B"}}
        |]""".stripMargin

    Ns.long(2L).intMap$.getJson ===
      """[
        |{"ns.long": 2, "ns.intMap": null},
        |{"ns.long": 2, "ns.intMap": {"a": 1, "b": 2}}
        |]""".stripMargin

    Ns.int(3).longMap$.getJson ===
      """[
        |{"ns.int": 3, "ns.longMap": null},
        |{"ns.int": 3, "ns.longMap": {"a": 1, "b": 2}}
        |]""".stripMargin

    Ns.int(4).floatMap$.getJson ===
      """[
        |{"ns.int": 4, "ns.floatMap": null},
        |{"ns.int": 4, "ns.floatMap": {"a": 1.0, "b": 2.0}}
        |]""".stripMargin

    Ns.int(5).doubleMap$.getJson ===
      """[
        |{"ns.int": 5, "ns.doubleMap": null},
        |{"ns.int": 5, "ns.doubleMap": {"a": 1.0, "b": 2.0}}
        |]""".stripMargin

    Ns.int(6).boolMap$.getJson ===
      """[
        |{"ns.int": 6, "ns.boolMap": {"a": true, "b": false}},
        |{"ns.int": 6, "ns.boolMap": null}
        |]""".stripMargin

    Ns.int(7).dateMap$.getJson ===
      s"""[
         |{"ns.int": 7, "ns.dateMap": null},
         |{"ns.int": 7, "ns.dateMap": {"a": "1970-01-01T01:00:01.000+01:00", "b": "1970-01-01T01:00:02.000+01:00"}}
         |]""".stripMargin

    Ns.int(8).uuidMap$.getJson ===
      s"""[
         |{"ns.int": 8, "ns.uuidMap": null},
         |{"ns.int": 8, "ns.uuidMap": {"a": "$uuid1", "b": "$uuid2"}}
         |]""".stripMargin

    Ns.int(9).uriMap$.getJson ===
      s"""[
         |{"ns.int": 9, "ns.uriMap": null},
         |{"ns.int": 9, "ns.uriMap": {"a": "$uri1", "b": "$uri2"}}
         |]""".stripMargin

    Ns.int(11).bigIntMap$.getJson ===
      s"""[
         |{"ns.int": 11, "ns.bigIntMap": null},
         |{"ns.int": 11, "ns.bigIntMap": {"a": $bigInt1, "b": $bigInt2}}
         |]""".stripMargin

    Ns.int(12).bigDecMap$.getJson ===
      s"""[
         |{"ns.int": 12, "ns.bigDecMap": null},
         |{"ns.int": 12, "ns.bigDecMap": {"a": $bigDec1, "b": $bigDec2}}
         |]""".stripMargin
  }
}
