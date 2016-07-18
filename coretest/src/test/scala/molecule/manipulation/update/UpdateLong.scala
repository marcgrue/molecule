package molecule.manipulation.update

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateLong extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.long(2L).save.eid

      // Apply value (retracts current value)
      Ns(eid).long(1L).update
      Ns.long.one === 1L

      // Apply new value
      Ns(eid).long(2L).update
      Ns.long.one === 2L

      // Delete value (apply no value)
      Ns(eid).long().update
      Ns.long.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).long(2L, 3L).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  ns ... long(2, 3)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.long(long2).save.eid

      // Apply value (retracts current value)
      Ns(eid).long(long1).update
      Ns.long.one === long1

      // Apply new value
      Ns(eid).long(long2).update
      Ns.long.one === long2

      // Delete value (apply no value)
      Ns(eid).long().update
      Ns.long.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).long(long2, long3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  ns ... long($long2, $long3)"
    }
  }


  "Card-many values" >> {

    "add" in new CoreSetup {

      val eid = Ns.longs(1L).save.eid

      // Add value
      Ns(eid).longs.add(2L).update
      Ns.longs.one === Set(1L, 2L)

      // Add exisiting value (no effect)
      Ns(eid).longs.add(2L).update
      Ns.longs.one === Set(1L, 2L)

      // Add multiple values (vararg)
      Ns(eid).longs.add(3L, 4L).update
      Ns.longs.one === Set(1L, 2L, 3L, 4L)

      // Add Seq of values (existing values unaffected)
      Ns(eid).longs.add(Seq(4L, 5L)).update
      Ns.longs.one === Set(1L, 2L, 3L, 4L, 5L)

      // Add empty Seq of values (no effect)
      Ns(eid).longs.add(Seq[Long]()).update
      Ns.longs.one === Set(1L, 2L, 3L, 4L, 5L)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).longs.add(5L, 5L, 6L, 6L, 7L).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/longs`:" +
          "\n5" +
          "\n6")

      // Seq
      expectCompileError(
        """Ns(eid).longs.add(Seq(5L, 5L, 6L, 6L, 7L)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/longs`:" +
          "\n5" +
          "\n6")
    }


    "replace" in new CoreSetup {

      val eid = Ns.longs(1L, 2L, 3L, 4L, 5L, 6L).save.eid

      // Replace value
      Ns(eid).longs.replace(6L -> 8L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 3L, 4L, 5L, 8L)

      // Replace value to existing value simply retracts it
      Ns(eid).longs.replace(5L -> 8L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 3L, 4L, 8L)

      // Replace multiple values (vararg)
      Ns(eid).longs.replace(3L -> 6L, 4L -> 7L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 6L, 7L, 8L)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longs.replace(42L -> 9L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 6L, 7L, 8L, 9L)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).longs.replace(Seq(2L -> 5L)).update
      Ns.longs.one.toList.sorted === List(1L, 5L, 6L, 7L, 8L, 9L)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).longs.replace(Seq[(Long, Long)]()).update
      Ns.longs.one.toList.sorted === List(1L, 5L, 6L, 7L, 8L, 9L)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).longs.replace(7L -> 8L, 8L -> 8L).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/longs`:" +
          "\n8")

      expectCompileError(
        """Ns(eid).longs.replace(Seq(7L -> 8L, 8L -> 8L)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/longs`:" +
          "\n8")
    }


    "remove" in new CoreSetup {

      val eid = Ns.longs(1L, 2L, 3L, 4L, 5L, 6L).save.eid

      // Remove value
      Ns(eid).longs.remove(6L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 3L, 4L, 5L)

      // Removing non-existing value has no effect
      Ns(eid).longs.remove(7L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 3L, 4L, 5L)

      // Removing duplicate values removes the distinc value
      Ns(eid).longs.remove(5L, 5L).update
      Ns.longs.one.toList.sorted === List(1L, 2L, 3L, 4L)

      // Remove multiple values (vararg)
      Ns(eid).longs.remove(3L, 4L).update
      Ns.longs.one.toList.sorted === List(1L, 2L)

      // Remove Seq of values
      Ns(eid).longs.remove(Seq(2L)).update
      Ns.longs.one.toList.sorted === List(1L)

      // Removing empty Seq of values has no effect
      Ns(eid).longs.remove(Seq[Long]()).update
      Ns.longs.one.toList.sorted === List(1L)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longs(2L, 3L).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).longs(1L).update
      Ns.longs.one.toList.sorted === List(1L)

      // Apply multiple values (vararg)
      Ns(eid).longs(2L, 3L).update
      Ns.longs.one.toList.sorted === List(2L, 3L)

      // Apply Seq of values
      Ns(eid).longs(Set(4L)).update
      Ns.longs.one.toList.sorted === List(4L)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).longs(Set[Long]()).update
      Ns.longs.get === List()


      Ns(eid).longs(Set(1L, 2L)).update

      // Delete all (apply no values)
      Ns(eid).longs().update
      Ns.longs.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).longs(2L, 2L, 3L, 4L, 3L).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/longs`:" +
          "\n2" +
          "\n3")
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.longs(long1).save.eid

      // Add value
      Ns(eid).longs.add(long2).update
      Ns.longs.one === Set(long1, long2)

      // Add exisiting value (no effect)
      Ns(eid).longs.add(long2).update
      Ns.longs.one === Set(long1, long2)

      // Add multiple values
      Ns(eid).longs.add(long3, long4).update
      Ns.longs.one === Set(long1, long2, long3, long4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).longs.add(Seq(long4, long5)).update
      Ns.longs.one === Set(long1, long2, long3, long4, long5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(long6, long7)
      Ns(eid).longs.add(values).update
      Ns.longs.one === Set(long1, long2, long3, long4, long5, long6, long7)

      // Add empty Seq of values (no effect)
      Ns(eid).longs.add(Seq[Long]()).update
      Ns.longs.one === Set(long1, long2, long3, long4, long5, long6, long7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).longs.add(long5, long5, long6, long6, long7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/longs`:" +
          "\n__ident__long6" +
          "\n__ident__long5"
      )

      // Seq
      expectCompileError(
        """Ns(eid).longs.add(Seq(long5, long5, long6, long6, long7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/longs`:" +
          "\n__ident__long6" +
          "\n__ident__long5"
      )

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (5L, 6L)

      // vararg
      (Ns(eid).longs.add(other5, long5, long6, other6, long7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/longs`:" +
        "\n5" +
        "\n6"

      // Seq
      (Ns(eid).longs.add(Seq(other5, long5, long6, other6, long7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/longs`:" +
        "\n5" +
        "\n6"
    }


    "replace" in new CoreSetup {

      val eid = Ns.longs(long1, long2, long3, long4, long5, long6).save.eid

      // Replace value
      Ns(eid).longs.replace(long6 -> long8).update
      Ns.longs.one.toList.sorted === List(long1, long2, long3, long4, long5, long8)

      // Replace value to existing value simply retracts it
      Ns(eid).longs.replace(long5 -> long8).update
      Ns.longs.one.toList.sorted === List(long1, long2, long3, long4, long8)

      // Replace multiple values (vararg)
      Ns(eid).longs.replace(long3 -> long6, long4 -> long7).update
      Ns.longs.one.toList.sorted === List(long1, long2, long6, long7, long8)

      // Missing old value has no effect. The new value is inserted (upsert semantics)
      Ns(eid).longs.replace(42L -> long9).update
      Ns.longs.one.toList.sorted === List(long1, long2, long6, long7, long8, long9)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).longs.replace(Seq(long2 -> long5)).update
      Ns.longs.one.toList.sorted === List(long1, long5, long6, long7, long8, long9)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(long1 -> long4)
      Ns(eid).longs.replace(values).update
      Ns.longs.one.toList.sorted === List(long4, long5, long6, long7, long8, long9)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).longs.replace(Seq[(Long, Long)]()).update
      Ns.longs.one.toList.sorted === List(long4, long5, long6, long7, long8, long9)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).longs.replace(long7 -> long8, long8 -> long8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/longs`:" +
          "\n__ident__long8")

      expectCompileError(
        """Ns(eid).longs.replace(Seq(long7 -> long8, long8 -> long8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/longs`:" +
          "\n__ident__long8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = 8L

      (Ns(eid).longs.replace(long7 -> long8, long8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/longs`:" +
        "\n8"

      // Conflicting new values
      (Ns(eid).longs.replace(Seq(long7 -> long8, long8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/longs`:" +
        "\n8"
    }


    "remove" in new CoreSetup {

      val eid = Ns.longs(long1, long2, long3, long4, long5, long6).save.eid

      // Remove value
      Ns(eid).longs.remove(long6).update
      Ns.longs.one.toList.sorted === List(long1, long2, long3, long4, long5)

      // Removing non-existing value has no effect
      Ns(eid).longs.remove(long7).update
      Ns.longs.one.toList.sorted === List(long1, long2, long3, long4, long5)

      // Removing duplicate values removes the distinc value
      Ns(eid).longs.remove(long5, long5).update
      Ns.longs.one.toList.sorted === List(long1, long2, long3, long4)

      // Remove multiple values (vararg)
      Ns(eid).longs.remove(long3, long4).update
      Ns.longs.one.toList.sorted === List(long1, long2)

      // Remove Seq of values
      Ns(eid).longs.remove(Seq(long2)).update
      Ns.longs.one.toList.sorted === List(long1)

      // Remove Seq of values as variable
      val values = Seq(long1)
      Ns(eid).longs.remove(values).update
      Ns.longs.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).longs(long1).update
      Ns(eid).longs.remove(Seq[Long]()).update
      Ns.longs.one.toList.sorted === List(long1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.longs(long2, long3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).longs(long1).update
      Ns.longs.one.toList.sorted === List(long1)

      // Apply multiple values (vararg)
      Ns(eid).longs(long2, long3).update
      Ns.longs.one.toList.sorted === List(long2, long3)

      // Apply Seq of values
      Ns(eid).longs(Set(long4)).update
      Ns.longs.one.toList.sorted === List(long4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).longs(Set[Long]()).update
      Ns.longs.get === List()

      // Apply Seq of values as variable
      val values = Set(long1, long2)
      Ns(eid).longs(values).update
      Ns.longs.one.toList.sorted === List(long1, long2)

      // Delete all (apply no values)
      Ns(eid).longs().update
      Ns.longs.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).longs(long2, long2, long3, long4, long3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/longs`:" +
          "\n__ident__long3" +
          "\n__ident__long2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (2L, 3L)

      (Ns(eid).longs(long2, other2, long3, long4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/longs`:" +
        "\n2" +
        "\n3"
    }
  }
}
