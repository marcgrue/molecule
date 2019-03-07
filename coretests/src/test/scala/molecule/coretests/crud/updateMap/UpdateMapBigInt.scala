package molecule.coretests.crud.updateMap

import molecule.api.out1._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

class UpdateMapBigInt extends CoreSpec {


  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.bigIntMap(str1 -> bigInt1).save.eid

      // Add pair
      Ns(eid).bigIntMap.assert(str2 -> bigInt3).update
      Ns.bigIntMap.get.head === Map(str1 -> bigInt1, str2 -> bigInt3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).bigIntMap.assert(str2 -> bigInt2).update
      Ns.bigIntMap.get.head === Map(str1 -> bigInt1, str2 -> bigInt2)

      // Add multiple pairs (vararg)
      Ns(eid).bigIntMap.assert(str3 -> bigInt3, str4 -> bigInt4).update
      Ns.bigIntMap.get.head === Map(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).bigIntMap.assert(Seq(str4 -> bigInt4, str5 -> bigInt5)).update
      Ns.bigIntMap.get.head === Map(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5)

      // Add empty Map of pair (no effect)
      Ns(eid).bigIntMap.assert(Seq[(String, BigInt)]()).update
      Ns.bigIntMap.get.head === Map(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).bigIntMap.assert(str1 -> bigInt1, str1 -> bigInt2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
          "\n__ident__str1 -> __ident__bigInt1" +
          "\n__ident__str1 -> __ident__bigInt2")

      // Seq
      expectCompileError(
        """Ns(eid).bigIntMap.assert(Seq(str1 -> bigInt1, str1 -> bigInt2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
          "\n__ident__str1 -> __ident__bigInt1" +
          "\n__ident__str1 -> __ident__bigInt2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).bigIntMap.assert(str1 -> bigInt1, str1x -> bigInt2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
        "\na -> " + bigInt1 +
        "\na -> " + bigInt2


      // Seq
      (Ns(eid).bigIntMap.assert(Seq(str1 -> bigInt1, str1x -> bigInt2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
        "\na -> " + bigInt1 +
        "\na -> " + bigInt2
    }


    "replace" in new CoreSetup {

      val eid = Ns.bigIntMap(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5, str6 -> bigInt6).save.eid

      // Replace value
      Ns(eid).bigIntMap.replace(str6 -> bigInt8).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5, str6 -> bigInt8)

      // Replace value to existing value at another key is ok
      Ns(eid).bigIntMap.replace(str5 -> bigInt8).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt8, str6 -> bigInt8)

      // Replace multiple values (vararg)
      Ns(eid).bigIntMap.replace(str3 -> bigInt6, str4 -> bigInt7).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt6, str4 -> bigInt7, str5 -> bigInt8, str6 -> bigInt8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).bigIntMap.replace(str3 -> bigInt6, str4 -> bigInt7).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt6, str4 -> bigInt7, str5 -> bigInt8, str6 -> bigInt8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).bigIntMap.replace(Seq(str2 -> bigInt5)).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt5, str3 -> bigInt6, str4 -> bigInt7, str5 -> bigInt8, str6 -> bigInt8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).bigIntMap.replace(Seq[(String, BigInt)]()).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt5, str3 -> bigInt6, str4 -> bigInt7, str5 -> bigInt8, str6 -> bigInt8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).bigIntMap.replace(str1 -> bigInt1, str1 -> bigInt2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
          "\n__ident__str1 -> __ident__bigInt1" +
          "\n__ident__str1 -> __ident__bigInt2")

      expectCompileError(
        """Ns(eid).bigIntMap.replace(Seq(str1 -> bigInt1, str1 -> bigInt2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
          "\n__ident__str1 -> __ident__bigInt1" +
          "\n__ident__str1 -> __ident__bigInt2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.bigIntMap(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5, str6 -> bigInt6).save.eid

      // Remove pair by key
      Ns(eid).bigIntMap.retract(str6).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5)

      // Removing pair by non-existing key has no effect
      Ns(eid).bigIntMap.retract(str7).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4, str5 -> bigInt5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).bigIntMap.retract(str5, str5).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2, str3 -> bigInt3, str4 -> bigInt4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).bigIntMap.retract(str3, str4).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1, str2 -> bigInt2)

      // Remove pairs by Seq of keys
      Ns(eid).bigIntMap.retract(Seq(str2)).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).bigIntMap.retract(Seq[String]()).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.bigIntMap(str1 -> bigInt1, str2 -> bigInt2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).bigIntMap(str1 -> bigInt1).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str1 -> bigInt1)

      // Apply multiple values (vararg)
      Ns(eid).bigIntMap(str2 -> bigInt2, str3 -> bigInt3).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str2 -> bigInt2, str3 -> bigInt3)

      // Apply Map of values
      Ns(eid).bigIntMap(Seq(str4 -> bigInt4)).update
      Ns.bigIntMap.get.head.toList.sortBy(_._1) === List(str4 -> bigInt4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).bigIntMap(Seq[(String, BigInt)]()).update
      Ns.bigIntMap.get === List()


      Ns(eid).bigIntMap(Seq(str1 -> bigInt1, str2 -> bigInt2)).update

      // Delete all (apply no values)
      Ns(eid).bigIntMap().update
      Ns.bigIntMap.get === List()


      // Can't apply pairs with duplicate keys

      (Ns(eid).bigIntMap(str1 -> bigInt1, str1 -> bigInt2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
        "\na -> " + bigInt1 +
        "\na -> " + bigInt2

      (Ns(eid).bigIntMap(Seq(str1 -> bigInt1, str1 -> bigInt2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/bigIntMap`:" +
        "\na -> " + bigInt1 +
        "\na -> " + bigInt2
    }
  }
}
