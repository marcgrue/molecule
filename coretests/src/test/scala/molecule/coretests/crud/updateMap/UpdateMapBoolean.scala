package molecule.coretests.crud.updateMap

import molecule.api.out1._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

class UpdateMapBoolean extends CoreSpec {


  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.boolMap(str1 -> bool1).save.eid

      // Add pair
      Ns(eid).boolMap.assert(str2 -> bool3).update
      Ns.boolMap.get.head === Map(str1 -> bool1, str2 -> bool3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).boolMap.assert(str2 -> bool2).update
      Ns.boolMap.get.head === Map(str1 -> bool1, str2 -> bool2)

      // Add multiple pairs (vararg)
      Ns(eid).boolMap.assert(str3 -> bool3, str4 -> bool4).update
      Ns.boolMap.get.head === Map(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).boolMap.assert(Seq(str4 -> bool4, str5 -> bool5)).update
      Ns.boolMap.get.head === Map(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5)

      // Add empty Map of pair (no effect)
      Ns(eid).boolMap.assert(Seq[(String, Boolean)]()).update
      Ns.boolMap.get.head === Map(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).boolMap.assert(str1 -> bool1, str1 -> bool2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
          "\n__ident__str1 -> __ident__bool1" +
          "\n__ident__str1 -> __ident__bool2")

      // Seq
      expectCompileError(
        """Ns(eid).boolMap.assert(Seq(str1 -> bool1, str1 -> bool2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't assert multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
          "\n__ident__str1 -> __ident__bool1" +
          "\n__ident__str1 -> __ident__bool2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).boolMap.assert(str1 -> bool1, str1x -> bool2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
        "\na -> true" +
        "\na -> false"


      // Seq
      (Ns(eid).boolMap.assert(Seq(str1 -> bool1, str1x -> bool2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
        "\na -> true" +
        "\na -> false"
    }


    "replace" in new CoreSetup {

      val eid = Ns.boolMap(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5, str6 -> bool6).save.eid

      // Replace value
      Ns(eid).boolMap.replace(str6 -> bool8).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5, str6 -> bool8)

      // Replace value to existing value at another key is ok
      Ns(eid).boolMap.replace(str5 -> bool8).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool8, str6 -> bool8)

      // Replace multiple values (vararg)
      Ns(eid).boolMap.replace(str3 -> bool6, str4 -> bool7).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool6, str4 -> bool7, str5 -> bool8, str6 -> bool8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).boolMap.replace(str3 -> bool6, str4 -> bool7).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool6, str4 -> bool7, str5 -> bool8, str6 -> bool8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).boolMap.replace(Seq(str2 -> bool5)).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool5, str3 -> bool6, str4 -> bool7, str5 -> bool8, str6 -> bool8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).boolMap.replace(Seq[(String, Boolean)]()).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool5, str3 -> bool6, str4 -> bool7, str5 -> bool8, str6 -> bool8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).boolMap.replace(str1 -> bool1, str1 -> bool2).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
          "\n__ident__str1 -> __ident__bool1" +
          "\n__ident__str1 -> __ident__bool2")

      expectCompileError(
        """Ns(eid).boolMap.replace(Seq(str1 -> bool1, str1 -> bool2)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace multiple key/value pairs with the same key for attribute `:ns/boolMap`:" +
          "\n__ident__str1 -> __ident__bool1" +
          "\n__ident__str1 -> __ident__bool2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.boolMap(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5, str6 -> bool6).save.eid

      // Remove pair by key
      Ns(eid).boolMap.retract(str6).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5)

      // Removing pair by non-existing key has no effect
      Ns(eid).boolMap.retract(str7).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4, str5 -> bool5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).boolMap.retract(str5, str5).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2, str3 -> bool3, str4 -> bool4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).boolMap.retract(str3, str4).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1, str2 -> bool2)

      // Remove pairs by Seq of keys
      Ns(eid).boolMap.retract(Seq(str2)).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).boolMap.retract(Seq[String]()).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.boolMap(str1 -> bool1, str2 -> bool2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).boolMap(str1 -> bool1).update
      Ns.boolMap.get.head.toList.sorted === List(str1 -> bool1)

      // Apply multiple values (vararg)
      Ns(eid).boolMap(str2 -> bool2, str3 -> bool3).update
      Ns.boolMap.get.head.toList.sorted === List(str2 -> bool2, str3 -> bool3)

      // Apply Map of values
      Ns(eid).boolMap(Seq(str4 -> bool4)).update
      Ns.boolMap.get.head.toList.sorted === List(str4 -> bool4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).boolMap(Seq[(String, Boolean)]()).update
      Ns.boolMap.get === List()


      Ns(eid).boolMap(Seq(str1 -> bool1, str2 -> bool2)).update

      // Delete all (apply no values)
      Ns(eid).boolMap().update
      Ns.boolMap.get === List()


      // Can't apply pairs with duplicate keys
      (Ns(eid).longMap(str1 -> long1, str1 -> long2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"

      (Ns(eid).longMap(Seq(str1 -> long1, str1 -> long2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }
  }
}
