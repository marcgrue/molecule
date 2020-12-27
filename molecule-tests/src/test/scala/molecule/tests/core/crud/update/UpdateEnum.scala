package molecule.tests.core.crud.update

import java.util.concurrent.ExecutionException
import datomicClient.anomaly.Incorrect
import datomicClient.anomaly.Incorrect
import molecule.core.ops.exception.VerifyModelException
import molecule.core.transform.exception.Model2TransactionException
import molecule.core.util.SystemPeer
import molecule.core.util.testing.expectCompileError
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out1._
import molecule.TestSpec

class UpdateEnum extends TestSpec {


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

      (Ns(eid).enum("enum2", "enum3").update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
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

      (Ns(eid).enum(enum2, enum3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... enum($enum2, $enum3)"
    }
  }


  "Card-many values" >> {

    "assert" in new CoreSetup {

      val eid = Ns.enums("enum1").save.eid

      // Assert value
      Ns(eid).enums.assert("enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")

      // Assert existing value (no effect)
      Ns(eid).enums.assert("enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")

      // Assert multiple values (vararg)
      Ns(eid).enums.assert("enum3", "enum4").update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4")

      // Assert Seq of values (existing values unaffected)
      Ns(eid).enums.assert(Seq("enum4", "enum5")).update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4", "enum5")

      // Assert empty Seq of values (no effect)
      Ns(eid).enums.assert(Seq[String]()).update
      Ns.enums.get.head === Set("enum1", "enum2", "enum3", "enum4", "enum5")


      // Reset
      Ns(eid).enums().update

      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).enums.assert("enum1", "enum2", "enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")
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

      // Different exceptions for each system
      if (system == SystemPeer) {
        // Trying to use a non-existing enum not possibole
        (Ns(eid).enums.replace("x" -> "enum9").update must throwA[ExecutionException])
          .message === "Got the exception java.util.concurrent.ExecutionException: " +
          "java.lang.IllegalArgumentException: :db.error/not-an-entity " +
          s"Unable to resolve entity: :Ns.enums/x in datom [$eid :Ns/enums :Ns.enums/x]"
      } else {
        // Trying to use a non-existing enum not possibole
        (Ns(eid).enums.replace("x" -> "enum9").update must throwA[Incorrect])
          .message === "Got the exception datomicClient.anomaly.Incorrect: " +
          s"Unable to resolve entity: :Ns.enums/x in datom [$eid :Ns/enums :Ns.enums/x]"
      }

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
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/enums`:" +
          "\nenum8")

      expectCompileError(
        """Ns(eid).enums.replace(Seq("enum7" -> "enum8", "enum8" -> "enum8")).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/enums`:" +
          "\nenum8")
    }


    "retract" in new CoreSetup {

      val eid = Ns.enums("enum1", "enum2", "enum3", "enum4", "enum5", "enum6").save.eid

      // Retract value
      Ns(eid).enums.retract("enum6").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum5")

      // Retracting non-existing value has no effect
      Ns(eid).enums.retract("enum7").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4", "enum5")

      // Retracting duplicate values removes the distinc value
      Ns(eid).enums.retract("enum5", "enum5").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2", "enum3", "enum4")

      // Retract multiple values (vararg)
      Ns(eid).enums.retract("enum3", "enum4").update
      Ns.enums.get.head.toList.sorted === List("enum1", "enum2")

      // Retract Seq of values
      Ns(eid).enums.retract(Seq("enum2")).update
      Ns.enums.get.head.toList.sorted === List("enum1")

      // Retracting empty Seq of values has no effect
      Ns(eid).enums.retract(Seq[String]()).update
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


      // Redundant duplicate values are discarded (at compile time)
      Ns(eid).enums("enum1", "enum2", "enum2").update
      Ns.enums.get.head === Set("enum1", "enum2")
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.enums(enum1).save.eid

      // Assert value
      Ns(eid).enums.assert(enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Assert existing value (no effect)
      Ns(eid).enums.assert(enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Assert multiple values
      Ns(eid).enums.assert(enum3, enum4).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).enums.assert(Seq(enum4, enum5)).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(enum6, enum7)
      Ns(eid).enums.assert(values).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5, enum6, enum7)

      // Assert empty Seq of values (no effect)
      Ns(eid).enums.assert(Seq[String]()).update
      Ns.enums.get.head === Set(enum1, enum2, enum3, enum4, enum5, enum6, enum7)


      // Reset
      Ns(eid).enums().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).enums.assert(enum1, enum2, enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Equal values are coalesced (at runtime)
      val other3 = enum3
      Ns(eid).enums.assert(enum2, enum3, other3).update
      Ns.enums.get.head === Set(enum3, enum2, enum1)
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
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/enums`:" +
          "\n__ident__enum8")

      expectCompileError(
        """Ns(eid).enums.replace(Seq(enum7 -> enum8, enum8 -> enum8)).update""",
        "molecule.core.ops.exception.VerifyRawModelException: Can't replace with duplicate values of attribute `:Ns/enums`:" +
          "\n__ident__enum8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = enum8

      (Ns(eid).enums.replace(enum7 -> enum8, enum8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/enums`:" +
        "\nenum8"

      // Conflicting new values
      (Ns(eid).enums.replace(Seq(enum7 -> enum8, enum8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.core.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:Ns/enums`:" +
        "\nenum8"
    }


    "retract" in new CoreSetup {

      val eid = Ns.enums(enum1, enum2, enum3, enum4, enum5, enum6).save.eid

      // Retract value
      Ns(eid).enums.retract(enum6).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum5)

      // Retracting non-existing value has no effect
      Ns(eid).enums.retract(enum7).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4, enum5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).enums.retract(enum5, enum5).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2, enum3, enum4)

      // Retract multiple values (vararg)
      Ns(eid).enums.retract(enum3, enum4).update
      Ns.enums.get.head.toList.sorted === List(enum1, enum2)

      // Retract Seq of values
      Ns(eid).enums.retract(Seq(enum2)).update
      Ns.enums.get.head.toList.sorted === List(enum1)

      // Retract Seq of values as variable
      val values = Seq(enum1)
      Ns(eid).enums.retract(values).update
      Ns.enums.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).enums(enum1).update
      Ns(eid).enums.retract(Seq[String]()).update
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


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).enums(enum1, enum2, enum2).update
      Ns.enums.get.head === Set(enum1, enum2)

      // Equal values are coalesced (at runtime)
      val other3 = enum3
      Ns(eid).enums(enum2, enum3, other3).update
      Ns.enums.get.head === Set(enum2, enum3)
    }
  }
}
