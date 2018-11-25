package molecule.coretests.crud.update

import molecule.api.out1._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.ops.exception.VerifyModelException
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

class UpdateString extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.str("b").save.eid

      // Apply value (retracts current value)
      Ns(eid).str("a").update
      Ns.str.get.head === "a"

      // Apply new value
      Ns(eid).str("b").update
      Ns.str.get.head === "b"

      // Delete value (apply no value)
      Ns(eid).str().update
      Ns.str.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).str("b", "c").update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... str(b, c)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.str(str2).save.eid

      // Apply value (retracts current value)
      Ns(eid).str(str1).update
      Ns.str.get.head === str1

      // Apply new value
      Ns(eid).str(str2).update
      Ns.str.get.head === str2

      // Delete value (apply no value)
      Ns(eid).str().update
      Ns.str.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).str(str2, str3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... str($str2, $str3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.strs("a").save.eid

      // Assert value
      Ns(eid).strs.assert("b").update
      Ns.strs.get.head === Set("a", "b")

      // Assert existing value (no effect)
      Ns(eid).strs.assert("b").update
      Ns.strs.get.head === Set("a", "b")

      // Assert multiple values (vararg)
      Ns(eid).strs.assert("c", "d").update
      Ns.strs.get.head === Set("a", "b", "c", "d")

      // Assert Seq of values (existing values unaffected)
      Ns(eid).strs.assert(Seq("d", "e")).update
      Ns.strs.get.head === Set("a", "b", "c", "d", "e")

      // Assert empty Seq of values (no effect)
      Ns(eid).strs.assert(Seq[String]()).update
      Ns.strs.get.head === Set("a", "b", "c", "d", "e")


      // Reset
      Ns(eid).strs().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).strs.assert("a", "b", "b").update
      Ns.strs.get.head === Set("a", "b")
    }


    "replace" in new CoreSetup {

      val eid = Ns.strs("a", "b", "c", "d", "e", "f").save.eid

      // Replace value
      Ns(eid).strs.replace("f" -> "h").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "c", "d", "e", "h")

      // Replace value to existing value simply retracts it
      Ns(eid).strs.replace("e" -> "h").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "c", "d", "h")

      // Replace multiple values (vararg)
      Ns(eid).strs.replace("c" -> "f", "d" -> "g").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "f", "g", "h")

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strs.replace("x" -> "i").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "f", "g", "h", "i")

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).strs.replace(Seq("b" -> "e")).update
      Ns.strs.get.head.toList.sorted === List("a", "e", "f", "g", "h", "i")

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).strs.replace(Seq[(String, String)]()).update
      Ns.strs.get.head.toList.sorted === List("a", "e", "f", "g", "h", "i")


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).strs.replace("g" -> "h", "h" -> "h").update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\nh")

      expectCompileError(
        """Ns(eid).strs.replace(Seq("g" -> "h", "h" -> "h")).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\nh")
    }


    "retract" in new CoreSetup {

      val eid = Ns.strs("a", "b", "c", "d", "e", "f").save.eid

      // Retract value
      Ns(eid).strs.retract("f").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "c", "d", "e")

      // Retracting non-existing value has no effect
      Ns(eid).strs.retract("g").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "c", "d", "e")

      // Retracting duplicate values removes the distinc value
      Ns(eid).strs.retract("e", "e").update
      Ns.strs.get.head.toList.sorted === List("a", "b", "c", "d")

      // Retract multiple values (vararg)
      Ns(eid).strs.retract("c", "d").update
      Ns.strs.get.head.toList.sorted === List("a", "b")

      // Retract Seq of values
      Ns(eid).strs.retract(Seq("b")).update
      Ns.strs.get.head.toList.sorted === List("a")

      // Retracting empty Seq of values has no effect
      Ns(eid).strs.retract(Seq[String]()).update
      Ns.strs.get.head.toList.sorted === List("a")
    }


    "apply" in new CoreSetup {

      val eid = Ns.strs("b", "c").save.eid

      // Apply value (retracts all current values!)
      Ns(eid).strs("a").update
      Ns.strs.get.head.toList.sorted === List("a")

      // Apply multiple values (vararg)
      Ns(eid).strs("b", "c").update
      Ns.strs.get.head.toList.sorted === List("b", "c")

      // Apply Seq of values
      Ns(eid).strs(Set("d")).update
      Ns.strs.get.head.toList.sorted === List("d")

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).strs(Set[String]()).update
      Ns.strs.get === List()


      Ns(eid).strs(Set("a", "b")).update

      // Delete all (apply no values)
      Ns(eid).strs().update
      Ns.strs.get === List()


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).strs("a", "b", "b").update
      Ns.strs.get.head === Set("a", "b")
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.strs(str1).save.eid

      // Assert value
      Ns(eid).strs.assert(str2).update
      Ns.strs.get.head === Set(str1, str2)

      // Assert existing value (no effect)
      Ns(eid).strs.assert(str2).update
      Ns.strs.get.head === Set(str1, str2)

      // Assert multiple values
      Ns(eid).strs.assert(str3, str4).update
      Ns.strs.get.head === Set(str1, str2, str3, str4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).strs.assert(Seq(str4, str5)).update
      Ns.strs.get.head === Set(str1, str2, str3, str4, str5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(str6, str7)
      Ns(eid).strs.assert(values).update
      Ns.strs.get.head === Set(str1, str2, str3, str4, str5, str6, str7)

      // Assert empty Seq of values (no effect)
      Ns(eid).strs.assert(Seq[String]()).update
      Ns.strs.get.head === Set(str1, str2, str3, str4, str5, str6, str7)


      // Reset
      Ns(eid).strs().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).strs.assert(str1, str2, str2).update
      Ns.strs.get.head === Set(str1, str2)

      // Equal values are coalesced (at runtime)
      val other3 = str3
      Ns(eid).strs.assert(str2, str3, other3).update
    }


    "replace" in new CoreSetup {

      val eid = Ns.strs(str1, str2, str3, str4, str5, str6).save.eid

      // Replace value
      Ns(eid).strs.replace(str6 -> str8).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str3, str4, str5, str8)

      // Replace value to existing value simply retracts it
      Ns(eid).strs.replace(str5 -> str8).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str3, str4, str8)

      // Replace multiple values (vararg)
      Ns(eid).strs.replace(str3 -> str6, str4 -> str7).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str6, str7, str8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strs.replace("x" -> str9).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str6, str7, str8, str9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).strs.replace(Seq(str2 -> str5)).update
      Ns.strs.get.head.toList.sorted === List(str1, str5, str6, str7, str8, str9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(str1 -> str4)
      Ns(eid).strs.replace(values).update
      Ns.strs.get.head.toList.sorted === List(str4, str5, str6, str7, str8, str9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).strs.replace(Seq[(String, String)]()).update
      Ns.strs.get.head.toList.sorted === List(str4, str5, str6, str7, str8, str9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).strs.replace(str7 -> str8, str8 -> str8).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\n__ident__str8")

      expectCompileError(
        """Ns(eid).strs.replace(Seq(str7 -> str8, str8 -> str8)).update""",
        "molecule.transform.exception.Dsl2ModelException: Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\n__ident__str8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = str8

      (Ns(eid).strs.replace(str7 -> str8, str8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/strs`:" +
        "\nh"

      // Conflicting new values
      (Ns(eid).strs.replace(Seq(str7 -> str8, str8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/strs`:" +
        "\nh"
    }


    "retract" in new CoreSetup {

      val eid = Ns.strs(str1, str2, str3, str4, str5, str6).save.eid

      // Retract value
      Ns(eid).strs.retract(str6).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str3, str4, str5)

      // Retracting non-existing value has no effect
      Ns(eid).strs.retract(str7).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str3, str4, str5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).strs.retract(str5, str5).update
      Ns.strs.get.head.toList.sorted === List(str1, str2, str3, str4)

      // Retract multiple values (vararg)
      Ns(eid).strs.retract(str3, str4).update
      Ns.strs.get.head.toList.sorted === List(str1, str2)

      // Retract Seq of values
      Ns(eid).strs.retract(Seq(str2)).update
      Ns.strs.get.head.toList.sorted === List(str1)

      // Retract Seq of values as variable
      val values = Seq(str1)
      Ns(eid).strs.retract(values).update
      Ns.strs.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).strs(str1).update
      Ns(eid).strs.retract(Seq[String]()).update
      Ns.strs.get.head.toList.sorted === List(str1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.strs(str2, str3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).strs(str1).update
      Ns.strs.get.head.toList.sorted === List(str1)

      // Apply multiple values (vararg)
      Ns(eid).strs(str2, str3).update
      Ns.strs.get.head.toList.sorted === List(str2, str3)

      // Apply Seq of values
      Ns(eid).strs(Set(str4)).update
      Ns.strs.get.head.toList.sorted === List(str4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).strs(Set[String]()).update
      Ns.strs.get === List()

      // Apply Seq of values as variable
      val values = Set(str1, str2)
      Ns(eid).strs(values).update
      Ns.strs.get.head.toList.sorted === List(str1, str2)

      // Delete all (apply no values)
      Ns(eid).strs().update
      Ns.strs.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).strs(str1, str2, str2).update
      Ns.strs.get.head === Set(str1, str2)

      // Equal values are coalesced (at runtime)
      val other3 = str3
      Ns(eid).strs(str2, str3, other3).update
      Ns.strs.get.head === Set(str2, str3)
    }
  }
}
