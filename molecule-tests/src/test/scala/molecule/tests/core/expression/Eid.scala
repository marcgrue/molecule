package molecule.tests.core.expression

import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.in1_out2._
import molecule.core.ops.exception.VerifyModelException

class Eid extends Base {

  "Entity id" in new CoreSetup {

    val List(e1, e2, e3, e4) = Ns.int insert List(1, 2, 3, 4) eids
    val seq                  = Set(e1, e2)
    val set                  = Seq(e3, e4)
    val iterable             = Iterable(e3, e4)

    Ns.int.get === List(1, 2, 3, 4)

    // Single eid
    Ns(e1).int.get === List(1)

    // Vararg
    Ns(e1, e2).int.get.sorted === List(1, 2)

    // Seq
    Ns(Seq(e1, e2)).int.get.sorted === List(1, 2)
    Ns(seq).int.get.sorted === List(1, 2)

    // Set
    Ns(Set(e3, e4)).int.get.sorted === List(3, 4)
    Ns(set).int.get.sorted === List(3, 4)

    // Iterable
    Ns(Iterable(e3, e4)).int.get.sorted === List(3, 4)
    Ns(iterable).int.get.sorted === List(3, 4)
  }


  "Applied eid to namespace" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get.sorted === List(1, 2, 3)

    Ns(e1).int.get === List(1)

    Ns(e1, e2).int.get.sorted === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns(e23).int.get.sorted === List(2, 3)

    val e23s = Set(e2, e3)
    Ns(e23s).int.get.sorted === List(2, 3)
  }


  "Applied eid to `e`" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get === List(1, 2, 3)

    Ns.e(e1).int.get === List((e1, 1))
    Ns.e_(e1).int.get === List(1)

    Ns.e(e1, e2).int.get.sorted === List((e1, 1), (e2, 2))
    Ns.e_(e1, e2).int.get.sorted === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns.e(e23).int.get.sorted === List((e2, 2), (e3, 3))
    Ns.e_(e23).int.get.sorted === List(2, 3)
  }


  "Input molecule" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    val ints = m(Ns(?).int)

    ints(e1).get === List(1)

    ints(e1, e2).get.sorted === List(1, 2)

    val e23 = Seq(e2, e3)
    ints.apply(e23).get.sorted === List(2, 3)
  }


  "e" in new CoreSetup {
    val List(e1, e2) = Ns.int insert List(1, 2) eids

    Ns.e.int.get.sorted === List((e1, 1), (e2, 2))


    // Applying attribute values

    Ns.e.int_(1).get === List(e1)
    Ns.e.int_(2).get === List(e2)

    Ns.e.int(1).get === List((e1, 1))
    Ns.e.int(2).get === List((e2, 2))

    Ns.e.int_(1, 2).get.sorted === List(e1, e2)
    Ns.e.int(1, 2).get.sorted === List((e1, 1), (e2, 2))


    // Applying entity id values
    Ns.e_(e1).int.get === List(1)
    Ns.e_(e2).int.get === List(2)
    // Same semantics as
    Ns(e1).int.get === List(1)
    Ns(e2).int.get === List(2)

    Ns.e(e1).int.get.sorted === List((e1, 1))
    Ns.e(e2).int.get.sorted === List((e2, 2))

    Ns.e_(e1, e2).int.get.sorted === List(1, 2)
    Ns.e(e1, e2).int.get.sorted === List((e1, 1), (e2, 2))
  }


  "e count" in new CoreSetup {

    Ns.int.Ref1.str1 insert List(
      (1, "a"),
      (2, "b"),
      (3, "c")
    )

    Ns.e(count).int_.>(1).get.head === 2
    Ns.int_.>(1).e(count).get.head === 2
  }


  "Saving generic `e` values not allowed" in new CoreSetup {
    (Ns(42L).str("man").save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[unexpectedAppliedId]  Applying an eid is only allowed for updates."

    (Ns.e(42L).str("man").save must throwA[VerifyModelException])
      .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
      s"[unexpectedAppliedId]  Applying an eid is only allowed for updates."
  }
}