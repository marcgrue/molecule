package molecule.coretests.crud.updateMap

import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out1._

class UpdateMapDouble extends CoreSpec {

  "Mapped values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.doubleMap("str1" -> 1.0).save.eid

      // Add pair
      Ns(eid).doubleMap.assert("str2" -> 3.0).update
      Ns.doubleMap.get.head === Map("str1" -> 1.0, "str2" -> 3.0)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).doubleMap.assert("str2" -> 2.0).update
      Ns.doubleMap.get.head === Map("str1" -> 1.0, "str2" -> 2.0)

      // Add multiple pairs (vararg)
      Ns(eid).doubleMap.assert("str3" -> 3.0, "str4" -> 4.0).update
      Ns.doubleMap.get.head === Map("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).doubleMap.assert(Seq("str4" -> 4.0, "str5" -> 5.0)).update
      Ns.doubleMap.get.head === Map("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0)

      // Add empty Map of pair (no effect)
      Ns(eid).doubleMap.assert(Seq[(String, Double)]()).update
      Ns.doubleMap.get.head === Map("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).doubleMap.assert("str1" -> 1.0, "str1" -> 2.0).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")

      // Seq
      expectCompileError(
        """Ns(eid).doubleMap.assert(Seq("str1" -> 1.0, "str1" -> 2.0)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubleMap("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0, "str6" -> 6.0).save.eid

      // Replace value
      Ns(eid).doubleMap.replace("str6" -> 8.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0, "str6" -> 8.0)

      // Replace value to existing value at another key is ok
      Ns(eid).doubleMap.replace("str5" -> 8.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 8.0, "str6" -> 8.0)

      // Replace multiple values (vararg)
      Ns(eid).doubleMap.replace("str3" -> 6.0, "str4" -> 7.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 6.0, "str4" -> 7.0, "str5" -> 8.0, "str6" -> 8.0)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubleMap.replace("str3" -> 6.0, "str4" -> 7.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 6.0, "str4" -> 7.0, "str5" -> 8.0, "str6" -> 8.0)

      // Replace with Seq of key/newValue pairs
      Ns(eid).doubleMap.replace(Seq("str2" -> 5.0)).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 5.0, "str3" -> 6.0, "str4" -> 7.0, "str5" -> 8.0, "str6" -> 8.0)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).doubleMap.replace(Seq[(String, Double)]()).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 5.0, "str3" -> 6.0, "str4" -> 7.0, "str5" -> 8.0, "str6" -> 8.0)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).doubleMap.replace("str1" -> 1.0, "str1" -> 2.0).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")

      expectCompileError(
        """Ns(eid).doubleMap.replace(Seq("str1" -> 1.0, "str1" -> 2.0)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\nstr1 -> 1.0" +
          "\nstr1 -> 2.0")
    }


    "retract" in new CoreSetup {

      val eid = Ns.doubleMap("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0, "str6" -> 6.0).save.eid

      // Remove pair by key
      Ns(eid).doubleMap.retract("str6").update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0)

      // Removing pair by non-existing key has no effect
      Ns(eid).doubleMap.retract("str7").update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0, "str5" -> 5.0)

      // Removing duplicate keys removes the distinct key
      Ns(eid).doubleMap.retract("str5", "str5").update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0, "str3" -> 3.0, "str4" -> 4.0)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).doubleMap.retract("str3", "str4").update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0, "str2" -> 2.0)

      // Remove pairs by Seq of keys
      Ns(eid).doubleMap.retract(Seq("str2")).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).doubleMap.retract(Seq[String]()).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubleMap("str1" -> 1.0, "str2" -> 2.0).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).doubleMap("str1" -> 1.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str1" -> 1.0)

      // Apply multiple values (vararg)
      Ns(eid).doubleMap("str2" -> 2.0, "str3" -> 3.0).update
      Ns.doubleMap.get.head.toList.sorted === List("str2" -> 2.0, "str3" -> 3.0)

      // Apply Map of values
      Ns(eid).doubleMap(Seq("str4" -> 4.0)).update
      Ns.doubleMap.get.head.toList.sorted === List("str4" -> 4.0)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).doubleMap(Seq[(String, Double)]()).update
      Ns.doubleMap.get === List()


      Ns(eid).doubleMap(Seq("str1" -> 1.0, "str2" -> 2.0)).update

      // Delete all (apply no values)
      Ns(eid).doubleMap().update
      Ns.doubleMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).doubleMap("str1" -> 1.0, "str1" -> 2.0).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\nstr1 -> 1.0" +
        "\nstr1 -> 2.0"

      // Seq
      (Ns(eid).doubleMap(Seq("str1" -> 1.0, "str1" -> 2.0)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\nstr1 -> 1.0" +
        "\nstr1 -> 2.0"
    }
  }


  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.doubleMap(str1 -> double1).save.eid

      // Add pair
      Ns(eid).doubleMap.assert(str2 -> double3).update
      Ns.doubleMap.get.head === Map(str1 -> double1, str2 -> double3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).doubleMap.assert(str2 -> double2).update
      Ns.doubleMap.get.head === Map(str1 -> double1, str2 -> double2)

      // Add multiple pairs (vararg)
      Ns(eid).doubleMap.assert(str3 -> double3, str4 -> double4).update
      Ns.doubleMap.get.head === Map(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).doubleMap.assert(Seq(str4 -> double4, str5 -> double5)).update
      Ns.doubleMap.get.head === Map(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5)

      // Add empty Map of pair (no effect)
      Ns(eid).doubleMap.assert(Seq[(String, Double)]()).update
      Ns.doubleMap.get.head === Map(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).doubleMap.assert(str1 -> double1, str1 -> double2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\n__ident__str1 -> __ident__double1" +
          "\n__ident__str1 -> __ident__double2")

      // Seq
      expectCompileError(
        """Ns(eid).doubleMap.assert(Seq(str1 -> double1, str1 -> double2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\n__ident__str1 -> __ident__double1" +
          "\n__ident__str1 -> __ident__double2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).doubleMap.assert(str1 -> double1, str1x -> double2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"

      // Seq
      (Ns(eid).doubleMap.assert(Seq(str1 -> double1, str1x -> double2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubleMap(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5, str6 -> double6).save.eid

      // Replace value
      Ns(eid).doubleMap.replace(str6 -> double8).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5, str6 -> double8)

      // Replace value to existing value at another key is ok
      Ns(eid).doubleMap.replace(str5 -> double8).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double8, str6 -> double8)

      // Replace multiple values (vararg)
      Ns(eid).doubleMap.replace(str3 -> double6, str4 -> double7).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double6, str4 -> double7, str5 -> double8, str6 -> double8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubleMap.replace(str3 -> double6, str4 -> double7).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double6, str4 -> double7, str5 -> double8, str6 -> double8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).doubleMap.replace(Seq(str2 -> double5)).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double5, str3 -> double6, str4 -> double7, str5 -> double8, str6 -> double8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).doubleMap.replace(Seq[(String, Double)]()).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double5, str3 -> double6, str4 -> double7, str5 -> double8, str6 -> double8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).doubleMap.replace(str1 -> double1, str1 -> double2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\n__ident__str1 -> __ident__double1" +
          "\n__ident__str1 -> __ident__double2")

      expectCompileError(
        """Ns(eid).doubleMap.replace(Seq(str1 -> double1, str1 -> double2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
          "\n__ident__str1 -> __ident__double1" +
          "\n__ident__str1 -> __ident__double2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.doubleMap(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5, str6 -> double6).save.eid

      // Remove pair by key
      Ns(eid).doubleMap.retract(str6).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5)

      // Removing pair by non-existing key has no effect
      Ns(eid).doubleMap.retract(str7).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4, str5 -> double5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).doubleMap.retract(str5, str5).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2, str3 -> double3, str4 -> double4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).doubleMap.retract(str3, str4).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1, str2 -> double2)

      // Remove pairs by Seq of keys
      Ns(eid).doubleMap.retract(Seq(str2)).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).doubleMap.retract(Seq[String]()).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubleMap(str1 -> double1, str2 -> double2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).doubleMap(str1 -> double1).update
      Ns.doubleMap.get.head.toList.sorted === List(str1 -> double1)

      // Apply multiple values (vararg)
      Ns(eid).doubleMap(str2 -> double2, str3 -> double3).update
      Ns.doubleMap.get.head.toList.sorted === List(str2 -> double2, str3 -> double3)

      // Apply Map of values
      Ns(eid).doubleMap(Seq(str4 -> double4)).update
      Ns.doubleMap.get.head.toList.sorted === List(str4 -> double4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).doubleMap(Seq[(String, Double)]()).update
      Ns.doubleMap.get === List()


      Ns(eid).doubleMap(Seq(str1 -> double1, str2 -> double2)).update

      // Delete all (apply no values)
      Ns(eid).doubleMap().update
      Ns.doubleMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).doubleMap(str1 -> double1, str1 -> double2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"

      // Seq
      (Ns(eid).doubleMap(Seq(str1 -> double1, str1 -> double2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/doubleMap`:" +
        "\na -> 1.0" +
        "\na -> 2.0"
    }
  }
}
