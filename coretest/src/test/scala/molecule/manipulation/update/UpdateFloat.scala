package molecule.manipulation.update

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateFloat extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.float(2f).save.eid

      // Apply value (retracts current value)
      Ns(eid).float(1f).update
      Ns.float.one === 1f

      // Apply new value
      Ns(eid).float(2f).update
      Ns.float.one === 2f

      // Delete value (apply no value)
      Ns(eid).float().update
      Ns.float.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).float(2f, 3f).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... float(2.0, 3.0)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.float(float2).save.eid

      // Apply value (retracts current value)
      Ns(eid).float(float1).update
      Ns.float.one === float1

      // Apply new value
      Ns(eid).float(float2).update
      Ns.float.one === float2

      // Delete value (apply no value)
      Ns(eid).float().update
      Ns.float.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).float(float2, float3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... float($float2, $float3)"
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.floats(1f).save.eid

      // Add value
      Ns(eid).floats.add(2f).update
      Ns.floats.one === Set(1f, 2f)

      // Add exisiting value (no effect)
      Ns(eid).floats.add(2f).update
      Ns.floats.one === Set(1f, 2f)

      // Add multiple values (vararg)
      Ns(eid).floats.add(3f, 4f).update
      Ns.floats.one === Set(1f, 2f, 3f, 4f)

      // Add Seq of values (existing values unaffected)
      Ns(eid).floats.add(Seq(4f, 5f)).update
      Ns.floats.one === Set(1f, 2f, 3f, 4f, 5f)

      // Add empty Seq of values (no effect)
      Ns(eid).floats.add(Seq[Float]()).update
      Ns.floats.one === Set(1f, 2f, 3f, 4f, 5f)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).floats.add(5f, 5f, 6f, 6f, 7f).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/floats`:" +
          "\n5.0" +
          "\n6.0")

      // Seq
      expectCompileError(
        """Ns(eid).floats.add(Seq(5f, 5f, 6f, 6f, 7f)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/floats`:" +
          "\n5.0" +
          "\n6.0")
    }


    "replace" in new CoreSetup {

      val eid = Ns.floats(1f, 2f, 3f, 4f, 5f, 6f).save.eid

      // Replace value
      Ns(eid).floats.replace(6f -> 8f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 3f, 4f, 5f, 8f)

      // Replace value to existing value simply retracts it
      Ns(eid).floats.replace(5f -> 8f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 3f, 4f, 8f)

      // Replace multiple values (vararg)
      Ns(eid).floats.replace(3f -> 6f, 4f -> 7f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 6f, 7f, 8f)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floats.replace(42f -> 9f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 6f, 7f, 8f, 9f)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).floats.replace(Seq(2f -> 5f)).update
      Ns.floats.one.toList.sorted === List(1f, 5f, 6f, 7f, 8f, 9f)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).floats.replace(Seq[(Float, Float)]()).update
      Ns.floats.one.toList.sorted === List(1f, 5f, 6f, 7f, 8f, 9f)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).floats.replace(7f -> 8f, 8f -> 8f).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/floats`:" +
          "\n8.0")

      expectCompileError(
        """Ns(eid).floats.replace(Seq(7f -> 8f, 8f -> 8f)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/floats`:" +
          "\n8.0")
    }


    "remove" in new CoreSetup {

      val eid = Ns.floats(1f, 2f, 3f, 4f, 5f, 6f).save.eid

      // Remove value
      Ns(eid).floats.remove(6f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 3f, 4f, 5f)

      // Removing non-existing value has no effect
      Ns(eid).floats.remove(7f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 3f, 4f, 5f)

      // Removing duplicate values removes the distinc value
      Ns(eid).floats.remove(5f, 5f).update
      Ns.floats.one.toList.sorted === List(1f, 2f, 3f, 4f)

      // Remove multiple values (vararg)
      Ns(eid).floats.remove(3f, 4f).update
      Ns.floats.one.toList.sorted === List(1f, 2f)

      // Remove Seq of values
      Ns(eid).floats.remove(Seq(2f)).update
      Ns.floats.one.toList.sorted === List(1f)

      // Removing empty Seq of values has no effect
      Ns(eid).floats.remove(Seq[Float]()).update
      Ns.floats.one.toList.sorted === List(1f)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floats(2f, 3f).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).floats(1f).update
      Ns.floats.one.toList.sorted === List(1f)

      // Apply multiple values (vararg)
      Ns(eid).floats(2f, 3f).update
      Ns.floats.one.toList.sorted === List(2f, 3f)

      // Apply Seq of values
      Ns(eid).floats(Set(4f)).update
      Ns.floats.one.toList.sorted === List(4f)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).floats(Set[Float]()).update
      Ns.floats.get === List()


      Ns(eid).floats(Set(1f, 2f)).update

      // Delete all (apply no values)
      Ns(eid).floats().update
      Ns.floats.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).floats(2f, 2f, 3f, 4f, 3f).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/floats`:" +
          "\n2.0" +
          "\n3.0")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.floats(float1).save.eid

      // Add value
      Ns(eid).floats.add(float2).update
      Ns.floats.one === Set(float1, float2)

      // Add exisiting value (no effect)
      Ns(eid).floats.add(float2).update
      Ns.floats.one === Set(float1, float2)

      // Add multiple values
      Ns(eid).floats.add(float3, float4).update
      Ns.floats.one === Set(float1, float2, float3, float4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).floats.add(Seq(float4, float5)).update
      Ns.floats.one === Set(float1, float2, float3, float4, float5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(float6, float7)
      Ns(eid).floats.add(values).update
      Ns.floats.one === Set(float1, float2, float3, float4, float5, float6, float7)

      // Add empty Seq of values (no effect)
      Ns(eid).floats.add(Seq[Float]()).update
      Ns.floats.one === Set(float1, float2, float3, float4, float5, float6, float7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).floats.add(float5, float5, float6, float6, float7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/floats`:" +
          "\n__ident__float5" +
          "\n__ident__float6"
      )

      // Seq
      expectCompileError(
        """Ns(eid).floats.add(Seq(float5, float5, float6, float6, float7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/floats`:" +
          "\n__ident__float5" +
          "\n__ident__float6"
      )

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (5f, 6f)

      // vararg
      (Ns(eid).floats.add(other5, float5, float6, other6, float7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/floats`:" +
        "\n5.0" +
        "\n6.0"

      // Seq
      (Ns(eid).floats.add(Seq(other5, float5, float6, other6, float7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/floats`:" +
        "\n5.0" +
        "\n6.0"
    }


    "replace" in new CoreSetup {

      val eid = Ns.floats(float1, float2, float3, float4, float5, float6).save.eid

      // Replace value
      Ns(eid).floats.replace(float6 -> float8).update
      Ns.floats.one.toList.sorted === List(float1, float2, float3, float4, float5, float8)

      // Replace value to existing value simply retracts it
      Ns(eid).floats.replace(float5 -> float8).update
      Ns.floats.one.toList.sorted === List(float1, float2, float3, float4, float8)

      // Replace multiple values (vararg)
      Ns(eid).floats.replace(float3 -> float6, float4 -> float7).update
      Ns.floats.one.toList.sorted === List(float1, float2, float6, float7, float8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floats.replace(42f -> float9).update
      Ns.floats.one.toList.sorted === List(float1, float2, float6, float7, float8, float9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).floats.replace(Seq(float2 -> float5)).update
      Ns.floats.one.toList.sorted === List(float1, float5, float6, float7, float8, float9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(float1 -> float4)
      Ns(eid).floats.replace(values).update
      Ns.floats.one.toList.sorted === List(float4, float5, float6, float7, float8, float9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).floats.replace(Seq[(Float, Float)]()).update
      Ns.floats.one.toList.sorted === List(float4, float5, float6, float7, float8, float9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).floats.replace(float7 -> float8, float8 -> float8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/floats`:" +
          "\n__ident__float8")

      expectCompileError(
        """Ns(eid).floats.replace(Seq(float7 -> float8, float8 -> float8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/floats`:" +
          "\n__ident__float8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8f

      (Ns(eid).floats.replace(float7 -> float8, float8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/floats`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).floats.replace(Seq(float7 -> float8, float8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/floats`:" +
        "\n8.0"
    }


    "remove" in new CoreSetup {

      val eid = Ns.floats(float1, float2, float3, float4, float5, float6).save.eid

      // Remove value
      Ns(eid).floats.remove(float6).update
      Ns.floats.one.toList.sorted === List(float1, float2, float3, float4, float5)

      // Removing non-existing value has no effect
      Ns(eid).floats.remove(float7).update
      Ns.floats.one.toList.sorted === List(float1, float2, float3, float4, float5)

      // Removing duplicate values removes the distinc value
      Ns(eid).floats.remove(float5, float5).update
      Ns.floats.one.toList.sorted === List(float1, float2, float3, float4)

      // Remove multiple values (vararg)
      Ns(eid).floats.remove(float3, float4).update
      Ns.floats.one.toList.sorted === List(float1, float2)

      // Remove Seq of values
      Ns(eid).floats.remove(Seq(float2)).update
      Ns.floats.one.toList.sorted === List(float1)

      // Remove Seq of values as variable
      val values = Seq(float1)
      Ns(eid).floats.remove(values).update
      Ns.floats.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).floats(float1).update
      Ns(eid).floats.remove(Seq[Float]()).update
      Ns.floats.one.toList.sorted === List(float1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floats(float2, float3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).floats(float1).update
      Ns.floats.one.toList.sorted === List(float1)

      // Apply multiple values (vararg)
      Ns(eid).floats(float2, float3).update
      Ns.floats.one.toList.sorted === List(float2, float3)

      // Apply Seq of values
      Ns(eid).floats(Set(float4)).update
      Ns.floats.one.toList.sorted === List(float4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).floats(Set[Float]()).update
      Ns.floats.get === List()

      // Apply Seq of values as variable
      val values = Set(float1, float2)
      Ns(eid).floats(values).update
      Ns.floats.one.toList.sorted === List(float1, float2)

      // Delete all (apply no values)
      Ns(eid).floats().update
      Ns.floats.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).floats(float2, float2, float3, float4, float3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/floats`:" +
          "\n__ident__float2" +
          "\n__ident__float3"
      )

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (2f, 3f)

      (Ns(eid).floats(float2, other2, float3, float4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/floats`:" +
        "\n2.0" +
        "\n3.0"
    }
  }
}
