package molecule.coretests

import java.net.URI
import molecule.api._
import molecule.coretests.util.dsl.coreTest._
import molecule.coretests.util.{CoreSetup, CoreSpec}
import molecule.input.{InputMolecule_1, InputMolecule_2, InputMolecule_3}
import molecule.input.exception.{InputMoleculeException, InputMolecule_2_Exception}
import molecule.transform.Model2Query
import scala.collection.immutable


class AdHocTest extends CoreSpec