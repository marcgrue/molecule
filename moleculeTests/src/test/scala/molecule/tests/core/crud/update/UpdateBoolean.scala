package molecule.tests.core.crud.update

import molecule.core.ops.exception.VerifyModelException
import molecule.tests.core.base.dsl.coreTest._
import molecule.datomic.api.out1._
import molecule.TestSpec

class UpdateBoolean extends TestSpec {

  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bool(true).save.eid

      // Apply value (retracts current value)
      Ns(eid).bool(false).update
      Ns.bool.get.head === false

      // Delete value (apply no value)
      Ns(eid).bool().update
      Ns.bool.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).bool(true, false).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bool(true, false)"
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bool(bool2).save.eid

      // Apply value (retracts current value)
      Ns(eid).bool(bool1).update
      Ns.bool.get.head === bool1

      // Delete value (apply no value)
      Ns(eid).bool().update
      Ns.bool.get === List()


      // Applying multiple values to card-one attribute not allowed

      (Ns(eid).bool(bool2, bool3).update must throwA[VerifyModelException])
        .message === "Got the exception molecule.core.ops.exception.VerifyModelException: " +
        "[noConflictingCardOneValues]  Can't update multiple values for cardinality-one attribute:" +
        s"\n  Ns ... bool($bool2, $bool3)"
    }
  }


  // Using card-many attributes with Sets of boolean values doesn't make sense since Sets will
  // coalesce any input to sets of only true/false values. Use boolean map attributes if you want
  // to keep track of multiple true/false values for one parameter.
}
