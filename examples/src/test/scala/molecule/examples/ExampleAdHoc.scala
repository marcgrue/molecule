package molecule.examples
import molecule.examples.dayOfDatomic.SocialNewsSetup
import molecule.util.MoleculeSpec
import molecule.imports._
import molecule.examples.dayOfDatomic.dsl.socialNews._

class ExampleAdHoc extends MoleculeSpec {


  "ad hoc" in new SocialNewsSetup {



    ok
  }

}
