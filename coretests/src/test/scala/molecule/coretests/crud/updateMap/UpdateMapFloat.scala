package molecule.coretests.crud.updateMap

import molecule.api.out1._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

class UpdateMapFloat extends CoreSpec {


  "Mapped values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.floatMap("str1" -> 1f).save.eid

      // Add pair
      Ns(eid).floatMap.assert("str2" -> 3f).update
      Ns.floatMap.get.head === Map("str1" -> 1f, "str2" -> 3f)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).floatMap.assert("str2" -> 2f).update
      Ns.floatMap.get.head === Map("str1" -> 1f, "str2" -> 2f)

      // Add multiple pairs (vararg)
      Ns(eid).floatMap.assert("str3" -> 3f, "str4" -> 4f).update
      Ns.floatMap.get.head === Map("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).floatMap.assert(Seq("str4" -> 4f, "str5" -> 5f)).update
      Ns.floatMap.get.head === Map("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f)

      // Add empty Map of pair (no effect)
      Ns(eid).floatMap.assert(Seq[(String, Float)]()).update
      Ns.floatMap.get.head === Map("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).floatMap.assert("str1" -> 1f, "str1" -> 2f).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")

      // Seq
      expectCompileError(
        """Ns(eid).floatMap.assert(Seq("str1" -> 1f, "str1" -> 2f)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")
    }


    "replace" in new CoreSetup {

      val eid = Ns.floatMap("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f, "str6" -> 6f).save.eid

      // Replace value
      Ns(eid).floatMap.replace("str6" -> 8f).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f, "str6" -> 8f)

      // Replace value to existing value at another key is ok
      Ns(eid).floatMap.replace("str5" -> 8f).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 8f, "str6" -> 8f)

      // Replace multiple values (vararg)
      Ns(eid).floatMap.replace("str3" -> 6f, "str4" -> 7f).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 6f, "str4" -> 7f, "str5" -> 8f, "str6" -> 8f)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floatMap.replace("str3" -> 6f, "str4" -> 7f).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 6f, "str4" -> 7f, "str5" -> 8f, "str6" -> 8f)

      // Replace with Seq of key/newValue pairs
      Ns(eid).floatMap.replace(Seq("str2" -> 5f)).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 5f, "str3" -> 6f, "str4" -> 7f, "str5" -> 8f, "str6" -> 8f)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).floatMap.replace(Seq[(String, Float)]()).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 5f, "str3" -> 6f, "str4" -> 7f, "str5" -> 8f, "str6" -> 8f)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).floatMap.replace("str1" -> 1f, "str1" -> 2f).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")

      expectCompileError(
        """Ns(eid).floatMap.replace(Seq("str1" -> 1f, "str1" -> 2f)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")
    }


    "retract" in new CoreSetup {

      val eid = Ns.floatMap("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f, "str6" -> 6f).save.eid

      // Remove pair by key
      Ns(eid).floatMap.retract("str6").update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f)

      // Removing pair by non-existing key has no effect
      Ns(eid).floatMap.retract("str7").update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f, "str5" -> 5f)

      // Removing duplicate keys removes the distinct key
      Ns(eid).floatMap.retract("str5", "str5").update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f, "str3" -> 3f, "str4" -> 4f)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).floatMap.retract("str3", "str4").update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f, "str2" -> 2f)

      // Remove pairs by Seq of keys
      Ns(eid).floatMap.retract(Seq("str2")).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).floatMap.retract(Seq[String]()).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floatMap("str1" -> 1f, "str2" -> 2f).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).floatMap("str1" -> 1f).update
      Ns.floatMap.get.head.toList.sorted === List("str1" -> 1f)

      // Apply multiple values (vararg)
      Ns(eid).floatMap("str2" -> 2f, "str3" -> 3f).update
      Ns.floatMap.get.head.toList.sorted === List("str2" -> 2f, "str3" -> 3f)

      // Apply Map of values
      Ns(eid).floatMap(Seq("str4" -> 4f)).update
      Ns.floatMap.get.head.toList.sorted === List("str4" -> 4f)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).floatMap(Seq[(String, Float)]()).update
      Ns.floatMap.get === List()


      Ns(eid).floatMap(Seq("str1" -> 1f, "str2" -> 2f)).update

      // Delete all (apply no values)
      Ns(eid).floatMap().update
      Ns.floatMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).floatMap("str1" -> 1f, "str1" -> 2f).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\nstr1 -> 1.0" +
        "\nstr1 -> 2.0"

      // Seq
      (Ns(eid).floatMap(Seq("str1" -> 1f, "str1" -> 2f)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\nstr1 -> 1.0" +
        "\nstr1 -> 2.0"
    }
  }


  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.floatMap(str1 -> float1).save.eid

      // Add pair
      Ns(eid).floatMap.assert(str2 -> float3).update
      Ns.floatMap.get.head === Map(str1 -> float1, str2 -> float3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).floatMap.assert(str2 -> float2).update
      Ns.floatMap.get.head === Map(str1 -> float1, str2 -> float2)

      // Add multiple pairs (vararg)
      Ns(eid).floatMap.assert(str3 -> float3, str4 -> float4).update
      Ns.floatMap.get.head === Map(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).floatMap.assert(Seq(str4 -> float4, str5 -> float5)).update
      Ns.floatMap.get.head === Map(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5)

      // Add empty Map of pair (no effect)
      Ns(eid).floatMap.assert(Seq[(String, Float)]()).update
      Ns.floatMap.get.head === Map(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).floatMap.assert(str1 -> float1, str1 -> float2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\n__ident__str1 -> __ident__float1" +
          "\n__ident__str1 -> __ident__float2")

      // Seq
      expectCompileError(
        """Ns(eid).floatMap.assert(Seq(str1 -> float1, str1 -> float2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\n__ident__str1 -> __ident__float1" +
          "\n__ident__str1 -> __ident__float2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).floatMap.assert(str1 -> float1, str1x -> float2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"


      // Seq
      (Ns(eid).floatMap.assert(Seq(str1 -> float1, str1x -> float2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"
    }


    "replace" in new CoreSetup {

      val eid = Ns.floatMap(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5, str6 -> float6).save.eid

      // Replace value
      Ns(eid).floatMap.replace(str6 -> float8).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5, str6 -> float8)

      // Replace value to existing value at another key is ok
      Ns(eid).floatMap.replace(str5 -> float8).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float8, str6 -> float8)

      // Replace multiple values (vararg)
      Ns(eid).floatMap.replace(str3 -> float6, str4 -> float7).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float6, str4 -> float7, str5 -> float8, str6 -> float8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).floatMap.replace(str3 -> float6, str4 -> float7).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float6, str4 -> float7, str5 -> float8, str6 -> float8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).floatMap.replace(Seq(str2 -> float5)).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float5, str3 -> float6, str4 -> float7, str5 -> float8, str6 -> float8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).floatMap.replace(Seq[(String, Float)]()).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float5, str3 -> float6, str4 -> float7, str5 -> float8, str6 -> float8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).floatMap.replace(str1 -> float1, str1 -> float2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\n__ident__str1 -> __ident__float1" +
          "\n__ident__str1 -> __ident__float2")

      expectCompileError(
        """Ns(eid).floatMap.replace(Seq(str1 -> float1, str1 -> float2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
          "\n__ident__str1 -> __ident__float1" +
          "\n__ident__str1 -> __ident__float2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.floatMap(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5, str6 -> float6).save.eid

      // Remove pair by key
      Ns(eid).floatMap.retract(str6).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5)

      // Removing pair by non-existing key has no effect
      Ns(eid).floatMap.retract(str7).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4, str5 -> float5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).floatMap.retract(str5, str5).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2, str3 -> float3, str4 -> float4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).floatMap.retract(str3, str4).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1, str2 -> float2)

      // Remove pairs by Seq of keys
      Ns(eid).floatMap.retract(Seq(str2)).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).floatMap.retract(Seq[String]()).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.floatMap(str1 -> float1, str2 -> float2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).floatMap(str1 -> float1).update
      Ns.floatMap.get.head.toList.sorted === List(str1 -> float1)

      // Apply multiple values (vararg)
      Ns(eid).floatMap(str2 -> float2, str3 -> float3).update
      Ns.floatMap.get.head.toList.sorted === List(str2 -> float2, str3 -> float3)

      // Apply Map of values
      Ns(eid).floatMap(Seq(str4 -> float4)).update
      Ns.floatMap.get.head.toList.sorted === List(str4 -> float4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).floatMap(Seq[(String, Float)]()).update
      Ns.floatMap.get === List()


      Ns(eid).floatMap(Seq(str1 -> float1, str2 -> float2)).update

      // Delete all (apply no values)
      Ns(eid).floatMap().update
      Ns.floatMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).floatMap(str1 -> float1, str1 -> float2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"

      // Seq
      (Ns(eid).floatMap(Seq(str1 -> float1, str1 -> float2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/floatMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"
    }
  }
}
