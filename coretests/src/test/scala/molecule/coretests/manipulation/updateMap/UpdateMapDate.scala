package molecule.coretests.manipulation.updateMap

import java.util.Date
import molecule.imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class UpdateMapDate extends CoreSpec {


  "Mapped variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.dateMap(str1 -> date1).save.eid

      // Add pair
      Ns(eid).dateMap.add(str2 -> date3).update
      Ns.dateMap.get.head === Map(str1 -> date1, str2 -> date3)

      // Add pair at existing key - replaces the value (not the key)
      Ns(eid).dateMap.add(str2 -> date2).update
      Ns.dateMap.get.head === Map(str1 -> date1, str2 -> date2)

      // Add multiple pairs (vararg)
      Ns(eid).dateMap.add(str3 -> date3, str4 -> date4).update
      Ns.dateMap.get.head === Map(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4)

      // Add Map of pairs. Existing identical pairs (key and value the same) unaffected
      Ns(eid).dateMap.add(Seq(str4 -> date4, str5 -> date5)).update
      Ns.dateMap.get.head === Map(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5)

      // Add empty Map of pair (no effect)
      Ns(eid).dateMap.add(Seq[(String, Date)]()).update
      Ns.dateMap.get.head === Map(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5)


      // Can't add pairs with duplicate keys

      // vararg
      expectCompileError(
        """Ns(eid).dateMap.add(str1 -> date1, str1 -> date2).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
          "\n__ident__str1 -> __ident__date1" +
          "\n__ident__str1 -> __ident__date2")

      // Seq
      expectCompileError(
        """Ns(eid).dateMap.add(Seq(str1 -> date1, str1 -> date2)).update""",
        "[Dsl2Model:apply (14)] Can't add multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
          "\n__ident__str1 -> __ident__date1" +
          "\n__ident__str1 -> __ident__date2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val str1x = str1

      // vararg
      (Ns(eid).dateMap.add(str1 -> date1, str1x -> date2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
        "\na -> Thu Jan 01 01:00:01 CET 1970" +
        "\na -> Thu Jan 01 01:00:02 CET 1970"


      // Seq
      (Ns(eid).dateMap.add(Seq(str1 -> date1, str1x -> date2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
        "\na -> Thu Jan 01 01:00:01 CET 1970" +
        "\na -> Thu Jan 01 01:00:02 CET 1970"
    }


    "replace" in new CoreSetup {

      val eid = Ns.dateMap(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5, str6 -> date6).save.eid

      // Replace value
      Ns(eid).dateMap.replace(str6 -> date8).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5, str6 -> date8)

      // Replace value to existing value at another key is ok
      Ns(eid).dateMap.replace(str5 -> date8).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date8, str6 -> date8)

      // Replace multiple values (vararg)
      Ns(eid).dateMap.replace(str3 -> date6, str4 -> date7).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date6, str4 -> date7, str5 -> date8, str6 -> date8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).dateMap.replace(str3 -> date6, str4 -> date7).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date6, str4 -> date7, str5 -> date8, str6 -> date8)

      // Replace with Seq of key/newValue pairs
      Ns(eid).dateMap.replace(Seq(str2 -> date5)).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date5, str3 -> date6, str4 -> date7, str5 -> date8, str6 -> date8)

      // Replacing with empty Seq of key/newValue mapped values has no effect
      Ns(eid).dateMap.replace(Seq[(String, Date)]()).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date5, str3 -> date6, str4 -> date7, str5 -> date8, str6 -> date8)


      // Can't replace pairs with duplicate keys

      expectCompileError(
        """Ns(eid).dateMap.replace(str1 -> date1, str1 -> date2).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
          "\n__ident__str1 -> __ident__date1" +
          "\n__ident__str1 -> __ident__date2")

      expectCompileError(
        """Ns(eid).dateMap.replace(Seq(str1 -> date1, str1 -> date2)).update""",
        "[Dsl2Model:apply (15)] Can't replace multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
          "\n__ident__str1 -> __ident__date1" +
          "\n__ident__str1 -> __ident__date2")
    }


    "remove" in new CoreSetup {

      val eid = Ns.dateMap(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5, str6 -> date6).save.eid

      // Remove pair by key
      Ns(eid).dateMap.remove(str6).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5)

      // Removing pair by non-existing key has no effect
      Ns(eid).dateMap.remove(str7).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4, str5 -> date5)

      // Removing duplicate keys removes the distinct key
      Ns(eid).dateMap.remove(str5, str5).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2, str3 -> date3, str4 -> date4)

      // Remove pairs by multiple keys (vararg)
      Ns(eid).dateMap.remove(str3, str4).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1, str2 -> date2)

      // Remove pairs by Seq of keys
      Ns(eid).dateMap.remove(Seq(str2)).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1)

      // Removing pairs by empty Seq of keys has no effect
      Ns(eid).dateMap.remove(Seq[String]()).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.dateMap(str1 -> date1, str2 -> date2).save.eid

      // Apply value (replaces all current values!)
      Ns(eid).dateMap(str1 -> date1).update
      Ns.dateMap.get.head.toList.sorted === List(str1 -> date1)

      // Apply multiple values (vararg)
      Ns(eid).dateMap(str2 -> date2, str3 -> date3).update
      Ns.dateMap.get.head.toList.sorted === List(str2 -> date2, str3 -> date3)

      // Apply Map of values
      Ns(eid).dateMap(Seq(str4 -> date4)).update
      Ns.dateMap.get.head.toList.sorted === List(str4 -> date4)

      // Apply empty Map of values (retracting all values!)
      Ns(eid).dateMap(Seq[(String, Date)]()).update
      Ns.dateMap.get === List()


      Ns(eid).dateMap(Seq(str1 -> date1, str2 -> date2)).update

      // Delete all (apply no values)
      Ns(eid).dateMap().update
      Ns.dateMap.get === List()


      // Can't apply pairs with duplicate keys

      // vararg
      (Ns(eid).dateMap(str1 -> date1, str1 -> date2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
        "\na -> Thu Jan 01 01:00:01 CET 1970" +
        "\na -> Thu Jan 01 01:00:02 CET 1970"


      // Seq
      (Ns(eid).dateMap(Seq(str1 -> date1, str1 -> date2)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply multiple key/value pairs with the same key for attribute `:ns/dateMap`:" +
        "\na -> Thu Jan 01 01:00:01 CET 1970" +
        "\na -> Thu Jan 01 01:00:02 CET 1970"
    }
  }
}
