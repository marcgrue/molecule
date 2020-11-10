package molecule.coretests.crud.updateMap

import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.peer.api.out1._
import scala.concurrent.ExecutionContext.Implicits.global


class UpdateMapInt extends CoreSpec {

  "Async" in new CoreSetup {

    // Update asynchronously and return Future[TxReport]
    // Calls Datomic's transactAsync API

    // Initial data
    Ns.intMap("str1" -> 1).saveAsync map { tx => // tx report from successful insert transaction
      // Inserted entity
      val e = tx.eid
      Ns.intMap.get === List("str1" -> 1)

      // Update entity asynchronously
      Ns(e).intMap.assert("str1" -> 10).updateAsync.map { tx2 => // tx report from successful update transaction
        // Current data
        Ns.intMap.get === List("str1" -> 10)
      }
    }

    // For brevity, the synchronous equivalent `update` is used in the following tests
  }


  "Mapped values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1).save.eid

      // Add pair
      Ns(eid).intMap.assert("str2" -> 3).update
      Ns.intMap.get.head === Map("str1" -> 1, "str2" -> 3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).intMap.assert("str2" -> 2).update
      Ns.intMap.get.head === Map("str1" -> 1, "str2" -> 2)

      // Add multiple pairs (vararg)
      Ns(eid).intMap.assert("str3" -> 3, "str4" -> 4).update
      Ns.intMap.get.head === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).intMap.assert(Seq("str4" -> 4, "str5" -> 5)).update
      Ns.intMap.get.head === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Add empty Map of pair (no effect)
      Ns(eid).intMap.assert(Seq[(String, Int)]()).update
      Ns.intMap.get.head === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).intMap.assert("str1" -> 1, "str1" -> 2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      // Seq
      expectCompileError(
        """Ns(eid).intMap.assert(Seq("str1" -> 1, "str1" -> 2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")
    }


    "replace" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 6).save.eid

      // Replace value
      Ns(eid).intMap.replace("str6" -> 8).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 8)

      // Replace value to existing value at another key is ok
      Ns(eid).intMap.replace("str5" -> 8).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 8, "str6" -> 8)

      // Replace multiple values (vararg)
      Ns(eid).intMap.replace("str3" -> 6, "str4" -> 7).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).intMap.replace("str3" -> 6, "str4" -> 7).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).intMap.replace(Seq("str2" -> 5)).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 5, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).intMap.replace(Seq[(String, Int)]()).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 5, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap.replace("str1" -> 1, "str1" -> 2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      expectCompileError(
        """Ns(eid).intMap.replace(Seq("str1" -> 1, "str1" -> 2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      (Ns(eid).intMap.replace("unknownKey" -> 42).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  " +
        "Can't replace non-existing keys of map attribute `:Ns/intMap`:" +
        "\nunknownKey" +
        "\nYou might want to apply the values instead to replace all current values?"
    }


    "retract" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 6).save.eid

      // Remove pair by key
      Ns(eid).intMap.retract("str6").update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Removing pair by non-existing key has no effect
      Ns(eid).intMap.retract("str7").update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).intMap.retract("str5", "str5").update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).intMap.retract("str3", "str4").update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1, "str2" -> 2)

      // Remove pairs by Seq of keys
      Ns(eid).intMap.retract(Seq("str2")).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).intMap.retract(Seq[String]()).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).intMap("str1" -> 1).update
      Ns.intMap.get.head.toList.sorted === List("str1" -> 1)

      // Apply multiple values (vararg)
      Ns(eid).intMap("str2" -> 2, "str3" -> 3).update
      Ns.intMap.get.head.toList.sorted === List("str2" -> 2, "str3" -> 3)

      // Apply Map of values
      Ns(eid).intMap(Seq("str4" -> 4)).update
      Ns.intMap.get.head.toList.sorted === List("str4" -> 4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).intMap(Seq[(String, Int)]()).update
      Ns.intMap.get === List()


      Ns(eid).intMap(Seq("str1" -> 1, "str2" -> 2)).update

      // Delete all (apply no values)
      Ns(eid).intMap().update
      Ns.intMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).intMap("str1" -> 1, "str1" -> 2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\nstr1 -> 1" +
        "\nstr1 -> 2"

      // Seq
      (Ns(eid).intMap(Seq("str1" -> 1, "str1" -> 2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\nstr1 -> 1" +
        "\nstr1 -> 2"
    }
  }


  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1).save.eid

      // Add pair
      Ns(eid).intMap.assert(str2 -> int3).update
      Ns.intMap.get.head === Map(str1 -> int1, str2 -> int3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).intMap.assert(str2 -> int2).update
      Ns.intMap.get.head === Map(str1 -> int1, str2 -> int2)

      // Add multiple pairs (vararg)
      Ns(eid).intMap.assert(str3 -> int3, str4 -> int4).update
      Ns.intMap.get.head === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).intMap.assert(Seq(str4 -> int4, str5 -> int5)).update
      Ns.intMap.get.head === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Add empty Map of pair (no effect)
      Ns(eid).intMap.assert(Seq[(String, Int)]()).update
      Ns.intMap.get.head === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).intMap.assert(str1 -> int1, str1 -> int2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      // Seq
      expectCompileError(
        """Ns(eid).intMap.assert(Seq(str1 -> int1, str1 -> int2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).intMap.assert(str1 -> int1, str1x -> int2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"


      // Seq
      (Ns(eid).intMap.assert(Seq(str1 -> int1, str1x -> int2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }


    "replace" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int6).save.eid

      // Replace value
      Ns(eid).intMap.replace(str6 -> int8).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int8)

      // Replace value to existing value at another key is ok
      Ns(eid).intMap.replace(str5 -> int8).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int8, str6 -> int8)

      // Replace multiple values (vararg)
      Ns(eid).intMap.replace(str3 -> int6, str4 -> int7).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).intMap.replace(str3 -> int6, str4 -> int7).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).intMap.replace(Seq(str2 -> int5)).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int5, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).intMap.replace(Seq[(String, Int)]()).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int5, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap.replace(str1 -> int1, str1 -> int2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      expectCompileError(
        """Ns(eid).intMap.replace(Seq(str1 -> int1, str1 -> int2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int6).save.eid

      // Remove pair by key
      Ns(eid).intMap.retract(str6).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Removing pair by non-existing key has no effect
      Ns(eid).intMap.retract(str7).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).intMap.retract(str5, str5).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).intMap.retract(str3, str4).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1, str2 -> int2)

      // Remove pairs by Seq of keys
      Ns(eid).intMap.retract(Seq(str2)).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).intMap.retract(Seq[String]()).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).intMap(str1 -> int1).update
      Ns.intMap.get.head.toList.sorted === List(str1 -> int1)

      // Apply multiple values (vararg)
      Ns(eid).intMap(str2 -> int2, str3 -> int3).update
      Ns.intMap.get.head.toList.sorted === List(str2 -> int2, str3 -> int3)

      // Apply Map of values
      Ns(eid).intMap(Seq(str4 -> int4)).update
      Ns.intMap.get.head.toList.sorted === List(str4 -> int4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).intMap(Seq[(String, Int)]()).update
      Ns.intMap.get === List()


      Ns(eid).intMap(Seq(str1 -> int1, str2 -> int2)).update

      // Delete all (apply no values)
      Ns(eid).intMap().update
      Ns.intMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).intMap(str1 -> int1, str1 -> int2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"

      // Seq
      (Ns(eid).intMap(Seq(str1 -> int1, str1 -> int2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }
  }
}
