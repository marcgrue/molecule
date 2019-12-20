package molecule.coretests.crud.insert

import molecule.api.out10._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._


class InsertRelated extends CoreSpec {

  "Basics" in new CoreSetup {

    // Asserting a fact in the `Ref1` namespace is the same as creating
    // one in the `Ns` namespace (no references between the two are made):

    val a0 = Ns.str.insert("a0").eid
    a0.touch === Map(
      ":db/id" -> 17592186045454L,
      ":Ns/str" -> "a0")

    val b0 = Ref1.str1.insert("b0").eid
    b0.touch === Map(
      ":db/id" -> 17592186045456L,
      ":Ref1/str1" -> "b0")

    // If we also assert a fact in `Ns` we will get an entity with
    // a :Ns/str assertion ("a0") of namespace `Ns` and a reference to an entity
    // with another :Ref1/str assertion ("b1") in namespace `Ref1`:

    val a0b1 = Ns.str.Ref1.str1.insert("a0", "b1").eid
    a0b1.touch === Map(
      ":db/id" -> 17592186045458L,
      ":Ns/str" -> "a0",
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045459L,
        ":Ref1/str1" -> "b1")
    )


    // We can expand our graph one level deeper

    val a0b1c2 = Ns.str.Ref1.str1.Ref2.str2.insert("a0", "b1", "c2").eid
    a0b1c2.touch === Map(
      ":db/id" -> 17592186045461L,
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045462L,
        ":Ref1/ref2" -> Map(
          ":db/id" -> 17592186045463L,
          ":Ref2/str2" -> "c2"),
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )


    // We can limit the depth of the retrieved graph

    a0b1c2.touchMax(3) === Map(
      ":db/id" -> 17592186045461L,
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045462L,
        ":Ref1/ref2" -> Map(
          ":db/id" -> 17592186045463L,
          ":Ref2/str2" -> "c2"),
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )

    a0b1c2.touchMax(2) === Map(
      ":db/id" -> 17592186045461L,
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045462L,
        ":Ref1/ref2" -> 17592186045463L,
        ":Ref1/str1" -> "b1"),
      ":Ns/str" -> "a0"
    )

    a0b1c2.touchMax(1) === Map(
      ":db/id" -> 17592186045461L,
      ":Ns/ref1" -> 17592186045462L,
      ":Ns/str" -> "a0"
    )

    // Use `touchQ` to generate a quoted graph that you can paste into your tests
    a0b1c2.touchQuotedMax(1) ===
      """Map(
        |  ":db/id" -> 17592186045461L,
        |  ":Ns/ref1" -> 17592186045462L,
        |  ":Ns/str" -> "a0")""".stripMargin
  }


  "Multiple values across namespaces" in new CoreSetup {

    Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.insert("a0", 0, "b1", 1, "c2", 2)
    Ns.str.int.Ref1.str1.int1.Ref2.str2.int2.get.head === ("a0", 0, "b1", 1, "c2", 2)

    Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.insert(Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))
    Ns.strs.ints.Ref1.strs1.ints1.Ref2.strs2.ints2.get.head === (Set("a0"), Set(0), Set("b1"), Set(1), Set("c2"), Set(2))

    // Address example
    val address = Ns.str.Ref1.int1.str1.Ref2.str2.insert("273 Broadway", 10700, "New York", "USA").eid
    address.touch === Map(
      ":db/id" -> 17592186045462L,
      ":Ns/ref1" -> Map(
        ":db/id" -> 17592186045463L,
        ":Ref1/int1" -> 10700,
        ":Ref1/ref2" -> Map(":db/id" -> 17592186045464L, ":Ref2/str2" -> "USA"),
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

    val id = Ns.int.Refs1.str1.insert(42, "r").eid
    id.touch === Map(
      ":db/id" -> 17592186045454L,
      ":Ns/refs1" -> List( // <-- notice we have a list of references now (with one ref here)
        Map(":db/id" -> 17592186045455L, ":Ref1/str1" -> "r")),
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