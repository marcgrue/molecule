package molecule.coretests.ref

import molecule.core.ops.exception.VerifyModelException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.out4._

class Relations extends CoreSpec {

  "One-to-One" in new CoreSetup {

    // Creating 3 entities referencing 3 other entities
    val List(
    a0, a1,
    b0, b1,
    c0, c1
    ) = Ns.str.Ref1.str1 insert List(
      ("a0", "a1"),
      ("b0", "b1"),
      ("c0", "c1")) eids

    // Get attribute values from 2 namespaces
    // Namespace references like `Ref1` starts with Capital letter
    Ns.str.Ref1.str1.get.sorted === List(
      ("a0", "a1"),
      ("b0", "b1"),
      ("c0", "c1")
    )

    // We can also retrieve the referenced entity id
    // Referenced entity id `ref1` starts with lower case letter
    Ns.str.ref1.get.sorted === List(
      ("a0", a1),
      ("b0", b1),
      ("c0", c1))
  }


  "Referenced entity ids" in new CoreSetup {

    val List(father1, father2, father3) = Ref1.str1 insert List("father1", "father2", "father3") eids

    // We can insert ref entity ids
    Ns.str.ref1 insert List(
      ("kid1", father1),
      ("kid2", father2),
      ("kid3", father3)
    )

    // Get attribute values from 2 namespaces
    // Namespace references like `Ref1` starts with Capital letter
    Ns.str.Ref1.str1.get.sorted === List(
      ("kid1", "father1"),
      ("kid2", "father2"),
      ("kid3", "father3")
    )

    // We can also retrieve the referenced entity id
    // Referenced entity id `ref1` starts with lower case letter
    Ns.str.ref1.get.sorted === List(
      ("kid1", father1),
      ("kid2", father2),
      ("kid3", father3))
  }


  "Referenced entity ids" in new CoreSetup {
    val id = Ns.str("a").save.eid

    // Avoid mixing update/save semantics
    (Ns(id).Refs1.int1(1).save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      "[unexpectedAppliedId]  Applying an eid is only allowed for updates."

    // Updating across namespaces not allowed

    (Ns(id).Refs1.int1(1).update must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      "[update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."
  }


  "Enum" in new CoreSetup {
    Ns.str.enum insert List(("a", "enum0"))

    Ns.str.enum.get === List(("a", "enum0"))
  }

  "Ref enum after ref" in new CoreSetup {
    Ns.str.Ref1.enum1 insert List(("b", "enum10"))
    Ns.str.Ref1.enum1.get === List(("b", "enum10"))
  }

  "Ref enum after attr" in new CoreSetup {
    Ns.str.Ref1.int1.enum1 insert List(("c", 11, "enum11"))
    Ns.str.Ref1.int1.enum1.get === List(("c", 11, "enum11"))
  }

  "BackRef" in new CoreSetup {
    Ns.str.Ref1.str1._Ns.Refs1.int1 insert List(
      ("a", "a1", 1),
      ("b", "b1", 2))

    Ns.str.Ref1.str1._Ns.Refs1.int1.get === List(
      ("a", "a1", 1),
      ("b", "b1", 2))

    Ns.str.Refs1.int1._Ns.Ref1.str1.get === List(
      ("a", 1, "a1"),
      ("b", 2, "b1"))
  }

  "BackRef, 2 levels" in new CoreSetup {
    Ns.str.Ref1.str1.Ref2.str2._Ref1._Ns.Refs1.int1 insert List(
      ("a", "a1", "a2", 1),
      ("b", "b1", "b2", 2))

    Ns.str.Ref1.str1.Ref2.str2._Ref1._Ns.Refs1.int1.get === List(
      ("a", "a1", "a2", 1),
      ("b", "b1", "b2", 2))
  }


  "Back ref, Adjacent" in new CoreSetup {
    m(Ns.str.Ref1.str1._Ns.Refs1.str1) insert List(("book", "John", "Marc"))

    m(Ns.str.Ref1.str1._Ns.Refs1.str1).get === List(("book", "John", "Marc"))
    m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).get === List(("book", "John", List("Marc")))
  }

  "Adjacent ref without attribute" in new CoreSetup {
    Ns.str.Ref1.int1.Ref2.int2 insert List(
      ("a", 11, 12),
      ("b", 21, 22))

    // We can jump from namespace to namespace in queries
    Ns.str.Ref1.Ref2.int2.get === List(
      ("b", 22),
      ("a", 12))
  }

  "Expecting mandatory attributes after card-one ref" in new CoreSetup {
    m(Ns.str.Ref1.int1) insert List(
      ("a", 1),
      ("b", 2))

    // Ok to ask for an optional referenced value
    m(Ns.str.Ref1.int1$).get.sortBy(_._1) === List(
      ("a", Some(1)),
      ("b", Some(2)))

    // But in insert molecules we don't want to create referenced orphan entities
    (m(Ns.str.Ref1.int1$).insert must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      "[missingAttrInStartEnd]  Missing mandatory attributes of last namespace."
  }

  "Aggregates one" in new CoreSetup {
    m(Ns.str.ref1) insert List(
      ("a", 1L),
      ("b", 2L))

    m(Ns.str.ref1(count)).get === List(
      ("a", 1),
      ("b", 1))
  }

  "Aggregates many" in new CoreSetup {
    m(Ns.str.refs1) insert List(
      ("a", Set(1L)),
      ("b", Set(2L, 3L)))

    m(Ns.str.refs1(count)).get === List(
      ("a", 1),
      ("b", 2))
  }

  "Self-refs" in new CoreSetup {
    // OBS: not considered "Self-joins" in this context
    m(Ns.str.Parent.str) insert List(("child", "parent"))
    m(Ns.str.Parent.str).get === List(("child", "parent"))

    m(Ns.str.Parents * Ns.str) insert List(("child", List("parent1", "parent2")))
    m(Ns.str.Parents * Ns.str).get === List(("child", List("parent1", "parent2")))
    m(Ns.str.Parents.str).get === List(("child", "parent1"), ("child", "parent2"))
  }

  "Many attribute + ref" in new CoreSetup {
    m(Ns.str.Refs1.*(Ref1.int1)) insert List(("a", List(1, 2)))

    expectCompileError(
      "m(Ns.str.refs1.Refs1.int1)",
      "molecule.core.ops.exception.VerifyRawModelException: Instead of getting the ref id with `refs1` please get it via the referenced namespace: `Refs1.e ...`")

    expectCompileError(
      "m(Ns.refs1.str.Refs1.int1)",
      "molecule.core.ops.exception.VerifyRawModelException: Instead of getting the ref id with `refs1` please get it via the referenced namespace: `Refs1.e ...`")
  }

  "Molecule has to end with attribute" >> {

    "Ending with ref" in new CoreSetup {
      expectCompileError(
        "m(Ns.str.Ref1)",
        "molecule.core.ops.exception.VerifyRawModelException: Molecule not allowed to end with a reference. Please add one or more attribute to the reference.")
      ok
    }

    "Ending with refs" in new CoreSetup {

      expectCompileError(
        "m(Ns.str.Refs1)",
        "molecule.core.ops.exception.VerifyRawModelException: Molecule not allowed to end with a reference. Please add one or more attribute to the reference.")
      ok
    }
  }
}