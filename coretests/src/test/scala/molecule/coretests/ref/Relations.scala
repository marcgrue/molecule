//package molecule.coretests.ref
//
//import molecule.Imports._
//
//import molecule.coretests.util.dsl.coreTest._
//import molecule.coretests.util.{CoreSetup, CoreSpec}
//import molecule.util.expectCompileError
//
//class Relations extends CoreSpec {
//
//  "One-to-One" in new CoreSetup {
//
//    // Creating 3 entities referencing 3 other entities
//    val List(
//    a0, a1,
//    b0, b1,
//    c0, c1
//    ) = Ns.str.Ref1.str1 insert List(
//      ("a0", "a1"),
//      ("b0", "b1"),
//      ("c0", "c1")) eids
//
//    // Get attribute values from 2 namespaces
//    // Namespace references like `Ref1` starts with Capital letter
//    Ns.str.Ref1.str1.get.toSeq.sorted === List(
//      ("a0", "a1"),
//      ("b0", "b1"),
//      ("c0", "c1")
//    )
//
//    // We can also retrieve the referenced entity id
//    // Referenced entity id `ref1` starts with lower case letter
//    Ns.str.ref1.get.toSeq.sorted === List(
//      ("a0", a1),
//      ("b0", b1),
//      ("c0", c1))
//  }
//
//
//  "Referenced entity ids" in new CoreSetup {
//
//    val List(father1, father2, father3) = Ref1.str1 insert List("father1", "father2", "father3") eids
//
//    // We can insert ref entity ids
//    Ns.str.ref1 insert List(
//      ("kid1", father1),
//      ("kid2", father2),
//      ("kid3", father3)
//    )
//
//    // Get attribute values from 2 namespaces
//    // Namespace references like `Ref1` starts with Capital letter
//    Ns.str.Ref1.str1.get.toSeq.sorted === List(
//      ("kid1", "father1"),
//      ("kid2", "father2"),
//      ("kid3", "father3")
//    )
//
//    // We can also retrieve the referenced entity id
//    // Referenced entity id `ref1` starts with lower case letter
//    Ns.str.ref1.get.toSeq.sorted === List(
//      ("kid1", father1),
//      ("kid2", father2),
//      ("kid3", father3))
//  }
//
//
//  "Referenced entity ids" in new CoreSetup {
//    val id = Ns.str("a").save.eid
//
//    // Avoid mixing update/save semantics
//    (Ns(id).Refs1.int1(1).save must throwA[IllegalArgumentException])
//      .message === "Got the exception java.lang.IllegalArgumentException: " +
//      "[molecule.api.CheckModel.unexpectedAppliedId]  Can't save molecule with an applied eid as in `Ns(eid)`. " +
//      """Applying an eid is for updates, like `Ns(johnId).likes("pizza").update`"""
//
//    // Updating across namespaces not allowed
//
//    (Ns(id).Refs1.int1(1).update must throwA[IllegalArgumentException])
//      .message === "Got the exception java.lang.IllegalArgumentException: " +
//      "[molecule.api.CheckModel.update_onlyOneNs]  Update molecules can't span multiple namespaces like `Ref1`."
//  }
//
//
//  "Enum" in new CoreSetup {
//    Ns.str.enum insert List(("a", "enum0"))
//
//    Ns.str.enum.get === List(("a", "enum0"))
//  }
//
//  "Ref enum after ref" in new CoreSetup {
//    Ns.str.Ref1.enum1 insert List(("b", "enum10"))
//    Ns.str.Ref1.enum1.get === List(("b", "enum10"))
//  }
//
//  "Ref enum after attr" in new CoreSetup {
//    Ns.str.Ref1.int1.enum1 insert List(("c", 11, "enum11"))
//    Ns.str.Ref1.int1.enum1.get === List(("c", 11, "enum11"))
//  }
//
//  "BackRef" in new CoreSetup {
//    Ns.str.Ref1.str1._Ns.Refs1.int1 insert List(
//      ("a", "a1", 1),
//      ("b", "b1", 2))
//
//    Ns.str.Ref1.str1._Ns.Refs1.int1.get === List(
//      ("a", "a1", 1),
//      ("b", "b1", 2))
//
//    Ns.str.Refs1.int1._Ns.Ref1.str1.get === List(
//      ("a", 1, "a1"),
//      ("b", 2, "b1"))
//  }
//
//
//  "Back ref, Adjacent" in new CoreSetup {
//    m(Ns.str.Ref1.str1._Ns.Refs1.str1) insert List(("book", "John", "Marc"))
//
//    m(Ns.str.Ref1.str1._Ns.Refs1.str1).get === List(("book", "John", "Marc"))
//    m(Ns.str.Ref1.str1._Ns.Refs1 * Ref1.str1).get === List(("book", "John", List("Marc")))
//  }
//
//  "Adjacent ref without attribute" in new CoreSetup {
//    Ns.str.Ref1.int1.Ref2.int2 insert List(
//      ("a", 11, 12),
//      ("b", 21, 22))
//
//    // We can jump from namespace to namespace in queries
//    Ns.str.Ref1.Ref2.int2.get === List(
//      ("b", 22),
//      ("a", 12))
//  }
//
//  "Expecting mandatory attributes after card-one ref" in new CoreSetup {
//    m(Ns.str.Ref1.int1) insert List(
//      ("a", 1),
//      ("b", 2))
//
//    // Ok to ask for an optional referenced value
//    m(Ns.str.Ref1.int1$).get === List(
//      ("a", Some(1)),
//      ("b", Some(2)))
//
//    // But in insert molecules we don't want to create referenced orphan entities
//    (m(Ns.str.Ref1.int1$).insert must throwA[IllegalArgumentException])
//      .message === "Got the exception java.lang.IllegalArgumentException: " +
//      "[molecule.api.CheckModel.missingAttrInStartEnd]  Missing mandatory attributes of last namespace."
//  }
//
//  "Aggregates one" in new CoreSetup {
//    m(Ns.str.ref1) insert List(
//      ("a", 1L),
//      ("b", 2L))
//
//    m(Ns.str.ref1(count)).get === List(
//      ("a", 1),
//      ("b", 1))
//  }
//
//  "Aggregates many" in new CoreSetup {
//    m(Ns.str.refs1) insert List(
//      ("a", Set(1L)),
//      ("b", Set(2L, 3L)))
//
//    m(Ns.str.refs1(count)).get === List(
//      ("a", 1),
//      ("b", 2))
//  }
//
//  "Self-refs" in new CoreSetup {
//    // OBS: not considered "Self-joins" in this context
//    m(Ns.str.Parent.str) insert List(("child", "parent"))
//    m(Ns.str.Parent.str).get === List(("child", "parent"))
//
//    m(Ns.str.Parents * Ns.str) insert List(("child", List("parent1", "parent2")))
//    m(Ns.str.Parents * Ns.str).get === List(("child", List("parent1", "parent2")))
//    m(Ns.str.Parents.str).get === List(("child", "parent1"), ("child", "parent2"))
//  }
//
//  "Many attribute + ref" in new CoreSetup {
//    m(Ns.str.Refs1.*(Ref1.int1)) insert List(("a", List(1, 2)))
//
//    expectCompileError(
//      "m(Ns.str.refs1.Refs1.int1)",
//      "[Dsl2Model:apply (10)] Instead of getting the ref id with `refs1` please get it via the referenced namespace: `Ref1.e ...`")
//
//    expectCompileError(
//      "m(Ns.refs1.str.Refs1.int1)",
//      "[Dsl2Model:apply (10)] Instead of getting the ref id with `refs1` please get it via the referenced namespace: `Ref1.e ...`")
//  }
//
//  "Molecule has to end with attribute" >> {
//
//    "Ending with ref" in new CoreSetup {
//
//      expectCompileError(
//        "m(Ns.Ref1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//
//      expectCompileError(
//        "m(Ns.str.Ref1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//
//      expectCompileError(
//        "m(Ns.str_.Ref1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//      ok
//    }
//
//    "Ending with refs" in new CoreSetup {
//
//      expectCompileError(
//        "m(Ns.Refs1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//
//      expectCompileError(
//        "m(Ns.str.Refs1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//
//      expectCompileError(
//        "m(Ns.str_.Refs1)",
//        "[Dsl2Model:apply (1)] Molecule not allowed to end with a reference. Please add a one or more attribute to the reference.")
//      ok
//    }
//  }
//}