package molecule.manipulation.updateMap

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateMapBigDecimal extends CoreSpec {


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.bigDecMap(str1 -> bigDec1).save.eid

      // Add pair
      Ns(eid).bigDecMap.add(str2 -> bigDec3).update
      Ns.bigDecMap.one === Map(str1 -> bigDec1, str2 -> bigDec3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).bigDecMap.add(str2 -> bigDec2).update
      Ns.bigDecMap.one === Map(str1 -> bigDec1, str2 -> bigDec2)

      // Add multiple pairs (vararg)
      Ns(eid).bigDecMap.add(str3 -> bigDec3, str4 -> bigDec4).update
      Ns.bigDecMap.one === Map(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).bigDecMap.add(Seq(str4 -> bigDec4, str5 -> bigDec5)).update
      Ns.bigDecMap.one === Map(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5)

      // Add empty Map of pair (no effect)
      Ns(eid).bigDecMap.add(Seq[(String, BigDecimal)]()).update
      Ns.bigDecMap.one === Map(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).bigDecMap.add(str1 -> bigDec1, str1 -> bigDec2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")

      // Seq
      expectCompileError(
        """Ns(eid).bigDecMap.add(Seq(str1 -> bigDec1, str1 -> bigDec2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).bigDecMap.add(str1 -> bigDec1, str1x -> bigDec2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
        "\na -> " + bigDec1 +
      "\na -> " + bigDec2


      // Seq
      (Ns(eid).bigDecMap.add(Seq(str1 -> bigDec1, str1x -> bigDec2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
        "\na -> " + bigDec1 +
      "\na -> " + bigDec2
    }


    "replace" in new CoreSetup {

      val eid = Ns.bigDecMap(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5, str6 -> bigDec6).save.eid

      // Replace value
      Ns(eid).bigDecMap.replace(str6 -> bigDec8).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5, str6 -> bigDec8)

      // Replace value to existing value at another key is ok
      Ns(eid).bigDecMap.replace(str5 -> bigDec8).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec8, str6 -> bigDec8)

      // Replace multiple values (vararg)
      Ns(eid).bigDecMap.replace(str3 -> bigDec6, str4 -> bigDec7).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec6, str4 -> bigDec7, str5 -> bigDec8, str6 -> bigDec8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).bigDecMap.replace(str3 -> bigDec6, str4 -> bigDec7).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec6, str4 -> bigDec7, str5 -> bigDec8, str6 -> bigDec8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).bigDecMap.replace(Seq(str2 -> bigDec5)).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec5, str3 -> bigDec6, str4 -> bigDec7, str5 -> bigDec8, str6 -> bigDec8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).bigDecMap.replace(Seq[(String, BigDecimal)]()).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec5, str3 -> bigDec6, str4 -> bigDec7, str5 -> bigDec8, str6 -> bigDec8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).bigDecMap.replace(str1 -> bigDec1, str1 -> bigDec2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")

      expectCompileError(
        """Ns(eid).bigDecMap.replace(Seq(str1 -> bigDec1, str1 -> bigDec2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.bigDecMap(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5, str6 -> bigDec6).save.eid

      // Remove pair by key
      Ns(eid).bigDecMap.remove(str6).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5)

      // Removing pair by non-existing key has no effect
      Ns(eid).bigDecMap.remove(str7).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4, str5 -> bigDec5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).bigDecMap.remove(str5, str5).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2, str3 -> bigDec3, str4 -> bigDec4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).bigDecMap.remove(str3, str4).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1, str2 -> bigDec2)

      // Remove pairs by Seq of keys
      Ns(eid).bigDecMap.remove(Seq(str2)).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).bigDecMap.remove(Seq[String]()).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.bigDecMap(str1 -> bigDec1, str2 -> bigDec2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).bigDecMap(str1 -> bigDec1).update
      Ns.bigDecMap.one.toList.sorted === List(str1 -> bigDec1)

      // Apply multiple values (vararg)
      Ns(eid).bigDecMap(str2 -> bigDec2, str3 -> bigDec3).update
      Ns.bigDecMap.one.toList.sorted === List(str2 -> bigDec2, str3 -> bigDec3)

      // Apply Map of values
      Ns(eid).bigDecMap(Seq(str4 -> bigDec4)).update
      Ns.bigDecMap.one.toList.sorted === List(str4 -> bigDec4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).bigDecMap(Seq[(String, BigDecimal)]()).update
      Ns.bigDecMap.get === List()


      Ns(eid).bigDecMap(Seq(str1 -> bigDec1, str2 -> bigDec2)).update

      // Delete all (apply no values)
      Ns(eid).bigDecMap().update
      Ns.bigDecMap.get === List()


      // Can't apply pairs with duplicate keys

      expectCompileError(
        """Ns(eid).bigDecMap(str1 -> bigDec1, str1 -> bigDec2).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")

      expectCompileError(
        """Ns(eid).bigDecMap(Seq(str1 -> bigDec1, str1 -> bigDec2)).update""",
        "[Dsl2Model:apply (16)] Can't apply multiple key/value pairs with the same key for attribute `:ns/bigDecMap`:" +
          "\n__ident__str1 -> __ident__bigDec1" +
          "\n__ident__str1 -> __ident__bigDec2")
    }
  }
}
