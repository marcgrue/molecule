package molecule.coretests.manipulation.update

import java.util.concurrent.ExecutionException

import molecule._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class UpdateEnum extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.enum("enum2").save.eid

      // Apply value (retracts current value)
      Ns(eid).enum("enum1").update
      Ns.enum.get.head === "enum1"

      // Apply new value
      Ns(eid).enum("enum2").update
      Ns.enum.get.head === "enum2"

      // Delete value (apply no value)
      Ns(eid).enum().update
      Ns.enum.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).enum("enum2", "enum3").update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... enum(enum2, enum3)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.enum(enum2).save.eid

      // Apply value (retracts current value)
      Ns(eid).enum(enum1).update
      Ns.enum.get.head === enum1

      // Apply new value
      Ns(eid).enum(enum2).update
      Ns.enum.get.head === enum2

      // Delete value (apply no value)
      Ns(eid).enum().update
      Ns.enum.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).enum(enum2, enum3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... enum($enum2, $enum3)"
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.enums("enum1").save.eid

      // Add value
      Ns(eid).enums.add("enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")

      // Add exisiting value (no effect)
      Ns(eid).enums.add("enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")

      // Add multiple values (vararg)
      Ns(eid).enums.add("enum3", "enum4").update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4")

      // Add Seq of values (existing values unaffected)
      Ns(eid).enums.add(Seq("enum4", "enum5")).update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4", "enum5")

      // Add empty Seq of values (no effect)
      Ns(eid).enums.add(Seq[String]()).update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4", "enum5")


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).enums.add("enum5", "enum5", "enum6", "enum6", "enum7").update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/enums`:" +
          "\nenum6" +
          "\nenum5")

      // Seq
      expectCompileError(
        """Ns(eid).enums.add(Seq("enum5", "enum5", "enum6", "enum6", "enum7")).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/enums`:" +
          "\nenum6" +
          "\nenum5")
    }


    "replace" in new CoreSetup {

      val eid = Ns.enums("enum1", "enum2", "enum3", "enum4", "enum5", "enum6").save.eid

      // Replace value
      Ns(eid).enums.replace("enum6" -> "enum8").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum5", "enum8")

      // Replace value to existing value simply retracts it
      Ns(eid).enums.replace("enum5" -> "enum8").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum8")

      // Replace multiple values (vararg)
      Ns(eid).enums.replace("enum3" -> "enum6", "enum4" -> "enum7").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum6", "enum7", "enum8")

      // Trying to use a non-existing enum not possibole
      (Ns(eid).enums.replace("x" -> "enum9").update must throwA[ExecutionException])
        .message === "Got the exception java.util.concurrent.ExecutionException: " +
        "java.lang.IllegalArgumentException: :db.error/not-an-entity " +
        """Unable to resolve entity: :ns.enums/x in datom [17592186045445 ":ns/enums" ":ns.enums/x"]""".stripMargin


      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum6", "enum7", "enum8")

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).enums.replace(Seq("enum2" -> "enum5")).update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum5", "enum6", "enum7", "enum8")

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).enums.replace(Seq[(String, String)]()).update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum5", "enum6", "enum7", "enum8")


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).enums.replace("enum7" -> "enum8", "enum8" -> "enum8").update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/enums`:" +
          "\nenum8")

      expectCompileError(
        """Ns(eid).enums.replace(Seq("enum7" -> "enum8", "enum8" -> "enum8")).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/enums`:" +
          "\nenum8")
    }


    "remove" in new CoreSetup {

      val eid = Ns.enums("enum1", "enum2", "enum3", "enum4", "enum5", "enum6").save.eid

      // Remove value
      Ns(eid).enums.remove("enum6").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum5")

      // Removing non-existing value has no effect
      Ns(eid).enums.remove("enum7").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum5")

      // Removing duplicate values removes the distinc value
      Ns(eid).enums.remove("enum5", "enum5").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4")

      // Remove multiple values (vararg)
      Ns(eid).enums.remove("enum3", "enum4").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2")

      // Remove Seq of values
      Ns(eid).enums.remove(Seq("enum2")).update
      Ns.enums.get.head.toList.sorted === List("enum1")

      // Removing empty Seq of values has no effect
      Ns(eid).enums.remove(Seq[String]()).update
      Ns.enums.get.head.toList.sorted === List("enum1")
    }


    "apply" in new CoreSetup {

      val eid = Ns.enums("enum2", "enum3").save.eid

      // Apply value (retracts all current values!)
      Ns(eid).enums("enum1").update
      Ns.enums.get.head.toList.sorted === List("enum1")

      // Apply multiple values (vararg)
      Ns(eid).enums("enum2", "enum3").update
      Ns.enums.get.head.toList.sorted === List("enum2", "enum3")

      // Apply Seq of values
      Ns(eid).enums(Set("enum4")).update
      Ns.enums.get.head.toList.sorted === List("enum4")

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).enums(Set[String]()).update
      Ns.enums.get === List()


      Ns(eid).enums(Set("enum1", "enum2")).update

      // Delete all (apply no values)
      Ns(eid).enums().update
      Ns.enums.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).enums("enum2", "enum2", "enum3", "enum4", "enum3").update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/enums`:" +
          "\nenum3" +
          "\nenum2")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.enums(enum1).save.eid

      // Add value
      Ns(eid).enums.add(enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Add exisiting value (no effect)
      Ns(eid).enums.add(enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Add multiple values
      Ns(eid).enums.add(enum3, enum4).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).enums.add(Seq(enum4, enum5)).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(enum6, enum7)
      Ns(eid).enums.add(values).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5, enum6, enum7)

      // Add empty Seq of values (no effect)
      Ns(eid).enums.add(Seq[String]()).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5, enum6, enum7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).enums.add(enum5, enum5, enum6, enum6, enum7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/enums`:" +
          "\n__ident__enum6" +
          "\n__ident__enum5")

      // Seq
      expectCompileError(
        """Ns(eid).enums.add(Seq(enum5, enum5, enum6, enum6, enum7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/enums`:" +
          "\n__ident__enum6" +
          "\n__ident__enum5")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (enum5, enum6)

      // vararg
      (Ns(eid).enums.add(other5, enum5, enum6, other6, enum7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/enums`:" +
        "\nenum6" +
        "\nenum5"

      // Seq
      (Ns(eid).enums.add(Seq(other5, enum5, enum6, other6, enum7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/enums`:" +
        "\nenum6" +
        "\nenum5"
    }


    "replace" in new CoreSetup {

      val eid = Ns.enums(enum1, enum2, enum3, enum4, enum5, enum6).save.eid

      // Replace value
      Ns(eid).enums.replace(enum6 -> enum8).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum5, enum8)

      // Replace value to existing value simply retracts it
      Ns(eid).enums.replace(enum5 -> enum8).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum8)

      // Replace multiple values (vararg)
      Ns(eid).enums.replace(enum3 -> enum6, enum4 -> enum7).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum6, enum7, enum8)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).enums.replace(Seq(enum2 -> enum5)).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum5, enum6, enum7, enum8)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(enum1 -> enum4)
      Ns(eid).enums.replace(values).update
      Ns.enums.get.head.toList.sorted === List(enum4, enum5, enum6, enum7, enum8)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).enums.replace(Seq[(String, String)]()).update
      Ns.enums.get.head.toList.sorted === List(enum4, enum5, enum6, enum7, enum8)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).enums.replace(enum7 -> enum8, enum8 -> enum8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/enums`:" +
          "\n__ident__enum8")

      expectCompileError(
        """Ns(eid).enums.replace(Seq(enum7 -> enum8, enum8 -> enum8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/enums`:" +
          "\n__ident__enum8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = enum8

      (Ns(eid).enums.replace(enum7 -> enum8, enum8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/enums`:" +
        "\nenum8"

      // Conflicting new values
      (Ns(eid).enums.replace(Seq(enum7 -> enum8, enum8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/enums`:" +
        "\nenum8"
    }


    "remove" in new CoreSetup {

      val eid = Ns.enums(enum1, enum2, enum3, enum4, enum5, enum6).save.eid

      // Remove value
      Ns(eid).enums.remove(enum6).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum5)

      // Removing non-existing value has no effect
      Ns(eid).enums.remove(enum7).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum5)

      // Removing duplicate values removes the distinc value
      Ns(eid).enums.remove(enum5, enum5).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4)

      // Remove multiple values (vararg)
      Ns(eid).enums.remove(enum3, enum4).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2)

      // Remove Seq of values
      Ns(eid).enums.remove(Seq(enum2)).update
      Ns.enums.get.head.toList.sorted === List(enum1)

      // Remove Seq of values as variable
      val values = Seq(enum1)
      Ns(eid).enums.remove(values).update
      Ns.enums.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).enums(enum1).update
      Ns(eid).enums.remove(Seq[String]()).update
      Ns.enums.get.head.toList.sorted === List(enum1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.enums(enum2, enum3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).enums(enum1).update
      Ns.enums.get.head.toList.sorted === List(enum1)

      // Apply multiple values (vararg)
      Ns(eid).enums(enum2, enum3).update
      Ns.enums.get.head.toList.sorted === List(enum2, enum3)

      // Apply Seq of values
      Ns(eid).enums(Set(enum4)).update
      Ns.enums.get.head.toList.sorted === List(enum4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).enums(Set[String]()).update
      Ns.enums.get === List()

      // Apply Seq of values as variable
      val values = Set(enum1, enum2)
      Ns(eid).enums(values).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2)

      // Delete all (apply no values)
      Ns(eid).enums().update
      Ns.enums.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).enums(enum2, enum2, enum3, enum4, enum3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/enums`:" +
          "\n__ident__enum3" +
          "\n__ident__enum2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = ("enum2", "enum3")

      (Ns(eid).enums(enum2, other2, enum3, enum4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/enums`:" +
        "\nenum3" +
        "\nenum2"
    }
  }
}
