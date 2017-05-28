package molecule.coretests.manipulation.update

import molecule._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class UpdateBigDecimal extends CoreSpec {


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bigDec(bigDec2).save.eid

      // Apply value (retracts current value)
      Ns(eid).bigDec(bigDec1).update
      Ns.bigDec.get.head === bigDec1

      // Apply new value
      Ns(eid).bigDec(bigDec2).update
      Ns.bigDec.get.head === bigDec2

      // Delete value (apply no value)
      Ns(eid).bigDec().update
      Ns.bigDec.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).bigDec(bigDec2, bigDec3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bigDec($bigDec2, $bigDec3)"
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec1).save.eid

      // Add value
      Ns(eid).bigDecs.add(bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec2, bigDec1)

      // Add exisiting value (no effect)
      Ns(eid).bigDecs.add(bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2)

      // Add multiple values
      Ns(eid).bigDecs.add(bigDec3, bigDec4).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).bigDecs.add(Seq(bigDec4, bigDec5)).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(bigDec6, bigDec7)
      Ns(eid).bigDecs.add(values).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6, bigDec7)

      // Add empty Seq of values (no effect)
      Ns(eid).bigDecs.add(Seq[BigDecimal]()).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6, bigDec7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).bigDecs.add(bigDec5, bigDec5, bigDec6, bigDec6, bigDec7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/bigDecs`:" +
          "\n__ident__bigDec6" +
          "\n__ident__bigDec5")

      // Seq
      expectCompileError(
        """Ns(eid).bigDecs.add(Seq(bigDec5, bigDec5, bigDec6, bigDec6, bigDec7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/bigDecs`:" +
          "\n__ident__bigDec6" +
          "\n__ident__bigDec5")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (bigDec5, bigDec6)

      // vararg
      (Ns(eid).bigDecs.add(other5, bigDec5, bigDec6, other6, bigDec7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/bigDecs`:" +
        "\n5.0" +
        "\n6.0"

      // Seq
      (Ns(eid).bigDecs.add(Seq(other5, bigDec5, bigDec6, other6, bigDec7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/bigDecs`:" +
        "\n5.0" +
        "\n6.0"
    }


    "replace" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6).save.eid

      // Replace value
      Ns(eid).bigDecs.replace(bigDec6 -> bigDec8).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec8)

      // Replace value to existing value simply retracts it
      Ns(eid).bigDecs.replace(bigDec5 -> bigDec8).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec8)

      // Replace multiple values (vararg)
      Ns(eid).bigDecs.replace(bigDec3 -> bigDec6, bigDec4 -> bigDec7).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec6, bigDec7, bigDec8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      val bigDec42 = BigDecimal(42)
      Ns(eid).bigDecs.replace(bigDec42 -> bigDec9).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec6, bigDec7, bigDec8, bigDec9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).bigDecs.replace(Seq(bigDec2 -> bigDec5)).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec5, bigDec6, bigDec7, bigDec8, bigDec9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(bigDec1 -> bigDec4)
      Ns(eid).bigDecs.replace(values).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec4, bigDec5, bigDec6, bigDec7, bigDec8, bigDec9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).bigDecs.replace(Seq[(BigDecimal, BigDecimal)]()).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec4, bigDec5, bigDec6, bigDec7, bigDec8, bigDec9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).bigDecs.replace(bigDec7 -> bigDec8, bigDec8 -> bigDec8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/bigDecs`:" +
          "\n__ident__bigDec8")

      expectCompileError(
        """Ns(eid).bigDecs.replace(Seq(bigDec7 -> bigDec8, bigDec8 -> bigDec8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/bigDecs`:" +
          "\n__ident__bigDec8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = bigDec8

      (Ns(eid).bigDecs.replace(bigDec7 -> bigDec8, bigDec8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/bigDecs`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).bigDecs.replace(Seq(bigDec7 -> bigDec8, bigDec8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/bigDecs`:" +
        "\n8.0"
    }


    "remove" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6).save.eid

      // Remove value
      Ns(eid).bigDecs.remove(bigDec6).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Removing non-existing value has no effect
      Ns(eid).bigDecs.remove(bigDec7).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Removing duplicate values removes the distinc value
      Ns(eid).bigDecs.remove(bigDec5, bigDec5).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4)

      // Remove multiple values (vararg)
      Ns(eid).bigDecs.remove(bigDec3, bigDec4).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2)

      // Remove Seq of values
      Ns(eid).bigDecs.remove(Seq(bigDec2)).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1)

      // Remove Seq of values as variable
      val values = Seq(bigDec1)
      Ns(eid).bigDecs.remove(values).update
      Ns.bigDecs.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).bigDecs(bigDec1).update
      Ns(eid).bigDecs.remove(Seq[BigDecimal]()).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec2, bigDec3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).bigDecs(bigDec1).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1)

      // Apply multiple values (vararg)
      Ns(eid).bigDecs(bigDec2, bigDec3).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec2, bigDec3)

      // Apply Seq of values
      Ns(eid).bigDecs(Set(bigDec4)).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).bigDecs(Set[BigDecimal]()).update
      Ns.bigDecs.get === List()

      // Apply Seq of values as variable
      val values = Set(bigDec1, bigDec2)
      Ns(eid).bigDecs(values).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2)

      // Delete all (apply no values)
      Ns(eid).bigDecs().update
      Ns.bigDecs.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).bigDecs(bigDec2, bigDec2, bigDec3, bigDec4, bigDec3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/bigDecs`:" +
          "\n__ident__bigDec3" +
          "\n__ident__bigDec2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (bigDec2, bigDec3)

      (Ns(eid).bigDecs(bigDec2, other2, bigDec3, bigDec4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/bigDecs`:" +
        "\n2.0" +
        "\n3.0"
    }
  }
}
