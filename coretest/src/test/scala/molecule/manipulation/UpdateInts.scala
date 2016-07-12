package molecule
package manipulation

import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateInts extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(2).save.eid

      // Apply value (retracts all current values!)
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
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/int`:\n" +
          "2\n" +
          "3")
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.int(int2).save.eid

      // Apply value (retracts all current values!)
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
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/int`:\n" +
          "__ident__int2\n" +
          "__ident__int3")
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

      // Add multiple values
      Ns(eid).ints.add(3, 4).update
      Ns.ints.one === Set(1, 2, 3, 4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).ints.add(Seq(4, 5)).update
      Ns.ints.one === Set(1, 2, 3, 4, 5)

      // Add empty Seq of values (no effect)
      Ns(eid).ints.add(Seq[Int]()).update
      Ns.ints.one === Set(1, 2, 3, 4, 5)


      // Can't add duplicate values

      expectCompileError(
        """Ns(eid).ints.add(5, 5, 6, 6, 7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:\n" +
          "5\n" +
          "6")

      expectCompileError(
        """Ns(eid).ints.add(Seq(5, 5, 6, 6, 7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:\n" +
          "5\n" +
          "6")
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(1, 2, 3, 4, 5).save.eid

      // Replace value
      Ns(eid).ints.replace(1 -> 6).update
      Ns.ints.one.toList.sorted === List(2, 3, 4, 5, 6)

      // Replace values
      Ns(eid).ints.replace(2 -> 7, 3 -> 8).update
      Ns.ints.one.toList.sorted === List(4, 5, 6, 7, 8)

      // Missing old value has no effect. The new value is inserted
      Ns(eid).ints.replace(42 -> 9).update
      Ns.ints.one.toList.sorted === List(4, 5, 6, 7, 8, 9)

      // Replace old->new mapped values
      Ns(eid).ints.replace(Seq(4 -> 9)).update
      Ns.ints.one.toList.sorted === List(5, 6, 7, 8, 9)

      // Replace empty old->new mapped values (no effect)
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.one.toList.sorted === List(5, 6, 7, 8, 9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(7 -> 8, 8 -> 8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:\n" +
          "8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(7 -> 8, 8 -> 8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:\n" +
          "8")
    }


    "remove" in new CoreSetup {

      val eid = Ns.ints(4, 5, 6, 7, 8, 9).save.eid

      // Remove value
      Ns(eid).ints.remove(4).update
      Ns.ints.one.toList.sorted === List(5, 6, 7, 8, 9)

      // Remove non-existing value (no effect)
      Ns(eid).ints.remove(1).update
      Ns.ints.one.toList.sorted === List(5, 6, 7, 8, 9)

      // Remove duplicate values (no effect - distinct values are retracted)
      Ns(eid).ints.remove(5, 5).update
      Ns.ints.one.toList.sorted === List(6, 7, 8, 9)

      // Remove multiple values
      Ns(eid).ints.remove(6, 7).update
      Ns.ints.one.toList.sorted === List(8, 9)

      // Remove Seq of values
      Ns(eid).ints.remove(Seq(8)).update
      Ns.ints.one.toList.sorted === List(9)

      // Remove empty Seq of values (no effect)
      Ns(eid).ints.remove(Seq[Int]()).update
      Ns.ints.one.toList.sorted === List(9)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(2, 3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(1).update
      Ns.ints.one.toList.sorted === List(1)

      // Apply values
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
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:\n" +
          "2\n" +
          "3")
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

      // Add empty Seq of values (no effect)
      Ns(eid).ints.add(Seq[Int]()).update
      Ns.ints.one === Set(int1, int2, int3, int4, int5)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).ints.add(int5, int5, int6, int6, int7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:\n" +
          "__ident__int5\n" +
          "__ident__int6")

      // Seq
      expectCompileError(
        """Ns(eid).ints.add(Seq(int5, int5, int6, int6, int7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/ints`:\n" +
          "__ident__int5\n" +
          "__ident__int6")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (5, 6)

      // vararg
      (Ns(eid).ints.add(other5, int5, int6, other6, int7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"""[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/ints`:
            |5
            |6
        """.stripMargin.trim

      // Seq
      (Ns(eid).ints.add(Seq(other5, int5, int6, other6, int7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"""[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/ints`:
            |5
            |6
        """.stripMargin.trim

      // If a Seq has been assigned to a variable, the values have already coalesced and would be good to enter
      val intSet = Set(other5, int5, int6, other6, int7)
      intSet === Set(5, 6, 7)
    }


    "replace" in new CoreSetup {

      val eid = Ns.ints(int1, int2, int3, int4, int5).save.eid

      // Replace value
      Ns(eid).ints.replace(int1 -> int6).update
      Ns.ints.one.toList.sorted === List(int2, int3, int4, int5, int6)

      // Replace values
      Ns(eid).ints.replace(int2 -> int7, int3 -> int8).update
      Ns.ints.one.toList.sorted === List(int4, int5, int6, int7, int8)

      // Missing old value has no effect. The new value is inserted
      Ns(eid).ints.replace(int1 -> int9).update
      Ns.ints.one.toList.sorted === List(int4, int5, int6, int7, int8, int9)

      // Replace old->new mapped values
      Ns(eid).ints.replace(Seq(int4 -> int9)).update
      Ns.ints.one.toList.sorted === List(int5, int6, int7, int8, int9)

      // Replace empty old->new mapped values (no effect)
      Ns(eid).ints.replace(Seq[(Int, Int)]()).update
      Ns.ints.one.toList.sorted === List(int5, int6, int7, int8, int9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).ints.replace(int7 -> int8, int8 -> int8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:\n" +
          "__ident__int8")

      expectCompileError(
        """Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> int8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/ints`:\n" +
          "__ident__int8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8

      (Ns(eid).ints.replace(int7 -> int8, int8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"""[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/ints`:
            |8
        """.stripMargin.trim

      // Conflicting new values
      (Ns(eid).ints.replace(Seq(int7 -> int8, int8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"""[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/ints`:
            |8
        """.stripMargin.trim
    }


    "remove" in new CoreSetup {

      val eid = Ns.ints(int4, int5, int6, int7, int8, int9).save.eid

      // Remove value
      Ns(eid).ints.remove(int4).update
      Ns.ints.one.toList.sorted === List(int5, int6, int7, int8, int9)

      // Remove non-existing value (no effect)
      Ns(eid).ints.remove(int1).update
      Ns.ints.one.toList.sorted === List(int5, int6, int7, int8, int9)

      // Remove duplicate values (no effect - distinct values are retracted)
      Ns(eid).ints.remove(int5, int5).update
      Ns.ints.one.toList.sorted === List(int6, int7, int8, int9)

      // Remove multiple values
      Ns(eid).ints.remove(int6, int7).update
      Ns.ints.one.toList.sorted === List(int8, int9)

      // Remove Seq of values
      Ns(eid).ints.remove(Seq(int8)).update
      Ns.ints.one.toList.sorted === List(int9)

      // Remove empty Seq of values (no effect)
      Ns(eid).ints.remove(Seq[Int]()).update
      Ns.ints.one.toList.sorted === List(int9)
    }


    "apply" in new CoreSetup {

      val eid = Ns.ints(int2, int3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).ints(int1).update
      Ns.ints.one.toList.sorted === List(int1)

      // Apply values
      Ns(eid).ints(int2, int3).update
      Ns.ints.one.toList.sorted === List(int2, int3)

      // Apply Seq of values
      Ns(eid).ints(Set(int4)).update
      Ns.ints.one.toList.sorted === List(int4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).ints(Set[Int]()).update
      Ns.ints.get === List()


      Ns(eid).ints(Set(int1, int2)).update

      // Delete all (apply no values)
      Ns(eid).ints().update
      Ns.ints.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).ints(int2, int2, int3, int4, int3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/ints`:\n" +
          "__ident__int3\n" +
          "__ident__int2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (2, 3)

      (Ns(eid).ints(int2, other2, int3, int4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        s"""[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/ints`:
            |2
            |3
        """.stripMargin.trim
    }
  }
}
