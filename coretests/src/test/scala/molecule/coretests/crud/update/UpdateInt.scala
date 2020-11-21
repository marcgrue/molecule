package molecule.coretests.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.{expectCompileError, DatomicPeer}
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out1._
import scala.concurrent.ExecutionContext.Implicits.global


class UpdateInt extends CoreSpec {

  if (system == DatomicPeer) {
    "Async" in new CoreSetup {

      // Update data asynchronously. Internally calls Datomic's transactAsync API.

      for {
        // Initial data
        saveTx <- Ns.int insertAsync List(1, 2)
        List(id1, id2) = saveTx.eids

        // Update 2 to 3
        updateTx <- Ns(id2).int(3).updateAsync

        // Get result
        result <- Ns.int.getAsync
      } yield {
        // 2 was updated to 3
        result === List(1, 3)
      }

      // For brevity, the synchronous equivalent `update` is used in the following tests
    }
  }

  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(2).save.eid

      // Apply value (retracts current value)
      Ns(eid).int(1).update
      Ns.int.get.head === 1

      // Apply new value
      Ns(eid).int(2).update
      Ns.int.get.head === 2

      // Apply empty value (retract `int` datom)
      Ns(eid).int().update
      Ns.int.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).int.update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[onlyAtomsWithValue]  Update molecule can only have attributes with some value(s) applied/added/replaced etc."

      (Ns(eid).int(2, 3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... int(2, 3)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(int2).save.eid

      // Apply value (retracts current value)
      Ns(eid).int(int1).update
      Ns.int.get.head === int1

      // Apply new value
      Ns(eid).int(int2).update
      Ns.int.get.head === int2

      // Delete value (apply empty value)
      Ns(eid).int().update
      Ns.int.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).int(int2, int3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... int($int2, $int3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.ints(1).save.eid

      // Assert value
      Ns(eid).ints.assert(2).update
      Ns.ints.get.head === Set(1, 2)

      // Assert existing value (no effect)
      Ns(eid).ints.assert(2).update
      Ns.ints.get.head === Set(1, 2)

      // Assert multiple values (vararg)
      Ns(eid).ints.assert(3, 4).update
      Ns.ints.get.head === Set(1, 2, 3, 4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).ints.assert(Seq(4, 5)).update
      Ns.ints.get.head === Set(1, 2, 3, 4, 5)

      // Assert Set of values
      Ns(eid).ints.assert(Set(6)).update
      Ns.ints.get.head === Set(1, 2, 3, 4, 5, 6)

      // Assert Iterable of values
      Ns(eid).ints.assert(Iterable(7)).update
      Ns.ints.get.head === Set(1, 2, 3, 4, 5, 6, 7)

      // Assert empty Seq of values (no effect)
      Ns(eid).ints.assert(Seq[Int]()).update
      Ns.ints.get.head === Set(1, 2, 3, 4, 5, 6, 7)


      // Reset
      Ns(eid).ints().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).ints.assert(1, 2, 2).update
      Ns.ints.get.head === Set(1, 2)
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(1, 2, 3, 4, 5, 6).save.eid

      // Replace value
      Ns(eid).ints.replace(6 -> 8).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 3, 4, 5, 8)

      // Replacing value to existing value simply retracts it
      Ns(eid).ints.replace(5 -> 8).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 3, 4, 8)

      // Replace multiple values (vararg)
      Ns(eid).ints.replace(3 -> 6, 4 -> 7).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 6, 7, 8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).ints.replace(42 -> 9).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 6, 7, 8, 9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).ints.replace(Seq(2 -> 5)).update
      Ns.ints.get.head.toList.sorted === List(1, 5, 6, 7, 8, 9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.get.head.toList.sorted === List(1, 5, 6, 7, 8, 9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(7 -> 8, 8 -> 8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/ints`:" +
          "\n8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(7 -> 8, 8 -> 8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/ints`:" +
          "\n8")
    }


    "retract" in new CoreSetup {

      val eid = Ns.ints(1, 2, 3, 4, 5, 6).save.eid

      // Retract value
      Ns(eid).ints.retract(6).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 3, 4, 5)

      // Retracting non-existing value has no effect
      Ns(eid).ints.retract(7).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 3, 4, 5)

      // Retracting duplicate values removes the distinct value
      Ns(eid).ints.retract(5, 5).update
      Ns.ints.get.head.toList.sorted === List(1, 2, 3, 4)

      // Retract multiple values (vararg)
      Ns(eid).ints.retract(3, 4).update
      Ns.ints.get.head.toList.sorted === List(1, 2)

      // Retract Seq of values
      Ns(eid).ints.retract(Seq(2)).update
      Ns.ints.get.head.toList.sorted === List(1)

      // Retracting empty Seq of values has no effect
      Ns(eid).ints.retract(Seq[Int]()).update
      Ns.ints.get.head.toList.sorted === List(1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(2, 3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(1).update
      Ns.ints.get.head.toList.sorted === List(1)

      // Apply multiple values (vararg)
      Ns(eid).ints(2, 3).update
      Ns.ints.get.head.toList.sorted === List(2, 3)

      // Apply Seq of values
      Ns(eid).ints(Set(4)).update
      Ns.ints.get.head.toList.sorted === List(4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).ints(Seq[Int]()).update
      Ns.ints.get === List()


      Ns(eid).ints(Set(1, 2)).update

      // Delete all (apply no values)
      Ns(eid).ints().update
      Ns.ints.get === List()


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).ints(1, 2, 2).update
      Ns.ints.get.head === Set(1, 2)
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.ints(int1).save.eid

      // Assert value
      Ns(eid).ints.assert(int2).update
      Ns.ints.get.head === Set(int1, int2)

      // Assert existing value (no effect)
      Ns(eid).ints.assert(int2).update
      Ns.ints.get.head === Set(int1, int2)

      // Assert multiple values
      Ns(eid).ints.assert(int3, int4).update
      Ns.ints.get.head === Set(int1, int2, int3, int4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).ints.assert(Seq(int4, int5)).update
      Ns.ints.get.head === Set(int1, int2, int3, int4, int5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(int6, int7)
      Ns(eid).ints.assert(values).update
      Ns.ints.get.head === Set(int1, int2, int3, int4, int5, int6, int7)

      // Assert empty Seq of values (no effect)
      Ns(eid).ints.assert(Seq[Int]()).update
      Ns.ints.get.head === Set(int1, int2, int3, int4, int5, int6, int7)


      // Reset
      Ns(eid).ints().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).ints.assert(int1, int2, int2).update
      Ns.ints.get.head === Set(int1, int2)

      // Equal values are coalesced (at runtime)
      val other3 = int3
      Ns(eid).ints.assert(int2, int3, other3).update
      Ns.ints.get.head === Set(int3, int2, int1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(int1, int2, int3, int4, int5, int6).save.eid

      // Replace value
      Ns(eid).ints.replace(int6 -> int8).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int3, int4, int5, int8)

      // Replace value to existing value simply retracts it
      Ns(eid).ints.replace(int5 -> int8).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int3, int4, int8)

      // Replace multiple values (vararg)
      Ns(eid).ints.replace(int3 -> int6, int4 -> int7).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int6, int7, int8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).ints.replace(42 -> int9).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int6, int7, int8, int9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).ints.replace(Seq(int2 -> int5)).update
      Ns.ints.get.head.toList.sorted === List(int1, int5, int6, int7, int8, int9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(int1 -> int4)
      Ns(eid).ints.replace(values).update
      Ns.ints.get.head.toList.sorted === List(int4, int5, int6, int7, int8, int9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.get.head.toList.sorted === List(int4, int5, int6, int7, int8, int9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(int7 -> int8, int8 -> int8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/ints`:" +
          "\n__ident__int8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> int8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/ints`:" +
          "\n__ident__int8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8

      (Ns(eid).ints.replace(int7 -> int8, int8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/ints`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/ints`:" +
        "\n8"
    }


    "retract" in new CoreSetup {

      val eid = Ns.ints(int1, int2, int3, int4, int5, int6).save.eid

      // Retract value
      Ns(eid).ints.retract(int6).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int3, int4, int5)

      // Retracting non-existing value has no effect
      Ns(eid).ints.retract(int7).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int3, int4, int5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).ints.retract(int5, int5).update
      Ns.ints.get.head.toList.sorted === List(int1, int2, int3, int4)

      // Retract multiple values (vararg)
      Ns(eid).ints.retract(int3, int4).update
      Ns.ints.get.head.toList.sorted === List(int1, int2)

      // Retract Seq of values
      Ns(eid).ints.retract(Seq(int2)).update
      Ns.ints.get.head.toList.sorted === List(int1)

      // Retract Seq of values as variable
      val values = Seq(int1)
      Ns(eid).ints.retract(values).update
      Ns.ints.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).ints(int1).update
      Ns(eid).ints.retract(Seq[Int]()).update
      Ns.ints.get.head.toList.sorted === List(int1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(int2, int3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(int1).update
      Ns.ints.get.head.toList.sorted === List(int1)

      // Apply multiple values (vararg)
      Ns(eid).ints(int2, int3).update
      Ns.ints.get.head.toList.sorted === List(int2, int3)

      // Apply Seq of values
      Ns(eid).ints(Set(int4)).update
      Ns.ints.get.head.toList.sorted === List(int4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).ints(Set[Int]()).update
      Ns.ints.get === List()

      // Apply Seq of values as variable
      val values = Set(int1, int2)
      Ns(eid).ints(values).update
      Ns.ints.get.head.toList.sorted === List(int1, int2)

      // Delete all (apply no values)
      Ns(eid).ints().update
      Ns.ints.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).ints(int1, int2, int2).update
      Ns.ints.get.head === Set(int1, int2)

      // Equal values are coalesced (at runtime)
      val other3 = int3
      Ns(eid).ints(int2, int3, other3).update
      Ns.ints.get.head === Set(int2, int3)
    }
  }
}
