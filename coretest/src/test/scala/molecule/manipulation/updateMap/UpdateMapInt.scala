package molecule.manipulation.updateMap

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateMapInt extends CoreSpec {


  "Mapped values" >> {

    "add" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1).save.eid

      // Add pair
      Ns(eid).intMap.add("str2" -> 3).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).intMap.add("str2" -> 2).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 2)

      // Add multiple pairs (vararg)
      Ns(eid).intMap.add("str3" -> 3, "str4" -> 4).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).intMap.add(Seq("str4" -> 4, "str5" -> 5)).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Add empty Map of pair (no effect)
      Ns(eid).intMap.add(Seq[(String, Int)]()).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).intMap.add("str1" -> 1, "str1" -> 2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      // Seq
      expectCompileError(
        """Ns(eid).intMap.add(Seq("str1" -> 1, "str1" -> 2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")
    }


    "replace" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 6).save.eid

      // Replace value
      Ns(eid).intMap.replace("str6" -> 8).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 8)

      // Replace value to existing value at another key is ok
      Ns(eid).intMap.replace("str5" -> 8).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 8, "str6" -> 8)

      // Replace multiple values (vararg)
      Ns(eid).intMap.replace("str3" -> 6, "str4" -> 7).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).intMap.replace("str3" -> 6, "str4" -> 7).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).intMap.replace(Seq("str2" -> 5)).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 5, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).intMap.replace(Seq[(String, Int)]()).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 5, "str3" -> 6, "str4" -> 7, "str5" -> 8, "str6" -> 8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap.replace("str1" -> 1, "str1" -> 2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      expectCompileError(
        """Ns(eid).intMap.replace(Seq("str1" -> 1, "str1" -> 2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5, "str6" -> 6).save.eid

      // Remove pair by key
      Ns(eid).intMap.remove("str6").update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Removing pair by non-existing key has no effect
      Ns(eid).intMap.remove("str7").update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).intMap.remove("str5", "str5").update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2, "str3" -> 3, "str4" -> 4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).intMap.remove("str3", "str4").update
      Ns.intMap.one.toList.sorted === List("str1" -> 1, "str2" -> 2)

      // Remove pairs by Seq of keys
      Ns(eid).intMap.remove(Seq("str2")).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).intMap.remove(Seq[String]()).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1, "str2" -> 2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).intMap("str1" -> 1).update
      Ns.intMap.one.toList.sorted === List("str1" -> 1)

      // Apply multiple values (vararg)
      Ns(eid).intMap("str2" -> 2, "str3" -> 3).update
      Ns.intMap.one.toList.sorted === List("str2" -> 2, "str3" -> 3)

      // Apply Map of values
      Ns(eid).intMap(Seq("str4" -> 4)).update
      Ns.intMap.one.toList.sorted === List("str4" -> 4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).intMap(Seq[(String, Int)]()).update
      Ns.intMap.get === List()


      Ns(eid).intMap(Seq("str1" -> 1, "str2" -> 2)).update

      // Delete all (apply no values)
      Ns(eid).intMap().update
      Ns.intMap.get === List()


      // Can't apply pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap("str1" -> 1, "str1" -> 2).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")

      expectCompileError(
        """Ns(eid).intMap(Seq("str1" -> 1, "str1" -> 2)).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\nstr1 -> 1" +
          "\nstr1 -> 2")
    }
  }


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1).save.eid

      // Add pair
      Ns(eid).intMap.add(str2 -> int3).update
      Ns.intMap.one === Map(str1 -> int1, str2 -> int3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).intMap.add(str2 -> int2).update
      Ns.intMap.one === Map(str1 -> int1, str2 -> int2)

      // Add multiple pairs (vararg)
      Ns(eid).intMap.add(str3 -> int3, str4 -> int4).update
      Ns.intMap.one === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).intMap.add(Seq(str4 -> int4, str5 -> int5)).update
      Ns.intMap.one === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Add empty Map of pair (no effect)
      Ns(eid).intMap.add(Seq[(String, Int)]()).update
      Ns.intMap.one === Map(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).intMap.add(str1 -> int1, str1 -> int2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      // Seq
      expectCompileError(
        """Ns(eid).intMap.add(Seq(str1 -> int1, str1 -> int2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).intMap.add(str1 -> int1, str1x -> int2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"


      // Seq
      (Ns(eid).intMap.add(Seq(str1 -> int1, str1x -> int2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
        "\na -> 1" +
        "\na -> 2"
    }


    "replace" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int6).save.eid

      // Replace value
      Ns(eid).intMap.replace(str6 -> int8).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int8)

      // Replace value to existing value at another key is ok
      Ns(eid).intMap.replace(str5 -> int8).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int8, str6 -> int8)

      // Replace multiple values (vararg)
      Ns(eid).intMap.replace(str3 -> int6, str4 -> int7).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).intMap.replace(str3 -> int6, str4 -> int7).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).intMap.replace(Seq(str2 -> int5)).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int5, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).intMap.replace(Seq[(String, Int)]()).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int5, str3 -> int6, str4 -> int7, str5 -> int8, str6 -> int8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap.replace(str1 -> int1, str1 -> int2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      expectCompileError(
        """Ns(eid).intMap.replace(Seq(str1 -> int1, str1 -> int2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5, str6 -> int6).save.eid

      // Remove pair by key
      Ns(eid).intMap.remove(str6).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Removing pair by non-existing key has no effect
      Ns(eid).intMap.remove(str7).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4, str5 -> int5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).intMap.remove(str5, str5).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2, str3 -> int3, str4 -> int4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).intMap.remove(str3, str4).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1, str2 -> int2)

      // Remove pairs by Seq of keys
      Ns(eid).intMap.remove(Seq(str2)).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).intMap.remove(Seq[String]()).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.intMap(str1 -> int1, str2 -> int2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).intMap(str1 -> int1).update
      Ns.intMap.one.toList.sorted === List(str1 -> int1)

      // Apply multiple values (vararg)
      Ns(eid).intMap(str2 -> int2, str3 -> int3).update
      Ns.intMap.one.toList.sorted === List(str2 -> int2, str3 -> int3)

      // Apply Map of values
      Ns(eid).intMap(Seq(str4 -> int4)).update
      Ns.intMap.one.toList.sorted === List(str4 -> int4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).intMap(Seq[(String, Int)]()).update
      Ns.intMap.get === List()


      Ns(eid).intMap(Seq(str1 -> int1, str2 -> int2)).update

      // Delete all (apply no values)
      Ns(eid).intMap().update
      Ns.intMap.get === List()


      // Can't apply pairs with duplicate keys

      expectCompileError(
        """Ns(eid).intMap(str1 -> int1, str1 -> int2).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")

      expectCompileError(
        """Ns(eid).intMap(Seq(str1 -> int1, str1 -> int2)).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/intMap`:" +
          "\n__ident__str1 -> __ident__int1" +
          "\n__ident__str1 -> __ident__int2")
    }
  }
}
