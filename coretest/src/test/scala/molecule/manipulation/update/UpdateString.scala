package molecule.manipulation.update

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateString extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.str("b").save.eid

      // Apply value (retracts current value)
      Ns(eid).str("a").update
      Ns.str.one === "a"

      // Apply new value
      Ns(eid).str("b").update
      Ns.str.one === "b"

      // Delete value (apply no value)
      Ns(eid).str().update
      Ns.str.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).str("b", "c").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  ns ... str(b, c)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.str(str2).save.eid

      // Apply value (retracts current value)
      Ns(eid).str(str1).update
      Ns.str.one === str1

      // Apply new value
      Ns(eid).str(str2).update
      Ns.str.one === str2

      // Delete value (apply no value)
      Ns(eid).str().update
      Ns.str.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).str(str2, str3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  ns ... str($str2, $str3)"
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.strs("a").save.eid

      // Add value
      Ns(eid).strs.add("b").update
      Ns.strs.one === Set("a", "b")

      // Add exisiting value (no effect)
      Ns(eid).strs.add("b").update
      Ns.strs.one === Set("a", "b")

      // Add multiple values (vararg)
      Ns(eid).strs.add("c", "d").update
      Ns.strs.one === Set("a", "b", "c", "d")

      // Add Seq of values (existing values unaffected)
      Ns(eid).strs.add(Seq("d", "e")).update
      Ns.strs.one === Set("a", "b", "c", "d", "e")

      // Add empty Seq of values (no effect)
      Ns(eid).strs.add(Seq[String]()).update
      Ns.strs.one === Set("a", "b", "c", "d", "e")


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).strs.add("e", "e", "f", "f", "g").update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/strs`:" +
          "\ne" +
          "\nf")

      // Seq
      expectCompileError(
        """Ns(eid).strs.add(Seq("e", "e", "f", "f", "g")).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/strs`:" +
          "\ne" +
          "\nf")
    }


    "replace" in new CoreSetup {

      val eid = Ns.strs("a", "b", "c", "d", "e", "f").save.eid

      // Replace value
      Ns(eid).strs.replace("f" -> "h").update
      Ns.strs.one.toList.sorted === List("a", "b", "c", "d", "e", "h")

      // Replace value to existing value simply retracts it
      Ns(eid).strs.replace("e" -> "h").update
      Ns.strs.one.toList.sorted === List("a", "b", "c", "d", "h")

      // Replace multiple values (vararg)
      Ns(eid).strs.replace("c" -> "f", "d" -> "g").update
      Ns.strs.one.toList.sorted === List("a", "b", "f", "g", "h")

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strs.replace("x" -> "i").update
      Ns.strs.one.toList.sorted === List("a", "b", "f", "g", "h", "i")

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).strs.replace(Seq("b" -> "e")).update
      Ns.strs.one.toList.sorted === List("a", "e", "f", "g", "h", "i")

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).strs.replace(Seq[(String, String)]()).update
      Ns.strs.one.toList.sorted === List("a", "e", "f", "g", "h", "i")


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).strs.replace("g" -> "h", "h" -> "h").update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\nh")

      expectCompileError(
        """Ns(eid).strs.replace(Seq("g" -> "h", "h" -> "h")).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\nh")
    }


    "remove" in new CoreSetup {

      val eid = Ns.strs("a", "b", "c", "d", "e", "f").save.eid

      // Remove value
      Ns(eid).strs.remove("f").update
      Ns.strs.one.toList.sorted === List("a", "b", "c", "d", "e")

      // Removing non-existing value has no effect
      Ns(eid).strs.remove("g").update
      Ns.strs.one.toList.sorted === List("a", "b", "c", "d", "e")

      // Removing duplicate values removes the distinc value
      Ns(eid).strs.remove("e", "e").update
      Ns.strs.one.toList.sorted === List("a", "b", "c", "d")

      // Remove multiple values (vararg)
      Ns(eid).strs.remove("c", "d").update
      Ns.strs.one.toList.sorted === List("a", "b")

      // Remove Seq of values
      Ns(eid).strs.remove(Seq("b")).update
      Ns.strs.one.toList.sorted === List("a")

      // Removing empty Seq of values has no effect
      Ns(eid).strs.remove(Seq[String]()).update
      Ns.strs.one.toList.sorted === List("a")
    }


    "apply" in new CoreSetup {

      val eid = Ns.strs("b", "c").save.eid

      // Apply value (retracts all current values!)
      Ns(eid).strs("a").update
      Ns.strs.one.toList.sorted === List("a")

      // Apply multiple values (vararg)
      Ns(eid).strs("b", "c").update
      Ns.strs.one.toList.sorted === List("b", "c")

      // Apply Seq of values
      Ns(eid).strs(Set("d")).update
      Ns.strs.one.toList.sorted === List("d")

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).strs(Set[String]()).update
      Ns.strs.get === List()


      Ns(eid).strs(Set("a", "b")).update

      // Delete all (apply no values)
      Ns(eid).strs().update
      Ns.strs.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).strs("b", "b", "c", "d", "c").update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/strs`:" +
          "\nb" +
          "\nc")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.strs(str1).save.eid

      // Add value
      Ns(eid).strs.add(str2).update
      Ns.strs.one === Set(str1, str2)

      // Add exisiting value (no effect)
      Ns(eid).strs.add(str2).update
      Ns.strs.one === Set(str1, str2)

      // Add multiple values
      Ns(eid).strs.add(str3, str4).update
      Ns.strs.one === Set(str1, str2, str3, str4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).strs.add(Seq(str4, str5)).update
      Ns.strs.one === Set(str1, str2, str3, str4, str5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(str6, str7)
      Ns(eid).strs.add(values).update
      Ns.strs.one === Set(str1, str2, str3, str4, str5, str6, str7)

      // Add empty Seq of values (no effect)
      Ns(eid).strs.add(Seq[String]()).update
      Ns.strs.one === Set(str1, str2, str3, str4, str5, str6, str7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).strs.add(str5, str5, str6, str6, str7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/strs`:" +
          "\n__ident__str6" +
          "\n__ident__str5")

      // Seq
      expectCompileError(
        """Ns(eid).strs.add(Seq(str5, str5, str6, str6, str7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/strs`:" +
          "\n__ident__str6" +
          "\n__ident__str5")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (str5, str6)

      // vararg
      (Ns(eid).strs.add(other5, str5, str6, other6, str7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/strs`:" +
        "\ne" +
        "\nf"

      // Seq
      (Ns(eid).strs.add(Seq(other5, str5, str6, other6, str7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/strs`:" +
        "\ne" +
        "\nf"
    }


    "replace" in new CoreSetup {

      val eid = Ns.strs(str1, str2, str3, str4, str5, str6).save.eid

      // Replace value
      Ns(eid).strs.replace(str6 -> str8).update
      Ns.strs.one.toList.sorted === List(str1, str2, str3, str4, str5, str8)

      // Replace value to existing value simply retracts it
      Ns(eid).strs.replace(str5 -> str8).update
      Ns.strs.one.toList.sorted === List(str1, str2, str3, str4, str8)

      // Replace multiple values (vararg)
      Ns(eid).strs.replace(str3 -> str6, str4 -> str7).update
      Ns.strs.one.toList.sorted === List(str1, str2, str6, str7, str8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).strs.replace("x" -> str9).update
      Ns.strs.one.toList.sorted === List(str1, str2, str6, str7, str8, str9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).strs.replace(Seq(str2 -> str5)).update
      Ns.strs.one.toList.sorted === List(str1, str5, str6, str7, str8, str9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(str1 -> str4)
      Ns(eid).strs.replace(values).update
      Ns.strs.one.toList.sorted === List(str4, str5, str6, str7, str8, str9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).strs.replace(Seq[(String, String)]()).update
      Ns.strs.one.toList.sorted === List(str4, str5, str6, str7, str8, str9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).strs.replace(str7 -> str8, str8 -> str8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\n__ident__str8")

      expectCompileError(
        """Ns(eid).strs.replace(Seq(str7 -> str8, str8 -> str8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/strs`:" +
          "\n__ident__str8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = str8

      (Ns(eid).strs.replace(str7 -> str8, str8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/strs`:" +
        "\nh"

      // Conflicting new values
      (Ns(eid).strs.replace(Seq(str7 -> str8, str8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/strs`:" +
        "\nh"
    }


    "remove" in new CoreSetup {

      val eid = Ns.strs(str1, str2, str3, str4, str5, str6).save.eid

      // Remove value
      Ns(eid).strs.remove(str6).update
      Ns.strs.one.toList.sorted === List(str1, str2, str3, str4, str5)

      // Removing non-existing value has no effect
      Ns(eid).strs.remove(str7).update
      Ns.strs.one.toList.sorted === List(str1, str2, str3, str4, str5)

      // Removing duplicate values removes the distinc value
      Ns(eid).strs.remove(str5, str5).update
      Ns.strs.one.toList.sorted === List(str1, str2, str3, str4)

      // Remove multiple values (vararg)
      Ns(eid).strs.remove(str3, str4).update
      Ns.strs.one.toList.sorted === List(str1, str2)

      // Remove Seq of values
      Ns(eid).strs.remove(Seq(str2)).update
      Ns.strs.one.toList.sorted === List(str1)

      // Remove Seq of values as variable
      val values = Seq(str1)
      Ns(eid).strs.remove(values).update
      Ns.strs.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).strs(str1).update
      Ns(eid).strs.remove(Seq[String]()).update
      Ns.strs.one.toList.sorted === List(str1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.strs(str2, str3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).strs(str1).update
      Ns.strs.one.toList.sorted === List(str1)

      // Apply multiple values (vararg)
      Ns(eid).strs(str2, str3).update
      Ns.strs.one.toList.sorted === List(str2, str3)

      // Apply Seq of values
      Ns(eid).strs(Set(str4)).update
      Ns.strs.one.toList.sorted === List(str4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).strs(Set[String]()).update
      Ns.strs.get === List()

      // Apply Seq of values as variable
      val values = Set(str1, str2)
      Ns(eid).strs(values).update
      Ns.strs.one.toList.sorted === List(str1, str2)

      // Delete all (apply no values)
      Ns(eid).strs().update
      Ns.strs.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).strs(str2, str2, str3, str4, str3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/strs`:" +
          "\n__ident__str3" +
          "\n__ident__str2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = ("b", "c")

      (Ns(eid).strs(str2, other2, str3, str4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/strs`:" +
        "\nb" +
        "\nc"
    }
  }
}
