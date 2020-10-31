package molecule.coretests.crud.update

import java.util.Date
import molecule.api.out1._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.ops.exception.VerifyModelException
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

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

      (Ns(eid).date(date2, date3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... date($date2, $date3)"
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.dates(date1).save.eid

      // Assert value
      Ns(eid).dates.assert(date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Assert existing value (no effect)
      Ns(eid).dates.assert(date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Assert multiple values
      Ns(eid).dates.assert(date3, date4).update
      Ns.dates.get.head === Set(date1, date2, date3, date4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).dates.assert(Seq(date4, date5)).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(date6, date7)
      Ns(eid).dates.assert(values).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5, date6, date7)

      // Assert empty Seq of values (no effect)
      Ns(eid).dates.assert(Seq[Date]()).update
      Ns.dates.get.head === Set(date1, date2, date3, date4, date5, date6, date7)


      // Reset
      Ns(eid).dates().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).dates.assert(date1, date2, date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Equal values are coalesced (at runtime)
      val other3 = date3
      Ns(eid).dates.assert(date2, date3, other3).update
      Ns.dates.get.head === Set(date3, date2, date1)
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
        "molecule.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/dates`:" +
          "\n__ident__date8")

      expectCompileError(
        """Ns(eid).dates.replace(Seq(date7 -> date8, date8 -> date8)).update""",
        "molecule.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/dates`:" +
          "\n__ident__date8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = date8

      (Ns(eid).dates.replace(date7 -> date8, date8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/dates`:" +
        "\n" + date8

      // Conflicting new values
      (Ns(eid).dates.replace(Seq(date7 -> date8, date8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/dates`:" +
        "\n" + date8
    }


    "retract" in new CoreSetup {

      val eid = Ns.dates(date1, date2, date3, date4, date5, date6).save.eid

      // Retract value
      Ns(eid).dates.retract(date6).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date5)

      // Retracting non-existing value has no effect
      Ns(eid).dates.retract(date7).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4, date5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).dates.retract(date5, date5).update
      Ns.dates.get.head.toList.sorted === List(date1, date2, date3, date4)

      // Retract multiple values (vararg)
      Ns(eid).dates.retract(date3, date4).update
      Ns.dates.get.head.toList.sorted === List(date1, date2)

      // Retract Seq of values
      Ns(eid).dates.retract(Seq(date2)).update
      Ns.dates.get.head.toList.sorted === List(date1)

      // Retract Seq of values as variable
      val values = Seq(date1)
      Ns(eid).dates.retract(values).update
      Ns.dates.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).dates(date1).update
      Ns(eid).dates.retract(Seq[Date]()).update
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


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).dates(date1, date2, date2).update
      Ns.dates.get.head === Set(date1, date2)

      // Equal values are coalesced (at runtime)
      val other3 = date3
      Ns(eid).dates(date2, date3, other3).update
      Ns.dates.get.head === Set(date2, date3)
    }
  }
}
