//package molecule
//package manipulation
//
//import molecule.util.dsl.coreTest._
//import molecule.util.{CoreSetup, CoreSpec}
//
//class CardManyStrings extends CoreSpec {
//
//
//  "values" in new CoreSetup {
//
//    val eid = Ns.strs("str1").save.eid
//    Ns.strs.one === Set("str1")
//
//
//    // Add value
//    Ns(eid).strs.add("str2").update
//    Ns.strs.one === Set("str1", "str2")
//
//    // Add multiple values
//    Ns(eid).strs.add("str3", "str4").update
//    Ns.strs.one === Set("str1", "str2", "str3", "str4")
//
//    // Add Seq of values
//    Ns(eid).strs.add(Set("str5")).update
//    Ns.strs.one === Set("str1", "str2", "str3", "str4", "str5")
//
//    // Add empty Seq of values (no effect)
//    Ns(eid).strs.add(Set[String]()).update
//    Ns.strs.one === Set("str1", "str2", "str3", "str4", "str5")
//
//
//    // Replace value
//    Ns(eid).strs.replace("str1" -> "str6").update
//    Ns.strs.one.toList.sorted === List("str2", "str3", "str4", "str5", "str6")
//
//    // Replace values
//    Ns(eid).strs.replace("str2" -> "str7", "str3" -> "str8").update
//    Ns.strs.one.toList.sorted === List("str4", "str5", "str6", "str7", "str8")
//
//    // Replace old->new mapped values
//    Ns(eid).strs.replace(Map("str4" -> "str9")).update
//    Ns.strs.one.toList.sorted === List("str5", "str6", "str7", "str8", "str9")
//
//    // Replace empty old->new mapped values (no effect)
//    Ns(eid).strs.replace(Map[String, String]()).update
//    Ns.strs.one.toList.sorted === List("str5", "str6", "str7", "str8", "str9")
//
//
//    // Remove value
//    Ns(eid).strs.remove("str5").update
//    Ns.strs.one.toList.sorted === List("str6", "str7", "str8", "str9")
//
//    // Remove values
//    Ns(eid).strs.remove("str6", "str7").update
//    Ns.strs.one.toList.sorted === List("str8", "str9")
//
//    // Remove Set of values
//    Ns(eid).strs.remove(Set("str8")).update
//    Ns.strs.one.toList.sorted === List("str9")
//
//    // Remove Set of values (no effect)
//    Ns(eid).strs.remove(Set[String]()).update
//    Ns.strs.one.toList.sorted === List("str9")
//
//
//    // Apply value (replaces all current values!)
//    Ns(eid).strs("str1").update
//    Ns.strs.one.toList.sorted === List("str1")
//
//    // Apply values
//    Ns(eid).strs("str2", "str3").update
//    Ns.strs.one.toList.sorted === List("str2", "str3")
//
//    // Apply Set of values
//    Ns(eid).strs(Set("str4")).update
//    Ns.strs.one.toList.sorted === List("str4")
//
//    // Apply empty Set of values (retracting all values!)
//    Ns(eid).strs(Set[String]()).update
//    Ns.strs.get === List()
//
//
//    Ns(eid).strs(Set("str1", "str2")).update
//
//    // Delete all (apply no values)
//    Ns(eid).strs().update
//    Ns.strs.get === List()
//  }
//
//
//  "variables" in new CoreSetup {
//
//    val eid = Ns.strs(str1).save.eid
//    Ns.strs.one === Set(str1)
//
//
//    // Add value
//    Ns(eid).strs.add(str2).update
//    Ns.strs.one === Set(str1, str2)
//
//    // Add multiple values
//    Ns(eid).strs.add(str3, str4).update
//    Ns.strs.one === Set(str1, str2, str3, str4)
//
//    // Add Seq of values
//    Ns(eid).strs.add(Set(str5)).update
//    Ns.strs.one === Set(str1, str2, str3, str4, str5)
//
//    // Add empty Seq of values (no effect)
//    Ns(eid).strs.add(Set[String]()).update
//    Ns.strs.one === Set(str1, str2, str3, str4, str5)
//
//
//    // Replace value
//    Ns(eid).strs.replace(str1 -> str6).update
//    Ns.strs.one.toList.sorted === List(str2, str3, str4, str5, str6)
//
//    // Replace values
//    Ns(eid).strs.replace(str2 -> str7, str3 -> str8).update
//    Ns.strs.one.toList.sorted === List(str4, str5, str6, str7, str8)
//
//    // Replace old->new mapped values
//    Ns(eid).strs.replace(Map(str4 -> str9)).update
//    Ns.strs.one.toList.sorted === List(str5, str6, str7, str8, str9)
//
//    // Replace empty old->new mapped values (no effect)
//    Ns(eid).strs.replace(Map[String, String]()).update
//    Ns.strs.one.toList.sorted === List(str5, str6, str7, str8, str9)
//
//
//    // Remove value
//    Ns(eid).strs.remove(str5).update
//    Ns.strs.one.toList.sorted === List(str6, str7, str8, str9)
//
//    // Remove values
//    Ns(eid).strs.remove(str6, str7).update
//    Ns.strs.one.toList.sorted === List(str8, str9)
//
//    // Remove Set of values
//    Ns(eid).strs.remove(Set(str8)).update
//    Ns.strs.one.toList.sorted === List(str9)
//
//    // Remove Set of values (no effect)
//    Ns(eid).strs.remove(Set[String]()).update
//    Ns.strs.one.toList.sorted === List(str9)
//
//
//    // Apply value (replaces all current values!)
//    Ns(eid).strs.apply(str1).update
//    Ns.strs.one.toList.sorted === List(str1)
//
//    // Apply values
//    Ns(eid).strs(str2, str3).update
//    Ns.strs.one.toList.sorted === List(str2, str3)
//
//    // Apply Set of values
//    Ns(eid).strs(Set(str4)).update
//    Ns.strs.one.toList.sorted === List(str4)
//
//    // Apply empty Set of values (retracting all values!)
//    Ns(eid).strs(Set[String]()).update
//    Ns.strs.get === List()
//
//
//    Ns(eid).strs(Set(str1, str2)).update
//
//    // Delete all (apply no values)
//    Ns(eid).strs().update
//    Ns.strs.get === List()
//  }
//}
