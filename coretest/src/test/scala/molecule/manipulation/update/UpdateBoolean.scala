package molecule.manipulation.update

import java.util.concurrent.ExecutionException

import molecule._
import molecule.util.dsl.coreTest._
import molecule.util.{CoreSetup, CoreSpec, expectCompileError}

class UpdateBoolean extends CoreSpec {


  "Card-one values" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bool(true).save.eid

      // Apply value (retracts current value)
      Ns(eid).bool(false).update
      Ns.bool.one === false

      // Delete value (apply no value)
      Ns(eid).bool().update
      Ns.bool.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).bool(true, false).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/bool`:" +
          "\ntrue" +
          "\nfalse")
    }
  }


  "Card-one variables" >> {

    "apply" in new CoreSetup {

      val eid = Ns.bool(bool2).save.eid

      // Apply value (retracts current value)
      Ns(eid).bool(bool1).update
      Ns.bool.one === bool1

      // Delete value (apply no value)
      Ns(eid).bool().update
      Ns.bool.get === List()


      // Applying multiple values to card-one attribute not allowed

      expectCompileError(
        """Ns(eid).bool(bool2, bool3).update""",
        "[Dsl2Model:apply (10)] Can't apply multiple values to card-one attribute `:ns/bool`:" +
          "\n__ident__bool2" +
          "\n__ident__bool3")
    }
  }


  // Using card-many attributes with Sets of boolean values doesn't make sense since Sets will
  // coalesce any input to sets of only true/false values. Use boolean map attributes if you want
  // to keep track of multiple true/false values for one parameter.
}
