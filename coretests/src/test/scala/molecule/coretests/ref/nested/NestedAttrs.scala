package molecule.coretests.ref.nested

import molecule.api.in3_out4._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._


class NestedAttrs extends CoreSpec {


  "card 1" >> {

    "attr" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.str1$) insert List(
        (1, List((1, Some("a")), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.str1$).get === List(
        (1, List((1, Some("a")), (2, None))),
      )
      m(Ns.int.Refs1 * Ref1.int1.str1).get === List(
        (1, List((1, "a"))),
      )
      m(Ns.int.Refs1 * Ref1.int1.str1_).get === List(
        (1, List(1)),
      )

      m(Ns.int.Refs1 *? Ref1.int1.str1$).get.sortBy(_._1) === List(
        (1, List((1, Some("a")), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.str1).get.sortBy(_._1) === List(
        (1, List((1, "a"))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.str1_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }


    "enum" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.enum1$) insert List(
        (1, List((1, Some("enum10")), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.enum1$).get === List(
        (1, List((1, Some("enum10")), (2, None)))
      )
      m(Ns.int.Refs1 * Ref1.int1.enum1).get === List(
        (1, List((1, "enum10")))
      )
      m(Ns.int.Refs1 * Ref1.int1.enum1_).get === List(
        (1, List(1))
      )

      m(Ns.int.Refs1 *? Ref1.int1.enum1$).get.sortBy(_._1) === List(
        (1, List((1, Some("enum10")), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.enum1).get.sortBy(_._1) === List(
        (1, List((1, "enum10"))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.enum1_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }


    "ref" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.ref2$) insert List(
        (1, List((1, Some(42L)), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.ref2$).get === List(
        (1, List((1, Some(42L)), (2, None)))
      )
      m(Ns.int.Refs1 * Ref1.int1.ref2).get === List(
        (1, List((1, 42L)))
      )
      m(Ns.int.Refs1 * Ref1.int1.ref2_).get === List(
        (1, List(1))
      )

      m(Ns.int.Refs1 *? Ref1.int1.ref2$).get.sortBy(_._1) === List(
        (1, List((1, Some(42L)), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.ref2).get.sortBy(_._1) === List(
        (1, List((1, 42L))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.ref2_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }
  }


  "card 2" >> {

    "attr" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.strs1$) insert List(
        (1, List((1, Some(Set("a", "b"))), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.strs1$).get === List(
        (1, List((1, Some(Set("a", "b"))), (2, None))),
      )
      m(Ns.int.Refs1 * Ref1.int1.strs1).get === List(
        (1, List((1, Set("a", "b")))),
      )
      m(Ns.int.Refs1 * Ref1.int1.strs1_).get === List(
        (1, List(1)),
      )

      m(Ns.int.Refs1 *? Ref1.int1.strs1$).get.sortBy(_._1) === List(
        (1, List((1, Some(Set("a", "b"))), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.strs1).get.sortBy(_._1) === List(
        (1, List((1, Set("a", "b")))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.strs1_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }


    "enum" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.enums1$) insert List(
        (1, List((1, Some(Set("enum10", "enum11"))), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.enums1$).get === List(
        (1, List((1, Some(Set("enum10", "enum11"))), (2, None)))
      )
      m(Ns.int.Refs1 * Ref1.int1.enums1).get === List(
        (1, List((1, Set("enum10", "enum11"))))
      )
      m(Ns.int.Refs1 * Ref1.int1.enums1_).get === List(
        (1, List(1))
      )

      m(Ns.int.Refs1 *? Ref1.int1.enums1$).get.sortBy(_._1) === List(
        (1, List((1, Some(Set("enum10", "enum11"))), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.enums1).get.sortBy(_._1) === List(
        (1, List((1, Set("enum10", "enum11")))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.enums1_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }


    "ref" in new CoreSetup {

      m(Ns.int.Refs1 * Ref1.int1.refs2$) insert List(
        (1, List((1, Some(Set(42L, 43L))), (2, None))),
        (2, List())
      )

      m(Ns.int.Refs1 * Ref1.int1.refs2$).get === List(
        (1, List((1, Some(Set(42L, 43L))), (2, None)))
      )
      m(Ns.int.Refs1 * Ref1.int1.refs2).get === List(
        (1, List((1, Set(42L, 43L))))
      )
      m(Ns.int.Refs1 * Ref1.int1.refs2_).get === List(
        (1, List(1))
      )

      m(Ns.int.Refs1 *? Ref1.int1.refs2$).get.sortBy(_._1) === List(
        (1, List((1, Some(Set(42L, 43L))), (2, None))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.refs2).get.sortBy(_._1) === List(
        (1, List((1, Set(42L, 43L)))),
        (2, List())
      )
      m(Ns.int.Refs1 *? Ref1.int1.refs2_).get.sortBy(_._1) === List(
        (1, List(1)),
        (2, List())
      )
    }
  }


  "card 3 (map)" in new CoreSetup {

    m(Ns.int.Refs1 * Ref1.int1.intMap1$) insert List(
      (1, List((1, Some(Map("a" -> 1, "b" -> 2))), (2, None))),
      (2, List())
    )

    m(Ns.int.Refs1 * Ref1.int1.intMap1$).get === List(
      (1, List((1, Some(Map("a" -> 1, "b" -> 2))), (2, None))),
    )
    m(Ns.int.Refs1 * Ref1.int1.intMap1).get === List(
      (1, List((1, Map("a" -> 1, "b" -> 2)))),
    )
    m(Ns.int.Refs1 * Ref1.int1.intMap1_).get === List(
      (1, List(1)),
    )

    m(Ns.int.Refs1 *? Ref1.int1.intMap1$).get.sortBy(_._1) === List(
      (1, List((1, Some(Map("a" -> 1, "b" -> 2))), (2, None))),
      (2, List())
    )
    m(Ns.int.Refs1 *? Ref1.int1.intMap1).get.sortBy(_._1) === List(
      (1, List((1, Map("a" -> 1, "b" -> 2)))),
      (2, List())
    )
    m(Ns.int.Refs1 *? Ref1.int1.intMap1_).get.sortBy(_._1) === List(
      (1, List(1)),
      (2, List())
    )
  }


  "Post attributes after nested" in new CoreSetup {

    Ns.float.str.Refs1.*(Ref1.int1).insert(1f, "a", Seq(11, 12))

    Ns.float.str.Refs1.*(Ref1.int1).get.head === (1f, "a", Seq(11, 12))

    Ns.float.Refs1.*(Ref1.int1)._Ns.str.get.head === (1f, Seq(11, 12), "a")
  }


  "Applied eid" in new CoreSetup {
    val eid = Ns.str.Refs1.*(Ref1.int1).insert("a", List(1, 2)).eid
    Ns(eid).str.get.head === "a"
    Ns(eid).Refs1.*(Ref1.int1).get.head === List(1, 2)
  }
}