package molecule.coretests.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.expectCompileError
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.CoreSpec
import molecule.datomic.api.out1._

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

      (Ns(eid).bigDec(bigDec2, bigDec3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bigDec($bigDec2, $bigDec3)"
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec1).save.eid

      // Assert value
      Ns(eid).bigDecs.assert(bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec2, bigDec1)

      // Assert existing value (no effect)
      Ns(eid).bigDecs.assert(bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2)

      // Assert multiple values
      Ns(eid).bigDecs.assert(bigDec3, bigDec4).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).bigDecs.assert(Seq(bigDec4, bigDec5)).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(bigDec6, bigDec7)
      Ns(eid).bigDecs.assert(values).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6, bigDec7)

      // Assert empty Seq of values (no effect)
      Ns(eid).bigDecs.assert(Seq[BigDecimal]()).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6, bigDec7)


      // Reset
      Ns(eid).bigDecs().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).bigDecs.assert(bigDec1, bigDec2, bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2)

      // Equal values are coalesced (at runtime)
      val other3 = bigDec3
      Ns(eid).bigDecs.assert(bigDec2, bigDec3, other3).update
      Ns.bigDecs.get.head === Set(bigDec3, bigDec2, bigDec1)
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
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/bigDecs`:" +
          "\n__ident__bigDec8")

      expectCompileError(
        """Ns(eid).bigDecs.replace(Seq(bigDec7 -> bigDec8, bigDec8 -> bigDec8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/bigDecs`:" +
          "\n__ident__bigDec8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = bigDec8

      (Ns(eid).bigDecs.replace(bigDec7 -> bigDec8, bigDec8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/bigDecs`:" +
        "\n8.0"

      // Conflicting new values
      (Ns(eid).bigDecs.replace(Seq(bigDec7 -> bigDec8, bigDec8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/bigDecs`:" +
        "\n8.0"
    }


    "retract" in new CoreSetup {

      val eid = Ns.bigDecs(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5, bigDec6).save.eid

      // Retract value
      Ns(eid).bigDecs.retract(bigDec6).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Retracting non-existing value has no effect
      Ns(eid).bigDecs.retract(bigDec7).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4, bigDec5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).bigDecs.retract(bigDec5, bigDec5).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2, bigDec3, bigDec4)

      // Retract multiple values (vararg)
      Ns(eid).bigDecs.retract(bigDec3, bigDec4).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1, bigDec2)

      // Retract Seq of values
      Ns(eid).bigDecs.retract(Seq(bigDec2)).update
      Ns.bigDecs.get.head.toList.sorted === List(bigDec1)

      // Retract Seq of values as variable
      val values = Seq(bigDec1)
      Ns(eid).bigDecs.retract(values).update
      Ns.bigDecs.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).bigDecs(bigDec1).update
      Ns(eid).bigDecs.retract(Seq[BigDecimal]()).update
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


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).bigDecs(bigDec1, bigDec2, bigDec2).update
      Ns.bigDecs.get.head === Set(bigDec1, bigDec2)

      // Equal values are coalesced (at runtime)
      val other3 = bigDec3
      Ns(eid).bigDecs(bigDec2, bigDec3, other3).update
      Ns.bigDecs.get.head === Set(bigDec2, bigDec3)
    }
  }
}
