package molecule.manipulation.update

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateDouble extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.double(2.0).save.eid

      // Apply value (retracts current value)
      Ns(eid).double(1.0).update
      Ns.double.one === 1.0

      // Apply new value
      Ns(eid).double(2.0).update
      Ns.double.one === 2.0

      // Delete value (apply no value)
      Ns(eid).double().update
      Ns.double.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).double(2.0, 3.0).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/double`:" +
          "\n2.0" +
          "\n3.0")
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.double(double2).save.eid

      // Apply value (retracts current value)
      Ns(eid).double(double1).update
      Ns.double.one === double1

      // Apply new value
      Ns(eid).double(double2).update
      Ns.double.one === double2

      // Delete value (apply no value)
      Ns(eid).double().update
      Ns.double.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).double(double2, double3).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/double`:" +
          "\n__ident__double2" +
          "\n__ident__double3")
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.doubles(1.0).save.eid

      // Add value
      Ns(eid).doubles.add(2.0).update
      Ns.doubles.one === Set(1.0, 2.0)

      // Add exisiting value (no effect)
      Ns(eid).doubles.add(2.0).update
      Ns.doubles.one === Set(1.0, 2.0)

      // Add multiple values (vararg)
      Ns(eid).doubles.add(3.0, 4.0).update
      Ns.doubles.one === Set(1.0, 2.0, 3.0, 4.0)

      // Add Seq of values (existing values unaffected)
      Ns(eid).doubles.add(Seq(4.0, 5.0)).update
      Ns.doubles.one === Set(1.0, 2.0, 3.0, 4.0, 5.0)

      // Add empty Seq of values (no effect)
      Ns(eid).doubles.add(Seq[Double]()).update
      Ns.doubles.one === Set(1.0, 2.0, 3.0, 4.0, 5.0)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).doubles.add(5.0, 5.0, 6.0, 6.0, 7.0).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/doubles`:" +
          "\n5.0" +
          "\n6.0")

      // Seq
      expectCompileError(
        """Ns(eid).doubles.add(Seq(5.0, 5.0, 6.0, 6.0, 7.0)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/doubles`:" +
          "\n5.0" +
          "\n6.0")
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubles(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).save.eid

      // Replace value
      Ns(eid).doubles.replace(6.0 -> 8.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0, 8.0)

      // Replace value to existing value simply retracts it
      Ns(eid).doubles.replace(5.0 -> 8.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 8.0)

      // Replace multiple values (vararg)
      Ns(eid).doubles.replace(3.0 -> 6.0, 4.0 -> 7.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 6.0, 7.0, 8.0)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubles.replace(42.0 -> 9.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 6.0, 7.0, 8.0, 9.0)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).doubles.replace(Seq(2.0 -> 5.0)).update
      Ns.doubles.one.toList.sorted === List(1.0, 5.0, 6.0, 7.0, 8.0, 9.0)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).doubles.replace(Seq[(Double, Double)]()).update
      Ns.doubles.one.toList.sorted === List(1.0, 5.0, 6.0, 7.0, 8.0, 9.0)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).doubles.replace(7.0 -> 8.0, 8.0 -> 8.0).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/doubles`:" +
          "\n8.0")

      expectCompileError(
        """Ns(eid).doubles.replace(Seq(7.0 -> 8.0, 8.0 -> 8.0)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/doubles`:" +
          "\n8.0")
    }


    "remove" in new CoreSetup {

      val eid = Ns.doubles(1.0, 2.0, 3.0, 4.0, 5.0, 6.0).save.eid

      // Remove value
      Ns(eid).doubles.remove(6.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0)

      // Removing non-existing value has no effect
      Ns(eid).doubles.remove(7.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 3.0, 4.0, 5.0)

      // Removing duplicate values removes the distinc value
      Ns(eid).doubles.remove(5.0, 5.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0, 3.0, 4.0)

      // Remove multiple values (vararg)
      Ns(eid).doubles.remove(3.0, 4.0).update
      Ns.doubles.one.toList.sorted === List(1.0, 2.0)

      // Remove Seq of values
      Ns(eid).doubles.remove(Seq(2.0)).update
      Ns.doubles.one.toList.sorted === List(1.0)

      // Removing empty Seq of values has no effect
      Ns(eid).doubles.remove(Seq[Double]()).update
      Ns.doubles.one.toList.sorted === List(1.0)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubles(2.0, 3.0).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).doubles(1.0).update
      Ns.doubles.one.toList.sorted === List(1.0)

      // Apply multiple values (vararg)
      Ns(eid).doubles(2.0, 3.0).update
      Ns.doubles.one.toList.sorted === List(2.0, 3.0)

      // Apply Seq of values
      Ns(eid).doubles(Set(4.0)).update
      Ns.doubles.one.toList.sorted === List(4.0)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).doubles(Set[Double]()).update
      Ns.doubles.get === List()


      Ns(eid).doubles(Set(1.0, 2.0)).update

      // Delete all (apply no values)
      Ns(eid).doubles().update
      Ns.doubles.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).doubles(2.0, 2.0, 3.0, 4.0, 3.0).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/doubles`:" +
          "\n2.0" +
          "\n3.0")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.doubles(double1).save.eid

      // Add value
      Ns(eid).doubles.add(double2).update
      Ns.doubles.one === Set(double1, double2)

      // Add exisiting value (no effect)
      Ns(eid).doubles.add(double2).update
      Ns.doubles.one === Set(double1, double2)

      // Add multiple values
      Ns(eid).doubles.add(double3, double4).update
      Ns.doubles.one === Set(double1, double2, double3, double4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).doubles.add(Seq(double4, double5)).update
      Ns.doubles.one === Set(double1, double2, double3, double4, double5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(double6, double7)
      Ns(eid).doubles.add(values).update
      Ns.doubles.one === Set(double1, double2, double3, double4, double5, double6, double7)

      // Add empty Seq of values (no effect)
      Ns(eid).doubles.add(Seq[Double]()).update
      Ns.doubles.one === Set(double1, double2, double3, double4, double5, double6, double7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).doubles.add(double5, double5, double6, double6, double7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/doubles`:" +
          "\n__ident__double6" +
          "\n__ident__double5"
      )

      // Seq
      expectCompileError(
        """Ns(eid).doubles.add(Seq(double5, double5, double6, double6, double7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/doubles`:" +
          "\n__ident__double6" +
          "\n__ident__double5"
      )

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (5.0, 6.0)

      // vararg
      (Ns(eid).doubles.add(other5, double5, double6, other6, double7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/doubles`:" +
        "\n5.0" +
        "\n6.0"

      // Seq
      (Ns(eid).doubles.add(Seq(other5, double5, double6, other6, double7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/doubles`:" +
        "\n5.0" +
        "\n6.0"
    }


    "replace" in new CoreSetup {

      val eid = Ns.doubles(double1, double2, double3, double4, double5, double6).save.eid

      // Replace value
      Ns(eid).doubles.replace(double6 -> double8).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double3, double4, double5, double8)

      // Replace value to existing value simply retracts it
      Ns(eid).doubles.replace(double5 -> double8).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double3, double4, double8)

      // Replace multiple values (vararg)
      Ns(eid).doubles.replace(double3 -> double6, double4 -> double7).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double6, double7, double8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).doubles.replace(42.0 -> double9).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double6, double7, double8, double9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).doubles.replace(Seq(double2 -> double5)).update
      Ns.doubles.one.toList.sorted === List(double1, double5, double6, double7, double8, double9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(double1 -> double4)
      Ns(eid).doubles.replace(values).update
      Ns.doubles.one.toList.sorted === List(double4, double5, double6, double7, double8, double9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).doubles.replace(Seq[(Double, Double)]()).update
      Ns.doubles.one.toList.sorted === List(double4, double5, double6, double7, double8, double9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).doubles.replace(double7 -> double8, double8 -> double8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/doubles`:" +
          "\n__ident__double8")

      expectCompileError(
        """Ns(eid).doubles.replace(Seq(double7 -> double8, double8 -> double8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/doubles`:" +
          "\n__ident__double8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8.0

      (Ns(eid).doubles.replace(double7 -> double8, double8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/doubles`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).doubles.replace(Seq(double7 -> double8, double8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/doubles`:" +
        "\n8.0"
    }


    "remove" in new CoreSetup {

      val eid = Ns.doubles(double1, double2, double3, double4, double5, double6).save.eid

      // Remove value
      Ns(eid).doubles.remove(double6).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double3, double4, double5)

      // Removing non-existing value has no effect
      Ns(eid).doubles.remove(double7).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double3, double4, double5)

      // Removing duplicate values removes the distinc value
      Ns(eid).doubles.remove(double5, double5).update
      Ns.doubles.one.toList.sorted === List(double1, double2, double3, double4)

      // Remove multiple values (vararg)
      Ns(eid).doubles.remove(double3, double4).update
      Ns.doubles.one.toList.sorted === List(double1, double2)

      // Remove Seq of values
      Ns(eid).doubles.remove(Seq(double2)).update
      Ns.doubles.one.toList.sorted === List(double1)

      // Remove Seq of values as variable
      val values = Seq(double1)
      Ns(eid).doubles.remove(values).update
      Ns.doubles.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).doubles(double1).update
      Ns(eid).doubles.remove(Seq[Double]()).update
      Ns.doubles.one.toList.sorted === List(double1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.doubles(double2, double3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).doubles(double1).update
      Ns.doubles.one.toList.sorted === List(double1)

      // Apply multiple values (vararg)
      Ns(eid).doubles(double2, double3).update
      Ns.doubles.one.toList.sorted === List(double2, double3)

      // Apply Seq of values
      Ns(eid).doubles(Set(double4)).update
      Ns.doubles.one.toList.sorted === List(double4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).doubles(Set[Double]()).update
      Ns.doubles.get === List()

      // Apply Seq of values as variable
      val values = Set(double1, double2)
      Ns(eid).doubles(values).update
      Ns.doubles.one.toList.sorted === List(double1, double2)

      // Delete all (apply no values)
      Ns(eid).doubles().update
      Ns.doubles.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).doubles(double2, double2, double3, double4, double3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/doubles`:" +
          "\n__ident__double3" +
          "\n__ident__double2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (2.0, 3.0)

      (Ns(eid).doubles(double2, other2, double3, double4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/doubles`:" +
        "\n2.0" +
        "\n3.0"
    }
  }
}
