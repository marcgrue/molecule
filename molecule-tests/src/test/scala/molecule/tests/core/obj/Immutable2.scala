//package molecule.tests.core.obj
//
//import molecule.TestSpec
//import molecule.core.util.Helpers
////import molecule.datomic.api.out3._
////import molecule.tests.core.base.dsl.coreTest._
//import scala.language.dynamics
//
//
//class Immutable2 extends TestSpec with Helpers {
//
//  trait Obj[Ref] {
//    //    type ns = Ns0
//    //    val x: ns
//  }
//
//  trait Property
//
//
//  trait A_
//
//  trait A_str {
//    val str: String
//  }
//  trait A_int {
//    val int: Int
//  }
//  trait A_oneB {
//    val oneB: Long
//  }
//
//  trait EmptyRef
//
//  trait A_OneB_0{
//    def B: EmptyRef
//  }
//  trait A_OneB_1[a] {
//    def B: a
//  }
//  trait A_OneB_2[a, b] {
//    def B: a with b
//  }
//
//  trait A_ManyB_0
//  trait A_ManyB_flat_1[a] {
//    def BB: a
//  }
//  trait A_ManyB_flat_2[a, b] {
//    def BB: a with b
//  }
//  trait A_ManyB_nested_1[a] {
//    def BB: List[a]
//  }
//  trait A_ManyB_nested_2[a, b] {
//    def BB: List[a with b]
//  }
//
////  trait B_OneB_0
////  trait B_OneB_1[a] {
////    def B: a
////  }
////  trait B_OneB_2[a, b] {
////    def OneB: a with b
////  }
//
//  trait B_str1 {
//    val str1: String
//  }
//  trait B_int1 {
//    val int1: Int
//  }
//
//  trait B_OneC_0
//  trait B_OneC_1[a] {
//    def C: a
//  }
//  trait B_OneC_2[a, b] {
//    def C: a with b
//  }
//
//  trait B_ManyC_0
//  trait B_ManyC_flat_1[a] {
//    def CC: a
//  }
//  trait B_ManyC_flat_2[a, b] {
//    def CC: a with b
//  }
//  trait B_ManyC_nested_1[a] {
//    def CC: List[a]
//  }
//  trait B_ManyC_nested_2[a, b] {
//    def CC: List[a with b]
//  }
//
//
//
//  trait C_0
//  trait C_1[a] {
//    def C: a
//  }
//  trait C_2[a, b] {
//    def C: a with b
//  }
//
//  trait C_str2 {
//    val str2: String
//  }
//  trait C_int2 {
//    val int2: Int
//  }
//
//
//  "adhoc" in new CoreSetup {
//
//    object attrs {
//
//      // tacit attrs not part of type build-up
//      // Ns.int._str
//      val o1 = new A_str {
//        val str = "a"
//      }
//      o1.str
//
//      // Ns.int.str
//      // (A, B)
//      // a with b
//      val o2 = new A_str with A_int {
//        val str = "a"
//        val int = 1
//      }
//      o2.str
//      o2.int
//
//      // Ns.int.ref1
//      val o3 = new A_str with A_oneB {
//        val str  = "a"
//        val oneB = 42L
//      }
//      o3.str
//      o3.oneB // ref attribute
//    }
//
//
//    object cardOne {
//      // Ns.int.Ref1 or Ns.int.Ref1._int1
//      val o1 = new A_int with A_OneB_0 {
//        val int = 1
//      }
//      o1.int
//
//      // Ns.int.Ref1.int1
//      // (A, B)
//      // a with X[b]
//      val o2 = new A_int with A_OneB_1[B_int1] {
//        val int = 1
//        def B: B_int1 = new B_int1 {
//          val int1 = 11
//        }
//      }
//      o2.int
//      o2.B.int1 // card-one
//
//      // Ns.int.Ref1.int1.str1
//      // (A, B, C)
//      // a with X[b, c]
//      val o3 = new A_int with A_OneB_2[B_int1, B_str1] {
//        val int = 1
//        def B: B_int1 with B_str1 = new B_int1 with B_str1 {
//          val int1 = 11
//          val str1 = "aa"
//        }
//      }
//      o3.int
//      o3.B.int1 // card-one
//      o3.B.str1
//
//      // Ns.int.Ref1.int1.str1.Ref2._int2
//      // (A, B, C)
//      // a with X[b, c]
//      val o4 = new A_int with A_OneB_2[B_int1, B_str1] {
//        val int = 1
//        def B: B_int1 with B_str1 = new B_int1 with B_str1 {
//          val int1 = 11
//          val str1 = "aa"
//        }
//      }
//      o4.int
//      o4.B.int1 // card-one:
//      o4.B.str1
//
//      // Ns.int.Ref1.int1.str1.Ref2.int2
//      // (A, B, C, D)
//      // a with X[b with c with Y[d]]
//      val o5 = new A_int with A_OneB_2[B_int1, B_str1 with B_OneC_1[C_int2]]  {
//        val int = 1
//        def B: B_int1 with B_str1 with B_OneC_1[C_int2] = new B_int1 with B_str1 with B_OneC_1[C_int2] {
//          val int1 = 11
//          val str1 = "aa"
//          def C: C_int2 = new C_int2 {
//            val int2 = 111
//          }
//        }
//      }
//      o5.int
//      o5.B      // what about this??
//      o5.B.int1 // card-one
//      o5.B.str1
//      o5.B.C
//      o5.B.C.int2
//    }
//
//
//    object composite {
//      // Ns.int.+(Ref2._int2)
//      // A
//      // a
//      val o1 = new A_int with C_0 {
//        val int = 1
//      }
//      o1.int
//
//      // Ns.int.+(Ref2.int2)
//      // (a, b)
//      // a with X[b]
//      val o2 = new A_int with C_1[C_int2] {
//        val int = 1
//        def C: C_int2 = new C_int2 {
//          val int2 = 11
//        }
//      }
//      o2.int
//      o2.C.int2 // composite - same as card-one
//
//      // Ns.int.Refs1.int1.str1
//      // (a, (b, c))
//      // a with X[b with c]
//      val o3 = new A_int with C_2[C_int2, C_str2] {
//        val int = 1
//        def C: C_int2 with C_str2 = new C_int2 with C_str2 {
//          val int2 = 11
//          val str2 = "aa"
//        }
//      }
//      o3.int
//      o3.C.int2 // composite - same as card-one
//      o3.C.str2
//    }
//
//
//    object cardManyFlat {
//      // Ns.int.Refs1 or Ns.int.Refs1._int1
//      val o1 = new A_int with A_ManyB_0 {
//        val int = 1
//      }
//      o1.int
//
//      // Ns.int.Refs1.int1
//      val o2 = new A_int with A_ManyB_flat_1[B_int1] {
//        val int = 1
//        def BB: B_int1 = new B_int1 {
//          val int1 = 11
//        }
//      }
//      o2.int
//      o2.BB.int1 // card-many flat
//
//      // Ns.int.Refs1.int1.str1
//      val o3 = new A_int with A_ManyB_flat_2[B_int1, B_str1] {
//        val int = 1
//        def BB: B_int1 with B_str1 = new B_int1 with B_str1 {
//          val int1 = 11
//          val str1 = "aa"
//        }
//      }
//      o3.int
//      o3.BB.int1 // card-many flat
//      o3.BB.str1
//    }
//
//
//    object cardManyNested {
//      // Ns.int.Refs1
//      val o1 = new A_int with A_ManyB_0 {
//        val int = 1
//      }
//      o1.int
//
//      // Ns.int.Refs1.*(Ref1.int1)
//      val o2 = new A_int with A_ManyB_nested_1[B_int1] {
//        val int = 1
//        def BB: List[B_int1] = List(new B_int1 {
//          val int1 = 11
//        })
//      }
//      o2.int
//      o2.BB.head.int1 // card-many nested
//
//      // Ns.int.Refs1.*(Ref1.int1.str1)
//      val o3 = new A_int with A_ManyB_nested_2[B_int1, B_str1] {
//        val int = 1
//        def BB: List[B_int1 with B_str1] = List(new B_int1 with B_str1 {
//          val int1 = 11
//          val str1 = "aa"
//        })
//      }
//      o3.int
//      o3.BB.head.int1 // card-many nested
//      o3.BB.head.str1
//    }
//
//
//
//    object Ns extends Ns_0
//    trait Ns_0 {
//      val int: Ns_1[Int, A_int] = ???
//    }
//
//    trait Ns_1[A, a] {
//      def Ref1: Ref1_1[A, a, A_OneB_1[_]] = ???
//    }
//
//    // Pair[SomeNs[_], T]
//
//    trait Pair[Obj, Prop] {
//
//    }
////    trait Obj[TT]
////    trait Obj1[T] extends Obj[T]
////    trait Obj2[T] extends Obj[T]
////    trait Pair[] =
//
////    trait NS_0_02[A, B, a, b, c]
////    trait NS_0_02[A, B, a, b[_]]
//    trait NS_0_02[A, B, Pair, Pair]
//
//    trait Ref1_1[A, a, r1] {
//      val str1: NS_0_02[A, String, a, r1[B_str1]] = ???
//    }
//
////    trait Ref1_2[A, B, a, r1[b], b] {
////
////    }
//
//    val m: NS_0_02[Int, String, A_int, A_OneB_1[_][B_str1]] = Ns.int.Ref1.str1
//
//
//    def get[A, B, a, b[_], c](m: NS_0_02[A, B, a, b, c]) = {
//      val tpl: (A, B) = ???
//      val obj: a with b[c] = ???
//      (tpl, obj)
//    }
//
//    val o = get(m)._2
//    o.int
//    o.B.
//
//
//
//
//
//    ok
//  }
//}
