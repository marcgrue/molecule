package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.in1_out12._
import molecule.datomic.api.in3_out10.m
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Ref extends TestSpec with Helpers {

  "ref/backref" in new CoreSetup {
    Ns.int(0).str("a")
      .Ref1.int1(1).str1("b")
      .Refs2.int2(22)._Ref1
      .Ref2.int2(2).str2("c")._Ref1._Ns
      .Refs1.int1(11)
      .save

    {
      val o = Ns.int.Ref1.int1.getObj
      o.int === 0
      o.Ref1.int1 === 1
    }
    {
      val o = Ns.int.Ref1.int1._Ns.str.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.str === "a"
    }
    {
      val o = Ns.int.Ref1.int1._Ns.Refs1.int1.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Refs1.int1 === 11
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1.str1.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.Ref1.str1 === "b"
    }
    {
      val o = Ns.int.Ref1.Ref2.int2._Ref1.int1.getObj
      o.int === 0
      o.Ref1.Ref2.int2 === 2
      o.Ref1.int1 === 1
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1.Refs2.int2.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.Ref1.Refs2.int2 === 22
    }
    {
      val o = Ns.int.Ref1.Ref2.int2._Ref1.Refs2.int2.getObj
      o.int === 0
      o.Ref1.Ref2.int2 === 2
      o.Ref1.Refs2.int2 === 22
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1.str1._Ns.str.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.Ref1.str1 === "b"
      o.str === "a"
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1._Ns.str.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.str === "a"
    }
    {
      val o = Ns.int.Ref1.Ref2.int2._Ref1._Ns.str.getObj
      o.int === 0
      o.Ref1.Ref2.int2 === 2
      o.str === "a"
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1.str1._Ns.Refs1.int1.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.Ref1.str1 === "b"
      o.Refs1.int1 === 11
    }
    {
      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1._Ns.Refs1.int1.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.Ref1.Ref2.int2 === 2
      o.Refs1.int1 === 11
    }
    {
      val o = Ns.int.Ref1.Ref2.int2._Ref1._Ns.Refs1.int1.getObj
      o.int === 0
      o.Ref1.Ref2.int2 === 2
      o.Refs1.int1 === 11
    }
    {
      val o = Ns.Ref1.Ref2.int2._Ref1._Ns.Refs1.int1.getObj
      o.Ref1.Ref2.int2 === 2
      o.Refs1.int1 === 11
    }
  }


  "Tacit orphans" in new CoreSetup {
    // Only add ref if there are any props to ref
    // (we can't be selective in the molecule type buildup, so the "empty refs" have to remain)
    Ns.int(0).Ref1.int1(1).save
    val o = Ns.int.Ref1.int1_.getObj
    o.int === 0
    o.Ref1 // tacit int1_ returns no value and thus has no object property
  }


  "Nested" in new CoreSetup {
    m(Ns.int.Refs1 * Ref1.int1.str1$) insert List(
      (1, List((11, Some("a")), (12, None))),
      (2, List())
    )

    val List(o1, o2) = (Ns.int.Refs1 *? Ref1.int1.str1$).getObjList
    o1.int === 1
    o1.Refs1.head.int1 === 11
    o1.Refs1.head.str1$ === Some("a")
    o1.Refs1.last.int1 === 12
    o1.Refs1.last.str1$ === None

    o2.int === 2
    o2.Refs1 === Nil
  }


  "Composites" in new CoreSetup {
    // All properties are namespaced
    {
      (Ns.int(1) + Ref1.int1(1) + Ref2.int2(1)).save
      val o = m(Ns.int + Ref1.int1 + Ref2.int2).getObj
      o.Ns.int === 1
      o.Ref1.int1 === 1
      o.Ref2.int2 === 1
    }
    {
      (Ns.int(2).Ref1.int1(1) + Ref2.int2(2)).save
      val o = m(Ns.int.Ref1.int1 + Ref2.int2).getObj
      o.Ns.int === 2
      o.Ns.Ref1.int1 === 1
      o.Ref2.int2 === 2
    }
    {
      (Ns.int(3) + Ref1.int1(3).Ref2.int2(3)).save
      val o = m(Ns.int + Ref1.int1.Ref2.int2).getObj
      o.Ns.int === 3
      o.Ref1.int1 === 3
      o.Ref1.Ref2.int2 === 3
    }
  }


  "Multiple same-name ns composites" in new CoreSetup {
    (Ns.int + Ns.float.str + Ref1.int1.str1).insert(1, (2f, "a"), (3, "b"))

    // Multiple same-name namespace composites need ++ to allow access to object interface
    // Can't access object properties from same-name namespace composites
    (m(Ns.int + Ns.float.str + Ref1.int1.str1).getObj must throwA[molecule.core.exceptions.MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      s"Please compose multiple same-name namespaces with `++` instead of `+` to access object properties."

    val o = m(Ns.int ++ Ns.float.str + Ref1.int1.str1).getObj
    o.Ns.int === 1
    o.Ns.float === 2f
    o.Ns.str === "a"
    o.Ref1.int1 === 3
    o.Ref1.str1 === "b"

    // Multiple same-name namespace composites behaves equally for tuple output
    m(Ns.int + Ns.float.str + Ref1.int1.str1).get.head === (1, (2f, "a"), (3, "b"))
    m(Ns.int ++ Ns.float.str + Ref1.int1.str1).get.head === (1, (2f, "a"), (3, "b"))
  }


  "Self-joins" in new SelfJoinSetup {
    import molecule.tests.core.ref.dsl.SelfJoin._
    m(Person.age.name.Likes * Score.beverage.rating) insert List(
      (23, "Joe", List(("Coffee", 3), ("Cola", 2), ("Pepsi", 3))),
      (25, "Ben", List(("Coffee", 2), ("Tea", 3))),
      (23, "Liz", List(("Coffee", 1), ("Tea", 3), ("Pepsi", 1))))

    {
      val List(p1, p2, p3) = Person
        .age_(23).name.Likes.beverage._Person.Self
        .age_(25).name.Likes.beverage_(unify).getObjList

      p1.name === "Liz"
      p1.Likes.beverage === "Coffee"
      // Self-join to other Person (same namespace)
      p1.Person.name === "Ben"

      p2.name === "Joe"
      p2.Likes.beverage === "Coffee"
      p2.Person.name === "Ben"

      p3.name === "Liz"
      p3.Likes.beverage === "Tea"
      p3.Person.name === "Ben"
    }

    {
      val p = Person
        .name("Joe").Likes.beverage._Person.Self
        .name("Ben").Likes.beverage_(unify)._Person.Self
        .name("Liz").Likes.beverage_(unify).getObj

      p.name === "Joe"
      p.Likes.beverage === "Coffee"
      // Self-join to other Person (Ben) in same namespace
      p.Person.name === "Ben"
      // Self-join from self-joined person (Ben) to other Person (Liz) in same namespace
      p.Person.Person.name === "Liz"
    }
  }

}
