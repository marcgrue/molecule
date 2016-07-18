package molecule.manipulation.updateMap

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateMapLong extends CoreSpec {


  "Mapped values" >> {

    "add" in new CoreSetup {

      val eid = Ns.longMap("str1" -> 1L).save.eid

      // Add pair
      Ns(eid).longMap.add("str2" -> 3L).update
      Ns.longMap.one === Map("str1" -> 1L, "str2" -> 3L)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).longMap.add("str2" -> 2L).update
      Ns.longMap.one === Map("str1" -> 1L, "str2" -> 2L)

      // Add multiple pairs (vararg)
      Ns(eid).longMap.add("str3" -> 3L, "str4" -> 4L).update
      Ns.longMap.one === Map("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).longMap.add(Seq("str4" -> 4L, "str5" -> 5L)).update
      Ns.longMap.one === Map("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L)

      // Add empty Map of pair (no effect)
      Ns(eid).longMap.add(Seq[(String, Long)]()).update
      Ns.longMap.one === Map("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).longMap.add("str1" -> 1L, "str1" -> 2L).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\nstr1 -> 1L" +
          "\nstr1 -> 2L")

      // Seq
      expectCompileError(
        """Ns(eid).longMap.add(Seq("str1" -> 1L, "str1" -> 2L)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\nstr1 -> 1L" +
          "\nstr1 -> 2L")
    }


    "replace" in new CoreSetup {

      val eid = Ns.longMap("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L, "str6" -> 6L).save.eid

      // Replace value
      Ns(eid).longMap.replace("str6" -> 8L).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L, "str6" -> 8L)

      // Replace value to existing value at another key is ok
      Ns(eid).longMap.replace("str5" -> 8L).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 8L, "str6" -> 8L)

      // Replace multiple values (vararg)
      Ns(eid).longMap.replace("str3" -> 6L, "str4" -> 7L).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 6L, "str4" -> 7L, "str5" -> 8L, "str6" -> 8L)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longMap.replace("str3" -> 6L, "str4" -> 7L).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 6L, "str4" -> 7L, "str5" -> 8L, "str6" -> 8L)

      // Replace with Seq of key/newValue pairs
      Ns(eid).longMap.replace(Seq("str2" -> 5L)).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 5L, "str3" -> 6L, "str4" -> 7L, "str5" -> 8L, "str6" -> 8L)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).longMap.replace(Seq[(String, Long)]()).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 5L, "str3" -> 6L, "str4" -> 7L, "str5" -> 8L, "str6" -> 8L)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).longMap.replace("str1" -> 1L, "str1" -> 2L).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\nstr1 -> 1L" +
          "\nstr1 -> 2L")

      expectCompileError(
        """Ns(eid).longMap.replace(Seq("str1" -> 1L, "str1" -> 2L)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\nstr1 -> 1L" +
          "\nstr1 -> 2L")
    }


    "remove" in new CoreSetup {

      val eid = Ns.longMap("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L, "str6" -> 6L).save.eid

      // Remove pair by key
      Ns(eid).longMap.remove("str6").update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L)

      // Removing pair by non-existing key has no effect
      Ns(eid).longMap.remove("str7").update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L, "str5" -> 5L)

      // Removing duplicate keys removes the distinct key
      Ns(eid).longMap.remove("str5", "str5").update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L, "str3" -> 3L, "str4" -> 4L)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).longMap.remove("str3", "str4").update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L, "str2" -> 2L)

      // Remove pairs by Seq of keys
      Ns(eid).longMap.remove(Seq("str2")).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).longMap.remove(Seq[String]()).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longMap("str1" -> 1L, "str2" -> 2L).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).longMap("str1" -> 1L).update
      Ns.longMap.one.toList.sorted === List("str1" -> 1L)

      // Apply multiple values (vararg)
      Ns(eid).longMap("str2" -> 2L, "str3" -> 3L).update
      Ns.longMap.one.toList.sorted === List("str2" -> 2L, "str3" -> 3L)

      // Apply Map of values
      Ns(eid).longMap(Seq("str4" -> 4L)).update
      Ns.longMap.one.toList.sorted === List("str4" -> 4L)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).longMap(Seq[(String, Long)]()).update
      Ns.longMap.get === List()


      Ns(eid).longMap(Seq("str1" -> 1L, "str2" -> 2L)).update

      // Delete all (apply no values)
      Ns(eid).longMap().update
      Ns.longMap.get === List()


      // Can't apply pairs with duplicate keys

      (Ns(eid).longMap("str1" -> 1L, "str1" -> 2L).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\nstr1 -> 1" +
        "\nstr1 -> 2"

      (Ns(eid).longMap(Seq("str1" -> 1L, "str1" -> 2L)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\nstr1 -> 1" +
        "\nstr1 -> 2"
    }
  }


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.longMap(str1 -> long1).save.eid

      // Add pair
      Ns(eid).longMap.add(str2 -> long3).update
      Ns.longMap.one === Map(str1 -> long1, str2 -> long3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).longMap.add(str2 -> long2).update
      Ns.longMap.one === Map(str1 -> long1, str2 -> long2)

      // Add multiple pairs (vararg)
      Ns(eid).longMap.add(str3 -> long3, str4 -> long4).update
      Ns.longMap.one === Map(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).longMap.add(Seq(str4 -> long4, str5 -> long5)).update
      Ns.longMap.one === Map(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5)

      // Add empty Map of pair (no effect)
      Ns(eid).longMap.add(Seq[(String, Long)]()).update
      Ns.longMap.one === Map(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).longMap.add(str1 -> long1, str1 -> long2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\n__ident__str1 -> __ident__long1" +
          "\n__ident__str1 -> __ident__long2")

      // Seq
      expectCompileError(
        """Ns(eid).longMap.add(Seq(str1 -> long1, str1 -> long2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\n__ident__str1 -> __ident__long1" +
          "\n__ident__str1 -> __ident__long2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).longMap.add(str1 -> long1, str1x -> long2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"

      // Seq
      (Ns(eid).longMap.add(Seq(str1 -> long1, str1x -> long2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }


    "replace" in new CoreSetup {

      val eid = Ns.longMap(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5, str6 -> long6).save.eid

      // Replace value
      Ns(eid).longMap.replace(str6 -> long8).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5, str6 -> long8)

      // Replace value to existing value at another key is ok
      Ns(eid).longMap.replace(str5 -> long8).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long8, str6 -> long8)

      // Replace multiple values (vararg)
      Ns(eid).longMap.replace(str3 -> long6, str4 -> long7).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long6, str4 -> long7, str5 -> long8, str6 -> long8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longMap.replace(str3 -> long6, str4 -> long7).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long6, str4 -> long7, str5 -> long8, str6 -> long8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).longMap.replace(Seq(str2 -> long5)).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long5, str3 -> long6, str4 -> long7, str5 -> long8, str6 -> long8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).longMap.replace(Seq[(String, Long)]()).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long5, str3 -> long6, str4 -> long7, str5 -> long8, str6 -> long8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).longMap.replace(str1 -> long1, str1 -> long2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\n__ident__str1 -> __ident__long1" +
          "\n__ident__str1 -> __ident__long2")

      expectCompileError(
        """Ns(eid).longMap.replace(Seq(str1 -> long1, str1 -> long2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
          "\n__ident__str1 -> __ident__long1" +
          "\n__ident__str1 -> __ident__long2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.longMap(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5, str6 -> long6).save.eid

      // Remove pair by key
      Ns(eid).longMap.remove(str6).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5)

      // Removing pair by non-existing key has no effect
      Ns(eid).longMap.remove(str7).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4, str5 -> long5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).longMap.remove(str5, str5).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2, str3 -> long3, str4 -> long4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).longMap.remove(str3, str4).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1, str2 -> long2)

      // Remove pairs by Seq of keys
      Ns(eid).longMap.remove(Seq(str2)).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).longMap.remove(Seq[String]()).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longMap(str1 -> long1, str2 -> long2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).longMap(str1 -> long1).update
      Ns.longMap.one.toList.sorted === List(str1 -> long1)

      // Apply multiple values (vararg)
      Ns(eid).longMap(str2 -> long2, str3 -> long3).update
      Ns.longMap.one.toList.sorted === List(str2 -> long2, str3 -> long3)

      // Apply Map of values
      Ns(eid).longMap(Seq(str4 -> long4)).update
      Ns.longMap.one.toList.sorted === List(str4 -> long4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).longMap(Seq[(String, Long)]()).update
      Ns.longMap.get === List()


      Ns(eid).longMap(Seq(str1 -> long1, str2 -> long2)).update

      // Delete all (apply no values)
      Ns(eid).longMap().update
      Ns.longMap.get === List()


      // Can't apply pairs with duplicate keys

      (Ns(eid).longMap(str1 -> long1, str1 -> long2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"

      (Ns(eid).longMap(Seq(str1 -> long1, str1 -> long2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/longMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }
  }
}
