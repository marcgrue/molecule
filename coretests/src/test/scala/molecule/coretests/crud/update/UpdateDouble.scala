package molecule.coretests.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out1._

class UpdateDouble extends CoreSpec {

  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.double(2.0).save.eid

      // Apply value (retracts current value)
      Ns(eid).double(1.0).update
      Ns.double.get.head === 1.0

      // Apply new value
      Ns(eid).double(2.0).update
      Ns.double.get.head === 2.0

      // Delete value (apply no value)
      Ns(eid).double().update
      Ns.double.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).double(2.0, 3.0).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... double(2.0, 3.0)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.double(double2).save.eid

      // Apply value (retracts current value)
      Ns(eid).double(double1).update
      Ns.double.get.head === double1

      // Apply new value
      Ns(eid).double(double2).update
      Ns.double.get.head === double2

      // Delete value (apply no value)
      Ns(eid).double().update
      Ns.double.get === List()


      // Applying multiple values to card-one attribute not alloweddouble(2.0, 3.0)

      (Ns(eid).double(double2, double3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... double($double2, $double3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.doubles(1.0).save.eid

      // Assert value
      Ns(eid).doubles.assert(2.0).update
      Ns.doubles.get.head === Set(1.0, 2.0)

      // Assert existing value (no effect)
      Ns(eid).doubles.assert(2.0).update
      Ns.doubles.get.head === Set(1.0, 2.0)

      // Assert multiple values (vararg)
      Ns(eid).doubles.assert(3.0, 4.0).update
      Ns.doubles.get.head === Set(1.0, 2.0, 3.0, 4.0)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).doubles.assert(Seq(4.0, 5.0)).update
      Ns.doubles.get.head === Set(1.0, 2.0, 3.0, 4.0, 5.0)

      // Assert empty Seq of values (no effect)
      Ns(eid).doubles.assert(Seq[Double]()).update
      Ns.doubles.get.head === Set(1.0, 2.0, 3.0, 4.0, 5.0)


      // Reset
      Ns(eid).doubles().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).doubles.assert(1.0, 2.0, 2.0).update
      Ns.doubles.get.head === Set(1.0, 2.0)
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubles(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).save.eid

      // Replace value
      Ns(eid).doubles.replace(6.0 -> 8.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0, 8.0)

      // Replace value to existing value simply retracts it
      Ns(eid).doubles.replace(5.0 -> 8.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 8.0)

      // Replace multiple values (vararg)
      Ns(eid).doubles.replace(3.0 -> 6.0, 4.0 -> 7.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 6.0, 7.0, 8.0)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubles.replace(42.0 -> 9.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 6.0, 7.0, 8.0, 9.0)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).doubles.replace(Seq(2.0 -> 5.0)).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 5.0, 6.0, 7.0, 8.0, 9.0)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).doubles.replace(Seq[(Double, Double)]()).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 5.0, 6.0, 7.0, 8.0, 9.0)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).doubles.replace(7.0 -> 8.0, 8.0 -> 8.0).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/doubles`:" +
          "\n8.0")

      expectCompileError(
        """Ns(eid).doubles.replace(Seq(7.0 -> 8.0, 8.0 -> 8.0)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/doubles`:" +
          "\n8.0")
    }


    "retract" in new CoreSetup {

      val eid = Ns.doubles(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).save.eid

      // Retract value
      Ns(eid).doubles.retract(6.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0)

      // Retracting non-existing value has no effect
      Ns(eid).doubles.retract(7.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0)

      // Retracting duplicate values removes the distinc value
      Ns(eid).doubles.retract(5.0, 5.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0, 3.0, 4.0)

      // Retract multiple values (vararg)
      Ns(eid).doubles.retract(3.0, 4.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0, 2.0)

      // Retract Seq of values
      Ns(eid).doubles.retract(Seq(2.0)).update
      Ns.doubles.get.head.toList.sorted === List(1.0)

      // Retracting empty Seq of values has no effect
      Ns(eid).doubles.retract(Seq[Double]()).update
      Ns.doubles.get.head.toList.sorted === List(1.0)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubles(2.0, 3.0).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).doubles(1.0).update
      Ns.doubles.get.head.toList.sorted === List(1.0)

      // Apply multiple values (vararg)
      Ns(eid).doubles(2.0, 3.0).update
      Ns.doubles.get.head.toList.sorted === List(2.0, 3.0)

      // Apply Seq of values
      Ns(eid).doubles(Set(4.0)).update
      Ns.doubles.get.head.toList.sorted === List(4.0)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).doubles(Set[Double]()).update
      Ns.doubles.get === List()


      Ns(eid).doubles(Set(1.0, 2.0)).update

      // Delete all (apply no values)
      Ns(eid).doubles().update
      Ns.doubles.get === List()


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).doubles(1.0, 2.0, 2.0).update
      Ns.doubles.get.head === Set(1.0, 2.0)
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.doubles(double1).save.eid

      // Assert value
      Ns(eid).doubles.assert(double2).update
      Ns.doubles.get.head === Set(double1, double2)

      // Assert existing value (no effect)
      Ns(eid).doubles.assert(double2).update
      Ns.doubles.get.head === Set(double1, double2)

      // Assert multiple values
      Ns(eid).doubles.assert(double3, double4).update
      Ns.doubles.get.head === Set(double1, double2, double3, double4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).doubles.assert(Seq(double4, double5)).update
      Ns.doubles.get.head === Set(double1, double2, double3, double4, double5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(double6, double7)
      Ns(eid).doubles.assert(values).update
      Ns.doubles.get.head === Set(double1, double2, double3, double4, double5, double6, double7)

      // Assert empty Seq of values (no effect)
      Ns(eid).doubles.assert(Seq[Double]()).update
      Ns.doubles.get.head === Set(double1, double2, double3, double4, double5, double6, double7)


      // Reset
      Ns(eid).doubles().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).doubles.assert(double1, double2, double2).update
      Ns.doubles.get.head === Set(double1, double2)

      // Equal values are coalesced (at runtime)
      val other3 = double3
      Ns(eid).doubles.assert(double2, double3, other3).update
      Ns.doubles.get.head === Set(double3, double2, double1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubles(double1, double2, double3, double4, double5, double6).save.eid

      // Replace value
      Ns(eid).doubles.replace(double6 -> double8).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double3, double4, double5, double8)

      // Replace value to existing value simply retracts it
      Ns(eid).doubles.replace(double5 -> double8).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double3, double4, double8)

      // Replace multiple values (vararg)
      Ns(eid).doubles.replace(double3 -> double6, double4 -> double7).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double6, double7, double8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubles.replace(42.0 -> double9).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double6, double7, double8, double9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).doubles.replace(Seq(double2 -> double5)).update
      Ns.doubles.get.head.toList.sorted === List(double1, double5, double6, double7, double8, double9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(double1 -> double4)
      Ns(eid).doubles.replace(values).update
      Ns.doubles.get.head.toList.sorted === List(double4, double5, double6, double7, double8, double9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).doubles.replace(Seq[(Double, Double)]()).update
      Ns.doubles.get.head.toList.sorted === List(double4, double5, double6, double7, double8, double9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).doubles.replace(double7 -> double8, double8 -> double8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/doubles`:" +
          "\n__ident__double8")

      expectCompileError(
        """Ns(eid).doubles.replace(Seq(double7 -> double8, double8 -> double8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/doubles`:" +
          "\n__ident__double8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8.0

      (Ns(eid).doubles.replace(double7 -> double8, double8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/doubles`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).doubles.replace(Seq(double7 -> double8, double8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/doubles`:" +
        "\n8.0"
    }


    "retract" in new CoreSetup {

      val eid = Ns.doubles(double1, double2, double3, double4, double5, double6).save.eid

      // Retract value
      Ns(eid).doubles.retract(double6).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double3, double4, double5)

      // Retracting non-existing value has no effect
      Ns(eid).doubles.retract(double7).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double3, double4, double5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).doubles.retract(double5, double5).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2, double3, double4)

      // Retract multiple values (vararg)
      Ns(eid).doubles.retract(double3, double4).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2)

      // Retract Seq of values
      Ns(eid).doubles.retract(Seq(double2)).update
      Ns.doubles.get.head.toList.sorted === List(double1)

      // Retract Seq of values as variable
      val values = Seq(double1)
      Ns(eid).doubles.retract(values).update
      Ns.doubles.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).doubles(double1).update
      Ns(eid).doubles.retract(Seq[Double]()).update
      Ns.doubles.get.head.toList.sorted === List(double1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubles(double2, double3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).doubles(double1).update
      Ns.doubles.get.head.toList.sorted === List(double1)

      // Apply multiple values (vararg)
      Ns(eid).doubles(double2, double3).update
      Ns.doubles.get.head.toList.sorted === List(double2, double3)

      // Apply Seq of values
      Ns(eid).doubles(Set(double4)).update
      Ns.doubles.get.head.toList.sorted === List(double4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).doubles(Set[Double]()).update
      Ns.doubles.get === List()

      // Apply Seq of values as variable
      val values = Set(double1, double2)
      Ns(eid).doubles(values).update
      Ns.doubles.get.head.toList.sorted === List(double1, double2)

      // Delete all (apply no values)
      Ns(eid).doubles().update
      Ns.doubles.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).doubles(double1, double2, double2).update
      Ns.doubles.get.head === Set(double1, double2)

      // Equal values are coalesced (at runtime)
      val other3 = double3
      Ns(eid).doubles(double2, double3, other3).update
      Ns.doubles.get.head === Set(double2, double3)
    }
  }
}
