package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.core.util.testing.expectCompileError
import molecule.datomic.api.in1_out3._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Basics extends TestSpec with Helpers {


  "How it works" in new CoreSetup {
    Ns.int(0).str("x").Ref1.int1(1).save

    // `getObj` is same as `getObjList.head` - presuming one row is returned or that
    // just want the first of more rows
    val o1 = Ns.int.str.Ref1.int1.getObj

    // The above molecule and object getter creates the following code:

    val o2 = new Ns_int with Ns_str with Ns__Ref1[Ref1_int1] {
      override lazy val int: Int    = 0
      override lazy val str: String = "x"
      override def Ref1: Ref1_int1 = new Ref1_int1 {
        override lazy val int1: Int = 1
      }
    }

    // This way, we get type inference in the IDE and can access the data
    // as named object properties, even in referenced namespaces:

    o1.int === 0
    o1.str === "x"
    o1.Ref1.int1 === 1

    o2.int === 0
    o2.str === "x"
    o2.Ref1.int1 === 1

    // We could also get the data as a tuple:
    Ns.int.str.Ref1.int1.get.head === (0, "x", 1)
  }


  "Mandatory - Optional - Tacit" in new CoreSetup {
    Ns.int.str$.bool$ insert List(
      (1, Some("a"), Some(true)),
      (2, Some("b"), None),
      (3, None, None),
    )

    // third row not returned since `str` is tacitly required
    val List(o1, o2) = Ns.int.str_.bool$.getObjList

    o1.int === 1
    o1.bool$ === Some(true)

    o2.int === 2
    o2.bool$ === None
  }


  "Input" in new CoreSetup {
    Ns.int(1).str("a").save

    // Object is created after and thus unaffected by input resolution
    val inputMolecule = m(Ns.int.str(?))
    val o             = inputMolecule("a").getObj
    o.int === 1
    o.str === "a"
  }


  "Mapped/Keyed" in new CoreSetup {
    Ns.int.strMap insert List(
      (1, Map("en" -> "Hi there", "da" -> "Hejsa")),
      (2, Map("en" -> "Hello", "da" -> "Hej")),
    )

    Ns.int_(1).strMap.getObj.strMap === Map("en" -> "Hi there", "da" -> "Hejsa")
    Ns.int_(1).strMap.getObj.strMap("en") === "Hi there"
    Ns.int_(1).strMapK("en").getObj.strMapK === "Hi there"
  }


  "Repeated attributes" in new CoreSetup {
    Ns.int.insert(1, 2, 3)

    // Object will only have 1 property given repeated attributes
    Ns.int.>(1).int.<(3).getObj.int === 2

    // While tuple outputs the value twice
    Ns.int.>(1).int.<(3).get.head === (2, 2)

    // Or you can make one of the attributes tacit
    Ns.int.>(1).int_.<(3).get.head === 2
  }
}
