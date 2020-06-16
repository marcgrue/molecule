package molecule.coretests


import molecule.api.out10._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.Helpers
import scala.collection.mutable.ListBuffer


class AdHocTest extends CoreSpec with Helpers {
  sequential


  "Adhoc" in new CoreSetup {

    Ns.int(1).save

    Ns.int.get === List(1)

    val lb = new ListBuffer[Int]

    lb += 1

    val l = List(1)

    Ns.ints.apply(l).save
    Ns.ints.apply(lb).save
    Ns.int.apply(lb).save

//    Ns.ints(lb).debugGet

    ok
  }
}