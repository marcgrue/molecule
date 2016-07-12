//package molecule
//package manipulation
//
//import molecule.util.dsl.coreTest._
//import molecule.util.{CoreSetup, CoreSpec}
//
//class CardManyEnums extends CoreSpec {
//
//
//  "values" in new CoreSetup {
//
//    val eid = Ns.enums("enum1").save.eid
//    Ns.enums.one === Set("enum1")
//
//
//    // Add value
//    Ns(eid).enums.add("enum2").update
//    Ns.enums.one === Set("enum1", "enum2")
//
//    // Add multiple values
//    Ns(eid).enums.add("enum3", "enum4").update
//    Ns.enums.one === Set("enum1", "enum2", "enum3", "enum4")
//
//    // Add Seq of values
//    Ns(eid).enums.add(Set("enum5")).update
//    Ns.enums.one === Set("enum1", "enum2", "enum3", "enum4", "enum5")
//
//    // Add empty Seq of values (no effect)
//    Ns(eid).enums.add(Set[String]()).update
//    Ns.enums.one === Set("enum1", "enum2", "enum3", "enum4", "enum5")
//
//
//    // Replace value
//    Ns(eid).enums.replace("enum1" -> "enum6").update
//    Ns.enums.one.toList.sorted === List("enum2", "enum3", "enum4", "enum5", "enum6")
//
//    // Replace values
//    Ns(eid).enums.replace("enum2" -> "enum7", "enum3" -> "enum8").update
//    Ns.enums.one.toList.sorted === List("enum4", "enum5", "enum6", "enum7", "enum8")
//
//    // Replace old->new mapped values
//    Ns(eid).enums.replace(Map("enum4" -> "enum9")).update
//    Ns.enums.one.toList.sorted === List("enum5", "enum6", "enum7", "enum8", "enum9")
//
//    // Replace empty old->new mapped values (no effect)
//    Ns(eid).enums.replace(Map[String, String]()).update
//    Ns.enums.one.toList.sorted === List("enum5", "enum6", "enum7", "enum8", "enum9")
//
//
//    // Remove value
//    Ns(eid).enums.remove("enum5").update
//    Ns.enums.one.toList.sorted === List("enum6", "enum7", "enum8", "enum9")
//
//    // Remove values
//    Ns(eid).enums.remove("enum6", "enum7").update
//    Ns.enums.one.toList.sorted === List("enum8", "enum9")
//
//    // Remove Set of values
//    Ns(eid).enums.remove(Set("enum8")).update
//    Ns.enums.one.toList.sorted === List("enum9")
//
//    // Remove Set of values (no effect)
//    Ns(eid).enums.remove(Set[String]()).update
//    Ns.enums.one.toList.sorted === List("enum9")
//
//
//    // Apply value (replaces all current values!)
//    Ns(eid).enums.apply("enum1").update
//    Ns.enums.one.toList.sorted === List("enum1")
//
//    // Apply values
//    Ns(eid).enums("enum2", "enum3").update
//    Ns.enums.one.toList.sorted === List("enum2", "enum3")
//
//    // Apply Set of values
//    Ns(eid).enums(Set("enum4")).update
//    Ns.enums.one.toList.sorted === List("enum4")
//
//    // Apply empty Set of values (retracting all values!)
//    Ns(eid).enums(Set[String]()).update
//    Ns.enums.get === List()
//
//
//    Ns(eid).enums(Set("enum1", "enum2")).update
//
//    // Delete all (apply no values)
//    Ns(eid).enums().update
//    Ns.enums.get === List()
//  }
//
//
//  "variables" in new CoreSetup {
//
//    val eid = Ns.enums(enum1).save.eid
//    Ns.enums.one === Set(enum1)
//
//
//    // Add value
//    Ns(eid).enums.add(enum2).update
//    Ns.enums.one === Set(enum1, enum2)
//
//    // Add multiple values
//    Ns(eid).enums.add(enum3, enum4).update
//    Ns.enums.one === Set(enum1, enum2, enum3, enum4)
//
//    // Add Seq of values
//    Ns(eid).enums.add(Set(enum5)).update
//    Ns.enums.one === Set(enum1, enum2, enum3, enum4, enum5)
//
//    // Add empty Seq of values (no effect)
//    Ns(eid).enums.add(Set[String]()).update
//    Ns.enums.one === Set(enum1, enum2, enum3, enum4, enum5)
//
//
//    // Replace value
//    Ns(eid).enums.replace(enum1 -> enum6).update
//    Ns.enums.one.toList.sorted === List(enum2, enum3, enum4, enum5, enum6)
//
//    // Replace values
//    Ns(eid).enums.replace(enum2 -> enum7, enum3 -> enum8).update
//    Ns.enums.one.toList.sorted === List(enum4, enum5, enum6, enum7, enum8)
//
//    // Replace old->new mapped values
//    Ns(eid).enums.replace(Map(enum4 -> enum9)).update
//    Ns.enums.one.toList.sorted === List(enum5, enum6, enum7, enum8, enum9)
//
//    // Replace empty old->new mapped values (no effect)
//    Ns(eid).enums.replace(Map[String, String]()).update
//    Ns.enums.one.toList.sorted === List(enum5, enum6, enum7, enum8, enum9)
//
//
//    // Remove value
//    Ns(eid).enums.remove(enum5).update
//    Ns.enums.one.toList.sorted === List(enum6, enum7, enum8, enum9)
//
//    // Remove values
//    Ns(eid).enums.remove(enum6, enum7).update
//    Ns.enums.one.toList.sorted === List(enum8, enum9)
//
//    // Remove Set of values
//    Ns(eid).enums.remove(Set(enum8)).update
//    Ns.enums.one.toList.sorted === List(enum9)
//
//    // Remove Set of values (no effect)
//    Ns(eid).enums.remove(Set[String]()).update
//    Ns.enums.one.toList.sorted === List(enum9)
//
//
//    // Apply value (replaces all current values!)
//    Ns(eid).enums(enum1).update
//    Ns.enums.one.toList.sorted === List(enum1)
//
//    // Apply values
//    Ns(eid).enums(enum2, enum3).update
//    Ns.enums.one.toList.sorted === List(enum2, enum3)
//
//    // Apply Set of values
//    Ns(eid).enums(Set(enum4)).update
//    Ns.enums.one.toList.sorted === List(enum4)
//
//    // Apply empty Set of values (retracting all values!)
//    Ns(eid).enums(Set[String]()).update
//    Ns.enums.get === List()
//
//
//    Ns(eid).enums(Set(enum1, enum2)).update
//
//    // Delete all (apply no values)
//    Ns(eid).enums().update
//    Ns.enums.get === List()
//  }
//}
