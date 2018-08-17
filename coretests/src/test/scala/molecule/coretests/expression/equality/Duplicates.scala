package molecule.coretests.expression.equality

import molecule.api._
import molecule.ast.model._
import molecule.ast.query._
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError


class Duplicates extends ApplyBase {


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
          List(),
          List(
            Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Val(1), Empty, NoBinding))),
            Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Val(2), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          RuleInvocation("rule1", List(Var("a"))))))


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
          List(),
          List(
            Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Val(1), Empty, NoBinding))),
            Rule("rule1", List(Var("a")), List(DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Val(2), Empty, NoBinding)))),
          List(DS)),
        Where(List(
          DataClause(ImplDS, Var("a"), KW("ns", "int", ""), Var("b"), Empty, NoBinding),
          RuleInvocation("rule1", List(Var("a"))))))
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
      Ns.int.apply(List(1, 1)).get === List(1, 2)
      Ns.int.apply(List(1), List(1)).get === List(1, 2)
    }


    "Card-many" in new ManySetup {

      Ns.ints.apply(1, 1).get === List(1)
      Ns.ints.apply(List(1, 1)).get === List(1, 2)
      Ns.ints.apply(List(1), List(1)).get === List(1, 2)
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
    Ns.int.apply(List(1, 1)).get === List(1, 2)
    Ns.int.apply(List(1), List(1)).get === List(1, 2)
  }

}