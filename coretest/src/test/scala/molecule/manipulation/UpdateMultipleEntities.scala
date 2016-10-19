package molecule.manipulation

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec}

class UpdateMultipleEntities extends CoreSpec {


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

    "add" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1)),
        ("b", Set(2)),
        ("c", Set(3)),
        ("d", Set(4))
      ) eids

      // Add value
      Ns(a, b).ints.add(5).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 5)),
        ("b", Set(2, 5)),
        ("c", Set(3)),
        ("d", Set(4))
      )

      // Add possibly exisiting value
      Ns(b, c).ints.add(2).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(1, 5)),
        ("b", Set(2, 5)), // <-- 2 not added again
        ("c", Set(2, 3)), // <-- 2 added
        ("d", Set(4))
      )

      // Add multiple values
      Ns(a, d).ints.add(6, 7).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(7, 1, 6, 5)),
        ("b", Set(2, 5)),
        ("c", Set(3, 2)),
        ("d", Set(7, 4, 6))
      )

      // Add empty Seq of values (no effect)
      Ns(a, c).ints.add(Seq[Int]()).update
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


    "remove" in new CoreSetup {

      val List(a, b, c, d) = Ns.str.ints insert List(
        ("a", Set(1, 2, 3)),
        ("b", Set(1, 2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      ) eids


      // Remove values
      Ns(a, b).ints.remove(1).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(2, 3)),
        ("b", Set(2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Removing non-existing value has no effect
      Ns(a, b).ints.remove(7).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(2, 3)),
        ("b", Set(2, 3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Removing duplicate values removes the distinct value
      Ns(a, b).ints.remove(2, 2).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(3)),
        ("b", Set(3)),
        ("c", Set(1, 2, 3)),
        ("d", Set(1, 2, 3))
      )

      // Remove multiple values
      Ns(c, d).ints.remove(2, 3).update
      Ns.str.ints.get.sortBy(_._1) === List(
        ("a", Set(3)),
        ("b", Set(3)),
        ("c", Set(1)),
        ("d", Set(1))
      )

      // Removing empty Seq of values has no effect
      Ns(c, d).ints.remove(Seq[Int]()).update
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
}
