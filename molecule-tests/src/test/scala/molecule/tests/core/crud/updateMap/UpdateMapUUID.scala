package molecule.tests.core.crud.updateMap

import java.util.UUID
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.CoreTest._
import molecule.datomic.api.out1._
import molecule.datomic.base.transform.exception.Model2TransactionException
import molecule.setup.TestSpec

class UpdateMapUUID extends TestSpec {

  "Mapped variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.uuidMap(str1 -> uuid1).save.eid

      // Add pair
      Ns(eid).uuidMap.assert(str2 -> uuid3).update
      Ns.uuidMap.get.head === Map(str1 -> uuid1, str2 -> uuid3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).uuidMap.assert(str2 -> uuid2).update
      Ns.uuidMap.get.head === Map(str1 -> uuid1, str2 -> uuid2)

      // Add multiple pairs (vararg)
      Ns(eid).uuidMap.assert(str3 -> uuid3, str4 -> uuid4).update
      Ns.uuidMap.get.head === Map(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).uuidMap.assert(Seq(str4 -> uuid4, str5 -> uuid5)).update
      Ns.uuidMap.get.head === Map(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5)

      // Add empty Map of pair (no effect)
      Ns(eid).uuidMap.assert(Seq[(String, UUID)]()).update
      Ns.uuidMap.get.head === Map(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).uuidMap.assert(str1 -> uuid1, str1 -> uuid2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
          "\n__ident__str1 -> __ident__uuid1" +
          "\n__ident__str1 -> __ident__uuid2")

      // Seq
      expectCompileError(
        """Ns(eid).uuidMap.assert(Seq(str1 -> uuid1, str1 -> uuid2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
          "\n__ident__str1 -> __ident__uuid1" +
          "\n__ident__str1 -> __ident__uuid2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).uuidMap.assert(str1 -> uuid1, str1x -> uuid2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
        "\na -> " + uuid1 +
        "\na -> " + uuid2


      // Seq
      (Ns(eid).uuidMap.assert(Seq(str1 -> uuid1, str1x -> uuid2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
        "\na -> " + uuid1 +
        "\na -> " + uuid2
    }


    "replace" in new CoreSetup {

      val eid = Ns.uuidMap(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5, str6 -> uuid6).save.eid

      // Replace value
      Ns(eid).uuidMap.replace(str6 -> uuid8).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5, str6 -> uuid8)

      // Replace value to existing value at another key is ok
      Ns(eid).uuidMap.replace(str5 -> uuid8).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid8, str6 -> uuid8)

      // Replace multiple values (vararg)
      Ns(eid).uuidMap.replace(str3 -> uuid6, str4 -> uuid7).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid6, str4 -> uuid7, str5 -> uuid8, str6 -> uuid8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).uuidMap.replace(str3 -> uuid6, str4 -> uuid7).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid6, str4 -> uuid7, str5 -> uuid8, str6 -> uuid8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).uuidMap.replace(Seq(str2 -> uuid5)).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid5, str3 -> uuid6, str4 -> uuid7, str5 -> uuid8, str6 -> uuid8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).uuidMap.replace(Seq[(String, UUID)]()).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid5, str3 -> uuid6, str4 -> uuid7, str5 -> uuid8, str6 -> uuid8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).uuidMap.replace(str1 -> uuid1, str1 -> uuid2).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
          "\n__ident__str1 -> __ident__uuid1" +
          "\n__ident__str1 -> __ident__uuid2")

      expectCompileError(
        """Ns(eid).uuidMap.replace(Seq(str1 -> uuid1, str1 -> uuid2)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
          "\n__ident__str1 -> __ident__uuid1" +
          "\n__ident__str1 -> __ident__uuid2")
    }


    "retract" in new CoreSetup {

      val eid = Ns.uuidMap(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5, str6 -> uuid6).save.eid

      // Remove pair by key
      Ns(eid).uuidMap.retract(str6).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5)

      // Removing pair by non-existing key has no effect
      Ns(eid).uuidMap.retract(str7).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4, str5 -> uuid5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).uuidMap.retract(str5, str5).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2, str3 -> uuid3, str4 -> uuid4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).uuidMap.retract(str3, str4).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1, str2 -> uuid2)

      // Remove pairs by Seq of keys
      Ns(eid).uuidMap.retract(Seq(str2)).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).uuidMap.retract(Seq[String]()).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.uuidMap(str1 -> uuid1, str2 -> uuid2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).uuidMap(str1 -> uuid1).update
      Ns.uuidMap.get.head.toList.sorted === List(str1 -> uuid1)

      // Apply multiple values (vararg)
      Ns(eid).uuidMap(str2 -> uuid2, str3 -> uuid3).update
      Ns.uuidMap.get.head.toList.sorted === List(str2 -> uuid2, str3 -> uuid3)

      // Apply Map of values
      Ns(eid).uuidMap(Seq(str4 -> uuid4)).update
      Ns.uuidMap.get.head.toList.sorted === List(str4 -> uuid4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).uuidMap(Seq[(String, UUID)]()).update
      Ns.uuidMap.get === List()


      Ns(eid).uuidMap(Seq(str1 -> uuid1, str2 -> uuid2)).update

      // Delete all (apply no values)
      Ns(eid).uuidMap().update
      Ns.uuidMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).uuidMap(str1 -> uuid1, str1 -> uuid2).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
        "\na -> " + uuid1 +
        "\na -> " + uuid2


      // Seq
      (Ns(eid).uuidMap(Seq(str1 -> uuid1, str1 -> uuid2)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
        "\na -> " + uuid1 +
        "\na -> " + uuid2

//      expectCompileError(
//        """Ns(eid).uuidMap(str1 -> uuid1, str1 -> uuid2).update""",
//        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
//          "\n__ident__str1 -> __ident__uuid1" +
//          "\n__ident__str1 -> __ident__uuid2")
//
//      expectCompileError(
//        """Ns(eid).uuidMap(Seq(str1 -> uuid1, str1 -> uuid2)).update""",
//        "molecule.core.ops.exception.VerifyRawModelException: Can't assert multiple key/value pairs with the same key for attribute `:Ns/uuidMap`:" +
//          "\n__ident__str1 -> __ident__uuid1" +
//          "\n__ident__str1 -> __ident__uuid2")
    }
  }
}
