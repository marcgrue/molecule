package molecule.tests.core.crud

import molecule.core.util.DatomicPeer
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out3._
import molecule.TestSpec
import scala.concurrent.ExecutionContext.Implicits.global


class UpdateMultipleEntities extends TestSpec {

  "Async" in new CoreSetup {

    // todo: remove when async implemented for other systems
    if (system == DatomicPeer) {

      // Update multiple entities asynchronously and return Future[TxReport]
      // Calls Datomic's transactAsync API

      // Initial data
      Ns.str.int insertAsync List(
        ("a", 1),
        ("b", 2),
        ("c", 3),
        ("d", 4)
      ) map { tx => // tx report from successful insert transaction
        // 4 inserted entities
        val List(a, b, c, d) = tx.eids
        Ns.int.get === List(
          ("a", 1),
          ("b", 2),
          ("c", 3),
          ("d", 4)
        )

        // Update multiple entities asynchronously
        Ns(a, b).int(5).updateAsync.map { tx2 => // tx report from successful update transaction
          // Current data
          Ns.int.get.sorted === List(
            ("a", 5),
            ("b", 5),
            ("c", 3),
            ("d", 4)
          )
        }
      }

      // For brevity, the synchronous equivalent `update` is used in the following tests
    }
  }


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.int insert List(
        ("a", 1),
        ("b", 2),
        ("c", 3),
        ("d", 4)
      ) eids

      // Apply value to card-one attribute of multiple entities (retracts current values)
      Ns(a, b).int(5).update
      Ns.str.int.get.sorted === List(
        ("a", 5),
        ("b", 5),
        ("c", 3),
        ("d", 4)
      )

      // Entity ids as Seq
      val bc = Seq(b, c)
      Ns(bc).int(6).update
      Ns.str.int.get.sorted === List(
        ("a", 5),
        ("b", 6),
        ("c", 6),
        ("d", 4)
      )

      // Apply empty value to card-one attribute of multiple entities (delete values)
      Ns(c, d).int().update
      Ns.str.int$.get.sorted === List(
        ("a", Some(5)),
        ("b", Some(6)),
        ("c", None),
        ("d", None)
      )
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.int insert List(
        ("a", int1),
        ("b", int2),
        ("c", int3),
        ("d", int4)
      ) eids

      // Apply value (retracts current values)
      Ns(a, b).int(int5).update
      Ns.str.int.get.sorted === List(
        ("a", int5),
        ("b", int5),
        ("c", int3),
        ("d", int4)
      )

      // Apply empty value (delete values)
      Ns(b, d).int().update
      Ns.str.int$.get.sorted === List(
        ("a", Some(int5)),
        ("b", None),
        ("c", Some(int3)),
        ("d", None)
      )
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1)),
        ("b", Set(2)),
        ("c", Set(3)),
        ("d", Set(4))
      ) eids

      // Add value
      Ns(a, b).ints.assert(5).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 5)),
        ("b", Set(2, 5)),
        ("c", Set(3)),
        ("d", Set(4))
      )

      // Add possibly existing value
      Ns(b, c).ints.assert(2).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 5)),
        ("b", Set(2, 5)), // <-- 2 not added again
        ("c", Set(2, 3)), // <-- 2 added
        ("d", Set(4))
      )

      // Add multiple values
      Ns(a, d).ints.assert(6, 7).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(7, 1, 6, 5)),
        ("b", Set(2, 5)),
        ("c", Set(3, 2)),
        ("d", Set(7, 4, 6))
      )

      // Add empty Seq of values (no effect)
      Ns(a, c).ints.assert(Seq[Int]()).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(7, 1, 6, 5)),
        ("b", Set(2, 5)),
        ("c", Set(3, 2)),
        ("d", Set(7, 4, 6))
      )
    }


    "replace" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1, 2, 3)),
        ("b", Set(1, 2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      ) eids

      // Replace values
      Ns(a, b).ints.replace(3 -> 4).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 2, 4)), // 3 -> 4
        ("b", Set(1, 2, 4)), // 3 -> 4
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Replacing value to existing value simply retracts it
      Ns(b, c).ints.replace(2 -> 3).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 2, 4)),
        ("b", Set(1, 3, 4)), // 2 -> 3
        ("c", Set(1, 3)), // 2 retracted
        ("d", Set(1, 2, 3))
      )

      // Replace multiple values (vararg)
      Ns(a, d).ints.replace(1 -> 5, 2 -> 6).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(5, 6, 4)), // 1 -> 5, 2 -> 6
        ("b", Set(1, 3, 4)),
        ("c", Set(1, 3)),
        ("d", Set(5, 6, 3)) // 1 -> 5, 2 -> 6
      )

      // Missing old values have no effect. The new value is inserted (upsert semantics)
      Ns(a, d).ints.replace(42 -> 7).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(7, 4, 6, 5)), // 7 added
        ("b", Set(1, 3, 4)),
        ("c", Set(1, 3)),
        ("d", Set(7, 6, 3, 5)) // 7 added
      )
    }


    "retract" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1, 2, 3)),
        ("b", Set(1, 2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      ) eids


      // Remove values
      Ns(a, b).ints.retract(1).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(2, 3)),
        ("b", Set(2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Removing non-existing value has no effect
      Ns(a, b).ints.retract(7).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(2, 3)),
        ("b", Set(2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Removing duplicate values removes the distinct value
      Ns(a, b).ints.retract(2, 2).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(3)),
        ("b", Set(3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Remove multiple values
      Ns(c, d).ints.retract(2, 3).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(3)),
        ("b", Set(3)),
        ("c", Set(1)),
        ("d", Set(1))
      )

      // Removing empty Seq of values has no effect
      Ns(c, d).ints.retract(Seq[Int]()).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(3)),
        ("b", Set(3)),
        ("c", Set(1)),
        ("d", Set(1))
      )
    }


    "apply" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1, 2, 3)),
        ("b", Set(1, 2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      ) eids


      // Apply value (retracts all current values!)
      Ns(a, b).ints(1).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1)),
        ("b", Set(1)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Apply multiple values
      Ns(b, c).ints(2, 3).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1)),
        ("b", Set(2, 3)),
        ("c", Set(2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Apply empty Seq of values (retracts all values!)
      Ns(c, d).ints(Set[Int]()).update
      Ns.str.ints$.get.sortBy(_._1) === List(
        ("a", Some(Set(1))),
        ("b", Some(Set(2, 3))),
        ("c", None),
        ("d", None)
      )

      // Apply nothing (retracts all values!)
      Ns(b, c).ints().update
      Ns.str.ints$.get.sortBy(_._1) === List(
        ("a", Some(Set(1))),
        ("b", None),
        ("c", None),
        ("d", None)
      )
    }
  }


  "Optional values" in new CoreSetup {

    val List(a, b, c, d) = Ns.str.int insert List(
      ("a", 1),
      ("b", 2),
      ("c", 3),
      ("d", 4)
    ) eids

    // Apply Some(value) to card-one attribute of multiple entities (retracts current values)
    Ns(a, b).str("e").int$(Some(5)).update
    Ns.e.str.int.get.sorted === List(
      (a, "e", 5),
      (b, "e", 5),
      (c, "c", 3),
      (d, "d", 4)
    )

    // Apply None to card-one attribute of multiple entities (delete values)
    Ns(c, d).str("f").int$(None).update
    Ns.e.str.int$.get.sorted === List(
      (a, "e", Some(5)),
      (b, "e", Some(5)),
      (c, "f", None),
      (d, "f", None)
    )

    // Reversing positions

    Ns(a, b).int$(Some(6)).str("g").update
    Ns.e.str.int$.get.sorted === List(
      (a, "g", Some(6)),
      (b, "g", Some(6)),
      (c, "f", None),
      (d, "f", None)
    )

    Ns(b, c).int$(None).str("h").update
    Ns.e.str.int$.get.sorted === List(
      (a, "g", Some(6)),
      (b, "h", None),
      (c, "h", None),
      (d, "f", None)
    )
  }
}
