package molecule.coretests

import java.net.URI
import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.schema.CoreTestSchema
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.{InputMolecule_1, InputMolecule_2, InputMolecule_3}
import molecule.input.exception.{InputMoleculeException, InputMolecule_2_Exception}
import molecule.transform.Model2Query
import org.specs2.specification.Scope

// For temporary testing without having to recompile whole packages
class AdHocTest extends CoreSpec {
  sequential


//  class Setup extends Scope {
//    implicit val conn = recreateDbFrom(CoreTestSchema)
//  }

  "t" in new CoreSetup {

    // 3 transaction times `t`
    val t1 = Ns.str("Ann").save.tx
    val t2 = Ns.str("Ben").save.tx
    val t3 = Ns.str("Cay").save.tx

    // Current values as Iterable
    Ns.str.getIterable.iterator.toList === List("Ann", "Ben", "Cay")

    // Ben and Cay added since transaction time t1
    Ns.str.getIterableSince(t1).iterator.toList === List("Ben", "Cay")

    // Cay added since transaction time t2
    Ns.str.getIterableSince(t2).iterator.toList === List("Cay")

    // Nothing added since transaction time t3
    Ns.str.getIterableSince(t3).iterator.toList === Nil
  }

}