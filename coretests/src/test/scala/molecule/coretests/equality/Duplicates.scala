package molecule.coretests.equality

import molecule.api._
import molecule.ast.model._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}


class Duplicates extends CoreSpec {

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
      (1, Some(Set(false))),
      (2, Some(Set(false, true))),
      (3, None)
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

  "Internal transformations" >> {

    "Card-one" in new OneSetup {

      // Distinct values (no transformation)

      m(Ns.int(1, 2))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1, 2)), None, List(), List())
      ))

      m(Ns.int(List(1, 2)))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1, 2)), None, List(), List())
      ))

      m(Ns.int(List(1), List(2)))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1, 2)), None, List(), List())
      ))


      // Single value (no transformation)

      m(Ns.int(1))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
      ))

      m(Ns.int(List(1)))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
      ))


      // Transformation: redundant duplicate values discarded at compile time

      m(Ns.int(1, 1))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
      ))

      m(Ns.int(List(1, 1)))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
      ))

      m(Ns.int(List(1), List(1)))._model === Model(List(
        Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
      ))
    }


    "Card-many" in new ManySetup {

      // Distinct values (no transformation)

      m(Ns.ints(1, 2))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1, 2)), None, List(), List())
      ))

      m(Ns.ints(List(1, 2)))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1, 2)), None, List(), List())
      ))

      m(Ns.ints(List(1), List(2)))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1, 2)), None, List(), List())
      ))


      // Single value (no transformation)

      m(Ns.ints(1))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1)), None, List(), List())
      ))

      m(Ns.ints(List(1)))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1)), None, List(), List())
      ))


      // Transformation: redundant duplicate values discarded at compile time

      m(Ns.ints(1, 1))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1)), None, List(), List())
      ))

      m(Ns.ints(List(1, 1)))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1)), None, List(), List())
      ))

      m(Ns.ints(List(1), List(1)))._model === Model(List(
        Atom("ns", "ints", "Long", 2, Eq(List(1)), None, List(), List())
      ))
    }


    "Input molecule, card-one" in new OneSetup {

      // Distinct values
      m(Ns.int(?)).apply(1, 2)._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("b")), Seq(Seq(1, 2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))


      // Query transformed: redundant duplicate values discarded at runtime and Query representation transformed
      m(Ns.int(?)).apply(1, 1)._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(ScalarBinding(Var("b")), List(List(1)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))

      // Query transformed - becomes the same as applying 1 and 2 only
      m(Ns.int(?)).apply(1, 2, 1)._query === Query(
        Find(List(
          Var("b"))),
        In(
          List(
            InVar(CollectionBinding(Var("b")), Seq(Seq(1, 2)))),
          List(),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding))))
    }
  }


  "Molecules" >> {

    "Card-one" in new OneSetup {

      Ns.bigDec.apply(bigDec1, bigDec1).get === List(bigDec1)
      Ns.bigDec.apply(List(bigDec1, bigDec1)).get === List(bigDec1)
      Ns.bigDec.apply(List(bigDec1), List(bigDec1)).get === List(bigDec1)

      Ns.bigInt.apply(bigInt1, bigInt1).get === List(bigInt1)
      Ns.bigInt.apply(List(bigInt1, bigInt1)).get === List(bigInt1)
      Ns.bigInt.apply(List(bigInt1), List(bigInt1)).get === List(bigInt1)

      Ns.bool.apply(true, true).get === List(true)
      Ns.bool.apply(List(true, true)).get === List(true)
      Ns.bool.apply(List(true), List(true)).get === List(true)

      Ns.date.apply(date1, date1).get === List(date1)
      Ns.date.apply(List(date1, date1)).get === List(date1)
      Ns.date.apply(List(date1), List(date1)).get === List(date1)

      Ns.double.apply(1.0, 1.0).get === List(1.0)
      Ns.double.apply(List(1.0, 1.0)).get === List(1.0)
      Ns.double.apply(List(1.0), List(1.0)).get === List(1.0)

      Ns.enum.apply("enum1", "enum1").get === List("enum1")
      Ns.enum.apply(List("enum1", "enum1")).get === List("enum1")
      Ns.enum.apply(List("enum1"), List("enum1")).get === List("enum1")

      Ns.float.apply(1.0f, 1.0f).get === List(1.0f)
      Ns.float.apply(List(1.0f, 1.0f)).get === List(1.0f)
      Ns.float.apply(List(1.0f), List(1.0f)).get === List(1.0f)

      Ns.int.apply(1, 1).get === List(1)
      Ns.int.apply(List(1, 1)).get === List(1)
      Ns.int.apply(List(1), List(1)).get === List(1)

      Ns.long.apply(1L, 1L).get === List(1L)
      Ns.long.apply(List(1L, 1L)).get === List(1L)
      Ns.long.apply(List(1L), List(1L)).get === List(1L)

      Ns.str.apply("a", "a").get === List("a")
      Ns.str.apply(List("a", "a")).get === List("a")
      Ns.str.apply(List("a"), List("a")).get === List("a")

      Ns.uri.apply(uri1, uri1).get === List(uri1)
      Ns.uri.apply(List(uri1, uri1)).get === List(uri1)
      Ns.uri.apply(List(uri1), List(uri1)).get === List(uri1)

      Ns.uuid.apply(uuid1, uuid1).get === List(uuid1)
      Ns.uuid.apply(List(uuid1, uuid1)).get === List(uuid1)
      Ns.uuid.apply(List(uuid1), List(uuid1)).get === List(uuid1)
    }


    "Card-many" in new ManySetup {

      // Retrieve sets with `1` asserted: Set(1, 2)
      Ns.bigDecs.apply(bigDec1, bigDec1).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(List(bigDec1, bigDec1)).get === List(Set(bigDec1, bigDec2))
      Ns.bigDecs.apply(List(bigDec1), List(bigDec1)).get === List(Set(bigDec1, bigDec2))

      Ns.bigInts.apply(bigInt1, bigInt1).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(List(bigInt1, bigInt1)).get === List(Set(bigInt1, bigInt2))
      Ns.bigInts.apply(List(bigInt1), List(bigInt1)).get === List(Set(bigInt1, bigInt2))

      Ns.bools.apply(true, true).get === List(Set(true, false))
      Ns.bools.apply(List(true, true)).get === List(Set(true, false))
      Ns.bools.apply(List(true), List(true)).get === List(Set(true, false))

      Ns.dates.apply(date1, date1).get === List(Set(date1, date2))
      Ns.dates.apply(List(date1, date1)).get === List(Set(date1, date2))
      Ns.dates.apply(List(date1), List(date1)).get === List(Set(date1, date2))

      Ns.doubles.apply(1.0, 1.0).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(List(1.0, 1.0)).get === List(Set(1.0, 2.0))
      Ns.doubles.apply(List(1.0), List(1.0)).get === List(Set(1.0, 2.0))

      Ns.enums.apply("enum1", "enum1").get === List(Set("enum1", "enum2"))
      Ns.enums.apply(List("enum1", "enum1")).get === List(Set("enum1", "enum2"))
      Ns.enums.apply(List("enum1"), List("enum1")).get === List(Set("enum1", "enum2"))

      Ns.floats.apply(1.0f, 1.0f).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(List(1.0f, 1.0f)).get === List(Set(1.0f, 2.0f))
      Ns.floats.apply(List(1.0f), List(1.0f)).get === List(Set(1.0f, 2.0f))

      Ns.ints.apply(1, 1).get === List(Set(1, 2))
      Ns.ints.apply(List(1, 1)).get === List(Set(1, 2))
      Ns.ints.apply(List(1), List(1)).get === List(Set(1, 2))

      Ns.longs.apply(1L, 1L).get === List(Set(1L, 2L))
      Ns.longs.apply(List(1L, 1L)).get === List(Set(1L, 2L))
      Ns.longs.apply(List(1L), List(1L)).get === List(Set(1L, 2L))

      Ns.strs.apply("a", "a").get === List(Set("a", "b"))
      Ns.strs.apply(List("a", "a")).get === List(Set("a", "b"))
      Ns.strs.apply(List("a"), List("a")).get === List(Set("a", "b"))

      Ns.uris.apply(uri1, uri1).get === List(Set(uri1, uri2))
      Ns.uris.apply(List(uri1, uri1)).get === List(Set(uri1, uri2))
      Ns.uris.apply(List(uri1), List(uri1)).get === List(Set(uri1, uri2))

      Ns.uuids.apply(uuid1, uuid1).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(List(uuid1, uuid1)).get === List(Set(uuid1, uuid2))
      Ns.uuids.apply(List(uuid1), List(uuid1)).get === List(Set(uuid1, uuid2))
    }
  }


  "Input molecules" >> {

    "Card-one" in new OneSetup {
      Ns.int.apply(1, 1).get === List(1)
      Ns.int.apply(List(1, 1)).get === List(1)
      Ns.int.apply(List(1), List(1)).get === List(1)
    }

    "Card-many" in new ManySetup {
      Ns.ints.apply(1, 1).get === List(Set(1, 2))
      Ns.ints.apply(List(1, 1)).get === List(Set(1, 2))
      Ns.ints.apply(List(1), List(1)).get === List(Set(1, 2))
    }
  }


  "Neq" in new OneSetup {

    // Redundant duplicate values are discarded at compile time
    m(Ns.int(1, 1))._model === Model(List(
      Atom("ns", "int", "Long", 1, Eq(List(1)), None, List(), List())
    ))

    m(Ns.int(1, 2))._model === Model(List(
      Atom("ns", "int", "Long", 1, Eq(List(1, 2)), None, List(), List())
    ))

    Ns.int.apply(1, 1).get === List(1)
    Ns.int.apply(List(1, 1)).get === List(1)
    Ns.int.apply(List(1), List(1)).get === List(1)
  }
}