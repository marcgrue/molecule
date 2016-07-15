package molecule.manipulation.updateMap

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateMapString extends CoreSpec {


  "Mapped values" >> {

    "add" in new CoreSetup {

      val eid = Ns.strMap("str1" -> "a").save.eid

      // Add pair
      Ns(eid).strMap.add("str2" -> "c").update
      Ns.strMap.one === Map("str1" -> "a", "str2" -> "c")

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).strMap.add("str2" -> "b").update
      Ns.strMap.one === Map("str1" -> "a", "str2" -> "b")

      // Add multiple pairs (vararg)
      Ns(eid).strMap.add("str3" -> "c", "str4" -> "d").update
      Ns.strMap.one === Map("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d")

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).strMap.add(Seq("str4" -> "d", "str5" -> "e")).update
      Ns.strMap.one === Map("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e")

      // Add empty Map of pair (no effect)
      Ns(eid).strMap.add(Seq[(String, String)]()).update
      Ns.strMap.one === Map("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e")


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).strMap.add("str1" -> "a", "str1" -> "b").update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")

      // Seq
      expectCompileError(
        """Ns(eid).strMap.add(Seq("str1" -> "a", "str1" -> "b")).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")
    }


    "replace" in new CoreSetup {

      val eid = Ns.strMap("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e", "str6" -> "f").save.eid

      // Replace value
      Ns(eid).strMap.replace("str6" -> "h").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e", "str6" -> "h")

      // Replace value to existing value at another key is ok
      Ns(eid).strMap.replace("str5" -> "h").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "h", "str6" -> "h")

      // Replace multiple values (vararg)
      Ns(eid).strMap.replace("str3" -> "f", "str4" -> "g").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "f", "str4" -> "g", "str5" -> "h", "str6" -> "h")

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strMap.replace("str3" -> "f", "str4" -> "g").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "f", "str4" -> "g", "str5" -> "h", "str6" -> "h")

      // Replace with Seq of key/newValue pairs
      Ns(eid).strMap.replace(Seq("str2" -> "e")).update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "e", "str3" -> "f", "str4" -> "g", "str5" -> "h", "str6" -> "h")

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).strMap.replace(Seq[(String, String)]()).update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "e", "str3" -> "f", "str4" -> "g", "str5" -> "h", "str6" -> "h")


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).strMap.replace("str1" -> "a", "str1" -> "b").update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")

      expectCompileError(
        """Ns(eid).strMap.replace(Seq("str1" -> "a", "str1" -> "b")).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")
    }


    "remove" in new CoreSetup {

      val eid = Ns.strMap("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e", "str6" -> "f").save.eid

      // Remove pair by key
      Ns(eid).strMap.remove("str6").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e")

      // Removing pair by non-existing key has no effect
      Ns(eid).strMap.remove("str7").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d", "str5" -> "e")

      // Removing duplicate keys removes the distinct key
      Ns(eid).strMap.remove("str5", "str5").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b", "str3" -> "c", "str4" -> "d")

      // Remove pairs by multiple keys (vararg)
      Ns(eid).strMap.remove("str3", "str4").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a", "str2" -> "b")

      // Remove pairs by Seq of keys
      Ns(eid).strMap.remove(Seq("str2")).update
      Ns.strMap.one.toList.sorted === List("str1" -> "a")

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).strMap.remove(Seq[String]()).update
      Ns.strMap.one.toList.sorted === List("str1" -> "a")
    }


    "apply" in new CoreSetup {

      val eid = Ns.strMap("str1" -> "a", "str2" -> "b").save.eid

      // Apply value (replaces all current values!)
      Ns(eid).strMap("str1" -> "a").update
      Ns.strMap.one.toList.sorted === List("str1" -> "a")

      // Apply multiple values (vararg)
      Ns(eid).strMap("str2" -> "b", "str3" -> "c").update
      Ns.strMap.one.toList.sorted === List("str2" -> "b", "str3" -> "c")

      // Apply Map of values
      Ns(eid).strMap(Seq("str4" -> "d")).update
      Ns.strMap.one.toList.sorted === List("str4" -> "d")

      // Apply empty Map of values (retracting all values!)
      Ns(eid).strMap(Seq[(String, String)]()).update
      Ns.strMap.get === List()


      Ns(eid).strMap(Seq("str1" -> "a", "str2" -> "b")).update

      // Delete all (apply no values)
      Ns(eid).strMap().update
      Ns.strMap.get === List()


      // Can't apply pairs with duplicate keys

      expectCompileError(
        """Ns(eid).strMap("str1" -> "a", "str1" -> "b").update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")

      expectCompileError(
        """Ns(eid).strMap(Seq("str1" -> "a", "str1" -> "b")).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\nstr1 -> a" +
          "\nstr1 -> b")
    }
  }


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.strMap(str1 -> str1).save.eid

      // Add pair
      Ns(eid).strMap.add(str2 -> str3).update
      Ns.strMap.one === Map(str1 -> str1, str2 -> str3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).strMap.add(str2 -> str2).update
      Ns.strMap.one === Map(str1 -> str1, str2 -> str2)

      // Add multiple pairs (vararg)
      Ns(eid).strMap.add(str3 -> str3, str4 -> str4).update
      Ns.strMap.one === Map(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).strMap.add(Seq(str4 -> str4, str5 -> str5)).update
      Ns.strMap.one === Map(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5)

      // Add empty Map of pair (no effect)
      Ns(eid).strMap.add(Seq[(String, String)]()).update
      Ns.strMap.one === Map(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).strMap.add(str1 -> str1, str1 -> str2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")

      // Seq
      expectCompileError(
        """Ns(eid).strMap.add(Seq(str1 -> str1, str1 -> str2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).strMap.add(str1 -> str1, str1x -> str2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
        "\na -> a" +
        "\na -> b"

      // Seq
      (Ns(eid).strMap.add(Seq(str1 -> str1, str1x -> str2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
        "\na -> a" +
        "\na -> b"
    }


    "replace" in new CoreSetup {

      val eid = Ns.strMap(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5, str6 -> str6).save.eid

      // Replace value
      Ns(eid).strMap.replace(str6 -> str8).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5, str6 -> str8)

      // Replace value to existing value at another key is ok
      Ns(eid).strMap.replace(str5 -> str8).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str8, str6 -> str8)

      // Replace multiple values (vararg)
      Ns(eid).strMap.replace(str3 -> str6, str4 -> str7).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str6, str4 -> str7, str5 -> str8, str6 -> str8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strMap.replace(str3 -> str6, str4 -> str7).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str6, str4 -> str7, str5 -> str8, str6 -> str8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).strMap.replace(Seq(str2 -> str5)).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str5, str3 -> str6, str4 -> str7, str5 -> str8, str6 -> str8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).strMap.replace(Seq[(String, String)]()).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str5, str3 -> str6, str4 -> str7, str5 -> str8, str6 -> str8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).strMap.replace(str1 -> str1, str1 -> str2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")

      expectCompileError(
        """Ns(eid).strMap.replace(Seq(str1 -> str1, str1 -> str2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.strMap(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5, str6 -> str6).save.eid

      // Remove pair by key
      Ns(eid).strMap.remove(str6).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5)

      // Removing pair by non-existing key has no effect
      Ns(eid).strMap.remove(str7).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4, str5 -> str5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).strMap.remove(str5, str5).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2, str3 -> str3, str4 -> str4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).strMap.remove(str3, str4).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1, str2 -> str2)

      // Remove pairs by Seq of keys
      Ns(eid).strMap.remove(Seq(str2)).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).strMap.remove(Seq[String]()).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.strMap(str1 -> str1, str2 -> str2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).strMap(str1 -> str1).update
      Ns.strMap.one.toList.sorted === List(str1 -> str1)

      // Apply multiple values (vararg)
      Ns(eid).strMap(str2 -> str2, str3 -> str3).update
      Ns.strMap.one.toList.sorted === List(str2 -> str2, str3 -> str3)

      // Apply Map of values
      Ns(eid).strMap(Seq(str4 -> str4)).update
      Ns.strMap.one.toList.sorted === List(str4 -> str4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).strMap(Seq[(String, String)]()).update
      Ns.strMap.get === List()


      Ns(eid).strMap(Seq(str1 -> str1, str2 -> str2)).update

      // Delete all (apply no values)
      Ns(eid).strMap().update
      Ns.strMap.get === List()


      // Can't apply pairs with duplicate keys

      expectCompileError(
        """Ns(eid).strMap(str1 -> str1, str1 -> str2).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")

      expectCompileError(
        """Ns(eid).strMap(Seq(str1 -> str1, str1 -> str2)).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/strMap`:" +
          "\n__ident__str1 -> __ident__str1" +
          "\n__ident__str1 -> __ident__str2")
    }
  }
}
