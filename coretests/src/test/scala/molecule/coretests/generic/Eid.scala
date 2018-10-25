package molecule.coretests.generic

import molecule.api.in1_out4._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.ops.exception.VerifyModelException

class Eid extends CoreSpec {


  "Applied eid to namespace" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get === List(1, 2, 3)

    Ns(e1).int.get === List(1)

    Ns(e1, e2).int.get === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns(e23).int.get === List(2, 3)

    val e23s = Set(e2, e3)
    Ns(e23s).int.get === List(2, 3)
  }


  "Applied eid to `e`" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    Ns.int.get === List(1, 2, 3)

    Ns.e(e1).int.get === List((e1, 1))
    Ns.e_(e1).int.get === List(1)

    Ns.e(e1, e2).int.get === List((e2, 2), (e1, 1))
    Ns.e_(e1, e2).int.get === List(1, 2)

    val e23 = Seq(e2, e3)
    Ns.e(e23).int.get === List((e2, 2), (e3, 3))
    Ns.e_(e23).int.get === List(2, 3)
  }


  "Input molecule" in new CoreSetup {

    val List(e1, e2, e3) = Ns.int.insert(1, 2, 3).eids

    val ints = m(Ns(?).int)

    ints(e1).get === List(1)

    ints(e1, e2).get === List(1, 2)

    val e23 = Seq(e2, e3)
    ints.apply(e23).get === List(2, 3)
  }


  "e" in new CoreSetup {
    val List(e1, e2) = Ns.int insert List(1, 2) eids

    Ns.e.int.get === List((e1, 1), (e2, 2))


    // Applying attribute values

    Ns.e.int_(1).get === List(e1)
    Ns.e.int_(2).get === List(e2)

    Ns.e.int(1).get === List((e1, 1))
    Ns.e.int(2).get === List((e2, 2))

    Ns.e.int_(1, 2).get === List(e1, e2)
    Ns.e.int(1, 2).get === List((e1, 1), (e2, 2))


    // Applying entity id values
    Ns.e_(e1).int.get === List(1)
    Ns.e_(e2).int.get === List(2)
    // Same semantics as
    Ns(e1).int.get === List(1)
    Ns(e2).int.get === List(2)

    Ns.e(e1).int.get === List((e1, 1))
    Ns.e(e2).int.get === List((e2, 2))

    Ns.e_(e1, e2).int.get === List(1, 2)
    Ns.e(e1, e2).int.get === List((e2, 2), (e1, 1))
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

    (Ns(42L).str("man").save must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[unexpectedAppliedId]  " +
      s"""Can't save molecule with an applied eid as in `Ns(eid)`. Applying an eid is for updates, like `Ns(johnId).likes("pizza").update`"""

    (Ns.e(42L).str("man").save must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
      s"[unexpectedAppliedId]  " +
      s"""Can't save molecule with an applied eid as in `Ns(eid)`. Applying an eid is for updates, like `Ns(johnId).likes("pizza").update`"""
  }


  "Saving generic `ns` values not allowed" in new CoreSetup {

    val eid = Ns.str("a").save.eid

    m(Ns.e.str("a")).get === List((eid, "a"))
    m(Ns.ns.str("a")).get === List(("ns", "a"))

    // todo
    //    (Ns.ns("hi").str("man").save must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
    //      s"""[noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` not allowed in save molecules. Found `a("hej")`"""

    //    (Ns.str("man").Ref1.ns("hi").save must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
    //      s"""[noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` not allowed in save molecules. Found `a("hej")`"""
    //
    //    (Ns.str("man").Ref1.a("hi").save must throwA[VerifyModelException]).message === "Got the exception molecule.ops.exception.VerifyModelException: " +
    //      s"""[noGenerics]  Generic elements `e`, `a`, `v`, `ns`, `tx`, `t`, `txInstant` and `op` not allowed in save molecules. Found `a("hej")`"""

    //    Ns.str("man").Ref1.a("hej").save
    //    Ns.str("man").Ref1.v("hej").save
    //    Ns.str("man").Ref1.ns("hej").save
    //    Ns.str("man").Ref1.tx(42).save
    //    Ns.str("man").Ref1.t(43L).save
    //    val now = new java.util.Date()
    //    Ns.str("man").Ref1.txInstant(now).save
    //    Ns.str("man").Ref1.op(true).save
  }

}