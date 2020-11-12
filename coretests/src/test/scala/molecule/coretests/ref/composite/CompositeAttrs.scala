package molecule.coretests.ref.composite

import molecule.core.util.expectCompileError
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.datomic.api.out6._

class CompositeAttrs extends CoreSpec {

  "tacits" in new CoreSetup {

    Ref2.int2.str2$ + Ref1.int1.str1$ + Ns.int.str$ insert Seq(
      ((1, Some("a")), (11, Some("aa")), (111, Some("aaa"))),
      ((2, Some("b")), (22, Some("bb")), (222, None)),
      ((3, Some("c")), (33, None), (333, None)),
      ((4, None), (44, None), (444, None)),
    )


    // Same namespace

    m(Ref2.int2.str2).get.sorted === List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )
    // When 1 + 1 attribute, this outcome will be the same
    m(Ref2.int2 + Ref2.str2).get.sorted === List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )

    m(Ref2.int2).get.sorted === List(1, 2, 3, 4)

    m(Ref2.int2 + Ref2.str2_).get.sorted === List(1, 2, 3)
    // Order irrelevant
    m(Ref2.str2_ + Ref2.int2).get.sorted === List(1, 2, 3)

    m(Ref1.int1 + Ref1.str1_).get.sorted === List(11, 22)

    m(Ns.int + Ns.str_).get === List(111)


    // 2 namespaces, 1 tacit

    m(Ref2.int2 + Ref1.str1_).get.sorted === List(1, 2)
    m(Ref2.int2 + Ns.str_).get === List(1)
    m(Ref1.int1 + Ns.str_).get === List(11)


    // 3 namespaces, 2 tacits

    m(Ref2.int2 + Ref1.str1_ + Ns.str_).get === List(1)
    m(Ref2.str2_ + Ref1.int1 + Ns.str_).get === List(11)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int).get.sorted === List(111, 222)


    // 3 namespaces, 3 tacits, 4 composite parts (to test second `+` method)

    m(Ref2.int2 + Ref1.str1_ + Ns.int_ + Ns.str_).get === List(1)
    m(Ref2.str2_ + Ref1.int1 + Ns.int_ + Ns.str_).get === List(11)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int + Ns.str_).get === List(111)
    m(Ref2.str2_ + Ref1.str1_ + Ns.int_ + Ns.str).get === List("aaa")
  }



  "Duplicate attributes" >> {

    "Duplicate attrs/refs with same entity on top-level not allowed" in new CoreSetup {

      // 1 + 1 attr
      expectCompileError(
        "m(Ns.int + Ns.int)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:Ns/int`")

      // 0 + 2 attr
      expectCompileError(
        "m(Ns.int + Ns.str.str)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:Ns/str`")

      // 1 + 1 ref
      expectCompileError(
        "m(Ns.bool.Ref1.int1 + Ns.str.Ref1.int1)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite molecules can't contain the same ref more than once. Found multiple instances of `:Ns/ref1`")

      // 0 + 2 attr after backref
      expectCompileError(
        "m(Ns.int + Ref1.int1.Ref2.int2._Ref1.int1)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite molecules can't contain the same attribute more than once. Found multiple instances of `:Ref1/int1`")

      // 0 + 2 ref after backref
      expectCompileError(
        "m(Ns.int + Ref1.int1.Ref2.int2._Ref1.str1.Ref2.str2)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite molecules can't contain the same ref more than once. Found multiple instances of `:Ref1/ref2`")

      ok
    }


    "Duplicate attrs/refs with same entity within sub-molecule not allowed" in new CoreSetup {

      // 2 attr
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.int1)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:Ref1/int1`")

      // 2 attr after backref
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.Ref2.int2._Ref1.int1)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite sub-molecules can't contain the same attribute more than once. Found multiple instances of `:Ref1/int1`")

      // 2 ref
      expectCompileError(
        "m(Ref1.int1 + Ns.int.Ref1.int1.Ref2.int2._Ref1.str1.Ref2.str2)",
        "molecule.core.ops.exception.VerifyRawModelException: " +
          "Composite sub-molecules can't contain the same ref more than once. Found multiple instances of `:Ref1/ref2`")

      ok
    }


    "Twice on different levels ok" in new CoreSetup {

      // Okay to repeat attribute in _referenced_ namespace

      val List(e1, r11, e2, r22) = Ref1.int1 + Ns.str.Ref1.int1 insert Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      ) eids

      m(Ns.str).get.sorted === Seq("aa", "bb")
      m(Ns.str.Ref1.int1).get.sorted === Seq(("aa", 11), ("bb", 22))

      // Note how 4 Ref1.int1 values have been inserted!
      m(Ref1.int1).get.sorted === Seq(1, 2, 11, 22)

      // Composite query
      m(Ref1.int1 + Ns.str.Ref1.int1).get.sorted === Seq(
        (1, ("aa", 11)),
        (2, ("bb", 22))
      )
    }
  }
}