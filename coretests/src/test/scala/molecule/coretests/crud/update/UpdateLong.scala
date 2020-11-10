package molecule.coretests.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.out1._

class UpdateLong extends CoreSpec {

  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.long(2L).save.eid

      // Apply value (retracts current value)
      Ns(eid).long(1L).update
      Ns.long.get.head === 1L

      // Apply new value
      Ns(eid).long(2L).update
      Ns.long.get.head === 2L

      // Delete value (apply no value)
      Ns(eid).long().update
      Ns.long.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).long(2L, 3L).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... long(2, 3)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.long(long2).save.eid

      // Apply value (retracts current value)
      Ns(eid).long(long1).update
      Ns.long.get.head === long1

      // Apply new value
      Ns(eid).long(long2).update
      Ns.long.get.head === long2

      // Delete value (apply no value)
      Ns(eid).long().update
      Ns.long.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).long(long2, long3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... long($long2, $long3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.longs(1L).save.eid

      // Assert value
      Ns(eid).longs.assert(2L).update
      Ns.longs.get.head === Set(1L, 2L)

      // Assert existing value (no effect)
      Ns(eid).longs.assert(2L).update
      Ns.longs.get.head === Set(1L, 2L)

      // Assert multiple values (vararg)
      Ns(eid).longs.assert(3L, 4L).update
      Ns.longs.get.head === Set(1L, 2L, 3L, 4L)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).longs.assert(Seq(4L, 5L)).update
      Ns.longs.get.head === Set(1L, 2L, 3L, 4L, 5L)

      // Assert empty Seq of values (no effect)
      Ns(eid).longs.assert(Seq[Long]()).update
      Ns.longs.get.head === Set(1L, 2L, 3L, 4L, 5L)


      // Reset
      Ns(eid).longs().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).longs.assert(1L, 2L, 2L).update
      Ns.longs.get.head === Set(1L, 2L)
    }


    "replace" in new CoreSetup {

      val eid = Ns.longs(1L, 2L, 3L, 4L, 5L, 6L).save.eid

      // Replace value
      Ns(eid).longs.replace(6L -> 8L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 3L, 4L, 5L, 8L)

      // Replace value to existing value simply retracts it
      Ns(eid).longs.replace(5L -> 8L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 3L, 4L, 8L)

      // Replace multiple values (vararg)
      Ns(eid).longs.replace(3L -> 6L, 4L -> 7L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 6L, 7L, 8L)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longs.replace(42L -> 9L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 6L, 7L, 8L, 9L)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).longs.replace(Seq(2L -> 5L)).update
      Ns.longs.get.head.toList.sorted === List(1L, 5L, 6L, 7L, 8L, 9L)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).longs.replace(Seq[(Long, Long)]()).update
      Ns.longs.get.head.toList.sorted === List(1L, 5L, 6L, 7L, 8L, 9L)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).longs.replace(7L -> 8L, 8L -> 8L).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/longs`:" +
          "\n8")

      expectCompileError(
        """Ns(eid).longs.replace(Seq(7L -> 8L, 8L -> 8L)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/longs`:" +
          "\n8")
    }


    "retract" in new CoreSetup {

      val eid = Ns.longs(1L, 2L, 3L, 4L, 5L, 6L).save.eid

      // Retract value
      Ns(eid).longs.retract(6L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 3L, 4L, 5L)

      // Retracting non-existing value has no effect
      Ns(eid).longs.retract(7L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 3L, 4L, 5L)

      // Retracting duplicate values removes the distinc value
      Ns(eid).longs.retract(5L, 5L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L, 3L, 4L)

      // Retract multiple values (vararg)
      Ns(eid).longs.retract(3L, 4L).update
      Ns.longs.get.head.toList.sorted === List(1L, 2L)

      // Retract Seq of values
      Ns(eid).longs.retract(Seq(2L)).update
      Ns.longs.get.head.toList.sorted === List(1L)

      // Retracting empty Seq of values has no effect
      Ns(eid).longs.retract(Seq[Long]()).update
      Ns.longs.get.head.toList.sorted === List(1L)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longs(2L, 3L).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).longs(1L).update
      Ns.longs.get.head.toList.sorted === List(1L)

      // Apply multiple values (vararg)
      Ns(eid).longs(2L, 3L).update
      Ns.longs.get.head.toList.sorted === List(2L, 3L)

      // Apply Seq of values
      Ns(eid).longs(Set(4L)).update
      Ns.longs.get.head.toList.sorted === List(4L)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).longs(Set[Long]()).update
      Ns.longs.get === List()


      Ns(eid).longs(Set(1L, 2L)).update

      // Delete all (apply no values)
      Ns(eid).longs().update
      Ns.longs.get === List()


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).longs(1L, 2L, 2L).update
      Ns.longs.get.head === Set(1L, 2L)
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.longs(long1).save.eid

      // Assert value
      Ns(eid).longs.assert(long2).update
      Ns.longs.get.head === Set(long1, long2)

      // Assert existing value (no effect)
      Ns(eid).longs.assert(long2).update
      Ns.longs.get.head === Set(long1, long2)

      // Assert multiple values
      Ns(eid).longs.assert(long3, long4).update
      Ns.longs.get.head === Set(long1, long2, long3, long4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).longs.assert(Seq(long4, long5)).update
      Ns.longs.get.head === Set(long1, long2, long3, long4, long5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(long6, long7)
      Ns(eid).longs.assert(values).update
      Ns.longs.get.head === Set(long1, long2, long3, long4, long5, long6, long7)

      // Assert empty Seq of values (no effect)
      Ns(eid).longs.assert(Seq[Long]()).update
      Ns.longs.get.head === Set(long1, long2, long3, long4, long5, long6, long7)


      // Reset
      Ns(eid).longs().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).longs.assert(long1, long2, long2).update
      Ns.longs.get.head === Set(long1, long2)

      // Equal values are coalesced (at runtime)
      val other3 = long3
      Ns(eid).longs.assert(long2, long3, other3).update
    }


    "replace" in new CoreSetup {

      val eid = Ns.longs(long1, long2, long3, long4, long5, long6).save.eid

      // Replace value
      Ns(eid).longs.replace(long6 -> long8).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long3, long4, long5, long8)

      // Replace value to existing value simply retracts it
      Ns(eid).longs.replace(long5 -> long8).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long3, long4, long8)

      // Replace multiple values (vararg)
      Ns(eid).longs.replace(long3 -> long6, long4 -> long7).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long6, long7, long8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longs.replace(42L -> long9).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long6, long7, long8, long9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).longs.replace(Seq(long2 -> long5)).update
      Ns.longs.get.head.toList.sorted === List(long1, long5, long6, long7, long8, long9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(long1 -> long4)
      Ns(eid).longs.replace(values).update
      Ns.longs.get.head.toList.sorted === List(long4, long5, long6, long7, long8, long9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).longs.replace(Seq[(Long, Long)]()).update
      Ns.longs.get.head.toList.sorted === List(long4, long5, long6, long7, long8, long9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).longs.replace(long7 -> long8, long8 -> long8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/longs`:" +
          "\n__ident__long8")

      expectCompileError(
        """Ns(eid).longs.replace(Seq(long7 -> long8, long8 -> long8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/longs`:" +
          "\n__ident__long8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8L

      (Ns(eid).longs.replace(long7 -> long8, long8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/longs`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).longs.replace(Seq(long7 -> long8, long8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/longs`:" +
        "\n8"
    }


    "retract" in new CoreSetup {

      val eid = Ns.longs(long1, long2, long3, long4, long5, long6).save.eid

      // Retract value
      Ns(eid).longs.retract(long6).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long3, long4, long5)

      // Retracting non-existing value has no effect
      Ns(eid).longs.retract(long7).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long3, long4, long5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).longs.retract(long5, long5).update
      Ns.longs.get.head.toList.sorted === List(long1, long2, long3, long4)

      // Retract multiple values (vararg)
      Ns(eid).longs.retract(long3, long4).update
      Ns.longs.get.head.toList.sorted === List(long1, long2)

      // Retract Seq of values
      Ns(eid).longs.retract(Seq(long2)).update
      Ns.longs.get.head.toList.sorted === List(long1)

      // Retract Seq of values as variable
      val values = Seq(long1)
      Ns(eid).longs.retract(values).update
      Ns.longs.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).longs(long1).update
      Ns(eid).longs.retract(Seq[Long]()).update
      Ns.longs.get.head.toList.sorted === List(long1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longs(long2, long3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).longs(long1).update
      Ns.longs.get.head.toList.sorted === List(long1)

      // Apply multiple values (vararg)
      Ns(eid).longs(long2, long3).update
      Ns.longs.get.head.toList.sorted === List(long2, long3)

      // Apply Seq of values
      Ns(eid).longs(Set(long4)).update
      Ns.longs.get.head.toList.sorted === List(long4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).longs(Set[Long]()).update
      Ns.longs.get === List()

      // Apply Seq of values as variable
      val values = Set(long1, long2)
      Ns(eid).longs(values).update
      Ns.longs.get.head.toList.sorted === List(long1, long2)

      // Ref attributes are treated the same way
      Ns(eid).refs1(values).update

      // Delete all (apply no values)
      Ns(eid).longs().update
      Ns.longs.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).longs(long1, long2, long2).update
      Ns.longs.get.head === Set(long1, long2)

      // Equal values are coalesced (at runtime)
      val other3 = long3
      Ns(eid).longs(long2, long3, other3).update
      Ns.longs.get.head === Set(long2, long3)
    }
  }
}
