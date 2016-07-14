package molecule.manipulation.update

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateInt extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(2).save.eid

      // Apply value (retracts current value)
      Ns(eid).int(1).update
      Ns.int.one === 1

      // Apply new value
      Ns(eid).int(2).update
      Ns.int.one === 2

      // Delete value (apply no value)
      Ns(eid).int().update
      Ns.int.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).int(2, 3).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/int`:" +
          "\n2" +
          "\n3")
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(int2).save.eid

      // Apply value (retracts current value)
      Ns(eid).int(int1).update
      Ns.int.one === int1

      // Apply new value
      Ns(eid).int(int2).update
      Ns.int.one === int2

      // Delete value (apply no value)
      Ns(eid).int().update
      Ns.int.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).int(int2, int3).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/int`:" +
          "\n__ident__int2" +
          "\n__ident__int3")
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.ints(1).save.eid

      // Add value
      Ns(eid).ints.add(2).update
      Ns.ints.one === Set(1, 2)

      // Add exisiting value (no effect)
      Ns(eid).ints.add(2).update
      Ns.ints.one === Set(1, 2)

      // Add multiple values (vararg)
      Ns(eid).ints.add(3, 4).update
      Ns.ints.one === Set(1, 2, 3, 4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).ints.add(Seq(4, 5)).update
      Ns.ints.one === Set(1, 2, 3, 4, 5)

      // Add empty Seq of values (no effect)
      Ns(eid).ints.add(Seq[Int]()).update
      Ns.ints.one === Set(1, 2, 3, 4, 5)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).ints.add(5, 5, 6, 6, 7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:" +
          "\n5" +
          "\n6")

      // Seq
      expectCompileError(
        """Ns(eid).ints.add(Seq(5, 5, 6, 6, 7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:" +
          "\n5" +
          "\n6")
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(1, 2, 3, 4, 5, 6).save.eid

      // Replace value
      Ns(eid).ints.replace(6 -> 8).update
      Ns.ints.one.toList.sorted === List(1, 2, 3, 4, 5, 8)

      // Replace value to existing value simply retracts it
      Ns(eid).ints.replace(5 -> 8).update
      Ns.ints.one.toList.sorted === List(1, 2, 3, 4, 8)

      // Replace multiple values (vararg)
      Ns(eid).ints.replace(3 -> 6, 4 -> 7).update
      Ns.ints.one.toList.sorted === List(1, 2, 6, 7, 8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).ints.replace(42 -> 9).update
      Ns.ints.one.toList.sorted === List(1, 2, 6, 7, 8, 9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).ints.replace(Seq(2 -> 5)).update
      Ns.ints.one.toList.sorted === List(1, 5, 6, 7, 8, 9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.one.toList.sorted === List(1, 5, 6, 7, 8, 9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(7 -> 8, 8 -> 8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:" +
          "\n8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(7 -> 8, 8 -> 8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:" +
          "\n8")
    }


    "remove" in new CoreSetup {

      val eid = Ns.ints(1, 2, 3, 4, 5, 6).save.eid

      // Remove value
      Ns(eid).ints.remove(6).update
      Ns.ints.one.toList.sorted === List(1, 2, 3, 4, 5)

      // Removing non-existing value has no effect
      Ns(eid).ints.remove(7).update
      Ns.ints.one.toList.sorted === List(1, 2, 3, 4, 5)

      // Removing duplicate values removes the distinc value
      Ns(eid).ints.remove(5, 5).update
      Ns.ints.one.toList.sorted === List(1, 2, 3, 4)

      // Remove multiple values (vararg)
      Ns(eid).ints.remove(3, 4).update
      Ns.ints.one.toList.sorted === List(1, 2)

      // Remove Seq of values
      Ns(eid).ints.remove(Seq(2)).update
      Ns.ints.one.toList.sorted === List(1)

      // Removing empty Seq of values has no effect
      Ns(eid).ints.remove(Seq[Int]()).update
      Ns.ints.one.toList.sorted === List(1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(2, 3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(1).update
      Ns.ints.one.toList.sorted === List(1)

      // Apply multiple values (vararg)
      Ns(eid).ints(2, 3).update
      Ns.ints.one.toList.sorted === List(2, 3)

      // Apply Seq of values
      Ns(eid).ints(Set(4)).update
      Ns.ints.one.toList.sorted === List(4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).ints(Set[Int]()).update
      Ns.ints.get === List()


      Ns(eid).ints(Set(1, 2)).update

      // Delete all (apply no values)
      Ns(eid).ints().update
      Ns.ints.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).ints(2, 2, 3, 4, 3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n2" +
          "\n3")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.ints(int1).save.eid

      // Add value
      Ns(eid).ints.add(int2).update
      Ns.ints.one === Set(int1, int2)

      // Add exisiting value (no effect)
      Ns(eid).ints.add(int2).update
      Ns.ints.one === Set(int1, int2)

      // Add multiple values
      Ns(eid).ints.add(int3, int4).update
      Ns.ints.one === Set(int1, int2, int3, int4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).ints.add(Seq(int4, int5)).update
      Ns.ints.one === Set(int1, int2, int3, int4, int5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(int6, int7)
      Ns(eid).ints.add(values).update
      Ns.ints.one === Set(int1, int2, int3, int4, int5, int6, int7)

      // Add empty Seq of values (no effect)
      Ns(eid).ints.add(Seq[Int]()).update
      Ns.ints.one === Set(int1, int2, int3, int4, int5, int6, int7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).ints.add(int5, int5, int6, int6, int7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:" +
          "\n__ident__int5" +
          "\n__ident__int6")

      // Seq
      expectCompileError(
        """Ns(eid).ints.add(Seq(int5, int5, int6, int6, int7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:" +
          "\n__ident__int5" +
          "\n__ident__int6")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (5, 6)

      // vararg
      (Ns(eid).ints.add(other5, int5, int6, other6, int7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/ints`:" +
        "\n5" +
        "\n6"

      // Seq
      (Ns(eid).ints.add(Seq(other5, int5, int6, other6, int7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/ints`:" +
        "\n5" +
        "\n6"
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(int1, int2, int3, int4, int5, int6).save.eid

      // Replace value
      Ns(eid).ints.replace(int6 -> int8).update
      Ns.ints.one.toList.sorted === List(int1, int2, int3, int4, int5, int8)

      // Replace value to existing value simply retracts it
      Ns(eid).ints.replace(int5 -> int8).update
      Ns.ints.one.toList.sorted === List(int1, int2, int3, int4, int8)

      // Replace multiple values (vararg)
      Ns(eid).ints.replace(int3 -> int6, int4 -> int7).update
      Ns.ints.one.toList.sorted === List(int1, int2, int6, int7, int8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).ints.replace(42 -> int9).update
      Ns.ints.one.toList.sorted === List(int1, int2, int6, int7, int8, int9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).ints.replace(Seq(int2 -> int5)).update
      Ns.ints.one.toList.sorted === List(int1, int5, int6, int7, int8, int9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(int1 -> int4)
      Ns(eid).ints.replace(values).update
      Ns.ints.one.toList.sorted === List(int4, int5, int6, int7, int8, int9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.one.toList.sorted === List(int4, int5, int6, int7, int8, int9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(int7 -> int8, int8 -> int8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:" +
          "\n__ident__int8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> int8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:" +
          "\n__ident__int8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8

      (Ns(eid).ints.replace(int7 -> int8, int8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/ints`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/ints`:" +
        "\n8"
    }


    "remove" in new CoreSetup {

      val eid = Ns.ints(int1, int2, int3, int4, int5, int6).save.eid

      // Remove value
      Ns(eid).ints.remove(int6).update
      Ns.ints.one.toList.sorted === List(int1, int2, int3, int4, int5)

      // Removing non-existing value has no effect
      Ns(eid).ints.remove(int7).update
      Ns.ints.one.toList.sorted === List(int1, int2, int3, int4, int5)

      // Removing duplicate values removes the distinc value
      Ns(eid).ints.remove(int5, int5).update
      Ns.ints.one.toList.sorted === List(int1, int2, int3, int4)

      // Remove multiple values (vararg)
      Ns(eid).ints.remove(int3, int4).update
      Ns.ints.one.toList.sorted === List(int1, int2)

      // Remove Seq of values
      Ns(eid).ints.remove(Seq(int2)).update
      Ns.ints.one.toList.sorted === List(int1)

      // Remove Seq of values as variable
      val values = Seq(int1)
      Ns(eid).ints.remove(values).update
      Ns.ints.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).ints(int1).update
      Ns(eid).ints.remove(Seq[Int]()).update
      Ns.ints.one.toList.sorted === List(int1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(int2, int3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(int1).update
      Ns.ints.one.toList.sorted === List(int1)

      // Apply multiple values (vararg)
      Ns(eid).ints(int2, int3).update
      Ns.ints.one.toList.sorted === List(int2, int3)

      // Apply Seq of values
      Ns(eid).ints(Set(int4)).update
      Ns.ints.one.toList.sorted === List(int4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).ints(Set[Int]()).update
      Ns.ints.get === List()

      // Apply Seq of values as variable
      val values = Set(int1, int2)
      Ns(eid).ints(values).update
      Ns.ints.one.toList.sorted === List(int1, int2)

      // Delete all (apply no values)
      Ns(eid).ints().update
      Ns.ints.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).ints(int2, int2, int3, int4, int3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:" +
          "\n__ident__int3" +
          "\n__ident__int2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (2, 3)

      (Ns(eid).ints(int2, other2, int3, int4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/ints`:" +
        "\n2" +
        "\n3"
    }
  }
}
