package molecule.tests.core.obj

import molecule.core.util.Helpers
import molecule.datomic.api.in1_out10._
import molecule.setup.TestSpec
import molecule.tests.core.base.dsl.CoreTest._


class Ref extends TestSpec with Helpers {


  "ref/backref" in new CoreSetup {
    Ns.int(0).str("a")
      .Ref1.int1(1).str1("b")._Ns
      .Refs1.int1(11) //.str1("bb")
      .Ref2.int2(2).str2("c")._Ref1
      .Refs2.int2(22)
      .save

    {
      val o = Ns.int.Ref1.int1.getObj
      o.int === 0
      o.Ref1.int1 === 1
    }
    {
      val o = Ns.int.Ref1.int1._Ns.str.getObj
      o.int === 0
      o.Ref1.int1 === 1
      o.str === "a"
    }
//    {
//      val o = Ns.int.Ref1.int1._Ns.Refs1.str1.getObj
//      o.int === 0
//      o.Ref1.int1 === 1
//      o.Refs1.str1 === "b"
//    }
//    {
//      val o = Ns.int.Ref1.int1.Ref2.int2._Ref1.str1.getObj
//      o.int === 0
//      o.Ref1.int1 === 1
//      o.Ref1.Ref2.int2 === "c"
//      o.Ref1.str1 === "b"
//    }
//    {
//      val o = Ns.int.Ref1.Ref2.str2._Ref1.str1.getObj
//      o.int === 0
//      o.Ref1.Ref2.str2 === "c"
//      o.Ref1.str1 === "b"
//    }
//    {
//      val o = Ns.int.Ref1.int1.Ref2.str2._Ref1.Refs2.int2.getObj
//      o.int === 0
//      o.Ref1.int1 === 1
//      o.Ref1.Ref2.str2 === "c"
//      o.Ref1.Refs2.int2 === 2
//    }
//    {
//      val o = Ns.int.Ref1.Ref2.str2._Ref1.Refs2.int2.getObj
//      o.int === 0
//      o.Ref1.Ref2.str2 === "c"
//      o.Ref1.Refs2.int2 === "c"
//    }
//    {
//      val o = Ns.int.Ref1.int1.Ref2.str2._Ref1.str1._Ns.str.getObj
//      o.int === 0
//      o.Ref1.int1 === 1
//    }
//    {
//      val o = Ns.int.Ref1.int1.Ref2.str2._Ref1._Ns.str.getObj
//      o.int === 0
//      o.Ref1.int1 === 1
//    }
    //    {
    //      val o = Ns.int.Ref1.Ref2.str2._Ref1._Ns.str.getObj
    //      o.int === 0
    //      o.Ref1.int1 === 1
    //    }
    //    {
    //      val o = Ns.int.Ref1.int1.Ref2.str2._Ref1.str1._Ns.Refs1.str1.getObj
    //      o.int === 0
    //      o.Ref1.int1 === 1
    //    }
    //    {
    //      val o = Ns.int.Ref1.int1.Ref2.str2._Ref1._Ns.Refs1.str1.getObj
    //      o.int === 0
    //      o.Ref1.int1 === 1
    //    }
    //    {
    //      val o = Ns.int.Ref1.Ref2.str2._Ref1._Ns.Refs1.str1.getObj
    //      o.int === 0
    //      o.Ref1.int1 === 1
    //    }
  }
}