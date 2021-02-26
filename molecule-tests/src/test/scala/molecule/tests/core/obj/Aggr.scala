package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.core.util.testing.expectCompileError
import molecule.datomic.api.in1_out3._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Aggr extends TestSpec with Helpers {

  class Setup extends CoreSetup {
    Ns.int.insert(1, 2, 3)
    Ns.double.insert(1.0, 2.0, 3.0)
    Ns.str.insert("a", "b", "c")
  }


  "min, max, rand, sample, median" in new Setup {
    // For any property

    Ns.int(min).getObj.int === 1
    Ns.int(max).getObj.int === 3
    Ns.int(rand).getObj.int // 1, 2 or 3
    Ns.int(sample).getObj.int // 1, 2 or 3
    Ns.int(median).getObj.int === 2

    Ns.double(min).getObj.double === 1.0
    Ns.double(max).getObj.double === 3.0
    Ns.double(rand).getObj.double // 1.0, 2.0 or 3.0
    Ns.double(sample).getObj.double // 1.0, 2.0 or 3.0
    Ns.double(median).getObj.double === 2.0

    Ns.str(min).getObj.str === "a"
    Ns.str(max).getObj.str === "c"
    Ns.str(rand).getObj.str // a, b or c
    Ns.str(sample).getObj.str // a, b or c
    Ns.str(median).getObj.str === "b"
  }


  "sum" in new Setup {
    // For number properties

    Ns.int(sum).getObj.int === 6
    Ns.double(sum).getObj.double === 6.0

    expectCompileError(
      """m(Ns.str(sum))""",
      "molecule.core.transform.exception.Dsl2ModelException: " +
        "Can't apply `sum` aggregate to non-number attribute `str` of type `String`.")
  }


  "count, countDistinct" in new Setup {
    // For Int properties

    Ns.int(count).getObj.int === 3
    Ns.int(countDistinct).getObj.int === 3

    (Ns.double(count).getObj.double must throwA[molecule.core.exceptions.MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Object property `double` not available since the aggregate changes its type to `Int`. " +
      "Please use tuple output instead to access aggregate value."

    // Tuple output allowed
    Ns.double(count).get.head === 3
    Ns.double(countDistinct).get.head === 3
    Ns.str(count).get.head === 3
    Ns.str(countDistinct).get.head === 3
  }


  "avg, variance, stddev" in new Setup {
    // For Double properties

    Ns.double(avg).getObj.double === 2.0
    Ns.double(variance).getObj.double === 0.6666666666666666
    Ns.double(stddev).getObj.double === 0.816496580927726

    (Ns.int(avg).getObj.int must throwA[molecule.core.exceptions.MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Object property `int` not available since the aggregate changes its type to `Double`. " +
      "Please use tuple output instead to access aggregate value."

    // Tuple output allowed
    Ns.int(avg).get.head === 2.0
    Ns.int(variance).get.head === 0.6666666666666666
    Ns.int(stddev).get.head === 0.816496580927726
  }


  "Aggregates returning multiple values" in new Setup {
    // Can be accessed as tuple data only

    (Ns.int(min(2)).getObj.int must throwA[molecule.core.exceptions.MoleculeException])
      .message === "Got the exception molecule.core.exceptions.package$MoleculeException: " +
      "Object property `int` not available since the aggregate changes its type to `List[Int]`. " +
      "Please use tuple output instead to access aggregate value."

    // Tuple output allowed
    Ns.int(min(2)).get.head.sorted === List(1, 2)
    Ns.int(max(2)).get.head.sorted === List(2, 3)
    Ns.int(distinct).get.head.sorted === List(1, 2, 3)
    Ns.int(rand(2)).get.head // two of 1, 2 or 3
    Ns.int(sample(2)).get.head // two of 1, 2 or 3

    Ns.str(min(2)).get.head.sorted === List("a", "b")
    Ns.str(max(2)).get.head.sorted === List("b", "c")
    Ns.str(distinct).get.head.sorted === List("a", "b", "c")
    Ns.str(rand(2)).get.head // two of a, b or c
    Ns.str(sample(2)).get.head // two of a, b or c
  }
}
