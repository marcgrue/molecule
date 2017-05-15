package molecule.manipulation.updateMap

import java.net.URI

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateMapURI extends CoreSpec {


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.uriMap(str1 -> uri1).save.eid

      // Add pair
      Ns(eid).uriMap.add(str2 -> uri3).update
      Ns.uriMap.get.head === Map(str1 -> uri1, str2 -> uri3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).uriMap.add(str2 -> uri2).update
      Ns.uriMap.get.head === Map(str1 -> uri1, str2 -> uri2)

      // Add multiple pairs (vararg)
      Ns(eid).uriMap.add(str3 -> uri3, str4 -> uri4).update
      Ns.uriMap.get.head === Map(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).uriMap.add(Seq(str4 -> uri4, str5 -> uri5)).update
      Ns.uriMap.get.head === Map(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5)

      // Add empty Map of pair (no effect)
      Ns(eid).uriMap.add(Seq[(String, URI)]()).update
      Ns.uriMap.get.head === Map(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).uriMap.add(str1 -> uri1, str1 -> uri2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
          "\n__ident__str1 -> __ident__uri1" +
          "\n__ident__str1 -> __ident__uri2")

      // Seq
      expectCompileError(
        """Ns(eid).uriMap.add(Seq(str1 -> uri1, str1 -> uri2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
          "\n__ident__str1 -> __ident__uri1" +
          "\n__ident__str1 -> __ident__uri2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).uriMap.add(str1 -> uri1, str1x -> uri2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
        "\na -> " + uri1 +
        "\na -> " + uri2


      // Seq
      (Ns(eid).uriMap.add(Seq(str1 -> uri1, str1x -> uri2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
        "\na -> " + uri1 +
        "\na -> " + uri2
    }


    "replace" in new CoreSetup {

      val eid = Ns.uriMap(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5, str6 -> uri6).save.eid

      // Replace value
      Ns(eid).uriMap.replace(str6 -> uri8).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5, str6 -> uri8)

      // Replace value to existing value at another key is ok
      Ns(eid).uriMap.replace(str5 -> uri8).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri8, str6 -> uri8)

      // Replace multiple values (vararg)
      Ns(eid).uriMap.replace(str3 -> uri6, str4 -> uri7).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri6, str4 -> uri7, str5 -> uri8, str6 -> uri8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).uriMap.replace(str3 -> uri6, str4 -> uri7).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri6, str4 -> uri7, str5 -> uri8, str6 -> uri8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).uriMap.replace(Seq(str2 -> uri5)).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri5, str3 -> uri6, str4 -> uri7, str5 -> uri8, str6 -> uri8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).uriMap.replace(Seq[(String, URI)]()).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri5, str3 -> uri6, str4 -> uri7, str5 -> uri8, str6 -> uri8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).uriMap.replace(str1 -> uri1, str1 -> uri2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
          "\n__ident__str1 -> __ident__uri1" +
          "\n__ident__str1 -> __ident__uri2")

      expectCompileError(
        """Ns(eid).uriMap.replace(Seq(str1 -> uri1, str1 -> uri2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
          "\n__ident__str1 -> __ident__uri1" +
          "\n__ident__str1 -> __ident__uri2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.uriMap(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5, str6 -> uri6).save.eid

      // Remove pair by key
      Ns(eid).uriMap.remove(str6).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5)

      // Removing pair by non-existing key has no effect
      Ns(eid).uriMap.remove(str7).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4, str5 -> uri5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).uriMap.remove(str5, str5).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2, str3 -> uri3, str4 -> uri4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).uriMap.remove(str3, str4).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1, str2 -> uri2)

      // Remove pairs by Seq of keys
      Ns(eid).uriMap.remove(Seq(str2)).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).uriMap.remove(Seq[String]()).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.uriMap(str1 -> uri1, str2 -> uri2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).uriMap(str1 -> uri1).update
      Ns.uriMap.get.head.toList.sorted === List(str1 -> uri1)

      // Apply multiple values (vararg)
      Ns(eid).uriMap(str2 -> uri2, str3 -> uri3).update
      Ns.uriMap.get.head.toList.sorted === List(str2 -> uri2, str3 -> uri3)

      // Apply Map of values
      Ns(eid).uriMap(Seq(str4 -> uri4)).update
      Ns.uriMap.get.head.toList.sorted === List(str4 -> uri4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).uriMap(Seq[(String, URI)]()).update
      Ns.uriMap.get === List()


      Ns(eid).uriMap(Seq(str1 -> uri1, str2 -> uri2)).update

      // Delete all (apply no values)
      Ns(eid).uriMap().update
      Ns.uriMap.get === List()


      // Can't apply pairs with duplicate keys

      (Ns(eid).uriMap(str1 -> uri1, str1 -> uri2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
        "\na -> " + uri1 +
        "\na -> " + uri2

      (Ns(eid).uriMap(Seq(str1 -> uri1, str1 -> uri2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/uriMap`:" +
        "\na -> " + uri1 +
        "\na -> " + uri2
    }
  }
}
