package molecule.coretests.attr

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class OptionalValues extends CoreSpec {


  "Correct card-one types returned" >> {

    "String (no assertion)" in new CoreSetup {
      Ns.int.str.insert(1, "a")
      Ns.int.insert(2)

      // Int mandatory, String optional
      Ns.int.str$.get === List((1, Some("a")), (2, None))

      // Int and String mandatory
      Ns.int.str.get === List((1, "a"))
    }

    "String (optional assertion)" in new CoreSetup {
      Ns.int.str$ insert List((1, Some("a")), (2, None))

      Ns.int.str$.get === List((1, Some("a")), (2, None))
      Ns.int.str.get === List((1, "a"))
    }

    "Int" in new CoreSetup {
      Ns.str.int$ insert List(("a", Some(1)), ("b", None))

      Ns.str.int$.get === List(("a", Some(1)), ("b", None))
      Ns.str.int.get === List(("a", 1))
    }

    "Long" in new CoreSetup {
      Ns.int.long$ insert List((1, Some(3L)), (2, None))

      Ns.int.long$.get === List((1, Some(3L)), (2, None))
      Ns.int.long.get === List((1, 3L))
    }

    "Float" in new CoreSetup {
      Ns.int.float$ insert List((1, Some(3.0f)), (2, None))

      Ns.int.float$.get === List((1, Some(3.0f)), (2, None))
      Ns.int.float.get === List((1, 3.0f))
    }

    "Boolean" in new CoreSetup {
      Ns.int.bool$ insert List((1, Some(true)), (2, None))

      Ns.int.bool$.get === List((1, Some(true)), (2, None))
      Ns.int.bool.get === List((1, true))
    }

    "Date" in new CoreSetup {
      Ns.int.date$ insert List((1, Some(date1)), (2, None))

      Ns.int.date$.get === List((1, Some(date1)), (2, None))
      Ns.int.date.get === List((1, date1))
    }

    "UUID" in new CoreSetup {
      Ns.int.uuid$ insert List((1, Some(uuid1)), (2, None))

      Ns.int.uuid$.get === List((1, Some(uuid1)), (2, None))
      Ns.int.uuid.get === List((1, uuid1))
    }

    "URI" in new CoreSetup {
      Ns.int.uri$ insert List((1, Some(uri1)), (2, None))

      Ns.int.uri$.get === List((1, Some(uri1)), (2, None))
      Ns.int.uri.get === List((1, uri1))
    }

    "Enum" in new CoreSetup {
      Ns.int.enum$ insert List((1, Some("enum1")), (2, None))

      Ns.int.enum$.get === List((1, Some("enum1")), (2, None))
      Ns.int.enum.get === List((1, "enum1"))
    }

    "Ref Long" in new CoreSetup {
      Ns.int.ref1$ insert List((1, Some(3L)), (2, None))

      Ns.int.ref1$.get === List((1, Some(3L)), (2, None))
      Ns.int.ref1.get === List((1, 3L))
    }
  }


  "Correct card-many types returned" >> {

    "String (no assertion)" in new CoreSetup {
      Ns.int.strs.insert(1, Set("a", "b"))
      Ns.int.insert(2)

      Ns.int.strs$.get === List((1, Some(Set("a", "b"))), (2, None))
      Ns.int.strs.get === List((1, Set("a", "b")))
    }

    "String (empty Set asserted)" in new CoreSetup {
      Ns.int.strs.insert(1, Set("a", "b"))
      // No strings asserted from empty Set
      Ns.int.strs.insert(2, Set[String]())

      Ns.int.strs$.get === List((1, Some(Set("a", "b"))), (2, None))
      Ns.int.strs.get === List((1, Set("a", "b")))
    }

    "String (optional assertion)" in new CoreSetup {
      Ns.int.strs$ insert Seq((1, Some(Set("a", "b"))), (2, None))

      Ns.int.strs$.get === List((1, Some(Set("a", "b"))), (2, None))
      Ns.int.strs.get === List((1, Set("a", "b")))
    }

    "Int" in new CoreSetup {
      Ns.str.ints$ insert List(("a", Some(Set(1, 2))), ("b", None))

      Ns.str.ints$.get === List(("a", Some(Set(1, 2))), ("b", None))
      Ns.str.ints.get === List(("a", Set(1, 2)))
    }

    "Long" in new CoreSetup {
      Ns.int.longs$ insert Seq((1, Some(Set(3L, 4L))), (2, None))

      Ns.int.longs$.get === List((1, Some(Set(3L, 4L))), (2, None))
      Ns.int.longs.get === List((1, Set(3L, 4L)))
    }

    "Float" in new CoreSetup {
      Ns.int.floats$ insert Seq((1, Some(Set(3.0f, 4.0f))), (2, None))

      Ns.int.floats$.get === List((1, Some(Set(3.0f, 4.0f))), (2, None))
      Ns.int.floats.get === List((1, Set(3.0f, 4.0f)))
    }

    // (Boolean Sets not implemented)

    "Date" in new CoreSetup {
      Ns.int.dates$ insert Seq((1, Some(Set(date1, date2))), (2, None))

      Ns.int.dates$.get === List((1, Some(Set(date1, date2))), (2, None))
      Ns.int.dates.get === List((1, Set(date1, date2)))
    }

    "UUID" in new CoreSetup {
      Ns.int.uuids$ insert Seq((1, Some(Set(uuid1, uuid2))), (2, None))

      Ns.int.uuids$.get === List((1, Some(Set(uuid1, uuid2))), (2, None))
      Ns.int.uuids.get === List((1, Set(uuid1, uuid2)))
    }

    "URI" in new CoreSetup {
      Ns.int.uris$ insert Seq((1, Some(Set(uri1, uri2))), (2, None))

      Ns.int.uris$.get === List((1, Some(Set(uri1, uri2))), (2, None))
      Ns.int.uris.get === List((1, Set(uri1, uri2)))
    }

    "Enum" in new CoreSetup {
      Ns.int.enums$ insert Seq((1, Some(Set("enum1", "enum2"))), (2, None))

      Ns.int.enums$.get === List((1, Some(Set("enum1", "enum2"))), (2, None))
      Ns.int.enums.get === List((1, Set("enum1", "enum2")))
    }

    "Ref" in new CoreSetup {
      Ns.int.refs1$ insert Seq((1, Some(Set(3L, 4L))), (2, None))

      Ns.int.refs1$.get === List((1, Some(Set(3L, 4L))), (2, None))
      Ns.int.refs1.get === List((1, Set(3L, 4L)))
    }

    "Ref with sub components" in new CoreSetup {
      Ns.int.refsSub1$ insert Seq((1, Some(Set(3L, 4L))), (2, None))

      Ns.int.refsSub1$.get === List((1, Some(Set(3L, 4L))), (2, None))
      Ns.int.refsSub1.get === List((1, Set(3L, 4L)))
    }
  }


  "Multiple optional attributes" >> {

    "One namespace" in new CoreSetup {
      Ns.str.int$.long$ insert List(
        ("a", Some(1), Some(10L)),
        ("b", None, Some(20L)),
        ("c", Some(3), None),
        ("d", None, None))

      Ns.str.int$.long$.get === List(
        ("a", Some(1), Some(10L)),
        ("b", None, Some(20L)),
        ("c", Some(3), None),
        ("d", None, None))

      // We don't have to retrieve the attribute values in the same order as inserted
      Ns.int$.str.long$.get === List(
        (None, "d", None),
        (Some(3), "c", None),
        (None, "b", Some(20L)),
        (Some(1), "a", Some(10L)))
    }
  }


  "Ref optionals" >> {

    "Ref attribute can be optional (1)" in new CoreSetup {
      Ns.str.Ref1.str1.int1$ insert List(
        ("a", "a1", Some(11)),
        ("b", "b1", None))

      // Now there's a ref from entity with "b" to entity with "b1"
      Ns.str.Ref1.str1.int1$.get === List(
        ("a", "a1", Some(11)),
        ("b", "b1", None))
    }

    "Ref attribute can be optional (2)" in new CoreSetup {
      Ns.str.Ref1.str1$.int1 insert List(
        ("a", None, 11),
        ("b", Some("b1"), 21))

      Ns.str.Ref1.str1$.int1.get === List(
        ("a", None, 11),
        ("b", Some("b1"), 21))
      ok
    }

    "Nested attribute can be optional" in new CoreSetup {
      m(Ns.str.Refs1 * Ref1.str1.int1$) insert List(
        ("a", List(("a1", Some(11)))),
        ("b", List(("b1", None))))

      // Now there's a ref from entity with "b" to entity with "b1"
      m(Ns.str.Refs1 * Ref1.str1.int1$).get === List(
        ("a", List(("a1", Some(11)))),
        ("b", List(("b1", None))))
    }

    "Ref enum" in new CoreSetup {
      Ns.str.Ref1.str1.enum1$ insert List(
        ("a", "a1", Some("enum10")),
        ("b", "b1", None))

      Ns.str.Ref1.str1.enum1$.get === List(
        ("a", "a1", Some("enum10")),
        ("b", "b1", None))
    }

    "Nested enum" in new CoreSetup {
      m(Ns.str.Refs1 * Ref1.str1.int1$.enum1$) insert List(
        ("a", List(("a1", Some(11), None))),
        ("b", List(("b1", None, Some("enum12")))))

      m(Ns.str.Refs1 * Ref1.str1.int1$.enum1$).get === List(
        ("a", List(("a1", Some(11), None))),
        ("b", List(("b1", None, Some("enum12")))))
    }
  }


  "Ref optionals, 2 levels" >> {

    "Adjacent" in new CoreSetup {
      Ns.str.Ref1.str1$.int1.Ref2.str2.int2$ insert List(
        ("a", None, 11, "a2", Some(12)),
        ("b", Some("b1"), 21, "b2", None))

      Ns.str.Ref1.str1$.int1.Ref2.str2.int2$.get === List(
        ("b", Some("b1"), 21, "b2", None),
        ("a", None, 11, "a2", Some(12)))
    }

    "Nested" in new CoreSetup {
      m(Ns.str.Refs1 * (Ref1.str1$.int1.Refs2 * Ref2.str2.int2$)) insert List(
        ("a", List(
          (None, 11, List(
            ("a2", Some(12)))))),
        ("b", List(
          (Some("b1"), 21, List(
            ("b2", None))))))

      m(Ns.str.Refs1 * (Ref1.str1$.int1.Refs2 * Ref2.str2.int2$)).get === List(
        ("a", List(
          (None, 11, List(
            ("a2", Some(12)))))),
        ("b", List(
          (Some("b1"), 21, List(
            ("b2", None))))))
    }
  }


  "Mixing optional and tacit attributes" >> {

    "Ok in query" in new CoreSetup {
      Ns.str.int$ insert List(
        ("a", Some(1)),
        ("b", None))

      m(Ns.str_.int$).get === List(
        Some(1),
        None)
    }

    "IllegalArgumentException when inserting" in new CoreSetup {
      (m(Ns.str_.int$).insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.ops.VerifyModel.noTacitAttrs]  Tacit attributes like `str_` not allowed in insert molecules."
    }
  }


  "No attributes at all" in new CoreSetup {
    expectCompileError(
      "m(Ns)",
      """
        |[Dsl2Model:dslStructure] Unexpected DSL structure: molecule.coretests.util.dsl.coreTest.Ns
        |Select(Select(Select(Select(Select(Ident(molecule), molecule.coretests), molecule.coretests.util), molecule.coretests.util.dsl), molecule.coretests.util.dsl.coreTest), molecule.coretests.util.dsl.coreTest.Ns)
      """)
  }


  "Ns without attribute" in new CoreSetup {
    Ns.str.Ref1.int1 insert List(
      ("a", 1),
      ("b", 2))

    Ref1.int1.get === List(1, 2)

    // Adding unnecessary Ns gives same result
    Ns.Ref1.int1.get === List(1, 2)

    // First namespace without any attributes not allowed
    (m(Ns.Ref1.int1).insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[molecule.ops.VerifyModel.missingAttrInStartEnd]  Missing mandatory attributes of first namespace."

    // First namespace without any mandatory attributes not allowed
    (Ns.str$.Ref1.int1.insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[molecule.ops.VerifyModel.missingAttrInStartEnd]  Missing mandatory attributes of first namespace."

    // If at least 1 mandatory attribute is present we can have optional attributes too
    Ns.str$.int.insert(Some("a"), 1)
    Ns.str$.int.get === List((Some("a"), 1))

    Ns.int.str$.insert(2, None)
    Ns.int.str$.get === List((1, Some("a")), (2, None))

    // First namespace without any mandatory attributes not allowed
    (Ns.str_.Ref1.int1.insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[molecule.ops.VerifyModel.noTacitAttrs]  Tacit attributes like `str_` not allowed in insert molecules."

    // Last namespace without any mandatory attributes not allowed
    (Ns.str.Ref1.int1$.insert must throwA[IllegalArgumentException]).message === "Got the exception java.lang.IllegalArgumentException: " +
      "[molecule.ops.VerifyModel.missingAttrInStartEnd]  Missing mandatory attributes of last namespace."
  }


  "Only optional attributes" in new CoreSetup {

    expectCompileError(
      "m(Ns.str$)",
      "[Dsl2Model:apply (3)] Molecule is empty or has only meta/optional attributes. Please add one or more attributes.")

    expectCompileError(
      "m(Ns.str$.int$)",
      "[Dsl2Model:apply (3)] Molecule is empty or has only meta/optional attributes. Please add one or more attributes.")
  }


  "Only tacit attributes" in new CoreSetup {

    // Queries with only tacit attributes not allowed
    expectCompileError(
      "m(Ns.str_).get",
      "value get is not a member of molecule.api.Molecule0")

    expectCompileError(
      "m(Ns.str_.Ref1.int1_).get",
      "value get is not a member of molecule.api.Molecule0")
  }
}