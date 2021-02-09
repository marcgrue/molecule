package molecule

import molecule.core.dsl.attributes
import molecule.core.util.Helpers
import molecule.datomic.api.in1_out5._
import molecule.datomic.base.facade.Conn
import molecule.datomic.peer.facade.Datomic_Peer
import molecule.tests.core.base.schema.CoreTestSchema
import org.specs2.mutable.Specification


//class AdHocTest extends TestSpec with Helpers {
  class AdHocTest extends Specification {


  //  "Simple hyperedge" in new GraphSetup {
  //
  //    import molecule.tests.examples.datomic.dayOfDatomic.dsl.Graph._
  //
  //    // User 1 Roles in Group 2
  //    User.name_("User1")
  //      .RoleInGroup.Group.name_("Group2")
  //      ._RoleInGroup.Role.name.inspectGet
  //
  //
  //    ok
  //  }

  //  "adhoc" in new BidirectionalSetup {
  //    import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //
  //        val animalsCloseTo = m(Person.name_(?).CloseTo.*(CloseTo.weight.Animal.name))
  //  }
  //
//
//
//
//  "adhoc" in new CoreSetup {
//
//  }



  "adhoc" >> {

    import molecule.tests.core.base.dsl.CoreTest._
    implicit val conn: Conn = Datomic_Peer.recreateDbFrom(CoreTestSchema)
//    m(Ns.str.Ref1.str1._Ns.Refs1.int1)

    //    {
    //      val o = m(Ns.float.Refs1.*(Ref1.int1).RefsSub1.str1).getObjList.head
    //      o.float
    //      o.Refs1.head.int1
    //      o.RefsSub1.str1
    //    }
    //
    //    val o = m(Ns.float.Ref1.int1.Refs2.*(Ref2.int2)).getObjList.head
    //    o.float
    //    o.Ref1.int1
    //    o.Ref1.Refs2.head.int2

    Ns.str.Ref1.str1._Ns.Refs1.int1.inspectInsert(
      List(
        ("a", "a1", 1),
        ("b", "b1", 2)
      )
    )
//try {
//} catch {
//  case e: Throwable =>
//    println(e)
//    throw e
//}

    //      .insert.apply(
//      List(
//        ("a", "a1", 1),
//        ("b", "b1", 2)
//      )
//    )


    ok
  }
  //
  //
  //  "A first query" in new SeattleSetup {
  //    import molecule.tests.examples.datomic.seattle.dsl.Seattle._
  //
  //    // A Community-name molecule
  //    val communities = m(Community. e.name_)
  //
  //    // We have 150 communities
  //    communities.get.size === 150
  //  }

  //  "adhoc" in new BidirectionalSetup {
  //import molecule.tests.core.bidirectionals.dsl.Bidirectional._
  //
  //  }

  //  "adhoc" in new PartitionSetup {

  //  }


  //  "example adhoc" in new SeattleSetup {
  //
  //
  //    //    ok
  //  }


  //  "example adhoc" in new SocialNewsSetup {
  //
  //
  //
  //    ok
  //  }
}
//