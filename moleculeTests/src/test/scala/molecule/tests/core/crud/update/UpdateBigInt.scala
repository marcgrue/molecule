package molecule.tests.core.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out1._
import molecule.TestSpec

class UpdateBigInt extends TestSpec {

  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bigInt(bigInt2).save.eid

      // Apply value (retracts current value)
      Ns(eid).bigInt(bigInt1).update
      Ns.bigInt.get.head === bigInt1

      // Apply new value
      Ns(eid).bigInt(bigInt2).update
      Ns.bigInt.get.head === bigInt2

      // Delete value (apply no value)
      Ns(eid).bigInt().update
      Ns.bigInt.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).bigInt(bigInt2, bigInt3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bigInt($bigInt2, $bigInt3)"
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1).save.eid

      // Assert value
      Ns(eid).bigInts.assert(bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Assert existing value (no effect)
      Ns(eid).bigInts.assert(bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Assert multiple values
      Ns(eid).bigInts.assert(bigInt3, bigInt4).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).bigInts.assert(Seq(bigInt4, bigInt5)).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(bigInt6, bigInt7)
      Ns(eid).bigInts.assert(values).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6, bigInt7)

      // Assert empty Seq of values (no effect)
      Ns(eid).bigInts.assert(Seq[BigInt]()).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6, bigInt7)

      
      // Reset
      Ns(eid).bigInts().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).bigInts.assert(bigInt1, bigInt2, bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Equal values are coalesced (at runtime)
      val other3 = bigInt3
      Ns(eid).bigInts.assert(bigInt2, bigInt3, other3).update
      Ns.bigInts.get.head === Set(bigInt3, bigInt2, bigInt1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6).save.eid

      // Replace value
      Ns(eid).bigInts.replace(bigInt6 -> bigInt8).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt8)

      // Replace value to existing value simply retracts it
      Ns(eid).bigInts.replace(bigInt5 -> bigInt8).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt8)

      // Replace multiple values (vararg)
      Ns(eid).bigInts.replace(bigInt3 -> bigInt6, bigInt4 -> bigInt7).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt6, bigInt7, bigInt8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      val bigInt42 = BigInt(42)
      Ns(eid).bigInts.replace(bigInt42 -> bigInt9).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).bigInts.replace(Seq(bigInt2 -> bigInt5)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(bigInt1 -> bigInt4)
      Ns(eid).bigInts.replace(values).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).bigInts.replace(Seq[(BigInt, BigInt)]()).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).bigInts.replace(bigInt7 -> bigInt8, bigInt8 -> bigInt8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/bigInts`:" +
          "\n__ident__bigInt8")

      expectCompileError(
        """Ns(eid).bigInts.replace(Seq(bigInt7 -> bigInt8, bigInt8 -> bigInt8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/bigInts`:" +
          "\n__ident__bigInt8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = bigInt8

      (Ns(eid).bigInts.replace(bigInt7 -> bigInt8, bigInt8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/bigInts`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).bigInts.replace(Seq(bigInt7 -> bigInt8, bigInt8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/bigInts`:" +
        "\n8"
    }


    "retract" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6).save.eid

      // Retract value
      Ns(eid).bigInts.retract(bigInt6).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Retracting non-existing value has no effect
      Ns(eid).bigInts.retract(bigInt7).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Retracting duplicate values removes the distinct value
      Ns(eid).bigInts.retract(bigInt5, bigInt5).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4)

      // Retract multiple values (vararg)
      Ns(eid).bigInts.retract(bigInt3, bigInt4).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2)

      // Retract Seq of values
      Ns(eid).bigInts.retract(Seq(bigInt2)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)

      // Retract Seq of values as variable
      val values = Seq(bigInt1)
      Ns(eid).bigInts.retract(values).update
      Ns.bigInts.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).bigInts(bigInt1).update
      Ns(eid).bigInts.retract(Seq[BigInt]()).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.bigInts(bigInt2, bigInt3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).bigInts(bigInt1).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)

      // Apply multiple values (vararg)
      Ns(eid).bigInts(bigInt2, bigInt3).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt2, bigInt3)

      // Apply Seq of values
      Ns(eid).bigInts(Set(bigInt4)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).bigInts(Set[BigInt]()).update
      Ns.bigInts.get === List()

      // Apply Seq of values as variable
      val values = Set(bigInt1, bigInt2)
      Ns(eid).bigInts(values).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2)

      // Delete all (apply no values)
      Ns(eid).bigInts().update
      Ns.bigInts.get === List()


      Ns(eid).bigInts(bigInt1, bigInt2, bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      val other3 = bigInt3
      Ns(eid).bigInts(bigInt2, bigInt3, other3).update
      Ns.bigInts.get.head === Set(bigInt2, bigInt3)


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).bigInts(bigInt1, bigInt2, bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Equal values are coalesced (at runtime)
      Ns(eid).bigInts(bigInt2, bigInt3, other3).update
      Ns.bigInts.get.head === Set(bigInt2, bigInt3)
    }
  }
}
