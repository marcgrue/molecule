package molecule.coretests.crud.insert

import molecule.datomic.peer.api._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._


class InsertRelated extends CoreSpec {


  "Basics" in new CoreSetup {

    // Asserting a fact in the `Ref1` namespace is the same as creating
    // one in the `Ns` namespace (no references between the two are made):

    val a0 = Ns.str.insert("a0").eid
    a0.touch === Map(
      ":db/id" -> a0,
      ":Ns/str" -> "a0")

    val b0 = Ref1.str1.insert("b0").eid
    b0.touch === Map(
      ":db/id" -> b0,
      ":Ref1/str1" -> "b0")

    // If we also assert a fact in `Ns` we will get an entity with
    // a :Ns/str assertion ("a0") of namespace `Ns` and a reference to an entity
    // with another :Ref1/str assertion ("b1") in namespace `Ref1`:

    val List(a0ref, b1ref) = Ns.str.Ref1.str1.insert("a0", "b1").eids
    a0ref.touch === Map(
      ":db/id" -> a0ref,
      ":Ns/str" -> "a0",
      ":Ns/ref1" -> Map(
        ":db/id" -> b1ref,
        ":Ref1/str1" -> "b1")
    )


    // We can expand our graph one level deeper
    val List(a0refs, b1ref1, c2ref2) =
      Ns.str.Ref1.str1.Ref2.str2.insert("a0", "b1", "c2").eids

    a0refs.touch === Map(
      ":db/id" -> a0refs,
      ":Ns/ref1" -> Map(
        ":db/id" -> b1ref1,
        ":Ref1/ref2" -> Map(
          ":db/id" -> c2ref2,
          ":Ref2/str2" -> "c2"),
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )


    // We can limit the depth of the retrieved graph

    a0refs.touchMax(3) === Map(
      ":db/id" -> a0refs,
      ":Ns/ref1" -> Map(
        ":db/id" -> b1ref1,
        ":Ref1/ref2" -> Map(
          ":db/id" -> c2ref2,
          ":Ref2/str2" -> "c2"),
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )

    a0refs.touchMax(2) === Map(
      ":db/id" -> a0refs,
      ":Ns/ref1" -> Map(
        ":db/id" -> b1ref1,
        ":Ref1/ref2" -> c2ref2,
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )

    a0refs.touchMax(1) === Map(
      ":db/id" -> a0refs,
      ":Ns/ref1" -> b1ref1,
      ":Ns/str" -> "a0"
    )

    // Use `touchQ` to generate a quoted graph that you can paste into your tests
    a0refs.touchQuotedMax(1) ===
      s"""Map(
         |  ":db/id" -> ${a0refs}L,
         |  ":Ns/ref1" -> ${b1ref1}L,
         |  ":Ns/str" -> "a0")""".stripMargin
  }


  "Multiple values across namespaces" in new CoreSetup {

    Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.insert("a0", 0, "b1", 1, "c2", 2)
    Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.get.head === ("a0", 0, "b1", 1, "c2", 2)

    Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.insert(Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))
    Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.get.head === (Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))

    // Address example
    val List(addressE, streetE, countryE) =
      Ns.str.Ref1.int1.str1.Ref2.str2.insert("273 Broadway", 10700, "New York", "USA").eids
    addressE.touch === Map(
      ":db/id" -> addressE,
      ":Ns/ref1" -> Map(
        ":db/id" -> streetE,
        ":Ref1/int1" -> 10700,
        ":Ref1/ref2" -> Map(":db/id" -> countryE, ":Ref2/str2" -> "USA"),
        ":Ref1/str1" -> "New York"),
      ":Ns/str" -> "273 Broadway")

    // We can even create chains of relationships without having intermediate attribute values
    Ns.str.Ref1.Ref2.int2.insert("a", 1)
    Ns.str.Ref1.Ref2.int2.get.head === ("a", 1)
  }


  "Optional values" in new CoreSetup {

    Ns.str.Ref1.str1$.Ref2.int2 insert List(
      ("a", Some("aa"), 1),
      ("b", None, 2)
    )

    Ns.str.Ref1.str1$.Ref2.int2.get === List(
      ("b", None, 2),
      ("a", Some("aa"), 1)
    )
    Ns.str.Ref1.str1.Ref2.int2.get === List(
      ("a", "aa", 1)
    )
  }


  "Card many references" in new CoreSetup {

    val List(base, ref) = Ns.int.Refs1.str1.insert(42, "r").eids
    base.touch === Map(
      ":db/id" -> base,
      ":Ns/refs1" -> List( // <-- notice we have a list of references now (with one ref here)
        Map(":db/id" -> ref, ":Ref1/str1" -> "r")),
      ":Ns/int" -> 42
    )


    // Note that applying multiple values creates multiple base entities with a
    // reference to each new `:Ref1/str` assertion, so that we get the following:

    val List(id1, ref1, id2, ref2) = Ns.int.Refs1.str1.insert.apply(Seq((1, "r"), (2, "s"))).eids
    id1.touch === Map(
      ":db/id" -> id1,
      ":Ns/refs1" -> List(
        Map(":db/id" -> ref1, ":Ref1/str1" -> "r")),
      ":Ns/int" -> 1
    )
    id2.touch === Map(
      ":db/id" -> id2,
      ":Ns/refs1" -> List(
        Map(":db/id" -> ref2, ":Ref1/str1" -> "s")),
      ":Ns/int" -> 2
    )
  }
}