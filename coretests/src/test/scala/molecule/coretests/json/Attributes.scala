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
        |{"Ns.str": "a"}
        |]""".stripMargin

    Ns.int.getJson ===
      """[
        |{"Ns.int": 1}
        |]""".stripMargin

    Ns.long.getJson ===
      """[
        |{"Ns.long": 1}
        |]""".stripMargin

    Ns.float.getJson ===
      """[
        |{"Ns.float": 1.0}
        |]""".stripMargin

    Ns.double.getJson ===
      """[
        |{"Ns.double": 1.0}
        |]""".stripMargin

    Ns.bool.getJson ===
      """[
        |{"Ns.bool": true}
        |]""".stripMargin

    Ns.date.getJson ===
      s"""[
         |{"Ns.date": "1970-01-01T01:00:01.000+01:00"}
         |]""".stripMargin

    Ns.uuid.getJson ===
      s"""[
         |{"Ns.uuid": "$uuid1"}
         |]""".stripMargin

    Ns.uri.getJson ===
      s"""[
         |{"Ns.uri": "$uri1"}
         |]""".stripMargin

    Ns.enum.getJson ===
      """[
        |{"Ns.enum": "enum1"}
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
        |{"Ns.strs": ["", "a", "b"]}
        |]""".stripMargin

    Ns.ints.getJson ===
      """[
        |{"Ns.ints": [1, 2]}
        |]""".stripMargin

    Ns.longs.getJson ===
      """[
        |{"Ns.longs": [1, 2]}
        |]""".stripMargin

    // (Order is not guaranteed in Sets)
    Ns.floats.getJson ===
      """[
        |{"Ns.floats": [2.0, 1.0]}
        |]""".stripMargin

    Ns.doubles.getJson ===
      """[
        |{"Ns.doubles": [2.0, 1.0]}
        |]""".stripMargin

    Ns.bools.getJson ===
      """[
        |{"Ns.bools": [true, false]}
        |]""".stripMargin

    Ns.dates.getJson ===
      s"""[
         |{"Ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"]}
         |]""".stripMargin

    Ns.uuids.getJson ===
      s"""[
         |{"Ns.uuids": ["$uuid1"]}
         |]""".stripMargin

    Ns.uris.getJson ===
      s"""[
         |{"Ns.uris": ["$uri1", "$uri2"]}
         |]""".stripMargin

    Ns.enums.getJson ===
      s"""[
         |{"Ns.enums": ["$enum1", "$enum2"]}
         |]""".stripMargin

    Ns.bigInts.getJson ===
      """[
        |{"Ns.bigInts": [1, 2]}
        |]""".stripMargin

    Ns.bigDecs.getJson ===
      """[
        |{"Ns.bigDecs": [2.0, 1.0]}
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
        |{"Ns.strMap": {"b": "B", "a": "A"}}
        |]""".stripMargin

    Ns.intMap.getJson ===
      """[
        |{"Ns.intMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.longMap.getJson ===
      """[
        |{"Ns.longMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.floatMap.getJson ===
      """[
        |{"Ns.floatMap": {"b": 2.0, "a": 1.0}}
        |]""".stripMargin

    Ns.doubleMap.getJson ===
      """[
        |{"Ns.doubleMap": {"b": 2.0, "a": 1.0}}
        |]""".stripMargin

    Ns.boolMap.getJson ===
      """[
        |{"Ns.boolMap": {"b": false, "a": true}}
        |]""".stripMargin

    Ns.dateMap.getJson ===
      s"""[
         |{"Ns.dateMap": {"b": "1970-01-01T01:00:02.000+01:00", "a": "1970-01-01T01:00:01.000+01:00"}}
         |]""".stripMargin

    Ns.uuidMap.getJson ===
      s"""[
         |{"Ns.uuidMap": {"a": "$uuid1"}}
         |]""".stripMargin

    Ns.uriMap.getJson ===
      s"""[
         |{"Ns.uriMap": {"b": "$uri2", "a": "$uri1"}}
         |]""".stripMargin

    Ns.bigIntMap.getJson ===
      """[
        |{"Ns.bigIntMap": {"b": 2, "a": 1}}
        |]""".stripMargin

    Ns.bigDecMap.getJson ===
      """[
        |{"Ns.bigDecMap": {"b": 2.0, "a": 1.0}}
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
        |{"Ns.int": 1, "Ns.str": null},
        |{"Ns.int": 1, "Ns.str": ""},
        |{"Ns.int": 1, "Ns.str": "c"}
        |]""".stripMargin

    Ns.long(2L).int$.getJson ===
      """[
        |{"Ns.long": 2, "Ns.int": null},
        |{"Ns.long": 2, "Ns.int": 2}
        |]""".stripMargin

    Ns.int(3).long$.getJson ===
      """[
        |{"Ns.int": 3, "Ns.long": null},
        |{"Ns.int": 3, "Ns.long": 20}
        |]""".stripMargin

    Ns.int(4).float$.getJson ===
      """[
        |{"Ns.int": 4, "Ns.float": null},
        |{"Ns.int": 4, "Ns.float": 2.0}
        |]""".stripMargin

    Ns.int(5).double$.getJson ===
      """[
        |{"Ns.int": 5, "Ns.double": null},
        |{"Ns.int": 5, "Ns.double": 2.0}
        |]""".stripMargin

    Ns.int(6).bool$.getJson ===
      """[
        |{"Ns.int": 6, "Ns.bool": null},
        |{"Ns.int": 6, "Ns.bool": true},
        |{"Ns.int": 6, "Ns.bool": false}
        |]""".stripMargin

    Ns.int(7).date$.getJson ===
      s"""[
         |{"Ns.int": 7, "Ns.date": null},
         |{"Ns.int": 7, "Ns.date": "1970-01-01T01:00:02.000+01:00"}
         |]""".stripMargin

    Ns.int(8).uuid$.getJson ===
      s"""[
         |{"Ns.int": 8, "Ns.uuid": null},
         |{"Ns.int": 8, "Ns.uuid": "$uuid2"}
         |]""".stripMargin

    Ns.int(9).uri$.getJson ===
      s"""[
         |{"Ns.int": 9, "Ns.uri": null},
         |{"Ns.int": 9, "Ns.uri": "$uri2"}
         |]""".stripMargin

    Ns.int(10).enum$.getJson ===
      s"""[
         |{"Ns.int": 10, "Ns.enum": null},
         |{"Ns.int": 10, "Ns.enum": "$enum2"}
         |]""".stripMargin

    Ns.int(11).bigInt$.getJson ===
      s"""[
         |{"Ns.int": 11, "Ns.bigInt": null},
         |{"Ns.int": 11, "Ns.bigInt": $bigInt2}
         |]""".stripMargin

    Ns.int(12).bigDec$.getJson ===
      s"""[
         |{"Ns.int": 12, "Ns.bigDec": null},
         |{"Ns.int": 12, "Ns.bigDec": $bigDec2}
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
        |{"Ns.int": 1, "Ns.strs": null},
        |{"Ns.int": 1, "Ns.strs": ["", "b"]}
        |]""".stripMargin

    Ns.long(2L).ints$.getJson ===
      """[
        |{"Ns.long": 2, "Ns.ints": null},
        |{"Ns.long": 2, "Ns.ints": [1, 2]}
        |]""".stripMargin

    Ns.int(3).longs$.getJson ===
      """[
        |{"Ns.int": 3, "Ns.longs": null},
        |{"Ns.int": 3, "Ns.longs": [21, 22]}
        |]""".stripMargin

    Ns.int(4).floats$.getJson ===
      """[
        |{"Ns.int": 4, "Ns.floats": null},
        |{"Ns.int": 4, "Ns.floats": [1.0, 2.0]}
        |]""".stripMargin

    Ns.int(5).doubles$.getJson ===
      """[
        |{"Ns.int": 5, "Ns.doubles": null},
        |{"Ns.int": 5, "Ns.doubles": [1.0, 2.0]}
        |]""".stripMargin

    // OBS!: Sets of booleans truncate to one value!
    Ns.int(6).bools$.getJson ===
      """[
        |{"Ns.int": 6, "Ns.bools": [true]},
        |{"Ns.int": 6, "Ns.bools": null}
        |]""".stripMargin

    Ns.int(7).dates$.getJson ===
      s"""[
         |{"Ns.int": 7, "Ns.dates": null},
         |{"Ns.int": 7, "Ns.dates": ["1970-01-01T01:00:01.000+01:00", "1970-01-01T01:00:02.000+01:00"]}
         |]""".stripMargin

    Ns.int(8).uuids$.getJson ===
      s"""[
         |{"Ns.int": 8, "Ns.uuids": null},
         |{"Ns.int": 8, "Ns.uuids": ["$uuid1", "$uuid2"]}
         |]""".stripMargin

    Ns.int(9).uris$.getJson ===
      s"""[
         |{"Ns.int": 9, "Ns.uris": null},
         |{"Ns.int": 9, "Ns.uris": ["$uri1", "$uri2"]}
         |]""".stripMargin

    Ns.int(10).enums$.getJson ===
      s"""[
         |{"Ns.int": 10, "Ns.enums": null},
         |{"Ns.int": 10, "Ns.enums": ["$enum1", "$enum2"]}
         |]""".stripMargin

    Ns.int(11).bigInts$.getJson ===
      s"""[
         |{"Ns.int": 11, "Ns.bigInts": null},
         |{"Ns.int": 11, "Ns.bigInts": [$bigInt1, $bigInt2]}
         |]""".stripMargin

    Ns.int(12).bigDecs$.getJson ===
      s"""[
         |{"Ns.int": 12, "Ns.bigDecs": null},
         |{"Ns.int": 12, "Ns.bigDecs": [$bigDec1, $bigDec2]}
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
        |{"Ns.int": 1, "Ns.strMap": null},
        |{"Ns.int": 1, "Ns.strMap": {"a": "A", "b": "B"}}
        |]""".stripMargin

    Ns.long(2L).intMap$.getJson ===
      """[
        |{"Ns.long": 2, "Ns.intMap": null},
        |{"Ns.long": 2, "Ns.intMap": {"a": 1, "b": 2}}
        |]""".stripMargin

    Ns.int(3).longMap$.getJson ===
      """[
        |{"Ns.int": 3, "Ns.longMap": null},
        |{"Ns.int": 3, "Ns.longMap": {"a": 1, "b": 2}}
        |]""".stripMargin

    Ns.int(4).floatMap$.getJson ===
      """[
        |{"Ns.int": 4, "Ns.floatMap": null},
        |{"Ns.int": 4, "Ns.floatMap": {"a": 1.0, "b": 2.0}}
        |]""".stripMargin

    Ns.int(5).doubleMap$.getJson ===
      """[
        |{"Ns.int": 5, "Ns.doubleMap": null},
        |{"Ns.int": 5, "Ns.doubleMap": {"a": 1.0, "b": 2.0}}
        |]""".stripMargin

    Ns.int(6).boolMap$.getJson ===
      """[
        |{"Ns.int": 6, "Ns.boolMap": {"a": true, "b": false}},
        |{"Ns.int": 6, "Ns.boolMap": null}
        |]""".stripMargin

    Ns.int(7).dateMap$.getJson ===
      s"""[
         |{"Ns.int": 7, "Ns.dateMap": null},
         |{"Ns.int": 7, "Ns.dateMap": {"a": "1970-01-01T01:00:01.000+01:00", "b": "1970-01-01T01:00:02.000+01:00"}}
         |]""".stripMargin

    Ns.int(8).uuidMap$.getJson ===
      s"""[
         |{"Ns.int": 8, "Ns.uuidMap": null},
         |{"Ns.int": 8, "Ns.uuidMap": {"a": "$uuid1", "b": "$uuid2"}}
         |]""".stripMargin

    Ns.int(9).uriMap$.getJson ===
      s"""[
         |{"Ns.int": 9, "Ns.uriMap": null},
         |{"Ns.int": 9, "Ns.uriMap": {"a": "$uri1", "b": "$uri2"}}
         |]""".stripMargin

    Ns.int(11).bigIntMap$.getJson ===
      s"""[
         |{"Ns.int": 11, "Ns.bigIntMap": null},
         |{"Ns.int": 11, "Ns.bigIntMap": {"a": $bigInt1, "b": $bigInt2}}
         |]""".stripMargin

    Ns.int(12).bigDecMap$.getJson ===
      s"""[
         |{"Ns.int": 12, "Ns.bigDecMap": null},
         |{"Ns.int": 12, "Ns.bigDecMap": {"a": $bigDec1, "b": $bigDec2}}
         |]""".stripMargin
  }
}
