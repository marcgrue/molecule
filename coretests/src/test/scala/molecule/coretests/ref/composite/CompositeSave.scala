package molecule.coretests.ref.composite

import molecule.api.in3_out22._
import molecule.coretests.util.CoreSpec
import molecule.coretests.util.dsl.coreTest._
import molecule.util.expectCompileError

class CompositeSave extends CoreSpec {


  "Save" in new CoreSetup {
    // 1 + 1
    m(Ns.int(1) + Ref2.int2(11)).save
    m(Ns.int(1) + Ref2.int2(11)).get.head === (1, 11)

    // n + 1
    m(Ns.int(2).str("b") + Ref2.int2(22)).save
    m(Ns.int(2).str("b") + Ref2.int2(22)).get.head === ((2, "b"), 22)

    // 1 + n
    m(Ns.int(3) + Ref2.int2(33).str2("cc")).save
    m(Ns.int(3) + Ref2.int2(33).str2("cc")).get.head === (3, (33, "cc"))

    // n + n
    m(Ns.int(4).str("d") + Ref2.int2(44).str2("dd")).save
    m(Ns.int(4).str("d") + Ref2.int2(44).str2("dd")).get.head === ((4, "d"), (44, "dd"))

    // All sub-molecules share the same entity id
    val e5 = m(Ns.int(5).Ref1.int1(55) + Ref2.int2(555)).save.eid
    Ns(e5).int.Ref1.int1.get.head === (5, 55)
    Ref2(e5).int2.get.head === 555
    m(Ns(e5).int.Ref1.int1 + Ref2.int2).get.head === ((5, 55), 555)

    // Sub-molecules can point straight to other entities (without any attributes of their own)
    val e6 = m(Ns.Ref1.int1(6) + Ref2.int2(66)).save.eid
    m(Ns(e6).Ref1.int1).get.head === 6
    m(Ref2(e6).int2).get.head === 66
    m(Ns(e6).Ref1.int1 + Ref2.int2).get.head === (6, 66)
  }
}