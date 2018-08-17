package molecule.coretests

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}

class ApplyBase extends CoreSpec {

  class OneStringSetup extends CoreSetup {
    Ns.int.str$ insert List(
      (1, Some("a")),
      (2, Some("b")),
      (3, Some("c")),
      (4, None)
    )
  }

  class OneSetup extends CoreSetup {
    Ns.str.int$ insert List(
      ("a", Some(1)),
      ("b", Some(2)),
      ("c", Some(3)),
      ("d", None)
    )

    Ns.int.long$ insert List(
      (1, Some(1L)),
      (2, Some(2L)),
      (3, Some(3L)),
      (4, None)
    )

    Ns.int.float$ insert List(
      (1, Some(1.0f)),
      (2, Some(2.0f)),
      (3, Some(3.0f)),
      (4, None)
    )

    Ns.int.double$ insert List(
      (1, Some(1.0)),
      (2, Some(2.0)),
      (3, Some(3.0)),
      (4, None)
    )

    Ns.int.bool$ insert List(
      (1, Some(true)),
      (2, Some(false)),
      (3, None)
    )

    Ns.int.date$ insert List(
      (1, Some(date1)),
      (2, Some(date2)),
      (3, Some(date3)),
      (4, None)
    )

    Ns.int.bigInt$ insert List(
      (1, Some(bigInt1)),
      (2, Some(bigInt2)),
      (3, Some(bigInt3)),
      (4, None)
    )

    Ns.int.bigDec$ insert List(
      (1, Some(bigDec1)),
      (2, Some(bigDec2)),
      (3, Some(bigDec3)),
      (4, None)
    )

    Ns.int.uuid$ insert List(
      (1, Some(uuid1)),
      (2, Some(uuid2)),
      (3, Some(uuid3)),
      (4, None)
    )

    Ns.int.uri$ insert List(
      (1, Some(uri1)),
      (2, Some(uri2)),
      (3, Some(uri3)),
      (4, None)
    )

    Ns.int.enum$ insert List(
      (1, Some("enum1")),
      (2, Some("enum2")),
      (3, Some("enum3")),
      (4, None)
    )
  }

  class ManySetup extends CoreSetup {

    // We pair cardinality many attribute values with card-one's too to be able to group by cardinality one values
    Ns.int.strs$ insert List(
      (1, Some(Set("a", "b"))),
      (2, Some(Set("b", "c"))),
      (3, Some(Set("c", "d"))),
      (4, None)
    )

    Ns.int.ints$ insert List(
      (10, Some(Set(1, 2))),
      (20, Some(Set(2, 3))),
      (30, Some(Set(3, 4))),
      (40, None)
    )

    Ns.int.longs$ insert List(
      (1, Some(Set(1L, 2L))),
      (2, Some(Set(2L, 3L))),
      (3, Some(Set(3L, 4L))),
      (4, None),
    )

    Ns.int.floats$ insert List(
      (1, Some(Set(1.0f, 2.0f))),
      (2, Some(Set(2.0f, 3.0f))),
      (3, Some(Set(3.0f, 4.0f))),
      (4, None)
    )

    Ns.int.doubles$ insert List(
      (1, Some(Set(1.0, 2.0))),
      (2, Some(Set(2.0, 3.0))),
      (3, Some(Set(3.0, 4.0))),
      (4, None)
    )

    // Set of boolean values maybe not so useful
    Ns.int.bools$ insert List(
      (1, Some(Set(true))),
      (2, Some(Set(false))),
      (3, Some(Set(true, false))),
      (4, None)
    )

    Ns.int.dates$ insert List(
      (1, Some(Set(date1, date2))),
      (2, Some(Set(date2, date3))),
      (3, Some(Set(date3, date4))),
      (4, None)
    )

    Ns.int.bigInts$ insert List(
      (1, Some(Set(bigInt1, bigInt2))),
      (2, Some(Set(bigInt2, bigInt3))),
      (3, Some(Set(bigInt3, bigInt4))),
      (4, None)
    )

    Ns.int.bigDecs$ insert List(
      (1, Some(Set(bigDec1, bigDec2))),
      (2, Some(Set(bigDec2, bigDec3))),
      (3, Some(Set(bigDec3, bigDec4))),
      (4, None)
    )

    Ns.int.uuids$ insert List(
      (1, Some(Set(uuid1, uuid2))),
      (2, Some(Set(uuid2, uuid3))),
      (3, Some(Set(uuid3, uuid4))),
      (4, None)
    )

    Ns.int.uris$ insert List(
      (1, Some(Set(uri1, uri2))),
      (2, Some(Set(uri2, uri3))),
      (3, Some(Set(uri3, uri4))),
      (4, None)
    )

    Ns.int.enums$ insert List(
      (1, Some(Set("enum1", "enum2"))),
      (2, Some(Set("enum2", "enum3"))),
      (3, Some(Set("enum3", "enum4"))),
      (4, None)
    )
  }

  class OneRefSetup extends CoreSetup {

    m(Ns.str.Ref1.int1) insert List(
      ("en", 1),
      ("fr", 2)
    )
  }

  class ManyRefSetup extends CoreSetup {

    m(Ns.str.Refs1 * Ref1.int1) insert List(
      ("en", List(1, 2)),
      ("fr", List(1, 2)),
      ("da", List(3, 4))
    )
  }

  // Todo
  //  class OneRefSetup extends CoreSetup {
  //
  //    m(Ns.strMap.Ref1.int1) insert List(
  //      (Map("en" -> "Hi there"), 1),
  //      (Map("fr" -> "Bonjour", "en" -> "Oh, Hi"), 1)
  //    )
  //  }
  //
  //  class ManyRefSetup extends CoreSetup {
  //
  //    m(Ns.strMap.Refs1 * Ref1.int1) insert List(
  //      (Map("en" -> "Hi there"), List(1, 2)),
  //      (Map("fr" -> "Bonjour", "en" -> "Oh, Hi"), List(1, 2)),
  //      (Map("en" -> "Hello"), List(2, 3)),
  //      (Map("da" -> "Hej"), List(3, 4))
  //    )
  //  }
}