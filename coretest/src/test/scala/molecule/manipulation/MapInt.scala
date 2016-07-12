package molecule
package manipulation

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class MapInt extends CoreSpec {


  "Mapped values" >> {

    "add" in new CoreSetup {

      val eid = Ns.intMap("str1" -> 1).save.eid

      // Add pair
      Ns(eid).intMap.add("str2" -> 20).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 20)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).intMap.add("str2" -> 2).update
      Ns.intMap.one === Map("str1" -> 1, "str2" -> 2)

      // Add multiple pairs
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
        """Ns(eid).intMap.add("str5" -> 5, "str5" -> 50, "str6" -> 6, "str6" -> 6, "str7" -> 7).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:"
          + "\nstr5 -> 5"
          + "\nstr5 -> 50"
          + "\nstr6 -> 6"
          + "\nstr6 -> 6")

      // Seq
      expectCompileError(
        """Ns(eid).intMap.add(Seq("str5" -> 5, "str5" -> 50, "str6" -> 6, "str6" -> 6, "str7" -> 7)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/intMap`:"
          + "\nstr5 -> 5"
          + "\nstr5 -> 50"
          + "\nstr6 -> 6"
          + "\nstr6 -> 6")

    }

    //    "replace" in new CoreSetup {
    //
    //
    //    // Replace value
    //    Ns(eid).intMap.replace("str1" -> 6).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 2, "str3" -> 3, "str4" -> 4, "str5" -> 5)
    //
    //    // Replace values
    //    Ns(eid).intMap.replace("str2" -> 7, "str3" -> 8).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 7, "str3" -> 8, "str4" -> 4, "str5" -> 5)

    // Replace old->new mapped values
    //      Ns(42L).intMap.replace(Map("str4" -> 9)).updateD
    //    Ns(eid).intMap.replace(Map("str4" -> 9)).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 7, "str3" -> 8, "str4" -> 9, "str5" -> 5)
    //
    //    // Replace empty old->new mapped values (no effect)
    //    Ns(eid).intMap.replace(Map[String, Int]()).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 7, "str3" -> 8, "str4" -> 9, "str5" -> 5)
    //
    //    // To replace a pair, first remove by key and then add new pair
    //
    //    }
    //
    //    "remove" in new CoreSetup {
    //
    //    // Remove pair by key
    //    Ns(eid).intMap.remove("str5").update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 7, "str3" -> 8, "str4" -> 9)
    //
    //    // Remove pairs by keys
    //    Ns(eid).intMap.remove("str3", "str4").update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6, "str2" -> 7)
    //
    //    // Remove pairs by Seq of keys
    //    Ns(eid).intMap.remove(Set("str2")).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6)
    //
    //    // Remove pairs by empty Seq of keys (no effect)
    //    Ns(eid).intMap.remove(Set[String]()).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 6)
    //
    //
    //    }
    //
    //    "apply" in new CoreSetup {
    //
    //    // Apply value (replaces all current values!)
    //    Ns(eid).intMap("str1" -> 1).update
    //    Ns.intMap.one.toList.sorted === List("str1" -> 1)
    //
    //    // Apply values
    //    Ns(eid).intMap("str2" -> 2, "str3" -> 3).update
    //    Ns.intMap.one.toList.sorted === List("str2" -> 2, "str3" -> 3)
    //
    //    // Apply Map of values
    //    Ns(eid).intMap(Map("str4" -> 4)).update
    //    Ns.intMap.one.toList.sorted === List("str4" -> 4)
    //
    //    // Apply empty Map of values (retracting all values!)
    //    Ns(eid).intMap(Map[String, Int]()).update
    //    Ns.intMap.get === List()
    //
    //
    //    Ns(eid).intMap(Map("str1" -> 1, "str2" -> 2)).update
    //
    //    // Delete all (apply no values)
    //    Ns(eid).intMap().update
    //    Ns.intMap.get === List()
    //    }
  }

  //  "Mapped values" >> {
  //
  //  "add" in new CoreMapup {
  //
  //    val eid = Ns.intMap(str1).save.eid
  //    Ns.intMap.one === Map(str1)
  //
  //
  //    // Add value
  //    Ns(eid).intMap.add(str2).update
  //    Ns.intMap.one === Map(str1, str2)
  //
  //    // Add multiple values
  //    Ns(eid).intMap.add(str3, str4).update
  //    Ns.intMap.one === Map(str1, str2, str3, str4)
  //
  //    // Add Seq of values
  //    Ns(eid).intMap.add(Map(str5)).update
  //    Ns.intMap.one === Map(str1, str2, str3, str4, str5)
  //
  //    // Add empty Seq of values (no effect)
  //    Ns(eid).intMap.add(Map[String]()).update
  //    Ns.intMap.one === Map(str1, str2, str3, str4, str5)
  //}
  //
  //  "replace" in new CoreMapup {

  //    // Replace value
  //    Ns(eid).intMap.replace(str1 -> str6).update
  //    Ns.intMap.one.toList.sorted === List(str2, str3, str4, str5, str6)
  //
  //    // Replace values
  //    Ns(eid).intMap.replace(str2 -> str7, str3 -> str8).update
  //    Ns.intMap.one.toList.sorted === List(str4, str5, str6, str7, str8)
  //
  //    // Replace old->new mapped values
  //    Ns(eid).intMap.replace(Map(str4 -> str9)).update
  //    Ns.intMap.one.toList.sorted === List(str5, str6, str7, str8, str9)
  //
  //    // Replace empty old->new mapped values (no effect)
  //    Ns(eid).intMap.replace(Map[String, String]()).update
  //    Ns.intMap.one.toList.sorted === List(str5, str6, str7, str8, str9)
  //
  //}
  //
  //  "remove" in new CoreMapup {
  //
  //    // Remove value
  //    Ns(eid).intMap.remove(str5).update
  //    Ns.intMap.one.toList.sorted === List(str6, str7, str8, str9)
  //
  //    // Remove values
  //    Ns(eid).intMap.remove(str6, str7).update
  //    Ns.intMap.one.toList.sorted === List(str8, str9)
  //
  //    // Remove Map of values
  //    Ns(eid).intMap.remove(Map(str8)).update
  //    Ns.intMap.one.toList.sorted === List(str9)
  //
  //    // Remove Map of values (no effect)
  //    Ns(eid).intMap.remove(Map[String]()).update
  //    Ns.intMap.one.toList.sorted === List(str9)
  //
  //}

  //  "apply" in new CoreMapup {
  //
  //    // Apply value (replaces all current values!)
  //    Ns(eid).intMap.apply(str1).update
  //    Ns.intMap.one.toList.sorted === List(str1)
  //
  //    // Apply values
  //    Ns(eid).intMap(str2, str3).update
  //    Ns.intMap.one.toList.sorted === List(str2, str3)
  //
  //    // Apply Map of values
  //    Ns(eid).intMap(Map(str4)).update
  //    Ns.intMap.one.toList.sorted === List(str4)
  //
  //    // Apply empty Map of values (retracting all values!)
  //    Ns(eid).intMap(Map[String]()).update
  //    Ns.intMap.get === List()
  //
  //
  //    Ns(eid).intMap(Map(str1, str2)).update
  //
  //    // Delete all (apply no values)
  //    Ns(eid).intMap().update
  //    Ns.intMap.get === List()
  //  }
  //  }
}
