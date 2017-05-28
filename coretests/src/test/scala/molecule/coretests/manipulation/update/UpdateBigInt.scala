package molecule.coretests.manipulation.update

import molecule._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.util.expectCompileError

class UpdateBigInt extends CoreSpec {


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bigInt(bigInt2).save.eid

      // Apply value (retracts current value)
      Ns(eid).bigInt(bigInt1).update
      Ns.bigInt.get.head === bigInt1

      // Apply new value
      Ns(eid).bigInt(bigInt2).update
      Ns.bigInt.get.head === bigInt2

      // Delete value (apply no value)
      Ns(eid).bigInt().update
      Ns.bigInt.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).bigInt(bigInt2, bigInt3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bigInt($bigInt2, $bigInt3)"
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1).save.eid

      // Add value
      Ns(eid).bigInts.add(bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Add exisiting value (no effect)
      Ns(eid).bigInts.add(bigInt2).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2)

      // Add multiple values
      Ns(eid).bigInts.add(bigInt3, bigInt4).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).bigInts.add(Seq(bigInt4, bigInt5)).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(bigInt6, bigInt7)
      Ns(eid).bigInts.add(values).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6, bigInt7)

      // Add empty Seq of values (no effect)
      Ns(eid).bigInts.add(Seq[BigInt]()).update
      Ns.bigInts.get.head === Set(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6, bigInt7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).bigInts.add(bigInt5, bigInt5, bigInt6, bigInt6, bigInt7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/bigInts`:" +
          "\n__ident__bigInt5" +
          "\n__ident__bigInt6")

      // Seq
      expectCompileError(
        """Ns(eid).bigInts.add(Seq(bigInt5, bigInt5, bigInt6, bigInt6, bigInt7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/bigInts`:" +
          "\n__ident__bigInt5" +
          "\n__ident__bigInt6")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (bigInt5, bigInt6)

      // vararg
      (Ns(eid).bigInts.add(other5, bigInt5, bigInt6, other6, bigInt7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/bigInts`:" +
        "\n5" +
        "\n6"

      // Seq
      (Ns(eid).bigInts.add(Seq(other5, bigInt5, bigInt6, other6, bigInt7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/bigInts`:" +
        "\n5" +
        "\n6"
    }


    "replace" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6).save.eid

      // Replace value
      Ns(eid).bigInts.replace(bigInt6 -> bigInt8).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt8)

      // Replace value to existing value simply retracts it
      Ns(eid).bigInts.replace(bigInt5 -> bigInt8).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt8)

      // Replace multiple values (vararg)
      Ns(eid).bigInts.replace(bigInt3 -> bigInt6, bigInt4 -> bigInt7).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt6, bigInt7, bigInt8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      val bigInt42 = BigInt(42)
      Ns(eid).bigInts.replace(bigInt42 -> bigInt9).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).bigInts.replace(Seq(bigInt2 -> bigInt5)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(bigInt1 -> bigInt4)
      Ns(eid).bigInts.replace(values).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).bigInts.replace(Seq[(BigInt, BigInt)]()).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4, bigInt5, bigInt6, bigInt7, bigInt8, bigInt9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).bigInts.replace(bigInt7 -> bigInt8, bigInt8 -> bigInt8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/bigInts`:" +
          "\n__ident__bigInt8")

      expectCompileError(
        """Ns(eid).bigInts.replace(Seq(bigInt7 -> bigInt8, bigInt8 -> bigInt8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/bigInts`:" +
          "\n__ident__bigInt8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = bigInt8

      (Ns(eid).bigInts.replace(bigInt7 -> bigInt8, bigInt8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/bigInts`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).bigInts.replace(Seq(bigInt7 -> bigInt8, bigInt8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/bigInts`:" +
        "\n8"
    }


    "remove" in new CoreSetup {

      val eid = Ns.bigInts(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5, bigInt6).save.eid

      // Remove value
      Ns(eid).bigInts.remove(bigInt6).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Removing non-existing value has no effect
      Ns(eid).bigInts.remove(bigInt7).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4, bigInt5)

      // Removing duplicate values removes the distinc value
      Ns(eid).bigInts.remove(bigInt5, bigInt5).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2, bigInt3, bigInt4)

      // Remove multiple values (vararg)
      Ns(eid).bigInts.remove(bigInt3, bigInt4).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2)

      // Remove Seq of values
      Ns(eid).bigInts.remove(Seq(bigInt2)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)

      // Remove Seq of values as variable
      val values = Seq(bigInt1)
      Ns(eid).bigInts.remove(values).update
      Ns.bigInts.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).bigInts(bigInt1).update
      Ns(eid).bigInts.remove(Seq[BigInt]()).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.bigInts(bigInt2, bigInt3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).bigInts(bigInt1).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1)

      // Apply multiple values (vararg)
      Ns(eid).bigInts(bigInt2, bigInt3).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt2, bigInt3)

      // Apply Seq of values
      Ns(eid).bigInts(Set(bigInt4)).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).bigInts(Set[BigInt]()).update
      Ns.bigInts.get === List()

      // Apply Seq of values as variable
      val values = Set(bigInt1, bigInt2)
      Ns(eid).bigInts(values).update
      Ns.bigInts.get.head.toList.sorted === List(bigInt1, bigInt2)

      // Delete all (apply no values)
      Ns(eid).bigInts().update
      Ns.bigInts.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).bigInts(bigInt2, bigInt2, bigInt3, bigInt4, bigInt3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/bigInts`:" +
          "\n__ident__bigInt2" +
          "\n__ident__bigInt3")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (bigInt2, bigInt3)

      (Ns(eid).bigInts(bigInt2, other2, bigInt3, bigInt4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/bigInts`:" +
        "\n2" +
        "\n3"
    }
  }
}
