package molecule.coretests.crud.update

import java.util.UUID

import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.ops.exception.VerifyModelException
import molecule.transform.exception.Model2TransactionException
import molecule.util.expectCompileError

class UpdateUUID extends CoreSpec {


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.uuid(uuid2).save.eid

      // Apply value (retracts current value)
      Ns(eid).uuid(uuid1).update
      Ns.uuid.get.head === uuid1

      // Apply new value
      Ns(eid).uuid(uuid2).update
      Ns.uuid.get.head === uuid2

      // Delete value (apply no value)
      Ns(eid).uuid().update
      Ns.uuid.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).uuid(uuid2, uuid3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... uuid($uuid2, $uuid3)"
    }
  }


  "Card-many variables" >> {

    "assert" in new CoreSetup {

      val eid = Ns.uuids(uuid1).save.eid

      // Assert value
      Ns(eid).uuids.assert(uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Assert existing value (no effect)
      Ns(eid).uuids.assert(uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Assert multiple values
      Ns(eid).uuids.assert(uuid3, uuid4).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4)

      // Assert Seq of values (existing values unaffected)
      Ns(eid).uuids.assert(Seq(uuid4, uuid5)).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Assert Seq of values as variable (existing values unaffected)
      val values = Seq(uuid6, uuid7)
      Ns(eid).uuids.assert(values).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7)

      // Assert empty Seq of values (no effect)
      Ns(eid).uuids.assert(Seq[UUID]()).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7)


      // Reset
      Ns(eid).uuids().update

      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).uuids.assert(uuid1, uuid2, uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Equal values are coalesced (at runtime)
      val other3 = uuid3
      Ns(eid).uuids.assert(uuid2, uuid3, other3).update
      Ns.uuids.get.head === Set(uuid3, uuid2, uuid1)
    }


    "replace" in new CoreSetup {

      val eid = Ns.uuids(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6).save.eid

      // Replace value
      Ns(eid).uuids.replace(uuid6 -> uuid8).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5, uuid8)

      // Replace value to existing value simply retracts it
      Ns(eid).uuids.replace(uuid5 -> uuid8).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid8)

      // Replace multiple values (vararg)
      Ns(eid).uuids.replace(uuid3 -> uuid6, uuid4 -> uuid7).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid6, uuid7, uuid8)

      // Replace with Seq of oldValue->newValue pairs
      Ns(eid).uuids.replace(Seq(uuid2 -> uuid5)).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid5, uuid6, uuid7, uuid8)

      // Replace with Seq of oldValue->newValue pairs as variable
      val values = Seq(uuid1 -> uuid4)
      Ns(eid).uuids.replace(values).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid4, uuid5, uuid6, uuid7, uuid8)

      // Replacing with empty Seq of oldValue->newValue pairs has no effect
      Ns(eid).uuids.replace(Seq[(UUID, UUID)]()).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid4, uuid5, uuid6, uuid7, uuid8)


      // Can't replace duplicate values

      expectCompileError(
        """Ns(eid).uuids.replace(uuid7 -> uuid8, uuid8 -> uuid8).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/uuids`:" +
          "\n__ident__uuid8")

      expectCompileError(
        """Ns(eid).uuids.replace(Seq(uuid7 -> uuid8, uuid8 -> uuid8)).update""",
        "[Dsl2Model:apply (12)] Can't replace with duplicate values of attribute `:ns/uuids`:" +
          "\n__ident__uuid8")


      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other8 = uuid8

      (Ns(eid).uuids.replace(uuid7 -> uuid8, uuid8 -> other8).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uuids`:" +
        "\n" + uuid8

      // Conflicting new values
      (Ns(eid).uuids.replace(Seq(uuid7 -> uuid8, uuid8 -> other8)).update must throwA[Model2TransactionException])
        .message === "Got the exception molecule.transform.exception.Model2TransactionException: " +
        "[valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uuids`:" +
        "\n" + uuid8
    }


    "retract" in new CoreSetup {

      val eid = Ns.uuids(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6).save.eid

      // Retract value
      Ns(eid).uuids.retract(uuid6).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Retracting non-existing value has no effect
      Ns(eid).uuids.retract(uuid7).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Retracting duplicate values removes the distinc value
      Ns(eid).uuids.retract(uuid5, uuid5).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4)

      // Retract multiple values (vararg)
      Ns(eid).uuids.retract(uuid3, uuid4).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2)

      // Retract Seq of values
      Ns(eid).uuids.retract(Seq(uuid2)).update
      Ns.uuids.get.head.toList === List(uuid1)

      // Retract Seq of values as variable
      val values = Seq(uuid1)
      Ns(eid).uuids.retract(values).update
      Ns.uuids.get === List()

      // Retracting empty Seq of values has no effect
      Ns(eid).uuids(uuid1).update
      Ns(eid).uuids.retract(Seq[UUID]()).update
      Ns.uuids.get.head.toList === List(uuid1)
    }


    "apply" in new CoreSetup {

      val eid = Ns.uuids(uuid2, uuid3).save.eid

      // Apply value (retracts all current values!)
      Ns(eid).uuids(uuid1).update
      Ns.uuids.get.head.toList === List(uuid1)

      // Apply multiple values (vararg)
      Ns(eid).uuids(uuid2, uuid3).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid2, uuid3)

      // Apply Seq of values
      Ns(eid).uuids(Set(uuid4)).update
      Ns.uuids.get.head.toList === List(uuid4)

      // Apply empty Seq of values (retracting all values!)
      Ns(eid).uuids(Set[UUID]()).update
      Ns.uuids.get === List()

      // Apply Seq of values as variable
      val values = Set(uuid1, uuid2)
      Ns(eid).uuids(values).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2)

      // Delete all (apply no values)
      Ns(eid).uuids().update
      Ns.uuids.get === List()


      // Redundant duplicate values are discarded

      // Equally named variables are coalesced (at compile time)
      Ns(eid).uuids(uuid1, uuid2, uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Equal values are coalesced (at runtime)
      val other3 = uuid3
      Ns(eid).uuids(uuid2, uuid3, other3).update
      Ns.uuids.get.head === Set(uuid2, uuid3)
    }
  }
}
