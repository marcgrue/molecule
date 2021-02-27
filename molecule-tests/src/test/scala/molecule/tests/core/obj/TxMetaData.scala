package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.out5._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class TxMetaData extends TestSpec with Helpers {

  "Attr + meta" in new CoreSetup {
    Ns.int(0).Tx(Ref1.int1(1)).save
    val o = Ns.int.Tx(Ref1.int1).getObj
    o.int === 0
    o.Tx.Ref1.int1 === 1
  }

  "Ref + meta" in new CoreSetup {
    Ns.int(0).Ref1.int1(1).Tx(Ref2.int2(2)).save
    val o = Ns.int.Ref1.int1.Tx(Ref2.int2).getObj
    o.int === 0
    o.Ref1.int1 === 1
    // Note how Tx attach to the base namespace (Ns)
    o.Tx.Ref2.int2 === 2
  }

  "Refs + meta" in new CoreSetup {
    Ns.int(0).Ref1.int1(1).Ref2.int2(2).Tx(Ref3.int3(3)).save
    val o = Ns.int.Ref1.int1.Ref2.int2.Tx(Ref3.int3).getObj
    o.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Ref2.int2 === 2
    o.Tx.Ref3.int3 === 3
  }

  "Ref + meta composites" in new CoreSetup {
    Ns.int(0).Ref1.int1(1).Tx(Ref2.int2(2) + Ref3.int3(3)).save
    val o = Ns.int.Ref1.int1.Tx(Ref2.int2 + Ref3.int3).getObj
    o.int === 0
    o.Ref1.int1 === 1
    o.Tx.Ref2.int2 === 2
    o.Tx.Ref3.int3 === 3
  }

  "Refs + meta composites" in new CoreSetup {
    Ns.int(0).Ref1.int1(1).Ref2.int2(2).Tx(Ref3.int3(3) + Ref4.int4(4)).save
    val o = Ns.int.Ref1.int1.Ref2.int2.Tx(Ref3.int3 + Ref4.int4).getObj
    o.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Ref2.int2 === 2
    o.Tx.Ref3.int3 === 3
    o.Tx.Ref4.int4 === 4
  }

  "Composite + meta" in new CoreSetup {
    (Ns.int(0) + Ref1.int1(1).Tx(Ref2.int2(2))).save
    val o = (Ns.int + Ref1.int1.Tx(Ref2.int2)).getObj
    // All properties are namespaced
    o.Ns.int === 0
    o.Ref1.int1 === 1
    // Note how Tx attach to the last composite
    o.Ref1.Tx.Ref2.int2 === 2
  }

  "Composite + meta ref" in new CoreSetup {
    (Ns.int(0) + Ref1.int1(1).Tx(Ref2.int2(2).Ref3.int3(3))).save
    val o = (Ns.int + Ref1.int1.Tx(Ref2.int2.Ref3.int3)).getObj
    o.Ns.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Tx.Ref2.int2 === 2
    o.Ref1.Tx.Ref2.Ref3.int3 === 3
  }

  "Composite/ref + meta ref" in new CoreSetup {
    (Ns.int(0) + Ref1.int1(1).Ref2.int2(2).Tx(Ref3.int3(3))).save
    val o = (Ns.int + Ref1.int1.Ref2.int2.Tx(Ref3.int3)).getObj
    o.Ns.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Ref2.int2 === 2
    o.Ref1.Tx.Ref3.int3 === 3
  }

  "Composite/refs + meta ref" in new CoreSetup {
    (Ns.int(0) + Ref1.int1(1).Ref2.int2(2).Ref3.int3(3).Tx(Ref4.int4(4))).save
    val o = (Ns.int + Ref1.int1.Ref2.int2.Ref3.int3.Tx(Ref4.int4)).getObj
    o.Ns.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Ref2.int2 === 2
    o.Ref1.Ref2.Ref3.int3 === 3
    o.Ref1.Tx.Ref4.int4 === 4
  }

  "Composite/ref + meta composite" in new CoreSetup {
    (Ns.int(0) + Ref1.int1(1).Ref2.int2(2).Tx(Ref3.int3(3) + Ref4.int4(4))).save
    val o = (Ns.int + Ref1.int1.Ref2.int2.Tx(Ref3.int3 + Ref4.int4)).getObj
    o.Ns.int === 0
    o.Ref1.int1 === 1
    o.Ref1.Ref2.int2 === 2
    o.Ref1.Tx.Ref3.int3 === 3
    o.Ref1.Tx.Ref4.int4 === 4
  }
}
