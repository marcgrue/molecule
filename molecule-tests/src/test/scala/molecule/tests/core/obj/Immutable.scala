//package molecule.tests.core.obj
//
//import molecule.TestSpec
//import molecule.core.util.Helpers
//import molecule.datomic.api.out3._
//import molecule.tests.core.base.dsl.CoreTest._
//import scala.language.dynamics
//
//
//class Immutable extends TestSpec with Helpers {
//
//
//  trait Ns_str {
//    val str: String
//  }
//  trait Ns_str$ {
//    val str$: Option[String]
//  }
//  trait Ns_int {
//    val int: Int
//  }
//  trait Ns_Refs[Sub] {
//    def Refs: List[Sub]
//  }
//  trait Ref1_str1 {
//    val str1: String
//  }
//  trait Ref1_int1 {
//    val int1: Int
//  }
//
//  trait tpe1 extends Ns_int with Ns_str
//  case class obj1(
//    int: Int,
//    str: String
//  ) extends tpe1
//
//
//
//  "adhoc" in new CoreSetup {
//
////    val o: obj1 = obj1(1, "a")
////
////    val o2: Ns_str with Ns_int =
////      new Ns_str
////        with Ns_int {
////        val str: String = "a"
////        val int: Int    = 1
////      }
////
////    o.int === 1
////
////    val i: Int = o.int
//
////    Ns.int.inspectGet
////    Ns.int.get
////
////    m(Ns.str.int).getObjList
//
//
//    Ns.int(1).save
//
//    Ns.int.get.head === 1
//
//    ok
//  }
//}
