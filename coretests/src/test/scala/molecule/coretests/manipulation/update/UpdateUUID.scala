package molecule.coretests.manipulation.update

import java.util.UUID

import molecule.Imports._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
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

      (Ns(eid).uuid(uuid2, uuid3).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.ops.VerifyModel.noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... uuid($uuid2, $uuid3)"
    }
  }


  "Card-many variables" >> {

    "add" in new CoreSetup {

      val eid = Ns.uuids(uuid1).save.eid

      // Add value
      Ns(eid).uuids.add(uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Add exisiting value (no effect)
      Ns(eid).uuids.add(uuid2).update
      Ns.uuids.get.head === Set(uuid1, uuid2)

      // Add multiple values
      Ns(eid).uuids.add(uuid3, uuid4).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4)

      // Add Seq of values (existing values unaffected)
      Ns(eid).uuids.add(Seq(uuid4, uuid5)).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Add Seq of values as variable (existing values unaffected)
      val values = Seq(uuid6, uuid7)
      Ns(eid).uuids.add(values).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7)

      // Add empty Seq of values (no effect)
      Ns(eid).uuids.add(Seq[UUID]()).update
      Ns.uuids.get.head === Set(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6, uuid7)


      // Can't add duplicate values

      // vararg
      expectCompileError(
        """Ns(eid).uuids.add(uuid5, uuid5, uuid6, uuid6, uuid7).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/uuids`:" +
          "\n__ident__uuid6" +
          "\n__ident__uuid5")

      // Seq
      expectCompileError(
        """Ns(eid).uuids.add(Seq(uuid5, uuid5, uuid6, uuid6, uuid7)).update""",
        "[Dsl2Model:apply (11)] Can't add duplicate values to attribute `:ns/uuids`:" +
          "\n__ident__uuid6" +
          "\n__ident__uuid5")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val (other5, other6) = (uuid5, uuid6)

      // vararg
      (Ns(eid).uuids.add(other5, uuid5).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/uuids`:" +
        "\n" + uuid5

      // Seq
      (Ns(eid).uuids.add(Seq(other5, uuid5)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't add duplicate new values to attribute `:ns/uuids`:" +
        "\n" + uuid5
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

      (Ns(eid).uuids.replace(uuid7 -> uuid8, uuid8 -> other8).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uuids`:" +
        "\n" + uuid8

      // Conflicting new values
      (Ns(eid).uuids.replace(Seq(uuid7 -> uuid8, uuid8 -> other8)).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't replace with duplicate new values of attribute `:ns/uuids`:" +
        "\n" + uuid8
    }


    "remove" in new CoreSetup {

      val eid = Ns.uuids(uuid1, uuid2, uuid3, uuid4, uuid5, uuid6).save.eid

      // Remove value
      Ns(eid).uuids.remove(uuid6).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Removing non-existing value has no effect
      Ns(eid).uuids.remove(uuid7).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4, uuid5)

      // Removing duplicate values removes the distinc value
      Ns(eid).uuids.remove(uuid5, uuid5).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2, uuid3, uuid4)

      // Remove multiple values (vararg)
      Ns(eid).uuids.remove(uuid3, uuid4).update
      Ns.uuids.get.head.toList.sortBy(_.toString) === List(uuid1, uuid2)

      // Remove Seq of values
      Ns(eid).uuids.remove(Seq(uuid2)).update
      Ns.uuids.get.head.toList === List(uuid1)

      // Remove Seq of values as variable
      val values = Seq(uuid1)
      Ns(eid).uuids.remove(values).update
      Ns.uuids.get === List()

      // Removing empty Seq of values has no effect
      Ns(eid).uuids(uuid1).update
      Ns(eid).uuids.remove(Seq[UUID]()).update
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


      // Can't apply duplicate values

      expectCompileError(
        """Ns(eid).uuids(uuid2, uuid2, uuid3, uuid4, uuid3).update""",
        "[Dsl2Model:apply (13)] Can't apply duplicate values to attribute `:ns/uuids`:" +
          "\n__ident__uuid3" +
          "\n__ident__uuid2")

      // If duplicate values are added with non-equally-named variables we can still catch them at runtime
      val other2 = uuid2

      (Ns(eid).uuids(uuid2, other2).update must throwA[IllegalArgumentException])
        .message === "Got the exception java.lang.IllegalArgumentException: " +
        "[molecule.transform.Model2Transaction.valueStmts:default]  Can't apply duplicate new values to attribute `:ns/uuids`:" +
        "\n" + uuid2
    }
  }
}
