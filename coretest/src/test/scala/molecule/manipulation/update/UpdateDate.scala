package molecule.manipulation.update

import java.util.Date

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateDate extends CoreSpec {


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.date(date2).save.eid

      // Apply value (retracts current value)
      Ns(eid).date(date1).update
      Ns.date.get.head === date1

      // Apply new value
      Ns(eid).date(date2).update
      Ns.date.get.head === date2

      // Delete value (apply no value)
      Ns(eid).date().update
      Ns.date.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).date(date2, date3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.api.CheckModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... date($date2, $date3)"
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.dates(date1).save.eid

      // Add value
      Ns(eid).dates.add(date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Add exisiting value (no effect)
      Ns(eid).dates.add(date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Add multiple values
      Ns(eid).dates.add(date3, date4).update
      Ns.dates.get.head === Set(date1, date2, date3, date4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).dates.add(Seq(date4, date5)).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(date6, date7)
      Ns(eid).dates.add(values).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5, date6, date7)

      // Add empty Seq of values (no effect)
      Ns(eid).dates.add(Seq[Date]()).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5, date6, date7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).dates.add(date5, date5, date6, date6, date7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/dates`:" +
          "\n__ident__date5" +
          "\n__ident__date6")

      // Seq
      expectCompileError(
        """Ns(eid).dates.add(Seq(date5, date5, date6, date6, date7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/dates`:" +
          "\n__ident__date5" +
          "\n__ident__date6")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (date5, date6)

      // vararg
      (Ns(eid).dates.add(other5, date5, date6, other6, date7).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/dates`:" +
        "\nThu Jan 01 01:00:05 CET 1970" +
        "\nThu Jan 01 01:00:06 CET 1970"

      // Seq
      (Ns(eid).dates.add(Seq(other5, date5, date6, other6, date7)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/dates`:" +
        "\nThu Jan 01 01:00:05 CET 1970" +
        "\nThu Jan 01 01:00:06 CET 1970"
    }


    "replace" in new CoreSetup {

      val eid = Ns.dates(date1, date2, date3, date4, date5, date6).save.eid

      // Replace value
      Ns(eid).dates.replace(date6 -> date8).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date5, date8)

      // Replace value to existing value simply retracts it
      Ns(eid).dates.replace(date5 -> date8).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date8)

      // Replace multiple values (vararg)
      Ns(eid).dates.replace(date3 -> date6, date4 -> date7).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date6, date7, date8)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).dates.replace(Seq(date2 -> date5)).update
      Ns.dates.get.head.toList.sorted === List(date1, date5, date6, date7, date8)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(date1 -> date4)
      Ns(eid).dates.replace(values).update
      Ns.dates.get.head.toList.sorted === List(date4, date5, date6, date7, date8)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).dates.replace(Seq[(Date, Date)]()).update
      Ns.dates.get.head.toList.sorted === List(date4, date5, date6, date7, date8)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).dates.replace(date7 -> date8, date8 -> date8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/dates`:" +
          "\n__ident__date8")

      expectCompileError(
        """Ns(eid).dates.replace(Seq(date7 -> date8, date8 -> date8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/dates`:" +
          "\n__ident__date8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = date8

      (Ns(eid).dates.replace(date7 -> date8, date8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/dates`:" +
        "\nThu Jan 01 01:00:08 CET 1970"

      // Conflicting new values
      (Ns(eid).dates.replace(Seq(date7 -> date8, date8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/dates`:" +
        "\nThu Jan 01 01:00:08 CET 1970"
    }


    "remove" in new CoreSetup {

      val eid = Ns.dates(date1, date2, date3, date4, date5, date6).save.eid

      // Remove value
      Ns(eid).dates.remove(date6).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date5)

      // Removing non-existing value has no effect
      Ns(eid).dates.remove(date7).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date5)

      // Removing duplicate values removes the distinc value
      Ns(eid).dates.remove(date5, date5).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4)

      // Remove multiple values (vararg)
      Ns(eid).dates.remove(date3, date4).update
      Ns.dates.get.head.toList.sorted === List(date1, date2)

      // Remove Seq of values
      Ns(eid).dates.remove(Seq(date2)).update
      Ns.dates.get.head.toList.sorted === List(date1)

      // Remove Seq of values as variable
      val values = Seq(date1)
      Ns(eid).dates.remove(values).update
      Ns.dates.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).dates(date1).update
      Ns(eid).dates.remove(Seq[Date]()).update
      Ns.dates.get.head.toList.sorted === List(date1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.dates(date2, date3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).dates(date1).update
      Ns.dates.get.head.toList.sorted === List(date1)

      // Apply multiple values (vararg)
      Ns(eid).dates(date2, date3).update
      Ns.dates.get.head.toList.sorted === List(date2, date3)

      // Apply Seq of values
      Ns(eid).dates(Set(date4)).update
      Ns.dates.get.head.toList.sorted === List(date4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).dates(Set[Date]()).update
      Ns.dates.get === List()

      // Apply Seq of values as variable
      val values = Set(date1, date2)
      Ns(eid).dates(values).update
      Ns.dates.get.head.toList.sorted === List(date1, date2)

      // Delete all (apply no values)
      Ns(eid).dates().update
      Ns.dates.get === List()


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).dates(date2, date2, date3, date4, date3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/dates`:" +
          "\n__ident__date2" +
          "\n__ident__date3")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other2, other3) = (date2, date3)

      (Ns(eid).dates(date2, other2, date3, date4, other3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/dates`:" +
        "\nThu Jan 01 01:00:02 CET 1970" +
        "\nThu Jan 01 01:00:03 CET 1970"
    }
  }
}
