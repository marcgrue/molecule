package molecule.tests.core.crud.update

import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out1._
import molecule.datomic.base.transform.exception.Model2TransactionException
import molecule.core.ops.exception.VerifyModelException
import molecule.setup.TestSpec

class UpdateFloat extends TestSpec {

  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.float(2f).save.eid

      // Apply value (retracts current value)
      Ns(eid).float(1f).update
      Ns.float.get.head === 1f

      // Apply new value
      Ns(eid).float(2f).update
      Ns.float.get.head === 2f

      // Delete value (apply no value)
      Ns(eid).float().update
      Ns.float.get === List()


      // decimals with correct precision
      val e2 = Ns.float(2.3f).save.eid
      Ns(e2).float.get.head === 2.3f

      Ns(e2).float(2.6f).update
      Ns(e2).float.get.head === 2.6f

      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).float(2f, 3f).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... float(2.0, 3.0)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.float(float2).save.eid

      // Apply value (retracts current value)
      Ns(eid).float(float1).update
      Ns.float.get.head === float1

      // Apply new value
      Ns(eid).float(float2).update
      Ns.float.get.head === float2

      // Delete value (apply no value)
      Ns(eid).float().update
      Ns.float.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).float(float2, float3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... float($float2, $float3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.floats(1f).save.eid

      // Assert value
      Ns(eid).floats.assert(2f).update
      Ns.floats.get.head === Set(1f, 2f)

      // Assert existing value (no effect)
      Ns(eid).floats.assert(2f).update
      Ns.floats.get.head === Set(1f, 2f)

      // Assert multiple values (vararg)
      Ns(eid).floats.assert(3f, 4f).update
      Ns.floats.get.head === Set(1f, 2f, 3f, 4f)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).floats.assert(Seq(4f, 5f)).update
      Ns.floats.get.head === Set(1f, 2f, 3f, 4f, 5f)

      // Assert empty Seq of values (no effect)
      Ns(eid).floats.assert(Seq[Float]()).update
      Ns.floats.get.head === Set(1f, 2f, 3f, 4f, 5f)


      // Reset
      Ns(eid).floats().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).floats.assert(1.0f, 2.0f, 2.0f).update
      Ns.floats.get.head === Set(1.0f, 2.0f)
    }


    "replace" in new CoreSetup {

      val eid = Ns.floats(1f, 2f, 3f, 4f, 5f, 6f).save.eid

      // Replace value
      Ns(eid).floats.replace(6f -> 8f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 3f, 4f, 5f, 8f)

      // Replace value to existing value simply retracts it
      Ns(eid).floats.replace(5f -> 8f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 3f, 4f, 8f)

      // Replace multiple values (vararg)
      Ns(eid).floats.replace(3f -> 6f, 4f -> 7f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 6f, 7f, 8f)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floats.replace(42f -> 9f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 6f, 7f, 8f, 9f)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).floats.replace(Seq(2f -> 5f)).update
      Ns.floats.get.head.toList.sorted === List(1f, 5f, 6f, 7f, 8f, 9f)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).floats.replace(Seq[(Float, Float)]()).update
      Ns.floats.get.head.toList.sorted === List(1f, 5f, 6f, 7f, 8f, 9f)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).floats.replace(7f -> 8f, 8f -> 8f).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/floats`:" +
          "\n8.0")

      expectCompileError(
        """Ns(eid).floats.replace(Seq(7f -> 8f, 8f -> 8f)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/floats`:" +
          "\n8.0")
    }


    "retract" in new CoreSetup {

      val eid = Ns.floats(1f, 2f, 3f, 4f, 5f, 6f).save.eid

      // Retract value
      Ns(eid).floats.retract(6f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 3f, 4f, 5f)

      // Retracting non-existing value has no effect
      Ns(eid).floats.retract(7f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 3f, 4f, 5f)

      // Retracting duplicate values removes the distinc value
      Ns(eid).floats.retract(5f, 5f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f, 3f, 4f)

      // Retract multiple values (vararg)
      Ns(eid).floats.retract(3f, 4f).update
      Ns.floats.get.head.toList.sorted === List(1f, 2f)

      // Retract Seq of values
      Ns(eid).floats.retract(Seq(2f)).update
      Ns.floats.get.head.toList.sorted === List(1f)

      // Retracting empty Seq of values has no effect
      Ns(eid).floats.retract(Seq[Float]()).update
      Ns.floats.get.head.toList.sorted === List(1f)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floats(2f, 3f).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).floats(1f).update
      Ns.floats.get.head.toList.sorted === List(1f)

      // Apply multiple values (vararg)
      Ns(eid).floats(2f, 3f).update
      Ns.floats.get.head.toList.sorted === List(2f, 3f)

      // Apply Seq of values
      Ns(eid).floats(Set(4f)).update
      Ns.floats.get.head.toList.sorted === List(4f)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).floats(Set[Float]()).update
      Ns.floats.get === List()


      Ns(eid).floats(Set(1f, 2f)).update

      // Delete all (apply no values)
      Ns(eid).floats().update
      Ns.floats.get === List()


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).floats(1.0f, 2.0f, 2.0f).update
      Ns.floats.get.head === Set(1.0f, 2.0f)
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.floats(float1).save.eid

      // Assert value
      Ns(eid).floats.assert(float2).update
      Ns.floats.get.head === Set(float1, float2)

      // Assert existing value (no effect)
      Ns(eid).floats.assert(float2).update
      Ns.floats.get.head === Set(float1, float2)

      // Assert multiple values
      Ns(eid).floats.assert(float3, float4).update
      Ns.floats.get.head === Set(float1, float2, float3, float4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).floats.assert(Seq(float4, float5)).update
      Ns.floats.get.head === Set(float1, float2, float3, float4, float5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(float6, float7)
      Ns(eid).floats.assert(values).update
      Ns.floats.get.head === Set(float1, float2, float3, float4, float5, float6, float7)

      // Assert empty Seq of values (no effect)
      Ns(eid).floats.assert(Seq[Float]()).update
      Ns.floats.get.head === Set(float1, float2, float3, float4, float5, float6, float7)


      // Reset
      Ns(eid).floats().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).floats.assert(float1, float2, float2).update
      Ns.floats.get.head === Set(float1, float2)

      // Equal values are coalesced (at runtime)
      val other3 = float3
      Ns(eid).floats.assert(float2, float3, other3).update
      Ns.floats.get.head === Set(float3, float2, float1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.floats(float1, float2, float3, float4, float5, float6).save.eid

      // Replace value
      Ns(eid).floats.replace(float6 -> float8).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float3, float4, float5, float8)

      // Replace value to existing value simply retracts it
      Ns(eid).floats.replace(float5 -> float8).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float3, float4, float8)

      // Replace multiple values (vararg)
      Ns(eid).floats.replace(float3 -> float6, float4 -> float7).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float6, float7, float8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floats.replace(42f -> float9).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float6, float7, float8, float9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).floats.replace(Seq(float2 -> float5)).update
      Ns.floats.get.head.toList.sorted === List(float1, float5, float6, float7, float8, float9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(float1 -> float4)
      Ns(eid).floats.replace(values).update
      Ns.floats.get.head.toList.sorted === List(float4, float5, float6, float7, float8, float9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).floats.replace(Seq[(Float, Float)]()).update
      Ns.floats.get.head.toList.sorted === List(float4, float5, float6, float7, float8, float9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).floats.replace(float7 -> float8, float8 -> float8).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/floats`:" +
          "\n__ident__float8")

      expectCompileError(
        """Ns(eid).floats.replace(Seq(float7 -> float8, float8 -> float8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/floats`:" +
          "\n__ident__float8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8f

      (Ns(eid).floats.replace(float7 -> float8, float8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.datomic.base.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/floats`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).floats.replace(Seq(float7 -> float8, float8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.datomic.base.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/floats`:" +
        "\n8.0"
    }


    "retract" in new CoreSetup {

      val eid = Ns.floats(float1, float2, float3, float4, float5, float6).save.eid

      // Retract value
      Ns(eid).floats.retract(float6).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float3, float4, float5)

      // Retracting non-existing value has no effect
      Ns(eid).floats.retract(float7).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float3, float4, float5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).floats.retract(float5, float5).update
      Ns.floats.get.head.toList.sorted === List(float1, float2, float3, float4)

      // Retract multiple values (vararg)
      Ns(eid).floats.retract(float3, float4).update
      Ns.floats.get.head.toList.sorted === List(float1, float2)

      // Retract Seq of values
      Ns(eid).floats.retract(Seq(float2)).update
      Ns.floats.get.head.toList.sorted === List(float1)

      // Retract Seq of values as variable
      val values = Seq(float1)
      Ns(eid).floats.retract(values).update
      Ns.floats.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).floats(float1).update
      Ns(eid).floats.retract(Seq[Float]()).update
      Ns.floats.get.head.toList.sorted === List(float1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floats(float2, float3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).floats(float1).update
      Ns.floats.get.head.toList.sorted === List(float1)

      // Apply multiple values (vararg)
      Ns(eid).floats(float2, float3).update
      Ns.floats.get.head.toList.sorted === List(float2, float3)

      // Apply Seq of values
      Ns(eid).floats(Set(float4)).update
      Ns.floats.get.head.toList.sorted === List(float4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).floats(Set[Float]()).update
      Ns.floats.get === List()

      // Apply Seq of values as variable
      val values = Set(float1, float2)
      Ns(eid).floats(values).update
      Ns.floats.get.head.toList.sorted === List(float1, float2)

      // Delete all (apply no values)
      Ns(eid).floats().update
      Ns.floats.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).floats(float1, float2, float2).update
      Ns.floats.get.head === Set(float1, float2)

      // Equal values are coalesced (at runtime)
      val other3 = float3
      Ns(eid).floats(float2, float3, other3).update
      Ns.floats.get.head === Set(float2, float3)
    }
  }
}
