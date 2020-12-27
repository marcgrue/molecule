//package molecule.tests.core.ref
//
//import datomicClient.ClojureBridge
//import molecule.core.util.Helpers
//import molecule.datomic.api.out7._
//import molecule.tests.core.base.dsl.coreTest._
//import molecule.TestSpec
//
//
//class NewNestedNotation extends TestSpec with Helpers with ClojureBridge {
//
//  tests = 1
//
//  "1 + 1$" in new CoreSetup {
//
//    Ns.long.int$ debugInsert Seq(
//      // Two rows of data
//      (1, Some(11)),
//      (2, None)
//    )
//
//    Ref2.int2 + Ns.int$ debugInsert Seq(
//      // Two rows of data
//      (1, Some(11)),
//      (2, None)
//    )
//
//
//    // Composite of two single-value molecules
//    val List(e1, e2) = Ref2.int2 + Ns.int$ insert Seq(
//      // Two rows of data
//      (1, Some(11)),
//      (2, None)
//    ) eids
//
//    // Two entities created
//    e1.touchList === List(
//      ":db/id" -> e1,
//      ":Ns/int" -> 11,
//      ":Ref2/int2" -> 1
//    )
//    e2.touchList === List(
//      ":db/id" -> e2,
//      ":Ref2/int2" -> 2
//    )
//
//    // Queries via each namespace
//    m(Ref2.int2).get.sorted === Seq(1, 2)
//    m(Ns.int).get.sorted === Seq(11)
//
//    // Composite query
//    m(Ref2.int2 + Ns.int).get.sorted === Seq(
//      (1, 11)
//    )
//    m(Ref2.int2 + Ns.int$).get.sorted === Seq(
//      (1, Some(11)),
//      (2, None)
//    )
//  }
//
//  //  "adhoc" in new CoreSetup {
//  //
//  //    m(Ns.int.Refs1 * Ref1.int1.str1$) insert List(
//  //      (1, List((1, Some("a")), (2, None))),
//  //      (2, List())
//  //    )
//  //
//  //    Ns.int.Refs1.*(Ref1.int1.str1$).get === List(
//  //      (1, List((1, Some("a")), (2, None))),
//  //    )
//  //
//  //
//  //    // Is this possible? Optional associative relationship...
//  //    // No! Refs1 should only be followed by Ref1 attributes!
//  //    // use + and +? instead...
//  //    //     +*    +*?  maybe too?
//  //    m(Ns.int.Refs1 * Ref2.int2.str2$) insert List(
//  //      (1, List((1, Some("a")), (2, None))),
//  //      (2, List())
//  //    )
//  //
//  //    Ns.int.Refs1.*(Ref2.int2.str2$).get === List(
//  //      (1, List((1, Some("a")), (2, None))),
//  //    )
//  //
//  //    //    Ns.int.Refs1.*.int1.str1$.get === List(
//  //    //      (1, List((1, Some("a")), (2, None))),
//  //    //    )
//  //
//  //    Ns.int.Refs1.int1.str1$.get === List(
//  //      (1, 1, Some("a")),
//  //      (1, 2, None)
//  //    )
//  //
//  //    object Ns1 {
//  //      val int = this
//  //      def Refs1 = new Refs1x {}
//  //
//  //      class Refs1x {
//  //        def * : Ns1.Refs1x = this
//  //        def *? : Ns1.Refs1x = this
//  //
//  //        // Optional associative relationship?
//  //        def +?[T](x: T): T = ???
//  //
//  //        def ++[T](x: T): T = ???
//  //        def ++?[T](x: T): T = ???
//  //
//  //        def +*[T](x: T): T = ???
//  //        def +*? [T](x: T): T = ???
//  //
//  //        def ##[T](x: T): T = ???
//  //
//  //        def &[T](x: T): T = ???
//  //
//  //        val int1 = this
//  //        val str1 = this
//  //
//  //      }
//  //    }
//  //
//  //    object Ref2x {
//  //
//  //      val int2 = this
//  //      val str2 = this
//  //    }
//  //
//  //    // flat
//  //    Ns1.int.Refs1.int1.str1
//  //
//  //    // nested
//  //    Ns1.int.Refs1.*.int1.str1
//  //
//  //    // optional nested
//  //    Ns1.int.Refs1.*?.int1.str1
//  //
//  //
//  //    // Associative relationships...
//  //
//  //    // associative
//  //    m(Ns.int + Ref2.int2.str2). get === 7
//  //
//  //
//  //    // optional associative
//  //    Ns1.int.Refs1 +? int1
//  //
//  //    Ns1.int.Refs1 ++ Ref2x.int2.str2
//  //
//  //    Ns1.int.Refs1 ++? Ref2x.int2.str2
//  //
//  //
//  //    Ns1.int.Refs1 +* Ref2.int2.str2
//  //
//  //    Ns1.int.Refs1 +*? Ref2.int2.str2
//  //
//  //
//  //
//  //    Ns1.int.Refs1 & Ref2x.int2.str2
//  //
//  //
//  //    Ns1.int.Refs1 ## Ref2x.int2.str2
//  //
//  //
//  //
//  //
//  //
//  //    ok
//  //  }
//
//  //  "adhoc" in new BidirectionalSetup {
//  //import molecule.tests.core.bidirectionals.dsl.bidirectional._
//  //
//  //  }
//
//  //  "adhoc" in new PartitionSetup {
//
//  //  }
//
//
//  //  "example adhoc" in new SeattleSetup {
//  //
//  //
//  //    //    ok
//  //  }
//
//
//  //  "example adhoc" in new SocialNewsSetup {
//  //
//  //
//  //
//  //    ok
//  //  }
//}
