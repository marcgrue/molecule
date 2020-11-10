package molecule.coretests.errors

import molecule.core.util.Helpers
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest.Ns
import molecule.coretests.util.schema.CoreTestSchema
import molecule.datomic.peer.api.in1_out1._


class runtime extends CoreSpec with Helpers {

  sequential

  implicit val conn = recreateDbFrom(CoreTestSchema)


  "Cannot resolve overloaded method 'inputMolecule'" >> {

    // Missing explicit `m` to create input molecule
    // val inputMolecule1 = Ns.int(?)

    // inferred: Cannot resolve overloaded method 'inputMolecule'
    // compiled: overloaded method value apply with alternatives:
    // inputMolecule1(42)

    // ok
    val inputMolecule2 = m(Ns.str(?))
    inputMolecule2("Ben")

    ok
  }

}